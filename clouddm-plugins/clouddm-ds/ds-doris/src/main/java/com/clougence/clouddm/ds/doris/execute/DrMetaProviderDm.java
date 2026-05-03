package com.clougence.clouddm.ds.doris.execute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.ds.doris.dialect.DorisDialect;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderDm;
import com.clougence.schema.metadata.MetaDataService;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/12/2
 **/
@Slf4j
public class DrMetaProviderDm extends MyMetaProviderDm implements MetaDataService {

    public DrMetaProviderDm(Connection connection){
        super(connection);
        this.providerUtils = new DrMetaProviderUtils();
    }

    @Override
    public String getVersion() throws SQLException {
        try (Connection conn = this.connectSupplier.eGet();
                PreparedStatement ps = conn.prepareStatement("show variables like '%version_comment%'");
                ResultSet resultSet = ps.executeQuery()) {
            resultSet.next();// or show variables like '%version_comment%'
            return resultSet.getString("Value");
        }
    }

    @Override
    public List<Value> selectSchemas() throws SQLException {
        String sql = "show databases";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schema = this.providerUtils.convertSchema(rs);
                schema.sort(Comparator.comparing(Value::asValue));
                return schema;
            }
        }
    }

    @Override
    public Value selectSchema(String schema) throws SQLException {
        final String replaceName = schema.replace("'", "''");

        String sql = "show databases like '" + replaceName + "'";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> valueList = this.providerUtils.convertSchema(rs);
                return valueList.stream().filter(value -> StringUtils.equals(value.asValue(), schema)).findFirst().orElse(null);
            }
        }
    }

    public List<Value> selectTables(String schema) throws SQLException {
        String sql = "show full tables from " + DorisDialect.INSTANCE.fmtName(false, schema) + " where Table_type = 'BASE TABLE'";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = this.providerUtils.convertTableName(rs);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    @Override
    public List<Value> selectViews(String schema) throws SQLException {
        return this.selectByConditions(schema, "where table_type like '%VIEW'");
    }

    @Override
    public List<Value> selectFunctions(String schema) throws SQLException {
        String safeName = DorisDialect.INSTANCE.fmtName(false, schema);
        String sql = String.format("show full functions from %s ", safeName);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = this.providerUtils.convertFunctionName(rs);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }

    }

    private List<Value> selectByConditions(String schema, String conditions) throws SQLException {
        String safeName = DorisDialect.INSTANCE.fmtName(false, schema);
        String sql = String.format("show full tables from %s ", safeName) + conditions;
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = this.providerUtils.convertTableName(rs);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    @Override
    protected List<RdbTable> convertTables(ResultSet rs) throws SQLException {
        return this.providerUtils.convertTable(rs);
    }

    @Override
    protected List<RdbColumn> convertColumns(ResultSet rs) throws SQLException {
        return this.providerUtils.convertColumn(rs);
    }

    public List<Value> selectMaterializeds(String schema) throws SQLException {
        String sql = "select TABLE_NAME from information_schema.tables where TABLE_TYPE = 'BASE TABLE' and ENGINE is null and  TABLE_SCHEMA = ?";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schema);
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = ((DrMetaProviderUtils) this.providerUtils).convertName(rs, UmiTypes.Materialized);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectExternalTables(String catalog, String schema) throws SQLException {
        String sql = "show full tables from " + DorisDialect.INSTANCE.fmtName(false, catalog) + "." + DorisDialect.INSTANCE.fmtName(false, schema);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = ((DrMetaProviderUtils) this.providerUtils).convertExtTableName(rs);
                schemas.sort(Comparator.comparing(Value::asValue));
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectCatalogs() throws SQLException {
        String sql = "show catalogs";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schemas = ((DrMetaProviderUtils) this.providerUtils).convertCatalogName(rs);
                schemas.sort((o1, o2) -> {
                    String o1Str = o1.asValue();
                    String o2Str = o2.asValue();
                    if (o1Str.equals("internal")) {
                        return -1;
                    } else if (o2Str.equals("internal")) {
                        return 1;
                    } else {
                        return o1Str.compareTo(o2Str);
                    }
                });
                return schemas.stream().filter(value -> value.getUmiType() != null).collect(Collectors.toList());
            }
        }
    }

    public List<Value> selectSchemas(String catalog) throws SQLException {
        String safeName = DorisDialect.INSTANCE.fmtName(false, catalog);
        String sql = "show databases from " + safeName;
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> schema = ((DrMetaProviderUtils) this.providerUtils).convertSchema(catalog, rs);
                schema.sort(Comparator.comparing(Value::asValue));
                return schema;
            }
        }
    }

    public Value selectCatalog(String catalog) throws SQLException {
        final String replaceName = catalog.replace("'", "''");

        String sql = "show catalogs like '" + replaceName + "'";
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<Value> valueList = ((DrMetaProviderUtils) this.providerUtils).convertCatalog(rs);
                return valueList.stream().filter(value -> StringUtils.equals(value.asValue(), catalog)).findFirst().orElse(null);
            }
        }
    }

    @Override
    public RdbTable loadSelectObject(String catalog, String schema, String table) throws SQLException {
        RdbTable rdbTable = new RdbTable();
        rdbTable.setCatalog(catalog);
        rdbTable.setSchema(schema);
        rdbTable.setName(table);

        String sql = "show columns from " + DorisDialect.INSTANCE.fmtName(false, catalog) + "." + DorisDialect.INSTANCE.fmtName(false, schema) + "."
                     + DorisDialect.INSTANCE.fmtName(false, table);
        try (Connection conn = this.connectSupplier.eGet(); PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                int i = 1;
                while (rs.next()) {
                    RdbColumn rdbColumn = new RdbColumn();
                    rdbColumn.setCatalog(catalog);
                    rdbColumn.setSchema(schema);
                    rdbColumn.setTable(table);
                    rdbColumn.setName(rs.getString("Field"));
                    rdbColumn.setSqlType(null/*rs.getString("Type")*/);
                    rdbColumn.setIndex(i++);
                    rdbTable.addColumn(rdbColumn);
                }
            }
        }
        return rdbTable;
    }
}
