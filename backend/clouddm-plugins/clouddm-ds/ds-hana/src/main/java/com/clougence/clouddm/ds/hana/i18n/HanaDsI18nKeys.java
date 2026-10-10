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
package com.clougence.clouddm.ds.hana.i18n;

import com.clougence.clouddm.dsfamily.i18n.DsDataEditorI18nKeys;
import com.clougence.clouddm.dsfamily.i18n.DsTableEditorI18nKeys;
import com.clougence.clouddm.dsfamily.i18n.DsTriggerEditorI18nKeys;
import com.clougence.clouddm.dsfamily.i18n.DsViewEditorI18nKeys;
import com.clougence.utils.i18n.I18nResource;

/**
 * @author chunlin
 * @date 2024/4/2
 */
@I18nResource("/META-INF/clougence/i18n/hana-ui-editor-table")
public interface HanaDsI18nKeys extends DsDataEditorI18nKeys, DsTableEditorI18nKeys, DsTriggerEditorI18nKeys, DsViewEditorI18nKeys {

    String PLUGIN_NAME_HANA                              = "PLUGIN_NAME_HANA";
    String EDITOR_INDEXES_TYPE_BTREE_LABEL               = "UI_EDITOR_INDEXES_TYPE_BTREE_LABEL";
    String EDITOR_INDEXES_TYPE_CPBTREE_LABEL             = "UI_EDITOR_INDEXES_TYPE_CPBTREE_LABEL";
    String EDITOR_INDEXES_TYPE_INVERTED_LABEL            = "UI_EDITOR_INDEXES_TYPE_INVERTED_LABEL";
    String EDITOR_INDEXES_TYPE_FULLTEXT_LABEL            = "UI_EDITOR_INDEXES_TYPE_FULLTEXT_LABEL";
    String EDITOR_COMM_COLUMNS_ORDER_TITLE               = "UI_EDITOR_COMM_COLUMNS_ORDER_TITLE";
    String EDITOR_COMM_COLUMNS_ORDER_DESC                = "UI_EDITOR_COMM_COLUMNS_ORDER_DESC";
    String EDITOR_COMM_COLUMNS_ORDER_ASC_LABEL           = "UI_EDITOR_COMM_COLUMN_ORDER_ASC_LABEL";
    String EDITOR_COMM_COLUMNS_ORDER_DESC_LABEL          = "UI_EDITOR_COMM_COLUMN_ORDER_DESC_LABEL";
    String EDITOR_INDEXES_INDEX_TYPE_TITLE               = "UI_EDITOR_INDEXES_INDEX_TYPE_TITLE";
    String EDITOR_INDEXES_INDEX_TYPE_DESC                = "UI_EDITOR_INDEXES_INDEX_TYPE_DESC";
    String EDITOR_COLUMNS_AUTOINCREMENT_TITLE            = "UI_EDITOR_COLUMNS_AUTOINCREMENT_TITLE";
    String EDITOR_COLUMNS_AUTOINCREMENT_DESC             = "UI_EDITOR_COLUMNS_AUTOINCREMENT_DESC";
    String EDITOR_INDEXES_TYPE_INVERTED_VALUE_LABEL      = "UI_EDITOR_INDEXES_TYPE_INVERTED_VALUE_LABEL";
    String EDITOR_INDEXES_TYPE_INVERTED_HASH_LABEL       = "UI_EDITOR_INDEXES_TYPE_INVERTED_HASH_LABEL";
    String EDITOR_INDEXES_TYPE_INVERTED_INDIVIDUAL_LABEL = "UI_EDITOR_INDEXES_TYPE_INVERTED_INDIVIDUAL_LABEL";
    String EDITOR_INDEXES_TYPE_GEOCODE_LABEL             = "UI_EDITOR_INDEXES_TYPE_GEOCODE_LABEL";

    String EDITOR_TABLE_STORAGE_TITLE                    = "HANA_EDITOR_TABLE_STORAGE_TITLE";
    String EDITOR_TABLE_STORAGE_DESC                     = "HANA_EDITOR_TABLE_STORAGE_DESC";
    String EDITOR_TABLE_ROW_LABEL                        = "HANA_EDITOR_TABLE_ROW_LABEL";
    String EDITOR_TABLE_COLUMN_LABEL                     = "HANA_EDITOR_TABLE_COLUMN_LABEL";

    String HANA_SCRIPT_UNSUPPORTED                       = "HANA_SCRIPT_UNSUPPORTED";
    String HANA_TABLE_TYPE_UNSUPPORTED                   = "HANA_TABLE_TYPE_UNSUPPORTED";
    String HANA_INDEX_NATIVE_DDL                         = "HANA_INDEX_NATIVE_DDL";
    String HANA_TRIGGER_NATIVE_DDL                       = "HANA_TRIGGER_NATIVE_DDL";
    String HANA_PAGE_RANGE_INVALID                       = "HANA_PAGE_RANGE_INVALID";
    String HANA_COLUMN_UNKNOWN                           = "HANA_COLUMN_UNKNOWN";
    String HANA_COLUMN_READ_ONLY                         = "HANA_COLUMN_READ_ONLY";
    String HANA_NO_INSERT_COLUMN                         = "HANA_NO_INSERT_COLUMN";
    String HANA_COLUMN_NOT_EDITABLE                      = "HANA_COLUMN_NOT_EDITABLE";
    String HANA_NO_UPDATE_COLUMN                         = "HANA_NO_UPDATE_COLUMN";
    String HANA_ROW_KEY_MISSING                          = "HANA_ROW_KEY_MISSING";
    String HANA_ROW_KEY_REQUIRED                         = "HANA_ROW_KEY_REQUIRED";
    String HANA_AFFECTED_ROWS_UNEXPECTED                 = "HANA_AFFECTED_ROWS_UNEXPECTED";
    String HANA_GENERATION_NATIVE_DDL                    = "HANA_GENERATION_NATIVE_DDL";
    String HANA_GENERATED_PROPERTIES_NATIVE_DDL          = "HANA_GENERATED_PROPERTIES_NATIVE_DDL";
    String HANA_DECIMAL_SCALE_INVALID                    = "HANA_DECIMAL_SCALE_INVALID";
    String HANA_TYPE_UNSUPPORTED                         = "HANA_TYPE_UNSUPPORTED";
    String HANA_TYPE_PARAMETER_RANGE                     = "HANA_TYPE_PARAMETER_RANGE";
    String HANA_CHECK_NATIVE_DDL                         = "HANA_CHECK_NATIVE_DDL";
    String HANA_CONSTRAINT_CONVERSION                    = "HANA_CONSTRAINT_CONVERSION";
    String HANA_INDEX_CONVERSION                         = "HANA_INDEX_CONVERSION";
    String HANA_GENERATED_CONVERSION                     = "HANA_GENERATED_CONVERSION";
    String HANA_DECIMAL_CONVERSION                       = "HANA_DECIMAL_CONVERSION";
    String HANA_LENGTH_CONVERSION                        = "HANA_LENGTH_CONVERSION";

}
