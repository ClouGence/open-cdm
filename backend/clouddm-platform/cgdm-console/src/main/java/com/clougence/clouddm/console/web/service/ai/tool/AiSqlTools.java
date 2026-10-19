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
package com.clougence.clouddm.console.web.service.ai.tool;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.console.web.model.ai.AiSqlConstants;
import com.clougence.clouddm.console.web.service.ai.AiRunHandle;
import com.clougence.clouddm.ds.ai.tool.ChatTool;
import com.clougence.clouddm.ds.ai.tool.ChatToolParameter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** The selected database and cancellation handle belong to this run, not to a singleton tool bean. */
public final class AiSqlTools {
    private final AiMetadataTools   metadata;
    private final AiMetadataContext context;
    private final AiRunHandle       handle;

    public AiSqlTools(AiMetadataTools metadata, AiMetadataContext context, AiRunHandle handle){
        this.metadata = metadata;
        this.context = context;
        this.handle = handle;
    }

    @ChatTool(name = AiSqlConstants.TOOL_LIST_TABLES, description = "Search authorized table and view names in the current database. Use returned objectRef values to read their structure.")
    public Map<String, Object> listTables(@ChatToolParameter(name = AiSqlConstants.FIELD_KEYWORD, description = "Table or view name keyword; omit to list all.", required = false) @Size(max = 128) String keyword,
                                          @ChatToolParameter(name = AiSqlConstants.FIELD_CURSOR, description = "Pagination cursor returned by the preceding search.", required = false) @Pattern(regexp = AiSqlConstants.UUID_PATTERN) String cursor) {
        if (keyword == null) {
            keyword = "";
        }
        return metadata.listTables(context, keyword, cursor, handle);
    }

    @ChatTool(name = AiSqlConstants.TOOL_DESCRIBE_TABLES, description = "Read authorized table columns, comments and known keys using objectRefs returned by "
                                                                        + AiSqlConstants.TOOL_LIST_TABLES + " in this turn. Never execute SQL.")
    public Map<String, Object> describeTables(@ChatToolParameter(name = AiSqlConstants.FIELD_OBJECT_REFS, description = "Current-turn objectRefs returned by the table search.") @NotEmpty List<@Pattern(regexp = AiSqlConstants.UUID_PATTERN) String> objectRefs) {
        return metadata.describeTables(context, objectRefs, handle);
    }
}
