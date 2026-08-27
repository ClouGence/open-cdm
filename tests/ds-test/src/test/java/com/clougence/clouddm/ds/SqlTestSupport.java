package com.clougence.clouddm.ds;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.ads.sql.ads4my.AdsMySqlEngineSpi;
import com.clougence.clouddm.ds.clickhouse.sql.ChSqlEngineSpi;
import com.clougence.clouddm.ds.dameng.sql.DmSqlEngineSpi;
import com.clougence.clouddm.ds.gauss.sql.GaussSqlEngineSpi;
import com.clougence.clouddm.ds.hana.sql.HanaSqlEngineSpi;
import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.ds.maxcompute.sql.McSqlEngineSpi;
import com.clougence.clouddm.ds.oceanbase.sql.ob4my.ObSqlEngineSpi;
import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.ObOraSqlEngineSpi;
import com.clougence.clouddm.ds.polardb.sql.porx.PorXSqlEngineSpi;
import com.clougence.clouddm.ds.starrocks.sql.SrSqlEngineSpi;
import com.clougence.clouddm.ds.tidb.sql.TiSqlEngineSpi;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.SqlEngineSpi;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.sql.db2.Db2SqlEngineSpi;
import com.clougence.sql.doris.DrSqlEngineSpi;
import com.clougence.sql.iso.sql2003.Sql2003SqlEngineSpi;
import com.clougence.sql.iso.sql92.Sql92SqlEngineSpi;
import com.clougence.sql.iso.sql99.Sql99SqlEngineSpi;
import com.clougence.sql.mongodb.MongoSqlEngineSpi;
import com.clougence.sql.mysql.MySqlEngineSpi;
import com.clougence.sql.oracle.OraSqlEngineSpi;
import com.clougence.sql.postgres.PgSqlEngineSpi;
import com.clougence.sql.redis.RedisSqlEngineSpi;
import com.clougence.sql.sqlserver.MsSqlSqlEngineSpi;

public final class SqlTestSupport {

    private static final MetaService                                      META_SERVICE = new VirtualMetaService();
    private static final Map<String, Function<MetaService, SqlEngineSpi>> SQL_ENGINES  = new LinkedHashMap<>();
    private static final Map<String, DataSourceType>                      DS_TYPES     = new LinkedHashMap<>();

    static {
        bind("sql2003", DataSourceType.Db2, Sql2003SqlEngineSpi::new);
        bind("sql92", DataSourceType.Db2, Sql92SqlEngineSpi::new);
        bind("sql99", DataSourceType.Db2, Sql99SqlEngineSpi::new);
        //
        bind("adb", DataSourceType.AdbForMySQL, AdsMySqlEngineSpi::new);
        bind("clickhouse", DataSourceType.ClickHouse, ChSqlEngineSpi::new);
        bind("dameng", DataSourceType.Dameng, DmSqlEngineSpi::new);
        bind("db2", DataSourceType.Db2, metaService -> new Db2SqlEngineSpi());
        bind("doris", DataSourceType.Doris, DrSqlEngineSpi::new);
        bind("gauss", DataSourceType.GaussDB, GaussSqlEngineSpi::new);
        bind("gauss_og", DataSourceType.GaussDBForOpenGauss, GaussSqlEngineSpi::new);
        bind("goldendb", DataSourceType.GoldenDBMySQL, MySqlEngineSpi::new);
        bind("greenplum", DataSourceType.Greenplum, PgSqlEngineSpi::new);
        bind("hana", DataSourceType.Hana, metaService -> new HanaSqlEngineSpi());
        bind("hologres", DataSourceType.Hologres, PgSqlEngineSpi::new);
        bind("mariadb", DataSourceType.MariaDB, MySqlEngineSpi::new);
        bind("maxcompute", DataSourceType.MaxCompute, McSqlEngineSpi::new);
        bind("mongodb", DataSourceType.MongoDB, MongoSqlEngineSpi::new);
        bind("mysql", DataSourceType.MySQL, MySqlEngineSpi::new);
        bind("ob4my", DataSourceType.OceanBase, ObSqlEngineSpi::new);
        bind("ob4ora", DataSourceType.ObForOracle, ObOraSqlEngineSpi::new);
        bind("oracle", DataSourceType.Oracle, OraSqlEngineSpi::new);
        bind("por4my", DataSourceType.PolarDbMySQL, MySqlEngineSpi::new);
        bind("por4pg", DataSourceType.PolarDBPg, PgSqlEngineSpi::new);
        bind("por4x", DataSourceType.PolarDbX, PorXSqlEngineSpi::new);
        bind("postgres", DataSourceType.PostgreSQL, PgSqlEngineSpi::new);
        bind("redis", DataSourceType.Redis, RedisSqlEngineSpi::new);
        bind("selectdb", DataSourceType.SelectDB, DrSqlEngineSpi::new);
        bind("sqlserver", DataSourceType.SQLServer, MsSqlSqlEngineSpi::new);
        bind("starrocks", DataSourceType.StarRocks, SrSqlEngineSpi::new);
        bind("tidb", DataSourceType.TiDB, TiSqlEngineSpi::new);
    }

    private SqlTestSupport(){
    }

    public static MetaService metaService() {
        return META_SERVICE;
    }

    public static SqlEngineSpi sqlEngine(String datasource) {
        return sqlEngine(datasource, META_SERVICE);
    }

    public static SqlEngineSpi sqlEngine(String datasource, MetaService metaService) {
        Function<MetaService, SqlEngineSpi> factory = SQL_ENGINES.get(datasource);
        if (factory == null) {
            throw new IllegalStateException("No SqlEngineSpi mapping for datasource: " + datasource);
        }
        return factory.apply(metaService);
    }

    public static DataSourceType dataSourceType(String datasource) {
        DataSourceType dataSourceType = DS_TYPES.get(datasource);
        if (dataSourceType == null) {
            throw new IllegalStateException("No DataSourceType mapping for datasource: " + datasource);
        }
        return dataSourceType;
    }

    public static SqlParserParameters parserParameters(String datasource) {
        return switch (datasource) {
            case "adb" -> new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "8.0.46"));
            case "mariadb" -> new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "8.0.46"));
            case "goldendb" -> new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "8.0.25"));
            case "mysql" -> new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "8.0.46"));
            case "ob4my" -> new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "8.0.46"));
            case "por4my" -> new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "8.0.46"));
            case "por4x" -> new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "8.0.46"));
            case "tidb" -> new SqlParserParameters(Map.of(SqlParserParameters.VERSION, "8.0.46"));
            default -> SqlParserParameters.empty();
        };
    }

    public static ContextInfo contextInfo(String datasource) {
        if ("maxcompute".equals(datasource)) {
            return maxComputeContext(true);
        }
        return ContextInfo.builder().build();
    }

    public static String datasourceFromPath(String resourcePath) {
        String[] parts = resourcePath.split("/");
        if (parts.length < 2) {
            throw new IllegalStateException("Invalid SQL test resource path: " + resourcePath);
        }
        return datasourceFromFilename(parts[1], parts[parts.length - 1]);
    }

    public static String datasourceFromFilename(String datasource, String filename) {
        if ("doris".equals(datasource) && filename.startsWith("selectdb_")) {
            return "selectdb";
        }
        if ("gauss".equals(datasource) && filename.startsWith("gauss_og_")) {
            return "gauss_og";
        }
        if ("maxcompute".equals(datasource)) {
            return "maxcompute";
        }
        if ("mysql".equals(datasource)) {
            if (filename.startsWith("mariadb_")) {
                return "mariadb";
            }
            if (filename.startsWith("por4my_")) {
                return "por4my";
            }
            if (filename.startsWith("por4x_")) {
                return "por4x";
            }
            if (filename.startsWith("tidb_")) {
                return "tidb";
            }
        }
        if ("postgres".equals(datasource)) {
            if (filename.startsWith("greenplum_")) {
                return "greenplum";
            }
            if (filename.startsWith("hologres_")) {
                return "hologres";
            }
            if (filename.startsWith("por4pg_")) {
                return "por4pg";
            }
        }
        return datasource;
    }

    public static List<String> resourceFiles(String resourceDir) {
        return TextCaseSupport.resourceFiles(resourceDir);
    }

    private static void bind(String datasource, DataSourceType dataSourceType, Function<MetaService, SqlEngineSpi> sqlEngineFactory) {
        SQL_ENGINES.put(datasource, sqlEngineFactory);
        DS_TYPES.put(datasource, dataSourceType);
    }

    private static ContextInfo maxComputeContext(boolean schemaStyle) {
        McConfig dataSourceConfig = new McConfig();
        dataSourceConfig.setSchemaStyle(schemaStyle);
        return ContextInfo.builder().dataSourceConfig(dataSourceConfig).build();
    }
}
