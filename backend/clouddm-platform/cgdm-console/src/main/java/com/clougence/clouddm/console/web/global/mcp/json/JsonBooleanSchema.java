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

import static com.clougence.clouddm.console.web.global.mcp.utils.RagUtils.quoted;

import java.util.Objects;

public class JsonBooleanSchema implements JsonSchemaElement {

    private final String description;

    public JsonBooleanSchema(){
        this.description = null;
    }

    public JsonBooleanSchema(Builder builder){
        this.description = builder.description;
    }

    @Override
    public String description() {
        return description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String description;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public JsonBooleanSchema build() {
            return new JsonBooleanSchema(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        JsonBooleanSchema that = (JsonBooleanSchema) o;
        return Objects.equals(this.description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public String toString() {
        return "JsonBooleanSchema {" + "description = " + quoted(description) + " }";
    }
}
