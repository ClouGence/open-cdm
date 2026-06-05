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
package com.clougence.clouddm.ds.ads.execute.ads4my;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.ds.ads.definition.ui.editor.table.AdbMyEditorProvider;
import com.clougence.clouddm.ds.ads.dialect.ads4my.AdbMySqlDialect;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class AdsMyMetaService extends MyMetaService {

    public AdsMyMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection con) {
        return new AdsMyUmiServiceDm(con);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return AdbMyEditorProvider.INSTANCE; }

    @Override
    protected List<String> showCreateTable(Connection con, String schema, String table) throws SQLException {
        String showSql = "show create table " + AdbMySqlDialect.INSTANCE.fmtTableName(true, null, schema, table);

        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            while (rs.next()) {
                String tableName = rs.getString("Table");
                String scriptBody = rs.getString("Create Table");

                if (StringUtils.equals(table, tableName)) {
                    return Collections.singletonList(scriptBody);
                }
            }
            return Collections.emptyList();
        }
    }
}
