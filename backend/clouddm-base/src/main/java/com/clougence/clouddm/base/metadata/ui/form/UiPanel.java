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
