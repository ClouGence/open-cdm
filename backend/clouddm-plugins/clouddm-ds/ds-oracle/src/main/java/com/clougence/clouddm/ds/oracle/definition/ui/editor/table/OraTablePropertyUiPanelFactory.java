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
package com.clougence.clouddm.ds.oracle.definition.ui.editor.table;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.oracle.i18n.Ora18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTablePropertyUiPanelFactory;

public class OraTablePropertyUiPanelFactory extends DsFamilyTablePropertyUiPanelFactory implements OraTableEditorFields {

    protected List<UiPanelField> fillBaseInfoItem(DataSourceConfig dsConfig) {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_SCHEMA)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_OBJ_SCHEMA_TITLE)
            .descI18N(Ora18nKeys.EDITOR_OBJ_SCHEMA_TITLE)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(CREATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_CREATE_TIME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_CREATE_TIME_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(DDL_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_LAST_DDL_TIME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_LAST_DDL_TIME_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(STATUS)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_OBJ_STATUS_TITLE)
            .descI18N(Ora18nKeys.EDITOR_OBJ_STATUS_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(FIELD_TABLE_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_NAME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(TABLESPACE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLESPACE_NAME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLESPACE_NAME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(TEMPORARY)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_TEMPORARY_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_TEMPORARY_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(PARTITION)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_PARTITION_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_PARTITION_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(CLUSTER_NAME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_CLUSTER_NAME_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_CLUSTER_NAME_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(PCT_FREE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_PCT_FREE_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_PCT_FREE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(PCT_USED)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_PCT_USED_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_PCT_USED_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(INI_TRANS)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_INI_TRANS_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_INI_TRANS_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(MAX_TRANS)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_MAX_TRANS_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_MAX_TRANS_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(INITIAL_EXTENT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_INITIAL_EXTENT_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_INITIAL_EXTENT_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(NEXT_EXTENT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_NEXT_EXTENT_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_NEXT_EXTENT_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(MIN_EXTENTS)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_MIN_EXTENTS_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_MIN_EXTENTS_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(MAX_EXTENTS)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Ora18nKeys.EDITOR_TABLE_MAX_EXTENTS_TITLE)
            .descI18N(Ora18nKeys.EDITOR_TABLE_MAX_EXTENTS_DESC)
            .readOnly(true)
            .build());

        return fields;
    }
}
