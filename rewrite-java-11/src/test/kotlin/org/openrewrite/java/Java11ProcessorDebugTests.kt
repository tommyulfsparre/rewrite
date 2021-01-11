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
package org.openrewrite.java

import org.junit.jupiter.api.extension.ExtendWith
import org.openrewrite.DebugOnly
import org.openrewrite.java.format.BlankLinesTest
import org.openrewrite.java.format.TabsAndIndentsTest
import org.openrewrite.java.search.*
import org.openrewrite.java.tree.TypeTreeTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11AddImportTest: Java11Test, AddImportTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11BlankLinesTest: Java11Test, BlankLinesTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11ChangeFieldNameTest: Java11Test, ChangeFieldNameTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11ChangeFieldTypeTest: Java11Test, ChangeFieldTypeTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11ChangeLiteralTest: Java11Test, ChangeLiteralTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11ChangeMethodNameTest: Java11Test, ChangeMethodNameTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11ChangeMethodTargetToStaticTest: Java11Test, ChangeMethodTargetToStaticTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11ChangeMethodTargetToVariableTest: Java11Test, ChangeMethodTargetToVariableTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11ChangeTypeTest: Java11Test, ChangeTypeTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11DeleteMethodArgumentTest: Java11Test, DeleteMethodArgumentTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11DeleteStatementTest: Java11Test, DeleteStatementTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11FindAnnotationTest: Java11Test, FindAnnotationTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11FindFieldTest : Java11Test, FindFieldTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11FindInheritedFieldTest : Java11Test, FindInheritedFieldTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11FindMethodTest : Java11Test, FindMethodTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11FindTypeTest : Java11Test, FindTypeTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11ImplementInterfaceTest : Java11Test, ImplementInterfaceTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11JavaTemplateTest : Java11Test, JavaTemplateTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11OrderImportsTest : Java11Test, OrderImportsTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11RemoveImportTest : Java11Test, RemoveImportTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11RenameVariableTest : Java11Test, RenameVariableTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11TabsAndIndentsTest: Java11Test, TabsAndIndentsTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11TypeTreeTest :  Java11Test, TypeTreeTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11UnwrapParenthesesTest :  Java11Test, UnwrapParenthesesTest

@DebugOnly
@ExtendWith(JavaParserResolver::class)
class Java11UseStaticImportTest :  Java11Test, UseStaticImportTest