/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.oracle.analysis.behavior;

import java.util.*;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.oracle.parser.antlr.PlSqlParserBaseVisitor;
import com.clougence.sql.oracle.parser.antlr.PlSqlParser;
import com.clougence.sql.oracle.parser.antlr.PlSqlParser.*;
import com.clougence.utils.StringUtils;

final class OraBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {

    private final Parser                  parser;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    OraBehaviorParserVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.levels = levels;
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
    }

    List<StatementBehavior> behaviors() {
        return behaviors;
    }

    @Override
    public Void visit(ParseTree tree) {
        OraStatementBehaviorVisitor visitor = new OraStatementBehaviorVisitor(parser, levels, baseLine, baseColumn);
        visitor.visit(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class OraStatementBehaviorVisitor extends PlSqlParserBaseVisitor<Void> {

    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    OraStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitAlter_system(Alter_systemContext ctx) {
        if (!ctx.alter_system_set_clause().isEmpty()) {
            for (Alter_system_set_clauseContext setting : ctx.alter_system_set_clause()) {
                addUnary(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE,
                    principal(TargetType.ConfigKey, setting.system_parameter_name()));
            }
        } else if (!ctx.alter_system_reset_clause().isEmpty()) {
            for (Alter_system_reset_clauseContext setting : ctx.alter_system_reset_clause()) {
                addUnary(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.RESET,
                    principal(TargetType.ConfigKey, setting.system_parameter_name()));
            }
        } else if (ctx.alter_system_security_clause() != null) {
            // Wallet passwords and key material must not become resource names.
            addUnary(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE,
                objects.instanceObject(TargetType.ConfigKey, ctx.alter_system_security_clause()));
        } else if (ctx.archive_log_clause() != null) {
            addUnary(SplitQueryType.MAINTAIN_LOG, BehaviorAction.EXPORT,
                objects.instanceObject(TargetType.Log, ctx.archive_log_clause()));
        } else if (ctx.SWITCH() != null) {
            addUnary(SplitQueryType.MAINTAIN_LOG, BehaviorAction.SWITCH,
                objects.instanceObject(TargetType.Log, ctx.LOGFILE().getSymbol()));
        } else if (ctx.alter_system_flush_clause() != null) {
            systemFlush(ctx.alter_system_flush_clause());
        } else if (ctx.affinity_clauses() != null) {
            visitAffinity_clauses(ctx.affinity_clauses());
        } else {
            systemRuntime(ctx);
        }
        return null;
    }

    private void systemFlush(Alter_system_flush_clauseContext ctx) {
        if (ctx.REDO() != null) {
            addRelation(SplitQueryType.ADMIN_REPLICATION, BehaviorAction.FLUSH,
                objects.instanceObject(TargetType.Log, ctx.REDO().getSymbol()),
                objects(object(TargetType.Catalog, ctx.id_expression())));
            return;
        }
        SplitQueryType type = SplitQueryType.ADMIN_PERFORMANCE;
        if (ctx.PASSWORDFILE_METADATA_CACHE() != null) {
            type = SplitQueryType.ADMIN;
        }
        addUnary(type, BehaviorAction.FLUSH, objects.instanceObject(TargetType.Instance, ctx));
    }

    @Override
    public Void visitAffinity_clauses(Affinity_clausesContext ctx) {
        List<String> names = new ArrayList<>();
        if (ctx.schema_name() != null) {
            collectNames(ctx.schema_name(), names);
        }
        collectNames(ctx.table_name(), names);
        ParserRuleContext start = ctx.table_name();
        if (ctx.schema_name() != null) {
            start = ctx.schema_name();
        }
        BehaviorObject table = objects.object(TargetType.Table, start.start, ctx.table_name().stop, names);
        addUnary(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.CONFIGURE, table);
        if (ctx.id_expression() != null) {
            // SERVICE is referenced, not reconfigured together with the table.
            addUnary(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.READ, principal(TargetType.Object, ctx.id_expression()));
        }
        return null;
    }

    private void systemRuntime(Alter_systemContext ctx) {
        BehaviorAction action;
        switch (ctx.getChild(2).getText().toUpperCase(Locale.ROOT)) {
            case "CHECKPOINT" -> action = BehaviorAction.CHECKPOINT;
            case "CHECK" -> action = BehaviorAction.REFRESH;
            case "ENABLE", "START" -> action = BehaviorAction.START;
            case "DISABLE", "STOP" -> action = BehaviorAction.STOP;
            case "KILL", "DISCONNECT", "CANCEL" -> action = BehaviorAction.TERMINATE;
            case "SUSPEND", "RESUME", "QUIESCE", "UNQUIESCE" -> action = BehaviorAction.CONFIGURE;
            case "SHUTDOWN" -> action = BehaviorAction.STOP;
            case "REGISTER" -> action = BehaviorAction.REFRESH;
            case "RELOCATE" -> action = BehaviorAction.MOVE;
            default -> action = BehaviorAction.UNKNOWN;
        }
        TargetType type = TargetType.Instance;
        if (ctx.CANCEL() != null) {
            // Cancels the running SQL, not its owning session or its referenced tables.
            type = TargetType.Query;
        } else if (ctx.SHUTDOWN() != null || ctx.RELOCATE() != null || ctx.ROLLING() != null) {
            // SDK has no dispatcher, ASM client or rolling-operation type.
            type = TargetType.Object;
        }
        addUnary(SplitQueryType.ADMIN, action, objects.instanceObject(type, ctx));
    }

    @Override
    public Void visitAlter_session(Alter_sessionContext ctx) {
        if (ctx.CLOSE() != null) {
            // Dots belong to the link name, not to a schema qualification.
            addUnary(SplitQueryType.ADMIN, BehaviorAction.STOP,
                objects.object(TargetType.Link, ctx.link_name(), List.of(ctx.link_name().getText())));
        } else if (ctx.SYNC() != null) {
            // Waits for active standby redo apply; does not start replication.
            addUnary(SplitQueryType.ADMIN, BehaviorAction.APPLY,
                objects.unnamedObject(TargetType.Catalog, ctx.SYNC().getSymbol(), UmiTypes.Catalog));
        } else if (ctx.alter_session_set_clause() != null) {
            sessionSettings(ctx.alter_session_set_clause());
        } else {
            addUnary(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE,
                objects.instanceObject(TargetType.ConfigKey, ctx));
        }
        return null;
    }

    private void sessionSettings(Alter_session_set_clauseContext ctx) {
        if (ctx.CONTAINER() != null) {
            List<BehaviorObject> references = new ArrayList<>();
            if (ctx.sn != null) {
                references.add(principal(TargetType.Object, ctx.sn));
            }
            addRelation(SplitQueryType.SWITCH_CATALOG, BehaviorAction.SWITCH, object(TargetType.Catalog, ctx.cn), references);
        } else if (ctx.EDITION() != null) {
            addUnary(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.SWITCH, principal(TargetType.Object, ctx.en));
        } else if (ctx.ROW() != null || ctx.COLLATION() != null) {
            TerminalNode end = ctx.COLLATION();
            String name = "DEFAULT COLLATION";
            if (ctx.ROW() != null) {
                end = ctx.VISIBILITY();
                name = "ROW ARCHIVAL VISIBILITY";
            }
            addUnary(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE,
                objects.instanceObject(TargetType.ConfigKey, ctx.start, end.getSymbol(), name));
        } else {
            boolean switchesSchema = false;
            for (int i = 0; i < ctx.parameter_name().size(); i++) {
                Parameter_nameContext parameter = ctx.parameter_name(i);
                if ("CURRENT_SCHEMA".equalsIgnoreCase(parameter.getText())) {
                    switchesSchema = true;
                    addUnary(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH,
                        object(TargetType.Schema, ctx.session_parameter_value(i)));
                } else {
                    addUnary(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, principal(TargetType.ConfigKey, parameter));
                }
            }
            // A later parameter must not overwrite the statement's switch classification.
            if (switchesSchema) {
                behavior.setStatementType(SplitQueryType.SWITCH_SCHEMA);
            }
        }
    }

    @Override
    public Void visitSet_role(Set_roleContext ctx) {
        // NONE is also accepted by regular_id, so the grammar may expose it as role_name.
        boolean none = ctx.NONE() != null || ctx.role_name().size() == 1 && "NONE".equalsIgnoreCase(ctx.role_name(0).getText());
        if (ctx.ALL() != null || none) {
            List<BehaviorObject> references = new ArrayList<>();
            if (ctx.EXCEPT() != null) {
                for (Role_nameContext excluded : ctx.role_name()) {
                    references.add(principal(TargetType.Role, excluded));
                }
            }
            // ALL/NONE are unnamed sets; EXCEPT roles are filters, not enabled subjects.
            addRelation(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH,
                objects.instanceObject(TargetType.Role, ctx.ROLE().getSymbol()), references);
        } else {
            for (Role_nameContext role : ctx.role_name()) {
                addUnary(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH, principal(TargetType.Role, role));
            }
        }
        return null;
    }

    @Override
    public Void visitCreate_database(Create_databaseContext ctx) {
        BehaviorObject database = object(TargetType.Catalog, ctx.database_name());
        addRelation(SplitQueryType.CREATE_CATALOG, BehaviorAction.CREATE, database, databaseReferences(ctx, database));
        return null;
    }

    @Override
    public Void visitAlter_database(Alter_databaseContext ctx) {
        BehaviorObject database;
        if (ctx.database_clause().database_name() == null) {
            database = objects.unnamedObject(TargetType.Catalog, ctx.database_clause().DATABASE().getSymbol(), UmiTypes.Catalog);
        } else {
            database = object(TargetType.Catalog, ctx.database_clause().database_name());
        }
        addRelation(SplitQueryType.ALTER_CATALOG, BehaviorAction.ALTER, database, databaseReferences(ctx, database));
        return null;
    }

    @Override
    public Void visitDrop_database(Drop_databaseContext ctx) {
        addUnary(SplitQueryType.DROP_CATALOG, BehaviorAction.DROP, objects.unnamedObject(TargetType.Catalog, ctx.DATABASE().getSymbol(), UmiTypes.Catalog));
        return null;
    }

    @Override
    public Void visitCreate_pluggable_database(Create_pluggable_databaseContext ctx) {
        BehaviorObject database = object(TargetType.Catalog, ctx.database_name(0));
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.FROM() != null) {
            targets.add(object(TargetType.Catalog, ctx.database_name(1)));
        }
        targets.addAll(databaseReferences(ctx, database));
        addRelation(SplitQueryType.CREATE_CATALOG, BehaviorAction.CREATE, database, targets);
        return null;
    }

    @Override
    public Void visitAlter_pluggable_database(Alter_pluggable_databaseContext ctx) {
        if (ctx.ALL() != null) {
            // The affected PDB set requires metadata; EXCEPT names are not affected databases.
            addUnary(SplitQueryType.ALTER_CATALOG, BehaviorAction.ALTER, objects.instanceObject(TargetType.Catalog, ctx.ALL().getSymbol()));
        } else if (ctx.database_name().isEmpty()) {
            BehaviorObject database = objects.unnamedObject(TargetType.Catalog, ctx.DATABASE().getSymbol(), UmiTypes.Catalog);
            addRelation(SplitQueryType.ALTER_CATALOG, BehaviorAction.ALTER, database, databaseReferences(ctx, database));
        } else {
            for (Database_nameContext name : ctx.database_name()) {
                BehaviorObject database = object(TargetType.Catalog, name);
                addRelation(SplitQueryType.ALTER_CATALOG, BehaviorAction.ALTER, database, databaseReferences(ctx, database));
            }
        }
        return null;
    }

    @Override
    public Void visitDrop_pluggable_database(Drop_pluggable_databaseContext ctx) {
        addUnary(SplitQueryType.DROP_CATALOG, BehaviorAction.DROP, object(TargetType.Catalog, ctx.database_name()));
        return null;
    }

    private List<BehaviorObject> databaseReferences(ParserRuleContext ctx, BehaviorObject database) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (TablespaceContext name : descendants(ctx, TablespaceContext.class)) {
            BehaviorObject tablespace = catalogObject(TargetType.Tablespace, name);
            tablespace.setObjectPath(database.getObjectPath() + unquote(name.getText()) + "/");
            targets.add(tablespace);
        }
        for (Edition_nameContext name : descendants(ctx, Edition_nameContext.class)) {
            targets.add(principal(TargetType.Object, name));
        }
        return targets;
    }

    @Override
    public Void visitCreate_tablespace(Create_tablespaceContext ctx) {
        addRelation(SplitQueryType.CREATE_TABLESPACE, BehaviorAction.CREATE,
            catalogObject(TargetType.Tablespace, first(ctx, Id_expressionContext.class)), tablespaceGroups(ctx));
        return null;
    }

    @Override
    public Void visitAlter_tablespace(Alter_tablespaceContext ctx) {
        BehaviorObject tablespace = catalogObject(TargetType.Tablespace, ctx.tablespace());
        if (ctx.new_tablespace_name() != null) {
            addRelation(SplitQueryType.RENAME_TABLESPACE, BehaviorAction.RENAME, tablespace, objects(catalogObject(TargetType.Tablespace, ctx.new_tablespace_name())));
        } else {
            addRelation(SplitQueryType.ALTER_TABLESPACE, BehaviorAction.ALTER, tablespace, tablespaceGroups(ctx));
        }
        return null;
    }

    @Override
    public Void visitDrop_tablespace(Drop_tablespaceContext ctx) {
        addUnary(SplitQueryType.DROP_TABLESPACE, BehaviorAction.DROP, catalogObject(TargetType.Tablespace, ctx.ts));
        return null;
    }

    private BehaviorObject catalogObject(TargetType type, ParserRuleContext name) {
        BehaviorObject result = objects.unnamedObject(type, name, UmiTypes.Catalog);
        result.setObjectPath(result.getObjectPath() + unquote(parser.getTokenStream().getText(name.getStart(), name.getStop())) + "/");
        return result;
    }

    private List<BehaviorObject> tablespaceGroups(ParserRuleContext ctx) {
        List<BehaviorObject> references = new ArrayList<>();
        for (Tablespace_group_clauseContext clause : descendants(ctx, Tablespace_group_clauseContext.class)) {
            // GROUP '' removes membership; the previous group is not named in SQL.
            if (clause.tablespace_group_name() != null) {
                references.add(catalogObject(TargetType.Object, clause.tablespace_group_name()));
            }
        }
        return references;
    }

    @Override
    public Void visitAudit_traditional(Audit_traditionalContext ctx) {
        addAuditConfiguration(ctx);
        return null;
    }

    @Override
    public Void visitNoaudit_statement(Noaudit_statementContext ctx) {
        addAuditConfiguration(ctx);
        return null;
    }

    @Override
    public Void visitUnified_auditing(Unified_auditingContext ctx) {
        addAuditConfiguration(ctx);
        return null;
    }

    @Override
    public Void visitUnified_noauditing(Unified_noauditingContext ctx) {
        addAuditConfiguration(ctx);
        return null;
    }

    private void addAuditConfiguration(ParserRuleContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (Audit_userContext user : descendants(ctx, Audit_userContext.class)) {
            targets.add(principal(TargetType.User, user));
        }
        for (Role_nameContext role : descendants(ctx, Role_nameContext.class)) {
            targets.add(principal(TargetType.Role, role));
        }
        List<BehaviorObject> subjects = new ArrayList<>();
        Policy_nameContext policy = first(ctx, Policy_nameContext.class);
        if (policy != null) {
            subjects.add(catalogObject(TargetType.Policy, policy));
        }
        for (Audit_context_clauseContext context : descendants(ctx, Audit_context_clauseContext.class)) {
            subjects.add(catalogObject(TargetType.Context, context.oracle_namespace()));
        }
        Auditing_on_clauseContext on = first(ctx, Auditing_on_clauseContext.class);
        if (on != null && on.DEFAULT() == null) {
            if (on.DIRECTORY() != null) {
                subjects.add(principal(TargetType.Object, on.regular_id()));
            } else {
                subjects.add(qualifiedPrivilegeObject(TargetType.SchemaObject, on));
            }
        }
        if (subjects.isEmpty()) {
            subjects.add(objects.instanceObject(TargetType.Instance, ctx.getStart()));
        }
        for (BehaviorObject subject : subjects) {
            addRelation(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, subject, targets);
        }
    }

    @Override
    public Void visitCreate_audit_policy(Create_audit_policyContext ctx) {
        addRelation(SplitQueryType.CREATE_POLICY, BehaviorAction.CREATE, catalogObject(TargetType.Policy, ctx.p), auditPolicyTargets(ctx));
        return null;
    }

    @Override
    public Void visitAlter_audit_policy(Alter_audit_policyContext ctx) {
        addRelation(SplitQueryType.ALTER_POLICY, BehaviorAction.ALTER, catalogObject(TargetType.Policy, ctx.p), auditPolicyTargets(ctx));
        return null;
    }

    @Override
    public Void visitDrop_audit_policy(Drop_audit_policyContext ctx) {
        addUnary(SplitQueryType.DROP_POLICY, BehaviorAction.DROP, catalogObject(TargetType.Policy, ctx.p));
        return null;
    }

    private List<BehaviorObject> auditPolicyTargets(ParserRuleContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (Actions_clauseContext action : descendants(ctx, Actions_clauseContext.class)) {
            if (action.DIRECTORY() != null) {
                targets.add(principal(TargetType.Object, action.directory_name()));
            } else if (action.ON() != null) {
                targets.add(qualifiedPrivilegeObject(TargetType.SchemaObject, action));
            }
        }
        for (Role_nameContext role : descendants(ctx, Role_nameContext.class)) {
            targets.add(principal(TargetType.Role, role));
        }
        return targets;
    }

    @Override
    public Void visitAnalyze(AnalyzeContext ctx) {
        SplitQueryType type = SplitQueryType.ADMIN;
        if (ctx.TABLE() != null) {
            type = SplitQueryType.ADMIN_TABLE;
        }
        BehaviorAction action = BehaviorAction.VALIDATE;
        if (ctx.DELETE() != null || ctx.compute_clauses() != null || ctx.ESTIMATE() != null) {
            type = SplitQueryType.ADMIN_PERFORMANCE;
            action = BehaviorAction.ANALYZE;
            if (ctx.DELETE() != null) {
                action = BehaviorAction.RESET;
            }
        } else if (ctx.LIST() != null) {
            action = BehaviorAction.READ;
        } else if (ctx.validation_clauses() != null && ctx.validation_clauses().REF() != null) {
            action = BehaviorAction.REPAIR;
        }
        BehaviorObject subject;
        if (ctx.TABLE() != null) {
            Tableview_nameContext table = ctx.tableview_name();
            List<String> names = new ArrayList<>();
            collectNames(table.identifier(), names);
            ParserRuleContext end = table.identifier();
            if (table.id_expression() != null) {
                collectNames(table.id_expression(), names);
                end = table.id_expression();
            }
            subject = objects.object(TargetType.Table, table.getStart(), end.getStop(), names);
        } else if (ctx.INDEX() != null) {
            subject = object(TargetType.Index, ctx.index_name());
        } else {
            subject = object(TargetType.SchemaObject, ctx.cluster_name());
        }
        List<BehaviorObject> references = analyzeReferences(ctx, subject);
        addRelation(type, action, subject, references);
        Into_clause1Context into = first(ctx, Into_clause1Context.class);
        if (into != null) {
            addRelation(type, BehaviorAction.INSERT, object(TargetType.Table, into.tableview_name()), objects(subject));
        }
        return null;
    }

    private List<BehaviorObject> analyzeReferences(AnalyzeContext ctx, BehaviorObject subject) {
        List<BehaviorObject> result = new ArrayList<>();
        List<ParserRuleContext> names = new ArrayList<>();
        Partition_extention_clauseContext partition = ctx.partition_extention_clause();
        if (partition != null) {
            if (partition.partition_name() != null) {
                names.add(partition.partition_name());
            } else if (partition.subpartition_name() != null) {
                names.add(partition.subpartition_name());
            }
        }
        // Table grammar may consume the partition selector inside tableview_name.
        if (ctx.tableview_name() != null && ctx.tableview_name().partition_extension_clause() != null) {
            Partition_extension_clauseContext extension = ctx.tableview_name().partition_extension_clause();
            if (extension.FOR() == null) {
                Id_expressionContext name = first(extension.expressions_(), Id_expressionContext.class);
                if (name != null) {
                    names.add(name);
                }
            }
        }
        names.addAll(descendants(ctx.compute_clauses(), Column_nameContext.class));
        for (ParserRuleContext name : names) {
            TargetType type = TargetType.Partition;
            if (name instanceof Column_nameContext) {
                type = TargetType.Column;
            }
            BehaviorObject reference = objects.object(type, name, List.of(unquote(name.getText())));
            reference.setObjectPath(subject.getObjectPath() + unquote(name.getText()) + "/");
            result.add(reference);
        }
        return result;
    }

    @Override
    public Void visitPurge_statement(Purge_statementContext ctx) {
        BehaviorObject subject;
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.TABLE() != null) {
            subject = object(TargetType.Table, ctx.id_expression(0));
        } else if (ctx.INDEX() != null) {
            subject = object(TargetType.Index, ctx.id_expression(0));
        } else if (ctx.TABLESPACE() != null) {
            TargetType type = TargetType.Tablespace;
            if (ctx.SET() != null) {
                type = TargetType.Object;
            }
            subject = catalogObject(type, ctx.ts);
            if (ctx.u != null) {
                targets.add(principal(TargetType.User, ctx.u));
            }
        } else if (ctx.DBA_RECYCLEBIN() != null) {
            subject = objects.unnamedObject(TargetType.Catalog, ctx.DBA_RECYCLEBIN().getSymbol(), UmiTypes.Catalog);
        } else {
            // CURRENT_SCHEMA may differ from the login user; levels do not identify that user.
            subject = objects.instanceObject(TargetType.User, ctx.RECYCLEBIN().getSymbol());
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.PURGE, subject, targets);
        return null;
    }

    @Override
    public Void visitCreate_user(Create_userContext ctx) {
        addRelation(SplitQueryType.CREATE_USER, BehaviorAction.CREATE, principal(TargetType.User, ctx.user_object_name()), userReferences(ctx));
        return null;
    }

    @Override
    public Void visitAlter_user(Alter_userContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.proxy_clause() != null && ctx.proxy_clause().user_object_name() != null) {
            targets.add(principal(TargetType.User, ctx.proxy_clause().user_object_name()));
        }
        // These are references in an ALTER, including roles named by ALL EXCEPT.
        // They do not imply that any role membership has been granted.
        for (Role_nameContext role : descendants(ctx, Role_nameContext.class)) {
            targets.add(principal(TargetType.Role, role));
        }
        targets.addAll(userReferences(ctx));
        for (User_object_nameContext user : ctx.user_object_name()) {
            addRelation(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, principal(TargetType.User, user), targets);
        }
        return null;
    }

    @Override
    public Void visitDrop_user(Drop_userContext ctx) {
        addUnary(SplitQueryType.DROP_USER, BehaviorAction.DROP, principal(TargetType.User, ctx.user_object_name()));
        return null;
    }

    @Override
    public Void visitCreate_role(Create_roleContext ctx) {
        addRelation(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE, principal(TargetType.Role, ctx.role_name()), roleAuthentication(ctx.role_identified_clause()));
        return null;
    }

    @Override
    public Void visitAlter_role(Alter_roleContext ctx) {
        addRelation(SplitQueryType.ALTER_ROLE, BehaviorAction.ALTER, principal(TargetType.Role, ctx.role_name()), roleAuthentication(ctx.role_identified_clause()));
        return null;
    }

    private List<BehaviorObject> userReferences(ParserRuleContext ctx) {
        List<BehaviorObject> references = new ArrayList<>();
        for (ParseTree child : ctx.children) {
            if (child instanceof User_tablespace_clauseContext clause) {
                Id_expressionContext name = clause.id_expression();
                if ("CDB$DEFAULT".equalsIgnoreCase(name.getText())) {
                    references.add(objects.unnamedObject(TargetType.Object, name, UmiTypes.Catalog));
                } else {
                    TargetType type = TargetType.Tablespace;
                    // Shared temporary storage can name a tablespace or a group.
                    if (clause.TEMPORARY() != null && clause.LOCAL() == null) {
                        type = TargetType.Object;
                    }
                    references.add(catalogObject(type, name));
                }
            } else if (child instanceof Quota_clauseContext clause) {
                references.add(catalogObject(TargetType.Tablespace, clause.id_expression()));
            } else if (child instanceof Profile_clauseContext clause) {
                if (clause.id_expression() != null) {
                    references.add(catalogObject(TargetType.Profile, clause.id_expression()));
                } else {
                    BehaviorObject profile = objects.unnamedObject(TargetType.Profile, clause.DEFAULT().getSymbol(), UmiTypes.Catalog);
                    profile.setObjectPath(profile.getObjectPath() + "DEFAULT/");
                    references.add(profile);
                }
            } else if (child instanceof Container_data_clauseContext clause) {
                for (Container_namesContext containers : descendants(clause, Container_namesContext.class)) {
                    for (Id_expressionContext name : containers.id_expression()) {
                        references.add(object(TargetType.Catalog, name));
                    }
                }
                if (clause.container_tableview_name() != null) {
                    references.add(object(TargetType.SchemaObject, clause.container_tableview_name()));
                }
            }
        }
        return references;
    }

    private List<BehaviorObject> roleAuthentication(Role_identified_clauseContext ctx) {
        if (ctx == null || ctx.USING() == null) {
            return List.of();
        }
        List<String> names = new ArrayList<>();
        collectNames(ctx, names);
        return objects(objects.object(TargetType.Package, ctx.identifier().start, ctx.stop, names));
    }

    @Override
    public Void visitDrop_role(Drop_roleContext ctx) {
        addUnary(SplitQueryType.DROP_ROLE, BehaviorAction.DROP, principal(TargetType.Role, ctx.role_name()));
        return null;
    }

    @Override
    public Void visitGrant_statement(Grant_statementContext ctx) {
        if (ctx.grant_system_privileges() != null) {
            Grant_system_privilegesContext grant = ctx.grant_system_privileges();
            ParserRuleContext grantees = grant.grant_grantee_clause();
            if (grantees == null) {
                grantees = grant;
            }
            addPrivilegeRelations(SplitQueryType.GRANT, BehaviorAction.GRANT, grant, grantees(grantees));
        } else if (ctx.grant_object_privileges() != null) {
            addObjectGrant(ctx.grant_object_privileges());
        } else {
            addPrivilegeRelations(SplitQueryType.GRANT, BehaviorAction.GRANT, ctx, programTargets(ctx.program_unit()));
        }
        return null;
    }

    @Override
    public Void visitRevoke_statement(Revoke_statementContext ctx) {
        if (ctx.revoke_system_privilege() != null) {
            Revoke_system_privilegeContext revoke = ctx.revoke_system_privilege();
            addPrivilegeRelations(SplitQueryType.REVOKE, BehaviorAction.REVOKE, revoke, grantees(revoke.revokee_clause()));
        } else if (ctx.revoke_object_privileges() != null) {
            Revoke_object_privilegesContext revoke = ctx.revoke_object_privileges();
            List<BehaviorObject> targets = grantees(revoke.revokee_clause());
            for (BehaviorObject subject : privilegeObjects(revoke.on_object_clause())) {
                addRelation(SplitQueryType.REVOKE, BehaviorAction.REVOKE, subject, targets);
            }
        } else {
            Revoke_roles_from_programsContext revoke = ctx.revoke_roles_from_programs();
            List<BehaviorObject> targets = programTargets(revoke.program_unit());
            if (revoke.ALL() != null) {
                addRelation(SplitQueryType.REVOKE, BehaviorAction.REVOKE, objects.instanceObject(TargetType.Role, revoke.ALL().getSymbol()), targets);
            } else {
                addPrivilegeRelations(SplitQueryType.REVOKE, BehaviorAction.REVOKE, revoke, targets);
            }
        }
        return null;
    }

    private BehaviorObject principal(TargetType type, ParserRuleContext name) {
        return objects.instanceObject(type, name, unquote(parser.getTokenStream().getText(name.getStart(), name.getStop())));
    }

    private List<BehaviorObject> grantees(ParserRuleContext ctx) {
        List<BehaviorObject> result = new ArrayList<>();
        for (ParseTree child : ctx.children) {
            if (child instanceof Grantee_nameContext || child instanceof Id_expressionContext && ctx instanceof Revokee_clauseContext) {
                result.add(principal(TargetType.UserOrRole, (ParserRuleContext) child));
            } else if (child instanceof TerminalNode node && node.getSymbol().getType() == PlSqlParser.PUBLIC) {
                result.add(objects.instanceObject(TargetType.UserOrRole, node.getSymbol(), node.getText()));
            }
        }
        return result;
    }

    private void addPrivilegeRelations(SplitQueryType type, BehaviorAction action, ParserRuleContext ctx, List<BehaviorObject> targets) {
        for (ParseTree child : ctx.children) {
            if (child instanceof System_privilegeContext privilege) {
                addRelation(type, action, objects.instanceObject(TargetType.Instance, privilege), targets);
            } else if (child instanceof Role_nameContext role) {
                addRelation(type, action, principal(TargetType.Role, role), targets);
            }
        }
    }

    private List<BehaviorObject> programTargets(List<Program_unitContext> programs) {
        List<BehaviorObject> result = new ArrayList<>();
        for (Program_unitContext program : programs) {
            TargetType type = TargetType.Package;
            if (program.FUNCTION() != null) {
                type = TargetType.Function;
            } else if (program.PROCEDURE() != null) {
                type = TargetType.Procedure;
            }
            result.add(qualifiedPrivilegeObject(type, program));
        }
        return result;
    }

    private List<BehaviorObject> privilegeObjects(ParserRuleContext ctx) {
        List<BehaviorObject> result = new ArrayList<>();
        if (ctx.getToken(PlSqlParser.USER, 0) != null) {
            for (Id_expressionContext user : descendants(ctx, Id_expressionContext.class)) {
                result.add(principal(TargetType.User, user));
            }
        } else if (ctx.getToken(PlSqlParser.DIRECTORY, 0) != null || ctx.getToken(PlSqlParser.EDITION, 0) != null) {
            // Directories and editions are not schema-qualified; the SDK has no dedicated types.
            ParserRuleContext name = (ParserRuleContext) ctx.getChild(ctx.getChildCount() - 1);
            result.add(principal(TargetType.Object, name));
        } else {
            // ON name alone cannot distinguish tables, views, sequences, programs or types.
            TargetType type = TargetType.SchemaObject;
            if (ctx.getToken(PlSqlParser.PROFILE, 0) != null) {
                type = TargetType.Profile;
            }
            result.add(qualifiedPrivilegeObject(type, ctx));
        }
        return result;
    }

    private BehaviorObject qualifiedPrivilegeObject(TargetType type, ParserRuleContext ctx) {
        List<Id_expressionContext> identifiers = descendants(ctx, Id_expressionContext.class);
        List<String> names = new ArrayList<>();
        for (Id_expressionContext identifier : identifiers) {
            names.add(unquote(parser.getTokenStream().getText(identifier.getStart(), identifier.getStop())));
        }
        return objects.object(type, identifiers.get(0).getStart(), identifiers.get(identifiers.size() - 1).getStop(), names);
    }

    private void addObjectGrant(Grant_object_privilegesContext ctx) {
        List<BehaviorObject> targets = grantees(ctx.grant_grantee_clause());
        List<BehaviorObject> subjects = privilegeObjects(ctx.grant_object_name());
        boolean objectPrivilege = false;
        for (int i = 0; i < ctx.getChildCount(); i++) {
            if (ctx.getChild(i) instanceof Object_privilegeContext && !(ctx.getChild(i + 1) instanceof Paren_column_listContext)) {
                objectPrivilege = true;
            }
        }
        if (objectPrivilege) {
            for (BehaviorObject subject : subjects) {
                addRelation(SplitQueryType.GRANT, BehaviorAction.GRANT, subject, targets);
            }
        }
        for (Paren_column_listContext columns : ctx.paren_column_list()) {
            for (Column_nameContext column : columns.column_list().column_name()) {
                BehaviorObject subject = object(TargetType.Column, column);
                String name = unquote(parser.getTokenStream().getText(column.getStart(), column.getStop()));
                subject.setObjectPath(subjects.get(0).getObjectPath() + name + "/");
                addRelation(SplitQueryType.GRANT, BehaviorAction.GRANT, subject, targets);
            }
        }
    }

    @Override
    public Void visitExplain_statement(Explain_statementContext ctx) {
        Tableview_nameContext createdTable = null;
        if (ctx.create_table() != null) {
            createdTable = ctx.create_table().tableview_name(0);
        }
        for (Tableview_nameContext table : descendants(ctx, Tableview_nameContext.class)) {
            if (table.identifier() != null && table != ctx.tableview_name() && table != createdTable && !isExplainCteReference(table)) {
                addUnary(SplitQueryType.PERFORMANCE, BehaviorAction.READ, explainTableObject(table));
            }
        }
        if (ctx.index_name() != null) {
            BehaviorObject index = object(TargetType.Index, ctx.index_name());
            List<BehaviorObject> targets = new ArrayList<>();
            ParserRuleContext partition = ctx.rebuild_clause().partition_name();
            if (partition == null) {
                partition = ctx.rebuild_clause().subpartition_name();
            }
            if (partition != null) {
                BehaviorObject reference = objects.object(TargetType.Partition, partition, List.of(unquote(partition.getText())));
                reference.setObjectPath(index.getObjectPath() + unquote(partition.getText()) + "/");
                targets.add(reference);
            }
            addRelation(SplitQueryType.PERFORMANCE, BehaviorAction.READ, index, targets);
        }
        BehaviorObject output;
        if (ctx.tableview_name() != null) {
            output = explainTableObject(ctx.tableview_name());
        } else {
            // Oracle defines PLAN_TABLE as the implicit output; anchor it at the PLAN keyword.
            output = objects.object(TargetType.Table, ctx.PLAN().getSymbol(), List.of("PLAN_TABLE"));
        }
        addUnary(SplitQueryType.PERFORMANCE, BehaviorAction.INSERT, output);
        return null;
    }

    private BehaviorObject explainTableObject(Tableview_nameContext ctx) {
        List<String> names = new ArrayList<>();
        collectNames(ctx.identifier(), names);
        ParserRuleContext end = ctx.identifier();
        if (ctx.id_expression() != null) {
            collectNames(ctx.id_expression(), names);
            end = ctx.id_expression();
        }
        if (ctx.link_name() != null) {
            // Keep the link in the symbolic locator; do not report a remote table as local.
            names.set(names.size() - 1, names.get(names.size() - 1) + "@" + ctx.link_name().getText());
            end = ctx.link_name();
        } else if (names.size() == 1 && StringUtils.equalsIgnoreCase("DUAL", names.get(0))) {
            return objects.instanceObject(TargetType.Table, ctx.identifier(), names.get(0));
        }
        return objects.object(TargetType.Table, ctx.getStart(), end.getStop(), names);
    }

    private boolean isExplainCteReference(Tableview_nameContext table) {
        if (table.id_expression() != null || table.link_name() != null) {
            return false;
        }
        String reference = oracleIdentifierKey(table.identifier().getText());
        Subquery_factoring_clauseContext containingDefinition = null;
        for (ParseTree parent = table.getParent(); parent != null; parent = parent.getParent()) {
            // A DML destination is a physical object even if a query uses a CTE with that name.
            if (parent instanceof General_table_refContext) {
                return false;
            }
            if (parent instanceof Subquery_factoring_clauseContext definition) {
                containingDefinition = definition;
            }
            With_clauseContext with = null;
            if (parent instanceof Query_blockContext block) {
                with = block.with_clause();
            } else if (parent instanceof Select_only_statementContext select) {
                with = select.with_clause();
            }
            if (with != null) {
                for (With_factoring_clauseContext clause : with.with_factoring_clause()) {
                    Subquery_factoring_clauseContext definition = clause.subquery_factoring_clause();
                    if (definition != null) {
                        if (reference.equals(oracleIdentifierKey(definition.query_name().getText()))) {
                            return true;
                        }
                        if (definition == containingDefinition) {
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    private String oracleIdentifierKey(String name) {
        if (name.startsWith("\"")) {
            return unquote(name);
        }
        return name.toUpperCase(Locale.ROOT);
    }

    @Override
    public Void visitDml_table_expression_clause(Dml_table_expression_clauseContext ctx) {
        if (ctx.tableview_name() != null) {
            addUnary(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.tableview_name()));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitTable_collection_expression(Table_collection_expressionContext ctx) {
        ExpressionContext expression = ctx.expression();
        if (expression == null) {
            return visitChildren(ctx);
        }
        Function_callContext call = first(expression, Function_callContext.class);
        ParserRuleContext name = first(expression, ColumnrefContext.class);
        List<ArgumentContext> arguments = List.of();
        if (call != null && call.start == expression.start && call.stop == expression.stop) {
            name = call.routine_name();
            if (!call.function_argument().isEmpty()) {
                arguments = call.function_argument(0).argument();
            }
        } else if (name == null || name.start != expression.start || name.stop != expression.stop) {
            return visitChildren(ctx);
        }
        List<String> names = new ArrayList<>();
        if (name != null) {
            // Preserve quoted-case resolution before normalizing built-in names.
            for (Id_expressionContext part : descendants(name, Id_expressionContext.class)) {
                names.add(oracleIdentifierKey(part.getText()));
            }
        }
        if (names.size() == 3 && "SYS".equals(names.get(0))) {
            names.remove(0);
        }
        if (names.size() != 2 || !"DBMS_XPLAN".equals(names.get(0))
                || !("DISPLAY".equals(names.get(1)) || "DISPLAY_CURSOR".equals(names.get(1)))) {
            return visitChildren(ctx);
        }
        addUnary(SplitQueryType.SELECT, BehaviorAction.CALL,
            objects.object(TargetType.Function, name, List.of("SYS", String.join(".", names))));
        if ("DISPLAY".equals(names.get(1))) {
            displayPlanTable(name, arguments);
        } else {
            addUnary(SplitQueryType.SELECT, BehaviorAction.READ, objects.instanceObject(TargetType.Query, name));
        }
        return visitChildren(ctx);
    }

    private void displayPlanTable(ParserRuleContext function, List<ArgumentContext> arguments) {
        ExpressionContext table = null;
        for (int i = 0; i < arguments.size(); i++) {
            ArgumentContext argument = arguments.get(i);
            if (argument.identifier() == null && i == 0
                    || argument.identifier() != null && "TABLE_NAME".equals(oracleIdentifierKey(argument.identifier().getText()))) {
                table = argument.expression();
            }
        }
        if (table == null || "NULL".equalsIgnoreCase(table.getText()) || "''".equals(table.getText())) {
            ParserRuleContext anchor = table == null ? function : table;
            addUnary(SplitQueryType.SELECT, BehaviorAction.READ, objects.object(TargetType.Table, anchor, List.of("PLAN_TABLE")));
            return;
        }
        Quoted_stringContext literal = first(table, Quoted_stringContext.class);
        if (literal != null && literal.start == table.start && literal.stop == table.stop) {
            String value = literal.getText();
            if (value.startsWith("'") && value.endsWith("'")) {
                value = value.substring(1, value.length() - 1).replace("''", "'");
            } else if (value.length() >= 5 && (value.startsWith("q'") || value.startsWith("Q'"))) {
                value = value.substring(3, value.length() - 2);
            } else {
                value = "";
            }
            String identifier = "(?:\"[^\"]+\"|[\\p{L}_][\\p{L}\\p{N}_$#]*)";
            if (value.matches(identifier + "(?:\\." + identifier + ")?")) {
                List<String> names = new ArrayList<>();
                var matcher = java.util.regex.Pattern.compile(identifier).matcher(value);
                while (matcher.find()) {
                    names.add(oracleIdentifierKey(matcher.group()));
                }
                addUnary(SplitQueryType.SELECT, BehaviorAction.READ, objects.object(TargetType.Table, table, names));
                return;
            }
        }
        // Dynamic or unsupported locators remain unresolved rather than becoming fake tables.
        addUnary(SplitQueryType.SELECT, BehaviorAction.READ, objects.unnamedObject(TargetType.Table, table, UmiTypes.Schema));
    }

    @Override
    public Void visitCreate_table(Create_tableContext ctx) {
        if (!ctx.tableview_name().isEmpty()) {
            addRelation(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.tableview_name(0)), sourceTables(ctx, Set.of()));
        }
        return null;
    }

    @Override
    public Void visitCreate_view(Create_viewContext ctx) {
        addRelation(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.tableview_name()), sourceTables(ctx.select_only_statement(), Set.of()));
        return null;
    }

    @Override
    public Void visitCreate_materialized_view(Create_materialized_viewContext ctx) {
        addRelation(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.Materialized, ctx.tableview_name()), sourceTables(ctx, Set.of()));
        return null;
    }

    @Override
    public Void visitCreate_index(Create_indexContext ctx) {
        addRelation(SplitQueryType.ADD_INDEX, BehaviorAction.CREATE, object(TargetType.Index, ctx.index_name()), objects(object(TargetType.Table, ctx.tableview_name())));
        return null;
    }

    @Override
    public Void visitCreate_procedure_body(Create_procedure_bodyContext ctx) {
        addUnary(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Procedure, ctx.procedure_name()));
        return null;
    }

    @Override
    public Void visitCreate_function_body(Create_function_bodyContext ctx) {
        addUnary(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Function, ctx.function_name()));
        return null;
    }

    @Override
    public Void visitCreate_trigger(Create_triggerContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (Tableview_nameContext table : descendants(ctx, Tableview_nameContext.class)) {
            addObject(targets, object(TargetType.Table, table));
        }
        addRelation(SplitQueryType.CREATE_TRIGGER, BehaviorAction.CREATE, object(TargetType.Trigger, ctx.trigger_name()), targets);
        return null;
    }

    @Override
    public Void visitCreate_sequence(Create_sequenceContext ctx) {
        addUnary(SplitQueryType.CREATE_SEQUENCE, BehaviorAction.CREATE, object(TargetType.Sequence, ctx.sequence_name()));
        return null;
    }

    @Override
    public Void visitCreate_synonym(Create_synonymContext ctx) {
        addUnary(SplitQueryType.CREATE_SYNONYM, BehaviorAction.CREATE, object(TargetType.Synonym, ctx.synonym_name()));
        return null;
    }

    @Override
    public Void visitDrop_table(Drop_tableContext ctx) {
        addUnary(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, ctx.tableview_name()));
        return null;
    }

    @Override
    public Void visitDrop_function(Drop_functionContext ctx) {
        addUnary(SplitQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(TargetType.Function, ctx.function_name()));
        return null;
    }

    @Override
    public Void visitDrop_procedure(Drop_procedureContext ctx) {
        addUnary(SplitQueryType.DROP_PROG_OBJ, BehaviorAction.DROP, object(TargetType.Procedure, ctx.procedure_name()));
        return null;
    }

    @Override
    public Void visitDrop_sequence(Drop_sequenceContext ctx) {
        addUnary(SplitQueryType.DROP_SEQUENCE, BehaviorAction.DROP, object(TargetType.Sequence, ctx.sequence_name()));
        return null;
    }

    @Override
    public Void visitDrop_trigger(Drop_triggerContext ctx) {
        addUnary(SplitQueryType.DROP_TRIGGER, BehaviorAction.DROP, object(TargetType.Trigger, ctx.trigger_name()));
        return null;
    }

    @Override
    public Void visitAlter_table(Alter_tableContext ctx) {
        addUnary(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.tableview_name()));
        return null;
    }

    @Override
    public Void visitRename_object(Rename_objectContext ctx) {
        List<Object_nameContext> names = ctx.object_name();
        if (names.size() >= 2) {
            BehaviorObject source = object(TargetType.Table, names.get(0));
            BehaviorObject target = object(TargetType.Table, names.get(1));
            moveToSameContainer(source, target);
            addRelation(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, source, objects(target));
        }
        return null;
    }

    @Override
    public Void visitTruncate_table(Truncate_tableContext ctx) {
        addUnary(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.tableview_name()));
        return null;
    }

    @Override
    public Void visitCall_statement(Call_statementContext ctx) {
        if (!ctx.routine_name().isEmpty()) {
            addUnary(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, ctx.routine_name(0)));
        }
        return null;
    }

    @Override
    public Void visitInsert_statement(Insert_statementContext ctx) {
        List<Insert_into_clauseContext> inserts = descendants(ctx, Insert_into_clauseContext.class);
        Set<Dml_table_expression_clauseContext> insertTargets = new LinkedHashSet<>();
        for (Insert_into_clauseContext insert : inserts) {
            Dml_table_expression_clauseContext target = first(insert.general_table_ref(), Dml_table_expression_clauseContext.class);
            if (target != null) {
                insertTargets.add(target);
            }
        }
        List<BehaviorObject> sources = sourceTables(ctx, insertTargets);
        List<Values_clauseContext> values = descendants(ctx, Values_clauseContext.class);
        Long insertRows = values.size() == 1 && !values.get(0).expressions_().isEmpty() ? (long) values.get(0).expressions_().size() : null;
        for (Dml_table_expression_clauseContext target : insertTargets) {
            BehaviorRelation relation = addRelation(SplitQueryType.INSERT, BehaviorAction.INSERT, object(TargetType.Table, target.tableview_name()), sources);
            if (relation != null && insertRows != null) {
                relation.setInsertRows(insertRows);
            }
        }
        return null;
    }

    @Override
    public Void visitUpdate_statement(Update_statementContext ctx) {
        Dml_table_expression_clauseContext target = first(ctx.general_table_ref(), Dml_table_expression_clauseContext.class);
        addRelation(SplitQueryType.UPDATE, BehaviorAction.UPDATE, target == null ? null : object(TargetType.Table, target.tableview_name()), sourceTables(ctx, target == null ? Set
            .of() : Set.of(target)));
        return null;
    }

    @Override
    public Void visitDelete_statement(Delete_statementContext ctx) {
        Dml_table_expression_clauseContext target = first(ctx.general_table_ref(), Dml_table_expression_clauseContext.class);
        addRelation(SplitQueryType.DELETE, BehaviorAction.DELETE, target == null ? null : object(TargetType.Table, target.tableview_name()), sourceTables(ctx, target == null ? Set
            .of() : Set.of(target)));
        return null;
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        collectNames(context, names);
        if (type == TargetType.Table && names.size() == 1 && StringUtils.equalsIgnoreCase("DUAL", names.get(0))) {
            return objects.instanceObject(type, context, names.get(0));
        }
        return objects.object(type, context, names);
    }

    private void collectNames(ParseTree tree, List<String> names) {
        if (tree instanceof IdentifierContext || tree instanceof Id_expressionContext) {
            ParserRuleContext context = (ParserRuleContext) tree;
            names.add(unquote(parser.getTokenStream().getText(context.getStart(), context.getStop())));
            return;
        }
        if (tree instanceof Link_nameContext) {
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectNames(tree.getChild(i), names);
        }
    }

    private void addUnary(SplitQueryType type, BehaviorAction action, BehaviorObject subject) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        behavior.getRelations().add(relation);
        behavior.setStatementType(type);
    }

    private BehaviorRelation addRelation(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return null;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        for (BehaviorObject target : targets) {
            if (target != null) {
                relation.getTarget().add(target);
            }
        }
        behavior.getRelations().add(relation);
        behavior.setStatementType(type);
        return relation;
    }

    private List<BehaviorObject> sourceTables(ParseTree tree, Set<Dml_table_expression_clauseContext> excluded) {
        List<BehaviorObject> result = new ArrayList<>();
        for (Dml_table_expression_clauseContext source : descendants(tree, Dml_table_expression_clauseContext.class)) {
            if (!excluded.contains(source) && source.tableview_name() != null) {
                addObject(result, object(TargetType.Table, source.tableview_name()));
            }
        }
        return result;
    }

    private List<BehaviorObject> objects(BehaviorObject... values) {
        List<BehaviorObject> result = new ArrayList<>();
        for (BehaviorObject value : values) {
            addObject(result, value);
        }
        return result;
    }

    private void addObject(List<BehaviorObject> target, BehaviorObject value) {
        if (value != null) {
            target.add(value);
        }
    }

    private <T extends ParserRuleContext> T first(ParseTree tree, Class<T> type) {
        List<T> result = descendants(tree, type);
        return result.isEmpty() ? null : result.get(0);
    }

    private void moveToSameContainer(BehaviorObject source, BehaviorObject target) {
        if (source == null || target == null) {
            return;
        }
        String sourcePath = source.getObjectPath();
        String targetPath = target.getObjectPath();
        int sourceNameStart = sourcePath.lastIndexOf('/', sourcePath.length() - 2);
        int targetNameStart = targetPath.lastIndexOf('/', targetPath.length() - 2);
        if (sourceNameStart >= 0 && targetNameStart >= 0) {
            target.setObjectPath(sourcePath.substring(0, sourceNameStart + 1) + targetPath.substring(targetNameStart + 1));
            moveObjectNameToSameContainer(source, target);
        }
    }

    private void moveObjectNameToSameContainer(BehaviorObject source, BehaviorObject target) {
        ObjectName sourceName = source.getObjectName();
        ObjectName targetName = target.getObjectName();
        if (sourceName == null || targetName == null) {
            return;
        }
        target.setObjectName(new ObjectName(sourceName.getCatalog(), sourceName.getSchema(), targetName.getObjectName()));
    }

    private <T extends ParserRuleContext> List<T> descendants(ParseTree tree, Class<T> type) {
        List<T> result = new ArrayList<>();
        collectDescendants(tree, type, result);
        return result;
    }

    private <T extends ParserRuleContext> void collectDescendants(ParseTree tree, Class<T> type, List<T> result) {
        if (tree == null) {
            return;
        }
        if (type.isInstance(tree)) {
            result.add(type.cast(tree));
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectDescendants(tree.getChild(i), type, result);
        }
    }

    private String unquote(String value) {
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
