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
package org.openrewrite.java.cleanup;

import org.openrewrite.Incubating;
import org.openrewrite.Tree;
import org.openrewrite.internal.ListUtils;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.NameTree;
import org.openrewrite.java.tree.Space;
import org.openrewrite.marker.Markers;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Incubating(since = "7.0.0")
public class FinalLocalVariableVisitor<P> extends JavaIsoVisitor<P> {

    public FinalLocalVariableVisitor() {
        setCursoringOn();
    }

    @Override
    public J.VariableDeclarations visitVariableDeclarations(J.VariableDeclarations multiVariable, P p) {
        J.VariableDeclarations mv = visitAndCast(multiVariable, p, super::visitVariableDeclarations);

        J variableScope = getCursor().dropParentUntil(J.class::isInstance).getValue();
        if (variableScope instanceof J.ClassDeclaration) {
            // we don't care about fields here
            return mv;
        }

        if (!multiVariable.hasModifier(J.Modifier.Type.Final)) {
            Stream<J.VariableDeclarations.NamedVariable> sv = multiVariable.getVariables().stream();

            // BiPredicate<Path, BasicFileAttributes> predicate = (p, bfa) -> bfa.isRegularFile() &&
            //         p.getFileName().toString().endsWith(".jar");

            // TODO note
            // what is the " (variable.getInitializer() == null ? -1 : 0)" saying here? Are we saying
            // it's acceptable for a declaration and one assignment...?
            Predicate<J.VariableDeclarations.NamedVariable> predicate = (variable) ->
                    FindAssignmentReferencesToVariable.find(variableScope, variable).size() + (variable.getInitializer() == null ? -1 : 0) <= 0;

            if (sv.anyMatch(predicate)) {
                /**
                 * TODO need a way to insert a modifier using Template.
                 * Can't use `mv.getModifiers().get(0).getCoordinates().before()` or something, because
                 * what if the list of Modifiers is empty? And "VariableDeclarations" doesn't have
                 * an accessor to add/replace the coordinates of modifiers
                 */
                mv = maybeAutoFormat(mv,
                        mv.withModifiers(
                                ListUtils.concat(mv.getModifiers(), new J.Modifier(Tree.randomId(), Space.EMPTY, Markers.EMPTY, J.Modifier.Type.Final))
                        ), p, getCursor().dropParentUntil(J.class::isInstance));
            }
        }

        return mv;
    }

    private static class FindAssignmentReferencesToVariable {

        private static Set<NameTree> find(J j, J.VariableDeclarations.NamedVariable variable) {

            JavaIsoVisitor<Set<NameTree>> findVisitor = new JavaIsoVisitor<Set<NameTree>>() {

                @Override
                public J.Assignment visitAssignment(J.Assignment assignment, Set<NameTree> ctx) {
                    J.Assignment a = super.visitAssignment(assignment, ctx);
                    if (a.getVariable() instanceof J.Identifier) {
                        J.Identifier i = ((J.Identifier) a.getVariable());
                        if (i.getSimpleName().equals(variable.getSimpleName())) {
                            ctx.add(i);
                        }
                    }
                    return a;
                }

                @Override
                public J.AssignmentOperation visitAssignmentOperation(J.AssignmentOperation assignOp, Set<NameTree> ctx) {
                    J.AssignmentOperation a = super.visitAssignmentOperation(assignOp, ctx);
                    if (a.getVariable() instanceof J.Identifier) {
                        J.Identifier i = ((J.Identifier) a.getVariable());
                        if (i.getSimpleName().equals(variable.getSimpleName())) {
                            ctx.add(i);
                        }
                    }
                    return a;
                }

                @Override
                public J.Unary visitUnary(J.Unary unary, Set<NameTree> ctx) {
                    J.Unary u = super.visitUnary(unary, ctx);
                    if (u.getExpression() instanceof J.Identifier) {
                        J.Identifier i = ((J.Identifier) u.getExpression());
                        if (i.getSimpleName().equals(variable.getSimpleName())) {
                            ctx.add(i);
                        }
                    }
                    return u;
                }

            };

            Set<NameTree> refs = new HashSet<>();
            findVisitor.visit(j, refs);
            return refs;
        }

    }

}
