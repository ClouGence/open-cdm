package com.clougence.clouddm.ds.oracle.definition.ui.editor.data;

import java.sql.JDBCType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.oracle.dialect.OracleDialect;
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
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

public class OraDataEditorSpi extends DsFamilyDataEditorSpi {

    @Override
    protected Dialect getDialect() { return OracleDialect.INSTANCE; }

    @Override
    public String buildSelect(RdbTable tableMeta, String condition, String orderBy, Integer offset, Integer limit) {
        Dialect dialect = this.getDialect();
        String tableName = getDialect().fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());

        StringBuilder selectSql = this.buildSelect(dialect, tableMeta);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("select %s from %s", selectSql, tableName));
        //        sb.append(" where rowid in ( select rd from ( select rowid as rd, rownum as sn from ");
        //        sb.append(tableName);
        sb.append(" WHERE rownum <= ");
        sb.append(limit + offset);
        sb.append("and rownum > ");
        sb.append(offset);
        //        sb.append(" ");
        if (StringUtils.isNotBlank(condition)) {
            sb.append(" and ");
            sb.append(condition);
        }
        if (StringUtils.isNotBlank(orderBy)) {
            sb.append(" order by ");
            sb.append(orderBy);
        }
        return sb.toString();
    }

    @Override
    protected StringBuilder buildSelect(Dialect dialect, RdbTable tableMeta) {
        StringBuilder sb = super.buildSelect(dialect, tableMeta);
        if (tableMeta.getUmiType() == UmiTypes.Table) {
            sb.insert(0, "ROWID, ");
        }
        return sb;
    }

    @Override
    public String buildUpdate(RdbTable tableMeta, Map<String, String> whereData, Map<String, String> setData) {
        Dialect dialect = this.getDialect();

        StringBuilder setSql = this.buildSet(dialect, tableMeta, setData);
        String tableName = dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        return String.format("update %s set %s where rowid = '%s'", tableName, setSql, whereData.get("ROWID"));
    }

    @Override
    public String buildDelete(RdbTable tableMeta, Map<String, String> whereData) {
        Dialect dialect = this.getDialect();

        String tableName = dialect.fmtTableName(true, tableMeta.getCatalog(), tableMeta.getSchema(), tableMeta.getName());
        return String.format("delete from %s where rowid = '%s'", tableName, whereData.get("ROWID"));
    }

    @Override
    protected String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        return OraDataEditorUtils.templateOfSelectCol(dialect, col);
    }

    @Override
    protected String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        return OraDataEditorUtils.templateOfInsert(dialect, col, value);
    }

    @Override
    protected String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        return OraDataEditorUtils.templateOfSet(dialect, col, value);
    }

    @Override
    protected String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        return OraDataEditorUtils.templateOfWhere(dialect, col, value);
    }

    @Override
    public void configTableHeader(RdbTable rdbTable, List<DataEditorColumn> headerCols, Map<String, RdbColumn> colMap, I18nUtils i18nUtils) {
        for (DataEditorColumn header : headerCols) {
            OraDataEditorUtils.configTableHeader(rdbTable, header, colMap.get(header.getColumn()), i18nUtils);
        }

        if (rdbTable.getUmiType() == UmiTypes.Table) {
            DataEditorColumn idColumn = new DataEditorColumn();
            idColumn.setColumn("ROWID");
            idColumn.setInsertReadOnly(true);
            idColumn.setUpdateReadOnly(true);
            idColumn.setWhereKey(true);
            idColumn.setHide(true);
            headerCols.add(0, idColumn);
        }
    }

    @Override
    public void beforeExecute(RdbTable tableMeta, DataEditorSqlType dmlType, QueryRequest request) {
        if (dmlType.equals(DataEditorSqlType.INSERT)) {
            request.setQueryBody("BEGIN " + request.getQueryBody() + " RETURN ROWID INTO ?; END;");
            request.setUseCallable(true);
            QueryArg inParam = new QueryArg(1, null, JDBCType.VARCHAR.getVendorTypeNumber(), null, false);
            QueryArg outParam = new QueryArg(1, null, 0, null, true);
            request.setQueryArgs(Arrays.asList(inParam, outParam));
        }
    }

    @Override
    public Reload afterExecute(RdbTable tableMeta, QueryRequest request, EditorResultSet result, SqlData sqlData) {
        Map<String, String> outParams = result.getOutParams();
        String rid = outParams != null && outParams.containsKey("1") ? outParams.get("1") : sqlData.getWhereData().get("ROWID");
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
