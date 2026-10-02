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
package com.clougence.clouddm.ds.hana.execute;

import com.clougence.clouddm.ds.hana.definition.ui.editor.table.HanaEditorProvider;
import com.clougence.clouddm.ds.hana.i18n.HanaConfigI18nKeys;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbMetaService;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.hana.parser.HanaVersion;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.jdbc.mapper.SingleValueRowMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class HanaMetaService extends DefaultRdbMetaService {

    public HanaMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    public Map<String, String> getSqlParserParameters() {
        String databaseVersion = this.fetchVersion("SELECT VERSION FROM M_DATABASE");
        return Map.of(SqlParserParameters.VERSION, HanaVersion.parse(databaseVersion).versionString());
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new HanaUmiServiceDm(con);
    }

    @Override
    public void testConnect() {
        try {
            int res = this.rdbSession.executeQuery(con -> {
                try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery("SELECT 1 FROM DUMMY")) {
                    return ((SingleValueRowMapper<Integer>) (rs, columnType, columnTypeName, columnClassName) -> rs.getInt(1)).mapRow(resultSet);
                }
            });
            if (res != 1) {
                throw new SQLException("Test SQL 'SELECT 1 FROM DUMMY' failed.");
            }
        } catch (Exception e) {
            String msg = "testConnect failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    public String getVersion() {
        try {
            return this.rdbSession.executeQuery(con -> {
                try (PreparedStatement ps = con.prepareStatement("SELECT VERSION FROM M_DATABASE;"); ResultSet resultSet = ps.executeQuery()) {
                    resultSet.next();// or show variables like '%version_comment%'
                    return resultSet.getString("VERSION");
                }
            });
        } catch (Exception e) {
            String msg = "getVersion failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return HanaEditorProvider.INSTANCE; }

    @Override
    public String getCurrentCatalog() {
        try {
            return this.rdbSession.executeQuery(HanaHooks::getCurrentCatalog);
        } catch (Exception e) {
            log.error("Read HANA current database failed", e);
            throw ThirdPartyApiException.as().with(e);
        }
    }

    @Override
    public String getCurrentSchema() {
        try {
            return this.rdbSession.executeQuery(con -> {
                String queryString = "SELECT CURRENT_SCHEMA FROM SYS.DUMMY";
                try (Statement s = con.createStatement(); ResultSet resultSet = s.executeQuery(queryString)) {
                    return ((SingleValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
                }
            });
        } catch (Exception e) {
            log.error("Read HANA current schema failed", e);
            throw ThirdPartyApiException.as().with(e);
        }
    }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        if (!Set.of(UmiTypes.Table, UmiTypes.View, UmiTypes.Trigger, UmiTypes.Procedure, UmiTypes.Function, UmiTypes.Sequence, UmiTypes.Synonym).contains(leafType)) {
            throw ThirdPartyApiException.as().with(new IllegalArgumentException("Unsupported HANA script object: " + leafType));
        }

        String catalog = (String) levelsParam.get(UmiTypes.Catalog);
        String schema = (String) levelsParam.get(UmiTypes.Schema);
        try {
            return this.rdbSession.executeQuery(con -> {
                if (!StringUtils.equals(catalog, HanaHooks.getCurrentCatalog(con))) {
                    throw ThirdPartyApiException.as().with(HanaConfigI18nKeys.CONFIG_HANA_CATALOG_MISMATCH);
                }

                List<String> scripts = new ArrayList<>();
                try (CallableStatement statement = con.prepareCall("CALL SYS.GET_OBJECT_DEFINITION(?, ?)")) {
                    statement.setString(1, schema);
                    statement.setString(2, leafName);
                    try (ResultSet result = statement.executeQuery()) {
                        while (result.next()) {
                            String definition = result.getString("OBJECT_CREATION_STATEMENT");
                            if (StringUtils.isNotBlank(definition)) {
                                // Keep SQLScript bodies and their internal semicolons intact.
                                if (!definition.stripTrailing().endsWith(";")) {
                                    definition += "\n;";
                                }
                                scripts.add(definition);
                            }
                        }
                    }
                }

                if (scripts.isEmpty()) {
                    throw new SQLException("No HANA object definition returned for " + schema + "." + leafName);
                }

                return scripts;
            });
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Read HANA object script failed: {}.{} ({})", schema, leafName, leafType, e);
            throw ThirdPartyApiException.as().with(e);
        }
    }
}
