package com.clougence.clouddm.sdk.ui.editor.property;

import lombok.Getter;

public enum PropertyEditorI18nKeys {

    UI_PANEL_BASE_INFO_TITLE("UI_PANEL_BASE_INFO_TITLE"),
    UI_PANEL_BASE_INFO_DESC("UI_PANEL_BASE_INFO_DESC"),;

    @Getter
    private final String i18nKey;

    PropertyEditorI18nKeys(String i18nKey){
        this.i18nKey = i18nKey;
    }

}
