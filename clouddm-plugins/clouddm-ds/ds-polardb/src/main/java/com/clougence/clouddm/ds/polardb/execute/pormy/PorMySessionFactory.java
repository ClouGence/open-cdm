package com.clougence.clouddm.ds.polardb.execute.pormy;

import java.sql.Connection;

import com.clougence.clouddm.ds.polardb.dsconf.pormy.PorMyConfig;
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
public class PorMySessionFactory extends RdbSessionFactory<PorMyConfig> {

    @Override
    protected Session newSession(PorMyConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        PorMySession session = new PorMySession(contextDTO.getSessionId(), dsConfig, dsObject);
        session.initSession(ownerRM, contextDTO);
        return session;
    }
}
