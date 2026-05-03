package com.clougence.clouddm.ds.clickhouse.execute;

import static com.clougence.adapter.clickhouse.ClickHouseAttributeNames.*;
import static com.clougence.schema.umi.special.rdb.RdbAttributeNames.COMMENT;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.clickhouse.ClickHouseTypes;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * clickhouse
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class ChMetaProviderUtils {

    public static List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("name"));
            schema.setUmiType(UmiTypes.Schema);
            schema.setAttribute(ENGINE, rs.getString("engine"));
            result.add(schema);
        }
        return result;
    }

    public static List<Value> convertTableName(ResultSet rs, boolean supportComment) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("name"));

            String engine = rs.getString("engine");
            if (StringUtils.endsWithIgnoreCase(engine, "View")) {
                schema.setUmiType(UmiTypes.View);
            } else {
                schema.setUmiType(UmiTypes.Table);
            }

            schema.setAttribute(ENGINE, engine);
            schema.setAttribute(COMMENT, supportComment ? rs.getString("comment") : null);
            result.add(schema);
        }
        return result;
    }

    public static List<RdbTable> convertTable(ResultSet rs, boolean supportComment) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            RdbTable table = new RdbTable();
            table.setUmiType(UmiTypes.Table);

            table.setSchema(rs.getString("database"));
            table.setName(rs.getString("name"));
            table.setTableType(rs.getString("engine"));
            table.setComment(supportComment ? rs.getString("comment") : null);

            table.setAttribute(DATA_PATH, rs.getString("data_paths"));
            table.setAttribute(METADATA_PATH, rs.getString("metadata_path"));
            table.setAttribute(ENGINE, rs.getString("engine"));
            table.setAttribute(ENGINE_FULL, rs.getString("engine_full"));
            table.setAttribute(TEMPORARY, String.valueOf(rs.getBoolean("is_temporary")));
            result.add(table);
        }
        return result;
    }

    public static List<RdbColumn> convertColumn(ResultSet rs) throws SQLException {
        List<RdbColumn> result = new ArrayList<>();
        while (rs.next()) {
            RdbColumn rdbColumn = new RdbColumn();
            rdbColumn.setUmiType(UmiTypes.Column);
            rdbColumn.setTable(rs.getString("table"));
            rdbColumn.setName(rs.getString("name"));
            rdbColumn.setComment(rs.getString("comment"));
            rdbColumn.setIndex(rs.getInt("position"));

            String columnType = rs.getString("type");
            String dataType = columnType;
            if (StringUtils.startsWithIgnoreCase(columnType, "NULLABLE(") && StringUtils.endsWithIgnoreCase(columnType, ")")) {
                dataType = columnType.substring("NULLABLE(".length(), columnType.length() - 1);
            } else {
                rdbColumn.addConstraint(new NonNull());
            }

            rdbColumn.setSqlType(safeToChTypes(dataType));
            passerDatetime(dataType, rdbColumn);
            passerNumeric(dataType, rdbColumn);
            passerLength(dataType, rdbColumn);

            // default
            String defaultDataKind = rs.getString("default_kind");
            String defaultDataExpr = rs.getString("default_expression");
            if (StringUtils.isNotBlank(defaultDataKind) || StringUtils.isNotBlank(defaultDataExpr)) {
                passerDefault(defaultDataKind, defaultDataExpr, rdbColumn);
            }

            // attr
            rdbColumn.setAttribute(PARTITION_KEY, StringUtils.toString("1".equals(rs.getString("is_in_partition_key"))));
            rdbColumn.setAttribute(SORTING_KEY, StringUtils.toString("1".equals(rs.getString("is_in_sorting_key"))));
            rdbColumn.setAttribute(SAMPLING_KEY, StringUtils.toString("1".equals(rs.getString("is_in_sampling_key"))));

            result.add(rdbColumn);
        }
        return result;
    }

    private static ClickHouseTypes safeToChTypes(String dataType) {
        String dataTypeTmp = dataType;
        if (dataType.contains("(")) {
            dataTypeTmp = dataType.substring(0, dataType.indexOf("("));
        }

        for (ClickHouseTypes type : ClickHouseTypes.values()) {
            if (dataTypeTmp.equals(type.getCodeKey())) {
                return type;
            }
        }
        return null;
    }

    private static void passerDatetime(String dataType, RdbColumn column) {
        ClickHouseTypes sqlType = (ClickHouseTypes) column.getSqlType();
        switch (sqlType) {
            case Date:
            case DateTime:
            case DateTime32:
            case DateTime64:
                break;
            default:
                return;
        }

        // use default first
        column.setDatetimePrecision(sqlType.getDriverType().getDefaultPrecision());

        if (dataType.contains("(") && dataType.endsWith(")")) {
            int typeDescIndex = dataType.indexOf("(");
            String typeDesc = dataType.substring(typeDescIndex + 1, dataType.length() - 1);

            String[] descInfos = typeDesc.split(",");
            if (descInfos.length == 1) {
                if (descInfos[0].charAt(0) == '\'' && descInfos[0].charAt(descInfos[0].length() - 1) == '\'') {
                    column.setAttribute(DATETIME_ZONE, descInfos[0].substring(1, descInfos[0].length() - 1));
                } else {
                    column.setDatetimePrecision(Integer.parseInt(descInfos[0].trim()));
                }
            } else if (descInfos.length == 2) {
                descInfos[1] = descInfos[1].trim();
                column.setDatetimePrecision(Integer.parseInt(descInfos[0].trim()));
                column.setAttribute(DATETIME_ZONE, descInfos[1].substring(1, descInfos[1].length() - 1));
            }
        }
    }

    private static void passerNumeric(String dataType, RdbColumn column) {
        ClickHouseTypes sqlType = (ClickHouseTypes) column.getSqlType();
        switch (sqlType) {
            case Decimal32:
            case Decimal64:
            case Decimal128:
            case Decimal256:
            case Decimal:
                break;
            default: {
                return;
            }
        }

        // use default first
        column.setNumericUnsigned(sqlType.isUnsigned());
        column.setNumericPrecision(sqlType.getDriverType().getDefaultPrecision());
        column.setNumericScale(sqlType.getDriverType().getDefaultScale());

        String typeName = dataType;
        String typeDesc = "";
        if (dataType.contains("(") && dataType.endsWith(")")) {
            int typeDescIndex = dataType.indexOf("(");
            typeName = dataType.substring(0, typeDescIndex);
            typeDesc = dataType.substring(typeDescIndex + 1, dataType.length() - 1);
        }

        String[] descNumbers = typeDesc.split(",");
        if (descNumbers.length > 0) {
            column.setNumericPrecision(Integer.parseInt(descNumbers[0].trim()));
        }
        if (descNumbers.length > 1) {
            column.setNumericScale(Integer.parseInt(descNumbers[1].trim()));
        }
    }

    private static void passerLength(String dataType, RdbColumn column) {
        ClickHouseTypes sqlType = (ClickHouseTypes) column.getSqlType();
        switch (sqlType) {
            case FixedString:
                break;
            default: {
                return;
            }
        }

        int typeDescIndex = dataType.indexOf("(");
        String typeDesc = dataType.substring(typeDescIndex + 1, dataType.length() - 1);
        long longValue = Long.parseLong(typeDesc.trim());

        column.setCharLength(longValue);
        column.setByteLength(longValue);
    }

    private static void passerDefault(String defaultDataKind, String dataDefault, RdbColumn rdbColumn) {
        //    private String          defaultValue;
        //    private String          defaultFunction;
        //    private DateFormatType  defaultValueOfTimeType;
    }
}
