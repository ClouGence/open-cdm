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
package com.clougence.clouddm.ds.gauss.sql.analysis.behavior;

import static com.clougence.clouddm.ds.gauss.sql.parser.antlr.GaussSqlParser.*;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.ds.gauss.sql.parser.antlr.GaussSqlParserBaseVisitor;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;

public class GaussSplitVisitor extends GaussSqlParserBaseVisitor<StatementType> {
    public GaussSplitVisitor(){
    }

    @Override
    public StatementType visitDostmt(DostmtContext ctx) {
        return StatementType.BLOCK;
    }

    @Override
    public StatementType visitRefreshmatviewstmt(RefreshmatviewstmtContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitAnalyzestmt(AnalyzestmtContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitCreatepolicystmt(CreatepolicystmtContext ctx) {
        return StatementType.CREATE_POLICY;
    }

    @Override
    public StatementType visitAlterpolicystmt(AlterpolicystmtContext ctx) {
        return StatementType.ALTER_POLICY;
    }

    @Override
    public StatementType visitCreateseqstmt(CreateseqstmtContext ctx) {
        return StatementType.CREATE_SEQUENCE;
    }

    @Override
    public StatementType visitTruncatestmt(TruncatestmtContext ctx) {
        return StatementType.TRUNCATE_TABLE;
    }

    @Override
    public StatementType visitAlterdatabasestmt(AlterdatabasestmtContext ctx) {
        return StatementType.ALTER_CATALOG;
    }

    @Override
    public StatementType visitAlterdatabasesetstmt(AlterdatabasesetstmtContext ctx) {
        return StatementType.ALTER_CATALOG;
    }

    @Override
    public StatementType visitRename_table_stmt(Rename_table_stmtContext ctx) {
        return StatementType.RENAME_TABLE;
    }

    @Override
    public StatementType visitRename_database_stmt(Rename_database_stmtContext ctx) {
        return StatementType.RENAME_CATALOG;
    }

    @Override
    public StatementType visitRename_column_stmt(Rename_column_stmtContext ctx) {
        return StatementType.RENAME_COLUMN;
    }

    @Override
    public StatementType visitRename_schema_stmt(Rename_schema_stmtContext ctx) {
        return StatementType.RENAME_SCHEMA;
    }

    @Override
    public StatementType visitComment_table_stmt(Comment_table_stmtContext ctx) {
        return StatementType.COMMENT_TABLE;
    }

    @Override
    public StatementType visitComment_column_stmt(Comment_column_stmtContext ctx) {
        return StatementType.COMMENT_COLUMN;
    }

    @Override
    public StatementType visitCreatedbstmt(CreatedbstmtContext ctx) {
        return StatementType.CREATE_CATALOG;
    }

    @Override
    public StatementType visitDropdbstmt(DropdbstmtContext ctx) {
        return StatementType.DROP_CATALOG;
    }

    @Override
    public StatementType visitCreateschemastmt(CreateschemastmtContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitVariableshowstmt(VariableshowstmtContext ctx) {
        return StatementType.SESSION_VARIABLE_RW;
    }

    @Override
    public StatementType visitDropschemastmt(DropschemastmtContext ctx) {
        return StatementType.DROP_SCHEMA;
    }

    @Override
    public StatementType visitAlterownerstmt(AlterownerstmtContext ctx) {
        if (ctx.aggregate_with_argtypes() != null || ctx.function_with_argtypes() != null || ctx.operator_with_argtypes() != null) {
            return StatementType.ALTER_PROG_OBJ;
        }
        ParseTree alterContext = ctx.getChild(1);
        if (alterContext instanceof TerminalNodeImpl childNode) {
            int type = childNode.getSymbol().getType();
            if (type == DATABASE) {
                return StatementType.ALTER_CATALOG;

            } else if (type == SCHEMA) {
                return StatementType.ALTER_SCHEMA;

            } else if (type == FUNCTION) {
                return StatementType.ALTER_PROG_OBJ;

            } else if (type == PUBLICATION) {
                return StatementType.ALTER_PUB_SUB;

            } else if (type == SUBSCRIPTION) {
                return StatementType.ALTER_PUB_SUB;
            }
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitAlterobjectschemastmt(AlterobjectschemastmtContext ctx) {
        if (ctx.aggregate_with_argtypes() != null || ctx.function_with_argtypes() != null || ctx.operator_with_argtypes() != null) {
            return StatementType.ALTER_PROG_OBJ;
        }
        ParseTree alterContext = ctx.getChild(1);
        if (alterContext instanceof TerminalNodeImpl childNode) {
            if (childNode.getSymbol().getType() == FUNCTION) {
                return StatementType.ALTER_PROG_OBJ;

            }
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitCreatestmt(CreatestmtContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitCreateasstmt(CreateasstmtContext ctx) {
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitAltertablestmt(AltertablestmtContext ctx) {
        ParseTree alterContext = ctx.getChild(1);
        if (alterContext instanceof TerminalNodeImpl childNode) {
            if (childNode.getSymbol().getType() == TABLE) {
                return StatementType.ALTER_TABLE;

            } else if (childNode.getSymbol().getType() == INDEX) {
                return StatementType.ALTER_INDEX;

            } else if (childNode.getSymbol().getType() == VIEW) {
                return StatementType.ALTER_VIEW;

            }
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitDroptablestmt(DroptablestmtContext ctx) {
        return StatementType.DROP_TABLE;
    }

    @Override
    public StatementType visitDropstmt(DropstmtContext ctx) {
        if (hasToken(ctx, INDEX)) {
            return StatementType.DROP_INDEX;
        } else if (hasToken(ctx, VIEW)) {
            return StatementType.DROP_VIEW;
        } else if (hasToken(ctx, POLICY)) {
            return StatementType.DROP_POLICY;
        } else if (hasToken(ctx, PUBLICATION)) {
            return StatementType.DROP_PUB_SUB;
        }
        return StatementType.UNKNOWN;
    }

    private boolean hasToken(ParseTree tree, int type) {
        if (tree instanceof TerminalNodeImpl childNode) {
            return childNode.getSymbol().getType() == type;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            if (hasToken(tree.getChild(i), type)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public StatementType visitCreatepublicationstmt(CreatepublicationstmtContext ctx) {
        return StatementType.CREATE_PUB_SUB;
    }

    @Override
    public StatementType visitAlterpublicationstmt(AlterpublicationstmtContext ctx) {
        return StatementType.ALTER_PUB_SUB;
    }

    @Override
    public StatementType visitCreatesubscriptionstmt(CreatesubscriptionstmtContext ctx) {
        return StatementType.CREATE_PUB_SUB;
    }

    @Override
    public StatementType visitAltersubscriptionstmt(AltersubscriptionstmtContext ctx) {
        if (ctx.ENABLE_P() != null || ctx.DISABLE_P() != null || ctx.REFRESH() != null) {
            return StatementType.ADMIN_PUB_SUB;
        }
        return StatementType.ALTER_PUB_SUB;
    }

    @Override
    public StatementType visitDropsubscriptionstmt(DropsubscriptionstmtContext ctx) {
        return StatementType.DROP_PUB_SUB;
    }

    @Override
    public StatementType visitCreatetrigstmt(CreatetrigstmtContext ctx) {
        return StatementType.CREATE_TRIGGER;
    }

    @Override
    public StatementType visitIndexstmt(IndexstmtContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitViewstmt(ViewstmtContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitCreatefunctionstmt(CreatefunctionstmtContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitAlterfunctionstmt(AlterfunctionstmtContext ctx) {
        return StatementType.ALTER_PROG_OBJ;
    }

    @Override
    public StatementType visitAlteroperatorstmt(AlteroperatorstmtContext ctx) {
        return StatementType.ALTER_PROG_OBJ;
    }

    @Override
    public StatementType visitDefinestmt(DefinestmtContext ctx) {
        if (ctx.AGGREGATE() != null || ctx.OPERATOR() != null) {
            return StatementType.CREATE_PROG_OBJ;
        } else if (ctx.TYPE_P() != null) {
            return StatementType.CREATE_TYPE;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitRemovefuncstmt(RemovefuncstmtContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitRemoveaggrstmt(RemoveaggrstmtContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitRemoveoperstmt(RemoveoperstmtContext ctx) {
        return StatementType.DROP_PROG_OBJ;
    }

    @Override
    public StatementType visitAlterobjectdependsstmt(AlterobjectdependsstmtContext ctx) {
        if (ctx.function_with_argtypes() != null) {
            return StatementType.ALTER_PROG_OBJ;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitCreatematviewstmt(CreatematviewstmtContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitSelectstmt(SelectstmtContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitInsertstmt(InsertstmtContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitUpdatestmt(UpdatestmtContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitDeletestmt(DeletestmtContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitCreateuserstmt(CreateuserstmtContext ctx) {
        return StatementType.CREATE_USER;
    }

    @Override
    public StatementType visitDropuserstmt(DropuserstmtContext ctx) {
        return StatementType.DROP_USER;
    }

    @Override
    public StatementType visitCreaterolestmt(CreaterolestmtContext ctx) {
        return StatementType.CREATE_ROLE;
    }

    @Override
    public StatementType visitDroprolestmt(DroprolestmtContext ctx) {
        return StatementType.DROP_ROLE;
    }

    @Override
    public StatementType visitGrantstmt(GrantstmtContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitRevokestmt(RevokestmtContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitCallstmt(CallstmtContext ctx) {
        return StatementType.CALL_PROG_OBJ;
    }

    @Override
    public StatementType visitChildren(RuleNode node) {
        int n = node.getChildCount();
        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            StatementType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }
        return StatementType.UNKNOWN;
    }

}
