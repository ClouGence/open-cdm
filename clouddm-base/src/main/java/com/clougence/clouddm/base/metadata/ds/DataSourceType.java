package com.clougence.clouddm.base.metadata.ds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    AuroraMySQL("aum", "AuroraMySQL", DsClassify.RDB, 0),
    PostgreSQL("pg", "PostgreSQL", DsClassify.RDB, 0),
    AuroraPostgreSQL("aup", "AuroraPostgreSQL", DsClassify.RDB, 0),
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
    GaussDBForMySQL("gsmy", "GaussDBForMySQL", DsClassify.RDB, 0),
    GaussDBForOpenGauss("gsog", "GaussDBForOpenGauss", DsClassify.RDB, 0),
    GaussDB("gs", "GaussDB", DsClassify.RDB, 0),
    Dameng("dm", "Dameng", DsClassify.RDB, 0),
    TdsqlCMySQL("tdcmy", "TdsqlCMySQL", DsClassify.RDB, 0),
    TdsqlMySQL("tdmy", "TdsqlMySQL", DsClassify.RDB, 0),
    KingbaseES("kes", "KingbaseES", DsClassify.RDB, 0),

    // mq,cache,search,schemaless db
    Kafka("kf", "Kafka", DsClassify.MQ, 1),
    AmazonMSK("amsk", "AmazonMSK", DsClassify.MQ, 1),
    RocketMQ("rmq", "RocketMQ", DsClassify.MQ, 1),
    Pulsar("ps", "Pulsar", DsClassify.MQ, 1),
    RabbitMQ("rbq", "RabbitMQ", DsClassify.MQ, 1),
    AutoMQ("amq", "AutoMQ", DsClassify.MQ, 1),
    Redis("re", "Redis", DsClassify.CACHE, 1),
    ElastiCache("ec", "ElastiCache", DsClassify.CACHE, 1),
    ElasticSearch("es", "ElasticSearch", DsClassify.SEARCH, 1),
    MongoDB("mdb", "MongoDB", DsClassify.RDB, 1),
    DynamoDB("dyn", "DynamoDB", DsClassify.RDB, 1),
    DocumentDB("ddb", "DocumentDB", DsClassify.RDB, 1),

    // data warehouse
    StarRocks("sr", "StarRocks", DsClassify.RDB, 2),
    Doris("drs", "Doris", DsClassify.RDB, 2),
    SelectDB("sel", "SelectDB", DsClassify.RDB, 2),
    AdbForMySQL("amy", "AdbForMySQL", DsClassify.RDB, 2),
    ClickHouse("ck", "ClickHouse", DsClassify.RDB, 2),
    Greenplum("gp", "Greenplum", DsClassify.RDB, 2),
    DuckDB("duck", "DuckDB", DsClassify.RDB, 2),
    Hana("hana", "Hana", DsClassify.RDB, 2),
    Redshift("rs", "Redshift", DsClassify.RDB, 2),
    Hologres("hg", "Hologres", DsClassify.RDB, 2),

    // big data
    Iceberg("ib", "Iceberg", DsClassify.RDB, 3),
    Paimon("pm", "Paimon", DsClassify.RDB, 3),
    DataLakeFormation("dlf", "DataLakeFormation", DsClassify.RDB, 3),
    DeltaLake("dl", "DeltaLake", DsClassify.RDB, 3),
    Hive("hv", "Hive", DsClassify.RDB, 3),
    Kudu("kd", "Kudu", DsClassify.RDB, 3),
    Lindorm("ld", "Lindorm", DsClassify.RDB, 3),
    Hudi("hu", "Hudi", DsClassify.RDB, 3),
    MaxCompute("mc", "MaxCompute", DsClassify.RDB, 3),

    //other
    GreptimeDB("gt", "GreptimeDB", DsClassify.RDB, 4),
    TDengine("taos", "TDengine", DsClassify.RDB, 4),
    Tunnel("tn", "Tunnel", DsClassify.RDB, 4),
    S3File("s3", "S3File", DsClassify.FILE, 4),
    OssFile("oss", "OssFile", DsClassify.FILE, 4),
    SshFile("ssh", "SshFile", DsClassify.FILE, 4),
    GoogleDrive("gdrive", "GoogleDrive", DsClassify.FILE, 4),
    Yuque("yu", "Yuque", DsClassify.FILE, 4),

    //api
    RagApi("rag", "RagApi", DsClassify.API, 5),
    OpenAI("opai", "OpenAI", DsClassify.API, 5),
    DashScope("dscp", "DashScope", DsClassify.API, 5),
    HuggingFace("hufc", "HuggingFace", DsClassify.API, 5),
    Cohere("cohr", "Cohere", DsClassify.API, 5),
    DeepSeek("dpsk", "DeepSeek", DsClassify.API, 5),
    LocalAI("lcai", "LocalAI", DsClassify.API, 5),
    Ollama("olma", "Ollama", DsClassify.API, 5),
    ZhipuAI("zhip", "ZhipuAI", DsClassify.API, 5),
    Anthropic("anthr", "Anthropic", DsClassify.API, 5),
    Bedrock("bedr", "Bedrock", DsClassify.API, 5),;

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

    public static List<List<DataSourceType>> groupByDisplay(List<DataSourceType> dataSourceTypes) {
        Map<Integer, List<DataSourceType>> sRe = new TreeMap<>();

        for (DataSourceType dataSourceType : dataSourceTypes) {
            List<DataSourceType> l = sRe.computeIfAbsent(dataSourceType.getDisplayGroup(), t -> new ArrayList<>());
            l.add(dataSourceType);
        }

        return new ArrayList<>(sRe.values());
    }
}
