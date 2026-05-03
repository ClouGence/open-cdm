package com.clougence.clouddm.worker.services;

import java.util.Map;

import javax.annotation.Resource;

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
