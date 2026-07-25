package com.clougence.clouddm.dsfamily.mysql.execute;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.dsconf.SslConfig;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.drivers.DsObject;
import com.clougence.utils.timer.HashedWheelTimer;
import com.clougence.utils.timer.Timer;

public class MySessionParserParametersTest {

    private static final String URL_PROPERTY = "mysql.session.test.url";
    private static final String USER_PROPERTY = "mysql.session.test.user";
    private static final String PASSWORD_PROPERTY = "mysql.session.test.password";

    @Test
    public void probeReadsActualVersionAndSessionSqlMode() throws Exception {
        try (Connection connection = newConnection()) {
            SqlParserParameters parameters = getSqlParserParameters(connection);

            Assertions.assertNotNull(parameters);
            Assertions.assertEquals(queryValue(connection, "SELECT VERSION()"), parameters.version());
            Assertions.assertTrue(parameters.contains(SqlParserParameters.SQL_MODE));
            Assertions.assertEquals(queryValue(connection, "SELECT @@SESSION.sql_mode"), parameters.get(SqlParserParameters.SQL_MODE));
        }
    }

    @Test
    public void emptySqlModeRemainsKnownEmpty() throws Exception {
        try (Connection connection = newConnection(); Statement statement = connection.createStatement()) {
            statement.execute("SET SESSION sql_mode = ''");

            SqlParserParameters parameters = getSqlParserParameters(connection);

            Assertions.assertTrue(parameters.contains(SqlParserParameters.SQL_MODE));
            Assertions.assertEquals("", parameters.get(SqlParserParameters.SQL_MODE));
        }
    }

    @Test
    public void sessionsOnTheSameDatasourceKeepIndependentSqlModes() throws Exception {
        try (Connection first = newConnection();
             Connection second = newConnection();
             Statement firstStatement = first.createStatement();
             Statement secondStatement = second.createStatement()) {
            firstStatement.execute("SET SESSION sql_mode = 'ANSI_QUOTES'");
            secondStatement.execute("SET SESSION sql_mode = 'PIPES_AS_CONCAT'");

            SqlParserParameters firstParameters = getSqlParserParameters(first);
            SqlParserParameters secondParameters = getSqlParserParameters(second);

            Assertions.assertEquals("ANSI_QUOTES", firstParameters.get(SqlParserParameters.SQL_MODE));
            Assertions.assertEquals("PIPES_AS_CONCAT", secondParameters.get(SqlParserParameters.SQL_MODE));
        }
    }

    @Test
    public void setExpressionsAndReconnectAreConfirmedFromTheConnection() throws Exception {
        SqlParserParameters oldSnapshot;
        try (Connection connection = newConnection(); Statement statement = connection.createStatement()) {
            statement.execute("SET SESSION sql_mode = 'ANSI_QUOTES'");
            statement.execute("SET @@SESSION.sql_mode = CONCAT(@@SESSION.sql_mode, ',PIPES_AS_CONCAT')");
            oldSnapshot = getSqlParserParameters(connection);
            Assertions.assertEquals(queryValue(connection, "SELECT @@SESSION.sql_mode"), oldSnapshot.get(SqlParserParameters.SQL_MODE));

            statement.execute("SET SESSION sql_mode = DEFAULT");
            SqlParserParameters defaultParameters = getSqlParserParameters(connection);
            Assertions.assertEquals(queryValue(connection, "SELECT @@SESSION.sql_mode"), defaultParameters.get(SqlParserParameters.SQL_MODE));
        }

        try (Connection replacement = newConnection()) {
            SqlParserParameters replacementParameters = getSqlParserParameters(replacement);
            Assertions.assertEquals(queryValue(replacement, "SELECT VERSION()"), replacementParameters.version());
            Assertions.assertEquals(queryValue(replacement, "SELECT @@SESSION.sql_mode"), replacementParameters.get(SqlParserParameters.SQL_MODE));
            Assertions.assertNotSame(oldSnapshot, replacementParameters);
        }
    }

    @Test
    public void metaServiceRefreshesActualConnectionWithoutMySessionMutatingSnapshot() throws Exception {
        Connection connection = newConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute("SET SESSION sql_mode = ''");
        }
        String expectedVersion = queryValue(connection, "SELECT VERSION()");

        HashedWheelTimer timer = new HashedWheelTimer();
        DsObject<Connection> connectionObject = new DsObject<>(new Properties(), connection, null);
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setVersion("stale-datasource-version");
        SessionContextDTO context = new SessionContextDTO();
        context.setSessionId("same-session-id");
        context.setRdbAutoCommit(true);
        context.setSqlParameters(Map.of(
                SqlParserParameters.VERSION, "old-version",
                SqlParserParameters.SQL_MODE, "ANSI_QUOTES"));

        MySession session = new MySession(context.getSessionId(), dataSourceConfig, connectionObject);
        try {
            session.initSession(resourceManager(timer), context);

            SqlParserParameters persistedParameters = new SqlParserParameters(context.getSqlParameters());
            Assertions.assertEquals("old-version", persistedParameters.version());
            Assertions.assertEquals("ANSI_QUOTES", persistedParameters.get(SqlParserParameters.SQL_MODE));

            SqlParserParameters actualParameters = new SqlParserParameters(session.getMetaService().getSqlParserParameters());
            Assertions.assertEquals(expectedVersion, actualParameters.version());
            Assertions.assertTrue(actualParameters.contains(SqlParserParameters.SQL_MODE));
            Assertions.assertEquals("", actualParameters.get(SqlParserParameters.SQL_MODE));
        } finally {
            session.close();
            timer.stop();
        }
    }

    private static Connection newConnection() throws Exception {
        String url = System.getProperty(URL_PROPERTY, System.getenv("MYSQL_SESSION_TEST_URL"));
        Assumptions.assumeTrue(url != null && !url.isBlank(), "Set -D" + URL_PROPERTY + " to run the MySQL Session integration tests.");
        String user = System.getProperty(USER_PROPERTY, System.getenv().getOrDefault("MYSQL_SESSION_TEST_USER", "root"));
        String password = System.getProperty(PASSWORD_PROPERTY, System.getenv().getOrDefault("MYSQL_SESSION_TEST_PASSWORD", ""));
        return DriverManager.getConnection(url, user, password);
    }

    private static SqlParserParameters getSqlParserParameters(Connection connection) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        DsObject<Connection> connectionObject = new DsObject<>(new Properties(), connection, null);
        MySession session = new MySession("parser-parameters-test", dataSourceConfig, connectionObject);
        return new SqlParserParameters(new MyMetaService(session).getSqlParserParameters());
    }

    private static String queryValue(Connection connection, String sql) throws Exception {
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            Assertions.assertTrue(resultSet.next());
            return resultSet.getString(1);
        }
    }

    private static DsResourceManager resourceManager(Timer timer) {
        return new DsResourceManager() {
            @Override
            public <C extends DataSourceConfig> Timer getTimer(C dbConfig) {
                return timer;
            }

            @Override
            public <C extends DataSourceConfig> Executor getExecutor(C dbConfig) {
                return Runnable::run;
            }

            @Override
            public <C extends DataSourceConfig, T extends AutoCloseable> DsObject<T> requestResource(C dbConfig) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isTask() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public SslConfig fetchSslConfig(DataSourceConfig config) {
                return null;
            }
        };
    }
}
