package com.clougence.clouddm.sdk.ui.editor.property;

import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.utils.i18n.I18nUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyUiPanel {

    private UiPanel baseInfo = new UiPanel();
    //    private UiPanel columns     = new UiPanel();
    //    private UiPanel keys        = new UiPanel();
    //    private UiPanel foreignKeys = new UiPanel();
    //    private UiPanel indexes     = new UiPanel();
    //    private UiPanel partitions  = new UiPanel();
    //    private UiPanel constraints = new UiPanel();

    public PropertyUiPanel(){
        this.baseInfo.setKey("baseInfo");
        this.baseInfo.setTitleI18N(PropertyEditorI18nKeys.UI_PANEL_BASE_INFO_TITLE.getI18nKey());
        this.baseInfo.setDescI18N(PropertyEditorI18nKeys.UI_PANEL_BASE_INFO_DESC.getI18nKey());

        //        this.columns.setKey("TableColumns");
        //        this.columns.setTitleI18N(TableEditorI18nKeys.UI_PANEL_COLUMNS_TITLE.getI18nKey());
        //        this.columns.setDescI18N(TableEditorI18nKeys.UI_PANEL_COLUMNS_DESC.getI18nKey());
        //
        //        this.keys.setKey("TableKeys");
        //        this.keys.setTitleI18N(TableEditorI18nKeys.UI_PANEL_KEYS_TITLE.getI18nKey());
        //        this.keys.setDescI18N(TableEditorI18nKeys.UI_PANEL_KEYS_DESC.getI18nKey());
        //
        //        this.indexes.setKey("TableIndexes");
        //        this.indexes.setTitleI18N(TableEditorI18nKeys.UI_PANEL_INDEXES_TITLE.getI18nKey());
        //        this.indexes.setDescI18N(TableEditorI18nKeys.UI_PANEL_INDEXES_DESC.getI18nKey());
        //
        //        this.partitions.setKey("TablePartitions");
        //        this.partitions.setTitleI18N(TableEditorI18nKeys.UI_PANEL_PARTITIONS_TITLE.getI18nKey());
        //        this.partitions.setDescI18N(TableEditorI18nKeys.UI_PANEL_PARTITIONS_DESC.getI18nKey());
        //
        //        this.constraints.setKey("TableConstraints");
        //        this.constraints.setTitleI18N(TableEditorI18nKeys.UI_PANEL_CONSTRAINT_TITLE.getI18nKey());
        //        this.constraints.setDescI18N(TableEditorI18nKeys.UI_PANEL_CONSTRAINT_DESC.getI18nKey());
        //
        //        this.foreignKeys.setKey("TableForeignKeys");
        //        this.foreignKeys.setTitleI18N(TableEditorI18nKeys.UI_PANEL_FOREIGN_KEYS_TITLE.getI18nKey());
        //        this.foreignKeys.setDescI18N(TableEditorI18nKeys.UI_PANEL_FOREIGN_KEYS_DESC.getI18nKey());
    }

    public void initI18n(I18nUtils i18nMessages) {
        this.initI18n(this.baseInfo, i18nMessages);
        //        this.initI18n(this.columns, i18nMessages);
        //        this.initI18n(this.keys, i18nMessages);
        //        this.initI18n(this.indexes, i18nMessages);
        //        this.initI18n(this.partitions, i18nMessages);
        //        this.initI18n(this.constraints, i18nMessages);
        //        this.initI18n(this.foreignKeys, i18nMessages);
    }

    public void initI18n(UiPanel uiPanel, I18nUtils i18nMessages) {
        uiPanel.setTitleI18N(i18nMessages.getMessage(uiPanel.getTitleI18N()));
        uiPanel.setDescI18N(i18nMessages.getMessage(uiPanel.getDescI18N()));

        for (UiPanelField uiField : uiPanel.getChildren()) {
            uiField.initI18n(i18nMessages);
        }
    }
}
