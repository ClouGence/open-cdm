// Generated from dt-sql-parser/src/grammar/redis/RedisParser.g4 by ANTLR 4.9.0-SNAPSHOT
import { ATN } from "antlr4ts/atn/ATN";
import { ATNDeserializer } from "antlr4ts/atn/ATNDeserializer";
import { FailedPredicateException } from "antlr4ts/FailedPredicateException";
import { NoViableAltException } from "antlr4ts/NoViableAltException";
import { Parser } from "antlr4ts/Parser";
import { ParserRuleContext } from "antlr4ts/ParserRuleContext";
import { ParserATNSimulator } from "antlr4ts/atn/ParserATNSimulator";
import { RecognitionException } from "antlr4ts/RecognitionException";
import { Token } from "antlr4ts/Token";
import { VocabularyImpl } from "antlr4ts/VocabularyImpl";
import * as Utils from "antlr4ts/misc/Utils";
export class RedisParser extends Parser {
    // @Override
    // @NotNull
    get vocabulary() {
        return RedisParser.VOCABULARY;
    }
    // tslint:enable:no-trailing-whitespace
    // @Override
    get grammarFileName() { return "RedisParser.g4"; }
    // @Override
    get ruleNames() { return RedisParser.ruleNames; }
    // @Override
    get serializedATN() { return RedisParser._serializedATN; }
    createFailedPredicateException(predicate, message) {
        return new FailedPredicateException(this, predicate, message);
    }
    constructor(input) {
        super(input);
        this._interp = new ParserATNSimulator(RedisParser._ATN, this);
    }
    // @RuleVersion(0)
    program() {
        let _localctx = new ProgramContext(this._ctx, this.state);
        this.enterRule(_localctx, 0, RedisParser.RULE_program);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 393;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if ((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << RedisParser.COPY) | (1 << RedisParser.DEL) | (1 << RedisParser.DUMP) | (1 << RedisParser.EXISTS) | (1 << RedisParser.EXPIRE) | (1 << RedisParser.EXPIREAT) | (1 << RedisParser.EXPIRETIME) | (1 << RedisParser.KEYS) | (1 << RedisParser.MOVE) | (1 << RedisParser.OBJECT) | (1 << RedisParser.PERSIST) | (1 << RedisParser.PEXPIRE) | (1 << RedisParser.PEXPIREAT) | (1 << RedisParser.PEXPIRETIME) | (1 << RedisParser.TTL) | (1 << RedisParser.PTTL) | (1 << RedisParser.RANDOMKEY) | (1 << RedisParser.RENAME) | (1 << RedisParser.RENAMENX) | (1 << RedisParser.RESTORE) | (1 << RedisParser.SCAN) | (1 << RedisParser.SORT) | (1 << RedisParser.SORT_RO) | (1 << RedisParser.TOUCH) | (1 << RedisParser.TYPE) | (1 << RedisParser.UNLINK) | (1 << RedisParser.WAIT))) !== 0) || ((((_la - 32)) & ~0x1F) === 0 && ((1 << (_la - 32)) & ((1 << (RedisParser.APPEND - 32)) | (1 << (RedisParser.DECR - 32)) | (1 << (RedisParser.DECRBY - 32)) | (1 << (RedisParser.GET - 32)) | (1 << (RedisParser.GETDEL - 32)) | (1 << (RedisParser.GETEX - 32)) | (1 << (RedisParser.GETRANGE - 32)) | (1 << (RedisParser.GETSET - 32)) | (1 << (RedisParser.GETBIT - 32)) | (1 << (RedisParser.INCR - 32)) | (1 << (RedisParser.INCRBY - 32)) | (1 << (RedisParser.INCRBYFLOAT - 32)) | (1 << (RedisParser.LCS - 32)) | (1 << (RedisParser.MGET - 32)) | (1 << (RedisParser.MSET - 32)) | (1 << (RedisParser.MSETNX - 32)) | (1 << (RedisParser.SETEX - 32)) | (1 << (RedisParser.PSETEX - 32)) | (1 << (RedisParser.SET - 32)) | (1 << (RedisParser.SETNX - 32)) | (1 << (RedisParser.SETRANGE - 32)) | (1 << (RedisParser.SETBIT - 32)) | (1 << (RedisParser.STRLEN - 32)) | (1 << (RedisParser.SUBSTR - 32)) | (1 << (RedisParser.HDEL - 32)) | (1 << (RedisParser.HEXISTS - 32)) | (1 << (RedisParser.HGET - 32)) | (1 << (RedisParser.HGETALL - 32)) | (1 << (RedisParser.HINCRBY - 32)) | (1 << (RedisParser.HINCRBYFLOAT - 32)) | (1 << (RedisParser.HKEYS - 32)) | (1 << (RedisParser.HLEN - 32)))) !== 0) || ((((_la - 64)) & ~0x1F) === 0 && ((1 << (_la - 64)) & ((1 << (RedisParser.HMGET - 64)) | (1 << (RedisParser.HMSET - 64)) | (1 << (RedisParser.HRANDFIELD - 64)) | (1 << (RedisParser.HSCAN - 64)) | (1 << (RedisParser.HSET - 64)) | (1 << (RedisParser.HSETNX - 64)) | (1 << (RedisParser.HSTRLEN - 64)) | (1 << (RedisParser.HVALS - 64)) | (1 << (RedisParser.BLMOVE - 64)) | (1 << (RedisParser.BLMPOP - 64)) | (1 << (RedisParser.BLPOP - 64)) | (1 << (RedisParser.BRPOP - 64)) | (1 << (RedisParser.BRPOPLPUSH - 64)) | (1 << (RedisParser.LINDEX - 64)) | (1 << (RedisParser.LINSERT - 64)) | (1 << (RedisParser.LLEN - 64)) | (1 << (RedisParser.LMOVE - 64)) | (1 << (RedisParser.LMPOP - 64)) | (1 << (RedisParser.LPOP - 64)) | (1 << (RedisParser.LPOS - 64)) | (1 << (RedisParser.LPUSH - 64)) | (1 << (RedisParser.LPUSHX - 64)) | (1 << (RedisParser.LRANGE - 64)) | (1 << (RedisParser.LREM - 64)) | (1 << (RedisParser.LSET - 64)) | (1 << (RedisParser.LTRIM - 64)) | (1 << (RedisParser.RPOP - 64)) | (1 << (RedisParser.RPOPLPUSH - 64)) | (1 << (RedisParser.RPUSH - 64)) | (1 << (RedisParser.RPUSHX - 64)) | (1 << (RedisParser.SADD - 64)) | (1 << (RedisParser.SCARD - 64)))) !== 0) || ((((_la - 96)) & ~0x1F) === 0 && ((1 << (_la - 96)) & ((1 << (RedisParser.SDIFF - 96)) | (1 << (RedisParser.SDIFFSTORE - 96)) | (1 << (RedisParser.SINTER - 96)) | (1 << (RedisParser.SINTERCARD - 96)) | (1 << (RedisParser.SINTERSTORE - 96)) | (1 << (RedisParser.SISMEMBER - 96)) | (1 << (RedisParser.SMEMBERS - 96)) | (1 << (RedisParser.SMISMEMBER - 96)) | (1 << (RedisParser.SMOVE - 96)) | (1 << (RedisParser.SPOP - 96)) | (1 << (RedisParser.SRANDMEMBER - 96)) | (1 << (RedisParser.SREM - 96)) | (1 << (RedisParser.SSCAN - 96)) | (1 << (RedisParser.SUNION - 96)) | (1 << (RedisParser.SUNIONSTORE - 96)) | (1 << (RedisParser.BZMPOP - 96)) | (1 << (RedisParser.BZPOPMAX - 96)) | (1 << (RedisParser.BZPOPMIN - 96)) | (1 << (RedisParser.ZADD - 96)) | (1 << (RedisParser.ZCARD - 96)) | (1 << (RedisParser.ZCOUNT - 96)) | (1 << (RedisParser.ZDIFF - 96)) | (1 << (RedisParser.ZDIFFSTORE - 96)) | (1 << (RedisParser.ZINCRBY - 96)) | (1 << (RedisParser.ZINTER - 96)) | (1 << (RedisParser.ZINTERCARD - 96)) | (1 << (RedisParser.ZINTERSTORE - 96)) | (1 << (RedisParser.ZLEXCOUNT - 96)) | (1 << (RedisParser.ZMPOP - 96)) | (1 << (RedisParser.ZMSCORE - 96)) | (1 << (RedisParser.ZPOPMAX - 96)) | (1 << (RedisParser.ZPOPMIN - 96)))) !== 0) || ((((_la - 128)) & ~0x1F) === 0 && ((1 << (_la - 128)) & ((1 << (RedisParser.ZRANDMEMBER - 128)) | (1 << (RedisParser.ZRANGE - 128)) | (1 << (RedisParser.ZRANGEBYLEX - 128)) | (1 << (RedisParser.ZRANGEBYSCORE - 128)) | (1 << (RedisParser.ZRANGESTORE - 128)) | (1 << (RedisParser.ZRANK - 128)) | (1 << (RedisParser.ZREM - 128)) | (1 << (RedisParser.ZREMRANGEBYLEX - 128)) | (1 << (RedisParser.ZREMRANGEBYRANK - 128)) | (1 << (RedisParser.ZREMRANGEBYSCORE - 128)) | (1 << (RedisParser.ZREVRANGE - 128)) | (1 << (RedisParser.ZREVRANGEBYLEX - 128)) | (1 << (RedisParser.ZREVRANGEBYSCORE - 128)) | (1 << (RedisParser.ZREVRANK - 128)) | (1 << (RedisParser.ZSCAN - 128)) | (1 << (RedisParser.ZSCORE - 128)) | (1 << (RedisParser.ZUNION - 128)) | (1 << (RedisParser.ZUNIONSTORE - 128)) | (1 << (RedisParser.PSUBSCRIBE - 128)) | (1 << (RedisParser.PUBSUB - 128)) | (1 << (RedisParser.PUBLISH - 128)) | (1 << (RedisParser.PUNSUBSCRIBE - 128)) | (1 << (RedisParser.SUBSCRIBE - 128)) | (1 << (RedisParser.UNSUBSCRIBE - 128)) | (1 << (RedisParser.SSUBSCRIBE - 128)) | (1 << (RedisParser.SUNSUBSCRIBE - 128)))) !== 0) || _la === RedisParser.ASKING || _la === RedisParser.CLUSTER || ((((_la - 209)) & ~0x1F) === 0 && ((1 << (_la - 209)) & ((1 << (RedisParser.READONLY - 209)) | (1 << (RedisParser.READWRITE - 209)) | (1 << (RedisParser.FLUSHDB - 209)) | (1 << (RedisParser.SCRIPT - 209)) | (1 << (RedisParser.EVAL - 209)) | (1 << (RedisParser.EVALSHA - 209)) | (1 << (RedisParser.EXEC - 209)) | (1 << (RedisParser.WATCH - 209)) | (1 << (RedisParser.DISCARD - 209)) | (1 << (RedisParser.UNWATCH - 209)) | (1 << (RedisParser.MULTI - 209)) | (1 << (RedisParser.PFMERGE - 209)) | (1 << (RedisParser.PFADD - 209)) | (1 << (RedisParser.PFCOUNT - 209)) | (1 << (RedisParser.ECHO - 209)) | (1 << (RedisParser.PING - 209)) | (1 << (RedisParser.SAVE - 209)) | (1 << (RedisParser.SLOWLOG - 209)) | (1 << (RedisParser.LASTSAVE - 209)) | (1 << (RedisParser.CONFIG - 209)) | (1 << (RedisParser.CLIENT - 209)) | (1 << (RedisParser.SHUTDOWN - 209)) | (1 << (RedisParser.SYNC - 209)) | (1 << (RedisParser.ROLE - 209)) | (1 << (RedisParser.MONITOR - 209)) | (1 << (RedisParser.SLAVEOF - 209)) | (1 << (RedisParser.FLUSHALL - 209)) | (1 << (RedisParser.SELECT - 209)) | (1 << (RedisParser.QUIT - 209)) | (1 << (RedisParser.AUTH - 209)) | (1 << (RedisParser.DBSIZE - 209)) | (1 << (RedisParser.BGREWRITEAOF - 209)))) !== 0) || ((((_la - 241)) & ~0x1F) === 0 && ((1 << (_la - 241)) & ((1 << (RedisParser.TIME - 241)) | (1 << (RedisParser.INFO - 241)) | (1 << (RedisParser.BGSAVE - 241)) | (1 << (RedisParser.COMMAND - 241)) | (1 << (RedisParser.DEBUG - 241)))) !== 0) || ((((_la - 276)) & ~0x1F) === 0 && ((1 << (_la - 276)) & ((1 << (RedisParser.RESETSTAT - 276)) | (1 << (RedisParser.REWRITE - 276)) | (1 << (RedisParser.SETNAME - 276)) | (1 << (RedisParser.GETNAME - 276)) | (1 << (RedisParser.LIST - 276)) | (1 << (RedisParser.GETKEYS - 276)) | (1 << (RedisParser.COUNT - 276)) | (1 << (RedisParser.KILL - 276)))) !== 0) || _la === RedisParser.SEMI) {
                    {
                        this.state = 392;
                        this.sqlStatements();
                    }
                }
                this.state = 395;
                this.match(RedisParser.EOF);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    sqlStatements() {
        let _localctx = new SqlStatementsContext(this._ctx, this.state);
        this.enterRule(_localctx, 2, RedisParser.RULE_sqlStatements);
        let _la;
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 401;
                this._errHandler.sync(this);
                _alt = this.interpreter.adaptivePredict(this._input, 2, this._ctx);
                while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
                    if (_alt === 1) {
                        {
                            this.state = 399;
                            this._errHandler.sync(this);
                            switch (this._input.LA(1)) {
                                case RedisParser.COPY:
                                case RedisParser.DEL:
                                case RedisParser.DUMP:
                                case RedisParser.EXISTS:
                                case RedisParser.EXPIRE:
                                case RedisParser.EXPIREAT:
                                case RedisParser.EXPIRETIME:
                                case RedisParser.KEYS:
                                case RedisParser.MOVE:
                                case RedisParser.OBJECT:
                                case RedisParser.PERSIST:
                                case RedisParser.PEXPIRE:
                                case RedisParser.PEXPIREAT:
                                case RedisParser.PEXPIRETIME:
                                case RedisParser.TTL:
                                case RedisParser.PTTL:
                                case RedisParser.RANDOMKEY:
                                case RedisParser.RENAME:
                                case RedisParser.RENAMENX:
                                case RedisParser.RESTORE:
                                case RedisParser.SCAN:
                                case RedisParser.SORT:
                                case RedisParser.SORT_RO:
                                case RedisParser.TOUCH:
                                case RedisParser.TYPE:
                                case RedisParser.UNLINK:
                                case RedisParser.WAIT:
                                case RedisParser.APPEND:
                                case RedisParser.DECR:
                                case RedisParser.DECRBY:
                                case RedisParser.GET:
                                case RedisParser.GETDEL:
                                case RedisParser.GETEX:
                                case RedisParser.GETRANGE:
                                case RedisParser.GETSET:
                                case RedisParser.GETBIT:
                                case RedisParser.INCR:
                                case RedisParser.INCRBY:
                                case RedisParser.INCRBYFLOAT:
                                case RedisParser.LCS:
                                case RedisParser.MGET:
                                case RedisParser.MSET:
                                case RedisParser.MSETNX:
                                case RedisParser.SETEX:
                                case RedisParser.PSETEX:
                                case RedisParser.SET:
                                case RedisParser.SETNX:
                                case RedisParser.SETRANGE:
                                case RedisParser.SETBIT:
                                case RedisParser.STRLEN:
                                case RedisParser.SUBSTR:
                                case RedisParser.HDEL:
                                case RedisParser.HEXISTS:
                                case RedisParser.HGET:
                                case RedisParser.HGETALL:
                                case RedisParser.HINCRBY:
                                case RedisParser.HINCRBYFLOAT:
                                case RedisParser.HKEYS:
                                case RedisParser.HLEN:
                                case RedisParser.HMGET:
                                case RedisParser.HMSET:
                                case RedisParser.HRANDFIELD:
                                case RedisParser.HSCAN:
                                case RedisParser.HSET:
                                case RedisParser.HSETNX:
                                case RedisParser.HSTRLEN:
                                case RedisParser.HVALS:
                                case RedisParser.BLMOVE:
                                case RedisParser.BLMPOP:
                                case RedisParser.BLPOP:
                                case RedisParser.BRPOP:
                                case RedisParser.BRPOPLPUSH:
                                case RedisParser.LINDEX:
                                case RedisParser.LINSERT:
                                case RedisParser.LLEN:
                                case RedisParser.LMOVE:
                                case RedisParser.LMPOP:
                                case RedisParser.LPOP:
                                case RedisParser.LPOS:
                                case RedisParser.LPUSH:
                                case RedisParser.LPUSHX:
                                case RedisParser.LRANGE:
                                case RedisParser.LREM:
                                case RedisParser.LSET:
                                case RedisParser.LTRIM:
                                case RedisParser.RPOP:
                                case RedisParser.RPOPLPUSH:
                                case RedisParser.RPUSH:
                                case RedisParser.RPUSHX:
                                case RedisParser.SADD:
                                case RedisParser.SCARD:
                                case RedisParser.SDIFF:
                                case RedisParser.SDIFFSTORE:
                                case RedisParser.SINTER:
                                case RedisParser.SINTERCARD:
                                case RedisParser.SINTERSTORE:
                                case RedisParser.SISMEMBER:
                                case RedisParser.SMEMBERS:
                                case RedisParser.SMISMEMBER:
                                case RedisParser.SMOVE:
                                case RedisParser.SPOP:
                                case RedisParser.SRANDMEMBER:
                                case RedisParser.SREM:
                                case RedisParser.SSCAN:
                                case RedisParser.SUNION:
                                case RedisParser.SUNIONSTORE:
                                case RedisParser.BZMPOP:
                                case RedisParser.BZPOPMAX:
                                case RedisParser.BZPOPMIN:
                                case RedisParser.ZADD:
                                case RedisParser.ZCARD:
                                case RedisParser.ZCOUNT:
                                case RedisParser.ZDIFF:
                                case RedisParser.ZDIFFSTORE:
                                case RedisParser.ZINCRBY:
                                case RedisParser.ZINTER:
                                case RedisParser.ZINTERCARD:
                                case RedisParser.ZINTERSTORE:
                                case RedisParser.ZLEXCOUNT:
                                case RedisParser.ZMPOP:
                                case RedisParser.ZMSCORE:
                                case RedisParser.ZPOPMAX:
                                case RedisParser.ZPOPMIN:
                                case RedisParser.ZRANDMEMBER:
                                case RedisParser.ZRANGE:
                                case RedisParser.ZRANGEBYLEX:
                                case RedisParser.ZRANGEBYSCORE:
                                case RedisParser.ZRANGESTORE:
                                case RedisParser.ZRANK:
                                case RedisParser.ZREM:
                                case RedisParser.ZREMRANGEBYLEX:
                                case RedisParser.ZREMRANGEBYRANK:
                                case RedisParser.ZREMRANGEBYSCORE:
                                case RedisParser.ZREVRANGE:
                                case RedisParser.ZREVRANGEBYLEX:
                                case RedisParser.ZREVRANGEBYSCORE:
                                case RedisParser.ZREVRANK:
                                case RedisParser.ZSCAN:
                                case RedisParser.ZSCORE:
                                case RedisParser.ZUNION:
                                case RedisParser.ZUNIONSTORE:
                                case RedisParser.PSUBSCRIBE:
                                case RedisParser.PUBSUB:
                                case RedisParser.PUBLISH:
                                case RedisParser.PUNSUBSCRIBE:
                                case RedisParser.SUBSCRIBE:
                                case RedisParser.UNSUBSCRIBE:
                                case RedisParser.SSUBSCRIBE:
                                case RedisParser.SUNSUBSCRIBE:
                                case RedisParser.ASKING:
                                case RedisParser.CLUSTER:
                                case RedisParser.READONLY:
                                case RedisParser.READWRITE:
                                case RedisParser.FLUSHDB:
                                case RedisParser.SCRIPT:
                                case RedisParser.EVAL:
                                case RedisParser.EVALSHA:
                                case RedisParser.EXEC:
                                case RedisParser.WATCH:
                                case RedisParser.DISCARD:
                                case RedisParser.UNWATCH:
                                case RedisParser.MULTI:
                                case RedisParser.PFMERGE:
                                case RedisParser.PFADD:
                                case RedisParser.PFCOUNT:
                                case RedisParser.ECHO:
                                case RedisParser.PING:
                                case RedisParser.SAVE:
                                case RedisParser.SLOWLOG:
                                case RedisParser.LASTSAVE:
                                case RedisParser.CONFIG:
                                case RedisParser.CLIENT:
                                case RedisParser.SHUTDOWN:
                                case RedisParser.SYNC:
                                case RedisParser.ROLE:
                                case RedisParser.MONITOR:
                                case RedisParser.SLAVEOF:
                                case RedisParser.FLUSHALL:
                                case RedisParser.SELECT:
                                case RedisParser.QUIT:
                                case RedisParser.AUTH:
                                case RedisParser.DBSIZE:
                                case RedisParser.BGREWRITEAOF:
                                case RedisParser.TIME:
                                case RedisParser.INFO:
                                case RedisParser.BGSAVE:
                                case RedisParser.COMMAND:
                                case RedisParser.DEBUG:
                                case RedisParser.RESETSTAT:
                                case RedisParser.REWRITE:
                                case RedisParser.SETNAME:
                                case RedisParser.GETNAME:
                                case RedisParser.LIST:
                                case RedisParser.GETKEYS:
                                case RedisParser.COUNT:
                                case RedisParser.KILL:
                                    {
                                        this.state = 397;
                                        this.sqlStatement();
                                    }
                                    break;
                                case RedisParser.SEMI:
                                    {
                                        this.state = 398;
                                        this.emptyStatement_();
                                    }
                                    break;
                                default:
                                    throw new NoViableAltException(this);
                            }
                        }
                    }
                    this.state = 403;
                    this._errHandler.sync(this);
                    _alt = this.interpreter.adaptivePredict(this._input, 2, this._ctx);
                }
                this.state = 409;
                this._errHandler.sync(this);
                switch (this._input.LA(1)) {
                    case RedisParser.COPY:
                    case RedisParser.DEL:
                    case RedisParser.DUMP:
                    case RedisParser.EXISTS:
                    case RedisParser.EXPIRE:
                    case RedisParser.EXPIREAT:
                    case RedisParser.EXPIRETIME:
                    case RedisParser.KEYS:
                    case RedisParser.MOVE:
                    case RedisParser.OBJECT:
                    case RedisParser.PERSIST:
                    case RedisParser.PEXPIRE:
                    case RedisParser.PEXPIREAT:
                    case RedisParser.PEXPIRETIME:
                    case RedisParser.TTL:
                    case RedisParser.PTTL:
                    case RedisParser.RANDOMKEY:
                    case RedisParser.RENAME:
                    case RedisParser.RENAMENX:
                    case RedisParser.RESTORE:
                    case RedisParser.SCAN:
                    case RedisParser.SORT:
                    case RedisParser.SORT_RO:
                    case RedisParser.TOUCH:
                    case RedisParser.TYPE:
                    case RedisParser.UNLINK:
                    case RedisParser.WAIT:
                    case RedisParser.APPEND:
                    case RedisParser.DECR:
                    case RedisParser.DECRBY:
                    case RedisParser.GET:
                    case RedisParser.GETDEL:
                    case RedisParser.GETEX:
                    case RedisParser.GETRANGE:
                    case RedisParser.GETSET:
                    case RedisParser.GETBIT:
                    case RedisParser.INCR:
                    case RedisParser.INCRBY:
                    case RedisParser.INCRBYFLOAT:
                    case RedisParser.LCS:
                    case RedisParser.MGET:
                    case RedisParser.MSET:
                    case RedisParser.MSETNX:
                    case RedisParser.SETEX:
                    case RedisParser.PSETEX:
                    case RedisParser.SET:
                    case RedisParser.SETNX:
                    case RedisParser.SETRANGE:
                    case RedisParser.SETBIT:
                    case RedisParser.STRLEN:
                    case RedisParser.SUBSTR:
                    case RedisParser.HDEL:
                    case RedisParser.HEXISTS:
                    case RedisParser.HGET:
                    case RedisParser.HGETALL:
                    case RedisParser.HINCRBY:
                    case RedisParser.HINCRBYFLOAT:
                    case RedisParser.HKEYS:
                    case RedisParser.HLEN:
                    case RedisParser.HMGET:
                    case RedisParser.HMSET:
                    case RedisParser.HRANDFIELD:
                    case RedisParser.HSCAN:
                    case RedisParser.HSET:
                    case RedisParser.HSETNX:
                    case RedisParser.HSTRLEN:
                    case RedisParser.HVALS:
                    case RedisParser.BLMOVE:
                    case RedisParser.BLMPOP:
                    case RedisParser.BLPOP:
                    case RedisParser.BRPOP:
                    case RedisParser.BRPOPLPUSH:
                    case RedisParser.LINDEX:
                    case RedisParser.LINSERT:
                    case RedisParser.LLEN:
                    case RedisParser.LMOVE:
                    case RedisParser.LMPOP:
                    case RedisParser.LPOP:
                    case RedisParser.LPOS:
                    case RedisParser.LPUSH:
                    case RedisParser.LPUSHX:
                    case RedisParser.LRANGE:
                    case RedisParser.LREM:
                    case RedisParser.LSET:
                    case RedisParser.LTRIM:
                    case RedisParser.RPOP:
                    case RedisParser.RPOPLPUSH:
                    case RedisParser.RPUSH:
                    case RedisParser.RPUSHX:
                    case RedisParser.SADD:
                    case RedisParser.SCARD:
                    case RedisParser.SDIFF:
                    case RedisParser.SDIFFSTORE:
                    case RedisParser.SINTER:
                    case RedisParser.SINTERCARD:
                    case RedisParser.SINTERSTORE:
                    case RedisParser.SISMEMBER:
                    case RedisParser.SMEMBERS:
                    case RedisParser.SMISMEMBER:
                    case RedisParser.SMOVE:
                    case RedisParser.SPOP:
                    case RedisParser.SRANDMEMBER:
                    case RedisParser.SREM:
                    case RedisParser.SSCAN:
                    case RedisParser.SUNION:
                    case RedisParser.SUNIONSTORE:
                    case RedisParser.BZMPOP:
                    case RedisParser.BZPOPMAX:
                    case RedisParser.BZPOPMIN:
                    case RedisParser.ZADD:
                    case RedisParser.ZCARD:
                    case RedisParser.ZCOUNT:
                    case RedisParser.ZDIFF:
                    case RedisParser.ZDIFFSTORE:
                    case RedisParser.ZINCRBY:
                    case RedisParser.ZINTER:
                    case RedisParser.ZINTERCARD:
                    case RedisParser.ZINTERSTORE:
                    case RedisParser.ZLEXCOUNT:
                    case RedisParser.ZMPOP:
                    case RedisParser.ZMSCORE:
                    case RedisParser.ZPOPMAX:
                    case RedisParser.ZPOPMIN:
                    case RedisParser.ZRANDMEMBER:
                    case RedisParser.ZRANGE:
                    case RedisParser.ZRANGEBYLEX:
                    case RedisParser.ZRANGEBYSCORE:
                    case RedisParser.ZRANGESTORE:
                    case RedisParser.ZRANK:
                    case RedisParser.ZREM:
                    case RedisParser.ZREMRANGEBYLEX:
                    case RedisParser.ZREMRANGEBYRANK:
                    case RedisParser.ZREMRANGEBYSCORE:
                    case RedisParser.ZREVRANGE:
                    case RedisParser.ZREVRANGEBYLEX:
                    case RedisParser.ZREVRANGEBYSCORE:
                    case RedisParser.ZREVRANK:
                    case RedisParser.ZSCAN:
                    case RedisParser.ZSCORE:
                    case RedisParser.ZUNION:
                    case RedisParser.ZUNIONSTORE:
                    case RedisParser.PSUBSCRIBE:
                    case RedisParser.PUBSUB:
                    case RedisParser.PUBLISH:
                    case RedisParser.PUNSUBSCRIBE:
                    case RedisParser.SUBSCRIBE:
                    case RedisParser.UNSUBSCRIBE:
                    case RedisParser.SSUBSCRIBE:
                    case RedisParser.SUNSUBSCRIBE:
                    case RedisParser.ASKING:
                    case RedisParser.CLUSTER:
                    case RedisParser.READONLY:
                    case RedisParser.READWRITE:
                    case RedisParser.FLUSHDB:
                    case RedisParser.SCRIPT:
                    case RedisParser.EVAL:
                    case RedisParser.EVALSHA:
                    case RedisParser.EXEC:
                    case RedisParser.WATCH:
                    case RedisParser.DISCARD:
                    case RedisParser.UNWATCH:
                    case RedisParser.MULTI:
                    case RedisParser.PFMERGE:
                    case RedisParser.PFADD:
                    case RedisParser.PFCOUNT:
                    case RedisParser.ECHO:
                    case RedisParser.PING:
                    case RedisParser.SAVE:
                    case RedisParser.SLOWLOG:
                    case RedisParser.LASTSAVE:
                    case RedisParser.CONFIG:
                    case RedisParser.CLIENT:
                    case RedisParser.SHUTDOWN:
                    case RedisParser.SYNC:
                    case RedisParser.ROLE:
                    case RedisParser.MONITOR:
                    case RedisParser.SLAVEOF:
                    case RedisParser.FLUSHALL:
                    case RedisParser.SELECT:
                    case RedisParser.QUIT:
                    case RedisParser.AUTH:
                    case RedisParser.DBSIZE:
                    case RedisParser.BGREWRITEAOF:
                    case RedisParser.TIME:
                    case RedisParser.INFO:
                    case RedisParser.BGSAVE:
                    case RedisParser.COMMAND:
                    case RedisParser.DEBUG:
                    case RedisParser.RESETSTAT:
                    case RedisParser.REWRITE:
                    case RedisParser.SETNAME:
                    case RedisParser.GETNAME:
                    case RedisParser.LIST:
                    case RedisParser.GETKEYS:
                    case RedisParser.COUNT:
                    case RedisParser.KILL:
                        {
                            this.state = 404;
                            this.sqlStatement();
                            this.state = 406;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            if (_la === RedisParser.SEMI) {
                                {
                                    this.state = 405;
                                    this.match(RedisParser.SEMI);
                                }
                            }
                        }
                        break;
                    case RedisParser.SEMI:
                        {
                            this.state = 408;
                            this.emptyStatement_();
                        }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    emptyStatement_() {
        let _localctx = new EmptyStatement_Context(this._ctx, this.state);
        this.enterRule(_localctx, 4, RedisParser.RULE_emptyStatement_);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 411;
                this.match(RedisParser.SEMI);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    keyName() {
        let _localctx = new KeyNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 6, RedisParser.RULE_keyName);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 413;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    fieldName() {
        let _localctx = new FieldNameContext(this._ctx, this.state);
        this.enterRule(_localctx, 8, RedisParser.RULE_fieldName);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 415;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    stringValue() {
        let _localctx = new StringValueContext(this._ctx, this.state);
        this.enterRule(_localctx, 10, RedisParser.RULE_stringValue);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 417;
                this.match(RedisParser.STRING);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    intValue() {
        let _localctx = new IntValueContext(this._ctx, this.state);
        this.enterRule(_localctx, 12, RedisParser.RULE_intValue);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 419;
                this.match(RedisParser.INTEGER_NUM);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    floatValue() {
        let _localctx = new FloatValueContext(this._ctx, this.state);
        this.enterRule(_localctx, 14, RedisParser.RULE_floatValue);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 421;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.INTEGER_NUM || _la === RedisParser.DECIMAL_NUM)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    pattern() {
        let _localctx = new PatternContext(this._ctx, this.state);
        this.enterRule(_localctx, 16, RedisParser.RULE_pattern);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 423;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cursor() {
        let _localctx = new CursorContext(this._ctx, this.state);
        this.enterRule(_localctx, 18, RedisParser.RULE_cursor);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 425;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    min() {
        let _localctx = new MinContext(this._ctx, this.state);
        this.enterRule(_localctx, 20, RedisParser.RULE_min);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 427;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    max() {
        let _localctx = new MaxContext(this._ctx, this.state);
        this.enterRule(_localctx, 22, RedisParser.RULE_max);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 429;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    multiString() {
        let _localctx = new MultiStringContext(this._ctx, this.state);
        this.enterRule(_localctx, 24, RedisParser.RULE_multiString);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 431;
                this.match(RedisParser.STRING);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cout() {
        let _localctx = new CoutContext(this._ctx, this.state);
        this.enterRule(_localctx, 26, RedisParser.RULE_cout);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 433;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    start() {
        let _localctx = new StartContext(this._ctx, this.state);
        this.enterRule(_localctx, 28, RedisParser.RULE_start);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 435;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    end() {
        let _localctx = new EndContext(this._ctx, this.state);
        this.enterRule(_localctx, 30, RedisParser.RULE_end);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 437;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    port() {
        let _localctx = new PortContext(this._ctx, this.state);
        this.enterRule(_localctx, 32, RedisParser.RULE_port);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 439;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    keyAndString() {
        let _localctx = new KeyAndStringContext(this._ctx, this.state);
        this.enterRule(_localctx, 34, RedisParser.RULE_keyAndString);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 441;
                this.keyName();
                this.state = 442;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    intAndString() {
        let _localctx = new IntAndStringContext(this._ctx, this.state);
        this.enterRule(_localctx, 36, RedisParser.RULE_intAndString);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 444;
                this.intValue();
                this.state = 445;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    infoOpt() {
        let _localctx = new InfoOptContext(this._ctx, this.state);
        this.enterRule(_localctx, 38, RedisParser.RULE_infoOpt);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 447;
                _la = this._input.LA(1);
                if (!(((((_la - 160)) & ~0x1F) === 0 && ((1 << (_la - 160)) & ((1 << (RedisParser.SERVER - 160)) | (1 << (RedisParser.CLIENTS - 160)) | (1 << (RedisParser.MEMORY - 160)) | (1 << (RedisParser.PERSISTENCE - 160)) | (1 << (RedisParser.STATS - 160)) | (1 << (RedisParser.REPLICATION - 160)) | (1 << (RedisParser.CPU - 160)) | (1 << (RedisParser.COMMANDSTATS - 160)) | (1 << (RedisParser.LATENCYSTATS - 160)) | (1 << (RedisParser.SENTINEL - 160)) | (1 << (RedisParser.MODULES - 160)) | (1 << (RedisParser.KEYSPACE - 160)) | (1 << (RedisParser.ERRORSTATS - 160)) | (1 << (RedisParser.ALL - 160)) | (1 << (RedisParser.DEFAULT - 160)) | (1 << (RedisParser.EVERYTHING - 160)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    patternOpt() {
        let _localctx = new PatternOptContext(this._ctx, this.state);
        this.enterRule(_localctx, 40, RedisParser.RULE_patternOpt);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 449;
                this.match(RedisParser.GET);
                this.state = 450;
                this.pattern();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    keyOpt() {
        let _localctx = new KeyOptContext(this._ctx, this.state);
        this.enterRule(_localctx, 42, RedisParser.RULE_keyOpt);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 452;
                _la = this._input.LA(1);
                if (!(((((_la - 247)) & ~0x1F) === 0 && ((1 << (_la - 247)) & ((1 << (RedisParser.NX - 247)) | (1 << (RedisParser.XX - 247)) | (1 << (RedisParser.GT - 247)) | (1 << (RedisParser.LT - 247)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    idletimeOpt() {
        let _localctx = new IdletimeOptContext(this._ctx, this.state);
        this.enterRule(_localctx, 44, RedisParser.RULE_idletimeOpt);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 454;
                this.match(RedisParser.IDLETIME);
                this.state = 455;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    freqOpt() {
        let _localctx = new FreqOptContext(this._ctx, this.state);
        this.enterRule(_localctx, 46, RedisParser.RULE_freqOpt);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 457;
                this.match(RedisParser.FREQ);
                this.state = 458;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    limitOpt() {
        let _localctx = new LimitOptContext(this._ctx, this.state);
        this.enterRule(_localctx, 48, RedisParser.RULE_limitOpt);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 460;
                this.match(RedisParser.LIMIT);
                this.state = 461;
                this.intValue();
                this.state = 462;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    sortOpt() {
        let _localctx = new SortOptContext(this._ctx, this.state);
        this.enterRule(_localctx, 50, RedisParser.RULE_sortOpt);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 464;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.ASC || _la === RedisParser.DESC)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    getsetOpt() {
        let _localctx = new GetsetOptContext(this._ctx, this.state);
        this.enterRule(_localctx, 52, RedisParser.RULE_getsetOpt);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 466;
                _la = this._input.LA(1);
                if (!(((((_la - 262)) & ~0x1F) === 0 && ((1 << (_la - 262)) & ((1 << (RedisParser.EX - 262)) | (1 << (RedisParser.PX - 262)) | (1 << (RedisParser.EXAT - 262)) | (1 << (RedisParser.PXAT - 262)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 467;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    lrOpt() {
        let _localctx = new LrOptContext(this._ctx, this.state);
        this.enterRule(_localctx, 54, RedisParser.RULE_lrOpt);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 469;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.LEFT || _la === RedisParser.RIGHT)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    rankOpt() {
        let _localctx = new RankOptContext(this._ctx, this.state);
        this.enterRule(_localctx, 56, RedisParser.RULE_rankOpt);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 471;
                this.match(RedisParser.RANK);
                this.state = 472;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    countOpt() {
        let _localctx = new CountOptContext(this._ctx, this.state);
        this.enterRule(_localctx, 58, RedisParser.RULE_countOpt);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 474;
                this.match(RedisParser.COUNT);
                this.state = 475;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    maxlenOpt() {
        let _localctx = new MaxlenOptContext(this._ctx, this.state);
        this.enterRule(_localctx, 60, RedisParser.RULE_maxlenOpt);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 477;
                this.match(RedisParser.MAXLEN);
                this.state = 478;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdCopy() {
        let _localctx = new CmdCopyContext(this._ctx, this.state);
        this.enterRule(_localctx, 62, RedisParser.RULE_cmdCopy);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 480;
                this.match(RedisParser.COPY);
                this.state = 481;
                this.keyName();
                this.state = 482;
                this.keyName();
                this.state = 485;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.DB) {
                    {
                        this.state = 483;
                        this.match(RedisParser.DB);
                        this.state = 484;
                        this.intValue();
                    }
                }
                this.state = 488;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.REPLACE) {
                    {
                        this.state = 487;
                        this.match(RedisParser.REPLACE);
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdDel() {
        let _localctx = new CmdDelContext(this._ctx, this.state);
        this.enterRule(_localctx, 64, RedisParser.RULE_cmdDel);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 490;
                this.match(RedisParser.DEL);
                this.state = 492;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 491;
                            this.keyName();
                        }
                    }
                    this.state = 494;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdDump() {
        let _localctx = new CmdDumpContext(this._ctx, this.state);
        this.enterRule(_localctx, 66, RedisParser.RULE_cmdDump);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 496;
                this.match(RedisParser.DUMP);
                this.state = 497;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdExists() {
        let _localctx = new CmdExistsContext(this._ctx, this.state);
        this.enterRule(_localctx, 68, RedisParser.RULE_cmdExists);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 499;
                this.match(RedisParser.EXISTS);
                this.state = 501;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 500;
                            this.keyName();
                        }
                    }
                    this.state = 503;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdExpire() {
        let _localctx = new CmdExpireContext(this._ctx, this.state);
        this.enterRule(_localctx, 70, RedisParser.RULE_cmdExpire);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 505;
                this.match(RedisParser.EXPIRE);
                this.state = 506;
                this.keyName();
                this.state = 507;
                this.intValue();
                this.state = 509;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (((((_la - 247)) & ~0x1F) === 0 && ((1 << (_la - 247)) & ((1 << (RedisParser.NX - 247)) | (1 << (RedisParser.XX - 247)) | (1 << (RedisParser.GT - 247)) | (1 << (RedisParser.LT - 247)))) !== 0)) {
                    {
                        this.state = 508;
                        this.keyOpt();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdExpireat() {
        let _localctx = new CmdExpireatContext(this._ctx, this.state);
        this.enterRule(_localctx, 72, RedisParser.RULE_cmdExpireat);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 511;
                this.match(RedisParser.EXPIREAT);
                this.state = 512;
                this.keyName();
                this.state = 513;
                this.intValue();
                this.state = 515;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (((((_la - 247)) & ~0x1F) === 0 && ((1 << (_la - 247)) & ((1 << (RedisParser.NX - 247)) | (1 << (RedisParser.XX - 247)) | (1 << (RedisParser.GT - 247)) | (1 << (RedisParser.LT - 247)))) !== 0)) {
                    {
                        this.state = 514;
                        this.keyOpt();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdExpireTime() {
        let _localctx = new CmdExpireTimeContext(this._ctx, this.state);
        this.enterRule(_localctx, 74, RedisParser.RULE_cmdExpireTime);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 517;
                this.match(RedisParser.EXPIRETIME);
                this.state = 518;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdKeys() {
        let _localctx = new CmdKeysContext(this._ctx, this.state);
        this.enterRule(_localctx, 76, RedisParser.RULE_cmdKeys);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 520;
                this.match(RedisParser.KEYS);
                this.state = 521;
                this.pattern();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdMove() {
        let _localctx = new CmdMoveContext(this._ctx, this.state);
        this.enterRule(_localctx, 78, RedisParser.RULE_cmdMove);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 523;
                this.match(RedisParser.MOVE);
                this.state = 524;
                this.keyName();
                this.state = 525;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdObject() {
        let _localctx = new CmdObjectContext(this._ctx, this.state);
        this.enterRule(_localctx, 80, RedisParser.RULE_cmdObject);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 527;
                this.match(RedisParser.OBJECT);
                this.state = 528;
                _la = this._input.LA(1);
                if (!(((((_la - 251)) & ~0x1F) === 0 && ((1 << (_la - 251)) & ((1 << (RedisParser.ENCODING - 251)) | (1 << (RedisParser.FREQ - 251)) | (1 << (RedisParser.IDLETIME - 251)) | (1 << (RedisParser.REFCOUNT - 251)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 529;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPersist() {
        let _localctx = new CmdPersistContext(this._ctx, this.state);
        this.enterRule(_localctx, 82, RedisParser.RULE_cmdPersist);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 531;
                this.match(RedisParser.PERSIST);
                this.state = 532;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPexpire() {
        let _localctx = new CmdPexpireContext(this._ctx, this.state);
        this.enterRule(_localctx, 84, RedisParser.RULE_cmdPexpire);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 534;
                this.match(RedisParser.PEXPIRE);
                this.state = 535;
                this.keyName();
                this.state = 536;
                this.intValue();
                this.state = 538;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (((((_la - 247)) & ~0x1F) === 0 && ((1 << (_la - 247)) & ((1 << (RedisParser.NX - 247)) | (1 << (RedisParser.XX - 247)) | (1 << (RedisParser.GT - 247)) | (1 << (RedisParser.LT - 247)))) !== 0)) {
                    {
                        this.state = 537;
                        this.keyOpt();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPexpireat() {
        let _localctx = new CmdPexpireatContext(this._ctx, this.state);
        this.enterRule(_localctx, 86, RedisParser.RULE_cmdPexpireat);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 540;
                this.match(RedisParser.PEXPIREAT);
                this.state = 541;
                this.keyName();
                this.state = 542;
                this.intValue();
                this.state = 544;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (((((_la - 247)) & ~0x1F) === 0 && ((1 << (_la - 247)) & ((1 << (RedisParser.NX - 247)) | (1 << (RedisParser.XX - 247)) | (1 << (RedisParser.GT - 247)) | (1 << (RedisParser.LT - 247)))) !== 0)) {
                    {
                        this.state = 543;
                        this.keyOpt();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPExpireTime() {
        let _localctx = new CmdPExpireTimeContext(this._ctx, this.state);
        this.enterRule(_localctx, 88, RedisParser.RULE_cmdPExpireTime);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 546;
                this.match(RedisParser.PEXPIRETIME);
                this.state = 547;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdTtl() {
        let _localctx = new CmdTtlContext(this._ctx, this.state);
        this.enterRule(_localctx, 90, RedisParser.RULE_cmdTtl);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 549;
                this.match(RedisParser.TTL);
                this.state = 550;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPttl() {
        let _localctx = new CmdPttlContext(this._ctx, this.state);
        this.enterRule(_localctx, 92, RedisParser.RULE_cmdPttl);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 552;
                this.match(RedisParser.PTTL);
                this.state = 553;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdRandomkey() {
        let _localctx = new CmdRandomkeyContext(this._ctx, this.state);
        this.enterRule(_localctx, 94, RedisParser.RULE_cmdRandomkey);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 555;
                this.match(RedisParser.RANDOMKEY);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdRename() {
        let _localctx = new CmdRenameContext(this._ctx, this.state);
        this.enterRule(_localctx, 96, RedisParser.RULE_cmdRename);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 557;
                this.match(RedisParser.RENAME);
                this.state = 558;
                this.keyName();
                this.state = 559;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdRenamenx() {
        let _localctx = new CmdRenamenxContext(this._ctx, this.state);
        this.enterRule(_localctx, 98, RedisParser.RULE_cmdRenamenx);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 561;
                this.match(RedisParser.RENAMENX);
                this.state = 562;
                this.keyName();
                this.state = 563;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdRestore() {
        let _localctx = new CmdRestoreContext(this._ctx, this.state);
        this.enterRule(_localctx, 100, RedisParser.RULE_cmdRestore);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 565;
                this.match(RedisParser.RESTORE);
                this.state = 566;
                this.keyName();
                this.state = 567;
                this.intValue();
                this.state = 568;
                this.stringValue();
                this.state = 570;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.REPLACE) {
                    {
                        this.state = 569;
                        this.match(RedisParser.REPLACE);
                    }
                }
                this.state = 573;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.ABSTTL) {
                    {
                        this.state = 572;
                        this.match(RedisParser.ABSTTL);
                    }
                }
                this.state = 576;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.IDLETIME) {
                    {
                        this.state = 575;
                        this.idletimeOpt();
                    }
                }
                this.state = 579;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.FREQ) {
                    {
                        this.state = 578;
                        this.freqOpt();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdScan() {
        let _localctx = new CmdScanContext(this._ctx, this.state);
        this.enterRule(_localctx, 102, RedisParser.RULE_cmdScan);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 581;
                this.match(RedisParser.SCAN);
                this.state = 582;
                this.cursor();
                this.state = 583;
                this.pattern();
                this.state = 585;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.INTEGER_NUM) {
                    {
                        this.state = 584;
                        this.intValue();
                    }
                }
                this.state = 589;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 18, this._ctx)) {
                    case 1:
                        {
                            this.state = 587;
                            this.match(RedisParser.TYPE);
                            this.state = 588;
                            this.stringValue();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSort() {
        let _localctx = new CmdSortContext(this._ctx, this.state);
        this.enterRule(_localctx, 104, RedisParser.RULE_cmdSort);
        let _la;
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 591;
                this.match(RedisParser.SORT);
                this.state = 592;
                this.keyName();
                this.state = 595;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.BY) {
                    {
                        this.state = 593;
                        this.match(RedisParser.BY);
                        this.state = 594;
                        this.pattern();
                    }
                }
                this.state = 598;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.LIMIT) {
                    {
                        this.state = 597;
                        this.limitOpt();
                    }
                }
                this.state = 605;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 22, this._ctx)) {
                    case 1:
                        {
                            this.state = 601;
                            this._errHandler.sync(this);
                            _alt = 1;
                            do {
                                switch (_alt) {
                                    case 1:
                                        {
                                            {
                                                this.state = 600;
                                                this.patternOpt();
                                            }
                                        }
                                        break;
                                    default:
                                        throw new NoViableAltException(this);
                                }
                                this.state = 603;
                                this._errHandler.sync(this);
                                _alt = this.interpreter.adaptivePredict(this._input, 21, this._ctx);
                            } while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER);
                        }
                        break;
                }
                this.state = 608;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.ASC || _la === RedisParser.DESC) {
                    {
                        this.state = 607;
                        this.sortOpt();
                    }
                }
                this.state = 611;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.ALPHA) {
                    {
                        this.state = 610;
                        this.match(RedisParser.ALPHA);
                    }
                }
                this.state = 615;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.STORE) {
                    {
                        this.state = 613;
                        this.match(RedisParser.STORE);
                        this.state = 614;
                        this.stringValue();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSortro() {
        let _localctx = new CmdSortroContext(this._ctx, this.state);
        this.enterRule(_localctx, 106, RedisParser.RULE_cmdSortro);
        let _la;
        try {
            let _alt;
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 617;
                this.match(RedisParser.SORT_RO);
                this.state = 618;
                this.keyName();
                this.state = 621;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.BY) {
                    {
                        this.state = 619;
                        this.match(RedisParser.BY);
                        this.state = 620;
                        this.pattern();
                    }
                }
                this.state = 624;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.LIMIT) {
                    {
                        this.state = 623;
                        this.limitOpt();
                    }
                }
                this.state = 631;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 29, this._ctx)) {
                    case 1:
                        {
                            this.state = 627;
                            this._errHandler.sync(this);
                            _alt = 1;
                            do {
                                switch (_alt) {
                                    case 1:
                                        {
                                            {
                                                this.state = 626;
                                                this.patternOpt();
                                            }
                                        }
                                        break;
                                    default:
                                        throw new NoViableAltException(this);
                                }
                                this.state = 629;
                                this._errHandler.sync(this);
                                _alt = this.interpreter.adaptivePredict(this._input, 28, this._ctx);
                            } while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER);
                        }
                        break;
                }
                this.state = 634;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.ASC || _la === RedisParser.DESC) {
                    {
                        this.state = 633;
                        this.sortOpt();
                    }
                }
                this.state = 637;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.ALPHA) {
                    {
                        this.state = 636;
                        this.match(RedisParser.ALPHA);
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdTouch() {
        let _localctx = new CmdTouchContext(this._ctx, this.state);
        this.enterRule(_localctx, 108, RedisParser.RULE_cmdTouch);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 639;
                this.match(RedisParser.TOUCH);
                this.state = 641;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 640;
                            this.keyName();
                        }
                    }
                    this.state = 643;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdType() {
        let _localctx = new CmdTypeContext(this._ctx, this.state);
        this.enterRule(_localctx, 110, RedisParser.RULE_cmdType);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 645;
                this.match(RedisParser.TYPE);
                this.state = 646;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdUnlink() {
        let _localctx = new CmdUnlinkContext(this._ctx, this.state);
        this.enterRule(_localctx, 112, RedisParser.RULE_cmdUnlink);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 648;
                this.match(RedisParser.UNLINK);
                this.state = 650;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 649;
                            this.keyName();
                        }
                    }
                    this.state = 652;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdWait() {
        let _localctx = new CmdWaitContext(this._ctx, this.state);
        this.enterRule(_localctx, 114, RedisParser.RULE_cmdWait);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 654;
                this.match(RedisParser.WAIT);
                this.state = 655;
                this.intValue();
                this.state = 656;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdAppend() {
        let _localctx = new CmdAppendContext(this._ctx, this.state);
        this.enterRule(_localctx, 116, RedisParser.RULE_cmdAppend);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 658;
                this.match(RedisParser.APPEND);
                this.state = 659;
                this.keyName();
                this.state = 660;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdDecr() {
        let _localctx = new CmdDecrContext(this._ctx, this.state);
        this.enterRule(_localctx, 118, RedisParser.RULE_cmdDecr);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 662;
                this.match(RedisParser.DECR);
                this.state = 663;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdDecrby() {
        let _localctx = new CmdDecrbyContext(this._ctx, this.state);
        this.enterRule(_localctx, 120, RedisParser.RULE_cmdDecrby);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 665;
                this.match(RedisParser.DECRBY);
                this.state = 666;
                this.keyName();
                this.state = 667;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdGet() {
        let _localctx = new CmdGetContext(this._ctx, this.state);
        this.enterRule(_localctx, 122, RedisParser.RULE_cmdGet);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 669;
                this.match(RedisParser.GET);
                this.state = 670;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdGetdel() {
        let _localctx = new CmdGetdelContext(this._ctx, this.state);
        this.enterRule(_localctx, 124, RedisParser.RULE_cmdGetdel);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 672;
                this.match(RedisParser.GETDEL);
                this.state = 673;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdGetex() {
        let _localctx = new CmdGetexContext(this._ctx, this.state);
        this.enterRule(_localctx, 126, RedisParser.RULE_cmdGetex);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 675;
                this.match(RedisParser.GETEX);
                this.state = 676;
                this.keyName();
                this.state = 679;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 34, this._ctx)) {
                    case 1:
                        {
                            this.state = 677;
                            this.getsetOpt();
                        }
                        break;
                    case 2:
                        {
                            this.state = 678;
                            this.match(RedisParser.PERSIST);
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdGetrange() {
        let _localctx = new CmdGetrangeContext(this._ctx, this.state);
        this.enterRule(_localctx, 128, RedisParser.RULE_cmdGetrange);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 681;
                this.match(RedisParser.GETRANGE);
                this.state = 682;
                this.keyName();
                this.state = 683;
                this.intValue();
                this.state = 684;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdGetset() {
        let _localctx = new CmdGetsetContext(this._ctx, this.state);
        this.enterRule(_localctx, 130, RedisParser.RULE_cmdGetset);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 686;
                this.match(RedisParser.GETSET);
                this.state = 687;
                this.keyName();
                this.state = 688;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdGetbit() {
        let _localctx = new CmdGetbitContext(this._ctx, this.state);
        this.enterRule(_localctx, 132, RedisParser.RULE_cmdGetbit);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 690;
                this.match(RedisParser.GETBIT);
                this.state = 691;
                this.keyName();
                this.state = 692;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdInc() {
        let _localctx = new CmdIncContext(this._ctx, this.state);
        this.enterRule(_localctx, 134, RedisParser.RULE_cmdInc);
        try {
            this.state = 704;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case RedisParser.INCR:
                    _localctx = new CmdIncrContext(_localctx);
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 694;
                        this.match(RedisParser.INCR);
                        this.state = 695;
                        this.keyName();
                    }
                    break;
                case RedisParser.INCRBY:
                    _localctx = new CmdIncrbyContext(_localctx);
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 696;
                        this.match(RedisParser.INCRBY);
                        this.state = 697;
                        this.keyName();
                        this.state = 698;
                        this.intValue();
                    }
                    break;
                case RedisParser.INCRBYFLOAT:
                    _localctx = new CmdIncrbyFloatContext(_localctx);
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 700;
                        this.match(RedisParser.INCRBYFLOAT);
                        this.state = 701;
                        this.keyName();
                        this.state = 702;
                        this.floatValue();
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdLcs() {
        let _localctx = new CmdLcsContext(this._ctx, this.state);
        this.enterRule(_localctx, 136, RedisParser.RULE_cmdLcs);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 706;
                this.match(RedisParser.LCS);
                this.state = 707;
                this.keyName();
                this.state = 708;
                this.keyName();
                this.state = 710;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.LEN) {
                    {
                        this.state = 709;
                        this.match(RedisParser.LEN);
                    }
                }
                this.state = 713;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.IDX) {
                    {
                        this.state = 712;
                        this.match(RedisParser.IDX);
                    }
                }
                this.state = 717;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.MINMATCHLEN) {
                    {
                        this.state = 715;
                        this.match(RedisParser.MINMATCHLEN);
                        this.state = 716;
                        this.intValue();
                    }
                }
                this.state = 720;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.WITHMATCHLEN) {
                    {
                        this.state = 719;
                        this.match(RedisParser.WITHMATCHLEN);
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdMget() {
        let _localctx = new CmdMgetContext(this._ctx, this.state);
        this.enterRule(_localctx, 138, RedisParser.RULE_cmdMget);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 722;
                this.match(RedisParser.MGET);
                this.state = 724;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 723;
                            this.keyName();
                        }
                    }
                    this.state = 726;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdMset() {
        let _localctx = new CmdMsetContext(this._ctx, this.state);
        this.enterRule(_localctx, 140, RedisParser.RULE_cmdMset);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 728;
                this.match(RedisParser.MSET);
                this.state = 730;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 729;
                            this.keyAndString();
                        }
                    }
                    this.state = 732;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdMsetnx() {
        let _localctx = new CmdMsetnxContext(this._ctx, this.state);
        this.enterRule(_localctx, 142, RedisParser.RULE_cmdMsetnx);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 734;
                this.match(RedisParser.MSETNX);
                this.state = 736;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 735;
                            this.keyAndString();
                        }
                    }
                    this.state = 738;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSetex() {
        let _localctx = new CmdSetexContext(this._ctx, this.state);
        this.enterRule(_localctx, 144, RedisParser.RULE_cmdSetex);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 740;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.SETEX || _la === RedisParser.PSETEX)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 741;
                this.keyName();
                this.state = 742;
                this.intValue();
                this.state = 743;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSet() {
        let _localctx = new CmdSetContext(this._ctx, this.state);
        this.enterRule(_localctx, 146, RedisParser.RULE_cmdSet);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 745;
                this.match(RedisParser.SET);
                this.state = 746;
                this.keyName();
                this.state = 747;
                this.stringValue();
                this.state = 750;
                this._errHandler.sync(this);
                switch (this._input.LA(1)) {
                    case RedisParser.EX:
                    case RedisParser.PX:
                    case RedisParser.EXAT:
                    case RedisParser.PXAT:
                        {
                            this.state = 748;
                            this.getsetOpt();
                        }
                        break;
                    case RedisParser.KEEPTTL:
                        {
                            this.state = 749;
                            this.match(RedisParser.KEEPTTL);
                        }
                        break;
                    case RedisParser.EOF:
                    case RedisParser.COPY:
                    case RedisParser.DEL:
                    case RedisParser.DUMP:
                    case RedisParser.EXISTS:
                    case RedisParser.EXPIRE:
                    case RedisParser.EXPIREAT:
                    case RedisParser.EXPIRETIME:
                    case RedisParser.KEYS:
                    case RedisParser.MOVE:
                    case RedisParser.OBJECT:
                    case RedisParser.PERSIST:
                    case RedisParser.PEXPIRE:
                    case RedisParser.PEXPIREAT:
                    case RedisParser.PEXPIRETIME:
                    case RedisParser.TTL:
                    case RedisParser.PTTL:
                    case RedisParser.RANDOMKEY:
                    case RedisParser.RENAME:
                    case RedisParser.RENAMENX:
                    case RedisParser.RESTORE:
                    case RedisParser.SCAN:
                    case RedisParser.SORT:
                    case RedisParser.SORT_RO:
                    case RedisParser.TOUCH:
                    case RedisParser.TYPE:
                    case RedisParser.UNLINK:
                    case RedisParser.WAIT:
                    case RedisParser.APPEND:
                    case RedisParser.DECR:
                    case RedisParser.DECRBY:
                    case RedisParser.GET:
                    case RedisParser.GETDEL:
                    case RedisParser.GETEX:
                    case RedisParser.GETRANGE:
                    case RedisParser.GETSET:
                    case RedisParser.GETBIT:
                    case RedisParser.INCR:
                    case RedisParser.INCRBY:
                    case RedisParser.INCRBYFLOAT:
                    case RedisParser.LCS:
                    case RedisParser.MGET:
                    case RedisParser.MSET:
                    case RedisParser.MSETNX:
                    case RedisParser.SETEX:
                    case RedisParser.PSETEX:
                    case RedisParser.SET:
                    case RedisParser.SETNX:
                    case RedisParser.SETRANGE:
                    case RedisParser.SETBIT:
                    case RedisParser.STRLEN:
                    case RedisParser.SUBSTR:
                    case RedisParser.HDEL:
                    case RedisParser.HEXISTS:
                    case RedisParser.HGET:
                    case RedisParser.HGETALL:
                    case RedisParser.HINCRBY:
                    case RedisParser.HINCRBYFLOAT:
                    case RedisParser.HKEYS:
                    case RedisParser.HLEN:
                    case RedisParser.HMGET:
                    case RedisParser.HMSET:
                    case RedisParser.HRANDFIELD:
                    case RedisParser.HSCAN:
                    case RedisParser.HSET:
                    case RedisParser.HSETNX:
                    case RedisParser.HSTRLEN:
                    case RedisParser.HVALS:
                    case RedisParser.BLMOVE:
                    case RedisParser.BLMPOP:
                    case RedisParser.BLPOP:
                    case RedisParser.BRPOP:
                    case RedisParser.BRPOPLPUSH:
                    case RedisParser.LINDEX:
                    case RedisParser.LINSERT:
                    case RedisParser.LLEN:
                    case RedisParser.LMOVE:
                    case RedisParser.LMPOP:
                    case RedisParser.LPOP:
                    case RedisParser.LPOS:
                    case RedisParser.LPUSH:
                    case RedisParser.LPUSHX:
                    case RedisParser.LRANGE:
                    case RedisParser.LREM:
                    case RedisParser.LSET:
                    case RedisParser.LTRIM:
                    case RedisParser.RPOP:
                    case RedisParser.RPOPLPUSH:
                    case RedisParser.RPUSH:
                    case RedisParser.RPUSHX:
                    case RedisParser.SADD:
                    case RedisParser.SCARD:
                    case RedisParser.SDIFF:
                    case RedisParser.SDIFFSTORE:
                    case RedisParser.SINTER:
                    case RedisParser.SINTERCARD:
                    case RedisParser.SINTERSTORE:
                    case RedisParser.SISMEMBER:
                    case RedisParser.SMEMBERS:
                    case RedisParser.SMISMEMBER:
                    case RedisParser.SMOVE:
                    case RedisParser.SPOP:
                    case RedisParser.SRANDMEMBER:
                    case RedisParser.SREM:
                    case RedisParser.SSCAN:
                    case RedisParser.SUNION:
                    case RedisParser.SUNIONSTORE:
                    case RedisParser.BZMPOP:
                    case RedisParser.BZPOPMAX:
                    case RedisParser.BZPOPMIN:
                    case RedisParser.ZADD:
                    case RedisParser.ZCARD:
                    case RedisParser.ZCOUNT:
                    case RedisParser.ZDIFF:
                    case RedisParser.ZDIFFSTORE:
                    case RedisParser.ZINCRBY:
                    case RedisParser.ZINTER:
                    case RedisParser.ZINTERCARD:
                    case RedisParser.ZINTERSTORE:
                    case RedisParser.ZLEXCOUNT:
                    case RedisParser.ZMPOP:
                    case RedisParser.ZMSCORE:
                    case RedisParser.ZPOPMAX:
                    case RedisParser.ZPOPMIN:
                    case RedisParser.ZRANDMEMBER:
                    case RedisParser.ZRANGE:
                    case RedisParser.ZRANGEBYLEX:
                    case RedisParser.ZRANGEBYSCORE:
                    case RedisParser.ZRANGESTORE:
                    case RedisParser.ZRANK:
                    case RedisParser.ZREM:
                    case RedisParser.ZREMRANGEBYLEX:
                    case RedisParser.ZREMRANGEBYRANK:
                    case RedisParser.ZREMRANGEBYSCORE:
                    case RedisParser.ZREVRANGE:
                    case RedisParser.ZREVRANGEBYLEX:
                    case RedisParser.ZREVRANGEBYSCORE:
                    case RedisParser.ZREVRANK:
                    case RedisParser.ZSCAN:
                    case RedisParser.ZSCORE:
                    case RedisParser.ZUNION:
                    case RedisParser.ZUNIONSTORE:
                    case RedisParser.PSUBSCRIBE:
                    case RedisParser.PUBSUB:
                    case RedisParser.PUBLISH:
                    case RedisParser.PUNSUBSCRIBE:
                    case RedisParser.SUBSCRIBE:
                    case RedisParser.UNSUBSCRIBE:
                    case RedisParser.SSUBSCRIBE:
                    case RedisParser.SUNSUBSCRIBE:
                    case RedisParser.ASKING:
                    case RedisParser.CLUSTER:
                    case RedisParser.READONLY:
                    case RedisParser.READWRITE:
                    case RedisParser.FLUSHDB:
                    case RedisParser.SCRIPT:
                    case RedisParser.EVAL:
                    case RedisParser.EVALSHA:
                    case RedisParser.EXEC:
                    case RedisParser.WATCH:
                    case RedisParser.DISCARD:
                    case RedisParser.UNWATCH:
                    case RedisParser.MULTI:
                    case RedisParser.PFMERGE:
                    case RedisParser.PFADD:
                    case RedisParser.PFCOUNT:
                    case RedisParser.ECHO:
                    case RedisParser.PING:
                    case RedisParser.SAVE:
                    case RedisParser.SLOWLOG:
                    case RedisParser.LASTSAVE:
                    case RedisParser.CONFIG:
                    case RedisParser.CLIENT:
                    case RedisParser.SHUTDOWN:
                    case RedisParser.SYNC:
                    case RedisParser.ROLE:
                    case RedisParser.MONITOR:
                    case RedisParser.SLAVEOF:
                    case RedisParser.FLUSHALL:
                    case RedisParser.SELECT:
                    case RedisParser.QUIT:
                    case RedisParser.AUTH:
                    case RedisParser.DBSIZE:
                    case RedisParser.BGREWRITEAOF:
                    case RedisParser.TIME:
                    case RedisParser.INFO:
                    case RedisParser.BGSAVE:
                    case RedisParser.COMMAND:
                    case RedisParser.DEBUG:
                    case RedisParser.NX:
                    case RedisParser.XX:
                    case RedisParser.RESETSTAT:
                    case RedisParser.REWRITE:
                    case RedisParser.SETNAME:
                    case RedisParser.GETNAME:
                    case RedisParser.LIST:
                    case RedisParser.GETKEYS:
                    case RedisParser.COUNT:
                    case RedisParser.KILL:
                    case RedisParser.SEMI:
                        break;
                    default:
                        break;
                }
                this.state = 753;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.NX || _la === RedisParser.XX) {
                    {
                        this.state = 752;
                        _la = this._input.LA(1);
                        if (!(_la === RedisParser.NX || _la === RedisParser.XX)) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
                this.state = 756;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 45, this._ctx)) {
                    case 1:
                        {
                            this.state = 755;
                            this.match(RedisParser.GET);
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSetnx() {
        let _localctx = new CmdSetnxContext(this._ctx, this.state);
        this.enterRule(_localctx, 148, RedisParser.RULE_cmdSetnx);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 758;
                this.match(RedisParser.SETNX);
                this.state = 759;
                this.keyName();
                this.state = 760;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSetrange() {
        let _localctx = new CmdSetrangeContext(this._ctx, this.state);
        this.enterRule(_localctx, 150, RedisParser.RULE_cmdSetrange);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 762;
                this.match(RedisParser.SETRANGE);
                this.state = 763;
                this.keyName();
                this.state = 764;
                this.intValue();
                this.state = 765;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSetbit() {
        let _localctx = new CmdSetbitContext(this._ctx, this.state);
        this.enterRule(_localctx, 152, RedisParser.RULE_cmdSetbit);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 767;
                this.match(RedisParser.SETBIT);
                this.state = 768;
                this.keyName();
                this.state = 769;
                this.intValue();
                this.state = 770;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdStrlen() {
        let _localctx = new CmdStrlenContext(this._ctx, this.state);
        this.enterRule(_localctx, 154, RedisParser.RULE_cmdStrlen);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 772;
                this.match(RedisParser.STRLEN);
                this.state = 773;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSubstr() {
        let _localctx = new CmdSubstrContext(this._ctx, this.state);
        this.enterRule(_localctx, 156, RedisParser.RULE_cmdSubstr);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 775;
                this.match(RedisParser.SUBSTR);
                this.state = 776;
                this.keyName();
                this.state = 777;
                this.intValue();
                this.state = 778;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdHdelMget() {
        let _localctx = new CmdHdelMgetContext(this._ctx, this.state);
        this.enterRule(_localctx, 158, RedisParser.RULE_cmdHdelMget);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 780;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.HDEL || _la === RedisParser.HMGET)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 781;
                this.keyName();
                this.state = 783;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 782;
                            this.fieldName();
                        }
                    }
                    this.state = 785;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdHexistsGetKeysStrlen() {
        let _localctx = new CmdHexistsGetKeysStrlenContext(this._ctx, this.state);
        this.enterRule(_localctx, 160, RedisParser.RULE_cmdHexistsGetKeysStrlen);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 787;
                _la = this._input.LA(1);
                if (!(((((_la - 57)) & ~0x1F) === 0 && ((1 << (_la - 57)) & ((1 << (RedisParser.HEXISTS - 57)) | (1 << (RedisParser.HGET - 57)) | (1 << (RedisParser.HSTRLEN - 57)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 788;
                this.keyName();
                this.state = 789;
                this.fieldName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdHgetallLenVals() {
        let _localctx = new CmdHgetallLenValsContext(this._ctx, this.state);
        this.enterRule(_localctx, 162, RedisParser.RULE_cmdHgetallLenVals);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 791;
                _la = this._input.LA(1);
                if (!(((((_la - 59)) & ~0x1F) === 0 && ((1 << (_la - 59)) & ((1 << (RedisParser.HGETALL - 59)) | (1 << (RedisParser.HKEYS - 59)) | (1 << (RedisParser.HLEN - 59)) | (1 << (RedisParser.HVALS - 59)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 792;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdHmsetHset() {
        let _localctx = new CmdHmsetHsetContext(this._ctx, this.state);
        this.enterRule(_localctx, 164, RedisParser.RULE_cmdHmsetHset);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 794;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.HMSET || _la === RedisParser.HSET)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 795;
                this.keyName();
                this.state = 797;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 796;
                            this.keyAndString();
                        }
                    }
                    this.state = 799;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdHincrby() {
        let _localctx = new CmdHincrbyContext(this._ctx, this.state);
        this.enterRule(_localctx, 166, RedisParser.RULE_cmdHincrby);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 801;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.HINCRBY || _la === RedisParser.HINCRBYFLOAT)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 802;
                this.keyName();
                this.state = 803;
                this.fieldName();
                this.state = 804;
                this.floatValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdHrandfield() {
        let _localctx = new CmdHrandfieldContext(this._ctx, this.state);
        this.enterRule(_localctx, 168, RedisParser.RULE_cmdHrandfield);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 806;
                this.match(RedisParser.HRANDFIELD);
                this.state = 807;
                this.keyName();
                this.state = 812;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.INTEGER_NUM) {
                    {
                        this.state = 808;
                        this.intValue();
                        this.state = 810;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === RedisParser.WITHVALUES) {
                            {
                                this.state = 809;
                                this.match(RedisParser.WITHVALUES);
                            }
                        }
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdHscan() {
        let _localctx = new CmdHscanContext(this._ctx, this.state);
        this.enterRule(_localctx, 170, RedisParser.RULE_cmdHscan);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 814;
                this.match(RedisParser.HSCAN);
                this.state = 815;
                this.keyName();
                this.state = 816;
                this.intValue();
                this.state = 819;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.MATCH) {
                    {
                        this.state = 817;
                        this.match(RedisParser.MATCH);
                        this.state = 818;
                        this.pattern();
                    }
                }
                this.state = 823;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 51, this._ctx)) {
                    case 1:
                        {
                            this.state = 821;
                            this.match(RedisParser.COUNT);
                            this.state = 822;
                            this.intValue();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdHsetnx() {
        let _localctx = new CmdHsetnxContext(this._ctx, this.state);
        this.enterRule(_localctx, 172, RedisParser.RULE_cmdHsetnx);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 825;
                this.match(RedisParser.HSETNX);
                this.state = 826;
                this.keyName();
                this.state = 827;
                this.keyAndString();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdBlmove() {
        let _localctx = new CmdBlmoveContext(this._ctx, this.state);
        this.enterRule(_localctx, 174, RedisParser.RULE_cmdBlmove);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 829;
                this.match(RedisParser.BLMOVE);
                this.state = 830;
                this.keyName();
                this.state = 831;
                this.keyName();
                this.state = 832;
                this.lrOpt();
                this.state = 833;
                this.lrOpt();
                this.state = 834;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdLmpop() {
        let _localctx = new CmdLmpopContext(this._ctx, this.state);
        this.enterRule(_localctx, 176, RedisParser.RULE_cmdLmpop);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 836;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.BLMPOP || _la === RedisParser.LMPOP)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 837;
                this.intValue();
                this.state = 838;
                this.intValue();
                this.state = 840;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 839;
                            this.keyName();
                        }
                    }
                    this.state = 842;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                this.state = 844;
                this.lrOpt();
                this.state = 846;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.INTEGER_NUM) {
                    {
                        this.state = 845;
                        this.intValue();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdBpop() {
        let _localctx = new CmdBpopContext(this._ctx, this.state);
        this.enterRule(_localctx, 178, RedisParser.RULE_cmdBpop);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 848;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.BLPOP || _la === RedisParser.BRPOP)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 850;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 849;
                            this.keyName();
                        }
                    }
                    this.state = 852;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                this.state = 854;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdBrpoplpush() {
        let _localctx = new CmdBrpoplpushContext(this._ctx, this.state);
        this.enterRule(_localctx, 180, RedisParser.RULE_cmdBrpoplpush);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 856;
                this.match(RedisParser.BRPOPLPUSH);
                this.state = 857;
                this.keyName();
                this.state = 858;
                this.keyName();
                this.state = 859;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdLindex() {
        let _localctx = new CmdLindexContext(this._ctx, this.state);
        this.enterRule(_localctx, 182, RedisParser.RULE_cmdLindex);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 861;
                this.match(RedisParser.LINDEX);
                this.state = 862;
                this.keyName();
                this.state = 863;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdLinsert() {
        let _localctx = new CmdLinsertContext(this._ctx, this.state);
        this.enterRule(_localctx, 184, RedisParser.RULE_cmdLinsert);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 865;
                this.match(RedisParser.LINSERT);
                this.state = 866;
                this.keyName();
                this.state = 867;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.BEFORE || _la === RedisParser.AFTER)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 868;
                this.stringValue();
                this.state = 869;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdLlen() {
        let _localctx = new CmdLlenContext(this._ctx, this.state);
        this.enterRule(_localctx, 186, RedisParser.RULE_cmdLlen);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 871;
                this.match(RedisParser.LLEN);
                this.state = 872;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdLmove() {
        let _localctx = new CmdLmoveContext(this._ctx, this.state);
        this.enterRule(_localctx, 188, RedisParser.RULE_cmdLmove);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 874;
                this.match(RedisParser.LMOVE);
                this.state = 875;
                this.keyName();
                this.state = 876;
                this.keyName();
                this.state = 877;
                this.lrOpt();
                this.state = 878;
                this.lrOpt();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPop() {
        let _localctx = new CmdPopContext(this._ctx, this.state);
        this.enterRule(_localctx, 190, RedisParser.RULE_cmdPop);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 880;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.LPOP || _la === RedisParser.RPOP)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 881;
                this.keyName();
                this.state = 883;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.INTEGER_NUM) {
                    {
                        this.state = 882;
                        this.intValue();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdLpos() {
        let _localctx = new CmdLposContext(this._ctx, this.state);
        this.enterRule(_localctx, 192, RedisParser.RULE_cmdLpos);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 885;
                this.match(RedisParser.LPOS);
                this.state = 886;
                this.keyName();
                this.state = 887;
                this.fieldName();
                this.state = 889;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.RANK) {
                    {
                        this.state = 888;
                        this.rankOpt();
                    }
                }
                this.state = 892;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 57, this._ctx)) {
                    case 1:
                        {
                            this.state = 891;
                            this.countOpt();
                        }
                        break;
                }
                this.state = 895;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.MAXLEN) {
                    {
                        this.state = 894;
                        this.maxlenOpt();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPush() {
        let _localctx = new CmdPushContext(this._ctx, this.state);
        this.enterRule(_localctx, 194, RedisParser.RULE_cmdPush);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 897;
                _la = this._input.LA(1);
                if (!(((((_la - 84)) & ~0x1F) === 0 && ((1 << (_la - 84)) & ((1 << (RedisParser.LPUSH - 84)) | (1 << (RedisParser.LPUSHX - 84)) | (1 << (RedisParser.RPUSH - 84)) | (1 << (RedisParser.RPUSHX - 84)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 898;
                this.keyName();
                this.state = 900;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 899;
                            this.stringValue();
                        }
                    }
                    this.state = 902;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdLrangeTrim() {
        let _localctx = new CmdLrangeTrimContext(this._ctx, this.state);
        this.enterRule(_localctx, 196, RedisParser.RULE_cmdLrangeTrim);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 904;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.LRANGE || _la === RedisParser.LTRIM)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 905;
                this.keyName();
                this.state = 906;
                this.intValue();
                this.state = 907;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdLremSet() {
        let _localctx = new CmdLremSetContext(this._ctx, this.state);
        this.enterRule(_localctx, 198, RedisParser.RULE_cmdLremSet);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 909;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.LREM || _la === RedisParser.LSET)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 910;
                this.keyName();
                this.state = 911;
                this.intValue();
                this.state = 912;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdRpoplpush() {
        let _localctx = new CmdRpoplpushContext(this._ctx, this.state);
        this.enterRule(_localctx, 200, RedisParser.RULE_cmdRpoplpush);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 914;
                this.match(RedisParser.RPOPLPUSH);
                this.state = 915;
                this.keyName();
                this.state = 916;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSadd() {
        let _localctx = new CmdSaddContext(this._ctx, this.state);
        this.enterRule(_localctx, 202, RedisParser.RULE_cmdSadd);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 918;
                _la = this._input.LA(1);
                if (!(((((_la - 94)) & ~0x1F) === 0 && ((1 << (_la - 94)) & ((1 << (RedisParser.SADD - 94)) | (1 << (RedisParser.SMISMEMBER - 94)) | (1 << (RedisParser.SREM - 94)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 919;
                this.keyName();
                this.state = 921;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 920;
                            this.fieldName();
                        }
                    }
                    this.state = 923;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdScard() {
        let _localctx = new CmdScardContext(this._ctx, this.state);
        this.enterRule(_localctx, 204, RedisParser.RULE_cmdScard);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 925;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.SCARD || _la === RedisParser.SMEMBERS)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 926;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSdiff() {
        let _localctx = new CmdSdiffContext(this._ctx, this.state);
        this.enterRule(_localctx, 206, RedisParser.RULE_cmdSdiff);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 928;
                _la = this._input.LA(1);
                if (!(((((_la - 96)) & ~0x1F) === 0 && ((1 << (_la - 96)) & ((1 << (RedisParser.SDIFF - 96)) | (1 << (RedisParser.SINTER - 96)) | (1 << (RedisParser.SUNION - 96)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 930;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 929;
                            this.keyName();
                        }
                    }
                    this.state = 932;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSdiffstore() {
        let _localctx = new CmdSdiffstoreContext(this._ctx, this.state);
        this.enterRule(_localctx, 208, RedisParser.RULE_cmdSdiffstore);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 934;
                _la = this._input.LA(1);
                if (!(((((_la - 97)) & ~0x1F) === 0 && ((1 << (_la - 97)) & ((1 << (RedisParser.SDIFFSTORE - 97)) | (1 << (RedisParser.SINTERSTORE - 97)) | (1 << (RedisParser.SUNIONSTORE - 97)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 935;
                this.keyName();
                this.state = 937;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 936;
                            this.keyName();
                        }
                    }
                    this.state = 939;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSintercard() {
        let _localctx = new CmdSintercardContext(this._ctx, this.state);
        this.enterRule(_localctx, 210, RedisParser.RULE_cmdSintercard);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 941;
                this.match(RedisParser.SINTERCARD);
                this.state = 942;
                this.intValue();
                this.state = 944;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 943;
                            this.keyName();
                        }
                    }
                    this.state = 946;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                this.state = 950;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.LIMIT) {
                    {
                        this.state = 948;
                        this.match(RedisParser.LIMIT);
                        this.state = 949;
                        this.intValue();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSismember() {
        let _localctx = new CmdSismemberContext(this._ctx, this.state);
        this.enterRule(_localctx, 212, RedisParser.RULE_cmdSismember);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 952;
                this.match(RedisParser.SISMEMBER);
                this.state = 953;
                this.keyName();
                this.state = 954;
                this.fieldName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSmove() {
        let _localctx = new CmdSmoveContext(this._ctx, this.state);
        this.enterRule(_localctx, 214, RedisParser.RULE_cmdSmove);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 956;
                this.match(RedisParser.SMOVE);
                this.state = 957;
                this.keyName();
                this.state = 958;
                this.keyName();
                this.state = 959;
                this.fieldName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSpop() {
        let _localctx = new CmdSpopContext(this._ctx, this.state);
        this.enterRule(_localctx, 216, RedisParser.RULE_cmdSpop);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 961;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.SPOP || _la === RedisParser.SRANDMEMBER)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 962;
                this.keyName();
                this.state = 964;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.INTEGER_NUM) {
                    {
                        this.state = 963;
                        this.intValue();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSscan() {
        let _localctx = new CmdSscanContext(this._ctx, this.state);
        this.enterRule(_localctx, 218, RedisParser.RULE_cmdSscan);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 966;
                this.match(RedisParser.SSCAN);
                this.state = 967;
                this.keyName();
                this.state = 968;
                this.intValue();
                this.state = 971;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.MATCH) {
                    {
                        this.state = 969;
                        this.match(RedisParser.MATCH);
                        this.state = 970;
                        this.pattern();
                    }
                }
                this.state = 974;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 67, this._ctx)) {
                    case 1:
                        {
                            this.state = 973;
                            this.countOpt();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdBzmpop() {
        let _localctx = new CmdBzmpopContext(this._ctx, this.state);
        this.enterRule(_localctx, 220, RedisParser.RULE_cmdBzmpop);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 976;
                this.match(RedisParser.BZMPOP);
                this.state = 977;
                this.intValue();
                this.state = 978;
                this.intValue();
                this.state = 980;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 979;
                            this.keyName();
                        }
                    }
                    this.state = 982;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                this.state = 984;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.MIN || _la === RedisParser.MAX)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 986;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 69, this._ctx)) {
                    case 1:
                        {
                            this.state = 985;
                            this.countOpt();
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdBzpopmax() {
        let _localctx = new CmdBzpopmaxContext(this._ctx, this.state);
        this.enterRule(_localctx, 222, RedisParser.RULE_cmdBzpopmax);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 988;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.BZPOPMAX || _la === RedisParser.BZPOPMIN)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 990;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 989;
                            this.keyName();
                        }
                    }
                    this.state = 992;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                this.state = 994;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZadd() {
        let _localctx = new CmdZaddContext(this._ctx, this.state);
        this.enterRule(_localctx, 224, RedisParser.RULE_cmdZadd);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 996;
                this.match(RedisParser.ZADD);
                this.state = 997;
                this.keyName();
                this.state = 999;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.NX || _la === RedisParser.XX) {
                    {
                        this.state = 998;
                        _la = this._input.LA(1);
                        if (!(_la === RedisParser.NX || _la === RedisParser.XX)) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
                this.state = 1002;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.GT || _la === RedisParser.LT) {
                    {
                        this.state = 1001;
                        _la = this._input.LA(1);
                        if (!(_la === RedisParser.GT || _la === RedisParser.LT)) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
                this.state = 1005;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.CH) {
                    {
                        this.state = 1004;
                        this.match(RedisParser.CH);
                    }
                }
                this.state = 1008;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.INCR) {
                    {
                        this.state = 1007;
                        this.match(RedisParser.INCR);
                    }
                }
                this.state = 1011;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1010;
                            this.intAndString();
                        }
                    }
                    this.state = 1013;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.INTEGER_NUM);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZcard() {
        let _localctx = new CmdZcardContext(this._ctx, this.state);
        this.enterRule(_localctx, 226, RedisParser.RULE_cmdZcard);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1015;
                this.match(RedisParser.ZCARD);
                this.state = 1016;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZcount() {
        let _localctx = new CmdZcountContext(this._ctx, this.state);
        this.enterRule(_localctx, 228, RedisParser.RULE_cmdZcount);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1018;
                _la = this._input.LA(1);
                if (!(((((_la - 116)) & ~0x1F) === 0 && ((1 << (_la - 116)) & ((1 << (RedisParser.ZCOUNT - 116)) | (1 << (RedisParser.ZLEXCOUNT - 116)) | (1 << (RedisParser.ZREMRANGEBYLEX - 116)) | (1 << (RedisParser.ZREMRANGEBYSCORE - 116)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1019;
                this.keyName();
                this.state = 1020;
                this.min();
                this.state = 1021;
                this.max();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZdiff() {
        let _localctx = new CmdZdiffContext(this._ctx, this.state);
        this.enterRule(_localctx, 230, RedisParser.RULE_cmdZdiff);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1023;
                this.match(RedisParser.ZDIFF);
                this.state = 1024;
                this.intValue();
                this.state = 1026;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1025;
                            this.keyName();
                        }
                    }
                    this.state = 1028;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                this.state = 1031;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.WITHSCORES) {
                    {
                        this.state = 1030;
                        this.match(RedisParser.WITHSCORES);
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZdiffstore() {
        let _localctx = new CmdZdiffstoreContext(this._ctx, this.state);
        this.enterRule(_localctx, 232, RedisParser.RULE_cmdZdiffstore);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1033;
                this.match(RedisParser.ZDIFFSTORE);
                this.state = 1034;
                this.keyName();
                this.state = 1035;
                this.intValue();
                this.state = 1037;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1036;
                            this.keyName();
                        }
                    }
                    this.state = 1039;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZincrby() {
        let _localctx = new CmdZincrbyContext(this._ctx, this.state);
        this.enterRule(_localctx, 234, RedisParser.RULE_cmdZincrby);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1041;
                this.match(RedisParser.ZINCRBY);
                this.state = 1042;
                this.keyName();
                this.state = 1043;
                this.intValue();
                this.state = 1044;
                this.fieldName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZinter() {
        let _localctx = new CmdZinterContext(this._ctx, this.state);
        this.enterRule(_localctx, 236, RedisParser.RULE_cmdZinter);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1046;
                this.match(RedisParser.ZINTER);
                this.state = 1047;
                this.intValue();
                this.state = 1049;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1048;
                            this.keyName();
                        }
                    }
                    this.state = 1051;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                this.state = 1059;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.WEIGHTS) {
                    {
                        this.state = 1053;
                        this.match(RedisParser.WEIGHTS);
                        this.state = 1055;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        do {
                            {
                                {
                                    this.state = 1054;
                                    this.intValue();
                                }
                            }
                            this.state = 1057;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        } while (_la === RedisParser.INTEGER_NUM);
                    }
                }
                this.state = 1063;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.AGGREGATE) {
                    {
                        this.state = 1061;
                        this.match(RedisParser.AGGREGATE);
                        this.state = 1062;
                        _la = this._input.LA(1);
                        if (!(((((_la - 291)) & ~0x1F) === 0 && ((1 << (_la - 291)) & ((1 << (RedisParser.MIN - 291)) | (1 << (RedisParser.MAX - 291)) | (1 << (RedisParser.SUM - 291)))) !== 0))) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
                this.state = 1066;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.WITHSCORES) {
                    {
                        this.state = 1065;
                        this.match(RedisParser.WITHSCORES);
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZintercard() {
        let _localctx = new CmdZintercardContext(this._ctx, this.state);
        this.enterRule(_localctx, 238, RedisParser.RULE_cmdZintercard);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1068;
                this.match(RedisParser.ZINTERCARD);
                this.state = 1069;
                this.intValue();
                this.state = 1071;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1070;
                            this.keyName();
                        }
                    }
                    this.state = 1073;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                this.state = 1077;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.LIMIT) {
                    {
                        this.state = 1075;
                        this.match(RedisParser.LIMIT);
                        this.state = 1076;
                        this.intValue();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZinterstore() {
        let _localctx = new CmdZinterstoreContext(this._ctx, this.state);
        this.enterRule(_localctx, 240, RedisParser.RULE_cmdZinterstore);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1079;
                this.match(RedisParser.ZINTERSTORE);
                this.state = 1080;
                this.keyName();
                this.state = 1081;
                this.intValue();
                this.state = 1083;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1082;
                            this.keyName();
                        }
                    }
                    this.state = 1085;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                this.state = 1093;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.WEIGHTS) {
                    {
                        this.state = 1087;
                        this.match(RedisParser.WEIGHTS);
                        this.state = 1089;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        do {
                            {
                                {
                                    this.state = 1088;
                                    this.intValue();
                                }
                            }
                            this.state = 1091;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        } while (_la === RedisParser.INTEGER_NUM);
                    }
                }
                this.state = 1097;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.AGGREGATE) {
                    {
                        this.state = 1095;
                        this.match(RedisParser.AGGREGATE);
                        this.state = 1096;
                        _la = this._input.LA(1);
                        if (!(((((_la - 291)) & ~0x1F) === 0 && ((1 << (_la - 291)) & ((1 << (RedisParser.MIN - 291)) | (1 << (RedisParser.MAX - 291)) | (1 << (RedisParser.SUM - 291)))) !== 0))) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZmpop() {
        let _localctx = new CmdZmpopContext(this._ctx, this.state);
        this.enterRule(_localctx, 242, RedisParser.RULE_cmdZmpop);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1099;
                this.match(RedisParser.ZMPOP);
                this.state = 1100;
                this.intValue();
                this.state = 1102;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1101;
                            this.keyName();
                        }
                    }
                    this.state = 1104;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                this.state = 1106;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.MIN || _la === RedisParser.MAX)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1107;
                this.countOpt();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZmscore() {
        let _localctx = new CmdZmscoreContext(this._ctx, this.state);
        this.enterRule(_localctx, 244, RedisParser.RULE_cmdZmscore);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1109;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.ZMSCORE || _la === RedisParser.ZREM)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1110;
                this.keyName();
                this.state = 1112;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1111;
                            this.fieldName();
                        }
                    }
                    this.state = 1114;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZpopmax() {
        let _localctx = new CmdZpopmaxContext(this._ctx, this.state);
        this.enterRule(_localctx, 246, RedisParser.RULE_cmdZpopmax);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1116;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.ZPOPMAX || _la === RedisParser.ZPOPMIN)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1117;
                this.keyName();
                this.state = 1119;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.INTEGER_NUM) {
                    {
                        this.state = 1118;
                        this.intValue();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZrandmember() {
        let _localctx = new CmdZrandmemberContext(this._ctx, this.state);
        this.enterRule(_localctx, 248, RedisParser.RULE_cmdZrandmember);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1121;
                this.match(RedisParser.ZRANDMEMBER);
                this.state = 1122;
                this.keyName();
                this.state = 1127;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.INTEGER_NUM) {
                    {
                        this.state = 1123;
                        this.intValue();
                        this.state = 1125;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        if (_la === RedisParser.WITHSCORES) {
                            {
                                this.state = 1124;
                                this.match(RedisParser.WITHSCORES);
                            }
                        }
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZrange() {
        let _localctx = new CmdZrangeContext(this._ctx, this.state);
        this.enterRule(_localctx, 250, RedisParser.RULE_cmdZrange);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1129;
                this.match(RedisParser.ZRANGE);
                this.state = 1130;
                this.keyName();
                this.state = 1131;
                this.min();
                this.state = 1132;
                this.max();
                this.state = 1134;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.BYSCORE || _la === RedisParser.BYLEX) {
                    {
                        this.state = 1133;
                        _la = this._input.LA(1);
                        if (!(_la === RedisParser.BYSCORE || _la === RedisParser.BYLEX)) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
                this.state = 1137;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.REV) {
                    {
                        this.state = 1136;
                        this.match(RedisParser.REV);
                    }
                }
                this.state = 1140;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.LIMIT) {
                    {
                        this.state = 1139;
                        this.limitOpt();
                    }
                }
                this.state = 1143;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.WITHSCORES) {
                    {
                        this.state = 1142;
                        this.match(RedisParser.WITHSCORES);
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZrangebylex() {
        let _localctx = new CmdZrangebylexContext(this._ctx, this.state);
        this.enterRule(_localctx, 252, RedisParser.RULE_cmdZrangebylex);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1145;
                this.match(RedisParser.ZRANGEBYLEX);
                this.state = 1146;
                this.keyName();
                this.state = 1147;
                this.min();
                this.state = 1148;
                this.max();
                this.state = 1150;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.LIMIT) {
                    {
                        this.state = 1149;
                        this.limitOpt();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZrangebyscore() {
        let _localctx = new CmdZrangebyscoreContext(this._ctx, this.state);
        this.enterRule(_localctx, 254, RedisParser.RULE_cmdZrangebyscore);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1152;
                this.match(RedisParser.ZRANGEBYSCORE);
                this.state = 1153;
                this.keyName();
                this.state = 1154;
                this.min();
                this.state = 1155;
                this.max();
                this.state = 1157;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.WITHSCORES) {
                    {
                        this.state = 1156;
                        this.match(RedisParser.WITHSCORES);
                    }
                }
                this.state = 1160;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.LIMIT) {
                    {
                        this.state = 1159;
                        this.limitOpt();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZrangestore() {
        let _localctx = new CmdZrangestoreContext(this._ctx, this.state);
        this.enterRule(_localctx, 256, RedisParser.RULE_cmdZrangestore);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1162;
                this.match(RedisParser.ZRANGESTORE);
                this.state = 1163;
                this.keyName();
                this.state = 1164;
                this.keyName();
                this.state = 1165;
                this.min();
                this.state = 1166;
                this.max();
                this.state = 1168;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.BYSCORE || _la === RedisParser.BYLEX) {
                    {
                        this.state = 1167;
                        _la = this._input.LA(1);
                        if (!(_la === RedisParser.BYSCORE || _la === RedisParser.BYLEX)) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
                this.state = 1171;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.LIMIT) {
                    {
                        this.state = 1170;
                        this.limitOpt();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZrank() {
        let _localctx = new CmdZrankContext(this._ctx, this.state);
        this.enterRule(_localctx, 258, RedisParser.RULE_cmdZrank);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1173;
                _la = this._input.LA(1);
                if (!(((((_la - 133)) & ~0x1F) === 0 && ((1 << (_la - 133)) & ((1 << (RedisParser.ZRANK - 133)) | (1 << (RedisParser.ZREVRANK - 133)) | (1 << (RedisParser.ZSCORE - 133)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1174;
                this.keyName();
                this.state = 1175;
                this.fieldName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZremrangebyrank() {
        let _localctx = new CmdZremrangebyrankContext(this._ctx, this.state);
        this.enterRule(_localctx, 260, RedisParser.RULE_cmdZremrangebyrank);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1177;
                this.match(RedisParser.ZREMRANGEBYRANK);
                this.state = 1178;
                this.keyName();
                this.state = 1179;
                this.intValue();
                this.state = 1180;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZrevrange() {
        let _localctx = new CmdZrevrangeContext(this._ctx, this.state);
        this.enterRule(_localctx, 262, RedisParser.RULE_cmdZrevrange);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1182;
                this.match(RedisParser.ZREVRANGE);
                this.state = 1183;
                this.keyName();
                this.state = 1184;
                this.intValue();
                this.state = 1185;
                this.intValue();
                this.state = 1187;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.WITHSCORES) {
                    {
                        this.state = 1186;
                        this.match(RedisParser.WITHSCORES);
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZrevrangebylex() {
        let _localctx = new CmdZrevrangebylexContext(this._ctx, this.state);
        this.enterRule(_localctx, 264, RedisParser.RULE_cmdZrevrangebylex);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1189;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.ZREVRANGEBYLEX || _la === RedisParser.ZREVRANGEBYSCORE)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1190;
                this.keyName();
                this.state = 1191;
                this.max();
                this.state = 1192;
                this.min();
                this.state = 1194;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.WITHSCORES) {
                    {
                        this.state = 1193;
                        this.match(RedisParser.WITHSCORES);
                    }
                }
                this.state = 1197;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.LIMIT) {
                    {
                        this.state = 1196;
                        this.limitOpt();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZscan() {
        let _localctx = new CmdZscanContext(this._ctx, this.state);
        this.enterRule(_localctx, 266, RedisParser.RULE_cmdZscan);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1199;
                this.match(RedisParser.ZSCAN);
                this.state = 1200;
                this.keyName();
                this.state = 1201;
                this.intValue();
                this.state = 1204;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.MATCH) {
                    {
                        this.state = 1202;
                        this.match(RedisParser.MATCH);
                        this.state = 1203;
                        this.pattern();
                    }
                }
                this.state = 1206;
                this.countOpt();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZunion() {
        let _localctx = new CmdZunionContext(this._ctx, this.state);
        this.enterRule(_localctx, 268, RedisParser.RULE_cmdZunion);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1208;
                this.match(RedisParser.ZUNION);
                this.state = 1209;
                this.intValue();
                this.state = 1211;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1210;
                            this.keyName();
                        }
                    }
                    this.state = 1213;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                this.state = 1221;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.WEIGHTS) {
                    {
                        this.state = 1215;
                        this.match(RedisParser.WEIGHTS);
                        this.state = 1217;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        do {
                            {
                                {
                                    this.state = 1216;
                                    this.intValue();
                                }
                            }
                            this.state = 1219;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        } while (_la === RedisParser.INTEGER_NUM);
                    }
                }
                this.state = 1225;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.AGGREGATE) {
                    {
                        this.state = 1223;
                        this.match(RedisParser.AGGREGATE);
                        this.state = 1224;
                        _la = this._input.LA(1);
                        if (!(((((_la - 291)) & ~0x1F) === 0 && ((1 << (_la - 291)) & ((1 << (RedisParser.MIN - 291)) | (1 << (RedisParser.MAX - 291)) | (1 << (RedisParser.SUM - 291)))) !== 0))) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
                this.state = 1228;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.WITHSCORES) {
                    {
                        this.state = 1227;
                        this.match(RedisParser.WITHSCORES);
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdZunionstore() {
        let _localctx = new CmdZunionstoreContext(this._ctx, this.state);
        this.enterRule(_localctx, 270, RedisParser.RULE_cmdZunionstore);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1230;
                this.match(RedisParser.ZUNIONSTORE);
                this.state = 1231;
                this.keyName();
                this.state = 1232;
                this.intValue();
                this.state = 1234;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1233;
                            this.keyName();
                        }
                    }
                    this.state = 1236;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                this.state = 1244;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.WEIGHTS) {
                    {
                        this.state = 1238;
                        this.match(RedisParser.WEIGHTS);
                        this.state = 1240;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        do {
                            {
                                {
                                    this.state = 1239;
                                    this.intValue();
                                }
                            }
                            this.state = 1242;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        } while (_la === RedisParser.INTEGER_NUM);
                    }
                }
                this.state = 1248;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.AGGREGATE) {
                    {
                        this.state = 1246;
                        this.match(RedisParser.AGGREGATE);
                        this.state = 1247;
                        _la = this._input.LA(1);
                        if (!(((((_la - 291)) & ~0x1F) === 0 && ((1 << (_la - 291)) & ((1 << (RedisParser.MIN - 291)) | (1 << (RedisParser.MAX - 291)) | (1 << (RedisParser.SUM - 291)))) !== 0))) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdScriptKill() {
        let _localctx = new CmdScriptKillContext(this._ctx, this.state);
        this.enterRule(_localctx, 272, RedisParser.RULE_cmdScriptKill);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1250;
                this.match(RedisParser.SCRIPT);
                this.state = 1251;
                this.match(RedisParser.KILL);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdScriptLoad() {
        let _localctx = new CmdScriptLoadContext(this._ctx, this.state);
        this.enterRule(_localctx, 274, RedisParser.RULE_cmdScriptLoad);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1253;
                this.match(RedisParser.SCRIPT);
                this.state = 1254;
                this.match(RedisParser.LOAD);
                this.state = 1255;
                this.multiString();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdEval() {
        let _localctx = new CmdEvalContext(this._ctx, this.state);
        this.enterRule(_localctx, 276, RedisParser.RULE_cmdEval);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1257;
                this.match(RedisParser.EVAL);
                this.state = 1258;
                this.multiString();
                this.state = 1259;
                this.intValue();
                this.state = 1263;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN) {
                    {
                        {
                            this.state = 1260;
                            this.keyName();
                        }
                    }
                    this.state = 1265;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdEvalsha() {
        let _localctx = new CmdEvalshaContext(this._ctx, this.state);
        this.enterRule(_localctx, 278, RedisParser.RULE_cmdEvalsha);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1266;
                this.match(RedisParser.EVALSHA);
                this.state = 1267;
                this.stringValue();
                this.state = 1268;
                this.intValue();
                this.state = 1272;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN) {
                    {
                        {
                            this.state = 1269;
                            this.keyName();
                        }
                    }
                    this.state = 1274;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdScriptExists() {
        let _localctx = new CmdScriptExistsContext(this._ctx, this.state);
        this.enterRule(_localctx, 280, RedisParser.RULE_cmdScriptExists);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1275;
                this.match(RedisParser.SCRIPT);
                this.state = 1276;
                this.match(RedisParser.EXISTS);
                this.state = 1277;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdScriptFlush() {
        let _localctx = new CmdScriptFlushContext(this._ctx, this.state);
        this.enterRule(_localctx, 282, RedisParser.RULE_cmdScriptFlush);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1279;
                this.match(RedisParser.SCRIPT);
                this.state = 1280;
                this.match(RedisParser.FLUSH);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdExec() {
        let _localctx = new CmdExecContext(this._ctx, this.state);
        this.enterRule(_localctx, 284, RedisParser.RULE_cmdExec);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1282;
                this.match(RedisParser.EXEC);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdWatch() {
        let _localctx = new CmdWatchContext(this._ctx, this.state);
        this.enterRule(_localctx, 286, RedisParser.RULE_cmdWatch);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1284;
                this.match(RedisParser.WATCH);
                this.state = 1286;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1285;
                            this.keyName();
                        }
                    }
                    this.state = 1288;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdDiscard() {
        let _localctx = new CmdDiscardContext(this._ctx, this.state);
        this.enterRule(_localctx, 288, RedisParser.RULE_cmdDiscard);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1290;
                this.match(RedisParser.DISCARD);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdUnwatch() {
        let _localctx = new CmdUnwatchContext(this._ctx, this.state);
        this.enterRule(_localctx, 290, RedisParser.RULE_cmdUnwatch);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1292;
                this.match(RedisParser.UNWATCH);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdMulti() {
        let _localctx = new CmdMultiContext(this._ctx, this.state);
        this.enterRule(_localctx, 292, RedisParser.RULE_cmdMulti);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1294;
                this.match(RedisParser.MULTI);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPfmerge() {
        let _localctx = new CmdPfmergeContext(this._ctx, this.state);
        this.enterRule(_localctx, 294, RedisParser.RULE_cmdPfmerge);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1296;
                this.match(RedisParser.PFMERGE);
                this.state = 1297;
                this.keyName();
                this.state = 1299;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1298;
                            this.keyName();
                        }
                    }
                    this.state = 1301;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPfadd() {
        let _localctx = new CmdPfaddContext(this._ctx, this.state);
        this.enterRule(_localctx, 296, RedisParser.RULE_cmdPfadd);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1303;
                this.match(RedisParser.PFADD);
                this.state = 1304;
                this.keyName();
                this.state = 1306;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1305;
                            this.fieldName();
                        }
                    }
                    this.state = 1308;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPfcount() {
        let _localctx = new CmdPfcountContext(this._ctx, this.state);
        this.enterRule(_localctx, 298, RedisParser.RULE_cmdPfcount);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1310;
                this.match(RedisParser.PFCOUNT);
                this.state = 1311;
                this.keyName();
                this.state = 1313;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1312;
                            this.keyName();
                        }
                    }
                    this.state = 1315;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSsubscribe() {
        let _localctx = new CmdSsubscribeContext(this._ctx, this.state);
        this.enterRule(_localctx, 300, RedisParser.RULE_cmdSsubscribe);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1317;
                _la = this._input.LA(1);
                if (!(((((_la - 146)) & ~0x1F) === 0 && ((1 << (_la - 146)) & ((1 << (RedisParser.PSUBSCRIBE - 146)) | (1 << (RedisParser.SUBSCRIBE - 146)) | (1 << (RedisParser.SSUBSCRIBE - 146)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1319;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1318;
                            this.keyName();
                        }
                    }
                    this.state = 1321;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdUnsubscribe() {
        let _localctx = new CmdUnsubscribeContext(this._ctx, this.state);
        this.enterRule(_localctx, 302, RedisParser.RULE_cmdUnsubscribe);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1323;
                _la = this._input.LA(1);
                if (!(((((_la - 154)) & ~0x1F) === 0 && ((1 << (_la - 154)) & ((1 << (RedisParser.PUNSUBSCRIBE - 154)) | (1 << (RedisParser.UNSUBSCRIBE - 154)) | (1 << (RedisParser.SUNSUBSCRIBE - 154)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1329;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN) {
                    {
                        this.state = 1325;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        do {
                            {
                                {
                                    this.state = 1324;
                                    this.keyName();
                                }
                            }
                            this.state = 1327;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPusubnumpat() {
        let _localctx = new CmdPusubnumpatContext(this._ctx, this.state);
        this.enterRule(_localctx, 304, RedisParser.RULE_cmdPusubnumpat);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1331;
                this.match(RedisParser.PUBSUB);
                this.state = 1332;
                this.match(RedisParser.NUMPAT);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPusubnumsub() {
        let _localctx = new CmdPusubnumsubContext(this._ctx, this.state);
        this.enterRule(_localctx, 306, RedisParser.RULE_cmdPusubnumsub);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1334;
                this.match(RedisParser.PUBSUB);
                this.state = 1335;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.NUMSUB || _la === RedisParser.SHARDNUMSUB)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1341;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN) {
                    {
                        this.state = 1337;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        do {
                            {
                                {
                                    this.state = 1336;
                                    this.keyName();
                                }
                            }
                            this.state = 1339;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPusubchannels() {
        let _localctx = new CmdPusubchannelsContext(this._ctx, this.state);
        this.enterRule(_localctx, 308, RedisParser.RULE_cmdPusubchannels);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1343;
                this.match(RedisParser.PUBSUB);
                this.state = 1344;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.CHANNELS || _la === RedisParser.SHARDCHANNELS)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1346;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN) {
                    {
                        this.state = 1345;
                        this.keyName();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPublish() {
        let _localctx = new CmdPublishContext(this._ctx, this.state);
        this.enterRule(_localctx, 310, RedisParser.RULE_cmdPublish);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1348;
                this.match(RedisParser.PUBLISH);
                this.state = 1349;
                this.keyName();
                this.state = 1350;
                this.fieldName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdAsking() {
        let _localctx = new CmdAskingContext(this._ctx, this.state);
        this.enterRule(_localctx, 312, RedisParser.RULE_cmdAsking);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1352;
                this.match(RedisParser.ASKING);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdReadonly() {
        let _localctx = new CmdReadonlyContext(this._ctx, this.state);
        this.enterRule(_localctx, 314, RedisParser.RULE_cmdReadonly);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1354;
                this.match(RedisParser.READONLY);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdReadwrite() {
        let _localctx = new CmdReadwriteContext(this._ctx, this.state);
        this.enterRule(_localctx, 316, RedisParser.RULE_cmdReadwrite);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1356;
                this.match(RedisParser.READWRITE);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdAddDelSlots() {
        let _localctx = new CmdAddDelSlotsContext(this._ctx, this.state);
        this.enterRule(_localctx, 318, RedisParser.RULE_cmdAddDelSlots);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1358;
                this.match(RedisParser.CLUSTER);
                this.state = 1359;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.ADDSLOTS || _la === RedisParser.DELSLOTS)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1361;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                do {
                    {
                        {
                            this.state = 1360;
                            this.intValue();
                        }
                    }
                    this.state = 1363;
                    this._errHandler.sync(this);
                    _la = this._input.LA(1);
                } while (_la === RedisParser.INTEGER_NUM);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdCountKeysInSlot() {
        let _localctx = new CmdCountKeysInSlotContext(this._ctx, this.state);
        this.enterRule(_localctx, 320, RedisParser.RULE_cmdCountKeysInSlot);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1365;
                this.match(RedisParser.CLUSTER);
                this.state = 1366;
                this.match(RedisParser.COUNTKEYSINSLOT);
                this.state = 1367;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdFailOver() {
        let _localctx = new CmdFailOverContext(this._ctx, this.state);
        this.enterRule(_localctx, 322, RedisParser.RULE_cmdFailOver);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1369;
                this.match(RedisParser.CLUSTER);
                this.state = 1370;
                this.match(RedisParser.FAILOVER);
                this.state = 1372;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.FORCE || _la === RedisParser.TAKEOVER) {
                    {
                        this.state = 1371;
                        _la = this._input.LA(1);
                        if (!(_la === RedisParser.FORCE || _la === RedisParser.TAKEOVER)) {
                            this._errHandler.recoverInline(this);
                        }
                        else {
                            if (this._input.LA(1) === Token.EOF) {
                                this.matchedEOF = true;
                            }
                            this._errHandler.reportMatch(this);
                            this.consume();
                        }
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdForget() {
        let _localctx = new CmdForgetContext(this._ctx, this.state);
        this.enterRule(_localctx, 324, RedisParser.RULE_cmdForget);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1374;
                this.match(RedisParser.CLUSTER);
                this.state = 1375;
                this.match(RedisParser.FORGET);
                this.state = 1376;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdGetKeysInSlot() {
        let _localctx = new CmdGetKeysInSlotContext(this._ctx, this.state);
        this.enterRule(_localctx, 326, RedisParser.RULE_cmdGetKeysInSlot);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1378;
                this.match(RedisParser.CLUSTER);
                this.state = 1379;
                this.match(RedisParser.GETKEYSINSLOT);
                this.state = 1380;
                this.intValue();
                this.state = 1381;
                this.cout();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdClusterOrder() {
        let _localctx = new CmdClusterOrderContext(this._ctx, this.state);
        this.enterRule(_localctx, 328, RedisParser.RULE_cmdClusterOrder);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1383;
                this.match(RedisParser.CLUSTER);
                this.state = 1384;
                _la = this._input.LA(1);
                if (!(((((_la - 181)) & ~0x1F) === 0 && ((1 << (_la - 181)) & ((1 << (RedisParser.BUMPEPOCH - 181)) | (1 << (RedisParser.FLUSHSLOTS - 181)) | (1 << (RedisParser.LINKS - 181)) | (1 << (RedisParser.MYID - 181)) | (1 << (RedisParser.MYSHARDID - 181)) | (1 << (RedisParser.NODES - 181)) | (1 << (RedisParser.SAVECONFIG - 181)) | (1 << (RedisParser.SHARDS - 181)))) !== 0) || _la === RedisParser.INFO || _la === RedisParser.SLOTS)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdKeySlot() {
        let _localctx = new CmdKeySlotContext(this._ctx, this.state);
        this.enterRule(_localctx, 330, RedisParser.RULE_cmdKeySlot);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1386;
                this.match(RedisParser.CLUSTER);
                this.state = 1387;
                this.match(RedisParser.KEYSLOT);
                this.state = 1388;
                this.keyName();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdMeet() {
        let _localctx = new CmdMeetContext(this._ctx, this.state);
        this.enterRule(_localctx, 332, RedisParser.RULE_cmdMeet);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1390;
                this.match(RedisParser.CLUSTER);
                this.state = 1391;
                this.match(RedisParser.MEET);
                this.state = 1392;
                this.match(RedisParser.HOST);
                this.state = 1393;
                this.port();
                this.state = 1395;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.INTEGER_NUM) {
                    {
                        this.state = 1394;
                        this.intValue();
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdReplicaesSlave() {
        let _localctx = new CmdReplicaesSlaveContext(this._ctx, this.state);
        this.enterRule(_localctx, 334, RedisParser.RULE_cmdReplicaesSlave);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1397;
                this.match(RedisParser.CLUSTER);
                this.state = 1398;
                _la = this._input.LA(1);
                if (!(((((_la - 197)) & ~0x1F) === 0 && ((1 << (_la - 197)) & ((1 << (RedisParser.REPLICAS - 197)) | (1 << (RedisParser.REPLICATE - 197)) | (1 << (RedisParser.SLAVES - 197)))) !== 0))) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
                this.state = 1399;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdReset() {
        let _localctx = new CmdResetContext(this._ctx, this.state);
        this.enterRule(_localctx, 336, RedisParser.RULE_cmdReset);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1401;
                this.match(RedisParser.CLUSTER);
                this.state = 1402;
                this.match(RedisParser.RESET);
                this.state = 1403;
                _la = this._input.LA(1);
                if (!(_la === RedisParser.HARD || _la === RedisParser.SOFT)) {
                    this._errHandler.recoverInline(this);
                }
                else {
                    if (this._input.LA(1) === Token.EOF) {
                        this.matchedEOF = true;
                    }
                    this._errHandler.reportMatch(this);
                    this.consume();
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSetSlot() {
        let _localctx = new CmdSetSlotContext(this._ctx, this.state);
        this.enterRule(_localctx, 338, RedisParser.RULE_cmdSetSlot);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1405;
                this.match(RedisParser.CLUSTER);
                this.state = 1406;
                this.match(RedisParser.SETSLOT);
                this.state = 1407;
                this.intValue();
                this.state = 1418;
                this._errHandler.sync(this);
                switch (this._input.LA(1)) {
                    case RedisParser.MIGRATING:
                        {
                            {
                                this.state = 1408;
                                this.match(RedisParser.MIGRATING);
                                this.state = 1409;
                                this.stringValue();
                            }
                        }
                        break;
                    case RedisParser.EOF:
                    case RedisParser.COPY:
                    case RedisParser.DEL:
                    case RedisParser.DUMP:
                    case RedisParser.EXISTS:
                    case RedisParser.EXPIRE:
                    case RedisParser.EXPIREAT:
                    case RedisParser.EXPIRETIME:
                    case RedisParser.KEYS:
                    case RedisParser.MOVE:
                    case RedisParser.OBJECT:
                    case RedisParser.PERSIST:
                    case RedisParser.PEXPIRE:
                    case RedisParser.PEXPIREAT:
                    case RedisParser.PEXPIRETIME:
                    case RedisParser.TTL:
                    case RedisParser.PTTL:
                    case RedisParser.RANDOMKEY:
                    case RedisParser.RENAME:
                    case RedisParser.RENAMENX:
                    case RedisParser.RESTORE:
                    case RedisParser.SCAN:
                    case RedisParser.SORT:
                    case RedisParser.SORT_RO:
                    case RedisParser.TOUCH:
                    case RedisParser.TYPE:
                    case RedisParser.UNLINK:
                    case RedisParser.WAIT:
                    case RedisParser.APPEND:
                    case RedisParser.DECR:
                    case RedisParser.DECRBY:
                    case RedisParser.GET:
                    case RedisParser.GETDEL:
                    case RedisParser.GETEX:
                    case RedisParser.GETRANGE:
                    case RedisParser.GETSET:
                    case RedisParser.GETBIT:
                    case RedisParser.INCR:
                    case RedisParser.INCRBY:
                    case RedisParser.INCRBYFLOAT:
                    case RedisParser.LCS:
                    case RedisParser.MGET:
                    case RedisParser.MSET:
                    case RedisParser.MSETNX:
                    case RedisParser.SETEX:
                    case RedisParser.PSETEX:
                    case RedisParser.SET:
                    case RedisParser.SETNX:
                    case RedisParser.SETRANGE:
                    case RedisParser.SETBIT:
                    case RedisParser.STRLEN:
                    case RedisParser.SUBSTR:
                    case RedisParser.HDEL:
                    case RedisParser.HEXISTS:
                    case RedisParser.HGET:
                    case RedisParser.HGETALL:
                    case RedisParser.HINCRBY:
                    case RedisParser.HINCRBYFLOAT:
                    case RedisParser.HKEYS:
                    case RedisParser.HLEN:
                    case RedisParser.HMGET:
                    case RedisParser.HMSET:
                    case RedisParser.HRANDFIELD:
                    case RedisParser.HSCAN:
                    case RedisParser.HSET:
                    case RedisParser.HSETNX:
                    case RedisParser.HSTRLEN:
                    case RedisParser.HVALS:
                    case RedisParser.BLMOVE:
                    case RedisParser.BLMPOP:
                    case RedisParser.BLPOP:
                    case RedisParser.BRPOP:
                    case RedisParser.BRPOPLPUSH:
                    case RedisParser.LINDEX:
                    case RedisParser.LINSERT:
                    case RedisParser.LLEN:
                    case RedisParser.LMOVE:
                    case RedisParser.LMPOP:
                    case RedisParser.LPOP:
                    case RedisParser.LPOS:
                    case RedisParser.LPUSH:
                    case RedisParser.LPUSHX:
                    case RedisParser.LRANGE:
                    case RedisParser.LREM:
                    case RedisParser.LSET:
                    case RedisParser.LTRIM:
                    case RedisParser.RPOP:
                    case RedisParser.RPOPLPUSH:
                    case RedisParser.RPUSH:
                    case RedisParser.RPUSHX:
                    case RedisParser.SADD:
                    case RedisParser.SCARD:
                    case RedisParser.SDIFF:
                    case RedisParser.SDIFFSTORE:
                    case RedisParser.SINTER:
                    case RedisParser.SINTERCARD:
                    case RedisParser.SINTERSTORE:
                    case RedisParser.SISMEMBER:
                    case RedisParser.SMEMBERS:
                    case RedisParser.SMISMEMBER:
                    case RedisParser.SMOVE:
                    case RedisParser.SPOP:
                    case RedisParser.SRANDMEMBER:
                    case RedisParser.SREM:
                    case RedisParser.SSCAN:
                    case RedisParser.SUNION:
                    case RedisParser.SUNIONSTORE:
                    case RedisParser.BZMPOP:
                    case RedisParser.BZPOPMAX:
                    case RedisParser.BZPOPMIN:
                    case RedisParser.ZADD:
                    case RedisParser.ZCARD:
                    case RedisParser.ZCOUNT:
                    case RedisParser.ZDIFF:
                    case RedisParser.ZDIFFSTORE:
                    case RedisParser.ZINCRBY:
                    case RedisParser.ZINTER:
                    case RedisParser.ZINTERCARD:
                    case RedisParser.ZINTERSTORE:
                    case RedisParser.ZLEXCOUNT:
                    case RedisParser.ZMPOP:
                    case RedisParser.ZMSCORE:
                    case RedisParser.ZPOPMAX:
                    case RedisParser.ZPOPMIN:
                    case RedisParser.ZRANDMEMBER:
                    case RedisParser.ZRANGE:
                    case RedisParser.ZRANGEBYLEX:
                    case RedisParser.ZRANGEBYSCORE:
                    case RedisParser.ZRANGESTORE:
                    case RedisParser.ZRANK:
                    case RedisParser.ZREM:
                    case RedisParser.ZREMRANGEBYLEX:
                    case RedisParser.ZREMRANGEBYRANK:
                    case RedisParser.ZREMRANGEBYSCORE:
                    case RedisParser.ZREVRANGE:
                    case RedisParser.ZREVRANGEBYLEX:
                    case RedisParser.ZREVRANGEBYSCORE:
                    case RedisParser.ZREVRANK:
                    case RedisParser.ZSCAN:
                    case RedisParser.ZSCORE:
                    case RedisParser.ZUNION:
                    case RedisParser.ZUNIONSTORE:
                    case RedisParser.PSUBSCRIBE:
                    case RedisParser.PUBSUB:
                    case RedisParser.PUBLISH:
                    case RedisParser.PUNSUBSCRIBE:
                    case RedisParser.SUBSCRIBE:
                    case RedisParser.UNSUBSCRIBE:
                    case RedisParser.SSUBSCRIBE:
                    case RedisParser.SUNSUBSCRIBE:
                    case RedisParser.ASKING:
                    case RedisParser.CLUSTER:
                    case RedisParser.IMPORTING:
                    case RedisParser.READONLY:
                    case RedisParser.READWRITE:
                    case RedisParser.FLUSHDB:
                    case RedisParser.SCRIPT:
                    case RedisParser.EVAL:
                    case RedisParser.EVALSHA:
                    case RedisParser.EXEC:
                    case RedisParser.WATCH:
                    case RedisParser.DISCARD:
                    case RedisParser.UNWATCH:
                    case RedisParser.MULTI:
                    case RedisParser.PFMERGE:
                    case RedisParser.PFADD:
                    case RedisParser.PFCOUNT:
                    case RedisParser.ECHO:
                    case RedisParser.PING:
                    case RedisParser.SAVE:
                    case RedisParser.SLOWLOG:
                    case RedisParser.LASTSAVE:
                    case RedisParser.CONFIG:
                    case RedisParser.CLIENT:
                    case RedisParser.SHUTDOWN:
                    case RedisParser.SYNC:
                    case RedisParser.ROLE:
                    case RedisParser.MONITOR:
                    case RedisParser.SLAVEOF:
                    case RedisParser.FLUSHALL:
                    case RedisParser.SELECT:
                    case RedisParser.QUIT:
                    case RedisParser.AUTH:
                    case RedisParser.DBSIZE:
                    case RedisParser.BGREWRITEAOF:
                    case RedisParser.TIME:
                    case RedisParser.INFO:
                    case RedisParser.BGSAVE:
                    case RedisParser.COMMAND:
                    case RedisParser.DEBUG:
                    case RedisParser.RESETSTAT:
                    case RedisParser.REWRITE:
                    case RedisParser.SETNAME:
                    case RedisParser.GETNAME:
                    case RedisParser.LIST:
                    case RedisParser.GETKEYS:
                    case RedisParser.COUNT:
                    case RedisParser.KILL:
                    case RedisParser.SEMI:
                        {
                            this.state = 1413;
                            this._errHandler.sync(this);
                            switch (this._input.LA(1)) {
                                case RedisParser.IMPORTING:
                                    {
                                        this.state = 1410;
                                        this.match(RedisParser.IMPORTING);
                                        this.state = 1411;
                                        this.stringValue();
                                    }
                                    break;
                                case RedisParser.EOF:
                                case RedisParser.COPY:
                                case RedisParser.DEL:
                                case RedisParser.DUMP:
                                case RedisParser.EXISTS:
                                case RedisParser.EXPIRE:
                                case RedisParser.EXPIREAT:
                                case RedisParser.EXPIRETIME:
                                case RedisParser.KEYS:
                                case RedisParser.MOVE:
                                case RedisParser.OBJECT:
                                case RedisParser.PERSIST:
                                case RedisParser.PEXPIRE:
                                case RedisParser.PEXPIREAT:
                                case RedisParser.PEXPIRETIME:
                                case RedisParser.TTL:
                                case RedisParser.PTTL:
                                case RedisParser.RANDOMKEY:
                                case RedisParser.RENAME:
                                case RedisParser.RENAMENX:
                                case RedisParser.RESTORE:
                                case RedisParser.SCAN:
                                case RedisParser.SORT:
                                case RedisParser.SORT_RO:
                                case RedisParser.TOUCH:
                                case RedisParser.TYPE:
                                case RedisParser.UNLINK:
                                case RedisParser.WAIT:
                                case RedisParser.APPEND:
                                case RedisParser.DECR:
                                case RedisParser.DECRBY:
                                case RedisParser.GET:
                                case RedisParser.GETDEL:
                                case RedisParser.GETEX:
                                case RedisParser.GETRANGE:
                                case RedisParser.GETSET:
                                case RedisParser.GETBIT:
                                case RedisParser.INCR:
                                case RedisParser.INCRBY:
                                case RedisParser.INCRBYFLOAT:
                                case RedisParser.LCS:
                                case RedisParser.MGET:
                                case RedisParser.MSET:
                                case RedisParser.MSETNX:
                                case RedisParser.SETEX:
                                case RedisParser.PSETEX:
                                case RedisParser.SET:
                                case RedisParser.SETNX:
                                case RedisParser.SETRANGE:
                                case RedisParser.SETBIT:
                                case RedisParser.STRLEN:
                                case RedisParser.SUBSTR:
                                case RedisParser.HDEL:
                                case RedisParser.HEXISTS:
                                case RedisParser.HGET:
                                case RedisParser.HGETALL:
                                case RedisParser.HINCRBY:
                                case RedisParser.HINCRBYFLOAT:
                                case RedisParser.HKEYS:
                                case RedisParser.HLEN:
                                case RedisParser.HMGET:
                                case RedisParser.HMSET:
                                case RedisParser.HRANDFIELD:
                                case RedisParser.HSCAN:
                                case RedisParser.HSET:
                                case RedisParser.HSETNX:
                                case RedisParser.HSTRLEN:
                                case RedisParser.HVALS:
                                case RedisParser.BLMOVE:
                                case RedisParser.BLMPOP:
                                case RedisParser.BLPOP:
                                case RedisParser.BRPOP:
                                case RedisParser.BRPOPLPUSH:
                                case RedisParser.LINDEX:
                                case RedisParser.LINSERT:
                                case RedisParser.LLEN:
                                case RedisParser.LMOVE:
                                case RedisParser.LMPOP:
                                case RedisParser.LPOP:
                                case RedisParser.LPOS:
                                case RedisParser.LPUSH:
                                case RedisParser.LPUSHX:
                                case RedisParser.LRANGE:
                                case RedisParser.LREM:
                                case RedisParser.LSET:
                                case RedisParser.LTRIM:
                                case RedisParser.RPOP:
                                case RedisParser.RPOPLPUSH:
                                case RedisParser.RPUSH:
                                case RedisParser.RPUSHX:
                                case RedisParser.SADD:
                                case RedisParser.SCARD:
                                case RedisParser.SDIFF:
                                case RedisParser.SDIFFSTORE:
                                case RedisParser.SINTER:
                                case RedisParser.SINTERCARD:
                                case RedisParser.SINTERSTORE:
                                case RedisParser.SISMEMBER:
                                case RedisParser.SMEMBERS:
                                case RedisParser.SMISMEMBER:
                                case RedisParser.SMOVE:
                                case RedisParser.SPOP:
                                case RedisParser.SRANDMEMBER:
                                case RedisParser.SREM:
                                case RedisParser.SSCAN:
                                case RedisParser.SUNION:
                                case RedisParser.SUNIONSTORE:
                                case RedisParser.BZMPOP:
                                case RedisParser.BZPOPMAX:
                                case RedisParser.BZPOPMIN:
                                case RedisParser.ZADD:
                                case RedisParser.ZCARD:
                                case RedisParser.ZCOUNT:
                                case RedisParser.ZDIFF:
                                case RedisParser.ZDIFFSTORE:
                                case RedisParser.ZINCRBY:
                                case RedisParser.ZINTER:
                                case RedisParser.ZINTERCARD:
                                case RedisParser.ZINTERSTORE:
                                case RedisParser.ZLEXCOUNT:
                                case RedisParser.ZMPOP:
                                case RedisParser.ZMSCORE:
                                case RedisParser.ZPOPMAX:
                                case RedisParser.ZPOPMIN:
                                case RedisParser.ZRANDMEMBER:
                                case RedisParser.ZRANGE:
                                case RedisParser.ZRANGEBYLEX:
                                case RedisParser.ZRANGEBYSCORE:
                                case RedisParser.ZRANGESTORE:
                                case RedisParser.ZRANK:
                                case RedisParser.ZREM:
                                case RedisParser.ZREMRANGEBYLEX:
                                case RedisParser.ZREMRANGEBYRANK:
                                case RedisParser.ZREMRANGEBYSCORE:
                                case RedisParser.ZREVRANGE:
                                case RedisParser.ZREVRANGEBYLEX:
                                case RedisParser.ZREVRANGEBYSCORE:
                                case RedisParser.ZREVRANK:
                                case RedisParser.ZSCAN:
                                case RedisParser.ZSCORE:
                                case RedisParser.ZUNION:
                                case RedisParser.ZUNIONSTORE:
                                case RedisParser.PSUBSCRIBE:
                                case RedisParser.PUBSUB:
                                case RedisParser.PUBLISH:
                                case RedisParser.PUNSUBSCRIBE:
                                case RedisParser.SUBSCRIBE:
                                case RedisParser.UNSUBSCRIBE:
                                case RedisParser.SSUBSCRIBE:
                                case RedisParser.SUNSUBSCRIBE:
                                case RedisParser.ASKING:
                                case RedisParser.CLUSTER:
                                case RedisParser.READONLY:
                                case RedisParser.READWRITE:
                                case RedisParser.FLUSHDB:
                                case RedisParser.SCRIPT:
                                case RedisParser.EVAL:
                                case RedisParser.EVALSHA:
                                case RedisParser.EXEC:
                                case RedisParser.WATCH:
                                case RedisParser.DISCARD:
                                case RedisParser.UNWATCH:
                                case RedisParser.MULTI:
                                case RedisParser.PFMERGE:
                                case RedisParser.PFADD:
                                case RedisParser.PFCOUNT:
                                case RedisParser.ECHO:
                                case RedisParser.PING:
                                case RedisParser.SAVE:
                                case RedisParser.SLOWLOG:
                                case RedisParser.LASTSAVE:
                                case RedisParser.CONFIG:
                                case RedisParser.CLIENT:
                                case RedisParser.SHUTDOWN:
                                case RedisParser.SYNC:
                                case RedisParser.ROLE:
                                case RedisParser.MONITOR:
                                case RedisParser.SLAVEOF:
                                case RedisParser.FLUSHALL:
                                case RedisParser.SELECT:
                                case RedisParser.QUIT:
                                case RedisParser.AUTH:
                                case RedisParser.DBSIZE:
                                case RedisParser.BGREWRITEAOF:
                                case RedisParser.TIME:
                                case RedisParser.INFO:
                                case RedisParser.BGSAVE:
                                case RedisParser.COMMAND:
                                case RedisParser.DEBUG:
                                case RedisParser.RESETSTAT:
                                case RedisParser.REWRITE:
                                case RedisParser.SETNAME:
                                case RedisParser.GETNAME:
                                case RedisParser.LIST:
                                case RedisParser.GETKEYS:
                                case RedisParser.COUNT:
                                case RedisParser.KILL:
                                case RedisParser.SEMI:
                                    // tslint:disable-next-line:no-empty
                                    {
                                    }
                                    break;
                                default:
                                    throw new NoViableAltException(this);
                            }
                        }
                        break;
                    case RedisParser.NODE:
                        {
                            {
                                this.state = 1415;
                                this.match(RedisParser.NODE);
                                this.state = 1416;
                                this.stringValue();
                            }
                        }
                        break;
                    case RedisParser.STABLE:
                        {
                            this.state = 1417;
                            this.match(RedisParser.STABLE);
                        }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdFlushdb() {
        let _localctx = new CmdFlushdbContext(this._ctx, this.state);
        this.enterRule(_localctx, 340, RedisParser.RULE_cmdFlushdb);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1420;
                this.match(RedisParser.FLUSHDB);
                this.state = 1422;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 134, this._ctx)) {
                    case 1:
                        {
                            this.state = 1421;
                            _la = this._input.LA(1);
                            if (!(_la === RedisParser.SYNC || _la === RedisParser.ASYNC)) {
                                this._errHandler.recoverInline(this);
                            }
                            else {
                                if (this._input.LA(1) === Token.EOF) {
                                    this.matchedEOF = true;
                                }
                                this._errHandler.reportMatch(this);
                                this.consume();
                            }
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdEcho() {
        let _localctx = new CmdEchoContext(this._ctx, this.state);
        this.enterRule(_localctx, 342, RedisParser.RULE_cmdEcho);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1424;
                this.match(RedisParser.ECHO);
                this.state = 1425;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSave() {
        let _localctx = new CmdSaveContext(this._ctx, this.state);
        this.enterRule(_localctx, 344, RedisParser.RULE_cmdSave);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1427;
                this.match(RedisParser.SAVE);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSlowlog() {
        let _localctx = new CmdSlowlogContext(this._ctx, this.state);
        this.enterRule(_localctx, 346, RedisParser.RULE_cmdSlowlog);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1429;
                this.match(RedisParser.SLOWLOG);
                this.state = 1430;
                this.sqlStatement();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdLastsave() {
        let _localctx = new CmdLastsaveContext(this._ctx, this.state);
        this.enterRule(_localctx, 348, RedisParser.RULE_cmdLastsave);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1432;
                this.match(RedisParser.LASTSAVE);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdConfig() {
        let _localctx = new CmdConfigContext(this._ctx, this.state);
        this.enterRule(_localctx, 350, RedisParser.RULE_cmdConfig);
        try {
            this.state = 1443;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case RedisParser.CONFIG:
                    _localctx = new CmdConfigGetContext(_localctx);
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1434;
                        this.match(RedisParser.CONFIG);
                        {
                            this.state = 1435;
                            this.match(RedisParser.GET);
                            this.state = 1436;
                            this.pattern();
                        }
                    }
                    break;
                case RedisParser.SET:
                    _localctx = new CmdConfigSetContext(_localctx);
                    this.enterOuterAlt(_localctx, 2);
                    {
                        {
                            this.state = 1437;
                            this.match(RedisParser.SET);
                            this.state = 1438;
                            this.stringValue();
                            this.state = 1439;
                            this.stringValue();
                        }
                    }
                    break;
                case RedisParser.RESETSTAT:
                    _localctx = new CmdConfigResetContext(_localctx);
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 1441;
                        this.match(RedisParser.RESETSTAT);
                    }
                    break;
                case RedisParser.REWRITE:
                    _localctx = new CmdConfigRewriteContext(_localctx);
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 1442;
                        this.match(RedisParser.REWRITE);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdClient() {
        let _localctx = new CmdClientContext(this._ctx, this.state);
        this.enterRule(_localctx, 352, RedisParser.RULE_cmdClient);
        try {
            this.state = 1454;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case RedisParser.CLIENT:
                    _localctx = new CmdClientPauseContext(_localctx);
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1445;
                        this.match(RedisParser.CLIENT);
                        {
                            this.state = 1446;
                            this.match(RedisParser.PAUSE);
                            this.state = 1447;
                            this.intValue();
                        }
                    }
                    break;
                case RedisParser.KILL:
                    _localctx = new CmdClientKillContext(_localctx);
                    this.enterOuterAlt(_localctx, 2);
                    {
                        {
                            this.state = 1448;
                            this.match(RedisParser.KILL);
                            this.state = 1449;
                            this.match(RedisParser.HOST_PORT);
                        }
                    }
                    break;
                case RedisParser.GETNAME:
                    _localctx = new CmdClientGetnameContext(_localctx);
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 1450;
                        this.match(RedisParser.GETNAME);
                    }
                    break;
                case RedisParser.SETNAME:
                    _localctx = new CmdClientSetnameContext(_localctx);
                    this.enterOuterAlt(_localctx, 4);
                    {
                        {
                            this.state = 1451;
                            this.match(RedisParser.SETNAME);
                            this.state = 1452;
                            this.stringValue();
                        }
                    }
                    break;
                case RedisParser.LIST:
                    _localctx = new CmdClientListContext(_localctx);
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 1453;
                        this.match(RedisParser.LIST);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdShutdown() {
        let _localctx = new CmdShutdownContext(this._ctx, this.state);
        this.enterRule(_localctx, 354, RedisParser.RULE_cmdShutdown);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1456;
                this.match(RedisParser.SHUTDOWN);
                this.state = 1458;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (_la === RedisParser.NOSAVE) {
                    {
                        this.state = 1457;
                        this.match(RedisParser.NOSAVE);
                    }
                }
                this.state = 1461;
                this._errHandler.sync(this);
                switch (this.interpreter.adaptivePredict(this._input, 138, this._ctx)) {
                    case 1:
                        {
                            this.state = 1460;
                            this.match(RedisParser.SAVE);
                        }
                        break;
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSync() {
        let _localctx = new CmdSyncContext(this._ctx, this.state);
        this.enterRule(_localctx, 356, RedisParser.RULE_cmdSync);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1463;
                this.match(RedisParser.SYNC);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdRole() {
        let _localctx = new CmdRoleContext(this._ctx, this.state);
        this.enterRule(_localctx, 358, RedisParser.RULE_cmdRole);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1465;
                this.match(RedisParser.ROLE);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdMonitor() {
        let _localctx = new CmdMonitorContext(this._ctx, this.state);
        this.enterRule(_localctx, 360, RedisParser.RULE_cmdMonitor);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1467;
                this.match(RedisParser.MONITOR);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSlaveof() {
        let _localctx = new CmdSlaveofContext(this._ctx, this.state);
        this.enterRule(_localctx, 362, RedisParser.RULE_cmdSlaveof);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1469;
                this.match(RedisParser.SLAVEOF);
                this.state = 1470;
                this.match(RedisParser.HOST);
                this.state = 1471;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdFlushall() {
        let _localctx = new CmdFlushallContext(this._ctx, this.state);
        this.enterRule(_localctx, 364, RedisParser.RULE_cmdFlushall);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1473;
                this.match(RedisParser.FLUSHALL);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdSelect() {
        let _localctx = new CmdSelectContext(this._ctx, this.state);
        this.enterRule(_localctx, 366, RedisParser.RULE_cmdSelect);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1475;
                this.match(RedisParser.SELECT);
                this.state = 1476;
                this.intValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdPing() {
        let _localctx = new CmdPingContext(this._ctx, this.state);
        this.enterRule(_localctx, 368, RedisParser.RULE_cmdPing);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1478;
                this.match(RedisParser.PING);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdQuit() {
        let _localctx = new CmdQuitContext(this._ctx, this.state);
        this.enterRule(_localctx, 370, RedisParser.RULE_cmdQuit);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1480;
                this.match(RedisParser.QUIT);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdAuth() {
        let _localctx = new CmdAuthContext(this._ctx, this.state);
        this.enterRule(_localctx, 372, RedisParser.RULE_cmdAuth);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1482;
                this.match(RedisParser.AUTH);
                this.state = 1483;
                this.stringValue();
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdDbsize() {
        let _localctx = new CmdDbsizeContext(this._ctx, this.state);
        this.enterRule(_localctx, 374, RedisParser.RULE_cmdDbsize);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1485;
                this.match(RedisParser.DBSIZE);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdBgrewriteaof() {
        let _localctx = new CmdBgrewriteaofContext(this._ctx, this.state);
        this.enterRule(_localctx, 376, RedisParser.RULE_cmdBgrewriteaof);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1487;
                this.match(RedisParser.BGREWRITEAOF);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdTime() {
        let _localctx = new CmdTimeContext(this._ctx, this.state);
        this.enterRule(_localctx, 378, RedisParser.RULE_cmdTime);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1489;
                this.match(RedisParser.TIME);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdInfo() {
        let _localctx = new CmdInfoContext(this._ctx, this.state);
        this.enterRule(_localctx, 380, RedisParser.RULE_cmdInfo);
        let _la;
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1491;
                this.match(RedisParser.INFO);
                this.state = 1497;
                this._errHandler.sync(this);
                _la = this._input.LA(1);
                if (((((_la - 160)) & ~0x1F) === 0 && ((1 << (_la - 160)) & ((1 << (RedisParser.SERVER - 160)) | (1 << (RedisParser.CLIENTS - 160)) | (1 << (RedisParser.MEMORY - 160)) | (1 << (RedisParser.PERSISTENCE - 160)) | (1 << (RedisParser.STATS - 160)) | (1 << (RedisParser.REPLICATION - 160)) | (1 << (RedisParser.CPU - 160)) | (1 << (RedisParser.COMMANDSTATS - 160)) | (1 << (RedisParser.LATENCYSTATS - 160)) | (1 << (RedisParser.SENTINEL - 160)) | (1 << (RedisParser.MODULES - 160)) | (1 << (RedisParser.KEYSPACE - 160)) | (1 << (RedisParser.ERRORSTATS - 160)) | (1 << (RedisParser.ALL - 160)) | (1 << (RedisParser.DEFAULT - 160)) | (1 << (RedisParser.EVERYTHING - 160)))) !== 0)) {
                    {
                        this.state = 1493;
                        this._errHandler.sync(this);
                        _la = this._input.LA(1);
                        do {
                            {
                                {
                                    this.state = 1492;
                                    this.infoOpt();
                                }
                            }
                            this.state = 1495;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                        } while (((((_la - 160)) & ~0x1F) === 0 && ((1 << (_la - 160)) & ((1 << (RedisParser.SERVER - 160)) | (1 << (RedisParser.CLIENTS - 160)) | (1 << (RedisParser.MEMORY - 160)) | (1 << (RedisParser.PERSISTENCE - 160)) | (1 << (RedisParser.STATS - 160)) | (1 << (RedisParser.REPLICATION - 160)) | (1 << (RedisParser.CPU - 160)) | (1 << (RedisParser.COMMANDSTATS - 160)) | (1 << (RedisParser.LATENCYSTATS - 160)) | (1 << (RedisParser.SENTINEL - 160)) | (1 << (RedisParser.MODULES - 160)) | (1 << (RedisParser.KEYSPACE - 160)) | (1 << (RedisParser.ERRORSTATS - 160)) | (1 << (RedisParser.ALL - 160)) | (1 << (RedisParser.DEFAULT - 160)) | (1 << (RedisParser.EVERYTHING - 160)))) !== 0));
                    }
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdBgsave() {
        let _localctx = new CmdBgsaveContext(this._ctx, this.state);
        this.enterRule(_localctx, 382, RedisParser.RULE_cmdBgsave);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1499;
                this.match(RedisParser.BGSAVE);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdCmd() {
        let _localctx = new CmdCmdContext(this._ctx, this.state);
        this.enterRule(_localctx, 384, RedisParser.RULE_cmdCmd);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1501;
                this.match(RedisParser.COMMAND);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdCmdx() {
        let _localctx = new CmdCmdxContext(this._ctx, this.state);
        this.enterRule(_localctx, 386, RedisParser.RULE_cmdCmdx);
        let _la;
        try {
            this.state = 1517;
            this._errHandler.sync(this);
            switch (this._input.LA(1)) {
                case RedisParser.COMMAND:
                    _localctx = new CmdCmdInfoContext(_localctx);
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1503;
                        this.match(RedisParser.COMMAND);
                        {
                            this.state = 1504;
                            this.match(RedisParser.INFO);
                            this.state = 1506;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            do {
                                {
                                    {
                                        this.state = 1505;
                                        this.keyName();
                                    }
                                }
                                this.state = 1508;
                                this._errHandler.sync(this);
                                _la = this._input.LA(1);
                            } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                        }
                    }
                    break;
                case RedisParser.GETKEYS:
                    _localctx = new CmdCmdGetkeysContext(_localctx);
                    this.enterOuterAlt(_localctx, 2);
                    {
                        {
                            this.state = 1510;
                            this.match(RedisParser.GETKEYS);
                            this.state = 1512;
                            this._errHandler.sync(this);
                            _la = this._input.LA(1);
                            do {
                                {
                                    {
                                        this.state = 1511;
                                        this.keyName();
                                    }
                                }
                                this.state = 1514;
                                this._errHandler.sync(this);
                                _la = this._input.LA(1);
                            } while (_la === RedisParser.STRING || _la === RedisParser.NAME_TOKEN);
                        }
                    }
                    break;
                case RedisParser.COUNT:
                    _localctx = new CmdCmdCountContext(_localctx);
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 1516;
                        this.match(RedisParser.COUNT);
                    }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    cmdDebug() {
        let _localctx = new CmdDebugContext(this._ctx, this.state);
        this.enterRule(_localctx, 388, RedisParser.RULE_cmdDebug);
        try {
            this.enterOuterAlt(_localctx, 1);
            {
                this.state = 1519;
                this.match(RedisParser.DEBUG);
                this.state = 1523;
                this._errHandler.sync(this);
                switch (this._input.LA(1)) {
                    case RedisParser.SEGFAULT:
                        {
                            this.state = 1520;
                            this.match(RedisParser.SEGFAULT);
                        }
                        break;
                    case RedisParser.OBJECT:
                        {
                            this.state = 1521;
                            this.match(RedisParser.OBJECT);
                            this.state = 1522;
                            this.keyName();
                        }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    // @RuleVersion(0)
    sqlStatement() {
        let _localctx = new SqlStatementContext(this._ctx, this.state);
        this.enterRule(_localctx, 390, RedisParser.RULE_sqlStatement);
        try {
            this.state = 1690;
            this._errHandler.sync(this);
            switch (this.interpreter.adaptivePredict(this._input, 145, this._ctx)) {
                case 1:
                    this.enterOuterAlt(_localctx, 1);
                    {
                        this.state = 1525;
                        this.cmdCopy();
                    }
                    break;
                case 2:
                    this.enterOuterAlt(_localctx, 2);
                    {
                        this.state = 1526;
                        this.cmdDel();
                    }
                    break;
                case 3:
                    this.enterOuterAlt(_localctx, 3);
                    {
                        this.state = 1527;
                        this.cmdDump();
                    }
                    break;
                case 4:
                    this.enterOuterAlt(_localctx, 4);
                    {
                        this.state = 1528;
                        this.cmdExists();
                    }
                    break;
                case 5:
                    this.enterOuterAlt(_localctx, 5);
                    {
                        this.state = 1529;
                        this.cmdExpire();
                    }
                    break;
                case 6:
                    this.enterOuterAlt(_localctx, 6);
                    {
                        this.state = 1530;
                        this.cmdExpireat();
                    }
                    break;
                case 7:
                    this.enterOuterAlt(_localctx, 7);
                    {
                        this.state = 1531;
                        this.cmdExpireTime();
                    }
                    break;
                case 8:
                    this.enterOuterAlt(_localctx, 8);
                    {
                        this.state = 1532;
                        this.cmdKeys();
                    }
                    break;
                case 9:
                    this.enterOuterAlt(_localctx, 9);
                    {
                        this.state = 1533;
                        this.cmdMove();
                    }
                    break;
                case 10:
                    this.enterOuterAlt(_localctx, 10);
                    {
                        this.state = 1534;
                        this.cmdObject();
                    }
                    break;
                case 11:
                    this.enterOuterAlt(_localctx, 11);
                    {
                        this.state = 1535;
                        this.cmdPersist();
                    }
                    break;
                case 12:
                    this.enterOuterAlt(_localctx, 12);
                    {
                        this.state = 1536;
                        this.cmdPexpire();
                    }
                    break;
                case 13:
                    this.enterOuterAlt(_localctx, 13);
                    {
                        this.state = 1537;
                        this.cmdPexpireat();
                    }
                    break;
                case 14:
                    this.enterOuterAlt(_localctx, 14);
                    {
                        this.state = 1538;
                        this.cmdPExpireTime();
                    }
                    break;
                case 15:
                    this.enterOuterAlt(_localctx, 15);
                    {
                        this.state = 1539;
                        this.cmdTtl();
                    }
                    break;
                case 16:
                    this.enterOuterAlt(_localctx, 16);
                    {
                        this.state = 1540;
                        this.cmdPttl();
                    }
                    break;
                case 17:
                    this.enterOuterAlt(_localctx, 17);
                    {
                        this.state = 1541;
                        this.cmdRandomkey();
                    }
                    break;
                case 18:
                    this.enterOuterAlt(_localctx, 18);
                    {
                        this.state = 1542;
                        this.cmdRename();
                    }
                    break;
                case 19:
                    this.enterOuterAlt(_localctx, 19);
                    {
                        this.state = 1543;
                        this.cmdRenamenx();
                    }
                    break;
                case 20:
                    this.enterOuterAlt(_localctx, 20);
                    {
                        this.state = 1544;
                        this.cmdRestore();
                    }
                    break;
                case 21:
                    this.enterOuterAlt(_localctx, 21);
                    {
                        this.state = 1545;
                        this.cmdScan();
                    }
                    break;
                case 22:
                    this.enterOuterAlt(_localctx, 22);
                    {
                        this.state = 1546;
                        this.cmdSort();
                    }
                    break;
                case 23:
                    this.enterOuterAlt(_localctx, 23);
                    {
                        this.state = 1547;
                        this.cmdSortro();
                    }
                    break;
                case 24:
                    this.enterOuterAlt(_localctx, 24);
                    {
                        this.state = 1548;
                        this.cmdTouch();
                    }
                    break;
                case 25:
                    this.enterOuterAlt(_localctx, 25);
                    {
                        this.state = 1549;
                        this.cmdType();
                    }
                    break;
                case 26:
                    this.enterOuterAlt(_localctx, 26);
                    {
                        this.state = 1550;
                        this.cmdUnlink();
                    }
                    break;
                case 27:
                    this.enterOuterAlt(_localctx, 27);
                    {
                        this.state = 1551;
                        this.cmdWait();
                    }
                    break;
                case 28:
                    this.enterOuterAlt(_localctx, 28);
                    {
                        this.state = 1552;
                        this.cmdAppend();
                    }
                    break;
                case 29:
                    this.enterOuterAlt(_localctx, 29);
                    {
                        this.state = 1553;
                        this.cmdDecr();
                    }
                    break;
                case 30:
                    this.enterOuterAlt(_localctx, 30);
                    {
                        this.state = 1554;
                        this.cmdDecrby();
                    }
                    break;
                case 31:
                    this.enterOuterAlt(_localctx, 31);
                    {
                        this.state = 1555;
                        this.cmdGet();
                    }
                    break;
                case 32:
                    this.enterOuterAlt(_localctx, 32);
                    {
                        this.state = 1556;
                        this.cmdGetdel();
                    }
                    break;
                case 33:
                    this.enterOuterAlt(_localctx, 33);
                    {
                        this.state = 1557;
                        this.cmdGetex();
                    }
                    break;
                case 34:
                    this.enterOuterAlt(_localctx, 34);
                    {
                        this.state = 1558;
                        this.cmdGetrange();
                    }
                    break;
                case 35:
                    this.enterOuterAlt(_localctx, 35);
                    {
                        this.state = 1559;
                        this.cmdGetset();
                    }
                    break;
                case 36:
                    this.enterOuterAlt(_localctx, 36);
                    {
                        this.state = 1560;
                        this.cmdGetbit();
                    }
                    break;
                case 37:
                    this.enterOuterAlt(_localctx, 37);
                    {
                        this.state = 1561;
                        this.cmdInc();
                    }
                    break;
                case 38:
                    this.enterOuterAlt(_localctx, 38);
                    {
                        this.state = 1562;
                        this.cmdLcs();
                    }
                    break;
                case 39:
                    this.enterOuterAlt(_localctx, 39);
                    {
                        this.state = 1563;
                        this.cmdMget();
                    }
                    break;
                case 40:
                    this.enterOuterAlt(_localctx, 40);
                    {
                        this.state = 1564;
                        this.cmdMset();
                    }
                    break;
                case 41:
                    this.enterOuterAlt(_localctx, 41);
                    {
                        this.state = 1565;
                        this.cmdMsetnx();
                    }
                    break;
                case 42:
                    this.enterOuterAlt(_localctx, 42);
                    {
                        this.state = 1566;
                        this.cmdSetex();
                    }
                    break;
                case 43:
                    this.enterOuterAlt(_localctx, 43);
                    {
                        this.state = 1567;
                        this.cmdSet();
                    }
                    break;
                case 44:
                    this.enterOuterAlt(_localctx, 44);
                    {
                        this.state = 1568;
                        this.cmdSetnx();
                    }
                    break;
                case 45:
                    this.enterOuterAlt(_localctx, 45);
                    {
                        this.state = 1569;
                        this.cmdSetrange();
                    }
                    break;
                case 46:
                    this.enterOuterAlt(_localctx, 46);
                    {
                        this.state = 1570;
                        this.cmdSetbit();
                    }
                    break;
                case 47:
                    this.enterOuterAlt(_localctx, 47);
                    {
                        this.state = 1571;
                        this.cmdStrlen();
                    }
                    break;
                case 48:
                    this.enterOuterAlt(_localctx, 48);
                    {
                        this.state = 1572;
                        this.cmdSubstr();
                    }
                    break;
                case 49:
                    this.enterOuterAlt(_localctx, 49);
                    {
                        this.state = 1573;
                        this.cmdHdelMget();
                    }
                    break;
                case 50:
                    this.enterOuterAlt(_localctx, 50);
                    {
                        this.state = 1574;
                        this.cmdHexistsGetKeysStrlen();
                    }
                    break;
                case 51:
                    this.enterOuterAlt(_localctx, 51);
                    {
                        this.state = 1575;
                        this.cmdHgetallLenVals();
                    }
                    break;
                case 52:
                    this.enterOuterAlt(_localctx, 52);
                    {
                        this.state = 1576;
                        this.cmdHincrby();
                    }
                    break;
                case 53:
                    this.enterOuterAlt(_localctx, 53);
                    {
                        this.state = 1577;
                        this.cmdHmsetHset();
                    }
                    break;
                case 54:
                    this.enterOuterAlt(_localctx, 54);
                    {
                        this.state = 1578;
                        this.cmdHrandfield();
                    }
                    break;
                case 55:
                    this.enterOuterAlt(_localctx, 55);
                    {
                        this.state = 1579;
                        this.cmdHscan();
                    }
                    break;
                case 56:
                    this.enterOuterAlt(_localctx, 56);
                    {
                        this.state = 1580;
                        this.cmdHsetnx();
                    }
                    break;
                case 57:
                    this.enterOuterAlt(_localctx, 57);
                    {
                        this.state = 1581;
                        this.cmdBlmove();
                    }
                    break;
                case 58:
                    this.enterOuterAlt(_localctx, 58);
                    {
                        this.state = 1582;
                        this.cmdLmpop();
                    }
                    break;
                case 59:
                    this.enterOuterAlt(_localctx, 59);
                    {
                        this.state = 1583;
                        this.cmdBpop();
                    }
                    break;
                case 60:
                    this.enterOuterAlt(_localctx, 60);
                    {
                        this.state = 1584;
                        this.cmdBrpoplpush();
                    }
                    break;
                case 61:
                    this.enterOuterAlt(_localctx, 61);
                    {
                        this.state = 1585;
                        this.cmdLindex();
                    }
                    break;
                case 62:
                    this.enterOuterAlt(_localctx, 62);
                    {
                        this.state = 1586;
                        this.cmdLinsert();
                    }
                    break;
                case 63:
                    this.enterOuterAlt(_localctx, 63);
                    {
                        this.state = 1587;
                        this.cmdLlen();
                    }
                    break;
                case 64:
                    this.enterOuterAlt(_localctx, 64);
                    {
                        this.state = 1588;
                        this.cmdLmove();
                    }
                    break;
                case 65:
                    this.enterOuterAlt(_localctx, 65);
                    {
                        this.state = 1589;
                        this.cmdPop();
                    }
                    break;
                case 66:
                    this.enterOuterAlt(_localctx, 66);
                    {
                        this.state = 1590;
                        this.cmdLpos();
                    }
                    break;
                case 67:
                    this.enterOuterAlt(_localctx, 67);
                    {
                        this.state = 1591;
                        this.cmdPush();
                    }
                    break;
                case 68:
                    this.enterOuterAlt(_localctx, 68);
                    {
                        this.state = 1592;
                        this.cmdLrangeTrim();
                    }
                    break;
                case 69:
                    this.enterOuterAlt(_localctx, 69);
                    {
                        this.state = 1593;
                        this.cmdLremSet();
                    }
                    break;
                case 70:
                    this.enterOuterAlt(_localctx, 70);
                    {
                        this.state = 1594;
                        this.cmdRpoplpush();
                    }
                    break;
                case 71:
                    this.enterOuterAlt(_localctx, 71);
                    {
                        this.state = 1595;
                        this.cmdSadd();
                    }
                    break;
                case 72:
                    this.enterOuterAlt(_localctx, 72);
                    {
                        this.state = 1596;
                        this.cmdScard();
                    }
                    break;
                case 73:
                    this.enterOuterAlt(_localctx, 73);
                    {
                        this.state = 1597;
                        this.cmdSdiff();
                    }
                    break;
                case 74:
                    this.enterOuterAlt(_localctx, 74);
                    {
                        this.state = 1598;
                        this.cmdSdiffstore();
                    }
                    break;
                case 75:
                    this.enterOuterAlt(_localctx, 75);
                    {
                        this.state = 1599;
                        this.cmdSintercard();
                    }
                    break;
                case 76:
                    this.enterOuterAlt(_localctx, 76);
                    {
                        this.state = 1600;
                        this.cmdSismember();
                    }
                    break;
                case 77:
                    this.enterOuterAlt(_localctx, 77);
                    {
                        this.state = 1601;
                        this.cmdSmove();
                    }
                    break;
                case 78:
                    this.enterOuterAlt(_localctx, 78);
                    {
                        this.state = 1602;
                        this.cmdSpop();
                    }
                    break;
                case 79:
                    this.enterOuterAlt(_localctx, 79);
                    {
                        this.state = 1603;
                        this.cmdSscan();
                    }
                    break;
                case 80:
                    this.enterOuterAlt(_localctx, 80);
                    {
                        this.state = 1604;
                        this.cmdBzmpop();
                    }
                    break;
                case 81:
                    this.enterOuterAlt(_localctx, 81);
                    {
                        this.state = 1605;
                        this.cmdBzpopmax();
                    }
                    break;
                case 82:
                    this.enterOuterAlt(_localctx, 82);
                    {
                        this.state = 1606;
                        this.cmdZadd();
                    }
                    break;
                case 83:
                    this.enterOuterAlt(_localctx, 83);
                    {
                        this.state = 1607;
                        this.cmdZcard();
                    }
                    break;
                case 84:
                    this.enterOuterAlt(_localctx, 84);
                    {
                        this.state = 1608;
                        this.cmdZcount();
                    }
                    break;
                case 85:
                    this.enterOuterAlt(_localctx, 85);
                    {
                        this.state = 1609;
                        this.cmdZdiff();
                    }
                    break;
                case 86:
                    this.enterOuterAlt(_localctx, 86);
                    {
                        this.state = 1610;
                        this.cmdZdiffstore();
                    }
                    break;
                case 87:
                    this.enterOuterAlt(_localctx, 87);
                    {
                        this.state = 1611;
                        this.cmdZincrby();
                    }
                    break;
                case 88:
                    this.enterOuterAlt(_localctx, 88);
                    {
                        this.state = 1612;
                        this.cmdZinter();
                    }
                    break;
                case 89:
                    this.enterOuterAlt(_localctx, 89);
                    {
                        this.state = 1613;
                        this.cmdZintercard();
                    }
                    break;
                case 90:
                    this.enterOuterAlt(_localctx, 90);
                    {
                        this.state = 1614;
                        this.cmdZinterstore();
                    }
                    break;
                case 91:
                    this.enterOuterAlt(_localctx, 91);
                    {
                        this.state = 1615;
                        this.cmdZmpop();
                    }
                    break;
                case 92:
                    this.enterOuterAlt(_localctx, 92);
                    {
                        this.state = 1616;
                        this.cmdZmscore();
                    }
                    break;
                case 93:
                    this.enterOuterAlt(_localctx, 93);
                    {
                        this.state = 1617;
                        this.cmdZpopmax();
                    }
                    break;
                case 94:
                    this.enterOuterAlt(_localctx, 94);
                    {
                        this.state = 1618;
                        this.cmdZrandmember();
                    }
                    break;
                case 95:
                    this.enterOuterAlt(_localctx, 95);
                    {
                        this.state = 1619;
                        this.cmdZrange();
                    }
                    break;
                case 96:
                    this.enterOuterAlt(_localctx, 96);
                    {
                        this.state = 1620;
                        this.cmdZrangebylex();
                    }
                    break;
                case 97:
                    this.enterOuterAlt(_localctx, 97);
                    {
                        this.state = 1621;
                        this.cmdZrangebyscore();
                    }
                    break;
                case 98:
                    this.enterOuterAlt(_localctx, 98);
                    {
                        this.state = 1622;
                        this.cmdZrangestore();
                    }
                    break;
                case 99:
                    this.enterOuterAlt(_localctx, 99);
                    {
                        this.state = 1623;
                        this.cmdZrank();
                    }
                    break;
                case 100:
                    this.enterOuterAlt(_localctx, 100);
                    {
                        this.state = 1624;
                        this.cmdZremrangebyrank();
                    }
                    break;
                case 101:
                    this.enterOuterAlt(_localctx, 101);
                    {
                        this.state = 1625;
                        this.cmdZrevrange();
                    }
                    break;
                case 102:
                    this.enterOuterAlt(_localctx, 102);
                    {
                        this.state = 1626;
                        this.cmdZrevrangebylex();
                    }
                    break;
                case 103:
                    this.enterOuterAlt(_localctx, 103);
                    {
                        this.state = 1627;
                        this.cmdZscan();
                    }
                    break;
                case 104:
                    this.enterOuterAlt(_localctx, 104);
                    {
                        this.state = 1628;
                        this.cmdZunion();
                    }
                    break;
                case 105:
                    this.enterOuterAlt(_localctx, 105);
                    {
                        this.state = 1629;
                        this.cmdZunionstore();
                    }
                    break;
                case 106:
                    this.enterOuterAlt(_localctx, 106);
                    {
                        this.state = 1630;
                        this.cmdScriptKill();
                    }
                    break;
                case 107:
                    this.enterOuterAlt(_localctx, 107);
                    {
                        this.state = 1631;
                        this.cmdScriptLoad();
                    }
                    break;
                case 108:
                    this.enterOuterAlt(_localctx, 108);
                    {
                        this.state = 1632;
                        this.cmdEval();
                    }
                    break;
                case 109:
                    this.enterOuterAlt(_localctx, 109);
                    {
                        this.state = 1633;
                        this.cmdEvalsha();
                    }
                    break;
                case 110:
                    this.enterOuterAlt(_localctx, 110);
                    {
                        this.state = 1634;
                        this.cmdScriptExists();
                    }
                    break;
                case 111:
                    this.enterOuterAlt(_localctx, 111);
                    {
                        this.state = 1635;
                        this.cmdScriptFlush();
                    }
                    break;
                case 112:
                    this.enterOuterAlt(_localctx, 112);
                    {
                        this.state = 1636;
                        this.cmdExec();
                    }
                    break;
                case 113:
                    this.enterOuterAlt(_localctx, 113);
                    {
                        this.state = 1637;
                        this.cmdWatch();
                    }
                    break;
                case 114:
                    this.enterOuterAlt(_localctx, 114);
                    {
                        this.state = 1638;
                        this.cmdDiscard();
                    }
                    break;
                case 115:
                    this.enterOuterAlt(_localctx, 115);
                    {
                        this.state = 1639;
                        this.cmdUnwatch();
                    }
                    break;
                case 116:
                    this.enterOuterAlt(_localctx, 116);
                    {
                        this.state = 1640;
                        this.cmdMulti();
                    }
                    break;
                case 117:
                    this.enterOuterAlt(_localctx, 117);
                    {
                        this.state = 1641;
                        this.cmdPfmerge();
                    }
                    break;
                case 118:
                    this.enterOuterAlt(_localctx, 118);
                    {
                        this.state = 1642;
                        this.cmdPfadd();
                    }
                    break;
                case 119:
                    this.enterOuterAlt(_localctx, 119);
                    {
                        this.state = 1643;
                        this.cmdPfcount();
                    }
                    break;
                case 120:
                    this.enterOuterAlt(_localctx, 120);
                    {
                        this.state = 1644;
                        this.cmdEcho();
                    }
                    break;
                case 121:
                    this.enterOuterAlt(_localctx, 121);
                    {
                        this.state = 1645;
                        this.cmdSelect();
                    }
                    break;
                case 122:
                    this.enterOuterAlt(_localctx, 122);
                    {
                        this.state = 1646;
                        this.cmdPing();
                    }
                    break;
                case 123:
                    this.enterOuterAlt(_localctx, 123);
                    {
                        this.state = 1647;
                        this.cmdSave();
                    }
                    break;
                case 124:
                    this.enterOuterAlt(_localctx, 124);
                    {
                        this.state = 1648;
                        this.cmdSlowlog();
                    }
                    break;
                case 125:
                    this.enterOuterAlt(_localctx, 125);
                    {
                        this.state = 1649;
                        this.cmdLastsave();
                    }
                    break;
                case 126:
                    this.enterOuterAlt(_localctx, 126);
                    {
                        this.state = 1650;
                        this.cmdConfig();
                    }
                    break;
                case 127:
                    this.enterOuterAlt(_localctx, 127);
                    {
                        this.state = 1651;
                        this.cmdClient();
                    }
                    break;
                case 128:
                    this.enterOuterAlt(_localctx, 128);
                    {
                        this.state = 1652;
                        this.cmdShutdown();
                    }
                    break;
                case 129:
                    this.enterOuterAlt(_localctx, 129);
                    {
                        this.state = 1653;
                        this.cmdSync();
                    }
                    break;
                case 130:
                    this.enterOuterAlt(_localctx, 130);
                    {
                        this.state = 1654;
                        this.cmdRole();
                    }
                    break;
                case 131:
                    this.enterOuterAlt(_localctx, 131);
                    {
                        this.state = 1655;
                        this.cmdMonitor();
                    }
                    break;
                case 132:
                    this.enterOuterAlt(_localctx, 132);
                    {
                        this.state = 1656;
                        this.cmdSlaveof();
                    }
                    break;
                case 133:
                    this.enterOuterAlt(_localctx, 133);
                    {
                        this.state = 1657;
                        this.cmdFlushall();
                    }
                    break;
                case 134:
                    this.enterOuterAlt(_localctx, 134);
                    {
                        this.state = 1658;
                        this.cmdQuit();
                    }
                    break;
                case 135:
                    this.enterOuterAlt(_localctx, 135);
                    {
                        this.state = 1659;
                        this.cmdAuth();
                    }
                    break;
                case 136:
                    this.enterOuterAlt(_localctx, 136);
                    {
                        this.state = 1660;
                        this.cmdDbsize();
                    }
                    break;
                case 137:
                    this.enterOuterAlt(_localctx, 137);
                    {
                        this.state = 1661;
                        this.cmdBgrewriteaof();
                    }
                    break;
                case 138:
                    this.enterOuterAlt(_localctx, 138);
                    {
                        this.state = 1662;
                        this.cmdTime();
                    }
                    break;
                case 139:
                    this.enterOuterAlt(_localctx, 139);
                    {
                        this.state = 1663;
                        this.cmdInfo();
                    }
                    break;
                case 140:
                    this.enterOuterAlt(_localctx, 140);
                    {
                        this.state = 1664;
                        this.cmdBgsave();
                    }
                    break;
                case 141:
                    this.enterOuterAlt(_localctx, 141);
                    {
                        this.state = 1665;
                        this.cmdCmd();
                    }
                    break;
                case 142:
                    this.enterOuterAlt(_localctx, 142);
                    {
                        this.state = 1666;
                        this.cmdCmdx();
                    }
                    break;
                case 143:
                    this.enterOuterAlt(_localctx, 143);
                    {
                        this.state = 1667;
                        this.cmdDebug();
                    }
                    break;
                case 144:
                    this.enterOuterAlt(_localctx, 144);
                    {
                        this.state = 1668;
                        this.cmdFlushdb();
                    }
                    break;
                case 145:
                    this.enterOuterAlt(_localctx, 145);
                    {
                        this.state = 1669;
                        this.cmdSsubscribe();
                    }
                    break;
                case 146:
                    this.enterOuterAlt(_localctx, 146);
                    {
                        this.state = 1670;
                        this.cmdUnsubscribe();
                    }
                    break;
                case 147:
                    this.enterOuterAlt(_localctx, 147);
                    {
                        this.state = 1671;
                        this.cmdPusubnumsub();
                    }
                    break;
                case 148:
                    this.enterOuterAlt(_localctx, 148);
                    {
                        this.state = 1672;
                        this.cmdPusubnumpat();
                    }
                    break;
                case 149:
                    this.enterOuterAlt(_localctx, 149);
                    {
                        this.state = 1673;
                        this.cmdPusubchannels();
                    }
                    break;
                case 150:
                    this.enterOuterAlt(_localctx, 150);
                    {
                        this.state = 1674;
                        this.cmdPublish();
                    }
                    break;
                case 151:
                    this.enterOuterAlt(_localctx, 151);
                    {
                        this.state = 1675;
                        this.cmdInfo();
                    }
                    break;
                case 152:
                    this.enterOuterAlt(_localctx, 152);
                    {
                        this.state = 1676;
                        this.cmdAsking();
                    }
                    break;
                case 153:
                    this.enterOuterAlt(_localctx, 153);
                    {
                        this.state = 1677;
                        this.cmdAddDelSlots();
                    }
                    break;
                case 154:
                    this.enterOuterAlt(_localctx, 154);
                    {
                        this.state = 1678;
                        this.cmdCountKeysInSlot();
                    }
                    break;
                case 155:
                    this.enterOuterAlt(_localctx, 155);
                    {
                        this.state = 1679;
                        this.cmdFailOver();
                    }
                    break;
                case 156:
                    this.enterOuterAlt(_localctx, 156);
                    {
                        this.state = 1680;
                        this.cmdForget();
                    }
                    break;
                case 157:
                    this.enterOuterAlt(_localctx, 157);
                    {
                        this.state = 1681;
                        this.cmdGetKeysInSlot();
                    }
                    break;
                case 158:
                    this.enterOuterAlt(_localctx, 158);
                    {
                        this.state = 1682;
                        this.cmdClusterOrder();
                    }
                    break;
                case 159:
                    this.enterOuterAlt(_localctx, 159);
                    {
                        this.state = 1683;
                        this.cmdKeySlot();
                    }
                    break;
                case 160:
                    this.enterOuterAlt(_localctx, 160);
                    {
                        this.state = 1684;
                        this.cmdMeet();
                    }
                    break;
                case 161:
                    this.enterOuterAlt(_localctx, 161);
                    {
                        this.state = 1685;
                        this.cmdReadonly();
                    }
                    break;
                case 162:
                    this.enterOuterAlt(_localctx, 162);
                    {
                        this.state = 1686;
                        this.cmdReadwrite();
                    }
                    break;
                case 163:
                    this.enterOuterAlt(_localctx, 163);
                    {
                        this.state = 1687;
                        this.cmdReplicaesSlave();
                    }
                    break;
                case 164:
                    this.enterOuterAlt(_localctx, 164);
                    {
                        this.state = 1688;
                        this.cmdReset();
                    }
                    break;
                case 165:
                    this.enterOuterAlt(_localctx, 165);
                    {
                        this.state = 1689;
                        this.cmdSetSlot();
                    }
                    break;
            }
        }
        catch (re) {
            if (re instanceof RecognitionException) {
                _localctx.exception = re;
                this._errHandler.reportError(this, re);
                this._errHandler.recover(this, re);
            }
            else {
                throw re;
            }
        }
        finally {
            this.exitRule();
        }
        return _localctx;
    }
    static get _ATN() {
        if (!RedisParser.__ATN) {
            RedisParser.__ATN = new ATNDeserializer().deserialize(Utils.toCharArray(RedisParser._serializedATN));
        }
        return RedisParser.__ATN;
    }
}
RedisParser.SPACE = 1;
RedisParser.SPEC_MYSQL_COMMENT = 2;
RedisParser.COMMENT_INPUT = 3;
RedisParser.LINE_COMMENT = 4;
RedisParser.COPY = 5;
RedisParser.DEL = 6;
RedisParser.DUMP = 7;
RedisParser.EXISTS = 8;
RedisParser.EXPIRE = 9;
RedisParser.EXPIREAT = 10;
RedisParser.EXPIRETIME = 11;
RedisParser.KEYS = 12;
RedisParser.MOVE = 13;
RedisParser.OBJECT = 14;
RedisParser.PERSIST = 15;
RedisParser.PEXPIRE = 16;
RedisParser.PEXPIREAT = 17;
RedisParser.PEXPIRETIME = 18;
RedisParser.TTL = 19;
RedisParser.PTTL = 20;
RedisParser.RANDOMKEY = 21;
RedisParser.RENAME = 22;
RedisParser.RENAMENX = 23;
RedisParser.RESTORE = 24;
RedisParser.SCAN = 25;
RedisParser.SORT = 26;
RedisParser.SORT_RO = 27;
RedisParser.TOUCH = 28;
RedisParser.TYPE = 29;
RedisParser.UNLINK = 30;
RedisParser.WAIT = 31;
RedisParser.APPEND = 32;
RedisParser.DECR = 33;
RedisParser.DECRBY = 34;
RedisParser.GET = 35;
RedisParser.GETDEL = 36;
RedisParser.GETEX = 37;
RedisParser.GETRANGE = 38;
RedisParser.GETSET = 39;
RedisParser.GETBIT = 40;
RedisParser.INCR = 41;
RedisParser.INCRBY = 42;
RedisParser.INCRBYFLOAT = 43;
RedisParser.LCS = 44;
RedisParser.MGET = 45;
RedisParser.MSET = 46;
RedisParser.MSETNX = 47;
RedisParser.SETEX = 48;
RedisParser.PSETEX = 49;
RedisParser.SET = 50;
RedisParser.SETNX = 51;
RedisParser.SETRANGE = 52;
RedisParser.SETBIT = 53;
RedisParser.STRLEN = 54;
RedisParser.SUBSTR = 55;
RedisParser.HDEL = 56;
RedisParser.HEXISTS = 57;
RedisParser.HGET = 58;
RedisParser.HGETALL = 59;
RedisParser.HINCRBY = 60;
RedisParser.HINCRBYFLOAT = 61;
RedisParser.HKEYS = 62;
RedisParser.HLEN = 63;
RedisParser.HMGET = 64;
RedisParser.HMSET = 65;
RedisParser.HRANDFIELD = 66;
RedisParser.HSCAN = 67;
RedisParser.HSET = 68;
RedisParser.HSETNX = 69;
RedisParser.HSTRLEN = 70;
RedisParser.HVALS = 71;
RedisParser.BLMOVE = 72;
RedisParser.BLMPOP = 73;
RedisParser.BLPOP = 74;
RedisParser.BRPOP = 75;
RedisParser.BRPOPLPUSH = 76;
RedisParser.LINDEX = 77;
RedisParser.LINSERT = 78;
RedisParser.LLEN = 79;
RedisParser.LMOVE = 80;
RedisParser.LMPOP = 81;
RedisParser.LPOP = 82;
RedisParser.LPOS = 83;
RedisParser.LPUSH = 84;
RedisParser.LPUSHX = 85;
RedisParser.LRANGE = 86;
RedisParser.LREM = 87;
RedisParser.LSET = 88;
RedisParser.LTRIM = 89;
RedisParser.RPOP = 90;
RedisParser.RPOPLPUSH = 91;
RedisParser.RPUSH = 92;
RedisParser.RPUSHX = 93;
RedisParser.SADD = 94;
RedisParser.SCARD = 95;
RedisParser.SDIFF = 96;
RedisParser.SDIFFSTORE = 97;
RedisParser.SINTER = 98;
RedisParser.SINTERCARD = 99;
RedisParser.SINTERSTORE = 100;
RedisParser.SISMEMBER = 101;
RedisParser.SMEMBERS = 102;
RedisParser.SMISMEMBER = 103;
RedisParser.SMOVE = 104;
RedisParser.SPOP = 105;
RedisParser.SRANDMEMBER = 106;
RedisParser.SREM = 107;
RedisParser.SSCAN = 108;
RedisParser.SUNION = 109;
RedisParser.SUNIONSTORE = 110;
RedisParser.BZMPOP = 111;
RedisParser.BZPOPMAX = 112;
RedisParser.BZPOPMIN = 113;
RedisParser.ZADD = 114;
RedisParser.ZCARD = 115;
RedisParser.ZCOUNT = 116;
RedisParser.ZDIFF = 117;
RedisParser.ZDIFFSTORE = 118;
RedisParser.ZINCRBY = 119;
RedisParser.ZINTER = 120;
RedisParser.ZINTERCARD = 121;
RedisParser.ZINTERSTORE = 122;
RedisParser.ZLEXCOUNT = 123;
RedisParser.ZMPOP = 124;
RedisParser.ZMSCORE = 125;
RedisParser.ZPOPMAX = 126;
RedisParser.ZPOPMIN = 127;
RedisParser.ZRANDMEMBER = 128;
RedisParser.ZRANGE = 129;
RedisParser.ZRANGEBYLEX = 130;
RedisParser.ZRANGEBYSCORE = 131;
RedisParser.ZRANGESTORE = 132;
RedisParser.ZRANK = 133;
RedisParser.ZREM = 134;
RedisParser.ZREMRANGEBYLEX = 135;
RedisParser.ZREMRANGEBYRANK = 136;
RedisParser.ZREMRANGEBYSCORE = 137;
RedisParser.ZREVRANGE = 138;
RedisParser.ZREVRANGEBYLEX = 139;
RedisParser.ZREVRANGEBYSCORE = 140;
RedisParser.ZREVRANK = 141;
RedisParser.ZSCAN = 142;
RedisParser.ZSCORE = 143;
RedisParser.ZUNION = 144;
RedisParser.ZUNIONSTORE = 145;
RedisParser.PSUBSCRIBE = 146;
RedisParser.PUBSUB = 147;
RedisParser.PUBLISH = 148;
RedisParser.CHANNELS = 149;
RedisParser.NUMSUB = 150;
RedisParser.NUMPAT = 151;
RedisParser.SHARDCHANNELS = 152;
RedisParser.SHARDNUMSUB = 153;
RedisParser.PUNSUBSCRIBE = 154;
RedisParser.SPUBLISH = 155;
RedisParser.SUBSCRIBE = 156;
RedisParser.UNSUBSCRIBE = 157;
RedisParser.SSUBSCRIBE = 158;
RedisParser.SUNSUBSCRIBE = 159;
RedisParser.SERVER = 160;
RedisParser.CLIENTS = 161;
RedisParser.MEMORY = 162;
RedisParser.PERSISTENCE = 163;
RedisParser.STATS = 164;
RedisParser.REPLICATION = 165;
RedisParser.CPU = 166;
RedisParser.COMMANDSTATS = 167;
RedisParser.LATENCYSTATS = 168;
RedisParser.SENTINEL = 169;
RedisParser.MODULES = 170;
RedisParser.KEYSPACE = 171;
RedisParser.ERRORSTATS = 172;
RedisParser.ALL = 173;
RedisParser.DEFAULT = 174;
RedisParser.EVERYTHING = 175;
RedisParser.ASKING = 176;
RedisParser.CLUSTER = 177;
RedisParser.ADDSLOTS = 178;
RedisParser.DELSLOTS = 179;
RedisParser.ADDSLOTSRANGE = 180;
RedisParser.BUMPEPOCH = 181;
RedisParser.COUNTKEYSINSLOT = 182;
RedisParser.DELSLOTSRANGE = 183;
RedisParser.FAILOVER = 184;
RedisParser.FLUSHSLOTS = 185;
RedisParser.FORGET = 186;
RedisParser.GETKEYSINSLOT = 187;
RedisParser.KEYSLOT = 188;
RedisParser.LINKS = 189;
RedisParser.MEET = 190;
RedisParser.MYID = 191;
RedisParser.MYSHARDID = 192;
RedisParser.FORCE = 193;
RedisParser.TAKEOVER = 194;
RedisParser.NODE = 195;
RedisParser.NODES = 196;
RedisParser.REPLICAS = 197;
RedisParser.REPLICATE = 198;
RedisParser.RESET = 199;
RedisParser.HARD = 200;
RedisParser.SOFT = 201;
RedisParser.SAVECONFIG = 202;
RedisParser.SETSLOT = 203;
RedisParser.IMPORTING = 204;
RedisParser.MIGRATING = 205;
RedisParser.STABLE = 206;
RedisParser.SHARDS = 207;
RedisParser.SLAVES = 208;
RedisParser.READONLY = 209;
RedisParser.READWRITE = 210;
RedisParser.FLUSHDB = 211;
RedisParser.SCRIPT = 212;
RedisParser.EVAL = 213;
RedisParser.EVALSHA = 214;
RedisParser.EXEC = 215;
RedisParser.WATCH = 216;
RedisParser.DISCARD = 217;
RedisParser.UNWATCH = 218;
RedisParser.MULTI = 219;
RedisParser.PFMERGE = 220;
RedisParser.PFADD = 221;
RedisParser.PFCOUNT = 222;
RedisParser.ECHO = 223;
RedisParser.PING = 224;
RedisParser.SAVE = 225;
RedisParser.SLOWLOG = 226;
RedisParser.LASTSAVE = 227;
RedisParser.CONFIG = 228;
RedisParser.CLIENT = 229;
RedisParser.SHUTDOWN = 230;
RedisParser.SYNC = 231;
RedisParser.ROLE = 232;
RedisParser.MONITOR = 233;
RedisParser.SLAVEOF = 234;
RedisParser.FLUSHALL = 235;
RedisParser.SELECT = 236;
RedisParser.QUIT = 237;
RedisParser.AUTH = 238;
RedisParser.DBSIZE = 239;
RedisParser.BGREWRITEAOF = 240;
RedisParser.TIME = 241;
RedisParser.INFO = 242;
RedisParser.BGSAVE = 243;
RedisParser.COMMAND = 244;
RedisParser.DEBUG = 245;
RedisParser.DB = 246;
RedisParser.NX = 247;
RedisParser.XX = 248;
RedisParser.GT = 249;
RedisParser.LT = 250;
RedisParser.ENCODING = 251;
RedisParser.FREQ = 252;
RedisParser.IDLETIME = 253;
RedisParser.REFCOUNT = 254;
RedisParser.ABSTTL = 255;
RedisParser.BY = 256;
RedisParser.ASC = 257;
RedisParser.DESC = 258;
RedisParser.ALPHA = 259;
RedisParser.STORE = 260;
RedisParser.REPLACE = 261;
RedisParser.EX = 262;
RedisParser.PX = 263;
RedisParser.EXAT = 264;
RedisParser.PXAT = 265;
RedisParser.LEN = 266;
RedisParser.IDX = 267;
RedisParser.MINMATCHLEN = 268;
RedisParser.WITHMATCHLEN = 269;
RedisParser.KEEPTTL = 270;
RedisParser.WITHSCORES = 271;
RedisParser.LIMIT = 272;
RedisParser.BEFORE = 273;
RedisParser.AFTER = 274;
RedisParser.FLUSH = 275;
RedisParser.RESETSTAT = 276;
RedisParser.REWRITE = 277;
RedisParser.PAUSE = 278;
RedisParser.SETNAME = 279;
RedisParser.GETNAME = 280;
RedisParser.LIST = 281;
RedisParser.NOSAVE = 282;
RedisParser.SLOTS = 283;
RedisParser.GETKEYS = 284;
RedisParser.COUNT = 285;
RedisParser.SEGFAULT = 286;
RedisParser.KILL = 287;
RedisParser.LOAD = 288;
RedisParser.WITHVALUES = 289;
RedisParser.MATCH = 290;
RedisParser.MIN = 291;
RedisParser.MAX = 292;
RedisParser.CH = 293;
RedisParser.WEIGHTS = 294;
RedisParser.AGGREGATE = 295;
RedisParser.SUM = 296;
RedisParser.BYSCORE = 297;
RedisParser.BYLEX = 298;
RedisParser.REV = 299;
RedisParser.LEFT = 300;
RedisParser.RIGHT = 301;
RedisParser.RANK = 302;
RedisParser.MAXLEN = 303;
RedisParser.ASYNC = 304;
RedisParser.HEX_NUM = 305;
RedisParser.OCT_NUM = 306;
RedisParser.BIT_NUM = 307;
RedisParser.INTEGER_NUM = 308;
RedisParser.DECIMAL_NUM = 309;
RedisParser.STRING = 310;
RedisParser.HOST = 311;
RedisParser.HOST_PORT = 312;
RedisParser.NAME_TOKEN = 313;
RedisParser.SEMI = 314;
RedisParser.RULE_program = 0;
RedisParser.RULE_sqlStatements = 1;
RedisParser.RULE_emptyStatement_ = 2;
RedisParser.RULE_keyName = 3;
RedisParser.RULE_fieldName = 4;
RedisParser.RULE_stringValue = 5;
RedisParser.RULE_intValue = 6;
RedisParser.RULE_floatValue = 7;
RedisParser.RULE_pattern = 8;
RedisParser.RULE_cursor = 9;
RedisParser.RULE_min = 10;
RedisParser.RULE_max = 11;
RedisParser.RULE_multiString = 12;
RedisParser.RULE_cout = 13;
RedisParser.RULE_start = 14;
RedisParser.RULE_end = 15;
RedisParser.RULE_port = 16;
RedisParser.RULE_keyAndString = 17;
RedisParser.RULE_intAndString = 18;
RedisParser.RULE_infoOpt = 19;
RedisParser.RULE_patternOpt = 20;
RedisParser.RULE_keyOpt = 21;
RedisParser.RULE_idletimeOpt = 22;
RedisParser.RULE_freqOpt = 23;
RedisParser.RULE_limitOpt = 24;
RedisParser.RULE_sortOpt = 25;
RedisParser.RULE_getsetOpt = 26;
RedisParser.RULE_lrOpt = 27;
RedisParser.RULE_rankOpt = 28;
RedisParser.RULE_countOpt = 29;
RedisParser.RULE_maxlenOpt = 30;
RedisParser.RULE_cmdCopy = 31;
RedisParser.RULE_cmdDel = 32;
RedisParser.RULE_cmdDump = 33;
RedisParser.RULE_cmdExists = 34;
RedisParser.RULE_cmdExpire = 35;
RedisParser.RULE_cmdExpireat = 36;
RedisParser.RULE_cmdExpireTime = 37;
RedisParser.RULE_cmdKeys = 38;
RedisParser.RULE_cmdMove = 39;
RedisParser.RULE_cmdObject = 40;
RedisParser.RULE_cmdPersist = 41;
RedisParser.RULE_cmdPexpire = 42;
RedisParser.RULE_cmdPexpireat = 43;
RedisParser.RULE_cmdPExpireTime = 44;
RedisParser.RULE_cmdTtl = 45;
RedisParser.RULE_cmdPttl = 46;
RedisParser.RULE_cmdRandomkey = 47;
RedisParser.RULE_cmdRename = 48;
RedisParser.RULE_cmdRenamenx = 49;
RedisParser.RULE_cmdRestore = 50;
RedisParser.RULE_cmdScan = 51;
RedisParser.RULE_cmdSort = 52;
RedisParser.RULE_cmdSortro = 53;
RedisParser.RULE_cmdTouch = 54;
RedisParser.RULE_cmdType = 55;
RedisParser.RULE_cmdUnlink = 56;
RedisParser.RULE_cmdWait = 57;
RedisParser.RULE_cmdAppend = 58;
RedisParser.RULE_cmdDecr = 59;
RedisParser.RULE_cmdDecrby = 60;
RedisParser.RULE_cmdGet = 61;
RedisParser.RULE_cmdGetdel = 62;
RedisParser.RULE_cmdGetex = 63;
RedisParser.RULE_cmdGetrange = 64;
RedisParser.RULE_cmdGetset = 65;
RedisParser.RULE_cmdGetbit = 66;
RedisParser.RULE_cmdInc = 67;
RedisParser.RULE_cmdLcs = 68;
RedisParser.RULE_cmdMget = 69;
RedisParser.RULE_cmdMset = 70;
RedisParser.RULE_cmdMsetnx = 71;
RedisParser.RULE_cmdSetex = 72;
RedisParser.RULE_cmdSet = 73;
RedisParser.RULE_cmdSetnx = 74;
RedisParser.RULE_cmdSetrange = 75;
RedisParser.RULE_cmdSetbit = 76;
RedisParser.RULE_cmdStrlen = 77;
RedisParser.RULE_cmdSubstr = 78;
RedisParser.RULE_cmdHdelMget = 79;
RedisParser.RULE_cmdHexistsGetKeysStrlen = 80;
RedisParser.RULE_cmdHgetallLenVals = 81;
RedisParser.RULE_cmdHmsetHset = 82;
RedisParser.RULE_cmdHincrby = 83;
RedisParser.RULE_cmdHrandfield = 84;
RedisParser.RULE_cmdHscan = 85;
RedisParser.RULE_cmdHsetnx = 86;
RedisParser.RULE_cmdBlmove = 87;
RedisParser.RULE_cmdLmpop = 88;
RedisParser.RULE_cmdBpop = 89;
RedisParser.RULE_cmdBrpoplpush = 90;
RedisParser.RULE_cmdLindex = 91;
RedisParser.RULE_cmdLinsert = 92;
RedisParser.RULE_cmdLlen = 93;
RedisParser.RULE_cmdLmove = 94;
RedisParser.RULE_cmdPop = 95;
RedisParser.RULE_cmdLpos = 96;
RedisParser.RULE_cmdPush = 97;
RedisParser.RULE_cmdLrangeTrim = 98;
RedisParser.RULE_cmdLremSet = 99;
RedisParser.RULE_cmdRpoplpush = 100;
RedisParser.RULE_cmdSadd = 101;
RedisParser.RULE_cmdScard = 102;
RedisParser.RULE_cmdSdiff = 103;
RedisParser.RULE_cmdSdiffstore = 104;
RedisParser.RULE_cmdSintercard = 105;
RedisParser.RULE_cmdSismember = 106;
RedisParser.RULE_cmdSmove = 107;
RedisParser.RULE_cmdSpop = 108;
RedisParser.RULE_cmdSscan = 109;
RedisParser.RULE_cmdBzmpop = 110;
RedisParser.RULE_cmdBzpopmax = 111;
RedisParser.RULE_cmdZadd = 112;
RedisParser.RULE_cmdZcard = 113;
RedisParser.RULE_cmdZcount = 114;
RedisParser.RULE_cmdZdiff = 115;
RedisParser.RULE_cmdZdiffstore = 116;
RedisParser.RULE_cmdZincrby = 117;
RedisParser.RULE_cmdZinter = 118;
RedisParser.RULE_cmdZintercard = 119;
RedisParser.RULE_cmdZinterstore = 120;
RedisParser.RULE_cmdZmpop = 121;
RedisParser.RULE_cmdZmscore = 122;
RedisParser.RULE_cmdZpopmax = 123;
RedisParser.RULE_cmdZrandmember = 124;
RedisParser.RULE_cmdZrange = 125;
RedisParser.RULE_cmdZrangebylex = 126;
RedisParser.RULE_cmdZrangebyscore = 127;
RedisParser.RULE_cmdZrangestore = 128;
RedisParser.RULE_cmdZrank = 129;
RedisParser.RULE_cmdZremrangebyrank = 130;
RedisParser.RULE_cmdZrevrange = 131;
RedisParser.RULE_cmdZrevrangebylex = 132;
RedisParser.RULE_cmdZscan = 133;
RedisParser.RULE_cmdZunion = 134;
RedisParser.RULE_cmdZunionstore = 135;
RedisParser.RULE_cmdScriptKill = 136;
RedisParser.RULE_cmdScriptLoad = 137;
RedisParser.RULE_cmdEval = 138;
RedisParser.RULE_cmdEvalsha = 139;
RedisParser.RULE_cmdScriptExists = 140;
RedisParser.RULE_cmdScriptFlush = 141;
RedisParser.RULE_cmdExec = 142;
RedisParser.RULE_cmdWatch = 143;
RedisParser.RULE_cmdDiscard = 144;
RedisParser.RULE_cmdUnwatch = 145;
RedisParser.RULE_cmdMulti = 146;
RedisParser.RULE_cmdPfmerge = 147;
RedisParser.RULE_cmdPfadd = 148;
RedisParser.RULE_cmdPfcount = 149;
RedisParser.RULE_cmdSsubscribe = 150;
RedisParser.RULE_cmdUnsubscribe = 151;
RedisParser.RULE_cmdPusubnumpat = 152;
RedisParser.RULE_cmdPusubnumsub = 153;
RedisParser.RULE_cmdPusubchannels = 154;
RedisParser.RULE_cmdPublish = 155;
RedisParser.RULE_cmdAsking = 156;
RedisParser.RULE_cmdReadonly = 157;
RedisParser.RULE_cmdReadwrite = 158;
RedisParser.RULE_cmdAddDelSlots = 159;
RedisParser.RULE_cmdCountKeysInSlot = 160;
RedisParser.RULE_cmdFailOver = 161;
RedisParser.RULE_cmdForget = 162;
RedisParser.RULE_cmdGetKeysInSlot = 163;
RedisParser.RULE_cmdClusterOrder = 164;
RedisParser.RULE_cmdKeySlot = 165;
RedisParser.RULE_cmdMeet = 166;
RedisParser.RULE_cmdReplicaesSlave = 167;
RedisParser.RULE_cmdReset = 168;
RedisParser.RULE_cmdSetSlot = 169;
RedisParser.RULE_cmdFlushdb = 170;
RedisParser.RULE_cmdEcho = 171;
RedisParser.RULE_cmdSave = 172;
RedisParser.RULE_cmdSlowlog = 173;
RedisParser.RULE_cmdLastsave = 174;
RedisParser.RULE_cmdConfig = 175;
RedisParser.RULE_cmdClient = 176;
RedisParser.RULE_cmdShutdown = 177;
RedisParser.RULE_cmdSync = 178;
RedisParser.RULE_cmdRole = 179;
RedisParser.RULE_cmdMonitor = 180;
RedisParser.RULE_cmdSlaveof = 181;
RedisParser.RULE_cmdFlushall = 182;
RedisParser.RULE_cmdSelect = 183;
RedisParser.RULE_cmdPing = 184;
RedisParser.RULE_cmdQuit = 185;
RedisParser.RULE_cmdAuth = 186;
RedisParser.RULE_cmdDbsize = 187;
RedisParser.RULE_cmdBgrewriteaof = 188;
RedisParser.RULE_cmdTime = 189;
RedisParser.RULE_cmdInfo = 190;
RedisParser.RULE_cmdBgsave = 191;
RedisParser.RULE_cmdCmd = 192;
RedisParser.RULE_cmdCmdx = 193;
RedisParser.RULE_cmdDebug = 194;
RedisParser.RULE_sqlStatement = 195;
// tslint:disable:no-trailing-whitespace
RedisParser.ruleNames = [
    "program", "sqlStatements", "emptyStatement_", "keyName", "fieldName",
    "stringValue", "intValue", "floatValue", "pattern", "cursor", "min", "max",
    "multiString", "cout", "start", "end", "port", "keyAndString", "intAndString",
    "infoOpt", "patternOpt", "keyOpt", "idletimeOpt", "freqOpt", "limitOpt",
    "sortOpt", "getsetOpt", "lrOpt", "rankOpt", "countOpt", "maxlenOpt", "cmdCopy",
    "cmdDel", "cmdDump", "cmdExists", "cmdExpire", "cmdExpireat", "cmdExpireTime",
    "cmdKeys", "cmdMove", "cmdObject", "cmdPersist", "cmdPexpire", "cmdPexpireat",
    "cmdPExpireTime", "cmdTtl", "cmdPttl", "cmdRandomkey", "cmdRename", "cmdRenamenx",
    "cmdRestore", "cmdScan", "cmdSort", "cmdSortro", "cmdTouch", "cmdType",
    "cmdUnlink", "cmdWait", "cmdAppend", "cmdDecr", "cmdDecrby", "cmdGet",
    "cmdGetdel", "cmdGetex", "cmdGetrange", "cmdGetset", "cmdGetbit", "cmdInc",
    "cmdLcs", "cmdMget", "cmdMset", "cmdMsetnx", "cmdSetex", "cmdSet", "cmdSetnx",
    "cmdSetrange", "cmdSetbit", "cmdStrlen", "cmdSubstr", "cmdHdelMget", "cmdHexistsGetKeysStrlen",
    "cmdHgetallLenVals", "cmdHmsetHset", "cmdHincrby", "cmdHrandfield", "cmdHscan",
    "cmdHsetnx", "cmdBlmove", "cmdLmpop", "cmdBpop", "cmdBrpoplpush", "cmdLindex",
    "cmdLinsert", "cmdLlen", "cmdLmove", "cmdPop", "cmdLpos", "cmdPush", "cmdLrangeTrim",
    "cmdLremSet", "cmdRpoplpush", "cmdSadd", "cmdScard", "cmdSdiff", "cmdSdiffstore",
    "cmdSintercard", "cmdSismember", "cmdSmove", "cmdSpop", "cmdSscan", "cmdBzmpop",
    "cmdBzpopmax", "cmdZadd", "cmdZcard", "cmdZcount", "cmdZdiff", "cmdZdiffstore",
    "cmdZincrby", "cmdZinter", "cmdZintercard", "cmdZinterstore", "cmdZmpop",
    "cmdZmscore", "cmdZpopmax", "cmdZrandmember", "cmdZrange", "cmdZrangebylex",
    "cmdZrangebyscore", "cmdZrangestore", "cmdZrank", "cmdZremrangebyrank",
    "cmdZrevrange", "cmdZrevrangebylex", "cmdZscan", "cmdZunion", "cmdZunionstore",
    "cmdScriptKill", "cmdScriptLoad", "cmdEval", "cmdEvalsha", "cmdScriptExists",
    "cmdScriptFlush", "cmdExec", "cmdWatch", "cmdDiscard", "cmdUnwatch", "cmdMulti",
    "cmdPfmerge", "cmdPfadd", "cmdPfcount", "cmdSsubscribe", "cmdUnsubscribe",
    "cmdPusubnumpat", "cmdPusubnumsub", "cmdPusubchannels", "cmdPublish",
    "cmdAsking", "cmdReadonly", "cmdReadwrite", "cmdAddDelSlots", "cmdCountKeysInSlot",
    "cmdFailOver", "cmdForget", "cmdGetKeysInSlot", "cmdClusterOrder", "cmdKeySlot",
    "cmdMeet", "cmdReplicaesSlave", "cmdReset", "cmdSetSlot", "cmdFlushdb",
    "cmdEcho", "cmdSave", "cmdSlowlog", "cmdLastsave", "cmdConfig", "cmdClient",
    "cmdShutdown", "cmdSync", "cmdRole", "cmdMonitor", "cmdSlaveof", "cmdFlushall",
    "cmdSelect", "cmdPing", "cmdQuit", "cmdAuth", "cmdDbsize", "cmdBgrewriteaof",
    "cmdTime", "cmdInfo", "cmdBgsave", "cmdCmd", "cmdCmdx", "cmdDebug", "sqlStatement",
];
RedisParser._LITERAL_NAMES = [
    undefined, undefined, undefined, undefined, undefined, "'COPY'", "'DEL'",
    "'DUMP'", "'EXISTS'", "'EXPIRE'", "'EXPIREAT'", "'EXPIRETIME'", "'KEYS'",
    "'MOVE'", "'OBJECT'", "'PERSIST'", "'PEXPIRE'", "'PEXPIREAT'", "'PEXPIRETIME'",
    "'TTL'", "'PTTL'", "'RANDOMKEY'", "'RENAME'", "'RENAMENX'", "'RESTORE'",
    "'SCAN'", "'SORT'", "'SORT_RO'", "'TOUCH'", "'TYPE'", "'UNLINK'", "'WAIT'",
    "'APPEND'", "'DECR'", "'DECRBY'", "'GET'", "'GETDEL'", "'GETEX'", "'GETRANGE'",
    "'GETSET'", "'GETBIT'", "'INCR'", "'INCRBY'", "'INCRBYFLOAT'", "'LCS'",
    "'MGET'", "'MSET'", "'MSETNX'", "'SETEX'", "'PSETEX'", "'SET'", "'SETNX'",
    "'SETRANGE'", "'SETBIT'", "'STRLEN'", "'SUBSTR'", "'HDEL'", "'HEXISTS'",
    "'HGET'", "'HGETALL'", "'HINCRBY'", "'HINCRBYFLOAT'", "'HKEYS'", "'HLEN'",
    "'HMGET'", "'HMSET'", "'HRANDFIELD'", "'HSCAN'", "'HSET'", "'HSETNX'",
    "'HSTRLEN'", "'HVALS'", "'BLMOVE'", "'BLMPOP'", "'BLPOP'", "'BRPOP'",
    "'BRPOPLPUSH'", "'LINDEX'", "'LINSERT'", "'LLEN'", "'LMOVE'", "'LMPOP'",
    "'LPOP'", "'LPOS'", "'LPUSH'", "'LPUSHX'", "'LRANGE'", "'LREM'", "'LSET'",
    "'LTRIM'", "'RPOP'", "'RPOPLPUSH'", "'RPUSH'", "'RPUSHX'", "'SADD'", "'SCARD'",
    "'SDIFF'", "'SDIFFSTORE'", "'SINTER'", "'SINTERCARD'", "'SINTERSTORE'",
    "'SISMEMBER'", "'SMEMBERS'", "'SMISMEMBER'", "'SMOVE'", "'SPOP'", "'SRANDMEMBER'",
    "'SREM'", "'SSCAN'", "'SUNION'", "'SUNIONSTORE'", "'BZMPOP'", "'BZPOPMAX'",
    "'BZPOPMIN'", "'ZADD'", "'ZCARD'", "'ZCOUNT'", "'ZDIFF'", "'ZDIFFSTORE'",
    "'ZINCRBY'", "'ZINTER'", "'ZINTERCARD'", "'ZINTERSTORE'", "'ZLEXCOUNT'",
    "'ZMPOP'", "'ZMSCORE'", "'ZPOPMAX'", "'ZPOPMIN'", "'ZRANDMEMBER'", "'ZRANGE'",
    "'ZRANGEBYLEX'", "'ZRANGEBYSCORE'", "'ZRANGESTORE'", "'ZRANK'", "'ZREM'",
    "'ZREMRANGEBYLEX'", "'ZREMRANGEBYRANK'", "'ZREMRANGEBYSCORE'", "'ZREVRANGE'",
    "'ZREVRANGEBYLEX'", "'ZREVRANGEBYSCORE'", "'ZREVRANK'", "'ZSCAN'", "'ZSCORE'",
    "'ZUNION'", "'ZUNIONSTORE'", "'PSUBSCRIBE'", "'PUBSUB'", "'PUBLISH'",
    "'CHANNELS'", "'NUMSUB'", "'NUMPAT'", "'SHARDCHANNELS'", "'SHARDNUMSUB'",
    "'PUNSUBSCRIBE'", "'SPUBLISH'", "'SUBSCRIBE'", "'UNSUBSCRIBE'", "'SSUBSCRIBE'",
    "'SUNSUBSCRIBE'", "'SERVER'", "'CLIENTS'", "'MEMORY'", "'PERSISTENCE'",
    "'STATS'", "'REPLICATION'", "'CPU'", "'COMMANDSTATS'", "'LATENCYSTATS'",
    "'SENTINEL'", "'MODULES'", "'KEYSPACE'", "'ERRORSTATS'", "'ALL'", "'DEFAULT'",
    "'EVERYTHING'", "'ASKING'", "'CLUSTER'", "'ADDSLOTS'", "'DELSLOTS'", "'ADDSLOTSRANGE'",
    "'BUMPEPOCH'", "'COUNTKEYSINSLOT'", "'DELSLOTSRANGE'", "'FAILOVER'", "'FLUSHSLOTS'",
    "'FORGET'", "'GETKEYSINSLOT'", "'KEYSLOT'", "'LINKS'", "'MEET'", "'MYID'",
    "'MYSHARDID'", "'FORCE'", "'TAKEOVER'", "'NODE'", "'NODES'", "'REPLICAS'",
    "'REPLICATE'", "'RESET'", "'HARD'", "'SOFT'", "'SAVECONFIG'", "'SETSLOT'",
    "'IMPORTING'", "'MIGRATING'", "'STABLE'", "'SHARDS'", "'SLAVES'", "'READONLY'",
    "'READWRITE'", "'FLUSHDB'", "'SCRIPT'", "'EVAL'", "'EVALSHA'", "'EXEC'",
    "'WATCH'", "'DISCARD'", "'UNWATCH'", "'MULTI'", "'PFMERGE'", "'PFADD'",
    "'PFCOUNT'", "'ECHO'", "'PING'", "'SAVE'", "'SLOWLOG'", "'LASTSAVE'",
    "'CONFIG'", "'CLIENT'", "'SHUTDOWN'", "'SYNC'", "'ROLE'", "'MONITOR'",
    "'SLAVEOF'", "'FLUSHALL'", "'SELECT'", "'QUIT'", "'AUTH'", "'DBSIZE'",
    "'BGREWRITEAOF'", "'TIME'", "'INFO'", "'BGSAVE'", "'COMMAND'", "'DEBUG'",
    "'DB'", "'NX'", "'XX'", "'GT'", "'LT'", "'ENCODING'", "'FREQ'", "'IDLETIME'",
    "'REFCOUNT'", "'ABSTTL'", "'BY'", "'ASC'", "'DESC'", "'ALPHA'", "'STORE'",
    "'REPLACE'", "'EX'", "'PX'", "'EXAT'", "'PXAT'", "'LEN'", "'IDX'", "'MINMATCHLEN'",
    "'WITHMATCHLEN'", "'KEEPTTL'", "'WITHSCORES'", "'LIMIT'", "'BEFORE'",
    "'AFTER'", "'FLUSH'", "'RESETSTAT'", "'REWRITE'", "'PAUSE'", "'SETNAME'",
    "'GETNAME'", "'LIST'", "'NOSAVE'", "'SLOTS'", "'GETKEYS'", "'COUNT'",
    "'SEGFAULT'", "'KILL'", "'LOAD'", "'WITHVALUES'", "'MATCH'", "'MIN'",
    "'MAX'", "'CH'", "'WEIGHTS'", "'AGGREGATE'", "'SUM'", "'BYSCORE'", "'BYLEX'",
    "'REV'", "'LEFT'", "'RIGHT'", "'RANK'", "'MAXLEN'", "'ASYNC'",
];
RedisParser._SYMBOLIC_NAMES = [
    undefined, "SPACE", "SPEC_MYSQL_COMMENT", "COMMENT_INPUT", "LINE_COMMENT",
    "COPY", "DEL", "DUMP", "EXISTS", "EXPIRE", "EXPIREAT", "EXPIRETIME", "KEYS",
    "MOVE", "OBJECT", "PERSIST", "PEXPIRE", "PEXPIREAT", "PEXPIRETIME", "TTL",
    "PTTL", "RANDOMKEY", "RENAME", "RENAMENX", "RESTORE", "SCAN", "SORT",
    "SORT_RO", "TOUCH", "TYPE", "UNLINK", "WAIT", "APPEND", "DECR", "DECRBY",
    "GET", "GETDEL", "GETEX", "GETRANGE", "GETSET", "GETBIT", "INCR", "INCRBY",
    "INCRBYFLOAT", "LCS", "MGET", "MSET", "MSETNX", "SETEX", "PSETEX", "SET",
    "SETNX", "SETRANGE", "SETBIT", "STRLEN", "SUBSTR", "HDEL", "HEXISTS",
    "HGET", "HGETALL", "HINCRBY", "HINCRBYFLOAT", "HKEYS", "HLEN", "HMGET",
    "HMSET", "HRANDFIELD", "HSCAN", "HSET", "HSETNX", "HSTRLEN", "HVALS",
    "BLMOVE", "BLMPOP", "BLPOP", "BRPOP", "BRPOPLPUSH", "LINDEX", "LINSERT",
    "LLEN", "LMOVE", "LMPOP", "LPOP", "LPOS", "LPUSH", "LPUSHX", "LRANGE",
    "LREM", "LSET", "LTRIM", "RPOP", "RPOPLPUSH", "RPUSH", "RPUSHX", "SADD",
    "SCARD", "SDIFF", "SDIFFSTORE", "SINTER", "SINTERCARD", "SINTERSTORE",
    "SISMEMBER", "SMEMBERS", "SMISMEMBER", "SMOVE", "SPOP", "SRANDMEMBER",
    "SREM", "SSCAN", "SUNION", "SUNIONSTORE", "BZMPOP", "BZPOPMAX", "BZPOPMIN",
    "ZADD", "ZCARD", "ZCOUNT", "ZDIFF", "ZDIFFSTORE", "ZINCRBY", "ZINTER",
    "ZINTERCARD", "ZINTERSTORE", "ZLEXCOUNT", "ZMPOP", "ZMSCORE", "ZPOPMAX",
    "ZPOPMIN", "ZRANDMEMBER", "ZRANGE", "ZRANGEBYLEX", "ZRANGEBYSCORE", "ZRANGESTORE",
    "ZRANK", "ZREM", "ZREMRANGEBYLEX", "ZREMRANGEBYRANK", "ZREMRANGEBYSCORE",
    "ZREVRANGE", "ZREVRANGEBYLEX", "ZREVRANGEBYSCORE", "ZREVRANK", "ZSCAN",
    "ZSCORE", "ZUNION", "ZUNIONSTORE", "PSUBSCRIBE", "PUBSUB", "PUBLISH",
    "CHANNELS", "NUMSUB", "NUMPAT", "SHARDCHANNELS", "SHARDNUMSUB", "PUNSUBSCRIBE",
    "SPUBLISH", "SUBSCRIBE", "UNSUBSCRIBE", "SSUBSCRIBE", "SUNSUBSCRIBE",
    "SERVER", "CLIENTS", "MEMORY", "PERSISTENCE", "STATS", "REPLICATION",
    "CPU", "COMMANDSTATS", "LATENCYSTATS", "SENTINEL", "MODULES", "KEYSPACE",
    "ERRORSTATS", "ALL", "DEFAULT", "EVERYTHING", "ASKING", "CLUSTER", "ADDSLOTS",
    "DELSLOTS", "ADDSLOTSRANGE", "BUMPEPOCH", "COUNTKEYSINSLOT", "DELSLOTSRANGE",
    "FAILOVER", "FLUSHSLOTS", "FORGET", "GETKEYSINSLOT", "KEYSLOT", "LINKS",
    "MEET", "MYID", "MYSHARDID", "FORCE", "TAKEOVER", "NODE", "NODES", "REPLICAS",
    "REPLICATE", "RESET", "HARD", "SOFT", "SAVECONFIG", "SETSLOT", "IMPORTING",
    "MIGRATING", "STABLE", "SHARDS", "SLAVES", "READONLY", "READWRITE", "FLUSHDB",
    "SCRIPT", "EVAL", "EVALSHA", "EXEC", "WATCH", "DISCARD", "UNWATCH", "MULTI",
    "PFMERGE", "PFADD", "PFCOUNT", "ECHO", "PING", "SAVE", "SLOWLOG", "LASTSAVE",
    "CONFIG", "CLIENT", "SHUTDOWN", "SYNC", "ROLE", "MONITOR", "SLAVEOF",
    "FLUSHALL", "SELECT", "QUIT", "AUTH", "DBSIZE", "BGREWRITEAOF", "TIME",
    "INFO", "BGSAVE", "COMMAND", "DEBUG", "DB", "NX", "XX", "GT", "LT", "ENCODING",
    "FREQ", "IDLETIME", "REFCOUNT", "ABSTTL", "BY", "ASC", "DESC", "ALPHA",
    "STORE", "REPLACE", "EX", "PX", "EXAT", "PXAT", "LEN", "IDX", "MINMATCHLEN",
    "WITHMATCHLEN", "KEEPTTL", "WITHSCORES", "LIMIT", "BEFORE", "AFTER", "FLUSH",
    "RESETSTAT", "REWRITE", "PAUSE", "SETNAME", "GETNAME", "LIST", "NOSAVE",
    "SLOTS", "GETKEYS", "COUNT", "SEGFAULT", "KILL", "LOAD", "WITHVALUES",
    "MATCH", "MIN", "MAX", "CH", "WEIGHTS", "AGGREGATE", "SUM", "BYSCORE",
    "BYLEX", "REV", "LEFT", "RIGHT", "RANK", "MAXLEN", "ASYNC", "HEX_NUM",
    "OCT_NUM", "BIT_NUM", "INTEGER_NUM", "DECIMAL_NUM", "STRING", "HOST",
    "HOST_PORT", "NAME_TOKEN", "SEMI",
];
RedisParser.VOCABULARY = new VocabularyImpl(RedisParser._LITERAL_NAMES, RedisParser._SYMBOLIC_NAMES, []);
RedisParser._serializedATNSegments = 4;
RedisParser._serializedATNSegment0 = "\x03\uC91D\uCABA\u058D\uAFBA\u4F53\u0607\uEA8B\uC241\x03\u013C\u069F\x04" +
    "\x02\t\x02\x04\x03\t\x03\x04\x04\t\x04\x04\x05\t\x05\x04\x06\t\x06\x04" +
    "\x07\t\x07\x04\b\t\b\x04\t\t\t\x04\n\t\n\x04\v\t\v\x04\f\t\f\x04\r\t\r" +
    "\x04\x0E\t\x0E\x04\x0F\t\x0F\x04\x10\t\x10\x04\x11\t\x11\x04\x12\t\x12" +
    "\x04\x13\t\x13\x04\x14\t\x14\x04\x15\t\x15\x04\x16\t\x16\x04\x17\t\x17" +
    "\x04\x18\t\x18\x04\x19\t\x19\x04\x1A\t\x1A\x04\x1B\t\x1B\x04\x1C\t\x1C" +
    "\x04\x1D\t\x1D\x04\x1E\t\x1E\x04\x1F\t\x1F\x04 \t \x04!\t!\x04\"\t\"\x04" +
    "#\t#\x04$\t$\x04%\t%\x04&\t&\x04\'\t\'\x04(\t(\x04)\t)\x04*\t*\x04+\t" +
    "+\x04,\t,\x04-\t-\x04.\t.\x04/\t/\x040\t0\x041\t1\x042\t2\x043\t3\x04" +
    "4\t4\x045\t5\x046\t6\x047\t7\x048\t8\x049\t9\x04:\t:\x04;\t;\x04<\t<\x04" +
    "=\t=\x04>\t>\x04?\t?\x04@\t@\x04A\tA\x04B\tB\x04C\tC\x04D\tD\x04E\tE\x04" +
    "F\tF\x04G\tG\x04H\tH\x04I\tI\x04J\tJ\x04K\tK\x04L\tL\x04M\tM\x04N\tN\x04" +
    "O\tO\x04P\tP\x04Q\tQ\x04R\tR\x04S\tS\x04T\tT\x04U\tU\x04V\tV\x04W\tW\x04" +
    "X\tX\x04Y\tY\x04Z\tZ\x04[\t[\x04\\\t\\\x04]\t]\x04^\t^\x04_\t_\x04`\t" +
    "`\x04a\ta\x04b\tb\x04c\tc\x04d\td\x04e\te\x04f\tf\x04g\tg\x04h\th\x04" +
    "i\ti\x04j\tj\x04k\tk\x04l\tl\x04m\tm\x04n\tn\x04o\to\x04p\tp\x04q\tq\x04" +
    "r\tr\x04s\ts\x04t\tt\x04u\tu\x04v\tv\x04w\tw\x04x\tx\x04y\ty\x04z\tz\x04" +
    "{\t{\x04|\t|\x04}\t}\x04~\t~\x04\x7F\t\x7F\x04\x80\t\x80\x04\x81\t\x81" +
    "\x04\x82\t\x82\x04\x83\t\x83\x04\x84\t\x84\x04\x85\t\x85\x04\x86\t\x86" +
    "\x04\x87\t\x87\x04\x88\t\x88\x04\x89\t\x89\x04\x8A\t\x8A\x04\x8B\t\x8B" +
    "\x04\x8C\t\x8C\x04\x8D\t\x8D\x04\x8E\t\x8E\x04\x8F\t\x8F\x04\x90\t\x90" +
    "\x04\x91\t\x91\x04\x92\t\x92\x04\x93\t\x93\x04\x94\t\x94\x04\x95\t\x95" +
    "\x04\x96\t\x96\x04\x97\t\x97\x04\x98\t\x98\x04\x99\t\x99\x04\x9A\t\x9A" +
    "\x04\x9B\t\x9B\x04\x9C\t\x9C\x04\x9D\t\x9D\x04\x9E\t\x9E\x04\x9F\t\x9F" +
    "\x04\xA0\t\xA0\x04\xA1\t\xA1\x04\xA2\t\xA2\x04\xA3\t\xA3\x04\xA4\t\xA4" +
    "\x04\xA5\t\xA5\x04\xA6\t\xA6\x04\xA7\t\xA7\x04\xA8\t\xA8\x04\xA9\t\xA9" +
    "\x04\xAA\t\xAA\x04\xAB\t\xAB\x04\xAC\t\xAC\x04\xAD\t\xAD\x04\xAE\t\xAE" +
    "\x04\xAF\t\xAF\x04\xB0\t\xB0\x04\xB1\t\xB1\x04\xB2\t\xB2\x04\xB3\t\xB3" +
    "\x04\xB4\t\xB4\x04\xB5\t\xB5\x04\xB6\t\xB6\x04\xB7\t\xB7\x04\xB8\t\xB8" +
    "\x04\xB9\t\xB9\x04\xBA\t\xBA\x04\xBB\t\xBB\x04\xBC\t\xBC\x04\xBD\t\xBD" +
    "\x04\xBE\t\xBE\x04\xBF\t\xBF\x04\xC0\t\xC0\x04\xC1\t\xC1\x04\xC2\t\xC2" +
    "\x04\xC3\t\xC3\x04\xC4\t\xC4\x04\xC5\t\xC5\x03\x02\x05\x02\u018C\n\x02" +
    "\x03\x02\x03\x02\x03\x03\x03\x03\x07\x03\u0192\n\x03\f\x03\x0E\x03\u0195" +
    "\v\x03\x03\x03\x03\x03\x05\x03\u0199\n\x03\x03\x03\x05\x03\u019C\n\x03" +
    "\x03\x04\x03\x04\x03\x05\x03\x05\x03\x06\x03\x06\x03\x07\x03\x07\x03\b" +
    "\x03\b\x03\t\x03\t\x03\n\x03\n\x03\v\x03\v\x03\f\x03\f\x03\r\x03\r\x03" +
    "\x0E\x03\x0E\x03\x0F\x03\x0F\x03\x10\x03\x10\x03\x11\x03\x11\x03\x12\x03" +
    "\x12\x03\x13\x03\x13\x03\x13\x03\x14\x03\x14\x03\x14\x03\x15\x03\x15\x03" +
    "\x16\x03\x16\x03\x16\x03\x17\x03\x17\x03\x18\x03\x18\x03\x18\x03\x19\x03" +
    "\x19\x03\x19\x03\x1A\x03\x1A\x03\x1A\x03\x1A\x03\x1B\x03\x1B\x03\x1C\x03" +
    "\x1C\x03\x1C\x03\x1D\x03\x1D\x03\x1E\x03\x1E\x03\x1E\x03\x1F\x03\x1F\x03" +
    "\x1F\x03 \x03 \x03 \x03!\x03!\x03!\x03!\x03!\x05!\u01E8\n!\x03!\x05!\u01EB" +
    "\n!\x03\"\x03\"\x06\"\u01EF\n\"\r\"\x0E\"\u01F0\x03#\x03#\x03#\x03$\x03" +
    "$\x06$\u01F8\n$\r$\x0E$\u01F9\x03%\x03%\x03%\x03%\x05%\u0200\n%\x03&\x03" +
    "&\x03&\x03&\x05&\u0206\n&\x03\'\x03\'\x03\'\x03(\x03(\x03(\x03)\x03)\x03" +
    ")\x03)\x03*\x03*\x03*\x03*\x03+\x03+\x03+\x03,\x03,\x03,\x03,\x05,\u021D" +
    "\n,\x03-\x03-\x03-\x03-\x05-\u0223\n-\x03.\x03.\x03.\x03/\x03/\x03/\x03" +
    "0\x030\x030\x031\x031\x032\x032\x032\x032\x033\x033\x033\x033\x034\x03" +
    "4\x034\x034\x034\x054\u023D\n4\x034\x054\u0240\n4\x034\x054\u0243\n4\x03" +
    "4\x054\u0246\n4\x035\x035\x035\x035\x055\u024C\n5\x035\x035\x055\u0250" +
    "\n5\x036\x036\x036\x036\x056\u0256\n6\x036\x056\u0259\n6\x036\x066\u025C" +
    "\n6\r6\x0E6\u025D\x056\u0260\n6\x036\x056\u0263\n6\x036\x056\u0266\n6" +
    "\x036\x036\x056\u026A\n6\x037\x037\x037\x037\x057\u0270\n7\x037\x057\u0273" +
    "\n7\x037\x067\u0276\n7\r7\x0E7\u0277\x057\u027A\n7\x037\x057\u027D\n7" +
    "\x037\x057\u0280\n7\x038\x038\x068\u0284\n8\r8\x0E8\u0285\x039\x039\x03" +
    "9\x03:\x03:\x06:\u028D\n:\r:\x0E:\u028E\x03;\x03;\x03;\x03;\x03<\x03<" +
    "\x03<\x03<\x03=\x03=\x03=\x03>\x03>\x03>\x03>\x03?\x03?\x03?\x03@\x03" +
    "@\x03@\x03A\x03A\x03A\x03A\x05A\u02AA\nA\x03B\x03B\x03B\x03B\x03B\x03" +
    "C\x03C\x03C\x03C\x03D\x03D\x03D\x03D\x03E\x03E\x03E\x03E\x03E\x03E\x03" +
    "E\x03E\x03E\x03E\x05E\u02C3\nE\x03F\x03F\x03F\x03F\x05F\u02C9\nF\x03F" +
    "\x05F\u02CC\nF\x03F\x03F\x05F\u02D0\nF\x03F\x05F\u02D3\nF\x03G\x03G\x06" +
    "G\u02D7\nG\rG\x0EG\u02D8\x03H\x03H\x06H\u02DD\nH\rH\x0EH\u02DE\x03I\x03" +
    "I\x06I\u02E3\nI\rI\x0EI\u02E4\x03J\x03J\x03J\x03J\x03J\x03K\x03K\x03K" +
    "\x03K\x03K\x05K\u02F1\nK\x03K\x05K\u02F4\nK\x03K\x05K\u02F7\nK\x03L\x03" +
    "L\x03L\x03L\x03M\x03M\x03M\x03M\x03M\x03N\x03N\x03N\x03N\x03N\x03O\x03" +
    "O\x03O\x03P\x03P\x03P\x03P\x03P\x03Q\x03Q\x03Q\x06Q\u0312\nQ\rQ\x0EQ\u0313" +
    "\x03R\x03R\x03R\x03R\x03S\x03S\x03S\x03T\x03T\x03T\x06T\u0320\nT\rT\x0E" +
    "T\u0321\x03U\x03U\x03U\x03U\x03U\x03V\x03V\x03V\x03V\x05V\u032D\nV\x05" +
    "V\u032F\nV\x03W\x03W\x03W\x03W\x03W\x05W\u0336\nW\x03W\x03W\x05W\u033A" +
    "\nW\x03X\x03X\x03X\x03X\x03Y\x03Y\x03Y\x03Y\x03Y\x03Y\x03Y\x03Z\x03Z\x03" +
    "Z\x03Z\x06Z\u034B\nZ\rZ\x0EZ\u034C\x03Z\x03Z\x05Z\u0351\nZ\x03[\x03[\x06" +
    "[\u0355\n[\r[\x0E[\u0356\x03[\x03[\x03\\\x03\\\x03\\\x03\\\x03\\\x03]" +
    "\x03]\x03]\x03]\x03^\x03^\x03^\x03^\x03^\x03^\x03_\x03_\x03_\x03`\x03" +
    "`\x03`\x03`\x03`\x03`\x03a\x03a\x03a\x05a\u0376\na\x03b\x03b\x03b\x03" +
    "b\x05b\u037C\nb\x03b\x05b\u037F\nb\x03b\x05b\u0382\nb\x03c\x03c\x03c\x06" +
    "c\u0387\nc\rc\x0Ec\u0388\x03d\x03d\x03d\x03d\x03d\x03e\x03e\x03e\x03e" +
    "\x03e\x03f\x03f\x03f\x03f\x03g\x03g\x03g\x06g\u039C\ng\rg\x0Eg\u039D\x03" +
    "h\x03h\x03h\x03i\x03i\x06i\u03A5\ni\ri\x0Ei\u03A6\x03j\x03j\x03j\x06j" +
    "\u03AC\nj\rj\x0Ej\u03AD\x03k\x03k\x03k\x06k\u03B3\nk\rk\x0Ek\u03B4\x03" +
    "k\x03k\x05k\u03B9\nk\x03l\x03l\x03l\x03l\x03m\x03m\x03m\x03m\x03m\x03" +
    "n\x03n\x03n\x05n\u03C7\nn\x03o\x03o\x03o\x03o\x03o\x05o\u03CE\no\x03o" +
    "\x05o\u03D1\no\x03p\x03p\x03p\x03p\x06p\u03D7\np\rp\x0Ep\u03D8\x03p\x03" +
    "p\x05p\u03DD\np\x03q\x03q\x06q\u03E1\nq\rq\x0Eq\u03E2\x03q\x03q\x03r\x03" +
    "r\x03r\x05r\u03EA\nr\x03r\x05r\u03ED\nr\x03r\x05r\u03F0\nr\x03r\x05r\u03F3" +
    "\nr\x03r\x06r\u03F6\nr\rr\x0Er\u03F7\x03s\x03s\x03s\x03t\x03t\x03t\x03" +
    "t\x03t\x03u\x03u\x03u\x06u\u0405\nu\ru\x0Eu\u0406\x03u\x05u\u040A\nu\x03" +
    "v\x03v\x03v\x03v\x06v\u0410\nv\rv\x0Ev\u0411\x03w\x03w\x03w\x03w\x03w" +
    "\x03x\x03x\x03x\x06x\u041C\nx\rx\x0Ex\u041D\x03x\x03x\x06x\u0422\nx\r" +
    "x\x0Ex\u0423\x05x\u0426\nx\x03x\x03x\x05x\u042A\nx\x03x\x05x\u042D\nx" +
    "\x03y\x03y\x03y\x06y\u0432\ny\ry\x0Ey\u0433\x03y\x03y\x05y\u0438\ny\x03" +
    "z\x03z\x03z\x03z\x06z\u043E\nz\rz\x0Ez\u043F\x03z\x03z\x06z\u0444\nz\r" +
    "z\x0Ez\u0445\x05z\u0448\nz\x03z\x03z\x05z\u044C\nz\x03{\x03{\x03{\x06" +
    "{\u0451\n{\r{\x0E{\u0452\x03{\x03{\x03{\x03|\x03|\x03|\x06|\u045B\n|\r" +
    "|\x0E|\u045C\x03}\x03}\x03}\x05}\u0462\n}\x03~\x03~\x03~\x03~\x05~\u0468" +
    "\n~\x05~\u046A\n~\x03\x7F\x03\x7F\x03\x7F\x03\x7F\x03\x7F\x05\x7F\u0471" +
    "\n\x7F\x03\x7F\x05\x7F\u0474\n\x7F\x03\x7F\x05\x7F\u0477\n\x7F\x03\x7F" +
    "\x05\x7F\u047A\n\x7F\x03\x80\x03\x80\x03\x80\x03\x80\x03\x80\x05\x80\u0481" +
    "\n\x80\x03\x81\x03\x81\x03\x81\x03\x81\x03\x81\x05\x81\u0488\n\x81\x03" +
    "\x81\x05\x81\u048B\n\x81\x03\x82\x03\x82\x03\x82\x03\x82\x03\x82\x03\x82" +
    "\x05\x82\u0493\n\x82\x03\x82\x05\x82\u0496\n\x82\x03\x83\x03\x83\x03\x83" +
    "\x03\x83\x03\x84\x03\x84\x03\x84\x03\x84\x03\x84\x03\x85\x03\x85\x03\x85" +
    "\x03\x85\x03\x85\x05\x85\u04A6\n\x85\x03\x86\x03\x86\x03\x86\x03\x86\x03" +
    "\x86\x05\x86\u04AD\n\x86\x03\x86\x05\x86\u04B0\n\x86\x03\x87\x03\x87\x03" +
    "\x87\x03\x87\x03\x87\x05\x87\u04B7\n\x87\x03\x87\x03\x87\x03\x88\x03\x88" +
    "\x03\x88\x06\x88\u04BE\n\x88\r\x88\x0E\x88\u04BF\x03\x88\x03\x88\x06\x88" +
    "\u04C4\n\x88\r\x88\x0E\x88\u04C5\x05\x88\u04C8\n\x88\x03\x88\x03\x88\x05" +
    "\x88\u04CC\n\x88\x03\x88\x05\x88\u04CF\n\x88\x03\x89\x03\x89\x03\x89\x03" +
    "\x89\x06\x89\u04D5\n\x89\r\x89\x0E\x89\u04D6\x03\x89\x03\x89\x06\x89\u04DB" +
    "\n\x89\r\x89\x0E\x89\u04DC\x05\x89\u04DF\n\x89\x03\x89\x03\x89\x05\x89" +
    "\u04E3\n\x89\x03\x8A\x03\x8A\x03\x8A\x03\x8B\x03\x8B\x03\x8B\x03\x8B\x03" +
    "\x8C\x03\x8C\x03\x8C\x03\x8C\x07\x8C\u04F0\n\x8C\f\x8C\x0E\x8C\u04F3\v" +
    "\x8C\x03\x8D\x03\x8D\x03\x8D\x03\x8D\x07\x8D\u04F9\n\x8D\f\x8D\x0E\x8D" +
    "\u04FC\v\x8D\x03\x8E\x03\x8E\x03\x8E\x03\x8E\x03\x8F\x03\x8F\x03\x8F\x03" +
    "\x90\x03\x90\x03\x91\x03\x91\x06\x91\u0509\n\x91\r\x91\x0E\x91\u050A\x03" +
    "\x92\x03\x92\x03\x93\x03\x93\x03\x94\x03\x94\x03\x95\x03\x95\x03\x95\x06" +
    "\x95\u0516\n\x95\r\x95\x0E\x95\u0517\x03\x96\x03\x96\x03\x96\x06\x96\u051D" +
    "\n\x96\r\x96\x0E\x96\u051E\x03\x97\x03\x97\x03\x97\x06\x97\u0524\n\x97" +
    "\r\x97\x0E\x97\u0525\x03\x98\x03\x98\x06\x98\u052A\n\x98\r\x98\x0E\x98" +
    "\u052B\x03\x99\x03\x99\x06\x99\u0530\n\x99\r\x99\x0E\x99\u0531\x05\x99" +
    "\u0534\n\x99\x03\x9A\x03\x9A\x03\x9A\x03\x9B\x03\x9B\x03\x9B\x06\x9B\u053C" +
    "\n\x9B\r\x9B\x0E\x9B\u053D\x05\x9B\u0540\n\x9B\x03\x9C\x03\x9C\x03\x9C" +
    "\x05\x9C\u0545\n\x9C\x03\x9D\x03\x9D\x03\x9D\x03\x9D\x03\x9E\x03\x9E\x03" +
    "\x9F\x03\x9F\x03\xA0\x03\xA0\x03\xA1\x03\xA1\x03\xA1\x06\xA1\u0554\n\xA1" +
    "\r\xA1\x0E\xA1\u0555\x03\xA2\x03\xA2\x03\xA2\x03\xA2\x03\xA3\x03\xA3\x03" +
    "\xA3\x05\xA3\u055F\n\xA3\x03\xA4\x03\xA4\x03\xA4\x03\xA4\x03\xA5\x03\xA5" +
    "\x03\xA5\x03\xA5\x03\xA5\x03\xA6\x03\xA6\x03\xA6\x03\xA7\x03\xA7\x03\xA7" +
    "\x03\xA7\x03\xA8\x03\xA8\x03\xA8\x03\xA8\x03\xA8\x05\xA8\u0576\n\xA8\x03" +
    "\xA9\x03\xA9\x03\xA9\x03\xA9\x03\xAA\x03\xAA\x03\xAA\x03\xAA\x03\xAB\x03" +
    "\xAB\x03\xAB\x03\xAB\x03\xAB\x03\xAB\x03\xAB\x03\xAB\x05\xAB\u0588\n\xAB" +
    "\x03\xAB\x03\xAB\x03\xAB\x05\xAB\u058D\n\xAB\x03\xAC\x03\xAC\x05\xAC\u0591" +
    "\n\xAC\x03\xAD\x03\xAD\x03\xAD\x03\xAE\x03\xAE\x03\xAF\x03\xAF\x03\xAF" +
    "\x03\xB0\x03\xB0\x03\xB1\x03\xB1\x03\xB1\x03\xB1\x03\xB1\x03\xB1\x03\xB1" +
    "\x03\xB1\x03\xB1\x05\xB1\u05A6\n\xB1\x03\xB2\x03\xB2\x03\xB2\x03\xB2\x03" +
    "\xB2\x03\xB2\x03\xB2\x03\xB2\x03\xB2\x05\xB2\u05B1\n\xB2\x03\xB3\x03\xB3" +
    "\x05\xB3\u05B5\n\xB3\x03\xB3\x05\xB3\u05B8\n\xB3\x03\xB4\x03\xB4\x03\xB5" +
    "\x03\xB5\x03\xB6\x03\xB6\x03\xB7\x03\xB7\x03\xB7\x03\xB7\x03\xB8\x03\xB8" +
    "\x03\xB9\x03\xB9\x03\xB9\x03\xBA\x03\xBA\x03\xBB\x03\xBB\x03\xBC\x03\xBC" +
    "\x03\xBC\x03\xBD\x03\xBD\x03\xBE\x03\xBE\x03\xBF\x03\xBF\x03\xC0\x03\xC0" +
    "\x06\xC0\u05D8\n\xC0\r\xC0\x0E\xC0\u05D9\x05\xC0\u05DC\n\xC0\x03\xC1\x03" +
    "\xC1\x03\xC2\x03\xC2\x03\xC3\x03\xC3\x03\xC3\x06\xC3\u05E5\n\xC3\r\xC3" +
    "\x0E\xC3\u05E6\x03\xC3\x03\xC3\x06\xC3\u05EB\n\xC3\r\xC3\x0E\xC3\u05EC" +
    "\x03\xC3\x05\xC3\u05F0\n\xC3\x03\xC4\x03\xC4\x03\xC4\x03\xC4\x05\xC4\u05F6" +
    "\n\xC4\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x03\xC5" +
    "\x03\xC5\x03\xC5\x03\xC5\x03\xC5\x05\xC5\u069D\n\xC5\x03\xC5\x02\x02\x02" +
    "\xC6\x02\x02\x04\x02\x06\x02\b\x02\n\x02\f\x02\x0E\x02\x10\x02\x12\x02" +
    "\x14\x02\x16\x02\x18\x02\x1A\x02\x1C\x02\x1E\x02 \x02\"\x02$\x02&\x02" +
    "(\x02*\x02,\x02.\x020\x022\x024\x026\x028\x02:\x02<\x02>\x02@\x02B\x02" +
    "D\x02F\x02H\x02J\x02L\x02N\x02P\x02R\x02T\x02V\x02X\x02Z\x02\\\x02^\x02" +
    "`\x02b\x02d\x02f\x02h\x02j\x02l\x02n\x02p\x02r\x02t\x02v\x02x\x02z\x02" +
    "|\x02~\x02\x80\x02\x82\x02\x84\x02\x86\x02\x88\x02\x8A\x02\x8C\x02\x8E" +
    "\x02\x90\x02\x92\x02\x94\x02\x96\x02\x98\x02\x9A\x02\x9C\x02\x9E\x02\xA0" +
    "\x02\xA2\x02\xA4\x02\xA6\x02\xA8\x02\xAA\x02\xAC\x02\xAE\x02\xB0\x02\xB2" +
    "\x02\xB4\x02\xB6\x02\xB8\x02\xBA\x02\xBC\x02\xBE\x02\xC0\x02\xC2\x02\xC4" +
    "\x02\xC6\x02\xC8\x02\xCA\x02\xCC\x02\xCE\x02\xD0\x02\xD2\x02\xD4\x02\xD6" +
    "\x02\xD8\x02\xDA\x02\xDC\x02\xDE\x02\xE0\x02\xE2\x02\xE4\x02\xE6\x02\xE8" +
    "\x02\xEA\x02\xEC\x02\xEE\x02\xF0\x02\xF2\x02\xF4\x02\xF6\x02\xF8\x02\xFA" +
    "\x02\xFC\x02\xFE\x02\u0100\x02\u0102\x02\u0104\x02\u0106\x02\u0108\x02" +
    "\u010A\x02\u010C\x02\u010E\x02\u0110\x02\u0112\x02\u0114\x02\u0116\x02" +
    "\u0118\x02\u011A\x02\u011C\x02\u011E\x02\u0120\x02\u0122\x02\u0124\x02" +
    "\u0126\x02\u0128\x02\u012A\x02\u012C\x02\u012E\x02\u0130\x02\u0132\x02" +
    "\u0134\x02\u0136\x02\u0138\x02\u013A\x02\u013C\x02\u013E\x02\u0140\x02" +
    "\u0142\x02\u0144\x02\u0146\x02\u0148\x02\u014A\x02\u014C\x02\u014E\x02" +
    "\u0150\x02\u0152\x02\u0154\x02\u0156\x02\u0158\x02\u015A\x02\u015C\x02" +
    "\u015E\x02\u0160\x02\u0162\x02\u0164\x02\u0166\x02\u0168\x02\u016A\x02" +
    "\u016C\x02\u016E\x02\u0170\x02\u0172\x02\u0174\x02\u0176\x02\u0178\x02" +
    "\u017A\x02\u017C\x02\u017E\x02\u0180\x02\u0182\x02\u0184\x02\u0186\x02" +
    "\u0188\x02\x021\x04\x02\u0138\u0138\u013B\u013B\x03\x02\u0136\u0137\x03" +
    "\x02\xA2\xB1\x03\x02\xF9\xFC\x03\x02\u0103\u0104\x03\x02\u0108\u010B\x03" +
    "\x02\u012E\u012F\x03\x02\xFD\u0100\x03\x0223\x03\x02\xF9\xFA\x04\x02:" +
    ":BB\x04\x02;<HH\x05\x02==@AII\x04\x02CCFF\x03\x02>?\x04\x02KKSS\x03\x02" +
    "LM\x03\x02\u0113\u0114\x04\x02TT\\\\\x04\x02VW^_\x04\x02XX[[\x03\x02Y" +
    "Z\x05\x02``iimm\x04\x02aahh\x05\x02bbddoo\x05\x02ccffpp\x03\x02kl\x03" +
    "\x02\u0125\u0126\x03\x02rs\x03\x02\xFB\xFC\x06\x02vv}}\x89\x89\x8B\x8B" +
    "\x04\x02\u0125\u0126\u012A\u012A\x04\x02\x7F\x7F\x88\x88\x03\x02\x80\x81" +
    "\x03\x02\u012B\u012C\x05\x02\x87\x87\x8F\x8F\x91\x91\x03\x02\x8D\x8E\x05" +
    "\x02\x94\x94\x9E\x9E\xA0\xA0\x05\x02\x9C\x9C\x9F\x9F\xA1\xA1\x04\x02\x98" +
    "\x98\x9B\x9B\x04\x02\x97\x97\x9A\x9A\x03\x02\xB4\xB5\x03\x02\xC3\xC4\v" +
    "\x02\xB7\xB7\xBB\xBB\xBF\xBF\xC1\xC2\xC6\xC6\xCC\xCC\xD1\xD1\xF4\xF4\u011D" +
    "\u011D\x04\x02\xC7\xC8\xD2\xD2\x03\x02\xCA\xCB\x04\x02\xE9\xE9\u0132\u0132" +
    "\x02\u071A\x02\u018B\x03\x02\x02\x02\x04\u0193\x03\x02\x02\x02\x06\u019D" +
    "\x03\x02\x02\x02\b\u019F\x03\x02\x02\x02\n\u01A1\x03\x02\x02\x02\f\u01A3" +
    "\x03\x02\x02\x02\x0E\u01A5\x03\x02\x02\x02\x10\u01A7\x03\x02\x02\x02\x12" +
    "\u01A9\x03\x02\x02\x02\x14\u01AB\x03\x02\x02\x02\x16\u01AD\x03\x02\x02" +
    "\x02\x18\u01AF\x03\x02\x02\x02\x1A\u01B1\x03\x02\x02\x02\x1C\u01B3\x03" +
    "\x02\x02\x02\x1E\u01B5\x03\x02\x02\x02 \u01B7\x03\x02\x02\x02\"\u01B9" +
    "\x03\x02\x02\x02$\u01BB\x03\x02\x02\x02&\u01BE\x03\x02\x02\x02(\u01C1" +
    "\x03\x02\x02\x02*\u01C3\x03\x02\x02\x02,\u01C6\x03\x02\x02\x02.\u01C8" +
    "\x03\x02\x02\x020\u01CB\x03\x02\x02\x022\u01CE\x03\x02\x02\x024\u01D2" +
    "\x03\x02\x02\x026\u01D4\x03\x02\x02\x028\u01D7\x03\x02\x02\x02:\u01D9" +
    "\x03\x02\x02\x02<\u01DC\x03\x02\x02\x02>\u01DF\x03\x02\x02\x02@\u01E2" +
    "\x03\x02\x02\x02B\u01EC\x03\x02\x02\x02D\u01F2\x03\x02\x02\x02F\u01F5" +
    "\x03\x02\x02\x02H\u01FB\x03\x02\x02\x02J\u0201\x03\x02\x02\x02L\u0207" +
    "\x03\x02\x02\x02N\u020A\x03\x02\x02\x02P\u020D\x03\x02\x02\x02R\u0211" +
    "\x03\x02\x02\x02T\u0215\x03\x02\x02\x02V\u0218\x03\x02\x02\x02X\u021E" +
    "\x03\x02\x02\x02Z\u0224\x03\x02\x02\x02\\\u0227\x03\x02\x02\x02^\u022A" +
    "\x03\x02\x02\x02`\u022D\x03\x02\x02\x02b\u022F\x03\x02\x02\x02d\u0233" +
    "\x03\x02\x02\x02f\u0237\x03\x02\x02\x02h\u0247\x03\x02\x02\x02j\u0251" +
    "\x03\x02\x02\x02l\u026B\x03\x02\x02\x02n\u0281\x03\x02\x02\x02p\u0287" +
    "\x03\x02\x02\x02r\u028A\x03\x02\x02\x02t\u0290\x03\x02\x02\x02v\u0294" +
    "\x03\x02\x02\x02x\u0298\x03\x02\x02\x02z\u029B\x03\x02\x02\x02|\u029F" +
    "\x03\x02\x02\x02~\u02A2\x03\x02\x02\x02\x80\u02A5\x03\x02\x02\x02\x82" +
    "\u02AB\x03\x02\x02\x02\x84\u02B0\x03\x02\x02\x02\x86\u02B4\x03\x02\x02" +
    "\x02\x88\u02C2\x03\x02\x02\x02\x8A\u02C4\x03\x02\x02\x02\x8C\u02D4\x03" +
    "\x02\x02\x02\x8E\u02DA\x03\x02\x02\x02\x90\u02E0\x03\x02\x02\x02\x92\u02E6" +
    "\x03\x02\x02\x02\x94\u02EB\x03\x02\x02\x02\x96\u02F8\x03\x02\x02\x02\x98" +
    "\u02FC\x03\x02\x02\x02\x9A\u0301\x03\x02\x02\x02\x9C\u0306\x03\x02\x02" +
    "\x02\x9E\u0309\x03\x02\x02\x02\xA0\u030E\x03\x02\x02\x02\xA2\u0315\x03" +
    "\x02\x02\x02\xA4\u0319\x03\x02\x02\x02\xA6\u031C\x03\x02\x02\x02\xA8\u0323" +
    "\x03\x02\x02\x02\xAA\u0328\x03\x02\x02\x02\xAC\u0330\x03\x02\x02\x02\xAE" +
    "\u033B\x03\x02\x02\x02\xB0\u033F\x03\x02\x02\x02\xB2\u0346\x03\x02\x02" +
    "\x02\xB4\u0352\x03\x02\x02\x02\xB6\u035A\x03\x02\x02\x02\xB8\u035F\x03" +
    "\x02\x02\x02\xBA\u0363\x03\x02\x02\x02\xBC\u0369\x03\x02\x02\x02\xBE\u036C" +
    "\x03\x02\x02\x02\xC0\u0372\x03\x02\x02\x02\xC2\u0377\x03\x02\x02\x02\xC4" +
    "\u0383\x03\x02\x02\x02\xC6\u038A\x03\x02\x02\x02\xC8\u038F\x03\x02\x02" +
    "\x02\xCA\u0394\x03\x02\x02\x02\xCC\u0398\x03\x02\x02\x02\xCE\u039F\x03" +
    "\x02\x02\x02\xD0\u03A2\x03\x02\x02\x02\xD2\u03A8\x03\x02\x02\x02\xD4\u03AF" +
    "\x03\x02\x02\x02\xD6\u03BA\x03\x02\x02\x02\xD8\u03BE\x03\x02\x02\x02\xDA" +
    "\u03C3\x03\x02\x02\x02\xDC\u03C8\x03\x02\x02\x02\xDE\u03D2\x03\x02\x02" +
    "\x02\xE0\u03DE\x03\x02\x02\x02\xE2\u03E6\x03\x02\x02\x02\xE4\u03F9\x03" +
    "\x02\x02\x02\xE6\u03FC\x03\x02\x02\x02\xE8\u0401\x03\x02\x02\x02\xEA\u040B" +
    "\x03\x02\x02\x02\xEC\u0413\x03\x02\x02\x02\xEE\u0418\x03\x02\x02\x02\xF0" +
    "\u042E\x03\x02\x02\x02\xF2\u0439\x03\x02\x02\x02\xF4\u044D\x03\x02\x02" +
    "\x02\xF6\u0457\x03\x02\x02\x02\xF8\u045E\x03";
RedisParser._serializedATNSegment1 = "\x02\x02\x02\xFA\u0463\x03\x02\x02\x02\xFC\u046B\x03\x02\x02\x02\xFE\u047B" +
    "\x03\x02\x02\x02\u0100\u0482\x03\x02\x02\x02\u0102\u048C\x03\x02\x02\x02" +
    "\u0104\u0497\x03\x02\x02\x02\u0106\u049B\x03\x02\x02\x02\u0108\u04A0\x03" +
    "\x02\x02\x02\u010A\u04A7\x03\x02\x02\x02\u010C\u04B1\x03\x02\x02\x02\u010E" +
    "\u04BA\x03\x02\x02\x02\u0110\u04D0\x03\x02\x02\x02\u0112\u04E4\x03\x02" +
    "\x02\x02\u0114\u04E7\x03\x02\x02\x02\u0116\u04EB\x03\x02\x02\x02\u0118" +
    "\u04F4\x03\x02\x02\x02\u011A\u04FD\x03\x02\x02\x02\u011C\u0501\x03\x02" +
    "\x02\x02\u011E\u0504\x03\x02\x02\x02\u0120\u0506\x03\x02\x02\x02\u0122" +
    "\u050C\x03\x02\x02\x02\u0124\u050E\x03\x02\x02\x02\u0126\u0510\x03\x02" +
    "\x02\x02\u0128\u0512\x03\x02\x02\x02\u012A\u0519\x03\x02\x02\x02\u012C" +
    "\u0520\x03\x02\x02\x02\u012E\u0527\x03\x02\x02\x02\u0130\u052D\x03\x02" +
    "\x02\x02\u0132\u0535\x03\x02\x02\x02\u0134\u0538\x03\x02\x02\x02\u0136" +
    "\u0541\x03\x02\x02\x02\u0138\u0546\x03\x02\x02\x02\u013A\u054A\x03\x02" +
    "\x02\x02\u013C\u054C\x03\x02\x02\x02\u013E\u054E\x03\x02\x02\x02\u0140" +
    "\u0550\x03\x02\x02\x02\u0142\u0557\x03\x02\x02\x02\u0144\u055B\x03\x02" +
    "\x02\x02\u0146\u0560\x03\x02\x02\x02\u0148\u0564\x03\x02\x02\x02\u014A" +
    "\u0569\x03\x02\x02\x02\u014C\u056C\x03\x02\x02\x02\u014E\u0570\x03\x02" +
    "\x02\x02\u0150\u0577\x03\x02\x02\x02\u0152\u057B\x03\x02\x02\x02\u0154" +
    "\u057F\x03\x02\x02\x02\u0156\u058E\x03\x02\x02\x02\u0158\u0592\x03\x02" +
    "\x02\x02\u015A\u0595\x03\x02\x02\x02\u015C\u0597\x03\x02\x02\x02\u015E" +
    "\u059A\x03\x02\x02\x02\u0160\u05A5\x03\x02\x02\x02\u0162\u05B0\x03\x02" +
    "\x02\x02\u0164\u05B2\x03\x02\x02\x02\u0166\u05B9\x03\x02\x02\x02\u0168" +
    "\u05BB\x03\x02\x02\x02\u016A\u05BD\x03\x02\x02\x02\u016C\u05BF\x03\x02" +
    "\x02\x02\u016E\u05C3\x03\x02\x02\x02\u0170\u05C5\x03\x02\x02\x02\u0172" +
    "\u05C8\x03\x02\x02\x02\u0174\u05CA\x03\x02\x02\x02\u0176\u05CC\x03\x02" +
    "\x02\x02\u0178\u05CF\x03\x02\x02\x02\u017A\u05D1\x03\x02\x02\x02\u017C" +
    "\u05D3\x03\x02\x02\x02\u017E\u05D5\x03\x02\x02\x02\u0180\u05DD\x03\x02" +
    "\x02\x02\u0182\u05DF\x03\x02\x02\x02\u0184\u05EF\x03\x02\x02\x02\u0186" +
    "\u05F1\x03\x02\x02\x02\u0188\u069C\x03\x02\x02\x02\u018A\u018C\x05\x04" +
    "\x03\x02\u018B\u018A\x03\x02\x02\x02\u018B\u018C\x03\x02\x02\x02\u018C" +
    "\u018D\x03\x02\x02\x02\u018D\u018E\x07\x02\x02\x03\u018E\x03\x03\x02\x02" +
    "\x02\u018F\u0192\x05\u0188\xC5\x02\u0190\u0192\x05\x06\x04\x02\u0191\u018F" +
    "\x03\x02\x02\x02\u0191\u0190\x03\x02\x02\x02\u0192\u0195\x03\x02\x02\x02" +
    "\u0193\u0191\x03\x02\x02\x02\u0193\u0194\x03\x02\x02\x02\u0194\u019B\x03" +
    "\x02\x02\x02\u0195\u0193\x03\x02\x02\x02\u0196\u0198\x05\u0188\xC5\x02" +
    "\u0197\u0199\x07\u013C\x02\x02\u0198\u0197\x03\x02\x02\x02\u0198\u0199" +
    "\x03\x02\x02\x02\u0199\u019C\x03\x02\x02\x02\u019A\u019C\x05\x06\x04\x02" +
    "\u019B\u0196\x03\x02\x02\x02\u019B\u019A\x03\x02\x02\x02\u019C\x05\x03" +
    "\x02\x02\x02\u019D\u019E\x07\u013C\x02\x02\u019E\x07\x03\x02\x02\x02\u019F" +
    "\u01A0\t\x02\x02\x02\u01A0\t\x03\x02\x02\x02\u01A1\u01A2\t\x02\x02\x02" +
    "\u01A2\v\x03\x02\x02\x02\u01A3\u01A4\x07\u0138\x02\x02\u01A4\r\x03\x02" +
    "\x02\x02\u01A5\u01A6\x07\u0136\x02\x02\u01A6\x0F\x03\x02\x02\x02\u01A7" +
    "\u01A8\t\x03\x02\x02\u01A8\x11\x03\x02\x02\x02\u01A9\u01AA\t\x02\x02\x02" +
    "\u01AA\x13\x03\x02\x02\x02\u01AB\u01AC\x05\x0E\b\x02\u01AC\x15\x03\x02" +
    "\x02\x02\u01AD\u01AE\x05\x0E\b\x02\u01AE\x17\x03\x02\x02\x02\u01AF\u01B0" +
    "\x05\x0E\b\x02\u01B0\x19\x03\x02\x02\x02\u01B1\u01B2\x07\u0138\x02\x02" +
    "\u01B2\x1B\x03\x02\x02\x02\u01B3\u01B4\x05\x0E\b\x02\u01B4\x1D\x03\x02" +
    "\x02\x02\u01B5\u01B6\x05\x0E\b\x02\u01B6\x1F\x03\x02\x02\x02\u01B7\u01B8" +
    "\x05\x0E\b\x02\u01B8!\x03\x02\x02\x02\u01B9\u01BA\x05\x0E\b\x02\u01BA" +
    "#\x03\x02\x02\x02\u01BB\u01BC\x05\b\x05\x02\u01BC\u01BD\x05\f\x07\x02" +
    "\u01BD%\x03\x02\x02\x02\u01BE\u01BF\x05\x0E\b\x02\u01BF\u01C0\x05\f\x07" +
    "\x02\u01C0\'\x03\x02\x02\x02\u01C1\u01C2\t\x04\x02\x02\u01C2)\x03\x02" +
    "\x02\x02\u01C3\u01C4\x07%\x02\x02\u01C4\u01C5\x05\x12\n\x02\u01C5+\x03" +
    "\x02\x02\x02\u01C6\u01C7\t\x05\x02\x02\u01C7-\x03\x02\x02\x02\u01C8\u01C9" +
    "\x07\xFF\x02\x02\u01C9\u01CA\x05\x0E\b\x02\u01CA/\x03\x02\x02\x02\u01CB" +
    "\u01CC\x07\xFE\x02\x02\u01CC\u01CD\x05\x0E\b\x02\u01CD1\x03\x02\x02\x02" +
    "\u01CE\u01CF\x07\u0112\x02\x02\u01CF\u01D0\x05\x0E\b\x02\u01D0\u01D1\x05" +
    "\x0E\b\x02\u01D13\x03\x02\x02\x02\u01D2\u01D3\t\x06\x02\x02\u01D35\x03" +
    "\x02\x02\x02\u01D4\u01D5\t\x07\x02\x02\u01D5\u01D6\x05\x0E\b\x02\u01D6" +
    "7\x03\x02\x02\x02\u01D7\u01D8\t\b\x02\x02\u01D89\x03\x02\x02\x02\u01D9" +
    "\u01DA\x07\u0130\x02\x02\u01DA\u01DB\x05\x0E\b\x02\u01DB;\x03\x02\x02" +
    "\x02\u01DC\u01DD\x07\u011F\x02\x02\u01DD\u01DE\x05\x0E\b\x02\u01DE=\x03" +
    "\x02\x02\x02\u01DF\u01E0\x07\u0131\x02\x02\u01E0\u01E1\x05\x0E\b\x02\u01E1" +
    "?\x03\x02\x02\x02\u01E2\u01E3\x07\x07\x02\x02\u01E3\u01E4\x05\b\x05\x02" +
    "\u01E4\u01E7\x05\b\x05\x02\u01E5\u01E6\x07\xF8\x02\x02\u01E6\u01E8\x05" +
    "\x0E\b\x02\u01E7\u01E5\x03\x02\x02\x02\u01E7\u01E8\x03\x02\x02\x02\u01E8" +
    "\u01EA\x03\x02\x02\x02\u01E9\u01EB\x07\u0107\x02\x02\u01EA\u01E9\x03\x02" +
    "\x02\x02\u01EA\u01EB\x03\x02\x02\x02\u01EBA\x03\x02\x02\x02\u01EC\u01EE" +
    "\x07\b\x02\x02\u01ED\u01EF\x05\b\x05\x02\u01EE\u01ED\x03\x02\x02\x02\u01EF" +
    "\u01F0\x03\x02\x02\x02\u01F0\u01EE\x03\x02\x02\x02\u01F0\u01F1\x03\x02" +
    "\x02\x02\u01F1C\x03\x02\x02\x02\u01F2\u01F3\x07\t\x02\x02\u01F3\u01F4" +
    "\x05\b\x05\x02\u01F4E\x03\x02\x02\x02\u01F5\u01F7\x07\n\x02\x02\u01F6" +
    "\u01F8\x05\b\x05\x02\u01F7\u01F6\x03\x02\x02\x02\u01F8\u01F9\x03\x02\x02" +
    "\x02\u01F9\u01F7\x03\x02\x02\x02\u01F9\u01FA\x03\x02\x02\x02\u01FAG\x03" +
    "\x02\x02\x02\u01FB\u01FC\x07\v\x02\x02\u01FC\u01FD\x05\b\x05\x02\u01FD" +
    "\u01FF\x05\x0E\b\x02\u01FE\u0200\x05,\x17\x02\u01FF\u01FE\x03\x02\x02" +
    "\x02\u01FF\u0200\x03\x02\x02\x02\u0200I\x03\x02\x02\x02\u0201\u0202\x07" +
    "\f\x02\x02\u0202\u0203\x05\b\x05\x02\u0203\u0205\x05\x0E\b\x02\u0204\u0206" +
    "\x05,\x17\x02\u0205\u0204\x03\x02\x02\x02\u0205\u0206\x03\x02\x02\x02" +
    "\u0206K\x03\x02\x02\x02\u0207\u0208\x07\r\x02\x02\u0208\u0209\x05\b\x05" +
    "\x02\u0209M\x03\x02\x02\x02\u020A\u020B\x07\x0E\x02\x02\u020B\u020C\x05" +
    "\x12\n\x02\u020CO\x03\x02\x02\x02\u020D\u020E\x07\x0F\x02\x02\u020E\u020F" +
    "\x05\b\x05\x02\u020F\u0210\x05\x0E\b\x02\u0210Q\x03\x02\x02\x02\u0211" +
    "\u0212\x07\x10\x02\x02\u0212\u0213\t\t\x02\x02\u0213\u0214\x05\b\x05\x02" +
    "\u0214S\x03\x02\x02\x02\u0215\u0216\x07\x11\x02\x02\u0216\u0217\x05\b" +
    "\x05\x02\u0217U\x03\x02\x02\x02\u0218\u0219\x07\x12\x02\x02\u0219\u021A" +
    "\x05\b\x05\x02\u021A\u021C\x05\x0E\b\x02\u021B\u021D\x05,\x17\x02\u021C" +
    "\u021B\x03\x02\x02\x02\u021C\u021D\x03\x02\x02\x02\u021DW\x03\x02\x02" +
    "\x02\u021E\u021F\x07\x13\x02\x02\u021F\u0220\x05\b\x05\x02\u0220\u0222" +
    "\x05\x0E\b\x02\u0221\u0223\x05,\x17\x02\u0222\u0221\x03\x02\x02\x02\u0222" +
    "\u0223\x03\x02\x02\x02\u0223Y\x03\x02\x02\x02\u0224\u0225\x07\x14\x02" +
    "\x02\u0225\u0226\x05\b\x05\x02\u0226[\x03\x02\x02\x02\u0227\u0228\x07" +
    "\x15\x02\x02\u0228\u0229\x05\b\x05\x02\u0229]\x03\x02\x02\x02\u022A\u022B" +
    "\x07\x16\x02\x02\u022B\u022C\x05\b\x05\x02\u022C_\x03\x02\x02\x02\u022D" +
    "\u022E\x07\x17\x02\x02\u022Ea\x03\x02\x02\x02\u022F\u0230\x07\x18\x02" +
    "\x02\u0230\u0231\x05\b\x05\x02\u0231\u0232\x05\b\x05\x02\u0232c\x03\x02" +
    "\x02\x02\u0233\u0234\x07\x19\x02\x02\u0234\u0235\x05\b\x05\x02\u0235\u0236" +
    "\x05\b\x05\x02\u0236e\x03\x02\x02\x02\u0237\u0238\x07\x1A\x02\x02\u0238" +
    "\u0239\x05\b\x05\x02\u0239\u023A\x05\x0E\b\x02\u023A\u023C\x05\f\x07\x02" +
    "\u023B\u023D\x07\u0107\x02\x02\u023C\u023B\x03\x02\x02\x02\u023C\u023D" +
    "\x03\x02\x02\x02\u023D\u023F\x03\x02\x02\x02\u023E\u0240\x07\u0101\x02" +
    "\x02\u023F\u023E\x03\x02\x02\x02\u023F\u0240\x03\x02\x02\x02\u0240\u0242" +
    "\x03\x02\x02\x02\u0241\u0243\x05.\x18\x02\u0242\u0241\x03\x02\x02\x02" +
    "\u0242\u0243\x03\x02\x02\x02\u0243\u0245\x03\x02\x02\x02\u0244\u0246\x05" +
    "0\x19\x02\u0245\u0244\x03\x02\x02\x02\u0245\u0246\x03\x02\x02\x02\u0246" +
    "g\x03\x02\x02\x02\u0247\u0248\x07\x1B\x02\x02\u0248\u0249\x05\x14\v\x02" +
    "\u0249\u024B\x05\x12\n\x02\u024A\u024C\x05\x0E\b\x02\u024B\u024A\x03\x02" +
    "\x02\x02\u024B\u024C\x03\x02\x02\x02\u024C\u024F\x03\x02\x02\x02\u024D" +
    "\u024E\x07\x1F\x02\x02\u024E\u0250\x05\f\x07\x02\u024F\u024D\x03\x02\x02" +
    "\x02\u024F\u0250\x03\x02\x02\x02\u0250i\x03\x02\x02\x02\u0251\u0252\x07" +
    "\x1C\x02\x02\u0252\u0255\x05\b\x05\x02\u0253\u0254\x07\u0102\x02\x02\u0254" +
    "\u0256\x05\x12\n\x02\u0255\u0253\x03\x02\x02\x02\u0255\u0256\x03\x02\x02" +
    "\x02\u0256\u0258\x03\x02\x02\x02\u0257\u0259\x052\x1A\x02\u0258\u0257" +
    "\x03\x02\x02\x02\u0258\u0259\x03\x02\x02\x02\u0259\u025F\x03\x02\x02\x02" +
    "\u025A\u025C\x05*\x16\x02\u025B\u025A\x03\x02\x02\x02\u025C\u025D\x03" +
    "\x02\x02\x02\u025D\u025B\x03\x02\x02\x02\u025D\u025E\x03\x02\x02\x02\u025E" +
    "\u0260\x03\x02\x02\x02\u025F\u025B\x03\x02\x02\x02\u025F\u0260\x03\x02" +
    "\x02\x02\u0260\u0262\x03\x02\x02\x02\u0261\u0263\x054\x1B\x02\u0262\u0261" +
    "\x03\x02\x02\x02\u0262\u0263\x03\x02\x02\x02\u0263\u0265\x03\x02\x02\x02" +
    "\u0264\u0266\x07\u0105\x02\x02\u0265\u0264\x03\x02\x02\x02\u0265\u0266" +
    "\x03\x02\x02\x02\u0266\u0269\x03\x02\x02\x02\u0267\u0268\x07\u0106\x02" +
    "\x02\u0268\u026A\x05\f\x07\x02\u0269\u0267\x03\x02\x02\x02\u0269\u026A" +
    "\x03\x02\x02\x02\u026Ak\x03\x02\x02\x02\u026B\u026C\x07\x1D\x02\x02\u026C" +
    "\u026F\x05\b\x05\x02\u026D\u026E\x07\u0102\x02\x02\u026E\u0270\x05\x12" +
    "\n\x02\u026F\u026D\x03\x02\x02\x02\u026F\u0270\x03\x02\x02\x02\u0270\u0272" +
    "\x03\x02\x02\x02\u0271\u0273\x052\x1A\x02\u0272\u0271\x03\x02\x02\x02" +
    "\u0272\u0273\x03\x02\x02\x02\u0273\u0279\x03\x02\x02\x02\u0274\u0276\x05" +
    "*\x16\x02\u0275\u0274\x03\x02\x02\x02\u0276\u0277\x03\x02\x02\x02\u0277" +
    "\u0275\x03\x02\x02\x02\u0277\u0278\x03\x02\x02\x02\u0278\u027A\x03\x02" +
    "\x02\x02\u0279\u0275\x03\x02\x02\x02\u0279\u027A\x03\x02\x02\x02\u027A" +
    "\u027C\x03\x02\x02\x02\u027B\u027D\x054\x1B\x02\u027C\u027B\x03\x02\x02" +
    "\x02\u027C\u027D\x03\x02\x02\x02\u027D\u027F\x03\x02\x02\x02\u027E\u0280" +
    "\x07\u0105\x02\x02\u027F\u027E\x03\x02\x02\x02\u027F\u0280\x03\x02\x02" +
    "\x02\u0280m\x03\x02\x02\x02\u0281\u0283\x07\x1E\x02\x02\u0282\u0284\x05" +
    "\b\x05\x02\u0283\u0282\x03\x02\x02\x02\u0284\u0285\x03\x02\x02\x02\u0285" +
    "\u0283\x03\x02\x02\x02\u0285\u0286\x03\x02\x02\x02\u0286o\x03\x02\x02" +
    "\x02\u0287\u0288\x07\x1F\x02\x02\u0288\u0289\x05\b\x05\x02\u0289q\x03" +
    "\x02\x02\x02\u028A\u028C\x07 \x02\x02\u028B\u028D\x05\b\x05\x02\u028C" +
    "\u028B\x03\x02\x02\x02\u028D\u028E\x03\x02\x02\x02\u028E\u028C\x03\x02" +
    "\x02\x02\u028E\u028F\x03\x02\x02\x02\u028Fs\x03\x02\x02\x02\u0290\u0291" +
    "\x07!\x02\x02\u0291\u0292\x05\x0E\b\x02\u0292\u0293\x05\x0E\b\x02\u0293" +
    "u\x03\x02\x02\x02\u0294\u0295\x07\"\x02\x02\u0295\u0296\x05\b\x05\x02" +
    "\u0296\u0297\x05\f\x07\x02\u0297w\x03\x02\x02\x02\u0298\u0299\x07#\x02" +
    "\x02\u0299\u029A\x05\b\x05\x02\u029Ay\x03\x02\x02\x02\u029B\u029C\x07" +
    "$\x02\x02\u029C\u029D\x05\b\x05\x02\u029D\u029E\x05\x0E\b\x02\u029E{\x03" +
    "\x02\x02\x02\u029F\u02A0\x07%\x02\x02\u02A0\u02A1\x05\b\x05\x02\u02A1" +
    "}\x03\x02\x02\x02\u02A2\u02A3\x07&\x02\x02\u02A3\u02A4\x05\b\x05\x02\u02A4" +
    "\x7F\x03\x02\x02\x02\u02A5\u02A6\x07\'\x02\x02\u02A6\u02A9\x05\b\x05\x02" +
    "\u02A7\u02AA\x056\x1C\x02\u02A8\u02AA\x07\x11\x02\x02\u02A9\u02A7\x03" +
    "\x02\x02\x02\u02A9\u02A8\x03\x02\x02\x02\u02A9\u02AA\x03\x02\x02\x02\u02AA" +
    "\x81\x03\x02\x02\x02\u02AB\u02AC\x07(\x02\x02\u02AC\u02AD\x05\b\x05\x02" +
    "\u02AD\u02AE\x05\x0E\b\x02\u02AE\u02AF\x05\x0E\b\x02\u02AF\x83\x03\x02" +
    "\x02\x02\u02B0\u02B1\x07)\x02\x02\u02B1\u02B2\x05\b\x05\x02\u02B2\u02B3" +
    "\x05\f\x07\x02\u02B3\x85\x03\x02\x02\x02\u02B4\u02B5\x07*\x02\x02\u02B5" +
    "\u02B6\x05\b\x05\x02\u02B6\u02B7\x05\x0E\b\x02\u02B7\x87\x03\x02\x02\x02" +
    "\u02B8\u02B9\x07+\x02\x02\u02B9\u02C3\x05\b\x05\x02\u02BA\u02BB\x07,\x02" +
    "\x02\u02BB\u02BC\x05\b\x05\x02\u02BC\u02BD\x05\x0E\b\x02\u02BD\u02C3\x03" +
    "\x02\x02\x02\u02BE\u02BF\x07-\x02\x02\u02BF\u02C0\x05\b\x05\x02\u02C0" +
    "\u02C1\x05\x10\t\x02\u02C1\u02C3\x03\x02\x02\x02\u02C2\u02B8\x03\x02\x02" +
    "\x02\u02C2\u02BA\x03\x02\x02\x02\u02C2\u02BE\x03\x02\x02\x02\u02C3\x89" +
    "\x03\x02\x02\x02\u02C4\u02C5\x07.\x02\x02\u02C5\u02C6\x05\b\x05\x02\u02C6" +
    "\u02C8\x05\b\x05\x02\u02C7\u02C9\x07\u010C\x02\x02\u02C8\u02C7\x03\x02" +
    "\x02\x02\u02C8\u02C9\x03\x02\x02\x02\u02C9\u02CB\x03\x02\x02\x02\u02CA" +
    "\u02CC\x07\u010D\x02\x02\u02CB\u02CA\x03\x02\x02\x02\u02CB\u02CC\x03\x02" +
    "\x02\x02\u02CC\u02CF\x03\x02\x02\x02\u02CD\u02CE\x07\u010E\x02\x02\u02CE" +
    "\u02D0\x05\x0E\b\x02\u02CF\u02CD\x03\x02\x02\x02\u02CF\u02D0\x03\x02\x02" +
    "\x02\u02D0\u02D2\x03\x02\x02\x02\u02D1\u02D3\x07\u010F\x02\x02\u02D2\u02D1" +
    "\x03\x02\x02\x02\u02D2\u02D3\x03\x02\x02\x02\u02D3\x8B\x03\x02\x02\x02" +
    "\u02D4\u02D6\x07/\x02\x02\u02D5\u02D7\x05\b\x05\x02\u02D6\u02D5\x03\x02" +
    "\x02\x02\u02D7\u02D8\x03\x02\x02\x02\u02D8\u02D6\x03\x02\x02\x02\u02D8" +
    "\u02D9\x03\x02\x02\x02\u02D9\x8D\x03\x02\x02\x02\u02DA\u02DC\x070\x02" +
    "\x02\u02DB\u02DD\x05$\x13\x02\u02DC\u02DB\x03\x02\x02\x02\u02DD\u02DE" +
    "\x03\x02\x02\x02\u02DE\u02DC\x03\x02\x02\x02\u02DE\u02DF\x03\x02\x02\x02" +
    "\u02DF\x8F\x03\x02\x02\x02\u02E0\u02E2\x071\x02\x02\u02E1\u02E3\x05$\x13" +
    "\x02\u02E2\u02E1\x03\x02\x02\x02\u02E3\u02E4\x03\x02\x02\x02\u02E4\u02E2" +
    "\x03\x02\x02\x02\u02E4\u02E5\x03\x02\x02\x02\u02E5\x91\x03\x02\x02\x02" +
    "\u02E6\u02E7\t\n\x02\x02\u02E7\u02E8\x05\b\x05\x02\u02E8\u02E9\x05\x0E" +
    "\b\x02\u02E9\u02EA\x05\f\x07\x02\u02EA\x93\x03\x02\x02\x02\u02EB\u02EC" +
    "\x074\x02\x02\u02EC\u02ED\x05\b\x05\x02\u02ED\u02F0\x05\f\x07\x02\u02EE" +
    "\u02F1\x056\x1C\x02\u02EF\u02F1\x07\u0110\x02\x02\u02F0\u02EE\x03\x02" +
    "\x02\x02\u02F0\u02EF\x03\x02\x02\x02\u02F0\u02F1\x03\x02\x02\x02\u02F1" +
    "\u02F3\x03\x02\x02\x02\u02F2\u02F4\t\v\x02\x02\u02F3\u02F2\x03\x02\x02" +
    "\x02\u02F3\u02F4\x03\x02\x02\x02\u02F4\u02F6\x03\x02\x02\x02\u02F5\u02F7" +
    "\x07%\x02\x02\u02F6\u02F5\x03\x02\x02\x02\u02F6\u02F7\x03\x02\x02\x02" +
    "\u02F7\x95\x03\x02\x02\x02\u02F8\u02F9\x075\x02\x02\u02F9\u02FA\x05\b" +
    "\x05\x02\u02FA\u02FB\x05\f\x07\x02\u02FB\x97\x03\x02\x02\x02\u02FC\u02FD" +
    "\x076\x02\x02\u02FD\u02FE\x05\b\x05\x02\u02FE\u02FF\x05\x0E\b\x02\u02FF" +
    "\u0300\x05\f\x07\x02\u0300\x99\x03\x02\x02\x02\u0301\u0302\x077\x02\x02" +
    "\u0302\u0303\x05\b\x05\x02\u0303\u0304\x05\x0E\b\x02\u0304\u0305\x05\x0E" +
    "\b\x02\u0305\x9B\x03\x02\x02\x02\u0306\u0307\x078\x02\x02\u0307\u0308" +
    "\x05\b\x05\x02\u0308\x9D\x03\x02\x02\x02\u0309\u030A\x079\x02\x02\u030A" +
    "\u030B\x05\b\x05\x02\u030B\u030C\x05\x0E\b\x02\u030C\u030D\x05\x0E\b\x02" +
    "\u030D\x9F\x03\x02\x02\x02\u030E\u030F\t\f\x02\x02\u030F\u0311\x05\b\x05" +
    "\x02\u0310\u0312\x05\n\x06\x02\u0311\u0310\x03\x02\x02\x02\u0312\u0313" +
    "\x03\x02\x02\x02\u0313\u0311\x03\x02\x02\x02\u0313\u0314\x03\x02\x02\x02" +
    "\u0314\xA1\x03\x02\x02\x02\u0315\u0316\t\r\x02\x02\u0316\u0317\x05\b\x05" +
    "\x02\u0317\u0318\x05\n\x06\x02\u0318\xA3\x03\x02\x02\x02\u0319\u031A\t" +
    "\x0E\x02\x02\u031A\u031B\x05\b\x05\x02\u031B\xA5\x03\x02\x02\x02\u031C" +
    "\u031D\t\x0F\x02\x02\u031D\u031F\x05\b\x05\x02\u031E\u0320\x05$\x13\x02" +
    "\u031F\u031E\x03\x02\x02\x02\u0320\u0321\x03\x02\x02\x02\u0321\u031F\x03" +
    "\x02\x02\x02\u0321\u0322\x03\x02\x02\x02\u0322\xA7\x03\x02\x02\x02\u0323" +
    "\u0324\t\x10\x02\x02\u0324\u0325\x05\b\x05\x02\u0325\u0326\x05\n\x06\x02" +
    "\u0326\u0327\x05\x10\t\x02\u0327\xA9\x03\x02\x02\x02\u0328\u0329\x07D" +
    "\x02\x02\u0329\u032E\x05\b\x05\x02\u032A\u032C\x05\x0E\b\x02\u032B\u032D" +
    "\x07\u0123\x02\x02\u032C\u032B\x03\x02\x02\x02\u032C\u032D\x03\x02\x02" +
    "\x02\u032D\u032F\x03\x02\x02\x02\u032E\u032A\x03\x02\x02\x02\u032E\u032F" +
    "\x03\x02\x02\x02\u032F\xAB\x03\x02\x02\x02\u0330\u0331\x07E\x02\x02\u0331" +
    "\u0332\x05\b\x05\x02\u0332\u0335\x05\x0E\b\x02\u0333\u0334\x07\u0124\x02" +
    "\x02\u0334\u0336\x05\x12\n\x02\u0335\u0333\x03\x02\x02\x02\u0335\u0336" +
    "\x03\x02\x02\x02\u0336\u0339\x03\x02\x02\x02\u0337\u0338\x07\u011F\x02" +
    "\x02\u0338\u033A\x05\x0E\b\x02\u0339\u0337\x03\x02\x02\x02\u0339\u033A" +
    "\x03\x02\x02\x02\u033A\xAD\x03\x02\x02\x02\u033B\u033C\x07G\x02\x02\u033C" +
    "\u033D\x05\b\x05\x02\u033D\u033E\x05$\x13\x02\u033E\xAF\x03\x02\x02\x02" +
    "\u033F\u0340\x07J\x02\x02\u0340\u0341\x05\b\x05\x02\u0341\u0342\x05\b" +
    "\x05\x02\u0342\u0343\x058\x1D\x02\u0343\u0344\x058\x1D\x02\u0344\u0345" +
    "\x05\x0E\b\x02\u0345\xB1\x03\x02\x02\x02\u0346\u0347\t\x11\x02\x02\u0347" +
    "\u0348\x05\x0E\b\x02\u0348\u034A\x05\x0E\b\x02\u0349\u034B\x05\b\x05\x02" +
    "\u034A\u0349\x03\x02\x02\x02\u034B\u034C\x03\x02\x02\x02\u034C\u034A\x03" +
    "\x02\x02\x02\u034C\u034D\x03\x02\x02\x02\u034D\u034E\x03\x02\x02\x02\u034E" +
    "\u0350\x058\x1D\x02\u034F\u0351\x05\x0E\b\x02\u0350\u034F\x03\x02\x02" +
    "\x02\u0350\u0351\x03\x02\x02\x02\u0351\xB3\x03\x02\x02\x02\u0352\u0354" +
    "\t\x12\x02\x02\u0353\u0355\x05\b\x05\x02\u0354\u0353\x03\x02\x02\x02\u0355" +
    "\u0356\x03\x02\x02\x02\u0356\u0354\x03\x02\x02\x02\u0356\u0357\x03\x02" +
    "\x02\x02\u0357\u0358\x03\x02\x02\x02\u0358\u0359\x05\x0E\b\x02\u0359\xB5" +
    "\x03\x02\x02\x02\u035A\u035B\x07N\x02\x02\u035B\u035C\x05\b\x05\x02\u035C" +
    "\u035D\x05\b\x05\x02\u035D\u035E\x05\x0E\b\x02\u035E\xB7\x03\x02\x02\x02" +
    "\u035F\u0360\x07O\x02\x02\u0360\u0361\x05\b\x05\x02\u0361\u0362\x05\x0E" +
    "\b\x02\u0362\xB9\x03\x02\x02\x02\u0363\u0364\x07P\x02\x02\u0364\u0365" +
    "\x05\b\x05\x02\u0365\u0366\t\x13\x02\x02\u0366\u0367\x05\f\x07\x02\u0367" +
    "\u0368\x05\f\x07\x02\u0368\xBB\x03\x02\x02\x02\u0369\u036A\x07Q\x02\x02" +
    "\u036A\u036B\x05\b\x05\x02\u036B\xBD\x03\x02\x02\x02\u036C\u036D\x07R" +
    "\x02\x02\u036D\u036E\x05\b\x05\x02\u036E\u036F\x05\b\x05\x02\u036F\u0370" +
    "\x058\x1D\x02\u0370\u0371\x058\x1D\x02\u0371\xBF\x03\x02\x02\x02\u0372" +
    "\u0373\t\x14\x02\x02\u0373\u0375\x05\b\x05\x02\u0374\u0376\x05\x0E\b\x02" +
    "\u0375\u0374\x03\x02\x02\x02\u0375\u0376\x03\x02\x02\x02\u0376\xC1\x03" +
    "\x02\x02\x02\u0377\u0378\x07U\x02\x02\u0378\u0379\x05\b\x05\x02\u0379" +
    "\u037B\x05\n\x06\x02\u037A\u037C\x05:\x1E\x02\u037B\u037A\x03\x02\x02" +
    "\x02\u037B\u037C\x03\x02\x02\x02\u037C\u037E\x03\x02\x02\x02\u037D\u037F" +
    "\x05<\x1F\x02\u037E\u037D\x03\x02\x02\x02\u037E\u037F\x03\x02\x02\x02" +
    "\u037F\u0381\x03\x02\x02\x02\u0380\u0382\x05> \x02\u0381\u0380\x03\x02" +
    "\x02\x02\u0381\u0382\x03\x02\x02\x02\u0382\xC3\x03\x02\x02\x02\u0383\u0384" +
    "\t\x15\x02\x02\u0384\u0386\x05\b\x05\x02\u0385\u0387\x05\f\x07\x02\u0386" +
    "\u0385\x03\x02\x02\x02\u0387\u0388\x03\x02\x02\x02\u0388\u0386\x03\x02" +
    "\x02\x02\u0388\u0389\x03\x02\x02\x02\u0389\xC5\x03\x02\x02\x02\u038A\u038B" +
    "\t\x16\x02\x02\u038B\u038C\x05\b\x05\x02\u038C\u038D\x05\x0E\b\x02\u038D" +
    "\u038E\x05\x0E\b\x02\u038E\xC7\x03\x02\x02\x02\u038F\u0390\t\x17\x02\x02" +
    "\u0390\u0391\x05\b\x05\x02\u0391\u0392\x05\x0E\b\x02\u0392\u0393\x05\f" +
    "\x07\x02\u0393\xC9\x03\x02\x02\x02\u0394\u0395\x07]\x02\x02\u0395\u0396" +
    "\x05\b\x05\x02\u0396\u0397\x05\b\x05\x02\u0397\xCB\x03\x02\x02\x02\u0398" +
    "\u0399\t\x18\x02\x02\u0399\u039B\x05\b\x05\x02\u039A\u039C\x05\n\x06\x02" +
    "\u039B\u039A\x03\x02\x02\x02\u039C\u039D\x03\x02\x02\x02\u039D\u039B\x03" +
    "\x02\x02\x02\u039D\u039E\x03\x02\x02\x02\u039E\xCD\x03\x02\x02\x02\u039F" +
    "\u03A0\t\x19\x02\x02\u03A0\u03A1\x05\b\x05\x02\u03A1\xCF\x03\x02\x02\x02" +
    "\u03A2\u03A4\t\x1A\x02\x02\u03A3\u03A5\x05\b\x05\x02\u03A4\u03A3\x03\x02" +
    "\x02\x02\u03A5\u03A6\x03\x02\x02\x02\u03A6\u03A4\x03\x02\x02\x02\u03A6" +
    "\u03A7\x03\x02\x02\x02\u03A7\xD1\x03\x02\x02\x02\u03A8\u03A9\t\x1B\x02" +
    "\x02\u03A9\u03AB\x05\b\x05\x02\u03AA\u03AC\x05\b\x05\x02\u03AB\u03AA\x03" +
    "\x02\x02\x02\u03AC\u03AD\x03\x02\x02\x02\u03AD\u03AB\x03\x02\x02\x02\u03AD" +
    "\u03AE\x03\x02\x02\x02\u03AE\xD3\x03\x02\x02\x02\u03AF\u03B0\x07e\x02" +
    "\x02\u03B0\u03B2\x05\x0E\b\x02\u03B1\u03B3\x05\b\x05\x02\u03B2\u03B1\x03" +
    "\x02\x02\x02\u03B3\u03B4\x03\x02\x02\x02\u03B4\u03B2\x03\x02\x02\x02\u03B4" +
    "\u03B5\x03\x02\x02\x02\u03B5\u03B8\x03\x02\x02\x02\u03B6\u03B7\x07\u0112" +
    "\x02\x02\u03B7\u03B9\x05\x0E\b\x02\u03B8\u03B6\x03\x02\x02\x02\u03B8\u03B9" +
    "\x03\x02\x02\x02\u03B9\xD5\x03\x02\x02\x02\u03BA\u03BB\x07g\x02\x02\u03BB" +
    "\u03BC\x05\b\x05\x02\u03BC\u03BD\x05\n\x06\x02\u03BD\xD7\x03\x02\x02\x02" +
    "\u03BE\u03BF\x07j\x02\x02\u03BF\u03C0\x05\b\x05\x02\u03C0\u03C1\x05\b" +
    "\x05\x02\u03C1\u03C2\x05\n\x06\x02\u03C2\xD9\x03\x02\x02\x02\u03C3\u03C4" +
    "\t\x1C\x02\x02\u03C4\u03C6\x05\b\x05\x02\u03C5\u03C7\x05\x0E\b\x02\u03C6" +
    "\u03C5\x03\x02\x02\x02\u03C6\u03C7\x03\x02\x02\x02\u03C7\xDB\x03\x02\x02" +
    "\x02\u03C8\u03C9\x07n\x02\x02\u03C9\u03CA\x05\b\x05\x02\u03CA\u03CD\x05" +
    "\x0E\b\x02\u03CB\u03CC\x07\u0124\x02\x02\u03CC\u03CE\x05\x12\n\x02\u03CD" +
    "\u03CB\x03\x02\x02\x02\u03CD\u03CE\x03\x02\x02\x02\u03CE\u03D0\x03\x02" +
    "\x02\x02\u03CF\u03D1\x05<\x1F\x02\u03D0\u03CF\x03\x02\x02\x02\u03D0\u03D1" +
    "\x03\x02\x02\x02\u03D1\xDD\x03\x02\x02\x02\u03D2\u03D3\x07q\x02\x02\u03D3" +
    "\u03D4\x05\x0E\b\x02\u03D4\u03D6\x05\x0E\b\x02\u03D5\u03D7\x05\b\x05\x02" +
    "\u03D6\u03D5\x03\x02\x02\x02\u03D7\u03D8\x03\x02\x02\x02\u03D8\u03D6\x03" +
    "\x02\x02\x02\u03D8\u03D9\x03\x02\x02\x02\u03D9\u03DA\x03\x02\x02\x02\u03DA" +
    "\u03DC\t\x1D\x02\x02\u03DB\u03DD\x05<\x1F\x02\u03DC\u03DB\x03\x02\x02" +
    "\x02\u03DC\u03DD\x03\x02\x02\x02\u03DD\xDF\x03\x02\x02\x02\u03DE\u03E0" +
    "\t\x1E\x02\x02\u03DF\u03E1\x05\b\x05\x02\u03E0\u03DF\x03\x02\x02\x02\u03E1" +
    "\u03E2\x03\x02\x02\x02\u03E2\u03E0\x03\x02\x02\x02\u03E2\u03E3\x03\x02" +
    "\x02\x02\u03E3\u03E4\x03\x02\x02\x02\u03E4\u03E5\x05\x0E\b\x02\u03E5\xE1" +
    "\x03\x02\x02\x02\u03E6\u03E7\x07t\x02\x02\u03E7\u03E9\x05\b\x05\x02\u03E8" +
    "\u03EA\t\v\x02\x02\u03E9\u03E8\x03\x02\x02\x02\u03E9\u03EA\x03\x02\x02" +
    "\x02\u03EA\u03EC\x03\x02\x02\x02\u03EB\u03ED\t\x1F\x02\x02\u03EC\u03EB" +
    "\x03\x02\x02\x02\u03EC\u03ED\x03\x02\x02\x02\u03ED\u03EF\x03\x02\x02\x02" +
    "\u03EE\u03F0\x07\u0127\x02\x02\u03EF\u03EE\x03\x02\x02\x02\u03EF\u03F0" +
    "\x03\x02\x02\x02\u03F0\u03F2\x03\x02\x02\x02\u03F1\u03F3\x07+\x02\x02" +
    "\u03F2\u03F1\x03\x02\x02\x02\u03F2\u03F3\x03\x02\x02\x02\u03F3\u03F5\x03" +
    "\x02\x02\x02\u03F4\u03F6\x05&\x14\x02\u03F5\u03F4\x03\x02\x02\x02\u03F6" +
    "\u03F7\x03\x02\x02\x02\u03F7\u03F5\x03\x02\x02\x02\u03F7\u03F8\x03\x02" +
    "\x02\x02\u03F8\xE3\x03\x02\x02\x02\u03F9\u03FA\x07u\x02\x02\u03FA\u03FB" +
    "\x05\b\x05\x02\u03FB\xE5\x03\x02\x02\x02\u03FC\u03FD\t \x02\x02\u03FD" +
    "\u03FE\x05\b\x05\x02\u03FE\u03FF\x05\x16\f\x02\u03FF\u0400\x05\x18\r\x02" +
    "\u0400\xE7\x03\x02\x02\x02\u0401\u0402\x07w\x02\x02\u0402\u0404\x05\x0E" +
    "\b\x02\u0403\u0405\x05\b\x05\x02\u0404\u0403\x03\x02\x02\x02\u0405\u0406" +
    "\x03\x02\x02\x02\u0406\u0404\x03\x02\x02\x02\u0406\u0407\x03\x02\x02\x02" +
    "\u0407\u0409\x03\x02\x02\x02\u0408\u040A\x07\u0111\x02\x02\u0409\u0408" +
    "\x03\x02\x02\x02\u0409\u040A\x03\x02\x02\x02\u040A\xE9\x03\x02\x02\x02" +
    "\u040B\u040C\x07x\x02\x02\u040C\u040D\x05\b\x05\x02\u040D\u040F\x05\x0E" +
    "\b\x02\u040E\u0410\x05\b\x05\x02\u040F\u040E\x03\x02\x02\x02\u0410\u0411" +
    "\x03\x02\x02\x02\u0411\u040F\x03\x02\x02\x02\u0411\u0412\x03\x02\x02\x02" +
    "\u0412\xEB\x03\x02\x02\x02\u0413\u0414\x07y\x02\x02\u0414\u0415\x05\b" +
    "\x05\x02\u0415\u0416\x05\x0E\b\x02\u0416\u0417\x05\n\x06\x02\u0417\xED" +
    "\x03\x02\x02\x02\u0418\u0419\x07z\x02\x02\u0419\u041B\x05\x0E\b\x02\u041A" +
    "\u041C\x05\b\x05\x02\u041B\u041A\x03\x02\x02\x02\u041C\u041D\x03\x02\x02" +
    "\x02\u041D\u041B\x03\x02\x02\x02\u041D\u041E\x03\x02\x02\x02\u041E\u0425" +
    "\x03\x02\x02\x02\u041F\u0421\x07\u0128\x02\x02\u0420\u0422\x05\x0E\b\x02" +
    "\u0421\u0420\x03\x02\x02\x02\u0422\u0423\x03\x02\x02\x02\u0423\u0421\x03" +
    "\x02\x02\x02\u0423\u0424\x03\x02\x02\x02\u0424\u0426\x03\x02\x02\x02\u0425" +
    "\u041F\x03\x02\x02\x02\u0425\u0426\x03\x02\x02\x02\u0426\u0429\x03\x02" +
    "\x02\x02\u0427\u0428\x07\u0129\x02\x02\u0428\u042A\t!\x02\x02\u0429\u0427" +
    "\x03\x02\x02\x02\u0429\u042A\x03\x02\x02\x02\u042A\u042C\x03\x02\x02\x02" +
    "\u042B\u042D\x07\u0111\x02\x02\u042C\u042B\x03\x02\x02";
RedisParser._serializedATNSegment2 = "\x02\u042C\u042D\x03\x02\x02\x02\u042D\xEF\x03\x02\x02\x02\u042E\u042F" +
    "\x07{\x02\x02\u042F\u0431\x05\x0E\b\x02\u0430\u0432\x05\b\x05\x02\u0431" +
    "\u0430\x03\x02\x02\x02\u0432\u0433\x03\x02\x02\x02\u0433\u0431\x03\x02" +
    "\x02\x02\u0433\u0434\x03\x02\x02\x02\u0434\u0437\x03\x02\x02\x02\u0435" +
    "\u0436\x07\u0112\x02\x02\u0436\u0438\x05\x0E\b\x02\u0437\u0435\x03\x02" +
    "\x02\x02\u0437\u0438\x03\x02\x02\x02\u0438\xF1\x03\x02\x02\x02\u0439\u043A" +
    "\x07|\x02\x02\u043A\u043B\x05\b\x05\x02\u043B\u043D\x05\x0E\b\x02\u043C" +
    "\u043E\x05\b\x05\x02\u043D\u043C\x03\x02\x02\x02\u043E\u043F\x03\x02\x02" +
    "\x02\u043F\u043D\x03\x02\x02\x02\u043F\u0440\x03\x02\x02\x02\u0440\u0447" +
    "\x03\x02\x02\x02\u0441\u0443\x07\u0128\x02\x02\u0442\u0444\x05\x0E\b\x02" +
    "\u0443\u0442\x03\x02\x02\x02\u0444\u0445\x03\x02\x02\x02\u0445\u0443\x03" +
    "\x02\x02\x02\u0445\u0446\x03\x02\x02\x02\u0446\u0448\x03\x02\x02\x02\u0447" +
    "\u0441\x03\x02\x02\x02\u0447\u0448\x03\x02\x02\x02\u0448\u044B\x03\x02" +
    "\x02\x02\u0449\u044A\x07\u0129\x02\x02\u044A\u044C\t!\x02\x02\u044B\u0449" +
    "\x03\x02\x02\x02\u044B\u044C\x03\x02\x02\x02\u044C\xF3\x03\x02\x02\x02" +
    "\u044D\u044E\x07~\x02\x02\u044E\u0450\x05\x0E\b\x02\u044F\u0451\x05\b" +
    "\x05\x02\u0450\u044F\x03\x02\x02\x02\u0451\u0452\x03\x02\x02\x02\u0452" +
    "\u0450\x03\x02\x02\x02\u0452\u0453\x03\x02\x02\x02\u0453\u0454\x03\x02" +
    "\x02\x02\u0454\u0455\t\x1D\x02\x02\u0455\u0456\x05<\x1F\x02\u0456\xF5" +
    "\x03\x02\x02\x02\u0457\u0458\t\"\x02\x02\u0458\u045A\x05\b\x05\x02\u0459" +
    "\u045B\x05\n\x06\x02\u045A\u0459\x03\x02\x02\x02\u045B\u045C\x03\x02\x02" +
    "\x02\u045C\u045A\x03\x02\x02\x02\u045C\u045D\x03\x02\x02\x02\u045D\xF7" +
    "\x03\x02\x02\x02\u045E\u045F\t#\x02\x02\u045F\u0461\x05\b\x05\x02\u0460" +
    "\u0462\x05\x0E\b\x02\u0461\u0460\x03\x02\x02\x02\u0461\u0462\x03\x02\x02" +
    "\x02\u0462\xF9\x03\x02\x02\x02\u0463\u0464\x07\x82\x02\x02\u0464\u0469" +
    "\x05\b\x05\x02\u0465\u0467\x05\x0E\b\x02\u0466\u0468\x07\u0111\x02\x02" +
    "\u0467\u0466\x03\x02\x02\x02\u0467\u0468\x03\x02\x02\x02\u0468\u046A\x03" +
    "\x02\x02\x02\u0469\u0465\x03\x02\x02\x02\u0469\u046A\x03\x02\x02\x02\u046A" +
    "\xFB\x03\x02\x02\x02\u046B\u046C\x07\x83\x02\x02\u046C\u046D\x05\b\x05" +
    "\x02\u046D\u046E\x05\x16\f\x02\u046E\u0470\x05\x18\r\x02\u046F\u0471\t" +
    "$\x02\x02\u0470\u046F\x03\x02\x02\x02\u0470\u0471\x03\x02\x02\x02\u0471" +
    "\u0473\x03\x02\x02\x02\u0472\u0474\x07\u012D\x02\x02\u0473\u0472\x03\x02" +
    "\x02\x02\u0473\u0474\x03\x02\x02\x02\u0474\u0476\x03\x02\x02\x02\u0475" +
    "\u0477\x052\x1A\x02\u0476\u0475\x03\x02\x02\x02\u0476\u0477\x03\x02\x02" +
    "\x02\u0477\u0479\x03\x02\x02\x02\u0478\u047A\x07\u0111\x02\x02\u0479\u0478" +
    "\x03\x02\x02\x02\u0479\u047A\x03\x02\x02\x02\u047A\xFD\x03\x02\x02\x02" +
    "\u047B\u047C\x07\x84\x02\x02\u047C\u047D\x05\b\x05\x02\u047D\u047E\x05" +
    "\x16\f\x02\u047E\u0480\x05\x18\r\x02\u047F\u0481\x052\x1A\x02\u0480\u047F" +
    "\x03\x02\x02\x02\u0480\u0481\x03\x02\x02\x02\u0481\xFF\x03\x02\x02\x02" +
    "\u0482\u0483\x07\x85\x02\x02\u0483\u0484\x05\b\x05\x02\u0484\u0485\x05" +
    "\x16\f\x02\u0485\u0487\x05\x18\r\x02\u0486\u0488\x07\u0111\x02\x02\u0487" +
    "\u0486\x03\x02\x02\x02\u0487\u0488\x03\x02\x02\x02\u0488\u048A\x03\x02" +
    "\x02\x02\u0489\u048B\x052\x1A\x02\u048A\u0489\x03\x02\x02\x02\u048A\u048B" +
    "\x03\x02\x02\x02\u048B\u0101\x03\x02\x02\x02\u048C\u048D\x07\x86\x02\x02" +
    "\u048D\u048E\x05\b\x05\x02\u048E\u048F\x05\b\x05\x02\u048F\u0490\x05\x16" +
    "\f\x02\u0490\u0492\x05\x18\r\x02\u0491\u0493\t$\x02\x02\u0492\u0491\x03" +
    "\x02\x02\x02\u0492\u0493\x03\x02\x02\x02\u0493\u0495\x03\x02\x02\x02\u0494" +
    "\u0496\x052\x1A\x02\u0495\u0494\x03\x02\x02\x02\u0495\u0496\x03\x02\x02" +
    "\x02\u0496\u0103\x03\x02\x02\x02\u0497\u0498\t%\x02\x02\u0498\u0499\x05" +
    "\b\x05\x02\u0499\u049A\x05\n\x06\x02\u049A\u0105\x03\x02\x02\x02\u049B" +
    "\u049C\x07\x8A\x02\x02\u049C\u049D\x05\b\x05\x02\u049D\u049E\x05\x0E\b" +
    "\x02\u049E\u049F\x05\x0E\b\x02\u049F\u0107\x03\x02\x02\x02\u04A0\u04A1" +
    "\x07\x8C\x02\x02\u04A1\u04A2\x05\b\x05\x02\u04A2\u04A3\x05\x0E\b\x02\u04A3" +
    "\u04A5\x05\x0E\b\x02\u04A4\u04A6\x07\u0111\x02\x02\u04A5\u04A4\x03\x02" +
    "\x02\x02\u04A5\u04A6\x03\x02\x02\x02\u04A6\u0109\x03\x02\x02\x02\u04A7" +
    "\u04A8\t&\x02\x02\u04A8\u04A9\x05\b\x05\x02\u04A9\u04AA\x05\x18\r\x02" +
    "\u04AA\u04AC\x05\x16\f\x02\u04AB\u04AD\x07\u0111\x02\x02\u04AC\u04AB\x03" +
    "\x02\x02\x02\u04AC\u04AD\x03\x02\x02\x02\u04AD\u04AF\x03\x02\x02\x02\u04AE" +
    "\u04B0\x052\x1A\x02\u04AF\u04AE\x03\x02\x02\x02\u04AF\u04B0\x03\x02\x02" +
    "\x02\u04B0\u010B\x03\x02\x02\x02\u04B1\u04B2\x07\x90\x02\x02\u04B2\u04B3" +
    "\x05\b\x05\x02\u04B3\u04B6\x05\x0E\b\x02\u04B4\u04B5\x07\u0124\x02\x02" +
    "\u04B5\u04B7\x05\x12\n\x02\u04B6\u04B4\x03\x02\x02\x02\u04B6\u04B7\x03" +
    "\x02\x02\x02\u04B7\u04B8\x03\x02\x02\x02\u04B8\u04B9\x05<\x1F\x02\u04B9" +
    "\u010D\x03\x02\x02\x02\u04BA\u04BB\x07\x92\x02\x02\u04BB\u04BD\x05\x0E" +
    "\b\x02\u04BC\u04BE\x05\b\x05\x02\u04BD\u04BC\x03\x02\x02\x02\u04BE\u04BF" +
    "\x03\x02\x02\x02\u04BF\u04BD\x03\x02\x02\x02\u04BF\u04C0\x03\x02\x02\x02" +
    "\u04C0\u04C7\x03\x02\x02\x02\u04C1\u04C3\x07\u0128\x02\x02\u04C2\u04C4" +
    "\x05\x0E\b\x02\u04C3\u04C2\x03\x02\x02\x02\u04C4\u04C5\x03\x02\x02\x02" +
    "\u04C5\u04C3\x03\x02\x02\x02\u04C5\u04C6\x03\x02\x02\x02\u04C6\u04C8\x03" +
    "\x02\x02\x02\u04C7\u04C1\x03\x02\x02\x02\u04C7\u04C8\x03\x02\x02\x02\u04C8" +
    "\u04CB\x03\x02\x02\x02\u04C9\u04CA\x07\u0129\x02\x02\u04CA\u04CC\t!\x02" +
    "\x02\u04CB\u04C9\x03\x02\x02\x02\u04CB\u04CC\x03\x02\x02\x02\u04CC\u04CE" +
    "\x03\x02\x02\x02\u04CD\u04CF\x07\u0111\x02\x02\u04CE\u04CD\x03\x02\x02" +
    "\x02\u04CE\u04CF\x03\x02\x02\x02\u04CF\u010F\x03\x02\x02\x02\u04D0\u04D1" +
    "\x07\x93\x02\x02\u04D1\u04D2\x05\b\x05\x02\u04D2\u04D4\x05\x0E\b\x02\u04D3" +
    "\u04D5\x05\b\x05\x02\u04D4\u04D3\x03\x02\x02\x02\u04D5\u04D6\x03\x02\x02" +
    "\x02\u04D6\u04D4\x03\x02\x02\x02\u04D6\u04D7\x03\x02\x02\x02\u04D7\u04DE" +
    "\x03\x02\x02\x02\u04D8\u04DA\x07\u0128\x02\x02\u04D9\u04DB\x05\x0E\b\x02" +
    "\u04DA\u04D9\x03\x02\x02\x02\u04DB\u04DC\x03\x02\x02\x02\u04DC\u04DA\x03" +
    "\x02\x02\x02\u04DC\u04DD\x03\x02\x02\x02\u04DD\u04DF\x03\x02\x02\x02\u04DE" +
    "\u04D8\x03\x02\x02\x02\u04DE\u04DF\x03\x02\x02\x02\u04DF\u04E2\x03\x02" +
    "\x02\x02\u04E0\u04E1\x07\u0129\x02\x02\u04E1\u04E3\t!\x02\x02\u04E2\u04E0" +
    "\x03\x02\x02\x02\u04E2\u04E3\x03\x02\x02\x02\u04E3\u0111\x03\x02\x02\x02" +
    "\u04E4\u04E5\x07\xD6\x02\x02\u04E5\u04E6\x07\u0121\x02\x02\u04E6\u0113" +
    "\x03\x02\x02\x02\u04E7\u04E8\x07\xD6\x02\x02\u04E8\u04E9\x07\u0122\x02" +
    "\x02\u04E9\u04EA\x05\x1A\x0E\x02\u04EA\u0115\x03\x02\x02\x02\u04EB\u04EC" +
    "\x07\xD7\x02\x02\u04EC\u04ED\x05\x1A\x0E\x02\u04ED\u04F1\x05\x0E\b\x02" +
    "\u04EE\u04F0\x05\b\x05\x02\u04EF\u04EE\x03\x02\x02\x02\u04F0\u04F3\x03" +
    "\x02\x02\x02\u04F1\u04EF\x03\x02\x02\x02\u04F1\u04F2\x03\x02\x02\x02\u04F2" +
    "\u0117\x03\x02\x02\x02\u04F3\u04F1\x03\x02\x02\x02\u04F4\u04F5\x07\xD8" +
    "\x02\x02\u04F5\u04F6\x05\f\x07\x02\u04F6\u04FA\x05\x0E\b\x02\u04F7\u04F9" +
    "\x05\b\x05\x02\u04F8\u04F7\x03\x02\x02\x02\u04F9\u04FC\x03\x02\x02\x02" +
    "\u04FA\u04F8\x03\x02\x02\x02\u04FA\u04FB\x03\x02\x02\x02\u04FB\u0119\x03" +
    "\x02\x02\x02\u04FC\u04FA\x03\x02\x02\x02\u04FD\u04FE\x07\xD6\x02\x02\u04FE" +
    "\u04FF\x07\n\x02\x02\u04FF\u0500\x05\f\x07\x02\u0500\u011B\x03\x02\x02" +
    "\x02\u0501\u0502\x07\xD6\x02\x02\u0502\u0503\x07\u0115\x02\x02\u0503\u011D" +
    "\x03\x02\x02\x02\u0504\u0505\x07\xD9\x02\x02\u0505\u011F\x03\x02\x02\x02" +
    "\u0506\u0508\x07\xDA\x02\x02\u0507\u0509\x05\b\x05\x02\u0508\u0507\x03" +
    "\x02\x02\x02\u0509\u050A\x03\x02\x02\x02\u050A\u0508\x03\x02\x02\x02\u050A" +
    "\u050B\x03\x02\x02\x02\u050B\u0121\x03\x02\x02\x02\u050C\u050D\x07\xDB" +
    "\x02\x02\u050D\u0123\x03\x02\x02\x02\u050E\u050F\x07\xDC\x02\x02\u050F" +
    "\u0125\x03\x02\x02\x02\u0510\u0511\x07\xDD\x02\x02\u0511\u0127\x03\x02" +
    "\x02\x02\u0512\u0513\x07\xDE\x02\x02\u0513\u0515\x05\b\x05\x02\u0514\u0516" +
    "\x05\b\x05\x02\u0515\u0514\x03\x02\x02\x02\u0516\u0517\x03\x02\x02\x02" +
    "\u0517\u0515\x03\x02\x02\x02\u0517\u0518\x03\x02\x02\x02\u0518\u0129\x03" +
    "\x02\x02\x02\u0519\u051A\x07\xDF\x02\x02\u051A\u051C\x05\b\x05\x02\u051B" +
    "\u051D\x05\n\x06\x02\u051C\u051B\x03\x02\x02\x02\u051D\u051E\x03\x02\x02" +
    "\x02\u051E\u051C\x03\x02\x02\x02\u051E\u051F\x03\x02\x02\x02\u051F\u012B" +
    "\x03\x02\x02\x02\u0520\u0521\x07\xE0\x02\x02\u0521\u0523\x05\b\x05\x02" +
    "\u0522\u0524\x05\b\x05\x02\u0523\u0522\x03\x02\x02\x02\u0524\u0525\x03" +
    "\x02\x02\x02\u0525\u0523\x03\x02\x02\x02\u0525\u0526\x03\x02\x02\x02\u0526" +
    "\u012D\x03\x02\x02\x02\u0527\u0529\t\'\x02\x02\u0528\u052A\x05\b\x05\x02" +
    "\u0529\u0528\x03\x02\x02\x02\u052A\u052B\x03\x02\x02\x02\u052B\u0529\x03" +
    "\x02\x02\x02\u052B\u052C\x03\x02\x02\x02\u052C\u012F\x03\x02\x02\x02\u052D" +
    "\u0533\t(\x02\x02\u052E\u0530\x05\b\x05\x02\u052F\u052E\x03\x02\x02\x02" +
    "\u0530\u0531\x03\x02\x02\x02\u0531\u052F\x03\x02\x02\x02\u0531\u0532\x03" +
    "\x02\x02\x02\u0532\u0534\x03\x02\x02\x02\u0533\u052F\x03\x02\x02\x02\u0533" +
    "\u0534\x03\x02\x02\x02\u0534\u0131\x03\x02\x02\x02\u0535\u0536\x07\x95" +
    "\x02\x02\u0536\u0537\x07\x99\x02\x02\u0537\u0133\x03\x02\x02\x02\u0538" +
    "\u0539\x07\x95\x02\x02\u0539\u053F\t)\x02\x02\u053A\u053C\x05\b\x05\x02" +
    "\u053B\u053A\x03\x02\x02\x02\u053C\u053D\x03\x02\x02\x02\u053D\u053B\x03" +
    "\x02\x02\x02\u053D\u053E\x03\x02\x02\x02\u053E\u0540\x03\x02\x02\x02\u053F" +
    "\u053B\x03\x02\x02\x02\u053F\u0540\x03\x02\x02\x02\u0540\u0135\x03\x02" +
    "\x02\x02\u0541\u0542\x07\x95\x02\x02\u0542\u0544\t*\x02\x02\u0543\u0545" +
    "\x05\b\x05\x02\u0544\u0543\x03\x02\x02\x02\u0544\u0545\x03\x02\x02\x02" +
    "\u0545\u0137\x03\x02\x02\x02\u0546\u0547\x07\x96\x02\x02\u0547\u0548\x05" +
    "\b\x05\x02\u0548\u0549\x05\n\x06\x02\u0549\u0139\x03\x02\x02\x02\u054A" +
    "\u054B\x07\xB2\x02\x02\u054B\u013B\x03\x02\x02\x02\u054C\u054D\x07\xD3" +
    "\x02\x02\u054D\u013D\x03\x02\x02\x02\u054E\u054F\x07\xD4\x02\x02\u054F" +
    "\u013F\x03\x02\x02\x02\u0550\u0551\x07\xB3\x02\x02\u0551\u0553\t+\x02" +
    "\x02\u0552\u0554\x05\x0E\b\x02\u0553\u0552\x03\x02\x02\x02\u0554\u0555" +
    "\x03\x02\x02\x02\u0555\u0553\x03\x02\x02\x02\u0555\u0556\x03\x02\x02\x02" +
    "\u0556\u0141\x03\x02\x02\x02\u0557\u0558\x07\xB3\x02\x02\u0558\u0559\x07" +
    "\xB8\x02\x02\u0559\u055A\x05\x0E\b\x02\u055A\u0143\x03\x02\x02\x02\u055B" +
    "\u055C\x07\xB3\x02\x02\u055C\u055E\x07\xBA\x02\x02\u055D\u055F\t,\x02" +
    "\x02\u055E\u055D\x03\x02\x02\x02\u055E\u055F\x03\x02\x02\x02\u055F\u0145" +
    "\x03\x02\x02\x02\u0560\u0561\x07\xB3\x02\x02\u0561\u0562\x07\xBC\x02\x02" +
    "\u0562\u0563\x05\f\x07\x02\u0563\u0147\x03\x02\x02\x02\u0564\u0565\x07" +
    "\xB3\x02\x02\u0565\u0566\x07\xBD\x02\x02\u0566\u0567\x05\x0E\b\x02\u0567" +
    "\u0568\x05\x1C\x0F\x02\u0568\u0149\x03\x02\x02\x02\u0569\u056A\x07\xB3" +
    "\x02\x02\u056A\u056B\t-\x02\x02\u056B\u014B\x03\x02\x02\x02\u056C\u056D" +
    "\x07\xB3\x02\x02\u056D\u056E\x07\xBE\x02\x02\u056E\u056F\x05\b\x05\x02" +
    "\u056F\u014D\x03\x02\x02\x02\u0570\u0571\x07\xB3\x02\x02\u0571\u0572\x07" +
    "\xC0\x02\x02\u0572\u0573\x07\u0139\x02\x02\u0573\u0575\x05\"\x12\x02\u0574" +
    "\u0576\x05\x0E\b\x02\u0575\u0574\x03\x02\x02\x02\u0575\u0576\x03\x02\x02" +
    "\x02\u0576\u014F\x03\x02\x02\x02\u0577\u0578\x07\xB3\x02\x02\u0578\u0579" +
    "\t.\x02\x02\u0579\u057A\x05\f\x07\x02\u057A\u0151\x03\x02\x02\x02\u057B" +
    "\u057C\x07\xB3\x02\x02\u057C\u057D\x07\xC9\x02\x02\u057D\u057E\t/\x02" +
    "\x02\u057E\u0153\x03\x02\x02\x02\u057F\u0580\x07\xB3\x02\x02\u0580\u0581" +
    "\x07\xCD\x02\x02\u0581\u058C\x05\x0E\b\x02\u0582\u0583\x07\xCF\x02\x02" +
    "\u0583\u058D\x05\f\x07\x02\u0584\u0585\x07\xCE\x02\x02\u0585\u0588\x05" +
    "\f\x07\x02\u0586\u0588\x03\x02\x02\x02\u0587\u0584\x03\x02\x02\x02\u0587" +
    "\u0586\x03\x02\x02\x02\u0588\u058D\x03\x02\x02\x02\u0589\u058A\x07\xC5" +
    "\x02\x02\u058A\u058D\x05\f\x07\x02\u058B\u058D\x07\xD0\x02\x02\u058C\u0582" +
    "\x03\x02\x02\x02\u058C\u0587\x03\x02\x02\x02\u058C\u0589\x03\x02\x02\x02" +
    "\u058C\u058B\x03\x02\x02\x02\u058D\u0155\x03\x02\x02\x02\u058E\u0590\x07" +
    "\xD5\x02\x02\u058F\u0591\t0\x02\x02\u0590\u058F\x03\x02\x02\x02\u0590" +
    "\u0591\x03\x02\x02\x02\u0591\u0157\x03\x02\x02\x02\u0592\u0593\x07\xE1" +
    "\x02\x02\u0593\u0594\x05\f\x07\x02\u0594\u0159\x03\x02\x02\x02\u0595\u0596" +
    "\x07\xE3\x02\x02\u0596\u015B\x03\x02\x02\x02\u0597\u0598\x07\xE4\x02\x02" +
    "\u0598\u0599\x05\u0188\xC5\x02\u0599\u015D\x03\x02\x02\x02\u059A\u059B" +
    "\x07\xE5\x02\x02\u059B\u015F\x03\x02\x02\x02\u059C\u059D\x07\xE6\x02\x02" +
    "\u059D\u059E\x07%\x02\x02\u059E\u05A6\x05\x12\n\x02\u059F\u05A0\x074\x02" +
    "\x02\u05A0\u05A1\x05\f\x07\x02\u05A1\u05A2\x05\f\x07\x02\u05A2\u05A6\x03" +
    "\x02\x02\x02\u05A3\u05A6\x07\u0116\x02\x02\u05A4\u05A6\x07\u0117\x02\x02" +
    "\u05A5\u059C\x03\x02\x02\x02\u05A5\u059F\x03\x02\x02\x02\u05A5\u05A3\x03" +
    "\x02\x02\x02\u05A5\u05A4\x03\x02\x02\x02\u05A6\u0161\x03\x02\x02\x02\u05A7" +
    "\u05A8\x07\xE7\x02\x02\u05A8\u05A9\x07\u0118\x02\x02\u05A9\u05B1\x05\x0E" +
    "\b\x02\u05AA\u05AB\x07\u0121\x02\x02\u05AB\u05B1\x07\u013A\x02\x02\u05AC" +
    "\u05B1\x07\u011A\x02\x02\u05AD\u05AE\x07\u0119\x02\x02\u05AE\u05B1\x05" +
    "\f\x07\x02\u05AF\u05B1\x07\u011B\x02\x02\u05B0\u05A7\x03\x02\x02\x02\u05B0" +
    "\u05AA\x03\x02\x02\x02\u05B0\u05AC\x03\x02\x02\x02\u05B0\u05AD\x03\x02" +
    "\x02\x02\u05B0\u05AF\x03\x02\x02\x02\u05B1\u0163\x03\x02\x02\x02\u05B2" +
    "\u05B4\x07\xE8\x02\x02\u05B3\u05B5\x07\u011C\x02\x02\u05B4\u05B3\x03\x02" +
    "\x02\x02\u05B4\u05B5\x03\x02\x02\x02\u05B5\u05B7\x03\x02\x02\x02\u05B6" +
    "\u05B8\x07\xE3\x02\x02\u05B7\u05B6\x03\x02\x02\x02\u05B7\u05B8\x03\x02" +
    "\x02\x02\u05B8\u0165\x03\x02\x02\x02\u05B9\u05BA\x07\xE9\x02\x02\u05BA" +
    "\u0167\x03\x02\x02\x02\u05BB\u05BC\x07\xEA\x02\x02\u05BC\u0169\x03\x02" +
    "\x02\x02\u05BD\u05BE\x07\xEB\x02\x02\u05BE\u016B\x03\x02\x02\x02\u05BF" +
    "\u05C0\x07\xEC\x02\x02\u05C0\u05C1\x07\u0139\x02\x02\u05C1\u05C2\x05\x0E" +
    "\b\x02\u05C2\u016D\x03\x02\x02\x02\u05C3\u05C4\x07\xED\x02\x02\u05C4\u016F" +
    "\x03\x02\x02\x02\u05C5\u05C6\x07\xEE\x02\x02\u05C6\u05C7\x05\x0E\b\x02" +
    "\u05C7\u0171\x03\x02\x02\x02\u05C8\u05C9\x07\xE2\x02\x02\u05C9\u0173\x03" +
    "\x02\x02\x02\u05CA\u05CB\x07\xEF\x02\x02\u05CB\u0175\x03\x02\x02\x02\u05CC" +
    "\u05CD\x07\xF0\x02\x02\u05CD\u05CE\x05\f\x07\x02\u05CE\u0177\x03\x02\x02" +
    "\x02\u05CF\u05D0\x07\xF1\x02\x02\u05D0\u0179\x03\x02\x02\x02\u05D1\u05D2" +
    "\x07\xF2\x02\x02\u05D2\u017B\x03\x02\x02\x02\u05D3\u05D4\x07\xF3\x02\x02" +
    "\u05D4\u017D\x03\x02\x02\x02\u05D5\u05DB\x07\xF4\x02\x02\u05D6\u05D8\x05" +
    "(\x15\x02\u05D7\u05D6\x03\x02\x02\x02\u05D8\u05D9\x03\x02\x02\x02\u05D9" +
    "\u05D7\x03\x02\x02\x02\u05D9\u05DA\x03\x02\x02\x02\u05DA\u05DC\x03\x02" +
    "\x02\x02\u05DB\u05D7\x03\x02\x02\x02\u05DB\u05DC\x03\x02\x02\x02\u05DC" +
    "\u017F\x03\x02\x02\x02\u05DD\u05DE\x07\xF5\x02\x02\u05DE\u0181\x03\x02" +
    "\x02\x02\u05DF\u05E0\x07\xF6\x02\x02\u05E0\u0183\x03\x02\x02\x02\u05E1" +
    "\u05E2\x07\xF6\x02\x02\u05E2\u05E4\x07\xF4\x02\x02\u05E3\u05E5\x05\b\x05" +
    "\x02\u05E4\u05E3\x03\x02\x02\x02\u05E5\u05E6\x03\x02\x02\x02\u05E6\u05E4" +
    "\x03\x02\x02\x02\u05E6\u05E7\x03\x02\x02\x02\u05E7\u05F0\x03\x02\x02\x02" +
    "\u05E8\u05EA\x07\u011E\x02\x02\u05E9\u05EB\x05\b\x05\x02\u05EA\u05E9\x03" +
    "\x02\x02\x02\u05EB\u05EC\x03\x02\x02\x02\u05EC\u05EA\x03\x02\x02\x02\u05EC" +
    "\u05ED\x03\x02\x02\x02\u05ED\u05F0\x03\x02\x02\x02\u05EE\u05F0\x07\u011F" +
    "\x02\x02\u05EF\u05E1\x03\x02\x02\x02\u05EF\u05E8\x03\x02\x02\x02\u05EF" +
    "\u05EE\x03\x02\x02\x02\u05F0\u0185\x03\x02\x02\x02\u05F1\u05F5\x07\xF7" +
    "\x02\x02\u05F2\u05F6\x07\u0120\x02\x02\u05F3\u05F4\x07\x10\x02\x02\u05F4" +
    "\u05F6\x05\b\x05\x02\u05F5\u05F2\x03\x02\x02\x02\u05F5\u05F3\x03\x02\x02" +
    "\x02\u05F6\u0187\x03\x02\x02\x02\u05F7\u069D\x05@!\x02\u05F8\u069D\x05" +
    "B\"\x02\u05F9\u069D\x05D#\x02\u05FA\u069D\x05F$\x02\u05FB\u069D\x05H%" +
    "\x02\u05FC\u069D\x05J&\x02\u05FD\u069D\x05L\'\x02\u05FE\u069D\x05N(\x02" +
    "\u05FF\u069D\x05P)\x02\u0600\u069D\x05R*\x02\u0601\u069D\x05T+\x02\u0602" +
    "\u069D\x05V,\x02\u0603\u069D\x05X-\x02\u0604\u069D\x05Z.\x02\u0605\u069D" +
    "\x05\\/\x02\u0606\u069D\x05^0\x02\u0607\u069D\x05`1\x02\u0608\u069D\x05" +
    "b2\x02\u0609\u069D\x05d3\x02\u060A\u069D\x05f4\x02\u060B\u069D\x05h5\x02" +
    "\u060C\u069D\x05j6\x02\u060D\u069D\x05l7\x02\u060E\u069D\x05n8\x02\u060F" +
    "\u069D\x05p9\x02\u0610\u069D\x05r:\x02\u0611\u069D\x05t;\x02\u0612\u069D" +
    "\x05v<\x02\u0613\u069D\x05x=\x02\u0614\u069D\x05z>\x02\u0615\u069D\x05" +
    "|?\x02\u0616\u069D\x05~@\x02\u0617\u069D\x05\x80A\x02\u0618\u069D\x05" +
    "\x82B\x02\u0619\u069D\x05\x84C\x02\u061A\u069D\x05\x86D\x02\u061B\u069D" +
    "\x05\x88E\x02\u061C\u069D\x05\x8AF\x02\u061D\u069D\x05\x8CG\x02\u061E" +
    "\u069D\x05\x8EH\x02\u061F\u069D\x05\x90I\x02\u0620\u069D\x05\x92J\x02" +
    "\u0621\u069D\x05\x94K\x02\u0622\u069D\x05\x96L\x02\u0623\u069D\x05\x98" +
    "M\x02\u0624\u069D\x05\x9AN\x02\u0625\u069D\x05\x9CO\x02\u0626\u069D\x05" +
    "\x9EP\x02\u0627\u069D\x05\xA0Q\x02\u0628\u069D\x05\xA2R\x02\u0629\u069D" +
    "\x05\xA4S\x02\u062A\u069D\x05\xA8U\x02\u062B\u069D\x05\xA6T\x02\u062C" +
    "\u069D\x05\xAAV\x02\u062D\u069D\x05\xACW\x02\u062E\u069D\x05\xAEX\x02" +
    "\u062F\u069D\x05\xB0Y\x02\u0630\u069D\x05\xB2Z\x02\u0631\u069D\x05\xB4" +
    "[\x02\u0632\u069D\x05\xB6\\\x02\u0633\u069D\x05\xB8]\x02\u0634\u069D\x05" +
    "\xBA^\x02\u0635\u069D\x05\xBC_\x02\u0636\u069D\x05\xBE`\x02\u0637\u069D" +
    "\x05\xC0a\x02\u0638\u069D\x05\xC2b\x02\u0639\u069D\x05\xC4c\x02\u063A" +
    "\u069D\x05\xC6d\x02\u063B\u069D\x05\xC8e\x02\u063C\u069D\x05\xCAf\x02" +
    "\u063D\u069D\x05\xCCg\x02\u063E\u069D\x05\xCEh\x02\u063F\u069D\x05\xD0" +
    "i\x02\u0640\u069D\x05\xD2j\x02\u0641\u069D\x05\xD4k\x02\u0642\u069D\x05" +
    "\xD6l\x02\u0643\u069D\x05\xD8m\x02\u0644\u069D\x05\xDAn\x02\u0645\u069D" +
    "\x05\xDCo\x02\u0646\u069D\x05\xDEp\x02\u0647\u069D\x05\xE0q\x02\u0648" +
    "\u069D\x05\xE2r\x02\u0649\u069D\x05\xE4s\x02\u064A\u069D\x05\xE6t\x02" +
    "\u064B\u069D\x05\xE8u\x02\u064C\u069D\x05\xEAv\x02\u064D\u069D\x05\xEC" +
    "w\x02\u064E\u069D\x05\xEEx\x02\u064F\u069D\x05\xF0y\x02\u0650\u069D\x05" +
    "\xF2z\x02\u0651\u069D\x05\xF4{\x02\u0652\u069D\x05\xF6|\x02\u0653\u069D" +
    "\x05\xF8}\x02\u0654\u069D\x05\xFA~\x02\u0655\u069D\x05\xFC\x7F\x02\u0656" +
    "\u069D\x05\xFE\x80\x02\u0657\u069D\x05\u0100\x81\x02\u0658\u069D\x05\u0102" +
    "\x82\x02\u0659\u069D\x05\u0104\x83\x02\u065A\u069D\x05\u0106\x84\x02\u065B" +
    "\u069D\x05\u0108\x85\x02\u065C\u069D\x05\u010A\x86\x02\u065D\u069D\x05" +
    "\u010C\x87\x02\u065E\u069D\x05\u010E\x88\x02\u065F\u069D\x05\u0110\x89" +
    "\x02\u0660\u069D\x05\u0112\x8A\x02\u0661\u069D\x05\u0114\x8B\x02\u0662" +
    "\u069D\x05\u0116\x8C\x02\u0663\u069D\x05\u0118\x8D\x02\u0664\u069D\x05" +
    "\u011A\x8E\x02\u0665\u069D\x05\u011C\x8F\x02\u0666\u069D\x05\u011E\x90" +
    "\x02\u0667\u069D\x05\u0120\x91\x02\u0668\u069D\x05\u0122\x92\x02\u0669" +
    "\u069D\x05\u0124\x93\x02\u066A\u069D\x05\u0126\x94\x02\u066B\u069D\x05" +
    "\u0128\x95\x02\u066C\u069D\x05\u012A\x96\x02\u066D\u069D\x05\u012C\x97" +
    "\x02\u066E\u069D\x05\u0158\xAD\x02\u066F\u069D\x05\u0170\xB9\x02\u0670" +
    "\u069D\x05\u0172\xBA\x02\u0671\u069D\x05\u015A\xAE\x02\u0672\u069D\x05" +
    "\u015C\xAF\x02\u0673\u069D\x05\u015E\xB0\x02\u0674\u069D\x05\u0160\xB1" +
    "\x02\u0675\u069D\x05\u0162\xB2\x02\u0676\u069D\x05\u0164\xB3\x02\u0677" +
    "\u069D\x05\u0166\xB4\x02\u0678\u069D\x05\u0168\xB5\x02\u0679\u069D\x05" +
    "\u016A\xB6\x02\u067A\u069D\x05\u016C\xB7\x02\u067B\u069D\x05\u016E\xB8" +
    "\x02\u067C\u069D\x05\u0174\xBB\x02\u067D\u069D\x05\u0176\xBC\x02\u067E" +
    "\u069D\x05\u0178\xBD\x02\u067F\u069D\x05\u017A\xBE\x02\u0680\u069D\x05" +
    "\u017C\xBF\x02\u0681\u069D\x05\u017E\xC0\x02\u0682\u069D\x05\u0180\xC1" +
    "\x02\u0683\u069D\x05\u0182\xC2\x02\u0684\u069D\x05\u0184\xC3\x02\u0685" +
    "\u069D\x05\u0186\xC4\x02\u0686\u069D\x05\u0156\xAC\x02\u0687\u069D\x05" +
    "\u012E\x98\x02\u0688\u069D\x05\u0130\x99\x02\u0689\u069D\x05\u0134\x9B" +
    "\x02\u068A\u069D\x05\u0132\x9A\x02\u068B\u069D\x05\u0136\x9C\x02\u068C" +
    "\u069D\x05\u0138\x9D\x02\u068D\u069D\x05\u017E\xC0\x02\u068E\u069D\x05" +
    "\u013A\x9E\x02\u068F\u069D\x05\u0140\xA1\x02\u0690\u069D\x05\u0142\xA2" +
    "\x02\u0691\u069D\x05\u0144\xA3\x02\u0692\u069D\x05\u0146\xA4\x02\u0693" +
    "\u069D\x05\u0148\xA5\x02\u0694\u069D\x05\u014A\xA6\x02\u0695\u069D\x05" +
    "\u014C\xA7\x02\u0696\u069D\x05\u014E\xA8\x02\u0697\u069D\x05\u013C\x9F" +
    "\x02\u0698\u069D\x05\u013E\xA0\x02\u0699\u069D\x05\u0150\xA9\x02\u069A" +
    "\u069D\x05\u0152\xAA\x02\u069B\u069D\x05\u0154\xAB\x02\u069C\u05F7\x03" +
    "\x02\x02\x02\u069C\u05F8\x03\x02\x02\x02\u069C\u05F9\x03\x02\x02\x02\u069C" +
    "\u05FA\x03\x02\x02\x02\u069C\u05FB\x03\x02\x02\x02\u069C\u05FC\x03\x02" +
    "\x02\x02\u069C\u05FD\x03\x02\x02\x02\u069C\u05FE\x03\x02\x02\x02\u069C" +
    "\u05FF\x03\x02\x02\x02\u069C\u0600\x03\x02\x02\x02\u069C\u0601\x03\x02" +
    "\x02\x02\u069C\u0602\x03\x02\x02\x02\u069C\u0603\x03\x02\x02\x02\u069C" +
    "\u0604\x03\x02\x02\x02\u069C\u0605\x03\x02\x02\x02\u069C\u0606\x03\x02" +
    "\x02\x02\u069C\u0607\x03\x02\x02\x02\u069C\u0608\x03\x02\x02\x02\u069C" +
    "\u0609\x03\x02\x02\x02\u069C\u060A\x03\x02\x02\x02\u069C\u060B\x03\x02" +
    "\x02\x02\u069C\u060C\x03\x02\x02\x02\u069C\u060D\x03\x02\x02\x02\u069C" +
    "\u060E\x03\x02\x02\x02\u069C\u060F\x03\x02\x02\x02\u069C\u0610\x03\x02" +
    "\x02\x02\u069C\u0611\x03\x02\x02\x02\u069C\u0612\x03\x02\x02\x02\u069C" +
    "\u0613\x03\x02\x02\x02\u069C\u0614\x03\x02\x02\x02\u069C\u0615\x03\x02" +
    "\x02\x02\u069C\u0616\x03\x02\x02\x02\u069C\u0617\x03\x02\x02\x02\u069C" +
    "\u0618\x03\x02\x02\x02\u069C\u0619\x03\x02\x02\x02\u069C\u061A\x03\x02" +
    "\x02\x02\u069C\u061B\x03\x02\x02\x02\u069C\u061C\x03\x02\x02\x02\u069C" +
    "\u061D\x03\x02\x02\x02\u069C\u061E\x03\x02\x02\x02\u069C\u061F\x03\x02" +
    "\x02\x02\u069C\u0620\x03\x02\x02\x02\u069C\u0621\x03\x02\x02\x02\u069C" +
    "\u0622\x03\x02\x02\x02\u069C\u0623\x03\x02\x02\x02\u069C\u0624\x03\x02" +
    "\x02\x02\u069C\u0625\x03\x02\x02\x02\u069C\u0626\x03\x02\x02\x02\u069C" +
    "\u0627\x03\x02\x02\x02\u069C\u0628\x03\x02\x02\x02\u069C\u0629\x03\x02" +
    "\x02\x02\u069C\u062A\x03\x02\x02\x02\u069C\u062B\x03\x02\x02\x02\u069C" +
    "\u062C\x03\x02\x02\x02\u069C\u062D\x03\x02\x02\x02\u069C\u062E\x03\x02" +
    "\x02\x02\u069C\u062F\x03\x02\x02\x02\u069C\u0630\x03\x02\x02\x02\u069C" +
    "\u0631\x03\x02\x02\x02\u069C\u0632\x03\x02\x02\x02\u069C\u0633\x03\x02" +
    "\x02\x02\u069C\u0634\x03\x02\x02\x02\u069C\u0635\x03\x02\x02\x02\u069C" +
    "\u0636\x03\x02\x02\x02\u069C\u0637\x03\x02\x02\x02\u069C\u0638\x03\x02" +
    "\x02\x02\u069C\u0639\x03\x02\x02\x02\u069C\u063A\x03\x02\x02\x02\u069C" +
    "\u063B\x03\x02\x02\x02\u069C\u063C\x03\x02\x02\x02\u069C\u063D\x03\x02" +
    "\x02\x02\u069C\u063E\x03\x02\x02\x02\u069C\u063F\x03\x02\x02\x02\u069C" +
    "\u0640\x03\x02\x02\x02\u069C\u0641\x03\x02\x02\x02\u069C\u0642\x03\x02" +
    "\x02\x02\u069C\u0643\x03\x02\x02\x02\u069C\u0644\x03\x02\x02\x02\u069C" +
    "\u0645\x03\x02\x02\x02\u069C\u0646\x03\x02\x02\x02\u069C\u0647\x03\x02" +
    "\x02\x02\u069C\u0648\x03\x02\x02\x02\u069C\u0649\x03\x02\x02\x02\u069C" +
    "\u064A\x03\x02\x02\x02\u069C\u064B\x03\x02\x02\x02\u069C\u064C\x03\x02" +
    "\x02\x02\u069C\u064D\x03\x02\x02\x02\u069C\u064E\x03\x02\x02\x02\u069C" +
    "\u064F\x03\x02\x02\x02\u069C\u0650\x03\x02\x02\x02\u069C\u0651\x03\x02" +
    "\x02\x02\u069C\u0652\x03\x02\x02\x02\u069C\u0653\x03\x02\x02\x02\u069C" +
    "\u0654\x03\x02\x02\x02\u069C\u0655\x03\x02\x02\x02\u069C\u0656\x03\x02" +
    "\x02\x02\u069C\u0657\x03\x02\x02\x02\u069C\u0658\x03\x02\x02\x02\u069C" +
    "\u0659\x03\x02\x02\x02\u069C\u065A\x03\x02\x02\x02\u069C\u065B\x03\x02" +
    "\x02\x02\u069C\u065C\x03\x02\x02\x02\u069C\u065D\x03\x02\x02\x02\u069C" +
    "\u065E\x03\x02\x02\x02\u069C\u065F\x03\x02\x02\x02\u069C\u0660\x03\x02" +
    "\x02\x02\u069C\u0661\x03\x02\x02\x02\u069C\u0662\x03\x02\x02\x02\u069C" +
    "\u0663\x03\x02\x02\x02\u069C\u0664\x03\x02\x02\x02\u069C\u0665\x03\x02" +
    "\x02\x02\u069C\u0666\x03\x02\x02\x02\u069C\u0667\x03\x02\x02\x02\u069C" +
    "\u0668\x03\x02\x02\x02\u069C\u0669\x03\x02\x02\x02\u069C\u066A\x03\x02" +
    "\x02\x02\u069C\u066B\x03\x02\x02\x02\u069C\u066C\x03\x02\x02\x02\u069C" +
    "\u066D\x03\x02\x02\x02\u069C\u066E\x03\x02\x02\x02\u069C\u066F\x03\x02" +
    "\x02\x02\u069C\u0670\x03\x02\x02\x02\u069C\u0671\x03\x02\x02\x02\u069C" +
    "\u0672\x03\x02\x02\x02\u069C\u0673\x03\x02\x02\x02\u069C\u0674\x03\x02" +
    "\x02\x02\u069C\u0675\x03\x02\x02\x02\u069C\u0676\x03\x02\x02\x02\u069C" +
    "\u0677\x03\x02\x02\x02\u069C\u0678\x03\x02\x02\x02\u069C\u0679\x03\x02" +
    "\x02\x02\u069C\u067A\x03\x02\x02\x02\u069C\u067B\x03\x02\x02\x02\u069C" +
    "\u067C\x03\x02\x02\x02\u069C\u067D\x03\x02\x02\x02\u069C\u067E\x03\x02" +
    "\x02\x02\u069C\u067F\x03\x02\x02\x02\u069C\u0680\x03\x02\x02\x02\u069C" +
    "\u0681\x03\x02\x02\x02\u069C\u0682\x03\x02\x02\x02\u069C";
RedisParser._serializedATNSegment3 = "\u0683\x03\x02\x02\x02\u069C\u0684\x03\x02\x02\x02\u069C\u0685\x03\x02" +
    "\x02\x02\u069C\u0686\x03\x02\x02\x02\u069C\u0687\x03\x02\x02\x02\u069C" +
    "\u0688\x03\x02\x02\x02\u069C\u0689\x03\x02\x02\x02\u069C\u068A\x03\x02" +
    "\x02\x02\u069C\u068B\x03\x02\x02\x02\u069C\u068C\x03\x02\x02\x02\u069C" +
    "\u068D\x03\x02\x02\x02\u069C\u068E\x03\x02\x02\x02\u069C\u068F\x03\x02" +
    "\x02\x02\u069C\u0690\x03\x02\x02\x02\u069C\u0691\x03\x02\x02\x02\u069C" +
    "\u0692\x03\x02\x02\x02\u069C\u0693\x03\x02\x02\x02\u069C\u0694\x03\x02" +
    "\x02\x02\u069C\u0695\x03\x02\x02\x02\u069C\u0696\x03\x02\x02\x02\u069C" +
    "\u0697\x03\x02\x02\x02\u069C\u0698\x03\x02\x02\x02\u069C\u0699\x03\x02" +
    "\x02\x02\u069C\u069A\x03\x02\x02\x02\u069C\u069B\x03\x02\x02\x02\u069D" +
    "\u0189\x03\x02\x02\x02\x94\u018B\u0191\u0193\u0198\u019B\u01E7\u01EA\u01F0" +
    "\u01F9\u01FF\u0205\u021C\u0222\u023C\u023F\u0242\u0245\u024B\u024F\u0255" +
    "\u0258\u025D\u025F\u0262\u0265\u0269\u026F\u0272\u0277\u0279\u027C\u027F" +
    "\u0285\u028E\u02A9\u02C2\u02C8\u02CB\u02CF\u02D2\u02D8\u02DE\u02E4\u02F0" +
    "\u02F3\u02F6\u0313\u0321\u032C\u032E\u0335\u0339\u034C\u0350\u0356\u0375" +
    "\u037B\u037E\u0381\u0388\u039D\u03A6\u03AD\u03B4\u03B8\u03C6\u03CD\u03D0" +
    "\u03D8\u03DC\u03E2\u03E9\u03EC\u03EF\u03F2\u03F7\u0406\u0409\u0411\u041D" +
    "\u0423\u0425\u0429\u042C\u0433\u0437\u043F\u0445\u0447\u044B\u0452\u045C" +
    "\u0461\u0467\u0469\u0470\u0473\u0476\u0479\u0480\u0487\u048A\u0492\u0495" +
    "\u04A5\u04AC\u04AF\u04B6\u04BF\u04C5\u04C7\u04CB\u04CE\u04D6\u04DC\u04DE" +
    "\u04E2\u04F1\u04FA\u050A\u0517\u051E\u0525\u052B\u0531\u0533\u053D\u053F" +
    "\u0544\u0555\u055E\u0575\u0587\u058C\u0590\u05A5\u05B0\u05B4\u05B7\u05D9" +
    "\u05DB\u05E6\u05EC\u05EF\u05F5\u069C";
RedisParser._serializedATN = Utils.join([
    RedisParser._serializedATNSegment0,
    RedisParser._serializedATNSegment1,
    RedisParser._serializedATNSegment2,
    RedisParser._serializedATNSegment3,
], "");
export class ProgramContext extends ParserRuleContext {
    EOF() { return this.getToken(RedisParser.EOF, 0); }
    sqlStatements() {
        return this.tryGetRuleContext(0, SqlStatementsContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_program; }
    // @Override
    enterRule(listener) {
        if (listener.enterProgram) {
            listener.enterProgram(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitProgram) {
            listener.exitProgram(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitProgram) {
            return visitor.visitProgram(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SqlStatementsContext extends ParserRuleContext {
    sqlStatement(i) {
        if (i === undefined) {
            return this.getRuleContexts(SqlStatementContext);
        }
        else {
            return this.getRuleContext(i, SqlStatementContext);
        }
    }
    emptyStatement_(i) {
        if (i === undefined) {
            return this.getRuleContexts(EmptyStatement_Context);
        }
        else {
            return this.getRuleContext(i, EmptyStatement_Context);
        }
    }
    SEMI() { return this.tryGetToken(RedisParser.SEMI, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_sqlStatements; }
    // @Override
    enterRule(listener) {
        if (listener.enterSqlStatements) {
            listener.enterSqlStatements(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSqlStatements) {
            listener.exitSqlStatements(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSqlStatements) {
            return visitor.visitSqlStatements(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class EmptyStatement_Context extends ParserRuleContext {
    SEMI() { return this.getToken(RedisParser.SEMI, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_emptyStatement_; }
    // @Override
    enterRule(listener) {
        if (listener.enterEmptyStatement_) {
            listener.enterEmptyStatement_(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitEmptyStatement_) {
            listener.exitEmptyStatement_(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitEmptyStatement_) {
            return visitor.visitEmptyStatement_(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class KeyNameContext extends ParserRuleContext {
    NAME_TOKEN() { return this.tryGetToken(RedisParser.NAME_TOKEN, 0); }
    STRING() { return this.tryGetToken(RedisParser.STRING, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_keyName; }
    // @Override
    enterRule(listener) {
        if (listener.enterKeyName) {
            listener.enterKeyName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitKeyName) {
            listener.exitKeyName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitKeyName) {
            return visitor.visitKeyName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class FieldNameContext extends ParserRuleContext {
    NAME_TOKEN() { return this.tryGetToken(RedisParser.NAME_TOKEN, 0); }
    STRING() { return this.tryGetToken(RedisParser.STRING, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_fieldName; }
    // @Override
    enterRule(listener) {
        if (listener.enterFieldName) {
            listener.enterFieldName(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitFieldName) {
            listener.exitFieldName(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitFieldName) {
            return visitor.visitFieldName(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class StringValueContext extends ParserRuleContext {
    STRING() { return this.getToken(RedisParser.STRING, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_stringValue; }
    // @Override
    enterRule(listener) {
        if (listener.enterStringValue) {
            listener.enterStringValue(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitStringValue) {
            listener.exitStringValue(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitStringValue) {
            return visitor.visitStringValue(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class IntValueContext extends ParserRuleContext {
    INTEGER_NUM() { return this.getToken(RedisParser.INTEGER_NUM, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_intValue; }
    // @Override
    enterRule(listener) {
        if (listener.enterIntValue) {
            listener.enterIntValue(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitIntValue) {
            listener.exitIntValue(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitIntValue) {
            return visitor.visitIntValue(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class FloatValueContext extends ParserRuleContext {
    DECIMAL_NUM() { return this.tryGetToken(RedisParser.DECIMAL_NUM, 0); }
    INTEGER_NUM() { return this.tryGetToken(RedisParser.INTEGER_NUM, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_floatValue; }
    // @Override
    enterRule(listener) {
        if (listener.enterFloatValue) {
            listener.enterFloatValue(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitFloatValue) {
            listener.exitFloatValue(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitFloatValue) {
            return visitor.visitFloatValue(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PatternContext extends ParserRuleContext {
    NAME_TOKEN() { return this.tryGetToken(RedisParser.NAME_TOKEN, 0); }
    STRING() { return this.tryGetToken(RedisParser.STRING, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_pattern; }
    // @Override
    enterRule(listener) {
        if (listener.enterPattern) {
            listener.enterPattern(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitPattern) {
            listener.exitPattern(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitPattern) {
            return visitor.visitPattern(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CursorContext extends ParserRuleContext {
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cursor; }
    // @Override
    enterRule(listener) {
        if (listener.enterCursor) {
            listener.enterCursor(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCursor) {
            listener.exitCursor(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCursor) {
            return visitor.visitCursor(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class MinContext extends ParserRuleContext {
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_min; }
    // @Override
    enterRule(listener) {
        if (listener.enterMin) {
            listener.enterMin(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitMin) {
            listener.exitMin(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitMin) {
            return visitor.visitMin(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class MaxContext extends ParserRuleContext {
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_max; }
    // @Override
    enterRule(listener) {
        if (listener.enterMax) {
            listener.enterMax(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitMax) {
            listener.exitMax(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitMax) {
            return visitor.visitMax(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class MultiStringContext extends ParserRuleContext {
    STRING() { return this.getToken(RedisParser.STRING, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_multiString; }
    // @Override
    enterRule(listener) {
        if (listener.enterMultiString) {
            listener.enterMultiString(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitMultiString) {
            listener.exitMultiString(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitMultiString) {
            return visitor.visitMultiString(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CoutContext extends ParserRuleContext {
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cout; }
    // @Override
    enterRule(listener) {
        if (listener.enterCout) {
            listener.enterCout(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCout) {
            listener.exitCout(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCout) {
            return visitor.visitCout(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class StartContext extends ParserRuleContext {
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_start; }
    // @Override
    enterRule(listener) {
        if (listener.enterStart) {
            listener.enterStart(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitStart) {
            listener.exitStart(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitStart) {
            return visitor.visitStart(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class EndContext extends ParserRuleContext {
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_end; }
    // @Override
    enterRule(listener) {
        if (listener.enterEnd) {
            listener.enterEnd(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitEnd) {
            listener.exitEnd(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitEnd) {
            return visitor.visitEnd(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PortContext extends ParserRuleContext {
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_port; }
    // @Override
    enterRule(listener) {
        if (listener.enterPort) {
            listener.enterPort(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitPort) {
            listener.exitPort(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitPort) {
            return visitor.visitPort(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class KeyAndStringContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_keyAndString; }
    // @Override
    enterRule(listener) {
        if (listener.enterKeyAndString) {
            listener.enterKeyAndString(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitKeyAndString) {
            listener.exitKeyAndString(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitKeyAndString) {
            return visitor.visitKeyAndString(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class IntAndStringContext extends ParserRuleContext {
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_intAndString; }
    // @Override
    enterRule(listener) {
        if (listener.enterIntAndString) {
            listener.enterIntAndString(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitIntAndString) {
            listener.exitIntAndString(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitIntAndString) {
            return visitor.visitIntAndString(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class InfoOptContext extends ParserRuleContext {
    SERVER() { return this.tryGetToken(RedisParser.SERVER, 0); }
    CLIENTS() { return this.tryGetToken(RedisParser.CLIENTS, 0); }
    MEMORY() { return this.tryGetToken(RedisParser.MEMORY, 0); }
    PERSISTENCE() { return this.tryGetToken(RedisParser.PERSISTENCE, 0); }
    STATS() { return this.tryGetToken(RedisParser.STATS, 0); }
    REPLICATION() { return this.tryGetToken(RedisParser.REPLICATION, 0); }
    CPU() { return this.tryGetToken(RedisParser.CPU, 0); }
    COMMANDSTATS() { return this.tryGetToken(RedisParser.COMMANDSTATS, 0); }
    LATENCYSTATS() { return this.tryGetToken(RedisParser.LATENCYSTATS, 0); }
    SENTINEL() { return this.tryGetToken(RedisParser.SENTINEL, 0); }
    MODULES() { return this.tryGetToken(RedisParser.MODULES, 0); }
    KEYSPACE() { return this.tryGetToken(RedisParser.KEYSPACE, 0); }
    ERRORSTATS() { return this.tryGetToken(RedisParser.ERRORSTATS, 0); }
    ALL() { return this.tryGetToken(RedisParser.ALL, 0); }
    DEFAULT() { return this.tryGetToken(RedisParser.DEFAULT, 0); }
    EVERYTHING() { return this.tryGetToken(RedisParser.EVERYTHING, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_infoOpt; }
    // @Override
    enterRule(listener) {
        if (listener.enterInfoOpt) {
            listener.enterInfoOpt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitInfoOpt) {
            listener.exitInfoOpt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitInfoOpt) {
            return visitor.visitInfoOpt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class PatternOptContext extends ParserRuleContext {
    GET() { return this.getToken(RedisParser.GET, 0); }
    pattern() {
        return this.getRuleContext(0, PatternContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_patternOpt; }
    // @Override
    enterRule(listener) {
        if (listener.enterPatternOpt) {
            listener.enterPatternOpt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitPatternOpt) {
            listener.exitPatternOpt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitPatternOpt) {
            return visitor.visitPatternOpt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class KeyOptContext extends ParserRuleContext {
    NX() { return this.tryGetToken(RedisParser.NX, 0); }
    XX() { return this.tryGetToken(RedisParser.XX, 0); }
    GT() { return this.tryGetToken(RedisParser.GT, 0); }
    LT() { return this.tryGetToken(RedisParser.LT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_keyOpt; }
    // @Override
    enterRule(listener) {
        if (listener.enterKeyOpt) {
            listener.enterKeyOpt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitKeyOpt) {
            listener.exitKeyOpt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitKeyOpt) {
            return visitor.visitKeyOpt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class IdletimeOptContext extends ParserRuleContext {
    IDLETIME() { return this.getToken(RedisParser.IDLETIME, 0); }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_idletimeOpt; }
    // @Override
    enterRule(listener) {
        if (listener.enterIdletimeOpt) {
            listener.enterIdletimeOpt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitIdletimeOpt) {
            listener.exitIdletimeOpt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitIdletimeOpt) {
            return visitor.visitIdletimeOpt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class FreqOptContext extends ParserRuleContext {
    FREQ() { return this.getToken(RedisParser.FREQ, 0); }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_freqOpt; }
    // @Override
    enterRule(listener) {
        if (listener.enterFreqOpt) {
            listener.enterFreqOpt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitFreqOpt) {
            listener.exitFreqOpt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitFreqOpt) {
            return visitor.visitFreqOpt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LimitOptContext extends ParserRuleContext {
    LIMIT() { return this.getToken(RedisParser.LIMIT, 0); }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_limitOpt; }
    // @Override
    enterRule(listener) {
        if (listener.enterLimitOpt) {
            listener.enterLimitOpt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLimitOpt) {
            listener.exitLimitOpt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLimitOpt) {
            return visitor.visitLimitOpt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SortOptContext extends ParserRuleContext {
    ASC() { return this.tryGetToken(RedisParser.ASC, 0); }
    DESC() { return this.tryGetToken(RedisParser.DESC, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_sortOpt; }
    // @Override
    enterRule(listener) {
        if (listener.enterSortOpt) {
            listener.enterSortOpt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSortOpt) {
            listener.exitSortOpt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSortOpt) {
            return visitor.visitSortOpt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class GetsetOptContext extends ParserRuleContext {
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    EX() { return this.tryGetToken(RedisParser.EX, 0); }
    PX() { return this.tryGetToken(RedisParser.PX, 0); }
    EXAT() { return this.tryGetToken(RedisParser.EXAT, 0); }
    PXAT() { return this.tryGetToken(RedisParser.PXAT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_getsetOpt; }
    // @Override
    enterRule(listener) {
        if (listener.enterGetsetOpt) {
            listener.enterGetsetOpt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitGetsetOpt) {
            listener.exitGetsetOpt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitGetsetOpt) {
            return visitor.visitGetsetOpt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class LrOptContext extends ParserRuleContext {
    LEFT() { return this.tryGetToken(RedisParser.LEFT, 0); }
    RIGHT() { return this.tryGetToken(RedisParser.RIGHT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_lrOpt; }
    // @Override
    enterRule(listener) {
        if (listener.enterLrOpt) {
            listener.enterLrOpt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitLrOpt) {
            listener.exitLrOpt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitLrOpt) {
            return visitor.visitLrOpt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class RankOptContext extends ParserRuleContext {
    RANK() { return this.getToken(RedisParser.RANK, 0); }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_rankOpt; }
    // @Override
    enterRule(listener) {
        if (listener.enterRankOpt) {
            listener.enterRankOpt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitRankOpt) {
            listener.exitRankOpt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitRankOpt) {
            return visitor.visitRankOpt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CountOptContext extends ParserRuleContext {
    COUNT() { return this.getToken(RedisParser.COUNT, 0); }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_countOpt; }
    // @Override
    enterRule(listener) {
        if (listener.enterCountOpt) {
            listener.enterCountOpt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCountOpt) {
            listener.exitCountOpt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCountOpt) {
            return visitor.visitCountOpt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class MaxlenOptContext extends ParserRuleContext {
    MAXLEN() { return this.getToken(RedisParser.MAXLEN, 0); }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_maxlenOpt; }
    // @Override
    enterRule(listener) {
        if (listener.enterMaxlenOpt) {
            listener.enterMaxlenOpt(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitMaxlenOpt) {
            listener.exitMaxlenOpt(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitMaxlenOpt) {
            return visitor.visitMaxlenOpt(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdCopyContext extends ParserRuleContext {
    COPY() { return this.getToken(RedisParser.COPY, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    DB() { return this.tryGetToken(RedisParser.DB, 0); }
    intValue() {
        return this.tryGetRuleContext(0, IntValueContext);
    }
    REPLACE() { return this.tryGetToken(RedisParser.REPLACE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdCopy; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdCopy) {
            listener.enterCmdCopy(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdCopy) {
            listener.exitCmdCopy(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdCopy) {
            return visitor.visitCmdCopy(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdDelContext extends ParserRuleContext {
    DEL() { return this.getToken(RedisParser.DEL, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdDel; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdDel) {
            listener.enterCmdDel(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdDel) {
            listener.exitCmdDel(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdDel) {
            return visitor.visitCmdDel(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdDumpContext extends ParserRuleContext {
    DUMP() { return this.getToken(RedisParser.DUMP, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdDump; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdDump) {
            listener.enterCmdDump(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdDump) {
            listener.exitCmdDump(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdDump) {
            return visitor.visitCmdDump(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdExistsContext extends ParserRuleContext {
    EXISTS() { return this.getToken(RedisParser.EXISTS, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdExists; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdExists) {
            listener.enterCmdExists(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdExists) {
            listener.exitCmdExists(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdExists) {
            return visitor.visitCmdExists(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdExpireContext extends ParserRuleContext {
    EXPIRE() { return this.getToken(RedisParser.EXPIRE, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    keyOpt() {
        return this.tryGetRuleContext(0, KeyOptContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdExpire; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdExpire) {
            listener.enterCmdExpire(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdExpire) {
            listener.exitCmdExpire(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdExpire) {
            return visitor.visitCmdExpire(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdExpireatContext extends ParserRuleContext {
    EXPIREAT() { return this.getToken(RedisParser.EXPIREAT, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    keyOpt() {
        return this.tryGetRuleContext(0, KeyOptContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdExpireat; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdExpireat) {
            listener.enterCmdExpireat(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdExpireat) {
            listener.exitCmdExpireat(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdExpireat) {
            return visitor.visitCmdExpireat(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdExpireTimeContext extends ParserRuleContext {
    EXPIRETIME() { return this.getToken(RedisParser.EXPIRETIME, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdExpireTime; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdExpireTime) {
            listener.enterCmdExpireTime(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdExpireTime) {
            listener.exitCmdExpireTime(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdExpireTime) {
            return visitor.visitCmdExpireTime(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdKeysContext extends ParserRuleContext {
    KEYS() { return this.getToken(RedisParser.KEYS, 0); }
    pattern() {
        return this.getRuleContext(0, PatternContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdKeys; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdKeys) {
            listener.enterCmdKeys(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdKeys) {
            listener.exitCmdKeys(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdKeys) {
            return visitor.visitCmdKeys(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdMoveContext extends ParserRuleContext {
    MOVE() { return this.getToken(RedisParser.MOVE, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdMove; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdMove) {
            listener.enterCmdMove(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdMove) {
            listener.exitCmdMove(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdMove) {
            return visitor.visitCmdMove(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdObjectContext extends ParserRuleContext {
    OBJECT() { return this.getToken(RedisParser.OBJECT, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    ENCODING() { return this.tryGetToken(RedisParser.ENCODING, 0); }
    FREQ() { return this.tryGetToken(RedisParser.FREQ, 0); }
    IDLETIME() { return this.tryGetToken(RedisParser.IDLETIME, 0); }
    REFCOUNT() { return this.tryGetToken(RedisParser.REFCOUNT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdObject; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdObject) {
            listener.enterCmdObject(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdObject) {
            listener.exitCmdObject(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdObject) {
            return visitor.visitCmdObject(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPersistContext extends ParserRuleContext {
    PERSIST() { return this.getToken(RedisParser.PERSIST, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPersist; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPersist) {
            listener.enterCmdPersist(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPersist) {
            listener.exitCmdPersist(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPersist) {
            return visitor.visitCmdPersist(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPexpireContext extends ParserRuleContext {
    PEXPIRE() { return this.getToken(RedisParser.PEXPIRE, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    keyOpt() {
        return this.tryGetRuleContext(0, KeyOptContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPexpire; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPexpire) {
            listener.enterCmdPexpire(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPexpire) {
            listener.exitCmdPexpire(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPexpire) {
            return visitor.visitCmdPexpire(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPexpireatContext extends ParserRuleContext {
    PEXPIREAT() { return this.getToken(RedisParser.PEXPIREAT, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    keyOpt() {
        return this.tryGetRuleContext(0, KeyOptContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPexpireat; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPexpireat) {
            listener.enterCmdPexpireat(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPexpireat) {
            listener.exitCmdPexpireat(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPexpireat) {
            return visitor.visitCmdPexpireat(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPExpireTimeContext extends ParserRuleContext {
    PEXPIRETIME() { return this.getToken(RedisParser.PEXPIRETIME, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPExpireTime; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPExpireTime) {
            listener.enterCmdPExpireTime(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPExpireTime) {
            listener.exitCmdPExpireTime(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPExpireTime) {
            return visitor.visitCmdPExpireTime(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdTtlContext extends ParserRuleContext {
    TTL() { return this.getToken(RedisParser.TTL, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdTtl; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdTtl) {
            listener.enterCmdTtl(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdTtl) {
            listener.exitCmdTtl(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdTtl) {
            return visitor.visitCmdTtl(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPttlContext extends ParserRuleContext {
    PTTL() { return this.getToken(RedisParser.PTTL, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPttl; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPttl) {
            listener.enterCmdPttl(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPttl) {
            listener.exitCmdPttl(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPttl) {
            return visitor.visitCmdPttl(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdRandomkeyContext extends ParserRuleContext {
    RANDOMKEY() { return this.getToken(RedisParser.RANDOMKEY, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdRandomkey; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdRandomkey) {
            listener.enterCmdRandomkey(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdRandomkey) {
            listener.exitCmdRandomkey(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdRandomkey) {
            return visitor.visitCmdRandomkey(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdRenameContext extends ParserRuleContext {
    RENAME() { return this.getToken(RedisParser.RENAME, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdRename; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdRename) {
            listener.enterCmdRename(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdRename) {
            listener.exitCmdRename(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdRename) {
            return visitor.visitCmdRename(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdRenamenxContext extends ParserRuleContext {
    RENAMENX() { return this.getToken(RedisParser.RENAMENX, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdRenamenx; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdRenamenx) {
            listener.enterCmdRenamenx(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdRenamenx) {
            listener.exitCmdRenamenx(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdRenamenx) {
            return visitor.visitCmdRenamenx(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdRestoreContext extends ParserRuleContext {
    RESTORE() { return this.getToken(RedisParser.RESTORE, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    REPLACE() { return this.tryGetToken(RedisParser.REPLACE, 0); }
    ABSTTL() { return this.tryGetToken(RedisParser.ABSTTL, 0); }
    idletimeOpt() {
        return this.tryGetRuleContext(0, IdletimeOptContext);
    }
    freqOpt() {
        return this.tryGetRuleContext(0, FreqOptContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdRestore; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdRestore) {
            listener.enterCmdRestore(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdRestore) {
            listener.exitCmdRestore(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdRestore) {
            return visitor.visitCmdRestore(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdScanContext extends ParserRuleContext {
    SCAN() { return this.getToken(RedisParser.SCAN, 0); }
    cursor() {
        return this.getRuleContext(0, CursorContext);
    }
    pattern() {
        return this.getRuleContext(0, PatternContext);
    }
    intValue() {
        return this.tryGetRuleContext(0, IntValueContext);
    }
    TYPE() { return this.tryGetToken(RedisParser.TYPE, 0); }
    stringValue() {
        return this.tryGetRuleContext(0, StringValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdScan; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdScan) {
            listener.enterCmdScan(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdScan) {
            listener.exitCmdScan(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdScan) {
            return visitor.visitCmdScan(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSortContext extends ParserRuleContext {
    SORT() { return this.getToken(RedisParser.SORT, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    BY() { return this.tryGetToken(RedisParser.BY, 0); }
    pattern() {
        return this.tryGetRuleContext(0, PatternContext);
    }
    limitOpt() {
        return this.tryGetRuleContext(0, LimitOptContext);
    }
    sortOpt() {
        return this.tryGetRuleContext(0, SortOptContext);
    }
    ALPHA() { return this.tryGetToken(RedisParser.ALPHA, 0); }
    STORE() { return this.tryGetToken(RedisParser.STORE, 0); }
    stringValue() {
        return this.tryGetRuleContext(0, StringValueContext);
    }
    patternOpt(i) {
        if (i === undefined) {
            return this.getRuleContexts(PatternOptContext);
        }
        else {
            return this.getRuleContext(i, PatternOptContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSort; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSort) {
            listener.enterCmdSort(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSort) {
            listener.exitCmdSort(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSort) {
            return visitor.visitCmdSort(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSortroContext extends ParserRuleContext {
    SORT_RO() { return this.getToken(RedisParser.SORT_RO, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    BY() { return this.tryGetToken(RedisParser.BY, 0); }
    pattern() {
        return this.tryGetRuleContext(0, PatternContext);
    }
    limitOpt() {
        return this.tryGetRuleContext(0, LimitOptContext);
    }
    sortOpt() {
        return this.tryGetRuleContext(0, SortOptContext);
    }
    ALPHA() { return this.tryGetToken(RedisParser.ALPHA, 0); }
    patternOpt(i) {
        if (i === undefined) {
            return this.getRuleContexts(PatternOptContext);
        }
        else {
            return this.getRuleContext(i, PatternOptContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSortro; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSortro) {
            listener.enterCmdSortro(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSortro) {
            listener.exitCmdSortro(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSortro) {
            return visitor.visitCmdSortro(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdTouchContext extends ParserRuleContext {
    TOUCH() { return this.getToken(RedisParser.TOUCH, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdTouch; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdTouch) {
            listener.enterCmdTouch(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdTouch) {
            listener.exitCmdTouch(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdTouch) {
            return visitor.visitCmdTouch(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdTypeContext extends ParserRuleContext {
    TYPE() { return this.getToken(RedisParser.TYPE, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdType; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdType) {
            listener.enterCmdType(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdType) {
            listener.exitCmdType(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdType) {
            return visitor.visitCmdType(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdUnlinkContext extends ParserRuleContext {
    UNLINK() { return this.getToken(RedisParser.UNLINK, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdUnlink; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdUnlink) {
            listener.enterCmdUnlink(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdUnlink) {
            listener.exitCmdUnlink(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdUnlink) {
            return visitor.visitCmdUnlink(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdWaitContext extends ParserRuleContext {
    WAIT() { return this.getToken(RedisParser.WAIT, 0); }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdWait; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdWait) {
            listener.enterCmdWait(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdWait) {
            listener.exitCmdWait(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdWait) {
            return visitor.visitCmdWait(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdAppendContext extends ParserRuleContext {
    APPEND() { return this.getToken(RedisParser.APPEND, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdAppend; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdAppend) {
            listener.enterCmdAppend(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdAppend) {
            listener.exitCmdAppend(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdAppend) {
            return visitor.visitCmdAppend(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdDecrContext extends ParserRuleContext {
    DECR() { return this.getToken(RedisParser.DECR, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdDecr; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdDecr) {
            listener.enterCmdDecr(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdDecr) {
            listener.exitCmdDecr(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdDecr) {
            return visitor.visitCmdDecr(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdDecrbyContext extends ParserRuleContext {
    DECRBY() { return this.getToken(RedisParser.DECRBY, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdDecrby; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdDecrby) {
            listener.enterCmdDecrby(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdDecrby) {
            listener.exitCmdDecrby(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdDecrby) {
            return visitor.visitCmdDecrby(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdGetContext extends ParserRuleContext {
    GET() { return this.getToken(RedisParser.GET, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdGet; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdGet) {
            listener.enterCmdGet(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdGet) {
            listener.exitCmdGet(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdGet) {
            return visitor.visitCmdGet(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdGetdelContext extends ParserRuleContext {
    GETDEL() { return this.getToken(RedisParser.GETDEL, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdGetdel; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdGetdel) {
            listener.enterCmdGetdel(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdGetdel) {
            listener.exitCmdGetdel(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdGetdel) {
            return visitor.visitCmdGetdel(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdGetexContext extends ParserRuleContext {
    GETEX() { return this.getToken(RedisParser.GETEX, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    getsetOpt() {
        return this.tryGetRuleContext(0, GetsetOptContext);
    }
    PERSIST() { return this.tryGetToken(RedisParser.PERSIST, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdGetex; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdGetex) {
            listener.enterCmdGetex(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdGetex) {
            listener.exitCmdGetex(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdGetex) {
            return visitor.visitCmdGetex(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdGetrangeContext extends ParserRuleContext {
    GETRANGE() { return this.getToken(RedisParser.GETRANGE, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdGetrange; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdGetrange) {
            listener.enterCmdGetrange(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdGetrange) {
            listener.exitCmdGetrange(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdGetrange) {
            return visitor.visitCmdGetrange(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdGetsetContext extends ParserRuleContext {
    GETSET() { return this.getToken(RedisParser.GETSET, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdGetset; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdGetset) {
            listener.enterCmdGetset(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdGetset) {
            listener.exitCmdGetset(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdGetset) {
            return visitor.visitCmdGetset(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdGetbitContext extends ParserRuleContext {
    GETBIT() { return this.getToken(RedisParser.GETBIT, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdGetbit; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdGetbit) {
            listener.enterCmdGetbit(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdGetbit) {
            listener.exitCmdGetbit(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdGetbit) {
            return visitor.visitCmdGetbit(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdIncContext extends ParserRuleContext {
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdInc; }
    copyFrom(ctx) {
        super.copyFrom(ctx);
    }
}
export class CmdIncrContext extends CmdIncContext {
    INCR() { return this.getToken(RedisParser.INCR, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdIncr) {
            listener.enterCmdIncr(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdIncr) {
            listener.exitCmdIncr(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdIncr) {
            return visitor.visitCmdIncr(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdIncrbyContext extends CmdIncContext {
    INCRBY() { return this.getToken(RedisParser.INCRBY, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdIncrby) {
            listener.enterCmdIncrby(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdIncrby) {
            listener.exitCmdIncrby(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdIncrby) {
            return visitor.visitCmdIncrby(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdIncrbyFloatContext extends CmdIncContext {
    INCRBYFLOAT() { return this.getToken(RedisParser.INCRBYFLOAT, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    floatValue() {
        return this.getRuleContext(0, FloatValueContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdIncrbyFloat) {
            listener.enterCmdIncrbyFloat(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdIncrbyFloat) {
            listener.exitCmdIncrbyFloat(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdIncrbyFloat) {
            return visitor.visitCmdIncrbyFloat(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdLcsContext extends ParserRuleContext {
    LCS() { return this.getToken(RedisParser.LCS, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    LEN() { return this.tryGetToken(RedisParser.LEN, 0); }
    IDX() { return this.tryGetToken(RedisParser.IDX, 0); }
    MINMATCHLEN() { return this.tryGetToken(RedisParser.MINMATCHLEN, 0); }
    intValue() {
        return this.tryGetRuleContext(0, IntValueContext);
    }
    WITHMATCHLEN() { return this.tryGetToken(RedisParser.WITHMATCHLEN, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdLcs; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdLcs) {
            listener.enterCmdLcs(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdLcs) {
            listener.exitCmdLcs(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdLcs) {
            return visitor.visitCmdLcs(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdMgetContext extends ParserRuleContext {
    MGET() { return this.getToken(RedisParser.MGET, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdMget; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdMget) {
            listener.enterCmdMget(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdMget) {
            listener.exitCmdMget(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdMget) {
            return visitor.visitCmdMget(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdMsetContext extends ParserRuleContext {
    MSET() { return this.getToken(RedisParser.MSET, 0); }
    keyAndString(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyAndStringContext);
        }
        else {
            return this.getRuleContext(i, KeyAndStringContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdMset; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdMset) {
            listener.enterCmdMset(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdMset) {
            listener.exitCmdMset(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdMset) {
            return visitor.visitCmdMset(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdMsetnxContext extends ParserRuleContext {
    MSETNX() { return this.getToken(RedisParser.MSETNX, 0); }
    keyAndString(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyAndStringContext);
        }
        else {
            return this.getRuleContext(i, KeyAndStringContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdMsetnx; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdMsetnx) {
            listener.enterCmdMsetnx(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdMsetnx) {
            listener.exitCmdMsetnx(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdMsetnx) {
            return visitor.visitCmdMsetnx(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSetexContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    SETEX() { return this.tryGetToken(RedisParser.SETEX, 0); }
    PSETEX() { return this.tryGetToken(RedisParser.PSETEX, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSetex; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSetex) {
            listener.enterCmdSetex(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSetex) {
            listener.exitCmdSetex(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSetex) {
            return visitor.visitCmdSetex(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSetContext extends ParserRuleContext {
    SET() { return this.getToken(RedisParser.SET, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    getsetOpt() {
        return this.tryGetRuleContext(0, GetsetOptContext);
    }
    KEEPTTL() { return this.tryGetToken(RedisParser.KEEPTTL, 0); }
    GET() { return this.tryGetToken(RedisParser.GET, 0); }
    NX() { return this.tryGetToken(RedisParser.NX, 0); }
    XX() { return this.tryGetToken(RedisParser.XX, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSet; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSet) {
            listener.enterCmdSet(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSet) {
            listener.exitCmdSet(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSet) {
            return visitor.visitCmdSet(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSetnxContext extends ParserRuleContext {
    SETNX() { return this.getToken(RedisParser.SETNX, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSetnx; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSetnx) {
            listener.enterCmdSetnx(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSetnx) {
            listener.exitCmdSetnx(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSetnx) {
            return visitor.visitCmdSetnx(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSetrangeContext extends ParserRuleContext {
    SETRANGE() { return this.getToken(RedisParser.SETRANGE, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSetrange; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSetrange) {
            listener.enterCmdSetrange(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSetrange) {
            listener.exitCmdSetrange(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSetrange) {
            return visitor.visitCmdSetrange(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSetbitContext extends ParserRuleContext {
    SETBIT() { return this.getToken(RedisParser.SETBIT, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSetbit; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSetbit) {
            listener.enterCmdSetbit(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSetbit) {
            listener.exitCmdSetbit(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSetbit) {
            return visitor.visitCmdSetbit(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdStrlenContext extends ParserRuleContext {
    STRLEN() { return this.getToken(RedisParser.STRLEN, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdStrlen; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdStrlen) {
            listener.enterCmdStrlen(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdStrlen) {
            listener.exitCmdStrlen(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdStrlen) {
            return visitor.visitCmdStrlen(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSubstrContext extends ParserRuleContext {
    SUBSTR() { return this.getToken(RedisParser.SUBSTR, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSubstr; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSubstr) {
            listener.enterCmdSubstr(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSubstr) {
            listener.exitCmdSubstr(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSubstr) {
            return visitor.visitCmdSubstr(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdHdelMgetContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    HDEL() { return this.tryGetToken(RedisParser.HDEL, 0); }
    HMGET() { return this.tryGetToken(RedisParser.HMGET, 0); }
    fieldName(i) {
        if (i === undefined) {
            return this.getRuleContexts(FieldNameContext);
        }
        else {
            return this.getRuleContext(i, FieldNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdHdelMget; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdHdelMget) {
            listener.enterCmdHdelMget(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdHdelMget) {
            listener.exitCmdHdelMget(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdHdelMget) {
            return visitor.visitCmdHdelMget(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdHexistsGetKeysStrlenContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    fieldName() {
        return this.getRuleContext(0, FieldNameContext);
    }
    HEXISTS() { return this.tryGetToken(RedisParser.HEXISTS, 0); }
    HGET() { return this.tryGetToken(RedisParser.HGET, 0); }
    HSTRLEN() { return this.tryGetToken(RedisParser.HSTRLEN, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdHexistsGetKeysStrlen; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdHexistsGetKeysStrlen) {
            listener.enterCmdHexistsGetKeysStrlen(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdHexistsGetKeysStrlen) {
            listener.exitCmdHexistsGetKeysStrlen(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdHexistsGetKeysStrlen) {
            return visitor.visitCmdHexistsGetKeysStrlen(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdHgetallLenValsContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    HGETALL() { return this.tryGetToken(RedisParser.HGETALL, 0); }
    HLEN() { return this.tryGetToken(RedisParser.HLEN, 0); }
    HVALS() { return this.tryGetToken(RedisParser.HVALS, 0); }
    HKEYS() { return this.tryGetToken(RedisParser.HKEYS, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdHgetallLenVals; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdHgetallLenVals) {
            listener.enterCmdHgetallLenVals(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdHgetallLenVals) {
            listener.exitCmdHgetallLenVals(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdHgetallLenVals) {
            return visitor.visitCmdHgetallLenVals(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdHmsetHsetContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    HMSET() { return this.tryGetToken(RedisParser.HMSET, 0); }
    HSET() { return this.tryGetToken(RedisParser.HSET, 0); }
    keyAndString(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyAndStringContext);
        }
        else {
            return this.getRuleContext(i, KeyAndStringContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdHmsetHset; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdHmsetHset) {
            listener.enterCmdHmsetHset(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdHmsetHset) {
            listener.exitCmdHmsetHset(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdHmsetHset) {
            return visitor.visitCmdHmsetHset(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdHincrbyContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    fieldName() {
        return this.getRuleContext(0, FieldNameContext);
    }
    floatValue() {
        return this.getRuleContext(0, FloatValueContext);
    }
    HINCRBY() { return this.tryGetToken(RedisParser.HINCRBY, 0); }
    HINCRBYFLOAT() { return this.tryGetToken(RedisParser.HINCRBYFLOAT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdHincrby; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdHincrby) {
            listener.enterCmdHincrby(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdHincrby) {
            listener.exitCmdHincrby(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdHincrby) {
            return visitor.visitCmdHincrby(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdHrandfieldContext extends ParserRuleContext {
    HRANDFIELD() { return this.getToken(RedisParser.HRANDFIELD, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.tryGetRuleContext(0, IntValueContext);
    }
    WITHVALUES() { return this.tryGetToken(RedisParser.WITHVALUES, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdHrandfield; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdHrandfield) {
            listener.enterCmdHrandfield(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdHrandfield) {
            listener.exitCmdHrandfield(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdHrandfield) {
            return visitor.visitCmdHrandfield(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdHscanContext extends ParserRuleContext {
    HSCAN() { return this.getToken(RedisParser.HSCAN, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    MATCH() { return this.tryGetToken(RedisParser.MATCH, 0); }
    pattern() {
        return this.tryGetRuleContext(0, PatternContext);
    }
    COUNT() { return this.tryGetToken(RedisParser.COUNT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdHscan; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdHscan) {
            listener.enterCmdHscan(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdHscan) {
            listener.exitCmdHscan(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdHscan) {
            return visitor.visitCmdHscan(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdHsetnxContext extends ParserRuleContext {
    HSETNX() { return this.getToken(RedisParser.HSETNX, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    keyAndString() {
        return this.getRuleContext(0, KeyAndStringContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdHsetnx; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdHsetnx) {
            listener.enterCmdHsetnx(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdHsetnx) {
            listener.exitCmdHsetnx(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdHsetnx) {
            return visitor.visitCmdHsetnx(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdBlmoveContext extends ParserRuleContext {
    BLMOVE() { return this.getToken(RedisParser.BLMOVE, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    lrOpt(i) {
        if (i === undefined) {
            return this.getRuleContexts(LrOptContext);
        }
        else {
            return this.getRuleContext(i, LrOptContext);
        }
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdBlmove; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdBlmove) {
            listener.enterCmdBlmove(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdBlmove) {
            listener.exitCmdBlmove(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdBlmove) {
            return visitor.visitCmdBlmove(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdLmpopContext extends ParserRuleContext {
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    lrOpt() {
        return this.getRuleContext(0, LrOptContext);
    }
    LMPOP() { return this.tryGetToken(RedisParser.LMPOP, 0); }
    BLMPOP() { return this.tryGetToken(RedisParser.BLMPOP, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdLmpop; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdLmpop) {
            listener.enterCmdLmpop(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdLmpop) {
            listener.exitCmdLmpop(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdLmpop) {
            return visitor.visitCmdLmpop(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdBpopContext extends ParserRuleContext {
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    BLPOP() { return this.tryGetToken(RedisParser.BLPOP, 0); }
    BRPOP() { return this.tryGetToken(RedisParser.BRPOP, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdBpop; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdBpop) {
            listener.enterCmdBpop(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdBpop) {
            listener.exitCmdBpop(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdBpop) {
            return visitor.visitCmdBpop(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdBrpoplpushContext extends ParserRuleContext {
    BRPOPLPUSH() { return this.getToken(RedisParser.BRPOPLPUSH, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdBrpoplpush; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdBrpoplpush) {
            listener.enterCmdBrpoplpush(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdBrpoplpush) {
            listener.exitCmdBrpoplpush(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdBrpoplpush) {
            return visitor.visitCmdBrpoplpush(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdLindexContext extends ParserRuleContext {
    LINDEX() { return this.getToken(RedisParser.LINDEX, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdLindex; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdLindex) {
            listener.enterCmdLindex(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdLindex) {
            listener.exitCmdLindex(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdLindex) {
            return visitor.visitCmdLindex(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdLinsertContext extends ParserRuleContext {
    LINSERT() { return this.getToken(RedisParser.LINSERT, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    stringValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(StringValueContext);
        }
        else {
            return this.getRuleContext(i, StringValueContext);
        }
    }
    BEFORE() { return this.tryGetToken(RedisParser.BEFORE, 0); }
    AFTER() { return this.tryGetToken(RedisParser.AFTER, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdLinsert; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdLinsert) {
            listener.enterCmdLinsert(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdLinsert) {
            listener.exitCmdLinsert(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdLinsert) {
            return visitor.visitCmdLinsert(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdLlenContext extends ParserRuleContext {
    LLEN() { return this.getToken(RedisParser.LLEN, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdLlen; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdLlen) {
            listener.enterCmdLlen(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdLlen) {
            listener.exitCmdLlen(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdLlen) {
            return visitor.visitCmdLlen(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdLmoveContext extends ParserRuleContext {
    LMOVE() { return this.getToken(RedisParser.LMOVE, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    lrOpt(i) {
        if (i === undefined) {
            return this.getRuleContexts(LrOptContext);
        }
        else {
            return this.getRuleContext(i, LrOptContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdLmove; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdLmove) {
            listener.enterCmdLmove(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdLmove) {
            listener.exitCmdLmove(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdLmove) {
            return visitor.visitCmdLmove(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPopContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    LPOP() { return this.tryGetToken(RedisParser.LPOP, 0); }
    RPOP() { return this.tryGetToken(RedisParser.RPOP, 0); }
    intValue() {
        return this.tryGetRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPop; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPop) {
            listener.enterCmdPop(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPop) {
            listener.exitCmdPop(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPop) {
            return visitor.visitCmdPop(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdLposContext extends ParserRuleContext {
    LPOS() { return this.getToken(RedisParser.LPOS, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    fieldName() {
        return this.getRuleContext(0, FieldNameContext);
    }
    rankOpt() {
        return this.tryGetRuleContext(0, RankOptContext);
    }
    countOpt() {
        return this.tryGetRuleContext(0, CountOptContext);
    }
    maxlenOpt() {
        return this.tryGetRuleContext(0, MaxlenOptContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdLpos; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdLpos) {
            listener.enterCmdLpos(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdLpos) {
            listener.exitCmdLpos(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdLpos) {
            return visitor.visitCmdLpos(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPushContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    LPUSH() { return this.tryGetToken(RedisParser.LPUSH, 0); }
    LPUSHX() { return this.tryGetToken(RedisParser.LPUSHX, 0); }
    RPUSH() { return this.tryGetToken(RedisParser.RPUSH, 0); }
    RPUSHX() { return this.tryGetToken(RedisParser.RPUSHX, 0); }
    stringValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(StringValueContext);
        }
        else {
            return this.getRuleContext(i, StringValueContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPush; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPush) {
            listener.enterCmdPush(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPush) {
            listener.exitCmdPush(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPush) {
            return visitor.visitCmdPush(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdLrangeTrimContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    LRANGE() { return this.tryGetToken(RedisParser.LRANGE, 0); }
    LTRIM() { return this.tryGetToken(RedisParser.LTRIM, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdLrangeTrim; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdLrangeTrim) {
            listener.enterCmdLrangeTrim(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdLrangeTrim) {
            listener.exitCmdLrangeTrim(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdLrangeTrim) {
            return visitor.visitCmdLrangeTrim(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdLremSetContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    LREM() { return this.tryGetToken(RedisParser.LREM, 0); }
    LSET() { return this.tryGetToken(RedisParser.LSET, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdLremSet; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdLremSet) {
            listener.enterCmdLremSet(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdLremSet) {
            listener.exitCmdLremSet(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdLremSet) {
            return visitor.visitCmdLremSet(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdRpoplpushContext extends ParserRuleContext {
    RPOPLPUSH() { return this.getToken(RedisParser.RPOPLPUSH, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdRpoplpush; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdRpoplpush) {
            listener.enterCmdRpoplpush(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdRpoplpush) {
            listener.exitCmdRpoplpush(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdRpoplpush) {
            return visitor.visitCmdRpoplpush(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSaddContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    SADD() { return this.tryGetToken(RedisParser.SADD, 0); }
    SREM() { return this.tryGetToken(RedisParser.SREM, 0); }
    SMISMEMBER() { return this.tryGetToken(RedisParser.SMISMEMBER, 0); }
    fieldName(i) {
        if (i === undefined) {
            return this.getRuleContexts(FieldNameContext);
        }
        else {
            return this.getRuleContext(i, FieldNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSadd; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSadd) {
            listener.enterCmdSadd(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSadd) {
            listener.exitCmdSadd(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSadd) {
            return visitor.visitCmdSadd(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdScardContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    SCARD() { return this.tryGetToken(RedisParser.SCARD, 0); }
    SMEMBERS() { return this.tryGetToken(RedisParser.SMEMBERS, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdScard; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdScard) {
            listener.enterCmdScard(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdScard) {
            listener.exitCmdScard(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdScard) {
            return visitor.visitCmdScard(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSdiffContext extends ParserRuleContext {
    SDIFF() { return this.tryGetToken(RedisParser.SDIFF, 0); }
    SINTER() { return this.tryGetToken(RedisParser.SINTER, 0); }
    SUNION() { return this.tryGetToken(RedisParser.SUNION, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSdiff; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSdiff) {
            listener.enterCmdSdiff(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSdiff) {
            listener.exitCmdSdiff(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSdiff) {
            return visitor.visitCmdSdiff(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSdiffstoreContext extends ParserRuleContext {
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    SDIFFSTORE() { return this.tryGetToken(RedisParser.SDIFFSTORE, 0); }
    SINTERSTORE() { return this.tryGetToken(RedisParser.SINTERSTORE, 0); }
    SUNIONSTORE() { return this.tryGetToken(RedisParser.SUNIONSTORE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSdiffstore; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSdiffstore) {
            listener.enterCmdSdiffstore(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSdiffstore) {
            listener.exitCmdSdiffstore(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSdiffstore) {
            return visitor.visitCmdSdiffstore(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSintercardContext extends ParserRuleContext {
    SINTERCARD() { return this.getToken(RedisParser.SINTERCARD, 0); }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    LIMIT() { return this.tryGetToken(RedisParser.LIMIT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSintercard; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSintercard) {
            listener.enterCmdSintercard(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSintercard) {
            listener.exitCmdSintercard(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSintercard) {
            return visitor.visitCmdSintercard(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSismemberContext extends ParserRuleContext {
    SISMEMBER() { return this.getToken(RedisParser.SISMEMBER, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    fieldName() {
        return this.getRuleContext(0, FieldNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSismember; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSismember) {
            listener.enterCmdSismember(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSismember) {
            listener.exitCmdSismember(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSismember) {
            return visitor.visitCmdSismember(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSmoveContext extends ParserRuleContext {
    SMOVE() { return this.getToken(RedisParser.SMOVE, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    fieldName() {
        return this.getRuleContext(0, FieldNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSmove; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSmove) {
            listener.enterCmdSmove(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSmove) {
            listener.exitCmdSmove(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSmove) {
            return visitor.visitCmdSmove(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSpopContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    SPOP() { return this.tryGetToken(RedisParser.SPOP, 0); }
    SRANDMEMBER() { return this.tryGetToken(RedisParser.SRANDMEMBER, 0); }
    intValue() {
        return this.tryGetRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSpop; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSpop) {
            listener.enterCmdSpop(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSpop) {
            listener.exitCmdSpop(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSpop) {
            return visitor.visitCmdSpop(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSscanContext extends ParserRuleContext {
    SSCAN() { return this.getToken(RedisParser.SSCAN, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    MATCH() { return this.tryGetToken(RedisParser.MATCH, 0); }
    pattern() {
        return this.tryGetRuleContext(0, PatternContext);
    }
    countOpt() {
        return this.tryGetRuleContext(0, CountOptContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSscan; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSscan) {
            listener.enterCmdSscan(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSscan) {
            listener.exitCmdSscan(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSscan) {
            return visitor.visitCmdSscan(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdBzmpopContext extends ParserRuleContext {
    BZMPOP() { return this.getToken(RedisParser.BZMPOP, 0); }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    MIN() { return this.tryGetToken(RedisParser.MIN, 0); }
    MAX() { return this.tryGetToken(RedisParser.MAX, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    countOpt() {
        return this.tryGetRuleContext(0, CountOptContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdBzmpop; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdBzmpop) {
            listener.enterCmdBzmpop(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdBzmpop) {
            listener.exitCmdBzmpop(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdBzmpop) {
            return visitor.visitCmdBzmpop(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdBzpopmaxContext extends ParserRuleContext {
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    BZPOPMAX() { return this.tryGetToken(RedisParser.BZPOPMAX, 0); }
    BZPOPMIN() { return this.tryGetToken(RedisParser.BZPOPMIN, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdBzpopmax; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdBzpopmax) {
            listener.enterCmdBzpopmax(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdBzpopmax) {
            listener.exitCmdBzpopmax(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdBzpopmax) {
            return visitor.visitCmdBzpopmax(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZaddContext extends ParserRuleContext {
    ZADD() { return this.getToken(RedisParser.ZADD, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    CH() { return this.tryGetToken(RedisParser.CH, 0); }
    INCR() { return this.tryGetToken(RedisParser.INCR, 0); }
    intAndString(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntAndStringContext);
        }
        else {
            return this.getRuleContext(i, IntAndStringContext);
        }
    }
    NX() { return this.tryGetToken(RedisParser.NX, 0); }
    XX() { return this.tryGetToken(RedisParser.XX, 0); }
    GT() { return this.tryGetToken(RedisParser.GT, 0); }
    LT() { return this.tryGetToken(RedisParser.LT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZadd; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZadd) {
            listener.enterCmdZadd(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZadd) {
            listener.exitCmdZadd(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZadd) {
            return visitor.visitCmdZadd(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZcardContext extends ParserRuleContext {
    ZCARD() { return this.getToken(RedisParser.ZCARD, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZcard; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZcard) {
            listener.enterCmdZcard(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZcard) {
            listener.exitCmdZcard(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZcard) {
            return visitor.visitCmdZcard(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZcountContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    min() {
        return this.getRuleContext(0, MinContext);
    }
    max() {
        return this.getRuleContext(0, MaxContext);
    }
    ZCOUNT() { return this.tryGetToken(RedisParser.ZCOUNT, 0); }
    ZLEXCOUNT() { return this.tryGetToken(RedisParser.ZLEXCOUNT, 0); }
    ZREMRANGEBYLEX() { return this.tryGetToken(RedisParser.ZREMRANGEBYLEX, 0); }
    ZREMRANGEBYSCORE() { return this.tryGetToken(RedisParser.ZREMRANGEBYSCORE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZcount; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZcount) {
            listener.enterCmdZcount(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZcount) {
            listener.exitCmdZcount(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZcount) {
            return visitor.visitCmdZcount(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZdiffContext extends ParserRuleContext {
    ZDIFF() { return this.getToken(RedisParser.ZDIFF, 0); }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    WITHSCORES() { return this.tryGetToken(RedisParser.WITHSCORES, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZdiff; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZdiff) {
            listener.enterCmdZdiff(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZdiff) {
            listener.exitCmdZdiff(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZdiff) {
            return visitor.visitCmdZdiff(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZdiffstoreContext extends ParserRuleContext {
    ZDIFFSTORE() { return this.getToken(RedisParser.ZDIFFSTORE, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZdiffstore; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZdiffstore) {
            listener.enterCmdZdiffstore(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZdiffstore) {
            listener.exitCmdZdiffstore(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZdiffstore) {
            return visitor.visitCmdZdiffstore(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZincrbyContext extends ParserRuleContext {
    ZINCRBY() { return this.getToken(RedisParser.ZINCRBY, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    fieldName() {
        return this.getRuleContext(0, FieldNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZincrby; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZincrby) {
            listener.enterCmdZincrby(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZincrby) {
            listener.exitCmdZincrby(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZincrby) {
            return visitor.visitCmdZincrby(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZinterContext extends ParserRuleContext {
    ZINTER() { return this.getToken(RedisParser.ZINTER, 0); }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    WEIGHTS() { return this.tryGetToken(RedisParser.WEIGHTS, 0); }
    AGGREGATE() { return this.tryGetToken(RedisParser.AGGREGATE, 0); }
    WITHSCORES() { return this.tryGetToken(RedisParser.WITHSCORES, 0); }
    SUM() { return this.tryGetToken(RedisParser.SUM, 0); }
    MIN() { return this.tryGetToken(RedisParser.MIN, 0); }
    MAX() { return this.tryGetToken(RedisParser.MAX, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZinter; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZinter) {
            listener.enterCmdZinter(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZinter) {
            listener.exitCmdZinter(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZinter) {
            return visitor.visitCmdZinter(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZintercardContext extends ParserRuleContext {
    ZINTERCARD() { return this.getToken(RedisParser.ZINTERCARD, 0); }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    LIMIT() { return this.tryGetToken(RedisParser.LIMIT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZintercard; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZintercard) {
            listener.enterCmdZintercard(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZintercard) {
            listener.exitCmdZintercard(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZintercard) {
            return visitor.visitCmdZintercard(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZinterstoreContext extends ParserRuleContext {
    ZINTERSTORE() { return this.getToken(RedisParser.ZINTERSTORE, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    WEIGHTS() { return this.tryGetToken(RedisParser.WEIGHTS, 0); }
    AGGREGATE() { return this.tryGetToken(RedisParser.AGGREGATE, 0); }
    SUM() { return this.tryGetToken(RedisParser.SUM, 0); }
    MIN() { return this.tryGetToken(RedisParser.MIN, 0); }
    MAX() { return this.tryGetToken(RedisParser.MAX, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZinterstore; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZinterstore) {
            listener.enterCmdZinterstore(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZinterstore) {
            listener.exitCmdZinterstore(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZinterstore) {
            return visitor.visitCmdZinterstore(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZmpopContext extends ParserRuleContext {
    ZMPOP() { return this.getToken(RedisParser.ZMPOP, 0); }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    countOpt() {
        return this.getRuleContext(0, CountOptContext);
    }
    MIN() { return this.tryGetToken(RedisParser.MIN, 0); }
    MAX() { return this.tryGetToken(RedisParser.MAX, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZmpop; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZmpop) {
            listener.enterCmdZmpop(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZmpop) {
            listener.exitCmdZmpop(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZmpop) {
            return visitor.visitCmdZmpop(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZmscoreContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    ZMSCORE() { return this.tryGetToken(RedisParser.ZMSCORE, 0); }
    ZREM() { return this.tryGetToken(RedisParser.ZREM, 0); }
    fieldName(i) {
        if (i === undefined) {
            return this.getRuleContexts(FieldNameContext);
        }
        else {
            return this.getRuleContext(i, FieldNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZmscore; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZmscore) {
            listener.enterCmdZmscore(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZmscore) {
            listener.exitCmdZmscore(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZmscore) {
            return visitor.visitCmdZmscore(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZpopmaxContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    ZPOPMAX() { return this.tryGetToken(RedisParser.ZPOPMAX, 0); }
    ZPOPMIN() { return this.tryGetToken(RedisParser.ZPOPMIN, 0); }
    intValue() {
        return this.tryGetRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZpopmax; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZpopmax) {
            listener.enterCmdZpopmax(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZpopmax) {
            listener.exitCmdZpopmax(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZpopmax) {
            return visitor.visitCmdZpopmax(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZrandmemberContext extends ParserRuleContext {
    ZRANDMEMBER() { return this.getToken(RedisParser.ZRANDMEMBER, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.tryGetRuleContext(0, IntValueContext);
    }
    WITHSCORES() { return this.tryGetToken(RedisParser.WITHSCORES, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZrandmember; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZrandmember) {
            listener.enterCmdZrandmember(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZrandmember) {
            listener.exitCmdZrandmember(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZrandmember) {
            return visitor.visitCmdZrandmember(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZrangeContext extends ParserRuleContext {
    ZRANGE() { return this.getToken(RedisParser.ZRANGE, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    min() {
        return this.getRuleContext(0, MinContext);
    }
    max() {
        return this.getRuleContext(0, MaxContext);
    }
    REV() { return this.tryGetToken(RedisParser.REV, 0); }
    limitOpt() {
        return this.tryGetRuleContext(0, LimitOptContext);
    }
    WITHSCORES() { return this.tryGetToken(RedisParser.WITHSCORES, 0); }
    BYSCORE() { return this.tryGetToken(RedisParser.BYSCORE, 0); }
    BYLEX() { return this.tryGetToken(RedisParser.BYLEX, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZrange; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZrange) {
            listener.enterCmdZrange(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZrange) {
            listener.exitCmdZrange(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZrange) {
            return visitor.visitCmdZrange(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZrangebylexContext extends ParserRuleContext {
    ZRANGEBYLEX() { return this.getToken(RedisParser.ZRANGEBYLEX, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    min() {
        return this.getRuleContext(0, MinContext);
    }
    max() {
        return this.getRuleContext(0, MaxContext);
    }
    limitOpt() {
        return this.tryGetRuleContext(0, LimitOptContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZrangebylex; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZrangebylex) {
            listener.enterCmdZrangebylex(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZrangebylex) {
            listener.exitCmdZrangebylex(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZrangebylex) {
            return visitor.visitCmdZrangebylex(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZrangebyscoreContext extends ParserRuleContext {
    ZRANGEBYSCORE() { return this.getToken(RedisParser.ZRANGEBYSCORE, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    min() {
        return this.getRuleContext(0, MinContext);
    }
    max() {
        return this.getRuleContext(0, MaxContext);
    }
    WITHSCORES() { return this.tryGetToken(RedisParser.WITHSCORES, 0); }
    limitOpt() {
        return this.tryGetRuleContext(0, LimitOptContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZrangebyscore; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZrangebyscore) {
            listener.enterCmdZrangebyscore(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZrangebyscore) {
            listener.exitCmdZrangebyscore(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZrangebyscore) {
            return visitor.visitCmdZrangebyscore(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZrangestoreContext extends ParserRuleContext {
    ZRANGESTORE() { return this.getToken(RedisParser.ZRANGESTORE, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    min() {
        return this.getRuleContext(0, MinContext);
    }
    max() {
        return this.getRuleContext(0, MaxContext);
    }
    limitOpt() {
        return this.tryGetRuleContext(0, LimitOptContext);
    }
    BYSCORE() { return this.tryGetToken(RedisParser.BYSCORE, 0); }
    BYLEX() { return this.tryGetToken(RedisParser.BYLEX, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZrangestore; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZrangestore) {
            listener.enterCmdZrangestore(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZrangestore) {
            listener.exitCmdZrangestore(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZrangestore) {
            return visitor.visitCmdZrangestore(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZrankContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    fieldName() {
        return this.getRuleContext(0, FieldNameContext);
    }
    ZRANK() { return this.tryGetToken(RedisParser.ZRANK, 0); }
    ZREVRANK() { return this.tryGetToken(RedisParser.ZREVRANK, 0); }
    ZSCORE() { return this.tryGetToken(RedisParser.ZSCORE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZrank; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZrank) {
            listener.enterCmdZrank(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZrank) {
            listener.exitCmdZrank(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZrank) {
            return visitor.visitCmdZrank(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZremrangebyrankContext extends ParserRuleContext {
    ZREMRANGEBYRANK() { return this.getToken(RedisParser.ZREMRANGEBYRANK, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZremrangebyrank; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZremrangebyrank) {
            listener.enterCmdZremrangebyrank(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZremrangebyrank) {
            listener.exitCmdZremrangebyrank(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZremrangebyrank) {
            return visitor.visitCmdZremrangebyrank(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZrevrangeContext extends ParserRuleContext {
    ZREVRANGE() { return this.getToken(RedisParser.ZREVRANGE, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    WITHSCORES() { return this.tryGetToken(RedisParser.WITHSCORES, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZrevrange; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZrevrange) {
            listener.enterCmdZrevrange(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZrevrange) {
            listener.exitCmdZrevrange(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZrevrange) {
            return visitor.visitCmdZrevrange(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZrevrangebylexContext extends ParserRuleContext {
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    max() {
        return this.getRuleContext(0, MaxContext);
    }
    min() {
        return this.getRuleContext(0, MinContext);
    }
    ZREVRANGEBYLEX() { return this.tryGetToken(RedisParser.ZREVRANGEBYLEX, 0); }
    ZREVRANGEBYSCORE() { return this.tryGetToken(RedisParser.ZREVRANGEBYSCORE, 0); }
    WITHSCORES() { return this.tryGetToken(RedisParser.WITHSCORES, 0); }
    limitOpt() {
        return this.tryGetRuleContext(0, LimitOptContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZrevrangebylex; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZrevrangebylex) {
            listener.enterCmdZrevrangebylex(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZrevrangebylex) {
            listener.exitCmdZrevrangebylex(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZrevrangebylex) {
            return visitor.visitCmdZrevrangebylex(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZscanContext extends ParserRuleContext {
    ZSCAN() { return this.getToken(RedisParser.ZSCAN, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    countOpt() {
        return this.getRuleContext(0, CountOptContext);
    }
    MATCH() { return this.tryGetToken(RedisParser.MATCH, 0); }
    pattern() {
        return this.tryGetRuleContext(0, PatternContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZscan; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZscan) {
            listener.enterCmdZscan(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZscan) {
            listener.exitCmdZscan(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZscan) {
            return visitor.visitCmdZscan(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZunionContext extends ParserRuleContext {
    ZUNION() { return this.getToken(RedisParser.ZUNION, 0); }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    WEIGHTS() { return this.tryGetToken(RedisParser.WEIGHTS, 0); }
    AGGREGATE() { return this.tryGetToken(RedisParser.AGGREGATE, 0); }
    WITHSCORES() { return this.tryGetToken(RedisParser.WITHSCORES, 0); }
    SUM() { return this.tryGetToken(RedisParser.SUM, 0); }
    MIN() { return this.tryGetToken(RedisParser.MIN, 0); }
    MAX() { return this.tryGetToken(RedisParser.MAX, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZunion; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZunion) {
            listener.enterCmdZunion(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZunion) {
            listener.exitCmdZunion(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZunion) {
            return visitor.visitCmdZunion(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdZunionstoreContext extends ParserRuleContext {
    ZUNIONSTORE() { return this.getToken(RedisParser.ZUNIONSTORE, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    WEIGHTS() { return this.tryGetToken(RedisParser.WEIGHTS, 0); }
    AGGREGATE() { return this.tryGetToken(RedisParser.AGGREGATE, 0); }
    SUM() { return this.tryGetToken(RedisParser.SUM, 0); }
    MIN() { return this.tryGetToken(RedisParser.MIN, 0); }
    MAX() { return this.tryGetToken(RedisParser.MAX, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdZunionstore; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdZunionstore) {
            listener.enterCmdZunionstore(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdZunionstore) {
            listener.exitCmdZunionstore(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdZunionstore) {
            return visitor.visitCmdZunionstore(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdScriptKillContext extends ParserRuleContext {
    SCRIPT() { return this.getToken(RedisParser.SCRIPT, 0); }
    KILL() { return this.getToken(RedisParser.KILL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdScriptKill; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdScriptKill) {
            listener.enterCmdScriptKill(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdScriptKill) {
            listener.exitCmdScriptKill(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdScriptKill) {
            return visitor.visitCmdScriptKill(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdScriptLoadContext extends ParserRuleContext {
    SCRIPT() { return this.getToken(RedisParser.SCRIPT, 0); }
    LOAD() { return this.getToken(RedisParser.LOAD, 0); }
    multiString() {
        return this.getRuleContext(0, MultiStringContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdScriptLoad; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdScriptLoad) {
            listener.enterCmdScriptLoad(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdScriptLoad) {
            listener.exitCmdScriptLoad(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdScriptLoad) {
            return visitor.visitCmdScriptLoad(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdEvalContext extends ParserRuleContext {
    EVAL() { return this.getToken(RedisParser.EVAL, 0); }
    multiString() {
        return this.getRuleContext(0, MultiStringContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdEval; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdEval) {
            listener.enterCmdEval(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdEval) {
            listener.exitCmdEval(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdEval) {
            return visitor.visitCmdEval(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdEvalshaContext extends ParserRuleContext {
    EVALSHA() { return this.getToken(RedisParser.EVALSHA, 0); }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdEvalsha; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdEvalsha) {
            listener.enterCmdEvalsha(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdEvalsha) {
            listener.exitCmdEvalsha(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdEvalsha) {
            return visitor.visitCmdEvalsha(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdScriptExistsContext extends ParserRuleContext {
    SCRIPT() { return this.getToken(RedisParser.SCRIPT, 0); }
    EXISTS() { return this.getToken(RedisParser.EXISTS, 0); }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdScriptExists; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdScriptExists) {
            listener.enterCmdScriptExists(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdScriptExists) {
            listener.exitCmdScriptExists(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdScriptExists) {
            return visitor.visitCmdScriptExists(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdScriptFlushContext extends ParserRuleContext {
    SCRIPT() { return this.getToken(RedisParser.SCRIPT, 0); }
    FLUSH() { return this.getToken(RedisParser.FLUSH, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdScriptFlush; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdScriptFlush) {
            listener.enterCmdScriptFlush(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdScriptFlush) {
            listener.exitCmdScriptFlush(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdScriptFlush) {
            return visitor.visitCmdScriptFlush(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdExecContext extends ParserRuleContext {
    EXEC() { return this.getToken(RedisParser.EXEC, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdExec; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdExec) {
            listener.enterCmdExec(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdExec) {
            listener.exitCmdExec(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdExec) {
            return visitor.visitCmdExec(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdWatchContext extends ParserRuleContext {
    WATCH() { return this.getToken(RedisParser.WATCH, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdWatch; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdWatch) {
            listener.enterCmdWatch(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdWatch) {
            listener.exitCmdWatch(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdWatch) {
            return visitor.visitCmdWatch(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdDiscardContext extends ParserRuleContext {
    DISCARD() { return this.getToken(RedisParser.DISCARD, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdDiscard; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdDiscard) {
            listener.enterCmdDiscard(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdDiscard) {
            listener.exitCmdDiscard(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdDiscard) {
            return visitor.visitCmdDiscard(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdUnwatchContext extends ParserRuleContext {
    UNWATCH() { return this.getToken(RedisParser.UNWATCH, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdUnwatch; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdUnwatch) {
            listener.enterCmdUnwatch(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdUnwatch) {
            listener.exitCmdUnwatch(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdUnwatch) {
            return visitor.visitCmdUnwatch(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdMultiContext extends ParserRuleContext {
    MULTI() { return this.getToken(RedisParser.MULTI, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdMulti; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdMulti) {
            listener.enterCmdMulti(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdMulti) {
            listener.exitCmdMulti(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdMulti) {
            return visitor.visitCmdMulti(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPfmergeContext extends ParserRuleContext {
    PFMERGE() { return this.getToken(RedisParser.PFMERGE, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPfmerge; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPfmerge) {
            listener.enterCmdPfmerge(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPfmerge) {
            listener.exitCmdPfmerge(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPfmerge) {
            return visitor.visitCmdPfmerge(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPfaddContext extends ParserRuleContext {
    PFADD() { return this.getToken(RedisParser.PFADD, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    fieldName(i) {
        if (i === undefined) {
            return this.getRuleContexts(FieldNameContext);
        }
        else {
            return this.getRuleContext(i, FieldNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPfadd; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPfadd) {
            listener.enterCmdPfadd(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPfadd) {
            listener.exitCmdPfadd(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPfadd) {
            return visitor.visitCmdPfadd(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPfcountContext extends ParserRuleContext {
    PFCOUNT() { return this.getToken(RedisParser.PFCOUNT, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPfcount; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPfcount) {
            listener.enterCmdPfcount(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPfcount) {
            listener.exitCmdPfcount(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPfcount) {
            return visitor.visitCmdPfcount(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSsubscribeContext extends ParserRuleContext {
    SSUBSCRIBE() { return this.tryGetToken(RedisParser.SSUBSCRIBE, 0); }
    SUBSCRIBE() { return this.tryGetToken(RedisParser.SUBSCRIBE, 0); }
    PSUBSCRIBE() { return this.tryGetToken(RedisParser.PSUBSCRIBE, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSsubscribe; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSsubscribe) {
            listener.enterCmdSsubscribe(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSsubscribe) {
            listener.exitCmdSsubscribe(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSsubscribe) {
            return visitor.visitCmdSsubscribe(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdUnsubscribeContext extends ParserRuleContext {
    UNSUBSCRIBE() { return this.tryGetToken(RedisParser.UNSUBSCRIBE, 0); }
    PUNSUBSCRIBE() { return this.tryGetToken(RedisParser.PUNSUBSCRIBE, 0); }
    SUNSUBSCRIBE() { return this.tryGetToken(RedisParser.SUNSUBSCRIBE, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdUnsubscribe; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdUnsubscribe) {
            listener.enterCmdUnsubscribe(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdUnsubscribe) {
            listener.exitCmdUnsubscribe(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdUnsubscribe) {
            return visitor.visitCmdUnsubscribe(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPusubnumpatContext extends ParserRuleContext {
    PUBSUB() { return this.getToken(RedisParser.PUBSUB, 0); }
    NUMPAT() { return this.getToken(RedisParser.NUMPAT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPusubnumpat; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPusubnumpat) {
            listener.enterCmdPusubnumpat(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPusubnumpat) {
            listener.exitCmdPusubnumpat(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPusubnumpat) {
            return visitor.visitCmdPusubnumpat(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPusubnumsubContext extends ParserRuleContext {
    PUBSUB() { return this.getToken(RedisParser.PUBSUB, 0); }
    NUMSUB() { return this.tryGetToken(RedisParser.NUMSUB, 0); }
    SHARDNUMSUB() { return this.tryGetToken(RedisParser.SHARDNUMSUB, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPusubnumsub; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPusubnumsub) {
            listener.enterCmdPusubnumsub(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPusubnumsub) {
            listener.exitCmdPusubnumsub(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPusubnumsub) {
            return visitor.visitCmdPusubnumsub(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPusubchannelsContext extends ParserRuleContext {
    PUBSUB() { return this.getToken(RedisParser.PUBSUB, 0); }
    CHANNELS() { return this.tryGetToken(RedisParser.CHANNELS, 0); }
    SHARDCHANNELS() { return this.tryGetToken(RedisParser.SHARDCHANNELS, 0); }
    keyName() {
        return this.tryGetRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPusubchannels; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPusubchannels) {
            listener.enterCmdPusubchannels(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPusubchannels) {
            listener.exitCmdPusubchannels(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPusubchannels) {
            return visitor.visitCmdPusubchannels(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPublishContext extends ParserRuleContext {
    PUBLISH() { return this.getToken(RedisParser.PUBLISH, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    fieldName() {
        return this.getRuleContext(0, FieldNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPublish; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPublish) {
            listener.enterCmdPublish(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPublish) {
            listener.exitCmdPublish(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPublish) {
            return visitor.visitCmdPublish(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdAskingContext extends ParserRuleContext {
    ASKING() { return this.getToken(RedisParser.ASKING, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdAsking; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdAsking) {
            listener.enterCmdAsking(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdAsking) {
            listener.exitCmdAsking(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdAsking) {
            return visitor.visitCmdAsking(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdReadonlyContext extends ParserRuleContext {
    READONLY() { return this.getToken(RedisParser.READONLY, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdReadonly; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdReadonly) {
            listener.enterCmdReadonly(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdReadonly) {
            listener.exitCmdReadonly(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdReadonly) {
            return visitor.visitCmdReadonly(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdReadwriteContext extends ParserRuleContext {
    READWRITE() { return this.getToken(RedisParser.READWRITE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdReadwrite; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdReadwrite) {
            listener.enterCmdReadwrite(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdReadwrite) {
            listener.exitCmdReadwrite(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdReadwrite) {
            return visitor.visitCmdReadwrite(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdAddDelSlotsContext extends ParserRuleContext {
    CLUSTER() { return this.getToken(RedisParser.CLUSTER, 0); }
    ADDSLOTS() { return this.tryGetToken(RedisParser.ADDSLOTS, 0); }
    DELSLOTS() { return this.tryGetToken(RedisParser.DELSLOTS, 0); }
    intValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(IntValueContext);
        }
        else {
            return this.getRuleContext(i, IntValueContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdAddDelSlots; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdAddDelSlots) {
            listener.enterCmdAddDelSlots(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdAddDelSlots) {
            listener.exitCmdAddDelSlots(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdAddDelSlots) {
            return visitor.visitCmdAddDelSlots(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdCountKeysInSlotContext extends ParserRuleContext {
    CLUSTER() { return this.getToken(RedisParser.CLUSTER, 0); }
    COUNTKEYSINSLOT() { return this.getToken(RedisParser.COUNTKEYSINSLOT, 0); }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdCountKeysInSlot; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdCountKeysInSlot) {
            listener.enterCmdCountKeysInSlot(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdCountKeysInSlot) {
            listener.exitCmdCountKeysInSlot(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdCountKeysInSlot) {
            return visitor.visitCmdCountKeysInSlot(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdFailOverContext extends ParserRuleContext {
    CLUSTER() { return this.getToken(RedisParser.CLUSTER, 0); }
    FAILOVER() { return this.getToken(RedisParser.FAILOVER, 0); }
    FORCE() { return this.tryGetToken(RedisParser.FORCE, 0); }
    TAKEOVER() { return this.tryGetToken(RedisParser.TAKEOVER, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdFailOver; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdFailOver) {
            listener.enterCmdFailOver(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdFailOver) {
            listener.exitCmdFailOver(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdFailOver) {
            return visitor.visitCmdFailOver(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdForgetContext extends ParserRuleContext {
    CLUSTER() { return this.getToken(RedisParser.CLUSTER, 0); }
    FORGET() { return this.getToken(RedisParser.FORGET, 0); }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdForget; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdForget) {
            listener.enterCmdForget(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdForget) {
            listener.exitCmdForget(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdForget) {
            return visitor.visitCmdForget(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdGetKeysInSlotContext extends ParserRuleContext {
    CLUSTER() { return this.getToken(RedisParser.CLUSTER, 0); }
    GETKEYSINSLOT() { return this.getToken(RedisParser.GETKEYSINSLOT, 0); }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    cout() {
        return this.getRuleContext(0, CoutContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdGetKeysInSlot; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdGetKeysInSlot) {
            listener.enterCmdGetKeysInSlot(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdGetKeysInSlot) {
            listener.exitCmdGetKeysInSlot(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdGetKeysInSlot) {
            return visitor.visitCmdGetKeysInSlot(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdClusterOrderContext extends ParserRuleContext {
    CLUSTER() { return this.getToken(RedisParser.CLUSTER, 0); }
    INFO() { return this.tryGetToken(RedisParser.INFO, 0); }
    BUMPEPOCH() { return this.tryGetToken(RedisParser.BUMPEPOCH, 0); }
    LINKS() { return this.tryGetToken(RedisParser.LINKS, 0); }
    FLUSHSLOTS() { return this.tryGetToken(RedisParser.FLUSHSLOTS, 0); }
    MYID() { return this.tryGetToken(RedisParser.MYID, 0); }
    MYSHARDID() { return this.tryGetToken(RedisParser.MYSHARDID, 0); }
    NODES() { return this.tryGetToken(RedisParser.NODES, 0); }
    SAVECONFIG() { return this.tryGetToken(RedisParser.SAVECONFIG, 0); }
    SHARDS() { return this.tryGetToken(RedisParser.SHARDS, 0); }
    SLOTS() { return this.tryGetToken(RedisParser.SLOTS, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdClusterOrder; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdClusterOrder) {
            listener.enterCmdClusterOrder(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdClusterOrder) {
            listener.exitCmdClusterOrder(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdClusterOrder) {
            return visitor.visitCmdClusterOrder(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdKeySlotContext extends ParserRuleContext {
    CLUSTER() { return this.getToken(RedisParser.CLUSTER, 0); }
    KEYSLOT() { return this.getToken(RedisParser.KEYSLOT, 0); }
    keyName() {
        return this.getRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdKeySlot; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdKeySlot) {
            listener.enterCmdKeySlot(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdKeySlot) {
            listener.exitCmdKeySlot(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdKeySlot) {
            return visitor.visitCmdKeySlot(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdMeetContext extends ParserRuleContext {
    CLUSTER() { return this.getToken(RedisParser.CLUSTER, 0); }
    MEET() { return this.getToken(RedisParser.MEET, 0); }
    HOST() { return this.getToken(RedisParser.HOST, 0); }
    port() {
        return this.getRuleContext(0, PortContext);
    }
    intValue() {
        return this.tryGetRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdMeet; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdMeet) {
            listener.enterCmdMeet(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdMeet) {
            listener.exitCmdMeet(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdMeet) {
            return visitor.visitCmdMeet(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdReplicaesSlaveContext extends ParserRuleContext {
    CLUSTER() { return this.getToken(RedisParser.CLUSTER, 0); }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    REPLICAS() { return this.tryGetToken(RedisParser.REPLICAS, 0); }
    REPLICATE() { return this.tryGetToken(RedisParser.REPLICATE, 0); }
    SLAVES() { return this.tryGetToken(RedisParser.SLAVES, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdReplicaesSlave; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdReplicaesSlave) {
            listener.enterCmdReplicaesSlave(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdReplicaesSlave) {
            listener.exitCmdReplicaesSlave(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdReplicaesSlave) {
            return visitor.visitCmdReplicaesSlave(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdResetContext extends ParserRuleContext {
    CLUSTER() { return this.getToken(RedisParser.CLUSTER, 0); }
    RESET() { return this.getToken(RedisParser.RESET, 0); }
    HARD() { return this.tryGetToken(RedisParser.HARD, 0); }
    SOFT() { return this.tryGetToken(RedisParser.SOFT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdReset; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdReset) {
            listener.enterCmdReset(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdReset) {
            listener.exitCmdReset(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdReset) {
            return visitor.visitCmdReset(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSetSlotContext extends ParserRuleContext {
    CLUSTER() { return this.getToken(RedisParser.CLUSTER, 0); }
    SETSLOT() { return this.getToken(RedisParser.SETSLOT, 0); }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    STABLE() { return this.tryGetToken(RedisParser.STABLE, 0); }
    MIGRATING() { return this.tryGetToken(RedisParser.MIGRATING, 0); }
    stringValue() {
        return this.tryGetRuleContext(0, StringValueContext);
    }
    IMPORTING() { return this.tryGetToken(RedisParser.IMPORTING, 0); }
    NODE() { return this.tryGetToken(RedisParser.NODE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSetSlot; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSetSlot) {
            listener.enterCmdSetSlot(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSetSlot) {
            listener.exitCmdSetSlot(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSetSlot) {
            return visitor.visitCmdSetSlot(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdFlushdbContext extends ParserRuleContext {
    FLUSHDB() { return this.getToken(RedisParser.FLUSHDB, 0); }
    ASYNC() { return this.tryGetToken(RedisParser.ASYNC, 0); }
    SYNC() { return this.tryGetToken(RedisParser.SYNC, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdFlushdb; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdFlushdb) {
            listener.enterCmdFlushdb(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdFlushdb) {
            listener.exitCmdFlushdb(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdFlushdb) {
            return visitor.visitCmdFlushdb(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdEchoContext extends ParserRuleContext {
    ECHO() { return this.getToken(RedisParser.ECHO, 0); }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdEcho; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdEcho) {
            listener.enterCmdEcho(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdEcho) {
            listener.exitCmdEcho(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdEcho) {
            return visitor.visitCmdEcho(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSaveContext extends ParserRuleContext {
    SAVE() { return this.getToken(RedisParser.SAVE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSave; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSave) {
            listener.enterCmdSave(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSave) {
            listener.exitCmdSave(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSave) {
            return visitor.visitCmdSave(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSlowlogContext extends ParserRuleContext {
    SLOWLOG() { return this.getToken(RedisParser.SLOWLOG, 0); }
    sqlStatement() {
        return this.getRuleContext(0, SqlStatementContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSlowlog; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSlowlog) {
            listener.enterCmdSlowlog(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSlowlog) {
            listener.exitCmdSlowlog(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSlowlog) {
            return visitor.visitCmdSlowlog(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdLastsaveContext extends ParserRuleContext {
    LASTSAVE() { return this.getToken(RedisParser.LASTSAVE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdLastsave; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdLastsave) {
            listener.enterCmdLastsave(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdLastsave) {
            listener.exitCmdLastsave(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdLastsave) {
            return visitor.visitCmdLastsave(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdConfigContext extends ParserRuleContext {
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdConfig; }
    copyFrom(ctx) {
        super.copyFrom(ctx);
    }
}
export class CmdConfigGetContext extends CmdConfigContext {
    CONFIG() { return this.getToken(RedisParser.CONFIG, 0); }
    GET() { return this.tryGetToken(RedisParser.GET, 0); }
    pattern() {
        return this.tryGetRuleContext(0, PatternContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdConfigGet) {
            listener.enterCmdConfigGet(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdConfigGet) {
            listener.exitCmdConfigGet(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdConfigGet) {
            return visitor.visitCmdConfigGet(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdConfigSetContext extends CmdConfigContext {
    SET() { return this.tryGetToken(RedisParser.SET, 0); }
    stringValue(i) {
        if (i === undefined) {
            return this.getRuleContexts(StringValueContext);
        }
        else {
            return this.getRuleContext(i, StringValueContext);
        }
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdConfigSet) {
            listener.enterCmdConfigSet(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdConfigSet) {
            listener.exitCmdConfigSet(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdConfigSet) {
            return visitor.visitCmdConfigSet(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdConfigResetContext extends CmdConfigContext {
    RESETSTAT() { return this.getToken(RedisParser.RESETSTAT, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdConfigReset) {
            listener.enterCmdConfigReset(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdConfigReset) {
            listener.exitCmdConfigReset(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdConfigReset) {
            return visitor.visitCmdConfigReset(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdConfigRewriteContext extends CmdConfigContext {
    REWRITE() { return this.getToken(RedisParser.REWRITE, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdConfigRewrite) {
            listener.enterCmdConfigRewrite(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdConfigRewrite) {
            listener.exitCmdConfigRewrite(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdConfigRewrite) {
            return visitor.visitCmdConfigRewrite(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdClientContext extends ParserRuleContext {
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdClient; }
    copyFrom(ctx) {
        super.copyFrom(ctx);
    }
}
export class CmdClientPauseContext extends CmdClientContext {
    CLIENT() { return this.getToken(RedisParser.CLIENT, 0); }
    PAUSE() { return this.tryGetToken(RedisParser.PAUSE, 0); }
    intValue() {
        return this.tryGetRuleContext(0, IntValueContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdClientPause) {
            listener.enterCmdClientPause(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdClientPause) {
            listener.exitCmdClientPause(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdClientPause) {
            return visitor.visitCmdClientPause(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdClientKillContext extends CmdClientContext {
    KILL() { return this.tryGetToken(RedisParser.KILL, 0); }
    HOST_PORT() { return this.tryGetToken(RedisParser.HOST_PORT, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdClientKill) {
            listener.enterCmdClientKill(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdClientKill) {
            listener.exitCmdClientKill(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdClientKill) {
            return visitor.visitCmdClientKill(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdClientGetnameContext extends CmdClientContext {
    GETNAME() { return this.getToken(RedisParser.GETNAME, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdClientGetname) {
            listener.enterCmdClientGetname(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdClientGetname) {
            listener.exitCmdClientGetname(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdClientGetname) {
            return visitor.visitCmdClientGetname(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdClientSetnameContext extends CmdClientContext {
    SETNAME() { return this.tryGetToken(RedisParser.SETNAME, 0); }
    stringValue() {
        return this.tryGetRuleContext(0, StringValueContext);
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdClientSetname) {
            listener.enterCmdClientSetname(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdClientSetname) {
            listener.exitCmdClientSetname(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdClientSetname) {
            return visitor.visitCmdClientSetname(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdClientListContext extends CmdClientContext {
    LIST() { return this.getToken(RedisParser.LIST, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdClientList) {
            listener.enterCmdClientList(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdClientList) {
            listener.exitCmdClientList(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdClientList) {
            return visitor.visitCmdClientList(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdShutdownContext extends ParserRuleContext {
    SHUTDOWN() { return this.getToken(RedisParser.SHUTDOWN, 0); }
    NOSAVE() { return this.tryGetToken(RedisParser.NOSAVE, 0); }
    SAVE() { return this.tryGetToken(RedisParser.SAVE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdShutdown; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdShutdown) {
            listener.enterCmdShutdown(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdShutdown) {
            listener.exitCmdShutdown(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdShutdown) {
            return visitor.visitCmdShutdown(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSyncContext extends ParserRuleContext {
    SYNC() { return this.getToken(RedisParser.SYNC, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSync; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSync) {
            listener.enterCmdSync(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSync) {
            listener.exitCmdSync(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSync) {
            return visitor.visitCmdSync(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdRoleContext extends ParserRuleContext {
    ROLE() { return this.getToken(RedisParser.ROLE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdRole; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdRole) {
            listener.enterCmdRole(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdRole) {
            listener.exitCmdRole(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdRole) {
            return visitor.visitCmdRole(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdMonitorContext extends ParserRuleContext {
    MONITOR() { return this.getToken(RedisParser.MONITOR, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdMonitor; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdMonitor) {
            listener.enterCmdMonitor(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdMonitor) {
            listener.exitCmdMonitor(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdMonitor) {
            return visitor.visitCmdMonitor(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSlaveofContext extends ParserRuleContext {
    SLAVEOF() { return this.getToken(RedisParser.SLAVEOF, 0); }
    HOST() { return this.getToken(RedisParser.HOST, 0); }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSlaveof; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSlaveof) {
            listener.enterCmdSlaveof(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSlaveof) {
            listener.exitCmdSlaveof(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSlaveof) {
            return visitor.visitCmdSlaveof(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdFlushallContext extends ParserRuleContext {
    FLUSHALL() { return this.getToken(RedisParser.FLUSHALL, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdFlushall; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdFlushall) {
            listener.enterCmdFlushall(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdFlushall) {
            listener.exitCmdFlushall(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdFlushall) {
            return visitor.visitCmdFlushall(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdSelectContext extends ParserRuleContext {
    SELECT() { return this.getToken(RedisParser.SELECT, 0); }
    intValue() {
        return this.getRuleContext(0, IntValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdSelect; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdSelect) {
            listener.enterCmdSelect(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdSelect) {
            listener.exitCmdSelect(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdSelect) {
            return visitor.visitCmdSelect(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdPingContext extends ParserRuleContext {
    PING() { return this.getToken(RedisParser.PING, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdPing; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdPing) {
            listener.enterCmdPing(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdPing) {
            listener.exitCmdPing(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdPing) {
            return visitor.visitCmdPing(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdQuitContext extends ParserRuleContext {
    QUIT() { return this.getToken(RedisParser.QUIT, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdQuit; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdQuit) {
            listener.enterCmdQuit(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdQuit) {
            listener.exitCmdQuit(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdQuit) {
            return visitor.visitCmdQuit(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdAuthContext extends ParserRuleContext {
    AUTH() { return this.getToken(RedisParser.AUTH, 0); }
    stringValue() {
        return this.getRuleContext(0, StringValueContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdAuth; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdAuth) {
            listener.enterCmdAuth(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdAuth) {
            listener.exitCmdAuth(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdAuth) {
            return visitor.visitCmdAuth(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdDbsizeContext extends ParserRuleContext {
    DBSIZE() { return this.getToken(RedisParser.DBSIZE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdDbsize; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdDbsize) {
            listener.enterCmdDbsize(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdDbsize) {
            listener.exitCmdDbsize(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdDbsize) {
            return visitor.visitCmdDbsize(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdBgrewriteaofContext extends ParserRuleContext {
    BGREWRITEAOF() { return this.getToken(RedisParser.BGREWRITEAOF, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdBgrewriteaof; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdBgrewriteaof) {
            listener.enterCmdBgrewriteaof(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdBgrewriteaof) {
            listener.exitCmdBgrewriteaof(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdBgrewriteaof) {
            return visitor.visitCmdBgrewriteaof(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdTimeContext extends ParserRuleContext {
    TIME() { return this.getToken(RedisParser.TIME, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdTime; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdTime) {
            listener.enterCmdTime(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdTime) {
            listener.exitCmdTime(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdTime) {
            return visitor.visitCmdTime(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdInfoContext extends ParserRuleContext {
    INFO() { return this.getToken(RedisParser.INFO, 0); }
    infoOpt(i) {
        if (i === undefined) {
            return this.getRuleContexts(InfoOptContext);
        }
        else {
            return this.getRuleContext(i, InfoOptContext);
        }
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdInfo; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdInfo) {
            listener.enterCmdInfo(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdInfo) {
            listener.exitCmdInfo(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdInfo) {
            return visitor.visitCmdInfo(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdBgsaveContext extends ParserRuleContext {
    BGSAVE() { return this.getToken(RedisParser.BGSAVE, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdBgsave; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdBgsave) {
            listener.enterCmdBgsave(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdBgsave) {
            listener.exitCmdBgsave(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdBgsave) {
            return visitor.visitCmdBgsave(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdCmdContext extends ParserRuleContext {
    COMMAND() { return this.getToken(RedisParser.COMMAND, 0); }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdCmd; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdCmd) {
            listener.enterCmdCmd(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdCmd) {
            listener.exitCmdCmd(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdCmd) {
            return visitor.visitCmdCmd(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdCmdxContext extends ParserRuleContext {
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdCmdx; }
    copyFrom(ctx) {
        super.copyFrom(ctx);
    }
}
export class CmdCmdInfoContext extends CmdCmdxContext {
    COMMAND() { return this.getToken(RedisParser.COMMAND, 0); }
    INFO() { return this.tryGetToken(RedisParser.INFO, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdCmdInfo) {
            listener.enterCmdCmdInfo(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdCmdInfo) {
            listener.exitCmdCmdInfo(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdCmdInfo) {
            return visitor.visitCmdCmdInfo(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdCmdGetkeysContext extends CmdCmdxContext {
    GETKEYS() { return this.tryGetToken(RedisParser.GETKEYS, 0); }
    keyName(i) {
        if (i === undefined) {
            return this.getRuleContexts(KeyNameContext);
        }
        else {
            return this.getRuleContext(i, KeyNameContext);
        }
    }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdCmdGetkeys) {
            listener.enterCmdCmdGetkeys(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdCmdGetkeys) {
            listener.exitCmdCmdGetkeys(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdCmdGetkeys) {
            return visitor.visitCmdCmdGetkeys(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdCmdCountContext extends CmdCmdxContext {
    COUNT() { return this.getToken(RedisParser.COUNT, 0); }
    constructor(ctx) {
        super(ctx.parent, ctx.invokingState);
        this.copyFrom(ctx);
    }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdCmdCount) {
            listener.enterCmdCmdCount(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdCmdCount) {
            listener.exitCmdCmdCount(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdCmdCount) {
            return visitor.visitCmdCmdCount(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class CmdDebugContext extends ParserRuleContext {
    DEBUG() { return this.getToken(RedisParser.DEBUG, 0); }
    SEGFAULT() { return this.tryGetToken(RedisParser.SEGFAULT, 0); }
    OBJECT() { return this.tryGetToken(RedisParser.OBJECT, 0); }
    keyName() {
        return this.tryGetRuleContext(0, KeyNameContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_cmdDebug; }
    // @Override
    enterRule(listener) {
        if (listener.enterCmdDebug) {
            listener.enterCmdDebug(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitCmdDebug) {
            listener.exitCmdDebug(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitCmdDebug) {
            return visitor.visitCmdDebug(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
export class SqlStatementContext extends ParserRuleContext {
    cmdCopy() {
        return this.tryGetRuleContext(0, CmdCopyContext);
    }
    cmdDel() {
        return this.tryGetRuleContext(0, CmdDelContext);
    }
    cmdDump() {
        return this.tryGetRuleContext(0, CmdDumpContext);
    }
    cmdExists() {
        return this.tryGetRuleContext(0, CmdExistsContext);
    }
    cmdExpire() {
        return this.tryGetRuleContext(0, CmdExpireContext);
    }
    cmdExpireat() {
        return this.tryGetRuleContext(0, CmdExpireatContext);
    }
    cmdExpireTime() {
        return this.tryGetRuleContext(0, CmdExpireTimeContext);
    }
    cmdKeys() {
        return this.tryGetRuleContext(0, CmdKeysContext);
    }
    cmdMove() {
        return this.tryGetRuleContext(0, CmdMoveContext);
    }
    cmdObject() {
        return this.tryGetRuleContext(0, CmdObjectContext);
    }
    cmdPersist() {
        return this.tryGetRuleContext(0, CmdPersistContext);
    }
    cmdPexpire() {
        return this.tryGetRuleContext(0, CmdPexpireContext);
    }
    cmdPexpireat() {
        return this.tryGetRuleContext(0, CmdPexpireatContext);
    }
    cmdPExpireTime() {
        return this.tryGetRuleContext(0, CmdPExpireTimeContext);
    }
    cmdTtl() {
        return this.tryGetRuleContext(0, CmdTtlContext);
    }
    cmdPttl() {
        return this.tryGetRuleContext(0, CmdPttlContext);
    }
    cmdRandomkey() {
        return this.tryGetRuleContext(0, CmdRandomkeyContext);
    }
    cmdRename() {
        return this.tryGetRuleContext(0, CmdRenameContext);
    }
    cmdRenamenx() {
        return this.tryGetRuleContext(0, CmdRenamenxContext);
    }
    cmdRestore() {
        return this.tryGetRuleContext(0, CmdRestoreContext);
    }
    cmdScan() {
        return this.tryGetRuleContext(0, CmdScanContext);
    }
    cmdSort() {
        return this.tryGetRuleContext(0, CmdSortContext);
    }
    cmdSortro() {
        return this.tryGetRuleContext(0, CmdSortroContext);
    }
    cmdTouch() {
        return this.tryGetRuleContext(0, CmdTouchContext);
    }
    cmdType() {
        return this.tryGetRuleContext(0, CmdTypeContext);
    }
    cmdUnlink() {
        return this.tryGetRuleContext(0, CmdUnlinkContext);
    }
    cmdWait() {
        return this.tryGetRuleContext(0, CmdWaitContext);
    }
    cmdAppend() {
        return this.tryGetRuleContext(0, CmdAppendContext);
    }
    cmdDecr() {
        return this.tryGetRuleContext(0, CmdDecrContext);
    }
    cmdDecrby() {
        return this.tryGetRuleContext(0, CmdDecrbyContext);
    }
    cmdGet() {
        return this.tryGetRuleContext(0, CmdGetContext);
    }
    cmdGetdel() {
        return this.tryGetRuleContext(0, CmdGetdelContext);
    }
    cmdGetex() {
        return this.tryGetRuleContext(0, CmdGetexContext);
    }
    cmdGetrange() {
        return this.tryGetRuleContext(0, CmdGetrangeContext);
    }
    cmdGetset() {
        return this.tryGetRuleContext(0, CmdGetsetContext);
    }
    cmdGetbit() {
        return this.tryGetRuleContext(0, CmdGetbitContext);
    }
    cmdInc() {
        return this.tryGetRuleContext(0, CmdIncContext);
    }
    cmdLcs() {
        return this.tryGetRuleContext(0, CmdLcsContext);
    }
    cmdMget() {
        return this.tryGetRuleContext(0, CmdMgetContext);
    }
    cmdMset() {
        return this.tryGetRuleContext(0, CmdMsetContext);
    }
    cmdMsetnx() {
        return this.tryGetRuleContext(0, CmdMsetnxContext);
    }
    cmdSetex() {
        return this.tryGetRuleContext(0, CmdSetexContext);
    }
    cmdSet() {
        return this.tryGetRuleContext(0, CmdSetContext);
    }
    cmdSetnx() {
        return this.tryGetRuleContext(0, CmdSetnxContext);
    }
    cmdSetrange() {
        return this.tryGetRuleContext(0, CmdSetrangeContext);
    }
    cmdSetbit() {
        return this.tryGetRuleContext(0, CmdSetbitContext);
    }
    cmdStrlen() {
        return this.tryGetRuleContext(0, CmdStrlenContext);
    }
    cmdSubstr() {
        return this.tryGetRuleContext(0, CmdSubstrContext);
    }
    cmdHdelMget() {
        return this.tryGetRuleContext(0, CmdHdelMgetContext);
    }
    cmdHexistsGetKeysStrlen() {
        return this.tryGetRuleContext(0, CmdHexistsGetKeysStrlenContext);
    }
    cmdHgetallLenVals() {
        return this.tryGetRuleContext(0, CmdHgetallLenValsContext);
    }
    cmdHincrby() {
        return this.tryGetRuleContext(0, CmdHincrbyContext);
    }
    cmdHmsetHset() {
        return this.tryGetRuleContext(0, CmdHmsetHsetContext);
    }
    cmdHrandfield() {
        return this.tryGetRuleContext(0, CmdHrandfieldContext);
    }
    cmdHscan() {
        return this.tryGetRuleContext(0, CmdHscanContext);
    }
    cmdHsetnx() {
        return this.tryGetRuleContext(0, CmdHsetnxContext);
    }
    cmdBlmove() {
        return this.tryGetRuleContext(0, CmdBlmoveContext);
    }
    cmdLmpop() {
        return this.tryGetRuleContext(0, CmdLmpopContext);
    }
    cmdBpop() {
        return this.tryGetRuleContext(0, CmdBpopContext);
    }
    cmdBrpoplpush() {
        return this.tryGetRuleContext(0, CmdBrpoplpushContext);
    }
    cmdLindex() {
        return this.tryGetRuleContext(0, CmdLindexContext);
    }
    cmdLinsert() {
        return this.tryGetRuleContext(0, CmdLinsertContext);
    }
    cmdLlen() {
        return this.tryGetRuleContext(0, CmdLlenContext);
    }
    cmdLmove() {
        return this.tryGetRuleContext(0, CmdLmoveContext);
    }
    cmdPop() {
        return this.tryGetRuleContext(0, CmdPopContext);
    }
    cmdLpos() {
        return this.tryGetRuleContext(0, CmdLposContext);
    }
    cmdPush() {
        return this.tryGetRuleContext(0, CmdPushContext);
    }
    cmdLrangeTrim() {
        return this.tryGetRuleContext(0, CmdLrangeTrimContext);
    }
    cmdLremSet() {
        return this.tryGetRuleContext(0, CmdLremSetContext);
    }
    cmdRpoplpush() {
        return this.tryGetRuleContext(0, CmdRpoplpushContext);
    }
    cmdSadd() {
        return this.tryGetRuleContext(0, CmdSaddContext);
    }
    cmdScard() {
        return this.tryGetRuleContext(0, CmdScardContext);
    }
    cmdSdiff() {
        return this.tryGetRuleContext(0, CmdSdiffContext);
    }
    cmdSdiffstore() {
        return this.tryGetRuleContext(0, CmdSdiffstoreContext);
    }
    cmdSintercard() {
        return this.tryGetRuleContext(0, CmdSintercardContext);
    }
    cmdSismember() {
        return this.tryGetRuleContext(0, CmdSismemberContext);
    }
    cmdSmove() {
        return this.tryGetRuleContext(0, CmdSmoveContext);
    }
    cmdSpop() {
        return this.tryGetRuleContext(0, CmdSpopContext);
    }
    cmdSscan() {
        return this.tryGetRuleContext(0, CmdSscanContext);
    }
    cmdBzmpop() {
        return this.tryGetRuleContext(0, CmdBzmpopContext);
    }
    cmdBzpopmax() {
        return this.tryGetRuleContext(0, CmdBzpopmaxContext);
    }
    cmdZadd() {
        return this.tryGetRuleContext(0, CmdZaddContext);
    }
    cmdZcard() {
        return this.tryGetRuleContext(0, CmdZcardContext);
    }
    cmdZcount() {
        return this.tryGetRuleContext(0, CmdZcountContext);
    }
    cmdZdiff() {
        return this.tryGetRuleContext(0, CmdZdiffContext);
    }
    cmdZdiffstore() {
        return this.tryGetRuleContext(0, CmdZdiffstoreContext);
    }
    cmdZincrby() {
        return this.tryGetRuleContext(0, CmdZincrbyContext);
    }
    cmdZinter() {
        return this.tryGetRuleContext(0, CmdZinterContext);
    }
    cmdZintercard() {
        return this.tryGetRuleContext(0, CmdZintercardContext);
    }
    cmdZinterstore() {
        return this.tryGetRuleContext(0, CmdZinterstoreContext);
    }
    cmdZmpop() {
        return this.tryGetRuleContext(0, CmdZmpopContext);
    }
    cmdZmscore() {
        return this.tryGetRuleContext(0, CmdZmscoreContext);
    }
    cmdZpopmax() {
        return this.tryGetRuleContext(0, CmdZpopmaxContext);
    }
    cmdZrandmember() {
        return this.tryGetRuleContext(0, CmdZrandmemberContext);
    }
    cmdZrange() {
        return this.tryGetRuleContext(0, CmdZrangeContext);
    }
    cmdZrangebylex() {
        return this.tryGetRuleContext(0, CmdZrangebylexContext);
    }
    cmdZrangebyscore() {
        return this.tryGetRuleContext(0, CmdZrangebyscoreContext);
    }
    cmdZrangestore() {
        return this.tryGetRuleContext(0, CmdZrangestoreContext);
    }
    cmdZrank() {
        return this.tryGetRuleContext(0, CmdZrankContext);
    }
    cmdZremrangebyrank() {
        return this.tryGetRuleContext(0, CmdZremrangebyrankContext);
    }
    cmdZrevrange() {
        return this.tryGetRuleContext(0, CmdZrevrangeContext);
    }
    cmdZrevrangebylex() {
        return this.tryGetRuleContext(0, CmdZrevrangebylexContext);
    }
    cmdZscan() {
        return this.tryGetRuleContext(0, CmdZscanContext);
    }
    cmdZunion() {
        return this.tryGetRuleContext(0, CmdZunionContext);
    }
    cmdZunionstore() {
        return this.tryGetRuleContext(0, CmdZunionstoreContext);
    }
    cmdScriptKill() {
        return this.tryGetRuleContext(0, CmdScriptKillContext);
    }
    cmdScriptLoad() {
        return this.tryGetRuleContext(0, CmdScriptLoadContext);
    }
    cmdEval() {
        return this.tryGetRuleContext(0, CmdEvalContext);
    }
    cmdEvalsha() {
        return this.tryGetRuleContext(0, CmdEvalshaContext);
    }
    cmdScriptExists() {
        return this.tryGetRuleContext(0, CmdScriptExistsContext);
    }
    cmdScriptFlush() {
        return this.tryGetRuleContext(0, CmdScriptFlushContext);
    }
    cmdExec() {
        return this.tryGetRuleContext(0, CmdExecContext);
    }
    cmdWatch() {
        return this.tryGetRuleContext(0, CmdWatchContext);
    }
    cmdDiscard() {
        return this.tryGetRuleContext(0, CmdDiscardContext);
    }
    cmdUnwatch() {
        return this.tryGetRuleContext(0, CmdUnwatchContext);
    }
    cmdMulti() {
        return this.tryGetRuleContext(0, CmdMultiContext);
    }
    cmdPfmerge() {
        return this.tryGetRuleContext(0, CmdPfmergeContext);
    }
    cmdPfadd() {
        return this.tryGetRuleContext(0, CmdPfaddContext);
    }
    cmdPfcount() {
        return this.tryGetRuleContext(0, CmdPfcountContext);
    }
    cmdEcho() {
        return this.tryGetRuleContext(0, CmdEchoContext);
    }
    cmdSelect() {
        return this.tryGetRuleContext(0, CmdSelectContext);
    }
    cmdPing() {
        return this.tryGetRuleContext(0, CmdPingContext);
    }
    cmdSave() {
        return this.tryGetRuleContext(0, CmdSaveContext);
    }
    cmdSlowlog() {
        return this.tryGetRuleContext(0, CmdSlowlogContext);
    }
    cmdLastsave() {
        return this.tryGetRuleContext(0, CmdLastsaveContext);
    }
    cmdConfig() {
        return this.tryGetRuleContext(0, CmdConfigContext);
    }
    cmdClient() {
        return this.tryGetRuleContext(0, CmdClientContext);
    }
    cmdShutdown() {
        return this.tryGetRuleContext(0, CmdShutdownContext);
    }
    cmdSync() {
        return this.tryGetRuleContext(0, CmdSyncContext);
    }
    cmdRole() {
        return this.tryGetRuleContext(0, CmdRoleContext);
    }
    cmdMonitor() {
        return this.tryGetRuleContext(0, CmdMonitorContext);
    }
    cmdSlaveof() {
        return this.tryGetRuleContext(0, CmdSlaveofContext);
    }
    cmdFlushall() {
        return this.tryGetRuleContext(0, CmdFlushallContext);
    }
    cmdQuit() {
        return this.tryGetRuleContext(0, CmdQuitContext);
    }
    cmdAuth() {
        return this.tryGetRuleContext(0, CmdAuthContext);
    }
    cmdDbsize() {
        return this.tryGetRuleContext(0, CmdDbsizeContext);
    }
    cmdBgrewriteaof() {
        return this.tryGetRuleContext(0, CmdBgrewriteaofContext);
    }
    cmdTime() {
        return this.tryGetRuleContext(0, CmdTimeContext);
    }
    cmdInfo() {
        return this.tryGetRuleContext(0, CmdInfoContext);
    }
    cmdBgsave() {
        return this.tryGetRuleContext(0, CmdBgsaveContext);
    }
    cmdCmd() {
        return this.tryGetRuleContext(0, CmdCmdContext);
    }
    cmdCmdx() {
        return this.tryGetRuleContext(0, CmdCmdxContext);
    }
    cmdDebug() {
        return this.tryGetRuleContext(0, CmdDebugContext);
    }
    cmdFlushdb() {
        return this.tryGetRuleContext(0, CmdFlushdbContext);
    }
    cmdSsubscribe() {
        return this.tryGetRuleContext(0, CmdSsubscribeContext);
    }
    cmdUnsubscribe() {
        return this.tryGetRuleContext(0, CmdUnsubscribeContext);
    }
    cmdPusubnumsub() {
        return this.tryGetRuleContext(0, CmdPusubnumsubContext);
    }
    cmdPusubnumpat() {
        return this.tryGetRuleContext(0, CmdPusubnumpatContext);
    }
    cmdPusubchannels() {
        return this.tryGetRuleContext(0, CmdPusubchannelsContext);
    }
    cmdPublish() {
        return this.tryGetRuleContext(0, CmdPublishContext);
    }
    cmdAsking() {
        return this.tryGetRuleContext(0, CmdAskingContext);
    }
    cmdAddDelSlots() {
        return this.tryGetRuleContext(0, CmdAddDelSlotsContext);
    }
    cmdCountKeysInSlot() {
        return this.tryGetRuleContext(0, CmdCountKeysInSlotContext);
    }
    cmdFailOver() {
        return this.tryGetRuleContext(0, CmdFailOverContext);
    }
    cmdForget() {
        return this.tryGetRuleContext(0, CmdForgetContext);
    }
    cmdGetKeysInSlot() {
        return this.tryGetRuleContext(0, CmdGetKeysInSlotContext);
    }
    cmdClusterOrder() {
        return this.tryGetRuleContext(0, CmdClusterOrderContext);
    }
    cmdKeySlot() {
        return this.tryGetRuleContext(0, CmdKeySlotContext);
    }
    cmdMeet() {
        return this.tryGetRuleContext(0, CmdMeetContext);
    }
    cmdReadonly() {
        return this.tryGetRuleContext(0, CmdReadonlyContext);
    }
    cmdReadwrite() {
        return this.tryGetRuleContext(0, CmdReadwriteContext);
    }
    cmdReplicaesSlave() {
        return this.tryGetRuleContext(0, CmdReplicaesSlaveContext);
    }
    cmdReset() {
        return this.tryGetRuleContext(0, CmdResetContext);
    }
    cmdSetSlot() {
        return this.tryGetRuleContext(0, CmdSetSlotContext);
    }
    constructor(parent, invokingState) {
        super(parent, invokingState);
    }
    // @Override
    get ruleIndex() { return RedisParser.RULE_sqlStatement; }
    // @Override
    enterRule(listener) {
        if (listener.enterSqlStatement) {
            listener.enterSqlStatement(this);
        }
    }
    // @Override
    exitRule(listener) {
        if (listener.exitSqlStatement) {
            listener.exitSqlStatement(this);
        }
    }
    // @Override
    accept(visitor) {
        if (visitor.visitSqlStatement) {
            return visitor.visitSqlStatement(this);
        }
        else {
            return visitor.visitChildren(this);
        }
    }
}
