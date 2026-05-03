package com.clougence.clouddm.ds.selectdb.execute;

import com.clougence.clouddm.ds.doris.execute.DrHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;

import lombok.extern.slf4j.Slf4j;

/**
 * only for integration test
 *
 * @author Ekko 2022/11/03 16:48
 **/
@Slf4j
public class SelHooks extends DrHooks {

    public SelHooks(String mainVersion){
        super(mainVersion);
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new SelMetaService(session);
    }
}
