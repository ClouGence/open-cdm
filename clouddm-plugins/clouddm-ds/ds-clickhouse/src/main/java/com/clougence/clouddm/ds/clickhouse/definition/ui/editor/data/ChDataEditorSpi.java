package com.clougence.clouddm.ds.clickhouse.definition.ui.editor.data;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.clickhouse.dialect.ClickHouseDialect;
import com.clougence.clouddm.dsfamily.definition.ui.editor.data.DsFamilyDataEditorSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorColumn;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSqlType;
import com.clougence.clouddm.sdk.ui.editor.data.reload.EditorResultSet;
import com.clougence.clouddm.sdk.ui.editor.data.reload.Reload;
import com.clougence.clouddm.sdk.ui.editor.data.reload.SqlData;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.utils.i18n.I18nUtils;

public class ChDataEditorSpi extends DsFamilyDataEditorSpi {

    @Override
    protected Dialect getDialect() { return ClickHouseDialect.INSTANCE; }

    @Override
    public String buildUpdate(RdbTable tableMeta, Map<String, String> whereData, Map<String, String> setData) {
        Dialect dialect = this.getDialect();

        StringBuilder setSql = this.buildSet(dialect, tableMeta, setData);
        StringBuilder whereSql = this.buildWhere(dialect, tableMeta, whereData);

        String tableName = dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        return String.format("alter table %s update %s where %s", tableName, setSql, whereSql);
    }

    @Override
    public String buildDelete(RdbTable tableMeta, Map<String, String> whereData) {
        Dialect dialect = this.getDialect();

        StringBuilder whereSql = this.buildWhere(dialect, tableMeta, whereData);

        String tableName = dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        return String.format("alter table %s delete where %s", tableName, whereSql);
    }

    @Override
    public String buildSelect(RdbTable tableMeta, String condition, String orderBy, Integer offset, Integer limit) {
        String selectSql = super.buildSelect(tableMeta, condition, orderBy, offset, limit);

        if (offset == null) {
            return selectSql + String.format(" limit %s", limit);
        } else {
            return selectSql + String.format(" limit %s, %s", offset, limit);
        }
    }

    @Override
    protected String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        String result = ChDataEditorUtils.templateOfSelectCol(dialect, col);
        if (result != null) {
            return result;
        } else {
            return super.templateOfSelectCol(dialect, col);
        }
    }

    @Override
    protected String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        String result = ChDataEditorUtils.templateOfInsert(dialect, col, value);
        if (result != null) {
            return result;
        } else {
            return super.templateOfInsert(dialect, col, value);
        }
    }

    @Override
    protected String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        return ChDataEditorUtils.templateOfSet(dialect, col, value);
    }

    @Override
    protected String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        return ChDataEditorUtils.templateOfWhere(dialect, col, value);
    }

    @Override
    public void configTableHeader(RdbTable rdbTable, List<DataEditorColumn> headerCols, Map<String, RdbColumn> colMap, I18nUtils i18nUtils) {
        for (DataEditorColumn header : headerCols) {
            ChDataEditorUtils.configTableHeader(rdbTable, header, colMap.get(header.getColumn()), i18nUtils);
        }
    }

    @Override
    public void beforeExecute(RdbTable tableMeta, DataEditorSqlType dmlType, QueryRequest request) {

    }

    @Override
    public Reload afterExecute(RdbTable tableMeta, QueryRequest request, EditorResultSet result, SqlData sqlData) {
        Dialect dialect = getDialect();

        // reload sql
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(super.buildSelect(dialect, tableMeta));
        sb.append(" from ");
        sb.append(dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName()));
        sb.append(" where ");
        sb.append(this.buildWhere(dialect, tableMeta, convertWhereData(sqlData.getWhereData(), sqlData.getUpdateData())));
        sb.append(" limit 1");
        return Reload.reload(sb.toString());
    }
}
