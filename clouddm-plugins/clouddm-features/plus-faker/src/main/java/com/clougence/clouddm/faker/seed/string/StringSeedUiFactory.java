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
