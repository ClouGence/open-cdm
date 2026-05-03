package com.clougence.clouddm.ds.redis.analysis;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.redis.analysis.builder.RedisBuilderFactory;
import com.clougence.clouddm.ds.redis.analysis.secrules.RedisCmdDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.redis.parser.antlr.RedisParser;
import com.clougence.clouddm.ds.redis.parser.antlr.RedisParserBaseVisitor;
import com.clougence.clouddm.ds.redis.parser.ast.RedisCmdType;

public class RedisParserVisitor extends RedisParserBaseVisitor<Void> {

    protected final RedisBuilderFactory builder;
    private final Parser                parser;

    public RedisParserVisitor(RedisBuilderFactory builder, Parser parser){
        this.builder = builder;
        this.parser = parser;
    }

    public void dmVisitChildren(RuleNode node) {
        int n = node.getChildCount();

        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            c.accept(this);
        }
    }

    private void buildCmdDomain(RedisCmdType cmdType, RuleNode ctx) {
        this.buildCmdDomain(cmdType, ctx, null);
    }

    private void buildCmdDomain(RedisCmdType cmdType, RuleNode ctx, String schema) {
        String cmdStr = cmdType.getCommandStr().toUpperCase();
        String kindStr = cmdType.getKindType().getType().toUpperCase();
        SecQueryType queryType = RedisAnalysisHelper.cmdTypeToSecQueryType(cmdType);

        RedisCmdDomain domain = new RedisCmdDomain(cmdStr, kindStr);
        domain.setAuditKind(queryType.getAuditKind());
        domain.setSqlType(queryType);
        domain.setSchema(schema);
        this.builder.addDomain(domain);

        dmVisitChildren(ctx);
    }

    //    private AccessType convertTo(RedisCmdType cmdType) {
    //        SecQueryType queryType = RedisAnalysisHelper.cmdTypeToSecQueryType(cmdType);
    //        switch (queryType.getAuthKind()) {
    //            case READ:
    //                return AccessType.READ;
    //            case WRITE:
    //                return AccessType.WRITE;
    //            case ADMIN:
    //                return AccessType.ADMIN;
    //            default:
    //                return AccessType.OTHER;
    //        }
    //    }
    //    private void buildKeyDomain(RedisCmdType cmdType, RuleNode ctx, String key) {
    //        this.buildKeyDomain(cmdType, ctx, key, this.convertTo(cmdType));
    //    }
    //
    //    private void buildKeyDomain(RedisCmdType cmdType, RuleNode ctx, String key, AccessType accessType) {
    //        String cmdStr = cmdType.getCommandStr().toUpperCase();
    //        String kindStr = cmdType.getKindType().getType().toUpperCase();
    //        SecQueryType queryType = RedisAnalysisHelper.cmdTypeToSecQueryType(cmdType);
    //
    //        RedisKeyDomain domain = new RedisKeyDomain(cmdStr, kindStr);
    //        domain.setSqlKind(queryType.getAuditKind());
    //        domain.setSqlType(queryType);
    //        domain.setKey(key);
    //        domain.setAccessType(accessType);
    //        this.builder.addDomain(domain);
    //
    //        dmVisitChildren(ctx);
    //    }

    /* ----------------------------------------------------------------------------------- Keys commands */

    @Override
    public Void visitCmdCopy(RedisParser.CmdCopyContext ctx) {
        this.buildCmdDomain(RedisCmdType.COPY, ctx);

        if (ctx.db != null) {
            this.buildCmdDomain(RedisCmdType.COPY, ctx, ctx.db.getText());
        }
        return null;
    }

    @Override
    public Void visitCmdDel(RedisParser.CmdDelContext ctx) {
        this.buildCmdDomain(RedisCmdType.DEL, ctx);
        return null;
    }

    @Override
    public Void visitCmdDump(RedisParser.CmdDumpContext ctx) {
        this.buildCmdDomain(RedisCmdType.DUMP, ctx);
        return null;
    }

    @Override
    public Void visitCmdExists(RedisParser.CmdExistsContext ctx) {
        this.buildCmdDomain(RedisCmdType.EXISTS, ctx);
        return null;
    }

    @Override
    public Void visitCmdExpire(RedisParser.CmdExpireContext ctx) {
        this.buildCmdDomain(RedisCmdType.EXPIRE, ctx);
        return null;
    }

    @Override
    public Void visitCmdExpireat(RedisParser.CmdExpireatContext ctx) {
        this.buildCmdDomain(RedisCmdType.EXPIREAT, ctx);
        return null;
    }

    @Override
    public Void visitCmdExpireTime(RedisParser.CmdExpireTimeContext ctx) {
        this.buildCmdDomain(RedisCmdType.EXPIRETIME, ctx);
        return null;
    }

    @Override
    public Void visitCmdKeys(RedisParser.CmdKeysContext ctx) {
        this.buildCmdDomain(RedisCmdType.KEYS, ctx);
        return null;
    }

    @Override
    public Void visitCmdMove(RedisParser.CmdMoveContext ctx) {
        this.buildCmdDomain(RedisCmdType.MOVE, ctx);
        return null;
    }

    @Override
    public Void visitCmdObject(RedisParser.CmdObjectContext ctx) {
        this.buildCmdDomain(RedisCmdType.OBJECT, ctx);
        return null;
    }

    @Override
    public Void visitCmdPersist(RedisParser.CmdPersistContext ctx) {
        this.buildCmdDomain(RedisCmdType.PERSIST, ctx);
        return null;
    }

    @Override
    public Void visitCmdPexpire(RedisParser.CmdPexpireContext ctx) {
        this.buildCmdDomain(RedisCmdType.PEXPIRE, ctx);
        return null;
    }

    @Override
    public Void visitCmdPexpireat(RedisParser.CmdPexpireatContext ctx) {
        this.buildCmdDomain(RedisCmdType.PEXPIREAT, ctx);
        return null;
    }

    @Override
    public Void visitCmdPExpireTime(RedisParser.CmdPExpireTimeContext ctx) {
        this.buildCmdDomain(RedisCmdType.PEXPIRETIME, ctx);
        return null;
    }

    @Override
    public Void visitCmdTtl(RedisParser.CmdTtlContext ctx) {
        this.buildCmdDomain(RedisCmdType.TTL, ctx);
        return null;
    }

    @Override
    public Void visitCmdPttl(RedisParser.CmdPttlContext ctx) {
        this.buildCmdDomain(RedisCmdType.PTTL, ctx);
        return null;
    }

    @Override
    public Void visitCmdRandomkey(RedisParser.CmdRandomkeyContext ctx) {
        this.buildCmdDomain(RedisCmdType.RANDOMKEY, ctx);
        return null;
    }

    @Override
    public Void visitCmdRename(RedisParser.CmdRenameContext ctx) {
        this.buildCmdDomain(RedisCmdType.RENAME, ctx);
        return null;
    }

    @Override
    public Void visitCmdRenamenx(RedisParser.CmdRenamenxContext ctx) {
        this.buildCmdDomain(RedisCmdType.RENAMENX, ctx);
        return null;
    }

    @Override
    public Void visitCmdRestore(RedisParser.CmdRestoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.RESTORE, ctx);
        return null;
    }

    @Override
    public Void visitCmdScan(RedisParser.CmdScanContext ctx) {
        this.buildCmdDomain(RedisCmdType.SCAN, ctx);
        return null;
    }

    @Override
    public Void visitCmdSort(RedisParser.CmdSortContext ctx) {
        this.buildCmdDomain(RedisCmdType.SORT, ctx);
        return null;
    }

    @Override
    public Void visitCmdSortro(RedisParser.CmdSortroContext ctx) {
        this.buildCmdDomain(RedisCmdType.SORT_RO, ctx);
        return null;
    }

    @Override
    public Void visitCmdTouch(RedisParser.CmdTouchContext ctx) {
        this.buildCmdDomain(RedisCmdType.TOUCH, ctx);
        return null;
    }

    @Override
    public Void visitCmdType(RedisParser.CmdTypeContext ctx) {
        this.buildCmdDomain(RedisCmdType.TYPE, ctx);
        return null;
    }

    @Override
    public Void visitCmdUnlink(RedisParser.CmdUnlinkContext ctx) {
        this.buildCmdDomain(RedisCmdType.UNLINK, ctx);
        return null;
    }

    @Override
    public Void visitCmdWait(RedisParser.CmdWaitContext ctx) {
        this.buildCmdDomain(RedisCmdType.WAIT, ctx);
        return null;
    }

    @Override
    public Void visitCmdWaitAOF(RedisParser.CmdWaitAOFContext ctx) {
        this.buildCmdDomain(RedisCmdType.WAITAOF, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- String commands */

    @Override
    public Void visitCmdAppend(RedisParser.CmdAppendContext ctx) {
        this.buildCmdDomain(RedisCmdType.APPEND, ctx);
        return null;
    }

    @Override
    public Void visitCmdDecr(RedisParser.CmdDecrContext ctx) {
        this.buildCmdDomain(RedisCmdType.DECR, ctx);
        return null;
    }

    @Override
    public Void visitCmdDecrby(RedisParser.CmdDecrbyContext ctx) {
        this.buildCmdDomain(RedisCmdType.DECRBY, ctx);
        return null;
    }

    @Override
    public Void visitCmdGet(RedisParser.CmdGetContext ctx) {
        this.buildCmdDomain(RedisCmdType.GET, ctx);
        return null;
    }

    @Override
    public Void visitCmdGetdel(RedisParser.CmdGetdelContext ctx) {
        this.buildCmdDomain(RedisCmdType.GETDEL, ctx);
        return null;
    }

    @Override
    public Void visitCmdGetex(RedisParser.CmdGetexContext ctx) {
        this.buildCmdDomain(RedisCmdType.GETEX, ctx);
        return null;
    }

    @Override
    public Void visitCmdGetrange(RedisParser.CmdGetrangeContext ctx) {
        this.buildCmdDomain(RedisCmdType.GETRANGE, ctx);
        return null;
    }

    @Override
    public Void visitCmdGetset(RedisParser.CmdGetsetContext ctx) {
        this.buildCmdDomain(RedisCmdType.GETSET, ctx);
        return null;
    }

    @Override
    public Void visitCmdIncr(RedisParser.CmdIncrContext ctx) {
        this.buildCmdDomain(RedisCmdType.INCR, ctx);
        return null;
    }

    @Override
    public Void visitCmdIncrby(RedisParser.CmdIncrbyContext ctx) {
        this.buildCmdDomain(RedisCmdType.INCRBY, ctx);
        return null;
    }

    @Override
    public Void visitCmdIncrbyFloat(RedisParser.CmdIncrbyFloatContext ctx) {
        this.buildCmdDomain(RedisCmdType.INCRBYFLOAT, ctx);
        return null;
    }

    @Override
    public Void visitCmdLcs(RedisParser.CmdLcsContext ctx) {
        this.buildCmdDomain(RedisCmdType.LCS, ctx);
        return null;
    }

    @Override
    public Void visitCmdMget(RedisParser.CmdMgetContext ctx) {
        this.buildCmdDomain(RedisCmdType.MGET, ctx);
        return null;
    }

    @Override
    public Void visitCmdMset(RedisParser.CmdMsetContext ctx) {
        this.buildCmdDomain(RedisCmdType.MSET, ctx);
        return null;
    }

    @Override
    public Void visitCmdMsetnx(RedisParser.CmdMsetnxContext ctx) {
        this.buildCmdDomain(RedisCmdType.MSETNX, ctx);
        return null;
    }

    @Override
    public Void visitCmdSetex(RedisParser.CmdSetexContext ctx) {
        this.buildCmdDomain(RedisCmdType.SETEX, ctx);
        return null;
    }

    @Override
    public Void visitCmdPSetex(RedisParser.CmdPSetexContext ctx) {
        this.buildCmdDomain(RedisCmdType.PSETEX, ctx);
        return null;
    }

    @Override
    public Void visitCmdSet(RedisParser.CmdSetContext ctx) {
        this.buildCmdDomain(RedisCmdType.SET, ctx);
        return null;
    }

    @Override
    public Void visitCmdSetnx(RedisParser.CmdSetnxContext ctx) {
        this.buildCmdDomain(RedisCmdType.SETNX, ctx);
        return null;
    }

    @Override
    public Void visitCmdSetrange(RedisParser.CmdSetrangeContext ctx) {
        this.buildCmdDomain(RedisCmdType.SETRANGE, ctx);
        return null;
    }

    @Override
    public Void visitCmdStrlen(RedisParser.CmdStrlenContext ctx) {
        this.buildCmdDomain(RedisCmdType.STRLEN, ctx);
        return null;
    }

    @Override
    public Void visitCmdSubstr(RedisParser.CmdSubstrContext ctx) {
        this.buildCmdDomain(RedisCmdType.SUBSTR, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- Bit commands */

    @Override
    public Void visitCmdBitCount(RedisParser.CmdBitCountContext ctx) {
        this.buildCmdDomain(RedisCmdType.BITCOUNT, ctx);
        return null;
    }

    @Override
    public Void visitCmdBitField(RedisParser.CmdBitFieldContext ctx) {
        this.buildCmdDomain(RedisCmdType.BITFIELD, ctx);
        return null;
    }

    @Override
    public Void visitCmdBitFieldRO(RedisParser.CmdBitFieldROContext ctx) {
        this.buildCmdDomain(RedisCmdType.BITFIELD_RO, ctx);
        return null;
    }

    @Override
    public Void visitCmdBitOP(RedisParser.CmdBitOPContext ctx) {
        this.buildCmdDomain(RedisCmdType.BITOP, ctx);
        return null;
    }

    @Override
    public Void visitCmdBitPos(RedisParser.CmdBitPosContext ctx) {
        this.buildCmdDomain(RedisCmdType.BITPOS, ctx);
        return null;
    }

    @Override
    public Void visitCmdGetbit(RedisParser.CmdGetbitContext ctx) {
        this.buildCmdDomain(RedisCmdType.GETBIT, ctx);
        return null;
    }

    @Override
    public Void visitCmdSetbit(RedisParser.CmdSetbitContext ctx) {
        this.buildCmdDomain(RedisCmdType.SETBIT, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- Hash commands */

    @Override
    public Void visitCmdHdel(RedisParser.CmdHdelContext ctx) {
        this.buildCmdDomain(RedisCmdType.HDEL, ctx);
        return null;
    }

    @Override
    public Void visitCmdHexists(RedisParser.CmdHexistsContext ctx) {
        this.buildCmdDomain(RedisCmdType.HEXISTS, ctx);
        return null;
    }

    @Override
    public Void visitCmdHexpire(RedisParser.CmdHexpireContext ctx) {
        this.buildCmdDomain(RedisCmdType.HEXPIRE, ctx);
        return null;
    }

    @Override
    public Void visitCmdHexpireat(RedisParser.CmdHexpireatContext ctx) {
        this.buildCmdDomain(RedisCmdType.HEXPIREAT, ctx);
        return null;
    }

    @Override
    public Void visitCmdHexpiretime(RedisParser.CmdHexpiretimeContext ctx) {
        this.buildCmdDomain(RedisCmdType.HEXPIRETIME, ctx);
        return null;
    }

    @Override
    public Void visitCmdHGet(RedisParser.CmdHGetContext ctx) {
        this.buildCmdDomain(RedisCmdType.HGET, ctx);
        return null;
    }

    @Override
    public Void visitCmdHGetAll(RedisParser.CmdHGetAllContext ctx) {
        this.buildCmdDomain(RedisCmdType.HGETALL, ctx);
        return null;
    }

    @Override
    public Void visitCmdHgetDel(RedisParser.CmdHgetDelContext ctx) {
        this.buildCmdDomain(RedisCmdType.HGETDEL, ctx);
        return null;
    }

    @Override
    public Void visitCmdHgetex(RedisParser.CmdHgetexContext ctx) {
        this.buildCmdDomain(RedisCmdType.HGETEX, ctx);
        return null;
    }

    @Override
    public Void visitCmdHincrBy(RedisParser.CmdHincrByContext ctx) {
        this.buildCmdDomain(RedisCmdType.HINCRBY, ctx);
        return null;
    }

    @Override
    public Void visitCmdHincrByFloat(RedisParser.CmdHincrByFloatContext ctx) {
        this.buildCmdDomain(RedisCmdType.HINCRBYFLOAT, ctx);
        return null;
    }

    @Override
    public Void visitCmdHKeys(RedisParser.CmdHKeysContext ctx) {
        this.buildCmdDomain(RedisCmdType.HKEYS, ctx);
        return null;
    }

    @Override
    public Void visitCmdHLen(RedisParser.CmdHLenContext ctx) {
        this.buildCmdDomain(RedisCmdType.HLEN, ctx);
        return null;
    }

    @Override
    public Void visitCmdHMget(RedisParser.CmdHMgetContext ctx) {
        this.buildCmdDomain(RedisCmdType.HMGET, ctx);
        return null;
    }

    @Override
    public Void visitCmdHMset(RedisParser.CmdHMsetContext ctx) {
        this.buildCmdDomain(RedisCmdType.HMSET, ctx);
        return null;
    }

    @Override
    public Void visitCmdHPersist(RedisParser.CmdHPersistContext ctx) {
        this.buildCmdDomain(RedisCmdType.HPERSIST, ctx);
        return null;
    }

    @Override
    public Void visitCmdHPexpire(RedisParser.CmdHPexpireContext ctx) {
        this.buildCmdDomain(RedisCmdType.HPEXPIRE, ctx);
        return null;
    }

    @Override
    public Void visitCmdHPexpireAt(RedisParser.CmdHPexpireAtContext ctx) {
        this.buildCmdDomain(RedisCmdType.HPEXPIREAT, ctx);
        return null;
    }

    @Override
    public Void visitCmdHPexpireTime(RedisParser.CmdHPexpireTimeContext ctx) {
        this.buildCmdDomain(RedisCmdType.HPEXPIRETIME, ctx);
        return null;
    }

    @Override
    public Void visitCmdHPTTL(RedisParser.CmdHPTTLContext ctx) {
        this.buildCmdDomain(RedisCmdType.HPTTL, ctx);
        return null;
    }

    @Override
    public Void visitCmdHTTL(RedisParser.CmdHTTLContext ctx) {
        this.buildCmdDomain(RedisCmdType.HTTL, ctx);
        return null;
    }

    @Override
    public Void visitCmdHrandfield(RedisParser.CmdHrandfieldContext ctx) {
        this.buildCmdDomain(RedisCmdType.HRANDFIELD, ctx);
        return null;
    }

    @Override
    public Void visitCmdHscan(RedisParser.CmdHscanContext ctx) {
        this.buildCmdDomain(RedisCmdType.HSCAN, ctx);
        return null;
    }

    @Override
    public Void visitCmdHSet(RedisParser.CmdHSetContext ctx) {
        this.buildCmdDomain(RedisCmdType.HSET, ctx);
        return null;
    }

    @Override
    public Void visitCmdHSetex(RedisParser.CmdHSetexContext ctx) {
        this.buildCmdDomain(RedisCmdType.HSETEX, ctx);
        return null;
    }

    @Override
    public Void visitCmdHsetnx(RedisParser.CmdHsetnxContext ctx) {
        this.buildCmdDomain(RedisCmdType.HSETNX, ctx);
        return null;
    }

    @Override
    public Void visitCmdHStrLen(RedisParser.CmdHStrLenContext ctx) {
        this.buildCmdDomain(RedisCmdType.HSTRLEN, ctx);
        return null;
    }

    @Override
    public Void visitCmdHVals(RedisParser.CmdHValsContext ctx) {
        this.buildCmdDomain(RedisCmdType.HVALS, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- List commands */

    @Override
    public Void visitCmdBlmove(RedisParser.CmdBlmoveContext ctx) {
        this.buildCmdDomain(RedisCmdType.BLMOVE, ctx);
        return null;
    }

    @Override
    public Void visitCmdBLmpop(RedisParser.CmdBLmpopContext ctx) {
        this.buildCmdDomain(RedisCmdType.BLMPOP, ctx);
        return null;
    }

    @Override
    public Void visitCmdBLPop(RedisParser.CmdBLPopContext ctx) {
        this.buildCmdDomain(RedisCmdType.BLPOP, ctx);
        return null;
    }

    @Override
    public Void visitCmdBRPop(RedisParser.CmdBRPopContext ctx) {
        this.buildCmdDomain(RedisCmdType.BRPOP, ctx);
        return null;
    }

    @Override
    public Void visitCmdBrpoplpush(RedisParser.CmdBrpoplpushContext ctx) {
        this.buildCmdDomain(RedisCmdType.BRPOPLPUSH, ctx);
        return null;
    }

    @Override
    public Void visitCmdLindex(RedisParser.CmdLindexContext ctx) {
        this.buildCmdDomain(RedisCmdType.LINDEX, ctx);
        return null;
    }

    @Override
    public Void visitCmdLinsert(RedisParser.CmdLinsertContext ctx) {
        this.buildCmdDomain(RedisCmdType.LINSERT, ctx);
        return null;
    }

    @Override
    public Void visitCmdLlen(RedisParser.CmdLlenContext ctx) {
        this.buildCmdDomain(RedisCmdType.LLEN, ctx);
        return null;
    }

    @Override
    public Void visitCmdLmove(RedisParser.CmdLmoveContext ctx) {
        this.buildCmdDomain(RedisCmdType.LMOVE, ctx);
        return null;
    }

    @Override
    public Void visitCmdLmpop(RedisParser.CmdLmpopContext ctx) {
        this.buildCmdDomain(RedisCmdType.LMPOP, ctx);
        return null;
    }

    @Override
    public Void visitCmdLPop(RedisParser.CmdLPopContext ctx) {
        this.buildCmdDomain(RedisCmdType.LPOP, ctx);
        return null;
    }

    @Override
    public Void visitCmdLpos(RedisParser.CmdLposContext ctx) {
        this.buildCmdDomain(RedisCmdType.LPOS, ctx);
        return null;
    }

    @Override
    public Void visitCmdLPush(RedisParser.CmdLPushContext ctx) {
        this.buildCmdDomain(RedisCmdType.LPUSH, ctx);
        return null;
    }

    @Override
    public Void visitCmdLPushx(RedisParser.CmdLPushxContext ctx) {
        this.buildCmdDomain(RedisCmdType.LPUSHX, ctx);
        return null;
    }

    @Override
    public Void visitCmdLRange(RedisParser.CmdLRangeContext ctx) {
        this.buildCmdDomain(RedisCmdType.LRANGE, ctx);
        return null;
    }

    @Override
    public Void visitCmdLRem(RedisParser.CmdLRemContext ctx) {
        this.buildCmdDomain(RedisCmdType.LREM, ctx);
        return null;
    }

    @Override
    public Void visitCmdLSet(RedisParser.CmdLSetContext ctx) {
        this.buildCmdDomain(RedisCmdType.LSET, ctx);
        return null;
    }

    @Override
    public Void visitCmdLTrim(RedisParser.CmdLTrimContext ctx) {
        this.buildCmdDomain(RedisCmdType.LTRIM, ctx);
        return null;
    }

    @Override
    public Void visitCmdRPop(RedisParser.CmdRPopContext ctx) {
        this.buildCmdDomain(RedisCmdType.RPOP, ctx);
        return null;
    }

    @Override
    public Void visitCmdRpoplpush(RedisParser.CmdRpoplpushContext ctx) {
        this.buildCmdDomain(RedisCmdType.RPOPLPUSH, ctx);
        return null;
    }

    @Override
    public Void visitCmdRPush(RedisParser.CmdRPushContext ctx) {
        this.buildCmdDomain(RedisCmdType.RPUSH, ctx);
        return null;
    }

    @Override
    public Void visitCmdRPushx(RedisParser.CmdRPushxContext ctx) {
        this.buildCmdDomain(RedisCmdType.RPUSHX, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- Set commands */

    @Override
    public Void visitCmdSadd(RedisParser.CmdSaddContext ctx) {
        this.buildCmdDomain(RedisCmdType.SADD, ctx);
        return null;
    }

    @Override
    public Void visitCmdScard(RedisParser.CmdScardContext ctx) {
        this.buildCmdDomain(RedisCmdType.SCARD, ctx);
        return null;
    }

    @Override
    public Void visitCmdSdiff(RedisParser.CmdSdiffContext ctx) {
        this.buildCmdDomain(RedisCmdType.SDIFF, ctx);
        return null;
    }

    @Override
    public Void visitCmdSdiffstore(RedisParser.CmdSdiffstoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.SDIFFSTORE, ctx);
        return null;
    }

    @Override
    public Void visitCmdSinter(RedisParser.CmdSinterContext ctx) {
        this.buildCmdDomain(RedisCmdType.SINTER, ctx);
        return null;
    }

    @Override
    public Void visitCmdSinterCard(RedisParser.CmdSinterCardContext ctx) {
        this.buildCmdDomain(RedisCmdType.SINTERCARD, ctx);
        return null;
    }

    @Override
    public Void visitCmdSinterStore(RedisParser.CmdSinterStoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.SINTERSTORE, ctx);
        return null;
    }

    @Override
    public Void visitCmdSismember(RedisParser.CmdSismemberContext ctx) {
        this.buildCmdDomain(RedisCmdType.SISMEMBER, ctx);
        return null;
    }

    @Override
    public Void visitCmdSmembers(RedisParser.CmdSmembersContext ctx) {
        this.buildCmdDomain(RedisCmdType.SMEMBERS, ctx);
        return null;
    }

    @Override
    public Void visitCmdSmismember(RedisParser.CmdSmismemberContext ctx) {
        this.buildCmdDomain(RedisCmdType.SMISMEMBER, ctx);
        return null;
    }

    @Override
    public Void visitCmdSmove(RedisParser.CmdSmoveContext ctx) {
        this.buildCmdDomain(RedisCmdType.SMOVE, ctx);
        return null;
    }

    @Override
    public Void visitCmdSpop(RedisParser.CmdSpopContext ctx) {
        this.buildCmdDomain(RedisCmdType.SPOP, ctx);
        return null;
    }

    @Override
    public Void visitCmdSrandmember(RedisParser.CmdSrandmemberContext ctx) {
        this.buildCmdDomain(RedisCmdType.SRANDMEMBER, ctx);
        return null;
    }

    @Override
    public Void visitCmdSrem(RedisParser.CmdSremContext ctx) {
        this.buildCmdDomain(RedisCmdType.SREM, ctx);
        return null;
    }

    @Override
    public Void visitCmdSscan(RedisParser.CmdSscanContext ctx) {
        this.buildCmdDomain(RedisCmdType.SSCAN, ctx);
        return null;
    }

    @Override
    public Void visitCmdSunion(RedisParser.CmdSunionContext ctx) {
        this.buildCmdDomain(RedisCmdType.SUNION, ctx);
        return null;
    }

    @Override
    public Void visitCmdSunionstore(RedisParser.CmdSunionstoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.SUNIONSTORE, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- sorted set commands */

    @Override
    public Void visitCmdBzmpop(RedisParser.CmdBzmpopContext ctx) {
        this.buildCmdDomain(RedisCmdType.BZMPOP, ctx);
        return null;
    }

    @Override
    public Void visitCmdBzpopmax(RedisParser.CmdBzpopmaxContext ctx) {
        this.buildCmdDomain(RedisCmdType.BZPOPMAX, ctx);
        return null;
    }

    @Override
    public Void visitCmdBzpopmin(RedisParser.CmdBzpopminContext ctx) {
        this.buildCmdDomain(RedisCmdType.BZPOPMIN, ctx);
        return null;
    }

    @Override
    public Void visitCmdZadd(RedisParser.CmdZaddContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZADD, ctx);
        return null;
    }

    @Override
    public Void visitCmdZcard(RedisParser.CmdZcardContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZCARD, ctx);
        return null;
    }

    @Override
    public Void visitCmdZcount(RedisParser.CmdZcountContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZCOUNT, ctx);
        return null;
    }

    @Override
    public Void visitCmdZdiff(RedisParser.CmdZdiffContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZDIFF, ctx);
        return null;
    }

    @Override
    public Void visitCmdZdiffStore(RedisParser.CmdZdiffStoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZDIFFSTORE, ctx);
        return null;
    }

    @Override
    public Void visitCmdZincrby(RedisParser.CmdZincrbyContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZINCRBY, ctx);
        return null;
    }

    @Override
    public Void visitCmdZinter(RedisParser.CmdZinterContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZINTER, ctx);
        return null;
    }

    @Override
    public Void visitCmdZintercard(RedisParser.CmdZintercardContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZINTERCARD, ctx);
        return null;
    }

    @Override
    public Void visitCmdZinterstore(RedisParser.CmdZinterstoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZINTERSTORE, ctx);
        return null;
    }

    @Override
    public Void visitCmdZLexCount(RedisParser.CmdZLexCountContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZLEXCOUNT, ctx);
        return null;
    }

    @Override
    public Void visitCmdZmpop(RedisParser.CmdZmpopContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZMPOP, ctx);
        return null;
    }

    @Override
    public Void visitCmdZmscore(RedisParser.CmdZmscoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZMSCORE, ctx);
        return null;
    }

    @Override
    public Void visitCmdZpopmax(RedisParser.CmdZpopmaxContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZPOPMAX, ctx);
        return null;
    }

    @Override
    public Void visitCmdZpopmin(RedisParser.CmdZpopminContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZPOPMIN, ctx);
        return null;
    }

    @Override
    public Void visitCmdZrandmember(RedisParser.CmdZrandmemberContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZRANDMEMBER, ctx);
        return null;
    }

    @Override
    public Void visitCmdZrange(RedisParser.CmdZrangeContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZRANGE, ctx);
        return null;
    }

    @Override
    public Void visitCmdZrangebylex(RedisParser.CmdZrangebylexContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZRANGEBYLEX, ctx);
        return null;
    }

    @Override
    public Void visitCmdZrangebyscore(RedisParser.CmdZrangebyscoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZRANGEBYSCORE, ctx);
        return null;
    }

    @Override
    public Void visitCmdZrangestore(RedisParser.CmdZrangestoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZRANGESTORE, ctx);
        return null;
    }

    @Override
    public Void visitCmdZrank(RedisParser.CmdZrankContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZRANK, ctx);
        return null;
    }

    @Override
    public Void visitCmdZRem(RedisParser.CmdZRemContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZREM, ctx);
        return null;
    }

    @Override
    public Void visitCmdZRemRangeByLex(RedisParser.CmdZRemRangeByLexContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZREMRANGEBYLEX, ctx);
        return null;
    }

    @Override
    public Void visitCmdZremrangebyrank(RedisParser.CmdZremrangebyrankContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZREMRANGEBYRANK, ctx);
        return null;
    }

    @Override
    public Void visitCmdZRemRangeByScore(RedisParser.CmdZRemRangeByScoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZREMRANGEBYSCORE, ctx);
        return null;
    }

    @Override
    public Void visitCmdZrevrange(RedisParser.CmdZrevrangeContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZREVRANGE, ctx);
        return null;
    }

    @Override
    public Void visitCmdZrevrangebylex(RedisParser.CmdZrevrangebylexContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZREVRANGEBYLEX, ctx);
        return null;
    }

    @Override
    public Void visitCmdZrevrangebyscore(RedisParser.CmdZrevrangebyscoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZREVRANGEBYSCORE, ctx);
        return null;
    }

    @Override
    public Void visitCmdZrevrank(RedisParser.CmdZrevrankContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZREVRANK, ctx);
        return null;
    }

    @Override
    public Void visitCmdZscan(RedisParser.CmdZscanContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZSCAN, ctx);
        return null;
    }

    @Override
    public Void visitCmdZscore(RedisParser.CmdZscoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZSCORE, ctx);
        return null;
    }

    @Override
    public Void visitCmdZunion(RedisParser.CmdZunionContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZUNION, ctx);
        return null;
    }

    @Override
    public Void visitCmdZunionstore(RedisParser.CmdZunionstoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.ZUNIONSTORE, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- script commands */

    @Override
    public Void visitCmdScriptDebug(RedisParser.CmdScriptDebugContext ctx) {
        this.buildCmdDomain(RedisCmdType.SCRIPT_DEBUG, ctx);
        return null;
    }

    @Override
    public Void visitCmdScriptExists(RedisParser.CmdScriptExistsContext ctx) {
        this.buildCmdDomain(RedisCmdType.SCRIPT_EXISTS, ctx);
        return null;
    }

    @Override
    public Void visitCmdScriptFlush(RedisParser.CmdScriptFlushContext ctx) {
        this.buildCmdDomain(RedisCmdType.SCRIPT_FLUSH, ctx);
        return null;
    }

    @Override
    public Void visitCmdScriptKill(RedisParser.CmdScriptKillContext ctx) {
        this.buildCmdDomain(RedisCmdType.SCRIPT_KILL, ctx);
        return null;
    }

    @Override
    public Void visitCmdScriptLoad(RedisParser.CmdScriptLoadContext ctx) {
        this.buildCmdDomain(RedisCmdType.SCRIPT_LOAD, ctx);
        return null;
    }

    @Override
    public Void visitCmdEval(RedisParser.CmdEvalContext ctx) {
        this.buildCmdDomain(RedisCmdType.EVAL, ctx);
        return null;
    }

    @Override
    public Void visitCmdEvalRO(RedisParser.CmdEvalROContext ctx) {
        this.buildCmdDomain(RedisCmdType.EVAL_RO, ctx);
        return null;
    }

    @Override
    public Void visitCmdEvalsha(RedisParser.CmdEvalshaContext ctx) {
        this.buildCmdDomain(RedisCmdType.EVALSHA, ctx);
        return null;
    }

    @Override
    public Void visitCmdEvalshaRO(RedisParser.CmdEvalshaROContext ctx) {
        this.buildCmdDomain(RedisCmdType.EVALSHA_RO, ctx);
        return null;
    }

    @Override
    public Void visitCmdFCall(RedisParser.CmdFCallContext ctx) {
        this.buildCmdDomain(RedisCmdType.FCALL, ctx);
        return null;
    }

    @Override
    public Void visitCmdFCallRO(RedisParser.CmdFCallROContext ctx) {
        this.buildCmdDomain(RedisCmdType.FCALL_RO, ctx);
        return null;
    }

    @Override
    public Void visitCmdFunctionDel(RedisParser.CmdFunctionDelContext ctx) {
        this.buildCmdDomain(RedisCmdType.FUNCTION_DEL, ctx);
        return null;
    }

    @Override
    public Void visitCmdFunctionDump(RedisParser.CmdFunctionDumpContext ctx) {
        this.buildCmdDomain(RedisCmdType.FUNCTION_DUMP, ctx);
        return null;
    }

    @Override
    public Void visitCmdFunctionFlush(RedisParser.CmdFunctionFlushContext ctx) {
        this.buildCmdDomain(RedisCmdType.FUNCTION_FLUSH, ctx);
        return null;
    }

    @Override
    public Void visitCmdFunctionKill(RedisParser.CmdFunctionKillContext ctx) {
        this.buildCmdDomain(RedisCmdType.FUNCTION_KILL, ctx);
        return null;
    }

    @Override
    public Void visitCmdFunctionList(RedisParser.CmdFunctionListContext ctx) {
        this.buildCmdDomain(RedisCmdType.FUNCTION_LIST, ctx);
        return null;
    }

    @Override
    public Void visitCmdFunctionLoad(RedisParser.CmdFunctionLoadContext ctx) {
        this.buildCmdDomain(RedisCmdType.FUNCTION_LOAD, ctx);
        return null;
    }

    @Override
    public Void visitCmdFunctionRestore(RedisParser.CmdFunctionRestoreContext ctx) {
        this.buildCmdDomain(RedisCmdType.FUNCTION_RESTORE, ctx);
        return null;
    }

    @Override
    public Void visitCmdFunctionStats(RedisParser.CmdFunctionStatsContext ctx) {
        this.buildCmdDomain(RedisCmdType.FUNCTION_STATS, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- tx commands */

    @Override
    public Void visitCmdDiscard(RedisParser.CmdDiscardContext ctx) {
        this.buildCmdDomain(RedisCmdType.DISCARD, ctx);
        return null;
    }

    @Override
    public Void visitCmdExec(RedisParser.CmdExecContext ctx) {
        this.buildCmdDomain(RedisCmdType.EXEC, ctx);
        return null;
    }

    @Override
    public Void visitCmdMulti(RedisParser.CmdMultiContext ctx) {
        this.buildCmdDomain(RedisCmdType.MULTI, ctx);
        return null;
    }

    @Override
    public Void visitCmdUnwatch(RedisParser.CmdUnwatchContext ctx) {
        this.buildCmdDomain(RedisCmdType.UNWATCH, ctx);
        return null;
    }

    @Override
    public Void visitCmdWatch(RedisParser.CmdWatchContext ctx) {
        this.buildCmdDomain(RedisCmdType.WATCH, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- HyperLog commands */

    @Override
    public Void visitCmdPFAdd(RedisParser.CmdPFAddContext ctx) {
        this.buildCmdDomain(RedisCmdType.PFADD, ctx);
        return null;
    }

    @Override
    public Void visitCmdPFCount(RedisParser.CmdPFCountContext ctx) {
        this.buildCmdDomain(RedisCmdType.PFCOUNT, ctx);
        return null;
    }

    @Override
    public Void visitCmdPFMerge(RedisParser.CmdPFMergeContext ctx) {
        this.buildCmdDomain(RedisCmdType.PFMERGE, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- publish commands */

    @Override
    public Void visitCmdPSubscribe(RedisParser.CmdPSubscribeContext ctx) {
        this.buildCmdDomain(RedisCmdType.PSUBSCRIBE, ctx);
        return null;
    }

    @Override
    public Void visitCmdPublish(RedisParser.CmdPublishContext ctx) {
        this.buildCmdDomain(RedisCmdType.PUBLISH, ctx);
        return null;
    }

    @Override
    public Void visitCmdPubSubChannels(RedisParser.CmdPubSubChannelsContext ctx) {
        this.buildCmdDomain(RedisCmdType.PUBSUB_CHANNELS, ctx);
        return null;
    }

    @Override
    public Void visitCmdPubSubNumPat(RedisParser.CmdPubSubNumPatContext ctx) {
        this.buildCmdDomain(RedisCmdType.PUBSUB_NUMPAT, ctx);
        return null;
    }

    @Override
    public Void visitCmdPubSubNumSub(RedisParser.CmdPubSubNumSubContext ctx) {
        this.buildCmdDomain(RedisCmdType.PUBSUB_NUMSUB, ctx);
        return null;
    }

    @Override
    public Void visitCmdPubSubShardChannels(RedisParser.CmdPubSubShardChannelsContext ctx) {
        this.buildCmdDomain(RedisCmdType.PUBSUB_SHARDCHANNELS, ctx);
        return null;
    }

    @Override
    public Void visitCmdPubSubShardNumSub(RedisParser.CmdPubSubShardNumSubContext ctx) {
        this.buildCmdDomain(RedisCmdType.PUBSUB_SHARDNUMSUB, ctx);
        return null;
    }

    @Override
    public Void visitCmdPunSubscribe(RedisParser.CmdPunSubscribeContext ctx) {
        this.buildCmdDomain(RedisCmdType.PUNSUBSCRIBE, ctx);
        return null;
    }

    @Override
    public Void visitCmdSpublish(RedisParser.CmdSpublishContext ctx) {
        this.buildCmdDomain(RedisCmdType.SPUBLISH, ctx);
        return null;
    }

    @Override
    public Void visitCmdSSubscribe(RedisParser.CmdSSubscribeContext ctx) {
        this.buildCmdDomain(RedisCmdType.SSUBSCRIBE, ctx);
        return null;
    }

    @Override
    public Void visitCmdSubscribe(RedisParser.CmdSubscribeContext ctx) {
        this.buildCmdDomain(RedisCmdType.SUBSCRIBE, ctx);
        return null;
    }

    @Override
    public Void visitCmdSunSubscribe(RedisParser.CmdSunSubscribeContext ctx) {
        this.buildCmdDomain(RedisCmdType.SUNSUBSCRIBE, ctx);
        return null;
    }

    @Override
    public Void visitCmdUnSubScribe(RedisParser.CmdUnSubScribeContext ctx) {
        this.buildCmdDomain(RedisCmdType.UNSUBSCRIBE, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- cluster commands */

    @Override
    public Void visitCmdAsking(RedisParser.CmdAskingContext ctx) {
        this.buildCmdDomain(RedisCmdType.ASKING, ctx);
        return null;
    }

    @Override
    public Void visitCmdReadonly(RedisParser.CmdReadonlyContext ctx) {
        this.buildCmdDomain(RedisCmdType.READONLY, ctx);
        return null;
    }

    @Override
    public Void visitCmdReadWrite(RedisParser.CmdReadWriteContext ctx) {
        this.buildCmdDomain(RedisCmdType.READWRITE, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterAddSlots(RedisParser.CmdClusterAddSlotsContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_ADDSLOTS, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterDelSlots(RedisParser.CmdClusterDelSlotsContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_DELSLOTS, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterAddSlotsRange(RedisParser.CmdClusterAddSlotsRangeContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_ADDSLOTSRANGE, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterDelSlotsRange(RedisParser.CmdClusterDelSlotsRangeContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_DELSLOTSRANGE, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterBumpEpoch(RedisParser.CmdClusterBumpEpochContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_BUMPEPOCH, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterCountFailureReports(RedisParser.CmdClusterCountFailureReportsContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_COUNT_FAILURE_REPORTS, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterCountKeysInSlot(RedisParser.CmdClusterCountKeysInSlotContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_COUNTKEYSINSLOT, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterFailOver(RedisParser.CmdClusterFailOverContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_FAILOVER, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterFlushSlots(RedisParser.CmdClusterFlushSlotsContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_FLUSHSLOTS, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterForget(RedisParser.CmdClusterForgetContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_FORGET, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterGetKeysInSlot(RedisParser.CmdClusterGetKeysInSlotContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_GETKEYSINSLOT, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterInfo(RedisParser.CmdClusterInfoContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_INFO, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterKeySlot(RedisParser.CmdClusterKeySlotContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_KEYSLOT, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterLinks(RedisParser.CmdClusterLinksContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_LINKS, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterMeet(RedisParser.CmdClusterMeetContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_MEET, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterMyId(RedisParser.CmdClusterMyIdContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_MYID, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterMyShardId(RedisParser.CmdClusterMyShardIdContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_MYSHARDID, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterNodes(RedisParser.CmdClusterNodesContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_NODES, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterReplicas(RedisParser.CmdClusterReplicasContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_REPLICAS, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterReplicate(RedisParser.CmdClusterReplicateContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_REPLICATE, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterReset(RedisParser.CmdClusterResetContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_RESET, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterSaveConfig(RedisParser.CmdClusterSaveConfigContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_SAVECONFIG, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterSetConfigEpoch(RedisParser.CmdClusterSetConfigEpochContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_SET_CONFIG_EPOCH, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterSetSlot(RedisParser.CmdClusterSetSlotContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_SETSLOT, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterShards(RedisParser.CmdClusterShardsContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_SHARDS, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterSlaves(RedisParser.CmdClusterSlavesContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_SLAVES, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterSlotStats(RedisParser.CmdClusterSlotStatsContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_SLOT_STATS, ctx);
        return null;
    }

    @Override
    public Void visitCmdClusterSlots(RedisParser.CmdClusterSlotsContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLUSTER_SLOTS, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- info commands */

    @Override
    public Void visitCmdInfo(RedisParser.CmdInfoContext ctx) {
        this.buildCmdDomain(RedisCmdType.INFO, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- acl commands */

    @Override
    public Void visitCmdAclCat(RedisParser.CmdAclCatContext ctx) {
        this.buildCmdDomain(RedisCmdType.ACL_CAT, ctx);
        return null;
    }

    @Override
    public Void visitCmdAclDelUser(RedisParser.CmdAclDelUserContext ctx) {
        this.buildCmdDomain(RedisCmdType.ACL_DELUSER, ctx);
        return null;
    }

    @Override
    public Void visitCmdAclDryRun(RedisParser.CmdAclDryRunContext ctx) {
        this.buildCmdDomain(RedisCmdType.ACL_DRYRUN, ctx);
        return null;
    }

    @Override
    public Void visitCmdAclGenPass(RedisParser.CmdAclGenPassContext ctx) {
        this.buildCmdDomain(RedisCmdType.ACL_GENPASS, ctx);
        return null;
    }

    @Override
    public Void visitCmdAclGetUser(RedisParser.CmdAclGetUserContext ctx) {
        this.buildCmdDomain(RedisCmdType.ACL_GETUSER, ctx);
        return null;
    }

    @Override
    public Void visitCmdAclList(RedisParser.CmdAclListContext ctx) {
        this.buildCmdDomain(RedisCmdType.ACL_LIST, ctx);
        return null;
    }

    @Override
    public Void visitCmdAclLoad(RedisParser.CmdAclLoadContext ctx) {
        this.buildCmdDomain(RedisCmdType.ACL_LOAD, ctx);
        return null;
    }

    @Override
    public Void visitCmdAclLog(RedisParser.CmdAclLogContext ctx) {
        this.buildCmdDomain(RedisCmdType.ACL_LOG, ctx);
        return null;
    }

    @Override
    public Void visitCmdAclSave(RedisParser.CmdAclSaveContext ctx) {
        this.buildCmdDomain(RedisCmdType.ACL_SAVE, ctx);
        return null;
    }

    @Override
    public Void visitCmdAclSetUser(RedisParser.CmdAclSetUserContext ctx) {
        this.buildCmdDomain(RedisCmdType.ACL_SETUSER, ctx);
        return null;
    }

    @Override
    public Void visitCmdAclUsers(RedisParser.CmdAclUsersContext ctx) {
        this.buildCmdDomain(RedisCmdType.ACL_USERS, ctx);
        return null;
    }

    @Override
    public Void visitCmdAclWhoami(RedisParser.CmdAclWhoamiContext ctx) {
        this.buildCmdDomain(RedisCmdType.ACL_WHOAMI, ctx);
        return null;
    }

    /* -------------------------------------------------------------------------------- command commands */

    @Override
    public Void visitCmdCommand(RedisParser.CmdCommandContext ctx) {
        this.buildCmdDomain(RedisCmdType.COMMAND, ctx);
        return null;
    }

    @Override
    public Void visitCmdCommandCount(RedisParser.CmdCommandCountContext ctx) {
        this.buildCmdDomain(RedisCmdType.COMMAND_COUNT, ctx);
        return null;
    }

    @Override
    public Void visitCmdCommandDocs(RedisParser.CmdCommandDocsContext ctx) {
        this.buildCmdDomain(RedisCmdType.COMMAND_DOCS, ctx);
        return null;
    }

    @Override
    public Void visitCmdCommandGetKeys(RedisParser.CmdCommandGetKeysContext ctx) {
        this.buildCmdDomain(RedisCmdType.COMMAND_GETKEYS, ctx);
        return null;
    }

    @Override
    public Void visitCmdCommandGetKeysAndFlags(RedisParser.CmdCommandGetKeysAndFlagsContext ctx) {
        this.buildCmdDomain(RedisCmdType.COMMAND_GETKEYSANDFLAGS, ctx);
        return null;
    }

    @Override
    public Void visitCmdCommandInfo(RedisParser.CmdCommandInfoContext ctx) {
        this.buildCmdDomain(RedisCmdType.COMMAND_INFO, ctx);
        return null;
    }

    @Override
    public Void visitCmdCommandList(RedisParser.CmdCommandListContext ctx) {
        this.buildCmdDomain(RedisCmdType.COMMAND_LIST, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- config commands */

    @Override
    public Void visitCmdConfigGet(RedisParser.CmdConfigGetContext ctx) {
        this.buildCmdDomain(RedisCmdType.CONFIG_GET, ctx);
        return null;
    }

    @Override
    public Void visitCmdConfigSet(RedisParser.CmdConfigSetContext ctx) {
        this.buildCmdDomain(RedisCmdType.CONFIG_SET, ctx);
        return null;
    }

    @Override
    public Void visitCmdConfigResetStat(RedisParser.CmdConfigResetStatContext ctx) {
        this.buildCmdDomain(RedisCmdType.CONFIG_RESETSTAT, ctx);
        return null;
    }

    @Override
    public Void visitCmdConfigRewrite(RedisParser.CmdConfigRewriteContext ctx) {
        this.buildCmdDomain(RedisCmdType.CONFIG_REWRITE, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- latency commands */

    @Override
    public Void visitCmdLatencyHistogram(RedisParser.CmdLatencyHistogramContext ctx) {
        this.buildCmdDomain(RedisCmdType.LATENCY_HISTOGRAM, ctx);
        return null;
    }

    @Override
    public Void visitCmdLatencyHistory(RedisParser.CmdLatencyHistoryContext ctx) {
        this.buildCmdDomain(RedisCmdType.LATENCY_HISTORY, ctx);
        return null;
    }

    @Override
    public Void visitCmdLatencyLatest(RedisParser.CmdLatencyLatestContext ctx) {
        this.buildCmdDomain(RedisCmdType.LATENCY_LATEST, ctx);
        return null;
    }

    @Override
    public Void visitCmdLatencyReset(RedisParser.CmdLatencyResetContext ctx) {
        this.buildCmdDomain(RedisCmdType.LATENCY_RESET, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- memory commands */

    @Override
    public Void visitCmdMemoryDoctor(RedisParser.CmdMemoryDoctorContext ctx) {
        this.buildCmdDomain(RedisCmdType.MEMORY_DOCTOR, ctx);
        return null;
    }

    @Override
    public Void visitCmdMemoryMallocStats(RedisParser.CmdMemoryMallocStatsContext ctx) {
        this.buildCmdDomain(RedisCmdType.MEMORY_MALLOC_STATS, ctx);
        return null;
    }

    @Override
    public Void visitCmdMemoryPurge(RedisParser.CmdMemoryPurgeContext ctx) {
        this.buildCmdDomain(RedisCmdType.MEMORY_PURGE, ctx);
        return null;
    }

    @Override
    public Void visitCmdMemoryStats(RedisParser.CmdMemoryStatsContext ctx) {
        this.buildCmdDomain(RedisCmdType.MEMORY_STATS, ctx);
        return null;
    }

    @Override
    public Void visitCmdMemoryUsage(RedisParser.CmdMemoryUsageContext ctx) {
        this.buildCmdDomain(RedisCmdType.MEMORY_USAGE, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- module commands */

    @Override
    public Void visitCmdModuleList(RedisParser.CmdModuleListContext ctx) {
        this.buildCmdDomain(RedisCmdType.MODULE_LIST, ctx);
        return null;
    }

    @Override
    public Void visitCmdModuleLoad(RedisParser.CmdModuleLoadContext ctx) {
        this.buildCmdDomain(RedisCmdType.MODULE_LOAD, ctx);
        return null;
    }

    @Override
    public Void visitCmdModuleLoadEx(RedisParser.CmdModuleLoadExContext ctx) {
        this.buildCmdDomain(RedisCmdType.MODULE_LOADEX, ctx);
        return null;
    }

    @Override
    public Void visitCmdModuleUnload(RedisParser.CmdModuleUnloadContext ctx) {
        this.buildCmdDomain(RedisCmdType.MODULE_UNLOAD, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- control commands */

    @Override
    public Void visitCmdBgrewriteaof(RedisParser.CmdBgrewriteaofContext ctx) {
        this.buildCmdDomain(RedisCmdType.BGREWRITEAOF, ctx);
        return null;
    }

    @Override
    public Void visitCmdBgsave(RedisParser.CmdBgsaveContext ctx) {
        this.buildCmdDomain(RedisCmdType.BGSAVE, ctx);
        return null;
    }

    @Override
    public Void visitCmdDbsize(RedisParser.CmdDbsizeContext ctx) {
        this.buildCmdDomain(RedisCmdType.DBSIZE, ctx);
        return null;
    }

    @Override
    public Void visitCmdFailover(RedisParser.CmdFailoverContext ctx) {
        this.buildCmdDomain(RedisCmdType.FAILOVER, ctx);
        return null;
    }

    @Override
    public Void visitCmdFlushAll(RedisParser.CmdFlushAllContext ctx) {
        this.buildCmdDomain(RedisCmdType.FLUSHALL, ctx);
        return null;
    }

    @Override
    public Void visitCmdFlushDB(RedisParser.CmdFlushDBContext ctx) {
        this.buildCmdDomain(RedisCmdType.FLUSHDB, ctx);
        return null;
    }

    @Override
    public Void visitCmdLastsave(RedisParser.CmdLastsaveContext ctx) {
        this.buildCmdDomain(RedisCmdType.LASTSAVE, ctx);
        return null;
    }

    @Override
    public Void visitCmdLolwut(RedisParser.CmdLolwutContext ctx) {
        this.buildCmdDomain(RedisCmdType.LOLWUT, ctx);
        return null;
    }

    @Override
    public Void visitCmdMonitor(RedisParser.CmdMonitorContext ctx) {
        this.buildCmdDomain(RedisCmdType.MONITOR, ctx);
        return null;
    }

    @Override
    public Void visitCmdPSync(RedisParser.CmdPSyncContext ctx) {
        this.buildCmdDomain(RedisCmdType.PSYNC, ctx);
        return null;
    }

    @Override
    public Void visitCmdReplicaOf(RedisParser.CmdReplicaOfContext ctx) {
        this.buildCmdDomain(RedisCmdType.REPLICAOF, ctx);
        return null;
    }

    @Override
    public Void visitCmdRole(RedisParser.CmdRoleContext ctx) {
        this.buildCmdDomain(RedisCmdType.ROLE, ctx);
        return null;
    }

    @Override
    public Void visitCmdSave(RedisParser.CmdSaveContext ctx) {
        this.buildCmdDomain(RedisCmdType.SAVE, ctx);
        return null;
    }

    @Override
    public Void visitCmdShutdown(RedisParser.CmdShutdownContext ctx) {
        this.buildCmdDomain(RedisCmdType.SHUTDOWN, ctx);
        return null;
    }

    @Override
    public Void visitCmdSlaveOf(RedisParser.CmdSlaveOfContext ctx) {
        this.buildCmdDomain(RedisCmdType.SLAVEOF, ctx);
        return null;
    }

    @Override
    public Void visitCmdSlowlogGet(RedisParser.CmdSlowlogGetContext ctx) {
        this.buildCmdDomain(RedisCmdType.SLOWLOG_GET, ctx);
        return null;
    }

    @Override
    public Void visitCmdSlowlogLen(RedisParser.CmdSlowlogLenContext ctx) {
        this.buildCmdDomain(RedisCmdType.SLOWLOG_LEN, ctx);
        return null;
    }

    @Override
    public Void visitCmdSlowlogReset(RedisParser.CmdSlowlogResetContext ctx) {
        this.buildCmdDomain(RedisCmdType.SLOWLOG_RESET, ctx);
        return null;
    }

    @Override
    public Void visitCmdSwapDB(RedisParser.CmdSwapDBContext ctx) {
        this.buildCmdDomain(RedisCmdType.SWAPDB, ctx);
        return null;
    }

    @Override
    public Void visitCmdSync(RedisParser.CmdSyncContext ctx) {
        this.buildCmdDomain(RedisCmdType.SYNC, ctx);
        return null;
    }

    @Override
    public Void visitCmdTime(RedisParser.CmdTimeContext ctx) {
        this.buildCmdDomain(RedisCmdType.TIME, ctx);
        return null;
    }

    /* ----------------------------------------------------------------------------------- Client commands */

    @Override
    public Void visitCmdClientCaching(RedisParser.CmdClientCachingContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_CACHING, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientGetname(RedisParser.CmdClientGetnameContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_GETNAME, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientGetredir(RedisParser.CmdClientGetredirContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_GETREDIR, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientID(RedisParser.CmdClientIDContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_ID, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientInfo(RedisParser.CmdClientInfoContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_INFO, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientKill(RedisParser.CmdClientKillContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_KILL, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientList(RedisParser.CmdClientListContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_LIST, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientNoEvict(RedisParser.CmdClientNoEvictContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_NO_EVICT, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientNoTouch(RedisParser.CmdClientNoTouchContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_NO_TOUCH, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientPause(RedisParser.CmdClientPauseContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_PAUSE, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientReply(RedisParser.CmdClientReplyContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_REPLY, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientSetInfo(RedisParser.CmdClientSetInfoContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_SETINFO, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientSetname(RedisParser.CmdClientSetnameContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_SETNAME, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientTracking(RedisParser.CmdClientTrackingContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_TRACKING, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientTrackingInfo(RedisParser.CmdClientTrackingInfoContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_TRACKINGINFO, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientUnBlock(RedisParser.CmdClientUnBlockContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_UNBLOCK, ctx);
        return null;
    }

    @Override
    public Void visitCmdClientUnPause(RedisParser.CmdClientUnPauseContext ctx) {
        this.buildCmdDomain(RedisCmdType.CLIENT_UNPAUSE, ctx);
        return null;
    }

    @Override
    public Void visitCmdAuth(RedisParser.CmdAuthContext ctx) {
        this.buildCmdDomain(RedisCmdType.AUTH, ctx);
        return null;
    }

    @Override
    public Void visitCmdEcho(RedisParser.CmdEchoContext ctx) {
        this.buildCmdDomain(RedisCmdType.ECHO, ctx);
        return null;
    }

    @Override
    public Void visitCmdHello(RedisParser.CmdHelloContext ctx) {
        this.buildCmdDomain(RedisCmdType.HELLO, ctx);
        return null;
    }

    @Override
    public Void visitCmdPing(RedisParser.CmdPingContext ctx) {
        this.buildCmdDomain(RedisCmdType.PING, ctx);
        return null;
    }

    @Override
    public Void visitCmdQuit(RedisParser.CmdQuitContext ctx) {
        this.buildCmdDomain(RedisCmdType.QUIT, ctx);
        return null;
    }

    @Override
    public Void visitCmdReset(RedisParser.CmdResetContext ctx) {
        this.buildCmdDomain(RedisCmdType.RESET, ctx);
        return null;
    }

    @Override
    public Void visitCmdSelect(RedisParser.CmdSelectContext ctx) {
        this.buildCmdDomain(RedisCmdType.SELECT, ctx);
        return null;
    }
}
