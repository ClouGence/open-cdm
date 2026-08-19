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
package com.clougence.clouddm.ds.kingbasees.execute.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderDm;
import com.clougence.schema.umi.special.rdb.RdbForeignKey;
import com.clougence.schema.umi.special.rdb.RdbIndex;
import com.clougence.schema.umi.special.rdb.RdbPartition;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.utils.CollectionUtils;

public class KingbaseESMySQLMetaProviderDm extends MyMetaProviderDm {

    public KingbaseESMySQLMetaProviderDm(Connection connection){
        super(connection);
        this.providerUtils = new KingbaseESMySQLMetaProviderUtils();
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "SELECT ns.nspname AS TABLE_SCHEMA, tbl.relname AS TABLE_NAME, '' AS INDEX_COMMENT, '' AS COMMENT, "
                     + "CASE WHEN ind.indisunique THEN 0 ELSE 1 END AS NON_UNIQUE, ord.ordinality AS SEQ_IN_INDEX, "
                     + "CASE WHEN (ind.indoption[ord.ordinality - 1] & 1) = 1 THEN 'D' ELSE 'A' END AS COLLATION, "
                     + "idx.relname AS INDEX_NAME, attr.attname AS COLUMN_NAME, UPPER(am.amname) AS INDEX_TYPE, NULL::integer AS SUB_PART " + "FROM sys_catalog.sys_index ind "
                     + "JOIN sys_catalog.sys_class tbl ON tbl.oid = ind.indrelid " + "JOIN sys_catalog.sys_class idx ON idx.oid = ind.indexrelid "
                     + "JOIN sys_catalog.sys_namespace ns ON ns.oid = tbl.relnamespace " + "JOIN sys_catalog.sys_am am ON am.oid = idx.relam "
                     + "JOIN LATERAL unnest(ind.indkey) WITH ORDINALITY ord(attnum, ordinality) ON TRUE "
                     + "JOIN sys_catalog.sys_attribute attr ON attr.attrelid = tbl.oid AND attr.attnum = ord.attnum " + "WHERE ns.nspname = ? AND tbl.relname IN "
                     + buildWhereIn(tabs) + " AND NOT ind.indisprimary AND NOT ind.indisunique ORDER BY idx.relname, ord.ordinality";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bindSchemaAndTables(ps, schema, tabs);
            try (ResultSet rs = ps.executeQuery()) {
                Map<String, Map<String, RdbIndex>> indexMap = this.providerUtils.convertIndexList(this.providerUtils.convertIndex(rs));
                Map<String, List<RdbIndex>> result = new LinkedHashMap<>();
                indexMap.forEach((table, indexes) -> result.put(table, new ArrayList<>(indexes.values())));
                return result;
            }
        }
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "SELECT con.conname AS CONSTRAINT_NAME, src_ns.nspname AS TABLE_SCHEMA, src.relname AS TABLE_NAME, "
                     + "src_attr.attname AS COLUMN_NAME, ref_ns.nspname AS REFERENCED_TABLE_SCHEMA, "
                     + "ref.relname AS REFERENCED_TABLE_NAME, ref_attr.attname AS REFERENCED_COLUMN_NAME, "
                     + "CASE con.confupdtype WHEN 'a' THEN 'NO ACTION' WHEN 'r' THEN 'RESTRICT' WHEN 'c' THEN 'CASCADE' "
                     + "WHEN 'n' THEN 'SET NULL' WHEN 'd' THEN 'SET DEFAULT' END AS UPDATE_RULE, "
                     + "CASE con.confdeltype WHEN 'a' THEN 'NO ACTION' WHEN 'r' THEN 'RESTRICT' WHEN 'c' THEN 'CASCADE' "
                     + "WHEN 'n' THEN 'SET NULL' WHEN 'd' THEN 'SET DEFAULT' END AS DELETE_RULE " + "FROM sys_catalog.sys_constraint con "
                     + "JOIN sys_catalog.sys_class src ON src.oid = con.conrelid " + "JOIN sys_catalog.sys_namespace src_ns ON src_ns.oid = src.relnamespace "
                     + "JOIN sys_catalog.sys_class ref ON ref.oid = con.confrelid " + "JOIN sys_catalog.sys_namespace ref_ns ON ref_ns.oid = ref.relnamespace "
                     + "JOIN LATERAL unnest(con.conkey) WITH ORDINALITY src_ord(attnum, ordinality) ON TRUE "
                     + "JOIN LATERAL unnest(con.confkey) WITH ORDINALITY ref_ord(attnum, ordinality) ON ref_ord.ordinality = src_ord.ordinality "
                     + "JOIN sys_catalog.sys_attribute src_attr ON src_attr.attrelid = src.oid AND src_attr.attnum = src_ord.attnum "
                     + "JOIN sys_catalog.sys_attribute ref_attr ON ref_attr.attrelid = ref.oid AND ref_attr.attnum = ref_ord.attnum "
                     + "WHERE con.contype = 'f' AND src_ns.nspname = ? AND src.relname IN " + buildWhereIn(tabs) + " ORDER BY con.conname, src_ord.ordinality";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bindSchemaAndTables(ps, schema, tabs);
            try (ResultSet rs = ps.executeQuery()) {
                Map<String, Map<String, RdbForeignKey>> foreignKeyMap = new LinkedHashMap<>();
                for (RdbForeignKey foreignKey : this.providerUtils.convertForeignKey(rs)) {
                    Map<String, RdbForeignKey> tableKeys = foreignKeyMap.computeIfAbsent(foreignKey.getTable(), key -> new LinkedHashMap<>());
                    RdbForeignKey existing = tableKeys.get(foreignKey.getName());
                    if (existing == null) {
                        tableKeys.put(foreignKey.getName(), foreignKey);
                    } else {
                        existing.getColumnList().addAll(foreignKey.getColumnList());
                        existing.getReferenceMapping().putAll(foreignKey.getReferenceMapping());
                    }
                }
                Map<String, List<RdbForeignKey>> result = new LinkedHashMap<>();
                foreignKeyMap.forEach((table, keys) -> result.put(table, new ArrayList<>(keys.values())));
                return result;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "SELECT ns.nspname AS TABLE_SCHEMA, tbl.relname AS TABLE_NAME, '' AS COMMENT, con.conname AS CONSTRAINT_NAME, "
                     + "idx.relname AS INDEX_NAME, UPPER(am.amname) AS INDEX_TYPE, NULL::integer AS SUB_PART, 'A' AS COLLATION, "
                     + "attr.attname AS COLUMN_NAME, CASE con.contype WHEN 'p' THEN 'PRIMARY KEY' WHEN 'u' THEN 'UNIQUE' END AS CONSTRAINT_TYPE "
                     + "FROM sys_catalog.sys_constraint con " + "JOIN sys_catalog.sys_class tbl ON tbl.oid = con.conrelid "
                     + "JOIN sys_catalog.sys_namespace ns ON ns.oid = tbl.relnamespace " + "JOIN sys_catalog.sys_class idx ON idx.oid = con.conindid "
                     + "JOIN sys_catalog.sys_am am ON am.oid = idx.relam " + "JOIN LATERAL unnest(con.conkey) WITH ORDINALITY ord(attnum, ordinality) ON TRUE "
                     + "JOIN sys_catalog.sys_attribute attr ON attr.attrelid = tbl.oid AND attr.attnum = ord.attnum "
                     + "WHERE con.contype IN ('p', 'u') AND ns.nspname = ? AND tbl.relname IN " + buildWhereIn(tabs) + " ORDER BY con.conname, ord.ordinality";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bindSchemaAndTables(ps, schema, tabs);
            try (ResultSet rs = ps.executeQuery()) {
                Map<String, Map<String, UmiConstraint>> constraints = new LinkedHashMap<>();
                while (rs.next()) {
                    String table = rs.getString("TABLE_NAME");
                    Map<String, UmiConstraint> tableConstraints = constraints.computeIfAbsent(table, key -> new LinkedHashMap<>());
                    String type = rs.getString("CONSTRAINT_TYPE");
                    if ("PRIMARY KEY".equals(type)) {
                        this.providerUtils.mapToPkExt(rs, tableConstraints);
                    } else {
                        this.providerUtils.mapToUkExt(rs, tableConstraints);
                    }
                }
                return (Map<String, Map<String, UmiConstraint>>) CollectionUtils.decorateCaseSensitive(constraints);
            }
        }
    }

    @Override
    protected Map<String, RdbPartition> fetchTablePartition(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "SELECT t.`TABLE_CATALOG`, t.`TABLE_SCHEMA`, t.`TABLE_NAME`, t.`TABLE_TYPE`, t.`TABLE_COLLATION`, "
                     + "t.`CREATE_TIME`, v.`CHECK_OPTION`, t.`UPDATE_TIME`, t.`TABLE_COMMENT`, t.`ENGINE`, t.`ROW_FORMAT`, "
                     + "t.`AVG_ROW_LENGTH`, t.`CREATE_OPTIONS`, t.`AUTO_INCREMENT`, v.`VIEW_DEFINITION`, v.`DEFINER`, "
                     + "v.`SECURITY_TYPE`, v.`CHARACTER_SET_CLIENT`, v.`COLLATION_CONNECTION`, v.`IS_UPDATABLE` "
                     + "FROM INFORMATION_SCHEMA.TABLES t LEFT JOIN INFORMATION_SCHEMA.VIEWS v " + "ON t.`TABLE_NAME` = v.`TABLE_NAME` AND t.`TABLE_SCHEMA` = v.`TABLE_SCHEMA` "
                     + "WHERE t.`TABLE_SCHEMA` = ? AND t.`TABLE_TYPE` IN ('VIEW', 'SYSTEM VIEW') AND t.`TABLE_NAME` IN " + buildWhereIn(tabs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            bindSchemaAndTables(ps, schema, tabs);
            try (ResultSet rs = ps.executeQuery()) {
                return this.convertView(rs);
            }
        }
    }

    private static void bindSchemaAndTables(PreparedStatement statement, String schema, List<String> tables) throws SQLException {
        statement.setString(1, schema);
        for (int i = 0; i < tables.size(); i++) {
            statement.setString(i + 2, tables.get(i));
        }
    }
}
