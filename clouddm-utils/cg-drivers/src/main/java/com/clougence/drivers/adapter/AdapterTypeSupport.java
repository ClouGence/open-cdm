/*
 * Copyright 2015-2022 the original author or authors.
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
package com.clougence.drivers.adapter;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.sql.Types;
import java.time.*;
import java.time.chrono.JapaneseDate;
import java.util.*;

import com.clougence.utils.StringUtils;
import com.clougence.utils.convert.ConverterUtils;
import com.clougence.utils.ref.Tuple;

public class AdapterTypeSupport implements TypeSupport {

    private static final Tuple                          defaultTypeTuple    = Tuple.of(Types.OTHER, Object.class);
    private static final TypeConvert                    defaultConverter    = ConverterUtils::convert;

    private final Map<Class<?>, String>                 classToNameMap      = new HashMap<>();
    private final Map<Integer, String>                  numberTypeToNameMap = new HashMap<>();
    private final Map<String, String>                   aliasMap            = new HashMap<>();
    private final Map<String, Tuple>                    typeMap             = new HashMap<>();
    private final Map<String, Map<String, TypeConvert>> typeConvertMap      = new HashMap<>();

    public AdapterTypeSupport(Properties properties){
        this.initNumberTypeMapping(properties);
        this.initClassTypeMapping(properties);
        this.initTypeAlias(properties);
        this.initTypeMappingTo(properties);
        this.initTypeConvert(properties);
    }

    // from jdbcType to NameType.
    protected void initNumberTypeMapping(Properties properties) {
        this.addNumberMapping(Types.BIT, AdapterType.Bit);
        this.addNumberMapping(Types.BOOLEAN, AdapterType.Boolean);
        this.addNumberMapping(Types.TINYINT, AdapterType.Byte);
        this.addNumberMapping(Types.SMALLINT, AdapterType.Short);
        this.addNumberMapping(Types.INTEGER, AdapterType.Int);
        this.addNumberMapping(Types.BIGINT, AdapterType.Long);
        this.addNumberMapping(Types.FLOAT, AdapterType.Float);
        this.addNumberMapping(Types.REAL, AdapterType.Float);
        this.addNumberMapping(Types.DOUBLE, AdapterType.Double);
        this.addNumberMapping(Types.NUMERIC, AdapterType.BigDecimal);
        this.addNumberMapping(Types.DECIMAL, AdapterType.BigDecimal);
        this.addNumberMapping(Types.CHAR, AdapterType.String);
        this.addNumberMapping(Types.NCHAR, AdapterType.String);
        this.addNumberMapping(Types.VARCHAR, AdapterType.String);
        this.addNumberMapping(Types.NVARCHAR, AdapterType.String);
        this.addNumberMapping(Types.LONGVARCHAR, AdapterType.String);
        this.addNumberMapping(Types.LONGNVARCHAR, AdapterType.String);
        this.addNumberMapping(Types.CLOB, AdapterType.String);
        this.addNumberMapping(Types.NCLOB, AdapterType.String);
        this.addNumberMapping(Types.SQLXML, AdapterType.String);
        this.addNumberMapping(Types.BINARY, AdapterType.Bytes);
        this.addNumberMapping(Types.VARBINARY, AdapterType.Bytes);
        this.addNumberMapping(Types.LONGVARBINARY, AdapterType.Bytes);
        this.addNumberMapping(Types.BLOB, AdapterType.Bytes);
        this.addNumberMapping(Types.DATE, AdapterType.SqlDate);
        this.addNumberMapping(Types.TIME, AdapterType.SqlTime);
        this.addNumberMapping(Types.TIMESTAMP, AdapterType.SqlTimestamp);
        this.addNumberMapping(Types.TIME_WITH_TIMEZONE, AdapterType.OffsetTime);
        this.addNumberMapping(Types.TIMESTAMP_WITH_TIMEZONE, AdapterType.OffsetDateTime);
        this.addNumberMapping(Types.ARRAY, AdapterType.Array);
        this.addNumberMapping(Types.NULL, AdapterType.Null);
        this.addNumberMapping(Types.DATALINK, AdapterType.String);
        this.addNumberMapping(Types.OTHER, AdapterType.Unknown);
        this.addNumberMapping(Types.JAVA_OBJECT, AdapterType.Unknown);
    }

    // from classType to NameType.
    protected void initClassTypeMapping(Properties properties) {
        this.addClassMapping(Boolean.class, AdapterType.Boolean);
        this.addClassMapping(boolean.class, AdapterType.Boolean);
        this.addClassMapping(Byte.class, AdapterType.Byte);
        this.addClassMapping(byte.class, AdapterType.Byte);
        this.addClassMapping(Short.class, AdapterType.Short);
        this.addClassMapping(short.class, AdapterType.Short);
        this.addClassMapping(Integer.class, AdapterType.Int);
        this.addClassMapping(int.class, AdapterType.Int);
        this.addClassMapping(Long.class, AdapterType.Long);
        this.addClassMapping(long.class, AdapterType.Long);
        this.addClassMapping(Float.class, AdapterType.Float);
        this.addClassMapping(float.class, AdapterType.Float);
        this.addClassMapping(Double.class, AdapterType.Double);
        this.addClassMapping(double.class, AdapterType.Double);
        this.addClassMapping(Character.class, AdapterType.String);
        this.addClassMapping(char.class, AdapterType.String);
        // java time
        this.addClassMapping(java.util.Date.class, AdapterType.SqlDate);
        this.addClassMapping(java.sql.Date.class, AdapterType.SqlDate);
        this.addClassMapping(java.sql.Timestamp.class, AdapterType.SqlTimestamp);
        this.addClassMapping(java.sql.Time.class, AdapterType.SqlTime);
        this.addClassMapping(Instant.class, AdapterType.SqlTimestamp);
        this.addClassMapping(LocalDateTime.class, AdapterType.SqlTimestamp);
        this.addClassMapping(LocalDate.class, AdapterType.SqlDate);
        this.addClassMapping(LocalTime.class, AdapterType.SqlTime);
        this.addClassMapping(ZonedDateTime.class, AdapterType.OffsetDateTime);
        this.addClassMapping(JapaneseDate.class, AdapterType.SqlTimestamp);
        this.addClassMapping(YearMonth.class, AdapterType.String);
        this.addClassMapping(Year.class, AdapterType.Short);
        this.addClassMapping(Month.class, AdapterType.Short);
        this.addClassMapping(OffsetTime.class, AdapterType.OffsetTime);
        this.addClassMapping(OffsetDateTime.class, AdapterType.OffsetDateTime);
        // java extensions Types
        this.addClassMapping(String.class, AdapterType.String);
        this.addClassMapping(BigInteger.class, AdapterType.BigInteger);
        this.addClassMapping(BigDecimal.class, AdapterType.BigDecimal);
        //this.addClassMapping(Reader.class, AdapterType.CLOB);
        //this.addClassMapping(InputStream.class, AdapterType.BLOB);
        this.addClassMapping(URL.class, AdapterType.String);
        this.addClassMapping(URI.class, AdapterType.String);
        this.addClassMapping(Byte[].class, AdapterType.Bytes);
        this.addClassMapping(byte[].class, AdapterType.Bytes);
        this.addClassMapping(Object[].class, AdapterType.Array);
        this.addClassMapping(List.class, AdapterType.Array);
        this.addClassMapping(Set[].class, AdapterType.Array);
        this.addClassMapping(Object.class, AdapterType.Unknown);
    }

    // from NameType to jdbcType and classType.
    protected void initTypeMappingTo(Properties properties) {
        this.addTypeMappingTo(AdapterType.Unknown, Types.OTHER, Object.class);
        this.addTypeMappingTo(AdapterType.Null, Types.NULL, Void.class);
        this.addTypeMappingTo(AdapterType.Bit, Types.BIT, String.class);
        this.addTypeMappingTo(AdapterType.Boolean, Types.BOOLEAN, Boolean.class);
        this.addTypeMappingTo(AdapterType.Byte, Types.TINYINT, Byte.class);
        this.addTypeMappingTo(AdapterType.Short, Types.SMALLINT, Short.class);
        this.addTypeMappingTo(AdapterType.Int, Types.INTEGER, Integer.class);
        this.addTypeMappingTo(AdapterType.Long, Types.BIGINT, Long.class);
        this.addTypeMappingTo(AdapterType.Float, Types.FLOAT, Float.class);
        this.addTypeMappingTo(AdapterType.Double, Types.DOUBLE, Double.class);
        this.addTypeMappingTo(AdapterType.BigDecimal, Types.DECIMAL, BigDecimal.class);
        this.addTypeMappingTo(AdapterType.BigInteger, Types.DECIMAL, BigInteger.class);
        this.addTypeMappingTo(AdapterType.String, Types.VARCHAR, String.class);
        this.addTypeMappingTo(AdapterType.Bytes, Types.VARBINARY, byte[].class);
        this.addTypeMappingTo(AdapterType.SqlDate, Types.DATE, java.sql.Date.class);
        this.addTypeMappingTo(AdapterType.SqlTime, Types.TIME, java.sql.Time.class);
        this.addTypeMappingTo(AdapterType.SqlTimestamp, Types.TIMESTAMP, java.sql.Timestamp.class);
        this.addTypeMappingTo(AdapterType.OffsetTime, Types.TIME_WITH_TIMEZONE, OffsetTime.class);
        this.addTypeMappingTo(AdapterType.OffsetDateTime, Types.TIMESTAMP_WITH_TIMEZONE, OffsetDateTime.class);
    }

    // value convert to classType.
    protected void initTypeConvert(Properties properties) {
        String timeZone = properties.getProperty(JdbcDriver.P_TIME_ZONE);
        ZoneOffset defaultZone;
        if (StringUtils.isNotBlank(timeZone)) {
            defaultZone = ZoneOffset.of(timeZone);
        } else {
            defaultZone = ZoneOffset.UTC;
        }

        // default
        this.addConvert(AdapterType.Unknown, (t, v) -> v);
        this.addConvert(AdapterType.Null, (t, v) -> null);
        this.addConvert(AdapterType.Bit, (t, v) -> ConvertUtils.toBit(v));
        this.addConvert(AdapterType.Boolean, (t, v) -> ConvertUtils.toBoolean(v, false));
        this.addConvert(AdapterType.Byte, (t, v) -> ConvertUtils.toByte(v, false));
        this.addConvert(AdapterType.Short, (t, v) -> ConvertUtils.toShort(v, false));
        this.addConvert(AdapterType.Int, (t, v) -> ConvertUtils.toInteger(v, false));
        this.addConvert(AdapterType.Long, (t, v) -> ConvertUtils.toLong(v, false));
        this.addConvert(AdapterType.Float, (t, v) -> ConvertUtils.toFloat(v, false));
        this.addConvert(AdapterType.Double, (t, v) -> ConvertUtils.toDouble(v, false));
        this.addConvert(AdapterType.BigDecimal, (t, v) -> ConvertUtils.toBigDecimal(v));
        this.addConvert(AdapterType.BigInteger, (t, v) -> ConvertUtils.toBigInteger(v));
        this.addConvert(AdapterType.String, (t, v) -> ConvertUtils.toString(v));
        this.addConvert(AdapterType.Bytes, (t, v) -> ConvertUtils.toBytes(v));
        this.addConvert(AdapterType.SqlDate, (t, v) -> ConvertUtils.toSqlDate(v));
        this.addConvert(AdapterType.SqlTime, (t, v) -> ConvertUtils.toSqlTime(v));
        this.addConvert(AdapterType.SqlTimestamp, (t, v) -> ConvertUtils.toSqlTimestamp(v));
        this.addConvert(AdapterType.OffsetTime, (t, v) -> ConvertUtils.toOffsetTime(v, defaultZone));
        this.addConvert(AdapterType.OffsetDateTime, (t, v) -> ConvertUtils.toOffsetDateTime(v, defaultZone));
        // javaType
        this.addConvert(Boolean.class.getName(), (t, v) -> ConvertUtils.toBoolean(v, false));
        this.addConvert(boolean.class.getName(), (t, v) -> ConvertUtils.toBoolean(v, true));
        this.addConvert(Byte.class.getName(), (t, v) -> ConvertUtils.toByte(v, false));
        this.addConvert(byte.class.getName(), (t, v) -> ConvertUtils.toByte(v, true));
        this.addConvert(Short.class.getName(), (t, v) -> ConvertUtils.toShort(v, false));
        this.addConvert(short.class.getName(), (t, v) -> ConvertUtils.toShort(v, true));
        this.addConvert(Integer.class.getName(), (t, v) -> ConvertUtils.toInteger(v, false));
        this.addConvert(int.class.getName(), (t, v) -> ConvertUtils.toInteger(v, true));
        this.addConvert(Long.class.getName(), (t, v) -> ConvertUtils.toLong(v, false));
        this.addConvert(long.class.getName(), (t, v) -> ConvertUtils.toLong(v, true));
        this.addConvert(Float.class.getName(), (t, v) -> ConvertUtils.toFloat(v, false));
        this.addConvert(float.class.getName(), (t, v) -> ConvertUtils.toFloat(v, true));
        this.addConvert(Double.class.getName(), (t, v) -> ConvertUtils.toDouble(v, false));
        this.addConvert(double.class.getName(), (t, v) -> ConvertUtils.toDouble(v, true));
        this.addConvert(Character.class.getName(), (t, v) -> ConvertUtils.toChar(v, false));
        this.addConvert(char.class.getName(), (t, v) -> ConvertUtils.toChar(v, true));
        // java time
        this.addConvert(java.util.Date.class.getName(), (t, v) -> ConvertUtils.toUtilDate(v));
        this.addConvert(java.util.Calendar.class.getName(), (t, v) -> ConvertUtils.toCalendar(v));
        this.addConvert(java.sql.Date.class.getName(), (t, v) -> ConvertUtils.toSqlDate(v));
        this.addConvert(java.sql.Timestamp.class.getName(), (t, v) -> ConvertUtils.toSqlTimestamp(v));
        this.addConvert(java.sql.Time.class.getName(), (t, v) -> ConvertUtils.toSqlTime(v));
        this.addConvert(Instant.class.getName(), (t, v) -> ConvertUtils.toInstant(v));
        this.addConvert(LocalDateTime.class.getName(), (t, v) -> ConvertUtils.toLocalDateTime(v));
        this.addConvert(LocalDate.class.getName(), (t, v) -> ConvertUtils.toLocalDate(v));
        this.addConvert(LocalTime.class.getName(), (t, v) -> ConvertUtils.toLocalTime(v));
        this.addConvert(ZonedDateTime.class.getName(), (t, v) -> ConvertUtils.toZonedDateTime(v, defaultZone));
        this.addConvert(JapaneseDate.class.getName(), (t, v) -> ConvertUtils.toJapaneseDate(v));
        this.addConvert(YearMonth.class.getName(), (t, v) -> ConvertUtils.toYearMonth(v));
        this.addConvert(Year.class.getName(), (t, v) -> ConvertUtils.toYear(v));
        this.addConvert(Month.class.getName(), (t, v) -> ConvertUtils.toMonth(v));
        this.addConvert(OffsetTime.class.getName(), (t, v) -> ConvertUtils.toOffsetTime(v, defaultZone));
        this.addConvert(OffsetDateTime.class.getName(), (t, v) -> ConvertUtils.toOffsetDateTime(v, defaultZone));
        // java extensions Types
        this.addConvert(String.class.getName(), (t, v) -> ConvertUtils.toString(v));
        this.addConvert(BigInteger.class.getName(), (t, v) -> ConvertUtils.toBigInteger(v));
        this.addConvert(BigDecimal.class.getName(), (t, v) -> ConvertUtils.toBigDecimal(v));
        //this.addConvert(Reader.class, AdapterType.CLOB);
        //this.addConvert(InputStream.class, AdapterType.BLOB);
        this.addConvert(URL.class.getName(), (t, v) -> ConvertUtils.toBigDecimal(v));
        this.addConvert(URI.class.getName(), (t, v) -> ConvertUtils.toBigDecimal(v));
        this.addConvert(Byte[].class.getName(), (t, v) -> ConvertUtils.toBytesWrap(v));
        this.addConvert(byte[].class.getName(), (t, v) -> ConvertUtils.toBytes(v));
        //this.addConvert(Object[].class, AdapterType.ARRAY);
        this.addConvert(Object.class.getName(), (t, v) -> v);
    }

    // value convert to classType.
    protected void initTypeAlias(Properties properties) {
        this.addAlias("Integer", AdapterType.Int);

        for (Field f : AdapterType.class.getFields()) {
            try {
                this.addAlias(f.getName().toLowerCase(), (String) f.get(null));
                this.addAlias(f.getName().toUpperCase(), (String) f.get(null));
            } catch (IllegalAccessException e) {
                //
            }
        }
    }

    public void addNumberMapping(int numberType, String typeName) {
        this.numberTypeToNameMap.put(numberType, Objects.requireNonNull(typeName));
    }

    public void addClassMapping(Class<?> classType, String typeName) {
        this.classToNameMap.put(Objects.requireNonNull(classType), Objects.requireNonNull(typeName));
    }

    public void addTypeMappingTo(String typeName, int bindJdbcType, Class<?> bindJavaType) {
        this.typeMap.put(typeName, Tuple.of(bindJdbcType, bindJavaType));
    }

    public void addAlias(String typeName, String aliasName) {
        this.aliasMap.put(Objects.requireNonNull(typeName), Objects.requireNonNull(aliasName));
    }

    public void addConvert(String dst, TypeConvert converter) {
        this.addConvert(dst, null, converter);
    }

    public void addConvert(String dst, String src, TypeConvert converter) {
        src = src == null ? "" : src;
        Map<String, TypeConvert> convertMap = this.typeConvertMap.computeIfAbsent(Objects.requireNonNull(dst), s -> new HashMap<>());
        convertMap.put(src, Objects.requireNonNull(converter));
    }

    @Override
    public String getTypeName(int typeNumber) {
        return this.numberTypeToNameMap.getOrDefault(typeNumber, AdapterType.Unknown);
    }

    @Override
    public String getTypeName(Class<?> classType) {
        String type = this.classToNameMap.get(classType);
        if (type != null) {
            return type;
        }

        if (classType.getClass().isArray()) {
            return AdapterType.Array;
        } else {
            return AdapterType.Unknown;
        }
    }

    @Override
    public String getTypeClassName(String typeName) {
        if (this.aliasMap.containsKey(typeName)) {
            typeName = this.aliasMap.get(typeName);
        }
        if (StringUtils.isBlank(typeName)) {
            return Object.class.getName();
        } else {
            return ((Class<?>) this.typeMap.getOrDefault(typeName, defaultTypeTuple).get1()).getName();
        }
    }

    @Override
    public int getTypeNumber(String typeName) {
        if (this.aliasMap.containsKey(typeName)) {
            typeName = this.aliasMap.get(typeName);
        }
        if (StringUtils.isBlank(typeName)) {
            return Types.OTHER;
        } else {
            return this.typeMap.getOrDefault(typeName, defaultTypeTuple).get0();
        }
    }

    @Override
    public TypeConvert findConvert(String typeName, Class<?> toClass) {
        TypeConvert c = this.findConvert(toClass.getName());
        if (c == null) {
            c = this.findConvert(typeName);
        }

        return c == null ? defaultConverter : c;
    }

    protected TypeConvert findConvert(String typeName) {
        if (this.typeConvertMap.containsKey(typeName)) {
            Map<String, TypeConvert> convertMap = this.typeConvertMap.get(typeName);
            if (convertMap.containsKey(typeName)) {
                return convertMap.get(typeName);
            }
            if (convertMap.containsKey("")) {
                return convertMap.get("");// empty string is default
            }
        }
        return null;
    }
}
