package com.clougence.clouddm.ds.oceanbase.execute.obformysql;

import static com.clougence.adapter.ob.obformysql.ObForMySQLAttributeNames.*;
import static com.clougence.utils.jdbc.JdbcUtils.tryWasNull;

import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clougence.adapter.mysql.MySQLOnCurrentUpdateType;
import com.clougence.adapter.ob.obformysql.ObForMySQLTypes;
import com.clougence.clouddm.dsfamily.mysql.execute.MyMetaProviderUtils;
import com.clougence.schema.umi.special.rdb.RdbColumn;
import com.clougence.schema.umi.struts.constraint.NonNull;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.format.DateFormatType;
import com.clougence.utils.json.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-01-22
 */
@Slf4j
public class ObForMySQLMetaProviderUtils extends MyMetaProviderUtils {

    private final Pattern pattern = Pattern.compile(PART);

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

                ObForMySQLTypes sqlType = safeToOceanBaseTypes(rs.getString("DATA_TYPE"));
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

    private static void passerDefault(String dataDefault, RdbColumn column) {
        boolean isDate = column.getSqlType() == ObForMySQLTypes.DATE //
                         || column.getSqlType() == ObForMySQLTypes.DATETIME;
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

    protected static ObForMySQLTypes safeToOceanBaseTypes(Object obj) {
        String dat = (obj == null) ? null : obj.toString();
        for (ObForMySQLTypes type : ObForMySQLTypes.values()) {
            if (type.getCodeKey().equalsIgnoreCase(dat)) {
                return type;
            }
        }
        return null;
    }
}
