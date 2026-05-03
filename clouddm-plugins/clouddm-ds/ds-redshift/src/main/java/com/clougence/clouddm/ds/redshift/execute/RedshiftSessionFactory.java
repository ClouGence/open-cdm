package com.clougence.clouddm.ds.redshift.execute;

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.rdb.postgres.PostgresConfig;
import com.clougence.clouddm.dsfamily.execute.RdbSessionFactory;
import com.clougence.clouddm.dsfamily.postgres.execute.PostgresSession;
import com.clougence.clouddm.sdk.execute.resource.ResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.drivers.api.DsObject;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public class RedshiftSessionFactory extends RdbSessionFactory<PostgresConfig> {

    @Override
    protected Session<Connection> newSession(PostgresConfig dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, ResourceManager ownerRM) throws Exception {
        PostgresSession session = new PostgresSession(contextDTO.getSessionId(), dsConfig, dsObject);
        session.initSession(ownerRM, contextDTO);
        return session;
    }
}
