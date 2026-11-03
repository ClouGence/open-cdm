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
    MySQL("my", "MySQL", DsClassify.RDB, 0, 10),
    Oracle("ora", "Oracle", DsClassify.RDB, 0, 20),
    SQLServer("ms", "SQLServer", DsClassify.RDB, 0, 30),
    PostgreSQL("pg", "PostgreSQL", DsClassify.RDB, 0, 40),
    CockroachDB("crdb", "CockroachDB", DsClassify.RDB, 0, 45),
    MariaDB("mar", "MariaDB", DsClassify.RDB, 0, 50),
    TiDB("ti", "TiDB", DsClassify.RDB, 0, 60),
    Dameng("dm", "Dameng", DsClassify.RDB, 0, 70),
    KingbaseESPostgreSQL("kespg", "KingbaseESPostgreSQL", DsClassify.RDB, 0, 75),
    KingbaseESMySQL("kesmy", "KingbaseESMySQL", DsClassify.RDB, 0, 76),
    KingbaseESOracle("kesora", "KingbaseESOracle", DsClassify.RDB, 0, 77),
    KingbaseESSQLServer("kesms", "KingbaseESSQLServer", DsClassify.RDB, 0, 78),
    GoldenDBMySQL("gdbmy", "GoldenDBMySQL", DsClassify.RDB, 0, 79),
    GoldenDBOracle("gdbora", "GoldenDBOracle", DsClassify.RDB, 0, 80),
    YashanDBOracle("yasora", "YashanDBOracle", DsClassify.RDB, 0, 81),
    YashanDBMySQL("yasmy", "YashanDBMySQL", DsClassify.RDB, 0, 82),
    OceanBase("ob", "OceanBase", DsClassify.RDB, 0, 90),
    ObForOracle("obo", "ObForOracle", DsClassify.RDB, 0, 100),
    Db2("db2", "Db2", DsClassify.RDB, 0, 110),
    Db2Fori("db24i", "Db2Fori", DsClassify.RDB, 0, 120),
    Hana("hana", "Hana", DsClassify.RDB, 0, 130),
    GaussDBForOpenGauss("gsog", "GaussDBForOpenGauss", DsClassify.RDB, 0, 140),
    GaussDB("gs", "GaussDB", DsClassify.RDB, 0, 150),
    PolarDbMySQL("pom", "PolarDbMySQL", DsClassify.RDB, 0, 160),
    PolarDbX("pox", "PolarDbX", DsClassify.RDB, 0, 170),
    PolarDBPg("popg", "PolarDBPg", DsClassify.RDB, 0, 180),

    // big data, analytics, olap
    StarRocks("sr", "StarRocks", DsClassify.RDB, 1, 10),
    Doris("drs", "Doris", DsClassify.RDB, 1, 20),
    SelectDB("sel", "SelectDB", DsClassify.RDB, 1, 30),
    ClickHouse("ck", "ClickHouse", DsClassify.RDB, 1, 40),
    Greenplum("gp", "Greenplum", DsClassify.RDB, 1, 50),
    Cloudberry("cb", "Cloudberry", DsClassify.RDB, 1, 60),

    // non-relational db
    Redis("re", "Redis", DsClassify.CACHE, 2, 10),
    Valkey("vk", "Valkey", DsClassify.CACHE, 2, 15),
    MongoDB("mdb", "MongoDB", DsClassify.DOCUMENT, 2, 20),
    Kafka("ka", "Kafka", DsClassify.MQ, 2, 30),
    ElasticSearch("es", "ElasticSearch", DsClassify.SEARCH, 2, 40),
    Etcd("et", "Etcd", DsClassify.CACHE, 2, 50),

    // cloud database
    AdbForMySQL("amy", "AdbForMySQL", DsClassify.RDB, 3, 10),
    Redshift("rs", "Redshift", DsClassify.RDB, 3, 20),
    Hologres("hg", "Hologres", DsClassify.RDB, 3, 30),
    MaxCompute("mc", "MaxCompute", DsClassify.RDB, 3, 40),;

    private final String     typeName;
    private final String     shortName;
    private final DsClassify classify;
    private final int        displayGroup;
    private final int        order;

    DataSourceType(String shortName, String typeName, DsClassify classify, int displayGroup, int order){
        this.shortName = shortName;
        this.typeName = typeName;
        this.classify = classify;
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
