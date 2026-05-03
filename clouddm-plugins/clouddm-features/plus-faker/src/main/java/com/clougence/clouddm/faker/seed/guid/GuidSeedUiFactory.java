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
