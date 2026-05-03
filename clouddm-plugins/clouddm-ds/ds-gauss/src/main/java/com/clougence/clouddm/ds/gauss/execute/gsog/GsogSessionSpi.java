package com.clougence.clouddm.ds.gauss.execute.gsog;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionSpi;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;

/**
 * https://docs.vmware.com/en/VMware-Greenplum/7/greenplum-database/admin_guide-intro-about_mvcc.html
 * @author mode 2021/4/25 15:13
 */
public class GsogSessionSpi extends RdbSessionSpi {

    @Override
    public SessionContextDTO createSessionContext(DataSourceConfig dsConfig, Map<String, Object> params) {
        SessionContextDTO contextDTO = super.createSessionContext(dsConfig, params);

        if (contextDTO.getRdbTxIsolation() == null || contextDTO.getRdbTxIsolation() == RdbIsolation.DEFAULT) {
            contextDTO.setRdbTxIsolation(RdbIsolation.READ_COMMITTED);
        }
        return contextDTO;
    }
}
