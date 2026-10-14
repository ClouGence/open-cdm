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
package com.clougence.clouddm.ds.yashandb.execute;

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.dsfamily.oracle.execute.OraMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.clouddm.sdk.sql.SqlParserParameters;
import com.clougence.sql.oracle.parser.OracleVersion;

import lombok.extern.slf4j.Slf4j;

/**
 * 崖山数据库元数据服务。
 * <p>
 * 崖山数据库兼容 Oracle 语法与数据字典，因此方言、DDL、对象编辑等能力全部沿用
 * {@code dsfamily.oracle}。仅有以下两点在崖山（实测 23.4.7）上存在差异：
 * <ul>
 * <li>不存在 {@code PRODUCT_COMPONENT_VERSION} 视图，数据库版本需从 {@code V$VERSION} 读取；</li>
 * <li>不存在 {@code GLOBAL_NAME} 视图，崖山也没有 Oracle 的 catalog（PDB）概念，
 * 因此 {@link #getCurrentCatalog()} 返回 {@code null}（与达梦等 Oracle 兼容库的处理方式一致）。</li>
 * </ul>
 * {@code getCurrentSchema()} 沿用 Oracle 实现——崖山支持
 * {@code SYS_CONTEXT('USERENV','CURRENT_SCHEMA')}，无需覆写。
 * </p>
 *
 * @author open-cdm
 */
@Slf4j
public class YashanDBMetaService extends OraMetaService {

    public YashanDBMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new YashanDBUmiServiceDm(con);
    }

    @Override
    public Map<String, String> getSqlParserParameters() {
        String databaseVersion = this.fetchVersion("SELECT VERSION_NUMBER FROM V$VERSION");
        return Map.of(SqlParserParameters.VERSION, OracleVersion.parse(databaseVersion).versionString());
    }

    @Override
    public String getCurrentCatalog() {
        // YashanDB 无 Oracle catalog(PDB) 概念，ALL_USERS 也没有 ORACLE_MAINTAINED 之外的库级标识。
        return null;
    }
}
