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
 *
 * @author wanshao create time is 2019/12/12 3:36 下午
 */
@Getter
public enum DataSourceType {

    //transactional db
    MySQL("my", "MySQL", DsClassify.RDB, 0),
    MariaDB("mar", "MariaDB", DsClassify.RDB, 0),
    PostgreSQL("pg", "PostgreSQL", DsClassify.RDB, 0),
    Oracle("ora", "Oracle", DsClassify.RDB, 0),
    SQLServer("ms", "SQLServer", DsClassify.RDB, 0),
    Db2("db2", "Db2", DsClassify.RDB, 0),
    Db2Fori("db24i", "Db2Fori", DsClassify.RDB, 0),
    OceanBase("ob", "OceanBase", DsClassify.RDB, 0),
    ObForOracle("obo", "ObForOracle", DsClassify.RDB, 0),
    TiDB("ti", "TiDB", DsClassify.RDB, 0),
    PolarDbMySQL("pom", "PolarDbMySQL", DsClassify.RDB, 0),
    PolarDbX("pox", "PolarDbX", DsClassify.RDB, 0),
    PolarDBPg("popg", "PolarDBPg", DsClassify.RDB, 0),
    GaussDBForOpenGauss("gsog", "GaussDBForOpenGauss", DsClassify.RDB, 0),
    GaussDB("gs", "GaussDB", DsClassify.RDB, 0),
    Dameng("dm", "Dameng", DsClassify.RDB, 0),

    // mq,cache,search,schemaless db
    Redis("re", "Redis", DsClassify.CACHE, 1),
    MongoDB("mdb", "MongoDB", DsClassify.RDB, 1),

    // data warehouse
    StarRocks("sr", "StarRocks", DsClassify.RDB, 2),
    Doris("drs", "Doris", DsClassify.RDB, 2),
    SelectDB("sel", "SelectDB", DsClassify.RDB, 2),
    AdbForMySQL("amy", "AdbForMySQL", DsClassify.RDB, 2),
    ClickHouse("ck", "ClickHouse", DsClassify.RDB, 2),
    Greenplum("gp", "Greenplum", DsClassify.RDB, 2),
    Hana("hana", "Hana", DsClassify.RDB, 2),
    Redshift("rs", "Redshift", DsClassify.RDB, 2),
    Hologres("hg", "Hologres", DsClassify.RDB, 2),

    // big data
    MaxCompute("mc", "MaxCompute", DsClassify.RDB, 3),;

    private final String     typeName;

    private final String     shortName;

    private final DsClassify dsClassify;

    /**
     * just for display on web page.no more meaning
     */
    private final int        displayGroup;

    DataSourceType(String shortName, String typeName, DsClassify dsClassify, int displayGroup){
        this.shortName = shortName;
        this.typeName = typeName;
        this.dsClassify = dsClassify;
        this.displayGroup = displayGroup;
    }

    public static DataSourceType getTypeByName(String typeName) {
        if (typeName == null || typeName.equals("")) {
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
