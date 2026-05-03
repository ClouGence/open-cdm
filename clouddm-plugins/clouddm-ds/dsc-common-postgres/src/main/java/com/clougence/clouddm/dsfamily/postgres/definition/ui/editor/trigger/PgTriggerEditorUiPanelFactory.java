package com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.trigger;

import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.fieldOptionDef;
import static com.clougence.clouddm.base.metadata.ui.form.UiUtils.strValueDef;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelField;
import com.clougence.clouddm.base.metadata.ui.form.UiPanelFieldType;
import com.clougence.clouddm.base.metadata.ui.form.value.ValueDef;
import com.clougence.clouddm.dsfamily.definition.ui.editor.trigger.DsFamilyTriggerEditorUiPanelFactory;
import com.clougence.clouddm.dsfamily.i18n.DsTriggerEditorI18nKeys;
import com.clougence.clouddm.dsfamily.postgres.i18n.PgDsI18nKeys;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.jdbc.extractor.MultipleRowResultSetExtractor;

public class PgTriggerEditorUiPanelFactory extends DsFamilyTriggerEditorUiPanelFactory implements PgTriggerEditorFields {

    @Override
    protected void fillSqlInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .type(UiPanelFieldType.Input)
            .require(true)
            //            .options(fetchTriggerFunction(con, envVariables))
            .field(TRIGGER_BODY)
            .titleI18N(PgDsI18nKeys.EDITOR_TRIGGER_FUNCTION_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_TRIGGER_FUNCTION_DESC)
            .build());
    }

    @Override
    protected void fillBaseInfoUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {
        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_NAME)
            .type(UiPanelFieldType.Input)
            .titleI18N(PgDsI18nKeys.EDITOR_TRIGGER_NAME_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_TRIGGER_NAME_DESC)
            .build());

        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_TIME)
            .type(UiPanelFieldType.Radios)
            .options(fetchTriggerTimes())
            .defaultValue(strValueDef("before"))
            .titleI18N(PgDsI18nKeys.EDITOR_TRIGGER_TIME_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_TRIGGER_TIME_DESC)
            .build());

        uiPanel.addField(UiPanelField.builder()
            .require(true)
            .field(TRIGGER_EVENT)
            .type(UiPanelFieldType.CheckBox)
            .options(fetchTriggerEvents())
            .titleI18N(PgDsI18nKeys.EDITOR_TRIGGER_EVENT_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_TRIGGER_EVENT_DESC)
            .build());
    }

    @Override
    protected void fillFeaturesUiPanel(UiPanel uiPanel, DataSourceConfig dsConfig, EditorViewMode viewMode, Connection con) {




        uiPanel.addField(UiPanelField.builder()
            .field(TRIGGER_GRANULARITY)
            .type(UiPanelFieldType.Radios)
            .options(fetchGranularity())
            .defaultValue(strValueDef("statement"))
            .titleI18N(PgDsI18nKeys.EDITOR_TRIGGER_TRIGGER_GRANULARITY_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_TRIGGER_TRIGGER_GRANULARITY_DESC)
            .build());

        //        UiPanelField constraint = UiPanelField.builder().field(TRIGGER_CONSTRAINT).type(UiPanelFieldType.Check).titleI18N(PgDsI18nKeys.EDIt).build();
        //
        //        uiPanel.getFeatures().addField(constraint);
    }

    protected List<ValueDef> fetchTriggerTimes() {
        List<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_TRIGGER_TIME_BEFORE_LABEL, "before"));
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_TRIGGER_TIME_AFTER_LABEL, "after"));
        return result;
    }

    private List<ValueDef> fetchGranularity() {
        ArrayList<ValueDef> result = new ArrayList<>();
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_TRIGGER_TRIGGER_GRANULARITY_ROW_LABEL, "row")
            .addField(UiPanelField.builder()
                .field(NEW_ALIAS)
                .type(UiPanelFieldType.Input)
                .titleI18N(PgDsI18nKeys.EDITOR_TRIGGER_NEW_ALIAS_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_TRIGGER_NEW_ALIAS_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(OLD_ALIAS)
                .type(UiPanelFieldType.Input)
                .titleI18N(PgDsI18nKeys.EDITOR_TRIGGER_OLD_ALIAS_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_TRIGGER_OLD_ALIAS_DESC)
                .build())
            .addField(UiPanelField.builder()
                .field(TRIGGER_CONDITION)
                .type(UiPanelFieldType.Input)
                .titleI18N(PgDsI18nKeys.EDITOR_TRIGGER_CONDITION_TITLE)
                .descI18N(PgDsI18nKeys.EDITOR_TRIGGER_CONDITION_DESC)
                .build()));
        result.add(fieldOptionDef(PgDsI18nKeys.EDITOR_TRIGGER_TRIGGER_GRANULARITY_STATEMENT_LABEL, "statement"));
        return result;
    }

    protected List<ValueDef> fetchTriggerEvents() {
        List<ValueDef> result = new ArrayList<>();
        UiPanelField columnList = UiPanelField.builder()
            .field(TRIGGER_COLUMNS)
            .type(UiPanelFieldType.TriggerColumns)
            //            .options(fetchColumns(con, envVariables))
            .titleI18N(PgDsI18nKeys.EDITOR_TRIGGER_UPDATE_COLUMNS_TITLE)
            .descI18N(PgDsI18nKeys.EDITOR_TRIGGER_UPDATE_COLUMNS_DESC)
            .build();
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_DELETE_LABEL, "delete"));
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_INSERT_LABEL, "insert"));
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_UPDATE_LABEL, "update").addField(columnList));
        result.add(fieldOptionDef(DsTriggerEditorI18nKeys.EDITOR_TRIGGER_EVENT_TRUNCATE_LABEL, "truncate"));

        return result;
    }

    private List<ValueDef> fetchColumns(Connection con, Map<String, String> envVariables) {
        List<ValueDef> result = new ArrayList<>();
        String schema = envVariables.get(UmiTypes.Schema.getTypeName());
        String table = envVariables.get(UmiTypes.Table.getTypeName());
        String sql = "select column_name from information_schema.columns where table_schema = ? and table_name = ?";
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, schema);
            statement.setString(2, table);
            ResultSet resultSet = statement.executeQuery();
            new MultipleRowResultSetExtractor<>((rs, rowNum) -> {
                String tableName = rs.getString(1);
                result.add(strValueDef(tableName));
                return null;
            }).extractData(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // find trigger function 
    private List<ValueDef> fetchTriggerFunction(Connection con, Map<String, String> envVariables) {
        List<ValueDef> result = new ArrayList<>();
        String schema = envVariables.get(UmiTypes.Schema.getTypeName());
        String sql = "SELECT proname FROM pg_proc AS pn LEFT JOIN pg_namespace AS ns ON pn.pronamespace = ns.oid   WHERE ns.nspname = ? AND pn.prokind = 'f' and pn.prorettype = 2279";

        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, schema);
            ResultSet resultSet = statement.executeQuery();
            new MultipleRowResultSetExtractor<>((rs, rowNum) -> {
                String tableName = rs.getString(1);
                result.add(strValueDef(tableName));
                return null;
            }).extractData(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
