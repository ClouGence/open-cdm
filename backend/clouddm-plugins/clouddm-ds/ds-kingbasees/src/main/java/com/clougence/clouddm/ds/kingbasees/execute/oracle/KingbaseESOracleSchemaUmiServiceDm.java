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
package com.clougence.clouddm.ds.kingbasees.execute.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.clougence.clouddm.ds.kingbasees.execute.postgresql.KingbaseESPostgreSQLUmiServiceDm;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;

public class KingbaseESOracleSchemaUmiServiceDm extends KingbaseESPostgreSQLUmiServiceDm {

    private static final Set<String> INTERNAL_SCHEMAS = Set.of("information_schema", "pg_catalog");

    private final Connection         connection;

    public KingbaseESOracleSchemaUmiServiceDm(Connection connection){
        super(connection);
        this.connection = connection;
    }

    @Override
    public List<Value> listLevels(List<UmiTypes> levels, Map<UmiTypes, Object> levelsParam) throws SQLException {
        if (levels.isEmpty()) {
            List<Value> values = super.listLevels(List.of(UmiTypes.Catalog), withCurrentCatalog(levelsParam));
            return values.stream().filter(value -> {
                String schema = StringUtils.trimToEmpty(value.asValue()).toLowerCase(Locale.ROOT);
                return !INTERNAL_SCHEMAS.contains(schema);
            }).toList();
        }
        throw new UnsupportedOperationException("KingbaseES schema hierarchy has no child level after schema.");
    }

    @Override
    public Value fetchSelectObject(Map<UmiTypes, Object> levelsParam, String leafName) throws SQLException {
        return super.fetchSelectObject(withCurrentCatalog(levelsParam), leafName);
    }

    @Override
    public List<Value> listLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String pattern) throws SQLException {
        return super.listLeaf(withCurrentCatalog(levelsParam), leafType, pattern);
    }

    @Override
    public Value detailLeaf(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, String leafName) throws SQLException {
        return super.detailLeaf(withCurrentCatalog(levelsParam), leafType, leafName);
    }

    @Override
    public Map<String, List<RdbColumn>> loadColumns(Map<UmiTypes, Object> levelsParam, UmiTypes leafType, List<String> leafNames) throws SQLException {
        return super.loadColumns(withCurrentCatalog(levelsParam), leafType, leafNames);
    }

    private Map<UmiTypes, Object> withCurrentCatalog(Map<UmiTypes, Object> levelsParam) throws SQLException {
        Map<UmiTypes, Object> result = new EnumMap<>(UmiTypes.class);
        if (levelsParam != null) {
            result.putAll(levelsParam);
        }
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT current_database()")) {
            if (resultSet.next()) {
                result.put(UmiTypes.Catalog, resultSet.getString(1));
            }
        }
        return result;
    }
}
