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
package com.clougence.clouddm.ds.goldendb.execute.mysql;

import static com.clougence.adapter.goldendb.GoldenDBAttributeNames.*;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.dsfamily.mysql.dialect.MySqlDialect;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderDm;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;

public class GoldenDBMySQLMetaProvider extends MyMetaProviderDm {

    private static final String GLOBAL_INDEX_TABLE_COMMENT_PREFIX = "Global Index Table Name = ";

    public GoldenDBMySQLMetaProvider(Connection connection){
        super(connection);
    }

    @Override
    public List<Value> selectTables(String schema) throws SQLException {
        String sql = "SELECT TABLE_NAME,TABLE_TYPE,TABLE_COLLATION,TABLE_COMMENT,ENGINE FROM INFORMATION_SCHEMA.TABLES " + "WHERE TABLE_SCHEMA = ? AND TABLE_TYPE = 'BASE TABLE' "
                     + "AND (TABLE_COMMENT IS NULL OR TABLE_COMMENT NOT LIKE ?) ORDER BY TABLE_NAME ASC";
        try (Connection connection = this.connectSupplier.eGet(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, schema);
            statement.setString(2, GLOBAL_INDEX_TABLE_COMMENT_PREFIX + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                return this.providerUtils.convertTableName(resultSet);
            }
        }
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection connection, String catalog, String schema, List<String> tables) throws SQLException {
        List<RdbTable> result = super.fetchTableByPart(connection, catalog, schema, tables);
        if (CollectionUtils.isEmpty(result)) {
            return result;
        }

        for (RdbTable table : result) {
            loadDistributionMetadata(connection, schema, table);
        }
        return result;
    }

    private void loadDistributionMetadata(Connection connection, String schema, RdbTable table) throws SQLException {
        String qualifiedTable = MySqlDialect.INSTANCE.fmtTableName(true, null, schema, table.getName());
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SHOW DISTRIBUTION FROM " + qualifiedTable)) {
            if (!resultSet.next()) {
                return;
            }

            String distributionType = StringUtils.trimToNull(resultSet.getString("Dist_type"));
            String distributionKey = StringUtils.trimToNull(resultSet.getString("Dist_key"));
            table.setAttribute(DISTRIBUTION_TYPE, distributionType);
            table.setAttribute(DISTRIBUTION_EXPRESSION, distributionKey);
            table.setAttribute(DISTRIBUTION_GROUPS, StringUtils.trimToNull(resultSet.getString("Groups")));
            if (StringUtils.equalsIgnoreCase("HASH", distributionType)) {
                table.setAttribute(DISTRIBUTION_COLUMNS, toColumnJson(distributionKey));
            }
        }
    }

    private String toColumnJson(String distributionKey) {
        if (StringUtils.isBlank(distributionKey)) {
            return null;
        }
        List<String> columns = Arrays.stream(distributionKey.split(","))
            .map(String::trim)
            .map(column -> StringUtils.removeStart(StringUtils.removeEnd(column, "`"), "`"))
            .filter(StringUtils::isNotBlank)
            .toList();
        return columns.isEmpty() ? null : JsonUtils.toJson(columns);
    }
}
