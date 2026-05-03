package com.clougence.schema.umi.serializer;

import java.util.Map;

import com.clougence.schema.umi.struts.constraint.ConstraintObject;
import com.clougence.utils.StringUtils;

public class ConstraintObjectSerializer<T extends ConstraintObject> extends UmiAttributeSetSerializer<T> {

    public void readData(Map<String, Object> jsonMap, T readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        readTo.setCatalog((String) jsonMap.get(KEY_RDB_CATALOG));
        readTo.setSchema((String) jsonMap.get(KEY_RDB_SCHEMA));
        readTo.setTable((String) jsonMap.get(KEY_RDB_TABLE));
        readTo.setName((String) jsonMap.get(KEY_NAME));
        readTo.setComment((String) jsonMap.get(KEY_COMMENT));
    }

    @Override
    public void writeToMap(T constraint, Map<String, Object> toMap) {
        super.writeToMap(constraint, toMap);
        if (constraint == null) {
            return;
        }

        if (StringUtils.isNotBlank(constraint.getCatalog())) {
            toMap.put(KEY_RDB_CATALOG, constraint.getCatalog());
        }
        if (StringUtils.isNotBlank(constraint.getSchema())) {
            toMap.put(KEY_RDB_SCHEMA, constraint.getSchema());
        }
        if (StringUtils.isNotBlank(constraint.getTable())) {
            toMap.put(KEY_RDB_TABLE, constraint.getTable());
        }
        if (StringUtils.isNotBlank(constraint.getName())) {
            toMap.put(KEY_NAME, constraint.getName());
        }
        if (StringUtils.isNotBlank(constraint.getComment())) {
            toMap.put(KEY_COMMENT, constraint.getComment());
        }

        toMap.put(KEY_TYPE, constraint.getConstraintType().getFullTypeCode());
    }
}
