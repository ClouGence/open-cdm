package com.clougence.clouddm.faker.config.ui;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.faker.config.i18n.FakerI18nKeys;
import com.clougence.clouddm.faker.seed.SeedType;
import com.clougence.clouddm.sdk.ui.faker.FakerUiPanel;

/**
 * @author olddream
 */
public class FakerUiPanelFactory implements FakerUiFieldKeys {

    public FakerUiPanel createFakerUiPanel() {
        FakerUiPanel uiPanel = new FakerUiPanel();
        fillGlobalConfigUiPanel(uiPanel);
        fillTableConfigUiPanel(uiPanel);
        fillColumnConfigUiPanel(uiPanel);
        return uiPanel;
    }

    public FakerUiPanel createIncrementFakerUiPanel() {
        FakerUiPanel uiPanel = new FakerUiPanel();
        fillGlobalConfigUiPanel(uiPanel);
        fillIncrementGlobalConfigUiPanel(uiPanel);

        fillIncrementTableConfigUiPanel(uiPanel);

        fillColumnConfigUiPanel(uiPanel);
        fillIncrementConfigUiPanel(uiPanel);
        return uiPanel;
    }

    private void fillIncrementTableConfigUiPanel(FakerUiPanel uiPanel) {
        uiPanel.getTableConfig()
            .addField(UiPanelField.builder()
                .field(TABLE_TOTAL)
                .type(UiPanelFieldType.Input)
                .require(true)
                .defaultValue(strValueDef("1"))
                .titleI18N(FakerI18nKeys.FAKER_TABLECONFIG_TOTAL_TITLE)
                .descI18N(FakerI18nKeys.FAKER_TABLECONFIG_TOTAL_DESC)
                .hide(true)
                .build());

        uiPanel.getTableConfig()
            .addField(UiPanelField.builder()
                .field(TABLE_INSERT_POLITIC)
                .type(UiPanelFieldType.Options)
                .require(true)
                .options(fetchPoliticOption())
                .defaultValue(strValueDef("FullCol"))
                .titleI18N(FakerI18nKeys.FAKER_TABLECONFIG_INSERTPOLITIC_TITLE)
                .descI18N(FakerI18nKeys.FAKER_TABLECONFIG_INSERTOLITIC_DESC)
                .build());

        uiPanel.getTableConfig()
            .addField(UiPanelField.builder()
                .field(TABLE_UPDATE_POLITIC)
                .type(UiPanelFieldType.Options)
                .require(true)
                .options(fetchPoliticOption())
                .defaultValue(strValueDef("FullCol"))
                .titleI18N(FakerI18nKeys.FAKER_TABLECONFIG_UPDATEPOLITIC_TITLE)
                .descI18N(FakerI18nKeys.FAKER_TABLECONFIG_UPDATEPOLITIC_DESC)
                .build());

        uiPanel.getTableConfig()
            .addField(UiPanelField.builder()
                .field(TABLE_WHERE_POLITIC)
                .type(UiPanelFieldType.Options)
                .require(true)
                .options(fetchPoliticOption())
                .defaultValue(strValueDef("KeyCol"))
                .titleI18N(FakerI18nKeys.FAKER_TABLECONFIG_WHEREPOLITIC_TITLE)
                .descI18N(FakerI18nKeys.FAKER_TABLECONFIG_WHEREPOLITIC_DESC)
                .build());

    }

    private void fillIncrementConfigUiPanel(FakerUiPanel uiPanel) {
        uiPanel.getColumnConfig()
            .addField(UiPanelField.builder()
                .field(COLUMN_INSERT)
                .type(UiPanelFieldType.Check)
                .readOnly(true)
                .titleI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_INSERT_TITLE)
                .descI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_INSERT_DESC)
                .defaultValue(strValueDef("false"))
                .build());

        uiPanel.getColumnConfig()
            .addField(UiPanelField.builder()
                .field(COLUMN_UPDATE_SET)
                .type(UiPanelFieldType.Check)
                .readOnly(true)
                .titleI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_UPDATESET_TITLE)
                .descI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_UPDATESET_DESC)
                .defaultValue(strValueDef("false"))
                .build());

        uiPanel.getColumnConfig()
            .addField(UiPanelField.builder()
                .field(COLUMN_UPDATE_WHERE)
                .type(UiPanelFieldType.Check)
                .readOnly(true)
                .titleI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_UPDATEWHERE_TITLE)
                .descI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_UPDATEWHERE_DESC)
                .defaultValue(strValueDef("false"))
                .build());

        uiPanel.getColumnConfig()
            .addField(UiPanelField.builder()
                .field(COLUMN_DELETE_WHERE)
                .type(UiPanelFieldType.Check)
                .readOnly(true)
                .titleI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_DELETEWHERE_TITLE)
                .descI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_DELETEWHERE_DESC)
                .defaultValue(strValueDef("false"))
                .build());
    }

    private void fillIncrementGlobalConfigUiPanel(FakerUiPanel uiPanel) {
        uiPanel.getGlobalConfig()
            .addField(UiPanelField.builder()
                .field(GLOBAL_TIME)
                .type(UiPanelFieldType.Input)
                .titleI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_TIME_TITLE)
                .descI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_TIME_DESC)
                .require(true)
                .defaultValue(strValueDef("30"))
                .build());
        uiPanel.getGlobalConfig()
            .addField(UiPanelField.builder()
                .field(GLOBAL_INSERT_RATIO)
                .type(UiPanelFieldType.Input)
                .titleI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_INSERT_TITLE)
                .descI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_INSERT_DESC)
                .require(true)
                .defaultValue(strValueDef("30"))
                .build());
        uiPanel.getGlobalConfig()
            .addField(UiPanelField.builder()
                .field(GLOBAL_UPDATE_RATIO)
                .type(UiPanelFieldType.Input)
                .titleI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_UPDATE_TITLE)
                .descI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_UPDATE_DESC)
                .require(true)
                .defaultValue(strValueDef("30"))
                .build());
        uiPanel.getGlobalConfig()
            .addField(UiPanelField.builder()
                .field(GLOBAL_DELETE_RATIO)
                .type(UiPanelFieldType.Input)
                .titleI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_DELETE_TITLE)
                .descI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_DELETE_DESC)
                .require(true)
                .defaultValue(strValueDef("30"))
                .build());

    }

    private void fillGlobalConfigUiPanel(FakerUiPanel uiPanel) {
        uiPanel.getGlobalConfig()
            .addField(UiPanelField.builder()
                .field(GLOBAL_PTHREADCNT)
                .type(UiPanelFieldType.Input)
                .titleI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_PTHREADCNT_TITLE)
                .descI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_PTHREADCNT_DESC)
                .require(true)
                .defaultValue(strValueDef("3"))
                .build());

        uiPanel.getGlobalConfig()
            .addField(UiPanelField.builder()
                .field(GLOBAL_WTHREADCNT)
                .type(UiPanelFieldType.Input)
                .titleI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_WTHREADCNT_TITLE)
                .descI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_WTHREADCNT_DESC)
                .require(true)
                .defaultValue(strValueDef("5"))
                .build());

        uiPanel.getGlobalConfig()
            .addField(UiPanelField.builder()
                .field(GLOBAL_IGNOREERRORS)
                .type(UiPanelFieldType.Check)
                .titleI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_IGNOREERRORS_TITLE)
                .descI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_IGNOREERRORS_DESC)
                .require(true)
                .defaultValue(strValueDef("true"))
                .build());

        uiPanel.getGlobalConfig()
            .addField(UiPanelField.builder()
                .field(GLOBAL_TRANSACTION)
                .type(UiPanelFieldType.Check)
                .titleI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_TRANSACTION_TITLE)
                .descI18N(FakerI18nKeys.FAKER_GLOBALCONFIG_TRANSACTION_DESC)
                .require(true)
                .defaultValue(strValueDef("false"))
                .build());
    }

    private void fillTableConfigUiPanel(FakerUiPanel uiPanel) {
        uiPanel.getTableConfig()
            .addField(UiPanelField.builder()
                .field(TABLE_TOTAL)
                .type(UiPanelFieldType.Input)
                .require(true)
                .defaultValue(strValueDef("1000"))
                .titleI18N(FakerI18nKeys.FAKER_TABLECONFIG_TOTAL_TITLE)
                .descI18N(FakerI18nKeys.FAKER_TABLECONFIG_TOTAL_DESC)
                .build());
    }

    private void fillColumnConfigUiPanel(FakerUiPanel uiPanel) {
        this.fillColumnConfigUiPanelForBasic(uiPanel);
        this.fillColumnConfigUiPanelForAdvanced(uiPanel);
    }

    private void fillColumnConfigUiPanelForBasic(FakerUiPanel uiPanel) {
        uiPanel.getColumnConfig()
            .addField(UiPanelField.builder()
                .field(COLUMN_SEED)
                .type(UiPanelFieldType.Options)
                .require(true)
                .options(fetchSeedOption())
                .titleI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_COLUMNSEED_TITLE)
                .descI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_COLUMNSEED_DESC)
                .build());

        uiPanel.getColumnConfig()
            .addField(UiPanelField.builder()
                .field(ALLOW_NULLABLE)
                .type(UiPanelFieldType.Check)
                .titleI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_NULLABLE_TITLE)
                .descI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_NULLABLE_DESC)
                .defaultValue(strValueDef("false"))
                .build());

        uiPanel.getColumnConfig()
            .addField(UiPanelField.builder()
                .field(NULLABLE_RATIO)
                .type(UiPanelFieldType.Input)
                .titleI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_RATIO_TITLE)
                .descI18N(FakerI18nKeys.FAKER_COLUMNCONFIG_RATIO_DESC)
                .build());
    }

    private void fillColumnConfigUiPanelForAdvanced(FakerUiPanel uiPanel) {
    }

    protected List<ValueDef> fetchSeedOption() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(FakerI18nKeys.FAKER_COLUMNCONFIG_BOOLEAN_LABEL, "Boolean"));
//        result.add(fieldOptionDef(FakerI18nKeys.FAKER_COLUMNCONFIG_DATE_LABEL, "Date").addFields(SeedType.Date.newUiFactory().buildSeedUi()));
        result.add(fieldOptionDef(FakerI18nKeys.FAKER_COLUMNCONFIG_STRING_LABEL, "String").addFields(SeedType.String.newUiFactory().buildSeedUi()));
        result.add(fieldOptionDef(FakerI18nKeys.FAKER_COLUMNCONFIG_NUMBER_LABEL, "Number").addFields(SeedType.Number.newUiFactory().buildSeedUi()));
//        result.add(fieldOptionDef(FakerI18nKeys.FAKER_COLUMNCONFIG_ENUMS_LABEL, "Enums"));
        result.add(fieldOptionDef(FakerI18nKeys.FAKER_COLUMNCONFIG_BYTES_LABEL, "Bytes").addFields(SeedType.Bytes.newUiFactory().buildSeedUi()));
        result.add(fieldOptionDef(FakerI18nKeys.FAKER_COLUMNCONFIG_GID_LABEL, "Gid").addFields(SeedType.GID.newUiFactory().buildSeedUi()));
        result.add(fieldOptionDef(FakerI18nKeys.FAKER_COLUMNCONFIG_ARRAY_LABEL, "Array").addFields(SeedType.Array.newUiFactory().buildSeedUi()));
        result.add(fieldOptionDef(FakerI18nKeys.FAKER_COLUMNCONFIG_GEOMETRY_LABEL, "Geometry").addFields(SeedType.Geometry.newUiFactory().buildSeedUi()));
        return result;
    }

    protected List<ValueDef> fetchPoliticOption() {

        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(FakerI18nKeys.FAKER_TABLECONFIG_RANDOMCOL_LABEL, "RandomCol"));
        result.add(fieldOptionDef(FakerI18nKeys.FAKER_TABLECONFIG_RANDOMKEYCOL_LABEL, "RandomKeyCol"));
        result.add(fieldOptionDef(FakerI18nKeys.FAKER_TABLECONFIG_FULLCOL_LABEL, "FullCol"));
        result.add(fieldOptionDef(FakerI18nKeys.FAKER_TABLECONFIG_KEYCOL_LABEL, "KeyCol"));
        return result;
    }

}
