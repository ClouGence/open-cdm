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
package com.clougence.clouddm.ds.gauss.sql.parser;

import static com.clougence.clouddm.ds.gauss.sql.parser.antlr.GaussSqlParser.*;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.ds.gauss.sql.parser.antlr.GaussSqlParserBaseVisitor;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

public class GaussSplitVisitor extends GaussSqlParserBaseVisitor<SplitQueryType> {
    public GaussSplitVisitor(){
    }

    @Override
    public SplitQueryType visitDostmt(DostmtContext ctx) {
        return SplitQueryType.BLOCK;
    }

    @Override
    public SplitQueryType visitRefreshmatviewstmt(RefreshmatviewstmtContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitAnalyzestmt(AnalyzestmtContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitCreatepolicystmt(CreatepolicystmtContext ctx) {
        return SplitQueryType.CREATE_POLICY;
    }

    @Override
    public SplitQueryType visitAlterpolicystmt(AlterpolicystmtContext ctx) {
        return SplitQueryType.ALTER_POLICY;
    }

    @Override
    public SplitQueryType visitCreateseqstmt(CreateseqstmtContext ctx) {
        return SplitQueryType.CREATE_SEQUENCE;
    }

    @Override
    public SplitQueryType visitTruncatestmt(TruncatestmtContext ctx) {
        return SplitQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SplitQueryType visitAlterdatabasestmt(AlterdatabasestmtContext ctx) {
        return SplitQueryType.ALTER_CATALOG;
    }

    @Override
    public SplitQueryType visitAlterdatabasesetstmt(AlterdatabasesetstmtContext ctx) {
        return SplitQueryType.ALTER_CATALOG;
    }

    @Override
    public SplitQueryType visitRename_table_stmt(Rename_table_stmtContext ctx) {
        return SplitQueryType.RENAME_TABLE;
    }

    @Override
    public SplitQueryType visitRename_database_stmt(Rename_database_stmtContext ctx) {
        return SplitQueryType.RENAME_CATALOG;
    }

    @Override
    public SplitQueryType visitRename_column_stmt(Rename_column_stmtContext ctx) {
        return SplitQueryType.RENAME_COLUMN;
    }

    @Override
    public SplitQueryType visitRename_schema_stmt(Rename_schema_stmtContext ctx) {
        return SplitQueryType.RENAME_SCHEMA;
    }

    @Override
    public SplitQueryType visitComment_table_stmt(Comment_table_stmtContext ctx) {
        return SplitQueryType.COMMENT_TABLE;
    }

    @Override
    public SplitQueryType visitComment_column_stmt(Comment_column_stmtContext ctx) {
        return SplitQueryType.COMMENT_COLUMN;
    }

    @Override
    public SplitQueryType visitCreatedbstmt(CreatedbstmtContext ctx) {
        return SplitQueryType.CREATE_CATALOG;
    }

    @Override
    public SplitQueryType visitDropdbstmt(DropdbstmtContext ctx) {
        return SplitQueryType.DROP_CATALOG;
    }

    @Override
    public SplitQueryType visitCreateschemastmt(CreateschemastmtContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitVariableshowstmt(VariableshowstmtContext ctx) {
        return SplitQueryType.SESSION_VARIABLE_RW;
    }

    @Override
    public SplitQueryType visitDropschemastmt(DropschemastmtContext ctx) {
        return SplitQueryType.DROP_SCHEMA;
    }

    @Override
    public SplitQueryType visitAlterownerstmt(AlterownerstmtContext ctx) {
        if (ctx.aggregate_with_argtypes() != null || ctx.function_with_argtypes() != null || ctx.operator_with_argtypes() != null) {
            return SplitQueryType.ALTER_PROG_OBJ;
        }
        ParseTree alterContext = ctx.getChild(1);
        if (alterContext instanceof TerminalNodeImpl childNode) {
            int type = childNode.getSymbol().getType();
            if (type == DATABASE) {
                return SplitQueryType.ALTER_CATALOG;

            } else if (type == SCHEMA) {
                return SplitQueryType.ALTER_SCHEMA;

            } else if (type == FUNCTION) {
                return SplitQueryType.ALTER_PROG_OBJ;

            } else if (type == PUBLICATION) {
                return SplitQueryType.ALTER_PUB_SUB;

            } else if (type == SUBSCRIPTION) {
                return SplitQueryType.ALTER_PUB_SUB;
            }
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitAlterobjectschemastmt(AlterobjectschemastmtContext ctx) {
        if (ctx.aggregate_with_argtypes() != null || ctx.function_with_argtypes() != null || ctx.operator_with_argtypes() != null) {
            return SplitQueryType.ALTER_PROG_OBJ;
        }
        ParseTree alterContext = ctx.getChild(1);
        if (alterContext instanceof TerminalNodeImpl childNode) {
            if (childNode.getSymbol().getType() == FUNCTION) {
                return SplitQueryType.ALTER_PROG_OBJ;

            }
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitCreatestmt(CreatestmtContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitCreateasstmt(CreateasstmtContext ctx) {
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitAltertablestmt(AltertablestmtContext ctx) {
        ParseTree alterContext = ctx.getChild(1);
        if (alterContext instanceof TerminalNodeImpl childNode) {
            if (childNode.getSymbol().getType() == TABLE) {
                return SplitQueryType.ALTER_TABLE;

            } else if (childNode.getSymbol().getType() == INDEX) {
                return SplitQueryType.ALTER_INDEX;

            } else if (childNode.getSymbol().getType() == VIEW) {
                return SplitQueryType.ALTER_VIEW;

            }
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitDroptablestmt(DroptablestmtContext ctx) {
        return SplitQueryType.DROP_TABLE;
    }

    @Override
    public SplitQueryType visitDropstmt(DropstmtContext ctx) {
        if (hasToken(ctx, INDEX)) {
            return SplitQueryType.DROP_INDEX;
        } else if (hasToken(ctx, VIEW)) {
            return SplitQueryType.DROP_VIEW;
        } else if (hasToken(ctx, POLICY)) {
            return SplitQueryType.DROP_POLICY;
        } else if (hasToken(ctx, PUBLICATION)) {
            return SplitQueryType.DROP_PUB_SUB;
        }
        return SplitQueryType.UNKNOWN;
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
    public SplitQueryType visitCreatepublicationstmt(CreatepublicationstmtContext ctx) {
        return SplitQueryType.CREATE_PUB_SUB;
    }

    @Override
    public SplitQueryType visitAlterpublicationstmt(AlterpublicationstmtContext ctx) {
        return SplitQueryType.ALTER_PUB_SUB;
    }

    @Override
    public SplitQueryType visitCreatesubscriptionstmt(CreatesubscriptionstmtContext ctx) {
        return SplitQueryType.CREATE_PUB_SUB;
    }

    @Override
    public SplitQueryType visitAltersubscriptionstmt(AltersubscriptionstmtContext ctx) {
        if (ctx.ENABLE_P() != null || ctx.DISABLE_P() != null || ctx.REFRESH() != null) {
            return SplitQueryType.ADMIN_PUB_SUB;
        }
        return SplitQueryType.ALTER_PUB_SUB;
    }

    @Override
    public SplitQueryType visitDropsubscriptionstmt(DropsubscriptionstmtContext ctx) {
        return SplitQueryType.DROP_PUB_SUB;
    }

    @Override
    public SplitQueryType visitCreatetrigstmt(CreatetrigstmtContext ctx) {
        return SplitQueryType.CREATE_TRIGGER;
    }

    @Override
    public SplitQueryType visitIndexstmt(IndexstmtContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitViewstmt(ViewstmtContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitCreatefunctionstmt(CreatefunctionstmtContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlterfunctionstmt(AlterfunctionstmtContext ctx) {
        return SplitQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlteroperatorstmt(AlteroperatorstmtContext ctx) {
        return SplitQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitDefinestmt(DefinestmtContext ctx) {
        if (ctx.AGGREGATE() != null || ctx.OPERATOR() != null) {
            return SplitQueryType.CREATE_PROG_OBJ;
        } else if (ctx.TYPE_P() != null) {
            return SplitQueryType.CREATE_TYPE;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitRemovefuncstmt(RemovefuncstmtContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitRemoveaggrstmt(RemoveaggrstmtContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitRemoveoperstmt(RemoveoperstmtContext ctx) {
        return SplitQueryType.DROP_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitAlterobjectdependsstmt(AlterobjectdependsstmtContext ctx) {
        if (ctx.function_with_argtypes() != null) {
            return SplitQueryType.ALTER_PROG_OBJ;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitCreatematviewstmt(CreatematviewstmtContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitSelectstmt(SelectstmtContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitInsertstmt(InsertstmtContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitUpdatestmt(UpdatestmtContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitDeletestmt(DeletestmtContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitCreateuserstmt(CreateuserstmtContext ctx) {
        return SplitQueryType.CREATE_USER;
    }

    @Override
    public SplitQueryType visitDropuserstmt(DropuserstmtContext ctx) {
        return SplitQueryType.DROP_USER;
    }

    @Override
    public SplitQueryType visitCreaterolestmt(CreaterolestmtContext ctx) {
        return SplitQueryType.CREATE_ROLE;
    }

    @Override
    public SplitQueryType visitDroprolestmt(DroprolestmtContext ctx) {
        return SplitQueryType.DROP_ROLE;
    }

    @Override
    public SplitQueryType visitGrantstmt(GrantstmtContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokestmt(RevokestmtContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitCallstmt(CallstmtContext ctx) {
        return SplitQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitChildren(RuleNode node) {
        int n = node.getChildCount();
        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            SplitQueryType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }
        return SplitQueryType.UNKNOWN;
    }

}
