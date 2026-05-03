package com.clougence.schema.umi.serializer.rdb;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.clougence.schema.DsType;
import com.clougence.schema.SchemaFramework;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.umi.serializer.ConstraintUmiDataSerializer;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class RdbColumnSerializer extends ConstraintUmiDataSerializer<RdbColumn> {

    private final static Map<String, DsType> cacheDsType = new ConcurrentHashMap<>();

    public void readData(Map<String, Object> jsonMap, RdbColumn readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        readTo.setCatalog((String) jsonMap.get(KEY_RDB_CATALOG));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setTable((String) jsonMap.get(KEY_RDB_TABLE));
        readTo.setName((String) jsonMap.get(KEY_NAME));
        readTo.setComment((String) jsonMap.get(KEY_COMMENT));
        readTo.setUmiType(UmiTypes.valueOfCode((String) jsonMap.get(KEY_UMI_TYPE)));

        readTo.setSqlType(readFieldType((String) jsonMap.get(KEY_TYPE)));
        readTo.setIndex((Integer) jsonMap.get(KEY_RDB_ORDER));
        if (jsonMap.get(KEY_RDB_CHAR_LENGTH) != null) {
            readTo.setCharLength(((Number) jsonMap.get(KEY_RDB_CHAR_LENGTH)).longValue());
        }
        if (jsonMap.get(KEY_RDB_BYTE_LENGTH) != null) {
            readTo.setByteLength(((Number) jsonMap.get(KEY_RDB_BYTE_LENGTH)).longValue());
        }
        readTo.setNumericPrecision((Integer) jsonMap.get(KEY_RDB_NUMERIC_PRECISION));
        readTo.setNumericScale((Integer) jsonMap.get(KEY_RDB_NUMERIC_SCALE));
        readTo.setNumericUnsigned((Boolean) jsonMap.get(KEY_RDB_NUMERIC_UNSIGNED));
        readTo.setDatetimePrecision((Integer) jsonMap.get(KEY_RDB_DATETIME_PRECISION));
        readTo.setDefaultValue((String) jsonMap.get(KEY_RDB_DEFAULT));
        readTo.setDefaultValueIsFunc((Boolean) jsonMap.get(KEY_RDB_DEFAULT_T));
    }

    private FieldType readFieldType(String sqlType) {
        if (StringUtils.isBlank(sqlType)) {
            return null;
        }

        String[] sqlTypeSplit = sqlType.split(",");
        DsType dsType = cacheDsType.computeIfAbsent(sqlTypeSplit[0], DsType::valueOfCode);
        return Objects.requireNonNull(SchemaFramework.getFieldType(dsType, Integer.parseInt(sqlTypeSplit[1])), "passed FieldType failed.");
    }

    @Override
    public void writeToMap(RdbColumn topic, Map<String, Object> toMap) {
        super.writeToMap(topic, toMap);
        if (topic == null) {
            return;
        }

        if (StringUtils.isNotBlank(topic.getCatalog())) {
            toMap.put(KEY_RDB_CATALOG, topic.getCatalog());
        }
        if (StringUtils.isNotBlank(topic.getSchema())) {
            toMap.put(KEY_RDB_SCHEMA, topic.getSchema());
        }
        if (StringUtils.isNotBlank(topic.getTable())) {
            toMap.put(KEY_RDB_TABLE, topic.getTable());
        }
        if (StringUtils.isNotBlank(topic.getName())) {
            toMap.put(KEY_NAME, topic.getName());
        }
        if (StringUtils.isNotBlank(topic.getComment())) {
            toMap.put(KEY_COMMENT, topic.getComment());
        }
        if (topic.getUmiType() != null) {
            toMap.put(KEY_UMI_TYPE, topic.getUmiType().getTypeName());
        }
        if (topic.getSqlType() != null) {
            toMap.put(KEY_TYPE, topic.getSqlType().getStoreType());
        }
        if (topic.getIndex() != null) {
            toMap.put(KEY_RDB_ORDER, topic.getIndex());
        }
        if (topic.getCharLength() != null) {
            toMap.put(KEY_RDB_CHAR_LENGTH, topic.getCharLength());
        }
        if (topic.getByteLength() != null) {
            toMap.put(KEY_RDB_BYTE_LENGTH, topic.getByteLength());
        }
        if (topic.getNumericPrecision() != null) {
            toMap.put(KEY_RDB_NUMERIC_PRECISION, topic.getNumericPrecision());
        }
        if (topic.getNumericScale() != null) {
            toMap.put(KEY_RDB_NUMERIC_SCALE, topic.getNumericScale());
        }
        if (topic.getNumericUnsigned() != null) {
            toMap.put(KEY_RDB_NUMERIC_UNSIGNED, topic.getNumericUnsigned());
        }
        if (topic.getDatetimePrecision() != null) {
            toMap.put(KEY_RDB_DATETIME_PRECISION, topic.getDatetimePrecision());
        }
        if (topic.getDefaultValue() != null) {
            toMap.put(KEY_RDB_DEFAULT, topic.getDefaultValue());
        }
        if (topic.getDefaultValueIsFunc() != null) {
            toMap.put(KEY_RDB_DEFAULT_T, topic.getDefaultValueIsFunc());
        }
    }
}
