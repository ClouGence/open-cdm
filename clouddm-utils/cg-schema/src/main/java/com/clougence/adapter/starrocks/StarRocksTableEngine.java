package com.clougence.adapter.starrocks;

/**
 * @author chunlin create time is 2025/4/18
 * https://github.com/StarRocks/starrocks/blob/main/fe/fe-core/src/main/java/com/starrocks/catalog/Table.java
 */
public enum StarRocksTableEngine {
    MYSQL,
    OLAP,
    OLAP_EXTERNAL,
    SCHEMA,
    INLINE_VIEW,
    VIEW,
    BROKER,
    ELASTICSEARCH,
    HIVE,
    ICEBERG,
    HUDI,
    JDBC,
    MATERIALIZED_VIEW,
    CLOUD_NATIVE,
    DELTALAKE,
    FILE,
    CLOUD_NATIVE_MATERIALIZED_VIEW,
    TABLE_FUNCTION,
    PAIMON,
    ODPS,
    BLACKHOLE,
    METADATA,
    KUDU,
    HIVE_VIEW,
    ICEBERG_VIEW;
}
