package com.clougence.clouddm.worker.component.session;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;

public interface SessionManager {

    int getMaxSessionCount();

    int getSessionCount();

    boolean hasSessionById(String sessionId);

    SessionAgent createSession(DsResourceManager rm, DataSourceConfig dsConfig, SessionContextDTO contextDTO);

    SessionAgent getSessionById(String sessionId);

    void closeSessionById(String sessionId);
}
