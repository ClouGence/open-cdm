package com.clougence.clouddm.ds.starrocks.definition.ui.editor.table;

import com.clougence.adapter.starrocks.StarRocksAttributeNames;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorFields;

/**
 * @Author: Ekko
 * @Date: 2023-12-13 15:20
 */
public interface SrTableEditorFields extends DsFamilyTableEditorFields {

    String SPI_TABLE_DISTRIBUTED_COLUMNS_NAME      = "name";
    String SPI_TABLE_ORDER_BY_COLUMNS_NAME         = "name";

    String FIELD_MODEL_KEY_NAME                    = MODE_KEY_NAME;
    String FIELD_TABLE_DISTRIBUTED_BY_COLUMNS_NAME = SPI_TABLE_DISTRIBUTED_COLUMNS_NAME;
    String FIELD_TABLE_ORDER_BY_COLUMNS_NAME       = SPI_TABLE_ORDER_BY_COLUMNS_NAME;

    String FIELD_TABLE_DISTRIBUTED_BY_COLUMNS      = StarRocksAttributeNames.DISTRIBUTED_BY_COLUMNS.getCodeKey();
    String FIELD_TABLE_DISTRIBUTED_BY              = StarRocksAttributeNames.DISTRIBUTED_BY_TYPE.getCodeKey();
    String FIELD_TABLE_ORDER_BY                    = StarRocksAttributeNames.ORDER_BY.getCodeKey();
    String FIELD_TABLE_BUCKET_NUMBER               = StarRocksAttributeNames.BUCKET_NUMBER.getCodeKey();
    String FIELD_TABLE_ENGINE                      = StarRocksAttributeNames.ENGINE.getCodeKey();
    String FIELD_TABLE_EXTERNAL                    = StarRocksAttributeNames.EXTERNAL.getCodeKey();
    String FIELD_INDEX_TYPE                        = StarRocksAttributeNames.INDEX_TYPE.getCodeKey();
    String FIELD_COLUMN_AGG_TYPE                   = StarRocksAttributeNames.AGG_TYPE.getCodeKey();
    String FIELD_COLUMN_AUTOINCREMENT              = StarRocksAttributeNames.AUTO_INCREMENT.getCodeKey();
}
