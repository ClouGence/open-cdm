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
package com.clougence.sql.db2.analysis.behavior;

import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;
import com.clougence.sql.db2.parser.antlr.Db2SqlParser;
import com.clougence.sql.db2.parser.antlr.Db2SqlParserBaseVisitor;

public class Db2SplitVisitor extends Db2SqlParserBaseVisitor<StatementType> {

    public static final Db2SplitVisitor INSTANCE = new Db2SplitVisitor();

    @Override
    protected StatementType defaultResult() {
        return StatementType.UNKNOWN;
    }

    @Override
    protected StatementType aggregateResult(StatementType aggregate, StatementType nextResult) {
        if (aggregate != null && aggregate != StatementType.UNKNOWN) {
            return aggregate;
        }
        return nextResult == null ? StatementType.UNKNOWN : nextResult;
    }

    @Override
    public StatementType visitCreate_schema_statement(Db2SqlParser.Create_schema_statementContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitCreate_table_statement(Db2SqlParser.Create_table_statementContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitAlter_table_statement(Db2SqlParser.Alter_table_statementContext ctx) {
        return StatementType.ALTER_TABLE;
    }

    @Override
    public StatementType visitCreate_index_statement(Db2SqlParser.Create_index_statementContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitCreate_view_statement(Db2SqlParser.Create_view_statementContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitAlter_view_statement(Db2SqlParser.Alter_view_statementContext ctx) {
        return StatementType.ALTER_VIEW;
    }

    @Override
    public StatementType visitDrop_statement(Db2SqlParser.Drop_statementContext ctx) {
        if (ctx.schema_name() != null) {
            return StatementType.DROP_SCHEMA;
        }
        if (ctx.table_name() != null) {
            return StatementType.DROP_TABLE;
        }
        if (ctx.index_name() != null) {
            return StatementType.DROP_INDEX;
        }
        if (ctx.view_name() != null) {
            return StatementType.DROP_VIEW;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitRename_statement(Db2SqlParser.Rename_statementContext ctx) {
        return ctx.source_table_name() == null ? StatementType.UNKNOWN : StatementType.RENAME_TABLE;
    }

    @Override
    public StatementType visitTruncate_statement(Db2SqlParser.Truncate_statementContext ctx) {
        return StatementType.TRUNCATE_TABLE;
    }

    @Override
    public StatementType visitComment_statement(Db2SqlParser.Comment_statementContext ctx) {
        if (ctx.comment_objects() != null) {
            if (ctx.comment_objects().TABLE() != null) {
                return StatementType.COMMENT_TABLE;
            }
            if (ctx.comment_objects().COLUMN() != null) {
                return StatementType.COMMENT_COLUMN;
            }
        }
        if (!ctx.column_comment().isEmpty()) {
            return StatementType.COMMENT_COLUMN;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitCall_statement(Db2SqlParser.Call_statementContext ctx) {
        return StatementType.CALL_PROG_OBJ;
    }

    @Override
    public StatementType visitSelect_statement(Db2SqlParser.Select_statementContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitSelect_into_statement(Db2SqlParser.Select_into_statementContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitInsert_statement(Db2SqlParser.Insert_statementContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitInsert_datalake_statement(Db2SqlParser.Insert_datalake_statementContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitUpdate_statement(Db2SqlParser.Update_statementContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitUpdate_datalake_statement(Db2SqlParser.Update_datalake_statementContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitDelete_statement(Db2SqlParser.Delete_statementContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitDelete_deltalake_statement(Db2SqlParser.Delete_deltalake_statementContext ctx) {
        return StatementType.DELETE;
    }
}
