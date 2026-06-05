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
package com.clougence.clouddm.faker.seed.string;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.faker.config.i18n.FakerSeedI18nKeys;
import com.clougence.clouddm.faker.config.ui.FakerUiFieldKeys;
import com.clougence.clouddm.faker.seed.SeedUiFactory;

public class StringSeedUiFactory implements SeedUiFactory, FakerUiFieldKeys {

    @Override
    public List<UiPanelField> buildSeedUi() {
        return buildSeedUiDetails();
    }

    private List<UiPanelField> buildSeedUiDetails() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(MIN_LENGTH)
            .type(UiPanelFieldType.Input)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_STRING_MINLENGTH_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_STRING_MINLENGTH_DESC)
            .build());

        fields.add(UiPanelField.builder()
            .field(MAX_LENGTH)
            .type(UiPanelFieldType.Input)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_STRING_MAXLENGTH_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_STRING_MAXLENGTH_DESC)
            .build());

        fields.add(UiPanelField.builder()
            .field(ALLOW_EMPTY)
            .type(UiPanelFieldType.Check)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_STRING_ALLOWEMPTY_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_STRING_ALLOWEMPTY_DESC)
            .build());
        return fields;
    }

}
