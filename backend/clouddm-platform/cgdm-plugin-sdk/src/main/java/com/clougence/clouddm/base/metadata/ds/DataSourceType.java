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
    MySQL("my", "MySQL", 0),
    MariaDB("mar", "MariaDB", 0),
    PostgreSQL("pg", "PostgreSQL", 0),
    Oracle("ora", "Oracle", 0),
    SQLServer("ms", "SQLServer", 0),
    Db2("db2", "Db2", 0),
    Db2Fori("db24i", "Db2Fori", 0),
    OceanBase("ob", "OceanBase", 0),
    ObForOracle("obo", "ObForOracle", 0),
    TiDB("ti", "TiDB", 0),
    PolarDbMySQL("pom", "PolarDbMySQL", 0),
    PolarDbX("pox", "PolarDbX", 0),
    PolarDBPg("popg", "PolarDBPg", 0),
    GaussDBForOpenGauss("gsog", "GaussDBForOpenGauss", 0),
    GaussDB("gs", "GaussDB", 0),
    Dameng("dm", "Dameng", 0),

    // mq,cache,search,schemaless db
    Redis("re", "Redis", 1),
    MongoDB("mdb", "MongoDB", 1),

    // data warehouse
    StarRocks("sr", "StarRocks", 2),
    Doris("drs", "Doris", 2),
    SelectDB("sel", "SelectDB", 2),
    AdbForMySQL("amy", "AdbForMySQL", 2),
    ClickHouse("ck", "ClickHouse", 2),
    Greenplum("gp", "Greenplum", 2),
    Hana("hana", "Hana", 2),
    Redshift("rs", "Redshift", 2),
    Hologres("hg", "Hologres", 2),

    // big data
    MaxCompute("mc", "MaxCompute", 3),;

    private final String typeName;
    private final String shortName;
    private final int    displayGroup;

    DataSourceType(String shortName, String typeName, int displayGroup){
        this.shortName = shortName;
        this.typeName = typeName;
        this.displayGroup = displayGroup;
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
