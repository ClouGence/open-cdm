package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.data;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.db2.dialect.Db2Dialect;
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

public class Db2DataEditorSpi extends DsFamilyDataEditorSpi {

    @Override
    protected Dialect getDialect() { return Db2Dialect.INSTANCE; }

    protected boolean insertIgnore(RdbColumn colDef, String colValue) {
        String readOnly = colDef.getAttribute(DataEditorAttributeKeys.INSERT_READ_ONLY);
        if (Boolean.parseBoolean(colDef.getAttribute(DataEditorAttributeKeys.AUTOINCREMENT)) && StringUtils.isBlank(colValue)) {
            return true;
        }
        return Boolean.parseBoolean(readOnly);
    }

    @Override
    public String buildUpdate(RdbTable tableMeta, Map<String, String> whereData, Map<String, String> setData) {
        Dialect dialect = this.getDialect();
        StringBuilder setSql = this.buildSet(dialect, tableMeta, setData);
        String tableName = dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        return String.format("update %s set %s where rid()=%s limit 1", tableName, setSql, whereData.get("RID"));
    }

    @Override
    public String buildDelete(RdbTable tableMeta, Map<String, String> whereData) {
        String tabName = getDialect().fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        return String.format("delete from %s where rid()=%s limit 1", tabName, whereData.get("RID"));
    }

    @Override
    protected StringBuilder buildSelect(Dialect dialect, RdbTable tableMeta) {
        StringBuilder sb = super.buildSelect(dialect, tableMeta);
        if (tableMeta.getUmiType() == UmiTypes.Table) {
            sb.insert(0, "RID() as RID, ");
        }
        return sb;
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
        return Db2DataEditorUtils.templateOfSelectCol(dialect, col);
    }

    @Override
    protected String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        return Db2DataEditorUtils.templateOfInsert(dialect, col, value);
    }

    @Override
    protected String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        return Db2DataEditorUtils.templateOfSet(dialect, col, value);
    }

    @Override
    protected String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        return Db2DataEditorUtils.templateOfWhere(dialect, col, value);
    }

    @Override
    public void configTableHeader(RdbTable rdbTable, List<DataEditorColumn> headerCols, Map<String, RdbColumn> colMap, I18nUtils i18nUtils) {
        for (DataEditorColumn header : headerCols) {
            Db2DataEditorUtils.configTableHeader(rdbTable, header, colMap.get(header.getColumn()), i18nUtils);
        }

        if (rdbTable.getUmiType() == UmiTypes.Table) {
            DataEditorColumn ridColumn = new DataEditorColumn();
            ridColumn.setColumn("RID");
            ridColumn.setInsertReadOnly(true);
            ridColumn.setUpdateReadOnly(true);
            ridColumn.setSpareWhere(true);
            ridColumn.setHide(true);
            ridColumn.setWhereKey(true);
            headerCols.add(0, ridColumn);
        }
    }

    @Override
    public Reload afterExecute(RdbTable tableMeta, QueryRequest request, EditorResultSet result, SqlData sqlData) {
        //        if (dmlType == DataEditorSqlType.INSERT) {
        Dialect dialect = getDialect();
        StringBuilder where;
        if (sqlData.getDmlType().equals(DataEditorSqlType.INSERT)) {
            List<Map<String, String>> generatedKeys = result.getGeneratedKeys();
            if (CollectionUtils.isNotEmpty(generatedKeys)) {
                fillAutoCols(tableMeta, sqlData, generatedKeys);
            }
            where = this.buildWhere(dialect, tableMeta, sqlData.getUpdateData());
        } else {
            where = new StringBuilder(sqlData.getWhereData().get("RID"));
        }

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append(this.buildSelect(dialect, tableMeta));
        sb.append(" from ");
        sb.append(dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName()));
        sb.append(" where ");
        sb.append(where);
        sb.append(" limit 1");
        return Reload.reload(sb.toString());
    }

}
