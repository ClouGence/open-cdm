package com.clougence.schema.umi.serializer.rdb;

import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.serializer.UmiAttributeSetSerializer;
import com.clougence.schema.umi.special.rdb.RdbTrigger;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class RdbTriggerSerializer extends UmiAttributeSetSerializer<RdbTrigger> {

    @Override
    public void readData(Map<String, Object> jsonMap, RdbTrigger readTo) {
        super.readData(jsonMap, readTo);
        if (jsonMap == null) {
            return;
        }

        readTo.setTriggerTableCatalog((String) jsonMap.get(KEY_RDB_TRIGGER_TABLE_CATALOG));
        readTo.setTriggerTableSchema((String) jsonMap.get(KEY_RDB_TRIGGER_TABLE_SCHEMA));
        readTo.setTriggerTableName((String) jsonMap.get(KEY_RDB_TRIGGER_TABLE_NAME));
        readTo.setTriggerEvent((List<String>) jsonMap.get(KEY_RDB_TRIGGER_EVENT));
        readTo.setTriggerTableColumns((List<String>) jsonMap.get(KEY_RDB_TRIGGER_COLUMN));
        readTo.setName((String) jsonMap.get(KEY_NAME));
        readTo.setTriggerTime((String) jsonMap.get(KEY_RDB_TRIGGER_TIME));
        readTo.setSql((String) jsonMap.get(KEY_RDB_SQL));
        readTo.setFeatures((Map<String, Object>) jsonMap.get(KEY_RDB_FEATURES));
        readTo.setUmiType(UmiTypes.valueOfCode((String) jsonMap.get(KEY_UMI_TYPE)));
    }

    @Override
    public void writeToMap(RdbTrigger trigger, Map<String, Object> toMap) {
        super.writeToMap(trigger, toMap);
        if (trigger == null) {
            return;
        }

        if (StringUtils.isNotBlank(trigger.getTriggerTableCatalog())) {
            toMap.put(KEY_RDB_TRIGGER_TABLE_CATALOG, trigger.getTriggerTableCatalog());
        }
        if (StringUtils.isNotBlank(trigger.getTriggerTableSchema())) {
            toMap.put(KEY_RDB_TRIGGER_TABLE_SCHEMA, trigger.getTriggerTableSchema());
        }
        if (StringUtils.isNotBlank(trigger.getTriggerTableName())) {
            toMap.put(KEY_RDB_TRIGGER_TABLE_NAME, trigger.getTriggerTableName());
        }
        if (trigger.getTriggerEvent() != null) {
            toMap.put(KEY_RDB_TRIGGER_EVENT, trigger.getTriggerEvent());
        }
        if (trigger.getTriggerTableColumns() != null) {
            toMap.put(KEY_RDB_TRIGGER_COLUMN, trigger.getTriggerTableColumns());
        }
        if (trigger.getName() != null) {
            toMap.put(KEY_NAME, trigger.getName());
        }
        if (StringUtils.isNotBlank(trigger.getTriggerTime())) {
            toMap.put(KEY_RDB_TRIGGER_TIME, trigger.getTriggerTime());
        }
        if (StringUtils.isNotBlank(trigger.getSql())) {
            toMap.put(KEY_RDB_SQL, trigger.getSql());
        }
        if (trigger.getFeatures() != null) {
            toMap.put(KEY_RDB_FEATURES, trigger.getFeatures());
        }
        if (trigger.getUmiType() != null) {
            toMap.put(KEY_UMI_TYPE, trigger.getUmiType().getTypeName());
        }
    }
}
