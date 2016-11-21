/**
 * Copyright 2016 Netflix, Inc.
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
package com.netflix.java.refactor.refactor.op

import com.netflix.java.refactor.parse.OracleJdkParser
import com.netflix.java.refactor.parse.Parser

abstract class ChangeTypeTest(p: Parser): Parser by p {
    
//    @Ignore
//    @Test
//    fun refactorType() {
//        val a1 = """
//            |package a;
//            |public class A1 {}
//        """
//
//        val a2 = """
//            |package a;
//            |public class A2 {}
//        """
//
//        val b = """
//            |package b;
//            |import java.util.*;
//            |import a.A1;
//            |
//            |public class B extends A1 {
//            |    List<A1> aList = new ArrayList<>();
//            |    A1 aField = new A1();
//            |
//            |    public A1 b(A1 aParam) {
//            |        A1 aVar = new A1();
//            |        return aVar;
//            |    }
//            |}
//        """
//
//        val cu = parse(b, a1, a2)
//        cu.refactor().changeType("a.A1", "a.A2").fix()
//        cu.refactor().removeImport("a.A1").fix()
//
//        assertRefactored(cu, """
//            |package b;
//            |import java.util.*;
//            |import a.A2;
//            |
//            |public class B extends A2 {
//            |    List<A2> aList = new ArrayList<>();
//            |    A2 aField = new A2();
//            |
//            |    public A2 b(A2 aParam) {
//            |        A2 aVar = new A2();
//            |        return aVar;
//            |    }
//            |}
//        """)
//    }
}

class OracleJdkChangeTypeTest: ChangeTypeTest(OracleJdkParser())