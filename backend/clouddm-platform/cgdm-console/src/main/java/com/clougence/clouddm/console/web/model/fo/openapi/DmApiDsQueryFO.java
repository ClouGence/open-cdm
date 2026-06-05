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
package com.clougence.clouddm.console.web.model.fo.openapi;

import java.util.List;

import com.clougence.clouddm.console.web.constants.DmMcpI18nKey;
import com.clougence.clouddm.console.web.global.mcp.model.McpField;
import com.clougence.clouddm.console.web.model.fo.editor.query.WsQueryType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2025/12/9 10:18:45
 */
@Getter
@Setter
public class DmApiDsQueryFO {

    @McpField(value = DmMcpI18nKey.F_LEVELS_DESC, required = true)
    private List<String> levels;

    @McpField(value = DmMcpI18nKey.F_QUERY_TYPE_DESC, required = true)
    private WsQueryType  queryType;

    @McpField(value = DmMcpI18nKey.F_QUERY_STRING_DESC, required = true)
    private String       queryString;

    @McpField(value = DmMcpI18nKey.F_QUERY_FORCE_DESC)
    private boolean      queryForce;

}
