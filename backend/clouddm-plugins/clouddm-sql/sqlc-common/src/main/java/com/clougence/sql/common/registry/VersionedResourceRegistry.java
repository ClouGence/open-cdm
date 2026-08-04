/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.common.registry;

import java.util.*;

/**
 * Small dialect-neutral registry for versioned resource facts.
 *
 * <p>The registry owns no database data and performs no parser work. Dialect modules register
 * their own resources and choose the value carried by each entry.</p>
 */
public final class VersionedResourceRegistry<T> {

    private final ResourceRegistryDialect                   dialect;
    private final Map<ResourceKey, List<VersionedValue<T>>> resources = new LinkedHashMap<>();

    public VersionedResourceRegistry(ResourceRegistryDialect dialect){
        this.dialect = Objects.requireNonNull(dialect, "dialect");
    }

    public void register(RegisteredResourceType type, int minimumVersion, int maximumVersion, T value, String... nameParts) {
        if (minimumVersion > maximumVersion) {
            throw new IllegalArgumentException("minimumVersion must not exceed maximumVersion");
        }
        Objects.requireNonNull(value, "value");
        ResourceKey key = new ResourceKey(Objects.requireNonNull(type, "type"), normalize(nameParts));
        List<VersionedValue<T>> values = resources.computeIfAbsent(key, ignored -> new ArrayList<>());
        VersionedValue<T> entry = new VersionedValue<>(minimumVersion, maximumVersion, value);
        if (!values.contains(entry)) {
            values.add(entry);
        }
    }

    public Optional<T> find(RegisteredResourceType type, int exactVersion, String... nameParts) {
        ResourceKey key = new ResourceKey(Objects.requireNonNull(type, "type"), normalize(nameParts));
        T result = null;
        for (VersionedValue<T> entry : resources.getOrDefault(key, List.of())) {
            if (exactVersion < entry.minimumVersion || exactVersion > entry.maximumVersion) {
                continue;
            }
            if (result != null && !result.equals(entry.value)) {
                throw new IllegalStateException("Conflicting registered resource values for " + key.nameParts);
            }
            result = entry.value;
        }
        return Optional.ofNullable(result);
    }

    public boolean contains(RegisteredResourceType type, int exactVersion, String... nameParts) {
        return find(type, exactVersion, nameParts).isPresent();
    }

    public Map<String, T> registeredResources(RegisteredResourceType type, int exactVersion) {
        Map<String, T> result = new LinkedHashMap<>();
        for (ResourceKey key : resources.keySet()) {
            if (key.type != type) {
                continue;
            }
            find(type, exactVersion, key.nameParts.toArray(String[]::new)).ifPresent(value -> result.put(String.join(".", key.nameParts), value));
        }
        return Collections.unmodifiableMap(result);
    }

    private List<String> normalize(String... nameParts) {
        if (nameParts == null || nameParts.length == 0) {
            throw new IllegalArgumentException("resource name must not be empty");
        }
        List<String> normalized = Arrays.stream(nameParts).map(part -> {
            if (part == null || part.isBlank()) {
                throw new IllegalArgumentException("resource name part must not be blank");
            }
            String value = dialect.normalizeIdentifier(part);
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException("normalized resource name part must not be blank");
            }
            return value;
        }).toList();
        return List.copyOf(normalized);
    }

    private record ResourceKey(RegisteredResourceType type, List<String> nameParts) {
    }

    private record VersionedValue<T>(int minimumVersion, int maximumVersion, T value) {
    }
}
