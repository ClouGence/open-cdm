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
