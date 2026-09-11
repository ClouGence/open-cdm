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
package com.clougence.sql.postgres.analysis.behavior;

import static com.clougence.sql.postgres.parser.antlr.PgSqlParser.*;

import java.util.*;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.behavior.RdbBehaviorObjectFactory;
import com.clougence.sql.postgres.parser.PgSplitVisitor;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.sql.postgres.parser.antlr.PgSqlParserBaseVisitor;
import com.clougence.utils.StringUtils;

final class PgStatementBehaviorVisitor extends PgSqlParserBaseVisitor<Void> {

    private final Parser                   parser;
    private final RdbBehaviorObjectFactory objects;
    private final Map<UmiTypes, Object>    levels;
    private final int                      baseLine;
    private final int                      baseColumn;
    private final SplitQueryType           resolvedType;
    private final PostgresVersion          version;
    private final StatementBehavior        behavior = new StatementBehavior();

    PgStatementBehaviorVisitor(Parser parser, PostgresVersion version, SplitQueryType statementType, Map<UmiTypes, Object> levels, int baseLine, int baseColumn){
        this.parser = parser;
        this.version = version;
        this.objects = new RdbBehaviorObjectFactory(levels, baseLine, baseColumn);
        this.levels = levels;
        this.baseLine = Math.max(1, baseLine);
        this.baseColumn = Math.max(0, baseColumn);
        this.resolvedType = statementType == null ? SplitQueryType.UNKNOWN : statementType;
        this.behavior.setStatementType(this.resolvedType);
    }

    private ParseTree            root;
    private final Set<ParseTree> deferredBodies = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Set<ParseTree> mutations      = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Set<ParseTree> functions      = Collections.newSetFromMap(new IdentityHashMap<>());
    private final Set<ParseTree> tableReads     = Collections.newSetFromMap(new IdentityHashMap<>());

    @Override
    public Void visit(ParseTree tree) {
        if (root == null) {
            root = tree;
        }
        return super.visit(tree);
    }

    StatementBehavior behavior() {
        scanNestedBehaviors(root);
        return behavior;
    }

    private void addFunction(Func_applicationContext ctx) {
        List<String> names = new ArrayList<>();
        collectNames(ctx.func_name(), names);
        PgFunctionBehavior rule = PgFunctionBehaviorRegistry.INSTANCE.behavior(names);
        addUnary(rule.action(), object(TargetType.Function, ctx.func_name()));
        if (rule.targetType() == null || ctx.func_arg_list() == null)
            return;
        Func_arg_exprContext argument = ctx.func_arg_list().func_arg_expr(0);
        SconstContext literal = first(argument, SconstContext.class);
        BehaviorObject resource;
        if (literal != null && text(argument).equals(text(literal))) {
            String name = stringValue(literal);
            if (rule.targetType() == TargetType.File)
                name = name.replaceFirst("^/+", "");
            if (name.isEmpty())
                resource = objects.instanceObject(rule.targetType(), literal);
            else {
                resource = objects.instanceObject(rule.targetType(), literal, name);
                resource.setObjectName(new ObjectName(null, null, name));
            }
        } else {
            resource = objects.instanceObject(rule.targetType(), argument);
        }
        addUnary(rule.targetAction(), resource);
        if (rule.unsafe())
            addUnary(BehaviorAction.UNSAFE, resource);
    }

    private void scanNestedBehaviors(ParseTree tree) {
        if (tree == null || deferredBodies.contains(tree)) {
            return;
        }
        if ((tree instanceof InsertstmtContext || tree instanceof UpdatestmtContext || tree instanceof DeletestmtContext || tree instanceof MergestmtContext)
            && !mutations.contains(tree)) {
            visit(tree);
        }
        if (tree instanceof Func_applicationContext call && functions.add(call)) {
            addFunction(call);
        }
        if (tree instanceof Relation_exprContext table && isQueryTable(table) && tableReads.add(table) && !isCte(table)) {
            addUnary(BehaviorAction.READ, object(TargetType.Table, table.qualified_name()));
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            scanNestedBehaviors(tree.getChild(i));
        }
    }

    @Override
    public Void visitExplainstmt(ExplainstmtContext ctx) {
        if (new PgSplitVisitor(version).visit(ctx) != SplitQueryType.PERFORMANCE) {
            addUnary(BehaviorAction.UNSAFE, objects.instanceObject(TargetType.Instance, ctx));
            return visitChildren(ctx);
        }
        deferredBodies.add(ctx);
        for (BehaviorObject table : explainTables(ctx)) {
            addUnary(BehaviorAction.READ, table);
        }
        return null;
    }

    private List<BehaviorObject> explainTables(ExplainstmtContext ctx) {
        List<BehaviorObject> result = tableReferences(ctx);
        InsertstmtContext insert = first(ctx, InsertstmtContext.class);
        if (insert != null) {
            addObject(result, object(TargetType.Table, insert.insert_target().qualified_name()));
        }
        UpdatestmtContext update = first(ctx, UpdatestmtContext.class);
        if (update != null) {
            addObject(result, object(TargetType.Table, update.relation_expr_opt_alias().relation_expr().qualified_name()));
        }
        DeletestmtContext delete = first(ctx, DeletestmtContext.class);
        if (delete != null) {
            addObject(result, object(TargetType.Table, delete.relation_expr_opt_alias().relation_expr().qualified_name()));
        }
        MergestmtContext merge = first(ctx, MergestmtContext.class);
        if (merge != null) {
            addObject(result, object(TargetType.Table, merge.relation_expr_opt_alias().relation_expr().qualified_name()));
        }
        return result;
    }

    @Override
    public Void visitTable_ref(Table_refContext ctx) {
        if (ctx.relation_expr() != null && tableReads.add(ctx.relation_expr()) && !isCte(ctx.relation_expr())) {
            addUnary(BehaviorAction.READ, object(TargetType.Table, ctx.relation_expr().qualified_name()));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitSelect_no_parens(Select_no_parensContext ctx) {
        for (Simple_select_pramaryContext select : ctx.select_clause().simple_select_pramary()) {
            if (select.into_clause() != null) {
                addRelation(BehaviorAction.CREATE, object(TargetType.Table, select.into_clause().opttempTableName().qualified_name()), tableReferences(ctx));
                return null;
            }
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitSimple_select_pramary(Simple_select_pramaryContext ctx) {
        if (ctx.TABLE() != null && tableReads.add(ctx.relation_expr()) && !isCte(ctx.relation_expr())) {
            addUnary(BehaviorAction.READ, object(TargetType.Table, ctx.relation_expr().qualified_name()));
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitInsertstmt(InsertstmtContext ctx) {
        if (!mutations.add(ctx))
            return null;
        BehaviorAction action = BehaviorAction.INSERT;
        if (ctx.on_conflict_() != null) {
            action = BehaviorAction.MERGE;
        }
        BehaviorRelation relation = addRelation(action, object(TargetType.Table, ctx.insert_target().qualified_name()), tableReferences(ctx));
        if (relation != null) {
            Values_clauseContext values = ctx.insert_rest().values_clause();
            if (values != null) {
                relation.setInsertRows((long) values.expr_list().size());
            } else if (ctx.insert_rest().selectstmt() == null) {
                relation.setInsertRows(1L);
            }
        }
        return null;
    }

    @Override
    public Void visitUpdatestmt(UpdatestmtContext ctx) {
        if (!mutations.add(ctx))
            return null;
        addRelation(BehaviorAction.UPDATE, object(TargetType.Table, ctx.relation_expr_opt_alias().relation_expr().qualified_name()), tableReferences(ctx));
        return null;
    }

    @Override
    public Void visitDeletestmt(DeletestmtContext ctx) {
        if (!mutations.add(ctx))
            return null;
        addRelation(BehaviorAction.DELETE, object(TargetType.Table, ctx.relation_expr_opt_alias().relation_expr().qualified_name()), tableReferences(ctx));
        return null;
    }

    @Override
    public Void visitCreatestmt(CreatestmtContext ctx) {
        List<Qualified_nameContext> names = descendants(ctx, Qualified_nameContext.class);
        if (!names.isEmpty()) {
            List<BehaviorObject> targets = new ArrayList<>();
            for (int i = 1; i < names.size(); i++) {
                addObject(targets, object(TargetType.Table, names.get(i)));
            }
            addRelation(BehaviorAction.CREATE, object(TargetType.Table, names.get(0)), targets);
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
        addRelation(BehaviorAction.CREATE, subject, targets);
        return null;
    }

    @Override
    public Void visitCreatepolicystmt(CreatepolicystmtContext ctx) {
        addRelation(BehaviorAction.CREATE, object(TargetType.RowAccessPolicy, ctx.name()), objects(TargetType.Table, ctx.qualified_name()));
        return null;
    }

    @Override
    public Void visitAlterpolicystmt(AlterpolicystmtContext ctx) {
        addRelation(BehaviorAction.ALTER, object(TargetType.RowAccessPolicy, ctx.name(0)), objects(TargetType.Table, ctx.qualified_name()));
        return null;
    }

    @Override
    public Void visitIndexstmt(IndexstmtContext ctx) {
        ParserRuleContext indexName = ctx.index_name_() != null ? ctx.index_name_() : ctx.name();
        BehaviorObject index = object(TargetType.Index, indexName);
        if (index == null)
            index = objects.unnamedObject(TargetType.Index, ctx, UmiTypes.Schema);
        addRelation(BehaviorAction.CREATE, index, objects(TargetType.Table, ctx.relation_expr().qualified_name()));
        return null;
    }

    @Override
    public Void visitViewstmt(ViewstmtContext ctx) {
        BehaviorAction action = BehaviorAction.CREATE;
        if (ctx.REPLACE() != null) {
            action = BehaviorAction.REPLACE;
        }
        addRelation(action, object(TargetType.View, ctx.qualified_name()), tableReferences(ctx.selectstmt()));
        return null;
    }

    @Override
    public Void visitCreatematviewstmt(CreatematviewstmtContext ctx) {
        addRelation(BehaviorAction.CREATE, object(TargetType.Materialized, ctx.create_mv_target().qualified_name()), tableReferences(ctx.selectstmt()));
        return null;
    }

    @Override
    public Void visitCreatefunctionstmt(CreatefunctionstmtContext ctx) {
        deferredBodies.add(ctx);
        TargetType targetType = ctx.PROCEDURE() == null ? TargetType.Function : TargetType.Procedure;
        BehaviorObject function = object(targetType, ctx.func_name());
        BehaviorAction action = BehaviorAction.CREATE;
        if (ctx.or_replace_() != null) {
            action = BehaviorAction.REPLACE;
        }
        addUnary(action, function);
        if (ctx.sql_body() != null) {
            deferredBodies.remove(ctx);
            visit(ctx.sql_body());
        } else {
            addUnary(BehaviorAction.UNSAFE, function);
        }
        return null;
    }

    @Override
    public Void visitCreatetrigstmt(CreatetrigstmtContext ctx) {
        deferredBodies.add(ctx);
        List<BehaviorObject> targets = objects(TargetType.Table, firstQualifiedName(ctx));
        addObject(targets, object(TargetType.Function, ctx.func_name()));
        BehaviorAction action = BehaviorAction.CREATE;
        if (ctx.or_replace_() != null) {
            action = BehaviorAction.REPLACE;
        }
        addRelation(action, object(TargetType.Trigger, ctx.name()), targets);
        addUnary(BehaviorAction.UNSAFE, object(TargetType.Trigger, ctx.name()));
        return null;
    }

    @Override
    public Void visitCreateseqstmt(CreateseqstmtContext ctx) {
        addUnary(BehaviorAction.CREATE, object(TargetType.Sequence, ctx.qualified_name()));
        return null;
    }

    @Override
    public Void visitCreatedbstmt(CreatedbstmtContext ctx) {
        addUnary(BehaviorAction.CREATE, object(TargetType.Catalog, ctx.name()));
        return null;
    }

    @Override
    public Void visitDropdbstmt(DropdbstmtContext ctx) {
        addUnary(BehaviorAction.DROP, object(TargetType.Catalog, ctx.name()));
        return null;
    }

    @Override
    public Void visitCreateschemastmt(CreateschemastmtContext ctx) {
        if (ctx.optschemaname() != null) {
            addUnary(BehaviorAction.CREATE, object(TargetType.Schema, ctx.optschemaname()));
        }
        return null;
    }

    @Override
    public Void visitDropschemastmt(DropschemastmtContext ctx) {
        for (Qualified_nameContext name : ctx.qualified_name_list().qualified_name()) {
            addUnary(BehaviorAction.DROP, object(TargetType.Schema, name));
        }
        return null;
    }

    @Override
    public Void visitDroptablestmt(DroptablestmtContext ctx) {
        for (Any_nameContext name : ctx.any_name_list_().any_name()) {
            addUnary(BehaviorAction.DROP, object(TargetType.Table, name));
        }
        return null;
    }

    @Override
    public Void visitDropstmt(DropstmtContext ctx) {
        TargetType targetType = declaredType(ctx);
        if (ctx.object_type_name_on_any_name() != null && ctx.object_type_name_on_any_name().POLICY() != null) {
            targetType = TargetType.RowAccessPolicy;
        }
        if (ctx.object_type_name_on_any_name() != null && ctx.name() != null) {
            addRelation(BehaviorAction.DROP, object(targetType, ctx.name()), objects(TargetType.Table, ctx.any_name()));
        } else if (ctx.any_name_list_() != null) {
            for (Any_nameContext name : ctx.any_name_list_().any_name()) {
                addUnary(BehaviorAction.DROP, object(targetType, name));
            }
        } else if (ctx.name_list() != null) {
            for (NameContext name : ctx.name_list().name()) {
                addUnary(BehaviorAction.DROP, catalogObject(targetType, name));
            }
        } else if (ctx.type_name_list() != null) {
            for (TypenameContext name : ctx.type_name_list().typename())
                addUnary(BehaviorAction.DROP, object(targetType, name));
        } else if (ctx.any_name() != null) {
            addUnary(BehaviorAction.DROP, object(targetType, ctx.any_name()));
        } else if (ctx.name() != null) {
            addUnary(BehaviorAction.DROP, object(targetType, ctx.name()));
        }
        return null;
    }

    @Override
    public Void visitRename_table_stmt(Rename_table_stmtContext ctx) {
        BehaviorObject source = object(TargetType.Table, ctx.relation_expr().qualified_name());
        BehaviorObject target = object(TargetType.Table, ctx.name());
        moveToSameContainer(source, target);
        addRelation(BehaviorAction.RENAME, source, objects(target));
        return null;
    }

    @Override
    public Void visitTruncatestmt(TruncatestmtContext ctx) {
        for (Relation_exprContext relation : ctx.relation_expr_list().relation_expr()) {
            addUnary(BehaviorAction.ALTER, object(TargetType.Table, relation.qualified_name()));
        }
        return null;
    }

    @Override
    public Void visitReassignownedstmt(ReassignownedstmtContext ctx) {
        BehaviorObject newOwner = principal(ctx.rolespec());
        for (RolespecContext oldOwner : ctx.role_list().rolespec()) {
            addRelation(BehaviorAction.TRANSFER, principal(oldOwner), objects(newOwner));
        }
        return null;
    }

    @Override
    public Void visitDropownedstmt(DropownedstmtContext ctx) {
        for (RolespecContext owner : ctx.role_list().rolespec()) {
            addUnary(BehaviorAction.REVOKE, principal(owner));
        }
        return null;
    }

    @Override
    public Void visitAltertablestmt(AltertablestmtContext ctx) {
        if (resolvedType != SplitQueryType.TRANSFER_PRIVILEGE) {
            alterTable(ctx);
            return null;
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
        if (resolvedType != SplitQueryType.TRANSFER_PRIVILEGE) {
            addUnary(BehaviorAction.ALTER, object(TargetType.Sequence, ctx.qualified_name()));
            return null;
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
        functions.add(ctx.func_application());
        addUnary(BehaviorAction.CALL, object(TargetType.Procedure, ctx.func_application().func_name()));
        return null;
    }

    @Override
    public Void visitDiscardstmt(DiscardstmtContext ctx) {
        if (ctx.TEMP() != null || ctx.TEMPORARY() != null) {
            addUnary(BehaviorAction.DROP, sessionTemporaryTableScope(ctx));
        }
        return null;
    }

    @Override
    public Void visitPreparestmt(PreparestmtContext ctx) {
        deferredBodies.add(ctx);
        addUnary(BehaviorAction.UNSAFE, object(TargetType.PrepareStatement, ctx.name()));
        return null;
    }

    @Override
    public Void visitExecutestmt(ExecutestmtContext ctx) {
        deferredBodies.add(ctx);
        if (ctx.create_as_target() != null) {
            addUnary(BehaviorAction.CREATE, object(TargetType.Table, ctx.create_as_target().qualified_name()));
        }
        addUnary(BehaviorAction.UNSAFE, object(TargetType.PrepareStatement, ctx.name()));
        return null;
    }

    @Override
    public Void visitDeallocatestmt(DeallocatestmtContext ctx) {
        deferredBodies.add(ctx);
        if (ctx.name() != null) {
            addUnary(BehaviorAction.UNSAFE, object(TargetType.PrepareStatement, ctx.name()));
        } else {
            addUnary(BehaviorAction.UNSAFE, objects.instanceObject(TargetType.PrepareStatement, ctx));
        }
        return null;
    }

    @Override
    public Void visitMergestmt(MergestmtContext ctx) {
        if (!mutations.add(ctx))
            return null;
        addRelation(BehaviorAction.MERGE, object(TargetType.Table, ctx.relation_expr_opt_alias().relation_expr().qualified_name()), tableReferences(ctx));
        return null;
    }

    @Override
    public Void visitFunc_application(Func_applicationContext ctx) {
        functions.add(ctx);
        addFunction(ctx);
        return visitChildren(ctx);
    }

    @Override
    public Void visitVariableshowstmt(VariableshowstmtContext ctx) {
        ParserRuleContext name = first(ctx, Var_nameContext.class);
        if (name != null) {
            addUnary(BehaviorAction.READ, namedObject(TargetType.ConfigKey, name, text(name)));
        } else {
            addUnary(BehaviorAction.READ, objects.instanceObject(TargetType.ConfigKey, ctx));
        }
        return null;
    }

    @Override
    public Void visitVariablesetstmt(VariablesetstmtContext ctx) {
        ParserRuleContext name = first(ctx, Var_nameContext.class);
        if (name != null) {
            addUnary(BehaviorAction.CONFIGURE, namedObject(TargetType.ConfigKey, name, text(name)));
        } else {
            Set_rest_moreContext more = ctx.set_rest().set_rest_more();
            if (more != null && (more.ROLE() != null || more.AUTHORIZATION() != null || more.SCHEMA() != null || more.CATALOG() != null)) {
                TargetType type = TargetType.Role;
                if (more.AUTHORIZATION() != null)
                    type = TargetType.User;
                if (more.SCHEMA() != null)
                    type = TargetType.Schema;
                if (more.CATALOG() != null)
                    type = TargetType.Catalog;
                ParserRuleContext value = more.nonreservedword_or_sconst();
                if (value == null)
                    value = more.sconst();
                if (value == null)
                    addUnary(BehaviorAction.SWITCH, objects.instanceObject(type, more));
                else
                    addUnary(BehaviorAction.SWITCH, namedObject(type, value, stringValue(value)));
            } else if (more != null) {
                addUnary(BehaviorAction.CONFIGURE, objects.instanceObject(TargetType.ConfigKey, more));
            }
        }
        return null;
    }

    @Override
    public Void visitVariableresetstmt(VariableresetstmtContext ctx) {
        Reset_restContext reset = ctx.reset_rest();
        Generic_resetContext genericReset = reset.generic_reset();
        if (genericReset != null && genericReset.var_name() != null && "role".equalsIgnoreCase(genericReset.var_name().getText())) {
            addUnary(BehaviorAction.SWITCH, objects.instanceObject(TargetType.Role, ctx));
            return null;
        }
        if (reset.SESSION() != null && reset.AUTHORIZATION() != null) {
            addUnary(BehaviorAction.SWITCH, objects.instanceObject(TargetType.User, ctx));
            return null;
        }
        ParserRuleContext name = first(ctx, Var_nameContext.class);
        if (name != null) {
            addUnary(BehaviorAction.RESET, namedObject(TargetType.ConfigKey, name, text(name)));
        } else {
            addUnary(BehaviorAction.RESET, objects.instanceObject(TargetType.ConfigKey, ctx));
        }
        return null;
    }

    @Override
    public Void visitAltersystemstmt(AltersystemstmtContext ctx) {
        ParserRuleContext name = first(ctx, Var_nameContext.class);
        BehaviorAction action = BehaviorAction.CONFIGURE;
        if (ctx.RESET() != null)
            action = BehaviorAction.RESET;
        BehaviorObject config = object(TargetType.ConfigKey, name);
        if (config == null)
            config = objects.instanceObject(TargetType.ConfigKey, ctx);
        addUnary(action, config);
        return null;
    }

    @Override
    public Void visitCreateuserstmt(CreateuserstmtContext ctx) {
        addUnary(BehaviorAction.CREATE, object(TargetType.User, ctx.roleid()));
        return null;
    }

    @Override
    public Void visitCreaterolestmt(CreaterolestmtContext ctx) {
        addUnary(BehaviorAction.CREATE, object(TargetType.Role, ctx.roleid()));
        return null;
    }

    @Override
    public Void visitDropuserstmt(DropuserstmtContext ctx) {
        for (RolespecContext name : ctx.role_list().rolespec()) {
            addUnary(BehaviorAction.DROP, object(TargetType.User, name));
        }
        return null;
    }

    @Override
    public Void visitDroprolestmt(DroprolestmtContext ctx) {
        for (RolespecContext name : ctx.role_list().rolespec()) {
            addUnary(BehaviorAction.DROP, object(TargetType.Role, name));
        }
        return null;
    }

    @Override
    public Void visitLoadstmt(LoadstmtContext ctx) {
        SconstContext name = first(ctx, SconstContext.class);
        addUnary(BehaviorAction.UNSAFE, namedObject(TargetType.Library, name, stringValue(name)));
        return null;
    }

    private boolean isCte(Relation_exprContext table) {
        List<String> names = new ArrayList<>();
        collectNames(table.qualified_name(), names);
        if (names.size() != 1) {
            return false;
        }
        String reference = names.get(0);
        Set<ParseTree> ancestors = Collections.newSetFromMap(new IdentityHashMap<>());
        for (ParseTree parent = table.getParent(); parent != null; parent = parent.getParent())
            ancestors.add(parent);
        for (ParseTree parent = table.getParent(); parent != null; parent = parent.getParent()) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                ParseTree child = parent.getChild(i);
                if (child instanceof With_clause_Context wrapper)
                    child = wrapper.with_clause();
                if (child instanceof With_clauseContext with) {
                    List<Common_table_exprContext> ctes = with.cte_list().common_table_expr();
                    int visibleCount = ctes.size();
                    if (with.RECURSIVE() == null) {
                        for (int index = 0; index < ctes.size(); index++) {
                            if (ancestors.contains(ctes.get(index))) {
                                visibleCount = index;
                                break;
                            }
                        }
                    }
                    for (int index = 0; index < visibleCount; index++) {
                        String name = normalizeIdentifier(text(ctes.get(index).name()));
                        if (name.equals(reference))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private String stringValue(ParserRuleContext context) {
        SconstContext literal = first(context, SconstContext.class);
        if (literal == null) {
            return normalizeIdentifier(text(context));
        }
        AnysconstContext initial = literal.anysconst();
        StringBuilder value = new StringBuilder();
        if (initial.BeginDollarStringConstant() != null) {
            for (var part : initial.DollarText()) {
                value.append(part.getText());
            }
        } else {
            String token = text(initial);
            value.append(token.substring(token.indexOf('\'') + 1, token.length() - 1).replace("''", "'"));
        }
        for (var continuation : literal.StringConstant()) {
            String token = continuation.getText();
            value.append(token.substring(1, token.length() - 1).replace("''", "'"));
        }
        return value.toString();
    }

    @Override
    public Void visitDostmt(DostmtContext ctx) {
        deferredBodies.add(ctx);
        addUnary(BehaviorAction.UNSAFE, objects.instanceObject(TargetType.ProgramObject, ctx));
        return null;
    }

    @Override
    public Void visitCopystmt(CopystmtContext ctx) {
        boolean importing = ctx.copy_from() != null && ctx.copy_from().FROM() != null;
        BehaviorObject endpoint = null;
        if (ctx.copy_file_name().sconst() != null) {
            SconstContext literal = ctx.copy_file_name().sconst();
            String name = stringValue(literal);
            if (ctx.program_() != null) {
                endpoint = objects.instanceObject(TargetType.ProgramObject, literal, name);
            } else {
                name = name.replaceFirst("^/+", "");
                endpoint = namedObject(TargetType.File, literal, name);
            }
        }
        List<BehaviorObject> tables = tableReferences(ctx);
        if (ctx.qualified_name() != null) {
            tables.add(0, object(TargetType.Table, ctx.qualified_name()));
        }
        if (ctx.preparablestmt() != null) {
            InsertstmtContext insert = first(ctx, InsertstmtContext.class);
            UpdatestmtContext update = first(ctx, UpdatestmtContext.class);
            DeletestmtContext delete = first(ctx, DeletestmtContext.class);
            MergestmtContext merge = first(ctx, MergestmtContext.class);
            if (insert != null)
                addObject(tables, object(TargetType.Table, insert.insert_target().qualified_name()));
            if (update != null)
                addObject(tables, object(TargetType.Table, update.relation_expr_opt_alias().relation_expr().qualified_name()));
            if (delete != null)
                addObject(tables, object(TargetType.Table, delete.relation_expr_opt_alias().relation_expr().qualified_name()));
            if (merge != null)
                addObject(tables, object(TargetType.Table, merge.relation_expr_opt_alias().relation_expr().qualified_name()));
        }
        if (importing) {
            BehaviorObject destination = tables.remove(0);
            List<BehaviorObject> sources = objects(endpoint);
            sources.addAll(tables);
            addRelation(BehaviorAction.IMPORT, destination, sources);
        } else if (endpoint != null) {
            addRelation(BehaviorAction.EXPORT, endpoint, tables);
        } else {
            for (BehaviorObject table : tables) {
                addUnary(BehaviorAction.EXPORT, table);
            }
        }
        if (ctx.program_() != null) {
            addUnary(BehaviorAction.UNSAFE, endpoint);
        }
        if (ctx.preparablestmt() != null && first(ctx.preparablestmt(), SelectstmtContext.class) == null) {
            visit(ctx.preparablestmt());
        }
        return null;
    }

    @Override
    public Void visitCreatetablespacestmt(CreatetablespacestmtContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.opttablespaceowner() != null) {
            addObject(targets, principal(ctx.opttablespaceowner().rolespec()));
        }
        addObject(targets, namedObject(TargetType.File, ctx.sconst(), stringValue(ctx.sconst()).replaceFirst("^/+", "")));
        addRelation(BehaviorAction.CREATE, object(TargetType.Tablespace, ctx.name()), targets);
        return null;
    }

    @Override
    public Void visitDroptablespacestmt(DroptablespacestmtContext ctx) {
        addUnary(BehaviorAction.DROP, object(TargetType.Tablespace, ctx.name()));
        return null;
    }

    @Override
    public Void visitComment_table_stmt(Comment_table_stmtContext ctx) {
        addUnary(BehaviorAction.ALTER, object(TargetType.Table, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitComment_column_stmt(Comment_column_stmtContext ctx) {
        addUnary(BehaviorAction.ALTER, columnTable(ctx.any_name()));
        return null;
    }

    private BehaviorObject columnTable(Any_nameContext ctx) {
        List<String> names = new ArrayList<>();
        collectNames(ctx, names);
        names.remove(names.size() - 1);
        Token stop = ctx.colid().getStop();
        if (ctx.attrs() != null && ctx.attrs().attr_name().size() > 1) {
            stop = ctx.attrs().attr_name(ctx.attrs().attr_name().size() - 2).getStop();
        }
        return objects.object(TargetType.Table, ctx.getStart(), stop, names);
    }

    @Override
    public Void visitRename_database_stmt(Rename_database_stmtContext ctx) {
        rename(TargetType.Catalog, ctx.name(0), ctx.name(1));
        return null;
    }

    @Override
    public Void visitRename_schema_stmt(Rename_schema_stmtContext ctx) {
        rename(TargetType.Schema, ctx.qualified_name(), ctx.name());
        return null;
    }

    @Override
    public Void visitRename_column_stmt(Rename_column_stmtContext ctx) {
        addUnary(BehaviorAction.ALTER, object(TargetType.Table, ctx.relation_expr().qualified_name()));
        return null;
    }

    private void rename(TargetType type, ParserRuleContext oldName, ParserRuleContext newName) {
        BehaviorObject source = object(type, oldName);
        BehaviorObject target = object(type, newName);
        moveToSameContainer(source, target);
        addRelation(BehaviorAction.RENAME, source, objects(target));
    }

    private void alterTable(AltertablestmtContext ctx) {
        TargetType type = TargetType.Table;
        if (ctx.INDEX() != null) {
            type = TargetType.Index;
        } else if (ctx.MATERIALIZED() != null) {
            type = TargetType.Materialized;
        } else if (ctx.VIEW() != null) {
            type = TargetType.View;
        } else if (ctx.SEQUENCE() != null) {
            type = TargetType.Sequence;
        }
        ParserRuleContext name = ctx.qualified_name();
        if (ctx.relation_expr() != null) {
            name = ctx.relation_expr().qualified_name();
        }
        BehaviorObject table = object(type, name);
        addUnary(BehaviorAction.ALTER, table);
        for (Alter_table_cmdContext command : descendants(ctx, Alter_table_cmdContext.class)) {
            NameContext constraint = null;
            BehaviorAction action = BehaviorAction.ALTER;
            if (command instanceof AddConstraintContext add) {
                constraint = add.tableconstraint().name();
                action = BehaviorAction.CREATE;
            } else if (command instanceof DropConstraintContext drop) {
                constraint = drop.name();
                action = BehaviorAction.DROP;
            } else if (command instanceof ValidateConstraintContext validate) {
                constraint = validate.name();
                action = BehaviorAction.VALIDATE;
            } else if (command instanceof AlterConstaintContext alter) {
                constraint = alter.name();
            }
            if (constraint != null) {
                BehaviorObject subject = tableMember(TargetType.Constraint, constraint, table);
                addRelation(action, subject, objects(table));
            }
        }
        if (ctx.partition_cmd() != null) {
            addRelation(BehaviorAction.ALTER, object(TargetType.Table, ctx.partition_cmd().qualified_name()), objects(table));
        }
    }

    @Override
    public Void visitGrantstmt(GrantstmtContext ctx) {
        grant(ctx.privilege_target(), ctx.grantee_list(), BehaviorAction.GRANT);
        return null;
    }

    @Override
    public Void visitRevokestmt(RevokestmtContext ctx) {
        grant(ctx.privilege_target(), ctx.grantee_list(), BehaviorAction.REVOKE);
        return null;
    }

    private void grant(Privilege_targetContext ctx, Grantee_listContext grantees, BehaviorAction action) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (GranteeContext grantee : grantees.grantee()) {
            addObject(targets, principal(grantee.rolespec()));
        }
        TargetType type = TargetType.Table;
        if (ctx.SEQUENCE() != null)
            type = TargetType.Sequence;
        else if (ctx.DATABASE() != null)
            type = TargetType.Catalog;
        else if (ctx.SCHEMA() != null)
            type = TargetType.Schema;
        else if (ctx.TABLESPACE() != null)
            type = TargetType.Tablespace;
        else if (ctx.TYPE_P() != null || ctx.DOMAIN_P() != null)
            type = TargetType.Type;
        else if (ctx.FUNCTION() != null)
            type = TargetType.Function;
        else if (ctx.PROCEDURE() != null)
            type = TargetType.Procedure;
        else if (ctx.PARAMETER() != null)
            type = TargetType.ConfigKey;
        List<? extends ParserRuleContext> names = descendants(ctx, Qualified_nameContext.class);
        if (names.isEmpty())
            names = descendants(ctx, Any_nameContext.class);
        if (names.isEmpty())
            names = descendants(ctx, Func_nameContext.class);
        if (names.isEmpty())
            names = descendants(ctx, NameContext.class);
        for (ParserRuleContext name : names) {
            addRelation(action, object(type, name), targets);
        }
    }

    @Override
    public Void visitGrantrolestmt(GrantrolestmtContext ctx) {
        grantRoles(ctx.privilege_list(), ctx.role_list(), BehaviorAction.GRANT);
        return null;
    }

    @Override
    public Void visitRevokerolestmt(RevokerolestmtContext ctx) {
        grantRoles(ctx.privilege_list(), ctx.role_list(), BehaviorAction.REVOKE);
        return null;
    }

    private void grantRoles(Privilege_listContext roles, Role_listContext grantees, BehaviorAction action) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (RolespecContext role : grantees.rolespec()) {
            addObject(targets, principal(role));
        }
        for (PrivilegeContext role : roles.privilege()) {
            addRelation(action, object(TargetType.Role, role.colid()), targets);
        }
    }

    @Override
    public Void visitAlterrolestmt(AlterrolestmtContext ctx) {
        TargetType type = TargetType.Role;
        if (ctx.USER() != null)
            type = TargetType.User;
        addUnary(BehaviorAction.ALTER, object(type, ctx.rolespec()));
        return null;
    }

    @Override
    public Void visitAlterrolesetstmt(AlterrolesetstmtContext ctx) {
        TargetType type = TargetType.Role;
        if (ctx.USER() != null)
            type = TargetType.User;
        addRelation(BehaviorAction.CONFIGURE, object(type, ctx.rolespec()), objects(TargetType.ConfigKey, first(ctx, Var_nameContext.class)));
        return null;
    }

    @Override
    public Void visitAlterdatabasestmt(AlterdatabasestmtContext ctx) {
        List<NameContext> names = descendants(ctx, NameContext.class);
        List<BehaviorObject> targets = new ArrayList<>();
        if (names.size() > 1)
            addObject(targets, object(TargetType.Tablespace, names.get(1)));
        addRelation(BehaviorAction.ALTER, object(TargetType.Catalog, names.get(0)), targets);
        return null;
    }

    @Override
    public Void visitAlterdatabasesetstmt(AlterdatabasesetstmtContext ctx) {
        addRelation(BehaviorAction.CONFIGURE, object(TargetType.Catalog, ctx.name()), objects(TargetType.ConfigKey, first(ctx, Var_nameContext.class)));
        return null;
    }

    @Override
    public Void visitRefreshmatviewstmt(RefreshmatviewstmtContext ctx) {
        addUnary(BehaviorAction.REFRESH, object(TargetType.Materialized, ctx.qualified_name()));
        return null;
    }

    @Override
    public Void visitAnalyzestmt(AnalyzestmtContext ctx) {
        maintenance(ctx, BehaviorAction.ANALYZE);
        return null;
    }

    @Override
    public Void visitVacuumstmt(VacuumstmtContext ctx) {
        maintenance(ctx, BehaviorAction.OPTIMIZE);
        return null;
    }

    private void maintenance(ParserRuleContext ctx, BehaviorAction action) {
        List<Vacuum_relationContext> tables = descendants(ctx, Vacuum_relationContext.class);
        if (tables.isEmpty()) {
            addUnary(action, objects.unnamedObject(TargetType.Table, ctx, UmiTypes.Catalog));
        }
        for (Vacuum_relationContext table : tables) {
            addUnary(action, object(TargetType.Table, table.qualified_name()));
        }
    }

    @Override
    public Void visitReindexstmt(ReindexstmtContext ctx) {
        TargetType type = TargetType.Index;
        if (ctx.SCHEMA() != null)
            type = TargetType.Schema;
        else if (ctx.reindex_target_relation() != null && ctx.reindex_target_relation().TABLE() != null)
            type = TargetType.Table;
        else if (ctx.reindex_target_all() != null)
            type = TargetType.Catalog;
        ParserRuleContext name = ctx.qualified_name();
        if (name == null)
            name = ctx.name();
        if (name == null)
            name = ctx.single_name_();
        if (name == null)
            addUnary(BehaviorAction.OPTIMIZE, objects.unnamedObject(type, ctx, UmiTypes.Catalog));
        else
            addUnary(BehaviorAction.OPTIMIZE, object(type, name));
        return null;
    }

    @Override
    public Void visitLockstmt(LockstmtContext ctx) {
        for (Qualified_nameContext name : descendants(ctx, Qualified_nameContext.class)) {
            addUnary(BehaviorAction.LOCK, object(TargetType.Table, name));
        }
        return null;
    }

    @Override
    public Void visitCheckpointstmt(CheckpointstmtContext ctx) {
        addUnary(BehaviorAction.CHECKPOINT, objects.unnamedObject(TargetType.Catalog, ctx, UmiTypes.Catalog));
        return null;
    }

    @Override
    public Void visitCommentstmt(CommentstmtContext ctx) {
        if (ctx.comment_table_stmt() != null || ctx.comment_column_stmt() != null)
            return visitChildren(ctx);
        TargetType type = declaredType(ctx);
        ParserRuleContext name = declaredName(ctx);
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.TRANSFORM() != null)
            addObject(targets, catalogObject(TargetType.Object, ctx.name()));
        if (ctx.object_type_name_on_any_name() != null || ctx.CONSTRAINT() != null) {
            name = ctx.name();
            addObject(targets, object(TargetType.Table, ctx.any_name()));
        }
        BehaviorObject subject = object(type, name);
        if (type == TargetType.Constraint && !targets.isEmpty()) {
            subject = tableMember(type, name, targets.get(0));
        }
        if (type == TargetType.Object && (ctx.object_type_name() != null || ctx.TRANSFORM() != null || ctx.LARGE_P() != null)) {
            subject = catalogObject(type, name);
        }
        addRelation(BehaviorAction.ALTER, subject, targets);
        return null;
    }

    @Override
    public Void visitSeclabelstmt(SeclabelstmtContext ctx) {
        BehaviorObject subject;
        if (ctx.COLUMN() != null)
            subject = columnTable(ctx.any_name());
        else
            subject = object(declaredType(ctx), declaredName(ctx));
        addUnary(BehaviorAction.ALTER, subject);
        return null;
    }

    private ParserRuleContext declaredName(ParserRuleContext ctx) {
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof Any_nameContext || child instanceof Qualified_nameContext || child instanceof NameContext || child instanceof RoleidContext
                || child instanceof TypenameContext || child instanceof NumericonlyContext) {
                return (ParserRuleContext) child;
            }
            if (child instanceof Function_with_argtypesContext || child instanceof Aggregate_with_argtypesContext) {
                return first(child, Func_nameContext.class);
            }
            if (child instanceof Relation_exprContext relation)
                return relation.qualified_name();
        }
        return null;
    }

    private TargetType declaredType(ParserRuleContext ctx) {
        ParserRuleContext name = declaredName(ctx);
        int end = ctx.getStop().getTokenIndex();
        if (name != null)
            end = name.getStart().getTokenIndex() - 1;
        Set<Integer> keywords = new HashSet<>();
        for (int i = ctx.getStart().getTokenIndex(); i <= end; i++) {
            keywords.add(parser.getTokenStream().get(i).getType());
        }
        if (keywords.contains(TRANSFORM) || keywords.contains(CAST) || keywords.contains(LANGUAGE) || keywords.contains(LARGE_P) || keywords.contains(STATISTICS)
            || keywords.contains(ACCESS))
            return TargetType.Object;
        if (keywords.contains(MATERIALIZED))
            return TargetType.Materialized;
        if (keywords.contains(TABLE))
            return TargetType.Table;
        if (keywords.contains(VIEW))
            return TargetType.View;
        if (keywords.contains(DATABASE))
            return TargetType.Catalog;
        if (keywords.contains(SCHEMA))
            return TargetType.Schema;
        if (keywords.contains(TABLESPACE))
            return TargetType.Tablespace;
        if (keywords.contains(DOMAIN_P) || keywords.contains(TYPE_P))
            return TargetType.Type;
        if (keywords.contains(INDEX))
            return TargetType.Index;
        if (keywords.contains(SEQUENCE))
            return TargetType.Sequence;
        if (keywords.contains(PROCEDURE))
            return TargetType.Procedure;
        if (keywords.contains(FUNCTION) || keywords.contains(AGGREGATE))
            return TargetType.Function;
        if (keywords.contains(ROUTINE))
            return TargetType.ProgramObject;
        if (keywords.contains(TRIGGER))
            return TargetType.Trigger;
        if (keywords.contains(CONSTRAINT))
            return TargetType.Constraint;
        if (keywords.contains(POLICY))
            return TargetType.RowAccessPolicy;
        if (keywords.contains(RULE) || keywords.contains(SEARCH))
            return TargetType.Policy;
        if (keywords.contains(ROLE))
            return TargetType.Role;
        if (keywords.contains(USER))
            return TargetType.User;
        if (keywords.contains(OPERATOR))
            return TargetType.Operator;
        if (keywords.contains(PUBLICATION))
            return TargetType.Publication;
        if (keywords.contains(SUBSCRIPTION))
            return TargetType.Subscription;
        if (keywords.contains(EXTENSION))
            return TargetType.Library;
        if (keywords.contains(SERVER))
            return TargetType.Link;
        return TargetType.Object;
    }

    @Override
    public Void visitRenamestmt(RenamestmtContext ctx) {
        if (ctx.rename_table_stmt() != null || ctx.rename_column_stmt() != null || ctx.rename_database_stmt() != null || ctx.rename_schema_stmt() != null) {
            return visitChildren(ctx);
        }
        ParserRuleContext name = declaredName(ctx);
        TargetType type = declaredType(ctx);
        if (resolvedType == SplitQueryType.RENAME_COLUMN) {
            addUnary(BehaviorAction.ALTER, object(type, name));
        } else if (ctx.roleid().size() == 2) {
            type = TargetType.Role;
            if (ctx.USER() != null)
                type = TargetType.User;
            rename(type, ctx.roleid(0), ctx.roleid(1));
        } else if (ctx.CONSTRAINT() != null && ctx.name().size() == 2) {
            BehaviorObject table = object(TargetType.Table, declaredName(ctx));
            BehaviorObject source = tableMember(TargetType.Constraint, ctx.name(0), table);
            BehaviorObject target = tableMember(TargetType.Constraint, ctx.name(1), table);
            addRelation(BehaviorAction.RENAME, source, objects(target, table));
        } else if (!ctx.name().isEmpty()) {
            rename(type, name, ctx.name(ctx.name().size() - 1));
        }
        return null;
    }

    @Override
    public Void visitAlterobjectdependsstmt(AlterobjectdependsstmtContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.TRIGGER() != null)
            addObject(targets, object(TargetType.Table, ctx.qualified_name()));
        addObject(targets, object(TargetType.Library, ctx.name(ctx.name().size() - 1)));
        addRelation(BehaviorAction.ALTER, object(declaredType(ctx), declaredName(ctx)), targets);
        return null;
    }

    @Override
    public Void visitCreateextensionstmt(CreateextensionstmtContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (Create_extension_opt_itemContext option : descendants(ctx, Create_extension_opt_itemContext.class)) {
            if (option.SCHEMA() != null)
                addObject(targets, object(TargetType.Schema, option.name()));
        }
        addRelation(BehaviorAction.CREATE, object(TargetType.Library, ctx.name()), targets);
        return null;
    }

    @Override
    public Void visitAlterextensionstmt(AlterextensionstmtContext ctx) {
        addUnary(BehaviorAction.ALTER, object(TargetType.Library, ctx.name()));
        return null;
    }

    @Override
    public Void visitAlterextensioncontentsstmt(AlterextensioncontentsstmtContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        if (ctx.object_type_name() != null && ctx.object_type_name().DATABASE() != null && ctx.name().size() > 1) {
            addObject(targets, object(TargetType.Catalog, ctx.name(1)));
        }
        addRelation(BehaviorAction.ALTER, object(TargetType.Library, ctx.name(0)), targets);
        return null;
    }

    @Override
    public Void visitCreatepublicationstmt(CreatepublicationstmtContext ctx) {
        addRelation(BehaviorAction.CREATE, object(TargetType.Publication, ctx.name()), publicationTargets(ctx));
        return null;
    }

    @Override
    public Void visitAlterpublicationstmt(AlterpublicationstmtContext ctx) {
        addRelation(BehaviorAction.ALTER, object(TargetType.Publication, ctx.name()), publicationTargets(ctx));
        return null;
    }

    private List<BehaviorObject> publicationTargets(ParserRuleContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (Relation_exprContext table : descendants(ctx, Relation_exprContext.class))
            addObject(targets, object(TargetType.Table, table.qualified_name()));
        for (Publication_schema_nameContext schema : descendants(ctx, Publication_schema_nameContext.class))
            addObject(targets, object(TargetType.Schema, schema));
        return targets;
    }

    @Override
    public Void visitCreatesubscriptionstmt(CreatesubscriptionstmtContext ctx) {
        // Remote publications are named on the publisher, whose datasource identity is unavailable here.
        addUnary(BehaviorAction.CREATE, object(TargetType.Subscription, ctx.name(0)));
        return null;
    }

    @Override
    public Void visitAltersubscriptionstmt(AltersubscriptionstmtContext ctx) {
        addUnary(BehaviorAction.ALTER, object(TargetType.Subscription, ctx.name(0)));
        return null;
    }

    @Override
    public Void visitDropsubscriptionstmt(DropsubscriptionstmtContext ctx) {
        addUnary(BehaviorAction.DROP, object(TargetType.Subscription, ctx.name()));
        return null;
    }

    @Override
    public Void visitCreatestatsstmt(CreatestatsstmtContext ctx) {
        BehaviorObject statistics = object(TargetType.Object, ctx.any_name());
        if (statistics == null)
            statistics = objects.unnamedObject(TargetType.Object, ctx, UmiTypes.Schema);
        addRelation(BehaviorAction.CREATE, statistics, tableReferences(ctx));
        return null;
    }

    @Override
    public Void visitAlterstatsstmt(AlterstatsstmtContext ctx) {
        addUnary(BehaviorAction.ALTER, object(TargetType.Object, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitCreatedomainstmt(CreatedomainstmtContext ctx) {
        addUnary(BehaviorAction.CREATE, object(TargetType.Type, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitAlterdomainstmt(AlterdomainstmtContext ctx) {
        addUnary(BehaviorAction.ALTER, object(TargetType.Type, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitAlterenumstmt(AlterenumstmtContext ctx) {
        addUnary(BehaviorAction.ALTER, object(TargetType.Type, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitAltertypestmt(AltertypestmtContext ctx) {
        addUnary(BehaviorAction.ALTER, object(TargetType.Type, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitDefinestmt(DefinestmtContext ctx) {
        TargetType targetType = declaredType(ctx);
        if (ctx.func_name() != null) {
            addUnary(BehaviorAction.CREATE, object(targetType, ctx.func_name()));
        } else if (ctx.any_operator() != null) {
            addUnary(BehaviorAction.CREATE, operatorObject(ctx.any_operator()));
        } else {
            addUnary(BehaviorAction.CREATE, object(targetType, ctx.any_name(0)));
        }
        return null;
    }

    @Override
    public Void visitCreatefdwstmt(CreatefdwstmtContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (Handler_nameContext name : descendants(ctx, Handler_nameContext.class))
            addObject(targets, object(TargetType.Function, name));
        addRelation(BehaviorAction.CREATE, catalogObject(TargetType.Object, ctx.name()), targets);
        addUnary(BehaviorAction.UNSAFE, catalogObject(TargetType.Object, ctx.name()));
        return null;
    }

    @Override
    public Void visitAlterfdwstmt(AlterfdwstmtContext ctx) {
        addUnary(BehaviorAction.ALTER, catalogObject(TargetType.Object, ctx.name()));
        return null;
    }

    @Override
    public Void visitCreateforeignserverstmt(CreateforeignserverstmtContext ctx) {
        addRelation(BehaviorAction.CREATE, object(TargetType.Link, ctx.name(0)), objects(catalogObject(TargetType.Object, ctx.name(1))));
        return null;
    }

    @Override
    public Void visitAlterforeignserverstmt(AlterforeignserverstmtContext ctx) {
        addUnary(BehaviorAction.ALTER, object(TargetType.Link, ctx.name()));
        return null;
    }

    @Override
    public Void visitCreateforeigntablestmt(CreateforeigntablestmtContext ctx) {
        addRelation(BehaviorAction.CREATE, object(TargetType.Table, ctx.qualified_name(0)), objects(TargetType.Link, ctx.name()));
        return null;
    }

    @Override
    public Void visitImportforeignschemastmt(ImportforeignschemastmtContext ctx) {
        addRelation(BehaviorAction.IMPORT, object(TargetType.Schema, ctx.name(2)), objects(TargetType.Link, ctx.name(1)));
        return null;
    }

    @Override
    public Void visitCreateusermappingstmt(CreateusermappingstmtContext ctx) {
        addRelation(BehaviorAction.GRANT, object(TargetType.Link, ctx.name()), objects(principal(ctx.auth_ident().rolespec())));
        return null;
    }

    @Override
    public Void visitAlterusermappingstmt(AlterusermappingstmtContext ctx) {
        addRelation(BehaviorAction.ALTER, object(TargetType.Link, ctx.name()), objects(principal(ctx.auth_ident().rolespec())));
        return null;
    }

    @Override
    public Void visitDropusermappingstmt(DropusermappingstmtContext ctx) {
        addRelation(BehaviorAction.REVOKE, object(TargetType.Link, ctx.name()), objects(principal(ctx.auth_ident().rolespec())));
        return null;
    }

    @Override
    public Void visitRulestmt(RulestmtContext ctx) {
        deferredBodies.add(ctx);
        addRelation(BehaviorAction.CREATE, object(TargetType.Policy, ctx.name()), objects(TargetType.Table, ctx.qualified_name()));
        return null;
    }

    @Override
    public Void visitCreatetransformstmt(CreatetransformstmtContext ctx) {
        List<BehaviorObject> targets = objects(catalogObject(TargetType.Object, ctx.name()));
        for (Function_with_argtypesContext function : descendants(ctx, Function_with_argtypesContext.class))
            addObject(targets, object(TargetType.Function, function.func_name()));
        addRelation(BehaviorAction.CREATE, catalogObject(TargetType.Object, ctx.typename()), targets);
        return null;
    }

    @Override
    public Void visitDroptransformstmt(DroptransformstmtContext ctx) {
        addRelation(BehaviorAction.DROP, catalogObject(TargetType.Object, ctx.typename()), objects(catalogObject(TargetType.Object, ctx.name())));
        return null;
    }

    @Override
    public Void visitCreateplangstmt(CreateplangstmtContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (Handler_nameContext handler : descendants(ctx, Handler_nameContext.class))
            addObject(targets, object(TargetType.Function, handler));
        addRelation(BehaviorAction.CREATE, catalogObject(TargetType.Object, ctx.name()), targets);
        return null;
    }

    @Override
    public Void visitCreateamstmt(CreateamstmtContext ctx) {
        addRelation(BehaviorAction.CREATE, catalogObject(TargetType.Object, ctx.name()), objects(TargetType.Function, ctx.handler_name()));
        addUnary(BehaviorAction.UNSAFE, catalogObject(TargetType.Object, ctx.name()));
        return null;
    }

    @Override
    public Void visitCreateconversionstmt(CreateconversionstmtContext ctx) {
        addRelation(BehaviorAction.CREATE, object(TargetType.Object, ctx.any_name(0)), objects(TargetType.Function, ctx.any_name(1)));
        return null;
    }

    @Override
    public Void visitCreatecaststmt(CreatecaststmtContext ctx) {
        List<BehaviorObject> targets = new ArrayList<>();
        for (TypenameContext type : ctx.typename())
            addObject(targets, object(TargetType.Type, type));
        addObject(targets, object(TargetType.Function, first(ctx, Func_nameContext.class)));
        addRelation(BehaviorAction.CREATE, objects.unnamedObject(TargetType.Object, ctx, UmiTypes.Catalog), targets);
        return null;
    }

    @Override
    public Void visitDropcaststmt(DropcaststmtContext ctx) {
        addUnary(BehaviorAction.DROP, objects.unnamedObject(TargetType.Object, ctx, UmiTypes.Catalog));
        return null;
    }

    @Override
    public Void visitAlterfunctionstmt(AlterfunctionstmtContext ctx) {
        addUnary(BehaviorAction.ALTER, object(declaredType(ctx), first(ctx, Func_nameContext.class)));
        return null;
    }

    @Override
    public Void visitCreateeventtrigstmt(CreateeventtrigstmtContext ctx) {
        BehaviorObject trigger = object(TargetType.Trigger, ctx.name());
        trigger.setObjectPath(objects.unnamedObject(TargetType.Trigger, ctx.name(), UmiTypes.Catalog).getObjectPath() + normalizeIdentifier(text(ctx.name())) + "/");
        addRelation(BehaviorAction.CREATE, trigger, objects(TargetType.Function, ctx.func_name()));
        addUnary(BehaviorAction.UNSAFE, trigger);
        return null;
    }

    @Override
    public Void visitRemovefuncstmt(RemovefuncstmtContext ctx) {
        for (Function_with_argtypesContext function : descendants(ctx, Function_with_argtypesContext.class)) {
            TargetType type = TargetType.Function;
            if (ctx.PROCEDURE() != null)
                type = TargetType.Procedure;
            addUnary(BehaviorAction.DROP, object(type, function.func_name()));
        }
        return null;
    }

    @Override
    public Void visitAltertsdictionarystmt(AltertsdictionarystmtContext ctx) {
        addUnary(BehaviorAction.ALTER, object(TargetType.Policy, ctx.any_name()));
        return null;
    }

    @Override
    public Void visitAltertsconfigurationstmt(AltertsconfigurationstmtContext ctx) {
        addUnary(BehaviorAction.ALTER, object(TargetType.Policy, ctx.any_name(0)));
        return null;
    }

    @Override
    public Void visitAltertblspcstmt(AltertblspcstmtContext ctx) {
        addUnary(BehaviorAction.CONFIGURE, object(TargetType.Tablespace, ctx.name()));
        return null;
    }

    private BehaviorObject tableMember(TargetType type, ParserRuleContext name, BehaviorObject table) {
        BehaviorObject object = object(type, name);
        String member = normalizeIdentifier(text(name));
        object.setObjectPath(table.getObjectPath() + member + "/");
        ObjectName parentName = table.getObjectName();
        object.setObjectName(new ObjectName(parentName.getCatalog(), parentName.getSchema(), member));
        return object;
    }

    private BehaviorObject catalogObject(TargetType type, ParserRuleContext context) {
        if (context == null)
            return null;
        String name = normalizeIdentifier(text(context));
        BehaviorObject object = objects.unnamedObject(type, context, UmiTypes.Catalog);
        object.setObjectPath(object.getObjectPath() + name + "/");
        String catalog = levels == null ? null : StringUtils.toString(levels.get(UmiTypes.Catalog));
        object.setObjectName(new ObjectName(catalog, null, name));
        return object;
    }

    private BehaviorObject object(TargetType type, ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        collectNames(context, names);
        if (type == TargetType.Publication || type == TargetType.Subscription || type == TargetType.PublicationSubscription || type == TargetType.Library
            || type == TargetType.Link) {
            BehaviorObject result = objects.unnamedObject(type, context, UmiTypes.Catalog);
            String name = names.get(names.size() - 1);
            result.setObjectPath(result.getObjectPath() + name + "/");
            String catalog = levels == null ? null : StringUtils.toString(levels.get(UmiTypes.Catalog));
            result.setObjectName(new ObjectName(catalog, null, name));
            return result;
        }
        return scopedObject(type, context, names);
    }

    private BehaviorObject scopedObject(TargetType type, ParserRuleContext context, List<String> names) {
        if (names.isEmpty()) {
            names.add(normalizeIdentifier(text(context)));
        }
        String name = names.get(names.size() - 1);
        if (type == TargetType.User || type == TargetType.Role || type == TargetType.UserOrRole || type == TargetType.ConfigKey || type == TargetType.PrepareStatement
            || type == TargetType.Tablespace || type == TargetType.Library || type == TargetType.File) {
            BehaviorObject result = objects.instanceObject(type, context, name);
            result.setObjectName(new ObjectName(null, null, name));
            return result;
        }
        BehaviorObject result = objects.object(type, context, names);
        String catalog = levels == null ? null : StringUtils.toString(levels.get(UmiTypes.Catalog));
        String schema = levels == null ? null : StringUtils.toString(levels.get(UmiTypes.Schema));
        if (type == TargetType.Catalog) {
            result.setObjectName(new ObjectName(name, null, null));
        } else if (type == TargetType.Schema) {
            result.setObjectName(new ObjectName(catalog, name, null));
        } else {
            if (names.size() >= 2) {
                schema = names.get(names.size() - 2);
            }
            if (names.size() >= 3) {
                catalog = names.get(names.size() - 3);
            }
            result.setObjectName(new ObjectName(catalog, schema, name));
        }
        return result;
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

    private void addUnary(BehaviorAction action, BehaviorObject subject) {
        if (subject == null) {
            return;
        }
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(subject);
        relation.setAction(action);
        behavior.getRelations().add(relation);
    }

    private BehaviorRelation addRelation(BehaviorAction action, BehaviorObject subject, List<BehaviorObject> targets) {
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
        return relation;
    }

    private List<BehaviorObject> tableReferences(ParseTree tree) {
        List<BehaviorObject> result = new ArrayList<>();
        for (Relation_exprContext table : descendants(tree, Relation_exprContext.class)) {
            if (isQueryTable(table) && !isCte(table)) {
                tableReads.add(table);
                addObject(result, object(TargetType.Table, table.qualified_name()));
            }
        }
        return result;
    }

    private boolean isQueryTable(Relation_exprContext table) {
        return table.getParent() instanceof Table_refContext || table.getParent() instanceof Simple_select_pramaryContext;
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
        addRelation(BehaviorAction.TRANSFER, subject, objects(principal(newOwner)));
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
        return namedObject(type, context, normalizeIdentifier(text(context)));
    }

    private BehaviorObject namedObject(TargetType type, ParserRuleContext context, String name) {
        if (context == null || StringUtils.isBlank(name)) {
            return null;
        }
        return scopedObject(type, context, List.of(name));
    }

    private BehaviorObject operatorObject(Any_operatorContext context) {
        if (context == null) {
            return null;
        }
        List<String> names = new ArrayList<>();
        Any_operatorContext current = context;
        while (current.colid() != null) {
            names.add(normalizeIdentifier(text(current.colid())));
            current = current.any_operator();
        }
        names.add(normalizeIdentifier(text(current)));
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
        if (source.getObjectType() == TargetType.Catalog || source.getObjectType() == TargetType.Schema) {
            return;
        }
        target.setObjectName(new ObjectName(sourceName.getCatalog(), sourceName.getSchema(), targetName.getObjectName()));
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

    private String normalizeIdentifier(String value) {
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        // PostgreSQL preserves non-ASCII characters when folding UTF-8 identifiers.
        char[] normalized = value.toCharArray();
        for (int i = 0; i < normalized.length; i++) {
            if (normalized[i] >= 'A' && normalized[i] <= 'Z') {
                normalized[i] += 'a' - 'A';
            }
        }
        return new String(normalized);
    }
}
