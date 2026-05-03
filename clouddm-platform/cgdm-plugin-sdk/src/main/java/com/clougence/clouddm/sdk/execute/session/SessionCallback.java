package com.clougence.clouddm.sdk.execute.session;

import java.sql.Connection;

@FunctionalInterface
public interface SessionCallback<V> {

    V doCallback(Connection con) throws Exception;
}
