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
