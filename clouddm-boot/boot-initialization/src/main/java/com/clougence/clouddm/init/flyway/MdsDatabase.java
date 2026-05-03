package com.clougence.clouddm.init.flyway;

import java.sql.Connection;

import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.flywaydb.database.mysql.MySQLConnection;
import org.flywaydb.database.mysql.MySQLDatabase;

public class MdsDatabase extends MySQLDatabase {

    public MdsDatabase(Configuration configuration, JdbcConnectionFactory jdbcConnectionFactory, StatementInterceptor statementInterceptor){
        super(configuration, jdbcConnectionFactory, statementInterceptor);
    }

    @Override
    protected MySQLConnection doGetConnection(Connection connection) {
        return new MdsConnection(this, connection);
    }

    @Override
    protected boolean isCreateTableAsSelectAllowed() { return false; }

    @Override
    protected MigrationVersion determineVersion() {
        return MigrationVersion.fromVersion("8.0.1");
    }
}
