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
package com.clougence.clouddm.sdk.ui.editor.view;

import lombok.Getter;

public enum ViewEditorI18nKeys {

    UI_PANEL_BASE_INFO_TITLE("UI_VIEW_EDITOR_BASE_INFO_TITLE"),
    UI_PANEL_BASE_INFO_DESC("UI_VIEW_EDITOR_BASE_INFO_DESC"),

    UI_PANEL_BODY_TITLE("UI_VIEW_EDITOR_BODY_TITLE"),
    UI_PANEL_BODY_DESC("UI_VIEW_EDITOR_BODY_DESC"),

    UI_PANEL_FEATURE_TITLE("UI_VIEW_EDITOR_FEATURE_TITLE"),
    UI_PANEL_FEATURE_DESC("UI_VIEW_EDITOR_FEATURE_DESC"),;

    @Getter
    private final String i18nKey;

    ViewEditorI18nKeys(String i18nKey){
        this.i18nKey = i18nKey;
    }
}
