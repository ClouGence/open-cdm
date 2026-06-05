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
package com.clougence.clouddm.ds.hana.execute;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

/**
 * @author chunlin
 * @date 2024/4/2 HanaMetaProviderDm
 */
public class HanaMetaProviderDm extends AbstractMetadataProvider implements MetaDataService {

    public HanaMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select SCHEMA_NAME,TABLE_NAME,COMMENTS,IS_SYSTEM_TABLE,IS_COLUMN_TABLE,TABLE_TYPE,IS_INSERT_ONLY,IS_TEMPORARY,HAS_PRIMARY_KEY,CREATE_TIME "
                     + "from SYS.TABLES where SCHEMA_NAME = ? and TABLE_NAME in " + buildWhereIn(tabs);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbTable> tables = HanaMetaProviderUtils.convertTable(resultSet, catalog);
                if (tables.isEmpty()) {
                    return Collections.emptyList();
                }

                return tables;
            }
        }
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select SCHEMA_NAME,DEFINITION,VIEW_NAME,COMMENTS,CREATE_TIME,VIEW_TYPE " + "from SYS.VIEWS where SCHEMA_NAME = ? and VIEW_NAME in " + buildWhereIn(tabs);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet resultSet = ps.executeQuery()) {
                List<RdbTable> tables = HanaMetaProviderUtils.convertView(resultSet, catalog);
                if (tables.isEmpty()) {
                    return Collections.emptyList();
                }

                return tables;
            }
        }
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return Collections.emptyList();
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "SELECT I.SCHEMA_NAME,I.TABLE_NAME,I.INDEX_NAME,I.INDEX_TYPE,I.\"CONSTRAINT\",IC.COLUMN_NAME,ASCENDING_ORDER " + "FROM SYS.INDEXES I "
                     + "JOIN SYS.INDEX_COLUMNS IC ON I.INDEX_NAME = IC.INDEX_NAME " + "WHERE I.SCHEMA_NAME = ? AND I.\"CONSTRAINT\" IS NULL AND I.TABLE_NAME in "
                     + buildWhereIn(tabs) + "ORDER BY IC.\"POSITION\" ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                /*
                    INDEX_TYPE:
                        Type of row store indexes: BTREE, BTREE_UNIQUE, CPBTREE, and CPBTREE_UNIQUE.
                        Types of column store indexes: INVERTED VALUE, INVERTED VALUE UNIQUE, INVERTED HASH, INVERTED INDIVIDUAL, FULLTEXT TEXT ANALYSIS, and GEOCODE.
                 */
                List<RdbIndex> idxList = HanaMetaProviderUtils.convertIndex(rs);
                Map<String, Map<String, RdbIndex>> idxMap = HanaMetaProviderUtils.convertIndexList(idxList);
                return idxMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, idx -> new ArrayList<>(idx.getValue().values())));
            }
        }
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        tabs = tabs.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (tabs.isEmpty()) {
            return Collections.emptyMap();
        }

        String sql = "SELECT SCHEMA_NAME,TABLE_NAME,COLUMN_NAME,\"POSITION\",CONSTRAINT_NAME,REFERENCED_SCHEMA_NAME,REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME "
                     + "FROM REFERENTIAL_CONSTRAINTS " + "WHERE SCHEMA_NAME = ? AND TABLE_NAME IN " + buildWhereIn(tabs) + "ORDER BY \"POSITION\" ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<RdbForeignKey> fkList = HanaMetaProviderUtils.convertForeignKey(rs);
                if (fkList.isEmpty()) {
                    return Collections.emptyMap();
                }

                Map<String, Map<String, RdbForeignKey>> tableFkMap = new LinkedHashMap<>();
                HanaMetaProviderUtils.fillFkMap(tableFkMap, fkList);
                return tableFkMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, uks -> new ArrayList<>(uks.getValue().values())));
            }
        }
    }

    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "SELECT DISTINCT I.SCHEMA_NAME,I.TABLE_NAME,I.INDEX_NAME,I.INDEX_TYPE,I.\"CONSTRAINT\",IC.COLUMN_NAME,IC.\"POSITION\",ASCENDING_ORDER " + "FROM SYS.INDEXES I "
                     + "JOIN SYS.INDEX_COLUMNS IC ON I.INDEX_NAME = IC.INDEX_NAME "
                     + "WHERE I.SCHEMA_NAME = ? AND I.\"CONSTRAINT\" IN ('PRIMARY KEY','UNIQUE') AND I.TABLE_NAME in " + buildWhereIn(tabs) + " ORDER BY IC.\"POSITION\" ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                Map<String, Map<String, UmiConstraint>> pkUkMap = new LinkedHashMap<>();
                while (rs.next()) {
                    String type = rs.getString("CONSTRAINT");
                    String table = rs.getString("TABLE_NAME");
                    Map<String, UmiConstraint> constraints = pkUkMap.computeIfAbsent(table, t -> new LinkedHashMap<>());
                    if (type.equals("PRIMARY KEY")) {
                        HanaMetaProviderUtils.mapToPkExt(rs, constraints);
                    } else if (type.equals("UNIQUE")) {
                        HanaMetaProviderUtils.mapToUkExt(rs, constraints);
                    } else {
                        throw new IllegalArgumentException("unsupported type constraint type:" + type);
                    }
                }
                return (Map<String, Map<String, UmiConstraint>>) CollectionUtils.decorateCaseSensitive(pkUkMap);
            }
        }
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select SCHEMA_NAME,TABLE_NAME,COLUMN_NAME,IS_NULLABLE,DATA_TYPE_NAME,LENGTH,DEFAULT_VALUE,COMMENTS,SCALE,POSITION,GENERATION_TYPE "
                     + "from SYS.TABLE_COLUMNS where SCHEMA_NAME = ? and TABLE_NAME in " + buildWhereIn(tabs) + " order by POSITION asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<RdbColumn> columns = HanaMetaProviderUtils.convertColumns(rs);

                Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
                for (RdbColumn column : columns) {
                    result.computeIfAbsent(column.getTable(), s -> new ArrayList<>()).add(column);
                }
                return result;
            }
        }
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchViewColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select SCHEMA_NAME,VIEW_NAME as TABLE_NAME,COLUMN_NAME,IS_NULLABLE,DATA_TYPE_NAME,LENGTH,DEFAULT_VALUE,COMMENTS,SCALE,POSITION,GENERATION_TYPE "
                     + "from SYS.VIEW_COLUMNS where SCHEMA_NAME = ? and VIEW_NAME in " + buildWhereIn(tabs) + " order by POSITION asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<RdbColumn> columns = HanaMetaProviderUtils.convertColumns(rs);

                Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
                for (RdbColumn column : columns) {
                    result.computeIfAbsent(column.getTable(), s -> new ArrayList<>()).add(column);
                }
                return result;
            }
        }
    }

    /**
     * query database
     */
    public Value selectCatalog(String catalog) throws SQLException {
        String sql = "SELECT DATABASE_NAME FROM M_DATABASE where DATABASE_NAME = ? limit 1";
        try (Connection conn = this.connectSupplier.eGet();) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, catalog);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> valueList = HanaMetaProviderUtils.convertCatalog(rs);
                    return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
                }
            }
        }
    }

    /**
     * query database list
     */
    public List<Value> selectCatalogs() throws SQLException {
        String sql = "SELECT DATABASE_NAME FROM M_DATABASE order by DATABASE_NAME asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return HanaMetaProviderUtils.convertCatalog(rs);
            }
        }
    }

    /**
     * query schema
     */
    public Value selectSchema(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            String curCatalog = null;
            String queryCurCatalog = "SELECT DATABASE_NAME FROM M_DATABASE;";
            try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(queryCurCatalog)) {
                if (rs.next()) {
                    curCatalog = rs.getString(1);
                }
            }

            if (!StringUtils.equals(curCatalog, catalog)) {
                return null;
            }

            String querySchema = "SELECT SCHEMA_NAME FROM SCHEMAS WHERE SCHEMA_NAME = ? LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(querySchema)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> valueList = HanaMetaProviderUtils.convertSchema(rs);
                    return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
                }
            }
        }
    }

    /**
     * query schema list
     */
    public List<Value> selectSchemas(String catalog) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            String curCatalog = null;
            String queryCurCatalog = "SELECT DATABASE_NAME FROM M_DATABASE";
            try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(queryCurCatalog)) {
                if (rs.next()) {
                    curCatalog = rs.getString(1);
                }
            }

            if (!StringUtils.equals(curCatalog, catalog)) {
                return Collections.emptyList();
            }

            String querySchema = "SELECT SCHEMA_NAME FROM SCHEMAS order by SCHEMA_NAME asc";
            try (PreparedStatement ps = conn.prepareStatement(querySchema); ResultSet rs = ps.executeQuery()) {
                return HanaMetaProviderUtils.convertSchema(rs);
            }
        }
    }

    /**
     * query view list
     */
    public List<Value> selectViews(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            if (!checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = "SELECT VIEW_NAME, COMMENTS FROM SYS.VIEWS WHERE SCHEMA_NAME = ? order by VIEW_NAME asc";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    return HanaMetaProviderUtils.convertViewName(rs);
                }
            }
        }
    }

    /**
     * query table list
     */
    public List<Value> selectTables(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            if (!checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = "SELECT TABLE_NAME, COMMENTS FROM SYS.TABLES WHERE SCHEMA_NAME = ? order by TABLE_NAME asc";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    return HanaMetaProviderUtils.convertTableName(rs);
                }
            }
        }
    }

    /**
     * query procedure list
     */
    public List<Value> selectProcedures(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            if (!checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = "SELECT PROCEDURE_NAME FROM SYS.PROCEDURES WHERE SCHEMA_NAME = ? order by PROCEDURE_NAME asc";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    return HanaMetaProviderUtils.convertProcedureName(rs);
                }
            }
        }
    }

    /**
     * query function list
     */
    public List<Value> selectFunctions(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            if (!checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = "SELECT FUNCTION_NAME FROM SYS.FUNCTIONS WHERE SCHEMA_NAME = ? order by FUNCTION_NAME asc";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    return HanaMetaProviderUtils.convertFunctionName(rs);
                }
            }
        }
    }

    /**
     * query trigger list
     */
    public List<Value> selectTriggers(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            if (!checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = "SELECT TRIGGER_NAME FROM SYS.TRIGGERS WHERE SCHEMA_NAME = ? order by TRIGGER_NAME asc";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    return HanaMetaProviderUtils.convertTriggerName(rs);
                }
            }
        }
    }

    /**
     * query sequence list
     */
    public List<Value> selectSequence(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            if (!checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = "SELECT SEQUENCE_NAME FROM SYS.SEQUENCES WHERE SCHEMA_NAME = ? order by SEQUENCE_NAME asc";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    return HanaMetaProviderUtils.convertSequenceName(rs);
                }
            }
        }
    }

    /**
     * query synonym list
     */
    public List<Value> selectSynonyms(String catalog, String schema) throws SQLException {
        try (Connection conn = this.connectSupplier.eGet()) {
            if (!checkIsUseCurrentlyDataBase(conn, catalog)) {
                return Collections.emptyList();
            }

            String sql = "SELECT SYNONYM_NAME FROM SYS.SYNONYMS WHERE SCHEMA_NAME = ? order by SYNONYM_NAME asc";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);
                try (ResultSet rs = ps.executeQuery()) {
                    return HanaMetaProviderUtils.convertSynonymName(rs);
                }
            }
        }
    }

    /**
     * check if the current database is in use
     */
    protected boolean checkIsUseCurrentlyDataBase(Connection conn, String catalog) throws SQLException {
        String curCatalog = null;
        String queryCurCatalog = "SELECT DATABASE_NAME FROM M_DATABASE";
        try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(queryCurCatalog)) {
            if (rs.next()) {
                curCatalog = rs.getString(1);
            }
        }
        return StringUtils.equals(curCatalog, catalog);
    }

    public List<RdbProcedure> loadProcedures(Object o, String schema, List<String> procedureNames) throws SQLException {
        procedureNames = stringArray2List(procedureNames);
        if (procedureNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<RdbProcedure> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> proc : CollectionUtils.splitList(procedureNames, defaultGroupSize())) {
                List<RdbProcedure> rdbProcedures = this.fetchProcedureByPart(conn, schema, proc);
                List<RdbParam> rdbParams = this.fetchProceduresParams(conn, schema, proc);
                result.addAll(rdbProcedures.stream()
                    .peek(rdbProcedure -> rdbProcedure.setRdbParams(rdbParams.stream()
                        .sorted(Comparator.comparingInt(RdbParam::getOrdinal))
                        .filter(rdbParam -> rdbParam.getReferenceObject().equals(rdbProcedure.getName()))
                        .collect(Collectors.toList())))
                    .collect(Collectors.toList()));
            }
        }
        return result;
    }

    private List<RdbParam> fetchProceduresParams(Connection conn, String schema, List<String> pros) throws SQLException {
        String sql = "SELECT SCHEMA_NAME,PROCEDURE_NAME,PARAMETER_NAME,DATA_TYPE_NAME,\"POSITION\",\"LENGTH\" FROM SYS.PROCEDURE_PARAMETERS "
                     + "WHERE SCHEMA_NAME = ? AND PROCEDURE_NAME IN " + buildWhereIn(pros) + " ORDER BY \"POSITION\"";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(pros);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return HanaMetaProviderUtils.convertProcedureParams(rs);
            }
        }
    }

    private List<RdbProcedure> fetchProcedureByPart(Connection conn, String schema, List<String> proc) throws SQLException {
        String sql = "SELECT SCHEMA_NAME,PROCEDURE_NAME FROM SYS.PROCEDURES WHERE PROCEDURE_NAME = ? AND SCHEMA_NAME IN " + buildWhereIn(proc);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(proc);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return HanaMetaProviderUtils.convertProcedures(rs);
            }
        }
    }

    public List<RdbFunction> loadFunctions(Object o, String schema, List<String> functionNames) throws SQLException {
        functionNames = stringArray2List(functionNames);
        if (functionNames.isEmpty()) {
            return Collections.emptyList();
        }

        List<RdbFunction> result = new ArrayList<>();
        try (Connection conn = this.connectSupplier.eGet()) {
            for (List<String> funcs : CollectionUtils.splitList(functionNames, defaultGroupSize())) {
                List<RdbFunction> rdbFunctions = this.fetchFunctionByPart(conn, schema, funcs);
                List<RdbParam> rdbParams = this.fetchFunctionParams(conn, schema, funcs);
                if (!rdbParams.isEmpty()) {
                    result.addAll(rdbFunctions.stream().peek(rdbFunction -> {
                        List<RdbParam> rdbParamList = rdbParams.stream()
                            .sorted(Comparator.comparingInt(RdbParam::getOrdinal))
                            .filter(rdbParam -> rdbParam.getReferenceObject().equals(rdbFunction.getName()))
                            .collect(Collectors.toList());
                        if (!rdbParamList.isEmpty()) {
                            rdbFunction.setReturns(rdbParamList.remove(0));
                        }
                        rdbFunction.setRdbParams(rdbParamList);

                    }).collect(Collectors.toList()));
                }

            }
        }
        return result;
    }

    private List<RdbParam> fetchFunctionParams(Connection conn, String schema, List<String> funcs) throws SQLException {
        String sql = "SELECT SCHEMA_NAME,FUNCTION_NAME,PARAMETER_NAME,DATA_TYPE_NAME,\"POSITION\",\"LENGTH\" FROM SYS.FUNCTION_PARAMETERS "
                     + "WHERE SCHEMA_NAME = ? AND FUNCTION_NAME IN " + buildWhereIn(funcs) + " ORDER BY \"POSITION\"";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(funcs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return HanaMetaProviderUtils.convertFunctionParams(rs);
            }
        }
    }

    private List<RdbFunction> fetchFunctionByPart(Connection conn, String schema, List<String> funcs) throws SQLException {
        String sql = "SELECT SCHEMA_NAME,FUNCTION_NAME FROM SYS.FUNCTIONS WHERE SCHEMA_NAME = ? AND FUNCTION_NAME IN " + buildWhereIn(funcs);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(funcs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return HanaMetaProviderUtils.convertFunctions(rs);
            }
        }
    }

    public Value loadTrigger(String schema, String leafName) throws SQLException {
        String sql = "SELECT TRIGGER_NAME,SUBJECT_TABLE_NAME,DEFINITION,TRIGGER_ACTION_TIME,TRIGGER_EVENT FROM SYS.TRIGGERS " + "WHERE SCHEMA_NAME = ? and TRIGGER_NAME= ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            ps.setString(2, leafName);
            try (ResultSet rs = ps.executeQuery()) {
                List<RdbTrigger> values = HanaMetaProviderUtils.convertTrigger(rs);
                return CollectionUtils.isNotEmpty(values) ? values.get(0) : null;
            }
        }
    }
}
