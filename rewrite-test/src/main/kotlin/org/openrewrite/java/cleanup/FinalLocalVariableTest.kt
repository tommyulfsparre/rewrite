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
package org.openrewrite.java.cleanup

import org.junit.jupiter.api.Test
import org.openrewrite.Recipe
import org.openrewrite.RecipeTest
import org.openrewrite.java.JavaParser

interface FinalLocalVariableTest : RecipeTest {
    override val recipe: Recipe?
        get() = FinalLocalVariable()

    @Test
    fun localVariablesAreMadeFinal(jp: JavaParser) = assertChanged(
        jp,
        before = """
            public class A {
                {
                    int n = 1;
                    for(int i = 0; i < n; i++) {
                    }
                }
            }
        """,
        after = """
            public class A {
                {
                    final int n = 1;
                    for(int i = 0; i < n; i++) {
                    }
                }
            }
        """
    )

    @Test // TODO what a terrible test name
    fun localVariablesAreMadeFinalIgnoringReassignedVariables(jp: JavaParser) = assertChanged(
        jp,
        before = """
            public class A {
                public void test() {
                    int a = 0;
                    int b = 0;
                    int c = 10;
                    for(int i = 0; i < c; i++) {
                        a = i + c;
                        b++;
                    }
                }
            }
        """,
        after = """
            public class A {
                public void test() {
                    int a = 0;
                    int b = 0;
                    final int c = 10;
                    for(int i = 0; i < c; i++) {
                        a = i + c;
                        b++;
                    }
                }
            }
        """
    )

    @Test
    fun classFieldVariablesAreIgnored(jp: JavaParser) = assertUnchanged(
        jp,
        before = """
            public class Test {
                int a;
                int b = 1;
            }
        """
    )

    @Test // TODO copied test does not look quite right?
    fun multiVariables(jp: JavaParser) = assertChanged(
        jp,
        before = """
            public class A {
                {
                    int a, b = 1;
                    a = 0;
                }
            }
        """,
        // the final only applies to any initialized variables (b in this case)
        after = """
            public class A {
                {
                    final int a, b = 1;
                    a = 0;
                }
            }
        """
    )

    @Test // this should be able to be configurable to prevent "main"
    fun methodParameterVariablesAreMadeFinal(jp: JavaParser) = assertChanged(
        jp,
        before = """
            public class Test {
                private static int weirdMath(int x, int y) {
                    y = y + y;
                    return x + y;
                }

                public static void main(String[] args) {
                }
            }
        """,
        after = """
            public class Test {
                private static int weirdMath(final int x, int y) {
                    y = y + y;
                    return x + y;
                }

                public static void main(final String[] args) {
                }
            }
        """
    )

    @Test // TODO should be able to have this be configurable
    fun enhancedForLoopAssignment(jp: JavaParser) = assertChanged(
        jp,
        before = """
            public class Test {
                public static void main(String[] args) {
                    for (String i : args) {
                        System.out.println(i);
                    }

                    for (String i : args) {
                        i = i.toUpperCase();
                    }
                }
            }
        """,
        after = """
            public class Test {
                public static void main(final String[] args) {
                    for (final String i : args) {
                        System.out.println(i);
                    }

                    for (String i : args) {
                        i = i.toUpperCase();
                    }
                }
            }
        """
    )


}
