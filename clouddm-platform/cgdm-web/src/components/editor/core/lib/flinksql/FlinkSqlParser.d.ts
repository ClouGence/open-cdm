import { ATN } from "antlr4ts/atn/ATN";
import { FailedPredicateException } from "antlr4ts/FailedPredicateException";
import { Parser } from "antlr4ts/Parser";
import { ParserRuleContext } from "antlr4ts/ParserRuleContext";
import { RuleContext } from "antlr4ts/RuleContext";
import { TerminalNode } from "antlr4ts/tree/TerminalNode";
import { Token } from "antlr4ts/Token";
import { TokenStream } from "antlr4ts/TokenStream";
import { Vocabulary } from "antlr4ts/Vocabulary";
import { FlinkSqlParserListener } from "./FlinkSqlParserListener";
import { FlinkSqlParserVisitor } from "./FlinkSqlParserVisitor";
export declare class FlinkSqlParser extends Parser {
    static readonly SPACE = 1;
    static readonly COMMENT_INPUT = 2;
    static readonly LINE_COMMENT = 3;
    static readonly KW_ABS = 4;
    static readonly KW_ALL = 5;
    static readonly KW_ALLOCATE = 6;
    static readonly KW_ALLOW = 7;
    static readonly KW_ALTER = 8;
    static readonly KW_ANALYZE = 9;
    static readonly KW_AND = 10;
    static readonly KW_ANY = 11;
    static readonly KW_ARE = 12;
    static readonly KW_ARRAY = 13;
    static readonly KW_ARRAY_AGG = 14;
    static readonly KW_ARRAY_CONCAT_AGG = 15;
    static readonly KW_ARRAY_MAX_CARDINALITY = 16;
    static readonly KW_AS = 17;
    static readonly KW_ASENSITIVE = 18;
    static readonly KW_ASYMMETRIC = 19;
    static readonly KW_AT = 20;
    static readonly KW_ATOMIC = 21;
    static readonly KW_AUTHORIZATION = 22;
    static readonly KW_AVG = 23;
    static readonly KW_BEGIN = 24;
    static readonly KW_BEGIN_FRAME = 25;
    static readonly KW_BEGIN_PARTITION = 26;
    static readonly KW_BETWEEN = 27;
    static readonly KW_BIGINT = 28;
    static readonly KW_BINARY = 29;
    static readonly KW_BIT = 30;
    static readonly KW_BLOB = 31;
    static readonly KW_BOOLEAN = 32;
    static readonly KW_BOTH = 33;
    static readonly KW_BY = 34;
    static readonly KW_BYTES = 35;
    static readonly KW_CALL = 36;
    static readonly KW_CALLED = 37;
    static readonly KW_CARDINALITY = 38;
    static readonly KW_CASCADED = 39;
    static readonly KW_CASE = 40;
    static readonly KW_CAST = 41;
    static readonly KW_CATALOGS = 42;
    static readonly KW_CEIL = 43;
    static readonly KW_CEILING = 44;
    static readonly KW_CHANGELOG_MODE = 45;
    static readonly KW_CHAR = 46;
    static readonly KW_CHARACTER = 47;
    static readonly KW_CHARACTER_LENGTH = 48;
    static readonly KW_CHAR_LENGTH = 49;
    static readonly KW_CHECK = 50;
    static readonly KW_CLASSIFIER = 51;
    static readonly KW_CLOB = 52;
    static readonly KW_CLOSE = 53;
    static readonly KW_COALESCE = 54;
    static readonly KW_COLLATE = 55;
    static readonly KW_COLLECT = 56;
    static readonly KW_COLUMN = 57;
    static readonly KW_COLUMNS = 58;
    static readonly KW_COMMENT = 59;
    static readonly KW_COMMIT = 60;
    static readonly KW_COMPUTE = 61;
    static readonly KW_CONDITION = 62;
    static readonly KW_CONNECT = 63;
    static readonly KW_CONSTRAINT = 64;
    static readonly KW_CONTAINS = 65;
    static readonly KW_CONVERT = 66;
    static readonly KW_CORR = 67;
    static readonly KW_CORRESPONDING = 68;
    static readonly KW_COUNT = 69;
    static readonly KW_COVAR_POP = 70;
    static readonly KW_COVAR_SAMP = 71;
    static readonly KW_CREATE = 72;
    static readonly KW_CROSS = 73;
    static readonly KW_CUBE = 74;
    static readonly KW_CUME_DIST = 75;
    static readonly KW_CURRENT = 76;
    static readonly KW_CURRENT_CATALOG = 77;
    static readonly KW_CURRENT_DATE = 78;
    static readonly KW_CURRENT_DEFAULT_TRANSFORM_GROUP = 79;
    static readonly KW_CURRENT_PATH = 80;
    static readonly KW_CURRENT_ROLE = 81;
    static readonly KW_CURRENT_ROW = 82;
    static readonly KW_CURRENT_SCHEMA = 83;
    static readonly KW_CURRENT_TIME = 84;
    static readonly KW_CURRENT_TIMESTAMP = 85;
    static readonly KW_CURRENT_TRANSFORM_GROUP_FOR_TYPE = 86;
    static readonly KW_CURRENT_USER = 87;
    static readonly KW_CURSOR = 88;
    static readonly KW_CYCLE = 89;
    static readonly KW_DATABASES = 90;
    static readonly KW_DATE = 91;
    static readonly KW_DATETIME = 92;
    static readonly KW_DATETIME_DIFF = 93;
    static readonly KW_DATETIME_TRUNC = 94;
    static readonly KW_DATE_DIFF = 95;
    static readonly KW_DATE_TRUNC = 96;
    static readonly KW_DAY = 97;
    static readonly KW_DAYOFWEEK = 98;
    static readonly KW_DAYOFYEAR = 99;
    static readonly KW_DEALLOCATE = 100;
    static readonly KW_DEC = 101;
    static readonly KW_DECIMAL = 102;
    static readonly KW_DECLARE = 103;
    static readonly KW_DEFAULT = 104;
    static readonly KW_DEFINE = 105;
    static readonly KW_DELETE = 106;
    static readonly KW_DENSE_RANK = 107;
    static readonly KW_DEREF = 108;
    static readonly KW_DESCRIBE = 109;
    static readonly KW_DETERMINISTIC = 110;
    static readonly KW_DISALLOW = 111;
    static readonly KW_DISCONNECT = 112;
    static readonly KW_DISTINCT = 113;
    static readonly KW_DOT = 114;
    static readonly KW_DOUBLE = 115;
    static readonly KW_DROP = 116;
    static readonly KW_DYNAMIC = 117;
    static readonly KW_EACH = 118;
    static readonly KW_ELEMENT = 119;
    static readonly KW_ELSE = 120;
    static readonly KW_EMPTY = 121;
    static readonly KW_END = 122;
    static readonly KW_END_FRAME = 123;
    static readonly KW_END_PARTITION = 124;
    static readonly KW_ENFORCED = 125;
    static readonly KW_EQUALS = 126;
    static readonly KW_ESCAPE = 127;
    static readonly KW_ESTIMATED_COST = 128;
    static readonly KW_EVERY = 129;
    static readonly KW_EXCEPT = 130;
    static readonly KW_EXEC = 131;
    static readonly KW_EXECUTE = 132;
    static readonly KW_EXISTS = 133;
    static readonly KW_EXP = 134;
    static readonly KW_EXPLAIN = 135;
    static readonly KW_EXTEND = 136;
    static readonly KW_EXTENDED = 137;
    static readonly KW_EXTERNAL = 138;
    static readonly KW_EXTRACT = 139;
    static readonly KW_FALSE = 140;
    static readonly KW_FETCH = 141;
    static readonly KW_FILTER = 142;
    static readonly KW_FIRST_VALUE = 143;
    static readonly KW_FLOAT = 144;
    static readonly KW_FLOOR = 145;
    static readonly KW_FOR = 146;
    static readonly KW_FOREIGN = 147;
    static readonly KW_FRAME_ROW = 148;
    static readonly KW_FREE = 149;
    static readonly KW_FRIDAY = 150;
    static readonly KW_FROM = 151;
    static readonly KW_FULL = 152;
    static readonly KW_FUNCTION = 153;
    static readonly KW_FUNCTIONS = 154;
    static readonly KW_FUSION = 155;
    static readonly KW_GET = 156;
    static readonly KW_GLOBAL = 157;
    static readonly KW_GRANT = 158;
    static readonly KW_GROUP = 159;
    static readonly KW_GROUPING = 160;
    static readonly KW_GROUPS = 161;
    static readonly KW_GROUP_CONCAT = 162;
    static readonly KW_HAVING = 163;
    static readonly KW_HOLD = 164;
    static readonly KW_HOUR = 165;
    static readonly KW_IDENTITY = 166;
    static readonly KW_IF = 167;
    static readonly KW_ILIKE = 168;
    static readonly KW_IMPORT = 169;
    static readonly KW_IN = 170;
    static readonly KW_INCLUDE = 171;
    static readonly KW_INDICATOR = 172;
    static readonly KW_INITIAL = 173;
    static readonly KW_INNER = 174;
    static readonly KW_INOUT = 175;
    static readonly KW_INSENSITIVE = 176;
    static readonly KW_INSERT = 177;
    static readonly KW_INT = 178;
    static readonly KW_INTEGER = 179;
    static readonly KW_INTERSECT = 180;
    static readonly KW_INTERSECTION = 181;
    static readonly KW_INTERVAL = 182;
    static readonly KW_INTO = 183;
    static readonly KW_IS = 184;
    static readonly KW_JOIN = 185;
    static readonly KW_JSON_ARRAY = 186;
    static readonly KW_JSON_ARRAYAGG = 187;
    static readonly KW_JSON_EXECUTION_PLAN = 188;
    static readonly KW_JSON_EXISTS = 189;
    static readonly KW_JSON_OBJECT = 190;
    static readonly KW_JSON_OBJECTAGG = 191;
    static readonly KW_JSON_QUERY = 192;
    static readonly KW_JSON_VALUE = 193;
    static readonly KW_LAG = 194;
    static readonly KW_LANGUAGE = 195;
    static readonly KW_LARGE = 196;
    static readonly KW_LAST_VALUE = 197;
    static readonly KW_LATERAL = 198;
    static readonly KW_LEAD = 199;
    static readonly KW_LEADING = 200;
    static readonly KW_LEFT = 201;
    static readonly KW_LIKE = 202;
    static readonly KW_LIKE_REGEX = 203;
    static readonly KW_LIMIT = 204;
    static readonly KW_LN = 205;
    static readonly KW_LOCAL = 206;
    static readonly KW_LOCALTIME = 207;
    static readonly KW_LOCALTIMESTAMP = 208;
    static readonly KW_LOWER = 209;
    static readonly KW_MATCH = 210;
    static readonly KW_MATCHES = 211;
    static readonly KW_MATCH_NUMBER = 212;
    static readonly KW_MATCH_RECOGNIZE = 213;
    static readonly KW_MAX = 214;
    static readonly KW_MEASURES = 215;
    static readonly KW_MEMBER = 216;
    static readonly KW_MERGE = 217;
    static readonly KW_METADATA = 218;
    static readonly KW_METHOD = 219;
    static readonly KW_MIN = 220;
    static readonly KW_MINUS = 221;
    static readonly KW_MINUTE = 222;
    static readonly KW_MOD = 223;
    static readonly KW_MODIFIES = 224;
    static readonly KW_MODIFY = 225;
    static readonly KW_MODULE = 226;
    static readonly KW_MODULES = 227;
    static readonly KW_MONDAY = 228;
    static readonly KW_MONTH = 229;
    static readonly KW_MORE = 230;
    static readonly KW_MULTISET = 231;
    static readonly KW_NATIONAL = 232;
    static readonly KW_NATURAL = 233;
    static readonly KW_NCHAR = 234;
    static readonly KW_NCLOB = 235;
    static readonly KW_NEW = 236;
    static readonly KW_NEXT = 237;
    static readonly KW_NO = 238;
    static readonly KW_NONE = 239;
    static readonly KW_NORMALIZE = 240;
    static readonly KW_NOT = 241;
    static readonly KW_NTH_VALUE = 242;
    static readonly KW_NTILE = 243;
    static readonly KW_NULL = 244;
    static readonly KW_NULLIF = 245;
    static readonly KW_NUMERIC = 246;
    static readonly KW_OCCURRENCES_REGEX = 247;
    static readonly KW_OCTET_LENGTH = 248;
    static readonly KW_OF = 249;
    static readonly KW_OFFSET = 250;
    static readonly KW_OLD = 251;
    static readonly KW_OMIT = 252;
    static readonly KW_ON = 253;
    static readonly KW_ONE = 254;
    static readonly KW_ONLY = 255;
    static readonly KW_OPEN = 256;
    static readonly KW_OR = 257;
    static readonly KW_ORDER = 258;
    static readonly KW_ORDINAL = 259;
    static readonly KW_OUT = 260;
    static readonly KW_OUTER = 261;
    static readonly KW_OVER = 262;
    static readonly KW_OVERLAPS = 263;
    static readonly KW_OVERLAY = 264;
    static readonly KW_OVERWRITE = 265;
    static readonly KW_OVERWRITING = 266;
    static readonly KW_PARAMETER = 267;
    static readonly KW_PARTITION = 268;
    static readonly KW_PARTITIONED = 269;
    static readonly KW_PARTITIONS = 270;
    static readonly KW_PATTERN = 271;
    static readonly KW_PER = 272;
    static readonly KW_PERCENT = 273;
    static readonly KW_PERCENTILE_CONT = 274;
    static readonly KW_PERCENTILE_DISC = 275;
    static readonly KW_PERCENT_RANK = 276;
    static readonly KW_PERIOD = 277;
    static readonly KW_PERMUTE = 278;
    static readonly KW_PIVOT = 279;
    static readonly KW_PORTION = 280;
    static readonly KW_POSITION = 281;
    static readonly KW_POSITION_REGEX = 282;
    static readonly KW_POWER = 283;
    static readonly KW_PRECEDES = 284;
    static readonly KW_PRECISION = 285;
    static readonly KW_PREPARE = 286;
    static readonly KW_PREV = 287;
    static readonly KW_PRIMARY = 288;
    static readonly KW_PROCEDURE = 289;
    static readonly KW_QUALIFY = 290;
    static readonly KW_QUARTERS = 291;
    static readonly KW_RANGE = 292;
    static readonly KW_RANK = 293;
    static readonly KW_RAW = 294;
    static readonly KW_READS = 295;
    static readonly KW_REAL = 296;
    static readonly KW_RECURSIVE = 297;
    static readonly KW_REF = 298;
    static readonly KW_REFERENCES = 299;
    static readonly KW_REFERENCING = 300;
    static readonly KW_REGR_AVGX = 301;
    static readonly KW_REGR_AVGY = 302;
    static readonly KW_REGR_COUNT = 303;
    static readonly KW_REGR_INTERCEPT = 304;
    static readonly KW_REGR_R2 = 305;
    static readonly KW_REGR_SLOPE = 306;
    static readonly KW_REGR_SXX = 307;
    static readonly KW_REGR_SXY = 308;
    static readonly KW_REGR_SYY = 309;
    static readonly KW_RELEASE = 310;
    static readonly KW_RENAME = 311;
    static readonly KW_RESET = 312;
    static readonly KW_RESULT = 313;
    static readonly KW_RETURN = 314;
    static readonly KW_RETURNS = 315;
    static readonly KW_REVOKE = 316;
    static readonly KW_RIGHT = 317;
    static readonly KW_RLIKE = 318;
    static readonly KW_ROLLBACK = 319;
    static readonly KW_ROLLUP = 320;
    static readonly KW_ROW = 321;
    static readonly KW_ROWS = 322;
    static readonly KW_ROW_NUMBER = 323;
    static readonly KW_RUNNING = 324;
    static readonly KW_SAFE_CAST = 325;
    static readonly KW_SAFE_OFFSET = 326;
    static readonly KW_SAFE_ORDINAL = 327;
    static readonly KW_SATURDAY = 328;
    static readonly KW_SAVEPOINT = 329;
    static readonly KW_SCALA = 330;
    static readonly KW_SCOPE = 331;
    static readonly KW_SCROLL = 332;
    static readonly KW_SEARCH = 333;
    static readonly KW_SECOND = 334;
    static readonly KW_SEEK = 335;
    static readonly KW_SELECT = 336;
    static readonly KW_SENSITIVE = 337;
    static readonly KW_SEPARATOR = 338;
    static readonly KW_SESSION_USER = 339;
    static readonly KW_SET = 340;
    static readonly KW_SHOW = 341;
    static readonly KW_SIMILAR = 342;
    static readonly KW_SKIP = 343;
    static readonly KW_SMALLINT = 344;
    static readonly KW_SOME = 345;
    static readonly KW_SPECIFIC = 346;
    static readonly KW_SPECIFICTYPE = 347;
    static readonly KW_SQL = 348;
    static readonly KW_SQLEXCEPTION = 349;
    static readonly KW_SQLSTATE = 350;
    static readonly KW_SQLWARNING = 351;
    static readonly KW_SQRT = 352;
    static readonly KW_START = 353;
    static readonly KW_STATEMENT = 354;
    static readonly KW_STATIC = 355;
    static readonly KW_STATISTICS = 356;
    static readonly KW_STDDEV_POP = 357;
    static readonly KW_STDDEV_SAMP = 358;
    static readonly KW_STREAM = 359;
    static readonly KW_STRING = 360;
    static readonly KW_STRING_AGG = 361;
    static readonly KW_SUBMULTISET = 362;
    static readonly KW_SUBSET = 363;
    static readonly KW_SUBSTRING = 364;
    static readonly KW_SUBSTRING_REGEX = 365;
    static readonly KW_SUCCEEDS = 366;
    static readonly KW_SUM = 367;
    static readonly KW_SUNDAY = 368;
    static readonly KW_SYMMETRIC = 369;
    static readonly KW_SYSTEM = 370;
    static readonly KW_SYSTEM_TIME = 371;
    static readonly KW_SYSTEM_USER = 372;
    static readonly KW_TABLE = 373;
    static readonly KW_TABLES = 374;
    static readonly KW_TABLESAMPLE = 375;
    static readonly KW_THEN = 376;
    static readonly KW_THURSDAY = 377;
    static readonly KW_TIME = 378;
    static readonly KW_TIMESTAMP = 379;
    static readonly KW_TIMESTAMP_DIFF = 380;
    static readonly KW_TIMESTAMP_LTZ = 381;
    static readonly KW_TIMESTAMP_TRUNC = 382;
    static readonly KW_TIMEZONE_HOUR = 383;
    static readonly KW_TIMEZONE_MINUTE = 384;
    static readonly KW_TIME_DIFF = 385;
    static readonly KW_TIME_TRUNC = 386;
    static readonly KW_TINYINT = 387;
    static readonly KW_TO = 388;
    static readonly KW_TRAILING = 389;
    static readonly KW_TRANSLATE = 390;
    static readonly KW_TRANSLATE_REGEX = 391;
    static readonly KW_TRANSLATION = 392;
    static readonly KW_TREAT = 393;
    static readonly KW_TRIGGER = 394;
    static readonly KW_TRIM = 395;
    static readonly KW_TRIM_ARRAY = 396;
    static readonly KW_TRUE = 397;
    static readonly KW_TRUNCATE = 398;
    static readonly KW_TRY_CAST = 399;
    static readonly KW_TUESDAY = 400;
    static readonly KW_UESCAPE = 401;
    static readonly KW_UNION = 402;
    static readonly KW_UNIQUE = 403;
    static readonly KW_UNKNOWN = 404;
    static readonly KW_UNNEST = 405;
    static readonly KW_UNPIVOT = 406;
    static readonly KW_UPDATE = 407;
    static readonly KW_UPPER = 408;
    static readonly KW_UPSERT = 409;
    static readonly KW_USE = 410;
    static readonly KW_USER = 411;
    static readonly KW_USING = 412;
    static readonly KW_VALUE = 413;
    static readonly KW_VALUES = 414;
    static readonly KW_VALUE_OF = 415;
    static readonly KW_VARBINARY = 416;
    static readonly KW_VARCHAR = 417;
    static readonly KW_VARYING = 418;
    static readonly KW_VAR_POP = 419;
    static readonly KW_VAR_SAMP = 420;
    static readonly KW_VERSIONING = 421;
    static readonly KW_VIEWS = 422;
    static readonly KW_VIRTUAL = 423;
    static readonly KW_WATERMARK = 424;
    static readonly KW_WATERMARKS = 425;
    static readonly KW_WEDNESDAY = 426;
    static readonly KW_WEEKS = 427;
    static readonly KW_WHEN = 428;
    static readonly KW_WHENEVER = 429;
    static readonly KW_WHERE = 430;
    static readonly KW_WIDTH_BUCKET = 431;
    static readonly KW_WINDOW = 432;
    static readonly KW_WITH = 433;
    static readonly KW_WITHIN = 434;
    static readonly KW_WITHOUT = 435;
    static readonly KW_YEAR = 436;
    static readonly KW_ADD = 437;
    static readonly KW_AFTER = 438;
    static readonly KW_ASC = 439;
    static readonly KW_CASCADE = 440;
    static readonly KW_CATALOG = 441;
    static readonly KW_CENTURY = 442;
    static readonly KW_CONFIG = 443;
    static readonly KW_CONSTRAINTS = 444;
    static readonly KW_CUMULATE = 445;
    static readonly KW_DATA = 446;
    static readonly KW_DATABASE = 447;
    static readonly KW_DAYS = 448;
    static readonly KW_DECADE = 449;
    static readonly KW_DESC = 450;
    static readonly KW_DESCRIPTOR = 451;
    static readonly KW_DIV = 452;
    static readonly KW_ENGINE = 453;
    static readonly KW_EPOCH = 454;
    static readonly KW_EXCLUDING = 455;
    static readonly KW_FILE = 456;
    static readonly KW_FIRST = 457;
    static readonly KW_GENERATED = 458;
    static readonly KW_HOP = 459;
    static readonly KW_HOURS = 460;
    static readonly KW_IGNORE = 461;
    static readonly KW_INCLUDING = 462;
    static readonly KW_JAR = 463;
    static readonly KW_JARS = 464;
    static readonly KW_JAVA = 465;
    static readonly KW_KEY = 466;
    static readonly KW_LAST = 467;
    static readonly KW_LOAD = 468;
    static readonly KW_MAP = 469;
    static readonly KW_MICROSECOND = 470;
    static readonly KW_MILLENNIUM = 471;
    static readonly KW_MILLISECOND = 472;
    static readonly KW_MINUTES = 473;
    static readonly KW_MONTHS = 474;
    static readonly KW_NANOSECOND = 475;
    static readonly KW_NULLS = 476;
    static readonly KW_OPTIONS = 477;
    static readonly KW_PAST = 478;
    static readonly KW_PLAN = 479;
    static readonly KW_PRECEDING = 480;
    static readonly KW_PYTHON = 481;
    static readonly KW_PYTHON_ARCHIVES = 482;
    static readonly KW_PYTHON_DEPENDENCIES = 483;
    static readonly KW_PYTHON_FILES = 484;
    static readonly KW_PYTHON_JAR = 485;
    static readonly KW_PYTHON_PARAMETER = 486;
    static readonly KW_PYTHON_REQUIREMENTS = 487;
    static readonly KW_QUARTER = 488;
    static readonly KW_REMOVE = 489;
    static readonly KW_RESTRICT = 490;
    static readonly KW_SECONDS = 491;
    static readonly KW_SESSION = 492;
    static readonly KW_SETS = 493;
    static readonly KW_SIZE = 494;
    static readonly KW_SLIDE = 495;
    static readonly KW_STEP = 496;
    static readonly KW_TEMPORARY = 497;
    static readonly KW_TIMECOL = 498;
    static readonly KW_TUMBLE = 499;
    static readonly KW_UNLOAD = 500;
    static readonly KW_VIEW = 501;
    static readonly KW_WEEK = 502;
    static readonly KW_YEARS = 503;
    static readonly KW_ZONE = 504;
    static readonly EQUAL_SYMBOL = 505;
    static readonly GREATER_SYMBOL = 506;
    static readonly LESS_SYMBOL = 507;
    static readonly EXCLAMATION_SYMBOL = 508;
    static readonly BIT_NOT_OP = 509;
    static readonly BIT_OR_OP = 510;
    static readonly BIT_AND_OP = 511;
    static readonly BIT_XOR_OP = 512;
    static readonly DOT = 513;
    static readonly LS_BRACKET = 514;
    static readonly RS_BRACKET = 515;
    static readonly LR_BRACKET = 516;
    static readonly RR_BRACKET = 517;
    static readonly LB_BRACKET = 518;
    static readonly RB_BRACKET = 519;
    static readonly COMMA = 520;
    static readonly SEMICOLON = 521;
    static readonly AT_SIGN = 522;
    static readonly SINGLE_QUOTE_SYMB = 523;
    static readonly DOUBLE_QUOTE_SYMB = 524;
    static readonly REVERSE_QUOTE_SYMB = 525;
    static readonly COLON_SYMB = 526;
    static readonly ASTERISK_SIGN = 527;
    static readonly UNDERLINE_SIGN = 528;
    static readonly HYPNEN_SIGN = 529;
    static readonly ADD_SIGN = 530;
    static readonly PENCENT_SIGN = 531;
    static readonly DOUBLE_VERTICAL_SIGN = 532;
    static readonly DOUBLE_HYPNEN_SIGN = 533;
    static readonly SLASH_SIGN = 534;
    static readonly QUESTION_MARK_SIGN = 535;
    static readonly DOUBLE_RIGHT_ARROW = 536;
    static readonly STRING_LITERAL = 537;
    static readonly DIG_LITERAL = 538;
    static readonly REAL_LITERAL = 539;
    static readonly BIT_STRING = 540;
    static readonly ID_LITERAL = 541;
    static readonly RULE_program = 0;
    static readonly RULE_singleStatement = 1;
    static readonly RULE_sqlStatement = 2;
    static readonly RULE_emptyStatement = 3;
    static readonly RULE_ddlStatement = 4;
    static readonly RULE_dmlStatement = 5;
    static readonly RULE_describeStatement = 6;
    static readonly RULE_explainStatement = 7;
    static readonly RULE_explainDetails = 8;
    static readonly RULE_explainDetail = 9;
    static readonly RULE_useStatement = 10;
    static readonly RULE_useModuleStatement = 11;
    static readonly RULE_showStatememt = 12;
    static readonly RULE_loadStatement = 13;
    static readonly RULE_unloadStatememt = 14;
    static readonly RULE_setStatememt = 15;
    static readonly RULE_resetStatememt = 16;
    static readonly RULE_jarStatememt = 17;
    static readonly RULE_dtAddStatement = 18;
    static readonly RULE_dtFilePath = 19;
    static readonly RULE_createTable = 20;
    static readonly RULE_simpleCreateTable = 21;
    static readonly RULE_createTableAsSelect = 22;
    static readonly RULE_columnOptionDefinition = 23;
    static readonly RULE_physicalColumnDefinition = 24;
    static readonly RULE_columnNameCreate = 25;
    static readonly RULE_columnName = 26;
    static readonly RULE_columnNameList = 27;
    static readonly RULE_columnType = 28;
    static readonly RULE_lengthOneDimension = 29;
    static readonly RULE_lengthTwoOptionalDimension = 30;
    static readonly RULE_lengthTwoStringDimension = 31;
    static readonly RULE_lengthOneTypeDimension = 32;
    static readonly RULE_mapTypeDimension = 33;
    static readonly RULE_rowTypeDimension = 34;
    static readonly RULE_columnConstraint = 35;
    static readonly RULE_commentSpec = 36;
    static readonly RULE_metadataColumnDefinition = 37;
    static readonly RULE_metadataKey = 38;
    static readonly RULE_computedColumnDefinition = 39;
    static readonly RULE_computedColumnExpression = 40;
    static readonly RULE_watermarkDefinition = 41;
    static readonly RULE_tableConstraint = 42;
    static readonly RULE_constraintName = 43;
    static readonly RULE_selfDefinitionClause = 44;
    static readonly RULE_partitionDefinition = 45;
    static readonly RULE_transformList = 46;
    static readonly RULE_transform = 47;
    static readonly RULE_transformArgument = 48;
    static readonly RULE_likeDefinition = 49;
    static readonly RULE_likeOption = 50;
    static readonly RULE_createCatalog = 51;
    static readonly RULE_createDatabase = 52;
    static readonly RULE_createView = 53;
    static readonly RULE_createFunction = 54;
    static readonly RULE_usingClause = 55;
    static readonly RULE_jarFileName = 56;
    static readonly RULE_alterTable = 57;
    static readonly RULE_renameDefinition = 58;
    static readonly RULE_setKeyValueDefinition = 59;
    static readonly RULE_addConstraint = 60;
    static readonly RULE_dropConstraint = 61;
    static readonly RULE_addUnique = 62;
    static readonly RULE_notForced = 63;
    static readonly RULE_alertView = 64;
    static readonly RULE_alterDatabase = 65;
    static readonly RULE_alterFunction = 66;
    static readonly RULE_dropCatalog = 67;
    static readonly RULE_dropTable = 68;
    static readonly RULE_dropDatabase = 69;
    static readonly RULE_dropView = 70;
    static readonly RULE_dropFunction = 71;
    static readonly RULE_insertStatement = 72;
    static readonly RULE_insertSimpleStatement = 73;
    static readonly RULE_insertPartitionDefinition = 74;
    static readonly RULE_valuesDefinition = 75;
    static readonly RULE_valuesRowDefinition = 76;
    static readonly RULE_insertMulStatementCompatibility = 77;
    static readonly RULE_insertMulStatement = 78;
    static readonly RULE_queryStatement = 79;
    static readonly RULE_valuesCaluse = 80;
    static readonly RULE_withClause = 81;
    static readonly RULE_withItem = 82;
    static readonly RULE_withItemName = 83;
    static readonly RULE_selectStatement = 84;
    static readonly RULE_selectClause = 85;
    static readonly RULE_projectItemDefinition = 86;
    static readonly RULE_overWindowItem = 87;
    static readonly RULE_fromClause = 88;
    static readonly RULE_tableExpression = 89;
    static readonly RULE_tableReference = 90;
    static readonly RULE_tablePrimary = 91;
    static readonly RULE_systemTimePeriod = 92;
    static readonly RULE_dateTimeExpression = 93;
    static readonly RULE_inlineDataValueClause = 94;
    static readonly RULE_windoTVFClause = 95;
    static readonly RULE_windowTVFExression = 96;
    static readonly RULE_windoTVFName = 97;
    static readonly RULE_windowTVFParam = 98;
    static readonly RULE_timeIntervalParamName = 99;
    static readonly RULE_columnDescriptor = 100;
    static readonly RULE_joinCondition = 101;
    static readonly RULE_whereClause = 102;
    static readonly RULE_groupByClause = 103;
    static readonly RULE_groupItemDefinition = 104;
    static readonly RULE_groupingSets = 105;
    static readonly RULE_groupingSetsNotaionName = 106;
    static readonly RULE_groupWindowFunction = 107;
    static readonly RULE_groupWindowFunctionName = 108;
    static readonly RULE_timeAttrColumn = 109;
    static readonly RULE_havingClause = 110;
    static readonly RULE_windowClause = 111;
    static readonly RULE_namedWindow = 112;
    static readonly RULE_windowSpec = 113;
    static readonly RULE_matchRecognizeClause = 114;
    static readonly RULE_orderByCaluse = 115;
    static readonly RULE_orderItemDefition = 116;
    static readonly RULE_limitClause = 117;
    static readonly RULE_partitionByClause = 118;
    static readonly RULE_quantifiers = 119;
    static readonly RULE_measuresClause = 120;
    static readonly RULE_patternDefination = 121;
    static readonly RULE_patternVariable = 122;
    static readonly RULE_outputMode = 123;
    static readonly RULE_afterMatchStrategy = 124;
    static readonly RULE_patternVariablesDefination = 125;
    static readonly RULE_windowFrame = 126;
    static readonly RULE_frameBound = 127;
    static readonly RULE_withinClause = 128;
    static readonly RULE_expression = 129;
    static readonly RULE_booleanExpression = 130;
    static readonly RULE_predicate = 131;
    static readonly RULE_likePredicate = 132;
    static readonly RULE_valueExpression = 133;
    static readonly RULE_primaryExpression = 134;
    static readonly RULE_functionNameCreate = 135;
    static readonly RULE_functionName = 136;
    static readonly RULE_functionParam = 137;
    static readonly RULE_dereferenceDefinition = 138;
    static readonly RULE_correlationName = 139;
    static readonly RULE_qualifiedName = 140;
    static readonly RULE_timeIntervalExpression = 141;
    static readonly RULE_errorCapturingMultiUnitsInterval = 142;
    static readonly RULE_multiUnitsInterval = 143;
    static readonly RULE_errorCapturingUnitToUnitInterval = 144;
    static readonly RULE_unitToUnitInterval = 145;
    static readonly RULE_intervalValue = 146;
    static readonly RULE_tableAlias = 147;
    static readonly RULE_errorCapturingIdentifier = 148;
    static readonly RULE_errorCapturingIdentifierExtra = 149;
    static readonly RULE_identifierList = 150;
    static readonly RULE_identifierSeq = 151;
    static readonly RULE_identifier = 152;
    static readonly RULE_unquotedIdentifier = 153;
    static readonly RULE_quotedIdentifier = 154;
    static readonly RULE_whenClause = 155;
    static readonly RULE_catalogPath = 156;
    static readonly RULE_catalogPathCreate = 157;
    static readonly RULE_databasePath = 158;
    static readonly RULE_databasePathCreate = 159;
    static readonly RULE_tablePathCreate = 160;
    static readonly RULE_tablePath = 161;
    static readonly RULE_viewPath = 162;
    static readonly RULE_viewPathCreate = 163;
    static readonly RULE_uid = 164;
    static readonly RULE_withOption = 165;
    static readonly RULE_ifNotExists = 166;
    static readonly RULE_ifExists = 167;
    static readonly RULE_tablePropertyList = 168;
    static readonly RULE_tableProperty = 169;
    static readonly RULE_tablePropertyKey = 170;
    static readonly RULE_tablePropertyValue = 171;
    static readonly RULE_logicalOperator = 172;
    static readonly RULE_comparisonOperator = 173;
    static readonly RULE_bitOperator = 174;
    static readonly RULE_mathOperator = 175;
    static readonly RULE_unaryOperator = 176;
    static readonly RULE_constant = 177;
    static readonly RULE_timePointLiteral = 178;
    static readonly RULE_stringLiteral = 179;
    static readonly RULE_decimalLiteral = 180;
    static readonly RULE_booleanLiteral = 181;
    static readonly RULE_setQuantifier = 182;
    static readonly RULE_timePointUnit = 183;
    static readonly RULE_timeIntervalUnit = 184;
    static readonly RULE_reservedKeywordsUsedAsFuncParam = 185;
    static readonly RULE_reservedKeywordsUsedAsFuncName = 186;
    static readonly RULE_nonReservedKeywords = 187;
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
    emptyStatement(): EmptyStatementContext;
    ddlStatement(): DdlStatementContext;
    dmlStatement(): DmlStatementContext;
    describeStatement(): DescribeStatementContext;
    explainStatement(): ExplainStatementContext;
    explainDetails(): ExplainDetailsContext;
    explainDetail(): ExplainDetailContext;
    useStatement(): UseStatementContext;
    useModuleStatement(): UseModuleStatementContext;
    showStatememt(): ShowStatememtContext;
    loadStatement(): LoadStatementContext;
    unloadStatememt(): UnloadStatememtContext;
    setStatememt(): SetStatememtContext;
    resetStatememt(): ResetStatememtContext;
    jarStatememt(): JarStatememtContext;
    dtAddStatement(): DtAddStatementContext;
    dtFilePath(): DtFilePathContext;
    createTable(): CreateTableContext;
    simpleCreateTable(): SimpleCreateTableContext;
    createTableAsSelect(): CreateTableAsSelectContext;
    columnOptionDefinition(): ColumnOptionDefinitionContext;
    physicalColumnDefinition(): PhysicalColumnDefinitionContext;
    columnNameCreate(): ColumnNameCreateContext;
    columnName(): ColumnNameContext;
    columnNameList(): ColumnNameListContext;
    columnType(): ColumnTypeContext;
    lengthOneDimension(): LengthOneDimensionContext;
    lengthTwoOptionalDimension(): LengthTwoOptionalDimensionContext;
    lengthTwoStringDimension(): LengthTwoStringDimensionContext;
    lengthOneTypeDimension(): LengthOneTypeDimensionContext;
    mapTypeDimension(): MapTypeDimensionContext;
    rowTypeDimension(): RowTypeDimensionContext;
    columnConstraint(): ColumnConstraintContext;
    commentSpec(): CommentSpecContext;
    metadataColumnDefinition(): MetadataColumnDefinitionContext;
    metadataKey(): MetadataKeyContext;
    computedColumnDefinition(): ComputedColumnDefinitionContext;
    computedColumnExpression(): ComputedColumnExpressionContext;
    watermarkDefinition(): WatermarkDefinitionContext;
    tableConstraint(): TableConstraintContext;
    constraintName(): ConstraintNameContext;
    selfDefinitionClause(): SelfDefinitionClauseContext;
    partitionDefinition(): PartitionDefinitionContext;
    transformList(): TransformListContext;
    transform(): TransformContext;
    transformArgument(): TransformArgumentContext;
    likeDefinition(): LikeDefinitionContext;
    likeOption(): LikeOptionContext;
    createCatalog(): CreateCatalogContext;
    createDatabase(): CreateDatabaseContext;
    createView(): CreateViewContext;
    createFunction(): CreateFunctionContext;
    usingClause(): UsingClauseContext;
    jarFileName(): JarFileNameContext;
    alterTable(): AlterTableContext;
    renameDefinition(): RenameDefinitionContext;
    setKeyValueDefinition(): SetKeyValueDefinitionContext;
    addConstraint(): AddConstraintContext;
    dropConstraint(): DropConstraintContext;
    addUnique(): AddUniqueContext;
    notForced(): NotForcedContext;
    alertView(): AlertViewContext;
    alterDatabase(): AlterDatabaseContext;
    alterFunction(): AlterFunctionContext;
    dropCatalog(): DropCatalogContext;
    dropTable(): DropTableContext;
    dropDatabase(): DropDatabaseContext;
    dropView(): DropViewContext;
    dropFunction(): DropFunctionContext;
    insertStatement(): InsertStatementContext;
    insertSimpleStatement(): InsertSimpleStatementContext;
    insertPartitionDefinition(): InsertPartitionDefinitionContext;
    valuesDefinition(): ValuesDefinitionContext;
    valuesRowDefinition(): ValuesRowDefinitionContext;
    insertMulStatementCompatibility(): InsertMulStatementCompatibilityContext;
    insertMulStatement(): InsertMulStatementContext;
    queryStatement(): QueryStatementContext;
    queryStatement(_p: number): QueryStatementContext;
    valuesCaluse(): ValuesCaluseContext;
    withClause(): WithClauseContext;
    withItem(): WithItemContext;
    withItemName(): WithItemNameContext;
    selectStatement(): SelectStatementContext;
    selectClause(): SelectClauseContext;
    projectItemDefinition(): ProjectItemDefinitionContext;
    overWindowItem(): OverWindowItemContext;
    fromClause(): FromClauseContext;
    tableExpression(): TableExpressionContext;
    tableExpression(_p: number): TableExpressionContext;
    tableReference(): TableReferenceContext;
    tablePrimary(): TablePrimaryContext;
    systemTimePeriod(): SystemTimePeriodContext;
    dateTimeExpression(): DateTimeExpressionContext;
    inlineDataValueClause(): InlineDataValueClauseContext;
    windoTVFClause(): WindoTVFClauseContext;
    windowTVFExression(): WindowTVFExressionContext;
    windoTVFName(): WindoTVFNameContext;
    windowTVFParam(): WindowTVFParamContext;
    timeIntervalParamName(): TimeIntervalParamNameContext;
    columnDescriptor(): ColumnDescriptorContext;
    joinCondition(): JoinConditionContext;
    whereClause(): WhereClauseContext;
    groupByClause(): GroupByClauseContext;
    groupItemDefinition(): GroupItemDefinitionContext;
    groupingSets(): GroupingSetsContext;
    groupingSetsNotaionName(): GroupingSetsNotaionNameContext;
    groupWindowFunction(): GroupWindowFunctionContext;
    groupWindowFunctionName(): GroupWindowFunctionNameContext;
    timeAttrColumn(): TimeAttrColumnContext;
    havingClause(): HavingClauseContext;
    windowClause(): WindowClauseContext;
    namedWindow(): NamedWindowContext;
    windowSpec(): WindowSpecContext;
    matchRecognizeClause(): MatchRecognizeClauseContext;
    orderByCaluse(): OrderByCaluseContext;
    orderItemDefition(): OrderItemDefitionContext;
    limitClause(): LimitClauseContext;
    partitionByClause(): PartitionByClauseContext;
    quantifiers(): QuantifiersContext;
    measuresClause(): MeasuresClauseContext;
    patternDefination(): PatternDefinationContext;
    patternVariable(): PatternVariableContext;
    outputMode(): OutputModeContext;
    afterMatchStrategy(): AfterMatchStrategyContext;
    patternVariablesDefination(): PatternVariablesDefinationContext;
    windowFrame(): WindowFrameContext;
    frameBound(): FrameBoundContext;
    withinClause(): WithinClauseContext;
    expression(): ExpressionContext;
    booleanExpression(): BooleanExpressionContext;
    booleanExpression(_p: number): BooleanExpressionContext;
    predicate(): PredicateContext;
    likePredicate(): LikePredicateContext;
    valueExpression(): ValueExpressionContext;
    valueExpression(_p: number): ValueExpressionContext;
    primaryExpression(): PrimaryExpressionContext;
    primaryExpression(_p: number): PrimaryExpressionContext;
    functionNameCreate(): FunctionNameCreateContext;
    functionName(): FunctionNameContext;
    functionParam(): FunctionParamContext;
    dereferenceDefinition(): DereferenceDefinitionContext;
    correlationName(): CorrelationNameContext;
    qualifiedName(): QualifiedNameContext;
    timeIntervalExpression(): TimeIntervalExpressionContext;
    errorCapturingMultiUnitsInterval(): ErrorCapturingMultiUnitsIntervalContext;
    multiUnitsInterval(): MultiUnitsIntervalContext;
    errorCapturingUnitToUnitInterval(): ErrorCapturingUnitToUnitIntervalContext;
    unitToUnitInterval(): UnitToUnitIntervalContext;
    intervalValue(): IntervalValueContext;
    tableAlias(): TableAliasContext;
    errorCapturingIdentifier(): ErrorCapturingIdentifierContext;
    errorCapturingIdentifierExtra(): ErrorCapturingIdentifierExtraContext;
    identifierList(): IdentifierListContext;
    identifierSeq(): IdentifierSeqContext;
    identifier(): IdentifierContext;
    unquotedIdentifier(): UnquotedIdentifierContext;
    quotedIdentifier(): QuotedIdentifierContext;
    whenClause(): WhenClauseContext;
    catalogPath(): CatalogPathContext;
    catalogPathCreate(): CatalogPathCreateContext;
    databasePath(): DatabasePathContext;
    databasePathCreate(): DatabasePathCreateContext;
    tablePathCreate(): TablePathCreateContext;
    tablePath(): TablePathContext;
    viewPath(): ViewPathContext;
    viewPathCreate(): ViewPathCreateContext;
    uid(): UidContext;
    withOption(): WithOptionContext;
    ifNotExists(): IfNotExistsContext;
    ifExists(): IfExistsContext;
    tablePropertyList(): TablePropertyListContext;
    tableProperty(): TablePropertyContext;
    tablePropertyKey(): TablePropertyKeyContext;
    tablePropertyValue(): TablePropertyValueContext;
    logicalOperator(): LogicalOperatorContext;
    comparisonOperator(): ComparisonOperatorContext;
    bitOperator(): BitOperatorContext;
    mathOperator(): MathOperatorContext;
    unaryOperator(): UnaryOperatorContext;
    constant(): ConstantContext;
    timePointLiteral(): TimePointLiteralContext;
    stringLiteral(): StringLiteralContext;
    decimalLiteral(): DecimalLiteralContext;
    booleanLiteral(): BooleanLiteralContext;
    setQuantifier(): SetQuantifierContext;
    timePointUnit(): TimePointUnitContext;
    timeIntervalUnit(): TimeIntervalUnitContext;
    reservedKeywordsUsedAsFuncParam(): ReservedKeywordsUsedAsFuncParamContext;
    reservedKeywordsUsedAsFuncName(): ReservedKeywordsUsedAsFuncNameContext;
    nonReservedKeywords(): NonReservedKeywordsContext;
    sempred(_localctx: RuleContext, ruleIndex: number, predIndex: number): boolean;
    private queryStatement_sempred;
    private tableExpression_sempred;
    private booleanExpression_sempred;
    private valueExpression_sempred;
    private primaryExpression_sempred;
    private static readonly _serializedATNSegments;
    private static readonly _serializedATNSegment0;
    private static readonly _serializedATNSegment1;
    private static readonly _serializedATNSegment2;
    private static readonly _serializedATNSegment3;
    private static readonly _serializedATNSegment4;
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
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SingleStatementContext extends ParserRuleContext {
    sqlStatement(): SqlStatementContext | undefined;
    SEMICOLON(): TerminalNode | undefined;
    emptyStatement(): EmptyStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SqlStatementContext extends ParserRuleContext {
    ddlStatement(): DdlStatementContext | undefined;
    dmlStatement(): DmlStatementContext | undefined;
    describeStatement(): DescribeStatementContext | undefined;
    explainStatement(): ExplainStatementContext | undefined;
    useStatement(): UseStatementContext | undefined;
    showStatememt(): ShowStatememtContext | undefined;
    loadStatement(): LoadStatementContext | undefined;
    unloadStatememt(): UnloadStatememtContext | undefined;
    setStatememt(): SetStatememtContext | undefined;
    resetStatememt(): ResetStatememtContext | undefined;
    jarStatememt(): JarStatememtContext | undefined;
    dtAddStatement(): DtAddStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class EmptyStatementContext extends ParserRuleContext {
    SEMICOLON(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DdlStatementContext extends ParserRuleContext {
    createTable(): CreateTableContext | undefined;
    createDatabase(): CreateDatabaseContext | undefined;
    createView(): CreateViewContext | undefined;
    createFunction(): CreateFunctionContext | undefined;
    createCatalog(): CreateCatalogContext | undefined;
    alterTable(): AlterTableContext | undefined;
    alertView(): AlertViewContext | undefined;
    alterDatabase(): AlterDatabaseContext | undefined;
    alterFunction(): AlterFunctionContext | undefined;
    dropCatalog(): DropCatalogContext | undefined;
    dropTable(): DropTableContext | undefined;
    dropDatabase(): DropDatabaseContext | undefined;
    dropView(): DropViewContext | undefined;
    dropFunction(): DropFunctionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DmlStatementContext extends ParserRuleContext {
    queryStatement(): QueryStatementContext | undefined;
    insertStatement(): InsertStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DescribeStatementContext extends ParserRuleContext {
    tablePath(): TablePathContext;
    KW_DESCRIBE(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ExplainStatementContext extends ParserRuleContext {
    KW_EXPLAIN(): TerminalNode;
    dmlStatement(): DmlStatementContext | undefined;
    insertSimpleStatement(): InsertSimpleStatementContext | undefined;
    insertMulStatement(): InsertMulStatementContext | undefined;
    explainDetails(): ExplainDetailsContext | undefined;
    KW_PLAN(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ExplainDetailsContext extends ParserRuleContext {
    explainDetail(): ExplainDetailContext[];
    explainDetail(i: number): ExplainDetailContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ExplainDetailContext extends ParserRuleContext {
    KW_CHANGELOG_MODE(): TerminalNode | undefined;
    KW_JSON_EXECUTION_PLAN(): TerminalNode | undefined;
    KW_ESTIMATED_COST(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class UseStatementContext extends ParserRuleContext {
    KW_USE(): TerminalNode | undefined;
    KW_CATALOG(): TerminalNode | undefined;
    catalogPath(): CatalogPathContext | undefined;
    databasePath(): DatabasePathContext | undefined;
    useModuleStatement(): UseModuleStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class UseModuleStatementContext extends ParserRuleContext {
    KW_USE(): TerminalNode;
    KW_MODULES(): TerminalNode;
    uid(): UidContext[];
    uid(i: number): UidContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ShowStatememtContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_CATALOGS(): TerminalNode | undefined;
    KW_DATABASES(): TerminalNode | undefined;
    KW_VIEWS(): TerminalNode | undefined;
    KW_JARS(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_CATALOG(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    KW_TABLES(): TerminalNode | undefined;
    databasePath(): DatabasePathContext | undefined;
    likePredicate(): LikePredicateContext | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    viewPath(): ViewPathContext | undefined;
    tablePath(): TablePathContext | undefined;
    KW_CREATE(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    KW_VIEW(): TerminalNode | undefined;
    KW_FUNCTIONS(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_MODULES(): TerminalNode | undefined;
    KW_FULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LoadStatementContext extends ParserRuleContext {
    KW_LOAD(): TerminalNode;
    KW_MODULE(): TerminalNode;
    uid(): UidContext;
    KW_WITH(): TerminalNode | undefined;
    tablePropertyList(): TablePropertyListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class UnloadStatememtContext extends ParserRuleContext {
    KW_UNLOAD(): TerminalNode;
    KW_MODULE(): TerminalNode;
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SetStatememtContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    tableProperty(): TablePropertyContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ResetStatememtContext extends ParserRuleContext {
    KW_RESET(): TerminalNode;
    tablePropertyKey(): TablePropertyKeyContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class JarStatememtContext extends ParserRuleContext {
    KW_JAR(): TerminalNode;
    jarFileName(): JarFileNameContext;
    KW_ADD(): TerminalNode | undefined;
    KW_REMOVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DtAddStatementContext extends ParserRuleContext {
    KW_ADD(): TerminalNode;
    KW_JAR(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    dtFilePath(): DtFilePathContext;
    KW_AS(): TerminalNode | undefined;
    uid(): UidContext[];
    uid(i: number): UidContext;
    KW_FILE(): TerminalNode | undefined;
    KW_RENAME(): TerminalNode | undefined;
    KW_PYTHON_FILES(): TerminalNode | undefined;
    KW_PYTHON_REQUIREMENTS(): TerminalNode | undefined;
    KW_PYTHON_DEPENDENCIES(): TerminalNode | undefined;
    KW_PYTHON_JAR(): TerminalNode | undefined;
    KW_PYTHON_ARCHIVES(): TerminalNode | undefined;
    KW_PYTHON_PARAMETER(): TerminalNode | undefined;
    KW_ENGINE(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    KW_CONFIG(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DtFilePathContext extends ParserRuleContext {
    uid(): UidContext[];
    uid(i: number): UidContext;
    SLASH_SIGN(): TerminalNode[];
    SLASH_SIGN(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class CreateTableContext extends ParserRuleContext {
    simpleCreateTable(): SimpleCreateTableContext | undefined;
    createTableAsSelect(): CreateTableAsSelectContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SimpleCreateTableContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tablePathCreate(): TablePathCreateContext;
    LR_BRACKET(): TerminalNode;
    columnOptionDefinition(): ColumnOptionDefinitionContext[];
    columnOptionDefinition(i: number): ColumnOptionDefinitionContext;
    RR_BRACKET(): TerminalNode;
    withOption(): WithOptionContext;
    KW_TEMPORARY(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    watermarkDefinition(): WatermarkDefinitionContext | undefined;
    tableConstraint(): TableConstraintContext | undefined;
    selfDefinitionClause(): SelfDefinitionClauseContext | undefined;
    commentSpec(): CommentSpecContext | undefined;
    partitionDefinition(): PartitionDefinitionContext | undefined;
    likeDefinition(): LikeDefinitionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class CreateTableAsSelectContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tablePathCreate(): TablePathCreateContext;
    withOption(): WithOptionContext;
    ifNotExists(): IfNotExistsContext | undefined;
    KW_AS(): TerminalNode | undefined;
    queryStatement(): QueryStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ColumnOptionDefinitionContext extends ParserRuleContext {
    physicalColumnDefinition(): PhysicalColumnDefinitionContext | undefined;
    metadataColumnDefinition(): MetadataColumnDefinitionContext | undefined;
    computedColumnDefinition(): ComputedColumnDefinitionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class PhysicalColumnDefinitionContext extends ParserRuleContext {
    columnNameCreate(): ColumnNameCreateContext;
    columnType(): ColumnTypeContext;
    columnConstraint(): ColumnConstraintContext | undefined;
    commentSpec(): CommentSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameCreateContext extends ParserRuleContext {
    uid(): UidContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameContext extends ParserRuleContext {
    uid(): UidContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameListContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    columnName(): ColumnNameContext[];
    columnName(i: number): ColumnNameContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ColumnTypeContext extends ParserRuleContext {
    _typeName: Token;
    _type: Token;
    KW_DATE(): TerminalNode | undefined;
    KW_BOOLEAN(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_CHAR(): TerminalNode | undefined;
    KW_VARCHAR(): TerminalNode | undefined;
    KW_STRING(): TerminalNode | undefined;
    KW_BINARY(): TerminalNode | undefined;
    KW_VARBINARY(): TerminalNode | undefined;
    KW_BYTES(): TerminalNode | undefined;
    KW_TINYINT(): TerminalNode | undefined;
    KW_SMALLINT(): TerminalNode | undefined;
    KW_INT(): TerminalNode | undefined;
    KW_INTEGER(): TerminalNode | undefined;
    KW_BIGINT(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_TIMESTAMP_LTZ(): TerminalNode | undefined;
    KW_DATETIME(): TerminalNode | undefined;
    lengthOneDimension(): LengthOneDimensionContext | undefined;
    KW_TIMESTAMP(): TerminalNode | undefined;
    KW_ZONE(): TerminalNode | undefined;
    KW_WITHOUT(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    KW_DECIMAL(): TerminalNode | undefined;
    KW_DEC(): TerminalNode | undefined;
    KW_NUMERIC(): TerminalNode | undefined;
    KW_FLOAT(): TerminalNode | undefined;
    KW_DOUBLE(): TerminalNode | undefined;
    lengthTwoOptionalDimension(): LengthTwoOptionalDimensionContext | undefined;
    KW_ARRAY(): TerminalNode | undefined;
    KW_MULTISET(): TerminalNode | undefined;
    lengthOneTypeDimension(): LengthOneTypeDimensionContext | undefined;
    KW_MAP(): TerminalNode | undefined;
    mapTypeDimension(): MapTypeDimensionContext | undefined;
    KW_ROW(): TerminalNode | undefined;
    rowTypeDimension(): RowTypeDimensionContext | undefined;
    KW_RAW(): TerminalNode | undefined;
    lengthTwoStringDimension(): LengthTwoStringDimensionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LengthOneDimensionContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext;
    RR_BRACKET(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LengthTwoOptionalDimensionContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    decimalLiteral(): DecimalLiteralContext[];
    decimalLiteral(i: number): DecimalLiteralContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LengthTwoStringDimensionContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    stringLiteral(): StringLiteralContext[];
    stringLiteral(i: number): StringLiteralContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LengthOneTypeDimensionContext extends ParserRuleContext {
    LESS_SYMBOL(): TerminalNode;
    columnType(): ColumnTypeContext;
    GREATER_SYMBOL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class MapTypeDimensionContext extends ParserRuleContext {
    LESS_SYMBOL(): TerminalNode;
    columnType(): ColumnTypeContext[];
    columnType(i: number): ColumnTypeContext;
    GREATER_SYMBOL(): TerminalNode;
    COMMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class RowTypeDimensionContext extends ParserRuleContext {
    LESS_SYMBOL(): TerminalNode;
    columnName(): ColumnNameContext[];
    columnName(i: number): ColumnNameContext;
    columnType(): ColumnTypeContext[];
    columnType(i: number): ColumnTypeContext;
    GREATER_SYMBOL(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ColumnConstraintContext extends ParserRuleContext {
    KW_PRIMARY(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    KW_CONSTRAINT(): TerminalNode | undefined;
    constraintName(): ConstraintNameContext | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_ENFORCED(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class CommentSpecContext extends ParserRuleContext {
    KW_COMMENT(): TerminalNode;
    STRING_LITERAL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class MetadataColumnDefinitionContext extends ParserRuleContext {
    columnNameCreate(): ColumnNameCreateContext;
    columnType(): ColumnTypeContext;
    KW_METADATA(): TerminalNode;
    KW_FROM(): TerminalNode | undefined;
    metadataKey(): MetadataKeyContext | undefined;
    KW_VIRTUAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class MetadataKeyContext extends ParserRuleContext {
    STRING_LITERAL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ComputedColumnDefinitionContext extends ParserRuleContext {
    columnNameCreate(): ColumnNameCreateContext;
    KW_AS(): TerminalNode;
    computedColumnExpression(): ComputedColumnExpressionContext;
    commentSpec(): CommentSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ComputedColumnExpressionContext extends ParserRuleContext {
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WatermarkDefinitionContext extends ParserRuleContext {
    KW_WATERMARK(): TerminalNode;
    KW_FOR(): TerminalNode;
    columnName(): ColumnNameContext;
    KW_AS(): TerminalNode;
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TableConstraintContext extends ParserRuleContext {
    KW_PRIMARY(): TerminalNode;
    KW_KEY(): TerminalNode;
    columnNameList(): ColumnNameListContext;
    KW_NOT(): TerminalNode;
    KW_ENFORCED(): TerminalNode;
    KW_CONSTRAINT(): TerminalNode | undefined;
    constraintName(): ConstraintNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ConstraintNameContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SelfDefinitionClauseContext extends ParserRuleContext {
    KW_PERIOD(): TerminalNode;
    KW_FOR(): TerminalNode;
    KW_SYSTEM_TIME(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class PartitionDefinitionContext extends ParserRuleContext {
    KW_PARTITIONED(): TerminalNode;
    KW_BY(): TerminalNode;
    transformList(): TransformListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TransformListContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    transform(): TransformContext[];
    transform(i: number): TransformContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TransformContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: TransformContext): void;
}
export declare class IdentityTransformContext extends TransformContext {
    columnName(): ColumnNameContext;
    constructor(ctx: TransformContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ColumnTransformContext extends TransformContext {
    qualifiedName(): QualifiedNameContext;
    constructor(ctx: TransformContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ApplyTransformContext extends TransformContext {
    LR_BRACKET(): TerminalNode;
    transformArgument(): TransformArgumentContext[];
    transformArgument(i: number): TransformArgumentContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: TransformContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TransformArgumentContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext | undefined;
    constant(): ConstantContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LikeDefinitionContext extends ParserRuleContext {
    KW_LIKE(): TerminalNode;
    tablePath(): TablePathContext;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    likeOption(): LikeOptionContext[];
    likeOption(i: number): LikeOptionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LikeOptionContext extends ParserRuleContext {
    KW_INCLUDING(): TerminalNode | undefined;
    KW_EXCLUDING(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_CONSTRAINTS(): TerminalNode | undefined;
    KW_PARTITIONS(): TerminalNode | undefined;
    KW_OVERWRITING(): TerminalNode | undefined;
    KW_GENERATED(): TerminalNode | undefined;
    KW_OPTIONS(): TerminalNode | undefined;
    KW_WATERMARKS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class CreateCatalogContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_CATALOG(): TerminalNode;
    catalogPathCreate(): CatalogPathCreateContext;
    withOption(): WithOptionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class CreateDatabaseContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_DATABASE(): TerminalNode;
    databasePathCreate(): DatabasePathCreateContext;
    withOption(): WithOptionContext;
    ifNotExists(): IfNotExistsContext | undefined;
    commentSpec(): CommentSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class CreateViewContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewPathCreate(): ViewPathCreateContext;
    KW_AS(): TerminalNode;
    queryStatement(): QueryStatementContext;
    KW_TEMPORARY(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    columnNameList(): ColumnNameListContext | undefined;
    commentSpec(): CommentSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class CreateFunctionContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    functionNameCreate(): FunctionNameCreateContext;
    KW_AS(): TerminalNode;
    identifier(): IdentifierContext;
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_SYSTEM(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    KW_LANGUAGE(): TerminalNode | undefined;
    usingClause(): UsingClauseContext | undefined;
    KW_JAVA(): TerminalNode | undefined;
    KW_SCALA(): TerminalNode | undefined;
    KW_PYTHON(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class UsingClauseContext extends ParserRuleContext {
    KW_USING(): TerminalNode;
    KW_JAR(): TerminalNode[];
    KW_JAR(i: number): TerminalNode;
    jarFileName(): JarFileNameContext[];
    jarFileName(i: number): JarFileNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class JarFileNameContext extends ParserRuleContext {
    STRING_LITERAL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class AlterTableContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tablePath(): TablePathContext;
    renameDefinition(): RenameDefinitionContext | undefined;
    setKeyValueDefinition(): SetKeyValueDefinitionContext | undefined;
    addConstraint(): AddConstraintContext | undefined;
    dropConstraint(): DropConstraintContext | undefined;
    addUnique(): AddUniqueContext | undefined;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class RenameDefinitionContext extends ParserRuleContext {
    KW_RENAME(): TerminalNode;
    KW_TO(): TerminalNode;
    uid(): UidContext[];
    uid(i: number): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SetKeyValueDefinitionContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    tablePropertyList(): TablePropertyListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class AddConstraintContext extends ParserRuleContext {
    KW_ADD(): TerminalNode;
    KW_CONSTRAINT(): TerminalNode;
    constraintName(): ConstraintNameContext;
    KW_PRIMARY(): TerminalNode;
    KW_KEY(): TerminalNode;
    columnNameList(): ColumnNameListContext;
    notForced(): NotForcedContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DropConstraintContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_CONSTRAINT(): TerminalNode;
    constraintName(): ConstraintNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class AddUniqueContext extends ParserRuleContext {
    KW_ADD(): TerminalNode;
    KW_UNIQUE(): TerminalNode;
    columnNameList(): ColumnNameListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class NotForcedContext extends ParserRuleContext {
    KW_NOT(): TerminalNode;
    KW_ENFORCED(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class AlertViewContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewPath(): ViewPathContext;
    renameDefinition(): RenameDefinitionContext | undefined;
    KW_AS(): TerminalNode | undefined;
    queryStatement(): QueryStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class AlterDatabaseContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_DATABASE(): TerminalNode;
    databasePath(): DatabasePathContext;
    setKeyValueDefinition(): SetKeyValueDefinitionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class AlterFunctionContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    functionName(): FunctionNameContext;
    KW_AS(): TerminalNode;
    identifier(): IdentifierContext;
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_SYSTEM(): TerminalNode | undefined;
    ifExists(): IfExistsContext | undefined;
    KW_LANGUAGE(): TerminalNode | undefined;
    KW_JAVA(): TerminalNode | undefined;
    KW_SCALA(): TerminalNode | undefined;
    KW_PYTHON(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DropCatalogContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_CATALOG(): TerminalNode;
    catalogPath(): CatalogPathContext;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DropTableContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tablePath(): TablePathContext;
    KW_TEMPORARY(): TerminalNode | undefined;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DropDatabaseContext extends ParserRuleContext {
    _dropType: Token;
    KW_DROP(): TerminalNode;
    KW_DATABASE(): TerminalNode;
    databasePath(): DatabasePathContext;
    ifExists(): IfExistsContext | undefined;
    KW_RESTRICT(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DropViewContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewPath(): ViewPathContext;
    KW_TEMPORARY(): TerminalNode | undefined;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DropFunctionContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    functionName(): FunctionNameContext;
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_SYSTEM(): TerminalNode | undefined;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class InsertStatementContext extends ParserRuleContext {
    insertSimpleStatement(): InsertSimpleStatementContext | undefined;
    KW_EXECUTE(): TerminalNode | undefined;
    insertMulStatementCompatibility(): InsertMulStatementCompatibilityContext | undefined;
    insertMulStatement(): InsertMulStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class InsertSimpleStatementContext extends ParserRuleContext {
    KW_INSERT(): TerminalNode;
    tablePath(): TablePathContext;
    KW_INTO(): TerminalNode | undefined;
    KW_OVERWRITE(): TerminalNode | undefined;
    queryStatement(): QueryStatementContext | undefined;
    valuesDefinition(): ValuesDefinitionContext | undefined;
    insertPartitionDefinition(): InsertPartitionDefinitionContext | undefined;
    columnNameList(): ColumnNameListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class InsertPartitionDefinitionContext extends ParserRuleContext {
    KW_PARTITION(): TerminalNode;
    tablePropertyList(): TablePropertyListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ValuesDefinitionContext extends ParserRuleContext {
    KW_VALUES(): TerminalNode;
    valuesRowDefinition(): ValuesRowDefinitionContext[];
    valuesRowDefinition(i: number): ValuesRowDefinitionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ValuesRowDefinitionContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    constant(): ConstantContext[];
    constant(i: number): ConstantContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class InsertMulStatementCompatibilityContext extends ParserRuleContext {
    KW_BEGIN(): TerminalNode;
    KW_STATEMENT(): TerminalNode;
    KW_SET(): TerminalNode;
    SEMICOLON(): TerminalNode[];
    SEMICOLON(i: number): TerminalNode;
    KW_END(): TerminalNode;
    insertSimpleStatement(): InsertSimpleStatementContext[];
    insertSimpleStatement(i: number): InsertSimpleStatementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class InsertMulStatementContext extends ParserRuleContext {
    KW_STATEMENT(): TerminalNode;
    KW_SET(): TerminalNode;
    KW_BEGIN(): TerminalNode;
    KW_END(): TerminalNode;
    insertSimpleStatement(): InsertSimpleStatementContext[];
    insertSimpleStatement(i: number): InsertSimpleStatementContext;
    SEMICOLON(): TerminalNode[];
    SEMICOLON(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class QueryStatementContext extends ParserRuleContext {
    _left: QueryStatementContext;
    _operator: Token;
    _right: QueryStatementContext;
    valuesCaluse(): ValuesCaluseContext | undefined;
    withClause(): WithClauseContext | undefined;
    queryStatement(): QueryStatementContext[];
    queryStatement(i: number): QueryStatementContext;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    KW_INTERSECT(): TerminalNode | undefined;
    KW_UNION(): TerminalNode | undefined;
    KW_EXCEPT(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    orderByCaluse(): OrderByCaluseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    selectClause(): SelectClauseContext | undefined;
    selectStatement(): SelectStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ValuesCaluseContext extends ParserRuleContext {
    KW_VALUES(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WithClauseContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    withItem(): WithItemContext[];
    withItem(i: number): WithItemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WithItemContext extends ParserRuleContext {
    withItemName(): WithItemNameContext;
    KW_AS(): TerminalNode;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    queryStatement(): QueryStatementContext;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    columnName(): ColumnNameContext[];
    columnName(i: number): ColumnNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WithItemNameContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SelectStatementContext extends ParserRuleContext {
    selectClause(): SelectClauseContext;
    fromClause(): FromClauseContext | undefined;
    whereClause(): WhereClauseContext | undefined;
    groupByClause(): GroupByClauseContext | undefined;
    havingClause(): HavingClauseContext | undefined;
    windowClause(): WindowClauseContext | undefined;
    matchRecognizeClause(): MatchRecognizeClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SelectClauseContext extends ParserRuleContext {
    KW_SELECT(): TerminalNode;
    ASTERISK_SIGN(): TerminalNode | undefined;
    projectItemDefinition(): ProjectItemDefinitionContext[];
    projectItemDefinition(i: number): ProjectItemDefinitionContext;
    setQuantifier(): SetQuantifierContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ProjectItemDefinitionContext extends ParserRuleContext {
    overWindowItem(): OverWindowItemContext | undefined;
    columnName(): ColumnNameContext | undefined;
    expression(): ExpressionContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class OverWindowItemContext extends ParserRuleContext {
    primaryExpression(): PrimaryExpressionContext;
    KW_OVER(): TerminalNode;
    windowSpec(): WindowSpecContext | undefined;
    KW_AS(): TerminalNode;
    identifier(): IdentifierContext;
    errorCapturingIdentifier(): ErrorCapturingIdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class FromClauseContext extends ParserRuleContext {
    KW_FROM(): TerminalNode;
    tableExpression(): TableExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TableExpressionContext extends ParserRuleContext {
    tableReference(): TableReferenceContext[];
    tableReference(i: number): TableReferenceContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    tableExpression(): TableExpressionContext[];
    tableExpression(i: number): TableExpressionContext;
    KW_JOIN(): TerminalNode | undefined;
    KW_NATURAL(): TerminalNode | undefined;
    KW_OUTER(): TerminalNode | undefined;
    joinCondition(): JoinConditionContext | undefined;
    KW_LEFT(): TerminalNode | undefined;
    KW_RIGHT(): TerminalNode | undefined;
    KW_FULL(): TerminalNode | undefined;
    KW_INNER(): TerminalNode | undefined;
    KW_CROSS(): TerminalNode | undefined;
    inlineDataValueClause(): InlineDataValueClauseContext | undefined;
    windoTVFClause(): WindoTVFClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TableReferenceContext extends ParserRuleContext {
    tablePrimary(): TablePrimaryContext;
    tableAlias(): TableAliasContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TablePrimaryContext extends ParserRuleContext {
    tablePath(): TablePathContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    systemTimePeriod(): SystemTimePeriodContext | undefined;
    correlationName(): CorrelationNameContext | undefined;
    KW_AS(): TerminalNode | undefined;
    viewPath(): ViewPathContext | undefined;
    KW_LATERAL(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode[];
    LR_BRACKET(i: number): TerminalNode;
    functionName(): FunctionNameContext | undefined;
    functionParam(): FunctionParamContext[];
    functionParam(i: number): FunctionParamContext;
    RR_BRACKET(): TerminalNode[];
    RR_BRACKET(i: number): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    queryStatement(): QueryStatementContext | undefined;
    KW_UNNEST(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SystemTimePeriodContext extends ParserRuleContext {
    KW_FOR(): TerminalNode;
    KW_SYSTEM_TIME(): TerminalNode;
    KW_AS(): TerminalNode;
    KW_OF(): TerminalNode;
    dateTimeExpression(): DateTimeExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DateTimeExpressionContext extends ParserRuleContext {
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class InlineDataValueClauseContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    valuesDefinition(): ValuesDefinitionContext;
    RR_BRACKET(): TerminalNode;
    tableAlias(): TableAliasContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WindoTVFClauseContext extends ParserRuleContext {
    KW_TABLE(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    windowTVFExression(): WindowTVFExressionContext;
    RR_BRACKET(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WindowTVFExressionContext extends ParserRuleContext {
    windoTVFName(): WindoTVFNameContext;
    LR_BRACKET(): TerminalNode;
    windowTVFParam(): WindowTVFParamContext[];
    windowTVFParam(i: number): WindowTVFParamContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WindoTVFNameContext extends ParserRuleContext {
    KW_TUMBLE(): TerminalNode | undefined;
    KW_HOP(): TerminalNode | undefined;
    KW_CUMULATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WindowTVFParamContext extends ParserRuleContext {
    KW_TABLE(): TerminalNode | undefined;
    timeAttrColumn(): TimeAttrColumnContext | undefined;
    columnDescriptor(): ColumnDescriptorContext | undefined;
    timeIntervalExpression(): TimeIntervalExpressionContext | undefined;
    KW_DATA(): TerminalNode | undefined;
    DOUBLE_RIGHT_ARROW(): TerminalNode | undefined;
    KW_TIMECOL(): TerminalNode | undefined;
    timeIntervalParamName(): TimeIntervalParamNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TimeIntervalParamNameContext extends ParserRuleContext {
    KW_DATA(): TerminalNode | undefined;
    KW_TIMECOL(): TerminalNode | undefined;
    KW_SIZE(): TerminalNode | undefined;
    KW_OFFSET(): TerminalNode | undefined;
    KW_STEP(): TerminalNode | undefined;
    KW_SLIDE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ColumnDescriptorContext extends ParserRuleContext {
    KW_DESCRIPTOR(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    columnName(): ColumnNameContext;
    RR_BRACKET(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class JoinConditionContext extends ParserRuleContext {
    KW_ON(): TerminalNode | undefined;
    booleanExpression(): BooleanExpressionContext | undefined;
    KW_USING(): TerminalNode | undefined;
    columnNameList(): ColumnNameListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WhereClauseContext extends ParserRuleContext {
    KW_WHERE(): TerminalNode;
    booleanExpression(): BooleanExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class GroupByClauseContext extends ParserRuleContext {
    KW_GROUP(): TerminalNode;
    KW_BY(): TerminalNode;
    groupItemDefinition(): GroupItemDefinitionContext[];
    groupItemDefinition(i: number): GroupItemDefinitionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class GroupItemDefinitionContext extends ParserRuleContext {
    columnName(): ColumnNameContext | undefined;
    groupWindowFunction(): GroupWindowFunctionContext | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    groupingSetsNotaionName(): GroupingSetsNotaionNameContext | undefined;
    groupingSets(): GroupingSetsContext | undefined;
    groupItemDefinition(): GroupItemDefinitionContext[];
    groupItemDefinition(i: number): GroupItemDefinitionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class GroupingSetsContext extends ParserRuleContext {
    KW_GROUPING(): TerminalNode;
    KW_SETS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class GroupingSetsNotaionNameContext extends ParserRuleContext {
    KW_CUBE(): TerminalNode | undefined;
    KW_ROLLUP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class GroupWindowFunctionContext extends ParserRuleContext {
    groupWindowFunctionName(): GroupWindowFunctionNameContext;
    LR_BRACKET(): TerminalNode;
    timeAttrColumn(): TimeAttrColumnContext;
    COMMA(): TerminalNode;
    timeIntervalExpression(): TimeIntervalExpressionContext;
    RR_BRACKET(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class GroupWindowFunctionNameContext extends ParserRuleContext {
    KW_TUMBLE(): TerminalNode | undefined;
    KW_HOP(): TerminalNode | undefined;
    KW_SESSION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TimeAttrColumnContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class HavingClauseContext extends ParserRuleContext {
    KW_HAVING(): TerminalNode;
    booleanExpression(): BooleanExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WindowClauseContext extends ParserRuleContext {
    KW_WINDOW(): TerminalNode;
    namedWindow(): NamedWindowContext[];
    namedWindow(i: number): NamedWindowContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class NamedWindowContext extends ParserRuleContext {
    _name: ErrorCapturingIdentifierContext;
    KW_AS(): TerminalNode;
    windowSpec(): WindowSpecContext;
    errorCapturingIdentifier(): ErrorCapturingIdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WindowSpecContext extends ParserRuleContext {
    _name: ErrorCapturingIdentifierContext;
    LR_BRACKET(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    partitionByClause(): PartitionByClauseContext | undefined;
    orderByCaluse(): OrderByCaluseContext | undefined;
    windowFrame(): WindowFrameContext | undefined;
    errorCapturingIdentifier(): ErrorCapturingIdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class MatchRecognizeClauseContext extends ParserRuleContext {
    KW_MATCH_RECOGNIZE(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    patternVariablesDefination(): PatternVariablesDefinationContext;
    RR_BRACKET(): TerminalNode;
    partitionByClause(): PartitionByClauseContext | undefined;
    orderByCaluse(): OrderByCaluseContext | undefined;
    measuresClause(): MeasuresClauseContext | undefined;
    outputMode(): OutputModeContext | undefined;
    afterMatchStrategy(): AfterMatchStrategyContext | undefined;
    patternDefination(): PatternDefinationContext | undefined;
    identifier(): IdentifierContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class OrderByCaluseContext extends ParserRuleContext {
    KW_ORDER(): TerminalNode;
    KW_BY(): TerminalNode;
    orderItemDefition(): OrderItemDefitionContext[];
    orderItemDefition(i: number): OrderItemDefitionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class OrderItemDefitionContext extends ParserRuleContext {
    _ordering: Token;
    _nullOrder: Token;
    columnName(): ColumnNameContext;
    KW_NULLS(): TerminalNode | undefined;
    KW_ASC(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LimitClauseContext extends ParserRuleContext {
    _limit: ExpressionContext;
    KW_LIMIT(): TerminalNode;
    KW_ALL(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class PartitionByClauseContext extends ParserRuleContext {
    KW_PARTITION(): TerminalNode;
    KW_BY(): TerminalNode;
    columnName(): ColumnNameContext[];
    columnName(i: number): ColumnNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class QuantifiersContext extends ParserRuleContext {
    ASTERISK_SIGN(): TerminalNode | undefined;
    ADD_SIGN(): TerminalNode | undefined;
    QUESTION_MARK_SIGN(): TerminalNode | undefined;
    LB_BRACKET(): TerminalNode | undefined;
    DIG_LITERAL(): TerminalNode[];
    DIG_LITERAL(i: number): TerminalNode;
    COMMA(): TerminalNode | undefined;
    RB_BRACKET(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class MeasuresClauseContext extends ParserRuleContext {
    KW_MEASURES(): TerminalNode;
    projectItemDefinition(): ProjectItemDefinitionContext[];
    projectItemDefinition(i: number): ProjectItemDefinitionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class PatternDefinationContext extends ParserRuleContext {
    KW_PATTERN(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    patternVariable(): PatternVariableContext[];
    patternVariable(i: number): PatternVariableContext;
    withinClause(): WithinClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class PatternVariableContext extends ParserRuleContext {
    unquotedIdentifier(): UnquotedIdentifierContext;
    quantifiers(): QuantifiersContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class OutputModeContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    KW_ROWS(): TerminalNode | undefined;
    KW_PER(): TerminalNode;
    KW_MATCH(): TerminalNode;
    KW_ONE(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class AfterMatchStrategyContext extends ParserRuleContext {
    KW_AFTER(): TerminalNode;
    KW_MATCH(): TerminalNode;
    KW_SKIP(): TerminalNode;
    KW_PAST(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    KW_NEXT(): TerminalNode | undefined;
    unquotedIdentifier(): UnquotedIdentifierContext | undefined;
    KW_FIRST(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class PatternVariablesDefinationContext extends ParserRuleContext {
    KW_DEFINE(): TerminalNode;
    projectItemDefinition(): ProjectItemDefinitionContext[];
    projectItemDefinition(i: number): ProjectItemDefinitionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WindowFrameContext extends ParserRuleContext {
    KW_RANGE(): TerminalNode | undefined;
    KW_BETWEEN(): TerminalNode;
    timeIntervalExpression(): TimeIntervalExpressionContext | undefined;
    frameBound(): FrameBoundContext;
    KW_ROWS(): TerminalNode | undefined;
    DIG_LITERAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class FrameBoundContext extends ParserRuleContext {
    KW_PRECEDING(): TerminalNode;
    KW_AND(): TerminalNode;
    KW_CURRENT(): TerminalNode;
    KW_ROW(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WithinClauseContext extends ParserRuleContext {
    KW_WITHIN(): TerminalNode;
    timeIntervalExpression(): TimeIntervalExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ExpressionContext extends ParserRuleContext {
    booleanExpression(): BooleanExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class BooleanExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: BooleanExpressionContext): void;
}
export declare class LogicalNotContext extends BooleanExpressionContext {
    KW_NOT(): TerminalNode;
    booleanExpression(): BooleanExpressionContext;
    constructor(ctx: BooleanExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ExistsContext extends BooleanExpressionContext {
    KW_EXISTS(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    queryStatement(): QueryStatementContext;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: BooleanExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class PredicatedContext extends BooleanExpressionContext {
    valueExpression(): ValueExpressionContext;
    predicate(): PredicateContext | undefined;
    constructor(ctx: BooleanExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LogicalBinaryContext extends BooleanExpressionContext {
    _left: BooleanExpressionContext;
    _operator: Token;
    _right: BooleanExpressionContext;
    booleanExpression(): BooleanExpressionContext[];
    booleanExpression(i: number): BooleanExpressionContext;
    KW_AND(): TerminalNode | undefined;
    KW_OR(): TerminalNode | undefined;
    constructor(ctx: BooleanExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LogicalNestedContext extends BooleanExpressionContext {
    _kind: Token;
    booleanExpression(): BooleanExpressionContext;
    KW_IS(): TerminalNode;
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    KW_UNKNOWN(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: BooleanExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class PredicateContext extends ParserRuleContext {
    _kind: Token;
    _lower: ValueExpressionContext;
    _upper: ValueExpressionContext;
    _pattern: ValueExpressionContext;
    _right: ValueExpressionContext;
    KW_AND(): TerminalNode | undefined;
    KW_BETWEEN(): TerminalNode | undefined;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    KW_NOT(): TerminalNode | undefined;
    KW_ASYMMETRIC(): TerminalNode | undefined;
    KW_SYMMETRIC(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    RR_BRACKET(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    queryStatement(): QueryStatementContext | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    KW_RLIKE(): TerminalNode | undefined;
    likePredicate(): LikePredicateContext | undefined;
    KW_IS(): TerminalNode | undefined;
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    KW_UNKNOWN(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    KW_SIMILAR(): TerminalNode | undefined;
    KW_ESCAPE(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LikePredicateContext extends ParserRuleContext {
    _kind: Token;
    _quantifier: Token;
    _pattern: ValueExpressionContext;
    KW_LIKE(): TerminalNode;
    KW_ANY(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    LR_BRACKET(): TerminalNode | undefined;
    RR_BRACKET(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_NOT(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    valueExpression(): ValueExpressionContext | undefined;
    KW_ESCAPE(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ValueExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ValueExpressionContext): void;
}
export declare class ValueExpressionDefaultContext extends ValueExpressionContext {
    primaryExpression(): PrimaryExpressionContext;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ArithmeticUnaryContext extends ValueExpressionContext {
    _operator: Token;
    valueExpression(): ValueExpressionContext;
    HYPNEN_SIGN(): TerminalNode | undefined;
    ADD_SIGN(): TerminalNode | undefined;
    BIT_NOT_OP(): TerminalNode | undefined;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ArithmeticBinaryContext extends ValueExpressionContext {
    _left: ValueExpressionContext;
    _operator: Token;
    _right: ValueExpressionContext;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    ASTERISK_SIGN(): TerminalNode | undefined;
    SLASH_SIGN(): TerminalNode | undefined;
    PENCENT_SIGN(): TerminalNode | undefined;
    KW_DIV(): TerminalNode | undefined;
    ADD_SIGN(): TerminalNode | undefined;
    HYPNEN_SIGN(): TerminalNode | undefined;
    DOUBLE_VERTICAL_SIGN(): TerminalNode | undefined;
    BIT_AND_OP(): TerminalNode | undefined;
    BIT_XOR_OP(): TerminalNode | undefined;
    BIT_OR_OP(): TerminalNode | undefined;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ComparisonContext extends ValueExpressionContext {
    _left: ValueExpressionContext;
    _right: ValueExpressionContext;
    comparisonOperator(): ComparisonOperatorContext;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class PrimaryExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PrimaryExpressionContext): void;
}
export declare class SearchedCaseContext extends PrimaryExpressionContext {
    _elseExpression: ExpressionContext;
    KW_CASE(): TerminalNode;
    KW_END(): TerminalNode;
    whenClause(): WhenClauseContext[];
    whenClause(i: number): WhenClauseContext;
    KW_ELSE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SimpleCaseContext extends PrimaryExpressionContext {
    _value: ExpressionContext;
    _elseExpression: ExpressionContext;
    KW_CASE(): TerminalNode;
    KW_END(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    whenClause(): WhenClauseContext[];
    whenClause(i: number): WhenClauseContext;
    KW_ELSE(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class CastContext extends PrimaryExpressionContext {
    KW_CAST(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext;
    KW_AS(): TerminalNode;
    columnType(): ColumnTypeContext;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class FirstContext extends PrimaryExpressionContext {
    KW_FIRST(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext;
    RR_BRACKET(): TerminalNode;
    KW_IGNORE(): TerminalNode | undefined;
    KW_NULLS(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LastContext extends PrimaryExpressionContext {
    KW_LAST(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext;
    RR_BRACKET(): TerminalNode;
    KW_IGNORE(): TerminalNode | undefined;
    KW_NULLS(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class PositionContext extends PrimaryExpressionContext {
    _substr: ValueExpressionContext;
    _str: ValueExpressionContext;
    KW_POSITION(): TerminalNode;
    LR_BRACKET(): TerminalNode;
    KW_IN(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ConstantDefaultContext extends PrimaryExpressionContext {
    constant(): ConstantContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class StarContext extends PrimaryExpressionContext {
    ASTERISK_SIGN(): TerminalNode;
    uid(): UidContext | undefined;
    DOT(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SubqueryExpressionContext extends PrimaryExpressionContext {
    LR_BRACKET(): TerminalNode;
    queryStatement(): QueryStatementContext;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class FunctionCallContext extends PrimaryExpressionContext {
    functionName(): FunctionNameContext;
    LR_BRACKET(): TerminalNode;
    RR_BRACKET(): TerminalNode;
    functionParam(): FunctionParamContext[];
    functionParam(i: number): FunctionParamContext;
    setQuantifier(): SetQuantifierContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SubscriptContext extends PrimaryExpressionContext {
    _value: PrimaryExpressionContext;
    _index: ValueExpressionContext;
    LS_BRACKET(): TerminalNode;
    RS_BRACKET(): TerminalNode;
    primaryExpression(): PrimaryExpressionContext;
    valueExpression(): ValueExpressionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ColumnReferenceContext extends PrimaryExpressionContext {
    identifier(): IdentifierContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DereferenceContext extends PrimaryExpressionContext {
    dereferenceDefinition(): DereferenceDefinitionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ParenthesizedExpressionContext extends PrimaryExpressionContext {
    LR_BRACKET(): TerminalNode;
    expression(): ExpressionContext;
    RR_BRACKET(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DateFunctionExpressionContext extends PrimaryExpressionContext {
    KW_CURRENT_TIMESTAMP(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class FunctionNameCreateContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class FunctionNameContext extends ParserRuleContext {
    reservedKeywordsUsedAsFuncName(): ReservedKeywordsUsedAsFuncNameContext | undefined;
    uid(): UidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class FunctionParamContext extends ParserRuleContext {
    reservedKeywordsUsedAsFuncParam(): ReservedKeywordsUsedAsFuncParamContext | undefined;
    timeIntervalUnit(): TimeIntervalUnitContext | undefined;
    timePointUnit(): TimePointUnitContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DereferenceDefinitionContext extends ParserRuleContext {
    uid(): UidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class CorrelationNameContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class QualifiedNameContext extends ParserRuleContext {
    identifier(): IdentifierContext | undefined;
    dereferenceDefinition(): DereferenceDefinitionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TimeIntervalExpressionContext extends ParserRuleContext {
    KW_INTERVAL(): TerminalNode;
    errorCapturingMultiUnitsInterval(): ErrorCapturingMultiUnitsIntervalContext | undefined;
    errorCapturingUnitToUnitInterval(): ErrorCapturingUnitToUnitIntervalContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ErrorCapturingMultiUnitsIntervalContext extends ParserRuleContext {
    multiUnitsInterval(): MultiUnitsIntervalContext;
    unitToUnitInterval(): UnitToUnitIntervalContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class MultiUnitsIntervalContext extends ParserRuleContext {
    intervalValue(): IntervalValueContext[];
    intervalValue(i: number): IntervalValueContext;
    timeIntervalUnit(): TimeIntervalUnitContext[];
    timeIntervalUnit(i: number): TimeIntervalUnitContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ErrorCapturingUnitToUnitIntervalContext extends ParserRuleContext {
    _body: UnitToUnitIntervalContext;
    _error1: MultiUnitsIntervalContext;
    _error2: UnitToUnitIntervalContext;
    unitToUnitInterval(): UnitToUnitIntervalContext[];
    unitToUnitInterval(i: number): UnitToUnitIntervalContext;
    multiUnitsInterval(): MultiUnitsIntervalContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class UnitToUnitIntervalContext extends ParserRuleContext {
    _value: IntervalValueContext;
    _from: TimeIntervalUnitContext;
    _to: TimeIntervalUnitContext;
    KW_TO(): TerminalNode;
    intervalValue(): IntervalValueContext;
    timeIntervalUnit(): TimeIntervalUnitContext[];
    timeIntervalUnit(i: number): TimeIntervalUnitContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class IntervalValueContext extends ParserRuleContext {
    DIG_LITERAL(): TerminalNode | undefined;
    REAL_LITERAL(): TerminalNode | undefined;
    ADD_SIGN(): TerminalNode | undefined;
    HYPNEN_SIGN(): TerminalNode | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TableAliasContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    KW_AS(): TerminalNode | undefined;
    identifierList(): IdentifierListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ErrorCapturingIdentifierContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    errorCapturingIdentifierExtra(): ErrorCapturingIdentifierExtraContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ErrorCapturingIdentifierExtraContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ErrorCapturingIdentifierExtraContext): void;
}
export declare class ErrorIdentContext extends ErrorCapturingIdentifierExtraContext {
    KW_MINUS(): TerminalNode[];
    KW_MINUS(i: number): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(ctx: ErrorCapturingIdentifierExtraContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class RealIdentContext extends ErrorCapturingIdentifierExtraContext {
    constructor(ctx: ErrorCapturingIdentifierExtraContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class IdentifierListContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    identifierSeq(): IdentifierSeqContext;
    RR_BRACKET(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class IdentifierSeqContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class IdentifierContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: IdentifierContext): void;
}
export declare class UnquotedIdentifierAlternativeContext extends IdentifierContext {
    unquotedIdentifier(): UnquotedIdentifierContext;
    constructor(ctx: IdentifierContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class QuotedIdentifierAlternativeContext extends IdentifierContext {
    quotedIdentifier(): QuotedIdentifierContext;
    constructor(ctx: IdentifierContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class NonReservedKeywordsAlternativeContext extends IdentifierContext {
    nonReservedKeywords(): NonReservedKeywordsContext;
    constructor(ctx: IdentifierContext);
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class UnquotedIdentifierContext extends ParserRuleContext {
    DIG_LITERAL(): TerminalNode | undefined;
    ID_LITERAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class QuotedIdentifierContext extends ParserRuleContext {
    STRING_LITERAL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WhenClauseContext extends ParserRuleContext {
    _condition: ExpressionContext;
    _result: ExpressionContext;
    KW_WHEN(): TerminalNode;
    KW_THEN(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class CatalogPathContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class CatalogPathCreateContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DatabasePathContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    DOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DatabasePathCreateContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    DOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TablePathCreateContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TablePathContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ViewPathContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ViewPathCreateContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class UidContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class WithOptionContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    tablePropertyList(): TablePropertyListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class IfNotExistsContext extends ParserRuleContext {
    KW_IF(): TerminalNode;
    KW_NOT(): TerminalNode;
    KW_EXISTS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class IfExistsContext extends ParserRuleContext {
    KW_IF(): TerminalNode;
    KW_EXISTS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TablePropertyListContext extends ParserRuleContext {
    LR_BRACKET(): TerminalNode;
    tableProperty(): TablePropertyContext[];
    tableProperty(i: number): TablePropertyContext;
    RR_BRACKET(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TablePropertyContext extends ParserRuleContext {
    _key: TablePropertyKeyContext;
    _value: TablePropertyValueContext;
    tablePropertyKey(): TablePropertyKeyContext;
    tablePropertyValue(): TablePropertyValueContext | undefined;
    EQUAL_SYMBOL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TablePropertyKeyContext extends ParserRuleContext {
    identifier(): IdentifierContext | undefined;
    dereferenceDefinition(): DereferenceDefinitionContext | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TablePropertyValueContext extends ParserRuleContext {
    DIG_LITERAL(): TerminalNode | undefined;
    REAL_LITERAL(): TerminalNode | undefined;
    booleanLiteral(): BooleanLiteralContext | undefined;
    STRING_LITERAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class LogicalOperatorContext extends ParserRuleContext {
    KW_AND(): TerminalNode | undefined;
    BIT_AND_OP(): TerminalNode[];
    BIT_AND_OP(i: number): TerminalNode;
    KW_OR(): TerminalNode | undefined;
    BIT_OR_OP(): TerminalNode[];
    BIT_OR_OP(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ComparisonOperatorContext extends ParserRuleContext {
    EQUAL_SYMBOL(): TerminalNode | undefined;
    GREATER_SYMBOL(): TerminalNode | undefined;
    LESS_SYMBOL(): TerminalNode | undefined;
    EXCLAMATION_SYMBOL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
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
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class MathOperatorContext extends ParserRuleContext {
    ASTERISK_SIGN(): TerminalNode | undefined;
    SLASH_SIGN(): TerminalNode | undefined;
    PENCENT_SIGN(): TerminalNode | undefined;
    KW_DIV(): TerminalNode | undefined;
    ADD_SIGN(): TerminalNode | undefined;
    HYPNEN_SIGN(): TerminalNode | undefined;
    DOUBLE_HYPNEN_SIGN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class UnaryOperatorContext extends ParserRuleContext {
    EXCLAMATION_SYMBOL(): TerminalNode | undefined;
    BIT_NOT_OP(): TerminalNode | undefined;
    ADD_SIGN(): TerminalNode | undefined;
    HYPNEN_SIGN(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ConstantContext extends ParserRuleContext {
    timeIntervalExpression(): TimeIntervalExpressionContext | undefined;
    timePointLiteral(): TimePointLiteralContext | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    decimalLiteral(): DecimalLiteralContext | undefined;
    HYPNEN_SIGN(): TerminalNode | undefined;
    booleanLiteral(): BooleanLiteralContext | undefined;
    REAL_LITERAL(): TerminalNode | undefined;
    BIT_STRING(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TimePointLiteralContext extends ParserRuleContext {
    timePointUnit(): TimePointUnitContext;
    stringLiteral(): StringLiteralContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class StringLiteralContext extends ParserRuleContext {
    STRING_LITERAL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class DecimalLiteralContext extends ParserRuleContext {
    DIG_LITERAL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class BooleanLiteralContext extends ParserRuleContext {
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class SetQuantifierContext extends ParserRuleContext {
    KW_DISTINCT(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TimePointUnitContext extends ParserRuleContext {
    KW_YEAR(): TerminalNode | undefined;
    KW_QUARTER(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_WEEK(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    KW_MILLISECOND(): TerminalNode | undefined;
    KW_MICROSECOND(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class TimeIntervalUnitContext extends ParserRuleContext {
    KW_MILLENNIUM(): TerminalNode | undefined;
    KW_CENTURY(): TerminalNode | undefined;
    KW_DECADE(): TerminalNode | undefined;
    KW_YEAR(): TerminalNode | undefined;
    KW_YEARS(): TerminalNode | undefined;
    KW_QUARTER(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_MONTHS(): TerminalNode | undefined;
    KW_WEEK(): TerminalNode | undefined;
    KW_WEEKS(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_DAYS(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_HOURS(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    KW_MINUTES(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    KW_SECONDS(): TerminalNode | undefined;
    KW_MILLISECOND(): TerminalNode | undefined;
    KW_MICROSECOND(): TerminalNode | undefined;
    KW_NANOSECOND(): TerminalNode | undefined;
    KW_EPOCH(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ReservedKeywordsUsedAsFuncParamContext extends ParserRuleContext {
    KW_ARRAY(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_BOTH(): TerminalNode | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    KW_LEADING(): TerminalNode | undefined;
    KW_TRAILING(): TerminalNode | undefined;
    KW_VALUE(): TerminalNode | undefined;
    ASTERISK_SIGN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class ReservedKeywordsUsedAsFuncNameContext extends ParserRuleContext {
    KW_ABS(): TerminalNode | undefined;
    KW_ARRAY(): TerminalNode | undefined;
    KW_AVG(): TerminalNode | undefined;
    KW_CARDINALITY(): TerminalNode | undefined;
    KW_CAST(): TerminalNode | undefined;
    KW_CEIL(): TerminalNode | undefined;
    KW_CEILING(): TerminalNode | undefined;
    KW_COALESCE(): TerminalNode | undefined;
    KW_COLLECT(): TerminalNode | undefined;
    KW_COUNT(): TerminalNode | undefined;
    KW_CUME_DIST(): TerminalNode | undefined;
    KW_CURRENT_DATE(): TerminalNode | undefined;
    KW_CURRENT_TIME(): TerminalNode | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    KW_DATE(): TerminalNode | undefined;
    KW_DAYOFWEEK(): TerminalNode | undefined;
    KW_DAYOFYEAR(): TerminalNode | undefined;
    KW_DENSE_RANK(): TerminalNode | undefined;
    KW_ELEMENT(): TerminalNode | undefined;
    KW_EXP(): TerminalNode | undefined;
    KW_EXTRACT(): TerminalNode | undefined;
    KW_FIRST_VALUE(): TerminalNode | undefined;
    KW_FLOOR(): TerminalNode | undefined;
    KW_GROUPING(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_IF(): TerminalNode | undefined;
    KW_LAG(): TerminalNode | undefined;
    KW_LAST_VALUE(): TerminalNode | undefined;
    KW_LEAD(): TerminalNode | undefined;
    KW_LEFT(): TerminalNode | undefined;
    KW_LN(): TerminalNode | undefined;
    KW_LOCALTIME(): TerminalNode | undefined;
    KW_LOCALTIMESTAMP(): TerminalNode | undefined;
    KW_LOWER(): TerminalNode | undefined;
    KW_MAP(): TerminalNode | undefined;
    KW_MAX(): TerminalNode | undefined;
    KW_MIN(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    KW_MOD(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_NULLIF(): TerminalNode | undefined;
    KW_NTILE(): TerminalNode | undefined;
    KW_OVERLAY(): TerminalNode | undefined;
    KW_PERCENT_RANK(): TerminalNode | undefined;
    KW_POSITION(): TerminalNode | undefined;
    KW_POWER(): TerminalNode | undefined;
    KW_QUARTER(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_ROWS(): TerminalNode | undefined;
    KW_ROW_NUMBER(): TerminalNode | undefined;
    KW_RANK(): TerminalNode | undefined;
    KW_RIGHT(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    KW_STDDEV_POP(): TerminalNode | undefined;
    KW_STDDEV_SAMP(): TerminalNode | undefined;
    KW_SUBSTRING(): TerminalNode | undefined;
    KW_SUM(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_TIMESTAMP(): TerminalNode | undefined;
    KW_TIMESTAMP_DIFF(): TerminalNode | undefined;
    KW_TRIM(): TerminalNode | undefined;
    KW_TRUNCATE(): TerminalNode | undefined;
    KW_TRY_CAST(): TerminalNode | undefined;
    KW_UPPER(): TerminalNode | undefined;
    KW_VAR_POP(): TerminalNode | undefined;
    KW_VAR_SAMP(): TerminalNode | undefined;
    KW_WEEK(): TerminalNode | undefined;
    KW_YEAR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
export declare class NonReservedKeywordsContext extends ParserRuleContext {
    KW_ADD(): TerminalNode | undefined;
    KW_AFTER(): TerminalNode | undefined;
    KW_ASC(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    KW_CATALOG(): TerminalNode | undefined;
    KW_CENTURY(): TerminalNode | undefined;
    KW_CONFIG(): TerminalNode | undefined;
    KW_CONSTRAINTS(): TerminalNode | undefined;
    KW_CUMULATE(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    KW_DAYS(): TerminalNode | undefined;
    KW_DECADE(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    KW_DESCRIPTOR(): TerminalNode | undefined;
    KW_DIV(): TerminalNode | undefined;
    KW_ENGINE(): TerminalNode | undefined;
    KW_EPOCH(): TerminalNode | undefined;
    KW_EXCLUDING(): TerminalNode | undefined;
    KW_FILE(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_GENERATED(): TerminalNode | undefined;
    KW_HOP(): TerminalNode | undefined;
    KW_HOURS(): TerminalNode | undefined;
    KW_IGNORE(): TerminalNode | undefined;
    KW_INCLUDING(): TerminalNode | undefined;
    KW_JAR(): TerminalNode | undefined;
    KW_JARS(): TerminalNode | undefined;
    KW_JAVA(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    KW_LOAD(): TerminalNode | undefined;
    KW_MAP(): TerminalNode | undefined;
    KW_MICROSECOND(): TerminalNode | undefined;
    KW_MILLENNIUM(): TerminalNode | undefined;
    KW_MILLISECOND(): TerminalNode | undefined;
    KW_MINUTES(): TerminalNode | undefined;
    KW_MONTHS(): TerminalNode | undefined;
    KW_NANOSECOND(): TerminalNode | undefined;
    KW_NULLS(): TerminalNode | undefined;
    KW_OPTIONS(): TerminalNode | undefined;
    KW_PAST(): TerminalNode | undefined;
    KW_PLAN(): TerminalNode | undefined;
    KW_PRECEDING(): TerminalNode | undefined;
    KW_PYTHON(): TerminalNode | undefined;
    KW_PYTHON_ARCHIVES(): TerminalNode | undefined;
    KW_PYTHON_DEPENDENCIES(): TerminalNode | undefined;
    KW_PYTHON_FILES(): TerminalNode | undefined;
    KW_PYTHON_JAR(): TerminalNode | undefined;
    KW_PYTHON_PARAMETER(): TerminalNode | undefined;
    KW_PYTHON_REQUIREMENTS(): TerminalNode | undefined;
    KW_QUARTER(): TerminalNode | undefined;
    KW_REMOVE(): TerminalNode | undefined;
    KW_RESTRICT(): TerminalNode | undefined;
    KW_SECONDS(): TerminalNode | undefined;
    KW_SESSION(): TerminalNode | undefined;
    KW_SETS(): TerminalNode | undefined;
    KW_SIZE(): TerminalNode | undefined;
    KW_SLIDE(): TerminalNode | undefined;
    KW_STEP(): TerminalNode | undefined;
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_TIMECOL(): TerminalNode | undefined;
    KW_TUMBLE(): TerminalNode | undefined;
    KW_UNLOAD(): TerminalNode | undefined;
    KW_VIEW(): TerminalNode | undefined;
    KW_WEEK(): TerminalNode | undefined;
    KW_YEARS(): TerminalNode | undefined;
    KW_ZONE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: FlinkSqlParserListener): void;
    exitRule(listener: FlinkSqlParserListener): void;
    accept<Result>(visitor: FlinkSqlParserVisitor<Result>): Result;
}
