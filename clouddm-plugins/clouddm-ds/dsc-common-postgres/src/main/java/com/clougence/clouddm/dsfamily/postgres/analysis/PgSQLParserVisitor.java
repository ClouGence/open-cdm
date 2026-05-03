package com.clougence.clouddm.dsfamily.postgres.analysis;

import static com.clougence.clouddm.dsfamily.postgres.parser.antlr.PgSqlParser.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ConstraintTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.StringDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.PgBuilderFactory;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.enums.PgAttribute;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgTableDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.dsfamily.postgres.parser.antlr.PgSqlParserBaseVisitor;
import com.clougence.schema.umi.struts.UmiTypes;

public class PgSQLParserVisitor extends PgSqlParserBaseVisitor<Void> {

    private final PgBuilderFactory builder;
    private final Parser           parser;

    public PgSQLParserVisitor(PgBuilderFactory builder, Parser parser){
        this.builder = builder;
        this.parser = parser;
    }

    private String getText(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
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

    @Override
    public Void visitVariableshowstmt(VariableshowstmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setTarget(TargetType.Environment);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitDostmt(DostmtContext ctx) {
        RdbResourceDomain domain = new RdbResourceDomain();
        domain.setSqlType(SecQueryType.SQL_BLOCK);
        domain.setAuditKind(SecQueryKind.OTHER);
        domain.setTarget(TargetType.Unknown);
        this.builder.addDomain(domain);
        return null;
    }

    @Override
    public Void visitAnalyzestmt(AnalyzestmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setSqlType(SecQueryType.ANALYZE);
        if (ctx.schema != null) {
            rdbResourceDomain.setSchema(getName(getText(ctx.schema)));
        }
        rdbResourceDomain.setName(getName(getText(ctx.table)));
        rdbResourceDomain.setTarget(TargetType.Table);
        rdbResourceDomain.setNeedSupply(true);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitRefreshmatviewstmt(RefreshmatviewstmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setSqlType(SecQueryType.REFRESH_VIEW);
        rdbResourceDomain.setTarget(TargetType.Materialized);
        rdbResourceDomain.setNeedSupply(true);

        List<String> strings = handleQualifiedName(ctx.qualified_name());
        Map<UmiTypes, String> map = BuilderUtil.parseViewName(strings);
        rdbResourceDomain.setCatalog(map.get(UmiTypes.Catalog));
        rdbResourceDomain.setSchema(map.get(UmiTypes.Schema));
        rdbResourceDomain.setName(map.get(UmiTypes.View));

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitDropstmt(DropstmtContext ctx) {
        return super.visitDropstmt(ctx);
    }

    private List<String> handleQualifiedName(Qualified_nameContext ctx) {
        List<String> names = new ArrayList<>();
        parseName(names, ctx);
        return names;
    }

    private void parseName(List<String> nameList, ParseTree ctx) {
        if (ctx instanceof IdentifierContext) {
            nameList.add(getName(getText((ParserRuleContext) ctx)));
        } else {
            for (int i = 0; i < ctx.getChildCount(); i++) {
                parseName(nameList, ctx.getChild(i));
            }
        }
    }

    @Override
    public Void visitSelect_clause(Select_clauseContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitUnion_context(Union_contextContext ctx) {
        builder.addAttr(CommonAttribute.UNION, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSimple_select_pramary(Simple_select_pramaryContext ctx) {
        if (ctx.TABLE() != null) {
            throw new UnsupportedOperationException("Unsupported sql:" + getText(ctx));
        }
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitFor_locking_clause(For_locking_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitSelect_no_parens(Select_no_parensContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitTable_ref(Table_refContext ctx) {
        builder.handleSelectTable(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    private String getName(String name) {
        if (name.startsWith("\"")) {
            return name.substring(1, name.length() - 1);
        }
        return name;
    }

    @Override
    public Void visitAlias_clause(Alias_clauseContext ctx) {
        if (ctx.name_list() != null) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }
        builder.addAttr(CommonAttribute.ALIAS, ctx.getText());
        return null;
    }

    @Override
    public Void visitTarget_alias(Target_aliasContext ctx) {
        if (ctx.getChild(0) instanceof TerminalNodeImpl) {
            builder.addAttr(CommonAttribute.ALIAS, ctx.getChild(1).getText());
        } else {
            builder.addAttr(CommonAttribute.ALIAS, ctx.getText());
        }

        return null;
    }

    @Override
    public Void visitJoin_qual(Join_qualContext ctx) {
        return null;
    }

    @Override
    public Void visitColumnref(ColumnrefContext ctx) {
        builder.handleSelectColumn(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitTarget_el(Target_elContext ctx) {
        builder.handleBuildSelectItem(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitFrom_clause(From_clauseContext ctx) {
        builder.handleSelectFrom(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitCross_join(Cross_joinContext ctx) {
        builder.enterJoin(ctx.getText());
        dmVisitChildren(ctx);
        builder.exitJoin();
        return null;
    }

    @Override
    public Void visitNormal_join(Normal_joinContext ctx) {

        builder.enterJoin(ctx.getText());
        dmVisitChildren(ctx);
        builder.exitJoin();
        return null;
    }

    @Override
    public Void visitNatural_join(Natural_joinContext ctx) {
        String text = ctx.getText();

        builder.enterJoin(text);
        dmVisitChildren(ctx);
        builder.exitJoin();
        return null;
    }

    @Override
    public Void visitGroup_clause(Group_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitHaving_clause(Having_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitOver_clause(Over_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitSort_clause_(Sort_clause_Context ctx) {
        return null;
    }

    // parse name
    @Override
    public Void visitIdentifier(IdentifierContext ctx) {
        builder.addAttr(CommonAttribute.VALUE, ctx.getText());
        return null;
    }

    @Override
    public Void visitType_func_name_keyword(Type_func_name_keywordContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, ctx.getText());
        return null;
    }

    @Override
    public Void visitUnreserved_keyword(Unreserved_keywordContext ctx) {
        builder.addAttr(CommonAttribute.VALUE, ctx.getText());
        return null;
    }

    @Override
    public Void visitCol_name_keyword(Col_name_keywordContext ctx) {
        builder.addAttr(CommonAttribute.VALUE, ctx.getText());
        return null;
    }

    // function
    @Override
    public Void visitFunc_application(Func_applicationContext ctx) {
        builder.handleCall(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitFunc_arg_list(Func_arg_listContext ctx) {
        builder.handleFunctionArgs(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitFunc_expr_common_subexpr(Func_expr_common_subexprContext ctx) {
        builder.handleCall(() -> {
            ParseTree child = ctx.getChild(0);
            builder.handleDomain(new ObjNameDomain(child.getText(), NameType.FUNCTION), DomainSource.OBJ_NAME);
            builder.handleFunctionArgs(() -> {
                for (ParseTree parseTree : ctx.children) {
                    if (parseTree instanceof Expr_listContext) {
                        for (A_exprContext aExprContext : ((Expr_listContext) parseTree).a_expr()) {
                            aExprContext.accept(this);
                        }
                    } else {
                        parseTree.accept(this);
                    }
                }
            });
        });
        return null;
    }

    @Override
    public Void visitWithin_group_clause(Within_group_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCase_expr(Case_exprContext ctx) {
        if (ctx.case_arg() != null) {
            builder.handleOtherDomains(() -> {
                ctx.case_arg().accept(this);
            });
        }
        if (ctx.case_default() != null) {
            ctx.case_default().accept(this);
        }
        ctx.when_clause_list().accept(this);
        return null;
    }

    @Override
    public Void visitWhen_clause_list(When_clause_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCase_default(Case_defaultContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCase_arg(Case_argContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWhen_clause(When_clauseContext ctx) {
        builder.handleOtherDomains(() -> {
            ctx.a_expr(0).accept(this);
        });

        ctx.a_expr(1).accept(this);
        return null;
    }

    @Override
    public Void visitOverlay_list(Overlay_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPosition_list(Position_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQual_op(Qual_opContext ctx) {
        return null;
    }

    @Override
    public Void visitAexprconst(AexprconstContext ctx) {
        builder.enterConstant();
        //        dmVisitChildren(ctx);
        builder.addAttr(CommonAttribute.VALUE, ctx.getText());
        builder.exitConstant();
        return null;
    }

    @Override
    public Void visitA_expr_compare(A_expr_compareContext ctx) {
        if (ctx.children.size() == 3) {
            String left = ctx.getChild(0).getText();
            String right = this.getText(((ParserRuleContext) ctx.getChild(2)));
            if (left.startsWith("(")) {
                left = left.substring(1, left.length() - 1);
            }
            if (right.startsWith("(")) {
                right = right.substring(1, right.length() - 1);
            }
            if (!right.equals(left)) {
                builder.addAttr(CommonAttribute.VALID_WHERE, true);
            }
        }
        dmVisitChildren(ctx);
        return null;
    }

    /**
     * valid where
     * @param ctx the parse tree
     *
     */
    @Override
    public Void visitA_expr_is_not(A_expr_is_notContext ctx) {
        for (ParseTree child : ctx.children) {
            if (child instanceof TerminalNodeImpl) {
                TerminalNodeImpl terminalNodeImpl = (TerminalNodeImpl) child;
                if (terminalNodeImpl.getSymbol().getType() == IS) {
                    builder.addAttr(CommonAttribute.VALID_WHERE, true);
                    break;
                }
            }
        }
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitStar_context(Star_contextContext ctx) {
        RdbColumnDomain rdbColumnDomain = new RdbColumnDomain();
        rdbColumnDomain.setColumn("*");
        builder.handleDomain(rdbColumnDomain, DomainSource.COLUMN);

        return null;
    }

    @Override
    public Void visitA_expr_in(A_expr_inContext ctx) {
        for (ParseTree child : ctx.children) {
            if (child instanceof TerminalNodeImpl) {
                TerminalNodeImpl terminalNodeImpl = (TerminalNodeImpl) child;
                if (terminalNodeImpl.getSymbol().getType() == IN_P) {
                    builder.addAttr(CommonAttribute.VALID_WHERE, true);
                    break;
                }
            }
        }
        dmVisitChildren(ctx);
        return null;
    }

    /**
     * limit
     */
    @Override
    public Void visitLimit_clause(Limit_clauseContext ctx) {
        builder.addAttr(CommonAttribute.LIMIT, true);
        if (ctx.select_limit_value() != null) {
            builder.handleOtherDomains(() -> {
                ctx.select_limit_value().accept(this);
            });
        }
        if (ctx.select_offset_value() != null) {
            builder.handleOtherDomains(() -> {
                ctx.select_offset_value().accept(this);
            });
        }
        if (ctx.select_fetch_first_value() != null && ctx.select_fetch_first_value().c_expr() != null) {
            builder.handleOtherDomains(() -> {
                ctx.select_fetch_first_value().c_expr().accept(this);
            });
        }
        return null;
    }

    @Override
    public Void visitSelect_limit_value(Select_limit_valueContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    /**
     * where
     */

    @Override
    public Void visitWhere_clause(Where_clauseContext ctx) {
        builder.handleWhere(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitA_expr(A_exprContext ctx) {
        builder.addAttr(CommonAttribute.VALUE, this.getText(ctx));
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIndexstmt(IndexstmtContext ctx) {
        builder.handleCreateIndex(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitIndex_elem(Index_elemContext ctx) {
        builder.handleColumnList(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitIndex_name_(Index_name_Context ctx) {
        builder.enterObjName(NameType.INDEX);
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitUnique_(Unique_Context ctx) {
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Unique), DomainSource.CONSTRAINT_TYPE);
        return null;
    }

    /**
     * with
     */
    @Override
    public Void visitCommon_table_expr(Common_table_exprContext ctx) {
        builder.handleWithSelect(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    /**
     * black
     */
    @Override
    public Void visitName_list_(Name_list_Context ctx) {
        throw new UnsupportedOperationException();
    }

    /**
     * alter table
     */

    @Override
    public Void visitAltertablestmt(AltertablestmtContext ctx) {
        if (ctx.getChild(1).getText().equalsIgnoreCase("table") && !ctx.getChild(2).getText().equalsIgnoreCase("all")) {
            builder.enterAlterTable();
            dmVisitChildren(ctx);
            builder.exitAlterTable();
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }

        return null;
    }

    @Override
    public Void visitAddColumn(AddColumnContext ctx) {
        builder.handleAlterTableItem(() -> {
            dmVisitChildren(ctx);
        }, AlterTableType.ADD_COLUMN);

        return null;
    }

    @Override
    public Void visitAlter_column_default(Alter_column_defaultContext ctx) {
        builder.enterColumnDefault();
        dmVisitChildren(ctx);
        builder.exitColumnDefault();

        return null;
    }

    @Override
    public Void visitConstraintDefault(ConstraintDefaultContext ctx) {
        String text = this.getText((ParserRuleContext) ctx.getChild(1));
        if (text.startsWith("'")) {
            text = text.substring(1, text.length() - 1);
        }
        builder.handleDomain(new RdbConstantDomain(text), DomainSource.COLUMN_DEFAULT_VALUE);

        return null;
    }

    @Override
    public Void visitDropColumn(DropColumnContext ctx) {
        builder.handleAlterTableItem(() -> {
            builder.handleColumnDef(() -> {
                dmVisitChildren(ctx);
            });
        }, AlterTableType.DROP_COLUMN);

        return null;
    }

    @Override
    public Void visitDropConstraint(DropConstraintContext ctx) {
        builder.handleAlterTableItem(() -> {
            builder.handleConstraint(() -> {
                dmVisitChildren(ctx);
            });
        }, AlterTableType.DROP_CONSTRAINT);
        return null;
    }

    @Override
    public Void visitAlterColumn(AlterColumnContext ctx) {
        builder.handleAlterTableItem(() -> {
            builder.handleColumnDef(() -> {
                dmVisitChildren(ctx);
            });
        }, AlterTableType.ALTER_COLUMN);

        return null;
    }

    @Override
    public Void visitAddConstraint(AddConstraintContext ctx) {
        builder.handleAlterTableItem(() -> {
            dmVisitChildren(ctx);
        }, AlterTableType.ADD_CONSTRAINT);
        return null;
    }

    @Override
    public Void visitConstraintelem(ConstraintelemContext ctx) {
        ParseTree child = ctx.getChild(0);
        TerminalNodeImpl node = (TerminalNodeImpl) child;
        int type = node.getSymbol().getType();
        if (type == PRIMARY) {
            builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);
        } else if (type == UNIQUE) {
            builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Unique), DomainSource.CONSTRAINT_TYPE);
        } else if (type == FOREIGN) {
            builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.ForeignKey), DomainSource.CONSTRAINT_TYPE);
        } else {
            throw new UnsupportedOperationException(node.getSymbol().getText());
        }
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTableconstraint(TableconstraintContext ctx) {
        builder.handleConstraint(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitColumnlist(ColumnlistContext ctx) {
        builder.handleColumnList(() -> {
            dmVisitChildren(ctx);
        });

        return null;
    }

    @Override
    public Void visitColumnDef(ColumnDefContext ctx) {
        builder.handleColumnDef(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitTypename(TypenameContext ctx) {
        builder.enterColumnType(this.getText(ctx));
        dmVisitChildren(ctx);
        builder.exitColumnType();
        return null;
    }

    @Override
    public Void visitOpt_array_bounds(Opt_array_boundsContext ctx) {
        builder.addAttr(PgAttribute.ARRAY_TYPE, true);
        return null;
    }

    /**
     * column def  ,column type
     */
    @Override
    public Void visitCharacter(CharacterContext ctx) {
        builder.handleDomain(new ObjNameDomain(Collections.singletonList(ctx.getChild(0).getText()), NameType.COLUMN_TYPE), DomainSource.OBJ_NAME);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitGenerictype(GenerictypeContext ctx) {
        builder.handleDomain(new ObjNameDomain(Collections.singletonList(ctx.getChild(0).getText()), NameType.COLUMN_TYPE), DomainSource.OBJ_NAME);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNumeric(NumericContext ctx) {
        builder.handleDomain(new ObjNameDomain(Collections.singletonList(ctx.getChild(0).getText()), NameType.COLUMN_TYPE), DomainSource.OBJ_NAME);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitJsonType(JsonTypeContext ctx) {
        builder.handleDomain(new ObjNameDomain(Collections.singletonList(ctx.getChild(0).getText()), NameType.COLUMN_TYPE), DomainSource.OBJ_NAME);
        return null;
    }

    @Override
    public Void visitConstinterval(ConstintervalContext ctx) {
        builder.handleDomain(new ObjNameDomain(Collections.singletonList(ctx.getChild(0).getText()), NameType.COLUMN_TYPE), DomainSource.OBJ_NAME);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConstdatetime(ConstdatetimeContext ctx) {
        //        builder.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());
        builder.handleDomain(new ObjNameDomain(Collections.singletonList(ctx.getChild(0).getText()), NameType.COLUMN_TYPE), DomainSource.OBJ_NAME);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConstraintNotNull(ConstraintNotNullContext ctx) {
        builder.addAttr(CommonAttribute.NOT_NULL, true);
        return null;
    }

    @Override
    public Void visitConstraintUnique(ConstraintUniqueContext ctx) {
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Unique), DomainSource.CONSTRAINT_TYPE);
        return null;
    }

    @Override
    public Void visitConstraintPrimary(ConstraintPrimaryContext ctx) {
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);
        return null;
    }

    @Override
    public Void visitConstraintReference(ConstraintReferenceContext ctx) {
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.ForeignKey), DomainSource.CONSTRAINT_TYPE);
        return null;
    }

    /**
     * insert
     */
    @Override
    public Void visitInsertstmt(InsertstmtContext ctx) {
        builder.handleInsert(() -> {
            dmVisitChildren(ctx);
        });

        return null;
    }

    @Override
    public Void visitInsert_column_list(Insert_column_listContext ctx) {
        builder.handleInsertColumn(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitExpr_list(Expr_listContext ctx) {
        builder.handleValues(() -> {
            dmVisitChildren(ctx);
        });

        return null;
    }

    @Override
    public Void visitOn_conflict_(On_conflict_Context ctx) {
        ParseTree child = ctx.getChild(ctx.children.size() - 1);
        boolean ignore = child.getText().equalsIgnoreCase("nothing");
        if (ignore) {
            builder.addAttr(CommonAttribute.INSERT_CONFLICT, RdbInsertConflictStrategy.IGNORE);
        } else {
            builder.addAttr(CommonAttribute.INSERT_CONFLICT, RdbInsertConflictStrategy.UPDATE);
        }

        return null;
    }

    /**
     * delete
     */
    @Override
    public Void visitDeletestmt(DeletestmtContext ctx) {
        builder.handleDelete(() -> {
            dmVisitChildren(ctx);
        });

        return null;
    }

    @Override
    public Void visitUsing_clause(Using_clauseContext ctx) {
        throw new UnsupportedOperationException(this.getText(ctx));
    }

    /**
     * update
     */
    @Override
    public Void visitUpdatestmt(UpdatestmtContext ctx) {
        builder.handleUpdate(() -> {
            dmVisitChildren(ctx);
        });

        return null;
    }

    @Override
    public Void visitSet_clause_list(Set_clause_listContext ctx) {
        builder.enterSetColumnValue();
        dmVisitChildren(ctx);
        builder.exitSetColumnValue();

        return null;
    }

    @Override
    public Void visitSet_target(Set_targetContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();

        return null;
    }

    @Override
    public Void visitWhere_or_current_clause(Where_or_current_clauseContext ctx) {
        builder.handleWhere(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    /**
     *  database
     */

    @Override
    public Void visitCreatedbstmt(CreatedbstmtContext ctx) {
        builder.enterCreateCatalog();
        dmVisitChildren(ctx);
        builder.exitCreateCatalog();

        return null;
    }

    @Override
    public Void visitDropdbstmt(DropdbstmtContext ctx) {
        builder.enterDropCatalog();
        dmVisitChildren(ctx);
        builder.exitDropCatalog();
        return null;
    }

    @Override
    public Void visitAlterdatabasestmt(AlterdatabasestmtContext ctx) {
        builder.enterAlterDatabase();
        dmVisitChildren(ctx);
        builder.exitAlterDatabase();

        return null;
    }

    @Override
    public Void visitRename_database_stmt(Rename_database_stmtContext ctx) {
        builder.handleRename(() -> {
            dmVisitChildren(ctx);
        }, TargetType.Catalog);

        return null;
    }

    @Override
    public Void visitAlterdatabasesetstmt(AlterdatabasesetstmtContext ctx) {
        builder.enterAlterDatabase();
        dmVisitChildren(ctx);
        builder.exitAlterDatabase();

        return null;
    }

    @Override
    public Void visitSetresetclause(SetresetclauseContext ctx) {
        builder.enterSetInfo();
        dmVisitChildren(ctx);
        builder.exitSetInfo();

        return null;
    }

    @Override
    public Void visitSetTablespaceName(SetTablespaceNameContext ctx) {
        builder.addAttr(PgAttribute.TABLESPACE, ctx.getChild(2).getText());
        return null;
    }

    @Override
    public Void visitRefresh_collation_version(Refresh_collation_versionContext ctx) {
        builder.addAttr(PgAttribute.REFRESH_COLLATION_VERSION, true);
        return null;
    }

    @Override
    public Void visitVar_list(Var_listContext ctx) {
        builder.enterConstant();
        dmVisitChildren(ctx);
        builder.addAttr(CommonAttribute.VALUE, ctx.getText());
        builder.exitConstant();
        return null;
    }

    @Override
    public Void visitDefault_(Default_Context ctx) {
        builder.addAttr(CommonAttribute.DEFAULT, true);
        return null;
    }

    @Override
    public Void visitReset_rest(Reset_restContext ctx) {
        builder.addAttr(CommonAttribute.VALUE, ctx.getText());
        return null;
    }

    /**
     * rename
     */
    @Override
    public Void visitRename_column_stmt(Rename_column_stmtContext ctx) {
        builder.handleRename(() -> {
            dmVisitChildren(ctx);
        }, TargetType.Column);

        return null;
    }

    @Override
    public Void visitRename_table_stmt(Rename_table_stmtContext ctx) {
        builder.handleRename(() -> {
            dmVisitChildren(ctx);
        }, TargetType.Table);

        return null;
    }

    /**
     * comment
     */
    @Override
    public Void visitComment_table_stmt(Comment_table_stmtContext ctx) {
        builder.enterComment(TargetType.Table);
        dmVisitChildren(ctx);
        builder.exitComment();
        return null;
    }

    @Override
    public Void visitComment_column_stmt(Comment_column_stmtContext ctx) {
        builder.enterComment(TargetType.Column);
        dmVisitChildren(ctx);
        builder.exitComment();
        return null;
    }

    @Override
    public Void visitComment_text(Comment_textContext ctx) {
        StringDomain domain = new StringDomain(ctx.getText());
        builder.handleDomain(domain, DomainSource.COMMENT);
        return null;
    }

    /**
     * CommonAttribute
     */
    @Override
    public Void visitIf_exists_(If_exists_Context ctx) {
        builder.addAttr(CommonAttribute.IF_EXISTS, true);

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIf_not_exists_(If_not_exists_Context ctx) {
        builder.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
        dmVisitChildren(ctx);
        return null;
    }

    /**
     * create table
     */
    @Override
    public Void visitCreatestmt(CreatestmtContext ctx) {
        if (ctx.OPEN_PAREN() != null) {
            builder.handleCreateTable(() -> {
                dmVisitChildren(ctx);
            });
        } else if (ctx.PARTITION() != null) {
            builder.handleCreateTable(() -> {
                ctx.qualified_name(0).accept(this);
            });

            builder.handleAlterTable(() -> {
                ctx.qualified_name(1).accept(this);
                PgTableDomain pgTableDomain = new PgTableDomain();
                pgTableDomain.setAuditKind(SecQueryKind.ALTER);
                pgTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
                builder.handleDomain(pgTableDomain, DomainSource.ALTER_TABLE_ITEM);
            });
        } else {
            throw new UnsupportedOperationException("Not supported:" + getText(ctx));
        }

        return null;
    }

    @Override
    public Void visitCreateasstmt(CreateasstmtContext ctx) {
        builder.handleCreateTable(() -> {
            dmVisitChildren(ctx);
        });

        return null;
    }

    @Override
    public Void visitOptinherit(OptinheritContext ctx) {
        builder.enterInherit();
        dmVisitChildren(ctx);
        builder.exitInherit();

        return null;
    }

    //    @Override
    //    public Void visitTerminal(TerminalNode node) {
    //        if(node.getSymbol().getType() == PostgreSQLLexer.PRIMARY){
    //            System.out.println(32);
    //        }
    //    }

    @Override
    public Void visitDroptablestmt(DroptablestmtContext ctx) {
        builder.handleDropTable(() -> {
            dmVisitChildren(ctx);
        });

        return null;
    }

    /**
     * schema
     */
    @Override
    public Void visitCreateschemastmt(CreateschemastmtContext ctx) {
        builder.handleCreateSchema(() -> {
            dmVisitChildren(ctx);
        });

        return null;
    }

    @Override
    public Void visitOptschemaname(OptschemanameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitDropschemastmt(DropschemastmtContext ctx) {
        builder.handleDropSchema(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitRename_schema_stmt(Rename_schema_stmtContext ctx) {
        builder.handleRename(() -> {
            dmVisitChildren(ctx);
        }, TargetType.Schema);

        return null;
    }

    @Override
    public Void visitDrop_behavior_(Drop_behavior_Context ctx) {
        String text = ctx.getText();
        if (text.equalsIgnoreCase("CASCADE")) {
            builder.addAttr(CommonAttribute.CASCADE, true);
        } else if (text.equalsIgnoreCase("RESTRICT")) {
            builder.addAttr(CommonAttribute.RESTRICT, true);
        }
        return null;
    }

    /**
     * view
     */
    @Override
    public Void visitViewstmt(ViewstmtContext ctx) {
        builder.handleCreateView(() -> {
            for (ParseTree child : ctx.children) {
                if (child instanceof Qualified_nameContext) {
                    visit(child);
                }
            }
        });

        return null;
    }

    @Override
    public Void visitCreatematviewstmt(CreatematviewstmtContext ctx) {
        builder.handleCreateView(() -> {
            builder.addAttr(CommonAttribute.MATERIALIZED, true);
            for (ParseTree child : ctx.children) {
                if (child instanceof Qualified_nameContext) {
                    visit(child);
                }
            }
        });

        return null;
    }

    @Override
    public Void visitCreatefunctionstmt(CreatefunctionstmtContext ctx) {
        builder.enterCreateFunction();
        for (ParseTree child : ctx.children) {
            if (child instanceof Func_nameContext) {
                visit(child);
            }
        }
        builder.exitCreateFunction();

        return null;
    }

    @Override
    public Void visitFunc_name(Func_nameContext ctx) {
        builder.enterObjName(NameType.FUNCTION);
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitCreatetrigstmt(CreatetrigstmtContext ctx) {
        builder.enterCreateTrigger();
        for (ParseTree child : ctx.children) {
            if (child instanceof Qualified_nameContext) {
                visit(child);
            }
        }
        builder.exitCreateTrigger();
        return null;
    }

    /**
     * user and role
     */
    @Override
    public Void visitCreaterolestmt(CreaterolestmtContext ctx) {
        builder.enterCreateRole();
        dmVisitChildren(ctx);
        builder.exitCreateRole();
        return null;
    }

    @Override
    public Void visitCreateuserstmt(CreateuserstmtContext ctx) {
        builder.enterCreateUser();
        dmVisitChildren(ctx);
        builder.exitCreateUser();
        return null;
    }

    @Override
    public Void visitAlteroptroleelem(AlteroptroleelemContext ctx) {
        String text = ctx.getChild(0).getText();
        if (text.equalsIgnoreCase("password")) {
            String text1 = ctx.getChild(1).getText();
            builder.addAttr(CommonAttribute.PASSWORD, text1.substring(1, text1.length() - 1));
        }
        return null;
    }

    @Override
    public Void visitDropuserstmt(DropuserstmtContext ctx) {
        builder.enterDropUser();
        dmVisitChildren(ctx);
        builder.exitDropUser();

        return null;
    }

    @Override
    public Void visitDroprolestmt(DroprolestmtContext ctx) {
        builder.enterDropRole();
        dmVisitChildren(ctx);
        builder.exitDropRole();

        return null;
    }

    @Override
    public Void visitGrantstmt(GrantstmtContext ctx) {
        builder.enterGrant();
        dmVisitChildren(ctx);
        builder.exitGrant();
        return null;
    }

    @Override
    public Void visitPrivileges(PrivilegesContext ctx) {
        return null;
    }

    @Override
    public Void visitPrivilege_target(Privilege_targetContext ctx) {
        return null;
    }

    @Override
    public Void visitRevokestmt(RevokestmtContext ctx) {
        builder.enterRevoke();
        dmVisitChildren(ctx);
        builder.exitRevoke();

        return null;
    }

    /**
     * for name
     */
    @Override
    public Void visitQualified_name(Qualified_nameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();

        return null;
    }

    @Override
    public Void visitAlterownerstmt(AlterownerstmtContext ctx) {
        String text = ctx.getChild(1).getText();
        if (text.equalsIgnoreCase("database")) {
            builder.enterAlterOwner(TargetType.Catalog);
        } else if (text.equalsIgnoreCase("schema")) {
            builder.enterAlterOwner(TargetType.Schema);
        } else {
            throw new UnsupportedOperationException(text);
        }
        dmVisitChildren(ctx);
        builder.exitAlterOwner();

        return null;
    }

    @Override
    public Void visitRolespec(RolespecContext ctx) {
        builder.enterRoleSpec(ctx.getText());
        dmVisitChildren(ctx);
        builder.exitRoleSpec();
        return null;
    }

    @Override
    public Void visitAny_name(Any_nameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();

        return null;
    }

    @Override
    public Void visitSubstr_list(Substr_listContext ctx) {
        for (A_exprContext aExprContext : ctx.a_expr()) {
            aExprContext.accept(this);
            builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(aExprContext));
        }
        return null;
    }

    @Override
    public Void visitArray_expr_list(Array_expr_listContext ctx) {
        builder.enterArray();
        dmVisitChildren(ctx);
        builder.exitArray();
        return null;
    }

    @Override
    public Void visitArray_expr(Array_exprContext ctx) {
        builder.enterArray();
        for (ParseTree child : ctx.children) {
            if (child instanceof Expr_listContext) {
                Expr_listContext context = (Expr_listContext) child;
                dmVisitChildren(context);
                break;
            }
        }
        builder.exitArray();
        return null;
    }

    @Override
    public Void visitFunc_expr_windowless(Func_expr_windowlessContext ctx) {
        if (ctx.parent instanceof Index_elemContext) {
            return null;
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }

    }

    @Override
    public Void visitBitwithoutlength(BitwithoutlengthContext ctx) {
        //        builder.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());
        builder.handleDomain(new ObjNameDomain(Collections.singletonList(ctx.getChild(0).getText()), NameType.COLUMN_TYPE), DomainSource.OBJ_NAME);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBitwithlength(BitwithlengthContext ctx) {
        //        builder.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());
        builder.handleDomain(new ObjNameDomain(Collections.singletonList(ctx.getChild(0).getText()), NameType.COLUMN_TYPE), DomainSource.OBJ_NAME);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSort_clause(Sort_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitIndex_elem_options(Index_elem_optionsContext ctx) {
        return null;
    }

    @Override
    public Void visitAccess_method_clause(Access_method_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitTruncatestmt(TruncatestmtContext ctx) {
        builder.enterTruncateTable();
        for (Relation_exprContext relationExprContext : ctx.relation_expr_list().relation_expr()) {
            relationExprContext.qualified_name().accept(this);
        }
        builder.exitTruncateTable();
        return null;
    }

    @Override
    public Void visitConstraintattributeElem(ConstraintattributeElemContext ctx) {
        return null;
    }

    @Override
    public Void visitCreateseqstmt(CreateseqstmtContext ctx) {
        builder.enterCreateSequence();
        ctx.qualified_name().accept(this);
        builder.exitCreateSequence();
        return null;
    }

    @Override
    public Void visitOpttablespace(OpttablespaceContext ctx) {
        return null;
    }

    @Override
    public Void visitOptwith(OptwithContext ctx) {
        return null;
    }

    @Override
    public Void visitOptpartitionspec(OptpartitionspecContext ctx) {
        return null;
    }

    @Override
    public Void visitStmtblock(StmtblockContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitStmtmulti(StmtmultiContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitStmt(StmtContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCallstmt(CallstmtContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWith_(With_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitOptrolelist(OptrolelistContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreateoptroleelem(CreateoptroleelemContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitOptschemaeltlist(OptschemaeltlistContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSchema_stmt(Schema_stmtContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSet_rest(Set_restContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitGeneric_set(Generic_setContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSet_rest_more(Set_rest_moreContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitVar_name(Var_nameContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitVar_value(Var_valueContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBoolean_or_string_(Boolean_or_string_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNonreservedword_or_sconst(Nonreservedword_or_sconstContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitVariableresetstmt(VariableresetstmtContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAlter_table_cmds(Alter_table_cmdsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitOpttableelementlist(OpttableelementlistContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTableelementlist(TableelementlistContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTableelement(TableelementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColquallist(ColquallistContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColconstraint(ColconstraintContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConstraintNull(ConstraintNullContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitGenerated_when(Generated_whenContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumn_list_(Column_list_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnElem(ColumnElemContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPartition_item(Partition_itemContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreate_as_target(Create_as_targetContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConstraintattributespec(ConstraintattributespecContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAny_name_list_(Any_name_list_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAttrs(AttrsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCommentstmt(CommentstmtContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitGrantee_list(Grantee_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitGrantee(GranteeContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIndex_params(Index_paramsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFunc_body(Func_bodyContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFunc_body_statement(Func_body_statementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAssignment_statement(Assignment_statementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRenamestmt(RenamestmtContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumn_(Column_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreatedb_opt_list(Createdb_opt_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreatedb_opt_items(Createdb_opt_itemsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreatedb_opt_item(Createdb_opt_itemContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreatedb_opt_name(Createdb_opt_nameContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitEqual_(Equal_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPreparablestmt(PreparablestmtContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitInsert_target(Insert_targetContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitInsert_rest(Insert_restContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitInsert_column_item(Insert_column_itemContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSet_clause(Set_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSet_target_list(Set_target_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSelectstmt(SelectstmtContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSelect_with_parens(Select_with_parensContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWith_clause(With_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCte_list(Cte_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWith_clause_(With_clause_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAll_or_distinct(All_or_distinctContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDistinct_clause(Distinct_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSelect_limit(Select_limitContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitValues_clause(Values_clauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFrom_list(From_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitJoin_type(Join_typeContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRelation_expr(Relation_exprContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRelation_expr_opt_alias(Relation_expr_opt_aliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSimpletypename(SimpletypenameContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitType_modifiers_(Type_modifiers_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFloat_(Float_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBit(BitContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCharacter_c(Character_cContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitVarying_(Varying_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTimezone_(Timezone_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitInterval_(Interval_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitInterval_second(Interval_secondContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_qual(A_expr_qualContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_lessless(A_expr_lesslessContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_or(A_expr_orContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_and(A_expr_andContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_between(A_expr_betweenContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_unary_not(A_expr_unary_notContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_isnull(A_expr_isnullContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_like(A_expr_likeContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_qual_op(A_expr_qual_opContext ctx) {
        ctx.a_expr_unary_qualop(0).accept(this);
        builder.handleOtherDomains(() -> {
            for (int i = 1; i < ctx.a_expr_unary_qualop().size(); i++) {
                ctx.a_expr_unary_qualop(i).accept(this);
            }
        });
        return null;
    }

    @Override
    public Void visitA_expr_unary_qualop(A_expr_unary_qualopContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_add(A_expr_addContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_mul(A_expr_mulContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_caret(A_expr_caretContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_unary_sign(A_expr_unary_signContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_at_time_zone(A_expr_at_time_zoneContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_collate(A_expr_collateContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitA_expr_typecast(A_expr_typecastContext ctx) {
        ctx.c_expr().accept(this);
        return null;
    }

    @Override
    public Void visitB_expr(B_exprContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitC_expr(C_exprContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFunc_expr(Func_exprContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitImplicit_row(Implicit_rowContext ctx) {
        builder.handleValues(()->{
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitFunc_arg_expr(Func_arg_exprContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIn_expr_select(In_expr_selectContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIn_expr_list(In_expr_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIndirection_el(Indirection_elContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIndirection(IndirectionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitOpt_indirection(Opt_indirectionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTarget_list_(Target_list_Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTarget_list(Target_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQualified_name_list(Qualified_name_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitName(NameContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAttr_name(Attr_nameContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIconst(IconstContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSconst(SconstContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAnysconst(AnysconstContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSignediconst(SignediconstContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRoleid(RoleidContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRole_list(Role_listContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColid(ColidContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitType_function_name(Type_function_nameContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNonreservedword(NonreservedwordContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColLabel(ColLabelContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBareColLabel(BareColLabelContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBare_label_keyword(Bare_label_keywordContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitOffset_clause(Offset_clauseContext ctx) {
        if (ctx.select_offset_value() != null) {
            builder.handleOtherDomains(() -> {
                ctx.select_offset_value().accept(this);
            });
        } else if (ctx.select_fetch_first_value() != null && ctx.select_fetch_first_value().c_expr() != null) {
            ctx.select_fetch_first_value().c_expr().accept(this);
        }
        return null;
    }

    @Override
    public Void visitSelect_offset_value(Select_offset_valueContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }
}
