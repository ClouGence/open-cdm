package com.clougence.clouddm.ds.oceanbase.execute.obforora;

import java.sql.Connection;

import com.clougence.clouddm.ds.oceanbase.dsconf.obforora.ObOraConfig;
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
public class ObForOraSessionFactory extends RdbSessionFactory<ObOraConfig> {

    @Override
    protected Session newSession(ObOraConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        ObForOraSession session = new ObForOraSession(contextDTO.getSessionId(), dsConfig, dsObject);
        session.initSession(ownerRM, contextDTO);
        return session;
    }

}
