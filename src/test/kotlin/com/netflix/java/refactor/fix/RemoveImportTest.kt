package com.netflix.java.refactor.fix

import com.netflix.java.refactor.AbstractRefactorTest
import org.junit.Test

class RemoveImportTest: AbstractRefactorTest() {

    @Test
    fun removeNamedImport() {
        val a = java("""
            |import java.util.List;
            |class A {}
        """)
        
        refactor(a).refactor().removeImport("java.util.List").fix()
        
        assertRefactored(a, "class A {}")
    }

    @Test
    fun removeNamedImportByClass() {
        val a = java("""
            |import java.util.List;
            |class A {}
        """)

        refactor(a).refactor().removeImport(List::class.java).fix()
        
        assertRefactored(a, "class A {}")
    }
    
    @Test
    fun removeStarImportIfNoTypesReferredTo() {
        val a = java("""
            |import java.util.*;
            |class A {}
        """.trimMargin())

        refactor(a).refactor().removeImport(List::class.java).fix()

        assertRefactored(a, "class A {}")
    }
    
    @Test
    fun replaceStarImportWithNamedImportIfOnlyOneReferencedTypeRemains() {
        val a = java("""
            |import java.util.*;
            |class A {
            |   Collection c;
            |}
        """)

        refactor(a).refactor().removeImport(List::class.java).fix()
        
        assertRefactored(a, """
            |import java.util.Collection;
            |class A {
            |   Collection c;
            |}
        """)
    }
    
    @Test
    fun leaveStarImportInPlaceIfMoreThanTwoTypesStillReferredTo() {
        val a = java("""
            |import java.util.*;
            |class A {
            |   Collection c;
            |   Set s;
            |}
        """)
        
        refactor(a).refactor().removeImport("java.util.List").fix()

        assertRefactored(a, """
            |import java.util.*;
            |class A {
            |   Collection c;
            |   Set s;
            |}
        """)
    }
}