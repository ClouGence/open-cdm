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
package com.clougence.sql.db2.parser;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.sql.db2.parser.antlr.Db2SqlParser;
import com.clougence.sql.db2.parser.antlr.Db2SqlParserBaseVisitor;

public class Db2SplitVisitor extends Db2SqlParserBaseVisitor<SecQueryType> {

    public static final Db2SplitVisitor INSTANCE = new Db2SplitVisitor();

    @Override
    protected SecQueryType defaultResult() {
        return SecQueryType.UNKNOWN;
    }

    @Override
    protected SecQueryType aggregateResult(SecQueryType aggregate, SecQueryType nextResult) {
        if (aggregate != null && aggregate != SecQueryType.UNKNOWN) {
            return aggregate;
        }
        return nextResult == null ? SecQueryType.UNKNOWN : nextResult;
    }

    @Override
    public SecQueryType visitCreate_schema_statement(Db2SqlParser.Create_schema_statementContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitCreate_table_statement(Db2SqlParser.Create_table_statementContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitAlter_table_statement(Db2SqlParser.Alter_table_statementContext ctx) {
        return SecQueryType.ALTER_TABLE;
    }

    @Override
    public SecQueryType visitCreate_index_statement(Db2SqlParser.Create_index_statementContext ctx) {
        return SecQueryType.ADD_INDEX;
    }

    @Override
    public SecQueryType visitCreate_view_statement(Db2SqlParser.Create_view_statementContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitAlter_view_statement(Db2SqlParser.Alter_view_statementContext ctx) {
        return SecQueryType.ALTER_VIEW;
    }

    @Override
    public SecQueryType visitDrop_statement(Db2SqlParser.Drop_statementContext ctx) {
        if (ctx.schema_name() != null) {
            return SecQueryType.DROP_SCHEMA;
        }
        if (ctx.table_name() != null) {
            return SecQueryType.DROP_TABLE;
        }
        if (ctx.index_name() != null) {
            return SecQueryType.DROP_INDEX;
        }
        if (ctx.view_name() != null) {
            return SecQueryType.DROP_VIEW;
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitRename_statement(Db2SqlParser.Rename_statementContext ctx) {
        return ctx.source_table_name() == null ? SecQueryType.UNKNOWN : SecQueryType.RENAME_TABLE;
    }

    @Override
    public SecQueryType visitTruncate_statement(Db2SqlParser.Truncate_statementContext ctx) {
        return SecQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SecQueryType visitComment_statement(Db2SqlParser.Comment_statementContext ctx) {
        if (ctx.comment_objects() != null) {
            if (ctx.comment_objects().TABLE() != null) {
                return SecQueryType.COMMENT_TABLE;
            }
            if (ctx.comment_objects().COLUMN() != null) {
                return SecQueryType.COMMENT_COLUMN;
            }
        }
        if (!ctx.column_comment().isEmpty()) {
            return SecQueryType.COMMENT_COLUMN;
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitCall_statement(Db2SqlParser.Call_statementContext ctx) {
        return SecQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SecQueryType visitSelect_statement(Db2SqlParser.Select_statementContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitSelect_into_statement(Db2SqlParser.Select_into_statementContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitInsert_statement(Db2SqlParser.Insert_statementContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitInsert_datalake_statement(Db2SqlParser.Insert_datalake_statementContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitUpdate_statement(Db2SqlParser.Update_statementContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitUpdate_datalake_statement(Db2SqlParser.Update_datalake_statementContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitDelete_statement(Db2SqlParser.Delete_statementContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitDelete_deltalake_statement(Db2SqlParser.Delete_deltalake_statementContext ctx) {
        return SecQueryType.DELETE;
    }
}
