package com.clougence.clouddm.ds.gauss.execute.gsog;

import java.sql.Connection;

import com.clougence.clouddm.ds.gauss.dsconf.gsog.GsogConfig;
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
public class GsogSessionFactory extends RdbSessionFactory<GsogConfig> {

    @Override
    protected Session newSession(GsogConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        GsogSession session = new GsogSession(contextDTO.getSessionId(), dsConfig, dsObject);
        session.initSession(ownerRM, contextDTO);
        return session;
    }
}
