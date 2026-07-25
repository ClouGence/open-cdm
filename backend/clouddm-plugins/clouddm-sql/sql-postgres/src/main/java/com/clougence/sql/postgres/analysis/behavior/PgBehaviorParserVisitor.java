/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.postgres.analysis.behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorObject;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorRelation;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
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
        SecQueryType statementType = new PgSplitVisitor(version).visit(tree);
        PgStatementBehaviorVisitor visitor = new PgStatementBehaviorVisitor(parser, statementType, levels, baseLine, baseColumn);
        visitor.visit(tree);
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
    private final SecQueryType             resolvedType;
    private final StatementBehavior        behavior = new StatementBehavior();

    PgStatementBehaviorVisitor(Parser parser, SecQueryType statementType, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.levels = levels;
        this.baseLine = Math.max(1, baseLine);
        this.baseColumn = Math.max(0, baseColumn);
        this.resolvedType = statementType == null ? SecQueryType.UNKNOWN : statementType;
        this.behavior.setStatementType(this.resolvedType);
    }

    StatementBehavior behavior() {
        return behavior;
    }

    @Override
    public Void visitTable_ref(Table_refContext ctx) {
        if (ctx.relation_expr() != null) {
            addUnary(SecQueryType.SELECT, BehaviorAction.READ, object(TargetType.Table, ctx.relation_expr().qualified_name()));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitInsertstmt(InsertstmtContext ctx) {
        addRelation(SecQueryType.INSERT, BehaviorAction.INSERT, object(TargetType.Table, ctx.insert_target().qualified_name()), tableReferences(ctx.insert_rest()));
        return null;
    }

    @Override
    public Void visitUpdatestmt(UpdatestmtContext ctx) {
        addRelation(SecQueryType.UPDATE, BehaviorAction.UPDATE, object(TargetType.Table, ctx.relation_expr_opt_alias().relation_expr().qualified_name()), tableReferences(ctx));
        return null;
    }

    @Override
    public Void visitDeletestmt(DeletestmtContext ctx) {
        addRelation(SecQueryType.DELETE, BehaviorAction.DELETE, object(TargetType.Table, ctx.relation_expr_opt_alias().relation_expr().qualified_name()), tableReferences(ctx));
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
            addRelation(SecQueryType.CREATE_TABLE, BehaviorAction.CREATE, object(TargetType.Table, names.get(0)), targets);
        }
        return null;
    }

    @Override
    public Void visitCreateasstmt(CreateasstmtContext ctx) {
        BehaviorObject subject = object(TargetType.Table, ctx.create_as_target().qualified_name());
        List<BehaviorObject> targets = tableReferences(ctx.selectstmt());
        if (ctx.selectstmt() == null && ctx.qualified_name() != null) {
            addObject(targets, object(TargetType.Table, ctx.qualified_name()));
        }
        addRelation(SecQueryType.CREATE_TABLE, BehaviorAction.CREATE, subject, targets);
        return null;
    }

    @Override
    public Void visitCreatepolicystmt(CreatepolicystmtContext ctx) {
        addRelation(SecQueryType.CREATE_POLICY, BehaviorAction.CREATE, object(TargetType.RowAccessPolicy, ctx.name()), objects(TargetType.Table, ctx.qualified_name()));
        return null;
    }

    @Override
    public Void visitAlterpolicystmt(AlterpolicystmtContext ctx) {
        addRelation(SecQueryType.ALTER_POLICY, BehaviorAction.ALTER, object(TargetType.RowAccessPolicy, ctx.name(0)), objects(TargetType.Table, ctx.qualified_name()));
        return null;
    }

    @Override
    public Void visitIndexstmt(IndexstmtContext ctx) {
        ParserRuleContext indexName = ctx.index_name_() != null ? ctx.index_name_() : ctx.name();
        addRelation(SecQueryType.ADD_INDEX, BehaviorAction.CREATE, object(TargetType.Index, indexName), objects(TargetType.Table, ctx.relation_expr().qualified_name()));
        return null;
    }

    @Override
    public Void visitViewstmt(ViewstmtContext ctx) {
        addRelation(SecQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.View, ctx.qualified_name()), tableReferences(ctx.selectstmt()));
        return null;
    }

    @Override
    public Void visitCreatematviewstmt(CreatematviewstmtContext ctx) {
        addRelation(SecQueryType.CREATE_VIEW, BehaviorAction.CREATE, object(TargetType.Materialized, ctx.create_mv_target().qualified_name()), tableReferences(ctx.selectstmt()));
        return null;
    }

    @Override
    public Void visitCreatefunctionstmt(CreatefunctionstmtContext ctx) {
        TargetType targetType = ctx.PROCEDURE() == null ? TargetType.Function : TargetType.Procedure;
        addUnary(SecQueryType.CREATE_PROG_OBJ, BehaviorAction.CREATE, object(targetType, ctx.func_name()));
        return null;
    }

    @Override
    public Void visitCreatetrigstmt(CreatetrigstmtContext ctx) {
        List<BehaviorObject> targets = objects(TargetType.Table, firstQualifiedName(ctx));
        addObject(targets, object(TargetType.Function, ctx.func_name()));
        addRelation(SecQueryType.CREATE_TRIGGER, BehaviorAction.CREATE, object(TargetType.Trigger, ctx.name()), targets);
        return null;
    }

    @Override
    public Void visitCreateseqstmt(CreateseqstmtContext ctx) {
        addUnary(SecQueryType.CREATE_SEQUENCE, BehaviorAction.CREATE, object(TargetType.Sequence, ctx.qualified_name()));
        return null;
    }

    @Override
    public Void visitCreatedbstmt(CreatedbstmtContext ctx) {
        addUnary(SecQueryType.CREATE_CATALOG, BehaviorAction.CREATE, object(TargetType.Catalog, ctx.name()));
        return null;
    }

    @Override
    public Void visitDropdbstmt(DropdbstmtContext ctx) {
        addUnary(SecQueryType.DROP_CATALOG, BehaviorAction.DROP, object(TargetType.Catalog, ctx.name()));
        return null;
    }

    @Override
    public Void visitCreateschemastmt(CreateschemastmtContext ctx) {
        if (ctx.optschemaname() != null) {
            addUnary(SecQueryType.CREATE_SCHEMA, BehaviorAction.CREATE, object(TargetType.Schema, ctx.optschemaname()));
        }
        return null;
    }

    @Override
    public Void visitDropschemastmt(DropschemastmtContext ctx) {
        for (Qualified_nameContext name : ctx.qualified_name_list().qualified_name()) {
            addUnary(SecQueryType.DROP_SCHEMA, BehaviorAction.DROP, object(TargetType.Schema, name));
        }
        return null;
    }

    @Override
    public Void visitDroptablestmt(DroptablestmtContext ctx) {
        for (Any_nameContext name : ctx.any_name_list_().any_name()) {
            addUnary(SecQueryType.DROP_TABLE, BehaviorAction.DROP, object(TargetType.Table, name));
        }
        return null;
    }

    @Override
    public Void visitDropstmt(DropstmtContext ctx) {
        TargetType targetType = resolvedType.getTarget();
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
                addUnary(resolvedType, BehaviorAction.DROP, object(targetType, name));
            }
        } else if (ctx.any_name() != null) {
            addUnary(resolvedType, BehaviorAction.DROP, object(targetType, ctx.any_name()));
        } else if (ctx.name() != null) {
            addUnary(resolvedType, BehaviorAction.DROP, object(targetType, ctx.name()));
        }
        return null;
    }

    @Override
    public Void visitRename_table_stmt(Rename_table_stmtContext ctx) {
        BehaviorObject source = object(TargetType.Table, ctx.relation_expr().qualified_name());
        BehaviorObject target = object(TargetType.Table, ctx.name());
        moveToSameContainer(source, target);
        addRelation(SecQueryType.RENAME_TABLE, BehaviorAction.RENAME, source, objects(target));
        return null;
    }

    @Override
    public Void visitTruncatestmt(TruncatestmtContext ctx) {
        for (Relation_exprContext relation : ctx.relation_expr_list().relation_expr()) {
            addUnary(SecQueryType.TRUNCATE_TABLE, BehaviorAction.ALTER, object(TargetType.Table, relation.qualified_name()));
        }
        return null;
    }

    @Override
    public Void visitReassignownedstmt(ReassignownedstmtContext ctx) {
        BehaviorObject newOwner = principal(ctx.rolespec());
        for (RolespecContext oldOwner : ctx.role_list().rolespec()) {
            addRelation(SecQueryType.TRANSFER_PRIVILEGE, BehaviorAction.TRANSFER, principal(oldOwner), objects(newOwner));
        }
        return null;
    }

    @Override
    public Void visitDropownedstmt(DropownedstmtContext ctx) {
        for (RolespecContext owner : ctx.role_list().rolespec()) {
            addUnary(SecQueryType.REVOKE, BehaviorAction.REVOKE, principal(owner));
        }
        return null;
    }

    @Override
    public Void visitAltertablestmt(AltertablestmtContext ctx) {
        if (resolvedType != SecQueryType.TRANSFER_PRIVILEGE) {
            return visitChildren(ctx);
        }
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
        transfer(object(targetType, subjectName), first(ctx, RolespecContext.class));
        return null;
    }

    @Override
    public Void visitAlterseqstmt(AlterseqstmtContext ctx) {
        if (resolvedType != SecQueryType.TRANSFER_PRIVILEGE) {
            return visitChildren(ctx);
        }
        transfer(object(TargetType.Sequence, ctx.qualified_name()), first(ctx, RolespecContext.class));
        return null;
    }

    @Override
    public Void visitAlterownerstmt(AlterownerstmtContext ctx) {
        transfer(ownershipSubject(ctx), ctx.rolespec());
        return null;
    }

    @Override
    public Void visitCallstmt(CallstmtContext ctx) {
        addUnary(SecQueryType.CALL_PROG_OBJ, BehaviorAction.CALL, object(TargetType.Procedure, ctx.func_application().func_name()));
        return null;
    }

    @Override
    public Void visitDiscardstmt(DiscardstmtContext ctx) {
        if (ctx.TEMP() != null || ctx.TEMPORARY() != null) {
            addUnary(SecQueryType.DROP_TABLE, BehaviorAction.DROP, sessionTemporaryTableScope(ctx));
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

    private void collectNames(ParseTree tree, List<String> names) {
        if (tree instanceof ColidContext || tree instanceof Attr_nameContext || tree instanceof Type_function_nameContext) {
            ParserRuleContext context = (ParserRuleContext) tree;
            names.add(unquote(parser.getTokenStream().getText(context.getStart(), context.getStop())));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectNames(tree.getChild(i), names);
        }
    }

    private void addUnary(SecQueryType type, BehaviorAction action, BehaviorObject subject) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        behavior.getRelations().add(relation);
        behavior.setStatementType(type);
    }

    private void addRelation(SecQueryType type, BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
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

    private List<BehaviorObject> tableReferences(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        for (Table_refContext table : descendants(tree, Table_refContext.class)) {
            if (table.relation_expr() != null) {
                addObject(result, object(TargetType.Table, table.relation_expr().qualified_name()));
            }
        }
        return result;
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
        addRelation(SecQueryType.TRANSFER_PRIVILEGE, BehaviorAction.TRANSFER, subject, objects(principal(newOwner)));
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
        return namedObject(type, context, unquote(text(context)));
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
        String sourcePath = source.getResourcePath();
        String targetPath = target.getResourcePath();
        int sourceNameStart = sourcePath.lastIndexOf('/', sourcePath.length() - 2);
        int targetNameStart = targetPath.lastIndexOf('/', targetPath.length() - 2);
        if (sourceNameStart >= 0 && targetNameStart >= 0) {
            target.setResourcePath(sourcePath.substring(0, sourceNameStart + 1) + targetPath.substring(targetNameStart + 1));
        }
    }

    private BehaviorObject sessionTemporaryTableScope(ParserRuleContext context) {
        List<String> path = new ArrayList<>();
        addLevel(path, UmiTypes.Instance, true);
        addLevel(path, UmiTypes.Catalog, false);
        addLevel(path, UmiTypes.Schema, false);

        Token start = context.getStart();
        Token stop = context.getStop();
        BehaviorObject object = new BehaviorObject();
        object.setTargetType(TargetType.Table);
        object.setResourcePath(path.isEmpty() ? "/" : "/" + String.join("/", path) + "/");
        object.setStartLine(line(start));
        object.setStartColumn(column(start));
        object.setEndLine(line(stop));
        object.setEndColumn(column(stop) + stop.getText().length());
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
            for (String part : value.split("/")) {
                if (StringUtils.isNotBlank(part)) {
                    path.add(part);
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
}
