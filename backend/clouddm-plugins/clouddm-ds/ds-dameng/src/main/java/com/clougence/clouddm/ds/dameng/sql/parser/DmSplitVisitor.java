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

import java.util.Locale;

import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;
import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParserBaseVisitor;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

public class DmSplitVisitor extends DmSqlParserBaseVisitor<SplitQueryType> {
    public static final DmSplitVisitor INSTANCE = new DmSplitVisitor();

    @Override
    protected SplitQueryType defaultResult() {
        return SplitQueryType.UNKNOWN;
    }

    @Override
    protected SplitQueryType aggregateResult(SplitQueryType aggregate, SplitQueryType nextResult) {
        return nextResult == SplitQueryType.UNKNOWN ? aggregate : nextResult;
    }

    @Override
    public SplitQueryType visitSelectStatement(DmSqlParser.SelectStatementContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitInsertStatement(DmSqlParser.InsertStatementContext ctx) {
        return SplitQueryType.INSERT;
    }

    @Override
    public SplitQueryType visitUpdateStatement(DmSqlParser.UpdateStatementContext ctx) {
        return SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitDeleteStatement(DmSqlParser.DeleteStatementContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitMergeStatement(DmSqlParser.MergeStatementContext ctx) {
        return SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitFlashbackStatement(DmSqlParser.FlashbackStatementContext ctx) {
        return SplitQueryType.ADMIN_TABLE;
    }

    @Override
    public SplitQueryType visitRefreshMaterializedViewStatement(DmSqlParser.RefreshMaterializedViewStatementContext ctx) {
        return SplitQueryType.ADMIN;
    }

    @Override
    public SplitQueryType visitTableCreate(DmSqlParser.TableCreateContext ctx) {
        if (ctx.tableCreateBody() != null && ctx.tableCreateBody().selectStatement() != null) {
            return SplitQueryType.CREATE_TABLE;
        }
        return SplitQueryType.CREATE_TABLE;
    }

    @Override
    public SplitQueryType visitViewCreate(DmSqlParser.ViewCreateContext ctx) {
        return SplitQueryType.CREATE_VIEW;
    }

    @Override
    public SplitQueryType visitMaterializedViewLogCreate(DmSqlParser.MaterializedViewLogCreateContext ctx) {
        return SplitQueryType.CREATE_LOG;
    }

    @Override
    public SplitQueryType visitIndexCreate(DmSqlParser.IndexCreateContext ctx) {
        return SplitQueryType.ADD_INDEX;
    }

    @Override
    public SplitQueryType visitSchemaCreate(DmSqlParser.SchemaCreateContext ctx) {
        return SplitQueryType.CREATE_SCHEMA;
    }

    @Override
    public SplitQueryType visitSequenceCreate(DmSqlParser.SequenceCreateContext ctx) {
        return SplitQueryType.CREATE_SEQUENCE;
    }

    @Override
    public SplitQueryType visitUserCreate(DmSqlParser.UserCreateContext ctx) {
        return SplitQueryType.CREATE_USER;
    }

    @Override
    public SplitQueryType visitRoleCreate(DmSqlParser.RoleCreateContext ctx) {
        return SplitQueryType.CREATE_ROLE;
    }

    @Override
    public SplitQueryType visitProcedureCreate(DmSqlParser.ProcedureCreateContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitFunctionCreate(DmSqlParser.FunctionCreateContext ctx) {
        return SplitQueryType.CREATE_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitTriggerCreate(DmSqlParser.TriggerCreateContext ctx) {
        return SplitQueryType.CREATE_TRIGGER;
    }

    @Override
    public SplitQueryType visitSynonymCreate(DmSqlParser.SynonymCreateContext ctx) {
        return SplitQueryType.CREATE_SYNONYM;
    }

    @Override
    public SplitQueryType visitObjectCreate(DmSqlParser.ObjectCreateContext ctx) {
        if (ctx.replaceableObjectCreate() != null) {
            return visitReplaceableObjectCreate(ctx.replaceableObjectCreate());
        }
        if (ctx.TABLESPACE() != null) {
            return SplitQueryType.CREATE_TABLESPACE;
        }
        if (ctx.DOMAIN() != null || ctx.typeBodyCreate() != null || ctx.typeCreate() != null) {
            return SplitQueryType.CREATE_TYPE;
        }
        if (ctx.operatorCreate() != null) {
            return SplitQueryType.CREATE_PROG_OBJ;
        }
        if (ctx.PROFILE() != null) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        if (ctx.partitionGroupCreate() != null) {
            return SplitQueryType.CREATE_POLICY;
        }
        if (ctx.classBodyCreate() != null || ctx.javaClassCreate() != null || ctx.classCreate() != null) {
            return SplitQueryType.CREATE_TYPE;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitReplaceableObjectCreate(DmSqlParser.ReplaceableObjectCreateContext ctx) {
        if (ctx.PACKAGE() != null) {
            return SplitQueryType.CREATE_PROG_OBJ;
        }
        if (ctx.LIBRARY() != null) {
            return SplitQueryType.CREATE_LIBRARY;
        }
        if (ctx.typeBodyCreate() != null || ctx.typeCreate() != null) {
            return SplitQueryType.CREATE_TYPE;
        }
        if (ctx.classBodyCreate() != null || ctx.javaClassCreate() != null || ctx.classCreate() != null) {
            return SplitQueryType.CREATE_TYPE;
        }
        if (ctx.LINK() != null || ctx.DIRECTORY() != null || ctx.CONTEXT() != null) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitAdminStatement(DmSqlParser.AdminStatementContext ctx) {
        if (ctx.backupStatementTail() != null || ctx.dumpStatementTail() != null) {
            return SplitQueryType.DATA_EXPORT;
        }
        if (ctx.restoreStatementTail() != null || ctx.recoverStatementTail() != null || ctx.loadBackupsetsTail() != null || ctx.mergeDatabaseTail() != null) {
            return SplitQueryType.DATA_IMPORT;
        }
        if (ctx.showBackupsetTail() != null || ctx.CONFIGURE() != null && ctx.configureStatementTail() == null) {
            return SplitQueryType.METADATA;
        }
        if (ctx.CONFIGURE() != null) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        if (ctx.repairStatementTail() != null || ctx.CHECKPOINT() != null) {
            return SplitQueryType.MAINTAIN_LOG;
        }
        if (ctx.dataWatcherAdminProcedure() != null) {
            return ctx.dataWatcherAdminProcedure().SP_SET_OGUID() != null ? SplitQueryType.ALTER_REPLICATION : SplitQueryType.ADMIN_REPLICATION;
        }
        if (ctx.alterSystemAction() != null) {
            return SplitQueryType.MAINTAIN_LOG;
        }
        if (ctx.alterDatabaseAction() != null) {
            DmSqlParser.AlterDatabaseActionContext action = ctx.alterDatabaseAction();
            if (action.SUSPEND() != null) {
                return SplitQueryType.ADMIN_REPLICATION;
            }
            if ((action.ADD() != null || action.MODIFY() != null || action.DELETE() != null) && action.ARCHIVELOG() != null) {
                return isLocalArchiveDestination(action) ? SplitQueryType.ALTER_LOG : SplitQueryType.ALTER_REPLICATION;
            }
            if (action.LOGFILE() != null || action.NOARCHIVELOG() != null || action.ARCHIVELOG() != null && action.CURRENT() == null) {
                return SplitQueryType.ALTER_LOG;
            }
            if (action.ARCHIVELOG() != null && action.CURRENT() != null) {
                return SplitQueryType.MAINTAIN_LOG;
            }
            if (action.NORMAL() != null || action.PRIMARY() != null || action.STANDBY() != null) {
                return SplitQueryType.ALTER_REPLICATION;
            }
        }
        return SplitQueryType.ADMIN;
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
    public SplitQueryType visitStatStatement(DmSqlParser.StatStatementContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitStatProcedureStatement(DmSqlParser.StatProcedureStatementContext ctx) {
        return SplitQueryType.ADMIN_PERFORMANCE;
    }

    @Override
    public SplitQueryType visitAlterTarget(DmSqlParser.AlterTargetContext ctx) {
        if (ctx.TABLE() != null) {
            return SplitQueryType.ALTER_TABLE;
        }
        if (ctx.INDEX() != null) {
            if (ctx.alterIndexAction() != null && ctx.alterIndexAction().RENAME() != null) {
                return SplitQueryType.RENAME_INDEX;
            }
            return SplitQueryType.ALTER_INDEX;
        }
        if (ctx.VIEW() != null) {
            return SplitQueryType.ALTER_VIEW;
        }
        if (ctx.SEQUENCE() != null) {
            if (ctx.alterSequenceAction() != null && ctx.alterSequenceAction().RENAME() != null) {
                return SplitQueryType.RENAME_SEQUENCE;
            }
            return SplitQueryType.ALTER_SEQUENCE;
        }
        if (ctx.USER() != null) {
            return SplitQueryType.ALTER_USER;
        }
        if (ctx.PROCEDURE() != null) {
            return SplitQueryType.ALTER_PROG_OBJ;
        }
        if (ctx.FUNCTION() != null) {
            return SplitQueryType.ALTER_PROG_OBJ;
        }
        if (ctx.TRIGGER() != null) {
            return SplitQueryType.ALTER_TRIGGER;
        }
        if (ctx.PACKAGE() != null) {
            return SplitQueryType.ALTER_PROG_OBJ;
        }
        if (ctx.TABLESPACE() != null) {
            if (ctx.tablespaceAlterAction() != null && ctx.tablespaceAlterAction().RENAME() != null && ctx.tablespaceAlterAction().DATAFILE() == null) {
                return SplitQueryType.RENAME_TABLESPACE;
            }
            return SplitQueryType.ALTER_TABLESPACE;
        }
        if (ctx.PROFILE() != null) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        if (ctx.TYPE() != null) {
            return SplitQueryType.ALTER_TYPE;
        }
        if (ctx.CLASS() != null) {
            return SplitQueryType.ALTER_TYPE;
        }
        if (ctx.OPERATOR() != null) {
            return SplitQueryType.ALTER_PROG_OBJ;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitDropTarget(DmSqlParser.DropTargetContext ctx) {
        if (ctx.TABLE() != null) {
            return SplitQueryType.DROP_TABLE;
        }
        if (ctx.MATERIALIZED() != null && ctx.LOG() != null) {
            return SplitQueryType.DROP_LOG;
        }
        if (ctx.MATERIALIZED() != null) {
            return SplitQueryType.DROP_VIEW;
        }
        if (ctx.VIEW() != null) {
            return SplitQueryType.DROP_VIEW;
        }
        if (ctx.INDEX() != null) {
            return SplitQueryType.DROP_INDEX;
        }
        if (ctx.SCHEMA() != null) {
            return SplitQueryType.DROP_SCHEMA;
        }
        if (ctx.SEQUENCE() != null) {
            return SplitQueryType.DROP_SEQUENCE;
        }
        if (ctx.USER() != null) {
            return SplitQueryType.DROP_USER;
        }
        if (ctx.ROLE() != null) {
            return SplitQueryType.DROP_ROLE;
        }
        if (ctx.PROCEDURE() != null) {
            return SplitQueryType.DROP_PROG_OBJ;
        }
        if (ctx.FUNCTION() != null) {
            return SplitQueryType.DROP_PROG_OBJ;
        }
        if (ctx.TRIGGER() != null) {
            return SplitQueryType.DROP_TRIGGER;
        }
        if (ctx.SYNONYM() != null) {
            return SplitQueryType.DROP_SYNONYM;
        }
        if (ctx.PACKAGE() != null) {
            return SplitQueryType.DROP_PROG_OBJ;
        }
        if (ctx.TABLESPACE() != null) {
            return SplitQueryType.DROP_TABLESPACE;
        }
        if (ctx.LIBRARY() != null) {
            return SplitQueryType.DROP_LIBRARY;
        }
        if (ctx.DOMAIN() != null || ctx.TYPE() != null) {
            return SplitQueryType.DROP_TYPE;
        }
        if (ctx.OPERATOR() != null) {
            return SplitQueryType.DROP_PROG_OBJ;
        }
        if (ctx.CLASS() != null) {
            return SplitQueryType.DROP_TYPE;
        }
        if (ctx.LINK() != null || ctx.DIRECTORY() != null || ctx.CONTEXT() != null || ctx.PROFILE() != null) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        if (ctx.PARTITION() != null && ctx.GROUP() != null) {
            return SplitQueryType.DROP_POLICY;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitTruncateStatement(DmSqlParser.TruncateStatementContext ctx) {
        return SplitQueryType.TRUNCATE_TABLE;
    }

    @Override
    public SplitQueryType visitCommentStatement(DmSqlParser.CommentStatementContext ctx) {
        DmSqlParser.CommentTargetContext target = ctx.commentTarget();
        if (target.MATERIALIZED() != null) {
            return SplitQueryType.COMMENT_MATERIALIZED_VIEW;
        }
        if (target.TABLE() != null) {
            return SplitQueryType.COMMENT_TABLE;
        }
        if (target.VIEW() != null) {
            return SplitQueryType.COMMENT_VIEW;
        }
        if (target.COLUMN() != null) {
            return SplitQueryType.COMMENT_COLUMN;
        }
        if (target.SCHEMA() != null) {
            return SplitQueryType.COMMENT_SCHEMA;
        }
        if (target.TABLESPACE() != null) {
            return SplitQueryType.COMMENT_TABLESPACE;
        }
        if (target.ROLE() != null) {
            return SplitQueryType.COMMENT_ROLE;
        }
        if (target.SEQUENCE() != null) {
            return SplitQueryType.COMMENT_SEQUENCE;
        }
        if (target.INDEX() != null) {
            return SplitQueryType.COMMENT_INDEX;
        }
        if (target.TRIGGER() != null) {
            return SplitQueryType.COMMENT_TRIGGER;
        }
        if (target.TYPE() != null) {
            return SplitQueryType.COMMENT_TYPE;
        }
        if (target.CONTEXT() != null) {
            return SplitQueryType.COMMENT_CONTEXT;
        }
        if (target.DOMAIN() != null) {
            return SplitQueryType.COMMENT_DOMAIN;
        }
        if (target.DIRECTORY() != null) {
            return SplitQueryType.COMMENT_DIRECTORY;
        }
        if (target.PROFILE() != null) {
            return SplitQueryType.COMMENT_PROFILE;
        }
        if (target.LINK() != null) {
            return SplitQueryType.COMMENT_LINK;
        }
        if (target.CLASS() != null) {
            return SplitQueryType.COMMENT_CLASS;
        }
        if (target.FUNCTION() != null) {
            return SplitQueryType.COMMENT_FUNCTION;
        }
        if (target.PACKAGE() != null) {
            return SplitQueryType.COMMENT_PACKAGE;
        }
        if (target.PROCEDURE() != null) {
            return SplitQueryType.COMMENT_PROCEDURE;
        }
        if (target.DATABASE() != null) {
            return SplitQueryType.COMMENT_SCHEMA;
        }
        return SplitQueryType.UNKNOWN;
    }

    @Override
    public SplitQueryType visitGrantStatement(DmSqlParser.GrantStatementContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitRevokeStatement(DmSqlParser.RevokeStatementContext ctx) {
        return SplitQueryType.REVOKE;
    }

    @Override
    public SplitQueryType visitCallStatement(DmSqlParser.CallStatementContext ctx) {
        return builtInProcedureType(ctx.qualifiedName().getText());
    }

    @Override
    public SplitQueryType visitLockTableStatement(DmSqlParser.LockTableStatementContext ctx) {
        return SplitQueryType.SESSION_LOCK;
    }

    @Override
    public SplitQueryType visitAlterSessionParallelDmlStatement(DmSqlParser.AlterSessionParallelDmlStatementContext ctx) {
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSetSchemaStatement(DmSqlParser.SetSchemaStatementContext ctx) {
        return SplitQueryType.SWITCH_SCHEMA;
    }

    @Override
    public SplitQueryType visitSetTimeZoneStatement(DmSqlParser.SetTimeZoneStatementContext ctx) {
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSetIdentityInsertStatement(DmSqlParser.SetIdentityInsertStatementContext ctx) {
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitConfigWriteStatement(DmSqlParser.ConfigWriteStatementContext ctx) {
        if (ctx.sessionConfigAssignment() != null) {
            return SplitQueryType.SESSION_SETTING_WRITE;
        }
        DmSqlParser.ConfigWriteProcedureContext procedure = ctx.configWriteProcedure();
        if (procedure != null && (procedure.SF_SET_SESSION_PARA_VALUE() != null || procedure.SP_RESET_SESSION_PARA_VALUE() != null || procedure.SP_SET_PARAM_IN_SESSION() != null
                                  || procedure.SP_SET_SESSION_READONLY() != null)) {
            return SplitQueryType.SESSION_SETTING_WRITE;
        }
        return SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitAuditAdminStatement(DmSqlParser.AuditAdminStatementContext ctx) {
        DmSqlParser.AuditAdminProcedureContext procedure = ctx.auditAdminProcedure();
        return procedure.SP_DROP_AUDIT_FILE() != null || procedure.SP_SWITCH_AUDIT_FILE() != null ? SplitQueryType.MAINTAIN_LOG : SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitSecurityAdminStatement(DmSqlParser.SecurityAdminStatementContext ctx) {
        return ctx.securityAdminProcedure().SP_SET_ROLE() != null ? SplitQueryType.SWITCH_ROLE : SplitQueryType.SYSTEM_SETTING_WRITE;
    }

    @Override
    public SplitQueryType visitProcedureCallStatement(DmSqlParser.ProcedureCallStatementContext ctx) {
        return builtInProcedureType(ctx.qualifiedName() != null ? ctx.qualifiedName().getText() : ctx.bareRoutineName().getText());
    }

    private SplitQueryType builtInProcedureType(String rawName) {
        String name = rawName.toUpperCase(Locale.ROOT);
        int separator = name.lastIndexOf('.');
        if (separator >= 0) {
            name = name.substring(separator + 1);
        }
        return switch (name) {
            case "SP_TS_GROUP_CREATE" -> SplitQueryType.CREATE_POLICY;
            case "SP_TS_GROUP_ADD_TS", "SP_TS_GROUP_REMOVE_TS" -> SplitQueryType.ALTER_POLICY;
            case "SP_TS_GROUP_DROP" -> SplitQueryType.DROP_POLICY;
            case "SP_GET_ALL_TS_BY_TSGROUP", "SP_GET_RAFT_ASYNC_INTERVAL" -> SplitQueryType.METADATA;
            case "SP_GET_EP_COUNT" -> SplitQueryType.SELECT;
            case "SP_SET_SESSION_LOCAL_TYPE", "SP_SET_SESSION_MPP_SELECT_LOCAL" -> SplitQueryType.SESSION_SETTING_WRITE;
            case "SP_CLEAR_TAB_ROWCNT_CACHE", "SP_DPC_REBANLANCE_SESSION", "SP_INIT_AWR_SYS", "SP_SET_DBG_SHOW" -> SplitQueryType.ADMIN_PERFORMANCE;
            case "SP_DISABLE_DPC_RAFT", "SP_ENABLE_DPC_RAFT", "SP_RAFT_RESUME_THREAD", "SP_RAFT_SUSPEND_THREAD", "SP_RAFT_SWITCHOVER" -> SplitQueryType.ADMIN_REPLICATION;
            case "SP_DPC_DUMP_INST" -> SplitQueryType.DATA_EXPORT;
            case "SP_TS_DROP_INVALID" -> SplitQueryType.DROP_TABLESPACE;
            case "SP_FILE_SYS_CHECK", "SP_TABLESPACE_PREPARE_RECOVER", "SP_TABLESPACE_RECOVER" -> SplitQueryType.ADMIN;
            case "SP_ADD_RAFT_LEARNER", "SP_ADD_RAFT_NODE", "SP_ALTER_DPC_INSTANCE", "SP_ALTER_RAFT_NODE", "SP_BP_GROUP_ADD_RAFT", "SP_BP_GROUP_DEL_RAFT", "SP_CREATE_DPC_BP_GROUP",
                    "SP_CREATE_DPC_INSTANCE", "SP_CREATE_DPC_RAFT", "SP_CREATE_DPC_SP_GROUP", "SP_CREATE_SYSTEM_PACKAGES", "SP_DELETE_RAFT_LEARNER", "SP_DELETE_RAFT_NODE",
                    "SP_DPC_MOVE_TS_OFFLINE", "SP_DROP_DPC_BP_GROUP", "SP_DROP_DPC_BP_RAFT", "SP_DROP_DPC_INSTANCE", "SP_DROP_DPC_RAFT", "SP_DROP_DPC_SP_GROUP",
                    "SP_INIT_DBMS_SCHEDULER_SYS", "SP_MODIFY_DPC_INSTANCE", "SP_RENAME_DPC_INSTANCE", "SP_REPLACE_RAFT_NODE", "SP_RESET_SP_UPGRADE", "SP_SET_DPC_INST_AUX",
                    "SP_SET_DPC_NET_CONF", "SP_SET_SP_UPGRADE", "SP_SP_GROUP_ADD_RAFT", "SP_SP_GROUP_DEL_RAFT" ->
                SplitQueryType.SYSTEM_SETTING_WRITE;
            default -> SplitQueryType.CALL_PROG_OBJ;
        };
    }

    @Override
    public SplitQueryType visitTransactionStatement(DmSqlParser.TransactionStatementContext ctx) {
        return SplitQueryType.TRANSACTION;
    }

    @Override
    public SplitQueryType visitExplainStatement(DmSqlParser.ExplainStatementContext ctx) {
        return SplitQueryType.PERFORMANCE;
    }

    @Override
    public SplitQueryType visitSqlBlockStatement(DmSqlParser.SqlBlockStatementContext ctx) {
        return SplitQueryType.BLOCK;
    }

    @Override
    public SplitQueryType visitCStyleBlockStatement(DmSqlParser.CStyleBlockStatementContext ctx) {
        return SplitQueryType.BLOCK;
    }
}
