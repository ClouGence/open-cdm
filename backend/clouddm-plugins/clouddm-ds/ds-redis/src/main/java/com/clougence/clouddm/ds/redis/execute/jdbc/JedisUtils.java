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
package com.clougence.clouddm.ds.redis.execute.jdbc;

import java.sql.SQLException;
import java.util.*;

import com.clougence.clouddm.ds.redis.parser.ast.token.*;
import com.clougence.drivers.adapter.*;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.args.ExpiryOption;
import redis.clients.jedis.args.SortedSetOption;
import redis.clients.jedis.params.ZParams;
import redis.clients.jedis.resps.Tuple;
import redis.clients.jedis.util.KeyValue;

@Slf4j
class JedisUtils {

    // value (value / element / the value hash)
    protected static final JdbcColumn COL_VALUE_STRING   = new JdbcColumn("VALUE", AdapterType.String, "", "", "");
    protected static final JdbcColumn COL_VALUE_LONG     = new JdbcColumn("VALUE", AdapterType.Long, "", "", "");
    protected static final JdbcColumn COL_VALUE_BYTES    = new JdbcColumn("VALUE", AdapterType.Bytes, "", "", "");
    protected static final JdbcColumn COL_ELEMENT_STRING = new JdbcColumn("ELEMENT", AdapterType.String, "", "", "");
    protected static final JdbcColumn COL_RANK_LONG      = new JdbcColumn("RANK", AdapterType.Long, "", "", "");
    protected static final JdbcColumn COL_SCORE_DOUBLE   = new JdbcColumn("SCORE", AdapterType.Double, "", "", "");
    // result (not value)
    protected static final JdbcColumn COL_RESULT_BOOLEAN = new JdbcColumn("RESULT", AdapterType.Boolean, "", "", "");
    protected static final JdbcColumn COL_RESULT_STRING  = new JdbcColumn("RESULT", AdapterType.String, "", "", "");
    protected static final JdbcColumn COL_RESULT_LONG    = new JdbcColumn("RESULT", AdapterType.Long, "", "", "");
    protected static final JdbcColumn COL_RESULT_DOUBLE  = new JdbcColumn("RESULT", AdapterType.Double, "", "", "");

    //
    protected static final JdbcColumn COL_VALUE_BOOLEAN  = new JdbcColumn("VALUE", AdapterType.Boolean, "", "", "");
    protected static final JdbcColumn COL_VALUE_DOUBLE   = new JdbcColumn("VALUE", AdapterType.Double, "", "", "");
    protected static final JdbcColumn COL_INFO_STRING    = new JdbcColumn("INFO", AdapterType.String, "", "", "");
    protected static final JdbcColumn COL_GROUP_STRING   = new JdbcColumn("GROUP", AdapterType.String, "", "", "");
    protected static final JdbcColumn COL_NAME_STRING    = new JdbcColumn("NAME", AdapterType.String, "", "", "");
    protected static final JdbcColumn COL_STATUS_STRING  = new JdbcColumn("STATUS", AdapterType.String, "", "", "");
    protected static final JdbcColumn COL_STATUS_LONG    = new JdbcColumn("STATUS", AdapterType.Long, "", "", "");
    protected static final JdbcColumn COL_KEY_STRING     = new JdbcColumn("KEY", AdapterType.String, "", "", "");
    protected static final JdbcColumn COL_FIELD_STRING   = new JdbcColumn("FIELD", AdapterType.String, "", "", "");
    protected static final JdbcColumn COL_LENGTH_LONG    = new JdbcColumn("LENGTH", AdapterType.Long, "", "", "");
    protected static final JdbcColumn COL_COUNT_LONG     = new JdbcColumn("COUNT", AdapterType.Long, "", "", "");
    protected static final JdbcColumn COL_TYPE_STRING    = new JdbcColumn("TYPE", AdapterType.String, "", "", "");
    protected static final JdbcColumn COL_CURSOR_STRING  = new JdbcColumn("CURSOR", AdapterType.String, "", "", "");
    protected static final JdbcColumn COL_LOCAL_LONG     = new JdbcColumn("LOCAL", AdapterType.Long, "", "", "");
    protected static final JdbcColumn COL_REPLICAS_LONG  = new JdbcColumn("REPLICAS", AdapterType.Long, "", "", "");

    public static CgFuture<?> completed(CgFuture<Object> sync) {
        sync.completed(true);
        return sync;
    }

    public static CgFuture<?> failed(CgFuture<Object> sync, Exception e) {
        sync.failed(e);
        return sync;
    }

    public static <T> Map<String, T> singletonMap(String column, T keyCol) {
        Map<String, T> dataMap = new LinkedHashMap<>();
        dataMap.put(column, keyCol);
        return dataMap;
    }

    public static AdapterResultCursor singleResult(AdapterRequest request, JdbcColumn col, Object value) throws SQLException {
        AdapterResultCursor result = new AdapterResultCursor(request, Collections.singletonList(col));
        result.pushData(singletonMap(col.name, value));
        result.pushFinish();
        return result;
    }

    public static AdapterResultCursor listResult(AdapterRequest request, JdbcColumn col, Collection<?> result) throws SQLException {
        long maxRows = request.getMaxRows();
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Collections.singletonList(col));
        int affectRows = 0;
        for (Object item : result) {
            receiveCur.pushData(CollectionUtils.asMap(col.name, item));

            affectRows++;
            if (maxRows > 0 && affectRows >= maxRows) {
                break;
            }
        }
        receiveCur.pushFinish();
        return receiveCur;
    }

    public static AdapterResultCursor listResult(AdapterRequest request, JdbcColumn keyCol, JdbcColumn valueCol, Map<?, ?> result) throws SQLException {
        long maxRows = request.getMaxRows();
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(keyCol, valueCol));
        int affectRows = 0;
        for (Map.Entry<?, ?> item : result.entrySet()) {
            receiveCur.pushData(CollectionUtils.asMap(  //
                    keyCol.name, item.getKey(),         //
                    valueCol.name, item.getValue()      //
            ));

            affectRows++;
            if (maxRows > 0 && affectRows >= maxRows) {
                break;
            }
        }
        receiveCur.pushFinish();
        return receiveCur;
    }

    protected static AdapterResultCursor listResult(AdapterRequest request, List<Tuple> result) throws SQLException {
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(//
                COL_SCORE_DOUBLE,   //
                COL_ELEMENT_STRING));

        long maxRows = request.getMaxRows();
        int affectRows = 0;
        for (Tuple tuple : result) {
            receiveCur.pushData(CollectionUtils.asMap(          //
                    COL_SCORE_DOUBLE.name, tuple.getScore(),    //
                    COL_ELEMENT_STRING.name, tuple.getElement() //
            ));
            affectRows++;

            if (maxRows > 0 && affectRows >= maxRows) {
                break;
            }
        }

        receiveCur.pushFinish();
        return receiveCur;
    }

    protected static AdapterResultCursor listResult(AdapterRequest request, JdbcColumn keyCol, KeyValue<String, List<Tuple>> result) throws SQLException {
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(//
                keyCol,     //
                COL_SCORE_DOUBLE,   //
                COL_ELEMENT_STRING));

        for (Tuple tuple : result.getValue()) {
            receiveCur.pushData(CollectionUtils.asMap(          //
                    keyCol.name, result.getKey(),       //
                    COL_SCORE_DOUBLE.name, tuple.getScore(),    //
                    COL_ELEMENT_STRING.name, tuple.getElement() //
            ));
        }
        receiveCur.pushFinish();
        return receiveCur;
    }

    public static AdapterResultCursor listFixedColAndResult(AdapterRequest request, JdbcColumn fixedCol, Object fixedColValue,//
                                                            JdbcColumn col, Collection<?> result) throws SQLException {
        long maxRows = request.getMaxRows();
        AdapterResultCursor receiveCur = new AdapterResultCursor(request, Arrays.asList(fixedCol, col));
        int affectRows = 0;
        for (Object item : result) {
            receiveCur.pushData(CollectionUtils.asMap(fixedCol.name, fixedColValue, col.name, item));

            affectRows++;
            if (maxRows > 0 && affectRows >= maxRows) {
                break;
            }
        }
        receiveCur.pushFinish();
        return receiveCur;
    }

    public static String argAsString(AdapterRequest request, ArgToken srcKey) throws SQLException {
        Object objValue;
        if (srcKey.isArg()) {
            String argName = "arg" + (srcKey.getArgIndex() + 1);
            JdbcArg jdbcArg = request.getArgMap().get(argName);
            if (jdbcArg == null) {
                throw new SQLException(argName + " not found in request.");
            } else {
                objValue = jdbcArg.getValue();
            }
        } else {
            objValue = srcKey.getValue();
        }

        return objValue == null ? null : objValue.toString();
    }

    public static byte[] argAsBytes(AdapterRequest request, ArgToken srcKey) throws SQLException {
        Object objValue;
        if (srcKey.isArg()) {
            String argName = "arg" + (srcKey.getArgIndex() + 1);
            JdbcArg jdbcArg = request.getArgMap().get(argName);
            if (jdbcArg == null) {
                throw new SQLException(argName + " not found in request.");
            } else {
                objValue = jdbcArg.getValue();
            }
        } else {
            objValue = srcKey.getValue();
        }

        if (objValue == null) {
            return null;
        } else if (objValue instanceof byte[] || objValue instanceof Byte[]) {
            return ConvertUtils.toBytes(objValue);
        } else if (objValue instanceof String) {
            String str = objValue.toString();
            str = (str.startsWith("0x") || str.startsWith("0X")) ? str.substring(2) : str;
            return HexadecimalUtils.hex2bytes(str);
        } else {
            throw new SQLException("Cannot convert to byte[] from " + objValue.getClass().getName());
        }
    }

    public static int argAsInt(AdapterRequest request, ArgToken srcKey) throws SQLException {
        Object objValue;
        if (srcKey.isArg()) {
            String argName = "arg" + (srcKey.getArgIndex() + 1);
            JdbcArg jdbcArg = request.getArgMap().get(argName);
            if (jdbcArg == null) {
                throw new SQLException(argName + " not found in request.");
            } else {
                objValue = jdbcArg.getValue();
            }
        } else {
            objValue = srcKey.getValue();
        }

        return ConvertUtils.toInteger(objValue, true);
    }

    public static boolean argAsBool(AdapterRequest request, BoolToken boolValue) throws SQLException {
        Object objValue;
        if (boolValue.isArg()) {
            String argName = "arg" + (boolValue.getArgIndex() + 1);
            JdbcArg jdbcArg = request.getArgMap().get(argName);
            if (jdbcArg == null) {
                throw new SQLException(argName + " not found in request.");
            } else {
                objValue = jdbcArg.getValue();
            }
        } else {
            objValue = boolValue.getValue();
        }

        return ConvertUtils.toBoolean(objValue, true);
    }

    public static Integer argAsInteger(AdapterRequest request, ArgToken srcKey) throws SQLException {
        if (srcKey == null) {
            return null;
        }
        Object objValue;
        if (srcKey.isArg()) {
            String argName = "arg" + (srcKey.getArgIndex() + 1);
            JdbcArg jdbcArg = request.getArgMap().get(argName);
            if (jdbcArg == null) {
                throw new SQLException(argName + " not found in request.");
            } else {
                objValue = jdbcArg.getValue();
            }
        } else {
            objValue = srcKey.getValue();
        }

        return ConvertUtils.toInteger(objValue, false);
    }

    public static long argAsLong(AdapterRequest request, ArgToken srcKey) throws SQLException {
        Object objValue;
        if (srcKey.isArg()) {
            String argName = "arg" + (srcKey.getArgIndex() + 1);
            JdbcArg jdbcArg = request.getArgMap().get(argName);
            if (jdbcArg == null) {
                throw new SQLException(argName + " not found in request.");
            } else {
                objValue = jdbcArg.getValue();
            }
        } else {
            objValue = srcKey.getValue();
        }

        return ConvertUtils.toLong(objValue, true);
    }

    public static double argAsDouble(AdapterRequest request, ArgToken srcKey) throws SQLException {
        Object objValue;
        if (srcKey.isArg()) {
            String argName = "arg" + (srcKey.getArgIndex() + 1);
            JdbcArg jdbcArg = request.getArgMap().get(argName);
            if (jdbcArg == null) {
                throw new SQLException(argName + " not found in request.");
            } else {
                objValue = jdbcArg.getValue();
            }
        } else {
            objValue = srcKey.getValue();
        }

        return ConvertUtils.toDouble(objValue, true);
    }

    protected static void numKeysCheck(String command, long numKeys, long expect) throws SQLException {
        if (numKeys != expect) {
            throw new SQLException(command + " numKeys " + numKeys + " not match actual keys " + expect + ".", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
    }

    protected static ExpiryOption convertToExpiryOption(KeyOptToken.KeyOptionType opt) throws SQLException {
        switch (opt) {
            case NX:
                return ExpiryOption.NX;
            case XX:
                return ExpiryOption.XX;
            case GT:
                return ExpiryOption.GT;
            case LT:
                return ExpiryOption.LT;
            default:
                throw new SQLException("expire option(" + opt.name() + ") not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
    }

    protected static SortedSetOption convertToSortedSetOption(MinMax opt) throws SQLException {
        switch (opt) {
            case MAX:
                return SortedSetOption.MAX;
            case MIN:
                return SortedSetOption.MIN;
            default:
                throw new SQLException("minmax option(" + opt.name() + ") not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
    }

    protected static ZParams.Aggregate convertToAggregate(AggregateType aggregate) throws SQLException {
        switch (aggregate) {
            case MIN:
                return ZParams.Aggregate.MIN;
            case MAX:
                return ZParams.Aggregate.MAX;
            case SUM:
                return ZParams.Aggregate.SUM;
            default:
                throw new SQLException("AggregateType " + aggregate.name() + " not support.", JdbcErrorCode.SQL_STATE_ILLEGAL_ARGUMENT);
        }
    }
}
