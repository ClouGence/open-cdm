package com.clougence.clouddm.ds.maxcompute.execute;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionSpi;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;

/**
 * https://eco.dameng.com/document/dm/zh-cn/pm/management-affairs#19.7%20%E4%BA%8B%E5%8A%A1%E9%9A%94%E7%A6%BB%E7%BA%A7
 * @author mode 2021/4/25 15:13
 */
public class McSessionSpi extends RdbSessionSpi {

    @Override
    public SessionContextDTO createSessionContext(DataSourceConfig dsConfig, Map<String, Object> params) {
        SessionContextDTO contextDTO = super.createSessionContext(dsConfig, params);

        if (contextDTO.getRdbTxIsolation() == null || contextDTO.getRdbTxIsolation() == RdbIsolation.DEFAULT) {
            contextDTO.setRdbTxIsolation(RdbIsolation.READ_COMMITTED);
        }
        return contextDTO;
    }
}
