package com.clougence.clouddm.dsfamily.db2.definition.ui.editor.sequence;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.dsfamily.db2.i18n.Db2DsI18nKeys;
import com.clougence.clouddm.dsfamily.definition.ui.editor.sequence.DsFamilySequencePropertiesUiPanelFactory;

public class Db2SequencePropertyUiPanelFactory extends DsFamilySequencePropertiesUiPanelFactory implements Db2SequenceFields {

    protected List<UiPanelField> fillBaseInfoItem() {
        List<UiPanelField> fields = new ArrayList<>();

        fields.add(UiPanelField.builder()
            .field(CREATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_CREATE_TIME_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_CREATE_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(UPDATE_TIME)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_UPDATE_TIME_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_UPDATE_TIME_DESC)
            .build());
        fields.add(UiPanelField.builder()
            .field(START_VALUE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_START_VALUE_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_START_VALUE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(MIN_VALUE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_MIN_VALUE_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_MIN_VALUE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(MAX_VALUE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_MAX_VALUE_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_MAX_VALUE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(INCREMENT)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_INCREMENT_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_INCREMENT_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(CYCLE_FLAG)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_CYCLE_FLAG_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_CYCLE_FLAG_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(CACHE_SIZE)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_CACHE_SIZE_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_CACHE_SIZE_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(LAST_NUMBER)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_LAST_NUMBER_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_LAST_NUMBER_DESC)
            .readOnly(true)
            .build());
        fields.add(UiPanelField.builder()
            .field(ORDER)
            .type(UiPanelFieldType.MapItem)
            .titleI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_ORDER_TITLE)
            .descI18N(Db2DsI18nKeys.EDITOR_SEQUENCE_ORDER_DESC)
            .readOnly(true)
            .build());

        return fields;
    }
}
