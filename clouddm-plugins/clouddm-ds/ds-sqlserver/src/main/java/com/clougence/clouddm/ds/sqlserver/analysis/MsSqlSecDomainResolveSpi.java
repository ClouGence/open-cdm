package com.clougence.clouddm.ds.sqlserver.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.*;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.clougence.clouddm.ds.sqlserver.analysis.builder.MsBuildFactory;
import com.clougence.clouddm.ds.sqlserver.analysis.resolve.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCallDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTriggerDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbViewDomain;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.sqlserver.parser.MsSqlDslProvider;
import com.clougence.clouddm.ds.sqlserver.parser.antlr.SqlServerParser;
import com.clougence.clouddm.ds.sqlserver.parser.antlr.SqlServerParserBaseVisitor;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

import lombok.Getter;

public class MsSqlSecDomainResolveSpi implements SecDomainResolveSpi {

    protected MsCatalogResolveHelper catalogHelper;
    protected MsSchemaResolveHelper  schemaHelper;
    protected MsTableResolveHelper   tableHelper;
    protected MsViewResolveHelper    viewHelper;
    protected MsIndexResolveHelper   indexHelper;
    protected MsQueryResolveHelper   queryHelper;
    protected MsInsertResolveHelper  insertHelper;
    protected MsUpdateResolveHelper  updateHelper;
    protected MsDeleteResolveHelper  deleteHelper;
    protected MsUseResolveHelper     useResolveHelper;

    private MetaService              metaService;

    public MsSqlSecDomainResolveSpi(MetaService metaService){
        this.metaService = metaService;

        this.catalogHelper = new MsCatalogResolveHelper();
        this.schemaHelper = new MsSchemaResolveHelper();
        this.tableHelper = new MsTableResolveHelper();
        this.viewHelper = new MsViewResolveHelper();
        this.indexHelper = new MsIndexResolveHelper();
        this.queryHelper = new MsQueryResolveHelper();
        this.insertHelper = new MsInsertResolveHelper();
        this.updateHelper = new MsUpdateResolveHelper();
        this.deleteHelper = new MsDeleteResolveHelper();
        this.useResolveHelper = new MsUseResolveHelper();
    }

    public MsSqlSecDomainResolveSpi(){
        this.catalogHelper = new MsCatalogResolveHelper();
        this.schemaHelper = new MsSchemaResolveHelper();
        this.tableHelper = new MsTableResolveHelper();
        this.viewHelper = new MsViewResolveHelper();
        this.indexHelper = new MsIndexResolveHelper();
        this.queryHelper = new MsQueryResolveHelper();
        this.insertHelper = new MsInsertResolveHelper();
        this.updateHelper = new MsUpdateResolveHelper();
        this.deleteHelper = new MsDeleteResolveHelper();
        this.useResolveHelper = new MsUseResolveHelper();
    }

    protected DslProvider dslProvider() {
        return MsSqlDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(MsBuildFactory domainBuilder, Parser parser) {
        return new MsSqlParserVisitor(domainBuilder, parser);
    }

    //    protected AbstractParseTreeVisitor<Void> parserVisitor(MyBaseDomainBuilder domainBuilder) {
    //        return new MySQLParserVisitor(domainBuilder);
    //    }

    @Override
    public List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo ctxInfo) {
        com.clougence.dslpaser.ast.location.CodeLocation dslBase =//
            new com.clougence.dslpaser.ast.location.CodeLocation(codeInfo.getBaseLine(), codeInfo.getBaseColumn());
        List<RuleDomain> result = new ArrayList<>();

        List<SplitScript> splitScripts = new ArrayList<>();
        List<AstSplitScript> scripts = DslHelper.splitDsl(dslProvider(), codeInfo.getQuery(), dslBase);
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            //
            if (specialHandle(s.getAstTree(), result, ss)) {
                continue;
            }
            splitScripts.add(ss);
        }

        for (SplitScript splitScript : splitScripts) {
            SQLStatement statement = new SQLServerStatementParser(splitScript.getScript(), SQLParserFeature.KeepComments).parseStatement();
            List<RuleDomain> list = parseSql(statement, splitScript);
            result.addAll(list);
        }
        result.forEach(ruleDomain -> ruleDomain.setDsType(dsType));
        return result;
    }

    private boolean specialHandle(ParseTree parseTree, List<RuleDomain> result, SplitScript splitScript) {
        ParseTree child = parseTree.getChild(0);

        if ((child instanceof SqlServerParser.Ddl_clauseContext
             && (child.getChild(0) instanceof SqlServerParser.Create_synonymContext || child.getChild(0) instanceof SqlServerParser.Create_sequenceContext
                 || child.getChild(0) instanceof SqlServerParser.Create_login_sql_serverContext))
            || (child instanceof SqlServerParser.Batch_level_statementContext && (child.getChild(0).getChild(0) instanceof SqlServerParser.Create_or_alter_dml_triggerContext
                                                                                  || child.getChild(0) instanceof SqlServerParser.Create_or_alter_functionContext
                                                                                  || child.getChild(0) instanceof SqlServerParser.Create_or_alter_procedureContext))) {
            MsBuildFactory builder = new MsBuildFactory(this.metaService);
            DslHelper.doVisitor(dslProvider(), splitScript.getScript(), (lexer, parser) -> this.parserVisitor(builder, parser));
            result.addAll(builder.build());
            return true;
        } else if (child instanceof SqlServerParser.Batch_level_statementContext) {
            if (child.getChild(0) instanceof SqlServerParser.Create_or_alter_triggerContext) {
                SqlServerParser.Create_or_alter_triggerContext triggerContext = (SqlServerParser.Create_or_alter_triggerContext) child.getChild(0);
                if (triggerContext.getChild(0) instanceof SqlServerParser.Create_or_alter_dml_triggerContext) {
                    SqlServerParser.Create_or_alter_dml_triggerContext context = (SqlServerParser.Create_or_alter_dml_triggerContext) triggerContext.getChild(0);

                    RdbTriggerDomain rdbTriggerDomain = new RdbTriggerDomain();
                    rdbTriggerDomain.setSqlType(SecQueryType.CREATE_TRIGGER);
                    rdbTriggerDomain.setAuditKind(SecQueryKind.CREATE);
                    for (ParseTree tree : context.children) {
                        if (tree instanceof TerminalNodeImpl) {
                            TerminalNodeImpl terminalNodeImpl = (TerminalNodeImpl) parseTree;
                            if (terminalNodeImpl.getSymbol().getType() == SqlServerParser.ALTER) {
                                rdbTriggerDomain.setAuditKind(SecQueryKind.ALTER);
                                rdbTriggerDomain.setSqlType(SecQueryType.ALTER_TRIGGER);
                            }
                        }
                        if (tree instanceof SqlServerParser.Table_nameContext) {
                            MsVisitor msVisitor = new MsVisitor();
                            msVisitor.visit(tree);
                            List<String> nameList = msVisitor.getNameList();
                            int size = nameList.size();
                            switch (size) {
                                case 3: {
                                    rdbTriggerDomain.setCatalog(nameList.get(0));
                                }
                                case 2: {
                                    rdbTriggerDomain.setSchema(nameList.get(size - 2));
                                }
                                case 1: {
                                    rdbTriggerDomain.setTable(nameList.get(size - 1));
                                }
                            }
                            result.add(rdbTriggerDomain);
                            return true;
                        }
                    }
                }
            } else if (child.getChild(0) instanceof SqlServerParser.Create_viewContext) {
                SqlServerParser.Create_viewContext viewContext = (SqlServerParser.Create_viewContext) child.getChild(0);
                for (ParseTree tree : viewContext.children) {
                    if (tree instanceof SqlServerParser.Simple_nameContext) {
                        MsVisitor msVisitor = new MsVisitor();
                        msVisitor.visit(tree);
                        List<String> nameList = msVisitor.getNameList();
                        int size = nameList.size();
                        RdbViewDomain rdbViewDomain = new RdbViewDomain();
                        switch (size) {
                            case 3: {
                                rdbViewDomain.setCatalog(nameList.get(0));
                            }
                            case 2: {
                                rdbViewDomain.setSchema(nameList.get(size - 2));
                            }
                            case 1: {
                                rdbViewDomain.setView(nameList.get(size - 1));
                            }
                        }
                        rdbViewDomain.setSqlType(SecQueryType.CREATE_VIEW);
                        rdbViewDomain.setAuditKind(SecQueryKind.CREATE);
                        result.add(rdbViewDomain);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private List<RuleDomain> parseSql(SQLStatement sqlStatement, SplitScript splitScript) {
        List<RuleDomain> list = this.parseSql(sqlStatement);
        for (RuleDomain ruleDomain : list) {
            ruleDomain.setSplitScript(splitScript);
        }
        return list;
    }

    private List<RuleDomain> parseSql(SQLStatement statement) {
        // database
        if (statement instanceof SQLCreateDatabaseStatement) {
            return this.catalogHelper.fromCreateCatalog((SQLCreateDatabaseStatement) statement);
        } else if (statement instanceof SQLDropDatabaseStatement) {
            return this.catalogHelper.fromDropCatalog((SQLDropDatabaseStatement) statement);
        } else if (statement instanceof SQLServerCreateSchemaStatement) {
            return this.schemaHelper.fromCreateSchema((SQLServerCreateSchemaStatement) statement);
        } else if (statement instanceof SQLServerDropSchemaStatement) {
            return this.schemaHelper.fromDropSchema((SQLServerDropSchemaStatement) statement);
        }

        // table
        if (statement instanceof SQLCreateTableStatement) {
            return this.tableHelper.fromCreateTable((SQLCreateTableStatement) statement);
        } else if (statement instanceof SQLAlterTableStatement) {
            return this.tableHelper.fromAlterTable((SQLAlterTableStatement) statement);
        } else if (statement instanceof SQLDropTableStatement) {
            return this.tableHelper.fromDropTable((SQLDropTableStatement) statement);
        } else if (statement instanceof SQLCommentStatement) {
            if (((SQLCommentStatement) statement).getType() == SQLCommentStatement.Type.TABLE) {
                return this.tableHelper.fromAlterTableComment((SQLCommentStatement) statement);
            } else if (((SQLCommentStatement) statement).getType() == SQLCommentStatement.Type.COLUMN) {
                return this.tableHelper.fromAlterColumnComment((SQLCommentStatement) statement);
            }
        }

        // view
        if (statement instanceof SQLCreateViewStatement) {
            return this.viewHelper.fromCreateView((SQLCreateViewStatement) statement); // TODO Unsupported
        } else if (statement instanceof SQLAlterViewStatement) {
            return this.viewHelper.fromAlterView((SQLAlterViewStatement) statement);
        } else if (statement instanceof SQLDropViewStatement) {
            return this.viewHelper.fromDropView((SQLDropViewStatement) statement);
        }

        // index
        if (statement instanceof SQLCreateIndexStatement) {
            return this.indexHelper.fromCreateIndex((SQLCreateIndexStatement) statement); // TODO Unsupported
        } else if (statement instanceof SQLDropIndexStatement) {
            return this.indexHelper.fromDropIndex((SQLDropIndexStatement) statement); // TODO Unsupported
        } else if (statement instanceof SQLAlterIndexStatement) {
            //return Collections.emptyList(); // TODO Unsupported
        }

        // function
        if (statement instanceof SQLAlterFunctionStatement) {
            //return Collections.emptyList(); // TODO Unsupported
        }

        // query
        if (statement instanceof SQLSelectStatement) {
            return this.queryHelper.fromSelect(((SQLSelectStatement) statement).getSelect());
        } else if (statement instanceof SQLServerUpdateStatement) {
            return this.updateHelper.fromUpdate((SQLServerUpdateStatement) statement);
        } else if (statement instanceof SQLServerInsertStatement) {
            return this.insertHelper.fromInsert((SQLServerInsertStatement) statement);
        } else if (statement instanceof SQLDeleteStatement) {
            return this.deleteHelper.fromDelete((SQLDeleteStatement) statement);
        }

        // exec
        if (statement instanceof SQLServerExecStatement) {
            SQLServerExecStatement execStatement = (SQLServerExecStatement) statement;
            if (execStatement.getModuleName().getSimpleName().equalsIgnoreCase("sp_rename")) {
                SQLServerExecStatement.SQLServerParameter sqlServerParameter = execStatement.getParameters().get(2);
                String type = sqlServerParameter.getExpr().toString().toLowerCase();
                if (type.equals("'database'")) {
                    return this.catalogHelper.fromRenameCatalog(execStatement);
                }
            }

        }
        if (statement instanceof SQLUseStatement) {
            return this.useResolveHelper.fromSwitchCatalog((SQLUseStatement) statement);
        }
        if (statement instanceof SQLServerExecStatement) {
            SQLServerExecStatement execStatement = (SQLServerExecStatement) statement;
            if (execStatement.getModuleName().getSimpleName().equalsIgnoreCase("sp_rename")) {
                RdbCallDomain rdbCallDomain = new RdbCallDomain();
                rdbCallDomain.setAuditKind(SecQueryKind.CALL);
                rdbCallDomain.setSqlType(SecQueryType.CALL);
                rdbCallDomain.setCallName(execStatement.getModuleName().getSimpleName());
                return Collections.singletonList(rdbCallDomain);
            }
        }

        throw new UnsupportedOperationException("unsupported SQL: " + statement.toString());
    }

    private static class MsVisitor extends SqlServerParserBaseVisitor<Void> {

        @Getter
        private List<String> nameList = new ArrayList<>();

        @Override
        public Void visitId_(SqlServerParser.Id_Context ctx) {
            String text = ctx.getText();
            if (text.startsWith("[")) {
                nameList.add(text.substring(1, text.length() - 1));
            } else {
                nameList.add(text);
            }

            return null;
        }
    }
}
