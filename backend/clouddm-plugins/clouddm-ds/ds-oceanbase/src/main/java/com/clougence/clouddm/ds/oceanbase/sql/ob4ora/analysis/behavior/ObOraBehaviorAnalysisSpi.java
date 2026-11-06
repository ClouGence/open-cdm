/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.ds.oceanbase.sql.ob4ora.analysis.behavior;

import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Stream;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.analysis.reference.ObOraResourceRegistry;
import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.parser.ObForOraSplitAnalysisSpi;
import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.parser.ObOraDslProvider;
import com.clougence.clouddm.ds.oceanbase.sql.parser.antlr.ObForOracleParserBaseVisitor;
import com.clougence.clouddm.ds.oceanbase.sql.parser.antlr.ObForOracleParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.common.registry.FunctionBehavior;

public class ObOraBehaviorAnalysisSpi implements BehaviorAnalysisSpi {
    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        var scripts = new ObForOraSplitAnalysisSpi().splitScriptStream(queryReader, List.of(), baseLine, baseColumn);
        return scripts.flatMap(script -> {
            StringReader reader = new StringReader(script.getScript());
            int codeLine = script.getBodyStartCodeLine();
            int codeColumn = script.getBodyStartCodeColumn();

            return analyzeStatement(reader, levels, codeLine, codeColumn).stream();
        }).onClose(scripts::close);
    }

    private List<StatementBehavior> analyzeStatement(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        ObOraBehaviorParserVisitor[] holder = new ObOraBehaviorParserVisitor[1];
        DslHelper.doVisitor(ObOraDslProvider.INSTANCE, queryReader, (lexer, parser) -> {
            holder[0] = new ObOraBehaviorParserVisitor(parser, levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}

final class ObOraBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {
    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    ObOraBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.levels = levels;
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
    }

    List<StatementBehavior> behaviors() {
        return behaviors;
    }

    @Override
    public Void visit(ParseTree tree) {
        ObOraStatementBehaviorVisitor visitor = new ObOraStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        visitor.scanFunctionCalls(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class ObOraStatementBehaviorVisitor extends ObForOracleParserBaseVisitor<Void> {

    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();
    private boolean                        explaining;
    private boolean                        definingView;

    ObOraStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    void scanFunctionCalls(ParseTree tree) {
        if (!descendants(tree, Create_function_bodyContext.class).isEmpty() || !descendants(tree, Create_procedure_bodyContext.class).isEmpty()
            || !descendants(tree, Create_triggerContext.class).isEmpty()) {
            return;
        }

        explaining = !descendants(tree, Explain_statementContext.class).isEmpty();
        definingView = !descendants(tree, Create_viewContext.class).isEmpty();
        if (explaining) {
            behavior.setStatementType(SplitQueryType.PERFORMANCE);
        }

        for (Function_callContext ctx : descendants(tree, Function_callContext.class)) {
            if (ctx.function_argument().isEmpty()) {
                continue;
            }

            ParserRuleContext name = ctx.routine_name();
            if (name == null) {
                name = ctx.id_expression();
            }

            recordFunctionCall(name);
        }

        for (General_element_partContext ctx : descendants(tree, General_element_partContext.class)) {
            if (!ctx.function_argument().isEmpty() && ctx.getParent() instanceof General_elementContext parent && parent.general_element_part().size() == 1) {
                ParserRuleContext name = ctx.routine_name();
                if (name == null) {
                    name = ctx.id_expression();
                }

                recordFunctionCall(name);
            }
        }

        for (Standard_functionContext ctx : descendants(tree, Standard_functionContext.class)) {
            if (ctx.other_function() != null && (ctx.other_function().cursor_name() != null || ctx.other_function().TIME() != null)) {
                continue;
            }
            var token = ctx.getStart();
            BehaviorObject function = objects.object(TargetType.Function, token, List.of(token.getText().toUpperCase(Locale.ROOT)));
            FunctionBehavior rule = FunctionBehavior.DEFAULT;
            if (!explaining && !definingView) {
                rule = ObOraResourceRegistry.instance().functionBehavior(token.getText());
            }
            objects.applyFunctionBehavior(behavior, function, rule, ctx);
        }
    }

    private void recordFunctionCall(ParserRuleContext name) {
        if (name == null) {
            return;
        }

        BehaviorObject function = object(TargetType.Function, name);
        boolean alreadyRecorded = behavior.getRelations()
            .stream()
            .anyMatch(existing -> existing.getSubject().getObjectType() == TargetType.Function && existing.getSubject().getStartLine() == function.getStartLine()
                                  && existing.getSubject().getStartColumn() == function.getStartColumn());
        if (alreadyRecorded) {
            return;
        }

        FunctionBehavior rule = FunctionBehavior.DEFAULT;
        if (!explaining && !definingView) {
            rule = ObOraResourceRegistry.instance().functionBehavior(name.getText());
        }
        objects.applyFunctionBehavior(behavior, function, rule, name);
    }

    @Override
    public Void visitDml_table_expression_clause(Dml_table_expression_clauseContext ctx) {
        if (ctx.tableview_name() != null) {
            add(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.tableview_name()), List.of());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitCreate_view(Create_viewContext ctx) {
        List<BehaviorObject> targets = descendants(ctx.select_only_statement(), Dml_table_expression_clauseContext.class).stream()
            .filter(source -> source.tableview_name() != null)
            .map(source -> object(TargetType.Table, source.tableview_name()))
            .filter(Objects::nonNull)
            .toList();
        add(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.tableview_name()), targets);
        return null;
    }

    @Override
    public Void visitCall_statement(Call_statementContext ctx) {
        if (!ctx.routine_name().isEmpty()) {
            ParserRuleContext name = ctx.routine_name(0);
            boolean definingProgram = false;
            for (ParseTree parent = ctx.getParent(); parent != null; parent = parent.getParent()) {
                if (parent instanceof Create_function_bodyContext || parent instanceof Create_procedure_bodyContext || parent instanceof Create_triggerContext) {
                    definingProgram = true;
                    break;
                }
            }
            FunctionBehavior rule = FunctionBehavior.DEFAULT;
            if (!definingProgram) {
                rule = ObOraResourceRegistry.instance().functionBehavior(name.getText());
            }
            objects.applyFunctionBehavior(behavior, object(TargetType.Procedure, name), rule, name);
            if (behavior.getStatementType() == SplitQueryType.UNKNOWN) {
                behavior.setStatementType(SplitQueryType.CALL_PROG_OBJ);
            }
        }
        return null;
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        collectNames(context, names, type == TargetType.Function);
        if (type == TargetType.Table && names.size() == 1 && "DUAL".equalsIgnoreCase(names.get(0))) {
            return objects.instanceObject(type, context, names.get(0));
        }
        return objects.object(type, context, names);
    }

    private void collectNames(ParseTree tree, List<String> names) {
        collectNames(tree, names, false);
    }

    private void collectNames(ParseTree tree, List<String> names, boolean preserveQuotes) {
        if (tree instanceof IdentifierContext || tree instanceof Id_expressionContext) {
            ParserRuleContext context = (ParserRuleContext) tree;
            String name = parser.getTokenStream().getText(context.getStart(), context.getStop());
            if (!preserveQuotes) {
                name = unquote(name);
            }

            names.add(name);
            return;
        }
        if (tree instanceof Link_nameContext) {
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectNames(tree.getChild(i), names, preserveQuotes);
        }
    }

    private String unquote(String value) {
        return value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"' ? value.substring(1, value.length() - 1) : value;
    }

    private BehaviorRelation add(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return null;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        relation.getTarget().addAll(targets);
        behavior.getRelations().add(relation);
        behavior.setStatementType(type);
        return relation;
    }

    private <T extends ParserRuleContext> List<T> descendants(ParseTree tree, Class<T> type) {
        List<T> result = new ArrayList<>();
        collect(tree, type, result);
        return result;
    }

    private <T extends ParserRuleContext> void collect(ParseTree tree, Class<T> type, List<T> result) {
        if (tree == null) {
            return;
        }
        if (type.isInstance(tree)) {
            result.add(type.cast(tree));
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collect(tree.getChild(i), type, result);
        }
    }
}
