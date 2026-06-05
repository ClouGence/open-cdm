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
package com.clougence.clouddm.sdk.ui.editor.table;

import lombok.Getter;

public enum TableEditorI18nKeys {

    UI_PANEL_INFO_TITLE("UI_TABLEEDITOR_INFO_TITLE"),
    UI_PANEL_INFO_DESC("UI_TABLEEDITOR_INFO_DESC"),

    UI_PANEL_COLUMNS_TITLE("UI_TABLEEDITOR_COLUMNS_TITLE"),
    UI_PANEL_COLUMNS_DESC("UI_TABLEEDITOR_COLUMNS_DESC"),

    UI_PANEL_KEYS_TITLE("UI_TABLEEDITOR_KEYS_TITLE"),
    UI_PANEL_KEYS_DESC("UI_TABLEEDITOR_KEYS_DESC"),

    UI_PANEL_INDEXES_TITLE("UI_TABLEEDITOR_INDEXES_TITLE"),
    UI_PANEL_INDEXES_DESC("UI_TABLEEDITOR_INDEXES_DESC"),

    UI_PANEL_PARTITIONS_TITLE("UI_TABLEEDITOR_PARTITION_TITLE"),
    UI_PANEL_PARTITIONS_DESC("UI_TABLEEDITOR_PARTITION_DESC"),

    UI_PANEL_CONSTRAINT_TITLE("UI_TABLEEDITOR_CONSTRAINT_TITLE"),
    UI_PANEL_CONSTRAINT_DESC("UI_TABLEEDITOR_CONSTRAINT_DESC"),

    UI_PANEL_FOREIGN_KEYS_TITLE("UI_TABLEEDITOR_FOREIGN_KEYS_TITLE"),
    UI_PANEL_FOREIGN_KEYS_DESC("UI_TABLEEDITOR_FOREIGN_KEYS_DESC"),

    ;

    @Getter
    private final String i18nKey;

    TableEditorI18nKeys(String i18nKey){
        this.i18nKey = i18nKey;
    }

}
