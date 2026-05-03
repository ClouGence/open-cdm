package com.clougence.clouddm.faker.seed.number;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.faker.config.i18n.FakerSeedI18nKeys;
import com.clougence.clouddm.faker.config.ui.FakerUiFieldKeys;
import com.clougence.clouddm.faker.seed.SeedUiFactory;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

public class NumberSeedUiFactory implements SeedUiFactory, FakerUiFieldKeys {

    @Override
    public List<UiPanelField> buildSeedUi() {
        return buildSeedUiDetails();
    }

    private List<UiPanelField> buildSeedUiDetails() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(NUMBER_TYPE)
            .type(UiPanelFieldType.Options)
            .defaultValue(strValueDef(NumberType.Integer.name()))
            .options(fetchNumberTypes())
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_NUMBER_TYPE_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_NUMBER_TYPE_DESC)
            .build());

        fields.add(UiPanelField.builder()
            .field(MIN)
            .type(UiPanelFieldType.Input)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_NUMBER_MIN_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_NUMBER_MIN_DESC)
            .build());

        fields.add(UiPanelField.builder()
            .field(MAX)
            .type(UiPanelFieldType.Input)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_NUMBER_MAX_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_NUMBER_MAX_DESC)
            .build());

        fields.add(UiPanelField.builder()
            .field(PRECISION)
            .type(UiPanelFieldType.Input)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_NUMBER_PRECISION_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_NUMBER_PRECISION_DESC)
            .build());

        fields.add(UiPanelField.builder()
            .field(SCALE)
            .type(UiPanelFieldType.Input)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_NUMBER_SCALE_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_NUMBER_SCALE_DESC)
            .build());

        fields.add(UiPanelField.builder()
            .field(ABS)
            .type(UiPanelFieldType.Check)
            .titleI18N(FakerSeedI18nKeys.FAKER_SEED_NUMBER_ABS_TITLE)
            .descI18N(FakerSeedI18nKeys.FAKER_SEED_NUMBER_ABS_DESC)
            .build());
        return fields;
    }

    private List<ValueDef> fetchNumberTypes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(NumberType.Bool.name(), NumberType.Bool.name()));
        result.add(fieldOptionDef(NumberType.Byte.name(), NumberType.Byte.name()));
        result.add(fieldOptionDef(NumberType.Short.name(), NumberType.Short.name()));
        result.add(fieldOptionDef(NumberType.Integer.name(), NumberType.Integer.name()));
        result.add(fieldOptionDef(NumberType.Int.name(), NumberType.Int.name()));
        result.add(fieldOptionDef(NumberType.Long.name(), NumberType.Long.name()));
        result.add(fieldOptionDef(NumberType.Float.name(), NumberType.Float.name()));
        result.add(fieldOptionDef(NumberType.Double.name(), NumberType.Double.name()));
        result.add(fieldOptionDef(NumberType.BigInt.name(), NumberType.BigInt.name()));
        result.add(fieldOptionDef(NumberType.Decimal.name(), NumberType.Decimal.name()));
        return result;
    }

}
