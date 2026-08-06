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
package com.clougence.sql.mysql.parser;

import java.util.*;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;
import com.clougence.sql.mysql.parser.antlr.MySqlParser;

public class MySplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    private final MyDslProvider provider;

    public MySplitAnalysisSpi(MyDslProvider provider){
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
        return new MySplitVisitor(this.provider.version());
    }

    @Override
    protected Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        Set<SplitQueryType> types = new MySplitVisitor(this.provider.version()).collectTypes(context);
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
        if (context instanceof MySqlParser.RoutineBodyContext || context instanceof MySqlParser.CompoundStatementContext) {
            ParserRuleContext child = firstRuleChild(context);
            return programNode(child, tokens);
        }
        if (context instanceof MySqlParser.ProcedureSqlStatementContext) {
            ParserRuleContext statement = unwrapProgramStatement(firstRuleChild(context));
            return createProgramNode(context, statement, tokens);
        }
        return createProgramNode(context, context, tokens);
    }

    private SplitScript createProgramNode(ParserRuleContext range, ParserRuleContext statement, CommonTokenStream tokens) {
        statement = unwrapProgramStatement(statement);
        Set<SplitQueryType> types;
        List<SplitScript> children;
        if (statement instanceof MySqlParser.BlockStatementContext) {
            types = Collections.singleton(SplitQueryType.BLOCK);
            children = directProgramChildren(statement, tokens);
        } else if (isProgramControl(statement)) {
            types = Collections.singleton(SplitQueryType.PROGRAM_CONTROL);
            children = directProgramChildren(statement, tokens);
        } else if (statement instanceof MySqlParser.DeclareVariableContext || statement instanceof MySqlParser.DeclareConditionContext
                   || statement instanceof MySqlParser.DeclareCursorContext || statement instanceof MySqlParser.DeclareHandlerContext) {
            types = statement instanceof MySqlParser.DeclareCursorContext ? cursorTypes() : Collections.singleton(SplitQueryType.PROGRAM_CONTROL);
            children = statement instanceof MySqlParser.DeclareHandlerContext ? directProgramChildren(statement, tokens) : Collections.emptyList();
        } else if (statement instanceof MySqlParser.CursorStatementContext) {
            types = cursorTypes();
            children = Collections.emptyList();
        } else if (containsContext(statement, MySqlParser.SetNewValueInsideTriggerContext.class)) {
            types = Collections.singleton(SplitQueryType.UPDATE);
            children = Collections.emptyList();
        } else if (!mayContainProgramOwner(statement) && findContext(statement, MySqlParser.SetVariableContext.class) instanceof MySqlParser.SetVariableContext setVariable) {
            types = classifyRoutineSet(setVariable);
            children = Collections.emptyList();
        } else {
            types = new MySplitVisitor(this.provider.version()).collectTypes(statement);
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
        if (context instanceof MySqlParser.ViewQueryStatementContext) {
            return Collections.emptyList();
        }
        ParserRuleContext viewQuery = findContext(context, MySqlParser.ViewQueryStatementContext.class);
        return viewQuery == null ? Collections.emptyList() : List.of(programNode(viewQuery, tokens));
    }

    private Set<SplitQueryType> classifyRoutineSet(MySqlParser.SetVariableContext context) {
        Set<String> localNames = routineLocalNames(context);
        Set<SplitQueryType> result = new LinkedHashSet<>();
        for (MySqlParser.SetVariableAssignmentContext assignment : context.setVariableAssignment()) {
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
        while (owner != null && !(owner instanceof MySqlParser.CreateProcedureContext) && !(owner instanceof MySqlParser.CreateFunctionContext)
               && !(owner instanceof MySqlParser.CreateTriggerContext) && !(owner instanceof MySqlParser.CreateEventContext) && !(owner instanceof MySqlParser.AlterEventContext)) {
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
        if (tree instanceof MySqlParser.ProcedureParameterContext context) {
            names.add(normalizeIdentifier(context.uid().getText()));
            return;
        }
        if (tree instanceof MySqlParser.FunctionParameterContext context) {
            names.add(normalizeIdentifier(context.uid().getText()));
            return;
        }
        if (tree instanceof MySqlParser.DeclareVariableContext context) {
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
            SplitScript child = childContext instanceof MySqlParser.ExpressionContext ? programExpressionNode(childContext, tokens) : programNode(childContext, tokens);
            if (child != null && seen.add(ProgramTypeTree.from(child))) {
                children.add(child);
            }
        }
        return List.copyOf(children);
    }

    private SplitScript programExpressionNode(ParserRuleContext expression, CommonTokenStream tokens) {
        Set<SplitQueryType> types = new MySplitVisitor(this.provider.version()).collectTypes(expression);
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
            if (tree == owner && owner instanceof MySqlParser.DeclareHandlerContext
                && (context instanceof MySqlParser.CompoundStatementContext || context instanceof MySqlParser.SqlStatementContext)) {
                result.add(context);
                continue;
            }
            if (context instanceof MySqlParser.ProcedureSqlStatementContext || context instanceof MySqlParser.DeclareVariableContext
                || context instanceof MySqlParser.DeclareConditionContext || context instanceof MySqlParser.DeclareCursorContext
                || context instanceof MySqlParser.DeclareHandlerContext) {
                result.add(context);
                continue;
            }
            if (isProgramControl(owner) && context instanceof MySqlParser.ExpressionContext) {
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
        return context instanceof MySqlParser.BlockStatementContext || isProgramControl(context);
    }

    private static boolean isProgramControl(ParserRuleContext context) {
        return context instanceof MySqlParser.CaseStatementContext || context instanceof MySqlParser.IfStatementContext || context instanceof MySqlParser.LoopStatementContext
               || context instanceof MySqlParser.RepeatStatementContext || context instanceof MySqlParser.WhileStatementContext
               || context instanceof MySqlParser.IterateStatementContext || context instanceof MySqlParser.LeaveStatementContext
               || context instanceof MySqlParser.ReturnStatementContext;
    }

    private static ParserRuleContext findProgramOwner(ParseTree tree) {
        if (tree instanceof MySqlParser.CreateProcedureContext || tree instanceof MySqlParser.CreateFunctionContext || tree instanceof MySqlParser.CreateTriggerContext
            || tree instanceof MySqlParser.CreateEventContext || tree instanceof MySqlParser.AlterEventContext) {
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
            if (child instanceof MySqlParser.RoutineBodyContext || child instanceof MySqlParser.ReturnStatementContext) {
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
        while (current instanceof MySqlParser.RoutineBodyContext || current instanceof MySqlParser.CompoundStatementContext) {
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
        ((MySqlParser) parser).root();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof MySqlParser.SqlStatementContext && context.getParent() instanceof MySqlParser.SqlStatementsContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return this.provider.treeParser();
    }
}
