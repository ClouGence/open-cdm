package com.clougence.clouddm.ds.starrocks.execute;

import java.sql.Connection;

import com.clougence.clouddm.ds.starrocks.dsconf.SrConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionFactory;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.drivers.DsObject;

/**
 * only for integration test
 *
 * @author Ekko 2022/11/03 16:48
 **/
public class SrSessionFactory extends RdbSessionFactory<SrConfig> {

    @Override
    protected Session newSession(SrConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        SrSession session = new SrSession(contextDTO.getSessionId(), dsConfig, dsObject);
        session.initSession(ownerRM, contextDTO);
        return session;
    }
}
