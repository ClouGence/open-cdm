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
package com.clougence.clouddm.faker.config;

/**
 * FakerTable Builder
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public enum FakerConfigEnum {

    /** Existing-row data loader used to build UPDATE/DELETE WHERE clauses. */
    GLOBAL_DATA_LOADER_FACTORY("dataLoaderFactory"),
    /** SQL dialect. */
    GLOBAL_DIALECT("dialect"),
    /** Default data generation strategy. */
    GLOBAL_STRATEGY("strategy"),

    /** Table catalog. */
    TABLE_CATALOG("catalog"),
    /** Table schema. */
    TABLE_SCHEMA("schema"),
    /** Total number of rows generated for the table. */
    TABLE_GENERATOR_TOTAL("total"),
    /** Table name. */
    TABLE_TABLE("table"),
    /** Column configuration. */
    TABLE_COLUMNS("columns"),
    /** Columns excluded from all operations. */
    TABLE_COL_IGNORE_ALL("ignoreColsAll"),
    /** Columns excluded from INSERT statements. */
    TABLE_COL_IGNORE_INSERT("ignoreColsInsert"),
    /** Columns excluded from UPDATE SET clauses. */
    TABLE_COL_IGNORE_UPDATE("ignoreColsUpdate"),
    /** Columns excluded from DELETE WHERE conditions. */
    TABLE_COL_IGNORE_DELETE_WHERE("ignoreColsDeleteWhere"),
    /** Columns excluded from UPDATE WHERE conditions. */
    TABLE_COL_IGNORE_UPDATE_WHERE("ignoreColsUpdateWhere"),
    /** INSERT statement generation strategy. */
    TABLE_ACT_POLITIC_INSERT("insertPolitic"),
    /** UPDATE SET statement generation policy */
    TABLE_ACT_POLITIC_UPDATE("updatePolitic"),
    /** WHERE clause generation strategy for UPDATE/DELETE statements. */
    TABLE_ACT_POLITIC_WHERE("wherePolitic"),

    /** Custom data generator type. */
    COLUMN_SEED_TYPE("seedType"),
    /** Custom data generator. */
    COLUMN_SEED_FACTORY("seedFactory"),
    /** Whether the column is an array. */
    COLUMN_ARRAY_TYPE("isArray"),

    /** Parameter template for SELECT statements, default: {name}. */
    SELECT_TEMPLATE("selectTemplate"),
    /** Parameter template for INSERT statements, default: ?. */
    INSERT_TEMPLATE("insertTemplate"),
    /** Column-name template for UPDATE statements, default: {name}. */
    SET_COL_TEMPLATE("setColTemplate"),
    /** Parameter template for UPDATE statements, default: ?. */
    SET_VALUE_TEMPLATE("setValueTemplate"),
    /** Column-name template for UPDATE/DELETE WHERE clauses, default: {name}. */
    WHERE_COL_TEMPLATE("whereColTemplate"),
    /** Parameter template for UPDATE/DELETE WHERE clauses, default: ?. */
    WHERE_VALUE_TEMPLATE("whereValueTemplate");

    private final String configKey;

    public String getConfigKey() { return configKey; }

    FakerConfigEnum(String configKey){
        this.configKey = configKey;
    }
}
