package com.clougence.clouddm.sdk.service.execute;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.sdk.execute.resultset.file.ResultReaderService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.result.ValueProcessService;
import com.clougence.clouddm.sdk.execute.tools.ToolSession;
import com.clougence.clouddm.sdk.execute.tools.ToolSessionContextDTO;
import com.clougence.clouddm.sdk.service.Service;

public interface SessionService extends Service {

    SessionContextDTO createDsSessionCtx(DataSourceConfig dsConfig, Map<String, Object> params);

    Session createDsSession(DataSourceConfig dsConfig, SessionContextDTO contextDTO) throws Exception;

    ToolSession createToolSession(ToolConfig dsConfig, ToolSessionContextDTO contextDTO) throws Exception;

    ValueProcessService getProcessSpi();

    ResultReaderService getResultService();
}
