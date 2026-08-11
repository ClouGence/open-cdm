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
package com.clougence.clouddm.base.metadata.ds;

import lombok.Getter;

/**
 * The enum Db type.
 * <p>
 * Data source types are grouped by {@link #displayGroup}; groups are rendered from small to large. Within each group,
 * {@link #order} follows product popularity first, and keeps products from the same family adjacent where possible.
 * Cloud database group only contains services that are not intended for private deployment.
 *
 * @author wanshao create time is 2019/12/12 3:36 下午
 */
@Getter
public enum DataSourceType {

    // main db
    MySQL("my", "MySQL", 0, 10),
    Oracle("ora", "Oracle", 0, 20),
    SQLServer("ms", "SQLServer", 0, 30),
    PostgreSQL("pg", "PostgreSQL", 0, 40),
    MariaDB("mar", "MariaDB", 0, 50),
    TiDB("ti", "TiDB", 0, 60),
    Dameng("dm", "Dameng", 0, 70),
    OceanBase("ob", "OceanBase", 0, 80),
    ObForOracle("obo", "ObForOracle", 0, 90),
    Db2("db2", "Db2", 0, 100),
    Db2Fori("db24i", "Db2Fori", 0, 110),
    Hana("hana", "Hana", 0, 120),
    GaussDBForOpenGauss("gsog", "GaussDBForOpenGauss", 0, 130),
    GaussDB("gs", "GaussDB", 0, 140),
    PolarDbMySQL("pom", "PolarDbMySQL", 0, 150),
    PolarDbX("pox", "PolarDbX", 0, 160),
    PolarDBPg("popg", "PolarDBPg", 0, 170),

    // big data, analytics, olap
    StarRocks("sr", "StarRocks", 1, 10),
    Doris("drs", "Doris", 1, 20),
    SelectDB("sel", "SelectDB", 1, 30),
    ClickHouse("ck", "ClickHouse", 1, 40),
    Greenplum("gp", "Greenplum", 1, 50),
    Cloudberry("cb", "Cloudberry", 1, 60),

    // non-relational db
    Redis("re", "Redis", 2, 10),
    Valkey("vk", "Valkey", 2, 15),
    MongoDB("mdb", "MongoDB", 2, 20),

    // cloud database
    AdbForMySQL("amy", "AdbForMySQL", 3, 10),
    Redshift("rs", "Redshift", 3, 20),
    Hologres("hg", "Hologres", 3, 30),
    MaxCompute("mc", "MaxCompute", 3, 40),;

    private final String typeName;
    private final String shortName;
    private final int    displayGroup;
    private final int    order;

    DataSourceType(String shortName, String typeName, int displayGroup, int order){
        this.shortName = shortName;
        this.typeName = typeName;
        this.displayGroup = displayGroup;
        this.order = order;
    }

    public static DataSourceType getTypeByName(String typeName) {
        if (typeName == null || typeName.isEmpty()) {
            return null;
        }
        DataSourceType result = null;
        for (DataSourceType dataSourceType : DataSourceType.values()) {
            if (typeName.equalsIgnoreCase(dataSourceType.getTypeName())) {
                result = dataSourceType;
                break;
            }
        }
        return result;
    }

}
