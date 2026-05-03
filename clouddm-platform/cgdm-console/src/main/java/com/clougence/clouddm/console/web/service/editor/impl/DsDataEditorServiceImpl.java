package com.clougence.clouddm.console.web.service.editor.impl;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.clougence.clouddm.sdk.execute.resultset.echo.*;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.session.execute.ResultList;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.execute.QueryService;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.constants.DmMode;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.config.DmConsoleConfig;
import com.clougence.clouddm.console.web.model.fo.editor.data.*;
import com.clougence.clouddm.console.web.model.vo.editor.data.DataEditorResultVO;
import com.clougence.clouddm.console.web.service.editor.DsDataEditorService;
import com.clougence.clouddm.console.web.service.editor.model.DataEditorChangeDTO;
import com.clougence.clouddm.console.web.service.editor.model.DataEditorExecuteResultDTO;
import com.clougence.clouddm.console.web.service.editor.model.DataEditorResultDTO;
import com.clougence.clouddm.console.web.service.editor.model.DataEditorUpdateDTO;
import com.clougence.clouddm.console.web.util.DmConvertUtils;
import com.clougence.clouddm.console.web.util.DmDsUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.dsfamily.definition.TypeMapUtils;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.execute.session.MessageLevel;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSpi;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorSqlType;
import com.clougence.clouddm.sdk.ui.editor.data.DataEditorUiStyle;
import com.clougence.clouddm.sdk.ui.editor.data.reload.DataEditorReloadSpi;
import com.clougence.clouddm.sdk.ui.editor.data.reload.EditorResultSet;
import com.clougence.clouddm.sdk.ui.editor.data.reload.Reload;
import com.clougence.clouddm.sdk.ui.editor.data.reload.SqlData;
import com.clougence.rdp.dal.model.RdpDataSourceDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.i18n.I18nUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DsDataEditorServiceImpl implements DsDataEditorService {

    @Resource
    private DmConsoleConfig     dmConfig;
    @Resource
    private DmDsConfigService   dmDsConfigService;
    @Resource
    private DsSchemaService     dsSchemaService;
    @Resource
    private QueryService        queryService;
    @Resource
    private DmAuthServiceForBiz authCheckService;

    /** for service API '/editor/data/fetchData' */
    @Override
    public DataEditorResultVO fetchData(String puid, String uid, String clientIp, DsLevels levels, SelectDataFO selectFO) {
        RdpDataSourceDO dsDO = levels.getDsDO();
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();
        String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        String table = selectFO.getTargetName();
        UmiTypes targetType = UmiTypes.valueOfCode(selectFO.getTargetType());

        // create session/request
        String sessionId = null;
        try {
            DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType());
            SessionContextDTO sessionCtx = DmDsUtils.createSessionCtx(dsConfig, levelsParam);
            sessionId = this.queryService.createSession(uid, levels, sessionCtx);

            //to last page
            boolean toLast = false;
            if (selectFO.getOffset() < 0) {

                String countSql = selectCount(dsDO, catalog, schema, table, targetType, selectFO.getCondition());
                EditorResultSet result = doFetchCount(sessionId, uid, clientIp, levels, dsConfig, sessionCtx, countSql, table);
                if (!result.isSuccess()) {
                    throw new ErrorMessageException(result.getMessage());
                }

                ResultSetValue dto = result.getRowSet().get(0).getData().get(0);
                selectFO.setOffset(Math.max(Integer.parseInt(dto.getValue()) - selectFO.getPageSize(), 0));
                toLast = true;
            }

            // column metadata
            RdbTable rdbTable = (RdbTable) this.dsSchemaService.detailLeaf(uid, dsDO, levelsParam, targetType, table, true);
            if (rdbTable == null) {
                throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_DATA_EDITOR_TABLE_NOT_EXIST_ERROR.name(), table));
            }

            List<RdbColumn> columns = new ArrayList<>(rdbTable.getColumns().values());

            // execute select
            DataEditorSpi spi = PluginManager.findDataEditorSpi(dsDO.getDataSourceType());
            String selectSQL = spi.buildSelect(rdbTable, selectFO.getCondition(), selectFO.getOrderBy(), selectFO.getOffset(), selectFO.getPageSize());
            EditorResultSet dmlResult = doFetchData(puid, uid, clientIp, sessionId, levels, dsConfig, sessionCtx, selectSQL, table);
            if (!dmlResult.isSuccess()) {
                throw new ErrorMessageException(dmlResult.getMessage());
            }

            DataEditorResultDTO result = EditorConvertUtils.convertToDataEditorResultDTO(dmlResult);

            // improve result
            calculatePage(rdbTable, selectFO, result, toLast);
            EditorConvertUtils.convertDataEditorResultDto(columns, result, rdbTable);
            fillUiType(result, dsDO.getDataSourceType());

            // config Table Header
            I18nUtils i18nUtils = PluginManager.findDsI18nUtil(dsDO.getDataSourceType());
            Map<String, RdbColumn> columnAttr = columns.stream().collect(Collectors.toMap(RdbColumn::getName, c -> c));
            spi.configTableHeader(rdbTable, result.getColumnList(), columnAttr, i18nUtils);

            // convert
            DataEditorResultVO resultVO = EditorConvertUtils.convertResultDTO2VO(result);
            resultVO.setReadOnly(UmiTypes.View == targetType || UmiTypes.Materialized == targetType);
            return resultVO;
        } finally {
            this.queryService.closeSession(uid, sessionId);
        }
    }

    /** for service API '/editor/data/fetchCount' */
    @Override
    public long fetchCount(String puid, String uid, DsLevels levels, SelectCountFO selectFO, String clientIp) {
        RdpDataSourceDO dsDO = levels.getDsDO();
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();
        String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        String table = selectFO.getTargetName();
        UmiTypes targetType = UmiTypes.valueOfCode(selectFO.getTargetType());

        // create session/request
        String sessionId = null;
        try {
            DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType());
            SessionContextDTO sessionCtx = DmDsUtils.createSessionCtx(dsConfig, levelsParam);
            sessionId = this.queryService.createSession(uid, levels, sessionCtx);

            // build select count query
            String countSql = selectCount(dsDO, catalog, schema, table, targetType, selectFO.getCondition());

            // fetch count
            EditorResultSet result = doFetchCount(sessionId, uid, clientIp, levels, dsConfig, sessionCtx, countSql, table);

            // result
            if (result.isSuccess()) {
                return Long.parseLong(result.getRowSet().get(0).getData().get(0).getValue());
            } else {
                throw new ErrorMessageException(result.getMessage());
            }
        } finally {
            this.queryService.closeSession(uid, sessionId);
        }
    }

    /** for service API '/editor/data/generateDml' */
    @Override
    public List<DataEditorChangeDTO> generateDml(String puid, String uid, DsLevels levels, GenerateDataFO changeFO) {
        RdpDataSourceDO dsDO = levels.getDsDO();
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();
        String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        String table = changeFO.getTargetName();
        UmiTypes targetType = UmiTypes.valueOfCode(changeFO.getTargetType());

        //
        List<DataEditorUpdateDTO> dtoList;
        if (changeFO.getChangeRows() == null) {
            dtoList = Collections.emptyList();
        } else {
            dtoList = changeFO.getChangeRows().stream().map(EditorConvertUtils::convertRenewDataFO2DTO).collect(Collectors.toList());
        }

        RdbTable tableMeta = EditorConvertUtils.convertColumnVO2DTO(dsDO.getDataSourceType(), catalog, schema, table, targetType, changeFO.getColumnList());

        return buildDML(dsDO, tableMeta, dtoList, true);
    }

    /** for service API '/editor/data/saveData' */
    @Override
    public DataEditorExecuteResultDTO saveData(String puid, String uid, DsLevels levels, ExecuteSqlFO execFO, String clientIp) {
        RdpDataSourceDO dsDO = levels.getDsDO();
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();
        String catalog = StringUtils.toString(levelsParam.get(UmiTypes.Catalog));
        String schema = StringUtils.toString(levelsParam.get(UmiTypes.Schema));
        String table = execFO.getTargetName();
        UmiTypes targetType = UmiTypes.valueOfCode(execFO.getTargetType());

        // rebuild sql
        ChangeRowFO data = execFO.getChangeRow();
        DataEditorUpdateDTO updateData = EditorConvertUtils.convertRenewDataFO2DTO(data);
        RdbTable tableMeta = EditorConvertUtils.convertColumnVO2DTO(dsDO.getDataSourceType(), catalog, schema, table, targetType, execFO.getColumnList());
        List<DataEditorChangeDTO> buildResult = buildDML(dsDO, tableMeta, Collections.singletonList(updateData), false);
        if (CollectionUtils.isEmpty(buildResult)) {
            DataEditorExecuteResultDTO dto = new DataEditorExecuteResultDTO();
            dto.setSuccess(false);
            dto.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_DATA_EDITOR_EMPTY_DML_ERROR.name()));
            return dto;
        }

        // executeSQL
        DataEditorChangeDTO changeDTO = buildResult.get(0);

        // create session/request
        String sessionId = null;
        try {
            DataSourceConfig dsConfig = this.dmDsConfigService.fetchDsConfigFromDM(dsDO.getId(), dsDO.getDataSourceType());
            SessionContextDTO sessionCtx = DmDsUtils.createSessionCtx(dsConfig, levelsParam);
            sessionId = this.queryService.createSession(uid, levels, sessionCtx);

            Reload reload = doExecuteDml(puid, uid, clientIp, sessionId, levels, dsConfig, sessionCtx, changeDTO, tableMeta, updateData);
            if (!reload.isEnable() && !reload.getResult().isSuccess()) {
                DataEditorExecuteResultDTO dto = new DataEditorExecuteResultDTO();
                dto.setSuccess(false);
                dto.setMessage(reload.getResult().getMessage());
                return dto;
            }

            // process reload
            EditorResultSet result = reload.getResult();
            Map<String, Object> refreshData = new HashMap<>();
            boolean refresh = checkAndFillRefresh(updateData.getUpdateData(), tableMeta, refreshData, updateData.getDmlType());
            if (reload.isEnable() && !refresh) {
                result = doFetchData(puid, uid, clientIp, sessionId, levels, dsConfig, sessionCtx, reload.getReloadSql(), table);
            }

            DataEditorResultDTO resultDTO = EditorConvertUtils.convertToDataEditorResultDTO(result);
            DataEditorExecuteResultDTO dto = new DataEditorExecuteResultDTO();
            dto.setSuccess(true);
            dto.setMessage(result.getMessage());
            dto.setSequence(changeDTO.getSequence());
            dto.setResultSet(resultDTO.getResultSet());
            dto.setResultSetMore(resultDTO.getResultSetMore());
            //resultSet is null ,frontend refresh
            if (StringUtils.isNotBlank(result.getMessage()) || CollectionUtils.isEmpty(resultDTO.getResultSet())) {
                dto.setResultSet(Collections.singletonList(refreshData));
                dto.setRefresh(true);
            }
            return dto;
        } finally {
            this.queryService.closeSession(uid, sessionId);
        }
    }

    //
    // Utils Method
    //

    private EditorResultSet doFetchCount(String sessionId, String uid, String clientIp, DsLevels levels, DataSourceConfig dsConfig, SessionContextDTO sessionCtx, String countSql,
                                         String table) {
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();

        // create session/request
        QueryRequest request = DmDsUtils.createRequestCtx(dsConfig, levelsParam, sessionCtx, uid, clientIp, true);
        request.setQueryBody(countSql);
        request.setQueryArgs(Collections.emptyList());
        request.setQueryType(SecQueryType.SELECT);
        request.setRequester(Requester.CONSOLE);
        request.setResource(Collections.singletonList(DmConvertUtils.convertToResource(levels, table)));

        // execute sql
        try {
            ResultList result = this.queryService.syncExecuteQuery(uid, sessionId, request);
            for (Result r : result.getResultList()) {
                if (r.getResultType() == ResultType.ResultSet) {
                    return EditorConvertUtils.convertToEditorResultSet((ResultSet) r);
                }
                if (r.getResultType() == ResultType.Message) {
                    ResultMessage resultMessage = (ResultMessage) r;
                    if (resultMessage.getLevel() == MessageLevel.Error) {
                        EditorResultSet resultDTO = new EditorResultSet();
                        resultDTO.setSuccess(false);
                        resultDTO.setMessage(resultMessage.getMessage());
                        return resultDTO;
                    }
                }
            }

            EditorResultSet resultDTO = new EditorResultSet();
            resultDTO.setSuccess(false);
            resultDTO.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_DATA_EDITOR_QUERY_MISSING_RESULT_ERROR.name()));
            return resultDTO;
        } catch (Exception e) {
            EditorResultSet resultDTO = new EditorResultSet();
            resultDTO.setSuccess(false);
            resultDTO.setMessage(e.getMessage());
            return resultDTO;
        }
    }

    private EditorResultSet doFetchData(String puid, String uid, String clientIp, String sessionId, DsLevels levels, DataSourceConfig dsConfig, SessionContextDTO sessionCtx,
                                        String fetchSql, String table) {
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();

        // create session/request
        QueryRequest request = DmDsUtils.createRequestCtx(dsConfig, levelsParam, sessionCtx, uid, clientIp, true);
        request.setQueryBody(fetchSql);
        request.setQueryArgs(Collections.emptyList());
        request.setQueryType(SecQueryType.SELECT);
        request.setRequester(Requester.CONSOLE);
        request.setResource(Collections.singletonList(DmConvertUtils.convertToResource(levels, table)));
        request.setUsingValueProcess(this.dmConfig.getDmMode() == DmMode.output && !this.authCheckService
            .checkResPathWithoutError(puid, uid, levels.getDsDO().getId(), AuthKind.DataSource, levels.asResPath(), SecDataAuthLabel.DM_DAUTH_SENSITIVE));

        // execute sql
        try {
            ResultList result = this.queryService.syncExecuteQuery(uid, sessionId, request);
            for (Result r : result.getResultList()) {
                if (r.getResultType() == ResultType.ResultSet) {
                    return EditorConvertUtils.convertToEditorResultSet((ResultSet) r);
                }
                if (r.getResultType() == ResultType.Message) {
                    ResultMessage resultMessage = (ResultMessage) r;
                    if (resultMessage.getLevel() == MessageLevel.Error) {
                        EditorResultSet resultDTO = new EditorResultSet();
                        resultDTO.setSuccess(false);
                        resultDTO.setMessage(resultMessage.getMessage());
                        return resultDTO;
                    }
                }
            }

            EditorResultSet resultDTO = new EditorResultSet();
            resultDTO.setSuccess(false);
            resultDTO.setMessage(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_DATA_EDITOR_QUERY_MISSING_RESULT_ERROR.name()));
            return resultDTO;
        } catch (Exception e) {
            EditorResultSet resultDTO = new EditorResultSet();
            resultDTO.setSuccess(false);
            resultDTO.setMessage(e.getMessage());
            return resultDTO;
        }
    }

    private Reload doExecuteDml(String puid, String uid, String clientIp, String sessionId, DsLevels levels,//
                                DataSourceConfig dsConfig, SessionContextDTO sessionCtx, DataEditorChangeDTO dmlChange, RdbTable tableMeta, DataEditorUpdateDTO updateData) {
        Map<UmiTypes, Object> levelsParam = levels.getLevelsParam();
        RdpDataSourceDO dsDO = levels.getDsDO();

        // create session/request
        QueryRequest request = DmDsUtils.createRequestCtx(dsConfig, levelsParam, sessionCtx, uid, clientIp, true);
        request.setQueryBody(dmlChange.getSql());
        request.setQueryArgs(Collections.emptyList());
        request.setQueryType(DmConvertUtils.convertToSecQueryType(dmlChange.getSqlType()));
        request.setRequester(Requester.CONSOLE);
        request.setResource(Collections.singletonList(DmConvertUtils.convertToResource(levels, tableMeta.getName())));

        // ReloadSpi  request
        DataEditorReloadSpi extSpi = PluginManager.findDataEditorExtSpi(dsDO.getDataSourceType());
        if (extSpi != null) {
            extSpi.beforeExecute(tableMeta, updateData.getDmlType(), request);
        }

        // execute sql
        try {
            ResultCount rc;
            ResultOut ro;
            ResultList list = this.queryService.syncExecuteQuery(uid, sessionId, request);
            List<Result> collect = list.getResultList().stream().filter(r -> r.getResultType() == ResultType.ResultCount).collect(Collectors.toList());
            List<Result> resultOutList = list.getResultList().stream().filter(r -> r.getResultType() == ResultType.ResultOut).collect(Collectors.toList());

            if (collect.isEmpty()) {
                // pg  result is result set
                collect = list.getResultList().stream().filter(r -> r.getResultType() == ResultType.ResultSet).collect(Collectors.toList());
                if (collect.isEmpty()) {
                    List<Result> messageResults = list.getResultList().stream().filter(r -> r.getResultType() == ResultType.Message).collect(Collectors.toList());
                    ResultMessage resultMessage = (ResultMessage) messageResults.get(0);
                    throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_EXEC_ERROR.name(), resultMessage.getMessage()));
                }
                rc = new ResultCount();
                rc.setUpdateCount(((ResultSet) collect.get(0)).getRowSet().size());
                rc.setSuccess(true);
            } else {
                rc = (ResultCount) collect.get(0);
            }

            if (resultOutList.isEmpty()) {
                ro = new ResultOut();
            } else {
                ro = (ResultOut) resultOutList.get(0);
            }

            EditorResultSet result = EditorConvertUtils.convertToEditorResultSet(rc, ro);
            if (!result.isSuccess()) {
                return Reload.failed(result.getMessage());
            }

            if (DataEditorSqlType.DELETE.equals(updateData.getDmlType())) {
                return Reload.success(result);
            }

            if (extSpi != null) {
                SqlData sqlData = EditorConvertUtils.convertDTO2SqlData(updateData);
                return extSpi.afterExecute(tableMeta, request, result, sqlData);
            } else {
                return Reload.success(result);
            }
        } catch (Exception e) {
            return Reload.failed(e.getMessage());
        }
    }

    private static void fillUiType(DataEditorResultDTO resultDto, DataSourceType dsType) {
        resultDto.getColumnList().forEach(item -> {
            FieldType fieldType = TypeMapUtils.findColumnTypes(dsType, item.getColumnType());
            if (fieldType.hasDate() && fieldType.hasTime()) {
                item.setUiType(DataEditorUiStyle.DATE_TIME_PICKER);
            } else if (fieldType.hasDate()) {
                item.setUiType(DataEditorUiStyle.DATEPICKER);
            } else if (fieldType.hasTime()) {
                item.setUiType(DataEditorUiStyle.TIMEPICKER);
            } else {
                switch (fieldType.getCodeKey()) {
                    case "ENUM":
                        item.setUiType(DataEditorUiStyle.SELECT);
                        break;
                    case "SET":
                        item.setUiType(DataEditorUiStyle.MULTI_SELECT);
                        break;
                    case "BOOLEAN":
                        item.setUiType(DataEditorUiStyle.CHECKBOX);
                        break;
                    default:
                        item.setUiType(DataEditorUiStyle.INPUT);
                        break;
                }
            }
        });
    }

    private static void calculatePage(RdbTable tableMeta, SelectDataFO selectDataFo, DataEditorResultDTO resultDto, boolean toLast) {
        resultDto.setCatalog(tableMeta.getCatalog());
        resultDto.setSchema(tableMeta.getSchema());
        resultDto.setTable(tableMeta.getName());
        resultDto.setPageSize(selectDataFo.getPageSize());
        resultDto.setOffset(selectDataFo.getOffset());
        resultDto.setIsFirst(selectDataFo.getOffset() == 0);
        if (toLast) {
            resultDto.setHasNext(false);
        } else {
            resultDto.setHasNext(resultDto.getResultSet().size() == selectDataFo.getPageSize());
        }
    }

    private static String selectCount(RdpDataSourceDO dsDO, String catalog, String schema, String table, UmiTypes targetType, String condition) {
        DataEditorSpi spi = PluginManager.findDataEditorSpi(dsDO.getDataSourceType());

        RdbTable tableMeta = new RdbTable();
        tableMeta.setCatalog(catalog);
        tableMeta.setSchema(schema);
        tableMeta.setName(table);
        tableMeta.setUmiType(targetType);
        return spi.buildSelectCount(tableMeta, condition);
    }

    private static List<DataEditorChangeDTO> buildDML(RdpDataSourceDO dsDO, RdbTable tableMeta, List<DataEditorUpdateDTO> dataSet, boolean sem) {
        DataEditorSpi spi = PluginManager.findDataEditorSpi(dsDO.getDataSourceType());
        Map<DataEditorSqlType, List<DataEditorUpdateDTO>> groupMap = dataSet.stream().collect(Collectors.groupingBy(DataEditorUpdateDTO::getDmlType));
        List<DataEditorChangeDTO> sqlScript = new ArrayList<>();
        if (groupMap.containsKey(DataEditorSqlType.DELETE)) {
            for (DataEditorUpdateDTO data : groupMap.get(DataEditorSqlType.DELETE)) {
                DataEditorChangeDTO dto = new DataEditorChangeDTO();
                String deleteSql = spi.buildDelete(tableMeta, data.getWhereData());
                dto.setSql(deleteSql + (sem ? ";" : ""));
                dto.setSqlType(DataEditorSqlType.DELETE);
                dto.setSequence(data.getSequence());
                sqlScript.add(dto);
            }
        }
        if (groupMap.containsKey(DataEditorSqlType.UPDATE)) {
            for (DataEditorUpdateDTO data : groupMap.get(DataEditorSqlType.UPDATE)) {
                DataEditorChangeDTO dto = new DataEditorChangeDTO();
                String updateSql = spi.buildUpdate(tableMeta, data.getWhereData(), data.getUpdateData());
                dto.setSql(updateSql + (sem ? ";" : ""));
                dto.setSqlType(DataEditorSqlType.UPDATE);
                dto.setSequence(data.getSequence());
                sqlScript.add(dto);
            }
        }
        if (groupMap.containsKey(DataEditorSqlType.INSERT)) {
            for (DataEditorUpdateDTO data : groupMap.get(DataEditorSqlType.INSERT)) {
                DataEditorChangeDTO dto = new DataEditorChangeDTO();
                String insertSql = spi.buildInsert(tableMeta, data.getUpdateData());
                dto.setSql(insertSql + (sem ? ";" : ""));
                dto.setSqlType(DataEditorSqlType.INSERT);
                dto.setSequence(data.getSequence());
                sqlScript.add(dto);
            }
        }
        return sqlScript;
    }

    private static boolean checkAndFillRefresh(Map<String, String> recordData, RdbTable tableMeta, Map<String, Object> refreshData, DataEditorSqlType dmlSql) {
        if (!DataEditorSqlType.INSERT.equals(dmlSql)) {
            return false;
        }
        boolean refresh = false;
        for (RdbColumn colDef : tableMeta.getColumns().values()) {
            String colValue = recordData.get(colDef.getName());
            if (colValue == null && colDef.getDefaultValue() != null) {
                refresh = true;
            }
            refreshData.put(colDef.getName(), colValue);
        }
        return refresh;
    }
}
