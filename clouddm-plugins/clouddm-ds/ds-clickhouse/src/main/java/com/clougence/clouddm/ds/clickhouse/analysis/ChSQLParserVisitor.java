package com.clougence.clouddm.ds.clickhouse.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.clickhouse.analysis.builder.ChBuilderFactory;
import com.clougence.clouddm.ds.clickhouse.analysis.builder.enums.ChAttribute;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChColumnDomain;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ConstraintTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.clickhouse.parser.antlr.ClickHouseParser;
import com.clougence.clouddm.ds.clickhouse.parser.antlr.ClickHouseParserBaseVisitor;
import com.clougence.schema.umi.struts.UmiTypes;

public class ChSQLParserVisitor extends ClickHouseParserBaseVisitor<Void> {

    private final ChBuilderFactory builder;
    private final Parser           parser;

    public ChSQLParserVisitor(ChBuilderFactory domainBuilder, Parser parser){
        this.builder = domainBuilder;
        this.parser = parser;
    }

    private String getText(ParserRuleContext context) {
        return parser.getTokenStream().getText(context);
    }

    private String getString(String text) {
        if (text.startsWith("'")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    @Override
    public Void visitChildren(RuleNode node) {
        if (node instanceof ParserRuleContext) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText((ParserRuleContext) node));
        }
        throw new UnsupportedOperationException("unsupported SQL: " + node.getText());
    }

    private void visitIfExists(ParserRuleContext ctx) {
        if (ctx != null) {
            ctx.accept(this);
        }
    }

    public void dmVisitChildren(RuleNode node) {
        int n = node.getChildCount();

        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            c.accept(this);
        }
    }

    //

    @Override
    public Void visitSelectUnionStmt(ClickHouseParser.SelectUnionStmtContext ctx) {
        builder.handleSelectDomain(() -> {
            visitIfExists(ctx.withClause());
            if (!ctx.UNION().isEmpty() || !ctx.EXCEPT().isEmpty() || !ctx.INTERSECT().isEmpty()) {
                builder.addAttr(CommonAttribute.UNION, true);
            }
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitColumnExprNegate(ClickHouseParser.ColumnExprNegateContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitLimitByClause(ClickHouseParser.LimitByClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitColumnExprSubstring(ClickHouseParser.ColumnExprSubstringContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(Collections.singletonList(getName(ctx.SUBSTRING().getText())), null), DomainSource.OBJ_NAME);

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
    public Void visitColumnExprList(ClickHouseParser.ColumnExprListContext ctx) {
        if (ctx.parent instanceof ClickHouseParser.SelectStmtContext) {
            for (ParseTree child : ctx.children) {
                if (child instanceof ClickHouseParser.ColumnsExprContext) {
                    builder.handleBuildSelectItem(() -> {
                        child.accept(this);
                    });
                }
            }
        } else {
            dmVisitChildren(ctx);
        }
        return null;
    }

    @Override
    public Void visitColumnExprAsterisk(ClickHouseParser.ColumnExprAsteriskContext ctx) {
        if (ctx.parent instanceof ClickHouseParser.ColumnArgExprContext) {
            builder.addAttr(CommonAttribute.VALUE, "*");
            builder.handleDomain(new RdbConstantDomain("*"), DomainSource.CONSTANT);
            return null;
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + getText((ParserRuleContext) ctx.parent.parent.parent));
        }
    }

    @Override
    public Void visitColumnExprCast(ClickHouseParser.ColumnExprCastContext ctx) {
        builder.handleCall(() -> {
            builder.handleObjName(() -> {
                builder.addAttr(CommonAttribute.VALUE, ctx.CAST().getText());
            });

            builder.handleFunctionArgs(() -> {
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.columnExpr()));
                ctx.columnExpr().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitDeleteStmt(ClickHouseParser.DeleteStmtContext ctx) {
        builder.handleDelete(() -> {
            builder.handleObjName(() -> {
                ctx.nestedIdentifier().accept(this);
            });

            visitIfExists(ctx.whereClause());
        });
        return null;
    }

    @Override
    public Void visitJoinExprCrossOp(ClickHouseParser.JoinExprCrossOpContext ctx) {
        ctx.joinExpr(0).accept(this);

        builder.enterJoin("crossjoin");
        builder.exitJoin();
        ctx.joinExpr(1).accept(this);
        return null;
    }

    @Override
    public Void visitQualifyClause(ClickHouseParser.QualifyClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitPrewhereClause(ClickHouseParser.PrewhereClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitColumnExprPrecedence2(ClickHouseParser.ColumnExprPrecedence2Context ctx) {
        String left = getText(ctx.columnExpr(0));
        String right = getText(ctx.columnExpr(1));
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
    public Void visitJoinExprOp(ClickHouseParser.JoinExprOpContext ctx) {
        ctx.joinExpr(0).accept(this);

        if (ctx.joinOp() != null) {
            builder.enterJoin(ctx.joinOp().getText() + ctx.JOIN().getText());
        } else {
            builder.enterJoin(ctx.JOIN().getText());
        }
        builder.exitJoin();
        ctx.joinExpr(1).accept(this);
        return null;
    }

    @Override
    public Void visitUseStmt(ClickHouseParser.UseStmtContext ctx) {
        return null;
    }

    @Override
    public Void visitColumnExprPrecedence1(ClickHouseParser.ColumnExprPrecedence1Context ctx) {
        String left = getText(ctx.columnExpr(0));
        String right = getText(ctx.columnExpr(1));
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
    public Void visitTableExprSubquery(ClickHouseParser.TableExprSubqueryContext ctx) {
        builder.handleSelectTable(() -> {
            if (ctx.parent instanceof ClickHouseParser.TableExprAliasContext) {
                ClickHouseParser.TableExprAliasContext parent = (ClickHouseParser.TableExprAliasContext) ctx.parent;
                if (parent.alias() != null) {
                    builder.addAttr(CommonAttribute.ALIAS, getName(getText(parent.alias())));
                } else {
                    builder.addAttr(CommonAttribute.ALIAS, getName(getText(parent.identifier())));
                }
            }
            dmVisitChildren(ctx);
        });

        return null;
    }

    @Override
    public Void visitInsertStmt(ClickHouseParser.InsertStmtContext ctx) {
        if (ctx.tableFunctionExpr() != null) {
            throw new UnsupportedOperationException("unsupported SQL: " + getText(ctx));
        }
        builder.handleInsert(() -> {
            builder.handleObjName(() -> {
                ctx.tableIdentifier().accept(this);
            });

            visitIfExists(ctx.columnsClause());
            ctx.dataClause().accept(this);
        });
        return null;
    }

    @Override
    public Void visitDataClauseValues(ClickHouseParser.DataClauseValuesContext ctx) {
        if (ctx.assignmentValues().size() > 1) {
            builder.addAttr(CommonAttribute.MULTI_VALUE, true);
        }
        builder.handleValues(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitColumnsClause(ClickHouseParser.ColumnsClauseContext ctx) {
        builder.handleInsertColumn(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitWhereClause(ClickHouseParser.WhereClauseContext ctx) {
        builder.handleWhere(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitTableExprAlias(ClickHouseParser.TableExprAliasContext ctx) {
        ctx.tableExpr().accept(this);
        return null;
    }

    @Override
    public Void visitTruncateStmt(ClickHouseParser.TruncateStmtContext ctx) {
        builder.handleTruncateTable(() -> {
            builder.handleObjName(() -> {
                ctx.tableIdentifier().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitSetStmt(ClickHouseParser.SetStmtContext ctx) {
        for (ClickHouseParser.SettingExprContext settingExprContext : ctx.settingExprList().settingExpr()) {
            RdbConfigDomain rdbConfigDomain = new RdbConfigDomain(getName(getText(settingExprContext.identifier())));
            rdbConfigDomain.setAuditKind(SecQueryKind.OTHER);
            rdbConfigDomain.setSqlType(SecQueryType.CONFIG_WRITE);

            builder.addDomain(rdbConfigDomain);
        }
        return null;
    }

    @Override
    public Void visitColumnExprPrecedence3(ClickHouseParser.ColumnExprPrecedence3Context ctx) {
        String left = getText(ctx.columnExpr(0));
        String right = getText(ctx.columnExpr(1));
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
    public Void visitLiteral(ClickHouseParser.LiteralContext ctx) {
        RdbConstantDomain rdbConstantDomain = new RdbConstantDomain(getString(getText(ctx)));
        builder.handleDomain(rdbConstantDomain, DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitLimitClause(ClickHouseParser.LimitClauseContext ctx) {
        builder.addAttr(CommonAttribute.LIMIT, true);
        return null;
    }

    @Override
    public Void visitTableElementExprPri(ClickHouseParser.TableElementExprPriContext ctx) {
        builder.handleConstraint(() -> {
            builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);

            builder.handleColumnList((() -> {
                ctx.columnExpr().accept(this);
            }));
        });
        return null;
    }

    @Override
    public Void visitPrimaryKeyClause(ClickHouseParser.PrimaryKeyClauseContext ctx) {
        builder.handleConstraint(() -> {
            builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);

            builder.handleColumnList((() -> {
                ctx.columnExpr().accept(this);
            }));
        });
        return null;
    }

    @Override
    public Void visitCreateViewStmt(ClickHouseParser.CreateViewStmtContext ctx) {
        builder.handleCreateView(() -> {
            builder.handleObjName(() -> {
                ctx.tableIdentifier().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitDropTableStmt(ClickHouseParser.DropTableStmtContext ctx) {
        if (ctx.TABLE() != null) {
            builder.handleDropTable(() -> {
                builder.handleObjName(() -> {
                    ctx.tableIdentifier().accept(this);
                });

                if (ctx.EXISTS() != null) {
                    builder.addAttr(CommonAttribute.IF_EXISTS, true);
                }
            });
        } else if (ctx.VIEW() != null) {
            builder.handleDropView(() -> {
                builder.handleObjName(() -> {
                    ctx.tableIdentifier().accept(this);
                });
            });
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + getText(ctx));
        }

        return null;
    }

    @Override
    public Void visitCreateTableStmt(ClickHouseParser.CreateTableStmtContext ctx) {
        builder.handleCreateTable(() -> {
            if (ctx.EXISTS() != null) {
                builder.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
            }

            if (ctx.TEMPORARY() != null) {
                builder.addAttr(ChAttribute.TEMPORARY, true);
            }

            builder.handleObjName(() -> {
                ctx.tableIdentifier().accept(this);
            });

            visitIfExists(ctx.tableSchemaClause());
            visitIfExists(ctx.subqueryClause());
            visitIfExists(ctx.engineClause());
        });
        return null;
    }

    @Override
    public Void visitTableColumnDfnt(ClickHouseParser.TableColumnDfntContext ctx) {
        if (ctx.nestedIdentifier().identifier().size() > 1) {
            throw new UnsupportedOperationException("unsupported SQL: " + getText(ctx));
        }
        builder.handleColumnDef(() -> {
            builder.handleObjName(() -> {
                ctx.nestedIdentifier().accept(this);
            });
            ctx.columnTypeExpr().accept(this);
            visitIfExists(ctx.tableColumnPropertyExpr());
            if (ctx.stringLiteral() != null) {
                builder.addAttr(CommonAttribute.COMMENT, getString(getText(ctx.stringLiteral())));
            }

            if (ctx.NULLABLE() != null) {
                builder.addAttr(CommonAttribute.COLUMN_ALLOW_NULL, true);
            }

        });
        return null;
    }

    @Override
    public Void visitEngineExpr(ClickHouseParser.EngineExprContext ctx) {
        builder.addAttr(ChAttribute.ENGINE, getString(getText(ctx.identifierOrNull())));
        return null;
    }

    @Override
    public Void visitColumnTypeExprSimple(ClickHouseParser.ColumnTypeExprSimpleContext ctx) {
        builder.handleColumnType(() -> {
            builder.handleDomain(new ObjNameDomain(Collections.singletonList(getName(getText(ctx.identifier()))), null), DomainSource.OBJ_NAME);
        }, getText(ctx));

        return null;
    }

    @Override
    public Void visitColumnTypeExprComplex(ClickHouseParser.ColumnTypeExprComplexContext ctx) {
        builder.handleColumnType(() -> {
            builder.handleDomain(new ObjNameDomain(Collections.singletonList(getName(getText(ctx.identifier()))), null), DomainSource.OBJ_NAME);
        }, getText(ctx));

        return null;
    }

    @Override
    public Void visitColumnTypeExprParam(ClickHouseParser.ColumnTypeExprParamContext ctx) {
        builder.handleColumnType(() -> {
            builder.handleDomain(new ObjNameDomain(Collections.singletonList(getName(getText(ctx.identifier()))), null), DomainSource.OBJ_NAME);

            for (ParseTree child : ctx.columnExprList().children) {
                child.accept(this);
            }
        }, getText(ctx));

        return null;
    }

    @Override
    public Void visitSchemaAsTableClause(ClickHouseParser.SchemaAsTableClauseContext ctx) {
        builder.handleObjName(() -> {
            ctx.tableIdentifier().accept(this);
        });
        return null;
    }

    @Override
    public Void visitTableElementExprIndex(ClickHouseParser.TableElementExprIndexContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTableIndexDfnt(ClickHouseParser.TableIndexDfntContext ctx) {
        builder.handleCreateIndex(() -> {
            builder.handleObjName(() -> {
                ctx.nestedIdentifier().accept(this);
            }, NameType.INDEX);

            builder.handleColumnList(() -> {
                ctx.columnExpr().accept(this);
            });
        });

        return null;
    }

    @Override
    public Void visitWithExprSubquery(ClickHouseParser.WithExprSubqueryContext ctx) {
        builder.handleWithSelect(() -> {
            ctx.identifier().accept(this);
            ctx.selectUnionStmt().accept(this);
        });
        return null;
    }

    @Override
    public Void visitColumnsExprColumn(ClickHouseParser.ColumnsExprColumnContext ctx) {
        if (ctx.parent.parent instanceof ClickHouseParser.SelectStmtContext) {
            if (ctx.columnExpr() instanceof ClickHouseParser.ColumnExprAliasContext) {
                ClickHouseParser.ColumnExprAliasContext alias = (ClickHouseParser.ColumnExprAliasContext) ctx.columnExpr();
                builder.addAttr(CommonAttribute.VALUE, getText(alias.columnExpr()));
            } else {
                builder.addAttr(CommonAttribute.VALUE, getText(ctx));
            }
            dmVisitChildren(ctx);
        } else {
            dmVisitChildren(ctx);
        }
        return null;
    }

    @Override
    public Void visitColumnArgExpr(ClickHouseParser.ColumnArgExprContext ctx) {
        builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx));
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnExprFunction(ClickHouseParser.ColumnExprFunctionContext ctx) {
        builder.handleCall(() -> {
            builder.handleObjName(() -> {
                ctx.identifier().accept(this);
            });

            builder.handleFunctionArgs(() -> {
                visitIfExists(ctx.columnArgList());
            });
        });
        return null;
    }

    @Override
    public Void visitHavingClause(ClickHouseParser.HavingClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitColumnExprWinFunction(ClickHouseParser.ColumnExprWinFunctionContext ctx) {
        builder.handleCall(() -> {
            builder.handleObjName(() -> {
                ctx.identifier().accept(this);
            });

            builder.handleFunctionArgs(() -> {
                visitIfExists(ctx.columnExprList());
            });
        });
        return null;
    }

    @Override
    public Void visitTableExprIdentifier(ClickHouseParser.TableExprIdentifierContext ctx) {
        builder.handleSelectTable(() -> {
            builder.handleObjName(() -> {
                dmVisitChildren(ctx);
            }, NameType.TABLE);
            if (ctx.parent instanceof ClickHouseParser.TableExprAliasContext) {
                ClickHouseParser.TableExprAliasContext parent = (ClickHouseParser.TableExprAliasContext) ctx.parent;
                if (parent.alias() != null) {
                    builder.addAttr(CommonAttribute.ALIAS, getName(getText(parent.alias())));
                } else {
                    builder.addAttr(CommonAttribute.ALIAS, getName(getText(parent.identifier())));
                }
            }
        });
        return null;
    }

    @Override
    public Void visitFromClause(ClickHouseParser.FromClauseContext ctx) {
        builder.handleSelectFrom(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitColumnExprAlias(ClickHouseParser.ColumnExprAliasContext ctx) {
        if (ctx.alias() != null) {
            builder.addAttr(CommonAttribute.ALIAS, getName(getText(ctx.alias())));
        } else {
            builder.addAttr(CommonAttribute.ALIAS, getName(getText(ctx.identifier())));
        }
        ctx.columnExpr().accept(this);
        return null;
    }

    @Override
    public Void visitColumnExprIdentifier(ClickHouseParser.ColumnExprIdentifierContext ctx) {
        builder.handleSelectColumn(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    @Override
    public Void visitColumnExprDate(ClickHouseParser.ColumnExprDateContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(Collections.singletonList(ctx.DATE().getText()), null), DomainSource.OBJ_NAME);
            builder.handleFunctionArgs(() -> {
                String string = getString(getText(ctx.stringLiteral()));
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, string);
                builder.handleDomain(new RdbConstantDomain(string), DomainSource.CONSTANT);
            });
        });
        return null;
    }

    @Override
    public Void visitColumnExprInterval(ClickHouseParser.ColumnExprIntervalContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(Collections.singletonList(ctx.INTERVAL().getText()), null), DomainSource.OBJ_NAME);
            builder.handleFunctionArgs(() -> {
                String string = getString(getText(ctx.columnExpr()));
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, string);
                ctx.columnExpr().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitCommentClause(ClickHouseParser.CommentClauseContext ctx) {
        builder.addAttr(CommonAttribute.COMMENT, getString(ctx.STRING_LITERAL().getText()));
        return null;
    }

    @Override
    public Void visitIdentifier(ClickHouseParser.IdentifierContext ctx) {
        builder.addAttr(CommonAttribute.VALUE, getName(getText(ctx)));
        return null;
    }

    private String getName(String name) {
        if (name.startsWith("`") || name.startsWith("\"")) {
            return name.substring(1, name.length() - 1);
        }
        return name;
    }

    @Override
    public Void visitColumnsExprAsterisk(ClickHouseParser.ColumnsExprAsteriskContext ctx) {
        builder.handleSelectColumn(() -> {
            if (ctx.columnExceptExpr() != null) {
                throw new UnsupportedOperationException("Column except expression not supported");
            }
            if (ctx.tableIdentifier() != null) {
                ctx.tableIdentifier().accept(this);
            }
            builder.addAttr(CommonAttribute.VALUE, "*");
        });
        return null;
    }

    @Override
    public Void visitOrderByClause(ClickHouseParser.OrderByClauseContext ctx) {
        if (ctx.parent instanceof ClickHouseParser.EngineClauseContext) {
            for (ParseTree child : ((ClickHouseParser.EngineClauseContext) ctx.parent).children) {
                if (child instanceof ClickHouseParser.PrimaryKeyClauseContext) {
                    return null;
                }
            }

            if (ctx.parent.parent instanceof ClickHouseParser.CreateTableStmtContext) {
                ClickHouseParser.CreateTableStmtContext createTableStmt = (ClickHouseParser.CreateTableStmtContext) ctx.parent.parent;
                if (createTableStmt.tableSchemaClause() != null) {
                    for (ParseTree child : createTableStmt.tableSchemaClause().children) {
                        if (child instanceof ClickHouseParser.TableElementExprPriContext) {
                            return null;
                        }
                    }
                }
            }
        } else {
            return null;
        }
        builder.handleConstraint(() -> {
            builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);

            builder.handleColumnList((() -> {
                ctx.orderExprList().accept(this);
            }));
        });

        return null;
    }

    @Override
    public Void visitOrderExpr(ClickHouseParser.OrderExprContext ctx) {
        ctx.columnExpr(0).accept(this);
        return null;
    }

    @Override
    public Void visitRenameStmt(ClickHouseParser.RenameStmtContext ctx) {
        ctx.renameEntityClause().accept(this);
        return null;
    }

    @Override
    public Void visitAlterTableStmt(ClickHouseParser.AlterTableStmtContext ctx) {
        builder.enterAlterTable();

        builder.handleObjName(() -> {
            ctx.tableIdentifier().accept(this);
        });

        for (ClickHouseParser.AlterTableClauseContext alterTableClauseContext : ctx.alterTableClause()) {
            alterTableClauseContext.accept(this);
        }
        builder.exitAlterTable();
        return null;
    }

    @Override
    public Void visitAlterTableClauseModifyTTL(ClickHouseParser.AlterTableClauseModifyTTLContext ctx) {
        ChTableDomain domain = new ChTableDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        builder.handleDomain(domain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableClauseDropPartition(ClickHouseParser.AlterTableClauseDropPartitionContext ctx) {
        ChTableDomain domain = new ChTableDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        builder.handleDomain(domain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableModifyColumnDefault(ClickHouseParser.AlterTableModifyColumnDefaultContext ctx) {
        ChColumnDomain domain = new ChColumnDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE_ALTER_COLUMN);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setDefaultValue(getString(getText(ctx.literal())));
        domain.setColumn(getName(getText(ctx.identifier())));
        builder.handleDomain(domain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableModifyComment(ClickHouseParser.AlterTableModifyCommentContext ctx) {
        ChTableDomain domain = new ChTableDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setComment(getString(ctx.commentClause().STRING_LITERAL().getText()));
        builder.handleDomain(domain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableClauseMovePartition(ClickHouseParser.AlterTableClauseMovePartitionContext ctx) {
        ChTableDomain domain = new ChTableDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        builder.handleDomain(domain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableClauseFreezePartition(ClickHouseParser.AlterTableClauseFreezePartitionContext ctx) {
        ChTableDomain domain = new ChTableDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        builder.handleDomain(domain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableClauseAddProjection(ClickHouseParser.AlterTableClauseAddProjectionContext ctx) {
        ChTableDomain domain = new ChTableDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        builder.handleDomain(domain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableClauseClearProjection(ClickHouseParser.AlterTableClauseClearProjectionContext ctx) {
        ChTableDomain domain = new ChTableDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        builder.handleDomain(domain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableClauseDropProjection(ClickHouseParser.AlterTableClauseDropProjectionContext ctx) {
        ChTableDomain domain = new ChTableDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        builder.handleDomain(domain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableClauseAddColumn(ClickHouseParser.AlterTableClauseAddColumnContext ctx) {
        builder.handleAlterTableItem(() -> {
            ctx.tableColumnDfnt().accept(this);
        }, AlterTableType.ADD_COLUMN);
        return null;
    }

    @Override
    public Void visitAlterTableModifyColumn(ClickHouseParser.AlterTableModifyColumnContext ctx) {
        builder.handleAlterTableItem(() -> {
            builder.handleColumnDef(() -> {
                builder.handleObjName(() -> {
                    builder.addAttr(CommonAttribute.VALUE, getName(getText(ctx.identifier())));
                });

                visitIfExists(ctx.columnTypeExpr());
                visitIfExists(ctx.commentClause());
                visitIfExists(ctx.tableColumnPropertyExpr());
            });
        }, AlterTableType.ALTER_COLUMN);
        return null;
    }

    @Override
    public Void visitAlterTableAlterColumn(ClickHouseParser.AlterTableAlterColumnContext ctx) {
        builder.handleAlterTableItem(() -> {
            builder.handleColumnDef(() -> {
                builder.handleObjName(() -> {
                    builder.addAttr(CommonAttribute.VALUE, getName(getText(ctx.identifier())));
                });

                visitIfExists(ctx.columnTypeExpr());
                visitIfExists(ctx.commentClause());
                visitIfExists(ctx.tableColumnPropertyExpr());
            });
        }, AlterTableType.ALTER_COLUMN);
        return null;
    }

    @Override
    public Void visitTableColumnPropertyExpr(ClickHouseParser.TableColumnPropertyExprContext ctx) {
        builder.handleDomain(new RdbConstantDomain(getText(ctx.columnExpr())), DomainSource.COLUMN_DEFAULT_VALUE);
        return null;
    }

    @Override
    public Void visitAlterTableClauseDropIndex(ClickHouseParser.AlterTableClauseDropIndexContext ctx) {
        builder.handleAlterTableItem(() -> {
            builder.enterDropIndex();
            builder.handleObjName(() -> {
                ctx.nestedIdentifier().accept(this);
            }, NameType.INDEX);
            builder.exitDropIndex();
        }, AlterTableType.DROP_INDEX);
        return null;
    }

    @Override
    public Void visitAlterTableClauseComment(ClickHouseParser.AlterTableClauseCommentContext ctx) {
        ChColumnDomain chColumnDomain = new ChColumnDomain();
        chColumnDomain.setAuditKind(SecQueryKind.ALTER);
        chColumnDomain.setSqlType(SecQueryType.COMMENT_COLUMN);
        chColumnDomain.setColumn(getName(getText(ctx.identifier())));
        chColumnDomain.setComment(getString(getText(ctx.stringLiteral())));
        builder.handleDomain(chColumnDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableClauseRenameColumn(ClickHouseParser.AlterTableClauseRenameColumnContext ctx) {
        ChColumnDomain chColumnDomain = new ChColumnDomain();
        chColumnDomain.setAuditKind(SecQueryKind.ALTER);
        chColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME_COLUMN);
        chColumnDomain.setColumn(getName(getText(ctx.identifier(0))));
        chColumnDomain.setNewName(getName(getText(ctx.identifier(1))));
        builder.handleDomain(chColumnDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableClauseAddIndex(ClickHouseParser.AlterTableClauseAddIndexContext ctx) {
        builder.handleAlterTableItem(() -> {
            ctx.tableIndexDfnt().accept(this);
        }, AlterTableType.ADD_INDEX);
        return null;
    }

    @Override
    public Void visitAlterTableClauseDropColumn(ClickHouseParser.AlterTableClauseDropColumnContext ctx) {
        builder.handleAlterTableItem(() -> {
            RdbColumnDomain rdbColumnDomain = new ChColumnDomain();
            rdbColumnDomain.setColumn(getName(getText(ctx.identifier())));
            builder.handleDomain(rdbColumnDomain, DomainSource.COLUMN_DEF);
        }, AlterTableType.DROP_COLUMN);
        return null;
    }

    @Override
    public Void visitDropDatabaseStmt(ClickHouseParser.DropDatabaseStmtContext ctx) {
        builder.handleDropSchema(() -> {
            if (ctx.EXISTS() != null) {
                builder.addAttr(CommonAttribute.IF_EXISTS, true);
            }

            builder.handleObjName(() -> {
                ctx.databaseIdentifier().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitCreateDatabaseStmt(ClickHouseParser.CreateDatabaseStmtContext ctx) {
        builder.handleCreateSchema(() -> {
            builder.handleObjName(() -> {
                ctx.databaseIdentifier().accept(this);
            });

            if (ctx.EXISTS() != null) {
                builder.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
            }
        });
        return null;
    }

    @Override
    public Void visitAlterTableClauseUpdate(ClickHouseParser.AlterTableClauseUpdateContext ctx) {
        builder.handleUpdate(() -> {

            builder.handleSetColumnValue(() -> {
                ctx.assignmentExprList().accept(this);
            });

            ctx.whereClause().accept(this);

        });
        return null;
    }

    @Override
    public Void visitAlterTableClauseDelete(ClickHouseParser.AlterTableClauseDeleteContext ctx) {
        builder.handleDelete(() -> {
            builder.handleWhere(() -> {
                ctx.columnExpr().accept(this);
            });
        });

        return null;
    }

    @Override
    public Void visitAlterTableClauseRemoveTTL(ClickHouseParser.AlterTableClauseRemoveTTLContext ctx) {
        ChTableDomain domain = new ChTableDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        builder.handleDomain(domain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitOptimizeStmt(ClickHouseParser.OptimizeStmtContext ctx) {
        ChTableDomain domain = new ChTableDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        if (ctx.tableIdentifier().databaseIdentifier() != null) {
            domain.setSchema(getText(ctx.tableIdentifier().databaseIdentifier()));
        }
        domain.setTable(getText(ctx.tableIdentifier().identifier()));
        builder.addDomain(domain);
        return null;
    }

    @Override
    public Void visitRenameEntityClause(ClickHouseParser.RenameEntityClauseContext ctx) {
        if (ctx.TABLE() != null) {
            builder.handleRename(() -> {
                builder.handleObjName(() -> {
                    ctx.tableIdentifier(0).accept(this);
                });

                builder.handleObjName(() -> {
                    ctx.tableIdentifier(1).accept(this);
                });
            }, TargetType.Table);
        } else if (ctx.DATABASE() != null) {
            builder.handleRename(() -> {
                builder.handleObjName(() -> {
                    ctx.databaseIdentifier(0).accept(this);
                });

                builder.handleObjName(() -> {
                    ctx.databaseIdentifier(1).accept(this);
                });
            }, TargetType.Schema);
        } else {
            throw new UnsupportedOperationException("Unsupported " + getText(ctx));
        }
        return null;
    }

    @Override
    public Void visitColumnExprIsNull(ClickHouseParser.ColumnExprIsNullContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnExprBetween(ClickHouseParser.ColumnExprBetweenContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUpdateStmt(ClickHouseParser.UpdateStmtContext ctx) {
        builder.handleUpdate(() -> {
            builder.handleObjName(() -> {
                ctx.nestedIdentifier().accept(this);
            });

            builder.handleSetColumnValue(() -> {
                ctx.assignmentExprList().accept(this);
            });

            ctx.whereClause().accept(this);
        });
        return null;
    }

    @Override
    public Void visitAssignmentExpr(ClickHouseParser.AssignmentExprContext ctx) {
        builder.handleSelectColumn(() -> {
            ctx.nestedIdentifier().accept(this);
        });
        ctx.columnExpr().accept(this);
        return null;
    }

    //

    @Override
    public Void visitShowCreateTableStmt(ClickHouseParser.ShowCreateTableStmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setTarget(TargetType.Table);

        Map<UmiTypes, String> map = parseTableName(ctx.tableIdentifier());
        rdbResourceDomain.setSchema(map.get(UmiTypes.Schema));
        rdbResourceDomain.setName(map.get(UmiTypes.Table));
        rdbResourceDomain.setNeedSupply(true);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowTablesStmt(ClickHouseParser.ShowTablesStmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setTarget(TargetType.Table);

        if (ctx.databaseIdentifier() != null) {
            rdbResourceDomain.setSchema(getName(getText(ctx.databaseIdentifier())));
        }
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowPrivilegesStmt(ClickHouseParser.ShowPrivilegesStmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setTarget(TargetType.Environment);
        rdbResourceDomain.setNeedSupply(false);

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowUsersStmt(ClickHouseParser.ShowUsersStmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        rdbResourceDomain.setNeedSupply(false);

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowRolesStmt(ClickHouseParser.ShowRolesStmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        rdbResourceDomain.setNeedSupply(false);

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowFilesystemCaches(ClickHouseParser.ShowFilesystemCachesContext ctx) {
        return envShowDomain();
    }

    @Override
    public Void visitShowClusterStmt(ClickHouseParser.ShowClusterStmtContext ctx) {
        return envShowDomain();
    }

    @Override
    public Void visitShowProfilesStmt(ClickHouseParser.ShowProfilesStmtContext ctx) {
        return envShowDomain();
    }

    @Override
    public Void visitShowQuotasStmt(ClickHouseParser.ShowQuotasStmtContext ctx) {
        return envShowDomain();
    }

    @Override
    public Void visitShowQuotaStmt(ClickHouseParser.ShowQuotaStmtContext ctx) {
        return envShowDomain();
    }

    @Override
    public Void visitShowPoliciesStmt(ClickHouseParser.ShowPoliciesStmtContext ctx) {
        return envShowDomain();
    }

    @Override
    public Void visitShowCreateQuotaStmt(ClickHouseParser.ShowCreateQuotaStmtContext ctx) {
        return envShowDomain();
    }

    private Void envShowDomain() {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setTarget(TargetType.Environment);
        rdbResourceDomain.setNeedSupply(false);

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowAccessStmt(ClickHouseParser.ShowAccessStmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setTarget(TargetType.Environment);
        rdbResourceDomain.setNeedSupply(false);

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowClustersStmt(ClickHouseParser.ShowClustersStmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setTarget(TargetType.Environment);
        rdbResourceDomain.setNeedSupply(false);

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowFunctionsStmt(ClickHouseParser.ShowFunctionsStmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setTarget(TargetType.Function);
        rdbResourceDomain.setNeedSupply(false);

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowEnginesStmt(ClickHouseParser.ShowEnginesStmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setTarget(TargetType.Environment);
        rdbResourceDomain.setNeedSupply(false);

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowMergesStmt(ClickHouseParser.ShowMergesStmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setTarget(TargetType.Environment);
        rdbResourceDomain.setNeedSupply(false);

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    private Map<UmiTypes, String> parseTableName(ClickHouseParser.TableIdentifierContext context) {
        List<String> names = new ArrayList<>();
        if (context.databaseIdentifier() != null) {
            names.add(getName(getText(context.databaseIdentifier().identifier())));
        }
        names.add(getName(getText(context.identifier())));
        return BuilderUtil.parseTableName(names);
    }

    @Override
    public Void visitShowCreateDatabaseStmt(ClickHouseParser.ShowCreateDatabaseStmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setTarget(TargetType.Schema);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setSchema(getName(getText(ctx.databaseIdentifier())));
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitQueryStmtQuery(ClickHouseParser.QueryStmtQueryContext ctx) {
        if (ctx.FORMAT() != null) {
            throw new UnsupportedOperationException("Unsupported format :" + getText(ctx));
        }
        ctx.query().accept(this);
        return null;
    }

    @Override
    public Void visitSelectStmt(ClickHouseParser.SelectStmtContext ctx) {
        builder.handleSelectDomain(() -> {
            dmVisitChildren(ctx);
        });
        return null;
    }

    //
    @Override
    public Void visitGroupByClauseSimple(ClickHouseParser.GroupByClauseSimpleContext ctx) {
        return null;
    }

    @Override
    public Void visitGroupByClauseAll(ClickHouseParser.GroupByClauseAllContext ctx) {
        return null;
    }

    @Override
    public Void visitGroupByClauseCubeOrRollup(ClickHouseParser.GroupByClauseCubeOrRollupContext ctx) {
        return null;
    }

    @Override
    public Void visitGroupByClauseGroupingSets(ClickHouseParser.GroupByClauseGroupingSetsContext ctx) {
        return null;
    }

    @Override
    public Void visitTableElementExprProjection(ClickHouseParser.TableElementExprProjectionContext ctx) {
        return null;
    }

    @Override
    public Void visitPartitionByClause(ClickHouseParser.PartitionByClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitTtlClause(ClickHouseParser.TtlClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitSampleByClause(ClickHouseParser.SampleByClauseContext ctx) {
        return null;
    }

    //
    @Override
    public Void visitColumnArgList(ClickHouseParser.ColumnArgListContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnExprLiteral(ClickHouseParser.ColumnExprLiteralContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnsExprSubquery(ClickHouseParser.ColumnsExprSubqueryContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnExprAnd(ClickHouseParser.ColumnExprAndContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnExprOr(ClickHouseParser.ColumnExprOrContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnExprParens(ClickHouseParser.ColumnExprParensContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWithClause(ClickHouseParser.WithClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWithExprList(ClickHouseParser.WithExprListContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitJoinExprTable(ClickHouseParser.JoinExprTableContext ctx) {
        ctx.tableExpr().accept(this);
        return null;
    }

    @Override
    public Void visitQueryStmtDelete(ClickHouseParser.QueryStmtDeleteContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTableIdentifier(ClickHouseParser.TableIdentifierContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDatabaseIdentifier(ClickHouseParser.DatabaseIdentifierContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnIdentifier(ClickHouseParser.ColumnIdentifierContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNestedIdentifier(ClickHouseParser.NestedIdentifierContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitJoinExprParens(ClickHouseParser.JoinExprParensContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSelectStmtWithParens(ClickHouseParser.SelectStmtWithParensContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSettingsClause(ClickHouseParser.SettingsClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitQuery(ClickHouseParser.QueryContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnExprSubquery(ClickHouseParser.ColumnExprSubqueryContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQueryStmtInsert(ClickHouseParser.QueryStmtInsertContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnExprTuple(ClickHouseParser.ColumnExprTupleContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAssignmentValue(ClickHouseParser.AssignmentValueContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAssignmentValues(ClickHouseParser.AssignmentValuesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitOrderExprList(ClickHouseParser.OrderExprListContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDataClauseSelect(ClickHouseParser.DataClauseSelectContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQueryStmtUpdate(ClickHouseParser.QueryStmtUpdateContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSchemaDescriptionClause(ClickHouseParser.SchemaDescriptionClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTableElementExprColumn(ClickHouseParser.TableElementExprColumnContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitEngineClause(ClickHouseParser.EngineClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSubqueryClause(ClickHouseParser.SubqueryClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAssignmentExprList(ClickHouseParser.AssignmentExprListContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }
}
