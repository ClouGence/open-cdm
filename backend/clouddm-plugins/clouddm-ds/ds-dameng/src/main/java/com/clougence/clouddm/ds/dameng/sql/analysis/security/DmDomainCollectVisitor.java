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
package com.clougence.clouddm.ds.dameng.sql.analysis.security;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;
import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParserBaseVisitor;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;

public class DmDomainCollectVisitor extends DmSqlParserBaseVisitor<Void> {
    private final List<RuleDomain>   domains      = new ArrayList<>();
    private final List<String>       schemaScopes = new ArrayList<>();
    private final List<List<String>> cteScopes    = new ArrayList<>();

    public List<RuleDomain> getDomains() { return domains; }

    @Override
    public Void visitSelectStatement(DmSqlParser.SelectStatementContext ctx) {
        boolean pushed = pushCteScope(ctx.withClause());
        try {
            return visitChildren(ctx);
        } finally {
            popCteScope(pushed);
        }
    }

    @Override
    public Void visitSelectQuery(DmSqlParser.SelectQueryContext ctx) {
        if (ctx.fromClause() != null) {
            for (NameParts table : collectTables(ctx.fromClause())) {
                tableDomain(table, SplitQueryType.SELECT);
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitInsertStatement(DmSqlParser.InsertStatementContext ctx) {
        if (ctx.singleInsertStatement() != null) {
            insertDomains(ctx.singleInsertStatement().insertTarget());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitMultiInsertInto(DmSqlParser.MultiInsertIntoContext ctx) {
        insertDomains(ctx.insertTarget());
        return visitChildren(ctx);
    }

    @Override
    public Void visitInsertTableSource(DmSqlParser.InsertTableSourceContext ctx) {
        NameParts name = NameParts.from(ctx.qualifiedName());
        if (!isCteReference(name)) {
            tableDomain(name, SplitQueryType.SELECT);
        }
        return visitChildren(ctx);
    }

    private void insertDomains(DmSqlParser.InsertTargetContext ctx) {
        if (ctx == null) {
            return;
        }
        if (ctx.qualifiedName() != null) {
            insertDomain(NameParts.from(ctx.qualifiedName()));
            return;
        }
        addDmlTargetDomains(ctx.selectStatement(), SplitQueryType.INSERT);
    }

    private void insertDomain(NameParts name) {
        name = schemaScoped(name);
        if (name == null) {
            return;
        }
        RdbInsertDomain domain = new RdbInsertDomain();
        domain.setCatalog(name.catalog());
        domain.setSchema(name.schema());
        domain.setTable(name.name());
        add(domain, SplitQueryType.INSERT);
    }

    private void updateDomains(DmSqlParser.TablePrimaryContext ctx, SplitQueryType type) {
        if (ctx == null) {
            return;
        }
        if (ctx.qualifiedName() != null) {
            updateDomain(NameParts.from(ctx.qualifiedName()), type);
            return;
        }
        if (ctx.selectStatement() != null) {
            addDmlTargetDomains(ctx.selectStatement(), type);
            return;
        }
        List<NameParts> names = new ArrayList<>();
        collectTablePrimary(ctx, names);
        for (NameParts name : names) {
            updateDomain(name, type);
        }
    }

    private void updateDomains(DmSqlParser.TableSourceContext ctx, SplitQueryType type) {
        if (ctx == null) {
            return;
        }
        updateDomains(ctx.tablePrimary(), type);
        for (DmSqlParser.JoinClauseContext joinClauseContext : ctx.joinClause()) {
            if (joinClauseContext.tablePrimary() != null) {
                updateDomains(joinClauseContext.tablePrimary(), type);
            } else if (joinClauseContext.applyJoinClause() != null) {
                updateDomains(joinClauseContext.applyJoinClause().tablePrimary(), type);
            }
        }
    }

    private void updateDomain(NameParts name, SplitQueryType type) {
        name = schemaScoped(name);
        if (name == null) {
            return;
        }
        RdbUpdateDomain domain = new RdbUpdateDomain();
        domain.setCatalog(name.catalog());
        domain.setSchema(name.schema());
        domain.setTable(name.name());
        add(domain, type);
    }

    private void deleteDomain(NameParts name) {
        name = schemaScoped(name);
        if (name == null) {
            return;
        }
        RdbDeleteDomain domain = new RdbDeleteDomain();
        domain.setCatalog(name.catalog());
        domain.setSchema(name.schema());
        domain.setTable(name.name());
        add(domain, SplitQueryType.DELETE);
    }

    @Override
    public Void visitUpdateStatement(DmSqlParser.UpdateStatementContext ctx) {
        for (DmSqlParser.TableSourceContext tableSourceContext : ctx.updateTargetList().tableSource()) {
            updateDomains(tableSourceContext, SplitQueryType.UPDATE);
        }
        if (ctx.fromClause() != null) {
            for (NameParts table : collectTables(ctx.fromClause())) {
                tableDomain(table, SplitQueryType.SELECT);
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitDeleteStatement(DmSqlParser.DeleteStatementContext ctx) {
        for (NameParts name : deleteTargetNames(ctx)) {
            deleteDomain(name);
        }
        if (ctx.deleteMultiTableClause() != null) {
            for (NameParts table : collectTables(ctx.deleteMultiTableClause().deleteTableList().tableSource())) {
                tableDomain(table, SplitQueryType.SELECT);
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitMergeStatement(DmSqlParser.MergeStatementContext ctx) {
        if (ctx.mergeIntoTarget().qualifiedName() != null) {
            updateDomain(NameParts.from(ctx.mergeIntoTarget().qualifiedName()), SplitQueryType.MERGE);
        } else if (ctx.mergeIntoTarget().selectStatement() != null) {
            addDmlTargetDomains(ctx.mergeIntoTarget().selectStatement(), SplitQueryType.MERGE);
        }
        if (ctx.mergeSource().tableSource() != null) {
            List<NameParts> tables = new ArrayList<>();
            collectTableSource(ctx.mergeSource().tableSource(), tables);
            for (NameParts table : tables) {
                tableDomain(table, SplitQueryType.SELECT);
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitFlashbackStatement(DmSqlParser.FlashbackStatementContext ctx) {
        for (DmSqlParser.QualifiedNameContext nameContext : ctx.qualifiedName()) {
            tableDomain(NameParts.from(nameContext), SplitQueryType.ADMIN_TABLE);
        }
        return null;
    }

    @Override
    public Void visitRefreshMaterializedViewStatement(DmSqlParser.RefreshMaterializedViewStatementContext ctx) {
        RdbViewDomain domain = new RdbViewDomain();
        setViewName(domain, NameParts.from(ctx.qualifiedName()));
        domain.setMaterialized(true);
        add(domain, SplitQueryType.ADMIN);
        return null;
    }

    @Override
    public Void visitTableCreate(DmSqlParser.TableCreateContext ctx) {
        SplitQueryType type = SplitQueryType.CREATE_TABLE;
        tableDomain(schemaScoped(NameParts.from(ctx.targetTable)), type);
        if (ctx.likeSourceTable != null) {
            tableDomain(NameParts.from(ctx.likeSourceTable), SplitQueryType.SELECT);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitViewCreate(DmSqlParser.ViewCreateContext ctx) {
        RdbViewDomain domain = new RdbViewDomain();
        NameParts name = schemaScoped(NameParts.from(ctx.qualifiedName()));
        domain.setCatalog(name.catalog());
        domain.setSchema(name.schema());
        domain.setView(name.name());
        domain.setMaterialized(ctx.MATERIALIZED() != null);
        add(domain, SplitQueryType.CREATE_VIEW);
        return visitChildren(ctx);
    }

    @Override
    public Void visitMaterializedViewPrebuiltClause(DmSqlParser.MaterializedViewPrebuiltClauseContext ctx) {
        if (ctx.prebuiltTable != null) {
            tableDomain(NameParts.from(ctx.prebuiltTable), SplitQueryType.ALTER_TABLE);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitMaterializedViewLogCreate(DmSqlParser.MaterializedViewLogCreateContext ctx) {
        NameParts name = NameParts.from(ctx.qualifiedName());
        objectDomain(name, SplitQueryType.CREATE_LOG);
        tableDomain(name, SplitQueryType.ALTER_TABLE);
        return null;
    }

    @Override
    public Void visitIndexCreate(DmSqlParser.IndexCreateContext ctx) {
        List<DmSqlParser.QualifiedNameContext> names = ctx.qualifiedName();
        RdbIndexDomain domain = new RdbIndexDomain();
        if (!names.isEmpty()) {
            NameParts index = schemaScoped(NameParts.from(names.get(0)));
            domain.setCatalog(index.catalog());
            domain.setSchema(index.schema());
            domain.setName(index.name());
        }
        if (names.size() > 1) {
            NameParts table = schemaScoped(NameParts.from(names.get(1)));
            domain.setTableCatalog(table.catalog());
            domain.setTableSchema(table.schema());
            domain.setTableName(table.name());
        }
        add(domain, SplitQueryType.ADD_INDEX);
        return null;
    }

    @Override
    public Void visitSchemaCreate(DmSqlParser.SchemaCreateContext ctx) {
        RdbSchemaDomain domain = new RdbSchemaDomain();
        NameParts name = ctx.schemaName == null ? null : NameParts.from(ctx.schemaName);
        String schema = name == null ? schemaAuthorizationOwner(ctx) : name.name();
        if (name != null) {
            domain.setCatalog(name.catalog());
        }
        domain.setSchema(schema);
        add(domain, SplitQueryType.CREATE_SCHEMA);
        if (schema == null) {
            return visitChildren(ctx);
        }
        schemaScopes.add(schema);
        try {
            return visitChildren(ctx);
        } finally {
            schemaScopes.remove(schemaScopes.size() - 1);
        }
    }

    @Override
    public Void visitSequenceCreate(DmSqlParser.SequenceCreateContext ctx) {
        RdbSequenceDomain domain = new RdbSequenceDomain();
        setObjectName(domain, NameParts.from(ctx.qualifiedName()));
        add(domain, SplitQueryType.CREATE_SEQUENCE);
        return null;
    }

    @Override
    public Void visitUserCreate(DmSqlParser.UserCreateContext ctx) {
        RdbUserDomain domain = new RdbUserDomain();
        domain.setUser(NameParts.clean(ctx.identifier().getText()));
        add(domain, SplitQueryType.CREATE_USER);
        return null;
    }

    @Override
    public Void visitRoleCreate(DmSqlParser.RoleCreateContext ctx) {
        RdbRoleDomain domain = new RdbRoleDomain();
        domain.setRole(NameParts.clean(ctx.identifier().getText()));
        add(domain, SplitQueryType.CREATE_ROLE);
        return null;
    }

    @Override
    public Void visitProcedureCreate(DmSqlParser.ProcedureCreateContext ctx) {
        RdbProcedureDomain domain = new RdbProcedureDomain();
        setObjectName(domain, NameParts.from(ctx.qualifiedName()));
        add(domain, SplitQueryType.CREATE_PROG_OBJ);
        return visitChildren(ctx);
    }

    @Override
    public Void visitFunctionCreate(DmSqlParser.FunctionCreateContext ctx) {
        RdbFunctionDomain domain = new RdbFunctionDomain();
        setObjectName(domain, NameParts.from(ctx.qualifiedName()));
        add(domain, SplitQueryType.CREATE_PROG_OBJ);
        return visitChildren(ctx);
    }

    @Override
    public Void visitTriggerCreate(DmSqlParser.TriggerCreateContext ctx) {
        RdbTriggerDomain domain = new RdbTriggerDomain();
        NameParts name = schemaScoped(NameParts.from(ctx.qualifiedName()));
        domain.setCatalog(name.catalog());
        domain.setSchema(name.schema());
        domain.setName(name.name());
        add(domain, SplitQueryType.CREATE_TRIGGER);
        if (ctx.triggerCreateTail().tableTriggerCreateTail() != null) {
            tableDomain(NameParts.from(firstQualifiedName(ctx.triggerCreateTail().tableTriggerCreateTail().qualifiedName())), SplitQueryType.ALTER_TABLE);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitSynonymCreate(DmSqlParser.SynonymCreateContext ctx) {
        RdbSynonymDomain domain = new RdbSynonymDomain();
        setObjectName(domain, NameParts.from(ctx.qualifiedName(0)));
        add(domain, SplitQueryType.CREATE_SYNONYM);
        return null;
    }

    @Override
    public Void visitObjectCreate(DmSqlParser.ObjectCreateContext ctx) {
        if (ctx.replaceableObjectCreate() != null) {
            return visit(ctx.replaceableObjectCreate());
        }
        NameParts name = schemaScoped(objectCreateName(ctx));
        if (ctx.TABLESPACE() != null) {
            objectDomain(name, SplitQueryType.CREATE_TABLESPACE);
        } else if (ctx.DOMAIN() != null || ctx.typeBodyCreate() != null || ctx.typeCreate() != null) {
            objectDomain(name, SplitQueryType.CREATE_TYPE);
        } else if (ctx.operatorCreate() != null) {
            objectDomain(name, SplitQueryType.CREATE_PROG_OBJ);
        } else if (ctx.PROFILE() != null) {
            configDomain(name == null ? null : name.name());
        } else if (ctx.classBodyCreate() != null || ctx.javaClassCreate() != null || ctx.classCreate() != null) {
            objectDomain(name, SplitQueryType.CREATE_TYPE);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitReplaceableObjectCreate(DmSqlParser.ReplaceableObjectCreateContext ctx) {
        NameParts name = schemaScoped(replaceableObjectCreateName(ctx));
        if (ctx.PACKAGE() != null) {
            objectDomain(name, SplitQueryType.CREATE_PROG_OBJ);
        } else if (ctx.LIBRARY() != null) {
            objectDomain(name, SplitQueryType.CREATE_LIBRARY);
        } else if (ctx.typeBodyCreate() != null || ctx.typeCreate() != null) {
            objectDomain(name, SplitQueryType.CREATE_TYPE);
        } else if (ctx.classBodyCreate() != null || ctx.javaClassCreate() != null || ctx.classCreate() != null) {
            objectDomain(name, SplitQueryType.CREATE_TYPE);
        } else if (ctx.LINK() != null || ctx.DIRECTORY() != null || ctx.CONTEXT() != null) {
            configDomain(name == null ? null : name.name());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitAlterTarget(DmSqlParser.AlterTargetContext ctx) {
        DmSqlParser.QualifiedNameContext qualifiedName = firstQualifiedName(ctx.qualifiedName());
        NameParts name = qualifiedName == null ? null : schemaScoped(NameParts.from(qualifiedName));
        if (ctx.TABLE() != null) {
            tableDomain(name, SplitQueryType.ALTER_TABLE);
        } else if (ctx.INDEX() != null) {
            indexDomain(contextIndexName(ctx.contextIndexName, name), contextTableName(ctx.contextTableName), SplitQueryType.ALTER_INDEX);
        } else if (ctx.VIEW() != null) {
            RdbViewDomain domain = new RdbViewDomain();
            setViewName(domain, name);
            domain.setMaterialized(ctx.MATERIALIZED() != null);
            add(domain, SplitQueryType.ALTER_VIEW);
        } else if (ctx.SEQUENCE() != null) {
            RdbSequenceDomain domain = new RdbSequenceDomain();
            setObjectName(domain, name);
            add(domain, SplitQueryType.ALTER_SEQUENCE);
        } else if (ctx.USER() != null) {
            RdbUserDomain domain = new RdbUserDomain();
            domain.setUser(NameParts.clean(ctx.identifier().getText()));
            add(domain, SplitQueryType.ALTER_USER);
        } else if (ctx.PROCEDURE() != null) {
            RdbProcedureDomain domain = new RdbProcedureDomain();
            setObjectName(domain, name);
            add(domain, SplitQueryType.ALTER_PROG_OBJ);
        } else if (ctx.FUNCTION() != null) {
            RdbFunctionDomain domain = new RdbFunctionDomain();
            setObjectName(domain, name);
            add(domain, SplitQueryType.ALTER_PROG_OBJ);
        } else if (ctx.TRIGGER() != null) {
            RdbTriggerDomain domain = new RdbTriggerDomain();
            if (name != null) {
                domain.setCatalog(name.catalog());
                domain.setSchema(name.schema());
                domain.setName(name.name());
            }
            add(domain, SplitQueryType.ALTER_TRIGGER);
        } else if (ctx.PACKAGE() != null) {
            objectDomain(name, SplitQueryType.ADMIN_PROG_OBJ);
        } else if (ctx.TABLESPACE() != null) {
            objectDomain(name, SplitQueryType.ALTER_TABLESPACE);
        } else if (ctx.PROFILE() != null) {
            configDomain(ctx.identifier() == null ? null : NameParts.clean(ctx.identifier().getText()));
        } else if (ctx.TYPE() != null) {
            objectDomain(name, SplitQueryType.ADMIN_TYPE);
        } else if (ctx.CLASS() != null) {
            objectDomain(name, SplitQueryType.ADMIN_TYPE);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitDropTarget(DmSqlParser.DropTargetContext ctx) {
        DmSqlParser.QualifiedNameContext qualifiedName = firstQualifiedName(ctx.qualifiedName());
        NameParts name = qualifiedName == null ? null : NameParts.from(qualifiedName);
        if (ctx.TABLE() != null) {
            tableDomain(name, SplitQueryType.DROP_TABLE);
        } else if (ctx.MATERIALIZED() != null && ctx.LOG() != null) {
            objectDomain(name, SplitQueryType.DROP_LOG);
            tableDomain(name, SplitQueryType.ALTER_TABLE);
        } else if (ctx.VIEW() != null) {
            RdbViewDomain domain = new RdbViewDomain();
            setViewName(domain, name);
            domain.setMaterialized(ctx.MATERIALIZED() != null);
            add(domain, SplitQueryType.DROP_VIEW);
        } else if (ctx.INDEX() != null) {
            indexDomain(contextIndexName(ctx.contextIndexName, name), contextTableName(ctx.contextTableName), SplitQueryType.DROP_INDEX);
        } else if (ctx.SCHEMA() != null || ctx.DATABASE() != null) {
            RdbSchemaDomain domain = new RdbSchemaDomain();
            if (name != null) {
                domain.setCatalog(name.catalog());
                domain.setSchema(name.name());
            }
            add(domain, SplitQueryType.DROP_SCHEMA);
        } else if (ctx.SEQUENCE() != null) {
            RdbSequenceDomain domain = new RdbSequenceDomain();
            setObjectName(domain, name);
            add(domain, SplitQueryType.DROP_SEQUENCE);
        } else if (ctx.USER() != null) {
            RdbUserDomain domain = new RdbUserDomain();
            domain.setUser(NameParts.clean(ctx.identifier().getText()));
            add(domain, SplitQueryType.DROP_USER);
        } else if (ctx.ROLE() != null) {
            RdbRoleDomain domain = new RdbRoleDomain();
            domain.setRole(NameParts.clean(ctx.identifier().getText()));
            add(domain, SplitQueryType.DROP_ROLE);
        } else if (ctx.PROCEDURE() != null) {
            RdbProcedureDomain domain = new RdbProcedureDomain();
            setObjectName(domain, name);
            add(domain, SplitQueryType.DROP_PROG_OBJ);
        } else if (ctx.FUNCTION() != null) {
            RdbFunctionDomain domain = new RdbFunctionDomain();
            setObjectName(domain, name);
            add(domain, SplitQueryType.DROP_PROG_OBJ);
        } else if (ctx.TRIGGER() != null) {
            RdbTriggerDomain domain = new RdbTriggerDomain();
            if (name != null) {
                domain.setCatalog(name.catalog());
                domain.setSchema(name.schema());
                domain.setName(name.name());
            }
            add(domain, SplitQueryType.DROP_TRIGGER);
        } else if (ctx.SYNONYM() != null) {
            RdbSynonymDomain domain = new RdbSynonymDomain();
            setObjectName(domain, name);
            add(domain, SplitQueryType.DROP_SYNONYM);
        } else if (ctx.PACKAGE() != null) {
            objectDomain(name, SplitQueryType.DROP_PROG_OBJ);
        } else if (ctx.TABLESPACE() != null) {
            objectDomain(name, SplitQueryType.DROP_TABLESPACE);
        } else if (ctx.LIBRARY() != null) {
            objectDomain(name, SplitQueryType.DROP_LIBRARY);
        } else if (ctx.DOMAIN() != null || ctx.TYPE() != null) {
            objectDomain(name, SplitQueryType.DROP_TYPE);
        } else if (ctx.CLASS() != null) {
            objectDomain(name, SplitQueryType.DROP_TYPE);
        } else if (ctx.LINK() != null || ctx.DIRECTORY() != null || ctx.CONTEXT() != null || ctx.PROFILE() != null) {
            NameParts objectName = objectTargetName(qualifiedName, ctx.identifier());
            configDomain(objectName == null ? null : objectName.name());
        } else if (ctx.OPERATOR() != null) {
            objectDomain(operatorName(ctx.operatorQualifiedName()), SplitQueryType.DROP_PROG_OBJ);
        }
        return null;
    }

    @Override
    public Void visitTruncateStatement(DmSqlParser.TruncateStatementContext ctx) {
        tableDomain(NameParts.from(ctx.qualifiedName()), SplitQueryType.TRUNCATE_TABLE);
        return null;
    }

    @Override
    public Void visitCommentStatement(DmSqlParser.CommentStatementContext ctx) {
        if (ctx.commentTarget().TABLE() != null) {
            tableDomain(schemaScoped(NameParts.from(ctx.commentTarget().qualifiedName())), SplitQueryType.COMMENT_TABLE);
        } else if (ctx.commentTarget().VIEW() != null) {
            RdbViewDomain domain = new RdbViewDomain();
            setViewName(domain, schemaScoped(NameParts.from(ctx.commentTarget().qualifiedName())));
            add(domain, SplitQueryType.ALTER_VIEW);
        } else {
            tableDomain(schemaScoped(columnTableName(ctx.commentTarget().qualifiedName())), SplitQueryType.COMMENT_COLUMN);
        }
        return null;
    }

    @Override
    public Void visitGrantStatement(DmSqlParser.GrantStatementContext ctx) {
        DmSqlParser.GranteeListContext granteeList = ctx.grantPrivilegeStatement() != null ? ctx.grantPrivilegeStatement().granteeList() : ctx.grantRoleStatement().granteeList(1);
        for (DmSqlParser.GranteeContext granteeContext : granteeList.grantee()) {
            RdbGrantDomain domain = new RdbGrantDomain();
            domain.setName(NameParts.clean(granteeContext.getText()));
            add(domain, SplitQueryType.GRANT);
        }
        return null;
    }

    @Override
    public Void visitRevokeStatement(DmSqlParser.RevokeStatementContext ctx) {
        DmSqlParser.GranteeListContext granteeList = ctx.revokePrivilegeStatement() != null ? ctx.revokePrivilegeStatement().granteeList() : ctx.revokeRoleStatement()
            .granteeList(1);
        for (DmSqlParser.GranteeContext granteeContext : granteeList.grantee()) {
            RdbRevokeDomain domain = new RdbRevokeDomain();
            domain.setName(NameParts.clean(granteeContext.getText()));
            add(domain, SplitQueryType.REVOKE);
        }
        return null;
    }

    @Override
    public Void visitCallStatement(DmSqlParser.CallStatementContext ctx) {
        callDomain(NameParts.from(ctx.qualifiedName()));
        return null;
    }

    @Override
    public Void visitProcedureCallStatement(DmSqlParser.ProcedureCallStatementContext ctx) {
        if (ctx.qualifiedName() != null) {
            callDomain(NameParts.from(ctx.qualifiedName()));
        } else {
            callDomain(NameParts.from(ctx.bareRoutineName()));
        }
        return null;
    }

    private void callDomain(NameParts name) {
        RdbCallDomain domain = new RdbCallDomain();
        domain.setCatalog(name.catalog());
        domain.setSchema(name.schema());
        domain.setCallName(name.name());
        add(domain, SplitQueryType.CALL_PROG_OBJ);
    }

    @Override
    public Void visitLockTableStatement(DmSqlParser.LockTableStatementContext ctx) {
        tableDomain(NameParts.from(ctx.qualifiedName()), SplitQueryType.TRANSACTION);
        return null;
    }

    @Override
    public Void visitAlterSessionParallelDmlStatement(DmSqlParser.AlterSessionParallelDmlStatementContext ctx) {
        configDomain("PARALLEL_POLICY");
        return null;
    }

    @Override
    public Void visitSetSchemaStatement(DmSqlParser.SetSchemaStatementContext ctx) {
        RdbSchemaDomain domain = new RdbSchemaDomain();
        NameParts name = NameParts.from(ctx.qualifiedName());
        domain.setCatalog(name.catalog());
        domain.setSchema(name.name());
        add(domain, SplitQueryType.SWITCH_SCHEMA);
        return null;
    }

    @Override
    public Void visitSetTimeZoneStatement(DmSqlParser.SetTimeZoneStatementContext ctx) {
        configDomain("TIME_ZONE");
        return null;
    }

    @Override
    public Void visitSetIdentityInsertStatement(DmSqlParser.SetIdentityInsertStatementContext ctx) {
        tableDomain(NameParts.from(ctx.qualifiedName()), SplitQueryType.TRANSACTION);
        return null;
    }

    @Override
    public Void visitAdminStatement(DmSqlParser.AdminStatementContext ctx) {
        objectDomain(new NameParts(null, null, "DATABASE"), SplitQueryType.ADMIN);
        return null;
    }

    @Override
    public Void visitStatStatement(DmSqlParser.StatStatementContext ctx) {
        NameParts name = NameParts.from(ctx.statTarget().qualifiedName());
        if (ctx.statTarget().INDEX() != null) {
            RdbIndexDomain domain = new RdbIndexDomain();
            setIndexName(domain, name);
            add(domain, SplitQueryType.ADMIN_TABLE);
            return null;
        }
        tableDomain(name, SplitQueryType.ADMIN_TABLE);
        return null;
    }

    @Override
    public Void visitStatProcedureStatement(DmSqlParser.StatProcedureStatementContext ctx) {
        String procedure = ctx.statProcedureName().getText();
        List<DmSqlParser.ExpressionContext> args = ctx.expressionList() == null ? new ArrayList<>() : ctx.expressionList().expression();
        if (isTableStatProcedure(procedure) || isColumnStatProcedure(procedure)) {
            tableDomain(statTargetName(args, 0, 1), SplitQueryType.ADMIN_TABLE);
            return null;
        }
        if (isIndexStatProcedure(procedure)) {
            RdbIndexDomain domain = new RdbIndexDomain();
            setIndexName(domain, statTargetName(args, 0, 1));
            add(domain, SplitQueryType.ADMIN_TABLE);
            return null;
        }
        objectDomain(new NameParts(null, null, "DATABASE"), SplitQueryType.ADMIN_TABLE);
        return null;
    }

    @Override
    public Void visitConfigWriteStatement(DmSqlParser.ConfigWriteStatementContext ctx) {
        if (!ctx.configAssignment().isEmpty()) {
            for (DmSqlParser.ConfigAssignmentContext assignmentContext : ctx.configAssignment()) {
                configDomain(configKeyText(assignmentContext.configKey()));
            }
            return null;
        }
        configDomain(configProcedureKey(ctx));
        return null;
    }

    @Override
    public Void visitAuditAdminStatement(DmSqlParser.AuditAdminStatementContext ctx) {
        objectDomain(new NameParts(null, null, "DATABASE"), SplitQueryType.ADMIN);
        return null;
    }

    @Override
    public Void visitSecurityAdminStatement(DmSqlParser.SecurityAdminStatementContext ctx) {
        objectDomain(new NameParts(null, null, "SECURITY"), SplitQueryType.ADMIN);
        return null;
    }

    private List<NameParts> collectTables(DmSqlParser.FromClauseContext ctx) {
        return collectTables(ctx.tableSource(), false);
    }

    private List<NameParts> collectTables(DmSqlParser.SelectStatementContext ctx) {
        List<NameParts> result = new ArrayList<>();
        collectSelectStatement(ctx, result, true);
        return result;
    }

    private List<NameParts> collectTables(List<DmSqlParser.TableSourceContext> tableSources) {
        return collectTables(tableSources, false);
    }

    private List<NameParts> collectTables(List<DmSqlParser.TableSourceContext> tableSources, boolean descendIntoSubqueries) {
        List<NameParts> result = new ArrayList<>();
        for (DmSqlParser.TableSourceContext tableSourceContext : tableSources) {
            collectTableSource(tableSourceContext, result, descendIntoSubqueries);
        }
        return result;
    }

    private void collectSelectStatement(DmSqlParser.SelectStatementContext ctx, List<NameParts> result, boolean descendIntoSubqueries) {
        if (ctx == null) {
            return;
        }
        boolean pushed = pushCteScope(ctx.withClause());
        try {
            if (ctx.withClause() != null && ctx.withClause().cteDefinitionList() != null) {
                for (DmSqlParser.CteDefinitionContext cteDefinitionContext : ctx.withClause().cteDefinitionList().cteDefinition()) {
                    collectSelectStatement(cteDefinitionContext.selectStatement(), result, descendIntoSubqueries);
                }
            }
            collectSelectOperand(ctx.selectOperand(), result, descendIntoSubqueries);
            for (DmSqlParser.QueryRemainderContext queryRemainderContext : ctx.queryRemainder()) {
                collectSelectOperand(queryRemainderContext.selectOperand(), result, descendIntoSubqueries);
            }
        } finally {
            popCteScope(pushed);
        }
    }

    private void collectSelectOperand(DmSqlParser.SelectOperandContext ctx, List<NameParts> result, boolean descendIntoSubqueries) {
        if (ctx == null) {
            return;
        }
        if (ctx.selectQuery() != null) {
            if (ctx.selectQuery().fromClause() != null) {
                result.addAll(collectTables(ctx.selectQuery().fromClause().tableSource(), descendIntoSubqueries));
            }
            return;
        }
        collectSelectStatement(ctx.selectStatement(), result, descendIntoSubqueries);
    }

    private void collectTableSource(DmSqlParser.TableSourceContext ctx, List<NameParts> result) {
        collectTableSource(ctx, result, false);
    }

    private void collectTableSource(DmSqlParser.TableSourceContext ctx, List<NameParts> result, boolean descendIntoSubqueries) {
        collectTablePrimary(ctx.tablePrimary(), result, descendIntoSubqueries);
        for (DmSqlParser.JoinClauseContext joinClauseContext : ctx.joinClause()) {
            if (joinClauseContext.tablePrimary() != null) {
                collectTablePrimary(joinClauseContext.tablePrimary(), result, descendIntoSubqueries);
            }
            if (joinClauseContext.applyJoinClause() != null) {
                collectTablePrimary(joinClauseContext.applyJoinClause().tablePrimary(), result, descendIntoSubqueries);
            }
        }
    }

    private void collectTablePrimary(DmSqlParser.TablePrimaryContext ctx, List<NameParts> result) {
        collectTablePrimary(ctx, result, false);
    }

    private void collectTablePrimary(DmSqlParser.TablePrimaryContext ctx, List<NameParts> result, boolean descendIntoSubqueries) {
        if (ctx.qualifiedName() != null) {
            NameParts name = NameParts.from(ctx.qualifiedName());
            if (!isCteReference(name)) {
                result.add(name);
            }
        }
        if (descendIntoSubqueries && ctx.selectStatement() != null) {
            collectSelectStatement(ctx.selectStatement(), result, true);
        }
        for (DmSqlParser.TableSourceContext tableSourceContext : ctx.tableSource()) {
            collectTableSource(tableSourceContext, result, descendIntoSubqueries);
        }
    }

    private boolean pushCteScope(DmSqlParser.WithClauseContext ctx) {
        List<String> cteNames = cteNames(ctx);
        if (cteNames.isEmpty()) {
            return false;
        }
        cteScopes.add(cteNames);
        return true;
    }

    private void popCteScope(boolean pushed) {
        if (pushed) {
            cteScopes.remove(cteScopes.size() - 1);
        }
    }

    private List<String> cteNames(DmSqlParser.WithClauseContext ctx) {
        List<String> names = new ArrayList<>();
        if (ctx == null || ctx.cteDefinitionList() == null) {
            return names;
        }
        for (DmSqlParser.CteDefinitionContext cteDefinitionContext : ctx.cteDefinitionList().cteDefinition()) {
            names.add(NameParts.clean(cteDefinitionContext.identifier().getText()));
        }
        return names;
    }

    private boolean isCteReference(NameParts name) {
        if (name == null || name.catalog() != null || name.schema() != null) {
            return false;
        }
        String table = name.name();
        if (table == null) {
            return false;
        }
        for (int i = cteScopes.size() - 1; i >= 0; i--) {
            for (String cteName : cteScopes.get(i)) {
                if (table.equalsIgnoreCase(cteName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addDmlTargetDomains(DmSqlParser.SelectStatementContext ctx, SplitQueryType type) {
        for (NameParts name : collectTables(ctx)) {
            if (type == SplitQueryType.INSERT) {
                insertDomain(name);
            } else if (type == SplitQueryType.DELETE) {
                deleteDomain(name);
            } else {
                updateDomain(name, type);
            }
        }
    }

    private List<NameParts> deleteTargetNames(DmSqlParser.DeleteStatementContext ctx) {
        List<NameParts> result = new ArrayList<>();
        DmSqlParser.TablePrimaryContext target = ctx.deleteTarget().tablePrimary();
        if (target.qualifiedName() != null) {
            NameParts name = NameParts.from(target.qualifiedName());
            if (name.schema() == null && ctx.deleteMultiTableClause() != null) {
                NameParts aliasTarget = resolveTableAlias(name.name(), ctx.deleteMultiTableClause().deleteTableList().tableSource());
                result.add(aliasTarget == null ? name : aliasTarget);
            } else {
                result.add(name);
            }
            return result;
        }
        if (target.selectStatement() != null) {
            result.addAll(collectTables(target.selectStatement()));
            return result;
        }
        collectTablePrimary(target, result);
        return result;
    }

    private NameParts resolveTableAlias(String alias, List<DmSqlParser.TableSourceContext> tableSources) {
        for (DmSqlParser.TableSourceContext tableSourceContext : tableSources) {
            NameParts name = resolveTableAlias(alias, tableSourceContext);
            if (name != null) {
                return name;
            }
        }
        return null;
    }

    private NameParts resolveTableAlias(String alias, DmSqlParser.TableSourceContext ctx) {
        NameParts name = resolveTableAlias(alias, ctx.tablePrimary());
        if (name != null) {
            return name;
        }
        for (DmSqlParser.JoinClauseContext joinClauseContext : ctx.joinClause()) {
            if (joinClauseContext.tablePrimary() != null) {
                name = resolveTableAlias(alias, joinClauseContext.tablePrimary());
            } else if (joinClauseContext.applyJoinClause() != null) {
                name = resolveTableAlias(alias, joinClauseContext.applyJoinClause().tablePrimary());
            }
            if (name != null) {
                return name;
            }
        }
        return null;
    }

    private NameParts resolveTableAlias(String alias, DmSqlParser.TablePrimaryContext ctx) {
        if (ctx.qualifiedName() != null && aliasMatches(alias, ctx.tableAlias())) {
            return NameParts.from(ctx.qualifiedName());
        }
        for (DmSqlParser.TableSourceContext tableSourceContext : ctx.tableSource()) {
            NameParts name = resolveTableAlias(alias, tableSourceContext);
            if (name != null) {
                return name;
            }
        }
        return null;
    }

    private boolean aliasMatches(String alias, DmSqlParser.TableAliasContext ctx) {
        return ctx != null && alias != null && alias.equalsIgnoreCase(NameParts.clean(ctx.identifier().getText()));
    }

    private NameParts columnTableName(DmSqlParser.QualifiedNameContext ctx) {
        NameParts name = NameParts.from(ctx);
        String text = ctx == null ? null : ctx.getText();
        if (text == null) {
            return name;
        }
        int lastDot = text.lastIndexOf('.');
        if (lastDot < 0) {
            return name;
        }
        String tablePath = text.substring(0, lastDot);
        String[] parts = tablePath.split("\\.");
        if (parts.length == 1) {
            return new NameParts(null, null, NameParts.clean(parts[0]));
        }
        if (parts.length == 2) {
            return new NameParts(null, NameParts.clean(parts[0]), NameParts.clean(parts[1]));
        }
        return new NameParts(NameParts.clean(parts[parts.length - 3]), NameParts.clean(parts[parts.length - 2]), NameParts.clean(parts[parts.length - 1]));
    }

    private RdbTableDomain tableDomain(NameParts name, SplitQueryType type) {
        RdbTableDomain domain = new RdbTableDomain();
        if (name != null) {
            domain.setCatalog(name.catalog());
            domain.setSchema(name.schema());
            domain.setTable(name.name());
        }
        return add(domain, type);
    }

    private RdbIndexDomain indexDomain(NameParts index, NameParts table, SplitQueryType type) {
        RdbIndexDomain domain = new RdbIndexDomain();
        setIndexName(domain, index);
        table = schemaScoped(table);
        if (table != null) {
            domain.setTableCatalog(table.catalog());
            domain.setTableSchema(table.schema());
            domain.setTableName(table.name());
        }
        return add(domain, type);
    }

    private RdbObjectDomain objectDomain(NameParts name, SplitQueryType type) {
        RdbObjectDomain domain = new RdbObjectDomain();
        name = schemaScoped(name);
        if (name != null) {
            domain.setCatalog(name.catalog());
            domain.setSchema(name.schema());
            domain.setName(name.name());
        }
        return add(domain, type);
    }

    private RdbConfigDomain configDomain(String configKey) {
        return add(new RdbConfigDomain(configKey), SplitQueryType.SYSTEM_SETTING_WRITE);
    }

    private boolean isTableStatProcedure(String procedure) {
        return "SP_TAB_INDEX_STAT_INIT".equalsIgnoreCase(procedure) || "SP_TAB_COL_STAT_INIT".equalsIgnoreCase(procedure) || "SP_STAT_ON_TABLE_COLS".equalsIgnoreCase(procedure)
               || "SP_TAB_STAT_INIT".equalsIgnoreCase(procedure) || "SP_TAB_COL_STAT_DEINIT".equalsIgnoreCase(procedure) || "SP_TAB_STAT_DEINIT".equalsIgnoreCase(procedure)
               || "SP_TAB_MSTAT_DEINIT".equalsIgnoreCase(procedure);
    }

    private boolean isColumnStatProcedure(String procedure) {
        return "SP_COL_STAT_INIT".equalsIgnoreCase(procedure) || "SP_COL_STAT_DEINIT".equalsIgnoreCase(procedure);
    }

    private boolean isIndexStatProcedure(String procedure) {
        return "SP_INDEX_STAT_INIT".equalsIgnoreCase(procedure) || "SP_INDEX_STAT_DEINIT".equalsIgnoreCase(procedure);
    }

    private NameParts statTargetName(List<DmSqlParser.ExpressionContext> args, int schemaIndex, int objectIndex) {
        String schema = statArgument(args, schemaIndex);
        String name = statArgument(args, objectIndex);
        return new NameParts(null, schema, name);
    }

    private String statArgument(List<DmSqlParser.ExpressionContext> args, int index) {
        if (index >= args.size()) {
            return null;
        }
        return cleanString(args.get(index).getText());
    }

    private String configKeyText(DmSqlParser.ConfigKeyContext ctx) {
        if (ctx == null) {
            return null;
        }
        if (ctx.STRING() != null) {
            return cleanString(ctx.STRING().getText());
        }
        return NameParts.clean(ctx.qualifiedName().getText());
    }

    private String configProcedureKey(DmSqlParser.ConfigWriteStatementContext ctx) {
        List<DmSqlParser.ExpressionContext> args = ctx.expressionList() == null ? new ArrayList<>() : ctx.expressionList().expression();
        String procedure = ctx.configWriteProcedure().getText();
        if ("SP_SET_SESSION_READONLY".equalsIgnoreCase(procedure)) {
            return "SESSION_READONLY";
        }
        int index = 0;
        if ("SP_SET_PARAM_IN_SESSION".equalsIgnoreCase(procedure)) {
            index = 2;
        } else if ("SF_SET_SYSTEM_PARA_VALUE".equalsIgnoreCase(procedure)) {
            index = args.size() >= 5 ? 1 : 0;
        } else if (procedure.startsWith("SP_SET_PARA_") || "SP_SET_PARA_VALUE".equalsIgnoreCase(procedure) || "SP_SET_INI_PARA_VALUE".equalsIgnoreCase(procedure)) {
            index = args.size() >= 4 ? 2 : 1;
        }
        if (index >= args.size()) {
            return procedure;
        }
        return cleanString(args.get(index).getText());
    }

    private String cleanString(String text) {
        if (text != null && text.length() >= 2 && text.startsWith("'") && text.endsWith("'")) {
            return text.substring(1, text.length() - 1).replace("''", "'");
        }
        return NameParts.clean(text);
    }

    private String schemaAuthorizationOwner(DmSqlParser.SchemaCreateContext ctx) {
        if (ctx.schemaAuthorizationOnly() != null) {
            return NameParts.clean(ctx.schemaAuthorizationOnly().schemaOwner.getText());
        }
        if (ctx.schemaAuthorizationClause() != null) {
            return NameParts.clean(ctx.schemaAuthorizationClause().schemaOwner.getText());
        }
        return null;
    }

    private NameParts schemaScoped(NameParts name) {
        if (name == null || name.name() == null || name.schema() != null || schemaScopes.isEmpty()) {
            return name;
        }
        return new NameParts(name.catalog(), schemaScopes.get(schemaScopes.size() - 1), name.name());
    }

    private NameParts contextIndexName(DmSqlParser.QualifiedNameContext contextIndexName, NameParts fallback) {
        return contextIndexName == null ? fallback : NameParts.from(contextIndexName);
    }

    private NameParts contextTableName(DmSqlParser.QualifiedNameContext contextTableName) {
        return contextTableName == null ? null : NameParts.from(contextTableName);
    }

    private DmSqlParser.QualifiedNameContext firstQualifiedName(List<DmSqlParser.QualifiedNameContext> qualifiedNames) {
        return qualifiedNames == null || qualifiedNames.isEmpty() ? null : qualifiedNames.get(0);
    }

    private NameParts objectCreateName(DmSqlParser.ObjectCreateContext ctx) {
        if (ctx.replaceableObjectCreate() != null) {
            return replaceableObjectCreateName(ctx.replaceableObjectCreate());
        }
        if (ctx.typeCreate() != null) {
            return NameParts.from(ctx.typeCreate().qualifiedName());
        }
        if (ctx.typeBodyCreate() != null) {
            return NameParts.from(ctx.typeBodyCreate().qualifiedName());
        }
        if (ctx.classBodyCreate() != null) {
            return NameParts.from(ctx.classBodyCreate().qualifiedName());
        }
        if (ctx.javaClassCreate() != null) {
            return NameParts.from(ctx.javaClassCreate().qualifiedName());
        }
        if (ctx.classCreate() != null) {
            return NameParts.from(ctx.classCreate().qualifiedName());
        }
        if (ctx.operatorCreate() != null) {
            return operatorName(ctx.operatorCreate().operatorQualifiedName());
        }
        if (ctx.PROFILE() != null) {
            return objectTargetName(null, ctx.identifier());
        }
        return objectTargetName(ctx.qualifiedName(), null);
    }

    private NameParts replaceableObjectCreateName(DmSqlParser.ReplaceableObjectCreateContext ctx) {
        if (ctx.CONTEXT() != null) {
            return objectTargetName(null, ctx.identifier());
        }
        if (ctx.typeCreate() != null) {
            return NameParts.from(ctx.typeCreate().qualifiedName());
        }
        if (ctx.typeBodyCreate() != null) {
            return NameParts.from(ctx.typeBodyCreate().qualifiedName());
        }
        if (ctx.classBodyCreate() != null) {
            return NameParts.from(ctx.classBodyCreate().qualifiedName());
        }
        if (ctx.javaClassCreate() != null) {
            return NameParts.from(ctx.javaClassCreate().qualifiedName());
        }
        if (ctx.classCreate() != null) {
            return NameParts.from(ctx.classCreate().qualifiedName());
        }
        return objectTargetName(ctx.qualifiedName(), null);
    }

    private NameParts objectTargetName(DmSqlParser.QualifiedNameContext qualifiedName, DmSqlParser.IdentifierContext identifier) {
        if (qualifiedName != null) {
            return NameParts.from(qualifiedName);
        }
        if (identifier != null) {
            return new NameParts(null, null, NameParts.clean(identifier.getText()));
        }
        return null;
    }

    private NameParts operatorName(DmSqlParser.OperatorQualifiedNameContext ctx) {
        if (ctx == null) {
            return null;
        }
        String text = ctx.getText();
        int dot = text.lastIndexOf('.');
        if (dot < 0) {
            return new NameParts(null, null, NameParts.clean(text));
        }
        return new NameParts(null, NameParts.clean(text.substring(0, dot)), NameParts.clean(text.substring(dot + 1)));
    }

    private <T extends RuleDomain> T add(T domain, SplitQueryType type) {
        domain.addSqlType(type);
        domain.setAuditKind(type.getAuditKind());
        domains.add(domain);
        return domain;
    }

    private void setObjectName(RdbSequenceDomain domain, NameParts name) {
        name = schemaScoped(name);
        if (name != null) {
            domain.setCatalog(name.catalog());
            domain.setSchema(name.schema());
            domain.setName(name.name());
        }
    }

    private void setObjectName(RdbProcedureDomain domain, NameParts name) {
        name = schemaScoped(name);
        if (name != null) {
            domain.setCatalog(name.catalog());
            domain.setSchema(name.schema());
            domain.setName(name.name());
        }
    }

    private void setObjectName(RdbFunctionDomain domain, NameParts name) {
        name = schemaScoped(name);
        if (name != null) {
            domain.setCatalog(name.catalog());
            domain.setSchema(name.schema());
            domain.setName(name.name());
        }
    }

    private void setObjectName(RdbSynonymDomain domain, NameParts name) {
        name = schemaScoped(name);
        if (name != null) {
            domain.setCatalog(name.catalog());
            domain.setSchema(name.schema());
            domain.setName(name.name());
        }
    }

    private void setViewName(RdbViewDomain domain, NameParts name) {
        name = schemaScoped(name);
        if (name != null) {
            domain.setCatalog(name.catalog());
            domain.setSchema(name.schema());
            domain.setView(name.name());
        }
    }

    private void setIndexName(RdbIndexDomain domain, NameParts name) {
        name = schemaScoped(name);
        if (name != null) {
            domain.setCatalog(name.catalog());
            domain.setSchema(name.schema());
            domain.setName(name.name());
        }
    }

    private record NameParts(String catalog, String schema, String name) {

        private static NameParts from(DmSqlParser.QualifiedNameContext ctx) {
            if (ctx == null) {
                return new NameParts(null, null, null);
            }
            List<String> parts = new ArrayList<>();
            parts.add(clean(ctx.dottedName().identifier().getText()));
            for (DmSqlParser.DottedNamePartContext partContext : ctx.dottedName().dottedNamePart()) {
                parts.add(clean(partContext.getText()));
            }
            return fromParts(parts);
        }

        private static NameParts from(DmSqlParser.BareRoutineNameContext ctx) {
            if (ctx == null) {
                return new NameParts(null, null, null);
            }
            List<String> parts = new ArrayList<>();
            parts.add(clean(ctx.regularIdentifier().getText()));
            for (DmSqlParser.DottedNamePartContext partContext : ctx.dottedNamePart()) {
                parts.add(clean(partContext.getText()));
            }
            return fromParts(parts);
        }

        private static NameParts fromParts(List<String> parts) {
            if (parts.isEmpty()) {
                return new NameParts(null, null, null);
            }
            String name = parts.get(parts.size() - 1);
            String schema = parts.size() > 1 ? parts.get(parts.size() - 2) : null;
            String catalog = parts.size() > 2 ? parts.get(parts.size() - 3) : null;
            return new NameParts(catalog, schema, name);
        }

        private static String clean(String text) {
            if (text == null || text.length() < 2) {
                return text;
            }
            if (text.startsWith("\"") && text.endsWith("\"")) {
                return text.substring(1, text.length() - 1).replace("\"\"", "\"");
            }
            if (text.startsWith("[") && text.endsWith("]")) {
                return text.substring(1, text.length() - 1);
            }
            return text;
        }
    }
}
