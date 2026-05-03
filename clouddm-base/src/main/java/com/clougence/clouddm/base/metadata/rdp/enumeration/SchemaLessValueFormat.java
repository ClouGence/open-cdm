package com.clougence.clouddm.base.metadata.rdp.enumeration;

/**
 * @author bucketli 2021/6/10 10:03
 */
public enum SchemaLessValueFormat {

    CLOUDCANAL_JSON_FOR_MQ,

    CLOUDCANAL_JSON_FOR_TUNNEL,

    CANAL_JSON_FOR_MQ,

    ALIYUN_DTS_AVRO_FOR_MQ,

    ALIYUN_DTS_AVRO_FOR_MQ_V1,

    ALIYUN_DTS_AVRO_FOR_MQ_V2,

    DEBEZIUM_ENVELOP_JSON_FOR_MQ,

    VALUE_JSON_FOR_CACHE,

    VALUE_COL_CAMEL_CASE_JSON_FOR_CACHE,

    ORIGIN_MSG_FOR_MQ,

    DSG_JSON_FOR_MQ,

    OGG_KV_JSON_FOR_MQ;

    public static SchemaLessValueFormat valueOfIgnoreCase(String tarFormat) {
        if (tarFormat == null || tarFormat.trim().equals("")) {
            return null;
        }

        for (SchemaLessValueFormat format : SchemaLessValueFormat.values()) {
            if (format.name().toLowerCase().equals(tarFormat.trim().toLowerCase())) {
                return format;
            }
        }
        return null;
    }
}
