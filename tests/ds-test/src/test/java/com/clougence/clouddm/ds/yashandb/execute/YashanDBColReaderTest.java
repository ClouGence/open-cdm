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
package com.clougence.clouddm.ds.yashandb.execute;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;

/**
 * 崖山结果集列类型映射测试。类型名为 1.10.7 驱动在 23.4.7 服务端上
 * {@code ResultSetMetaData.getColumnTypeName()} 的小写实测值。
 *
 * @author open-cdm
 */
class YashanDBColReaderTest {

    private final YashanDBColReader reader = new YashanDBColReader();

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
        assertEquals(BooleanValueFetcher.class, fetcherClass("bit"));
        assertEquals(StringValueFetcher.class, fetcherClass("varchar"));
        assertEquals(StringValueFetcher.class, fetcherClass("ds_interval"));
        assertEquals(StringValueFetcher.class, fetcherClass("ym_interval"));
    }

    @Test
    void yashanNativeTimeTypes() {
        assertEquals(TimeValueFetcher.class, fetcherClass("time"));
        assertEquals(DateTimeWithZoneValueFetcher.class, fetcherClass("timestamp_tz"));
        assertEquals(DateTimeWithZoneValueFetcher.class, fetcherClass("timestamp_ltz"));
    }

    @Test
    void varchar2ReportedAsVarcharMustResolve() {
        // 崖山驱动把 VARCHAR2 与 VARCHAR 都报告为 "varchar"，父类只认识 varchar2
        assertEquals(StringValueFetcher.class, fetcherClass("varchar"));
    }

    @Test
    void yashanJsonMustUseGetStringNotGetClob() {
        // 崖山 JSON 列 rs.getClob 会抛 "Cannot convert an instance of JSON to type CLOB"
        assertEquals(StringValueFetcher.class, fetcherClass("json"));
    }

    @Test
    void oracleCompatibleNamesStillWork() {
        // 父类对 varchar2 用 OracleStringValueFetcher（处理 Oracle 字符集），行为正确
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
        // 驱动 1.10.7 在 23.4.7 上 ResultSetMetaData.getColumnTypeName() 的小写实测词表，
        // 不允许出现 null（出现即网格显示 "Unsupported xxx type."）。
        // 注意：驱动把 VARCHAR2/VARCHAR 都报为 "varchar"，DECIMAL/NUMERIC 都报为 "number"，
        // BINARY_FLOAT 报为 "float"，BINARY_DOUBLE 报为 "double"，INT 报为 "integer"。
        String[] all = { "bigint", "blob", "boolean", "char", "clob", "date", "double", "float", "integer",
                         "json", "number", "nvarchar", "raw", "smallint", "time", "timestamp", "timestamp_tz",
                         "ds_interval", "ym_interval", "tinyint", "varchar", "nchar", "nclob", "bit", "real",
                         "timestamp_ltz" };
        for (String type : all) {
            ValueFetcher f = reader.readColumn("C1", col(type));
            assertNotNull(f, "type '" + type + "' must resolve to a fetcher");
        }
    }
}
