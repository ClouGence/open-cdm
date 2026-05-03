package com.clougence.clouddm.init.flyway;

import java.sql.Connection;

import org.flywaydb.database.mysql.MySQLConnection;
import org.flywaydb.database.mysql.MySQLDatabase;

public class MdsConnection extends MySQLConnection {

    public MdsConnection(MySQLDatabase database, Connection connection){
        super(database, connection);
    }

    @Override
    protected boolean canUseNamedLockTemplate() {
        return false;
    }
}
