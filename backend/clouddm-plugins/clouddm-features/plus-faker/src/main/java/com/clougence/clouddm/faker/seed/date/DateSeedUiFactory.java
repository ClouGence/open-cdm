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
package com.clougence.clouddm.faker.seed.date;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.optionDef;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.faker.config.i18n.FakerSeedI18nKeys;
import com.clougence.clouddm.faker.config.ui.FakerUiFieldKeys;
import com.clougence.clouddm.faker.seed.SeedUiFactory;

public class DateSeedUiFactory implements SeedUiFactory, FakerUiFieldKeys {

    @Override
    public List<UiPanelField> buildSeedUi() {
        return buildSeedUiDetails();
    }

    private List<UiPanelField> buildSeedUiDetails() {
        List<UiPanelField> fields = new ArrayList<>();
        fields.add(UiPanelField.builder()
            .field(GEN_TYPE)
            .type(UiPanelFieldType.Options)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_GENTYPE_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_GENTYPE_DESC)
            .options(fetchGenType())
            .build());

        fields.add(UiPanelField.builder()
            .field(PRECISION)
            .type(UiPanelFieldType.Input)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_PRECISION_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_PRECISION_DESC)
            .build());
        return fields;
    }

    private List<ValueDef> fetchGenType() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_GEN_RANDOM_LABEL, "Random")
            .addField(UiPanelField.builder()
                .field(RANDOM_FORM)
                .type(UiPanelFieldType.DateTime)
                .titleI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_RANDOM_FORM_TITLE)
                .descI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_RANDOM_FORM_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(RANDOM_TO)
                .type(UiPanelFieldType.DateTime)
                .titleI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_RANDOM_TO_TITLE)
                .descI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_RANDOM_TO_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(RANDOM_ZONE_FORM)
                .type(UiPanelFieldType.Input)
                .titleI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_RANDOM_ZONE_FORM_TITLE)
                .descI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_RANDOM_ZONE_FORM_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(RANDOM_ZONE_TO)
                .type(UiPanelFieldType.Input)
                .titleI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_RANDOM_ZONE_TO_TITLE)
                .descI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_RANDOM_ZONE_TO_DESC)
                .build()));

        result.add(fieldOptionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_GEN_INTERVAL_LABEL, "Interval")
            .addField(UiPanelField.builder()
                .field(START_TIME)
                .type(UiPanelFieldType.DateTime)
                .titleI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_START_TIME_TITLE)
                .descI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_START_TIME_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(MIN_INTERVAL)
                .type(UiPanelFieldType.Input)
                .titleI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_MIN_INTERVAL_TITLE)
                .descI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_MIN_INTERVAL_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(MAX_INTERVAL)
                .type(UiPanelFieldType.Input)
                .titleI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_MAX_INTERVAL_TITLE)
                .descI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_MAX_INTERVAL_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(INTERVAL_SCOPE)
                .type(UiPanelFieldType.Options)
                .titleI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_INTERVAL_SCOPE_TITLE)
                .descI18N(FakerSeedI18nKeys.FAKER_SEED_DATE_INTERVAL_SCOPE_DESC)
                .options(fetchIntervalScope())
                .build()));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_GEN_FIXED_LABEL, "Fixed"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_GEN_SYSDATA_LABEL, "SysData"));
        return result;
    }

    private List<ValueDef> fetchIntervalScope() {
        List<ValueDef> result = new ArrayList<>();
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_YEAR_LABEL, "Year"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_MONTH_LABEL, "Month"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_DAY_LABEL, "Day"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_WEEK_LABEL, "Week"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_HOURS_LABEL, "Hours"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_MINUTE_LABEL, "Minute"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_SECOND_LABEL, "Second"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_MILLI_LABEL, "Milli"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_MICRO_LABEL, "Micro"));
        result.add(optionDef(FakerSeedI18nKeys.FAKER_SEED_DATE_NANO_LABEL, "Nano"));
        return result;
    }

}
