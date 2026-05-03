package com.clougence.clouddm.sdk.execute.tools;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.sdk.execute.session.SessionCloseListener;

public interface ToolSession extends AutoCloseable {

    String getSessionId();

    ToolConfig getConfig();

    ToolResultDTO invoke(String methodKey, ToolRequestDTO requestDTO);

    ToolResultDTO tailLog(ToolRequestDTO requestDTO);

    ToolResultDTO tailStatus(ToolRequestDTO requestDTO);

    void addCloseListener(SessionCloseListener closeListener);
}
