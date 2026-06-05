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
package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.procedure;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.definition.ui.editor.function.DsFamilyFunctionPropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.definition.ui.editor.procedure.DsFamilyProcedurePropertyUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.function.MyFunctionEditorFields;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;

public class MyProcedurePropertyUiPanelFactory extends DsFamilyProcedurePropertyUiPanelFactory implements MyProcedureEditorFields {

    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(CREATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_CREATE_TIME_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_CREATE_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(UPDATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_TABLE_UPDATE_TIME_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_TABLE_UPDATE_TIME_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(DEFINER)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_VIEW_DEFINER_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_VIEW_DEFINER_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(SECURITY_TYPE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_VIEW_SECURITY_TYPE_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_VIEW_SECURITY_TYPE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(SQL_DATA_ACCESS)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_FUNCTION_PROPERTY_SQL_DATA_ACCESS_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_FUNCTION_PROPERTY_SQL_DATA_ACCESS_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(DETERMINISTIC)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(MyDsI18nKeys.EDITOR_FUNCTION_PROPERTY_OPTION_DETERMINISTIC_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_FUNCTION_PROPERTY_OPTION_DETERMINISTIC_DESC)
            .build());

        return fields;
    }
}
