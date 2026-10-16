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
package com.clougence.clouddm.console.web.model.ai;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public final class AiTableSchema {
    private final AiObject           object;

    private final String             comment;

    private final List<AiColumn>     columns;

    private final boolean            columnsComplete;

    private final List<String>       primaryKey;

    private final List<AiForeignKey> foreignKeys;

    private final boolean            keysKnown;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final Instant            schemaReadAt;

    @JsonCreator
    public AiTableSchema(@JsonProperty("object") AiObject object, @JsonProperty("comment") String comment, @JsonProperty("columns") List<AiColumn> columns,
                         @JsonProperty("columnsComplete") boolean columnsComplete, @JsonProperty("primaryKey") List<String> primaryKey,
                         @JsonProperty("foreignKeys") List<AiForeignKey> foreignKeys, @JsonProperty("keysKnown") boolean keysKnown,
                         @JsonProperty("schemaReadAt") Instant schemaReadAt){
        this.object = object;
        this.comment = comment;
        this.columns = columns;
        this.columnsComplete = columnsComplete;
        this.primaryKey = primaryKey;
        this.foreignKeys = foreignKeys;
        this.keysKnown = keysKnown;
        this.schemaReadAt = schemaReadAt;
    }
}
