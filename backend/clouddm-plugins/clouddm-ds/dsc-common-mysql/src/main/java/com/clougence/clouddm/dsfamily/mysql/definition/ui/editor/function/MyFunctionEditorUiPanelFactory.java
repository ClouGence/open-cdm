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
package com.clougence.clouddm.dsfamily.mysql.definition.ui.editor.function;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.boolValueDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.config.FoldTypeConfig;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.definition.ui.editor.function.DsFamilyFunctionEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsFunctionEditorI18nKeys;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyDsI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;

public class MyFunctionEditorUiPanelFactory extends DsFamilyFunctionEditorUiPanelFactory implements MyFunctionEditorFields {

    protected void fetchOptionUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        UiPanelField features = UiPanelField.builder()
            .field(FEATURES)
            .type(UiPanelFieldType.Fold)
            .typeConfig(new FoldTypeConfig())
            .titleI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_FEATURES_TITLE)
            .descI18N(DsFunctionEditorI18nKeys.EDITOR_FUNCTION_FEATURES_DESC)
            .build();

        features.addField(UiPanelField.builder()
            .type(UiPanelFieldType.Check)
            .defaultValue(boolValueDef(false))
            .field(DETERMINISTIC)
            .titleI18N(MyDsI18nKeys.EDITOR_FUNCTION_OPTION_DETERMINISTIC_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_FUNCTION_OPTION_DETERMINISTIC_DESC)
            .build());

        features.addField(UiPanelField.builder()
            .type(UiPanelFieldType.Options)
            .field(SQL_DATA_ACCESS)
            .titleI18N(MyDsI18nKeys.EDITOR_FUNCTION_SQL_DATA_ACCESS_TITLE)
            .descI18N(MyDsI18nKeys.EDITOR_FUNCTION_SQL_DATA_ACCESS_DESC)
            .options(fetchSqlDataAccess())
            .build());

        uiPanel.addField(features);
    }

    private List<ValueDef> fetchSqlDataAccess() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef("NO SQL", "NO SQL"));
        result.add(fieldOptionDef("READS SQL DATA", "READS SQL DATA"));
        return result;
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
