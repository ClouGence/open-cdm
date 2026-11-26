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
package com.clougence.clouddm.ds.metadata;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.clougence.clouddm.ds.ads.execute.ads4my.AdsMyMetaService;
import com.clougence.clouddm.ds.clickhouse.execute.ChMetaService;
import com.clougence.clouddm.ds.cloudberry.execute.CbMetaService;
import com.clougence.clouddm.ds.cockroachdb.execute.CrdbMetaService;
import com.clougence.clouddm.ds.dameng.execute.DmMetaService;
import com.clougence.clouddm.ds.db2i.execute.Db2ForiMetaService;
import com.clougence.clouddm.ds.db2zos.execute.Db2ForZosMetaService;
import com.clougence.clouddm.ds.doris.execute.DrMetaService;
import com.clougence.clouddm.ds.gauss.execute.gs.GsMetaService;
import com.clougence.clouddm.ds.gauss.execute.gsog.GsogMetaService;
import com.clougence.clouddm.ds.goldendb.execute.mysql.GoldenDBMySQLMetaService;
import com.clougence.clouddm.ds.goldendb.execute.oracle.GoldenDBOracleMetaService;
import com.clougence.clouddm.ds.greenplum.execute.GpMetaService;
import com.clougence.clouddm.ds.hana.execute.HanaMetaService;
import com.clougence.clouddm.ds.hologres.execute.HgMetaService;
import com.clougence.clouddm.ds.kingbasees.execute.mysql.KingbaseESMySQLMetaService;
import com.clougence.clouddm.ds.kingbasees.execute.oracle.KingbaseESOracleMetaService;
import com.clougence.clouddm.ds.kingbasees.execute.postgresql.KingbaseESPostgreSQLMetaService;
import com.clougence.clouddm.ds.kingbasees.execute.sqlserver.KingbaseESSQLServerMetaService;
import com.clougence.clouddm.ds.mariadb.execute.MarMetaService;
import com.clougence.clouddm.ds.oceanbase.execute.ob4my.ObMetaService;
import com.clougence.clouddm.ds.oceanbase.execute.ob4ora.ObForOraMetaService;
import com.clougence.clouddm.ds.polardb.execute.pormy.PorMyMetaService;
import com.clougence.clouddm.ds.polardb.execute.porpg.PorPgMetaService;
import com.clougence.clouddm.ds.polardb.execute.porx.PorXMetaService;
import com.clougence.clouddm.ds.selectdb.execute.SelMetaService;
import com.clougence.clouddm.ds.starrocks.execute.SrMetaService;
import com.clougence.clouddm.ds.tidb.execute.TiMetaService;
import com.clougence.clouddm.ds.yashandb.execute.mysql.YashanDBMySQLMetaService;
import com.clougence.clouddm.ds.yashandb.execute.oracle.YashanDBOracleMetaService;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaService;
import com.clougence.clouddm.dsfamily.oracle.execute.OraMetaService;
import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaService;
import com.clougence.clouddm.dsfamily.sqlserver.execute.MsSqlMetaService;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbMetaService;

@RunWith(Parameterized.class)
public class SelectObjectContractTest extends SelectObjectContract {
    public SelectObjectContractTest(String datasource, Class<? extends DefaultRdbMetaService> metaServiceType, String version){
        super(metaServiceType, version);
    }

    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> datasources() {
        List<Object[]> datasources = new ArrayList<>();
        datasources.add(new Object[] { "AdsMyMetaService", AdsMyMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "CbMetaService", CbMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "ChMetaService", ChMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "CrdbMetaService", CrdbMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "Db2ForZosMetaService", Db2ForZosMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "Db2ForiMetaService", Db2ForiMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "DmMetaService", DmMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "DrMetaService", DrMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "GoldenDBMySQLMetaService", GoldenDBMySQLMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "GoldenDBOracleMetaService", GoldenDBOracleMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "GpMetaService", GpMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "GsMetaService", GsMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "GsogMetaService", GsogMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "HanaMetaService", HanaMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "HgMetaService", HgMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "KingbaseESMySQLMetaService", KingbaseESMySQLMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "KingbaseESOracleMetaService", KingbaseESOracleMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "KingbaseESPostgreSQLMetaService", KingbaseESPostgreSQLMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "KingbaseESSQLServerMetaService", KingbaseESSQLServerMetaService.class, "KingbaseES V009R001C010" });
        datasources.add(new Object[] { "MarMetaService", MarMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "MsSqlMetaService", MsSqlMetaService.class, "Microsoft SQL Server 2022" });
        datasources.add(new Object[] { "MyMetaService", MyMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "ObForOraMetaService", ObForOraMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "ObMetaService", ObMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "OraMetaService", OraMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "PgMetaService", PgMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "PorMyMetaService", PorMyMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "PorPgMetaService", PorPgMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "PorXMetaService", PorXMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "SelMetaService", SelMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "SrMetaService", SrMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "TiMetaService", TiMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "YashanDBMySQLMetaService", YashanDBMySQLMetaService.class, "17.0.0" });
        datasources.add(new Object[] { "YashanDBOracleMetaService", YashanDBOracleMetaService.class, "17.0.0" });
        return datasources;
    }
}
