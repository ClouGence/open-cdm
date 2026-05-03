package com.clougence.clouddm.worker.component.tools;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.sdk.execute.resource.ToolResourceManager;
import com.clougence.clouddm.sdk.execute.tools.ToolSession;
import com.clougence.clouddm.sdk.execute.tools.ToolSessionContextDTO;

public interface ToolSessionManager {

    int getMaxSessionCount();

    int getSessionCount();

    boolean hasSessionById(String sessionId);

    ToolSession createSession(ToolResourceManager rm, ToolConfig toolConfig, ToolSessionContextDTO contextDTO);

    ToolSession getSessionById(String sessionId);

    void closeSessionById(String sessionId);
}
