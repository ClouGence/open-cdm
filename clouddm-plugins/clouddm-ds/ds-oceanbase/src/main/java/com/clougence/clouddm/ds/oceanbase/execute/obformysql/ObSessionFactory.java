package com.clougence.clouddm.ds.oceanbase.execute.obformysql;

import java.sql.Connection;

import com.clougence.clouddm.ds.oceanbase.dsconf.obformysql.ObConfig;
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
public class ObSessionFactory extends RdbSessionFactory<ObConfig> {

    @Override
    protected Session newSession(ObConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        ObSession session = new ObSession(contextDTO.getSessionId(), dsConfig, dsObject);
        session.initSession(ownerRM, contextDTO);
        return session;
    }

}
