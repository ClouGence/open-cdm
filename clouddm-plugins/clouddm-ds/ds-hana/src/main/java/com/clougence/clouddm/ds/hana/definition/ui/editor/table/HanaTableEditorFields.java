package com.clougence.clouddm.ds.hana.definition.ui.editor.table;

import com.clougence.adapter.hana.HanaAttributeNames;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorFields;

/**
 * @author chunlin
 * @date 2024/4/2
 */
public interface HanaTableEditorFields extends DsFamilyTableEditorFields {

    String SPI_PK_COLUMNS_LENGTH       = "length";
    String SPI_PK_COLUMNS_ORDER        = "order";
    String SPI_COLUMNS_CUSTOM          = "custom";
    String SPI_INDEX_COLUMNS_LENGTH    = "length";
    String SPI_INDEX_COLUMNS_ORDER     = "order";

    String FIELD_COLUMN_DEFAULT_CUSTOM = HanaTableEditorUiDataSpi.SPI_COLUMNS_CUSTOM;
    String FIELD_COLUMN_AUTOINCREMENT  = MODE_COLUMN_AUTOINCREMENT;

    String FIELD_INDEXES_INDEX_TYPE    = HanaAttributeNames.INDEX_WAY.getCodeKey();
    String FIELD_INDEXES_COLUMNS_PART  = HanaTableEditorFields.SPI_INDEX_COLUMNS_LENGTH;
    String FIELD_INDEXES_COLUMNS_ORDER = HanaTableEditorFields.SPI_INDEX_COLUMNS_ORDER;

    String FIELD_PRIMARY_COLUMNS_ORDER = HanaTableEditorUiDataSpi.SPI_PK_COLUMNS_ORDER;
}
