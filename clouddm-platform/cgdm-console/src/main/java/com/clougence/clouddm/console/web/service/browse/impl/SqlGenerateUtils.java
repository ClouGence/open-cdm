package com.clougence.clouddm.console.web.service.browse.impl;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.console.web.service.browse.model.ActionInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/6 11:25
 */
@Slf4j
public class SqlGenerateUtils {

    public static String fmtName(CmdTemplateOption option, Dialect dialect, String nameStr) {
        if (nameStr == null || "".equals(nameStr) || dialect == null) {
            return nameStr;
        } else {
            return dialect.fmtName(option.isDelimited(), nameStr);
        }
    }

    public static CmdTemplateOption resolverOptions(ActionInfo info) {
        Map<String, Object> options = info.getOptions();
        CmdTemplateOption option = new CmdTemplateOption();

        //normal
        Map<UmiTypes, Object> levelsParam = info.getLevelsParam();
        option.setTargetName(info.getTargetName());
        option.setTargetNewName(info.getTargetNewName());
        option.setTargetExactName(info.getTargetExactName());
        option.setCatalog(StringUtils.toString(levelsParam.get(UmiTypes.Catalog)));
        option.setSchema(StringUtils.toString(levelsParam.get(UmiTypes.Schema)));

        //
        if (options != null && options.containsKey("delimited")) {
            option.setDelimited(Boolean.parseBoolean(options.get("delimited").toString()));
        }
        if (options != null && options.containsKey("cascade")) {
            option.setCascade(Boolean.parseBoolean(options.get("cascade").toString()));
        }
        if (options != null && options.containsKey("restrict")) {
            option.setRestrict(Boolean.parseBoolean(options.get("restrict").toString()));
        }
        if (options != null && options.containsKey("purge")) {
            option.setPurge(Boolean.parseBoolean(options.get("purge").toString()));
        }
        if (options != null && options.containsKey("usingExists")) {
            option.setUsingExists(Boolean.parseBoolean(options.get("usingExists").toString()));
        }
        if (options != null && options.containsKey("defaultLimit")) {
            option.setDefaultLimit(Integer.parseInt(options.get("defaultLimit").toString()));
        }
        if (options != null && options.containsKey("clusterName")) {
            option.setClusterName(options.get("clusterName").toString());
        }
        if (options != null && options.containsKey("truncateUseDelete")) {
            option.setTruncateUseDelete(Boolean.parseBoolean(options.get("truncateUseDelete").toString()));
        }

        if (options != null && options.containsKey("triggerTable")) {
            option.setTriggerTable(options.get("triggerTable").toString());
        }
        option.setData(info.getOptions());

        return option;
    }

    public static List<String> generateCatalogCreate(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getCreateCatalog(resolverOptions(info));
    }

    public static List<String> generateCatalogDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropCatalog(resolverOptions(info));
    }

    public static List<String> generateCatalogRename(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getRenameCatalog(resolverOptions(info));
    }

    public static List<String> generateSchemaCreate(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getCreateSchema(resolverOptions(info));
    }

    public static List<String> generateSchemaDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropSchema(resolverOptions(info));
    }

    public static List<String> generateSchemaRename(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getRenameSchema(resolverOptions(info));
    }

    public static List<String> generateTableDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropTable(resolverOptions(info));
    }

    public static List<String> generateTableRename(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getRenameTable(resolverOptions(info));
    }

    public static List<String> generateTableTruncate(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        CmdTemplateOption options = resolverOptions(info);
        if (options.isTruncateUseDelete()) {
            return tmpSpi.getDeleteTable(options);
        } else {
            return tmpSpi.getTruncateTable(options);
        }
    }

    public static List<String> generateKeyDel(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropKey(resolverOptions(info));
    }

    public static List<String> generateKeyRename(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getRenameKey(resolverOptions(info));
    }

    public static List<String> generateViewDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropView(resolverOptions(info));
    }

    public static List<String> generateFunctionCompile(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getCompileFunction(resolverOptions(info));
    }

    public static List<String> generateProcedureCompile(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getCompileProcedure(resolverOptions(info));
    }

    public static List<String> generateTriggerCompile(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getCompileTrigger(resolverOptions(info));
    }

    public static List<String> generateViewCompile(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getCompileView(resolverOptions(info));
    }

    public static List<String> generateTableIndexDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropTableIndex(resolverOptions(info));
    }

    public static List<String> generateTablePrimaryKeyDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropTablePrimaryKey(resolverOptions(info));
    }

    public static List<String> generateDBLinkCreate(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDbLinkCreate(resolverOptions(info));
    }

    public static List<String> generateDBLinkDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDbLinkDrop(resolverOptions(info));
    }

    public static List<String> generateDBLinkTest(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDbLinkTest(resolverOptions(info));
    }

    public static List<String> generateJobCreate(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getJobCreate(resolverOptions(info));
    }

    public static List<String> generateJobAlter(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getJobAlter(resolverOptions(info));
    }

    public static List<String> generateJobDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getJobDrop(resolverOptions(info));
    }

    public static List<String> generateJobStop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getJobStop(resolverOptions(info));
    }

    public static List<String> generateJobResume(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getJobResume(resolverOptions(info));
    }

    public static List<String> generateScheduleJobDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getScheduleJobDrop(resolverOptions(info));
    }

    public static List<String> generateScheduleJobDisable(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getScheduleJobDisable(resolverOptions(info));
    }

    public static List<String> generateScheduleJobEnable(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getScheduleJobEnable(resolverOptions(info));
    }

    public static List<String> generateScheduleJobRun(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getScheduleJobRun(resolverOptions(info));
    }

    public static List<String> generateScheduleJobCreate(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getScheduleJobCreate(resolverOptions(info));
    }

    public static List<String> generateJobRun(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getJobRun(resolverOptions(info));
    }

    public static List<String> generateViewRename(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getRenameView(resolverOptions(info));
    }

    public static List<String> generateTriggerDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropTrigger(resolverOptions(info));
    }

    public static List<String> generateTriggerCreate(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getCreateTrigger(resolverOptions(info));
    }

    public static List<String> generateTriggerAlter(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getAlterTrigger(resolverOptions(info));
    }

    public static List<String> generateViewCreate(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getCreateView(resolverOptions(info));
    }

    public static List<String> generateViewAlter(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getAlterView(resolverOptions(info));
    }

    public static List<String> generateProcedureCreate(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getCreateProcedure(resolverOptions(info));
    }

    public static List<String> generateProcedureAlter(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getAlterProcedure(resolverOptions(info));
    }

    public static List<String> generateConstraintEnable(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getEnableConstraint(resolverOptions(info));
    }

    public static List<String> generateConstraintDisable(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDisableConstraint(resolverOptions(info));
    }

    public static List<String> generateUserDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropUser(resolverOptions(info));
    }

    public static List<String> generateFunctionCreate(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getCreateFunction(resolverOptions(info));
    }

    public static List<String> generateFunctionAlter(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getAlterFunction(resolverOptions(info));
    }

    public static List<String> generateFunctionDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropFunction(resolverOptions(info));
    }

    public static List<String> generateMaterializedDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropMaterialized(resolverOptions(info));
    }

    public static List<String> generateSynonymDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropSynonym(resolverOptions(info));
    }

    public static List<String> generateProcedureDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropProcedure(resolverOptions(info));
    }

    public static List<String> generateSequenceDrop(ActionInfo info) {
        CmdTemplateSpi tmpSpi = PluginManager.findCmdTemplateSpi(info.getDsDO().getDataSourceType());
        return tmpSpi.getDropSequence(resolverOptions(info));
    }

}
