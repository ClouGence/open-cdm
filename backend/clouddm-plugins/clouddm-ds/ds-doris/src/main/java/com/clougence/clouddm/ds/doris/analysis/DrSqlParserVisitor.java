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
package com.clougence.clouddm.ds.doris.analysis;

import static com.clougence.clouddm.ds.doris.parser.antlr.DorisParser.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.clougence.clouddm.ds.doris.analysis.builder.DrBuilderFactory;
import com.clougence.clouddm.ds.doris.analysis.enums.DrAttribute;
import com.clougence.clouddm.ds.doris.analysis.secrules.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ColumnTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.KeyValueDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.enums.MyAttribute;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.doris.parser.antlr.DorisParserBaseVisitor;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;

public class DrSqlParserVisitor extends DorisParserBaseVisitor<Void> {

    private final DrBuilderFactory builder;
    private final Parser           parser;

    public DrSqlParserVisitor(DrBuilderFactory builder, Parser parser){
        this.builder = builder;
        this.parser = parser;
    }

    private String getText(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private String getText(Token start, Token end) {
        return parser.getTokenStream().getText(start, end);
    }

    @Override
    public Void visitCallProcedure(CallProcedureContext ctx) {
        throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        //        builder.enterCall();
        //        ctx.multipartIdentifier().accept(this);
        //        if (CollectionUtils.isNotEmpty(ctx.expression())) {
        //            builder.enterFunctionArgs();
        //            for (ExpressionContext expressionContext : ctx.expression()) {
        //                expressionContext.accept(this);
        //                String text = this.getText(expressionContext);
        //                if (text.startsWith("'")) {
        //                    text = text.substring(1, text.length() - 1);
        //                }
        //                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, text);
        //            }
        //            builder.exitFunctionArgs();
        //        }
        //        builder.exitCall();
        //        return null;
    }

    @Override
    public Void visitRefreshMTMV(RefreshMTMVContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.mvName.accept(this);
            });
        }, SecQueryType.REFRESH_VIEW, SecQueryKind.ALTER, true, TargetType.View);
        return null;
    }

    @Override
    public Void visitAlterMTMV(AlterMTMVContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.mvName.accept(this);
            });
        }, SecQueryType.ALTER_VIEW, SecQueryKind.ALTER, true, TargetType.View);
        return null;
    }

    @Override
    public Void visitCreateMTMV(CreateMTMVContext ctx) {
        RdbViewDomain rdbViewDomain = new RdbViewDomain();
        rdbViewDomain.setAuditKind(SecQueryKind.CREATE);
        rdbViewDomain.setSqlType(SecQueryType.CREATE_VIEW);
        List<String> list = new ArrayList<>();
        for (ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.multipartIdentifier().errorCapturingIdentifier()) {
            list.add(getName(errorCapturingIdentifierContext));
        }
        Map<UmiTypes, String> map = BuilderUtil.parseViewName(list);
        rdbViewDomain.setCatalog(map.get(UmiTypes.Catalog));
        rdbViewDomain.setSchema(map.get(UmiTypes.Schema));
        rdbViewDomain.setView(map.get(UmiTypes.View));

        rdbViewDomain.setMaterialized(true);
        builder.addDomain(rdbViewDomain);
        return null;
    }

    @Override
    public Void visitSearchedCase(SearchedCaseContext ctx) {
        for (WhenClauseContext whenClauseContext : ctx.whenClause()) {
            builder.handleOtherDomains(() -> {
                whenClauseContext.condition.accept(this);
            });
            whenClauseContext.result.accept(this);
        }
        if (ctx.elseExpression != null) {
            ctx.elseExpression.accept(this);
        }
        return null;
    }

    @Override
    public Void visitSimpleCase(SimpleCaseContext ctx) {
        builder.handleOtherDomains(() -> {
            ctx.value.accept(this);
        });
        for (WhenClauseContext whenClauseContext : ctx.whenClause()) {
            builder.handleOtherDomains(() -> {
                whenClauseContext.condition.accept(this);
            });
            whenClauseContext.result.accept(this);
        }
        if (ctx.elseExpression != null) {
            ctx.elseExpression.accept(this);
        }
        return null;
    }

    @Override
    public Void visitIntervalLiteral(IntervalLiteralContext ctx) {
        builder.handleDomain(new RdbConstantDomain(getText(ctx)), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitInsertTable(InsertTableContext ctx) {
        if (ctx.tableId != null) {
            throw new UnsupportedOperationException("unsupported SQL: " + ctx.DORIS_INTERNAL_TABLE_ID() + ctx.LEFT_PAREN() + ctx.tableId + ctx.RIGHT_PAREN());
        }

        errorIfExist(ctx.explain());
        errorIfExist(ctx.cte());
        errorIfExist(ctx.hints);
        builder.enterInsertBuilder();

        builder.enterObjName(NameType.TABLE);
        ctx.tableName.accept(this);
        builder.exitObjName();

        if (ctx.cols != null) {
            for (ErrorCapturingIdentifierContext column : ctx.cols.identifierSeq().errorCapturingIdentifier()) {
                builder.handleDomain(new RdbConstantDomain(getName(column.identifier())), DomainSource.INSERT_COLUMN);
            }
            ctx.cols.accept(this);
        }
        if (ctx.query().queryTerm().getChild(0).getChild(0) instanceof InlineTableContext) {
            InlineTableContext inlineTable = (InlineTableContext) ctx.query().queryTerm().getChild(0).getChild(0);
            if (inlineTable.rowConstructor().size() > 1) {
                builder.addAttr(CommonAttribute.MULTI_VALUE, true);
            }
        }
        ctx.query().accept(this);

        builder.exitInsertBuilder();

        return null;

    }

    @Override
    public Void visitUpdate(UpdateContext ctx) {
        builder.enterUpdate();

        visitIfExist(ctx.explain());
        visitIfExist(ctx.cte());

        builder.enterObjName(NameType.TABLE);
        ctx.tableName.accept(this);
        builder.exitObjName();

        ctx.updateAssignmentSeq().accept(this);

        errorIfExist(ctx.fromClause());
        visitIfExist(ctx.whereClause());

        builder.exitUpdate();
        return null;
    }

    private void visitIfExist(ParserRuleContext ctx) {
        if (ctx != null) {
            ctx.accept(this);
        }
    }

    private void errorIfExist(ParserRuleContext ctx) {
        if (ctx != null) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }
    }

    @Override
    public Void visitDelete(DeleteContext ctx) {
        if (ctx.relations() != null || ctx.explain() != null || ctx.cte() != null) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }
        builder.enterDelete();

        builder.enterObjName(NameType.TABLE);
        ctx.tableName.accept(this);
        builder.exitObjName();

        visitIfExist(ctx.whereClause());

        builder.exitDelete();

        return null;
    }

    @Override
    public Void visitTruncateTable(TruncateTableContext ctx) {
        RdbTableDomain rdbTableDomain = new DrTableDomain();
        rdbTableDomain.setSqlType(SecQueryType.TRUNCATE);
        rdbTableDomain.setAuditKind(SecQueryKind.DML);

        List<String> names = new ArrayList<>();
        for (ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.multipartIdentifier().errorCapturingIdentifier()) {
            names.add(getName(errorCapturingIdentifierContext.identifier()));
        }
        if (names.size() == 1) {
            rdbTableDomain.setTable(names.get(0));
        } else if (names.size() == 2) {
            rdbTableDomain.setSchema(names.get(0));
            rdbTableDomain.setTable(names.get(1));
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx.multipartIdentifier()));
        }

        builder.addDomain(rdbTableDomain);
        return null;
    }

    @Override
    public Void visitCreateTable(CreateTableContext ctx) {
        builder.enterCreateTable(SecQueryType.CREATE_TABLE);
        builder.enterObjName(NameType.TABLE);
        ctx.name.accept(this);
        builder.exitObjName();

        if (ctx.COMMENT() != null) {
            builder.addAttr(CommonAttribute.COMMENT, getText(ctx.STRING_LITERAL().getText()));
        }

        if (ctx.EXTERNAL() != null) {
            builder.addAttr(DrAttribute.EXTERNAL, true);
        }

        if (ctx.EXISTS() != null) {
            builder.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
        }

        if (ctx.TEMPORARY() != null) {
            builder.addAttr(MyAttribute.TEMPORARY, true);
        }

        if (ctx.engine != null) {
            builder.addAttr(MyAttribute.ENGINE, getText(getName(ctx.engine)));
        }

        if (ctx.UNIQUE() != null) {
            builder.addAttr(DrAttribute.TABLE_MODEL, ctx.UNIQUE().getText());
        } else if (ctx.AGGREGATE() != null) {
            builder.addAttr(DrAttribute.TABLE_MODEL, ctx.AGGREGATE().getText());
        } else if (ctx.DUPLICATE() != null) {
            builder.addAttr(DrAttribute.TABLE_MODEL, ctx.DUPLICATE().getText());
        }

        visitIfExist(ctx.columnDefs());
        visitIfExist(ctx.indexDefs());
        visitIfExist(ctx.properties);
        visitIfExist(ctx.extProperties);
        visitIfExist(ctx.query());

        builder.exitCreateTable();
        return null;
    }

    private String getText(String text) {
        if (text.startsWith("'") || text.startsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    @Override
    public Void visitCreateView(CreateViewContext ctx) {
        RdbViewDomain rdbViewDomain = new RdbViewDomain();
        rdbViewDomain.setAuditKind(SecQueryKind.CREATE);
        rdbViewDomain.setSqlType(SecQueryType.CREATE_VIEW);
        List<String> list = new ArrayList<>();
        for (ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.multipartIdentifier().errorCapturingIdentifier()) {
            list.add(getName(errorCapturingIdentifierContext));
        }
        Map<UmiTypes, String> map = BuilderUtil.parseViewName(list);
        rdbViewDomain.setCatalog(map.get(UmiTypes.Catalog));
        rdbViewDomain.setSchema(map.get(UmiTypes.Schema));
        rdbViewDomain.setView(map.get(UmiTypes.View));

        builder.addDomain(rdbViewDomain);
        return null;
    }

    @Override
    public Void visitShowProcedureStatus(ShowProcedureStatusContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain(SecQueryType.SHOW, SecQueryKind.OTHER);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowConfig(ShowConfigContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitSupportedRefreshStatementAlias(SupportedRefreshStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRefreshTable(RefreshTableContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.name.accept(this);
            });
        }, SecQueryType.REFRESH_TABLE, SecQueryKind.ALTER, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitRefreshDatabase(RefreshDatabaseContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.name.accept(this);
            }, NameType.SCHEMA);
        }, SecQueryType.REFRESH_SCHEMA, SecQueryKind.ALTER, true, TargetType.Schema);
        return null;
    }

    @Override
    public Void visitRefreshLdap(RefreshLdapContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitSupportedLoadStatementAlias(SupportedLoadStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSync(SyncContext ctx) {
        return null;
    }

    @Override
    public Void visitShowCreateRoutineLoad(ShowCreateRoutineLoadContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.SHOW, SecQueryKind.OTHER, true, TargetType.Unknown));
        return null;
    }

    @Override
    public Void visitCreateRoutineLoadAlias(CreateRoutineLoadAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitMysqlLoad(MysqlLoadContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.mysqlDataDesc().tableName.accept(this);
            });
        }, SecQueryType.LOAD, SecQueryKind.DML, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitPauseRoutineLoad(PauseRoutineLoadContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.label.accept(this);
            });
        }, SecQueryType.ALTER_OBJECT, SecQueryKind.ALTER, true, TargetType.Unknown);
        return null;
    }

    @Override
    public Void visitDropWorkloadGroup(DropWorkloadGroupContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitDropWorkloadPolicy(DropWorkloadPolicyContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitDropFile(DropFileContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitDropStoragePolicy(DropStoragePolicyContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitDropSqlBlockRule(DropSqlBlockRuleContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitDropCatalogRecycleBin(DropCatalogRecycleBinContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitDropEncryptkey(DropEncryptkeyContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitAlterColocateGroup(AlterColocateGroupContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitAlterRoutineLoad(AlterRoutineLoadContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.name.accept(this);
            });
        }, SecQueryType.ALTER_OBJECT, SecQueryKind.ALTER, true, TargetType.Unknown);
        return null;
    }

    @Override
    public Void visitPauseAllRoutineLoad(PauseAllRoutineLoadContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ALTER_OBJECT, SecQueryKind.ALTER, true, TargetType.Unknown));
        return null;
    }

    @Override
    public Void visitResumeAllRoutineLoad(ResumeAllRoutineLoadContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ALTER_OBJECT, SecQueryKind.ALTER, true, TargetType.Unknown));
        return null;
    }

    @Override
    public Void visitShowRoutineLoad(ShowRoutineLoadContext ctx) {
        if (ctx.label != null) {
            builder.handleResource(() -> {
                builder.handleObjName(() -> {
                    ctx.label.accept(this);
                });
            }, SecQueryType.SHOW, SecQueryKind.QUERY, true, TargetType.Unknown);
        } else {
            RdbResourceDomain rdbResourceDomain = new RdbResourceDomain(SecQueryType.SHOW, SecQueryKind.QUERY, true, TargetType.Unknown);
            builder.addDomain(rdbResourceDomain);
        }
        return null;
    }

    @Override
    public Void visitSupportedRecoverStatementAlias(SupportedRecoverStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRecoverDatabase(RecoverDatabaseContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain(SecQueryType.RECOVER, SecQueryKind.ALTER, true, TargetType.Schema);
        rdbResourceDomain.setSchema(getName(ctx.name));
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitRecoverTable(RecoverTableContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.name.accept(this);
            });
        }, SecQueryType.RECOVER, SecQueryKind.ALTER, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitRecoverPartition(RecoverPartitionContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.tableName.accept(this);
            });
        }, SecQueryType.RECOVER, SecQueryKind.ALTER, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitSupportedKillStatementAlias(SupportedKillStatementAliasContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain(SecQueryType.KILL, SecQueryKind.ADMIN);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowRoutineLoadTask(ShowRoutineLoadTaskContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain(SecQueryType.SHOW, SecQueryKind.QUERY, true, TargetType.Unknown);
        if (ctx.database != null) {
            rdbResourceDomain.setSchema(getName(ctx.database));
        }
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitStopRoutineLoad(StopRoutineLoadContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.label.accept(this);
            });
        }, SecQueryType.ALTER_OBJECT, SecQueryKind.ALTER, true, TargetType.Unknown);
        return null;
    }

    @Override
    public Void visitResumeRoutineLoad(ResumeRoutineLoadContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.label.accept(this);
            });
        }, SecQueryType.ALTER_OBJECT, SecQueryKind.ALTER, true, TargetType.Unknown);
        return null;
    }

    @Override
    public Void visitCreateRoutineLoad(CreateRoutineLoadContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.label.accept(this);
            });
        }, SecQueryType.CREATE_OBJECT, SecQueryKind.CREATE, true, TargetType.Unknown);
        return null;
    }

    @Override
    public Void visitRefreshCatalog(RefreshCatalogContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain(SecQueryType.REFRESH_CATALOG, SecQueryKind.ALTER, false, TargetType.Catalog);
        rdbResourceDomain.setName(getName(ctx.name));
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitSupportedUnsetStatementAlias(SupportedUnsetStatementAliasContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitSupportedCleanStatementAlias(SupportedCleanStatementAliasContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitCreateTableLike(CreateTableLikeContext ctx) {
        builder.enterCreateTable(SecQueryType.CREATE_TABLE_LIKE);

        builder.enterObjName(NameType.TABLE);
        ctx.name.accept(this);
        builder.exitObjName();

        builder.enterObjName(NameType.TABLE);
        ctx.existedTable.accept(this);
        builder.exitObjName();

        builder.exitCreateTable();
        return null;
    }

    @Override
    public Void visitCreateRole(CreateRoleContext ctx) {
        builder.enterCreateRole();

        builder.enterObjName();
        ctx.name.accept(this);
        builder.exitObjName();

        builder.exitCreateRole();
        return null;
    }

    @Override
    public Void visitCreateRowPolicy(CreateRowPolicyContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.table.accept(this);
            });
        }, SecQueryType.ADMIN, SecQueryKind.ADMIN, true, TargetType.Unknown);
        return null;
    }

    @Override
    public Void visitCreateCatalog(CreateCatalogContext ctx) {
        builder.enterCreateCatalog();

        builder.enterObjName();
        ctx.catalogName.accept(this);
        builder.exitObjName();

        visitIfExist(ctx.properties);

        if (ctx.COMMENT() != null) {
            builder.addAttr(CommonAttribute.COMMENT, getComment(ctx.STRING_LITERAL().getText()));
        }

        builder.exitCreateCatalog();
        return null;
    }

    @Override
    public Void visitCreateWorkloadPolicy(CreateWorkloadPolicyContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitCreateSqlBlockRule(CreateSqlBlockRuleContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitCreateEncryptkey(CreateEncryptkeyContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitCreateIndex(CreateIndexContext ctx) {
        RdbIndexDomain indexDomain = new RdbIndexDomain();

        indexDomain.setAuditKind(SecQueryKind.CREATE);
        indexDomain.setSqlType(SecQueryType.CREATE_INDEX);
        indexDomain.setType("index");
        indexDomain.setName(getName(ctx.name));

        List<String> list = new ArrayList<>();
        for (ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.multipartIdentifier().errorCapturingIdentifier()) {
            list.add(getName(errorCapturingIdentifierContext));
        }
        Map<UmiTypes, String> map = BuilderUtil.parseTableName(list);
        indexDomain.setCatalog(map.get(UmiTypes.Catalog));
        indexDomain.setTableSchema(map.get(UmiTypes.Schema));
        indexDomain.setTableName(map.get(UmiTypes.Table));

        indexDomain.setColumns(new ArrayList<>());
        for (ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.identifierList().identifierSeq().errorCapturingIdentifier()) {
            indexDomain.getColumns().add(getName(errorCapturingIdentifierContext.identifier()));
        }
        if (ctx.STRING_LITERAL() != null) {
            indexDomain.setComment(getText(ctx.STRING_LITERAL().getText()));
        }
        builder.addDomain(indexDomain);
        return null;
    }

    @Override
    public Void visitCreateUserDefineFunction(CreateUserDefineFunctionContext ctx) {
        RdbFunctionDomain rdbFunctionDomain = new RdbFunctionDomain();
        rdbFunctionDomain.setAuditKind(SecQueryKind.CREATE);
        rdbFunctionDomain.setSqlType(SecQueryType.CREATE_FUNCTION);

        if (ctx.functionIdentifier().dbName != null) {
            rdbFunctionDomain.setSchema(getName(ctx.functionIdentifier().dbName));
        }
        rdbFunctionDomain.setName(getName(ctx.functionIdentifier().functionNameIdentifier()));

        builder.addDomain(rdbFunctionDomain);
        return null;
    }

    @Override
    public Void visitCreateAliasFunction(CreateAliasFunctionContext ctx) {
        builder.enterCreateFunction();
        ctx.functionIdentifier().accept(this);
        builder.exitCreateFunction();
        return null;
    }

    @Override
    public Void visitCreateUser(CreateUserContext ctx) {
        builder.enterCreateUser();

        if (ctx.grantUserIdentify().STRING_LITERAL() != null) {
            builder.addAttr(CommonAttribute.PASSWORD, getText(ctx.grantUserIdentify().STRING_LITERAL().getText()));
        }

        builder.enterObjName();
        ctx.grantUserIdentify().accept(this);
        builder.exitObjName();

        builder.exitCreateUser();
        return null;
    }

    @Override
    public Void visitCreateDatabase(CreateDatabaseContext ctx) {
        builder.enterCreateSchema();
        if (ctx.IF() != null) {
            builder.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
        }

        builder.enterObjName();
        ctx.name.accept(this);
        builder.exitObjName();

        visitIfExist(ctx.propertyClause());

        builder.exitCreateSchema();

        return null;
    }

    @Override
    public Void visitAlterRole(AlterRoleContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitAlterCatalogProperties(AlterCatalogPropertiesContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain(SecQueryType.ALTER_CATALOG, SecQueryKind.ALTER, false, TargetType.Catalog);
        rdbResourceDomain.setCatalog(getName(ctx.name));
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitAlterWorkloadPolicy(AlterWorkloadPolicyContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitAlterSqlBlockRule(AlterSqlBlockRuleContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitAlterStorageVault(AlterStorageVaultContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitAlterCatalogRename(AlterCatalogRenameContext ctx) {
        builder.enterRename(TargetType.Catalog);

        builder.enterObjName();
        ctx.name.accept(this);
        builder.exitObjName();

        builder.enterObjName();
        ctx.newName.accept(this);
        builder.exitObjName();

        builder.exitRename();
        return null;
    }

    @Override
    public Void visitAlterCatalogComment(AlterCatalogCommentContext ctx) {
        DrCatalogDomain domain = new DrCatalogDomain();
        domain.setSqlType(SecQueryType.ALTER_CATALOG);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setCatalog(getName(ctx.name));
        String text = ctx.comment.getText();
        domain.setComment(getComment(text));
        builder.addDomain(domain);
        return null;
    }

    private String getComment(String comment) {
        if (comment.startsWith("\"") || comment.startsWith("'")) {
            return comment.substring(1, comment.length() - 1);
        }
        return comment;
    }

    @Override
    public Void visitAlterDatabaseRename(AlterDatabaseRenameContext ctx) {
        builder.enterRename(TargetType.Schema);
        dmVisitChildren(ctx);
        builder.exitRename();
        return null;
    }

    @Override
    public Void visitAlterStoragePolicy(AlterStoragePolicyContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitAlterTable(AlterTableContext ctx) {
        builder.enterAlterTable();

        builder.enterObjName(NameType.TABLE);
        ctx.tableName.accept(this);
        builder.exitObjName();

        for (AlterTableClauseContext alterTableClauseContext : ctx.alterTableClause()) {
            alterTableClauseContext.accept(this);
        }

        builder.exitAlterTable();
        return null;
    }

    @Override
    public Void visitAlterTableAddRollup(AlterTableAddRollupContext ctx) {
        DrTableDomain drTableDomain = new DrTableDomain();
        drTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        drTableDomain.setAuditKind(SecQueryKind.ALTER);
        List<String> list = new ArrayList<>();
        for (ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.multipartIdentifier().errorCapturingIdentifier()) {
            list.add(getName(errorCapturingIdentifierContext));
        }
        Map<UmiTypes, String> map = BuilderUtil.parseTableName(list);
        drTableDomain.setCatalog(map.get(UmiTypes.Catalog));
        drTableDomain.setSchema(map.get(UmiTypes.Schema));
        drTableDomain.setTable(map.get(UmiTypes.Table));
        builder.addDomain(drTableDomain);
        return null;
    }

    @Override
    public Void visitAlterTableDropRollup(AlterTableDropRollupContext ctx) {
        DrTableDomain drTableDomain = new DrTableDomain();
        drTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        drTableDomain.setAuditKind(SecQueryKind.ALTER);
        List<String> list = new ArrayList<>();
        for (ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.multipartIdentifier().errorCapturingIdentifier()) {
            list.add(getName(errorCapturingIdentifierContext));
        }
        Map<UmiTypes, String> map = BuilderUtil.parseTableName(list);
        drTableDomain.setCatalog(map.get(UmiTypes.Catalog));
        drTableDomain.setSchema(map.get(UmiTypes.Schema));
        drTableDomain.setTable(map.get(UmiTypes.Table));
        builder.addDomain(drTableDomain);
        return null;
    }

    // todo option
    @Override
    public Void visitAlterTableProperties(AlterTablePropertiesContext ctx) {
        DrTableDomain drTableDomain = new DrTableDomain();
        drTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        drTableDomain.setAuditKind(SecQueryKind.ALTER);
        List<String> list = new ArrayList<>();
        for (ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.multipartIdentifier().errorCapturingIdentifier()) {
            list.add(getName(errorCapturingIdentifierContext));
        }
        Map<UmiTypes, String> map = BuilderUtil.parseTableName(list);
        drTableDomain.setCatalog(map.get(UmiTypes.Catalog));
        drTableDomain.setSchema(map.get(UmiTypes.Schema));
        drTableDomain.setTable(map.get(UmiTypes.Table));
        builder.addDomain(drTableDomain);
        return null;
    }

    @Override
    public Void visitAlterResource(AlterResourceContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ALTER_OBJECT, SecQueryKind.ALTER));
        return null;
    }

    @Override
    public Void visitAlterDatabaseProperties(AlterDatabasePropertiesContext ctx) {
        builder.enterAlterSchema();

        builder.enterObjName();
        ctx.name.accept(this);
        builder.exitObjName();

        visitIfExist(ctx.propertyItemList());

        builder.exitAlterSchema();
        return null;
    }

    @Override
    public Void visitPropertyItemList(PropertyItemListContext ctx) {
        builder.enterOptionsBuilder();
        dmVisitChildren(ctx);
        builder.exitOptionsBuilder();
        return null;
    }

    @Override
    public Void visitDropRole(DropRoleContext ctx) {
        builder.enterDropRole();

        builder.enterObjName();
        ctx.name.accept(this);
        builder.exitObjName();

        builder.exitDropRole();
        return null;
    }

    @Override
    public Void visitDropUser(DropUserContext ctx) {
        DrUserDomain drUserDomain = new DrUserDomain();
        drUserDomain.setSqlType(SecQueryType.DROP_USER);
        drUserDomain.setAuditKind(SecQueryKind.DROP);
        drUserDomain.setUser(getText(this.getText(ctx.userIdentify().user)));
        if (ctx.userIdentify().host != null) {
            drUserDomain.setHost(getText(this.getText(ctx.userIdentify().host)));
        }
        builder.addDomain(drUserDomain);
        return null;
    }

    @Override
    public Void visitDropCatalog(DropCatalogContext ctx) {
        builder.enterDropCatalog();

        if (ctx.IF() != null) {
            builder.addAttr(CommonAttribute.IF_EXISTS, true);
        }

        builder.enterObjName();
        ctx.name.accept(this);
        builder.exitObjName();

        builder.exitDropCatalog();
        return null;
    }

    @Override
    public Void visitDropTable(DropTableContext ctx) {
        builder.enterDropTable();
        builder.enterObjName(NameType.TABLE);
        ctx.name.accept(this);
        builder.exitObjName();
        builder.exitDropTable();
        return null;
    }

    @Override
    public Void visitDropDatabase(DropDatabaseContext ctx) {
        builder.enterDropSchema();
        if (ctx.EXISTS() != null) {
            builder.addAttr(CommonAttribute.IF_EXISTS, true);
        }

        if (ctx.FORCE() != null) {
            builder.addAttr(DrAttribute.FORCE, true);
        }

        builder.enterObjName();
        ctx.name.accept(this);
        builder.exitObjName();

        builder.exitDropSchema();
        return null;
    }

    @Override
    public Void visitDropIndex(DropIndexContext ctx) {
        RdbIndexDomain indexDomain = new RdbIndexDomain();
        indexDomain.setAuditKind(SecQueryKind.DROP);
        indexDomain.setSqlType(SecQueryType.DROP_INDEX);

        indexDomain.setName(getName(ctx.name));
        List<String> list = new ArrayList<>();
        for (ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.multipartIdentifier().errorCapturingIdentifier()) {
            list.add(getName(errorCapturingIdentifierContext));
        }
        Map<UmiTypes, String> map = BuilderUtil.parseTableName(list);
        indexDomain.setTableCatalog(map.get(UmiTypes.Catalog));
        indexDomain.setTableSchema(map.get(UmiTypes.Schema));
        indexDomain.setTableName(map.get(UmiTypes.Table));

        builder.addDomain(indexDomain);
        return null;
    }

    @Override
    public Void visitSupportedShowStatementAlias(SupportedShowStatementAliasContext ctx) {
        DrShowDomain drShowDomain = new DrShowDomain();
        drShowDomain.setSqlType(SecQueryType.SHOW);
        drShowDomain.setAuditKind(SecQueryKind.QUERY);
        builder.addDomain(drShowDomain);
        return null;
    }

    @Override
    public Void visitCancelAlterTable(CancelAlterTableContext ctx) {
        DrTableDomain drTableDomain = new DrTableDomain();
        drTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        drTableDomain.setAuditKind(SecQueryKind.ALTER);

        List<String> names = new ArrayList<>();
        for (ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.tableName.errorCapturingIdentifier()) {
            names.add(getName(errorCapturingIdentifierContext.identifier()));
        }
        if (names.size() == 1) {
            drTableDomain.setTable(names.get(0));
        } else if (names.size() == 2) {
            drTableDomain.setSchema(names.get(0));
            drTableDomain.setTable(names.get(1));
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx.tableName));
        }
        builder.addDomain(drTableDomain);
        return null;
    }

    @Override
    public Void visitGrantTablePrivilege(GrantTablePrivilegeContext ctx) {
        DrGrantDomain drGrantDomain = new DrGrantDomain();
        drGrantDomain.setSqlType(SecQueryType.GRANT);
        drGrantDomain.setAuditKind(SecQueryKind.ALTER);

        if (ctx.userIdentify() != null) {
            drGrantDomain.setName(getText(this.getText(ctx.userIdentify().user)));
            if (ctx.userIdentify().host != null) {
                drGrantDomain.setHost(getText(this.getText(ctx.userIdentify().host)));
            }
        } else {
            drGrantDomain.setName(getText(this.getText(ctx.identifierOrText())));
        }

        builder.addDomain(drGrantDomain);
        return null;
    }

    @Override
    public Void visitGrantResourcePrivilege(GrantResourcePrivilegeContext ctx) {
        DrGrantDomain drGrantDomain = new DrGrantDomain();
        drGrantDomain.setSqlType(SecQueryType.GRANT);
        drGrantDomain.setAuditKind(SecQueryKind.ALTER);

        if (ctx.userIdentify() != null) {
            drGrantDomain.setName(getText(this.getText(ctx.userIdentify().user)));
            if (ctx.userIdentify().host != null) {
                drGrantDomain.setHost(getText(this.getText(ctx.userIdentify().host)));
            }
        } else {
            drGrantDomain.setName(getText(this.getText(ctx.identifierOrText())));
        }

        builder.addDomain(drGrantDomain);
        return null;
    }

    @Override
    public Void visitGrantRole(GrantRoleContext ctx) {
        DrGrantDomain drGrantDomain = new DrGrantDomain();
        drGrantDomain.setSqlType(SecQueryType.GRANT);
        drGrantDomain.setAuditKind(SecQueryKind.ALTER);

        drGrantDomain.setName(getText(this.getText(ctx.userIdentify().user)));
        if (ctx.userIdentify().host != null) {
            drGrantDomain.setHost(getText(this.getText(ctx.userIdentify().host)));
        }

        builder.addDomain(drGrantDomain);
        return null;
    }

    @Override
    public Void visitRevokeRole(RevokeRoleContext ctx) {
        DrRevokeDomain drGrantDomain = new DrRevokeDomain();
        drGrantDomain.setSqlType(SecQueryType.REVOKE);
        drGrantDomain.setAuditKind(SecQueryKind.ALTER);

        drGrantDomain.setName(getText(this.getText(ctx.userIdentify().user)));
        if (ctx.userIdentify().host != null) {
            drGrantDomain.setHost(getText(this.getText(ctx.userIdentify().host)));
        }

        builder.addDomain(drGrantDomain);
        return null;
    }

    @Override
    public Void visitRevokeResourcePrivilege(RevokeResourcePrivilegeContext ctx) {
        DrRevokeDomain drGrantDomain = new DrRevokeDomain();
        drGrantDomain.setSqlType(SecQueryType.REVOKE);
        drGrantDomain.setAuditKind(SecQueryKind.ALTER);

        if (ctx.userIdentify() != null) {
            drGrantDomain.setName(getText(this.getText(ctx.userIdentify().user)));
            if (ctx.userIdentify().host != null) {
                drGrantDomain.setHost(getText(this.getText(ctx.userIdentify().host)));
            }
        } else {
            drGrantDomain.setName(getText(this.getText(ctx.identifierOrText())));
        }

        builder.addDomain(drGrantDomain);
        return null;
    }

    @Override
    public Void visitRevokeTablePrivilege(RevokeTablePrivilegeContext ctx) {
        DrRevokeDomain drGrantDomain = new DrRevokeDomain();
        drGrantDomain.setSqlType(SecQueryType.REVOKE);
        drGrantDomain.setAuditKind(SecQueryKind.ALTER);

        if (ctx.userIdentify() != null) {
            drGrantDomain.setName(getText(this.getText(ctx.userIdentify().user)));
            if (ctx.userIdentify().host != null) {
                drGrantDomain.setHost(getText(this.getText(ctx.userIdentify().host)));
            }
        } else {
            drGrantDomain.setName(getText(this.getText(ctx.identifierOrText())));
        }

        builder.addDomain(drGrantDomain);
        return null;
    }

    @Override
    public Void visitAddColumnClause(AddColumnClauseContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ADD_COLUMN);
        ctx.columnDef().accept(this);
        visitIfExist(ctx.properties);
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitAddColumnsClause(AddColumnsClauseContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ADD_COLUMN);
        ctx.columnDefs().accept(this);
        visitIfExist(ctx.properties);
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitDropColumnClause(DropColumnClauseContext ctx) {
        builder.enterAlterTableItem(AlterTableType.DROP_COLUMN);
        DrColumnDomain drColumnDomain = new DrColumnDomain();
        drColumnDomain.setColumn(getName(ctx.name));
        builder.handleDomain(drColumnDomain, DomainSource.COLUMN_DEF);
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitModifyColumnClause(ModifyColumnClauseContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ALTER_COLUMN);
        ctx.columnDef().accept(this);
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitRenameClause(RenameClauseContext ctx) {
        DrTableDomain drTableDomain = new DrTableDomain();
        drTableDomain.setSqlType(SecQueryType.RENAME_TABLE);
        drTableDomain.setAuditKind(SecQueryKind.ALTER);

        String name = getName(ctx.newName);
        drTableDomain.setNewName(name);

        builder.handleDomain(drTableDomain, DomainSource.ALTER_TABLE_ITEM);

        return null;
    }

    @Override
    public Void visitRenameColumnClause(RenameColumnClauseContext ctx) {
        DrColumnDomain drColumnDomain = new DrColumnDomain();
        drColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME_COLUMN);
        drColumnDomain.setAuditKind(SecQueryKind.ALTER);

        drColumnDomain.setColumn(getName(ctx.name));
        drColumnDomain.setNewName(getName(ctx.newName));
        builder.handleDomain(drColumnDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
        //        throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
    }

    @Override
    public Void visitAddIndexClause(AddIndexClauseContext ctx) {
        RdbIndexDomain indexDomain = new RdbIndexDomain();
        indexDomain.setSqlType(SecQueryType.ALTER_TABLE_ADD_INDEX);
        indexDomain.setAuditKind(SecQueryKind.CREATE);
        indexDomain.setType("index");

        indexDomain.setName(getName(ctx.indexDef().indexName));
        indexDomain.setColumns(new ArrayList<>());
        for (ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.indexDef().identifierList().identifierSeq().errorCapturingIdentifier()) {
            indexDomain.getColumns().add(getName(errorCapturingIdentifierContext.identifier()));
        }
        if (ctx.indexDef().comment != null) {
            indexDomain.setComment(getText(ctx.indexDef().STRING_LITERAL().getText()));
        }
        builder.handleDomain(indexDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitDropIndexClause(DropIndexClauseContext ctx) {
        RdbIndexDomain indexDomain = new RdbIndexDomain();
        indexDomain.setSqlType(SecQueryType.DROP_INDEX);
        indexDomain.setAuditKind(SecQueryKind.DROP);
        indexDomain.setName(getName(ctx.name));
        builder.handleDomain(indexDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitModifyTableCommentClause(ModifyTableCommentClauseContext ctx) {
        DrTableDomain drTableDomain = new DrTableDomain();
        drTableDomain.setAuditKind(SecQueryKind.ALTER);
        drTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        drTableDomain.setComment(getText(ctx.comment.getText()));
        builder.handleDomain(drTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitSetVariableWithType(SetVariableWithTypeContext ctx) {
        StatementScopeContext scope = ctx.statementScope();

        DrScopeType scopeType;
        String keyName;
        if (scope.GLOBAL() != null) {
            scopeType = DrScopeType.GLOBAL;
            keyName = ctx.identifier().getText();
        } else if (scope.LOCAL() != null) {
            scopeType = DrScopeType.LOCAL;
            keyName = ctx.identifier().getText();
        } else if (scope.SESSION() != null) {
            scopeType = DrScopeType.SESSION;
            keyName = ctx.identifier().getText();
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }

        DrConfigDomain domain = new DrConfigDomain(keyName, scopeType);
        domain.setSqlType(SecQueryType.CONFIG_WRITE);
        domain.setAuditKind(SecQueryKind.OTHER);
        builder.addDomain(domain);
        return null;
    }

    @Override
    public Void visitSetSystemVariable(SetSystemVariableContext ctx) {
        DrScopeType scopeType = DrScopeType.GLOBAL;
        String keyName = ctx.identifier().getText();

        DrConfigDomain domain = new DrConfigDomain(keyName, scopeType);
        domain.setSqlType(SecQueryType.CONFIG_WRITE);
        domain.setAuditKind(SecQueryKind.OTHER);
        builder.addDomain(domain);
        return null;
    }

    @Override
    public Void visitSetUserVariable(SetUserVariableContext ctx) {
        DrScopeType scopeType = DrScopeType.LOCAL;
        String keyName = ctx.identifier().getText();

        DrConfigDomain domain = new DrConfigDomain(keyName, scopeType);
        domain.setSqlType(SecQueryType.CONFIG_WRITE);
        domain.setAuditKind(SecQueryKind.OTHER);
        builder.addDomain(domain);
        return null;
    }

    @Override
    public Void visitSwitchCatalog(SwitchCatalogContext ctx) {
        return null;
    }

    @Override
    public Void visitUseDatabase(UseDatabaseContext ctx) {
        return null;
    }

    @Override
    public Void visitPartitionTable(PartitionTableContext ctx) {
        return null;
    }

    @Override
    public Void visitIdentifierOrText(IdentifierOrTextContext ctx) {
        String text = this.getText(ctx);
        builder.addAttr(CommonAttribute.VALUE, getText(text));
        return null;
    }

    @Override
    public Void visitGrantUserIdentify(GrantUserIdentifyContext ctx) {
        if (ctx.STRING_LITERAL() != null) {
            String text = ctx.STRING_LITERAL().getText();
            builder.addAttr(CommonAttribute.PASSWORD, getText(text));
        }
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQuery(QueryContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitSetOperation(SetOperationContext ctx) {
        builder.addAttr(CommonAttribute.UNION, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQueryPrimaryDefault(QueryPrimaryDefaultContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitAliasQuery(AliasQueryContext ctx) {
        builder.enterWithSelect();
        dmVisitChildren(ctx);
        builder.exitWithSelect();
        return null;
    }

    @Override
    public Void visitWhereClause(WhereClauseContext ctx) {
        builder.enterWhere();
        dmVisitChildren(ctx);
        builder.exitWhere();
        return null;
    }

    @Override
    public Void visitLogicalNot(LogicalNotContext ctx) {
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
    public Void visitJoinRelation(JoinRelationContext ctx) {
        builder.enterSelectTableBuilder();
        dmVisitChildren(ctx);
        builder.exitSelectTableBuilder();
        return null;
    }

    @Override
    public Void visitRelation(RelationContext ctx) {
        builder.enterSelectTableBuilder();
        dmVisitChildren(ctx);
        builder.exitSelectTableBuilder();
        return null;
    }

    @Override
    public Void visitAggClause(AggClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitGroupingSet(GroupingSetContext ctx) {
        return null;
    }

    @Override
    public Void visitHavingClause(HavingClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitUpdateAssignment(UpdateAssignmentContext ctx) {
        builder.enterSetColumnValue();

        builder.enterSelectColumn();
        ctx.col.accept(this);
        builder.exitSelectColumn();

        ctx.expression().accept(this);

        builder.exitSetColumnValue();
        return null;
    }

    @Override
    public Void visitSortClause(SortClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitLimitClause(LimitClauseContext ctx) {
        builder.addAttr(CommonAttribute.LIMIT, true);
        return null;
    }

    @Override
    public Void visitJoinType(JoinTypeContext ctx) {
        builder.enterJoin(ctx.getText());
        builder.exitJoin();
        return null;
    }

    @Override
    public Void visitJoinCriteria(JoinCriteriaContext ctx) {
        return null;
    }

    @Override
    public Void visitTableName(TableNameContext ctx) {
        builder.enterObjName(NameType.TABLE);
        ctx.multipartIdentifier().accept(this);
        builder.exitObjName();

        visit(ctx.tableAlias());
        return null;
    }

    @Override
    public Void visitAliasedQuery(AliasedQueryContext ctx) {
        builder.enterSelectTableBuilder();
        dmVisitChildren(ctx);
        builder.exitSelectTableBuilder();
        return null;
    }

    @Override
    public Void visitPropertyItem(PropertyItemContext ctx) {
        String key = this.getText(ctx.key);
        String value = this.getText(ctx.value);
        if (key.startsWith("\"")) {
            key = key.substring(1, key.length() - 1);
        }
        if (value.startsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        KeyValueDomain domain = new KeyValueDomain(key, value);
        builder.handleDomain(domain, DomainSource.KEY_VALUE);
        return null;
    }

    @Override
    public Void visitTableAlias(TableAliasContext ctx) {
        if (ctx.strictIdentifier() != null) {
            builder.addAttr(CommonAttribute.ALIAS, this.getText(ctx.strictIdentifier()));
        }
        return null;
    }

    @Override
    public Void visitColumnDef(ColumnDefContext ctx) {
        builder.enterColumnDef();

        builder.enterObjName();
        ctx.colName.accept(this);
        builder.exitObjName();

        ctx.type.accept(this);
        visitIfExist(ctx.aggType);

        if (ctx.DEFAULT() != null && ctx.nullValue == null) {
            for (int i = 0; i < ctx.children.size(); i++) {
                if (ctx.getChild(i) == ctx.DEFAULT()) {
                    ParseTree start = ctx.getChild(i + 1);
                    ParseTree end = ctx.getChild(ctx.getChildCount() - 1);
                    for (; i < ctx.children.size(); i++) {
                        if (ctx.getChild(i) == ctx.ON()) {
                            end = ctx.getChild(i - 1);
                            break;
                        } else if (ctx.getChild(i) == ctx.COMMENT()) {
                            end = ctx.getChild(i - 1);
                            break;
                        }
                    }
                    builder.handleDomain(new RdbConstantDomain(
                        getText(this.getText(((TerminalNodeImpl) start).getSymbol(), ((TerminalNodeImpl) end).getSymbol()))), DomainSource.COLUMN_DEFAULT_VALUE);
                    break;
                }
            }
        }

        if (ctx.comment != null) {
            builder.addAttr(CommonAttribute.COMMENT, getText(ctx.comment.getText()));
        }

        if (ctx.NOT() != null) {
            builder.addAttr(CommonAttribute.NOT_NULL, true);
        }

        if (ctx.AUTO_INCREMENT() != null) {
            builder.addAttr(MyAttribute.AUTO_INCREMENT, true);
        }

        builder.exitColumnDef();
        return null;
    }

    @Override
    public Void visitIndexDef(IndexDefContext ctx) {
        RdbIndexDomain indexDomain = new RdbIndexDomain();
        indexDomain.setColumns(new ArrayList<>());
        indexDomain.setName(getName(ctx.indexName));
        indexDomain.setType("index");
        for (ErrorCapturingIdentifierContext errorCapturingIdentifierContext : ctx.cols.identifierSeq().errorCapturingIdentifier()) {
            String name = getName(errorCapturingIdentifierContext);
            indexDomain.getColumns().add(name);
        }

        if (ctx.comment != null) {
            indexDomain.setComment(getText(ctx.comment.getText()));
        }
        builder.handleDomain(indexDomain, DomainSource.INDEX);
        return null;
    }

    @Override
    public Void visitAggTypeDef(AggTypeDefContext ctx) {
        builder.addAttr(DrAttribute.AGG_TYPE, ctx.getText());
        return null;
    }

    @Override
    public Void visitInlineTable(InlineTableContext ctx) {
        if (!(ctx.parent.parent.parent.parent instanceof InsertTableContext)) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }
        builder.enterSelectDomain();
        builder.addAttr(DrAttribute.VALUES_SELECT, true);
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitExist(ExistContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNamedExpression(NamedExpressionContext ctx) {
        builder.enterBuildSelectItem();

        if (ctx.identifierOrText() != null) {
            builder.addAttr(CommonAttribute.ALIAS, this.getText(ctx.identifierOrText()));
        }

        builder.addAttr(CommonAttribute.VALUE, this.getText(ctx.expression()));
        ctx.expression().accept(this);

        builder.exitBuildSelectItem();
        return null;
    }

    @Override
    public Void visitRowConstructorItem(RowConstructorItemContext ctx) {
        if (ctx.constant() != null) {
            builder.enterBuildSelectItem();
            dmVisitChildren(ctx);
            builder.exitBuildSelectItem();
        } else {
            dmVisitChildren(ctx);
        }

        return null;
    }

    @Override
    public Void visitPredicate(PredicateContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitComparison(ComparisonContext ctx) {
        String left = this.getText(ctx.left);
        String right = this.getText(ctx.right);
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
    public Void visitDoublePipes(DoublePipesContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDereference(DereferenceContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitConvertType(ConvertTypeContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.CONVERT().getText()), DomainSource.OBJ_NAME);

            builder.handleFunctionArgs(() -> {
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.expression()));
                ctx.expression().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitConvertCharSet(ConvertCharSetContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.CONVERT().getText()), DomainSource.OBJ_NAME);

            builder.handleFunctionArgs(() -> {
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.expression()));
                ctx.expression().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitGroupConcat(GroupConcatContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.GROUP_CONCAT().getText()), DomainSource.OBJ_NAME);

            builder.handleFunctionArgs(() -> {
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.argument));
                ctx.argument.accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitTrim(TrimContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.TRIM().getText()), DomainSource.OBJ_NAME);

            builder.handleFunctionArgs(() -> {
                for (ExpressionContext expressionContext : ctx.expression()) {
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(expressionContext));
                    expressionContext.accept(this);
                }
            });
        });
        return null;
    }

    @Override
    public Void visitExtract(ExtractContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.EXTRACT().getText()), DomainSource.OBJ_NAME);

            builder.handleFunctionArgs(() -> {
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx.valueExpression()));
                ctx.valueExpression().accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitCurrentDate(CurrentDateContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.CURRENT_DATE().getText()), DomainSource.OBJ_NAME);
        });
        return null;
    }

    @Override
    public Void visitCurrentTime(CurrentTimeContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.CURRENT_TIME().getText()), DomainSource.OBJ_NAME);
        });
        return null;
    }

    @Override
    public Void visitDropMV(DropMVContext ctx) {
        builder.handleDropView(() -> {
            builder.handleObjName(() -> {
                ctx.mvName.accept(this);
            });
            builder.addAttr(CommonAttribute.MATERIALIZED, true);
        });
        return null;
    }

    @Override
    public Void visitSupportedTransactionStatementAlias(SupportedTransactionStatementAliasContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.OTHER);
        rdbResourceDomain.setSqlType(SecQueryType.TRANSACTION);
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
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
    public Void visitSupportedAdminStatementAlias(SupportedAdminStatementAliasContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setAuditKind(SecQueryKind.ADMIN);
        rdbResourceDomain.setSqlType(SecQueryType.ADMIN);
        rdbResourceDomain.setNeedSupply(false);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitSupportedDescribeStatementAlias(SupportedDescribeStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDescribeTable(DescribeTableContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.multipartIdentifier().accept(this);
            });
        }, SecQueryType.SHOW, SecQueryKind.QUERY, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitSupportedJobStatementAlias(SupportedJobStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreateScheduledJob(CreateScheduledJobContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setTarget(TargetType.Unknown);
        rdbResourceDomain.setSqlType(SecQueryType.CREATE_OBJECT);
        rdbResourceDomain.setAuditKind(SecQueryKind.CREATE);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitLoad(LoadContext ctx) {
        for (DataDescContext dataDesc : ctx.dataDescs) {
            builder.handleResource(() -> {
                builder.handleObjName(() -> {
                    dataDesc.targetTableName.accept(this);
                });
            }, SecQueryType.LOAD, SecQueryKind.DML, true, TargetType.Insert);

            if (dataDesc.sourceTableName != null) {
                throw new UnsupportedOperationException("Unsupported sql:" + getText(ctx));
            }
        }
        return null;
    }

    @Override
    public Void visitResumeJob(ResumeJobContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setTarget(TargetType.Unknown);
        rdbResourceDomain.setSqlType(SecQueryType.ALTER_OBJECT);
        rdbResourceDomain.setAuditKind(SecQueryKind.ALTER);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitSupportedStatsStatementAlias(SupportedStatsStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCancelJobTask(CancelJobTaskContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setTarget(TargetType.Unknown);
        rdbResourceDomain.setSqlType(SecQueryType.ADMIN);
        rdbResourceDomain.setSqlType(SecQueryType.ADMIN);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitAnalyzeTable(AnalyzeTableContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.name.accept(this);
            });
        }, SecQueryType.ANALYZE, SecQueryKind.OTHER, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitShowTableStats(ShowTableStatsContext ctx) {
        builder.handleResource(() -> {
            if (ctx.tableName != null) {
                builder.handleObjName(() -> {
                    ctx.tableName.accept(this);
                });
            }
        }, SecQueryType.SHOW, SecQueryKind.QUERY, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitCreateStoragePolicy(CreateStoragePolicyContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitCreateStorageVault(CreateStorageVaultContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.CREATE_OBJECT, SecQueryKind.CREATE));
        return null;
    }

    @Override
    public Void visitCreateRepository(CreateRepositoryContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitExport(ExportContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.tableName.accept(this);
            });
        }, SecQueryType.EXPORT, SecQueryKind.OTHER, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitCreateResource(CreateResourceContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitShowColumnHistogramStats(ShowColumnHistogramStatsContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.tableName.accept(this);
            });
        }, SecQueryType.SHOW, SecQueryKind.QUERY, true, TargetType.Column);
        return null;
    }

    @Override
    public Void visitDropJob(DropJobContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setTarget(TargetType.Unknown);
        rdbResourceDomain.setSqlType(SecQueryType.DROP_OBJECT);
        rdbResourceDomain.setAuditKind(SecQueryKind.DROP);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitPauseJob(PauseJobContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setTarget(TargetType.Unknown);
        rdbResourceDomain.setSqlType(SecQueryType.ALTER_OBJECT);
        rdbResourceDomain.setAuditKind(SecQueryKind.ALTER);
        rdbResourceDomain.setNeedSupply(false);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitSupportedOtherStatementAlias(SupportedOtherStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDropView(DropViewContext ctx) {
        builder.handleDropView(() -> {
            builder.handleObjName(() -> {
                ctx.name.accept(this);
            });
        });
        return null;
    }

    @Override
    public Void visitElementAt(ElementAtContext ctx) {
        ctx.value.accept(this);
        builder.handleOtherDomains(() -> {
            ctx.index.accept(this);
        });
        return null;
    }

    @Override
    public Void visitArraySlice(ArraySliceContext ctx) {
        ctx.value.accept(this);
        for (ValueExpressionContext valueExpressionContext : ctx.valueExpression()) {
            builder.handleOtherDomains(() -> {
                valueExpressionContext.accept(this);
            });
        }
        return null;
    }

    @Override
    public Void visitCurrentTimestamp(CurrentTimestampContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.CURRENT_TIMESTAMP().getText()), DomainSource.OBJ_NAME);
        });
        return null;
    }

    @Override
    public Void visitCurrentUser(CurrentUserContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.CURRENT_USER().getText()), DomainSource.OBJ_NAME);
        });
        return null;
    }

    @Override
    public Void visitSessionUser(SessionUserContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.SESSION_USER().getText()), DomainSource.OBJ_NAME);
        });
        return null;
    }

    @Override
    public Void visitLocalTimestamp(LocalTimestampContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.LOCALTIMESTAMP().getText()), DomainSource.OBJ_NAME);
        });
        return null;
    }

    @Override
    public Void visitLocalTime(LocalTimeContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.LOCALTIME().getText()), DomainSource.OBJ_NAME);
        });
        return null;
    }

    @Override
    public Void visitQualifyClause(QualifyClauseContext ctx) {
        return null;
    }

    @Override
    public Void visitCast(CastContext ctx) {
        builder.enterCall();

        builder.handleDomain(new ObjNameDomain(Collections.singletonList(ctx.name.getText()), NameType.FUNCTION), DomainSource.OBJ_NAME);

        builder.enterFunctionArgs();
        builder.addAttr(CommonAttribute.FUNC_ARG_NAME, this.getText(ctx.expression()));
        ctx.expression().accept(this);
        builder.exitFunctionArgs();

        builder.exitCall();
        return null;
    }

    @Override
    public Void visitStar(StarContext ctx) {
        RdbColumnDomain rdbColumnDomain = new RdbColumnDomain();
        rdbColumnDomain.setTable(getName(ctx.qualifiedName()));
        rdbColumnDomain.setColumn("*");
        builder.handleDomain(rdbColumnDomain, DomainSource.COLUMN);
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
    public Void visitUserVariable(UserVariableContext ctx) {
        IdentifierOrTextContext idOrText = ctx.identifierOrText();
        TerminalNode strText = idOrText.STRING_LITERAL();
        IdentifierContext idText = idOrText.identifier();

        String text;
        if (strText != null) {
            text = strText.getText();
            text = text.substring(1, text.length() - 1);
        } else if (idText != null) {
            text = idText.getText();
            if (text.startsWith("`")) {
                text = text.substring(1, text.length() - 1);
            }
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }

        builder.handleDomain(new DrVariableDomain(text, DrVariableType.USER), DomainSource.VARIABLE);
        return null;
    }

    @Override
    public Void visitSystemVariable(SystemVariableContext ctx) {
        TerminalNode global = ctx.GLOBAL();
        TerminalNode session = ctx.SESSION();
        DrVariableType type;
        if (global != null) {
            type = DrVariableType.GLOBAL;
        } else if (session != null) {
            type = DrVariableType.SESSION;
        } else {
            type = DrVariableType.NONE;
        }

        String text = ctx.identifier().getText();
        if (text.startsWith("`")) {
            text = text.substring(1, text.length() - 1);
        }
        builder.handleDomain(new DrVariableDomain(text, type), DomainSource.VARIABLE);
        return null;
    }

    @Override
    public Void visitIsnull(IsnullContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        ctx.valueExpression().accept(this);
        return null;
    }

    @Override
    public Void visitIs_not_null_pred(Is_not_null_predContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        ctx.valueExpression().accept(this);
        return null;
    }

    @Override
    public Void visitFunctionCallExpression(FunctionCallExpressionContext ctx) {
        builder.enterCall();
        ctx.functionIdentifier().accept(this);
        if (CollectionUtils.isNotEmpty(ctx.arguments)) {
            builder.enterFunctionArgs();
            for (FuncExpressionContext expressionContext : ctx.arguments) {
                String text = this.getText(expressionContext);
                if (text.equals("*")) {
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, text);
                    continue;
                }
                expressionContext.accept(this);
                if (text.startsWith("'")) {
                    text = text.substring(1, text.length() - 1);
                }
                builder.addAttr(CommonAttribute.FUNC_ARG_NAME, text);
            }
            builder.exitFunctionArgs();
        }
        builder.exitCall();
        return null;
    }

    @Override
    public Void visitAlterColumnStats(AlterColumnStatsContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.name.accept(this);
            });
        }, SecQueryType.ALTER_TABLE, SecQueryKind.ALTER, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitDropStats(DropStatsContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.tableName.accept(this);
            });
        }, SecQueryType.ALTER_TABLE, SecQueryKind.ALTER, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitShowColumnStats(ShowColumnStatsContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.tableName.accept(this);
            });
        }, SecQueryType.SHOW, SecQueryKind.QUERY, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitFunctionIdentifier(FunctionIdentifierContext ctx) {
        builder.enterObjName(NameType.FUNCTION);
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitFunctionNameIdentifier(FunctionNameIdentifierContext ctx) {
        String text = getName(ctx);
        builder.addAttr(CommonAttribute.VALUE, text);
        return null;
    }

    @Override
    public Void visitNullLiteral(NullLiteralContext ctx) {
        String text = getText(this.getText(ctx));
        builder.handleDomain(new RdbConstantDomain(text), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitNumericLiteral(NumericLiteralContext ctx) {
        String text = getText(this.getText(ctx));
        builder.handleDomain(new RdbConstantDomain(text), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitBooleanLiteral(BooleanLiteralContext ctx) {
        String text = getText(this.getText(ctx));
        builder.handleDomain(new RdbConstantDomain(text), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitTypeConstructor(TypeConstructorContext ctx) {
        String text = getText(this.getText(ctx));
        builder.handleDomain(new RdbConstantDomain(text), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitArrayLiteral(ArrayLiteralContext ctx) {
        String text = getText(this.getText(ctx));
        builder.handleDomain(new RdbConstantDomain(text), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitStructLiteral(StructLiteralContext ctx) {
        String text = getText(this.getText(ctx));
        builder.handleDomain(new RdbConstantDomain(text), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitMapLiteral(MapLiteralContext ctx) {
        String text = getText(this.getText(ctx));
        builder.handleDomain(new RdbConstantDomain(text), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitStringLiteral(StringLiteralContext ctx) {
        String text = getText(this.getText(ctx));
        builder.handleDomain(new RdbConstantDomain(text), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitValueExpressionDefault(ValueExpressionDefaultContext ctx) {
        if (ctx.primaryExpression() instanceof ConstantDefaultContext) {
            String text = this.getText(ctx.primaryExpression());
            if (text.startsWith("'")) {
                text = text.substring(1, text.length() - 1);
            }
            builder.handleDomain(new RdbConstantDomain(text), DomainSource.CONSTANT);
        } else if (ctx.primaryExpression() instanceof ColumnReferenceContext || ctx.primaryExpression() instanceof DereferenceContext) {
            builder.enterSelectColumn();
            dmVisitChildren(ctx);
            builder.exitSelectColumn();
        } else {
            dmVisitChildren(ctx);
        }
        return null;
    }

    @Override
    public Void visitComplexDataType(ComplexDataTypeContext ctx) {
        ColumnTypeDomain columnTypeDomain;
        columnTypeDomain = new ColumnTypeDomain(ctx.complex.getText(), this.getText(ctx), this.getText(ctx.LT().getSymbol(), null));
        builder.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);
        return null;
    }

    @Override
    public Void visitVariantPredefinedFields(VariantPredefinedFieldsContext ctx) {
        ColumnTypeDomain domain = new ColumnTypeDomain(ctx.variantTypeDefinitions().getChild(0).getText(), this.getText(ctx), null);
        builder.handleDomain(domain, DomainSource.COLUMN_TYPE);
        return null;
    }

    @Override
    public Void visitCreateFile(CreateFileContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setSqlType(SecQueryType.CREATE_OBJECT);
        rdbResourceDomain.setAuditKind(SecQueryKind.CREATE);
        rdbResourceDomain.setTarget(TargetType.Unknown);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitCreateWorkloadGroup(CreateWorkloadGroupContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain(SecQueryType.CREATE_OBJECT, SecQueryKind.ADMIN);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitCopyInto(CopyIntoContext ctx) {
        builder.handleResource(() -> {
            builder.handleObjName(() -> {
                ctx.multipartIdentifier().accept(this);
            });
        }, SecQueryType.COPY_INTO, SecQueryKind.DML, true, TargetType.Table);
        return null;
    }

    @Override
    public Void visitPrimitiveDataType(PrimitiveDataTypeContext ctx) {
        ColumnTypeDomain columnTypeDomain;
        if (ctx.INTEGER_VALUE().size() >= 1) {
            columnTypeDomain = new ColumnTypeDomain(this.getText(ctx.primitiveColType()), this.getText(ctx), ctx.INTEGER_VALUE(0).getText());
        } else {
            columnTypeDomain = new ColumnTypeDomain(this.getText(ctx.primitiveColType()), this.getText(ctx), null);
        }

        builder.handleDomain(columnTypeDomain, DomainSource.COLUMN_TYPE);
        return null;
    }

    @Override
    public Void visitAlterView(AlterViewContext ctx) {
        builder.enterAlterView();
        builder.handleObjName(() -> {
            ctx.name.accept(this);
        });
        builder.exitAlterView();
        return null;
    }

    @Override
    public Void visitAlterSystem(AlterSystemContext ctx) {
        builder.addDomain(new RdbResourceDomain(SecQueryType.ADMIN, SecQueryKind.ADMIN));
        return null;
    }

    @Override
    public Void visitIdentifier(IdentifierContext ctx) {
        String text = this.getText(ctx);
        if (text.startsWith("`")) {
            text = text.substring(1, text.length() - 1);
        }
        builder.addAttr(CommonAttribute.VALUE, text);
        return null;
    }

    @Override
    public Void visitCharFunction(CharFunctionContext ctx) {
        builder.handleCall(() -> {
            builder.handleDomain(new ObjNameDomain(ctx.CHAR().getText()), DomainSource.OBJ_NAME);
            builder.handleFunctionArgs(() -> {
                for (ExpressionContext argument : ctx.arguments) {
                    builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(argument));
                    argument.accept(this);
                }
            });
        });
        return null;
    }

    @Override
    public Void visitStatementBaseAlias(StatementBaseAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitStatementDefault(StatementDefaultContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSupportedDmlStatementAlias(SupportedDmlStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSupportedCreateStatementAlias(SupportedCreateStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSupportedAlterStatementAlias(SupportedAlterStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitMaterializedViewStatementAlias(MaterializedViewStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSupportedDropStatementAlias(SupportedDropStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSupportedSetStatementAlias(SupportedSetStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSupportedCancelStatementAlias(SupportedCancelStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSupportedUseStatementAlias(SupportedUseStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSupportedGrantRevokeStatementAlias(SupportedGrantRevokeStatementAliasContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSetOptions(SetOptionsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSetVariableWithoutType(SetVariableWithoutTypeContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUserIdentify(UserIdentifyContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQueryTermDefault(QueryTermDefaultContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSetQuantifier(SetQuantifierContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSubquery(SubqueryContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitValuesTable(ValuesTableContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRegularQuerySpecification(RegularQuerySpecificationContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCte(CteContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSelectClause(SelectClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSelectColumnClause(SelectColumnClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRelations(RelationsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUpdateAssignmentSeq(UpdateAssignmentSeqContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitQueryOrganization(QueryOrganizationContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIdentifierList(IdentifierListContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIdentifierSeq(IdentifierSeqContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRelationList(RelationListContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPropertyClause(PropertyClauseContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitMultipartIdentifier(MultipartIdentifierContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnDefs(ColumnDefsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIndexDefs(IndexDefsContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNamedExpressionSeq(NamedExpressionSeqContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpression(ExpressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFuncExpression(FuncExpressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPredicated(PredicatedContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitLogicalBinary(LogicalBinaryContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRowConstructor(RowConstructorContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitArithmeticBinary(ArithmeticBinaryContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitParenthesizedExpression(ParenthesizedExpressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumnReference(ColumnReferenceContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSubqueryExpression(SubqueryExpressionContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConstantDefault(ConstantDefaultContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFunctionCall(FunctionCallContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitComparisonOperator(ComparisonOperatorContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitErrorCapturingIdentifier(ErrorCapturingIdentifierContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRealIdent(RealIdentContext ctx) {
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
