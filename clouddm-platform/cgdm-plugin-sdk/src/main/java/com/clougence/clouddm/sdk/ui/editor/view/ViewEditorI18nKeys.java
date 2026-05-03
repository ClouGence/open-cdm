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
