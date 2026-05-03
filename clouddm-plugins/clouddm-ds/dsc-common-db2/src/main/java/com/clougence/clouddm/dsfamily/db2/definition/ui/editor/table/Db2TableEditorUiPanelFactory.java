package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.table;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.db2.Db2Types;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.definition.ui.editor.table.DsFamilyTableEditorUiPanelFactory;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;

/**
 * @author Ekko
 * @date 2023/9/27 10:24
*/
public abstract class Db2TableEditorUiPanelFactory extends DsFamilyTableEditorUiPanelFactory implements Db2TableEditorFields {

    // tableEditor Columns panel
    @Override
    protected ValueDef fillTableColumnsUiPanelForPreferenceColumnType(List<ValueDef> fetchTypes) {
        return strValueDef(Db2Types.INTEGER.getCodeKey());
    }

    @Override
    protected List<ValueDef> fetchTypesUseBasic(TableEditorUiPanel uiPanel, DataSourceConfig dsConfig, Connection con,  //
                                                UiPanelField numPrecision, UiPanelField numScale, UiPanelField datePrecision, UiPanelField length) {
        List<ValueDef> result = new ArrayList<>();
        for (Db2Types type : Db2Types.values()) {
            switch (type) {
                case DECIMAL:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(numPrecision).addField(numScale));
                    break;
                case CHARACTER:
                case VARCHAR:
                case GRAPHIC:
                case VARGRAPHIC:
                case CLOB:
                case DBCLOB:
                case CHAR_FOR_BIT_DATA:
                case VARCHAR_FOR_BIT_DATA:
                case BLOB:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(length));
                    break;
                case TIMESTAMP:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()).addField(datePrecision));
                    break;
                default:
                    result.add(fieldOptionDef(type.getCodeKey(), type.getCodeKey()));
            }
        }
        return result;
    }
}
