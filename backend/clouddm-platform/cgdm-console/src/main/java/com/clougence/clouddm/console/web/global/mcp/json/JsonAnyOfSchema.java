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
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JsonAnyOfSchema implements JsonSchemaElement {

    private final String                  description;

    private final List<JsonSchemaElement> anyOf;

    public JsonAnyOfSchema(Builder builder){
        this.description = builder.description;
        this.anyOf = new ArrayList<>(ensureNotEmpty(builder.anyOf, "anyOf"));
    }

    @Override
    public String description() {
        return description;
    }

    public List<JsonSchemaElement> anyOf() {
        return anyOf;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String                  description;

        private List<JsonSchemaElement> anyOf;

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder anyOf(List<JsonSchemaElement> anyOf) {
            this.anyOf = anyOf;
            return this;
        }

        public Builder anyOf(JsonSchemaElement... anyOf) {
            return anyOf(asList(anyOf));
        }

        public JsonAnyOfSchema build() {
            return new JsonAnyOfSchema(this);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof JsonAnyOfSchema that)) {
            return false;
        }

        return Objects.equals(description, that.description) && Objects.equals(anyOf, that.anyOf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, anyOf);
    }

    @Override
    public String toString() {
        return "JsonAnyOfSchema {" + "description = " + quoted(description) + ", anyOf = " + anyOf + " }";
    }
}
