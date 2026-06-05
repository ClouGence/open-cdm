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
package com.clougence.clouddm.dsfamily.db2.analysis.resolve;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbJoinType;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.ref.MetaCollectTables;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2UpdateDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class Db2UpdateResolveHelper extends Db2QueryResolveHelper {

    protected Db2UpdateDomain newUpdateDomain() {
        Db2UpdateDomain domain = new Db2UpdateDomain();
        domain.setSqlType(SecQueryType.UPDATE);
        domain.setAuditKind(SecQueryKind.DML);
        domain.setSetColumns(new ArrayList<>());
        domain.setWhereColumns(new ArrayList<>());
        domain.setJoinType(RdbJoinType.NONE);
        domain.setOptions(new HashMap<>());
        return domain;
    }

    public List<RuleDomain> fromUpdate(SQLUpdateStatement statement) {
        List<RuleDomain> result = new ArrayList<>();
        Db2UpdateDomain domain = newUpdateDomain();
        //domain.setHasLimit(statement.getLimit() != null);
        //domain.setHasIgnore(statement.isIgnore());
        domain.setMultiUpdate(statement.getTableSource() instanceof SQLJoinTableSource);

        MetaCollectTables ctxTabs = new MetaCollectTables();
        ctxTabs.createFrame();
        this.helper.collectTables(ctxTabs, Arrays.asList(statement.getTableSource(), statement.getFrom()), statement.getWith());
        if (domain.isMultiUpdate()) {
            SQLJoinTableSource joinSource = (SQLJoinTableSource) statement.getTableSource();
            SQLExprTableSource left = (SQLExprTableSource) joinSource.getLeft();
            SQLExprTableSource right = (SQLExprTableSource) joinSource.getRight();

            markJoin(domain, joinSource.getJoinType());
            fromUpdate(result, ctxTabs, domain, left, statement.getItems(), statement.getWhere(), statement.getWith());
            fromUpdate(result, ctxTabs, domain, right, statement.getItems(), statement.getWhere(), statement.getWith());
        } else {
            configTargetTableName(ctxTabs, domain, statement.getTableSource());
        }

        fromUpdateSet(result, ctxTabs, domain, statement.getItems());
        fromWhere(result, ctxTabs, domain, statement.getWhere());
        fromWithQuery(result, ctxTabs, domain, statement.getWith());

        result.add(0, domain);
        return result;
    }

    private void fromUpdate(List<RuleDomain> result, MetaCollectTables ctxTabs, Db2UpdateDomain mainDomain, //
                            SQLExprTableSource source, List<SQLUpdateSetItem> set, SQLExpr where, SQLWithSubqueryClause with) {
        Db2UpdateDomain domain = newUpdateDomain();
        //domain.setHasLimit(mainDomain.isHasLimit());
        //domain.setHasIgnore(mainDomain.isHasIgnore());

        configTargetTableName(ctxTabs, domain, source);

        // TODO currently using the share.
        fromUpdateSet(result, ctxTabs, domain, set);
        fromWhere(result, ctxTabs, domain, where);
        fromWithQuery(result, ctxTabs, domain, with);
        result.add(domain);
    }

    private void fromUpdateSet(List<RuleDomain> result, MetaCollectTables ctxTabs, Db2UpdateDomain domain, List<SQLUpdateSetItem> sets) {
        domain.setSetColumns(new ArrayList<>());
        for (SQLUpdateSetItem item : sets) {
            SQLExpr column = item.getColumn();
            if (column instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr expr = (SQLIdentifierExpr) column;
                domain.getSetColumns().add(getColumnNameRemoveQualifier(expr.getName()));
            } else if (column instanceof SQLPropertyExpr) {
                SQLPropertyExpr expr = (SQLPropertyExpr) column;
                domain.getSetColumns().add(getColumnNameRemoveQualifier(expr.getName()));
            }

            if (item.getValue() instanceof SQLQueryExpr) {
                domain.setSelectInSet(true);
                fromSelect(result, ctxTabs, ((SQLQueryExpr) item.getValue()).getSubQuery(), RdbQueryMode.SUB_SET);
            }
        }
    }
}
