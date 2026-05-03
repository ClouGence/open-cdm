package com.clougence.clouddm.faker.seed.array;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.faker.config.i18n.FakerSeedI18nKeys;
import com.clougence.clouddm.faker.config.ui.FakerUiFieldKeys;
import com.clougence.clouddm.faker.seed.SeedUiFactory;

public class ArraySeedUiFactory implements SeedUiFactory, FakerUiFieldKeys {

    @Override
    public List<UiPanelField> buildSeedUi() {
        return buildSeedUiDetails();
    }

    private List<UiPanelField> buildSeedUiDetails() {
        List<UiPanelField> fields = new ArrayList<>();
        fields.add(UiPanelField.builder()
            .field(MIN_SIZE)
            .type(UiPanelFieldType.Input)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_AEEAY_MINSIZE_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_AEEAY_MINSIZE_DESC)
            .build());

        fields.add(UiPanelField.builder()
            .field(MAX_SIZE)
            .type(UiPanelFieldType.Input)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_AEEAY_MAXSIZE_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_AEEAY_MAXSIZE_DESC)
            .build());
        return fields;
    }

}
