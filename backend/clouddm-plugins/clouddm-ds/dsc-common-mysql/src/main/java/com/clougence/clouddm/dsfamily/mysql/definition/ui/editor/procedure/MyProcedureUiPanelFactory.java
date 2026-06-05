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

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;

import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.mysql.MySQLParamModeTypes;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.definition.ui.editor.procedure.DsFamilyProcedureEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;

public class MyProcedureUiPanelFactory extends DsFamilyProcedureEditorUiPanelFactory implements MyProcedureEditorFields {

    public List<ValueDef> fetchModeTypes() {
        List<ValueDef> valueDefs = new ArrayList<>();
        for (MySQLParamModeTypes value : MySQLParamModeTypes.values()) {
            valueDefs.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()));
        }
        return valueDefs;
    }

    protected List<ValueDef> fetchTypes() {
        UiPanelField numPrecision = UiPanelField.builder()
            .field(PARAM_NUM_PRECISION)
            .type(UiPanelFieldType.Input)
            .titleI18N(MyDsI18nKeys.EDITOR_COLUMN_NUM_PRECISION_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COLUMN_NUM_PRECISION_DESC)
            .build();

        UiPanelField numScale = UiPanelField.builder()
            .field(PARAM_NUM_SCALE)
            .type(UiPanelFieldType.Input)
            .titleI18N(MyDsI18nKeys.EDITOR_COLUMN_NUM_SCALE_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COLUMN_NUM_SCALE_DESC)
            .build();

        UiPanelField datePrecision = UiPanelField.builder()
            .field(PARAM_DATE_PRECISION)
            .type(UiPanelFieldType.Input)
            .titleI18N(MyDsI18nKeys.EDITOR_COLUMN_DATE_PRECISION_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COLUMN_DATE_PRECISION_DESC)
            .build();

        UiPanelField length = UiPanelField.builder()
            .field(PARAM_LENGTH)
            .type(UiPanelFieldType.Input)
            .titleI18N(MyDsI18nKeys.EDITOR_COLUMN_LENGTH_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_COLUMN_LENGTH_DESC)
            .build();

        List<ValueDef> result = new ArrayList<>();
        for (MySQLTypes type : MySQLTypes.values()) {
            switch (type) {
                case BIT:
                case TINYINT:
                case SMALLINT:
                case MEDIUMINT:
                case INT:
                case BIGINT:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision));
                    break;
                case DECIMAL:
                case FLOAT:
                case DOUBLE:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision).addField(numScale));
                    break;
                case DATETIME:
                case TIMESTAMP:
                case TIME:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(datePrecision));
                    break;
                case CHAR:
                case VARCHAR:
                case BINARY:
                case VARBINARY:
                case BLOB:
                case TEXT:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(length));
                    break;
                case ENUM:
                case SET:
                case GEOM_COLLECTION:
                case GEOMETRY_COLLECTION:
                    // not support set
                    break;
                default:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()));
                    break;
            }
        }
        return result;
    }

}
