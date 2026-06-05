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
package com.clougence.clouddm.ds.mariadb.execute;

import static com.clougence.adapter.mysql.MyUmiAttributeNames.*;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clougence.adapter.mysql.MySQLOnCurrentUpdateType;
import com.clougence.adapter.mysql.MySQLTypes;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderUtils;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.special.rdb.RdbView;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.json.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * MySQL 元信息获取，参考资料：
 * <li>https://dev.mysql.com/doc/refman/8.0/en/information-schema.html</li>
 *
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-01-22
 */
@Slf4j
public class MarMetaProviderUtils extends MyMetaProviderUtils {

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

            String tableCollation = rs.getString("TABLE_COLLATION");
            if (StringUtils.isNotBlank(tableCollation)) {
                int index = tableCollation.indexOf("_");
                String charset = tableCollation.substring(0, index);

                table.setAttribute(CHARACTER_SET, charset);
                table.setAttribute(COLLATION, tableCollation);
            } else {
                table.setAttribute(CHARACTER_SET, null);
                table.setAttribute(COLLATION, null);
            }

            table.setAttribute(CREATE_TIME, rs.getString("CREATE_TIME"));
            table.setAttribute(UPDATE_TIME, rs.getString("UPDATE_TIME"));
            table.setAttribute(ENGINE, rs.getString("ENGINE"));
            table.setAttribute(ROW_FORMAT, rs.getString("ROW_FORMAT"));
            table.setAttribute(AVG_ROW_LENGTH, rs.getString("AVG_ROW_LENGTH"));
            //table.setAttribute(DEFINER, rs.getString("DEFINER"));
            //table.setAttribute(SECURITY_TYPE, rs.getString("SECURITY_TYPE"));
            //table.setAttribute(CHARACTER_SET, rs.getString("CHARACTER_SET_CLIENT"));
            //table.setAttribute(COLLATION, rs.getString("COLLATION_CONNECTION"));
            //table.setAttribute(VIEW_UPDATABLE, rs.getString("IS_UPDATABLE"));

            String createOptions = rs.getString("CREATE_OPTIONS");
            containAndSetOpt("max_rows", createOptions, MAX_ROWS, table);
            containAndSetOpt("min_rows", createOptions, MIN_ROWS, table);
            containAndSetOpt("compression", createOptions, COMPRESSION, table);
            containAndSetOpt("key_block_size", createOptions, KEY_BLOCK_SIZE, table);

            table.setAttribute(AUTO_INCREMENT_STAR, rs.getString("AUTO_INCREMENT"));
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
    public List<RdbColumn> convertColumn(ResultSet rs) throws SQLException {
        List<RdbColumn> columns = new ArrayList<>();
        Pattern pattern = Pattern.compile(PART);

        while (rs.next()) {
            String table = rs.getString("TABLE_NAME");
            String colName = rs.getString("COLUMN_NAME");
            try {
                RdbColumn column = new RdbColumn();
                column.setTable(table);
                column.setName(colName);
                MySQLTypes sqlType = safeToMySqlTypes(rs.getString("DATA_TYPE"));
                column.setSqlType(sqlType);
                if (!"YES".equalsIgnoreCase(rs.getString("IS_NULLABLE"))) {
                    column.addConstraint(new NonNull());
                }

                column.setAttribute(DATA_TYPE, rs.getString("DATA_TYPE"));
                column.setAttribute(COLUMN_TYPE, rs.getString("COLUMN_TYPE"));

                String columnType = column.getAttribute(COLUMN_TYPE);
                JDBCType jdbcType = columnTypeMappingToJdbcType(column.getSqlType(), columnType);
                column.setAttribute(JDBC_TYPE, jdbcType.getName());

                String dataDefault = rs.getString("COLUMN_DEFAULT");
                if (dataDefault != null) {
                    parseMariaDefault(dataDefault, column);
                }
                column.setDatetimePrecision(tryWasNull(rs.getInt("DATETIME_PRECISION"), rs));
                switch (sqlType) {
                    case CHAR:
                    case VARCHAR: {
                        if (tryWasNull(rs.getInt("CHARACTER_MAXIMUM_LENGTH"), rs) != null) {
                            column.setCharLength((long) rs.getInt("CHARACTER_MAXIMUM_LENGTH"));
                        }
                        break;
                    }
                    case ENUM:
                    case SET: {
                        if (StringUtils.isNotBlank(columnType)) {

                            Matcher matcher = pattern.matcher(columnType);
                            String content = "";
                            if (matcher.find()) {
                                content = matcher.group(1);
                            }

                            String[] values = content.split(",");

                            List<String> list = new ArrayList<>();
                            Collections.addAll(list, values);
                            column.setAttribute(DEFAULT_ENUMSET_COLLATION, JSON.toString(list));
                            break;
                        }
                    }
                    default: {
                        column.setNumericPrecision(tryWasNull(rs.getInt("NUMERIC_PRECISION"), rs));
                    }
                }
                column.setNumericScale(tryWasNull(rs.getInt("NUMERIC_SCALE"), rs));

                if (columnType.contains(" unsigned")) {
                    column.setAttribute(UNSIGNED, "true");
                } else {
                    column.setAttribute(UNSIGNED, "false");
                }

                if (columnType.contains(" zerofill")) {
                    column.setAttribute(ZEROFILL, "true");
                } else {
                    column.setAttribute(ZEROFILL, "false");
                }

                column.setAttribute(DEFAULT_COLLATION_NAME, rs.getString("COLLATION_NAME"));
                column.setAttribute(DEFAULT_CHARACTER_SET_NAME, rs.getString("CHARACTER_SET_NAME"));
                column.setAttribute(CHARACTERS_MAX_LENGTH, rs.getString("CHARACTER_MAXIMUM_LENGTH"));
                column.setAttribute(BYTES_MAX_LENGTH, rs.getString("CHARACTER_OCTET_LENGTH"));

                String extra = rs.getString("EXTRA");
                if (StringUtils.isNotBlank(extra)) {
                    if (StringUtils.isBlank(column.getAttribute(CURRENT_UPDATE_TYPE)) && extra.toLowerCase().contains("on update current_timestamp")) {
                        column.setAttribute(CURRENT_UPDATE_TYPE, MySQLOnCurrentUpdateType.CurrentTimestamp.getTypeName());
                    } else if (StringUtils.isBlank(column.getAttribute(CURRENT_UPDATE_TYPE)) && extra.toLowerCase().contains("on update current_date")) {
                        column.setAttribute(CURRENT_UPDATE_TYPE, MySQLOnCurrentUpdateType.CurrentDate.getTypeName());
                    } else if (StringUtils.isBlank(column.getAttribute(CURRENT_UPDATE_TYPE)) && extra.toLowerCase().contains("on update current_time")) {
                        column.setAttribute(CURRENT_UPDATE_TYPE, MySQLOnCurrentUpdateType.CurrentTime.getTypeName());
                    } else {
                        column.setAttribute(CURRENT_UPDATE_TYPE, null);
                    }
                    if (extra.equalsIgnoreCase("auto_increment")) {
                        column.setAttribute(AUTO_INCREMENT, "true");
                    } else {
                        column.setAttribute(AUTO_INCREMENT, "false");
                    }

                    if (extra.contains("VIRTUAL GENERATED")) {
                        column.setAttribute(GENERATED, "true");
                    } else {
                        column.setAttribute(GENERATED, "false");
                    }

                } else {
                    column.setAttribute(CURRENT_UPDATE_TYPE, null);
                }

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

    protected void parseMariaDefault(String dataDefault, RdbColumn column) {
        if (dataDefault == null || dataDefault.equals("NULL")) {
            return;
        }
        if (dataDefault.length() >= 2 && dataDefault.charAt(0) == '\'' && dataDefault.charAt(dataDefault.length() - 1) == '\'') {
            dataDefault = dataDefault.substring(1, dataDefault.length() - 1);
            dataDefault = dataDefault.replace("\\'", "'");
        }

        parseDefault(dataDefault, column);
    }
}
