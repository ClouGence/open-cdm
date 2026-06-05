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
 */package com.clougence.clouddm.worker.component.tools;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.sdk.execute.resource.ToolObject;
import com.clougence.clouddm.sdk.execute.resource.ToolResourceManager;
import com.clougence.clouddm.sdk.execute.tools.ToolSession;
import com.clougence.clouddm.sdk.execute.tools.ToolSessionContextDTO;
import com.clougence.clouddm.worker.global.config.DmSidecarConfig;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.utils.StringUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2022/6/1 15:06:59
 */
@Slf4j
@Service
public class ToolSessionManagerImpl implements ToolSessionManager, UnifiedPostConstruct {

    @Resource
    private DmSidecarConfig                dmConfig;
    private final Map<String, ToolSession> sessionMap = new ConcurrentHashMap<>();
    private final AtomicBoolean            inited     = new AtomicBoolean(false);

    @Override
    public void init() throws Exception {
        if (this.inited.compareAndSet(false, true)) {
            // this.sessionManagerThread = new Thread(this::checkAndClearSession);
            // this.sessionManagerThread.setDaemon(true);
            // this.sessionManagerThread.start();
        }
    }

    @Override
    public void stop() {
        if (this.inited.compareAndSet(true, false)) {
            // try {
            //      if (this.sessionManagerThread != null) {
            //          this.sessionManagerThread.interrupt();
            //      }
            // } catch (Exception e) {
            //      log.error(e.getMessage(), e);
            // }
        }
    }

    @Override
    public int getMaxSessionCount() { return this.dmConfig.getMaxToolsCount(); }

    @Override
    public int getSessionCount() { return this.sessionMap.size(); }

    @Override
    public boolean hasSessionById(String sessionId) {
        return this.sessionMap.containsKey(sessionId);
    }

    @Override
    public ToolSession getSessionById(String sessionId) {
        return this.sessionMap.get(sessionId);
    }

    @SneakyThrows
    @Override
    public ToolSession createSession(ToolResourceManager rm, ToolConfig toolConfig, ToolSessionContextDTO contextDTO) {
        if (!rm.isReady()) {
            throw new RuntimeException("ResourceManager is not ready.");
        }

        String newSessionId = contextDTO.getSessionId();
        if (StringUtils.isBlank(newSessionId)) {
            throw new RuntimeException(newSessionId + " newSessionId is blank.");
        }

        if (this.sessionMap.containsKey(newSessionId)) {
            throw new RuntimeException(newSessionId + " newSessionId is exist.");
        }

        synchronized (this) {
            int maxSessionCount = this.dmConfig.getMaxToolsCount();
            if (this.getSessionCount() >= maxSessionCount) {
                throw new IllegalStateException("exceed tools max pool size: " + maxSessionCount);
            }

            ToolObject toolsObject = rm.requestResource(toolConfig);
            if (toolsObject == null) {
                String msg = "toolsName '" + toolConfig.getToolName() + "', toolsVersion '" + toolConfig.getVersion() + "'";
                throw new RuntimeException(msg + ", ToolsFactory not register.");
            }

            DefaultToolSession session;
            try {
                session = new DefaultToolSession(newSessionId, toolConfig, toolsObject);
                session.initSession(rm, contextDTO);
            } catch (Exception e) {
                toolsObject.close();
                throw e;
            }

            session.addCloseListener(this::closeSessionById);
            this.sessionMap.put(newSessionId, session);
            return session;
        }
    }

    @Override
    public void closeSessionById(String sessionId) {
        if (this.sessionMap.containsKey(sessionId)) {
            ToolSession toolSession = this.sessionMap.get(sessionId);
            if (toolSession == null) {
                return;
            }

            this.sessionMap.remove(sessionId);

            try {
                toolSession.close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
