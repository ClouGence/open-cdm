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

lexer grammar RedisLexer;

channels { COMMENT }

/* skip spaces */
WS      : [ \t\f]+              -> channel(HIDDEN); // skip spaces, (空格\水平制表符\换行\回车\换页)
COMMENT1: '//' (~[\n\r])* EOL?  -> channel(COMMENT);
COMMENT2: '/*' .*? '*/'         -> channel(COMMENT);
EOL     : ('\r''\n'? | '\n');

/* key command words */
COPY        : [cC][oO][pP][yY];
DEL         : [dD][eE][lL];
DUMP        : [dD][uU][mM][pP];
EXISTS      : [eE][xX][iI][sS][tT][sS];
EXPIRE      : [eE][xX][pP][iI][rR][eE];
EXPIREAT    : [eE][xX][pP][iI][rR][eE][aA][tT];
EXPIRETIME  : [eE][xX][pP][iI][rR][eE][tT][iI][mM][eE];
KEYS        : [kK][eE][yY][sS];
MOVE        : [mM][oO][vV][eE];
OBJECT      : [oO][bB][jJ][eE][cC][tT];
PERSIST     : [pP][eE][rR][sS][iI][sS][tT];
PEXPIRE     : [pP][eE][xX][pP][iI][rR][eE];
PEXPIREAT   : [pP][eE][xX][pP][iI][rR][eE][aA][tT];
PEXPIRETIME : [pP][eE][xX][pP][iI][rR][eE][tT][iI][mM][eE];
TTL         : [tT][tT][lL];
PTTL        : [pP][tT][tT][lL];
RANDOMKEY   : [rR][aA][nN][dD][oO][mM][kK][eE][yY];
RENAME      : [rR][eE][nN][aA][mM][eE];
RENAMENX    : [rR][eE][nN][aA][mM][eE][nN][xX];
RESTORE     : [rR][eE][sS][tT][oO][rR][eE];
SCAN        : [sS][cC][aA][nN];
SORT        : [sS][oO][rR][tT];
SORT_RO     : [sS][oO][rR][tT]'_'[rR][oO];
TOUCH       : [tT][oO][uU][cC][hH];
TYPE        : [tT][yY][pP][eE];
UNLINK      : [uU][nN][lL][iI][nN][kK];
WAIT        : [wW][aA][iI][tT];
WAITAOF     : [wW][aA][iI][tT][aA][oO][fF];

/* string command words */
APPEND      : [aA][pP][pP][eE][nN][dD];
DECR        : [dD][eE][cC][rR];
DECRBY      : [dD][eE][cC][rR][bB][yY];
GET         : [gG][eE][tT];
GETDEL      : [gG][eE][tT][dD][eE][lL];
GETEX       : [gG][eE][tT][eE][xX];
GETRANGE    : [gG][eE][tT][rR][aA][nN][gG][eE];
GETSET      : [gG][eE][tT][sS][eE][tT];
INCR        : [iI][nN][cC][rR];
INCRBY      : [iI][nN][cC][rR][bB][yY];
INCRBYFLOAT : [iI][nN][cC][rR][bB][yY][fF][lL][oO][aA][tT];
LCS         : [lL][cC][sS];
MGET        : [mM][gG][eE][tT];
MSET        : [mM][sS][eE][tT];
MSETNX      : [mM][sS][eE][tT][nN][xX];
SETEX       : [sS][eE][tT][eE][xX];
PSETEX      : [pP][sS][eE][tT][eE][xX];
SET         : [sS][eE][tT];
SETNX       : [sS][eE][tT][nN][xX];
SETRANGE    : [sS][eE][tT][rR][aA][nN][gG][eE];
STRLEN      : [sS][tT][rR][lL][eE][nN];
SUBSTR      : [sS][uU][bB][sS][tT][rR];

/* bit command words */
BITCOUNT    : [bB][iI][tT][cC][oO][uU][nN][tT];
BITFIELD_RO : [bB][iI][tT][fF][iI][eE][lL][dD]'_'[rR][oO];
BITFIELD    : [bB][iI][tT][fF][iI][eE][lL][dD];
BITOP       : [bB][iI][tT][oO][pP];
BITPOS      : [bB][iI][tT][pP][oO][sS];
GETBIT      : [gG][eE][tT][bB][iI][tT];
SETBIT      : [sS][eE][tT][bB][iI][tT];
OVERFLOW    : [oO][vV][eE][rR][fF][lL][oO][wW];
WRAP        : [wW][rR][aA][pP];
SAT         : [sS][aA][tT];
FAIL        : [fF][aA][iI][lL];

/* hash command words */
HDEL        : [hH][dD][eE][lL];
HEXISTS     : [hH][eE][xX][iI][sS][tT][sS];
HEXPIRE     : [hH][eE][xX][pP][iI][rR][eE];
HEXPIREAT   : [hH][eE][xX][pP][iI][rR][eE][aA][tT];
HEXPIRETIME : [hH][eE][xX][pP][iI][rR][eE][tT][iI][mM][eE];
HGET        : [hH][gG][eE][tT];
HGETALL     : [hH][gG][eE][tT][aA][lL][lL];
HGETDEL     : [hH][gG][eE][tT][dD][eE][lL];
HGETEX      : [hH][gG][eE][tT][eE][xX];
HINCRBY     : [hH][iI][nN][cC][rR][bB][yY];
HINCRBYFLOAT: [hH][iI][nN][cC][rR][bB][yY][fF][lL][oO][aA][tT];
HKEYS       : [hH][kK][eE][yY][sS];
HLEN        : [hH][lL][eE][nN];
HMGET       : [hH][mM][gG][eE][tT];
HMSET       : [hH][mM][sS][eE][tT];
HPERSIST    : [hH][pP][eE][rR][sS][iI][sS][tT];
HPEXPIRE    : [hH][pP][eE][xX][pP][iI][rR][eE];
HPEXPIREAT  : [hH][pP][eE][xX][pP][iI][rR][eE][aA][tT];
HPEXPIRETIME: [hH][pP][eE][xX][pP][iI][rR][eE][tT][iI][mM][eE];
HPTTL       : [hH][pP][tT][tT][lL];
HTTL        : [hH][tT][tT][lL];
HRANDFIELD  : [hH][rR][aA][nN][dD][fF][iI][eE][lL][dD];
HSCAN       : [hH][sS][cC][aA][nN];
HSET        : [hH][sS][eE][tT];
HSETEX      : [hH][sS][eE][tT][eE][xX];
HSETNX      : [hH][sS][eE][tT][nN][xX];
HSTRLEN     : [hH][sS][tT][rR][lL][eE][nN];
HVALS       : [hH][vV][aA][lL][sS];

/* list command words */
BLMOVE      : [bB][lL][mM][oO][vV][eE];
BLMPOP      : [bB][lL][mM][pP][oO][pP];
BLPOP       : [bB][lL][pP][oO][pP];
BRPOP       : [bB][rR][pP][oO][pP];
BRPOPLPUSH  : [bB][rR][pP][oO][pP][lL][pP][uU][sS][hH];
LINDEX      : [lL][iI][nN][dD][eE][xX];
LINSERT     : [lL][iI][nN][sS][eE][rR][tT];
LLEN        : [lL][lL][eE][nN];
LMOVE       : [lL][mM][oO][vV][eE];
LMPOP       : [lL][mM][pP][oO][pP];
LPOP        : [lL][pP][oO][pP];
LPOS        : [lL][pP][oO][sS];
LPUSH       : [lL][pP][uU][sS][hH];
LPUSHX      : [lL][pP][uU][sS][hH][xX];
LRANGE      : [lL][rR][aA][nN][gG][eE];
LREM        : [lL][rR][eE][mM];
LSET        : [lL][sS][eE][tT];
LTRIM       : [lL][tT][rR][iI][mM];
RPOP        : [rR][pP][oO][pP];
RPOPLPUSH   : [rR][pP][oO][pP][lL][pP][uU][sS][hH];
RPUSH       : [rR][pP][uU][sS][hH];
RPUSHX      : [rR][pP][uU][sS][hH][xX];

/* set command words */
SADD        : [sS][aA][dD][dD];
SCARD       : [sS][cC][aA][rR][dD];
SDIFF       : [sS][dD][iI][fF][fF];
SDIFFSTORE  : [sS][dD][iI][fF][fF][sS][tT][oO][rR][eE];
SINTER      : [sS][iI][nN][tT][eE][rR];
SINTERCARD  : [sS][iI][nN][tT][eE][rR][cC][aA][rR][dD];
SINTERSTORE : [sS][iI][nN][tT][eE][rR][sS][tT][oO][rR][eE];
SISMEMBER   : [sS][iI][sS][mM][eE][mM][bB][eE][rR];
SMEMBERS    : [sS][mM][eE][mM][bB][eE][rR][sS];
SMISMEMBER  : [sS][mM][iI][sS][mM][eE][mM][bB][eE][rR];
SMOVE       : [sS][mM][oO][vV][eE];
SPOP        : [sS][pP][oO][pP];
SRANDMEMBER : [sS][rR][aA][nN][dD][mM][eE][mM][bB][eE][rR];
SREM        : [sS][rR][eE][mM];
SSCAN       : [sS][sS][cC][aA][nN];
SUNION      : [sS][uU][nN][iI][oO][nN];
SUNIONSTORE : [sS][uU][nN][iI][oO][nN][sS][tT][oO][rR][eE];

/* sorted set command words */
BZMPOP      : [bB][zZ][mM][pP][oO][pP];
BZPOPMAX    : [bB][zZ][pP][oO][pP][mM][aA][xX];
BZPOPMIN    : [bB][zZ][pP][oO][pP][mM][iI][nN];
ZADD        : [zZ][aA][dD][dD];
ZCARD       : [zZ][cC][aA][rR][dD];
ZCOUNT      : [zZ][cC][oO][uU][nN][tT];
ZDIFF       : [zZ][dD][iI][fF][fF];
ZDIFFSTORE  : [zZ][dD][iI][fF][fF][sS][tT][oO][rR][eE];
ZINCRBY     : [zZ][iI][nN][cC][rR][bB][yY];
ZINTER      : [zZ][iI][nN][tT][eE][rR];
ZINTERCARD  : [zZ][iI][nN][tT][eE][rR][cC][aA][rR][dD];
ZINTERSTORE : [zZ][iI][nN][tT][eE][rR][sS][tT][oO][rR][eE];
ZLEXCOUNT   : [zZ][lL][eE][xX][cC][oO][uU][nN][tT];
ZMPOP       : [zZ][mM][pP][oO][pP];
ZMSCORE     : [zZ][mM][sS][cC][oO][rR][eE];
ZPOPMAX     : [zZ][pP][oO][pP][mM][aA][xX];
ZPOPMIN     : [zZ][pP][oO][pP][mM][iI][nN];
ZRANDMEMBER : [zZ][rR][aA][nN][dD][mM][eE][mM][bB][eE][rR];
ZRANGE      : [zZ][rR][aA][nN][gG][eE];
ZRANGEBYLEX : [zZ][rR][aA][nN][gG][eE][bB][yY][lL][eE][xX];
ZRANGEBYSCORE    : [zZ][rR][aA][nN][gG][eE][bB][yY][sS][cC][oO][rR][eE];
ZRANGESTORE : [zZ][rR][aA][nN][gG][eE][sS][tT][oO][rR][eE];
ZRANK       : [zZ][rR][aA][nN][kK];
ZREM        : [zZ][rR][eE][mM];
ZREMRANGEBYLEX   : [zZ][rR][eE][mM][rR][aA][nN][gG][eE][bB][yY][lL][eE][xX];
ZREMRANGEBYRANK  : [zZ][rR][eE][mM][rR][aA][nN][gG][eE][bB][yY][rR][aA][nN][kK];
ZREMRANGEBYSCORE : [zZ][rR][eE][mM][rR][aA][nN][gG][eE][bB][yY][sS][cC][oO][rR][eE];
ZREVRANGE        : [zZ][rR][eE][vV][rR][aA][nN][gG][eE];
ZREVRANGEBYLEX   : [zZ][rR][eE][vV][rR][aA][nN][gG][eE][bB][yY][lL][eE][xX];
ZREVRANGEBYSCORE : [zZ][rR][eE][vV][rR][aA][nN][gG][eE][bB][yY][sS][cC][oO][rR][eE];
ZREVRANK    : [zZ][rR][eE][vV][rR][aA][nN][kK];
ZSCAN       : [zZ][sS][cC][aA][nN];
ZSCORE      : [zZ][sS][cC][oO][rR][eE];
ZUNION      : [zZ][uU][nN][iI][oO][nN];
ZUNIONSTORE : [zZ][uU][nN][iI][oO][nN][sS][tT][oO][rR][eE];

/* script commands words */
SCRIPT      : [sS][cC][rR][iI][pP][tT];
EVAL        : [eE][vV][aA][lL];
EVAL_RO     : [eE][vV][aA][lL]'_'[rR][oO];
EVALSHA     : [eE][vV][aA][lL][sS][hH][aA];
EVALSHA_RO  : [eE][vV][aA][lL][sS][hH][aA]'_'[rR][oO];
FCALL       : [fF][cC][aA][lL][lL];
FCALL_RO    : [fF][cC][aA][lL][lL]'_'[rR][oO];
FUNCTION    : [fF][uU][nN][cC][tT][iI][oO][nN];

/* tx commands words */
DISCARD     : [dD][iI][sS][cC][aA][rR][dD];
EXEC        : [eE][xX][eE][cC];
MULTI       : [mM][uU][lL][tT][iI];
UNWATCH     : [uU][nN][wW][aA][tT][cC][hH];
WATCH       : [wW][aA][tT][cC][hH];

/* HyperLog commands words */
PFADD       : [pP][fF][aA][dD][dD];
PFCOUNT     : [pP][fF][cC][oO][uU][nN][tT];
PFMERGE     : [pP][fF][mM][eE][rR][gG][eE];
//PFSELFTEST  : [pP][fF][sS][eE][lL][fF][tT][eE][sS][tT];

/* publish commands words */
PSUBSCRIBE    : [pP][sS][uU][bB][sS][cC][rR][iI][bB][eE];
PUBLISH       : [pP][uU][bB][lL][iI][sS][hH];
PUBSUB        : [pP][uU][bB][sS][uU][bB];
CHANNELS      : [cC][hH][aA][nN][nN][eE][lL][sS];
NUMPAT        : [nN][uU][mM][pP][aA][tT];
NUMSUB        : [nN][uU][mM][sS][uU][bB];
SHARDCHANNELS : [sS][hH][aA][rR][dD][cC][hH][aA][nN][nN][eE][lL][sS];
SHARDNUMSUB   : [sS][hH][aA][rR][dD][nN][uU][mM][sS][uU][bB];
PUNSUBSCRIBE  : [pP][uU][nN][sS][uU][bB][sS][cC][rR][iI][bB][eE];
SPUBLISH      : [sS][pP][uU][bB][lL][iI][sS][hH];
SSUBSCRIBE    : [sS][sS][uU][bB][sS][cC][rR][iI][bB][eE];
SUBSCRIBE     : [sS][uU][bB][sS][cC][rR][iI][bB][eE];
SUNSUBSCRIBE  : [sS][uU][nN][sS][uU][bB][sS][cC][rR][iI][bB][eE];
UNSUBSCRIBE   : [uU][nN][sS][uU][bB][sS][cC][rR][iI][bB][eE];

/* cluster command words */
ASKING                : [aA][sS][kK][iI][nN][gG];
CLUSTER               : [cC][lL][uU][sS][tT][eE][rR];
ADDSLOTS              : [aA][dD][dD][sS][lL][oO][tT][sS];
ADDSLOTSRANGE         : [aA][dD][dD][sS][lL][oO][tT][sS][rR][aA][nN][gG][eE];
BUMPEPOCH             : [bB][uU][mM][pP][eE][pP][oO][cC][hH];
COUNT_FAILURE_REPORTS : [cC][oO][uU][nN][tT]'-'[fF][aA][iI][lL][uU][rR][eE]'-'[rR][eE][pP][oO][rR][tT][sS];
COUNTKEYSINSLOT       : [cC][oO][uU][nN][tT][kK][eE][yY][sS][iI][nN][sS][lL][oO][tT];
DELSLOTS              : [dD][eE][lL][sS][lL][oO][tT][sS];
DELSLOTSRANGE         : [dD][eE][lL][sS][lL][oO][tT][sS][rR][aA][nN][gG][eE];
FORGET                : [fF][oO][rR][gG][eE][tT];
GETKEYSINSLOT         : [gG][eE][tT][kK][eE][yY][sS][iI][nN][sS][lL][oO][tT];
KEYSLOT               : [kK][eE][yY][sS][lL][oO][tT];
LINKS                 : [lL][iI][nN][kK][sS];
MEET                  : [mM][eE][eE][tT];
MYID                  : [mM][yY][iI][dD];
MYSHARDID             : [mM][yY][sS][hH][aA][rR][dD][iI][dD];
NODES                 : [nN][oO][dD][eE][sS];
REPLICAS              : [rR][eE][pP][lL][iI][cC][aA][sS];
REPLICATE             : [rR][eE][pP][lL][iI][cC][aA][tT][eE];
RESET                 : [rR][eE][sS][eE][tT];
SAVECONFIG            : [sS][aA][vV][eE][cC][oO][nN][fF][iI][gG];
SET_CONFIG_EPOCH      : [sS][eE][tT]'-'[cC][oO][nN][fF][iI][gG]'-'[eE][pP][oO][cC][hH];
SETSLOT               : [sS][eE][tT][sS][lL][oO][tT];
SHARDS                : [sS][hH][aA][rR][dD][sS];
SLAVES                : [sS][lL][aA][vV][eE][sS];
SLOT_STATS            : [sS][lL][oO][tT]'-'[sS][tT][aA][tT][sS];
SLOTS                 : [sS][lL][oO][tT][sS];
READONLY              : [rR][eE][aA][dD][oO][nN][lL][yY];
READWRITE             : [rR][eE][aA][dD][wW][rR][iI][tT][eE];

/* info command words */
INFO         : [iI][nN][fF][oO];
SERVER       : [sS][eE][rR][vV][eE][rR];
CLIENTS      : [cC][lL][iI][eE][nN][tT][sS];
PERSISTENCE  : [pP][eE][rR][sS][iI][sS][tT][eE][nN][cC][eE];
THREADS      : [tT][hH][rR][eE][aA][dD][sS];
STATS        : [sS][tT][aA][tT][sS];
REPLICATION  : [rR][eE][pP][lL][iI][cC][aA][tT][iI][oO][nN];
CPU          : [cC][pP][uU];
COMMANDSTATS : [cC][oO][mM][mM][aA][nN][dD][sS][tT][aA][tT][sS];
LATENCYSTATS : [lL][aA][tT][eE][nN][cC][yY][sS][tT][aA][tT][sS];
SENTINEL     : [sS][eE][nN][tT][iI][nN][eE][lL];
MODULES      : [mM][oO][dD][uU][lL][eE][sS];
KEYSPACE     : [kK][eE][yY][sS][pP][aA][cC][eE];
ERRORSTATS   : [eE][rR][rR][oO][rR][sS][tT][aA][tT][sS];
ALL          : [aA][lL][lL];
DEFAULT      : [dD][eE][fF][aA][uU][lL][tT];
EVERYTHING   : [eE][vV][eE][rR][yY][tT][hH][iI][nN][gG];

/* acl command words */
ACL         : [aA][cC][lL];
CAT         : [cC][aA][tT];
DELUSER     : [dD][eE][lL][uU][sS][eE][rR];
DRYRUN      : [dD][rR][yY][rR][uU][nN];
GENPASS     : [gG][eE][nN][pP][aA][sS][sS];
GETUSER     : [gG][eE][tT][uU][sS][eE][rR];
LIST        : [lL][iI][sS][tT];
LOAD        : [lL][oO][aA][dD];
LOG         : [lL][oO][gG];
SETUSER     : [sS][eE][tT][uU][sS][eE][rR];
USERS       : [uU][sS][eE][rR][sS];
WHOAMI      : [wW][hH][oO][aA][mM][iI];

/* command command words */
COMMAND     : [cC][oO][mM][mM][aA][nN][dD];
COUNT       : [cC][oO][uU][nN][tT];
DOCS        : [dD][oO][cC][sS];
GETKEYS     : [gG][eE][tT][kK][eE][yY][sS];
GETKEYSANDFLAGS : [gG][eE][tT][kK][eE][yY][sS][aA][nN][dD][fF][lL][aA][gG][sS];
FILTERBY    : [fF][iI][lL][tT][eE][rR][bB][yY];
ACLCAT      : [aA][cC][lL][cC][aA][tT];
PATTERN     : [pP][aA][tT][tT][eE][rR][nN];

/* config command words */
CONFIG      : [cC][oO][nN][fF][iI][gG];
RESETSTAT   : [rR][eE][sS][eE][tT][sS][tT][aA][tT];
REWRITE     : [rR][eE][wW][rR][iI][tT][eE];

/* latency command words */
LATENCY     : [lL][aA][tT][eE][nN][cC][yY];
DOCTOR      : [dD][oO][cC][tT][oO][rR];
GRAPH       : [gG][rR][aA][pP][hH];
HISTOGRAM   : [hH][iI][sS][tT][oO][gG][rR][aA][mM];
HISTORY     : [hH][iI][sS][tT][oO][rR][yY];
LATEST      : [lL][aA][tT][eE][sS][tT];

/* memory command words */
MEMORY      : [mM][eE][mM][oO][rR][yY];
MALLOC_STATS: [mM][aA][lL][lL][oO][cC]'-'[sS][tT][aA][tT][sS];
PURGE       : [pP][uU][rR][gG][eE];
USAGE       : [uU][sS][aA][gG][eE];
SAMPLES     : [sS][aA][mM][pP][lL][eE][sS];

/* module command words */
MODULE      : [mM][oO][dD][uU][lL][eE];
LOADEX      : [lL][oO][aA][dD][eE][xX];
UNLOAD      : [uU][nN][lL][oO][aA][dD];
ARGS        : [aA][rR][gG][sS];

/* control command words */
BGREWRITEAOF: [bB][gG][rR][eE][wW][rR][iI][tT][eE][aA][oO][fF];
BGSAVE      : [bB][gG][sS][aA][vV][eE];
DBSIZE      : [dD][bB][sS][iI][zZ][eE];
FAILOVER    : [fF][aA][iI][lL][oO][vV][eE][rR];
FLUSHALL    : [fF][lL][uU][sS][hH][aA][lL][lL];
FLUSHDB     : [fF][lL][uU][sS][hH][dD][bB];
LASTSAVE    : [lL][aA][sS][tT][sS][aA][vV][eE];
LOLWUT      : [lL][oO][lL][wW][uU][tT];
MONITOR     : [mM][oO][nN][iI][tT][oO][rR];
PSYNC       : [pP][sS][yY][nN][cC];
//REPLCONF    : [rR][eE][pP][lL][cC][oO][nN][fF];
REPLICAOF   : [rR][eE][pP][lL][iI][cC][aA][oO][fF];
//RESTORE_ASKING: [rR][eE][sS][tT][oO][rR][eE]'-'[aA][sS][kK][iI][nN][gG];
ROLE        : [rR][oO][lL][eE];
SAVE        : [sS][aA][vV][eE];
SHUTDOWN    : [sS][hH][uU][tT][dD][oO][wW][nN];
SLAVEOF     : [sS][lL][aA][vV][eE][oO][fF];
SLOWLOG     : [sS][lL][oO][wW][lL][oO][gG];
SWAPDB      : [sS][wW][aA][pP][dD][bB];
SYNC        : [sS][yY][nN][cC];
TIME        : [tT][iI][mM][eE];

/* client1 command words */
CLIENT      : [cC][lL][iI][eE][nN][tT];
CACHING     : [cC][aA][cC][hH][iI][nN][gG];
GETNAME     : [gG][eE][tT][nN][aA][mM][eE];
GETREDIR    : [gG][eE][tT][rR][eE][dD][iI][rR];
ID          : [iI][dD];
KILL        : [kK][iI][lL][lL];
NO_EVICT    : [nN][oO]'-'[eE][vV][iI][cC][tT];
NO_TOUCH    : [nN][oO]'-'[tT][oO][uU][cC][hH];
PAUSE       : [pP][aA][uU][sS][eE];
REPLY       : [rR][eE][pP][lL][yY];
SETINFO     : [sS][eE][tT][iI][nN][fF][oO];
SETNAME     : [sS][eE][tT][nN][aA][mM][eE];
TRACKING    : [tT][rR][aA][cC][kK][iI][nN][gG];
TRACKINGINFO: [tT][rR][aA][cC][kK][iI][nN][gG][iI][nN][fF][oO];
UNBLOCK     : [uU][nN][bB][lL][oO][cC][kK];
UNPAUSE     : [uU][nN][pP][aA][uU][sS][eE];
LIB_NAME    : [lL][iI][bB]'-'[nN][aA][mM][eE];
LIB_VER     : [lL][iI][bB]'-'[vV][eE][rR];

/* client2 command words */
AUTH        : [aA][uU][tT][hH];
ECHO        : [eE][cC][hH][oO];
HELLO       : [hH][eE][lL][lL][oO];
PING        : [pP][iI][nN][gG];
QUIT        : [qQ][uU][iI][tT];
SELECT      : [sS][eE][lL][eE][cC][tT];

/* token words */
NORMAL      : [nN][oO][rR][mM][aA][lL];
MASTER      : [mM][aA][sS][tT][eE][rR];
SLAVE       : [sS][lL][aA][vV][eE];
REPLICA     : [rR][eE][pP][lL][iI][cC][aA];
FLUSHSLOTS  : [fF][lL][uU][sS][hH][sS][lL][oO][tT][sS];
FORCE       : [fF][oO][rR][cC][eE];
TAKEOVER    : [tT][aA][kK][eE][oO][vV][eE][rR];
NODE        : [nN][oO][dD][eE];
HARD        : [hH][aA][rR][dD];
SOFT        : [sS][oO][fF][tT];
IMPORTING   : [iI][mM][pP][oO][rR][tT][iI][nN][gG];
MIGRATING   : [mM][iI][gG][rR][aA][tT][iI][nN][gG];
STABLE      : [sS][tT][aA][bB][lL][eE];
USER        : [uU][sS][eE][rR];
ADDR        : [aA][dD][dD][rR];
LADDR       : [lL][aA][dD][dD][rR];
SKIPME      : [sS][kK][iI][pP][mM][eE];
SKIP_       : [sS][kK][iI][pP];
MAXAGE      : [mM][aA][xX][aA][gG][eE];
ENCODING    : [eE][nN][cC][oO][dD][iI][nN][gG];
IDLETIME    : [iI][dD][lL][eE][tT][iI][mM][eE];
REFCOUNT    : [rR][eE][fF][cC][oO][uU][nN][tT];
ABSTTL      : [aA][bB][sS][tT][tT][lL];
REPLACE     : [rR][eE][pP][lL][aA][cC][eE];
MINMATCHLEN : [mM][iI][nN][mM][aA][tT][cC][hH][lL][eE][nN];
WITHMATCHLEN: [wW][iI][tT][hH][mM][aA][tT][cC][hH][lL][eE][nN];
KEEPTTL     : [kK][eE][eE][pP][tT][tT][lL];
WITHSCORE   : [wW][iI][tT][hH][sS][cC][oO][rR][eE];
WITHSCORES  : [wW][iI][tT][hH][sS][cC][oO][rR][eE][sS];
SLOTSRANGE  : [sS][lL][oO][tT][sS][rR][aA][nN][gG][eE];
ORDERBY     : [oO][rR][dD][eE][rR][bB][yY];
SLOT        : [sS][lL][oO][tT];
TO          : [tT][oO];
ABORT       : [aA][bB][oO][rR][tT];
TIMEOUT     : [tT][iI][mM][eE][oO][uU][tT];
VERSION     : [vV][eE][rR][sS][iI][oO][nN];
NOW         : [nN][oO][wW];
REDIRECT    : [rR][eE][dD][iI][rR][eE][cC][tT];
BCAST       : [bB][cC][aA][sS][tT];
OPTIN       : [oO][pP][tT][iI][nN];
OPTOUT      : [oO][pP][tT][oO][uU][tT];
NOLOOP      : [nN][oO][lL][oO][oO][pP];
PREFIX      : [pP][rR][eE][fF][iI][xX];
ERROR       : [eE][rR][rR][oO][rR];
LIMIT       : [lL][iI][mM][iI][tT];
BEFORE      : [bB][eE][fF][oO][rR][eE];
AFTER       : [aA][fF][tT][eE][rR];
FLUSH       : [fF][lL][uU][sS][hH];
NOSAVE      : [nN][oO][sS][aA][vV][eE];
SEGFAULT    : [sS][eE][gG][fF][aA][uU][lL][tT];
WITHVALUES  : [wW][iI][tT][hH][vV][aA][lL][uU][eE][sS];
MATCH       : [mM][aA][tT][cC][hH];
WEIGHTS     : [wW][eE][iI][gG][hH][tT][sS];
AGGREGATE   : [aA][gG][gG][rR][eE][gG][aA][tT][eE];
SUM         : [sS][uU][mM];
BYSCORE     : [bB][yY][sS][cC][oO][rR][eE];
BYLEX       : [bB][yY][lL][eE][xX];
REV         : [rR][eE][vV];
LEFT        : [lL][eE][fF][tT];
RIGHT       : [rR][iI][gG][hH][tT];
RANK        : [rR][aA][nN][kK];
MAXLEN      : [mM][aA][xX][lL][eE][nN];
ASYNC       : [aA][sS][yY][nN][cC];
BIT         : [bB][iI][tT];
BYTE        : [bB][yY][tT][eE];
AND         : [aA][nN][dD];
OR          : [oO][rR];
XOR         : [xX][oO][rR];
NOT         : [nN][oO][tT];
DIFF        : [dD][iI][fF][fF];
DIFF1       : [dD][iI][fF][fF]'1';
ANDOR       : [aA][nN][dD][oO][rR];
ONE         : [oO][nN][eE];
ON          : [oO][nN];
OFF         : [oO][fF][fF];
WRITE       : [wW][rR][iI][tT][eE];
FIELDS      : [fF][iI][eE][lL][dD][sS];
NOVALUES    : [nN][oO][vV][aA][lL][uU][eE][sS];
FNX         : [fF][nN][xX];
FXX         : [fF][xX][xX];
DB          : [dD][bB];
NX          : [nN][xX];
XX          : [xX][xX];
GT          : [gG][tT];
LT          : [lL][tT];
FREQ        : [fF][rR][eE][qQ];
BY          : [bB][yY];
ASC         : [aA][sS][cC];
DESC        : [dD][eE][sS][cC];
ALPHA       : [aA][lL][pP][hH][aA];
STORE       : [sS][tT][oO][rR][eE];
EX          : [eE][xX];
PX          : [pP][xX];
EXAT        : [eE][xX][aA][tT];
PXAT        : [pP][xX][aA][tT];
LEN         : [lL][eE][nN];
IDX         : [iI][dD][xX];
MIN         : [mM][iI][nN];
MAX         : [mM][aA][xX];
CH          : [cC][hH];
YES         : [yY][eE][sS];
NO          : [nN][oO];
DEBUG       : [dD][eE][bB][uU][gG];
DELETE      : [dD][eE][lL][eE][tT][eE];
LIBRARYNAME : [lL][iI][bB][rR][aA][rR][yY][nN][aA][mM][eE];
WITHCODE    : [wW][iI][tT][hH][cC][oO][dD][eE];
SCHEDULE    : [sS][cC][hH][eE][dD][uU][lL][eE];
ALLKEYS        : [aA][lL][lL][kK][eE][yY][sS];
RESETKEYS      : [rR][eE][sS][eE][tT][kK][eE][yY][sS];
ALLCHANNELS    : [aA][lL][lL][cC][hH][aA][nN][nN][eE][lL][sS];
RESETCHANNELS  : [rR][eE][sS][eE][tT][cC][hH][aA][nN][nN][eE][lL][sS];
ALLCOMMANDS    : [aA][lL][lL][cC][oO][mM][mM][aA][nN][dD][sS];
NOCOMMANDS     : [nN][oO][cC][oO][mM][mM][aA][nN][dD][sS];
CLEARSELECTORS : [cC][lL][eE][aA][rR][sS][eE][lL][eE][cC][tT][oO][rR][sS];
NOPASS         : [nN][oO][pP][aA][sS][sS];

/* 数字 */
BIT_VALUE       : [01];                                 // Bit 值
HEX_NUM         : '0' [xX] [0-9a-fA-F]+;                // 十六进制：0x12345
OCT_NUM         : '0' [oO] [0-7]+;                      // 八 进 制：0o1234567
BIT_NUM         : '0' [bB] BIT_VALUE+;                  // 二 进 制：0b01010101100
INTEGER_NUM     : '-'? [0-9]+;                          // 十进制数：-0000234 or 123
DECIMAL_NUM     : '-'? (([0-9]* '.' [0-9]+) | [1-9]+)   // 浮点数
                  ([eE] [+-]? [1-9][0-9]*)?;            // 科学计数法
N_INF           : '-'[iI][nN][fF];
S_INF           : '+'[iI][nN][fF];
RANGE_NUM       : [+-]
                | (  ('('|'['|')'|']')?
                     ([0-9a-fA-F])+
                     ('('|'['|')'|']')?
                  );
ARG             : '?';
SEMI            : ';';

/* 字符串 */
//MULTI_STRING    : '"""' TRANS* '"""'
//                | '\'\'\'' TRANS* '\'\'\''
//                ;
//MULTI_STRING    : '""""""' | MULTI2;
//fragment MULTI2 : '"""' -> pushMode(EXT_MULTI2);
OFFSET          : '#' INTEGER_NUM;
STRING          : '""'   | ('"' (~["\r\n] | TRANS)* '"')
                | '\'\'' | '\'' (~['\r\n] | TRANS)* '\''
                ;
fragment TRANS  : '\\' (['"\\/bfnrt] | UNICODE);
fragment UNICODE: 'u' HEX HEX HEX HEX;
fragment HEX    : [0-9a-fA-F];

/* command token words */
HOST        : IPT '.' IPT '.' IPT '.' IPT ;
HOST_PORT   : HOST ':' INTEGER_NUM;
fragment IPT: [0-9][0-9]?[0-9]?;

NAME_TOKEN  : (~[? \r\n\\[\]{}.] | TRANS)+;

PATTERN_STRING  : PATTERN_TOKEN+ | (PATTERN_TOKEN+ '->' PATTERN_TOKEN+);
fragment  PATTERN_TOKEN: ~(' ' | '\t' | '\'' | '"' | '\n' | '\r') | TRANS;

ACL_RULE_PATTERN : ('~' | '%R~' | '%W~' | '%RW~' | '&') PATTERN_STRING;
ACL_RULE_PRE_CMD : ('+' | '-') '@'?;

/* 扩展代码块 */
//mode EXT_MULTI2;
//CLOS_TAG        : '"""' -> popMode;
//BODY            : (CHAR1 | CHAR2)+;
//fragment CHAR1  : ~["]+;
//fragment CHAR2  : '"' | '""';