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
package com.clougence.clouddm.ds.dameng.sql.parser;

import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;
import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParserBaseVisitor;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class DmSplitVisitor extends DmSqlParserBaseVisitor<SecQueryType> {
    public static final DmSplitVisitor INSTANCE = new DmSplitVisitor();

    @Override
    protected SecQueryType defaultResult() {
        return SecQueryType.UNKNOWN;
    }

    @Override
    protected SecQueryType aggregateResult(SecQueryType aggregate, SecQueryType nextResult) {
        return nextResult == SecQueryType.UNKNOWN ? aggregate : nextResult;
    }

    @Override
    public SecQueryType visitSelectStatement(DmSqlParser.SelectStatementContext ctx) {
        return SecQueryType.SELECT;
    }

    @Override
    public SecQueryType visitInsertStatement(DmSqlParser.InsertStatementContext ctx) {
        return SecQueryType.INSERT;
    }

    @Override
    public SecQueryType visitUpdateStatement(DmSqlParser.UpdateStatementContext ctx) {
        return SecQueryType.UPDATE;
    }

    @Override
    public SecQueryType visitDeleteStatement(DmSqlParser.DeleteStatementContext ctx) {
        return SecQueryType.DELETE;
    }

    @Override
    public SecQueryType visitMergeStatement(DmSqlParser.MergeStatementContext ctx) {
        return SecQueryType.MERGE;
    }

    @Override
    public SecQueryType visitFlashbackStatement(DmSqlParser.FlashbackStatementContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitRefreshMaterializedViewStatement(DmSqlParser.RefreshMaterializedViewStatementContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitTableCreate(DmSqlParser.TableCreateContext ctx) {
        if (ctx.tableCreateBody() != null && ctx.tableCreateBody().selectStatement() != null) {
            return SecQueryType.CREATE_TABLE;
        }
        return SecQueryType.CREATE_TABLE;
    }

    @Override
    public SecQueryType visitViewCreate(DmSqlParser.ViewCreateContext ctx) {
        return SecQueryType.CREATE_VIEW;
    }

    @Override
    public SecQueryType visitMaterializedViewLogCreate(DmSqlParser.MaterializedViewLogCreateContext ctx) {
        return SecQueryType.CREATE_LOG;
    }

    @Override
    public SecQueryType visitIndexCreate(DmSqlParser.IndexCreateContext ctx) {
        return SecQueryType.ADD_INDEX;
    }

    @Override
    public SecQueryType visitSchemaCreate(DmSqlParser.SchemaCreateContext ctx) {
        return SecQueryType.CREATE_SCHEMA;
    }

    @Override
    public SecQueryType visitSequenceCreate(DmSqlParser.SequenceCreateContext ctx) {
        return SecQueryType.CREATE_SEQUENCE;
    }

    @Override
    public SecQueryType visitUserCreate(DmSqlParser.UserCreateContext ctx) {
        return SecQueryType.CREATE_USER;
    }

    @Override
    public SecQueryType visitRoleCreate(DmSqlParser.RoleCreateContext ctx) {
        return SecQueryType.CREATE_ROLE;
    }

    @Override
    public SecQueryType visitProcedureCreate(DmSqlParser.ProcedureCreateContext ctx) {
        return SecQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SecQueryType visitFunctionCreate(DmSqlParser.FunctionCreateContext ctx) {
        return SecQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SecQueryType visitTriggerCreate(DmSqlParser.TriggerCreateContext ctx) {
        return SecQueryType.CREATE_TRIGGER;
    }

    @Override
    public SecQueryType visitSynonymCreate(DmSqlParser.SynonymCreateContext ctx) {
        return SecQueryType.CREATE_SYNONYM;
    }

    @Override
    public SecQueryType visitObjectCreate(DmSqlParser.ObjectCreateContext ctx) {
        if (ctx.replaceableObjectCreate() != null) {
            return visitReplaceableObjectCreate(ctx.replaceableObjectCreate());
        }
        if (ctx.TABLESPACE() != null) {
            return SecQueryType.CREATE_TABLESPACE;
        }
        if (ctx.DOMAIN() != null || ctx.typeBodyCreate() != null || ctx.typeCreate() != null) {
            return SecQueryType.CREATE_TYPE;
        }
        if (ctx.operatorCreate() != null) {
            return SecQueryType.CREATE_PROG_OBJ;
        }
        if (ctx.PROFILE() != null) {
            return SecQueryType.SYSTEM_SETTING_WRITE;
        }
        if (ctx.classBodyCreate() != null || ctx.javaClassCreate() != null || ctx.classCreate() != null) {
            return SecQueryType.CREATE_TYPE;
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitReplaceableObjectCreate(DmSqlParser.ReplaceableObjectCreateContext ctx) {
        if (ctx.PACKAGE() != null) {
            return SecQueryType.CREATE_PROG_OBJ;
        }
        if (ctx.LIBRARY() != null) {
            return SecQueryType.CREATE_LIBRARY;
        }
        if (ctx.typeBodyCreate() != null || ctx.typeCreate() != null) {
            return SecQueryType.CREATE_TYPE;
        }
        if (ctx.classBodyCreate() != null || ctx.javaClassCreate() != null || ctx.classCreate() != null) {
            return SecQueryType.CREATE_TYPE;
        }
        if (ctx.LINK() != null || ctx.DIRECTORY() != null || ctx.CONTEXT() != null) {
            return SecQueryType.SYSTEM_SETTING_WRITE;
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitAdminStatement(DmSqlParser.AdminStatementContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitStatStatement(DmSqlParser.StatStatementContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitStatProcedureStatement(DmSqlParser.StatProcedureStatementContext ctx) {
        return SecQueryType.ADMIN_TABLE;
    }

    @Override
    public SecQueryType visitAlterTarget(DmSqlParser.AlterTargetContext ctx) {
        if (ctx.TABLE() != null) {
            return SecQueryType.ALTER_TABLE;
        }
        if (ctx.INDEX() != null) {
            return SecQueryType.ALTER_INDEX;
        }
        if (ctx.VIEW() != null) {
            return SecQueryType.ALTER_VIEW;
        }
        if (ctx.SEQUENCE() != null) {
            return SecQueryType.ALTER_SEQUENCE;
        }
        if (ctx.USER() != null) {
            return SecQueryType.ALTER_USER;
        }
        if (ctx.PROCEDURE() != null) {
            return SecQueryType.ALTER_PROG_OBJ;
        }
        if (ctx.FUNCTION() != null) {
            return SecQueryType.ALTER_PROG_OBJ;
        }
        if (ctx.TRIGGER() != null) {
            return SecQueryType.ALTER_TRIGGER;
        }
        if (ctx.PACKAGE() != null) {
            return SecQueryType.ADMIN_PROG_OBJ;
        }
        if (ctx.TABLESPACE() != null) {
            return SecQueryType.ALTER_TABLESPACE;
        }
        if (ctx.PROFILE() != null) {
            return SecQueryType.SYSTEM_SETTING_WRITE;
        }
        if (ctx.TYPE() != null) {
            return SecQueryType.ADMIN_TYPE;
        }
        if (ctx.CLASS() != null) {
            return SecQueryType.ADMIN_TYPE;
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitDropTarget(DmSqlParser.DropTargetContext ctx) {
        if (ctx.TABLE() != null) {
            return SecQueryType.DROP_TABLE;
        }
        if (ctx.MATERIALIZED() != null && ctx.LOG() != null) {
            return SecQueryType.DROP_LOG;
        }
        if (ctx.MATERIALIZED() != null) {
            return SecQueryType.DROP_VIEW;
        }
        if (ctx.VIEW() != null) {
            return SecQueryType.DROP_VIEW;
        }
        if (ctx.INDEX() != null) {
            return SecQueryType.DROP_INDEX;
        }
        if (ctx.SCHEMA() != null || ctx.DATABASE() != null) {
            return SecQueryType.DROP_SCHEMA;
        }
        if (ctx.SEQUENCE() != null) {
            return SecQueryType.DROP_SEQUENCE;
        }
        if (ctx.USER() != null) {
            return SecQueryType.DROP_USER;
        }
        if (ctx.ROLE() != null) {
            return SecQueryType.DROP_ROLE;
        }
        if (ctx.PROCEDURE() != null) {
            return SecQueryType.DROP_PROG_OBJ;
        }
        if (ctx.FUNCTION() != null) {
            return SecQueryType.DROP_PROG_OBJ;
        }
        if (ctx.TRIGGER() != null) {
            return SecQueryType.DROP_TRIGGER;
        }
        if (ctx.SYNONYM() != null) {
            return SecQueryType.DROP_SYNONYM;
        }
        if (ctx.PACKAGE() != null) {
            return SecQueryType.DROP_PROG_OBJ;
        }
        if (ctx.TABLESPACE() != null) {
            return SecQueryType.DROP_TABLESPACE;
        }
        if (ctx.LIBRARY() != null) {
            return SecQueryType.DROP_LIBRARY;
        }
        if (ctx.DOMAIN() != null || ctx.TYPE() != null) {
            return SecQueryType.DROP_TYPE;
        }
        if (ctx.OPERATOR() != null) {
            return SecQueryType.DROP_PROG_OBJ;
        }
        if (ctx.CLASS() != null) {
            return SecQueryType.DROP_TYPE;
        }
        if (ctx.LINK() != null || ctx.DIRECTORY() != null || ctx.CONTEXT() != null || ctx.PROFILE() != null) {
            return SecQueryType.SYSTEM_SETTING_WRITE;
        }
        return SecQueryType.UNKNOWN;
    }

    @Override
    public SecQueryType visitTruncateStatement(DmSqlParser.TruncateStatementContext ctx) {
        return SecQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SecQueryType visitCommentStatement(DmSqlParser.CommentStatementContext ctx) {
        if (ctx.commentTarget().TABLE() != null) {
            return SecQueryType.COMMENT_TABLE;
        }
        if (ctx.commentTarget().VIEW() != null) {
            return SecQueryType.ALTER_VIEW;
        }
        return SecQueryType.COMMENT_COLUMN;
    }

    @Override
    public SecQueryType visitGrantStatement(DmSqlParser.GrantStatementContext ctx) {
        return SecQueryType.GRANT;
    }

    @Override
    public SecQueryType visitRevokeStatement(DmSqlParser.RevokeStatementContext ctx) {
        return SecQueryType.REVOKE;
    }

    @Override
    public SecQueryType visitCallStatement(DmSqlParser.CallStatementContext ctx) {
        return SecQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SecQueryType visitLockTableStatement(DmSqlParser.LockTableStatementContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitAlterSessionParallelDmlStatement(DmSqlParser.AlterSessionParallelDmlStatementContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitSetSchemaStatement(DmSqlParser.SetSchemaStatementContext ctx) {
        return SecQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SecQueryType visitSetTimeZoneStatement(DmSqlParser.SetTimeZoneStatementContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitSetIdentityInsertStatement(DmSqlParser.SetIdentityInsertStatementContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitConfigWriteStatement(DmSqlParser.ConfigWriteStatementContext ctx) {
        return SecQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SecQueryType visitAuditAdminStatement(DmSqlParser.AuditAdminStatementContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitSecurityAdminStatement(DmSqlParser.SecurityAdminStatementContext ctx) {
        return SecQueryType.ADMIN;
    }

    @Override
    public SecQueryType visitProcedureCallStatement(DmSqlParser.ProcedureCallStatementContext ctx) {
        return SecQueryType.CALL_PROG_OBJ;
    }

    @Override
    public SecQueryType visitTransactionStatement(DmSqlParser.TransactionStatementContext ctx) {
        return SecQueryType.TRANSACTION;
    }

    @Override
    public SecQueryType visitExplainStatement(DmSqlParser.ExplainStatementContext ctx) {
        return SecQueryType.PERFORMANCE;
    }

    @Override
    public SecQueryType visitSqlBlockStatement(DmSqlParser.SqlBlockStatementContext ctx) {
        return SecQueryType.BLOCK;
    }

    @Override
    public SecQueryType visitCStyleBlockStatement(DmSqlParser.CStyleBlockStatementContext ctx) {
        return SecQueryType.BLOCK;
    }
}
