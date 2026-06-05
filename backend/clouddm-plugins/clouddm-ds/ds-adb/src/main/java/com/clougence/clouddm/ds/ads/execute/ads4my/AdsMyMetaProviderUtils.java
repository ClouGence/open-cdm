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
package com.clougence.clouddm.ds.ads.execute.ads4my;

import static com.clougence.adapter.adbmysql.AdsMyUmiAttributeNames.*;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clougence.adapter.adbmysql.domain.AdbMySQLTypes;
import com.clougence.adapter.mysql.MySQLOnCurrentUpdateType;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderUtils;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Adb For MySql 3.0 metadata
 * <li>https://dev.mysql.com/doc/refman/8.0/en/information-schema.html</li>
 * <li>https://help.aliyun.com/document_detail/197326.html</li>
 *
 * @version : 2020-01-22
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public class AdsMyMetaProviderUtils extends MyMetaProviderUtils {

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
                AdbMySQLTypes sqlType = safeToAdbMySqlTypes(rs.getString("DATA_TYPE"));
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
                    parseDefault(dataDefault, column);
                }
                column.setDatetimePrecision(tryWasNull(rs.getInt("DATETIME_PRECISION"), rs));
                switch (sqlType) {
                    case VARCHAR: {
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

                    if (extra.contains("GENERATED")) {
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

    protected AdbMySQLTypes safeToAdbMySqlTypes(Object obj) {
        String dat = (obj == null) ? null : obj.toString();
        for (AdbMySQLTypes type : AdbMySQLTypes.values()) {
            if (type.getCodeKey().equalsIgnoreCase(dat)) {
                return type;
            }
        }
        return null;
    }
}
