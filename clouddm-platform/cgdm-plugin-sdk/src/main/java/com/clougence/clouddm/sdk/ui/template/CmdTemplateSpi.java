package com.clougence.clouddm.sdk.ui.template;

import java.util.List;

import com.clougence.clouddm.sdk.Spi;

public interface CmdTemplateSpi extends Spi {

    String DB_PLACEHOLDER       = "%%DB%%";
    String SCHEMA_PLACEHOLDER   = "%%SCHEMA%%";
    String TABLE_PLACEHOLDER    = "%%TABLE%%";
    String COLUMN_PLACEHOLDER   = "%%COLUMN%%";
    String KEY_PLACEHOLDER      = "%%KEY%%";
    //
    String NEW_NAME_PLACEHOLDER = "%%NEW_NAME%%";
    String PARAM_PLACEHOLDER    = "%%PARAM%%";

    String getQuickQuery(CmdTemplateOption option);

    /**
     * contain table, view, materialized
     */
    default String getQuickQueryByTable(CmdTemplateOption option) {
        return "";
    }

    default String getQuickQueryByView(CmdTemplateOption option) {
        return "";
    }

    default String getQuickQueryByScheduleJob(CmdTemplateOption option) {
        return "";
    }

    default String getQuickQueryByJob(CmdTemplateOption option) {
        return "";
    }

    default String getQuickQueryByMaterialized(CmdTemplateOption option) {
        return "";
    }

    default String getQuickQueryByColumn(CmdTemplateOption option) {
        return "";
    }

    default String getQuickQueryByProcedure(CmdTemplateOption option) {
        return "";
    }

    default String getQuickQueryByFunction(CmdTemplateOption option) {
        return "";
    }

    default String getQuickQueryByTrigger(CmdTemplateOption option) {
        return "";
    }

    default String getQuickQueryBySequence(CmdTemplateOption option) {
        return "";
    }

    default String getQuickQueryBySynonym(CmdTemplateOption option) {
        return "";
    }

    default String getQuickQueryKey(CmdTemplateOption option) {
        return "";
    }

    default List<String> getCreateCatalog(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropCatalog(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getRenameCatalog(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getCreateSchema(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropSchema(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getRenameSchema(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropTable(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropKey(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDeleteTable(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getRenameTable(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getTruncateTable(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getRenameView(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getRenameKey(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropView(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getCompileFunction(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getCompileProcedure(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getCompileTrigger(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getCompileView(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropTableIndex(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropTablePrimaryKey(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropTrigger(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropProcedure(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropFunction(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropMaterialized(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropSynonym(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropSequence(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getCreateTrigger(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getAlterTrigger(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getCreateProcedure(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getCreateFunction(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDropUser(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getAlterProcedure(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getAlterFunction(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getCreateView(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getAlterView(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getEnableConstraint(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDisableConstraint(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDbLinkCreate(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDbLinkDrop(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getDbLinkTest(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getJobCreate(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getJobAlter(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getJobDrop(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getJobStop(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getJobResume(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getJobRun(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getScheduleJobDrop(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getScheduleJobDisable(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getScheduleJobEnable(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getScheduleJobRun(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }

    default List<String> getScheduleJobCreate(CmdTemplateOption option) {
        throw new UnsupportedOperationException();
    }
}
