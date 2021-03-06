/*
 * Copyright 2020 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.maven

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.openrewrite.Parser
import org.openrewrite.RecipeTest
import org.openrewrite.maven.tree.Maven
import java.nio.file.Path

class UpgradeDependencyVersionTest : RecipeTest {
    override val parser: Parser<Maven> = MavenParser.builder().resolveOptional(false).build()

    @Test
    fun upgradeVersion() = assertChanged(
        recipe = UpgradeDependencyVersion(
            "org.springframework.boot",
            null as String?,
            "~1.5",
            null as String?),
        before = """
            <project>
              <modelVersion>4.0.0</modelVersion>
              
              <groupId>com.mycompany.app</groupId>
              <artifactId>my-app</artifactId>
              <version>1</version>
              
              <dependencies>
                <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot</artifactId>
                  <version>1.5.1.RELEASE</version>
                </dependency>
              </dependencies>
            </project>
        """,
        after = """
            <project>
              <modelVersion>4.0.0</modelVersion>
              
              <groupId>com.mycompany.app</groupId>
              <artifactId>my-app</artifactId>
              <version>1</version>
              
              <dependencies>
                <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot</artifactId>
                  <version>1.5.22.RELEASE</version>
                </dependency>
              </dependencies>
            </project>
        """
    )

    @Test
    fun upgradeGuava() = assertChanged(
        recipe = UpgradeDependencyVersion(
            "com.google.guava",
            null as String?,
            "25-28",
            "-jre"),
        before = """
            <project>
              <modelVersion>4.0.0</modelVersion>
              
              <groupId>com.mycompany.app</groupId>
              <artifactId>my-app</artifactId>
              <version>1</version>
              
              <dependencies>
                <dependency>
                  <groupId>com.google.guava</groupId>
                  <artifactId>guava</artifactId>
                  <version>25.0-android</version>
                </dependency>
              </dependencies>
            </project>
        """,
        after = """
                <project>
                  <modelVersion>4.0.0</modelVersion>
                  
                  <groupId>com.mycompany.app</groupId>
                  <artifactId>my-app</artifactId>
                  <version>1</version>
                  
                  <dependencies>
                    <dependency>
                      <groupId>com.google.guava</groupId>
                      <artifactId>guava</artifactId>
                      <version>28.0-jre</version>
                    </dependency>
                  </dependencies>
                </project>
            """
    )

    @Test
    fun upgradeGuavaInParent(@TempDir tempDir: Path) {
        val parent = tempDir.resolve("pom.xml")
        val server = tempDir.resolve("server/pom.xml")
        server.toFile().parentFile.mkdirs()

        parent.toFile().writeText(
            """
                <project>
                  <modelVersion>4.0.0</modelVersion>
                
                  <packaging>pom</packaging>
                  <groupId>com.mycompany.app</groupId>
                  <artifactId>my-app</artifactId>
                  <version>1</version>
                  
                  <properties>
                    <guava.version>25.0-jre</guava.version>
                  </properties>
                </project>
            """.trimIndent()
        )

        server.toFile().writeText(
            """
                <project>
                  <modelVersion>4.0.0</modelVersion>
                  
                  <parent>
                    <groupId>com.mycompany.app</groupId>
                    <artifactId>my-app</artifactId>
                    <version>1</version>
                  </parent>
                
                  <groupId>com.mycompany.app</groupId>
                  <artifactId>my-app-server</artifactId>
                  <version>1</version>
                  
                  <dependencies>
                    <dependency>
                      <groupId>com.google.guava</groupId>
                      <artifactId>guava</artifactId>
                      <version>${"$"}{guava.version}</version>
                    </dependency>
                  </dependencies>
                </project>
            """.trimIndent()
        )

        assertChanged(
            recipe = UpgradeDependencyVersion(
                "com.google.guava",
                null as String?,
                "25-28",
                "-jre"),
            dependsOn = arrayOf(server.toFile()),
            before = parent.toFile(),
            after = """
                <project>
                  <modelVersion>4.0.0</modelVersion>
                
                  <packaging>pom</packaging>
                  <groupId>com.mycompany.app</groupId>
                  <artifactId>my-app</artifactId>
                  <version>1</version>
                  
                  <properties>
                    <guava.version>28.0-jre</guava.version>
                  </properties>
                </project>
            """
        )
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @Test
    fun checkValidation() {
        var recipe = UpgradeDependencyVersion(null, null, null, null)
        var valid = recipe.validate()
        Assertions.assertThat(valid.isValid).isFalse()
        Assertions.assertThat(valid.failures()).hasSize(2)
        Assertions.assertThat(valid.failures()[0].property).isEqualTo("groupId")
        Assertions.assertThat(valid.failures()[1].property).isEqualTo("newVersion")

        recipe = UpgradeDependencyVersion(null, "rewrite-maven", "latest.release", null)
        valid = recipe.validate()
        Assertions.assertThat(valid.isValid).isFalse()
        Assertions.assertThat(valid.failures()).hasSize(1)
        Assertions.assertThat(valid.failures()[0].property).isEqualTo("groupId")

        recipe = UpgradeDependencyVersion("org.openrewrite", null, null, null)
        valid = recipe.validate()
        Assertions.assertThat(valid.isValid).isFalse()
        Assertions.assertThat(valid.failures()).hasSize(1)
        Assertions.assertThat(valid.failures()[0].property).isEqualTo("newVersion")

        recipe = UpgradeDependencyVersion("org.openrewrite", "rewrite-maven", "latest.release", null)
        valid = recipe.validate()
        Assertions.assertThat(valid.isValid).isTrue()

        recipe = UpgradeDependencyVersion("org.openrewrite", "rewrite-maven", "latest.release", "123")
        valid = recipe.validate()
        Assertions.assertThat(valid.isValid).isTrue()
    }
}
