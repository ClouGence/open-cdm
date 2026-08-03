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
package com.clougence.clouddm.ds.tidb.sql.parser;

import java.util.*;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;

public class TiDBSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    private final TiDBDslProvider provider;

    public TiDBSplitAnalysisSpi(TiDBDslProvider provider){
        this.provider = provider;
    }

    protected DslProvider dslProvider() {
        return provider;
    }

    @Override
    protected Object predictionCacheScope() {
        return this.provider.config();
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return new TiDBSplitVisitor(this.provider.version());
    }

    @Override
    protected Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        Set<SplitQueryType> types = new TiDBSplitVisitor(this.provider.version()).collectTypes(context);
        return types.isEmpty() ? Collections.singleton(SplitQueryType.UNKNOWN) : types;
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        if (mayContainProgramOwner(context)) {
            ParserRuleContext owner = findProgramOwner(context);
            ParserRuleContext body = programBody(owner);
            if (body != null) {
                return List.of(programNode(body, tokens));
            }
        }
        return viewQueryChild(context, tokens);
    }

    private SplitScript programNode(ParserRuleContext context, CommonTokenStream tokens) {
        if (context instanceof TiDBParser.RoutineBodyContext || context instanceof TiDBParser.CompoundStatementContext) {
            ParserRuleContext child = firstRuleChild(context);
            return programNode(child, tokens);
        }
        if (context instanceof TiDBParser.ProcedureSqlStatementContext) {
            ParserRuleContext statement = unwrapProgramStatement(firstRuleChild(context));
            return createProgramNode(context, statement, tokens);
        }
        return createProgramNode(context, context, tokens);
    }

    private SplitScript createProgramNode(ParserRuleContext range, ParserRuleContext statement, CommonTokenStream tokens) {
        statement = unwrapProgramStatement(statement);
        Set<SplitQueryType> types;
        List<SplitScript> children;
        if (statement instanceof TiDBParser.BlockStatementContext) {
            types = Collections.singleton(SplitQueryType.BLOCK);
            children = directProgramChildren(statement, tokens);
        } else if (isProgramControl(statement)) {
            types = Collections.singleton(SplitQueryType.PROGRAM_CONTROL);
            children = directProgramChildren(statement, tokens);
        } else if (statement instanceof TiDBParser.DeclareVariableContext || statement instanceof TiDBParser.DeclareConditionContext
                   || statement instanceof TiDBParser.DeclareCursorContext || statement instanceof TiDBParser.DeclareHandlerContext) {
            types = statement instanceof TiDBParser.DeclareCursorContext ? cursorTypes() : Collections.singleton(SplitQueryType.PROGRAM_CONTROL);
            children = statement instanceof TiDBParser.DeclareHandlerContext ? directProgramChildren(statement, tokens) : Collections.emptyList();
        } else if (statement instanceof TiDBParser.CursorStatementContext) {
            types = cursorTypes();
            children = Collections.emptyList();
        } else if (containsContext(statement, TiDBParser.SetNewValueInsideTriggerContext.class)) {
            types = Collections.singleton(SplitQueryType.UPDATE);
            children = Collections.emptyList();
        } else if (!mayContainProgramOwner(statement)
                   && findContext(statement, TiDBParser.SetVariableContext.class) instanceof TiDBParser.SetVariableContext setVariable) {
            types = classifyRoutineSet(setVariable);
            children = Collections.emptyList();
        } else {
            types = new TiDBSplitVisitor(this.provider.version()).collectTypes(statement);
            if (types.isEmpty()) {
                types = Collections.singleton(SplitQueryType.UNKNOWN);
            }
            children = viewQueryChild(statement, tokens);
            if (children.isEmpty()) {
                ParserRuleContext nestedOwner = mayContainProgramOwner(statement) ? findProgramOwner(statement) : null;
                ParserRuleContext nestedBody = programBody(nestedOwner);
                children = nestedBody == null ? Collections.emptyList() : List.of(programNode(nestedBody, tokens));
            }
        }
        return createChild(range, tokens, new LinkedHashSet<>(types), children);
    }

    private static Set<SplitQueryType> cursorTypes() {
        Set<SplitQueryType> types = new LinkedHashSet<>();
        types.add(SplitQueryType.SELECT);
        types.add(SplitQueryType.PROGRAM_CONTROL);
        return types;
    }

    private List<SplitScript> viewQueryChild(ParserRuleContext context, CommonTokenStream tokens) {
        if (context instanceof TiDBParser.ViewQueryStatementContext) {
            return Collections.emptyList();
        }
        ParserRuleContext viewQuery = findContext(context, TiDBParser.ViewQueryStatementContext.class);
        return viewQuery == null ? Collections.emptyList() : List.of(programNode(viewQuery, tokens));
    }

    private Set<SplitQueryType> classifyRoutineSet(TiDBParser.SetVariableContext context) {
        Set<String> localNames = routineLocalNames(context);
        Set<SplitQueryType> result = new LinkedHashSet<>();
        for (TiDBParser.SetVariableAssignmentContext assignment : context.setVariableAssignment()) {
            String variable = assignment.variableClause().getText();
            String normalized = normalizeIdentifier(variable);
            String upper = variable.toUpperCase(Locale.ROOT);
            if (localNames.contains(normalized)) {
                result.add(SplitQueryType.PROGRAM_CONTROL);
            } else if (assignment.variableClause().LOCAL_ID() != null) {
                result.add(SplitQueryType.SESSION_VARIABLE_RW);
            } else if (upper.contains("GTID_") || upper.contains("SLAVE_") || upper.contains("REPLICA_")) {
                result.add(SplitQueryType.ALTER_REPLICATION);
            } else if (upper.startsWith("@@GLOBAL.") || upper.startsWith("@@PERSIST.") || upper.startsWith("@@PERSIST_ONLY.") || upper.startsWith("GLOBAL")
                       || upper.startsWith("PERSIST")) {
                result.add(SplitQueryType.SYSTEM_SETTING_WRITE);
            } else {
                result.add(SplitQueryType.SESSION_SETTING_WRITE);
            }
        }
        return result.isEmpty() ? Collections.singleton(SplitQueryType.UNKNOWN) : result;
    }

    private static Set<String> routineLocalNames(ParserRuleContext context) {
        ParserRuleContext owner = context;
        while (owner != null && !(owner instanceof TiDBParser.CreateProcedureContext) && !(owner instanceof TiDBParser.CreateFunctionContext)
               && !(owner instanceof TiDBParser.CreateTriggerContext) && !(owner instanceof TiDBParser.CreateEventContext)
               && !(owner instanceof TiDBParser.AlterEventContext)) {
            owner = owner.getParent();
        }
        if (owner == null) {
            return Collections.emptySet();
        }

        Set<String> names = new LinkedHashSet<>();
        collectRoutineLocalNames(owner, names);
        return names;
    }

    private static void collectRoutineLocalNames(ParseTree tree, Set<String> names) {
        if (tree instanceof TiDBParser.ProcedureParameterContext context) {
            names.add(normalizeIdentifier(context.uid().getText()));
            return;
        }
        if (tree instanceof TiDBParser.FunctionParameterContext context) {
            names.add(normalizeIdentifier(context.uid().getText()));
            return;
        }
        if (tree instanceof TiDBParser.DeclareVariableContext context) {
            context.uidList().uid().forEach(uid -> names.add(normalizeIdentifier(uid.getText())));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectRoutineLocalNames(tree.getChild(i), names);
        }
    }

    private static String normalizeIdentifier(String value) {
        String normalized = value.trim();
        if (normalized.length() >= 2) {
            char quote = normalized.charAt(0);
            if ((quote == '`' || quote == '"') && normalized.charAt(normalized.length() - 1) == quote) {
                normalized = normalized.substring(1, normalized.length() - 1);
            }
        }
        return normalized.toLowerCase(Locale.ROOT);
    }

    private List<SplitScript> directProgramChildren(ParserRuleContext context, CommonTokenStream tokens) {
        List<ParserRuleContext> contexts = new ArrayList<>();
        collectDirectProgramChildren(context, context, contexts);
        Set<ProgramTypeTree> seen = new LinkedHashSet<>();
        List<SplitScript> children = new ArrayList<>();
        for (ParserRuleContext childContext : contexts) {
            SplitScript child = childContext instanceof TiDBParser.ExpressionContext ? programExpressionNode(childContext, tokens) : programNode(childContext, tokens);
            if (child != null && seen.add(ProgramTypeTree.from(child))) {
                children.add(child);
            }
        }
        return List.copyOf(children);
    }

    private SplitScript programExpressionNode(ParserRuleContext expression, CommonTokenStream tokens) {
        Set<SplitQueryType> types = new TiDBSplitVisitor(this.provider.version()).collectTypes(expression);
        if (types.isEmpty()) {
            return null;
        }
        return createChild(expression, tokens, new LinkedHashSet<>(types), Collections.emptyList());
    }

    private record ProgramTypeTree(List<SplitQueryType> types, List<ProgramTypeTree> children) {

        private static ProgramTypeTree from(SplitScript script) {
            List<SplitScript> children = script.getChildren();
            List<ProgramTypeTree> childTypes = List.of();
            if (children != null) {
                childTypes = children.stream().map(ProgramTypeTree::from).toList();
            }
            return new ProgramTypeTree(List.copyOf(script.getType()), childTypes);
        }
    }

    private static void collectDirectProgramChildren(ParseTree tree, ParserRuleContext owner, List<ParserRuleContext> result) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (!(child instanceof ParserRuleContext context)) {
                continue;
            }
            if (tree == owner && owner instanceof TiDBParser.DeclareHandlerContext
                && (context instanceof TiDBParser.CompoundStatementContext || context instanceof TiDBParser.SqlStatementContext)) {
                result.add(context);
                continue;
            }
            if (context instanceof TiDBParser.ProcedureSqlStatementContext || context instanceof TiDBParser.DeclareVariableContext
                || context instanceof TiDBParser.DeclareConditionContext || context instanceof TiDBParser.DeclareCursorContext
                || context instanceof TiDBParser.DeclareHandlerContext) {
                result.add(context);
                continue;
            }
            if (isProgramControl(owner) && context instanceof TiDBParser.ExpressionContext) {
                result.add(context);
                continue;
            }
            if (isNestedProgramBoundary(context) && context != owner) {
                result.add(context);
                continue;
            }
            collectDirectProgramChildren(context, owner, result);
        }
    }

    private static boolean isNestedProgramBoundary(ParserRuleContext context) {
        return context instanceof TiDBParser.BlockStatementContext || isProgramControl(context);
    }

    private static boolean isProgramControl(ParserRuleContext context) {
        return context instanceof TiDBParser.CaseStatementContext || context instanceof TiDBParser.IfStatementContext
               || context instanceof TiDBParser.LoopStatementContext || context instanceof TiDBParser.RepeatStatementContext
               || context instanceof TiDBParser.WhileStatementContext || context instanceof TiDBParser.IterateStatementContext
               || context instanceof TiDBParser.LeaveStatementContext || context instanceof TiDBParser.ReturnStatementContext;
    }

    private static ParserRuleContext findProgramOwner(ParseTree tree) {
        if (tree instanceof TiDBParser.CreateProcedureContext || tree instanceof TiDBParser.CreateFunctionContext || tree instanceof TiDBParser.CreateTriggerContext
            || tree instanceof TiDBParser.CreateEventContext || tree instanceof TiDBParser.AlterEventContext) {
            return (ParserRuleContext) tree;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParserRuleContext owner = findProgramOwner(tree.getChild(i));
            if (owner != null) {
                return owner;
            }
        }
        return null;
    }

    private static boolean mayContainProgramOwner(ParserRuleContext context) {
        String firstToken = context.getStart().getText();
        return "CREATE".equalsIgnoreCase(firstToken) || "ALTER".equalsIgnoreCase(firstToken);
    }

    private static ParserRuleContext programBody(ParserRuleContext owner) {
        if (owner == null) {
            return null;
        }
        for (int i = 0; i < owner.getChildCount(); i++) {
            ParseTree child = owner.getChild(i);
            if (child instanceof TiDBParser.RoutineBodyContext || child instanceof TiDBParser.ReturnStatementContext) {
                return (ParserRuleContext) child;
            }
        }
        return null;
    }

    private static ParserRuleContext firstRuleChild(ParseTree context) {
        for (int i = 0; i < context.getChildCount(); i++) {
            if (context.getChild(i) instanceof ParserRuleContext child) {
                return child;
            }
        }
        throw new IllegalArgumentException("program node has no rule child: " + context.getClass().getSimpleName());
    }

    private static ParserRuleContext unwrapProgramStatement(ParserRuleContext context) {
        ParserRuleContext current = context;
        while (current instanceof TiDBParser.RoutineBodyContext || current instanceof TiDBParser.CompoundStatementContext) {
            current = firstRuleChild(current);
        }
        return current;
    }

    private static boolean containsContext(ParseTree tree, Class<? extends ParserRuleContext> contextType) {
        return findContext(tree, contextType) != null;
    }

    private static ParserRuleContext findContext(ParseTree tree, Class<? extends ParserRuleContext> contextType) {
        if (contextType.isInstance(tree)) {
            return (ParserRuleContext) tree;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParserRuleContext context = findContext(tree.getChild(i), contextType);
            if (context != null) {
                return context;
            }
        }
        return null;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((TiDBParser) parser).root();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof TiDBParser.SqlStatementContext && context.getParent() instanceof TiDBParser.SqlStatementsContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return this.provider.treeParser();
    }
}
