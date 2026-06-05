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
package com.clougence.clouddm.console.web.global.mcp.json;

import static com.clougence.clouddm.console.web.global.mcp.utils.RagUtils.ensureNotBlank;
import static com.clougence.clouddm.console.web.global.mcp.utils.RagUtils.quoted;

import java.util.Objects;

public class JsonRawSchema implements JsonSchemaElement {

    private final String schema;

    public JsonRawSchema(Builder builder){
        this.schema = ensureNotBlank(builder.schema, "schema");
    }

    @Override
    public String description() {
        return null;
    }

    public String schema() {
        return schema;
    }

    public static JsonRawSchema from(String schema) {
        return builder().schema(schema).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        public String schema;

        public Builder schema(String schema) {
            this.schema = schema;
            return this;
        }

        public JsonRawSchema build() {
            return new JsonRawSchema(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JsonRawSchema that = (JsonRawSchema) o;
        return Objects.equals(this.schema, that.schema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema);
    }

    @Override
    public String toString() {
        return "JsonRawSchema {" + "schema = " + quoted(schema) + " }";
    }
}
