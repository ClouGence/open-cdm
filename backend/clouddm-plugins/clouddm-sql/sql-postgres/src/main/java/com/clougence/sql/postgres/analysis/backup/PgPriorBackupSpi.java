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

    /** WHERE CURRENT OF cursor 形态无法转成 SELECT，排除备份 */
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
        // CTE、多表 FROM 参照、RETURNING 均不是纯单表变更，跳过备份
        if (ctx.with_clause_() != null || ctx.from_clause() != null || ctx.returning_clause() != null) {
            return null;
        }
        return buildStatement(SplitQueryType.UPDATE, ctx.relation_expr_opt_alias(), ctx.where_or_current_clause());
    }

    private BackupStatement analysisDelete(PgSqlParser.DeletestmtContext ctx) {
        // CTE、USING 多表、RETURNING 跳过备份
        if (ctx.with_clause_() != null || ctx.using_clause() != null || ctx.returning_clause() != null) {
            return null;
        }
        return buildStatement(SplitQueryType.DELETE, ctx.relation_expr_opt_alias(), ctx.where_or_current_clause());
    }

    private BackupStatement buildStatement(SplitQueryType type, PgSqlParser.Relation_expr_opt_aliasContext relationCtx,
                                           PgSqlParser.Where_or_current_clauseContext whereCtx) {
        // FROM 片段保留语句里的别名原文，保证 WHERE 中的别名引用在备份 SELECT 中依然成立
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
        // IF NOT EXISTS 语义下表可能本来就存在，逆向 DROP 会误删旧表，跳过
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
        // 匿名索引名字由数据库生成，无法静态推导；IF NOT EXISTS 同 CREATE TABLE 的顾虑，均跳过
        if (ctx.index_name_() == null || ctx.IF_P() != null) {
            return null;
        }
        String indexName = originalText(ctx.index_name_()).trim();
        // 索引与表同 schema：表名带 schema 前缀时，DROP INDEX 也补同样前缀
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
        // 只逆向"全部子命令都是 ADD COLUMN"的 ALTER：混入其他操作（DROP/ALTER TYPE/ADD CONSTRAINT）时整条放弃
        if (ctx.relation_expr() == null || ctx.alter_table_cmds() == null) {
            return null;
        }
        String table = originalText(ctx.relation_expr());
        List<String> dropColumns = new ArrayList<>();
        for (PgSqlParser.Alter_table_cmdContext cmd : ctx.alter_table_cmds().alter_table_cmd()) {
            // 文法按 label 生成子类：只有 ADD [COLUMN]（AddColumnContext）可逆；
            // IF NOT EXISTS 存在时列可能本来就有，逆向 DROP 会误删旧列，整条放弃
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

    /** 从原始输入流取 ctx 的原文，保留大小写、引号和内部空白 */
    private String originalText(ParserRuleContext ctx) {
        return ctx.start.getInputStream().getText(Interval.of(ctx.start.getStartIndex(), ctx.stop.getStopIndex()));
    }
}
