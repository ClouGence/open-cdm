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
