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
package com.clougence.clouddm.ds.sqlserver.definition.ui.editor.data;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.sqlserver.dialect.SqlServerDialect;
import com.clougence.clouddm.dsfamily.definition.ui.editor.data.DsFamilyDataEditorSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorAttributeKeys;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorColumn;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSqlType;
import com.clougence.clouddm.sdk.ui.editor.data.reload.EditorResultSet;
import com.clougence.clouddm.sdk.ui.editor.data.reload.Reload;
import com.clougence.clouddm.sdk.ui.editor.data.reload.SqlData;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

public class SqlServerDataEditorSpi extends DsFamilyDataEditorSpi {

    @Override
    protected Dialect getDialect() { return SqlServerDialect.INSTANCE; }

    @Override
    public String buildUpdate(RdbTable tableMeta, Map<String, String> whereData, Map<String, String> setData) {
        Dialect dialect = this.getDialect();

        StringBuilder sb = new StringBuilder();
        sb.append("update top (1) ");
        sb.append(dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName()));
        sb.append(" set ");
        sb.append(this.buildSet(dialect, tableMeta, setData));
        sb.append(" where %%physloc%% like ");
        sb.append(whereData.get("RID"));
        return sb.toString();
    }

    @Override
    public String buildDelete(RdbTable tableMeta, Map<String, String> whereData) {
        Dialect dialect = this.getDialect();
        StringBuilder sb = new StringBuilder();
        sb.append("delete top (1) from ");
        sb.append(dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName()));
        sb.append(" where %%physloc%% like ");
        sb.append(whereData.get("RID"));
        return sb.toString();
    }

    @Override
    public String buildSelect(RdbTable tableMeta, String condition, String orderBy, Integer offset, Integer limit) {
        Dialect dialect = getDialect();

        StringBuilder sb = new StringBuilder();
        if (tableMeta.getUmiType() == UmiTypes.Table) {
            sb.append("select %%physloc%% as RID, ");
        } else {
            sb.append("select ");
        }
        sb.append(buildSelect(dialect, tableMeta));
        sb.append(" from ");
        sb.append(dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName()));

        if (StringUtils.isNotBlank(condition)) {
            sb.append(" where ");
            sb.append(condition);
        }
        sb.append(" order by ");
        if (StringUtils.isNotBlank(orderBy)) {
            sb.append(orderBy);
        } else {
            sb.append("(select null)");
        }

        sb.append(" offset " + offset);
        sb.append(" rows fetch next " + limit);
        sb.append(" rows only ");
        return sb.toString();
    }

    @Override
    protected String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        return SqlServerDataEditorUtils.templateOfSelectCol(dialect, col);
    }

    @Override
    protected String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        return SqlServerDataEditorUtils.templateOfInsert(dialect, col, value);
    }

    @Override
    protected String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        return SqlServerDataEditorUtils.templateOfSet(dialect, col, value);
    }

    @Override
    protected String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        return SqlServerDataEditorUtils.templateOfWhere(dialect, col, value);
    }

    @Override
    public void configTableHeader(RdbTable rdbTable, List<DataEditorColumn> headerCols, Map<String, RdbColumn> colMap, I18nUtils i18nUtils) {
        for (DataEditorColumn header : headerCols) {
            SqlServerDataEditorUtils.configTableHeader(rdbTable, header, colMap.get(header.getColumn()), i18nUtils);
        }

        if (rdbTable.getUmiType() == UmiTypes.Table) {
            DataEditorColumn ridColumn = new DataEditorColumn();
            ridColumn.setHide(true);
            ridColumn.setColumn("RID");
            ridColumn.setWhereKey(true);
            ridColumn.setSpareWhere(true);
            ridColumn.setUpdateReadOnly(true);
            ridColumn.setInsertReadOnly(true);
            headerCols.add(0, ridColumn);
        }
    }

    @Override
    public Reload afterExecute(RdbTable tableMeta, QueryRequest request, EditorResultSet result, SqlData sqlData) {
        Dialect dialect = getDialect();
        StringBuilder where;
        if (DataEditorSqlType.INSERT.equals(sqlData.getDmlType())) {
            List<Map<String, String>> generatedKeys = result.getGeneratedKeys();
            if (CollectionUtils.isNotEmpty(generatedKeys)) {
                fillAutoCols(tableMeta, sqlData, generatedKeys);
            }
            where = this.buildWhere(dialect, tableMeta, sqlData.getUpdateData());
        } else {
            where = new StringBuilder("%%physloc%% like " + sqlData.getWhereData().get("RID"));
        }

        // reload sql
        StringBuilder sb = new StringBuilder();
        sb.append("select top(1)");
        sb.append(super.buildSelect(dialect, tableMeta).append(", %%physloc%% as RID"));
        sb.append(" from ");
        sb.append(dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName()));
        sb.append(" where ");
        sb.append(where);
        return Reload.reload(sb.toString());
    }

    protected boolean insertIgnore(RdbColumn colDef, String colValue) {
        String readOnly = colDef.getAttribute(DataEditorAttributeKeys.INSERT_READ_ONLY);
        if (Boolean.parseBoolean(colDef.getAttribute(DataEditorAttributeKeys.AUTOINCREMENT)) && StringUtils.isBlank(colValue)) {
            return true;
        }
        return Boolean.parseBoolean(readOnly);
    }
}
