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
package com.clougence.clouddm.ds.starrocks.sql.parser;

import java.util.Collections;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.starrocks.sql.parser.antlr.StarRocksParser;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AntlrStatementParser;
import com.clougence.sql.common.parser.AbstractSplitAnalysisSpi;

public class SrSplitAnalysisSpi extends AbstractSplitAnalysisSpi {

    private static final Set<String> KNOWN_USER_FUNCTIONS = Set.of("ADS_VERSION", "TEST", "TEST_FUNC", "TEST_FUNC1", "TEST_FUNCTION");

    // StarRocks 3.5.21 Config.java: getConfigInfo exposes these values without masking.
    // Credential IDs, public paths and switches are deliberately not treated as secrets.
    private static final Set<String> SECRET_FE_CONFIGS = Set.of(
        "authentication_ldap_simple_ssl_conn_trust_store_pwd", "authentication_ldap_simple_bind_root_pwd",
        "auth_token", "default_master_key", "aws_s3_secret_key", "azure_blob_shared_key", "azure_blob_sas_token",
        "azure_adls2_shared_key", "azure_adls2_sas_token", "azure_adls2_oauth2_client_secret",
        "gcp_gcs_service_account_private_key", "ssl_keystore_password", "ssl_key_password",
        "ssl_truststore_password", "oauth2_client_secret");

    protected DslProvider dslProvider() {
        return SrDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<SplitQueryType> splitVisitor() {
        return SrSplitVisitor.INSTANCE;
    }

    @Override
    protected Set<SplitQueryType> collectTypes(ParserRuleContext context, String script) {
        ParseTree statement = context;
        if (context instanceof StarRocksParser.StatementContext) {
            statement = context.getChild(0);
        }
        if (SrSplitVisitor.isPlanOnly(statement)) {
            // EXPLAIN and TRACE do not execute their query/DML bodies.
            return Set.of(SplitQueryType.PERFORMANCE);
        }
        if (statement instanceof StarRocksParser.AdminRepairTableStatementContext repair) {
            return collectRepairTypes(repair);
        }
        if (statement instanceof StarRocksParser.CreateDataCacheRuleStatementContext) {
            // Rule predicates are policy definitions, not executed queries.
            return Set.of(SplitQueryType.ADMIN_PERFORMANCE);
        }
        if (statement instanceof StarRocksParser.SubmitTaskStatementContext
            || statement instanceof StarRocksParser.CreatePipeStatementContext
            || statement instanceof StarRocksParser.CreateViewStatementContext
            || statement instanceof StarRocksParser.AlterViewStatementContext
            || statement instanceof StarRocksParser.CreateMaterializedViewStatementContext
            || statement instanceof StarRocksParser.CreateRoutineLoadStatementContext
            || statement instanceof StarRocksParser.AlterRoutineLoadStatementContext) {
            // SQL definition bodies belong to children; load mappings/source options only define the job.
            return Set.of(statement.accept(splitVisitor()));
        }
        boolean collectQueryActions = statement instanceof StarRocksParser.SetStatementContext
            || statement instanceof StarRocksParser.DataCacheSelectStatementContext
            || statement instanceof StarRocksParser.QueryStatementContext
            || statement instanceof StarRocksParser.AlterPlanAdvisorAddStatementContext;
        if (statement instanceof StarRocksParser.InsertStatementContext insert && insert.explainDesc() != null) {
            // ANALYZE runs the load before aborting its transaction; its source actions still execute.
            collectQueryActions = insert.explainDesc().ANALYZE() != null;
        }
        if (!collectQueryActions) {
            return super.collectTypes(context, script);
        }

        Set<SplitQueryType> types = new LinkedHashSet<>();
        types.add(normalizeType(statement.accept(splitVisitor())));
        collectSessionTypes(statement, types);
        return types;
    }

    private Set<SplitQueryType> collectRepairTypes(StarRocksParser.AdminRepairTableStatementContext repair) {
        boolean dryRun = false;
        if (repair.properties() != null) {
            for (StarRocksParser.PropertyContext property : repair.properties().property()) {
                if ("dry_run".equals(decodeSqlString(property.key.getText()))) {
                    dryRun = Boolean.parseBoolean(decodeSqlString(property.value.getText()));
                }
            }
        }
        if (dryRun) {
            // Lake repair only probes a plan in this mode, even with destructive recovery options.
            return Set.of(SplitQueryType.PERFORMANCE);
        }
        Set<SplitQueryType> types = new LinkedHashSet<>();
        types.add(repair.accept(splitVisitor()));
        if (repair.properties() != null) {
            // Repair properties are lake-only; execution can roll back versions or recover empty tablets.
            types.add(SplitQueryType.UNSAFE);
        }
        return types;
    }

    private void collectSessionTypes(ParseTree tree, Set<SplitQueryType> types) {
        SplitQueryType type = null;
        if (tree instanceof StarRocksParser.SetVarContext variable) {
            type = variable.accept(splitVisitor());
        } else if (tree instanceof StarRocksParser.UserVariableContext) {
            type = SplitQueryType.SESSION_VARIABLE_RW;
        } else if (tree instanceof StarRocksParser.SystemVariableContext variable) {
            // A SET target is already covered by SETTING_WRITE; only expression reads add RW.
            if (!(variable.getParent() instanceof StarRocksParser.SetSystemVarContext)
                && (variable.varType() == null || variable.varType().GLOBAL() == null)) {
                type = SplitQueryType.SESSION_VARIABLE_RW;
            }
        } else if (tree instanceof StarRocksParser.QueryRelationContext) {
            type = SplitQueryType.SELECT;
        }
        if (type == null) {
            type = additionalType(tree);
        }
        if (type != null) {
            types.add(type);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectSessionTypes(tree.getChild(i), types);
        }
    }

    @Override
    protected SplitQueryType additionalType(ParseTree tree) {
        if (tree instanceof StarRocksParser.InsertStatementContext ctx) {
            return containsContext(ctx, StarRocksParser.QueryRelationContext.class) ? SplitQueryType.SELECT : null;
        }
        if (tree instanceof StarRocksParser.UpdateStatementContext ctx) {
            return containsContext(ctx, StarRocksParser.QueryRelationContext.class) ? SplitQueryType.SELECT : null;
        }
        if (tree instanceof StarRocksParser.DeleteStatementContext ctx) {
            return containsContext(ctx, StarRocksParser.QueryRelationContext.class) ? SplitQueryType.SELECT : null;
        }
        if (tree instanceof StarRocksParser.FileTableFunctionContext) {
            // FILES in a source query reads external data. Planning alone does not import it.
            for (ParseTree parent = tree.getParent(); parent != null; parent = parent.getParent()) {
                if (SrSplitVisitor.isPlanOnly(parent)) {
                    return null;
                }
            }
            return SplitQueryType.DATA_IMPORT;
        }
        if (tree instanceof StarRocksParser.DescPipeStatementContext) {
            // DESC PIPE exposes original INSERT SQL, including unmasked FILES credentials.
            return SplitQueryType.ADMIN;
        }
        if (tree instanceof StarRocksParser.AdminSetAutomatedSnapshotOffStatementContext) {
            // Turning the policy off also purges historical cluster snapshots.
            return SplitQueryType.UNSAFE;
        }
        if (tree instanceof StarRocksParser.CompactionClauseContext ctx) {
            if (ctx.identifier() != null || ctx.identifierList() != null) {
                return SplitQueryType.ADMIN_PARTITION;
            }
            return SplitQueryType.ADMIN_TABLE;
        }
        if (tree instanceof StarRocksParser.AdminSetPartitionVersionContext) {
            return SplitQueryType.UNSAFE;
        }
        if (tree instanceof StarRocksParser.DataCacheSelectStatementContext) {
            return SplitQueryType.SELECT;
        }
        if (tree instanceof StarRocksParser.AddColumnClauseContext || tree instanceof StarRocksParser.AddColumnsClauseContext) {
            return SplitQueryType.ADD_COLUMN;
        }
        if (tree instanceof StarRocksParser.DropColumnClauseContext) {
            return SplitQueryType.DROP_COLUMN;
        }
        if (tree instanceof StarRocksParser.ModifyColumnClauseContext || tree instanceof StarRocksParser.ModifyColumnCommentClauseContext) {
            return SplitQueryType.ALTER_COLUMN;
        }
        if (tree instanceof StarRocksParser.ColumnRenameClauseContext) {
            return SplitQueryType.RENAME_COLUMN;
        }
        if (tree instanceof StarRocksParser.CreateIndexClauseContext) {
            return SplitQueryType.ADD_INDEX;
        }
        if (tree instanceof StarRocksParser.DropIndexClauseContext) {
            return SplitQueryType.DROP_INDEX;
        }
        if (tree instanceof StarRocksParser.TableRenameClauseContext) {
            return SplitQueryType.RENAME_TABLE;
        }
        if (tree instanceof StarRocksParser.ModifyCommentClauseContext) {
            return SplitQueryType.COMMENT_TABLE;
        }
        if (tree instanceof StarRocksParser.AddPartitionClauseContext) {
            return SplitQueryType.ADD_PARTITION;
        }
        if (tree instanceof StarRocksParser.DropPartitionClauseContext) {
            return SplitQueryType.DROP_PARTITION;
        }
        if (tree instanceof StarRocksParser.TruncatePartitionClauseContext) {
            return SplitQueryType.TRUNCATE_PARTITION;
        }
        if (tree instanceof StarRocksParser.ModifyPartitionClauseContext || tree instanceof StarRocksParser.DistributionClauseContext
            || tree instanceof StarRocksParser.ReplacePartitionClauseContext || tree instanceof StarRocksParser.PartitionRenameClauseContext) {
            return SplitQueryType.ALTER_PARTITION;
        }
        if (tree instanceof StarRocksParser.AlterTableStatementContext ctx && ctx.ROLLUP() != null) {
            return ctx.ADD() != null ? SplitQueryType.ADD_INDEX : SplitQueryType.DROP_INDEX;
        }
        if (tree instanceof StarRocksParser.CreateTableStatementContext ctx && ctx.comment() != null) {
            return SplitQueryType.COMMENT_TABLE;
        }
        if (tree instanceof StarRocksParser.CreateTableAsSelectStatementContext ctx && ctx.comment() != null) {
            return SplitQueryType.COMMENT_TABLE;
        }
        if (tree instanceof StarRocksParser.CreateRoleStatementContext ctx && ctx.comment() != null) {
            return SplitQueryType.COMMENT_ROLE;
        }
        if (tree instanceof StarRocksParser.CreateIndexStatementContext ctx && ctx.comment() != null) {
            return SplitQueryType.COMMENT_INDEX;
        }
        if (tree instanceof StarRocksParser.IndexDescContext ctx && ctx.comment() != null) {
            return SplitQueryType.COMMENT_INDEX;
        }
        if (tree instanceof StarRocksParser.CommentContext ctx) {
            if (hasAncestor(ctx, StarRocksParser.CreateIndexClauseContext.class)) {
                return SplitQueryType.COMMENT_INDEX;
            }
            if (hasAncestor(ctx, StarRocksParser.AddColumnClauseContext.class) || hasAncestor(ctx, StarRocksParser.AddColumnsClauseContext.class)
                || hasAncestor(ctx, StarRocksParser.ModifyColumnClauseContext.class) || hasAncestor(ctx, StarRocksParser.ModifyColumnCommentClauseContext.class)) {
                return SplitQueryType.COMMENT_COLUMN;
            }
        }
        if (tree instanceof StarRocksParser.CreateExternalCatalogStatementContext ctx && hasProperty(ctx, "driver_url")) {
            return SplitQueryType.UNSAFE;
        }
        if (tree instanceof StarRocksParser.CreateResourceStatementContext ctx && hasProperty(ctx, "driver_url")) {
            return SplitQueryType.UNSAFE;
        }
        if (tree instanceof StarRocksParser.CreateFileStatementContext) {
            return SplitQueryType.DATA_IMPORT;
        }
        if (tree instanceof StarRocksParser.AdminShowConfigStatementContext ctx && exposesConfigSecrets(ctx)) {
            return SplitQueryType.ADMIN;
        }
        if (tree instanceof StarRocksParser.SimpleFunctionCallContext ctx && KNOWN_USER_FUNCTIONS.contains(ctx.qualifiedName().getText().toUpperCase(Locale.ROOT))) {
            return SplitQueryType.CALL_PROG_OBJ;
        }
        return null;
    }

    @Override
    protected List<SplitScript> collectChildren(ParserRuleContext context, CommonTokenStream tokens) {
        ParserRuleContext body = definitionBody(context);
        if (body == null) {
            return Collections.emptyList();
        }
        String script = tokens.getText(body.getStart(), body.getStop());
        return List.of(createChild(body, tokens, collectTypes(body, script), collectChildren(body, tokens)));
    }

    private ParserRuleContext definitionBody(ParseTree tree) {
        if (tree instanceof StarRocksParser.SubmitTaskStatementContext ctx) {
            if (ctx.createTableAsSelectStatement() != null) {
                return ctx.createTableAsSelectStatement();
            }
            if (ctx.insertStatement() != null) {
                return ctx.insertStatement();
            }
            return ctx.dataCacheSelectStatement();
        }
        if (tree instanceof StarRocksParser.CreatePipeStatementContext ctx) {
            return ctx.insertStatement();
        }
        if (tree instanceof StarRocksParser.CreateViewStatementContext ctx) {
            return ctx.queryStatement();
        }
        if (tree instanceof StarRocksParser.AlterViewStatementContext ctx) {
            return ctx.queryStatement();
        }
        if (tree instanceof StarRocksParser.CreateMaterializedViewStatementContext ctx) {
            return ctx.queryStatement();
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParserRuleContext body = definitionBody(tree.getChild(i));
            if (body != null) {
                return body;
            }
        }
        return null;
    }

    private boolean exposesConfigSecrets(StarRocksParser.AdminShowConfigStatementContext ctx) {
        if (ctx.pattern == null) {
            return true;
        }
        String like = decodeSqlString(ctx.pattern.getText());
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < like.length(); i++) {
            char ch = like.charAt(i);
            if (ch == '\\' && i + 1 < like.length()) {
                // LIKE removes escape prefixes, after SQL string decoding.
                regex.append(Pattern.quote(String.valueOf(like.charAt(++i))));
            } else if (ch == '%') {
                regex.append(".*");
            } else if (ch == '_') {
                regex.append('.');
            } else {
                regex.append(Pattern.quote(String.valueOf(ch)));
            }
        }
        // Config names are case-sensitive (CaseSensibility.CONFIG in the upstream code).
        Pattern pattern = Pattern.compile(regex.toString());
        return SECRET_FE_CONFIGS.stream().anyMatch(key -> pattern.matcher(key).matches());
    }

    private String decodeSqlString(String raw) {
        String quote = raw.substring(0, 1);
        String text = raw.substring(1, raw.length() - 1).replace(quote + quote, quote);
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch != '\\' || i + 1 == text.length()) {
                value.append(ch);
                continue;
            }
            char escaped = text.charAt(++i);
            // StarRocks AstBuilder preserves backslashes before LIKE wildcards.
            switch (escaped) {
                case 'n' -> value.append('\n');
                case 'r' -> value.append('\r');
                case 't' -> value.append('\t');
                case 'b' -> value.append('\b');
                case '0' -> value.append('\0');
                case 'Z' -> value.append('\032');
                case '_', '%' -> value.append('\\').append(escaped);
                default -> value.append(escaped);
            }
        }
        return value.toString();
    }

    private boolean hasProperty(ParseTree tree, String key) {
        if (tree instanceof StarRocksParser.PropertyContext ctx) {
            String rawKey = ctx.key.getText();
            String propertyKey = rawKey.length() >= 2 ? rawKey.substring(1, rawKey.length() - 1) : rawKey;
            return key.equalsIgnoreCase(propertyKey);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (hasProperty(tree.getChild(i), key)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAncestor(ParseTree tree, Class<? extends ParserRuleContext> type) {
        ParseTree parent = tree.getParent();
        while (parent != null) {
            if (type.isInstance(parent)) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    private boolean containsContext(ParseTree tree, Class<? extends ParserRuleContext> type) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseTree child = tree.getChild(i);
            if (type.isInstance(child) || containsContext(child, type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void parseRoot(Parser parser) {
        ((StarRocksParser) parser).sqlStatements();
    }

    @Override
    protected boolean isStatementContext(ParserRuleContext context) {
        return context instanceof StarRocksParser.StatementContext && context.getParent() instanceof StarRocksParser.SingleStatementContext;
    }

    @Override
    protected AntlrStatementParser statementParser() {
        return new SrStatementParser();
    }
}
