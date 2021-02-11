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
package org.openrewrite.maven.internal

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openrewrite.InMemoryExecutionContext
import org.openrewrite.Parser
import java.nio.file.Paths

class RawMavenTest {
    @Test
    fun emptyContainers() {
        val maven = RawMaven.parse(Parser.Input(Paths.get("pom.xml")) {
            """
                <project>
                    <dependencyManagement>
                        <!--  none, for now  -->
                    </dependencyManagement>
                    <dependencies>
                        <!--  none, for now  -->
                    </dependencies>
                    <repositories>
                        <!--  none, for now  -->
                    </repositories>
                    <licenses>
                        <!--  none, for now  -->
                    </licenses>
                    <profiles>
                        <!--  none, for now  -->
                    </profiles>
                </project>
            """.trimIndent().byteInputStream()
        }, null, null, InMemoryExecutionContext())

        assertThat(maven.pom.dependencyManagement?.dependencies).isNull()
        assertThat(maven.pom.getActiveRepositories(emptyList())).isEmpty()
        assertThat(maven.pom.innerLicenses).isEmpty()
        assertThat(maven.pom.innerProfiles).isEmpty()
    }

    @Test
    fun dependencyManagement() {
        val maven = RawMaven.parse(Parser.Input(Paths.get("pom.xml")) {
            """
                <project>
                  <modelVersion>4.0.0</modelVersion>
                  <groupId>com.mycompany.app</groupId>
                  <artifactId>my-app</artifactId>
                  <version>1</version>
                  <dependencyManagement>
                    <dependencies>
                      <dependency>
                        <groupId>com.fasterxml.jackson.core</groupId>
                        <artifactId>jackson-databind</artifactId>
                        <version>latest.release</version>
                      </dependency>
                    </dependencies>
                  </dependencyManagement>
                </project>
            """.trimIndent().byteInputStream()
        }, null, null, InMemoryExecutionContext())

        assertThat(maven.pom.dependencyManagement?.dependencies?.dependencies).isNotEmpty()
    }
}
