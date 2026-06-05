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
 */package com.clougence.clouddm.worker.provider;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.session.tools.ToolsRService;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolRequestDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolResultDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolSession;
import com.clougence.clouddm.sdk.execute.tools.ToolSessionContextDTO;
import com.clougence.clouddm.worker.component.resource.OnlineToolResourceManager;
import com.clougence.clouddm.worker.component.tools.ToolSessionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RSocketApiClass
public class ToolsRServiceProvider implements ToolsRService {

    @Resource
    private OnlineToolResourceManager onlineRM;
    @Resource
    private ToolSessionManager        toolsManager;

    @Override
    public boolean hasSession(RSocketSendDTO sendDTO, String sessionId) {
        return this.toolsManager.hasSessionById(sessionId);
    }

    @Override
    public void createSession(RSocketSendDTO sendDTO, ToolConfig toolConfig, ToolSessionContextDTO contextDTO) {
        this.toolsManager.createSession(this.onlineRM, toolConfig, contextDTO);
    }

    @Override
    public void closeSession(RSocketSendDTO sendDTO, String sessionId) {
        if (this.toolsManager.hasSessionById(sessionId)) {
            this.toolsManager.closeSessionById(sessionId);
        }
    }

    @Override
    public ToolResultDTO invoke(RSocketSendDTO sendDTO, String sessionId, String methodKey, ToolRequestDTO requestDTO) {
        if (this.toolsManager.hasSessionById(sessionId)) {
            ToolSession target = this.toolsManager.getSessionById(sessionId);
            return target.invoke(methodKey, requestDTO);
        } else {
            throw new IllegalStateException("Session does not exist.");
        }
    }

    @Override
    public ToolResultDTO tailLog(RSocketSendDTO sendDTO, String sessionId, ToolRequestDTO requestDTO) {
        if (this.toolsManager.hasSessionById(sessionId)) {
            ToolSession target = this.toolsManager.getSessionById(sessionId);
            return target.tailLog(requestDTO);
        } else {
            throw new IllegalStateException("Session does not exist.");
        }
    }

    @Override
    public ToolResultDTO tailStatus(RSocketSendDTO sendDTO, String sessionId, ToolRequestDTO requestDTO) {
        if (this.toolsManager.hasSessionById(sessionId)) {
            ToolSession target = this.toolsManager.getSessionById(sessionId);
            return target.tailStatus(requestDTO);
        } else {
            throw new IllegalStateException("Session does not exist.");
        }
    }
}
