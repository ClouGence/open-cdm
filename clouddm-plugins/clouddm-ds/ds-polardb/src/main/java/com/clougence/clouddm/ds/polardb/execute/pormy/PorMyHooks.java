package com.clougence.clouddm.ds.polardb.execute.pormy;

import com.clougence.clouddm.dsfamily.mysql.execute.MyHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class PorMyHooks extends MyHooks {

    public PorMyHooks(String mainVersion){
        super(mainVersion);
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new PorMyMetaService(session);
    }
}
