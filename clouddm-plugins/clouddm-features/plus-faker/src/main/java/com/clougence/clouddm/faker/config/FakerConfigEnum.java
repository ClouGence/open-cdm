/*
 * Copyright 2015-2022 the original author or authors.
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
 * FakerTable 构建器
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public enum FakerConfigEnum {

    /** 表 处理 update/delete 生成 where 的存量数据加载器 */
    GLOBAL_DATA_LOADER_FACTORY("dataLoaderFactory"),
    /** sql 语句方言 */
    GLOBAL_DIALECT("dialect"),
    /** 数据默认生成策略 */
    GLOBAL_STRATEGY("strategy"),

    /** 表 catalog */
    TABLE_CATALOG("catalog"),
    /** 表 schema */
    TABLE_SCHEMA("schema"),
    /** 表 生成的总条数 */
    TABLE_GENERATOR_TOTAL("total"),
    /** 表 name */
    TABLE_TABLE("table"),
    /** 列配置信息 */
    TABLE_COLUMNS("columns"),
    /** 任何操作都不参与的列 */
    TABLE_COL_IGNORE_ALL("ignoreColsAll"),
    /** 不参与 insert 的列 */
    TABLE_COL_IGNORE_INSERT("ignoreColsInsert"),
    /** 不参与 update set 的列 */
    TABLE_COL_IGNORE_UPDATE("ignoreColsUpdate"),
    /** 不参与 delete where 条件的列 */
    TABLE_COL_IGNORE_DELETE_WHERE("ignoreColsDeleteWhere"),
    /** 不参与 update where 条件的列 */
    TABLE_COL_IGNORE_UPDATE_WHERE("ignoreColsUpdateWhere"),
    /** INSERT 语句生成策略 */
    TABLE_ACT_POLITIC_INSERT("insertPolitic"),
    /** UPDATE SET 语句生成策略 */
    TABLE_ACT_POLITIC_UPDATE("updatePolitic"),
    /** UPDATE/DELETE 语句的 WHERE 子句生成策略 */
    TABLE_ACT_POLITIC_WHERE("wherePolitic"),

    /** 自定义数据发生器类型 */
    COLUMN_SEED_TYPE("seedType"),
    /** 自定义数据发生器 */
    COLUMN_SEED_FACTORY("seedFactory"),
    /** 是一个数组 */
    COLUMN_ARRAY_TYPE("isArray"),

    /** 用于 select 语句的参数，默认是：{name} */
    SELECT_TEMPLATE("selectTemplate"),
    /** 用于 insert 语句的参数，默认是：? */
    INSERT_TEMPLATE("insertTemplate"),
    /** 用于 update 语句的列名，默认是：{name} */
    SET_COL_TEMPLATE("setColTemplate"),
    /** 用于 update 语句的参数，默认是：? */
    SET_VALUE_TEMPLATE("setValueTemplate"),
    /** 用于 update/delete 中 where 子语句的列名，默认是：{name} */
    WHERE_COL_TEMPLATE("whereColTemplate"),
    /** 用于 update/delete 中 where 子语句的参数，默认是：? */
    WHERE_VALUE_TEMPLATE("whereValueTemplate");

    private final String configKey;

    public String getConfigKey() { return configKey; }

    FakerConfigEnum(String configKey){
        this.configKey = configKey;
    }
}
