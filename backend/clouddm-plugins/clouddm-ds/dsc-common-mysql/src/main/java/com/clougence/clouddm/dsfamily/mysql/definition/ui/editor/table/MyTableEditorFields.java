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
package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.table;

import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorFields;
import com.clougence.adapter.mysql.MyUmiAttributeNames;

public interface MyTableEditorFields extends DsFamilyTableEditorFields {

    String SPI_PK_COLUMNS_LENGTH           = "length";
    String SPI_PK_COLUMNS_ORDER            = "order";
    String SPI_COLUMNS_CUSTOM              = "custom";
    String SPI_INDEX_COLUMNS_LENGTH        = "length";
    String SPI_INDEX_COLUMNS_ORDER         = "order";

    String FIELD_TABLE_TEMPORARY           = MyUmiAttributeNames.TEMPORARY.getCodeKey();
    String FIELD_TABLE_ENGINE              = MyUmiAttributeNames.ENGINE.getCodeKey();
    String FIELD_TABLE_CHARACTER_SET       = MyUmiAttributeNames.CHARACTER_SET.getCodeKey();
    String FIELD_TABLE_COLLATION           = MyUmiAttributeNames.COLLATION.getCodeKey();
    String FIELD_TABLE_ROWFORMAT           = MyUmiAttributeNames.ROW_FORMAT.getCodeKey();
    String FIELD_TABLE_COMPRESSION         = MyUmiAttributeNames.COMPRESSION.getCodeKey();
    String FIELD_TABLE_KEY_BLOCK_SIZE      = MyUmiAttributeNames.KEY_BLOCK_SIZE.getCodeKey();
    String FIELD_TABLE_AVG_ROW_LENGTH      = MyUmiAttributeNames.AVG_ROW_LENGTH.getCodeKey();
    String FIELD_TABLE_MIN_ROWS            = MyUmiAttributeNames.MIN_ROWS.getCodeKey();
    String FIELD_TABLE_MAX_ROWS            = MyUmiAttributeNames.MAX_ROWS.getCodeKey();
    String FIELD_TABLE_AUTOINCREMENT_VALUE = MyUmiAttributeNames.AUTO_INCREMENT_STAR.getCodeKey();

    String FIELD_COLUMN_AUTOINCREMENT      = MODE_COLUMN_AUTOINCREMENT;
    String FIELD_COLUMN_ONUPDATE           = MyUmiAttributeNames.CURRENT_UPDATE_TYPE.getCodeKey();
    String FIELD_COLUMN_CHARACTER_SET      = MyUmiAttributeNames.DEFAULT_CHARACTER_SET_NAME.getCodeKey();
    String FIELD_COLUMN_COLLATION          = MyUmiAttributeNames.DEFAULT_COLLATION_NAME.getCodeKey();
    String FIELD_COLUMN_UNSIGNED           = MyUmiAttributeNames.UNSIGNED.getCodeKey();
    String FIELD_COLUMN_ZEROFILL           = MyUmiAttributeNames.ZEROFILL.getCodeKey();
    String FIELD_COLUMN_ENUMSET            = MyUmiAttributeNames.DEFAULT_ENUMSET_COLLATION.getCodeKey();
    String FIELD_COLUMN_DEFAULT_CUSTOM     = MyTableEditorUiDataSpi.SPI_COLUMNS_CUSTOM;

    String FIELD_PRIMARY_INDEX_TYPE        = MyUmiAttributeNames.INDEX_TYPE.getCodeKey();
    String FIELD_PRIMARY_KEY_BLOCK_SIZE    = MyUmiAttributeNames.KEY_BLOCK_SIZE.getCodeKey();
    String FIELD_PRIMARY_COLUMNS_PART      = MyTableEditorUiDataSpi.SPI_PK_COLUMNS_LENGTH;
    String FIELD_PRIMARY_COLUMNS_ORDER     = MyTableEditorUiDataSpi.SPI_PK_COLUMNS_ORDER;

    String FIELD_INDEXES_INDEX_TYPE        = MyUmiAttributeNames.INDEX_TYPE.getCodeKey();
    String FIELD_INDEXES_INDEX_WAY         = MyUmiAttributeNames.INDEX_WAY.getCodeKey();
    String FIELD_INDEXES_KEY_BLOCK_SIZE    = MyUmiAttributeNames.KEY_BLOCK_SIZE.getCodeKey();
    String FIELD_INDEXES_COLUMNS_PART      = MyTableEditorUiDataSpi.SPI_INDEX_COLUMNS_LENGTH;
    String FIELD_INDEXES_COLUMNS_ORDER     = MyTableEditorUiDataSpi.SPI_INDEX_COLUMNS_ORDER;

    String FIELD_PARTITION_DATA_DIRECTORY  = MyUmiAttributeNames.PARTITION_ITEM_DATA_DIRECTORY.getCodeKey();
    String FIELD_PARTITION_INDEX_DIRECTORY = MyUmiAttributeNames.PARTITION_ITEM_INDEX_DIRECTORY.getCodeKey();
    String FIELD_PARTITION_MIN_ROWS        = MyUmiAttributeNames.PARTITION_ITEM_MIN_ROW.getCodeKey();
    String FIELD_PARTITION_MAX_ROWS        = MyUmiAttributeNames.PARTITION_ITEM_MAX_ROW.getCodeKey();

    // property
    String FIELD_TABLE_UPDATE_TIME         = MyUmiAttributeNames.UPDATE_TIME.getCodeKey();
    String FIELD_TABLE_CREATE_TIME         = MyUmiAttributeNames.CREATE_TIME.getCodeKey();
    String FIELD_TABLE_CHECK_TIME          = MyUmiAttributeNames.CHECK_TIME.getCodeKey();
    String FIELD_TABLE_INDEX_LENGTH        = MyUmiAttributeNames.INDEX_LENGTH.getCodeKey();
    String FIELD_TABLE_DATA_LENGTH         = MyUmiAttributeNames.DATA_LENGTH.getCodeKey();
    String FIELD_TABLE_DATA_ROW            = MyUmiAttributeNames.DATA_ROW.getCodeKey();
    String FIELD_TABLE_CREATE_OPTION       = MyUmiAttributeNames.CREATE_OPTIONS.getCodeKey();
    String FIELD_TABLE_DATA_FREE           = MyUmiAttributeNames.DATA_FREE.getCodeKey();

}
