/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.sqlserver.analysis.behavior;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Locale;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerLexer;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerParser;
import com.clougence.sql.sqlserver.parser.antlr.SqlServerParserBaseVisitor;

final class MsStatementBehaviorVisitor extends SqlServerParserBaseVisitor<Void> {

    private final Parser                   parser;
    private final Map<UmiTypes, Object>     levels;
    private final int                      baseLine;
    private final RdbBehaviorObjectFactory objects;
    private final StatementBehavior        behavior = new StatementBehavior();

    MsStatementBehaviorVisitor(Parser parser, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.levels = levels;
        this.baseLine = Math.max(1, baseLine);
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.behavior.setStatementType(SplitQueryType.UNKNOWN);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    // Walk every node once even when a statement handler only records its own relation.
    void analyze(ParseTree tree) {
        tree.accept(this);
        if (tree instanceof SqlServerParser.Function_callContext function) {
            functionCall(function);
        }
        if (tree instanceof TerminalNode terminal
            && terminal.getSymbol().getType() == SqlServerParser.LOCAL_ID
            && terminal.getText().startsWith("@@")) {
            addUnary(SplitQueryType.SELECT, BehaviorAction.READ,
                instanceNamed(TargetType.ConfigKey, terminal.getSymbol(), terminal.getText().toUpperCase(Locale.ROOT)));
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            analyze(tree.getChild(i));
        }
    }

    @Override
    public Void visitChildren(RuleNode node) {
        return null;
    }

    private void setType(SplitQueryType type) {
        if (behavior.getStatementType() == SplitQueryType.UNKNOWN) {
            behavior.setStatementType(type);
        }
    }

    @Override
    public Void visitUse_statement(SqlServerParser.Use_statementContext ctx) {
        addUnary(SplitQueryType.SWITCH_CATALOG, BehaviorAction.SWITCH, object(TargetType.Catalog, ctx.database));
        return null;
    }

    @Override
    public Void visitSet_statement(SqlServerParser.Set_statementContext ctx) {
        if (ctx.set_special() == null) {
            setType(SplitQueryType.SESSION_VARIABLE_RW);
        } else {
            setType(SplitQueryType.SESSION_SETTING_WRITE);
        }
        return null;
    }

    @Override
    public Void visitSet_special(SqlServerParser.Set_specialContext ctx) {
        setType(SplitQueryType.SESSION_SETTING_WRITE);
        if (!ctx.special_list().isEmpty()) {
            for (SqlServerParser.Special_listContext option : ctx.special_list()) {
                addUnary(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE,
                    instanceNamed(TargetType.ConfigKey, option.getStart(), option.getText().toUpperCase(Locale.ROOT)));
            }
            return null;
        }
        if (ctx.STATISTICS() != null) {
            Token start = ctx.STATISTICS().getSymbol();
            for (ParseTree child : ctx.children) {
                if (child instanceof TerminalNode terminal) {
                    Token option = terminal.getSymbol();
                    switch (option.getType()) {
                        case SqlServerParser.IO, SqlServerParser.TIME, SqlServerParser.XML, SqlServerParser.PROFILE -> {
                            String name = "STATISTICS " + option.getText().toUpperCase(Locale.ROOT);
                            BehaviorObject config = objects.instanceObject(TargetType.ConfigKey, start, option, name);
                            config.setObjectName(new ObjectName(null, null, name));
                            addUnary(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, config);
                        }
                        default -> { }
                    }
                }
            }
            return null;
        }
        ParseTree key = ctx.getChild(1);
        Token start = ctx.getStart();
        if (key instanceof ParserRuleContext context) {
            start = context.getStart();
        } else if (key instanceof TerminalNode terminal) {
            start = terminal.getSymbol();
        }
        BehaviorObject config = instanceNamed(TargetType.ConfigKey, start, start.getText().toUpperCase(Locale.ROOT));
        if (ctx.ISOLATION() != null) {
            config = objects.instanceObject(TargetType.ConfigKey, start, ctx.LEVEL().getSymbol(), "TRANSACTION ISOLATION LEVEL");
            config.setObjectName(new ObjectName(null, null, "TRANSACTION ISOLATION LEVEL"));
        }
        addRelation(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, config,
            objects(object(TargetType.Table, ctx.table_name())));
        return null;
    }

    @Override
    public Void visitTransaction_statement(SqlServerParser.Transaction_statementContext ctx) {
        setType(SplitQueryType.TRANSACTION);
        return null;
    }

    @Override
    public Void visitDeclare_statement(SqlServerParser.Declare_statementContext ctx) {
        setType(SplitQueryType.PROGRAM_CONTROL);
        return null;
    }

    @Override
    public Void visitBlock_statement(SqlServerParser.Block_statementContext ctx) {
        setType(SplitQueryType.BLOCK);
        return null;
    }

    @Override
    public Void visitIf_statement(SqlServerParser.If_statementContext ctx) {
        setType(SplitQueryType.PROGRAM_CONTROL);
        return null;
    }

    @Override
    public Void visitRevert_statement(SqlServerParser.Revert_statementContext ctx) {
        addUnary(SplitQueryType.SWITCH_USER, BehaviorAction.SWITCH, identity(ctx.getStart(), false, null));
        return null;
    }

    @Override
    public Void visitSetuser_statement(SqlServerParser.Setuser_statementContext ctx) {
        Token token = ctx.user;
        if (token == null) {
            token = ctx.getStart();
        }
        String name = null;
        if (ctx.user != null) {
            name = unquote(ctx.user.getText());
        }
        addUnary(SplitQueryType.SWITCH_USER, BehaviorAction.SWITCH, identity(token, true, name));
        return null;
    }

    @Override
    public Void visitSelect_statement_standalone(SqlServerParser.Select_statement_standaloneContext ctx) {
        setType(SplitQueryType.SELECT);
        for (BehaviorObject source : sourceTables(ctx)) {
            addUnary(SplitQueryType.SELECT, BehaviorAction.READ, source);
        }
        return null;
    }

    @Override
    public Void visitCreate_table(SqlServerParser.Create_tableContext ctx) {
        addUnary(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, ctx.table_name()));
        return null;
    }

    @Override
    public Void visitCreate_security_policy(SqlServerParser.Create_security_policyContext ctx) {
        addRelation(SplitQueryType.CREATE_POLICY, BehaviorAction.CREATE,
            qualifiedName(TargetType.RowAccessPolicy, ctx.schema_name, ctx.security_policy_name), policyDependencies(ctx));
        return null;
    }

    @Override
    public Void visitDrop_security_policy(SqlServerParser.Drop_security_policyContext ctx) {
        addUnary(SplitQueryType.DROP_POLICY, BehaviorAction.DROP, qualifiedName(TargetType.RowAccessPolicy, ctx.schema_name, ctx.security_policy_name));
        return null;
    }

    @Override
    public Void visitDrop_table(SqlServerParser.Drop_tableContext ctx) {
        for (SqlServerParser.Table_nameContext tableName : ctx.table_name()) {
            addUnary(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, tableName));
        }
        return null;
    }

    @Override
    public Void visitTruncate_table(SqlServerParser.Truncate_tableContext ctx) {
        addUnary(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.table_name()));
        return null;
    }

    @Override
    public Void visitCreate_view(SqlServerParser.Create_viewContext ctx) {
        addRelation(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.simple_name()), sourceTables(ctx));
        return null;
    }

    @Override
    public Void visitDrop_view(SqlServerParser.Drop_viewContext ctx) {
        for (SqlServerParser.Simple_nameContext viewName : ctx.simple_name()) {
            addUnary(SplitQueryType.DROP_VIEW, BehaviorAction.DROP, object(TargetType.View, viewName));
        }
        return null;
    }

    @Override
    public Void visitCreate_index(SqlServerParser.Create_indexContext ctx) {
        if (!ctx.id_().isEmpty()) {
            addRelation(SplitQueryType.ADD_INDEX, BehaviorAction.CREATE, object(TargetType.Index, ctx.id_(0)), objects(object(TargetType.Table, ctx.table_name())));
        }
        return null;
    }

    @Override
    public Void visitDrop_index(SqlServerParser.Drop_indexContext ctx) {
        for (SqlServerParser.Drop_relational_or_xml_or_spatial_indexContext item : ctx.drop_relational_or_xml_or_spatial_index()) {
            List<SqlServerParser.Id_Context> ids = descendants(item, SqlServerParser.Id_Context.class);
            SqlServerParser.Full_table_nameContext tableName = first(item, SqlServerParser.Full_table_nameContext.class);
            if (!ids.isEmpty()) {
                addRelation(SplitQueryType.DROP_INDEX, BehaviorAction.DROP, object(TargetType.Index, ids.get(0)), objects(object(TargetType.Table, tableName)));
            }
        }
        return null;
    }

    @Override
    public Void visitCreate_or_alter_function(SqlServerParser.Create_or_alter_functionContext ctx) {
        addUnary(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Function, ctx.func_proc_name_schema()));
        return null;
    }

    @Override
    public Void visitCreate_or_alter_procedure(SqlServerParser.Create_or_alter_procedureContext ctx) {
        addUnary(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(TargetType.Procedure, ctx.func_proc_name_schema()));
        return null;
    }

    @Override
    public Void visitInsert_statement(SqlServerParser.Insert_statementContext ctx) {
        SqlServerParser.Full_table_nameContext tableName = first(ctx.ddl_object(), SqlServerParser.Full_table_nameContext.class);
        BehaviorRelation relation = addRelation(SplitQueryType.INSERT, BehaviorAction.INSERT, object(TargetType.Table, tableName), sourceTables(ctx.insert_statement_value()));
        SqlServerParser.Table_value_constructorContext values = first(ctx.insert_statement_value(), SqlServerParser.Table_value_constructorContext.class);
        if (relation != null && values != null) {
            relation.setInsertRows((long) values.exps.size());
        }
        return null;
    }

    @Override
    public Void visitUpdate_statement(SqlServerParser.Update_statementContext ctx) {
        SqlServerParser.Full_table_nameContext tableName = first(ctx.ddl_object(), SqlServerParser.Full_table_nameContext.class);
        tableName = resolveAlias(tableName, ctx.table_sources());
        addRelation(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, tableName), sourceTables(ctx));
        return null;
    }

    @Override
    public Void visitDelete_statement(SqlServerParser.Delete_statementContext ctx) {
        SqlServerParser.Full_table_nameContext tableName = first(ctx.delete_statement_from(), SqlServerParser.Full_table_nameContext.class);
        if (tableName == null) {
            tableName = first(ctx.table_sources(), SqlServerParser.Full_table_nameContext.class);
        } else {
            tableName = resolveAlias(tableName, ctx.table_sources());
        }
        addRelation(SplitQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, tableName), sourceTables(ctx));
        return null;
    }

    @Override
    public Void visitExecute_statement(SqlServerParser.Execute_statementContext ctx) {
        SqlServerParser.Execute_bodyContext body = ctx.execute_body();
        SqlServerParser.Execute_as_contextContext context = body.execute_as_context();
        if (context != null) {
            Token token = context.getStart();
            String name = null;
            if (context.STRING() != null) {
                token = context.STRING().getSymbol();
                name = unquote(token.getText());
            } else if (!context.LOCAL_ID().isEmpty()) {
                token = context.LOCAL_ID(0).getSymbol();
            } else if (context.CALLER() != null) {
                token = context.CALLER().getSymbol();
            }
            addUnary(SplitQueryType.SWITCH_USER, BehaviorAction.SWITCH, identity(token, context.USER() != null, name));
            return null;
        }
        setType(SplitQueryType.CALL_PROG_OBJ);
        if (body.linkedServer != null) {
            addUnary(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, scopedName(TargetType.Link, body.linkedServer, null));
            return null;
        }
        SqlServerParser.Func_proc_name_server_database_schemaContext procName = body.func_proc_name_server_database_schema();
        if (procName != null) {
            addUnary(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, procName));
        } else {
            Token token = body.getStart();
            if (!body.execute_var_string().isEmpty()) {
                token = body.execute_var_string(0).getStart();
            }
            TargetType type = TargetType.Call;
            if (token.getType() == SqlServerParser.LOCAL_ID) {
                type = TargetType.Procedure;
            }
            BehaviorObject target = objects.unnamedObject(type, token, UmiTypes.Schema);
            target.setObjectName(new ObjectName(level(UmiTypes.Catalog), level(UmiTypes.Schema), null));
            addUnary(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, target);
        }
        return null;
    }

    @Override
    public Void visitReconfigure_statement(SqlServerParser.Reconfigure_statementContext ctx) {
        addUnary(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.APPLY,
            unnamed(TargetType.ConfigKey, ctx.getStart(), UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitAlter_server_configuration(SqlServerParser.Alter_server_configurationContext ctx) {
        Token start = startToken(ctx.getChild(4));
        Token stop = start;
        if (ctx.PROCESS() != null) {
            stop = ctx.AFFINITY().getSymbol();
        } else if (ctx.BUFFER() != null) {
            stop = ctx.EXTENSION().getSymbol();
        } else if (ctx.MEMORY_OPTIMIZED() != null) {
            stop = startToken(ctx.getChild(5));
        } else if (ctx.DIAGNOSTICS() != null) {
            stop = ctx.LOG().getSymbol();
        } else if (ctx.FAILOVER() != null) {
            stop = ctx.PROPERTY().getSymbol();
        } else if (ctx.HADR() != null) {
            stop = ctx.CONTEXT().getSymbol();
        }
        List<String> keyParts = new ArrayList<>();
        for (int i = start.getTokenIndex(); i <= stop.getTokenIndex(); i++) {
            Token token = parser.getTokenStream().get(i);
            if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                keyParts.add(token.getText().toUpperCase(Locale.ROOT));
            }
        }
        String name = String.join(" ", keyParts);
        BehaviorObject key = objects.instanceObject(TargetType.ConfigKey, start, stop, name);
        key.setObjectName(new ObjectName(null, null, name));
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.FILENAME() != null) {
            targets.add(instanceValue(TargetType.File, ctx.STRING().getSymbol()));
        }
        if (ctx.GROUP() != null) {
            for (SqlServerParser.Id_Context database : ctx.id_()) {
                targets.add(object(TargetType.Catalog, database));
            }
        }
        addRelation(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, key, targets);
        return null;
    }

    @Override
    public Void visitKill_process(SqlServerParser.Kill_processContext ctx) {
        SplitQueryType type = SplitQueryType.ADMIN;
        BehaviorAction action = BehaviorAction.TERMINATE;
        if (ctx.STATUSONLY() != null) {
            type = SplitQueryType.PERFORMANCE;
            action = BehaviorAction.READ;
        }
        addUnary(type, action, unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitShutdown_statement(SqlServerParser.Shutdown_statementContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.STOP,
            unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitCheckpoint_statement(SqlServerParser.Checkpoint_statementContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.CHECKPOINT, currentCatalog(ctx.getStart()));
        return null;
    }

    @Override
    public Void visitCreate_database(SqlServerParser.Create_databaseContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (SqlServerParser.Database_attach_fileContext attached : ctx.database_attach_file()) {
            targets.add(instanceValue(TargetType.File, attached.STRING().getSymbol()));
        }
        addObject(targets, object(TargetType.Catalog, ctx.source_database));
        addRelation(SplitQueryType.CREATE_CATALOG, BehaviorAction.CREATE, object(TargetType.Catalog, ctx.database), targets);
        for (SqlServerParser.Database_snapshot_fileContext snapshot : ctx.database_snapshot_file()) {
            addUnary(SplitQueryType.CREATE_CATALOG, BehaviorAction.CREATE,
                instanceValue(TargetType.File, snapshot.STRING().getSymbol()));
        }
        for (SqlServerParser.File_specContext file : descendants(ctx, SqlServerParser.File_specContext.class)) {
            addUnary(SplitQueryType.CREATE_CATALOG, BehaviorAction.CREATE, instanceValue(TargetType.File, file.file));
        }
        return null;
    }

    @Override
    public Void visitAlter_database(SqlServerParser.Alter_databaseContext ctx) {
        BehaviorObject database = currentCatalog(ctx.getStart());
        if (ctx.database != null) {
            database = object(TargetType.Catalog, ctx.database);
        } else if (ctx.CURRENT() != null) {
            database = currentCatalog(ctx.CURRENT().getSymbol());
        }
        if (ctx.new_name != null) {
            addRelation(SplitQueryType.RENAME_CATALOG, BehaviorAction.RENAME, database,
                objects(object(TargetType.Catalog, ctx.new_name)));
            return null;
        }
        SqlServerParser.Query_store_optionsContext queryStore = first(ctx, SqlServerParser.Query_store_optionsContext.class);
        if (queryStore != null && queryStore.CLEAR() != null) {
            addUnary(SplitQueryType.ALTER_CATALOG, BehaviorAction.PURGE, database);
            return null;
        }
        List<BehaviorObject> files = new ArrayList<>();
        for (SqlServerParser.FilespecContext file : descendants(ctx, SqlServerParser.FilespecContext.class)) {
            if (file.file_name != null) {
                files.add(instanceValue(TargetType.File, file.file_name));
            }
        }
        for (SqlServerParser.Hadr_optionsContext hadr : descendants(ctx, SqlServerParser.Hadr_optionsContext.class)) {
            addObject(files, scopedName(TargetType.AvailabilityGroup, hadr.availability_group_name, null));
        }
        // Logical files and filegroups remain catalog properties at the current SDK granularity.
        addRelation(SplitQueryType.ALTER_CATALOG, BehaviorAction.ALTER, database, files);
        return null;
    }

    @Override
    public Void visitDrop_database(SqlServerParser.Drop_databaseContext ctx) {
        for (SqlServerParser.Id_Context database : ctx.database_name_or_database_snapshot_name) {
            addUnary(SplitQueryType.DROP_CATALOG, BehaviorAction.DROP, object(TargetType.Catalog, database));
        }
        return null;
    }

    @Override
    public Void visitAlter_database_scoped_configuration(SqlServerParser.Alter_database_scoped_configurationContext ctx) {
        Token token;
        BehaviorAction action = BehaviorAction.CONFIGURE;
        if (ctx.database_scoped_option() != null) {
            token = ctx.database_scoped_option().getStart();
        } else {
            token = ctx.PROCEDURE_CACHE().getSymbol();
            action = BehaviorAction.RESET;
        }
        BehaviorObject key = unnamed(TargetType.ConfigKey, token, UmiTypes.Catalog);
        String name = token.getText().toUpperCase(Locale.ROOT);
        key.setObjectPath(key.getObjectPath() + name + "/");
        key.setObjectName(new ObjectName(level(UmiTypes.Catalog), null, name));
        addUnary(SplitQueryType.ALTER_CATALOG, action, key);
        return null;
    }

    @Override
    public Void visitBackup_database(SqlServerParser.Backup_databaseContext ctx) {
        Token token = ctx.database_variable;
        if (ctx.database_name != null) {
            token = ctx.database_name.getStart();
        }
        backup(ctx, objects(instanceValue(TargetType.Catalog, token)));
        return null;
    }

    @Override
    public Void visitBackup_log(SqlServerParser.Backup_logContext ctx) {
        Token token = ctx.database_variable;
        if (ctx.database_name != null) {
            token = ctx.database_name.getStart();
        }
        backup(ctx, objects(instanceValue(TargetType.Catalog, token)));
        return null;
    }

    @Override
    public Void visitBackup_snapshot(SqlServerParser.Backup_snapshotContext ctx) {
        List<BehaviorObject> sources = new ArrayList<>();
        if (ctx.SERVER() != null) {
            sources.add(unnamed(TargetType.Instance, ctx.SERVER().getSymbol(), UmiTypes.Instance));
        } else {
            for (SqlServerParser.Id_Context database : ctx.id_()) {
                sources.add(object(TargetType.Catalog, database));
            }
        }
        backup(ctx, sources);
        return null;
    }

    private void backup(ParserRuleContext ctx, List<BehaviorObject> sources) {
        for (BehaviorObject device : backupDevices(ctx)) {
            addRelation(SplitQueryType.ADMIN, BehaviorAction.EXPORT, device, sources);
        }
    }

    private List<BehaviorObject> backupDevices(ParserRuleContext ctx) {
        List<BehaviorObject> result = new ArrayList<>();
        for (SqlServerParser.Backup_deviceContext device : descendants(ctx, SqlServerParser.Backup_deviceContext.class)) {
            Token token = device.getStart();
            if (device.STRING() != null) {
                token = device.STRING().getSymbol();
            } else if (device.LOCAL_ID() != null) {
                token = device.LOCAL_ID().getSymbol();
            }
            result.add(instanceValue(TargetType.File, token));
        }
        return result;
    }

    @Override
    public Void visitRestore_database(SqlServerParser.Restore_databaseContext ctx) {
        Token token = ctx.database_variable;
        if (ctx.database_name != null) {
            token = ctx.database_name.getStart();
        }
        BehaviorObject database = instanceValue(TargetType.Catalog, token);
        if (ctx.DATABASE_SNAPSHOT() != null) {
            Token snapshot = startToken(ctx.getChild(ctx.getChildCount() - 1));
            addRelation(SplitQueryType.ADMIN, BehaviorAction.RESTORE, database,
                objects(instanceValue(TargetType.Catalog, snapshot)));
        } else {
            restore(ctx, database);
        }
        return null;
    }

    @Override
    public Void visitRestore_log(SqlServerParser.Restore_logContext ctx) {
        Token token = ctx.database_variable;
        if (ctx.database_name != null) {
            token = ctx.database_name.getStart();
        }
        restore(ctx, instanceValue(TargetType.Catalog, token));
        return null;
    }

    private void restore(ParserRuleContext ctx, BehaviorObject database) {
        List<BehaviorObject> devices = backupDevices(ctx);
        BehaviorAction action = BehaviorAction.RESTORE;
        if (devices.isEmpty()) {
            for (SqlServerParser.Restore_common_optionContext option : descendants(ctx, SqlServerParser.Restore_common_optionContext.class)) {
                if (option.RECOVERY() != null) {
                    action = BehaviorAction.RECOVER;
                }
            }
        }
        addRelation(SplitQueryType.ADMIN, action, database, devices);
    }

    @Override
    public Void visitRestore_metadata(SqlServerParser.Restore_metadataContext ctx) {
        for (BehaviorObject device : backupDevices(ctx)) {
            addUnary(SplitQueryType.METADATA, BehaviorAction.READ, device);
        }
        return null;
    }

    @Override
    public Void visitRestore_verify(SqlServerParser.Restore_verifyContext ctx) {
        List<BehaviorObject> destinations = new ArrayList<>();
        for (SqlServerParser.Restore_verify_optionContext option : ctx.restore_verify_option()) {
            if (option.MOVE() != null) {
                destinations.add(instanceValue(TargetType.File, startToken(option.getChild(3))));
            }
        }
        for (BehaviorObject device : backupDevices(ctx)) {
            addRelation(SplitQueryType.ADMIN, BehaviorAction.VALIDATE, device, destinations);
        }
        return null;
    }

    @Override
    public Void visitDbcc_freeproccache(SqlServerParser.Dbcc_freeproccacheContext ctx) {
        BehaviorObject scope = unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance);
        if (ctx.id_or_string() != null) {
            scope = instanceValue(TargetType.ResourceGroup, ctx.id_or_string().getStart());
        }
        addUnary(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.RESET, scope);
        return null;
    }

    @Override
    public Void visitDbcc_freesystemcache(SqlServerParser.Dbcc_freesystemcacheContext ctx) {
        BehaviorObject scope = unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance);
        if (ctx.id_or_string() != null) {
            scope = instanceValue(TargetType.ResourceGroup, ctx.id_or_string().getStart());
        }
        addUnary(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.RESET, scope);
        return null;
    }

    @Override
    public Void visitDbcc_opentran(SqlServerParser.Dbcc_opentranContext ctx) {
        addUnary(SplitQueryType.PERFORMANCE, BehaviorAction.READ, dbccDatabase(ctx));
        return null;
    }

    @Override
    public Void visitDbcc_sqlperf(SqlServerParser.Dbcc_sqlperfContext ctx) {
        SplitQueryType type = SplitQueryType.PERFORMANCE;
        BehaviorAction action = BehaviorAction.READ;
        if (ctx.CLEAR() != null) {
            type = SplitQueryType.ADMIN_PERFORMANCE;
            action = BehaviorAction.RESET;
        }
        addUnary(type, action, unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitDbcc_show_statistics(SqlServerParser.Dbcc_show_statisticsContext ctx) {
        addUnary(SplitQueryType.PERFORMANCE, BehaviorAction.READ,
            tableFromString(ctx.id_or_string(0).getStart(), level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDbcc_showcontig(SqlServerParser.Dbcc_showcontigContext ctx) {
        BehaviorObject scope = currentCatalog(ctx.getStart());
        if (ctx.table_or_view != null) {
            Token argument = ctx.table_or_view.getStart();
            if (argument == ctx.table_or_view.getStop()) {
                scope = tableFromString(argument, level(UmiTypes.Catalog));
            } else {
                // Expressions such as OBJECT_ID(...) cannot be resolved without evaluating SQL.
                scope = unnamed(TargetType.Table, argument, UmiTypes.Catalog);
                scope.setObjectName(new ObjectName(level(UmiTypes.Catalog), null, null));
            }
        }
        addUnary(SplitQueryType.PERFORMANCE, BehaviorAction.READ, scope);
        return null;
    }

    @Override
    public Void visitDbcc_tracestatus(SqlServerParser.Dbcc_tracestatusContext ctx) {
        traceFlags(ctx, ctx.dbcc_trace_flag(), true);
        return null;
    }

    @Override
    public Void visitDbcc_trace_control(SqlServerParser.Dbcc_trace_controlContext ctx) {
        traceFlags(ctx, ctx.dbcc_trace_flag(), false);
        return null;
    }

    private void traceFlags(ParserRuleContext ctx, List<SqlServerParser.Dbcc_trace_flagContext> flags, boolean read) {
        boolean global = flags.stream().anyMatch(flag -> "-1".equals(flag.getText()));
        SplitQueryType type = SplitQueryType.SESSION_SETTING_WRITE;
        BehaviorAction action = BehaviorAction.CONFIGURE;
        if (read) {
            type = SplitQueryType.PERFORMANCE;
            action = BehaviorAction.READ;
        } else if (global) {
            type = SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        boolean namedFlag = false;
        for (SqlServerParser.Dbcc_trace_flagContext flag : flags) {
            if ("-1".equals(flag.getText())) {
                continue;
            }
            Token number = flag.DECIMAL().getSymbol();
            addUnary(type, action, instanceNamed(TargetType.ConfigKey, number, number.getText()));
            namedFlag = true;
        }
        if (!namedFlag) {
            addUnary(type, action, unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance));
        }
    }

    @Override
    public Void visitDbcc_freesessioncache(SqlServerParser.Dbcc_freesessioncacheContext ctx) {
        addUnary(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.RESET,
            unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitDbcc_dropcleanbuffers(SqlServerParser.Dbcc_dropcleanbuffersContext ctx) {
        addUnary(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.RESET,
            unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitDbcc_inputbuffer(SqlServerParser.Dbcc_inputbufferContext ctx) {
        addUnary(SplitQueryType.PERFORMANCE, BehaviorAction.READ,
            unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitDbcc_outputbuffer(SqlServerParser.Dbcc_outputbufferContext ctx) {
        addUnary(SplitQueryType.PERFORMANCE, BehaviorAction.READ,
            unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitDbcc_proccache(SqlServerParser.Dbcc_proccacheContext ctx) {
        addUnary(SplitQueryType.PERFORMANCE, BehaviorAction.READ,
            unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitDbcc_memorystatus(SqlServerParser.Dbcc_memorystatusContext ctx) {
        addUnary(SplitQueryType.PERFORMANCE, BehaviorAction.READ,
            unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitDbcc_useroptions(SqlServerParser.Dbcc_useroptionsContext ctx) {
        addUnary(SplitQueryType.METADATA, BehaviorAction.READ,
            unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitDbcc_checkdb(SqlServerParser.Dbcc_checkdbContext ctx) {
        BehaviorAction action = BehaviorAction.VALIDATE;
        if (ctx.REPAIR_REBUILD() != null || ctx.REPAIR_ALLOW_DATA_LOSS() != null) {
            action = BehaviorAction.REPAIR;
        }
        addUnary(SplitQueryType.ADMIN, action, dbccDatabase(ctx));
        return null;
    }

    @Override
    public Void visitDbcc_checkalloc(SqlServerParser.Dbcc_checkallocContext ctx) {
        BehaviorAction action = BehaviorAction.VALIDATE;
        // CHECKALLOC only repairs at REPAIR_ALLOW_DATA_LOSS; REPAIR_REBUILD is not applicable.
        if (ctx.REPAIR_ALLOW_DATA_LOSS() != null) {
            action = BehaviorAction.REPAIR;
        }
        addUnary(SplitQueryType.ADMIN, action, dbccDatabase(ctx));
        return null;
    }

    @Override
    public Void visitDbcc_checkcatalog(SqlServerParser.Dbcc_checkcatalogContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.VALIDATE, dbccDatabase(ctx));
        return null;
    }

    private BehaviorObject dbccDatabase(ParserRuleContext ctx) {
        if (ctx.getToken(SqlServerParser.LR_BRACKET, 0) == null) {
            return currentCatalog(ctx.getStart());
        }
        Token argument = startToken(ctx.getChild(2));
        if (argument.getType() == SqlServerParser.DECIMAL && "0".equals(argument.getText())) {
            return currentCatalog(argument);
        }
        return instanceValue(TargetType.Catalog, argument);
    }

    @Override
    public Void visitDbcc_checktable(SqlServerParser.Dbcc_checktableContext ctx) {
        BehaviorAction action = BehaviorAction.VALIDATE;
        if (ctx.REPAIR_REBUILD() != null || ctx.REPAIR_ALLOW_DATA_LOSS() != null) {
            action = BehaviorAction.REPAIR;
        }
        // The expression alternative also accepts the unquoted repair keyword as an identifier.
        if (ctx.index_id != null && ctx.index_id.getStart() == ctx.index_id.getStop()) {
            int option = ctx.index_id.getStart().getType();
            if (option == SqlServerParser.REPAIR_REBUILD || option == SqlServerParser.REPAIR_ALLOW_DATA_LOSS) {
                action = BehaviorAction.REPAIR;
            }
        }
        addUnary(SplitQueryType.ADMIN_TABLE, action, tableFromString(ctx.table_or_view_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDbcc_checkident(SqlServerParser.Dbcc_checkidentContext ctx) {
        BehaviorAction action = BehaviorAction.REPAIR;
        if (ctx.NORESEED() != null) {
            action = BehaviorAction.READ;
        }
        addUnary(SplitQueryType.ADMIN_TABLE, action,
            tableFromString(ctx.id_or_string().getStart(), level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDbcc_shrinkdatabase(SqlServerParser.Dbcc_shrinkdatabaseContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.OPTIMIZE, dbccDatabase(ctx));
        return null;
    }

    @Override
    public Void visitDbcc_updateusage(SqlServerParser.Dbcc_updateusageContext ctx) {
        BehaviorObject database = dbccDatabase(ctx);
        if (ctx.table_or_view == null) {
            addUnary(SplitQueryType.ADMIN, BehaviorAction.REPAIR, database);
        } else {
            String catalog = database.getObjectName().getObjectName();
            if ("0".equals(ctx.database.getText())) {
                catalog = level(UmiTypes.Catalog);
            }
            addUnary(SplitQueryType.ADMIN_TABLE, BehaviorAction.REPAIR,
                tableFromString(ctx.table_or_view.getStart(), catalog));
        }
        return null;
    }

    @Override
    public Void visitAlter_index(SqlServerParser.Alter_indexContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.table_name());
        Token token = startToken(ctx.getChild(2));
        String name = null;
        if (ctx.id_() != null) {
            name = unquote(token.getText());
        }
        BehaviorObject index = unnamed(TargetType.Index, token, UmiTypes.Schema);
        String path = table.getObjectPath();
        String tableName = table.getObjectName().getObjectName();
        path = path.substring(0, path.length() - tableName.length() - 1);
        if (name != null) {
            path += name + "/";
        }
        index.setObjectPath(path);
        index.setObjectName(new ObjectName(table.getObjectName().getCatalog(), table.getObjectName().getSchema(), name));
        BehaviorAction action = BehaviorAction.OPTIMIZE;
        if (ctx.PAUSE() != null) {
            action = BehaviorAction.STOP;
        } else if (ctx.RESUME() != null) {
            action = BehaviorAction.START;
        } else if (ctx.ABORT() != null) {
            action = BehaviorAction.TERMINATE;
        } else if (ctx.DISABLE() != null) {
            action = BehaviorAction.ALTER;
        } else if (ctx.set_index_options() != null) {
            action = BehaviorAction.CONFIGURE;
        }
        addRelation(SplitQueryType.ALTER_INDEX, action, index, objects(table));
        return null;
    }

    @Override
    public Void visitCreate_statistics(SqlServerParser.Create_statisticsContext ctx) {
        addUnary(SplitQueryType.ADMIN_TABLE, BehaviorAction.ANALYZE, object(TargetType.Table, ctx.table_name()));
        return null;
    }

    @Override
    public Void visitUpdate_statistics(SqlServerParser.Update_statisticsContext ctx) {
        addUnary(SplitQueryType.ADMIN_TABLE, BehaviorAction.ANALYZE, object(TargetType.Table, ctx.full_table_name()));
        return null;
    }

    @Override
    public Void visitDrop_statistics(SqlServerParser.Drop_statisticsContext ctx) {
        for (SqlServerParser.Table_nameContext table : ctx.table_name()) {
            addUnary(SplitQueryType.ADMIN_TABLE, BehaviorAction.ALTER, object(TargetType.Table, table));
        }
        return null;
    }

    @Override
    public Void visitCreate_workload_group(SqlServerParser.Create_workload_groupContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (ParseTree child : ctx.children) {
            if (child == ctx.workload_group_pool_name || child == ctx.external_pool_name
                || child instanceof TerminalNode terminal && terminal.getSymbol().getType() == SqlServerParser.DEFAULT_DOUBLE_QUOTE) {
                targets.add(instanceValue(TargetType.ResourceGroup, startToken(child)));
            }
        }
        addRelation(SplitQueryType.CREATE_RESOURCE_GROUP, BehaviorAction.CREATE,
            instanceValue(TargetType.ResourceGroup, ctx.workload_group_group_name.getStart()), targets);
        return null;
    }

    @Override
    public Void visitAlter_workload_group(SqlServerParser.Alter_workload_groupContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.USING() != null) {
            targets.add(instanceValue(TargetType.ResourceGroup, startToken(ctx.getChild(ctx.getChildCount() - 1))));
        }
        addRelation(SplitQueryType.ALTER_RESOURCE_GROUP, BehaviorAction.ALTER,
            instanceValue(TargetType.ResourceGroup, startToken(ctx.getChild(3))), targets);
        return null;
    }

    @Override
    public Void visitDrop_workload_group(SqlServerParser.Drop_workload_groupContext ctx) {
        addUnary(SplitQueryType.DROP_RESOURCE_GROUP, BehaviorAction.DROP,
            instanceValue(TargetType.ResourceGroup, ctx.group_name.getStart()));
        return null;
    }

    @Override
    public Void visitAlter_resource_governor(SqlServerParser.Alter_resource_governorContext ctx) {
        BehaviorObject governor = objects.instanceObject(TargetType.Instance,
            ctx.RESOURCE().getSymbol(), ctx.GOVERNOR().getSymbol(), "");
        governor.setObjectPath(unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance).getObjectPath());
        governor.setObjectName(new ObjectName(null, null, null));
        BehaviorAction action = BehaviorAction.CONFIGURE;
        if (ctx.RECONFIGURE() != null) {
            action = BehaviorAction.APPLY;
        } else if (ctx.DISABLE() != null) {
            action = BehaviorAction.STOP;
        } else if (ctx.RESET() != null) {
            action = BehaviorAction.RESET;
        }
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.function_name != null) {
            String schema = unquote(ctx.schema_name.getText());
            String name = unquote(ctx.function_name.getText());
            BehaviorObject function = objects.object(TargetType.Function, ctx.schema_name.getStart(), ctx.function_name.getStop(),
                List.of(schema, name));
            function.setObjectName(new ObjectName(level(UmiTypes.Catalog), schema, name));
            endPosition(function, ctx.function_name.getStop());
            targets.add(function);
        }
        addRelation(SplitQueryType.ADMIN_RESOURCE_GROUP, action, governor, targets);
        return null;
    }

    @Override
    public Void visitCreate_login_sql_server(SqlServerParser.Create_login_sql_serverContext ctx) {
        List<BehaviorObject> targets = loginOptions(ctx);
        addObject(targets, scopedName(TargetType.Certificate, ctx.certname, "master"));
        addObject(targets, scopedName(TargetType.AsymmetricKey, ctx.asym_key_name, "master"));
        addRelation(SplitQueryType.CREATE_USER, BehaviorAction.CREATE,
            scopedName(TargetType.User, ctx.login_name, null), targets);
        return null;
    }

    @Override
    public Void visitAlter_login_sql_server(SqlServerParser.Alter_login_sql_serverContext ctx) {
        List<BehaviorObject> targets = loginOptions(ctx);
        addObject(targets, scopedName(TargetType.Credential, ctx.credential_name, null));
        BehaviorObject renamed = null;
        for (SqlServerParser.Alter_login_optionContext option : ctx.alter_login_option()) {
            if (option.new_name != null) {
                renamed = scopedName(TargetType.User, option.new_name, null);
            } else if (option.CREDENTIAL() != null && option.id_() != null) {
                targets.add(scopedName(TargetType.Credential, option.id_(), null));
            }
        }
        alterPrincipal(scopedName(TargetType.User, ctx.login_name, null), renamed,
            ctx.alter_login_option().size(), SplitQueryType.ALTER_USER, SplitQueryType.RENAME_USER, targets);
        return null;
    }

    private void alterPrincipal(BehaviorObject subject, BehaviorObject renamed, int optionCount,
                                SplitQueryType alterType, SplitQueryType renameType, List<BehaviorObject> targets) {
        targets.sort(Comparator.comparingInt(BehaviorObject::getStartLine).thenComparingInt(BehaviorObject::getStartColumn));
        SplitQueryType type = alterType;
        if (renamed != null && optionCount == 1) {
            type = renameType;
        }
        if (renamed != null) {
            addRelation(type, BehaviorAction.RENAME, subject, objects(renamed));
        }
        // Other attributes remain ALTER dependencies; a schema or credential is not a rename destination.
        if (renamed == null || optionCount > 1) {
            addRelation(type, BehaviorAction.ALTER, subject, targets);
        }
    }

    private List<BehaviorObject> loginOptions(ParserRuleContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (SqlServerParser.Windows_login_optionContext option : descendants(ctx, SqlServerParser.Windows_login_optionContext.class)) {
            addObject(targets, object(TargetType.Catalog, option.default_database));
        }
        for (SqlServerParser.Create_login_sql_server_withContext option : descendants(ctx, SqlServerParser.Create_login_sql_server_withContext.class)) {
            addObject(targets, scopedName(TargetType.Credential, option.credential_name, null));
        }
        targets.sort(Comparator.comparingInt(BehaviorObject::getStartLine).thenComparingInt(BehaviorObject::getStartColumn));
        return targets;
    }

    @Override
    public Void visitCreate_user(SqlServerParser.Create_userContext ctx) {
        List<BehaviorObject> targets = objects(scopedName(TargetType.User, ctx.login_name, null),
            scopedName(TargetType.Certificate, ctx.cert_name, level(UmiTypes.Catalog)),
            scopedName(TargetType.AsymmetricKey, ctx.asym_key_name, level(UmiTypes.Catalog)));
        for (SqlServerParser.Mapped_user_optionContext option : descendants(ctx, SqlServerParser.Mapped_user_optionContext.class)) {
            addObject(targets, object(TargetType.Schema, option.schema_name));
        }
        if (ctx.DEFAULT_SCHEMA() != null) {
            targets.add(object(TargetType.Schema, ctx.id_(ctx.id_().size() - 1)));
        }
        addRelation(SplitQueryType.CREATE_USER, BehaviorAction.CREATE,
            scopedName(TargetType.User, ctx.user_name, level(UmiTypes.Catalog)), targets);
        return null;
    }

    @Override
    public Void visitAlter_user(SqlServerParser.Alter_userContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        BehaviorObject renamed = null;
        for (SqlServerParser.Alter_user_optionContext option : ctx.alter_user_option()) {
            if (option.new_name != null) {
                renamed = scopedName(TargetType.User, option.new_name, level(UmiTypes.Catalog));
            } else if (option.LOGIN() != null) {
                targets.add(scopedName(TargetType.User, option.id_(), null));
            } else if (option.DEFAULT_SCHEMA() != null) {
                addObject(targets, object(TargetType.Schema, option.id_()));
            }
        }
        alterPrincipal(scopedName(TargetType.User, ctx.username, level(UmiTypes.Catalog)), renamed,
            ctx.alter_user_option().size(), SplitQueryType.ALTER_USER, SplitQueryType.RENAME_USER, targets);
        return null;
    }

    @Override
    public Void visitCreate_server_role(SqlServerParser.Create_server_roleContext ctx) {
        addRelation(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE, scopedName(TargetType.Role, ctx.server_role, null),
            objects(scopedName(TargetType.UserOrRole, ctx.server_principal, null)));
        return null;
    }

    @Override
    public Void visitCreate_db_role(SqlServerParser.Create_db_roleContext ctx) {
        addRelation(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE,
            scopedName(TargetType.Role, ctx.role_name, level(UmiTypes.Catalog)),
            objects(scopedName(TargetType.UserOrRole, ctx.owner_name, level(UmiTypes.Catalog))));
        return null;
    }

    @Override
    public Void visitAlter_server_role(SqlServerParser.Alter_server_roleContext ctx) {
        alterRole(ctx.server_role_name, ctx.new_server_role_name, ctx.server_principal, ctx.ADD() != null, null);
        return null;
    }

    @Override
    public Void visitAlter_db_role(SqlServerParser.Alter_db_roleContext ctx) {
        alterRole(ctx.role_name, ctx.new_role_name, ctx.database_principal, ctx.ADD() != null, level(UmiTypes.Catalog));
        return null;
    }

    private void alterRole(ParserRuleContext name, ParserRuleContext newName, ParserRuleContext member, boolean add, String catalog) {
        BehaviorObject role = scopedName(TargetType.Role, name, catalog);
        if (newName != null) {
            addRelation(SplitQueryType.RENAME_ROLE, BehaviorAction.RENAME, role, objects(scopedName(TargetType.Role, newName, catalog)));
        } else {
            BehaviorAction action = BehaviorAction.REVOKE;
            if (add) {
                action = BehaviorAction.GRANT;
            }
            addRelation(SplitQueryType.ALTER_ROLE, action, role, objects(scopedName(TargetType.UserOrRole, member, catalog)));
        }
    }

    @Override
    public Void visitCreate_application_role(SqlServerParser.Create_application_roleContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.DEFAULT_SCHEMA() != null) {
            targets.add(object(TargetType.Schema, ctx.id_(1)));
        }
        addRelation(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE,
            scopedName(TargetType.Role, ctx.application_role, level(UmiTypes.Catalog)), targets);
        return null;
    }

    @Override
    public Void visitAlter_application_role(SqlServerParser.Alter_application_roleContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        BehaviorObject renamed = null;
        for (SqlServerParser.Application_role_optionContext option : ctx.application_role_option()) {
            if (option.new_name != null) {
                renamed = scopedName(TargetType.Role, option.new_name, level(UmiTypes.Catalog));
            } else if (option.DEFAULT_SCHEMA() != null) {
                targets.add(object(TargetType.Schema, option.id_()));
            }
        }
        alterPrincipal(scopedName(TargetType.Role, ctx.application_role, level(UmiTypes.Catalog)), renamed,
            ctx.application_role_option().size(), SplitQueryType.ALTER_ROLE, SplitQueryType.RENAME_ROLE, targets);
        return null;
    }

    @Override
    public Void visitGrant_statement(SqlServerParser.Grant_statementContext ctx) {
        permission(ctx, SplitQueryType.GRANT, BehaviorAction.GRANT, ctx.permission_list(), ctx.permission_target(), ctx.principal_id(), ctx.AS() != null);
        return null;
    }

    @Override
    public Void visitDeny_statement(SqlServerParser.Deny_statementContext ctx) {
        permission(ctx, SplitQueryType.ADMIN, BehaviorAction.DENY, ctx.permission_list(), ctx.permission_target(), ctx.principal_id(), ctx.AS() != null);
        return null;
    }

    @Override
    public Void visitRevoke_statement(SqlServerParser.Revoke_statementContext ctx) {
        permission(ctx, SplitQueryType.REVOKE, BehaviorAction.REVOKE, ctx.permission_list(), ctx.permission_target(), ctx.principal_id(), ctx.AS() != null);
        return null;
    }

    private void permission(ParserRuleContext ctx, SplitQueryType type, BehaviorAction action,
                            SqlServerParser.Permission_listContext permissions, SqlServerParser.Permission_targetContext target,
                            List<SqlServerParser.Principal_idContext> principals, boolean grantor) {
        boolean server = false;
        BehaviorObject subject;
        if (target == null) {
            for (SqlServerParser.Permission_itemContext item : permissions.permission_item()) {
                server |= serverPermission(item.grant_permission());
            }
            subject = currentCatalog(ctx.getStart());
            if (server) {
                subject = unnamed(TargetType.Instance, ctx.getStart(), UmiTypes.Instance);
            }
        } else {
            String kind = "OBJECT";
            if (target.class_type_for_grant() != null) {
                kind = target.class_type_for_grant().getText().toUpperCase(Locale.ROOT);
            }
            server = serverSecurable(kind);
            subject = securable(kind, target.table_name());
        }
        String catalog = level(UmiTypes.Catalog);
        if (server) {
            catalog = null;
        }
        List<BehaviorObject> targets = new ArrayList<>();
        // AS is the grantor, not a recipient of the permission being changed.
        int count = principals.size();
        if (grantor) {
            count--;
        }
        for (int i = 0; i < count; i++) {
            targets.add(scopedName(TargetType.UserOrRole, principals.get(i), catalog));
        }
        addRelation(type, action, subject, targets);
    }

    private boolean serverPermission(SqlServerParser.Grant_permissionContext permission) {
        // These permissions have instance scope even when the word SERVER is absent.
        return switch (permission.getText().toUpperCase(Locale.ROOT)) {
            case "ADMINISTERBULKOPERATIONS", "ALTERANYAVAILABILITYGROUP", "ALTERANYCONNECTION", "ALTERANYCREDENTIAL",
                 "ALTERANYDATABASE", "ALTERANYENDPOINT", "ALTERANYEVENTNOTIFICATION", "ALTERANYEVENTSESSION",
                 "ALTERANYLINKEDSERVER", "ALTERANYLOGIN", "ALTERANYSERVERAUDIT", "ALTERANYSERVERROLE",
                 "ALTERRESOURCES", "ALTERSERVERSTATE", "ALTERSETTINGS", "ALTERTRACE", "AUTHENTICATESERVER",
                 "CONNECTANYDATABASE", "CONNECTSQL", "CONTROLSERVER", "CREATEANYDATABASE", "CREATEAVAILABILITYGROUP",
                 "CREATEDDLEVENTNOTIFICATION", "CREATEENDPOINT", "CREATESERVERROLE", "CREATETRACEEVENTNOTIFICATION",
                 "EXTERNALACCESSASSEMBLY", "IMPERSONATEANYLOGIN", "SELECTALLUSERSECURABLES", "SHUTDOWN",
                 "UNSAFEASSEMBLY", "VIEWANYDATABASE", "VIEWANYDEFINITION", "VIEWANYSECURITYDEFINITION",
                 "VIEWANYPERFORMANCEDEFINITION", "VIEWANYCRYPTOGRAPHICALLYSECUREDDEFINITION", "VIEWSERVERSTATE",
                 "VIEWSERVERSECURITYSTATE", "VIEWSERVERPERFORMANCESTATE" -> true;
            default -> false;
        };
    }

    private boolean serverSecurable(String kind) {
        return switch (kind) {
            case "LOGIN", "SQLLOGIN", "SERVERROLE", "CREDENTIAL", "DATABASE", "SERVER", "ENDPOINT",
                 "AVAILABILITYGROUP", "AUDIT", "SERVERAUDIT", "SERVERAUDITSPECIFICATION", "EVENTSESSION",
                 "CRYPTOGRAPHICPROVIDER", "RESOURCEGOVERNOR", "EXTERNALRESOURCEPOOL", "TRIGGERSERVER" -> true;
            default -> false;
        };
    }

    private BehaviorObject securable(String kind, ParserRuleContext name) {
        TargetType type = switch (kind) {
            case "OBJECT" -> TargetType.SchemaObject;
            case "DATABASE" -> TargetType.Catalog;
            case "SCHEMA" -> TargetType.Schema;
            case "LOGIN", "SQLLOGIN", "USER" -> TargetType.User;
            case "ROLE", "SERVERROLE", "APPLICATIONROLE" -> TargetType.Role;
            case "CERTIFICATE" -> TargetType.Certificate;
            case "ASYMMETRICKEY" -> TargetType.AsymmetricKey;
            case "SYMMETRICKEY" -> TargetType.SymmetricKey;
            case "COLUMNMASTERKEY" -> TargetType.ColumnMasterKey;
            case "COLUMNENCRYPTIONKEY" -> TargetType.ColumnEncryptionKey;
            case "CREDENTIAL", "DATABASESCOPEDCREDENTIAL" -> TargetType.Credential;
            case "TYPE" -> TargetType.Type;
            default -> TargetType.Object;
        };
        if (type == TargetType.SchemaObject || type == TargetType.Catalog || type == TargetType.Schema || type == TargetType.Type) {
            return object(type, name);
        }
        String catalog = level(UmiTypes.Catalog);
        if (serverSecurable(kind)) {
            catalog = null;
        }
        return scopedName(type, name, catalog);
    }

    @Override
    public Void visitAlter_authorization(SqlServerParser.Alter_authorizationContext ctx) {
        String kind = "OBJECT";
        if (ctx.class_type() != null) {
            kind = ctx.class_type().getText().toUpperCase(Locale.ROOT);
        }
        ownership(kind, ctx.entity, ctx.authorization_grantee());
        return null;
    }

    private void ownership(String kind, ParserRuleContext entity, SqlServerParser.Authorization_granteeContext grantee) {
        BehaviorObject owner;
        if (grantee.principal_name == null) {
            owner = unnamedRange(TargetType.UserOrRole, grantee.getStart(), grantee.getStop(), UmiTypes.Catalog);
        } else {
            String catalog = level(UmiTypes.Catalog);
            if (serverSecurable(kind)) {
                catalog = null;
            }
            TargetType ownerType = TargetType.UserOrRole;
            if ("DATABASE".equals(kind)) {
                ownerType = TargetType.User;
            }
            owner = scopedName(ownerType, grantee.principal_name, catalog);
        }
        addRelation(SplitQueryType.TRANSFER_PRIVILEGE, BehaviorAction.TRANSFER, securable(kind, entity), objects(owner));
    }

    private BehaviorObject scopedName(TargetType type, ParserRuleContext name, String catalog) {
        if (name == null) {
            return null;
        }
        String value = unquote(parser.getTokenStream().getText(name.getStart(), name.getStop()));
        BehaviorObject result = unnamedRange(type, name.getStart(), name.getStop(), UmiTypes.Instance);
        if (catalog != null) {
            result.setObjectPath(result.getObjectPath() + catalog + "/");
        }
        result.setObjectPath(result.getObjectPath() + value + "/");
        result.setObjectName(new ObjectName(catalog, null, value));
        return result;
    }

    private BehaviorObject unnamedRange(TargetType type, Token start, Token stop, UmiTypes scope) {
        BehaviorObject result = objects.instanceObject(type, start, stop, "");
        result.setObjectPath(unnamed(type, start, scope).getObjectPath());
        result.setObjectName(new ObjectName(null, null, null));
        endPosition(result, stop);
        return result;
    }

    private BehaviorObject qualifiedName(TargetType type, SqlServerParser.Id_Context schema, SqlServerParser.Id_Context name) {
        if (schema == null) {
            return object(type, name);
        }
        String schemaName = unquote(schema.getText());
        String objectName = unquote(name.getText());
        BehaviorObject result = objects.object(type, schema.getStart(), name.getStop(), List.of(schemaName, objectName));
        result.setObjectName(new ObjectName(level(UmiTypes.Catalog), schemaName, objectName));
        endPosition(result, name.getStop());
        return result;
    }

    private List<BehaviorObject> policyDependencies(ParserRuleContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (SqlServerParser.Security_predicate_functionContext function : descendants(ctx, SqlServerParser.Security_predicate_functionContext.class)) {
            targets.add(qualifiedName(TargetType.Function, function.id_(0), function.id_(1)));
        }
        for (SqlServerParser.Table_nameContext table : descendants(ctx, SqlServerParser.Table_nameContext.class)) {
            targets.add(object(TargetType.Table, table));
        }
        targets.sort(Comparator.comparingInt(BehaviorObject::getStartLine).thenComparingInt(BehaviorObject::getStartColumn));
        return targets;
    }

    @Override
    public Void visitAlter_security_policy(SqlServerParser.Alter_security_policyContext ctx) {
        addRelation(SplitQueryType.ALTER_POLICY, BehaviorAction.ALTER,
            qualifiedName(TargetType.RowAccessPolicy, ctx.schema_name, ctx.security_policy_name), policyDependencies(ctx));
        return null;
    }

    @Override
    public Void visitCreate_certificate(SqlServerParser.Create_certificateContext ctx) {
        List<BehaviorObject> targets = objects(scopedName(TargetType.UserOrRole, ctx.user_name, level(UmiTypes.Catalog)));
        if (ctx.existing_keys() != null && ctx.existing_keys().path_to_file != null) {
            targets.add(instanceValue(TargetType.File, ctx.existing_keys().path_to_file));
        }
        targets.addAll(privateKeyFiles(ctx));
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.Certificate, ctx.certificate_name, level(UmiTypes.Catalog)), targets);
        return null;
    }

    @Override
    public Void visitAlter_certificate(SqlServerParser.Alter_certificateContext ctx) {
        addRelation(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            scopedName(TargetType.Certificate, ctx.certificate_name, level(UmiTypes.Catalog)), privateKeyFiles(ctx));
        return null;
    }

    private List<BehaviorObject> privateKeyFiles(ParserRuleContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (SqlServerParser.Private_key_optionContext option : descendants(ctx, SqlServerParser.Private_key_optionContext.class)) {
            if (option.FILE() != null) {
                targets.add(instanceValue(TargetType.File, option.STRING().getSymbol()));
            }
        }
        return targets;
    }

    @Override
    public Void visitBackup_certificate(SqlServerParser.Backup_certificateContext ctx) {
        BehaviorObject certificate = scopedName(TargetType.Certificate, ctx.certname, level(UmiTypes.Catalog));
        addRelation(SplitQueryType.ADMIN, BehaviorAction.EXPORT, instanceValue(TargetType.File, ctx.cert_file), objects(certificate));
        for (SqlServerParser.Certificate_backup_optionContext option : ctx.certificate_backup_option()) {
            if (option.FILE() != null) {
                addRelation(SplitQueryType.ADMIN, BehaviorAction.EXPORT,
                    instanceValue(TargetType.File, option.STRING().getSymbol()), objects(certificate));
            }
        }
        return null;
    }

    @Override
    public Void visitCreate_asymmetric_key(SqlServerParser.Create_asymmetric_keyContext ctx) {
        List<BehaviorObject> targets = objects(scopedName(TargetType.UserOrRole, ctx.database_principal_name, level(UmiTypes.Catalog)));
        if (ctx.FILE() != null) {
            targets.add(instanceValue(TargetType.File, ctx.STRING(0).getSymbol()));
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.AsymmetricKey, ctx.Asym_Key_Nam, level(UmiTypes.Catalog)), targets);
        return null;
    }

    @Override
    public Void visitCreate_key(SqlServerParser.Create_keyContext ctx) {
        List<BehaviorObject> targets = objects(scopedName(TargetType.UserOrRole, ctx.user_name, level(UmiTypes.Catalog)));
        targets.addAll(keyProtectors(ctx));
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.SymmetricKey, ctx.key_name, level(UmiTypes.Catalog)), targets);
        return null;
    }

    @Override
    public Void visitAlter_symmetric_key(SqlServerParser.Alter_symmetric_keyContext ctx) {
        addRelation(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            scopedName(TargetType.SymmetricKey, ctx.key_name, level(UmiTypes.Catalog)), keyProtectors(ctx));
        return null;
    }

    private List<BehaviorObject> keyProtectors(ParserRuleContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (SqlServerParser.Encryption_mechanismContext mechanism : descendants(ctx, SqlServerParser.Encryption_mechanismContext.class)) {
            addObject(targets, scopedName(TargetType.Certificate, mechanism.certificate_name, level(UmiTypes.Catalog)));
            addObject(targets, scopedName(TargetType.AsymmetricKey, mechanism.asym_key_name, level(UmiTypes.Catalog)));
            addObject(targets, scopedName(TargetType.SymmetricKey, mechanism.decrypting_Key_name, level(UmiTypes.Catalog)));
        }
        for (SqlServerParser.Decryption_mechanismContext mechanism : descendants(ctx, SqlServerParser.Decryption_mechanismContext.class)) {
            addObject(targets, scopedName(TargetType.Certificate, mechanism.certificate_name, level(UmiTypes.Catalog)));
            addObject(targets, scopedName(TargetType.AsymmetricKey, mechanism.asym_key_name, level(UmiTypes.Catalog)));
            addObject(targets, scopedName(TargetType.SymmetricKey, mechanism.decrypting_Key_name, level(UmiTypes.Catalog)));
        }
        return targets;
    }

    @Override
    public Void visitOpen_key(SqlServerParser.Open_keyContext ctx) {
        BehaviorObject key;
        if (ctx.key_name != null) {
            key = scopedName(TargetType.SymmetricKey, ctx.key_name, level(UmiTypes.Catalog));
        } else {
            key = unnamedRange(TargetType.DatabaseMasterKey, ctx.MASTER().getSymbol(), ctx.KEY().getSymbol(), UmiTypes.Catalog);
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.UNLOCK, key, keyProtectors(ctx));
        return null;
    }

    @Override
    public Void visitClose_key(SqlServerParser.Close_keyContext ctx) {
        BehaviorObject key;
        if (ctx.key_name != null) {
            key = scopedName(TargetType.SymmetricKey, ctx.key_name, level(UmiTypes.Catalog));
        } else if (ctx.ALL() != null) {
            key = unnamedRange(TargetType.SymmetricKey, ctx.ALL().getSymbol(), ctx.KEYS().getSymbol(), UmiTypes.Catalog);
        } else {
            key = unnamedRange(TargetType.DatabaseMasterKey, ctx.MASTER().getSymbol(), ctx.KEY().getSymbol(), UmiTypes.Catalog);
        }
        addUnary(SplitQueryType.ADMIN, BehaviorAction.LOCK, key);
        return null;
    }

    @Override
    public Void visitAlter_master_key_sql_server(SqlServerParser.Alter_master_key_sql_serverContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.SERVICE() != null) {
            targets.add(unnamedRange(TargetType.ServiceMasterKey, ctx.SERVICE().getSymbol(), ctx.KEY(1).getSymbol(), UmiTypes.Instance));
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            unnamedRange(TargetType.DatabaseMasterKey, ctx.MASTER(0).getSymbol(), ctx.KEY(0).getSymbol(), UmiTypes.Catalog), targets);
        return null;
    }

    @Override
    public Void visitCreate_database_encryption_key(SqlServerParser.Create_database_encryption_keyContext ctx) {
        TargetType protector = TargetType.Certificate;
        if (ctx.ASYMMETRIC() != null) {
            protector = TargetType.AsymmetricKey;
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            unnamedRange(TargetType.DatabaseEncryptionKey, ctx.DATABASE().getSymbol(), ctx.KEY(0).getSymbol(), UmiTypes.Catalog),
            objects(scopedName(protector, ctx.id_(), "master")));
        return null;
    }

    @Override
    public Void visitAlter_database_encryption_key(SqlServerParser.Alter_database_encryption_keyContext ctx) {
        TargetType protector = TargetType.Certificate;
        if (ctx.ASYMMETRIC() != null) {
            protector = TargetType.AsymmetricKey;
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            unnamedRange(TargetType.DatabaseEncryptionKey, ctx.DATABASE().getSymbol(), ctx.KEY(0).getSymbol(), UmiTypes.Catalog),
            objects(scopedName(protector, ctx.id_(), "master")));
        return null;
    }

    @Override
    public Void visitCreate_column_encryption_key(SqlServerParser.Create_column_encryption_keyContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (SqlServerParser.Column_encryption_key_valueContext value : ctx.column_encryption_key_value()) {
            targets.add(scopedName(TargetType.ColumnMasterKey, value.id_(), level(UmiTypes.Catalog)));
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.ColumnEncryptionKey, ctx.column_encryption_key, level(UmiTypes.Catalog)), targets);
        return null;
    }

    @Override
    public Void visitAlter_column_encryption_key(SqlServerParser.Alter_column_encryption_keyContext ctx) {
        SqlServerParser.Id_Context protector = ctx.id_(ctx.id_().size() - 1);
        if (ctx.column_encryption_key_value() != null) {
            protector = ctx.column_encryption_key_value().id_();
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            scopedName(TargetType.ColumnEncryptionKey, ctx.column_encryption_key, level(UmiTypes.Catalog)),
            objects(scopedName(TargetType.ColumnMasterKey, protector, level(UmiTypes.Catalog))));
        return null;
    }

    @Override
    public Void visitAdd_signature(SqlServerParser.Add_signatureContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (SqlServerParser.Signature_sourceContext source : ctx.signature_source()) {
            TargetType type = TargetType.Certificate;
            if (source.ASYMMETRIC() != null) {
                type = TargetType.AsymmetricKey;
            }
            targets.add(scopedName(type, source.id_(), level(UmiTypes.Catalog)));
        }
        addRelation(SplitQueryType.ADMIN_PROG_OBJ, BehaviorAction.ALTER,
            qualifiedName(TargetType.ProgramObject, ctx.schema_name, ctx.module_name), targets);
        return null;
    }

    @Override
    public Void visitDrop_signature(SqlServerParser.Drop_signatureContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        TargetType type = null;
        for (ParseTree child : ctx.children) {
            if (child instanceof TerminalNode terminal) {
                if (terminal.getSymbol().getType() == SqlServerParser.CERTIFICATE) {
                    type = TargetType.Certificate;
                } else if (terminal.getSymbol().getType() == SqlServerParser.ASYMMETRIC) {
                    type = TargetType.AsymmetricKey;
                }
            } else if (type != null && child instanceof SqlServerParser.Id_Context name) {
                targets.add(scopedName(type, name, level(UmiTypes.Catalog)));
            }
        }
        addRelation(SplitQueryType.ADMIN_PROG_OBJ, BehaviorAction.ALTER,
            qualifiedName(TargetType.ProgramObject, ctx.schema_name, ctx.module_name), targets);
        return null;
    }

    @Override
    public Void visitDrop_login(SqlServerParser.Drop_loginContext ctx) {
        addUnary(SplitQueryType.DROP_USER, BehaviorAction.DROP,
            scopedName(TargetType.User, ctx.login_name, null));
        return null;
    }

    @Override
    public Void visitDrop_user(SqlServerParser.Drop_userContext ctx) {
        addUnary(SplitQueryType.DROP_USER, BehaviorAction.DROP,
            scopedName(TargetType.User, ctx.user_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_server_role(SqlServerParser.Drop_server_roleContext ctx) {
        addUnary(SplitQueryType.DROP_ROLE, BehaviorAction.DROP,
            scopedName(TargetType.Role, ctx.role_name, null));
        return null;
    }

    @Override
    public Void visitDrop_db_role(SqlServerParser.Drop_db_roleContext ctx) {
        addUnary(SplitQueryType.DROP_ROLE, BehaviorAction.DROP,
            scopedName(TargetType.Role, ctx.role_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_application_role(SqlServerParser.Drop_application_roleContext ctx) {
        addUnary(SplitQueryType.DROP_ROLE, BehaviorAction.DROP,
            scopedName(TargetType.Role, ctx.rolename, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_certificate(SqlServerParser.Drop_certificateContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP,
            scopedName(TargetType.Certificate, ctx.certificate_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_asymmetric_key(SqlServerParser.Drop_asymmetric_keyContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP,
            scopedName(TargetType.AsymmetricKey, ctx.key_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitAlter_asymmetric_key(SqlServerParser.Alter_asymmetric_keyContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            scopedName(TargetType.AsymmetricKey, ctx.Asym_Key_Name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_symmetric_key(SqlServerParser.Drop_symmetric_keyContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP,
            scopedName(TargetType.SymmetricKey, ctx.symmetric_key_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitCreate_column_master_key(SqlServerParser.Create_column_master_keyContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.ColumnMasterKey, ctx.key_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_column_master_key(SqlServerParser.Drop_column_master_keyContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP,
            scopedName(TargetType.ColumnMasterKey, ctx.key_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_column_encryption_key(SqlServerParser.Drop_column_encryption_keyContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP,
            scopedName(TargetType.ColumnEncryptionKey, ctx.key_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitCreate_credential(SqlServerParser.Create_credentialContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.Credential, ctx.credential_name, null));
        return null;
    }

    @Override
    public Void visitAlter_credential(SqlServerParser.Alter_credentialContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            scopedName(TargetType.Credential, ctx.credential_name, null));
        return null;
    }

    @Override
    public Void visitDrop_credential(SqlServerParser.Drop_credentialContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP,
            scopedName(TargetType.Credential, ctx.credential_name, null));
        return null;
    }

    @Override
    public Void visitCreate_database_scoped_credential(SqlServerParser.Create_database_scoped_credentialContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.Credential, ctx.credential_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitAlter_database_scoped_credential(SqlServerParser.Alter_database_scoped_credentialContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            scopedName(TargetType.Credential, ctx.credential_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_database_scoped_credential(SqlServerParser.Drop_database_scoped_credentialContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP,
            scopedName(TargetType.Credential, ctx.credential_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitCreate_master_key_sql_server(SqlServerParser.Create_master_key_sql_serverContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            unnamedRange(TargetType.DatabaseMasterKey, ctx.MASTER().getSymbol(), ctx.KEY().getSymbol(), UmiTypes.Catalog));
        return null;
    }

    @Override
    public Void visitDrop_master_key(SqlServerParser.Drop_master_keyContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP,
            unnamedRange(TargetType.DatabaseMasterKey, ctx.MASTER().getSymbol(), ctx.KEY().getSymbol(), UmiTypes.Catalog));
        return null;
    }

    @Override
    public Void visitAlter_service_master_key(SqlServerParser.Alter_service_master_keyContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            unnamedRange(TargetType.ServiceMasterKey, ctx.SERVICE().getSymbol(), ctx.KEY().getSymbol(), UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitDrop_database_encryption_key(SqlServerParser.Drop_database_encryption_keyContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP,
            unnamedRange(TargetType.DatabaseEncryptionKey, ctx.DATABASE().getSymbol(), ctx.KEY().getSymbol(), UmiTypes.Catalog));
        return null;
    }

    @Override
    public Void visitBackup_master_key(SqlServerParser.Backup_master_keyContext ctx) {
        BehaviorObject key = unnamedRange(TargetType.DatabaseMasterKey, ctx.MASTER().getSymbol(), ctx.KEY().getSymbol(), UmiTypes.Catalog);
        BehaviorObject file = instanceValue(TargetType.File, ctx.master_key_backup_file);
        addRelation(SplitQueryType.ADMIN, BehaviorAction.EXPORT, file, objects(key));
        return null;
    }

    @Override
    public Void visitRestore_master_key(SqlServerParser.Restore_master_keyContext ctx) {
        BehaviorObject key = unnamedRange(TargetType.DatabaseMasterKey, ctx.MASTER().getSymbol(), ctx.KEY().getSymbol(), UmiTypes.Catalog);
        BehaviorObject file = instanceValue(TargetType.File, ctx.STRING(0).getSymbol());
        addRelation(SplitQueryType.ADMIN, BehaviorAction.RESTORE, key, objects(file));
        return null;
    }

    @Override
    public Void visitBackup_service_master_key(SqlServerParser.Backup_service_master_keyContext ctx) {
        BehaviorObject key = unnamedRange(TargetType.ServiceMasterKey, ctx.SERVICE().getSymbol(), ctx.KEY().getSymbol(), UmiTypes.Instance);
        BehaviorObject file = instanceValue(TargetType.File, ctx.service_master_key_backup_file);
        addRelation(SplitQueryType.ADMIN, BehaviorAction.EXPORT, file, objects(key));
        return null;
    }

    @Override
    public Void visitRestore_service_master_key(SqlServerParser.Restore_service_master_keyContext ctx) {
        BehaviorObject key = unnamedRange(TargetType.ServiceMasterKey, ctx.SERVICE().getSymbol(), ctx.KEY().getSymbol(), UmiTypes.Instance);
        BehaviorObject file = instanceValue(TargetType.File, ctx.STRING(0).getSymbol());
        addRelation(SplitQueryType.ADMIN, BehaviorAction.RESTORE, key, objects(file));
        return null;
    }

    @Override
    public Void visitCreate_server_audit(SqlServerParser.Create_server_auditContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.FILEPATH() != null) {
            targets.add(instanceValue(TargetType.File, ctx.STRING().getSymbol()));
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE, scopedName(TargetType.Audit, ctx.audit_name, null), targets);
        return null;
    }

    @Override
    public Void visitAlter_server_audit(SqlServerParser.Alter_server_auditContext ctx) {
        BehaviorObject audit = scopedName(TargetType.Audit, ctx.audit_name, null);
        if (ctx.new_audit_name != null) {
            addRelation(SplitQueryType.ADMIN, BehaviorAction.RENAME, audit, objects(scopedName(TargetType.Audit, ctx.new_audit_name, null)));
            return null;
        }
        List<BehaviorObject> targets = new ArrayList<>();
        for (SqlServerParser.Audit_file_optionContext option : descendants(ctx, SqlServerParser.Audit_file_optionContext.class)) {
            if (option.FILEPATH() != null) {
                targets.add(instanceValue(TargetType.File, option.STRING().getSymbol()));
            }
        }
        boolean attributes = ctx.audit_target() != null || ctx.audit_predicate() != null || ctx.REMOVE() != null;
        for (SqlServerParser.Audit_alter_optionContext option : ctx.audit_alter_option()) {
            if (option.STATE() != null) {
                BehaviorAction action = BehaviorAction.STOP;
                if (option.on_off().ON() != null) {
                    action = BehaviorAction.START;
                }
                addUnary(SplitQueryType.ADMIN, action, audit);
            } else {
                attributes = true;
            }
        }
        if (attributes) {
            addRelation(SplitQueryType.ADMIN, BehaviorAction.ALTER, audit, targets);
        }
        return null;
    }

    @Override
    public Void visitCreate_server_audit_specification(SqlServerParser.Create_server_audit_specificationContext ctx) {
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE, scopedName(TargetType.AuditSpecification, ctx.audit_specification_name, null),
            objects(scopedName(TargetType.Audit, ctx.audit_name, null)));
        return null;
    }

    @Override
    public Void visitAlter_server_audit_specification(SqlServerParser.Alter_server_audit_specificationContext ctx) {
        addRelation(SplitQueryType.ADMIN, BehaviorAction.ALTER, scopedName(TargetType.AuditSpecification, ctx.audit_specification_name, null),
            objects(scopedName(TargetType.Audit, ctx.audit_name, null)));
        return null;
    }

    @Override
    public Void visitCreate_database_audit_specification(SqlServerParser.Create_database_audit_specificationContext ctx) {
        List<BehaviorObject> targets = objects(scopedName(TargetType.Audit, ctx.audit_name, null));
        targets.addAll(auditDependencies(ctx));
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.AuditSpecification, ctx.audit_specification_name, level(UmiTypes.Catalog)), targets);
        return null;
    }

    @Override
    public Void visitAlter_database_audit_specification(SqlServerParser.Alter_database_audit_specificationContext ctx) {
        List<BehaviorObject> targets = objects(scopedName(TargetType.Audit, ctx.audit_name, null));
        targets.addAll(auditDependencies(ctx));
        addRelation(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            scopedName(TargetType.AuditSpecification, ctx.audit_specification_name, level(UmiTypes.Catalog)), targets);
        return null;
    }

    private List<BehaviorObject> auditDependencies(ParserRuleContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (SqlServerParser.Audit_action_specificationContext spec : descendants(ctx, SqlServerParser.Audit_action_specificationContext.class)) {
            TargetType type = TargetType.SchemaObject;
            if (spec.audit_class_name() != null) {
                if (spec.audit_class_name().SCHEMA() != null) {
                    type = TargetType.Schema;
                } else if (spec.audit_class_name().TABLE() != null) {
                    type = TargetType.Table;
                }
            }
            targets.add(object(type, spec.audit_securable()));
            for (SqlServerParser.Principal_idContext principal : spec.principal_id()) {
                targets.add(scopedName(TargetType.UserOrRole, principal, level(UmiTypes.Catalog)));
            }
        }
        return targets;
    }

    @Override
    public Void visitCreate_or_alter_event_session(SqlServerParser.Create_or_alter_event_sessionContext ctx) {
        BehaviorAction action = BehaviorAction.ALTER;
        if (ctx.CREATE() != null) {
            action = BehaviorAction.CREATE;
        } else if (ctx.START() != null) {
            action = BehaviorAction.START;
        } else if (ctx.STOP() != null) {
            action = BehaviorAction.STOP;
        }
        List<BehaviorObject> targets = new ArrayList<>();
        for (SqlServerParser.Xe_add_targetContext target : descendants(ctx, SqlServerParser.Xe_add_targetContext.class)) {
            List<String> name = names(target.xe_object_name());
            if (name.size() < 2 || !"package0".equalsIgnoreCase(name.get(name.size() - 2))
                || !"event_file".equalsIgnoreCase(name.get(name.size() - 1))) {
                continue;
            }
            for (SqlServerParser.Xe_parameterContext parameter : target.xe_parameter()) {
                if ("filename".equalsIgnoreCase(unquote(parameter.id_().getText()))) {
                    SqlServerParser.Xe_literalContext literal = parameter.xe_literal();
                    while (literal.xe_literal() != null) {
                        literal = literal.xe_literal();
                    }
                    if (literal.STRING() != null) {
                        targets.add(instanceValue(TargetType.File, literal.STRING().getSymbol()));
                    }
                }
            }
        }
        addRelation(SplitQueryType.ADMIN_PERFORMANCE, action, scopedName(TargetType.EventSession, ctx.event_session_name, null), targets);
        return null;
    }

    @Override
    public Void visitCreate_availability_group(SqlServerParser.Create_availability_groupContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        // Only direct database identifiers belong to the local instance; remote replica strings do not.
        if (ctx.DATABASE() != null) {
            for (SqlServerParser.Id_Context database : ctx.id_()) {
                if (database != ctx.group_name) {
                    targets.add(object(TargetType.Catalog, database));
                }
            }
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE, scopedName(TargetType.AvailabilityGroup, ctx.group_name, null), targets);
        return null;
    }

    @Override
    public Void visitAlter_availability_group(SqlServerParser.Alter_availability_groupContext ctx) {
        BehaviorObject group = scopedName(TargetType.AvailabilityGroup, ctx.alter_availability_group_start().group_name, null);
        SqlServerParser.Alter_availability_group_optionsContext option = ctx.alter_availability_group_options();
        if (option.GRANT() != null || option.DENY() != null) {
            BehaviorAction action = BehaviorAction.GRANT;
            if (option.DENY() != null) {
                action = BehaviorAction.DENY;
            }
            addRelation(SplitQueryType.ADMIN, action,
                unnamedRange(TargetType.Instance, option.CREATE().getSymbol(), option.DATABASE().getSymbol(), UmiTypes.Instance), objects(group));
            return null;
        }
        BehaviorAction action = BehaviorAction.ALTER;
        if (option.FAILOVER() != null || option.FORCE_FAILOVER_ALLOW_DATA_LOSS() != null) {
            action = BehaviorAction.SWITCH;
        } else if (option.OFFLINE() != null) {
            action = BehaviorAction.STOP;
        }
        List<BehaviorObject> targets = new ArrayList<>();
        if (option.DATABASE() != null && option.id_() != null) {
            targets.add(object(TargetType.Catalog, option.id_()));
        }
        addRelation(SplitQueryType.ADMIN, action, group, targets);
        return null;
    }

    @Override
    public Void visitCreate_endpoint(SqlServerParser.Create_endpointContext ctx) {
        List<BehaviorObject> targets = objects(scopedName(TargetType.User, ctx.login, null));
        targets.addAll(endpointCertificates(ctx));
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE, scopedName(TargetType.Endpoint, ctx.endpointname, null), targets);
        return null;
    }

    @Override
    public Void visitAlter_endpoint(SqlServerParser.Alter_endpointContext ctx) {
        BehaviorObject endpoint = scopedName(TargetType.Endpoint, ctx.endpointname, null);
        if (ctx.STATE() != null) {
            BehaviorAction action = BehaviorAction.STOP;
            if (ctx.STARTED() != null) {
                action = BehaviorAction.START;
            }
            addUnary(SplitQueryType.ADMIN, action, endpoint);
        }
        if (ctx.STATE() == null || ctx.login != null || ctx.endpoint_protocol() != null || ctx.endpoint_listener_clause() != null) {
            List<BehaviorObject> targets = objects(scopedName(TargetType.User, ctx.login, null));
            targets.addAll(endpointCertificates(ctx));
            addRelation(SplitQueryType.ADMIN, BehaviorAction.ALTER, endpoint, targets);
        }
        return null;
    }

    private List<BehaviorObject> endpointCertificates(ParserRuleContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (SqlServerParser.Endpoint_authentication_clauseContext auth : descendants(ctx, SqlServerParser.Endpoint_authentication_clauseContext.class)) {
            addObject(targets, scopedName(TargetType.Certificate, auth.cert_name, "master"));
        }
        return targets;
    }

    @Override
    public Void visitAlter_table(SqlServerParser.Alter_tableContext ctx) {
        // Other ALTER TABLE forms still need their own dependency analysis.
        if (ctx.CHANGE_TRACKING() != null) {
            addUnary(SplitQueryType.ALTER_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.table_name(0)));
        }
        return null;
    }

    @Override
    public Void visitMessage_statement(SqlServerParser.Message_statementContext ctx) {
        List<BehaviorObject> targets = objects(scopedName(TargetType.UserOrRole, ctx.owner_name, level(UmiTypes.Catalog)));
        if (ctx.COLLECTION() != null) {
            List<SqlServerParser.Id_Context> ids = ctx.id_();
            SqlServerParser.Id_Context schema = null;
            int nameIndex = ids.size() - 1;
            if (ctx.DOT() != null) {
                schema = ids.get(nameIndex - 1);
            }
            targets.add(qualifiedName(TargetType.XmlSchemaCollection, schema, ids.get(nameIndex)));
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.BrokerMessageType, ctx.message_type_name, level(UmiTypes.Catalog)), targets);
        return null;
    }

    @Override
    public Void visitAlter_message_type(SqlServerParser.Alter_message_typeContext ctx) {
        addRelation(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            scopedName(TargetType.BrokerMessageType, ctx.message_type_name, level(UmiTypes.Catalog)),
            objects(object(TargetType.XmlSchemaCollection, ctx.schema_collection_name)));
        return null;
    }

    @Override
    public Void visitCreate_contract(SqlServerParser.Create_contractContext ctx) {
        List<BehaviorObject> targets = objects(scopedName(TargetType.UserOrRole, ctx.owner_name, level(UmiTypes.Catalog)));
        for (ParseTree child : ctx.children) {
            if (child instanceof SqlServerParser.Id_Context name && name != ctx.owner_name) {
                targets.add(scopedName(TargetType.BrokerMessageType, name, level(UmiTypes.Catalog)));
            } else if (child instanceof TerminalNode terminal && terminal.getSymbol().getType() == SqlServerParser.DEFAULT) {
                targets.add(catalogValue(TargetType.BrokerMessageType, terminal.getSymbol()));
            }
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.BrokerContract, ctx.contract_name(), level(UmiTypes.Catalog)), targets);
        return null;
    }

    @Override
    public Void visitCreate_queue(SqlServerParser.Create_queueContext ctx) {
        BehaviorObject queue = object(TargetType.Queue, ctx.full_table_name());
        if (queue == null) {
            queue = object(TargetType.Queue, ctx.queue_name);
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE, queue, queueDependencies(ctx.queue_settings()));
        return null;
    }

    @Override
    public Void visitAlter_queue(SqlServerParser.Alter_queueContext ctx) {
        BehaviorObject queue = object(TargetType.Queue, ctx.full_table_name());
        if (queue == null) {
            queue = object(TargetType.Queue, ctx.queue_name);
        }
        BehaviorAction action = BehaviorAction.ALTER;
        if (ctx.queue_action() != null) {
            action = BehaviorAction.OPTIMIZE;
            if (ctx.queue_action().MOVE() != null) {
                action = BehaviorAction.MOVE;
            }
        }
        addRelation(SplitQueryType.ADMIN, action, queue, queueDependencies(ctx.queue_settings()));
        return null;
    }

    private List<BehaviorObject> queueDependencies(SqlServerParser.Queue_settingsContext settings) {
        if (settings == null) {
            return List.of();
        }
        List<BehaviorObject> targets = objects(object(TargetType.Procedure, settings.func_proc_name_database_schema()));
        if (settings.user_name != null) {
            targets.add(catalogValue(TargetType.User, settings.user_name));
        } else if (settings.OWNER() != null) {
            targets.add(unnamed(TargetType.User, settings.OWNER().getSymbol(), UmiTypes.Catalog));
        } else if (settings.SELF() != null) {
            targets.add(unnamed(TargetType.User, settings.SELF().getSymbol(), UmiTypes.Catalog));
        }
        return targets;
    }

    @Override
    public Void visitDrop_queue(SqlServerParser.Drop_queueContext ctx) {
        // Optional grammar labels are ambiguous for two-part names; resolve by arity.
        List<String> parts = names(ctx);
        Token start = ctx.id_(0).getStart();
        BehaviorObject queue = objects.object(TargetType.Queue, start, ctx.queue_name.getStop(), parts);
        String catalog = level(UmiTypes.Catalog);
        String schema = level(UmiTypes.Schema);
        if (parts.size() >= 2) {
            schema = parts.get(parts.size() - 2);
        }
        if (parts.size() >= 3) {
            catalog = parts.get(parts.size() - 3);
        }
        queue.setObjectName(new ObjectName(catalog, schema, parts.get(parts.size() - 1)));
        endPosition(queue, ctx.queue_name.getStop());
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP, queue);
        return null;
    }

    @Override
    public Void visitCreate_service(SqlServerParser.Create_serviceContext ctx) {
        List<BehaviorObject> targets = objects(scopedName(TargetType.UserOrRole, ctx.owner_name, level(UmiTypes.Catalog)),
            qualifiedName(TargetType.Queue, ctx.schema_name, ctx.queue_name));
        for (ParseTree child : ctx.children) {
            if (child instanceof SqlServerParser.Id_Context name && name != ctx.create_service_name && name != ctx.owner_name
                && name != ctx.schema_name && name != ctx.queue_name) {
                targets.add(scopedName(TargetType.BrokerContract, name, level(UmiTypes.Catalog)));
            } else if (child instanceof TerminalNode terminal && terminal.getSymbol().getType() == SqlServerParser.DEFAULT) {
                targets.add(catalogValue(TargetType.BrokerContract, terminal.getSymbol()));
            }
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.BrokerService, ctx.create_service_name, level(UmiTypes.Catalog)), targets);
        return null;
    }

    @Override
    public Void visitAlter_service(SqlServerParser.Alter_serviceContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.queue_name != null) {
            targets.add(qualifiedName(TargetType.Queue, ctx.schema_name, ctx.queue_name));
        }
        for (SqlServerParser.Opt_arg_clauseContext option : ctx.opt_arg_clause()) {
            targets.add(scopedName(TargetType.BrokerContract, option.modified_contract_name, level(UmiTypes.Catalog)));
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            scopedName(TargetType.BrokerService, ctx.modified_service_name, level(UmiTypes.Catalog)), targets);
        return null;
    }

    @Override
    public Void visitCreate_route(SqlServerParser.Create_routeContext ctx) {
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.BrokerRoute, ctx.route_name, level(UmiTypes.Catalog)),
            objects(scopedName(TargetType.UserOrRole, ctx.owner_name, level(UmiTypes.Catalog))));
        return null;
    }

    @Override
    public Void visitCreate_remote_service_binding(SqlServerParser.Create_remote_service_bindingContext ctx) {
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            scopedName(TargetType.RemoteServiceBinding, ctx.binding_name, level(UmiTypes.Catalog)),
            objects(scopedName(TargetType.UserOrRole, ctx.owner_name, level(UmiTypes.Catalog)),
                scopedName(TargetType.User, ctx.user_name, level(UmiTypes.Catalog))));
        return null;
    }

    @Override
    public Void visitAlter_remote_service_binding(SqlServerParser.Alter_remote_service_bindingContext ctx) {
        addRelation(SplitQueryType.ADMIN, BehaviorAction.ALTER,
            scopedName(TargetType.RemoteServiceBinding, ctx.binding_name, level(UmiTypes.Catalog)),
            objects(scopedName(TargetType.User, ctx.user_name, level(UmiTypes.Catalog))));
        return null;
    }

    @Override
    public Void visitCreate_or_alter_broker_priority(SqlServerParser.Create_or_alter_broker_priorityContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        TargetType type = null;
        for (ParseTree child : ctx.children) {
            if (child instanceof TerminalNode terminal) {
                switch (terminal.getSymbol().getType()) {
                    case SqlServerParser.CONTRACT_NAME -> type = TargetType.BrokerContract;
                    case SqlServerParser.LOCAL_SERVICE_NAME -> type = TargetType.BrokerService;
                    case SqlServerParser.REMOTE_SERVICE_NAME, SqlServerParser.PRIORITY_LEVEL -> type = null;
                    default -> { }
                }
            } else if (type != null && child instanceof SqlServerParser.Id_Context name) {
                targets.add(scopedName(type, name, level(UmiTypes.Catalog)));
            }
        }
        BehaviorAction action = BehaviorAction.ALTER;
        if (ctx.CREATE() != null) {
            action = BehaviorAction.CREATE;
        }
        addRelation(SplitQueryType.ADMIN, action,
            scopedName(TargetType.BrokerPriority, ctx.ConversationPriorityName, level(UmiTypes.Catalog)), targets);
        return null;
    }

    @Override
    public Void visitCreate_event_notification(SqlServerParser.Create_event_notificationContext ctx) {
        BehaviorObject queue = object(TargetType.Queue, ctx.full_table_name());
        List<BehaviorObject> targets = objects(queue);
        if ("current database".equalsIgnoreCase(unquote(ctx.STRING(1).getText()))) {
            targets.add(catalogValue(TargetType.BrokerService, ctx.STRING(0).getSymbol()));
        }
        addRelation(SplitQueryType.ADMIN, BehaviorAction.CREATE,
            notification(ctx.event_notification_name, ctx.SERVER() != null, queue), targets);
        return null;
    }

    @Override
    public Void visitDrop_event_notifications(SqlServerParser.Drop_event_notificationsContext ctx) {
        BehaviorObject queue = object(TargetType.Queue, ctx.full_table_name());
        for (SqlServerParser.Id_Context name : ctx.id_()) {
            addRelation(SplitQueryType.ADMIN, BehaviorAction.DROP, notification(name, ctx.SERVER() != null, queue), objects(queue));
        }
        return null;
    }

    private BehaviorObject notification(ParserRuleContext name, boolean server, BehaviorObject queue) {
        String catalog = level(UmiTypes.Catalog);
        if (server) {
            catalog = null;
        }
        BehaviorObject notification = scopedName(TargetType.EventNotification, name, catalog);
        if (queue != null) {
            notification.setObjectPath(queue.getObjectPath() + unquote(name.getText()) + "/");
            notification.setObjectName(new ObjectName(queue.getObjectName().getCatalog(), queue.getObjectName().getSchema(), unquote(name.getText())));
        }
        return notification;
    }

    @Override
    public Void visitEnd_conversation(SqlServerParser.End_conversationContext ctx) {
        BehaviorAction action = BehaviorAction.STOP;
        if (ctx.CLEANUP() != null) {
            action = BehaviorAction.PURGE;
        }
        addUnary(SplitQueryType.ADMIN, action, unnamed(TargetType.BrokerConversation, ctx.conversation_handle, UmiTypes.Catalog));
        return null;
    }

    @Override
    public Void visitBegin_conversation_timer(SqlServerParser.Begin_conversation_timerContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.CONFIGURE,
            unnamed(TargetType.BrokerConversation, ctx.LOCAL_ID().getSymbol(), UmiTypes.Catalog));
        return null;
    }

    @Override
    public Void visitMove_conversation(SqlServerParser.Move_conversationContext ctx) {
        addRelation(SplitQueryType.ADMIN, BehaviorAction.MOVE,
            unnamed(TargetType.BrokerConversation, ctx.LOCAL_ID(0).getSymbol(), UmiTypes.Catalog),
            objects(unnamed(TargetType.BrokerConversationGroup, ctx.LOCAL_ID(1).getSymbol(), UmiTypes.Catalog)));
        return null;
    }

    private BehaviorObject catalogValue(TargetType type, Token token) {
        BehaviorObject result = unnamed(type, token, UmiTypes.Catalog);
        String name = unquote(token.getText());
        result.setObjectPath(result.getObjectPath() + name + "/");
        result.setObjectName(new ObjectName(level(UmiTypes.Catalog), null, name));
        return result;
    }

    @Override
    public Void visitDrop_server_audit(SqlServerParser.Drop_server_auditContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP, scopedName(TargetType.Audit, ctx.audit_name, null));
        return null;
    }

    @Override
    public Void visitDrop_server_audit_specification(SqlServerParser.Drop_server_audit_specificationContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP, scopedName(TargetType.AuditSpecification, ctx.audit_specification_name, null));
        return null;
    }

    @Override
    public Void visitDrop_database_audit_specification(SqlServerParser.Drop_database_audit_specificationContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP, scopedName(TargetType.AuditSpecification, ctx.audit_specification_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_event_session(SqlServerParser.Drop_event_sessionContext ctx) {
        addUnary(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.DROP, scopedName(TargetType.EventSession, ctx.event_session_name, null));
        return null;
    }

    @Override
    public Void visitDrop_availability_group(SqlServerParser.Drop_availability_groupContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP, scopedName(TargetType.AvailabilityGroup, ctx.group_name, null));
        return null;
    }

    @Override
    public Void visitDrop_endpoint(SqlServerParser.Drop_endpointContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP, scopedName(TargetType.Endpoint, ctx.endPointName, null));
        return null;
    }

    @Override
    public Void visitDrop_message_type(SqlServerParser.Drop_message_typeContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP, scopedName(TargetType.BrokerMessageType, ctx.message_type_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_contract(SqlServerParser.Drop_contractContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP, scopedName(TargetType.BrokerContract, ctx.dropped_contract_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_service(SqlServerParser.Drop_serviceContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP, scopedName(TargetType.BrokerService, ctx.dropped_service_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_route(SqlServerParser.Drop_routeContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP, scopedName(TargetType.BrokerRoute, ctx.route_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitAlter_route(SqlServerParser.Alter_routeContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.ALTER, scopedName(TargetType.BrokerRoute, ctx.route_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_remote_service_binding(SqlServerParser.Drop_remote_service_bindingContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP, scopedName(TargetType.RemoteServiceBinding, ctx.binding_name, level(UmiTypes.Catalog)));
        return null;
    }

    @Override
    public Void visitDrop_broker_priority(SqlServerParser.Drop_broker_priorityContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.DROP, scopedName(TargetType.BrokerPriority, ctx.ConversationPriorityName, level(UmiTypes.Catalog)));
        return null;
    }

    private Token startToken(ParseTree node) {
        if (node instanceof TerminalNode terminal) {
            return terminal.getSymbol();
        }
        return ((ParserRuleContext) node).getStart();
    }

    private BehaviorObject unnamed(TargetType type, Token token, UmiTypes scope) {
        BehaviorObject result = objects.unnamedObject(type, token, scope);
        result.setObjectName(new ObjectName(null, null, null));
        endPosition(result, token);
        return result;
    }

    private BehaviorObject currentCatalog(Token token) {
        // The path is known from levels, but the source token does not spell the catalog's name.
        return unnamed(TargetType.Catalog, token, UmiTypes.Catalog);
    }

    private BehaviorObject instanceValue(TargetType type, Token token) {
        if (token.getType() == SqlServerParser.LOCAL_ID || token.getType() == SqlServerParser.DECIMAL) {
            return unnamed(type, token, UmiTypes.Instance);
        }
        return instanceNamed(type, token, unquote(token.getText()));
    }

    private BehaviorObject tableFromString(Token token, String catalog) {
        if (token.getType() == SqlServerParser.DECIMAL || token.getType() == SqlServerParser.LOCAL_ID || catalog == null) {
            BehaviorObject result = unnamed(TargetType.Table, token, UmiTypes.Instance);
            if (catalog != null) {
                result.setObjectPath(result.getObjectPath() + catalog + "/");
            }
            result.setObjectName(new ObjectName(catalog, null, null));
            return result;
        }
        String value = token.getText();
        if (token.getType() == SqlServerParser.STRING) {
            value = unquote(value);
        }
        // Lex identifier components, not SQL statements. Quoted dots and doubled delimiters stay in a name.
        SqlServerLexer lexer = new SqlServerLexer(CharStreams.fromString(value));
        List<String> parts = new ArrayList<>();
        StringBuilder part = new StringBuilder();
        for (Token component : lexer.getAllTokens()) {
            if (component.getChannel() != Token.DEFAULT_CHANNEL) {
                continue;
            }
            if (component.getType() == SqlServerParser.DOT) {
                parts.add(part.toString());
                part.setLength(0);
            } else {
                part.append(unquote(component.getText()));
            }
        }
        parts.add(part.toString());
        String name = parts.get(parts.size() - 1);
        String schema = level(UmiTypes.Schema);
        if (parts.size() >= 2 && !parts.get(parts.size() - 2).isEmpty()) {
            schema = parts.get(parts.size() - 2);
        }
        if (parts.size() >= 3) {
            catalog = parts.get(parts.size() - 3);
        }
        BehaviorObject result = objects.object(TargetType.Table, token, List.of(catalog, schema, name));
        result.setObjectName(new ObjectName(catalog, schema, name));
        endPosition(result, token);
        return result;
    }

    @Override
    public Void visitCreate_resource_pool(SqlServerParser.Create_resource_poolContext ctx) {
        addUnary(SplitQueryType.CREATE_RESOURCE_GROUP, BehaviorAction.CREATE,
            instanceValue(TargetType.ResourceGroup, startToken(ctx.getChild(3))));
        return null;
    }

    @Override
    public Void visitAlter_resource_pool(SqlServerParser.Alter_resource_poolContext ctx) {
        addUnary(SplitQueryType.ALTER_RESOURCE_GROUP, BehaviorAction.ALTER,
            instanceValue(TargetType.ResourceGroup, startToken(ctx.getChild(3))));
        return null;
    }

    @Override
    public Void visitDrop_resource_pool(SqlServerParser.Drop_resource_poolContext ctx) {
        addUnary(SplitQueryType.DROP_RESOURCE_GROUP, BehaviorAction.DROP,
            instanceValue(TargetType.ResourceGroup, startToken(ctx.getChild(3))));
        return null;
    }

    @Override
    public Void visitCreate_external_resource_pool(SqlServerParser.Create_external_resource_poolContext ctx) {
        addUnary(SplitQueryType.CREATE_RESOURCE_GROUP, BehaviorAction.CREATE,
            instanceValue(TargetType.ResourceGroup, startToken(ctx.getChild(4))));
        return null;
    }

    @Override
    public Void visitAlter_external_resource_pool(SqlServerParser.Alter_external_resource_poolContext ctx) {
        addUnary(SplitQueryType.ALTER_RESOURCE_GROUP, BehaviorAction.ALTER,
            instanceValue(TargetType.ResourceGroup, startToken(ctx.getChild(4))));
        return null;
    }

    @Override
    public Void visitDrop_external_resource_pool(SqlServerParser.Drop_external_resource_poolContext ctx) {
        addUnary(SplitQueryType.DROP_RESOURCE_GROUP, BehaviorAction.DROP,
            instanceValue(TargetType.ResourceGroup, startToken(ctx.getChild(4))));
        return null;
    }

    private void functionCall(SqlServerParser.Function_callContext ctx) {
        BehaviorObject function;
        if (ctx instanceof SqlServerParser.SCALAR_FUNCTIONContext scalar) {
            function = object(TargetType.Function, scalar.scalar_function_name());
        } else {
            Token token = ctx.getStart();
            // CAST/CONVERT are real built-in calls; grammar keywords such as CASE are not function_call nodes.
            function = instanceNamed(TargetType.Function, token, token.getText().toUpperCase(Locale.ROOT));
        }
        addUnary(SplitQueryType.SELECT, BehaviorAction.CALL, function);
    }

    private BehaviorObject identity(Token token, boolean databaseScope, String name) {
        UmiTypes scope = UmiTypes.Instance;
        if (databaseScope) {
            scope = UmiTypes.Catalog;
        }
        BehaviorObject result = objects.unnamedObject(TargetType.User, token, scope);
        if (name != null) {
            result.setObjectPath(result.getObjectPath() + name + "/");
        }
        String catalog = null;
        if (databaseScope) {
            catalog = level(UmiTypes.Catalog);
        }
        result.setObjectName(new ObjectName(catalog, null, name));
        endPosition(result, token);
        return result;
    }

    private BehaviorObject instanceNamed(TargetType type, Token token, String name) {
        BehaviorObject result = objects.instanceObject(type, token, name);
        result.setObjectName(new ObjectName(null, null, name));
        endPosition(result, token);
        return result;
    }

    private String level(UmiTypes type) {
        Object value = levels.get(type);
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    private void endPosition(BehaviorObject result, Token token) {
        String[] lines = token.getText().split("\\n", -1);
        if (lines.length > 1) {
            result.setEndLine(baseLine + token.getLine() + lines.length - 2);
            result.setEndColumn(lines[lines.length - 1].length());
        }
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> parts = names(context);
        if (parts.isEmpty()) {
            return null;
        }
        BehaviorObject result = objects.object(type, context, parts);
        String catalog = level(UmiTypes.Catalog);
        String schema = level(UmiTypes.Schema);
        String name = parts.get(parts.size() - 1);
        if (type == TargetType.Catalog) {
            catalog = null;
            schema = null;
        } else if (type == TargetType.Schema) {
            schema = null;
        } else {
            if (parts.size() >= 2) {
                schema = parts.get(parts.size() - 2);
            }
            if (parts.size() >= 3) {
                catalog = parts.get(parts.size() - 3);
            }
        }
        result.setObjectName(new ObjectName(catalog, schema, name));
        endPosition(result, context.getStop());
        return result;
    }

    private List<String> names(ParserRuleContext context) {
        List<String> names = new ArrayList<>();
        if (context == null) {
            return names;
        }
        for (SqlServerParser.Id_Context id : descendants(context, SqlServerParser.Id_Context.class)) {
            names.add(unquote(parser.getTokenStream().getText(id.getStart(), id.getStop())));
        }
        return names;
    }

    private void addUnary(SplitQueryType type, BehaviorAction action, BehaviorObject subject) {
        addRelation(type, action, subject, List.of());
    }

    private BehaviorRelation addRelation(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return null;
        }
        // A source already represented as a CREATE/INSERT/etc. dependency is not a second READ relation.
        if (action == BehaviorAction.READ) {
            for (BehaviorRelation existing : behavior.getRelations()) {
                for (BehaviorObject target : existing.getTarget()) {
                    if (target.getObjectType() == subject.getObjectType() && target.getObjectPath().equals(subject.getObjectPath())
                        && target.getStartLine() == subject.getStartLine() && target.getStartColumn() == subject.getStartColumn()) {
                        return existing;
                    }
                }
            }
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
        setType(type);
        return relation;
    }

    private List<BehaviorObject> sourceTables(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        for (SqlServerParser.Table_source_itemContext source : descendants(tree, SqlServerParser.Table_source_itemContext.class)) {
            if (source.full_table_name() != null) {
                addObject(result, object(TargetType.Table, source.full_table_name()));
            }
        }
        return result;
    }

    private SqlServerParser.Full_table_nameContext resolveAlias(SqlServerParser.Full_table_nameContext target, SqlServerParser.Table_sourcesContext sources) {
        if (target == null || sources == null) {
            return target;
        }
        String targetName = parser.getTokenStream().getText(target.getStart(), target.getStop());
        for (SqlServerParser.Table_source_itemContext source : descendants(sources, SqlServerParser.Table_source_itemContext.class)) {
            if (source.full_table_name() == null || source.as_table_alias() == null) {
                continue;
            }
            SqlServerParser.Id_Context alias = source.as_table_alias().table_alias().id_();
            String aliasName = parser.getTokenStream().getText(alias.getStart(), alias.getStop());
            if (unquote(targetName).equalsIgnoreCase(unquote(aliasName))) {
                return source.full_table_name();
            }
        }
        return target;
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
        if (value.length() > 1 && (value.charAt(0) == 'N' || value.charAt(0) == 'n') && value.charAt(1) == '\'') {
            value = value.substring(1);
        }
        if (value.length() < 2) {
            return value;
        }
        char first = value.charAt(0);
        char last = value.charAt(value.length() - 1);
        if (first == '[' && last == ']') {
            return value.substring(1, value.length() - 1).replace("]]", "]");
        }
        if (first == '"' && last == '"') {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        if (first == '\'' && last == '\'') {
            return value.substring(1, value.length() - 1).replace("''", "'");
        }
        return value;
    }
}
