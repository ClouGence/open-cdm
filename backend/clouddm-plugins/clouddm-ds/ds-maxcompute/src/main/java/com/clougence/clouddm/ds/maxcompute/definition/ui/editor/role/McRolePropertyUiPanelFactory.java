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
package com.clougence.clouddm.ds.maxcompute.definition.ui.editor.role;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.ds.maxcompute.definition.ui.editor.table.McTableEditorFields;
import com.clougence.clouddm.ds.maxcompute.i18n.McI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTablePropertyUiPanelFactory;

public class McRolePropertyUiPanelFactory extends DsFamilyTablePropertyUiPanelFactory implements McTableEditorFields {

    protected List<UiPanelField> fillBaseInfoItem(DataSourceConfig dsConfig) {
        List<UiPanelField> fields = new ArrayList<>();
        fields.add(UiPanelField.builder()
            .field(ROLE_POLICY)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_ROLE_POLICY_TITLE)
            .descI18N(McI18nKeys.EDITOR_ROLE_POLICY_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(ROLE_TYPE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(McI18nKeys.EDITOR_ROLE_TYPE_TITLE)
            .descI18N(McI18nKeys.EDITOR_ROLE_TYPE_DESC)
            .readOnly(true)
            .build());
        return fields;
    }
}
