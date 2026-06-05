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
package com.clougence.clouddm.ds.postgres.definition.ui.ddl;

import com.clougence.clouddm.dsfamily.postgres.definition.ui.ddl.PgFamilyConvertTableDDLSpi;
import com.clougence.clouddm.sdk.ui.ddl.DDLType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;

/**
 * @Author: mode
 * @Date: 2024-01-22 10:10
 */
public class PgConvertTableDDLSpi extends PgFamilyConvertTableDDLSpi {

    public PgConvertTableDDLSpi(){
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
