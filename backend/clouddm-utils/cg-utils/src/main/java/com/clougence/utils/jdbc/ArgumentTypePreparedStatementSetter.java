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
package com.clougence.utils.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.*;
import java.time.chrono.JapaneseDate;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * copy from SpringJDBC
 * process PreparedStatement setValues
 * @version : 2013-10-9
 * @author Thomas Risberg
 * @author Juergen Hoeller
 * @author 赵永春 (zyc@hasor.net)
 */
public class ArgumentTypePreparedStatementSetter {

    private static final Map<String, Integer> javaTypeToJdbcTypeMap = new ConcurrentHashMap<>();

    static {
        // primitive and wrapper
        javaTypeToJdbcTypeMap.put(Boolean.class.getName(), Types.BIT);
        javaTypeToJdbcTypeMap.put(boolean.class.getName(), Types.BIT);
        javaTypeToJdbcTypeMap.put(Byte.class.getName(), Types.TINYINT);
        javaTypeToJdbcTypeMap.put(byte.class.getName(), Types.TINYINT);
        javaTypeToJdbcTypeMap.put(Short.class.getName(), Types.SMALLINT);
        javaTypeToJdbcTypeMap.put(short.class.getName(), Types.SMALLINT);
        javaTypeToJdbcTypeMap.put(Integer.class.getName(), Types.INTEGER);
        javaTypeToJdbcTypeMap.put(int.class.getName(), Types.INTEGER);
        javaTypeToJdbcTypeMap.put(Long.class.getName(), Types.BIGINT);
        javaTypeToJdbcTypeMap.put(long.class.getName(), Types.BIGINT);
        javaTypeToJdbcTypeMap.put(Float.class.getName(), Types.FLOAT);
        javaTypeToJdbcTypeMap.put(float.class.getName(), Types.FLOAT);
        javaTypeToJdbcTypeMap.put(Double.class.getName(), Types.DOUBLE);
        javaTypeToJdbcTypeMap.put(double.class.getName(), Types.DOUBLE);
        javaTypeToJdbcTypeMap.put(Character.class.getName(), Types.CHAR);
        javaTypeToJdbcTypeMap.put(char.class.getName(), Types.CHAR);
        // java time
        javaTypeToJdbcTypeMap.put(java.util.Date.class.getName(), Types.TIMESTAMP);
        javaTypeToJdbcTypeMap.put(java.sql.Date.class.getName(), Types.DATE);
        javaTypeToJdbcTypeMap.put(java.sql.Timestamp.class.getName(), Types.TIMESTAMP);
        javaTypeToJdbcTypeMap.put(java.sql.Time.class.getName(), Types.TIME);
        javaTypeToJdbcTypeMap.put(Instant.class.getName(), Types.TIMESTAMP);
        javaTypeToJdbcTypeMap.put(LocalDateTime.class.getName(), Types.TIMESTAMP);
        javaTypeToJdbcTypeMap.put(LocalDate.class.getName(), Types.DATE);
        javaTypeToJdbcTypeMap.put(LocalTime.class.getName(), Types.TIME);
        javaTypeToJdbcTypeMap.put(ZonedDateTime.class.getName(), Types.TIMESTAMP_WITH_TIMEZONE);
        javaTypeToJdbcTypeMap.put(JapaneseDate.class.getName(), Types.TIMESTAMP);
        javaTypeToJdbcTypeMap.put(YearMonth.class.getName(), Types.VARCHAR);
        javaTypeToJdbcTypeMap.put(Year.class.getName(), Types.SMALLINT);
        javaTypeToJdbcTypeMap.put(Month.class.getName(), Types.SMALLINT);
        javaTypeToJdbcTypeMap.put(OffsetDateTime.class.getName(), Types.TIMESTAMP_WITH_TIMEZONE);
        javaTypeToJdbcTypeMap.put(OffsetTime.class.getName(), Types.TIME_WITH_TIMEZONE);
        // java extensions Types
        javaTypeToJdbcTypeMap.put(String.class.getName(), Types.VARCHAR);
        javaTypeToJdbcTypeMap.put(BigInteger.class.getName(), Types.BIGINT);
        javaTypeToJdbcTypeMap.put(BigDecimal.class.getName(), Types.DECIMAL);
        javaTypeToJdbcTypeMap.put(Reader.class.getName(), Types.CLOB);
        javaTypeToJdbcTypeMap.put(InputStream.class.getName(), Types.BLOB);
        javaTypeToJdbcTypeMap.put(URL.class.getName(), Types.DATALINK);
        javaTypeToJdbcTypeMap.put(URI.class.getName(), Types.DATALINK);
        javaTypeToJdbcTypeMap.put(Byte[].class.getName(), Types.VARBINARY);
        javaTypeToJdbcTypeMap.put(byte[].class.getName(), Types.VARBINARY);
        javaTypeToJdbcTypeMap.put(Object[].class.getName(), Types.ARRAY);
        javaTypeToJdbcTypeMap.put(Object.class.getName(), Types.JAVA_OBJECT);
        // oracle types
        javaTypeToJdbcTypeMap.put("oracle.jdbc.OracleBlob", Types.BLOB);
        javaTypeToJdbcTypeMap.put("oracle.jdbc.OracleClob", Types.CLOB);
        javaTypeToJdbcTypeMap.put("oracle.jdbc.OracleNClob", Types.NCLOB);
        javaTypeToJdbcTypeMap.put("oracle.sql.DATE", Types.DATE);
        javaTypeToJdbcTypeMap.put("oracle.sql.TIMESTAMP", Types.TIMESTAMP);
        javaTypeToJdbcTypeMap.put("oracle.sql.TIMESTAMPTZ", Types.TIMESTAMP_WITH_TIMEZONE);
        javaTypeToJdbcTypeMap.put("oracle.sql.TIMESTAMPLTZ", Types.TIMESTAMP_WITH_TIMEZONE);
    }

    public static void setValues(PreparedStatement ps, Object[] args) throws SQLException {
        int parameterPosition = 1;
        if (args != null) {
            int[] argTypes = new int[args.length];
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                argTypes[i] = Types.OTHER;

                if (arg != null) {
                    String typeName = arg.getClass().getName();
                    if (javaTypeToJdbcTypeMap.containsKey(typeName)) {
                        argTypes[i] = javaTypeToJdbcTypeMap.get(typeName);
                    }
                }

                if (arg instanceof Collection && argTypes[i] != Types.ARRAY) {
                    Collection<?> entries = (Collection<?>) arg;
                    for (Object entry : entries) {
                        if (entry instanceof Object[]) {
                            Object[] valueArray = ((Object[]) entry);
                            for (Object argValue : valueArray) {
                                doSetValue(ps, parameterPosition, argTypes[i], argValue);
                                parameterPosition++;
                            }
                        } else {
                            doSetValue(ps, parameterPosition, argTypes[i], entry);
                            parameterPosition++;
                        }
                    }
                } else {
                    doSetValue(ps, parameterPosition, argTypes[i], arg);
                    parameterPosition++;
                }
            }
        }
    }

    /**
     * Set the value for the prepared statement's specified parameter position using the passed in
     * value and type. This method can be overridden by subclasses if needed.
     * @param ps the PreparedStatement
     * @param parameterPosition index of the parameter position
     * @param argType the argument type
     * @param argValue the argument value
     * @throws SQLException if thrown by PreparedStatement methods
     */
    protected static void doSetValue(PreparedStatement ps, int parameterPosition, int argType, Object argValue) throws SQLException {
        if (argValue == null) {
            setNull(ps, parameterPosition, argType);
        } else {
            setValue(ps, parameterPosition, argType, argValue);
        }
    }

    /** Set the specified PreparedStatement parameter to null, respecting database-specific peculiarities. */
    private static void setNull(PreparedStatement ps, int paramIndex, int sqlType) throws SQLException {
        if (sqlType == Types.OTHER) {
            boolean useSetObject = false;
            Integer sqlTypeToUse = null;
            // Proceed with database-specific checks
            sqlTypeToUse = Types.NULL;
            DatabaseMetaData dbmd = ps.getConnection().getMetaData();
            String jdbcDriverName = dbmd.getDriverName();
            String databaseProductName = dbmd.getDatabaseProductName();
            if (databaseProductName.startsWith("Informix") || (jdbcDriverName.startsWith("Microsoft") && jdbcDriverName.contains("SQL Server"))) {
                // "Microsoft SQL Server JDBC Driver 3.0" versus "Microsoft JDBC Driver 4.0 for SQL Server"
                useSetObject = true;
            } else if (databaseProductName.startsWith("DB2") || jdbcDriverName.startsWith("jConnect") || jdbcDriverName.startsWith("SQLServer") || jdbcDriverName.startsWith("Apache Derby")) {
                sqlTypeToUse = Types.VARCHAR;
            }

            if (useSetObject) {
                ps.setObject(paramIndex, null);
            } else {
                ps.setNull(paramIndex, sqlTypeToUse);
            }
        } else {
            ps.setNull(paramIndex, sqlType);
        }
    }

    private static void setValue(PreparedStatement ps, int paramIndex, int sqlType, Object inValue) throws SQLException {

        if (sqlType == Types.VARCHAR || sqlType == Types.LONGVARCHAR) {
            ps.setString(paramIndex, inValue.toString());
        } else if (sqlType == Types.NVARCHAR || sqlType == Types.LONGNVARCHAR) {
            ps.setNString(paramIndex, inValue.toString());
        } else if ((sqlType == Types.CLOB || sqlType == Types.NCLOB) && isStringValue(inValue.getClass())) {
            String strVal = inValue.toString();
            if (strVal.length() > 4000) {
                // Necessary for older Oracle drivers, in particular when running against an Oracle 10 database.
                // Should also work fine against other drivers/databases since it uses standard JDBC 4.0 API.
                if (sqlType == Types.NCLOB) {
                    ps.setNClob(paramIndex, new StringReader(strVal), strVal.length());
                } else {
                    ps.setClob(paramIndex, new StringReader(strVal), strVal.length());
                }
            } else {
                // Fallback: setString or setNString binding
                if (sqlType == Types.NCLOB) {
                    ps.setNString(paramIndex, strVal);
                } else {
                    ps.setString(paramIndex, strVal);
                }
            }
        } else if (sqlType == Types.DECIMAL || sqlType == Types.NUMERIC) {
            if (inValue instanceof BigDecimal) {
                ps.setBigDecimal(paramIndex, (BigDecimal) inValue);
                //            } else if (scale != null) {
                //                ps.setObject(paramIndex, inValue, sqlType, scale);
            } else {
                ps.setObject(paramIndex, inValue, sqlType);
            }
        } else if (sqlType == Types.BOOLEAN) {
            if (inValue instanceof Boolean) {
                ps.setBoolean(paramIndex, (Boolean) inValue);
            } else {
                ps.setObject(paramIndex, inValue, Types.BOOLEAN);
            }
        } else if (sqlType == Types.DATE) {
            if (inValue instanceof java.util.Date) {
                if (inValue instanceof java.sql.Date) {
                    ps.setDate(paramIndex, (java.sql.Date) inValue);
                } else {
                    ps.setDate(paramIndex, new java.sql.Date(((java.util.Date) inValue).getTime()));
                }
            } else if (inValue instanceof Calendar) {
                Calendar cal = (Calendar) inValue;
                ps.setDate(paramIndex, new java.sql.Date(cal.getTime().getTime()), cal);
            } else {
                ps.setObject(paramIndex, inValue, Types.DATE);
            }
        } else if (sqlType == Types.TIME) {
            if (inValue instanceof java.util.Date) {
                if (inValue instanceof java.sql.Time) {
                    ps.setTime(paramIndex, (java.sql.Time) inValue);
                } else {
                    ps.setTime(paramIndex, new java.sql.Time(((java.util.Date) inValue).getTime()));
                }
            } else if (inValue instanceof Calendar) {
                Calendar cal = (Calendar) inValue;
                ps.setTime(paramIndex, new java.sql.Time(cal.getTime().getTime()), cal);
            } else {
                ps.setObject(paramIndex, inValue, Types.TIME);
            }
        } else if (sqlType == Types.TIMESTAMP) {
            if (inValue instanceof java.util.Date) {
                if (inValue instanceof java.sql.Timestamp) {
                    ps.setTimestamp(paramIndex, (java.sql.Timestamp) inValue);
                } else {
                    ps.setTimestamp(paramIndex, new java.sql.Timestamp(((java.util.Date) inValue).getTime()));
                }
            } else if (inValue instanceof Calendar) {
                Calendar cal = (Calendar) inValue;
                ps.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()), cal);
            } else {
                ps.setObject(paramIndex, inValue, Types.TIMESTAMP);
            }
        } else if ((sqlType == Types.OTHER && "Oracle".equals(ps.getConnection().getMetaData().getDatabaseProductName()))) {
            if (isStringValue(inValue.getClass())) {
                ps.setString(paramIndex, inValue.toString());
            } else if (isDateValue(inValue.getClass())) {
                ps.setTimestamp(paramIndex, new java.sql.Timestamp(((java.util.Date) inValue).getTime()));
            } else if (inValue instanceof Calendar) {
                Calendar cal = (Calendar) inValue;
                ps.setTimestamp(paramIndex, new java.sql.Timestamp(cal.getTime().getTime()), cal);
            } else {
                // Fall back to generic setObject call without SQL type specified.
                ps.setObject(paramIndex, inValue);
            }
        } else {
            // Fall back to generic setObject call with SQL type specified.
            ps.setObject(paramIndex, inValue, sqlType);
        }
    }

    /** Check whether the given value can be treated as a String value. */
    private static boolean isStringValue(Class<?> inValueType) {
        // Consider any CharSequence (including StringBuffer and StringBuilder) as a String.
        return (CharSequence.class.isAssignableFrom(inValueType) || StringWriter.class.isAssignableFrom(inValueType));
    }

    /** Check whether the given value is a {@code java.util.Date} (but not one of the JDBC-specific subclasses). */
    private static boolean isDateValue(Class<?> inValueType) {
        return (java.util.Date.class.isAssignableFrom(inValueType) //
                && !(java.sql.Date.class.isAssignableFrom(inValueType) //
                     || java.sql.Time.class.isAssignableFrom(inValueType) //
                     || java.sql.Timestamp.class.isAssignableFrom(inValueType)));
    }
}
