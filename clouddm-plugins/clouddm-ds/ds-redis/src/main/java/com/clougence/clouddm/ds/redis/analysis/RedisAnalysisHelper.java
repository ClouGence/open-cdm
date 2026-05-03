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
