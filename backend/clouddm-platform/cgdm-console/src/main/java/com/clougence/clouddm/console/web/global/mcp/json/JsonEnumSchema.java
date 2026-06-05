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

import static com.clougence.clouddm.console.web.global.mcp.utils.RagUtils.ensureNotEmpty;
import static com.clougence.clouddm.console.web.global.mcp.utils.RagUtils.quoted;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class JsonEnumSchema implements JsonSchemaElement {

    private final String       description;

    private final List<String> enumValues;

    public JsonEnumSchema(Builder builder){
        this.description = builder.description;
        this.enumValues = new ArrayList<>(ensureNotEmpty(builder.enumValues, "enumValues"));
    }

    @Override
    public String description() {
        return description;
    }

    public List<String> enumValues() {
        return enumValues;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String       description;

        private List<String> enumValues;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder enumValues(List<String> enumValues) {
            this.enumValues = enumValues;
            return this;
        }

        public Builder enumValues(String... enumValues) {
            return enumValues(Arrays.asList(enumValues));
        }

        public JsonEnumSchema build() {
            return new JsonEnumSchema(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        JsonEnumSchema that = (JsonEnumSchema) o;
        return Objects.equals(this.description, that.description) && Objects.equals(this.enumValues, that.enumValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, enumValues);
    }

    @Override
    public String toString() {
        return "JsonEnumSchema {" + "description = " + quoted(description) + ", enumValues = " + enumValues + " }";
    }
}
