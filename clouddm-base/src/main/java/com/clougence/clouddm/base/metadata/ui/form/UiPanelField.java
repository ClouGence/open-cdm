package com.clougence.clouddm.base.metadata.ui.form;

import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.utils.i18n.I18nUtils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Builder
@Getter
@Setter
public class UiPanelField extends UiChildren implements UiI18n {

    private String           field;
    private UiPanelFieldType type;
    private TypeConfig       typeConfig;
    private boolean          require;
    private boolean          readOnly;
    private boolean          addReadOnly;
    private boolean          deleteReadOnly;
    private boolean          hide;
    private ValueDef         defaultValue;
    private List<ValueDef>   options;

    private String           titleI18N;
    private String           descI18N;

    @Tolerate
    public UiPanelField(){
    }

    @Override
    public void initI18n(I18nUtils i18nMessages) {
        this.titleI18N = i18nMessages.getMessage(this.titleI18N);
        this.descI18N = i18nMessages.getMessage(this.descI18N);

        if (this.defaultValue instanceof UiI18n) {
            ((UiI18n) this.defaultValue).initI18n(i18nMessages);
        }
        if (this.options != null) {
            for (ValueDef def : this.options) {
                if (def instanceof UiI18n) {
                    ((UiI18n) def).initI18n(i18nMessages);
                }
            }
        }

        super.initI18n(i18nMessages);
    }

    public UiPanelField addField(UiPanelField fieldDef) {
        return (UiPanelField) super.addField(fieldDef);
    }

    public void applyTo(UiPanelField newValue) {
        this.type = newValue.type;
        this.require = newValue.require;
        this.readOnly = newValue.readOnly;
        this.addReadOnly = newValue.addReadOnly;
        this.deleteReadOnly = newValue.deleteReadOnly;
        this.defaultValue = newValue.defaultValue;
        this.options = newValue.options;
        this.titleI18N = newValue.titleI18N;
        this.descI18N = newValue.descI18N;
        this.children = newValue.children;
        this.typeConfig = newValue.typeConfig;
    }
}
