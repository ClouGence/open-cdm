package com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.data;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.definition.ui.editor.data.DsFamilyDataEditorSpi;
import com.clougence.clouddm.dsfamily.postgres.dialect.PostgreDialect;
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
import com.clougence.utils.i18n.I18nUtils;

public class PgDataEditorSpi extends DsFamilyDataEditorSpi {

    @Override
    protected Dialect getDialect() { return PostgreDialect.INSTANCE; }

    @Override
    protected boolean insertIgnore(RdbColumn colDef, String colValue) {
        String readOnly = colDef.getAttribute(DataEditorAttributeKeys.INSERT_READ_ONLY);
        String auto = colDef.getAttribute(DataEditorAttributeKeys.AUTOINCREMENT);
        return Boolean.parseBoolean(readOnly) || Boolean.parseBoolean(auto);
    }

    @Override
    public String buildSelect(RdbTable tableMeta, String condition, String orderBy, Integer offset, Integer limit) {
        String selectSql = super.buildSelect(tableMeta, condition, orderBy, offset, limit);

        if (offset == null) {
            return selectSql + String.format(" limit %s", limit);
        } else {
            return selectSql + String.format(" limit %s offset %s", limit, offset);
        }
    }

    @Override
    protected StringBuilder buildWhere(Dialect dialect, RdbTable tableMeta, Map<String, String> data) {
        return new StringBuilder().append("ctid = '").append(data.get("ctid")).append("'");
    }

    @Override
    protected StringBuilder buildSelect(Dialect dialect, RdbTable tableMeta) {
        StringBuilder sb = super.buildSelect(dialect, tableMeta);
        if (tableMeta.getUmiType() == UmiTypes.Table) {
            sb.insert(0, "ctid, ");
        }
        return sb;
    }

    @Override
    protected String templateOfSelectCol(Dialect dialect, RdbColumn col) {
        return PgDataEditorUtils.templateOfSelectCol(dialect, col);
    }

    @Override
    protected String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        return PgDataEditorUtils.templateOfInsert(dialect, col, value);
    }

    @Override
    protected String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        return PgDataEditorUtils.templateOfSet(dialect, col, value);
    }

    @Override
    protected String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        return PgDataEditorUtils.templateOfWhere(dialect, col, value);
    }

    @Override
    public void configTableHeader(RdbTable rdbTable, List<DataEditorColumn> headerCols, Map<String, RdbColumn> colMap, I18nUtils i18nUtils) {
        for (DataEditorColumn header : headerCols) {
            PgDataEditorUtils.configTableHeader(rdbTable, header, colMap.get(header.getColumn()), i18nUtils);
        }

        if (rdbTable.getUmiType() == UmiTypes.Table) {
            DataEditorColumn column = new DataEditorColumn();
            column.setHide(true);
            column.setInsertReadOnly(true);
            column.setUpdateReadOnly(true);
            column.setWhereKey(true);
            column.setColumn("ctid");
            headerCols.add(0, column);
        }
    }

    @Override
    public void beforeExecute(RdbTable tableMeta, DataEditorSqlType dmlType, QueryRequest request) {
        if (dmlType == DataEditorSqlType.INSERT || dmlType == DataEditorSqlType.UPDATE) {
            StringBuilder sb = this.buildSelect(getDialect(), tableMeta);
            request.setQueryBody(request.getQueryBody() + " returning " + sb + ";");
        }
    }

    @Override
    public Reload afterExecute(RdbTable tableMeta, QueryRequest request, EditorResultSet result, SqlData sqlData) {
        return Reload.success(result);
    }
}
