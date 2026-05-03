package com.clougence.clouddm.base.metadata.ui.form.value;

import com.clougence.clouddm.base.metadata.ui.form.UiI18n;
import com.clougence.utils.i18n.I18nUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Builder
@Getter
@Setter
public final class OptionValueDef implements ValueDef, UiI18n {

    private String labelI18N;
    private String value;

    @Tolerate
    public OptionValueDef(){
    }

    @Override
    public Object asValue() {
        return this.value;
    }

    @Override
    public void initI18n(I18nUtils i18nMessages) {
        this.labelI18N = i18nMessages.getMessage(this.labelI18N);
    }
}
