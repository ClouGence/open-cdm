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

package com.clougence.clouddm.ds.greptimedb.execute;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaService;
import com.clougence.clouddm.sdk.execute.meta.DsElement;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;
import com.clougence.utils.function.EFunction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
final class GreptimeMetaService extends MyMetaService {

    GreptimeMetaService(Session session){
        super(session);
    }

    @Override
    public String getVersion() {
        try {
            return rdbSession.executeQuery(connection -> {
                try (Statement statement = connection.createStatement(); ResultSet rows = statement.executeQuery("SELECT pkg_version FROM information_schema.build_info")) {
                    if (!rows.next()) {
                        throw new SQLException("GreptimeDB build_info returned no rows.");
                    }
                    String version = StringUtils.trimToNull(rows.getString(1));
                    if (version == null) {
                        throw new SQLException("GreptimeDB build_info returned an empty package version.");
                    }
                    return version;
                }
            });
        } catch (Exception e) {
            log.error("Read GreptimeDB version failed", e);
            throw ThirdPartyApiException.as().with(e);
        }
    }

    @Override
    public Map<String, String> getSqlParserParameters() { return Map.of(); }

    @Override
    public List<DsElement> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> params) {
        if (StringUtils.isNotBlank(database(params))) {
            return List.of();
        }
        return metadata(meta -> meta.databases(null));
    }

    @Override
    public DsElement detailLevel(List<UmiTypes> levels, Map<UmiTypes, Object> params) {
        String database = database(params);
        if (StringUtils.isBlank(database)) {
            return null;
        }
        return metadata(meta -> meta.databases(database).stream().findFirst().orElse(null));
    }

    @Override
    public List<DsElement> listLeaf(Map<UmiTypes, Object> params, UmiTypes type, String pattern) {
        String database = database(params);
        if (StringUtils.isBlank(database) || (type != UmiTypes.Table && type != UmiTypes.View)) {
            return List.of();
        }
        return metadata(meta -> meta.list(database, type, pattern));
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> params, UmiTypes type, String name) {
        String database = database(params);
        if (StringUtils.isBlank(database) || StringUtils.isBlank(name) || (type != UmiTypes.Table && type != UmiTypes.View)) {
            return null;
        }
        return metadata(meta -> meta.detail(database, type, name));
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> params, String name) {
        String database = database(params);
        if (StringUtils.isBlank(database) || StringUtils.isBlank(name)) {
            return null;
        }
        return metadata(meta -> meta.detail(database, null, name));
    }

    @Override
    public Map<String, List<RdbColumn>> batchColumns(Map<UmiTypes, Object> params, UmiTypes type, List<String> names) {
        String database = database(params);
        if (StringUtils.isBlank(database) || names.isEmpty() || (type != UmiTypes.Table && type != UmiTypes.View)) {
            return Map.of();
        }
        return metadata(meta -> meta.batchColumns(database, type, names));
    }

    @Override
    public String loadTableEditor(Map<UmiTypes, Object> params, String table) {
        throw ThirdPartyApiException.as().with("GreptimeDB table editor is unsupported.");
    }

    @Override
    public List<String> requestObjectScript(Map<UmiTypes, Object> params, UmiTypes type, String name) {
        String database = database(params);
        if (type == UmiTypes.Schema) {
            String target = StringUtils.defaultIfBlank(name, database);
            if (StringUtils.isNotBlank(target)) {
                return metadata(meta -> meta.createScript(target, type, null));
            }
        } else if ((type == UmiTypes.Table || type == UmiTypes.View) && StringUtils.isNotBlank(database) && StringUtils.isNotBlank(name)) {
            return metadata(meta -> meta.createScript(database, type, name));
        }
        throw ThirdPartyApiException.as().with("GreptimeDB SHOW CREATE is unsupported for the requested object.");
    }

    private static String database(Map<UmiTypes, Object> params) {
        return params == null ? null : (String) params.get(UmiTypes.Schema);
    }

    private <T> T metadata(EFunction<GreptimeMetadata, T, SQLException> query) {
        try {
            return rdbSession.executeQuery(conn -> query.eApply(new GreptimeMetadata(conn)));
        } catch (ThirdPartyApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("Read GreptimeDB metadata failed", e);
            throw ThirdPartyApiException.as().with(e);
        }
    }
}
