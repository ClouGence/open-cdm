import { ATN } from "antlr4ts/atn/ATN";
import { FailedPredicateException } from "antlr4ts/FailedPredicateException";
import { Parser } from "antlr4ts/Parser";
import { ParserRuleContext } from "antlr4ts/ParserRuleContext";
import { TerminalNode } from "antlr4ts/tree/TerminalNode";
import { TokenStream } from "antlr4ts/TokenStream";
import { Vocabulary } from "antlr4ts/Vocabulary";
import { RedisSqlParserListener } from "./RedisSqlParserListener";
import { RedisSqlParserVisitor } from "./RedisSqlParserVisitor";
export declare class RedisSqlParser extends Parser {
    static readonly SPACE = 1;
    static readonly SPEC_MYSQL_COMMENT = 2;
    static readonly COMMENT_INPUT = 3;
    static readonly LINE_COMMENT = 4;
    static readonly COPY = 5;
    static readonly DEL = 6;
    static readonly DUMP = 7;
    static readonly EXISTS = 8;
    static readonly EXPIRE = 9;
    static readonly EXPIREAT = 10;
    static readonly EXPIRETIME = 11;
    static readonly KEYS = 12;
    static readonly MOVE = 13;
    static readonly OBJECT = 14;
    static readonly PERSIST = 15;
    static readonly PEXPIRE = 16;
    static readonly PEXPIREAT = 17;
    static readonly PEXPIRETIME = 18;
    static readonly TTL = 19;
    static readonly PTTL = 20;
    static readonly RANDOMKEY = 21;
    static readonly RENAME = 22;
    static readonly RENAMENX = 23;
    static readonly RESTORE = 24;
    static readonly SCAN = 25;
    static readonly SORT = 26;
    static readonly SORT_RO = 27;
    static readonly TOUCH = 28;
    static readonly TYPE = 29;
    static readonly UNLINK = 30;
    static readonly WAIT = 31;
    static readonly APPEND = 32;
    static readonly DECR = 33;
    static readonly DECRBY = 34;
    static readonly GET = 35;
    static readonly GETDEL = 36;
    static readonly GETEX = 37;
    static readonly GETRANGE = 38;
    static readonly GETSET = 39;
    static readonly GETBIT = 40;
    static readonly INCR = 41;
    static readonly INCRBY = 42;
    static readonly INCRBYFLOAT = 43;
    static readonly LCS = 44;
    static readonly MGET = 45;
    static readonly MSET = 46;
    static readonly MSETNX = 47;
    static readonly SETEX = 48;
    static readonly PSETEX = 49;
    static readonly SET = 50;
    static readonly SETNX = 51;
    static readonly SETRANGE = 52;
    static readonly SETBIT = 53;
    static readonly STRLEN = 54;
    static readonly SUBSTR = 55;
    static readonly HDEL = 56;
    static readonly HEXISTS = 57;
    static readonly HGET = 58;
    static readonly HGETALL = 59;
    static readonly HINCRBY = 60;
    static readonly HINCRBYFLOAT = 61;
    static readonly HKEYS = 62;
    static readonly HLEN = 63;
    static readonly HMGET = 64;
    static readonly HMSET = 65;
    static readonly HRANDFIELD = 66;
    static readonly HSCAN = 67;
    static readonly HSET = 68;
    static readonly HSETNX = 69;
    static readonly HSTRLEN = 70;
    static readonly HVALS = 71;
    static readonly BLMOVE = 72;
    static readonly BLMPOP = 73;
    static readonly BLPOP = 74;
    static readonly BRPOP = 75;
    static readonly BRPOPLPUSH = 76;
    static readonly LINDEX = 77;
    static readonly LINSERT = 78;
    static readonly LLEN = 79;
    static readonly LMOVE = 80;
    static readonly LMPOP = 81;
    static readonly LPOP = 82;
    static readonly LPOS = 83;
    static readonly LPUSH = 84;
    static readonly LPUSHX = 85;
    static readonly LRANGE = 86;
    static readonly LREM = 87;
    static readonly LSET = 88;
    static readonly LTRIM = 89;
    static readonly RPOP = 90;
    static readonly RPOPLPUSH = 91;
    static readonly RPUSH = 92;
    static readonly RPUSHX = 93;
    static readonly SADD = 94;
    static readonly SCARD = 95;
    static readonly SDIFF = 96;
    static readonly SDIFFSTORE = 97;
    static readonly SINTER = 98;
    static readonly SINTERCARD = 99;
    static readonly SINTERSTORE = 100;
    static readonly SISMEMBER = 101;
    static readonly SMEMBERS = 102;
    static readonly SMISMEMBER = 103;
    static readonly SMOVE = 104;
    static readonly SPOP = 105;
    static readonly SRANDMEMBER = 106;
    static readonly SREM = 107;
    static readonly SSCAN = 108;
    static readonly SUNION = 109;
    static readonly SUNIONSTORE = 110;
    static readonly BZMPOP = 111;
    static readonly BZPOPMAX = 112;
    static readonly BZPOPMIN = 113;
    static readonly ZADD = 114;
    static readonly ZCARD = 115;
    static readonly ZCOUNT = 116;
    static readonly ZDIFF = 117;
    static readonly ZDIFFSTORE = 118;
    static readonly ZINCRBY = 119;
    static readonly ZINTER = 120;
    static readonly ZINTERCARD = 121;
    static readonly ZINTERSTORE = 122;
    static readonly ZLEXCOUNT = 123;
    static readonly ZMPOP = 124;
    static readonly ZMSCORE = 125;
    static readonly ZPOPMAX = 126;
    static readonly ZPOPMIN = 127;
    static readonly ZRANDMEMBER = 128;
    static readonly ZRANGE = 129;
    static readonly ZRANGEBYLEX = 130;
    static readonly ZRANGEBYSCORE = 131;
    static readonly ZRANGESTORE = 132;
    static readonly ZRANK = 133;
    static readonly ZREM = 134;
    static readonly ZREMRANGEBYLEX = 135;
    static readonly ZREMRANGEBYRANK = 136;
    static readonly ZREMRANGEBYSCORE = 137;
    static readonly ZREVRANGE = 138;
    static readonly ZREVRANGEBYLEX = 139;
    static readonly ZREVRANGEBYSCORE = 140;
    static readonly ZREVRANK = 141;
    static readonly ZSCAN = 142;
    static readonly ZSCORE = 143;
    static readonly ZUNION = 144;
    static readonly ZUNIONSTORE = 145;
    static readonly PSUBSCRIBE = 146;
    static readonly PUBSUB = 147;
    static readonly PUBLISH = 148;
    static readonly CHANNELS = 149;
    static readonly NUMSUB = 150;
    static readonly NUMPAT = 151;
    static readonly SHARDCHANNELS = 152;
    static readonly SHARDNUMSUB = 153;
    static readonly PUNSUBSCRIBE = 154;
    static readonly SPUBLISH = 155;
    static readonly SUBSCRIBE = 156;
    static readonly UNSUBSCRIBE = 157;
    static readonly SSUBSCRIBE = 158;
    static readonly SUNSUBSCRIBE = 159;
    static readonly SERVER = 160;
    static readonly CLIENTS = 161;
    static readonly MEMORY = 162;
    static readonly PERSISTENCE = 163;
    static readonly STATS = 164;
    static readonly REPLICATION = 165;
    static readonly CPU = 166;
    static readonly COMMANDSTATS = 167;
    static readonly LATENCYSTATS = 168;
    static readonly SENTINEL = 169;
    static readonly MODULES = 170;
    static readonly KEYSPACE = 171;
    static readonly ERRORSTATS = 172;
    static readonly ALL = 173;
    static readonly DEFAULT = 174;
    static readonly EVERYTHING = 175;
    static readonly ASKING = 176;
    static readonly CLUSTER = 177;
    static readonly ADDSLOTS = 178;
    static readonly DELSLOTS = 179;
    static readonly ADDSLOTSRANGE = 180;
    static readonly BUMPEPOCH = 181;
    static readonly COUNTKEYSINSLOT = 182;
    static readonly DELSLOTSRANGE = 183;
    static readonly FAILOVER = 184;
    static readonly FLUSHSLOTS = 185;
    static readonly FORGET = 186;
    static readonly GETKEYSINSLOT = 187;
    static readonly KEYSLOT = 188;
    static readonly LINKS = 189;
    static readonly MEET = 190;
    static readonly MYID = 191;
    static readonly MYSHARDID = 192;
    static readonly FORCE = 193;
    static readonly TAKEOVER = 194;
    static readonly NODE = 195;
    static readonly NODES = 196;
    static readonly REPLICAS = 197;
    static readonly REPLICATE = 198;
    static readonly RESET = 199;
    static readonly HARD = 200;
    static readonly SOFT = 201;
    static readonly SAVECONFIG = 202;
    static readonly SETSLOT = 203;
    static readonly IMPORTING = 204;
    static readonly MIGRATING = 205;
    static readonly STABLE = 206;
    static readonly SHARDS = 207;
    static readonly SLAVES = 208;
    static readonly READONLY = 209;
    static readonly READWRITE = 210;
    static readonly FLUSHDB = 211;
    static readonly SCRIPT = 212;
    static readonly EVAL = 213;
    static readonly EVALSHA = 214;
    static readonly EXEC = 215;
    static readonly WATCH = 216;
    static readonly DISCARD = 217;
    static readonly UNWATCH = 218;
    static readonly MULTI = 219;
    static readonly PFMERGE = 220;
    static readonly PFADD = 221;
    static readonly PFCOUNT = 222;
    static readonly ECHO = 223;
    static readonly PING = 224;
    static readonly SAVE = 225;
    static readonly SLOWLOG = 226;
    static readonly LASTSAVE = 227;
    static readonly CONFIG = 228;
    static readonly CLIENT = 229;
    static readonly SHUTDOWN = 230;
    static readonly SYNC = 231;
    static readonly ROLE = 232;
    static readonly MONITOR = 233;
    static readonly SLAVEOF = 234;
    static readonly FLUSHALL = 235;
    static readonly SELECT = 236;
    static readonly QUIT = 237;
    static readonly AUTH = 238;
    static readonly DBSIZE = 239;
    static readonly BGREWRITEAOF = 240;
    static readonly TIME = 241;
    static readonly INFO = 242;
    static readonly BGSAVE = 243;
    static readonly COMMAND = 244;
    static readonly DEBUG = 245;
    static readonly DB = 246;
    static readonly NX = 247;
    static readonly XX = 248;
    static readonly GT = 249;
    static readonly LT = 250;
    static readonly ENCODING = 251;
    static readonly FREQ = 252;
    static readonly IDLETIME = 253;
    static readonly REFCOUNT = 254;
    static readonly ABSTTL = 255;
    static readonly BY = 256;
    static readonly ASC = 257;
    static readonly DESC = 258;
    static readonly ALPHA = 259;
    static readonly STORE = 260;
    static readonly REPLACE = 261;
    static readonly EX = 262;
    static readonly PX = 263;
    static readonly EXAT = 264;
    static readonly PXAT = 265;
    static readonly LEN = 266;
    static readonly IDX = 267;
    static readonly MINMATCHLEN = 268;
    static readonly WITHMATCHLEN = 269;
    static readonly KEEPTTL = 270;
    static readonly WITHSCORES = 271;
    static readonly LIMIT = 272;
    static readonly BEFORE = 273;
    static readonly AFTER = 274;
    static readonly FLUSH = 275;
    static readonly RESETSTAT = 276;
    static readonly REWRITE = 277;
    static readonly PAUSE = 278;
    static readonly SETNAME = 279;
    static readonly GETNAME = 280;
    static readonly LIST = 281;
    static readonly NOSAVE = 282;
    static readonly SLOTS = 283;
    static readonly GETKEYS = 284;
    static readonly COUNT = 285;
    static readonly SEGFAULT = 286;
    static readonly KILL = 287;
    static readonly LOAD = 288;
    static readonly WITHVALUES = 289;
    static readonly MATCH = 290;
    static readonly MIN = 291;
    static readonly MAX = 292;
    static readonly CH = 293;
    static readonly WEIGHTS = 294;
    static readonly AGGREGATE = 295;
    static readonly SUM = 296;
    static readonly BYSCORE = 297;
    static readonly BYLEX = 298;
    static readonly REV = 299;
    static readonly LEFT = 300;
    static readonly RIGHT = 301;
    static readonly RANK = 302;
    static readonly MAXLEN = 303;
    static readonly ASYNC = 304;
    static readonly HEX_NUM = 305;
    static readonly OCT_NUM = 306;
    static readonly BIT_NUM = 307;
    static readonly INTEGER_NUM = 308;
    static readonly DECIMAL_NUM = 309;
    static readonly STRING = 310;
    static readonly HOST = 311;
    static readonly HOST_PORT = 312;
    static readonly NAME_TOKEN = 313;
    static readonly SEMI = 314;
    static readonly RULE_program = 0;
    static readonly RULE_sqlStatements = 1;
    static readonly RULE_emptyStatement_ = 2;
    static readonly RULE_keyName = 3;
    static readonly RULE_fieldName = 4;
    static readonly RULE_stringValue = 5;
    static readonly RULE_intValue = 6;
    static readonly RULE_floatValue = 7;
    static readonly RULE_pattern = 8;
    static readonly RULE_cursor = 9;
    static readonly RULE_min = 10;
    static readonly RULE_max = 11;
    static readonly RULE_multiString = 12;
    static readonly RULE_cout = 13;
    static readonly RULE_start = 14;
    static readonly RULE_end = 15;
    static readonly RULE_port = 16;
    static readonly RULE_keyAndString = 17;
    static readonly RULE_intAndString = 18;
    static readonly RULE_infoOpt = 19;
    static readonly RULE_patternOpt = 20;
    static readonly RULE_keyOpt = 21;
    static readonly RULE_idletimeOpt = 22;
    static readonly RULE_freqOpt = 23;
    static readonly RULE_limitOpt = 24;
    static readonly RULE_sortOpt = 25;
    static readonly RULE_getsetOpt = 26;
    static readonly RULE_lrOpt = 27;
    static readonly RULE_rankOpt = 28;
    static readonly RULE_countOpt = 29;
    static readonly RULE_maxlenOpt = 30;
    static readonly RULE_cmdCopy = 31;
    static readonly RULE_cmdDel = 32;
    static readonly RULE_cmdDump = 33;
    static readonly RULE_cmdExists = 34;
    static readonly RULE_cmdExpire = 35;
    static readonly RULE_cmdExpireat = 36;
    static readonly RULE_cmdExpireTime = 37;
    static readonly RULE_cmdKeys = 38;
    static readonly RULE_cmdMove = 39;
    static readonly RULE_cmdObject = 40;
    static readonly RULE_cmdPersist = 41;
    static readonly RULE_cmdPexpire = 42;
    static readonly RULE_cmdPexpireat = 43;
    static readonly RULE_cmdPExpireTime = 44;
    static readonly RULE_cmdTtl = 45;
    static readonly RULE_cmdPttl = 46;
    static readonly RULE_cmdRandomkey = 47;
    static readonly RULE_cmdRename = 48;
    static readonly RULE_cmdRenamenx = 49;
    static readonly RULE_cmdRestore = 50;
    static readonly RULE_cmdScan = 51;
    static readonly RULE_cmdSort = 52;
    static readonly RULE_cmdSortro = 53;
    static readonly RULE_cmdTouch = 54;
    static readonly RULE_cmdType = 55;
    static readonly RULE_cmdUnlink = 56;
    static readonly RULE_cmdWait = 57;
    static readonly RULE_cmdAppend = 58;
    static readonly RULE_cmdDecr = 59;
    static readonly RULE_cmdDecrby = 60;
    static readonly RULE_cmdGet = 61;
    static readonly RULE_cmdGetdel = 62;
    static readonly RULE_cmdGetex = 63;
    static readonly RULE_cmdGetrange = 64;
    static readonly RULE_cmdGetset = 65;
    static readonly RULE_cmdGetbit = 66;
    static readonly RULE_cmdInc = 67;
    static readonly RULE_cmdLcs = 68;
    static readonly RULE_cmdMget = 69;
    static readonly RULE_cmdMset = 70;
    static readonly RULE_cmdMsetnx = 71;
    static readonly RULE_cmdSetex = 72;
    static readonly RULE_cmdSet = 73;
    static readonly RULE_cmdSetnx = 74;
    static readonly RULE_cmdSetrange = 75;
    static readonly RULE_cmdSetbit = 76;
    static readonly RULE_cmdStrlen = 77;
    static readonly RULE_cmdSubstr = 78;
    static readonly RULE_cmdHdelMget = 79;
    static readonly RULE_cmdHexistsGetKeysStrlen = 80;
    static readonly RULE_cmdHgetallLenVals = 81;
    static readonly RULE_cmdHmsetHset = 82;
    static readonly RULE_cmdHincrby = 83;
    static readonly RULE_cmdHrandfield = 84;
    static readonly RULE_cmdHscan = 85;
    static readonly RULE_cmdHsetnx = 86;
    static readonly RULE_cmdBlmove = 87;
    static readonly RULE_cmdLmpop = 88;
    static readonly RULE_cmdBpop = 89;
    static readonly RULE_cmdBrpoplpush = 90;
    static readonly RULE_cmdLindex = 91;
    static readonly RULE_cmdLinsert = 92;
    static readonly RULE_cmdLlen = 93;
    static readonly RULE_cmdLmove = 94;
    static readonly RULE_cmdPop = 95;
    static readonly RULE_cmdLpos = 96;
    static readonly RULE_cmdPush = 97;
    static readonly RULE_cmdLrangeTrim = 98;
    static readonly RULE_cmdLremSet = 99;
    static readonly RULE_cmdRpoplpush = 100;
    static readonly RULE_cmdSadd = 101;
    static readonly RULE_cmdScard = 102;
    static readonly RULE_cmdSdiff = 103;
    static readonly RULE_cmdSdiffstore = 104;
    static readonly RULE_cmdSintercard = 105;
    static readonly RULE_cmdSismember = 106;
    static readonly RULE_cmdSmove = 107;
    static readonly RULE_cmdSpop = 108;
    static readonly RULE_cmdSscan = 109;
    static readonly RULE_cmdBzmpop = 110;
    static readonly RULE_cmdBzpopmax = 111;
    static readonly RULE_cmdZadd = 112;
    static readonly RULE_cmdZcard = 113;
    static readonly RULE_cmdZcount = 114;
    static readonly RULE_cmdZdiff = 115;
    static readonly RULE_cmdZdiffstore = 116;
    static readonly RULE_cmdZincrby = 117;
    static readonly RULE_cmdZinter = 118;
    static readonly RULE_cmdZintercard = 119;
    static readonly RULE_cmdZinterstore = 120;
    static readonly RULE_cmdZmpop = 121;
    static readonly RULE_cmdZmscore = 122;
    static readonly RULE_cmdZpopmax = 123;
    static readonly RULE_cmdZrandmember = 124;
    static readonly RULE_cmdZrange = 125;
    static readonly RULE_cmdZrangebylex = 126;
    static readonly RULE_cmdZrangebyscore = 127;
    static readonly RULE_cmdZrangestore = 128;
    static readonly RULE_cmdZrank = 129;
    static readonly RULE_cmdZremrangebyrank = 130;
    static readonly RULE_cmdZrevrange = 131;
    static readonly RULE_cmdZrevrangebylex = 132;
    static readonly RULE_cmdZscan = 133;
    static readonly RULE_cmdZunion = 134;
    static readonly RULE_cmdZunionstore = 135;
    static readonly RULE_cmdScriptKill = 136;
    static readonly RULE_cmdScriptLoad = 137;
    static readonly RULE_cmdEval = 138;
    static readonly RULE_cmdEvalsha = 139;
    static readonly RULE_cmdScriptExists = 140;
    static readonly RULE_cmdScriptFlush = 141;
    static readonly RULE_cmdExec = 142;
    static readonly RULE_cmdWatch = 143;
    static readonly RULE_cmdDiscard = 144;
    static readonly RULE_cmdUnwatch = 145;
    static readonly RULE_cmdMulti = 146;
    static readonly RULE_cmdPfmerge = 147;
    static readonly RULE_cmdPfadd = 148;
    static readonly RULE_cmdPfcount = 149;
    static readonly RULE_cmdSsubscribe = 150;
    static readonly RULE_cmdUnsubscribe = 151;
    static readonly RULE_cmdPusubnumpat = 152;
    static readonly RULE_cmdPusubnumsub = 153;
    static readonly RULE_cmdPusubchannels = 154;
    static readonly RULE_cmdPublish = 155;
    static readonly RULE_cmdAsking = 156;
    static readonly RULE_cmdReadonly = 157;
    static readonly RULE_cmdReadwrite = 158;
    static readonly RULE_cmdAddDelSlots = 159;
    static readonly RULE_cmdCountKeysInSlot = 160;
    static readonly RULE_cmdFailOver = 161;
    static readonly RULE_cmdForget = 162;
    static readonly RULE_cmdGetKeysInSlot = 163;
    static readonly RULE_cmdClusterOrder = 164;
    static readonly RULE_cmdKeySlot = 165;
    static readonly RULE_cmdMeet = 166;
    static readonly RULE_cmdReplicaesSlave = 167;
    static readonly RULE_cmdReset = 168;
    static readonly RULE_cmdSetSlot = 169;
    static readonly RULE_cmdFlushdb = 170;
    static readonly RULE_cmdEcho = 171;
    static readonly RULE_cmdSave = 172;
    static readonly RULE_cmdSlowlog = 173;
    static readonly RULE_cmdLastsave = 174;
    static readonly RULE_cmdConfig = 175;
    static readonly RULE_cmdClient = 176;
    static readonly RULE_cmdShutdown = 177;
    static readonly RULE_cmdSync = 178;
    static readonly RULE_cmdRole = 179;
    static readonly RULE_cmdMonitor = 180;
    static readonly RULE_cmdSlaveof = 181;
    static readonly RULE_cmdFlushall = 182;
    static readonly RULE_cmdSelect = 183;
    static readonly RULE_cmdPing = 184;
    static readonly RULE_cmdQuit = 185;
    static readonly RULE_cmdAuth = 186;
    static readonly RULE_cmdDbsize = 187;
    static readonly RULE_cmdBgrewriteaof = 188;
    static readonly RULE_cmdTime = 189;
    static readonly RULE_cmdInfo = 190;
    static readonly RULE_cmdBgsave = 191;
    static readonly RULE_cmdCmd = 192;
    static readonly RULE_cmdCmdx = 193;
    static readonly RULE_cmdDebug = 194;
    static readonly RULE_sqlStatement = 195;
    static readonly ruleNames: string[];
    private static readonly _LITERAL_NAMES;
    private static readonly _SYMBOLIC_NAMES;
    static readonly VOCABULARY: Vocabulary;
    get vocabulary(): Vocabulary;
    get grammarFileName(): string;
    get ruleNames(): string[];
    get serializedATN(): string;
    protected createFailedPredicateException(predicate?: string, message?: string): FailedPredicateException;
    constructor(input: TokenStream);
    program(): ProgramContext;
    sqlStatements(): SqlStatementsContext;
    emptyStatement_(): EmptyStatement_Context;
    keyName(): KeyNameContext;
    fieldName(): FieldNameContext;
    stringValue(): StringValueContext;
    intValue(): IntValueContext;
    floatValue(): FloatValueContext;
    pattern(): PatternContext;
    cursor(): CursorContext;
    min(): MinContext;
    max(): MaxContext;
    multiString(): MultiStringContext;
    cout(): CoutContext;
    start(): StartContext;
    end(): EndContext;
    port(): PortContext;
    keyAndString(): KeyAndStringContext;
    intAndString(): IntAndStringContext;
    infoOpt(): InfoOptContext;
    patternOpt(): PatternOptContext;
    keyOpt(): KeyOptContext;
    idletimeOpt(): IdletimeOptContext;
    freqOpt(): FreqOptContext;
    limitOpt(): LimitOptContext;
    sortOpt(): SortOptContext;
    getsetOpt(): GetsetOptContext;
    lrOpt(): LrOptContext;
    rankOpt(): RankOptContext;
    countOpt(): CountOptContext;
    maxlenOpt(): MaxlenOptContext;
    cmdCopy(): CmdCopyContext;
    cmdDel(): CmdDelContext;
    cmdDump(): CmdDumpContext;
    cmdExists(): CmdExistsContext;
    cmdExpire(): CmdExpireContext;
    cmdExpireat(): CmdExpireatContext;
    cmdExpireTime(): CmdExpireTimeContext;
    cmdKeys(): CmdKeysContext;
    cmdMove(): CmdMoveContext;
    cmdObject(): CmdObjectContext;
    cmdPersist(): CmdPersistContext;
    cmdPexpire(): CmdPexpireContext;
    cmdPexpireat(): CmdPexpireatContext;
    cmdPExpireTime(): CmdPExpireTimeContext;
    cmdTtl(): CmdTtlContext;
    cmdPttl(): CmdPttlContext;
    cmdRandomkey(): CmdRandomkeyContext;
    cmdRename(): CmdRenameContext;
    cmdRenamenx(): CmdRenamenxContext;
    cmdRestore(): CmdRestoreContext;
    cmdScan(): CmdScanContext;
    cmdSort(): CmdSortContext;
    cmdSortro(): CmdSortroContext;
    cmdTouch(): CmdTouchContext;
    cmdType(): CmdTypeContext;
    cmdUnlink(): CmdUnlinkContext;
    cmdWait(): CmdWaitContext;
    cmdAppend(): CmdAppendContext;
    cmdDecr(): CmdDecrContext;
    cmdDecrby(): CmdDecrbyContext;
    cmdGet(): CmdGetContext;
    cmdGetdel(): CmdGetdelContext;
    cmdGetex(): CmdGetexContext;
    cmdGetrange(): CmdGetrangeContext;
    cmdGetset(): CmdGetsetContext;
    cmdGetbit(): CmdGetbitContext;
    cmdInc(): CmdIncContext;
    cmdLcs(): CmdLcsContext;
    cmdMget(): CmdMgetContext;
    cmdMset(): CmdMsetContext;
    cmdMsetnx(): CmdMsetnxContext;
    cmdSetex(): CmdSetexContext;
    cmdSet(): CmdSetContext;
    cmdSetnx(): CmdSetnxContext;
    cmdSetrange(): CmdSetrangeContext;
    cmdSetbit(): CmdSetbitContext;
    cmdStrlen(): CmdStrlenContext;
    cmdSubstr(): CmdSubstrContext;
    cmdHdelMget(): CmdHdelMgetContext;
    cmdHexistsGetKeysStrlen(): CmdHexistsGetKeysStrlenContext;
    cmdHgetallLenVals(): CmdHgetallLenValsContext;
    cmdHmsetHset(): CmdHmsetHsetContext;
    cmdHincrby(): CmdHincrbyContext;
    cmdHrandfield(): CmdHrandfieldContext;
    cmdHscan(): CmdHscanContext;
    cmdHsetnx(): CmdHsetnxContext;
    cmdBlmove(): CmdBlmoveContext;
    cmdLmpop(): CmdLmpopContext;
    cmdBpop(): CmdBpopContext;
    cmdBrpoplpush(): CmdBrpoplpushContext;
    cmdLindex(): CmdLindexContext;
    cmdLinsert(): CmdLinsertContext;
    cmdLlen(): CmdLlenContext;
    cmdLmove(): CmdLmoveContext;
    cmdPop(): CmdPopContext;
    cmdLpos(): CmdLposContext;
    cmdPush(): CmdPushContext;
    cmdLrangeTrim(): CmdLrangeTrimContext;
    cmdLremSet(): CmdLremSetContext;
    cmdRpoplpush(): CmdRpoplpushContext;
    cmdSadd(): CmdSaddContext;
    cmdScard(): CmdScardContext;
    cmdSdiff(): CmdSdiffContext;
    cmdSdiffstore(): CmdSdiffstoreContext;
    cmdSintercard(): CmdSintercardContext;
    cmdSismember(): CmdSismemberContext;
    cmdSmove(): CmdSmoveContext;
    cmdSpop(): CmdSpopContext;
    cmdSscan(): CmdSscanContext;
    cmdBzmpop(): CmdBzmpopContext;
    cmdBzpopmax(): CmdBzpopmaxContext;
    cmdZadd(): CmdZaddContext;
    cmdZcard(): CmdZcardContext;
    cmdZcount(): CmdZcountContext;
    cmdZdiff(): CmdZdiffContext;
    cmdZdiffstore(): CmdZdiffstoreContext;
    cmdZincrby(): CmdZincrbyContext;
    cmdZinter(): CmdZinterContext;
    cmdZintercard(): CmdZintercardContext;
    cmdZinterstore(): CmdZinterstoreContext;
    cmdZmpop(): CmdZmpopContext;
    cmdZmscore(): CmdZmscoreContext;
    cmdZpopmax(): CmdZpopmaxContext;
    cmdZrandmember(): CmdZrandmemberContext;
    cmdZrange(): CmdZrangeContext;
    cmdZrangebylex(): CmdZrangebylexContext;
    cmdZrangebyscore(): CmdZrangebyscoreContext;
    cmdZrangestore(): CmdZrangestoreContext;
    cmdZrank(): CmdZrankContext;
    cmdZremrangebyrank(): CmdZremrangebyrankContext;
    cmdZrevrange(): CmdZrevrangeContext;
    cmdZrevrangebylex(): CmdZrevrangebylexContext;
    cmdZscan(): CmdZscanContext;
    cmdZunion(): CmdZunionContext;
    cmdZunionstore(): CmdZunionstoreContext;
    cmdScriptKill(): CmdScriptKillContext;
    cmdScriptLoad(): CmdScriptLoadContext;
    cmdEval(): CmdEvalContext;
    cmdEvalsha(): CmdEvalshaContext;
    cmdScriptExists(): CmdScriptExistsContext;
    cmdScriptFlush(): CmdScriptFlushContext;
    cmdExec(): CmdExecContext;
    cmdWatch(): CmdWatchContext;
    cmdDiscard(): CmdDiscardContext;
    cmdUnwatch(): CmdUnwatchContext;
    cmdMulti(): CmdMultiContext;
    cmdPfmerge(): CmdPfmergeContext;
    cmdPfadd(): CmdPfaddContext;
    cmdPfcount(): CmdPfcountContext;
    cmdSsubscribe(): CmdSsubscribeContext;
    cmdUnsubscribe(): CmdUnsubscribeContext;
    cmdPusubnumpat(): CmdPusubnumpatContext;
    cmdPusubnumsub(): CmdPusubnumsubContext;
    cmdPusubchannels(): CmdPusubchannelsContext;
    cmdPublish(): CmdPublishContext;
    cmdAsking(): CmdAskingContext;
    cmdReadonly(): CmdReadonlyContext;
    cmdReadwrite(): CmdReadwriteContext;
    cmdAddDelSlots(): CmdAddDelSlotsContext;
    cmdCountKeysInSlot(): CmdCountKeysInSlotContext;
    cmdFailOver(): CmdFailOverContext;
    cmdForget(): CmdForgetContext;
    cmdGetKeysInSlot(): CmdGetKeysInSlotContext;
    cmdClusterOrder(): CmdClusterOrderContext;
    cmdKeySlot(): CmdKeySlotContext;
    cmdMeet(): CmdMeetContext;
    cmdReplicaesSlave(): CmdReplicaesSlaveContext;
    cmdReset(): CmdResetContext;
    cmdSetSlot(): CmdSetSlotContext;
    cmdFlushdb(): CmdFlushdbContext;
    cmdEcho(): CmdEchoContext;
    cmdSave(): CmdSaveContext;
    cmdSlowlog(): CmdSlowlogContext;
    cmdLastsave(): CmdLastsaveContext;
    cmdConfig(): CmdConfigContext;
    cmdClient(): CmdClientContext;
    cmdShutdown(): CmdShutdownContext;
    cmdSync(): CmdSyncContext;
    cmdRole(): CmdRoleContext;
    cmdMonitor(): CmdMonitorContext;
    cmdSlaveof(): CmdSlaveofContext;
    cmdFlushall(): CmdFlushallContext;
    cmdSelect(): CmdSelectContext;
    cmdPing(): CmdPingContext;
    cmdQuit(): CmdQuitContext;
    cmdAuth(): CmdAuthContext;
    cmdDbsize(): CmdDbsizeContext;
    cmdBgrewriteaof(): CmdBgrewriteaofContext;
    cmdTime(): CmdTimeContext;
    cmdInfo(): CmdInfoContext;
    cmdBgsave(): CmdBgsaveContext;
    cmdCmd(): CmdCmdContext;
    cmdCmdx(): CmdCmdxContext;
    cmdDebug(): CmdDebugContext;
    sqlStatement(): SqlStatementContext;
    private static readonly _serializedATNSegments;
    private static readonly _serializedATNSegment0;
    private static readonly _serializedATNSegment1;
    private static readonly _serializedATNSegment2;
    private static readonly _serializedATNSegment3;
    static readonly _serializedATN: string;
    static __ATN: ATN;
    static get _ATN(): ATN;
}
export declare class ProgramContext extends ParserRuleContext {
    EOF(): TerminalNode;
    sqlStatements(): SqlStatementsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class SqlStatementsContext extends ParserRuleContext {
    sqlStatement(): SqlStatementContext[];
    sqlStatement(i: number): SqlStatementContext;
    emptyStatement_(): EmptyStatement_Context[];
    emptyStatement_(i: number): EmptyStatement_Context;
    SEMI(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class EmptyStatement_Context extends ParserRuleContext {
    SEMI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class KeyNameContext extends ParserRuleContext {
    NAME_TOKEN(): TerminalNode | undefined;
    STRING(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class FieldNameContext extends ParserRuleContext {
    NAME_TOKEN(): TerminalNode | undefined;
    STRING(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class StringValueContext extends ParserRuleContext {
    STRING(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class IntValueContext extends ParserRuleContext {
    INTEGER_NUM(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class FloatValueContext extends ParserRuleContext {
    DECIMAL_NUM(): TerminalNode | undefined;
    INTEGER_NUM(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class PatternContext extends ParserRuleContext {
    NAME_TOKEN(): TerminalNode | undefined;
    STRING(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CursorContext extends ParserRuleContext {
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class MinContext extends ParserRuleContext {
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class MaxContext extends ParserRuleContext {
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class MultiStringContext extends ParserRuleContext {
    STRING(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CoutContext extends ParserRuleContext {
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class StartContext extends ParserRuleContext {
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class EndContext extends ParserRuleContext {
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class PortContext extends ParserRuleContext {
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class KeyAndStringContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    stringValue(): StringValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class IntAndStringContext extends ParserRuleContext {
    intValue(): IntValueContext;
    stringValue(): StringValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class InfoOptContext extends ParserRuleContext {
    SERVER(): TerminalNode | undefined;
    CLIENTS(): TerminalNode | undefined;
    MEMORY(): TerminalNode | undefined;
    PERSISTENCE(): TerminalNode | undefined;
    STATS(): TerminalNode | undefined;
    REPLICATION(): TerminalNode | undefined;
    CPU(): TerminalNode | undefined;
    COMMANDSTATS(): TerminalNode | undefined;
    LATENCYSTATS(): TerminalNode | undefined;
    SENTINEL(): TerminalNode | undefined;
    MODULES(): TerminalNode | undefined;
    KEYSPACE(): TerminalNode | undefined;
    ERRORSTATS(): TerminalNode | undefined;
    ALL(): TerminalNode | undefined;
    DEFAULT(): TerminalNode | undefined;
    EVERYTHING(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class PatternOptContext extends ParserRuleContext {
    GET(): TerminalNode;
    pattern(): PatternContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class KeyOptContext extends ParserRuleContext {
    NX(): TerminalNode | undefined;
    XX(): TerminalNode | undefined;
    GT(): TerminalNode | undefined;
    LT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class IdletimeOptContext extends ParserRuleContext {
    IDLETIME(): TerminalNode;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class FreqOptContext extends ParserRuleContext {
    FREQ(): TerminalNode;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class LimitOptContext extends ParserRuleContext {
    LIMIT(): TerminalNode;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class SortOptContext extends ParserRuleContext {
    ASC(): TerminalNode | undefined;
    DESC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class GetsetOptContext extends ParserRuleContext {
    intValue(): IntValueContext;
    EX(): TerminalNode | undefined;
    PX(): TerminalNode | undefined;
    EXAT(): TerminalNode | undefined;
    PXAT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class LrOptContext extends ParserRuleContext {
    LEFT(): TerminalNode | undefined;
    RIGHT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class RankOptContext extends ParserRuleContext {
    RANK(): TerminalNode;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CountOptContext extends ParserRuleContext {
    COUNT(): TerminalNode;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class MaxlenOptContext extends ParserRuleContext {
    MAXLEN(): TerminalNode;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdCopyContext extends ParserRuleContext {
    COPY(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    DB(): TerminalNode | undefined;
    intValue(): IntValueContext | undefined;
    REPLACE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdDelContext extends ParserRuleContext {
    DEL(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdDumpContext extends ParserRuleContext {
    DUMP(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdExistsContext extends ParserRuleContext {
    EXISTS(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdExpireContext extends ParserRuleContext {
    EXPIRE(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    keyOpt(): KeyOptContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdExpireatContext extends ParserRuleContext {
    EXPIREAT(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    keyOpt(): KeyOptContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdExpireTimeContext extends ParserRuleContext {
    EXPIRETIME(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdKeysContext extends ParserRuleContext {
    KEYS(): TerminalNode;
    pattern(): PatternContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdMoveContext extends ParserRuleContext {
    MOVE(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdObjectContext extends ParserRuleContext {
    OBJECT(): TerminalNode;
    keyName(): KeyNameContext;
    ENCODING(): TerminalNode | undefined;
    FREQ(): TerminalNode | undefined;
    IDLETIME(): TerminalNode | undefined;
    REFCOUNT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPersistContext extends ParserRuleContext {
    PERSIST(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPexpireContext extends ParserRuleContext {
    PEXPIRE(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    keyOpt(): KeyOptContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPexpireatContext extends ParserRuleContext {
    PEXPIREAT(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    keyOpt(): KeyOptContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPExpireTimeContext extends ParserRuleContext {
    PEXPIRETIME(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdTtlContext extends ParserRuleContext {
    TTL(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPttlContext extends ParserRuleContext {
    PTTL(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdRandomkeyContext extends ParserRuleContext {
    RANDOMKEY(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdRenameContext extends ParserRuleContext {
    RENAME(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdRenamenxContext extends ParserRuleContext {
    RENAMENX(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdRestoreContext extends ParserRuleContext {
    RESTORE(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    stringValue(): StringValueContext;
    REPLACE(): TerminalNode | undefined;
    ABSTTL(): TerminalNode | undefined;
    idletimeOpt(): IdletimeOptContext | undefined;
    freqOpt(): FreqOptContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdScanContext extends ParserRuleContext {
    SCAN(): TerminalNode;
    cursor(): CursorContext;
    pattern(): PatternContext;
    intValue(): IntValueContext | undefined;
    TYPE(): TerminalNode | undefined;
    stringValue(): StringValueContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSortContext extends ParserRuleContext {
    SORT(): TerminalNode;
    keyName(): KeyNameContext;
    BY(): TerminalNode | undefined;
    pattern(): PatternContext | undefined;
    limitOpt(): LimitOptContext | undefined;
    sortOpt(): SortOptContext | undefined;
    ALPHA(): TerminalNode | undefined;
    STORE(): TerminalNode | undefined;
    stringValue(): StringValueContext | undefined;
    patternOpt(): PatternOptContext[];
    patternOpt(i: number): PatternOptContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSortroContext extends ParserRuleContext {
    SORT_RO(): TerminalNode;
    keyName(): KeyNameContext;
    BY(): TerminalNode | undefined;
    pattern(): PatternContext | undefined;
    limitOpt(): LimitOptContext | undefined;
    sortOpt(): SortOptContext | undefined;
    ALPHA(): TerminalNode | undefined;
    patternOpt(): PatternOptContext[];
    patternOpt(i: number): PatternOptContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdTouchContext extends ParserRuleContext {
    TOUCH(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdTypeContext extends ParserRuleContext {
    TYPE(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdUnlinkContext extends ParserRuleContext {
    UNLINK(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdWaitContext extends ParserRuleContext {
    WAIT(): TerminalNode;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdAppendContext extends ParserRuleContext {
    APPEND(): TerminalNode;
    keyName(): KeyNameContext;
    stringValue(): StringValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdDecrContext extends ParserRuleContext {
    DECR(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdDecrbyContext extends ParserRuleContext {
    DECRBY(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdGetContext extends ParserRuleContext {
    GET(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdGetdelContext extends ParserRuleContext {
    GETDEL(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdGetexContext extends ParserRuleContext {
    GETEX(): TerminalNode;
    keyName(): KeyNameContext;
    getsetOpt(): GetsetOptContext | undefined;
    PERSIST(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdGetrangeContext extends ParserRuleContext {
    GETRANGE(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdGetsetContext extends ParserRuleContext {
    GETSET(): TerminalNode;
    keyName(): KeyNameContext;
    stringValue(): StringValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdGetbitContext extends ParserRuleContext {
    GETBIT(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdIncContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: CmdIncContext): void;
}
export declare class CmdIncrContext extends CmdIncContext {
    INCR(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(ctx: CmdIncContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdIncrbyContext extends CmdIncContext {
    INCRBY(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    constructor(ctx: CmdIncContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdIncrbyFloatContext extends CmdIncContext {
    INCRBYFLOAT(): TerminalNode;
    keyName(): KeyNameContext;
    floatValue(): FloatValueContext;
    constructor(ctx: CmdIncContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdLcsContext extends ParserRuleContext {
    LCS(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    LEN(): TerminalNode | undefined;
    IDX(): TerminalNode | undefined;
    MINMATCHLEN(): TerminalNode | undefined;
    intValue(): IntValueContext | undefined;
    WITHMATCHLEN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdMgetContext extends ParserRuleContext {
    MGET(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdMsetContext extends ParserRuleContext {
    MSET(): TerminalNode;
    keyAndString(): KeyAndStringContext[];
    keyAndString(i: number): KeyAndStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdMsetnxContext extends ParserRuleContext {
    MSETNX(): TerminalNode;
    keyAndString(): KeyAndStringContext[];
    keyAndString(i: number): KeyAndStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSetexContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    stringValue(): StringValueContext;
    SETEX(): TerminalNode | undefined;
    PSETEX(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSetContext extends ParserRuleContext {
    SET(): TerminalNode;
    keyName(): KeyNameContext;
    stringValue(): StringValueContext;
    getsetOpt(): GetsetOptContext | undefined;
    KEEPTTL(): TerminalNode | undefined;
    GET(): TerminalNode | undefined;
    NX(): TerminalNode | undefined;
    XX(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSetnxContext extends ParserRuleContext {
    SETNX(): TerminalNode;
    keyName(): KeyNameContext;
    stringValue(): StringValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSetrangeContext extends ParserRuleContext {
    SETRANGE(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    stringValue(): StringValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSetbitContext extends ParserRuleContext {
    SETBIT(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdStrlenContext extends ParserRuleContext {
    STRLEN(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSubstrContext extends ParserRuleContext {
    SUBSTR(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdHdelMgetContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    HDEL(): TerminalNode | undefined;
    HMGET(): TerminalNode | undefined;
    fieldName(): FieldNameContext[];
    fieldName(i: number): FieldNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdHexistsGetKeysStrlenContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    fieldName(): FieldNameContext;
    HEXISTS(): TerminalNode | undefined;
    HGET(): TerminalNode | undefined;
    HSTRLEN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdHgetallLenValsContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    HGETALL(): TerminalNode | undefined;
    HLEN(): TerminalNode | undefined;
    HVALS(): TerminalNode | undefined;
    HKEYS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdHmsetHsetContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    HMSET(): TerminalNode | undefined;
    HSET(): TerminalNode | undefined;
    keyAndString(): KeyAndStringContext[];
    keyAndString(i: number): KeyAndStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdHincrbyContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    fieldName(): FieldNameContext;
    floatValue(): FloatValueContext;
    HINCRBY(): TerminalNode | undefined;
    HINCRBYFLOAT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdHrandfieldContext extends ParserRuleContext {
    HRANDFIELD(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext | undefined;
    WITHVALUES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdHscanContext extends ParserRuleContext {
    HSCAN(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    MATCH(): TerminalNode | undefined;
    pattern(): PatternContext | undefined;
    COUNT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdHsetnxContext extends ParserRuleContext {
    HSETNX(): TerminalNode;
    keyName(): KeyNameContext;
    keyAndString(): KeyAndStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdBlmoveContext extends ParserRuleContext {
    BLMOVE(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    lrOpt(): LrOptContext[];
    lrOpt(i: number): LrOptContext;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdLmpopContext extends ParserRuleContext {
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    lrOpt(): LrOptContext;
    LMPOP(): TerminalNode | undefined;
    BLMPOP(): TerminalNode | undefined;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdBpopContext extends ParserRuleContext {
    intValue(): IntValueContext;
    BLPOP(): TerminalNode | undefined;
    BRPOP(): TerminalNode | undefined;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdBrpoplpushContext extends ParserRuleContext {
    BRPOPLPUSH(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdLindexContext extends ParserRuleContext {
    LINDEX(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdLinsertContext extends ParserRuleContext {
    LINSERT(): TerminalNode;
    keyName(): KeyNameContext;
    stringValue(): StringValueContext[];
    stringValue(i: number): StringValueContext;
    BEFORE(): TerminalNode | undefined;
    AFTER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdLlenContext extends ParserRuleContext {
    LLEN(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdLmoveContext extends ParserRuleContext {
    LMOVE(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    lrOpt(): LrOptContext[];
    lrOpt(i: number): LrOptContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPopContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    LPOP(): TerminalNode | undefined;
    RPOP(): TerminalNode | undefined;
    intValue(): IntValueContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdLposContext extends ParserRuleContext {
    LPOS(): TerminalNode;
    keyName(): KeyNameContext;
    fieldName(): FieldNameContext;
    rankOpt(): RankOptContext | undefined;
    countOpt(): CountOptContext | undefined;
    maxlenOpt(): MaxlenOptContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPushContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    LPUSH(): TerminalNode | undefined;
    LPUSHX(): TerminalNode | undefined;
    RPUSH(): TerminalNode | undefined;
    RPUSHX(): TerminalNode | undefined;
    stringValue(): StringValueContext[];
    stringValue(i: number): StringValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdLrangeTrimContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    LRANGE(): TerminalNode | undefined;
    LTRIM(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdLremSetContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    stringValue(): StringValueContext;
    LREM(): TerminalNode | undefined;
    LSET(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdRpoplpushContext extends ParserRuleContext {
    RPOPLPUSH(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSaddContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    SADD(): TerminalNode | undefined;
    SREM(): TerminalNode | undefined;
    SMISMEMBER(): TerminalNode | undefined;
    fieldName(): FieldNameContext[];
    fieldName(i: number): FieldNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdScardContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    SCARD(): TerminalNode | undefined;
    SMEMBERS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSdiffContext extends ParserRuleContext {
    SDIFF(): TerminalNode | undefined;
    SINTER(): TerminalNode | undefined;
    SUNION(): TerminalNode | undefined;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSdiffstoreContext extends ParserRuleContext {
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    SDIFFSTORE(): TerminalNode | undefined;
    SINTERSTORE(): TerminalNode | undefined;
    SUNIONSTORE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSintercardContext extends ParserRuleContext {
    SINTERCARD(): TerminalNode;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    LIMIT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSismemberContext extends ParserRuleContext {
    SISMEMBER(): TerminalNode;
    keyName(): KeyNameContext;
    fieldName(): FieldNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSmoveContext extends ParserRuleContext {
    SMOVE(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    fieldName(): FieldNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSpopContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    SPOP(): TerminalNode | undefined;
    SRANDMEMBER(): TerminalNode | undefined;
    intValue(): IntValueContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSscanContext extends ParserRuleContext {
    SSCAN(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    MATCH(): TerminalNode | undefined;
    pattern(): PatternContext | undefined;
    countOpt(): CountOptContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdBzmpopContext extends ParserRuleContext {
    BZMPOP(): TerminalNode;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    MIN(): TerminalNode | undefined;
    MAX(): TerminalNode | undefined;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    countOpt(): CountOptContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdBzpopmaxContext extends ParserRuleContext {
    intValue(): IntValueContext;
    BZPOPMAX(): TerminalNode | undefined;
    BZPOPMIN(): TerminalNode | undefined;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZaddContext extends ParserRuleContext {
    ZADD(): TerminalNode;
    keyName(): KeyNameContext;
    CH(): TerminalNode | undefined;
    INCR(): TerminalNode | undefined;
    intAndString(): IntAndStringContext[];
    intAndString(i: number): IntAndStringContext;
    NX(): TerminalNode | undefined;
    XX(): TerminalNode | undefined;
    GT(): TerminalNode | undefined;
    LT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZcardContext extends ParserRuleContext {
    ZCARD(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZcountContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    min(): MinContext;
    max(): MaxContext;
    ZCOUNT(): TerminalNode | undefined;
    ZLEXCOUNT(): TerminalNode | undefined;
    ZREMRANGEBYLEX(): TerminalNode | undefined;
    ZREMRANGEBYSCORE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZdiffContext extends ParserRuleContext {
    ZDIFF(): TerminalNode;
    intValue(): IntValueContext;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    WITHSCORES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZdiffstoreContext extends ParserRuleContext {
    ZDIFFSTORE(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZincrbyContext extends ParserRuleContext {
    ZINCRBY(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    fieldName(): FieldNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZinterContext extends ParserRuleContext {
    ZINTER(): TerminalNode;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    WEIGHTS(): TerminalNode | undefined;
    AGGREGATE(): TerminalNode | undefined;
    WITHSCORES(): TerminalNode | undefined;
    SUM(): TerminalNode | undefined;
    MIN(): TerminalNode | undefined;
    MAX(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZintercardContext extends ParserRuleContext {
    ZINTERCARD(): TerminalNode;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    LIMIT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZinterstoreContext extends ParserRuleContext {
    ZINTERSTORE(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    WEIGHTS(): TerminalNode | undefined;
    AGGREGATE(): TerminalNode | undefined;
    SUM(): TerminalNode | undefined;
    MIN(): TerminalNode | undefined;
    MAX(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZmpopContext extends ParserRuleContext {
    ZMPOP(): TerminalNode;
    intValue(): IntValueContext;
    countOpt(): CountOptContext;
    MIN(): TerminalNode | undefined;
    MAX(): TerminalNode | undefined;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZmscoreContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    ZMSCORE(): TerminalNode | undefined;
    ZREM(): TerminalNode | undefined;
    fieldName(): FieldNameContext[];
    fieldName(i: number): FieldNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZpopmaxContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    ZPOPMAX(): TerminalNode | undefined;
    ZPOPMIN(): TerminalNode | undefined;
    intValue(): IntValueContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZrandmemberContext extends ParserRuleContext {
    ZRANDMEMBER(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext | undefined;
    WITHSCORES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZrangeContext extends ParserRuleContext {
    ZRANGE(): TerminalNode;
    keyName(): KeyNameContext;
    min(): MinContext;
    max(): MaxContext;
    REV(): TerminalNode | undefined;
    limitOpt(): LimitOptContext | undefined;
    WITHSCORES(): TerminalNode | undefined;
    BYSCORE(): TerminalNode | undefined;
    BYLEX(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZrangebylexContext extends ParserRuleContext {
    ZRANGEBYLEX(): TerminalNode;
    keyName(): KeyNameContext;
    min(): MinContext;
    max(): MaxContext;
    limitOpt(): LimitOptContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZrangebyscoreContext extends ParserRuleContext {
    ZRANGEBYSCORE(): TerminalNode;
    keyName(): KeyNameContext;
    min(): MinContext;
    max(): MaxContext;
    WITHSCORES(): TerminalNode | undefined;
    limitOpt(): LimitOptContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZrangestoreContext extends ParserRuleContext {
    ZRANGESTORE(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    min(): MinContext;
    max(): MaxContext;
    limitOpt(): LimitOptContext | undefined;
    BYSCORE(): TerminalNode | undefined;
    BYLEX(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZrankContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    fieldName(): FieldNameContext;
    ZRANK(): TerminalNode | undefined;
    ZREVRANK(): TerminalNode | undefined;
    ZSCORE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZremrangebyrankContext extends ParserRuleContext {
    ZREMRANGEBYRANK(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZrevrangeContext extends ParserRuleContext {
    ZREVRANGE(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    WITHSCORES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZrevrangebylexContext extends ParserRuleContext {
    keyName(): KeyNameContext;
    max(): MaxContext;
    min(): MinContext;
    ZREVRANGEBYLEX(): TerminalNode | undefined;
    ZREVRANGEBYSCORE(): TerminalNode | undefined;
    WITHSCORES(): TerminalNode | undefined;
    limitOpt(): LimitOptContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZscanContext extends ParserRuleContext {
    ZSCAN(): TerminalNode;
    keyName(): KeyNameContext;
    intValue(): IntValueContext;
    countOpt(): CountOptContext;
    MATCH(): TerminalNode | undefined;
    pattern(): PatternContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZunionContext extends ParserRuleContext {
    ZUNION(): TerminalNode;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    WEIGHTS(): TerminalNode | undefined;
    AGGREGATE(): TerminalNode | undefined;
    WITHSCORES(): TerminalNode | undefined;
    SUM(): TerminalNode | undefined;
    MIN(): TerminalNode | undefined;
    MAX(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdZunionstoreContext extends ParserRuleContext {
    ZUNIONSTORE(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    WEIGHTS(): TerminalNode | undefined;
    AGGREGATE(): TerminalNode | undefined;
    SUM(): TerminalNode | undefined;
    MIN(): TerminalNode | undefined;
    MAX(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdScriptKillContext extends ParserRuleContext {
    SCRIPT(): TerminalNode;
    KILL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdScriptLoadContext extends ParserRuleContext {
    SCRIPT(): TerminalNode;
    LOAD(): TerminalNode;
    multiString(): MultiStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdEvalContext extends ParserRuleContext {
    EVAL(): TerminalNode;
    multiString(): MultiStringContext;
    intValue(): IntValueContext;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdEvalshaContext extends ParserRuleContext {
    EVALSHA(): TerminalNode;
    stringValue(): StringValueContext;
    intValue(): IntValueContext;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdScriptExistsContext extends ParserRuleContext {
    SCRIPT(): TerminalNode;
    EXISTS(): TerminalNode;
    stringValue(): StringValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdScriptFlushContext extends ParserRuleContext {
    SCRIPT(): TerminalNode;
    FLUSH(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdExecContext extends ParserRuleContext {
    EXEC(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdWatchContext extends ParserRuleContext {
    WATCH(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdDiscardContext extends ParserRuleContext {
    DISCARD(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdUnwatchContext extends ParserRuleContext {
    UNWATCH(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdMultiContext extends ParserRuleContext {
    MULTI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPfmergeContext extends ParserRuleContext {
    PFMERGE(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPfaddContext extends ParserRuleContext {
    PFADD(): TerminalNode;
    keyName(): KeyNameContext;
    fieldName(): FieldNameContext[];
    fieldName(i: number): FieldNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPfcountContext extends ParserRuleContext {
    PFCOUNT(): TerminalNode;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSsubscribeContext extends ParserRuleContext {
    SSUBSCRIBE(): TerminalNode | undefined;
    SUBSCRIBE(): TerminalNode | undefined;
    PSUBSCRIBE(): TerminalNode | undefined;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdUnsubscribeContext extends ParserRuleContext {
    UNSUBSCRIBE(): TerminalNode | undefined;
    PUNSUBSCRIBE(): TerminalNode | undefined;
    SUNSUBSCRIBE(): TerminalNode | undefined;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPusubnumpatContext extends ParserRuleContext {
    PUBSUB(): TerminalNode;
    NUMPAT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPusubnumsubContext extends ParserRuleContext {
    PUBSUB(): TerminalNode;
    NUMSUB(): TerminalNode | undefined;
    SHARDNUMSUB(): TerminalNode | undefined;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPusubchannelsContext extends ParserRuleContext {
    PUBSUB(): TerminalNode;
    CHANNELS(): TerminalNode | undefined;
    SHARDCHANNELS(): TerminalNode | undefined;
    keyName(): KeyNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPublishContext extends ParserRuleContext {
    PUBLISH(): TerminalNode;
    keyName(): KeyNameContext;
    fieldName(): FieldNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdAskingContext extends ParserRuleContext {
    ASKING(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdReadonlyContext extends ParserRuleContext {
    READONLY(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdReadwriteContext extends ParserRuleContext {
    READWRITE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdAddDelSlotsContext extends ParserRuleContext {
    CLUSTER(): TerminalNode;
    ADDSLOTS(): TerminalNode | undefined;
    DELSLOTS(): TerminalNode | undefined;
    intValue(): IntValueContext[];
    intValue(i: number): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdCountKeysInSlotContext extends ParserRuleContext {
    CLUSTER(): TerminalNode;
    COUNTKEYSINSLOT(): TerminalNode;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdFailOverContext extends ParserRuleContext {
    CLUSTER(): TerminalNode;
    FAILOVER(): TerminalNode;
    FORCE(): TerminalNode | undefined;
    TAKEOVER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdForgetContext extends ParserRuleContext {
    CLUSTER(): TerminalNode;
    FORGET(): TerminalNode;
    stringValue(): StringValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdGetKeysInSlotContext extends ParserRuleContext {
    CLUSTER(): TerminalNode;
    GETKEYSINSLOT(): TerminalNode;
    intValue(): IntValueContext;
    cout(): CoutContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdClusterOrderContext extends ParserRuleContext {
    CLUSTER(): TerminalNode;
    INFO(): TerminalNode | undefined;
    BUMPEPOCH(): TerminalNode | undefined;
    LINKS(): TerminalNode | undefined;
    FLUSHSLOTS(): TerminalNode | undefined;
    MYID(): TerminalNode | undefined;
    MYSHARDID(): TerminalNode | undefined;
    NODES(): TerminalNode | undefined;
    SAVECONFIG(): TerminalNode | undefined;
    SHARDS(): TerminalNode | undefined;
    SLOTS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdKeySlotContext extends ParserRuleContext {
    CLUSTER(): TerminalNode;
    KEYSLOT(): TerminalNode;
    keyName(): KeyNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdMeetContext extends ParserRuleContext {
    CLUSTER(): TerminalNode;
    MEET(): TerminalNode;
    HOST(): TerminalNode;
    port(): PortContext;
    intValue(): IntValueContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdReplicaesSlaveContext extends ParserRuleContext {
    CLUSTER(): TerminalNode;
    stringValue(): StringValueContext;
    REPLICAS(): TerminalNode | undefined;
    REPLICATE(): TerminalNode | undefined;
    SLAVES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdResetContext extends ParserRuleContext {
    CLUSTER(): TerminalNode;
    RESET(): TerminalNode;
    HARD(): TerminalNode | undefined;
    SOFT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSetSlotContext extends ParserRuleContext {
    CLUSTER(): TerminalNode;
    SETSLOT(): TerminalNode;
    intValue(): IntValueContext;
    STABLE(): TerminalNode | undefined;
    MIGRATING(): TerminalNode | undefined;
    stringValue(): StringValueContext | undefined;
    IMPORTING(): TerminalNode | undefined;
    NODE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdFlushdbContext extends ParserRuleContext {
    FLUSHDB(): TerminalNode;
    ASYNC(): TerminalNode | undefined;
    SYNC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdEchoContext extends ParserRuleContext {
    ECHO(): TerminalNode;
    stringValue(): StringValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSaveContext extends ParserRuleContext {
    SAVE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSlowlogContext extends ParserRuleContext {
    SLOWLOG(): TerminalNode;
    sqlStatement(): SqlStatementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdLastsaveContext extends ParserRuleContext {
    LASTSAVE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdConfigContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: CmdConfigContext): void;
}
export declare class CmdConfigGetContext extends CmdConfigContext {
    CONFIG(): TerminalNode;
    GET(): TerminalNode | undefined;
    pattern(): PatternContext | undefined;
    constructor(ctx: CmdConfigContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdConfigSetContext extends CmdConfigContext {
    SET(): TerminalNode | undefined;
    stringValue(): StringValueContext[];
    stringValue(i: number): StringValueContext;
    constructor(ctx: CmdConfigContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdConfigResetContext extends CmdConfigContext {
    RESETSTAT(): TerminalNode;
    constructor(ctx: CmdConfigContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdConfigRewriteContext extends CmdConfigContext {
    REWRITE(): TerminalNode;
    constructor(ctx: CmdConfigContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdClientContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: CmdClientContext): void;
}
export declare class CmdClientPauseContext extends CmdClientContext {
    CLIENT(): TerminalNode;
    PAUSE(): TerminalNode | undefined;
    intValue(): IntValueContext | undefined;
    constructor(ctx: CmdClientContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdClientKillContext extends CmdClientContext {
    KILL(): TerminalNode | undefined;
    HOST_PORT(): TerminalNode | undefined;
    constructor(ctx: CmdClientContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdClientGetnameContext extends CmdClientContext {
    GETNAME(): TerminalNode;
    constructor(ctx: CmdClientContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdClientSetnameContext extends CmdClientContext {
    SETNAME(): TerminalNode | undefined;
    stringValue(): StringValueContext | undefined;
    constructor(ctx: CmdClientContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdClientListContext extends CmdClientContext {
    LIST(): TerminalNode;
    constructor(ctx: CmdClientContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdShutdownContext extends ParserRuleContext {
    SHUTDOWN(): TerminalNode;
    NOSAVE(): TerminalNode | undefined;
    SAVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSyncContext extends ParserRuleContext {
    SYNC(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdRoleContext extends ParserRuleContext {
    ROLE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdMonitorContext extends ParserRuleContext {
    MONITOR(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSlaveofContext extends ParserRuleContext {
    SLAVEOF(): TerminalNode;
    HOST(): TerminalNode;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdFlushallContext extends ParserRuleContext {
    FLUSHALL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdSelectContext extends ParserRuleContext {
    SELECT(): TerminalNode;
    intValue(): IntValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdPingContext extends ParserRuleContext {
    PING(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdQuitContext extends ParserRuleContext {
    QUIT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdAuthContext extends ParserRuleContext {
    AUTH(): TerminalNode;
    stringValue(): StringValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdDbsizeContext extends ParserRuleContext {
    DBSIZE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdBgrewriteaofContext extends ParserRuleContext {
    BGREWRITEAOF(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdTimeContext extends ParserRuleContext {
    TIME(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdInfoContext extends ParserRuleContext {
    INFO(): TerminalNode;
    infoOpt(): InfoOptContext[];
    infoOpt(i: number): InfoOptContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdBgsaveContext extends ParserRuleContext {
    BGSAVE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdCmdContext extends ParserRuleContext {
    COMMAND(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdCmdxContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: CmdCmdxContext): void;
}
export declare class CmdCmdInfoContext extends CmdCmdxContext {
    COMMAND(): TerminalNode;
    INFO(): TerminalNode | undefined;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(ctx: CmdCmdxContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdCmdGetkeysContext extends CmdCmdxContext {
    GETKEYS(): TerminalNode | undefined;
    keyName(): KeyNameContext[];
    keyName(i: number): KeyNameContext;
    constructor(ctx: CmdCmdxContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdCmdCountContext extends CmdCmdxContext {
    COUNT(): TerminalNode;
    constructor(ctx: CmdCmdxContext);
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class CmdDebugContext extends ParserRuleContext {
    DEBUG(): TerminalNode;
    SEGFAULT(): TerminalNode | undefined;
    OBJECT(): TerminalNode | undefined;
    keyName(): KeyNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
export declare class SqlStatementContext extends ParserRuleContext {
    cmdCopy(): CmdCopyContext | undefined;
    cmdDel(): CmdDelContext | undefined;
    cmdDump(): CmdDumpContext | undefined;
    cmdExists(): CmdExistsContext | undefined;
    cmdExpire(): CmdExpireContext | undefined;
    cmdExpireat(): CmdExpireatContext | undefined;
    cmdExpireTime(): CmdExpireTimeContext | undefined;
    cmdKeys(): CmdKeysContext | undefined;
    cmdMove(): CmdMoveContext | undefined;
    cmdObject(): CmdObjectContext | undefined;
    cmdPersist(): CmdPersistContext | undefined;
    cmdPexpire(): CmdPexpireContext | undefined;
    cmdPexpireat(): CmdPexpireatContext | undefined;
    cmdPExpireTime(): CmdPExpireTimeContext | undefined;
    cmdTtl(): CmdTtlContext | undefined;
    cmdPttl(): CmdPttlContext | undefined;
    cmdRandomkey(): CmdRandomkeyContext | undefined;
    cmdRename(): CmdRenameContext | undefined;
    cmdRenamenx(): CmdRenamenxContext | undefined;
    cmdRestore(): CmdRestoreContext | undefined;
    cmdScan(): CmdScanContext | undefined;
    cmdSort(): CmdSortContext | undefined;
    cmdSortro(): CmdSortroContext | undefined;
    cmdTouch(): CmdTouchContext | undefined;
    cmdType(): CmdTypeContext | undefined;
    cmdUnlink(): CmdUnlinkContext | undefined;
    cmdWait(): CmdWaitContext | undefined;
    cmdAppend(): CmdAppendContext | undefined;
    cmdDecr(): CmdDecrContext | undefined;
    cmdDecrby(): CmdDecrbyContext | undefined;
    cmdGet(): CmdGetContext | undefined;
    cmdGetdel(): CmdGetdelContext | undefined;
    cmdGetex(): CmdGetexContext | undefined;
    cmdGetrange(): CmdGetrangeContext | undefined;
    cmdGetset(): CmdGetsetContext | undefined;
    cmdGetbit(): CmdGetbitContext | undefined;
    cmdInc(): CmdIncContext | undefined;
    cmdLcs(): CmdLcsContext | undefined;
    cmdMget(): CmdMgetContext | undefined;
    cmdMset(): CmdMsetContext | undefined;
    cmdMsetnx(): CmdMsetnxContext | undefined;
    cmdSetex(): CmdSetexContext | undefined;
    cmdSet(): CmdSetContext | undefined;
    cmdSetnx(): CmdSetnxContext | undefined;
    cmdSetrange(): CmdSetrangeContext | undefined;
    cmdSetbit(): CmdSetbitContext | undefined;
    cmdStrlen(): CmdStrlenContext | undefined;
    cmdSubstr(): CmdSubstrContext | undefined;
    cmdHdelMget(): CmdHdelMgetContext | undefined;
    cmdHexistsGetKeysStrlen(): CmdHexistsGetKeysStrlenContext | undefined;
    cmdHgetallLenVals(): CmdHgetallLenValsContext | undefined;
    cmdHincrby(): CmdHincrbyContext | undefined;
    cmdHmsetHset(): CmdHmsetHsetContext | undefined;
    cmdHrandfield(): CmdHrandfieldContext | undefined;
    cmdHscan(): CmdHscanContext | undefined;
    cmdHsetnx(): CmdHsetnxContext | undefined;
    cmdBlmove(): CmdBlmoveContext | undefined;
    cmdLmpop(): CmdLmpopContext | undefined;
    cmdBpop(): CmdBpopContext | undefined;
    cmdBrpoplpush(): CmdBrpoplpushContext | undefined;
    cmdLindex(): CmdLindexContext | undefined;
    cmdLinsert(): CmdLinsertContext | undefined;
    cmdLlen(): CmdLlenContext | undefined;
    cmdLmove(): CmdLmoveContext | undefined;
    cmdPop(): CmdPopContext | undefined;
    cmdLpos(): CmdLposContext | undefined;
    cmdPush(): CmdPushContext | undefined;
    cmdLrangeTrim(): CmdLrangeTrimContext | undefined;
    cmdLremSet(): CmdLremSetContext | undefined;
    cmdRpoplpush(): CmdRpoplpushContext | undefined;
    cmdSadd(): CmdSaddContext | undefined;
    cmdScard(): CmdScardContext | undefined;
    cmdSdiff(): CmdSdiffContext | undefined;
    cmdSdiffstore(): CmdSdiffstoreContext | undefined;
    cmdSintercard(): CmdSintercardContext | undefined;
    cmdSismember(): CmdSismemberContext | undefined;
    cmdSmove(): CmdSmoveContext | undefined;
    cmdSpop(): CmdSpopContext | undefined;
    cmdSscan(): CmdSscanContext | undefined;
    cmdBzmpop(): CmdBzmpopContext | undefined;
    cmdBzpopmax(): CmdBzpopmaxContext | undefined;
    cmdZadd(): CmdZaddContext | undefined;
    cmdZcard(): CmdZcardContext | undefined;
    cmdZcount(): CmdZcountContext | undefined;
    cmdZdiff(): CmdZdiffContext | undefined;
    cmdZdiffstore(): CmdZdiffstoreContext | undefined;
    cmdZincrby(): CmdZincrbyContext | undefined;
    cmdZinter(): CmdZinterContext | undefined;
    cmdZintercard(): CmdZintercardContext | undefined;
    cmdZinterstore(): CmdZinterstoreContext | undefined;
    cmdZmpop(): CmdZmpopContext | undefined;
    cmdZmscore(): CmdZmscoreContext | undefined;
    cmdZpopmax(): CmdZpopmaxContext | undefined;
    cmdZrandmember(): CmdZrandmemberContext | undefined;
    cmdZrange(): CmdZrangeContext | undefined;
    cmdZrangebylex(): CmdZrangebylexContext | undefined;
    cmdZrangebyscore(): CmdZrangebyscoreContext | undefined;
    cmdZrangestore(): CmdZrangestoreContext | undefined;
    cmdZrank(): CmdZrankContext | undefined;
    cmdZremrangebyrank(): CmdZremrangebyrankContext | undefined;
    cmdZrevrange(): CmdZrevrangeContext | undefined;
    cmdZrevrangebylex(): CmdZrevrangebylexContext | undefined;
    cmdZscan(): CmdZscanContext | undefined;
    cmdZunion(): CmdZunionContext | undefined;
    cmdZunionstore(): CmdZunionstoreContext | undefined;
    cmdScriptKill(): CmdScriptKillContext | undefined;
    cmdScriptLoad(): CmdScriptLoadContext | undefined;
    cmdEval(): CmdEvalContext | undefined;
    cmdEvalsha(): CmdEvalshaContext | undefined;
    cmdScriptExists(): CmdScriptExistsContext | undefined;
    cmdScriptFlush(): CmdScriptFlushContext | undefined;
    cmdExec(): CmdExecContext | undefined;
    cmdWatch(): CmdWatchContext | undefined;
    cmdDiscard(): CmdDiscardContext | undefined;
    cmdUnwatch(): CmdUnwatchContext | undefined;
    cmdMulti(): CmdMultiContext | undefined;
    cmdPfmerge(): CmdPfmergeContext | undefined;
    cmdPfadd(): CmdPfaddContext | undefined;
    cmdPfcount(): CmdPfcountContext | undefined;
    cmdEcho(): CmdEchoContext | undefined;
    cmdSelect(): CmdSelectContext | undefined;
    cmdPing(): CmdPingContext | undefined;
    cmdSave(): CmdSaveContext | undefined;
    cmdSlowlog(): CmdSlowlogContext | undefined;
    cmdLastsave(): CmdLastsaveContext | undefined;
    cmdConfig(): CmdConfigContext | undefined;
    cmdClient(): CmdClientContext | undefined;
    cmdShutdown(): CmdShutdownContext | undefined;
    cmdSync(): CmdSyncContext | undefined;
    cmdRole(): CmdRoleContext | undefined;
    cmdMonitor(): CmdMonitorContext | undefined;
    cmdSlaveof(): CmdSlaveofContext | undefined;
    cmdFlushall(): CmdFlushallContext | undefined;
    cmdQuit(): CmdQuitContext | undefined;
    cmdAuth(): CmdAuthContext | undefined;
    cmdDbsize(): CmdDbsizeContext | undefined;
    cmdBgrewriteaof(): CmdBgrewriteaofContext | undefined;
    cmdTime(): CmdTimeContext | undefined;
    cmdInfo(): CmdInfoContext | undefined;
    cmdBgsave(): CmdBgsaveContext | undefined;
    cmdCmd(): CmdCmdContext | undefined;
    cmdCmdx(): CmdCmdxContext | undefined;
    cmdDebug(): CmdDebugContext | undefined;
    cmdFlushdb(): CmdFlushdbContext | undefined;
    cmdSsubscribe(): CmdSsubscribeContext | undefined;
    cmdUnsubscribe(): CmdUnsubscribeContext | undefined;
    cmdPusubnumsub(): CmdPusubnumsubContext | undefined;
    cmdPusubnumpat(): CmdPusubnumpatContext | undefined;
    cmdPusubchannels(): CmdPusubchannelsContext | undefined;
    cmdPublish(): CmdPublishContext | undefined;
    cmdAsking(): CmdAskingContext | undefined;
    cmdAddDelSlots(): CmdAddDelSlotsContext | undefined;
    cmdCountKeysInSlot(): CmdCountKeysInSlotContext | undefined;
    cmdFailOver(): CmdFailOverContext | undefined;
    cmdForget(): CmdForgetContext | undefined;
    cmdGetKeysInSlot(): CmdGetKeysInSlotContext | undefined;
    cmdClusterOrder(): CmdClusterOrderContext | undefined;
    cmdKeySlot(): CmdKeySlotContext | undefined;
    cmdMeet(): CmdMeetContext | undefined;
    cmdReadonly(): CmdReadonlyContext | undefined;
    cmdReadwrite(): CmdReadwriteContext | undefined;
    cmdReplicaesSlave(): CmdReplicaesSlaveContext | undefined;
    cmdReset(): CmdResetContext | undefined;
    cmdSetSlot(): CmdSetSlotContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: RedisSqlParserListener): void;
    exitRule(listener: RedisSqlParserListener): void;
    accept<Result>(visitor: RedisSqlParserVisitor<Result>): Result;
}
