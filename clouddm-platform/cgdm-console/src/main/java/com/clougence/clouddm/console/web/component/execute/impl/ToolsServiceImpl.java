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
package com.clougence.clouddm.console.web.component.execute.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.session.tools.ToolsRService;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.comm.model.RSocketSendType;
import com.clougence.clouddm.console.web.component.dsconfig.DmToolConfigService;
import com.clougence.clouddm.console.web.component.execute.ToolsService;
import com.clougence.clouddm.console.web.constants.DmErrorCode;
import com.clougence.clouddm.console.web.constants.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.DsSessionType;
import com.clougence.clouddm.console.web.dal.mapper.DmDsSessionMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmWorkerMapper;
import com.clougence.clouddm.console.web.dal.model.DmDsSessionDO;
import com.clougence.clouddm.console.web.dal.model.DmWorkerDO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.clouddm.console.web.util.MessageUtils;
import com.clougence.clouddm.sdk.execute.tools.ToolRequestDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolResultDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolSessionContextDTO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020-01-20 21:11
 * @since 1.1.3
 */
@Slf4j
@Service
public class ToolsServiceImpl implements ToolsService {

    @Resource
    private DmToolConfigService  dmToolConfigService;
    @Resource
    private DmWorkerMapper       dmWorkerMapper;
    @Resource
    private ToolsRService        toolsRService;
    @Resource
    private DmDsSessionMapper    sessionMapper;

    private RSocketSendDTO buildRSocketSendDTO(long bindClusterId) {
        List<DmWorkerDO> workers = this.dmWorkerMapper.queryConnectedByClusterId(bindClusterId);
        if (workers.isEmpty()) {
            throw new ErrorMessageException(DmErrorCode.CLUSTER_HAVE_NO_WORKS_ERROR.code(), MessageUtils.getClusterHaveNoWorksErrorMessage(bindClusterId));
        }

        DmWorkerDO worker = workers.get(new Random(System.currentTimeMillis()).nextInt(workers.size()));

        RSocketSendDTO sendDTO = new RSocketSendDTO();
        sendDTO.setClusterId(worker.getClusterId());
        sendDTO.setWorkerSeqNumber(worker.getWorkerSeqNumber());
        sendDTO.setWorkerIP(worker.getWorkerIp());
        sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);
        return sendDTO;
    }

    private RSocketSendDTO buildRSocketSendDTO(String wsn) {
        DmWorkerDO workerStatus = this.dmWorkerMapper.queryConnectedByWsn(wsn);
        if (workerStatus != null) {
            RSocketSendDTO sendDTO = new RSocketSendDTO();
            sendDTO.setClusterId(workerStatus.getClusterId());
            sendDTO.setWorkerSeqNumber(workerStatus.getWorkerSeqNumber());
            sendDTO.setWorkerIP(workerStatus.getWorkerIp());
            sendDTO.setUid(workerStatus.getUid());
            sendDTO.setRSocketSendType(RSocketSendType.SPECIFIED);
            return sendDTO;
        } else {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.WORKER_STATUS_OFFLINE_ERROR.name(), wsn));
        }
    }

    @Override
    public boolean hasSession(String curUid, String sessionId) {
        DmDsSessionDO sessionDO = this.sessionMapper.queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return false;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO.getWsn());
        return this.toolsRService.hasSession(sendDTO, sessionId);
    }

    @Override
    public DmDsSessionDO getSessionInfo(String curUid, String sessionId) {
        return this.sessionMapper.queryBySessionId(curUid, sessionId);
    }

    @Override
    public String createSession(String curUid, String toolName, ToolSessionContextDTO context) {
        String sessionId = context.getSessionId();
        if (StringUtils.isBlank(sessionId)) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_NEED_SESSION_ID_ERROR.name()));
        }

        // close and remove old data.
        DmDsSessionDO sessionDO = this.sessionMapper.queryBySessionId(curUid, sessionId);
        if (sessionDO != null) {
            RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO.getWsn());
            this.toolsRService.closeSession(sendDTO, sessionId);
            this.sessionMapper.deleteBySessionId(sessionId);
        }

        // gen new session.
        RSocketSendDTO sendDTO = buildRSocketSendDTO(context.getBindClusterId());
        sessionDO = new DmDsSessionDO();
        sessionDO.setUid(curUid);
        sessionDO.setSessionId(sessionId);
        sessionDO.setSessionType(DsSessionType.QUERY);
        sessionDO.setWsn(sendDTO.getWorkerSeqNumber());
        sessionDO.setClusterId(String.valueOf(context.getBindClusterId()));
        sessionDO.setDatasourceId(0L);
        sessionDO.setDatasourceType(null);
        sessionDO.setConfig(JsonUtils.toJson(context));
        sessionDO.setGmtCreate(new Date());
        sessionDO.setGmtModified(new Date());

        int insert = this.sessionMapper.insert(sessionDO);
        if (insert != 1) {
            throw new RuntimeException("sessionDO insert failed.");
        }

        // tx session
        ToolConfig toolConfig = this.dmToolConfigService.fetchToolConfig(toolName);
        this.toolsRService.createSession(sendDTO, toolConfig, context);
        return sessionId;
    }

    @Override
    public void closeSession(String curUid, String sessionId) {
        if (StringUtils.isBlank(sessionId)) {
            return;
        }

        DmDsSessionDO sessionDO = this.sessionMapper.queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO.getWsn());
        this.toolsRService.closeSession(sendDTO, sessionId);
        this.sessionMapper.deleteBySessionId(sessionId);
    }

    @Override
    public String invoke(String curUid, String sessionId, String methodKey, ToolRequestDTO requestDTO) {
        DmDsSessionDO sessionDO = this.sessionMapper.queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return null;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO.getWsn());
        ToolResultDTO resultDTO = this.toolsRService.invoke(sendDTO, sessionId, methodKey, requestDTO);
        if (!resultDTO.isSuccess()) {
            throw new RuntimeException(resultDTO.getMessage());
        } else {
            return resultDTO.getBody();
        }
    }

    @Override
    public String tailLog(String curUid, String sessionId, ToolRequestDTO requestDTO) {
        DmDsSessionDO sessionDO = this.sessionMapper.queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return null;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO.getWsn());
        ToolResultDTO resultDTO = this.toolsRService.tailLog(sendDTO, sessionId, requestDTO);
        if (!resultDTO.isSuccess()) {
            throw new RuntimeException(resultDTO.getMessage());
        } else {
            return resultDTO.getBody();
        }
    }

    @Override
    public String tailStatus(String curUid, String sessionId, ToolRequestDTO requestDTO) {
        DmDsSessionDO sessionDO = this.sessionMapper.queryBySessionId(curUid, sessionId);
        if (sessionDO == null) {
            return null;
        }

        RSocketSendDTO sendDTO = buildRSocketSendDTO(sessionDO.getWsn());
        ToolResultDTO resultDTO = this.toolsRService.tailStatus(sendDTO, sessionId, requestDTO);
        if (!resultDTO.isSuccess()) {
            throw new RuntimeException(resultDTO.getMessage());
        } else {
            return resultDTO.getBody();
        }
    }
}
