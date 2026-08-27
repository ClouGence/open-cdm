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
package com.clougence.clouddm.ds.valkey.execute.jdbc;

import java.sql.SQLException;

import com.clougence.sql.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.sql.redis.parser.ast.commands.bit.*;
import com.clougence.sql.redis.parser.ast.commands.client.PingRedisCmd;
import com.clougence.sql.redis.parser.ast.commands.config.ConfigGetCmd;
import com.clougence.sql.redis.parser.ast.commands.control.DbSizeRedisCmd;
import com.clougence.sql.redis.parser.ast.commands.hash.*;
import com.clougence.sql.redis.parser.ast.commands.info.InfoRedisCmd;
import com.clougence.sql.redis.parser.ast.commands.keys.*;
import com.clougence.sql.redis.parser.ast.commands.list.*;
import com.clougence.sql.redis.parser.ast.commands.pf.PFAddRedisCmd;
import com.clougence.sql.redis.parser.ast.commands.pf.PFCountRedisCmd;
import com.clougence.sql.redis.parser.ast.commands.pf.PFMergeRedisCmd;
import com.clougence.sql.redis.parser.ast.commands.set.*;
import com.clougence.sql.redis.parser.ast.commands.sortedset.*;
import com.clougence.sql.redis.parser.ast.commands.string.*;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class GlideDistributeCall {

    public static CgFuture<?> execRedisCmd(CgFuture<Object> sync, GlideCmd glideCmd, AbstractRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        switch (command.getCmdType()) {
            /* ------------------------------------------------------------------------------------------------ String Commands */
            case APPEND:
                return GlideCallForString.cmdAppendRedisCmd(sync, glideCmd, (AppendRedisCmd) command, request, receive);
            case DECR:
                return GlideCallForString.cmdDecrRedisCmd(sync, glideCmd, (DecrRedisCmd) command, request, receive);
            case DECRBY:
                return GlideCallForString.cmdDecrByRedisCmd(sync, glideCmd, (DecrByRedisCmd) command, request, receive);
            case GET:
                return GlideCallForString.cmdGetRedisCmd(sync, glideCmd, (GetRedisCmd) command, request, receive);
            case GETDEL:
                return GlideCallForString.cmdGetDelRedisCmd(sync, glideCmd, (GetDelRedisCmd) command, request, receive);
            case GETEX:
                return GlideCallForString.cmdGetEXRedisCmd(sync, glideCmd, (GetEXRedisCmd) command, request, receive);
            case GETRANGE:
                return GlideCallForString.cmdGetRangeRedisCmd(sync, glideCmd, (GetRangeRedisCmd) command, request, receive);
            case GETSET:
                return GlideCallForString.cmdGetSetRedisCmd(sync, glideCmd, (GetSetRedisCmd) command, request, receive);
            case INCR:
                return GlideCallForString.cmdIncrRedisCmd(sync, glideCmd, (IncrRedisCmd) command, request, receive);
            case INCRBY:
                return GlideCallForString.cmdIncrByRedisCmd(sync, glideCmd, (IncrByRedisCmd) command, request, receive);
            case INCRBYFLOAT:
                return GlideCallForString.cmdIncrByFloatRedisCmd(sync, glideCmd, (IncrByFloatRedisCmd) command, request, receive);
            case LCS:
                return GlideCallForString.cmdLcsRedisCmd(sync, glideCmd, (LcsRedisCmd) command, request, receive);
            case MGET:
                return GlideCallForString.cmdMGetRedisCmd(sync, glideCmd, (MGetRedisCmd) command, request, receive);
            case MSET:
                return GlideCallForString.cmdMSetRedisCmd(sync, glideCmd, (MSetRedisCmd) command, request, receive);
            case MSETNX:
                return GlideCallForString.cmdMSetNXRedisCmd(sync, glideCmd, (MSetNXRedisCmd) command, request, receive);
            case PSETEX:
                return GlideCallForString.cmdPSetEXRedisCmd(sync, glideCmd, (PSetEXRedisCmd) command, request, receive);
            case SET:
                return GlideCallForString.cmdSetRedisCmd(sync, glideCmd, (SetRedisCmd) command, request, receive);
            case SETEX:
                return GlideCallForString.cmdSetEXRedisCmd(sync, glideCmd, (SetEXRedisCmd) command, request, receive);
            case SETNX:
                return GlideCallForString.cmdSetNXRedisCmd(sync, glideCmd, (SetNXRedisCmd) command, request, receive);
            case SETRANGE:
                return GlideCallForString.cmdSetRangeRedisCmd(sync, glideCmd, (SetRangeRedisCmd) command, request, receive);
            case STRLEN:
                return GlideCallForString.cmdStrLenRedisCmd(sync, glideCmd, (StrLenRedisCmd) command, request, receive);
            case SUBSTR:
                return GlideCallForString.cmdSubstrRedisCmd(sync, glideCmd, (SubstrRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ Bit commands */
            case BITFIELD:
                return GlideCallForBit.cmdBitFieldRedisCmd(sync, glideCmd, (BitFieldRedisCmd) command, request, receive);
            case BITFIELD_RO:
                return GlideCallForBit.cmdBitFieldRORedisCmd(sync, glideCmd, (BitFieldRORedisCmd) command, request, receive);
            case BITCOUNT:
                return GlideCallForBit.cmdBitCountRedisCmd(sync, glideCmd, (BitCountRedisCmd) command, request, receive);
            case BITOP:
                return GlideCallForBit.cmdBitOPRedisCmd(sync, glideCmd, (BitOPRedisCmd) command, request, receive);
            case BITPOS:
                return GlideCallForBit.cmdBitPosRedisCmd(sync, glideCmd, (BitPosRedisCmd) command, request, receive);
            case GETBIT:
                return GlideCallForBit.cmdGetBitRedisCmd(sync, glideCmd, (GetBitRedisCmd) command, request, receive);
            case SETBIT:
                return GlideCallForBit.cmdSetBitRedisCmd(sync, glideCmd, (SetBitRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ Hash commands */
            case HDEL:
                return GlideCallForHash.cmdHDelRedisCmd(sync, glideCmd, (HDelRedisCmd) command, request, receive);
            case HEXISTS:
                return GlideCallForHash.cmdHExistsRedisCmd(sync, glideCmd, (HExistsRedisCmd) command, request, receive);
            case HEXPIRE:
                return GlideCallForHash.cmdHExpireRedisCmd(sync, glideCmd, (HExpireRedisCmd) command, request, receive);
            case HEXPIREAT:
                return GlideCallForHash.cmdHExpireAtRedisCmd(sync, glideCmd, (HExpireAtRedisCmd) command, request, receive);
            case HEXPIRETIME:
                return GlideCallForHash.cmdHExpireTimeRedisCmd(sync, glideCmd, (HExpireTimeRedisCmd) command, request, receive);
            case HGET:
                return GlideCallForHash.cmdHGetRedisCmd(sync, glideCmd, (HGetRedisCmd) command, request, receive);
            case HGETALL:
                return GlideCallForHash.cmdHGetAllRedisCmd(sync, glideCmd, (HGetAllRedisCmd) command, request, receive);
            case HGETDEL:
                return GlideCallForHash.cmdHGetDelRedisCmd(sync, glideCmd, (HGetDelRedisCmd) command, request, receive);
            case HGETEX:
                return GlideCallForHash.cmdHGetEXRedisCmd(sync, glideCmd, (HGetEXRedisCmd) command, request, receive);
            case HINCRBY:
                return GlideCallForHash.cmdHIncrByRedisCmd(sync, glideCmd, (HIncrByRedisCmd) command, request, receive);
            case HINCRBYFLOAT:
                return GlideCallForHash.cmdHIncrByFloatRedisCmd(sync, glideCmd, (HIncrByFloatRedisCmd) command, request, receive);
            case HKEYS:
                return GlideCallForHash.cmdHKeysRedisCmd(sync, glideCmd, (HKeysRedisCmd) command, request, receive);
            case HLEN:
                return GlideCallForHash.cmdHLenRedisCmd(sync, glideCmd, (HLenRedisCmd) command, request, receive);
            case HMGET:
                return GlideCallForHash.cmdHMGetRedisCmd(sync, glideCmd, (HMGetRedisCmd) command, request, receive);
            case HMSET:
                return GlideCallForHash.cmdHMSetRedisCmd(sync, glideCmd, (HMSetRedisCmd) command, request, receive);
            case HPERSIST:
                return GlideCallForHash.cmdHPersistRedisCmd(sync, glideCmd, (HPersistRedisCmd) command, request, receive);
            case HPEXPIRE:
                return GlideCallForHash.cmdHPExpireRedisCmd(sync, glideCmd, (HPExpireRedisCmd) command, request, receive);
            case HPEXPIREAT:
                return GlideCallForHash.cmdHPExpireAtRedisCmd(sync, glideCmd, (HPExpireAtRedisCmd) command, request, receive);
            case HPEXPIRETIME:
                return GlideCallForHash.cmdHPExpireTimeRedisCmd(sync, glideCmd, (HPExpireTimeRedisCmd) command, request, receive);
            case HPTTL:
                return GlideCallForHash.cmdHPTtlRedisCmd(sync, glideCmd, (HPTtlRedisCmd) command, request, receive);
            case HTTL:
                return GlideCallForHash.cmdHTtlRedisCmd(sync, glideCmd, (HTtlRedisCmd) command, request, receive);
            case HRANDFIELD:
                return GlideCallForHash.cmdHRandFieldRedisCmd(sync, glideCmd, (HRandFieldRedisCmd) command, request, receive);
            case HSCAN:
                return GlideCallForHash.cmdHScanRedisCmd(sync, glideCmd, (HScanRedisCmd) command, request, receive);
            case HSET:
                return GlideCallForHash.cmdHSetRedisCmd(sync, glideCmd, (HSetRedisCmd) command, request, receive);
            case HSETEX:
                return GlideCallForHash.cmdHSetEXRedisCmd(sync, glideCmd, (HSetEXRedisCmd) command, request, receive);
            case HSETNX:
                return GlideCallForHash.cmdHSetNXRedisCmd(sync, glideCmd, (HSetNXRedisCmd) command, request, receive);
            case HSTRLEN:
                return GlideCallForHash.cmdHStrLenRedisCmd(sync, glideCmd, (HStrLenRedisCmd) command, request, receive);
            case HVALS:
                return GlideCallForHash.cmdHValsRedisCmd(sync, glideCmd, (HValsRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ List Commands */
            case BLMOVE:
                return GlideCallForList.cmdBLMoveRedisCmd(sync, glideCmd, (BLMoveRedisCmd) command, request, receive);
            case BLMPOP:
                return GlideCallForList.cmdBLMPopRedisCmd(sync, glideCmd, (BLMPopRedisCmd) command, request, receive);
            case BLPOP:
                return GlideCallForList.cmdBLPopRedisCmd(sync, glideCmd, (BLPopRedisCmd) command, request, receive);
            case BRPOP:
                return GlideCallForList.cmdBRPopRedisCmd(sync, glideCmd, (BRPopRedisCmd) command, request, receive);
            case BRPOPLPUSH:
                return GlideCallForList.cmdBRPopLPushRedisCmd(sync, glideCmd, (BRPopLPushRedisCmd) command, request, receive);
            case LINDEX:
                return GlideCallForList.cmdLIndexRedisCmd(sync, glideCmd, (LIndexRedisCmd) command, request, receive);
            case LINSERT:
                return GlideCallForList.cmdLInsertRedisCmd(sync, glideCmd, (LInsertRedisCmd) command, request, receive);
            case LLEN:
                return GlideCallForList.cmdLLenRedisCmd(sync, glideCmd, (LLenRedisCmd) command, request, receive);
            case LMOVE:
                return GlideCallForList.cmdLMoveRedisCmd(sync, glideCmd, (LMoveRedisCmd) command, request, receive);
            case LMPOP:
                return GlideCallForList.cmdLMPopRedisCmd(sync, glideCmd, (LMPopRedisCmd) command, request, receive);
            case LPOP:
                return GlideCallForList.cmdLPopRedisCmd(sync, glideCmd, (LPopRedisCmd) command, request, receive);
            case LPOS:
                return GlideCallForList.cmdLPosRedisCmd(sync, glideCmd, (LPosRedisCmd) command, request, receive);
            case LPUSH:
                return GlideCallForList.cmdLPushRedisCmd(sync, glideCmd, (LPushRedisCmd) command, request, receive);
            case LPUSHX:
                return GlideCallForList.cmdLPushXRedisCmd(sync, glideCmd, (LPushXRedisCmd) command, request, receive);
            case LRANGE:
                return GlideCallForList.cmdLRangeRedisCmd(sync, glideCmd, (LRangeRedisCmd) command, request, receive);
            case LREM:
                return GlideCallForList.cmdLRemRedisCmd(sync, glideCmd, (LRemRedisCmd) command, request, receive);
            case LSET:
                return GlideCallForList.cmdLSetRedisCmd(sync, glideCmd, (LSetRedisCmd) command, request, receive);
            case LTRIM:
                return GlideCallForList.cmdLTrimRedisCmd(sync, glideCmd, (LTrimRedisCmd) command, request, receive);
            case RPOP:
                return GlideCallForList.cmdRPopRedisCmd(sync, glideCmd, (RPopRedisCmd) command, request, receive);
            case RPOPLPUSH:
                return GlideCallForList.cmdRPopLPushRedisCmd(sync, glideCmd, (RPopLPushRedisCmd) command, request, receive);
            case RPUSH:
                return GlideCallForList.cmdRPushRedisCmd(sync, glideCmd, (RPushRedisCmd) command, request, receive);
            case RPUSHX:
                return GlideCallForList.cmdRPushXRedisCmd(sync, glideCmd, (RPushXRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ Set Commands */
            case SADD:
                return GlideCallForSet.cmdSAddRedisCmd(sync, glideCmd, (SAddRedisCmd) command, request, receive);
            case SCARD:
                return GlideCallForSet.cmdSCardRedisCmd(sync, glideCmd, (SCardRedisCmd) command, request, receive);
            case SDIFF:
                return GlideCallForSet.cmdSDiffRedisCmd(sync, glideCmd, (SDiffRedisCmd) command, request, receive);
            case SDIFFSTORE:
                return GlideCallForSet.cmdSDiffStoreRedisCmd(sync, glideCmd, (SDiffStoreRedisCmd) command, request, receive);
            case SINTER:
                return GlideCallForSet.cmdSInterRedisCmd(sync, glideCmd, (SInterRedisCmd) command, request, receive);
            case SINTERCARD:
                return GlideCallForSet.cmdSInterCardRedisCmd(sync, glideCmd, (SInterCardRedisCmd) command, request, receive);
            case SINTERSTORE:
                return GlideCallForSet.cmdSinterStoreRedisCmd(sync, glideCmd, (SinterStoreRedisCmd) command, request, receive);
            case SISMEMBER:
                return GlideCallForSet.cmdSISMemberRedisCmd(sync, glideCmd, (SISMemberRedisCmd) command, request, receive);
            case SMEMBERS:
                return GlideCallForSet.cmdSMembersRedisCmd(sync, glideCmd, (SMembersRedisCmd) command, request, receive);
            case SMISMEMBER:
                return GlideCallForSet.cmdSMISMemberRedisCmd(sync, glideCmd, (SMISMemberRedisCmd) command, request, receive);
            case SMOVE:
                return GlideCallForSet.cmdSMoveRedisCmd(sync, glideCmd, (SMoveRedisCmd) command, request, receive);
            case SPOP:
                return GlideCallForSet.cmdSPopRedisCmd(sync, glideCmd, (SPopRedisCmd) command, request, receive);
            case SRANDMEMBER:
                return GlideCallForSet.cmdSRandMemberRedisCmd(sync, glideCmd, (SRandMemberRedisCmd) command, request, receive);
            case SREM:
                return GlideCallForSet.cmdSRemRedisCmd(sync, glideCmd, (SRemRedisCmd) command, request, receive);
            case SSCAN:
                return GlideCallForSet.cmdSSCanRedisCmd(sync, glideCmd, (SSCanRedisCmd) command, request, receive);
            case SUNION:
                return GlideCallForSet.cmdSUnionRedisCmd(sync, glideCmd, (SUnionRedisCmd) command, request, receive);
            case SUNIONSTORE:
                return GlideCallForSet.cmdSUnionStoreRedisCmd(sync, glideCmd, (SUnionStoreRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ Sorted Set Commands */
            case BZMPOP:
                return GlideCallForSortedSet.cmdBZMPopRedisCmd(sync, glideCmd, (BZMPopRedisCmd) command, request, receive);
            case BZPOPMAX:
                return GlideCallForSortedSet.cmdBZPopMaxRedisCmd(sync, glideCmd, (BZPopMaxRedisCmd) command, request, receive);
            case BZPOPMIN:
                return GlideCallForSortedSet.cmdBZPopMinRedisCmd(sync, glideCmd, (BZPopMinRedisCmd) command, request, receive);
            case ZADD:
                return GlideCallForSortedSet.cmdZAddRedisCmd(sync, glideCmd, (ZAddRedisCmd) command, request, receive);
            case ZCARD:
                return GlideCallForSortedSet.cmdZCardRedisCmd(sync, glideCmd, (ZCardRedisCmd) command, request, receive);
            case ZCOUNT:
                return GlideCallForSortedSet.cmdZCountRedisCmd(sync, glideCmd, (ZCountRedisCmd) command, request, receive);
            case ZDIFF:
                return GlideCallForSortedSet.cmdZDiffRedisCmd(sync, glideCmd, (ZDiffRedisCmd) command, request, receive);
            case ZDIFFSTORE:
                return GlideCallForSortedSet.cmdZDiffStoreRedisCmd(sync, glideCmd, (ZDiffStoreRedisCmd) command, request, receive);
            case ZINCRBY:
                return GlideCallForSortedSet.cmdZIncrByRedisCmd(sync, glideCmd, (ZIncrByRedisCmd) command, request, receive);
            case ZINTER:
                return GlideCallForSortedSet.cmdZInterRedisCmd(sync, glideCmd, (ZInterRedisCmd) command, request, receive);
            case ZINTERCARD:
                return GlideCallForSortedSet.cmdZInterCardRedisCmd(sync, glideCmd, (ZInterCardRedisCmd) command, request, receive);
            case ZINTERSTORE:
                return GlideCallForSortedSet.cmdZInterStoreRedisCmd(sync, glideCmd, (ZInterStoreRedisCmd) command, request, receive);
            case ZLEXCOUNT:
                return GlideCallForSortedSet.cmdZLexCountRedisCmd(sync, glideCmd, (ZLexCountRedisCmd) command, request, receive);
            case ZMPOP:
                return GlideCallForSortedSet.cmdZMPopRedisCmd(sync, glideCmd, (ZMPopRedisCmd) command, request, receive);
            case ZMSCORE:
                return GlideCallForSortedSet.cmdZMSCoreRedisCmd(sync, glideCmd, (ZMScoreRedisCmd) command, request, receive);
            case ZPOPMAX:
                return GlideCallForSortedSet.cmdZPopMaxRedisCmd(sync, glideCmd, (ZPopMaxRedisCmd) command, request, receive);
            case ZPOPMIN:
                return GlideCallForSortedSet.cmdZPopMinRedisCmd(sync, glideCmd, (ZPopMinRedisCmd) command, request, receive);
            case ZRANDMEMBER:
                return GlideCallForSortedSet.cmdZRAndMemberRedisCmd(sync, glideCmd, (ZRAndMemberRedisCmd) command, request, receive);
            case ZRANGE:
                return GlideCallForSortedSet.cmdZRangeRedisCmd(sync, glideCmd, (ZRangeRedisCmd) command, request, receive);
            case ZRANGEBYLEX:
                return GlideCallForSortedSet.cmdZRangeByLexRedisCmd(sync, glideCmd, (ZRangeByLexRedisCmd) command, request, receive);
            case ZRANGEBYSCORE:
                return GlideCallForSortedSet.cmdZRangeByScoreRedisCmd(sync, glideCmd, (ZRangeByScoreRedisCmd) command, request, receive);
            case ZRANGESTORE:
                return GlideCallForSortedSet.cmdZRangeStoreRedisCmd(sync, glideCmd, (ZRangeStoreRedisCmd) command, request, receive);
            case ZRANK:
                return GlideCallForSortedSet.cmdZRankRedisCmd(sync, glideCmd, (ZRankRedisCmd) command, request, receive);
            case ZREM:
                return GlideCallForSortedSet.cmdZRemRedisCmd(sync, glideCmd, (ZRemRedisCmd) command, request, receive);
            case ZREMRANGEBYLEX:
                return GlideCallForSortedSet.cmdZRemRangeByLexRedisCmd(sync, glideCmd, (ZRemRangeByLexRedisCmd) command, request, receive);
            case ZREMRANGEBYRANK:
                return GlideCallForSortedSet.cmdZRemRangeByRankRedisCmd(sync, glideCmd, (ZRemRangeByRankRedisCmd) command, request, receive);
            case ZREMRANGEBYSCORE:
                return GlideCallForSortedSet.cmdZRemRangeByScoreRedisCmd(sync, glideCmd, (ZRemRangeByScoreRedisCmd) command, request, receive);
            case ZREVRANGE:
                return GlideCallForSortedSet.cmdZRevRangeRedisCmd(sync, glideCmd, (ZRevRangeRedisCmd) command, request, receive);
            case ZREVRANGEBYLEX:
                return GlideCallForSortedSet.cmdZRevRangeByLexRedisCmd(sync, glideCmd, (ZRevRangeByLexRedisCmd) command, request, receive);
            case ZREVRANGEBYSCORE:
                return GlideCallForSortedSet.cmdZRevRangeByScoreRedisCmd(sync, glideCmd, (ZRevRangeByScoreRedisCmd) command, request, receive);
            case ZREVRANK:
                return GlideCallForSortedSet.cmdZRevRankRedisCmd(sync, glideCmd, (ZRevRankRedisCmd) command, request, receive);
            case ZSCAN:
                return GlideCallForSortedSet.cmdZScanRedisCmd(sync, glideCmd, (ZSCanRedisCmd) command, request, receive);
            case ZSCORE:
                return GlideCallForSortedSet.cmdZScoreRedisCmd(sync, glideCmd, (ZScoreRedisCmd) command, request, receive);
            case ZUNION:
                return GlideCallForSortedSet.cmdZUnionRedisCmd(sync, glideCmd, (ZUnionRedisCmd) command, request, receive);
            case ZUNIONSTORE:
                return GlideCallForSortedSet.cmdZUnionStoreRedisCmd(sync, glideCmd, (ZUnionStoreRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ Key Commands */
            case DEL:
                return GlideCallForKeys.cmdDelRedisCmd(sync, glideCmd, (DelRedisCmd) command, request, receive);
            case DUMP:
                return GlideCallForKeys.cmdDumpRedisCmd(sync, glideCmd, (DumpRedisCmd) command, request, receive);
            case EXISTS:
                return GlideCallForKeys.cmdExistsRedisCmd(sync, glideCmd, (ExistsRedisCmd) command, request, receive);
            case EXPIRE:
                return GlideCallForKeys.cmdExpireRedisCmd(sync, glideCmd, (ExpireRedisCmd) command, request, receive);
            case EXPIREAT:
                return GlideCallForKeys.cmdExpireAtRedisCmd(sync, glideCmd, (ExpireAtRedisCmd) command, request, receive);
            case EXPIRETIME:
                return GlideCallForKeys.cmdExpireTimeRedisCmd(sync, glideCmd, (ExpireTimeRedisCmd) command, request, receive);
            case KEYS:
                return GlideCallForKeys.cmdKeysRedisCmd(sync, glideCmd, (KeysRedisCmd) command, request, receive);
            case MOVE:
                return GlideCallForKeys.cmdMoveRedisCmd(sync, glideCmd, (MoveRedisCmd) command, request, receive);
            case OBJECT:
                return GlideCallForKeys.cmdObjectRedisCmd(sync, glideCmd, (ObjectRedisCmd) command, request, receive);
            case PERSIST:
                return GlideCallForKeys.cmdPersistRedisCmd(sync, glideCmd, (PersistRedisCmd) command, request, receive);
            case PEXPIRE:
                return GlideCallForKeys.cmdPExpireRedisCmd(sync, glideCmd, (PExpireRedisCmd) command, request, receive);
            case PEXPIREAT:
                return GlideCallForKeys.cmdPExpireAtRedisCmd(sync, glideCmd, (PExpireAtRedisCmd) command, request, receive);
            case PEXPIRETIME:
                return GlideCallForKeys.cmdPExpireTimeRedisCmd(sync, glideCmd, (PExpireTimeRedisCmd) command, request, receive);
            case PTTL:
                return GlideCallForKeys.cmdPTTLRedisCmd(sync, glideCmd, (PTTLRedisCmd) command, request, receive);
            case TTL:
                return GlideCallForKeys.cmdTTLTimeRedisCmd(sync, glideCmd, (TTLRedisCmd) command, request, receive);
            case RANDOMKEY:
                return GlideCallForKeys.cmdRandomKeyRedisCmd(sync, glideCmd, (RandomKeyRedisCmd) command, request, receive);
            case RENAME:
                return GlideCallForKeys.cmdRenameRedisCmd(sync, glideCmd, (RenameRedisCmd) command, request, receive);
            case RENAMENX:
                return GlideCallForKeys.cmdRenameNXRedisCmd(sync, glideCmd, (RenameNXRedisCmd) command, request, receive);
            case RESTORE:
                return GlideCallForKeys.cmdRestoreRedisCmd(sync, glideCmd, (RestoreRedisCmd) command, request, receive);
            case SCAN:
                return GlideCallForKeys.cmdScanRedisCmd(sync, glideCmd, (ScanRedisCmd) command, request, receive);
            case SORT:
                return GlideCallForKeys.cmdSortRedisCmd(sync, glideCmd, (SortRedisCmd) command, request, receive);
            case SORT_RO:
                return GlideCallForKeys.cmdSortRORedisCmd(sync, glideCmd, (SortRORedisCmd) command, request, receive);
            case TOUCH:
                return GlideCallForKeys.cmdTouchRedisCmd(sync, glideCmd, (TouchRedisCmd) command, request, receive);
            case TYPE:
                return GlideCallForKeys.cmdTypeRedisCmd(sync, glideCmd, (TypeRedisCmd) command, request, receive);
            case UNLINK:
                return GlideCallForKeys.cmdUnlinkRedisCmd(sync, glideCmd, (UnlinkRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ HyperLog Commands */
            case PFADD:
                return GlideCallForHyperLog.cmdPFAddRedisCmd(sync, glideCmd, (PFAddRedisCmd) command, request, receive);
            case PFCOUNT:
                return GlideCallForHyperLog.cmdPFCountRedisCmd(sync, glideCmd, (PFCountRedisCmd) command, request, receive);
            case PFMERGE:
                return GlideCallForHyperLog.cmdPFMergeRedisCmd(sync, glideCmd, (PFMergeRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ DatabaseCommands */
            case COPY:
                return GlideCallForDB.cmdCopyRedisCmd(sync, glideCmd, (CopyRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ info Commands */
            case INFO:
                return GlideCallForServer.cmdInfoRedisCmd(sync, glideCmd, (InfoRedisCmd) command, request, receive);
            case PING:
                return GlideCallForServer.cmdPingRedisCmd(sync, glideCmd, (PingRedisCmd) command, request, receive);
            case WAIT:
                return GlideCallForServer.cmdWaitRedisCmd(sync, glideCmd, (WaitRedisCmd) command, request, receive);
            case WAITAOF:
                return GlideCallForServer.cmdWaitAOFRedisCmd(sync, glideCmd, (WaitAOFRedisCmd) command, request, receive);
            case DBSIZE:
                return GlideCallForServer.cmdDbSizeRedisCmd(sync, glideCmd, (DbSizeRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ config commands */
            case CONFIG_GET:
                return GlideCallForConfig.cmdConfigGetCmd(sync, glideCmd, (ConfigGetCmd) command, request, receive);
            default:
                throw new UnsupportedOperationException("redis command '" + command.getCmdType().getCommandStr() + "' Unsupported.");
        }
    }
}
