package com.clougence.clouddm.ds.hologres.execute;

import java.sql.Connection;

import com.clougence.clouddm.ds.hologres.dsconf.HgConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionFactory;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.drivers.DsObject;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class HgSessionFactory extends RdbSessionFactory<HgConfig> {

    @Override
    protected Session newSession(HgConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        HgSession session = new HgSession(contextDTO.getSessionId(), dsConfig, dsObject);
        session.initSession(ownerRM, contextDTO);
        return session;
    }
}
