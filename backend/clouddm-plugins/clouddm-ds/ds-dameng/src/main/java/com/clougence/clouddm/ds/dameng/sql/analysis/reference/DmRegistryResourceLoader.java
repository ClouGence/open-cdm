/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.dameng.sql.analysis.reference;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.clougence.sql.common.registry.RegisteredResourceType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

final class DmRegistryResourceLoader {

    private static final ObjectMapper JSON = new ObjectMapper();

    private DmRegistryResourceLoader(){
    }

    static List<Entry> load(Class<?> owner, String resource) {
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
        String location = resource + "#entries[" + index + "]";
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
            if (!versionNode.isTextual() || !"8".equals(versionNode.textValue()) || !versions.add(DmResourceRegistry.DM8)) {
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

    record Entry(Set<Integer> versions, RegisteredResourceType type, List<String> nameParts) {
    }
}
