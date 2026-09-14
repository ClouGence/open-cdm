/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package com.clougence.clouddm.ds.starrocks.sql.analysis.behavior;

import static com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksParser.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

/** Planning dependencies, diagnostic execution and cached tuning resources. */
final class SrExplainBehaviorVisitor extends SrJobBehaviorVisitor {
    private boolean planning;

    SrExplainBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        super(parser, levels, baseLine, baseColumn);
    }

    @Override
    protected SrJobBehaviorVisitor scopedVisitor(ParserRuleContext database) {
        Map<UmiTypes, Object> context = new HashMap<>(levels);
        if (database != null) {
            context.put(UmiTypes.Schema, name(database));
        }
        SrExplainBehaviorVisitor visitor = new SrExplainBehaviorVisitor(parser, context, baseLine, baseColumn);
        visitor.planning = planning;
        return visitor;
    }

    private void orderRelations() {
        behavior().getRelations().sort(Comparator
            .comparingInt((BehaviorRelation relation) -> relation.getSubject().getStartLine())
            .thenComparingInt(relation -> relation.getSubject().getStartColumn()));
    }

    @Override
    public Void visitQueryStatement(QueryStatementContext ctx) {
        boolean previous = planning;
        if (ctx.optimizerTrace() != null || (ctx.explainDesc() != null && ctx.explainDesc().ANALYZE() == null)) {
            planning = true;
        }
        if (behavior().getStatementType() == SplitQueryType.UNKNOWN) {
            SplitQueryType type = SplitQueryType.SELECT;
            if (planning) {
                type = SplitQueryType.PERFORMANCE;
            }
            behavior().setStatementType(type);
        }
        try {
            visitChildren(ctx);
            orderRelations();
        } finally {
            planning = previous;
        }
        return null;
    }

    @Override
    public Void visitQuerySpecification(QuerySpecificationContext ctx) {
        addHints(ctx.SELECT().getSymbol(), ctx.selectItem(0).getStart(), null, SplitQueryType.SELECT);
        return visitChildren(ctx);
    }

    @Override
    public Void visitTableAtom(TableAtomContext ctx) {
        // A CTE is visible after its definition, within the enclosing query or DML scope.
        if (ctx.qualifiedName().identifier().size() == 1) {
            String table = name(ctx.qualifiedName().identifier(0));
            for (ParseTree parent = ctx.getParent(); parent != null; parent = parent.getParent()) {
                if (!(parent instanceof ParserRuleContext rule)) {
                    continue;
                }
                WithClauseContext with = rule.getRuleContext(WithClauseContext.class, 0);
                if (with == null) {
                    continue;
                }
                for (CommonTableExpressionContext cte : with.commonTableExpression()) {
                    if (name(cte.name).equalsIgnoreCase(table) && cte.getStop().getTokenIndex() < ctx.getStart().getTokenIndex()) {
                        return null;
                    }
                }
            }
        }
        return super.visitTableAtom(ctx);
    }

    private Void planDml(ParserRuleContext ctx, QualifiedNameContext destination, PartitionNamesContext partitionNames) {
        boolean previous = planning;
        planning = true;
        if (behavior().getStatementType() == SplitQueryType.UNKNOWN) {
            behavior().setStatementType(SplitQueryType.PERFORMANCE);
        }
        try {
            // FILES/BLACKHOLE output targets are not input schema reads or deployed sinks.
            if (destination != null) {
                for (BehaviorObject target : partitions(object(TargetType.Table, destination), partitionNames)) {
                    add(SplitQueryType.PERFORMANCE, BehaviorAction.READ, target);
                }
            }
            visitChildren(ctx);
            orderRelations();
        } finally {
            planning = previous;
        }
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        if (ctx.explainDesc() != null && ctx.explainDesc().ANALYZE() == null) {
            return planDml(ctx, ctx.qualifiedName(), ctx.partitionNames());
        }
        return super.visitInsertStatement(ctx);
    }

    @Override
    public Void visitUpdateStatement(UpdateStatementContext ctx) {
        if (ctx.explainDesc() != null && ctx.explainDesc().ANALYZE() == null) {
            return planDml(ctx, ctx.qualifiedName(), null);
        }
        return super.visitUpdateStatement(ctx);
    }

    @Override
    public Void visitDeleteStatement(DeleteStatementContext ctx) {
        if (ctx.explainDesc() != null && ctx.explainDesc().ANALYZE() == null) {
            return planDml(ctx, ctx.qualifiedName(), ctx.partitionNames());
        }
        return super.visitDeleteStatement(ctx);
    }

    @Override
    public Void visitWindowFunction(WindowFunctionContext ctx) {
        Token function = ctx.getStart();
        add(SplitQueryType.SELECT, BehaviorAction.CALL, objects.object(TargetType.Function, function, List.of(function.getText())));
        return visitChildren(ctx);
    }

    @Override
    public Void visitInformationFunctionExpression(InformationFunctionExpressionContext ctx) {
        // FE information functions are evaluated during analysis, including plain EXPLAIN.
        Token function = ctx.getStart();
        super.add(SplitQueryType.SELECT, BehaviorAction.CALL,
            objects.object(TargetType.Function, function, List.of(function.getText())), List.of());
        return null;
    }

    @Override
    protected BehaviorRelation add(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (planning && subject != null && subject.getObjectType() == TargetType.Function && action == BehaviorAction.CALL) {
            action = BehaviorAction.READ;
            // Repeated lookups of the same function definition have one metadata dependency.
            for (BehaviorRelation existing : behavior().getRelations()) {
                if (existing.getAction() == action && existing.getSubject().getObjectType() == TargetType.Function
                    && existing.getSubject().getObjectPath().equals(subject.getObjectPath())
                    && existing.getTarget().size() == targets.size()) {
                    boolean same = true;
                    for (int i = 0; i < targets.size(); i++) {
                        BehaviorObject old = existing.getTarget().get(i);
                        BehaviorObject target = targets.get(i);
                        if (old.getObjectType() != target.getObjectType() || !old.getObjectPath().equals(target.getObjectPath())) {
                            same = false;
                            break;
                        }
                    }
                    if (same) {
                        return existing;
                    }
                }
            }
        }
        return super.add(type, action, subject, targets);
    }

    @Override
    public Void visitShowProfilelistStatement(ShowProfilelistStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ,
            objects.instanceObject(TargetType.Profile, ctx.PROFILELIST().getSymbol(), "query_profile"));
        return null;
    }

    @Override
    public Void visitAnalyzeProfileStatement(AnalyzeProfileStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ,
            objects.instanceObject(TargetType.Profile, ctx.string(), "query_profile/" + name(ctx.string())));
        return null;
    }

    @Override
    public Void visitAlterPlanAdvisorAddStatement(AlterPlanAdvisorAddStatementContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.CONFIGURE,
            objects.instanceObject(TargetType.Policy, ctx.PLAN().getSymbol(), ctx.ADVISOR().getSymbol(), "plan_advisor"));
        return visit(ctx.queryStatement());
    }

    @Override
    public Void visitShowPlanAdvisorStatement(ShowPlanAdvisorStatementContext ctx) {
        add(SplitQueryType.PERFORMANCE, BehaviorAction.READ,
            objects.instanceObject(TargetType.Policy, ctx.PLAN().getSymbol(), ctx.ADVISOR().getSymbol(), "plan_advisor"));
        return null;
    }

    @Override
    public Void visitAlterPlanAdvisorDropStatement(AlterPlanAdvisorDropStatementContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP,
            objects.instanceObject(TargetType.Policy, ctx.string(), "plan_advisor/" + name(ctx.string())));
        return null;
    }

    @Override
    public Void visitTruncatePlanAdvisorStatement(TruncatePlanAdvisorStatementContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.PURGE,
            objects.instanceObject(TargetType.Policy, ctx.PLAN().getSymbol(), ctx.ADVISOR().getSymbol(), "plan_advisor"));
        return null;
    }
}
