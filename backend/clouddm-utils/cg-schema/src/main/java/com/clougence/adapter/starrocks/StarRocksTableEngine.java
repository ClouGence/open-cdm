/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
