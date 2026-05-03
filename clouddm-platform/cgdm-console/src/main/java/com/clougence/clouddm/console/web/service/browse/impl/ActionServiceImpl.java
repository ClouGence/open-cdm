package com.clougence.clouddm.console.web.service.browse.impl;

import java.sql.Types;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.sidecar.session.execute.ResultList;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.DsClassify;
import com.clougence.clouddm.base.metadata.ui.form.UiPanel;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsDeletePrepareService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.execute.QueryService;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.constants.DmMode;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.model.fo.object.ObjectEditorDefFO;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorFieldForm;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorPanelForm;
import com.clougence.clouddm.console.web.service.browse.ActionService;
import com.clougence.clouddm.console.web.service.browse.model.ActionInfo;
import com.clougence.clouddm.console.web.service.browse.model.ActionTargetMO;
import com.clougence.clouddm.console.web.service.browse.model.GenerateSqlDataAuthEnum;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.console.web.util.UiWebUtil;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultMessage;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultType;
import com.clougence.clouddm.sdk.execute.session.*;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.ui.ddl.ConvertTableDDLSpi;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorVarKeys;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateOption;
import com.clougence.clouddm.sdk.ui.template.CmdTemplateSpi;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.global.exception.ConsoleRuntimeException;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpDsService;
import com.clougence.rdp.service.RdpNotifyService;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.EditorHelperDm;
import com.clougence.schema.editor.EditorOptions;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ekko
 * @date 2023/5/9 16:52
 */
@Slf4j
@Service
public class ActionServiceImpl implements ActionService, UnifiedPostConstruct {

    @Resource
    private DsSchemaService                                                        dmDsSchemaService;
    @Resource
    private DmDsConfigService                                                      dmDsConfigService;
    @Resource
    private QueryService                                                           dmQueryService;
    @Resource
    private DmDsDeletePrepareService                                               dmDsDeletePrepareService;
    @Resource
    private DmDsService                                                            dmDsService;
    @Resource
    private RdpDsService                                                           rdpDsService;
    @Resource
    private DmConsoleConfig                                                        dmConfig;
    @Resource
    private List<RdpNotifyService>                                                 notifyServices;

    private final Map<GenerateSqlDataAuthEnum, Function<ActionInfo, List<String>>> generateMap = new HashMap<>();

    private final Map<String, List<TableEditorFieldForm>>                          editorCache = new ConcurrentHashMap<>();

    @Override
    public void init() {
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_CATALOG_CREATE, SqlGenerateUtils::generateCatalogCreate);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_CATALOG_DROP, SqlGenerateUtils::generateCatalogDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_CATALOG_RENAME, SqlGenerateUtils::generateCatalogRename);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_SCHEMA_CREATE, SqlGenerateUtils::generateSchemaCreate);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_SCHEMA_DROP, SqlGenerateUtils::generateSchemaDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_SCHEMA_RENAME, SqlGenerateUtils::generateSchemaRename);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_TABLE_DROP, SqlGenerateUtils::generateTableDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_TABLE_RENAME, SqlGenerateUtils::generateTableRename);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_TABLE_TRUNCATE, SqlGenerateUtils::generateTableTruncate);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_VIEW_DROP, SqlGenerateUtils::generateViewDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_VIEW_RENAME, SqlGenerateUtils::generateViewRename);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_KEY_RENAME, SqlGenerateUtils::generateKeyRename);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_KEY_DROP, SqlGenerateUtils::generateKeyDel);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_TRIGGER_DROP, SqlGenerateUtils::generateTriggerDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_PROCEDURE_DROP, SqlGenerateUtils::generateProcedureDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_FUNCTION_DROP, SqlGenerateUtils::generateFunctionDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_MATERIALIZED_DROP, SqlGenerateUtils::generateMaterializedDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_SYNONYM_DROP, SqlGenerateUtils::generateSynonymDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_SEQUENCE_DROP, SqlGenerateUtils::generateSequenceDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_TRIGGER_CREATE, SqlGenerateUtils::generateTriggerCreate);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_TRIGGER_ALTER, SqlGenerateUtils::generateTriggerAlter);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_TRIGGER_COMPILE, SqlGenerateUtils::generateTriggerCompile);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_VIEW_CREATE, SqlGenerateUtils::generateViewCreate);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_VIEW_ALTER, SqlGenerateUtils::generateViewAlter);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_VIEW_COMPILE, SqlGenerateUtils::generateViewCompile);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_FUNCTION_CREATE, SqlGenerateUtils::generateFunctionCreate);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_FUNCTION_ALTER, SqlGenerateUtils::generateFunctionAlter);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_FUNCTION_COMPILE, SqlGenerateUtils::generateFunctionCompile);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_PROCEDURE_ALTER, SqlGenerateUtils::generateProcedureAlter);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_PROCEDURE_CREATE, SqlGenerateUtils::generateProcedureCreate);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_PROCEDURE_COMPILE, SqlGenerateUtils::generateProcedureCompile);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_CONSTRAINT_ENABLE, SqlGenerateUtils::generateConstraintEnable);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_CONSTRAINT_DISABLE, SqlGenerateUtils::generateConstraintDisable);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_USER_DROP, SqlGenerateUtils::generateUserDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_INDEX_DROP, SqlGenerateUtils::generateTableIndexDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_PRIMARY_DROP, SqlGenerateUtils::generateTablePrimaryKeyDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_DBLINK_CREATE, SqlGenerateUtils::generateDBLinkCreate);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_DBLINK_DROP, SqlGenerateUtils::generateDBLinkDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_DBLINK_TEST, SqlGenerateUtils::generateDBLinkTest);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_JOB_CREATE, SqlGenerateUtils::generateJobCreate);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_JOB_ALTER, SqlGenerateUtils::generateJobAlter);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_JOB_DROP, SqlGenerateUtils::generateJobDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_JOB_DISABLE, SqlGenerateUtils::generateJobStop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_JOB_ENABLE, SqlGenerateUtils::generateJobResume);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_JOB_RUN, SqlGenerateUtils::generateJobRun);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_SCHEDULE_DROP, SqlGenerateUtils::generateScheduleJobDrop);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_SCHEDULE_DISABLE, SqlGenerateUtils::generateScheduleJobDisable);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_SCHEDULE_ENABLE, SqlGenerateUtils::generateScheduleJobEnable);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_SCHEDULE_RUN, SqlGenerateUtils::generateScheduleJobRun);
        this.generateMap.put(GenerateSqlDataAuthEnum.MENU_BROWSE_SCHEDULE_CREATE, SqlGenerateUtils::generateScheduleJobCreate);
    }

    @Override
    public void stop() {

    }

    @Override
    public List<String> genAction(DsLevels levels, ActionTargetMO mo) {
        ActionInfo info = this.parseAction(levels, mo);
        Function<ActionInfo, List<String>> fooGen = this.generateMap.get(mo.getActionType());
        return this.generateSql(info, fooGen);
    }

    private List<String> generateSql(ActionInfo info, Function<ActionInfo, List<String>> fooGen) {
        Map<UmiTypes, Object> levelsParam = info.getLevelsParam();
        DataSourceType dsType = info.getDsDO().getDataSourceType();
        Dialect dialect = DmDsUtils.getDialect(dsType);
        CmdTemplateOption option = SqlGenerateUtils.resolverOptions(info);

        String catalogStr = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
        String schemaStr = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        String targetNameStr = info.getTargetName();
        String targetNewNameStr = info.getTargetNewName();
        if (dsType.getDsClassify() == DsClassify.RDB) {
            catalogStr = SqlGenerateUtils.fmtName(option, dialect, catalogStr);
            schemaStr = SqlGenerateUtils.fmtName(option, dialect, schemaStr);
            targetNameStr = SqlGenerateUtils.fmtName(option, dialect, targetNameStr);
            targetNewNameStr = SqlGenerateUtils.fmtName(option, dialect, targetNewNameStr);
        } else {
            targetNameStr = SqlGenerateUtils.fmtName(option, dialect, targetNameStr);
            targetNewNameStr = SqlGenerateUtils.fmtName(option, dialect, targetNewNameStr);
        }

        List<String> scripts = fooGen.apply(info);
        List<String> results = new ArrayList<>();
        for (String script : scripts) {
            script = StringUtils.replace(script, CmdTemplateSpi.DB_PLACEHOLDER, catalogStr);
            script = StringUtils.replace(script, CmdTemplateSpi.SCHEMA_PLACEHOLDER, schemaStr);

            switch (info.getTargetType()) {
                case Catalog:
                case Schema:
                case Table:
                case View:
                case Function:
                case USER:
                case Procedure:
                case ExternalTable:
                case DBLink:
                case Job:
                case ScheduleJob:
                case Materialized: {
                    script = StringUtils.replace(script, CmdTemplateSpi.TABLE_PLACEHOLDER, targetNameStr);
                    script = StringUtils.replace(script, CmdTemplateSpi.NEW_NAME_PLACEHOLDER, targetNewNameStr);
                    break;
                }
                case Key: {
                    script = StringUtils.replace(script, CmdTemplateSpi.KEY_PLACEHOLDER, targetNameStr);
                    script = StringUtils.replace(script, CmdTemplateSpi.NEW_NAME_PLACEHOLDER, targetNewNameStr);
                    break;
                }
                case Trigger: {
                    // for drop trigger
                    script = StringUtils.replace(script, CmdTemplateSpi.TABLE_PLACEHOLDER, targetNameStr);
                    break;
                }
                case Topic:
                default:
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_BROWSE_UNSUPPORTED_OBJTYPE_ERROR.name()));
            }
            results.add(script);
        }

        return results;
    }

    /**
     * for service API '/browse/doAction'
     */
    @Override
    public List<String> doAction(String puid, String uid, DsLevels levels, ActionTargetMO mo, String clientIp) {
        List<String> result = this.genAction(levels, mo);
        ActionInfo info = this.parseAction(levels, mo);
        List<UmiTypes> levelsDef = info.getLevelsDef();
        Map<UmiTypes, Object> levelsParam = info.getLevelsParam();
        switch (mo.getActionType()) {
            case MENU_BROWSE_CATALOG_CREATE:
            case MENU_BROWSE_CATALOG_DROP:
            case MENU_BROWSE_CATALOG_RENAME:
                levelsParam.remove(UmiTypes.Catalog);
                levelsParam.remove(UmiTypes.Schema);
                break;
            case MENU_BROWSE_SCHEMA_CREATE:
            case MENU_BROWSE_SCHEMA_DROP:
                if (levelsDef.contains(UmiTypes.Catalog)) {
                    levelsParam.remove(UmiTypes.Schema);
                } else {
                    levelsParam.remove(UmiTypes.Catalog);
                    levelsParam.remove(UmiTypes.Schema);
                }
                break;
            default:
                break;
        }

        return this.executeSql(puid, uid, clientIp, info, levelsParam, result, mo);
    }

    /**
     * for service API '/browse/actions/requestScript'
     */
    @Override
    public String requestObjectScript(String uid, DsLevels levels, ActionTargetMO mo) {
        ActionInfo info = this.parseAction(levels, mo);
        UmiTypes leafType = info.getTargetType();
        String leafName = info.getTargetName();

        // request script
        List<String> scriptList = this.dmDsSchemaService.requestObjectScript(uid, info.getDsDO(), info.getLevelsParam(), leafType, leafName);
        return StringUtils.join(scriptList.toArray(), "\n\n");
    }

    /**
     * for service API '/browse/actions/generateScript'
     */
    @Override
    public String generateObjectScript(String uid, DsLevels levels, ActionTargetMO mo) {
        ActionInfo info = this.parseAction(levels, mo);
        UmiTypes leafType = info.getTargetType();
        String leafName = info.getTargetName();

        // generate script
        CmdTemplateOption option = SqlGenerateUtils.resolverOptions(info);
        List<String> scriptList = this.dmDsSchemaService.generateObjectScript(uid, info.getDsDO(), info.getLevelsParam(), leafType, leafName, option);
        return StringUtils.join(scriptList.toArray(), "\n\n");
    }

    /**
     * for service API '/browse/actions/instanceRemark'
     */
    @Override
    public void instanceRemarks(String puid, String uid, DsLevels levels, String newRemark) {
        try {
            if (this.dmConfig.getDmMode() == DmMode.desktop) {
                this.dmDsService.updateDsDesc(puid, uid, levels.getDsDO().getId(), newRemark);
            } else {
                // this.dmDsService.updateDsTag(levels.getDsDO().getId(), uid, newRemark);
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_BROWSE_INSTANCE_REMARKS_TO_MANAGER_ERROR.name()));
            }
        } finally {
            this.notifyServices.forEach(s -> s.onDsUpdate(levels.getDsDO().getId()));
        }
    }

    /**
     * for service API '/browse/actions/instanceDelete'
     */
    @Override
    public void instanceDelete(String puid, String uid, DsLevels levels) {
        if (this.dmConfig.getDmMode() != DmMode.desktop) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_BROWSE_REMOVE_DS_ERROR.name()));
        }

        try {
            RdpDataSourceDO dsDO = levels.getDsDO();
            this.dmDsDeletePrepareService.prepareDelete(puid, dsDO.getId());
            this.rdpDsService.delDataSource(puid, dsDO.getId());
        } finally {
            this.notifyServices.forEach(s -> s.onDsDelete(levels.getDsDO().getId()));
        }
    }

    @Override
    public List<String> convertDDL(String puid, String uid, DsLevels levels, ActionTargetMO mo, DataSourceType dstType) {
        ActionInfo info = this.parseAction(levels, mo);
        DataSourceType srcDsType = info.getDsDO().getDataSourceType();

        TableEditor sourceEditor = this.buildTableEditor(uid, mo.getTargetName(), info);

        ConvertTableDDLSpi ddlSpi = PluginManager.findConvertDDLSpi(srcDsType);
        SqlBuilder targetSqlBuilder = PluginManager.findDsSqlBuilder(dstType);
        return ddlSpi.convertDDL(sourceEditor, targetSqlBuilder);
    }

    @Override
    public Map<String, Object> loadObject(String puid, String uid, List<String> levels, UmiTypes leafType, String leafName) {

        DsLevels result = this.dmDsConfigService.parseLevels(levels);
        if (result == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_SCHEMA_NOT_EXIST_ERROR.name(), levels.get(levels.size() - 1)));
        }

        Value value = this.dmDsSchemaService.detailLeaf(uid, result.getDsDO(), result.getLevelsParam(), leafType, leafName, true);
        if (value == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_OBJECT_NOT_EXIST.name(), leafName));
        }

        switch (leafType) {
            case Trigger:
                return DmConvertUtils.convertToBrowseTriggerVO((RdbTrigger) value);
            case View:
                return DmConvertUtils.convertToBrowseViewVO((RdbView) value);
            case Procedure:
                return DmConvertUtils.convertToBrowseProcedureVO((RdbProcedure) value);
            case Function:
                return DmConvertUtils.convertToBrowseFunctionVO((RdbFunction) value);
            case Job:
                return DmConvertUtils.convertToBrowseJobVO((RdbJob) value);
            default:
                throw new UnsupportedOperationException();
        }

    }

    //
    // Utils Method
    //

    private TableEditor buildTableEditor(String uid, String tableName, ActionInfo info) {
        CmdTemplateOption cmdOption = SqlGenerateUtils.resolverOptions(info);
        Map<UmiTypes, Object> levelsParam = info.getLevelsParam();
        RdpDataSourceDO dsDO = info.getDsDO();

        EditorOptions editorOption = new EditorOptions();
        editorOption.setUseDelimited(cmdOption.isDelimited());

        EditorContext ctx = this.dmDsSchemaService.createEditorContext(uid, dsDO, levelsParam, editorOption);
        String srcTableStruct = this.dmDsSchemaService.loadTableEditor(uid, dsDO, levelsParam, tableName, false);
        return EditorHelperDm.restoreTableEditor(srcTableStruct, ctx);
    }

    public ActionInfo parseAction(DsLevels levels, ActionTargetMO mo) {
        String targetType = mo.getTargetType();
        String targetName = mo.getTargetName();
        GenerateSqlDataAuthEnum eventType = mo.getActionType();

        if (StringUtils.isNotBlank(targetType)) {
            UmiTypes umiType = UmiTypes.valueOfCode(targetType);
            levels.getLevelsParam().put(umiType, targetName);
        }

        ActionInfo info = DmConvertUtils.convertToActionInfo(levels);

        info.setDataAuth((eventType == null) ? null : eventType.getDataAuth());
        info.setTargetType(UmiTypes.valueOfCode(targetType));
        info.setTargetName(targetName);
        info.setTargetExactName(mo.getTargetExactName());
        info.setTargetNewName(mo.getTargetNewName());
        info.setOptions(mo.getOptions());
        return info;
    }

    private List<String> executeSql(String puid, String uid, String clientIp, ActionInfo info, Map<UmiTypes, Object> levelsParam, List<String> generateSql, ActionTargetMO mo) {
        RdpDataSourceDO dsDO = info.getDsDO();
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType());
        DsConfig dsSetting = this.dmDsConfigService.dsConstantSettings(dsDO.getDataSourceType());
        SessionSpi sessionSpi = PluginManager.findSessionSpi(dsDO.getDataSourceType());

        //
        Map<String, Object> params = new HashMap<>();
        params.put(SessionSpi.PARAMS_DEFAULT_DB, StringUtils.toString(levelsParam.get(UmiTypes.Catalog)));
        params.put(SessionSpi.PARAMS_DEFAULT_SCHEMA, StringUtils.toString(levelsParam.get(UmiTypes.Schema)));
        SessionContextDTO contextDTO = sessionSpi.createSessionContext(dsConfig, params);

        String sessionId = "";
        try {
            DsLevels levels = DmDsUtils.createLevels(dsDO, dsSetting, contextDTO.getRdbCatalog(), contextDTO.getRdbSchema());
            sessionId = this.dmQueryService.createSession(uid, levels, contextDTO);
            for (String script : generateSql) {
                QueryRequest requestDTO = sessionSpi.createQueryRequest(contextDTO, dsConfig, params, uid, clientIp, true);
                requestDTO.setQueryBody(script);
                requestDTO.setQueryArgs(Collections.emptyList());
                requestDTO.setRequester(Requester.CONSOLE);

                setArgs(levelsParam, mo, requestDTO);

                ResultList list = this.dmQueryService.syncExecuteQuery(uid, sessionId, requestDTO);
                List<Result> collect = list.getResultList().stream().filter(r -> r.getResultType() == ResultType.Message).collect(Collectors.toList());
                for (Result result : collect) {
                    ResultMessage resultMessage = (ResultMessage) result;
                    if (MessageLevel.Error.equals(resultMessage.getLevel())) {
                        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_EXEC_ERROR.name(), resultMessage.getMessage()));
                    }
                }
                log.info("execute sql is: " + requestDTO.getQueryBody());
            }

            return generateSql;
        } catch (ErrorMessageException | ConsoleRuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Create Session failed, msg is : " + e.getMessage(), e);
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_EXEC_ERROR.name(), e.getMessage()));
        } finally {
            this.dmQueryService.closeSession(uid, sessionId);
        }
    }

    private static void setArgs(Map<UmiTypes, Object> levelsParam, ActionTargetMO mo, QueryRequest requestDTO) {
        if (mo.getActionType() == GenerateSqlDataAuthEnum.MENU_BROWSE_TRIGGER_COMPILE || mo.getActionType() == GenerateSqlDataAuthEnum.MENU_BROWSE_PROCEDURE_COMPILE
            || mo.getActionType() == GenerateSqlDataAuthEnum.MENU_BROWSE_FUNCTION_COMPILE || mo.getActionType() == GenerateSqlDataAuthEnum.MENU_BROWSE_VIEW_COMPILE) {
            requestDTO.setUseCompile(true);
            String schema = (String) levelsParam.get(UmiTypes.Schema);
            List<QueryArg> args = new ArrayList<>();
            args.add(new QueryArg(1, schema, Types.VARCHAR, "String", false));
            args.add(new QueryArg(2, mo.getTargetName(), Types.VARCHAR, "String", false));
            requestDTO.setQueryArgs(args);
        }
    }

    @Override
    public List<TableEditorFieldForm> loadObjectEditorDef(String puid, String uid, DsLevels levels, ObjectEditorDefFO defFO) {
        RdpDataSourceDO dsDO = levels.getDsDO();

        String i18nKey = I18nUtils.toI18nKey(DmI18nUtils.getLocale());
        UmiTypes umiTypes = UmiTypes.valueOfCode(defFO.getTargetType());
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();
        String cacheKey = String.format("%s_%s_%s_%s", dsDO.getId(), umiTypes, i18nKey, defFO.getViewMode());

        if (this.editorCache.containsKey(cacheKey)) {
            return this.editorCache.get(cacheKey);
        }
        synchronized (this) {
            if (this.editorCache.containsKey(cacheKey)) {
                return this.editorCache.get(cacheKey);
            }

            Map<String, String> envVariables = new HashMap<>();
            envVariables.put(TableEditorVarKeys.I18N_LOCAL_USER.codeKey(), i18nKey);
            envVariables.put(TableEditorVarKeys.I18N_LOCAL_DEFAULT.codeKey(), I18nUtils.DEFAULT.getDefaultI18nKey());
            envVariables.put(EditorViewMode.class.getName(), defFO.getViewMode().getViewMode().name());

            UiPanel uiPanel;
            switch (umiTypes) {
                case Function: {
                    uiPanel = dmDsSchemaService.fetchFunctionUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case Procedure: {
                    uiPanel = dmDsSchemaService.fetchProcedureUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case View: {
                    uiPanel = dmDsSchemaService.fetchViewUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case Trigger: {
                    uiPanel = dmDsSchemaService.fetchTriggerEditorUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case DBLink: {
                    uiPanel = dmDsSchemaService.fetchDbLinkUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case Job: {
                    uiPanel = dmDsSchemaService.fetchJobUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                case ScheduleJob: {
                    uiPanel = dmDsSchemaService.fetchScheduleJobEditorUiPanel(uid, dsDO, levelsParam, envVariables);
                    break;
                }
                default:
                    throw new UnsupportedOperationException();
            }
            TableEditorPanelForm editorForm = UiWebUtil.passerPanel(uiPanel);
            this.editorCache.put(cacheKey, editorForm.getChildren());
            return editorForm.getChildren();
        }
    }

}
