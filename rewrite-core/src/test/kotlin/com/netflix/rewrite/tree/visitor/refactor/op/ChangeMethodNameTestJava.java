/*
 * Copyright 2020 the original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.rewrite.tree.visitor.refactor.op;

import com.netflix.rewrite.parse.OpenJdkParser;
import com.netflix.rewrite.parse.Parser;
import com.netflix.rewrite.tree.Tr;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChangeMethodNameTestJava {
    final Parser parser = new OpenJdkParser();

    @Test
    public void refactorMethodName() {
        String a = "class A {{ B.foo(0); }}";
        String b = "class B { static void foo(int n) {} }";

        final Tr.CompilationUnit cu = parser.parse(a, /* which depends on */ b);

        Tr.CompilationUnit fixed = cu.refactor()
                .changeMethodName(cu.findMethodCalls("B foo(int)"), "bar")
                .fix().getFixed();

        assertEquals(fixed.print(), "class A {{ B.bar(0); }}");
    }

    @Test
    public void refactorMethodNameDiff() {
        String a = "class A {\n   {\n      B.foo(0);\n   }\n}";
        String b = "class B { static void foo(int n) {} }";

        final Tr.CompilationUnit cu = parser.parse(a, /* which depends on */ b);

        String diff = cu.refactor()
                .changeMethodName(cu.findMethodCalls("B foo(int)"), "bar")
                .fix()
                .diff();

        System.out.println(diff);
    }
}
