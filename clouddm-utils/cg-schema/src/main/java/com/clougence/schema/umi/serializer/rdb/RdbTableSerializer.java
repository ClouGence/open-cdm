package com.clougence.schema.umi.serializer.rdb;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.serializer.ConstraintUmiDataSerializer;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class RdbTableSerializer extends ConstraintUmiDataSerializer<RdbTable> {

    private static final RdbColumnSerializer     columnSerializer     = new RdbColumnSerializer();
    private static final RdbPrimaryKeySerializer primaryKeySerializer = new RdbPrimaryKeySerializer();
    private static final RdbUniqueKeySerializer  uniqueKeySerializer  = new RdbUniqueKeySerializer();
    private static final RdbForeignKeySerializer foreignKeySerializer = new RdbForeignKeySerializer();
    private static final RdbIndexSerializer      indicesSerializer    = new RdbIndexSerializer();

    public void readData(Map<String, Object> jsonMap, RdbTable readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        readTo.setCatalog((String) jsonMap.get(KEY_RDB_CATALOG));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setName((String) jsonMap.get(KEY_NAME));
        readTo.setAlias((String) jsonMap.get(KEY_ALIAS));
        readTo.setComment((String) jsonMap.get(KEY_COMMENT));
        readTo.setUmiType(UmiTypes.valueOfCode((String) jsonMap.get(KEY_UMI_TYPE)));

        readTo.setTableType((String) jsonMap.get(KEY_TYPE));
        readTo.setColumns(readColumns((List<Map<String, Object>>) jsonMap.get(KEY_RDB_COLUMN_LIST)));
        readTo.setPrimaryKey(readPrimaryKey((Map<String, Object>) jsonMap.get(KEY_RDB_PRIMARY_KEY)));
        readTo.setUniqueKeys(readUniqueKey((List<Map<String, Object>>) jsonMap.get(KEY_RDB_UNIQUE_KEY)));
        readTo.setForeignKeys(readForeignKey((List<Map<String, Object>>) jsonMap.get(KEY_RDB_FOREIGN_KEY)));
        readTo.setIndices(readIndices((List<Map<String, Object>>) jsonMap.get(KEY_RDB_INDEX)));
    }

    @Override
    public void writeToMap(RdbTable table, Map<String, Object> toMap) {
        super.writeToMap(table, toMap);
        if (table == null) {
            return;
        }

        if (StringUtils.isNotBlank(table.getCatalog())) {
            toMap.put(KEY_RDB_CATALOG, table.getCatalog());
        }
        if (StringUtils.isNotBlank(table.getSchema())) {
            toMap.put(KEY_RDB_SCHEMA, table.getSchema());
        }
        if (StringUtils.isNotBlank(table.getName())) {
            toMap.put(KEY_NAME, table.getName());
        }
        if (StringUtils.isNotBlank(table.getAlias())) {
            toMap.put(KEY_ALIAS, table.getAlias());
        }
        if (StringUtils.isNotBlank(table.getComment())) {
            toMap.put(KEY_COMMENT, table.getComment());
        }
        if (table.getUmiType() != null) {
            toMap.put(KEY_UMI_TYPE, table.getUmiType().getTypeName());
        }
        if (StringUtils.isNotBlank(table.getTableType())) {
            toMap.put(KEY_TYPE, table.getTableType());
        }
        if (table.getColumns() != null) {
            toMap.put(KEY_RDB_COLUMN_LIST, writeColumns(table.getColumns()));
        }
        if (table.getPrimaryKey() != null) {
            toMap.put(KEY_RDB_PRIMARY_KEY, writePrimaryKey(table.getPrimaryKey()));
        }
        if (table.getUniqueKeys() != null) {
            toMap.put(KEY_RDB_UNIQUE_KEY, writeUniqueKey(table.getUniqueKeys()));
        }
        if (table.getForeignKeys() != null) {
            toMap.put(KEY_RDB_FOREIGN_KEY, writeForeignKey(table.getForeignKeys()));
        }
        if (table.getIndices() != null) {
            toMap.put(KEY_RDB_INDEX, writeIndices(table.getIndices()));
        }
    }

    private Map<String, RdbColumn> readColumns(List<Map<String, Object>> jsonMaps) {
        if (jsonMaps == null) {
            return null;
        }

        Map<String, RdbColumn> columnMap = new LinkedHashMap<>();
        for (Map<String, Object> map : jsonMaps) {
            String aClassName = (String) map.get(KEY_CLASS);
            if (StringUtils.isNotBlank(aClassName)) {
                RdbColumn column = new RdbColumn();
                columnSerializer.readData(map, column);
                columnMap.put(column.getName(), column);
            }
        }

        return columnMap;
    }

    private RdbPrimaryKey readPrimaryKey(Map<String, Object> jsonMap) {
        if (jsonMap == null) {
            return null;
        }

        RdbPrimaryKey primaryKey = new RdbPrimaryKey();
        primaryKeySerializer.readData(jsonMap, primaryKey);
        return primaryKey;
    }

    private List<RdbUniqueKey> readUniqueKey(List<Map<String, Object>> jsonMaps) {
        if (jsonMaps != null) {
            List<RdbUniqueKey> uniqueKeys = new ArrayList<>();
            for (Map<String, Object> jsonMap : jsonMaps) {
                RdbUniqueKey uniqueKey = new RdbUniqueKey();
                uniqueKeySerializer.readData(jsonMap, uniqueKey);
                uniqueKeys.add(uniqueKey);
            }
            return uniqueKeys;
        }
        return null;
    }

    private List<RdbForeignKey> readForeignKey(List<Map<String, Object>> jsonMaps) {
        if (jsonMaps != null) {
            List<RdbForeignKey> foreignKeys = new ArrayList<>();
            for (Map<String, Object> jsonMap : jsonMaps) {
                RdbForeignKey foreignKey = new RdbForeignKey();
                foreignKeySerializer.readData(jsonMap, foreignKey);
                foreignKeys.add(foreignKey);
            }
            return foreignKeys;
        }
        return null;
    }

    private List<RdbIndex> readIndices(List<Map<String, Object>> jsonMaps) {
        if (jsonMaps != null) {
            List<RdbIndex> indexList = new ArrayList<>();
            for (Map<String, Object> jsonMap : jsonMaps) {
                RdbIndex rdbIndex = new RdbIndex();
                indicesSerializer.readData(jsonMap, rdbIndex);
                indexList.add(rdbIndex);
            }
            return indexList;
        }
        return null;
    }

    private List<Map<String, Object>> writeColumns(Map<String, RdbColumn> columnMap) {
        if (columnMap == null) {
            return null;
        }

        List<Map<String, Object>> jsonMaps = new ArrayList<>();
        columnMap.forEach((col, rdbColumn) -> {
            Map<String, Object> colData = new LinkedHashMap<>();
            columnSerializer.writeToMap(rdbColumn, colData);
            jsonMaps.add(colData);
        });

        return jsonMaps;
    }

    private Map<String, Object> writePrimaryKey(RdbPrimaryKey primaryKey) {
        if (primaryKey == null) {
            return null;
        }

        Map<String, Object> jsonMap = new LinkedHashMap<>();
        primaryKeySerializer.writeToMap(primaryKey, jsonMap);
        return jsonMap;
    }

    private List<Map<String, Object>> writeUniqueKey(List<RdbUniqueKey> uniqueKeys) {
        if (uniqueKeys != null) {
            List<Map<String, Object>> jsonMaps = new ArrayList<>();
            for (RdbUniqueKey uniqueKey : uniqueKeys) {
                Map<String, Object> jsonMap = new LinkedHashMap<>();
                uniqueKeySerializer.writeToMap(uniqueKey, jsonMap);
                jsonMaps.add(jsonMap);
            }
            return jsonMaps;
        }
        return null;
    }

    private List<Map<String, Object>> writeForeignKey(List<RdbForeignKey> foreignKeys) {
        if (foreignKeys != null) {
            List<Map<String, Object>> jsonMaps = new ArrayList<>();
            for (RdbForeignKey foreignKey : foreignKeys) {
                Map<String, Object> jsonMap = new LinkedHashMap<>();
                foreignKeySerializer.writeToMap(foreignKey, jsonMap);
                jsonMaps.add(jsonMap);
            }
            return jsonMaps;
        }
        return null;
    }

    private List<Map<String, Object>> writeIndices(List<RdbIndex> indices) {
        if (indices != null) {
            List<Map<String, Object>> jsonMaps = new ArrayList<>();
            for (RdbIndex index : indices) {
                Map<String, Object> jsonMap = new LinkedHashMap<>();
                indicesSerializer.writeToMap(index, jsonMap);
                jsonMaps.add(jsonMap);
            }
            return jsonMaps;
        }
        return null;
    }
}
