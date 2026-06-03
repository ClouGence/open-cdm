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
package com.clougence.clouddm.ds.oceanbase.analysis.ob4ora;

import static com.clougence.clouddm.ds.oceanbase.parser.ob4ora.antlr.ObForOracleParser.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.ds.oceanbase.parser.ob4ora.antlr.ObForOracleParserBaseVisitor;
import com.clougence.clouddm.ds.oracle.analysis.builder.OraBuilderFactory;
import com.clougence.clouddm.ds.oracle.analysis.builder.enums.OraAttribute;
import com.clougence.clouddm.ds.oracle.analysis.secrules.OraColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ConstraintTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.StringDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbResourceDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public abstract class AbstractObForOraSQLParserVisitor extends ObForOracleParserBaseVisitor<Void> {

    private final OraBuilderFactory builder;
    private final Parser            parser;

    public AbstractObForOraSQLParserVisitor(OraBuilderFactory builder, Parser parser){
        this.builder = builder;
        this.parser = parser;
    }

    private String getText(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    @Override
    public Void visitSelect_only_statement(Select_only_statementContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();

        return null;
    }

    @Override
    public Void visitSubquery(SubqueryContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitCreate_index(Create_indexContext ctx) {
        builder.enterCreateIndex();
        dmVisitChildren(ctx);
        builder.exitCreateIndex();
        return null;
    }

    @Override
    public Void visitIndex_name(Index_nameContext ctx) {
        builder.enterObjName(NameType.INDEX);
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitTable_index_columns(Table_index_columnsContext ctx) {
        builder.enterColumnList();
        dmVisitChildren(ctx);
        builder.exitColumnList();
        return null;
    }

    @Override
    public Void visitCreate_view(Create_viewContext ctx) {
        builder.enterCreateView();
        for (ParseTree child : ctx.children) {
            if (child instanceof Tableview_nameContext) {
                child.accept(this);
                break;
            }
        }
        builder.exitCreateView();
        return null;
    }

    @Override
    public Void visitCreate_trigger(Create_triggerContext ctx) {
        builder.enterCreateTrigger();
        for (ParseTree child : ctx.children) {
            if (child instanceof Trigger_nameContext) {
                child.accept(this);
                break;
            }
        }
        builder.exitCreateTrigger();
        return null;
    }

    @Override
    public Void visitDrop_sequence(Drop_sequenceContext ctx) {
        builder.enterDropSequence();
        ctx.sequence_name().accept(this);
        builder.exitDropSequence();
        return null;
    }

    @Override
    public Void visitAlter_trigger(Alter_triggerContext ctx) {
        builder.enterAlterTrigger();
        ctx.alter_trigger_name.accept(this);
        builder.exitAlterTrigger();
        return null;
    }

    @Override
    public Void visitDrop_trigger(Drop_triggerContext ctx) {
        builder.enterDropTrigger();
        ctx.trigger_name().accept(this);
        builder.exitDropTrigger();
        return null;
    }

    @Override
    public Void visitCreate_materialized_view(Create_materialized_viewContext ctx) {
        builder.enterCreateView();
        builder.addAttr(CommonAttribute.MATERIALIZED, true);
        for (ParseTree child : ctx.children) {
            if (child instanceof Tableview_nameContext) {
                child.accept(this);
                break;
            }
        }
        builder.exitCreateView();
        return null;
    }

    @Override
    public Void visitRename_object(Rename_objectContext ctx) {
        builder.enterRename(TargetType.Table);
        dmVisitChildren(ctx);
        builder.exitRename();
        return null;
    }

    @Override
    public Void visitRename_column_clause(Rename_column_clauseContext ctx) {
        OraColumnDomain oraColumnDomain = new OraColumnDomain();
        oraColumnDomain.setColumn(getName(ctx.old_column_name().getText()));
        oraColumnDomain.setNewName(getName(ctx.new_column_name().getText()));
        oraColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME_COLUMN);
        oraColumnDomain.setAuditKind(SecQueryKind.ALTER);
        builder.handleDomain(oraColumnDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitSimple_case_expression(Simple_case_expressionContext ctx) {
        builder.handleOtherDomains(() -> {
            ctx.expression().accept(this);
        });
        for (Case_when_part_expressionContext expressionContext : ctx.case_when_part_expression()) {
            builder.handleOtherDomains(() -> {
                expressionContext.expression(0).accept(this);
            });
            expressionContext.expression(1).accept(this);
        }
        if (ctx.expression() != null) {
            ctx.case_else_part_expression().accept(this);
        }

        return null;
    }

    @Override
    public Void visitFlashback_query_clause(Flashback_query_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitModel_clause(Model_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitSubquery_factoring_clause(Subquery_factoring_clauseContext ctx) {
        builder.enterWithSelect();
        dmVisitChildren(ctx);
        builder.exitWithSelect();
        return null;
    }

    @Override
    public Void visitDrop_function(Drop_functionContext ctx) {
        builder.enterDropFunction();
        dmVisitChildren(ctx);
        builder.exitDropFunction();
        return null;
    }

    @Override
    public Void visitInsert_statement(Insert_statementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitMulti_table_element(Multi_table_elementContext ctx) {
        builder.handleInsert(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitMulti_table_insert(Multi_table_insertContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConditional_insert_clause(Conditional_insert_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConditional_insert_else_part(Conditional_insert_else_partContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConditional_insert_when_part(Conditional_insert_when_partContext ctx) {
        for (Multi_table_elementContext multiTableElementContext : ctx.multi_table_element()) {
            multiTableElementContext.accept(this);
        }
        return null;
    }

    @Override
    public Void visitTable_partitioning_clauses(Table_partitioning_clausesContext ctx) {
        return null;
    }

    @Override
    public Void visitIndex_properties(Index_propertiesContext ctx) {
        return null;
    }

    @Override
    public Void visitSample_clause(Sample_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitInsert_column_list(Insert_column_listContext ctx) {
        builder.enterInsertColumnBuilder();
        dmVisitChildren(ctx);
        builder.exitInsertColumnBuilder();
        return null;
    }

    @Override
    public Void visitQuery_block(Query_blockContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();

        return null;
    }

    @Override
    public Void visitTable_alias(Table_aliasContext ctx) {
        builder.addAttr(CommonAttribute.ALIAS, ctx.getText());
        return null;
    }

    @Override
    public Void visitConstant(ConstantContext ctx) {
        builder.enterConstant();
        String text = this.getText(ctx);
        if (text.startsWith("'")) {
            text = text.substring(1, text.length() - 1);
        }
        builder.addAttr(CommonAttribute.VALUE, text);
        builder.exitConstant();
        return null;
    }

    @Override
    public Void visitCol_default(Col_defaultContext ctx) {
        String text = this.getText((ParserRuleContext) ctx.getChild(1));
        if (text.startsWith("'")) {
            text = text.substring(1, text.length() - 1);
        }
        builder.handleDomain(new RdbConstantDomain(text), DomainSource.COLUMN_DEFAULT_VALUE);
        return null;
    }

    @Override
    public Void visitIdentity_clause(Identity_clauseContext ctx) {
        builder.addAttr(OraAttribute.AUTO, true);
        return null;
    }

    @Override
    public Void visitValues_clause(Values_clauseContext ctx) {
        builder.enterValuesBuilder();
        dmVisitChildren(ctx);
        builder.exitValuesBuilder();
        return null;
    }

    @Override
    public Void visitUnion_context(Union_contextContext ctx) {
        builder.addAttr(CommonAttribute.UNION, true);
        return null;
    }

    @Override
    public Void visitDelete_statement(Delete_statementContext ctx) {
        builder.enterDelete();
        dmVisitChildren(ctx);
        builder.exitDelete();
        return null;
    }

    @Override
    public Void visitSelected_list(Selected_listContext ctx) {
        if (ctx.getChild(0) instanceof TerminalNode) {
            if (((TerminalNode) ctx.getChild(0)).getSymbol().getType() == ASTERISK) {
                builder.enterBuildSelectItem();
                RdbColumnDomain rdbColumnDomain = new RdbColumnDomain();
                rdbColumnDomain.setColumn("*");
                builder.handleDomain(rdbColumnDomain, DomainSource.COLUMN);
                builder.exitBuildSelectItem();
            }
        }
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSelect_list_elements(Select_list_elementsContext ctx) {
        builder.enterBuildSelectItem();
        if (ctx.getChild(0) instanceof Tableview_nameContext) {
            RdbColumnDomain rdbColumnDomain = new RdbColumnDomain();
            rdbColumnDomain.setColumn("*");
            rdbColumnDomain.setTable(this.getText((ParserRuleContext) ctx.getChild(0)));
            builder.handleDomain(rdbColumnDomain, DomainSource.COLUMN);
        } else {
            builder.addAttr(CommonAttribute.VALUE, this.getText((ParserRuleContext) ctx.getChild(0)));
            dmVisitChildren(ctx);
        }
        builder.exitBuildSelectItem();

        return null;
    }

    @Override
    public Void visitColumnref(ColumnrefContext ctx) {
        builder.enterSelectColumn();
        dmVisitChildren(ctx);
        builder.exitSelectColumn();
        return null;
    }

    @Override
    public Void visitFetch_clause(Fetch_clauseContext ctx) {
        builder.addAttr(CommonAttribute.LIMIT, true);
        return null;
    }

    @Override
    public Void visitInterval_type(Interval_typeContext ctx) {
        return super.visitInterval_type(ctx);
    }

    @Override
    public Void visitTrigger_name(Trigger_nameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitArgument(ArgumentContext ctx) {
        String text = this.getText(ctx);
        if (text.startsWith("'")) {
            text = text.substring(1, text.length() - 1);
        }
        builder.addAttr(CommonAttribute.VALUE, text);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRegular_id(Regular_idContext ctx) {
        builder.addAttr(CommonAttribute.VALUE, ctx.getText());
        return null;
    }

    @Override
    public Void visitId_expression(Id_expressionContext ctx) {
        builder.addAttr(CommonAttribute.VALUE, ctx.getText());
        return null;
    }

    @Override
    public Void visitKeyword_function(Keyword_functionContext ctx) {
        builder.addAttr(CommonAttribute.VALUE, ctx.getText());
        return null;
    }

    @Override
    public Void visitCreate_procedure_body(Create_procedure_bodyContext ctx) {
        builder.enterCreateProcedure();
        for (ParseTree child : ctx.children) {
            if (child instanceof Procedure_nameContext) {
                child.accept(this);
                break;
            }
        }
        builder.enterExitProcedure();
        return null;
    }

    @Override
    public Void visitProcedure_name(Procedure_nameContext ctx) {
        builder.enterObjName(NameType.PROCEDURE);
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitFunction_name(Function_nameContext ctx) {
        builder.enterObjName(NameType.FUNCTION);
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitDrop_procedure(Drop_procedureContext ctx) {
        builder.enterDropProcedure();
        dmVisitChildren(ctx);
        builder.exitDropProcedure();
        return null;
    }

    @Override
    public Void visitCreate_function_body(Create_function_bodyContext ctx) {
        builder.enterCreateFunction();
        for (ParseTree child : ctx.children) {
            if (child instanceof Function_nameContext) {
                child.accept(this);
                break;
            }
        }
        builder.exitCreateFunction();
        return null;
    }

    @Override
    public Void visitFrom_clause(From_clauseContext ctx) {
        builder.enterSelectFromBuilder();
        dmVisitChildren(ctx);
        builder.exitSelectFromBuilder();
        return null;
    }

    @Override
    public Void visitJoin_on_part(Join_on_partContext ctx) {
        return null;
    }

    @Override
    public Void visitTable_ref_aux(Table_ref_auxContext ctx) {
        builder.enterSelectTableBuilder();
        dmVisitChildren(ctx);
        builder.exitSelectTableBuilder();
        return null;
    }

    @Override
    public Void visitJoin_type(Join_typeContext ctx) {
        builder.enterJoin(ctx.getText());
        dmVisitChildren(ctx);
        builder.exitJoin();
        return null;
    }

    @Override
    public Void visitTableview_name(Tableview_nameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitTemporary_(Temporary_Context ctx) {
        builder.addAttr(OraAttribute.TEMPORARY, true);
        return null;
    }

    @Override
    public Void visitFunction_call(Function_callContext ctx) {
        builder.enterCall();
        dmVisitChildren(ctx);
        builder.exitCall();
        return null;
    }

    /**
     * system func  ,sum、case .....
     *
     * @param ctx
     * @return
     */
    @Override
    public Void visitStandard_function(Standard_functionContext ctx) {
        builder.enterCall();
        dmVisitChildren(ctx);
        builder.exitCall();
        return null;
    }

    @Override
    public Void visitString_function(String_functionContext ctx) {
        builder.enterObjName();
        builder.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());
        builder.exitObjName();

        builder.enterFunctionArgs();
        dmVisitChildren(ctx);
        builder.exitFunctionArgs();
        return null;
    }

    @Override
    public Void visitCreate_user(Create_userContext ctx) {
        builder.enterCreateUser();
        dmVisitChildren(ctx);
        builder.exitCreateUser();
        return null;
    }

    @Override
    public Void visitUser_object_name(User_object_nameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitRevoke_statement(Revoke_statementContext ctx) {
        builder.enterRevoke();
        dmVisitChildren(ctx);
        builder.exitRevoke();
        return null;
    }

    @Override
    public Void visitRevokee_clause(Revokee_clauseContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitGrantee_name(Grantee_nameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitDrop_user(Drop_userContext ctx) {
        builder.enterDropUser();
        dmVisitChildren(ctx);
        builder.exitDropUser();
        return null;
    }

    @Override
    public Void visitGrant_statement(Grant_statementContext ctx) {
        builder.enterGrant();
        dmVisitChildren(ctx);
        builder.exitGrant();
        return null;
    }

    @Override
    public Void visitCreate_role(Create_roleContext ctx) {
        builder.enterCreateRole();
        dmVisitChildren(ctx);
        builder.exitCreateRole();
        return null;
    }

    @Override
    public Void visitRole_name(Role_nameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitIdentified_by(Identified_byContext ctx) {
        for (ParseTree child : ctx.children) {
            if (child instanceof Id_expressionContext || child instanceof NumericContext) {
                String text = this.getText((ParserRuleContext) child);
                if (text.startsWith("\"")) {
                    builder.addAttr(CommonAttribute.PASSWORD, text.substring(1, text.length() - 1));
                } else {
                    builder.addAttr(CommonAttribute.PASSWORD, text);
                }

                break;
            }
        }
        return null;
    }

    @Override
    public Void visitAnonymous_block(Anonymous_blockContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SQL_BLOCK);
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitNumeric_function(Numeric_functionContext ctx) {
        builder.enterObjName();
        builder.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());
        builder.exitObjName();

        builder.enterFunctionArgs();
        dmVisitChildren(ctx);
        builder.exitFunctionArgs();
        return null;
    }

    @Override
    public Void visitNumeric(NumericContext ctx) {
        builder.handleDomain(new RdbConstantDomain(ctx.getText()), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitJson_function(Json_functionContext ctx) {
        builder.enterObjName();
        builder.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());
        builder.exitObjName();

        builder.enterFunctionArgs();
        dmVisitChildren(ctx);
        builder.exitFunctionArgs();
        return null;
    }

    @Override
    public Void visitOther_function(Other_functionContext ctx) {
        builder.enterObjName();
        builder.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());
        builder.exitObjName();
        boolean hasArgContext = false;
        for (ParseTree child : ctx.children) {
            if (child instanceof Function_argumentContext) {
                hasArgContext = true;
                break;
            }
        }
        if (!hasArgContext) {
            builder.enterFunctionArgs();
        }

        dmVisitChildren(ctx);
        if (!hasArgContext) {
            builder.exitFunctionArgs();
        }

        return null;
    }

    @Override
    public Void visitFunction_argument(Function_argumentContext ctx) {
        builder.enterFunctionArgs();
        dmVisitChildren(ctx);
        builder.exitFunctionArgs();
        return null;
    }

    @Override
    public Void visitWhere_clause(Where_clauseContext ctx) {
        builder.enterWhere();
        dmVisitChildren(ctx);
        builder.exitWhere();
        return null;
    }

    @Override
    public Void visitRelational_expression(Relational_expressionContext ctx) {
        if (ctx.children.size() == 3) {
            String left = this.getText((ParserRuleContext) ctx.getChild(0));
            String right = this.getText((ParserRuleContext) ctx.getChild(2));;
            if (left.startsWith("(")) {
                left = left.substring(1, left.length() - 1);
            }
            if (right.startsWith("(")) {
                right = right.substring(1, right.length() - 1);
            }
            if (!right.equals(left)) {
                builder.addAttr(CommonAttribute.VALID_WHERE, true);
            } else {
                return null;
            }
        } else {
            builder.addAttr(CommonAttribute.VALID_WHERE, true);
        }
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumn_alias(Column_aliasContext ctx) {
        builder.addAttr(CommonAttribute.ALIAS, getName(ctx.identifier().getText()));
        return null;
    }

    @Override
    public Void visitSequence_name(Sequence_nameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    private String getName(String name) {
        if (name.startsWith("\"")) {
            return name.substring(1, name.length() - 1);
        }
        return name;
    }

    @Override
    public Void visitCall_statement(Call_statementContext ctx) {
        builder.enterCall();
        dmVisitChildren(ctx);
        builder.exitCall();
        return null;
    }

    @Override
    public Void visitAlter_table(Alter_tableContext ctx) {
        builder.enterAlterTable();
        dmVisitChildren(ctx);
        builder.exitAlterTable();
        return null;
    }

    @Override
    public Void visitDrop_column_clause(Drop_column_clauseContext ctx) {
        builder.enterAlterTableItem(AlterTableType.DROP_COLUMN);
        builder.enterColumnDef();
        dmVisitChildren(ctx);
        builder.exitColumnDef();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitModify_col_properties(Modify_col_propertiesContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ALTER_COLUMN);
        builder.enterColumnDef();
        dmVisitChildren(ctx);
        builder.exitColumnDef();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitAdd_column_clause(Add_column_clauseContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ADD_COLUMN);
        dmVisitChildren(ctx);
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitColumn_definition(Column_definitionContext ctx) {
        builder.enterColumnDef();
        dmVisitChildren(ctx);
        builder.exitColumnDef();
        return null;
    }

    @Override
    public Void visitVirtual_column_definition(Virtual_column_definitionContext ctx) {
        builder.handleColumnDef(() -> {
            ctx.column_name().accept(this);
            if (ctx.datatype() != null) {
                ctx.datatype().accept(this);
            }
        });

        return null;
    }

    @Override
    public Void visitDatatype(DatatypeContext ctx) {
        builder.enterColumnType(this.getText(ctx));
        dmVisitChildren(ctx);
        builder.exitColumnType();
        return null;
    }

    @Override
    public Void visitCustom_type(Custom_typeContext ctx) {
        builder.enterColumnType(ctx.getText());
        builder.enterObjName(NameType.COLUMN_TYPE);
        dmVisitChildren(ctx);
        builder.exitObjName();
        builder.exitColumnType();
        return null;
    }

    @Override
    public Void visitNative_datatype_element(Native_datatype_elementContext ctx) {
        //        builder.addAttr(CommonAttribute.VALUE, this.getText(ctx));
        builder.handleDomain(new ObjNameDomain(Collections.singletonList(this.getText(ctx)), NameType.COLUMN_TYPE), DomainSource.OBJ_NAME);
        return null;
    }

    //    @Override
    //    public Void visitInline_constraint(Inline_constraintContext ctx) {
    //        builder.enterConstraint();
    //        visitOraChildren(ctx);
    //        builder.exitConstraint();
    //        return null;
    //    }

    @Override
    public Void visitNot_null_(Not_null_Context ctx) {
        builder.addAttr(CommonAttribute.NOT_NULL, true);
        return null;
    }

    @Override
    public Void visitPrimary_key_(Primary_key_Context ctx) {
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);
        return null;
    }

    @Override
    public Void visitUnique_(Unique_Context ctx) {
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Unique), DomainSource.CONSTRAINT_TYPE);
        return null;
    }

    @Override
    public Void visitReferences_clause(References_clauseContext ctx) {
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.ForeignKey), DomainSource.CONSTRAINT_TYPE);
        return null;
    }

    @Override
    public Void visitComment_on_column(Comment_on_columnContext ctx) {
        builder.enterComment(TargetType.Column);
        dmVisitChildren(ctx);
        builder.exitComment();
        return null;
    }

    @Override
    public Void visitColumn_name(Column_nameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitQuoted_string(Quoted_stringContext ctx) {
        builder.handleDomain(new RdbConstantDomain(ctx.getText()), DomainSource.CONSTANT);
        //        builder.buildName(this.getText(ctx).substring(1, this.getText(ctx).length() - 1));
        return null;
    }

    @Override
    public Void visitComment_context(Comment_contextContext ctx) {
        builder.handleDomain(new StringDomain(ctx.getText()), DomainSource.COMMENT);
        return null;
    }

    @Override
    public Void visitAdd_table_constarint(Add_table_constarintContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ADD_CONSTRAINT);
        builder.enterConstraint();
        dmVisitChildren(ctx);
        builder.exitConstraint();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitDrop_table_constarint(Drop_table_constarintContext ctx) {
        builder.enterAlterTableItem(AlterTableType.DROP_CONSTRAINT);
        builder.enterConstraint();
        dmVisitChildren(ctx);
        builder.exitConstraint();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitComment_on_table(Comment_on_tableContext ctx) {
        builder.enterComment(TargetType.Table);
        dmVisitChildren(ctx);
        builder.exitComment();
        return null;
    }

    @Override
    public Void visitDrop_table(Drop_tableContext ctx) {
        builder.enterDropTable();
        dmVisitChildren(ctx);
        builder.exitDropTable();
        return null;
    }

    //    @Override
    //    public Void visitConstraint_name(Constraint_nameContext ctx) {
    //        builder.enterConstraint();
    //        visitOraChildren(ctx);
    //        builder.exitConstraint();
    //        return null;
    //
    //    }

    @Override
    public Void visitOut_of_line_constraint(Out_of_line_constraintContext ctx) {
        ParseTree child = ctx.getChild(0);
        TerminalNodeImpl terminalNode = (TerminalNodeImpl) child;
        TerminalNodeImpl type = terminalNode;
        if (terminalNode.getSymbol().getType() == CONSTRAINT || terminalNode.getSymbol().getType() == CONSTRAINTS) {
            type = (TerminalNodeImpl) ctx.getChild(2);
        }
        switch (type.getSymbol().getType()) {
            case UNIQUE: {
                builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Unique), DomainSource.CONSTRAINT_TYPE);
                break;
            }
            case PRIMARY: {
                builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);
                break;
            }
            case FOREIGN: {
                builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.ForeignKey), DomainSource.CONSTRAINT_TYPE);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unsupported type: " + type.getSymbol().getText());
            }
        }

        dmVisitChildren(ctx);

        return null;
    }

    @Override
    public Void visitUpdate_statement(Update_statementContext ctx) {
        builder.enterUpdate();
        dmVisitChildren(ctx);
        builder.exitUpdate();
        return null;
    }

    @Override
    public Void visitUpdate_set_clause(Update_set_clauseContext ctx) {
        builder.enterSetColumnValue();
        dmVisitChildren(ctx);
        builder.exitSetColumnValue();
        return null;
    }

    @Override
    public Void visitRelational_property(Relational_propertyContext ctx) {
        if (ctx.getChild(0) instanceof Out_of_line_constraintContext) {
            builder.enterConstraint();
            dmVisitChildren(ctx);
            builder.exitConstraint();

        } else {
            dmVisitChildren(ctx);
        }
        return null;
    }

    @Override
    public Void visitConstraint_column_list(Constraint_column_listContext ctx) {
        builder.enterColumnList();
        dmVisitChildren(ctx);
        builder.exitColumnList();
        return null;
    }

    @Override
    public Void visitDrop_constraint_clause(Drop_constraint_clauseContext ctx) {
        TerminalNodeImpl node = (TerminalNodeImpl) ctx.getChild(1);
        if (node.getSymbol().getType() == CONSTRAINT) {
            dmVisitChildren(ctx);
        } else if (node.getSymbol().getType() == PRIMARY) {
            builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);
        } else if (node.getSymbol().getType() == UNIQUE) {
            builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Unique), DomainSource.CONSTRAINT_TYPE);
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + node.getSymbol().getType());
        }

        return null;
    }

    @Override
    public Void visitCreate_table(Create_tableContext ctx) {
        builder.enterCreateTable();
        dmVisitChildren(ctx);
        builder.exitCreateTable();
        return null;
    }

    @Override
    public Void visitTable_name(Table_nameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    // ignore

    @Override
    public Void visitHierarchical_query_clause(Hierarchical_query_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitJoin_using_part(Join_using_partContext ctx) {
        return null;
    }

    @Override
    public Void visitQuery_partition_clause(Query_partition_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitOver_clause(Over_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitOrder_by_clause(Order_by_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitGroup_by_clause(Group_by_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitGrant_object_name(Grant_object_nameContext ctx) {
        return null;
    }

    @Override
    public Void visitOn_object_clause(On_object_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitWithin_or_over_part(Within_or_over_partContext ctx) {
        return null;
    }

    @Override
    public Void visitTruncate_table(Truncate_tableContext ctx) {
        builder.enterTruncateTable();
        dmVisitChildren(ctx);
        builder.exitTruncateTable();
        return null;
    }

    @Override
    public Void visitGeneral_element_part(General_element_partContext ctx) {
        builder.enterCall();
        dmVisitChildren(ctx);
        builder.exitCall();
        return null;
    }

    @Override
    public Void visitUnit_statement(Unit_statementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreate_sequence(Create_sequenceContext ctx) {
        builder.enterCreateSequence();
        ctx.sequence_name().accept(this);
        builder.exitCreateSequence();
        return null;
    }

    @Override
    public Void visitIndex_expr(Index_exprContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUser_tablespace_clause(User_tablespace_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitProfile_clause(Profile_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUser_lock_clause(User_lock_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitObject_name(Object_nameContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRevoke_object_privileges(Revoke_object_privilegesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitInline_constraint(Inline_constraintContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNull_(Null_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRelational_table(Relational_tableContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitImmutable_table_clauses(Immutable_table_clausesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTable_properties(Table_propertiesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPhysical_properties(Physical_propertiesContext ctx) {
        //        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumn_clauses(Column_clausesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAdd_modify_drop_column_clauses(Add_modify_drop_column_clausesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitModify_column_clauses(Modify_column_clausesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitStatement(StatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitData_manipulation_language_statements(Data_manipulation_language_statementsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSelect_statement(Select_statementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWith_clause(With_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWith_factoring_clause(With_factoring_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSubquery_basic_elements(Subquery_basic_elementsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSubquery_operation_part(Subquery_operation_partContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTable_ref_list(Table_ref_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTable_ref(Table_refContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTable_ref_aux_internal_one(Table_ref_aux_internal_oneContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTable_ref_aux_internal_two(Table_ref_aux_internal_twoContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitJoin_clause(Join_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitOuter_join_type(Outer_join_typeContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitOffset_clause(Offset_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumn_based_update_set_clause(Column_based_update_set_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSingle_table_insert(Single_table_insertContext ctx) {
        builder.handleInsert(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitInsert_into_clause(Insert_into_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitGeneral_table_ref(General_table_refContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDml_table_expression_clause(Dml_table_expression_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCondition(ConditionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpressions_(Expressions_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpression(ExpressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitLogical_expression(Logical_expressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUnary_logical_expression(Unary_logical_expressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUnary_logical_operation(Unary_logical_operationContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitLogical_operation(Logical_operationContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitMultiset_expression(Multiset_expressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCompound_expression(Compound_expressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRelational_operator(Relational_operatorContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIn_elements(In_elementsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBetween_elements(Between_elementsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConcatenation(ConcatenationContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitModel_expression(Model_expressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUnary_expression(Unary_expressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCase_expression(Case_expressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSearched_case_expression(Searched_case_expressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCase_when_part_expression(Case_when_part_expressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCase_else_part_expression(Case_else_part_expressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAtom(AtomContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQuantified_expression(Quantified_expressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNumeric_function_wrapper(Numeric_function_wrapperContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitOver_clause_keyword(Over_clause_keywordContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWithin_or_over_clause_keyword(Within_or_over_clause_keywordContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRoutine_name(Routine_nameContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQuery_name(Query_nameContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConstraint_name(Constraint_nameContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitType_name(Type_nameContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFunction_argument_analytic(Function_argument_analyticContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRespect_or_ignore_nulls(Respect_or_ignore_nullsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitType_spec(Type_specContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPrecision_part(Precision_partContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTable_element(Table_elementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitObject_privilege(Object_privilegeContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSystem_privilege(System_privilegeContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIdentifier(IdentifierContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitOuter_join_sign(Outer_join_signContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreate_synonym(Create_synonymContext ctx) {
        builder.enterCreateSynonym();
        List<String> list = new ArrayList<>();
        if (ctx.synSchema != null) {
            list.add(getName(getText(ctx.synSchema)));
        }
        list.add(getName(getText(ctx.synonym_name())));
        builder.handleDomain(new ObjNameDomain(list, null), DomainSource.OBJ_NAME);

        builder.exitCreateSynonym();
        return null;
    }

    @Override
    public Void visitConstraint_state(Constraint_stateContext ctx) {
        return null;
    }

    @Override
    public Void visitCreate_materialized_view_log(Create_materialized_view_logContext ctx) {
        builder.handleCreateObject(() -> {
            ctx.tableview_name().accept(this);
        });
        return null;
    }

    @Override
    public Void visitChildren(RuleNode node) {
        if (node instanceof ParserRuleContext) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText((ParserRuleContext) node));
        }
        throw new UnsupportedOperationException("unsupported SQL: " + node.getText());
    }

    public void dmVisitChildren(RuleNode node) {
        int n = node.getChildCount();

        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            c.accept(this);
        }
    }
}
