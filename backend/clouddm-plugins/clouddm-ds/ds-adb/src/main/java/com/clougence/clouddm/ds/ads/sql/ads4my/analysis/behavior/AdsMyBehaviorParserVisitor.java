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
package com.clougence.clouddm.ds.ads.sql.ads4my.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.ads.sql.ads4my.analysis.reference.AdsMyResourceRegistry;
import com.clougence.clouddm.ds.ads.sql.ads4my.parser.antlr.AdsMyParserBaseVisitor;
import com.clougence.clouddm.ds.ads.sql.ads4my.parser.antlr.AdsMyParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.common.registry.FunctionBehavior;

final class AdsMyBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {

    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    AdsMyBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
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
        AdsMyStatementBehaviorVisitor visitor = new AdsMyStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        visitor.scanFunctionCalls(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class AdsMyStatementBehaviorVisitor extends AdsMyParserBaseVisitor<Void> {

    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    AdsMyStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitSimpleSelect(SimpleSelectContext ctx) {
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN) {
            behavior.setStatementType(SplitQueryType.SELECT);
        }
        return visitChildren(ctx);
    }

    void scanFunctionCalls(ParseTree tree) {
        if (behavior.getStatementType() == SplitQueryType.CREATE_PROG_OBJ || behavior.getStatementType() == SplitQueryType.ALTER_PROG_OBJ) {
            return;
        }

        boolean deferred = !descendants(tree, FullDescribeStatementContext.class).isEmpty() || !descendants(tree, CreateViewContext.class).isEmpty()
                           || !descendants(tree, AlterViewContext.class).isEmpty();
        for (FunctionCallContext ctx : descendants(tree, FunctionCallContext.class)) {
            BehaviorObject subject;
            FunctionBehavior classification = FunctionBehavior.DEFAULT;
            if (ctx instanceof UdfFunctionCallContext udf) {
                subject = object(TargetType.Function, udf.customFunctionName().fullId());
                if (udf.customFunctionName().fullId().uid().size() == 1) {
                    classification = AdsMyResourceRegistry.instance()
                        .functionClassification(udf.customFunctionName().getText(), !descendants(ctx, FunctionArgContext.class).isEmpty());
                }
            } else {
                if (ctx instanceof SpecificFunctionCallContext specific
                    && (specific.specificFunction() instanceof CaseFunctionCallContext || specific.specificFunction() instanceof SpecialTimeCallContext
                        || specific.specificFunction() instanceof ValuesFunctionCallContext)) {
                    continue;
                }
                subject = objects.object(TargetType.Function, ctx.getStart(), List.of(ctx.getStart().getText()));
                classification = AdsMyResourceRegistry.instance().functionClassification(ctx.getStart().getText(), !descendants(ctx, FunctionArgContext.class).isEmpty());
            }
            if (deferred) {
                classification = FunctionBehavior.DEFAULT;
            }
            objects.applyFunctionBehavior(behavior, subject, classification, ctx);
        }
    }

    @Override
    public Void visitTableName(TableNameContext ctx) {
        addUnary(SplitQueryType.SELECT, BehaviorAction.READ, table(ctx));
        return null;
    }

    @Override
    public Void visitQueryCreateTable(QueryCreateTableContext ctx) {
        addCreateTable(ctx.tableName(), descendants(ctx.selectStatement(), TableNameContext.class));
        return null;
    }

    @Override
    public Void visitCopyCreateTable(CopyCreateTableContext ctx) {
        List<TableNameContext> tables = ctx.tableName();
        addCreateTable(tables.get(0), tables.subList(1, tables.size()));
        return null;
    }

    @Override
    public Void visitColumnCreateTable(ColumnCreateTableContext ctx) {
        addCreateTable(ctx.tableName(), List.of());
        return null;
    }

    @Override
    public Void visitCallStatement(CallStatementContext ctx) {
        addUnary(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, ctx.procName().fullId()));
        return null;
    }

    private void addCreateTable(TableNameContext subjectContext, List<TableNameContext> sourceContexts) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (TableNameContext source : sourceContexts) {
            BehaviorObject target = table(source);
            if (target != null) {
                targets.add(target);
            }
        }
        addRelation(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, table(subjectContext), targets);
    }

    private BehaviorObject table(TableNameContext context) {
        return context == null ? null : object(TargetType.Table, context.fullId());
    }

    private BehaviorObject object(TargetType type, FullIdContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        for (UidContext uid : context.uid()) {
            String name = text(uid);
            if (type != TargetType.Function) {
                name = unquote(name);
            }

            names.add(name);
        }
        return objects.object(type, context, names);
    }

    private String text(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private String unquote(String value) {
        if (value.length() >= 2 && value.charAt(0) == '`' && value.charAt(value.length() - 1) == '`') {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private void addUnary(SplitQueryType type, BehaviorAction action, BehaviorObject subject) {
        addRelation(type, action, subject, List.of());
    }

    private void addRelation(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        relation.getTarget().addAll(targets);
        behavior.getRelations().add(relation);
        behavior.setStatementType(type);
    }

    private <T extends ParserRuleContext> List<T> descendants(ParseTree tree, Class<T> type) {
        List<T> result = new ArrayList<>();
        collectDescendants(tree, type, result);
        return result;
    }

    private <T extends ParserRuleContext> void collectDescendants(ParseTree tree, Class<T> type, List<T> result) {
        if (tree == null) {
            return;
        }
        if (type.isInstance(tree)) {
            result.add(type.cast(tree));
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectDescendants(tree.getChild(i), type, result);
        }
    }
}
