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
package com.clougence.sql.postgres.analysis.backup;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.backup.BackupStatement;
import com.clougence.clouddm.sdk.sql.backup.PriorBackupSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

public class PgPriorBackupSpi implements PriorBackupSpi {

    /** WHERE CURRENT OF cannot be converted into a backup SELECT. */
    private static final Pattern WHERE_CURRENT_OF = Pattern.compile("^WHERE\\s+CURRENT\\s+OF\\b", Pattern.CASE_INSENSITIVE);

    private final PgDslProvider  provider;

    public PgPriorBackupSpi(PostgresVersion version){
        this.provider = new PgDslProvider(version);
    }

    @Override
    public String name() {
        return "PG PriorBackup";
    }

    @Override
    public BackupStatement analysisBackup(String query) {
        List<AstSplitScript> scripts;
        try {
            scripts = DslHelper.splitDsl(provider, new StringReader(query));
        } catch (Exception e) {
            return null;
        }
        if (scripts.size() != 1) {
            return null;
        }

        ParseTree astTree = scripts.get(0).getAstTree();
        if (!(astTree instanceof PgSqlParser.StmtContext)) {
            return null;
        }
        PgSqlParser.StmtContext stmt = (PgSqlParser.StmtContext) astTree;
        if (stmt.updatestmt() != null) {
            return analysisUpdate(stmt.updatestmt());
        }
        if (stmt.deletestmt() != null) {
            return analysisDelete(stmt.deletestmt());
        }
        return null;
    }

    private BackupStatement analysisUpdate(PgSqlParser.UpdatestmtContext ctx) {
        // CTEs, additional FROM relations, and RETURNING are not simple single-table changes.
        if (ctx.with_clause_() != null || ctx.from_clause() != null || ctx.returning_clause() != null) {
            return null;
        }
        return buildStatement(SplitQueryType.UPDATE, ctx.relation_expr_opt_alias(), ctx.where_or_current_clause());
    }

    private BackupStatement analysisDelete(PgSqlParser.DeletestmtContext ctx) {
        // Skip CTEs, multi-table USING clauses, and RETURNING clauses.
        if (ctx.with_clause_() != null || ctx.using_clause() != null || ctx.returning_clause() != null) {
            return null;
        }
        return buildStatement(SplitQueryType.DELETE, ctx.relation_expr_opt_alias(), ctx.where_or_current_clause());
    }

    private BackupStatement buildStatement(SplitQueryType type, PgSqlParser.Relation_expr_opt_aliasContext relationCtx,
                                           PgSqlParser.Where_or_current_clauseContext whereCtx) {
        // Preserve the original alias so references in the WHERE clause remain valid.
        String fromText = originalText(relationCtx);
        String sourceTable = originalText(relationCtx.relation_expr().qualified_name());

        String whereText = null;
        if (whereCtx != null) {
            whereText = originalText(whereCtx);
            if (WHERE_CURRENT_OF.matcher(whereText).find()) {
                return null;
            }
        }

        BackupStatement statement = new BackupStatement();
        statement.setStatementType(type);
        statement.setSourceTable(sourceTable);
        statement.setHasWhere(whereText != null);
        if (whereText != null) {
            statement.setSelectSql("SELECT * FROM " + fromText + " " + whereText);
        } else {
            statement.setSelectSql("SELECT * FROM " + fromText);
        }
        return statement;
    }

    @Override
    public String reverseDdl(String query) {
        List<AstSplitScript> scripts;
        try {
            scripts = DslHelper.splitDsl(provider, new StringReader(query));
        } catch (Exception e) {
            return null;
        }
        if (scripts.size() != 1) {
            return null;
        }
        ParseTree astTree = scripts.get(0).getAstTree();
        if (!(astTree instanceof PgSqlParser.StmtContext)) {
            return null;
        }
        PgSqlParser.StmtContext stmt = (PgSqlParser.StmtContext) astTree;
        try {
            if (stmt.createstmt() != null) {
                return reverseCreateTable(stmt.createstmt());
            }
            if (stmt.indexstmt() != null) {
                return reverseCreateIndex(stmt.indexstmt());
            }
            if (stmt.altertablestmt() != null) {
                return reverseAlterTable(stmt.altertablestmt());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private String reverseCreateTable(PgSqlParser.CreatestmtContext ctx) {
        // Skip IF NOT EXISTS because the table may have existed before the statement.
        if (ctx.if_not_exists_() != null) {
            return null;
        }
        List<PgSqlParser.Qualified_nameContext> names = ctx.qualified_name();
        if (names == null || names.isEmpty()) {
            return null;
        }
        return "DROP TABLE " + originalText(names.get(0)) + ";";
    }

    private String reverseCreateIndex(PgSqlParser.IndexstmtContext ctx) {
        // Anonymous index names cannot be inferred, and IF NOT EXISTS may refer to an existing index.
        if (ctx.index_name_() == null || ctx.IF_P() != null) {
            return null;
        }
        String indexName = originalText(ctx.index_name_()).trim();
        // Qualify the index with the table schema when the table name is schema-qualified.
        String schemaPrefix = "";
        if (ctx.relation_expr() != null) {
            String table = originalText(ctx.relation_expr());
            int dotIndex = table.lastIndexOf('.');
            if (dotIndex > 0) {
                schemaPrefix = table.substring(0, dotIndex + 1);
            }
        }
        return "DROP INDEX " + schemaPrefix + indexName + ";";
    }

    private String reverseAlterTable(PgSqlParser.AltertablestmtContext ctx) {
        // Reverse only ALTER statements whose subcommands are all ADD COLUMN operations.
        if (ctx.relation_expr() == null || ctx.alter_table_cmds() == null) {
            return null;
        }
        String table = originalText(ctx.relation_expr());
        List<String> dropColumns = new ArrayList<>();
        for (PgSqlParser.Alter_table_cmdContext cmd : ctx.alter_table_cmds().alter_table_cmd()) {
            // The grammar labels ADD [COLUMN] as AddColumnContext, the only reversible form here.
            // Skip IF NOT EXISTS because the column may have existed before the statement.
            if (!(cmd instanceof PgSqlParser.AddColumnContext)) {
                return null;
            }
            PgSqlParser.AddColumnContext addColumn = (PgSqlParser.AddColumnContext) cmd;
            if (addColumn.IF_P() != null || addColumn.columnDef() == null) {
                return null;
            }
            dropColumns.add("ALTER TABLE " + table + " DROP COLUMN " + originalText(addColumn.columnDef().colid()) + ";");
        }
        if (dropColumns.isEmpty()) {
            return null;
        }
        Collections.reverse(dropColumns);
        return String.join("\n", dropColumns);
    }

    /** Returns the original source text while preserving case, quoting, and internal whitespace. */
    private String originalText(ParserRuleContext ctx) {
        return ctx.start.getInputStream().getText(Interval.of(ctx.start.getStartIndex(), ctx.stop.getStopIndex()));
    }
}
