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
package com.clougence.clouddm.ds.goldendb.execute.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;

import com.clougence.clouddm.ds.goldendb.definition.mysql.ui.editor.table.GoldenDBMySQLEditorProvider;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.sql.mysql.parser.MySqlVersion;
import com.clougence.utils.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoldenDBMySQLMetaService extends MyMetaService {

    private static final String MYSQL_COMPATIBLE_VERSION = "8.0";
    private static final String MYSQL_EXACT_VERSION      = "80025";

    public GoldenDBMySQLMetaService(Session session){
        super(session);
    }

    @Override
    public Map<String, String> getSqlParserParameters() {
        Map<String, String> parameters = new LinkedHashMap<>();
        parameters.put(SqlParserParameters.VERSION, MYSQL_COMPATIBLE_VERSION);
        parameters.put(SqlParserParameters.GRAMMAR_VERSION, MySqlVersion.MYSQL_8_0.versionString());
        parameters.put(SqlParserParameters.EXACT_VERSION, MYSQL_EXACT_VERSION);
        try {
            String sqlMode = this.rdbSession.executeQuery(connection -> {
                try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT @@SESSION.sql_mode")) {
                    return resultSet.next() ? resultSet.getString(1) : "";
                }
            });
            parameters.put(SqlParserParameters.SQL_MODE, sqlMode == null ? "" : sqlMode);
        } catch (Exception e) {
            String msg = "Get GoldenDB SQL mode failed: " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
        return parameters;
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection connection) {
        return new GoldenDBMySQLUmiServiceDm(connection);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return GoldenDBMySQLEditorProvider.INSTANCE; }
}
