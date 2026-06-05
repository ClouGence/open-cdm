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

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.redis.parser.antlr.RedisParser;
import com.clougence.clouddm.ds.redis.parser.antlr.RedisParserBaseVisitor;
import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;

public class RedisSplitVisitor extends RedisParserBaseVisitor<SecQueryType> {

    public static final AbstractParseTreeVisitor<SecQueryType> INSTANCE = new RedisSplitVisitor();

    private SecQueryType cmdTypeToSecQueryType(RedisCmdType cmdType) {
        return RedisAnalysisHelper.cmdTypeToSecQueryType(cmdType);
    }

    public SecQueryType visitChildren(RuleNode node) {
        int n = node.getChildCount();
        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            SecQueryType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }

        return SecQueryType.UNKNOWN;
    }

    /* ----------------------------------------------------------------------------------- Keys commands */

    @Override
    public SecQueryType visitCmdCopy(RedisParser.CmdCopyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COPY);
    }

    @Override
    public SecQueryType visitCmdDel(RedisParser.CmdDelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DEL);
    }

    @Override
    public SecQueryType visitCmdDump(RedisParser.CmdDumpContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DUMP);
    }

    @Override
    public SecQueryType visitCmdExists(RedisParser.CmdExistsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXISTS);
    }

    @Override
    public SecQueryType visitCmdExpire(RedisParser.CmdExpireContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXPIRE);
    }

    @Override
    public SecQueryType visitCmdExpireat(RedisParser.CmdExpireatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXPIREAT);
    }

    @Override
    public SecQueryType visitCmdExpireTime(RedisParser.CmdExpireTimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXPIRETIME);
    }

    @Override
    public SecQueryType visitCmdKeys(RedisParser.CmdKeysContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.KEYS);
    }

    @Override
    public SecQueryType visitCmdMove(RedisParser.CmdMoveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MOVE);
    }

    @Override
    public SecQueryType visitCmdObject(RedisParser.CmdObjectContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.OBJECT);
    }

    @Override
    public SecQueryType visitCmdPersist(RedisParser.CmdPersistContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PERSIST);
    }

    @Override
    public SecQueryType visitCmdPexpire(RedisParser.CmdPexpireContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PEXPIRE);
    }

    @Override
    public SecQueryType visitCmdPexpireat(RedisParser.CmdPexpireatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PEXPIREAT);
    }

    @Override
    public SecQueryType visitCmdPExpireTime(RedisParser.CmdPExpireTimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PEXPIRETIME);
    }

    @Override
    public SecQueryType visitCmdTtl(RedisParser.CmdTtlContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.TTL);
    }

    @Override
    public SecQueryType visitCmdPttl(RedisParser.CmdPttlContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PTTL);
    }

    @Override
    public SecQueryType visitCmdRandomkey(RedisParser.CmdRandomkeyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RANDOMKEY);
    }

    @Override
    public SecQueryType visitCmdRename(RedisParser.CmdRenameContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RENAME);
    }

    @Override
    public SecQueryType visitCmdRenamenx(RedisParser.CmdRenamenxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RENAMENX);
    }

    @Override
    public SecQueryType visitCmdRestore(RedisParser.CmdRestoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RESTORE);
    }

    @Override
    public SecQueryType visitCmdScan(RedisParser.CmdScanContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCAN);
    }

    @Override
    public SecQueryType visitCmdSort(RedisParser.CmdSortContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SORT);
    }

    @Override
    public SecQueryType visitCmdSortro(RedisParser.CmdSortroContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SORT_RO);
    }

    @Override
    public SecQueryType visitCmdTouch(RedisParser.CmdTouchContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.TOUCH);
    }

    @Override
    public SecQueryType visitCmdType(RedisParser.CmdTypeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.TYPE);
    }

    @Override
    public SecQueryType visitCmdUnlink(RedisParser.CmdUnlinkContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.UNLINK);
    }

    @Override
    public SecQueryType visitCmdWait(RedisParser.CmdWaitContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.WAIT);
    }

    @Override
    public SecQueryType visitCmdWaitAOF(RedisParser.CmdWaitAOFContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.WAITAOF);
    }

    /* ----------------------------------------------------------------------------------- String commands */

    @Override
    public SecQueryType visitCmdAppend(RedisParser.CmdAppendContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.APPEND);
    }

    @Override
    public SecQueryType visitCmdDecr(RedisParser.CmdDecrContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DECR);
    }

    @Override
    public SecQueryType visitCmdDecrby(RedisParser.CmdDecrbyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DECRBY);
    }

    @Override
    public SecQueryType visitCmdGet(RedisParser.CmdGetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GET);
    }

    @Override
    public SecQueryType visitCmdGetdel(RedisParser.CmdGetdelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETDEL);
    }

    @Override
    public SecQueryType visitCmdGetex(RedisParser.CmdGetexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETEX);
    }

    @Override
    public SecQueryType visitCmdGetrange(RedisParser.CmdGetrangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETRANGE);
    }

    @Override
    public SecQueryType visitCmdGetset(RedisParser.CmdGetsetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETSET);
    }

    @Override
    public SecQueryType visitCmdIncr(RedisParser.CmdIncrContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.INCR);
    }

    @Override
    public SecQueryType visitCmdIncrby(RedisParser.CmdIncrbyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.INCRBY);
    }

    @Override
    public SecQueryType visitCmdIncrbyFloat(RedisParser.CmdIncrbyFloatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.INCRBYFLOAT);
    }

    @Override
    public SecQueryType visitCmdLcs(RedisParser.CmdLcsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LCS);
    }

    @Override
    public SecQueryType visitCmdMget(RedisParser.CmdMgetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MGET);
    }

    @Override
    public SecQueryType visitCmdMset(RedisParser.CmdMsetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MSET);
    }

    @Override
    public SecQueryType visitCmdMsetnx(RedisParser.CmdMsetnxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MSETNX);
    }

    @Override
    public SecQueryType visitCmdSetex(RedisParser.CmdSetexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SETEX);
    }

    @Override
    public SecQueryType visitCmdPSetex(RedisParser.CmdPSetexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PSETEX);
    }

    @Override
    public SecQueryType visitCmdSet(RedisParser.CmdSetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SET);
    }

    @Override
    public SecQueryType visitCmdSetnx(RedisParser.CmdSetnxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SETNX);
    }

    @Override
    public SecQueryType visitCmdSetrange(RedisParser.CmdSetrangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SETRANGE);
    }

    @Override
    public SecQueryType visitCmdStrlen(RedisParser.CmdStrlenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.STRLEN);
    }

    @Override
    public SecQueryType visitCmdSubstr(RedisParser.CmdSubstrContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUBSTR);
    }

    /* ----------------------------------------------------------------------------------- Bit commands */

    @Override
    public SecQueryType visitCmdBitCount(RedisParser.CmdBitCountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITCOUNT);
    }

    @Override
    public SecQueryType visitCmdBitField(RedisParser.CmdBitFieldContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITFIELD);
    }

    @Override
    public SecQueryType visitCmdBitFieldRO(RedisParser.CmdBitFieldROContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITFIELD_RO);
    }

    @Override
    public SecQueryType visitCmdBitOP(RedisParser.CmdBitOPContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITOP);
    }

    @Override
    public SecQueryType visitCmdBitPos(RedisParser.CmdBitPosContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITPOS);
    }

    @Override
    public SecQueryType visitCmdGetbit(RedisParser.CmdGetbitContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETBIT);
    }

    @Override
    public SecQueryType visitCmdSetbit(RedisParser.CmdSetbitContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SETBIT);
    }

    /* ----------------------------------------------------------------------------------- Hash commands */

    @Override
    public SecQueryType visitCmdHdel(RedisParser.CmdHdelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HDEL);
    }

    @Override
    public SecQueryType visitCmdHexists(RedisParser.CmdHexistsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HEXISTS);
    }

    @Override
    public SecQueryType visitCmdHexpire(RedisParser.CmdHexpireContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HEXPIRE);
    }

    @Override
    public SecQueryType visitCmdHexpireat(RedisParser.CmdHexpireatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HEXPIREAT);
    }

    @Override
    public SecQueryType visitCmdHexpiretime(RedisParser.CmdHexpiretimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HEXPIRETIME);
    }

    @Override
    public SecQueryType visitCmdHGet(RedisParser.CmdHGetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HGET);
    }

    @Override
    public SecQueryType visitCmdHGetAll(RedisParser.CmdHGetAllContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HGETALL);
    }

    @Override
    public SecQueryType visitCmdHgetDel(RedisParser.CmdHgetDelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HGETDEL);
    }

    @Override
    public SecQueryType visitCmdHgetex(RedisParser.CmdHgetexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HGETEX);
    }

    @Override
    public SecQueryType visitCmdHincrBy(RedisParser.CmdHincrByContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HINCRBY);
    }

    @Override
    public SecQueryType visitCmdHincrByFloat(RedisParser.CmdHincrByFloatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HINCRBYFLOAT);
    }

    @Override
    public SecQueryType visitCmdHKeys(RedisParser.CmdHKeysContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HKEYS);
    }

    @Override
    public SecQueryType visitCmdHLen(RedisParser.CmdHLenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HLEN);
    }

    @Override
    public SecQueryType visitCmdHMget(RedisParser.CmdHMgetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HMGET);
    }

    @Override
    public SecQueryType visitCmdHMset(RedisParser.CmdHMsetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HMSET);
    }

    @Override
    public SecQueryType visitCmdHPersist(RedisParser.CmdHPersistContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPERSIST);
    }

    @Override
    public SecQueryType visitCmdHPexpire(RedisParser.CmdHPexpireContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPEXPIRE);
    }

    @Override
    public SecQueryType visitCmdHPexpireAt(RedisParser.CmdHPexpireAtContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPEXPIREAT);
    }

    @Override
    public SecQueryType visitCmdHPexpireTime(RedisParser.CmdHPexpireTimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPEXPIRETIME);
    }

    @Override
    public SecQueryType visitCmdHPTTL(RedisParser.CmdHPTTLContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPTTL);
    }

    @Override
    public SecQueryType visitCmdHTTL(RedisParser.CmdHTTLContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HTTL);
    }

    @Override
    public SecQueryType visitCmdHrandfield(RedisParser.CmdHrandfieldContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HRANDFIELD);
    }

    @Override
    public SecQueryType visitCmdHscan(RedisParser.CmdHscanContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSCAN);
    }

    @Override
    public SecQueryType visitCmdHSet(RedisParser.CmdHSetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSET);
    }

    @Override
    public SecQueryType visitCmdHSetex(RedisParser.CmdHSetexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSETEX);
    }

    @Override
    public SecQueryType visitCmdHsetnx(RedisParser.CmdHsetnxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSETNX);
    }

    @Override
    public SecQueryType visitCmdHStrLen(RedisParser.CmdHStrLenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSTRLEN);
    }

    @Override
    public SecQueryType visitCmdHVals(RedisParser.CmdHValsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HVALS);
    }

    /* ----------------------------------------------------------------------------------- List commands */

    @Override
    public SecQueryType visitCmdBlmove(RedisParser.CmdBlmoveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BLMOVE);
    }

    @Override
    public SecQueryType visitCmdBLmpop(RedisParser.CmdBLmpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BLMPOP);
    }

    @Override
    public SecQueryType visitCmdBLPop(RedisParser.CmdBLPopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BLPOP);
    }

    @Override
    public SecQueryType visitCmdBRPop(RedisParser.CmdBRPopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BRPOP);
    }

    @Override
    public SecQueryType visitCmdBrpoplpush(RedisParser.CmdBrpoplpushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BRPOPLPUSH);
    }

    @Override
    public SecQueryType visitCmdLindex(RedisParser.CmdLindexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LINDEX);
    }

    @Override
    public SecQueryType visitCmdLinsert(RedisParser.CmdLinsertContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LINSERT);
    }

    @Override
    public SecQueryType visitCmdLlen(RedisParser.CmdLlenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LLEN);
    }

    @Override
    public SecQueryType visitCmdLmove(RedisParser.CmdLmoveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LMOVE);
    }

    @Override
    public SecQueryType visitCmdLmpop(RedisParser.CmdLmpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LMPOP);
    }

    @Override
    public SecQueryType visitCmdLPop(RedisParser.CmdLPopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LPOP);
    }

    @Override
    public SecQueryType visitCmdLpos(RedisParser.CmdLposContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LPOS);
    }

    @Override
    public SecQueryType visitCmdLPush(RedisParser.CmdLPushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LPUSH);
    }

    @Override
    public SecQueryType visitCmdLPushx(RedisParser.CmdLPushxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LPUSHX);
    }

    @Override
    public SecQueryType visitCmdLRange(RedisParser.CmdLRangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LRANGE);
    }

    @Override
    public SecQueryType visitCmdLRem(RedisParser.CmdLRemContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LREM);
    }

    @Override
    public SecQueryType visitCmdLSet(RedisParser.CmdLSetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LSET);
    }

    @Override
    public SecQueryType visitCmdLTrim(RedisParser.CmdLTrimContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LTRIM);
    }

    @Override
    public SecQueryType visitCmdRPop(RedisParser.CmdRPopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RPOP);
    }

    @Override
    public SecQueryType visitCmdRpoplpush(RedisParser.CmdRpoplpushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RPOPLPUSH);
    }

    @Override
    public SecQueryType visitCmdRPush(RedisParser.CmdRPushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RPUSH);
    }

    @Override
    public SecQueryType visitCmdRPushx(RedisParser.CmdRPushxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RPUSHX);
    }

    /* ----------------------------------------------------------------------------------- Set commands */

    @Override
    public SecQueryType visitCmdSadd(RedisParser.CmdSaddContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SADD);
    }

    @Override
    public SecQueryType visitCmdScard(RedisParser.CmdScardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCARD);
    }

    @Override
    public SecQueryType visitCmdSdiff(RedisParser.CmdSdiffContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SDIFF);
    }

    @Override
    public SecQueryType visitCmdSdiffstore(RedisParser.CmdSdiffstoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SDIFFSTORE);
    }

    @Override
    public SecQueryType visitCmdSinter(RedisParser.CmdSinterContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SINTER);
    }

    @Override
    public SecQueryType visitCmdSinterCard(RedisParser.CmdSinterCardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SINTERCARD);
    }

    @Override
    public SecQueryType visitCmdSinterStore(RedisParser.CmdSinterStoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SINTERSTORE);
    }

    @Override
    public SecQueryType visitCmdSismember(RedisParser.CmdSismemberContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SISMEMBER);
    }

    @Override
    public SecQueryType visitCmdSmembers(RedisParser.CmdSmembersContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SMEMBERS);
    }

    @Override
    public SecQueryType visitCmdSmismember(RedisParser.CmdSmismemberContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SMISMEMBER);
    }

    @Override
    public SecQueryType visitCmdSmove(RedisParser.CmdSmoveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SMOVE);
    }

    @Override
    public SecQueryType visitCmdSpop(RedisParser.CmdSpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SPOP);
    }

    @Override
    public SecQueryType visitCmdSrandmember(RedisParser.CmdSrandmemberContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SRANDMEMBER);
    }

    @Override
    public SecQueryType visitCmdSrem(RedisParser.CmdSremContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SREM);
    }

    @Override
    public SecQueryType visitCmdSscan(RedisParser.CmdSscanContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SSCAN);
    }

    @Override
    public SecQueryType visitCmdSunion(RedisParser.CmdSunionContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUNION);
    }

    @Override
    public SecQueryType visitCmdSunionstore(RedisParser.CmdSunionstoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUNIONSTORE);
    }

    /* ----------------------------------------------------------------------------------- sorted set commands */

    @Override
    public SecQueryType visitCmdBzmpop(RedisParser.CmdBzmpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BZMPOP);
    }

    @Override
    public SecQueryType visitCmdBzpopmax(RedisParser.CmdBzpopmaxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BZPOPMAX);
    }

    @Override
    public SecQueryType visitCmdBzpopmin(RedisParser.CmdBzpopminContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BZPOPMIN);
    }

    @Override
    public SecQueryType visitCmdZadd(RedisParser.CmdZaddContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZADD);
    }

    @Override
    public SecQueryType visitCmdZcard(RedisParser.CmdZcardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZCARD);
    }

    @Override
    public SecQueryType visitCmdZcount(RedisParser.CmdZcountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZCOUNT);
    }

    @Override
    public SecQueryType visitCmdZdiff(RedisParser.CmdZdiffContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZDIFF);
    }

    @Override
    public SecQueryType visitCmdZdiffStore(RedisParser.CmdZdiffStoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZDIFFSTORE);
    }

    @Override
    public SecQueryType visitCmdZincrby(RedisParser.CmdZincrbyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZINCRBY);
    }

    @Override
    public SecQueryType visitCmdZinter(RedisParser.CmdZinterContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZINTER);
    }

    @Override
    public SecQueryType visitCmdZintercard(RedisParser.CmdZintercardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZINTERCARD);
    }

    @Override
    public SecQueryType visitCmdZinterstore(RedisParser.CmdZinterstoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZINTERSTORE);
    }

    @Override
    public SecQueryType visitCmdZLexCount(RedisParser.CmdZLexCountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZLEXCOUNT);
    }

    @Override
    public SecQueryType visitCmdZmpop(RedisParser.CmdZmpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZMPOP);
    }

    @Override
    public SecQueryType visitCmdZmscore(RedisParser.CmdZmscoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZMSCORE);
    }

    @Override
    public SecQueryType visitCmdZpopmax(RedisParser.CmdZpopmaxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZPOPMAX);
    }

    @Override
    public SecQueryType visitCmdZpopmin(RedisParser.CmdZpopminContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZPOPMIN);
    }

    @Override
    public SecQueryType visitCmdZrandmember(RedisParser.CmdZrandmemberContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANDMEMBER);
    }

    @Override
    public SecQueryType visitCmdZrange(RedisParser.CmdZrangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANGE);
    }

    @Override
    public SecQueryType visitCmdZrangebylex(RedisParser.CmdZrangebylexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANGEBYLEX);
    }

    @Override
    public SecQueryType visitCmdZrangebyscore(RedisParser.CmdZrangebyscoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANGEBYSCORE);
    }

    @Override
    public SecQueryType visitCmdZrangestore(RedisParser.CmdZrangestoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANGESTORE);
    }

    @Override
    public SecQueryType visitCmdZrank(RedisParser.CmdZrankContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANK);
    }

    @Override
    public SecQueryType visitCmdZRem(RedisParser.CmdZRemContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREM);
    }

    @Override
    public SecQueryType visitCmdZRemRangeByLex(RedisParser.CmdZRemRangeByLexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREMRANGEBYLEX);
    }

    @Override
    public SecQueryType visitCmdZremrangebyrank(RedisParser.CmdZremrangebyrankContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREMRANGEBYRANK);
    }

    @Override
    public SecQueryType visitCmdZRemRangeByScore(RedisParser.CmdZRemRangeByScoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREMRANGEBYSCORE);
    }

    @Override
    public SecQueryType visitCmdZrevrange(RedisParser.CmdZrevrangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREVRANGE);
    }

    @Override
    public SecQueryType visitCmdZrevrangebylex(RedisParser.CmdZrevrangebylexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREVRANGEBYLEX);
    }

    @Override
    public SecQueryType visitCmdZrevrangebyscore(RedisParser.CmdZrevrangebyscoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREVRANGEBYSCORE);
    }

    @Override
    public SecQueryType visitCmdZrevrank(RedisParser.CmdZrevrankContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREVRANK);
    }

    @Override
    public SecQueryType visitCmdZscan(RedisParser.CmdZscanContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZSCAN);
    }

    @Override
    public SecQueryType visitCmdZscore(RedisParser.CmdZscoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZSCORE);
    }

    @Override
    public SecQueryType visitCmdZunion(RedisParser.CmdZunionContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZUNION);
    }

    @Override
    public SecQueryType visitCmdZunionstore(RedisParser.CmdZunionstoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZUNIONSTORE);
    }

    /* ----------------------------------------------------------------------------------- script commands */

    @Override
    public SecQueryType visitCmdScriptDebug(RedisParser.CmdScriptDebugContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_DEBUG);
    }

    @Override
    public SecQueryType visitCmdScriptExists(RedisParser.CmdScriptExistsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_EXISTS);
    }

    @Override
    public SecQueryType visitCmdScriptFlush(RedisParser.CmdScriptFlushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_FLUSH);
    }

    @Override
    public SecQueryType visitCmdScriptKill(RedisParser.CmdScriptKillContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_KILL);
    }

    @Override
    public SecQueryType visitCmdScriptLoad(RedisParser.CmdScriptLoadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_LOAD);
    }

    @Override
    public SecQueryType visitCmdEval(RedisParser.CmdEvalContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EVAL);
    }

    @Override
    public SecQueryType visitCmdEvalRO(RedisParser.CmdEvalROContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EVAL_RO);
    }

    @Override
    public SecQueryType visitCmdEvalsha(RedisParser.CmdEvalshaContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EVALSHA);
    }

    @Override
    public SecQueryType visitCmdEvalshaRO(RedisParser.CmdEvalshaROContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EVALSHA_RO);
    }

    @Override
    public SecQueryType visitCmdFCall(RedisParser.CmdFCallContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FCALL);
    }

    @Override
    public SecQueryType visitCmdFCallRO(RedisParser.CmdFCallROContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FCALL_RO);
    }

    @Override
    public SecQueryType visitCmdFunctionDel(RedisParser.CmdFunctionDelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_DEL);
    }

    @Override
    public SecQueryType visitCmdFunctionDump(RedisParser.CmdFunctionDumpContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_DUMP);
    }

    @Override
    public SecQueryType visitCmdFunctionFlush(RedisParser.CmdFunctionFlushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_FLUSH);
    }

    @Override
    public SecQueryType visitCmdFunctionKill(RedisParser.CmdFunctionKillContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_KILL);
    }

    @Override
    public SecQueryType visitCmdFunctionList(RedisParser.CmdFunctionListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_LIST);
    }

    @Override
    public SecQueryType visitCmdFunctionLoad(RedisParser.CmdFunctionLoadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_LOAD);
    }

    @Override
    public SecQueryType visitCmdFunctionRestore(RedisParser.CmdFunctionRestoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_RESTORE);
    }

    @Override
    public SecQueryType visitCmdFunctionStats(RedisParser.CmdFunctionStatsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_STATS);
    }

    /* ----------------------------------------------------------------------------------- tx commands */

    @Override
    public SecQueryType visitCmdDiscard(RedisParser.CmdDiscardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DISCARD);
    }

    @Override
    public SecQueryType visitCmdExec(RedisParser.CmdExecContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXEC);
    }

    @Override
    public SecQueryType visitCmdMulti(RedisParser.CmdMultiContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MULTI);
    }

    @Override
    public SecQueryType visitCmdUnwatch(RedisParser.CmdUnwatchContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.UNWATCH);
    }

    @Override
    public SecQueryType visitCmdWatch(RedisParser.CmdWatchContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.WATCH);
    }

    /* ----------------------------------------------------------------------------------- HyperLog commands */

    @Override
    public SecQueryType visitCmdPFAdd(RedisParser.CmdPFAddContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PFADD);
    }

    @Override
    public SecQueryType visitCmdPFCount(RedisParser.CmdPFCountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PFCOUNT);
    }

    @Override
    public SecQueryType visitCmdPFMerge(RedisParser.CmdPFMergeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PFMERGE);
    }

    /* ----------------------------------------------------------------------------------- publish commands */

    @Override
    public SecQueryType visitCmdPSubscribe(RedisParser.CmdPSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PSUBSCRIBE);
    }

    @Override
    public SecQueryType visitCmdPublish(RedisParser.CmdPublishContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBLISH);
    }

    @Override
    public SecQueryType visitCmdPubSubChannels(RedisParser.CmdPubSubChannelsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_CHANNELS);
    }

    @Override
    public SecQueryType visitCmdPubSubNumPat(RedisParser.CmdPubSubNumPatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_NUMPAT);
    }

    @Override
    public SecQueryType visitCmdPubSubNumSub(RedisParser.CmdPubSubNumSubContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_NUMSUB);
    }

    @Override
    public SecQueryType visitCmdPubSubShardChannels(RedisParser.CmdPubSubShardChannelsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_SHARDCHANNELS);
    }

    @Override
    public SecQueryType visitCmdPubSubShardNumSub(RedisParser.CmdPubSubShardNumSubContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_SHARDNUMSUB);
    }

    @Override
    public SecQueryType visitCmdPunSubscribe(RedisParser.CmdPunSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUNSUBSCRIBE);
    }

    @Override
    public SecQueryType visitCmdSpublish(RedisParser.CmdSpublishContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SPUBLISH);
    }

    @Override
    public SecQueryType visitCmdSSubscribe(RedisParser.CmdSSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SSUBSCRIBE);
    }

    @Override
    public SecQueryType visitCmdSubscribe(RedisParser.CmdSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUBSCRIBE);
    }

    @Override
    public SecQueryType visitCmdSunSubscribe(RedisParser.CmdSunSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUNSUBSCRIBE);
    }

    @Override
    public SecQueryType visitCmdUnSubScribe(RedisParser.CmdUnSubScribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.UNSUBSCRIBE);
    }

    /* ----------------------------------------------------------------------------------- cluster commands */

    @Override
    public SecQueryType visitCmdAsking(RedisParser.CmdAskingContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ASKING);
    }

    @Override
    public SecQueryType visitCmdReadonly(RedisParser.CmdReadonlyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.READONLY);
    }

    @Override
    public SecQueryType visitCmdReadWrite(RedisParser.CmdReadWriteContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.READWRITE);
    }

    @Override
    public SecQueryType visitCmdClusterAddSlots(RedisParser.CmdClusterAddSlotsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_ADDSLOTS);
    }

    @Override
    public SecQueryType visitCmdClusterDelSlots(RedisParser.CmdClusterDelSlotsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_DELSLOTS);
    }

    @Override
    public SecQueryType visitCmdClusterAddSlotsRange(RedisParser.CmdClusterAddSlotsRangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_ADDSLOTSRANGE);
    }

    @Override
    public SecQueryType visitCmdClusterDelSlotsRange(RedisParser.CmdClusterDelSlotsRangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_DELSLOTSRANGE);
    }

    @Override
    public SecQueryType visitCmdClusterBumpEpoch(RedisParser.CmdClusterBumpEpochContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_BUMPEPOCH);
    }

    @Override
    public SecQueryType visitCmdClusterCountFailureReports(RedisParser.CmdClusterCountFailureReportsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_COUNT_FAILURE_REPORTS);
    }

    @Override
    public SecQueryType visitCmdClusterCountKeysInSlot(RedisParser.CmdClusterCountKeysInSlotContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_COUNTKEYSINSLOT);
    }

    @Override
    public SecQueryType visitCmdClusterFailOver(RedisParser.CmdClusterFailOverContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_FAILOVER);
    }

    @Override
    public SecQueryType visitCmdClusterFlushSlots(RedisParser.CmdClusterFlushSlotsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_FLUSHSLOTS);
    }

    @Override
    public SecQueryType visitCmdClusterForget(RedisParser.CmdClusterForgetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_FORGET);
    }

    @Override
    public SecQueryType visitCmdClusterGetKeysInSlot(RedisParser.CmdClusterGetKeysInSlotContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_GETKEYSINSLOT);
    }

    @Override
    public SecQueryType visitCmdClusterInfo(RedisParser.CmdClusterInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_INFO);
    }

    @Override
    public SecQueryType visitCmdClusterKeySlot(RedisParser.CmdClusterKeySlotContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_KEYSLOT);
    }

    @Override
    public SecQueryType visitCmdClusterLinks(RedisParser.CmdClusterLinksContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_LINKS);
    }

    @Override
    public SecQueryType visitCmdClusterMeet(RedisParser.CmdClusterMeetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_MEET);
    }

    @Override
    public SecQueryType visitCmdClusterMyId(RedisParser.CmdClusterMyIdContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_MYID);
    }

    @Override
    public SecQueryType visitCmdClusterMyShardId(RedisParser.CmdClusterMyShardIdContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_MYSHARDID);
    }

    @Override
    public SecQueryType visitCmdClusterNodes(RedisParser.CmdClusterNodesContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_NODES);
    }

    @Override
    public SecQueryType visitCmdClusterReplicas(RedisParser.CmdClusterReplicasContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_REPLICAS);
    }

    @Override
    public SecQueryType visitCmdClusterReplicate(RedisParser.CmdClusterReplicateContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_REPLICATE);
    }

    @Override
    public SecQueryType visitCmdClusterReset(RedisParser.CmdClusterResetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_RESET);
    }

    @Override
    public SecQueryType visitCmdClusterSaveConfig(RedisParser.CmdClusterSaveConfigContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SAVECONFIG);
    }

    @Override
    public SecQueryType visitCmdClusterSetConfigEpoch(RedisParser.CmdClusterSetConfigEpochContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SET_CONFIG_EPOCH);
    }

    @Override
    public SecQueryType visitCmdClusterSetSlot(RedisParser.CmdClusterSetSlotContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SETSLOT);
    }

    @Override
    public SecQueryType visitCmdClusterShards(RedisParser.CmdClusterShardsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SHARDS);
    }

    @Override
    public SecQueryType visitCmdClusterSlaves(RedisParser.CmdClusterSlavesContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SLAVES);
    }

    @Override
    public SecQueryType visitCmdClusterSlotStats(RedisParser.CmdClusterSlotStatsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SLOT_STATS);
    }

    @Override
    public SecQueryType visitCmdClusterSlots(RedisParser.CmdClusterSlotsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SLOTS);
    }

    /* ----------------------------------------------------------------------------------- info commands */

    @Override
    public SecQueryType visitCmdInfo(RedisParser.CmdInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.INFO);
    }

    /* ----------------------------------------------------------------------------------- acl commands */

    @Override
    public SecQueryType visitCmdAclCat(RedisParser.CmdAclCatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_CAT);
    }

    @Override
    public SecQueryType visitCmdAclDelUser(RedisParser.CmdAclDelUserContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_DELUSER);
    }

    @Override
    public SecQueryType visitCmdAclDryRun(RedisParser.CmdAclDryRunContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_DRYRUN);
    }

    @Override
    public SecQueryType visitCmdAclGenPass(RedisParser.CmdAclGenPassContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_GENPASS);
    }

    @Override
    public SecQueryType visitCmdAclGetUser(RedisParser.CmdAclGetUserContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_GETUSER);
    }

    @Override
    public SecQueryType visitCmdAclList(RedisParser.CmdAclListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_LIST);
    }

    @Override
    public SecQueryType visitCmdAclLoad(RedisParser.CmdAclLoadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_LOAD);
    }

    @Override
    public SecQueryType visitCmdAclLog(RedisParser.CmdAclLogContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_LOG);
    }

    @Override
    public SecQueryType visitCmdAclSave(RedisParser.CmdAclSaveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_SAVE);
    }

    @Override
    public SecQueryType visitCmdAclSetUser(RedisParser.CmdAclSetUserContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_SETUSER);
    }

    @Override
    public SecQueryType visitCmdAclUsers(RedisParser.CmdAclUsersContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_USERS);
    }

    @Override
    public SecQueryType visitCmdAclWhoami(RedisParser.CmdAclWhoamiContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_WHOAMI);
    }

    /* -------------------------------------------------------------------------------- command commands */

    @Override
    public SecQueryType visitCmdCommand(RedisParser.CmdCommandContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND);
    }

    @Override
    public SecQueryType visitCmdCommandCount(RedisParser.CmdCommandCountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_COUNT);
    }

    @Override
    public SecQueryType visitCmdCommandDocs(RedisParser.CmdCommandDocsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_DOCS);
    }

    @Override
    public SecQueryType visitCmdCommandGetKeys(RedisParser.CmdCommandGetKeysContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_GETKEYS);
    }

    @Override
    public SecQueryType visitCmdCommandGetKeysAndFlags(RedisParser.CmdCommandGetKeysAndFlagsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_GETKEYSANDFLAGS);
    }

    @Override
    public SecQueryType visitCmdCommandInfo(RedisParser.CmdCommandInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_INFO);
    }

    @Override
    public SecQueryType visitCmdCommandList(RedisParser.CmdCommandListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_LIST);
    }

    /* ----------------------------------------------------------------------------------- config commands */

    @Override
    public SecQueryType visitCmdConfigGet(RedisParser.CmdConfigGetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CONFIG_GET);
    }

    @Override
    public SecQueryType visitCmdConfigSet(RedisParser.CmdConfigSetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CONFIG_SET);
    }

    @Override
    public SecQueryType visitCmdConfigResetStat(RedisParser.CmdConfigResetStatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CONFIG_RESETSTAT);
    }

    @Override
    public SecQueryType visitCmdConfigRewrite(RedisParser.CmdConfigRewriteContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CONFIG_REWRITE);
    }

    /* ----------------------------------------------------------------------------------- latency commands */

    @Override
    public SecQueryType visitCmdLatencyHistogram(RedisParser.CmdLatencyHistogramContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_HISTOGRAM);
    }

    @Override
    public SecQueryType visitCmdLatencyHistory(RedisParser.CmdLatencyHistoryContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_HISTORY);
    }

    @Override
    public SecQueryType visitCmdLatencyLatest(RedisParser.CmdLatencyLatestContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_LATEST);
    }

    @Override
    public SecQueryType visitCmdLatencyReset(RedisParser.CmdLatencyResetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_RESET);
    }

    /* ----------------------------------------------------------------------------------- memory commands */

    @Override
    public SecQueryType visitCmdMemoryDoctor(RedisParser.CmdMemoryDoctorContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_DOCTOR);
    }

    @Override
    public SecQueryType visitCmdMemoryMallocStats(RedisParser.CmdMemoryMallocStatsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_MALLOC_STATS);
    }

    @Override
    public SecQueryType visitCmdMemoryPurge(RedisParser.CmdMemoryPurgeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_PURGE);
    }

    @Override
    public SecQueryType visitCmdMemoryStats(RedisParser.CmdMemoryStatsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_STATS);
    }

    @Override
    public SecQueryType visitCmdMemoryUsage(RedisParser.CmdMemoryUsageContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_USAGE);
    }

    /* ----------------------------------------------------------------------------------- module commands */

    @Override
    public SecQueryType visitCmdModuleList(RedisParser.CmdModuleListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MODULE_LIST);
    }

    @Override
    public SecQueryType visitCmdModuleLoad(RedisParser.CmdModuleLoadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MODULE_LOAD);
    }

    @Override
    public SecQueryType visitCmdModuleLoadEx(RedisParser.CmdModuleLoadExContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MODULE_LOADEX);
    }

    @Override
    public SecQueryType visitCmdModuleUnload(RedisParser.CmdModuleUnloadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MODULE_UNLOAD);
    }

    /* ----------------------------------------------------------------------------------- control commands */

    @Override
    public SecQueryType visitCmdBgrewriteaof(RedisParser.CmdBgrewriteaofContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BGREWRITEAOF);
    }

    @Override
    public SecQueryType visitCmdBgsave(RedisParser.CmdBgsaveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BGSAVE);
    }

    @Override
    public SecQueryType visitCmdDbsize(RedisParser.CmdDbsizeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DBSIZE);
    }

    @Override
    public SecQueryType visitCmdFailover(RedisParser.CmdFailoverContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FAILOVER);
    }

    @Override
    public SecQueryType visitCmdFlushAll(RedisParser.CmdFlushAllContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FLUSHALL);
    }

    @Override
    public SecQueryType visitCmdFlushDB(RedisParser.CmdFlushDBContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FLUSHDB);
    }

    @Override
    public SecQueryType visitCmdLastsave(RedisParser.CmdLastsaveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LASTSAVE);
    }

    @Override
    public SecQueryType visitCmdLolwut(RedisParser.CmdLolwutContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LOLWUT);
    }

    @Override
    public SecQueryType visitCmdMonitor(RedisParser.CmdMonitorContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MONITOR);
    }

    @Override
    public SecQueryType visitCmdPSync(RedisParser.CmdPSyncContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PSYNC);
    }

    @Override
    public SecQueryType visitCmdReplicaOf(RedisParser.CmdReplicaOfContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.REPLICAOF);
    }

    @Override
    public SecQueryType visitCmdRole(RedisParser.CmdRoleContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ROLE);
    }

    @Override
    public SecQueryType visitCmdSave(RedisParser.CmdSaveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SAVE);
    }

    @Override
    public SecQueryType visitCmdShutdown(RedisParser.CmdShutdownContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SHUTDOWN);
    }

    @Override
    public SecQueryType visitCmdSlaveOf(RedisParser.CmdSlaveOfContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SLAVEOF);
    }

    @Override
    public SecQueryType visitCmdSlowlogGet(RedisParser.CmdSlowlogGetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SLOWLOG_GET);
    }

    @Override
    public SecQueryType visitCmdSlowlogLen(RedisParser.CmdSlowlogLenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SLOWLOG_LEN);
    }

    @Override
    public SecQueryType visitCmdSlowlogReset(RedisParser.CmdSlowlogResetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SLOWLOG_RESET);
    }

    @Override
    public SecQueryType visitCmdSwapDB(RedisParser.CmdSwapDBContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SWAPDB);
    }

    @Override
    public SecQueryType visitCmdSync(RedisParser.CmdSyncContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SYNC);
    }

    @Override
    public SecQueryType visitCmdTime(RedisParser.CmdTimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.TIME);
    }

    /* ----------------------------------------------------------------------------------- Client commands */

    @Override
    public SecQueryType visitCmdClientCaching(RedisParser.CmdClientCachingContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_CACHING);
    }

    @Override
    public SecQueryType visitCmdClientGetname(RedisParser.CmdClientGetnameContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_GETNAME);
    }

    @Override
    public SecQueryType visitCmdClientGetredir(RedisParser.CmdClientGetredirContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_GETREDIR);
    }

    @Override
    public SecQueryType visitCmdClientID(RedisParser.CmdClientIDContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_ID);
    }

    @Override
    public SecQueryType visitCmdClientInfo(RedisParser.CmdClientInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_INFO);
    }

    @Override
    public SecQueryType visitCmdClientKill(RedisParser.CmdClientKillContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_KILL);
    }

    @Override
    public SecQueryType visitCmdClientList(RedisParser.CmdClientListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_LIST);
    }

    @Override
    public SecQueryType visitCmdClientNoEvict(RedisParser.CmdClientNoEvictContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_NO_EVICT);
    }

    @Override
    public SecQueryType visitCmdClientNoTouch(RedisParser.CmdClientNoTouchContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_NO_TOUCH);
    }

    @Override
    public SecQueryType visitCmdClientPause(RedisParser.CmdClientPauseContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_PAUSE);
    }

    @Override
    public SecQueryType visitCmdClientReply(RedisParser.CmdClientReplyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_REPLY);
    }

    @Override
    public SecQueryType visitCmdClientSetInfo(RedisParser.CmdClientSetInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_SETINFO);
    }

    @Override
    public SecQueryType visitCmdClientSetname(RedisParser.CmdClientSetnameContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_SETNAME);
    }

    @Override
    public SecQueryType visitCmdClientTracking(RedisParser.CmdClientTrackingContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_TRACKING);
    }

    @Override
    public SecQueryType visitCmdClientTrackingInfo(RedisParser.CmdClientTrackingInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_TRACKINGINFO);
    }

    @Override
    public SecQueryType visitCmdClientUnBlock(RedisParser.CmdClientUnBlockContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_UNBLOCK);
    }

    @Override
    public SecQueryType visitCmdClientUnPause(RedisParser.CmdClientUnPauseContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_UNPAUSE);
    }

    @Override
    public SecQueryType visitCmdAuth(RedisParser.CmdAuthContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.AUTH);
    }

    @Override
    public SecQueryType visitCmdEcho(RedisParser.CmdEchoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ECHO);
    }

    @Override
    public SecQueryType visitCmdHello(RedisParser.CmdHelloContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HELLO);
    }

    @Override
    public SecQueryType visitCmdPing(RedisParser.CmdPingContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PING);
    }

    @Override
    public SecQueryType visitCmdQuit(RedisParser.CmdQuitContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.QUIT);
    }

    @Override
    public SecQueryType visitCmdReset(RedisParser.CmdResetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RESET);
    }

    @Override
    public SecQueryType visitCmdSelect(RedisParser.CmdSelectContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SELECT);
    }
}
