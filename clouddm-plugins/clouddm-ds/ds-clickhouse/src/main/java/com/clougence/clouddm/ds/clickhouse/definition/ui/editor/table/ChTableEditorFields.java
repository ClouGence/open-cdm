package com.clougence.clouddm.ds.clickhouse.definition.ui.editor.table;

import com.clougence.adapter.clickhouse.ClickHouseAttributeNames;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorFields;

public interface ChTableEditorFields extends DsFamilyTableEditorFields {

    String FIELD_TABLE_ENGINE         = ClickHouseAttributeNames.ENGINE.getCodeKey();
    String FIELD_TABLE_ENGINE_FULL    = ClickHouseAttributeNames.ENGINE_FULL.getCodeKey();
    String FIELD_TABLE_DATA_PATH      = ClickHouseAttributeNames.DATA_PATH.getCodeKey();
    String FIELD_TABLE_METADATA_PATH  = ClickHouseAttributeNames.METADATA_PATH.getCodeKey();
    String FIELD_TABLE_TEMPORARY      = ClickHouseAttributeNames.TEMPORARY.getCodeKey();

    String FIELD_COLUMN_PARTITION_KEY = ClickHouseAttributeNames.PARTITION_KEY.getCodeKey();
    String FIELD_COLUMN_SORTING_KEY   = ClickHouseAttributeNames.SORTING_KEY.getCodeKey();
    String FIELD_COLUMN_SAMPLING_KEY  = ClickHouseAttributeNames.SAMPLING_KEY.getCodeKey();
    String FIELD_COLUMN_DATETIME_ZONE = ClickHouseAttributeNames.DATETIME_ZONE.getCodeKey();
}
