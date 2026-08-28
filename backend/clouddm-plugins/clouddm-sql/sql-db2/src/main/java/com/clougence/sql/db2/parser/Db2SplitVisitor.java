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

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.db2.parser.antlr.Db2SqlParser;
import com.clougence.sql.db2.parser.antlr.Db2SqlParserBaseVisitor;

public class Db2SplitVisitor extends Db2SqlParserBaseVisitor<SplitQueryType> {

    public static final Db2SplitVisitor INSTANCE = new Db2SplitVisitor();

    @Override
    protected SplitQueryType defaultResult() {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    protected SplitQueryType aggregateResult(SplitQueryType aggregate, SplitQueryType nextResult) {
        if (aggregate != null && aggregate != SplitQueryType.UNKNOWN) {
            return aggregate;
        }
        return nextResult == null ? SplitQueryType.UNKNOWN : nextResult;
    }

    @Override
    public SplitQueryType visitCreate_schema_statement(Db2SqlParser.Create_schema_statementContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitCreate_table_statement(Db2SqlParser.Create_table_statementContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitAlter_table_statement(Db2SqlParser.Alter_table_statementContext ctx) {
        return SplitQueryType.ALTER_TABLE;
    }

    @Override
    public SplitQueryType visitCreate_index_statement(Db2SqlParser.Create_index_statementContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitCreate_view_statement(Db2SqlParser.Create_view_statementContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitAlter_view_statement(Db2SqlParser.Alter_view_statementContext ctx) {
        return SplitQueryType.ALTER_VIEW;
    }

    @Override
    public SplitQueryType visitDrop_statement(Db2SqlParser.Drop_statementContext ctx) {
        if (ctx.schema_name() != null) {
            return SplitQueryType.DROP_SCHEMA;
        }
        if (ctx.table_name() != null) {
            return SplitQueryType.DROP_TABLE;
        }
        if (ctx.index_name() != null) {
            return SplitQueryType.DROP_INDEX;
        }
        if (ctx.view_name() != null) {
            return SplitQueryType.DROP_VIEW;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitRename_statement(Db2SqlParser.Rename_statementContext ctx) {
        return ctx.source_table_name() == null ? SplitQueryType.UNKNOWN : SplitQueryType.RENAME_TABLE;
    }

    @Override
    public SplitQueryType visitTruncate_statement(Db2SqlParser.Truncate_statementContext ctx) {
        return SplitQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SplitQueryType visitComment_statement(Db2SqlParser.Comment_statementContext ctx) {
        if (ctx.comment_objects() != null) {
            if (ctx.comment_objects().TABLE() != null) {
                return SplitQueryType.COMMENT_TABLE;
            }
            if (ctx.comment_objects().COLUMN() != null) {
                return SplitQueryType.COMMENT_COLUMN;
            }
        }
        if (!ctx.column_comment().isEmpty()) {
            return SplitQueryType.COMMENT_COLUMN;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitCall_statement(Db2SqlParser.Call_statementContext ctx) {
        return SplitQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitExplain_statement(Db2SqlParser.Explain_statementContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitSelect_statement(Db2SqlParser.Select_statementContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitSelect_into_statement(Db2SqlParser.Select_into_statementContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitInsert_statement(Db2SqlParser.Insert_statementContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitInsert_datalake_statement(Db2SqlParser.Insert_datalake_statementContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitUpdate_statement(Db2SqlParser.Update_statementContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitUpdate_datalake_statement(Db2SqlParser.Update_datalake_statementContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitDelete_statement(Db2SqlParser.Delete_statementContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitDelete_deltalake_statement(Db2SqlParser.Delete_deltalake_statementContext ctx) {
        return SplitQueryType.DELETE;
    }
}
