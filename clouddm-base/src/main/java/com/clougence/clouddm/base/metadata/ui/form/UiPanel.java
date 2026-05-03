package com.clougence.clouddm.base.metadata.ui.form;

import com.clougence.utils.i18n.I18nUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UiPanel extends UiChildren implements UiI18n {

    private String titleI18N;
    private String descI18N;
    private String key;

    @Override
    public void initI18n(I18nUtils messages) {
        this.titleI18N = messages.getMessage(this.titleI18N);
        this.descI18N = messages.getMessage(this.descI18N);
        super.initI18n(messages);
    }

    public UiPanel addField(UiPanelField fieldDef) {
        return (UiPanel) super.addField(fieldDef);
    }
}
