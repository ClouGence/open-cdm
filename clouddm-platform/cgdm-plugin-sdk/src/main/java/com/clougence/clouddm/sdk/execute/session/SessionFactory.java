package com.clougence.clouddm.sdk.execute.session;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;

@FunctionalInterface
public interface SessionFactory<C extends DataSourceConfig> {

    Session createSession(DsResourceManager rm, C dsConfig, SessionContextDTO contextDTO) throws Exception;
}
