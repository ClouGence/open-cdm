package com.clougence.clouddm.ds.oracle.execute;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionSpi;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;

/**
 * https://docs.oracle.com/javadb/10.8.3.0/devguide/cdevconcepts15366.html
 * @author mode 2021/4/25 15:13
 */
public class OraSessionSpi extends RdbSessionSpi {

    @Override
    public SessionContextDTO createSessionContext(DataSourceConfig dsConfig, Map<String, Object> params) {
        SessionContextDTO contextDTO = super.createSessionContext(dsConfig, params);

        if (contextDTO.getRdbTxIsolation() == null || contextDTO.getRdbTxIsolation() == RdbIsolation.DEFAULT) {
            contextDTO.setRdbTxIsolation(RdbIsolation.READ_COMMITTED);
        }
        return contextDTO;
    }
}
