/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.hana.sql.analysis.sysobj;

import java.util.*;

import com.clougence.clouddm.ds.hana.sql.parser.HanaVersion;
import com.clougence.sql.common.registry.DatabaseResource;
import com.clougence.sql.common.registry.DatabaseResourceXmlLoader;
import com.clougence.sql.common.registry.RegisteredResourceType;

/** HANA-owned versioned database resource facts. */
public final class HanaResourceRegistry {

    private static final String                            RESOURCE  = "/META-INF/clougence/hana-database-resources.xml";
    private static final HanaResourceRegistry              INSTANCE  = new HanaResourceRegistry();

    private final Map<HanaVersion, List<DatabaseResource>> resources = new EnumMap<>(HanaVersion.class);

    private HanaResourceRegistry(){
        for (HanaVersion version : HanaVersion.values()) {
            resources.put(version, new ArrayList<>());
        }
        for (DatabaseResource entry : DatabaseResourceXmlLoader.load(HanaResourceRegistry.class, RESOURCE)) {
            for (HanaVersion version : versions(entry)) {
                resources.get(version).add(entry);
            }
        }
        resources.replaceAll((version, entries) -> List.copyOf(entries));
    }

    public static HanaResourceRegistry instance() {
        return INSTANCE;
    }

    public boolean isSystemFunction(String name, HanaVersion version) {
        return contains(RegisteredResourceType.FUNCTION, splitQualifiedName(name), version, false);
    }

    public boolean isSystemFunction(List<String> name, HanaVersion version) {
        return contains(RegisteredResourceType.FUNCTION, name, version, false);
    }

    public boolean isAggregateFunction(String name, HanaVersion version) {
        return contains(RegisteredResourceType.FUNCTION, splitQualifiedName(name), version, true);
    }

    public boolean isSystemView(String schema, String name, HanaVersion version) {
        List<String> qualifiedName = new ArrayList<>(2);
        if (schema != null && !schema.isBlank()) {
            qualifiedName.add(schema);
        }
        qualifiedName.add(name);
        return contains(RegisteredResourceType.TABLE, qualifiedName, version, false);
    }

    public boolean isSystemView(List<String> name, HanaVersion version) {
        return contains(RegisteredResourceType.TABLE, name, version, false);
    }

    public boolean isSystemProcedure(List<String> name, HanaVersion version) {
        return contains(RegisteredResourceType.PROCEDURE, name, version, false);
    }

    public boolean shouldSkipPermissionCheck(RegisteredResourceType type, List<String> name, HanaVersion version) {
        for (DatabaseResource entry : values(version)) {
            if (entry.type() == type && entry.skipPermission() && matches(entry, name)) {
                return true;
            }
        }
        return false;
    }

    private boolean contains(RegisteredResourceType type, List<String> name, HanaVersion version, boolean aggregateOnly) {
        for (DatabaseResource entry : values(version)) {
            if (entry.type() == type && (!aggregateOnly || entry.aggregate()) && matches(entry, name)) {
                return true;
            }
        }
        return false;
    }

    private List<DatabaseResource> values(HanaVersion version) {
        return resources.get(version == null ? HanaVersion.LATEST : version);
    }

    private static boolean matches(DatabaseResource entry, List<String> inputName) {
        List<String> normalizedInput = inputName.stream().map(HanaResourceRegistry::normalize).toList();
        String objectName = normalizedInput.isEmpty() ? "" : normalizedInput.get(normalizedInput.size() - 1);
        if (!normalize(entry.name()).equals(objectName)) {
            return false;
        }

        String schema = normalizedInput.size() < 2 ? "" : normalizedInput.get(normalizedInput.size() - 2);
        if (!matchesPattern(schema, normalize(entry.schema()), "*".equals(entry.schema()))) {
            return false;
        }
        String catalog = normalizedInput.size() < 3 ? "" : normalizedInput.get(normalizedInput.size() - 3);
        return matchesPattern(catalog, normalize(entry.catalog()), "*".equals(entry.catalog()));
    }

    private static boolean matchesPattern(String value, String pattern, boolean unrestricted) {
        if (unrestricted) {
            return true;
        }
        int wildcard = pattern.indexOf('*');
        if (wildcard < 0) {
            return value.equals(pattern);
        }
        if (pattern.indexOf('*', wildcard + 1) >= 0) {
            throw new IllegalStateException("HANA resource pattern supports one wildcard: " + pattern);
        }
        return value.startsWith(pattern.substring(0, wildcard)) && value.endsWith(pattern.substring(wildcard + 1));
    }

    private static Set<HanaVersion> versions(DatabaseResource entry) {
        EnumSet<HanaVersion> versions = EnumSet.noneOf(HanaVersion.class);
        for (String value : entry.versions()) {
            HanaVersion version = switch (value) {
                case "1" -> HanaVersion.HANA_1;
                case "2" -> HanaVersion.HANA_2;
                default -> throw new IllegalStateException("Unsupported HANA parser version in " + RESOURCE + ": " + value);
            };
            versions.add(version);
        }
        return versions;
    }

    private static List<String> splitQualifiedName(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        List<String> result = new ArrayList<>();
        StringBuilder part = new StringBuilder();
        boolean quoted = false;
        for (int index = 0; index < value.length(); index++) {
            char current = value.charAt(index);
            if (current == '"') {
                if (quoted && index + 1 < value.length() && value.charAt(index + 1) == '"') {
                    part.append('"');
                    index++;
                } else {
                    quoted = !quoted;
                }
            } else if ((current == '.' || current == ':') && !quoted) {
                result.add(part.toString());
                part.setLength(0);
            } else {
                part.append(current);
            }
        }
        result.add(part.toString());
        return result;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.strip().toUpperCase(Locale.ROOT);
    }
}
