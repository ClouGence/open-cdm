/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.service.faker;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.base.metadata.ds.tools.FakerPluginConfig;
import com.clougence.clouddm.console.web.component.asyntask.AsyncTaskConfig;
import com.clougence.clouddm.console.web.component.asyntask.AsyncTaskScheduleService;
import com.clougence.clouddm.console.web.component.dsconfig.DmDsConfigService;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.console.web.component.execute.ToolsService;
import com.clougence.clouddm.console.web.component.schema.DsSchemaService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmLabelKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.model.fo.faker.FakerConfigFO;
import com.clougence.clouddm.console.web.model.fo.faker.FakerDefFO;
import com.clougence.clouddm.console.web.model.fo.faker.FakerInitFO;
import com.clougence.clouddm.console.web.model.fo.faker.FakerTableFO;
import com.clougence.clouddm.console.web.model.vo.faker.FakerColumnVO;
import com.clougence.clouddm.console.web.model.vo.faker.FakerDefVO;
import com.clougence.clouddm.console.web.model.vo.faker.FakerLogVO;
import com.clougence.clouddm.console.web.model.vo.faker.FakerPreviewVO;
import com.clougence.clouddm.console.web.service.asyntask.AsyncTaskService;
import com.clougence.clouddm.console.web.service.faker.asyntask.FakerAsyncTask;
import com.clougence.clouddm.console.web.service.faker.asyntask.FakerAsyncTaskConfig;
import com.clougence.clouddm.console.web.util.UiWebUtil;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.AsyncTaskProcessType;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAsyncTaskDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSessionDO;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.tools.ToolRequestDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolSessionContextDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolUtils;
import com.clougence.clouddm.sdk.model.faker.*;
import com.clougence.clouddm.sdk.ui.faker.FakerUiData;
import com.clougence.clouddm.sdk.ui.faker.FakerUiDefService;
import com.clougence.clouddm.sdk.ui.faker.FakerUiPanel;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FakerServiceImpl implements FakerService, FakerMethod {

    @Resource
    private ExecutionDal             executionDal;
    @Resource
    private DataSourceDal            dsDal;
    @Resource
    private AsyncTaskService         asyncTaskService;
    @Resource
    private ToolsService             toolsService;
    @Resource
    private DsSchemaService          dsSchemaService;
    @Resource
    private DmDsConfigService        dsConfigService;
    @Resource
    private AsyncTaskScheduleService scheduleService;

    @Override
    public FakerDefVO loadFakerDef(String puid, String uid, FakerDefFO fo) {
        FakerRunModel runModel = fo.getType();

        FakerUiDefService uiDefService = PluginManager.findService(FakerUiDefService.class);
        FakerUiPanel uiPanel;
        switch (runModel) {
            case FULL:
                uiPanel = uiDefService.fetchFakerUiPanelForFull(DmI18nUtils.getInstance());
                break;
            case INCREMENT:
                uiPanel = uiDefService.fetchFakerUiPanelForIncrement(DmI18nUtils.getInstance());
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return UiWebUtil.fakerDefUi2VO(uiPanel);
    }

    @Override
    public FakerUiData loadColumnSeed(String puid, String uid, FakerInitFO fo) {
        DsLevels levels = this.dsConfigService.parseLevels(fo.getLevels());
        if (levels == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }

        String tabName = fo.getTable();
        DmDsDO ds = this.dsDal.dsMapper().selectById(fo.getLevels().get(1));
        Map<UmiTypes, Object> levelsParam = levels.levelsParam();

        Map<String, List<RdbColumn>> columns = this.dsSchemaService.loadColumns(puid, ds, levelsParam, UmiTypes.Table, Collections.singletonList(tabName));
        FakerUiDefService uiDefService = PluginManager.findService(FakerUiDefService.class);
        return uiDefService.fetchFakerUiData(ds.getDataSourceType(), columns.get(tabName), fo.getType());
    }

    private ToolSessionContextDTO createToolCtx(FakerConfigFO fo) {
        DsLevels levels = this.dsConfigService.parseLevels(fo.getLevels());
        if (levels == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.DS_NOT_EXIST_ERROR.name()));
        }

        DmDsDO ds = levels.dsDO();
        Map<UmiTypes, Object> levelsParam = levels.levelsParam();

        String yaml = ConfigConvertYamlUtils.foConvertYaml(fo, levelsParam);
        List<FakerTableFO> tableConfigs = fo.getTableConfigs();

        FakerConfigDTO fakerConfig = new FakerConfigDTO();
        fakerConfig.setDsType(ds.getDataSourceType());
        fakerConfig.setDsId(ds.getId());
        fakerConfig.setCatalog((String) levelsParam.get(UmiTypes.Catalog));
        fakerConfig.setSchema((String) levelsParam.get(UmiTypes.Schema));

        fakerConfig.setYaml(yaml);
        fakerConfig.setTransaction(fo.isTransaction());
        fakerConfig.setIgnoreErrors(fo.isIgnoreErrors());
        fakerConfig.setPThreadCnt(fo.getProducer());
        fakerConfig.setWThreadCnt(fo.getWriter());

        fakerConfig.setRunModel(fo.getType());
        if (fo.getType() == FakerRunModel.FULL) {
            Map<String, Integer> tableTotal = tableConfigs.stream().collect(Collectors.toMap(FakerTableFO::getName, FakerTableFO::getTotal));
            fakerConfig.setWriterTotal(tableTotal);
        } else if (fo.getType() == FakerRunModel.INCREMENT) {
            fakerConfig.setWorkingTime(fo.getTime());
            fakerConfig.setDeleteRatio(fo.getDeleteRatio());
            fakerConfig.setInsertRatio(fo.getInsertRatio());
            fakerConfig.setUpdateRatio(fo.getUpdateRatio());
        }

        DmDsConfigDO dmDsConfigDO = this.dsDal.configMapper().queryById(ds.getUid(), ds.getId());
        ToolSessionContextDTO contextDTO = new ToolSessionContextDTO();
        contextDTO.setSessionId(UUID.randomUUID().toString().replace("-", ""));
        contextDTO.setConfiguration(JsonUtils.toJson(fakerConfig));
        contextDTO.setBindClusterId(dmDsConfigDO.getBindClusterId());

        return contextDTO;
    }

    @Override
    public FakerPreviewVO dataPreview(String puid, String uid, FakerConfigFO fo) {
        String sessionId = null;
        String result;
        try {
            sessionId = this.toolsService.createSession(uid, FakerPluginConfig.TOOL_NAME, this.createToolCtx(fo));
            result = this.toolsService.invoke(uid, sessionId, PREVIEW, null);
        } finally {
            this.toolsService.closeSession(uid, sessionId);
        }
        FakerPreviewDTO dto = JsonUtils.toObj(result, FakerPreviewDTO.class);
        FakerPreviewVO vo = new FakerPreviewVO();
        //        vo.setSessionId(sessionId);

        HashMap<String, List<FakerColumnVO>> map = new HashMap<>();
        dto.getPreview().forEach((k, v) -> {
            ArrayList<FakerColumnVO> columnVOs = new ArrayList<>();
            for (FakerLineDTO line : v) {
                FakerColumnVO columnVO = new FakerColumnVO(line.getOldValue(), line.getNewValue(), line.getUseWhere(), line.getUseSet(), line.getType());
                columnVOs.add(columnVO);
            }
            map.put(k, columnVOs);
        });
        vo.setData(map);
        return vo;
    }

    @Override
    public String execute(String uid, FakerConfigFO fo) {
        String sessionId = this.toolsService.createSession(uid, FakerPluginConfig.TOOL_NAME, this.createToolCtx(fo));
        this.toolsService.invoke(uid, sessionId, START, null);

        FakerAsyncTaskConfig taskConfig = new FakerAsyncTaskConfig();
        taskConfig.setSessionId(sessionId);
        taskConfig.setUserId(uid);
        taskConfig.setFoConfig(fo);

        AsyncTaskConfig config = new AsyncTaskConfig();
        List<String> tabNames = fo.getTableConfigs().stream().map(FakerTableFO::getName).sorted().collect(Collectors.toList());
        if (tabNames.size() == 1) {
            String tabNamesStr = tabNames.get(0);
            if (fo.getType() == FakerRunModel.FULL) {
                config.setTitle(DmI18nUtils.getMessage(I18nDmMsgKeys.FAKER_FULL_ONE_TABLE_ASYNC_TITLE_MESSAGE.name(), tabNamesStr));
                config.setDescription(DmI18nUtils.getMessage(I18nDmMsgKeys.FAKER_FULL_ONE_TABLE_ASYNC_DESC_MESSAGE.name(), tabNamesStr));
            } else {
                config.setTitle(DmI18nUtils.getMessage(I18nDmMsgKeys.FAKER_INCREMENT_ONE_TABLE_ASYNC_TITLE_MESSAGE.name(), tabNamesStr));
                config.setDescription(DmI18nUtils.getMessage(I18nDmMsgKeys.FAKER_INCREMENT_ONE_TABLE_ASYNC_DESC_MESSAGE.name(), tabNamesStr));
            }
        } else {
            String tabNamesStr = StringUtils.join(tabNames.toArray(), ",");
            String tabNameSize = String.valueOf(tabNames.size());
            if (fo.getType() == FakerRunModel.FULL) {
                config.setTitle(DmI18nUtils.getMessage(I18nDmMsgKeys.FAKER_FULL_MANY_TABLE_ASYNC_TITLE_MESSAGE.name(), tabNamesStr, tabNameSize));
                config.setDescription(DmI18nUtils.getMessage(I18nDmMsgKeys.FAKER_FULL_MANY_TABLE_ASYNC_DESC_MESSAGE.name(), tabNamesStr, tabNameSize));
            } else {
                config.setTitle(DmI18nUtils.getMessage(I18nDmMsgKeys.FAKER_INCREMENT_MANY_TABLE_ASYNC_TITLE_MESSAGE.name(), tabNamesStr, tabNameSize));
                config.setDescription(DmI18nUtils.getMessage(I18nDmMsgKeys.FAKER_INCREMENT_MANY_TABLE_ASYNC_DESC_MESSAGE.name(), tabNamesStr, tabNameSize));
            }
        }

        config.setConfigData(JsonUtils.toJson(taskConfig));
        config.setShowInDock(true);
        config.setProcessType(AsyncTaskProcessType.PROGRESS);
        config.setHandlerType(FakerAsyncTask.class);
        config.setBizType(FakerPluginConfig.TOOL_NAME);
        config.setBizId(sessionId);
        this.asyncTaskService.submitTask(uid, config);

        return sessionId;
    }

    @Override
    public boolean hasInstanceById(String uid, String sessionId) {
        return this.toolsService.hasSession(uid, sessionId);
    }

    @Override
    public FakerLogVO tailLog(String uid, String sessionId, int startLine) {
        if (StringUtils.isBlank(sessionId)) {
            throw new NullPointerException("tool SessionId is empty.");
        }

        try {
            FakerTailRequestDTO req = new FakerTailRequestDTO();
            req.setStartLine(startLine);
            ToolRequestDTO requestDTO = ToolUtils.buildRequest(JsonUtils.toJson(req));

            String result = this.toolsService.tailLog(uid, sessionId, requestDTO);

            FakerTailResponseDTO dto = JsonUtils.toObj(result, FakerTailResponseDTO.class);
            this.executionDal.sessionMapper().updateAttachment(sessionId, JsonUtils.toJson(dto));

            FakerLogVO vo = new FakerLogVO();
            vo.setSuccess(true);
            vo.setSuccessTotal(dto.getSuccessTotal());
            vo.setSuccessInsertTotal(dto.getSuccessInsertTotal());
            vo.setSuccessUpdateTotal(dto.getSuccessUpdateTotal());
            vo.setSuccessDeleteTotal(dto.getSuccessDeleteTotal());
            vo.setFailedTotal(dto.getFailedTotal());
            vo.setFailedInsertTotal(dto.getFailedInsertTotal());
            vo.setFailedUpdateTotal(dto.getFailedUpdateTotal());
            vo.setFailedDeleteTotal(dto.getFailedDeleteTotal());
            vo.setWriteAvgTime(dto.getWriteAvgTimeMs() + " " + DmI18nUtils.getMessage(I18nDmLabelKeys.LABEL_TIME_UNIT_MS.name()));
            vo.setLogArr(dto.getLogArr());
            vo.setStatus(dto.getStatus());
            vo.setEndLine(dto.getEndLine());
            vo.setLogFile(dto.getLogFile());
            vo.setLogHost(dto.getLogHost());
            return vo;
        } catch (Exception e) {
            FakerLogVO vo = new FakerLogVO();
            vo.setSuccess(false);
            vo.setMessage(e.getMessage());

            if (this.toolsService.hasSession(uid, sessionId)) {
                DmExecSessionDO sessionDO = this.executionDal.sessionMapper().queryBySessionId(uid, sessionId);
                FakerTailResponseDTO arrach = JsonUtils.toObj(sessionDO.getAttach(), FakerTailResponseDTO.class);
                vo.setSuccessTotal(arrach.getSuccessTotal());
                vo.setSuccessInsertTotal(arrach.getSuccessInsertTotal());
                vo.setSuccessUpdateTotal(arrach.getSuccessUpdateTotal());
                vo.setSuccessDeleteTotal(arrach.getSuccessDeleteTotal());
                vo.setFailedTotal(arrach.getFailedTotal());
                vo.setFailedInsertTotal(arrach.getFailedInsertTotal());
                vo.setFailedUpdateTotal(arrach.getFailedUpdateTotal());
                vo.setFailedDeleteTotal(arrach.getFailedDeleteTotal());
                vo.setWriteAvgTime(arrach.getWriteAvgTimeMs() + " " + DmI18nUtils.getMessage(I18nDmLabelKeys.LABEL_TIME_UNIT_MS.name()));
                vo.setLogArr(arrach.getLogArr());
                vo.setStatus(arrach.getStatus());
                vo.setEndLine(arrach.getEndLine());
                vo.setLogFile(arrach.getLogFile());
                vo.setLogHost(arrach.getLogHost());
                return vo;
            } else {
                vo.setStatus(FakerRunStatus.COMPLETE);
            }
            return vo;
        }
    }

    @Override
    public FakerStatusDTO tailStatus(String uid, String sessionId) {
        String result = this.toolsService.tailStatus(uid, sessionId, null);
        if (StringUtils.isNotBlank(result)) {
            return JsonUtils.toObj(result, FakerStatusDTO.class);
        } else {
            return null;
        }
    }

    @Override
    public void pause(String uid, String sessionId) {
        this.toolsService.invoke(uid, sessionId, PAUSE, null);
        DmExecAsyncTaskDO taskDO = this.asyncTaskService.queryAsyncTaskByBizId(sessionId, FakerPluginConfig.TOOL_NAME);
        scheduleService.pauseTask(taskDO.getId(), "Pause By Manual.");
    }

    @Override
    public void resume(String uid, String sessionId) {
        this.toolsService.invoke(uid, sessionId, RESUME, null);
        DmExecAsyncTaskDO taskDO = this.asyncTaskService.queryAsyncTaskByBizId(sessionId, FakerPluginConfig.TOOL_NAME);
        scheduleService.resumeTask(taskDO.getId(), "Resume By Manual.");
    }

    @Override
    public void close(String uid, String sessionId) {
        this.toolsService.closeSession(uid, sessionId);
    }

    @Override
    public FakerConfigFO fetchFoConfigByToolsSession(String uid, String sessionId) {
        DmExecAsyncTaskDO taskDO = this.asyncTaskService.queryAsyncTaskByBizId(sessionId, FakerPluginConfig.TOOL_NAME);
        if (taskDO != null) {
            FakerAsyncTaskConfig config = JsonUtils.toObj(taskDO.getConfigData(), FakerAsyncTaskConfig.class);
            return config.getFoConfig();
        }
        return null;
    }
}
