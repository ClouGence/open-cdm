package com.clougence.clouddm.ds.polardb.execute.porpg;

import com.clougence.clouddm.dsfamily.postgres.execute.PgHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class PorPgHooks extends PgHooks {

    @Override
    public DsMetaService createMetaService(Session session) {
        return new PorPgMetaService(session);
    }
}
