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
package com.clougence.clouddm.ds.yashandb.execute.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.clougence.clouddm.dsfamily.mysql.execute.MyHooks;
import com.clougence.clouddm.sdk.execute.meta.DsMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;

import lombok.extern.slf4j.Slf4j;

/**
 * Session hook for the YashanDB MySQL compatibility mode.
 * <p>
 * Session behaviour such as transactions, isolation level and fetch size follows the MySQL implementation
 * (the YashanDB MySQL compatibility mode provides standard MySQL semantics on the MySQL protocol port); the
 * differences and the metadata service are replaced by the YashanDB implementation.
 * </p>
 *
 * @author open-cdm
 */
@Slf4j
public class YashanDBMySQLHooks extends MyHooks {

    /**
     * Equivalent way to obtain the current session identifier, because the YashanDB MySQL compatibility mode
     * does not implement {@code CONNECTION_ID()}.
     * <p>
     * Measured on 23.4.1.109: {@code SELECT CONNECTION_ID()} raises {@code YAS-04243 invalid identifier}, and
     * {@code @@connection_id} / {@code @@pseudo_thread_id} raise {@code YAS-04607 unknown system variable};
     * whereas {@code SYS_CONTEXT('USERENV','SID')} returns the same value as the first column of
     * {@code SHOW PROCESSLIST} (the same session always yields the same number). It is therefore used as the
     * fallback source.
     * </p>
     */
    private static final String SESSION_ID_SQL = "SELECT SYS_CONTEXT('USERENV','SID') FROM DUAL";

    public YashanDBMySQLHooks(String mainVersion){
        super(mainVersion);
    }

    @Override
    public DsMetaService createMetaService(Session session) {
        return new YashanDBMySQLMetaService(session);
    }

    /**
     * During session initialization {@code DefaultRdbSession} calls this method to obtain the connection-level
     * query identifier; an exception here fails the whole connection, so the native MySQL form is downgraded to
     * a compatible fallback.
     */
    @Override
    public String getQueryID(Connection conn) throws SQLException {
        try {
            return super.getQueryID(conn);
        } catch (SQLException e) {
            log.error("YashanDB(MySQL mode) does not implement CONNECTION_ID(), fallback to SYS_CONTEXT: {}", e.getMessage());
        }

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(SESSION_ID_SQL)) {
            return resultSet.next() ? resultSet.getString(1) : null;
        }
    }
}
