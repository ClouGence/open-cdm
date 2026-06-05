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
package com.clougence.clouddm.dsfamily.postgres.execute;

import java.util.HashMap;
import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.ColMetaData;
import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.dsfamily.execute.fetcher.ArrayValueFetcher;
import com.clougence.clouddm.dsfamily.postgres.execute.fetcher.MoneyValueFetcher;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * https://www.postgresql.org/docs/13/datatype.html
 *
 * @author mode create time is 2021/1/12
 **/
@Slf4j
public class PgColReader extends AbstractColReader {

    public static final ValueFetcher        MONEY_VALUE_FETCHER = new MoneyValueFetcher();

    public static final Map<String, String> AliasNames          = new HashMap<>();

    static {
        AliasNames.put("serial2", "smallserial");
        AliasNames.put("serial4", "serial");
        AliasNames.put("serial8", "bigserial");
        AliasNames.put("int", "integer");
        AliasNames.put("int2", "smallint");
        AliasNames.put("int4", "integer");
        AliasNames.put("int8", "bigint");
        AliasNames.put("decimal", "numeric");
        AliasNames.put("float4", "real");
        AliasNames.put("float", "double precision");
        AliasNames.put("float8", "double precision");
        AliasNames.put("char", "character");
        AliasNames.put("varchar", "character varying");
        AliasNames.put("timestamp", "timestamp without time zone");
        AliasNames.put("timestamptz", "timestamp with time zone");
        AliasNames.put("time", "time without time zone");
        AliasNames.put("timetz", "time with time zone");
        AliasNames.put("varbit", "bit varying");
        AliasNames.put("bool", "boolean");
        // https://www.postgresql.org/docs/13/datatype-oid.html
        AliasNames.put("regclass", "oid");
        AliasNames.put("regcollation", "oid");
        AliasNames.put("regconfig", "oid");
        AliasNames.put("regdictionary", "oid");
        AliasNames.put("regnamespace", "oid");
        AliasNames.put("regoper", "oid");
        AliasNames.put("regoperator", "oid");
        AliasNames.put("regproc", "oid");
        AliasNames.put("regprocedure", "oid");
        AliasNames.put("regrole", "oid");
        AliasNames.put("regtype", "oid");

        AliasNames.put("int[]", "integer[]");
        AliasNames.put("int2[]", "smallint[]");
        AliasNames.put("int4[]", "integer[]");
        AliasNames.put("int8[]", "bigint[]");
        AliasNames.put("decimal[]", "numeric[]");
        AliasNames.put("float4[]", "real[]");
        AliasNames.put("float[]", "double precision[]");
        AliasNames.put("float8[]", "double precision[]");
        AliasNames.put("char[]", "character[]");
        AliasNames.put("varchar[]", "character varying[]");
        AliasNames.put("timestamp[]", "timestamp without time zone[]");
        AliasNames.put("timestamptz[]", "timestamp with time zone[]");
        AliasNames.put("time[]", "time without time zone[]");
        AliasNames.put("timetz[]", "time with time zone[]");
        AliasNames.put("varbit[]", "bit varying[]");
        AliasNames.put("bool[]", "boolean[]");

        AliasNames.put("_int", "integer[]");
        AliasNames.put("_int2", "smallint[]");
        AliasNames.put("_int4", "integer[]");
        AliasNames.put("_int8", "bigint[]");
        AliasNames.put("_numeric", "numeric[]");
        AliasNames.put("_float4", "real[]");
        AliasNames.put("_float8", "double precision[]");
        AliasNames.put("_money", "money[]");
        AliasNames.put("_bpchar", "character[]");
        AliasNames.put("_varchar", "character varying[]");
        AliasNames.put("_text", "text[]");
        AliasNames.put("_timestamp", "timestamp without time zone[]");
        AliasNames.put("_timestamptz", "timestamp with time zone[]");
        AliasNames.put("_date", "date[]");
        AliasNames.put("_time", "time without time zone[]");
        AliasNames.put("_timetz", "time with time zone[]");
        AliasNames.put("_interval", "interval[]");
        AliasNames.put("_bool", "boolean[]");
        AliasNames.put("_point", "point[]");
        AliasNames.put("_line", "line[]");
        AliasNames.put("_lseg", "lseg[]");
        AliasNames.put("_box", "box[]");
        AliasNames.put("_path", "path[]");
        AliasNames.put("_polygon", "polygon[]");
        AliasNames.put("_circle", "circle[]");
        AliasNames.put("_cidr", "cidr[]");
        AliasNames.put("_inet", "inet[]");
        AliasNames.put("_macaddr", "macaddr[]");
        AliasNames.put("_macaddr8", "macaddr8[]");
        AliasNames.put("_bit", "bit[]");
        AliasNames.put("_varbit", "bit varying[]");
        AliasNames.put("_bytea", "bytea[]");
        AliasNames.put("_tsvector", "tsvector[]");
        AliasNames.put("_tsquery", "tsquery[]");
        AliasNames.put("_uuid", "uuid[]");
        AliasNames.put("_xml", "xml[]");
        AliasNames.put("_json", "json[]");
        AliasNames.put("_jsonb", "jsonb[]");
        AliasNames.put("_int4range", "int4range[]");
        AliasNames.put("_int8range", "int8range[]");
        AliasNames.put("_numrange", "numrange[]");
        AliasNames.put("_tsrange", "tsrange[]");
        AliasNames.put("_tstzrange", "tstzrange[]");
        AliasNames.put("_daterange", "daterange[]");
        AliasNames.put("_pg_lsn", "pg_lsn[]");
        AliasNames.put("_txid_snapshot", "txid_snapshot[]");
        AliasNames.put("_oid", "oid[]");
        AliasNames.put("_name", "name[]");
    }

    @Override
    public ValueFetcher readColumn(String col, ColMetaData colMetaData) {
        String colType = StringUtils.defaultString(colMetaData.getColumnType(), "");
        boolean isArray = colType.charAt(0) == '_';
        if (isArray) {
            colType = colType.substring(1);
        }
        colType = colType.toLowerCase();
        colType = AliasNames.getOrDefault(colType, colType);

        ValueFetcher fetcher = _readColumn(colType);
        if (fetcher != null) {
            return isArray ? new ArrayValueFetcher(fetcher) : fetcher;
        } else {
            return null;
        }
    }

    private ValueFetcher _readColumn(String colType) {
        switch (colType) {
            case "boolean":
                return BOOLEAN_VALUE_FETCHER;
            case "smallint":
            case "smallserial":
                return SHORT_VALUE_FETCHER;
            case "integer":
            case "serial":
                return INTEGER_VALUE_FETCHER;
            case "oid":
            case "bigint":
            case "bigserial":
                return LONG_VALUE_FETCHER;
            case "numeric":
                return STRING_VALUE_FETCHER;
            case "real":
                return FLOAT_VALUE_FETCHER;
            case "double precision":
                return DOUBLE_VALUE_FETCHER;
            case "money":
                return MONEY_VALUE_FETCHER;
            case "\"char\"":
            case "name":
                return STRING_VALUE_FETCHER;
            case "character":
            case "character varying":
            case "bpchar":
            case "text":
            case "citext":
            case "xml":
            case "hstore":// key = value like map
            case "json":
            case "jsonb":
                return STRING_AS_READER_FETCHER;
            case "date":
                return DATE_VALUE_FETCHER;
            case "time without time zone":
                return TIME_VALUE_FETCHER;
            case "time with time zone":
                return TIMEZ_VALUE_FETCHER;
            case "timestamp without time zone":
                return DATETIME_VALUE_FETCHER;
            case "timestamp with time zone":
                return DATETIMEZ_VALUE_FETCHER;
            case "interval":
                return ISO8601_VALUE_FETCHER;
            case "cidr":
            case "inet":
            case "macaddr":
            case "macaddr8":
            case "uuid":
            case "int4range":
            case "int8range":
            case "numrange":
            case "tsrange":
            case "tstzrange":
            case "daterange":
            case "point":
            case "line":
            case "lseg":
            case "box":
            case "path":
            case "polygon":
            case "circle":
            case "xid":
            case "txid":
            case "xid8":
            case "txid_snapshot":
            case "pg_lsn":
            case "pg_node_tree":
            case "regtype":
            case "abstime":
            case "aclitem":
                return STRING_VALUE_FETCHER;
            case "geometry":
            case "geography":
                return GEOMETRY_VALUE_FETCHER;
            case "bit":
            case "bit varying":
            case "tsvector":
            case "tsquery":
                return STRING_AS_READER_FETCHER;
            case "bytea":
                return BYTES_AS_STREAM_FETCHER;
            default:
                return null;
        }
    }
}
