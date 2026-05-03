package com.clougence.clouddm.sdk.ui.editor.data;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.utils.i18n.I18nUtils;

public interface DataEditorSpi extends Spi {

    String buildInsert(RdbTable tableMeta, Map<String, String> recordData);

    String buildUpdate(RdbTable tableMeta, Map<String, String> whereData, Map<String, String> setData);

    String buildDelete(RdbTable tableMeta, Map<String, String> whereData);

    String buildSelect(RdbTable tableMeta, String condition, String orderBy, Integer offset, Integer limit);

    String buildSelectCount(RdbTable tableMeta, String condition);

    void configTableHeader(RdbTable rdbTable, List<DataEditorColumn> headerCols, Map<String, RdbColumn> colMap, I18nUtils i18nUtils);
}
