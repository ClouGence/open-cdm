package com.clougence.clouddm.ds.mongodb.execute;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.mongodb.dsconf.MongoConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionSpi;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/4/25 15:13
 */
public class MongoSessionSpi extends RdbSessionSpi {

    @Override
    public SessionContextDTO createSessionContext(DataSourceConfig dsConfig, Map<String, Object> params) {
        SessionContextDTO contextDTO = super.createSessionContext(dsConfig, params);
        MongoConfig config = (MongoConfig) dsConfig;
        if (StringUtils.isBlank((String) params.get(PARAMS_DEFAULT_SCHEMA)) && StringUtils.isNotBlank(config.getDefaultSchema())) {
            contextDTO.setRdbSchema(config.getDefaultSchema());
        }

        if (contextDTO.getRdbTxIsolation() == null || contextDTO.getRdbTxIsolation() == RdbIsolation.DEFAULT) {
            contextDTO.setRdbTxIsolation(RdbIsolation.DEFAULT);
        }
        return contextDTO;
    }
}
