package com.clougence.clouddm.ds.starrocks.execute;

import static com.clougence.adapter.starrocks.StarRocksAttributeNames.*;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.starrocks.StarRocksTypes;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/12/2
 **/
@Slf4j
public class SrMetaProviderUtils {

    public static String INTERNAL_CATALOG = "default_catalog";

    public static List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString(1));
            schema.setUmiType(UmiTypes.Schema);
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertSchema(String catalog, ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString(1));
            if (INTERNAL_CATALOG.equals(catalog)) {
                schema.setUmiType(UmiTypes.Schema);
            } else {
                schema.setUmiType(UmiTypes.ExternalSchema);
            }
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertExtTableName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString(1));
            String tableType = rs.getString(2);
            schema.setUmiType(UmiTypes.ExternalTable);
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertMaterializedName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("name"));
            schema.setUmiType(UmiTypes.Materialized);
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertFunctionName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString(1));
            schema.setUmiType(UmiTypes.Function);
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertTableName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString(1));
            String tableType = rs.getString(2);

            if (StringUtils.equalsIgnoreCase(tableType, "VIEW") || StringUtils.equalsIgnoreCase(tableType, "SYSTEM VIEW")) {
                schema.setUmiType(UmiTypes.View);
            } else {
                schema.setUmiType(UmiTypes.Table);
            }

            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertExternalTableName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            String tableName = rs.getString(1);

            schema.setValue(tableName);
            schema.setUmiType(UmiTypes.ExternalTable);

            result.add(schema);
        }
        return result;
    }

    public static List<RdbTable> convertTable(ResultSet rs) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            RdbTable table = new RdbTable();
            table.setCatalog(rs.getString("TABLE_CATALOG"));
            table.setSchema(rs.getString("TABLE_SCHEMA"));
            table.setName(rs.getString("TABLE_NAME"));
            table.setComment(rs.getString("TABLE_COMMENT"));
            String tableType = rs.getString("TABLE_TYPE");
            if (StringUtils.endsWith(tableType, "VIEW")) {
                table.setUmiType(UmiTypes.View);
                table.setTableType(tableType);
            } else {
                table.setUmiType(UmiTypes.Table);
                table.setTableType(tableType);
            }

            String tableCollation = rs.getString("TABLE_COLLATION");
            if (StringUtils.isNotBlank(tableCollation)) {
                table.setAttribute(CHARACTER_SET, tableCollation);
                table.setAttribute(COLLATION, tableCollation);
            } else {
                table.setAttribute(CHARACTER_SET, null);
                table.setAttribute(COLLATION, null);
            }

            table.setAttribute(CREATE_TIME, rs.getString("CREATE_TIME"));
            table.setAttribute(UPDATE_TIME, rs.getString("UPDATE_TIME"));

            result.add(table);
        }
        return result;
    }

    public static List<RdbColumn> convertColumn(ResultSet rs) throws SQLException {
        List<RdbColumn> columns = new ArrayList<>();
        while (rs.next()) {
            String table = rs.getString("TABLE_NAME");
            String colName = rs.getString("COLUMN_NAME");
            try {
                RdbColumn column = new RdbColumn();
                column.setTable(table);
                column.setName(colName);

                String dataType = rs.getString("DATA_TYPE");
                String columnType = rs.getString("COLUMN_TYPE");

                column.setSqlType(StarRocksTypes.valueOfCode(rs.getString("DATA_TYPE")));
                if (!"YES".equalsIgnoreCase(rs.getString("IS_NULLABLE"))) {
                    column.addConstraint(new NonNull());
                }

                column.setAttribute(DATA_TYPE, dataType);
                column.setAttribute(COLUMN_TYPE, columnType);

                String dataDefault = rs.getString("COLUMN_DEFAULT");
                if (dataDefault != null) {
                    passerDefault(dataDefault, column);
                }
                column.setDatetimePrecision(tryWasNull(rs.getInt("DATETIME_PRECISION"), rs));
                column.setNumericPrecision(tryWasNull(rs.getInt("NUMERIC_PRECISION"), rs));
                column.setNumericScale(tryWasNull(rs.getInt("NUMERIC_SCALE"), rs));

                column.setAttribute(DEFAULT_COLLATION_NAME, rs.getString("COLLATION_NAME"));
                column.setAttribute(DEFAULT_CHARACTER_SET_NAME, rs.getString("CHARACTER_SET_NAME"));
                column.setAttribute(CHARACTERS_MAX_LENGTH, rs.getString("CHARACTER_MAXIMUM_LENGTH"));
                column.setAttribute(BYTES_MAX_LENGTH, rs.getString("CHARACTER_OCTET_LENGTH"));

                column.setComment(rs.getString("COLUMN_COMMENT"));
                column.setIndex(rs.getInt("ORDINAL_POSITION"));

                columns.add(column);
            } catch (Exception e) {
                String msg = "extract " + table + " column (" + colName + ") error.msg:" + ExceptionUtils.getRootCauseMessage(e);
                log.error(msg, e);
                throw new SQLException(msg, e);
            }
        }
        return columns;
    }

    private static void passerDefault(String dataDefault, RdbColumn column) {
        boolean isDate = column.getSqlType() == StarRocksTypes.DATE //
                         || column.getSqlType() == StarRocksTypes.DATETIME;
        boolean isFunc = false;
        isFunc = isFunc | StringUtils.startsWithIgnoreCase(dataDefault, "CURRENT_TIMESTAMP");

        if (isDate && isFunc) {
            column.setDefaultValue(dataDefault);
            column.setDefaultValueIsFunc(true);
        } else {
            column.setDefaultValueIsFunc(false);
            if (isDate) {
                DateFormatType dateFormatType = DateFormatType.passerTypeWithoutUnsupported(dataDefault);
                if (dateFormatType != null) {
                    column.setAttribute(DEFAULT_VALUE_OF_TIME_TYPE, dateFormatType.name());
                    column.setDefaultValue(dataDefault);
                }
            } else {
                column.setDefaultValue(dataDefault);
            }
        }
    }

    public static List<Value> convertCatalogName(ResultSet rs) throws SQLException {
        List<Value> values = new ArrayList<>();
        while (rs.next()) {
            RdbValue catalog = new RdbValue();
            catalog.setValue(rs.getString("Catalog"));
            String type = rs.getString("Type");
            if (type.equals("Internal")) {
                catalog.setUmiType(UmiTypes.Catalog);
            } else {
                catalog.setUmiType(UmiTypes.ExternalCatalog);
            }
            values.add(catalog);
        }
        return values;
    }

    public static List<Value> convertCatalog(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString(3));
            schema.setUmiType(UmiTypes.Catalog);
            result.add(schema);
        }
        return result;
    }
}
