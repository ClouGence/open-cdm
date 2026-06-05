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
package com.clougence.clouddm.faker.seed.guid;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.optionDef;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.faker.config.i18n.FakerSeedI18nKeys;
import com.clougence.clouddm.faker.config.ui.FakerUiFieldKeys;
import com.clougence.clouddm.faker.seed.SeedUiFactory;

public class GuidSeedUiFactory implements SeedUiFactory, FakerUiFieldKeys {

    @Override
    public List<UiPanelField> buildSeedUi() {
        return buildSeedUiDetails();
    }

    private List<UiPanelField> buildSeedUiDetails() {
        List<UiPanelField> fields = new ArrayList<>();
        fields.add(UiPanelField.builder()
            .field(DATE_TYPE)
            .type(UiPanelFieldType.Options)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_GUID_DATETYPE_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_GUID_DATETYPE_DESC)
            .options(fetchDateType())
            .build());

        return fields;
    }

    private List<ValueDef> fetchDateType() {

        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GUID_STRING32_LABEL, "String32"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GUID_STRING36_LABEL, "String36"));
//        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GUID_TIMESTAMP_LABEL, "Timestamp"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_GUID_BYTES_LABEL, "Bytes"));
        return result;
    }

}
