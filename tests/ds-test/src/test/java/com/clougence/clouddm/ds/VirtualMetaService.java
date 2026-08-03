/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VirtualMetaService implements MetaService {

    private static final ObjectMapper JSON    = new ObjectMapper();
    private final List<VirtualObject> objects = new ArrayList<>();

    public VirtualMetaService(){
        this(resource("_meta"));
    }

    public VirtualMetaService(Path metaRoot){
        try {
            loadLayer(metaRoot.resolve("1-layer"), 1);
            loadLayer(metaRoot.resolve("2-layer"), 2);
            loadLayer(metaRoot.resolve("3-layer"), 3);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadLayer(Path layerRoot, int layer) throws IOException {
        if (!Files.isDirectory(layerRoot)) {
            return;
        }
        try (var paths = Files.walk(layerRoot)) {
            paths.filter(path -> {
                return Files.isRegularFile(path) && path.getFileName().toString().endsWith(".json");
            }).sorted().forEach(path -> loadObject(layerRoot, path, layer));
        }
    }

    private void loadObject(Path layerRoot, Path file, int layer) {
        try {
            Path relative = layerRoot.relativize(file);
            String catalog = null;
            String schema = null;
            String name = leafName(file);
            if (layer == 2) {
                schema = relative.getName(0).toString();
            } else if (layer == 3) {
                catalog = relative.getName(0).toString();
                schema = relative.getName(1).toString();
            }

            JsonNode columns = JSON.readTree(file.toFile());
            if (!columns.isArray()) {
                throw new IllegalStateException("Meta file must use column array format: " + file);
            }
            objects.add(new VirtualObject(catalog, schema, name, columns(columns, catalog, schema, name)));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static List<MetaCol> columns(JsonNode columnNodes, String catalog, String schema, String table) {
        List<MetaCol> columns = new ArrayList<>();
        for (JsonNode columnNode : columnNodes) {
            MetaCol column = new MetaCol();
            column.setCatalog(catalog);
            column.setSchema(schema);
            column.setTable(table);
            column.setColumn(columnName(columnNode));
            column.setIcon(columnNode.path("icon").asText(null));
            columns.add(column);
        }
        return columns;
    }

    @Override
    public List<MetaCol> fetchTableColumns(String uid, long dsId, Map<UmiTypes, Object> levelsParam, String tableName) {
        if (tableName == null) {
            throw new IllegalStateException("Column test meta not found: tableName=null");
        }
        QualifiedName qualifiedName = QualifiedName.parse(tableName);
        return objects.stream()
            .filter(object -> object.sameName(qualifiedName.objectName()))
            .filter(object -> object.matchScope(levelsParam, qualifiedName.catalog(), qualifiedName.schema()))
            .findFirst()
            .map(VirtualObject::columns)
            .orElseThrow(() -> new IllegalStateException(
                "Column test meta not found: catalog=" + qualifiedName.catalog() + ", schema=" + qualifiedName.schema() + ", tableName=" + tableName));
    }

    @Override
    public List<MetaObj> cachedObjectNames(String puid, String uid, long dsId, List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) {
        return objects.stream()
            .filter(object -> levels == null || levels.isEmpty() || levels.contains(object.type()))
            .filter(object -> object.matchScope(levelsParam, null, null))
            .map(VirtualObject::toMetaObj)
            .toList();
    }

    private record VirtualObject(String catalog, String schema, String name, List<MetaCol> columns) {
        boolean sameName(String tableName) {
            return name.equalsIgnoreCase(tableName);
        }

        UmiTypes type() {
            if (!columns.isEmpty()) {
                return UmiTypes.Table;
            }
            if (schema == null && catalog == null) {
                return UmiTypes.Key;
            }
            return UmiTypes.Function;
        }

        boolean matchScope(Map<UmiTypes, Object> levelsParam, String qualifiedCatalog, String qualifiedSchema) {
            return matchValue(catalog, qualifiedCatalog) && matchValue(schema, qualifiedSchema) && matchValue(catalog, levelValue(levelsParam, UmiTypes.Catalog))
                   && matchValue(schema, levelValue(levelsParam, UmiTypes.Schema));
        }

        MetaObj toMetaObj() {
            MetaObj metaObj = new MetaObj();
            metaObj.setType(type());
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

    private static String columnName(JsonNode column) {
        if (column.hasNonNull("name")) {
            return column.path("name").asText();
        }
        if (column.hasNonNull("column")) {
            return column.path("column").asText();
        }
        JsonNode nestedColumns = column.path("columns");
        if (nestedColumns.isArray() && !nestedColumns.isEmpty()) {
            return nestedColumns.get(0).path("column").asText();
        }
        return null;
    }

    private static String leafName(Path file) {
        String filename = file.getFileName().toString();
        if (filename.endsWith(".json")) {
            return filename.substring(0, filename.length() - ".json".length());
        }
        return filename;
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

    private static Path resource(String name) {
        try {
            return Path.of(Thread.currentThread().getContextClassLoader().getResource(name).toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }
}
