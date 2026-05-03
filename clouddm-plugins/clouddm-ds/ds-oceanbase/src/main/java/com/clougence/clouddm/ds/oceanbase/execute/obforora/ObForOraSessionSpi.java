package com.clougence.clouddm.ds.oceanbase.execute.obforora;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionSpi;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;

/**
 * https://en.oceanbase.com/docs/enterprise-oceanbase-database-en-10000000000383474
 * @author mode 2021/4/25 15:13
 */
public class ObForOraSessionSpi extends RdbSessionSpi {

    @Override
    public SessionContextDTO createSessionContext(DataSourceConfig dsConfig, Map<String, Object> params) {
        SessionContextDTO contextDTO = super.createSessionContext(dsConfig, params);

        //MySQL mode
        //  Read committed
        //  Repeatable read
        //Oracle mode
        //  Read committed
        //  Repeatable read
        //  Serializable
        if (contextDTO.getRdbTxIsolation() == null || contextDTO.getRdbTxIsolation() == RdbIsolation.DEFAULT) {
            contextDTO.setRdbTxIsolation(RdbIsolation.READ_COMMITTED);
        }
        return contextDTO;
    }
}
