/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.ds.valkey.execute.jdbc;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.clougence.drivers.adapter.*;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.sql.redis.parser.RedisDslProvider;
import com.clougence.sql.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.sql.redis.parser.ast.commands.client.SelectRedisCmd;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.future.CgFuture;
import com.clougence.utils.future.CgFutureObj;

public class GlideConnection extends AdapterConnection {

    private final Connection owner;
    private final GlideCmd   glideCmd;
    private int              database;
    private volatile boolean cancelled = false;

    GlideConnection(Connection owner, GlideCmd glideCmd, String jdbcUrl, Map<String, String> properties, int database){
        super(jdbcUrl, properties.get(GlideKeys.USERNAME));
        this.owner = owner;
        this.glideCmd = glideCmd;
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
    public void setSchema(String schema) {
        int newDatabase = Integer.parseInt(schema);
        if (this.database != newDatabase) {
            try {
                this.glideCmd.select(newDatabase);
                this.database = newDatabase;
            } catch (SQLException e) {
                throw new RuntimeException("select database failed: " + newDatabase, e);
            }
        }
    }

    @Override
    public AdapterRequest newRequest(String sql) {
        return new GlideRequest(sql);
    }

    @Override
    protected <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface == GlideConnection.class) {
            return (T) this;
        } else if (iface == GlideCmd.class) {
            return (T) this.glideCmd;
        } else {
            return super.unwrap(iface);
        }
    }

    public void killDriverConnection(String connID) throws SQLException {
        GlideConnection conn = (GlideConnection) AdapterConnManager.getConnection(connID);
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
        if (StringUtils.isBlank(((GlideRequest) request).getCommandBody())) {
            throw new SQLException("query command is empty.", JdbcErrorCode.SQL_STATE_QUERY_EMPTY);
        }

        this.cancelled = false;
        StatementSet statementSet;
        try (StringReader reader = new StringReader(((GlideRequest) request).getCommandBody())) {
            statementSet = DslHelper.parserDsl(RedisDslProvider.INSTANCE, reader);
        } catch (Exception e) {
            String errorMsg = "command '" + ((GlideRequest) request).getCommandBody() + "' parserFailed.')";
            throw new SQLException(errorMsg, JdbcErrorCode.SQL_STATE_SYNTAX_ERROR);
        }

        List<Statement> cmdSet = statementSet.getStatements();
        if (cmdSet.isEmpty()) {
            throw new SQLException("query command is empty.", JdbcErrorCode.SQL_STATE_QUERY_EMPTY);
        }

        for (Statement redisCmd : cmdSet) {
            if (this.cancelled) {
                throw new SQLException("Operation cancelled.", JdbcErrorCode.SQL_STATE_IS_CANCELLED);
            }
            try {
                CgFuture<Object> sync = new CgFutureObj<>();
                execRedisCmd(sync, this.glideCmd, (AbstractRedisCmd) redisCmd, request, receive);
                sync.await();
            } catch (SQLException e) {
                throw e;
            }
        }

        receive.responseFinish(request);
    }

    private void execRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, AbstractRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        switch (command.getCmdType()) {
            case SELECT: {
                int newDatabase = GlideUtils.argAsInt(request, ((SelectRedisCmd) command).getDatabase());
                String status = glideCmd.select(newDatabase);

                if (StringUtils.equalsIgnoreCase(status, "ok")) {
                    receive.responseResult(request, GlideUtils.singleResult(request, GlideUtils.COL_VALUE_LONG, status));
                    this.database = newDatabase;
                    GlideUtils.completed(sync);
                    return;
                } else {
                    throw new SQLException("select database failed, status: " + status);
                }
            }
            default: {
                GlideDistributeCall.execRedisCmd(sync, glideCmd, command, request, receive);
            }
        }
    }

    @Override
    public void cancelRequest() {
        this.cancelled = true;
    }

    @Override
    protected void doClose() throws IOException {
        this.cancelRequest();
        this.glideCmd.close();
    }
}
