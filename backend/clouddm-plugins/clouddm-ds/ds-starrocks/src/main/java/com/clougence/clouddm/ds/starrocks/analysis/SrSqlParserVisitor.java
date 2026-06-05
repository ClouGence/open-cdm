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
package com.clougence.clouddm.ds.starrocks.analysis;

import static com.clougence.clouddm.ds.starrocks.parser.antlr.StarRocksParser.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.ds.starrocks.analysis.builder.SrBuilderFactory;
import com.clougence.clouddm.ds.starrocks.analysis.enums.SrAttribute;
import com.clougence.clouddm.ds.starrocks.analysis.secrules.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ColumnTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.KeyValueDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbResourceDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbRoleDomain;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.starrocks.parser.antlr.StarRocksBaseVisitor;
import com.clougence.schema.umi.struts.UmiTypes;

public class SrSqlParserVisitor extends StarRocksBaseVisitor<Void> {

    private SrBuilderFactory factory;
    private Parser           parser;

    public SrSqlParserVisitor(SrBuilderFactory srBuilderFactory, Parser parser){
        this.factory = srBuilderFactory;
        this.parser = parser;
    }

    private void visitIfExist(ParserRuleContext ctx) {
        if (ctx != null) {
            ctx.accept(this);
        }
    }

    private String getText(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private String getText(Token start, Token end) {
        return parser.getTokenStream().getText(start, end);
    }

    @Override
    public Void visitSqlStatements(SqlStatementsContext ctx) {
        throw new UnsupportedOperationException(ctx.getText());
    }

    @Override
    public Void visitUseDatabaseStatement(UseDatabaseStatementContext ctx) {
        return null;
    }

    @Override
    public Void visitAlterDbQuotaStatement(AlterDbQuotaStatementContext ctx) {
        SrSchemaDomain srSchemaDomain = new SrSchemaDomain();
        srSchemaDomain.setAuditKind(SecQueryKind.ALTER);
        srSchemaDomain.setSqlType(SecQueryType.ALTER_SCHEMA);
        srSchemaDomain.setSchema(getName(ctx.identifier(0).getText()));
        factory.addDomain(srSchemaDomain);
        return null;
    }

    @Override
    public Void visitCreateDbStatement(CreateDbStatementContext ctx) {
        factory.enterCreateSchema();

        if (ctx.EXISTS() != null) {
            factory.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
        }

        List<String> nameList = new ArrayList<>();
        if (ctx.catalog != null) {
            nameList.add(getName(ctx.catalog.getText()));
        }
        if (ctx.database.getChildCount() > 1) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }

        nameList.add(getName(ctx.database.getText()));

        ObjNameDomain objNameDomain = new ObjNameDomain(nameList, null);
        factory.handleDomain(objNameDomain, DomainSource.OBJ_NAME);

        visitIfExist(ctx.properties());

        factory.exitCreateSchema();

        return null;
    }

    private String getName(String text) {
        if (text.startsWith("`")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    @Override
    public Void visitDropDbStatement(DropDbStatementContext ctx) {
        factory.enterDropSchema();

        if (ctx.EXISTS() != null) {
            factory.addAttr(CommonAttribute.IF_EXISTS, true);
        }

        if (ctx.FORCE() != null) {
            factory.addAttr(SrAttribute.FORCE, true);
        }

        List<String> nameList = new ArrayList<>();
        if (ctx.catalog != null) {
            nameList.add(getName(ctx.catalog.getText()));
        }
        if (ctx.database.getChildCount() > 1) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }

        nameList.add(getName(ctx.database.getText()));

        ObjNameDomain objNameDomain = new ObjNameDomain(nameList, null);
        factory.handleDomain(objNameDomain, DomainSource.OBJ_NAME);

        factory.exitDropSchema();
        return null;
    }

    @Override
    public Void visitAlterDatabaseRenameStatement(AlterDatabaseRenameStatementContext ctx) {
        SrSchemaDomain srSchemaDomain = new SrSchemaDomain();
        srSchemaDomain.setAuditKind(SecQueryKind.ALTER);
        srSchemaDomain.setSqlType(SecQueryType.RENAME_SCHEMA);
        srSchemaDomain.setSchema(getName(ctx.identifier(0).getText()));
        srSchemaDomain.setNewName(getName(ctx.identifier(1).getText()));
        factory.addDomain(srSchemaDomain);
        return null;
    }

    @Override
    public Void visitCreateTableStatement(CreateTableStatementContext ctx) {
        factory.enterCreateTable();

        if (ctx.TEMPORARY() != null) {
            factory.addAttr(MyAttribute.TEMPORARY, true);
        } else if (ctx.EXTERNAL() != null) {
            factory.addAttr(SrAttribute.EXTERNAL, true);
        }

        if (ctx.EXISTS() != null) {
            factory.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
        }

        dmVisitChildren(ctx);

        factory.exitCreateTable();

        return null;
    }

    @Override
    public Void visitDistributionDesc(DistributionDescContext ctx) {
        return null;
    }

    @Override
    public Void visitPartitionDesc(PartitionDescContext ctx) {
        return null;
    }

    @Override
    public Void visitOrderByDesc(OrderByDescContext ctx) {
        return null;
    }

    @Override
    public Void visitColumnDesc(ColumnDescContext ctx) {
        factory.enterColumnDef();

        factory.enterObjName();
        ctx.identifier().accept(this);
        factory.exitObjName();

        ctx.type().accept(this);

        if (ctx.AUTO_INCREMENT() != null) {
            factory.addAttr(MyAttribute.AUTO_INCREMENT, true);
        }

        visitIfExist(ctx.aggDesc());
        visitIfExist(ctx.defaultDesc());
        visitIfExist(ctx.comment());
        visitIfExist(ctx.columnNullable());

        factory.exitColumnDef();
        return null;
    }

    @Override
    public Void visitDefaultDesc(DefaultDescContext ctx) {
        factory.enterColumnDefault();
        if (ctx.string() != null) {
            factory.handleDomain(new RdbConstantDomain(getText(ctx.string())), DomainSource.CONSTANT);
        } else {
            TerminalNodeImpl node = (TerminalNodeImpl) ctx.getChild(1);
            TerminalNodeImpl node1 = (TerminalNodeImpl) ctx.getChild(ctx.getChildCount() - 1);
            factory.handleDomain(new RdbConstantDomain(getText(node.getSymbol(), node1.getSymbol())), DomainSource.CONSTANT);
        }
        factory.exitColumnDefault();
        return null;
    }

    @Override
    public Void visitIndexDesc(IndexDescContext ctx) {
        factory.enterCreateIndex();

        ObjNameDomain objNameDomain = new ObjNameDomain(ctx.indexName.getText(), NameType.INDEX);
        factory.handleDomain(objNameDomain, DomainSource.OBJ_NAME);

        ctx.identifierList().accept(this);
        visitIfExist(ctx.comment());
        visitIfExist(ctx.indexType());
        factory.exitCreateIndex();
        return null;
    }

    @Override
    public Void visitEngineDesc(EngineDescContext ctx) {
        factory.addAttr(MyAttribute.ENGINE, ctx.identifier().getText());
        return null;
    }

    @Override
    public Void visitKeyDesc(KeyDescContext ctx) {
        factory.addAttr(SrAttribute.TABLE_MODEL, ctx.getChild(0).getText());
        return null;
    }

    @Override
    public Void visitColumnNullable(ColumnNullableContext ctx) {
        if (ctx.NOT() != null) {
            factory.addAttr(CommonAttribute.NOT_NULL, true);
        }
        return null;
    }

    @Override
    public Void visitAggDesc(AggDescContext ctx) {
        factory.addAttr(SrAttribute.AGG_TYPE, this.getText(ctx));
        return null;
    }

    @Override
    public Void visitCreateTableAsSelectStatement(CreateTableAsSelectStatementContext ctx) {
        factory.enterCreateTable(SecQueryType.CREATE_TABLE_LIKE);

        if (ctx.TEMPORARY() != null) {
            factory.addAttr(MyAttribute.TEMPORARY, true);
        }

        if (ctx.EXISTS() != null) {
            factory.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
        }

        dmVisitChildren(ctx);

        factory.exitCreateTable();

        return null;
    }

    @Override
    public Void visitSpecialFunction(SpecialFunctionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSpecialDateTime(SpecialDateTimeContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitInformationFunction(InformationFunctionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTranslateFunctionCall(TranslateFunctionCallContext ctx) {
        factory.handleCall(() -> {
            factory.handleDomain(new ObjNameDomain(ctx.getChild(0).getText()), DomainSource.OBJ_NAME);

            factory.handleFunctionArgs(() -> {
                for (ExpressionContext expressionContext : ctx.expression()) {
                    factory.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(expressionContext));
                    expressionContext.accept(this);
                }
            });
        });
        return null;
    }

    @Override
    public Void visitInformationFunctionExpression(InformationFunctionExpressionContext ctx) {
        factory.handleCall(() -> {
            factory.handleDomain(new ObjNameDomain(ctx.getChild(0).getText()), DomainSource.OBJ_NAME);
        });
        return null;
    }

    @Override
    public Void visitSpecialDateTimeExpression(SpecialDateTimeExpressionContext ctx) {
        factory.handleCall(() -> {
            factory.handleDomain(new ObjNameDomain(ctx.getChild(0).getText()), DomainSource.OBJ_NAME);

            if (ctx.INTEGER_VALUE() != null) {
                factory.handleFunctionArgs(() -> {
                    factory.addAttr(CommonAttribute.FUNC_ARG_NAME, ctx.INTEGER_VALUE().getText());
                    factory.handleDomain(new RdbConstantDomain(ctx.INTEGER_VALUE().getText()), DomainSource.CONSTANT);
                });
            }
        });
        return null;
    }

    @Override
    public Void visitSpecialFunctionExpression(SpecialFunctionExpressionContext ctx) {
        factory.handleCall(() -> {
            factory.handleDomain(new ObjNameDomain(ctx.getChild(0).getText()), DomainSource.OBJ_NAME);

            factory.handleFunctionArgs(() -> {
                for (int i = 1; i < ctx.children.size(); i++) {
                    ParseTree child = ctx.getChild(i);
                    factory.addAttr(CommonAttribute.FUNC_ARG_NAME, child.getText());
                    if (child instanceof UnitIdentifierContext) {
                        factory.handleDomain(new RdbConstantDomain(child.getText()), DomainSource.CONSTANT);
                    } else {
                        child.accept(this);
                    }

                }
            });
        });
        return null;
    }

    @Override
    public Void visitDescTableStatement(DescTableStatementContext ctx) {
        if (ctx.table == null) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setTarget(TargetType.Table);
        rdbResourceDomain.setNeedSupply(true);
        Map<UmiTypes, String> map = BuilderUtil.parseTableName(parseName(ctx.table));
        rdbResourceDomain.setName(map.get(UmiTypes.Table));
        rdbResourceDomain.setSchema(map.get(UmiTypes.Schema));
        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitAnalyzeStatement(AnalyzeStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setTarget(TargetType.Table);
        rdbResourceDomain.setNeedSupply(true);
        Map<UmiTypes, String> map = BuilderUtil.parseTableName(parseName(ctx.tableName().qualifiedName()));
        rdbResourceDomain.setName(map.get(UmiTypes.Table));
        rdbResourceDomain.setSchema(map.get(UmiTypes.Schema));
        factory.addDomain(rdbResourceDomain);
        return null;
    }

    private List<String> parseName(QualifiedNameContext ctx) {
        ArrayList<String> list = new ArrayList<>();
        for (IdentifierContext identifierContext : ctx.identifier()) {
            list.add(getName(identifierContext.getText()));
        }
        return list;
    }

    @Override
    public Void visitDropTableStatement(DropTableStatementContext ctx) {
        factory.enterDropTable();

        if (ctx.TEMPORARY() != null) {
            factory.addAttr(MyAttribute.TEMPORARY, true);
        }

        ctx.qualifiedName().accept(this);

        factory.exitDropTable();
        return null;
    }

    @Override
    public Void visitAlterTableStatement(AlterTableStatementContext ctx) {
        factory.enterAlterTable();

        ctx.qualifiedName().accept(this);

        for (AlterClauseContext alterClauseContext : ctx.alterClause()) {
            alterClauseContext.accept(this);
        }

        factory.exitAlterTable();
        return null;
    }

    @Override
    public Void visitAddPartitionClause(AddPartitionClauseContext ctx) {
        SrTableDomain srTableDomain = new SrTableDomain();
        srTableDomain.setAuditKind(SecQueryKind.ALTER);
        srTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        factory.handleDomain(srTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitModifyPartitionClause(ModifyPartitionClauseContext ctx) {
        SrTableDomain srTableDomain = new SrTableDomain();
        srTableDomain.setAuditKind(SecQueryKind.ALTER);
        srTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        factory.handleDomain(srTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitDropPartitionClause(DropPartitionClauseContext ctx) {
        SrTableDomain srTableDomain = new SrTableDomain();
        srTableDomain.setAuditKind(SecQueryKind.ALTER);
        srTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        factory.handleDomain(srTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitCreateIndexStatement(CreateIndexStatementContext ctx) {
        factory.enterCreateIndex();

        factory.enterObjName(NameType.INDEX);
        ctx.indexName.accept(this);
        factory.exitObjName();

        ctx.qualifiedName().accept(this);
        ctx.identifierList().accept(this);

        visitIfExist(ctx.comment());
        visitIfExist(ctx.indexType());
        factory.exitCreateIndex();
        return null;
    }

    @Override
    public Void visitDropIndexStatement(DropIndexStatementContext ctx) {
        factory.enterDropIndex();

        factory.enterObjName(NameType.INDEX);
        factory.addAttr(CommonAttribute.VALUE, ctx.indexName.getText());
        factory.exitObjName();

        ctx.qualifiedName().accept(this);
        factory.exitDropIndex();
        return null;
    }

    @Override
    public Void visitIndexType(IndexTypeContext ctx) {
        factory.addAttr(CommonAttribute.INDEX_TYPE, ctx.getChild(1).getText());
        return null;
    }

    @Override
    public Void visitShowCreateExternalCatalogStatement(ShowCreateExternalCatalogStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setCatalog(getName(ctx.catalogName.getText()));
        rdbResourceDomain.setTarget(TargetType.Table);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowVariablesStatement(ShowVariablesStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setTarget(TargetType.Environment);
        rdbResourceDomain.setNeedSupply(false);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowWarningStatement(ShowWarningStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setTarget(TargetType.Environment);
        rdbResourceDomain.setNeedSupply(false);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowDatabasesStatement(ShowDatabasesStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setTarget(TargetType.Schema);
        if (ctx.qualifiedName() != null) {
            if (ctx.qualifiedName().identifier().size() != 1) {
                throw new UnsupportedOperationException("Error SQL:" + getText(ctx));
            }
            rdbResourceDomain.setCatalog(getText(ctx.qualifiedName()));
        }
        rdbResourceDomain.setNeedSupply(false);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowPartitionsStatement(ShowPartitionsStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        if (ctx.qualifiedName().identifier().size() == 1) {
            rdbResourceDomain.setName(getName(ctx.qualifiedName().identifier(0).getText()));
        } else if (ctx.qualifiedName().identifier().size() == 2) {
            rdbResourceDomain.setName(getName(ctx.qualifiedName().identifier(1).getText()));
            rdbResourceDomain.setSchema(getName(ctx.qualifiedName().identifier(0).getText()));
        } else if (ctx.qualifiedName().identifier().size() == 3) {
            rdbResourceDomain.setCatalog(getName(ctx.qualifiedName().identifier(0).getText()));
            rdbResourceDomain.setSchema(getName(ctx.qualifiedName().identifier(1).getText()));
            rdbResourceDomain.setName(getName(ctx.qualifiedName().identifier(1).getText()));
        }
        rdbResourceDomain.setTarget(TargetType.Table);
        rdbResourceDomain.setNeedSupply(true);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowTableStatement(ShowTableStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        if (ctx.db != null) {
            List<String> names = new ArrayList<>();
            for (IdentifierContext identifierContext : ctx.qualifiedName().identifier()) {
                names.add(getName(identifierContext.getText()));
            }
            if (names.size() == 1) {
                rdbResourceDomain.setSchema(names.get(0));
            } else if (names.size() == 2) {
                rdbResourceDomain.setCatalog(names.get(0));
                rdbResourceDomain.setSchema(names.get(1));
            } else {
                throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx.db));
            }
        }
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Table);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowMaterializedViewsStatement(ShowMaterializedViewsStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        if (ctx.db != null) {
            List<String> names = new ArrayList<>();
            for (IdentifierContext identifierContext : ctx.qualifiedName().identifier()) {
                names.add(getName(identifierContext.getText()));
            }
            if (names.size() == 1) {
                rdbResourceDomain.setSchema(names.get(0));
            } else if (names.size() == 2) {
                rdbResourceDomain.setCatalog(names.get(0));
                rdbResourceDomain.setSchema(names.get(1));
            } else {
                throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx.db));
            }
        }
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Materialized);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowFunctionsStatement(ShowFunctionsStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        if (ctx.db != null) {
            List<String> names = new ArrayList<>();
            for (IdentifierContext identifierContext : ctx.qualifiedName().identifier()) {
                names.add(getName(identifierContext.getText()));
            }
            if (names.size() == 1) {
                rdbResourceDomain.setSchema(names.get(0));
            } else if (names.size() == 2) {
                rdbResourceDomain.setCatalog(names.get(0));
                rdbResourceDomain.setSchema(names.get(1));
            } else {
                throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx.db));
            }
        }
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Function);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowDictionaryStatement(ShowDictionaryStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowCreateDbStatement(ShowCreateDbStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Schema);

        rdbResourceDomain.setSchema(getName(getText(ctx.identifier())));

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowBrokerStatement(ShowBrokerStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowComputeNodesStatement(ShowComputeNodesStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowFrontendsStatement(ShowFrontendsStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowProcesslistStatement(ShowProcesslistStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowRunningQueriesStatement(ShowRunningQueriesStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowAnalyzeStatement(ShowAnalyzeStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowDataStmt(ShowDataStmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Table);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowCreateTableStatement(ShowCreateTableStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        if (ctx.table != null) {
            List<String> names = new ArrayList<>();
            for (IdentifierContext identifierContext : ctx.qualifiedName().identifier()) {
                names.add(getName(identifierContext.getText()));
            }
            if (names.size() == 1) {
                rdbResourceDomain.setName(names.get(0));
            } else if (names.size() == 2) {
                rdbResourceDomain.setSchema(names.get(0));
                rdbResourceDomain.setName(names.get(1));
            } else {
                throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx.table));
            }
        }
        rdbResourceDomain.setNeedSupply(true);
        if (ctx.TABLE() != null) {
            rdbResourceDomain.setTarget(TargetType.Table);
        } else if (ctx.MATERIALIZED() != null) {
            rdbResourceDomain.setTarget(TargetType.Materialized);
        } else {
            rdbResourceDomain.setTarget(TargetType.View);
        }

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowColumnStatement(ShowColumnStatementContext ctx) {
        errorIfExist(ctx.expression());
        errorIfExist(ctx.db);
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        if (ctx.table != null) {
            List<String> names = new ArrayList<>();
            for (IdentifierContext identifierContext : ctx.table.identifier()) {
                names.add(getName(identifierContext.getText()));
            }
            if (names.size() == 1) {
                rdbResourceDomain.setName(names.get(0));
            } else if (names.size() == 2) {
                rdbResourceDomain.setSchema(names.get(0));
                rdbResourceDomain.setName(names.get(1));
            } else {
                rdbResourceDomain.setCatalog(names.get(0));
                rdbResourceDomain.setSchema(names.get(1));
                rdbResourceDomain.setName(names.get(2));
            }
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Table);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowAlterStatement(ShowAlterStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        if (ctx.db != null) {
            List<String> names = new ArrayList<>();
            for (IdentifierContext identifierContext : ctx.qualifiedName().identifier()) {
                names.add(getName(identifierContext.getText()));
            }
            if (names.size() == 1) {
                rdbResourceDomain.setSchema(names.get(0));
            } else if (names.size() == 2) {
                rdbResourceDomain.setCatalog(names.get(0));
                rdbResourceDomain.setSchema(names.get(1));
            } else {
                throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx.db));
            }
        }
        rdbResourceDomain.setNeedSupply(true);
        if (ctx.COLUMN() != null) {
            rdbResourceDomain.setTarget(TargetType.Column);
        } else if (ctx.VIEW() != null) {
            rdbResourceDomain.setTarget(TargetType.Materialized);
        } else {
            rdbResourceDomain.setTarget(TargetType.Unknown);
        }

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitCreateTableLikeStatement(CreateTableLikeStatementContext ctx) {
        factory.enterCreateTable(SecQueryType.CREATE_TABLE_LIKE);

        if (ctx.TEMPORARY() != null) {
            factory.addAttr(MyAttribute.TEMPORARY, true);
        } else if (ctx.EXTERNAL() != null) {
            factory.addAttr(SrAttribute.EXTERNAL, true);
        }

        if (ctx.EXISTS() != null) {
            factory.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
        }

        dmVisitChildren(ctx);

        factory.exitCreateTable();

        return null;
    }

    @Override
    public Void visitShowIndexStatement(ShowIndexStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        if (ctx.table != null) {
            List<String> names = new ArrayList<>();
            for (IdentifierContext identifierContext : ctx.table.identifier()) {
                names.add(getName(identifierContext.getText()));
            }
            if (names.size() == 1) {
                rdbResourceDomain.setName(names.get(0));
            } else if (names.size() == 2) {
                rdbResourceDomain.setSchema(names.get(0));
                rdbResourceDomain.setName(names.get(1));
            } else if (names.size() == 3) {
                rdbResourceDomain.setCatalog(names.get(0));
                rdbResourceDomain.setSchema(names.get(1));
                rdbResourceDomain.setName(names.get(2));
            } else {
                throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx.table));
            }
        }
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Table);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitTruncateTableStatement(TruncateTableStatementContext ctx) {
        factory.enterTruncateTable();
        dmVisitChildren(ctx);
        factory.exitTruncateTable();
        return null;
    }

    @Override
    public Void visitCancelAlterTableStatement(CancelAlterTableStatementContext ctx) {
        if (ctx.TABLE() == null) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }
        SrTableDomain drTableDomain = new SrTableDomain();
        drTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        drTableDomain.setAuditKind(SecQueryKind.ALTER);

        List<String> names = new ArrayList<>();
        for (IdentifierContext identifierContext : ctx.qualifiedName().identifier()) {
            names.add(getName(identifierContext.getText()));
        }
        if (names.size() == 1) {
            drTableDomain.setTable(names.get(0));
        } else if (names.size() == 2) {
            drTableDomain.setSchema(names.get(0));
            drTableDomain.setTable(names.get(1));
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx.qualifiedName()));
        }
        factory.addDomain(drTableDomain);

        return null;
    }

    @Override
    public Void visitCreateViewStatement(CreateViewStatementContext ctx) {
        factory.enterCreateView();
        ctx.qualifiedName().accept(this);
        factory.exitCreateView();
        return null;
    }

    @Override
    public Void visitDropViewStatement(DropViewStatementContext ctx) {
        factory.enterView(SecQueryType.DROP_VIEW);
        dmVisitChildren(ctx);
        factory.exitView();
        return null;

    }

    @Override
    public Void visitCreateMaterializedViewStatement(CreateMaterializedViewStatementContext ctx) {
        factory.enterCreateView();
        factory.addAttr(CommonAttribute.MATERIALIZED, true);
        ctx.qualifiedName().accept(this);
        factory.exitCreateView();
        return null;
    }

    @Override
    public Void visitDropMaterializedViewStatement(DropMaterializedViewStatementContext ctx) {
        factory.enterView(SecQueryType.DROP_VIEW);
        factory.addAttr(CommonAttribute.MATERIALIZED, true);
        ctx.qualifiedName().accept(this);
        factory.exitView();
        return null;
    }

    @Override
    public Void visitCreateExternalCatalogStatement(CreateExternalCatalogStatementContext ctx) {
        factory.enterCreateCatalog();

        factory.enterObjName();
        ctx.catalogName.accept(this);
        factory.exitObjName();

        visitIfExist(ctx.comment());

        ctx.properties().accept(this);

        if (ctx.EXISTS() != null) {
            factory.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
        }

        factory.exitCreateCatalog();
        return null;
    }

    @Override
    public Void visitDropExternalCatalogStatement(DropExternalCatalogStatementContext ctx) {
        factory.enterDropCatalog();

        factory.enterObjName();
        ctx.catalogName.accept(this);
        factory.exitObjName();

        if (ctx.EXISTS() != null) {
            factory.addAttr(CommonAttribute.IF_EXISTS, true);
        }

        factory.exitDropCatalog();
        return null;
    }

    @Override
    public Void visitShowCatalogsStatement(ShowCatalogsStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Catalog);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitAlterCatalogStatement(AlterCatalogStatementContext ctx) {
        factory.enterAlterCatalog();

        factory.enterObjName();
        ctx.catalogName.accept(this);
        factory.exitObjName();

        ctx.modifyPropertiesClause().propertyList().accept(this);

        factory.exitAlterCatalog();
        return null;
    }

    @Override
    public Void visitCreateIndexClause(CreateIndexClauseContext ctx) {
        factory.enterAlterTableItem(AlterTableType.ADD_INDEX);
        factory.enterCreateIndex();

        factory.enterObjName(NameType.INDEX);
        ctx.indexName.accept(this);
        factory.exitObjName();

        ctx.identifierList().accept(this);

        visitIfExist(ctx.comment());

        factory.exitCreateIndex();

        factory.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitDropIndexClause(DropIndexClauseContext ctx) {
        factory.enterAlterTableItem(AlterTableType.DROP_INDEX);
        factory.enterDropIndex();

        factory.enterObjName(NameType.INDEX);
        ctx.indexName.accept(this);
        factory.exitObjName();

        factory.exitDropIndex();

        factory.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitTableRenameClause(TableRenameClauseContext ctx) {
        SrTableDomain srTableDomain = new SrTableDomain();
        srTableDomain.setNewName(getName(ctx.identifier().getText()));
        srTableDomain.setAuditKind(SecQueryKind.ALTER);
        srTableDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME);
        factory.handleDomain(srTableDomain, DomainSource.ALTER_TABLE_ITEM);

        return null;
    }

    @Override
    public Void visitModifyCommentClause(ModifyCommentClauseContext ctx) {
        SrTableDomain srTableDomain = new SrTableDomain();
        srTableDomain.setComment(getString(ctx.string().getText()));
        srTableDomain.setAuditKind(SecQueryKind.ALTER);
        srTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        factory.handleDomain(srTableDomain, DomainSource.ALTER_TABLE_ITEM);

        return null;
    }

    @Override
    public Void visitOptimizeClause(OptimizeClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitAddColumnClause(AddColumnClauseContext ctx) {
        factory.enterAlterTableItem(AlterTableType.ADD_COLUMN);
        ctx.columnDesc().accept(this);
        factory.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitAddColumnsClause(AddColumnsClauseContext ctx) {
        factory.enterAlterTableItem(AlterTableType.ADD_COLUMN);
        ctx.columnDesc().forEach(desc -> {
            desc.accept(this);
        });
        factory.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitDropColumnClause(DropColumnClauseContext ctx) {
        SrColumnDomain columnDomain = new SrColumnDomain();
        columnDomain.setSqlType(SecQueryType.ALTER_TABLE_DROP_COLUMN);
        columnDomain.setAuditKind(SecQueryKind.ALTER);
        columnDomain.setColumn(getName(ctx.identifier(0).getText()));
        factory.handleDomain(columnDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitModifyColumnClause(ModifyColumnClauseContext ctx) {
        factory.enterAlterTableItem(AlterTableType.ALTER_COLUMN);
        ctx.columnDesc().accept(this);
        factory.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitColumnRenameClause(ColumnRenameClauseContext ctx) {
        SrColumnDomain columnDomain = new SrColumnDomain();
        columnDomain.setAuditKind(SecQueryKind.ALTER);
        columnDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME_COLUMN);
        columnDomain.setColumn(getName(ctx.oldColumn.getText()));
        columnDomain.setNewName(getName(ctx.newColumn.getText()));
        factory.handleDomain(columnDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        factory.enterInsertBuilder();
        errorIfExist(ctx.explainDesc());

        ctx.qualifiedName().accept(this);

        if (ctx.insertLabelOrColumnAliases().size() > 1) {
            throw new UnsupportedOperationException("Unsupported SQL: " + this.getText(ctx));
        }
        for (InsertLabelOrColumnAliasesContext insertLabelOrColumnAlias : ctx.insertLabelOrColumnAliases()) {
            insertLabelOrColumnAlias.accept(this);
        }

        if (ctx.expressionsWithDefault().size() > 1) {
            factory.addAttr(CommonAttribute.MULTI_VALUE, true);
        }
        factory.enterValuesBuilder();
        for (ExpressionsWithDefaultContext expressionsWithDefaultContext : ctx.expressionsWithDefault()) {
            expressionsWithDefaultContext.accept(this);
        }
        factory.exitValuesBuilder();

        visitIfExist(ctx.queryStatement());

        factory.exitInsertBuilder();
        return null;
    }

    @Override
    public Void visitInsertLabelOrColumnAliases(InsertLabelOrColumnAliasesContext ctx) {
        if (ctx.LABEL() != null) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnAliasesOrByName(ColumnAliasesOrByNameContext ctx) {
        factory.enterInsertColumnBuilder();
        dmVisitChildren(ctx);
        factory.exitInsertColumnBuilder();
        return null;
    }

    @Override
    public Void visitUpdateStatement(UpdateStatementContext ctx) {
        factory.enterUpdate();

        errorIfExist(ctx.explainDesc());
        visitIfExist(ctx.withClause());
        ctx.qualifiedName().accept(this);
        ctx.assignmentList().accept(this);
        ctx.fromClause().accept(this);

        if (ctx.where != null) {
            factory.enterWhere();
            ctx.where.accept(this);
            factory.exitWhere();
        }

        factory.exitUpdate();
        return null;
    }

    @Override
    public Void visitDeleteStatement(DeleteStatementContext ctx) {
        errorIfExist(ctx.using);
        errorIfExist(ctx.explainDesc());
        errorIfExist(ctx.withClause());

        factory.enterDelete();
        ctx.qualifiedName().accept(this);

        factory.enterWhere();
        visitIfExist(ctx.where);
        factory.exitWhere();

        factory.exitDelete();
        return null;
    }

    private void errorIfExist(ParserRuleContext ctx) {
        if (ctx != null) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }
    }

    @Override
    public Void visitDropFunctionStatement(DropFunctionStatementContext ctx) {
        factory.enterDropFunction();
        ctx.qualifiedName().accept(this);
        factory.exitDropFunction();
        return null;
    }

    @Override
    public Void visitCreateFunctionStatement(CreateFunctionStatementContext ctx) {
        factory.enterCreateFunction();
        ctx.qualifiedName().accept(this);
        factory.exitCreateFunction();
        return null;
    }

    @Override
    public Void visitShowDeleteStatement(ShowDeleteStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        if (ctx.db != null) {
            List<String> names = new ArrayList<>();
            for (IdentifierContext identifierContext : ctx.qualifiedName().identifier()) {
                names.add(getName(identifierContext.getText()));
            }
            if (names.size() == 1) {
                rdbResourceDomain.setSchema(names.get(0));
            } else {
                throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx.db));
            }
        }
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Schema);

        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitCreateUserStatement(CreateUserStatementContext ctx) {
        SrUserDomain srUserDomain = new SrUserDomain();
        srUserDomain.setAuditKind(SecQueryKind.CREATE);
        srUserDomain.setSqlType(SecQueryType.CREATE_USER);
        srUserDomain.setHost("%");

        for (ParseTree child : ctx.user().children) {
            if (child instanceof IdentifierOrStringContext) {
                if (srUserDomain.getUser() == null) {
                    srUserDomain.setUser(getString(child.getText()));
                } else {
                    srUserDomain.setHost(getString(child.getText()));
                }
            }
        }
        if (ctx.authOption() != null) {
            srUserDomain.setPassword(getString(ctx.authOption().getChild(ctx.authOption().getChildCount() - 1).getText()));
        }

        factory.addDomain(srUserDomain);

        return null;
    }

    @Override
    public Void visitDropUserStatement(DropUserStatementContext ctx) {
        SrUserDomain srUserDomain = new SrUserDomain();
        srUserDomain.setAuditKind(SecQueryKind.DROP);
        srUserDomain.setSqlType(SecQueryType.DROP_USER);

        for (ParseTree child : ctx.user().children) {
            if (child instanceof IdentifierOrStringContext) {
                if (srUserDomain.getUser() == null) {
                    srUserDomain.setUser(getString(child.getText()));
                } else {
                    srUserDomain.setHost(getString(child.getText()));
                }
            }
        }

        factory.addDomain(srUserDomain);

        return null;
    }

    @Override
    public Void visitShowUserStatement(ShowUserStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        rdbResourceDomain.setNeedSupply(false);
        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitCreateRoleStatement(CreateRoleStatementContext ctx) {
        for (IdentifierOrStringContext identifierOrStringContext : ctx.roleList().identifierOrString()) {
            RdbRoleDomain rdbRoleDomain = new RdbRoleDomain();
            rdbRoleDomain.setAuditKind(SecQueryKind.CREATE);
            rdbRoleDomain.setSqlType(SecQueryType.CREATE_ROLE);
            rdbRoleDomain.setRole(getString(identifierOrStringContext.getText()));
            factory.addDomain(rdbRoleDomain);
        }
        return null;
    }

    @Override
    public Void visitDropRoleStatement(DropRoleStatementContext ctx) {
        for (IdentifierOrStringContext identifierOrStringContext : ctx.roleList().identifierOrString()) {
            RdbRoleDomain rdbRoleDomain = new RdbRoleDomain();
            rdbRoleDomain.setAuditKind(SecQueryKind.DROP);
            rdbRoleDomain.setSqlType(SecQueryType.DROP_ROLE);
            rdbRoleDomain.setRole(getString(identifierOrStringContext.getText()));
            factory.addDomain(rdbRoleDomain);
        }
        return null;
    }

    @Override
    public Void visitShowRolesStatement(ShowRolesStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        rdbResourceDomain.setNeedSupply(false);
        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitGrantOnTableBrief(GrantOnTableBriefContext ctx) {
        SrGrantDomain rdbRevokeDomain = new SrGrantDomain();
        rdbRevokeDomain.setAuditKind(SecQueryKind.ALTER);
        rdbRevokeDomain.setSqlType(SecQueryType.GRANT);
        if (ctx.grantRevokeClause().user() != null) {
            for (ParseTree child : ctx.grantRevokeClause().user().children) {
                if (child instanceof IdentifierOrStringContext) {
                    if (rdbRevokeDomain.getName() == null) {
                        rdbRevokeDomain.setName(getString(child.getText()));
                    } else {
                        rdbRevokeDomain.setHost(getString(child.getText()));
                    }
                }
            }
        } else {
            rdbRevokeDomain.setName(getString(ctx.grantRevokeClause().identifierOrString().getText()));
        }

        factory.addDomain(rdbRevokeDomain);
        return null;
    }

    @Override
    public Void visitRevokeOnTableBrief(RevokeOnTableBriefContext ctx) {
        SrRevokeDomain rdbRevokeDomain = new SrRevokeDomain();
        rdbRevokeDomain.setAuditKind(SecQueryKind.ALTER);
        rdbRevokeDomain.setSqlType(SecQueryType.REVOKE);
        if (ctx.grantRevokeClause().user() != null) {
            for (ParseTree child : ctx.grantRevokeClause().user().children) {
                if (child instanceof IdentifierOrStringContext) {
                    if (rdbRevokeDomain.getName() == null) {
                        rdbRevokeDomain.setName(getString(child.getText()));
                    } else {
                        rdbRevokeDomain.setHost(getString(child.getText()));
                    }
                }
            }
        } else {
            rdbRevokeDomain.setName(getString(ctx.grantRevokeClause().identifierOrString().getText()));
        }

        factory.addDomain(rdbRevokeDomain);
        return null;
    }

    @Override
    public Void visitShowGrantsStatement(ShowGrantsStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        rdbResourceDomain.setNeedSupply(false);
        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitSetUserVar(SetUserVarContext ctx) {
        String keyName = ctx.userVariable().identifierOrString().getText();

        SrConfigDomain domain = new SrConfigDomain(keyName, null);
        domain.setSqlType(SecQueryType.CONFIG_WRITE);
        domain.setAuditKind(SecQueryKind.OTHER);
        factory.addDomain(domain);
        return null;
    }

    @Override
    public Void visitSetSystemVar(SetSystemVarContext ctx) {
        VarTypeContext scope;

        SrScopeType scopeType = null;
        String keyName;
        if (ctx.identifier() != null) {
            scope = ctx.varType();
            keyName = ctx.identifier().getText();
        } else {
            scope = ctx.systemVariable().varType();
            keyName = ctx.systemVariable().identifier().getText();
        }

        if (scope != null) {
            if (scope.GLOBAL() != null) {
                scopeType = SrScopeType.GLOBAL;
            } else if (scope.LOCAL() != null) {
                scopeType = SrScopeType.LOCAL;
            } else if (scope.SESSION() != null) {
                scopeType = SrScopeType.SESSION;
            } else {
                throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
            }
        }

        SrConfigDomain domain = new SrConfigDomain(keyName, scopeType);
        domain.setSqlType(SecQueryType.CONFIG_WRITE);
        domain.setAuditKind(SecQueryKind.OTHER);
        factory.addDomain(domain);
        return null;
    }

    @Override
    public Void visitQueryStatement(QueryStatementContext ctx) {
        factory.enterSelectDomain();
        dmVisitChildren(ctx);
        factory.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitQueryRelation(QueryRelationContext ctx) {
        factory.enterSelectDomain();
        dmVisitChildren(ctx);
        factory.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitQueryNoWith(QueryNoWithContext ctx) {
        factory.enterSelectDomain();
        dmVisitChildren(ctx);
        factory.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitSetOperation(SetOperationContext ctx) {
        factory.addAttr(CommonAttribute.UNION, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQueryPrimaryDefault(QueryPrimaryDefaultContext ctx) {
        factory.enterSelectDomain();
        dmVisitChildren(ctx);
        factory.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitSortItem(SortItemContext ctx) {
        return null;
    }

    @Override
    public Void visitLimitElement(LimitElementContext ctx) {
        factory.addAttr(CommonAttribute.LIMIT, true);
        return null;
    }

    @Override
    public Void visitQuerySpecification(QuerySpecificationContext ctx) {
        factory.enterSelectDomain();
        for (SelectItemContext selectItemContext : ctx.selectItem()) {
            selectItemContext.accept(this);
        }
        ctx.fromClause().accept(this);

        if (ctx.where != null) {
            factory.enterWhere();
            ctx.where.accept(this);
            factory.exitWhere();
        }

        factory.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitFrom(FromContext ctx) {
        if (ctx.getChildCount() == 0) {
            return null;
        }
        factory.enterSelectFromBuilder();
        dmVisitChildren(ctx);
        factory.exitSelectFromBuilder();
        return null;
    }

    @Override
    public Void visitRelation(RelationContext ctx) {
        factory.enterSelectTableBuilder();
        dmVisitChildren(ctx);
        factory.exitSelectTableBuilder();
        return null;
    }

    @Override
    public Void visitCommonTableExpression(CommonTableExpressionContext ctx) {
        factory.enterWithSelect();
        dmVisitChildren(ctx);
        factory.exitWithSelect();
        return null;
    }

    @Override
    public Void visitSelectSingle(SelectSingleContext ctx) {
        factory.enterBuildSelectItem();

        factory.addAttr(CommonAttribute.VALUE, getText(ctx.expression()));
        ctx.expression().accept(this);

        if (ctx.identifier() != null) {
            factory.addAttr(CommonAttribute.ALIAS, getText(ctx.identifier()));
        } else if (ctx.string() != null) {
            factory.addAttr(CommonAttribute.ALIAS, getText(ctx.string()));
        }

        factory.exitBuildSelectItem();
        return null;
    }

    @Override
    public Void visitSelectAll(SelectAllContext ctx) {
        errorIfExist(ctx.excludeClause());
        factory.enterBuildSelectItem();
        RdbColumnDomain rdbColumnDomain = new RdbColumnDomain();
        rdbColumnDomain.setColumn("*");
        if (ctx.qualifiedName() != null) {
            if (ctx.qualifiedName().identifier().size() > 1) {
                throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
            }
            rdbColumnDomain.setTable(getName(getText(ctx.qualifiedName().identifier(0))));
        }
        factory.handleDomain(rdbColumnDomain, DomainSource.COLUMN);

        factory.exitBuildSelectItem();
        return null;
    }

    @Override
    public Void visitTableAtom(TableAtomContext ctx) {
        factory.enterSelectTableBuilder();
        ctx.qualifiedName().accept(this);
        if (ctx.alias != null) {
            factory.addAttr(CommonAttribute.ALIAS, ctx.alias.getText());
        }
        factory.exitSelectTableBuilder();
        return null;
    }

    @Override
    public Void visitSubqueryWithAlias(SubqueryWithAliasContext ctx) {
        factory.enterSelectTableBuilder();
        ctx.subquery().accept(this);

        if (ctx.identifier() == null) {
            throw new UnsupportedOperationException("subquery need alias" + this.getText(ctx));
        }
        factory.addAttr(CommonAttribute.ALIAS, ctx.identifier().getText());

        factory.exitSelectTableBuilder();
        return null;
    }

    @Override
    public Void visitCrossOrInnerJoinType(CrossOrInnerJoinTypeContext ctx) {
        factory.enterJoin(ctx.getText());
        factory.exitJoin();
        return null;
    }

    @Override
    public Void visitOuterAndSemiJoinType(OuterAndSemiJoinTypeContext ctx) {
        factory.enterJoin(ctx.getText());
        factory.exitJoin();
        return null;
    }

    @Override
    public Void visitJoinCriteria(JoinCriteriaContext ctx) {
        return null;
    }

    @Override
    public Void visitIsNull(IsNullContext ctx) {
        factory.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitInSubquery(InSubqueryContext ctx) {
        factory.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitInList(InListContext ctx) {
        factory.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDereference(DereferenceContext ctx) {
        factory.enterSelectColumn();
        factory.enterObjName();

        dmVisitChildren(ctx);
        factory.exitObjName();
        factory.exitSelectColumn();
        return null;
    }

    @Override
    public Void visitColumnRef(ColumnRefContext ctx) {
        if (ctx.parent instanceof DereferenceContext) {
            dmVisitChildren(ctx);
        } else {
            factory.enterSelectColumn();
            factory.enterObjName();

            factory.addAttr(CommonAttribute.VALUE, this.getText(ctx));
            factory.exitObjName();
            factory.exitSelectColumn();
        }
        return null;
    }

    @Override
    public Void visitComparison(ComparisonContext ctx) {
        String left = getText(ctx.left);
        String right = getText(ctx.right);
        if (left.startsWith("(")) {
            left = left.substring(1, left.length() - 1);
        }
        if (right.startsWith("(")) {
            right = right.substring(1, right.length() - 1);
        }
        if (!left.equals(right)) {
            factory.addAttr(CommonAttribute.VALID_WHERE, true);
        }
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCast(CastContext ctx) {
        factory.enterCall();
        factory.addAttr(CommonAttribute.VALUE, ctx.CAST().getText());

        factory.enterFunctionArgs();
        factory.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.expression()));
        ctx.expression().accept(this);
        factory.exitFunctionArgs();

        factory.exitCall();
        return null;
    }

    @Override
    public Void visitExists(ExistsContext ctx) {
        factory.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNullLiteral(NullLiteralContext ctx) {
        factory.handleDomain(new RdbConstantDomain("null"), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitNumericLiteral(NumericLiteralContext ctx) {
        factory.handleDomain(new RdbConstantDomain(ctx.getText()), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitStringLiteral(StringLiteralContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitString(StringContext ctx) {
        String text = ctx.getText();
        factory.handleDomain(new RdbConstantDomain(text.substring(1, text.length() - 1)), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitAggregationFunctionCall(AggregationFunctionCallContext ctx) {
        factory.enterCall();
        ctx.aggregationFunction().accept(this);
        factory.exitCall();
        return null;
    }

    @Override
    public Void visitMapConstructor(MapConstructorContext ctx) {
        for (MapExpressionContext mapExpressionContext : ctx.mapExpressionList().mapExpression()) {
            mapExpressionContext.accept(this);
        }
        return null;
    }

    @Override
    public Void visitMapExpression(MapExpressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRefreshMaterializedViewStatement(RefreshMaterializedViewStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.ALTER);
        rdbResourceDomain.setSqlType(SecQueryType.REFRESH_VIEW);
        rdbResourceDomain.setNeedSupply(true);
        List<String> strings = parseName(ctx.mvName);
        Map<UmiTypes, String> map = BuilderUtil.parseViewName(strings);
        rdbResourceDomain.setCatalog(map.get(UmiTypes.Catalog));
        rdbResourceDomain.setSchema(map.get(UmiTypes.Schema));
        rdbResourceDomain.setName(map.get(UmiTypes.View));
        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowStatusStatement(ShowStatusStatementContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setTarget(TargetType.Environment);
        rdbResourceDomain.setNeedSupply(false);
        factory.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitTupleInSubquery(TupleInSubqueryContext ctx) {
        factory.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCollectionSubscript(CollectionSubscriptContext ctx) {
        ctx.primaryExpression().accept(this);
        return null;
    }

    @Override
    public Void visitWindowFunctionCall(WindowFunctionCallContext ctx) {
        factory.enterCall();

        factory.enterObjName();
        factory.addAttr(CommonAttribute.VALUE, ctx.windowFunction().name.getText());
        factory.exitObjName();

        factory.enterFunctionArgs();
        for (ExpressionContext expressionContext : ctx.windowFunction().expression()) {
            factory.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(expressionContext));
            expressionContext.accept(this);
        }
        factory.exitFunctionArgs();
        factory.exitCall();
        return null;
    }

    @Override
    public Void visitSimpleFunctionCall(SimpleFunctionCallContext ctx) {
        factory.enterCall();

        ctx.qualifiedName().accept(this);
        factory.enterFunctionArgs();

        for (ExpressionContext expressionContext : ctx.expression()) {
            factory.addAttr(CommonAttribute.FUNC_ARG_NAME, getConstant(getText(expressionContext)));
            expressionContext.accept(this);
        }
        factory.exitFunctionArgs();
        factory.exitCall();
        return null;

    }

    private String getConstant(String text) {
        if (text.startsWith("'")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    @Override
    public Void visitAggregationFunction(AggregationFunctionContext ctx) {

        factory.addAttr(CommonAttribute.VALUE, ctx.getChild(0).getText());
        int size = ctx.expression().size();
        if (ctx.SEPARATOR() != null) {
            size = size - 1;
        }

        if (ctx.ASTERISK_SYMBOL() != null) {
            factory.enterFunctionArgs();
            factory.addAttr(CommonAttribute.FUNC_ARG_NAME, "*");
            factory.exitFunctionArgs();
        }

        for (int i = 0; i < size; i++) {
            ExpressionContext expressionContext = ctx.expression(i);
            factory.enterFunctionArgs();
            factory.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(expressionContext));
            expressionContext.accept(this);
            factory.exitFunctionArgs();
        }

        return null;
    }

    @Override
    public Void visitProperties(PropertiesContext ctx) {
        factory.enterOptionsBuilder();
        dmVisitChildren(ctx);
        factory.exitOptionsBuilder();
        return null;
    }

    @Override
    public Void visitPropertyList(PropertyListContext ctx) {
        factory.enterOptionsBuilder();
        dmVisitChildren(ctx);
        factory.exitOptionsBuilder();
        return null;
    }

    @Override
    public Void visitProperty(PropertyContext ctx) {
        String text = getText(ctx.key);
        String text1 = getText(ctx.value);
        KeyValueDomain domain = new KeyValueDomain(text.substring(1, text.length() - 1), text1.substring(1, text1.length() - 1));
        factory.handleDomain(domain, DomainSource.KEY_VALUE);
        return null;
    }

    @Override
    public Void visitComment(CommentContext ctx) {
        factory.addAttr(CommonAttribute.COMMENT, getString(ctx.string().getText()));
        return null;
    }

    private String getString(String text) {
        if (text.startsWith("'") || text.startsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    @Override
    public Void visitArrayType(ArrayTypeContext ctx) {
        ColumnTypeDomain columnTypeDomain = new ColumnTypeDomain(ctx.getChild(0).getText(), this.getText(ctx), null);
        factory.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);

        return null;
    }

    @Override
    public Void visitMapType(MapTypeContext ctx) {
        ColumnTypeDomain columnTypeDomain = new ColumnTypeDomain(ctx.getChild(0).getText(), this.getText(ctx), null);
        factory.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);
        return null;
    }

    @Override
    public Void visitStructType(StructTypeContext ctx) {
        ColumnTypeDomain columnTypeDomain = new ColumnTypeDomain(ctx.getChild(0).getText(), this.getText(ctx), null);
        factory.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);
        return null;
    }

    @Override
    public Void visitBaseType(BaseTypeContext ctx) {
        if (ctx.typeParameter() != null) {
            ColumnTypeDomain columnTypeDomain = new ColumnTypeDomain(ctx.getChild(0).getText(), this.getText(ctx), ctx.typeParameter().INTEGER_VALUE().getText());
            factory.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);
        } else {
            ColumnTypeDomain columnTypeDomain = new ColumnTypeDomain(ctx.getChild(0).getText(), this.getText(ctx), null);
            factory.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);
        }
        return null;
    }

    @Override
    public Void visitDecimalType(DecimalTypeContext ctx) {
        ColumnTypeDomain columnTypeDomain;
        if (ctx.precision != null) {
            columnTypeDomain = new ColumnTypeDomain(ctx.getChild(0).getText(), getText(ctx), ctx.precision.getText());
        } else {
            columnTypeDomain = new ColumnTypeDomain(ctx.getChild(0).getText(), getText(ctx), null);
        }
        factory.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);
        return null;
    }

    @Override
    public Void visitQualifiedName(QualifiedNameContext ctx) {
        factory.enterObjName();
        dmVisitChildren(ctx);
        factory.exitObjName();
        return null;
    }

    @Override
    public Void visitUnquotedIdentifier(UnquotedIdentifierContext ctx) {
        factory.addAttr(CommonAttribute.VALUE, this.getText(ctx));
        return null;
    }

    @Override
    public Void visitBackQuotedIdentifier(BackQuotedIdentifierContext ctx) {
        factory.addAttr(CommonAttribute.VALUE, this.getText(ctx));
        return null;
    }

    @Override
    public Void visitIdentifierList(IdentifierListContext ctx) {
        factory.enterColumnList();
        dmVisitChildren(ctx);
        factory.exitColumnList();
        return null;
    }

    @Override
    public Void visitIdentifierOrString(IdentifierOrStringContext ctx) {
        factory.addAttr(CommonAttribute.VALUE, this.getText(ctx));
        return null;
    }

    @Override
    public Void visitAssignment(AssignmentContext ctx) {
        factory.enterObjName();
        ctx.identifier().accept(this);
        factory.exitObjName();

        ctx.expressionOrDefault().accept(this);
        return null;
    }

    @Override
    public Void visitAssignmentList(AssignmentListContext ctx) {
        factory.enterSetColumnValue();
        dmVisitChildren(ctx);
        factory.exitSetColumnValue();
        return null;
    }

    @Override
    public Void visitStatement(StatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAlterClause(AlterClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitModifyPropertiesClause(ModifyPropertiesClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSetStatement(SetStatementContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWithClause(WithClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQueryWithParentheses(QueryWithParenthesesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSubquery(SubqueryContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSetQuantifier(SetQuantifierContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRelations(RelationsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitParenthesizedRelation(ParenthesizedRelationContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitJoinRelation(JoinRelationContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnAliases(ColumnAliasesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpressionsWithDefault(ExpressionsWithDefaultContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpressionOrDefault(ExpressionOrDefaultContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpressionDefault(ExpressionDefaultContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitLogicalBinary(LogicalBinaryContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpressionList(ExpressionListContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBooleanExpressionDefault(BooleanExpressionDefaultContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPredicate(PredicateContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitValueExpressionDefault(ValueExpressionDefaultContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitArithmeticBinary(ArithmeticBinaryContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitLiteral(LiteralContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitParenthesizedExpression(ParenthesizedExpressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFunctionCallExpression(FunctionCallExpressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSubqueryExpression(SubqueryExpressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnReference(ColumnReferenceContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitComparisonOperator(ComparisonOperatorContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitType(TypeContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAlterViewStatement(AlterViewStatementContext ctx) {
        factory.enterAlterView();
        ctx.qualifiedName().accept(this);
        factory.exitAlterView();
        return null;
    }

    @Override
    public Void visitAlterMaterializedViewStatement(AlterMaterializedViewStatementContext ctx) {
        factory.enterAlterView();
        factory.addAttr(CommonAttribute.MATERIALIZED, true);
        ctx.qualifiedName().accept(this);
        factory.exitAlterView();
        return null;
    }

    @Override
    public Void visitSimpleCase(SimpleCaseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSearchedCase(SearchedCaseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWhenClause(WhenClauseContext ctx) {
        dmVisitChildren(ctx);
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
