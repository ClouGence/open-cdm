package com.clougence.clouddm.ds.dameng.execute;

import java.sql.Connection;

import com.clougence.clouddm.ds.dameng.dsconf.DmConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionFactory;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.drivers.DsObject;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityFileType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class DmSessionFactory extends RdbSessionFactory<DmConfig> {

    @Override
    protected Session newSession(DmConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception {
        DmSession session = new DmSession(contextDTO.getSessionId(), dsConfig, dsObject);
        session.initSession(ownerRM, contextDTO);
        return session;
    }

    @Override
    protected void configSSL(DmConfig dsConfig, DsResourceManager resRM) throws Exception {
        if (dsConfig.getSecurityType() != SecurityType.USER_PASSWD_WITH_KEYSTORE) {
            return;
        }

        String filePath = resRM.getSecurityFilePath(dsConfig, SecurityFileType.keystore_file.name(), SecurityFileType.keystore_file);
        dsConfig.setSslFilePath(filePath);

    }
}
