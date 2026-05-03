package com.clougence.clouddm.dsfamily.db2.analysis;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2CreateSchemaStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2CreateTableStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2DropSchemaStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2RenameTableStatement;
import com.alibaba.druid.sql.dialect.db2.parser.DB2StatementParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbViewDomain;
import com.clougence.clouddm.dsfamily.db2.analysis.resolve.*;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

public class Db2SecDomainResolveSpi implements SecDomainResolveSpi, Db2SecDomainOptionKeys {

    protected Db2SchemaResolveHelper schemaHelper;
    protected Db2TableResolveHelper  tableHelper;
    protected Db2ViewResolveHelper   viewHelper;
    protected Db2IndexResolveHelper  indexHelper;
    protected Db2QueryResolveHelper  queryHelper;
    protected Db2InsertResolveHelper insertHelper;
    protected Db2UpdateResolveHelper updateHelper;
    protected Db2DeleteResolveHelper deleteHelper;
    protected Db2CallResolveHelper   callHelper;

    public Db2SecDomainResolveSpi(){
        this.schemaHelper = new Db2SchemaResolveHelper();
        this.tableHelper = new Db2TableResolveHelper();
        this.viewHelper = new Db2ViewResolveHelper();
        this.indexHelper = new Db2IndexResolveHelper();
        this.queryHelper = new Db2QueryResolveHelper();
        this.insertHelper = new Db2InsertResolveHelper();
        this.updateHelper = new Db2UpdateResolveHelper();
        this.deleteHelper = new Db2DeleteResolveHelper();
        this.callHelper = new Db2CallResolveHelper();
    }

    @Override
    public List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo ctxInfo) {
        List<SQLStatement> sqlStatements = new DB2StatementParser(codeInfo.getQuery(), SQLParserFeature.KeepComments).parseStatementList();
        return sqlStatements.stream().flatMap((Function<SQLStatement, Stream<RuleDomain>>) s -> {
            List<RuleDomain> list = parseSql(s);
            for (RuleDomain ruleDomain : list) {
                SplitScript splitScript = new SplitScript();
                ruleDomain.setSplitScript(splitScript);
            }
            return list.stream();
        }).peek(r -> r.setDsType(dsType)).collect(Collectors.toList());
    }

    private List<RuleDomain> parseSql(SQLStatement statement) {
        // database
        if (statement instanceof DB2CreateSchemaStatement) {
            return this.schemaHelper.fromCreateSchema((DB2CreateSchemaStatement) statement);
        } else if (statement instanceof DB2DropSchemaStatement) {
            return this.schemaHelper.fromDropSchema((DB2DropSchemaStatement) statement);
        }

        // table
        if (statement instanceof DB2CreateTableStatement) {
            return this.tableHelper.fromCreateTable((DB2CreateTableStatement) statement);
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
            //return this.viewHelper.fromCreateView((SQLCreateViewStatement) statement); // TODO Unsupported
        } else if (statement instanceof SQLAlterViewStatement) {
            return this.viewHelper.fromAlterView((SQLAlterViewStatement) statement);
        } else if (statement instanceof SQLDropViewStatement) {
            return this.viewHelper.fromDropView((SQLDropViewStatement) statement);
        }

        // index
        if (statement instanceof SQLCreateIndexStatement) {
            return this.indexHelper.fromCreateIndex((SQLCreateIndexStatement) statement);
        } else if (statement instanceof SQLDropIndexStatement) {
            //return this.indexHelper.fromDropIndex((SQLDropIndexStatement) statement); // TODO Unsupported
        } else if (statement instanceof SQLAlterIndexStatement) {
            //return Collections.emptyList(); // TODO Unsupported
        }

        // function
        if (statement instanceof SQLAlterFunctionStatement) {
            //return Collections.emptyList(); // TODO Unsupported
        }

        // call
        if (statement instanceof SQLCallStatement) {
            return this.callHelper.fromCall((SQLCallStatement) statement);
        }

        // query
        if (statement instanceof SQLSelectStatement) {
            return this.queryHelper.fromSelect(((SQLSelectStatement) statement).getSelect());
        } else if (statement instanceof SQLUpdateStatement) {
            return this.updateHelper.fromUpdate((SQLUpdateStatement) statement);
        } else if (statement instanceof SQLInsertStatement) {
            return this.insertHelper.fromInsert((SQLInsertStatement) statement);
        } else if (statement instanceof SQLDeleteStatement) {
            return this.deleteHelper.fromDelete((SQLDeleteStatement) statement);
        }

        // rename
        if (statement instanceof DB2RenameTableStatement) {
            return this.tableHelper.fromRenameTable((DB2RenameTableStatement) statement);
        }

        if (statement instanceof SQLCreateViewStatement) {
            SQLCreateViewStatement sqlCreateViewStatement = (SQLCreateViewStatement) statement;
            SQLExprTableSource tableSource = sqlCreateViewStatement.getTableSource();
            if (tableSource.getExpr() instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr) tableSource.getExpr();
                RdbViewDomain rdbViewDomain = new RdbViewDomain();
                rdbViewDomain.setAuditKind(SecQueryKind.CREATE);
                rdbViewDomain.setSqlType(SecQueryType.CREATE_VIEW);
                rdbViewDomain.setView(identifierExpr.getName());
                return Collections.singletonList(rdbViewDomain);
            }
        }

        throw new UnsupportedOperationException("unsupported SQL: " + statement.toString());
    }
}
