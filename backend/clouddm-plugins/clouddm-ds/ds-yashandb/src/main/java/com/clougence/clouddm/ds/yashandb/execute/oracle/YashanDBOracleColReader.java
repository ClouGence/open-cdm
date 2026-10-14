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
package com.clougence.clouddm.ds.yashandb.execute.oracle;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.oracle.execute.OraColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.utils.StringUtils;

/**
 * Result set column reader for YashanDB.
 * <p>
 * {@code ResultSetMetaData.getColumnTypeName()} of the YashanDB JDBC driver returns YashanDB native type names
 * (such as {@code bigint}, {@code integer}, {@code varchar}, {@code timestamp_tz}, {@code ds_interval},
 * {@code ym_interval}, {@code json}) instead of Oracle type names ({@code NUMBER}, {@code VARCHAR2},
 * {@code TIMESTAMP WITH TIME ZONE}, ...). The parent class {@link OraColReader} would therefore fail the lookup
 * and get {@code null}, and the result grid would show {@code Unsupported xxx type.}. This class first tries
 * the Oracle name mapping of the parent class and adds the YashanDB native name mapping when it does not match.
 * </p>
 * <p>
 * The mapping is based on measurements of the 1.10.7 driver against the 23.4.7 server:
 * </p>
 * <ul>
 * <li>{@code bigint} -> {@code getObject} yields {@code Long}; {@code getInt} overflows on large values, so read it as Long;</li>
 * <li>{@code tinyint}/{@code smallint}/{@code integer} -> {@code getObject} always yields {@code Integer};</li>
 * <li>{@code number}/{@code numeric}/{@code decimal} -> {@code getObject} yields {@code BigDecimal} (NUMBER(38)
 * may exceed the Long/Integer range); consistent with the Oracle family, read it as a string to keep precision;</li>
 * <li>{@code double} -> {@code Double}, {@code real}/{@code float} -> {@code Float};</li>
 * <li>{@code boolean} -> {@code Boolean} (JDBC type {@code Types.BOOLEAN} = 16);</li>
 * <li>{@code bit} -> read as a string. A YashanDB {@code BIT} column holds up to 64 bits and is a distinct
 * JDBC type ({@code Types.BIT} = -7) from {@code BOOLEAN}. {@code getBoolean} collapses any non-zero
 * {@code BIT(n>1)} to {@code true} (255 -&gt; {@code true}), which loses the value; {@code getString} returns
 * the binary digits ({@code 255} -&gt; {@code "11111111"}), which is lossless. Note that the MySQL family
 * {@code BitValueFetcher} cannot be reused here: it assumes {@code getString} returns a <i>decimal</i>
 * string and applies {@code BigInteger(str, 10).toString(2)}, which would double-convert the already-binary
 * string that YashanDB returns;</li>
 * <li>{@code time} -> {@code rs.getTime} works;</li>
 * <li>{@code timestamp_tz} (including {@code TIMESTAMP WITH TIME ZONE} columns) and {@code timestamp_ltz}
 * (including {@code TIMESTAMP WITH LOCAL TIME ZONE} columns) -> {@code rs.getObject(OffsetDateTime/OffsetTime.class)} works;</li>
 * <li>{@code ds_interval}/{@code ym_interval} -> {@code getObject} yields a string;</li>
 * <li>{@code json} -> {@code rs.getClob} raises {@code Cannot convert an instance of JSON to type CLOB}, so
 * {@code rs.getString} must be used. Note that the parent class maps {@code json} to a CLOB fetcher that uses
 * {@code getClob}, so this mapping must be checked before the parent class;</li>
 * <li>{@code varchar} -> the YashanDB driver reports both {@code VARCHAR2} and {@code VARCHAR} as {@code varchar},
 * while the parent class only recognizes {@code varchar2}.</li>
 * </ul>
 * The remaining Oracle-compatible type names ({@code varchar2}/{@code char}/{@code clob}/{@code raw}/{@code date}/
 * {@code timestamp}, etc.) are handled by the parent class.
 *
 * @author open-cdm
 */
public class YashanDBOracleColReader extends OraColReader {

    public YashanDBOracleColReader(){
        this(null);
    }

    public YashanDBOracleColReader(String clientCharset){
        super(clientCharset);
    }

    @Override
    public ValueFetcher readColumn(String column, ColMetaData colMetaData) {
        String columnType = StringUtils.defaultString(colMetaData.getColumnType(), "").trim().toLowerCase();
        // Must be checked before the parent class: the parent maps "json" to STRING_AS_CLOB_FETCHER (which uses
        // rs.getClob), but getClob on a YashanDB JSON column raises YasException, so rs.getString must be used.
        if ("json".equals(columnType)) {
            return STRING_VALUE_FETCHER;
        }

        ValueFetcher fetcher = super.readColumn(column, colMetaData);
        if (fetcher != null) {
            return fetcher;
        }

        return switch (columnType) {
            case "bigint" -> LONG_VALUE_FETCHER;
            case "tinyint", "smallint", "integer" -> INTEGER_VALUE_FETCHER;
            case "numeric", "decimal" -> STRING_VALUE_FETCHER;
            case "double" -> DOUBLE_VALUE_FETCHER;
            case "real" -> FLOAT_VALUE_FETCHER;
            // BIT and BOOLEAN are different JDBC types on YashanDB (Types.BIT = -7 vs Types.BOOLEAN = 16).
            // BIT can hold up to 64 bits, and getBoolean() would collapse every non-zero value to true;
            // getString() returns the lossless binary digits instead.
            case "bit" -> STRING_VALUE_FETCHER;
            case "boolean" -> BOOLEAN_VALUE_FETCHER;
            case "varchar" -> STRING_VALUE_FETCHER;
            case "time" -> TIME_VALUE_FETCHER;
            case "timestamp_tz", "timestamp_ltz" -> DATETIMEZ_VALUE_FETCHER;
            case "ds_interval", "ym_interval" -> STRING_VALUE_FETCHER;
            default -> null;
        };
    }
}
