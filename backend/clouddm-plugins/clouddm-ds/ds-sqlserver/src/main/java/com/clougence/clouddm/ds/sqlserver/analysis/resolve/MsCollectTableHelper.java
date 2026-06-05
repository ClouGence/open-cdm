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

import java.util.List;

import com.alibaba.druid.sql.ast.statement.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.ref.MetaCollectTables;
import com.clougence.clouddm.dsfamily.analysis.secrules.ref.MetaTable;

public class MsCollectTableHelper extends MsAbstractResolveHelper {

    public void collectTables(MetaCollectTables collect, SQLSelectQuery source, SQLWithSubqueryClause withQuery) {
        collectTables(collect, withQuery);

        //
        if (source instanceof SQLUnionQuery) {
            List<SQLSelectQuery> queries = ((SQLUnionQuery) source).getRelations();
            for (SQLSelectQuery query : queries) {
                collectTables(collect, query, null);
            }
            return;
        }

        //
        SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) source;
        SQLTableSource from = queryBlock.getFrom();

        if (from != null) {
            if (from instanceof SQLExprTableSource) {
                String catalog = this.extractCatalogName(((SQLExprTableSource) from).getName());
                String schema = this.extractSchemaName(((SQLExprTableSource) from).getName());
                String table = this.extractName(((SQLExprTableSource) from).getName());
                String alias = from.getAlias();
                collect.pushTable(new MetaTable(catalog, schema, table, alias));
            } else if (from instanceof SQLJoinTableSource) {
                collectTableMeta(collect, ((SQLJoinTableSource) from).getLeft());
                collectTableMeta(collect, ((SQLJoinTableSource) from).getRight());
                collectTableMeta(collect, from);

            } else if (from instanceof SQLSubqueryTableSource) {
                String alias = from.getAlias();
                collect.pushTable(new MetaTable(null, null, null, alias));
            } else if (from instanceof SQLUnionQueryTableSource) {
                String alias = from.getAlias();
                collect.pushTable(new MetaTable(null, null, null, alias));
            } else {
                throw new UnsupportedOperationException("unsupported SQL: " + source.toString());
            }
        }
    }

    public void collectTables(MetaCollectTables collect, List<SQLTableSource> source, SQLWithSubqueryClause withQuery) {
        for (SQLTableSource tab : source) {
            if (tab == null) {
                continue;
            }
            collectTableMeta(collect, tab);
        }

        collectTables(collect, withQuery);
    }

    public void collectTables(MetaCollectTables collect, SQLWithSubqueryClause withQuery) {
        if (withQuery == null) {
            return;
        }

        for (SQLWithSubqueryClause.Entry entry : withQuery.getEntries()) {
            MetaTable tableRef = collect.pushTable(new MetaTable(null, null, entry.getAlias()));
            tableRef.setReal(false);
        }
    }

    public void collectTableMeta(MetaCollectTables collect, SQLTableSource tab) {
        if (tab instanceof SQLExprTableSource) {
            String catalog = this.extractCatalogName(((SQLExprTableSource) tab).getName());
            String schema = this.extractSchemaName(((SQLExprTableSource) tab).getName());
            String table = this.extractName(((SQLExprTableSource) tab).getName());
            String alias = tab.getAlias();
            collect.pushTable(new MetaTable(catalog, schema, table, alias));

        } else if (tab instanceof SQLJoinTableSource) {
            collectTableMeta(collect, ((SQLJoinTableSource) tab).getLeft());
            collectTableMeta(collect, ((SQLJoinTableSource) tab).getRight());
        } else if (tab instanceof SQLSubqueryTableSource) {
            String alias = tab.getAlias();
            //SQLSelect subSelect = ((SQLSubqueryTableSource) tab).getSelect();
            //SQLSelectQuery query = subSelect.getQuery();
            MetaTable metaTable = collect.pushTable(new MetaTable(null, null, null, alias));
            metaTable.setReal(false);
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + tab.toString());
        }
    }
}
