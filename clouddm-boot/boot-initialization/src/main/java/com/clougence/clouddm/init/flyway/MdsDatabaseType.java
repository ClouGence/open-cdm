package com.clougence.clouddm.init.flyway;

import java.sql.Types;

import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.database.base.Database;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.flywaydb.database.mysql.MySQLDatabaseType;

public class MdsDatabaseType extends MySQLDatabaseType {

    public String getName() { return "Mds"; }

    @Override
    public int getPriority() {
        // needs to be checked in advance of MySql
        return 1;
    }

    @Override
    public int getNullType() { return Types.VARCHAR; }

    @Override
    public boolean handlesJDBCUrl(String url) {
        return url.startsWith("jdbc:mysql:");
    }

    @Override
    public String getDriverClass(String url, ClassLoader classLoader) {
        return super.getDriverClass(url, classLoader);
    }

    @Override
    public String getBackupDriverClass(String url, ClassLoader classLoader) {
        return super.getBackupDriverClass(url, classLoader);
    }

    @Override
    public Database createDatabase(Configuration configuration, JdbcConnectionFactory jdbcConnectionFactory, StatementInterceptor statementInterceptor) {
        return new MdsDatabase(configuration, jdbcConnectionFactory, statementInterceptor);
    }
}
