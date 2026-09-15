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
package com.clougence.sql.postgres.analysis.reference;

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.registry.VersionedResourceRegistry;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Loads PostgreSQL resource facts from module-owned JSON catalogs. */
final class PgRegistryResourceLoader {

    private static final ObjectMapper JSON = new ObjectMapper();

    private PgRegistryResourceLoader(){
    }

    static void loadBuiltInFunctions(Class<?> owner, String resource, VersionedResourceRegistry<Boolean> registry) {
        JsonNode root = loadRoot(owner, resource);
        JsonNode entries = requiredArray(resource, root, "entries");
        Set<String> names = new HashSet<>();
        for (int i = 0; i < entries.size(); i++) {
            String location = resource + "#entries[" + i + "]";
            JsonNode entry = entries.get(i);
            if (entry == null || !entry.isObject()) {
                throw invalid(location, "entry must be an object");
            }
            String type = requiredText(location, entry, "type");
            if (!FUNCTION.name().equals(type.toUpperCase(Locale.ROOT))) {
                throw invalid(location, "type must be FUNCTION");
            }
            List<String> nameParts = parseNames(location, requiredArray(location, entry, "name"));
            if (nameParts.size() != 1 || !names.add(nameParts.get(0))) {
                throw invalid(location, "name must contain one unique function name");
            }
            register(registry, parseVersions(location, requiredArray(location, entry, "versions")), true, nameParts);
        }
    }

    static void loadAnalysisResources(Class<?> owner, String resource, VersionedResourceRegistry<Boolean> metadataFunctions,
                                      VersionedResourceRegistry<Set<SplitQueryType>> functionStatementTypes,
                                      VersionedResourceRegistry<PgFunctionBehavior> functionBehaviors,
                                      Set<String> performanceRelations, Set<String> systemSchemas) {
        JsonNode root = loadRoot(owner, resource);
        loadMetadataFunctions(resource, root, metadataFunctions);
        loadFunctionStatementTypes(resource, root, functionStatementTypes);
        loadFunctionBehaviors(resource, root, functionBehaviors);
        performanceRelations.addAll(parseNames(resource + "#performanceRelations",
            requiredArray(resource, root, "performanceRelations")));
        systemSchemas.addAll(parseNames(resource + "#systemSchemas", requiredArray(resource, root, "systemSchemas")));
    }

    private static void loadMetadataFunctions(String resource, JsonNode root, VersionedResourceRegistry<Boolean> registry) {
        JsonNode groups = requiredArray(resource, root, "metadataFunctions");
        for (int i = 0; i < groups.size(); i++) {
            String location = resource + "#metadataFunctions[" + i + "]";
            JsonNode group = groups.get(i);
            register(registry, parseVersions(location, requiredArray(location, group, "versions")), true,
                parseNames(location, requiredArray(location, group, "names")));
        }
    }

    private static void loadFunctionStatementTypes(String resource, JsonNode root,
                                                   VersionedResourceRegistry<Set<SplitQueryType>> registry) {
        JsonNode groups = requiredArray(resource, root, "functionStatementTypes");
        for (int i = 0; i < groups.size(); i++) {
            String location = resource + "#functionStatementTypes[" + i + "]";
            JsonNode group = groups.get(i);
            Set<SplitQueryType> types = parseStatementTypes(location, requiredArray(location, group, "types"));
            register(registry, parseVersions(location, requiredArray(location, group, "versions")), types,
                parseNames(location, requiredArray(location, group, "names")));
        }
    }

    private static void loadFunctionBehaviors(String resource, JsonNode root,
                                              VersionedResourceRegistry<PgFunctionBehavior> registry) {
        JsonNode groups = requiredArray(resource, root, "functionBehaviors");
        for (int i = 0; i < groups.size(); i++) {
            String location = resource + "#functionBehaviors[" + i + "]";
            JsonNode group = groups.get(i);
            BehaviorAction action = parseEnum(location, "action", requiredText(location, group, "action"), BehaviorAction.class);
            TargetType targetType = optionalEnum(location, "targetType", group.get("targetType"), TargetType.class);
            BehaviorAction targetAction = optionalEnum(location, "targetAction", group.get("targetAction"), BehaviorAction.class);
            JsonNode unsafeNode = group.get("unsafe");
            if (unsafeNode == null || !unsafeNode.isBoolean()) {
                throw invalid(location, "unsafe must be a boolean");
            }
            PgFunctionBehavior behavior = new PgFunctionBehavior(action, targetType, targetAction, unsafeNode.booleanValue());
            register(registry, parseVersions(location, requiredArray(location, group, "versions")), behavior,
                parseNames(location, requiredArray(location, group, "names")));
        }
    }

    private static Set<PostgresVersion> parseVersions(String location, JsonNode values) {
        EnumSet<PostgresVersion> versions = EnumSet.noneOf(PostgresVersion.class);
        for (JsonNode value : values) {
            if (!value.isTextual()) {
                throw invalid(location, "each version must be a string");
            }
            String version = value.textValue();
            PostgresVersion parsed = PostgresVersion.parse(version);
            if (!parsed.versionString().equals(version) || !versions.add(parsed)) {
                throw invalid(location, "invalid or duplicate version: " + version);
            }
        }
        return Set.copyOf(versions);
    }

    private static Set<SplitQueryType> parseStatementTypes(String location, JsonNode values) {
        Set<SplitQueryType> types = new LinkedHashSet<>();
        for (JsonNode value : values) {
            if (!value.isTextual()) {
                throw invalid(location, "each statement type must be a string");
            }
            SplitQueryType type = parseEnum(location, "types", value.textValue(), SplitQueryType.class);
            if (!types.add(type)) {
                throw invalid(location, "duplicate statement type: " + value.textValue());
            }
        }
        return Collections.unmodifiableSet(types);
    }

    private static List<String> parseNames(String location, JsonNode values) {
        List<String> names = new ArrayList<>(values.size());
        for (JsonNode value : values) {
            if (!value.isTextual() || value.textValue().isBlank()) {
                throw invalid(location, "each name must be a non-blank string");
            }
            names.add(value.textValue());
        }
        return List.copyOf(names);
    }

    private static <T> void register(VersionedResourceRegistry<T> registry, Set<PostgresVersion> versions, T value,
                                     List<String> names) {
        for (PostgresVersion version : versions) {
            int versionCode = Integer.parseInt(version.versionString());
            for (String name : names) {
                registry.register(FUNCTION, versionCode, versionCode, value, name);
            }
        }
    }

    private static JsonNode loadRoot(Class<?> owner, String resource) {
        InputStream input = owner.getResourceAsStream(resource);
        if (input == null) {
            throw new IllegalStateException("Missing PostgreSQL registered resources: " + resource);
        }
        try (input) {
            JsonNode root = JSON.readTree(input);
            if (root == null || !root.isObject()) {
                throw invalid(resource, "root must be an object");
            }
            return root;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load PostgreSQL registered resources: " + resource, e);
        }
    }

    private static JsonNode requiredArray(String location, JsonNode parent, String field) {
        JsonNode value = parent == null ? null : parent.get(field);
        if (value == null || !value.isArray() || value.isEmpty()) {
            throw invalid(location, field + " must be a non-empty array");
        }
        return value;
    }

    private static String requiredText(String location, JsonNode parent, String field) {
        JsonNode value = parent.get(field);
        if (value == null || !value.isTextual() || value.textValue().isBlank()) {
            throw invalid(location, field + " must be a non-blank string");
        }
        return value.textValue();
    }

    private static <T extends Enum<T>> T optionalEnum(String location, String field, JsonNode value, Class<T> type) {
        if (value == null || value.isNull()) {
            return null;
        }
        if (!value.isTextual()) {
            throw invalid(location, field + " must be a string or null");
        }
        return parseEnum(location, field, value.textValue(), type);
    }

    private static <T extends Enum<T>> T parseEnum(String location, String field, String value, Class<T> type) {
        try {
            return Enum.valueOf(type, value);
        } catch (IllegalArgumentException e) {
            throw invalid(location, "unsupported " + field + ": " + value);
        }
    }

    private static IllegalStateException invalid(String location, String message) {
        return new IllegalStateException("Invalid PostgreSQL registered resource " + location + ": " + message);
    }
}
