/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.mysql.analysis.reference;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.clougence.sql.common.registry.RegisteredResourceType;
import com.clougence.sql.mysql.parser.MySqlVersion;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

final class MySqlRegistryResourceLoader {

    private static final ObjectMapper JSON = new ObjectMapper();

    private MySqlRegistryResourceLoader(){
    }

    static List<Entry> load(Class<?> owner, String resource) {
        InputStream input = owner.getResourceAsStream(resource);
        if (input == null) {
            throw new IllegalStateException("Missing MySQL registered resources: " + resource);
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
            throw new IllegalStateException("Failed to load MySQL registered resources: " + resource, e);
        }
    }

    private static Entry parseEntry(String resource, int index, JsonNode entry) {
        String location = resource + "#entries[" + index + "]";
        if (entry == null || !entry.isObject()) {
            throw invalid(location, "entry must be an object");
        }
        RegisteredResourceType type = parseType(location, entry.get("type"));
        Set<MySqlVersion> versions = parseVersions(location, entry.get("versions"));
        List<String> nameParts = parseName(location, entry.get("name"));
        return new Entry(versions, type, nameParts);
    }

    private static RegisteredResourceType parseType(String location, JsonNode typeNode) {
        if (typeNode == null || !typeNode.isTextual()) {
            throw invalid(location, "type must be a string");
        }
        return switch (typeNode.textValue().toUpperCase(Locale.ROOT)) {
            case "TABLE", "VIEW" -> RegisteredResourceType.TABLE;
            case "FUNCTION" -> RegisteredResourceType.FUNCTION;
            case "PROCEDURE" -> RegisteredResourceType.PROCEDURE;
            default -> throw invalid(location, "unsupported type: " + typeNode.textValue());
        };
    }

    private static Set<MySqlVersion> parseVersions(String location, JsonNode versionsNode) {
        if (versionsNode == null || !versionsNode.isArray() || versionsNode.isEmpty()) {
            throw invalid(location, "versions must be a non-empty array");
        }
        EnumSet<MySqlVersion> versions = EnumSet.noneOf(MySqlVersion.class);
        for (JsonNode versionNode : versionsNode) {
            if (!versionNode.isTextual()) {
                throw invalid(location, "each version must be a string");
            }
            String version = versionNode.textValue();
            MySqlVersion parsed = MySqlVersion.parse(version);
            if (!parsed.versionString().equals(version) || !versions.add(parsed)) {
                throw invalid(location, "invalid or duplicate version: " + version);
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
        return new IllegalStateException("Invalid MySQL registered resource " + location + ": " + message);
    }

    record Entry(Set<MySqlVersion> versions, RegisteredResourceType type, List<String> nameParts) {
    }
}
