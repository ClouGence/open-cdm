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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
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

    private static final Set<String> BUILTIN_FUNCTIONS = Set.of("""
        ABS ACOS ADD_DAYS ADD_MONTHS ADD_WEEKS ASCII ASCIISTR ASIN ATAN ATAN2 AVG BIGDATEDIFF BINTOCHAR BITAND BIT_LENGTH BLOB_EQUAL
        CAST CEIL CEILING CHAR CHARACTER_LENGTH CHAR_LENGTH CHR COALESCE COMPOSE CONCAT CONCAT_WS CONVERT CORR COS COSH COT COUNT COVAR_POP
        COVAR_SAMP CUME_DIST CURDATE CURRENT_DATE CURRENT_TIME CURRENT_TIMESTAMP CURTIME DATEADD DATEDIFF DATEPART DATE_ADD DATE_FORMAT DATE_PART
        DATE_SUB DAY DAYNAME DAYOFMONTH DAYOFWEEK DAYOFYEAR DAYS_BETWEEN DBTIMEZONE DECODE DEGREES DENSE_RANK DIFFERENCE DM_HASH EMPTY_BLOB
        EMPTY_CLOB EXP EXTRACT FIELD FIND_IN_SET FIRST_VALUE FLOOR FROM_TZ FROM_UNIXTIME GETDATE GREAT GREATEST HEXTORAW HOUR IF IFNULL INITCAP
        INS INSERT INSSTR INSTR INSTRB ISDATE ISNULL ISNUMERIC LAG LAST_DAY LAST_VALUE LCASE LEAD LEAST LEFT LEFTSTR LEN LENGTH LENGTH2 LENGTH4
        LENGTHB LENGTHC LISTAGG LN LNNVL LOCALTIME LOCALTIMESTAMP LOCATE LOG LOG10 LOWER LPAD LTRIM MAX MEDIAN MIN MINUTE MOD MONTH MONTHNAME
        MONTHS_BETWEEN NANVL NCHR NEXT_DAY NTH_VALUE NTILE NLSSORT NLS_UPPER NOW NULLIF NULL_EQU NUMTODSINTERVAL NUMTOYMINTERVAL NVL
        OCTET_LENGTH ORA_HASH OVERLAPS OVERLAY PERCENTILE_CONT PERCENTILE_DISC PERCENT_RANK PI POSITION POWER POWER2 QUARTER RADIANS RAND RANK
        RATIO_TO_REPORT RAWTOHEX REGEXP_COUNT REGEXP_INSTR REGEXP_LIKE REGEXP_REPLACE REGEXP_SUBSTR REGR_AVGX REGR_AVGY REGR_COUNT REGR_INTERCEPT
        REGR_R2 REGR_SLOPE REGR_SXX REGR_SXY REGR_SYY REMAINDER REPEAT REPEATSTR REPLACE REPLICATE REVERSE RIGHT RIGHTSTR ROUND ROW_NUMBER RPAD
        RTRIM SCORE SECOND SEC_TO_TIME SESSIONTIMEZONE SIGN SIN SINH SOUNDEX SPACE SQRT STDDEV STDDEV_POP STDDEV_SAMP STRPOSDEC STRPOSINC STUFF SUBSTR
        SUBSTRB SUBSTRING SUBSTRING_INDEX SUM SYSDATE SYSTIMESTAMP SYS_EXTRACT_UTC TAN TANH TEXT_EQUAL TIMESTAMPADD TIMESTAMPDIFF TIME_TO_SEC
        TO_BINARY_DOUBLE TO_BINARY_FLOAT TO_BLOB TO_CHAR TO_DATE TO_DAYS TO_DSINTERVAL TO_MULTI_BYTE TO_NUMBER TO_SINGLE_BYTE TO_TIMESTAMP
        TO_TIMESTAMP_TZ TO_YMINTERVAL TRANSLATE TRIM TRUNC TRUNCATE TZ_OFFSET UCASE UNHEX UNISTR UNIX_TIMESTAMP UPPER VARIANCE VAR_POP VAR_SAMP
        SUPER THIS WEEK WEEKDAY WEEKS_BETWEEN WIDTH_BUCKET YEAR YEARS_BETWEEN
        """.trim().split("\\s+"));

    @Override
    protected DslProvider dslProvider() {
        return DmDslProvider.INSTANCE;
    }

    @Override
    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return DmSplitVisitor.INSTANCE;
    }

    @Override
    protected Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        Set<SplitQueryType> types = new LinkedHashSet<>();
        SplitQueryType primaryType = normalizeType(context.accept(splitVisitor()), script);
        types.add(primaryType);
        if (primaryType == SplitQueryType.BLOCK) {
            return types;
        }
        boolean viewDefinition = findContext(context, DmSqlParser.ViewCreateContext.class) != null;
        boolean programDefinition = findProgramOwner(context) != null;
        collectAdditionalTypes(context, context, types, viewDefinition, programDefinition);
        return types;
    }

    private void collectAdditionalTypes(ParseTree root, ParseTree tree, Set<SplitQueryType> types,
                                        boolean viewDefinition, boolean programDefinition) {
        if (tree != root && isDefinitionExecutionBody(tree, viewDefinition, programDefinition)) {
            return;
        }
        SplitQueryType type = additionalType(tree);
        if (type != null) {
            types.add(type);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectAdditionalTypes(root, tree.getChild(i), types, viewDefinition, programDefinition);
        }
    }

    private boolean isDefinitionExecutionBody(ParseTree tree, boolean viewDefinition, boolean programDefinition) {
        if (viewDefinition && tree instanceof DmSqlParser.SelectStatementContext) {
            return true;
        }
        if (!programDefinition) {
            return false;
        }
        return tree instanceof DmSqlParser.RoutineDefinitionContext
            || tree instanceof DmSqlParser.TriggerCreateTailContext;
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (tree instanceof DmSqlParser.SelectStatementContext) {
            return SplitQueryType.SELECT;
        }
        if (tree instanceof DmSqlParser.ForUpdateClauseContext ctx && ctx.UPDATE() != null) {
            return SplitQueryType.QUERY_LOCK;
        }
        if (tree instanceof DmSqlParser.FunctionCallContext ctx && isUserFunction(ctx)) {
            return SplitQueryType.CALL_PROG_OBJ;
        }
        if (tree instanceof DmSqlParser.ColumnDefinitionContext
            || tree instanceof DmSqlParser.CtasColumnDefinitionContext
            || tree instanceof DmSqlParser.HugeColumnDefinitionContext) {
            return isCreateOrAddDefinition(tree) ? SplitQueryType.ADD_COLUMN : null;
        }
        if (tree instanceof DmSqlParser.TableConstraintContext
            || tree instanceof DmSqlParser.CtasTableConstraintContext
            || tree instanceof DmSqlParser.HugeTableConstraintContext
            || tree instanceof DmSqlParser.UniqueSpecContext
            || tree instanceof DmSqlParser.ReferenceConstraintContext
            || isCheckConstraint(tree)) {
            return isConstraintDefinition(tree) ? SplitQueryType.ADD_CONSTRAINT : null;
        }
        if (isColumnComment(tree)) {
            return SplitQueryType.COMMENT_COLUMN;
        }
        if (!(tree instanceof DmSqlParser.AlterTableActionContext action)) {
            return null;
        }
        String actionText = action.getText().toUpperCase(java.util.Locale.ROOT);
        if (actionText.startsWith("RENAMECOLUMN")) {
            return SplitQueryType.RENAME_COLUMN;
        }
        if (actionText.startsWith("RENAMECONSTRAINT")) {
            return SplitQueryType.RENAME_CONSTRAINT;
        }
        if (actionText.startsWith("RENAMETO")) {
            return SplitQueryType.RENAME_TABLE;
        }
        if (actionText.startsWith("RENAMEPARTITION") || actionText.startsWith("RENAMESUBPARTITION")) {
            return SplitQueryType.ALTER_PARTITION;
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
        if (action.MODIFY() != null && (action.modifyColumnDefinitionList() != null || action.modifyColumnDefinition() != null)) {
            return SplitQueryType.ALTER_COLUMN;
        }
        if (action.MODIFY() != null && action.CONSTRAINT() != null) {
            return SplitQueryType.ALTER_CONSTRAINT;
        }
        if (action.ALTER() != null) {
            return actionText.contains("RENAMETO") ? SplitQueryType.RENAME_COLUMN : SplitQueryType.ALTER_COLUMN;
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
            if (action.IDENTITY() != null || action.AUTO_INCREMENT() != null) {
                return SplitQueryType.ALTER_COLUMN;
            }
        }
        if ((action.ENABLE() != null || action.DISABLE() != null)
            && action.CONSTRAINT() != null) {
            return SplitQueryType.ALTER_CONSTRAINT;
        }
        if (action.TRUNCATE() != null && action.alterPartitionTruncateTarget() != null) {
            return SplitQueryType.TRUNCATE_PARTITION;
        }
        if (action.partitionModifyAction() != null || action.SPLIT() != null || action.MERGE() != null || action.EXCHANGE() != null) {
            return SplitQueryType.ALTER_PARTITION;
        }
        return null;
    }

    private boolean isUserFunction(DmSqlParser.FunctionCallContext context) {
        String name = context.name.getText();
        if (name.indexOf('.') >= 0) {
            return true;
        }
        if (name.length() >= 2 && name.charAt(0) == '"' && name.charAt(name.length() - 1) == '"') {
            name = name.substring(1, name.length() - 1);
        }
        return !BUILTIN_FUNCTIONS.contains(name.toUpperCase(Locale.ROOT));
    }

    private boolean isColumnComment(ParseTree tree) {
        if (!(tree instanceof DmSqlParser.ColumnAttributeContext
            || tree instanceof DmSqlParser.ColumnTailClauseContext
            || tree instanceof DmSqlParser.CtasColumnAttributeContext
            || tree instanceof DmSqlParser.HugeColumnAttributeContext)) {
            return false;
        }
        return tree.getChildCount() > 0 && "COMMENT".equalsIgnoreCase(tree.getChild(0).getText());
    }

    private boolean isCreateOrAddDefinition(ParseTree tree) {
        ParseTree parent = tree.getParent();
        while (parent != null) {
            if (parent instanceof DmSqlParser.TableCreateContext) {
                return true;
            }
            if (parent instanceof DmSqlParser.AlterTableActionContext action) {
                return action.ADD() != null;
            }
            parent = parent.getParent();
        }
        return false;
    }

    private boolean isConstraintDefinition(ParseTree tree) {
        if (isCreateOrAddDefinition(tree)) {
            return true;
        }
        ParseTree parent = tree.getParent();
        while (parent != null) {
            if (parent instanceof DmSqlParser.AlterTableActionContext action) {
                return action.MODIFY() != null;
            }
            parent = parent.getParent();
        }
        return false;
    }

    private boolean isCheckConstraint(ParseTree tree) {
        if (tree instanceof DmSqlParser.ColumnConstraintActionContext context) {
            return context.CHECK() != null;
        }
        if (tree instanceof DmSqlParser.CtasColumnConstraintActionContext context) {
            return context.CHECK() != null;
        }
        return false;
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        if (context instanceof DmSqlParser.StatementContext) {
            DmSqlParser.SqlBlockStatementContext block = findContext(context, DmSqlParser.SqlBlockStatementContext.class);
            if (block != null && findProgramOwner(context) == null) {
                return directProgramChildren(block, tokens);
            }
        }

        DmSqlParser.ClassBodyInitializerContext initializer =
            findContext(context, DmSqlParser.ClassBodyInitializerContext.class);
        if (initializer != null) {
            return directProgramChildren(initializer, tokens);
        }

        ParseTree owner = findProgramOwner(context);
        if (owner != null) {
            List<SplitScript> children = new ArrayList<>();
            collectProgramDeclarations(owner, tokens, children);
            DmSqlParser.SqlBlockStatementContext block = findContext(owner, DmSqlParser.SqlBlockStatementContext.class);
            if (block != null) {
                children.add(createProgramNode(block, tokens));
            }
            return children;
        }

        DmSqlParser.ViewCreateContext view = findContext(context, DmSqlParser.ViewCreateContext.class);
        if (view != null && view.selectStatement() != null) {
            String script = tokens.getText(view.selectStatement().getStart(), view.selectStatement().getStop());
            return List.of(createChild(view.selectStatement(), tokens, collectTypes(view.selectStatement(), script), Collections.emptyList()));
        }
        return Collections.emptyList();
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

    private void collectProgramDeclarations(ParseTree tree, CommonTokenStream tokens, List<SplitScript> result) {
        if (tree instanceof DmSqlParser.SqlBlockStatementContext) {
            return;
        }
        if (tree instanceof DmSqlParser.BlockDeclarationContext declaration) {
            result.add(createProgramNode(declaration, tokens));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectProgramDeclarations(tree.getChild(i), tokens, result);
        }
    }

    private SplitScript createProgramNode(ParserRuleContext context, CommonTokenStream tokens) {
        Set<SplitQueryType> types = new LinkedHashSet<>();
        List<SplitScript> children = Collections.emptyList();
        if (context instanceof DmSqlParser.SqlBlockStatementContext || context instanceof DmSqlParser.CStyleBlockStatementContext) {
            types.add(SplitQueryType.BLOCK);
            children = directProgramChildren(context, tokens);
        } else if (isProgramControl(context)) {
            types.add(SplitQueryType.PROGRAM_CONTROL);
            children = directProgramChildren(context, tokens);
        } else if (context instanceof DmSqlParser.BlockDeclarationContext) {
            if (findContext(context, DmSqlParser.PackageCursorDeclarationContext.class) != null) {
                types.add(SplitQueryType.SELECT);
            }
            types.add(SplitQueryType.PROGRAM_CONTROL);
        } else if (context instanceof DmSqlParser.ExecuteImmediateStatementContext) {
            types.add(SplitQueryType.UNSAFE);
        } else if (context instanceof DmSqlParser.OpenCursorStatementContext
            || context instanceof DmSqlParser.FetchCursorStatementContext
            || context instanceof DmSqlParser.CloseCursorStatementContext) {
            types.add(SplitQueryType.SELECT);
            types.add(SplitQueryType.PROGRAM_CONTROL);
        } else if (context instanceof DmSqlParser.AssignmentStatementContext assignment) {
            DmSqlParser.TriggerPseudoRecordTargetContext target = assignment.assignmentTarget().triggerPseudoRecordTarget();
            types.add(target != null && target.BIND_VARIABLE().getText().equalsIgnoreCase(":new")
                ? SplitQueryType.UPDATE : SplitQueryType.PROGRAM_CONTROL);
            collectExpressionTypes(context, types);
        } else if (context instanceof DmSqlParser.BlockSqlStatementContext) {
            String script = tokens.getText(context.getStart(), context.getStop());
            types.addAll(collectTypes(context, script));
            children = collectChildren(context, tokens);
        } else {
            String script = tokens.getText(context.getStart(), context.getStop());
            types.addAll(collectTypes(context, script));
            children = collectChildren(context, tokens);
        }
        if (types.isEmpty()) {
            types.add(SplitQueryType.UNKNOWN);
        }
        return createChild(context, tokens, types, children);
    }

    private List<SplitScript> directProgramChildren(ParserRuleContext owner, CommonTokenStream tokens) {
        List<ParserRuleContext> contexts = new ArrayList<>();
        collectDirectProgramChildren(owner, owner, contexts);
        List<SplitScript> children = new ArrayList<>();
        for (ParserRuleContext context : contexts) {
            if (context instanceof DmSqlParser.ExpressionContext) {
                Set<SplitQueryType> expressionTypes = new LinkedHashSet<>();
                collectExpressionTypes(context, expressionTypes);
                if (!expressionTypes.isEmpty()) {
                    children.add(createChild(context, tokens, expressionTypes, Collections.emptyList()));
                }
            } else {
                children.add(createProgramNode(context, tokens));
            }
        }
        return List.copyOf(children);
    }

    private void collectDirectProgramChildren(ParseTree tree, ParserRuleContext owner, List<ParserRuleContext> result) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (!(child instanceof ParserRuleContext context)) {
                continue;
            }
            if (isProgramControl(owner) && context instanceof DmSqlParser.ExpressionContext) {
                result.add(context);
                continue;
            }
            if (context != owner && isProgramBoundary(context)) {
                result.add(context);
                continue;
            }
            collectDirectProgramChildren(context, owner, result);
        }
    }

    private boolean isProgramBoundary(ParserRuleContext context) {
        return context instanceof DmSqlParser.SqlBlockStatementContext
            || context instanceof DmSqlParser.CStyleBlockStatementContext
            || context instanceof DmSqlParser.BlockSqlStatementContext
            || context instanceof DmSqlParser.BlockDeclarationContext
            || context instanceof DmSqlParser.AssignmentStatementContext
            || context instanceof DmSqlParser.ExecuteImmediateStatementContext
            || context instanceof DmSqlParser.OpenCursorStatementContext
            || context instanceof DmSqlParser.FetchCursorStatementContext
            || context instanceof DmSqlParser.CloseCursorStatementContext
            || isProgramControl(context);
    }

    private boolean isProgramControl(ParserRuleContext context) {
        return context instanceof DmSqlParser.IfStatementContext
            || context instanceof DmSqlParser.LoopStatementContext
            || context instanceof DmSqlParser.RepeatStatementContext
            || context instanceof DmSqlParser.CaseControlStatementContext
            || context instanceof DmSqlParser.GotoStatementContext
            || context instanceof DmSqlParser.ExitStatementContext
            || context instanceof DmSqlParser.ContinueStatementContext
            || context instanceof DmSqlParser.NullStatementContext
            || context instanceof DmSqlParser.ForallStatementContext
            || context instanceof DmSqlParser.RaiseStatementContext
            || context instanceof DmSqlParser.ReturnStatementContext
            || context instanceof DmSqlParser.PrintStatementContext
            || context instanceof DmSqlParser.PipeRowStatementContext;
    }

    private void collectExpressionTypes(ParseTree tree, Set<SplitQueryType> types) {
        SplitQueryType type = additionalType(tree);
        if (type == SplitQueryType.SELECT || type == SplitQueryType.CALL_PROG_OBJ) {
            types.add(type);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectExpressionTypes(tree.getChild(i), types);
        }
    }

    private <T extends ParserRuleContext> T findContext(ParseTree tree, Class<T> type) {
        if (tree == null) {
            return null;
        }
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

}
