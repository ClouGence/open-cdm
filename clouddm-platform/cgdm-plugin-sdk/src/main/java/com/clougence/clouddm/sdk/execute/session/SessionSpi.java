package com.clougence.clouddm.sdk.execute.session;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.Spi;

public interface SessionSpi extends Spi {

    String PARAMS_DEFAULT_DB     = "DEFAULT_DB";
    String PARAMS_DEFAULT_SCHEMA = "DEFAULT_SCHEMA";

    SessionContextDTO createSessionContext(DataSourceConfig dsConfig, Map<String, Object> params);

    QueryRequest createQueryRequest(SessionContextDTO contextDTO, DataSourceConfig dsConfig, Map<String, Object> params, String uid, String clientIp, boolean console);

    String newQueryId();
}
