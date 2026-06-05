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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.MDC;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.sdk.execute.resource.ToolResourceManager;
import com.clougence.clouddm.sdk.execute.session.SessionCloseListener;
import com.clougence.clouddm.sdk.execute.tools.*;
import com.clougence.utils.timer.Timer;

public abstract class AbstractToolSession implements ToolSession {

    // self info
    protected static final Logger            log = ToolUtils.getLoggerAppender();
    protected final String                   newSessionId;
    protected final ToolConfig               toolsConfig;

    // context and env
    protected Timer                          globalTimer;
    private final List<SessionCloseListener> closeListenerList;

    public AbstractToolSession(String newSessionId, ToolConfig toolsConfig){
        this.newSessionId = Objects.requireNonNull(newSessionId, "newSessionId is null.");
        this.toolsConfig = Objects.requireNonNull(toolsConfig, "toolsConfig is null.");
        this.closeListenerList = new ArrayList<>();
    }

    protected void prepareMDC(ToolConfig toolsConfig, String toolSessionId) {
        String basePath = String.format("/plugins/%s/%s", toolsConfig.getToolName().toLowerCase(), toolSessionId);
        String logPath = GlobalConfUtils.getLogHome() + basePath;

        MDC.put("sessionKey", basePath + "/main");
        MDC.put(ToolUtils.MDCKEY_LOG_PATH, logPath);
        MDC.put(ToolUtils.MDCKEY_LOG_FILE, logPath + "/main.log");
    }

    protected void clearMDC() {
        MDC.remove("sessionKey");
        MDC.remove(ToolUtils.MDCKEY_LOG_PATH);
        MDC.remove(ToolUtils.MDCKEY_LOG_FILE);
    }

    public void initSession(ToolResourceManager rm, ToolSessionContextDTO initContextDTO) throws Exception {
        this.globalTimer = rm.getTimer(this.getConfig());

        try {
            this.prepareMDC(this.toolsConfig, this.newSessionId);
            this.currentResource().init(initContextDTO);
            log.info("tool init complete.");
        } catch (Exception e) {
            log.error("tool initSession failed, " + e.getMessage(), e);
            throw e;
        } finally {
            this.clearMDC();
        }
    }

    @Override
    public void addCloseListener(SessionCloseListener closeListener) {
        this.closeListenerList.add(closeListener);
    }

    @Override
    public String getSessionId() { return this.newSessionId; }

    @Override
    public ToolConfig getConfig() { return this.toolsConfig; }

    protected void triggerCloseListener() {
        for (SessionCloseListener listener : this.closeListenerList) {
            listener.onClose(this.getSessionId());
        }
    }

    protected abstract Tool currentResource();

    @Override
    public ToolResultDTO invoke(String methodKey, ToolRequestDTO requestDTO) {
        try {
            this.prepareMDC(this.toolsConfig, this.newSessionId);
            return this.currentResource().invoke(methodKey, requestDTO);
        } catch (Exception e) {
            log.error("tool invoke '" + methodKey + "' failed, " + e.getMessage(), e);
            return ToolUtils.buildError(e);
        } finally {
            this.clearMDC();
        }
    }

    @Override
    public ToolResultDTO tailLog(ToolRequestDTO requestDTO) {
        try {
            this.prepareMDC(this.toolsConfig, this.newSessionId);
            return this.currentResource().tailLog(requestDTO);
        } catch (Exception e) {
            log.error("tool tailLog failed, " + e.getMessage(), e);
            return ToolUtils.buildError(e);
        } finally {
            this.clearMDC();
        }
    }

    @Override
    public ToolResultDTO tailStatus(ToolRequestDTO requestDTO) {
        try {
            this.prepareMDC(this.toolsConfig, this.newSessionId);
            return this.currentResource().tailStatus(requestDTO);
        } catch (Exception e) {
            log.error("tool tailStatus failed, " + e.getMessage(), e);
            return ToolUtils.buildError(e);
        } finally {
            this.clearMDC();
        }
    }
}
