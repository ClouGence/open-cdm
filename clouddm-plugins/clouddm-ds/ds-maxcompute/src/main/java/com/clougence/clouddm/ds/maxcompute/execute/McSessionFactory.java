package com.clougence.clouddm.ds.maxcompute.execute;

import java.sql.Connection;

import com.clougence.clouddm.ds.maxcompute.dsconf.McConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionFactory;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.drivers.DsObject;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class McSessionFactory extends RdbSessionFactory<McConfig> {

    @Override
    protected Session newSession(McConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        McSession session = new McSession(contextDTO.getSessionId(), dsConfig, dsObject);
        session.initSession(ownerRM, contextDTO);
        return session;
    }

    @Override
    protected void configSSL(McConfig dsConfig, DsResourceManager resRM) throws Exception {
        if (dsConfig.getSecurityType() != SecurityType.USER_PASSWD_WITH_KEYSTORE) {
            return;
        }
    }
}
