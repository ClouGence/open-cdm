package com.clougence.clouddm.ds.gauss.definition.gs.ui.ddl;

import com.clougence.clouddm.dsfamily.postgres.definition.ui.ddl.PgFamilyConvertTableDDLSpi;
import com.clougence.clouddm.sdk.ui.ddl.DDLType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

/**
 * @Author: mode
 * @Date: 2024-01-22 10:10
 */
public class GsConvertTableDDLSpi extends PgFamilyConvertTableDDLSpi {

    public GsConvertTableDDLSpi(){
        // for PG Family
        targetList.add(DataSourceType.PostgreSQL);
        targetList.add(DataSourceType.Greenplum);
        targetList.add(DataSourceType.PolarDBPg);
        targetList.add(DataSourceType.GaussDB);
        targetList.add(DataSourceType.GaussDBForOpenGauss);
        // for MySQL Family
        targetList.add(DataSourceType.MySQL);
        targetList.add(DataSourceType.PolarDbX);
        targetList.add(DataSourceType.PolarDbMySQL);
        targetList.add(DataSourceType.TiDB);
        targetList.add(DataSourceType.OceanBase);
        // for Oracle Family
        targetList.add(DataSourceType.Oracle);
        targetList.add(DataSourceType.ObForOracle);
        targetList.add(DataSourceType.Dameng);
        // for Sr/Dr Family
        targetList.add(DataSourceType.Doris);
        targetList.add(DataSourceType.SelectDB);
        targetList.add(DataSourceType.StarRocks);
        // for Other Ds
        targetList.add(DataSourceType.ClickHouse);
        targetList.add(DataSourceType.SQLServer);
        targetList.add(DataSourceType.Hana);
        targetList.add(DataSourceType.Db2);

        // ddlType
        ddlTypeList.add(DDLType.Convert);
    }
}
