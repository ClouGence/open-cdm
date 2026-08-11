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
package com.clougence.clouddm.ds.valkey.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.RdbAttributeNames;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbForeignKey;
import com.clougence.schema.umi.special.rdb.RdbIndex;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.ConstraintObject;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Valkey 元数据访问（基于 Valkey GLIDE 驱动），selectSchemas 在 CONFIG 不可用时
 * 降级为 SELECT 递增嗅探真实 db 数量。
 */
@Slf4j
public class ValkeyMetaProviderDm extends AbstractMetadataProvider implements MetaDataService {

    // db 数量嗅探上限，Valkey 实际 databases 配置远小于此值，仅用于防止异常场景下的无限探测。
    static final int PROBE_LIMIT = 256;

    public ValkeyMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    public String getVersion() {
        String sql = "info server";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                Properties properties = ValkeyMetaProviderUtils.convertServerInfo(rs);
                return properties.getProperty("valkey_version");
            }
        } catch (Exception e) {
            String msg = "getVersion failed, " + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public List<Value> selectSchemas() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            try {
                return querySchemasByConfig(conn);
            } catch (SQLException e) {
                // 部分云托管服务禁用了 CONFIG 命令，典型报错为 "ERR unknown command 'CONFIG'..."；
                // 此类情况降级为 SELECT 嗅探真实 db 数量，权限等其他错误保持原样抛出。
                if (!isConfigUnsupported(e)) {
                    throw e;
                }
                log.warn("CONFIG GET databases unsupported, fallback to SELECT probe.");
                return ValkeyMetaProviderUtils.buildSchemaValues(probeDatabaseCount(conn));
            }
        }
    }

    private List<Value> querySchemasByConfig(Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("config get databases"); ResultSet rs = ps.executeQuery()) {
            return ValkeyMetaProviderUtils.convertSchema(rs);
        }
    }

    /**
     * CONFIG 命令被禁用时，典型报错信息包含 "unknown command 'CONFIG'"；权限等其他错误不在此列。
     */
    private boolean isConfigUnsupported(SQLException e) {
        Throwable cur = e;
        while (cur != null) {
            String msg = cur.getMessage();
            if (msg != null && msg.toLowerCase(Locale.ROOT).contains("unknown command 'config'")) {
                return true;
            }
            cur = cur.getCause();
        }
        return false;
    }

    /**
     * CONFIG 不可用时，通过 SELECT 以递增步长（0, 3, 7, 12, 18, ...）嗅探真实 db 数量：
     * 跳跃探测找到首个越界的 db，再在上一成功点与越界点之间逐个精确定位边界。
     */
    int probeDatabaseCount(Connection conn) {
        if (!selectDatabase(conn, 0)) {
            return 0;
        }
        int lastGood = 0;
        int step = 3;
        while (true) {
            int candidate = lastGood + step;
            if (candidate >= PROBE_LIMIT) {
                break;
            }
            if (!selectDatabase(conn, candidate)) {
                return refineDatabaseCount(conn, lastGood, candidate);
            }
            lastGood = candidate;
            step++;
        }
        return lastGood + 1;
    }

    int refineDatabaseCount(Connection conn, int lastGood, int failPoint) {
        int count = lastGood + 1;
        for (int i = lastGood + 1; i < failPoint; i++) {
            if (selectDatabase(conn, i)) {
                count = i + 1;
            } else {
                break;
            }
        }
        return count;
    }

    private boolean selectDatabase(Connection conn, int db) {
        try (PreparedStatement ps = conn.prepareStatement("select ?")) {
            ps.setString(1, String.valueOf(db));
            ps.execute();
            return true;
        } catch (SQLException e) {
            // CONFIG 被禁用的托管服务上 SELECT 仍可用，失败视为 db 越界。
            log.warn("SELECT db {} failed during schema probe, treat as out of range: {}", db, e.getMessage());
            return false;
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
                            keyInfo.setAttribute(RdbAttributeNames.OBJ_UI_TIPS, "[" + ValkeyMetaProviderUtils.keyTypeDesc(type) + "]");
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
