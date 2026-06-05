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
package com.clougence;

import com.clougence.reactor.mappings.adbmysql_src.AdbMySql2AdbMySqlSchemaPlugin;
import com.clougence.reactor.mappings.db2_src.*;
import com.clougence.reactor.mappings.dm_src.*;
import com.clougence.reactor.mappings.hana_src.*;
import com.clougence.reactor.mappings.mysql_src.*;
import com.clougence.reactor.mappings.oracle_src.*;
import com.clougence.reactor.mappings.postgres_src.*;
import com.clougence.reactor.mappings.sqlserver_src.*;
import com.clougence.reactor.mappings.starrocks_src.*;
import com.clougence.reactor.mappings.tidb_src.*;
import com.clougence.schema.SchemaBinder;
import com.clougence.schema.SchemaPlugin;

public class SchemaInitPlugin implements SchemaPlugin {

    @Override
    public void init(SchemaBinder binder) {
        initAdbMySqlSrc(binder);
        initMySqlSrc(binder);
        initTiDBSrc(binder);
        initDamengSrc(binder);
        initOracleSrc(binder);
        initMsSqlSrc(binder);
        initPostgresSrc(binder);
        initHanaSrc(binder);
        initStartRocksSrc(binder);
        initDB2Src(binder);
    }

    private void initDB2Src(SchemaBinder binder) {
        new Db2ToDamengSchemaPlugin().init(binder);
        new Db2ToDb2SchemaPlugin().init(binder);
        new Db2ToMySqlSchemaPlugin().init(binder);
        new Db2ToStarRocksSchemaPlugin().init(binder);
        new Db2ToTIDBSchemaPlugin().init(binder);
    }

    private void initStartRocksSrc(SchemaBinder binder) {
        new StartRocks2ClickhouseSchemaPlugin().init(binder);
        new StartRocks2DamengSchemaPlugin().init(binder);
        new StartRocks2MySqlSchemaPlugin().init(binder);
        new StartRocks2OracleSchemaPlugin().init(binder);
        new StartRocks2StartRocksSchemaPlugin().init(binder);
        new StartRocks2TiDBSchemaPlugin().init(binder);
    }

    private void initHanaSrc(SchemaBinder binder) {
        new Hana2AdbMySqlSchemaPlugin().init(binder);
        new Hana2DorisSchemaPlugin().init(binder);
        new Hana2HanaSchemaPlugin().init(binder);
        new Hana2MysqlSchemaPlugin().init(binder);
        new Hana2OceanBaseSchemaPlugin().init(binder);
        new Hana2PostgresSchemaPlugin().init(binder);
        new Hana2StarRocksSchemaPlugin().init(binder);
        new Hana2TiDBSchemaPlugin().init(binder);
    }

    private void initAdbMySqlSrc(SchemaBinder binder) {
        new AdbMySql2AdbMySqlSchemaPlugin().init(binder);
    }

    private void initMsSqlSrc(SchemaBinder binder) {
        new SqlServer2DamengSchemaPlugin().init(binder);
        new SqlServer2DorisSchemaPlugin().init(binder);
        new SqlServer2GaussDBSchemaPlugin().init(binder);
        new SqlServer2MySqlSchemaPlugin().init(binder);
        new SqlServer2OracleSchemaPlugin().init(binder);
        new SqlServer2PostgresSchemaPlugin().init(binder);
        new SqlServer2SqlServerSchemaPlugin().init(binder);
        new SqlServer2StartRocksSchemaPlugin().init(binder);
    }

    private void initOracleSrc(SchemaBinder binder) {
        new Oracle2AdbMySqlSchemaPlugin().init(binder);
        new Oracle2ClickhouseSchemaPlugin().init(binder);
        new Oracle2DamengSchemaPlugin().init(binder);
        new Oracle2Db2SchemaPlugin().init(binder);
        new Oracle2DorisSchemaPlugin().init(binder);
        new Oracle2GaussDBSchemaPlugin().init(binder);
        new Oracle2HanaSchemaPlugin().init(binder);
        new Oracle2MySqlSchemaPlugin().init(binder);
        new Oracle2OceanBaseSchemaPlugin().init(binder);
        new Oracle2OracleSchemaPlugin().init(binder);
        new Oracle2PostgresSchemaPlugin().init(binder);
        new Oracle2SqlServerSchemaPlugin().init(binder);
        new Oracle2StarRocksSchemaPlugin().init(binder);
        new Oracle2TiDBSchemaPlugin().init(binder);
    }

    private static void initDamengSrc(SchemaBinder binder) {
        new Dameng2ClickHouseSchemaPlugin().init(binder);
        new Dameng2DamengSchemaPlugin().init(binder);
        new Dameng2DorisSchemaPlugin().init(binder);
        new Dameng2MySqlSchemaPlugin().init(binder);
        new Dameng2OceanBaseSchemaPlugin().init(binder);
        new Dameng2StarRocksSchemaPlugin().init(binder);
    }

    private static void initMySqlSrc(SchemaBinder binder) {
        new MySql2HanaSchemaPlugin().init(binder);
        new MySql2ClickhouseSchemaPlugin().init(binder);
        new MySql2PostgresSchemaPlugin().init(binder);
        new MySql2MongoSchemaPlugin().init(binder);
        new MySql2AdbMySqlSchemaPlugin().init(binder);
        new MySql2TiDBSchemaPlugin().init(binder);
        new MySql2PolarDbMySchemaPlugin().init(binder);
        new MySql2MySqlSchemaPlugin().init(binder);
        new MySql2GaussDBSchemaPlugin().init(binder);
        new MySql2OceanBaseSchemaPlugin().init(binder);
        new MySql2ObForOracleSchemaPlugin().init(binder);
        new MySql2DorisSchemaPlugin().init(binder);
        new MySql2PolarDbXSchemaPlugin().init(binder);
        new MySql2OracleSchemaPlugin().init(binder);
        new MySql2DamengSchemaPlugin().init(binder);
        new MySql2SqlServerSchemaPlugin().init(binder);
        new MySql2StartRocksSchemaPlugin().init(binder);
        new MySql2Db2SchemaPlugin().init(binder);
    }

    private static void initTiDBSrc(SchemaBinder binder) {
        new TiDB2ClickhouseSchemaPlugin().init(binder);
        new TiDB2DorisSchemaPlugin().init(binder);
        new TiDB2MySQLSchemaPlugin().init(binder);
        new TiDB2OceanBaseSchemaPlugin().init(binder);
        new TiDB2PostgresSchemaPlugin().init(binder);
        new TiDB2StarRocksSchemaPlugin().init(binder);
        new TiDB2TiDBSchemaPlugin().init(binder);
    }

    private void initPostgresSrc(SchemaBinder binder) {
        new Postgres2ClickhouseSchemaPlugin().init(binder);
        new Postgres2DamengSchemaPlugin().init(binder);
        new Postgres2Db2SchemaPlugin().init(binder);
        new Postgres2DorisSchemaPlugin().init(binder);
        new Postgres2HanaSchemaPlugin().init(binder);
        new Postgres2MySqlSchemaPlugin().init(binder);
        new Postgres2OceanBaseSchemaPlugin().init(binder);
        new Postgres2OracleSchemaPlugin().init(binder);
        new Postgres2PostgresSchemaPlugin().init(binder);
        new Postgres2SqlServerSchemaPlugin().init(binder);
        new Postgres2StartRocksSchemaPlugin().init(binder);
        new Postgres2TiDBSchemaPlugin().init(binder);
    }

}
