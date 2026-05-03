package com.clougence.clouddm.ds.selectdb.execute;

import java.sql.Connection;

import com.clougence.clouddm.ds.selectdb.dsconf.SelConfig;
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
public class SelSessionFactory extends RdbSessionFactory<SelConfig> {

    @Override
    protected Session newSession(SelConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        SelSession session = new SelSession(contextDTO.getSessionId(), dsConfig, dsObject);
        session.initSession(ownerRM, contextDTO);
        return session;
    }

}
