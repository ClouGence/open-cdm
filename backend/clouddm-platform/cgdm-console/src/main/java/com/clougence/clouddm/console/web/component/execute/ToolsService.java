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
package com.clougence.clouddm.console.web.component.execute;

import com.clougence.clouddm.platform.dal.model.execution.DmExecSessionDO;
import com.clougence.clouddm.sdk.execute.tools.ToolRequestDTO;
import com.clougence.clouddm.sdk.execute.tools.ToolSessionContextDTO;

/**
 * @author mode 2020-01-20 21:04
 * @since 1.1.3
 */
public interface ToolsService {

    boolean hasSession(String curUid, String sessionId);

    DmExecSessionDO getSessionInfo(String curUid, String sessionId);

    String createSession(String curUid, String toolName, ToolSessionContextDTO contextDTO);

    void closeSession(String curUid, String sessionId);

    String invoke(String curUid, String sessionId, String methodKey, ToolRequestDTO requestDTO);

    String tailLog(String curUid, String sessionId, ToolRequestDTO requestDTO);

    String tailStatus(String curUid, String sessionId, ToolRequestDTO requestDTO);
}
