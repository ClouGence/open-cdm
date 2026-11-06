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
package com.clougence.clouddm.ds.dameng.sql.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.clougence.clouddm.ds.dameng.sql.parser.DmVersion;
import com.clougence.sql.common.registry.RegisteredResourceType;
import com.clougence.sql.common.registry.ResourceRegistryDialect;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

final class DmRegistryResourceLoader {

    private static final ObjectMapper JSON = new ObjectMapper();

    private DmRegistryResourceLoader(){
    }

    static void loadBuiltInFunctions(Class<?> owner, String resource, VersionedResourceRegistry<Boolean> registry, ResourceRegistryDialect dialect) {
        Set<String> names = new HashSet<>();
        for (Entry entry : load(owner, resource)) {
            if (entry.type() != FUNCTION || entry.nameParts().size() > 3) {
                throw new IllegalStateException("Dameng function resource requires one to three FUNCTION name parts: " + entry);
            }

            String name = String.join(".", entry.nameParts());
            String key = entry.nameParts().stream().map(dialect::normalizeIdentifier).reduce((left, right) -> left + "." + right).orElseThrow();
            if (!names.add(key)) {
                throw new IllegalStateException("Duplicate Dameng function resource: " + name);
            }

            entry.versions().forEach(version -> registry.register(FUNCTION, version, version, true, entry.nameParts().toArray(String[]::new)));
        }
    }

    static void loadSystemResources(Class<?> owner, String resource, VersionedResourceRegistry<Boolean> registry, ResourceRegistryDialect dialect) {
        Set<String> names = new HashSet<>();
        for (Entry entry : load(owner, resource)) {
            if (entry.type() != PROCEDURE && entry.type() != TABLE && entry.type() != TYPE) {
                throw new IllegalStateException("Dameng system resource requires TABLE, PROCEDURE, or TYPE: " + entry);
            }

            if (entry.nameParts().size() > 3) {
                throw new IllegalStateException("Dameng system resource supports at most three name parts: " + entry);
            }

            String[] nameParts = entry.nameParts().toArray(String[]::new);
            String key = entry.type() + ":" + entry.nameParts().stream().map(dialect::normalizeIdentifier).reduce((left, right) -> left + "." + right).orElseThrow();
            if (!names.add(key)) {
                throw new IllegalStateException("Duplicate Dameng system resource: " + String.join(".", nameParts));
            }

            entry.versions().forEach(version -> registry.register(entry.type(), version, version, true, nameParts));
        }
    }

    private static List<Entry> load(Class<?> owner, String resource) {
        InputStream input = owner.getResourceAsStream(resource);
        if (input == null) {
            throw new IllegalStateException("Missing Dameng registered resources: " + resource);
        }
        try (input) {
            JsonNode root = JSON.readTree(input);
            JsonNode entries = root == null ? null : root.get("entries");
            if (root == null || !root.isObject() || entries == null || !entries.isArray()) {
                throw invalid(resource, "root.entries must be an array");
            }
            List<Entry> result = new ArrayList<>(entries.size());
            for (int i = 0; i < entries.size(); i++) {
                result.add(parseEntry(resource, i, entries.get(i)));
            }
            return List.copyOf(result);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load Dameng registered resources: " + resource, e);
        }
    }

    private static Entry parseEntry(String resource, int index, JsonNode entry) {
        String location = resource + "[" + index + "]";
        if (entry == null || !entry.isObject()) {
            throw invalid(location, "entry must be an object");
        }
        RegisteredResourceType type = parseType(location, entry.get("type"));
        Set<Integer> versions = parseVersions(location, entry.get("versions"));
        List<String> nameParts = parseName(location, entry.get("name"));
        return new Entry(versions, type, nameParts);
    }

    private static RegisteredResourceType parseType(String location, JsonNode typeNode) {
        if (typeNode == null || !typeNode.isTextual()) {
            throw invalid(location, "type must be a string");
        }
        try {
            return RegisteredResourceType.valueOf(typeNode.textValue().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw invalid(location, "unsupported type: " + typeNode.textValue());
        }
    }

    private static Set<Integer> parseVersions(String location, JsonNode versionsNode) {
        if (versionsNode == null || !versionsNode.isArray() || versionsNode.isEmpty()) {
            throw invalid(location, "versions must be a non-empty array");
        }
        Set<Integer> versions = new LinkedHashSet<>();
        for (JsonNode versionNode : versionsNode) {
            if (!versionNode.isTextual()) {
                throw invalid(location, "version must be a string");
            }

            DmVersion version = DmVersion.parse(versionNode.textValue());
            if (!version.versionString().equals(versionNode.textValue()) || !versions.add(Integer.parseInt(version.versionString()))) {
                throw invalid(location, "invalid or duplicate version: " + versionNode);
            }
        }
        return Set.copyOf(versions);
    }

    private static List<String> parseName(String location, JsonNode nameNode) {
        if (nameNode == null || !nameNode.isArray() || nameNode.isEmpty()) {
            throw invalid(location, "name must be a non-empty array");
        }
        List<String> nameParts = new ArrayList<>(nameNode.size());
        for (JsonNode partNode : nameNode) {
            if (!partNode.isTextual() || partNode.textValue().isBlank()) {
                throw invalid(location, "each name part must be a non-blank string");
            }
            nameParts.add(partNode.textValue());
        }
        return List.copyOf(nameParts);
    }

    private static IllegalStateException invalid(String location, String message) {
        return new IllegalStateException("Invalid Dameng registered resource " + location + ": " + message);
    }

    private record Entry(Set<Integer> versions, RegisteredResourceType type, List<String> nameParts) {
    }
}
