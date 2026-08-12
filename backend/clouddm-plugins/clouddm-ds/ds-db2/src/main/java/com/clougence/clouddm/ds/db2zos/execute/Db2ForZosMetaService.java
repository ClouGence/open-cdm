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
package com.clougence.clouddm.ds.db2zos.execute;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.dsfamily.db2.execute.Db2MetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.sql.db2.parser.Db2Version;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class Db2ForZosMetaService extends Db2MetaService {

    public Db2ForZosMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    public Map<String, String> getSqlParserParameters() {
        String databaseVersion = this.fetchVersion("SELECT SERVICE_LEVEL FROM SYSIBMADM.ENV_INST_INFO");
        return Map.of(SqlParserParameters.VERSION, Db2Version.parse(databaseVersion).versionString());
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new Db2ForZosUmiServiceDM(con);
    }
}
