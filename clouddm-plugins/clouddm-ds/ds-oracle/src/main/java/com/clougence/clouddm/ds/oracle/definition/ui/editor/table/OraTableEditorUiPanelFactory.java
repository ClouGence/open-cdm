package com.clougence.clouddm.ds.oracle.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.optionDef;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.oracle.OracleSqlTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsDataEditorI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

/**
 * @Author: mode
 * @Date: 2023-08-29 15:21
 */
public class OraTableEditorUiPanelFactory extends DsFamilyTableEditorUiPanelFactory implements OraTableEditorFields {

    // tableEditor Columns panel
    @Override
    protected List<ValueDef> fetchDefault() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_NULL_LABEL, "NULL"));
        result.add(fieldOptionDef(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_LABEL, "CUSTOM").addField(UiPanelField.builder()
            .field(FIELD_COLUMN_DEFAULT)
            .type(UiPanelFieldType.Input)
            .titleI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_TITLE)
            .descI18N(DsDataEditorI18nKeys.EDITOR_COLUMN_DEFAULT_CUSTOM_DESC)
            .build()));

        return result;
    }

    @Override
    protected List<ValueDef> fetchTypesUseBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con,  //
                                                UiPanelField numPrecision, UiPanelField numScale, UiPanelField datePrecision, UiPanelField length) {
        List<ValueDef> result = new ArrayList<>();
        for (OracleSqlTypes value : OracleSqlTypes.values()) {
            switch (value) {
                case RAW:
                case CHAR:
                case NCHAR:
                case VARCHAR2:
                case NVARCHAR:
                case NVARCHAR2: {
                    result.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()).addField(length));
                    break;
                }
                // case NUMBER:
                case NUMBER_BIGINT:
                case NUMBER_DECIMAL: {
                    result.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()).addField(numPrecision).addField(numScale));
                    break;
                }
                case FLOAT: {
                    result.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()).addField(numPrecision));
                    break;
                }
                case TIMESTAMP:
                case TIMESTAMP_WITH_TIME_ZONE:
                case TIMESTAMP_WITH_LOCAL_TIME_ZONE: {
                    result.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()).addField(datePrecision));
                    break;
                }
                case OBJECT:
                case VARRAY:
                case NESTED_TABLE:
                case PLSQL_BOOLEAN:
                    break;
                default:
                    result.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()));
                    break;
            }

        }

        return result;
    }
}
