package com.clougence.clouddm.ds.sqlserver.execute;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.sqlserver.dsconf.MsSqlConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionSpi;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.utils.StringUtils;

/**
 * https://learn.microsoft.com/en-us/sql/t-sql/statements/set-transaction-isolation-level-transact-sql?view=sql-server-ver16
 * @author mode 2021/4/25 15:13
 */
public class MsSqlSessionSpi extends RdbSessionSpi {

    @Override
    public SessionContextDTO createSessionContext(DataSourceConfig dsConfig, Map<String, Object> params) {
        SessionContextDTO contextDTO = super.createSessionContext(dsConfig, params);
        MsSqlConfig sqlServerConfig = (MsSqlConfig) dsConfig;
        if (StringUtils.isBlank((String) params.get(PARAMS_DEFAULT_DB)) && StringUtils.isNotBlank(sqlServerConfig.getDefaultDataBase())) {
            contextDTO.setRdbCatalog(sqlServerConfig.getDefaultDataBase());
        }

        if (contextDTO.getRdbTxIsolation() == null || contextDTO.getRdbTxIsolation() == RdbIsolation.DEFAULT) {
            // - SQL SERVER              : READ_COMMITTED
            // - Azure Synapse Analytics : READ_UNCOMMITTED
            contextDTO.setRdbTxIsolation(RdbIsolation.READ_COMMITTED);
        }
        return contextDTO;
    }
}
