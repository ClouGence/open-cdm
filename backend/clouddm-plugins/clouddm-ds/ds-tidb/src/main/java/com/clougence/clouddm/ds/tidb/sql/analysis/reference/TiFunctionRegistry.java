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
package com.clougence.clouddm.ds.tidb.sql.analysis.reference;

import java.util.Locale;
import java.util.Set;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;

/** TiDB function names from PingCAP's parser AST and expression registry, across supported versions. */
public final class TiFunctionRegistry {
    private static final Set<String> EXTENSIONS = Set
        .of("PASSWORD", "CURRENT_RESOURCE_GROUP", "FORMAT_NANO_TIME", "JSON_SUM_CRC32", "APPROX_COUNT_DISTINCT", "APPROX_PERCENTILE", "EMBED_TEXT", "LASTVAL", "NEXTVAL", "SETVAL", "SM3", "TIDB_BOUNDED_STALENESS", "TIDB_CURRENT_TSO", "TIDB_DECODE_BASE64_KEY", "TIDB_DECODE_BINARY_PLAN", "TIDB_DECODE_KEY", "TIDB_DECODE_PLAN", "TIDB_DECODE_SQL_DIGESTS", "TIDB_ENCODE_INDEX_KEY", "TIDB_ENCODE_RECORD_KEY", "TIDB_ENCODE_SQL_DIGEST", "TIDB_IS_DDL_OWNER", "TIDB_MVCC_INFO", "TIDB_PARSE_TSO", "TIDB_PARSE_TSO_LOGICAL", "TIDB_ROW_CHECKSUM", "TIDB_SHARD", "TIDB_VERSION", "TRANSLATE", "VEC_AS_TEXT", "VEC_COSINE_DISTANCE", "VEC_DIMS", "VEC_EMBED_COSINE_DISTANCE", "VEC_EMBED_L2_DISTANCE", "VEC_FROM_TEXT", "VEC_L1_DISTANCE", "VEC_L2_DISTANCE", "VEC_L2_NORM", "VEC_NEGATIVE_INNER_PRODUCT", "VITESS_HASH");

    private TiFunctionRegistry(){
    }

    public static boolean isUserDefined(String name, boolean qualified) {
        if (qualified || name.startsWith("`")) {
            return true;
        }
        return !EXTENSIONS.contains(name.toUpperCase(Locale.ROOT)) && MySqlResourceRegistry.instance().isUserDefinedFunction(name, false);
    }

    public static BehaviorAction behavior(String name, boolean qualified) {
        if (qualified) {
            return BehaviorAction.CALL;
        }
        return switch (name.toUpperCase(Locale.ROOT)) {
            case "GET_LOCK" -> BehaviorAction.LOCK;
            case "RELEASE_LOCK", "RELEASE_ALL_LOCKS" -> BehaviorAction.UNLOCK;
            default -> BehaviorAction.CALL;
        };
    }

    public static SplitQueryType statementType(String name, boolean qualified) {
        if (isUserDefined(name, qualified)) {
            return SplitQueryType.CALL_PROG_OBJ;
        }
        return nativeStatementType(name);
    }

    public static SplitQueryType nativeStatementType(String name) {
        return switch (name.toUpperCase(Locale.ROOT)) {
            case "GET_LOCK", "RELEASE_LOCK", "RELEASE_ALL_LOCKS" -> SplitQueryType.SESSION_LOCK;
            case "BENCHMARK" -> SplitQueryType.PERFORMANCE;
            case "MASTER_POS_WAIT", "SOURCE_POS_WAIT", "WAIT_FOR_EXECUTED_GTID_SET", "WAIT_UNTIL_SQL_THREAD_AFTER_GTIDS" -> SplitQueryType.ADMIN_REPLICATION;
            default -> null;
        };
    }
}
