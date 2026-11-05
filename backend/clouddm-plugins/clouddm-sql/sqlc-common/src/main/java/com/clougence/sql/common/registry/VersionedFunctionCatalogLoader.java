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

import static com.clougence.sql.common.registry.RegisteredResourceType.FUNCTION;

import java.io.IOException;
import java.util.*;
import java.util.function.ToIntFunction;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Register named-release JSON facts in the existing versioned resource registry. */
public final class VersionedFunctionCatalogLoader {
    private static final ObjectMapper JSON = new ObjectMapper();

    private VersionedFunctionCatalogLoader(){
    }

    public static void load(Class<?> owner, String resource, VersionedResourceRegistry<Boolean> registry, ToIntFunction<String> versionCode) {
        for (var function : BuiltinFunctionCatalogLoader.load(owner, resource)) {
            for (String version : function.versions()) {
                int code = versionCode.applyAsInt(version);
                registry.register(FUNCTION, code, code, true, function.name());
            }
        }
    }

    public static void loadAnalysisResources(Class<?> owner, String resource, VersionedResourceRegistry<FunctionBehavior> registry, ToIntFunction<String> versionCode) {
        var input = owner.getResourceAsStream(resource);
        if (input == null) {
            throw new IllegalStateException("Missing function analysis resource: " + resource);
        }

        try (input) {
            JsonNode root = JSON.readTree(input);
            Map<Integer, Map<String, FunctionBehavior>> releases = new LinkedHashMap<>();
            for (String field : List.of("metadataFunctions", "privilegedFunctions", "functionStatementTypes", "functionBehaviors")) {
                JsonNode groups = root.path(field);
                if (!groups.isArray()) {
                    throw new IllegalStateException("Invalid function analysis groups: " + resource + "#" + field);
                }

                for (JsonNode group : groups) {
                    List<Integer> versions = new ArrayList<>();
                    if (group.has("versions")) {
                        if (!group.path("versions").isArray()) {
                            throw new IllegalStateException("Invalid function analysis versions: " + resource);
                        }

                        group.path("versions").forEach(version -> versions.add(versionCode.applyAsInt(version.asText())));
                    } else {
                        // Cloud services can expose version-independent behavior without a server release.
                        versions.add(versionCode.applyAsInt(null));
                    }

                    if (!group.path("names").isArray() || group.path("names").isEmpty()) {
                        throw new IllegalStateException("Missing function analysis names: " + resource);
                    }

                    for (int version : versions) {
                        Map<String, FunctionBehavior> functions = releases.computeIfAbsent(version, ignored -> new LinkedHashMap<>());
                        for (JsonNode name : group.path("names")) {
                            if (!name.isTextual() || name.asText().isBlank()) {
                                throw new IllegalStateException("Invalid function analysis name: " + resource);
                            }

                            FunctionBehavior previous = functions.getOrDefault(name.asText(), FunctionBehavior.DEFAULT);
                            functions.put(name.asText(), mergeAnalysis(previous, group, field));
                        }
                    }
                }
            }
            releases.forEach((version, functions) -> functions.forEach((name, behavior) -> registry.register(FUNCTION, version, version, behavior, name)));
        } catch (IOException | IllegalArgumentException e) {
            throw new IllegalStateException("Failed to load function analysis resources: " + resource, e);
        }
    }

    private static FunctionBehavior mergeAnalysis(FunctionBehavior previous, JsonNode group, String field) {
        Set<SplitQueryType> types = new LinkedHashSet<>(previous.statementTypes());
        for (JsonNode type : group.path("types")) {
            types.add(SplitQueryType.valueOf(type.asText()));
        }

        boolean metadata = previous.metadata() || "metadataFunctions".equals(field);
        if (metadata) {
            types.add(SplitQueryType.METADATA);
        }

        BehaviorAction action = previous.action();
        TargetType targetType = previous.targetType();
        BehaviorAction targetAction = previous.targetAction();
        if (group.has("action")) {
            action = BehaviorAction.valueOf(group.path("action").asText());
        }

        if (group.hasNonNull("targetType")) {
            targetType = TargetType.valueOf(group.path("targetType").asText());
        }

        if (group.hasNonNull("targetAction")) {
            targetAction = BehaviorAction.valueOf(group.path("targetAction").asText());
        }

        boolean unsafe = previous.unsafe() || group.path("unsafe").asBoolean(false);
        if (unsafe) {
            types.add(SplitQueryType.UNSAFE);
        }

        boolean privileged = previous.privileged() || "privilegedFunctions".equals(field);
        return new FunctionBehavior(types, action, targetType, targetAction, unsafe, metadata, privileged);
    }

}
