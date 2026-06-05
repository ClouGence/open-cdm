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
package com.clougence.clouddm.sdk.ui.faker;

import com.clougence.clouddm.base.metadata.ui.form.UiI18n;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.utils.i18n.I18nUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author olddream
 */
@Getter
@Setter
public class FakerUiPanel implements UiI18n {

    private UiPanel globalConfig = new UiPanel();
    private UiPanel tableConfig  = new UiPanel();
    private UiPanel columnConfig = new UiPanel();

    public FakerUiPanel(){
        this.globalConfig.setKey("GlobalConfig");
        this.globalConfig.setTitleI18N(FakerI18nKeys.UI_PANEL_GLOBAL_TITLE.name());
        this.globalConfig.setDescI18N(FakerI18nKeys.UI_PANEL_GLOBAL_DESC.name());

        this.tableConfig.setKey("TableConfig");
        this.tableConfig.setTitleI18N(FakerI18nKeys.UI_PANEL_TABLE_TITLE.name());
        this.tableConfig.setDescI18N(FakerI18nKeys.UI_PANEL_TABLE_DESC.name());

        this.columnConfig.setKey("ColumnConfig");
        this.columnConfig.setTitleI18N(FakerI18nKeys.UI_PANEL_COLUMN_TITLE.name());
        this.columnConfig.setDescI18N(FakerI18nKeys.UI_PANEL_COLUMN_DESC.name());
    }

    @Override
    public void initI18n(I18nUtils i18nMessages) {
        this.initI18n(this.globalConfig, i18nMessages);
        this.initI18n(this.tableConfig, i18nMessages);
        this.initI18n(this.columnConfig, i18nMessages);
    }

    public void initI18n(UiPanel uiPanel, I18nUtils i18nMessages) {
        uiPanel.setTitleI18N(i18nMessages.getMessage(uiPanel.getTitleI18N()));
        uiPanel.setDescI18N(i18nMessages.getMessage(uiPanel.getDescI18N()));

        for (UiPanelField uiField : uiPanel.getChildren()) {
            uiField.initI18n(i18nMessages);
        }
    }
}
