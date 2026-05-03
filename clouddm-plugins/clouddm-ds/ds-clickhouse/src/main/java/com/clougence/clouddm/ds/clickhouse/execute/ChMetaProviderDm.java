package com.clougence.clouddm.ds.clickhouse.execute;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import com.clougence.adapter.clickhouse.ClickHouseMainVersion;
import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.*;
import com.clougence.schema.umi.struts.UmiConstraint;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * clickhouse
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class ChMetaProviderDm extends AbstractMetadataProvider implements MetaDataService {

    // for clickhouse 20.x version,not support `comment` column.
    private static final String TABLE_NEW = "select `database`,`name`,`engine`,`engine_full`,`is_temporary`,`data_paths`,`metadata_path`,`comment` from system.tables";
    private static final String TABLE_OLD = "select `database`,`name`,`engine`,`engine_full`,`is_temporary`,`data_paths`,`metadata_path` from system.tables";
    private static final String COLUMNS   = "select `table`,`name`,`type`,`comment`,`position`,`default_kind`,`default_expression`,`is_in_partition_key`,`is_in_sorting_key`,`is_in_primary_key`,`is_in_sampling_key` from system.columns";

    public ChMetaProviderDm(Connection connection){
        super(connection);
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement("select version()"); ResultSet resultSet = ps.executeQuery()) {
            return ((SpecialValueRowMapper<String>) (rs, columnType, columnTypeName, columnClassName) -> rs.getString(1)).mapRow(resultSet);
        }
    }

    public List<Value> selectSchemas() throws SQLException {
        String sql = "select `name`,`engine` from system.databases order by `name` asc";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return ChMetaProviderUtils.convertSchema(rs);
            }
        }
    }

    public Value selectSchema(String schema) throws SQLException {
        String sql = "select `name`,`engine` from system.databases where `name` = ? limit 1";

        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> valueList = ChMetaProviderUtils.convertSchema(rs);
                return CollectionUtils.isEmpty(valueList) ? null : valueList.get(0);
            }
        }
    }

    public List<Value> selectTables(String schema) throws SQLException {
        return this.selectByConditions(schema, "(engine not like '%View')");
    }

    public List<Value> selectViews(String schema) throws SQLException {
        return this.selectByConditions(schema, "(engine like '%View')");
    }

    private List<Value> selectByConditions(String schema, String conditions) throws SQLException {
        String sql = "select `name`,`engine` from system.tables where `database` = ? and " + conditions + " order by `name` asc";
        try (Connection conn = this.connectSupplier.eGet()) {
            ClickHouseMainVersion mainVersion = fetchMainVersion(conn);
            boolean supportComment = ClickHouseMainVersion.CK_21_1.isGt(mainVersion);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, schema);

                try (ResultSet rs = ps.executeQuery()) {
                    List<Value> schemas = ChMetaProviderUtils.convertTableName(rs, supportComment);
                    return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
                }
            }
        }
    }

    @Override
    protected List<RdbTable> fetchTableByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.fetchByPart(conn, schema, tabs);
    }

    @Override
    protected List<RdbTable> fetchViewByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.fetchByPart(conn, schema, tabs);
    }

    @Override
    protected List<RdbTable> fetchMaterializedByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return this.fetchByPart(conn, schema, tabs);
    }

    private List<RdbTable> fetchByPart(Connection conn, String schema, List<String> tabs) throws SQLException {
        ClickHouseMainVersion mainVersion = fetchMainVersion(conn);
        boolean supportComment = mainVersion != null && mainVersion.isGt(ClickHouseMainVersion.CK_21_1);

        String sql = supportComment ? TABLE_NEW : TABLE_OLD;
        sql = sql + " where `database` = ? and `name` in " + buildWhereIn(tabs);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            tabs.add(0, schema);
            for (int i = 1; i <= tabs.size(); i++) {
                ps.setString(i, tabs.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return ChMetaProviderUtils.convertTable(rs, supportComment);
            }
        }
    }

    @Override
    protected Map<String, List<RdbIndex>> fetchIndexes(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, List<RdbForeignKey>> fetchForeignKeys(Connection conn, String catalog, String schema, List<String> tabs) {
        return Collections.emptyMap();
    }

    @Override
    protected Map<String, Map<String, UmiConstraint>> fetchPrimaryUnique(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = "select `table`,`name`,`is_in_primary_key` from system.columns\n" +//
                     "where `database` = ? and `is_in_primary_key` = 1 and `table` in " + buildWhereIn(tabs) + "\n" +//
                     "order by `position` asc";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                Map<String, Map<String, UmiConstraint>> result = new LinkedHashMap<>();
                while (rs.next()) {
                    String pkName = "PRIMARY";
                    String table = rs.getString("table");

                    Map<String, UmiConstraint> constraints = result.computeIfAbsent(table, t -> new LinkedHashMap<>());
                    RdbPrimaryKey primaryKey = (RdbPrimaryKey) (constraints.computeIfAbsent(pkName, k -> new RdbPrimaryKey()));
                    primaryKey.setName(pkName);
                    primaryKey.addColumn(rs.getString("name"));
                }
                return result;
            }
        }
    }

    @Override
    protected Map<String, List<RdbColumn>> fetchTableColumns(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        String sql = COLUMNS + " where `database` = ? and `table` in " + buildWhereIn(tabs) + " order by `position` asc";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            List<String> params = new ArrayList<>(tabs);
            params.add(0, schema);
            for (int i = 1; i <= params.size(); i++) {
                ps.setString(i, params.get(i - 1));
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<RdbColumn> columns = ChMetaProviderUtils.convertColumn(rs);

                Map<String, List<RdbColumn>> result = new LinkedHashMap<>();
                for (RdbColumn column : columns) {
                    result.computeIfAbsent(column.getTable(), s -> new ArrayList<>()).add(column);
                }
                return result;
            }
        }
    }

    @Override
    protected List<RdbTable> fetchSelectObjectByPart(Connection conn, String catalog, String schema, List<String> tabs) throws SQLException {
        return fetchByPart(conn, schema, tabs);
    }

    private static ClickHouseMainVersion fetchMainVersion(Connection conn) {
        try {
            DatabaseMetaData metaData = conn.getMetaData();
            String ver = metaData.getDatabaseProductVersion();
            return ClickHouseMainVersion.parserVersion(ver);
        } catch (Exception e) {
            log.error("CH fetchTableByPart error " + e.getMessage());
            return null;
        }
    }
}
