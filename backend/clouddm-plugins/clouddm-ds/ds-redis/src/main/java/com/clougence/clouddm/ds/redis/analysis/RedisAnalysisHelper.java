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
package com.clougence.clouddm.ds.redis.analysis;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.redis.parser.antlr.RedisParserBaseVisitor;
import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;

public class RedisAnalysisHelper extends RedisParserBaseVisitor<SecQueryType> {

    public static SecQueryType cmdTypeToSecQueryType(RedisCmdType cmdType) {
        if (cmdType == RedisCmdType.SELECT) {
            return SecQueryType.SWITCH_SCHEMA;
        }

        switch (cmdType.getKindType()) {
            case Read:
                return SecQueryType.READ;
            case Write:
                return SecQueryType.WRITE;
            case Script:
                return SecQueryType.REDIS_SCRIPT;
            case Maintenance:
                return SecQueryType.ADMIN;
            case Monitor:
                return SecQueryType.REDIS_MONITOR;
            case Connection:
                return SecQueryType.REDIS_CONNECTION;
            case PubSub:
                return SecQueryType.REDIS_PUBSUB;
            case Transaction:
                return SecQueryType.REDIS_TRANSACTION;
            case Other:
            default:
                return SecQueryType.UNKNOWN;
        }
    }
}
