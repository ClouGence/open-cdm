import { ATN } from "antlr4ts/atn/ATN";
import { FailedPredicateException } from "antlr4ts/FailedPredicateException";
import { Parser } from "antlr4ts/Parser";
import { ParserRuleContext } from "antlr4ts/ParserRuleContext";
import { RuleContext } from "antlr4ts/RuleContext";
import { TerminalNode } from "antlr4ts/tree/TerminalNode";
import { Token } from "antlr4ts/Token";
import { TokenStream } from "antlr4ts/TokenStream";
import { Vocabulary } from "antlr4ts/Vocabulary";
import { MySqlParserListener } from "./MySqlParserListener";
import { MySqlParserVisitor } from "./MySqlParserVisitor";
export declare class MySqlParser extends Parser {
    static readonly SPACE = 1;
    static readonly SPEC_MYSQL_COMMENT = 2;
    static readonly COMMENT_INPUT = 3;
    static readonly LINE_COMMENT = 4;
    static readonly KW_ACTIVE = 5;
    static readonly KW_ADD = 6;
    static readonly KW_ALL = 7;
    static readonly KW_ALTER = 8;
    static readonly KW_ALWAYS = 9;
    static readonly KW_ANALYZE = 10;
    static readonly KW_AND = 11;
    static readonly KW_ARRAY = 12;
    static readonly KW_AS = 13;
    static readonly KW_ASC = 14;
    static readonly KW_ATTRIBUTE = 15;
    static readonly KW_BEFORE = 16;
    static readonly KW_BETWEEN = 17;
    static readonly KW_BOTH = 18;
    static readonly KW_BUCKETS = 19;
    static readonly KW_BY = 20;
    static readonly KW_CALL = 21;
    static readonly KW_CASCADE = 22;
    static readonly KW_CASE = 23;
    static readonly KW_CAST = 24;
    static readonly KW_CHANGE = 25;
    static readonly KW_CHARACTER = 26;
    static readonly KW_CHECK = 27;
    static readonly KW_COLLATE = 28;
    static readonly KW_COLUMN = 29;
    static readonly KW_CONDITION = 30;
    static readonly KW_CONSTRAINT = 31;
    static readonly KW_CONTINUE = 32;
    static readonly KW_CONVERT = 33;
    static readonly KW_CREATE = 34;
    static readonly KW_CROSS = 35;
    static readonly KW_CURRENT = 36;
    static readonly KW_CURRENT_USER = 37;
    static readonly KW_CURSOR = 38;
    static readonly KW_DATABASE = 39;
    static readonly KW_DATABASES = 40;
    static readonly KW_DECLARE = 41;
    static readonly KW_DEFAULT = 42;
    static readonly KW_DELAYED = 43;
    static readonly KW_DELETE = 44;
    static readonly KW_DESC = 45;
    static readonly KW_DESCRIBE = 46;
    static readonly KW_DETERMINISTIC = 47;
    static readonly KW_DIAGNOSTICS = 48;
    static readonly KW_DISTINCT = 49;
    static readonly KW_DISTINCTROW = 50;
    static readonly KW_DROP = 51;
    static readonly KW_EACH = 52;
    static readonly KW_ELSE = 53;
    static readonly KW_ELSEIF = 54;
    static readonly KW_EMPTY = 55;
    static readonly KW_ENCLOSED = 56;
    static readonly KW_ENFORCED = 57;
    static readonly KW_ESCAPED = 58;
    static readonly KW_EXCEPT = 59;
    static readonly KW_EXISTS = 60;
    static readonly KW_EXIT = 61;
    static readonly KW_EXPLAIN = 62;
    static readonly KW_FALSE = 63;
    static readonly KW_FETCH = 64;
    static readonly KW_FOR = 65;
    static readonly KW_FORCE = 66;
    static readonly KW_FOREIGN = 67;
    static readonly KW_FROM = 68;
    static readonly KW_FULLTEXT = 69;
    static readonly KW_GENERATE = 70;
    static readonly KW_GENERATED = 71;
    static readonly KW_GET = 72;
    static readonly KW_GRANT = 73;
    static readonly KW_GROUP = 74;
    static readonly KW_HAVING = 75;
    static readonly KW_HIGH_PRIORITY = 76;
    static readonly KW_HISTOGRAM = 77;
    static readonly KW_IF = 78;
    static readonly KW_IGNORE = 79;
    static readonly KW_IN = 80;
    static readonly KW_INACTIVE = 81;
    static readonly KW_INDEX = 82;
    static readonly KW_INFILE = 83;
    static readonly KW_INNER = 84;
    static readonly KW_INOUT = 85;
    static readonly KW_INSERT = 86;
    static readonly KW_INTERVAL = 87;
    static readonly KW_INTO = 88;
    static readonly KW_IS = 89;
    static readonly KW_ITERATE = 90;
    static readonly KW_JOIN = 91;
    static readonly KW_KEY = 92;
    static readonly KW_KEYS = 93;
    static readonly KW_KILL = 94;
    static readonly KW_LATERAL = 95;
    static readonly KW_LEADING = 96;
    static readonly KW_LEAVE = 97;
    static readonly KW_LEFT = 98;
    static readonly KW_LIKE = 99;
    static readonly KW_LIMIT = 100;
    static readonly KW_LINEAR = 101;
    static readonly KW_LINES = 102;
    static readonly KW_LOAD = 103;
    static readonly KW_LOCK = 104;
    static readonly KW_LOCKED = 105;
    static readonly KW_LOOP = 106;
    static readonly KW_LOW_PRIORITY = 107;
    static readonly KW_MASTER_BIND = 108;
    static readonly KW_MASTER_SSL_VERIFY_SERVER_CERT = 109;
    static readonly KW_MATCH = 110;
    static readonly KW_MAXVALUE = 111;
    static readonly KW_MODIFIES = 112;
    static readonly KW_NATURAL = 113;
    static readonly KW_NOT = 114;
    static readonly KW_NO_WRITE_TO_BINLOG = 115;
    static readonly KW_NULL_LITERAL = 116;
    static readonly KW_NUMBER = 117;
    static readonly KW_STREAM = 118;
    static readonly KW_ON = 119;
    static readonly KW_OPTIMIZE = 120;
    static readonly KW_OPTION = 121;
    static readonly KW_OPTIONAL = 122;
    static readonly KW_OPTIONALLY = 123;
    static readonly KW_OR = 124;
    static readonly KW_ORDER = 125;
    static readonly KW_OUT = 126;
    static readonly KW_OUTER = 127;
    static readonly KW_OUTFILE = 128;
    static readonly KW_OVER = 129;
    static readonly KW_PARTITION = 130;
    static readonly KW_PRIMARY = 131;
    static readonly KW_PROCEDURE = 132;
    static readonly KW_PURGE = 133;
    static readonly KW_RANGE = 134;
    static readonly KW_READ = 135;
    static readonly KW_READS = 136;
    static readonly KW_REFERENCE = 137;
    static readonly KW_REFERENCES = 138;
    static readonly KW_REGEXP = 139;
    static readonly KW_RELEASE = 140;
    static readonly KW_RENAME = 141;
    static readonly KW_REPEAT = 142;
    static readonly KW_REPLACE = 143;
    static readonly KW_REQUIRE = 144;
    static readonly KW_RESIGNAL = 145;
    static readonly KW_RESTRICT = 146;
    static readonly KW_RETAIN = 147;
    static readonly KW_RETURN = 148;
    static readonly KW_REVOKE = 149;
    static readonly KW_RIGHT = 150;
    static readonly KW_RLIKE = 151;
    static readonly KW_SCHEMA = 152;
    static readonly KW_SCHEMAS = 153;
    static readonly KW_SELECT = 154;
    static readonly KW_SET = 155;
    static readonly KW_SEPARATOR = 156;
    static readonly KW_SHOW = 157;
    static readonly KW_SIGNAL = 158;
    static readonly KW_SKIP = 159;
    static readonly KW_SKIP_QUERY_REWRITE = 160;
    static readonly KW_SPATIAL = 161;
    static readonly KW_SQL = 162;
    static readonly KW_SQLEXCEPTION = 163;
    static readonly KW_SQLSTATE = 164;
    static readonly KW_SQLWARNING = 165;
    static readonly KW_SQL_BIG_RESULT = 166;
    static readonly KW_SQL_CALC_FOUND_ROWS = 167;
    static readonly KW_SQL_SMALL_RESULT = 168;
    static readonly KW_SSL = 169;
    static readonly KW_STACKED = 170;
    static readonly KW_STARTING = 171;
    static readonly KW_STRAIGHT_JOIN = 172;
    static readonly KW_TABLE = 173;
    static readonly KW_TERMINATED = 174;
    static readonly KW_THEN = 175;
    static readonly KW_TO = 176;
    static readonly KW_TRAILING = 177;
    static readonly KW_TRIGGER = 178;
    static readonly KW_TRUE = 179;
    static readonly KW_UNDO = 180;
    static readonly KW_UNION = 181;
    static readonly KW_UNIQUE = 182;
    static readonly KW_UNLOCK = 183;
    static readonly KW_UNSIGNED = 184;
    static readonly KW_UPDATE = 185;
    static readonly KW_USAGE = 186;
    static readonly KW_USE = 187;
    static readonly KW_USING = 188;
    static readonly KW_VALUES = 189;
    static readonly KW_VCPU = 190;
    static readonly KW_WHEN = 191;
    static readonly KW_WHERE = 192;
    static readonly KW_WHILE = 193;
    static readonly KW_WITH = 194;
    static readonly KW_WRITE = 195;
    static readonly KW_XOR = 196;
    static readonly KW_ZEROFILL = 197;
    static readonly KW_TINYINT = 198;
    static readonly KW_SMALLINT = 199;
    static readonly KW_MEDIUMINT = 200;
    static readonly KW_MIDDLEINT = 201;
    static readonly KW_INT = 202;
    static readonly KW_INT1 = 203;
    static readonly KW_INT2 = 204;
    static readonly KW_INT3 = 205;
    static readonly KW_INT4 = 206;
    static readonly KW_INT8 = 207;
    static readonly KW_INTEGER = 208;
    static readonly KW_BIGINT = 209;
    static readonly KW_REAL = 210;
    static readonly KW_DOUBLE = 211;
    static readonly KW_PRECISION = 212;
    static readonly KW_FLOAT = 213;
    static readonly KW_FLOAT4 = 214;
    static readonly KW_FLOAT8 = 215;
    static readonly KW_DECIMAL = 216;
    static readonly KW_DEC = 217;
    static readonly KW_NUMERIC = 218;
    static readonly KW_DATE = 219;
    static readonly KW_TIME = 220;
    static readonly KW_TIMESTAMP = 221;
    static readonly KW_DATETIME = 222;
    static readonly KW_YEAR = 223;
    static readonly KW_CHAR = 224;
    static readonly KW_VARCHAR = 225;
    static readonly KW_NVARCHAR = 226;
    static readonly KW_NATIONAL = 227;
    static readonly KW_BINARY = 228;
    static readonly KW_VARBINARY = 229;
    static readonly KW_TINYBLOB = 230;
    static readonly KW_BLOB = 231;
    static readonly KW_MEDIUMBLOB = 232;
    static readonly KW_LONG = 233;
    static readonly KW_LONGBLOB = 234;
    static readonly KW_TINYTEXT = 235;
    static readonly KW_TEXT = 236;
    static readonly KW_MEDIUMTEXT = 237;
    static readonly KW_LONGTEXT = 238;
    static readonly KW_ENUM = 239;
    static readonly KW_VARYING = 240;
    static readonly KW_SERIAL = 241;
    static readonly KW_YEAR_MONTH = 242;
    static readonly KW_DAY_HOUR = 243;
    static readonly KW_DAY_MINUTE = 244;
    static readonly KW_DAY_SECOND = 245;
    static readonly KW_HOUR_MINUTE = 246;
    static readonly KW_HOUR_SECOND = 247;
    static readonly KW_MINUTE_SECOND = 248;
    static readonly KW_SECOND_MICROSECOND = 249;
    static readonly KW_MINUTE_MICROSECOND = 250;
    static readonly KW_HOUR_MICROSECOND = 251;
    static readonly KW_DAY_MICROSECOND = 252;
    static readonly KW_JSON_TABLE = 253;
    static readonly KW_JSON_VALUE = 254;
    static readonly KW_NESTED = 255;
    static readonly KW_ORDINALITY = 256;
    static readonly KW_PATH = 257;
    static readonly KW_AVG = 258;
    static readonly KW_BIT_AND = 259;
    static readonly KW_BIT_OR = 260;
    static readonly KW_BIT_XOR = 261;
    static readonly KW_COUNT = 262;
    static readonly KW_CUME_DIST = 263;
    static readonly KW_DENSE_RANK = 264;
    static readonly KW_FIRST_VALUE = 265;
    static readonly KW_GROUP_CONCAT = 266;
    static readonly KW_LAG = 267;
    static readonly KW_LAST_VALUE = 268;
    static readonly KW_LEAD = 269;
    static readonly KW_MAX = 270;
    static readonly KW_MIN = 271;
    static readonly KW_NTILE = 272;
    static readonly KW_NTH_VALUE = 273;
    static readonly KW_PERCENT_RANK = 274;
    static readonly KW_RANK = 275;
    static readonly KW_ROW_NUMBER = 276;
    static readonly KW_STD = 277;
    static readonly KW_STDDEV = 278;
    static readonly KW_STDDEV_POP = 279;
    static readonly KW_STDDEV_SAMP = 280;
    static readonly KW_SUM = 281;
    static readonly KW_VAR_POP = 282;
    static readonly KW_VAR_SAMP = 283;
    static readonly KW_VARIANCE = 284;
    static readonly KW_CURRENT_DATE = 285;
    static readonly KW_CURRENT_TIME = 286;
    static readonly KW_CURRENT_TIMESTAMP = 287;
    static readonly KW_LOCALTIME = 288;
    static readonly KW_CURDATE = 289;
    static readonly KW_CURTIME = 290;
    static readonly KW_DATE_ADD = 291;
    static readonly KW_DATE_SUB = 292;
    static readonly KW_EXTRACT = 293;
    static readonly KW_LOCALTIMESTAMP = 294;
    static readonly KW_NOW = 295;
    static readonly KW_POSITION = 296;
    static readonly KW_SUBSTR = 297;
    static readonly KW_SUBSTRING = 298;
    static readonly KW_SYSDATE = 299;
    static readonly KW_TRIM = 300;
    static readonly KW_UTC_DATE = 301;
    static readonly KW_UTC_TIME = 302;
    static readonly KW_UTC_TIMESTAMP = 303;
    static readonly KW_ACCOUNT = 304;
    static readonly KW_ACTION = 305;
    static readonly KW_AFTER = 306;
    static readonly KW_AGGREGATE = 307;
    static readonly KW_ALGORITHM = 308;
    static readonly KW_ANY = 309;
    static readonly KW_ASSIGN_GTIDS_TO_ANONYMOUS_TRANSACTIONS = 310;
    static readonly KW_AT = 311;
    static readonly KW_AUTHORS = 312;
    static readonly KW_AUTOCOMMIT = 313;
    static readonly KW_AUTOEXTEND_SIZE = 314;
    static readonly KW_AUTO_INCREMENT = 315;
    static readonly KW_AVG_ROW_LENGTH = 316;
    static readonly KW_BEGIN = 317;
    static readonly KW_BINLOG = 318;
    static readonly KW_BIT = 319;
    static readonly KW_BLOCK = 320;
    static readonly KW_BOOL = 321;
    static readonly KW_BOOLEAN = 322;
    static readonly KW_BTREE = 323;
    static readonly KW_CACHE = 324;
    static readonly KW_CASCADED = 325;
    static readonly KW_CHAIN = 326;
    static readonly KW_CHANGED = 327;
    static readonly KW_CHANNEL = 328;
    static readonly KW_CHECKSUM = 329;
    static readonly KW_CHALLENGE_RESPONSE = 330;
    static readonly KW_CIPHER = 331;
    static readonly KW_CLASS_ORIGIN = 332;
    static readonly KW_CLIENT = 333;
    static readonly KW_CLOSE = 334;
    static readonly KW_COALESCE = 335;
    static readonly KW_CODE = 336;
    static readonly KW_COLUMNS = 337;
    static readonly KW_COLUMN_FORMAT = 338;
    static readonly KW_COLUMN_NAME = 339;
    static readonly KW_COMMENT = 340;
    static readonly KW_COMMIT = 341;
    static readonly KW_COMPACT = 342;
    static readonly KW_COMPLETION = 343;
    static readonly KW_COMPONENT = 344;
    static readonly KW_COMPRESSED = 345;
    static readonly KW_COMPRESSION = 346;
    static readonly KW_CONCURRENT = 347;
    static readonly KW_CONNECT = 348;
    static readonly KW_CONNECTION = 349;
    static readonly KW_CONSISTENT = 350;
    static readonly KW_CONSTRAINT_CATALOG = 351;
    static readonly KW_CONSTRAINT_SCHEMA = 352;
    static readonly KW_CONSTRAINT_NAME = 353;
    static readonly KW_CONTAINS = 354;
    static readonly KW_CONTEXT = 355;
    static readonly KW_CONTRIBUTORS = 356;
    static readonly KW_COPY = 357;
    static readonly KW_CPU = 358;
    static readonly KW_CURSOR_NAME = 359;
    static readonly KW_DATA = 360;
    static readonly KW_DATAFILE = 361;
    static readonly KW_DEALLOCATE = 362;
    static readonly KW_DEFAULT_AUTH = 363;
    static readonly KW_DEFINER = 364;
    static readonly KW_DELAY_KEY_WRITE = 365;
    static readonly KW_DES_KEY_FILE = 366;
    static readonly KW_DIRECTORY = 367;
    static readonly KW_DISABLE = 368;
    static readonly KW_DISCARD = 369;
    static readonly KW_DISK = 370;
    static readonly KW_DO = 371;
    static readonly KW_DUMPFILE = 372;
    static readonly KW_DUPLICATE = 373;
    static readonly KW_DYNAMIC = 374;
    static readonly KW_ENABLE = 375;
    static readonly KW_ENCRYPTION = 376;
    static readonly KW_ENCRYPTION_KEY_ID = 377;
    static readonly KW_END = 378;
    static readonly KW_ENDS = 379;
    static readonly KW_ENGINE = 380;
    static readonly KW_ENGINES = 381;
    static readonly KW_ERROR = 382;
    static readonly KW_ERRORS = 383;
    static readonly KW_ESCAPE = 384;
    static readonly KW_EVENT = 385;
    static readonly KW_EVENTS = 386;
    static readonly KW_EVERY = 387;
    static readonly KW_EXCHANGE = 388;
    static readonly KW_EXCLUSIVE = 389;
    static readonly KW_EXPIRE = 390;
    static readonly KW_EXPORT = 391;
    static readonly KW_EXTENDED = 392;
    static readonly KW_EXTENT_SIZE = 393;
    static readonly KW_FACTOR = 394;
    static readonly KW_FAILED_LOGIN_ATTEMPTS = 395;
    static readonly KW_FAST = 396;
    static readonly KW_FAULTS = 397;
    static readonly KW_FIELDS = 398;
    static readonly KW_FILE_BLOCK_SIZE = 399;
    static readonly KW_FILTER = 400;
    static readonly KW_FINISH = 401;
    static readonly KW_FIRST = 402;
    static readonly KW_FIXED = 403;
    static readonly KW_FLUSH = 404;
    static readonly KW_FOLLOWING = 405;
    static readonly KW_FOLLOWS = 406;
    static readonly KW_FOUND = 407;
    static readonly KW_FULL = 408;
    static readonly KW_FUNCTION = 409;
    static readonly KW_GENERAL = 410;
    static readonly KW_GET_MASTER_PUBLIC_KEY = 411;
    static readonly KW_GET_SOURCE_PUBLIC_KEY = 412;
    static readonly KW_GLOBAL = 413;
    static readonly KW_GRANTS = 414;
    static readonly KW_GROUP_REPLICATION = 415;
    static readonly KW_GTID_ONLY = 416;
    static readonly KW_HANDLER = 417;
    static readonly KW_HASH = 418;
    static readonly KW_HELP = 419;
    static readonly KW_HISTORY = 420;
    static readonly KW_HOST = 421;
    static readonly KW_HOSTS = 422;
    static readonly KW_IDENTIFIED = 423;
    static readonly KW_IGNORE_SERVER_IDS = 424;
    static readonly KW_IMPORT = 425;
    static readonly KW_INDEXES = 426;
    static readonly KW_INITIAL = 427;
    static readonly KW_INITIAL_SIZE = 428;
    static readonly KW_INITIATE = 429;
    static readonly KW_INPLACE = 430;
    static readonly KW_INSERT_METHOD = 431;
    static readonly KW_INSTALL = 432;
    static readonly KW_INSTANCE = 433;
    static readonly KW_INSTANT = 434;
    static readonly KW_INVISIBLE = 435;
    static readonly KW_INVOKER = 436;
    static readonly KW_IO = 437;
    static readonly KW_IO_THREAD = 438;
    static readonly KW_IPC = 439;
    static readonly KW_ISOLATION = 440;
    static readonly KW_ISSUER = 441;
    static readonly KW_JSON = 442;
    static readonly KW_KEY_BLOCK_SIZE = 443;
    static readonly KW_LANGUAGE = 444;
    static readonly KW_LAST = 445;
    static readonly KW_LEAVES = 446;
    static readonly KW_LESS = 447;
    static readonly KW_LEVEL = 448;
    static readonly KW_LIST = 449;
    static readonly KW_LOCAL = 450;
    static readonly KW_LOGFILE = 451;
    static readonly KW_LOGS = 452;
    static readonly KW_MASTER = 453;
    static readonly KW_MASTER_AUTO_POSITION = 454;
    static readonly KW_MASTER_COMPRESSION_ALGORITHMS = 455;
    static readonly KW_MASTER_CONNECT_RETRY = 456;
    static readonly KW_MASTER_DELAY = 457;
    static readonly KW_MASTER_HEARTBEAT_PERIOD = 458;
    static readonly KW_MASTER_HOST = 459;
    static readonly KW_MASTER_LOG_FILE = 460;
    static readonly KW_MASTER_LOG_POS = 461;
    static readonly KW_MASTER_PASSWORD = 462;
    static readonly KW_MASTER_PORT = 463;
    static readonly KW_MASTER_PUBLIC_KEY_PATH = 464;
    static readonly KW_MASTER_RETRY_COUNT = 465;
    static readonly KW_MASTER_SSL = 466;
    static readonly KW_MASTER_SSL_CA = 467;
    static readonly KW_MASTER_SSL_CAPATH = 468;
    static readonly KW_MASTER_SSL_CERT = 469;
    static readonly KW_MASTER_SSL_CIPHER = 470;
    static readonly KW_MASTER_SSL_CRL = 471;
    static readonly KW_MASTER_SSL_CRLPATH = 472;
    static readonly KW_MASTER_SSL_KEY = 473;
    static readonly KW_MASTER_TLS_CIPHERSUITES = 474;
    static readonly KW_MASTER_TLS_VERSION = 475;
    static readonly KW_MASTER_USER = 476;
    static readonly KW_MASTER_ZSTD_COMPRESSION_LEVEL = 477;
    static readonly KW_MAX_CONNECTIONS_PER_HOUR = 478;
    static readonly KW_MAX_QUERIES_PER_HOUR = 479;
    static readonly KW_MAX_ROWS = 480;
    static readonly KW_MAX_SIZE = 481;
    static readonly KW_MAX_UPDATES_PER_HOUR = 482;
    static readonly KW_MAX_USER_CONNECTIONS = 483;
    static readonly KW_MEDIUM = 484;
    static readonly KW_MEMBER = 485;
    static readonly KW_MERGE = 486;
    static readonly KW_MESSAGE_TEXT = 487;
    static readonly KW_MID = 488;
    static readonly KW_MIGRATE = 489;
    static readonly KW_MIN_ROWS = 490;
    static readonly KW_MODE = 491;
    static readonly KW_MODIFY = 492;
    static readonly KW_MUTEX = 493;
    static readonly KW_MYSQL = 494;
    static readonly KW_MYSQL_ERRNO = 495;
    static readonly KW_NAME = 496;
    static readonly KW_NAMES = 497;
    static readonly KW_NCHAR = 498;
    static readonly KW_NETWORK_NAMESPACE = 499;
    static readonly KW_NEVER = 500;
    static readonly KW_NEXT = 501;
    static readonly KW_NO = 502;
    static readonly KW_NOWAIT = 503;
    static readonly KW_NODEGROUP = 504;
    static readonly KW_NONE = 505;
    static readonly KW_ODBC = 506;
    static readonly KW_OFF = 507;
    static readonly KW_OFFLINE = 508;
    static readonly KW_OFFSET = 509;
    static readonly KW_OF = 510;
    static readonly KW_OLD = 511;
    static readonly KW_OLD_PASSWORD = 512;
    static readonly KW_ONE = 513;
    static readonly KW_ONLINE = 514;
    static readonly KW_ONLY = 515;
    static readonly KW_OPEN = 516;
    static readonly KW_OPTIMIZER_COSTS = 517;
    static readonly KW_OPTIONS = 518;
    static readonly KW_OWNER = 519;
    static readonly KW_PACK_KEYS = 520;
    static readonly KW_PAGE = 521;
    static readonly KW_PAGE_CHECKSUM = 522;
    static readonly KW_PAGE_COMPRESSED = 523;
    static readonly KW_PAGE_COMPRESSION_LEVEL = 524;
    static readonly KW_PARSER = 525;
    static readonly KW_PARTIAL = 526;
    static readonly KW_PARTITIONING = 527;
    static readonly KW_PARTITIONS = 528;
    static readonly KW_PASSWORD = 529;
    static readonly KW_PASSWORD_LOCK_TIME = 530;
    static readonly KW_PERSIST = 531;
    static readonly KW_PERSIST_ONLY = 532;
    static readonly KW_PHASE = 533;
    static readonly KW_PLUGIN = 534;
    static readonly KW_PLUGIN_DIR = 535;
    static readonly KW_PLUGINS = 536;
    static readonly KW_PORT = 537;
    static readonly KW_PRECEDES = 538;
    static readonly KW_PRECEDING = 539;
    static readonly KW_PREPARE = 540;
    static readonly KW_PRESERVE = 541;
    static readonly KW_PREV = 542;
    static readonly KW_PRIVILEGE_CHECKS_USER = 543;
    static readonly KW_PROCESSLIST = 544;
    static readonly KW_PROFILE = 545;
    static readonly KW_PROFILES = 546;
    static readonly KW_PROXY = 547;
    static readonly KW_QUERY = 548;
    static readonly KW_QUICK = 549;
    static readonly KW_REBUILD = 550;
    static readonly KW_RECOVER = 551;
    static readonly KW_RECURSIVE = 552;
    static readonly KW_REDO_BUFFER_SIZE = 553;
    static readonly KW_REDUNDANT = 554;
    static readonly KW_REGISTRATION = 555;
    static readonly KW_RELAY = 556;
    static readonly KW_RELAY_LOG_FILE = 557;
    static readonly KW_RELAY_LOG_POS = 558;
    static readonly KW_RELAYLOG = 559;
    static readonly KW_REMOVE = 560;
    static readonly KW_REORGANIZE = 561;
    static readonly KW_REPAIR = 562;
    static readonly KW_REPLICA = 563;
    static readonly KW_REPLICAS = 564;
    static readonly KW_REPLICATE_DO_DB = 565;
    static readonly KW_REPLICATE_DO_TABLE = 566;
    static readonly KW_REPLICATE_IGNORE_DB = 567;
    static readonly KW_REPLICATE_IGNORE_TABLE = 568;
    static readonly KW_REPLICATE_REWRITE_DB = 569;
    static readonly KW_REPLICATE_WILD_DO_TABLE = 570;
    static readonly KW_REPLICATE_WILD_IGNORE_TABLE = 571;
    static readonly KW_REPLICATION = 572;
    static readonly KW_REQUIRE_ROW_FORMAT = 573;
    static readonly KW_REQUIRE_TABLE_PRIMARY_KEY_CHECK = 574;
    static readonly KW_RESET = 575;
    static readonly KW_RESTART = 576;
    static readonly KW_RESUME = 577;
    static readonly KW_RETURNED_SQLSTATE = 578;
    static readonly KW_RETURNING = 579;
    static readonly KW_RETURNS = 580;
    static readonly KW_REUSE = 581;
    static readonly KW_ROLE = 582;
    static readonly KW_ROLLBACK = 583;
    static readonly KW_ROLLUP = 584;
    static readonly KW_ROTATE = 585;
    static readonly KW_ROW = 586;
    static readonly KW_ROWS = 587;
    static readonly KW_ROW_FORMAT = 588;
    static readonly KW_SAVEPOINT = 589;
    static readonly KW_SCHEDULE = 590;
    static readonly KW_SECURITY = 591;
    static readonly KW_SERVER = 592;
    static readonly KW_SESSION = 593;
    static readonly KW_SHARE = 594;
    static readonly KW_SHARED = 595;
    static readonly KW_SIGNED = 596;
    static readonly KW_SIMPLE = 597;
    static readonly KW_SLAVE = 598;
    static readonly KW_SLOW = 599;
    static readonly KW_SNAPSHOT = 600;
    static readonly KW_SOCKET = 601;
    static readonly KW_SOME = 602;
    static readonly KW_SONAME = 603;
    static readonly KW_SOUNDS = 604;
    static readonly KW_SOURCE = 605;
    static readonly KW_SOURCE_BIND = 606;
    static readonly KW_SOURCE_HOST = 607;
    static readonly KW_SOURCE_USER = 608;
    static readonly KW_SOURCE_PASSWORD = 609;
    static readonly KW_SOURCE_PORT = 610;
    static readonly KW_SOURCE_LOG_FILE = 611;
    static readonly KW_SOURCE_LOG_POS = 612;
    static readonly KW_SOURCE_AUTO_POSITION = 613;
    static readonly KW_SOURCE_HEARTBEAT_PERIOD = 614;
    static readonly KW_SOURCE_CONNECT_RETRY = 615;
    static readonly KW_SOURCE_RETRY_COUNT = 616;
    static readonly KW_SOURCE_CONNECTION_AUTO_FAILOVER = 617;
    static readonly KW_SOURCE_DELAY = 618;
    static readonly KW_SOURCE_COMPRESSION_ALGORITHMS = 619;
    static readonly KW_SOURCE_ZSTD_COMPRESSION_LEVEL = 620;
    static readonly KW_SOURCE_SSL = 621;
    static readonly KW_SOURCE_SSL_CA = 622;
    static readonly KW_SOURCE_SSL_CAPATH = 623;
    static readonly KW_SOURCE_SSL_CERT = 624;
    static readonly KW_SOURCE_SSL_CRL = 625;
    static readonly KW_SOURCE_SSL_CRLPATH = 626;
    static readonly KW_SOURCE_SSL_KEY = 627;
    static readonly KW_SOURCE_SSL_CIPHER = 628;
    static readonly KW_SOURCE_SSL_VERIFY_SERVER_CERT = 629;
    static readonly KW_SOURCE_TLS_VERSION = 630;
    static readonly KW_SOURCE_TLS_CIPHERSUITES = 631;
    static readonly KW_SOURCE_PUBLIC_KEY_PATH = 632;
    static readonly KW_SQL_AFTER_GTIDS = 633;
    static readonly KW_SQL_AFTER_MTS_GAPS = 634;
    static readonly KW_SQL_BEFORE_GTIDS = 635;
    static readonly KW_SQL_BUFFER_RESULT = 636;
    static readonly KW_SQL_CACHE = 637;
    static readonly KW_SQL_NO_CACHE = 638;
    static readonly KW_SQL_THREAD = 639;
    static readonly KW_START = 640;
    static readonly KW_STARTS = 641;
    static readonly KW_STATS_AUTO_RECALC = 642;
    static readonly KW_STATS_PERSISTENT = 643;
    static readonly KW_STATS_SAMPLE_PAGES = 644;
    static readonly KW_STATUS = 645;
    static readonly KW_STOP = 646;
    static readonly KW_STORAGE = 647;
    static readonly KW_STORED = 648;
    static readonly KW_STRING = 649;
    static readonly KW_SUBCLASS_ORIGIN = 650;
    static readonly KW_SUBJECT = 651;
    static readonly KW_SUBPARTITION = 652;
    static readonly KW_SUBPARTITIONS = 653;
    static readonly KW_SUSPEND = 654;
    static readonly KW_SWAPS = 655;
    static readonly KW_SWITCHES = 656;
    static readonly KW_TABLE_NAME = 657;
    static readonly KW_TABLESPACE = 658;
    static readonly KW_TABLE_TYPE = 659;
    static readonly KW_TEMPORARY = 660;
    static readonly KW_TEMPTABLE = 661;
    static readonly KW_THAN = 662;
    static readonly KW_TRADITIONAL = 663;
    static readonly KW_TRANSACTION = 664;
    static readonly KW_TRANSACTIONAL = 665;
    static readonly KW_TREE = 666;
    static readonly KW_TRIGGERS = 667;
    static readonly KW_TRUNCATE = 668;
    static readonly KW_UNBOUNDED = 669;
    static readonly KW_UNDEFINED = 670;
    static readonly KW_UNDOFILE = 671;
    static readonly KW_UNDO_BUFFER_SIZE = 672;
    static readonly KW_UNINSTALL = 673;
    static readonly KW_UNKNOWN = 674;
    static readonly KW_UNREGISTER = 675;
    static readonly KW_UNTIL = 676;
    static readonly KW_UPGRADE = 677;
    static readonly KW_USER = 678;
    static readonly KW_USE_FRM = 679;
    static readonly KW_USER_RESOURCES = 680;
    static readonly KW_VALIDATION = 681;
    static readonly KW_VALUE = 682;
    static readonly KW_VARIABLES = 683;
    static readonly KW_VIEW = 684;
    static readonly KW_VIRTUAL = 685;
    static readonly KW_VISIBLE = 686;
    static readonly KW_WAIT = 687;
    static readonly KW_WARNINGS = 688;
    static readonly KW_WINDOW = 689;
    static readonly KW_WITHOUT = 690;
    static readonly KW_WORK = 691;
    static readonly KW_WRAPPER = 692;
    static readonly KW_X509 = 693;
    static readonly KW_XA = 694;
    static readonly KW_XML = 695;
    static readonly KW_QUARTER = 696;
    static readonly KW_MONTH = 697;
    static readonly KW_DAY = 698;
    static readonly KW_HOUR = 699;
    static readonly KW_MINUTE = 700;
    static readonly KW_WEEK = 701;
    static readonly KW_SECOND = 702;
    static readonly KW_MICROSECOND = 703;
    static readonly KW_ADMIN = 704;
    static readonly KW_APPLICATION_PASSWORD_ADMIN = 705;
    static readonly KW_AUDIT_ABORT_EXEMPT = 706;
    static readonly KW_AUDIT_ADMIN = 707;
    static readonly KW_AUTHENTICATION = 708;
    static readonly KW_AUTHENTICATION_POLICY_ADMIN = 709;
    static readonly KW_BACKUP_ADMIN = 710;
    static readonly KW_BINLOG_ADMIN = 711;
    static readonly KW_BINLOG_ENCRYPTION_ADMIN = 712;
    static readonly KW_CLONE = 713;
    static readonly KW_CLONE_ADMIN = 714;
    static readonly KW_CONNECTION_ADMIN = 715;
    static readonly KW_ENCRYPTION_KEY_ADMIN = 716;
    static readonly KW_EXECUTE = 717;
    static readonly KW_FILE = 718;
    static readonly KW_FIREWALL_ADMIN = 719;
    static readonly KW_FIREWALL_EXEMPT = 720;
    static readonly KW_FIREWALL_USER = 721;
    static readonly KW_FLUSH_OPTIMIZER_COSTS = 722;
    static readonly KW_FLUSH_STATUS = 723;
    static readonly KW_FLUSH_TABLES = 724;
    static readonly KW_FLUSH_USER_RESOURCES = 725;
    static readonly KW_GROUP_REPLICATION_ADMIN = 726;
    static readonly KW_INNODB_REDO_LOG_ARCHIVE = 727;
    static readonly KW_INNODB_REDO_LOG_ENABLE = 728;
    static readonly KW_INVOKE = 729;
    static readonly KW_LAMBDA = 730;
    static readonly KW_NDB_STORED_USER = 731;
    static readonly KW_PASSWORDLESS_USER_ADMIN = 732;
    static readonly KW_PERSIST_RO_VARIABLES_ADMIN = 733;
    static readonly KW_PRIVILEGES = 734;
    static readonly KW_PROCESS = 735;
    static readonly KW_RELOAD = 736;
    static readonly KW_REPLICATION_APPLIER = 737;
    static readonly KW_REPLICATION_SLAVE_ADMIN = 738;
    static readonly KW_RESOURCE = 739;
    static readonly KW_RESOURCE_GROUP_ADMIN = 740;
    static readonly KW_RESOURCE_GROUP_USER = 741;
    static readonly KW_ROLE_ADMIN = 742;
    static readonly KW_ROUTINE = 743;
    static readonly KW_S3 = 744;
    static readonly KW_SERVICE_CONNECTION_ADMIN = 745;
    static readonly KW_SESSION_VARIABLES_ADMIN = 746;
    static readonly KW_SET_USER_ID = 747;
    static readonly KW_SHOW_ROUTINE = 748;
    static readonly KW_SHUTDOWN = 749;
    static readonly KW_SUPER = 750;
    static readonly KW_SYSTEM_VARIABLES_ADMIN = 751;
    static readonly KW_TABLES = 752;
    static readonly KW_TABLE_ENCRYPTION_ADMIN = 753;
    static readonly KW_VERSION_TOKEN_ADMIN = 754;
    static readonly KW_XA_RECOVER_ADMIN = 755;
    static readonly KW_ARMSCII8 = 756;
    static readonly KW_ASCII = 757;
    static readonly KW_BIG5 = 758;
    static readonly KW_CP1250 = 759;
    static readonly KW_CP1251 = 760;
    static readonly KW_CP1256 = 761;
    static readonly KW_CP1257 = 762;
    static readonly KW_CP850 = 763;
    static readonly KW_CP852 = 764;
    static readonly KW_CP866 = 765;
    static readonly KW_CP932 = 766;
    static readonly KW_DEC8 = 767;
    static readonly KW_EUCJPMS = 768;
    static readonly KW_EUCKR = 769;
    static readonly KW_GB18030 = 770;
    static readonly KW_GB2312 = 771;
    static readonly KW_GBK = 772;
    static readonly KW_GEOSTD8 = 773;
    static readonly KW_GREEK = 774;
    static readonly KW_HEBREW = 775;
    static readonly KW_HP8 = 776;
    static readonly KW_KEYBCS2 = 777;
    static readonly KW_KOI8R = 778;
    static readonly KW_KOI8U = 779;
    static readonly KW_LATIN1 = 780;
    static readonly KW_LATIN2 = 781;
    static readonly KW_LATIN5 = 782;
    static readonly KW_LATIN7 = 783;
    static readonly KW_MACCE = 784;
    static readonly KW_MACROMAN = 785;
    static readonly KW_SJIS = 786;
    static readonly KW_SWE7 = 787;
    static readonly KW_TIS620 = 788;
    static readonly KW_UCS2 = 789;
    static readonly KW_UJIS = 790;
    static readonly KW_UTF16 = 791;
    static readonly KW_UTF16LE = 792;
    static readonly KW_UTF32 = 793;
    static readonly KW_UTF8 = 794;
    static readonly KW_UTF8MB3 = 795;
    static readonly KW_UTF8MB4 = 796;
    static readonly KW_ARCHIVE = 797;
    static readonly KW_BLACKHOLE = 798;
    static readonly KW_CSV = 799;
    static readonly KW_FEDERATED = 800;
    static readonly KW_INNODB = 801;
    static readonly KW_MEMORY = 802;
    static readonly KW_MRG_MYISAM = 803;
    static readonly KW_MYISAM = 804;
    static readonly KW_NDB = 805;
    static readonly KW_NDBCLUSTER = 806;
    static readonly KW_PERFORMANCE_SCHEMA = 807;
    static readonly KW_TOKUDB = 808;
    static readonly KW_REPEATABLE = 809;
    static readonly KW_COMMITTED = 810;
    static readonly KW_UNCOMMITTED = 811;
    static readonly KW_SERIALIZABLE = 812;
    static readonly KW_GEOMETRYCOLLECTION = 813;
    static readonly KW_GEOMCOLLECTION = 814;
    static readonly KW_GEOMETRY = 815;
    static readonly KW_LINESTRING = 816;
    static readonly KW_MULTILINESTRING = 817;
    static readonly KW_MULTIPOINT = 818;
    static readonly KW_MULTIPOLYGON = 819;
    static readonly KW_POINT = 820;
    static readonly KW_POLYGON = 821;
    static readonly KW_CATALOG_NAME = 822;
    static readonly KW_CHARSET = 823;
    static readonly KW_COLLATION = 824;
    static readonly KW_ENGINE_ATTRIBUTE = 825;
    static readonly KW_FORMAT = 826;
    static readonly KW_GET_FORMAT = 827;
    static readonly KW_INTERSECT = 828;
    static readonly KW_RANDOM = 829;
    static readonly KW_REVERSE = 830;
    static readonly KW_ROW_COUNT = 831;
    static readonly KW_SCHEMA_NAME = 832;
    static readonly KW_SECONDARY_ENGINE_ATTRIBUTE = 833;
    static readonly KW_SRID = 834;
    static readonly KW_SYSTEM = 835;
    static readonly KW_SYSTEM_USER = 836;
    static readonly KW_THREAD_PRIORITY = 837;
    static readonly KW_TP_CONNECTION_ADMIN = 838;
    static readonly KW_TYPE = 839;
    static readonly KW_WEIGHT_STRING = 840;
    static readonly VAR_ASSIGN = 841;
    static readonly PLUS_ASSIGN = 842;
    static readonly MINUS_ASSIGN = 843;
    static readonly MULT_ASSIGN = 844;
    static readonly DIV_ASSIGN = 845;
    static readonly MOD_ASSIGN = 846;
    static readonly AND_ASSIGN = 847;
    static readonly XOR_ASSIGN = 848;
    static readonly OR_ASSIGN = 849;
    static readonly STAR = 850;
    static readonly DIVIDE = 851;
    static readonly MODULE = 852;
    static readonly PLUS = 853;
    static readonly MINUS = 854;
    static readonly DIV = 855;
    static readonly MOD = 856;
    static readonly EQUAL_SYMBOL = 857;
    static readonly GREATER_SYMBOL = 858;
    static readonly LESS_SYMBOL = 859;
    static readonly EXCLAMATION_SYMBOL = 860;
    static readonly BIT_NOT_OP = 861;
    static readonly BIT_OR_OP = 862;
    static readonly BIT_AND_OP = 863;
    static readonly BIT_XOR_OP = 864;
    static readonly DOT = 865;
    static readonly LR_BRACKET = 866;
    static readonly RR_BRACKET = 867;
    static readonly COMMA = 868;
    static readonly SEMI = 869;
    static readonly AT_SIGN = 870;
    static readonly ZERO_DECIMAL = 871;
    static readonly ONE_DECIMAL = 872;
    static readonly TWO_DECIMAL = 873;
    static readonly THREE_DECIMAL = 874;
    static readonly SINGLE_QUOTE_SYMB = 875;
    static readonly DOUBLE_QUOTE_SYMB = 876;
    static readonly REVERSE_QUOTE_SYMB = 877;
    static readonly COLON_SYMB = 878;
    static readonly CHARSET_REVERSE_QOUTE_STRING = 879;
    static readonly FILESIZE_LITERAL = 880;
    static readonly START_NATIONAL_STRING_LITERAL = 881;
    static readonly STRING_LITERAL = 882;
    static readonly DECIMAL_LITERAL = 883;
    static readonly HEXADECIMAL_LITERAL = 884;
    static readonly REAL_LITERAL = 885;
    static readonly NULL_SPEC_LITERAL = 886;
    static readonly BIT_STRING = 887;
    static readonly STRING_CHARSET_NAME = 888;
    static readonly ID = 889;
    static readonly REVERSE_QUOTE_ID = 890;
    static readonly HOST_IP_ADDRESS = 891;
    static readonly LOCAL_ID = 892;
    static readonly GLOBAL_ID = 893;
    static readonly PERSIST_ID = 894;
    static readonly ERROR_RECONGNIGION = 895;
    static readonly RULE_program = 0;
    static readonly RULE_singleStatement = 1;
    static readonly RULE_sqlStatement = 2;
    static readonly RULE_emptyStatement_ = 3;
    static readonly RULE_ddlStatement = 4;
    static readonly RULE_dmlStatement = 5;
    static readonly RULE_transactionStatement = 6;
    static readonly RULE_replicationStatement = 7;
    static readonly RULE_preparedStatement = 8;
    static readonly RULE_compoundStatement = 9;
    static readonly RULE_administrationStatement = 10;
    static readonly RULE_utilityStatement = 11;
    static readonly RULE_createDatabase = 12;
    static readonly RULE_createEvent = 13;
    static readonly RULE_createIndex = 14;
    static readonly RULE_createLogfileGroup = 15;
    static readonly RULE_createProcedure = 16;
    static readonly RULE_createRole = 17;
    static readonly RULE_createServer = 18;
    static readonly RULE_createTable = 19;
    static readonly RULE_createTablespaceInnodb = 20;
    static readonly RULE_createTablespaceNdb = 21;
    static readonly RULE_createTrigger = 22;
    static readonly RULE_withClause = 23;
    static readonly RULE_commonTableExpressions = 24;
    static readonly RULE_createView = 25;
    static readonly RULE_createDatabaseOption = 26;
    static readonly RULE_charSet = 27;
    static readonly RULE_currentUserExpression = 28;
    static readonly RULE_ownerStatement = 29;
    static readonly RULE_scheduleExpression = 30;
    static readonly RULE_timestampValue = 31;
    static readonly RULE_intervalExpr = 32;
    static readonly RULE_intervalType = 33;
    static readonly RULE_enableType = 34;
    static readonly RULE_indexType = 35;
    static readonly RULE_indexOption = 36;
    static readonly RULE_procedureParameter = 37;
    static readonly RULE_routineOption = 38;
    static readonly RULE_serverOption = 39;
    static readonly RULE_createDefinitions = 40;
    static readonly RULE_createDefinition = 41;
    static readonly RULE_checkConstraintDefinition = 42;
    static readonly RULE_constraintSymbol = 43;
    static readonly RULE_columnDefinition = 44;
    static readonly RULE_columnConstraint = 45;
    static readonly RULE_referenceDefinition = 46;
    static readonly RULE_referenceAction = 47;
    static readonly RULE_referenceControlType = 48;
    static readonly RULE_tableOption = 49;
    static readonly RULE_tableType = 50;
    static readonly RULE_tablespaceStorage = 51;
    static readonly RULE_partitionDefinitions = 52;
    static readonly RULE_partitionFunctionDefinition = 53;
    static readonly RULE_subpartitionFunctionDefinition = 54;
    static readonly RULE_partitionDefinition = 55;
    static readonly RULE_partitionDefinerAtom = 56;
    static readonly RULE_partitionDefinerVector = 57;
    static readonly RULE_subpartitionDefinition = 58;
    static readonly RULE_partitionOption = 59;
    static readonly RULE_alterDatabase = 60;
    static readonly RULE_alterEvent = 61;
    static readonly RULE_alterFunction = 62;
    static readonly RULE_alterInstance = 63;
    static readonly RULE_alterLogfileGroup = 64;
    static readonly RULE_alterProcedure = 65;
    static readonly RULE_alterServer = 66;
    static readonly RULE_alterTable = 67;
    static readonly RULE_alterTablespace = 68;
    static readonly RULE_alterView = 69;
    static readonly RULE_alterOption = 70;
    static readonly RULE_alterPartitionSpecification = 71;
    static readonly RULE_dropDatabase = 72;
    static readonly RULE_dropEvent = 73;
    static readonly RULE_dropIndex = 74;
    static readonly RULE_dropLogfileGroup = 75;
    static readonly RULE_dropProcedure = 76;
    static readonly RULE_dropFunction = 77;
    static readonly RULE_dropServer = 78;
    static readonly RULE_dropSpatial = 79;
    static readonly RULE_dropTable = 80;
    static readonly RULE_dropTablespace = 81;
    static readonly RULE_dropTrigger = 82;
    static readonly RULE_dropView = 83;
    static readonly RULE_dropRole = 84;
    static readonly RULE_setRole = 85;
    static readonly RULE_renameTable = 86;
    static readonly RULE_renameTableClause = 87;
    static readonly RULE_truncateTable = 88;
    static readonly RULE_callStatement = 89;
    static readonly RULE_deleteStatement = 90;
    static readonly RULE_doStatement = 91;
    static readonly RULE_handlerStatement = 92;
    static readonly RULE_insertStatement = 93;
    static readonly RULE_asRowAlias = 94;
    static readonly RULE_selectOrTableOrValues = 95;
    static readonly RULE_interSectStatement = 96;
    static readonly RULE_interSectQuery = 97;
    static readonly RULE_loadDataStatement = 98;
    static readonly RULE_loadXmlStatement = 99;
    static readonly RULE_parenthesizedQuery = 100;
    static readonly RULE_replaceStatement = 101;
    static readonly RULE_selectStatement = 102;
    static readonly RULE_setOperations = 103;
    static readonly RULE_queryExpressionBody = 104;
    static readonly RULE_queryItem = 105;
    static readonly RULE_queryPrimary = 106;
    static readonly RULE_updateStatement = 107;
    static readonly RULE_valuesStatement = 108;
    static readonly RULE_parenthesizedQueryExpression = 109;
    static readonly RULE_queryBlock = 110;
    static readonly RULE_replaceStatementValuesOrSelectOrTable = 111;
    static readonly RULE_rowValuesList = 112;
    static readonly RULE_setAssignmentList = 113;
    static readonly RULE_updatedElement = 114;
    static readonly RULE_assignmentField = 115;
    static readonly RULE_lockClause = 116;
    static readonly RULE_singleDeleteStatement = 117;
    static readonly RULE_multipleDeleteStatement = 118;
    static readonly RULE_handlerOpenStatement = 119;
    static readonly RULE_handlerReadIndexStatement = 120;
    static readonly RULE_handlerReadStatement = 121;
    static readonly RULE_handlerCloseStatement = 122;
    static readonly RULE_importTableStatement = 123;
    static readonly RULE_singleUpdateStatement = 124;
    static readonly RULE_multipleUpdateStatement = 125;
    static readonly RULE_orderByClause = 126;
    static readonly RULE_orderByExpression = 127;
    static readonly RULE_tableSources = 128;
    static readonly RULE_tableSource = 129;
    static readonly RULE_tableSourceItem = 130;
    static readonly RULE_fullColumnNames = 131;
    static readonly RULE_indexHint = 132;
    static readonly RULE_indexHintType = 133;
    static readonly RULE_joinPart = 134;
    static readonly RULE_joinSpec = 135;
    static readonly RULE_queryExpression = 136;
    static readonly RULE_queryExpressionNointo = 137;
    static readonly RULE_querySpecification = 138;
    static readonly RULE_querySpecificationNointo = 139;
    static readonly RULE_unionParenthesis = 140;
    static readonly RULE_unionStatement = 141;
    static readonly RULE_lateralStatement = 142;
    static readonly RULE_jsonTable = 143;
    static readonly RULE_jsonColumnList = 144;
    static readonly RULE_jsonColumn = 145;
    static readonly RULE_jsonOnEmpty = 146;
    static readonly RULE_jsonOnError = 147;
    static readonly RULE_selectSpec = 148;
    static readonly RULE_selectElements = 149;
    static readonly RULE_selectElement = 150;
    static readonly RULE_intoClause = 151;
    static readonly RULE_selectFieldsInto = 152;
    static readonly RULE_selectLinesInto = 153;
    static readonly RULE_fromClause = 154;
    static readonly RULE_groupByClause = 155;
    static readonly RULE_havingClause = 156;
    static readonly RULE_windowClause = 157;
    static readonly RULE_groupByItem = 158;
    static readonly RULE_limitClause = 159;
    static readonly RULE_limitClauseAtom = 160;
    static readonly RULE_startTransaction = 161;
    static readonly RULE_beginWork = 162;
    static readonly RULE_commitWork = 163;
    static readonly RULE_rollbackWork = 164;
    static readonly RULE_savepointStatement = 165;
    static readonly RULE_rollbackStatement = 166;
    static readonly RULE_releaseStatement = 167;
    static readonly RULE_lockTables = 168;
    static readonly RULE_unlockTables = 169;
    static readonly RULE_setAutocommitStatement = 170;
    static readonly RULE_setTransactionStatement = 171;
    static readonly RULE_transactionMode = 172;
    static readonly RULE_lockTableElement = 173;
    static readonly RULE_lockAction = 174;
    static readonly RULE_transactionOption = 175;
    static readonly RULE_transactionLevel = 176;
    static readonly RULE_changeMaster = 177;
    static readonly RULE_changeReplicationFilter = 178;
    static readonly RULE_changeReplicationSource = 179;
    static readonly RULE_purgeBinaryLogs = 180;
    static readonly RULE_startSlaveOrReplica = 181;
    static readonly RULE_stopSlaveOrReplica = 182;
    static readonly RULE_startGroupReplication = 183;
    static readonly RULE_stopGroupReplication = 184;
    static readonly RULE_masterOption = 185;
    static readonly RULE_stringMasterOption = 186;
    static readonly RULE_decimalMasterOption = 187;
    static readonly RULE_boolMasterOption = 188;
    static readonly RULE_v8NewMasterOption = 189;
    static readonly RULE_replicationSourceOption = 190;
    static readonly RULE_stringSourceOption = 191;
    static readonly RULE_decimalSourceOption = 192;
    static readonly RULE_boolSourceOption = 193;
    static readonly RULE_otherSourceOption = 194;
    static readonly RULE_channelOption = 195;
    static readonly RULE_replicationFilter = 196;
    static readonly RULE_tablePair = 197;
    static readonly RULE_threadType = 198;
    static readonly RULE_untilOption = 199;
    static readonly RULE_connectionOptions = 200;
    static readonly RULE_gtuidSet = 201;
    static readonly RULE_xaStartTransaction = 202;
    static readonly RULE_xaEndTransaction = 203;
    static readonly RULE_xaPrepareStatement = 204;
    static readonly RULE_xaCommitWork = 205;
    static readonly RULE_xaRollbackWork = 206;
    static readonly RULE_xaRecoverWork = 207;
    static readonly RULE_prepareStatement = 208;
    static readonly RULE_executeStatement = 209;
    static readonly RULE_deallocatePrepare = 210;
    static readonly RULE_routineBody = 211;
    static readonly RULE_blockStatement = 212;
    static readonly RULE_caseStatement = 213;
    static readonly RULE_ifStatement = 214;
    static readonly RULE_iterateStatement = 215;
    static readonly RULE_leaveStatement = 216;
    static readonly RULE_loopStatement = 217;
    static readonly RULE_repeatStatement = 218;
    static readonly RULE_returnStatement = 219;
    static readonly RULE_whileStatement = 220;
    static readonly RULE_cursorStatement = 221;
    static readonly RULE_declareVariable = 222;
    static readonly RULE_declareCondition = 223;
    static readonly RULE_declareCursor = 224;
    static readonly RULE_declareHandler = 225;
    static readonly RULE_handlerConditionValue = 226;
    static readonly RULE_procedureSqlStatement = 227;
    static readonly RULE_caseAlternative = 228;
    static readonly RULE_elifAlternative = 229;
    static readonly RULE_alterUser = 230;
    static readonly RULE_createUser = 231;
    static readonly RULE_dropUser = 232;
    static readonly RULE_grantStatement = 233;
    static readonly RULE_roleOption = 234;
    static readonly RULE_grantProxy = 235;
    static readonly RULE_alterResourceGroup = 236;
    static readonly RULE_createResourceGroup = 237;
    static readonly RULE_dropResourceGroup = 238;
    static readonly RULE_setResourceGroup = 239;
    static readonly RULE_resourceGroupVcpuSpec = 240;
    static readonly RULE_renameUser = 241;
    static readonly RULE_revokeStatement = 242;
    static readonly RULE_ignoreUnknownUser = 243;
    static readonly RULE_privilegeObjectType = 244;
    static readonly RULE_setPasswordStatement = 245;
    static readonly RULE_userSpecification = 246;
    static readonly RULE_alterUserAuthOption = 247;
    static readonly RULE_createUserAuthOption = 248;
    static readonly RULE_createUserInitialAuthOption = 249;
    static readonly RULE_userAuthOption = 250;
    static readonly RULE_authOptionClause = 251;
    static readonly RULE_authenticationRule = 252;
    static readonly RULE_tlsOption = 253;
    static readonly RULE_userResourceOption = 254;
    static readonly RULE_userPasswordOption = 255;
    static readonly RULE_userLockOption = 256;
    static readonly RULE_factorAuthOption = 257;
    static readonly RULE_registrationOption = 258;
    static readonly RULE_factor = 259;
    static readonly RULE_privelegeClause = 260;
    static readonly RULE_privilege = 261;
    static readonly RULE_privilegeLevel = 262;
    static readonly RULE_renameUserClause = 263;
    static readonly RULE_analyzeTable = 264;
    static readonly RULE_checkTable = 265;
    static readonly RULE_checksumTable = 266;
    static readonly RULE_optimizeTable = 267;
    static readonly RULE_repairTable = 268;
    static readonly RULE_tableActionOption = 269;
    static readonly RULE_checkTableOption = 270;
    static readonly RULE_createFunction = 271;
    static readonly RULE_installComponent = 272;
    static readonly RULE_variableExpr = 273;
    static readonly RULE_uninstallComponent = 274;
    static readonly RULE_installPlugin = 275;
    static readonly RULE_uninstallPlugin = 276;
    static readonly RULE_cloneStatement = 277;
    static readonly RULE_setStatement = 278;
    static readonly RULE_showStatement = 279;
    static readonly RULE_variableClause = 280;
    static readonly RULE_showCommonEntity = 281;
    static readonly RULE_showFilter = 282;
    static readonly RULE_showGlobalInfoClause = 283;
    static readonly RULE_showSchemaEntity = 284;
    static readonly RULE_showProfileType = 285;
    static readonly RULE_binlogStatement = 286;
    static readonly RULE_cacheIndexStatement = 287;
    static readonly RULE_flushStatement = 288;
    static readonly RULE_killStatement = 289;
    static readonly RULE_loadIndexIntoCache = 290;
    static readonly RULE_resetStatement = 291;
    static readonly RULE_resetOption = 292;
    static readonly RULE_resetPersist = 293;
    static readonly RULE_resetAllChannel = 294;
    static readonly RULE_reStartStatement = 295;
    static readonly RULE_shutdownStatement = 296;
    static readonly RULE_tableIndex = 297;
    static readonly RULE_flushOption = 298;
    static readonly RULE_flushTableOption = 299;
    static readonly RULE_loadedTableIndexes = 300;
    static readonly RULE_simpleDescribeStatement = 301;
    static readonly RULE_fullDescribeStatement = 302;
    static readonly RULE_analyzeDescribeStatement = 303;
    static readonly RULE_helpStatement = 304;
    static readonly RULE_useStatement = 305;
    static readonly RULE_signalStatement = 306;
    static readonly RULE_resignalStatement = 307;
    static readonly RULE_signalConditionInformation = 308;
    static readonly RULE_withStatement = 309;
    static readonly RULE_tableStatement = 310;
    static readonly RULE_diagnosticsStatement = 311;
    static readonly RULE_diagnosticsConditionInformationName = 312;
    static readonly RULE_describeObjectClause = 313;
    static readonly RULE_databaseNameCreate = 314;
    static readonly RULE_databaseName = 315;
    static readonly RULE_functionNameCreate = 316;
    static readonly RULE_functionName = 317;
    static readonly RULE_viewNameCreate = 318;
    static readonly RULE_viewName = 319;
    static readonly RULE_indexNameCreate = 320;
    static readonly RULE_indexNames = 321;
    static readonly RULE_indexName = 322;
    static readonly RULE_groupNameCreate = 323;
    static readonly RULE_groupName = 324;
    static readonly RULE_tableNameCreate = 325;
    static readonly RULE_tableNames = 326;
    static readonly RULE_tableName = 327;
    static readonly RULE_userOrRoleNames = 328;
    static readonly RULE_userOrRoleName = 329;
    static readonly RULE_columnNameCreate = 330;
    static readonly RULE_columnNames = 331;
    static readonly RULE_columnName = 332;
    static readonly RULE_tablespaceNameCreate = 333;
    static readonly RULE_tablespaceName = 334;
    static readonly RULE_partitionNameCreate = 335;
    static readonly RULE_partitionNames = 336;
    static readonly RULE_partitionName = 337;
    static readonly RULE_indexColumnName = 338;
    static readonly RULE_userHostPort = 339;
    static readonly RULE_userAtHost = 340;
    static readonly RULE_simpleUserName = 341;
    static readonly RULE_hostName = 342;
    static readonly RULE_userName = 343;
    static readonly RULE_mysqlVariable = 344;
    static readonly RULE_charsetName = 345;
    static readonly RULE_collationName = 346;
    static readonly RULE_engineName = 347;
    static readonly RULE_engineNameBase = 348;
    static readonly RULE_uuidSet = 349;
    static readonly RULE_xid = 350;
    static readonly RULE_xuidStringId = 351;
    static readonly RULE_fullId = 352;
    static readonly RULE_uidList = 353;
    static readonly RULE_uid = 354;
    static readonly RULE_simpleId = 355;
    static readonly RULE_dottedId = 356;
    static readonly RULE_decimalLiteral = 357;
    static readonly RULE_fileSizeLiteral = 358;
    static readonly RULE_stringLiteral = 359;
    static readonly RULE_booleanLiteral = 360;
    static readonly RULE_hexadecimalLiteral = 361;
    static readonly RULE_nullNotnull = 362;
    static readonly RULE_constant = 363;
    static readonly RULE_dataType = 364;
    static readonly RULE_collectionOptions = 365;
    static readonly RULE_convertedDataType = 366;
    static readonly RULE_lengthOneDimension = 367;
    static readonly RULE_lengthTwoDimension = 368;
    static readonly RULE_lengthTwoOptionalDimension = 369;
    static readonly RULE_indexColumnNames = 370;
    static readonly RULE_expressions = 371;
    static readonly RULE_valuesOrValueList = 372;
    static readonly RULE_expressionsWithDefaults = 373;
    static readonly RULE_expressionOrDefault = 374;
    static readonly RULE_constants = 375;
    static readonly RULE_simpleStrings = 376;
    static readonly RULE_userVariables = 377;
    static readonly RULE_defaultValue = 378;
    static readonly RULE_currentTimestamp = 379;
    static readonly RULE_ifExists = 380;
    static readonly RULE_ifNotExists = 381;
    static readonly RULE_orReplace = 382;
    static readonly RULE_functionCall = 383;
    static readonly RULE_specificFunction = 384;
    static readonly RULE_caseFuncAlternative = 385;
    static readonly RULE_levelsInWeightString = 386;
    static readonly RULE_levelInWeightListElement = 387;
    static readonly RULE_aggregateWindowedFunction = 388;
    static readonly RULE_nonAggregateWindowedFunction = 389;
    static readonly RULE_overClause = 390;
    static readonly RULE_windowSpec = 391;
    static readonly RULE_windowName = 392;
    static readonly RULE_frameClause = 393;
    static readonly RULE_frameUnits = 394;
    static readonly RULE_frameExtent = 395;
    static readonly RULE_frameBetween = 396;
    static readonly RULE_frameRange = 397;
    static readonly RULE_partitionClause = 398;
    static readonly RULE_scalarFunctionName = 399;
    static readonly RULE_passwordFunctionClause = 400;
    static readonly RULE_functionArgs = 401;
    static readonly RULE_functionArg = 402;
    static readonly RULE_expression = 403;
    static readonly RULE_predicate = 404;
    static readonly RULE_expressionAtom = 405;
    static readonly RULE_unaryOperator = 406;
    static readonly RULE_comparisonOperator = 407;
    static readonly RULE_comparisonBase = 408;
    static readonly RULE_logicalOperator = 409;
    static readonly RULE_bitOperator = 410;
    static readonly RULE_mathOperator = 411;
    static readonly RULE_jsonOperator = 412;
    static readonly RULE_charsetNameBase = 413;
    static readonly RULE_transactionLevelBase = 414;
    static readonly RULE_privilegesBase = 415;
    static readonly RULE_intervalTypeBase = 416;
    static readonly RULE_dataTypeBase = 417;
    static readonly RULE_keywordsCanBeId = 418;
    static readonly RULE_functionNameBase = 419;
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
    singleStatement(): SingleStatementContext;
    sqlStatement(): SqlStatementContext;
    emptyStatement_(): EmptyStatement_Context;
    ddlStatement(): DdlStatementContext;
    dmlStatement(): DmlStatementContext;
    transactionStatement(): TransactionStatementContext;
    replicationStatement(): ReplicationStatementContext;
    preparedStatement(): PreparedStatementContext;
    compoundStatement(): CompoundStatementContext;
    administrationStatement(): AdministrationStatementContext;
    utilityStatement(): UtilityStatementContext;
    createDatabase(): CreateDatabaseContext;
    createEvent(): CreateEventContext;
    createIndex(): CreateIndexContext;
    createLogfileGroup(): CreateLogfileGroupContext;
    createProcedure(): CreateProcedureContext;
    createRole(): CreateRoleContext;
    createServer(): CreateServerContext;
    createTable(): CreateTableContext;
    createTablespaceInnodb(): CreateTablespaceInnodbContext;
    createTablespaceNdb(): CreateTablespaceNdbContext;
    createTrigger(): CreateTriggerContext;
    withClause(): WithClauseContext;
    commonTableExpressions(): CommonTableExpressionsContext;
    createView(): CreateViewContext;
    createDatabaseOption(): CreateDatabaseOptionContext;
    charSet(): CharSetContext;
    currentUserExpression(): CurrentUserExpressionContext;
    ownerStatement(): OwnerStatementContext;
    scheduleExpression(): ScheduleExpressionContext;
    timestampValue(): TimestampValueContext;
    intervalExpr(): IntervalExprContext;
    intervalType(): IntervalTypeContext;
    enableType(): EnableTypeContext;
    indexType(): IndexTypeContext;
    indexOption(): IndexOptionContext;
    procedureParameter(): ProcedureParameterContext;
    routineOption(): RoutineOptionContext;
    serverOption(): ServerOptionContext;
    createDefinitions(): CreateDefinitionsContext;
    createDefinition(): CreateDefinitionContext;
    checkConstraintDefinition(): CheckConstraintDefinitionContext;
    constraintSymbol(): ConstraintSymbolContext;
    columnDefinition(): ColumnDefinitionContext;
    columnConstraint(): ColumnConstraintContext;
    referenceDefinition(): ReferenceDefinitionContext;
    referenceAction(): ReferenceActionContext;
    referenceControlType(): ReferenceControlTypeContext;
    tableOption(): TableOptionContext;
    tableType(): TableTypeContext;
    tablespaceStorage(): TablespaceStorageContext;
    partitionDefinitions(): PartitionDefinitionsContext;
    partitionFunctionDefinition(): PartitionFunctionDefinitionContext;
    subpartitionFunctionDefinition(): SubpartitionFunctionDefinitionContext;
    partitionDefinition(): PartitionDefinitionContext;
    partitionDefinerAtom(): PartitionDefinerAtomContext;
    partitionDefinerVector(): PartitionDefinerVectorContext;
    subpartitionDefinition(): SubpartitionDefinitionContext;
    partitionOption(): PartitionOptionContext;
    alterDatabase(): AlterDatabaseContext;
    alterEvent(): AlterEventContext;
    alterFunction(): AlterFunctionContext;
    alterInstance(): AlterInstanceContext;
    alterLogfileGroup(): AlterLogfileGroupContext;
    alterProcedure(): AlterProcedureContext;
    alterServer(): AlterServerContext;
    alterTable(): AlterTableContext;
    alterTablespace(): AlterTablespaceContext;
    alterView(): AlterViewContext;
    alterOption(): AlterOptionContext;
    alterPartitionSpecification(): AlterPartitionSpecificationContext;
    dropDatabase(): DropDatabaseContext;
    dropEvent(): DropEventContext;
    dropIndex(): DropIndexContext;
    dropLogfileGroup(): DropLogfileGroupContext;
    dropProcedure(): DropProcedureContext;
    dropFunction(): DropFunctionContext;
    dropServer(): DropServerContext;
    dropSpatial(): DropSpatialContext;
    dropTable(): DropTableContext;
    dropTablespace(): DropTablespaceContext;
    dropTrigger(): DropTriggerContext;
    dropView(): DropViewContext;
    dropRole(): DropRoleContext;
    setRole(): SetRoleContext;
    renameTable(): RenameTableContext;
    renameTableClause(): RenameTableClauseContext;
    truncateTable(): TruncateTableContext;
    callStatement(): CallStatementContext;
    deleteStatement(): DeleteStatementContext;
    doStatement(): DoStatementContext;
    handlerStatement(): HandlerStatementContext;
    insertStatement(): InsertStatementContext;
    asRowAlias(): AsRowAliasContext;
    selectOrTableOrValues(): SelectOrTableOrValuesContext;
    interSectStatement(): InterSectStatementContext;
    interSectQuery(): InterSectQueryContext;
    loadDataStatement(): LoadDataStatementContext;
    loadXmlStatement(): LoadXmlStatementContext;
    parenthesizedQuery(): ParenthesizedQueryContext;
    replaceStatement(): ReplaceStatementContext;
    selectStatement(): SelectStatementContext;
    setOperations(): SetOperationsContext;
    queryExpressionBody(): QueryExpressionBodyContext;
    queryExpressionBody(_p: number): QueryExpressionBodyContext;
    queryItem(): QueryItemContext;
    queryItem(_p: number): QueryItemContext;
    queryPrimary(): QueryPrimaryContext;
    updateStatement(): UpdateStatementContext;
    valuesStatement(): ValuesStatementContext;
    parenthesizedQueryExpression(): ParenthesizedQueryExpressionContext;
    queryBlock(): QueryBlockContext;
    replaceStatementValuesOrSelectOrTable(): ReplaceStatementValuesOrSelectOrTableContext;
    rowValuesList(): RowValuesListContext;
    setAssignmentList(): SetAssignmentListContext;
    updatedElement(): UpdatedElementContext;
    assignmentField(): AssignmentFieldContext;
    lockClause(): LockClauseContext;
    singleDeleteStatement(): SingleDeleteStatementContext;
    multipleDeleteStatement(): MultipleDeleteStatementContext;
    handlerOpenStatement(): HandlerOpenStatementContext;
    handlerReadIndexStatement(): HandlerReadIndexStatementContext;
    handlerReadStatement(): HandlerReadStatementContext;
    handlerCloseStatement(): HandlerCloseStatementContext;
    importTableStatement(): ImportTableStatementContext;
    singleUpdateStatement(): SingleUpdateStatementContext;
    multipleUpdateStatement(): MultipleUpdateStatementContext;
    orderByClause(): OrderByClauseContext;
    orderByExpression(): OrderByExpressionContext;
    tableSources(): TableSourcesContext;
    tableSource(): TableSourceContext;
    tableSourceItem(): TableSourceItemContext;
    fullColumnNames(): FullColumnNamesContext;
    indexHint(): IndexHintContext;
    indexHintType(): IndexHintTypeContext;
    joinPart(): JoinPartContext;
    joinSpec(): JoinSpecContext;
    queryExpression(): QueryExpressionContext;
    queryExpressionNointo(): QueryExpressionNointoContext;
    querySpecification(): QuerySpecificationContext;
    querySpecificationNointo(): QuerySpecificationNointoContext;
    unionParenthesis(): UnionParenthesisContext;
    unionStatement(): UnionStatementContext;
    lateralStatement(): LateralStatementContext;
    jsonTable(): JsonTableContext;
    jsonColumnList(): JsonColumnListContext;
    jsonColumn(): JsonColumnContext;
    jsonOnEmpty(): JsonOnEmptyContext;
    jsonOnError(): JsonOnErrorContext;
    selectSpec(): SelectSpecContext;
    selectElements(): SelectElementsContext;
    selectElement(): SelectElementContext;
    intoClause(): IntoClauseContext;
    selectFieldsInto(): SelectFieldsIntoContext;
    selectLinesInto(): SelectLinesIntoContext;
    fromClause(): FromClauseContext;
    groupByClause(): GroupByClauseContext;
    havingClause(): HavingClauseContext;
    windowClause(): WindowClauseContext;
    groupByItem(): GroupByItemContext;
    limitClause(): LimitClauseContext;
    limitClauseAtom(): LimitClauseAtomContext;
    startTransaction(): StartTransactionContext;
    beginWork(): BeginWorkContext;
    commitWork(): CommitWorkContext;
    rollbackWork(): RollbackWorkContext;
    savepointStatement(): SavepointStatementContext;
    rollbackStatement(): RollbackStatementContext;
    releaseStatement(): ReleaseStatementContext;
    lockTables(): LockTablesContext;
    unlockTables(): UnlockTablesContext;
    setAutocommitStatement(): SetAutocommitStatementContext;
    setTransactionStatement(): SetTransactionStatementContext;
    transactionMode(): TransactionModeContext;
    lockTableElement(): LockTableElementContext;
    lockAction(): LockActionContext;
    transactionOption(): TransactionOptionContext;
    transactionLevel(): TransactionLevelContext;
    changeMaster(): ChangeMasterContext;
    changeReplicationFilter(): ChangeReplicationFilterContext;
    changeReplicationSource(): ChangeReplicationSourceContext;
    purgeBinaryLogs(): PurgeBinaryLogsContext;
    startSlaveOrReplica(): StartSlaveOrReplicaContext;
    stopSlaveOrReplica(): StopSlaveOrReplicaContext;
    startGroupReplication(): StartGroupReplicationContext;
    stopGroupReplication(): StopGroupReplicationContext;
    masterOption(): MasterOptionContext;
    stringMasterOption(): StringMasterOptionContext;
    decimalMasterOption(): DecimalMasterOptionContext;
    boolMasterOption(): BoolMasterOptionContext;
    v8NewMasterOption(): V8NewMasterOptionContext;
    replicationSourceOption(): ReplicationSourceOptionContext;
    stringSourceOption(): StringSourceOptionContext;
    decimalSourceOption(): DecimalSourceOptionContext;
    boolSourceOption(): BoolSourceOptionContext;
    otherSourceOption(): OtherSourceOptionContext;
    channelOption(): ChannelOptionContext;
    replicationFilter(): ReplicationFilterContext;
    tablePair(): TablePairContext;
    threadType(): ThreadTypeContext;
    untilOption(): UntilOptionContext;
    connectionOptions(): ConnectionOptionsContext;
    gtuidSet(): GtuidSetContext;
    xaStartTransaction(): XaStartTransactionContext;
    xaEndTransaction(): XaEndTransactionContext;
    xaPrepareStatement(): XaPrepareStatementContext;
    xaCommitWork(): XaCommitWorkContext;
    xaRollbackWork(): XaRollbackWorkContext;
    xaRecoverWork(): XaRecoverWorkContext;
    prepareStatement(): PrepareStatementContext;
    executeStatement(): ExecuteStatementContext;
    deallocatePrepare(): DeallocatePrepareContext;
    routineBody(): RoutineBodyContext;
    blockStatement(): BlockStatementContext;
    caseStatement(): CaseStatementContext;
    ifStatement(): IfStatementContext;
    iterateStatement(): IterateStatementContext;
    leaveStatement(): LeaveStatementContext;
    loopStatement(): LoopStatementContext;
    repeatStatement(): RepeatStatementContext;
    returnStatement(): ReturnStatementContext;
    whileStatement(): WhileStatementContext;
    cursorStatement(): CursorStatementContext;
    declareVariable(): DeclareVariableContext;
    declareCondition(): DeclareConditionContext;
    declareCursor(): DeclareCursorContext;
    declareHandler(): DeclareHandlerContext;
    handlerConditionValue(): HandlerConditionValueContext;
    procedureSqlStatement(): ProcedureSqlStatementContext;
    caseAlternative(): CaseAlternativeContext;
    elifAlternative(): ElifAlternativeContext;
    alterUser(): AlterUserContext;
    createUser(): CreateUserContext;
    dropUser(): DropUserContext;
    grantStatement(): GrantStatementContext;
    roleOption(): RoleOptionContext;
    grantProxy(): GrantProxyContext;
    alterResourceGroup(): AlterResourceGroupContext;
    createResourceGroup(): CreateResourceGroupContext;
    dropResourceGroup(): DropResourceGroupContext;
    setResourceGroup(): SetResourceGroupContext;
    resourceGroupVcpuSpec(): ResourceGroupVcpuSpecContext;
    renameUser(): RenameUserContext;
    revokeStatement(): RevokeStatementContext;
    ignoreUnknownUser(): IgnoreUnknownUserContext;
    privilegeObjectType(): PrivilegeObjectTypeContext;
    setPasswordStatement(): SetPasswordStatementContext;
    userSpecification(): UserSpecificationContext;
    alterUserAuthOption(): AlterUserAuthOptionContext;
    createUserAuthOption(): CreateUserAuthOptionContext;
    createUserInitialAuthOption(): CreateUserInitialAuthOptionContext;
    userAuthOption(): UserAuthOptionContext;
    authOptionClause(): AuthOptionClauseContext;
    authenticationRule(): AuthenticationRuleContext;
    tlsOption(): TlsOptionContext;
    userResourceOption(): UserResourceOptionContext;
    userPasswordOption(): UserPasswordOptionContext;
    userLockOption(): UserLockOptionContext;
    factorAuthOption(): FactorAuthOptionContext;
    registrationOption(): RegistrationOptionContext;
    factor(): FactorContext;
    privelegeClause(): PrivelegeClauseContext;
    privilege(): PrivilegeContext;
    privilegeLevel(): PrivilegeLevelContext;
    renameUserClause(): RenameUserClauseContext;
    analyzeTable(): AnalyzeTableContext;
    checkTable(): CheckTableContext;
    checksumTable(): ChecksumTableContext;
    optimizeTable(): OptimizeTableContext;
    repairTable(): RepairTableContext;
    tableActionOption(): TableActionOptionContext;
    checkTableOption(): CheckTableOptionContext;
    createFunction(): CreateFunctionContext;
    installComponent(): InstallComponentContext;
    variableExpr(): VariableExprContext;
    uninstallComponent(): UninstallComponentContext;
    installPlugin(): InstallPluginContext;
    uninstallPlugin(): UninstallPluginContext;
    cloneStatement(): CloneStatementContext;
    setStatement(): SetStatementContext;
    showStatement(): ShowStatementContext;
    variableClause(): VariableClauseContext;
    showCommonEntity(): ShowCommonEntityContext;
    showFilter(): ShowFilterContext;
    showGlobalInfoClause(): ShowGlobalInfoClauseContext;
    showSchemaEntity(): ShowSchemaEntityContext;
    showProfileType(): ShowProfileTypeContext;
    binlogStatement(): BinlogStatementContext;
    cacheIndexStatement(): CacheIndexStatementContext;
    flushStatement(): FlushStatementContext;
    killStatement(): KillStatementContext;
    loadIndexIntoCache(): LoadIndexIntoCacheContext;
    resetStatement(): ResetStatementContext;
    resetOption(): ResetOptionContext;
    resetPersist(): ResetPersistContext;
    resetAllChannel(): ResetAllChannelContext;
    reStartStatement(): ReStartStatementContext;
    shutdownStatement(): ShutdownStatementContext;
    tableIndex(): TableIndexContext;
    flushOption(): FlushOptionContext;
    flushTableOption(): FlushTableOptionContext;
    loadedTableIndexes(): LoadedTableIndexesContext;
    simpleDescribeStatement(): SimpleDescribeStatementContext;
    fullDescribeStatement(): FullDescribeStatementContext;
    analyzeDescribeStatement(): AnalyzeDescribeStatementContext;
    helpStatement(): HelpStatementContext;
    useStatement(): UseStatementContext;
    signalStatement(): SignalStatementContext;
    resignalStatement(): ResignalStatementContext;
    signalConditionInformation(): SignalConditionInformationContext;
    withStatement(): WithStatementContext;
    tableStatement(): TableStatementContext;
    diagnosticsStatement(): DiagnosticsStatementContext;
    diagnosticsConditionInformationName(): DiagnosticsConditionInformationNameContext;
    describeObjectClause(): DescribeObjectClauseContext;
    databaseNameCreate(): DatabaseNameCreateContext;
    databaseName(): DatabaseNameContext;
    functionNameCreate(): FunctionNameCreateContext;
    functionName(): FunctionNameContext;
    viewNameCreate(): ViewNameCreateContext;
    viewName(): ViewNameContext;
    indexNameCreate(): IndexNameCreateContext;
    indexNames(): IndexNamesContext;
    indexName(): IndexNameContext;
    groupNameCreate(): GroupNameCreateContext;
    groupName(): GroupNameContext;
    tableNameCreate(): TableNameCreateContext;
    tableNames(): TableNamesContext;
    tableName(): TableNameContext;
    userOrRoleNames(): UserOrRoleNamesContext;
    userOrRoleName(): UserOrRoleNameContext;
    columnNameCreate(): ColumnNameCreateContext;
    columnNames(): ColumnNamesContext;
    columnName(): ColumnNameContext;
    tablespaceNameCreate(): TablespaceNameCreateContext;
    tablespaceName(): TablespaceNameContext;
    partitionNameCreate(): PartitionNameCreateContext;
    partitionNames(): PartitionNamesContext;
    partitionName(): PartitionNameContext;
    indexColumnName(): IndexColumnNameContext;
    userHostPort(): UserHostPortContext;
    userAtHost(): UserAtHostContext;
    simpleUserName(): SimpleUserNameContext;
    hostName(): HostNameContext;
    userName(): UserNameContext;
    mysqlVariable(): MysqlVariableContext;
    charsetName(): CharsetNameContext;
    collationName(): CollationNameContext;
    engineName(): EngineNameContext;
    engineNameBase(): EngineNameBaseContext;
    uuidSet(): UuidSetContext;
    xid(): XidContext;
    xuidStringId(): XuidStringIdContext;
    fullId(): FullIdContext;
    uidList(): UidListContext;
    uid(): UidContext;
    simpleId(): SimpleIdContext;
    dottedId(): DottedIdContext;
    decimalLiteral(): DecimalLiteralContext;
    fileSizeLiteral(): FileSizeLiteralContext;
    stringLiteral(): StringLiteralContext;
    booleanLiteral(): BooleanLiteralContext;
    hexadecimalLiteral(): HexadecimalLiteralContext;
    nullNotnull(): NullNotnullContext;
    constant(): ConstantContext;
    dataType(): DataTypeContext;
    collectionOptions(): CollectionOptionsContext;
    convertedDataType(): ConvertedDataTypeContext;
    lengthOneDimension(): LengthOneDimensionContext;
    lengthTwoDimension(): LengthTwoDimensionContext;
    lengthTwoOptionalDimension(): LengthTwoOptionalDimensionContext;
    indexColumnNames(): IndexColumnNamesContext;
    expressions(): ExpressionsContext;
    valuesOrValueList(): ValuesOrValueListContext;
    expressionsWithDefaults(): ExpressionsWithDefaultsContext;
    expressionOrDefault(): ExpressionOrDefaultContext;
    constants(): ConstantsContext;
    simpleStrings(): SimpleStringsContext;
    userVariables(): UserVariablesContext;
    defaultValue(): DefaultValueContext;
    currentTimestamp(): CurrentTimestampContext;
    ifExists(): IfExistsContext;
    ifNotExists(): IfNotExistsContext;
    orReplace(): OrReplaceContext;
    functionCall(): FunctionCallContext;
    specificFunction(): SpecificFunctionContext;
    caseFuncAlternative(): CaseFuncAlternativeContext;
    levelsInWeightString(): LevelsInWeightStringContext;
    levelInWeightListElement(): LevelInWeightListElementContext;
    aggregateWindowedFunction(): AggregateWindowedFunctionContext;
    nonAggregateWindowedFunction(): NonAggregateWindowedFunctionContext;
    overClause(): OverClauseContext;
    windowSpec(): WindowSpecContext;
    windowName(): WindowNameContext;
    frameClause(): FrameClauseContext;
    frameUnits(): FrameUnitsContext;
    frameExtent(): FrameExtentContext;
    frameBetween(): FrameBetweenContext;
    frameRange(): FrameRangeContext;
    partitionClause(): PartitionClauseContext;
    scalarFunctionName(): ScalarFunctionNameContext;
    passwordFunctionClause(): PasswordFunctionClauseContext;
    functionArgs(): FunctionArgsContext;
    functionArg(): FunctionArgContext;
    expression(): ExpressionContext;
    expression(_p: number): ExpressionContext;
    predicate(): PredicateContext;
    predicate(_p: number): PredicateContext;
    expressionAtom(): ExpressionAtomContext;
    expressionAtom(_p: number): ExpressionAtomContext;
    unaryOperator(): UnaryOperatorContext;
    comparisonOperator(): ComparisonOperatorContext;
    comparisonBase(): ComparisonBaseContext;
    logicalOperator(): LogicalOperatorContext;
    bitOperator(): BitOperatorContext;
    mathOperator(): MathOperatorContext;
    jsonOperator(): JsonOperatorContext;
    charsetNameBase(): CharsetNameBaseContext;
    transactionLevelBase(): TransactionLevelBaseContext;
    privilegesBase(): PrivilegesBaseContext;
    intervalTypeBase(): IntervalTypeBaseContext;
    dataTypeBase(): DataTypeBaseContext;
    keywordsCanBeId(): KeywordsCanBeIdContext;
    functionNameBase(): FunctionNameBaseContext;
    sempred(_localctx: RuleContext, ruleIndex: number, predIndex: number): boolean;
    private queryExpressionBody_sempred;
    private queryItem_sempred;
    private expression_sempred;
    private predicate_sempred;
    private expressionAtom_sempred;
    private static readonly _serializedATNSegments;
    private static readonly _serializedATNSegment0;
    private static readonly _serializedATNSegment1;
    private static readonly _serializedATNSegment2;
    private static readonly _serializedATNSegment3;
    private static readonly _serializedATNSegment4;
    private static readonly _serializedATNSegment5;
    private static readonly _serializedATNSegment6;
    private static readonly _serializedATNSegment7;
    private static readonly _serializedATNSegment8;
    private static readonly _serializedATNSegment9;
    private static readonly _serializedATNSegment10;
    private static readonly _serializedATNSegment11;
    private static readonly _serializedATNSegment12;
    private static readonly _serializedATNSegment13;
    private static readonly _serializedATNSegment14;
    private static readonly _serializedATNSegment15;
    static readonly _serializedATN: string;
    static __ATN: ATN;
    static get _ATN(): ATN;
}
export declare class ProgramContext extends ParserRuleContext {
    EOF(): TerminalNode;
    singleStatement(): SingleStatementContext[];
    singleStatement(i: number): SingleStatementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SingleStatementContext extends ParserRuleContext {
    sqlStatement(): SqlStatementContext | undefined;
    SEMI(): TerminalNode | undefined;
    emptyStatement_(): EmptyStatement_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SqlStatementContext extends ParserRuleContext {
    ddlStatement(): DdlStatementContext | undefined;
    dmlStatement(): DmlStatementContext | undefined;
    transactionStatement(): TransactionStatementContext | undefined;
    replicationStatement(): ReplicationStatementContext | undefined;
    preparedStatement(): PreparedStatementContext | undefined;
    administrationStatement(): AdministrationStatementContext | undefined;
    utilityStatement(): UtilityStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class EmptyStatement_Context extends ParserRuleContext {
    SEMI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DdlStatementContext extends ParserRuleContext {
    createDatabase(): CreateDatabaseContext | undefined;
    createEvent(): CreateEventContext | undefined;
    createIndex(): CreateIndexContext | undefined;
    createLogfileGroup(): CreateLogfileGroupContext | undefined;
    createProcedure(): CreateProcedureContext | undefined;
    createFunction(): CreateFunctionContext | undefined;
    createServer(): CreateServerContext | undefined;
    createTable(): CreateTableContext | undefined;
    createTablespaceInnodb(): CreateTablespaceInnodbContext | undefined;
    createTablespaceNdb(): CreateTablespaceNdbContext | undefined;
    createTrigger(): CreateTriggerContext | undefined;
    createView(): CreateViewContext | undefined;
    createRole(): CreateRoleContext | undefined;
    alterDatabase(): AlterDatabaseContext | undefined;
    alterEvent(): AlterEventContext | undefined;
    alterFunction(): AlterFunctionContext | undefined;
    alterInstance(): AlterInstanceContext | undefined;
    alterLogfileGroup(): AlterLogfileGroupContext | undefined;
    alterProcedure(): AlterProcedureContext | undefined;
    alterServer(): AlterServerContext | undefined;
    alterTable(): AlterTableContext | undefined;
    alterTablespace(): AlterTablespaceContext | undefined;
    alterView(): AlterViewContext | undefined;
    dropDatabase(): DropDatabaseContext | undefined;
    dropEvent(): DropEventContext | undefined;
    dropIndex(): DropIndexContext | undefined;
    dropLogfileGroup(): DropLogfileGroupContext | undefined;
    dropProcedure(): DropProcedureContext | undefined;
    dropFunction(): DropFunctionContext | undefined;
    dropServer(): DropServerContext | undefined;
    dropSpatial(): DropSpatialContext | undefined;
    dropTable(): DropTableContext | undefined;
    dropTablespace(): DropTablespaceContext | undefined;
    dropTrigger(): DropTriggerContext | undefined;
    dropView(): DropViewContext | undefined;
    dropRole(): DropRoleContext | undefined;
    setRole(): SetRoleContext | undefined;
    renameTable(): RenameTableContext | undefined;
    truncateTable(): TruncateTableContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DmlStatementContext extends ParserRuleContext {
    selectStatement(): SelectStatementContext | undefined;
    setOperations(): SetOperationsContext | undefined;
    insertStatement(): InsertStatementContext | undefined;
    updateStatement(): UpdateStatementContext | undefined;
    deleteStatement(): DeleteStatementContext | undefined;
    replaceStatement(): ReplaceStatementContext | undefined;
    callStatement(): CallStatementContext | undefined;
    interSectStatement(): InterSectStatementContext | undefined;
    loadDataStatement(): LoadDataStatementContext | undefined;
    loadXmlStatement(): LoadXmlStatementContext | undefined;
    parenthesizedQuery(): ParenthesizedQueryContext | undefined;
    doStatement(): DoStatementContext | undefined;
    handlerStatement(): HandlerStatementContext | undefined;
    importTableStatement(): ImportTableStatementContext | undefined;
    valuesStatement(): ValuesStatementContext | undefined;
    withStatement(): WithStatementContext | undefined;
    tableStatement(): TableStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TransactionStatementContext extends ParserRuleContext {
    startTransaction(): StartTransactionContext | undefined;
    beginWork(): BeginWorkContext | undefined;
    commitWork(): CommitWorkContext | undefined;
    rollbackWork(): RollbackWorkContext | undefined;
    savepointStatement(): SavepointStatementContext | undefined;
    rollbackStatement(): RollbackStatementContext | undefined;
    releaseStatement(): ReleaseStatementContext | undefined;
    lockTables(): LockTablesContext | undefined;
    unlockTables(): UnlockTablesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ReplicationStatementContext extends ParserRuleContext {
    changeMaster(): ChangeMasterContext | undefined;
    changeReplicationFilter(): ChangeReplicationFilterContext | undefined;
    changeReplicationSource(): ChangeReplicationSourceContext | undefined;
    purgeBinaryLogs(): PurgeBinaryLogsContext | undefined;
    startSlaveOrReplica(): StartSlaveOrReplicaContext | undefined;
    stopSlaveOrReplica(): StopSlaveOrReplicaContext | undefined;
    startGroupReplication(): StartGroupReplicationContext | undefined;
    stopGroupReplication(): StopGroupReplicationContext | undefined;
    xaStartTransaction(): XaStartTransactionContext | undefined;
    xaEndTransaction(): XaEndTransactionContext | undefined;
    xaPrepareStatement(): XaPrepareStatementContext | undefined;
    xaCommitWork(): XaCommitWorkContext | undefined;
    xaRollbackWork(): XaRollbackWorkContext | undefined;
    xaRecoverWork(): XaRecoverWorkContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PreparedStatementContext extends ParserRuleContext {
    prepareStatement(): PrepareStatementContext | undefined;
    executeStatement(): ExecuteStatementContext | undefined;
    deallocatePrepare(): DeallocatePrepareContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CompoundStatementContext extends ParserRuleContext {
    blockStatement(): BlockStatementContext | undefined;
    caseStatement(): CaseStatementContext | undefined;
    ifStatement(): IfStatementContext | undefined;
    leaveStatement(): LeaveStatementContext | undefined;
    loopStatement(): LoopStatementContext | undefined;
    repeatStatement(): RepeatStatementContext | undefined;
    whileStatement(): WhileStatementContext | undefined;
    iterateStatement(): IterateStatementContext | undefined;
    returnStatement(): ReturnStatementContext | undefined;
    cursorStatement(): CursorStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AdministrationStatementContext extends ParserRuleContext {
    alterUser(): AlterUserContext | undefined;
    createUser(): CreateUserContext | undefined;
    dropUser(): DropUserContext | undefined;
    grantStatement(): GrantStatementContext | undefined;
    grantProxy(): GrantProxyContext | undefined;
    renameUser(): RenameUserContext | undefined;
    revokeStatement(): RevokeStatementContext | undefined;
    alterResourceGroup(): AlterResourceGroupContext | undefined;
    createResourceGroup(): CreateResourceGroupContext | undefined;
    dropResourceGroup(): DropResourceGroupContext | undefined;
    setResourceGroup(): SetResourceGroupContext | undefined;
    analyzeTable(): AnalyzeTableContext | undefined;
    checkTable(): CheckTableContext | undefined;
    checksumTable(): ChecksumTableContext | undefined;
    optimizeTable(): OptimizeTableContext | undefined;
    repairTable(): RepairTableContext | undefined;
    installComponent(): InstallComponentContext | undefined;
    uninstallComponent(): UninstallComponentContext | undefined;
    installPlugin(): InstallPluginContext | undefined;
    uninstallPlugin(): UninstallPluginContext | undefined;
    cloneStatement(): CloneStatementContext | undefined;
    setStatement(): SetStatementContext | undefined;
    showStatement(): ShowStatementContext | undefined;
    binlogStatement(): BinlogStatementContext | undefined;
    cacheIndexStatement(): CacheIndexStatementContext | undefined;
    flushStatement(): FlushStatementContext | undefined;
    killStatement(): KillStatementContext | undefined;
    loadIndexIntoCache(): LoadIndexIntoCacheContext | undefined;
    resetStatement(): ResetStatementContext | undefined;
    resetPersist(): ResetPersistContext | undefined;
    resetAllChannel(): ResetAllChannelContext | undefined;
    reStartStatement(): ReStartStatementContext | undefined;
    shutdownStatement(): ShutdownStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UtilityStatementContext extends ParserRuleContext {
    simpleDescribeStatement(): SimpleDescribeStatementContext | undefined;
    fullDescribeStatement(): FullDescribeStatementContext | undefined;
    analyzeDescribeStatement(): AnalyzeDescribeStatementContext | undefined;
    helpStatement(): HelpStatementContext | undefined;
    useStatement(): UseStatementContext | undefined;
    signalStatement(): SignalStatementContext | undefined;
    resignalStatement(): ResignalStatementContext | undefined;
    diagnosticsStatement(): DiagnosticsStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateDatabaseContext extends ParserRuleContext {
    _dbFormat: Token;
    KW_CREATE(): TerminalNode;
    databaseNameCreate(): DatabaseNameCreateContext;
    KW_DATABASE(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    createDatabaseOption(): CreateDatabaseOptionContext[];
    createDatabaseOption(i: number): CreateDatabaseOptionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateEventContext extends ParserRuleContext {
    _event_name: FullIdContext;
    KW_CREATE(): TerminalNode;
    KW_EVENT(): TerminalNode;
    KW_ON(): TerminalNode[];
    KW_ON(i: number): TerminalNode;
    KW_SCHEDULE(): TerminalNode;
    scheduleExpression(): ScheduleExpressionContext;
    KW_DO(): TerminalNode;
    routineBody(): RoutineBodyContext;
    fullId(): FullIdContext;
    ownerStatement(): OwnerStatementContext | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    KW_COMPLETION(): TerminalNode | undefined;
    KW_PRESERVE(): TerminalNode | undefined;
    enableType(): EnableTypeContext | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateIndexContext extends ParserRuleContext {
    _intimeAction: Token;
    _indexCategory: Token;
    _algType: Token;
    _lockType: Token;
    KW_CREATE(): TerminalNode;
    KW_INDEX(): TerminalNode;
    indexNameCreate(): IndexNameCreateContext;
    KW_ON(): TerminalNode;
    tableName(): TableNameContext;
    indexColumnNames(): IndexColumnNamesContext;
    indexType(): IndexTypeContext | undefined;
    indexOption(): IndexOptionContext[];
    indexOption(i: number): IndexOptionContext;
    KW_ALGORITHM(): TerminalNode[];
    KW_ALGORITHM(i: number): TerminalNode;
    KW_LOCK(): TerminalNode[];
    KW_LOCK(i: number): TerminalNode;
    KW_ONLINE(): TerminalNode | undefined;
    KW_OFFLINE(): TerminalNode | undefined;
    KW_UNIQUE(): TerminalNode | undefined;
    KW_FULLTEXT(): TerminalNode | undefined;
    KW_SPATIAL(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode[];
    KW_DEFAULT(i: number): TerminalNode;
    KW_INPLACE(): TerminalNode[];
    KW_INPLACE(i: number): TerminalNode;
    KW_COPY(): TerminalNode[];
    KW_COPY(i: number): TerminalNode;
    KW_NONE(): TerminalNode[];
    KW_NONE(i: number): TerminalNode;
    KW_SHARED(): TerminalNode[];
    KW_SHARED(i: number): TerminalNode;
    KW_EXCLUSIVE(): TerminalNode[];
    KW_EXCLUSIVE(i: number): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateLogfileGroupContext extends ParserRuleContext {
    _logfileGroupName: UidContext;
    _undoFile: Token;
    _initSize: FileSizeLiteralContext;
    _undoSize: FileSizeLiteralContext;
    _redoSize: FileSizeLiteralContext;
    _nodegroup: UidContext;
    _comment: Token;
    KW_CREATE(): TerminalNode;
    KW_LOGFILE(): TerminalNode;
    KW_GROUP(): TerminalNode;
    KW_ADD(): TerminalNode;
    KW_UNDOFILE(): TerminalNode;
    KW_ENGINE(): TerminalNode;
    engineName(): EngineNameContext;
    uid(): UidContext[];
    uid(i: number): UidContext;
    STRING_LITERAL(): TerminalNode[];
    STRING_LITERAL(i: number): TerminalNode;
    KW_INITIAL_SIZE(): TerminalNode | undefined;
    KW_UNDO_BUFFER_SIZE(): TerminalNode | undefined;
    KW_REDO_BUFFER_SIZE(): TerminalNode | undefined;
    KW_NODEGROUP(): TerminalNode | undefined;
    KW_WAIT(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    fileSizeLiteral(): FileSizeLiteralContext[];
    fileSizeLiteral(i: number): FileSizeLiteralContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateProcedureContext extends ParserRuleContext {
    _sp_name: FullIdContext;
    KW_CREATE(): TerminalNode;
    KW_PROCEDURE(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    routineBody(): RoutineBodyContext;
    fullId(): FullIdContext;
    ownerStatement(): OwnerStatementContext | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    procedureParameter(): ProcedureParameterContext[];
    procedureParameter(i: number): ProcedureParameterContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    routineOption(): RoutineOptionContext[];
    routineOption(i: number): RoutineOptionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateRoleContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_ROLE(): TerminalNode;
    userOrRoleNames(): UserOrRoleNamesContext;
    ifNotExists(): IfNotExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateServerContext extends ParserRuleContext {
    _servername: UidContext;
    _wrapperName: Token;
    KW_CREATE(): TerminalNode;
    KW_SERVER(): TerminalNode;
    KW_FOREIGN(): TerminalNode;
    KW_DATA(): TerminalNode;
    KW_WRAPPER(): TerminalNode;
    KW_OPTIONS(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    serverOption(): ServerOptionContext[];
    serverOption(i: number): ServerOptionContext;
    RR_BRACKET(): TerminalNode;
    uid(): UidContext;
    KW_MYSQL(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateTableContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: CreateTableContext): void;
}
export declare class CopyCreateTableContext extends CreateTableContext {
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    createDefinitions(): CreateDefinitionsContext;
    KW_TEMPORARY(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    tableOption(): TableOptionContext[];
    tableOption(i: number): TableOptionContext;
    partitionDefinitions(): PartitionDefinitionsContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: CreateTableContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ColumnCreateTableContext extends CreateTableContext {
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    selectStatement(): SelectStatementContext;
    KW_TEMPORARY(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    createDefinitions(): CreateDefinitionsContext | undefined;
    tableOption(): TableOptionContext[];
    tableOption(i: number): TableOptionContext;
    partitionDefinitions(): PartitionDefinitionsContext | undefined;
    KW_AS(): TerminalNode | undefined;
    KW_IGNORE(): TerminalNode | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: CreateTableContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class QueryCreateTableContext extends CreateTableContext {
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    KW_LIKE(): TerminalNode | undefined;
    tableName(): TableNameContext | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    KW_TEMPORARY(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    constructor(ctx: CreateTableContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateTablespaceInnodbContext extends ParserRuleContext {
    _datafile: Token;
    _autoextendSize: FileSizeLiteralContext;
    _fileBlockSize: FileSizeLiteralContext;
    KW_CREATE(): TerminalNode;
    KW_TABLESPACE(): TerminalNode;
    tablespaceNameCreate(): TablespaceNameCreateContext;
    KW_UNDO(): TerminalNode | undefined;
    KW_ADD(): TerminalNode | undefined;
    KW_DATAFILE(): TerminalNode | undefined;
    KW_AUTOEXTEND_SIZE(): TerminalNode | undefined;
    KW_FILE_BLOCK_SIZE(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    KW_ENGINE(): TerminalNode | undefined;
    engineName(): EngineNameContext | undefined;
    KW_ENGINE_ATTRIBUTE(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode[];
    STRING_LITERAL(i: number): TerminalNode;
    fileSizeLiteral(): FileSizeLiteralContext[];
    fileSizeLiteral(i: number): FileSizeLiteralContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateTablespaceNdbContext extends ParserRuleContext {
    _datafile: Token;
    _logfileGroupName: UidContext;
    _extentSize: FileSizeLiteralContext;
    _initialSize: FileSizeLiteralContext;
    _autoextendSize: FileSizeLiteralContext;
    _maxSize: FileSizeLiteralContext;
    _nodegroup: UidContext;
    _comment: Token;
    KW_CREATE(): TerminalNode;
    KW_TABLESPACE(): TerminalNode;
    tablespaceNameCreate(): TablespaceNameCreateContext;
    KW_ADD(): TerminalNode;
    KW_DATAFILE(): TerminalNode;
    KW_USE(): TerminalNode;
    KW_LOGFILE(): TerminalNode;
    KW_GROUP(): TerminalNode;
    KW_ENGINE(): TerminalNode;
    engineName(): EngineNameContext;
    STRING_LITERAL(): TerminalNode[];
    STRING_LITERAL(i: number): TerminalNode;
    uid(): UidContext[];
    uid(i: number): UidContext;
    KW_UNDO(): TerminalNode | undefined;
    KW_EXTENT_SIZE(): TerminalNode | undefined;
    KW_INITIAL_SIZE(): TerminalNode | undefined;
    KW_AUTOEXTEND_SIZE(): TerminalNode | undefined;
    KW_MAX_SIZE(): TerminalNode | undefined;
    KW_NODEGROUP(): TerminalNode | undefined;
    KW_WAIT(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    fileSizeLiteral(): FileSizeLiteralContext[];
    fileSizeLiteral(i: number): FileSizeLiteralContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateTriggerContext extends ParserRuleContext {
    _trigger_name: FullIdContext;
    _triggerTime: Token;
    _triggerEvent: Token;
    _triggerPlace: Token;
    _other_trigger_name: FullIdContext;
    KW_CREATE(): TerminalNode;
    KW_TRIGGER(): TerminalNode;
    KW_ON(): TerminalNode;
    tableName(): TableNameContext;
    KW_FOR(): TerminalNode;
    KW_EACH(): TerminalNode;
    KW_ROW(): TerminalNode;
    routineBody(): RoutineBodyContext;
    fullId(): FullIdContext[];
    fullId(i: number): FullIdContext;
    KW_BEFORE(): TerminalNode | undefined;
    KW_AFTER(): TerminalNode | undefined;
    KW_INSERT(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    KW_DELETE(): TerminalNode | undefined;
    ownerStatement(): OwnerStatementContext | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    KW_FOLLOWS(): TerminalNode | undefined;
    KW_PRECEDES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class WithClauseContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    commonTableExpressions(): CommonTableExpressionsContext;
    KW_RECURSIVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CommonTableExpressionsContext extends ParserRuleContext {
    _cteName: UidContext;
    _cteColumnName: UidContext;
    KW_AS(): TerminalNode;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    dmlStatement(): DmlStatementContext;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    uid(): UidContext[];
    uid(i: number): UidContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    commonTableExpressions(): CommonTableExpressionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateViewContext extends ParserRuleContext {
    _algType: Token;
    _secContext: Token;
    _checkOption: Token;
    KW_CREATE(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewNameCreate(): ViewNameCreateContext;
    KW_AS(): TerminalNode;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    selectStatement(): SelectStatementContext | undefined;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    orReplace(): OrReplaceContext | undefined;
    KW_ALGORITHM(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    ownerStatement(): OwnerStatementContext | undefined;
    KW_SQL(): TerminalNode | undefined;
    KW_SECURITY(): TerminalNode | undefined;
    columnNames(): ColumnNamesContext | undefined;
    KW_UNDEFINED(): TerminalNode | undefined;
    KW_MERGE(): TerminalNode | undefined;
    KW_TEMPTABLE(): TerminalNode | undefined;
    KW_DEFINER(): TerminalNode | undefined;
    KW_INVOKER(): TerminalNode | undefined;
    withClause(): WithClauseContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_CHECK(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_CASCADED(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateDatabaseOptionContext extends ParserRuleContext {
    charSet(): CharSetContext | undefined;
    charsetName(): CharsetNameContext | undefined;
    KW_DEFAULT(): TerminalNode[];
    KW_DEFAULT(i: number): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    KW_COLLATE(): TerminalNode | undefined;
    collationName(): CollationNameContext | undefined;
    KW_ENCRYPTION(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_READ(): TerminalNode | undefined;
    KW_ONLY(): TerminalNode | undefined;
    ZERO_DECIMAL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CharSetContext extends ParserRuleContext {
    KW_CHARACTER(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_CHARSET(): TerminalNode | undefined;
    KW_CHAR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CurrentUserExpressionContext extends ParserRuleContext {
    KW_CURRENT_USER(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class OwnerStatementContext extends ParserRuleContext {
    KW_DEFINER(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    userName(): UserNameContext | undefined;
    currentUserExpression(): CurrentUserExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ScheduleExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ScheduleExpressionContext): void;
}
export declare class PreciseScheduleContext extends ScheduleExpressionContext {
    KW_AT(): TerminalNode;
    timestampValue(): TimestampValueContext;
    intervalExpr(): IntervalExprContext[];
    intervalExpr(i: number): IntervalExprContext;
    constructor(ctx: ScheduleExpressionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IntervalScheduleContext extends ScheduleExpressionContext {
    _startTimestamp: TimestampValueContext;
    _intervalExpr: IntervalExprContext;
    _startIntervals: IntervalExprContext[];
    _endTimestamp: TimestampValueContext;
    _endIntervals: IntervalExprContext[];
    KW_EVERY(): TerminalNode;
    intervalType(): IntervalTypeContext;
    decimalLiteral(): DecimalLiteralContext | undefined;
    expression(): ExpressionContext | undefined;
    KW_STARTS(): TerminalNode | undefined;
    KW_ENDS(): TerminalNode | undefined;
    timestampValue(): TimestampValueContext[];
    timestampValue(i: number): TimestampValueContext;
    intervalExpr(): IntervalExprContext[];
    intervalExpr(i: number): IntervalExprContext;
    constructor(ctx: ScheduleExpressionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TimestampValueContext extends ParserRuleContext {
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IntervalExprContext extends ParserRuleContext {
    PLUS(): TerminalNode;
    KW_INTERVAL(): TerminalNode;
    intervalType(): IntervalTypeContext;
    decimalLiteral(): DecimalLiteralContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IntervalTypeContext extends ParserRuleContext {
    intervalTypeBase(): IntervalTypeBaseContext | undefined;
    KW_YEAR(): TerminalNode | undefined;
    KW_YEAR_MONTH(): TerminalNode | undefined;
    KW_DAY_HOUR(): TerminalNode | undefined;
    KW_DAY_MINUTE(): TerminalNode | undefined;
    KW_DAY_SECOND(): TerminalNode | undefined;
    KW_HOUR_MINUTE(): TerminalNode | undefined;
    KW_HOUR_SECOND(): TerminalNode | undefined;
    KW_MINUTE_SECOND(): TerminalNode | undefined;
    KW_SECOND_MICROSECOND(): TerminalNode | undefined;
    KW_MINUTE_MICROSECOND(): TerminalNode | undefined;
    KW_HOUR_MICROSECOND(): TerminalNode | undefined;
    KW_DAY_MICROSECOND(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class EnableTypeContext extends ParserRuleContext {
    KW_ENABLE(): TerminalNode | undefined;
    KW_DISABLE(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_SLAVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IndexTypeContext extends ParserRuleContext {
    KW_USING(): TerminalNode;
    KW_BTREE(): TerminalNode | undefined;
    KW_HASH(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IndexOptionContext extends ParserRuleContext {
    _parserName: UidContext;
    KW_KEY_BLOCK_SIZE(): TerminalNode | undefined;
    fileSizeLiteral(): FileSizeLiteralContext | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    indexType(): IndexTypeContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_PARSER(): TerminalNode | undefined;
    uid(): UidContext | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_VISIBLE(): TerminalNode | undefined;
    KW_INVISIBLE(): TerminalNode | undefined;
    KW_ENGINE_ATTRIBUTE(): TerminalNode | undefined;
    KW_SECONDARY_ENGINE_ATTRIBUTE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ProcedureParameterContext extends ParserRuleContext {
    _direction: Token;
    _paramName: UidContext;
    dataType(): DataTypeContext;
    uid(): UidContext;
    KW_IN(): TerminalNode | undefined;
    KW_OUT(): TerminalNode | undefined;
    KW_INOUT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RoutineOptionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: RoutineOptionContext): void;
}
export declare class RoutineCommentContext extends RoutineOptionContext {
    KW_COMMENT(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(ctx: RoutineOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RoutineLanguageContext extends RoutineOptionContext {
    KW_LANGUAGE(): TerminalNode;
    KW_SQL(): TerminalNode;
    constructor(ctx: RoutineOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RoutineBehaviorContext extends RoutineOptionContext {
    KW_DETERMINISTIC(): TerminalNode;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: RoutineOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RoutineDataContext extends RoutineOptionContext {
    KW_CONTAINS(): TerminalNode | undefined;
    KW_SQL(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_READS(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_MODIFIES(): TerminalNode | undefined;
    constructor(ctx: RoutineOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RoutineSecurityContext extends RoutineOptionContext {
    _context: Token;
    KW_SQL(): TerminalNode;
    KW_SECURITY(): TerminalNode;
    KW_DEFINER(): TerminalNode | undefined;
    KW_INVOKER(): TerminalNode | undefined;
    constructor(ctx: RoutineOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ServerOptionContext extends ParserRuleContext {
    KW_HOST(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_PASSWORD(): TerminalNode | undefined;
    KW_SOCKET(): TerminalNode | undefined;
    KW_OWNER(): TerminalNode | undefined;
    KW_PORT(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateDefinitionsContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    createDefinition(): CreateDefinitionContext[];
    createDefinition(i: number): CreateDefinitionContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateDefinitionContext extends ParserRuleContext {
    columnName(): ColumnNameContext | undefined;
    columnDefinition(): ColumnDefinitionContext | undefined;
    indexColumnNames(): IndexColumnNamesContext | undefined;
    KW_INDEX(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    indexName(): IndexNameContext | undefined;
    indexType(): IndexTypeContext | undefined;
    indexOption(): IndexOptionContext[];
    indexOption(i: number): IndexOptionContext;
    KW_FULLTEXT(): TerminalNode | undefined;
    KW_SPATIAL(): TerminalNode | undefined;
    KW_PRIMARY(): TerminalNode | undefined;
    constraintSymbol(): ConstraintSymbolContext | undefined;
    KW_UNIQUE(): TerminalNode | undefined;
    KW_FOREIGN(): TerminalNode | undefined;
    referenceDefinition(): ReferenceDefinitionContext | undefined;
    KW_CHECK(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    checkConstraintDefinition(): CheckConstraintDefinitionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CheckConstraintDefinitionContext extends ParserRuleContext {
    KW_CHECK(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext;
    RR_BRACKET(): TerminalNode;
    constraintSymbol(): ConstraintSymbolContext | undefined;
    KW_ENFORCED(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ConstraintSymbolContext extends ParserRuleContext {
    _symbol: UidContext;
    KW_CONSTRAINT(): TerminalNode;
    uid(): UidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ColumnDefinitionContext extends ParserRuleContext {
    dataType(): DataTypeContext;
    columnConstraint(): ColumnConstraintContext[];
    columnConstraint(i: number): ColumnConstraintContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ColumnConstraintContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ColumnConstraintContext): void;
}
export declare class NullColumnConstraintContext extends ColumnConstraintContext {
    nullNotnull(): NullNotnullContext;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DefaultColumnConstraintContext extends ColumnConstraintContext {
    KW_DEFAULT(): TerminalNode;
    defaultValue(): DefaultValueContext;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class VisibilityColumnConstraintContext extends ColumnConstraintContext {
    KW_VISIBLE(): TerminalNode;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class InvisibilityColumnConstraintContext extends ColumnConstraintContext {
    KW_INVISIBLE(): TerminalNode;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AutoIncrementColumnConstraintContext extends ColumnConstraintContext {
    KW_AUTO_INCREMENT(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    currentTimestamp(): CurrentTimestampContext | undefined;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PrimaryKeyColumnConstraintContext extends ColumnConstraintContext {
    KW_KEY(): TerminalNode;
    KW_PRIMARY(): TerminalNode | undefined;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UniqueKeyColumnConstraintContext extends ColumnConstraintContext {
    KW_UNIQUE(): TerminalNode;
    KW_KEY(): TerminalNode | undefined;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CommentColumnConstraintContext extends ColumnConstraintContext {
    KW_COMMENT(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FormatColumnConstraintContext extends ColumnConstraintContext {
    _colformat: Token;
    KW_COLUMN_FORMAT(): TerminalNode;
    KW_FIXED(): TerminalNode | undefined;
    KW_DYNAMIC(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class StorageColumnConstraintContext extends ColumnConstraintContext {
    _storageval: Token;
    KW_STORAGE(): TerminalNode;
    KW_DISK(): TerminalNode | undefined;
    KW_MEMORY(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ReferenceColumnConstraintContext extends ColumnConstraintContext {
    referenceDefinition(): ReferenceDefinitionContext;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CollateColumnConstraintContext extends ColumnConstraintContext {
    KW_COLLATE(): TerminalNode;
    collationName(): CollationNameContext;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class GeneratedColumnConstraintContext extends ColumnConstraintContext {
    KW_AS(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext;
    RR_BRACKET(): TerminalNode;
    KW_GENERATED(): TerminalNode | undefined;
    KW_ALWAYS(): TerminalNode | undefined;
    KW_VIRTUAL(): TerminalNode | undefined;
    KW_STORED(): TerminalNode | undefined;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SerialDefaultColumnConstraintContext extends ColumnConstraintContext {
    KW_SERIAL(): TerminalNode;
    KW_DEFAULT(): TerminalNode;
    KW_VALUE(): TerminalNode;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CheckExprContext extends ColumnConstraintContext {
    checkConstraintDefinition(): CheckConstraintDefinitionContext;
    constructor(ctx: ColumnConstraintContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ReferenceDefinitionContext extends ParserRuleContext {
    _matchType: Token;
    KW_REFERENCES(): TerminalNode;
    tableName(): TableNameContext;
    indexColumnNames(): IndexColumnNamesContext | undefined;
    KW_MATCH(): TerminalNode | undefined;
    referenceAction(): ReferenceActionContext | undefined;
    KW_FULL(): TerminalNode | undefined;
    KW_PARTIAL(): TerminalNode | undefined;
    KW_SIMPLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ReferenceActionContext extends ParserRuleContext {
    _onDelete: ReferenceControlTypeContext;
    _onUpdate: ReferenceControlTypeContext;
    KW_ON(): TerminalNode[];
    KW_ON(i: number): TerminalNode;
    KW_DELETE(): TerminalNode | undefined;
    referenceControlType(): ReferenceControlTypeContext[];
    referenceControlType(i: number): ReferenceControlTypeContext;
    KW_UPDATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ReferenceControlTypeContext extends ParserRuleContext {
    KW_RESTRICT(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_NULL_LITERAL(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_ACTION(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: TableOptionContext): void;
}
export declare class TableOptionEngineContext extends TableOptionContext {
    KW_ENGINE(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    engineName(): EngineNameContext | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionEngineAttributeContext extends TableOptionContext {
    KW_ENGINE_ATTRIBUTE(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionAutoextendSizeContext extends TableOptionContext {
    KW_AUTOEXTEND_SIZE(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionAutoIncrementContext extends TableOptionContext {
    KW_AUTO_INCREMENT(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionAverageContext extends TableOptionContext {
    KW_AVG_ROW_LENGTH(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionCharsetContext extends TableOptionContext {
    charSet(): CharSetContext;
    charsetName(): CharsetNameContext | undefined;
    KW_DEFAULT(): TerminalNode[];
    KW_DEFAULT(i: number): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionChecksumContext extends TableOptionContext {
    _boolValue: Token;
    KW_CHECKSUM(): TerminalNode | undefined;
    KW_PAGE_CHECKSUM(): TerminalNode | undefined;
    ZERO_DECIMAL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionCollateContext extends TableOptionContext {
    KW_COLLATE(): TerminalNode;
    collationName(): CollationNameContext;
    KW_DEFAULT(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionCommentContext extends TableOptionContext {
    KW_COMMENT(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionCompressionContext extends TableOptionContext {
    KW_COMPRESSION(): TerminalNode;
    STRING_LITERAL(): TerminalNode | undefined;
    ID(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionConnectionContext extends TableOptionContext {
    KW_CONNECTION(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionDataDirectoryContext extends TableOptionContext {
    KW_DIRECTORY(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    KW_DATA(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionDelayContext extends TableOptionContext {
    _boolValue: Token;
    KW_DELAY_KEY_WRITE(): TerminalNode;
    ZERO_DECIMAL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionEncryptionContext extends TableOptionContext {
    KW_ENCRYPTION(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionPageCompressedContext extends TableOptionContext {
    KW_PAGE_COMPRESSED(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    ZERO_DECIMAL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionPageCompressionLevelContext extends TableOptionContext {
    decimalLiteral(): DecimalLiteralContext;
    KW_PAGE_COMPRESSION_LEVEL(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionEncryptionKeyIdContext extends TableOptionContext {
    KW_ENCRYPTION_KEY_ID(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionIndexDirectoryContext extends TableOptionContext {
    KW_INDEX(): TerminalNode;
    KW_DIRECTORY(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionInsertMethodContext extends TableOptionContext {
    _insertMethod: Token;
    KW_INSERT_METHOD(): TerminalNode;
    KW_NO(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionKeyBlockSizeContext extends TableOptionContext {
    KW_KEY_BLOCK_SIZE(): TerminalNode;
    fileSizeLiteral(): FileSizeLiteralContext;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionMaxRowsContext extends TableOptionContext {
    KW_MAX_ROWS(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionMinRowsContext extends TableOptionContext {
    KW_MIN_ROWS(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionPackKeysContext extends TableOptionContext {
    _extBoolValue: Token;
    KW_PACK_KEYS(): TerminalNode;
    ZERO_DECIMAL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionPasswordContext extends TableOptionContext {
    KW_PASSWORD(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionRowFormatContext extends TableOptionContext {
    _rowFormat: Token;
    KW_ROW_FORMAT(): TerminalNode;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_DYNAMIC(): TerminalNode | undefined;
    KW_FIXED(): TerminalNode | undefined;
    KW_COMPRESSED(): TerminalNode | undefined;
    KW_REDUNDANT(): TerminalNode | undefined;
    KW_COMPACT(): TerminalNode | undefined;
    ID(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionStartTransactionContext extends TableOptionContext {
    KW_START(): TerminalNode;
    KW_TRANSACTION(): TerminalNode;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionSecondaryEngineAttributeContext extends TableOptionContext {
    KW_SECONDARY_ENGINE_ATTRIBUTE(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionRecalculationContext extends TableOptionContext {
    _extBoolValue: Token;
    KW_STATS_AUTO_RECALC(): TerminalNode;
    KW_DEFAULT(): TerminalNode | undefined;
    ZERO_DECIMAL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionPersistentContext extends TableOptionContext {
    _extBoolValue: Token;
    KW_STATS_PERSISTENT(): TerminalNode;
    KW_DEFAULT(): TerminalNode | undefined;
    ZERO_DECIMAL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionSamplePageContext extends TableOptionContext {
    KW_STATS_SAMPLE_PAGES(): TerminalNode;
    KW_DEFAULT(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionTablespaceContext extends TableOptionContext {
    KW_TABLESPACE(): TerminalNode | undefined;
    tablespaceName(): TablespaceNameContext | undefined;
    tablespaceStorage(): TablespaceStorageContext | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionTableTypeContext extends TableOptionContext {
    KW_TABLE_TYPE(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    tableType(): TableTypeContext;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionTransactionalContext extends TableOptionContext {
    KW_TRANSACTIONAL(): TerminalNode;
    ZERO_DECIMAL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableOptionUnionContext extends TableOptionContext {
    KW_UNION(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    tableNames(): TableNamesContext;
    RR_BRACKET(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: TableOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableTypeContext extends ParserRuleContext {
    KW_MYSQL(): TerminalNode | undefined;
    KW_ODBC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TablespaceStorageContext extends ParserRuleContext {
    KW_STORAGE(): TerminalNode;
    KW_DISK(): TerminalNode | undefined;
    KW_MEMORY(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionDefinitionsContext extends ParserRuleContext {
    _count: DecimalLiteralContext;
    _subCount: DecimalLiteralContext;
    KW_PARTITION(): TerminalNode;
    KW_BY(): TerminalNode[];
    KW_BY(i: number): TerminalNode;
    partitionFunctionDefinition(): PartitionFunctionDefinitionContext;
    KW_PARTITIONS(): TerminalNode | undefined;
    KW_SUBPARTITION(): TerminalNode | undefined;
    subpartitionFunctionDefinition(): SubpartitionFunctionDefinitionContext | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    partitionDefinition(): PartitionDefinitionContext[];
    partitionDefinition(i: number): PartitionDefinitionContext;
    RR_BRACKET(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    KW_SUBPARTITIONS(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionFunctionDefinitionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PartitionFunctionDefinitionContext): void;
}
export declare class PartitionFunctionHashContext extends PartitionFunctionDefinitionContext {
    KW_HASH(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext;
    RR_BRACKET(): TerminalNode;
    KW_LINEAR(): TerminalNode | undefined;
    constructor(ctx: PartitionFunctionDefinitionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionFunctionKeyContext extends PartitionFunctionDefinitionContext {
    _algType: Token;
    KW_KEY(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    KW_LINEAR(): TerminalNode | undefined;
    KW_ALGORITHM(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    columnNames(): ColumnNamesContext | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    TWO_DECIMAL(): TerminalNode | undefined;
    constructor(ctx: PartitionFunctionDefinitionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionFunctionRangeContext extends PartitionFunctionDefinitionContext {
    KW_RANGE(): TerminalNode;
    LR_BRACKET(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    columnNames(): ColumnNamesContext | undefined;
    constructor(ctx: PartitionFunctionDefinitionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionFunctionListContext extends PartitionFunctionDefinitionContext {
    KW_LIST(): TerminalNode;
    LR_BRACKET(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    columnNames(): ColumnNamesContext | undefined;
    constructor(ctx: PartitionFunctionDefinitionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SubpartitionFunctionDefinitionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: SubpartitionFunctionDefinitionContext): void;
}
export declare class SubPartitionFunctionHashContext extends SubpartitionFunctionDefinitionContext {
    KW_HASH(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext;
    RR_BRACKET(): TerminalNode;
    KW_LINEAR(): TerminalNode | undefined;
    constructor(ctx: SubpartitionFunctionDefinitionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SubPartitionFunctionKeyContext extends SubpartitionFunctionDefinitionContext {
    _algType: Token;
    KW_KEY(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    columnNames(): ColumnNamesContext;
    RR_BRACKET(): TerminalNode;
    KW_LINEAR(): TerminalNode | undefined;
    KW_ALGORITHM(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    TWO_DECIMAL(): TerminalNode | undefined;
    constructor(ctx: SubpartitionFunctionDefinitionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionDefinitionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PartitionDefinitionContext): void;
}
export declare class PartitionComparisonContext extends PartitionDefinitionContext {
    KW_PARTITION(): TerminalNode;
    partitionName(): PartitionNameContext;
    KW_VALUES(): TerminalNode;
    KW_LESS(): TerminalNode;
    KW_THAN(): TerminalNode;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    partitionDefinerAtom(): PartitionDefinerAtomContext[];
    partitionDefinerAtom(i: number): PartitionDefinerAtomContext;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    partitionOption(): PartitionOptionContext[];
    partitionOption(i: number): PartitionOptionContext;
    subpartitionDefinition(): SubpartitionDefinitionContext[];
    subpartitionDefinition(i: number): SubpartitionDefinitionContext;
    constructor(ctx: PartitionDefinitionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionListAtomContext extends PartitionDefinitionContext {
    KW_PARTITION(): TerminalNode;
    partitionName(): PartitionNameContext;
    KW_VALUES(): TerminalNode;
    KW_IN(): TerminalNode;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    partitionDefinerAtom(): PartitionDefinerAtomContext[];
    partitionDefinerAtom(i: number): PartitionDefinerAtomContext;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    partitionOption(): PartitionOptionContext[];
    partitionOption(i: number): PartitionOptionContext;
    subpartitionDefinition(): SubpartitionDefinitionContext[];
    subpartitionDefinition(i: number): SubpartitionDefinitionContext;
    constructor(ctx: PartitionDefinitionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionListVectorContext extends PartitionDefinitionContext {
    KW_PARTITION(): TerminalNode;
    partitionName(): PartitionNameContext;
    KW_VALUES(): TerminalNode;
    KW_IN(): TerminalNode;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    partitionDefinerVector(): PartitionDefinerVectorContext[];
    partitionDefinerVector(i: number): PartitionDefinerVectorContext;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    partitionOption(): PartitionOptionContext[];
    partitionOption(i: number): PartitionOptionContext;
    subpartitionDefinition(): SubpartitionDefinitionContext[];
    subpartitionDefinition(i: number): SubpartitionDefinitionContext;
    constructor(ctx: PartitionDefinitionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionSimpleContext extends PartitionDefinitionContext {
    KW_PARTITION(): TerminalNode;
    partitionName(): PartitionNameContext;
    partitionOption(): PartitionOptionContext[];
    partitionOption(i: number): PartitionOptionContext;
    LR_BRACKET(): TerminalNode | undefined;
    subpartitionDefinition(): SubpartitionDefinitionContext[];
    subpartitionDefinition(i: number): SubpartitionDefinitionContext;
    RR_BRACKET(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: PartitionDefinitionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionDefinerAtomContext extends ParserRuleContext {
    constant(): ConstantContext | undefined;
    expression(): ExpressionContext | undefined;
    KW_MAXVALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionDefinerVectorContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    partitionDefinerAtom(): PartitionDefinerAtomContext[];
    partitionDefinerAtom(i: number): PartitionDefinerAtomContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SubpartitionDefinitionContext extends ParserRuleContext {
    _logicalName: UidContext;
    KW_SUBPARTITION(): TerminalNode;
    uid(): UidContext;
    partitionOption(): PartitionOptionContext[];
    partitionOption(i: number): PartitionOptionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionOptionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PartitionOptionContext): void;
}
export declare class PartitionOptionEngineContext extends PartitionOptionContext {
    KW_ENGINE(): TerminalNode;
    engineName(): EngineNameContext;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_STORAGE(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: PartitionOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionOptionCommentContext extends PartitionOptionContext {
    _comment: Token;
    KW_COMMENT(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: PartitionOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionOptionDataDirectoryContext extends PartitionOptionContext {
    _dataDirectory: Token;
    KW_DATA(): TerminalNode;
    KW_DIRECTORY(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: PartitionOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionOptionIndexDirectoryContext extends PartitionOptionContext {
    _indexDirectory: Token;
    KW_INDEX(): TerminalNode;
    KW_DIRECTORY(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: PartitionOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionOptionMaxRowsContext extends PartitionOptionContext {
    _maxRows: DecimalLiteralContext;
    KW_MAX_ROWS(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: PartitionOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionOptionMinRowsContext extends PartitionOptionContext {
    _minRows: DecimalLiteralContext;
    KW_MIN_ROWS(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: PartitionOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionOptionTablespaceContext extends PartitionOptionContext {
    KW_TABLESPACE(): TerminalNode;
    tablespaceName(): TablespaceNameContext;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: PartitionOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionOptionNodeGroupContext extends PartitionOptionContext {
    _nodegroup: UidContext;
    KW_NODEGROUP(): TerminalNode;
    uid(): UidContext;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: PartitionOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterDatabaseContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: AlterDatabaseContext): void;
}
export declare class AlterSimpleDatabaseContext extends AlterDatabaseContext {
    _dbFormat: Token;
    KW_ALTER(): TerminalNode;
    KW_DATABASE(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    databaseName(): DatabaseNameContext | undefined;
    createDatabaseOption(): CreateDatabaseOptionContext[];
    createDatabaseOption(i: number): CreateDatabaseOptionContext;
    constructor(ctx: AlterDatabaseContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterUpgradeNameContext extends AlterDatabaseContext {
    _dbFormat: Token;
    KW_ALTER(): TerminalNode;
    databaseName(): DatabaseNameContext;
    KW_UPGRADE(): TerminalNode;
    KW_DATA(): TerminalNode;
    KW_DIRECTORY(): TerminalNode;
    KW_NAME(): TerminalNode;
    KW_DATABASE(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    constructor(ctx: AlterDatabaseContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterEventContext extends ParserRuleContext {
    _event_name: FullIdContext;
    _new_event_name: FullIdContext;
    KW_ALTER(): TerminalNode;
    KW_EVENT(): TerminalNode;
    fullId(): FullIdContext[];
    fullId(i: number): FullIdContext;
    ownerStatement(): OwnerStatementContext | undefined;
    KW_ON(): TerminalNode[];
    KW_ON(i: number): TerminalNode;
    KW_SCHEDULE(): TerminalNode | undefined;
    scheduleExpression(): ScheduleExpressionContext | undefined;
    KW_COMPLETION(): TerminalNode | undefined;
    KW_PRESERVE(): TerminalNode | undefined;
    KW_RENAME(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    enableType(): EnableTypeContext | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_DO(): TerminalNode | undefined;
    routineBody(): RoutineBodyContext | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterFunctionContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    functionName(): FunctionNameContext;
    routineOption(): RoutineOptionContext[];
    routineOption(i: number): RoutineOptionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterInstanceContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_INSTANCE(): TerminalNode;
    KW_ROTATE(): TerminalNode;
    KW_INNODB(): TerminalNode;
    KW_MASTER(): TerminalNode;
    KW_KEY(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterLogfileGroupContext extends ParserRuleContext {
    _logfileGroupName: UidContext;
    KW_ALTER(): TerminalNode;
    KW_LOGFILE(): TerminalNode;
    KW_GROUP(): TerminalNode;
    KW_ADD(): TerminalNode;
    KW_UNDOFILE(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    KW_ENGINE(): TerminalNode;
    engineName(): EngineNameContext;
    uid(): UidContext;
    KW_INITIAL_SIZE(): TerminalNode | undefined;
    fileSizeLiteral(): FileSizeLiteralContext | undefined;
    KW_WAIT(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterProcedureContext extends ParserRuleContext {
    _proc_name: FullIdContext;
    KW_ALTER(): TerminalNode;
    KW_PROCEDURE(): TerminalNode;
    fullId(): FullIdContext;
    routineOption(): RoutineOptionContext[];
    routineOption(i: number): RoutineOptionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterServerContext extends ParserRuleContext {
    _serverName: UidContext;
    KW_ALTER(): TerminalNode;
    KW_SERVER(): TerminalNode;
    KW_OPTIONS(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    serverOption(): ServerOptionContext[];
    serverOption(i: number): ServerOptionContext;
    RR_BRACKET(): TerminalNode;
    uid(): UidContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterTableContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    alterOption(): AlterOptionContext[];
    alterOption(i: number): AlterOptionContext;
    alterPartitionSpecification(): AlterPartitionSpecificationContext[];
    alterPartitionSpecification(i: number): AlterPartitionSpecificationContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterTablespaceContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLESPACE(): TerminalNode;
    tablespaceName(): TablespaceNameContext;
    KW_DATAFILE(): TerminalNode;
    STRING_LITERAL(): TerminalNode[];
    STRING_LITERAL(i: number): TerminalNode;
    KW_ADD(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_UNDO(): TerminalNode | undefined;
    KW_INITIAL_SIZE(): TerminalNode | undefined;
    fileSizeLiteral(): FileSizeLiteralContext[];
    fileSizeLiteral(i: number): FileSizeLiteralContext;
    KW_WAIT(): TerminalNode | undefined;
    KW_RENAME(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    tablespaceNameCreate(): TablespaceNameCreateContext | undefined;
    KW_AUTOEXTEND_SIZE(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_ENCRYPTION(): TerminalNode | undefined;
    KW_ENGINE(): TerminalNode | undefined;
    engineName(): EngineNameContext | undefined;
    KW_ENGINE_ATTRIBUTE(): TerminalNode | undefined;
    KW_ACTIVE(): TerminalNode | undefined;
    KW_INACTIVE(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterViewContext extends ParserRuleContext {
    _algType: Token;
    _secContext: Token;
    _checkOpt: Token;
    KW_ALTER(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewName(): ViewNameContext;
    KW_AS(): TerminalNode;
    selectStatement(): SelectStatementContext;
    KW_ALGORITHM(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    ownerStatement(): OwnerStatementContext | undefined;
    KW_SQL(): TerminalNode | undefined;
    KW_SECURITY(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    columnNames(): ColumnNamesContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_CHECK(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_UNDEFINED(): TerminalNode | undefined;
    KW_MERGE(): TerminalNode | undefined;
    KW_TEMPTABLE(): TerminalNode | undefined;
    KW_DEFINER(): TerminalNode | undefined;
    KW_INVOKER(): TerminalNode | undefined;
    KW_CASCADED(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterOptionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: AlterOptionContext): void;
}
export declare class AlterByTableOptionContext extends AlterOptionContext {
    tableOption(): TableOptionContext[];
    tableOption(i: number): TableOptionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByAddColumnContext extends AlterOptionContext {
    KW_ADD(): TerminalNode;
    columnName(): ColumnNameContext[];
    columnName(i: number): ColumnNameContext;
    columnDefinition(): ColumnDefinitionContext;
    KW_COLUMN(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_AFTER(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByAddColumnsContext extends AlterOptionContext {
    KW_ADD(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    columnName(): ColumnNameContext[];
    columnName(i: number): ColumnNameContext;
    columnDefinition(): ColumnDefinitionContext[];
    columnDefinition(i: number): ColumnDefinitionContext;
    RR_BRACKET(): TerminalNode;
    KW_COLUMN(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByAddIndexContext extends AlterOptionContext {
    KW_ADD(): TerminalNode;
    indexColumnNames(): IndexColumnNamesContext;
    KW_INDEX(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    indexName(): IndexNameContext | undefined;
    indexType(): IndexTypeContext | undefined;
    indexOption(): IndexOptionContext[];
    indexOption(i: number): IndexOptionContext;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByAddSpecialIndexContext extends AlterOptionContext {
    KW_ADD(): TerminalNode;
    indexColumnNames(): IndexColumnNamesContext;
    KW_FULLTEXT(): TerminalNode | undefined;
    KW_SPATIAL(): TerminalNode | undefined;
    indexName(): IndexNameContext | undefined;
    indexOption(): IndexOptionContext[];
    indexOption(i: number): IndexOptionContext;
    KW_INDEX(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByAddPrimaryKeyContext extends AlterOptionContext {
    _symbol: UidContext;
    KW_ADD(): TerminalNode;
    KW_PRIMARY(): TerminalNode;
    KW_KEY(): TerminalNode;
    indexColumnNames(): IndexColumnNamesContext;
    KW_CONSTRAINT(): TerminalNode | undefined;
    indexType(): IndexTypeContext | undefined;
    indexOption(): IndexOptionContext[];
    indexOption(i: number): IndexOptionContext;
    uid(): UidContext | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByAddUniqueKeyContext extends AlterOptionContext {
    _symbol: UidContext;
    KW_ADD(): TerminalNode;
    KW_UNIQUE(): TerminalNode;
    indexColumnNames(): IndexColumnNamesContext;
    KW_CONSTRAINT(): TerminalNode | undefined;
    indexName(): IndexNameContext | undefined;
    indexType(): IndexTypeContext | undefined;
    indexOption(): IndexOptionContext[];
    indexOption(i: number): IndexOptionContext;
    KW_INDEX(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    uid(): UidContext | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByAddForeignKeyContext extends AlterOptionContext {
    _symbol: UidContext;
    KW_ADD(): TerminalNode;
    KW_FOREIGN(): TerminalNode;
    KW_KEY(): TerminalNode;
    indexColumnNames(): IndexColumnNamesContext;
    referenceDefinition(): ReferenceDefinitionContext;
    KW_CONSTRAINT(): TerminalNode | undefined;
    indexName(): IndexNameContext | undefined;
    uid(): UidContext | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByAddCheckTableConstraintContext extends AlterOptionContext {
    KW_ADD(): TerminalNode;
    checkConstraintDefinition(): CheckConstraintDefinitionContext | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByDropConstraintCheckContext extends AlterOptionContext {
    _symbol: UidContext;
    KW_DROP(): TerminalNode;
    KW_CHECK(): TerminalNode | undefined;
    KW_CONSTRAINT(): TerminalNode | undefined;
    uid(): UidContext;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByAlterCheckTableConstraintContext extends AlterOptionContext {
    _symbol: UidContext;
    KW_ALTER(): TerminalNode;
    KW_CHECK(): TerminalNode | undefined;
    KW_CONSTRAINT(): TerminalNode | undefined;
    uid(): UidContext;
    KW_NOT(): TerminalNode | undefined;
    KW_ENFORCED(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterBySetAlgorithmContext extends AlterOptionContext {
    KW_ALGORITHM(): TerminalNode;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_INSTANT(): TerminalNode | undefined;
    KW_INPLACE(): TerminalNode | undefined;
    KW_COPY(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByAlterColumnDefaultContext extends AlterOptionContext {
    KW_ALTER(): TerminalNode;
    columnName(): ColumnNameContext;
    KW_SET(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    defaultValue(): DefaultValueContext | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_COLUMN(): TerminalNode | undefined;
    KW_VISIBLE(): TerminalNode | undefined;
    KW_INVISIBLE(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByAlterIndexVisibilityContext extends AlterOptionContext {
    KW_ALTER(): TerminalNode;
    KW_INDEX(): TerminalNode;
    indexName(): IndexNameContext;
    KW_VISIBLE(): TerminalNode | undefined;
    KW_INVISIBLE(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByChangeColumnContext extends AlterOptionContext {
    _oldColumn: ColumnNameContext;
    _newColumn: ColumnNameCreateContext;
    KW_CHANGE(): TerminalNode;
    columnDefinition(): ColumnDefinitionContext;
    columnName(): ColumnNameContext[];
    columnName(i: number): ColumnNameContext;
    columnNameCreate(): ColumnNameCreateContext;
    KW_COLUMN(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_AFTER(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByDefaultCharsetContext extends AlterOptionContext {
    KW_CHARACTER(): TerminalNode;
    KW_SET(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    charsetName(): CharsetNameContext;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_COLLATE(): TerminalNode | undefined;
    collationName(): CollationNameContext | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByConvertCharsetContext extends AlterOptionContext {
    KW_CONVERT(): TerminalNode;
    KW_TO(): TerminalNode;
    charsetName(): CharsetNameContext;
    KW_CHARSET(): TerminalNode | undefined;
    KW_CHARACTER(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_COLLATE(): TerminalNode | undefined;
    collationName(): CollationNameContext | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterKeysContext extends AlterOptionContext {
    KW_KEYS(): TerminalNode;
    KW_DISABLE(): TerminalNode | undefined;
    KW_ENABLE(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterTablespaceOptionContext extends AlterOptionContext {
    KW_TABLESPACE(): TerminalNode;
    KW_DISCARD(): TerminalNode | undefined;
    KW_IMPORT(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByDropColumnContext extends AlterOptionContext {
    KW_DROP(): TerminalNode;
    columnName(): ColumnNameContext;
    KW_COLUMN(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByDropIndexContext extends AlterOptionContext {
    KW_DROP(): TerminalNode;
    indexName(): IndexNameContext;
    KW_INDEX(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByDropPrimaryKeyContext extends AlterOptionContext {
    KW_DROP(): TerminalNode;
    KW_PRIMARY(): TerminalNode;
    KW_KEY(): TerminalNode;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByDropForeignKeyContext extends AlterOptionContext {
    _fk_symbol: UidContext;
    KW_DROP(): TerminalNode;
    KW_FOREIGN(): TerminalNode;
    KW_KEY(): TerminalNode;
    uid(): UidContext;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByForceContext extends AlterOptionContext {
    KW_FORCE(): TerminalNode;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByLockContext extends AlterOptionContext {
    _lockType: Token;
    KW_LOCK(): TerminalNode;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_NONE(): TerminalNode | undefined;
    KW_SHARED(): TerminalNode | undefined;
    KW_EXCLUSIVE(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByModifyColumnContext extends AlterOptionContext {
    KW_MODIFY(): TerminalNode;
    columnName(): ColumnNameContext[];
    columnName(i: number): ColumnNameContext;
    columnDefinition(): ColumnDefinitionContext;
    KW_COLUMN(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_AFTER(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByOrderContext extends AlterOptionContext {
    KW_ORDER(): TerminalNode;
    KW_BY(): TerminalNode;
    columnNames(): ColumnNamesContext;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByRenameColumnContext extends AlterOptionContext {
    _olcdColumn: ColumnNameContext;
    _newColumn: ColumnNameCreateContext;
    KW_RENAME(): TerminalNode;
    KW_COLUMN(): TerminalNode;
    KW_TO(): TerminalNode;
    columnName(): ColumnNameContext;
    columnNameCreate(): ColumnNameCreateContext;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByRenameIndexContext extends AlterOptionContext {
    _indexFormat: Token;
    KW_RENAME(): TerminalNode;
    indexName(): IndexNameContext;
    KW_TO(): TerminalNode;
    indexNameCreate(): IndexNameCreateContext;
    KW_INDEX(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByRenameContext extends AlterOptionContext {
    _renameFormat: Token;
    KW_RENAME(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    KW_TO(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByValidateContext extends AlterOptionContext {
    KW_VALIDATION(): TerminalNode;
    KW_WITHOUT(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterPartitionContext extends AlterOptionContext {
    alterPartitionSpecification(): AlterPartitionSpecificationContext;
    constructor(ctx: AlterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterPartitionSpecificationContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: AlterPartitionSpecificationContext): void;
}
export declare class AlterByAddPartitionContext extends AlterPartitionSpecificationContext {
    KW_ADD(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    partitionDefinition(): PartitionDefinitionContext[];
    partitionDefinition(i: number): PartitionDefinitionContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByDropPartitionContext extends AlterPartitionSpecificationContext {
    KW_DROP(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    partitionNames(): PartitionNamesContext;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByDiscardPartitionContext extends AlterPartitionSpecificationContext {
    KW_DISCARD(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    KW_TABLESPACE(): TerminalNode;
    partitionNames(): PartitionNamesContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByImportPartitionContext extends AlterPartitionSpecificationContext {
    KW_IMPORT(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    KW_TABLESPACE(): TerminalNode;
    partitionNames(): PartitionNamesContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByTruncatePartitionContext extends AlterPartitionSpecificationContext {
    KW_TRUNCATE(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    partitionNames(): PartitionNamesContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByCoalescePartitionContext extends AlterPartitionSpecificationContext {
    KW_COALESCE(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByReorganizePartitionContext extends AlterPartitionSpecificationContext {
    KW_REORGANIZE(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    partitionNames(): PartitionNamesContext;
    KW_INTO(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    partitionDefinition(): PartitionDefinitionContext[];
    partitionDefinition(i: number): PartitionDefinitionContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByExchangePartitionContext extends AlterPartitionSpecificationContext {
    _validationFormat: Token;
    KW_EXCHANGE(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    partitionName(): PartitionNameContext;
    KW_WITH(): TerminalNode[];
    KW_WITH(i: number): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    KW_VALIDATION(): TerminalNode | undefined;
    KW_WITHOUT(): TerminalNode | undefined;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByAnalyzePartitionContext extends AlterPartitionSpecificationContext {
    KW_ANALYZE(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    partitionNames(): PartitionNamesContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByCheckPartitionContext extends AlterPartitionSpecificationContext {
    KW_CHECK(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    partitionNames(): PartitionNamesContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByOptimizePartitionContext extends AlterPartitionSpecificationContext {
    KW_OPTIMIZE(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    partitionNames(): PartitionNamesContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByRebuildPartitionContext extends AlterPartitionSpecificationContext {
    KW_REBUILD(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    partitionNames(): PartitionNamesContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByRepairPartitionContext extends AlterPartitionSpecificationContext {
    KW_REPAIR(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    partitionNames(): PartitionNamesContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByRemovePartitioningContext extends AlterPartitionSpecificationContext {
    KW_REMOVE(): TerminalNode;
    KW_PARTITIONING(): TerminalNode;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterByUpgradePartitioningContext extends AlterPartitionSpecificationContext {
    KW_UPGRADE(): TerminalNode;
    KW_PARTITIONING(): TerminalNode;
    constructor(ctx: AlterPartitionSpecificationContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropDatabaseContext extends ParserRuleContext {
    _dbFormat: Token;
    KW_DROP(): TerminalNode;
    databaseName(): DatabaseNameContext;
    KW_DATABASE(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropEventContext extends ParserRuleContext {
    _event_name: FullIdContext;
    KW_DROP(): TerminalNode;
    KW_EVENT(): TerminalNode;
    fullId(): FullIdContext;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropIndexContext extends ParserRuleContext {
    _intimeAction: Token;
    _algType: Token;
    _lockType: Token;
    KW_DROP(): TerminalNode;
    KW_INDEX(): TerminalNode;
    indexName(): IndexNameContext;
    KW_ON(): TerminalNode;
    tableName(): TableNameContext;
    KW_ALGORITHM(): TerminalNode[];
    KW_ALGORITHM(i: number): TerminalNode;
    KW_LOCK(): TerminalNode[];
    KW_LOCK(i: number): TerminalNode;
    KW_ONLINE(): TerminalNode | undefined;
    KW_OFFLINE(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode[];
    KW_DEFAULT(i: number): TerminalNode;
    KW_INPLACE(): TerminalNode[];
    KW_INPLACE(i: number): TerminalNode;
    KW_COPY(): TerminalNode[];
    KW_COPY(i: number): TerminalNode;
    KW_NONE(): TerminalNode[];
    KW_NONE(i: number): TerminalNode;
    KW_SHARED(): TerminalNode[];
    KW_SHARED(i: number): TerminalNode;
    KW_EXCLUSIVE(): TerminalNode[];
    KW_EXCLUSIVE(i: number): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropLogfileGroupContext extends ParserRuleContext {
    _logfileGroupName: UidContext;
    KW_DROP(): TerminalNode;
    KW_LOGFILE(): TerminalNode;
    KW_GROUP(): TerminalNode;
    KW_ENGINE(): TerminalNode;
    engineName(): EngineNameContext;
    uid(): UidContext;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropProcedureContext extends ParserRuleContext {
    _sp_name: FullIdContext;
    KW_DROP(): TerminalNode;
    KW_PROCEDURE(): TerminalNode;
    fullId(): FullIdContext;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropFunctionContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    functionName(): FunctionNameContext;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropServerContext extends ParserRuleContext {
    _serverName: UidContext;
    KW_DROP(): TerminalNode;
    KW_SERVER(): TerminalNode;
    uid(): UidContext;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropSpatialContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_SPATIAL(): TerminalNode;
    KW_REFERENCE(): TerminalNode;
    KW_SYSTEM(): TerminalNode;
    DECIMAL_LITERAL(): TerminalNode;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropTableContext extends ParserRuleContext {
    _dropType: Token;
    KW_DROP(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNames(): TableNamesContext;
    KW_TEMPORARY(): TerminalNode | undefined;
    ifExists(): IfExistsContext | undefined;
    KW_RESTRICT(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropTablespaceContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_TABLESPACE(): TerminalNode;
    tablespaceName(): TablespaceNameContext;
    KW_UNDO(): TerminalNode | undefined;
    KW_ENGINE(): TerminalNode | undefined;
    engineName(): EngineNameContext | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropTriggerContext extends ParserRuleContext {
    _trigger_name: FullIdContext;
    KW_DROP(): TerminalNode;
    KW_TRIGGER(): TerminalNode;
    fullId(): FullIdContext;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropViewContext extends ParserRuleContext {
    _dropType: Token;
    KW_DROP(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewName(): ViewNameContext[];
    viewName(i: number): ViewNameContext;
    ifExists(): IfExistsContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_RESTRICT(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropRoleContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_ROLE(): TerminalNode;
    userOrRoleNames(): UserOrRoleNamesContext;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetRoleContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode;
    KW_TO(): TerminalNode | undefined;
    KW_NONE(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    userOrRoleNames(): UserOrRoleNamesContext | undefined;
    userOrRoleName(): UserOrRoleNameContext[];
    userOrRoleName(i: number): UserOrRoleNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    roleOption(): RoleOptionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RenameTableContext extends ParserRuleContext {
    KW_RENAME(): TerminalNode;
    KW_TABLE(): TerminalNode;
    renameTableClause(): RenameTableClauseContext[];
    renameTableClause(i: number): RenameTableClauseContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RenameTableClauseContext extends ParserRuleContext {
    tableName(): TableNameContext;
    KW_TO(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TruncateTableContext extends ParserRuleContext {
    KW_TRUNCATE(): TerminalNode;
    tableName(): TableNameContext;
    KW_TABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CallStatementContext extends ParserRuleContext {
    _sp_name: FullIdContext;
    KW_CALL(): TerminalNode;
    fullId(): FullIdContext;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    constants(): ConstantsContext | undefined;
    expressions(): ExpressionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DeleteStatementContext extends ParserRuleContext {
    singleDeleteStatement(): SingleDeleteStatementContext | undefined;
    multipleDeleteStatement(): MultipleDeleteStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DoStatementContext extends ParserRuleContext {
    KW_DO(): TerminalNode;
    expressions(): ExpressionsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HandlerStatementContext extends ParserRuleContext {
    handlerOpenStatement(): HandlerOpenStatementContext | undefined;
    handlerReadIndexStatement(): HandlerReadIndexStatementContext | undefined;
    handlerReadStatement(): HandlerReadStatementContext | undefined;
    handlerCloseStatement(): HandlerCloseStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class InsertStatementContext extends ParserRuleContext {
    _priority: Token;
    _duplicatedFirst: UpdatedElementContext;
    _updatedElement: UpdatedElementContext;
    _duplicatedElements: UpdatedElementContext[];
    KW_INSERT(): TerminalNode;
    tableName(): TableNameContext;
    setAssignmentList(): SetAssignmentListContext | undefined;
    KW_IGNORE(): TerminalNode | undefined;
    KW_INTO(): TerminalNode | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    asRowAlias(): AsRowAliasContext[];
    asRowAlias(i: number): AsRowAliasContext;
    KW_ON(): TerminalNode | undefined;
    KW_DUPLICATE(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    updatedElement(): UpdatedElementContext[];
    updatedElement(i: number): UpdatedElementContext;
    KW_LOW_PRIORITY(): TerminalNode | undefined;
    KW_DELAYED(): TerminalNode | undefined;
    KW_HIGH_PRIORITY(): TerminalNode | undefined;
    fullColumnNames(): FullColumnNamesContext | undefined;
    valuesOrValueList(): ValuesOrValueListContext | undefined;
    selectOrTableOrValues(): SelectOrTableOrValuesContext | undefined;
    partitionNames(): PartitionNamesContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AsRowAliasContext extends ParserRuleContext {
    _rowAlias: UidContext;
    KW_AS(): TerminalNode;
    uid(): UidContext;
    fullColumnNames(): FullColumnNamesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SelectOrTableOrValuesContext extends ParserRuleContext {
    selectStatement(): SelectStatementContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    tableName(): TableNameContext | undefined;
    rowValuesList(): RowValuesListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class InterSectStatementContext extends ParserRuleContext {
    interSectQuery(): InterSectQueryContext[];
    interSectQuery(i: number): InterSectQueryContext;
    KW_INTERSECT(): TerminalNode[];
    KW_INTERSECT(i: number): TerminalNode;
    KW_ALL(): TerminalNode[];
    KW_ALL(i: number): TerminalNode;
    KW_DISTINCT(): TerminalNode[];
    KW_DISTINCT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class InterSectQueryContext extends ParserRuleContext {
    querySpecification(): QuerySpecificationContext;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LoadDataStatementContext extends ParserRuleContext {
    _priority: Token;
    _filename: Token;
    _violation: Token;
    _charset: CharsetNameContext;
    _fieldsFormat: Token;
    _linesFormat: Token;
    KW_LOAD(): TerminalNode;
    KW_DATA(): TerminalNode;
    KW_INFILE(): TerminalNode;
    KW_INTO(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    STRING_LITERAL(): TerminalNode;
    KW_LOCAL(): TerminalNode | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    partitionNames(): PartitionNamesContext | undefined;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    KW_CHARACTER(): TerminalNode | undefined;
    KW_SET(): TerminalNode[];
    KW_SET(i: number): TerminalNode;
    KW_LINES(): TerminalNode[];
    KW_LINES(i: number): TerminalNode;
    KW_IGNORE(): TerminalNode[];
    KW_IGNORE(i: number): TerminalNode;
    decimalLiteral(): DecimalLiteralContext | undefined;
    assignmentField(): AssignmentFieldContext[];
    assignmentField(i: number): AssignmentFieldContext;
    updatedElement(): UpdatedElementContext[];
    updatedElement(i: number): UpdatedElementContext;
    charsetName(): CharsetNameContext | undefined;
    KW_LOW_PRIORITY(): TerminalNode | undefined;
    KW_CONCURRENT(): TerminalNode | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    KW_FIELDS(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    KW_ROWS(): TerminalNode | undefined;
    selectFieldsInto(): SelectFieldsIntoContext[];
    selectFieldsInto(i: number): SelectFieldsIntoContext;
    selectLinesInto(): SelectLinesIntoContext[];
    selectLinesInto(i: number): SelectLinesIntoContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LoadXmlStatementContext extends ParserRuleContext {
    _priority: Token;
    _filename: Token;
    _violation: Token;
    _charset: CharsetNameContext;
    _tag: Token;
    _linesFormat: Token;
    KW_LOAD(): TerminalNode;
    KW_XML(): TerminalNode;
    KW_INFILE(): TerminalNode;
    KW_INTO(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    STRING_LITERAL(): TerminalNode[];
    STRING_LITERAL(i: number): TerminalNode;
    KW_LOCAL(): TerminalNode | undefined;
    KW_CHARACTER(): TerminalNode | undefined;
    KW_SET(): TerminalNode[];
    KW_SET(i: number): TerminalNode;
    KW_ROWS(): TerminalNode[];
    KW_ROWS(i: number): TerminalNode;
    KW_IDENTIFIED(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    KW_IGNORE(): TerminalNode[];
    KW_IGNORE(i: number): TerminalNode;
    decimalLiteral(): DecimalLiteralContext | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    assignmentField(): AssignmentFieldContext[];
    assignmentField(i: number): AssignmentFieldContext;
    RR_BRACKET(): TerminalNode | undefined;
    updatedElement(): UpdatedElementContext[];
    updatedElement(i: number): UpdatedElementContext;
    charsetName(): CharsetNameContext | undefined;
    KW_LOW_PRIORITY(): TerminalNode | undefined;
    KW_CONCURRENT(): TerminalNode | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    KW_LINES(): TerminalNode | undefined;
    LESS_SYMBOL(): TerminalNode | undefined;
    GREATER_SYMBOL(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ParenthesizedQueryContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    parenthesizedQueryExpression(): ParenthesizedQueryExpressionContext;
    RR_BRACKET(): TerminalNode;
    orderByClause(): OrderByClauseContext[];
    orderByClause(i: number): OrderByClauseContext;
    limitClause(): LimitClauseContext[];
    limitClause(i: number): LimitClauseContext;
    intoClause(): IntoClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ReplaceStatementContext extends ParserRuleContext {
    _priority: Token;
    KW_REPLACE(): TerminalNode;
    tableName(): TableNameContext;
    replaceStatementValuesOrSelectOrTable(): ReplaceStatementValuesOrSelectOrTableContext | undefined;
    setAssignmentList(): SetAssignmentListContext | undefined;
    KW_INTO(): TerminalNode | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    partitionNames(): PartitionNamesContext | undefined;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    KW_LOW_PRIORITY(): TerminalNode | undefined;
    KW_DELAYED(): TerminalNode | undefined;
    columnNames(): ColumnNamesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SelectStatementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: SelectStatementContext): void;
}
export declare class SimpleSelectContext extends SelectStatementContext {
    querySpecification(): QuerySpecificationContext | undefined;
    lockClause(): LockClauseContext | undefined;
    querySpecificationNointo(): QuerySpecificationNointoContext | undefined;
    intoClause(): IntoClauseContext | undefined;
    constructor(ctx: SelectStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ParenthesisSelectContext extends SelectStatementContext {
    queryExpression(): QueryExpressionContext;
    lockClause(): LockClauseContext | undefined;
    constructor(ctx: SelectStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UnionSelectContext extends SelectStatementContext {
    _unionType: Token;
    querySpecificationNointo(): QuerySpecificationNointoContext;
    unionStatement(): UnionStatementContext[];
    unionStatement(i: number): UnionStatementContext;
    KW_UNION(): TerminalNode | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    lockClause(): LockClauseContext | undefined;
    querySpecification(): QuerySpecificationContext | undefined;
    queryExpression(): QueryExpressionContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    constructor(ctx: SelectStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UnionParenthesisSelectContext extends SelectStatementContext {
    _unionType: Token;
    queryExpressionNointo(): QueryExpressionNointoContext;
    unionParenthesis(): UnionParenthesisContext[];
    unionParenthesis(i: number): UnionParenthesisContext;
    KW_UNION(): TerminalNode | undefined;
    queryExpression(): QueryExpressionContext | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    lockClause(): LockClauseContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    constructor(ctx: SelectStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class WithLateralStatementContext extends SelectStatementContext {
    querySpecificationNointo(): QuerySpecificationNointoContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    lateralStatement(): LateralStatementContext[];
    lateralStatement(i: number): LateralStatementContext;
    constructor(ctx: SelectStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetOperationsContext extends ParserRuleContext {
    queryExpressionBody(): QueryExpressionBodyContext;
    withClause(): WithClauseContext | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    intoClause(): IntoClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class QueryExpressionBodyContext extends ParserRuleContext {
    queryItem(): QueryItemContext;
    queryExpressionBody(): QueryExpressionBodyContext | undefined;
    KW_UNION(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    KW_EXCEPT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class QueryItemContext extends ParserRuleContext {
    queryPrimary(): QueryPrimaryContext;
    queryItem(): QueryItemContext | undefined;
    KW_INTERSECT(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class QueryPrimaryContext extends ParserRuleContext {
    queryBlock(): QueryBlockContext | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    queryExpressionBody(): QueryExpressionBodyContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    intoClause(): IntoClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UpdateStatementContext extends ParserRuleContext {
    singleUpdateStatement(): SingleUpdateStatementContext | undefined;
    multipleUpdateStatement(): MultipleUpdateStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ValuesStatementContext extends ParserRuleContext {
    rowValuesList(): RowValuesListContext;
    KW_ORDER(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    indexColumnName(): IndexColumnNameContext | undefined;
    KW_LIMIT(): TerminalNode | undefined;
    limitClauseAtom(): LimitClauseAtomContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ParenthesizedQueryExpressionContext extends ParserRuleContext {
    queryBlock(): QueryBlockContext[];
    queryBlock(i: number): QueryBlockContext;
    orderByClause(): OrderByClauseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    intoClause(): IntoClauseContext | undefined;
    KW_UNION(): TerminalNode[];
    KW_UNION(i: number): TerminalNode;
    KW_INTERSECT(): TerminalNode[];
    KW_INTERSECT(i: number): TerminalNode;
    KW_EXCEPT(): TerminalNode[];
    KW_EXCEPT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class QueryBlockContext extends ParserRuleContext {
    selectStatement(): SelectStatementContext | undefined;
    tableStatement(): TableStatementContext | undefined;
    valuesStatement(): ValuesStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ReplaceStatementValuesOrSelectOrTableContext extends ParserRuleContext {
    selectStatement(): SelectStatementContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    tableName(): TableNameContext | undefined;
    valuesOrValueList(): ValuesOrValueListContext | undefined;
    rowValuesList(): RowValuesListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RowValuesListContext extends ParserRuleContext {
    KW_VALUES(): TerminalNode;
    KW_ROW(): TerminalNode[];
    KW_ROW(i: number): TerminalNode;
    expressionsWithDefaults(): ExpressionsWithDefaultsContext[];
    expressionsWithDefaults(i: number): ExpressionsWithDefaultsContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetAssignmentListContext extends ParserRuleContext {
    _setFirst: UpdatedElementContext;
    _updatedElement: UpdatedElementContext;
    _setElements: UpdatedElementContext[];
    KW_SET(): TerminalNode;
    updatedElement(): UpdatedElementContext[];
    updatedElement(i: number): UpdatedElementContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UpdatedElementContext extends ParserRuleContext {
    columnName(): ColumnNameContext;
    EQUAL_SYMBOL(): TerminalNode;
    expressionOrDefault(): ExpressionOrDefaultContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AssignmentFieldContext extends ParserRuleContext {
    _var_name: UidContext;
    uid(): UidContext | undefined;
    LOCAL_ID(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LockClauseContext extends ParserRuleContext {
    KW_FOR(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    KW_SHARE(): TerminalNode | undefined;
    KW_OF(): TerminalNode | undefined;
    tableName(): TableNameContext[];
    tableName(i: number): TableNameContext;
    KW_LOCKED(): TerminalNode | undefined;
    KW_NOWAIT(): TerminalNode | undefined;
    KW_SKIP(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_LOCK(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_MODE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SingleDeleteStatementContext extends ParserRuleContext {
    _priority: Token;
    _table_alias: UidContext;
    KW_DELETE(): TerminalNode;
    KW_FROM(): TerminalNode;
    tableName(): TableNameContext;
    KW_QUICK(): TerminalNode | undefined;
    KW_IGNORE(): TerminalNode | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    partitionNames(): PartitionNamesContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    KW_WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    KW_LIMIT(): TerminalNode | undefined;
    limitClauseAtom(): LimitClauseAtomContext | undefined;
    KW_LOW_PRIORITY(): TerminalNode | undefined;
    uid(): UidContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class MultipleDeleteStatementContext extends ParserRuleContext {
    _priority: Token;
    KW_DELETE(): TerminalNode;
    tableName(): TableNameContext[];
    tableName(i: number): TableNameContext;
    KW_FROM(): TerminalNode | undefined;
    tableSources(): TableSourcesContext | undefined;
    KW_USING(): TerminalNode | undefined;
    KW_QUICK(): TerminalNode | undefined;
    KW_IGNORE(): TerminalNode | undefined;
    KW_WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    KW_LOW_PRIORITY(): TerminalNode | undefined;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    STAR(): TerminalNode[];
    STAR(i: number): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HandlerOpenStatementContext extends ParserRuleContext {
    _table_alias: UidContext;
    KW_HANDLER(): TerminalNode;
    tableName(): TableNameContext;
    KW_OPEN(): TerminalNode;
    uid(): UidContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HandlerReadIndexStatementContext extends ParserRuleContext {
    _moveOrder: Token;
    KW_HANDLER(): TerminalNode;
    tableName(): TableNameContext;
    KW_READ(): TerminalNode;
    indexName(): IndexNameContext;
    comparisonBase(): ComparisonBaseContext | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    constants(): ConstantsContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    KW_WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    KW_LIMIT(): TerminalNode | undefined;
    limitClauseAtom(): LimitClauseAtomContext | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_NEXT(): TerminalNode | undefined;
    KW_PREV(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HandlerReadStatementContext extends ParserRuleContext {
    _moveOrder: Token;
    KW_HANDLER(): TerminalNode;
    tableName(): TableNameContext;
    KW_READ(): TerminalNode;
    KW_FIRST(): TerminalNode | undefined;
    KW_NEXT(): TerminalNode | undefined;
    KW_WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    KW_LIMIT(): TerminalNode | undefined;
    limitClauseAtom(): LimitClauseAtomContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HandlerCloseStatementContext extends ParserRuleContext {
    KW_HANDLER(): TerminalNode;
    tableName(): TableNameContext;
    KW_CLOSE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ImportTableStatementContext extends ParserRuleContext {
    KW_IMPORT(): TerminalNode;
    KW_TABLE(): TerminalNode;
    KW_FROM(): TerminalNode;
    stringLiteral(): StringLiteralContext[];
    stringLiteral(i: number): StringLiteralContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SingleUpdateStatementContext extends ParserRuleContext {
    _priority: Token;
    _table_alias: UidContext;
    KW_UPDATE(): TerminalNode;
    tableName(): TableNameContext;
    KW_SET(): TerminalNode;
    updatedElement(): UpdatedElementContext[];
    updatedElement(i: number): UpdatedElementContext;
    KW_IGNORE(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    KW_LOW_PRIORITY(): TerminalNode | undefined;
    uid(): UidContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class MultipleUpdateStatementContext extends ParserRuleContext {
    _priority: Token;
    KW_UPDATE(): TerminalNode;
    tableSources(): TableSourcesContext;
    KW_SET(): TerminalNode;
    updatedElement(): UpdatedElementContext[];
    updatedElement(i: number): UpdatedElementContext;
    KW_IGNORE(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    KW_LOW_PRIORITY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class OrderByClauseContext extends ParserRuleContext {
    KW_ORDER(): TerminalNode;
    KW_BY(): TerminalNode;
    orderByExpression(): OrderByExpressionContext[];
    orderByExpression(i: number): OrderByExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class OrderByExpressionContext extends ParserRuleContext {
    _order: Token;
    expression(): ExpressionContext;
    KW_ASC(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableSourcesContext extends ParserRuleContext {
    tableSource(): TableSourceContext[];
    tableSource(i: number): TableSourceContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableSourceContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: TableSourceContext): void;
}
export declare class TableSourceBaseContext extends TableSourceContext {
    tableSourceItem(): TableSourceItemContext;
    joinPart(): JoinPartContext[];
    joinPart(i: number): JoinPartContext;
    constructor(ctx: TableSourceContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableSourceNestedContext extends TableSourceContext {
    LR_BRACKET(): TerminalNode;
    tableSourceItem(): TableSourceItemContext;
    RR_BRACKET(): TerminalNode;
    joinPart(): JoinPartContext[];
    joinPart(i: number): JoinPartContext;
    constructor(ctx: TableSourceContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableJsonContext extends TableSourceContext {
    jsonTable(): JsonTableContext;
    constructor(ctx: TableSourceContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableSourceItemContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: TableSourceItemContext): void;
}
export declare class AtomTableItemContext extends TableSourceItemContext {
    _alias: UidContext;
    tableName(): TableNameContext;
    KW_PARTITION(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    partitionNames(): PartitionNamesContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    indexHint(): IndexHintContext[];
    indexHint(i: number): IndexHintContext;
    uid(): UidContext | undefined;
    KW_AS(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: TableSourceItemContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SubqueryTableItemContext extends TableSourceItemContext {
    _parenthesisSubquery: SelectStatementContext;
    _alias: UidContext;
    uid(): UidContext;
    selectStatement(): SelectStatementContext | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    KW_LATERAL(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    fullColumnNames(): FullColumnNamesContext | undefined;
    constructor(ctx: TableSourceItemContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableSourcesItemContext extends TableSourceItemContext {
    LR_BRACKET(): TerminalNode;
    tableSources(): TableSourcesContext;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: TableSourceItemContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FullColumnNamesContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    columnNames(): ColumnNamesContext;
    RR_BRACKET(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IndexHintContext extends ParserRuleContext {
    _indexHintAction: Token;
    _keyFormat: Token;
    LR_BRACKET(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    KW_USE(): TerminalNode | undefined;
    KW_IGNORE(): TerminalNode | undefined;
    KW_FORCE(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    indexHintType(): IndexHintTypeContext | undefined;
    indexNames(): IndexNamesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IndexHintTypeContext extends ParserRuleContext {
    KW_JOIN(): TerminalNode | undefined;
    KW_ORDER(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class JoinPartContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: JoinPartContext): void;
}
export declare class InnerJoinContext extends JoinPartContext {
    KW_JOIN(): TerminalNode;
    tableSourceItem(): TableSourceItemContext;
    KW_LATERAL(): TerminalNode | undefined;
    joinSpec(): JoinSpecContext[];
    joinSpec(i: number): JoinSpecContext;
    KW_INNER(): TerminalNode | undefined;
    KW_CROSS(): TerminalNode | undefined;
    constructor(ctx: JoinPartContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class StraightJoinContext extends JoinPartContext {
    KW_STRAIGHT_JOIN(): TerminalNode;
    tableSourceItem(): TableSourceItemContext;
    joinSpec(): JoinSpecContext[];
    joinSpec(i: number): JoinSpecContext;
    constructor(ctx: JoinPartContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class OuterJoinContext extends JoinPartContext {
    KW_JOIN(): TerminalNode;
    tableSourceItem(): TableSourceItemContext;
    KW_LEFT(): TerminalNode | undefined;
    KW_RIGHT(): TerminalNode | undefined;
    KW_OUTER(): TerminalNode | undefined;
    KW_LATERAL(): TerminalNode | undefined;
    joinSpec(): JoinSpecContext[];
    joinSpec(i: number): JoinSpecContext;
    constructor(ctx: JoinPartContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class NaturalJoinContext extends JoinPartContext {
    KW_NATURAL(): TerminalNode;
    KW_JOIN(): TerminalNode;
    tableSourceItem(): TableSourceItemContext;
    KW_INNER(): TerminalNode | undefined;
    KW_LEFT(): TerminalNode | undefined;
    KW_RIGHT(): TerminalNode | undefined;
    KW_OUTER(): TerminalNode | undefined;
    constructor(ctx: JoinPartContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class JoinSpecContext extends ParserRuleContext {
    KW_ON(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    KW_USING(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    columnNames(): ColumnNamesContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class QueryExpressionContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    querySpecification(): QuerySpecificationContext | undefined;
    RR_BRACKET(): TerminalNode;
    queryExpression(): QueryExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class QueryExpressionNointoContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    querySpecificationNointo(): QuerySpecificationNointoContext | undefined;
    RR_BRACKET(): TerminalNode;
    queryExpressionNointo(): QueryExpressionNointoContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class QuerySpecificationContext extends ParserRuleContext {
    KW_SELECT(): TerminalNode;
    selectElements(): SelectElementsContext;
    fromClause(): FromClauseContext;
    selectSpec(): SelectSpecContext[];
    selectSpec(i: number): SelectSpecContext;
    intoClause(): IntoClauseContext | undefined;
    groupByClause(): GroupByClauseContext | undefined;
    havingClause(): HavingClauseContext | undefined;
    windowClause(): WindowClauseContext | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class QuerySpecificationNointoContext extends ParserRuleContext {
    KW_SELECT(): TerminalNode;
    selectElements(): SelectElementsContext;
    fromClause(): FromClauseContext;
    selectSpec(): SelectSpecContext[];
    selectSpec(i: number): SelectSpecContext;
    groupByClause(): GroupByClauseContext | undefined;
    havingClause(): HavingClauseContext | undefined;
    windowClause(): WindowClauseContext | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UnionParenthesisContext extends ParserRuleContext {
    _unionType: Token;
    KW_UNION(): TerminalNode;
    queryExpressionNointo(): QueryExpressionNointoContext;
    KW_ALL(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UnionStatementContext extends ParserRuleContext {
    _unionType: Token;
    KW_UNION(): TerminalNode;
    querySpecificationNointo(): QuerySpecificationNointoContext | undefined;
    queryExpressionNointo(): QueryExpressionNointoContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LateralStatementContext extends ParserRuleContext {
    _alias: UidContext;
    KW_LATERAL(): TerminalNode;
    querySpecificationNointo(): QuerySpecificationNointoContext | undefined;
    queryExpressionNointo(): QueryExpressionNointoContext | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    uid(): UidContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class JsonTableContext extends ParserRuleContext {
    _alias: UidContext;
    KW_JSON_TABLE(): TerminalNode;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    STRING_LITERAL(): TerminalNode[];
    STRING_LITERAL(i: number): TerminalNode;
    COMMA(): TerminalNode;
    KW_COLUMNS(): TerminalNode;
    jsonColumnList(): JsonColumnListContext;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    uid(): UidContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class JsonColumnListContext extends ParserRuleContext {
    jsonColumn(): JsonColumnContext[];
    jsonColumn(i: number): JsonColumnContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class JsonColumnContext extends ParserRuleContext {
    columnName(): ColumnNameContext | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_ORDINALITY(): TerminalNode | undefined;
    dataType(): DataTypeContext | undefined;
    KW_PATH(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    jsonOnEmpty(): JsonOnEmptyContext | undefined;
    jsonOnError(): JsonOnErrorContext | undefined;
    KW_NESTED(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    jsonColumnList(): JsonColumnListContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class JsonOnEmptyContext extends ParserRuleContext {
    KW_ON(): TerminalNode;
    KW_EMPTY(): TerminalNode;
    KW_NULL_LITERAL(): TerminalNode | undefined;
    KW_ERROR(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    defaultValue(): DefaultValueContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class JsonOnErrorContext extends ParserRuleContext {
    KW_ON(): TerminalNode;
    KW_ERROR(): TerminalNode[];
    KW_ERROR(i: number): TerminalNode;
    KW_NULL_LITERAL(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    defaultValue(): DefaultValueContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SelectSpecContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    KW_DISTINCTROW(): TerminalNode | undefined;
    KW_HIGH_PRIORITY(): TerminalNode | undefined;
    KW_STRAIGHT_JOIN(): TerminalNode | undefined;
    KW_SQL_SMALL_RESULT(): TerminalNode | undefined;
    KW_SQL_BIG_RESULT(): TerminalNode | undefined;
    KW_SQL_BUFFER_RESULT(): TerminalNode | undefined;
    KW_SQL_CACHE(): TerminalNode | undefined;
    KW_SQL_NO_CACHE(): TerminalNode | undefined;
    KW_SQL_CALC_FOUND_ROWS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SelectElementsContext extends ParserRuleContext {
    _star: Token;
    selectElement(): SelectElementContext[];
    selectElement(i: number): SelectElementContext;
    STAR(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SelectElementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: SelectElementContext): void;
}
export declare class SelectStarElementContext extends SelectElementContext {
    _select_element: FullIdContext;
    DOT(): TerminalNode;
    STAR(): TerminalNode;
    fullId(): FullIdContext;
    constructor(ctx: SelectElementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SelectColumnElementContext extends SelectElementContext {
    _alias: UidContext;
    columnName(): ColumnNameContext;
    uid(): UidContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(ctx: SelectElementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SelectFunctionElementContext extends SelectElementContext {
    _alias: UidContext;
    functionCall(): FunctionCallContext;
    uid(): UidContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(ctx: SelectElementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SelectExpressionElementContext extends SelectElementContext {
    _alias: UidContext;
    expression(): ExpressionContext;
    LOCAL_ID(): TerminalNode | undefined;
    VAR_ASSIGN(): TerminalNode | undefined;
    uid(): UidContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(ctx: SelectElementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IntoClauseContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: IntoClauseContext): void;
}
export declare class SelectIntoVariablesContext extends IntoClauseContext {
    KW_INTO(): TerminalNode;
    assignmentField(): AssignmentFieldContext[];
    assignmentField(i: number): AssignmentFieldContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: IntoClauseContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SelectIntoDumpFileContext extends IntoClauseContext {
    KW_INTO(): TerminalNode;
    KW_DUMPFILE(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(ctx: IntoClauseContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SelectIntoTextFileContext extends IntoClauseContext {
    _filename: Token;
    _charset: CharsetNameContext;
    _fieldsFormat: Token;
    KW_INTO(): TerminalNode | undefined;
    KW_OUTFILE(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_CHARACTER(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_LINES(): TerminalNode | undefined;
    charsetName(): CharsetNameContext | undefined;
    KW_FIELDS(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    selectFieldsInto(): SelectFieldsIntoContext[];
    selectFieldsInto(i: number): SelectFieldsIntoContext;
    selectLinesInto(): SelectLinesIntoContext[];
    selectLinesInto(i: number): SelectLinesIntoContext;
    constructor(ctx: IntoClauseContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SelectFieldsIntoContext extends ParserRuleContext {
    _terminationField: Token;
    _enclosion: Token;
    _escaping: Token;
    KW_TERMINATED(): TerminalNode | undefined;
    KW_BY(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    KW_ENCLOSED(): TerminalNode | undefined;
    KW_OPTIONALLY(): TerminalNode | undefined;
    KW_ESCAPED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SelectLinesIntoContext extends ParserRuleContext {
    _starting: Token;
    _terminationLine: Token;
    KW_STARTING(): TerminalNode | undefined;
    KW_BY(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    KW_TERMINATED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FromClauseContext extends ParserRuleContext {
    _whereExpr: ExpressionContext;
    KW_FROM(): TerminalNode | undefined;
    tableSources(): TableSourcesContext | undefined;
    KW_WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class GroupByClauseContext extends ParserRuleContext {
    KW_GROUP(): TerminalNode;
    KW_BY(): TerminalNode;
    groupByItem(): GroupByItemContext[];
    groupByItem(i: number): GroupByItemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_WITH(): TerminalNode | undefined;
    KW_ROLLUP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HavingClauseContext extends ParserRuleContext {
    _havingExpr: ExpressionContext;
    KW_HAVING(): TerminalNode;
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class WindowClauseContext extends ParserRuleContext {
    KW_WINDOW(): TerminalNode;
    windowName(): WindowNameContext[];
    windowName(i: number): WindowNameContext;
    KW_AS(): TerminalNode[];
    KW_AS(i: number): TerminalNode;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    windowSpec(): WindowSpecContext[];
    windowSpec(i: number): WindowSpecContext;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class GroupByItemContext extends ParserRuleContext {
    _order: Token;
    expression(): ExpressionContext;
    KW_ASC(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LimitClauseContext extends ParserRuleContext {
    _offset: LimitClauseAtomContext;
    _limit: LimitClauseAtomContext;
    KW_LIMIT(): TerminalNode;
    KW_OFFSET(): TerminalNode | undefined;
    limitClauseAtom(): LimitClauseAtomContext[];
    limitClauseAtom(i: number): LimitClauseAtomContext;
    COMMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LimitClauseAtomContext extends ParserRuleContext {
    decimalLiteral(): DecimalLiteralContext | undefined;
    mysqlVariable(): MysqlVariableContext | undefined;
    simpleId(): SimpleIdContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class StartTransactionContext extends ParserRuleContext {
    KW_START(): TerminalNode;
    KW_TRANSACTION(): TerminalNode;
    transactionMode(): TransactionModeContext[];
    transactionMode(i: number): TransactionModeContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class BeginWorkContext extends ParserRuleContext {
    KW_BEGIN(): TerminalNode;
    KW_WORK(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CommitWorkContext extends ParserRuleContext {
    _nochain: Token;
    _norelease: Token;
    KW_COMMIT(): TerminalNode;
    KW_WORK(): TerminalNode | undefined;
    KW_AND(): TerminalNode | undefined;
    KW_CHAIN(): TerminalNode | undefined;
    KW_RELEASE(): TerminalNode | undefined;
    KW_NO(): TerminalNode[];
    KW_NO(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RollbackWorkContext extends ParserRuleContext {
    _nochain: Token;
    _norelease: Token;
    KW_ROLLBACK(): TerminalNode;
    KW_WORK(): TerminalNode | undefined;
    KW_AND(): TerminalNode | undefined;
    KW_CHAIN(): TerminalNode | undefined;
    KW_RELEASE(): TerminalNode | undefined;
    KW_NO(): TerminalNode[];
    KW_NO(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SavepointStatementContext extends ParserRuleContext {
    _identifier: UidContext;
    KW_SAVEPOINT(): TerminalNode;
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RollbackStatementContext extends ParserRuleContext {
    _identifier: UidContext;
    KW_ROLLBACK(): TerminalNode;
    KW_TO(): TerminalNode;
    uid(): UidContext;
    KW_WORK(): TerminalNode | undefined;
    KW_SAVEPOINT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ReleaseStatementContext extends ParserRuleContext {
    _identifier: UidContext;
    KW_RELEASE(): TerminalNode;
    KW_SAVEPOINT(): TerminalNode;
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LockTablesContext extends ParserRuleContext {
    KW_LOCK(): TerminalNode;
    lockTableElement(): LockTableElementContext[];
    lockTableElement(i: number): LockTableElementContext;
    KW_TABLE(): TerminalNode | undefined;
    KW_TABLES(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UnlockTablesContext extends ParserRuleContext {
    KW_UNLOCK(): TerminalNode;
    KW_TABLES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetAutocommitStatementContext extends ParserRuleContext {
    _autocommitValue: Token;
    KW_SET(): TerminalNode;
    KW_AUTOCOMMIT(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    ZERO_DECIMAL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetTransactionStatementContext extends ParserRuleContext {
    _transactionContext: Token;
    KW_SET(): TerminalNode;
    KW_TRANSACTION(): TerminalNode;
    transactionOption(): TransactionOptionContext[];
    transactionOption(i: number): TransactionOptionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_GLOBAL(): TerminalNode | undefined;
    KW_SESSION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TransactionModeContext extends ParserRuleContext {
    KW_WITH(): TerminalNode | undefined;
    KW_CONSISTENT(): TerminalNode | undefined;
    KW_SNAPSHOT(): TerminalNode | undefined;
    KW_READ(): TerminalNode | undefined;
    KW_WRITE(): TerminalNode | undefined;
    KW_ONLY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LockTableElementContext extends ParserRuleContext {
    _alias: UidContext;
    tableName(): TableNameContext;
    lockAction(): LockActionContext;
    uid(): UidContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LockActionContext extends ParserRuleContext {
    KW_READ(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    KW_WRITE(): TerminalNode | undefined;
    KW_LOW_PRIORITY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TransactionOptionContext extends ParserRuleContext {
    KW_ISOLATION(): TerminalNode | undefined;
    KW_LEVEL(): TerminalNode | undefined;
    transactionLevel(): TransactionLevelContext | undefined;
    KW_READ(): TerminalNode | undefined;
    KW_WRITE(): TerminalNode | undefined;
    KW_ONLY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TransactionLevelContext extends ParserRuleContext {
    KW_REPEATABLE(): TerminalNode | undefined;
    KW_READ(): TerminalNode | undefined;
    KW_COMMITTED(): TerminalNode | undefined;
    KW_UNCOMMITTED(): TerminalNode | undefined;
    KW_SERIALIZABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ChangeMasterContext extends ParserRuleContext {
    KW_CHANGE(): TerminalNode;
    KW_MASTER(): TerminalNode;
    KW_TO(): TerminalNode;
    masterOption(): MasterOptionContext[];
    masterOption(i: number): MasterOptionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    channelOption(): ChannelOptionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ChangeReplicationFilterContext extends ParserRuleContext {
    KW_CHANGE(): TerminalNode;
    KW_REPLICATION(): TerminalNode;
    KW_FILTER(): TerminalNode;
    replicationFilter(): ReplicationFilterContext[];
    replicationFilter(i: number): ReplicationFilterContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    channelOption(): ChannelOptionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ChangeReplicationSourceContext extends ParserRuleContext {
    KW_CHANGE(): TerminalNode;
    KW_REPLICATION(): TerminalNode;
    KW_SOURCE(): TerminalNode;
    KW_TO(): TerminalNode;
    replicationSourceOption(): ReplicationSourceOptionContext[];
    replicationSourceOption(i: number): ReplicationSourceOptionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    channelOption(): ChannelOptionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PurgeBinaryLogsContext extends ParserRuleContext {
    _purgeFormat: Token;
    _fileName: Token;
    _timeValue: Token;
    KW_PURGE(): TerminalNode;
    KW_LOGS(): TerminalNode;
    KW_BINARY(): TerminalNode | undefined;
    KW_MASTER(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    KW_BEFORE(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class StartSlaveOrReplicaContext extends ParserRuleContext {
    KW_START(): TerminalNode;
    KW_SLAVE(): TerminalNode | undefined;
    KW_REPLICA(): TerminalNode | undefined;
    threadType(): ThreadTypeContext[];
    threadType(i: number): ThreadTypeContext;
    KW_UNTIL(): TerminalNode | undefined;
    untilOption(): UntilOptionContext | undefined;
    connectionOptions(): ConnectionOptionsContext[];
    connectionOptions(i: number): ConnectionOptionsContext;
    channelOption(): ChannelOptionContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class StopSlaveOrReplicaContext extends ParserRuleContext {
    KW_STOP(): TerminalNode;
    KW_SLAVE(): TerminalNode | undefined;
    KW_REPLICA(): TerminalNode | undefined;
    threadType(): ThreadTypeContext[];
    threadType(i: number): ThreadTypeContext;
    channelOption(): ChannelOptionContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class StartGroupReplicationContext extends ParserRuleContext {
    KW_START(): TerminalNode;
    KW_GROUP_REPLICATION(): TerminalNode;
    KW_USER(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    STRING_LITERAL(): TerminalNode[];
    STRING_LITERAL(i: number): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_PASSWORD(): TerminalNode | undefined;
    KW_DEFAULT_AUTH(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class StopGroupReplicationContext extends ParserRuleContext {
    KW_STOP(): TerminalNode;
    KW_GROUP_REPLICATION(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class MasterOptionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: MasterOptionContext): void;
}
export declare class MasterStringOptionContext extends MasterOptionContext {
    stringMasterOption(): StringMasterOptionContext;
    EQUAL_SYMBOL(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(ctx: MasterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class MasterDecimalOptionContext extends MasterOptionContext {
    decimalMasterOption(): DecimalMasterOptionContext;
    EQUAL_SYMBOL(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    constructor(ctx: MasterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class MasterBoolOptionContext extends MasterOptionContext {
    _boolVal: Token;
    boolMasterOption(): BoolMasterOptionContext;
    EQUAL_SYMBOL(): TerminalNode;
    ZERO_DECIMAL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    constructor(ctx: MasterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class V8AddMasterOptionContext extends MasterOptionContext {
    v8NewMasterOption(): V8NewMasterOptionContext;
    constructor(ctx: MasterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class MasterUidListOptionContext extends MasterOptionContext {
    _server_id: UidContext;
    KW_IGNORE_SERVER_IDS(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    uid(): UidContext[];
    uid(i: number): UidContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: MasterOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class StringMasterOptionContext extends ParserRuleContext {
    KW_MASTER_BIND(): TerminalNode | undefined;
    KW_MASTER_HOST(): TerminalNode | undefined;
    KW_MASTER_USER(): TerminalNode | undefined;
    KW_MASTER_PASSWORD(): TerminalNode | undefined;
    KW_MASTER_LOG_FILE(): TerminalNode | undefined;
    KW_RELAY_LOG_FILE(): TerminalNode | undefined;
    KW_MASTER_COMPRESSION_ALGORITHMS(): TerminalNode | undefined;
    KW_MASTER_SSL_CA(): TerminalNode | undefined;
    KW_MASTER_SSL_CAPATH(): TerminalNode | undefined;
    KW_MASTER_SSL_CERT(): TerminalNode | undefined;
    KW_MASTER_SSL_CRL(): TerminalNode | undefined;
    KW_MASTER_SSL_CRLPATH(): TerminalNode | undefined;
    KW_MASTER_SSL_KEY(): TerminalNode | undefined;
    KW_MASTER_SSL_CIPHER(): TerminalNode | undefined;
    KW_MASTER_TLS_VERSION(): TerminalNode | undefined;
    KW_MASTER_TLS_CIPHERSUITES(): TerminalNode | undefined;
    KW_MASTER_PUBLIC_KEY_PATH(): TerminalNode | undefined;
    KW_NETWORK_NAMESPACE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DecimalMasterOptionContext extends ParserRuleContext {
    KW_MASTER_PORT(): TerminalNode | undefined;
    KW_MASTER_LOG_POS(): TerminalNode | undefined;
    KW_RELAY_LOG_POS(): TerminalNode | undefined;
    KW_MASTER_HEARTBEAT_PERIOD(): TerminalNode | undefined;
    KW_MASTER_CONNECT_RETRY(): TerminalNode | undefined;
    KW_MASTER_RETRY_COUNT(): TerminalNode | undefined;
    KW_MASTER_DELAY(): TerminalNode | undefined;
    KW_MASTER_ZSTD_COMPRESSION_LEVEL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class BoolMasterOptionContext extends ParserRuleContext {
    KW_REQUIRE_ROW_FORMAT(): TerminalNode | undefined;
    KW_MASTER_AUTO_POSITION(): TerminalNode | undefined;
    KW_SOURCE_CONNECTION_AUTO_FAILOVER(): TerminalNode | undefined;
    KW_MASTER_SSL(): TerminalNode | undefined;
    KW_MASTER_SSL_VERIFY_SERVER_CERT(): TerminalNode | undefined;
    KW_GET_MASTER_PUBLIC_KEY(): TerminalNode | undefined;
    KW_GTID_ONLY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class V8NewMasterOptionContext extends ParserRuleContext {
    KW_PRIVILEGE_CHECKS_USER(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_NULL_LITERAL(): TerminalNode | undefined;
    KW_REQUIRE_TABLE_PRIMARY_KEY_CHECK(): TerminalNode | undefined;
    KW_STREAM(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_OFF(): TerminalNode | undefined;
    KW_ASSIGN_GTIDS_TO_ANONYMOUS_TRANSACTIONS(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    gtuidSet(): GtuidSetContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ReplicationSourceOptionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ReplicationSourceOptionContext): void;
}
export declare class SourceStringOptionContext extends ReplicationSourceOptionContext {
    stringSourceOption(): StringSourceOptionContext;
    EQUAL_SYMBOL(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(ctx: ReplicationSourceOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SourceDecimalOptionContext extends ReplicationSourceOptionContext {
    decimalSourceOption(): DecimalSourceOptionContext;
    EQUAL_SYMBOL(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    constructor(ctx: ReplicationSourceOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SourceBoolOptionContext extends ReplicationSourceOptionContext {
    _boolVal: Token;
    boolSourceOption(): BoolSourceOptionContext;
    EQUAL_SYMBOL(): TerminalNode;
    ZERO_DECIMAL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    constructor(ctx: ReplicationSourceOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SourceOtherOptionContext extends ReplicationSourceOptionContext {
    otherSourceOption(): OtherSourceOptionContext;
    constructor(ctx: ReplicationSourceOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SourceUidListOptionContext extends ReplicationSourceOptionContext {
    _server_id: UidContext;
    KW_IGNORE_SERVER_IDS(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    uid(): UidContext[];
    uid(i: number): UidContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: ReplicationSourceOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class StringSourceOptionContext extends ParserRuleContext {
    KW_SOURCE_BIND(): TerminalNode | undefined;
    KW_SOURCE_HOST(): TerminalNode | undefined;
    KW_SOURCE_USER(): TerminalNode | undefined;
    KW_SOURCE_PASSWORD(): TerminalNode | undefined;
    KW_SOURCE_LOG_FILE(): TerminalNode | undefined;
    KW_RELAY_LOG_FILE(): TerminalNode | undefined;
    KW_SOURCE_COMPRESSION_ALGORITHMS(): TerminalNode | undefined;
    KW_SOURCE_SSL_CA(): TerminalNode | undefined;
    KW_SOURCE_SSL_CAPATH(): TerminalNode | undefined;
    KW_SOURCE_SSL_CERT(): TerminalNode | undefined;
    KW_SOURCE_SSL_CRL(): TerminalNode | undefined;
    KW_SOURCE_SSL_CRLPATH(): TerminalNode | undefined;
    KW_SOURCE_SSL_KEY(): TerminalNode | undefined;
    KW_SOURCE_SSL_CIPHER(): TerminalNode | undefined;
    KW_SOURCE_TLS_VERSION(): TerminalNode | undefined;
    KW_SOURCE_TLS_CIPHERSUITES(): TerminalNode | undefined;
    KW_SOURCE_PUBLIC_KEY_PATH(): TerminalNode | undefined;
    KW_NETWORK_NAMESPACE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DecimalSourceOptionContext extends ParserRuleContext {
    KW_SOURCE_PORT(): TerminalNode | undefined;
    KW_SOURCE_LOG_POS(): TerminalNode | undefined;
    KW_RELAY_LOG_POS(): TerminalNode | undefined;
    KW_SOURCE_HEARTBEAT_PERIOD(): TerminalNode | undefined;
    KW_SOURCE_CONNECT_RETRY(): TerminalNode | undefined;
    KW_SOURCE_RETRY_COUNT(): TerminalNode | undefined;
    KW_SOURCE_DELAY(): TerminalNode | undefined;
    KW_SOURCE_ZSTD_COMPRESSION_LEVEL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class BoolSourceOptionContext extends ParserRuleContext {
    KW_REQUIRE_ROW_FORMAT(): TerminalNode | undefined;
    KW_SOURCE_AUTO_POSITION(): TerminalNode | undefined;
    KW_SOURCE_CONNECTION_AUTO_FAILOVER(): TerminalNode | undefined;
    KW_SOURCE_SSL(): TerminalNode | undefined;
    KW_SOURCE_SSL_VERIFY_SERVER_CERT(): TerminalNode | undefined;
    KW_GET_SOURCE_PUBLIC_KEY(): TerminalNode | undefined;
    KW_GTID_ONLY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class OtherSourceOptionContext extends ParserRuleContext {
    KW_PRIVILEGE_CHECKS_USER(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_NULL_LITERAL(): TerminalNode | undefined;
    KW_REQUIRE_TABLE_PRIMARY_KEY_CHECK(): TerminalNode | undefined;
    KW_STREAM(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_OFF(): TerminalNode | undefined;
    KW_GENERATE(): TerminalNode | undefined;
    KW_ASSIGN_GTIDS_TO_ANONYMOUS_TRANSACTIONS(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    gtuidSet(): GtuidSetContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ChannelOptionContext extends ParserRuleContext {
    KW_FOR(): TerminalNode;
    KW_CHANNEL(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ReplicationFilterContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ReplicationFilterContext): void;
}
export declare class DoDbReplicationContext extends ReplicationFilterContext {
    KW_REPLICATE_DO_DB(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    databaseName(): DatabaseNameContext[];
    databaseName(i: number): DatabaseNameContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: ReplicationFilterContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IgnoreDbReplicationContext extends ReplicationFilterContext {
    KW_REPLICATE_IGNORE_DB(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    databaseName(): DatabaseNameContext[];
    databaseName(i: number): DatabaseNameContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: ReplicationFilterContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DoTableReplicationContext extends ReplicationFilterContext {
    KW_REPLICATE_DO_TABLE(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    tableNames(): TableNamesContext;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: ReplicationFilterContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IgnoreTableReplicationContext extends ReplicationFilterContext {
    KW_REPLICATE_IGNORE_TABLE(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    tableNames(): TableNamesContext;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: ReplicationFilterContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class WildDoTableReplicationContext extends ReplicationFilterContext {
    KW_REPLICATE_WILD_DO_TABLE(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    simpleStrings(): SimpleStringsContext;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: ReplicationFilterContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class WildIgnoreTableReplicationContext extends ReplicationFilterContext {
    KW_REPLICATE_WILD_IGNORE_TABLE(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    simpleStrings(): SimpleStringsContext;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: ReplicationFilterContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RewriteDbReplicationContext extends ReplicationFilterContext {
    KW_REPLICATE_REWRITE_DB(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    tablePair(): TablePairContext[];
    tablePair(i: number): TablePairContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: ReplicationFilterContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TablePairContext extends ParserRuleContext {
    _firstTable: TableNameContext;
    _secondTable: TableNameContext;
    LR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    tableName(): TableNameContext[];
    tableName(i: number): TableNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ThreadTypeContext extends ParserRuleContext {
    KW_IO_THREAD(): TerminalNode | undefined;
    KW_SQL_THREAD(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UntilOptionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: UntilOptionContext): void;
}
export declare class GtidsUntilOptionContext extends UntilOptionContext {
    _gtids: Token;
    EQUAL_SYMBOL(): TerminalNode;
    gtuidSet(): GtuidSetContext;
    KW_SQL_BEFORE_GTIDS(): TerminalNode | undefined;
    KW_SQL_AFTER_GTIDS(): TerminalNode | undefined;
    constructor(ctx: UntilOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class MasterLogUntilOptionContext extends UntilOptionContext {
    KW_MASTER_LOG_FILE(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    COMMA(): TerminalNode;
    KW_MASTER_LOG_POS(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    constructor(ctx: UntilOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SourceLogUntilOptionContext extends UntilOptionContext {
    KW_SOURCE_LOG_FILE(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    COMMA(): TerminalNode;
    KW_SOURCE_LOG_POS(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    constructor(ctx: UntilOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RelayLogUntilOptionContext extends UntilOptionContext {
    KW_RELAY_LOG_FILE(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    COMMA(): TerminalNode;
    KW_RELAY_LOG_POS(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    constructor(ctx: UntilOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SqlGapsUntilOptionContext extends UntilOptionContext {
    KW_SQL_AFTER_MTS_GAPS(): TerminalNode;
    constructor(ctx: UntilOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ConnectionOptionsContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ConnectionOptionsContext): void;
}
export declare class UserConnectionOptionContext extends ConnectionOptionsContext {
    _conOptUser: Token;
    KW_USER(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(ctx: ConnectionOptionsContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PasswordConnectionOptionContext extends ConnectionOptionsContext {
    _conOptPassword: Token;
    KW_PASSWORD(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(ctx: ConnectionOptionsContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DefaultAuthConnectionOptionContext extends ConnectionOptionsContext {
    _conOptDefAuth: Token;
    KW_DEFAULT_AUTH(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(ctx: ConnectionOptionsContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PluginDirConnectionOptionContext extends ConnectionOptionsContext {
    _conOptPluginDir: Token;
    KW_PLUGIN_DIR(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(ctx: ConnectionOptionsContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class GtuidSetContext extends ParserRuleContext {
    uuidSet(): UuidSetContext[];
    uuidSet(i: number): UuidSetContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    STRING_LITERAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class XaStartTransactionContext extends ParserRuleContext {
    _xaStart: Token;
    _xaAction: Token;
    KW_XA(): TerminalNode;
    xid(): XidContext;
    KW_START(): TerminalNode | undefined;
    KW_BEGIN(): TerminalNode | undefined;
    KW_JOIN(): TerminalNode | undefined;
    KW_RESUME(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class XaEndTransactionContext extends ParserRuleContext {
    KW_XA(): TerminalNode;
    KW_END(): TerminalNode;
    xid(): XidContext;
    KW_SUSPEND(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_MIGRATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class XaPrepareStatementContext extends ParserRuleContext {
    KW_XA(): TerminalNode;
    KW_PREPARE(): TerminalNode;
    xid(): XidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class XaCommitWorkContext extends ParserRuleContext {
    KW_XA(): TerminalNode;
    KW_COMMIT(): TerminalNode;
    xid(): XidContext;
    KW_ONE(): TerminalNode | undefined;
    KW_PHASE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class XaRollbackWorkContext extends ParserRuleContext {
    KW_XA(): TerminalNode;
    KW_ROLLBACK(): TerminalNode;
    xid(): XidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class XaRecoverWorkContext extends ParserRuleContext {
    KW_XA(): TerminalNode;
    KW_RECOVER(): TerminalNode;
    KW_CONVERT(): TerminalNode | undefined;
    xid(): XidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PrepareStatementContext extends ParserRuleContext {
    _stmt_name: UidContext;
    _query: Token;
    _variable: Token;
    KW_PREPARE(): TerminalNode;
    KW_FROM(): TerminalNode;
    uid(): UidContext;
    STRING_LITERAL(): TerminalNode | undefined;
    LOCAL_ID(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ExecuteStatementContext extends ParserRuleContext {
    _stmt_name: UidContext;
    KW_EXECUTE(): TerminalNode;
    uid(): UidContext;
    KW_USING(): TerminalNode | undefined;
    userVariables(): UserVariablesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DeallocatePrepareContext extends ParserRuleContext {
    _dropFormat: Token;
    _stmt_name: UidContext;
    KW_PREPARE(): TerminalNode;
    uid(): UidContext;
    KW_DEALLOCATE(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RoutineBodyContext extends ParserRuleContext {
    blockStatement(): BlockStatementContext | undefined;
    sqlStatement(): SqlStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class BlockStatementContext extends ParserRuleContext {
    _begin: UidContext;
    _end: UidContext;
    KW_BEGIN(): TerminalNode;
    KW_END(): TerminalNode;
    COLON_SYMB(): TerminalNode | undefined;
    declareVariable(): DeclareVariableContext[];
    declareVariable(i: number): DeclareVariableContext;
    SEMI(): TerminalNode[];
    SEMI(i: number): TerminalNode;
    declareCondition(): DeclareConditionContext[];
    declareCondition(i: number): DeclareConditionContext;
    declareCursor(): DeclareCursorContext[];
    declareCursor(i: number): DeclareCursorContext;
    declareHandler(): DeclareHandlerContext[];
    declareHandler(i: number): DeclareHandlerContext;
    procedureSqlStatement(): ProcedureSqlStatementContext[];
    procedureSqlStatement(i: number): ProcedureSqlStatementContext;
    uid(): UidContext[];
    uid(i: number): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CaseStatementContext extends ParserRuleContext {
    _case_value: UidContext;
    KW_CASE(): TerminalNode[];
    KW_CASE(i: number): TerminalNode;
    KW_END(): TerminalNode;
    expression(): ExpressionContext | undefined;
    caseAlternative(): CaseAlternativeContext[];
    caseAlternative(i: number): CaseAlternativeContext;
    KW_ELSE(): TerminalNode | undefined;
    uid(): UidContext | undefined;
    procedureSqlStatement(): ProcedureSqlStatementContext[];
    procedureSqlStatement(i: number): ProcedureSqlStatementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IfStatementContext extends ParserRuleContext {
    _procedureSqlStatement: ProcedureSqlStatementContext;
    _thenStatements: ProcedureSqlStatementContext[];
    _elseStatements: ProcedureSqlStatementContext[];
    KW_IF(): TerminalNode[];
    KW_IF(i: number): TerminalNode;
    expression(): ExpressionContext;
    KW_THEN(): TerminalNode;
    KW_END(): TerminalNode;
    elifAlternative(): ElifAlternativeContext[];
    elifAlternative(i: number): ElifAlternativeContext;
    KW_ELSE(): TerminalNode | undefined;
    procedureSqlStatement(): ProcedureSqlStatementContext[];
    procedureSqlStatement(i: number): ProcedureSqlStatementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IterateStatementContext extends ParserRuleContext {
    _label: UidContext;
    KW_ITERATE(): TerminalNode;
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LeaveStatementContext extends ParserRuleContext {
    _label: UidContext;
    KW_LEAVE(): TerminalNode;
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LoopStatementContext extends ParserRuleContext {
    _begin_label: UidContext;
    _end_label: UidContext;
    KW_LOOP(): TerminalNode[];
    KW_LOOP(i: number): TerminalNode;
    KW_END(): TerminalNode;
    COLON_SYMB(): TerminalNode | undefined;
    procedureSqlStatement(): ProcedureSqlStatementContext[];
    procedureSqlStatement(i: number): ProcedureSqlStatementContext;
    uid(): UidContext[];
    uid(i: number): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RepeatStatementContext extends ParserRuleContext {
    _begin_label: UidContext;
    _end_label: UidContext;
    KW_REPEAT(): TerminalNode[];
    KW_REPEAT(i: number): TerminalNode;
    KW_UNTIL(): TerminalNode;
    expression(): ExpressionContext;
    KW_END(): TerminalNode;
    COLON_SYMB(): TerminalNode | undefined;
    procedureSqlStatement(): ProcedureSqlStatementContext[];
    procedureSqlStatement(i: number): ProcedureSqlStatementContext;
    uid(): UidContext[];
    uid(i: number): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ReturnStatementContext extends ParserRuleContext {
    KW_RETURN(): TerminalNode;
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class WhileStatementContext extends ParserRuleContext {
    _begin_label: UidContext;
    _end_label: UidContext;
    KW_WHILE(): TerminalNode[];
    KW_WHILE(i: number): TerminalNode;
    expression(): ExpressionContext;
    KW_DO(): TerminalNode;
    KW_END(): TerminalNode;
    COLON_SYMB(): TerminalNode | undefined;
    procedureSqlStatement(): ProcedureSqlStatementContext[];
    procedureSqlStatement(i: number): ProcedureSqlStatementContext;
    uid(): UidContext[];
    uid(i: number): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CursorStatementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: CursorStatementContext): void;
}
export declare class CloseCursorContext extends CursorStatementContext {
    _cursor_name: UidContext;
    KW_CLOSE(): TerminalNode;
    uid(): UidContext;
    constructor(ctx: CursorStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FetchCursorContext extends CursorStatementContext {
    _cursor_name: UidContext;
    _var_names: UidListContext;
    KW_FETCH(): TerminalNode;
    KW_INTO(): TerminalNode;
    uid(): UidContext;
    uidList(): UidListContext;
    KW_FROM(): TerminalNode | undefined;
    KW_NEXT(): TerminalNode | undefined;
    constructor(ctx: CursorStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class OpenCursorContext extends CursorStatementContext {
    _cursor_name: UidContext;
    KW_OPEN(): TerminalNode;
    uid(): UidContext;
    constructor(ctx: CursorStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DeclareVariableContext extends ParserRuleContext {
    _var_names: UidListContext;
    KW_DECLARE(): TerminalNode;
    dataType(): DataTypeContext;
    uidList(): UidListContext;
    KW_DEFAULT(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DeclareConditionContext extends ParserRuleContext {
    _condition_name: UidContext;
    KW_DECLARE(): TerminalNode;
    KW_CONDITION(): TerminalNode;
    KW_FOR(): TerminalNode;
    uid(): UidContext;
    decimalLiteral(): DecimalLiteralContext | undefined;
    KW_SQLSTATE(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DeclareCursorContext extends ParserRuleContext {
    _condition_name: UidContext;
    KW_DECLARE(): TerminalNode;
    KW_CURSOR(): TerminalNode;
    KW_FOR(): TerminalNode;
    selectStatement(): SelectStatementContext;
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DeclareHandlerContext extends ParserRuleContext {
    _handlerAction: Token;
    KW_DECLARE(): TerminalNode;
    KW_HANDLER(): TerminalNode;
    KW_FOR(): TerminalNode;
    handlerConditionValue(): HandlerConditionValueContext[];
    handlerConditionValue(i: number): HandlerConditionValueContext;
    routineBody(): RoutineBodyContext;
    KW_CONTINUE(): TerminalNode | undefined;
    KW_EXIT(): TerminalNode | undefined;
    KW_UNDO(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HandlerConditionValueContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: HandlerConditionValueContext): void;
}
export declare class HandlerConditionCodeContext extends HandlerConditionValueContext {
    decimalLiteral(): DecimalLiteralContext;
    constructor(ctx: HandlerConditionValueContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HandlerConditionStateContext extends HandlerConditionValueContext {
    KW_SQLSTATE(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    KW_VALUE(): TerminalNode | undefined;
    constructor(ctx: HandlerConditionValueContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HandlerConditionNameContext extends HandlerConditionValueContext {
    _condition_name: UidContext;
    uid(): UidContext;
    constructor(ctx: HandlerConditionValueContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HandlerConditionWarningContext extends HandlerConditionValueContext {
    KW_SQLWARNING(): TerminalNode;
    constructor(ctx: HandlerConditionValueContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HandlerConditionNotfoundContext extends HandlerConditionValueContext {
    KW_NOT(): TerminalNode;
    KW_FOUND(): TerminalNode;
    constructor(ctx: HandlerConditionValueContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HandlerConditionExceptionContext extends HandlerConditionValueContext {
    KW_SQLEXCEPTION(): TerminalNode;
    constructor(ctx: HandlerConditionValueContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ProcedureSqlStatementContext extends ParserRuleContext {
    SEMI(): TerminalNode;
    compoundStatement(): CompoundStatementContext | undefined;
    sqlStatement(): SqlStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CaseAlternativeContext extends ParserRuleContext {
    KW_WHEN(): TerminalNode;
    KW_THEN(): TerminalNode;
    constant(): ConstantContext | undefined;
    expression(): ExpressionContext | undefined;
    procedureSqlStatement(): ProcedureSqlStatementContext[];
    procedureSqlStatement(i: number): ProcedureSqlStatementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ElifAlternativeContext extends ParserRuleContext {
    KW_ELSEIF(): TerminalNode;
    expression(): ExpressionContext;
    KW_THEN(): TerminalNode;
    procedureSqlStatement(): ProcedureSqlStatementContext[];
    procedureSqlStatement(i: number): ProcedureSqlStatementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterUserContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_USER(): TerminalNode;
    ifExists(): IfExistsContext | undefined;
    userSpecification(): UserSpecificationContext[];
    userSpecification(i: number): UserSpecificationContext;
    alterUserAuthOption(): AlterUserAuthOptionContext[];
    alterUserAuthOption(i: number): AlterUserAuthOptionContext;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    roleOption(): RoleOptionContext | undefined;
    userOrRoleName(): UserOrRoleNameContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_REQUIRE(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    userPasswordOption(): UserPasswordOptionContext[];
    userPasswordOption(i: number): UserPasswordOptionContext;
    userLockOption(): UserLockOptionContext[];
    userLockOption(i: number): UserLockOptionContext;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    KW_ATTRIBUTE(): TerminalNode | undefined;
    KW_NONE(): TerminalNode | undefined;
    tlsOption(): TlsOptionContext[];
    tlsOption(i: number): TlsOptionContext;
    userResourceOption(): UserResourceOptionContext[];
    userResourceOption(i: number): UserResourceOptionContext;
    KW_AND(): TerminalNode[];
    KW_AND(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateUserContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_USER(): TerminalNode;
    userName(): UserNameContext[];
    userName(i: number): UserNameContext;
    ifNotExists(): IfNotExistsContext | undefined;
    createUserAuthOption(): CreateUserAuthOptionContext[];
    createUserAuthOption(i: number): CreateUserAuthOptionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    roleOption(): RoleOptionContext | undefined;
    KW_REQUIRE(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    userPasswordOption(): UserPasswordOptionContext[];
    userPasswordOption(i: number): UserPasswordOptionContext;
    userLockOption(): UserLockOptionContext[];
    userLockOption(i: number): UserLockOptionContext;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    KW_ATTRIBUTE(): TerminalNode | undefined;
    KW_NONE(): TerminalNode | undefined;
    tlsOption(): TlsOptionContext[];
    tlsOption(i: number): TlsOptionContext;
    userResourceOption(): UserResourceOptionContext[];
    userResourceOption(i: number): UserResourceOptionContext;
    KW_AND(): TerminalNode[];
    KW_AND(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropUserContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_USER(): TerminalNode;
    userName(): UserNameContext[];
    userName(i: number): UserNameContext;
    ifExists(): IfExistsContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class GrantStatementContext extends ParserRuleContext {
    _tlsNone: Token;
    KW_GRANT(): TerminalNode[];
    KW_GRANT(i: number): TerminalNode;
    privelegeClause(): PrivelegeClauseContext[];
    privelegeClause(i: number): PrivelegeClauseContext;
    KW_ON(): TerminalNode | undefined;
    privilegeLevel(): PrivilegeLevelContext | undefined;
    KW_TO(): TerminalNode;
    userOrRoleNames(): UserOrRoleNamesContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    privilegeObjectType(): PrivilegeObjectTypeContext | undefined;
    KW_REQUIRE(): TerminalNode | undefined;
    KW_WITH(): TerminalNode[];
    KW_WITH(i: number): TerminalNode;
    KW_AS(): TerminalNode | undefined;
    userName(): UserNameContext | undefined;
    userAuthOption(): UserAuthOptionContext[];
    userAuthOption(i: number): UserAuthOptionContext;
    tlsOption(): TlsOptionContext[];
    tlsOption(i: number): TlsOptionContext;
    KW_NONE(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode[];
    KW_OPTION(i: number): TerminalNode;
    userResourceOption(): UserResourceOptionContext[];
    userResourceOption(i: number): UserResourceOptionContext;
    KW_ROLE(): TerminalNode | undefined;
    roleOption(): RoleOptionContext | undefined;
    KW_AND(): TerminalNode[];
    KW_AND(i: number): TerminalNode;
    userOrRoleName(): UserOrRoleNameContext[];
    userOrRoleName(i: number): UserOrRoleNameContext;
    KW_PROXY(): TerminalNode | undefined;
    KW_ADMIN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RoleOptionContext extends ParserRuleContext {
    KW_DEFAULT(): TerminalNode | undefined;
    KW_NONE(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_EXCEPT(): TerminalNode | undefined;
    userOrRoleNames(): UserOrRoleNamesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class GrantProxyContext extends ParserRuleContext {
    _fromFirst: UserNameContext;
    _toFirst: UserNameContext;
    _userName: UserNameContext;
    _toOther: UserNameContext[];
    KW_GRANT(): TerminalNode[];
    KW_GRANT(i: number): TerminalNode;
    KW_PROXY(): TerminalNode;
    KW_ON(): TerminalNode;
    KW_TO(): TerminalNode;
    userName(): UserNameContext[];
    userName(i: number): UserNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_WITH(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterResourceGroupContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_RESOURCE(): TerminalNode;
    KW_GROUP(): TerminalNode;
    groupName(): GroupNameContext;
    KW_VCPU(): TerminalNode | undefined;
    resourceGroupVcpuSpec(): ResourceGroupVcpuSpecContext | undefined;
    KW_THREAD_PRIORITY(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    KW_ENABLE(): TerminalNode | undefined;
    KW_DISABLE(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    KW_FORCE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateResourceGroupContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_RESOURCE(): TerminalNode;
    KW_GROUP(): TerminalNode;
    groupNameCreate(): GroupNameCreateContext;
    KW_TYPE(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    KW_SYSTEM(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_VCPU(): TerminalNode | undefined;
    resourceGroupVcpuSpec(): ResourceGroupVcpuSpecContext | undefined;
    KW_THREAD_PRIORITY(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    KW_ENABLE(): TerminalNode | undefined;
    KW_DISABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DropResourceGroupContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_RESOURCE(): TerminalNode;
    KW_GROUP(): TerminalNode;
    groupName(): GroupNameContext;
    KW_FORCE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetResourceGroupContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    KW_RESOURCE(): TerminalNode;
    KW_GROUP(): TerminalNode;
    groupName(): GroupNameContext;
    KW_FOR(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ResourceGroupVcpuSpecContext extends ParserRuleContext {
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    MINUS(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    resourceGroupVcpuSpec(): ResourceGroupVcpuSpecContext[];
    resourceGroupVcpuSpec(i: number): ResourceGroupVcpuSpecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RenameUserContext extends ParserRuleContext {
    KW_RENAME(): TerminalNode;
    KW_USER(): TerminalNode;
    renameUserClause(): RenameUserClauseContext[];
    renameUserClause(i: number): RenameUserClauseContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RevokeStatementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: RevokeStatementContext): void;
}
export declare class DetailRevokeContext extends RevokeStatementContext {
    KW_REVOKE(): TerminalNode;
    privelegeClause(): PrivelegeClauseContext[];
    privelegeClause(i: number): PrivelegeClauseContext;
    KW_ON(): TerminalNode;
    privilegeLevel(): PrivilegeLevelContext;
    KW_FROM(): TerminalNode;
    userOrRoleNames(): UserOrRoleNamesContext;
    ifExists(): IfExistsContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    privilegeObjectType(): PrivilegeObjectTypeContext | undefined;
    ignoreUnknownUser(): IgnoreUnknownUserContext | undefined;
    constructor(ctx: RevokeStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShortRevokeContext extends RevokeStatementContext {
    KW_REVOKE(): TerminalNode;
    KW_ALL(): TerminalNode;
    COMMA(): TerminalNode;
    KW_GRANT(): TerminalNode;
    KW_OPTION(): TerminalNode;
    KW_FROM(): TerminalNode;
    userOrRoleNames(): UserOrRoleNamesContext;
    ifExists(): IfExistsContext | undefined;
    KW_PRIVILEGES(): TerminalNode | undefined;
    ignoreUnknownUser(): IgnoreUnknownUserContext | undefined;
    constructor(ctx: RevokeStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ProxyAndRoleRevokeContext extends RevokeStatementContext {
    KW_REVOKE(): TerminalNode;
    userOrRoleNames(): UserOrRoleNamesContext[];
    userOrRoleNames(i: number): UserOrRoleNamesContext;
    KW_FROM(): TerminalNode;
    ifExists(): IfExistsContext | undefined;
    KW_PROXY(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    ignoreUnknownUser(): IgnoreUnknownUserContext | undefined;
    constructor(ctx: RevokeStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IgnoreUnknownUserContext extends ParserRuleContext {
    KW_IGNORE(): TerminalNode;
    KW_UNKNOWN(): TerminalNode;
    KW_USER(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PrivilegeObjectTypeContext extends ParserRuleContext {
    KW_TABLE(): TerminalNode | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetPasswordStatementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: SetPasswordStatementContext): void;
}
export declare class V57Context extends SetPasswordStatementContext {
    KW_SET(): TerminalNode;
    KW_PASSWORD(): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode;
    passwordFunctionClause(): PasswordFunctionClauseContext | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    userName(): UserNameContext | undefined;
    constructor(ctx: SetPasswordStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class V80Context extends SetPasswordStatementContext {
    KW_SET(): TerminalNode;
    KW_PASSWORD(): TerminalNode[];
    KW_PASSWORD(i: number): TerminalNode;
    KW_TO(): TerminalNode | undefined;
    KW_RANDOM(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode[];
    STRING_LITERAL(i: number): TerminalNode;
    KW_FOR(): TerminalNode | undefined;
    userName(): UserNameContext | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    KW_RETAIN(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    constructor(ctx: SetPasswordStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UserSpecificationContext extends ParserRuleContext {
    userName(): UserNameContext;
    userPasswordOption(): UserPasswordOptionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AlterUserAuthOptionContext extends ParserRuleContext {
    userName(): UserNameContext;
    KW_IDENTIFIED(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    authOptionClause(): AuthOptionClauseContext | undefined;
    KW_RANDOM(): TerminalNode | undefined;
    KW_PASSWORD(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    authenticationRule(): AuthenticationRuleContext | undefined;
    KW_DISCARD(): TerminalNode | undefined;
    KW_OLD(): TerminalNode | undefined;
    factor(): FactorContext[];
    factor(i: number): FactorContext;
    KW_ADD(): TerminalNode[];
    KW_ADD(i: number): TerminalNode;
    KW_MODIFY(): TerminalNode[];
    KW_MODIFY(i: number): TerminalNode;
    KW_DROP(): TerminalNode[];
    KW_DROP(i: number): TerminalNode;
    factorAuthOption(): FactorAuthOptionContext[];
    factorAuthOption(i: number): FactorAuthOptionContext;
    registrationOption(): RegistrationOptionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateUserAuthOptionContext extends ParserRuleContext {
    _authPlugin: UidContext;
    KW_IDENTIFIED(): TerminalNode;
    KW_BY(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_AND(): TerminalNode | undefined;
    createUserAuthOption(): CreateUserAuthOptionContext | undefined;
    KW_RANDOM(): TerminalNode | undefined;
    KW_PASSWORD(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    uid(): UidContext | undefined;
    KW_AS(): TerminalNode | undefined;
    createUserInitialAuthOption(): CreateUserInitialAuthOptionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateUserInitialAuthOptionContext extends ParserRuleContext {
    _authPlugin: UidContext;
    KW_INITIAL(): TerminalNode;
    KW_AUTHENTICATION(): TerminalNode;
    KW_IDENTIFIED(): TerminalNode;
    KW_BY(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_RANDOM(): TerminalNode | undefined;
    KW_PASSWORD(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    uid(): UidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UserAuthOptionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: UserAuthOptionContext): void;
}
export declare class HashAuthOptionContext extends UserAuthOptionContext {
    _hashed: Token;
    userName(): UserNameContext;
    KW_IDENTIFIED(): TerminalNode;
    KW_BY(): TerminalNode;
    KW_PASSWORD(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(ctx: UserAuthOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RandomAuthOptionContext extends UserAuthOptionContext {
    userName(): UserNameContext;
    KW_IDENTIFIED(): TerminalNode;
    KW_BY(): TerminalNode;
    KW_RANDOM(): TerminalNode;
    KW_PASSWORD(): TerminalNode;
    authOptionClause(): AuthOptionClauseContext;
    constructor(ctx: UserAuthOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class StringAuthOptionContext extends UserAuthOptionContext {
    userName(): UserNameContext;
    KW_IDENTIFIED(): TerminalNode;
    KW_BY(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    authOptionClause(): AuthOptionClauseContext;
    constructor(ctx: UserAuthOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ModuleAuthOptionContext extends UserAuthOptionContext {
    userName(): UserNameContext;
    KW_IDENTIFIED(): TerminalNode;
    KW_WITH(): TerminalNode;
    authenticationRule(): AuthenticationRuleContext;
    constructor(ctx: UserAuthOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SimpleAuthOptionContext extends UserAuthOptionContext {
    userName(): UserNameContext;
    constructor(ctx: UserAuthOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AuthOptionClauseContext extends ParserRuleContext {
    KW_REPLACE(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_RETAIN(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_PASSWORD(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AuthenticationRuleContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: AuthenticationRuleContext): void;
}
export declare class ModuleContext extends AuthenticationRuleContext {
    _authPlugin: UidContext;
    uid(): UidContext;
    authOptionClause(): AuthOptionClauseContext | undefined;
    KW_BY(): TerminalNode | undefined;
    KW_USING(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_RANDOM(): TerminalNode | undefined;
    KW_PASSWORD(): TerminalNode | undefined;
    constructor(ctx: AuthenticationRuleContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PasswordModuleOptionContext extends AuthenticationRuleContext {
    _authPlugin: UidContext;
    KW_USING(): TerminalNode;
    passwordFunctionClause(): PasswordFunctionClauseContext;
    uid(): UidContext;
    constructor(ctx: AuthenticationRuleContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TlsOptionContext extends ParserRuleContext {
    KW_SSL(): TerminalNode | undefined;
    KW_X509(): TerminalNode | undefined;
    KW_CIPHER(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_ISSUER(): TerminalNode | undefined;
    KW_SUBJECT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UserResourceOptionContext extends ParserRuleContext {
    KW_MAX_QUERIES_PER_HOUR(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext;
    KW_MAX_UPDATES_PER_HOUR(): TerminalNode | undefined;
    KW_MAX_CONNECTIONS_PER_HOUR(): TerminalNode | undefined;
    KW_MAX_USER_CONNECTIONS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UserPasswordOptionContext extends ParserRuleContext {
    _expireType: Token;
    KW_PASSWORD(): TerminalNode | undefined;
    KW_EXPIRE(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_NEVER(): TerminalNode | undefined;
    KW_INTERVAL(): TerminalNode | undefined;
    KW_HISTORY(): TerminalNode | undefined;
    KW_REUSE(): TerminalNode | undefined;
    KW_REQUIRE(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_OPTIONAL(): TerminalNode | undefined;
    KW_FAILED_LOGIN_ATTEMPTS(): TerminalNode | undefined;
    KW_PASSWORD_LOCK_TIME(): TerminalNode | undefined;
    KW_UNBOUNDED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UserLockOptionContext extends ParserRuleContext {
    _lockType: Token;
    KW_ACCOUNT(): TerminalNode;
    KW_LOCK(): TerminalNode | undefined;
    KW_UNLOCK(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FactorAuthOptionContext extends ParserRuleContext {
    _authPlugin: UidContext;
    KW_IDENTIFIED(): TerminalNode;
    KW_AS(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    uid(): UidContext | undefined;
    KW_BY(): TerminalNode | undefined;
    KW_RANDOM(): TerminalNode | undefined;
    KW_PASSWORD(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RegistrationOptionContext extends ParserRuleContext {
    factor(): FactorContext;
    KW_INITIATE(): TerminalNode | undefined;
    KW_REGISTRATION(): TerminalNode | undefined;
    KW_FINISH(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_CHALLENGE_RESPONSE(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_UNREGISTER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FactorContext extends ParserRuleContext {
    KW_FACTOR(): TerminalNode;
    TWO_DECIMAL(): TerminalNode | undefined;
    THREE_DECIMAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PrivelegeClauseContext extends ParserRuleContext {
    privilege(): PrivilegeContext;
    LR_BRACKET(): TerminalNode | undefined;
    columnNames(): ColumnNamesContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PrivilegeContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    KW_PRIVILEGES(): TerminalNode | undefined;
    KW_ALTER(): TerminalNode | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    KW_CREATE(): TerminalNode | undefined;
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_TABLES(): TerminalNode | undefined;
    KW_VIEW(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_TABLESPACE(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_DELETE(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_EVENT(): TerminalNode | undefined;
    KW_EXECUTE(): TerminalNode | undefined;
    KW_FILE(): TerminalNode | undefined;
    KW_GRANT(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    KW_INSERT(): TerminalNode | undefined;
    KW_LOCK(): TerminalNode | undefined;
    KW_PROCESS(): TerminalNode | undefined;
    KW_PROXY(): TerminalNode | undefined;
    KW_REFERENCES(): TerminalNode | undefined;
    KW_RELOAD(): TerminalNode | undefined;
    KW_REPLICATION(): TerminalNode | undefined;
    KW_CLIENT(): TerminalNode | undefined;
    KW_SLAVE(): TerminalNode | undefined;
    KW_SELECT(): TerminalNode | undefined;
    KW_SHOW(): TerminalNode | undefined;
    KW_DATABASES(): TerminalNode | undefined;
    KW_SHUTDOWN(): TerminalNode | undefined;
    KW_SUPER(): TerminalNode | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    KW_USAGE(): TerminalNode | undefined;
    KW_APPLICATION_PASSWORD_ADMIN(): TerminalNode | undefined;
    KW_AUDIT_ABORT_EXEMPT(): TerminalNode | undefined;
    KW_AUDIT_ADMIN(): TerminalNode | undefined;
    KW_AUTHENTICATION_POLICY_ADMIN(): TerminalNode | undefined;
    KW_BACKUP_ADMIN(): TerminalNode | undefined;
    KW_BINLOG_ADMIN(): TerminalNode | undefined;
    KW_BINLOG_ENCRYPTION_ADMIN(): TerminalNode | undefined;
    KW_CLONE_ADMIN(): TerminalNode | undefined;
    KW_CONNECTION_ADMIN(): TerminalNode | undefined;
    KW_ENCRYPTION_KEY_ADMIN(): TerminalNode | undefined;
    KW_FIREWALL_ADMIN(): TerminalNode | undefined;
    KW_FIREWALL_EXEMPT(): TerminalNode | undefined;
    KW_FIREWALL_USER(): TerminalNode | undefined;
    KW_FLUSH_OPTIMIZER_COSTS(): TerminalNode | undefined;
    KW_FLUSH_STATUS(): TerminalNode | undefined;
    KW_FLUSH_TABLES(): TerminalNode | undefined;
    KW_FLUSH_USER_RESOURCES(): TerminalNode | undefined;
    KW_GROUP_REPLICATION_ADMIN(): TerminalNode | undefined;
    KW_INNODB_REDO_LOG_ARCHIVE(): TerminalNode | undefined;
    KW_INNODB_REDO_LOG_ENABLE(): TerminalNode | undefined;
    KW_NDB_STORED_USER(): TerminalNode | undefined;
    KW_PASSWORDLESS_USER_ADMIN(): TerminalNode | undefined;
    KW_PERSIST_RO_VARIABLES_ADMIN(): TerminalNode | undefined;
    KW_REPLICATION_APPLIER(): TerminalNode | undefined;
    KW_REPLICATION_SLAVE_ADMIN(): TerminalNode | undefined;
    KW_RESOURCE_GROUP_ADMIN(): TerminalNode | undefined;
    KW_RESOURCE_GROUP_USER(): TerminalNode | undefined;
    KW_ROLE_ADMIN(): TerminalNode | undefined;
    KW_SERVICE_CONNECTION_ADMIN(): TerminalNode | undefined;
    KW_SESSION_VARIABLES_ADMIN(): TerminalNode | undefined;
    KW_SET_USER_ID(): TerminalNode | undefined;
    KW_SKIP_QUERY_REWRITE(): TerminalNode | undefined;
    KW_SHOW_ROUTINE(): TerminalNode | undefined;
    KW_SYSTEM_USER(): TerminalNode | undefined;
    KW_SYSTEM_VARIABLES_ADMIN(): TerminalNode | undefined;
    KW_TABLE_ENCRYPTION_ADMIN(): TerminalNode | undefined;
    KW_TP_CONNECTION_ADMIN(): TerminalNode | undefined;
    KW_VERSION_TOKEN_ADMIN(): TerminalNode | undefined;
    KW_XA_RECOVER_ADMIN(): TerminalNode | undefined;
    KW_LOAD(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_S3(): TerminalNode | undefined;
    KW_INTO(): TerminalNode | undefined;
    KW_INVOKE(): TerminalNode | undefined;
    KW_LAMBDA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PrivilegeLevelContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PrivilegeLevelContext): void;
}
export declare class CurrentSchemaPriviLevelContext extends PrivilegeLevelContext {
    STAR(): TerminalNode;
    constructor(ctx: PrivilegeLevelContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class GlobalPrivLevelContext extends PrivilegeLevelContext {
    STAR(): TerminalNode[];
    STAR(i: number): TerminalNode;
    DOT(): TerminalNode;
    constructor(ctx: PrivilegeLevelContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DefiniteSchemaPrivLevelContext extends PrivilegeLevelContext {
    uid(): UidContext;
    DOT(): TerminalNode;
    STAR(): TerminalNode;
    constructor(ctx: PrivilegeLevelContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DefiniteFullTablePrivLevelContext extends PrivilegeLevelContext {
    uid(): UidContext[];
    uid(i: number): UidContext;
    DOT(): TerminalNode;
    constructor(ctx: PrivilegeLevelContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DefiniteFullTablePrivLevel2Context extends PrivilegeLevelContext {
    uid(): UidContext;
    dottedId(): DottedIdContext;
    constructor(ctx: PrivilegeLevelContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DefiniteTablePrivLevelContext extends PrivilegeLevelContext {
    uid(): UidContext;
    constructor(ctx: PrivilegeLevelContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RenameUserClauseContext extends ParserRuleContext {
    _fromFirst: UserNameContext;
    _toFirst: UserNameContext;
    KW_TO(): TerminalNode;
    userName(): UserNameContext[];
    userName(i: number): UserNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AnalyzeTableContext extends ParserRuleContext {
    KW_ANALYZE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNames(): TableNamesContext | undefined;
    tableActionOption(): TableActionOptionContext | undefined;
    tableName(): TableNameContext | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    KW_HISTOGRAM(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    columnNames(): ColumnNamesContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    KW_BUCKETS(): TerminalNode | undefined;
    columnName(): ColumnNameContext | undefined;
    KW_USING(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CheckTableContext extends ParserRuleContext {
    KW_CHECK(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNames(): TableNamesContext;
    checkTableOption(): CheckTableOptionContext[];
    checkTableOption(i: number): CheckTableOptionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ChecksumTableContext extends ParserRuleContext {
    _actionOption: Token;
    KW_CHECKSUM(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNames(): TableNamesContext;
    KW_QUICK(): TerminalNode | undefined;
    KW_EXTENDED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class OptimizeTableContext extends ParserRuleContext {
    KW_OPTIMIZE(): TerminalNode;
    tableNames(): TableNamesContext;
    KW_TABLE(): TerminalNode | undefined;
    KW_TABLES(): TerminalNode | undefined;
    tableActionOption(): TableActionOptionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RepairTableContext extends ParserRuleContext {
    KW_REPAIR(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNames(): TableNamesContext;
    tableActionOption(): TableActionOptionContext | undefined;
    KW_QUICK(): TerminalNode | undefined;
    KW_EXTENDED(): TerminalNode | undefined;
    KW_USE_FRM(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableActionOptionContext extends ParserRuleContext {
    KW_NO_WRITE_TO_BINLOG(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CheckTableOptionContext extends ParserRuleContext {
    KW_FOR(): TerminalNode | undefined;
    KW_UPGRADE(): TerminalNode | undefined;
    KW_QUICK(): TerminalNode | undefined;
    KW_FAST(): TerminalNode | undefined;
    KW_MEDIUM(): TerminalNode | undefined;
    KW_EXTENDED(): TerminalNode | undefined;
    KW_CHANGED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CreateFunctionContext extends ParserRuleContext {
    _returnType: Token;
    KW_CREATE(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    functionNameCreate(): FunctionNameCreateContext;
    KW_RETURNS(): TerminalNode;
    KW_SONAME(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    KW_STRING(): TerminalNode | undefined;
    KW_INTEGER(): TerminalNode | undefined;
    KW_REAL(): TerminalNode | undefined;
    KW_DECIMAL(): TerminalNode | undefined;
    KW_AGGREGATE(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class InstallComponentContext extends ParserRuleContext {
    _component_name: UidContext;
    KW_INSTALL(): TerminalNode;
    KW_COMPONENT(): TerminalNode;
    uid(): UidContext[];
    uid(i: number): UidContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_SET(): TerminalNode | undefined;
    variableExpr(): VariableExprContext[];
    variableExpr(i: number): VariableExprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class VariableExprContext extends ParserRuleContext {
    _system_var_name: FullIdContext;
    EQUAL_SYMBOL(): TerminalNode;
    expression(): ExpressionContext;
    KW_GLOBAL(): TerminalNode | undefined;
    GLOBAL_ID(): TerminalNode | undefined;
    KW_PERSIST(): TerminalNode | undefined;
    PERSIST_ID(): TerminalNode | undefined;
    fullId(): FullIdContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UninstallComponentContext extends ParserRuleContext {
    _component_name: UidContext;
    KW_UNINSTALL(): TerminalNode;
    KW_COMPONENT(): TerminalNode;
    uid(): UidContext[];
    uid(i: number): UidContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class InstallPluginContext extends ParserRuleContext {
    _pluginName: UidContext;
    KW_INSTALL(): TerminalNode;
    KW_PLUGIN(): TerminalNode;
    KW_SONAME(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UninstallPluginContext extends ParserRuleContext {
    _pluginName: UidContext;
    KW_UNINSTALL(): TerminalNode;
    KW_PLUGIN(): TerminalNode;
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CloneStatementContext extends ParserRuleContext {
    KW_CLONE(): TerminalNode;
    KW_LOCAL(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_DIRECTORY(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode[];
    STRING_LITERAL(i: number): TerminalNode;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    KW_INSTANCE(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    userHostPort(): UserHostPortContext | undefined;
    KW_IDENTIFIED(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    KW_REQUIRE(): TerminalNode | undefined;
    KW_SSL(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetStatementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: SetStatementContext): void;
}
export declare class SetVariableContext extends SetStatementContext {
    KW_SET(): TerminalNode;
    variableClause(): VariableClauseContext[];
    variableClause(i: number): VariableClauseContext;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    VAR_ASSIGN(): TerminalNode[];
    VAR_ASSIGN(i: number): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_ON(): TerminalNode[];
    KW_ON(i: number): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: SetStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetCharsetContext extends SetStatementContext {
    KW_SET(): TerminalNode;
    charSet(): CharSetContext;
    charsetName(): CharsetNameContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(ctx: SetStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetNamesContext extends SetStatementContext {
    KW_SET(): TerminalNode;
    KW_NAMES(): TerminalNode;
    charsetName(): CharsetNameContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_COLLATE(): TerminalNode | undefined;
    collationName(): CollationNameContext | undefined;
    constructor(ctx: SetStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetPasswordContext extends SetStatementContext {
    setPasswordStatement(): SetPasswordStatementContext;
    constructor(ctx: SetStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetTransactionContext extends SetStatementContext {
    setTransactionStatement(): SetTransactionStatementContext;
    constructor(ctx: SetStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetAutocommitContext extends SetStatementContext {
    setAutocommitStatement(): SetAutocommitStatementContext;
    constructor(ctx: SetStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SetNewValueInsideTriggerContext extends SetStatementContext {
    _system_var_name: FullIdContext;
    KW_SET(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    fullId(): FullIdContext[];
    fullId(i: number): FullIdContext;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    VAR_ASSIGN(): TerminalNode[];
    VAR_ASSIGN(i: number): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: SetStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowStatementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ShowStatementContext): void;
}
export declare class ShowMasterLogsContext extends ShowStatementContext {
    _logFormat: Token;
    KW_SHOW(): TerminalNode;
    KW_LOGS(): TerminalNode;
    KW_BINARY(): TerminalNode | undefined;
    KW_MASTER(): TerminalNode | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowLogEventsContext extends ShowStatementContext {
    _logFormat: Token;
    _filename: Token;
    _fromPosition: DecimalLiteralContext;
    _offset: DecimalLiteralContext;
    _rowCount: DecimalLiteralContext;
    KW_SHOW(): TerminalNode;
    KW_EVENTS(): TerminalNode;
    KW_BINLOG(): TerminalNode | undefined;
    KW_RELAYLOG(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_LIMIT(): TerminalNode | undefined;
    channelOption(): ChannelOptionContext | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    COMMA(): TerminalNode | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowObjectFilterContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    showCommonEntity(): ShowCommonEntityContext;
    showFilter(): ShowFilterContext | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowColumnsContext extends ShowStatementContext {
    _columnsFormat: Token;
    _tableFormat: Token;
    _schemaFormat: Token;
    KW_SHOW(): TerminalNode;
    tableName(): TableNameContext;
    KW_COLUMNS(): TerminalNode | undefined;
    KW_FIELDS(): TerminalNode | undefined;
    KW_FROM(): TerminalNode[];
    KW_FROM(i: number): TerminalNode;
    KW_IN(): TerminalNode[];
    KW_IN(i: number): TerminalNode;
    KW_EXTENDED(): TerminalNode | undefined;
    KW_FULL(): TerminalNode | undefined;
    databaseName(): DatabaseNameContext | undefined;
    showFilter(): ShowFilterContext | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowCreateDbContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    KW_CREATE(): TerminalNode;
    databaseNameCreate(): DatabaseNameCreateContext;
    KW_DATABASE(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowCreateFullIdObjectContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    KW_CREATE(): TerminalNode;
    fullId(): FullIdContext;
    KW_EVENT(): TerminalNode | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowCreateFunctionContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    KW_CREATE(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    functionNameCreate(): FunctionNameCreateContext;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowCreateViewContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    KW_CREATE(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewNameCreate(): ViewNameCreateContext;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowCreateTableContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowCreateUserContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    KW_CREATE(): TerminalNode;
    KW_USER(): TerminalNode;
    userName(): UserNameContext;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowEngineContext extends ShowStatementContext {
    _engineOption: Token;
    KW_SHOW(): TerminalNode;
    KW_ENGINE(): TerminalNode;
    engineName(): EngineNameContext;
    KW_STATUS(): TerminalNode | undefined;
    KW_MUTEX(): TerminalNode | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowGlobalInfoContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    showGlobalInfoClause(): ShowGlobalInfoClauseContext;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowErrorsContext extends ShowStatementContext {
    _errorFormat: Token;
    _offset: DecimalLiteralContext;
    _rowCount: DecimalLiteralContext;
    KW_SHOW(): TerminalNode;
    KW_ERRORS(): TerminalNode | undefined;
    KW_WARNINGS(): TerminalNode | undefined;
    KW_LIMIT(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    COMMA(): TerminalNode | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowCountErrorsContext extends ShowStatementContext {
    _errorFormat: Token;
    KW_SHOW(): TerminalNode;
    KW_COUNT(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    STAR(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    KW_ERRORS(): TerminalNode | undefined;
    KW_WARNINGS(): TerminalNode | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowSchemaFilterContext extends ShowStatementContext {
    _schemaFormat: Token;
    KW_SHOW(): TerminalNode;
    showSchemaEntity(): ShowSchemaEntityContext;
    databaseName(): DatabaseNameContext | undefined;
    showFilter(): ShowFilterContext | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowPercedureCodeContext extends ShowStatementContext {
    _proc_name: FullIdContext;
    KW_SHOW(): TerminalNode;
    KW_PROCEDURE(): TerminalNode;
    KW_CODE(): TerminalNode;
    fullId(): FullIdContext;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowFunctionCodeContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    KW_CODE(): TerminalNode;
    functionName(): FunctionNameContext;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowGrantsContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    KW_GRANTS(): TerminalNode;
    KW_FOR(): TerminalNode | undefined;
    userOrRoleName(): UserOrRoleNameContext | undefined;
    KW_USING(): TerminalNode | undefined;
    userOrRoleNames(): UserOrRoleNamesContext | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowIndexesContext extends ShowStatementContext {
    _indexFormat: Token;
    _tableFormat: Token;
    _schemaFormat: Token;
    KW_SHOW(): TerminalNode;
    tableName(): TableNameContext;
    KW_INDEX(): TerminalNode | undefined;
    KW_INDEXES(): TerminalNode | undefined;
    KW_KEYS(): TerminalNode | undefined;
    KW_FROM(): TerminalNode[];
    KW_FROM(i: number): TerminalNode;
    KW_IN(): TerminalNode[];
    KW_IN(i: number): TerminalNode;
    KW_EXTENDED(): TerminalNode | undefined;
    databaseName(): DatabaseNameContext | undefined;
    KW_WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowOpenTablesContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    KW_OPEN(): TerminalNode;
    KW_TABLES(): TerminalNode;
    databaseName(): DatabaseNameContext | undefined;
    showFilter(): ShowFilterContext | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowProfileContext extends ShowStatementContext {
    _queryCount: DecimalLiteralContext;
    _rowCount: DecimalLiteralContext;
    _offset: DecimalLiteralContext;
    KW_SHOW(): TerminalNode;
    KW_PROFILE(): TerminalNode;
    showProfileType(): ShowProfileTypeContext[];
    showProfileType(i: number): ShowProfileTypeContext;
    KW_FOR(): TerminalNode | undefined;
    KW_QUERY(): TerminalNode | undefined;
    KW_LIMIT(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_OFFSET(): TerminalNode | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowSlaveStatusContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    KW_STATUS(): TerminalNode;
    KW_REPLICA(): TerminalNode | undefined;
    KW_SLAVE(): TerminalNode | undefined;
    channelOption(): ChannelOptionContext | undefined;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowReplicasContext extends ShowStatementContext {
    KW_SHOW(): TerminalNode;
    KW_REPLICAS(): TerminalNode;
    constructor(ctx: ShowStatementContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class VariableClauseContext extends ParserRuleContext {
    _target: UidContext;
    LOCAL_ID(): TerminalNode | undefined;
    GLOBAL_ID(): TerminalNode | undefined;
    uid(): UidContext | undefined;
    KW_GLOBAL(): TerminalNode | undefined;
    KW_PERSIST(): TerminalNode | undefined;
    KW_PERSIST_ONLY(): TerminalNode | undefined;
    KW_SESSION(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    AT_SIGN(): TerminalNode[];
    AT_SIGN(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowCommonEntityContext extends ParserRuleContext {
    KW_CHARACTER(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_CHARSET(): TerminalNode | undefined;
    KW_COLLATION(): TerminalNode | undefined;
    KW_DATABASES(): TerminalNode | undefined;
    KW_SCHEMAS(): TerminalNode | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    KW_STATUS(): TerminalNode | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    KW_VARIABLES(): TerminalNode | undefined;
    KW_GLOBAL(): TerminalNode | undefined;
    KW_SESSION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowFilterContext extends ParserRuleContext {
    KW_LIKE(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    KW_WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowGlobalInfoClauseContext extends ParserRuleContext {
    KW_ENGINES(): TerminalNode | undefined;
    KW_STORAGE(): TerminalNode | undefined;
    KW_MASTER(): TerminalNode | undefined;
    KW_STATUS(): TerminalNode | undefined;
    KW_PLUGINS(): TerminalNode | undefined;
    KW_PRIVILEGES(): TerminalNode | undefined;
    KW_PROCESSLIST(): TerminalNode | undefined;
    KW_FULL(): TerminalNode | undefined;
    KW_PROFILES(): TerminalNode | undefined;
    KW_SLAVE(): TerminalNode | undefined;
    KW_HOSTS(): TerminalNode | undefined;
    KW_AUTHORS(): TerminalNode | undefined;
    KW_CONTRIBUTORS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowSchemaEntityContext extends ParserRuleContext {
    KW_EVENTS(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    KW_STATUS(): TerminalNode | undefined;
    KW_TABLES(): TerminalNode | undefined;
    KW_EXTENDED(): TerminalNode | undefined;
    KW_FULL(): TerminalNode | undefined;
    KW_TRIGGERS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShowProfileTypeContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    KW_BLOCK(): TerminalNode | undefined;
    KW_IO(): TerminalNode | undefined;
    KW_CONTEXT(): TerminalNode | undefined;
    KW_SWITCHES(): TerminalNode | undefined;
    KW_CPU(): TerminalNode | undefined;
    KW_IPC(): TerminalNode | undefined;
    KW_MEMORY(): TerminalNode | undefined;
    KW_PAGE(): TerminalNode | undefined;
    KW_FAULTS(): TerminalNode | undefined;
    KW_SOURCE(): TerminalNode | undefined;
    KW_SWAPS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class BinlogStatementContext extends ParserRuleContext {
    KW_BINLOG(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CacheIndexStatementContext extends ParserRuleContext {
    KW_CACHE(): TerminalNode;
    KW_INDEX(): TerminalNode;
    KW_IN(): TerminalNode;
    databaseName(): DatabaseNameContext;
    tableIndex(): TableIndexContext[];
    tableIndex(i: number): TableIndexContext;
    tableName(): TableNameContext | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    partitionNames(): PartitionNamesContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FlushStatementContext extends ParserRuleContext {
    KW_FLUSH(): TerminalNode;
    flushOption(): FlushOptionContext[];
    flushOption(i: number): FlushOptionContext;
    tableActionOption(): TableActionOptionContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class KillStatementContext extends ParserRuleContext {
    _connectionFormat: Token;
    KW_KILL(): TerminalNode;
    expression(): ExpressionContext;
    KW_CONNECTION(): TerminalNode | undefined;
    KW_QUERY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LoadIndexIntoCacheContext extends ParserRuleContext {
    KW_LOAD(): TerminalNode;
    KW_INDEX(): TerminalNode;
    KW_INTO(): TerminalNode;
    KW_CACHE(): TerminalNode;
    loadedTableIndexes(): LoadedTableIndexesContext[];
    loadedTableIndexes(i: number): LoadedTableIndexesContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ResetStatementContext extends ParserRuleContext {
    KW_RESET(): TerminalNode;
    resetOption(): ResetOptionContext[];
    resetOption(i: number): ResetOptionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ResetOptionContext extends ParserRuleContext {
    KW_MASTER(): TerminalNode | undefined;
    KW_REPLICA(): TerminalNode | undefined;
    KW_QUERY(): TerminalNode | undefined;
    KW_CACHE(): TerminalNode | undefined;
    KW_SLAVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ResetPersistContext extends ParserRuleContext {
    _system_var_name: UidContext;
    KW_RESET(): TerminalNode;
    KW_PERSIST(): TerminalNode;
    uid(): UidContext | undefined;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ResetAllChannelContext extends ParserRuleContext {
    KW_RESET(): TerminalNode;
    KW_REPLICA(): TerminalNode | undefined;
    KW_SLAVE(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    channelOption(): ChannelOptionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ReStartStatementContext extends ParserRuleContext {
    KW_RESTART(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ShutdownStatementContext extends ParserRuleContext {
    KW_SHUTDOWN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableIndexContext extends ParserRuleContext {
    _indexFormat: Token;
    tableName(): TableNameContext;
    LR_BRACKET(): TerminalNode | undefined;
    indexNames(): IndexNamesContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FlushOptionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: FlushOptionContext): void;
}
export declare class SimpleFlushOptionContext extends FlushOptionContext {
    KW_DES_KEY_FILE(): TerminalNode | undefined;
    KW_HOSTS(): TerminalNode | undefined;
    KW_LOGS(): TerminalNode | undefined;
    KW_OPTIMIZER_COSTS(): TerminalNode | undefined;
    KW_PRIVILEGES(): TerminalNode | undefined;
    KW_QUERY(): TerminalNode | undefined;
    KW_CACHE(): TerminalNode | undefined;
    KW_STATUS(): TerminalNode | undefined;
    KW_USER_RESOURCES(): TerminalNode | undefined;
    KW_TABLES(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_READ(): TerminalNode | undefined;
    KW_LOCK(): TerminalNode | undefined;
    KW_BINARY(): TerminalNode | undefined;
    KW_ENGINE(): TerminalNode | undefined;
    KW_ERROR(): TerminalNode | undefined;
    KW_GENERAL(): TerminalNode | undefined;
    KW_RELAY(): TerminalNode | undefined;
    KW_SLOW(): TerminalNode | undefined;
    constructor(ctx: FlushOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ChannelFlushOptionContext extends FlushOptionContext {
    KW_RELAY(): TerminalNode;
    KW_LOGS(): TerminalNode;
    channelOption(): ChannelOptionContext | undefined;
    constructor(ctx: FlushOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableFlushOptionContext extends FlushOptionContext {
    KW_TABLE(): TerminalNode | undefined;
    KW_TABLES(): TerminalNode | undefined;
    tableNames(): TableNamesContext | undefined;
    flushTableOption(): FlushTableOptionContext | undefined;
    constructor(ctx: FlushOptionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FlushTableOptionContext extends ParserRuleContext {
    KW_WITH(): TerminalNode | undefined;
    KW_READ(): TerminalNode | undefined;
    KW_LOCK(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_EXPORT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LoadedTableIndexesContext extends ParserRuleContext {
    _indexFormat: Token;
    tableName(): TableNameContext;
    KW_PARTITION(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    indexNames(): IndexNamesContext | undefined;
    KW_IGNORE(): TerminalNode | undefined;
    KW_LEAVES(): TerminalNode | undefined;
    partitionNames(): PartitionNamesContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SimpleDescribeStatementContext extends ParserRuleContext {
    _command: Token;
    _column: ColumnNameContext;
    _pattern: Token;
    tableName(): TableNameContext;
    KW_EXPLAIN(): TerminalNode | undefined;
    KW_DESCRIBE(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    columnName(): ColumnNameContext | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FullDescribeStatementContext extends ParserRuleContext {
    _command: Token;
    describeObjectClause(): DescribeObjectClauseContext;
    KW_EXPLAIN(): TerminalNode | undefined;
    KW_DESCRIBE(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    KW_EXTENDED(): TerminalNode | undefined;
    KW_PARTITIONS(): TerminalNode | undefined;
    KW_FORMAT(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    KW_TRADITIONAL(): TerminalNode | undefined;
    KW_JSON(): TerminalNode | undefined;
    KW_TREE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AnalyzeDescribeStatementContext extends ParserRuleContext {
    _command: Token;
    KW_ANALYZE(): TerminalNode;
    selectStatement(): SelectStatementContext;
    KW_EXPLAIN(): TerminalNode | undefined;
    KW_DESCRIBE(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    KW_FORMAT(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    KW_TREE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HelpStatementContext extends ParserRuleContext {
    KW_HELP(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UseStatementContext extends ParserRuleContext {
    KW_USE(): TerminalNode;
    databaseName(): DatabaseNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SignalStatementContext extends ParserRuleContext {
    KW_SIGNAL(): TerminalNode;
    ID(): TerminalNode | undefined;
    REVERSE_QUOTE_ID(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    signalConditionInformation(): SignalConditionInformationContext[];
    signalConditionInformation(i: number): SignalConditionInformationContext;
    KW_SQLSTATE(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ResignalStatementContext extends ParserRuleContext {
    KW_RESIGNAL(): TerminalNode;
    ID(): TerminalNode | undefined;
    REVERSE_QUOTE_ID(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    signalConditionInformation(): SignalConditionInformationContext[];
    signalConditionInformation(i: number): SignalConditionInformationContext;
    KW_SQLSTATE(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SignalConditionInformationContext extends ParserRuleContext {
    EQUAL_SYMBOL(): TerminalNode;
    KW_CLASS_ORIGIN(): TerminalNode | undefined;
    KW_SUBCLASS_ORIGIN(): TerminalNode | undefined;
    KW_MESSAGE_TEXT(): TerminalNode | undefined;
    KW_MYSQL_ERRNO(): TerminalNode | undefined;
    KW_CONSTRAINT_CATALOG(): TerminalNode | undefined;
    KW_CONSTRAINT_SCHEMA(): TerminalNode | undefined;
    KW_CONSTRAINT_NAME(): TerminalNode | undefined;
    KW_CATALOG_NAME(): TerminalNode | undefined;
    KW_SCHEMA_NAME(): TerminalNode | undefined;
    KW_TABLE_NAME(): TerminalNode | undefined;
    KW_COLUMN_NAME(): TerminalNode | undefined;
    KW_CURSOR_NAME(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    DECIMAL_LITERAL(): TerminalNode | undefined;
    mysqlVariable(): MysqlVariableContext | undefined;
    simpleId(): SimpleIdContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class WithStatementContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    commonTableExpressions(): CommonTableExpressionsContext[];
    commonTableExpressions(i: number): CommonTableExpressionsContext;
    KW_RECURSIVE(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableStatementContext extends ParserRuleContext {
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    orderByClause(): OrderByClauseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DiagnosticsStatementContext extends ParserRuleContext {
    KW_GET(): TerminalNode;
    KW_DIAGNOSTICS(): TerminalNode;
    KW_CURRENT(): TerminalNode | undefined;
    KW_STACKED(): TerminalNode | undefined;
    variableClause(): VariableClauseContext[];
    variableClause(i: number): VariableClauseContext;
    EQUAL_SYMBOL(): TerminalNode[];
    EQUAL_SYMBOL(i: number): TerminalNode;
    KW_CONDITION(): TerminalNode | undefined;
    diagnosticsConditionInformationName(): DiagnosticsConditionInformationNameContext[];
    diagnosticsConditionInformationName(i: number): DiagnosticsConditionInformationNameContext;
    KW_NUMBER(): TerminalNode[];
    KW_NUMBER(i: number): TerminalNode;
    KW_ROW_COUNT(): TerminalNode[];
    KW_ROW_COUNT(i: number): TerminalNode;
    decimalLiteral(): DecimalLiteralContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DiagnosticsConditionInformationNameContext extends ParserRuleContext {
    KW_CLASS_ORIGIN(): TerminalNode | undefined;
    KW_SUBCLASS_ORIGIN(): TerminalNode | undefined;
    KW_RETURNED_SQLSTATE(): TerminalNode | undefined;
    KW_MESSAGE_TEXT(): TerminalNode | undefined;
    KW_MYSQL_ERRNO(): TerminalNode | undefined;
    KW_CONSTRAINT_CATALOG(): TerminalNode | undefined;
    KW_CONSTRAINT_SCHEMA(): TerminalNode | undefined;
    KW_CONSTRAINT_NAME(): TerminalNode | undefined;
    KW_CATALOG_NAME(): TerminalNode | undefined;
    KW_SCHEMA_NAME(): TerminalNode | undefined;
    KW_TABLE_NAME(): TerminalNode | undefined;
    KW_COLUMN_NAME(): TerminalNode | undefined;
    KW_CURSOR_NAME(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DescribeObjectClauseContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: DescribeObjectClauseContext): void;
}
export declare class DescribeStatementsContext extends DescribeObjectClauseContext {
    selectStatement(): SelectStatementContext | undefined;
    deleteStatement(): DeleteStatementContext | undefined;
    insertStatement(): InsertStatementContext | undefined;
    replaceStatement(): ReplaceStatementContext | undefined;
    updateStatement(): UpdateStatementContext | undefined;
    constructor(ctx: DescribeObjectClauseContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DescribeConnectionContext extends DescribeObjectClauseContext {
    _connection_id: UidContext;
    KW_FOR(): TerminalNode;
    KW_CONNECTION(): TerminalNode;
    uid(): UidContext;
    constructor(ctx: DescribeObjectClauseContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DatabaseNameCreateContext extends ParserRuleContext {
    fullId(): FullIdContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DatabaseNameContext extends ParserRuleContext {
    fullId(): FullIdContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FunctionNameCreateContext extends ParserRuleContext {
    fullId(): FullIdContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FunctionNameContext extends ParserRuleContext {
    fullId(): FullIdContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ViewNameCreateContext extends ParserRuleContext {
    fullId(): FullIdContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ViewNameContext extends ParserRuleContext {
    fullId(): FullIdContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IndexNameCreateContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IndexNamesContext extends ParserRuleContext {
    indexName(): IndexNameContext[];
    indexName(i: number): IndexNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IndexNameContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class GroupNameCreateContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class GroupNameContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableNameCreateContext extends ParserRuleContext {
    fullId(): FullIdContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableNamesContext extends ParserRuleContext {
    tableName(): TableNameContext[];
    tableName(i: number): TableNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TableNameContext extends ParserRuleContext {
    fullId(): FullIdContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UserOrRoleNamesContext extends ParserRuleContext {
    userOrRoleName(): UserOrRoleNameContext[];
    userOrRoleName(i: number): UserOrRoleNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UserOrRoleNameContext extends ParserRuleContext {
    userName(): UserNameContext | undefined;
    uid(): UidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ColumnNameCreateContext extends ParserRuleContext {
    uid(): UidContext | undefined;
    dottedId(): DottedIdContext[];
    dottedId(i: number): DottedIdContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ColumnNamesContext extends ParserRuleContext {
    columnName(): ColumnNameContext[];
    columnName(i: number): ColumnNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ColumnNameContext extends ParserRuleContext {
    uid(): UidContext | undefined;
    dottedId(): DottedIdContext[];
    dottedId(i: number): DottedIdContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TablespaceNameCreateContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TablespaceNameContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionNameCreateContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionNamesContext extends ParserRuleContext {
    partitionName(): PartitionNameContext[];
    partitionName(i: number): PartitionNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionNameContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IndexColumnNameContext extends ParserRuleContext {
    _sortType: Token;
    uid(): UidContext | undefined;
    expression(): ExpressionContext | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    KW_ASC(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UserHostPortContext extends ParserRuleContext {
    userAtHost(): UserAtHostContext;
    COLON_SYMB(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UserAtHostContext extends ParserRuleContext {
    simpleUserName(): SimpleUserNameContext;
    HOST_IP_ADDRESS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SimpleUserNameContext extends ParserRuleContext {
    STRING_LITERAL(): TerminalNode | undefined;
    ID(): TerminalNode | undefined;
    KW_ADMIN(): TerminalNode | undefined;
    keywordsCanBeId(): KeywordsCanBeIdContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HostNameContext extends ParserRuleContext {
    LOCAL_ID(): TerminalNode | undefined;
    HOST_IP_ADDRESS(): TerminalNode | undefined;
    AT_SIGN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UserNameContext extends ParserRuleContext {
    simpleUserName(): SimpleUserNameContext | undefined;
    hostName(): HostNameContext | undefined;
    currentUserExpression(): CurrentUserExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class MysqlVariableContext extends ParserRuleContext {
    LOCAL_ID(): TerminalNode | undefined;
    GLOBAL_ID(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CharsetNameContext extends ParserRuleContext {
    KW_BINARY(): TerminalNode | undefined;
    charsetNameBase(): CharsetNameBaseContext | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    CHARSET_REVERSE_QOUTE_STRING(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CollationNameContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class EngineNameContext extends ParserRuleContext {
    engineNameBase(): EngineNameBaseContext | undefined;
    ID(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class EngineNameBaseContext extends ParserRuleContext {
    KW_ARCHIVE(): TerminalNode | undefined;
    KW_BLACKHOLE(): TerminalNode | undefined;
    KW_CONNECT(): TerminalNode | undefined;
    KW_CSV(): TerminalNode | undefined;
    KW_FEDERATED(): TerminalNode | undefined;
    KW_INNODB(): TerminalNode | undefined;
    KW_MEMORY(): TerminalNode | undefined;
    KW_MRG_MYISAM(): TerminalNode | undefined;
    KW_MYISAM(): TerminalNode | undefined;
    KW_NDB(): TerminalNode | undefined;
    KW_NDBCLUSTER(): TerminalNode | undefined;
    KW_PERFORMANCE_SCHEMA(): TerminalNode | undefined;
    KW_TOKUDB(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UuidSetContext extends ParserRuleContext {
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    MINUS(): TerminalNode[];
    MINUS(i: number): TerminalNode;
    COLON_SYMB(): TerminalNode[];
    COLON_SYMB(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class XidContext extends ParserRuleContext {
    _globalTableUid: XuidStringIdContext;
    _qualifier: XuidStringIdContext;
    _idFormat: DecimalLiteralContext;
    xuidStringId(): XuidStringIdContext[];
    xuidStringId(i: number): XuidStringIdContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    decimalLiteral(): DecimalLiteralContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class XuidStringIdContext extends ParserRuleContext {
    STRING_LITERAL(): TerminalNode | undefined;
    BIT_STRING(): TerminalNode | undefined;
    HEXADECIMAL_LITERAL(): TerminalNode[];
    HEXADECIMAL_LITERAL(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FullIdContext extends ParserRuleContext {
    uid(): UidContext;
    dottedId(): DottedIdContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UidListContext extends ParserRuleContext {
    uid(): UidContext[];
    uid(i: number): UidContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UidContext extends ParserRuleContext {
    simpleId(): SimpleIdContext | undefined;
    CHARSET_REVERSE_QOUTE_STRING(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SimpleIdContext extends ParserRuleContext {
    ID(): TerminalNode | undefined;
    charsetNameBase(): CharsetNameBaseContext | undefined;
    transactionLevelBase(): TransactionLevelBaseContext | undefined;
    engineNameBase(): EngineNameBaseContext | undefined;
    privilegesBase(): PrivilegesBaseContext | undefined;
    intervalTypeBase(): IntervalTypeBaseContext | undefined;
    dataTypeBase(): DataTypeBaseContext | undefined;
    keywordsCanBeId(): KeywordsCanBeIdContext | undefined;
    scalarFunctionName(): ScalarFunctionNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DottedIdContext extends ParserRuleContext {
    DOT(): TerminalNode;
    ID(): TerminalNode | undefined;
    uid(): UidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DecimalLiteralContext extends ParserRuleContext {
    DECIMAL_LITERAL(): TerminalNode | undefined;
    ZERO_DECIMAL(): TerminalNode | undefined;
    ONE_DECIMAL(): TerminalNode | undefined;
    TWO_DECIMAL(): TerminalNode | undefined;
    THREE_DECIMAL(): TerminalNode | undefined;
    REAL_LITERAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FileSizeLiteralContext extends ParserRuleContext {
    FILESIZE_LITERAL(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class StringLiteralContext extends ParserRuleContext {
    STRING_LITERAL(): TerminalNode[];
    STRING_LITERAL(i: number): TerminalNode;
    START_NATIONAL_STRING_LITERAL(): TerminalNode | undefined;
    STRING_CHARSET_NAME(): TerminalNode | undefined;
    KW_COLLATE(): TerminalNode | undefined;
    collationName(): CollationNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class BooleanLiteralContext extends ParserRuleContext {
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class HexadecimalLiteralContext extends ParserRuleContext {
    HEXADECIMAL_LITERAL(): TerminalNode;
    STRING_CHARSET_NAME(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class NullNotnullContext extends ParserRuleContext {
    KW_NULL_LITERAL(): TerminalNode | undefined;
    NULL_SPEC_LITERAL(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ConstantContext extends ParserRuleContext {
    _nullLiteral: Token;
    stringLiteral(): StringLiteralContext | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    MINUS(): TerminalNode | undefined;
    hexadecimalLiteral(): HexadecimalLiteralContext | undefined;
    booleanLiteral(): BooleanLiteralContext | undefined;
    REAL_LITERAL(): TerminalNode | undefined;
    BIT_STRING(): TerminalNode | undefined;
    KW_NULL_LITERAL(): TerminalNode | undefined;
    NULL_SPEC_LITERAL(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DataTypeContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: DataTypeContext): void;
}
export declare class StringDataTypeContext extends DataTypeContext {
    _typeName: Token;
    KW_CHAR(): TerminalNode | undefined;
    KW_CHARACTER(): TerminalNode | undefined;
    KW_VARCHAR(): TerminalNode | undefined;
    KW_TINYTEXT(): TerminalNode | undefined;
    KW_TEXT(): TerminalNode | undefined;
    KW_MEDIUMTEXT(): TerminalNode | undefined;
    KW_LONGTEXT(): TerminalNode | undefined;
    KW_NCHAR(): TerminalNode | undefined;
    KW_NVARCHAR(): TerminalNode | undefined;
    KW_LONG(): TerminalNode | undefined;
    KW_VARYING(): TerminalNode | undefined;
    lengthOneDimension(): LengthOneDimensionContext | undefined;
    KW_BINARY(): TerminalNode[];
    KW_BINARY(i: number): TerminalNode;
    charSet(): CharSetContext | undefined;
    charsetName(): CharsetNameContext | undefined;
    KW_COLLATE(): TerminalNode | undefined;
    collationName(): CollationNameContext | undefined;
    constructor(ctx: DataTypeContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class NationalVaryingStringDataTypeContext extends DataTypeContext {
    _typeName: Token;
    KW_NATIONAL(): TerminalNode;
    KW_VARYING(): TerminalNode;
    KW_CHAR(): TerminalNode | undefined;
    KW_CHARACTER(): TerminalNode | undefined;
    lengthOneDimension(): LengthOneDimensionContext | undefined;
    KW_BINARY(): TerminalNode | undefined;
    constructor(ctx: DataTypeContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class NationalStringDataTypeContext extends DataTypeContext {
    _typeName: Token;
    KW_NATIONAL(): TerminalNode | undefined;
    KW_VARCHAR(): TerminalNode | undefined;
    KW_CHARACTER(): TerminalNode | undefined;
    KW_CHAR(): TerminalNode | undefined;
    lengthOneDimension(): LengthOneDimensionContext | undefined;
    KW_BINARY(): TerminalNode | undefined;
    KW_NCHAR(): TerminalNode | undefined;
    constructor(ctx: DataTypeContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DimensionDataTypeContext extends DataTypeContext {
    _typeName: Token;
    KW_TINYINT(): TerminalNode | undefined;
    KW_SMALLINT(): TerminalNode | undefined;
    KW_MEDIUMINT(): TerminalNode | undefined;
    KW_INT(): TerminalNode | undefined;
    KW_INTEGER(): TerminalNode | undefined;
    KW_BIGINT(): TerminalNode | undefined;
    KW_MIDDLEINT(): TerminalNode | undefined;
    KW_INT1(): TerminalNode | undefined;
    KW_INT2(): TerminalNode | undefined;
    KW_INT3(): TerminalNode | undefined;
    KW_INT4(): TerminalNode | undefined;
    KW_INT8(): TerminalNode | undefined;
    lengthOneDimension(): LengthOneDimensionContext | undefined;
    KW_SIGNED(): TerminalNode[];
    KW_SIGNED(i: number): TerminalNode;
    KW_UNSIGNED(): TerminalNode[];
    KW_UNSIGNED(i: number): TerminalNode;
    KW_ZEROFILL(): TerminalNode[];
    KW_ZEROFILL(i: number): TerminalNode;
    KW_REAL(): TerminalNode | undefined;
    lengthTwoDimension(): LengthTwoDimensionContext | undefined;
    KW_DOUBLE(): TerminalNode | undefined;
    KW_PRECISION(): TerminalNode | undefined;
    KW_DECIMAL(): TerminalNode | undefined;
    KW_DEC(): TerminalNode | undefined;
    KW_FIXED(): TerminalNode | undefined;
    KW_NUMERIC(): TerminalNode | undefined;
    KW_FLOAT(): TerminalNode | undefined;
    KW_FLOAT4(): TerminalNode | undefined;
    KW_FLOAT8(): TerminalNode | undefined;
    lengthTwoOptionalDimension(): LengthTwoOptionalDimensionContext | undefined;
    KW_BIT(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_TIMESTAMP(): TerminalNode | undefined;
    KW_DATETIME(): TerminalNode | undefined;
    KW_BINARY(): TerminalNode | undefined;
    KW_VARBINARY(): TerminalNode | undefined;
    KW_BLOB(): TerminalNode | undefined;
    KW_YEAR(): TerminalNode | undefined;
    constructor(ctx: DataTypeContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SimpleDataTypeContext extends DataTypeContext {
    _typeName: Token;
    KW_DATE(): TerminalNode | undefined;
    KW_TINYBLOB(): TerminalNode | undefined;
    KW_MEDIUMBLOB(): TerminalNode | undefined;
    KW_LONGBLOB(): TerminalNode | undefined;
    KW_BOOL(): TerminalNode | undefined;
    KW_BOOLEAN(): TerminalNode | undefined;
    KW_SERIAL(): TerminalNode | undefined;
    constructor(ctx: DataTypeContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CollectionDataTypeContext extends DataTypeContext {
    _typeName: Token;
    collectionOptions(): CollectionOptionsContext;
    KW_ENUM(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_BINARY(): TerminalNode | undefined;
    charSet(): CharSetContext | undefined;
    charsetName(): CharsetNameContext | undefined;
    constructor(ctx: DataTypeContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SpatialDataTypeContext extends DataTypeContext {
    _typeName: Token;
    KW_GEOMETRYCOLLECTION(): TerminalNode | undefined;
    KW_GEOMCOLLECTION(): TerminalNode | undefined;
    KW_LINESTRING(): TerminalNode | undefined;
    KW_MULTILINESTRING(): TerminalNode | undefined;
    KW_MULTIPOINT(): TerminalNode | undefined;
    KW_MULTIPOLYGON(): TerminalNode | undefined;
    KW_POINT(): TerminalNode | undefined;
    KW_POLYGON(): TerminalNode | undefined;
    KW_JSON(): TerminalNode | undefined;
    KW_GEOMETRY(): TerminalNode | undefined;
    KW_SRID(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    constructor(ctx: DataTypeContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LongVarcharDataTypeContext extends DataTypeContext {
    _typeName: Token;
    KW_LONG(): TerminalNode;
    KW_VARCHAR(): TerminalNode | undefined;
    KW_BINARY(): TerminalNode | undefined;
    charSet(): CharSetContext | undefined;
    charsetName(): CharsetNameContext | undefined;
    KW_COLLATE(): TerminalNode | undefined;
    collationName(): CollationNameContext | undefined;
    constructor(ctx: DataTypeContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LongVarbinaryDataTypeContext extends DataTypeContext {
    KW_LONG(): TerminalNode;
    KW_VARBINARY(): TerminalNode;
    constructor(ctx: DataTypeContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CollectionOptionsContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    STRING_LITERAL(): TerminalNode[];
    STRING_LITERAL(i: number): TerminalNode;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ConvertedDataTypeContext extends ParserRuleContext {
    _typeName: Token;
    KW_CHAR(): TerminalNode | undefined;
    KW_SIGNED(): TerminalNode | undefined;
    KW_UNSIGNED(): TerminalNode | undefined;
    KW_ARRAY(): TerminalNode | undefined;
    KW_BINARY(): TerminalNode | undefined;
    KW_NCHAR(): TerminalNode | undefined;
    KW_DATE(): TerminalNode | undefined;
    KW_DATETIME(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_JSON(): TerminalNode | undefined;
    KW_INT(): TerminalNode | undefined;
    KW_INTEGER(): TerminalNode | undefined;
    KW_DECIMAL(): TerminalNode | undefined;
    KW_DEC(): TerminalNode | undefined;
    lengthOneDimension(): LengthOneDimensionContext | undefined;
    charSet(): CharSetContext | undefined;
    charsetName(): CharsetNameContext | undefined;
    lengthTwoOptionalDimension(): LengthTwoOptionalDimensionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LengthOneDimensionContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    RR_BRACKET(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LengthTwoDimensionContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    COMMA(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LengthTwoOptionalDimensionContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IndexColumnNamesContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    indexColumnName(): IndexColumnNameContext[];
    indexColumnName(i: number): IndexColumnNameContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ExpressionsContext extends ParserRuleContext {
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ValuesOrValueListContext extends ParserRuleContext {
    expressionsWithDefaults(): ExpressionsWithDefaultsContext[];
    expressionsWithDefaults(i: number): ExpressionsWithDefaultsContext;
    KW_VALUES(): TerminalNode | undefined;
    KW_VALUE(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ExpressionsWithDefaultsContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    expressionOrDefault(): ExpressionOrDefaultContext[];
    expressionOrDefault(i: number): ExpressionOrDefaultContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ExpressionOrDefaultContext extends ParserRuleContext {
    expression(): ExpressionContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ConstantsContext extends ParserRuleContext {
    constant(): ConstantContext[];
    constant(i: number): ConstantContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SimpleStringsContext extends ParserRuleContext {
    STRING_LITERAL(): TerminalNode[];
    STRING_LITERAL(i: number): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UserVariablesContext extends ParserRuleContext {
    LOCAL_ID(): TerminalNode[];
    LOCAL_ID(i: number): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DefaultValueContext extends ParserRuleContext {
    KW_NULL_LITERAL(): TerminalNode | undefined;
    KW_CAST(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    KW_AS(): TerminalNode | undefined;
    convertedDataType(): ConvertedDataTypeContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    constant(): ConstantContext | undefined;
    unaryOperator(): UnaryOperatorContext | undefined;
    currentTimestamp(): CurrentTimestampContext[];
    currentTimestamp(i: number): CurrentTimestampContext;
    KW_ON(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    fullId(): FullIdContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CurrentTimestampContext extends ParserRuleContext {
    KW_NOW(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    KW_LOCALTIME(): TerminalNode | undefined;
    KW_LOCALTIMESTAMP(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IfExistsContext extends ParserRuleContext {
    KW_IF(): TerminalNode;
    KW_EXISTS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IfNotExistsContext extends ParserRuleContext {
    KW_IF(): TerminalNode;
    KW_NOT(): TerminalNode;
    KW_EXISTS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class OrReplaceContext extends ParserRuleContext {
    KW_OR(): TerminalNode;
    KW_REPLACE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FunctionCallContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: FunctionCallContext): void;
}
export declare class SpecificFunctionCallContext extends FunctionCallContext {
    specificFunction(): SpecificFunctionContext;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AggregateFunctionCallContext extends FunctionCallContext {
    aggregateWindowedFunction(): AggregateWindowedFunctionContext;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class NonAggregateFunctionCallContext extends FunctionCallContext {
    nonAggregateWindowedFunction(): NonAggregateWindowedFunctionContext;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ScalarFunctionCallContext extends FunctionCallContext {
    scalarFunctionName(): ScalarFunctionNameContext;
    LR_BRACKET(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    functionArgs(): FunctionArgsContext | undefined;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UdfFunctionCallContext extends FunctionCallContext {
    functionName(): FunctionNameContext;
    LR_BRACKET(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    functionArgs(): FunctionArgsContext | undefined;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PasswordFunctionCallContext extends FunctionCallContext {
    passwordFunctionClause(): PasswordFunctionClauseContext;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SpecificFunctionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: SpecificFunctionContext): void;
}
export declare class SimpleFunctionCallContext extends SpecificFunctionContext {
    KW_CURRENT_DATE(): TerminalNode | undefined;
    KW_CURRENT_TIME(): TerminalNode | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    KW_LOCALTIME(): TerminalNode | undefined;
    KW_UTC_TIMESTAMP(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CurrentUserContext extends SpecificFunctionContext {
    currentUserExpression(): CurrentUserExpressionContext;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DataTypeFunctionCallContext extends SpecificFunctionContext {
    _separator: Token;
    KW_CONVERT(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext;
    convertedDataType(): ConvertedDataTypeContext | undefined;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode | undefined;
    KW_USING(): TerminalNode | undefined;
    charsetName(): CharsetNameContext | undefined;
    KW_CAST(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ValuesFunctionCallContext extends SpecificFunctionContext {
    KW_VALUES(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    columnName(): ColumnNameContext;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CaseExpressionFunctionCallContext extends SpecificFunctionContext {
    _elseArg: FunctionArgContext;
    KW_CASE(): TerminalNode;
    expression(): ExpressionContext;
    KW_END(): TerminalNode;
    caseFuncAlternative(): CaseFuncAlternativeContext[];
    caseFuncAlternative(i: number): CaseFuncAlternativeContext;
    KW_ELSE(): TerminalNode | undefined;
    functionArg(): FunctionArgContext | undefined;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CaseFunctionCallContext extends SpecificFunctionContext {
    _elseArg: FunctionArgContext;
    KW_CASE(): TerminalNode;
    KW_END(): TerminalNode;
    caseFuncAlternative(): CaseFuncAlternativeContext[];
    caseFuncAlternative(i: number): CaseFuncAlternativeContext;
    KW_ELSE(): TerminalNode | undefined;
    functionArg(): FunctionArgContext | undefined;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CharFunctionCallContext extends SpecificFunctionContext {
    KW_CHAR(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    functionArgs(): FunctionArgsContext;
    RR_BRACKET(): TerminalNode;
    KW_USING(): TerminalNode | undefined;
    charsetName(): CharsetNameContext | undefined;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PositionFunctionCallContext extends SpecificFunctionContext {
    _positionString: StringLiteralContext;
    _positionExpression: ExpressionContext;
    _inString: StringLiteralContext;
    _inExpression: ExpressionContext;
    KW_POSITION(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    KW_IN(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    stringLiteral(): StringLiteralContext[];
    stringLiteral(i: number): StringLiteralContext;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SubstrFunctionCallContext extends SpecificFunctionContext {
    _sourceString: StringLiteralContext;
    _sourceExpression: ExpressionContext;
    _fromDecimal: DecimalLiteralContext;
    _fromExpression: ExpressionContext;
    _forDecimal: DecimalLiteralContext;
    _forExpression: ExpressionContext;
    LR_BRACKET(): TerminalNode;
    KW_FROM(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    KW_SUBSTR(): TerminalNode | undefined;
    KW_SUBSTRING(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    KW_FOR(): TerminalNode | undefined;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TrimFunctionCallContext extends SpecificFunctionContext {
    _positioinForm: Token;
    _sourceString: StringLiteralContext;
    _sourceExpression: ExpressionContext;
    _fromString: StringLiteralContext;
    _fromExpression: ExpressionContext;
    KW_TRIM(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    KW_FROM(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    KW_BOTH(): TerminalNode | undefined;
    KW_LEADING(): TerminalNode | undefined;
    KW_TRAILING(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext[];
    stringLiteral(i: number): StringLiteralContext;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class WeightFunctionCallContext extends SpecificFunctionContext {
    _stringFormat: Token;
    KW_WEIGHT_STRING(): TerminalNode;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    stringLiteral(): StringLiteralContext | undefined;
    expression(): ExpressionContext | undefined;
    KW_AS(): TerminalNode | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    levelsInWeightString(): LevelsInWeightStringContext | undefined;
    KW_CHAR(): TerminalNode | undefined;
    KW_BINARY(): TerminalNode | undefined;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ExtractFunctionCallContext extends SpecificFunctionContext {
    _sourceString: StringLiteralContext;
    _sourceExpression: ExpressionContext;
    KW_EXTRACT(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    intervalType(): IntervalTypeContext;
    KW_FROM(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    stringLiteral(): StringLiteralContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class GetFormatFunctionCallContext extends SpecificFunctionContext {
    _datetimeFormat: Token;
    KW_GET_FORMAT(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode;
    stringLiteral(): StringLiteralContext;
    RR_BRACKET(): TerminalNode;
    KW_DATE(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_DATETIME(): TerminalNode | undefined;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class JsonValueFunctionCallContext extends SpecificFunctionContext {
    KW_JSON_VALUE(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    COMMA(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    KW_RETURNING(): TerminalNode | undefined;
    convertedDataType(): ConvertedDataTypeContext | undefined;
    jsonOnEmpty(): JsonOnEmptyContext | undefined;
    jsonOnError(): JsonOnErrorContext | undefined;
    constructor(ctx: SpecificFunctionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CaseFuncAlternativeContext extends ParserRuleContext {
    _condition: FunctionArgContext;
    _consequent: FunctionArgContext;
    KW_WHEN(): TerminalNode;
    KW_THEN(): TerminalNode;
    functionArg(): FunctionArgContext[];
    functionArg(i: number): FunctionArgContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LevelsInWeightStringContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: LevelsInWeightStringContext): void;
}
export declare class LevelWeightListContext extends LevelsInWeightStringContext {
    KW_LEVEL(): TerminalNode;
    levelInWeightListElement(): LevelInWeightListElementContext[];
    levelInWeightListElement(i: number): LevelInWeightListElementContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: LevelsInWeightStringContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LevelWeightRangeContext extends LevelsInWeightStringContext {
    _firstLevel: DecimalLiteralContext;
    _lastLevel: DecimalLiteralContext;
    KW_LEVEL(): TerminalNode;
    MINUS(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    constructor(ctx: LevelsInWeightStringContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LevelInWeightListElementContext extends ParserRuleContext {
    _orderType: Token;
    decimalLiteral(): DecimalLiteralContext;
    KW_ASC(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    KW_REVERSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class AggregateWindowedFunctionContext extends ParserRuleContext {
    _aggregator: Token;
    _starArg: Token;
    _separator: Token;
    LR_BRACKET(): TerminalNode;
    functionArg(): FunctionArgContext | undefined;
    RR_BRACKET(): TerminalNode;
    KW_AVG(): TerminalNode | undefined;
    KW_MAX(): TerminalNode | undefined;
    KW_MIN(): TerminalNode | undefined;
    KW_SUM(): TerminalNode | undefined;
    overClause(): OverClauseContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    KW_COUNT(): TerminalNode | undefined;
    functionArgs(): FunctionArgsContext | undefined;
    STAR(): TerminalNode | undefined;
    KW_BIT_AND(): TerminalNode | undefined;
    KW_BIT_OR(): TerminalNode | undefined;
    KW_BIT_XOR(): TerminalNode | undefined;
    KW_STD(): TerminalNode | undefined;
    KW_STDDEV(): TerminalNode | undefined;
    KW_STDDEV_POP(): TerminalNode | undefined;
    KW_STDDEV_SAMP(): TerminalNode | undefined;
    KW_VAR_POP(): TerminalNode | undefined;
    KW_VAR_SAMP(): TerminalNode | undefined;
    KW_VARIANCE(): TerminalNode | undefined;
    KW_GROUP_CONCAT(): TerminalNode | undefined;
    KW_ORDER(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    orderByExpression(): OrderByExpressionContext[];
    orderByExpression(i: number): OrderByExpressionContext;
    KW_SEPARATOR(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class NonAggregateWindowedFunctionContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext | undefined;
    RR_BRACKET(): TerminalNode;
    overClause(): OverClauseContext;
    KW_LAG(): TerminalNode | undefined;
    KW_LEAD(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    KW_FIRST_VALUE(): TerminalNode | undefined;
    KW_LAST_VALUE(): TerminalNode | undefined;
    KW_CUME_DIST(): TerminalNode | undefined;
    KW_DENSE_RANK(): TerminalNode | undefined;
    KW_PERCENT_RANK(): TerminalNode | undefined;
    KW_RANK(): TerminalNode | undefined;
    KW_ROW_NUMBER(): TerminalNode | undefined;
    KW_NTH_VALUE(): TerminalNode | undefined;
    KW_NTILE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class OverClauseContext extends ParserRuleContext {
    KW_OVER(): TerminalNode;
    LR_BRACKET(): TerminalNode | undefined;
    windowSpec(): WindowSpecContext | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    windowName(): WindowNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class WindowSpecContext extends ParserRuleContext {
    windowName(): WindowNameContext | undefined;
    partitionClause(): PartitionClauseContext | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    frameClause(): FrameClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class WindowNameContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FrameClauseContext extends ParserRuleContext {
    frameUnits(): FrameUnitsContext;
    frameExtent(): FrameExtentContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FrameUnitsContext extends ParserRuleContext {
    KW_ROWS(): TerminalNode | undefined;
    KW_RANGE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FrameExtentContext extends ParserRuleContext {
    frameRange(): FrameRangeContext | undefined;
    frameBetween(): FrameBetweenContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FrameBetweenContext extends ParserRuleContext {
    KW_BETWEEN(): TerminalNode;
    frameRange(): FrameRangeContext[];
    frameRange(i: number): FrameRangeContext;
    KW_AND(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FrameRangeContext extends ParserRuleContext {
    KW_CURRENT(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_UNBOUNDED(): TerminalNode | undefined;
    KW_PRECEDING(): TerminalNode | undefined;
    KW_FOLLOWING(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PartitionClauseContext extends ParserRuleContext {
    KW_PARTITION(): TerminalNode;
    KW_BY(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ScalarFunctionNameContext extends ParserRuleContext {
    functionNameBase(): FunctionNameBaseContext | undefined;
    KW_ASCII(): TerminalNode | undefined;
    KW_CURDATE(): TerminalNode | undefined;
    KW_CURRENT_DATE(): TerminalNode | undefined;
    KW_CURRENT_TIME(): TerminalNode | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    KW_CURTIME(): TerminalNode | undefined;
    KW_DATE_ADD(): TerminalNode | undefined;
    KW_DATE_SUB(): TerminalNode | undefined;
    KW_IF(): TerminalNode | undefined;
    KW_INSERT(): TerminalNode | undefined;
    KW_LOCALTIME(): TerminalNode | undefined;
    KW_LOCALTIMESTAMP(): TerminalNode | undefined;
    KW_MID(): TerminalNode | undefined;
    KW_NOW(): TerminalNode | undefined;
    KW_REPEAT(): TerminalNode | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    KW_SUBSTR(): TerminalNode | undefined;
    KW_SUBSTRING(): TerminalNode | undefined;
    KW_SYSDATE(): TerminalNode | undefined;
    KW_TRIM(): TerminalNode | undefined;
    KW_UTC_DATE(): TerminalNode | undefined;
    KW_UTC_TIME(): TerminalNode | undefined;
    KW_UTC_TIMESTAMP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PasswordFunctionClauseContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    functionArg(): FunctionArgContext;
    RR_BRACKET(): TerminalNode;
    KW_PASSWORD(): TerminalNode | undefined;
    KW_OLD_PASSWORD(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FunctionArgsContext extends ParserRuleContext {
    functionArg(): FunctionArgContext[];
    functionArg(i: number): FunctionArgContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FunctionArgContext extends ParserRuleContext {
    constant(): ConstantContext | undefined;
    columnName(): ColumnNameContext | undefined;
    functionCall(): FunctionCallContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ExpressionContext): void;
}
export declare class NotExpressionContext extends ExpressionContext {
    _notOperator: Token;
    expression(): ExpressionContext;
    KW_NOT(): TerminalNode | undefined;
    EXCLAMATION_SYMBOL(): TerminalNode | undefined;
    constructor(ctx: ExpressionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LogicalExpressionContext extends ExpressionContext {
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    logicalOperator(): LogicalOperatorContext;
    constructor(ctx: ExpressionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IsExpressionContext extends ExpressionContext {
    _testValue: Token;
    predicate(): PredicateContext;
    KW_IS(): TerminalNode;
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    KW_UNKNOWN(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: ExpressionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PredicateExpressionContext extends ExpressionContext {
    predicate(): PredicateContext;
    constructor(ctx: ExpressionContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PredicateContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PredicateContext): void;
}
export declare class InPredicateContext extends PredicateContext {
    predicate(): PredicateContext;
    KW_IN(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    selectStatement(): SelectStatementContext | undefined;
    expressions(): ExpressionsContext | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IsNullPredicateContext extends PredicateContext {
    predicate(): PredicateContext;
    KW_IS(): TerminalNode;
    nullNotnull(): NullNotnullContext;
    constructor(ctx: PredicateContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class BinaryComparisonPredicateContext extends PredicateContext {
    _left: PredicateContext;
    _right: PredicateContext;
    comparisonOperator(): ComparisonOperatorContext;
    predicate(): PredicateContext[];
    predicate(i: number): PredicateContext;
    constructor(ctx: PredicateContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SubqueryComparisonPredicateContext extends PredicateContext {
    _quantifier: Token;
    predicate(): PredicateContext;
    comparisonOperator(): ComparisonOperatorContext;
    LR_BRACKET(): TerminalNode;
    selectStatement(): SelectStatementContext;
    RR_BRACKET(): TerminalNode;
    KW_ALL(): TerminalNode | undefined;
    KW_ANY(): TerminalNode | undefined;
    KW_SOME(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class BetweenPredicateContext extends PredicateContext {
    predicate(): PredicateContext[];
    predicate(i: number): PredicateContext;
    KW_BETWEEN(): TerminalNode;
    KW_AND(): TerminalNode;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SoundsLikePredicateContext extends PredicateContext {
    predicate(): PredicateContext[];
    predicate(i: number): PredicateContext;
    KW_SOUNDS(): TerminalNode;
    KW_LIKE(): TerminalNode;
    constructor(ctx: PredicateContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LikePredicateContext extends PredicateContext {
    predicate(): PredicateContext[];
    predicate(i: number): PredicateContext;
    KW_LIKE(): TerminalNode;
    KW_NOT(): TerminalNode | undefined;
    KW_ESCAPE(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class RegexpPredicateContext extends PredicateContext {
    _regex: Token;
    predicate(): PredicateContext[];
    predicate(i: number): PredicateContext;
    KW_REGEXP(): TerminalNode | undefined;
    KW_RLIKE(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class JsonMemberOfPredicateContext extends PredicateContext {
    predicate(): PredicateContext[];
    predicate(i: number): PredicateContext;
    KW_MEMBER(): TerminalNode;
    KW_OF(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: PredicateContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ExpressionAtomPredicateContext extends PredicateContext {
    expressionAtom(): ExpressionAtomContext;
    constructor(ctx: PredicateContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ExpressionAtomContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ExpressionAtomContext): void;
}
export declare class ConstantExpressionAtomContext extends ExpressionAtomContext {
    constant(): ConstantContext;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ColumnNameExpressionAtomContext extends ExpressionAtomContext {
    columnName(): ColumnNameContext;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FunctionCallExpressionAtomContext extends ExpressionAtomContext {
    functionCall(): FunctionCallContext;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CollateExpressionAtomContext extends ExpressionAtomContext {
    expressionAtom(): ExpressionAtomContext;
    KW_COLLATE(): TerminalNode;
    collationName(): CollationNameContext;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class MysqlVariableExpressionAtomContext extends ExpressionAtomContext {
    mysqlVariable(): MysqlVariableContext;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UnaryExpressionAtomContext extends ExpressionAtomContext {
    unaryOperator(): UnaryOperatorContext;
    expressionAtom(): ExpressionAtomContext;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class BinaryExpressionAtomContext extends ExpressionAtomContext {
    KW_BINARY(): TerminalNode;
    expressionAtom(): ExpressionAtomContext;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class VariableAssignExpressionAtomContext extends ExpressionAtomContext {
    LOCAL_ID(): TerminalNode;
    VAR_ASSIGN(): TerminalNode;
    expressionAtom(): ExpressionAtomContext;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class NestedExpressionAtomContext extends ExpressionAtomContext {
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class NestedRowExpressionAtomContext extends ExpressionAtomContext {
    KW_ROW(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ExistsExpressionAtomContext extends ExpressionAtomContext {
    KW_EXISTS(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    selectStatement(): SelectStatementContext;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class SubqueryExpressionAtomContext extends ExpressionAtomContext {
    LR_BRACKET(): TerminalNode;
    selectStatement(): SelectStatementContext;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IntervalExpressionAtomContext extends ExpressionAtomContext {
    KW_INTERVAL(): TerminalNode;
    expression(): ExpressionContext;
    intervalType(): IntervalTypeContext;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class BitExpressionAtomContext extends ExpressionAtomContext {
    _left: ExpressionAtomContext;
    _right: ExpressionAtomContext;
    bitOperator(): BitOperatorContext;
    expressionAtom(): ExpressionAtomContext[];
    expressionAtom(i: number): ExpressionAtomContext;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class MathExpressionAtomContext extends ExpressionAtomContext {
    _left: ExpressionAtomContext;
    _right: ExpressionAtomContext;
    mathOperator(): MathOperatorContext;
    expressionAtom(): ExpressionAtomContext[];
    expressionAtom(i: number): ExpressionAtomContext;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class JsonExpressionAtomContext extends ExpressionAtomContext {
    _left: ExpressionAtomContext;
    _right: ExpressionAtomContext;
    jsonOperator(): JsonOperatorContext;
    expressionAtom(): ExpressionAtomContext[];
    expressionAtom(i: number): ExpressionAtomContext;
    constructor(ctx: ExpressionAtomContext);
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class UnaryOperatorContext extends ParserRuleContext {
    EXCLAMATION_SYMBOL(): TerminalNode | undefined;
    BIT_NOT_OP(): TerminalNode | undefined;
    PLUS(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ComparisonOperatorContext extends ParserRuleContext {
    comparisonBase(): ComparisonBaseContext | undefined;
    LESS_SYMBOL(): TerminalNode | undefined;
    GREATER_SYMBOL(): TerminalNode | undefined;
    EXCLAMATION_SYMBOL(): TerminalNode | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class ComparisonBaseContext extends ParserRuleContext {
    EQUAL_SYMBOL(): TerminalNode | undefined;
    GREATER_SYMBOL(): TerminalNode | undefined;
    LESS_SYMBOL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class LogicalOperatorContext extends ParserRuleContext {
    KW_AND(): TerminalNode | undefined;
    BIT_AND_OP(): TerminalNode[];
    BIT_AND_OP(i: number): TerminalNode;
    KW_XOR(): TerminalNode | undefined;
    KW_OR(): TerminalNode | undefined;
    BIT_OR_OP(): TerminalNode[];
    BIT_OR_OP(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class BitOperatorContext extends ParserRuleContext {
    LESS_SYMBOL(): TerminalNode[];
    LESS_SYMBOL(i: number): TerminalNode;
    GREATER_SYMBOL(): TerminalNode[];
    GREATER_SYMBOL(i: number): TerminalNode;
    BIT_AND_OP(): TerminalNode | undefined;
    BIT_XOR_OP(): TerminalNode | undefined;
    BIT_OR_OP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class MathOperatorContext extends ParserRuleContext {
    STAR(): TerminalNode | undefined;
    DIVIDE(): TerminalNode | undefined;
    MODULE(): TerminalNode | undefined;
    DIV(): TerminalNode | undefined;
    MOD(): TerminalNode | undefined;
    PLUS(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class JsonOperatorContext extends ParserRuleContext {
    MINUS(): TerminalNode;
    GREATER_SYMBOL(): TerminalNode[];
    GREATER_SYMBOL(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class CharsetNameBaseContext extends ParserRuleContext {
    KW_ARMSCII8(): TerminalNode | undefined;
    KW_ASCII(): TerminalNode | undefined;
    KW_BIG5(): TerminalNode | undefined;
    KW_BINARY(): TerminalNode | undefined;
    KW_CP1250(): TerminalNode | undefined;
    KW_CP1251(): TerminalNode | undefined;
    KW_CP1256(): TerminalNode | undefined;
    KW_CP1257(): TerminalNode | undefined;
    KW_CP850(): TerminalNode | undefined;
    KW_CP852(): TerminalNode | undefined;
    KW_CP866(): TerminalNode | undefined;
    KW_CP932(): TerminalNode | undefined;
    KW_DEC8(): TerminalNode | undefined;
    KW_EUCJPMS(): TerminalNode | undefined;
    KW_EUCKR(): TerminalNode | undefined;
    KW_GB18030(): TerminalNode | undefined;
    KW_GB2312(): TerminalNode | undefined;
    KW_GBK(): TerminalNode | undefined;
    KW_GEOSTD8(): TerminalNode | undefined;
    KW_GREEK(): TerminalNode | undefined;
    KW_HEBREW(): TerminalNode | undefined;
    KW_HP8(): TerminalNode | undefined;
    KW_KEYBCS2(): TerminalNode | undefined;
    KW_KOI8R(): TerminalNode | undefined;
    KW_KOI8U(): TerminalNode | undefined;
    KW_LATIN1(): TerminalNode | undefined;
    KW_LATIN2(): TerminalNode | undefined;
    KW_LATIN5(): TerminalNode | undefined;
    KW_LATIN7(): TerminalNode | undefined;
    KW_MACCE(): TerminalNode | undefined;
    KW_MACROMAN(): TerminalNode | undefined;
    KW_SJIS(): TerminalNode | undefined;
    KW_SWE7(): TerminalNode | undefined;
    KW_TIS620(): TerminalNode | undefined;
    KW_UCS2(): TerminalNode | undefined;
    KW_UJIS(): TerminalNode | undefined;
    KW_UTF16(): TerminalNode | undefined;
    KW_UTF16LE(): TerminalNode | undefined;
    KW_UTF32(): TerminalNode | undefined;
    KW_UTF8(): TerminalNode | undefined;
    KW_UTF8MB3(): TerminalNode | undefined;
    KW_UTF8MB4(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class TransactionLevelBaseContext extends ParserRuleContext {
    KW_REPEATABLE(): TerminalNode | undefined;
    KW_COMMITTED(): TerminalNode | undefined;
    KW_UNCOMMITTED(): TerminalNode | undefined;
    KW_SERIALIZABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class PrivilegesBaseContext extends ParserRuleContext {
    KW_TABLES(): TerminalNode | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    KW_EXECUTE(): TerminalNode | undefined;
    KW_FILE(): TerminalNode | undefined;
    KW_PROCESS(): TerminalNode | undefined;
    KW_RELOAD(): TerminalNode | undefined;
    KW_SHUTDOWN(): TerminalNode | undefined;
    KW_SUPER(): TerminalNode | undefined;
    KW_PRIVILEGES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class IntervalTypeBaseContext extends ParserRuleContext {
    KW_QUARTER(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    KW_WEEK(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    KW_MICROSECOND(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class DataTypeBaseContext extends ParserRuleContext {
    KW_DATE(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_TIMESTAMP(): TerminalNode | undefined;
    KW_DATETIME(): TerminalNode | undefined;
    KW_YEAR(): TerminalNode | undefined;
    KW_ENUM(): TerminalNode | undefined;
    KW_TEXT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class KeywordsCanBeIdContext extends ParserRuleContext {
    KW_ACCOUNT(): TerminalNode | undefined;
    KW_ACTION(): TerminalNode | undefined;
    KW_ADMIN(): TerminalNode | undefined;
    KW_AFTER(): TerminalNode | undefined;
    KW_AGGREGATE(): TerminalNode | undefined;
    KW_ALGORITHM(): TerminalNode | undefined;
    KW_ANY(): TerminalNode | undefined;
    KW_ARRAY(): TerminalNode | undefined;
    KW_AT(): TerminalNode | undefined;
    KW_AUDIT_ADMIN(): TerminalNode | undefined;
    KW_AUDIT_ABORT_EXEMPT(): TerminalNode | undefined;
    KW_AUTHORS(): TerminalNode | undefined;
    KW_AUTOCOMMIT(): TerminalNode | undefined;
    KW_AUTOEXTEND_SIZE(): TerminalNode | undefined;
    KW_AUTO_INCREMENT(): TerminalNode | undefined;
    KW_AUTHENTICATION_POLICY_ADMIN(): TerminalNode | undefined;
    KW_AVG(): TerminalNode | undefined;
    KW_AVG_ROW_LENGTH(): TerminalNode | undefined;
    KW_ATTRIBUTE(): TerminalNode | undefined;
    KW_BACKUP_ADMIN(): TerminalNode | undefined;
    KW_BEGIN(): TerminalNode | undefined;
    KW_BINLOG(): TerminalNode | undefined;
    KW_BINLOG_ADMIN(): TerminalNode | undefined;
    KW_BINLOG_ENCRYPTION_ADMIN(): TerminalNode | undefined;
    KW_BIT(): TerminalNode | undefined;
    KW_BIT_AND(): TerminalNode | undefined;
    KW_BIT_OR(): TerminalNode | undefined;
    KW_BIT_XOR(): TerminalNode | undefined;
    KW_BLOCK(): TerminalNode | undefined;
    KW_BOOL(): TerminalNode | undefined;
    KW_BOOLEAN(): TerminalNode | undefined;
    KW_BTREE(): TerminalNode | undefined;
    KW_BUCKETS(): TerminalNode | undefined;
    KW_CACHE(): TerminalNode | undefined;
    KW_CASCADED(): TerminalNode | undefined;
    KW_CHAIN(): TerminalNode | undefined;
    KW_CHANGED(): TerminalNode | undefined;
    KW_CHANNEL(): TerminalNode | undefined;
    KW_CHECKSUM(): TerminalNode | undefined;
    KW_PAGE_CHECKSUM(): TerminalNode | undefined;
    KW_CATALOG_NAME(): TerminalNode | undefined;
    KW_CIPHER(): TerminalNode | undefined;
    KW_CLASS_ORIGIN(): TerminalNode | undefined;
    KW_CLIENT(): TerminalNode | undefined;
    KW_CLONE_ADMIN(): TerminalNode | undefined;
    KW_CLOSE(): TerminalNode | undefined;
    KW_COALESCE(): TerminalNode | undefined;
    KW_CODE(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    KW_COLUMN_FORMAT(): TerminalNode | undefined;
    KW_COLUMN_NAME(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    KW_COMMIT(): TerminalNode | undefined;
    KW_COMPACT(): TerminalNode | undefined;
    KW_COMPLETION(): TerminalNode | undefined;
    KW_COMPRESSED(): TerminalNode | undefined;
    KW_COMPRESSION(): TerminalNode | undefined;
    KW_CONCURRENT(): TerminalNode | undefined;
    KW_CONDITION(): TerminalNode | undefined;
    KW_CONNECT(): TerminalNode | undefined;
    KW_CONNECTION(): TerminalNode | undefined;
    KW_CONNECTION_ADMIN(): TerminalNode | undefined;
    KW_CONSISTENT(): TerminalNode | undefined;
    KW_CONSTRAINT_CATALOG(): TerminalNode | undefined;
    KW_CONSTRAINT_NAME(): TerminalNode | undefined;
    KW_CONSTRAINT_SCHEMA(): TerminalNode | undefined;
    KW_CONTAINS(): TerminalNode | undefined;
    KW_CONTEXT(): TerminalNode | undefined;
    KW_CONTRIBUTORS(): TerminalNode | undefined;
    KW_COPY(): TerminalNode | undefined;
    KW_COUNT(): TerminalNode | undefined;
    KW_CPU(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_CURRENT_USER(): TerminalNode | undefined;
    KW_CURSOR_NAME(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_DATAFILE(): TerminalNode | undefined;
    KW_DEALLOCATE(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_DEFAULT_AUTH(): TerminalNode | undefined;
    KW_DEFINER(): TerminalNode | undefined;
    KW_DELAY_KEY_WRITE(): TerminalNode | undefined;
    KW_DES_KEY_FILE(): TerminalNode | undefined;
    KW_DIAGNOSTICS(): TerminalNode | undefined;
    KW_DIRECTORY(): TerminalNode | undefined;
    KW_DISABLE(): TerminalNode | undefined;
    KW_DISCARD(): TerminalNode | undefined;
    KW_DISK(): TerminalNode | undefined;
    KW_DO(): TerminalNode | undefined;
    KW_DUMPFILE(): TerminalNode | undefined;
    KW_DUPLICATE(): TerminalNode | undefined;
    KW_DYNAMIC(): TerminalNode | undefined;
    KW_EMPTY(): TerminalNode | undefined;
    KW_ENABLE(): TerminalNode | undefined;
    KW_ENCRYPTION(): TerminalNode | undefined;
    KW_ENCRYPTION_KEY_ADMIN(): TerminalNode | undefined;
    KW_END(): TerminalNode | undefined;
    KW_ENDS(): TerminalNode | undefined;
    KW_ENGINE(): TerminalNode | undefined;
    KW_ENGINE_ATTRIBUTE(): TerminalNode | undefined;
    KW_ENGINES(): TerminalNode | undefined;
    KW_ENFORCED(): TerminalNode | undefined;
    KW_ERROR(): TerminalNode | undefined;
    KW_ERRORS(): TerminalNode | undefined;
    KW_ESCAPE(): TerminalNode | undefined;
    KW_EVENT(): TerminalNode | undefined;
    KW_EVENTS(): TerminalNode | undefined;
    KW_EVERY(): TerminalNode | undefined;
    KW_EXCEPT(): TerminalNode | undefined;
    KW_EXCHANGE(): TerminalNode | undefined;
    KW_EXCLUSIVE(): TerminalNode | undefined;
    KW_EXPIRE(): TerminalNode | undefined;
    KW_EXPORT(): TerminalNode | undefined;
    KW_EXTENDED(): TerminalNode | undefined;
    KW_EXTENT_SIZE(): TerminalNode | undefined;
    KW_FAILED_LOGIN_ATTEMPTS(): TerminalNode | undefined;
    KW_FAST(): TerminalNode | undefined;
    KW_FAULTS(): TerminalNode | undefined;
    KW_FIELDS(): TerminalNode | undefined;
    KW_FILE_BLOCK_SIZE(): TerminalNode | undefined;
    KW_FILTER(): TerminalNode | undefined;
    KW_FIREWALL_ADMIN(): TerminalNode | undefined;
    KW_FIREWALL_EXEMPT(): TerminalNode | undefined;
    KW_FIREWALL_USER(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_FIXED(): TerminalNode | undefined;
    KW_FLUSH(): TerminalNode | undefined;
    KW_FOLLOWS(): TerminalNode | undefined;
    KW_FOUND(): TerminalNode | undefined;
    KW_FULL(): TerminalNode | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    KW_GENERAL(): TerminalNode | undefined;
    KW_GLOBAL(): TerminalNode | undefined;
    KW_GRANTS(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_GROUP_CONCAT(): TerminalNode | undefined;
    KW_GROUP_REPLICATION(): TerminalNode | undefined;
    KW_GROUP_REPLICATION_ADMIN(): TerminalNode | undefined;
    KW_HANDLER(): TerminalNode | undefined;
    KW_HASH(): TerminalNode | undefined;
    KW_HELP(): TerminalNode | undefined;
    KW_HISTORY(): TerminalNode | undefined;
    KW_HOST(): TerminalNode | undefined;
    KW_HOSTS(): TerminalNode | undefined;
    KW_IDENTIFIED(): TerminalNode | undefined;
    KW_IGNORE_SERVER_IDS(): TerminalNode | undefined;
    KW_IMPORT(): TerminalNode | undefined;
    KW_INDEXES(): TerminalNode | undefined;
    KW_INITIAL_SIZE(): TerminalNode | undefined;
    KW_INNODB_REDO_LOG_ARCHIVE(): TerminalNode | undefined;
    KW_INPLACE(): TerminalNode | undefined;
    KW_INSERT_METHOD(): TerminalNode | undefined;
    KW_INSTALL(): TerminalNode | undefined;
    KW_INSTANCE(): TerminalNode | undefined;
    KW_INSTANT(): TerminalNode | undefined;
    KW_INVOKE(): TerminalNode | undefined;
    KW_INVOKER(): TerminalNode | undefined;
    KW_IO(): TerminalNode | undefined;
    KW_IO_THREAD(): TerminalNode | undefined;
    KW_IPC(): TerminalNode | undefined;
    KW_ISOLATION(): TerminalNode | undefined;
    KW_ISSUER(): TerminalNode | undefined;
    KW_JSON(): TerminalNode | undefined;
    KW_KEY_BLOCK_SIZE(): TerminalNode | undefined;
    KW_LAMBDA(): TerminalNode | undefined;
    KW_LANGUAGE(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    KW_LATERAL(): TerminalNode | undefined;
    KW_LEAVES(): TerminalNode | undefined;
    KW_LESS(): TerminalNode | undefined;
    KW_LEVEL(): TerminalNode | undefined;
    KW_LIST(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    KW_LOGFILE(): TerminalNode | undefined;
    KW_LOGS(): TerminalNode | undefined;
    KW_MASTER(): TerminalNode | undefined;
    KW_MASTER_AUTO_POSITION(): TerminalNode | undefined;
    KW_MASTER_CONNECT_RETRY(): TerminalNode | undefined;
    KW_MASTER_DELAY(): TerminalNode | undefined;
    KW_MASTER_HEARTBEAT_PERIOD(): TerminalNode | undefined;
    KW_MASTER_HOST(): TerminalNode | undefined;
    KW_MASTER_LOG_FILE(): TerminalNode | undefined;
    KW_MASTER_LOG_POS(): TerminalNode | undefined;
    KW_MASTER_PASSWORD(): TerminalNode | undefined;
    KW_MASTER_PORT(): TerminalNode | undefined;
    KW_MASTER_RETRY_COUNT(): TerminalNode | undefined;
    KW_MASTER_SSL(): TerminalNode | undefined;
    KW_MASTER_SSL_CA(): TerminalNode | undefined;
    KW_MASTER_SSL_CAPATH(): TerminalNode | undefined;
    KW_MASTER_SSL_CERT(): TerminalNode | undefined;
    KW_MASTER_SSL_CIPHER(): TerminalNode | undefined;
    KW_MASTER_SSL_CRL(): TerminalNode | undefined;
    KW_MASTER_SSL_CRLPATH(): TerminalNode | undefined;
    KW_MASTER_SSL_KEY(): TerminalNode | undefined;
    KW_MASTER_TLS_VERSION(): TerminalNode | undefined;
    KW_MASTER_USER(): TerminalNode | undefined;
    KW_MAX_CONNECTIONS_PER_HOUR(): TerminalNode | undefined;
    KW_MAX_QUERIES_PER_HOUR(): TerminalNode | undefined;
    KW_MAX(): TerminalNode | undefined;
    KW_MAX_ROWS(): TerminalNode | undefined;
    KW_MAX_SIZE(): TerminalNode | undefined;
    KW_MAX_UPDATES_PER_HOUR(): TerminalNode | undefined;
    KW_MAX_USER_CONNECTIONS(): TerminalNode | undefined;
    KW_MEDIUM(): TerminalNode | undefined;
    KW_MEMBER(): TerminalNode | undefined;
    KW_MEMORY(): TerminalNode | undefined;
    KW_MERGE(): TerminalNode | undefined;
    KW_MESSAGE_TEXT(): TerminalNode | undefined;
    KW_MID(): TerminalNode | undefined;
    KW_MIGRATE(): TerminalNode | undefined;
    KW_MIN(): TerminalNode | undefined;
    KW_MIN_ROWS(): TerminalNode | undefined;
    KW_MODE(): TerminalNode | undefined;
    KW_MODIFY(): TerminalNode | undefined;
    KW_MUTEX(): TerminalNode | undefined;
    KW_MYSQL(): TerminalNode | undefined;
    KW_MYSQL_ERRNO(): TerminalNode | undefined;
    KW_NAME(): TerminalNode | undefined;
    KW_NAMES(): TerminalNode | undefined;
    KW_NCHAR(): TerminalNode | undefined;
    KW_NDB_STORED_USER(): TerminalNode | undefined;
    KW_NESTED(): TerminalNode | undefined;
    KW_NEVER(): TerminalNode | undefined;
    KW_NEXT(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_NODEGROUP(): TerminalNode | undefined;
    KW_NONE(): TerminalNode | undefined;
    KW_NOWAIT(): TerminalNode | undefined;
    KW_NUMBER(): TerminalNode | undefined;
    KW_ODBC(): TerminalNode | undefined;
    KW_OFFLINE(): TerminalNode | undefined;
    KW_OFFSET(): TerminalNode | undefined;
    KW_OF(): TerminalNode | undefined;
    KW_OLD_PASSWORD(): TerminalNode | undefined;
    KW_ONE(): TerminalNode | undefined;
    KW_ONLINE(): TerminalNode | undefined;
    KW_ONLY(): TerminalNode | undefined;
    KW_OPEN(): TerminalNode | undefined;
    KW_OPTIMIZER_COSTS(): TerminalNode | undefined;
    KW_OPTIONAL(): TerminalNode | undefined;
    KW_OPTIONS(): TerminalNode | undefined;
    KW_ORDER(): TerminalNode | undefined;
    KW_ORDINALITY(): TerminalNode | undefined;
    KW_OWNER(): TerminalNode | undefined;
    KW_PACK_KEYS(): TerminalNode | undefined;
    KW_PAGE(): TerminalNode | undefined;
    KW_PARSER(): TerminalNode | undefined;
    KW_PARTIAL(): TerminalNode | undefined;
    KW_PARTITIONING(): TerminalNode | undefined;
    KW_PARTITIONS(): TerminalNode | undefined;
    KW_PASSWORD(): TerminalNode | undefined;
    KW_PASSWORDLESS_USER_ADMIN(): TerminalNode | undefined;
    KW_PASSWORD_LOCK_TIME(): TerminalNode | undefined;
    KW_PATH(): TerminalNode | undefined;
    KW_PERSIST_RO_VARIABLES_ADMIN(): TerminalNode | undefined;
    KW_PHASE(): TerminalNode | undefined;
    KW_PLUGINS(): TerminalNode | undefined;
    KW_PLUGIN_DIR(): TerminalNode | undefined;
    KW_PLUGIN(): TerminalNode | undefined;
    KW_PORT(): TerminalNode | undefined;
    KW_PRECEDES(): TerminalNode | undefined;
    KW_PREPARE(): TerminalNode | undefined;
    KW_PRESERVE(): TerminalNode | undefined;
    KW_PREV(): TerminalNode | undefined;
    KW_PRIMARY(): TerminalNode | undefined;
    KW_PROCESSLIST(): TerminalNode | undefined;
    KW_PROFILE(): TerminalNode | undefined;
    KW_PROFILES(): TerminalNode | undefined;
    KW_PROXY(): TerminalNode | undefined;
    KW_QUERY(): TerminalNode | undefined;
    KW_QUICK(): TerminalNode | undefined;
    KW_REBUILD(): TerminalNode | undefined;
    KW_RECOVER(): TerminalNode | undefined;
    KW_RECURSIVE(): TerminalNode | undefined;
    KW_REDO_BUFFER_SIZE(): TerminalNode | undefined;
    KW_REDUNDANT(): TerminalNode | undefined;
    KW_RELAY(): TerminalNode | undefined;
    KW_RELAYLOG(): TerminalNode | undefined;
    KW_RELAY_LOG_FILE(): TerminalNode | undefined;
    KW_RELAY_LOG_POS(): TerminalNode | undefined;
    KW_REMOVE(): TerminalNode | undefined;
    KW_REORGANIZE(): TerminalNode | undefined;
    KW_REPAIR(): TerminalNode | undefined;
    KW_REPLICATE_DO_DB(): TerminalNode | undefined;
    KW_REPLICATE_DO_TABLE(): TerminalNode | undefined;
    KW_REPLICATE_IGNORE_DB(): TerminalNode | undefined;
    KW_REPLICATE_IGNORE_TABLE(): TerminalNode | undefined;
    KW_REPLICATE_REWRITE_DB(): TerminalNode | undefined;
    KW_REPLICATE_WILD_DO_TABLE(): TerminalNode | undefined;
    KW_REPLICATE_WILD_IGNORE_TABLE(): TerminalNode | undefined;
    KW_REPLICATION(): TerminalNode | undefined;
    KW_REPLICATION_APPLIER(): TerminalNode | undefined;
    KW_REPLICATION_SLAVE_ADMIN(): TerminalNode | undefined;
    KW_RESET(): TerminalNode | undefined;
    KW_RESOURCE_GROUP_ADMIN(): TerminalNode | undefined;
    KW_RESOURCE_GROUP_USER(): TerminalNode | undefined;
    KW_RESUME(): TerminalNode | undefined;
    KW_RETURNED_SQLSTATE(): TerminalNode | undefined;
    KW_RETURNS(): TerminalNode | undefined;
    KW_REUSE(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_ROLE_ADMIN(): TerminalNode | undefined;
    KW_ROLLBACK(): TerminalNode | undefined;
    KW_ROLLUP(): TerminalNode | undefined;
    KW_ROTATE(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_ROWS(): TerminalNode | undefined;
    KW_ROW_FORMAT(): TerminalNode | undefined;
    KW_S3(): TerminalNode | undefined;
    KW_SAVEPOINT(): TerminalNode | undefined;
    KW_SCHEDULE(): TerminalNode | undefined;
    KW_SCHEMA_NAME(): TerminalNode | undefined;
    KW_SECURITY(): TerminalNode | undefined;
    KW_SECONDARY_ENGINE_ATTRIBUTE(): TerminalNode | undefined;
    KW_SERIAL(): TerminalNode | undefined;
    KW_SERVER(): TerminalNode | undefined;
    KW_SESSION(): TerminalNode | undefined;
    KW_SESSION_VARIABLES_ADMIN(): TerminalNode | undefined;
    KW_SET_USER_ID(): TerminalNode | undefined;
    KW_SHARE(): TerminalNode | undefined;
    KW_SHARED(): TerminalNode | undefined;
    KW_SHOW_ROUTINE(): TerminalNode | undefined;
    KW_SIGNED(): TerminalNode | undefined;
    KW_SIMPLE(): TerminalNode | undefined;
    KW_SLAVE(): TerminalNode | undefined;
    KW_SLOW(): TerminalNode | undefined;
    KW_SKIP_QUERY_REWRITE(): TerminalNode | undefined;
    KW_SNAPSHOT(): TerminalNode | undefined;
    KW_SOCKET(): TerminalNode | undefined;
    KW_SOME(): TerminalNode | undefined;
    KW_SONAME(): TerminalNode | undefined;
    KW_SOUNDS(): TerminalNode | undefined;
    KW_SOURCE(): TerminalNode | undefined;
    KW_SQL_AFTER_GTIDS(): TerminalNode | undefined;
    KW_SQL_AFTER_MTS_GAPS(): TerminalNode | undefined;
    KW_SQL_BEFORE_GTIDS(): TerminalNode | undefined;
    KW_SQL_BUFFER_RESULT(): TerminalNode | undefined;
    KW_SQL_CACHE(): TerminalNode | undefined;
    KW_SQL_NO_CACHE(): TerminalNode | undefined;
    KW_SQL_THREAD(): TerminalNode | undefined;
    KW_STACKED(): TerminalNode | undefined;
    KW_START(): TerminalNode | undefined;
    KW_STARTS(): TerminalNode | undefined;
    KW_STATS_AUTO_RECALC(): TerminalNode | undefined;
    KW_STATS_PERSISTENT(): TerminalNode | undefined;
    KW_STATS_SAMPLE_PAGES(): TerminalNode | undefined;
    KW_STATUS(): TerminalNode | undefined;
    KW_STD(): TerminalNode | undefined;
    KW_STDDEV(): TerminalNode | undefined;
    KW_STDDEV_POP(): TerminalNode | undefined;
    KW_STDDEV_SAMP(): TerminalNode | undefined;
    KW_STOP(): TerminalNode | undefined;
    KW_STORAGE(): TerminalNode | undefined;
    KW_STRING(): TerminalNode | undefined;
    KW_SUBCLASS_ORIGIN(): TerminalNode | undefined;
    KW_SUBJECT(): TerminalNode | undefined;
    KW_SUBPARTITION(): TerminalNode | undefined;
    KW_SUBPARTITIONS(): TerminalNode | undefined;
    KW_SUM(): TerminalNode | undefined;
    KW_SUSPEND(): TerminalNode | undefined;
    KW_SWAPS(): TerminalNode | undefined;
    KW_SWITCHES(): TerminalNode | undefined;
    KW_SYSTEM_VARIABLES_ADMIN(): TerminalNode | undefined;
    KW_TABLE_NAME(): TerminalNode | undefined;
    KW_TABLESPACE(): TerminalNode | undefined;
    KW_TABLE_ENCRYPTION_ADMIN(): TerminalNode | undefined;
    KW_TABLE_TYPE(): TerminalNode | undefined;
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_TEMPTABLE(): TerminalNode | undefined;
    KW_THAN(): TerminalNode | undefined;
    KW_TP_CONNECTION_ADMIN(): TerminalNode | undefined;
    KW_TRADITIONAL(): TerminalNode | undefined;
    KW_TRANSACTION(): TerminalNode | undefined;
    KW_TRANSACTIONAL(): TerminalNode | undefined;
    KW_TRIGGERS(): TerminalNode | undefined;
    KW_TRUNCATE(): TerminalNode | undefined;
    KW_UNBOUNDED(): TerminalNode | undefined;
    KW_UNDEFINED(): TerminalNode | undefined;
    KW_UNDOFILE(): TerminalNode | undefined;
    KW_UNDO_BUFFER_SIZE(): TerminalNode | undefined;
    KW_UNINSTALL(): TerminalNode | undefined;
    KW_UNKNOWN(): TerminalNode | undefined;
    KW_UNTIL(): TerminalNode | undefined;
    KW_UPGRADE(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_USE_FRM(): TerminalNode | undefined;
    KW_USER_RESOURCES(): TerminalNode | undefined;
    KW_VALIDATION(): TerminalNode | undefined;
    KW_VALUE(): TerminalNode | undefined;
    KW_VAR_POP(): TerminalNode | undefined;
    KW_VAR_SAMP(): TerminalNode | undefined;
    KW_VARIABLES(): TerminalNode | undefined;
    KW_VARIANCE(): TerminalNode | undefined;
    KW_VERSION_TOKEN_ADMIN(): TerminalNode | undefined;
    KW_VIEW(): TerminalNode | undefined;
    KW_VIRTUAL(): TerminalNode | undefined;
    KW_WAIT(): TerminalNode | undefined;
    KW_WARNINGS(): TerminalNode | undefined;
    KW_WITHOUT(): TerminalNode | undefined;
    KW_WORK(): TerminalNode | undefined;
    KW_WRAPPER(): TerminalNode | undefined;
    KW_X509(): TerminalNode | undefined;
    KW_XA(): TerminalNode | undefined;
    KW_XA_RECOVER_ADMIN(): TerminalNode | undefined;
    KW_XML(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
export declare class FunctionNameBaseContext extends ParserRuleContext {
    KW_CHARSET(): TerminalNode | undefined;
    KW_COLLATION(): TerminalNode | undefined;
    KW_COUNT(): TerminalNode | undefined;
    KW_CUME_DIST(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    KW_DATE(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_DENSE_RANK(): TerminalNode | undefined;
    KW_FIRST_VALUE(): TerminalNode | undefined;
    KW_FORMAT(): TerminalNode | undefined;
    KW_GEOMETRYCOLLECTION(): TerminalNode | undefined;
    KW_GET_FORMAT(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_INVISIBLE(): TerminalNode | undefined;
    KW_LAG(): TerminalNode | undefined;
    KW_LAST_VALUE(): TerminalNode | undefined;
    KW_LEAD(): TerminalNode | undefined;
    KW_LEFT(): TerminalNode | undefined;
    KW_LINESTRING(): TerminalNode | undefined;
    KW_MICROSECOND(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    MOD(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_MULTILINESTRING(): TerminalNode | undefined;
    KW_MULTIPOINT(): TerminalNode | undefined;
    KW_MULTIPOLYGON(): TerminalNode | undefined;
    KW_NTH_VALUE(): TerminalNode | undefined;
    KW_NTILE(): TerminalNode | undefined;
    KW_PERCENT_RANK(): TerminalNode | undefined;
    KW_POINT(): TerminalNode | undefined;
    KW_POLYGON(): TerminalNode | undefined;
    KW_POSITION(): TerminalNode | undefined;
    KW_QUARTER(): TerminalNode | undefined;
    KW_RANDOM(): TerminalNode | undefined;
    KW_RANK(): TerminalNode | undefined;
    KW_REVERSE(): TerminalNode | undefined;
    KW_RIGHT(): TerminalNode | undefined;
    KW_ROW_COUNT(): TerminalNode | undefined;
    KW_ROW_NUMBER(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    KW_SESSION_VARIABLES_ADMIN(): TerminalNode | undefined;
    KW_SRID(): TerminalNode | undefined;
    KW_SYSTEM_USER(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_TIMESTAMP(): TerminalNode | undefined;
    KW_VISIBLE(): TerminalNode | undefined;
    KW_WEEK(): TerminalNode | undefined;
    KW_WEIGHT_STRING(): TerminalNode | undefined;
    KW_YEAR(): TerminalNode | undefined;
    KW_JSON_VALUE(): TerminalNode | undefined;
    KW_JSON_TABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: MySqlParserListener): void;
    exitRule(listener: MySqlParserListener): void;
    accept<Result>(visitor: MySqlParserVisitor<Result>): Result;
}
