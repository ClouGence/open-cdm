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
package com.clougence.sql.common.registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;

/** Loads structured function facts from module-owned JSON catalogs. */
public final class BuiltinFunctionCatalogLoader {
    private static final ObjectMapper JSON = new ObjectMapper();

    private BuiltinFunctionCatalogLoader(){
    }

    public static List<BuiltinFunctionDefinition> load(Class<?> owner, String resource) {
        var input = owner.getResourceAsStream(resource);
        if (input == null) {
            throw new IllegalStateException("Missing built-in function catalog: " + resource);
        }

        try (input) {
            var entries = JSON.readTree(input).get("entries");
            if (entries == null || !entries.isArray()) {
                throw new IllegalStateException("Missing function entries: " + resource);
            }

            List<BuiltinFunctionDefinition> functions = new ArrayList<>();
            Set<String> names = new HashSet<>();
            for (var entry : entries) {
                var function = JSON.treeToValue(entry, BuiltinFunctionDefinition.class);
                if (function.nameParts() == null || function.nameParts().isEmpty() || function.nameParts().stream().anyMatch(String::isBlank) || !names.add(function.name())
                    || !"FUNCTION".equals(function.type()) || function.versions() == null || function.versions().stream().anyMatch(String::isBlank)) {
                    throw new IllegalStateException("Invalid function entry in " + resource + ": " + entry);
                }

                functions.add(function);
            }
            return List.copyOf(functions);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load built-in function catalog: " + resource, e);
        }
    }

}
