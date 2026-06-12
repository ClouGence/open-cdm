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
package com.clougence.clouddm.console.web.service.sdk;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.component.resultfile.ResultFileReaderService;
import com.clougence.clouddm.sdk.execute.resultset.file.ResultReaderService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.result.ValueProcessService;
import com.clougence.clouddm.sdk.execute.tools.ToolSession;
import com.clougence.clouddm.sdk.execute.tools.ToolSessionContextDTO;
import com.clougence.clouddm.sdk.service.execute.SessionService;

@Service
public class ConsoleSessionServiceImpl implements SessionService {

    private final ResultReaderService resultReaderService = new ResultFileReaderService();

    @Override
    public SessionContextDTO createDsSessionCtx(DataSourceConfig dsConfig, Map<String, Object> params) {
        throw new UnsupportedOperationException("Console session service does not support datasource session context.");
    }

    @Override
    public Session createDsSession(DataSourceConfig dsConfig, SessionContextDTO contextDTO) {
        throw new UnsupportedOperationException("Console session service does not support datasource session.");
    }

    @Override
    public ToolSession createToolSession(ToolConfig dsConfig, ToolSessionContextDTO contextDTO) {
        throw new UnsupportedOperationException("Console session service does not support tool session.");
    }

    @Override
    public ValueProcessService getProcessSpi() { return null; }

    @Override
    public ResultReaderService getResultService() { return this.resultReaderService; }
}
