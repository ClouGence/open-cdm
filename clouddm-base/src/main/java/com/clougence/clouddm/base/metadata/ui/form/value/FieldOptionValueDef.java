package com.clougence.clouddm.base.metadata.ui.form.value;

import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiChildren;
import com.clougence.clouddm.base.metadata.ui.form.UiI18n;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.utils.i18n.I18nUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Builder
@Getter
@Setter
public class FieldOptionValueDef extends UiChildren implements ValueDef, UiI18n {

    private String  labelI18N;
    private String  descI18N;
    private Object  value;
    private boolean readOnly;

    @Tolerate
    public FieldOptionValueDef(){
    }

    @Override
    public Object asValue() {
        return this.value;
    }

    @Override
    public void initI18n(I18nUtils i18nMessages) {
        this.labelI18N = i18nMessages.getMessage(this.labelI18N);
        this.descI18N = i18nMessages.getMessage(this.descI18N);
        super.initI18n(i18nMessages);
    }

    public FieldOptionValueDef addField(UiPanelField fieldDef) {
        return (FieldOptionValueDef) super.addField(fieldDef);
    }

    public FieldOptionValueDef addFields(List<UiPanelField> fieldDefs) {
        FieldOptionValueDef optionValueDef = new FieldOptionValueDef();
        for (UiPanelField fieldDef : fieldDefs) {
            optionValueDef = (FieldOptionValueDef) super.addField(fieldDef);
        }
        return optionValueDef;
    }
}
