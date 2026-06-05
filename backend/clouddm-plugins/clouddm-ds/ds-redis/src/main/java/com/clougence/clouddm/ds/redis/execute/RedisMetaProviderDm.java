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
package com.clougence.clouddm.ds.redis.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Redis 元信息获取，参考资料：
 *
 * @version : 2021-04-29
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class RedisMetaProviderDm extends AbstractMetadataProvider implements MetaDataService {

    public RedisMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    public String getVersion() {
        String sql = "info server";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                Properties properties = RedisMetaProviderUtils.convertServerInfo(rs);
                return properties.getProperty("redis_version");
            }
        } catch (Exception e) {
            String msg = "getVersion failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public List<Value> selectSchemas() throws SQLException {
        String sql = "config get databases";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return RedisMetaProviderUtils.convertSchema(rs);
            }
        }
    }

    public Value selectSchema(String schema) {
        RdbValue v = new RdbValue();
        v.setValue(schema);
        v.setUmiType(UmiTypes.Schema);
        return v;
    }

    public List<Value> keysInfo(String schema, String pattern, int maxCount) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            // select db
            try (PreparedStatement ps = conn.prepareStatement("select ?")) {
                ps.setString(1, schema);
                ps.execute();
            }

            // some key
            List<String> someKey = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("keys ?")) {
                ps.setString(1, StringUtils.isBlank(pattern) ? "*" : pattern);
                ps.setMaxRows(maxCount);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        someKey.add(rs.getString("KEY"));
                    }
                }
            }

            List<Value> result = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(someKey)) {
                String typeCommand = "type \"" + String.join("\" \"", someKey) + "\"";
                try (PreparedStatement ps = conn.prepareStatement(typeCommand)) {
                    for (int i = 0; i < someKey.size(); i++) {
                        ps.setString(i + 1, someKey.get(i));
                    }

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            RdbValue keyInfo = new RdbValue();
                            String key = rs.getString("KEY");
                            String type = rs.getString("TYPE");

                            keyInfo.setValue(key);
                            keyInfo.setAttribute(RdbAttributeNames.OBJ_UI_TIPS, "[" + RedisMetaProviderUtils.keyTypeDesc(type) + "]");
                            keyInfo.setUmiType(UmiTypes.Key);
                            result.add(keyInfo);
                        }
                    }
                }
            }
            return result;
        }
    }

    public Value keyInfo(String schema, String keyName) throws SQLException {
        if (keyName == null || keyName.isEmpty()) {
            return null;
        }

        try (Connection conn = this.connectSupplier.eGet()) {
            try (PreparedStatement ps1 = conn.prepareStatement("select ?")) {
                ps1.setString(1, schema);
                ps1.execute();
            }

            try (PreparedStatement ps2 = conn.prepareStatement("type ?")) {
                ps2.setString(1, keyName);
                try (ResultSet rs = ps2.executeQuery()) {
                    if (rs.next()) {
                        RdbValue keyInfo = new RdbValue();
                        String type = rs.getString("TYPE");

                        keyInfo.setValue(keyName);
                        keyInfo.setAttribute(RdbAttributeNames.OBJ_UI_TIPS, type);
                        keyInfo.setUmiType(UmiTypes.Key);
                        return keyInfo;
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyList();
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyList();
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyList();
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchViewColumns(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<ConstraintObject>> fetchTableConstraints(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }
}
