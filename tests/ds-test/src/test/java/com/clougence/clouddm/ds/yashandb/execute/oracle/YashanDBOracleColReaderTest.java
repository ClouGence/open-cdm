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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;

import org.junit.jupiter.api.Test;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.execute.fetcher.BooleanValueFetcher;
import com.clougence.clouddm.dsfamily.execute.fetcher.DoubleValueFetcher;
import com.clougence.clouddm.dsfamily.execute.fetcher.FloatValueFetcher;
import com.clougence.clouddm.dsfamily.execute.fetcher.IntegerValueFetcher;
import com.clougence.clouddm.dsfamily.execute.fetcher.LongValueFetcher;
import com.clougence.clouddm.dsfamily.execute.fetcher.StringAsClobFetcher;
import com.clougence.clouddm.dsfamily.execute.fetcher.StringValueFetcher;
import com.clougence.clouddm.dsfamily.execute.fetcher.TimeValueFetcher;
import com.clougence.clouddm.dsfamily.execute.fetcher.DateTimeWithZoneValueFetcher;
import com.clougence.clouddm.sdk.execute.session.ResultColMeta;
import com.clougence.clouddm.sdk.execute.session.result.ReaderOptions;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcherContext;

/**
 * Column type mapping test for the YashanDB result set reader. The type names are the lower-case values
 * measured from {@code ResultSetMetaData.getColumnTypeName()} with the 1.10.7 driver against the 23.4.7 server.
 *
 * @author open-cdm
 */
class YashanDBOracleColReaderTest {

    private final YashanDBOracleColReader reader = new YashanDBOracleColReader();

    private static ColMetaData col(String type) {
        ColMetaData cm = new ColMetaData();
        cm.setColumn("C1");
        cm.setColumnType(type);
        return cm;
    }

    private Class<?> fetcherClass(String type) {
        return reader.readColumn("C1", col(type)).getClass();
    }

    @Test
    void yashanNativeIntegerTypes() {
        assertEquals(LongValueFetcher.class, fetcherClass("bigint"));
        assertEquals(IntegerValueFetcher.class, fetcherClass("tinyint"));
        assertEquals(IntegerValueFetcher.class, fetcherClass("smallint"));
        assertEquals(IntegerValueFetcher.class, fetcherClass("integer"));
    }

    @Test
    void yashanNativeDecimalTypesUseStringForPrecision() {
        assertEquals(StringValueFetcher.class, fetcherClass("number"));
        assertEquals(StringValueFetcher.class, fetcherClass("numeric"));
        assertEquals(StringValueFetcher.class, fetcherClass("decimal"));
        assertEquals(DoubleValueFetcher.class, fetcherClass("double"));
        assertEquals(FloatValueFetcher.class, fetcherClass("real"));
    }

    @Test
    void yashanNativeBooleanAndStringTypes() {
        assertEquals(BooleanValueFetcher.class, fetcherClass("boolean"));
        assertEquals(StringValueFetcher.class, fetcherClass("varchar"));
        assertEquals(StringValueFetcher.class, fetcherClass("ds_interval"));
        assertEquals(StringValueFetcher.class, fetcherClass("ym_interval"));
    }

    /**
     * BIT and BOOLEAN are distinct JDBC types on YashanDB ({@code Types.BIT} = -7 vs {@code Types.BOOLEAN} = 16)
     * and must not share a fetcher: a {@code BIT} column holds up to 64 bits, so {@code getBoolean} would collapse
     * every non-zero value to {@code true} and lose the value. {@code getString} returns the binary digits
     * (255 -&gt; {@code "11111111"}), which is lossless.
     */
    @Test
    void bitAndBooleanMustNotShareAFetcher() {
        assertEquals(StringValueFetcher.class, fetcherClass("bit"));
        assertEquals(BooleanValueFetcher.class, fetcherClass("boolean"));
        assertNotEquals(fetcherClass("bit"), fetcherClass("boolean"), "BIT must not be read as a boolean");
    }

    @Test
    void yashanNativeTimeTypes() {
        assertEquals(TimeValueFetcher.class, fetcherClass("time"));
        assertEquals(DateTimeWithZoneValueFetcher.class, fetcherClass("timestamp_tz"));
        assertEquals(DateTimeWithZoneValueFetcher.class, fetcherClass("timestamp_ltz"));
    }

    @Test
    void varchar2ReportedAsVarcharMustResolve() {
        // the YashanDB driver reports both VARCHAR2 and VARCHAR as "varchar", while the parent only knows varchar2
        assertEquals(StringValueFetcher.class, fetcherClass("varchar"));
    }

    @Test
    void yashanJsonMustUseGetStringNotGetClob() {
        // rs.getClob on a YashanDB JSON column raises "Cannot convert an instance of JSON to type CLOB"
        assertEquals(StringValueFetcher.class, fetcherClass("json"));
    }

    @Test
    void oracleCompatibleNamesStillWork() {
        // the parent uses OracleStringValueFetcher for varchar2 (handles the Oracle charset), which is correct
        assertEquals(com.clougence.clouddm.dsfamily.oracle.execute.fetcher.OracleStringValueFetcher.class,
            fetcherClass("varchar2"));
        assertEquals(StringAsClobFetcher.class, fetcherClass("clob"));
        assertEquals(StringValueFetcher.class, fetcherClass("number"));
        assertEquals(StringValueFetcher.class, fetcherClass("rowid"));
    }

    @Test
    void trulyUnknownTypeReturnsNull() {
        assertNull(reader.readColumn("C1", col("not_a_type")));
    }

    @Test
    void everyBusinessColumnTypeResolves() {
        // Lower-case vocabulary measured from ResultSetMetaData.getColumnTypeName() with the 1.10.7 driver against
        // the 23.4.7 server; none of them may resolve to null (a null fetcher makes the grid show
        // "Unsupported xxx type."). Note that the driver reports VARCHAR2/VARCHAR both as "varchar",
        // DECIMAL/NUMERIC both as "number", BINARY_FLOAT as "float", BINARY_DOUBLE as "double", INT as "integer".
        String[] all = { "bigint", "blob", "boolean", "char", "clob", "date", "double", "float", "integer",
                         "json", "number", "nvarchar", "raw", "smallint", "time", "timestamp", "timestamp_tz",
                         "ds_interval", "ym_interval", "tinyint", "varchar", "nchar", "nclob", "bit", "real",
                         "timestamp_ltz" };
        for (String type : all) {
            ValueFetcher f = reader.readColumn("C1", col(type));
            assertNotNull(f, "type '" + type + "' must resolve to a fetcher");
        }
    }

    /**
     * End-to-end check of the lossless BIT read, using a proxied {@link ResultSet} that behaves like the YashanDB
     * driver for a {@code bit(8)} column holding 255: {@code getString} yields the binary digits and
     * {@code getBoolean} yields {@code true}. The second half of the test is the control: it shows what the old
     * mapping ({@code BIT} wired to {@code BOOLEAN_VALUE_FETCHER}) would have returned for the same row.
     */
    @Test
    void bitIsReadLosslesslyWhileBooleanKeepsBooleanSemantics() throws Exception {
        ResultSet rs = proxyResultSet("11111111", true);

        ValueFetcher bitFetcher = reader.readColumn("C1", col("bit"));
        assertEquals("11111111", bitFetcher.getString("C1", rs, newContext(col("bit"), bitFetcher)));

        ValueFetcher booleanFetcher = reader.readColumn("C1", col("boolean"));
        assertEquals("true", booleanFetcher.getString("C1", rs, newContext(col("boolean"), booleanFetcher)));
        assertEquals(Boolean.TRUE, booleanFetcher.getBoolean("C1", rs, newContext(col("boolean"), booleanFetcher)));
    }

    private static ValueFetcherContext newContext(ColMetaData cm, ValueFetcher fetcher) {
        return new ValueFetcherContext("v-" + cm.getColumnType(), new ResultColMeta(cm, fetcher), new ReaderOptions());
    }

    private static ResultSet proxyResultSet(String stringValue, boolean booleanValue) {
        InvocationHandler handler = (proxy, method, args) -> {
            String name = method.getName();
            if ("getString".equals(name)) {
                return stringValue;
            }
            if ("getBoolean".equals(name)) {
                return booleanValue;
            }
            if ("wasNull".equals(name)) {
                return false;
            }
            if ("close".equals(name)) {
                return null;
            }
            if (method.getReturnType() == boolean.class) {
                return false;
            }
            if (method.getReturnType() == int.class) {
                return 0;
            }
            if (method.getReturnType() == long.class) {
                return 0L;
            }
            return null;
        };
        return (ResultSet) Proxy.newProxyInstance(YashanDBOracleColReaderTest.class.getClassLoader(), new Class<?>[] { ResultSet.class }, handler);
    }
}
