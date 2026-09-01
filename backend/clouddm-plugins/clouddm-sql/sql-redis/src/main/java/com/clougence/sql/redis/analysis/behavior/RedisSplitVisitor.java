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
package com.clougence.sql.redis.analysis.behavior;

import java.util.LinkedHashSet;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;
import com.clougence.sql.redis.analysis.security.RedisAnalysisHelper;
import com.clougence.sql.redis.parser.antlr.RedisParser;
import com.clougence.sql.redis.parser.antlr.RedisParserBaseVisitor;
import com.clougence.sql.redis.parser.ast.RedisCmdType;

public class RedisSplitVisitor extends RedisParserBaseVisitor<StatementType> {

    public static final RedisSplitVisitor INSTANCE = new RedisSplitVisitor();

    private StatementType cmdTypeToSecQueryType(RedisCmdType cmdType) {
        return RedisAnalysisHelper.cmdTypeToSecQueryType(cmdType);
    }

    public Set<StatementType> collectTypes(ParseTree tree) {
        LinkedHashSet<StatementType> types = new LinkedHashSet<>();
        RedisParser.CmdBitFieldContext bitField = findContext(tree, RedisParser.CmdBitFieldContext.class);
        if (bitField != null) {
            for (RedisParser.BitFieldItemContext item : bitField.bitFieldItem()) {
                if (item.GET() != null) {
                    types.add(StatementType.SELECT);
                } else {
                    types.add(StatementType.MERGE);
                }
            }
            if (types.isEmpty()) {
                types.add(StatementType.SELECT);
            }
            return types;
        }

        RedisParser.CmdAclSetUserContext aclSetUser = findContext(tree, RedisParser.CmdAclSetUserContext.class);
        if (aclSetUser != null) {
            for (RedisParser.AclCmdRuleContext rule : aclSetUser.aclCmdRule()) {
                collectAclRuleTypes(rule.getText(), types);
            }
            return types;
        }

        StatementType primary = tree.accept(this);
        types.add(primary == null ? StatementType.UNKNOWN : primary);

        if (containsContext(tree, RedisParser.CmdCopyContext.class) || containsContext(tree, RedisParser.CmdSortContext.class)
            || containsContext(tree, RedisParser.CmdBitOPContext.class) || containsContext(tree, RedisParser.CmdSdiffstoreContext.class)
            || containsContext(tree, RedisParser.CmdSinterStoreContext.class) || containsContext(tree, RedisParser.CmdSunionstoreContext.class)
            || containsContext(tree, RedisParser.CmdZdiffStoreContext.class) || containsContext(tree, RedisParser.CmdZinterstoreContext.class)
            || containsContext(tree, RedisParser.CmdZrangestoreContext.class) || containsContext(tree, RedisParser.CmdZunionstoreContext.class)
            || containsContext(tree, RedisParser.CmdPFMergeContext.class)) {
            if (!containsContext(tree, RedisParser.CmdSortContext.class) || findContext(tree, RedisParser.CmdSortContext.class).destination != null) {
                types.add(StatementType.SELECT);
            }
        }

        if (containsContext(tree, RedisParser.CmdMoveContext.class) || containsContext(tree, RedisParser.CmdBlmoveContext.class)
            || containsContext(tree, RedisParser.CmdBrpoplpushContext.class) || containsContext(tree, RedisParser.CmdLmoveContext.class)
            || containsContext(tree, RedisParser.CmdRpoplpushContext.class) || containsContext(tree, RedisParser.CmdSmoveContext.class)) {
            types.add(StatementType.INSERT);
        }

        RedisParser.CmdFunctionRestoreContext functionRestore = findContext(tree, RedisParser.CmdFunctionRestoreContext.class);
        if (functionRestore != null && functionRestore.FLUSH() != null) {
            types.add(StatementType.DROP_PROG_OBJ);
        }

        if (containsContext(tree, RedisParser.CmdModuleLoadContext.class) || containsContext(tree, RedisParser.CmdModuleLoadExContext.class)
            || containsContext(tree, RedisParser.CmdModuleUnloadContext.class) || containsContext(tree, RedisParser.CmdFunctionLoadContext.class)
            || containsContext(tree, RedisParser.CmdFunctionRestoreContext.class) || containsContext(tree, RedisParser.CmdScriptLoadContext.class)
            || containsContext(tree, RedisParser.CmdFlushAllContext.class) || containsContext(tree, RedisParser.CmdFlushDBContext.class)) {
            types.add(StatementType.UNSAFE);
        }
        return types;
    }

    private static void collectAclRuleTypes(String rule, Set<StatementType> types) {
        String normalized = rule.toLowerCase();
        if (normalized.startsWith("(")) {
            if (normalized.contains("+")) {
                types.add(StatementType.GRANT);
            }
            if (normalized.contains("-")) {
                types.add(StatementType.REVOKE);
            }
            return;
        }
        if (normalized.startsWith("-") || normalized.startsWith("<") || normalized.startsWith("!") || normalized.equals("reset") || normalized.equals("off")
            || normalized.equals("resetkeys") || normalized.equals("resetchannels") || normalized.equals("nocommands") || normalized.equals("clearselectors")) {
            types.add(StatementType.REVOKE);
        }
        if (normalized.startsWith("+") || normalized.startsWith(">") || normalized.startsWith("#") || normalized.equals("on") || normalized.equals("allkeys")
            || normalized.equals("allchannels") || normalized.equals("allcommands") || normalized.equals("nopass") || normalized.startsWith("~") || normalized.startsWith("%")
            || normalized.startsWith("&")) {
            types.add(StatementType.GRANT);
        }
    }

    private static boolean containsContext(ParseTree tree, Class<? extends ParseTree> type) {
        return findContext(tree, type) != null;
    }

    private static <T extends ParseTree> T findContext(ParseTree tree, Class<T> type) {
        if (type.isInstance(tree)) {
            return type.cast(tree);
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            T result = findContext(tree.getChild(i), type);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public StatementType visitChildren(RuleNode node) {
        int n = node.getChildCount();
        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            StatementType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }

        return StatementType.UNKNOWN;
    }

    /* ----------------------------------------------------------------------------------- Keys commands */

    @Override
    public StatementType visitCmdCopy(RedisParser.CmdCopyContext ctx) {
        return ctx.REPLACE() == null ? StatementType.INSERT : StatementType.MERGE;
    }

    @Override
    public StatementType visitCmdDel(RedisParser.CmdDelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DEL);
    }

    @Override
    public StatementType visitCmdDump(RedisParser.CmdDumpContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DUMP);
    }

    @Override
    public StatementType visitCmdExists(RedisParser.CmdExistsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXISTS);
    }

    @Override
    public StatementType visitCmdExpire(RedisParser.CmdExpireContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXPIRE);
    }

    @Override
    public StatementType visitCmdExpireat(RedisParser.CmdExpireatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXPIREAT);
    }

    @Override
    public StatementType visitCmdExpireTime(RedisParser.CmdExpireTimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXPIRETIME);
    }

    @Override
    public StatementType visitCmdKeys(RedisParser.CmdKeysContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.KEYS);
    }

    @Override
    public StatementType visitCmdMove(RedisParser.CmdMoveContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitCmdObject(RedisParser.CmdObjectContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.OBJECT);
    }

    @Override
    public StatementType visitCmdPersist(RedisParser.CmdPersistContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PERSIST);
    }

    @Override
    public StatementType visitCmdPexpire(RedisParser.CmdPexpireContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PEXPIRE);
    }

    @Override
    public StatementType visitCmdPexpireat(RedisParser.CmdPexpireatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PEXPIREAT);
    }

    @Override
    public StatementType visitCmdPExpireTime(RedisParser.CmdPExpireTimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PEXPIRETIME);
    }

    @Override
    public StatementType visitCmdTtl(RedisParser.CmdTtlContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.TTL);
    }

    @Override
    public StatementType visitCmdPttl(RedisParser.CmdPttlContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PTTL);
    }

    @Override
    public StatementType visitCmdRandomkey(RedisParser.CmdRandomkeyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RANDOMKEY);
    }

    @Override
    public StatementType visitCmdRename(RedisParser.CmdRenameContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RENAME);
    }

    @Override
    public StatementType visitCmdRenamenx(RedisParser.CmdRenamenxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RENAMENX);
    }

    @Override
    public StatementType visitCmdRestore(RedisParser.CmdRestoreContext ctx) {
        return ctx.REPLACE() == null ? StatementType.INSERT : StatementType.MERGE;
    }

    @Override
    public StatementType visitCmdScan(RedisParser.CmdScanContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCAN);
    }

    @Override
    public StatementType visitCmdSort(RedisParser.CmdSortContext ctx) {
        return ctx.destination == null ? StatementType.SELECT : StatementType.MERGE;
    }

    @Override
    public StatementType visitCmdSortro(RedisParser.CmdSortroContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SORT_RO);
    }

    @Override
    public StatementType visitCmdTouch(RedisParser.CmdTouchContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.TOUCH);
    }

    @Override
    public StatementType visitCmdType(RedisParser.CmdTypeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.TYPE);
    }

    @Override
    public StatementType visitCmdUnlink(RedisParser.CmdUnlinkContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.UNLINK);
    }

    @Override
    public StatementType visitCmdWait(RedisParser.CmdWaitContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.WAIT);
    }

    @Override
    public StatementType visitCmdWaitAOF(RedisParser.CmdWaitAOFContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.WAITAOF);
    }

    /* ----------------------------------------------------------------------------------- String commands */

    @Override
    public StatementType visitCmdAppend(RedisParser.CmdAppendContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.APPEND);
    }

    @Override
    public StatementType visitCmdDecr(RedisParser.CmdDecrContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DECR);
    }

    @Override
    public StatementType visitCmdDecrby(RedisParser.CmdDecrbyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DECRBY);
    }

    @Override
    public StatementType visitCmdGet(RedisParser.CmdGetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GET);
    }

    @Override
    public StatementType visitCmdGetdel(RedisParser.CmdGetdelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETDEL);
    }

    @Override
    public StatementType visitCmdGetex(RedisParser.CmdGetexContext ctx) {
        return ctx.ttlOpt() == null && ctx.PERSIST() == null ? StatementType.SELECT : StatementType.UPDATE;
    }

    @Override
    public StatementType visitCmdGetrange(RedisParser.CmdGetrangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETRANGE);
    }

    @Override
    public StatementType visitCmdGetset(RedisParser.CmdGetsetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETSET);
    }

    @Override
    public StatementType visitCmdIncr(RedisParser.CmdIncrContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.INCR);
    }

    @Override
    public StatementType visitCmdIncrby(RedisParser.CmdIncrbyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.INCRBY);
    }

    @Override
    public StatementType visitCmdIncrbyFloat(RedisParser.CmdIncrbyFloatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.INCRBYFLOAT);
    }

    @Override
    public StatementType visitCmdLcs(RedisParser.CmdLcsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LCS);
    }

    @Override
    public StatementType visitCmdMget(RedisParser.CmdMgetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MGET);
    }

    @Override
    public StatementType visitCmdMset(RedisParser.CmdMsetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MSET);
    }

    @Override
    public StatementType visitCmdMsetnx(RedisParser.CmdMsetnxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MSETNX);
    }

    @Override
    public StatementType visitCmdSetex(RedisParser.CmdSetexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SETEX);
    }

    @Override
    public StatementType visitCmdPSetex(RedisParser.CmdPSetexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PSETEX);
    }

    @Override
    public StatementType visitCmdSet(RedisParser.CmdSetContext ctx) {
        if (ctx.NX() != null) {
            return StatementType.INSERT;
        }
        if (ctx.XX() != null) {
            return StatementType.UPDATE;
        }
        return StatementType.MERGE;
    }

    @Override
    public StatementType visitCmdSetnx(RedisParser.CmdSetnxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SETNX);
    }

    @Override
    public StatementType visitCmdSetrange(RedisParser.CmdSetrangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SETRANGE);
    }

    @Override
    public StatementType visitCmdStrlen(RedisParser.CmdStrlenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.STRLEN);
    }

    @Override
    public StatementType visitCmdSubstr(RedisParser.CmdSubstrContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUBSTR);
    }

    /* ----------------------------------------------------------------------------------- Bit commands */

    @Override
    public StatementType visitCmdBitCount(RedisParser.CmdBitCountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITCOUNT);
    }

    @Override
    public StatementType visitCmdBitField(RedisParser.CmdBitFieldContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITFIELD);
    }

    @Override
    public StatementType visitCmdBitFieldRO(RedisParser.CmdBitFieldROContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITFIELD_RO);
    }

    @Override
    public StatementType visitCmdBitOP(RedisParser.CmdBitOPContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITOP);
    }

    @Override
    public StatementType visitCmdBitPos(RedisParser.CmdBitPosContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITPOS);
    }

    @Override
    public StatementType visitCmdGetbit(RedisParser.CmdGetbitContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETBIT);
    }

    @Override
    public StatementType visitCmdSetbit(RedisParser.CmdSetbitContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SETBIT);
    }

    /* ----------------------------------------------------------------------------------- Hash commands */

    @Override
    public StatementType visitCmdHdel(RedisParser.CmdHdelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HDEL);
    }

    @Override
    public StatementType visitCmdHexists(RedisParser.CmdHexistsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HEXISTS);
    }

    @Override
    public StatementType visitCmdHexpire(RedisParser.CmdHexpireContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HEXPIRE);
    }

    @Override
    public StatementType visitCmdHexpireat(RedisParser.CmdHexpireatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HEXPIREAT);
    }

    @Override
    public StatementType visitCmdHexpiretime(RedisParser.CmdHexpiretimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HEXPIRETIME);
    }

    @Override
    public StatementType visitCmdHGet(RedisParser.CmdHGetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HGET);
    }

    @Override
    public StatementType visitCmdHGetAll(RedisParser.CmdHGetAllContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HGETALL);
    }

    @Override
    public StatementType visitCmdHgetDel(RedisParser.CmdHgetDelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HGETDEL);
    }

    @Override
    public StatementType visitCmdHgetex(RedisParser.CmdHgetexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HGETEX);
    }

    @Override
    public StatementType visitCmdHincrBy(RedisParser.CmdHincrByContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HINCRBY);
    }

    @Override
    public StatementType visitCmdHincrByFloat(RedisParser.CmdHincrByFloatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HINCRBYFLOAT);
    }

    @Override
    public StatementType visitCmdHKeys(RedisParser.CmdHKeysContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HKEYS);
    }

    @Override
    public StatementType visitCmdHLen(RedisParser.CmdHLenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HLEN);
    }

    @Override
    public StatementType visitCmdHMget(RedisParser.CmdHMgetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HMGET);
    }

    @Override
    public StatementType visitCmdHMset(RedisParser.CmdHMsetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HMSET);
    }

    @Override
    public StatementType visitCmdHPersist(RedisParser.CmdHPersistContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPERSIST);
    }

    @Override
    public StatementType visitCmdHPexpire(RedisParser.CmdHPexpireContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPEXPIRE);
    }

    @Override
    public StatementType visitCmdHPexpireAt(RedisParser.CmdHPexpireAtContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPEXPIREAT);
    }

    @Override
    public StatementType visitCmdHPexpireTime(RedisParser.CmdHPexpireTimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPEXPIRETIME);
    }

    @Override
    public StatementType visitCmdHPTTL(RedisParser.CmdHPTTLContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPTTL);
    }

    @Override
    public StatementType visitCmdHTTL(RedisParser.CmdHTTLContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HTTL);
    }

    @Override
    public StatementType visitCmdHrandfield(RedisParser.CmdHrandfieldContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HRANDFIELD);
    }

    @Override
    public StatementType visitCmdHscan(RedisParser.CmdHscanContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSCAN);
    }

    @Override
    public StatementType visitCmdHSet(RedisParser.CmdHSetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSET);
    }

    @Override
    public StatementType visitCmdHSetex(RedisParser.CmdHSetexContext ctx) {
        if (ctx.FNX() != null) {
            return StatementType.INSERT;
        }
        if (ctx.FXX() != null) {
            return StatementType.UPDATE;
        }
        return StatementType.MERGE;
    }

    @Override
    public StatementType visitCmdHsetnx(RedisParser.CmdHsetnxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSETNX);
    }

    @Override
    public StatementType visitCmdHStrLen(RedisParser.CmdHStrLenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSTRLEN);
    }

    @Override
    public StatementType visitCmdHVals(RedisParser.CmdHValsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HVALS);
    }

    /* ----------------------------------------------------------------------------------- List commands */

    @Override
    public StatementType visitCmdBlmove(RedisParser.CmdBlmoveContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitCmdBLmpop(RedisParser.CmdBLmpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BLMPOP);
    }

    @Override
    public StatementType visitCmdBLPop(RedisParser.CmdBLPopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BLPOP);
    }

    @Override
    public StatementType visitCmdBRPop(RedisParser.CmdBRPopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BRPOP);
    }

    @Override
    public StatementType visitCmdBrpoplpush(RedisParser.CmdBrpoplpushContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitCmdLindex(RedisParser.CmdLindexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LINDEX);
    }

    @Override
    public StatementType visitCmdLinsert(RedisParser.CmdLinsertContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LINSERT);
    }

    @Override
    public StatementType visitCmdLlen(RedisParser.CmdLlenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LLEN);
    }

    @Override
    public StatementType visitCmdLmove(RedisParser.CmdLmoveContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitCmdLmpop(RedisParser.CmdLmpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LMPOP);
    }

    @Override
    public StatementType visitCmdLPop(RedisParser.CmdLPopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LPOP);
    }

    @Override
    public StatementType visitCmdLpos(RedisParser.CmdLposContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LPOS);
    }

    @Override
    public StatementType visitCmdLPush(RedisParser.CmdLPushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LPUSH);
    }

    @Override
    public StatementType visitCmdLPushx(RedisParser.CmdLPushxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LPUSHX);
    }

    @Override
    public StatementType visitCmdLRange(RedisParser.CmdLRangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LRANGE);
    }

    @Override
    public StatementType visitCmdLRem(RedisParser.CmdLRemContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LREM);
    }

    @Override
    public StatementType visitCmdLSet(RedisParser.CmdLSetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LSET);
    }

    @Override
    public StatementType visitCmdLTrim(RedisParser.CmdLTrimContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LTRIM);
    }

    @Override
    public StatementType visitCmdRPop(RedisParser.CmdRPopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RPOP);
    }

    @Override
    public StatementType visitCmdRpoplpush(RedisParser.CmdRpoplpushContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitCmdRPush(RedisParser.CmdRPushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RPUSH);
    }

    @Override
    public StatementType visitCmdRPushx(RedisParser.CmdRPushxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RPUSHX);
    }

    /* ----------------------------------------------------------------------------------- Set commands */

    @Override
    public StatementType visitCmdSadd(RedisParser.CmdSaddContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SADD);
    }

    @Override
    public StatementType visitCmdScard(RedisParser.CmdScardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCARD);
    }

    @Override
    public StatementType visitCmdSdiff(RedisParser.CmdSdiffContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SDIFF);
    }

    @Override
    public StatementType visitCmdSdiffstore(RedisParser.CmdSdiffstoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SDIFFSTORE);
    }

    @Override
    public StatementType visitCmdSinter(RedisParser.CmdSinterContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SINTER);
    }

    @Override
    public StatementType visitCmdSinterCard(RedisParser.CmdSinterCardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SINTERCARD);
    }

    @Override
    public StatementType visitCmdSinterStore(RedisParser.CmdSinterStoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SINTERSTORE);
    }

    @Override
    public StatementType visitCmdSismember(RedisParser.CmdSismemberContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SISMEMBER);
    }

    @Override
    public StatementType visitCmdSmembers(RedisParser.CmdSmembersContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SMEMBERS);
    }

    @Override
    public StatementType visitCmdSmismember(RedisParser.CmdSmismemberContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SMISMEMBER);
    }

    @Override
    public StatementType visitCmdSmove(RedisParser.CmdSmoveContext ctx) {
        return StatementType.DELETE;
    }

    @Override
    public StatementType visitCmdSpop(RedisParser.CmdSpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SPOP);
    }

    @Override
    public StatementType visitCmdSrandmember(RedisParser.CmdSrandmemberContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SRANDMEMBER);
    }

    @Override
    public StatementType visitCmdSrem(RedisParser.CmdSremContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SREM);
    }

    @Override
    public StatementType visitCmdSscan(RedisParser.CmdSscanContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SSCAN);
    }

    @Override
    public StatementType visitCmdSunion(RedisParser.CmdSunionContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUNION);
    }

    @Override
    public StatementType visitCmdSunionstore(RedisParser.CmdSunionstoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUNIONSTORE);
    }

    /* ----------------------------------------------------------------------------------- sorted set commands */

    @Override
    public StatementType visitCmdBzmpop(RedisParser.CmdBzmpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BZMPOP);
    }

    @Override
    public StatementType visitCmdBzpopmax(RedisParser.CmdBzpopmaxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BZPOPMAX);
    }

    @Override
    public StatementType visitCmdBzpopmin(RedisParser.CmdBzpopminContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BZPOPMIN);
    }

    @Override
    public StatementType visitCmdZadd(RedisParser.CmdZaddContext ctx) {
        if (ctx.NX() != null) {
            return StatementType.INSERT;
        }
        if (ctx.XX() != null) {
            return StatementType.UPDATE;
        }
        return StatementType.MERGE;
    }

    @Override
    public StatementType visitCmdZcard(RedisParser.CmdZcardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZCARD);
    }

    @Override
    public StatementType visitCmdZcount(RedisParser.CmdZcountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZCOUNT);
    }

    @Override
    public StatementType visitCmdZdiff(RedisParser.CmdZdiffContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZDIFF);
    }

    @Override
    public StatementType visitCmdZdiffStore(RedisParser.CmdZdiffStoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZDIFFSTORE);
    }

    @Override
    public StatementType visitCmdZincrby(RedisParser.CmdZincrbyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZINCRBY);
    }

    @Override
    public StatementType visitCmdZinter(RedisParser.CmdZinterContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZINTER);
    }

    @Override
    public StatementType visitCmdZintercard(RedisParser.CmdZintercardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZINTERCARD);
    }

    @Override
    public StatementType visitCmdZinterstore(RedisParser.CmdZinterstoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZINTERSTORE);
    }

    @Override
    public StatementType visitCmdZLexCount(RedisParser.CmdZLexCountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZLEXCOUNT);
    }

    @Override
    public StatementType visitCmdZmpop(RedisParser.CmdZmpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZMPOP);
    }

    @Override
    public StatementType visitCmdZmscore(RedisParser.CmdZmscoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZMSCORE);
    }

    @Override
    public StatementType visitCmdZpopmax(RedisParser.CmdZpopmaxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZPOPMAX);
    }

    @Override
    public StatementType visitCmdZpopmin(RedisParser.CmdZpopminContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZPOPMIN);
    }

    @Override
    public StatementType visitCmdZrandmember(RedisParser.CmdZrandmemberContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANDMEMBER);
    }

    @Override
    public StatementType visitCmdZrange(RedisParser.CmdZrangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANGE);
    }

    @Override
    public StatementType visitCmdZrangebylex(RedisParser.CmdZrangebylexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANGEBYLEX);
    }

    @Override
    public StatementType visitCmdZrangebyscore(RedisParser.CmdZrangebyscoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANGEBYSCORE);
    }

    @Override
    public StatementType visitCmdZrangestore(RedisParser.CmdZrangestoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANGESTORE);
    }

    @Override
    public StatementType visitCmdZrank(RedisParser.CmdZrankContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANK);
    }

    @Override
    public StatementType visitCmdZRem(RedisParser.CmdZRemContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREM);
    }

    @Override
    public StatementType visitCmdZRemRangeByLex(RedisParser.CmdZRemRangeByLexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREMRANGEBYLEX);
    }

    @Override
    public StatementType visitCmdZremrangebyrank(RedisParser.CmdZremrangebyrankContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREMRANGEBYRANK);
    }

    @Override
    public StatementType visitCmdZRemRangeByScore(RedisParser.CmdZRemRangeByScoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREMRANGEBYSCORE);
    }

    @Override
    public StatementType visitCmdZrevrange(RedisParser.CmdZrevrangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREVRANGE);
    }

    @Override
    public StatementType visitCmdZrevrangebylex(RedisParser.CmdZrevrangebylexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREVRANGEBYLEX);
    }

    @Override
    public StatementType visitCmdZrevrangebyscore(RedisParser.CmdZrevrangebyscoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREVRANGEBYSCORE);
    }

    @Override
    public StatementType visitCmdZrevrank(RedisParser.CmdZrevrankContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREVRANK);
    }

    @Override
    public StatementType visitCmdZscan(RedisParser.CmdZscanContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZSCAN);
    }

    @Override
    public StatementType visitCmdZscore(RedisParser.CmdZscoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZSCORE);
    }

    @Override
    public StatementType visitCmdZunion(RedisParser.CmdZunionContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZUNION);
    }

    @Override
    public StatementType visitCmdZunionstore(RedisParser.CmdZunionstoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZUNIONSTORE);
    }

    /* ----------------------------------------------------------------------------------- script commands */

    @Override
    public StatementType visitCmdScriptDebug(RedisParser.CmdScriptDebugContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_DEBUG);
    }

    @Override
    public StatementType visitCmdScriptExists(RedisParser.CmdScriptExistsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_EXISTS);
    }

    @Override
    public StatementType visitCmdScriptFlush(RedisParser.CmdScriptFlushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_FLUSH);
    }

    @Override
    public StatementType visitCmdScriptKill(RedisParser.CmdScriptKillContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_KILL);
    }

    @Override
    public StatementType visitCmdScriptLoad(RedisParser.CmdScriptLoadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_LOAD);
    }

    @Override
    public StatementType visitCmdEval(RedisParser.CmdEvalContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EVAL);
    }

    @Override
    public StatementType visitCmdEvalRO(RedisParser.CmdEvalROContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EVAL_RO);
    }

    @Override
    public StatementType visitCmdEvalsha(RedisParser.CmdEvalshaContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EVALSHA);
    }

    @Override
    public StatementType visitCmdEvalshaRO(RedisParser.CmdEvalshaROContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EVALSHA_RO);
    }

    @Override
    public StatementType visitCmdFCall(RedisParser.CmdFCallContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FCALL);
    }

    @Override
    public StatementType visitCmdFCallRO(RedisParser.CmdFCallROContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FCALL_RO);
    }

    @Override
    public StatementType visitCmdFunctionDel(RedisParser.CmdFunctionDelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_DEL);
    }

    @Override
    public StatementType visitCmdFunctionDump(RedisParser.CmdFunctionDumpContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_DUMP);
    }

    @Override
    public StatementType visitCmdFunctionFlush(RedisParser.CmdFunctionFlushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_FLUSH);
    }

    @Override
    public StatementType visitCmdFunctionKill(RedisParser.CmdFunctionKillContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_KILL);
    }

    @Override
    public StatementType visitCmdFunctionList(RedisParser.CmdFunctionListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_LIST);
    }

    @Override
    public StatementType visitCmdFunctionLoad(RedisParser.CmdFunctionLoadContext ctx) {
        return ctx.REPLACE() == null ? StatementType.CREATE_PROG_OBJ : StatementType.ALTER_PROG_OBJ;
    }

    @Override
    public StatementType visitCmdFunctionRestore(RedisParser.CmdFunctionRestoreContext ctx) {
        return ctx.REPLACE() == null ? StatementType.CREATE_PROG_OBJ : StatementType.UNSAFE;
    }

    @Override
    public StatementType visitCmdFunctionStats(RedisParser.CmdFunctionStatsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_STATS);
    }

    /* ----------------------------------------------------------------------------------- tx commands */

    @Override
    public StatementType visitCmdDiscard(RedisParser.CmdDiscardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DISCARD);
    }

    @Override
    public StatementType visitCmdExec(RedisParser.CmdExecContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXEC);
    }

    @Override
    public StatementType visitCmdMulti(RedisParser.CmdMultiContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MULTI);
    }

    @Override
    public StatementType visitCmdUnwatch(RedisParser.CmdUnwatchContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.UNWATCH);
    }

    @Override
    public StatementType visitCmdWatch(RedisParser.CmdWatchContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.WATCH);
    }

    /* ----------------------------------------------------------------------------------- HyperLog commands */

    @Override
    public StatementType visitCmdPFAdd(RedisParser.CmdPFAddContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PFADD);
    }

    @Override
    public StatementType visitCmdPFCount(RedisParser.CmdPFCountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PFCOUNT);
    }

    @Override
    public StatementType visitCmdPFMerge(RedisParser.CmdPFMergeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PFMERGE);
    }

    /* ----------------------------------------------------------------------------------- publish commands */

    @Override
    public StatementType visitCmdPSubscribe(RedisParser.CmdPSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PSUBSCRIBE);
    }

    @Override
    public StatementType visitCmdPublish(RedisParser.CmdPublishContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBLISH);
    }

    @Override
    public StatementType visitCmdPubSubChannels(RedisParser.CmdPubSubChannelsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_CHANNELS);
    }

    @Override
    public StatementType visitCmdPubSubNumPat(RedisParser.CmdPubSubNumPatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_NUMPAT);
    }

    @Override
    public StatementType visitCmdPubSubNumSub(RedisParser.CmdPubSubNumSubContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_NUMSUB);
    }

    @Override
    public StatementType visitCmdPubSubShardChannels(RedisParser.CmdPubSubShardChannelsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_SHARDCHANNELS);
    }

    @Override
    public StatementType visitCmdPubSubShardNumSub(RedisParser.CmdPubSubShardNumSubContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_SHARDNUMSUB);
    }

    @Override
    public StatementType visitCmdPunSubscribe(RedisParser.CmdPunSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUNSUBSCRIBE);
    }

    @Override
    public StatementType visitCmdSpublish(RedisParser.CmdSpublishContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SPUBLISH);
    }

    @Override
    public StatementType visitCmdSSubscribe(RedisParser.CmdSSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SSUBSCRIBE);
    }

    @Override
    public StatementType visitCmdSubscribe(RedisParser.CmdSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUBSCRIBE);
    }

    @Override
    public StatementType visitCmdSunSubscribe(RedisParser.CmdSunSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUNSUBSCRIBE);
    }

    @Override
    public StatementType visitCmdUnSubScribe(RedisParser.CmdUnSubScribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.UNSUBSCRIBE);
    }

    /* ----------------------------------------------------------------------------------- cluster commands */

    @Override
    public StatementType visitCmdAsking(RedisParser.CmdAskingContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ASKING);
    }

    @Override
    public StatementType visitCmdReadonly(RedisParser.CmdReadonlyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.READONLY);
    }

    @Override
    public StatementType visitCmdReadWrite(RedisParser.CmdReadWriteContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.READWRITE);
    }

    @Override
    public StatementType visitCmdClusterAddSlots(RedisParser.CmdClusterAddSlotsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_ADDSLOTS);
    }

    @Override
    public StatementType visitCmdClusterDelSlots(RedisParser.CmdClusterDelSlotsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_DELSLOTS);
    }

    @Override
    public StatementType visitCmdClusterAddSlotsRange(RedisParser.CmdClusterAddSlotsRangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_ADDSLOTSRANGE);
    }

    @Override
    public StatementType visitCmdClusterDelSlotsRange(RedisParser.CmdClusterDelSlotsRangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_DELSLOTSRANGE);
    }

    @Override
    public StatementType visitCmdClusterBumpEpoch(RedisParser.CmdClusterBumpEpochContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_BUMPEPOCH);
    }

    @Override
    public StatementType visitCmdClusterCountFailureReports(RedisParser.CmdClusterCountFailureReportsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_COUNT_FAILURE_REPORTS);
    }

    @Override
    public StatementType visitCmdClusterCountKeysInSlot(RedisParser.CmdClusterCountKeysInSlotContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_COUNTKEYSINSLOT);
    }

    @Override
    public StatementType visitCmdClusterFailOver(RedisParser.CmdClusterFailOverContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_FAILOVER);
    }

    @Override
    public StatementType visitCmdClusterFlushSlots(RedisParser.CmdClusterFlushSlotsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_FLUSHSLOTS);
    }

    @Override
    public StatementType visitCmdClusterForget(RedisParser.CmdClusterForgetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_FORGET);
    }

    @Override
    public StatementType visitCmdClusterGetKeysInSlot(RedisParser.CmdClusterGetKeysInSlotContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_GETKEYSINSLOT);
    }

    @Override
    public StatementType visitCmdClusterInfo(RedisParser.CmdClusterInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_INFO);
    }

    @Override
    public StatementType visitCmdClusterKeySlot(RedisParser.CmdClusterKeySlotContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitCmdClusterLinks(RedisParser.CmdClusterLinksContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_LINKS);
    }

    @Override
    public StatementType visitCmdClusterMeet(RedisParser.CmdClusterMeetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_MEET);
    }

    @Override
    public StatementType visitCmdClusterMyId(RedisParser.CmdClusterMyIdContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_MYID);
    }

    @Override
    public StatementType visitCmdClusterMyShardId(RedisParser.CmdClusterMyShardIdContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_MYSHARDID);
    }

    @Override
    public StatementType visitCmdClusterNodes(RedisParser.CmdClusterNodesContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_NODES);
    }

    @Override
    public StatementType visitCmdClusterReplicas(RedisParser.CmdClusterReplicasContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_REPLICAS);
    }

    @Override
    public StatementType visitCmdClusterReplicate(RedisParser.CmdClusterReplicateContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_REPLICATE);
    }

    @Override
    public StatementType visitCmdClusterReset(RedisParser.CmdClusterResetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_RESET);
    }

    @Override
    public StatementType visitCmdClusterSaveConfig(RedisParser.CmdClusterSaveConfigContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SAVECONFIG);
    }

    @Override
    public StatementType visitCmdClusterSetConfigEpoch(RedisParser.CmdClusterSetConfigEpochContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SET_CONFIG_EPOCH);
    }

    @Override
    public StatementType visitCmdClusterSetSlot(RedisParser.CmdClusterSetSlotContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SETSLOT);
    }

    @Override
    public StatementType visitCmdClusterShards(RedisParser.CmdClusterShardsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SHARDS);
    }

    @Override
    public StatementType visitCmdClusterSlaves(RedisParser.CmdClusterSlavesContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SLAVES);
    }

    @Override
    public StatementType visitCmdClusterSlotStats(RedisParser.CmdClusterSlotStatsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SLOT_STATS);
    }

    @Override
    public StatementType visitCmdClusterSlots(RedisParser.CmdClusterSlotsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SLOTS);
    }

    /* ----------------------------------------------------------------------------------- info commands */

    @Override
    public StatementType visitCmdInfo(RedisParser.CmdInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.INFO);
    }

    /* ----------------------------------------------------------------------------------- acl commands */

    @Override
    public StatementType visitCmdAclCat(RedisParser.CmdAclCatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_CAT);
    }

    @Override
    public StatementType visitCmdAclDelUser(RedisParser.CmdAclDelUserContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_DELUSER);
    }

    @Override
    public StatementType visitCmdAclDryRun(RedisParser.CmdAclDryRunContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_DRYRUN);
    }

    @Override
    public StatementType visitCmdAclGenPass(RedisParser.CmdAclGenPassContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_GENPASS);
    }

    @Override
    public StatementType visitCmdAclGetUser(RedisParser.CmdAclGetUserContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_GETUSER);
    }

    @Override
    public StatementType visitCmdAclList(RedisParser.CmdAclListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_LIST);
    }

    @Override
    public StatementType visitCmdAclLoad(RedisParser.CmdAclLoadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_LOAD);
    }

    @Override
    public StatementType visitCmdAclLog(RedisParser.CmdAclLogContext ctx) {
        return ctx.RESET() == null ? StatementType.LOG_READ : StatementType.MAINTAIN_LOG;
    }

    @Override
    public StatementType visitCmdAclSave(RedisParser.CmdAclSaveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_SAVE);
    }

    @Override
    public StatementType visitCmdAclSetUser(RedisParser.CmdAclSetUserContext ctx) {
        return StatementType.GRANT;
    }

    @Override
    public StatementType visitCmdAclUsers(RedisParser.CmdAclUsersContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_USERS);
    }

    @Override
    public StatementType visitCmdAclWhoami(RedisParser.CmdAclWhoamiContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_WHOAMI);
    }

    /* -------------------------------------------------------------------------------- command commands */

    @Override
    public StatementType visitCmdCommand(RedisParser.CmdCommandContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND);
    }

    @Override
    public StatementType visitCmdCommandCount(RedisParser.CmdCommandCountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_COUNT);
    }

    @Override
    public StatementType visitCmdCommandDocs(RedisParser.CmdCommandDocsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_DOCS);
    }

    @Override
    public StatementType visitCmdCommandGetKeys(RedisParser.CmdCommandGetKeysContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_GETKEYS);
    }

    @Override
    public StatementType visitCmdCommandGetKeysAndFlags(RedisParser.CmdCommandGetKeysAndFlagsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_GETKEYSANDFLAGS);
    }

    @Override
    public StatementType visitCmdCommandInfo(RedisParser.CmdCommandInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_INFO);
    }

    @Override
    public StatementType visitCmdCommandList(RedisParser.CmdCommandListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_LIST);
    }

    /* ----------------------------------------------------------------------------------- config commands */

    @Override
    public StatementType visitCmdConfigGet(RedisParser.CmdConfigGetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CONFIG_GET);
    }

    @Override
    public StatementType visitCmdConfigSet(RedisParser.CmdConfigSetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CONFIG_SET);
    }

    @Override
    public StatementType visitCmdConfigResetStat(RedisParser.CmdConfigResetStatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CONFIG_RESETSTAT);
    }

    @Override
    public StatementType visitCmdConfigRewrite(RedisParser.CmdConfigRewriteContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CONFIG_REWRITE);
    }

    /* ----------------------------------------------------------------------------------- latency commands */

    @Override
    public StatementType visitCmdLatencyDoctor(RedisParser.CmdLatencyDoctorContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_DOCTOR);
    }

    @Override
    public StatementType visitCmdLatencyGraph(RedisParser.CmdLatencyGraphContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_GRAPH);
    }

    @Override
    public StatementType visitCmdLatencyHistogram(RedisParser.CmdLatencyHistogramContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_HISTOGRAM);
    }

    @Override
    public StatementType visitCmdLatencyHistory(RedisParser.CmdLatencyHistoryContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_HISTORY);
    }

    @Override
    public StatementType visitCmdLatencyLatest(RedisParser.CmdLatencyLatestContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_LATEST);
    }

    @Override
    public StatementType visitCmdLatencyReset(RedisParser.CmdLatencyResetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_RESET);
    }

    /* ----------------------------------------------------------------------------------- memory commands */

    @Override
    public StatementType visitCmdMemoryDoctor(RedisParser.CmdMemoryDoctorContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_DOCTOR);
    }

    @Override
    public StatementType visitCmdMemoryMallocStats(RedisParser.CmdMemoryMallocStatsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_MALLOC_STATS);
    }

    @Override
    public StatementType visitCmdMemoryPurge(RedisParser.CmdMemoryPurgeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_PURGE);
    }

    @Override
    public StatementType visitCmdMemoryStats(RedisParser.CmdMemoryStatsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_STATS);
    }

    @Override
    public StatementType visitCmdMemoryUsage(RedisParser.CmdMemoryUsageContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_USAGE);
    }

    /* ----------------------------------------------------------------------------------- module commands */

    @Override
    public StatementType visitCmdModuleList(RedisParser.CmdModuleListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MODULE_LIST);
    }

    @Override
    public StatementType visitCmdModuleLoad(RedisParser.CmdModuleLoadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MODULE_LOAD);
    }

    @Override
    public StatementType visitCmdModuleLoadEx(RedisParser.CmdModuleLoadExContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MODULE_LOADEX);
    }

    @Override
    public StatementType visitCmdModuleUnload(RedisParser.CmdModuleUnloadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MODULE_UNLOAD);
    }

    /* ----------------------------------------------------------------------------------- control commands */

    @Override
    public StatementType visitCmdBgrewriteaof(RedisParser.CmdBgrewriteaofContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BGREWRITEAOF);
    }

    @Override
    public StatementType visitCmdBgsave(RedisParser.CmdBgsaveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BGSAVE);
    }

    @Override
    public StatementType visitCmdDbsize(RedisParser.CmdDbsizeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DBSIZE);
    }

    @Override
    public StatementType visitCmdFailover(RedisParser.CmdFailoverContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FAILOVER);
    }

    @Override
    public StatementType visitCmdFlushAll(RedisParser.CmdFlushAllContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FLUSHALL);
    }

    @Override
    public StatementType visitCmdFlushDB(RedisParser.CmdFlushDBContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FLUSHDB);
    }

    @Override
    public StatementType visitCmdLastsave(RedisParser.CmdLastsaveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LASTSAVE);
    }

    @Override
    public StatementType visitCmdLolwut(RedisParser.CmdLolwutContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitCmdMonitor(RedisParser.CmdMonitorContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MONITOR);
    }

    @Override
    public StatementType visitCmdPSync(RedisParser.CmdPSyncContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PSYNC);
    }

    @Override
    public StatementType visitCmdReplicaOf(RedisParser.CmdReplicaOfContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.REPLICAOF);
    }

    @Override
    public StatementType visitCmdRole(RedisParser.CmdRoleContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ROLE);
    }

    @Override
    public StatementType visitCmdSave(RedisParser.CmdSaveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SAVE);
    }

    @Override
    public StatementType visitCmdShutdown(RedisParser.CmdShutdownContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SHUTDOWN);
    }

    @Override
    public StatementType visitCmdSlaveOf(RedisParser.CmdSlaveOfContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SLAVEOF);
    }

    @Override
    public StatementType visitCmdSlowlogGet(RedisParser.CmdSlowlogGetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SLOWLOG_GET);
    }

    @Override
    public StatementType visitCmdSlowlogLen(RedisParser.CmdSlowlogLenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SLOWLOG_LEN);
    }

    @Override
    public StatementType visitCmdSlowlogReset(RedisParser.CmdSlowlogResetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SLOWLOG_RESET);
    }

    @Override
    public StatementType visitCmdSwapDB(RedisParser.CmdSwapDBContext ctx) {
        return StatementType.UNSAFE;
    }

    @Override
    public StatementType visitCmdSync(RedisParser.CmdSyncContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SYNC);
    }

    @Override
    public StatementType visitCmdTime(RedisParser.CmdTimeContext ctx) {
        return StatementType.SELECT;
    }

    /* ----------------------------------------------------------------------------------- Client commands */

    @Override
    public StatementType visitCmdClientCaching(RedisParser.CmdClientCachingContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_CACHING);
    }

    @Override
    public StatementType visitCmdClientGetname(RedisParser.CmdClientGetnameContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_GETNAME);
    }

    @Override
    public StatementType visitCmdClientGetredir(RedisParser.CmdClientGetredirContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_GETREDIR);
    }

    @Override
    public StatementType visitCmdClientID(RedisParser.CmdClientIDContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_ID);
    }

    @Override
    public StatementType visitCmdClientInfo(RedisParser.CmdClientInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_INFO);
    }

    @Override
    public StatementType visitCmdClientKill(RedisParser.CmdClientKillContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_KILL);
    }

    @Override
    public StatementType visitCmdClientList(RedisParser.CmdClientListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_LIST);
    }

    @Override
    public StatementType visitCmdClientNoEvict(RedisParser.CmdClientNoEvictContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_NO_EVICT);
    }

    @Override
    public StatementType visitCmdClientNoTouch(RedisParser.CmdClientNoTouchContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_NO_TOUCH);
    }

    @Override
    public StatementType visitCmdClientPause(RedisParser.CmdClientPauseContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_PAUSE);
    }

    @Override
    public StatementType visitCmdClientReply(RedisParser.CmdClientReplyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_REPLY);
    }

    @Override
    public StatementType visitCmdClientSetInfo(RedisParser.CmdClientSetInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_SETINFO);
    }

    @Override
    public StatementType visitCmdClientSetname(RedisParser.CmdClientSetnameContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_SETNAME);
    }

    @Override
    public StatementType visitCmdClientTracking(RedisParser.CmdClientTrackingContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_TRACKING);
    }

    @Override
    public StatementType visitCmdClientTrackingInfo(RedisParser.CmdClientTrackingInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_TRACKINGINFO);
    }

    @Override
    public StatementType visitCmdClientUnBlock(RedisParser.CmdClientUnBlockContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_UNBLOCK);
    }

    @Override
    public StatementType visitCmdClientUnPause(RedisParser.CmdClientUnPauseContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_UNPAUSE);
    }

    @Override
    public StatementType visitCmdAuth(RedisParser.CmdAuthContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.AUTH);
    }

    @Override
    public StatementType visitCmdEcho(RedisParser.CmdEchoContext ctx) {
        return StatementType.SELECT;
    }

    @Override
    public StatementType visitCmdHello(RedisParser.CmdHelloContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HELLO);
    }

    @Override
    public StatementType visitCmdPing(RedisParser.CmdPingContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PING);
    }

    @Override
    public StatementType visitCmdQuit(RedisParser.CmdQuitContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.QUIT);
    }

    @Override
    public StatementType visitCmdReset(RedisParser.CmdResetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RESET);
    }

    @Override
    public StatementType visitCmdSelect(RedisParser.CmdSelectContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SELECT);
    }
}
