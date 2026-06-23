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

import java.util.ArrayList;
import java.util.List;

import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class UiChildren implements UiI18n {

    protected List<UiPanelField> children = new ArrayList<>();

    @Override
    public void initI18n(I18nUtils messages) {
        for (UiPanelField field : this.children) {
            field.initI18n(messages);
        }
    }

    public UiChildren addField(UiPanelField fieldDef) {
        if (fieldDef == null) {
            return this;
        }

        this.children.add(fieldDef);
        return this;
    }

    public UiPanelField findField(String name) {
        if (StringUtils.isBlank(name) || this.children.isEmpty()) {
            return null;
        }

        return this.children.stream().filter(f -> StringUtils.equals(f.getField(), name)).findFirst().orElse(null);
    }

    public void removeField(String name) {
        if (StringUtils.isBlank(name) || this.children.isEmpty()) {
            return;
        }

        UiPanelField target = this.children.stream().filter(f -> StringUtils.equals(f.getField(), name)).findFirst().orElse(null);
        if (target != null) {
            this.children.remove(target);
        }
    }

    public void beforeAddField(UiPanelField fieldDef, String beforeField) {
        UiPanelField before = findField(beforeField);
        int idx = this.children.indexOf(before);
        if (idx < 0) {
            this.children.add(fieldDef);
        } else {
            this.children.add(idx, fieldDef);
        }
    }

    public void afterAddField(UiPanelField fieldDef, String afterField) {
        UiPanelField after = findField(afterField);
        int idx = this.children.indexOf(after);
        if (idx < 0 || idx == this.children.size() - 1) {
            this.children.add(fieldDef);
        } else {
            this.children.add(idx + 1, fieldDef);
        }
    }
}
