package com.clougence.clouddm.api.sidecar.session.tools;

import com.clougence.clouddm.base.metadata.ds.ToolConfig;
import com.clougence.clouddm.comm.RSocketApiClass;
import com.clougence.clouddm.comm.model.RSocketSendDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolRequestDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolResultDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolSessionContextDTO;

@RSocketApiClass
public interface ToolsRService {

    boolean hasSession(RSocketSendDTO sendDTO, String sessionId);

    void createSession(RSocketSendDTO sendDTO, ToolConfig toolConfig, ToolSessionContextDTO contextDTO);

    void closeSession(RSocketSendDTO sendDTO, String sessionId);

    ToolResultDTO invoke(RSocketSendDTO sendDTO, String sessionId, String methodKey, ToolRequestDTO requestDTO);

    ToolResultDTO tailLog(RSocketSendDTO sendDTO, String sessionId, ToolRequestDTO requestDTO);

    ToolResultDTO tailStatus(RSocketSendDTO sendDTO, String sessionId, ToolRequestDTO requestDTO);
}
