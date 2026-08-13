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
package com.clougence.sql.mysql.analysis.backup;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.backup.BackupStatement;
import com.clougence.clouddm.sdk.sql.backup.PriorBackupSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.sql.mysql.parser.MyDslProvider;
import com.clougence.sql.mysql.parser.MySqlParserConfig;
import com.clougence.sql.mysql.parser.antlr.MySqlParser;

/**
 * MySQL 方言的 prior backup 解析：与 PG 版语义对齐。
 * 备份仅覆盖纯单表 UPDATE/DELETE；多表变更、CTE、ORDER BY/LIMIT（影响行集与备份 SELECT 不保证一致）、
 * PARTITION 子句形态一律返回 null 跳过。DDL 逆语句覆盖建表/建索引/加列。
 */
public class MyPriorBackupSpi implements PriorBackupSpi {

    private final MyDslProvider provider;

    public MyPriorBackupSpi(MySqlParserConfig config){
        this.provider = new MyDslProvider(config);
    }

    @Override
    public String name() {
        return "MySQL PriorBackup";
    }

    @Override
    public BackupStatement analysisBackup(String query) {
        MySqlParser.SqlStatementContext stmt = parseSingle(query);
        MySqlParser.DmlStatementContext dml = stmt != null ? stmt.dmlStatement() : null;
        if (dml == null) {
            return null;
        }
        if (dml.updateStatement() != null) {
            return analysisUpdate(dml.updateStatement());
        }
        if (dml.deleteStatement() != null) {
            return analysisDelete(dml.deleteStatement());
        }
        return null;
    }

    private BackupStatement analysisUpdate(MySqlParser.UpdateStatementContext ctx) {
        // 多表 UPDATE 无法用单一 SELECT 备份，跳过
        MySqlParser.SingleUpdateStatementContext single = ctx.singleUpdateStatement();
        if (single == null) {
            return null;
        }
        // CTE、PARTITION、ORDER BY、LIMIT 形态的受影响行集与备份 SELECT 不保证一致，跳过
        if (single.withClause() != null || single.uidList() != null || single.orderByClause() != null || single.limitClause() != null) {
            return null;
        }
        String tableText = originalText(single.tableName());
        String aliasText = single.uid() != null ? " " + originalText(single.uid()) : "";
        return buildStatement(SplitQueryType.UPDATE, tableText, tableText + aliasText, single.whereClause());
    }

    private BackupStatement analysisDelete(MySqlParser.DeleteStatementContext ctx) {
        MySqlParser.SingleDeleteStatementContext single = ctx.singleDeleteStatement();
        if (single == null) {
            return null;
        }
        if (single.withClause() != null || single.uidList() != null || single.orderByClause() != null || single.limitClauseAtom() != null) {
            return null;
        }
        String tableText = originalText(single.tableName());
        String aliasText = single.deleteTableAlias() != null ? " " + originalText(single.deleteTableAlias()) : "";
        return buildStatement(SplitQueryType.DELETE, tableText, tableText + aliasText, single.whereClause());
    }

    private BackupStatement buildStatement(SplitQueryType type, String sourceTable, String fromText, MySqlParser.WhereClauseContext whereCtx) {
        BackupStatement statement = new BackupStatement();
        statement.setStatementType(type);
        statement.setSourceTable(sourceTable);
        statement.setHasWhere(whereCtx != null);
        if (whereCtx != null) {
            statement.setSelectSql("SELECT * FROM " + fromText + " " + originalText(whereCtx));
        } else {
            statement.setSelectSql("SELECT * FROM " + fromText);
        }
        return statement;
    }

    @Override
    public String reverseDdl(String query) {
        MySqlParser.SqlStatementContext stmt = parseSingle(query);
        if (stmt == null || stmt.ddlStatement() == null) {
            return null;
        }
        MySqlParser.DdlStatementContext ddl = stmt.ddlStatement();
        try {
            if (ddl.createTable() != null) {
                return reverseCreateTable(ddl.createTable());
            }
            if (ddl.createIndex() != null) {
                return reverseCreateIndex(ddl.createIndex());
            }
            if (ddl.alterTable() != null) {
                return reverseAlterTable(ddl.alterTable());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private String reverseCreateTable(MySqlParser.CreateTableContext ctx) {
        // 三种建表形态（常规/CTAS/LIKE 复制）都建出了新表，逆操作统一 DROP；
        // IF NOT EXISTS 语义下表可能本来就存在，逆向 DROP 会误删旧表，跳过
        if (ctx instanceof MySqlParser.ColumnCreateTableContext) {
            MySqlParser.ColumnCreateTableContext create = (MySqlParser.ColumnCreateTableContext) ctx;
            return create.ifNotExists() != null ? null : "DROP TABLE " + originalText(create.tableName()) + ";";
        }
        if (ctx instanceof MySqlParser.QueryCreateTableContext) {
            MySqlParser.QueryCreateTableContext create = (MySqlParser.QueryCreateTableContext) ctx;
            return create.ifNotExists() != null ? null : "DROP TABLE " + originalText(create.tableName()) + ";";
        }
        if (ctx instanceof MySqlParser.CopyCreateTableContext) {
            MySqlParser.CopyCreateTableContext create = (MySqlParser.CopyCreateTableContext) ctx;
            if (create.ifNotExists() != null) {
                return null;
            }
            // LIKE 复制形态有两个 tableName，文法顺序第一个是新建的表
            List<MySqlParser.TableNameContext> names = create.tableName();
            if (names == null || names.isEmpty()) {
                return null;
            }
            return "DROP TABLE " + originalText(names.get(0)) + ";";
        }
        return null;
    }

    private String reverseCreateIndex(MySqlParser.CreateIndexContext ctx) {
        if (ctx.indexName() == null || ctx.tableName() == null) {
            return null;
        }
        // MySQL 删索引语法要求带表名
        return "DROP INDEX " + originalText(ctx.indexName()) + " ON " + originalText(ctx.tableName()) + ";";
    }

    private String reverseAlterTable(MySqlParser.AlterTableContext ctx) {
        // 只逆向"全部子命令都是 ADD COLUMN"的 ALTER，混入其他操作时整条放弃
        if (ctx.tableName() == null || ctx.alterSpecification() == null || ctx.alterSpecification().isEmpty()) {
            return null;
        }
        String table = originalText(ctx.tableName());
        List<String> dropColumns = new ArrayList<>();
        for (MySqlParser.AlterSpecificationContext spec : ctx.alterSpecification()) {
            if (spec instanceof MySqlParser.AlterByAddColumnContext) {
                MySqlParser.AlterByAddColumnContext addColumn = (MySqlParser.AlterByAddColumnContext) spec;
                dropColumns.add("ALTER TABLE " + table + " DROP COLUMN " + originalText(addColumn.columnDefinition().uid()) + ";");
            } else if (spec instanceof MySqlParser.AlterByAddColumnsContext) {
                MySqlParser.AlterByAddColumnsContext addColumns = (MySqlParser.AlterByAddColumnsContext) spec;
                for (MySqlParser.ColumnDefinitionContext columnDefinition : addColumns.columnDefinition()) {
                    dropColumns.add("ALTER TABLE " + table + " DROP COLUMN " + originalText(columnDefinition.uid()) + ";");
                }
            } else {
                return null;
            }
        }
        if (dropColumns.isEmpty()) {
            return null;
        }
        Collections.reverse(dropColumns);
        return String.join("\n", dropColumns);
    }

    /** 解析并要求恰好一条语句，返回其 SqlStatementContext；解析失败或多条返回 null */
    private MySqlParser.SqlStatementContext parseSingle(String query) {
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
        if (!(astTree instanceof MySqlParser.SqlStatementContext)) {
            return null;
        }
        return (MySqlParser.SqlStatementContext) astTree;
    }

    /** 从原始输入流取 ctx 的原文，保留大小写、引号和内部空白 */
    private String originalText(ParserRuleContext ctx) {
        return ctx.start.getInputStream().getText(Interval.of(ctx.start.getStartIndex(), ctx.stop.getStopIndex()));
    }
}
