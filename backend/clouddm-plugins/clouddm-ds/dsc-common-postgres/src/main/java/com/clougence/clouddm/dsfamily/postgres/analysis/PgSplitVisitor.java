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
package com.clougence.clouddm.dsfamily.postgres.analysis;

import static com.clougence.clouddm.dsfamily.postgres.parser.antlr.PgSqlParser.*;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.dsfamily.postgres.parser.antlr.PgSqlParserBaseVisitor;

public class PgSplitVisitor extends PgSqlParserBaseVisitor<SecQueryType> {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new PgSplitVisitor();

    public PgSplitVisitor(){
    }

    @Override
    public SecQueryType visitDostmt(DostmtContext ctx) {
        return SecQueryType.SQL_BLOCK;
    }

    @Override
    public SecQueryType visitRefreshmatviewstmt(RefreshmatviewstmtContext ctx) {
        return SecQueryType.REFRESH_VIEW;
    }

    @Override
    public SecQueryType visitAnalyzestmt(AnalyzestmtContext ctx) {
        return SecQueryType.ANALYZE;
    }

    @Override
    public SecQueryType visitCreateseqstmt(CreateseqstmtContext ctx) {
        return SecQueryType.CREATE_SEQUENCE;
    }

    @Override
    public SecQueryType visitTruncatestmt(TruncatestmtContext ctx) {
        return SecQueryType.TRUNCATE;
    }

    @Override
    public SecQueryType visitAlterdatabasestmt(AlterdatabasestmtContext ctx) {
        return SecQueryType.ALTER_CATALOG;
    }

    @Override
    public SecQueryType visitAlterdatabasesetstmt(AlterdatabasesetstmtContext ctx) {
        return SecQueryType.ALTER_CATALOG;
    }

    @Override
    public SecQueryType visitRename_table_stmt(Rename_table_stmtContext ctx) {
        return SecQueryType.RENAME_TABLE;
    }

    @Override
    public SecQueryType visitRename_database_stmt(Rename_database_stmtContext ctx) {
        return SecQueryType.RENAME_CATALOG;
    }

    @Override
    public SecQueryType visitRename_column_stmt(Rename_column_stmtContext ctx) {
        return SecQueryType.ALTER_TABLE_RENAME_COLUMN;
    }

    @Override
    public SecQueryType visitRename_schema_stmt(Rename_schema_stmtContext ctx) {
        return SecQueryType.RENAME_SCHEMA;
    }

    @Override
    public SecQueryType visitComment_table_stmt(Comment_table_stmtContext ctx) {
        return SecQueryType.COMMENT_TABLE;
    }

    @Override
    public SecQueryType visitComment_column_stmt(Comment_column_stmtContext ctx) {
        return SecQueryType.COMMENT_COLUMN;
    }

    @Override
    public SecQueryType visitCreatedbstmt(CreatedbstmtContext ctx) {
        return SecQueryType.CREATE_CATALOG;
    }

    @Override
    public SecQueryType visitDropdbstmt(DropdbstmtContext ctx) {
        return SecQueryType.DROP_CATALOG;
    }

    @Override
    public SecQueryType visitCreateschemastmt(CreateschemastmtContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitVariableshowstmt(VariableshowstmtContext ctx) {
        return SecQueryType.SHOW;
    }

    @Override
    public SecQueryType visitDropschemastmt(DropschemastmtContext ctx) {
        return SecQueryType.DROP_SCHEMA;
    }

    @Override
    public SecQueryType visitAlterownerstmt(AlterownerstmtContext ctx) {
        ParseTree alterContext = ctx.getChild(1);
        if (alterContext instanceof TerminalNodeImpl) {
            TerminalNodeImpl childNode = (TerminalNodeImpl) alterContext;
            int type = childNode.getSymbol().getType();
            if (type == DATABASE) {
                return SecQueryType.ALTER_CATALOG;

            } else if (type == SCHEMA) {
                return SecQueryType.ALTER_SCHEMA;

            } else if (type == FUNCTION) {
                return SecQueryType.ALTER_FUNCTION;
            }
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitAlterobjectschemastmt(AlterobjectschemastmtContext ctx) {
        ParseTree alterContext = ctx.getChild(1);
        if (alterContext instanceof TerminalNodeImpl) {
            TerminalNodeImpl childNode = (TerminalNodeImpl) alterContext;
            if (childNode.getSymbol().getType() == FUNCTION) {
                return SecQueryType.ALTER_FUNCTION;

            }
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitCreatestmt(CreatestmtContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitCreateasstmt(CreateasstmtContext ctx) {
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitAltertablestmt(AltertablestmtContext ctx) {
        ParseTree alterContext = ctx.getChild(1);
        if (alterContext instanceof TerminalNodeImpl) {
            TerminalNodeImpl childNode = (TerminalNodeImpl) alterContext;
            if (childNode.getSymbol().getType() == TABLE) {
                return SecQueryType.ALTER_TABLE;

            } else if (childNode.getSymbol().getType() == INDEX) {
                return SecQueryType.ALTER_INDEX;

            } else if (childNode.getSymbol().getType() == VIEW) {
                return SecQueryType.ALTER_VIEW;

            }
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitDroptablestmt(DroptablestmtContext ctx) {
        return SecQueryType.DROP_TABLE;
    }

    @Override
    public SecQueryType visitDropstmt(DropstmtContext ctx) {
        ParseTree dropContext = ctx.getChild(1);
        if (dropContext instanceof TerminalNodeImpl) {
            TerminalNodeImpl childNode = (TerminalNodeImpl) dropContext;
            if (childNode.getSymbol().getType() == INDEX) {
                return SecQueryType.DROP_INDEX;
            } else if (childNode.getSymbol().getType() == VIEW) {
                return SecQueryType.DROP_VIEW;
            }
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitCreatetrigstmt(CreatetrigstmtContext ctx) {
        return SecQueryType.CREATE_TRIGGER;
    }

    @Override
    public SecQueryType visitIndexstmt(IndexstmtContext ctx) {
        return SecQueryType.CREATE_INDEX;
    }

    @Override
    public SecQueryType visitViewstmt(ViewstmtContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitCreatefunctionstmt(CreatefunctionstmtContext ctx) {
        if (ctx.PROCEDURE() != null) {
            return SecQueryType.CREATE_PROCEDURE;
        } else {
            return SecQueryType.CREATE_FUNCTION;
        }
    }

    @Override
    public SecQueryType visitCreatematviewstmt(CreatematviewstmtContext ctx) {
        return SecQueryType.CREATE_MATERIALIZED_VIEW;
    }

    @Override
    public SecQueryType visitSelectstmt(SelectstmtContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitInsertstmt(InsertstmtContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitUpdatestmt(UpdatestmtContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitDeletestmt(DeletestmtContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitCreateuserstmt(CreateuserstmtContext ctx) {
        return SecQueryType.CREATE_USER;
    }

    @Override
    public SecQueryType visitDropuserstmt(DropuserstmtContext ctx) {
        return SecQueryType.DROP_USER;
    }

    @Override
    public SecQueryType visitCreaterolestmt(CreaterolestmtContext ctx) {
        return SecQueryType.CREATE_ROLE;
    }

    @Override
    public SecQueryType visitDroprolestmt(DroprolestmtContext ctx) {
        return SecQueryType.DROP_ROLE;
    }

    @Override
    public SecQueryType visitGrantstmt(GrantstmtContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokestmt(RevokestmtContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitCallstmt(CallstmtContext ctx) {
        return SecQueryType.CALL;
    }

    @Override
    public SecQueryType visitChildren(RuleNode node) {
        int n = node.getChildCount();
        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            SecQueryType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }
        return SecQueryType.UNKNOWN;
    }

}
