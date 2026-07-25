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
package com.clougence.clouddm.ds.dameng.sql.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;

public class DmSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    @Override
    protected DslProvider dslProvider() {
        return DmDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return DmSplitVisitor.INSTANCE;
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (!(tree instanceof DmSqlParser.AlterTableActionContext action)) {
            return null;
        }
        if (action.ADD() != null) {
            if (action.tableConstraint() != null) {
                return SplitQueryType.ADD_CONSTRAINT;
            }
            if (action.columnDefinition() != null || action.tableElementList() != null) {
                return SplitQueryType.ADD_COLUMN;
            }
            if (action.partitionAddAction() != null) {
                return SplitQueryType.ADD_PARTITION;
            }
        }
        if (action.MODIFY() != null && (action.columnDefinitionList() != null || action.columnDefinition() != null)) {
            return SplitQueryType.ALTER_COLUMN;
        }
        if (action.DROP() != null) {
            if (action.dropColumnTarget() != null) {
                return SplitQueryType.DROP_COLUMN;
            }
            if (action.CONSTRAINT() != null || action.PRIMARY() != null) {
                return SplitQueryType.DROP_CONSTRAINT;
            }
            if (action.partitionDropAction() != null) {
                return SplitQueryType.DROP_PARTITION;
            }
        }
        if (action.TRUNCATE() != null && action.alterPartitionTruncateTarget() != null) {
            return SplitQueryType.TRUNCATE_PARTITION;
        }
        if (action.partitionModifyAction() != null || action.SPLIT() != null || action.MERGE() != null || action.EXCHANGE() != null) {
            return SplitQueryType.ALTER_PARTITION;
        }
        return null;
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        DmSqlParser.ViewCreateContext view = findContext(context, DmSqlParser.ViewCreateContext.class);
        if (view != null && view.selectStatement() != null) {
            return List.of(createChild(view.selectStatement(), tokens, Set.of(SplitQueryType.SELECT), Collections.emptyList()));
        }

        ParseTree owner = findProgramOwner(context);
        if (owner == null) {
            return Collections.emptyList();
        }
        List<SplitScript> children = new ArrayList<>();
        collectProgramNodes(owner, tokens, children);
        DmSqlParser.SqlBlockStatementContext block = findContext(owner, DmSqlParser.SqlBlockStatementContext.class);
        if (block == null) {
            return children;
        }
        List<SplitScript> blockChildren = new ArrayList<>();
        collectBlockNodes(block, tokens, blockChildren);
        children.add(createChild(block, tokens, Set.of(SplitQueryType.BLOCK), blockChildren));
        return children;
    }

    private ParseTree findProgramOwner(ParseTree tree) {
        if (tree instanceof DmSqlParser.ProcedureCreateContext || tree instanceof DmSqlParser.FunctionCreateContext || tree instanceof DmSqlParser.TriggerCreateContext) {
            return tree;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree result = findProgramOwner(tree.getChild(i));
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private void collectProgramNodes(ParseTree tree, CommonTokenStream tokens, List<SplitScript> result) {
        if (tree instanceof DmSqlParser.SqlBlockStatementContext) {
            return;
        }
        if (tree instanceof DmSqlParser.BlockDeclarationContext declaration) {
            result.add(createChild(declaration, tokens, Set.of(SplitQueryType.PROGRAM_CONTROL), Collections.emptyList()));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectProgramNodes(tree.getChild(i), tokens, result);
        }
    }

    private void collectBlockNodes(ParseTree tree, CommonTokenStream tokens, List<SplitScript> result) {
        if (tree instanceof DmSqlParser.BlockSqlStatementContext sql) {
            SplitQueryType type = sql.accept(splitVisitor());
            result.add(createChild(sql, tokens, Set.of(type == null ? SplitQueryType.UNKNOWN : type), Collections.emptyList()));
            return;
        }
        if (tree instanceof DmSqlParser.IfStatementContext || tree instanceof DmSqlParser.LoopStatementContext || tree instanceof DmSqlParser.RepeatStatementContext
            || tree instanceof DmSqlParser.CaseControlStatementContext || tree instanceof DmSqlParser.ReturnStatementContext || tree instanceof DmSqlParser.NullStatementContext) {
            result.add(createChild((ParserRuleContext) tree, tokens, Set.of(SplitQueryType.PROGRAM_CONTROL), Collections.emptyList()));
        } else if (tree instanceof DmSqlParser.AssignmentStatementContext assignment) {
            DmSqlParser.TriggerPseudoRecordTargetContext target = assignment.assignmentTarget().triggerPseudoRecordTarget();
            SplitQueryType type = target != null && target.BIND_VARIABLE().getText().equalsIgnoreCase(":new") ? SplitQueryType.UPDATE : SplitQueryType.PROGRAM_CONTROL;
            result.add(createChild(assignment, tokens, Set.of(type), Collections.emptyList()));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectBlockNodes(tree.getChild(i), tokens, result);
        }
    }

    private <T extends ParserRuleContext> T findContext(ParseTree tree, Class<T> type) {
        if (type.isInstance(tree)) {
            return type.cast(tree);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            T result = findContext(tree.getChild(i), type);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((DmSqlParser) parser).sqlScript();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof DmSqlParser.StatementContext && context.getParent() instanceof DmSqlParser.StatementBlockContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new DmStatementParser();
    }

    @Override
    protected SplitQueryType normalizeType(SplitQueryType type, String script) {
        if (type == null || type == SplitQueryType.UNKNOWN) {
            throw new IllegalStateException("Dameng SQL parsed but query type was not classified: " + script);
        }
        return type;
    }
}
