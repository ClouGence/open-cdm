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
package com.clougence.sql.redis.parser;

import java.util.LinkedHashSet;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.redis.analysis.security.RedisAnalysisHelper;
import com.clougence.sql.redis.parser.antlr.RedisParser;
import com.clougence.sql.redis.parser.antlr.RedisParserBaseVisitor;
import com.clougence.sql.redis.parser.ast.RedisCmdType;

public class RedisSplitVisitor extends RedisParserBaseVisitor<SplitQueryType> {

    public static final RedisSplitVisitor INSTANCE = new RedisSplitVisitor();

    private SplitQueryType cmdTypeToSecQueryType(RedisCmdType cmdType) {
        return RedisAnalysisHelper.cmdTypeToSecQueryType(cmdType);
    }

    public Set<SplitQueryType> collectTypes(ParseTree tree) {
        LinkedHashSet<SplitQueryType> types = new LinkedHashSet<>();
        RedisParser.CmdBitFieldContext bitField = findContext(tree, RedisParser.CmdBitFieldContext.class);
        if (bitField != null) {
            for (RedisParser.BitFieldItemContext item : bitField.bitFieldItem()) {
                if (item.GET() != null) {
                    types.add(SplitQueryType.SELECT);
                } else {
                    types.add(SplitQueryType.MERGE);
                }
            }
            if (types.isEmpty()) {
                types.add(SplitQueryType.SELECT);
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

        SplitQueryType primary = tree.accept(this);
        types.add(primary == null ? SplitQueryType.UNKNOWN : primary);

        if (containsContext(tree, RedisParser.CmdCopyContext.class) || containsContext(tree, RedisParser.CmdSortContext.class)
            || containsContext(tree, RedisParser.CmdBitOPContext.class) || containsContext(tree, RedisParser.CmdSdiffstoreContext.class)
            || containsContext(tree, RedisParser.CmdSinterStoreContext.class) || containsContext(tree, RedisParser.CmdSunionstoreContext.class)
            || containsContext(tree, RedisParser.CmdZdiffStoreContext.class) || containsContext(tree, RedisParser.CmdZinterstoreContext.class)
            || containsContext(tree, RedisParser.CmdZrangestoreContext.class) || containsContext(tree, RedisParser.CmdZunionstoreContext.class)
            || containsContext(tree, RedisParser.CmdPFMergeContext.class)) {
            if (!containsContext(tree, RedisParser.CmdSortContext.class) || findContext(tree, RedisParser.CmdSortContext.class).destination != null) {
                types.add(SplitQueryType.SELECT);
            }
        }

        if (containsContext(tree, RedisParser.CmdMoveContext.class) || containsContext(tree, RedisParser.CmdBlmoveContext.class)
            || containsContext(tree, RedisParser.CmdBrpoplpushContext.class) || containsContext(tree, RedisParser.CmdLmoveContext.class)
            || containsContext(tree, RedisParser.CmdRpoplpushContext.class) || containsContext(tree, RedisParser.CmdSmoveContext.class)) {
            types.add(SplitQueryType.INSERT);
        }

        RedisParser.CmdFunctionRestoreContext functionRestore = findContext(tree, RedisParser.CmdFunctionRestoreContext.class);
        if (functionRestore != null && functionRestore.FLUSH() != null) {
            types.add(SplitQueryType.DROP_PROG_OBJ);
        }

        if (containsContext(tree, RedisParser.CmdModuleLoadContext.class) || containsContext(tree, RedisParser.CmdModuleLoadExContext.class)
            || containsContext(tree, RedisParser.CmdModuleUnloadContext.class) || containsContext(tree, RedisParser.CmdFunctionLoadContext.class)
            || containsContext(tree, RedisParser.CmdFunctionRestoreContext.class) || containsContext(tree, RedisParser.CmdScriptLoadContext.class)
            || containsContext(tree, RedisParser.CmdFlushAllContext.class) || containsContext(tree, RedisParser.CmdFlushDBContext.class)) {
            types.add(SplitQueryType.UNSAFE);
        }
        return types;
    }

    private static void collectAclRuleTypes(String rule, Set<SplitQueryType> types) {
        String normalized = rule.toLowerCase();
        if (normalized.startsWith("(")) {
            if (normalized.contains("+")) {
                types.add(SplitQueryType.GRANT);
            }
            if (normalized.contains("-")) {
                types.add(SplitQueryType.REVOKE);
            }
            return;
        }
        if (normalized.startsWith("-") || normalized.startsWith("<") || normalized.startsWith("!") || normalized.equals("reset") || normalized.equals("off")
            || normalized.equals("resetkeys") || normalized.equals("resetchannels") || normalized.equals("nocommands") || normalized.equals("clearselectors")) {
            types.add(SplitQueryType.REVOKE);
        }
        if (normalized.startsWith("+") || normalized.startsWith(">") || normalized.startsWith("#") || normalized.equals("on") || normalized.equals("allkeys")
            || normalized.equals("allchannels") || normalized.equals("allcommands") || normalized.equals("nopass") || normalized.startsWith("~") || normalized.startsWith("%")
            || normalized.startsWith("&")) {
            types.add(SplitQueryType.GRANT);
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

    public SplitQueryType visitChildren(RuleNode node) {
        int n = node.getChildCount();
        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            SplitQueryType result = c.accept(this);
            if (result != null) {
                return result;
            }
        }

        return SplitQueryType.UNKNOWN;
    }

    /* ----------------------------------------------------------------------------------- Keys commands */

    @Override
    public SplitQueryType visitCmdCopy(RedisParser.CmdCopyContext ctx) {
        return ctx.REPLACE() == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitCmdDel(RedisParser.CmdDelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DEL);
    }

    @Override
    public SplitQueryType visitCmdDump(RedisParser.CmdDumpContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DUMP);
    }

    @Override
    public SplitQueryType visitCmdExists(RedisParser.CmdExistsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXISTS);
    }

    @Override
    public SplitQueryType visitCmdExpire(RedisParser.CmdExpireContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXPIRE);
    }

    @Override
    public SplitQueryType visitCmdExpireat(RedisParser.CmdExpireatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXPIREAT);
    }

    @Override
    public SplitQueryType visitCmdExpireTime(RedisParser.CmdExpireTimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXPIRETIME);
    }

    @Override
    public SplitQueryType visitCmdKeys(RedisParser.CmdKeysContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.KEYS);
    }

    @Override
    public SplitQueryType visitCmdMove(RedisParser.CmdMoveContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitCmdObject(RedisParser.CmdObjectContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.OBJECT);
    }

    @Override
    public SplitQueryType visitCmdPersist(RedisParser.CmdPersistContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PERSIST);
    }

    @Override
    public SplitQueryType visitCmdPexpire(RedisParser.CmdPexpireContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PEXPIRE);
    }

    @Override
    public SplitQueryType visitCmdPexpireat(RedisParser.CmdPexpireatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PEXPIREAT);
    }

    @Override
    public SplitQueryType visitCmdPExpireTime(RedisParser.CmdPExpireTimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PEXPIRETIME);
    }

    @Override
    public SplitQueryType visitCmdTtl(RedisParser.CmdTtlContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.TTL);
    }

    @Override
    public SplitQueryType visitCmdPttl(RedisParser.CmdPttlContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PTTL);
    }

    @Override
    public SplitQueryType visitCmdRandomkey(RedisParser.CmdRandomkeyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RANDOMKEY);
    }

    @Override
    public SplitQueryType visitCmdRename(RedisParser.CmdRenameContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RENAME);
    }

    @Override
    public SplitQueryType visitCmdRenamenx(RedisParser.CmdRenamenxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RENAMENX);
    }

    @Override
    public SplitQueryType visitCmdRestore(RedisParser.CmdRestoreContext ctx) {
        return ctx.REPLACE() == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitCmdScan(RedisParser.CmdScanContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCAN);
    }

    @Override
    public SplitQueryType visitCmdSort(RedisParser.CmdSortContext ctx) {
        return ctx.destination == null ? SplitQueryType.SELECT : SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitCmdSortro(RedisParser.CmdSortroContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SORT_RO);
    }

    @Override
    public SplitQueryType visitCmdTouch(RedisParser.CmdTouchContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.TOUCH);
    }

    @Override
    public SplitQueryType visitCmdType(RedisParser.CmdTypeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.TYPE);
    }

    @Override
    public SplitQueryType visitCmdUnlink(RedisParser.CmdUnlinkContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.UNLINK);
    }

    @Override
    public SplitQueryType visitCmdWait(RedisParser.CmdWaitContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.WAIT);
    }

    @Override
    public SplitQueryType visitCmdWaitAOF(RedisParser.CmdWaitAOFContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.WAITAOF);
    }

    /* ----------------------------------------------------------------------------------- String commands */

    @Override
    public SplitQueryType visitCmdAppend(RedisParser.CmdAppendContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.APPEND);
    }

    @Override
    public SplitQueryType visitCmdDecr(RedisParser.CmdDecrContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DECR);
    }

    @Override
    public SplitQueryType visitCmdDecrby(RedisParser.CmdDecrbyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DECRBY);
    }

    @Override
    public SplitQueryType visitCmdGet(RedisParser.CmdGetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GET);
    }

    @Override
    public SplitQueryType visitCmdGetdel(RedisParser.CmdGetdelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETDEL);
    }

    @Override
    public SplitQueryType visitCmdGetex(RedisParser.CmdGetexContext ctx) {
        return ctx.ttlOpt() == null && ctx.PERSIST() == null ? SplitQueryType.SELECT : SplitQueryType.UPDATE;
    }

    @Override
    public SplitQueryType visitCmdGetrange(RedisParser.CmdGetrangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETRANGE);
    }

    @Override
    public SplitQueryType visitCmdGetset(RedisParser.CmdGetsetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETSET);
    }

    @Override
    public SplitQueryType visitCmdIncr(RedisParser.CmdIncrContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.INCR);
    }

    @Override
    public SplitQueryType visitCmdIncrby(RedisParser.CmdIncrbyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.INCRBY);
    }

    @Override
    public SplitQueryType visitCmdIncrbyFloat(RedisParser.CmdIncrbyFloatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.INCRBYFLOAT);
    }

    @Override
    public SplitQueryType visitCmdLcs(RedisParser.CmdLcsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LCS);
    }

    @Override
    public SplitQueryType visitCmdMget(RedisParser.CmdMgetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MGET);
    }

    @Override
    public SplitQueryType visitCmdMset(RedisParser.CmdMsetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MSET);
    }

    @Override
    public SplitQueryType visitCmdMsetnx(RedisParser.CmdMsetnxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MSETNX);
    }

    @Override
    public SplitQueryType visitCmdSetex(RedisParser.CmdSetexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SETEX);
    }

    @Override
    public SplitQueryType visitCmdPSetex(RedisParser.CmdPSetexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PSETEX);
    }

    @Override
    public SplitQueryType visitCmdSet(RedisParser.CmdSetContext ctx) {
        if (ctx.NX() != null) {
            return SplitQueryType.INSERT;
        }
        if (ctx.XX() != null) {
            return SplitQueryType.UPDATE;
        }
        return SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitCmdSetnx(RedisParser.CmdSetnxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SETNX);
    }

    @Override
    public SplitQueryType visitCmdSetrange(RedisParser.CmdSetrangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SETRANGE);
    }

    @Override
    public SplitQueryType visitCmdStrlen(RedisParser.CmdStrlenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.STRLEN);
    }

    @Override
    public SplitQueryType visitCmdSubstr(RedisParser.CmdSubstrContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUBSTR);
    }

    /* ----------------------------------------------------------------------------------- Bit commands */

    @Override
    public SplitQueryType visitCmdBitCount(RedisParser.CmdBitCountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITCOUNT);
    }

    @Override
    public SplitQueryType visitCmdBitField(RedisParser.CmdBitFieldContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITFIELD);
    }

    @Override
    public SplitQueryType visitCmdBitFieldRO(RedisParser.CmdBitFieldROContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITFIELD_RO);
    }

    @Override
    public SplitQueryType visitCmdBitOP(RedisParser.CmdBitOPContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITOP);
    }

    @Override
    public SplitQueryType visitCmdBitPos(RedisParser.CmdBitPosContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BITPOS);
    }

    @Override
    public SplitQueryType visitCmdGetbit(RedisParser.CmdGetbitContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.GETBIT);
    }

    @Override
    public SplitQueryType visitCmdSetbit(RedisParser.CmdSetbitContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SETBIT);
    }

    /* ----------------------------------------------------------------------------------- Hash commands */

    @Override
    public SplitQueryType visitCmdHdel(RedisParser.CmdHdelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HDEL);
    }

    @Override
    public SplitQueryType visitCmdHexists(RedisParser.CmdHexistsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HEXISTS);
    }

    @Override
    public SplitQueryType visitCmdHexpire(RedisParser.CmdHexpireContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HEXPIRE);
    }

    @Override
    public SplitQueryType visitCmdHexpireat(RedisParser.CmdHexpireatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HEXPIREAT);
    }

    @Override
    public SplitQueryType visitCmdHexpiretime(RedisParser.CmdHexpiretimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HEXPIRETIME);
    }

    @Override
    public SplitQueryType visitCmdHGet(RedisParser.CmdHGetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HGET);
    }

    @Override
    public SplitQueryType visitCmdHGetAll(RedisParser.CmdHGetAllContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HGETALL);
    }

    @Override
    public SplitQueryType visitCmdHgetDel(RedisParser.CmdHgetDelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HGETDEL);
    }

    @Override
    public SplitQueryType visitCmdHgetex(RedisParser.CmdHgetexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HGETEX);
    }

    @Override
    public SplitQueryType visitCmdHincrBy(RedisParser.CmdHincrByContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HINCRBY);
    }

    @Override
    public SplitQueryType visitCmdHincrByFloat(RedisParser.CmdHincrByFloatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HINCRBYFLOAT);
    }

    @Override
    public SplitQueryType visitCmdHKeys(RedisParser.CmdHKeysContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HKEYS);
    }

    @Override
    public SplitQueryType visitCmdHLen(RedisParser.CmdHLenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HLEN);
    }

    @Override
    public SplitQueryType visitCmdHMget(RedisParser.CmdHMgetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HMGET);
    }

    @Override
    public SplitQueryType visitCmdHMset(RedisParser.CmdHMsetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HMSET);
    }

    @Override
    public SplitQueryType visitCmdHPersist(RedisParser.CmdHPersistContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPERSIST);
    }

    @Override
    public SplitQueryType visitCmdHPexpire(RedisParser.CmdHPexpireContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPEXPIRE);
    }

    @Override
    public SplitQueryType visitCmdHPexpireAt(RedisParser.CmdHPexpireAtContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPEXPIREAT);
    }

    @Override
    public SplitQueryType visitCmdHPexpireTime(RedisParser.CmdHPexpireTimeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPEXPIRETIME);
    }

    @Override
    public SplitQueryType visitCmdHPTTL(RedisParser.CmdHPTTLContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HPTTL);
    }

    @Override
    public SplitQueryType visitCmdHTTL(RedisParser.CmdHTTLContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HTTL);
    }

    @Override
    public SplitQueryType visitCmdHrandfield(RedisParser.CmdHrandfieldContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HRANDFIELD);
    }

    @Override
    public SplitQueryType visitCmdHscan(RedisParser.CmdHscanContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSCAN);
    }

    @Override
    public SplitQueryType visitCmdHSet(RedisParser.CmdHSetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSET);
    }

    @Override
    public SplitQueryType visitCmdHSetex(RedisParser.CmdHSetexContext ctx) {
        if (ctx.FNX() != null) {
            return SplitQueryType.INSERT;
        }
        if (ctx.FXX() != null) {
            return SplitQueryType.UPDATE;
        }
        return SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitCmdHsetnx(RedisParser.CmdHsetnxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSETNX);
    }

    @Override
    public SplitQueryType visitCmdHStrLen(RedisParser.CmdHStrLenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HSTRLEN);
    }

    @Override
    public SplitQueryType visitCmdHVals(RedisParser.CmdHValsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HVALS);
    }

    /* ----------------------------------------------------------------------------------- List commands */

    @Override
    public SplitQueryType visitCmdBlmove(RedisParser.CmdBlmoveContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitCmdBLmpop(RedisParser.CmdBLmpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BLMPOP);
    }

    @Override
    public SplitQueryType visitCmdBLPop(RedisParser.CmdBLPopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BLPOP);
    }

    @Override
    public SplitQueryType visitCmdBRPop(RedisParser.CmdBRPopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BRPOP);
    }

    @Override
    public SplitQueryType visitCmdBrpoplpush(RedisParser.CmdBrpoplpushContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitCmdLindex(RedisParser.CmdLindexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LINDEX);
    }

    @Override
    public SplitQueryType visitCmdLinsert(RedisParser.CmdLinsertContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LINSERT);
    }

    @Override
    public SplitQueryType visitCmdLlen(RedisParser.CmdLlenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LLEN);
    }

    @Override
    public SplitQueryType visitCmdLmove(RedisParser.CmdLmoveContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitCmdLmpop(RedisParser.CmdLmpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LMPOP);
    }

    @Override
    public SplitQueryType visitCmdLPop(RedisParser.CmdLPopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LPOP);
    }

    @Override
    public SplitQueryType visitCmdLpos(RedisParser.CmdLposContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LPOS);
    }

    @Override
    public SplitQueryType visitCmdLPush(RedisParser.CmdLPushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LPUSH);
    }

    @Override
    public SplitQueryType visitCmdLPushx(RedisParser.CmdLPushxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LPUSHX);
    }

    @Override
    public SplitQueryType visitCmdLRange(RedisParser.CmdLRangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LRANGE);
    }

    @Override
    public SplitQueryType visitCmdLRem(RedisParser.CmdLRemContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LREM);
    }

    @Override
    public SplitQueryType visitCmdLSet(RedisParser.CmdLSetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LSET);
    }

    @Override
    public SplitQueryType visitCmdLTrim(RedisParser.CmdLTrimContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LTRIM);
    }

    @Override
    public SplitQueryType visitCmdRPop(RedisParser.CmdRPopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RPOP);
    }

    @Override
    public SplitQueryType visitCmdRpoplpush(RedisParser.CmdRpoplpushContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitCmdRPush(RedisParser.CmdRPushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RPUSH);
    }

    @Override
    public SplitQueryType visitCmdRPushx(RedisParser.CmdRPushxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RPUSHX);
    }

    /* ----------------------------------------------------------------------------------- Set commands */

    @Override
    public SplitQueryType visitCmdSadd(RedisParser.CmdSaddContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SADD);
    }

    @Override
    public SplitQueryType visitCmdScard(RedisParser.CmdScardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCARD);
    }

    @Override
    public SplitQueryType visitCmdSdiff(RedisParser.CmdSdiffContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SDIFF);
    }

    @Override
    public SplitQueryType visitCmdSdiffstore(RedisParser.CmdSdiffstoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SDIFFSTORE);
    }

    @Override
    public SplitQueryType visitCmdSinter(RedisParser.CmdSinterContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SINTER);
    }

    @Override
    public SplitQueryType visitCmdSinterCard(RedisParser.CmdSinterCardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SINTERCARD);
    }

    @Override
    public SplitQueryType visitCmdSinterStore(RedisParser.CmdSinterStoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SINTERSTORE);
    }

    @Override
    public SplitQueryType visitCmdSismember(RedisParser.CmdSismemberContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SISMEMBER);
    }

    @Override
    public SplitQueryType visitCmdSmembers(RedisParser.CmdSmembersContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SMEMBERS);
    }

    @Override
    public SplitQueryType visitCmdSmismember(RedisParser.CmdSmismemberContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SMISMEMBER);
    }

    @Override
    public SplitQueryType visitCmdSmove(RedisParser.CmdSmoveContext ctx) {
        return SplitQueryType.DELETE;
    }

    @Override
    public SplitQueryType visitCmdSpop(RedisParser.CmdSpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SPOP);
    }

    @Override
    public SplitQueryType visitCmdSrandmember(RedisParser.CmdSrandmemberContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SRANDMEMBER);
    }

    @Override
    public SplitQueryType visitCmdSrem(RedisParser.CmdSremContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SREM);
    }

    @Override
    public SplitQueryType visitCmdSscan(RedisParser.CmdSscanContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SSCAN);
    }

    @Override
    public SplitQueryType visitCmdSunion(RedisParser.CmdSunionContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUNION);
    }

    @Override
    public SplitQueryType visitCmdSunionstore(RedisParser.CmdSunionstoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUNIONSTORE);
    }

    /* ----------------------------------------------------------------------------------- sorted set commands */

    @Override
    public SplitQueryType visitCmdBzmpop(RedisParser.CmdBzmpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BZMPOP);
    }

    @Override
    public SplitQueryType visitCmdBzpopmax(RedisParser.CmdBzpopmaxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BZPOPMAX);
    }

    @Override
    public SplitQueryType visitCmdBzpopmin(RedisParser.CmdBzpopminContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BZPOPMIN);
    }

    @Override
    public SplitQueryType visitCmdZadd(RedisParser.CmdZaddContext ctx) {
        if (ctx.NX() != null) {
            return SplitQueryType.INSERT;
        }
        if (ctx.XX() != null) {
            return SplitQueryType.UPDATE;
        }
        return SplitQueryType.MERGE;
    }

    @Override
    public SplitQueryType visitCmdZcard(RedisParser.CmdZcardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZCARD);
    }

    @Override
    public SplitQueryType visitCmdZcount(RedisParser.CmdZcountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZCOUNT);
    }

    @Override
    public SplitQueryType visitCmdZdiff(RedisParser.CmdZdiffContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZDIFF);
    }

    @Override
    public SplitQueryType visitCmdZdiffStore(RedisParser.CmdZdiffStoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZDIFFSTORE);
    }

    @Override
    public SplitQueryType visitCmdZincrby(RedisParser.CmdZincrbyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZINCRBY);
    }

    @Override
    public SplitQueryType visitCmdZinter(RedisParser.CmdZinterContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZINTER);
    }

    @Override
    public SplitQueryType visitCmdZintercard(RedisParser.CmdZintercardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZINTERCARD);
    }

    @Override
    public SplitQueryType visitCmdZinterstore(RedisParser.CmdZinterstoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZINTERSTORE);
    }

    @Override
    public SplitQueryType visitCmdZLexCount(RedisParser.CmdZLexCountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZLEXCOUNT);
    }

    @Override
    public SplitQueryType visitCmdZmpop(RedisParser.CmdZmpopContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZMPOP);
    }

    @Override
    public SplitQueryType visitCmdZmscore(RedisParser.CmdZmscoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZMSCORE);
    }

    @Override
    public SplitQueryType visitCmdZpopmax(RedisParser.CmdZpopmaxContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZPOPMAX);
    }

    @Override
    public SplitQueryType visitCmdZpopmin(RedisParser.CmdZpopminContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZPOPMIN);
    }

    @Override
    public SplitQueryType visitCmdZrandmember(RedisParser.CmdZrandmemberContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANDMEMBER);
    }

    @Override
    public SplitQueryType visitCmdZrange(RedisParser.CmdZrangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANGE);
    }

    @Override
    public SplitQueryType visitCmdZrangebylex(RedisParser.CmdZrangebylexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANGEBYLEX);
    }

    @Override
    public SplitQueryType visitCmdZrangebyscore(RedisParser.CmdZrangebyscoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANGEBYSCORE);
    }

    @Override
    public SplitQueryType visitCmdZrangestore(RedisParser.CmdZrangestoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANGESTORE);
    }

    @Override
    public SplitQueryType visitCmdZrank(RedisParser.CmdZrankContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZRANK);
    }

    @Override
    public SplitQueryType visitCmdZRem(RedisParser.CmdZRemContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREM);
    }

    @Override
    public SplitQueryType visitCmdZRemRangeByLex(RedisParser.CmdZRemRangeByLexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREMRANGEBYLEX);
    }

    @Override
    public SplitQueryType visitCmdZremrangebyrank(RedisParser.CmdZremrangebyrankContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREMRANGEBYRANK);
    }

    @Override
    public SplitQueryType visitCmdZRemRangeByScore(RedisParser.CmdZRemRangeByScoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREMRANGEBYSCORE);
    }

    @Override
    public SplitQueryType visitCmdZrevrange(RedisParser.CmdZrevrangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREVRANGE);
    }

    @Override
    public SplitQueryType visitCmdZrevrangebylex(RedisParser.CmdZrevrangebylexContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREVRANGEBYLEX);
    }

    @Override
    public SplitQueryType visitCmdZrevrangebyscore(RedisParser.CmdZrevrangebyscoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREVRANGEBYSCORE);
    }

    @Override
    public SplitQueryType visitCmdZrevrank(RedisParser.CmdZrevrankContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZREVRANK);
    }

    @Override
    public SplitQueryType visitCmdZscan(RedisParser.CmdZscanContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZSCAN);
    }

    @Override
    public SplitQueryType visitCmdZscore(RedisParser.CmdZscoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZSCORE);
    }

    @Override
    public SplitQueryType visitCmdZunion(RedisParser.CmdZunionContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZUNION);
    }

    @Override
    public SplitQueryType visitCmdZunionstore(RedisParser.CmdZunionstoreContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ZUNIONSTORE);
    }

    /* ----------------------------------------------------------------------------------- script commands */

    @Override
    public SplitQueryType visitCmdScriptDebug(RedisParser.CmdScriptDebugContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_DEBUG);
    }

    @Override
    public SplitQueryType visitCmdScriptExists(RedisParser.CmdScriptExistsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_EXISTS);
    }

    @Override
    public SplitQueryType visitCmdScriptFlush(RedisParser.CmdScriptFlushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_FLUSH);
    }

    @Override
    public SplitQueryType visitCmdScriptKill(RedisParser.CmdScriptKillContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_KILL);
    }

    @Override
    public SplitQueryType visitCmdScriptLoad(RedisParser.CmdScriptLoadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SCRIPT_LOAD);
    }

    @Override
    public SplitQueryType visitCmdEval(RedisParser.CmdEvalContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EVAL);
    }

    @Override
    public SplitQueryType visitCmdEvalRO(RedisParser.CmdEvalROContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EVAL_RO);
    }

    @Override
    public SplitQueryType visitCmdEvalsha(RedisParser.CmdEvalshaContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EVALSHA);
    }

    @Override
    public SplitQueryType visitCmdEvalshaRO(RedisParser.CmdEvalshaROContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EVALSHA_RO);
    }

    @Override
    public SplitQueryType visitCmdFCall(RedisParser.CmdFCallContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FCALL);
    }

    @Override
    public SplitQueryType visitCmdFCallRO(RedisParser.CmdFCallROContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FCALL_RO);
    }

    @Override
    public SplitQueryType visitCmdFunctionDel(RedisParser.CmdFunctionDelContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_DEL);
    }

    @Override
    public SplitQueryType visitCmdFunctionDump(RedisParser.CmdFunctionDumpContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_DUMP);
    }

    @Override
    public SplitQueryType visitCmdFunctionFlush(RedisParser.CmdFunctionFlushContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_FLUSH);
    }

    @Override
    public SplitQueryType visitCmdFunctionKill(RedisParser.CmdFunctionKillContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_KILL);
    }

    @Override
    public SplitQueryType visitCmdFunctionList(RedisParser.CmdFunctionListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_LIST);
    }

    @Override
    public SplitQueryType visitCmdFunctionLoad(RedisParser.CmdFunctionLoadContext ctx) {
        return ctx.REPLACE() == null ? SplitQueryType.CREATE_PROG_OBJ : SplitQueryType.ALTER_PROG_OBJ;
    }

    @Override
    public SplitQueryType visitCmdFunctionRestore(RedisParser.CmdFunctionRestoreContext ctx) {
        return ctx.REPLACE() == null ? SplitQueryType.CREATE_PROG_OBJ : SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitCmdFunctionStats(RedisParser.CmdFunctionStatsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FUNCTION_STATS);
    }

    /* ----------------------------------------------------------------------------------- tx commands */

    @Override
    public SplitQueryType visitCmdDiscard(RedisParser.CmdDiscardContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DISCARD);
    }

    @Override
    public SplitQueryType visitCmdExec(RedisParser.CmdExecContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.EXEC);
    }

    @Override
    public SplitQueryType visitCmdMulti(RedisParser.CmdMultiContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MULTI);
    }

    @Override
    public SplitQueryType visitCmdUnwatch(RedisParser.CmdUnwatchContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.UNWATCH);
    }

    @Override
    public SplitQueryType visitCmdWatch(RedisParser.CmdWatchContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.WATCH);
    }

    /* ----------------------------------------------------------------------------------- HyperLog commands */

    @Override
    public SplitQueryType visitCmdPFAdd(RedisParser.CmdPFAddContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PFADD);
    }

    @Override
    public SplitQueryType visitCmdPFCount(RedisParser.CmdPFCountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PFCOUNT);
    }

    @Override
    public SplitQueryType visitCmdPFMerge(RedisParser.CmdPFMergeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PFMERGE);
    }

    /* ----------------------------------------------------------------------------------- publish commands */

    @Override
    public SplitQueryType visitCmdPSubscribe(RedisParser.CmdPSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PSUBSCRIBE);
    }

    @Override
    public SplitQueryType visitCmdPublish(RedisParser.CmdPublishContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBLISH);
    }

    @Override
    public SplitQueryType visitCmdPubSubChannels(RedisParser.CmdPubSubChannelsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_CHANNELS);
    }

    @Override
    public SplitQueryType visitCmdPubSubNumPat(RedisParser.CmdPubSubNumPatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_NUMPAT);
    }

    @Override
    public SplitQueryType visitCmdPubSubNumSub(RedisParser.CmdPubSubNumSubContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_NUMSUB);
    }

    @Override
    public SplitQueryType visitCmdPubSubShardChannels(RedisParser.CmdPubSubShardChannelsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_SHARDCHANNELS);
    }

    @Override
    public SplitQueryType visitCmdPubSubShardNumSub(RedisParser.CmdPubSubShardNumSubContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUBSUB_SHARDNUMSUB);
    }

    @Override
    public SplitQueryType visitCmdPunSubscribe(RedisParser.CmdPunSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PUNSUBSCRIBE);
    }

    @Override
    public SplitQueryType visitCmdSpublish(RedisParser.CmdSpublishContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SPUBLISH);
    }

    @Override
    public SplitQueryType visitCmdSSubscribe(RedisParser.CmdSSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SSUBSCRIBE);
    }

    @Override
    public SplitQueryType visitCmdSubscribe(RedisParser.CmdSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUBSCRIBE);
    }

    @Override
    public SplitQueryType visitCmdSunSubscribe(RedisParser.CmdSunSubscribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SUNSUBSCRIBE);
    }

    @Override
    public SplitQueryType visitCmdUnSubScribe(RedisParser.CmdUnSubScribeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.UNSUBSCRIBE);
    }

    /* ----------------------------------------------------------------------------------- cluster commands */

    @Override
    public SplitQueryType visitCmdAsking(RedisParser.CmdAskingContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ASKING);
    }

    @Override
    public SplitQueryType visitCmdReadonly(RedisParser.CmdReadonlyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.READONLY);
    }

    @Override
    public SplitQueryType visitCmdReadWrite(RedisParser.CmdReadWriteContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.READWRITE);
    }

    @Override
    public SplitQueryType visitCmdClusterAddSlots(RedisParser.CmdClusterAddSlotsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_ADDSLOTS);
    }

    @Override
    public SplitQueryType visitCmdClusterDelSlots(RedisParser.CmdClusterDelSlotsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_DELSLOTS);
    }

    @Override
    public SplitQueryType visitCmdClusterAddSlotsRange(RedisParser.CmdClusterAddSlotsRangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_ADDSLOTSRANGE);
    }

    @Override
    public SplitQueryType visitCmdClusterDelSlotsRange(RedisParser.CmdClusterDelSlotsRangeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_DELSLOTSRANGE);
    }

    @Override
    public SplitQueryType visitCmdClusterBumpEpoch(RedisParser.CmdClusterBumpEpochContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_BUMPEPOCH);
    }

    @Override
    public SplitQueryType visitCmdClusterCountFailureReports(RedisParser.CmdClusterCountFailureReportsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_COUNT_FAILURE_REPORTS);
    }

    @Override
    public SplitQueryType visitCmdClusterCountKeysInSlot(RedisParser.CmdClusterCountKeysInSlotContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_COUNTKEYSINSLOT);
    }

    @Override
    public SplitQueryType visitCmdClusterFailOver(RedisParser.CmdClusterFailOverContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_FAILOVER);
    }

    @Override
    public SplitQueryType visitCmdClusterFlushSlots(RedisParser.CmdClusterFlushSlotsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_FLUSHSLOTS);
    }

    @Override
    public SplitQueryType visitCmdClusterForget(RedisParser.CmdClusterForgetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_FORGET);
    }

    @Override
    public SplitQueryType visitCmdClusterGetKeysInSlot(RedisParser.CmdClusterGetKeysInSlotContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_GETKEYSINSLOT);
    }

    @Override
    public SplitQueryType visitCmdClusterInfo(RedisParser.CmdClusterInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_INFO);
    }

    @Override
    public SplitQueryType visitCmdClusterKeySlot(RedisParser.CmdClusterKeySlotContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitCmdClusterLinks(RedisParser.CmdClusterLinksContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_LINKS);
    }

    @Override
    public SplitQueryType visitCmdClusterMeet(RedisParser.CmdClusterMeetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_MEET);
    }

    @Override
    public SplitQueryType visitCmdClusterMyId(RedisParser.CmdClusterMyIdContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_MYID);
    }

    @Override
    public SplitQueryType visitCmdClusterMyShardId(RedisParser.CmdClusterMyShardIdContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_MYSHARDID);
    }

    @Override
    public SplitQueryType visitCmdClusterNodes(RedisParser.CmdClusterNodesContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_NODES);
    }

    @Override
    public SplitQueryType visitCmdClusterReplicas(RedisParser.CmdClusterReplicasContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_REPLICAS);
    }

    @Override
    public SplitQueryType visitCmdClusterReplicate(RedisParser.CmdClusterReplicateContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_REPLICATE);
    }

    @Override
    public SplitQueryType visitCmdClusterReset(RedisParser.CmdClusterResetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_RESET);
    }

    @Override
    public SplitQueryType visitCmdClusterSaveConfig(RedisParser.CmdClusterSaveConfigContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SAVECONFIG);
    }

    @Override
    public SplitQueryType visitCmdClusterSetConfigEpoch(RedisParser.CmdClusterSetConfigEpochContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SET_CONFIG_EPOCH);
    }

    @Override
    public SplitQueryType visitCmdClusterSetSlot(RedisParser.CmdClusterSetSlotContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SETSLOT);
    }

    @Override
    public SplitQueryType visitCmdClusterShards(RedisParser.CmdClusterShardsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SHARDS);
    }

    @Override
    public SplitQueryType visitCmdClusterSlaves(RedisParser.CmdClusterSlavesContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SLAVES);
    }

    @Override
    public SplitQueryType visitCmdClusterSlotStats(RedisParser.CmdClusterSlotStatsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SLOT_STATS);
    }

    @Override
    public SplitQueryType visitCmdClusterSlots(RedisParser.CmdClusterSlotsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLUSTER_SLOTS);
    }

    /* ----------------------------------------------------------------------------------- info commands */

    @Override
    public SplitQueryType visitCmdInfo(RedisParser.CmdInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.INFO);
    }

    /* ----------------------------------------------------------------------------------- acl commands */

    @Override
    public SplitQueryType visitCmdAclCat(RedisParser.CmdAclCatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_CAT);
    }

    @Override
    public SplitQueryType visitCmdAclDelUser(RedisParser.CmdAclDelUserContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_DELUSER);
    }

    @Override
    public SplitQueryType visitCmdAclDryRun(RedisParser.CmdAclDryRunContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_DRYRUN);
    }

    @Override
    public SplitQueryType visitCmdAclGenPass(RedisParser.CmdAclGenPassContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_GENPASS);
    }

    @Override
    public SplitQueryType visitCmdAclGetUser(RedisParser.CmdAclGetUserContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_GETUSER);
    }

    @Override
    public SplitQueryType visitCmdAclList(RedisParser.CmdAclListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_LIST);
    }

    @Override
    public SplitQueryType visitCmdAclLoad(RedisParser.CmdAclLoadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_LOAD);
    }

    @Override
    public SplitQueryType visitCmdAclLog(RedisParser.CmdAclLogContext ctx) {
        return ctx.RESET() == null ? SplitQueryType.LOG_READ : SplitQueryType.MAINTAIN_LOG;
    }

    @Override
    public SplitQueryType visitCmdAclSave(RedisParser.CmdAclSaveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_SAVE);
    }

    @Override
    public SplitQueryType visitCmdAclSetUser(RedisParser.CmdAclSetUserContext ctx) {
        return SplitQueryType.GRANT;
    }

    @Override
    public SplitQueryType visitCmdAclUsers(RedisParser.CmdAclUsersContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_USERS);
    }

    @Override
    public SplitQueryType visitCmdAclWhoami(RedisParser.CmdAclWhoamiContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ACL_WHOAMI);
    }

    /* -------------------------------------------------------------------------------- command commands */

    @Override
    public SplitQueryType visitCmdCommand(RedisParser.CmdCommandContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND);
    }

    @Override
    public SplitQueryType visitCmdCommandCount(RedisParser.CmdCommandCountContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_COUNT);
    }

    @Override
    public SplitQueryType visitCmdCommandDocs(RedisParser.CmdCommandDocsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_DOCS);
    }

    @Override
    public SplitQueryType visitCmdCommandGetKeys(RedisParser.CmdCommandGetKeysContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_GETKEYS);
    }

    @Override
    public SplitQueryType visitCmdCommandGetKeysAndFlags(RedisParser.CmdCommandGetKeysAndFlagsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_GETKEYSANDFLAGS);
    }

    @Override
    public SplitQueryType visitCmdCommandInfo(RedisParser.CmdCommandInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_INFO);
    }

    @Override
    public SplitQueryType visitCmdCommandList(RedisParser.CmdCommandListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.COMMAND_LIST);
    }

    /* ----------------------------------------------------------------------------------- config commands */

    @Override
    public SplitQueryType visitCmdConfigGet(RedisParser.CmdConfigGetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CONFIG_GET);
    }

    @Override
    public SplitQueryType visitCmdConfigSet(RedisParser.CmdConfigSetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CONFIG_SET);
    }

    @Override
    public SplitQueryType visitCmdConfigResetStat(RedisParser.CmdConfigResetStatContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CONFIG_RESETSTAT);
    }

    @Override
    public SplitQueryType visitCmdConfigRewrite(RedisParser.CmdConfigRewriteContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CONFIG_REWRITE);
    }

    /* ----------------------------------------------------------------------------------- latency commands */

    @Override
    public SplitQueryType visitCmdLatencyDoctor(RedisParser.CmdLatencyDoctorContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_DOCTOR);
    }

    @Override
    public SplitQueryType visitCmdLatencyGraph(RedisParser.CmdLatencyGraphContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_GRAPH);
    }

    @Override
    public SplitQueryType visitCmdLatencyHistogram(RedisParser.CmdLatencyHistogramContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_HISTOGRAM);
    }

    @Override
    public SplitQueryType visitCmdLatencyHistory(RedisParser.CmdLatencyHistoryContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_HISTORY);
    }

    @Override
    public SplitQueryType visitCmdLatencyLatest(RedisParser.CmdLatencyLatestContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_LATEST);
    }

    @Override
    public SplitQueryType visitCmdLatencyReset(RedisParser.CmdLatencyResetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LATENCY_RESET);
    }

    /* ----------------------------------------------------------------------------------- memory commands */

    @Override
    public SplitQueryType visitCmdMemoryDoctor(RedisParser.CmdMemoryDoctorContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_DOCTOR);
    }

    @Override
    public SplitQueryType visitCmdMemoryMallocStats(RedisParser.CmdMemoryMallocStatsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_MALLOC_STATS);
    }

    @Override
    public SplitQueryType visitCmdMemoryPurge(RedisParser.CmdMemoryPurgeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_PURGE);
    }

    @Override
    public SplitQueryType visitCmdMemoryStats(RedisParser.CmdMemoryStatsContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_STATS);
    }

    @Override
    public SplitQueryType visitCmdMemoryUsage(RedisParser.CmdMemoryUsageContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MEMORY_USAGE);
    }

    /* ----------------------------------------------------------------------------------- module commands */

    @Override
    public SplitQueryType visitCmdModuleList(RedisParser.CmdModuleListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MODULE_LIST);
    }

    @Override
    public SplitQueryType visitCmdModuleLoad(RedisParser.CmdModuleLoadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MODULE_LOAD);
    }

    @Override
    public SplitQueryType visitCmdModuleLoadEx(RedisParser.CmdModuleLoadExContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MODULE_LOADEX);
    }

    @Override
    public SplitQueryType visitCmdModuleUnload(RedisParser.CmdModuleUnloadContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MODULE_UNLOAD);
    }

    /* ----------------------------------------------------------------------------------- control commands */

    @Override
    public SplitQueryType visitCmdBgrewriteaof(RedisParser.CmdBgrewriteaofContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BGREWRITEAOF);
    }

    @Override
    public SplitQueryType visitCmdBgsave(RedisParser.CmdBgsaveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.BGSAVE);
    }

    @Override
    public SplitQueryType visitCmdDbsize(RedisParser.CmdDbsizeContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.DBSIZE);
    }

    @Override
    public SplitQueryType visitCmdFailover(RedisParser.CmdFailoverContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FAILOVER);
    }

    @Override
    public SplitQueryType visitCmdFlushAll(RedisParser.CmdFlushAllContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FLUSHALL);
    }

    @Override
    public SplitQueryType visitCmdFlushDB(RedisParser.CmdFlushDBContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.FLUSHDB);
    }

    @Override
    public SplitQueryType visitCmdLastsave(RedisParser.CmdLastsaveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.LASTSAVE);
    }

    @Override
    public SplitQueryType visitCmdLolwut(RedisParser.CmdLolwutContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitCmdMonitor(RedisParser.CmdMonitorContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.MONITOR);
    }

    @Override
    public SplitQueryType visitCmdPSync(RedisParser.CmdPSyncContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PSYNC);
    }

    @Override
    public SplitQueryType visitCmdReplicaOf(RedisParser.CmdReplicaOfContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.REPLICAOF);
    }

    @Override
    public SplitQueryType visitCmdRole(RedisParser.CmdRoleContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.ROLE);
    }

    @Override
    public SplitQueryType visitCmdSave(RedisParser.CmdSaveContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SAVE);
    }

    @Override
    public SplitQueryType visitCmdShutdown(RedisParser.CmdShutdownContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SHUTDOWN);
    }

    @Override
    public SplitQueryType visitCmdSlaveOf(RedisParser.CmdSlaveOfContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SLAVEOF);
    }

    @Override
    public SplitQueryType visitCmdSlowlogGet(RedisParser.CmdSlowlogGetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SLOWLOG_GET);
    }

    @Override
    public SplitQueryType visitCmdSlowlogLen(RedisParser.CmdSlowlogLenContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SLOWLOG_LEN);
    }

    @Override
    public SplitQueryType visitCmdSlowlogReset(RedisParser.CmdSlowlogResetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SLOWLOG_RESET);
    }

    @Override
    public SplitQueryType visitCmdSwapDB(RedisParser.CmdSwapDBContext ctx) {
        return SplitQueryType.UNSAFE;
    }

    @Override
    public SplitQueryType visitCmdSync(RedisParser.CmdSyncContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SYNC);
    }

    @Override
    public SplitQueryType visitCmdTime(RedisParser.CmdTimeContext ctx) {
        return SplitQueryType.SELECT;
    }

    /* ----------------------------------------------------------------------------------- Client commands */

    @Override
    public SplitQueryType visitCmdClientCaching(RedisParser.CmdClientCachingContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_CACHING);
    }

    @Override
    public SplitQueryType visitCmdClientGetname(RedisParser.CmdClientGetnameContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_GETNAME);
    }

    @Override
    public SplitQueryType visitCmdClientGetredir(RedisParser.CmdClientGetredirContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_GETREDIR);
    }

    @Override
    public SplitQueryType visitCmdClientID(RedisParser.CmdClientIDContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_ID);
    }

    @Override
    public SplitQueryType visitCmdClientInfo(RedisParser.CmdClientInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_INFO);
    }

    @Override
    public SplitQueryType visitCmdClientKill(RedisParser.CmdClientKillContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_KILL);
    }

    @Override
    public SplitQueryType visitCmdClientList(RedisParser.CmdClientListContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_LIST);
    }

    @Override
    public SplitQueryType visitCmdClientNoEvict(RedisParser.CmdClientNoEvictContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_NO_EVICT);
    }

    @Override
    public SplitQueryType visitCmdClientNoTouch(RedisParser.CmdClientNoTouchContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_NO_TOUCH);
    }

    @Override
    public SplitQueryType visitCmdClientPause(RedisParser.CmdClientPauseContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_PAUSE);
    }

    @Override
    public SplitQueryType visitCmdClientReply(RedisParser.CmdClientReplyContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_REPLY);
    }

    @Override
    public SplitQueryType visitCmdClientSetInfo(RedisParser.CmdClientSetInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_SETINFO);
    }

    @Override
    public SplitQueryType visitCmdClientSetname(RedisParser.CmdClientSetnameContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_SETNAME);
    }

    @Override
    public SplitQueryType visitCmdClientTracking(RedisParser.CmdClientTrackingContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_TRACKING);
    }

    @Override
    public SplitQueryType visitCmdClientTrackingInfo(RedisParser.CmdClientTrackingInfoContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_TRACKINGINFO);
    }

    @Override
    public SplitQueryType visitCmdClientUnBlock(RedisParser.CmdClientUnBlockContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_UNBLOCK);
    }

    @Override
    public SplitQueryType visitCmdClientUnPause(RedisParser.CmdClientUnPauseContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.CLIENT_UNPAUSE);
    }

    @Override
    public SplitQueryType visitCmdAuth(RedisParser.CmdAuthContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.AUTH);
    }

    @Override
    public SplitQueryType visitCmdEcho(RedisParser.CmdEchoContext ctx) {
        return SplitQueryType.SELECT;
    }

    @Override
    public SplitQueryType visitCmdHello(RedisParser.CmdHelloContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.HELLO);
    }

    @Override
    public SplitQueryType visitCmdPing(RedisParser.CmdPingContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.PING);
    }

    @Override
    public SplitQueryType visitCmdQuit(RedisParser.CmdQuitContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.QUIT);
    }

    @Override
    public SplitQueryType visitCmdReset(RedisParser.CmdResetContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.RESET);
    }

    @Override
    public SplitQueryType visitCmdSelect(RedisParser.CmdSelectContext ctx) {
        return this.cmdTypeToSecQueryType(RedisCmdType.SELECT);
    }
}
