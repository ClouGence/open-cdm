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
