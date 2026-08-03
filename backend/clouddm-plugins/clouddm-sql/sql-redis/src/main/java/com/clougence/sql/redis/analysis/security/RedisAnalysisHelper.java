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
package com.clougence.sql.redis.analysis.security;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.redis.parser.antlr.RedisParserBaseVisitor;
import com.clougence.sql.redis.parser.ast.RedisCmdType;

public class RedisAnalysisHelper extends RedisParserBaseVisitor<SplitQueryType> {

    public static SplitQueryType cmdTypeToSecQueryType(RedisCmdType cmdType) {
        if (cmdType == RedisCmdType.SELECT) {
            return SplitQueryType.SWITCH_SCHEMA;
        }

        switch (cmdType) {
            // Dynamic script execution: the actual data operation cannot be determined statically.
            case EVAL:
            case EVAL_RO:
            case EVALSHA:
            case EVALSHA_RO:
                return SplitQueryType.UNSAFE;
            case FCALL:
            case FCALL_RO:
                return SplitQueryType.CALL_PROG_OBJ;
            case FUNCTION_LOAD:
            case SCRIPT_LOAD:
                return SplitQueryType.CREATE_PROG_OBJ;
            case FUNCTION_RESTORE:
                return SplitQueryType.UNSAFE;
            case FUNCTION_DEL:
            case FUNCTION_FLUSH:
            case SCRIPT_FLUSH:
                return SplitQueryType.DROP_PROG_OBJ;
            case FUNCTION_KILL:
            case SCRIPT_KILL:
                return SplitQueryType.ADMIN_PROG_OBJ;
            case FUNCTION_STATS:
                return SplitQueryType.PERFORMANCE;
            case SCRIPT_DEBUG:
                return SplitQueryType.SESSION_SETTING_WRITE;
            case SCRIPT_EXISTS:
            case FUNCTION_DUMP:
            case FUNCTION_LIST:
                return SplitQueryType.METADATA;

            // Pub/sub contains message writes, runtime subscription control and diagnostics.
            case PUBLISH:
            case SPUBLISH:
                return SplitQueryType.ADMIN_PUB_SUB;
            case PSUBSCRIBE:
            case PUNSUBSCRIBE:
            case SSUBSCRIBE:
            case SUBSCRIBE:
            case SUNSUBSCRIBE:
            case UNSUBSCRIBE:
                return SplitQueryType.ADMIN_PUB_SUB;
            case PUBSUB_CHANNELS:
            case PUBSUB_NUMPAT:
            case PUBSUB_NUMSUB:
            case PUBSUB_SHARDCHANNELS:
            case PUBSUB_SHARDNUMSUB:
                return SplitQueryType.PERFORMANCE;

            // Connection-kind commands have different real semantics.
            case ASKING:
            case READONLY:
            case READWRITE:
            case CLIENT_CACHING:
                return SplitQueryType.SESSION_SETTING_WRITE;
            case CLUSTER_INFO:
            case CLUSTER_MYID:
            case CLUSTER_MYSHARDID:
            case CLUSTER_NODES:
            case CLUSTER_SHARDS:
            case CLUSTER_SLOTS:
            case ROLE:
                return SplitQueryType.METADATA;
            case CLUSTER_SLOT_STATS:
            case CLIENT_GETNAME:
            case CLIENT_GETREDIR:
            case CLIENT_ID:
            case CLIENT_INFO:
            case PING:
                return SplitQueryType.PERFORMANCE;
            case WAIT:
            case WAITAOF:
                return SplitQueryType.ADMIN_REPLICATION;
            case DBSIZE:
                return SplitQueryType.SELECT;
            case INFO:
                return SplitQueryType.PERFORMANCE;
            case CLUSTER_KEYSLOT:
                return SplitQueryType.SELECT;
            case ACL_WHOAMI:
            case COMMAND:
            case COMMAND_COUNT:
            case COMMAND_DOCS:
            case COMMAND_GETKEYS:
            case COMMAND_GETKEYSANDFLAGS:
            case COMMAND_INFO:
            case COMMAND_LIST:
            case MODULE_LIST:
                return SplitQueryType.METADATA;
            case LOLWUT:
            case ECHO:
            case TIME:
                return SplitQueryType.SELECT;
            default:
                break;
        }

        switch (cmdType) {
            // Metadata, configuration, diagnostics and administrative domains.
            case ACL_CAT:
            case ACL_DRYRUN:
            case ACL_GETUSER:
            case ACL_LIST:
            case ACL_USERS:
            case CONFIG_GET:
            case CLUSTER_LINKS:
            case CLUSTER_REPLICAS:
            case CLUSTER_SLAVES:
            case OBJECT:
            case TYPE:
                return SplitQueryType.METADATA;
            case ACL_DELUSER:
                return SplitQueryType.DROP_USER;
            case ACL_GENPASS:
                return SplitQueryType.ADMIN;
            case ACL_LOAD:
            case ACL_SAVE:
            case CONFIG_SET:
            case CONFIG_REWRITE:
            case CLUSTER_ADDSLOTS:
            case CLUSTER_ADDSLOTSRANGE:
            case CLUSTER_BUMPEPOCH:
            case CLUSTER_DELSLOTS:
            case CLUSTER_DELSLOTSRANGE:
            case CLUSTER_FLUSHSLOTS:
            case CLUSTER_FORGET:
            case CLUSTER_MEET:
            case CLUSTER_RESET:
            case CLUSTER_SAVECONFIG:
            case CLUSTER_SET_CONFIG_EPOCH:
            case CLUSTER_SETSLOT:
            case MODULE_LOAD:
            case MODULE_LOADEX:
            case MODULE_UNLOAD:
                return SplitQueryType.SYSTEM_SETTING_WRITE;
            case ACL_LOG:
                return SplitQueryType.LOG_READ;
            case ACL_SETUSER:
                // SETUSER creates or alters depending on runtime state.
                return SplitQueryType.UNKNOWN;
            case CONFIG_RESETSTAT:
            case LATENCY_RESET:
            case MEMORY_PURGE:
                return SplitQueryType.ADMIN_PERFORMANCE;
            case LATENCY_DOCTOR:
            case LATENCY_GRAPH:
            case LATENCY_HISTOGRAM:
            case LATENCY_HISTORY:
            case LATENCY_LATEST:
                return SplitQueryType.PERFORMANCE;
            case BGREWRITEAOF:
                return SplitQueryType.MAINTAIN_LOG;
            case BGSAVE:
            case SAVE:
                return SplitQueryType.ADMIN;
            case FAILOVER:
            case REPLICAOF:
            case SLAVEOF:
            case CLUSTER_FAILOVER:
            case CLUSTER_REPLICATE:
                return SplitQueryType.ALTER_REPLICATION;
            case PSYNC:
            case SYNC:
                return SplitQueryType.ADMIN_REPLICATION;
            case FLUSHALL:
            case FLUSHDB:
                return SplitQueryType.DELETE;
            case LASTSAVE:
                return SplitQueryType.PERFORMANCE;
            case SHUTDOWN:
                return SplitQueryType.UNSAFE;
            case SLOWLOG_GET:
            case SLOWLOG_LEN:
                return SplitQueryType.LOG_READ;
            case SLOWLOG_RESET:
                return SplitQueryType.MAINTAIN_LOG;
            case SWAPDB:
                return SplitQueryType.UNSAFE;
            case TOUCH:
                return SplitQueryType.UPDATE;
            case CLIENT_LIST:
            case CLIENT_TRACKINGINFO:
                return SplitQueryType.PERFORMANCE;
            case CLIENT_NO_EVICT:
            case CLIENT_NO_TOUCH:
            case CLIENT_REPLY:
            case CLIENT_SETINFO:
            case CLIENT_SETNAME:
            case CLIENT_TRACKING:
            case AUTH:
            case HELLO:
            case RESET:
                return SplitQueryType.SESSION_SETTING_WRITE;
            case CLIENT_KILL:
            case CLIENT_PAUSE:
            case CLIENT_UNBLOCK:
            case CLIENT_UNPAUSE:
            case QUIT:
                return SplitQueryType.ADMIN;
            default:
                break;
        }

        switch (cmdType) {
            // Mutations whose result can be either a newly inserted value or an update.
            case RESTORE:
            case APPEND:
            case DECR:
            case DECRBY:
            case GETSET:
            case INCR:
            case INCRBY:
            case INCRBYFLOAT:
            case MSET:
            case PSETEX:
            case SET:
            case SETEX:
            case SETRANGE:
            case BITOP:
            case SETBIT:
            case HINCRBY:
            case HINCRBYFLOAT:
            case HMSET:
            case HSET:
            case HSETEX:
            case SDIFFSTORE:
            case SINTERSTORE:
            case SUNIONSTORE:
            case ZADD:
            case ZDIFFSTORE:
            case ZINCRBY:
            case ZINTERSTORE:
            case ZRANGESTORE:
            case ZUNIONSTORE:
            case PFADD:
            case PFMERGE:
                return SplitQueryType.MERGE;

            case COPY:
            case MSETNX:
            case SETNX:
            case HSETNX:
            case LINSERT:
            case LPUSH:
            case LPUSHX:
            case RPUSH:
            case RPUSHX:
            case SADD:
                return SplitQueryType.INSERT;

            case DEL:
            case UNLINK:
            case GETDEL:
            case HDEL:
            case HGETDEL:
            case MOVE:
            case BLMOVE:
            case BLMPOP:
            case BLPOP:
            case BRPOP:
            case BRPOPLPUSH:
            case LMOVE:
            case LMPOP:
            case LPOP:
            case LREM:
            case LTRIM:
            case RPOP:
            case RPOPLPUSH:
            case SMOVE:
            case SPOP:
            case SREM:
            case BZMPOP:
            case BZPOPMAX:
            case BZPOPMIN:
            case ZMPOP:
            case ZPOPMAX:
            case ZPOPMIN:
            case ZREM:
            case ZREMRANGEBYLEX:
            case ZREMRANGEBYRANK:
            case ZREMRANGEBYSCORE:
                return SplitQueryType.DELETE;

            case EXPIRE:
            case EXPIREAT:
            case PERSIST:
            case PEXPIRE:
            case PEXPIREAT:
            case RENAME:
            case RENAMENX:
            case GETEX:
            case HEXPIRE:
            case HEXPIREAT:
            case HGETEX:
            case HPERSIST:
            case HPEXPIRE:
            case HPEXPIREAT:
            case LSET:
                return SplitQueryType.UPDATE;

            // These commands contain multiple data actions and are expanded by the split visitor.
            case SORT:
                return SplitQueryType.UNKNOWN;
            case BITFIELD:
                return SplitQueryType.SELECT;
            default:
                break;
        }

        switch (cmdType.getKindType()) {
            case Read:
                return SplitQueryType.SELECT;
            case Write:
                return SplitQueryType.UNKNOWN;
            case Script:
                return SplitQueryType.UNKNOWN;
            case Maintenance:
                return SplitQueryType.ADMIN;
            case Monitor:
                return SplitQueryType.PERFORMANCE;
            case Connection:
                return SplitQueryType.UNKNOWN;
            case PubSub:
                return SplitQueryType.UNKNOWN;
            case Transaction:
                return SplitQueryType.TRANSACTION;
            case Other:
            default:
                return SplitQueryType.UNKNOWN;
        }
    }
}

