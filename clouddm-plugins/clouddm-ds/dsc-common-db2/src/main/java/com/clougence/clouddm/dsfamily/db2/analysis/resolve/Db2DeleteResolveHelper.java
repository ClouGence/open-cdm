package com.clougence.clouddm.dsfamily.db2.analysis.resolve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.ref.MetaCollectTables;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2DeleteDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class Db2DeleteResolveHelper extends Db2QueryResolveHelper {

    public List<RuleDomain> fromDelete(SQLDeleteStatement statement) {
        List<RuleDomain> result = new ArrayList<>();
        Db2DeleteDomain domain = new Db2DeleteDomain();
        domain.setSqlType(SecQueryType.DELETE);
        domain.setAuditKind(SecQueryKind.DML);
        domain.setWhereColumns(new ArrayList<>());
        domain.setJoinType(RdbJoinType.NONE);
        domain.setCatalog(null);

        domain.setOptions(new HashMap<>());
        //domain.setHasLimit(statement.getLimit() != null);
        //domain.setHasIgnore(statement.isIgnore());
        domain.setMultiDelete(statement.getTableSource() instanceof SQLJoinTableSource);

        SQLTableSource deleteSource = statement.getTableSource();
        SQLTableSource deleteFrom = statement.getFrom();

        MetaCollectTables ctxTabs = new MetaCollectTables();
        ctxTabs.createFrame();
        if (deleteFrom != null) {
            this.helper.collectTables(ctxTabs, Arrays.asList(deleteFrom, statement.getUsing()), statement.getWith());
        } else if (deleteSource != null) {
            this.helper.collectTables(ctxTabs, Arrays.asList(deleteSource, statement.getUsing()), statement.getWith());
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + statement.toString());
        }

        fromDeleteTable(result, ctxTabs, domain, statement.getTableSource(), statement.getWhere());
        fromDeleteUsing(result, ctxTabs, domain, statement.getUsing(), statement.getWhere());
        fromWhere(result, ctxTabs, domain, statement.getWhere());
        fromWithQuery(result, ctxTabs, domain, statement.getWith());

        result.add(0, domain);
        return result;
    }

    private void fromDeleteTable(List<RuleDomain> result, MetaCollectTables ctxTabs, Db2DeleteDomain domain, SQLTableSource delTable, SQLExpr whereExpr) {
        if (delTable instanceof SQLExprTableSource) {
            configTargetTableName(ctxTabs, domain, delTable);
        } else if (delTable instanceof SQLJoinTableSource) {
            markJoin(domain, ((SQLJoinTableSource) delTable).getJoinType());
            SQLTableSource left = ((SQLJoinTableSource) delTable).getLeft();
            SQLTableSource right = ((SQLJoinTableSource) delTable).getRight();

            if (left instanceof SQLJoinTableSource) {
                fromDeleteTable(result, ctxTabs, domain, left, whereExpr);
            } else {
                fromDeleteForMultiSource(result, ctxTabs, left, whereExpr);
            }

            if (right instanceof SQLJoinTableSource) {
                fromDeleteTable(result, ctxTabs, domain, right, whereExpr);
            } else {
                fromDeleteForMultiSource(result, ctxTabs, right, whereExpr);
            }
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + delTable.toString());
        }
    }

    private void fromDeleteForMultiSource(List<RuleDomain> result, MetaCollectTables ctxTabs, SQLTableSource delTable, SQLExpr whereExpr) {
        Db2DeleteDomain domain = new Db2DeleteDomain();
        domain.setSqlType(SecQueryType.DELETE);
        domain.setAuditKind(SecQueryKind.DML);
        domain.setWhereColumns(new ArrayList<>());
        domain.setJoinType(RdbJoinType.NONE);
        domain.setOptions(new HashMap<>());

        fromDeleteTable(result, ctxTabs, domain, delTable, whereExpr);
        fromWhere(result, ctxTabs, domain, whereExpr);

        result.add(domain);
    }

    private void fromDeleteUsing(List<RuleDomain> result, MetaCollectTables ctxTabs, Db2DeleteDomain domain, SQLTableSource using, SQLExpr whereExpr) {
        if (using == null) {
            return;
        }

        if (using instanceof SQLJoinTableSource) {
            markJoin(domain, ((SQLJoinTableSource) using).getJoinType());
            fromSelectForSource(result, ctxTabs, using, RdbQueryMode.JOIN, whereExpr);
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + using.toString());
        }
    }
}
