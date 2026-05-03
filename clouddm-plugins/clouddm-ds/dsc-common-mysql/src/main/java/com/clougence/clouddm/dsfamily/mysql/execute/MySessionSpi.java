package com.clougence.clouddm.dsfamily.mysql.execute;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionSpi;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;

/**
 * https://dev.mysql.com/doc/refman/8.0/en/innodb-transaction-isolation-levels.html
 * @author mode 2021/4/25 15:13
 */
public class MySessionSpi extends RdbSessionSpi {

    @Override
    public SessionContextDTO createSessionContext(DataSourceConfig dsConfig, Map<String, Object> params) {
        SessionContextDTO contextDTO = super.createSessionContext(dsConfig, params);

        if (contextDTO.getRdbTxIsolation() == null || contextDTO.getRdbTxIsolation() == RdbIsolation.DEFAULT) {
            contextDTO.setRdbTxIsolation(RdbIsolation.REPEATABLE_READ);
        }
        return contextDTO;
    }
}
