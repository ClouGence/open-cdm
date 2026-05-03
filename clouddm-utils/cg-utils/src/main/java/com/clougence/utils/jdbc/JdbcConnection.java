package com.clougence.utils.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @version : 2013-10-16
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class JdbcConnection {

    private DataSource dataSource;
    private Connection connection;
    private int        fetchSize    = 0;
    private int        maxRows      = 0;
    private int        queryTimeout = 0;

    /**
     * Construct a new JdbcConnection for bean usage.
     * <p>Note: The DataSource has to be set before using the instance.
     * @see #setDataSource
     */
    public JdbcConnection(){
    }

    /**
     * Construct a new JdbcConnection, given a DataSource to obtain connections from.
     * <p>Note: This will not trigger initialization of the exception translator.
     * @param dataSource the JDBC DataSource to obtain connections from
     */
    public JdbcConnection(final DataSource dataSource){
        this.setDataSource(dataSource);
    }

    /**
     * Construct a new JdbcConnection, given a Connection to obtain connections from.
     * <p>Note: This will not trigger initialization of the exception translator.
     * @param conn the JDBC Connection
     */
    public JdbcConnection(final Connection conn){
        this.setConnection(conn);
    }

    /**Return the DataSource used by this template.*/
    public DataSource getDataSource() { return this.dataSource; }

    /**Set the JDBC DataSource to obtain connections from.*/
    public void setDataSource(final DataSource dataSource) { this.dataSource = dataSource; }

    /**Return the Connection used by this template.*/
    public Connection getConnection() { return this.connection; }

    /**Set the JDBC Connection to obtain connection from.*/
    public void setConnection(final Connection connection) { this.connection = connection; }

    public int getFetchSize() { return this.fetchSize; }

    public void setFetchSize(final int fetchSize) { this.fetchSize = fetchSize; }

    public int getMaxRows() { return this.maxRows; }

    public void setMaxRows(final int maxRows) { this.maxRows = maxRows; }

    public int getQueryTimeout() { return this.queryTimeout; }

    public void setQueryTimeout(final int queryTimeout) { this.queryTimeout = queryTimeout; }

    public <T> T execute(final ConnectionCallback<T> action) throws SQLException {
        Objects.requireNonNull(action, "Callback object must not be null");

        Connection localConn = this.getConnection();
        DataSource localDS = this.getDataSource();
        boolean usingConn = (localConn != null);
        boolean usingDS = !usingConn && (localDS != null);

        if (!usingConn && !usingDS) {
            throw new IllegalArgumentException("Connection unavailable, any of (Connection/DataSource) is required.");
        }
        if (log.isDebugEnabled()) {
            log.trace("database connection using " + (usingConn ? "connection" : "dataSource"));
        }

        Connection useConn;
        if (usingConn) {
            useConn = localConn;
        } else {
            useConn = localDS.getConnection();
        }

        try {
            return action.doInConnection(useConn);
        } finally {
            if (!usingConn) {
                useConn.close();
            }
        }
    }

    /** apply Statement: fetchSize、maxRows、Timeout */
    protected void applyStatementSettings(final Statement stmt) throws SQLException {
        int fetchSize = this.getFetchSize();
        if (fetchSize > 0) {
            stmt.setFetchSize(fetchSize);
        }
        int maxRows = this.getMaxRows();
        if (maxRows > 0) {
            stmt.setMaxRows(maxRows);
        }
        int timeout = this.getQueryTimeout();
        if (timeout > 0) {
            stmt.setQueryTimeout(timeout);
        }
    }
}
