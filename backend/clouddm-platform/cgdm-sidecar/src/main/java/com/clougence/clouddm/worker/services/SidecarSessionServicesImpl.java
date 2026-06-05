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
 */package com.clougence.clouddm.worker.services;

import java.util.Map;

import jakarta.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.execute.session.result.ValueProcessService;
import com.clougence.clouddm.sdk.execute.tools.ToolSession;
import com.clougence.clouddm.sdk.execute.tools.ToolSessionContextDTO;
import com.clougence.clouddm.sdk.execute.resultset.file.ResultReaderService;
import com.clougence.clouddm.sdk.service.execute.SessionService;
import com.clougence.clouddm.worker.component.resource.OnlineDsResourceManager;
import com.clougence.clouddm.worker.component.resource.OnlineToolResourceManager;
import com.clougence.clouddm.worker.component.session.SessionManager;
import com.clougence.clouddm.worker.component.tools.ToolSessionManager;

@Service
public class SidecarSessionServicesImpl implements SessionService {

    @Resource
    private OnlineDsResourceManager   dsRM;
    @Resource
    private OnlineToolResourceManager toolRM;
    @Resource
    private SessionManager            dsSM;
    @Resource
    private ToolSessionManager        toolSM;
    @Resource
    private ResultReaderService       readerService;

    private ValueProcessService       processSpi = null;

    @Override
    public SessionContextDTO createDsSessionCtx(DataSourceConfig dsConfig, Map<String, Object> params) {
        SessionSpi sessionSpi = PluginManager.findSessionSpi(dsConfig.getDataSourceType());
        return sessionSpi.createSessionContext(dsConfig, params);
    }

    @Override
    public Session createDsSession(DataSourceConfig dsConfig, SessionContextDTO contextDTO) {
        return this.dsSM.createSession(this.dsRM, dsConfig, contextDTO);
    }

    @Override
    public ToolSession createToolSession(ToolConfig dsConfig, ToolSessionContextDTO contextDTO) {
        return this.toolSM.createSession(this.toolRM, dsConfig, contextDTO);
    }

    @Override
    public ValueProcessService getProcessSpi() {
        if (this.processSpi == null) {
            try {
                this.processSpi = PluginManager.findService(ValueProcessService.class);
            } catch (UnsupportedOperationException e) {
                this.processSpi = null;
            }
        }
        return this.processSpi;
    }

    @Override
    public ResultReaderService getResultService() { return this.readerService; }
}
