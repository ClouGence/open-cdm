/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.language.completion;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.*;

import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

final class VirtualMetaService implements MetaService {

    private static final ObjectMapper JSON    = new ObjectMapper();
    private final List<VirtualObject> objects = new ArrayList<>();

    VirtualMetaService(Path metaFile){
        try {
            JsonNode root = JSON.readTree(metaFile.toFile());
            for (JsonNode key : root.path("keynames")) {
                addObject(null, null, UmiTypes.Key, key.isTextual() ? key.asText() : key.path("name").asText(), key.path("columns"));
            }
            for (JsonNode schema : root.path("schemas")) {
                addObjects(null, schema.path("name").asText(null), schema.path("objects"));
            }
            for (JsonNode catalog : root.path("catalogs")) {
                String catalogName = catalog.path("name").asText(null);
                for (JsonNode schema : catalog.path("schemas")) {
                    addObjects(catalogName, schema.path("name").asText(null), schema.path("objects"));
                }
            }
            for (JsonNode object : root.path("objects")) {
                addObject(object.path("catalog").asText(null), object.path("schema").asText(null), UmiTypes.valueOfCode(object.path("type").asText()), object.path("name")
                    .asText(), object.path("columns"));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void addObjects(String catalog, String schema, JsonNode objectNodes) {
        for (JsonNode object : objectNodes) {
            addObject(catalog, schema, UmiTypes.valueOfCode(object.path("type").asText()), object.path("name").asText(), object.path("columns"));
        }
    }

    private void addObject(String catalog, String schema, UmiTypes type, String name, JsonNode columnNodes) {
        if (name == null || name.isBlank()) {
            return;
        }

        List<MetaCol> tableColumns = new ArrayList<>();
        for (JsonNode column : columnNodes) {
            MetaCol metaCol = new MetaCol();
            metaCol.setCatalog(catalog);
            metaCol.setSchema(schema);
            metaCol.setTable(name);
            metaCol.setColumn(column.path("name").asText());
            metaCol.setIcon(column.path("icon").asText(null));
            tableColumns.add(metaCol);
        }
        objects.add(new VirtualObject(catalog, schema, type, name, tableColumns));
    }

    @Override
    public List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String tableName) {
        if (tableName == null) {
            return List.of();
        }
        QualifiedName qualifiedName = QualifiedName.parse(tableName);
        return objects.stream()
            .filter(object -> object.sameName(qualifiedName.objectName()))
            .filter(object -> object.matchScope(levelsParam, qualifiedName.catalog(), qualifiedName.schema()))
            .findFirst()
            .map(VirtualObject::columns)
            .orElse(List.of());
    }

    @Override
    public List<MetaObj> cachedObjectNames(String puid, String uid, long dsId, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        return objects.stream()
            .filter(object -> levels == null || levels.isEmpty() || levels.contains(object.type()))
            .filter(object -> object.matchScope(levelsParam, null, null))
            .map(VirtualObject::toMetaObj)
            .toList();
    }

    private record VirtualObject(String catalog, String schema, UmiTypes type, String name, List<MetaCol> columns) {
        boolean sameName(String tableName) {
            return name.equalsIgnoreCase(tableName);
        }

        boolean matchScope(Map<UmiTypes, Object> levelsParam, String qualifiedCatalog, String qualifiedSchema) {
            return matchValue(catalog, qualifiedCatalog) && matchValue(schema, qualifiedSchema) && matchValue(catalog, levelValue(levelsParam, UmiTypes.Catalog))
                   && matchValue(schema, levelValue(levelsParam, UmiTypes.Schema));
        }

        MetaObj toMetaObj() {
            MetaObj metaObj = new MetaObj();
            metaObj.setType(type);
            metaObj.setName(name);
            return metaObj;
        }
    }

    private record QualifiedName(String catalog, String schema, String objectName) {
        static QualifiedName parse(String value) {
            String[] parts = value.split("\\.");
            if (parts.length >= 3) {
                return new QualifiedName(clean(parts[parts.length - 3]), clean(parts[parts.length - 2]), clean(parts[parts.length - 1]));
            }
            if (parts.length == 2) {
                return new QualifiedName(null, clean(parts[0]), clean(parts[1]));
            }
            return new QualifiedName(null, null, clean(value));
        }

        private static String clean(String value) {
            return value == null ? null : value.replace("\"", "").replace("`", "").replace("[", "").replace("]", "").trim();
        }
    }

    private static String levelValue(Map<UmiTypes, Object> levelsParam, UmiTypes type) {
        if (levelsParam == null || !levelsParam.containsKey(type)) {
            return null;
        }
        Object value = levelsParam.get(type);
        return value == null ? null : value.toString();
    }

    private static boolean matchValue(String actual, String expected) {
        if (expected == null || expected.isBlank()) {
            return true;
        }
        return Objects.equals(normalize(actual), normalize(expected));
    }

    private static String normalize(String value) {
        return value == null ? null : value.toLowerCase(Locale.ROOT);
    }
}
