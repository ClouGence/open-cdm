package com.clougence.clouddm.ds.clickhouse.execute;

import java.sql.Connection;

import com.clougence.clouddm.ds.clickhouse.dsconf.ChConfig;
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
public class ChSessionFactory extends RdbSessionFactory<ChConfig> {

    @Override
    protected Session newSession(ChConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        ChSession session = new ChSession(contextDTO.getSessionId(), dsConfig, dsObject);
        session.initSession(ownerRM, contextDTO);
        return session;
    }
}
