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

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.oracle.execute.OraColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.utils.StringUtils;

/**
 * 崖山数据库结果集列读取器。
 * <p>
 * 崖山 JDBC 的 {@code ResultSetMetaData.getColumnTypeName()} 返回的是崖山原生类型名
 * （如 {@code bigint}、{@code integer}、{@code varchar}、{@code timestamp_tz}、
 * {@code ds_interval}、{@code ym_interval}、{@code json}），与 Oracle 的类型名
 * （{@code NUMBER}、{@code VARCHAR2}、{@code TIMESTAMP WITH TIME ZONE}...）不同，
 * 父类 {@link OraColReader} 查表会落到 {@code null}，结果网格将显示
 * {@code Unsupported xxx type.}。本类先走父类的 Oracle 名映射，未命中时补充崖山原生名映射。
 * </p>
 * <p>
 * 映射依据为 1.10.7 驱动在 23.4.7 服务端上的实测结果：
 * </p>
 * <ul>
 * <li>{@code bigint} -> {@code getObject} 为 {@code Long}，大值 {@code getInt} 会溢出，用 Long 读取；</li>
 * <li>{@code tinyint}/{@code smallint}/{@code integer} -> {@code getObject} 均为 {@code Integer}；</li>
 * <li>{@code number}/{@code numeric}/{@code decimal} -> {@code getObject} 为 {@code BigDecimal}
 * （NUMBER(38) 可能超出 Long/Integer 范围），与 Oracle 家族一致用字符串保精度；</li>
 * <li>{@code double} -> {@code Double}，{@code real}/{@code float} -> {@code Float}；</li>
 * <li>{@code boolean}/{@code bit} -> {@code Boolean}；</li>
 * <li>{@code time} -> {@code rs.getTime} 可用；</li>
 * <li>{@code timestamp_tz}（含 {@code TIMESTAMP WITH TIME ZONE} 列）、{@code timestamp_ltz}
 * （含 {@code TIMESTAMP WITH LOCAL TIME ZONE} 列）-> {@code rs.getObject(OffsetDateTime/OffsetTime.class)} 可用；</li>
 * <li>{@code ds_interval}/{@code ym_interval} -> {@code getObject} 为字符串；</li>
 * <li>{@code json} -> {@code rs.getClob} 会抛 {@code Cannot convert an instance of JSON to type CLOB}，
 * 必须 {@code rs.getString}。注意父类会把 {@code json} 映射为走 {@code getClob} 的
 * CLOB fetcher，因此本映射必须先于父类判断；</li>
 * <li>{@code varchar} -> 崖山驱动把 {@code VARCHAR2} 与 {@code VARCHAR} 都报告为 {@code varchar}，
 * 父类只认识 {@code varchar2}。</li>
 * </ul>
 * 其余 Oracle 兼容类型名（{@code varchar2}/{@code char}/{@code clob}/{@code raw}/{@code date}/
 * {@code timestamp} 等）由父类处理。
 *
 * @author open-cdm
 */
public class YashanDBColReader extends OraColReader {

    public YashanDBColReader(){
        this(null);
    }

    public YashanDBColReader(String clientCharset){
        super(clientCharset);
    }

    @Override
    public ValueFetcher readColumn(String column, ColMetaData colMetaData) {
        String columnType = StringUtils.defaultString(colMetaData.getColumnType(), "").trim().toLowerCase();
        // 必须先于父类判断：父类把 "json" 映射为 STRING_AS_CLOB_FETCHER（走 rs.getClob），
        // 崖山的 JSON 列 getClob 会抛 YasException，必须用 rs.getString。
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
            case "bit", "boolean" -> BOOLEAN_VALUE_FETCHER;
            case "varchar" -> STRING_VALUE_FETCHER;
            case "time" -> TIME_VALUE_FETCHER;
            case "timestamp_tz", "timestamp_ltz" -> DATETIMEZ_VALUE_FETCHER;
            case "ds_interval", "ym_interval" -> STRING_VALUE_FETCHER;
            default -> null;
        };
    }
}
