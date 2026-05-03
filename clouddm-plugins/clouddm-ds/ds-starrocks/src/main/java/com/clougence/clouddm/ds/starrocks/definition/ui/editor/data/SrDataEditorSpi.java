package com.clougence.clouddm.ds.starrocks.definition.ui.editor.data;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.starrocks.dialect.StarRocksDialect;
import com.clougence.clouddm.dsfamily.definition.ui.editor.data.DsFamilyDataEditorSpi;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorAttributeKeys;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorColumn;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

public class SrDataEditorSpi extends DsFamilyDataEditorSpi {

    @Override
    protected Dialect getDialect() { return StarRocksDialect.INSTANCE; }

    protected boolean insertIgnore(RdbColumn colDef, String colValue) {
        String readOnly = colDef.getAttribute(DataEditorAttributeKeys.INSERT_READ_ONLY);
        if (Boolean.parseBoolean(colDef.getAttribute(DataEditorAttributeKeys.AUTOINCREMENT)) && StringUtils.isBlank(colValue)) {
            return true;
        }
        return Boolean.parseBoolean(readOnly);
    }

    @Override
    public String buildUpdate(RdbTable tableMeta, Map<String, String> whereData, Map<String, String> setData) {
        return super.buildUpdate(tableMeta, whereData, setData);
    }

    @Override
    public String buildDelete(RdbTable tableMeta, Map<String, String> whereData) {
        return super.buildDelete(tableMeta, whereData);
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
        return SrDataEditorUtils.templateOfSelectCol(dialect, col);
    }

    @Override
    protected String templateOfInsert(Dialect dialect, RdbColumn col, String value) {
        return SrDataEditorUtils.templateOfInsert(dialect, col, value);
    }

    @Override
    protected String templateOfSet(Dialect dialect, RdbColumn col, String value) {
        return SrDataEditorUtils.templateOfSet(dialect, col, value);
    }

    @Override
    protected String templateOfWhere(Dialect dialect, RdbColumn col, String value) {
        return SrDataEditorUtils.templateOfWhere(dialect, col, value);
    }

    @Override
    public void configTableHeader(RdbTable rdbTable, List<DataEditorColumn> headerCols, Map<String, RdbColumn> colMap, I18nUtils i18nUtils) {
        for (DataEditorColumn header : headerCols) {
            SrDataEditorUtils.configTableHeader(rdbTable, header, colMap.get(header.getColumn()), i18nUtils);
        }
    }

}
