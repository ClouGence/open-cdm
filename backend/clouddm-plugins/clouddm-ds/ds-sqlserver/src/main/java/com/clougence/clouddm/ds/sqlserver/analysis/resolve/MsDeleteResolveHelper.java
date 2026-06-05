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
package com.clougence.clouddm.ds.sqlserver.analysis.resolve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsDeleteDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.ref.MetaCollectTables;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class MsDeleteResolveHelper extends MsQueryResolveHelper {

    public List<RuleDomain> fromDelete(SQLDeleteStatement statement) {
        List<RuleDomain> result = new ArrayList<>();
        MsDeleteDomain domain = new MsDeleteDomain();
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
        fromDeleteFrom(result, ctxTabs, domain, statement.getFrom(), statement.getWhere());
        fromWhere(result, ctxTabs, domain, statement.getWhere());
        fromWithQuery(result, ctxTabs, domain, statement.getWith());

        result.add(0, domain);
        return result;
    }

    private void fromDeleteTable(List<RuleDomain> result, MetaCollectTables ctxTabs, MsDeleteDomain domain, SQLTableSource delTable, SQLExpr whereExpr) {
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
        MsDeleteDomain domain = new MsDeleteDomain();
        domain.setSqlType(SecQueryType.DELETE);
        domain.setAuditKind(SecQueryKind.DML);
        domain.setWhereColumns(new ArrayList<>());
        domain.setJoinType(RdbJoinType.NONE);
        domain.setOptions(new HashMap<>());

        fromDeleteTable(result, ctxTabs, domain, delTable, whereExpr);
        fromWhere(result, ctxTabs, domain, whereExpr);

        result.add(domain);
    }

    private void fromDeleteFrom(List<RuleDomain> result, MetaCollectTables ctxTabs, MsDeleteDomain domain, SQLTableSource using, SQLExpr whereExpr) {
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
