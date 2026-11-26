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
package com.clougence.clouddm.ds.dameng.definition.ui.editor.data;

import java.sql.JDBCType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.dameng.dialect.DmDialect;
import com.clougence.clouddm.dsfamily.definition.ui.editor.data.DsFamilyDataEditorSpi;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorColumn;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSqlType;
import com.clougence.clouddm.sdk.ui.editor.data.reload.EditorResultSet;
import com.clougence.clouddm.sdk.ui.editor.data.reload.Reload;
import com.clougence.clouddm.sdk.ui.editor.data.reload.SqlData;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.i18n.I18nUtils;

public class DmDataEditorSpi extends DsFamilyDataEditorSpi {

    private static final String ROWID_ALIAS = "__CDM_ROWID__";

    @Override
    protected Dialect getDialect() { return DmDialect.INSTANCE; }

    @Override
    public String buildSelect(RdbTable tableMeta, String condition, String orderBy, Integer offset, Integer limit) {
        String sql = super.buildSelect(tableMeta, condition, orderBy, offset, limit);
        int safeOffset = offset == null || offset < 0 ? 0 : offset;
        int safeLimit = limit == null || limit <= 0 ? 50 : limit;
        return sql + " limit " + safeLimit + " offset " + safeOffset;
    }

    @Override
    protected StringBuilder buildSelect(Dialect dialect, RdbTable tableMeta) {
        StringBuilder sb = super.buildSelect(dialect, tableMeta);
        if (tableMeta.getUmiType() == UmiTypes.Table) {
            sb.insert(0, "CAST(ROWID AS VARCHAR) AS \"" + ROWID_ALIAS + "\", ");
        }
        return sb;
    }

    @Override
    public String buildUpdate(RdbTable tableMeta, Map<String, String> whereData, Map<String, String> setData) {
        Dialect dialect = this.getDialect();

        StringBuilder setSql = this.buildSet(dialect, tableMeta, setData);
        String tableName = dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        String rid = whereData.containsKey(ROWID_ALIAS) ? whereData.get(ROWID_ALIAS) : whereData.get("ROWID");
        return String.format("update %s set %s where rowid = '%s'", tableName, setSql, rid);
    }

    @Override
    public String buildDelete(RdbTable tableMeta, Map<String, String> whereData) {
        Dialect dialect = this.getDialect();

        String tableName = dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        String rid = whereData.containsKey(ROWID_ALIAS) ? whereData.get(ROWID_ALIAS) : whereData.get("ROWID");
        return String.format("delete from %s where rowid = '%s'", tableName, rid);
    }

    @Override
    protected String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        return DmDataEditorUtils.templateOfSelectCol(dialect, col);
    }

    @Override
    protected String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        return DmDataEditorUtils.templateOfInsert(dialect, col, value);
    }

    @Override
    protected String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        return DmDataEditorUtils.templateOfSet(dialect, col, value);
    }

    @Override
    protected String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        return DmDataEditorUtils.templateOfWhere(dialect, col, value);
    }

    @Override
    public void configTableHeader(RdbTable rdbTable, List<DataEditorColumn> headerCols, Map<String, RdbColumn> colMap, I18nUtils i18nUtils) {
        for (DataEditorColumn header : headerCols) {
            DmDataEditorUtils.configTableHeader(rdbTable, header, colMap.get(header.getColumn()), i18nUtils);
        }

        if (rdbTable.getUmiType() == UmiTypes.Table) {
            DataEditorColumn idColumn = new DataEditorColumn();
            idColumn.setColumn(ROWID_ALIAS);
            idColumn.setInsertReadOnly(true);
            idColumn.setUpdateReadOnly(true);
            idColumn.setWhereKey(true);
            idColumn.setHide(true);
            headerCols.add(0, idColumn);
        }
    }

    @Override
    public void beforeExecute(RdbTable tableMeta, DataEditorSqlType dmlType, QueryRequest request) {
        if (dmlType == DataEditorSqlType.INSERT) {
            request.setUseCallable(true);
            request.setQueryBody("BEGIN " + request.getQueryBody() + " RETURN ROWID INTO ?; END;");
            QueryArg inParam = new QueryArg(1, null, JDBCType.VARCHAR.getVendorTypeNumber(), null, false);
            QueryArg outParam = new QueryArg(1, null, 0, null, true);
            request.setQueryArgs(Arrays.asList(inParam, outParam));
        }
    }

    @Override
    public Reload afterExecute(RdbTable tableMeta, QueryRequest request, EditorResultSet result, SqlData sqlData) {
        Map<String, String> outParams = result.getOutParams();
        String fallbackRid = sqlData.getWhereData().containsKey(ROWID_ALIAS) ? sqlData.getWhereData().get(ROWID_ALIAS) : sqlData.getWhereData().get("ROWID");
        String rid = outParams != null && outParams.containsKey("1") ? outParams.get("1") : fallbackRid;
        Dialect dialect = getDialect();
        StringBuilder builder = new StringBuilder();
        builder.append("select ");
        builder.append(this.buildSelect(dialect, tableMeta));
        builder.append(" from ");
        builder.append(dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName()));
        builder.append(" where rowid='");
        builder.append(rid);
        builder.append("';");
        return Reload.reload(builder.toString());
    }
}
