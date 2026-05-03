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
