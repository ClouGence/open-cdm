package com.clougence.clouddm.console.web.service.editor.impl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.session.execute.ResultList;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.execute.QueryService;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.editor.table.*;
import com.clougence.clouddm.console.web.model.vo.editor.table.TableEditorForm;
import com.clougence.clouddm.console.web.service.editor.DsTableEditorService;
import com.clougence.clouddm.console.web.service.editor.model.ResultSetDTO;
import com.clougence.clouddm.console.web.util.*;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultCount;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultMessage;
import com.clougence.clouddm.sdk.execute.resultset.echo.ResultType;
import com.clougence.clouddm.sdk.ui.editor.EditorViewMode;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiData;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiDataSpi;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorUiPanel;
import com.clougence.clouddm.sdk.ui.editor.table.TableEditorVarKeys;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.schema.editor.EditorContext;
import com.clougence.schema.editor.EditorHelperDm;
import com.clougence.schema.editor.EditorOptions;
import com.clougence.schema.editor.TableEditor;
import com.clougence.schema.editor.builder.actions.Action;
import com.clougence.schema.editor.domain.ETable;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.GeneralConstraintType;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/8 19:56
 */
@Slf4j
@Service
public class DsTableEditorServiceImpl implements DsTableEditorService {

    @Resource
    private DsSchemaService                    dmDsSchemaService;
    @Resource
    private DmDsConfigService                  dmDsConfigService;
    @Resource
    private QueryService                       queryService;

    private final Map<String, TableEditorForm> dsUiEditorCache = new ConcurrentHashMap<>();

    /** for service API '/editor/table/editorDef' */
    @Override
    public TableEditorForm loadTableEditorDef(String puid, String uid, DsLevels levels, EditorDefFO defFO) {
        RdpDataSourceDO dsDO = levels.getDsDO();
        DataSourceType dsType = dsDO.getDataSourceType();
        DsConfig dsConfig = this.dmDsConfigService.dsConstantSettings(dsType);
        if (dsConfig == null) {
            return null;
        }

        String i18nKey = I18nUtils.toI18nKey(DmI18nUtils.getLocale());
        EditorViewModeEnum viewMode = defFO.getViewMode();
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();
        String cacheKey = String.format("%s_%s_%s_%s", dsDO.getId(), levelsParam.get(UmiTypes.Catalog), viewMode, i18nKey);

        if (this.dsUiEditorCache.containsKey(cacheKey)) {
            return this.dsUiEditorCache.get(cacheKey);
        }
        synchronized (this) {
            if (this.dsUiEditorCache.containsKey(cacheKey)) {
                return this.dsUiEditorCache.get(cacheKey);
            }

            Map<String, String> envVariables = new HashMap<>();
            envVariables.put(TableEditorVarKeys.I18N_LOCAL_USER.codeKey(), i18nKey);
            envVariables.put(TableEditorVarKeys.I18N_LOCAL_DEFAULT.codeKey(), I18nUtils.DEFAULT.getDefaultI18nKey());
            envVariables.put(EditorViewMode.class.getName(), viewMode.getViewMode().name());
            TableEditorUiPanel uiPanel = this.dmDsSchemaService.fetchTableEditorUiPanel(uid, dsDO, levelsParam, envVariables);
            TableEditorForm editorForm = UiWebUtil.tableEditorUi2Form(uiPanel);

            this.dsUiEditorCache.put(cacheKey, editorForm);
            return editorForm;
        }
    }

    /** for service API '/editor/table/initEditor' */
    @Override
    public TableEditorUiData loadTableEditorData(String puid, String uid, DsLevels levels, EditorInitFO initFO) {
        RdpDataSourceDO dsDO = levels.getDsDO();
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();

        // table Editor
        TableEditor editor = this.initEditor(uid, dsDO, levelsParam, initFO.getTable(), null, initFO.isRefreshCache());
        if (editor == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_TABLE_NOT_EXIST_ERROR.name(), initFO.getTable()));
        }

        // process uiData
        ETable eTable = editor.getSource();
        TableEditorUiData uiData = TableEditorUiDataUtils.toUiData(eTable, dsDO.getVersion());
        this.triggerToUiDataPlugins(EditorViewMode.Alter, eTable, uiData, dsDO);
        return uiData;
    }

    /** for service API '/editor/table/generateScript' */
    @Override
    public List<ResultSetDTO> tableEditorGenerate(String puid, String uid, DsLevels levels, EditorGenFO genFO) {
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();
        RdpDataSourceDO dsDO = levels.getDsDO();

        EditorOptions options = new EditorOptions();
        options.setSkipHandlers(true);
        TableEditorUiData uiData = genFO.getTableSchema();

        List<Action> actions;
        TableEditor initEditor = this.initEditor(uid, dsDO, levelsParam, genFO.getTable(), options, false);
        if (initEditor == null) {
            String catalogStr = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
            String schemaStr = StringUtils.toString(levelsParam.get(UmiTypes.Schema));

            ETable newETable = TableEditorUiDataUtils.formUiData(uiData, new ETable(), schemaStr);
            if (StringUtils.isNotBlank(catalogStr)) {
                newETable.setCatalog(catalogStr);
            }
            if (StringUtils.isNotBlank(schemaStr)) {
                newETable.setSchema(schemaStr);
            }

            this.triggerToETablePlugins(EditorViewMode.Create, uiData, newETable, dsDO);
            TableEditor editor = this.restoreEditor(uid, dsDO, levelsParam, newETable, options);
            actions = editor.diffActions(editor.getSource(), true);
        } else {
            String schemaStr = StringUtils.toString(levelsParam.get(UmiTypes.Schema));

            ETable sourceETable = initEditor.getSource();
            ETable targetETable = TableEditorUiDataUtils.formUiData(uiData, sourceETable.clone(), schemaStr);
            this.triggerToETablePlugins(EditorViewMode.Alter, uiData, targetETable, dsDO);
            TableEditor actionEditor = this.restoreEditor(uid, dsDO, levelsParam, targetETable, options);
            actions = actionEditor.diffActions(sourceETable, false);
        }

        List<String> actionScripts = actions.stream().flatMap((Function<Action, Stream<String>>) action -> {
            return action.getSqlString().stream();
        }).collect(Collectors.toList());
        List<ResultSetDTO> dtos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(actionScripts)) {
            actionScripts.forEach(sql -> {
                ResultSetDTO resultDTO = new ResultSetDTO();
                resultDTO.setSql(sql);
                dtos.add(resultDTO);
            });
        }

        return dtos;
    }

    @Override
    public List<String> fetchReferencedColumns(String puid, String uid, DsLevels levels, EditorReferencedFO execFO) {
        RdpDataSourceDO dsDO = levels.getDsDO();
        Value value = dmDsSchemaService.detailLeaf(uid, dsDO, levels.getLevelsParam(), UmiTypes.Table, execFO.getTable(), true);

        RdbTable table = (RdbTable) value;

        if (execFO.getType() == GeneralConstraintType.Primary) {
            if (table.getPrimaryKey() == null) {
                return Collections.emptyList();
            }
            return table.getPrimaryKey().getColumnList();
        } else {
            return new ArrayList<>(table.getColumns().keySet());
        }
    }

    /** for service API '/editor/table/scriptExecute' */
    @Override
    public List<ResultSetDTO> tableEditorSave(String puid, String uid, DsLevels levels, EditorExecFO execFO, String clientIp) {
        RdpDataSourceDO dsDO = levels.getDsDO();

        // execute sql
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();
        DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType());

        String sessionId = "";
        try {
            SessionContextDTO contextDTO = DmDsUtils.createSessionCtx(dsConfig, levelsParam);
            QueryRequest queryDTO = DmDsUtils.createRequestCtx(dsConfig, levelsParam, contextDTO, uid, clientIp, true);

            sessionId = this.queryService.createSession(uid, levels, contextDTO);
            List<ResultSetDTO> dtos = new ArrayList<>();
            for (String sql : execFO.getSqlList()) {
                QueryRequest request = queryDTO.clone();
                request.setQueryBody(sql);
                request.setQueryArgs(Collections.emptyList());
                request.setQueryType(SecQueryType.UNKNOWN); // TODO bad way
                request.setRequester(Requester.CONSOLE);
                request.setResource(Collections.singletonList(DmConvertUtils.convertToResource(levels, execFO.getTable())));

                ResultList list = this.queryService.syncExecuteQuery(uid, sessionId, request);
                List<Result> collect = list.getResultList().stream().filter(r -> r.getResultType() == ResultType.ResultCount).collect(Collectors.toList());
                ResultSetDTO resultDTO = new ResultSetDTO();

                if (collect.isEmpty()) {
                    List<Result> results = list.getResultList().stream().filter(r -> r.getResultType() == ResultType.Message).collect(Collectors.toList());
                    ResultMessage rm = (ResultMessage) results.get(0);
                    resultDTO.setSuccess(false);
                    resultDTO.setMessage(rm.getMessage());
                    //resultDTO.setResource(rc.getResource());
                } else {
                    ResultCount rc = (ResultCount) collect.get(0);
                    resultDTO.setSuccess(rc.isSuccess());
                    resultDTO.setMessage(rc.getMessage());
                    //resultDTO.setResource(rc.getResource());
                }

                resultDTO.setSql(sql);
                dtos.add(resultDTO);
            }
            return dtos;
        } catch (ErrorMessageException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ErrorMessageException(e.getMessage());
        } finally {
            this.queryService.closeSession(uid, sessionId);
        }
    }

    //
    // Utils Method
    //

    private TableEditor initEditor(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, String tableName, EditorOptions options, boolean refreshCache) {
        EditorContext ctx = this.dmDsSchemaService.createEditorContext(uid, dsDO, levelsParam, options);
        ctx.setSkipHandlers(true);
        String editorData = this.dmDsSchemaService.loadTableEditor(uid, dsDO, levelsParam, tableName, refreshCache);
        if (editorData == null) {
            return null;
        } else {
            return EditorHelperDm.restoreTableEditor(editorData, ctx);
        }
    }

    private TableEditor restoreEditor(String uid, RdpDataSourceDO dsDO, Map<UmiTypes, Object> levelsParam, ETable eTable, EditorOptions options) {
        EditorContext ctx = this.dmDsSchemaService.createEditorContext(uid, dsDO, levelsParam, options);
        return EditorHelperDm.restoreTableEditor(eTable, ctx);
    }

    private void triggerToUiDataPlugins(EditorViewMode viewMode, ETable source, TableEditorUiData target, RdpDataSourceDO dataSourceDO) {
        TableEditorUiDataSpi spi = PluginManager.findTableEditorSpi(dataSourceDO.getDataSourceType());
        if (spi != null) {
            DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(dataSourceDO.getId(), dataSourceDO.getDataSourceType());
            spi.fillUiData(viewMode, source, target, dsConfig.getVersion());
        }
    }

    private void triggerToETablePlugins(EditorViewMode viewMode, TableEditorUiData source, ETable target, RdpDataSourceDO dataSourceDO) {
        TableEditorUiDataSpi spi = PluginManager.findTableEditorSpi(dataSourceDO.getDataSourceType());
        if (spi != null) {
            DataSourceConfig dsConfig = dmDsConfigService.fetchDsConfigFromDM(dataSourceDO.getId(), dataSourceDO.getDataSourceType());
            spi.fillETable(viewMode, source, target, dsConfig.getVersion());
        }
    }
}
