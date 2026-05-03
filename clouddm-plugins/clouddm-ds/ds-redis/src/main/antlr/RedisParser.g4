// Define a grammar called Hello
parser grammar RedisParser;
options { tokenVocab = RedisLexer; }
/* ----------------------------------------------------------------------------------- basic type */
intValue    : ARG | BIT_VALUE | INTEGER_NUM;
bitValue    : ARG | BIT_VALUE;
floatValue  : ARG | BIT_VALUE | INTEGER_NUM | N_INF | S_INF | DECIMAL_NUM;
keyName     : ARG | idStr | STRING | NAME_TOKEN;
pattern     : ARG | idStr | STRING | RANGE_NUM | PATTERN_STRING;
stringValue : ARG | STRING;
multiString : ARG | STRING/* | MULTI_STRING TODO */;
offsetNum   : ARG | OFFSET | intValue;
aclCmdRule  : ARG | NAME_TOKEN | ACL_RULE_PATTERN | ALLKEYS | RESETKEYS | ALLCHANNELS
                  | RESETCHANNELS | ALLCOMMANDS | NOCOMMANDS | CLEARSELECTORS | NOPASS
                  | RESET | OFF | ON | (ACL_RULE_PRE_CMD keyWordValue);
                  // >password           : Adds the specified clear text password as a hashed password in the list of the users passwords. Every user can have many active passwords, so that password rotation will be simpler. The specified password is not stored as clear text inside the server. Example: >mypassword.
                  // #<hashedpassword>   : Adds the specified hashed password to the list of user passwords. A Redis hashed password is hashed with SHA256 and translated into a hexadecimal string. Example: #c3ab8ff13720e8ad9047dd39466b3c8974e592c2fa383d4a3960714caef0c4f2.
                  // <password: Like >   password but removes the password instead of adding it.
                  // !<hashedpassword>   : Like #<hashedpassword> but removes the password instead of adding it.
                  // (<rule list>)       : (Available in Redis 7.0 and later) Creates a new selector to match rules against. Selectors are evaluated after the user permissions, and are evaluated according to the order they are defined. If a command matches either the user permissions or any selector, it is allowed. See selectors for more information.


idStr       : BIT_VALUE | INTEGER_NUM | NAME_TOKEN | N_INF | S_INF | RANGE_NUM | keyWordValue;
keyWordValue: COPY | DEL | DUMP | EXISTS | EXPIRE | EXPIREAT | EXPIRETIME | KEYS | MOVE | OBJECT | PERSIST | PEXPIRE
            | PEXPIREAT | PEXPIRETIME | TTL | PTTL | RANDOMKEY | RENAME | RENAMENX | RESTORE | SCAN | SORT | SORT_RO
            | TOUCH | TYPE | UNLINK | WAIT | WAITAOF | APPEND | DECR | DECRBY | GET | GETDEL | GETEX | GETRANGE | GETSET
            | INCR | INCRBY | INCRBYFLOAT | LCS | MGET | MSET | MSETNX | SETEX | PSETEX | SET | SETNX | SETRANGE | STRLEN
            | SUBSTR | BITCOUNT | BITFIELD_RO | BITFIELD | BITOP | BITPOS | GETBIT | SETBIT | HDEL | HEXISTS | HEXPIRE
            | HEXPIREAT | HEXPIRETIME | HGET | HGETALL | HGETDEL | HGETEX | HINCRBY | HINCRBYFLOAT | HKEYS | HLEN | HMGET
            | HMSET | HPERSIST | HPEXPIRE | HPEXPIREAT | HPEXPIRETIME | HPTTL | HTTL | HRANDFIELD | HSCAN | HSET | HSETEX
            | HSETNX | HSTRLEN | HVALS | OVERFLOW | WRAP | SAT | FAIL | BLMOVE | BLMPOP | BLPOP | BRPOP | BRPOPLPUSH
            | LINDEX | LINSERT | LLEN | LMOVE | LMPOP | LPOP | LPOS | LPUSH | LPUSHX | LRANGE | LREM | LSET | LTRIM | LEN
            | RPOP | RPOPLPUSH | RPUSH | RPUSHX | SADD | SCARD | SDIFF | SDIFFSTORE | SINTER | SINTERCARD | SINTERSTORE
            | SISMEMBER | SMEMBERS | SMISMEMBER | SMOVE | SPOP | SRANDMEMBER | SREM | SSCAN | SUNION | SUNIONSTORE| PXAT
            | BZMPOP | BZPOPMAX | BZPOPMIN | ZADD | ZCARD | ZCOUNT | ZDIFF | ZDIFFSTORE | ZINCRBY | ZINTER | ZINTERCARD
            | ZINTERSTORE | ZLEXCOUNT | ZMPOP | ZMSCORE | ZPOPMAX | ZPOPMIN | ZRANDMEMBER | ZRANGE | ZRANGEBYLEX | STORE
            | ZRANGEBYSCORE | ZRANGESTORE | ZRANK | ZREM | ZREMRANGEBYLEX | ZREMRANGEBYRANK | ZREMRANGEBYSCORE | ZREVRANGE
            | ZREVRANGEBYLEX | ZREVRANGEBYSCORE | ZREVRANK | ZSCAN | ZSCORE | ZUNION | ZUNIONSTORE | SCRIPT | EVAL| EX | PX
            | EVAL_RO | EVALSHA | EVALSHA_RO | FCALL | FCALL_RO | FUNCTION | DISCARD | EXEC | MULTI | UNWATCH | WATCH | MIN
            | PFADD | PFCOUNT | PFMERGE | /*PFSELFTEST |*/ PSUBSCRIBE | PUBLISH | PUBSUB | CHANNELS | NUMPAT | NUMSUB| WRITE
            | SHARDCHANNELS | SHARDNUMSUB | PUNSUBSCRIBE | SPUBLISH | SSUBSCRIBE | SUBSCRIBE | SUNSUBSCRIBE | UNSUBSCRIBE
            | ASKING | CLUSTER | ADDSLOTS | ADDSLOTSRANGE | BUMPEPOCH | COUNT_FAILURE_REPORTS | COUNTKEYSINSLOT | DELSLOTS
            | DELSLOTSRANGE | FORGET | GETKEYSINSLOT | KEYSLOT | LINKS | MEET | MYID | MYSHARDID | NODES | REPLICAS | IDX
            | REPLICATE | RESET | SAVECONFIG | SET_CONFIG_EPOCH | SETSLOT | SHARDS | SLAVES | SLOT_STATS | SLOTS | READONLY
            | READWRITE | INFO | SERVER | CLIENTS | PERSISTENCE | THREADS | STATS | REPLICATION | CPU | COMMANDSTATS | MAX
            | LATENCYSTATS | SENTINEL | MODULES | KEYSPACE | ERRORSTATS | ALL | DEFAULT | EVERYTHING | ACL | CAT | DELUSER
            | DRYRUN | GENPASS | GETUSER | LIST | LOAD | LOG | SETUSER | USERS | WHOAMI | COMMAND | COUNT | DOCS | GETKEYS
            | GETKEYSANDFLAGS | FILTERBY | ACLCAT | PATTERN | CONFIG | RESETSTAT | REWRITE | LATENCY | DOCTOR | GRAPH | EXAT
            | HISTOGRAM | HISTORY | LATEST | MEMORY | MALLOC_STATS | PURGE | USAGE | SAMPLES | MODULE | LOADEX | UNLOAD | CH
            | ARGS | BGREWRITEAOF | BGSAVE | DBSIZE | FAILOVER | FLUSHALL | FLUSHDB | LASTSAVE | LOLWUT | MONITOR | PSYNC
            | /* REPLCONF |*/ REPLICAOF | /*RESTORE_ASKING |*/ ROLE | SAVE | SHUTDOWN | SLAVEOF | SLOWLOG | SWAPDB | SYNC | TIME
            | CLIENT | CACHING | GETNAME | GETREDIR | ID | KILL | NO_EVICT | NO_TOUCH | PAUSE | REPLY | SETINFO | SETNAME
            | TRACKING | TRACKINGINFO | UNBLOCK | UNPAUSE | LIB_NAME | LIB_VER | AUTH | ECHO | HELLO | PING | QUIT | SELECT
            | NORMAL | MASTER | SLAVE | REPLICA | FLUSHSLOTS | FORCE | TAKEOVER | NODE | HARD | SOFT | IMPORTING | MIGRATING
            | STABLE | USER | ADDR | LADDR | SKIPME | SKIP_ | MAXAGE | ENCODING | IDLETIME | REFCOUNT | ABSTTL | REPLACE
            | MINMATCHLEN | WITHMATCHLEN | KEEPTTL | WITHSCORE | WITHSCORES | SLOTSRANGE | ORDERBY | SLOT | TO | ABORT | ONE
            | TIMEOUT | VERSION | NOW | REDIRECT | BCAST | OPTIN | OPTOUT | NOLOOP | PREFIX | ERROR | LIMIT | BEFORE | ALPHA
            | AFTER | FLUSH | NOSAVE | SEGFAULT | WITHVALUES | MATCH | WEIGHTS | AGGREGATE | SUM | BYSCORE | BYLEX | REV
            | LEFT | RIGHT | RANK | MAXLEN | ASYNC | BIT | BYTE | AND | OR | XOR | NOT | DIFF | DIFF1 | ANDOR | ON | OFF
            | YES | NO | NOVALUES | FNX | FXX | DB | NX | XX | GT | LT | FREQ | BY | ASC | DESC | DEBUG | DELETE | LIBRARYNAME
            | WITHCODE | SCHEDULE | ALLKEYS | RESETKEYS | ALLCHANNELS | RESETCHANNELS | ALLCOMMANDS | NOCOMMANDS
            | CLEARSELECTORS | NOPASS
            ;

/* alias */
valueAsStr  : stringValue | multiString | pattern | keyName | idStr | intValue | floatValue;
arg         : valueAsStr;
rangeInt  : pattern | intValue;
min       : pattern | floatValue;
max       : pattern | floatValue;
port      : intValue;
weight    : intValue;
sha1      : valueAsStr;
key       : keyName | pattern;
cursor    : intValue;
count     : intValue;
fieldName : keyName;
element   : keyName;
member    : keyName;
srcKey    : keyName;
channel   : keyName;
slot      : intValue;

/* part commands tokens */
keyAndString    : keyName valueAsStr;
patternOpt      : GET pattern;          // only for SORT、SORT_RO
keyOpt          : NX | XX | GT | LT;    // only for EXPIRE、EXPIREAT、PEXPIRE、PEXPIREAT
limitOpt        : LIMIT offset=intValue count;
sortOpt         : ASC | DESC;
ttlOpt          : (EX | PX | EXAT | PXAT) expirTime=intValue;// only for SET、GETEX
directionOpt    : LEFT | RIGHT;         // only for BLMOVE、LMPOP、LMOVE
/* ----------------------------------------------------------------------------------- Keys commands */
cmdCopy         : COPY          src = keyName dst=keyName (DB db=intValue)? REPLACE?;
cmdDel          : DEL           keyName+;
cmdDump         : DUMP          keyName;
cmdExists       : EXISTS        keyName+;
cmdExpire       : EXPIRE        keyName seconds=intValue keyOpt?;
cmdExpireat     : EXPIREAT      keyName unixTimeSeconds=intValue keyOpt?;
cmdExpireTime   : EXPIRETIME    keyName;
cmdKeys         : KEYS          pattern;
//cmdMigrate      : MIGRATE host port key|"" destination-db timeout [COPY] [REPLACE] [AUTH password] [AUTH2 username password] [KEYS key [key ...]]
cmdMove         : MOVE          keyName db=intValue;
cmdObject       : OBJECT (ENCODING | FREQ | IDLETIME | REFCOUNT) keyName;
cmdPersist      : PERSIST       keyName;
cmdPexpire      : PEXPIRE       keyName milliseconds=intValue keyOpt?;
cmdPexpireat    : PEXPIREAT     keyName unixTimeSeconds=intValue keyOpt?;
cmdPExpireTime  : PEXPIRETIME   keyName;
cmdTtl          : TTL           keyName;
cmdPttl         : PTTL          keyName;
cmdRandomkey    : RANDOMKEY;
cmdRename       : RENAME        oldKey=keyName newName=keyName;
cmdRenamenx     : RENAMENX      oldKey=keyName newName=keyName;
cmdRestore      : RESTORE       keyName ttl=intValue serializedValue=stringValue REPLACE? ABSTTL? (IDLETIME seconds=intValue)? (FREQ frequency=intValue)?;
cmdScan         : SCAN          cursor (MATCH pattern)? (COUNT count)? (TYPE type=stringValue)?;
cmdSort         : SORT          keyStr=keyName (BY byPattern=pattern)? limitOpt? patternOpt* sortOpt? ALPHA? (STORE destination=keyName)?;
cmdSortro       : SORT_RO       keyStr=keyName (BY byPattern=pattern)? limitOpt? patternOpt* sortOpt? ALPHA?;
cmdTouch        : TOUCH         keyName+;
cmdType         : TYPE          keyName+;
cmdUnlink       : UNLINK        keyName+;
cmdWait         : WAIT          numreplicas=intValue timeout=intValue;
cmdWaitAOF      : WAITAOF       numlocal=intValue numreplicas=intValue timeout=intValue;
/* ----------------------------------------------------------------------------------- String commands */
cmdAppend   : APPEND    keyName valueAsStr;
cmdDecr     : DECR      keyName;
cmdDecrby   : DECRBY    keyName decrement=intValue;
cmdGet      : GET       keyName;
cmdGetdel   : GETDEL    keyName;
cmdGetex    : GETEX     keyName (ttlOpt | PERSIST)?;
cmdGetrange : GETRANGE  keyName startNum=intValue endNum=intValue;
cmdGetset   : GETSET    keyName valueAsStr;
cmdIncr       : INCR        keyName;
cmdIncrby     : INCRBY      keyName increment=intValue;
cmdIncrbyFloat: INCRBYFLOAT keyName increment=floatValue;
cmdLcs      : LCS       key1=keyName key2=keyName LEN? IDX? (MINMATCHLEN minMatchLen=intValue)? WITHMATCHLEN?;
cmdMget     : MGET      keyName+;
cmdMset     : MSET      keyAndString+;
cmdMsetnx   : MSETNX    keyAndString+;
cmdSetex    : SETEX     keyName seconds=intValue valueAsStr;
cmdPSetex   : PSETEX    keyName milliseconds=intValue valueAsStr;
cmdSet      : SET       keyName valueAsStr (NX | XX)? GET? (ttlOpt | KEEPTTL)?;
cmdSetnx    : SETNX     keyName valueAsStr;
cmdSetrange : SETRANGE  keyName offset=intValue valueAsStr;
cmdStrlen   : STRLEN    keyName;
cmdSubstr   : SUBSTR    keyName startNum=intValue endNum=intValue;
/* ----------------------------------------------------------------------------------- Bit commands */
cmdBitCount    : BITCOUNT keyName (start=intValue end=intValue (BYTE | BIT)?)?;
cmdBitField    : BITFIELD keyName bitFieldItem*;
bitFieldItem   : ( GET getencode=encodOffOpt)
               | (  (OVERFLOW (WRAP | SAT | FAIL))? (
                         (SET setOpt=encodOffOpt valueAsStr) |
                         (INCRBY incrOpt=encodOffOpt intValue)
                    )
                 );
encodOffOpt    : encoding=valueAsStr offsetNum;
cmdBitFieldRO  : BITFIELD_RO keyName (GET encodOffOpt)*;
cmdBitOP       : BITOP  (AND | OR | XOR | NOT | DIFF | DIFF1 | ANDOR | ONE) destkey=keyName key+;
cmdBitPos      : BITPOS keyName bitValue (start=intValue (end=intValue (BYTE | BIT)?)?)?;
cmdGetbit      : GETBIT keyName offset=intValue;
cmdSetbit      : SETBIT keyName offset=intValue bitValue;
/* ----------------------------------------------------------------------------------- Hash commands */
cmdHdel        : HDEL    keyName fieldName+;
cmdHexists     : HEXISTS keyName fieldName;
cmdHexpire     : HEXPIRE keyName timeout=intValue keyOpt? FIELDS numfields=intValue fieldName+;
cmdHexpireat   : HEXPIREAT   keyName timeout=intValue keyOpt? FIELDS numfields=intValue fieldName+;
cmdHexpiretime : HEXPIRETIME keyName FIELDS numfields=intValue fieldName+;
cmdHGet        : HGET    keyName fieldName;
cmdHGetAll     : HGETALL keyName;
cmdHgetDel     : HGETDEL keyName FIELDS numfields=intValue fieldName+;
cmdHgetex      : HGETEX  keyName (ttlOpt | PERSIST) FIELDS numfields=intValue fieldName+;
cmdHincrBy     : HINCRBY keyName fieldName increment=floatValue;
cmdHincrByFloat: HINCRBYFLOAT keyName fieldName increment=floatValue;
cmdHKeys       : HKEYS   keyName;
cmdHLen        : HLEN    keyName;
cmdHMget       : HMGET   keyName fieldName+;
cmdHMset       : HMSET   keyName keyAndString+;
cmdHPersist    : HPERSIST keyName FIELDS numfields=intValue fieldName+;
cmdHPexpire    : HPEXPIRE keyName timeout=intValue keyOpt? FIELDS numfields=intValue fieldName+;
cmdHPexpireAt  : HPEXPIREAT keyName timeout=intValue keyOpt? FIELDS numfields=intValue fieldName+;
cmdHPexpireTime: HPEXPIRETIME keyName FIELDS numfields=intValue fieldName+;
cmdHPTTL       : HPTTL keyName FIELDS numfields=intValue fieldName+;
cmdHTTL        : HTTL keyName FIELDS numfields=intValue fieldName+;
cmdHrandfield  : HRANDFIELD keyName (count WITHVALUES?)?;
cmdHscan       : HSCAN   keyName cursor (MATCH pattern)? (COUNT count)? NOVALUES?;
cmdHSet        : HSET    keyName keyAndString+;
cmdHSetex      : HSETEX  keyName (FNX | FXX)? (ttlOpt | KEEPTTL)? FIELDS numfields=intValue keyAndString+;
cmdHsetnx      : HSETNX  keyName fieldName valueAsStr;
cmdHStrLen     : HSTRLEN keyName fieldName;
cmdHVals       : HVALS   keyName;
/* ----------------------------------------------------------------------------------- List commands */
cmdBlmove       : BLMOVE     src=keyName dst=keyName (srcDirection=directionOpt)? (dstDirection=directionOpt)? timeout=intValue;
cmdBLmpop       : BLMPOP     timeout=intValue numkeys=intValue keyName+ directionOpt (COUNT count)?;
cmdBLPop        : BLPOP      keyName+ timeout=intValue;
cmdBRPop        : BRPOP      keyName+ timeout=intValue;
cmdBrpoplpush   : BRPOPLPUSH src=keyName dst=keyName timeout=intValue;
cmdLindex       : LINDEX     keyName index=intValue;
cmdLinsert      : LINSERT    keyName (BEFORE | AFTER) pivot=stringValue element;
cmdLlen         : LLEN       keyName;
cmdLmove        : LMOVE      src=keyName dst=keyName (srcDirection=directionOpt)? (dstDirection=directionOpt)?;
cmdLmpop        : LMPOP      numkeys=intValue keyName+ directionOpt (COUNT count)?;
cmdLPop         : LPOP       keyName count?;
cmdLpos         : LPOS       keyName element (RANK rank=intValue)? (COUNT numMatches=intValue)? (MAXLEN len=intValue)?;
cmdLPush        : LPUSH      keyName stringValue+;
cmdLPushx       : LPUSHX     keyName stringValue+;
cmdLRange       : LRANGE     keyName startNum=intValue endNum=intValue;
cmdLRem         : LREM       keyName count element;
cmdLSet         : LSET       keyName index=intValue element;
cmdLTrim        : LTRIM      keyName startNum=intValue endNum=intValue;
cmdRPop         : RPOP       keyName count?;
cmdRpoplpush    : RPOPLPUSH  src=keyName dst=keyName;
cmdRPush        : RPUSH      keyName stringValue+;
cmdRPushx       : RPUSHX     keyName stringValue+;
/* ----------------------------------------------------------------------------------- Set commands */
cmdSadd         : SADD        keyName fieldName+;
cmdScard        : SCARD       keyName;
cmdSdiff        : SDIFF       keyName+;
cmdSdiffstore   : SDIFFSTORE  dst=keyName member+;
cmdSinter       : SINTER      keyName+;
cmdSinterCard   : SINTERCARD  numkeys=intValue keyName+ (LIMIT limit=intValue)?;
cmdSinterStore  : SINTERSTORE dst=keyName member+;
cmdSismember    : SISMEMBER   keyName member;
cmdSmembers     : SMEMBERS    keyName;
cmdSmismember   : SMISMEMBER  keyName fieldName+;
cmdSmove        : SMOVE       src=keyName dst=keyName member;
cmdSpop         : SPOP        keyName count?;
cmdSrandmember  : SRANDMEMBER keyName count?;
cmdSrem         : SREM        keyName fieldName+;
cmdSscan        : SSCAN       keyName cursor (MATCH pattern)? (COUNT count)?;
cmdSunion       : SUNION      keyName+;
cmdSunionstore  : SUNIONSTORE dst=keyName member+;
/* ----------------------------------------------------------------------------------- sorted set commands */
cmdBzmpop           : BZMPOP   timeout=intValue numkeys=intValue keyName+ (MIN | MAX) (COUNT count)?;
cmdBzpopmax         : BZPOPMAX keyName+ timeout=intValue;
cmdBzpopmin         : BZPOPMIN keyName+ timeout=intValue;
cmdZadd             : ZADD     keyName (NX | XX)? (GT | LT)? CH? INCR? scoreAndMemberOpt+;
scoreAndMemberOpt   : score=intValue member;
cmdZcard            : ZCARD    keyName;
cmdZcount           : ZCOUNT   keyName min max;
cmdZdiff            : ZDIFF    numkeys=intValue keyName+ WITHSCORES?;
cmdZdiffStore       : ZDIFFSTORE dst=keyName numkeys=intValue member+;
cmdZincrby          : ZINCRBY    keyName increment=intValue member;
cmdZinter           : ZINTER      numkeys=intValue keyName+ (WEIGHTS weight+)? (AGGREGATE (SUM | MIN | MAX))? WITHSCORES?;
cmdZintercard       : ZINTERCARD  numkeys=intValue keyName+ (LIMIT limit=intValue)?;
cmdZinterstore      : ZINTERSTORE dst=keyName numkeys=intValue member+ (WEIGHTS weight+)? (AGGREGATE (SUM | MIN | MAX))?;
cmdZLexCount        : ZLEXCOUNT   keyName min max;
cmdZmpop            : ZMPOP       numkeys=intValue keyName+ (MIN | MAX) (COUNT count)?;
cmdZmscore          : ZMSCORE     keyName fieldName+;
cmdZpopmax          : ZPOPMAX     keyName count?;
cmdZpopmin          : ZPOPMIN     keyName count?;
cmdZrandmember      : ZRANDMEMBER   keyName (count WITHSCORES?)?;
cmdZrange           : ZRANGE        keyName start=rangeInt end=rangeInt (BYSCORE | BYLEX)? REV? limitOpt? WITHSCORES?;
cmdZrangebylex      : ZRANGEBYLEX   keyName min max limitOpt?;
cmdZrangebyscore    : ZRANGEBYSCORE keyName min max WITHSCORES? limitOpt?;
cmdZrangestore      : ZRANGESTORE   dst=keyName src=keyName min max (BYSCORE | BYLEX)? REV? limitOpt?;
cmdZrank            : ZRANK         keyName member WITHSCORE?;
cmdZRem             : ZREM          keyName fieldName+;
cmdZRemRangeByLex   : ZREMRANGEBYLEX   keyName min max;
cmdZremrangebyrank  : ZREMRANGEBYRANK  keyName startNum=intValue endNum=intValue;
cmdZRemRangeByScore : ZREMRANGEBYSCORE keyName min max;
cmdZrevrange        : ZREVRANGE        keyName startNum=intValue endNum=intValue WITHSCORES?;
cmdZrevrangebylex   : ZREVRANGEBYLEX   keyName max min limitOpt?;
cmdZrevrangebyscore : ZREVRANGEBYSCORE keyName max min WITHSCORES? limitOpt?;
cmdZrevrank         : ZREVRANK         keyName member WITHSCORE?;
cmdZscan            : ZSCAN            keyName cursor (MATCH pattern)? (COUNT count)?;
cmdZscore           : ZSCORE           keyName member;
cmdZunion           : ZUNION           numkeys=intValue keyName+ (WEIGHTS weight+)? (AGGREGATE (SUM | MIN | MAX))? WITHSCORES?;
cmdZunionstore      : ZUNIONSTORE      dst=keyName numkeys=intValue member+ (WEIGHTS weight+)? (AGGREGATE (SUM | MIN | MAX))?;
/* ----------------------------------------------------------------------------------- script commands */
cmdScriptDebug  : SCRIPT DEBUG (YES | SYNC | NO);
cmdScriptExists : SCRIPT EXISTS sha1+;
cmdScriptFlush  : SCRIPT FLUSH (ASYNC | SYNC)?;
cmdScriptKill   : SCRIPT KILL;
cmdScriptLoad   : SCRIPT LOAD script=multiString;
cmdEval         : EVAL        script=multiString numkeys=intValue keyName* arg*;
cmdEvalRO       : EVAL_RO     script=multiString numkeys=intValue keyName* arg*;
cmdEvalsha      : EVALSHA     sha1 numkeys=intValue keyName* arg*;
cmdEvalshaRO    : EVALSHA_RO  sha1 numkeys=intValue keyName* arg*;
cmdFCall        : FCALL       sha1 numkeys=intValue keyName* arg*;
cmdFCallRO      : FCALL_RO    sha1 numkeys=intValue keyName* arg*;
cmdFunctionDel  : FUNCTION DELETE libraryName=valueAsStr;
cmdFunctionDump : FUNCTION DUMP;
cmdFunctionFlush: FUNCTION FLUSH (ASYNC | SYNC)?;
cmdFunctionKill : FUNCTION KILL;
cmdFunctionList : FUNCTION LIST (LIBRARYNAME pattern)? WITHCODE?;
cmdFunctionLoad : FUNCTION LOAD REPLACE? functionCode=stringValue;
cmdFunctionRestore: FUNCTION RESTORE serializedValue=stringValue (FLUSH | APPEND | REPLACE)?;
cmdFunctionStats: FUNCTION STATS;
/* ----------------------------------------------------------------------------------- tx commands */
cmdDiscard      : DISCARD;
cmdExec         : EXEC;
cmdMulti        : MULTI;
cmdUnwatch      : UNWATCH;
cmdWatch        : WATCH keyName+;
/* ----------------------------------------------------------------------------------- HyperLog commands */
cmdPFAdd        : PFADD   keyName element*;
cmdPFCount      : PFCOUNT keyName+;
// PFDEBUG is internal command.
cmdPFMerge      : PFMERGE dst=keyName srcKey+;
//cmdPFSelfTest   : PFSELFTEST;
/* ----------------------------------------------------------------------------------- publish commands */
cmdPSubscribe         : PSUBSCRIBE           pattern+;
cmdPublish            : PUBLISH              channel message=valueAsStr;
cmdPubSubChannels     : PUBSUB CHANNELS      pattern?;
cmdPubSubNumPat       : PUBSUB NUMPAT;
cmdPubSubNumSub       : PUBSUB NUMSUB        channel*;
cmdPubSubShardChannels: PUBSUB SHARDCHANNELS pattern?;
cmdPubSubShardNumSub  : PUBSUB SHARDNUMSUB   channel*;
cmdPunSubscribe       : PUNSUBSCRIBE         pattern*;
cmdSpublish           : SPUBLISH             channel message=valueAsStr;
cmdSSubscribe         : SSUBSCRIBE           channel*;
cmdSubscribe          : SUBSCRIBE            channel*;
cmdSunSubscribe       : SUNSUBSCRIBE         channel*;
cmdUnSubScribe        : UNSUBSCRIBE          channel*;
/* ----------------------------------------------------------------------------------- cluster commands */
cmdAsking                     : ASKING;
cmdReadonly                   : READONLY;
cmdReadWrite                  : READWRITE;
cmdClusterAddSlots            : CLUSTER ADDSLOTS slot+;
cmdClusterDelSlots            : CLUSTER DELSLOTS slot+;
cmdClusterAddSlotsRange       : CLUSTER ADDSLOTSRANGE clusterStartAndEnd+;
cmdClusterDelSlotsRange       : CLUSTER DELSLOTSRANGE clusterStartAndEnd+;
clusterStartAndEnd            : start=intValue end=intValue;
cmdClusterBumpEpoch           : CLUSTER BUMPEPOCH;
cmdClusterCountFailureReports : CLUSTER COUNT_FAILURE_REPORTS nodeId=valueAsStr;
cmdClusterCountKeysInSlot     : CLUSTER COUNTKEYSINSLOT slot;
cmdClusterFailOver            : CLUSTER FAILOVER (FORCE | TAKEOVER)?;
cmdClusterFlushSlots          : CLUSTER FLUSHSLOTS;
cmdClusterForget              : CLUSTER FORGET nodeId=valueAsStr;
cmdClusterGetKeysInSlot       : CLUSTER GETKEYSINSLOT slot count;
cmdClusterInfo                : CLUSTER INFO;
cmdClusterKeySlot             : CLUSTER KEYSLOT key;
cmdClusterLinks               : CLUSTER LINKS;
cmdClusterMeet                : CLUSTER MEET ip=HOST port clusterBusPort=intValue?;
cmdClusterMyId                : CLUSTER MYID;
cmdClusterMyShardId           : CLUSTER MYSHARDID;
cmdClusterNodes               : CLUSTER NODES;
cmdClusterReplicas            : CLUSTER REPLICAS nodeId=valueAsStr;
cmdClusterReplicate           : CLUSTER REPLICATE nodeId=valueAsStr;
cmdClusterReset               : CLUSTER RESET (HARD | SOFT)?;
cmdClusterSaveConfig          : CLUSTER SAVECONFIG;
cmdClusterSetConfigEpoch      : CLUSTER SET_CONFIG_EPOCH configEpoch=valueAsStr;
cmdClusterSetSlot             : CLUSTER SETSLOT slot (((IMPORTING | MIGRATING | NODE) nodeId=valueAsStr) | STABLE);
cmdClusterShards              : CLUSTER SHARDS;
cmdClusterSlaves              : CLUSTER SLAVES nodeId=valueAsStr;
cmdClusterSlotStats           : CLUSTER SLOT_STATS ( (SLOTSRANGE startNum=intValue endNum=intValue)
                                                   | (ORDERBY metric=(SLOT | MIGRATING | IMPORTING | STABLE) (LIMIT limit=intValue)? (ASC | DESC)?)
                              );
cmdClusterSlots               : CLUSTER SLOTS;
/* ----------------------------------------------------------------------------------- info commands */
cmdInfo         : INFO infoOpt*;
infoOpt         : SERVER | CLIENTS | MEMORY | PERSISTENCE | THREADS | STATS | REPLICATION | CPU | COMMANDSTATS
                | LATENCYSTATS | SENTINEL | CLUSTER | MODULES | KEYSPACE | ERRORSTATS | ALL | DEFAULT | EVERYTHING;
/* ----------------------------------------------------------------------------------- acl commands */
username        : key;
command         : keyWordValue;
cmdAclCat       : ACL CAT category=key?;
cmdAclDelUser   : ACL DELUSER username+;
cmdAclDryRun    : ACL DRYRUN username+ command+ arg*;
cmdAclGenPass   : ACL GENPASS (bits=intValue)?;
cmdAclGetUser   : ACL GETUSER username;
cmdAclList      : ACL LIST;
cmdAclLoad      : ACL LOAD;
cmdAclLog       : ACL LOG (count | RESET)?;
cmdAclSave      : ACL SAVE;
cmdAclSetUser   : ACL SETUSER username aclCmdRule+;
cmdAclUsers     : ACL USERS;
cmdAclWhoami    : ACL WHOAMI;
/* ----------------------------------------------------------------------------------- command commands */
cmdCommand      : COMMAND;
cmdCommandCount : COMMAND COUNT;
cmdCommandDocs  : COMMAND DOCS command*;
cmdCommandGetKeys        : COMMAND GETKEYS command arg+;
cmdCommandGetKeysAndFlags: COMMAND GETKEYSANDFLAGS command arg+;
cmdCommandInfo  : COMMAND INFO command+;
cmdCommandList  : COMMAND LIST (FILTERBY ((MODULE  moduleName=valueAsStr) | (ACLCAT category=valueAsStr) | (PATTERN pattern)))?;
/* ----------------------------------------------------------------------------------- config commands */
cmdConfigGet      : CONFIG GET pattern+;
cmdConfigSet      : CONFIG SET paramName=valueAsStr paramValue=valueAsStr;
cmdConfigResetStat: CONFIG RESETSTAT;
cmdConfigRewrite  : CONFIG REWRITE;
/* ----------------------------------------------------------------------------------- latency commands */
event              : valueAsStr;
cmdLatencyDoctor   : LATENCY DOCTOR;
cmdLatencyGraph    : LATENCY GRAPH event;
cmdLatencyHistogram: LATENCY HISTOGRAM command*;
cmdLatencyHistory  : LATENCY HISTORY event;
cmdLatencyLatest   : LATENCY LATEST;
cmdLatencyReset    : LATENCY RESET event*;
/* ----------------------------------------------------------------------------------- memory commands */
cmdMemoryDoctor    : MEMORY DOCTOR;
cmdMemoryMallocStats: MEMORY MALLOC_STATS;
cmdMemoryPurge    : MEMORY PURGE;
cmdMemoryStats    : MEMORY STATS;
cmdMemoryUsage    : MEMORY USAGE keyName (SAMPLES count)?;
/* ----------------------------------------------------------------------------------- module commands */
configKV          : CONFIG name=valueAsStr value=valueAsStr;
cmdModuleList     : MODULE LIST;
cmdModuleLoad     : MODULE LOAD path=valueAsStr arg+;
cmdModuleLoadEx   : MODULE LOADEX path=valueAsStr configKV* (ARGS arg+)?;
cmdModuleUnload   : MODULE UNLOAD name=valueAsStr;
/* ----------------------------------------------------------------------------------- control commands */
cmdBgrewriteaof : BGREWRITEAOF;
cmdBgsave       : BGSAVE SCHEDULE?;
cmdDbsize       : DBSIZE;
cmdFailover     : FAILOVER (TO host=HOST port (FORCE)?)? ABORT? (TIMEOUT milliseconds=intValue)?;
cmdFlushAll     : FLUSHALL (ASYNC | SYNC)?;
cmdFlushDB      : FLUSHDB (ASYNC | SYNC)?;
cmdLastsave     : LASTSAVE;
cmdLolwut       : LOLWUT (VERSION version=intValue)?;
cmdMonitor      : MONITOR;
cmdPSync        : PSYNC replicationId=valueAsStr offset=intValue;
//cmdReplConf     : REPLCONF;
cmdReplicaOf    : REPLICAOF ((host=HOST port) | (NO ONE));
//cmdRestoreAsking: RESTORE_ASKING keyName ttl=intValue serializedValue=stringValue REPLACE? ABSTTL? (IDLETIME seconds=intValue)? (FREQ frequency=intValue)?;
cmdRole         : ROLE;
cmdSave         : SAVE;
cmdShutdown     : SHUTDOWN (NOSAVE | SAVE)? NOW? FORCE? ABORT?;
cmdSlaveOf      : SLAVEOF ((host=HOST port) | (NO ONE));
cmdSlowlogGet   : SLOWLOG GET count?;
cmdSlowlogLen   : SLOWLOG LEN;
cmdSlowlogReset : SLOWLOG RESET;
cmdSwapDB       : SWAPDB index1=intValue index2=intValue;
cmdSync         : SYNC;
cmdTime         : TIME;
/* ----------------------------------------------------------------------------------- client commands part 1*/
clientId         : valueAsStr;
cmdClientCaching : CLIENT CACHING (YES | NO);
cmdClientGetname : CLIENT GETNAME;
cmdClientGetredir: CLIENT GETREDIR;
cmdClientID      : CLIENT ID;
cmdClientInfo    : CLIENT INFO;
cmdClientKill    : CLIENT KILL (ipPort=HOST_PORT | killBy+);
killBy           : killById | killByType | killByUser | killByAddr | killByLAddr | killBySkipme | killMaxage;
killById         : ID clientId;
killByType       : TYPE (NORMAL | MASTER | SLAVE | REPLICA | PUBSUB);
killByUser       : USER username;
killByAddr       : ADDR ipPort=HOST_PORT;
killByLAddr      : LADDR ipPort=HOST_PORT;
killBySkipme     : SKIPME (YES | NO);
killMaxage       : MAXAGE maxage=intValue;
cmdClientList    : CLIENT LIST (TYPE (NORMAL | MASTER | REPLICA | PUBSUB))?  (ID clientId+)?;
cmdClientNoEvict : CLIENT NO_EVICT (ON | OFF);
cmdClientNoTouch : CLIENT NO_TOUCH (ON | OFF);
cmdClientPause   : CLIENT PAUSE timeout=intValue (WRITE | ALL)?;
cmdClientReply   : CLIENT REPLY (ON | OFF | SKIP_);
cmdClientSetInfo : CLIENT SETINFO ((LIB_NAME libname=valueAsStr) | (LIB_VER libver=valueAsStr));
cmdClientSetname : CLIENT SETNAME connectionName=valueAsStr;
cmdClientTracking: CLIENT TRACKING (ON | OFF) (REDIRECT clientId)? prefixOpt* BCAST? OPTIN? OPTOUT? NOLOOP?;
prefixOpt : PREFIX prefix=valueAsStr;
cmdClientTrackingInfo: CLIENT TRACKINGINFO;
cmdClientUnBlock : CLIENT UNBLOCK clientId (TIMEOUT | ERROR)?;
cmdClientUnPause : CLIENT UNPAUSE;
cmdAuth   : AUTH username? password=valueAsStr;
cmdEcho   : ECHO message=stringValue;
cmdHello  : HELLO (protover=intValue (AUTH username password=valueAsStr)? (SETNAME clientname=valueAsStr)?)?;
cmdPing   : PING message=stringValue?;
cmdQuit   : QUIT;
cmdReset  : RESET;
cmdSelect : SELECT db=intValue;

cmdInst :
        // keys commands
          cmdCopy | cmdDel | cmdDump | cmdExists | cmdExpire | cmdExpireat | cmdExpireTime | cmdKeys /* | cmdMigrate */
        | cmdMove | cmdObject | cmdPersist | cmdPexpire | cmdPexpireat | cmdPExpireTime | cmdTtl | cmdPttl
        | cmdRandomkey | cmdRename | cmdRenamenx | cmdRestore | cmdScan | cmdSort | cmdSortro | cmdTouch
        | cmdType | cmdUnlink | cmdWait | cmdWaitAOF
        // string commands
        | cmdAppend | cmdDecr | cmdDecrby | cmdGet | cmdGetdel | cmdGetex | cmdGetrange | cmdGetset | cmdIncr
        | cmdIncrby | cmdIncrbyFloat | cmdLcs | cmdMget | cmdMset | cmdMsetnx | cmdSetex | cmdPSetex
        | cmdSet | cmdSetnx | cmdSetrange | cmdStrlen | cmdSubstr
        // bit commands
        | cmdBitCount | cmdBitField | cmdBitFieldRO | cmdBitOP | cmdBitPos | cmdGetbit | cmdSetbit
        // hash commands
        | cmdHdel | cmdHexists | cmdHexpire | cmdHexpireat | cmdHexpiretime | cmdHGet | cmdHGetAll | cmdHgetDel
        | cmdHgetex | cmdHincrBy | cmdHincrByFloat | cmdHKeys | cmdHLen | cmdHMget | cmdHMset | cmdHPersist
        | cmdHPexpire | cmdHPexpireAt | cmdHPexpireTime | cmdHPTTL | cmdHTTL | cmdHrandfield | cmdHscan | cmdHSet
        | cmdHSetex | cmdHsetnx | cmdHStrLen | cmdHVals
        // list commands
        | cmdBlmove | cmdLmpop | cmdBLmpop | cmdBLPop | cmdBRPop | cmdBrpoplpush | cmdLindex | cmdLinsert | cmdLlen
        | cmdLmove | cmdLPop | cmdRPop | cmdLpos | cmdLPush | cmdLPushx | cmdRPush | cmdRPushx | cmdLRange | cmdLTrim
        | cmdLRem | cmdLSet | cmdRpoplpush
        // set commands
        | cmdSadd | cmdSrem | cmdSmismember | cmdScard | cmdSmembers | cmdSdiff | cmdSdiffstore | cmdSunion | cmdSunionstore
        | cmdSinter | cmdSinterCard | cmdSinterStore | cmdSismember | cmdSmove | cmdSpop | cmdSrandmember | cmdSscan
        // sorted set commands
        | cmdBzmpop | cmdBzpopmax | cmdBzpopmin | cmdZadd | cmdZcard | cmdZcount | cmdZdiff | cmdZdiffStore
        | cmdZincrby | cmdZinter | cmdZintercard | cmdZinterstore | cmdZLexCount | cmdZmpop | cmdZmscore | cmdZpopmax
        | cmdZpopmin | cmdZrandmember | cmdZrange | cmdZrangebylex | cmdZrangebyscore | cmdZrangestore | cmdZrank
        | cmdZRem | cmdZRemRangeByLex | cmdZremrangebyrank | cmdZRemRangeByScore | cmdZrevrange | cmdZrevrangebylex
        | cmdZrevrangebyscore | cmdZrevrank | cmdZscan | cmdZscore | cmdZunion | cmdZunionstore
        // script commands
        | cmdScriptDebug | cmdScriptExists | cmdScriptFlush | cmdScriptKill | cmdScriptLoad | cmdEval | cmdEvalRO
        | cmdEvalsha | cmdEvalshaRO | cmdFCall | cmdFCallRO | cmdFunctionDel | cmdFunctionDump | cmdFunctionFlush
        | cmdFunctionKill | cmdFunctionList | cmdFunctionLoad | cmdFunctionRestore | cmdFunctionStats
        // tx commands
        | cmdDiscard | cmdExec | cmdMulti | cmdUnwatch | cmdWatch
        // HyperLog commands
        | cmdPFAdd | cmdPFCount | cmdPFMerge /*| cmdPFSelfTest*/
        // publish commands
        | cmdPSubscribe | cmdPublish | cmdPubSubChannels | cmdPubSubNumPat | cmdPubSubNumSub | cmdPubSubShardChannels
        | cmdPubSubShardNumSub | cmdPunSubscribe | cmdSpublish | cmdSSubscribe | cmdSubscribe | cmdSunSubscribe
        | cmdUnSubScribe
        // cluster commands
        | cmdAsking | cmdReadonly | cmdReadWrite | cmdClusterAddSlots | cmdClusterAddSlotsRange | cmdClusterBumpEpoch
        | cmdClusterCountFailureReports | cmdClusterCountKeysInSlot | cmdClusterDelSlots | cmdClusterDelSlotsRange
        | cmdClusterFailOver | cmdClusterFlushSlots | cmdClusterForget | cmdClusterGetKeysInSlot | cmdClusterInfo
        | cmdClusterKeySlot | cmdClusterLinks | cmdClusterMeet | cmdClusterMyId | cmdClusterMyShardId | cmdClusterNodes
        | cmdClusterReplicas | cmdClusterReplicate | cmdClusterReset | cmdClusterSaveConfig | cmdClusterSetConfigEpoch
        | cmdClusterSetSlot | cmdClusterShards | cmdClusterSlaves | cmdClusterSlotStats | cmdClusterSlots
        // info commands
        | cmdInfo
        // acl commands
        | cmdAclCat | cmdAclDelUser | cmdAclDryRun | cmdAclGenPass | cmdAclGetUser | cmdAclList | cmdAclLoad
        | cmdAclLog | cmdAclSave | cmdAclSetUser | cmdAclUsers | cmdAclWhoami
        // command commands
        | cmdCommand | cmdCommandCount | cmdCommandDocs | cmdCommandGetKeys | cmdCommandGetKeysAndFlags
        | cmdCommandInfo | cmdCommandList
        // config commands
        | cmdConfigGet | cmdConfigSet | cmdConfigResetStat | cmdConfigRewrite
        // latency commands
        | cmdLatencyDoctor | cmdLatencyGraph | cmdLatencyHistogram | cmdLatencyHistory | cmdLatencyLatest | cmdLatencyReset
        // memory commands
        | cmdMemoryDoctor | cmdMemoryMallocStats | cmdMemoryPurge | cmdMemoryStats | cmdMemoryUsage
        // module commands
        | cmdModuleList | cmdModuleLoad | cmdModuleLoadEx | cmdModuleUnload
        // control commands
        | cmdBgrewriteaof | cmdBgsave | cmdDbsize | cmdFailover | cmdFlushAll | cmdFlushDB | cmdLastsave | cmdLolwut
        | cmdMonitor | cmdPSync | /* cmdReplConf | */ cmdReplicaOf | /*cmdRestoreAsking |*/ cmdRole | cmdSave | cmdShutdown
        | cmdSlaveOf | cmdSlowlogGet | cmdSlowlogLen | cmdSlowlogReset | cmdSwapDB | cmdSync | cmdTime
        // client commands part 1
        | cmdClientCaching | cmdClientGetname | cmdClientGetredir | cmdClientID | cmdClientInfo | cmdClientKill
        | cmdClientList | cmdClientNoEvict | cmdClientNoTouch | cmdClientPause | cmdClientReply | cmdClientSetInfo
        | cmdClientSetname | cmdClientTracking | cmdClientTrackingInfo | cmdClientUnBlock | cmdClientUnPause
        // client commands part 2
        | cmdAuth | cmdEcho | cmdHello | cmdPing | cmdQuit | cmdReset | cmdSelect
        ;

/* 入口 */
rootInstSet : commands? EOF;
commands    : EOL* (cmdInst EOL*)
            | EOL* (cmdInst EOL+ commands)
            ;
