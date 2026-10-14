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
package com.clougence.clouddm.ds.yashandb.execute.oracle;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.dsfamily.oracle.execute.OraMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.sql.oracle.parser.OracleVersion;

import lombok.extern.slf4j.Slf4j;

/**
 * Metadata service for YashanDB.
 * <p>
 * YashanDB is compatible with Oracle syntax and the data dictionary, so the dialect, DDL and object editing
 * capabilities all reuse {@code dsfamily.oracle}. Only the following two points differ on YashanDB (measured
 * on 23.4.7):
 * <ul>
 * <li>the {@code PRODUCT_COMPONENT_VERSION} view does not exist, so the database version must be read from
 * {@code V$VERSION};</li>
 * <li>the {@code GLOBAL_NAME} view does not exist, and YashanDB has no Oracle catalog (PDB) concept, so
 * {@link #getCurrentCatalog()} returns {@code null} (consistent with how other Oracle-compatible databases
 * such as Dameng handle it).</li>
 * </ul>
 * {@code getCurrentSchema()} reuses the Oracle implementation &mdash; YashanDB supports
 * {@code SYS_CONTEXT('USERENV','CURRENT_SCHEMA')}, so no override is needed.
 * </p>
 *
 * @author open-cdm
 */
@Slf4j
public class YashanDBOracleMetaService extends OraMetaService {

    public YashanDBOracleMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new YashanDBOracleUmiServiceDm(con);
    }

    @Override
    public Map<String, String> getSqlParserParameters() {
        String databaseVersion = this.fetchVersion("SELECT VERSION_NUMBER FROM V$VERSION");
        return Map.of(SqlParserParameters.VERSION, OracleVersion.parse(databaseVersion).versionString());
    }

    @Override
    public String getCurrentCatalog() {
        // YashanDB has no Oracle catalog (PDB) concept, and ALL_USERS has no database-level identifier other
        // than ORACLE_MAINTAINED.
        return null;
    }
}
