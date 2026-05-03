package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.clougence.drivers.adapter.AdapterConnManager;
import com.clougence.drivers.adapter.AdapterConnection;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.clouddm.ds.mongodb.parser.MongoDslProvider;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.AbstractMongoFunc;
import com.clougence.clouddm.ds.mongodb.parser.ast.commands.client.UserFunc;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.future.CgFuture;
import com.clougence.utils.future.CgFutureObj;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;

public class MongoConnection extends AdapterConnection {

    private final Connection    owner;
    private final MongoClient   client;
    private final ClientSession session;
    private String              database;

    static {
        DslHelper.register(new MongoDslProvider());
    }

    MongoConnection(Connection owner, MongoClient client, String jdbcUrl, Properties properties, String database){
        super(jdbcUrl, properties.getProperty(MongoKeys.USERNAME));
        this.owner = owner;
        this.client = client;
        this.session = client.startSession();
        this.database = database;
    }

    @Override
    public String getCatalog() { return this.getSchema(); }

    @Override
    public void setCatalog(String catalog) {
        this.setSchema(catalog);
    }

    @Override
    public String getSchema() { return String.valueOf(database); }

    @Override
    public void setSchema(String schema) { this.database = schema; }

    @Override
    public AdapterRequest newRequest(String sql) {
        return new MongoRequest(sql);
    }

    @Override
    protected <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface == MongoConnection.class) {
            return (T) this;
        } else if (iface == MongoClient.class) {
            return (T) this.client;
        } else {
            return super.unwrap(iface);
        }
    }

    public void killDriverConnection(String connID) throws SQLException {
        MongoConnection conn = (MongoConnection) AdapterConnManager.getConnection(connID);
        if (conn != null) {
            try {
                conn.close();
            } catch (Throwable e) {
                Throwable ee = ExceptionUtils.getRootCause(e);
                if (ee instanceof SQLException) {
                    throw (SQLException) ee;
                } else {
                    throw new SQLException(e);
                }
            }
        }
    }

    @Override
    public synchronized void doRequest(AdapterRequest request, AdapterReceive receive) throws SQLException {
        StatementSet statementSet = DslHelper.parserDsl(MongoDslProvider.INSTANCE, ((MongoRequest) request).getCommandBody());
        List<Statement> cmdSet = statementSet.getStatements();
        if (cmdSet.isEmpty()) {
            //            throw new SQLException("query command is empty.", JdbcErrorCode.SQL_STATE_QUERY_EMPTY);
            throw new UnsupportedOperationException("");
        }

        for (Statement command : cmdSet) {
            CgFuture<Object> sync = new CgFutureObj<>();
            execCommand(sync, this.client, this.session, (AbstractMongoFunc) command, request, receive);
            sync.await();
        }

        receive.responseFinish(request);
    }

    private void execCommand(CgFuture<Object> sync, MongoClient jedisCmd, ClientSession session, AbstractMongoFunc func, AdapterRequest request,
                             AdapterReceive receive) throws SQLException {
        switch (func.getFuncType()) {
            case USE: {
                this.database = ((UserFunc) func).getDatabase();
                receive.responseResult(request, MongoUtils.singleResult(request, MongoUtils.RESULT_COLUMN, "ok"));
                MongoUtils.completed(sync);
                break;
            }
            default: {
                try {
                    MongoDistributeCall.execFunc(sync, jedisCmd, session, func, request, receive, this.database);
                } catch (IOException e) {
                    throw new SQLException(e);
                }
            }
        }
    }

    @Override
    public void cancelRequest() {
        throw new UnsupportedOperationException("cancelRequest not support."); // TODO
    }

    @Override
    protected void doClose() throws IOException {
        this.cancelRequest();
        this.session.close();
        this.client.close();
    }
}
