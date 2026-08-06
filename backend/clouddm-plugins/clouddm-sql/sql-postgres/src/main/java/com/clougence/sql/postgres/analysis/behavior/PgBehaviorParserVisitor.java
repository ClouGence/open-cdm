/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.postgres.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.postgres.parser.PgSplitVisitor;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParserBaseVisitor;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser.*;
import com.clougence.utils.StringUtils;

final class PgBehaviorParserVisitor extends AbstractParseTreeVisitor<Void> {

    private final Parser                  parser;
    private final PostgresVersion         version;
    private final Map<UmiTypes, Object>   levels;
    private final int                     baseLine;
    private final int                     baseColumn;
    private final List<StatementBehavior> behaviors = new ArrayList<>();

    PgBehaviorParserVisitor(Parser parser, PostgresVersion version, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.version = version;
        this.levels = levels;
        this.baseLine = baseLine;
        this.baseColumn = baseColumn;
    }

    List<StatementBehavior> behaviors() {
        return behaviors;
    }

    @Override
    public Void visit(ParseTree tree) {
        SplitQueryType statementType = new PgSplitVisitor(version).visit(tree);
        PgStatementBehaviorVisitor visitor = new PgStatementBehaviorVisitor(parser, statementType, levels, baseLine, baseColumn);
        visitor.visit(tree);
        visitor.complete(tree);
        behaviors.add(visitor.behavior());
        return null;
    }
}

final class PgStatementBehaviorVisitor extends PgSqlParserBaseVisitor<Void> {

    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final Map<UmiTypes, Object>    levels;
    private final int                      baseLine;
    private final int                      baseColumn;
    private final SplitQueryType           resolvedType;
    private final StatementBehavior        behavior = new StatementBehavior();

    PgStatementBehaviorVisitor(Parser parser, SplitQueryType statementType, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.levels = levels;
        this.baseLine = Math.max(1, baseLine);
        this.baseColumn = Math.max(0, baseColumn);
        this.resolvedType = statementType == null ? SplitQueryType.UNKNOWN : statementType;
        this.behavior.setStatementType(this.resolvedType);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    void complete(ParseTree tree) {
        if (!behavior.getRelations().isEmpty())
            return;
        if (tree instanceof ParserRuleContext context) {
            if (resolvedType == SplitQueryType.SELECT) {
                addNestedUnary(BehaviorAction.READ, objects.unnamedObject(TargetType.Query, context, UmiTypes.Catalog));
            } else if (resolvedType == SplitQueryType.PERFORMANCE) {
                addNestedUnary(BehaviorAction.ANALYZE, objects.unnamedObject(TargetType.Query, context, UmiTypes.Catalog));
            } else if (resolvedType == SplitQueryType.TRANSACTION) {
                String sql = text(context).stripLeading().toUpperCase(Locale.ROOT);
                BehaviorAction action = sql.startsWith("BEGIN") || sql.startsWith("START") ? BehaviorAction.START : sql.startsWith("COMMIT") || sql
                    .startsWith("END") ? BehaviorAction.STOP : sql.startsWith("SET") ? BehaviorAction.CONFIGURE : BehaviorAction.RESET;
                addNestedUnary(action, objects.unnamedObject(TargetType.Transaction, context, UmiTypes.Catalog));
            } else {
                addNestedUnary(BehaviorAction.UNKNOWN, objects.unnamedObject(TargetType.Unknown, context, UmiTypes.Instance));
            }
        }
    }

    @Override
    public Void visitTable_ref(Table_refContext ctx) {
        if (ctx.relation_expr() != null) {
            addUnary(SplitQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.relation_expr().qualified_name()));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitInsertstmt(InsertstmtContext ctx) {
        boolean upsert = resolvedType == SplitQueryType.MERGE;
        addRelation(upsert ? SplitQueryType.MERGE : SplitQueryType.INSERT, upsert ? BehaviorAction.MERGE : BehaviorAction.INSERT, object(TargetType.Table, ctx.insert_target()
            .qualified_name()), tableReferences(ctx.insert_rest()));
        addFunctionRelations(ctx);
        return null;
    }

    @Override
    public Void visitMergestmt(MergestmtContext ctx) {
        addRelation(SplitQueryType.MERGE, BehaviorAction.MERGE, object(TargetType.Table, ctx.relation_expr_opt_alias().relation_expr().qualified_name()), tableReferences(ctx
            .table_ref()));
        addFunctionRelations(ctx);
        return null;
    }

    @Override
    public Void visitUpdatestmt(UpdatestmtContext ctx) {
        addRelation(SplitQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, ctx.relation_expr_opt_alias().relation_expr().qualified_name()), tableReferences(ctx));
        addFunctionRelations(ctx);
        return null;
    }

    @Override
    public Void visitDeletestmt(DeletestmtContext ctx) {
        addRelation(SplitQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, ctx.relation_expr_opt_alias().relation_expr().qualified_name()), tableReferences(ctx));
        addFunctionRelations(ctx);
        return null;
    }

    @Override
    public Void visitCreatestmt(CreatestmtContext ctx) {
        List<Qualified_nameContext> names = ctx.qualified_name();
        if (!names.isEmpty()) {
            List<BehaviorObject> targets = new ArrayList<>();
            for (int i = 1; i < names.size(); i++) {
                addObject(targets, object(TargetType.Table, names.get(i)));
            }
            addRelation(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, names.get(0)), targets);
        }
        addFunctionRelations(ctx);
        return null;
    }

    @Override
    public Void visitCreateasstmt(CreateasstmtContext ctx) {
        BehaviorObject subject = object(TargetType.Table, ctx.create_as_target().qualified_name());
        List<BehaviorObject> targets = tableReferences(ctx.selectstmt());
        if (ctx.selectstmt() == null && ctx.qualified_name() != null) {
            addObject(targets, object(TargetType.Table, ctx.qualified_name()));
        }
        addRelation(SplitQueryType.CREATE_TABLE, BehaviorAction.CREATE, subject, targets);
        addFunctionRelations(ctx);
        return null;
    }

    @Override
    public Void visitCreatepolicystmt(CreatepolicystmtContext ctx) {
        addRelation(SplitQueryType.CREATE_POLICY, BehaviorAction.CREATE, object(TargetType.RowAccessPolicy, ctx.name()), objects(TargetType.Table, ctx.qualified_name()));
        return null;
    }

    @Override
    public Void visitAlterpolicystmt(AlterpolicystmtContext ctx) {
        addRelation(SplitQueryType.ALTER_POLICY, BehaviorAction.ALTER, object(TargetType.RowAccessPolicy, ctx.name(0)), objects(TargetType.Table, ctx.qualified_name()));
        return null;
    }

    @Override
    public Void visitIndexstmt(IndexstmtContext ctx) {
        ParserRuleContext indexName = ctx.index_name_() != null ? ctx.index_name_() : ctx.name();
        BehaviorObject index = indexName == null ? objects.unnamedObject(TargetType.Index, ctx, UmiTypes.Schema) : object(TargetType.Index, indexName);
        addRelation(SplitQueryType.ADD_INDEX, BehaviorAction.CREATE, index, objects(TargetType.Table, ctx.relation_expr().qualified_name()));
        return null;
    }

    @Override
    public Void visitViewstmt(ViewstmtContext ctx) {
        addRelation(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.qualified_name()), tableReferences(ctx.selectstmt()));
        addFunctionRelations(ctx);
        return null;
    }

    @Override
    public Void visitCreatematviewstmt(CreatematviewstmtContext ctx) {
        addRelation(SplitQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.Materialized, ctx.create_mv_target().qualified_name()), tableReferences(ctx.selectstmt()));
        addFunctionRelations(ctx);
        return null;
    }

    @Override
    public Void visitFunc_application(Func_applicationContext ctx) {
        addFunctionBehavior(ctx);
        return visitChildren(ctx);
    }

    @Override
    public Void visitCreatefunctionstmt(CreatefunctionstmtContext ctx) {
        TargetType targetType = ctx.PROCEDURE() == null ? TargetType.Function : TargetType.Procedure;
        BehaviorObject program = object(targetType, ctx.func_name());
        addUnary(SplitQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, program);
        addNestedUnary(BehaviorAction.UNSAFE, program);
        return null;
    }

    @Override
    public Void visitCreatetrigstmt(CreatetrigstmtContext ctx) {
        List<BehaviorObject> targets = objects(TargetType.Table, firstQualifiedName(ctx));
        addObject(targets, object(TargetType.Function, ctx.func_name()));
        addRelation(SplitQueryType.CREATE_TRIGGER, BehaviorAction.CREATE, object(TargetType.Trigger, ctx.name()), targets);
        addNestedUnary(BehaviorAction.UNSAFE, object(TargetType.Trigger, ctx.name()));
        return null;
    }

    @Override
    public Void visitDostmt(DostmtContext ctx) {
        addUnary(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, objects.unnamedObject(TargetType.ProgramObject, ctx, UmiTypes.Instance));
        return null;
    }

    @Override
    public Void visitLoadstmt(LoadstmtContext ctx) {
        addUnary(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, instanceNamed(TargetType.Library, ctx.file_name(), unquoteLiteral(text(ctx.file_name()))));
        return null;
    }

    @Override
    public Void visitAltersystemstmt(AltersystemstmtContext ctx) {
        if (ctx.generic_set() != null) {
            addUnary(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.CONFIGURE, instanceNamed(TargetType.ConfigKey, ctx.generic_set()
                .var_name(), text(ctx.generic_set().var_name())));
        } else if (ctx.generic_reset() != null && ctx.generic_reset().var_name() != null) {
            addUnary(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.RESET, instanceNamed(TargetType.ConfigKey, ctx.generic_reset()
                .var_name(), text(ctx.generic_reset().var_name())));
        } else {
            addUnary(SplitQueryType.SYSTEM_SETTING_WRITE, BehaviorAction.RESET, objects.unnamedObject(TargetType.ConfigKey, ctx, UmiTypes.Instance));
        }
        return null;
    }

    @Override
    public Void visitCreateseqstmt(CreateseqstmtContext ctx) {
        addUnary(SplitQueryType.CREATE_SEQUENCE, BehaviorAction.CREATE, object(TargetType.Sequence, ctx.qualified_name()));
        return null;
    }

    @Override
    public Void visitCreatedbstmt(CreatedbstmtContext ctx) {
        addUnary(SplitQueryType.CREATE_CATALOG, BehaviorAction.CREATE, object(TargetType.Catalog, ctx.name()));
        return null;
    }

    @Override
    public Void visitDropdbstmt(DropdbstmtContext ctx) {
        addUnary(SplitQueryType.DROP_CATALOG, BehaviorAction.DROP, object(TargetType.Catalog, ctx.name()));
        return null;
    }

    @Override
    public Void visitCreateschemastmt(CreateschemastmtContext ctx) {
        if (ctx.optschemaname() != null) {
            addUnary(SplitQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx.optschemaname()));
        }
        return null;
    }

    @Override
    public Void visitDropschemastmt(DropschemastmtContext ctx) {
        for (Qualified_nameContext name : ctx.qualified_name_list().qualified_name()) {
            addUnary(SplitQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, name));
        }
        return null;
    }

    @Override
    public Void visitCreateuserstmt(CreateuserstmtContext ctx) {
        addUnary(SplitQueryType.CREATE_USER, BehaviorAction.CREATE, instanceNamed(TargetType.User, ctx.roleid(), unquoteLiteral(text(ctx.roleid()))));
        return null;
    }

    @Override
    public Void visitCreaterolestmt(CreaterolestmtContext ctx) {
        addUnary(SplitQueryType.CREATE_ROLE, BehaviorAction.CREATE, instanceNamed(TargetType.Role, ctx.roleid(), unquoteLiteral(text(ctx.roleid()))));
        return null;
    }

    @Override
    public Void visitDropuserstmt(DropuserstmtContext ctx) {
        for (RolespecContext role : ctx.role_list().rolespec()) {
            addUnary(SplitQueryType.DROP_USER, BehaviorAction.DROP, principalAs(role, TargetType.User));
        }
        return null;
    }

    @Override
    public Void visitDroprolestmt(DroprolestmtContext ctx) {
        for (RolespecContext role : ctx.role_list().rolespec()) {
            addUnary(SplitQueryType.DROP_ROLE, BehaviorAction.DROP, principalAs(role, TargetType.Role));
        }
        return null;
    }

    @Override
    public Void visitCreatetablespacestmt(CreatetablespacestmtContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.opttablespaceowner() != null) {
            addObject(targets, principalAs(ctx.opttablespaceowner().rolespec(), TargetType.UserOrRole));
        }
        addObject(targets, instanceNamed(TargetType.File, ctx.sconst(), unquoteLiteral(text(ctx.sconst()))));
        addRelation(SplitQueryType.CREATE_TABLESPACE, BehaviorAction.CREATE, instanceNamed(TargetType.Tablespace, ctx.name(), unquoteLiteral(text(ctx.name()))), targets);
        return null;
    }

    @Override
    public Void visitAltertblspcstmt(AltertblspcstmtContext ctx) {
        addUnary(SplitQueryType.ALTER_TABLESPACE, BehaviorAction.ALTER, instanceNamed(TargetType.Tablespace, ctx.name(), unquoteLiteral(text(ctx.name()))));
        return null;
    }

    @Override
    public Void visitDroptablespacestmt(DroptablespacestmtContext ctx) {
        addUnary(SplitQueryType.DROP_TABLESPACE, BehaviorAction.DROP, instanceNamed(TargetType.Tablespace, ctx.name(), unquoteLiteral(text(ctx.name()))));
        return null;
    }

    @Override
    public Void visitAlterdatabasestmt(AlterdatabasestmtContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.setTablespaceName() != null) {
            NameContext tablespace = ctx.setTablespaceName().name();
            addObject(targets, instanceNamed(TargetType.Tablespace, tablespace, unquoteLiteral(text(tablespace))));
        }
        addRelation(SplitQueryType.ALTER_CATALOG, BehaviorAction.ALTER, object(TargetType.Catalog, ctx.name()), targets);
        return null;
    }

    @Override
    public Void visitAlterdatabasesetstmt(AlterdatabasesetstmtContext ctx) {
        List<BehaviorObject> config = new ArrayList<>();
        for (Var_nameContext name : descendants(ctx.setresetclause(), Var_nameContext.class)) {
            addObject(config, instanceNamed(TargetType.ConfigKey, name, text(name)));
        }
        addRelation(SplitQueryType.ALTER_CATALOG, BehaviorAction.CONFIGURE, object(TargetType.Catalog, ctx.name()), config);
        return null;
    }

    @Override
    public Void visitAlterrolestmt(AlterrolestmtContext ctx) {
        TargetType type = ctx.USER() == null ? TargetType.Role : TargetType.User;
        addUnary(SplitQueryType.ALTER_USER, BehaviorAction.ALTER, principalAs(ctx.rolespec(), type));
        return null;
    }

    @Override
    public Void visitAlterrolesetstmt(AlterrolesetstmtContext ctx) {
        TargetType type = ctx.USER() == null ? TargetType.Role : TargetType.User;
        BehaviorObject principal = ctx.rolespec() == null ? objects.unnamedObject(type, ctx, UmiTypes.Instance) : principalAs(ctx.rolespec(), type);
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.in_database_() != null) {
            addObject(targets, object(TargetType.Catalog, ctx.in_database_().name()));
        }
        for (Var_nameContext name : descendants(ctx.setresetclause(), Var_nameContext.class)) {
            addObject(targets, instanceNamed(TargetType.ConfigKey, name, text(name)));
        }
        addRelation(SplitQueryType.ALTER_USER, BehaviorAction.CONFIGURE, principal, targets);
        return null;
    }

    @Override
    public Void visitRefreshmatviewstmt(RefreshmatviewstmtContext ctx) {
        addUnary(SplitQueryType.ADMIN, BehaviorAction.REFRESH, object(TargetType.Materialized, ctx.qualified_name()));
        return null;
    }

    @Override
    public Void visitAnalyzestmt(AnalyzestmtContext ctx) {
        addMaintenanceRelations(SplitQueryType.ADMIN_TABLE, BehaviorAction.ANALYZE, ctx);
        return null;
    }

    @Override
    public Void visitVacuumstmt(VacuumstmtContext ctx) {
        addMaintenanceRelations(SplitQueryType.ADMIN_TABLE, BehaviorAction.OPTIMIZE, ctx);
        return null;
    }

    @Override
    public Void visitClusterstmt(ClusterstmtContext ctx) {
        BehaviorObject table = ctx.qualified_name() == null ? currentCatalog(ctx) : object(TargetType.Table, ctx.qualified_name());
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.name() != null) {
            addObject(targets, object(TargetType.Index, ctx.name()));
        }
        addRelation(SplitQueryType.ADMIN_TABLE, BehaviorAction.OPTIMIZE, table, targets);
        return null;
    }

    @Override
    public Void visitReindexstmt(ReindexstmtContext ctx) {
        if (ctx.qualified_name() != null) {
            TargetType type = ctx.reindex_target_relation() != null && ctx.reindex_target_relation().INDEX() != null ? TargetType.Index : TargetType.Table;
            addUnary(SplitQueryType.ADMIN, BehaviorAction.OPTIMIZE, object(type, ctx.qualified_name()));
        } else if (ctx.SCHEMA() != null && ctx.name() != null) {
            addUnary(SplitQueryType.ADMIN, BehaviorAction.OPTIMIZE, object(TargetType.Schema, ctx.name()));
        } else {
            addUnary(SplitQueryType.ADMIN, BehaviorAction.OPTIMIZE, currentCatalog(ctx));
        }
        return null;
    }

    @Override
    public Void visitLockstmt(LockstmtContext ctx) {
        for (Relation_exprContext relation : ctx.relation_expr_list().relation_expr()) {
            addUnary(SplitQueryType.SESSION_LOCK, BehaviorAction.LOCK, object(TargetType.Table, relation.qualified_name()));
        }
        return null;
    }

    @Override
    public Void visitCheckpointstmt(CheckpointstmtContext ctx) {
        addUnary(SplitQueryType.MAINTAIN_LOG, BehaviorAction.CHECKPOINT, currentCatalog(ctx));
        return null;
    }

    @Override
    public Void visitCreateextensionstmt(CreateextensionstmtContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (Create_extension_opt_itemContext option : descendants(ctx.create_extension_opt_list(), Create_extension_opt_itemContext.class)) {
            if (option.SCHEMA() != null) {
                addObject(targets, object(TargetType.Schema, option.name()));
            }
        }
        addRelation(SplitQueryType.CREATE_LIBRARY, BehaviorAction.CREATE, catalogNamed(TargetType.Extension, ctx.name(), unquoteLiteral(text(ctx.name()))), targets);
        return null;
    }

    @Override
    public Void visitAlterextensionstmt(AlterextensionstmtContext ctx) {
        addUnary(SplitQueryType.ALTER_LIBRARY, BehaviorAction.ALTER, catalogNamed(TargetType.Extension, ctx.name(), unquoteLiteral(text(ctx.name()))));
        return null;
    }

    @Override
    public Void visitAlterextensioncontentsstmt(AlterextensioncontentsstmtContext ctx) {
        List<NameContext> names = ctx.name();
        BehaviorObject extension = catalogNamed(TargetType.Extension, names.get(0), unquoteLiteral(text(names.get(0))));
        ParserRuleContext member = ctx.any_name();
        if (member == null) {
            member = ctx.function_with_argtypes() != null ? first(ctx.function_with_argtypes(), Func_nameContext.class) : names.size() > 1 ? names.get(1) : null;
        }
        addRelation(SplitQueryType.ALTER_LIBRARY, BehaviorAction.ALTER, extension, objects(object(TargetType.Object, member)));
        return null;
    }

    @Override
    public Void visitCreatestatsstmt(CreatestatsstmtContext ctx) {
        addRelation(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.CREATE, object(TargetType.Statistics, ctx.any_name()), tableReferences(ctx.from_list()));
        return null;
    }

    @Override
    public Void visitAlterstatsstmt(AlterstatsstmtContext ctx) {
        addUnary(SplitQueryType.ADMIN_PERFORMANCE, BehaviorAction.ALTER, object(TargetType.Statistics, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitCreatedomainstmt(CreatedomainstmtContext ctx) {
        addUnary(SplitQueryType.CREATE_TYPE, BehaviorAction.CREATE, object(TargetType.Domain, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitAlterdomainstmt(AlterdomainstmtContext ctx) {
        addUnary(SplitQueryType.ALTER_TYPE, BehaviorAction.ALTER, object(TargetType.Domain, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitAltercompositetypestmt(AltercompositetypestmtContext ctx) {
        addUnary(SplitQueryType.ALTER_TYPE, BehaviorAction.ALTER, object(TargetType.Type, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitAlterenumstmt(AlterenumstmtContext ctx) {
        addUnary(SplitQueryType.ALTER_TYPE, BehaviorAction.ALTER, object(TargetType.Type, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitAltertypestmt(AltertypestmtContext ctx) {
        addUnary(SplitQueryType.ALTER_TYPE, BehaviorAction.ALTER, object(TargetType.Type, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitVariableshowstmt(VariableshowstmtContext ctx) {
        BehaviorObject subject = ctx.var_name() == null ? objects
            .unnamedObject(TargetType.ConfigKey, ctx, UmiTypes.Instance) : instanceNamed(TargetType.ConfigKey, ctx.var_name(), text(ctx.var_name()));
        addUnary(SplitQueryType.SESSION_VARIABLE_RW, BehaviorAction.READ, subject);
        return null;
    }

    @Override
    public Void visitVariablesetstmt(VariablesetstmtContext ctx) {
        Set_rest_moreContext set = ctx.set_rest().set_rest_more();
        if (set == null) {
            return null;
        }
        if (set.generic_set() != null) {
            addUnary(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, instanceNamed(TargetType.ConfigKey, set.generic_set()
                .var_name(), text(set.generic_set().var_name())));
        } else if (set.ROLE() != null) {
            addUnary(SplitQueryType.SWITCH_ROLE, BehaviorAction.SWITCH, instanceNamed(TargetType.Role, set
                .nonreservedword_or_sconst(), unquoteLiteral(text(set.nonreservedword_or_sconst()))));
        } else if (set.SCHEMA() != null) {
            addUnary(SplitQueryType.SWITCH_SCHEMA, BehaviorAction.SWITCH, literalObject(TargetType.Schema, set.sconst()));
        } else if (set.AUTHORIZATION() != null) {
            ParserRuleContext name = set.nonreservedword_or_sconst();
            BehaviorObject user = name == null ? objects.unnamedObject(TargetType.User, set, UmiTypes.Instance) : instanceNamed(TargetType.User, name, unquoteLiteral(text(name)));
            addUnary(SplitQueryType.SWITCH_USER, BehaviorAction.SWITCH, user);
        } else if (set.ZONE() != null || set.NAMES() != null || set.CATALOG() != null || set.XML_P() != null) {
            addUnary(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.CONFIGURE, instanceNamed(TargetType.ConfigKey, set.getStart(), set.getStart().getText()));
        }
        return null;
    }

    @Override
    public Void visitVariableresetstmt(VariableresetstmtContext ctx) {
        Generic_resetContext reset = ctx.reset_rest().generic_reset();
        BehaviorObject subject = reset != null && reset.var_name() != null ? instanceNamed(TargetType.ConfigKey, reset.var_name(), text(reset.var_name())) : objects
            .unnamedObject(TargetType.ConfigKey, ctx.reset_rest(), UmiTypes.Instance);
        addUnary(SplitQueryType.SESSION_SETTING_WRITE, BehaviorAction.RESET, subject);
        return null;
    }

    @Override
    public Void visitCopystmt(CopystmtContext ctx) {
        TargetType streamType = ctx.program_() == null ? TargetType.File : TargetType.ProgramObject;
        BehaviorObject stream = instanceNamed(streamType, ctx.copy_file_name(), unquoteLiteral(text(ctx.copy_file_name())));
        List<BehaviorObject> tables = ctx.qualified_name() == null ? tableReferences(ctx.preparablestmt()) : objects(object(TargetType.Table, ctx.qualified_name()));
        if (ctx.copy_from() != null && ctx.copy_from().FROM() != null) {
            addRelation(SplitQueryType.DATA_IMPORT, BehaviorAction.IMPORT, tables.isEmpty() ? objects.unnamedObject(TargetType.Table, ctx, UmiTypes.Schema) : tables
                .get(0), objects(stream));
        } else {
            addRelation(SplitQueryType.DATA_EXPORT, BehaviorAction.EXPORT, stream, tables);
        }
        if (ctx.program_() != null) {
            addNestedUnary(BehaviorAction.UNSAFE, instanceNamed(TargetType.ProgramObject, ctx.copy_file_name(), unquoteLiteral(text(ctx.copy_file_name()))));
        }
        addFunctionRelations(ctx);
        return null;
    }

    @Override
    public Void visitDroptablestmt(DroptablestmtContext ctx) {
        for (Any_nameContext name : ctx.any_name_list_().any_name()) {
            addUnary(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, name));
        }
        return null;
    }

    @Override
    public Void visitDropstmt(DropstmtContext ctx) {
        TargetType targetType = dropTarget(resolvedType);
        if (ctx.object_type_name_on_any_name() != null && ctx.object_type_name_on_any_name().POLICY() != null) {
            targetType = TargetType.RowAccessPolicy;
        }
        if (targetType == null) {
            return null;
        }

        if (ctx.object_type_name_on_any_name() != null && ctx.name() != null) {
            addRelation(resolvedType, BehaviorAction.DROP, object(targetType, ctx.name()), objects(TargetType.Table, ctx.any_name()));
        } else if (ctx.any_name_list_() != null) {
            for (Any_nameContext name : ctx.any_name_list_().any_name()) {
                addUnary(resolvedType, BehaviorAction.DROP, object(targetType, name));
            }
        } else if (ctx.name_list() != null) {
            for (NameContext name : ctx.name_list().name()) {
                BehaviorObject subject = targetType == TargetType.Language ? catalogNamed(targetType, name, unquoteLiteral(text(name))) : object(targetType, name);
                addUnary(resolvedType, BehaviorAction.DROP, subject);
            }
        } else if (ctx.type_name_list() != null) {
            for (TypenameContext name : descendants(ctx.type_name_list(), TypenameContext.class)) {
                addUnary(resolvedType, BehaviorAction.DROP, object(targetType, name));
            }
        } else if (ctx.any_name() != null) {
            addUnary(resolvedType, BehaviorAction.DROP, object(targetType, ctx.any_name()));
        } else if (ctx.name() != null) {
            BehaviorObject subject = targetType == TargetType.Language ? catalogNamed(targetType, ctx.name(), unquoteLiteral(text(ctx.name()))) : object(targetType, ctx.name());
            addUnary(resolvedType, BehaviorAction.DROP, subject);
        }
        return null;
    }

    @Override
    public Void visitCreateplangstmt(CreateplangstmtContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (Handler_nameContext handler : descendants(ctx, Handler_nameContext.class)) {
            addObject(targets, object(TargetType.Function, handler));
        }
        addRelation(SplitQueryType.CREATE_LANGUAGE, ctx.or_replace_() == null ? BehaviorAction.CREATE : BehaviorAction.REPLACE, catalogNamed(TargetType.Language, ctx
            .name(), unquoteLiteral(text(ctx.name()))), targets);
        return null;
    }

    @Override
    public Void visitCreatetransformstmt(CreatetransformstmtContext ctx) {
        List<BehaviorObject> targets = objects(catalogNamed(TargetType.Language, ctx.name(), unquoteLiteral(text(ctx.name()))));
        for (Function_with_argtypesContext function : descendants(ctx, Function_with_argtypesContext.class)) {
            addObject(targets, object(TargetType.Function, first(function, Func_nameContext.class)));
        }
        addRelation(SplitQueryType.CREATE_TRANSFORM, ctx.or_replace_() == null ? BehaviorAction.CREATE : BehaviorAction.REPLACE, catalogNamed(TargetType.Transform, ctx
            .typename(), unquoteLiteral(text(ctx.typename()))), targets);
        return null;
    }

    @Override
    public Void visitDroptransformstmt(DroptransformstmtContext ctx) {
        addRelation(SplitQueryType.DROP_TRANSFORM, BehaviorAction.DROP, catalogNamed(TargetType.Transform, ctx
            .typename(), unquoteLiteral(text(ctx.typename()))), objects(catalogNamed(TargetType.Language, ctx.name(), unquoteLiteral(text(ctx.name())))));
        return null;
    }

    private TargetType dropTarget(SplitQueryType type) {
        return switch (type) {
            case DROP_LIBRARY -> TargetType.Library;
            case DROP_LANGUAGE -> TargetType.Language;
            case DROP_INDEX -> TargetType.Index;
            case DROP_VIEW -> TargetType.View;
            case DROP_TRIGGER -> TargetType.Trigger;
            case DROP_POLICY -> TargetType.Policy;
            case DROP_PUB_SUB -> TargetType.PublicationSubscription;
            case DROP_SEQUENCE -> TargetType.Sequence;
            case DROP_TYPE -> TargetType.Type;
            case DROP_PROG_OBJ -> TargetType.ProgramObject;
            case DROP_USER -> TargetType.User;
            case DROP_ROLE -> TargetType.Role;
            default -> TargetType.Unknown;
        };
    }

    private TargetType renameTarget(SplitQueryType type) {
        return switch (type) {
            case RENAME_CATALOG -> TargetType.Catalog;
            case RENAME_SCHEMA -> TargetType.Schema;
            case RENAME_TABLESPACE -> TargetType.Tablespace;
            case RENAME_TABLE -> TargetType.Table;
            case RENAME_COLUMN -> TargetType.Column;
            case RENAME_CONSTRAINT -> TargetType.Constraint;
            case RENAME_INDEX -> TargetType.Index;
            case RENAME_VIEW -> TargetType.View;
            case RENAME_SEQUENCE -> TargetType.Sequence;
            case RENAME_TYPE -> TargetType.Type;
            case RENAME_PROG_OBJ -> TargetType.ProgramObject;
            case RENAME_TRIGGER -> TargetType.Trigger;
            case RENAME_USER -> TargetType.User;
            case RENAME_ROLE -> TargetType.Role;
            default -> TargetType.Object;
        };
    }

    private ParserRuleContext renameSource(RenamestmtContext ctx) {
        if (ctx.function_with_argtypes() != null) {
            return first(ctx.function_with_argtypes(), Func_nameContext.class);
        }
        if (ctx.aggregate_with_argtypes() != null) {
            return first(ctx.aggregate_with_argtypes(), Func_nameContext.class);
        }
        if (ctx.qualified_name() != null)
            return ctx.qualified_name();
        if (ctx.relation_expr() != null)
            return ctx.relation_expr().qualified_name();
        if (ctx.any_name() != null)
            return ctx.any_name();
        if (!ctx.roleid().isEmpty())
            return ctx.roleid(0);
        return ctx.name().isEmpty() ? null : ctx.name(0);
    }

    private ParserRuleContext renameTargetName(RenamestmtContext ctx) {
        if (ctx.roleid().size() > 1)
            return ctx.roleid(1);
        List<NameContext> names = ctx.name();
        return names.isEmpty() ? null : names.get(names.size() - 1);
    }

    @Override
    public Void visitRename_table_stmt(Rename_table_stmtContext ctx) {
        BehaviorObject source = object(TargetType.Table, ctx.relation_expr().qualified_name());
        BehaviorObject target = object(TargetType.Table, ctx.name());
        moveToSameContainer(source, target);
        addRelation(SplitQueryType.RENAME_TABLE, BehaviorAction.RENAME, source, objects(target));
        return null;
    }

    @Override
    public Void visitRename_database_stmt(Rename_database_stmtContext ctx) {
        BehaviorObject source = object(TargetType.Catalog, ctx.name(0));
        BehaviorObject target = object(TargetType.Catalog, ctx.name(1));
        addRelation(SplitQueryType.RENAME_CATALOG, BehaviorAction.RENAME, source, objects(target));
        return null;
    }

    @Override
    public Void visitRename_schema_stmt(Rename_schema_stmtContext ctx) {
        BehaviorObject source = object(TargetType.Schema, ctx.qualified_name());
        BehaviorObject target = object(TargetType.Schema, ctx.name());
        moveToSameContainer(source, target);
        addRelation(SplitQueryType.RENAME_SCHEMA, BehaviorAction.RENAME, source, objects(target));
        return null;
    }

    @Override
    public Void visitRename_column_stmt(Rename_column_stmtContext ctx) {
        BehaviorObject table = object(TargetType.Table, ctx.relation_expr().qualified_name());
        List<NameContext> names = ctx.name();
        BehaviorObject source = childObject(TargetType.Column, table, names.get(0));
        BehaviorObject target = childObject(TargetType.Column, table, names.get(1));
        addRelation(SplitQueryType.RENAME_COLUMN, BehaviorAction.RENAME, source, objects(target, table));
        return null;
    }

    @Override
    public Void visitRenamestmt(RenamestmtContext ctx) {
        if (ctx.rename_table_stmt() != null)
            return visitRename_table_stmt(ctx.rename_table_stmt());
        if (ctx.rename_database_stmt() != null)
            return visitRename_database_stmt(ctx.rename_database_stmt());
        if (ctx.rename_schema_stmt() != null)
            return visitRename_schema_stmt(ctx.rename_schema_stmt());
        if (ctx.rename_column_stmt() != null)
            return visitRename_column_stmt(ctx.rename_column_stmt());

        TargetType type = renameTarget(resolvedType);
        ParserRuleContext sourceName = renameSource(ctx);
        ParserRuleContext targetName = renameTargetName(ctx);
        if (sourceName == null || targetName == null)
            return null;

        BehaviorObject source;
        BehaviorObject target;
        if (type == TargetType.User || type == TargetType.Role || type == TargetType.Tablespace) {
            source = instanceNamed(type, sourceName, unquoteLiteral(text(sourceName)));
            target = instanceNamed(type, targetName, unquoteLiteral(text(targetName)));
        } else {
            source = object(type, sourceName);
            target = object(type, targetName);
            moveToSameContainer(source, target);
        }
        addRelation(resolvedType, BehaviorAction.RENAME, source, objects(target));
        return null;
    }

    @Override
    public Void visitAlterobjectdependsstmt(AlterobjectdependsstmtContext ctx) {
        BehaviorObject subject;
        if (ctx.TRIGGER() != null) {
            subject = object(TargetType.Trigger, ctx.name(0));
        } else if (ctx.INDEX() != null) {
            subject = object(TargetType.Index, ctx.qualified_name());
        } else if (ctx.MATERIALIZED() != null) {
            subject = object(TargetType.Materialized, ctx.qualified_name());
        } else {
            TargetType type = ctx.PROCEDURE() != null ? TargetType.Procedure : ctx.ROUTINE() != null ? TargetType.ProgramObject : TargetType.Function;
            subject = object(type, first(ctx.function_with_argtypes(), Func_nameContext.class));
        }
        NameContext extensionName = ctx.name().get(ctx.name().size() - 1);
        addRelation(resolvedType, BehaviorAction.ALTER, subject, objects(catalogNamed(TargetType.Extension, extensionName, unquoteLiteral(text(extensionName)))));
        return null;
    }

    @Override
    public Void visitAltertsdictionarystmt(AltertsdictionarystmtContext ctx) {
        addUnary(resolvedType, BehaviorAction.ALTER, object(TargetType.Policy, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitComment_table_stmt(Comment_table_stmtContext ctx) {
        addUnary(SplitQueryType.COMMENT_TABLE, BehaviorAction.ALTER, object(TargetType.Table, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitComment_column_stmt(Comment_column_stmtContext ctx) {
        BehaviorObject table = tableFromColumnName(ctx.any_name());
        addRelation(SplitQueryType.COMMENT_COLUMN, BehaviorAction.ALTER, columnFromName(ctx.any_name()), objects(table));
        return null;
    }

    @Override
    public Void visitCommentstmt(CommentstmtContext ctx) {
        if (ctx.comment_table_stmt() != null) {
            return visitComment_table_stmt(ctx.comment_table_stmt());
        }
        if (ctx.comment_column_stmt() != null) {
            return visitComment_column_stmt(ctx.comment_column_stmt());
        }
        if (resolvedType == SplitQueryType.COMMENT_POLICY && ctx.name() != null && ctx.any_name() != null) {
            addRelation(resolvedType, BehaviorAction.ALTER, object(TargetType.RowAccessPolicy, ctx.name()), objects(TargetType.Table, ctx.any_name()));
            return null;
        }
        if (resolvedType == SplitQueryType.COMMENT_TRANSFORM && !ctx.typename().isEmpty() && ctx.name() != null) {
            addRelation(resolvedType, BehaviorAction.ALTER, catalogNamed(TargetType.Transform, ctx
                .typename(0), unquoteLiteral(text(ctx.typename(0)))), objects(catalogNamed(TargetType.Language, ctx.name(), unquoteLiteral(text(ctx.name())))));
            return null;
        }
        if (resolvedType == SplitQueryType.COMMENT_LARGE_OBJECT && ctx.numericonly() != null) {
            addUnary(resolvedType, BehaviorAction.ALTER, catalogNamed(TargetType.LargeObject, ctx.numericonly(), text(ctx.numericonly())));
            return null;
        }
        if ((resolvedType == SplitQueryType.COMMENT_LANGUAGE || resolvedType == SplitQueryType.COMMENT_FOREIGN_DATA_WRAPPER
             || resolvedType == SplitQueryType.COMMENT_FOREIGN_SERVER)
            && ctx.name() != null) {
            TargetType scopedType = commentTarget(ctx);
            addUnary(resolvedType, BehaviorAction.ALTER, catalogNamed(scopedType, ctx.name(), unquoteLiteral(text(ctx.name()))));
            return null;
        }
        TargetType type = commentTarget(ctx);
        ParserRuleContext name = commentName(ctx);
        if (type != null && name != null) {
            addUnary(resolvedType, BehaviorAction.ALTER, object(type, name));
        }
        return null;
    }

    @Override
    public Void visitSeclabelstmt(SeclabelstmtContext ctx) {
        BehaviorObject subject = securityLabelSubject(ctx);
        if (subject != null) {
            addUnary(SplitQueryType.SECURITY_LABEL, BehaviorAction.ALTER, subject);
        }
        return null;
    }

    @Override
    public Void visitTruncatestmt(TruncatestmtContext ctx) {
        for (Relation_exprContext relation : ctx.relation_expr_list().relation_expr()) {
            addUnary(SplitQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, relation.qualified_name()));
        }
        return null;
    }

    @Override
    public Void visitReassignownedstmt(ReassignownedstmtContext ctx) {
        BehaviorObject newOwner = principal(ctx.rolespec());
        for (RolespecContext oldOwner : ctx.role_list().rolespec()) {
            addRelation(SplitQueryType.TRANSFER_PRIVILEGE, BehaviorAction.TRANSFER, principal(oldOwner), objects(newOwner));
        }
        return null;
    }

    @Override
    public Void visitDropownedstmt(DropownedstmtContext ctx) {
        for (RolespecContext owner : ctx.role_list().rolespec()) {
            addUnary(SplitQueryType.REVOKE, BehaviorAction.REVOKE, principal(owner));
        }
        return null;
    }

    @Override
    public Void visitGrantstmt(GrantstmtContext ctx) {
        addPrivilegeRelations(ctx.privilege_target(), ctx.grantee_list(), BehaviorAction.GRANT);
        return null;
    }

    @Override
    public Void visitRevokestmt(RevokestmtContext ctx) {
        addPrivilegeRelations(ctx.privilege_target(), ctx.grantee_list(), BehaviorAction.REVOKE);
        return null;
    }

    @Override
    public Void visitGrantrolestmt(GrantrolestmtContext ctx) {
        addRoleMembershipRelations(ctx.privilege_list(), ctx.role_list(), BehaviorAction.GRANT);
        return null;
    }

    @Override
    public Void visitRevokerolestmt(RevokerolestmtContext ctx) {
        addRoleMembershipRelations(ctx.privilege_list(), ctx.role_list(), BehaviorAction.REVOKE);
        return null;
    }

    @Override
    public Void visitAltertablestmt(AltertablestmtContext ctx) {
        ParserRuleContext subjectName = ctx.relation_expr() == null ? ctx.qualified_name() : ctx.relation_expr().qualified_name();
        TargetType targetType;
        if (ctx.INDEX() != null) {
            targetType = TargetType.Index;
        } else if (ctx.SEQUENCE() != null) {
            targetType = TargetType.Sequence;
        } else if (ctx.MATERIALIZED() != null) {
            targetType = TargetType.Materialized;
        } else if (ctx.VIEW() != null) {
            targetType = TargetType.View;
        } else {
            targetType = TargetType.Table;
        }
        BehaviorObject subject = object(targetType, subjectName);
        if (resolvedType == SplitQueryType.TRANSFER_PRIVILEGE) {
            transfer(subject, first(ctx, RolespecContext.class));
            return null;
        }

        addUnary(resolvedType, BehaviorAction.ALTER, subject);
        if (targetType != TargetType.Table) {
            return null;
        }
        for (AddColumnContext command : descendants(ctx, AddColumnContext.class)) {
            ColidContext name = command.columnDef().colid();
            addNestedRelation(BehaviorAction.CREATE, childObject(TargetType.Column, subject, name), objects(subject));
        }
        for (AlterColumnContext command : descendants(ctx, AlterColumnContext.class)) {
            addNestedRelation(BehaviorAction.ALTER, childObject(TargetType.Column, subject, command.colid()), objects(subject));
        }
        for (DropColumnContext command : descendants(ctx, DropColumnContext.class)) {
            addNestedRelation(BehaviorAction.DROP, childObject(TargetType.Column, subject, command.colid()), objects(subject));
        }
        for (AddConstraintContext command : descendants(ctx, AddConstraintContext.class)) {
            NameContext name = command.tableconstraint().name();
            if (name != null) {
                addNestedRelation(BehaviorAction.CREATE, childObject(TargetType.Constraint, subject, name), objects(subject));
            }
        }
        for (AlterConstaintContext command : descendants(ctx, AlterConstaintContext.class)) {
            addNestedRelation(BehaviorAction.ALTER, childObject(TargetType.Constraint, subject, command.name()), objects(subject));
        }
        for (ValidateConstraintContext command : descendants(ctx, ValidateConstraintContext.class)) {
            addNestedRelation(BehaviorAction.VALIDATE, childObject(TargetType.Constraint, subject, command.name()), objects(subject));
        }
        for (DropConstraintContext command : descendants(ctx, DropConstraintContext.class)) {
            addNestedRelation(BehaviorAction.DROP, childObject(TargetType.Constraint, subject, command.name()), objects(subject));
        }
        if (ctx.partition_cmd() != null) {
            Partition_cmdContext command = ctx.partition_cmd();
            BehaviorAction action = command.ATTACH() == null ? BehaviorAction.DROP : BehaviorAction.CREATE;
            addNestedRelation(action, childObject(TargetType.Partition, subject, command.qualified_name()), objects(subject));
        }
        return null;
    }

    @Override
    public Void visitAlterseqstmt(AlterseqstmtContext ctx) {
        if (resolvedType == SplitQueryType.TRANSFER_PRIVILEGE) {
            transfer(object(TargetType.Sequence, ctx.qualified_name()), first(ctx, RolespecContext.class));
        } else {
            addUnary(SplitQueryType.ALTER_SEQUENCE, BehaviorAction.ALTER, object(TargetType.Sequence, ctx.qualified_name()));
        }
        return null;
    }

    @Override
    public Void visitCreatepublicationstmt(CreatepublicationstmtContext ctx) {
        addRelation(SplitQueryType.CREATE_PUB_SUB, BehaviorAction.CREATE, catalogNamed(TargetType.Publication, ctx.name(), unquoteLiteral(text(ctx.name()))), publicationTargets(ctx
            .publication_for_tables_()));
        return null;
    }

    @Override
    public Void visitAlterpublicationstmt(AlterpublicationstmtContext ctx) {
        addRelation(SplitQueryType.ALTER_PUB_SUB, BehaviorAction.ALTER, catalogNamed(TargetType.Publication, ctx
            .name(), unquoteLiteral(text(ctx.name()))), publicationTargets(ctx));
        return null;
    }

    @Override
    public Void visitCreatesubscriptionstmt(CreatesubscriptionstmtContext ctx) {
        NameContext subscriptionName = ctx.name(0);
        addRelation(SplitQueryType.CREATE_PUB_SUB, BehaviorAction.CREATE, catalogNamed(TargetType.Subscription, subscriptionName, unquoteLiteral(text(subscriptionName))), subscriptionTargets(ctx
            .publication_name_list()));
        return null;
    }

    @Override
    public Void visitAltersubscriptionstmt(AltersubscriptionstmtContext ctx) {
        NameContext subscriptionName = ctx.name(0);
        SplitQueryType statementType = resolvedType == SplitQueryType.ADMIN_PUB_SUB ? SplitQueryType.ADMIN_PUB_SUB : SplitQueryType.ALTER_PUB_SUB;
        BehaviorAction action = ctx.ENABLE_P() != null ? BehaviorAction.START : ctx
            .DISABLE_P() != null ? BehaviorAction.STOP : ctx.REFRESH() != null ? BehaviorAction.REFRESH : ctx.SKIP_P() != null ? BehaviorAction.CONFIGURE : BehaviorAction.ALTER;
        addRelation(statementType, action, catalogNamed(TargetType.Subscription, subscriptionName, unquoteLiteral(text(subscriptionName))), subscriptionTargets(ctx
            .publication_name_list()));
        return null;
    }

    @Override
    public Void visitListenstmt(ListenstmtContext ctx) {
        addUnary(SplitQueryType.ADMIN_PUB_SUB, BehaviorAction.START, catalogNamed(TargetType.Queue, ctx.colid(), unquoteLiteral(text(ctx.colid()))));
        return null;
    }

    @Override
    public Void visitUnlistenstmt(UnlistenstmtContext ctx) {
        BehaviorObject channel = ctx.colid() == null ? objects
            .unnamedObject(TargetType.Queue, ctx, UmiTypes.Catalog) : catalogNamed(TargetType.Queue, ctx.colid(), unquoteLiteral(text(ctx.colid())));
        addUnary(SplitQueryType.ADMIN_PUB_SUB, BehaviorAction.STOP, channel);
        return null;
    }

    @Override
    public Void visitNotifystmt(NotifystmtContext ctx) {
        addUnary(SplitQueryType.ADMIN_PUB_SUB, BehaviorAction.CALL, catalogNamed(TargetType.Queue, ctx.colid(), unquoteLiteral(text(ctx.colid()))));
        return null;
    }

    @Override
    public Void visitCreateeventtrigstmt(CreateeventtrigstmtContext ctx) {
        BehaviorObject trigger = catalogNamed(TargetType.Trigger, ctx.name(), unquoteLiteral(text(ctx.name())));
        addRelation(SplitQueryType.CREATE_TRIGGER, BehaviorAction.CREATE, trigger, objects(object(TargetType.Function, ctx.func_name())));
        addNestedUnary(BehaviorAction.UNSAFE, trigger);
        return null;
    }

    @Override
    public Void visitAltereventtrigstmt(AltereventtrigstmtContext ctx) {
        addUnary(SplitQueryType.ALTER_TRIGGER, BehaviorAction.ALTER, catalogNamed(TargetType.Trigger, ctx.name(), unquoteLiteral(text(ctx.name()))));
        return null;
    }

    @Override
    public Void visitRulestmt(RulestmtContext ctx) {
        BehaviorObject rule = object(TargetType.Rule, ctx.name());
        addUnary(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, rule);
        return null;
    }

    @Override
    public Void visitCreatefdwstmt(CreatefdwstmtContext ctx) {
        List<BehaviorObject> handlers = new ArrayList<>();
        for (Handler_nameContext handler : descendants(ctx.fdw_options_(), Handler_nameContext.class)) {
            addObject(handlers, object(TargetType.Function, handler));
        }
        BehaviorObject wrapper = catalogNamed(TargetType.ForeignDataWrapper, ctx.name(), unquoteLiteral(text(ctx.name())));
        addRelation(resolvedType, BehaviorAction.CREATE, wrapper, handlers);
        if (!handlers.isEmpty()) {
            addNestedUnary(BehaviorAction.UNSAFE, wrapper);
        }
        return null;
    }

    @Override
    public Void visitAlterfdwstmt(AlterfdwstmtContext ctx) {
        List<BehaviorObject> handlers = new ArrayList<>();
        for (Handler_nameContext handler : descendants(ctx, Handler_nameContext.class)) {
            addObject(handlers, object(TargetType.Function, handler));
        }
        BehaviorObject wrapper = catalogNamed(TargetType.ForeignDataWrapper, ctx.name(), unquoteLiteral(text(ctx.name())));
        addRelation(resolvedType, BehaviorAction.ALTER, wrapper, handlers);
        if (!handlers.isEmpty()) {
            addNestedUnary(BehaviorAction.UNSAFE, wrapper);
        }
        return null;
    }

    @Override
    public Void visitCreateforeignserverstmt(CreateforeignserverstmtContext ctx) {
        List<NameContext> names = ctx.name();
        BehaviorObject server = catalogNamed(TargetType.ForeignServer, names.get(0), unquoteLiteral(text(names.get(0))));
        BehaviorObject wrapper = catalogNamed(TargetType.ForeignDataWrapper, names.get(names.size() - 1), unquoteLiteral(text(names.get(names.size() - 1))));
        addRelation(resolvedType, BehaviorAction.CREATE, server, objects(wrapper));
        return null;
    }

    @Override
    public Void visitAlterforeignserverstmt(AlterforeignserverstmtContext ctx) {
        addUnary(resolvedType, BehaviorAction.ALTER, catalogNamed(TargetType.ForeignServer, ctx.name(), unquoteLiteral(text(ctx.name()))));
        return null;
    }

    @Override
    public Void visitCreateforeigntablestmt(CreateforeigntablestmtContext ctx) {
        List<Qualified_nameContext> tables = ctx.qualified_name();
        List<BehaviorObject> targets = new ArrayList<>();
        if (tables.size() > 1) {
            addObject(targets, object(TargetType.Table, tables.get(1)));
        }
        addObject(targets, catalogNamed(TargetType.ForeignServer, ctx.name(), unquoteLiteral(text(ctx.name()))));
        addRelation(resolvedType, BehaviorAction.CREATE, object(TargetType.Table, tables.get(0)), targets);
        return null;
    }

    @Override
    public Void visitImportforeignschemastmt(ImportforeignschemastmtContext ctx) {
        List<NameContext> names = ctx.name();
        BehaviorObject foreignSchema = catalogNamed(TargetType.ForeignSchema, names.get(0), unquoteLiteral(text(names.get(0))));
        BehaviorObject server = catalogNamed(TargetType.ForeignServer, names.get(1), unquoteLiteral(text(names.get(1))));
        BehaviorObject localSchema = object(TargetType.Schema, names.get(2));
        addRelation(resolvedType, BehaviorAction.IMPORT, localSchema, objects(foreignSchema, server));
        return null;
    }

    @Override
    public Void visitCreateusermappingstmt(CreateusermappingstmtContext ctx) {
        userMapping(ctx.auth_ident(), ctx.name(), BehaviorAction.CREATE);
        return null;
    }

    @Override
    public Void visitAlterusermappingstmt(AlterusermappingstmtContext ctx) {
        userMapping(ctx.auth_ident(), ctx.name(), BehaviorAction.ALTER);
        return null;
    }

    @Override
    public Void visitDropusermappingstmt(DropusermappingstmtContext ctx) {
        userMapping(ctx.auth_ident(), ctx.name(), BehaviorAction.DROP);
        return null;
    }

    @Override
    public Void visitDeclarecursorstmt(DeclarecursorstmtContext ctx) {
        BehaviorObject cursor = catalogNamed(TargetType.Cursor, ctx.cursor_name(), unquoteLiteral(text(ctx.cursor_name())));
        addRelation(resolvedType, BehaviorAction.CREATE, cursor, tableReferences(ctx.selectstmt()));
        addFunctionRelations(ctx.selectstmt());
        return null;
    }

    @Override
    public Void visitFetchstmt(FetchstmtContext ctx) {
        BehaviorAction action = ctx.MOVE() == null ? BehaviorAction.READ : BehaviorAction.MOVE;
        addUnary(resolvedType, action, catalogNamed(TargetType.Cursor, ctx.fetch_args().cursor_name(), unquoteLiteral(text(ctx.fetch_args().cursor_name()))));
        return null;
    }

    @Override
    public Void visitCloseportalstmt(CloseportalstmtContext ctx) {
        BehaviorObject cursor = ctx.cursor_name() == null ? objects
            .unnamedObject(TargetType.Cursor, ctx, UmiTypes.Catalog) : catalogNamed(TargetType.Cursor, ctx.cursor_name(), unquoteLiteral(text(ctx.cursor_name())));
        addUnary(resolvedType, BehaviorAction.DROP, cursor);
        return null;
    }

    @Override
    public Void visitTransactionstmt(TransactionstmtContext ctx) {
        ColidContext savepoint = ctx.colid();
        if (savepoint != null) {
            BehaviorAction action = ctx.RELEASE() != null ? BehaviorAction.DROP : ctx.ROLLBACK() != null ? BehaviorAction.RESET : BehaviorAction.CREATE;
            addUnary(resolvedType, action, catalogNamed(TargetType.Savepoint, savepoint, unquoteLiteral(text(savepoint))));
            return null;
        }
        if (ctx.sconst() != null) {
            BehaviorAction action = ctx.PREPARE() != null ? BehaviorAction.CREATE : ctx.COMMIT() != null ? BehaviorAction.STOP : BehaviorAction.RESET;
            addUnary(resolvedType, action, catalogNamed(TargetType.Transaction, ctx.sconst(), unquoteLiteral(text(ctx.sconst()))));
            return null;
        }
        BehaviorAction action = ctx.BEGIN_P() != null
                                || ctx.START() != null ? BehaviorAction.START : ctx.ROLLBACK() != null || ctx.ABORT_P() != null ? BehaviorAction.RESET : BehaviorAction.STOP;
        addUnary(resolvedType, action, objects.unnamedObject(TargetType.Transaction, ctx, UmiTypes.Catalog));
        return null;
    }

    @Override
    public Void visitExplainstmt(ExplainstmtContext ctx) {
        if (isExplainAnalyze(ctx)) {
            DeclarecursorstmtContext cursor = ctx.explainablestmt().declarecursorstmt();
            if (cursor != null) {
                return visit(cursor.selectstmt());
            }
            return visit(ctx.explainablestmt());
        }
        addRelation(resolvedType, BehaviorAction.ANALYZE, objects.unnamedObject(TargetType.Query, ctx, UmiTypes.Catalog), tableReferences(ctx.explainablestmt()));
        addFunctionRelations(ctx.explainablestmt());
        return null;
    }

    private boolean isExplainAnalyze(ExplainstmtContext ctx) {
        if (ctx.analyze_keyword() != null) {
            return true;
        }
        if (ctx.explain_option_list() == null) {
            return false;
        }
        for (Explain_option_elemContext option : ctx.explain_option_list().explain_option_elem()) {
            if (option.explain_option_name().analyze_keyword() == null) {
                continue;
            }
            if (option.explain_option_arg() == null) {
                return true;
            }
            String value = option.explain_option_arg().getText();
            return !("false".equalsIgnoreCase(value) || "off".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value) || "0".equals(value));
        }
        return false;
    }

    @Override
    public Void visitCreateamstmt(CreateamstmtContext ctx) {
        BehaviorObject accessMethod = catalogNamed(TargetType.AccessMethod, ctx.name(), unquoteLiteral(text(ctx.name())));
        addUnary(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, accessMethod);
        return null;
    }

    @Override
    public Void visitCreateconversionstmt(CreateconversionstmtContext ctx) {
        addRelation(resolvedType, BehaviorAction.CREATE, object(TargetType.Conversion, ctx.any_name(0)), objects(object(TargetType.Function, ctx.any_name(1))));
        return null;
    }

    @Override
    public Void visitCreatecaststmt(CreatecaststmtContext ctx) {
        List<TypenameContext> types = ctx.typename();
        List<BehaviorObject> targets = new ArrayList<>();
        for (TypenameContext type : types) {
            BehaviorObject typeObject = object(TargetType.Type, type);
            if (typeObject == null) {
                typeObject = namedObject(TargetType.Type, type, unquote(text(type)));
            }
            addObject(targets, typeObject);
        }
        if (ctx.function_with_argtypes() != null) {
            addObject(targets, object(TargetType.Function, first(ctx.function_with_argtypes(), Func_nameContext.class)));
        }
        addRelation(resolvedType, BehaviorAction.CREATE, namedObject(TargetType.Cast, ctx, text(types.get(0)) + "->" + text(types.get(1))), targets);
        return null;
    }

    @Override
    public Void visitDropcaststmt(DropcaststmtContext ctx) {
        List<TypenameContext> types = ctx.typename();
        addUnary(resolvedType, BehaviorAction.DROP, namedObject(TargetType.Cast, ctx, text(types.get(0)) + "->" + text(types.get(1))));
        return null;
    }

    @Override
    public Void visitAlterfunctionstmt(AlterfunctionstmtContext ctx) {
        TargetType type = ctx.PROCEDURE() != null ? TargetType.Procedure : ctx.ROUTINE() != null ? TargetType.ProgramObject : TargetType.Function;
        addUnary(resolvedType, BehaviorAction.ALTER, object(type, first(ctx.function_with_argtypes(), Func_nameContext.class)));
        return null;
    }

    @Override
    public Void visitRemovefuncstmt(RemovefuncstmtContext ctx) {
        TargetType type = ctx.PROCEDURE() != null ? TargetType.Procedure : ctx.ROUTINE() != null ? TargetType.ProgramObject : TargetType.Function;
        for (Function_with_argtypesContext function : descendants(ctx.function_with_argtypes_list(), Function_with_argtypesContext.class)) {
            addUnary(resolvedType, BehaviorAction.DROP, object(type, first(function, Func_nameContext.class)));
        }
        return null;
    }

    @Override
    public Void visitRemoveaggrstmt(RemoveaggrstmtContext ctx) {
        for (Aggregate_with_argtypesContext aggregate : descendants(ctx.aggregate_with_argtypes_list(), Aggregate_with_argtypesContext.class)) {
            addUnary(resolvedType, BehaviorAction.DROP, object(TargetType.Aggregate, first(aggregate, Func_nameContext.class)));
        }
        return null;
    }

    @Override
    public Void visitRemoveoperstmt(RemoveoperstmtContext ctx) {
        for (Operator_with_argtypesContext operator : ctx.operator_with_argtypes_list().operator_with_argtypes()) {
            addUnary(resolvedType, BehaviorAction.DROP, operatorObject(operator.any_operator()));
        }
        return null;
    }

    @Override
    public Void visitDropsubscriptionstmt(DropsubscriptionstmtContext ctx) {
        addUnary(resolvedType, BehaviorAction.DROP, catalogNamed(TargetType.Subscription, ctx.name(), unquoteLiteral(text(ctx.name()))));
        return null;
    }

    @Override
    public Void visitAlterownerstmt(AlterownerstmtContext ctx) {
        transfer(ownershipSubject(ctx), ctx.rolespec());
        return null;
    }

    @Override
    public Void visitCallstmt(CallstmtContext ctx) {
        addUnary(SplitQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, ctx.func_application().func_name()));
        return null;
    }

    @Override
    public Void visitPreparestmt(PreparestmtContext ctx) {
        addUnary(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, preparedStatement(ctx.name()));
        return null;
    }

    @Override
    public Void visitExecutestmt(ExecutestmtContext ctx) {
        addUnary(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, preparedStatement(ctx.name()));
        return null;
    }

    @Override
    public Void visitDeallocatestmt(DeallocatestmtContext ctx) {
        if (ctx.name() != null) {
            addUnary(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, preparedStatement(ctx.name()));
        } else {
            addUnary(SplitQueryType.UNSAFE, BehaviorAction.UNSAFE, sessionInstance(ctx));
        }
        return null;
    }

    @Override
    public Void visitDiscardstmt(DiscardstmtContext ctx) {
        if (ctx.TEMP() != null || ctx.TEMPORARY() != null) {
            addUnary(SplitQueryType.DROP_TABLE, BehaviorAction.DROP, sessionTemporaryTableScope(ctx));
        }
        return null;
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        collectNames(context, names);
        return objects.object(type, context, names);
    }

    private void userMapping(Auth_identContext user, NameContext serverName, BehaviorAction action) {
        String userName = unquoteLiteral(text(user));
        String server = unquoteLiteral(text(serverName));
        BehaviorObject mapping = catalogNamed(TargetType.UserMapping, user, userName + "@" + server);
        BehaviorObject principal = user.rolespec() == null ? instanceNamed(TargetType.User, user, userName) : principalAs(user.rolespec(), TargetType.User);
        addRelation(resolvedType, action, mapping, objects(principal, catalogNamed(TargetType.ForeignServer, serverName, server)));
    }

    private void collectNames(ParseTree tree, List<String> names) {
        if (tree instanceof ColidContext || tree instanceof Attr_nameContext || tree instanceof Type_function_nameContext) {
            ParserRuleContext context = (ParserRuleContext) tree;
            names.add(normalizeIdentifier(parser.getTokenStream().getText(context.getStart(), context.getStop())));
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

    private void addNestedUnary(BehaviorAction action, BehaviorObject subject) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        behavior.getRelations().add(relation);
    }

    private void addRelation(SplitQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return;
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
    }

    private void addNestedRelation(BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
        if (subject == null) {
            return;
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
    }

    private BehaviorObject childObject(TargetType type, BehaviorObject parent, ParserRuleContext context) {
        if (parent == null || context == null) {
            return null;
        }
        BehaviorObject child = object(type, context);
        if (child == null || child.getObjectName() == null || StringUtils.isBlank(child.getObjectName().getObjectName())) {
            return null;
        }
        String childName = child.getObjectName().getObjectName();
        child.setObjectPath(parent.getObjectPath() + childName + "/");
        ObjectName parentName = parent.getObjectName();
        if (parentName != null) {
            child.setObjectName(new ObjectName(parentName.getCatalog(), parentName.getSchema(), childName));
        }
        return child;
    }

    private List<BehaviorObject> tableReferences(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        for (Table_refContext table : descendants(tree, Table_refContext.class)) {
            if (table.relation_expr() != null) {
                addObject(result, object(TargetType.Table, table.relation_expr().qualified_name()));
            }
        }
        return result;
    }

    private void addFunctionRelations(ParseTree tree) {
        for (Func_applicationContext function : descendants(tree, Func_applicationContext.class)) {
            addFunctionBehavior(function);
        }
    }

    private void addFunctionBehavior(Func_applicationContext function) {
        addNestedUnary(BehaviorAction.CALL, object(TargetType.Function, function.func_name()));
        String name = functionName(function.func_name());
        SconstContext literal = first(function.func_arg_list(), SconstContext.class);
        Func_arg_exprContext argument = first(function.func_arg_list(), Func_arg_exprContext.class);
        switch (name) {
            case "pg_read_file", "pg_read_binary_file", "pg_ls_dir", "pg_stat_file", "pg_ls_tmpdir" -> {
                BehaviorObject file = literal == null ? objects
                    .unnamedObject(TargetType.File, function, UmiTypes.Instance) : instanceNamed(TargetType.File, literal, unquoteLiteral(text(literal)));
                addNestedUnary(BehaviorAction.READ, file);
                addNestedUnary(BehaviorAction.UNSAFE, file);
            }
            case "set_config" -> {
                BehaviorObject config = literal == null ? objects
                    .unnamedObject(TargetType.ConfigKey, function, UmiTypes.Instance) : instanceNamed(TargetType.ConfigKey, literal, unquoteLiteral(text(literal)));
                addNestedUnary(BehaviorAction.CONFIGURE, config);
            }
            case "pg_create_physical_replication_slot", "pg_create_logical_replication_slot", "pg_copy_physical_replication_slot", "pg_copy_logical_replication_slot",
                    "pg_replication_origin_create" ->
                addNestedUnary(BehaviorAction.CREATE, replicationObject(function, literal));
            case "pg_drop_replication_slot", "pg_replication_origin_drop" -> addNestedUnary(BehaviorAction.DROP, replicationObject(function, literal));
            case "pg_replication_slot_advance", "pg_replication_origin_advance", "pg_replication_origin_session_setup", "pg_replication_origin_session_reset",
                    "pg_replication_origin_xact_setup", "pg_replication_origin_xact_reset", "pg_wal_replay_pause", "pg_wal_replay_resume", "pg_sync_replication_slots" ->
                addNestedUnary(BehaviorAction.ALTER, replicationObject(function, literal));
            case "pg_cancel_backend", "pg_terminate_backend" -> {
                BehaviorObject session = argument == null ? objects
                    .unnamedObject(TargetType.Session, function, UmiTypes.Instance) : instanceNamed(TargetType.Session, argument, text(argument));
                addNestedUnary(BehaviorAction.TERMINATE, session);
                if ("pg_terminate_backend".equals(name)) {
                    addNestedUnary(BehaviorAction.UNSAFE, session);
                }
            }
            case "pg_backup_start" -> addNestedUnary(BehaviorAction.START, catalogNamed(TargetType.Backup, function.func_name(), "backup"));
            case "pg_backup_stop" -> addNestedUnary(BehaviorAction.STOP, catalogNamed(TargetType.Backup, function.func_name(), "backup"));
            case "pg_reload_conf", "pg_import_system_collations" -> addNestedUnary(BehaviorAction.CONFIGURE, currentCatalog(function));
            case "pg_promote" -> {
                BehaviorObject replication = catalogNamed(TargetType.Replication, function.func_name(), "replication");
                // "replication" is the logical resource path, not an identifier
                // present in pg_promote(), so it must not be exposed as ObjectName.
                replication.setObjectName(null);
                addNestedUnary(BehaviorAction.ALTER, replication);
                addNestedUnary(BehaviorAction.UNSAFE, replication);
            }
            default -> {
                // The ordinary CALL relation above is the complete behavior for other functions.
            }
        }
    }

    private BehaviorObject replicationObject(Func_applicationContext function, SconstContext name) {
        return name == null ? objects.unnamedObject(TargetType.Replication, function, UmiTypes.Catalog) : catalogNamed(TargetType.Replication, name, unquoteLiteral(text(name)));
    }

    private String functionName(Func_nameContext context) {
        String name = text(context);
        int dot = name.lastIndexOf('.');
        return unquote(dot < 0 ? name : name.substring(dot + 1)).toLowerCase(Locale.ROOT);
    }

    private void addPrivilegeRelations(Privilege_targetContext target, Grantee_listContext grantees, BehaviorAction action) {
        List<BehaviorObject> principals = new ArrayList<>();
        for (RolespecContext role : descendants(grantees, RolespecContext.class)) {
            addObject(principals, principal(role));
        }
        for (BehaviorObject subject : privilegeSubjects(target)) {
            addRelation(resolvedType, action, subject, principals);
        }
    }

    private List<BehaviorObject> privilegeSubjects(Privilege_targetContext target) {
        List<BehaviorObject> result = new ArrayList<>();
        if (target == null) {
            return result;
        }
        if (target.FUNCTION() != null || target.PROCEDURE() != null || target.ROUTINE() != null) {
            TargetType type = target.FUNCTION() != null ? TargetType.Function : target.PROCEDURE() != null ? TargetType.Procedure : TargetType.ProgramObject;
            for (Function_with_argtypesContext function : descendants(target, Function_with_argtypesContext.class)) {
                addObject(result, object(type, first(function, Func_nameContext.class)));
            }
            return result;
        }
        if (target.PARAMETER() != null) {
            for (Parameter_nameContext parameter : descendants(target, Parameter_nameContext.class)) {
                addObject(result, instanceNamed(TargetType.ConfigKey, parameter, text(parameter)));
            }
            return result;
        }

        TargetType type;
        if (target.SEQUENCE() != null) {
            type = TargetType.Sequence;
        } else if (target.DATABASE() != null) {
            type = TargetType.Catalog;
        } else if (target.SCHEMA() != null) {
            type = TargetType.Schema;
        } else if (target.TABLESPACE() != null) {
            type = TargetType.Tablespace;
        } else if (target.DOMAIN_P() != null) {
            type = TargetType.Domain;
        } else if (target.TYPE_P() != null) {
            type = TargetType.Type;
        } else if (target.LANGUAGE() != null) {
            type = TargetType.Language;
        } else if (target.WRAPPER() != null) {
            type = TargetType.ForeignDataWrapper;
        } else if (target.SERVER() != null) {
            type = TargetType.ForeignServer;
        } else if (target.LARGE_P() != null) {
            type = TargetType.LargeObject;
        } else {
            type = TargetType.Table;
        }

        if (target.ALL() != null && target.SCHEMA() != null) {
            type = TargetType.Schema;
        }
        if (target.qualified_name_list() != null) {
            for (Qualified_nameContext name : target.qualified_name_list().qualified_name()) {
                addObject(result, object(type, name));
            }
        } else if (target.name_list() != null) {
            for (NameContext name : target.name_list().name()) {
                BehaviorObject object = type == TargetType.Catalog || type == TargetType.Schema ? object(type, name) : instanceNamed(type, name, unquoteLiteral(text(name)));
                addObject(result, object);
            }
        } else if (target.any_name_list_() != null) {
            for (Any_nameContext name : target.any_name_list_().any_name()) {
                addObject(result, object(type, name));
            }
        } else if (target.numericonly_list() != null) {
            for (NumericonlyContext name : target.numericonly_list().numericonly()) {
                addObject(result, instanceNamed(type, name, text(name)));
            }
        }
        return result;
    }

    private void addRoleMembershipRelations(Privilege_listContext roles, Role_listContext recipients, BehaviorAction action) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (RolespecContext recipient : recipients.rolespec()) {
            addObject(targets, principal(recipient));
        }
        for (PrivilegeContext role : roles.privilege()) {
            ColidContext name = role.colid();
            if (name != null) {
                addRelation(resolvedType, action, instanceNamed(TargetType.Role, name, unquoteLiteral(text(name))), targets);
            }
        }
    }

    private List<BehaviorObject> objects(TargetType type, ParserRuleContext context) {
        return objects(object(type, context));
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

    private void transfer(BehaviorObject subject, RolespecContext newOwner) {
        addRelation(SplitQueryType.TRANSFER_PRIVILEGE, BehaviorAction.TRANSFER, subject, objects(principal(newOwner)));
    }

    private BehaviorObject ownershipSubject(AlterownerstmtContext ctx) {
        if (ctx.aggregate_with_argtypes() != null) {
            return object(TargetType.Function, first(ctx.aggregate_with_argtypes(), Func_nameContext.class));
        }
        if (ctx.operator_with_argtypes() != null) {
            Any_operatorContext operator = ctx.operator_with_argtypes().any_operator();
            return operatorObject(operator);
        }
        if (ctx.function_with_argtypes() != null) {
            TargetType type = ctx.PROCEDURE() != null ? TargetType.Procedure : ctx.ROUTINE() != null ? TargetType.ProgramObject : TargetType.Function;
            ParserRuleContext name = first(ctx.function_with_argtypes(), Func_nameContext.class);
            return object(type, name == null ? ctx.function_with_argtypes() : name);
        }

        TargetType type;
        if (ctx.DATABASE() != null) {
            type = TargetType.Catalog;
        } else if (ctx.SCHEMA() != null) {
            type = TargetType.Schema;
        } else if (ctx.TABLESPACE() != null) {
            type = TargetType.Tablespace;
        } else if (ctx.DOMAIN_P() != null || ctx.TYPE_P() != null) {
            type = TargetType.Type;
        } else if (ctx.EVENT() != null && ctx.TRIGGER() != null) {
            type = TargetType.Trigger;
        } else if (ctx.PUBLICATION() != null) {
            type = TargetType.Publication;
        } else if (ctx.SUBSCRIPTION() != null) {
            type = TargetType.Subscription;
        } else if (ctx.OPERATOR() != null) {
            type = TargetType.Operator;
        } else {
            type = TargetType.Object;
        }

        ParserRuleContext name = ctx.any_name();
        if (name == null) {
            name = ctx.name();
        }
        if (name == null) {
            name = ctx.numericonly();
        }
        return object(type, name);
    }

    private TargetType commentTarget(CommentstmtContext context) {
        if (context.FUNCTION() != null) {
            return TargetType.Function;
        }
        if (context.PROCEDURE() != null) {
            return TargetType.Procedure;
        }
        if (context.ROUTINE() != null || context.AGGREGATE() != null) {
            return TargetType.ProgramObject;
        }
        return switch (resolvedType) {
            case COMMENT_CATALOG -> TargetType.Catalog;
            case COMMENT_SCHEMA -> TargetType.Schema;
            case COMMENT_TABLESPACE -> TargetType.Tablespace;
            case COMMENT_TABLE, COMMENT_COLUMN -> TargetType.Table;
            case COMMENT_CONSTRAINT -> TargetType.Constraint;
            case COMMENT_INDEX -> TargetType.Index;
            case COMMENT_VIEW -> TargetType.View;
            case COMMENT_MATERIALIZED_VIEW -> TargetType.Materialized;
            case COMMENT_SEQUENCE -> TargetType.Sequence;
            case COMMENT_TYPE -> TargetType.Type;
            case COMMENT_DOMAIN -> TargetType.Domain;
            case COMMENT_FUNCTION -> TargetType.Function;
            case COMMENT_PROCEDURE -> TargetType.Procedure;
            case COMMENT_TRIGGER -> TargetType.Trigger;
            case COMMENT_ROLE, COMMENT_USER -> TargetType.UserOrRole;
            case COMMENT_OPERATOR -> TargetType.Operator;
            case COMMENT_LIBRARY -> TargetType.Library;
            case COMMENT_LANGUAGE -> TargetType.Language;
            case COMMENT_TRANSFORM -> TargetType.Transform;
            case COMMENT_POLICY -> TargetType.RowAccessPolicy;
            case COMMENT_LARGE_OBJECT -> TargetType.LargeObject;
            case COMMENT_FOREIGN_DATA_WRAPPER -> TargetType.ForeignDataWrapper;
            case COMMENT_FOREIGN_SERVER -> TargetType.ForeignServer;
            case COMMENT_PROG_OBJ -> TargetType.ProgramObject;
            default -> null;
        };
    }

    private ParserRuleContext commentName(CommentstmtContext context) {
        if (resolvedType == SplitQueryType.COMMENT_CONSTRAINT && context.name() != null) {
            return context.name();
        }
        if (resolvedType == SplitQueryType.COMMENT_FUNCTION || resolvedType == SplitQueryType.COMMENT_PROCEDURE || resolvedType == SplitQueryType.COMMENT_PROG_OBJ) {
            Func_nameContext function = first(context, Func_nameContext.class);
            if (function != null) {
                return function;
            }
        }
        if (context.any_name() != null) {
            return context.any_name();
        }
        if (context.name() != null) {
            return context.name();
        }
        if (!context.typename().isEmpty()) {
            return context.typename(0);
        }
        return first(context, Func_nameContext.class);
    }

    private BehaviorObject securityLabelSubject(SeclabelstmtContext ctx) {
        if (ctx.COLUMN() != null) {
            return columnFromName(ctx.any_name());
        }
        if (ctx.aggregate_with_argtypes() != null) {
            return object(TargetType.Aggregate, first(ctx.aggregate_with_argtypes(), Func_nameContext.class));
        }
        if (ctx.function_with_argtypes() != null) {
            TargetType type = ctx.PROCEDURE() != null ? TargetType.Procedure : ctx.ROUTINE() != null ? TargetType.ProgramObject : TargetType.Function;
            return object(type, first(ctx.function_with_argtypes(), Func_nameContext.class));
        }
        if (ctx.numericonly() != null) {
            return catalogNamed(TargetType.LargeObject, ctx.numericonly(), text(ctx.numericonly()));
        }
        if (ctx.typename() != null) {
            return object(ctx.DOMAIN_P() == null ? TargetType.Type : TargetType.Domain, ctx.typename());
        }

        String objectType = ctx.object_type_any_name() != null ? text(ctx.object_type_any_name())
            .toUpperCase(Locale.ROOT) : ctx.object_type_name() != null ? text(ctx.object_type_name()).toUpperCase(Locale.ROOT) : "";
        TargetType type;
        if (objectType.equals("TABLE") || objectType.equals("FOREIGNTABLE")) {
            type = TargetType.Table;
        } else if (objectType.equals("SEQUENCE")) {
            type = TargetType.Sequence;
        } else if (objectType.equals("MATERIALIZEDVIEW")) {
            type = TargetType.Materialized;
        } else if (objectType.equals("VIEW")) {
            type = TargetType.View;
        } else if (objectType.equals("INDEX")) {
            type = TargetType.Index;
        } else if (objectType.equals("DATABASE")) {
            type = TargetType.Catalog;
        } else if (objectType.equals("ROLE")) {
            type = TargetType.Role;
        } else if (objectType.equals("SUBSCRIPTION")) {
            type = TargetType.Subscription;
        } else if (objectType.equals("PUBLICATION")) {
            type = TargetType.Publication;
        } else if (objectType.equals("TABLESPACE")) {
            type = TargetType.Tablespace;
        } else if (objectType.endsWith("LANGUAGE")) {
            type = TargetType.Language;
        } else if (objectType.equals("SCHEMA")) {
            type = TargetType.Schema;
        } else if (objectType.equals("FOREIGNDATAWRAPPER")) {
            type = TargetType.ForeignDataWrapper;
        } else if (objectType.equals("SERVER")) {
            type = TargetType.ForeignServer;
        } else {
            type = TargetType.Object;
        }
        ParserRuleContext name = ctx.any_name() != null ? ctx.any_name() : ctx.name();
        if (type == TargetType.Role) {
            return instanceNamed(type, name, unquoteLiteral(text(name)));
        }
        if (type == TargetType.Tablespace) {
            return instanceNamed(type, name, unquoteLiteral(text(name)));
        }
        if (type == TargetType.Publication || type == TargetType.Subscription || type == TargetType.Language || type == TargetType.ForeignDataWrapper
            || type == TargetType.ForeignServer) {
            return catalogNamed(type, name, unquoteLiteral(text(name)));
        }
        return object(type, name);
    }

    private BehaviorObject tableFromColumnName(Any_nameContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        names.add(unquote(text(context.colid())));
        List<Attr_nameContext> attributes = context.attrs() == null ? List.of() : context.attrs().attr_name();
        for (Attr_nameContext attribute : attributes) {
            names.add(unquote(text(attribute)));
        }
        if (names.size() < 2) {
            return objects.unnamedObject(TargetType.Table, context, UmiTypes.Schema);
        }
        names.remove(names.size() - 1);
        Token stop = attributes.size() == 1 ? context.colid().getStop() : attributes.get(attributes.size() - 2).getStop();
        return objects.object(TargetType.Table, context.getStart(), stop, names);
    }

    private BehaviorObject columnFromName(Any_nameContext context) {
        if (context == null || context.attrs() == null || context.attrs().attr_name().isEmpty()) {
            return objects.unnamedObject(TargetType.Column, context, UmiTypes.Schema);
        }
        List<Attr_nameContext> attributes = context.attrs().attr_name();
        return childObject(TargetType.Column, tableFromColumnName(context), attributes.get(attributes.size() - 1));
    }

    private BehaviorObject principal(RolespecContext context) {
        if (context == null) {
            return null;
        }
        TargetType type;
        if (context.CURRENT_USER() != null || context.SESSION_USER() != null) {
            type = TargetType.User;
        } else if (context.CURRENT_ROLE() != null) {
            type = TargetType.Role;
        } else {
            type = TargetType.UserOrRole;
        }
        return instanceNamed(type, context, unquoteLiteral(text(context)));
    }

    private BehaviorObject principalAs(RolespecContext context, TargetType type) {
        return context == null ? null : instanceNamed(type, context, unquoteLiteral(text(context)));
    }

    private BehaviorObject instanceNamed(TargetType type, ParserRuleContext context, String name) {
        if (context == null || StringUtils.isBlank(name)) {
            return null;
        }
        return objects.instanceObject(type, context.getStart(), context.getStop(), name);
    }

    private BehaviorObject instanceNamed(TargetType type, Token token, String name) {
        if (token == null || StringUtils.isBlank(name)) {
            return null;
        }
        return objects.instanceObject(type, token, name);
    }

    private BehaviorObject literalObject(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        return objects.object(type, context.getStart(), context.getStop(), List.of(unquoteLiteral(text(context))));
    }

    private BehaviorObject namedObject(TargetType type, ParserRuleContext context, String name) {
        if (context == null || StringUtils.isBlank(name)) {
            return null;
        }
        return objects.object(type, context, List.of(name));
    }

    private BehaviorObject operatorObject(Any_operatorContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        Any_operatorContext current = context;
        while (current.colid() != null) {
            names.add(unquote(text(current.colid())));
            current = current.any_operator();
        }
        names.add(unquote(text(current)));
        return objects.object(TargetType.Operator, context, names);
    }

    private String text(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private <T extends ParserRuleContext> T first(ParseTree tree, Class<T> type) {
        List<T> result = descendants(tree, type);
        return result.isEmpty() ? null : result.get(0);
    }

    private Qualified_nameContext firstQualifiedName(ParserRuleContext context) {
        List<Qualified_nameContext> names = descendants(context, Qualified_nameContext.class);
        return names.isEmpty() ? null : names.get(0);
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
        if (target.getObjectType() == TargetType.Catalog) {
            target.setObjectName(new ObjectName(targetName.getCatalog(), null, null));
        } else if (target.getObjectType() == TargetType.Schema) {
            target.setObjectName(new ObjectName(sourceName.getCatalog(), targetName.getSchema(), null));
        } else {
            target.setObjectName(new ObjectName(sourceName.getCatalog(), sourceName.getSchema(), targetName.getObjectName()));
        }
    }

    private BehaviorObject catalogNamed(TargetType type, ParserRuleContext context, String name) {
        if (context == null || StringUtils.isBlank(name)) {
            return null;
        }
        return objects.catalogObject(type, context, name);
    }

    private BehaviorObject currentCatalog(ParserRuleContext context) {
        // The current catalog is implicit here; the source range contains a
        // keyword or function, not the configured catalog name.
        return objects.unnamedObject(TargetType.Catalog, context, UmiTypes.Catalog);
    }

    private void addMaintenanceRelations(SplitQueryType type, BehaviorAction action, ParserRuleContext context) {
        List<Qualified_nameContext> names = descendants(context, Qualified_nameContext.class);
        if (names.isEmpty()) {
            addUnary(type, action, currentCatalog(context));
            return;
        }
        for (Qualified_nameContext name : names) {
            addUnary(type, action, object(TargetType.Table, name));
        }
    }

    private List<BehaviorObject> publicationTargets(ParseTree context) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (Relation_exprContext relation : descendants(context, Relation_exprContext.class)) {
            addObject(targets, object(TargetType.Table, relation.qualified_name()));
        }
        for (Publication_schema_nameContext schema : descendants(context, Publication_schema_nameContext.class)) {
            if (schema.name() != null) {
                addObject(targets, object(TargetType.Schema, schema.name()));
            }
        }
        return targets;
    }

    private List<BehaviorObject> subscriptionTargets(Publication_name_listContext publications) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (Publication_name_itemContext publication : descendants(publications, Publication_name_itemContext.class)) {
            ColLabelContext name = publication.colLabel();
            addObject(targets, catalogNamed(TargetType.Publication, name, unquoteLiteral(text(name))));
        }
        return targets;
    }

    private BehaviorObject sessionTemporaryTableScope(ParserRuleContext context) {
        List<String> path = new ArrayList<>();
        addLevel(path, UmiTypes.Instance, true);
        addLevel(path, UmiTypes.Catalog, false);
        addLevel(path, UmiTypes.Schema, false);

        Token start = context.getStart();
        Token stop = context.getStop();
        BehaviorObject object = new BehaviorObject();
        object.setObjectType(TargetType.Table);
        object.setObjectPath(path.isEmpty() ? "/" : "/" + String.join("/", path) + "/");
        object.setStartLine(line(start));
        object.setStartColumn(column(start));
        object.setEndLine(line(stop));
        object.setEndColumn(column(stop) + stop.getText().length());
        return object;
    }

    private BehaviorObject sessionInstance(ParserRuleContext context) {
        List<String> path = new ArrayList<>();
        addLevel(path, UmiTypes.Instance, true);
        Token start = context.getStart();
        Token stop = context.getStop();
        BehaviorObject object = new BehaviorObject();
        object.setObjectType(TargetType.Instance);
        object.setObjectPath(path.isEmpty() ? "/" : "/" + String.join("/", path) + "/");
        object.setStartLine(line(start));
        object.setStartColumn(column(start));
        object.setEndLine(line(stop));
        object.setEndColumn(column(stop) + stop.getText().length());
        return object;
    }

    private BehaviorObject preparedStatement(NameContext context) {
        BehaviorObject object = object(TargetType.PrepareStatement, context);
        if (object == null) {
            return null;
        }
        String name = unquote(text(context));
        List<String> path = new ArrayList<>();
        addLevel(path, UmiTypes.Instance, true);
        path.add(name);
        object.setObjectPath("/" + String.join("/", path) + "/");
        object.setObjectName(new ObjectName(null, null, name));
        return object;
    }

    private void addLevel(List<String> path, UmiTypes type, boolean split) {
        if (levels == null || levels.get(type) == null) {
            return;
        }
        String value = StringUtils.toString(levels.get(type));
        if (StringUtils.isBlank(value)) {
            return;
        }
        if (split) {
            int start = 0;
            for (int i = 0; i <= value.length(); i++) {
                if (i == value.length() || value.charAt(i) == '/') {
                    String part = value.substring(start, i);
                    if (StringUtils.isNotBlank(part)) {
                        path.add(part);
                    }
                    start = i + 1;
                }
            }
        } else {
            path.add(value);
        }
    }

    private int line(Token token) {
        return baseLine + token.getLine() - 1;
    }

    private int column(Token token) {
        return token.getLine() == 1 ? baseColumn + token.getCharPositionInLine() : token.getCharPositionInLine();
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

    private String normalizeIdentifier(String value) {
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        return value.toLowerCase(Locale.ROOT);
    }

    private String unquoteLiteral(String value) {
        if (value.length() >= 2) {
            char first = value.charAt(0);
            char last = value.charAt(value.length() - 1);
            if (first == last && (first == '\'' || first == '"')) {
                return value.substring(1, value.length() - 1).replace(first == '\'' ? "''" : "\"\"", Character.toString(first));
            }
        }
        return unquote(value);
    }
}
