package com.clougence.clouddm.ds.sqlserver.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.clougence.adapter.sqlserver.SqlServerTypes;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorUiPanelFactory;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.utils.HashUtils;

/**
 * @Author: Ekko
 * @Date: 2023-08-29 15:21
 */
public class MsSqlTableEditorUiPanelFactory extends DsFamilyTableEditorUiPanelFactory implements MsSqlTableEditorFields {

    // tableEditor Columns panel
    @Override
    protected List<ValueDef> fetchTypesUseBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con,  //
                                                UiPanelField numPrecision, UiPanelField numScale, UiPanelField datePrecision, UiPanelField length) {
        List<ValueDef> result = new ArrayList<>();

        for (SqlServerTypes value : SqlServerTypes.values()) {
            switch (value) {
                case DECIMAL:
                case NUMERIC: {
                    result.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()).addField(numPrecision).addField(numScale));
                    break;
                }
                case FLOAT: {
                    result.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()).addField(numPrecision));
                    break;
                }
                case DATETIMEOFFSET:
                case DATETIME2:
                case TIME: {
                    result.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()).addField(datePrecision));
                    break;
                }
                case CHAR:
                case VARCHAR:
                case NCHAR:
                case NVARCHAR:
                case BINARY:
                case VARBINARY: {
                    result.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()).addField(length));
                    break;
                }
                default:
                    result.add(fieldOptionDef(value.getCodeKey(), value.getCodeKey()));
                    break;
            }
        }

        return result;
    }

    // tableEditor Keys panel
    @Override
    protected void fillTableKeysUiPanelForBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        super.fillTableKeysUiPanelForBasic(uiPanel, dsConfig, viewMode, con);

        UiPanelField nameField = uiPanel.getKeys().findField(FIELD_PRIMARY_NAME);
        nameField.setHide(false);
        nameField.setReadOnly(false);
        if (viewMode == EditorViewMode.Create) {
            String pkName = ("pk_" + Long.toString(HashUtils.fnvHash(UUID.randomUUID().toString()), 16)).toUpperCase();
            nameField.setDefaultValue(strValueDef(pkName));
        }
    }
}
