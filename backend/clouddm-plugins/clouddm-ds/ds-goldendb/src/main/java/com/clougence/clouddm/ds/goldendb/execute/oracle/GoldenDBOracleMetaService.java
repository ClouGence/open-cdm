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
package com.clougence.clouddm.ds.goldendb.execute.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

import com.clougence.clouddm.dsfamily.oracle.execute.OraMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.sql.oracle.parser.OracleVersion;
import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoldenDBOracleMetaService extends OraMetaService {

    public GoldenDBOracleMetaService(Session session){
        super(session);
    }

    @Override
    public Map<String, String> getSqlParserParameters() { return Map.of(SqlParserParameters.VERSION, OracleVersion.ORACLE_12.versionString()); }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection connection) {
        return new GoldenDBOracleUmiServiceDm(connection);
    }

    @Override
    public String getCurrentCatalog() { return querySingleValue("SELECT DATABASE()", "getCurrentCatalog"); }

    @Override
    public String getCurrentSchema() { return querySingleValue("SELECT SYS_CONTEXT('USERENV','CURRENT_SCHEMA') FROM DUAL", "getCurrentSchema"); }

    private String querySingleValue(String sql, String operation) {
        try {
            return this.rdbSession.executeQuery(connection -> {
                try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
                    return resultSet.next() ? resultSet.getString(1) : "";
                }
            });
        } catch (Exception e) {
            String msg = operation + " failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
