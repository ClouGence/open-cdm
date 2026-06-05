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
package com.clougence.clouddm.faker.engine;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.service.execute.SessionService;

import lombok.Getter;

public class SessionFactory {

    @Getter
    private final SessionService      service;
    private final DataSourceConfig    dsConfig;
    private final Map<String, Object> params;

    public SessionFactory(SessionService service, DataSourceConfig dsConfig, Map<String, Object> params){
        this.service = service;
        this.dsConfig = dsConfig;
        this.params = params;
    }

    public Session createSession() throws Exception {
        SessionContextDTO ctx = this.service.createDsSessionCtx(this.dsConfig, this.params);
        return this.service.createDsSession(this.dsConfig, ctx);
    }

}
