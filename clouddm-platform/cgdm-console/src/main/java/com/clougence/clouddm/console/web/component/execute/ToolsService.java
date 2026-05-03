package com.clougence.clouddm.console.web.component.execute;

import com.clougence.clouddm.console.web.dal.model.DmDsSessionDO;
import com.clougence.clouddm.sdk.execute.tools.ToolRequestDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolSessionContextDTO;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface ToolsService {

    boolean hasSession(String curUid, String sessionId);

    DmDsSessionDO getSessionInfo(String curUid, String sessionId);

    String createSession(String curUid, String toolName, ToolSessionContextDTO contextDTO);

    void closeSession(String curUid, String sessionId);

    String invoke(String curUid, String sessionId, String methodKey, ToolRequestDTO requestDTO);

    String tailLog(String curUid, String sessionId, ToolRequestDTO requestDTO);

    String tailStatus(String curUid, String sessionId, ToolRequestDTO requestDTO);
}
