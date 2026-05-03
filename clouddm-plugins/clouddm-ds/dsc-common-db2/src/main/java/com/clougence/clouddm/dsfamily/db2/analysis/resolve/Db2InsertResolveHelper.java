package com.clougence.clouddm.dsfamily.db2.analysis.resolve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.ref.MetaCollectTables;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2InsertDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.utils.CollectionUtils;

public class Db2InsertResolveHelper extends Db2QueryResolveHelper {

    //    public static RuleDomain parseReplace(SQLReplaceStatement statement) {
    //        SQLQueryExpr tableSource = statement.getQuery();
    //        InsertRuleDomain ruleDomain = new InsertRuleDomain();
    //        ruleDomain.setRuleTypeEnum(RuleTypeEnum.Replace);
    //        NameGroupInfo tableName = getGroupInfo(DataSourceType.MySQL, statement.getTableName());
    //        configNameGroupInfo(ruleDomain.findOrCreateNode("into"), tableName);
    //        parseTableResources(ruleDomain.getResourceTable(), statement.getTableSource());
    //        if (tableSource != null) {
    //            ruleDomain.setValue("mode", "select");
    //            parseTableResources(ruleDomain.getResourceTable(), tableSource);
    //        } else {
    //            ruleDomain.setValue("mode", "insert");
    //        }
    //        return ruleDomain;
    //    }

    public List<RuleDomain> fromInsert(SQLInsertStatement statement) {
        MetaCollectTables ctxTabs = new MetaCollectTables();
        ctxTabs.createFrame();
        this.helper.collectTables(ctxTabs, statement.getWith());

        List<RuleDomain> result = new ArrayList<>();
        Db2InsertDomain domain = new Db2InsertDomain();
        domain.setSqlType(SecQueryType.INSERT);
        domain.setAuditKind(SecQueryKind.DML);
        domain.setCatalog(extractCatalogName((statement.getTableSource()).getName()));
        domain.setSchema(extractSchemaName((statement.getTableSource()).getName()));
        domain.setTable(extractName((statement.getTableSource()).getName()));

        domain.setOptions(new HashMap<>());
        domain.setHasWith(statement.getWith() != null);

        fromInsertColumns(domain, statement);
        fromInsertValues(domain, ctxTabs, statement, result);
        fromInsertQuery(result, ctxTabs, domain, statement.getQuery());

        domain.setConflict(RdbInsertConflictStrategy.NONE);
        //        if (statement.isOnConflictDoNothing()) {
        //            domain.setConflict(RdbInsertConflictStrategy.IGNORE);
        //        } else if (CollectionUtils.isNotEmpty(statement.getOnConflictUpdateSetItems())) {
        //            domain.setConflict(RdbInsertConflictStrategy.UPDATE);
        //        }

        result.add(0, domain);
        return result;
    }

    private void fromInsertQuery(List<RuleDomain> result, MetaCollectTables ctxTabs, Db2InsertDomain domain, SQLSelect query) {
        if (query == null) {
            return;
        }

        domain.setFromSelect(true);
        fromSelect(result, ctxTabs, query, RdbQueryMode.INSERT);
    }

    private void fromInsertValues(Db2InsertDomain domain, MetaCollectTables ctxTabs, SQLInsertStatement statement, List<RuleDomain> result) {
        List<SQLInsertStatement.ValuesClause> valuesList = statement.getValuesList();
        domain.setMultipleValues(valuesList.size() > 1);

        for (SQLInsertStatement.ValuesClause values : valuesList) {
            for (SQLExpr value : values.getValues()) {
                if (value instanceof SQLQueryExpr) {
                    SQLQueryExpr queryExpr = (SQLQueryExpr) value;
                    domain.setHasSubQuery(true);
                    fromSelect(result, ctxTabs, queryExpr.getSubQuery(), RdbQueryMode.INSERT);
                } else if (value instanceof SQLNullExpr) {
                    domain.setHasNullValue(true);
                }
            }
        }

    }

    private void fromInsertColumns(Db2InsertDomain domain, SQLInsertStatement statement) {
        List<SQLExpr> columns = statement.getColumns();

        domain.setColumns(new ArrayList<>());
        if (CollectionUtils.isEmpty(columns)) {
            domain.setOnlyValues(true);
            return;
        }

        domain.setOnlyValues(false);
        for (SQLExpr column : columns) {
            if (column instanceof SQLIdentifierExpr) {
                domain.getColumns().add(getColumnNameRemoveQualifier(((SQLIdentifierExpr) column).getName()));
            }
        }
    }
}
