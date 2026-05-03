package com.clougence.clouddm.dsfamily.mysql.analysis;

import static com.clougence.clouddm.dsfamily.mysql.parser.antlr.MySqlParser.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ColumnTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ConstraintTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.MyBuilderFactory;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.*;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.dsfamily.mysql.parser.antlr.MySqlParserBaseVisitor;

public class MySQLParserVisitor extends MySqlParserBaseVisitor<Void> {

    protected final MyBuilderFactory builder;
    protected final Parser           parser;

    public MySQLParserVisitor(MyBuilderFactory builder, Parser parser){
        this.builder = builder;
        this.parser = parser;
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

    private String getText(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    @Override
    public Void visitQuerySpecification(QuerySpecificationContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitWithSelectStatement(WithSelectStatementContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitParenthesisSelect(ParenthesisSelectContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitSelectElements(SelectElementsContext ctx) {
        if (ctx.getChild(0).getText().equals("*")) {
            builder.handleBuildSelectItem(() -> {
                RdbColumnDomain rdbColumnDomain = new RdbColumnDomain();
                rdbColumnDomain.setColumn("*");
                builder.handleDomain(rdbColumnDomain, DomainSource.COLUMN);
            });
        }
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSelectColumnElement(SelectColumnElementContext ctx) {
        builder.handleBuildSelectItem(() -> {
            builder.addAttr(CommonAttribute.VALUE, this.getText((ParserRuleContext) ctx.getChild(0)));
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitSelectExpressionElement(SelectExpressionElementContext ctx) {
        builder.handleBuildSelectItem(() -> {
            ExpressionAtomContext expressionAtomContext = findExpressionAtomContext(ctx.expression());
            if (expressionAtomContext != null && expressionAtomContext.children.size() > 1) {
                builder.addAttr(CommonAttribute.VALUE, this.getText((ParserRuleContext) ctx.getChild(0)));
            } else {
                String text = this.getText((ParserRuleContext) ctx.getChild(0));
                if (text.startsWith("'")) {
                    text = text.substring(1, text.length() - 1);
                }
                builder.addAttr(CommonAttribute.VALUE, text);
            }

            dmVisitChildren(ctx);
        });
        return null;
    }

    private ExpressionAtomContext findExpressionAtomContext(ParserRuleContext ctx) {
        for (ParseTree child : ctx.children) {
            if (child instanceof ExpressionAtomContext) {
                return (ExpressionAtomContext) child;
            } else if (child instanceof ParserRuleContext) {
                return findExpressionAtomContext((ParserRuleContext) child);
            }
        }
        return null;
    }

    @Override
    public Void visitSelectFunctionElement(SelectFunctionElementContext ctx) {
        builder.handleBuildSelectItem(() -> {
            builder.addAttr(CommonAttribute.VALUE, this.getText((ParserRuleContext) ctx.getChild(0)));
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitAliasName(AliasNameContext ctx) {
        builder.addAttr(CommonAttribute.ALIAS, this.getText(ctx));
        return null;
    }

    @Override
    public Void visitGetFormatFunctionCall(GetFormatFunctionCallContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.GET_FORMAT().getText()), DomainSource.OBJ_NAME);
            builder.handleFunctionArgs(() -> {
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, ctx.datetimeFormat.getText());
                builder.handleDomain(new RdbConstantDomain(ctx.datetimeFormat.getText()), DomainSource.CONSTANT);

                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, ctx.stringLiteral().getText());
                ctx.stringLiteral().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitTrimFunctionCall(TrimFunctionCallContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.TRIM().getText()), DomainSource.OBJ_NAME);
            builder.handleFunctionArgs(() -> {
                if (ctx.positioinForm != null) {
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, ctx.positioinForm.getText());
                    builder.handleDomain(new RdbConstantDomain(ctx.positioinForm.getText()), DomainSource.CONSTANT);
                }
                if (ctx.sourceString != null) {
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, ctx.sourceString.getText());
                    builder.handleDomain(new RdbConstantDomain(ctx.sourceString.getText()), DomainSource.CONSTANT);
                } else if (ctx.sourceExpression != null) {
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.sourceExpression));
                    ctx.sourceExpression.accept(this);
                }

                if (ctx.fromString != null) {
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, ctx.fromString.getText());
                    builder.handleDomain(new RdbConstantDomain(ctx.fromString.getText()), DomainSource.CONSTANT);
                } else if (ctx.sourceExpression != null) {
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.sourceExpression));
                    ctx.sourceExpression.accept(this);
                }
            });
        });
        return null;
    }

    @Override
    public Void visitSimpleFunctionCall(SimpleFunctionCallContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.getChild(0).getText()), DomainSource.OBJ_NAME);
        });
        return null;
    }

    @Override
    public Void visitCharFunctionCall(CharFunctionCallContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.CHAR().getText()), DomainSource.OBJ_NAME);
            ctx.functionArgs().accept(this);
        });
        return null;
    }

    @Override
    public Void visitPositionFunctionCall(PositionFunctionCallContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.POSITION().getText()), DomainSource.OBJ_NAME);
            builder.handleFunctionArgs(() -> {
                if (ctx.positionString != null) {
                    ctx.positionString.accept(this);
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, ctx.positionString.getText());
                } else {
                    ctx.positionExpression.accept(this);
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.positionExpression));
                }

                if (ctx.inString != null) {
                    ctx.inString.accept(this);
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, ctx.inString.getText());
                } else {
                    ctx.inExpression.accept(this);
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.inExpression));
                }
            });

        });
        return null;
    }

    /**
     *  func call
     * @param ctx
     * @return
     */

    @Override
    public Void visitUdfFunctionCall(UdfFunctionCallContext ctx) {
        builder.handleCall(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitScalarFunctionCall(ScalarFunctionCallContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.scalarFunctionName().getText()), DomainSource.OBJ_NAME);
            if (ctx.functionArgs() != null) {
                ctx.functionArgs().accept(this);
            }
        });
        return null;
    }

    @Override
    public Void visitScalarFunctionName(ScalarFunctionNameContext ctx) {
        ObjNameDomain objNameDomain = new ObjNameDomain(Arrays.asList(this.getText(ctx)), NameType.FUNCTION);
        builder.handleDomain(objNameDomain, DomainSource.OBJ_NAME);
        return null;
    }

    @Override
    public Void visitWhereClause(WhereClauseContext ctx) {
        builder.handleWhere(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitFunctionArgs(FunctionArgsContext ctx) {
        builder.handleFunctionArgs(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitCustomFunctionName(CustomFunctionNameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitConstant(ConstantContext ctx) {
        String text = this.getText(ctx);
        if (text.startsWith("'")) {
            text = text.substring(1, text.length() - 1);
        }
        builder.handleDomain(new RdbConstantDomain(text), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitProcName(ProcNameContext ctx) {
        builder.enterObjName(NameType.PROCEDURE);
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitProcedureArgs(ProcedureArgsContext ctx) {
        builder.handleFunctionArgs(() -> {
            for (ParseTree child : ctx.children) {
                if (!child.getText().equals(",")) {
                    String text = this.getText((ParserRuleContext) child);
                    if (text.startsWith("'")) {
                        text = text.substring(1, text.length() - 1);
                    }
                    builder.addAttr(CommonAttribute.VALUE, text);
                }
            }
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitFullColumnName(FullColumnNameContext ctx) {
        builder.handleSelectColumn(() -> {
            builder.handleObjName(() -> {
                dmVisitChildren(ctx);
            });
        });
        return null;
    }

    @Override
    public Void visitBinaryComparasionPredicate(BinaryComparasionPredicateContext ctx) {
        String left = ctx.getChild(0).getText();
        String right = ctx.getChild(2).getText();
        if (left.startsWith("(")) {
            left = left.substring(1, left.length() - 1);
        }
        if (right.startsWith("(")) {
            right = right.substring(1, right.length() - 1);
        }
        if (!left.equals(right)) {
            builder.addAttr(CommonAttribute.VALID_WHERE, true);
        }
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitInPredicate(InPredicateContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIsNullPredicate(IsNullPredicateContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFromClause(FromClauseContext ctx) {
        builder.enterSelectFromBuilder();
        dmVisitChildren(ctx);
        builder.exitSelectFromBuilder();
        return null;
    }

    @Override
    public Void visitTableSources(TableSourcesContext ctx) {
        builder.enterSelectTableBuilder();
        dmVisitChildren(ctx);
        builder.exitSelectTableBuilder();
        return null;
    }

    @Override
    public Void visitTableName(TableNameContext ctx) {
        builder.enterObjName(NameType.TABLE);
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitSimpleId(SimpleIdContext ctx) {

        builder.addAttr(CommonAttribute.VALUE, this.getText(ctx));
        return null;
    }

    @Override
    public Void visitCreateDatabase(CreateDatabaseContext ctx) {
        builder.enterCreateSchema();
        dmVisitChildren(ctx);
        builder.exitCreateSchema();
        return null;
    }

    @Override
    public Void visitDatabaseName(DatabaseNameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitCreateIndex(CreateIndexContext ctx) {
        builder.enterCreateIndex();
        if (ctx.indexCategory != null) {
            if (ctx.indexCategory.getType() == UNIQUE) {
                builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Unique), DomainSource.CONSTRAINT_TYPE);
            }
        }
        dmVisitChildren(ctx);
        builder.exitCreateIndex();
        return null;
    }

    @Override
    public Void visitCreateProcedure(CreateProcedureContext ctx) {
        builder.enterCreateProcedure();
        builder.enterObjName();
        ctx.fullId().accept(this);
        builder.exitObjName();
        builder.enterExitProcedure();
        return null;
    }

    @Override
    public Void visitCreateFunction(CreateFunctionContext ctx) {
        builder.enterCreateFunction();
        builder.enterObjName();
        ctx.fullId().accept(this);
        builder.exitObjName();
        builder.exitCreateFunction();
        return null;
    }

    @Override
    public Void visitCreateRole(CreateRoleContext ctx) {
        builder.enterCreateRole();
        dmVisitChildren(ctx);
        builder.exitCreateRole();
        return null;
    }

    @Override
    public Void visitCopyCreateTable(CopyCreateTableContext ctx) {
        builder.enterCreateTable(SecQueryType.CREATE_TABLE_LIKE);
        dmVisitChildren(ctx);
        builder.exitCreateTable();
        return null;
    }

    @Override
    public Void visitQueryCreateTable(QueryCreateTableContext ctx) {
        builder.enterCreateTable(SecQueryType.CREATE_TABLE_SELECT);
        dmVisitChildren(ctx);
        builder.exitCreateTable();
        return null;
    }

    @Override
    public Void visitColumnCreateTable(ColumnCreateTableContext ctx) {
        builder.enterCreateTable(SecQueryType.CREATE_TABLE);
        dmVisitChildren(ctx);
        builder.exitCreateTable();
        return null;
    }

    @Override
    public Void visitCreateTrigger(CreateTriggerContext ctx) {
        builder.enterCreateTrigger();
        builder.enterObjName(NameType.TRIGGER);
        ctx.thisTrigger.accept(this);
        builder.exitObjName();
        builder.exitCreateTrigger();
        return null;
    }

    @Override
    public Void visitCreateView(CreateViewContext ctx) {

        builder.enterCreateView();
        builder.enterObjName();
        ctx.fullId().accept(this);
        builder.exitObjName();
        builder.exitCreateView();
        return null;
    }

    @Override
    public Void visitIndexOption(IndexOptionContext ctx) {
        if (ctx.getChild(0).getText().equalsIgnoreCase("comment")) {
            String text = ctx.getChild(1).getText();
            text = text.substring(1, text.length() - 1);
            builder.addAttr(CommonAttribute.COMMENT, text);
        }
        return null;
    }

    @Override
    public Void visitColumnDefinition(ColumnDefinitionContext ctx) {
        builder.handleColumnDef(() -> {
            builder.handleDomain(new ObjNameDomain(Collections.singletonList(getName(ctx.uid())), null), DomainSource.OBJ_NAME);

            for (int i = 1; i < ctx.children.size(); i++) {
                ctx.getChild(i).accept(this);
            }

            if (ctx.dataType() instanceof StringDataTypeContext) {
                StringDataTypeContext stringDataTypeContext = (StringDataTypeContext) ctx.dataType();
                if (stringDataTypeContext.collationName() != null) {
                    builder.addAttr(MyAttribute.COLLATE, stringDataTypeContext.collationName().getText());
                }
                if (stringDataTypeContext.charsetName() != null) {
                    builder.addAttr(MyAttribute.CHARACTER_SET, stringDataTypeContext.charsetName().getText());
                }
            }
        });
        return null;
    }

    @Override
    public Void visitNullColumnConstraint(NullColumnConstraintContext ctx) {
        builder.addAttr(CommonAttribute.NOT_NULL, true);
        return null;
    }

    @Override
    public Void visitDefaultColumnConstraint(DefaultColumnConstraintContext ctx) {
        builder.handleDomain(new RdbConstantDomain(this.getText(ctx.defaultValue())), DomainSource.COLUMN_DEFAULT_VALUE);
        return null;
    }

    @Override
    public Void visitInvisibleColumnConstraint(InvisibleColumnConstraintContext ctx) {
        return null;
    }

    @Override
    public Void visitAutoIncrementColumnConstraint(AutoIncrementColumnConstraintContext ctx) {
        if (ctx.AUTO_INCREMENT() != null) {
            builder.addAttr(MyAttribute.AUTO_INCREMENT, true);
        }
        return null;
    }

    @Override
    public Void visitPrimaryKeyColumnConstraint(PrimaryKeyColumnConstraintContext ctx) {

        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);
        return null;
    }

    @Override
    public Void visitUniqueKeyColumnConstraint(UniqueKeyColumnConstraintContext ctx) {
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Unique), DomainSource.CONSTRAINT_TYPE);
        return null;
    }

    @Override
    public Void visitCommentColumnConstraint(CommentColumnConstraintContext ctx) {
        String text = ctx.STRING_LITERAL().getText();
        builder.addAttr(CommonAttribute.COMMENT, text.substring(1, text.length() - 1));
        return null;
    }

    @Override
    public Void visitReferenceColumnConstraint(ReferenceColumnConstraintContext ctx) {
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.ForeignKey), DomainSource.CONSTRAINT_TYPE);
        return null;
    }

    @Override
    public Void visitGeneratedColumnConstraint(GeneratedColumnConstraintContext ctx) {
        return null;
    }

    @Override
    public Void visitPrimaryKeyTableConstraint(PrimaryKeyTableConstraintContext ctx) {
        builder.enterConstraint();
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);
        dmVisitChildren(ctx);
        builder.exitConstraint();
        return null;
    }

    @Override
    public Void visitUniqueKeyTableConstraint(UniqueKeyTableConstraintContext ctx) {
        builder.enterConstraint();
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Unique), DomainSource.CONSTRAINT_TYPE);
        dmVisitChildren(ctx);
        builder.exitConstraint();
        return null;
    }

    @Override
    public Void visitCheckTableConstraint(CheckTableConstraintContext ctx) {
        builder.enterConstraint();
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Check), DomainSource.CONSTRAINT_TYPE);
        dmVisitChildren(ctx);
        builder.exitConstraint();
        return null;
    }

    @Override
    public Void visitForeignKeyTableConstraint(ForeignKeyTableConstraintContext ctx) {

        builder.enterConstraint();
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.ForeignKey), DomainSource.CONSTRAINT_TYPE);
        dmVisitChildren(ctx);
        builder.exitConstraint();
        return null;
    }

    @Override
    public Void visitReferenceDefinition(ReferenceDefinitionContext ctx) {
        return null;
    }

    @Override
    public Void visitSimpleIndexDeclaration(SimpleIndexDeclarationContext ctx) {
        builder.enterCreateIndex();
        dmVisitChildren(ctx);
        builder.exitCreateIndex();
        return null;
    }

    @Override
    public Void visitSpecialIndexDeclaration(SpecialIndexDeclarationContext ctx) {
        builder.handleCreateIndex(() -> {
            builder.addAttr(CommonAttribute.INDEX_TYPE, ctx.getChild(0).getText());
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitOptimizeTable(OptimizeTableContext ctx) {
        for (TableNameContext tableNameContext : ctx.tables().tableName()) {
            builder.handleOptimizeTable(() -> {
                tableNameContext.accept(this);
            });
        }
        return null;
    }

    @Override
    public Void visitCheckTable(CheckTableContext ctx) {
        for (TableNameContext tableNameContext : ctx.tables().tableName()) {
            builder.handleResource(() -> {
                tableNameContext.accept(this);
            }, SecQueryType.CHECK_TABLE, SecQueryKind.ALTER, true, TargetType.Table);
        }
        return null;
    }

    @Override
    public Void visitChecksumTable(ChecksumTableContext ctx) {
        for (TableNameContext tableNameContext : ctx.tables().tableName()) {
            builder.handleResource(() -> {
                tableNameContext.accept(this);
            }, SecQueryType.CHECK_TABLE, SecQueryKind.OTHER, true, TargetType.Table);
        }
        return null;
    }

    @Override
    public Void visitInstallPlugin(InstallPluginContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.INSTALL_PLUGIN);
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitUninstallPlugin(UninstallPluginContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.UNINSTALL_PLUGIN);
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitCreateUdfFunction(CreateUdfFunctionContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.CREATE_UDF_FUNCTION);
        rdbResourceDomain.setAuditKind(SecQueryKind.CREATE);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Function);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitRepairTable(RepairTableContext ctx) {
        for (TableNameContext tableNameContext : ctx.tables().tableName()) {
            builder.handleResource(() -> {
                tableNameContext.accept(this);
            }, SecQueryType.REPAIR, SecQueryKind.OTHER, true, TargetType.Table);
        }
        return null;
    }

    @Override
    public Void visitAnalyzeTable(AnalyzeTableContext ctx) {
        for (TableNameContext tableNameContext : ctx.tables().tableName()) {
            builder.handleAnalyzeTable(() -> {
                tableNameContext.accept(this);
            });
        }
        return null;
    }

    @Override
    public Void visitFullDescribeStatement(FullDescribeStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.EXPLAIN);
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitCreateEvent(CreateEventContext ctx) {
        builder.handleCreateEvent(() -> {
            builder.handleObjName(() -> {
                ctx.fullId().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitSetTransaction(SetTransactionContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setSqlType(SecQueryType.TRANSACTION);
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitDropProcedure(DropProcedureContext ctx) {
        builder.enterDropProcedure();
        builder.handleObjName(() -> {
            ctx.fullId().accept(this);
        }, NameType.PROCEDURE);
        builder.exitDropProcedure();
        return null;
    }

    @Override
    public Void visitDropEvent(DropEventContext ctx) {
        builder.handleDropEvent(() -> {
            builder.handleObjName(() -> {
                ctx.fullId().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitDropTrigger(DropTriggerContext ctx) {
        builder.enterDropTrigger();
        builder.handleObjName(() -> {
            ctx.fullId().accept(this);
        }, NameType.TRIGGER);
        builder.exitDropTrigger();
        return null;
    }

    @Override
    public Void visitDropView(DropViewContext ctx) {
        for (FullIdContext fullIdContext : ctx.fullId()) {
            builder.handleDropView(() -> {
                builder.handleObjName(() -> {
                    fullIdContext.accept(this);
                });
            });
        }
        return null;
    }

    @Override
    public Void visitDropFunction(DropFunctionContext ctx) {
        builder.enterDropFunction();
        builder.handleObjName(() -> {
            ctx.fullId().accept(this);
        }, NameType.FUNCTION);
        builder.exitDropFunction();
        return null;
    }

    @Override
    public Void visitJsonExpressionAtom(JsonExpressionAtomContext ctx) {
        ctx.left.accept(this);

        builder.handleOtherDomains(() -> {
            ctx.right.accept(this);
        });
        return null;
    }

    @Override
    public Void visitIndexName(IndexNameContext ctx) {
        builder.enterObjName(NameType.INDEX);
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitTableOptionEngine(TableOptionEngineContext ctx) {
        if (ctx.engineName() != null) {
            builder.addAttr(MyAttribute.ENGINE, this.getText(ctx.engineName()));
        }
        return null;
    }

    @Override
    public Void visitTableOptionAutoIncrement(TableOptionAutoIncrementContext ctx) {
        String text = ctx.decimalLiteral().getText();
        builder.addAttr(MyAttribute.AUTO_INCREMENT, text);
        return null;
    }

    @Override
    public Void visitTableOptionCharset(TableOptionCharsetContext ctx) {
        if (ctx.charsetName() != null) {
            builder.addAttr(MyAttribute.CHARACTER_SET, this.getText(ctx.charsetName()));
        }
        return null;
    }

    @Override
    public Void visitTableOptionCollate(TableOptionCollateContext ctx) {
        builder.addAttr(MyAttribute.COLLATE, this.getText(ctx.collationName()));
        return null;
    }

    @Override
    public Void visitTemporary_(Temporary_Context ctx) {
        builder.addAttr(MyAttribute.TEMPORARY, true);
        return null;
    }

    @Override
    public Void visitTableOptionComment(TableOptionCommentContext ctx) {
        ParseTree child = ctx.getChild(ctx.children.size() - 1);
        builder.addAttr(CommonAttribute.COMMENT, child.getText().substring(1, child.getText().length() - 1));
        return null;
    }

    @Override
    public Void visitPartitionDefinitions(PartitionDefinitionsContext ctx) {
        return null;
    }

    @Override
    public Void visitAlterSimpleDatabase(AlterSimpleDatabaseContext ctx) {
        builder.enterAlterSchema();
        dmVisitChildren(ctx);
        builder.exitAlterSchema();
        return null;
    }

    @Override
    public Void visitAlterTable(AlterTableContext ctx) {
        builder.enterAlterTable();
        dmVisitChildren(ctx);
        builder.exitAlterTable();
        return null;
    }

    @Override
    public Void visitAlterByAddColumn(AlterByAddColumnContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ADD_COLUMN);
        dmVisitChildren(ctx);
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitAlterByAddIndex(AlterByAddIndexContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ADD_INDEX);
        builder.enterCreateIndex();
        dmVisitChildren(ctx);
        builder.exitCreateIndex();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitAlterByAddPrimaryKey(AlterByAddPrimaryKeyContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ADD_CONSTRAINT);
        builder.enterConstraint();
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);
        dmVisitChildren(ctx);
        builder.exitConstraint();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitAlterByAddUniqueKey(AlterByAddUniqueKeyContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ADD_CONSTRAINT);
        builder.enterConstraint();
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Unique), DomainSource.CONSTRAINT_TYPE);
        dmVisitChildren(ctx);
        builder.exitConstraint();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitAlterByAddCheckTableConstraint(AlterByAddCheckTableConstraintContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ADD_CONSTRAINT);
        builder.enterConstraint();
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Check), DomainSource.CONSTRAINT_TYPE);
        dmVisitChildren(ctx);
        builder.exitConstraint();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitCreateTablespaceInnodb(CreateTablespaceInnodbContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.CREATE_OBJECT);
        rdbResourceDomain.setAuditKind(SecQueryKind.CREATE);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitAlterLogfileGroup(AlterLogfileGroupContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.ALTER_OBJECT);
        rdbResourceDomain.setAuditKind(SecQueryKind.ALTER);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitAlterTablespace(AlterTablespaceContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.ALTER_OBJECT);
        rdbResourceDomain.setAuditKind(SecQueryKind.ALTER);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitDropTablespace(DropTablespaceContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.DROP_OBJECT);
        rdbResourceDomain.setAuditKind(SecQueryKind.DROP);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitDropLogfileGroup(DropLogfileGroupContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.DROP_OBJECT);
        rdbResourceDomain.setAuditKind(SecQueryKind.DROP);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitCreateLogfileGroup(CreateLogfileGroupContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.CREATE_OBJECT);
        rdbResourceDomain.setAuditKind(SecQueryKind.CREATE);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitCreateTablespaceNdb(CreateTablespaceNdbContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.CREATE_OBJECT);
        rdbResourceDomain.setAuditKind(SecQueryKind.CREATE);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitAlterByAddSpecialIndex(AlterByAddSpecialIndexContext ctx) {

        builder.enterAlterTableItem(AlterTableType.ADD_INDEX);
        builder.enterCreateIndex();
        dmVisitChildren(ctx);
        builder.exitCreateIndex();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitAlterByAddForeignKey(AlterByAddForeignKeyContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ADD_CONSTRAINT);
        builder.enterConstraint();
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.ForeignKey), DomainSource.CONSTRAINT_TYPE);
        dmVisitChildren(ctx);
        builder.exitConstraint();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitAlterByChangeDefault(AlterByChangeDefaultContext ctx) {
        MyColumnDomain myColumnDomain = new MyColumnDomain();
        myColumnDomain.setAuditKind(SecQueryKind.ALTER);
        myColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_ALTER_COLUMN);
        myColumnDomain.setColumn(getName(ctx.uid()));
        if (ctx.defaultValue() != null) {
            String text = this.getText(ctx.defaultValue());
            if (text.startsWith("'")) {
                text = text.substring(1, text.length() - 1);
            }
            myColumnDomain.setDefaultValue(text);
        }
        builder.handleDomain(myColumnDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterByChangeColumn(AlterByChangeColumnContext ctx) {
        builder.handleAlterTableItem(() -> {
            dmVisitChildren(ctx);
        }, AlterTableType.ALTER_COLUMN);
        return null;
    }

    @Override
    public Void visitAlterByModifyColumn(AlterByModifyColumnContext ctx) {
        builder.handleAlterTableItem(() -> {
            ctx.columnDefinition().accept(this);
        }, AlterTableType.ALTER_COLUMN);

        return null;
    }

    @Override
    public Void visitAlterByDropColumn(AlterByDropColumnContext ctx) {
        builder.handleAlterTableItem(() -> {
            builder.handleColumnDef(() -> {
                dmVisitChildren(ctx);
            });
        }, AlterTableType.DROP_COLUMN);
        return null;
    }

    @Override
    public Void visitAlterByDropConstraintCheck(AlterByDropConstraintCheckContext ctx) {
        builder.handleAlterTableItem(() -> {
            builder.handleConstraint(() -> {
                dmVisitChildren(ctx);
            });
        }, AlterTableType.DROP_CONSTRAINT);

        return null;
    }

    @Override
    public Void visitAlterByDropPrimaryKey(AlterByDropPrimaryKeyContext ctx) {
        builder.handleAlterTableItem(() -> {
            builder.handleConstraint(() -> {
                builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);
            });
        }, AlterTableType.DROP_CONSTRAINT);
        return null;
    }

    @Override
    public Void visitAlterByDropIndex(AlterByDropIndexContext ctx) {
        builder.handleAlterTableItem(() -> {
            builder.enterDropIndex();
            dmVisitChildren(ctx);
            builder.exitDropIndex();
        }, AlterTableType.DROP_INDEX);
        return null;
    }

    @Override
    public Void visitAlterByRenameIndex(AlterByRenameIndexContext ctx) {
        MyIndexDomain myIndexDomain = new MyIndexDomain();
        myIndexDomain.setSqlType(SecQueryType.ALTER_INDEX);
        myIndexDomain.setAuditKind(SecQueryKind.ALTER);
        myIndexDomain.setName(getName(ctx.uid(0)));
        myIndexDomain.setNewName(getName(ctx.uid(1)));

        builder.handleDomain(myIndexDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterByAlterIndexVisibility(AlterByAlterIndexVisibilityContext ctx) {
        MyIndexDomain myIndexDomain = new MyIndexDomain();
        myIndexDomain.setSqlType(SecQueryType.ALTER_INDEX);
        myIndexDomain.setAuditKind(SecQueryKind.ALTER);
        myIndexDomain.setName(getName(ctx.uid()));
        myIndexDomain.setVisible(ctx.visivility.getType() == VISIBLE);

        builder.handleDomain(myIndexDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterByDropForeignKey(AlterByDropForeignKeyContext ctx) {
        builder.handleAlterTableItem(() -> {
            builder.handleConstraint(() -> {
                builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.ForeignKey), DomainSource.CONSTRAINT_TYPE);
                dmVisitChildren(ctx);
            });
        }, AlterTableType.DROP_CONSTRAINT);
        return null;
    }

    @Override
    public Void visitAlterByRename(AlterByRenameContext ctx) {
        builder.enterObjName(NameType.NEW_TABLE);
        dmVisitChildren(ctx.tableName());
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitAlterByConvertCharset(AlterByConvertCharsetContext ctx) {
        MyTableDomain myTableDomain = new MyTableDomain();
        myTableDomain.setAuditKind(SecQueryKind.ALTER);
        myTableDomain.setSqlType(SecQueryType.ALTER_TABLE);

        myTableDomain.setCharacterSet(ctx.charsetName().getText());
        if (ctx.collationName() != null) {
            myTableDomain.setCollate(ctx.collationName().getText());
        }
        builder.handleDomain(myTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitDropDatabase(DropDatabaseContext ctx) {
        builder.handleDropSchema(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitDropIndex(DropIndexContext ctx) {
        builder.enterDropIndex();
        dmVisitChildren(ctx);
        builder.exitDropIndex();
        return null;
    }

    @Override
    public Void visitDropTable(DropTableContext ctx) {
        boolean ifExists = ctx.ifExists() != null;
        for (TableNameContext child : ctx.tables().tableName()) {
            builder.handleDropTable(() -> {
                if (ifExists) {
                    builder.addAttr(CommonAttribute.IF_EXISTS, true);
                }
                child.accept(this);
            });
        }
        return null;
    }

    @Override
    public Void visitRenameTableClause(RenameTableClauseContext ctx) {
        builder.enterRename(TargetType.Table);

        builder.enterObjName(NameType.TABLE);
        dmVisitChildren(ctx.tableName(0));
        builder.exitObjName();

        builder.enterObjName(NameType.NEW_TABLE);
        dmVisitChildren(ctx.tableName(1));
        builder.exitObjName();

        builder.exitRename();
        return null;
    }

    @Override
    public Void visitTruncateTable(TruncateTableContext ctx) {
        MyTableDomain myTableDomain = new MyTableDomain();
        myTableDomain.setAuditKind(SecQueryKind.DML);
        myTableDomain.setSqlType(SecQueryType.TRUNCATE);

        List<String> names = new ArrayList<>();
        for (UidContext uid : ctx.tableName().fullId().uid()) {
            names.add(getName(uid));
        }
        if (names.size() == 1) {
            myTableDomain.setTable(names.get(0));
        } else if (names.size() == 2) {
            myTableDomain.setSchema(names.get(0));
            myTableDomain.setTable(names.get(1));
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx.tableName()));
        }

        builder.addDomain(myTableDomain);
        return null;
    }

    @Override
    public Void visitCallStatement(CallStatementContext ctx) {
        builder.handleCall(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        builder.handleInsert(() -> {
            dmVisitChildren(ctx);
            if (ctx.duplicatedFirst != null) {
                builder.addAttr(CommonAttribute.INSERT_CONFLICT, RdbInsertConflictStrategy.UPDATE);
            }
        });
        return null;
    }

    @Override
    public Void visitUnionSelect(UnionSelectContext ctx) {
        builder.enterSelectDomain();
        builder.addAttr(CommonAttribute.UNION, true);
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitUnionParenthesisSelect(UnionParenthesisSelectContext ctx) {
        builder.enterSelectDomain();
        builder.addAttr(CommonAttribute.UNION, true);
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitCommentInsertValue(CommentInsertValueContext ctx) {
        if (ctx.expressionsWithDefaults() != null && ctx.expressionsWithDefaults().size() > 1) {
            builder.addAttr(CommonAttribute.MULTI_VALUE, true);
        }

        builder.handleValues(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitUpdatedElement(UpdatedElementContext ctx) {
        builder.handleUpdateColumn(() -> {
            ctx.fullColumnName().accept(this);
        });

        if (ctx.expression() != null) {
            builder.enterSetColumnValue();
            ctx.expression().accept(this);
            builder.exitSetColumnValue();
        }
        return null;
    }

    @Override
    public Void visitTableOptionRowFormat(TableOptionRowFormatContext ctx) {
        return null;
    }

    @Override
    public Void visitSubqueryComparasionPredicate(SubqueryComparasionPredicateContext ctx) {
        ctx.predicate().accept(this);

        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.quantifier.getText()), DomainSource.OBJ_NAME);

            builder.handleFunctionArgs(() -> {
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.selectStatement()));
                ctx.selectStatement().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitLockClause(LockClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitSingleDeleteStatement(SingleDeleteStatementContext ctx) {
        builder.handleDelete(() -> {
            dmVisitChildren(ctx);
            if (ctx.ignore != null) {
                builder.addAttr(CommonAttribute.IGNORE, true);
            }
            if (ctx.limit != null) {
                builder.addAttr(CommonAttribute.LIMIT, true);
            }
        });
        return null;
    }

    @Override
    public Void visitFullSearchPredicate(FullSearchPredicateContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        return null;
    }

    @Override
    public Void visitSingleUpdateStatement(SingleUpdateStatementContext ctx) {
        builder.handleUpdate(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitOrderByClause(OrderByClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitIgnore_(Ignore_Context ctx) {
        builder.addAttr(CommonAttribute.IGNORE, true);
        return null;
    }

    @Override
    public Void visitInnerJoin(InnerJoinContext ctx) {
        visit(ctx.getChild(0));
        visit(ctx.getChild(1));
        return null;
    }

    @Override
    public Void visitOuterJoin(OuterJoinContext ctx) {
        visit(ctx.getChild(0));
        visit(ctx.getChild(1));
        return null;
    }

    @Override
    public Void visitNaturalJoin(NaturalJoinContext ctx) {
        visit(ctx.getChild(0));
        visit(ctx.getChild(1));
        return null;
    }

    @Override
    public Void visitInnerJoinType(InnerJoinTypeContext ctx) {
        builder.enterJoin(ctx.getText());
        builder.exitJoin();
        return null;
    }

    @Override
    public Void visitOuterJoinType(OuterJoinTypeContext ctx) {
        builder.enterJoin(ctx.getText());
        builder.exitJoin();
        return null;
    }

    @Override
    public Void visitNaturalJoinType(NaturalJoinTypeContext ctx) {
        builder.enterJoin(ctx.getText());
        builder.exitJoin();
        return null;
    }

    @Override
    public Void visitAtomTableItem(AtomTableItemContext ctx) {
        builder.handleSelectTable(() -> {
            ctx.tableName().accept(this);
            if (ctx.aliasName() != null) {
                ctx.aliasName().accept(this);
            }
        });
        return null;
    }

    @Override
    public Void visitQueryExpressionNointo(QueryExpressionNointoContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitQuerySpecificationNointo(QuerySpecificationNointoContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitSelectStarElement(SelectStarElementContext ctx) {
        builder.handleBuildSelectItem(() -> {
            RdbColumnDomain rdbColumnDomain = new RdbColumnDomain();
            rdbColumnDomain.setTable(getName(ctx.uid()));
            rdbColumnDomain.setColumn("*");
            builder.handleDomain(rdbColumnDomain, DomainSource.COLUMN);
        });
        return null;
    }

    @Override
    public Void visitLimitClause(LimitClauseContext ctx) {
        builder.addAttr(CommonAttribute.LIMIT, true);
        return null;
    }

    @Override
    public Void visitWithSelectExpr(WithSelectExprContext ctx) {
        builder.handleWithSelect(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitLimitClauseAtom(LimitClauseAtomContext ctx) {
        return null;
    }

    @Override
    public Void visitCreateUser(CreateUserContext ctx) {
        //        builder.enterCreateUser();
        //        dmVisitChildren(ctx);
        //        builder.exitCreateUser();
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.CREATE_USER);
        rdbResourceDomain.setAuditKind(SecQueryKind.CREATE);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitDropUser(DropUserContext ctx) {
        builder.enterDropUser();
        dmVisitChildren(ctx);
        builder.exitDropUser();
        return null;
    }

    @Override
    public Void visitDropRole(DropRoleContext ctx) {
        builder.enterDropRole();
        dmVisitChildren(ctx);
        builder.exitDropRole();
        return null;
    }

    @Override
    public Void visitGrantStatement(GrantStatementContext ctx) {
        builder.enterGrant();
        dmVisitChildren(ctx);
        for (UserAuthOptionContext userAuthOptionContext : ctx.userAuthOption()) {
            for (ParseTree child : userAuthOptionContext.children) {
                if (child instanceof TerminalNodeImpl) {
                    if (((TerminalNodeImpl) child).getSymbol().getType() == PASSWORD) {
                        throw new UnsupportedOperationException("not support grant with create user");
                    }
                }
            }
        }
        builder.exitGrant();
        return null;
    }

    @Override
    public Void visitRevokeStatement(RevokeStatementContext ctx) {
        builder.enterRevoke();

        for (UserNameContext context : ctx.userName()) {
            context.accept(this);
        }
        builder.exitRevoke();
        return null;
    }

    @Override
    public Void visitStringAuthOption(StringAuthOptionContext ctx) {
        String password = ctx.password.getText();
        if (password.startsWith("'")) {
            password = password.substring(1, password.length() - 1);
        }
        builder.addAttr(CommonAttribute.PASSWORD, password);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitModuleAuthOption(ModuleAuthOptionContext ctx) {
        //todo
        return null;
    }

    @Override
    public Void visitAlterUserMysqlV57(AlterUserMysqlV57Context ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.ALTER_USER);
        rdbResourceDomain.setAuditKind(SecQueryKind.ADMIN);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitAlterUserMysqlV56(AlterUserMysqlV56Context ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.ALTER_USER);
        rdbResourceDomain.setAuditKind(SecQueryKind.ADMIN);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitGrantProxy(GrantProxyContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.GRANT);
        rdbResourceDomain.setAuditKind(SecQueryKind.ADMIN);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitRevokeProxy(RevokeProxyContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.REVOKE);
        rdbResourceDomain.setAuditKind(SecQueryKind.ADMIN);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitRenameUser(RenameUserContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.RENAME_USER);
        rdbResourceDomain.setAuditKind(SecQueryKind.ADMIN);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitSetVariable(SetVariableContext ctx) {
        List<VariableClauseContext> configKeys = ctx.variableClause();
        for (VariableClauseContext configKey : configKeys) {
            MyScopeType scopeType;
            String keyName;
            if (configKey.GLOBAL_ID() != null) {
                scopeType = MyScopeType.GLOBAL;
                keyName = configKey.GLOBAL_ID().getText().substring(2);
            } else if (configKey.GLOBAL() != null) {
                scopeType = MyScopeType.GLOBAL;
                keyName = configKey.uid().getText();
            } else if (configKey.PERSIST() != null) {
                scopeType = MyScopeType.GLOBAL;
                keyName = configKey.uid().getText();
            } else if (configKey.LOCAL_ID() != null) {
                scopeType = MyScopeType.LOCAL;
                keyName = configKey.LOCAL_ID().getText().substring(1);
            } else if (configKey.LOCAL() != null) {
                scopeType = MyScopeType.LOCAL;
                keyName = configKey.uid().getText();
            } else if (configKey.SESSION() != null) {
                scopeType = MyScopeType.SESSION;
                keyName = configKey.uid().getText();
            } else {
                throw new UnsupportedOperationException("unsupported SQL: " + this.getText(configKey));
            }

            MyConfigDomain domain = new MyConfigDomain(keyName, scopeType);
            domain.setSqlType(SecQueryType.CONFIG_WRITE);
            domain.setAuditKind(SecQueryKind.OTHER);
            builder.addDomain(domain);
        }

        return null;
    }

    @Override
    public Void visitSetPassword(SetPasswordContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.ALTER_USER);
        rdbResourceDomain.setAuditKind(SecQueryKind.ALTER);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowMasterLogs(ShowMasterLogsContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.BINARY_LOGS);
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowSlaveStatus(ShowSlaveStatusContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.SALVE_STATUS);
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitResetReplica(ResetReplicaContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.RESET);
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitResetSlave(ResetSlaveContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.RESET);
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitReplicationStatement(ReplicationStatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitLoadIndexIntoCache(LoadIndexIntoCacheContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.LOAD_INDEX_INTO_CACHE);
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setTarget(TargetType.Index);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitKillStatement(KillStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.KILL);
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitPurgeBinaryLogs(PurgeBinaryLogsContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.PURGE);
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitResetMaster(ResetMasterContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.RESET);
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowReplicaStatus(ShowReplicaStatusContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.REPLICA_STATUS);
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowCharset(ShowCharsetContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.CHARACTER_SET);
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowLogEvents(ShowLogEventsContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        if (ctx.logFormat.getType() == BINLOG) {
            myShowDomain.setShowType(MyShowType.BINLOG_EVENTS);
        } else {
            myShowDomain.setShowType(MyShowType.RELAYLOG_EVENTS);
        }

        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowObjectFilter(ShowObjectFilterContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        ParseTree child = ctx.showCommonEntity().getChild(0);
        TerminalNodeImpl node = (TerminalNodeImpl) child;
        int type = node.getSymbol().getType();
        if (type == DATABASES || type == SCHEMAS) {
            myShowDomain.setShowType(MyShowType.DATABASES);
            myShowDomain.setTarget(TargetType.Schema);
        } else if (type == CHARACTER) {
            myShowDomain.setShowType(MyShowType.CHARACTER_SET);
            myShowDomain.setTarget(TargetType.Environment);
        } else if (type == PROCEDURE) {
            myShowDomain.setShowType(MyShowType.PROCEDURE_STATUS);
            myShowDomain.setTarget(TargetType.Procedure);
        } else if (type == STATUS) {
            myShowDomain.setShowType(MyShowType.STATUS);
            myShowDomain.setTarget(TargetType.Environment);
        } else if (type == FUNCTION) {
            myShowDomain.setShowType(MyShowType.FUNCTION_STATUS);
            myShowDomain.setTarget(TargetType.Function);
        } else if (type == COLLATION) {
            myShowDomain.setShowType(MyShowType.COLLATION);
            myShowDomain.setTarget(TargetType.Environment);
        } else if (type == VARIABLES) {
            myShowDomain.setShowType(MyShowType.VARIABLES);
            myShowDomain.setTarget(TargetType.Environment);
        } else if (type == GLOBAL || type == SESSION) {
            if (((TerminalNodeImpl) ctx.showCommonEntity().getChild(1)).getSymbol().getType() == VARIABLES) {
                myShowDomain.setShowType(MyShowType.VARIABLES);
                myShowDomain.setTarget(TargetType.Environment);
            } else {
                myShowDomain.setShowType(MyShowType.STATUS);
                myShowDomain.setTarget(TargetType.Environment);
            }
        } else {
            throw new UnsupportedOperationException("not support:" + node.getText());
        }
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowColumns(ShowColumnsContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        List<String> nameList = new ArrayList<>();
        for (ParseTree child : ctx.tableName().fullId().children) {
            if (child instanceof UidContext) {
                String text = getName((ParserRuleContext) child);
                nameList.add(text);
            }
        }
        if (nameList.size() == 2) {
            myShowDomain.setSchema(nameList.get(0));
            myShowDomain.setTable(nameList.get(1));
        } else {
            myShowDomain.setTable(nameList.get(0));
        }

        String text = getName(ctx.uid());
        if (text != null) {
            myShowDomain.setSchema(text);
        }
        myShowDomain.setShowType(MyShowType.COLUMNS);
        myShowDomain.setTarget(TargetType.Column);
        builder.addDomain(myShowDomain);
        return null;
    }

    private String getName(ParserRuleContext ctx) {
        if (ctx == null) {
            return null;
        }
        String text = this.getText(ctx);
        if (text.startsWith("`")) {
            text = text.substring(1, text.length() - 1);
        }
        return text;
    }

    @Override
    public Void visitShowTables(ShowTablesContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.TABLES);
        myShowDomain.setTarget(TargetType.Table);
        String text = getName(ctx.uid(0));
        myShowDomain.setSchema(text);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowCreateDb(ShowCreateDbContext ctx) {

        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.CREATE_DATABASE);
        myShowDomain.setTarget(TargetType.Schema);
        String text = getName(ctx.uid());
        myShowDomain.setSchema(text);

        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitReplaceStatement(ReplaceStatementContext ctx) {
        builder.handleReplace(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitMultipleUpdateStatement(MultipleUpdateStatementContext ctx) {
        builder.handleUpdate(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitShowCreateFullIdObject(ShowCreateFullIdObjectContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        List<String> nameList = new ArrayList<>();
        for (ParseTree child : ctx.fullId().children) {
            if (child instanceof UidContext) {
                String text = getName((ParserRuleContext) child);
                nameList.add(text);
            }
        }
        String objName = nameList.get(0);
        if (nameList.size() == 2) {
            myShowDomain.setSchema(nameList.get(0));
            objName = nameList.get(1);
        }
        if (ctx.namedEntity.getType() == FUNCTION) {
            myShowDomain.setShowType(MyShowType.CREATE_FUNCTION);
            myShowDomain.setTarget(TargetType.Function);
            myShowDomain.setFunc(objName);
        } else if (ctx.namedEntity.getType() == PROCEDURE) {
            myShowDomain.setShowType(MyShowType.CREATE_PROCEDURE);
            myShowDomain.setTarget(TargetType.Procedure);
            myShowDomain.setProc(objName);
        } else if (ctx.namedEntity.getType() == TABLE) {
            myShowDomain.setShowType(MyShowType.CREATE_TABLE);
            myShowDomain.setTarget(TargetType.Table);
            myShowDomain.setTable(objName);
        } else if (ctx.namedEntity.getType() == VIEW) {
            myShowDomain.setShowType(MyShowType.CREATE_VIEW);
            myShowDomain.setTarget(TargetType.View);
            myShowDomain.setView(objName);
        } else if (ctx.namedEntity.getType() == EVENT) {
            myShowDomain.setShowType(MyShowType.CREATE_EVENT);
            myShowDomain.setTarget(TargetType.Event);
            myShowDomain.setEvent(objName);
        } else if (ctx.namedEntity.getType() == TRIGGER) {
            myShowDomain.setShowType(MyShowType.CREATE_TRIGGER);
            myShowDomain.setTarget(TargetType.Trigger);
            myShowDomain.setTrigger(objName);
        } else {
            throw new UnsupportedOperationException("Not Support:" + ctx.namedEntity.getText());
        }
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowEngine(ShowEngineContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.ENGINE);
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowEngines(ShowEnginesContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.ENGINES);
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowPrivileges(ShowPrivilegesContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.PRIVILEGES);
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowPlugins(ShowPluginsContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.PLUGINS);
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowErrors(ShowErrorsContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        if (ctx.errorFormat.getType() == ERRORS) {
            myShowDomain.setShowType(MyShowType.ERRORS);
        } else {
            myShowDomain.setShowType(MyShowType.WARNINGS);
        }
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowCountErrors(ShowCountErrorsContext ctx) {

        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        if (ctx.errorFormat.getType() == ERRORS) {
            myShowDomain.setShowType(MyShowType.ERRORS);
        } else {
            myShowDomain.setShowType(MyShowType.WARNINGS);
        }
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowSchemaFilter(ShowSchemaFilterContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        ParseTree child = ctx.showSchemaEntity().getChild(0);
        int type = ((TerminalNodeImpl) child).getSymbol().getType();
        if (type == EVENTS) {
            myShowDomain.setShowType(MyShowType.EVENTS);
            myShowDomain.setTarget(TargetType.Event);
        } else if (type == TABLES || type == FULL) {
            myShowDomain.setShowType(MyShowType.TABLES);
            myShowDomain.setTarget(TargetType.Table);
        } else if (type == TABLE) {
            if (ctx.uid() != null) {
                String text = getName(ctx.uid());
                myShowDomain.setSchema(text);
            }
            myShowDomain.setShowType(MyShowType.TABLE_STATUS);
            myShowDomain.setTarget(TargetType.Table);
        } else if (type == TRIGGERS) {
            myShowDomain.setShowType(MyShowType.TRIGGERS);
            myShowDomain.setTarget(TargetType.Trigger);
        }

        if (ctx.uid() != null) {
            String text = getName(ctx.uid());
            myShowDomain.setSchema(text);
        }

        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowRoutine(ShowRoutineContext ctx) {

        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        List<String> nameList = new ArrayList<>();
        for (ParseTree child : ctx.fullId().children) {
            if (child instanceof UidContext) {
                String text = getName((ParserRuleContext) child);
                nameList.add(text);
            }
        }
        String objName;
        if (nameList.size() == 2) {
            myShowDomain.setSchema(nameList.get(0));
            objName = nameList.get(1);
        } else {
            objName = nameList.get(0);
        }

        if (ctx.routine.getType() == PROCEDURE) {
            myShowDomain.setShowType(MyShowType.PROCEDURE_CODE);
            myShowDomain.setTarget(TargetType.Procedure);
            myShowDomain.setProc(objName);
        } else {
            myShowDomain.setShowType(MyShowType.FUNCTION_CODE);
            myShowDomain.setTarget(TargetType.Function);
            myShowDomain.setFunc(objName);
        }
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowGrants(ShowGrantsContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.GRANTS);
        myShowDomain.setTarget(TargetType.UserOrRole);
        if (ctx.userName() != null) {
            String text = this.getText(ctx.userName());
            myShowDomain.setUserOrRole(text);
        } else if (ctx.children.size() > 2) {
            myShowDomain.setUserOrRole("CURRENT_USER");
        }
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowIndexes(ShowIndexesContext ctx) {

        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        List<String> nameList = new ArrayList<>();
        for (ParseTree child : ctx.tableName().fullId().children) {
            if (child instanceof UidContext) {
                String text = getName((ParserRuleContext) child);
                nameList.add(text);
            }
        }
        if (nameList.size() == 2) {
            myShowDomain.setSchema(nameList.get(0));
            myShowDomain.setTable(nameList.get(1));
        } else {
            myShowDomain.setTable(nameList.get(0));
        }
        if (ctx.uid() != null) {
            String text = getName(ctx.uid());
            myShowDomain.setSchema(text);
        }
        myShowDomain.setShowType(MyShowType.INDEX);
        myShowDomain.setTarget(TargetType.Index);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowOpenTables(ShowOpenTablesContext ctx) {

        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.OPEN_TABLES);
        myShowDomain.setTarget(TargetType.Table);
        if (ctx.uid() != null) {
            String text = getName(ctx.uid());
            myShowDomain.setSchema(text);
        }
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowProfile(ShowProfileContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.PROFILE);
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowProcessList(ShowProcessListContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.PROCESSLIST);
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowProfiles(ShowProfilesContext ctx) {

        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.PROFILES);
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitFlushStatement(FlushStatementContext ctx) {
        for (FlushOptionContext flushOptionContext : ctx.flushOption()) {
            MyFlushDomain myFlushDomain = new MyFlushDomain();
            myFlushDomain.setSqlType(SecQueryType.MYSQL_FLUSH);
            myFlushDomain.setAuditKind(SecQueryKind.OTHER);

            String text = this.getText(flushOptionContext);
            myFlushDomain.setFlushType(MyFlushType.valueOfString(text));
            builder.addDomain(myFlushDomain);
        }

        return null;
    }

    @Override
    public Void visitSimpleDescribeStatement(SimpleDescribeStatementContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.COLUMNS);
        myShowDomain.setTarget(TargetType.Column);
        List<String> nameList = new ArrayList<>();
        for (ParseTree child : ctx.tableName().fullId().children) {
            if (child instanceof UidContext) {
                String text = getName((ParserRuleContext) child);
                nameList.add(text);
            }
        }
        if (nameList.size() == 1) {
            myShowDomain.setTable(nameList.get(0));
        } else {
            myShowDomain.setTable(nameList.get(1));
            myShowDomain.setSchema(nameList.get(0));
        }

        builder.addDomain(myShowDomain);
        return null;
    }

    @Override
    public Void visitShowStatus(ShowStatusContext ctx) {
        MyShowDomain myShowDomain = new MyShowDomain();
        myShowDomain.setSqlType(SecQueryType.SHOW);
        myShowDomain.setAuditKind(SecQueryKind.QUERY);
        myShowDomain.setShowType(MyShowType.MASTER_STATUS);
        myShowDomain.setTarget(TargetType.Environment);
        builder.addDomain(myShowDomain);
        return null;
    }

    //
    @Override
    public Void visitUseStatement(UseStatementContext ctx) {
        return null;
    }

    @Override
    public Void visitRoleName(RoleNameContext ctx) {
        builder.enterObjName();
        builder.addAttr(CommonAttribute.VALUE, getName(ctx));
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitIndexColumnName(IndexColumnNameContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitUserName(UserNameContext ctx) {
        builder.enterObjName();
        String text = ctx.user.getText();
        if (text.startsWith("'")) {
            text = text.substring(1, text.length() - 1);
        }
        String host;
        if (ctx.host != null) {
            host = ctx.host.getText().substring(1);
            if (host.startsWith("'")) {
                host = host.substring(1, host.length() - 1);
            }
        } else {
            host = "%";
        }
        builder.addAttr(CommonAttribute.VALUE, text);
        builder.addAttr(CommonAttribute.VALUE, host);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitMysqlVariable(MysqlVariableContext ctx) {
        TerminalNode globalID = ctx.GLOBAL_ID();
        TerminalNode localID = ctx.LOCAL_ID();

        if (globalID != null) {
            builder.handleDomain(new MyVariableDomain(globalID.getText().substring(2), MyScopeType.GLOBAL), DomainSource.VARIABLE);
        } else if (localID != null) {
            builder.handleDomain(new MyVariableDomain(localID.getText().substring(1), MyScopeType.LOCAL), DomainSource.VARIABLE);
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }

        return null;
    }

    @Override
    public Void visitCharsetName(CharsetNameContext ctx) {
        builder.addAttr(MyAttribute.CHARACTER_SET, this.getText(ctx));
        return null;
    }

    @Override
    public Void visitCollationName(CollationNameContext ctx) {
        builder.addAttr(MyAttribute.COLLATE, this.getText(ctx));
        return null;
    }

    @Override
    public Void visitStringLiteral(StringLiteralContext ctx) {
        String text = this.getText(ctx);
        if (text.startsWith("'")) {
            text = text.substring(1, text.length() - 1);
        }
        builder.handleDomain(new RdbConstantDomain(text), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitStringDataType(StringDataTypeContext ctx) {
        String type = ctx.typeName.getText();
        ColumnTypeDomain columnTypeDomain;
        if (ctx.lengthOneDimension() != null) {
            columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), ctx.lengthOneDimension().decimalLiteral().getText());
        } else {
            columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), null);
        }
        builder.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);

        return null;
    }

    @Override
    public Void visitNationalStringDataType(NationalStringDataTypeContext ctx) {

        String type = ctx.typeName.getText();
        ColumnTypeDomain columnTypeDomain;
        if (ctx.lengthOneDimension() != null) {
            columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), ctx.lengthOneDimension().decimalLiteral().getText());
        } else {
            columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), null);
        }
        builder.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);

        return null;
    }

    @Override
    public Void visitNationalVaryingStringDataType(NationalVaryingStringDataTypeContext ctx) {
        String type = ctx.typeName.getText();
        ColumnTypeDomain columnTypeDomain;
        if (ctx.lengthOneDimension() != null) {
            columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), ctx.lengthOneDimension().decimalLiteral().getText());
        } else {
            columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), null);
        }
        builder.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);

        return null;
    }

    @Override
    public Void visitDimensionDataType(DimensionDataTypeContext ctx) {
        String type = ctx.typeName.getText();
        ColumnTypeDomain columnTypeDomain;
        if (ctx.lengthOneDimension() != null) {
            columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), ctx.lengthOneDimension().decimalLiteral().getText());
        } else if (ctx.lengthTwoDimension() != null) {
            columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), ctx.lengthTwoDimension().decimalLiteral(0).getText());
        } else if (ctx.lengthTwoOptionalDimension() != null) {
            columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), ctx.lengthTwoOptionalDimension().decimalLiteral(0).getText());
        } else {
            columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), null);
        }
        builder.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);

        if (ctx.UNSIGNED() != null) {
            builder.addAttr(MyAttribute.UNSIGNED, true);
        }
        if (ctx.ZEROFILL() != null) {
            builder.addAttr(MyAttribute.ZEROFILL, true);
        }

        return null;
    }

    @Override
    public Void visitSimpleDataType(SimpleDataTypeContext ctx) {
        String type = ctx.typeName.getText();
        ColumnTypeDomain columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), null);
        builder.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);
        return null;
    }

    @Override
    public Void visitCollectionDataType(CollectionDataTypeContext ctx) {
        String type = ctx.typeName.getText();
        ColumnTypeDomain columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), null);
        builder.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);
        return null;
    }

    @Override
    public Void visitSpatialDataType(SpatialDataTypeContext ctx) {
        String type = ctx.typeName.getText();
        ColumnTypeDomain columnTypeDomain = new ColumnTypeDomain(type, this.getText(ctx), null);
        builder.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);
        return null;
    }

    @Override
    public Void visitUidList(UidListContext ctx) {
        builder.handleInsertColumn(() -> {
            dmVisitChildren(ctx);
        });

        return null;
    }

    @Override
    public Void visitIndexColumnNames(IndexColumnNamesContext ctx) {
        builder.handleColumnList(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitIfExists(IfExistsContext ctx) {
        builder.addAttr(CommonAttribute.IF_EXISTS, true);
        return null;
    }

    @Override
    public Void visitIfNotExists(IfNotExistsContext ctx) {
        builder.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
        return null;
    }

    @Override
    public Void visitDataTypeFunctionCall(DataTypeFunctionCallContext ctx) {
        builder.handleCall(() -> {
            builder.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());
            builder.handleFunctionArgs(() -> {
                for (ParseTree child : ctx.children) {
                    if (child instanceof ExpressionContext) {
                        builder.addAttr(CommonAttribute.VALUE, this.getText((ParserRuleContext) child));
                        child.accept(this);
                        break;
                    }
                }
            });
        });
        return null;
    }

    @Override
    public Void visitValuesFunctionCall(ValuesFunctionCallContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(Collections.singletonList("values"), NameType.FUNCTION), DomainSource.OBJ_NAME);

            builder.handleFunctionArgs(() -> {
                builder.addAttr(CommonAttribute.VALUE, this.getText(ctx.fullColumnName()));
                ctx.fullColumnName().accept(this);
            });
        });
        return null;

    }

    @Override
    public Void visitSubstrFunctionCall(SubstrFunctionCallContext ctx) {
        builder.handleCall(() -> {
            builder.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());

            builder.handleFunctionArgs(() -> {
                for (ParseTree child : ctx.children) {
                    if (child instanceof ParserRuleContext) {
                        builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText((ParserRuleContext) child));
                        child.accept(this);
                    }
                }
            });
        });
        return null;
    }

    @Override
    public Void visitCaseFunctionCall(CaseFunctionCallContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.CASE().getText()), DomainSource.OBJ_NAME);

            builder.handleFunctionArgs(() -> {
                if (ctx.expression() != null) {
                    ctx.expression().accept(this);
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.expression()));
                }
                for (CaseFuncAlternativeContext caseFuncAlternativeContext : ctx.caseFuncAlternative()) {
                    caseFuncAlternativeContext.accept(this);
                }
                if (ctx.elseArg != null) {
                    ctx.elseArg.accept(this);
                }
            });
        });

        return null;
    }

    @Override
    public Void visitCaseFuncAlternative(CaseFuncAlternativeContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExtractFunctionCall(ExtractFunctionCallContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.EXTRACT().getText()), DomainSource.OBJ_NAME);

            builder.handleFunctionArgs(() -> {
                ParserRuleContext arg = ctx.sourceString == null ? ctx.sourceExpression : ctx.sourceString;
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(arg));
                arg.accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitAggregateFunctionCall(AggregateFunctionCallContext ctx) {
        ctx.aggregateFunction().accept(this);
        return null;
    }

    @Override
    public Void visitNonAggregateFunctionCall(NonAggregateFunctionCallContext ctx) {
        ctx.nonAggregateFunction().accept(this);
        return null;
    }

    @Override
    public Void visitAggregateFunction(AggregateFunctionContext ctx) {
        builder.handleCall(() -> {
            builder.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());
            for (ParseTree child : ctx.children) {
                if (child instanceof FunctionArgsContext) {
                    child.accept(this);
                    break;
                } else if (child instanceof FunctionArgContext) {
                    builder.handleFunctionArgs(() -> {
                        child.accept(this);
                    });
                    break;
                } else if (child.getText().equals("*")) {
                    builder.handleFunctionArgs(() -> {
                        builder.addAttr(CommonAttribute.VALUE, "*");
                        builder.handleDomain(new RdbConstantDomain("*"), DomainSource.CONSTANT);
                    });
                    break;
                }
            }
        });
        return null;
    }

    @Override
    public Void visitNonAggregateFunction(NonAggregateFunctionContext ctx) {
        builder.handleCall(() -> {
            builder.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());
            if (ctx.functionArgs() != null) {
                ctx.functionArgs().accept(this);
            }
        });
        return null;
    }

    @Override
    public Void visitGroupClause(GroupClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitHavingClause(HavingClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitFunctionArg(FunctionArgContext ctx) {
        builder.addAttr(CommonAttribute.VALUE, this.getText(ctx));
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNotExpression(NotExpressionContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSpecialTimeCall(SpecialTimeCallContext ctx) {
        builder.handleCall(() -> {
            builder.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());

            builder.handleFunctionArgs(() -> {
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.stringLiteral()));
                ctx.stringLiteral().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitSubqueryTableItem(SubqueryTableItemContext ctx) {
        builder.handleSelectTable(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitBetweenPredicate(BetweenPredicateContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitLikePredicate(LikePredicateContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitMysqlVariableExpressionAtom(MysqlVariableExpressionAtomContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIntervalExpressionAtom(IntervalExpressionAtomContext ctx) {
        builder.handleCall(() -> {
            builder.addAttr(CommonAttribute.VALUE, ctx.INTERVAL().getText());
            builder.handleFunctionArgs(() -> {
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.expression()));
                ctx.expression().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitExistsExpessionAtom(ExistsExpessionAtomContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSqlStatement(SqlStatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDdlStatement(DdlStatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDmlStatement(DmlStatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAdministrationStatement(AdministrationStatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUtilityStatement(UtilityStatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreateDatabaseOption(CreateDatabaseOptionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreateDefinitions(CreateDefinitionsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnDeclaration(ColumnDeclarationContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConstraintDeclaration(ConstraintDeclarationContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIndexDeclaration(IndexDeclarationContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAlterByTableOption(AlterByTableOptionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRenameTable(RenameTableContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDeleteStatement(DeleteStatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSimpleSelect(SimpleSelectContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUpdateStatement(UpdateStatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWithClause(WithClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSelectInsertValue(SelectInsertValueContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTableSourceBase(TableSourceBaseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTableSourcesItem(TableSourcesItemContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTableSourceWith(TableSourceWithContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQueryExpression(QueryExpressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUnionParenthesis(UnionParenthesisContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUnionStatement(UnionStatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSelectSpec(SelectSpecContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSimpleAuthOption(SimpleAuthOptionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPrivelegeClause(PrivelegeClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPrivilege(PrivilegeContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitGlobalPrivLevel(GlobalPrivLevelContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDefiniteSchemaPrivLevel(DefiniteSchemaPrivLevelContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDefiniteFullTablePrivLevel(DefiniteFullTablePrivLevelContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDefiniteFullTablePrivLevel2(DefiniteFullTablePrivLevel2Context ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDefiniteTablePrivLevel(DefiniteTablePrivLevelContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitShowSlaveHosts(ShowSlaveHostsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitShowAuthros(ShowAuthrosContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitShowContributors(ShowContributorsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFullId(FullIdContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUid(UidContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDottedId(DottedIdContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDecimalLiteral(DecimalLiteralContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNullNotnull(NullNotnullContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTables(TablesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpressions(ExpressionsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpressionsWithDefaults(ExpressionsWithDefaultsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpressionOrDefault(ExpressionOrDefaultContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSpecificFunctionCall(SpecificFunctionCallContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitOverClause(OverClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWindow_specification(Window_specificationContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitLogicalExpression(LogicalExpressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPredicateExpression(PredicateExpressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRegexpPredicate(RegexpPredicateContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpressionAtomPredicate(ExpressionAtomPredicateContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSubqueryExpessionAtom(SubqueryExpessionAtomContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNestedExpressionAtom(NestedExpressionAtomContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitMathExpressionAtom(MathExpressionAtomContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConstantExpressionAtom(ConstantExpressionAtomContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFunctionCallExpressionAtom(FunctionCallExpressionAtomContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFullColumnNameExpressionAtom(FullColumnNameExpressionAtomContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUnaryExpressionAtom(UnaryExpressionAtomContext ctx) {
        ctx.expressionAtom().accept(this);
        return null;
    }

    @Override
    public Void visitBitOperator(BitOperatorContext ctx) {
        return null;
    }

    @Override
    public Void visitBitExpressionAtom(BitExpressionAtomContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTransactionStatement(TransactionStatementContext ctx) {
        RdbResourceDomain domain = new RdbResourceDomain();
        domain.setAuditKind(SecQueryKind.QUERY);
        domain.setSqlType(SecQueryType.TRANSACTION);
        domain.setNeedSupply(true);
        domain.setTarget(TargetType.Unknown);
        builder.addDomain(domain);
        return null;
    }

    @Override
    public Void visitPreparedStatement(PreparedStatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPrepareStatement(PrepareStatementContext ctx) {
        RdbResourceDomain domain = new RdbResourceDomain();
        domain.setAuditKind(SecQueryKind.OTHER);
        domain.setSqlType(SecQueryType.PREPARE);
        domain.setNeedSupply(false);
        domain.setTarget(TargetType.PrepareStatement);
        builder.addDomain(domain);
        return null;
    }

    @Override
    public Void visitExecuteStatement(ExecuteStatementContext ctx) {
        RdbResourceDomain domain = new RdbResourceDomain();
        domain.setAuditKind(SecQueryKind.OTHER);
        domain.setSqlType(SecQueryType.EXECUTE);
        domain.setNeedSupply(false);
        domain.setTarget(TargetType.PrepareStatement);
        builder.addDomain(domain);
        return null;
    }

    @Override
    public Void visitDeallocatePrepare(DeallocatePrepareContext ctx) {
        RdbResourceDomain domain = new RdbResourceDomain();
        domain.setAuditKind(SecQueryKind.OTHER);
        domain.setSqlType(SecQueryType.DEALLOCATE);
        domain.setNeedSupply(false);
        domain.setTarget(TargetType.PrepareStatement);
        builder.addDomain(domain);
        return null;
    }

    @Override
    public Void visitComparisonOperator(ComparisonOperatorContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitLogicalOperator(LogicalOperatorContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitMathOperator(MathOperatorContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitKeywordsCanBeId(KeywordsCanBeIdContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFunctionNameBase(FunctionNameBaseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }
}
