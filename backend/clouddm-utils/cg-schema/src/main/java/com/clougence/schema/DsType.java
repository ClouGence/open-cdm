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
package com.clougence.schema;

import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * The enum Db type.
 *
 * @author wanshao create time is 2019/12/12 3:36 下午
 */
@Getter
@Slf4j
public enum DsType {

    // mysql family
    MySQL("my", "MySQL"),
    AdbForMySQL("amy", "AdbForMySQL"),
    PolarDBMySQL("pmy", "PolarDBMySQL"),
    PolarDbX("pox", "PolarDbX"),
    TiDB("ti", "TiDB"),
    Lindorm("ld", "Lindorm"),

    // PostgreSQL family
    PostgreSQL("pg", "PostgreSQL"),
    PolarDBPostgre("ppg", "PolarDBPostgre"),
    GaussDB("gs", "GaussDB"),

    // doris family
    StarRocks("sr", "StarRocks"),
    Doris("dr", "Doris"),
    SelectDB("sel", "SelectDB"),

    Hana("ha", "Hana"),
    Oracle("or", "Oracle"),
    Dameng("dm", "Dameng"),
    Kudu("kd", "Kudu"),
    SqlServer("ms", "SqlServer"),
    Db2("db2", "Db2"),
    ClickHouse("ck", "ClickHouse"),
    Kafka("ka", "Kafka"),
    RocketMQ("rmq", "RocketMQ"),
    RabbitMQ("rbq", "RabbitMQ"),
    AutoMq("amq", "AutoMQ"),
    OceanBase("ob", "OceanBase"),
    ObForOracle("obo", "ObForOracle"),
    Redis("red", "Redis"),
    ElasticSearch("es", "ElasticSearch"),
    MongoDB("mdb", "MongoDB"),
    DynamoDB("dyn", "DynamoDB"),
    Tunnel("tn", "Tunnel"),
    Hudi("hu", "Hudi"),
    Hive("hv", "Hive"),
    Iceberg("ib", "Iceberg"),
    Paimon("pm", "Paimon"),
    DeltaLake("dl", "DeltaLake"),
    Redshift("rs", "Redshift"),
    H2("h2", "H2"),
    Pulsar("ps", "Pulsar"),
    GreptimeDB("gt", "GreptimeDB"),
    TDengine("taos", "TDengine"),
    DuckDB("dk", "DuckDB"),
    S3File("s3", "S3File"),
    OssFile("oss", "OssFile"),
    SshFile("ssh", "SshFile"),
    GoogleDrive("gdrive", "GoogleDrive"),
    Yuque("yu", "Yuque"),
    RagApi("rag", "RagApi"),
    MariaDB("mb", "MariaDB"),
    MaxCompute("mc", "MaxCompute"),
    Hologres("hg", "Hologres"),
    TdsqlCMySQL("tdcmy", "TdsqlCMySQL"),
    TdsqlMySQL("tdmy", "TdsqlMySQL"),
    KingbaseES("kes", "KingbaseES"),;

    private final String typeName;
    private final String shortName;

    DsType(String shortName, String typeName){
        this.shortName = shortName;
        this.typeName = typeName;
    }

    public static DsType valueOfCode(String code) {
        for (DsType dsType : DsType.values()) {
            if (StringUtils.equalsIgnoreCase(dsType.typeName, code) || StringUtils.equalsIgnoreCase(dsType.shortName, code)) {
                return dsType;
            }
        }
        throw new UnsupportedOperationException("Unsupported DsType " + code);
    }
}
