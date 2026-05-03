package com.clougence.clouddm.ds.redis.execute.jdbc;

import java.sql.SQLException;

import com.clougence.clouddm.ds.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.bit.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.client.PingRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.config.ConfigGetCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.control.DbSizeRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.hash.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.info.InfoRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.keys.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.list.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.pf.PFAddRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.pf.PFCountRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.pf.PFMergeRedisCmd;
import com.clougence.clouddm.ds.redis.parser.ast.commands.set.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.sortedset.*;
import com.clougence.clouddm.ds.redis.parser.ast.commands.string.*;
import com.clougence.drivers.adapter.AdapterReceive;
import com.clougence.drivers.adapter.AdapterRequest;
import com.clougence.utils.future.CgFuture;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class JedisDistributeCall {

    public static CgFuture<?> execRedisCmd(CgFuture<Object> sync, JedisCmd jedisCmd, AbstractRedisCmd command, AdapterRequest request, AdapterReceive receive) throws SQLException {
        switch (command.getCmdType()) {
            /* ------------------------------------------------------------------------------------------------ String Commands */
            case APPEND:
                return JedisCallForString.cmdAppendRedisCmd(sync, jedisCmd, (AppendRedisCmd) command, request, receive);
            case DECR:
                return JedisCallForString.cmdDecrRedisCmd(sync, jedisCmd, (DecrRedisCmd) command, request, receive);
            case DECRBY:
                return JedisCallForString.cmdDecrByRedisCmd(sync, jedisCmd, (DecrByRedisCmd) command, request, receive);
            case GET:
                return JedisCallForString.cmdGetRedisCmd(sync, jedisCmd, (GetRedisCmd) command, request, receive);
            case GETDEL:
                return JedisCallForString.cmdGetDelRedisCmd(sync, jedisCmd, (GetDelRedisCmd) command, request, receive);
            case GETEX:
                return JedisCallForString.cmdGetEXRedisCmd(sync, jedisCmd, (GetEXRedisCmd) command, request, receive);
            case GETRANGE:
                return JedisCallForString.cmdGetRangeRedisCmd(sync, jedisCmd, (GetRangeRedisCmd) command, request, receive);
            case GETSET:
                return JedisCallForString.cmdGetSetRedisCmd(sync, jedisCmd, (GetSetRedisCmd) command, request, receive);
            case INCR:
                return JedisCallForString.cmdIncrRedisCmd(sync, jedisCmd, (IncrRedisCmd) command, request, receive);
            case INCRBY:
                return JedisCallForString.cmdIncrByRedisCmd(sync, jedisCmd, (IncrByRedisCmd) command, request, receive);
            case INCRBYFLOAT:
                return JedisCallForString.cmdIncrByFloatRedisCmd(sync, jedisCmd, (IncrByFloatRedisCmd) command, request, receive);
            case LCS:
                return JedisCallForString.cmdLcsRedisCmd(sync, jedisCmd, (LcsRedisCmd) command, request, receive);
            case MGET:
                return JedisCallForString.cmdMGetRedisCmd(sync, jedisCmd, (MGetRedisCmd) command, request, receive);
            case MSET:
                return JedisCallForString.cmdMSetRedisCmd(sync, jedisCmd, (MSetRedisCmd) command, request, receive);
            case MSETNX:
                return JedisCallForString.cmdMSetNXRedisCmd(sync, jedisCmd, (MSetNXRedisCmd) command, request, receive);
            case PSETEX:
                return JedisCallForString.cmdPSetEXRedisCmd(sync, jedisCmd, (PSetEXRedisCmd) command, request, receive);
            case SET:
                return JedisCallForString.cmdSetRedisCmd(sync, jedisCmd, (SetRedisCmd) command, request, receive);
            case SETEX:
                return JedisCallForString.cmdSetEXRedisCmd(sync, jedisCmd, (SetEXRedisCmd) command, request, receive);
            case SETNX:
                return JedisCallForString.cmdSetNXRedisCmd(sync, jedisCmd, (SetNXRedisCmd) command, request, receive);
            case SETRANGE:
                return JedisCallForString.cmdSetRangeRedisCmd(sync, jedisCmd, (SetRangeRedisCmd) command, request, receive);
            case STRLEN:
                return JedisCallForString.cmdStrLenRedisCmd(sync, jedisCmd, (StrLenRedisCmd) command, request, receive);
            case SUBSTR:
                return JedisCallForString.cmdSubstrRedisCmd(sync, jedisCmd, (SubstrRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ Bit commands */
            case BITFIELD:
                return JedisCallForBit.cmdBitFieldRedisCmd(sync, jedisCmd, (BitFieldRedisCmd) command, request, receive);
            case BITFIELD_RO:
                return JedisCallForBit.cmdBitFieldRORedisCmd(sync, jedisCmd, (BitFieldRORedisCmd) command, request, receive);
            case BITCOUNT:
                return JedisCallForBit.cmdBitCountRedisCmd(sync, jedisCmd, (BitCountRedisCmd) command, request, receive);
            case BITOP:
                return JedisCallForBit.cmdBitOPRedisCmd(sync, jedisCmd, (BitOPRedisCmd) command, request, receive);
            case BITPOS:
                return JedisCallForBit.cmdBitPosRedisCmd(sync, jedisCmd, (BitPosRedisCmd) command, request, receive);
            case GETBIT:
                return JedisCallForBit.cmdGetBitRedisCmd(sync, jedisCmd, (GetBitRedisCmd) command, request, receive);
            case SETBIT:
                return JedisCallForBit.cmdSetBitRedisCmd(sync, jedisCmd, (SetBitRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ Hash commands */
            case HDEL:
                return JedisCallForHash.cmdHDelRedisCmd(sync, jedisCmd, (HDelRedisCmd) command, request, receive);
            case HEXISTS:
                return JedisCallForHash.cmdHExistsRedisCmd(sync, jedisCmd, (HExistsRedisCmd) command, request, receive);
            case HEXPIRE:
                return JedisCallForHash.cmdHExpireRedisCmd(sync, jedisCmd, (HExpireRedisCmd) command, request, receive);
            case HEXPIREAT:
                return JedisCallForHash.cmdHExpireAtRedisCmd(sync, jedisCmd, (HExpireAtRedisCmd) command, request, receive);
            case HEXPIRETIME:
                return JedisCallForHash.cmdHExpireTimeRedisCmd(sync, jedisCmd, (HExpireTimeRedisCmd) command, request, receive);
            case HGET:
                return JedisCallForHash.cmdHGetRedisCmd(sync, jedisCmd, (HGetRedisCmd) command, request, receive);
            case HGETALL:
                return JedisCallForHash.cmdHGetAllRedisCmd(sync, jedisCmd, (HGetAllRedisCmd) command, request, receive);
            case HGETDEL:
                return JedisCallForHash.cmdHGetDelRedisCmd(sync, jedisCmd, (HGetDelRedisCmd) command, request, receive);
            case HGETEX:
                return JedisCallForHash.cmdHGetEXRedisCmd(sync, jedisCmd, (HGetEXRedisCmd) command, request, receive);
            case HINCRBY:
                return JedisCallForHash.cmdHIncrByRedisCmd(sync, jedisCmd, (HIncrByRedisCmd) command, request, receive);
            case HINCRBYFLOAT:
                return JedisCallForHash.cmdHIncrByFloatRedisCmd(sync, jedisCmd, (HIncrByFloatRedisCmd) command, request, receive);
            case HKEYS:
                return JedisCallForHash.cmdHKeysRedisCmd(sync, jedisCmd, (HKeysRedisCmd) command, request, receive);
            case HLEN:
                return JedisCallForHash.cmdHLenRedisCmd(sync, jedisCmd, (HLenRedisCmd) command, request, receive);
            case HMGET:
                return JedisCallForHash.cmdHMGetRedisCmd(sync, jedisCmd, (HMGetRedisCmd) command, request, receive);
            case HMSET:
                return JedisCallForHash.cmdHMSetRedisCmd(sync, jedisCmd, (HMSetRedisCmd) command, request, receive);
            case HPERSIST:
                return JedisCallForHash.cmdHPersistRedisCmd(sync, jedisCmd, (HPersistRedisCmd) command, request, receive);
            case HPEXPIRE:
                return JedisCallForHash.cmdHPExpireRedisCmd(sync, jedisCmd, (HPExpireRedisCmd) command, request, receive);
            case HPEXPIREAT:
                return JedisCallForHash.cmdHPExpireAtRedisCmd(sync, jedisCmd, (HPExpireAtRedisCmd) command, request, receive);
            case HPEXPIRETIME:
                return JedisCallForHash.cmdHPExpireTimeRedisCmd(sync, jedisCmd, (HPExpireTimeRedisCmd) command, request, receive);
            case HPTTL:
                return JedisCallForHash.cmdHPTtlRedisCmd(sync, jedisCmd, (HPTtlRedisCmd) command, request, receive);
            case HTTL:
                return JedisCallForHash.cmdHTtlRedisCmd(sync, jedisCmd, (HTtlRedisCmd) command, request, receive);
            case HRANDFIELD:
                return JedisCallForHash.cmdHRandFieldRedisCmd(sync, jedisCmd, (HRandFieldRedisCmd) command, request, receive);
            case HSCAN:
                return JedisCallForHash.cmdHScanRedisCmd(sync, jedisCmd, (HScanRedisCmd) command, request, receive);
            case HSET:
                return JedisCallForHash.cmdHSetRedisCmd(sync, jedisCmd, (HSetRedisCmd) command, request, receive);
            case HSETEX:
                return JedisCallForHash.cmdHSetEXRedisCmd(sync, jedisCmd, (HSetEXRedisCmd) command, request, receive);
            case HSETNX:
                return JedisCallForHash.cmdHSetNXRedisCmd(sync, jedisCmd, (HSetNXRedisCmd) command, request, receive);
            case HSTRLEN:
                return JedisCallForHash.cmdHStrLenRedisCmd(sync, jedisCmd, (HStrLenRedisCmd) command, request, receive);
            case HVALS:
                return JedisCallForHash.cmdHValsRedisCmd(sync, jedisCmd, (HValsRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ List Commands */
            case BLMOVE:
                return JedisCallForList.cmdBLMoveRedisCmd(sync, jedisCmd, (BLMoveRedisCmd) command, request, receive);
            case BLMPOP:
                return JedisCallForList.cmdBLMPopRedisCmd(sync, jedisCmd, (BLMPopRedisCmd) command, request, receive);
            case BLPOP:
                return JedisCallForList.cmdBLPopRedisCmd(sync, jedisCmd, (BLPopRedisCmd) command, request, receive);
            case BRPOP:
                return JedisCallForList.cmdBRPopRedisCmd(sync, jedisCmd, (BRPopRedisCmd) command, request, receive);
            case BRPOPLPUSH:
                return JedisCallForList.cmdBRPopLPushRedisCmd(sync, jedisCmd, (BRPopLPushRedisCmd) command, request, receive);
            case LINDEX:
                return JedisCallForList.cmdLIndexRedisCmd(sync, jedisCmd, (LIndexRedisCmd) command, request, receive);
            case LINSERT:
                return JedisCallForList.cmdLInsertRedisCmd(sync, jedisCmd, (LInsertRedisCmd) command, request, receive);
            case LLEN:
                return JedisCallForList.cmdLLenRedisCmd(sync, jedisCmd, (LLenRedisCmd) command, request, receive);
            case LMOVE:
                return JedisCallForList.cmdLMoveRedisCmd(sync, jedisCmd, (LMoveRedisCmd) command, request, receive);
            case LMPOP:
                return JedisCallForList.cmdLMPopRedisCmd(sync, jedisCmd, (LMPopRedisCmd) command, request, receive);
            case LPOP:
                return JedisCallForList.cmdLPopRedisCmd(sync, jedisCmd, (LPopRedisCmd) command, request, receive);
            case LPOS:
                return JedisCallForList.cmdLPosRedisCmd(sync, jedisCmd, (LPosRedisCmd) command, request, receive);
            case LPUSH:
                return JedisCallForList.cmdLPushRedisCmd(sync, jedisCmd, (LPushRedisCmd) command, request, receive);
            case LPUSHX:
                return JedisCallForList.cmdLPushXRedisCmd(sync, jedisCmd, (LPushXRedisCmd) command, request, receive);
            case LRANGE:
                return JedisCallForList.cmdLRangeRedisCmd(sync, jedisCmd, (LRangeRedisCmd) command, request, receive);
            case LREM:
                return JedisCallForList.cmdLRemRedisCmd(sync, jedisCmd, (LRemRedisCmd) command, request, receive);
            case LSET:
                return JedisCallForList.cmdLSetRedisCmd(sync, jedisCmd, (LSetRedisCmd) command, request, receive);
            case LTRIM:
                return JedisCallForList.cmdLTrimRedisCmd(sync, jedisCmd, (LTrimRedisCmd) command, request, receive);
            case RPOP:
                return JedisCallForList.cmdRPopRedisCmd(sync, jedisCmd, (RPopRedisCmd) command, request, receive);
            case RPOPLPUSH:
                return JedisCallForList.cmdRPopLPushRedisCmd(sync, jedisCmd, (RPopLPushRedisCmd) command, request, receive);
            case RPUSH:
                return JedisCallForList.cmdRPushRedisCmd(sync, jedisCmd, (RPushRedisCmd) command, request, receive);
            case RPUSHX:
                return JedisCallForList.cmdRPushXRedisCmd(sync, jedisCmd, (RPushXRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ Set Commands */
            case SADD:
                return JedisCallForSet.cmdSAddRedisCmd(sync, jedisCmd, (SAddRedisCmd) command, request, receive);
            case SCARD:
                return JedisCallForSet.cmdSCardRedisCmd(sync, jedisCmd, (SCardRedisCmd) command, request, receive);
            case SDIFF:
                return JedisCallForSet.cmdSDiffRedisCmd(sync, jedisCmd, (SDiffRedisCmd) command, request, receive);
            case SDIFFSTORE:
                return JedisCallForSet.cmdSDiffStoreRedisCmd(sync, jedisCmd, (SDiffStoreRedisCmd) command, request, receive);
            case SINTER:
                return JedisCallForSet.cmdSInterRedisCmd(sync, jedisCmd, (SInterRedisCmd) command, request, receive);
            case SINTERCARD:
                return JedisCallForSet.cmdSInterCardRedisCmd(sync, jedisCmd, (SInterCardRedisCmd) command, request, receive);
            case SINTERSTORE:
                return JedisCallForSet.cmdSinterStoreRedisCmd(sync, jedisCmd, (SinterStoreRedisCmd) command, request, receive);
            case SISMEMBER:
                return JedisCallForSet.cmdSISMemberRedisCmd(sync, jedisCmd, (SISMemberRedisCmd) command, request, receive);
            case SMEMBERS:
                return JedisCallForSet.cmdSMembersRedisCmd(sync, jedisCmd, (SMembersRedisCmd) command, request, receive);
            case SMISMEMBER:
                return JedisCallForSet.cmdSMISMemberRedisCmd(sync, jedisCmd, (SMISMemberRedisCmd) command, request, receive);
            case SMOVE:
                return JedisCallForSet.cmdSMoveRedisCmd(sync, jedisCmd, (SMoveRedisCmd) command, request, receive);
            case SPOP:
                return JedisCallForSet.cmdSPopRedisCmd(sync, jedisCmd, (SPopRedisCmd) command, request, receive);
            case SRANDMEMBER:
                return JedisCallForSet.cmdSRandMemberRedisCmd(sync, jedisCmd, (SRandMemberRedisCmd) command, request, receive);
            case SREM:
                return JedisCallForSet.cmdSRemRedisCmd(sync, jedisCmd, (SRemRedisCmd) command, request, receive);
            case SSCAN:
                return JedisCallForSet.cmdSSCanRedisCmd(sync, jedisCmd, (SSCanRedisCmd) command, request, receive);
            case SUNION:
                return JedisCallForSet.cmdSUnionRedisCmd(sync, jedisCmd, (SUnionRedisCmd) command, request, receive);
            case SUNIONSTORE:
                return JedisCallForSet.cmdSUnionStoreRedisCmd(sync, jedisCmd, (SUnionStoreRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ Sorted Set Commands */
            case BZMPOP:
                return JedisCallForSortedSet.cmdBZMPopRedisCmd(sync, jedisCmd, (BZMPopRedisCmd) command, request, receive);
            case BZPOPMAX:
                return JedisCallForSortedSet.cmdBZPopMaxRedisCmd(sync, jedisCmd, (BZPopMaxRedisCmd) command, request, receive);
            case BZPOPMIN:
                return JedisCallForSortedSet.cmdBZPopMinRedisCmd(sync, jedisCmd, (BZPopMinRedisCmd) command, request, receive);
            case ZADD:
                return JedisCallForSortedSet.cmdZAddRedisCmd(sync, jedisCmd, (ZAddRedisCmd) command, request, receive);
            case ZCARD:
                return JedisCallForSortedSet.cmdZCardRedisCmd(sync, jedisCmd, (ZCardRedisCmd) command, request, receive);
            case ZCOUNT:
                return JedisCallForSortedSet.cmdZCountRedisCmd(sync, jedisCmd, (ZCountRedisCmd) command, request, receive);
            case ZDIFF:
                return JedisCallForSortedSet.cmdZDiffRedisCmd(sync, jedisCmd, (ZDiffRedisCmd) command, request, receive);
            case ZDIFFSTORE:
                return JedisCallForSortedSet.cmdZDiffStoreRedisCmd(sync, jedisCmd, (ZDiffStoreRedisCmd) command, request, receive);
            case ZINCRBY:
                return JedisCallForSortedSet.cmdZIncrByRedisCmd(sync, jedisCmd, (ZIncrByRedisCmd) command, request, receive);
            case ZINTER:
                return JedisCallForSortedSet.cmdZInterRedisCmd(sync, jedisCmd, (ZInterRedisCmd) command, request, receive);
            case ZINTERCARD:
                return JedisCallForSortedSet.cmdZInterCardRedisCmd(sync, jedisCmd, (ZInterCardRedisCmd) command, request, receive);
            case ZINTERSTORE:
                return JedisCallForSortedSet.cmdZInterStoreRedisCmd(sync, jedisCmd, (ZInterStoreRedisCmd) command, request, receive);
            case ZLEXCOUNT:
                return JedisCallForSortedSet.cmdZLexCountRedisCmd(sync, jedisCmd, (ZLexCountRedisCmd) command, request, receive);
            case ZMPOP:
                return JedisCallForSortedSet.cmdZMPopRedisCmd(sync, jedisCmd, (ZMPopRedisCmd) command, request, receive);
            case ZMSCORE:
                return JedisCallForSortedSet.cmdZMSCoreRedisCmd(sync, jedisCmd, (ZMScoreRedisCmd) command, request, receive);
            case ZPOPMAX:
                return JedisCallForSortedSet.cmdZPopMaxRedisCmd(sync, jedisCmd, (ZPopMaxRedisCmd) command, request, receive);
            case ZPOPMIN:
                return JedisCallForSortedSet.cmdZPopMinRedisCmd(sync, jedisCmd, (ZPopMinRedisCmd) command, request, receive);
            case ZRANDMEMBER:
                return JedisCallForSortedSet.cmdZRAndMemberRedisCmd(sync, jedisCmd, (ZRAndMemberRedisCmd) command, request, receive);
            case ZRANGE:
                return JedisCallForSortedSet.cmdZRangeRedisCmd(sync, jedisCmd, (ZRangeRedisCmd) command, request, receive);
            case ZRANGEBYLEX:
                return JedisCallForSortedSet.cmdZRangeByLexRedisCmd(sync, jedisCmd, (ZRangeByLexRedisCmd) command, request, receive);
            case ZRANGEBYSCORE:
                return JedisCallForSortedSet.cmdZRangeByScoreRedisCmd(sync, jedisCmd, (ZRangeByScoreRedisCmd) command, request, receive);
            case ZRANGESTORE:
                return JedisCallForSortedSet.cmdZRangeStoreRedisCmd(sync, jedisCmd, (ZRangeStoreRedisCmd) command, request, receive);
            case ZRANK:
                return JedisCallForSortedSet.cmdZRankRedisCmd(sync, jedisCmd, (ZRankRedisCmd) command, request, receive);
            case ZREM:
                return JedisCallForSortedSet.cmdZRemRedisCmd(sync, jedisCmd, (ZRemRedisCmd) command, request, receive);
            case ZREMRANGEBYLEX:
                return JedisCallForSortedSet.cmdZRemRangeByLexRedisCmd(sync, jedisCmd, (ZRemRangeByLexRedisCmd) command, request, receive);
            case ZREMRANGEBYRANK:
                return JedisCallForSortedSet.cmdZRemRangeByRankRedisCmd(sync, jedisCmd, (ZRemRangeByRankRedisCmd) command, request, receive);
            case ZREMRANGEBYSCORE:
                return JedisCallForSortedSet.cmdZRemRangeByScoreRedisCmd(sync, jedisCmd, (ZRemRangeByScoreRedisCmd) command, request, receive);
            case ZREVRANGE:
                return JedisCallForSortedSet.cmdZRevRangeRedisCmd(sync, jedisCmd, (ZRevRangeRedisCmd) command, request, receive);
            case ZREVRANGEBYLEX:
                return JedisCallForSortedSet.cmdZRevRangeByLexRedisCmd(sync, jedisCmd, (ZRevRangeByLexRedisCmd) command, request, receive);
            case ZREVRANGEBYSCORE:
                return JedisCallForSortedSet.cmdZRevRangeByScoreRedisCmd(sync, jedisCmd, (ZRevRangeByScoreRedisCmd) command, request, receive);
            case ZREVRANK:
                return JedisCallForSortedSet.cmdZRevRankRedisCmd(sync, jedisCmd, (ZRevRankRedisCmd) command, request, receive);
            case ZSCAN:
                return JedisCallForSortedSet.cmdZScanRedisCmd(sync, jedisCmd, (ZSCanRedisCmd) command, request, receive);
            case ZSCORE:
                return JedisCallForSortedSet.cmdZScoreRedisCmd(sync, jedisCmd, (ZScoreRedisCmd) command, request, receive);
            case ZUNION:
                return JedisCallForSortedSet.cmdZUnionRedisCmd(sync, jedisCmd, (ZUnionRedisCmd) command, request, receive);
            case ZUNIONSTORE:
                return JedisCallForSortedSet.cmdZUnionStoreRedisCmd(sync, jedisCmd, (ZUnionStoreRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ Key Commands */
            case DEL:
                return JedisCallForKeys.cmdDelRedisCmd(sync, jedisCmd, (DelRedisCmd) command, request, receive);
            case DUMP:
                return JedisCallForKeys.cmdDumpRedisCmd(sync, jedisCmd, (DumpRedisCmd) command, request, receive);
            case EXISTS:
                return JedisCallForKeys.cmdExistsRedisCmd(sync, jedisCmd, (ExistsRedisCmd) command, request, receive);
            case EXPIRE:
                return JedisCallForKeys.cmdExpireRedisCmd(sync, jedisCmd, (ExpireRedisCmd) command, request, receive);
            case EXPIREAT:
                return JedisCallForKeys.cmdExpireAtRedisCmd(sync, jedisCmd, (ExpireAtRedisCmd) command, request, receive);
            case EXPIRETIME:
                return JedisCallForKeys.cmdExpireTimeRedisCmd(sync, jedisCmd, (ExpireTimeRedisCmd) command, request, receive);
            case KEYS:
                return JedisCallForKeys.cmdKeysRedisCmd(sync, jedisCmd, (KeysRedisCmd) command, request, receive);
            case MOVE:
                return JedisCallForKeys.cmdMoveRedisCmd(sync, jedisCmd, (MoveRedisCmd) command, request, receive);
            case OBJECT:
                return JedisCallForKeys.cmdObjectRedisCmd(sync, jedisCmd, (ObjectRedisCmd) command, request, receive);
            case PERSIST:
                return JedisCallForKeys.cmdPersistRedisCmd(sync, jedisCmd, (PersistRedisCmd) command, request, receive);
            case PEXPIRE:
                return JedisCallForKeys.cmdPExpireRedisCmd(sync, jedisCmd, (PExpireRedisCmd) command, request, receive);
            case PEXPIREAT:
                return JedisCallForKeys.cmdPExpireAtRedisCmd(sync, jedisCmd, (PExpireAtRedisCmd) command, request, receive);
            case PEXPIRETIME:
                return JedisCallForKeys.cmdPExpireTimeRedisCmd(sync, jedisCmd, (PExpireTimeRedisCmd) command, request, receive);
            case PTTL:
                return JedisCallForKeys.cmdPTTLRedisCmd(sync, jedisCmd, (PTTLRedisCmd) command, request, receive);
            case TTL:
                return JedisCallForKeys.cmdTTLTimeRedisCmd(sync, jedisCmd, (TTLRedisCmd) command, request, receive);
            case RANDOMKEY:
                return JedisCallForKeys.cmdRandomKeyRedisCmd(sync, jedisCmd, (RandomKeyRedisCmd) command, request, receive);
            case RENAME:
                return JedisCallForKeys.cmdRenameRedisCmd(sync, jedisCmd, (RenameRedisCmd) command, request, receive);
            case RENAMENX:
                return JedisCallForKeys.cmdRenameNXRedisCmd(sync, jedisCmd, (RenameNXRedisCmd) command, request, receive);
            case RESTORE:
                return JedisCallForKeys.cmdRestoreRedisCmd(sync, jedisCmd, (RestoreRedisCmd) command, request, receive);
            case SCAN:
                return JedisCallForKeys.cmdScanRedisCmd(sync, jedisCmd, (ScanRedisCmd) command, request, receive);
            case SORT:
                return JedisCallForKeys.cmdSortRedisCmd(sync, jedisCmd, (SortRedisCmd) command, request, receive);
            case SORT_RO:
                return JedisCallForKeys.cmdSortRORedisCmd(sync, jedisCmd, (SortRORedisCmd) command, request, receive);
            case TOUCH:
                return JedisCallForKeys.cmdTouchRedisCmd(sync, jedisCmd, (TouchRedisCmd) command, request, receive);
            case TYPE:
                return JedisCallForKeys.cmdTypeRedisCmd(sync, jedisCmd, (TypeRedisCmd) command, request, receive);
            case UNLINK:
                return JedisCallForKeys.cmdUnlinkRedisCmd(sync, jedisCmd, (UnlinkRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ HyperLog Commands */
            case PFADD:
                return JedisCallForHyperLog.cmdPFAddRedisCmd(sync, jedisCmd, (PFAddRedisCmd) command, request, receive);
            case PFCOUNT:
                return JedisCallForHyperLog.cmdPFCountRedisCmd(sync, jedisCmd, (PFCountRedisCmd) command, request, receive);
            case PFMERGE:
                return JedisCallForHyperLog.cmdPFMergeRedisCmd(sync, jedisCmd, (PFMergeRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ DatabaseCommands */
            case COPY:
                return JedisCallForDB.cmdCopyRedisCmd(sync, jedisCmd, (CopyRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ info Commands */
            case INFO:
                return JedisCallForServer.cmdInfoRedisCmd(sync, jedisCmd, (InfoRedisCmd) command, request, receive);
            case PING:
                return JedisCallForServer.cmdPingRedisCmd(sync, jedisCmd, (PingRedisCmd) command, request, receive);
            case WAIT:
                return JedisCallForServer.cmdWaitRedisCmd(sync, jedisCmd, (WaitRedisCmd) command, request, receive);
            case WAITAOF:
                return JedisCallForServer.cmdWaitAOFRedisCmd(sync, jedisCmd, (WaitAOFRedisCmd) command, request, receive);
            case DBSIZE:
                return JedisCallForServer.cmdDbSizeRedisCmd(sync, jedisCmd, (DbSizeRedisCmd) command, request, receive);
            /* ------------------------------------------------------------------------------------------------ config commands */
            case CONFIG_GET:
                return JedisCallForConfig.cmdConfigGetCmd(sync, jedisCmd, (ConfigGetCmd) command, request, receive);
            default:
                throw new UnsupportedOperationException("redis command '" + command.getCmdType().getCommandStr() + "' Unsupported.");
        }
    }
}
