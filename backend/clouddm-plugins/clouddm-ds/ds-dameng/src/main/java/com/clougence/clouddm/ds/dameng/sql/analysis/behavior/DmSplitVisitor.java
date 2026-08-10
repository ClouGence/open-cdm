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
package com.clougence.clouddm.ds.dameng.sql.analysis.behavior;

import java.util.Locale;

import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;
import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParserBaseVisitor;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;

public class DmSplitVisitor extends DmSqlParserBaseVisitor<StatementType> {
    public static final DmSplitVisitor INSTANCE = new DmSplitVisitor();

    @Override
    protected StatementType defaultResult() {
        return StatementType.UNKNOWN;
    }

    @Override
    protected StatementType aggregateResult(StatementType aggregate, StatementType nextResult) {
        return nextResult == StatementType.UNKNOWN ? aggregate : nextResult;
    }

    @Override
    public StatementType visitSelectStatement(DmSqlParser.SelectStatementContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitInsertStatement(DmSqlParser.InsertStatementContext ctx) {
        return StatementType.INSERT;
    }

    @Override
    public StatementType visitUpdateStatement(DmSqlParser.UpdateStatementContext ctx) {
        return StatementType.UPDATE;
    }

    @Override
    public StatementType visitDeleteStatement(DmSqlParser.DeleteStatementContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitMergeStatement(DmSqlParser.MergeStatementContext ctx) {
        return StatementType.MERGE;
    }

    @Override
    public StatementType visitFlashbackStatement(DmSqlParser.FlashbackStatementContext ctx) {
        return StatementType.ADMIN_TABLE;
    }

    @Override
    public StatementType visitRefreshMaterializedViewStatement(DmSqlParser.RefreshMaterializedViewStatementContext ctx) {
        return StatementType.ADMIN;
    }

    @Override
    public StatementType visitTableCreate(DmSqlParser.TableCreateContext ctx) {
        if (ctx.tableCreateBody() != null && ctx.tableCreateBody().selectStatement() != null) {
            return StatementType.CREATE_TABLE;
        }
        return StatementType.CREATE_TABLE;
    }

    @Override
    public StatementType visitViewCreate(DmSqlParser.ViewCreateContext ctx) {
        return StatementType.CREATE_VIEW;
    }

    @Override
    public StatementType visitMaterializedViewLogCreate(DmSqlParser.MaterializedViewLogCreateContext ctx) {
        return StatementType.CREATE_LOG;
    }

    @Override
    public StatementType visitIndexCreate(DmSqlParser.IndexCreateContext ctx) {
        return StatementType.ADD_INDEX;
    }

    @Override
    public StatementType visitSchemaCreate(DmSqlParser.SchemaCreateContext ctx) {
        return StatementType.CREATE_SCHEMA;
    }

    @Override
    public StatementType visitSequenceCreate(DmSqlParser.SequenceCreateContext ctx) {
        return StatementType.CREATE_SEQUENCE;
    }

    @Override
    public StatementType visitUserCreate(DmSqlParser.UserCreateContext ctx) {
        return StatementType.CREATE_USER;
    }

    @Override
    public StatementType visitRoleCreate(DmSqlParser.RoleCreateContext ctx) {
        return StatementType.CREATE_ROLE;
    }

    @Override
    public StatementType visitProcedureCreate(DmSqlParser.ProcedureCreateContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitFunctionCreate(DmSqlParser.FunctionCreateContext ctx) {
        return StatementType.CREATE_PROG_OBJ;
    }

    @Override
    public StatementType visitTriggerCreate(DmSqlParser.TriggerCreateContext ctx) {
        return StatementType.CREATE_TRIGGER;
    }

    @Override
    public StatementType visitSynonymCreate(DmSqlParser.SynonymCreateContext ctx) {
        return StatementType.CREATE_SYNONYM;
    }

    @Override
    public StatementType visitObjectCreate(DmSqlParser.ObjectCreateContext ctx) {
        if (ctx.replaceableObjectCreate() != null) {
            return visitReplaceableObjectCreate(ctx.replaceableObjectCreate());
        }
        if (ctx.TABLESPACE() != null) {
            return StatementType.CREATE_TABLESPACE;
        }
        if (ctx.DOMAIN() != null || ctx.typeBodyCreate() != null || ctx.typeCreate() != null) {
            return StatementType.CREATE_TYPE;
        }
        if (ctx.operatorCreate() != null) {
            return StatementType.CREATE_PROG_OBJ;
        }
        if (ctx.PROFILE() != null) {
            return StatementType.SYSTEM_SETTING_WRITE;
        }
        if (ctx.partitionGroupCreate() != null) {
            return StatementType.CREATE_POLICY;
        }
        if (ctx.classBodyCreate() != null || ctx.javaClassCreate() != null || ctx.classCreate() != null) {
            return StatementType.CREATE_TYPE;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitReplaceableObjectCreate(DmSqlParser.ReplaceableObjectCreateContext ctx) {
        if (ctx.PACKAGE() != null) {
            return StatementType.CREATE_PROG_OBJ;
        }
        if (ctx.LIBRARY() != null) {
            return StatementType.CREATE_LIBRARY;
        }
        if (ctx.typeBodyCreate() != null || ctx.typeCreate() != null) {
            return StatementType.CREATE_TYPE;
        }
        if (ctx.classBodyCreate() != null || ctx.javaClassCreate() != null || ctx.classCreate() != null) {
            return StatementType.CREATE_TYPE;
        }
        if (ctx.LINK() != null || ctx.DIRECTORY() != null || ctx.CONTEXT() != null) {
            return StatementType.SYSTEM_SETTING_WRITE;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitAdminStatement(DmSqlParser.AdminStatementContext ctx) {
        if (ctx.backupStatementTail() != null) {
            return StatementType.BACKUP;
        }
        if (ctx.restoreStatementTail() != null) {
            return StatementType.RESTORE;
        }
        if (ctx.recoverStatementTail() != null || ctx.mergeDatabaseTail() != null) {
            return StatementType.RECOVER;
        }
        if (ctx.showBackupsetTail() != null || ctx.checkStatementTail() != null || ctx.dumpStatementTail() != null || ctx.loadBackupsetsTail() != null
            || ctx.removeStatementTail() != null) {
            return StatementType.MAINTAIN_BACKUP;
        }
        if (ctx.CONFIGURE() != null && ctx.configureStatementTail() == null) {
            return StatementType.METADATA;
        }
        if (ctx.CONFIGURE() != null) {
            return StatementType.SYSTEM_SETTING_WRITE;
        }
        if (ctx.repairStatementTail() != null || ctx.CHECKPOINT() != null) {
            return StatementType.MAINTAIN_LOG;
        }
        if (ctx.dataWatcherAdminProcedure() != null) {
            return ctx.dataWatcherAdminProcedure().SP_SET_OGUID() != null ? StatementType.ALTER_REPLICATION : StatementType.ADMIN_REPLICATION;
        }
        if (ctx.alterSystemAction() != null) {
            return StatementType.MAINTAIN_LOG;
        }
        if (ctx.alterDatabaseAction() != null) {
            DmSqlParser.AlterDatabaseActionContext action = ctx.alterDatabaseAction();
            if (action.SUSPEND() != null) {
                return StatementType.ADMIN_REPLICATION;
            }
            if ((action.ADD() != null || action.MODIFY() != null || action.DELETE() != null) && action.ARCHIVELOG() != null) {
                return isLocalArchiveDestination(action) ? StatementType.ALTER_LOG : StatementType.ALTER_REPLICATION;
            }
            if (action.LOGFILE() != null || action.NOARCHIVELOG() != null || action.ARCHIVELOG() != null && action.CURRENT() == null) {
                return StatementType.ALTER_LOG;
            }
            if (action.ARCHIVELOG() != null && action.CURRENT() != null) {
                return StatementType.MAINTAIN_LOG;
            }
            if (action.NORMAL() != null || action.PRIMARY() != null || action.STANDBY() != null) {
                return StatementType.ALTER_REPLICATION;
            }
        }
        return StatementType.ADMIN;
    }

    private boolean isLocalArchiveDestination(DmSqlParser.AlterDatabaseActionContext action) {
        if (action.backupFilePath().isEmpty()) {
            return false;
        }
        String specification = action.backupFilePath(0).getText();
        if (specification.length() >= 2 && specification.charAt(0) == '\'' && specification.charAt(specification.length() - 1) == '\'') {
            specification = specification.substring(1, specification.length() - 1);
        }
        return specification.replace(" ", "").toUpperCase(Locale.ROOT).startsWith("TYPE=LOCAL,") || specification.replace(" ", "").equalsIgnoreCase("TYPE=LOCAL");
    }

    @Override
    public StatementType visitStatStatement(DmSqlParser.StatStatementContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitStatProcedureStatement(DmSqlParser.StatProcedureStatementContext ctx) {
        return StatementType.ADMIN_PERFORMANCE;
    }

    @Override
    public StatementType visitAlterTarget(DmSqlParser.AlterTargetContext ctx) {
        if (ctx.TABLE() != null) {
            return StatementType.ALTER_TABLE;
        }
        if (ctx.INDEX() != null) {
            if (ctx.alterIndexAction() != null && ctx.alterIndexAction().RENAME() != null) {
                return StatementType.RENAME_INDEX;
            }
            return StatementType.ALTER_INDEX;
        }
        if (ctx.VIEW() != null) {
            return StatementType.ALTER_VIEW;
        }
        if (ctx.SEQUENCE() != null) {
            if (ctx.alterSequenceAction() != null && ctx.alterSequenceAction().RENAME() != null) {
                return StatementType.RENAME_SEQUENCE;
            }
            return StatementType.ALTER_SEQUENCE;
        }
        if (ctx.USER() != null) {
            return StatementType.ALTER_USER;
        }
        if (ctx.PROCEDURE() != null) {
            return StatementType.ALTER_PROG_OBJ;
        }
        if (ctx.FUNCTION() != null) {
            return StatementType.ALTER_PROG_OBJ;
        }
        if (ctx.TRIGGER() != null) {
            return StatementType.ALTER_TRIGGER;
        }
        if (ctx.PACKAGE() != null) {
            return StatementType.ALTER_PROG_OBJ;
        }
        if (ctx.TABLESPACE() != null) {
            if (ctx.tablespaceAlterAction() != null && ctx.tablespaceAlterAction().RENAME() != null && ctx.tablespaceAlterAction().DATAFILE() == null) {
                return StatementType.RENAME_TABLESPACE;
            }
            return StatementType.ALTER_TABLESPACE;
        }
        if (ctx.PROFILE() != null) {
            return StatementType.SYSTEM_SETTING_WRITE;
        }
        if (ctx.TYPE() != null) {
            return StatementType.ALTER_TYPE;
        }
        if (ctx.CLASS() != null) {
            return StatementType.ALTER_TYPE;
        }
        if (ctx.OPERATOR() != null) {
            return StatementType.ALTER_PROG_OBJ;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitDropTarget(DmSqlParser.DropTargetContext ctx) {
        if (ctx.TABLE() != null) {
            return StatementType.DROP_TABLE;
        }
        if (ctx.MATERIALIZED() != null && ctx.LOG() != null) {
            return StatementType.DROP_LOG;
        }
        if (ctx.MATERIALIZED() != null) {
            return StatementType.DROP_VIEW;
        }
        if (ctx.VIEW() != null) {
            return StatementType.DROP_VIEW;
        }
        if (ctx.INDEX() != null) {
            return StatementType.DROP_INDEX;
        }
        if (ctx.SCHEMA() != null) {
            return StatementType.DROP_SCHEMA;
        }
        if (ctx.SEQUENCE() != null) {
            return StatementType.DROP_SEQUENCE;
        }
        if (ctx.USER() != null) {
            return StatementType.DROP_USER;
        }
        if (ctx.ROLE() != null) {
            return StatementType.DROP_ROLE;
        }
        if (ctx.PROCEDURE() != null) {
            return StatementType.DROP_PROG_OBJ;
        }
        if (ctx.FUNCTION() != null) {
            return StatementType.DROP_PROG_OBJ;
        }
        if (ctx.TRIGGER() != null) {
            return StatementType.DROP_TRIGGER;
        }
        if (ctx.SYNONYM() != null) {
            return StatementType.DROP_SYNONYM;
        }
        if (ctx.PACKAGE() != null) {
            return StatementType.DROP_PROG_OBJ;
        }
        if (ctx.TABLESPACE() != null) {
            return StatementType.DROP_TABLESPACE;
        }
        if (ctx.LIBRARY() != null) {
            return StatementType.DROP_LIBRARY;
        }
        if (ctx.DOMAIN() != null || ctx.TYPE() != null) {
            return StatementType.DROP_TYPE;
        }
        if (ctx.OPERATOR() != null) {
            return StatementType.DROP_PROG_OBJ;
        }
        if (ctx.CLASS() != null) {
            return StatementType.DROP_TYPE;
        }
        if (ctx.LINK() != null || ctx.DIRECTORY() != null || ctx.CONTEXT() != null || ctx.PROFILE() != null) {
            return StatementType.SYSTEM_SETTING_WRITE;
        }
        if (ctx.PARTITION() != null && ctx.GROUP() != null) {
            return StatementType.DROP_POLICY;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitTruncateStatement(DmSqlParser.TruncateStatementContext ctx) {
        return StatementType.TRUNCATE_TABLE;
    }

    @Override
    public StatementType visitCommentStatement(DmSqlParser.CommentStatementContext ctx) {
        DmSqlParser.CommentTargetContext target = ctx.commentTarget();
        if (target.MATERIALIZED() != null) {
            return StatementType.COMMENT_MATERIALIZED_VIEW;
        }
        if (target.TABLE() != null) {
            return StatementType.COMMENT_TABLE;
        }
        if (target.VIEW() != null) {
            return StatementType.COMMENT_VIEW;
        }
        if (target.COLUMN() != null) {
            return StatementType.COMMENT_COLUMN;
        }
        if (target.SCHEMA() != null) {
            return StatementType.COMMENT_SCHEMA;
        }
        if (target.TABLESPACE() != null) {
            return StatementType.COMMENT_TABLESPACE;
        }
        if (target.ROLE() != null) {
            return StatementType.COMMENT_ROLE;
        }
        if (target.SEQUENCE() != null) {
            return StatementType.COMMENT_SEQUENCE;
        }
        if (target.INDEX() != null) {
            return StatementType.COMMENT_INDEX;
        }
        if (target.TRIGGER() != null) {
            return StatementType.COMMENT_TRIGGER;
        }
        if (target.TYPE() != null) {
            return StatementType.COMMENT_TYPE;
        }
        if (target.CONTEXT() != null) {
            return StatementType.COMMENT_CONTEXT;
        }
        if (target.DOMAIN() != null) {
            return StatementType.COMMENT_DOMAIN;
        }
        if (target.DIRECTORY() != null) {
            return StatementType.COMMENT_DIRECTORY;
        }
        if (target.PROFILE() != null) {
            return StatementType.COMMENT_PROFILE;
        }
        if (target.LINK() != null) {
            return StatementType.COMMENT_LINK;
        }
        if (target.CLASS() != null) {
            return StatementType.COMMENT_CLASS;
        }
        if (target.FUNCTION() != null) {
            return StatementType.COMMENT_FUNCTION;
        }
        if (target.PACKAGE() != null) {
            return StatementType.COMMENT_PACKAGE;
        }
        if (target.PROCEDURE() != null) {
            return StatementType.COMMENT_PROCEDURE;
        }
        if (target.DATABASE() != null) {
            return StatementType.COMMENT_SCHEMA;
        }
        return StatementType.UNKNOWN;
    }

    @Override
    public StatementType visitGrantStatement(DmSqlParser.GrantStatementContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitRevokeStatement(DmSqlParser.RevokeStatementContext ctx) {
        return StatementType.REVOKE;
    }

    @Override
    public StatementType visitCallStatement(DmSqlParser.CallStatementContext ctx) {
        return builtInProcedureType(ctx.qualifiedName().getText());
    }

    @Override
    public StatementType visitLockTableStatement(DmSqlParser.LockTableStatementContext ctx) {
        return StatementType.SESSION_LOCK;
    }

    @Override
    public StatementType visitAlterSessionParallelDmlStatement(DmSqlParser.AlterSessionParallelDmlStatementContext ctx) {
        return StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitSetSchemaStatement(DmSqlParser.SetSchemaStatementContext ctx) {
        return StatementType.SWITCH_SCHEMA;
    }

    @Override
    public StatementType visitSetTimeZoneStatement(DmSqlParser.SetTimeZoneStatementContext ctx) {
        return StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitSetIdentityInsertStatement(DmSqlParser.SetIdentityInsertStatementContext ctx) {
        return StatementType.SESSION_SETTING_WRITE;
    }

    @Override
    public StatementType visitConfigWriteStatement(DmSqlParser.ConfigWriteStatementContext ctx) {
        if (ctx.sessionConfigAssignment() != null) {
            return StatementType.SESSION_SETTING_WRITE;
        }
        DmSqlParser.ConfigWriteProcedureContext procedure = ctx.configWriteProcedure();
        if (procedure != null && (procedure.SF_SET_SESSION_PARA_VALUE() != null || procedure.SP_RESET_SESSION_PARA_VALUE() != null || procedure.SP_SET_PARAM_IN_SESSION() != null
                                  || procedure.SP_SET_SESSION_READONLY() != null)) {
            return StatementType.SESSION_SETTING_WRITE;
        }
        return StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitAuditAdminStatement(DmSqlParser.AuditAdminStatementContext ctx) {
        DmSqlParser.AuditAdminProcedureContext procedure = ctx.auditAdminProcedure();
        return procedure.SP_DROP_AUDIT_FILE() != null || procedure.SP_SWITCH_AUDIT_FILE() != null ? StatementType.MAINTAIN_LOG : StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitSecurityAdminStatement(DmSqlParser.SecurityAdminStatementContext ctx) {
        return ctx.securityAdminProcedure().SP_SET_ROLE() != null ? StatementType.SWITCH_ROLE : StatementType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public StatementType visitProcedureCallStatement(DmSqlParser.ProcedureCallStatementContext ctx) {
        return builtInProcedureType(ctx.qualifiedName() != null ? ctx.qualifiedName().getText() : ctx.bareRoutineName().getText());
    }

    private StatementType builtInProcedureType(String rawName) {
        String name = rawName.toUpperCase(Locale.ROOT);
        int separator = name.lastIndexOf('.');
        if (separator >= 0) {
            name = name.substring(separator + 1);
        }
        return switch (name) {
            case "SP_TS_GROUP_CREATE" -> StatementType.CREATE_POLICY;
            case "SP_TS_GROUP_ADD_TS", "SP_TS_GROUP_REMOVE_TS" -> StatementType.ALTER_POLICY;
            case "SP_TS_GROUP_DROP" -> StatementType.DROP_POLICY;
            case "SP_GET_ALL_TS_BY_TSGROUP", "SP_GET_RAFT_ASYNC_INTERVAL" -> StatementType.METADATA;
            case "SP_GET_EP_COUNT" -> StatementType.SELECT;
            case "SP_SET_SESSION_LOCAL_TYPE", "SP_SET_SESSION_MPP_SELECT_LOCAL" -> StatementType.SESSION_SETTING_WRITE;
            case "SP_CLEAR_TAB_ROWCNT_CACHE", "SP_DPC_REBANLANCE_SESSION", "SP_INIT_AWR_SYS", "SP_SET_DBG_SHOW" -> StatementType.ADMIN_PERFORMANCE;
            case "SP_DISABLE_DPC_RAFT", "SP_ENABLE_DPC_RAFT", "SP_RAFT_RESUME_THREAD", "SP_RAFT_SUSPEND_THREAD", "SP_RAFT_SWITCHOVER" -> StatementType.ADMIN_REPLICATION;
            case "SP_DPC_DUMP_INST" -> StatementType.DATA_EXPORT;
            case "SP_TS_DROP_INVALID" -> StatementType.DROP_TABLESPACE;
            case "SP_FILE_SYS_CHECK", "SP_TABLESPACE_PREPARE_RECOVER", "SP_TABLESPACE_RECOVER" -> StatementType.ADMIN;
            case "SP_ADD_RAFT_LEARNER", "SP_ADD_RAFT_NODE", "SP_ALTER_DPC_INSTANCE", "SP_ALTER_RAFT_NODE", "SP_BP_GROUP_ADD_RAFT", "SP_BP_GROUP_DEL_RAFT", "SP_CREATE_DPC_BP_GROUP",
                    "SP_CREATE_DPC_INSTANCE", "SP_CREATE_DPC_RAFT", "SP_CREATE_DPC_SP_GROUP", "SP_CREATE_SYSTEM_PACKAGES", "SP_DELETE_RAFT_LEARNER", "SP_DELETE_RAFT_NODE",
                    "SP_DPC_MOVE_TS_OFFLINE", "SP_DROP_DPC_BP_GROUP", "SP_DROP_DPC_BP_RAFT", "SP_DROP_DPC_INSTANCE", "SP_DROP_DPC_RAFT", "SP_DROP_DPC_SP_GROUP",
                    "SP_INIT_DBMS_SCHEDULER_SYS", "SP_MODIFY_DPC_INSTANCE", "SP_RENAME_DPC_INSTANCE", "SP_REPLACE_RAFT_NODE", "SP_RESET_SP_UPGRADE", "SP_SET_DPC_INST_AUX",
                    "SP_SET_DPC_NET_CONF", "SP_SET_SP_UPGRADE", "SP_SP_GROUP_ADD_RAFT", "SP_SP_GROUP_DEL_RAFT" ->
                StatementType.SYSTEM_SETTING_WRITE;
            default -> StatementType.CALL_PROG_OBJ;
        };
    }

    @Override
    public StatementType visitTransactionStatement(DmSqlParser.TransactionStatementContext ctx) {
        return StatementType.TRANSACTION;
    }

    @Override
    public StatementType visitExplainStatement(DmSqlParser.ExplainStatementContext ctx) {
        return StatementType.PERFORMANCE;
    }

    @Override
    public StatementType visitSqlBlockStatement(DmSqlParser.SqlBlockStatementContext ctx) {
        return StatementType.BLOCK;
    }

    @Override
    public StatementType visitCStyleBlockStatement(DmSqlParser.CStyleBlockStatementContext ctx) {
        return StatementType.BLOCK;
    }
}
