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
package com.clougence.clouddm.ds.gauss.execute.gsog;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.gauss.dialect.GaussDBDialect;
import com.clougence.clouddm.dsfamily.postgres.definition.ui.editor.table.PgEditorProvider;
import com.clougence.clouddm.dsfamily.postgres.execute.PgMetaService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2021/1/15 17:11
 */
@Slf4j
public class GsogMetaService extends PgMetaService {

    public GsogMetaService(Session rdbSession){
        super(rdbSession);
    }

    @Override
    protected DmRdbUmiService rdbUmiService(Connection connection) {
        return new GsogUmiServiceDm(connection);
    }

    @Override
    protected SqlBuilder getSqlBuilder() { return PgEditorProvider.INSTANCE; }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) {
        try {
            return this.rdbSession.executeQuery(con -> {
                switch (leafType) {
                    case View:
                        return showCreateView(con, StringUtils.toString(levelsParam.get(UmiTypes.Catalog)), StringUtils.toString(levelsParam.get(UmiTypes.Schema)), leafName);
                    case Function:
                    case Procedure:
                        return showCreateFunction(con, StringUtils.toString(levelsParam.get(UmiTypes.Catalog)), StringUtils
                            .toString(levelsParam.get(UmiTypes.Schema)), leafName, leafType);
                    default:
                        throw new UnsupportedOperationException("MSSQL '" + leafType + "' Unsupported.");
                }
            });
        } catch (Exception e) {
            String msg = "requestObjectScript error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    @Override
    protected List<String> showCreateView(Connection con, String catalog, String schema, String view) throws SQLException {
        String showSql = "select 'create view " + view + "\nas\n' || pg_get_viewdef('" + GaussDBDialect.INSTANCE.fmtTableName(true, catalog, schema, view) + "'::regclass, true)";

        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(showSql)) {
            if (rs.next()) {
                return Collections.singletonList(rs.getString(1));
            } else {
                return Collections.emptyList();
            }
        }
    }

    protected List<String> showCreateFunction(Connection con, String catalog, String schema, String leafName, UmiTypes types) {
        String sql = "SELECT pg_get_functiondef(?) ";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(leafName));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String string = rs.getString(1);
                    int start = string.indexOf("\"") + 1;
                    int end;
                    if (types == UmiTypes.Function) {
                        end = string.lastIndexOf("\"");
                    } else {
                        end = string.lastIndexOf("/\n\")");
                    }
                    if (end == -1) {
                        end = string.length();
                    }
                    return Collections.singletonList(string.substring(start, end));
                } else {
                    return Collections.emptyList();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
