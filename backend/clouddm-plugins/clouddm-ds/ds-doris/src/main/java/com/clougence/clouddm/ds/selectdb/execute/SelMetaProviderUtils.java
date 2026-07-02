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
package com.clougence.clouddm.ds.selectdb.execute;

import static com.clougence.adapter.selectdb.SelDorisAttributeNames.*;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.clougence.adapter.doris.DorisTypes;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderUtils;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbValue;
import com.clougence.schema.umi.special.rdb.RdbView;
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
public class SelMetaProviderUtils extends MyMetaProviderUtils {

    public static final String  INTERNAL_CATALOG = "internal";

    private static final String UNKNOWN          = "unknown";

    @Override
    public List<Value> convertSchema(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString(1));
            schema.setUmiType(UmiTypes.Schema);
            result.add(schema);
        }
        return result;
    }

    @Override
    public List<RdbTable> convertView(ResultSet rs) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            RdbView table = new RdbView();
            table.setSchema(rs.getString("TABLE_SCHEMA"));
            table.setName(rs.getString("TABLE_NAME"));
            table.setComment(rs.getString("TABLE_COMMENT"));
            table.setSql(rs.getString("VIEW_DEFINITION"));
            String tableType = rs.getString("TABLE_TYPE");
            if (StringUtils.endsWith(tableType, "VIEW")) {
                table.setUmiType(UmiTypes.View);
                table.setTableType(tableType);
            } else {
                table.setUmiType(UmiTypes.Table);
                table.setTableType(tableType);
            }

            if (tableType.equalsIgnoreCase("TEMPORARY")) {
                table.setAttribute(TEMPORARY, "true");
            } else {
                table.setAttribute(TEMPORARY, "false");
            }
            Map<String, Object> features = new HashMap<>();
            String checkOption = rs.getString("CHECK_OPTION");
            if (!"NONE".equals(checkOption)) {
                features.put(VIEW_CHECK_OPTION.getCodeKey(), checkOption);
            }
            table.setFeatures(features);
            result.add(table);
        }
        return result;
    }

    @Override
    public List<Value> convertTableName(ResultSet rs) throws SQLException {
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

    public List<Value> convertName(ResultSet rs, UmiTypes type) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString(1));
            schema.setUmiType(type);
            result.add(schema);
        }
        return result;
    }

    public List<Value> convertCatalogName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString(2));
            String type = rs.getString(3);
            if (!type.equals("internal")) {
                schema.setAttribute(JUST_TABLE.getCodeKey(), "true");
            }
            schema.setUmiType(UmiTypes.Catalog);
            result.add(schema);
        }
        return result;
    }

    @Override
    public List<Value> convertFunctionName(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue routine = new RdbValue();

            // originalName  with () and param type
            String originalName = rs.getString("Signature");
            String functionName = originalName.substring(0, originalName.lastIndexOf('('));

            // setting main params
            routine.setValue(functionName);
            routine.setUmiType(UmiTypes.Function);
            result.add(routine);
        }
        return result;
    }

    @Override
    public List<RdbTable> convertTable(ResultSet rs) throws SQLException {
        List<RdbTable> result = new ArrayList<>();
        while (rs.next()) {
            RdbTable table = new RdbTable();
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

            table.setAttribute(AUTO_INCREMENT_STAR, rs.getString("AUTO_INCREMENT"));
            if (tableType.equalsIgnoreCase("TEMPORARY")) {
                table.setAttribute(TEMPORARY, "true");
            } else {
                table.setAttribute(TEMPORARY, "false");
            }
            result.add(table);
        }
        return result;
    }

    @Override
    public List<RdbColumn> convertColumn(ResultSet rs) throws SQLException {
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

                DorisTypes sqlType = UNKNOWN.equalsIgnoreCase(dataType) ? DorisTypes.valueOfCode(columnType) : DorisTypes.valueOfCode(dataType);
                column.setSqlType(sqlType);
                if (!"YES".equalsIgnoreCase(rs.getString("IS_NULLABLE"))) {
                    column.addConstraint(new NonNull());
                }

                column.setAttribute(DATA_TYPE, dataType);
                column.setAttribute(COLUMN_TYPE, columnType);

                JDBCType jdbcType = columnTypeMappingToJdbcType(column.getSqlType(), columnType);
                column.setAttribute(JDBC_TYPE, jdbcType.getName());

                String dataDefault = rs.getString("COLUMN_DEFAULT");
                if (dataDefault != null) {
                    passerDefault(dataDefault, column);
                }
                column.setDatetimePrecision(tryWasNull(rs.getInt("DATETIME_PRECISION"), rs));
                switch (sqlType) {
                    case CHAR:
                    case VARCHAR:
                    case STRING: {
                        if (tryWasNull(rs.getInt("CHARACTER_MAXIMUM_LENGTH"), rs) != null) {
                            column.setCharLength((long) rs.getInt("CHARACTER_MAXIMUM_LENGTH"));
                        }
                        break;
                    }
                    default: {
                        column.setNumericPrecision(tryWasNull(rs.getInt("NUMERIC_PRECISION"), rs));
                    }
                }
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

    private void passerDefault(String dataDefault, RdbColumn column) {
        boolean isDate = column.getSqlType() == DorisTypes.DATE //
                         || column.getSqlType() == DorisTypes.DATETIME;
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

    public List<Value> convertCatalog(ResultSet rs) throws SQLException {
        List<Value> result = new ArrayList<>();
        while (rs.next()) {
            RdbValue schema = new RdbValue();
            schema.setValue(rs.getString("CatalogName"));
            schema.setUmiType(UmiTypes.Catalog);
            result.add(schema);
        }
        return result;
    }
}
