import { ATN } from "antlr4ts/atn/ATN";
import { FailedPredicateException } from "antlr4ts/FailedPredicateException";
import { Parser } from "antlr4ts/Parser";
import { ParserRuleContext } from "antlr4ts/ParserRuleContext";
import { RuleContext } from "antlr4ts/RuleContext";
import { TerminalNode } from "antlr4ts/tree/TerminalNode";
import { TokenStream } from "antlr4ts/TokenStream";
import { Vocabulary } from "antlr4ts/Vocabulary";
import { PostgreSQLParserListener } from "./PostgreSQLParserListener";
import { PostgreSQLParserVisitor } from "./PostgreSQLParserVisitor";
export declare class PostgreSQLParser extends Parser {
    static readonly Dollar = 1;
    static readonly OPEN_PAREN = 2;
    static readonly CLOSE_PAREN = 3;
    static readonly OPEN_BRACKET = 4;
    static readonly CLOSE_BRACKET = 5;
    static readonly COMMA = 6;
    static readonly SEMI = 7;
    static readonly COLON = 8;
    static readonly STAR = 9;
    static readonly EQUAL = 10;
    static readonly DOT = 11;
    static readonly PLUS = 12;
    static readonly MINUS = 13;
    static readonly SLASH = 14;
    static readonly CARET = 15;
    static readonly LT = 16;
    static readonly GT = 17;
    static readonly LESS_LESS = 18;
    static readonly GREATER_GREATER = 19;
    static readonly COLON_EQUALS = 20;
    static readonly LESS_EQUALS = 21;
    static readonly EQUALS_GREATER = 22;
    static readonly GREATER_EQUALS = 23;
    static readonly DOT_DOT = 24;
    static readonly NOT_EQUALS = 25;
    static readonly TYPECAST = 26;
    static readonly PERCENT = 27;
    static readonly PARAM = 28;
    static readonly Operator = 29;
    static readonly KW_ALL = 30;
    static readonly KW_ANALYSE = 31;
    static readonly KW_ANALYZE = 32;
    static readonly KW_AND = 33;
    static readonly KW_ANY = 34;
    static readonly KW_ARRAY = 35;
    static readonly KW_AS = 36;
    static readonly KW_ASC = 37;
    static readonly KW_ASYMMETRIC = 38;
    static readonly KW_BOTH = 39;
    static readonly KW_CASE = 40;
    static readonly KW_CAST = 41;
    static readonly KW_CHECK = 42;
    static readonly KW_COLLATE = 43;
    static readonly KW_COLUMN = 44;
    static readonly KW_CONSTRAINT = 45;
    static readonly KW_CREATE = 46;
    static readonly KW_CURRENT_CATALOG = 47;
    static readonly KW_CURRENT_DATE = 48;
    static readonly KW_CURRENT_ROLE = 49;
    static readonly KW_CURRENT_TIME = 50;
    static readonly KW_CURRENT_TIMESTAMP = 51;
    static readonly KW_CURRENT_USER = 52;
    static readonly KW_DEFAULT = 53;
    static readonly KW_DEFERRABLE = 54;
    static readonly KW_DESC = 55;
    static readonly KW_DISTINCT = 56;
    static readonly KW_DO = 57;
    static readonly KW_ELSE = 58;
    static readonly KW_EXCEPT = 59;
    static readonly KW_FALSE = 60;
    static readonly KW_FETCH = 61;
    static readonly KW_FOR = 62;
    static readonly KW_FOREIGN = 63;
    static readonly KW_FROM = 64;
    static readonly KW_GRANT = 65;
    static readonly KW_GROUP = 66;
    static readonly KW_HAVING = 67;
    static readonly KW_IN = 68;
    static readonly KW_INITIALLY = 69;
    static readonly KW_INTERSECT = 70;
    static readonly KW_INTO = 71;
    static readonly KW_LATERAL = 72;
    static readonly KW_LEADING = 73;
    static readonly KW_LIMIT = 74;
    static readonly KW_LOCALTIME = 75;
    static readonly KW_LOCALTIMESTAMP = 76;
    static readonly KW_NOT = 77;
    static readonly KW_NULL = 78;
    static readonly KW_OFFSET = 79;
    static readonly KW_ON = 80;
    static readonly KW_ONLY = 81;
    static readonly KW_OR = 82;
    static readonly KW_ORDER = 83;
    static readonly KW_PLACING = 84;
    static readonly KW_PRIMARY = 85;
    static readonly KW_REFERENCES = 86;
    static readonly KW_RETURNING = 87;
    static readonly KW_SELECT = 88;
    static readonly KW_SESSION_USER = 89;
    static readonly KW_SOME = 90;
    static readonly KW_SYMMETRIC = 91;
    static readonly KW_TABLE = 92;
    static readonly KW_THEN = 93;
    static readonly KW_TO = 94;
    static readonly KW_TRAILING = 95;
    static readonly KW_TRUE = 96;
    static readonly KW_UNION = 97;
    static readonly KW_UNIQUE = 98;
    static readonly KW_USER = 99;
    static readonly KW_USING = 100;
    static readonly KW_VARIADIC = 101;
    static readonly KW_WHEN = 102;
    static readonly KW_WHERE = 103;
    static readonly KW_WINDOW = 104;
    static readonly KW_WITH = 105;
    static readonly KW_AUTHORIZATION = 106;
    static readonly KW_BINARY = 107;
    static readonly KW_COLLATION = 108;
    static readonly KW_CONCURRENTLY = 109;
    static readonly KW_CROSS = 110;
    static readonly KW_CURRENT_SCHEMA = 111;
    static readonly KW_FREEZE = 112;
    static readonly KW_FULL = 113;
    static readonly KW_ILIKE = 114;
    static readonly KW_INNER = 115;
    static readonly KW_IS = 116;
    static readonly KW_ISNULL = 117;
    static readonly KW_JOIN = 118;
    static readonly KW_LEFT = 119;
    static readonly KW_LIKE = 120;
    static readonly KW_NATURAL = 121;
    static readonly KW_NOTNULL = 122;
    static readonly KW_OUTER = 123;
    static readonly KW_OVER = 124;
    static readonly KW_OVERLAPS = 125;
    static readonly KW_RIGHT = 126;
    static readonly KW_SIMILAR = 127;
    static readonly KW_VERBOSE = 128;
    static readonly KW_ABORT = 129;
    static readonly KW_ABSOLUTE = 130;
    static readonly KW_ACCESS = 131;
    static readonly KW_ACTION = 132;
    static readonly KW_ADD = 133;
    static readonly KW_ADMIN = 134;
    static readonly KW_AFTER = 135;
    static readonly KW_AGGREGATE = 136;
    static readonly KW_ALSO = 137;
    static readonly KW_ALTER = 138;
    static readonly KW_ALWAYS = 139;
    static readonly KW_ASSERTION = 140;
    static readonly KW_ASSIGNMENT = 141;
    static readonly KW_AT = 142;
    static readonly KW_ATTRIBUTE = 143;
    static readonly KW_BACKWARD = 144;
    static readonly KW_BEFORE = 145;
    static readonly KW_BEGIN = 146;
    static readonly KW_BY = 147;
    static readonly KW_CACHE = 148;
    static readonly KW_CALLED = 149;
    static readonly KW_CASCADE = 150;
    static readonly KW_CASCADED = 151;
    static readonly KW_CATALOG = 152;
    static readonly KW_CHAIN = 153;
    static readonly KW_CHARACTERISTICS = 154;
    static readonly KW_CHECKPOINT = 155;
    static readonly KW_CLASS = 156;
    static readonly KW_CLOSE = 157;
    static readonly KW_CLUSTER = 158;
    static readonly KW_COMMENT = 159;
    static readonly KW_COMMENTS = 160;
    static readonly KW_COMMIT = 161;
    static readonly KW_COMMITTED = 162;
    static readonly KW_CONFIGURATION = 163;
    static readonly KW_CONNECTION = 164;
    static readonly KW_CONSTRAINTS = 165;
    static readonly KW_CONTENT = 166;
    static readonly KW_CONTINUE = 167;
    static readonly KW_CONVERSION = 168;
    static readonly KW_COPY = 169;
    static readonly KW_COST = 170;
    static readonly KW_CSV = 171;
    static readonly KW_CURSOR = 172;
    static readonly KW_CYCLE = 173;
    static readonly KW_DATA = 174;
    static readonly KW_DATABASE = 175;
    static readonly KW_DAY = 176;
    static readonly KW_DEALLOCATE = 177;
    static readonly KW_DECLARE = 178;
    static readonly KW_DEFAULTS = 179;
    static readonly KW_DEFERRED = 180;
    static readonly KW_DEFINER = 181;
    static readonly KW_DELETE = 182;
    static readonly KW_DELIMITER = 183;
    static readonly KW_DELIMITERS = 184;
    static readonly KW_DICTIONARY = 185;
    static readonly KW_DISABLE = 186;
    static readonly KW_DISCARD = 187;
    static readonly KW_DOCUMENT = 188;
    static readonly KW_DOMAIN = 189;
    static readonly KW_DOUBLE = 190;
    static readonly KW_DROP = 191;
    static readonly KW_EACH = 192;
    static readonly KW_ENABLE = 193;
    static readonly KW_ENCODING = 194;
    static readonly KW_ENCRYPTED = 195;
    static readonly KW_ENUM = 196;
    static readonly KW_ESCAPE = 197;
    static readonly KW_EVENT = 198;
    static readonly KW_EXCLUDE = 199;
    static readonly KW_EXCLUDING = 200;
    static readonly KW_EXCLUSIVE = 201;
    static readonly KW_EXECUTE = 202;
    static readonly KW_EXPLAIN = 203;
    static readonly KW_EXTENSION = 204;
    static readonly KW_EXTERNAL = 205;
    static readonly KW_FAMILY = 206;
    static readonly KW_FIRST = 207;
    static readonly KW_FOLLOWING = 208;
    static readonly KW_FORCE = 209;
    static readonly KW_FORWARD = 210;
    static readonly KW_FUNCTION = 211;
    static readonly KW_FUNCTIONS = 212;
    static readonly KW_GLOBAL = 213;
    static readonly KW_GRANTED = 214;
    static readonly KW_HANDLER = 215;
    static readonly KW_HEADER = 216;
    static readonly KW_HOLD = 217;
    static readonly KW_HOUR = 218;
    static readonly KW_IDENTITY = 219;
    static readonly KW_IF = 220;
    static readonly KW_IMMEDIATE = 221;
    static readonly KW_IMMUTABLE = 222;
    static readonly KW_IMPLICIT = 223;
    static readonly KW_INCLUDING = 224;
    static readonly KW_INCREMENT = 225;
    static readonly KW_INDEX = 226;
    static readonly KW_INDEXES = 227;
    static readonly KW_INHERIT = 228;
    static readonly KW_NOINHERIT = 229;
    static readonly KW_SUPERUSER = 230;
    static readonly KW_NOSUPERUSER = 231;
    static readonly KW_CREATEDB = 232;
    static readonly KW_NOCREATEDB = 233;
    static readonly KW_CREATEROLE = 234;
    static readonly KW_NOCREATEROLE = 235;
    static readonly KW_CREATEUSER = 236;
    static readonly KW_NOCREATEUSER = 237;
    static readonly KW_INHERITS = 238;
    static readonly KW_INLINE = 239;
    static readonly KW_INSENSITIVE = 240;
    static readonly KW_INSERT = 241;
    static readonly KW_INSTEAD = 242;
    static readonly KW_INVOKER = 243;
    static readonly KW_ISOLATION = 244;
    static readonly KW_KEY = 245;
    static readonly KW_LABEL = 246;
    static readonly KW_LANGUAGE = 247;
    static readonly KW_LARGE = 248;
    static readonly KW_LAST = 249;
    static readonly KW_LEAKPROOF = 250;
    static readonly KW_LEVEL = 251;
    static readonly KW_LISTEN = 252;
    static readonly KW_LOAD = 253;
    static readonly KW_LOCAL = 254;
    static readonly KW_LOCATION = 255;
    static readonly KW_LOCK = 256;
    static readonly KW_MAPPING = 257;
    static readonly KW_MATCH = 258;
    static readonly KW_MATERIALIZED = 259;
    static readonly KW_MAXVALUE = 260;
    static readonly KW_MINUTE = 261;
    static readonly KW_MINVALUE = 262;
    static readonly KW_MODE = 263;
    static readonly KW_MONTH = 264;
    static readonly KW_MOVE = 265;
    static readonly KW_NAME = 266;
    static readonly KW_NAMES = 267;
    static readonly KW_NEXT = 268;
    static readonly KW_NO = 269;
    static readonly KW_NOTHING = 270;
    static readonly KW_NOTIFY = 271;
    static readonly KW_NOWAIT = 272;
    static readonly KW_NULLS = 273;
    static readonly KW_OBJECT = 274;
    static readonly KW_OF = 275;
    static readonly KW_OFF = 276;
    static readonly KW_OIDS = 277;
    static readonly KW_OPERATOR = 278;
    static readonly KW_OPTION = 279;
    static readonly KW_OPTIONS = 280;
    static readonly KW_OWNED = 281;
    static readonly KW_OWNER = 282;
    static readonly KW_PARSER = 283;
    static readonly KW_PARTIAL = 284;
    static readonly KW_PARTITION = 285;
    static readonly KW_PASSING = 286;
    static readonly KW_PASSWORD = 287;
    static readonly KW_PLANS = 288;
    static readonly KW_PRECEDING = 289;
    static readonly KW_PREPARE = 290;
    static readonly KW_PREPARED = 291;
    static readonly KW_PRESERVE = 292;
    static readonly KW_PRIOR = 293;
    static readonly KW_PRIVILEGES = 294;
    static readonly KW_PROCEDURAL = 295;
    static readonly KW_PROCEDURE = 296;
    static readonly KW_PROGRAM = 297;
    static readonly KW_QUOTE = 298;
    static readonly KW_RANGE = 299;
    static readonly KW_READ = 300;
    static readonly KW_REASSIGN = 301;
    static readonly KW_RECHECK = 302;
    static readonly KW_RECURSIVE = 303;
    static readonly KW_REF = 304;
    static readonly KW_REFRESH = 305;
    static readonly KW_REINDEX = 306;
    static readonly KW_RELATIVE = 307;
    static readonly KW_RELEASE = 308;
    static readonly KW_RENAME = 309;
    static readonly KW_REPEATABLE = 310;
    static readonly KW_REPLACE = 311;
    static readonly KW_REPLICA = 312;
    static readonly KW_RESET = 313;
    static readonly KW_RESTART = 314;
    static readonly KW_RESTRICT = 315;
    static readonly KW_RETURNS = 316;
    static readonly KW_REVOKE = 317;
    static readonly KW_ROLE = 318;
    static readonly KW_ROLLBACK = 319;
    static readonly KW_ROWS = 320;
    static readonly KW_RULE = 321;
    static readonly KW_SAVEPOINT = 322;
    static readonly KW_SCHEMA = 323;
    static readonly KW_SCROLL = 324;
    static readonly KW_SEARCH = 325;
    static readonly KW_SECOND = 326;
    static readonly KW_SECURITY = 327;
    static readonly KW_SEQUENCE = 328;
    static readonly KW_SEQUENCES = 329;
    static readonly KW_SERIALIZABLE = 330;
    static readonly KW_SERVER = 331;
    static readonly KW_SESSION = 332;
    static readonly KW_SET = 333;
    static readonly KW_SHARE = 334;
    static readonly KW_SHOW = 335;
    static readonly KW_SIMPLE = 336;
    static readonly KW_SNAPSHOT = 337;
    static readonly KW_STABLE = 338;
    static readonly KW_STANDALONE = 339;
    static readonly KW_START = 340;
    static readonly KW_STATEMENT = 341;
    static readonly KW_STATISTICS = 342;
    static readonly KW_STDIN = 343;
    static readonly KW_STDOUT = 344;
    static readonly KW_STORAGE = 345;
    static readonly KW_STRICT = 346;
    static readonly KW_STRIP = 347;
    static readonly KW_SYSID = 348;
    static readonly KW_SYSTEM = 349;
    static readonly KW_TABLES = 350;
    static readonly KW_TABLESPACE = 351;
    static readonly KW_TEMP = 352;
    static readonly KW_TEMPLATE = 353;
    static readonly KW_TEMPORARY = 354;
    static readonly KW_TEXT = 355;
    static readonly KW_TRANSACTION = 356;
    static readonly KW_TRIGGER = 357;
    static readonly KW_TRUNCATE = 358;
    static readonly KW_TRUSTED = 359;
    static readonly KW_TYPE = 360;
    static readonly KW_TYPES = 361;
    static readonly KW_UNBOUNDED = 362;
    static readonly KW_UNCOMMITTED = 363;
    static readonly KW_UNENCRYPTED = 364;
    static readonly KW_UNKNOWN = 365;
    static readonly KW_UNLISTEN = 366;
    static readonly KW_UNLOGGED = 367;
    static readonly KW_UNTIL = 368;
    static readonly KW_UPDATE = 369;
    static readonly KW_VACUUM = 370;
    static readonly KW_VALID = 371;
    static readonly KW_VALIDATE = 372;
    static readonly KW_VALIDATOR = 373;
    static readonly KW_VARYING = 374;
    static readonly KW_VERSION = 375;
    static readonly KW_VIEW = 376;
    static readonly KW_VOLATILE = 377;
    static readonly KW_WHITESPACE = 378;
    static readonly KW_WITHOUT = 379;
    static readonly KW_WORK = 380;
    static readonly KW_WRAPPER = 381;
    static readonly KW_WRITE = 382;
    static readonly KW_XML = 383;
    static readonly KW_YEAR = 384;
    static readonly KW_YES = 385;
    static readonly KW_ZONE = 386;
    static readonly KW_BETWEEN = 387;
    static readonly KW_BIGINT = 388;
    static readonly KW_BIT = 389;
    static readonly KW_BOOLEAN = 390;
    static readonly KW_CHAR = 391;
    static readonly KW_CHARACTER = 392;
    static readonly KW_COALESCE = 393;
    static readonly KW_DEC = 394;
    static readonly KW_DECIMAL = 395;
    static readonly KW_EXISTS = 396;
    static readonly KW_EXTRACT = 397;
    static readonly KW_FLOAT = 398;
    static readonly KW_GREATEST = 399;
    static readonly KW_INOUT = 400;
    static readonly KW_INT = 401;
    static readonly KW_INTEGER = 402;
    static readonly KW_INTERVAL = 403;
    static readonly KW_LEAST = 404;
    static readonly KW_NATIONAL = 405;
    static readonly KW_NCHAR = 406;
    static readonly KW_NONE = 407;
    static readonly KW_NULLIF = 408;
    static readonly KW_NUMERIC = 409;
    static readonly KW_OVERLAY = 410;
    static readonly KW_POSITION = 411;
    static readonly KW_PRECISION = 412;
    static readonly KW_REAL = 413;
    static readonly KW_ROW = 414;
    static readonly KW_SETOF = 415;
    static readonly KW_SMALLINT = 416;
    static readonly KW_SUBSTRING = 417;
    static readonly KW_TIME = 418;
    static readonly KW_TIMESTAMP = 419;
    static readonly KW_TREAT = 420;
    static readonly KW_TRIM = 421;
    static readonly KW_VALUES = 422;
    static readonly KW_VARCHAR = 423;
    static readonly KW_XMLATTRIBUTES = 424;
    static readonly KW_XMLCONCAT = 425;
    static readonly KW_XMLELEMENT = 426;
    static readonly KW_XMLEXISTS = 427;
    static readonly KW_XMLFOREST = 428;
    static readonly KW_XMLPARSE = 429;
    static readonly KW_XMLPI = 430;
    static readonly KW_XMLROOT = 431;
    static readonly KW_XMLSERIALIZE = 432;
    static readonly KW_CALL = 433;
    static readonly KW_CURRENT = 434;
    static readonly KW_ATTACH = 435;
    static readonly KW_DETACH = 436;
    static readonly KW_EXPRESSION = 437;
    static readonly KW_GENERATED = 438;
    static readonly KW_LOGGED = 439;
    static readonly KW_STORED = 440;
    static readonly KW_INCLUDE = 441;
    static readonly KW_ROUTINE = 442;
    static readonly KW_TRANSFORM = 443;
    static readonly KW_IMPORT = 444;
    static readonly KW_POLICY = 445;
    static readonly KW_METHOD = 446;
    static readonly KW_REFERENCING = 447;
    static readonly KW_NEW = 448;
    static readonly KW_OLD = 449;
    static readonly KW_VALUE = 450;
    static readonly KW_SUBSCRIPTION = 451;
    static readonly KW_PUBLICATION = 452;
    static readonly KW_OUT = 453;
    static readonly KW_END = 454;
    static readonly KW_ROUTINES = 455;
    static readonly KW_SCHEMAS = 456;
    static readonly KW_PROCEDURES = 457;
    static readonly KW_INPUT = 458;
    static readonly KW_SUPPORT = 459;
    static readonly KW_PARALLEL = 460;
    static readonly KW_SQL = 461;
    static readonly KW_DEPENDS = 462;
    static readonly KW_OVERRIDING = 463;
    static readonly KW_CONFLICT = 464;
    static readonly KW_SKIP = 465;
    static readonly KW_LOCKED = 466;
    static readonly KW_TIES = 467;
    static readonly KW_ROLLUP = 468;
    static readonly KW_CUBE = 469;
    static readonly KW_GROUPING = 470;
    static readonly KW_SETS = 471;
    static readonly KW_TABLESAMPLE = 472;
    static readonly KW_ORDINALITY = 473;
    static readonly KW_XMLTABLE = 474;
    static readonly KW_COLUMNS = 475;
    static readonly KW_XMLNAMESPACES = 476;
    static readonly KW_ROWTYPE = 477;
    static readonly KW_NORMALIZED = 478;
    static readonly KW_WITHIN = 479;
    static readonly KW_FILTER = 480;
    static readonly KW_GROUPS = 481;
    static readonly KW_OTHERS = 482;
    static readonly KW_NFC = 483;
    static readonly KW_NFD = 484;
    static readonly KW_NFKC = 485;
    static readonly KW_NFKD = 486;
    static readonly KW_UESCAPE = 487;
    static readonly KW_VIEWS = 488;
    static readonly KW_NORMALIZE = 489;
    static readonly KW_DUMP = 490;
    static readonly KW_PRINT_STRICT_PARAMS = 491;
    static readonly KW_VARIABLE_CONFLICT = 492;
    static readonly KW_ERROR = 493;
    static readonly KW_USE_VARIABLE = 494;
    static readonly KW_USE_COLUMN = 495;
    static readonly KW_ALIAS = 496;
    static readonly KW_CONSTANT = 497;
    static readonly KW_PERFORM = 498;
    static readonly KW_GET = 499;
    static readonly KW_DIAGNOSTICS = 500;
    static readonly KW_STACKED = 501;
    static readonly KW_ELSIF = 502;
    static readonly KW_WHILE = 503;
    static readonly KW_REVERSE = 504;
    static readonly KW_FOREACH = 505;
    static readonly KW_SLICE = 506;
    static readonly KW_EXIT = 507;
    static readonly KW_RETURN = 508;
    static readonly KW_QUERY = 509;
    static readonly KW_RAISE = 510;
    static readonly KW_SQLSTATE = 511;
    static readonly KW_DEBUG = 512;
    static readonly KW_LOG = 513;
    static readonly KW_INFO = 514;
    static readonly KW_NOTICE = 515;
    static readonly KW_WARNING = 516;
    static readonly KW_EXCEPTION = 517;
    static readonly KW_ASSERT = 518;
    static readonly KW_LOOP = 519;
    static readonly KW_OPEN = 520;
    static readonly KW_PEFERENCES = 521;
    static readonly KW_USAGE = 522;
    static readonly KW_CONNECT = 523;
    static readonly KW_PUBLIC = 524;
    static readonly KW_MERGE = 525;
    static readonly KW_MATCHED = 526;
    static readonly KW_BREADTH = 527;
    static readonly KW_DEPTH = 528;
    static readonly KW_UNSAFE = 529;
    static readonly KW_RESTRICTED = 530;
    static readonly KW_SAFE = 531;
    static readonly KW_FINALIZE = 532;
    static readonly KW_MODULUS = 533;
    static readonly KW_REMAINDER = 534;
    static readonly KW_LOGIN = 535;
    static readonly KW_NOLOGIN = 536;
    static readonly KW_REPLICATION = 537;
    static readonly KW_NOREPLICATION = 538;
    static readonly KW_BYPASSRLS = 539;
    static readonly KW_NOBYPASSRLS = 540;
    static readonly KW_PERMISSIVE = 541;
    static readonly KW_RESTRICTIVE = 542;
    static readonly KW_COMPRESSION = 543;
    static readonly KW_PLAIN = 544;
    static readonly KW_EXTENDED = 545;
    static readonly KW_MAIN = 546;
    static readonly KW_SKIP_LOCKED = 547;
    static readonly KW_BUFFER_USAGE_LIMIT = 548;
    static readonly Identifier = 549;
    static readonly QuotedIdentifier = 550;
    static readonly UnterminatedQuotedIdentifier = 551;
    static readonly InvalidQuotedIdentifier = 552;
    static readonly InvalidUnterminatedQuotedIdentifier = 553;
    static readonly UnicodeQuotedIdentifier = 554;
    static readonly UnterminatedUnicodeQuotedIdentifier = 555;
    static readonly InvalidUnicodeQuotedIdentifier = 556;
    static readonly InvalidUnterminatedUnicodeQuotedIdentifier = 557;
    static readonly StringConstant = 558;
    static readonly UnterminatedStringConstant = 559;
    static readonly UnicodeEscapeStringConstant = 560;
    static readonly UnterminatedUnicodeEscapeStringConstant = 561;
    static readonly BeginDollarStringConstant = 562;
    static readonly BinaryStringConstant = 563;
    static readonly UnterminatedBinaryStringConstant = 564;
    static readonly InvalidBinaryStringConstant = 565;
    static readonly InvalidUnterminatedBinaryStringConstant = 566;
    static readonly HexadecimalStringConstant = 567;
    static readonly UnterminatedHexadecimalStringConstant = 568;
    static readonly InvalidHexadecimalStringConstant = 569;
    static readonly InvalidUnterminatedHexadecimalStringConstant = 570;
    static readonly Integral = 571;
    static readonly NumericFail = 572;
    static readonly Numeric = 573;
    static readonly PLSQLVARIABLENAME = 574;
    static readonly PLSQLIDENTIFIER = 575;
    static readonly Whitespace = 576;
    static readonly Newline = 577;
    static readonly LineComment = 578;
    static readonly BlockComment = 579;
    static readonly UnterminatedBlockComment = 580;
    static readonly MetaCommand = 581;
    static readonly EndMetaCommand = 582;
    static readonly ErrorCharacter = 583;
    static readonly EscapeStringConstant = 584;
    static readonly UnterminatedEscapeStringConstant = 585;
    static readonly InvalidEscapeStringConstant = 586;
    static readonly InvalidUnterminatedEscapeStringConstant = 587;
    static readonly DollarText = 588;
    static readonly EndDollarStringConstant = 589;
    static readonly AfterEscapeStringConstantWithNewlineMode_Continued = 590;
    static readonly RULE_program = 0;
    static readonly RULE_plsqlroot = 1;
    static readonly RULE_singleStmt = 2;
    static readonly RULE_stmt = 3;
    static readonly RULE_plsqlconsolecommand = 4;
    static readonly RULE_callstmt = 5;
    static readonly RULE_createrolestmt = 6;
    static readonly RULE_opt_with = 7;
    static readonly RULE_optrolelist = 8;
    static readonly RULE_alteroptrolelist = 9;
    static readonly RULE_alteroptroleelem = 10;
    static readonly RULE_createoptroleelem = 11;
    static readonly RULE_createuserstmt = 12;
    static readonly RULE_alterrolestmt = 13;
    static readonly RULE_opt_in_database = 14;
    static readonly RULE_alterrolesetstmt = 15;
    static readonly RULE_alterroutinestmt = 16;
    static readonly RULE_alter_routine_cluase = 17;
    static readonly RULE_routine_action_list = 18;
    static readonly RULE_routine_action = 19;
    static readonly RULE_creategroupstmt = 20;
    static readonly RULE_altergroupstmt = 21;
    static readonly RULE_add_drop = 22;
    static readonly RULE_createschemastmt = 23;
    static readonly RULE_schema_name_create = 24;
    static readonly RULE_optschemaeltlist = 25;
    static readonly RULE_schema_stmt = 26;
    static readonly RULE_variablesetstmt = 27;
    static readonly RULE_set_rest = 28;
    static readonly RULE_generic_set = 29;
    static readonly RULE_set_rest_more = 30;
    static readonly RULE_var_name = 31;
    static readonly RULE_var_list = 32;
    static readonly RULE_var_value = 33;
    static readonly RULE_iso_level = 34;
    static readonly RULE_opt_boolean_or_string_column = 35;
    static readonly RULE_opt_boolean_or_string = 36;
    static readonly RULE_zone_value = 37;
    static readonly RULE_opt_encoding = 38;
    static readonly RULE_nonreservedword_or_sconst_column = 39;
    static readonly RULE_nonreservedword_or_sconst = 40;
    static readonly RULE_variableresetstmt = 41;
    static readonly RULE_reset_rest = 42;
    static readonly RULE_generic_reset = 43;
    static readonly RULE_setresetclause = 44;
    static readonly RULE_functionsetresetclause = 45;
    static readonly RULE_variableshowstmt = 46;
    static readonly RULE_constraintssetstmt = 47;
    static readonly RULE_constraints_set_list = 48;
    static readonly RULE_constraints_set_mode = 49;
    static readonly RULE_checkpointstmt = 50;
    static readonly RULE_discardstmt = 51;
    static readonly RULE_altertablestmt = 52;
    static readonly RULE_alter_table_cmds = 53;
    static readonly RULE_partition_bound_spec = 54;
    static readonly RULE_partition_bound_cluase = 55;
    static readonly RULE_partition_bound_choose = 56;
    static readonly RULE_partition_with_cluase = 57;
    static readonly RULE_partition_cmd = 58;
    static readonly RULE_index_partition_cmd = 59;
    static readonly RULE_alter_table_cmd = 60;
    static readonly RULE_alter_column_default = 61;
    static readonly RULE_opt_drop_behavior = 62;
    static readonly RULE_opt_collate_clause = 63;
    static readonly RULE_alter_using = 64;
    static readonly RULE_replica_identity = 65;
    static readonly RULE_reloptions = 66;
    static readonly RULE_opt_reloptions = 67;
    static readonly RULE_reloption_list = 68;
    static readonly RULE_reloption_elem = 69;
    static readonly RULE_alter_identity_column_option_list = 70;
    static readonly RULE_alter_identity_column_option = 71;
    static readonly RULE_partitionboundspec = 72;
    static readonly RULE_hash_partbound_elem = 73;
    static readonly RULE_hash_partbound = 74;
    static readonly RULE_altercompositetypestmt = 75;
    static readonly RULE_alter_type_cmds = 76;
    static readonly RULE_alter_type_cmd = 77;
    static readonly RULE_closeportalstmt = 78;
    static readonly RULE_copystmt = 79;
    static readonly RULE_copy_from = 80;
    static readonly RULE_opt_program = 81;
    static readonly RULE_copy_file_name = 82;
    static readonly RULE_copy_options = 83;
    static readonly RULE_copy_opt_list = 84;
    static readonly RULE_copy_opt_item = 85;
    static readonly RULE_opt_binary = 86;
    static readonly RULE_copy_delimiter = 87;
    static readonly RULE_opt_using = 88;
    static readonly RULE_copy_generic_opt_list = 89;
    static readonly RULE_copy_generic_opt_elem = 90;
    static readonly RULE_copy_generic_opt_arg = 91;
    static readonly RULE_copy_generic_opt_arg_list = 92;
    static readonly RULE_copy_generic_opt_arg_list_item = 93;
    static readonly RULE_createstmt = 94;
    static readonly RULE_opttemp = 95;
    static readonly RULE_table_column_list = 96;
    static readonly RULE_opttableelementlist = 97;
    static readonly RULE_opttypedtableelementlist = 98;
    static readonly RULE_tableelementlist = 99;
    static readonly RULE_typedtableelementlist = 100;
    static readonly RULE_tableelement = 101;
    static readonly RULE_typedtableelement = 102;
    static readonly RULE_columnDefCluase = 103;
    static readonly RULE_columnDef = 104;
    static readonly RULE_compressionCluase = 105;
    static readonly RULE_storageCluase = 106;
    static readonly RULE_columnOptions = 107;
    static readonly RULE_colquallist = 108;
    static readonly RULE_colconstraint = 109;
    static readonly RULE_colconstraintelem = 110;
    static readonly RULE_nulls_distinct = 111;
    static readonly RULE_generated_when = 112;
    static readonly RULE_deferrable_trigger = 113;
    static readonly RULE_initially_trigger = 114;
    static readonly RULE_tablelikeclause = 115;
    static readonly RULE_tablelikeoptionlist = 116;
    static readonly RULE_tablelikeoption = 117;
    static readonly RULE_tableconstraint = 118;
    static readonly RULE_constraintelem = 119;
    static readonly RULE_opt_no_inherit = 120;
    static readonly RULE_opt_column_list = 121;
    static readonly RULE_columnlist = 122;
    static readonly RULE_opt_c_include = 123;
    static readonly RULE_key_match = 124;
    static readonly RULE_exclusionconstraintlist = 125;
    static readonly RULE_exclusionconstraintelem = 126;
    static readonly RULE_exclusionwhereclause = 127;
    static readonly RULE_key_actions = 128;
    static readonly RULE_key_update = 129;
    static readonly RULE_key_delete = 130;
    static readonly RULE_key_action = 131;
    static readonly RULE_optinherit = 132;
    static readonly RULE_optpartitionspec = 133;
    static readonly RULE_partitionspec = 134;
    static readonly RULE_part_params = 135;
    static readonly RULE_part_elem = 136;
    static readonly RULE_table_access_method_clause = 137;
    static readonly RULE_optwith = 138;
    static readonly RULE_oncommitoption = 139;
    static readonly RULE_opttablespace = 140;
    static readonly RULE_index_paramenters_create = 141;
    static readonly RULE_optconstablespace = 142;
    static readonly RULE_existingindex = 143;
    static readonly RULE_createstatsstmt = 144;
    static readonly RULE_alterstatsstmt = 145;
    static readonly RULE_createasstmt = 146;
    static readonly RULE_create_as_target = 147;
    static readonly RULE_opt_with_data = 148;
    static readonly RULE_creatematviewstmt = 149;
    static readonly RULE_create_mv_target = 150;
    static readonly RULE_optnolog = 151;
    static readonly RULE_refreshmatviewstmt = 152;
    static readonly RULE_createseqstmt = 153;
    static readonly RULE_alterseqstmt = 154;
    static readonly RULE_optseqoptlist = 155;
    static readonly RULE_optparenthesizedseqoptlist = 156;
    static readonly RULE_seqoptlist = 157;
    static readonly RULE_seqoptelem = 158;
    static readonly RULE_opt_by = 159;
    static readonly RULE_numericonly = 160;
    static readonly RULE_numericonly_list = 161;
    static readonly RULE_createplangstmt = 162;
    static readonly RULE_opt_trusted = 163;
    static readonly RULE_handler_name = 164;
    static readonly RULE_opt_inline_handler = 165;
    static readonly RULE_validator_clause = 166;
    static readonly RULE_opt_validator = 167;
    static readonly RULE_opt_procedural = 168;
    static readonly RULE_createtablespacestmt = 169;
    static readonly RULE_opttablespaceowner = 170;
    static readonly RULE_createextensionstmt = 171;
    static readonly RULE_create_extension_opt_list = 172;
    static readonly RULE_create_extension_opt_item = 173;
    static readonly RULE_alterextensionstmt = 174;
    static readonly RULE_alter_extension_opt_list = 175;
    static readonly RULE_alter_extension_opt_item = 176;
    static readonly RULE_alterextensioncontentsstmt = 177;
    static readonly RULE_createfdwstmt = 178;
    static readonly RULE_fdw_option = 179;
    static readonly RULE_fdw_options = 180;
    static readonly RULE_opt_fdw_options = 181;
    static readonly RULE_alterfdwstmt = 182;
    static readonly RULE_create_generic_options = 183;
    static readonly RULE_generic_option_list = 184;
    static readonly RULE_alter_generic_options = 185;
    static readonly RULE_alter_generic_option_list = 186;
    static readonly RULE_alter_generic_option_elem = 187;
    static readonly RULE_generic_option_elem = 188;
    static readonly RULE_generic_option_name = 189;
    static readonly RULE_generic_option_arg = 190;
    static readonly RULE_createforeignserverstmt = 191;
    static readonly RULE_opt_type = 192;
    static readonly RULE_foreign_server_version = 193;
    static readonly RULE_opt_foreign_server_version = 194;
    static readonly RULE_alterforeignserverstmt = 195;
    static readonly RULE_createforeigntablestmt = 196;
    static readonly RULE_importforeignschemastmt = 197;
    static readonly RULE_import_qualification_type = 198;
    static readonly RULE_import_qualification = 199;
    static readonly RULE_createusermappingstmt = 200;
    static readonly RULE_auth_ident = 201;
    static readonly RULE_alterusermappingstmt = 202;
    static readonly RULE_createpolicystmt = 203;
    static readonly RULE_alterpolicystmt = 204;
    static readonly RULE_alterprocedurestmt = 205;
    static readonly RULE_procedure_cluase = 206;
    static readonly RULE_procedure_action = 207;
    static readonly RULE_rowsecurityoptionalexpr = 208;
    static readonly RULE_rowsecurityoptionalwithcheck = 209;
    static readonly RULE_rowsecuritydefaulttorole = 210;
    static readonly RULE_rowsecurityoptionaltorole = 211;
    static readonly RULE_rowsecuritydefaultpermissive = 212;
    static readonly RULE_rowsecuritydefaultforcmd = 213;
    static readonly RULE_row_security_cmd = 214;
    static readonly RULE_createamstmt = 215;
    static readonly RULE_am_type = 216;
    static readonly RULE_createtrigstmt = 217;
    static readonly RULE_triggeractiontime = 218;
    static readonly RULE_foreachrow = 219;
    static readonly RULE_roworstatment = 220;
    static readonly RULE_triggerevents = 221;
    static readonly RULE_triggeroneevent = 222;
    static readonly RULE_triggerreferencing = 223;
    static readonly RULE_triggertransitions = 224;
    static readonly RULE_triggertransition = 225;
    static readonly RULE_transitionoldornew = 226;
    static readonly RULE_transitionrowortable = 227;
    static readonly RULE_transitionrelname = 228;
    static readonly RULE_triggerforspec = 229;
    static readonly RULE_triggerforopteach = 230;
    static readonly RULE_triggerfortype = 231;
    static readonly RULE_triggerwhen = 232;
    static readonly RULE_function_or_procedure = 233;
    static readonly RULE_triggerfuncargs = 234;
    static readonly RULE_triggerfuncarg = 235;
    static readonly RULE_optconstrfromtable = 236;
    static readonly RULE_constraintattributespec = 237;
    static readonly RULE_constraintattributeElem = 238;
    static readonly RULE_createeventtrigstmt = 239;
    static readonly RULE_event_trigger_when_list = 240;
    static readonly RULE_event_trigger_when_item = 241;
    static readonly RULE_event_trigger_value_list = 242;
    static readonly RULE_altereventtrigstmt = 243;
    static readonly RULE_enable_trigger = 244;
    static readonly RULE_createassertionstmt = 245;
    static readonly RULE_definestmt = 246;
    static readonly RULE_definition = 247;
    static readonly RULE_def_list = 248;
    static readonly RULE_def_elem = 249;
    static readonly RULE_def_arg = 250;
    static readonly RULE_old_aggr_definition = 251;
    static readonly RULE_old_aggr_list = 252;
    static readonly RULE_old_aggr_elem = 253;
    static readonly RULE_opt_enum_val_list = 254;
    static readonly RULE_enum_val_list = 255;
    static readonly RULE_alterenumstmt = 256;
    static readonly RULE_opt_if_not_exists = 257;
    static readonly RULE_createopclassstmt = 258;
    static readonly RULE_opclass_item_list = 259;
    static readonly RULE_opclass_item = 260;
    static readonly RULE_opt_default = 261;
    static readonly RULE_opt_opfamily = 262;
    static readonly RULE_opclass_purpose = 263;
    static readonly RULE_opt_recheck = 264;
    static readonly RULE_createopfamilystmt = 265;
    static readonly RULE_alteropfamilystmt = 266;
    static readonly RULE_opclass_drop_list = 267;
    static readonly RULE_opclass_drop = 268;
    static readonly RULE_reassignownedstmt = 269;
    static readonly RULE_dropstmt = 270;
    static readonly RULE_view_nameList = 271;
    static readonly RULE_object_type_any_name = 272;
    static readonly RULE_object_type_name = 273;
    static readonly RULE_object_type_name_on_any_name = 274;
    static readonly RULE_any_name_list = 275;
    static readonly RULE_table_column_name = 276;
    static readonly RULE_relation_column_name = 277;
    static readonly RULE_relation_name = 278;
    static readonly RULE_any_name = 279;
    static readonly RULE_attrs = 280;
    static readonly RULE_type_name_list = 281;
    static readonly RULE_truncatestmt = 282;
    static readonly RULE_opt_restart_seqs = 283;
    static readonly RULE_commentstmt = 284;
    static readonly RULE_comment_text = 285;
    static readonly RULE_seclabelstmt = 286;
    static readonly RULE_opt_provider = 287;
    static readonly RULE_security_label = 288;
    static readonly RULE_fetchstmt = 289;
    static readonly RULE_fetch_args = 290;
    static readonly RULE_from_in = 291;
    static readonly RULE_opt_from_in = 292;
    static readonly RULE_grantstmt = 293;
    static readonly RULE_revokestmt = 294;
    static readonly RULE_privileges = 295;
    static readonly RULE_beforeprivilegeselectlist = 296;
    static readonly RULE_beforeprivilegeselect = 297;
    static readonly RULE_privilege_list = 298;
    static readonly RULE_privilege = 299;
    static readonly RULE_privilege_target = 300;
    static readonly RULE_grantee_list = 301;
    static readonly RULE_grantee = 302;
    static readonly RULE_opt_grant_grant_option = 303;
    static readonly RULE_grantrolestmt = 304;
    static readonly RULE_revokerolestmt = 305;
    static readonly RULE_opt_grant_admin_option = 306;
    static readonly RULE_opt_granted_by = 307;
    static readonly RULE_alterdefaultprivilegesstmt = 308;
    static readonly RULE_defacloptionlist = 309;
    static readonly RULE_defacloption = 310;
    static readonly RULE_defaclaction = 311;
    static readonly RULE_defacl_privilege_target = 312;
    static readonly RULE_indexstmt = 313;
    static readonly RULE_opt_unique = 314;
    static readonly RULE_opt_concurrently = 315;
    static readonly RULE_opt_index_name = 316;
    static readonly RULE_access_method_clause = 317;
    static readonly RULE_index_params = 318;
    static readonly RULE_index_elem_options = 319;
    static readonly RULE_index_elem = 320;
    static readonly RULE_opt_include = 321;
    static readonly RULE_index_including_params = 322;
    static readonly RULE_opt_collate = 323;
    static readonly RULE_opt_class = 324;
    static readonly RULE_opt_asc_desc = 325;
    static readonly RULE_opt_nulls_order = 326;
    static readonly RULE_createfunctionstmt = 327;
    static readonly RULE_attrilist = 328;
    static readonly RULE_opt_or_replace = 329;
    static readonly RULE_func_args = 330;
    static readonly RULE_func_args_list = 331;
    static readonly RULE_routine_with_argtypes_list = 332;
    static readonly RULE_routine_with_argtypes = 333;
    static readonly RULE_procedure_with_argtypes_list = 334;
    static readonly RULE_procedure_with_argtypes = 335;
    static readonly RULE_function_with_argtypes_list = 336;
    static readonly RULE_function_with_argtypes = 337;
    static readonly RULE_func_args_with_defaults = 338;
    static readonly RULE_func_args_with_defaults_list = 339;
    static readonly RULE_func_arg = 340;
    static readonly RULE_arg_class = 341;
    static readonly RULE_param_name = 342;
    static readonly RULE_func_return = 343;
    static readonly RULE_func_type = 344;
    static readonly RULE_func_arg_with_default = 345;
    static readonly RULE_aggr_arg = 346;
    static readonly RULE_aggr_args = 347;
    static readonly RULE_aggr_args_list = 348;
    static readonly RULE_aggregate_with_argtypes = 349;
    static readonly RULE_aggregate_with_argtypes_list = 350;
    static readonly RULE_createfunc_opt_list = 351;
    static readonly RULE_common_func_opt_item = 352;
    static readonly RULE_createfunc_opt_item = 353;
    static readonly RULE_transform_type_list = 354;
    static readonly RULE_opt_definition = 355;
    static readonly RULE_table_func_column = 356;
    static readonly RULE_table_func_column_list = 357;
    static readonly RULE_alterfunctionstmt = 358;
    static readonly RULE_alterFunctionTypeClause = 359;
    static readonly RULE_alterfunc_opt_list = 360;
    static readonly RULE_opt_restrict = 361;
    static readonly RULE_removefuncstmt = 362;
    static readonly RULE_removeaggrstmt = 363;
    static readonly RULE_removeoperstmt = 364;
    static readonly RULE_oper_argtypes = 365;
    static readonly RULE_any_operator = 366;
    static readonly RULE_operator_with_argtypes_list = 367;
    static readonly RULE_operator_with_argtypes = 368;
    static readonly RULE_dostmt = 369;
    static readonly RULE_dostmt_opt_list = 370;
    static readonly RULE_dostmt_opt_item = 371;
    static readonly RULE_createcaststmt = 372;
    static readonly RULE_cast_context = 373;
    static readonly RULE_opt_if_exists = 374;
    static readonly RULE_createtransformstmt = 375;
    static readonly RULE_transform_element_list = 376;
    static readonly RULE_reindexstmt = 377;
    static readonly RULE_reindex_target_type = 378;
    static readonly RULE_reindex_target_multitable = 379;
    static readonly RULE_reindex_option_list = 380;
    static readonly RULE_reindex_option_elem = 381;
    static readonly RULE_altertblspcstmt = 382;
    static readonly RULE_renamestmt = 383;
    static readonly RULE_opt_column = 384;
    static readonly RULE_opt_set_data = 385;
    static readonly RULE_alterobjectdependsstmt = 386;
    static readonly RULE_opt_no = 387;
    static readonly RULE_alterobjectschemastmt = 388;
    static readonly RULE_alteroperatorstmt = 389;
    static readonly RULE_operator_def_list = 390;
    static readonly RULE_operator_def_elem = 391;
    static readonly RULE_operator_def_arg = 392;
    static readonly RULE_altertypestmt = 393;
    static readonly RULE_alterownerstmt = 394;
    static readonly RULE_createpublicationstmt = 395;
    static readonly RULE_opt_publication_for_tables = 396;
    static readonly RULE_publication_for_tables = 397;
    static readonly RULE_alterpublicationstmt = 398;
    static readonly RULE_createsubscriptionstmt = 399;
    static readonly RULE_publication_name_list = 400;
    static readonly RULE_publication_name_item = 401;
    static readonly RULE_altersubscriptionstmt = 402;
    static readonly RULE_rulestmt = 403;
    static readonly RULE_ruleactionlist = 404;
    static readonly RULE_ruleactionmulti = 405;
    static readonly RULE_ruleactionstmt = 406;
    static readonly RULE_ruleactionstmtOrEmpty = 407;
    static readonly RULE_event = 408;
    static readonly RULE_opt_instead = 409;
    static readonly RULE_notifystmt = 410;
    static readonly RULE_notify_payload = 411;
    static readonly RULE_listenstmt = 412;
    static readonly RULE_unlistenstmt = 413;
    static readonly RULE_transactionstmt = 414;
    static readonly RULE_opt_transaction = 415;
    static readonly RULE_transaction_mode_item = 416;
    static readonly RULE_transaction_mode_list = 417;
    static readonly RULE_transaction_mode_list_or_empty = 418;
    static readonly RULE_opt_transaction_chain = 419;
    static readonly RULE_viewstmt = 420;
    static readonly RULE_opt_check_option = 421;
    static readonly RULE_loadstmt = 422;
    static readonly RULE_createdbstmt = 423;
    static readonly RULE_createdb_opt_list = 424;
    static readonly RULE_createdb_opt_items = 425;
    static readonly RULE_createdb_opt_item = 426;
    static readonly RULE_createdb_opt_name = 427;
    static readonly RULE_opt_equal = 428;
    static readonly RULE_alterdatabasestmt = 429;
    static readonly RULE_alterdatabasesetstmt = 430;
    static readonly RULE_drop_option_list = 431;
    static readonly RULE_drop_option = 432;
    static readonly RULE_altercollationstmt = 433;
    static readonly RULE_altersystemstmt = 434;
    static readonly RULE_createdomainstmt = 435;
    static readonly RULE_alterdomainstmt = 436;
    static readonly RULE_opt_as = 437;
    static readonly RULE_altertsdictionarystmt = 438;
    static readonly RULE_altertsconfigurationstmt = 439;
    static readonly RULE_any_with = 440;
    static readonly RULE_createconversionstmt = 441;
    static readonly RULE_clusterstmt = 442;
    static readonly RULE_opt_verbose_list = 443;
    static readonly RULE_cluster_index_specification = 444;
    static readonly RULE_vacuumstmt = 445;
    static readonly RULE_analyzestmt = 446;
    static readonly RULE_vac_analyze_option_list = 447;
    static readonly RULE_analyze_keyword = 448;
    static readonly RULE_vac_analyze_option_elem = 449;
    static readonly RULE_vac_analyze_option_name = 450;
    static readonly RULE_vac_analyze_option_arg = 451;
    static readonly RULE_opt_analyze = 452;
    static readonly RULE_analyze_options_list = 453;
    static readonly RULE_analyze_option_elem = 454;
    static readonly RULE_opt_verbose = 455;
    static readonly RULE_opt_skiplock = 456;
    static readonly RULE_opt_buffer_usage_limit = 457;
    static readonly RULE_opt_full = 458;
    static readonly RULE_opt_freeze = 459;
    static readonly RULE_opt_name_list = 460;
    static readonly RULE_vacuum_relation = 461;
    static readonly RULE_vacuum_relation_list = 462;
    static readonly RULE_opt_vacuum_relation_list = 463;
    static readonly RULE_explainstmt = 464;
    static readonly RULE_explainablestmt = 465;
    static readonly RULE_explain_option_list = 466;
    static readonly RULE_explain_option_elem = 467;
    static readonly RULE_explain_option_name = 468;
    static readonly RULE_explain_option_arg = 469;
    static readonly RULE_preparestmt = 470;
    static readonly RULE_prep_type_clause = 471;
    static readonly RULE_preparablestmt = 472;
    static readonly RULE_executestmt = 473;
    static readonly RULE_execute_param_clause = 474;
    static readonly RULE_deallocatestmt = 475;
    static readonly RULE_insertstmt = 476;
    static readonly RULE_insert_target = 477;
    static readonly RULE_insert_rest = 478;
    static readonly RULE_override_kind = 479;
    static readonly RULE_insert_column_list = 480;
    static readonly RULE_insert_column_item = 481;
    static readonly RULE_opt_on_conflict = 482;
    static readonly RULE_opt_conf_expr = 483;
    static readonly RULE_returning_clause = 484;
    static readonly RULE_deletestmt = 485;
    static readonly RULE_using_clause = 486;
    static readonly RULE_lockstmt = 487;
    static readonly RULE_opt_lock = 488;
    static readonly RULE_lock_type = 489;
    static readonly RULE_opt_nowait = 490;
    static readonly RULE_opt_nowait_or_skip = 491;
    static readonly RULE_updatestmt = 492;
    static readonly RULE_set_clause_list = 493;
    static readonly RULE_set_clause = 494;
    static readonly RULE_set_target = 495;
    static readonly RULE_set_target_list = 496;
    static readonly RULE_declarecursorstmt = 497;
    static readonly RULE_cursor_name = 498;
    static readonly RULE_cursor_options = 499;
    static readonly RULE_opt_hold = 500;
    static readonly RULE_selectstmt = 501;
    static readonly RULE_select_with_parens = 502;
    static readonly RULE_select_no_parens = 503;
    static readonly RULE_select_clause = 504;
    static readonly RULE_simple_select = 505;
    static readonly RULE_set_operator = 506;
    static readonly RULE_set_operator_with_all_or_distinct = 507;
    static readonly RULE_with_clause = 508;
    static readonly RULE_cte_list = 509;
    static readonly RULE_common_table_expr = 510;
    static readonly RULE_search_cluase = 511;
    static readonly RULE_cycle_cluase = 512;
    static readonly RULE_opt_materialized = 513;
    static readonly RULE_opt_with_clause = 514;
    static readonly RULE_into_clause = 515;
    static readonly RULE_opt_strict = 516;
    static readonly RULE_opttempTableName = 517;
    static readonly RULE_opt_table = 518;
    static readonly RULE_all_or_distinct = 519;
    static readonly RULE_distinct_clause = 520;
    static readonly RULE_opt_all_clause = 521;
    static readonly RULE_opt_sort_clause = 522;
    static readonly RULE_sort_clause = 523;
    static readonly RULE_sortby_list = 524;
    static readonly RULE_sortby = 525;
    static readonly RULE_select_limit = 526;
    static readonly RULE_opt_select_limit = 527;
    static readonly RULE_limit_clause = 528;
    static readonly RULE_fetch_clause = 529;
    static readonly RULE_offset_clause = 530;
    static readonly RULE_select_limit_value = 531;
    static readonly RULE_select_offset_value = 532;
    static readonly RULE_select_fetch_first_value = 533;
    static readonly RULE_i_or_f_const = 534;
    static readonly RULE_row_or_rows = 535;
    static readonly RULE_first_or_next = 536;
    static readonly RULE_group_clause = 537;
    static readonly RULE_group_by_list = 538;
    static readonly RULE_group_by_item = 539;
    static readonly RULE_empty_grouping_set = 540;
    static readonly RULE_rollup_clause = 541;
    static readonly RULE_cube_clause = 542;
    static readonly RULE_grouping_sets_clause = 543;
    static readonly RULE_having_clause = 544;
    static readonly RULE_for_locking_clause = 545;
    static readonly RULE_opt_for_locking_clause = 546;
    static readonly RULE_for_locking_items = 547;
    static readonly RULE_for_locking_item = 548;
    static readonly RULE_for_locking_strength = 549;
    static readonly RULE_locked_rels_list = 550;
    static readonly RULE_values_clause = 551;
    static readonly RULE_from_clause = 552;
    static readonly RULE_from_list = 553;
    static readonly RULE_table_ref = 554;
    static readonly RULE_alias_clause = 555;
    static readonly RULE_opt_alias_clause = 556;
    static readonly RULE_func_alias_clause = 557;
    static readonly RULE_join_type = 558;
    static readonly RULE_join_qual = 559;
    static readonly RULE_relation_expr = 560;
    static readonly RULE_publication_relation_expr = 561;
    static readonly RULE_relation_expr_list = 562;
    static readonly RULE_publication_relation_expr_list = 563;
    static readonly RULE_relation_expr_opt_alias = 564;
    static readonly RULE_tablesample_clause = 565;
    static readonly RULE_opt_repeatable_clause = 566;
    static readonly RULE_func_table = 567;
    static readonly RULE_rowsfrom_item = 568;
    static readonly RULE_rowsfrom_list = 569;
    static readonly RULE_opt_col_def_list = 570;
    static readonly RULE_opt_ordinality = 571;
    static readonly RULE_where_clause = 572;
    static readonly RULE_where_or_current_clause = 573;
    static readonly RULE_opttablefuncelementlist = 574;
    static readonly RULE_tablefuncelementlist = 575;
    static readonly RULE_tablefuncelement = 576;
    static readonly RULE_xmltable = 577;
    static readonly RULE_xmltable_column_list = 578;
    static readonly RULE_xmltable_column_el = 579;
    static readonly RULE_xmltable_column_option_list = 580;
    static readonly RULE_xmltable_column_option_el = 581;
    static readonly RULE_xml_namespace_list = 582;
    static readonly RULE_xml_namespace_el = 583;
    static readonly RULE_typename = 584;
    static readonly RULE_opt_array_bounds = 585;
    static readonly RULE_simpletypename = 586;
    static readonly RULE_consttypename = 587;
    static readonly RULE_generictype = 588;
    static readonly RULE_opt_type_modifiers = 589;
    static readonly RULE_numeric = 590;
    static readonly RULE_opt_float = 591;
    static readonly RULE_bit = 592;
    static readonly RULE_constbit = 593;
    static readonly RULE_bitwithlength = 594;
    static readonly RULE_bitwithoutlength = 595;
    static readonly RULE_character = 596;
    static readonly RULE_constcharacter = 597;
    static readonly RULE_character_c = 598;
    static readonly RULE_opt_varying = 599;
    static readonly RULE_constdatetime = 600;
    static readonly RULE_constinterval = 601;
    static readonly RULE_opt_timezone = 602;
    static readonly RULE_opt_interval = 603;
    static readonly RULE_interval_second = 604;
    static readonly RULE_opt_escape = 605;
    static readonly RULE_a_expr = 606;
    static readonly RULE_a_expr_qual = 607;
    static readonly RULE_a_expr_lessless = 608;
    static readonly RULE_a_expr_or = 609;
    static readonly RULE_a_expr_and = 610;
    static readonly RULE_a_expr_in = 611;
    static readonly RULE_a_expr_unary_not = 612;
    static readonly RULE_a_expr_isnull = 613;
    static readonly RULE_a_expr_is_not = 614;
    static readonly RULE_a_expr_compare = 615;
    static readonly RULE_a_expr_like = 616;
    static readonly RULE_a_expr_qual_op = 617;
    static readonly RULE_a_expr_unary_qualop = 618;
    static readonly RULE_a_expr_add = 619;
    static readonly RULE_a_expr_mul = 620;
    static readonly RULE_a_expr_caret = 621;
    static readonly RULE_a_expr_unary_sign = 622;
    static readonly RULE_a_expr_at_time_zone = 623;
    static readonly RULE_a_expr_collate = 624;
    static readonly RULE_a_expr_typecast = 625;
    static readonly RULE_b_expr = 626;
    static readonly RULE_c_expr = 627;
    static readonly RULE_plsqlvariablename = 628;
    static readonly RULE_func_application = 629;
    static readonly RULE_func_expr = 630;
    static readonly RULE_func_expr_windowless = 631;
    static readonly RULE_func_expr_common_subexpr = 632;
    static readonly RULE_xml_root_version = 633;
    static readonly RULE_opt_xml_root_standalone = 634;
    static readonly RULE_xml_attributes = 635;
    static readonly RULE_xml_attribute_list = 636;
    static readonly RULE_xml_attribute_el = 637;
    static readonly RULE_document_or_content = 638;
    static readonly RULE_xml_whitespace_option = 639;
    static readonly RULE_xmlexists_argument = 640;
    static readonly RULE_xml_passing_mech = 641;
    static readonly RULE_within_group_clause = 642;
    static readonly RULE_filter_clause = 643;
    static readonly RULE_window_clause = 644;
    static readonly RULE_window_definition_list = 645;
    static readonly RULE_window_definition = 646;
    static readonly RULE_over_clause = 647;
    static readonly RULE_window_specification = 648;
    static readonly RULE_opt_existing_window_name = 649;
    static readonly RULE_opt_partition_clause = 650;
    static readonly RULE_opt_frame_clause = 651;
    static readonly RULE_frame_extent = 652;
    static readonly RULE_frame_bound = 653;
    static readonly RULE_opt_window_exclusion_clause = 654;
    static readonly RULE_row = 655;
    static readonly RULE_explicit_row = 656;
    static readonly RULE_implicit_row = 657;
    static readonly RULE_sub_type = 658;
    static readonly RULE_all_op = 659;
    static readonly RULE_mathop = 660;
    static readonly RULE_qual_op = 661;
    static readonly RULE_qual_all_op = 662;
    static readonly RULE_subquery_Op = 663;
    static readonly RULE_expr_list = 664;
    static readonly RULE_column_expr_list_noparen = 665;
    static readonly RULE_column_expr_list = 666;
    static readonly RULE_column_expr = 667;
    static readonly RULE_column_expr_noparen = 668;
    static readonly RULE_func_arg_list = 669;
    static readonly RULE_func_arg_expr = 670;
    static readonly RULE_type_list = 671;
    static readonly RULE_array_expr = 672;
    static readonly RULE_array_expr_list = 673;
    static readonly RULE_extract_list = 674;
    static readonly RULE_extract_arg = 675;
    static readonly RULE_unicode_normal_form = 676;
    static readonly RULE_overlay_list = 677;
    static readonly RULE_position_list = 678;
    static readonly RULE_substr_list = 679;
    static readonly RULE_trim_list = 680;
    static readonly RULE_in_expr = 681;
    static readonly RULE_case_expr = 682;
    static readonly RULE_when_clause_list = 683;
    static readonly RULE_when_clause = 684;
    static readonly RULE_case_default = 685;
    static readonly RULE_case_arg = 686;
    static readonly RULE_columnref = 687;
    static readonly RULE_indirection_el = 688;
    static readonly RULE_opt_slice_bound = 689;
    static readonly RULE_indirection = 690;
    static readonly RULE_opt_indirection = 691;
    static readonly RULE_opt_target_list = 692;
    static readonly RULE_target_list = 693;
    static readonly RULE_target_el = 694;
    static readonly RULE_qualified_name_list = 695;
    static readonly RULE_table_name_list = 696;
    static readonly RULE_schema_name_list = 697;
    static readonly RULE_database_nameList = 698;
    static readonly RULE_procedure_name_list = 699;
    static readonly RULE_tablespace_name_create = 700;
    static readonly RULE_tablespace_name = 701;
    static readonly RULE_table_name_create = 702;
    static readonly RULE_table_name = 703;
    static readonly RULE_view_name_create = 704;
    static readonly RULE_view_name = 705;
    static readonly RULE_qualified_name = 706;
    static readonly RULE_tablespace_name_list = 707;
    static readonly RULE_name_list = 708;
    static readonly RULE_database_name_create = 709;
    static readonly RULE_database_name = 710;
    static readonly RULE_schema_name = 711;
    static readonly RULE_routine_name_create = 712;
    static readonly RULE_routine_name = 713;
    static readonly RULE_procedure_name = 714;
    static readonly RULE_procedure_name_create = 715;
    static readonly RULE_column_name = 716;
    static readonly RULE_column_name_create = 717;
    static readonly RULE_name = 718;
    static readonly RULE_attr_name = 719;
    static readonly RULE_file_name = 720;
    static readonly RULE_function_name_create = 721;
    static readonly RULE_function_name = 722;
    static readonly RULE_usual_name = 723;
    static readonly RULE_aexprconst = 724;
    static readonly RULE_xconst = 725;
    static readonly RULE_bconst = 726;
    static readonly RULE_fconst = 727;
    static readonly RULE_iconst = 728;
    static readonly RULE_sconst = 729;
    static readonly RULE_anysconst = 730;
    static readonly RULE_opt_uescape = 731;
    static readonly RULE_signediconst = 732;
    static readonly RULE_groupname = 733;
    static readonly RULE_roleid = 734;
    static readonly RULE_rolespec = 735;
    static readonly RULE_role_list = 736;
    static readonly RULE_colid = 737;
    static readonly RULE_index_method_choices = 738;
    static readonly RULE_exclude_element = 739;
    static readonly RULE_index_paramenters = 740;
    static readonly RULE_type_function_name = 741;
    static readonly RULE_type_usual_name = 742;
    static readonly RULE_nonreservedword_column = 743;
    static readonly RULE_nonreservedword = 744;
    static readonly RULE_collabel = 745;
    static readonly RULE_identifier = 746;
    static readonly RULE_plsqlidentifier = 747;
    static readonly RULE_unreserved_keyword = 748;
    static readonly RULE_col_name_keyword = 749;
    static readonly RULE_type_func_name_keyword = 750;
    static readonly RULE_reserved_keyword = 751;
    static readonly RULE_pl_function = 752;
    static readonly RULE_comp_options = 753;
    static readonly RULE_comp_option = 754;
    static readonly RULE_sharp = 755;
    static readonly RULE_option_value = 756;
    static readonly RULE_opt_semi = 757;
    static readonly RULE_pl_block = 758;
    static readonly RULE_decl_sect = 759;
    static readonly RULE_decl_start = 760;
    static readonly RULE_decl_stmts = 761;
    static readonly RULE_label_decl = 762;
    static readonly RULE_decl_stmt = 763;
    static readonly RULE_decl_statement = 764;
    static readonly RULE_opt_scrollable = 765;
    static readonly RULE_decl_cursor_query = 766;
    static readonly RULE_decl_cursor_args = 767;
    static readonly RULE_decl_cursor_arglist = 768;
    static readonly RULE_decl_cursor_arg = 769;
    static readonly RULE_decl_is_for = 770;
    static readonly RULE_decl_aliasitem = 771;
    static readonly RULE_decl_varname = 772;
    static readonly RULE_decl_const = 773;
    static readonly RULE_decl_datatype = 774;
    static readonly RULE_decl_collate = 775;
    static readonly RULE_decl_notnull = 776;
    static readonly RULE_decl_defval = 777;
    static readonly RULE_decl_defkey = 778;
    static readonly RULE_assign_operator = 779;
    static readonly RULE_proc_sect = 780;
    static readonly RULE_proc_stmt = 781;
    static readonly RULE_stmt_perform = 782;
    static readonly RULE_stmt_call = 783;
    static readonly RULE_opt_expr_list = 784;
    static readonly RULE_stmt_assign = 785;
    static readonly RULE_stmt_getdiag = 786;
    static readonly RULE_getdiag_area_opt = 787;
    static readonly RULE_getdiag_list = 788;
    static readonly RULE_getdiag_list_item = 789;
    static readonly RULE_getdiag_item = 790;
    static readonly RULE_getdiag_target = 791;
    static readonly RULE_assign_var = 792;
    static readonly RULE_stmt_if = 793;
    static readonly RULE_stmt_elsifs = 794;
    static readonly RULE_stmt_else = 795;
    static readonly RULE_stmt_case = 796;
    static readonly RULE_opt_expr_until_when = 797;
    static readonly RULE_case_when_list = 798;
    static readonly RULE_case_when = 799;
    static readonly RULE_opt_case_else = 800;
    static readonly RULE_stmt_loop = 801;
    static readonly RULE_stmt_while = 802;
    static readonly RULE_stmt_for = 803;
    static readonly RULE_for_control = 804;
    static readonly RULE_opt_for_using_expression = 805;
    static readonly RULE_opt_cursor_parameters = 806;
    static readonly RULE_opt_reverse = 807;
    static readonly RULE_opt_by_expression = 808;
    static readonly RULE_for_variable = 809;
    static readonly RULE_stmt_foreach_a = 810;
    static readonly RULE_foreach_slice = 811;
    static readonly RULE_stmt_exit = 812;
    static readonly RULE_exit_type = 813;
    static readonly RULE_stmt_return = 814;
    static readonly RULE_opt_return_result = 815;
    static readonly RULE_stmt_raise = 816;
    static readonly RULE_opt_stmt_raise_level = 817;
    static readonly RULE_opt_raise_list = 818;
    static readonly RULE_opt_raise_using = 819;
    static readonly RULE_opt_raise_using_elem = 820;
    static readonly RULE_opt_raise_using_elem_list = 821;
    static readonly RULE_stmt_assert = 822;
    static readonly RULE_opt_stmt_assert_message = 823;
    static readonly RULE_loop_body = 824;
    static readonly RULE_stmt_execsql = 825;
    static readonly RULE_stmt_dynexecute = 826;
    static readonly RULE_opt_execute_using = 827;
    static readonly RULE_opt_execute_using_list = 828;
    static readonly RULE_opt_execute_into = 829;
    static readonly RULE_stmt_open = 830;
    static readonly RULE_opt_open_bound_list_item = 831;
    static readonly RULE_opt_open_bound_list = 832;
    static readonly RULE_opt_open_using = 833;
    static readonly RULE_opt_scroll_option = 834;
    static readonly RULE_opt_scroll_option_no = 835;
    static readonly RULE_stmt_fetch = 836;
    static readonly RULE_into_target = 837;
    static readonly RULE_opt_cursor_from = 838;
    static readonly RULE_opt_fetch_direction = 839;
    static readonly RULE_stmt_move = 840;
    static readonly RULE_mergestmt = 841;
    static readonly RULE_data_source = 842;
    static readonly RULE_join_condition = 843;
    static readonly RULE_merge_when_clause = 844;
    static readonly RULE_merge_insert = 845;
    static readonly RULE_merge_update = 846;
    static readonly RULE_default_values_or_values = 847;
    static readonly RULE_exprofdefaultlist = 848;
    static readonly RULE_exprofdefault = 849;
    static readonly RULE_stmt_close = 850;
    static readonly RULE_stmt_null = 851;
    static readonly RULE_stmt_commit = 852;
    static readonly RULE_stmt_rollback = 853;
    static readonly RULE_plsql_opt_transaction_chain = 854;
    static readonly RULE_stmt_set = 855;
    static readonly RULE_cursor_variable = 856;
    static readonly RULE_exception_sect = 857;
    static readonly RULE_proc_exceptions = 858;
    static readonly RULE_proc_exception = 859;
    static readonly RULE_proc_conditions = 860;
    static readonly RULE_proc_condition = 861;
    static readonly RULE_opt_block_label = 862;
    static readonly RULE_opt_loop_label = 863;
    static readonly RULE_opt_label = 864;
    static readonly RULE_opt_exitcond = 865;
    static readonly RULE_any_identifier = 866;
    static readonly RULE_plsql_unreserved_keyword = 867;
    static readonly RULE_sql_expression = 868;
    static readonly RULE_expr_until_then = 869;
    static readonly RULE_expr_until_semi = 870;
    static readonly RULE_expr_until_rightbracket = 871;
    static readonly RULE_expr_until_loop = 872;
    static readonly RULE_make_execsql_stmt = 873;
    static readonly RULE_opt_returning_clause_into = 874;
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
    plsqlroot(): PlsqlrootContext;
    singleStmt(): SingleStmtContext;
    stmt(): StmtContext;
    plsqlconsolecommand(): PlsqlconsolecommandContext;
    callstmt(): CallstmtContext;
    createrolestmt(): CreaterolestmtContext;
    opt_with(): Opt_withContext;
    optrolelist(): OptrolelistContext;
    alteroptrolelist(): AlteroptrolelistContext;
    alteroptroleelem(): AlteroptroleelemContext;
    createoptroleelem(): CreateoptroleelemContext;
    createuserstmt(): CreateuserstmtContext;
    alterrolestmt(): AlterrolestmtContext;
    opt_in_database(): Opt_in_databaseContext;
    alterrolesetstmt(): AlterrolesetstmtContext;
    alterroutinestmt(): AlterroutinestmtContext;
    alter_routine_cluase(): Alter_routine_cluaseContext;
    routine_action_list(): Routine_action_listContext;
    routine_action(): Routine_actionContext;
    creategroupstmt(): CreategroupstmtContext;
    altergroupstmt(): AltergroupstmtContext;
    add_drop(): Add_dropContext;
    createschemastmt(): CreateschemastmtContext;
    schema_name_create(): Schema_name_createContext;
    optschemaeltlist(): OptschemaeltlistContext;
    schema_stmt(): Schema_stmtContext;
    variablesetstmt(): VariablesetstmtContext;
    set_rest(): Set_restContext;
    generic_set(): Generic_setContext;
    set_rest_more(): Set_rest_moreContext;
    var_name(): Var_nameContext;
    var_list(): Var_listContext;
    var_value(): Var_valueContext;
    iso_level(): Iso_levelContext;
    opt_boolean_or_string_column(): Opt_boolean_or_string_columnContext;
    opt_boolean_or_string(): Opt_boolean_or_stringContext;
    zone_value(): Zone_valueContext;
    opt_encoding(): Opt_encodingContext;
    nonreservedword_or_sconst_column(): Nonreservedword_or_sconst_columnContext;
    nonreservedword_or_sconst(): Nonreservedword_or_sconstContext;
    variableresetstmt(): VariableresetstmtContext;
    reset_rest(): Reset_restContext;
    generic_reset(): Generic_resetContext;
    setresetclause(): SetresetclauseContext;
    functionsetresetclause(): FunctionsetresetclauseContext;
    variableshowstmt(): VariableshowstmtContext;
    constraintssetstmt(): ConstraintssetstmtContext;
    constraints_set_list(): Constraints_set_listContext;
    constraints_set_mode(): Constraints_set_modeContext;
    checkpointstmt(): CheckpointstmtContext;
    discardstmt(): DiscardstmtContext;
    altertablestmt(): AltertablestmtContext;
    alter_table_cmds(): Alter_table_cmdsContext;
    partition_bound_spec(): Partition_bound_specContext;
    partition_bound_cluase(): Partition_bound_cluaseContext;
    partition_bound_choose(): Partition_bound_chooseContext;
    partition_with_cluase(): Partition_with_cluaseContext;
    partition_cmd(): Partition_cmdContext;
    index_partition_cmd(): Index_partition_cmdContext;
    alter_table_cmd(): Alter_table_cmdContext;
    alter_column_default(): Alter_column_defaultContext;
    opt_drop_behavior(): Opt_drop_behaviorContext;
    opt_collate_clause(): Opt_collate_clauseContext;
    alter_using(): Alter_usingContext;
    replica_identity(): Replica_identityContext;
    reloptions(): ReloptionsContext;
    opt_reloptions(): Opt_reloptionsContext;
    reloption_list(): Reloption_listContext;
    reloption_elem(): Reloption_elemContext;
    alter_identity_column_option_list(): Alter_identity_column_option_listContext;
    alter_identity_column_option(): Alter_identity_column_optionContext;
    partitionboundspec(): PartitionboundspecContext;
    hash_partbound_elem(): Hash_partbound_elemContext;
    hash_partbound(): Hash_partboundContext;
    altercompositetypestmt(): AltercompositetypestmtContext;
    alter_type_cmds(): Alter_type_cmdsContext;
    alter_type_cmd(): Alter_type_cmdContext;
    closeportalstmt(): CloseportalstmtContext;
    copystmt(): CopystmtContext;
    copy_from(): Copy_fromContext;
    opt_program(): Opt_programContext;
    copy_file_name(): Copy_file_nameContext;
    copy_options(): Copy_optionsContext;
    copy_opt_list(): Copy_opt_listContext;
    copy_opt_item(): Copy_opt_itemContext;
    opt_binary(): Opt_binaryContext;
    copy_delimiter(): Copy_delimiterContext;
    opt_using(): Opt_usingContext;
    copy_generic_opt_list(): Copy_generic_opt_listContext;
    copy_generic_opt_elem(): Copy_generic_opt_elemContext;
    copy_generic_opt_arg(): Copy_generic_opt_argContext;
    copy_generic_opt_arg_list(): Copy_generic_opt_arg_listContext;
    copy_generic_opt_arg_list_item(): Copy_generic_opt_arg_list_itemContext;
    createstmt(): CreatestmtContext;
    opttemp(): OpttempContext;
    table_column_list(): Table_column_listContext;
    opttableelementlist(): OpttableelementlistContext;
    opttypedtableelementlist(): OpttypedtableelementlistContext;
    tableelementlist(): TableelementlistContext;
    typedtableelementlist(): TypedtableelementlistContext;
    tableelement(): TableelementContext;
    typedtableelement(): TypedtableelementContext;
    columnDefCluase(): ColumnDefCluaseContext;
    columnDef(): ColumnDefContext;
    compressionCluase(): CompressionCluaseContext;
    storageCluase(): StorageCluaseContext;
    columnOptions(): ColumnOptionsContext;
    colquallist(): ColquallistContext;
    colconstraint(): ColconstraintContext;
    colconstraintelem(): ColconstraintelemContext;
    nulls_distinct(): Nulls_distinctContext;
    generated_when(): Generated_whenContext;
    deferrable_trigger(): Deferrable_triggerContext;
    initially_trigger(): Initially_triggerContext;
    tablelikeclause(): TablelikeclauseContext;
    tablelikeoptionlist(): TablelikeoptionlistContext;
    tablelikeoption(): TablelikeoptionContext;
    tableconstraint(): TableconstraintContext;
    constraintelem(): ConstraintelemContext;
    opt_no_inherit(): Opt_no_inheritContext;
    opt_column_list(): Opt_column_listContext;
    columnlist(): ColumnlistContext;
    opt_c_include(): Opt_c_includeContext;
    key_match(): Key_matchContext;
    exclusionconstraintlist(): ExclusionconstraintlistContext;
    exclusionconstraintelem(): ExclusionconstraintelemContext;
    exclusionwhereclause(): ExclusionwhereclauseContext;
    key_actions(): Key_actionsContext;
    key_update(): Key_updateContext;
    key_delete(): Key_deleteContext;
    key_action(): Key_actionContext;
    optinherit(): OptinheritContext;
    optpartitionspec(): OptpartitionspecContext;
    partitionspec(): PartitionspecContext;
    part_params(): Part_paramsContext;
    part_elem(): Part_elemContext;
    table_access_method_clause(): Table_access_method_clauseContext;
    optwith(): OptwithContext;
    oncommitoption(): OncommitoptionContext;
    opttablespace(): OpttablespaceContext;
    index_paramenters_create(): Index_paramenters_createContext;
    optconstablespace(): OptconstablespaceContext;
    existingindex(): ExistingindexContext;
    createstatsstmt(): CreatestatsstmtContext;
    alterstatsstmt(): AlterstatsstmtContext;
    createasstmt(): CreateasstmtContext;
    create_as_target(): Create_as_targetContext;
    opt_with_data(): Opt_with_dataContext;
    creatematviewstmt(): CreatematviewstmtContext;
    create_mv_target(): Create_mv_targetContext;
    optnolog(): OptnologContext;
    refreshmatviewstmt(): RefreshmatviewstmtContext;
    createseqstmt(): CreateseqstmtContext;
    alterseqstmt(): AlterseqstmtContext;
    optseqoptlist(): OptseqoptlistContext;
    optparenthesizedseqoptlist(): OptparenthesizedseqoptlistContext;
    seqoptlist(): SeqoptlistContext;
    seqoptelem(): SeqoptelemContext;
    opt_by(): Opt_byContext;
    numericonly(): NumericonlyContext;
    numericonly_list(): Numericonly_listContext;
    createplangstmt(): CreateplangstmtContext;
    opt_trusted(): Opt_trustedContext;
    handler_name(): Handler_nameContext;
    opt_inline_handler(): Opt_inline_handlerContext;
    validator_clause(): Validator_clauseContext;
    opt_validator(): Opt_validatorContext;
    opt_procedural(): Opt_proceduralContext;
    createtablespacestmt(): CreatetablespacestmtContext;
    opttablespaceowner(): OpttablespaceownerContext;
    createextensionstmt(): CreateextensionstmtContext;
    create_extension_opt_list(): Create_extension_opt_listContext;
    create_extension_opt_item(): Create_extension_opt_itemContext;
    alterextensionstmt(): AlterextensionstmtContext;
    alter_extension_opt_list(): Alter_extension_opt_listContext;
    alter_extension_opt_item(): Alter_extension_opt_itemContext;
    alterextensioncontentsstmt(): AlterextensioncontentsstmtContext;
    createfdwstmt(): CreatefdwstmtContext;
    fdw_option(): Fdw_optionContext;
    fdw_options(): Fdw_optionsContext;
    opt_fdw_options(): Opt_fdw_optionsContext;
    alterfdwstmt(): AlterfdwstmtContext;
    create_generic_options(): Create_generic_optionsContext;
    generic_option_list(): Generic_option_listContext;
    alter_generic_options(): Alter_generic_optionsContext;
    alter_generic_option_list(): Alter_generic_option_listContext;
    alter_generic_option_elem(): Alter_generic_option_elemContext;
    generic_option_elem(): Generic_option_elemContext;
    generic_option_name(): Generic_option_nameContext;
    generic_option_arg(): Generic_option_argContext;
    createforeignserverstmt(): CreateforeignserverstmtContext;
    opt_type(): Opt_typeContext;
    foreign_server_version(): Foreign_server_versionContext;
    opt_foreign_server_version(): Opt_foreign_server_versionContext;
    alterforeignserverstmt(): AlterforeignserverstmtContext;
    createforeigntablestmt(): CreateforeigntablestmtContext;
    importforeignschemastmt(): ImportforeignschemastmtContext;
    import_qualification_type(): Import_qualification_typeContext;
    import_qualification(): Import_qualificationContext;
    createusermappingstmt(): CreateusermappingstmtContext;
    auth_ident(): Auth_identContext;
    alterusermappingstmt(): AlterusermappingstmtContext;
    createpolicystmt(): CreatepolicystmtContext;
    alterpolicystmt(): AlterpolicystmtContext;
    alterprocedurestmt(): AlterprocedurestmtContext;
    procedure_cluase(): Procedure_cluaseContext;
    procedure_action(): Procedure_actionContext;
    rowsecurityoptionalexpr(): RowsecurityoptionalexprContext;
    rowsecurityoptionalwithcheck(): RowsecurityoptionalwithcheckContext;
    rowsecuritydefaulttorole(): RowsecuritydefaulttoroleContext;
    rowsecurityoptionaltorole(): RowsecurityoptionaltoroleContext;
    rowsecuritydefaultpermissive(): RowsecuritydefaultpermissiveContext;
    rowsecuritydefaultforcmd(): RowsecuritydefaultforcmdContext;
    row_security_cmd(): Row_security_cmdContext;
    createamstmt(): CreateamstmtContext;
    am_type(): Am_typeContext;
    createtrigstmt(): CreatetrigstmtContext;
    triggeractiontime(): TriggeractiontimeContext;
    foreachrow(): ForeachrowContext;
    roworstatment(): RoworstatmentContext;
    triggerevents(): TriggereventsContext;
    triggeroneevent(): TriggeroneeventContext;
    triggerreferencing(): TriggerreferencingContext;
    triggertransitions(): TriggertransitionsContext;
    triggertransition(): TriggertransitionContext;
    transitionoldornew(): TransitionoldornewContext;
    transitionrowortable(): TransitionrowortableContext;
    transitionrelname(): TransitionrelnameContext;
    triggerforspec(): TriggerforspecContext;
    triggerforopteach(): TriggerforopteachContext;
    triggerfortype(): TriggerfortypeContext;
    triggerwhen(): TriggerwhenContext;
    function_or_procedure(): Function_or_procedureContext;
    triggerfuncargs(): TriggerfuncargsContext;
    triggerfuncarg(): TriggerfuncargContext;
    optconstrfromtable(): OptconstrfromtableContext;
    constraintattributespec(): ConstraintattributespecContext;
    constraintattributeElem(): ConstraintattributeElemContext;
    createeventtrigstmt(): CreateeventtrigstmtContext;
    event_trigger_when_list(): Event_trigger_when_listContext;
    event_trigger_when_item(): Event_trigger_when_itemContext;
    event_trigger_value_list(): Event_trigger_value_listContext;
    altereventtrigstmt(): AltereventtrigstmtContext;
    enable_trigger(): Enable_triggerContext;
    createassertionstmt(): CreateassertionstmtContext;
    definestmt(): DefinestmtContext;
    definition(): DefinitionContext;
    def_list(): Def_listContext;
    def_elem(): Def_elemContext;
    def_arg(): Def_argContext;
    old_aggr_definition(): Old_aggr_definitionContext;
    old_aggr_list(): Old_aggr_listContext;
    old_aggr_elem(): Old_aggr_elemContext;
    opt_enum_val_list(): Opt_enum_val_listContext;
    enum_val_list(): Enum_val_listContext;
    alterenumstmt(): AlterenumstmtContext;
    opt_if_not_exists(): Opt_if_not_existsContext;
    createopclassstmt(): CreateopclassstmtContext;
    opclass_item_list(): Opclass_item_listContext;
    opclass_item(): Opclass_itemContext;
    opt_default(): Opt_defaultContext;
    opt_opfamily(): Opt_opfamilyContext;
    opclass_purpose(): Opclass_purposeContext;
    opt_recheck(): Opt_recheckContext;
    createopfamilystmt(): CreateopfamilystmtContext;
    alteropfamilystmt(): AlteropfamilystmtContext;
    opclass_drop_list(): Opclass_drop_listContext;
    opclass_drop(): Opclass_dropContext;
    reassignownedstmt(): ReassignownedstmtContext;
    dropstmt(): DropstmtContext;
    view_nameList(): View_nameListContext;
    object_type_any_name(): Object_type_any_nameContext;
    object_type_name(): Object_type_nameContext;
    object_type_name_on_any_name(): Object_type_name_on_any_nameContext;
    any_name_list(): Any_name_listContext;
    table_column_name(): Table_column_nameContext;
    relation_column_name(): Relation_column_nameContext;
    relation_name(): Relation_nameContext;
    any_name(): Any_nameContext;
    attrs(): AttrsContext;
    type_name_list(): Type_name_listContext;
    truncatestmt(): TruncatestmtContext;
    opt_restart_seqs(): Opt_restart_seqsContext;
    commentstmt(): CommentstmtContext;
    comment_text(): Comment_textContext;
    seclabelstmt(): SeclabelstmtContext;
    opt_provider(): Opt_providerContext;
    security_label(): Security_labelContext;
    fetchstmt(): FetchstmtContext;
    fetch_args(): Fetch_argsContext;
    from_in(): From_inContext;
    opt_from_in(): Opt_from_inContext;
    grantstmt(): GrantstmtContext;
    revokestmt(): RevokestmtContext;
    privileges(): PrivilegesContext;
    beforeprivilegeselectlist(): BeforeprivilegeselectlistContext;
    beforeprivilegeselect(): BeforeprivilegeselectContext;
    privilege_list(): Privilege_listContext;
    privilege(): PrivilegeContext;
    privilege_target(): Privilege_targetContext;
    grantee_list(): Grantee_listContext;
    grantee(): GranteeContext;
    opt_grant_grant_option(): Opt_grant_grant_optionContext;
    grantrolestmt(): GrantrolestmtContext;
    revokerolestmt(): RevokerolestmtContext;
    opt_grant_admin_option(): Opt_grant_admin_optionContext;
    opt_granted_by(): Opt_granted_byContext;
    alterdefaultprivilegesstmt(): AlterdefaultprivilegesstmtContext;
    defacloptionlist(): DefacloptionlistContext;
    defacloption(): DefacloptionContext;
    defaclaction(): DefaclactionContext;
    defacl_privilege_target(): Defacl_privilege_targetContext;
    indexstmt(): IndexstmtContext;
    opt_unique(): Opt_uniqueContext;
    opt_concurrently(): Opt_concurrentlyContext;
    opt_index_name(): Opt_index_nameContext;
    access_method_clause(): Access_method_clauseContext;
    index_params(): Index_paramsContext;
    index_elem_options(): Index_elem_optionsContext;
    index_elem(): Index_elemContext;
    opt_include(): Opt_includeContext;
    index_including_params(): Index_including_paramsContext;
    opt_collate(): Opt_collateContext;
    opt_class(): Opt_classContext;
    opt_asc_desc(): Opt_asc_descContext;
    opt_nulls_order(): Opt_nulls_orderContext;
    createfunctionstmt(): CreatefunctionstmtContext;
    attrilist(): AttrilistContext;
    opt_or_replace(): Opt_or_replaceContext;
    func_args(): Func_argsContext;
    func_args_list(): Func_args_listContext;
    routine_with_argtypes_list(): Routine_with_argtypes_listContext;
    routine_with_argtypes(): Routine_with_argtypesContext;
    procedure_with_argtypes_list(): Procedure_with_argtypes_listContext;
    procedure_with_argtypes(): Procedure_with_argtypesContext;
    function_with_argtypes_list(): Function_with_argtypes_listContext;
    function_with_argtypes(): Function_with_argtypesContext;
    func_args_with_defaults(): Func_args_with_defaultsContext;
    func_args_with_defaults_list(): Func_args_with_defaults_listContext;
    func_arg(): Func_argContext;
    arg_class(): Arg_classContext;
    param_name(): Param_nameContext;
    func_return(): Func_returnContext;
    func_type(): Func_typeContext;
    func_arg_with_default(): Func_arg_with_defaultContext;
    aggr_arg(): Aggr_argContext;
    aggr_args(): Aggr_argsContext;
    aggr_args_list(): Aggr_args_listContext;
    aggregate_with_argtypes(): Aggregate_with_argtypesContext;
    aggregate_with_argtypes_list(): Aggregate_with_argtypes_listContext;
    createfunc_opt_list(): Createfunc_opt_listContext;
    common_func_opt_item(): Common_func_opt_itemContext;
    createfunc_opt_item(): Createfunc_opt_itemContext;
    transform_type_list(): Transform_type_listContext;
    opt_definition(): Opt_definitionContext;
    table_func_column(): Table_func_columnContext;
    table_func_column_list(): Table_func_column_listContext;
    alterfunctionstmt(): AlterfunctionstmtContext;
    alterFunctionTypeClause(): AlterFunctionTypeClauseContext;
    alterfunc_opt_list(): Alterfunc_opt_listContext;
    opt_restrict(): Opt_restrictContext;
    removefuncstmt(): RemovefuncstmtContext;
    removeaggrstmt(): RemoveaggrstmtContext;
    removeoperstmt(): RemoveoperstmtContext;
    oper_argtypes(): Oper_argtypesContext;
    any_operator(): Any_operatorContext;
    operator_with_argtypes_list(): Operator_with_argtypes_listContext;
    operator_with_argtypes(): Operator_with_argtypesContext;
    dostmt(): DostmtContext;
    dostmt_opt_list(): Dostmt_opt_listContext;
    dostmt_opt_item(): Dostmt_opt_itemContext;
    createcaststmt(): CreatecaststmtContext;
    cast_context(): Cast_contextContext;
    opt_if_exists(): Opt_if_existsContext;
    createtransformstmt(): CreatetransformstmtContext;
    transform_element_list(): Transform_element_listContext;
    reindexstmt(): ReindexstmtContext;
    reindex_target_type(): Reindex_target_typeContext;
    reindex_target_multitable(): Reindex_target_multitableContext;
    reindex_option_list(): Reindex_option_listContext;
    reindex_option_elem(): Reindex_option_elemContext;
    altertblspcstmt(): AltertblspcstmtContext;
    renamestmt(): RenamestmtContext;
    opt_column(): Opt_columnContext;
    opt_set_data(): Opt_set_dataContext;
    alterobjectdependsstmt(): AlterobjectdependsstmtContext;
    opt_no(): Opt_noContext;
    alterobjectschemastmt(): AlterobjectschemastmtContext;
    alteroperatorstmt(): AlteroperatorstmtContext;
    operator_def_list(): Operator_def_listContext;
    operator_def_elem(): Operator_def_elemContext;
    operator_def_arg(): Operator_def_argContext;
    altertypestmt(): AltertypestmtContext;
    alterownerstmt(): AlterownerstmtContext;
    createpublicationstmt(): CreatepublicationstmtContext;
    opt_publication_for_tables(): Opt_publication_for_tablesContext;
    publication_for_tables(): Publication_for_tablesContext;
    alterpublicationstmt(): AlterpublicationstmtContext;
    createsubscriptionstmt(): CreatesubscriptionstmtContext;
    publication_name_list(): Publication_name_listContext;
    publication_name_item(): Publication_name_itemContext;
    altersubscriptionstmt(): AltersubscriptionstmtContext;
    rulestmt(): RulestmtContext;
    ruleactionlist(): RuleactionlistContext;
    ruleactionmulti(): RuleactionmultiContext;
    ruleactionstmt(): RuleactionstmtContext;
    ruleactionstmtOrEmpty(): RuleactionstmtOrEmptyContext;
    event(): EventContext;
    opt_instead(): Opt_insteadContext;
    notifystmt(): NotifystmtContext;
    notify_payload(): Notify_payloadContext;
    listenstmt(): ListenstmtContext;
    unlistenstmt(): UnlistenstmtContext;
    transactionstmt(): TransactionstmtContext;
    opt_transaction(): Opt_transactionContext;
    transaction_mode_item(): Transaction_mode_itemContext;
    transaction_mode_list(): Transaction_mode_listContext;
    transaction_mode_list_or_empty(): Transaction_mode_list_or_emptyContext;
    opt_transaction_chain(): Opt_transaction_chainContext;
    viewstmt(): ViewstmtContext;
    opt_check_option(): Opt_check_optionContext;
    loadstmt(): LoadstmtContext;
    createdbstmt(): CreatedbstmtContext;
    createdb_opt_list(): Createdb_opt_listContext;
    createdb_opt_items(): Createdb_opt_itemsContext;
    createdb_opt_item(): Createdb_opt_itemContext;
    createdb_opt_name(): Createdb_opt_nameContext;
    opt_equal(): Opt_equalContext;
    alterdatabasestmt(): AlterdatabasestmtContext;
    alterdatabasesetstmt(): AlterdatabasesetstmtContext;
    drop_option_list(): Drop_option_listContext;
    drop_option(): Drop_optionContext;
    altercollationstmt(): AltercollationstmtContext;
    altersystemstmt(): AltersystemstmtContext;
    createdomainstmt(): CreatedomainstmtContext;
    alterdomainstmt(): AlterdomainstmtContext;
    opt_as(): Opt_asContext;
    altertsdictionarystmt(): AltertsdictionarystmtContext;
    altertsconfigurationstmt(): AltertsconfigurationstmtContext;
    any_with(): Any_withContext;
    createconversionstmt(): CreateconversionstmtContext;
    clusterstmt(): ClusterstmtContext;
    opt_verbose_list(): Opt_verbose_listContext;
    cluster_index_specification(): Cluster_index_specificationContext;
    vacuumstmt(): VacuumstmtContext;
    analyzestmt(): AnalyzestmtContext;
    vac_analyze_option_list(): Vac_analyze_option_listContext;
    analyze_keyword(): Analyze_keywordContext;
    vac_analyze_option_elem(): Vac_analyze_option_elemContext;
    vac_analyze_option_name(): Vac_analyze_option_nameContext;
    vac_analyze_option_arg(): Vac_analyze_option_argContext;
    opt_analyze(): Opt_analyzeContext;
    analyze_options_list(): Analyze_options_listContext;
    analyze_option_elem(): Analyze_option_elemContext;
    opt_verbose(): Opt_verboseContext;
    opt_skiplock(): Opt_skiplockContext;
    opt_buffer_usage_limit(): Opt_buffer_usage_limitContext;
    opt_full(): Opt_fullContext;
    opt_freeze(): Opt_freezeContext;
    opt_name_list(): Opt_name_listContext;
    vacuum_relation(): Vacuum_relationContext;
    vacuum_relation_list(): Vacuum_relation_listContext;
    opt_vacuum_relation_list(): Opt_vacuum_relation_listContext;
    explainstmt(): ExplainstmtContext;
    explainablestmt(): ExplainablestmtContext;
    explain_option_list(): Explain_option_listContext;
    explain_option_elem(): Explain_option_elemContext;
    explain_option_name(): Explain_option_nameContext;
    explain_option_arg(): Explain_option_argContext;
    preparestmt(): PreparestmtContext;
    prep_type_clause(): Prep_type_clauseContext;
    preparablestmt(): PreparablestmtContext;
    executestmt(): ExecutestmtContext;
    execute_param_clause(): Execute_param_clauseContext;
    deallocatestmt(): DeallocatestmtContext;
    insertstmt(): InsertstmtContext;
    insert_target(): Insert_targetContext;
    insert_rest(): Insert_restContext;
    override_kind(): Override_kindContext;
    insert_column_list(): Insert_column_listContext;
    insert_column_item(): Insert_column_itemContext;
    opt_on_conflict(): Opt_on_conflictContext;
    opt_conf_expr(): Opt_conf_exprContext;
    returning_clause(): Returning_clauseContext;
    deletestmt(): DeletestmtContext;
    using_clause(): Using_clauseContext;
    lockstmt(): LockstmtContext;
    opt_lock(): Opt_lockContext;
    lock_type(): Lock_typeContext;
    opt_nowait(): Opt_nowaitContext;
    opt_nowait_or_skip(): Opt_nowait_or_skipContext;
    updatestmt(): UpdatestmtContext;
    set_clause_list(): Set_clause_listContext;
    set_clause(): Set_clauseContext;
    set_target(): Set_targetContext;
    set_target_list(): Set_target_listContext;
    declarecursorstmt(): DeclarecursorstmtContext;
    cursor_name(): Cursor_nameContext;
    cursor_options(): Cursor_optionsContext;
    opt_hold(): Opt_holdContext;
    selectstmt(): SelectstmtContext;
    select_with_parens(): Select_with_parensContext;
    select_no_parens(): Select_no_parensContext;
    select_clause(): Select_clauseContext;
    simple_select(): Simple_selectContext;
    set_operator(): Set_operatorContext;
    set_operator_with_all_or_distinct(): Set_operator_with_all_or_distinctContext;
    with_clause(): With_clauseContext;
    cte_list(): Cte_listContext;
    common_table_expr(): Common_table_exprContext;
    search_cluase(): Search_cluaseContext;
    cycle_cluase(): Cycle_cluaseContext;
    opt_materialized(): Opt_materializedContext;
    opt_with_clause(): Opt_with_clauseContext;
    into_clause(): Into_clauseContext;
    opt_strict(): Opt_strictContext;
    opttempTableName(): OpttempTableNameContext;
    opt_table(): Opt_tableContext;
    all_or_distinct(): All_or_distinctContext;
    distinct_clause(): Distinct_clauseContext;
    opt_all_clause(): Opt_all_clauseContext;
    opt_sort_clause(): Opt_sort_clauseContext;
    sort_clause(): Sort_clauseContext;
    sortby_list(): Sortby_listContext;
    sortby(): SortbyContext;
    select_limit(): Select_limitContext;
    opt_select_limit(): Opt_select_limitContext;
    limit_clause(): Limit_clauseContext;
    fetch_clause(): Fetch_clauseContext;
    offset_clause(): Offset_clauseContext;
    select_limit_value(): Select_limit_valueContext;
    select_offset_value(): Select_offset_valueContext;
    select_fetch_first_value(): Select_fetch_first_valueContext;
    i_or_f_const(): I_or_f_constContext;
    row_or_rows(): Row_or_rowsContext;
    first_or_next(): First_or_nextContext;
    group_clause(): Group_clauseContext;
    group_by_list(): Group_by_listContext;
    group_by_item(): Group_by_itemContext;
    empty_grouping_set(): Empty_grouping_setContext;
    rollup_clause(): Rollup_clauseContext;
    cube_clause(): Cube_clauseContext;
    grouping_sets_clause(): Grouping_sets_clauseContext;
    having_clause(): Having_clauseContext;
    for_locking_clause(): For_locking_clauseContext;
    opt_for_locking_clause(): Opt_for_locking_clauseContext;
    for_locking_items(): For_locking_itemsContext;
    for_locking_item(): For_locking_itemContext;
    for_locking_strength(): For_locking_strengthContext;
    locked_rels_list(): Locked_rels_listContext;
    values_clause(): Values_clauseContext;
    from_clause(): From_clauseContext;
    from_list(): From_listContext;
    table_ref(): Table_refContext;
    alias_clause(): Alias_clauseContext;
    opt_alias_clause(): Opt_alias_clauseContext;
    func_alias_clause(): Func_alias_clauseContext;
    join_type(): Join_typeContext;
    join_qual(): Join_qualContext;
    relation_expr(): Relation_exprContext;
    publication_relation_expr(): Publication_relation_exprContext;
    relation_expr_list(): Relation_expr_listContext;
    publication_relation_expr_list(): Publication_relation_expr_listContext;
    relation_expr_opt_alias(): Relation_expr_opt_aliasContext;
    tablesample_clause(): Tablesample_clauseContext;
    opt_repeatable_clause(): Opt_repeatable_clauseContext;
    func_table(): Func_tableContext;
    rowsfrom_item(): Rowsfrom_itemContext;
    rowsfrom_list(): Rowsfrom_listContext;
    opt_col_def_list(): Opt_col_def_listContext;
    opt_ordinality(): Opt_ordinalityContext;
    where_clause(): Where_clauseContext;
    where_or_current_clause(): Where_or_current_clauseContext;
    opttablefuncelementlist(): OpttablefuncelementlistContext;
    tablefuncelementlist(): TablefuncelementlistContext;
    tablefuncelement(): TablefuncelementContext;
    xmltable(): XmltableContext;
    xmltable_column_list(): Xmltable_column_listContext;
    xmltable_column_el(): Xmltable_column_elContext;
    xmltable_column_option_list(): Xmltable_column_option_listContext;
    xmltable_column_option_el(): Xmltable_column_option_elContext;
    xml_namespace_list(): Xml_namespace_listContext;
    xml_namespace_el(): Xml_namespace_elContext;
    typename(): TypenameContext;
    opt_array_bounds(): Opt_array_boundsContext;
    simpletypename(): SimpletypenameContext;
    consttypename(): ConsttypenameContext;
    generictype(): GenerictypeContext;
    opt_type_modifiers(): Opt_type_modifiersContext;
    numeric(): NumericContext;
    opt_float(): Opt_floatContext;
    bit(): BitContext;
    constbit(): ConstbitContext;
    bitwithlength(): BitwithlengthContext;
    bitwithoutlength(): BitwithoutlengthContext;
    character(): CharacterContext;
    constcharacter(): ConstcharacterContext;
    character_c(): Character_cContext;
    opt_varying(): Opt_varyingContext;
    constdatetime(): ConstdatetimeContext;
    constinterval(): ConstintervalContext;
    opt_timezone(): Opt_timezoneContext;
    opt_interval(): Opt_intervalContext;
    interval_second(): Interval_secondContext;
    opt_escape(): Opt_escapeContext;
    a_expr(): A_exprContext;
    a_expr_qual(): A_expr_qualContext;
    a_expr_lessless(): A_expr_lesslessContext;
    a_expr_or(): A_expr_orContext;
    a_expr_and(): A_expr_andContext;
    a_expr_in(): A_expr_inContext;
    a_expr_unary_not(): A_expr_unary_notContext;
    a_expr_isnull(): A_expr_isnullContext;
    a_expr_is_not(): A_expr_is_notContext;
    a_expr_compare(): A_expr_compareContext;
    a_expr_like(): A_expr_likeContext;
    a_expr_qual_op(): A_expr_qual_opContext;
    a_expr_unary_qualop(): A_expr_unary_qualopContext;
    a_expr_add(): A_expr_addContext;
    a_expr_mul(): A_expr_mulContext;
    a_expr_caret(): A_expr_caretContext;
    a_expr_unary_sign(): A_expr_unary_signContext;
    a_expr_at_time_zone(): A_expr_at_time_zoneContext;
    a_expr_collate(): A_expr_collateContext;
    a_expr_typecast(): A_expr_typecastContext;
    b_expr(): B_exprContext;
    b_expr(_p: number): B_exprContext;
    c_expr(): C_exprContext;
    plsqlvariablename(): PlsqlvariablenameContext;
    func_application(): Func_applicationContext;
    func_expr(): Func_exprContext;
    func_expr_windowless(): Func_expr_windowlessContext;
    func_expr_common_subexpr(): Func_expr_common_subexprContext;
    xml_root_version(): Xml_root_versionContext;
    opt_xml_root_standalone(): Opt_xml_root_standaloneContext;
    xml_attributes(): Xml_attributesContext;
    xml_attribute_list(): Xml_attribute_listContext;
    xml_attribute_el(): Xml_attribute_elContext;
    document_or_content(): Document_or_contentContext;
    xml_whitespace_option(): Xml_whitespace_optionContext;
    xmlexists_argument(): Xmlexists_argumentContext;
    xml_passing_mech(): Xml_passing_mechContext;
    within_group_clause(): Within_group_clauseContext;
    filter_clause(): Filter_clauseContext;
    window_clause(): Window_clauseContext;
    window_definition_list(): Window_definition_listContext;
    window_definition(): Window_definitionContext;
    over_clause(): Over_clauseContext;
    window_specification(): Window_specificationContext;
    opt_existing_window_name(): Opt_existing_window_nameContext;
    opt_partition_clause(): Opt_partition_clauseContext;
    opt_frame_clause(): Opt_frame_clauseContext;
    frame_extent(): Frame_extentContext;
    frame_bound(): Frame_boundContext;
    opt_window_exclusion_clause(): Opt_window_exclusion_clauseContext;
    row(): RowContext;
    explicit_row(): Explicit_rowContext;
    implicit_row(): Implicit_rowContext;
    sub_type(): Sub_typeContext;
    all_op(): All_opContext;
    mathop(): MathopContext;
    qual_op(): Qual_opContext;
    qual_all_op(): Qual_all_opContext;
    subquery_Op(): Subquery_OpContext;
    expr_list(): Expr_listContext;
    column_expr_list_noparen(): Column_expr_list_noparenContext;
    column_expr_list(): Column_expr_listContext;
    column_expr(): Column_exprContext;
    column_expr_noparen(): Column_expr_noparenContext;
    func_arg_list(): Func_arg_listContext;
    func_arg_expr(): Func_arg_exprContext;
    type_list(): Type_listContext;
    array_expr(): Array_exprContext;
    array_expr_list(): Array_expr_listContext;
    extract_list(): Extract_listContext;
    extract_arg(): Extract_argContext;
    unicode_normal_form(): Unicode_normal_formContext;
    overlay_list(): Overlay_listContext;
    position_list(): Position_listContext;
    substr_list(): Substr_listContext;
    trim_list(): Trim_listContext;
    in_expr(): In_exprContext;
    case_expr(): Case_exprContext;
    when_clause_list(): When_clause_listContext;
    when_clause(): When_clauseContext;
    case_default(): Case_defaultContext;
    case_arg(): Case_argContext;
    columnref(): ColumnrefContext;
    indirection_el(): Indirection_elContext;
    opt_slice_bound(): Opt_slice_boundContext;
    indirection(): IndirectionContext;
    opt_indirection(): Opt_indirectionContext;
    opt_target_list(): Opt_target_listContext;
    target_list(): Target_listContext;
    target_el(): Target_elContext;
    qualified_name_list(): Qualified_name_listContext;
    table_name_list(): Table_name_listContext;
    schema_name_list(): Schema_name_listContext;
    database_nameList(): Database_nameListContext;
    procedure_name_list(): Procedure_name_listContext;
    tablespace_name_create(): Tablespace_name_createContext;
    tablespace_name(): Tablespace_nameContext;
    table_name_create(): Table_name_createContext;
    table_name(): Table_nameContext;
    view_name_create(): View_name_createContext;
    view_name(): View_nameContext;
    qualified_name(): Qualified_nameContext;
    tablespace_name_list(): Tablespace_name_listContext;
    name_list(): Name_listContext;
    database_name_create(): Database_name_createContext;
    database_name(): Database_nameContext;
    schema_name(): Schema_nameContext;
    routine_name_create(): Routine_name_createContext;
    routine_name(): Routine_nameContext;
    procedure_name(): Procedure_nameContext;
    procedure_name_create(): Procedure_name_createContext;
    column_name(): Column_nameContext;
    column_name_create(): Column_name_createContext;
    name(): NameContext;
    attr_name(): Attr_nameContext;
    file_name(): File_nameContext;
    function_name_create(): Function_name_createContext;
    function_name(): Function_nameContext;
    usual_name(): Usual_nameContext;
    aexprconst(): AexprconstContext;
    xconst(): XconstContext;
    bconst(): BconstContext;
    fconst(): FconstContext;
    iconst(): IconstContext;
    sconst(): SconstContext;
    anysconst(): AnysconstContext;
    opt_uescape(): Opt_uescapeContext;
    signediconst(): SignediconstContext;
    groupname(): GroupnameContext;
    roleid(): RoleidContext;
    rolespec(): RolespecContext;
    role_list(): Role_listContext;
    colid(): ColidContext;
    index_method_choices(): Index_method_choicesContext;
    exclude_element(): Exclude_elementContext;
    index_paramenters(): Index_paramentersContext;
    type_function_name(): Type_function_nameContext;
    type_usual_name(): Type_usual_nameContext;
    nonreservedword_column(): Nonreservedword_columnContext;
    nonreservedword(): NonreservedwordContext;
    collabel(): CollabelContext;
    identifier(): IdentifierContext;
    plsqlidentifier(): PlsqlidentifierContext;
    unreserved_keyword(): Unreserved_keywordContext;
    col_name_keyword(): Col_name_keywordContext;
    type_func_name_keyword(): Type_func_name_keywordContext;
    reserved_keyword(): Reserved_keywordContext;
    pl_function(): Pl_functionContext;
    comp_options(): Comp_optionsContext;
    comp_option(): Comp_optionContext;
    sharp(): SharpContext;
    option_value(): Option_valueContext;
    opt_semi(): Opt_semiContext;
    pl_block(): Pl_blockContext;
    decl_sect(): Decl_sectContext;
    decl_start(): Decl_startContext;
    decl_stmts(): Decl_stmtsContext;
    label_decl(): Label_declContext;
    decl_stmt(): Decl_stmtContext;
    decl_statement(): Decl_statementContext;
    opt_scrollable(): Opt_scrollableContext;
    decl_cursor_query(): Decl_cursor_queryContext;
    decl_cursor_args(): Decl_cursor_argsContext;
    decl_cursor_arglist(): Decl_cursor_arglistContext;
    decl_cursor_arg(): Decl_cursor_argContext;
    decl_is_for(): Decl_is_forContext;
    decl_aliasitem(): Decl_aliasitemContext;
    decl_varname(): Decl_varnameContext;
    decl_const(): Decl_constContext;
    decl_datatype(): Decl_datatypeContext;
    decl_collate(): Decl_collateContext;
    decl_notnull(): Decl_notnullContext;
    decl_defval(): Decl_defvalContext;
    decl_defkey(): Decl_defkeyContext;
    assign_operator(): Assign_operatorContext;
    proc_sect(): Proc_sectContext;
    proc_stmt(): Proc_stmtContext;
    stmt_perform(): Stmt_performContext;
    stmt_call(): Stmt_callContext;
    opt_expr_list(): Opt_expr_listContext;
    stmt_assign(): Stmt_assignContext;
    stmt_getdiag(): Stmt_getdiagContext;
    getdiag_area_opt(): Getdiag_area_optContext;
    getdiag_list(): Getdiag_listContext;
    getdiag_list_item(): Getdiag_list_itemContext;
    getdiag_item(): Getdiag_itemContext;
    getdiag_target(): Getdiag_targetContext;
    assign_var(): Assign_varContext;
    stmt_if(): Stmt_ifContext;
    stmt_elsifs(): Stmt_elsifsContext;
    stmt_else(): Stmt_elseContext;
    stmt_case(): Stmt_caseContext;
    opt_expr_until_when(): Opt_expr_until_whenContext;
    case_when_list(): Case_when_listContext;
    case_when(): Case_whenContext;
    opt_case_else(): Opt_case_elseContext;
    stmt_loop(): Stmt_loopContext;
    stmt_while(): Stmt_whileContext;
    stmt_for(): Stmt_forContext;
    for_control(): For_controlContext;
    opt_for_using_expression(): Opt_for_using_expressionContext;
    opt_cursor_parameters(): Opt_cursor_parametersContext;
    opt_reverse(): Opt_reverseContext;
    opt_by_expression(): Opt_by_expressionContext;
    for_variable(): For_variableContext;
    stmt_foreach_a(): Stmt_foreach_aContext;
    foreach_slice(): Foreach_sliceContext;
    stmt_exit(): Stmt_exitContext;
    exit_type(): Exit_typeContext;
    stmt_return(): Stmt_returnContext;
    opt_return_result(): Opt_return_resultContext;
    stmt_raise(): Stmt_raiseContext;
    opt_stmt_raise_level(): Opt_stmt_raise_levelContext;
    opt_raise_list(): Opt_raise_listContext;
    opt_raise_using(): Opt_raise_usingContext;
    opt_raise_using_elem(): Opt_raise_using_elemContext;
    opt_raise_using_elem_list(): Opt_raise_using_elem_listContext;
    stmt_assert(): Stmt_assertContext;
    opt_stmt_assert_message(): Opt_stmt_assert_messageContext;
    loop_body(): Loop_bodyContext;
    stmt_execsql(): Stmt_execsqlContext;
    stmt_dynexecute(): Stmt_dynexecuteContext;
    opt_execute_using(): Opt_execute_usingContext;
    opt_execute_using_list(): Opt_execute_using_listContext;
    opt_execute_into(): Opt_execute_intoContext;
    stmt_open(): Stmt_openContext;
    opt_open_bound_list_item(): Opt_open_bound_list_itemContext;
    opt_open_bound_list(): Opt_open_bound_listContext;
    opt_open_using(): Opt_open_usingContext;
    opt_scroll_option(): Opt_scroll_optionContext;
    opt_scroll_option_no(): Opt_scroll_option_noContext;
    stmt_fetch(): Stmt_fetchContext;
    into_target(): Into_targetContext;
    opt_cursor_from(): Opt_cursor_fromContext;
    opt_fetch_direction(): Opt_fetch_directionContext;
    stmt_move(): Stmt_moveContext;
    mergestmt(): MergestmtContext;
    data_source(): Data_sourceContext;
    join_condition(): Join_conditionContext;
    merge_when_clause(): Merge_when_clauseContext;
    merge_insert(): Merge_insertContext;
    merge_update(): Merge_updateContext;
    default_values_or_values(): Default_values_or_valuesContext;
    exprofdefaultlist(): ExprofdefaultlistContext;
    exprofdefault(): ExprofdefaultContext;
    stmt_close(): Stmt_closeContext;
    stmt_null(): Stmt_nullContext;
    stmt_commit(): Stmt_commitContext;
    stmt_rollback(): Stmt_rollbackContext;
    plsql_opt_transaction_chain(): Plsql_opt_transaction_chainContext;
    stmt_set(): Stmt_setContext;
    cursor_variable(): Cursor_variableContext;
    exception_sect(): Exception_sectContext;
    proc_exceptions(): Proc_exceptionsContext;
    proc_exception(): Proc_exceptionContext;
    proc_conditions(): Proc_conditionsContext;
    proc_condition(): Proc_conditionContext;
    opt_block_label(): Opt_block_labelContext;
    opt_loop_label(): Opt_loop_labelContext;
    opt_label(): Opt_labelContext;
    opt_exitcond(): Opt_exitcondContext;
    any_identifier(): Any_identifierContext;
    plsql_unreserved_keyword(): Plsql_unreserved_keywordContext;
    sql_expression(): Sql_expressionContext;
    expr_until_then(): Expr_until_thenContext;
    expr_until_semi(): Expr_until_semiContext;
    expr_until_rightbracket(): Expr_until_rightbracketContext;
    expr_until_loop(): Expr_until_loopContext;
    make_execsql_stmt(): Make_execsql_stmtContext;
    opt_returning_clause_into(): Opt_returning_clause_intoContext;
    sempred(_localctx: RuleContext, ruleIndex: number, predIndex: number): boolean;
    private b_expr_sempred;
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
    private static readonly _serializedATNSegment16;
    private static readonly _serializedATNSegment17;
    private static readonly _serializedATNSegment18;
    private static readonly _serializedATNSegment19;
    private static readonly _serializedATNSegment20;
    private static readonly _serializedATNSegment21;
    static readonly _serializedATN: string;
    static __ATN: ATN;
    static get _ATN(): ATN;
}
export declare class ProgramContext extends ParserRuleContext {
    EOF(): TerminalNode;
    singleStmt(): SingleStmtContext[];
    singleStmt(i: number): SingleStmtContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class PlsqlrootContext extends ParserRuleContext {
    pl_function(): Pl_functionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class SingleStmtContext extends ParserRuleContext {
    stmt(): StmtContext;
    SEMI(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class StmtContext extends ParserRuleContext {
    altereventtrigstmt(): AltereventtrigstmtContext | undefined;
    altercollationstmt(): AltercollationstmtContext | undefined;
    alterdatabasestmt(): AlterdatabasestmtContext | undefined;
    alterdatabasesetstmt(): AlterdatabasesetstmtContext | undefined;
    alterdefaultprivilegesstmt(): AlterdefaultprivilegesstmtContext | undefined;
    alterdomainstmt(): AlterdomainstmtContext | undefined;
    alterenumstmt(): AlterenumstmtContext | undefined;
    alterextensionstmt(): AlterextensionstmtContext | undefined;
    alterextensioncontentsstmt(): AlterextensioncontentsstmtContext | undefined;
    alterfdwstmt(): AlterfdwstmtContext | undefined;
    alterforeignserverstmt(): AlterforeignserverstmtContext | undefined;
    alterfunctionstmt(): AlterfunctionstmtContext | undefined;
    altergroupstmt(): AltergroupstmtContext | undefined;
    alterobjectdependsstmt(): AlterobjectdependsstmtContext | undefined;
    alterobjectschemastmt(): AlterobjectschemastmtContext | undefined;
    alterownerstmt(): AlterownerstmtContext | undefined;
    alteroperatorstmt(): AlteroperatorstmtContext | undefined;
    altertypestmt(): AltertypestmtContext | undefined;
    alterpolicystmt(): AlterpolicystmtContext | undefined;
    alterprocedurestmt(): AlterprocedurestmtContext | undefined;
    alterseqstmt(): AlterseqstmtContext | undefined;
    altersystemstmt(): AltersystemstmtContext | undefined;
    altertablestmt(): AltertablestmtContext | undefined;
    altertblspcstmt(): AltertblspcstmtContext | undefined;
    altercompositetypestmt(): AltercompositetypestmtContext | undefined;
    alterpublicationstmt(): AlterpublicationstmtContext | undefined;
    alterrolesetstmt(): AlterrolesetstmtContext | undefined;
    alterroutinestmt(): AlterroutinestmtContext | undefined;
    alterrolestmt(): AlterrolestmtContext | undefined;
    altersubscriptionstmt(): AltersubscriptionstmtContext | undefined;
    alterstatsstmt(): AlterstatsstmtContext | undefined;
    altertsconfigurationstmt(): AltertsconfigurationstmtContext | undefined;
    altertsdictionarystmt(): AltertsdictionarystmtContext | undefined;
    alterusermappingstmt(): AlterusermappingstmtContext | undefined;
    analyzestmt(): AnalyzestmtContext | undefined;
    callstmt(): CallstmtContext | undefined;
    checkpointstmt(): CheckpointstmtContext | undefined;
    closeportalstmt(): CloseportalstmtContext | undefined;
    clusterstmt(): ClusterstmtContext | undefined;
    commentstmt(): CommentstmtContext | undefined;
    constraintssetstmt(): ConstraintssetstmtContext | undefined;
    copystmt(): CopystmtContext | undefined;
    createamstmt(): CreateamstmtContext | undefined;
    createasstmt(): CreateasstmtContext | undefined;
    createassertionstmt(): CreateassertionstmtContext | undefined;
    createcaststmt(): CreatecaststmtContext | undefined;
    createconversionstmt(): CreateconversionstmtContext | undefined;
    createdomainstmt(): CreatedomainstmtContext | undefined;
    createextensionstmt(): CreateextensionstmtContext | undefined;
    createfdwstmt(): CreatefdwstmtContext | undefined;
    createforeignserverstmt(): CreateforeignserverstmtContext | undefined;
    createforeigntablestmt(): CreateforeigntablestmtContext | undefined;
    createfunctionstmt(): CreatefunctionstmtContext | undefined;
    creategroupstmt(): CreategroupstmtContext | undefined;
    creatematviewstmt(): CreatematviewstmtContext | undefined;
    createopclassstmt(): CreateopclassstmtContext | undefined;
    createopfamilystmt(): CreateopfamilystmtContext | undefined;
    createpublicationstmt(): CreatepublicationstmtContext | undefined;
    alteropfamilystmt(): AlteropfamilystmtContext | undefined;
    createpolicystmt(): CreatepolicystmtContext | undefined;
    createplangstmt(): CreateplangstmtContext | undefined;
    createschemastmt(): CreateschemastmtContext | undefined;
    createseqstmt(): CreateseqstmtContext | undefined;
    createstmt(): CreatestmtContext | undefined;
    createsubscriptionstmt(): CreatesubscriptionstmtContext | undefined;
    createstatsstmt(): CreatestatsstmtContext | undefined;
    createtablespacestmt(): CreatetablespacestmtContext | undefined;
    createtransformstmt(): CreatetransformstmtContext | undefined;
    createtrigstmt(): CreatetrigstmtContext | undefined;
    createeventtrigstmt(): CreateeventtrigstmtContext | undefined;
    createrolestmt(): CreaterolestmtContext | undefined;
    createuserstmt(): CreateuserstmtContext | undefined;
    createusermappingstmt(): CreateusermappingstmtContext | undefined;
    createdbstmt(): CreatedbstmtContext | undefined;
    deallocatestmt(): DeallocatestmtContext | undefined;
    declarecursorstmt(): DeclarecursorstmtContext | undefined;
    definestmt(): DefinestmtContext | undefined;
    deletestmt(): DeletestmtContext | undefined;
    discardstmt(): DiscardstmtContext | undefined;
    dostmt(): DostmtContext | undefined;
    dropstmt(): DropstmtContext | undefined;
    executestmt(): ExecutestmtContext | undefined;
    explainstmt(): ExplainstmtContext | undefined;
    fetchstmt(): FetchstmtContext | undefined;
    grantstmt(): GrantstmtContext | undefined;
    grantrolestmt(): GrantrolestmtContext | undefined;
    mergestmt(): MergestmtContext | undefined;
    importforeignschemastmt(): ImportforeignschemastmtContext | undefined;
    indexstmt(): IndexstmtContext | undefined;
    insertstmt(): InsertstmtContext | undefined;
    listenstmt(): ListenstmtContext | undefined;
    refreshmatviewstmt(): RefreshmatviewstmtContext | undefined;
    loadstmt(): LoadstmtContext | undefined;
    lockstmt(): LockstmtContext | undefined;
    notifystmt(): NotifystmtContext | undefined;
    preparestmt(): PreparestmtContext | undefined;
    reassignownedstmt(): ReassignownedstmtContext | undefined;
    reindexstmt(): ReindexstmtContext | undefined;
    removeaggrstmt(): RemoveaggrstmtContext | undefined;
    removefuncstmt(): RemovefuncstmtContext | undefined;
    removeoperstmt(): RemoveoperstmtContext | undefined;
    renamestmt(): RenamestmtContext | undefined;
    revokestmt(): RevokestmtContext | undefined;
    revokerolestmt(): RevokerolestmtContext | undefined;
    rulestmt(): RulestmtContext | undefined;
    seclabelstmt(): SeclabelstmtContext | undefined;
    selectstmt(): SelectstmtContext | undefined;
    transactionstmt(): TransactionstmtContext | undefined;
    truncatestmt(): TruncatestmtContext | undefined;
    unlistenstmt(): UnlistenstmtContext | undefined;
    updatestmt(): UpdatestmtContext | undefined;
    vacuumstmt(): VacuumstmtContext | undefined;
    variableresetstmt(): VariableresetstmtContext | undefined;
    variablesetstmt(): VariablesetstmtContext | undefined;
    variableshowstmt(): VariableshowstmtContext | undefined;
    viewstmt(): ViewstmtContext | undefined;
    plsqlconsolecommand(): PlsqlconsolecommandContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class PlsqlconsolecommandContext extends ParserRuleContext {
    MetaCommand(): TerminalNode;
    EndMetaCommand(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CallstmtContext extends ParserRuleContext {
    KW_CALL(): TerminalNode;
    func_application(): Func_applicationContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreaterolestmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_ROLE(): TerminalNode;
    roleid(): RoleidContext;
    optrolelist(): OptrolelistContext;
    opt_with(): Opt_withContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_withContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OptrolelistContext extends ParserRuleContext {
    createoptroleelem(): CreateoptroleelemContext[];
    createoptroleelem(i: number): CreateoptroleelemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlteroptrolelistContext extends ParserRuleContext {
    alteroptroleelem(): AlteroptroleelemContext[];
    alteroptroleelem(i: number): AlteroptroleelemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlteroptroleelemContext extends ParserRuleContext {
    KW_PASSWORD(): TerminalNode | undefined;
    sconst(): SconstContext | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_ENCRYPTED(): TerminalNode | undefined;
    KW_UNENCRYPTED(): TerminalNode | undefined;
    KW_INHERIT(): TerminalNode | undefined;
    KW_NOINHERIT(): TerminalNode | undefined;
    KW_CREATEUSER(): TerminalNode | undefined;
    KW_NOCREATEUSER(): TerminalNode | undefined;
    KW_CREATEROLE(): TerminalNode | undefined;
    KW_NOCREATEROLE(): TerminalNode | undefined;
    KW_CREATEDB(): TerminalNode | undefined;
    KW_NOCREATEDB(): TerminalNode | undefined;
    KW_SUPERUSER(): TerminalNode | undefined;
    KW_NOSUPERUSER(): TerminalNode | undefined;
    KW_LOGIN(): TerminalNode | undefined;
    KW_NOLOGIN(): TerminalNode | undefined;
    KW_REPLICATION(): TerminalNode | undefined;
    KW_NOREPLICATION(): TerminalNode | undefined;
    KW_BYPASSRLS(): TerminalNode | undefined;
    KW_NOBYPASSRLS(): TerminalNode | undefined;
    KW_CONNECTION(): TerminalNode | undefined;
    KW_LIMIT(): TerminalNode | undefined;
    signediconst(): SignediconstContext | undefined;
    KW_VALID(): TerminalNode | undefined;
    KW_UNTIL(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    name_list(): Name_listContext | undefined;
    KW_GROUP(): TerminalNode | undefined;
    role_list(): Role_listContext | undefined;
    KW_ADMIN(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateoptroleelemContext extends ParserRuleContext {
    alteroptroleelem(): AlteroptroleelemContext | undefined;
    KW_SYSID(): TerminalNode | undefined;
    iconst(): IconstContext | undefined;
    KW_ADMIN(): TerminalNode | undefined;
    role_list(): Role_listContext | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateuserstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_USER(): TerminalNode;
    roleid(): RoleidContext;
    optrolelist(): OptrolelistContext;
    opt_with(): Opt_withContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterrolestmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    rolespec(): RolespecContext;
    alteroptrolelist(): AlteroptrolelistContext;
    KW_ROLE(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    opt_with(): Opt_withContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_in_databaseContext extends ParserRuleContext {
    KW_IN(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    database_name(): Database_nameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterrolesetstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    opt_in_database(): Opt_in_databaseContext;
    setresetclause(): SetresetclauseContext;
    KW_ROLE(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    rolespec(): RolespecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterroutinestmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_ROUTINE(): TerminalNode;
    routine_name(): Routine_nameContext;
    alter_routine_cluase(): Alter_routine_cluaseContext;
    func_args(): Func_argsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_routine_cluaseContext extends ParserRuleContext {
    routine_action_list(): Routine_action_listContext | undefined;
    KW_RESTRICT(): TerminalNode | undefined;
    KW_RENAME(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    routine_name_create(): Routine_name_createContext | undefined;
    KW_OWNER(): TerminalNode | undefined;
    rolespec(): RolespecContext | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name_create(): Schema_name_createContext | undefined;
    KW_DEPENDS(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_EXTENSION(): TerminalNode | undefined;
    name(): NameContext | undefined;
    KW_NO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Routine_action_listContext extends ParserRuleContext {
    routine_action(): Routine_actionContext[];
    routine_action(i: number): Routine_actionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Routine_actionContext extends ParserRuleContext {
    KW_IMMUTABLE(): TerminalNode | undefined;
    KW_STABLE(): TerminalNode | undefined;
    KW_VOLATILE(): TerminalNode | undefined;
    KW_LEAKPROOF(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_SECURITY(): TerminalNode | undefined;
    KW_INVOKER(): TerminalNode | undefined;
    KW_DEFINER(): TerminalNode | undefined;
    KW_EXTERNAL(): TerminalNode | undefined;
    KW_PARALLEL(): TerminalNode | undefined;
    KW_UNSAFE(): TerminalNode | undefined;
    KW_RESTRICTED(): TerminalNode | undefined;
    KW_SAFE(): TerminalNode | undefined;
    KW_COST(): TerminalNode | undefined;
    attr_name(): Attr_nameContext | undefined;
    KW_ROWS(): TerminalNode | undefined;
    name(): NameContext[];
    name(i: number): NameContext;
    KW_SET(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    EQUAL(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_RESET(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreategroupstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_GROUP(): TerminalNode;
    groupname(): GroupnameContext;
    optrolelist(): OptrolelistContext;
    opt_with(): Opt_withContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AltergroupstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_GROUP(): TerminalNode;
    rolespec(): RolespecContext;
    add_drop(): Add_dropContext;
    KW_USER(): TerminalNode;
    role_list(): Role_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Add_dropContext extends ParserRuleContext {
    KW_ADD(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateschemastmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_SCHEMA(): TerminalNode;
    optschemaeltlist(): OptschemaeltlistContext;
    KW_AUTHORIZATION(): TerminalNode | undefined;
    rolespec(): RolespecContext | undefined;
    schema_name_create(): Schema_name_createContext | undefined;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Schema_name_createContext extends ParserRuleContext {
    colid(): ColidContext;
    attrs(): AttrsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OptschemaeltlistContext extends ParserRuleContext {
    schema_stmt(): Schema_stmtContext[];
    schema_stmt(i: number): Schema_stmtContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Schema_stmtContext extends ParserRuleContext {
    createstmt(): CreatestmtContext | undefined;
    indexstmt(): IndexstmtContext | undefined;
    createseqstmt(): CreateseqstmtContext | undefined;
    createtrigstmt(): CreatetrigstmtContext | undefined;
    grantstmt(): GrantstmtContext | undefined;
    viewstmt(): ViewstmtContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class VariablesetstmtContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    set_rest(): Set_restContext;
    KW_LOCAL(): TerminalNode | undefined;
    KW_SESSION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Set_restContext extends ParserRuleContext {
    KW_TRANSACTION(): TerminalNode | undefined;
    transaction_mode_list(): Transaction_mode_listContext | undefined;
    KW_SESSION(): TerminalNode | undefined;
    KW_CHARACTERISTICS(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    set_rest_more(): Set_rest_moreContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Generic_setContext extends ParserRuleContext {
    var_name(): Var_nameContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    var_list(): Var_listContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    EQUAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Set_rest_moreContext extends ParserRuleContext {
    generic_set(): Generic_setContext | undefined;
    var_name(): Var_nameContext | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_ZONE(): TerminalNode | undefined;
    zone_value(): Zone_valueContext | undefined;
    KW_CATALOG(): TerminalNode | undefined;
    sconst(): SconstContext | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name(): Schema_nameContext | undefined;
    KW_NAMES(): TerminalNode | undefined;
    opt_encoding(): Opt_encodingContext | undefined;
    KW_ROLE(): TerminalNode | undefined;
    nonreservedword_or_sconst(): Nonreservedword_or_sconstContext | undefined;
    KW_SESSION(): TerminalNode | undefined;
    KW_AUTHORIZATION(): TerminalNode | undefined;
    KW_XML(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    document_or_content(): Document_or_contentContext | undefined;
    KW_TRANSACTION(): TerminalNode | undefined;
    KW_SNAPSHOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Var_nameContext extends ParserRuleContext {
    colid(): ColidContext[];
    colid(i: number): ColidContext;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Var_listContext extends ParserRuleContext {
    var_value(): Var_valueContext[];
    var_value(i: number): Var_valueContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Var_valueContext extends ParserRuleContext {
    opt_boolean_or_string(): Opt_boolean_or_stringContext | undefined;
    numericonly(): NumericonlyContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Iso_levelContext extends ParserRuleContext {
    KW_READ(): TerminalNode | undefined;
    KW_UNCOMMITTED(): TerminalNode | undefined;
    KW_COMMITTED(): TerminalNode | undefined;
    KW_REPEATABLE(): TerminalNode | undefined;
    KW_SERIALIZABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_boolean_or_string_columnContext extends ParserRuleContext {
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    nonreservedword_or_sconst_column(): Nonreservedword_or_sconst_columnContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_boolean_or_stringContext extends ParserRuleContext {
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    nonreservedword_or_sconst(): Nonreservedword_or_sconstContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Zone_valueContext extends ParserRuleContext {
    sconst(): SconstContext | undefined;
    identifier(): IdentifierContext | undefined;
    constinterval(): ConstintervalContext | undefined;
    opt_interval(): Opt_intervalContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    iconst(): IconstContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    numericonly(): NumericonlyContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_encodingContext extends ParserRuleContext {
    sconst(): SconstContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Nonreservedword_or_sconst_columnContext extends ParserRuleContext {
    nonreservedword_column(): Nonreservedword_columnContext | undefined;
    sconst(): SconstContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Nonreservedword_or_sconstContext extends ParserRuleContext {
    nonreservedword(): NonreservedwordContext | undefined;
    sconst(): SconstContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class VariableresetstmtContext extends ParserRuleContext {
    KW_RESET(): TerminalNode;
    reset_rest(): Reset_restContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Reset_restContext extends ParserRuleContext {
    generic_reset(): Generic_resetContext | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_ZONE(): TerminalNode | undefined;
    KW_TRANSACTION(): TerminalNode | undefined;
    KW_ISOLATION(): TerminalNode | undefined;
    KW_LEVEL(): TerminalNode | undefined;
    KW_SESSION(): TerminalNode | undefined;
    KW_AUTHORIZATION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Generic_resetContext extends ParserRuleContext {
    var_name(): Var_nameContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class SetresetclauseContext extends ParserRuleContext {
    KW_SET(): TerminalNode | undefined;
    set_rest(): Set_restContext | undefined;
    variableresetstmt(): VariableresetstmtContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class FunctionsetresetclauseContext extends ParserRuleContext {
    KW_SET(): TerminalNode | undefined;
    set_rest_more(): Set_rest_moreContext | undefined;
    variableresetstmt(): VariableresetstmtContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class VariableshowstmtContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    var_name(): Var_nameContext | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_ZONE(): TerminalNode | undefined;
    KW_TRANSACTION(): TerminalNode | undefined;
    KW_ISOLATION(): TerminalNode | undefined;
    KW_LEVEL(): TerminalNode | undefined;
    KW_SESSION(): TerminalNode | undefined;
    KW_AUTHORIZATION(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ConstraintssetstmtContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    KW_CONSTRAINTS(): TerminalNode;
    constraints_set_list(): Constraints_set_listContext;
    constraints_set_mode(): Constraints_set_modeContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Constraints_set_listContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    qualified_name_list(): Qualified_name_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Constraints_set_modeContext extends ParserRuleContext {
    KW_DEFERRED(): TerminalNode | undefined;
    KW_IMMEDIATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CheckpointstmtContext extends ParserRuleContext {
    KW_CHECKPOINT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class DiscardstmtContext extends ParserRuleContext {
    KW_DISCARD(): TerminalNode;
    KW_ALL(): TerminalNode | undefined;
    KW_TEMP(): TerminalNode | undefined;
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_PLANS(): TerminalNode | undefined;
    KW_SEQUENCES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AltertablestmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode | undefined;
    relation_expr(): Relation_exprContext | undefined;
    alter_table_cmds(): Alter_table_cmdsContext | undefined;
    partition_cmd(): Partition_cmdContext | undefined;
    opt_if_exists(): Opt_if_existsContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_TABLESPACE(): TerminalNode[];
    KW_TABLESPACE(i: number): TerminalNode;
    tablespace_name(): Tablespace_nameContext | undefined;
    KW_SET(): TerminalNode | undefined;
    tablespace_name_create(): Tablespace_name_createContext | undefined;
    KW_OWNED(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    role_list(): Role_listContext | undefined;
    opt_nowait(): Opt_nowaitContext | undefined;
    table_name(): Table_nameContext | undefined;
    KW_ATTACH(): TerminalNode | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    qualified_name(): Qualified_nameContext | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_VALUES(): TerminalNode | undefined;
    partition_bound_spec(): Partition_bound_specContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_DETACH(): TerminalNode | undefined;
    KW_CONCURRENTLY(): TerminalNode | undefined;
    KW_FINALIZE(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    index_partition_cmd(): Index_partition_cmdContext | undefined;
    KW_SEQUENCE(): TerminalNode | undefined;
    KW_VIEW(): TerminalNode | undefined;
    view_name(): View_nameContext | undefined;
    KW_MATERIALIZED(): TerminalNode | undefined;
    KW_FOREIGN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_table_cmdsContext extends ParserRuleContext {
    alter_table_cmd(): Alter_table_cmdContext[];
    alter_table_cmd(i: number): Alter_table_cmdContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Partition_bound_specContext extends ParserRuleContext {
    KW_IN(): TerminalNode | undefined;
    opt_type_modifiers(): Opt_type_modifiersContext | undefined;
    KW_FROM(): TerminalNode | undefined;
    partition_bound_cluase(): Partition_bound_cluaseContext[];
    partition_bound_cluase(i: number): Partition_bound_cluaseContext;
    KW_TO(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    partition_with_cluase(): Partition_with_cluaseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Partition_bound_cluaseContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    partition_bound_choose(): Partition_bound_chooseContext[];
    partition_bound_choose(i: number): Partition_bound_chooseContext;
    CLOSE_PAREN(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Partition_bound_chooseContext extends ParserRuleContext {
    opt_type_modifiers(): Opt_type_modifiersContext | undefined;
    KW_MINVALUE(): TerminalNode | undefined;
    KW_MAXVALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Partition_with_cluaseContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    KW_MODULUS(): TerminalNode;
    numericonly(): NumericonlyContext[];
    numericonly(i: number): NumericonlyContext;
    COMMA(): TerminalNode;
    KW_REMAINDER(): TerminalNode;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Partition_cmdContext extends ParserRuleContext {
    KW_ATTACH(): TerminalNode | undefined;
    KW_PARTITION(): TerminalNode;
    qualified_name(): Qualified_nameContext;
    partitionboundspec(): PartitionboundspecContext | undefined;
    KW_DETACH(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Index_partition_cmdContext extends ParserRuleContext {
    KW_ATTACH(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    qualified_name(): Qualified_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_table_cmdContext extends ParserRuleContext {
    KW_ADD(): TerminalNode | undefined;
    columnDefCluase(): ColumnDefCluaseContext | undefined;
    opt_column(): Opt_columnContext | undefined;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    KW_ALTER(): TerminalNode | undefined;
    column_name(): Column_nameContext | undefined;
    alter_column_default(): Alter_column_defaultContext | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_EXPRESSION(): TerminalNode | undefined;
    opt_if_exists(): Opt_if_existsContext | undefined;
    KW_STATISTICS(): TerminalNode | undefined;
    signediconst(): SignediconstContext | undefined;
    reloptions(): ReloptionsContext | undefined;
    KW_RESET(): TerminalNode | undefined;
    KW_STORAGE(): TerminalNode | undefined;
    colid(): ColidContext | undefined;
    KW_GENERATED(): TerminalNode | undefined;
    generated_when(): Generated_whenContext | undefined;
    KW_AS(): TerminalNode | undefined;
    KW_IDENTITY(): TerminalNode | undefined;
    optparenthesizedseqoptlist(): OptparenthesizedseqoptlistContext | undefined;
    alter_identity_column_option_list(): Alter_identity_column_option_listContext | undefined;
    opt_drop_behavior(): Opt_drop_behaviorContext | undefined;
    KW_TYPE(): TerminalNode | undefined;
    typename(): TypenameContext | undefined;
    opt_set_data(): Opt_set_dataContext | undefined;
    opt_collate_clause(): Opt_collate_clauseContext | undefined;
    alter_using(): Alter_usingContext | undefined;
    alter_generic_options(): Alter_generic_optionsContext | undefined;
    tableconstraint(): TableconstraintContext | undefined;
    KW_CONSTRAINT(): TerminalNode | undefined;
    name(): NameContext | undefined;
    constraintattributespec(): ConstraintattributespecContext | undefined;
    KW_VALIDATE(): TerminalNode | undefined;
    KW_WITHOUT(): TerminalNode | undefined;
    KW_OIDS(): TerminalNode | undefined;
    KW_CLUSTER(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_LOGGED(): TerminalNode | undefined;
    KW_UNLOGGED(): TerminalNode | undefined;
    KW_ENABLE(): TerminalNode | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    KW_ALWAYS(): TerminalNode | undefined;
    KW_REPLICA(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_DISABLE(): TerminalNode | undefined;
    KW_RULE(): TerminalNode | undefined;
    KW_INHERIT(): TerminalNode | undefined;
    qualified_name(): Qualified_nameContext | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_OF(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    KW_OWNER(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    rolespec(): RolespecContext | undefined;
    KW_TABLESPACE(): TerminalNode | undefined;
    tablespace_name_create(): Tablespace_name_createContext | undefined;
    replica_identity(): Replica_identityContext | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_LEVEL(): TerminalNode | undefined;
    KW_SECURITY(): TerminalNode | undefined;
    KW_FORCE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_column_defaultContext extends ParserRuleContext {
    KW_SET(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode;
    a_expr(): A_exprContext | undefined;
    KW_DROP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_drop_behaviorContext extends ParserRuleContext {
    KW_CASCADE(): TerminalNode | undefined;
    KW_RESTRICT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_collate_clauseContext extends ParserRuleContext {
    KW_COLLATE(): TerminalNode;
    any_name(): Any_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_usingContext extends ParserRuleContext {
    KW_USING(): TerminalNode;
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Replica_identityContext extends ParserRuleContext {
    KW_NOTHING(): TerminalNode | undefined;
    KW_FULL(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_USING(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    name(): NameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ReloptionsContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    reloption_list(): Reloption_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_reloptionsContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    reloptions(): ReloptionsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Reloption_listContext extends ParserRuleContext {
    reloption_elem(): Reloption_elemContext[];
    reloption_elem(i: number): Reloption_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Reloption_elemContext extends ParserRuleContext {
    collabel(): CollabelContext[];
    collabel(i: number): CollabelContext;
    EQUAL(): TerminalNode | undefined;
    def_arg(): Def_argContext | undefined;
    DOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_identity_column_option_listContext extends ParserRuleContext {
    alter_identity_column_option(): Alter_identity_column_optionContext[];
    alter_identity_column_option(i: number): Alter_identity_column_optionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_identity_column_optionContext extends ParserRuleContext {
    KW_RESTART(): TerminalNode | undefined;
    numericonly(): NumericonlyContext | undefined;
    opt_with(): Opt_withContext | undefined;
    KW_SET(): TerminalNode | undefined;
    seqoptelem(): SeqoptelemContext | undefined;
    KW_GENERATED(): TerminalNode | undefined;
    generated_when(): Generated_whenContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class PartitionboundspecContext extends ParserRuleContext {
    KW_FOR(): TerminalNode | undefined;
    KW_VALUES(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode[];
    OPEN_PAREN(i: number): TerminalNode;
    hash_partbound(): Hash_partboundContext | undefined;
    CLOSE_PAREN(): TerminalNode[];
    CLOSE_PAREN(i: number): TerminalNode;
    KW_IN(): TerminalNode | undefined;
    expr_list(): Expr_listContext[];
    expr_list(i: number): Expr_listContext;
    KW_FROM(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Hash_partbound_elemContext extends ParserRuleContext {
    nonreservedword(): NonreservedwordContext;
    iconst(): IconstContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Hash_partboundContext extends ParserRuleContext {
    hash_partbound_elem(): Hash_partbound_elemContext[];
    hash_partbound_elem(i: number): Hash_partbound_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AltercompositetypestmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TYPE(): TerminalNode;
    any_name(): Any_nameContext;
    alter_type_cmds(): Alter_type_cmdsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_type_cmdsContext extends ParserRuleContext {
    alter_type_cmd(): Alter_type_cmdContext[];
    alter_type_cmd(i: number): Alter_type_cmdContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_type_cmdContext extends ParserRuleContext {
    KW_ADD(): TerminalNode | undefined;
    KW_ATTRIBUTE(): TerminalNode;
    tablefuncelement(): TablefuncelementContext | undefined;
    opt_drop_behavior(): Opt_drop_behaviorContext | undefined;
    KW_DROP(): TerminalNode | undefined;
    colid(): ColidContext | undefined;
    opt_if_exists(): Opt_if_existsContext | undefined;
    KW_ALTER(): TerminalNode | undefined;
    KW_TYPE(): TerminalNode | undefined;
    typename(): TypenameContext | undefined;
    opt_set_data(): Opt_set_dataContext | undefined;
    opt_collate_clause(): Opt_collate_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CloseportalstmtContext extends ParserRuleContext {
    KW_CLOSE(): TerminalNode;
    cursor_name(): Cursor_nameContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CopystmtContext extends ParserRuleContext {
    KW_COPY(): TerminalNode;
    table_name(): Table_nameContext | undefined;
    copy_from(): Copy_fromContext | undefined;
    copy_file_name(): Copy_file_nameContext;
    copy_options(): Copy_optionsContext;
    opt_binary(): Opt_binaryContext | undefined;
    opt_column_list(): Opt_column_listContext | undefined;
    opt_program(): Opt_programContext | undefined;
    copy_delimiter(): Copy_delimiterContext | undefined;
    opt_with(): Opt_withContext | undefined;
    where_clause(): Where_clauseContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    preparablestmt(): PreparablestmtContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Copy_fromContext extends ParserRuleContext {
    KW_FROM(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_programContext extends ParserRuleContext {
    KW_PROGRAM(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Copy_file_nameContext extends ParserRuleContext {
    sconst(): SconstContext | undefined;
    KW_STDIN(): TerminalNode | undefined;
    KW_STDOUT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Copy_optionsContext extends ParserRuleContext {
    copy_opt_list(): Copy_opt_listContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    copy_generic_opt_list(): Copy_generic_opt_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Copy_opt_listContext extends ParserRuleContext {
    copy_opt_item(): Copy_opt_itemContext[];
    copy_opt_item(i: number): Copy_opt_itemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Copy_opt_itemContext extends ParserRuleContext {
    KW_BINARY(): TerminalNode | undefined;
    KW_FREEZE(): TerminalNode | undefined;
    KW_DELIMITER(): TerminalNode | undefined;
    sconst(): SconstContext | undefined;
    opt_as(): Opt_asContext | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_CSV(): TerminalNode | undefined;
    KW_HEADER(): TerminalNode | undefined;
    KW_QUOTE(): TerminalNode | undefined;
    KW_ESCAPE(): TerminalNode | undefined;
    KW_FORCE(): TerminalNode | undefined;
    columnlist(): ColumnlistContext | undefined;
    STAR(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_ENCODING(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_binaryContext extends ParserRuleContext {
    KW_BINARY(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Copy_delimiterContext extends ParserRuleContext {
    KW_DELIMITERS(): TerminalNode;
    sconst(): SconstContext;
    opt_using(): Opt_usingContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_usingContext extends ParserRuleContext {
    KW_USING(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Copy_generic_opt_listContext extends ParserRuleContext {
    copy_generic_opt_elem(): Copy_generic_opt_elemContext[];
    copy_generic_opt_elem(i: number): Copy_generic_opt_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Copy_generic_opt_elemContext extends ParserRuleContext {
    collabel(): CollabelContext;
    copy_generic_opt_arg(): Copy_generic_opt_argContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Copy_generic_opt_argContext extends ParserRuleContext {
    opt_boolean_or_string(): Opt_boolean_or_stringContext | undefined;
    numericonly(): NumericonlyContext | undefined;
    STAR(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    copy_generic_opt_arg_list(): Copy_generic_opt_arg_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Copy_generic_opt_arg_listContext extends ParserRuleContext {
    copy_generic_opt_arg_list_item(): Copy_generic_opt_arg_list_itemContext[];
    copy_generic_opt_arg_list_item(i: number): Copy_generic_opt_arg_list_itemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Copy_generic_opt_arg_list_itemContext extends ParserRuleContext {
    opt_boolean_or_string_column(): Opt_boolean_or_string_columnContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatestmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    table_name_create(): Table_name_createContext;
    OPEN_PAREN(): TerminalNode | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_OF(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    qualified_name(): Qualified_nameContext | undefined;
    partitionboundspec(): PartitionboundspecContext | undefined;
    opttemp(): OpttempContext | undefined;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    table_column_list(): Table_column_listContext | undefined;
    optinherit(): OptinheritContext | undefined;
    optpartitionspec(): OptpartitionspecContext | undefined;
    table_access_method_clause(): Table_access_method_clauseContext | undefined;
    optwith(): OptwithContext | undefined;
    oncommitoption(): OncommitoptionContext | undefined;
    opttablespace(): OpttablespaceContext | undefined;
    opttypedtableelementlist(): OpttypedtableelementlistContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OpttempContext extends ParserRuleContext {
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_TEMP(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    KW_GLOBAL(): TerminalNode | undefined;
    KW_UNLOGGED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Table_column_listContext extends ParserRuleContext {
    tableelementlist(): TableelementlistContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OpttableelementlistContext extends ParserRuleContext {
    tableelementlist(): TableelementlistContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OpttypedtableelementlistContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    typedtableelementlist(): TypedtableelementlistContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TableelementlistContext extends ParserRuleContext {
    tableelement(): TableelementContext[];
    tableelement(i: number): TableelementContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TypedtableelementlistContext extends ParserRuleContext {
    typedtableelement(): TypedtableelementContext[];
    typedtableelement(i: number): TypedtableelementContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TableelementContext extends ParserRuleContext {
    columnDef(): ColumnDefContext | undefined;
    tablelikeclause(): TablelikeclauseContext | undefined;
    tableconstraint(): TableconstraintContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TypedtableelementContext extends ParserRuleContext {
    columnOptions(): ColumnOptionsContext | undefined;
    tableconstraint(): TableconstraintContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ColumnDefCluaseContext extends ParserRuleContext {
    column_name(): Column_nameContext;
    typename(): TypenameContext;
    colquallist(): ColquallistContext;
    create_generic_options(): Create_generic_optionsContext | undefined;
    storageCluase(): StorageCluaseContext | undefined;
    compressionCluase(): CompressionCluaseContext | undefined;
    KW_COLLATE(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_OPTIONS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ColumnDefContext extends ParserRuleContext {
    column_name(): Column_nameContext;
    typename(): TypenameContext;
    colquallist(): ColquallistContext;
    create_generic_options(): Create_generic_optionsContext | undefined;
    storageCluase(): StorageCluaseContext | undefined;
    compressionCluase(): CompressionCluaseContext | undefined;
    KW_COLLATE(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_OPTIONS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CompressionCluaseContext extends ParserRuleContext {
    KW_COMPRESSION(): TerminalNode;
    colid(): ColidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class StorageCluaseContext extends ParserRuleContext {
    KW_STORAGE(): TerminalNode;
    KW_PLAIN(): TerminalNode | undefined;
    KW_EXTERNAL(): TerminalNode | undefined;
    KW_EXTENDED(): TerminalNode | undefined;
    KW_MAIN(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    colid(): ColidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ColumnOptionsContext extends ParserRuleContext {
    column_name(): Column_nameContext;
    colquallist(): ColquallistContext;
    KW_WITH(): TerminalNode | undefined;
    KW_OPTIONS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ColquallistContext extends ParserRuleContext {
    colconstraint(): ColconstraintContext[];
    colconstraint(i: number): ColconstraintContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ColconstraintContext extends ParserRuleContext {
    colconstraintelem(): ColconstraintelemContext;
    KW_CONSTRAINT(): TerminalNode | undefined;
    name(): NameContext | undefined;
    deferrable_trigger(): Deferrable_triggerContext | undefined;
    initially_trigger(): Initially_triggerContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ColconstraintelemContext extends ParserRuleContext {
    KW_NOT(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_UNIQUE(): TerminalNode | undefined;
    opt_definition(): Opt_definitionContext | undefined;
    optconstablespace(): OptconstablespaceContext | undefined;
    index_paramenters_create(): Index_paramenters_createContext | undefined;
    nulls_distinct(): Nulls_distinctContext | undefined;
    KW_PRIMARY(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    KW_CHECK(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    opt_no_inherit(): Opt_no_inheritContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    b_expr(): B_exprContext | undefined;
    KW_GENERATED(): TerminalNode | undefined;
    generated_when(): Generated_whenContext | undefined;
    KW_AS(): TerminalNode | undefined;
    KW_IDENTITY(): TerminalNode | undefined;
    KW_STORED(): TerminalNode | undefined;
    optparenthesizedseqoptlist(): OptparenthesizedseqoptlistContext | undefined;
    KW_REFERENCES(): TerminalNode | undefined;
    qualified_name(): Qualified_nameContext | undefined;
    opt_column_list(): Opt_column_listContext | undefined;
    key_match(): Key_matchContext | undefined;
    key_actions(): Key_actionsContext | undefined;
    opt_collate(): Opt_collateContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Nulls_distinctContext extends ParserRuleContext {
    KW_NULLS(): TerminalNode;
    KW_DISTINCT(): TerminalNode;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Generated_whenContext extends ParserRuleContext {
    KW_ALWAYS(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Deferrable_triggerContext extends ParserRuleContext {
    KW_DEFERRABLE(): TerminalNode;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Initially_triggerContext extends ParserRuleContext {
    KW_INITIALLY(): TerminalNode;
    KW_DEFERRED(): TerminalNode | undefined;
    KW_IMMEDIATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TablelikeclauseContext extends ParserRuleContext {
    KW_LIKE(): TerminalNode;
    qualified_name(): Qualified_nameContext;
    tablelikeoptionlist(): TablelikeoptionlistContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TablelikeoptionlistContext extends ParserRuleContext {
    tablelikeoption(): TablelikeoptionContext[];
    tablelikeoption(i: number): TablelikeoptionContext;
    KW_INCLUDING(): TerminalNode[];
    KW_INCLUDING(i: number): TerminalNode;
    KW_EXCLUDING(): TerminalNode[];
    KW_EXCLUDING(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TablelikeoptionContext extends ParserRuleContext {
    KW_COMMENTS(): TerminalNode | undefined;
    KW_CONSTRAINTS(): TerminalNode | undefined;
    KW_DEFAULTS(): TerminalNode | undefined;
    KW_IDENTITY(): TerminalNode | undefined;
    KW_GENERATED(): TerminalNode | undefined;
    KW_INDEXES(): TerminalNode | undefined;
    KW_STATISTICS(): TerminalNode | undefined;
    KW_STORAGE(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TableconstraintContext extends ParserRuleContext {
    KW_CONSTRAINT(): TerminalNode | undefined;
    name(): NameContext | undefined;
    constraintelem(): ConstraintelemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ConstraintelemContext extends ParserRuleContext {
    KW_CHECK(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constraintattributespec(): ConstraintattributespecContext | undefined;
    KW_UNIQUE(): TerminalNode | undefined;
    columnlist(): ColumnlistContext | undefined;
    existingindex(): ExistingindexContext | undefined;
    opt_c_include(): Opt_c_includeContext | undefined;
    opt_definition(): Opt_definitionContext | undefined;
    optconstablespace(): OptconstablespaceContext | undefined;
    KW_PRIMARY(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    KW_EXCLUDE(): TerminalNode | undefined;
    exclusionconstraintlist(): ExclusionconstraintlistContext | undefined;
    access_method_clause(): Access_method_clauseContext | undefined;
    exclusionwhereclause(): ExclusionwhereclauseContext | undefined;
    KW_FOREIGN(): TerminalNode | undefined;
    KW_REFERENCES(): TerminalNode | undefined;
    qualified_name(): Qualified_nameContext | undefined;
    opt_column_list(): Opt_column_listContext | undefined;
    key_match(): Key_matchContext | undefined;
    key_actions(): Key_actionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_no_inheritContext extends ParserRuleContext {
    KW_NO(): TerminalNode;
    KW_INHERIT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_column_listContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    columnlist(): ColumnlistContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ColumnlistContext extends ParserRuleContext {
    column_name(): Column_nameContext[];
    column_name(i: number): Column_nameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_c_includeContext extends ParserRuleContext {
    KW_INCLUDE(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    columnlist(): ColumnlistContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Key_matchContext extends ParserRuleContext {
    KW_MATCH(): TerminalNode;
    KW_FULL(): TerminalNode | undefined;
    KW_PARTIAL(): TerminalNode | undefined;
    KW_SIMPLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ExclusionconstraintlistContext extends ParserRuleContext {
    exclusionconstraintelem(): ExclusionconstraintelemContext[];
    exclusionconstraintelem(i: number): ExclusionconstraintelemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ExclusionconstraintelemContext extends ParserRuleContext {
    index_elem(): Index_elemContext;
    KW_WITH(): TerminalNode;
    any_operator(): Any_operatorContext | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ExclusionwhereclauseContext extends ParserRuleContext {
    KW_WHERE(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    a_expr(): A_exprContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Key_actionsContext extends ParserRuleContext {
    key_update(): Key_updateContext | undefined;
    key_delete(): Key_deleteContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Key_updateContext extends ParserRuleContext {
    KW_ON(): TerminalNode;
    KW_UPDATE(): TerminalNode;
    key_action(): Key_actionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Key_deleteContext extends ParserRuleContext {
    KW_ON(): TerminalNode;
    KW_DELETE(): TerminalNode;
    key_action(): Key_actionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Key_actionContext extends ParserRuleContext {
    KW_NO(): TerminalNode | undefined;
    KW_ACTION(): TerminalNode | undefined;
    KW_RESTRICT(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    columnlist(): ColumnlistContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OptinheritContext extends ParserRuleContext {
    KW_INHERITS(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    qualified_name_list(): Qualified_name_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OptpartitionspecContext extends ParserRuleContext {
    partitionspec(): PartitionspecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class PartitionspecContext extends ParserRuleContext {
    KW_PARTITION(): TerminalNode;
    KW_BY(): TerminalNode;
    colid(): ColidContext;
    OPEN_PAREN(): TerminalNode;
    part_params(): Part_paramsContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Part_paramsContext extends ParserRuleContext {
    part_elem(): Part_elemContext[];
    part_elem(i: number): Part_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Part_elemContext extends ParserRuleContext {
    column_name(): Column_nameContext | undefined;
    opt_collate(): Opt_collateContext | undefined;
    opt_class(): Opt_classContext | undefined;
    func_expr_windowless(): Func_expr_windowlessContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Table_access_method_clauseContext extends ParserRuleContext {
    KW_USING(): TerminalNode;
    name(): NameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OptwithContext extends ParserRuleContext {
    KW_WITH(): TerminalNode | undefined;
    reloptions(): ReloptionsContext | undefined;
    KW_WITHOUT(): TerminalNode | undefined;
    KW_OIDS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OncommitoptionContext extends ParserRuleContext {
    KW_ON(): TerminalNode;
    KW_COMMIT(): TerminalNode;
    KW_DROP(): TerminalNode | undefined;
    KW_DELETE(): TerminalNode | undefined;
    KW_ROWS(): TerminalNode | undefined;
    KW_PRESERVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OpttablespaceContext extends ParserRuleContext {
    KW_TABLESPACE(): TerminalNode;
    tablespace_name(): Tablespace_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Index_paramenters_createContext extends ParserRuleContext {
    opt_include(): Opt_includeContext | undefined;
    with_clause(): With_clauseContext | undefined;
    optconstablespace(): OptconstablespaceContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OptconstablespaceContext extends ParserRuleContext {
    KW_USING(): TerminalNode;
    KW_INDEX(): TerminalNode;
    KW_TABLESPACE(): TerminalNode;
    tablespace_name(): Tablespace_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ExistingindexContext extends ParserRuleContext {
    KW_USING(): TerminalNode;
    KW_INDEX(): TerminalNode;
    name(): NameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatestatsstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_STATISTICS(): TerminalNode;
    KW_ON(): TerminalNode;
    KW_FROM(): TerminalNode;
    from_list(): From_listContext;
    column_expr_list(): Column_expr_listContext | undefined;
    expr_list(): Expr_listContext | undefined;
    any_name(): Any_nameContext | undefined;
    opt_name_list(): Opt_name_listContext | undefined;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterstatsstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_STATISTICS(): TerminalNode[];
    KW_STATISTICS(i: number): TerminalNode;
    any_name(): Any_nameContext;
    KW_SET(): TerminalNode;
    signediconst(): SignediconstContext;
    opt_if_exists(): Opt_if_existsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateasstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    create_as_target(): Create_as_targetContext;
    KW_AS(): TerminalNode;
    selectstmt(): SelectstmtContext;
    opttemp(): OpttempContext | undefined;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    opt_with_data(): Opt_with_dataContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Create_as_targetContext extends ParserRuleContext {
    table_name_create(): Table_name_createContext;
    opt_column_list(): Opt_column_listContext | undefined;
    table_access_method_clause(): Table_access_method_clauseContext | undefined;
    optwith(): OptwithContext | undefined;
    oncommitoption(): OncommitoptionContext | undefined;
    opttablespace(): OpttablespaceContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_with_dataContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    KW_DATA(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatematviewstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_MATERIALIZED(): TerminalNode;
    KW_VIEW(): TerminalNode;
    create_mv_target(): Create_mv_targetContext;
    KW_AS(): TerminalNode;
    selectstmt(): SelectstmtContext;
    optnolog(): OptnologContext | undefined;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    opt_with_data(): Opt_with_dataContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Create_mv_targetContext extends ParserRuleContext {
    view_name_create(): View_name_createContext;
    opt_column_list(): Opt_column_listContext | undefined;
    table_access_method_clause(): Table_access_method_clauseContext | undefined;
    opt_reloptions(): Opt_reloptionsContext | undefined;
    opttablespace(): OpttablespaceContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OptnologContext extends ParserRuleContext {
    KW_UNLOGGED(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RefreshmatviewstmtContext extends ParserRuleContext {
    KW_REFRESH(): TerminalNode;
    KW_MATERIALIZED(): TerminalNode;
    KW_VIEW(): TerminalNode;
    view_name(): View_nameContext;
    opt_concurrently(): Opt_concurrentlyContext | undefined;
    opt_with_data(): Opt_with_dataContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateseqstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_SEQUENCE(): TerminalNode;
    qualified_name(): Qualified_nameContext;
    opttemp(): OpttempContext | undefined;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    optseqoptlist(): OptseqoptlistContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterseqstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_SEQUENCE(): TerminalNode;
    qualified_name(): Qualified_nameContext;
    seqoptlist(): SeqoptlistContext;
    opt_if_exists(): Opt_if_existsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OptseqoptlistContext extends ParserRuleContext {
    seqoptlist(): SeqoptlistContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OptparenthesizedseqoptlistContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    seqoptlist(): SeqoptlistContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class SeqoptlistContext extends ParserRuleContext {
    seqoptelem(): SeqoptelemContext[];
    seqoptelem(i: number): SeqoptelemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class SeqoptelemContext extends ParserRuleContext {
    KW_AS(): TerminalNode | undefined;
    simpletypename(): SimpletypenameContext | undefined;
    KW_CACHE(): TerminalNode | undefined;
    numericonly(): NumericonlyContext | undefined;
    KW_CYCLE(): TerminalNode | undefined;
    KW_INCREMENT(): TerminalNode | undefined;
    opt_by(): Opt_byContext | undefined;
    KW_MAXVALUE(): TerminalNode | undefined;
    KW_MINVALUE(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_OWNED(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    table_column_name(): Table_column_nameContext | undefined;
    KW_SEQUENCE(): TerminalNode | undefined;
    KW_NAME(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    KW_START(): TerminalNode | undefined;
    opt_with(): Opt_withContext | undefined;
    KW_RESTART(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_byContext extends ParserRuleContext {
    KW_BY(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class NumericonlyContext extends ParserRuleContext {
    fconst(): FconstContext | undefined;
    PLUS(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    signediconst(): SignediconstContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Numericonly_listContext extends ParserRuleContext {
    numericonly(): NumericonlyContext[];
    numericonly(i: number): NumericonlyContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateplangstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_LANGUAGE(): TerminalNode;
    name(): NameContext;
    opt_or_replace(): Opt_or_replaceContext | undefined;
    opt_trusted(): Opt_trustedContext | undefined;
    opt_procedural(): Opt_proceduralContext | undefined;
    KW_HANDLER(): TerminalNode | undefined;
    handler_name(): Handler_nameContext | undefined;
    opt_inline_handler(): Opt_inline_handlerContext | undefined;
    opt_validator(): Opt_validatorContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_trustedContext extends ParserRuleContext {
    KW_TRUSTED(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Handler_nameContext extends ParserRuleContext {
    name(): NameContext;
    attrs(): AttrsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_inline_handlerContext extends ParserRuleContext {
    KW_INLINE(): TerminalNode;
    handler_name(): Handler_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Validator_clauseContext extends ParserRuleContext {
    KW_VALIDATOR(): TerminalNode;
    handler_name(): Handler_nameContext | undefined;
    KW_NO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_validatorContext extends ParserRuleContext {
    validator_clause(): Validator_clauseContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_proceduralContext extends ParserRuleContext {
    KW_PROCEDURAL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatetablespacestmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_TABLESPACE(): TerminalNode;
    tablespace_name(): Tablespace_nameContext;
    KW_LOCATION(): TerminalNode;
    sconst(): SconstContext;
    opttablespaceowner(): OpttablespaceownerContext | undefined;
    opt_reloptions(): Opt_reloptionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OpttablespaceownerContext extends ParserRuleContext {
    KW_OWNER(): TerminalNode;
    rolespec(): RolespecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateextensionstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_EXTENSION(): TerminalNode;
    name(): NameContext;
    create_extension_opt_list(): Create_extension_opt_listContext;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    opt_with(): Opt_withContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Create_extension_opt_listContext extends ParserRuleContext {
    create_extension_opt_item(): Create_extension_opt_itemContext[];
    create_extension_opt_item(i: number): Create_extension_opt_itemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Create_extension_opt_itemContext extends ParserRuleContext {
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name(): Schema_nameContext | undefined;
    KW_VERSION(): TerminalNode | undefined;
    nonreservedword_or_sconst(): Nonreservedword_or_sconstContext | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterextensionstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_EXTENSION(): TerminalNode;
    name(): NameContext;
    KW_UPDATE(): TerminalNode;
    alter_extension_opt_list(): Alter_extension_opt_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_extension_opt_listContext extends ParserRuleContext {
    alter_extension_opt_item(): Alter_extension_opt_itemContext[];
    alter_extension_opt_item(i: number): Alter_extension_opt_itemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_extension_opt_itemContext extends ParserRuleContext {
    KW_TO(): TerminalNode;
    nonreservedword_or_sconst(): Nonreservedword_or_sconstContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterextensioncontentsstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_EXTENSION(): TerminalNode;
    name(): NameContext[];
    name(i: number): NameContext;
    add_drop(): Add_dropContext;
    object_type_name(): Object_type_nameContext | undefined;
    object_type_any_name(): Object_type_any_nameContext | undefined;
    KW_AGGREGATE(): TerminalNode | undefined;
    aggregate_with_argtypes(): Aggregate_with_argtypesContext | undefined;
    KW_CAST(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    typename(): TypenameContext[];
    typename(i: number): TypenameContext;
    KW_AS(): TerminalNode | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_DOMAIN(): TerminalNode | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    function_with_argtypes(): Function_with_argtypesContext | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    operator_with_argtypes(): Operator_with_argtypesContext | undefined;
    KW_CLASS(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    KW_USING(): TerminalNode | undefined;
    KW_FAMILY(): TerminalNode | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    procedure_with_argtypes(): Procedure_with_argtypesContext | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    routine_with_argtypes(): Routine_with_argtypesContext | undefined;
    KW_TRANSFORM(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_LANGUAGE(): TerminalNode | undefined;
    KW_TYPE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatefdwstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_FOREIGN(): TerminalNode;
    KW_DATA(): TerminalNode;
    KW_WRAPPER(): TerminalNode;
    name(): NameContext;
    opt_fdw_options(): Opt_fdw_optionsContext | undefined;
    create_generic_options(): Create_generic_optionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Fdw_optionContext extends ParserRuleContext {
    KW_HANDLER(): TerminalNode | undefined;
    handler_name(): Handler_nameContext | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_VALIDATOR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Fdw_optionsContext extends ParserRuleContext {
    fdw_option(): Fdw_optionContext[];
    fdw_option(i: number): Fdw_optionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_fdw_optionsContext extends ParserRuleContext {
    fdw_options(): Fdw_optionsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterfdwstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_FOREIGN(): TerminalNode;
    KW_DATA(): TerminalNode;
    KW_WRAPPER(): TerminalNode;
    name(): NameContext;
    alter_generic_options(): Alter_generic_optionsContext | undefined;
    opt_fdw_options(): Opt_fdw_optionsContext | undefined;
    fdw_options(): Fdw_optionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Create_generic_optionsContext extends ParserRuleContext {
    KW_OPTIONS(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    generic_option_list(): Generic_option_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Generic_option_listContext extends ParserRuleContext {
    generic_option_elem(): Generic_option_elemContext[];
    generic_option_elem(i: number): Generic_option_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_generic_optionsContext extends ParserRuleContext {
    KW_OPTIONS(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    alter_generic_option_list(): Alter_generic_option_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_generic_option_listContext extends ParserRuleContext {
    alter_generic_option_elem(): Alter_generic_option_elemContext[];
    alter_generic_option_elem(i: number): Alter_generic_option_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alter_generic_option_elemContext extends ParserRuleContext {
    generic_option_elem(): Generic_option_elemContext;
    KW_SET(): TerminalNode | undefined;
    KW_ADD(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Generic_option_elemContext extends ParserRuleContext {
    generic_option_name(): Generic_option_nameContext;
    generic_option_arg(): Generic_option_argContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Generic_option_nameContext extends ParserRuleContext {
    collabel(): CollabelContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Generic_option_argContext extends ParserRuleContext {
    sconst(): SconstContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateforeignserverstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_SERVER(): TerminalNode;
    name(): NameContext[];
    name(i: number): NameContext;
    KW_FOREIGN(): TerminalNode;
    KW_DATA(): TerminalNode;
    KW_WRAPPER(): TerminalNode;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    opt_type(): Opt_typeContext | undefined;
    opt_foreign_server_version(): Opt_foreign_server_versionContext | undefined;
    create_generic_options(): Create_generic_optionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_typeContext extends ParserRuleContext {
    KW_TYPE(): TerminalNode;
    sconst(): SconstContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Foreign_server_versionContext extends ParserRuleContext {
    KW_VERSION(): TerminalNode;
    sconst(): SconstContext | undefined;
    KW_NULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_foreign_server_versionContext extends ParserRuleContext {
    foreign_server_version(): Foreign_server_versionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterforeignserverstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_SERVER(): TerminalNode;
    name(): NameContext;
    alter_generic_options(): Alter_generic_optionsContext | undefined;
    foreign_server_version(): Foreign_server_versionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateforeigntablestmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_FOREIGN(): TerminalNode;
    KW_TABLE(): TerminalNode;
    table_name_create(): Table_name_createContext;
    OPEN_PAREN(): TerminalNode | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_SERVER(): TerminalNode;
    name(): NameContext;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    opttableelementlist(): OpttableelementlistContext | undefined;
    optinherit(): OptinheritContext | undefined;
    create_generic_options(): Create_generic_optionsContext | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    KW_OF(): TerminalNode | undefined;
    table_name(): Table_nameContext | undefined;
    partitionboundspec(): PartitionboundspecContext | undefined;
    opttypedtableelementlist(): OpttypedtableelementlistContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ImportforeignschemastmtContext extends ParserRuleContext {
    KW_IMPORT(): TerminalNode;
    KW_FOREIGN(): TerminalNode;
    KW_SCHEMA(): TerminalNode;
    schema_name(): Schema_nameContext;
    KW_FROM(): TerminalNode;
    KW_SERVER(): TerminalNode;
    name(): NameContext[];
    name(i: number): NameContext;
    KW_INTO(): TerminalNode;
    import_qualification(): Import_qualificationContext | undefined;
    create_generic_options(): Create_generic_optionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Import_qualification_typeContext extends ParserRuleContext {
    KW_LIMIT(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    KW_EXCEPT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Import_qualificationContext extends ParserRuleContext {
    import_qualification_type(): Import_qualification_typeContext;
    OPEN_PAREN(): TerminalNode;
    relation_expr_list(): Relation_expr_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateusermappingstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_USER(): TerminalNode;
    KW_MAPPING(): TerminalNode;
    KW_FOR(): TerminalNode;
    auth_ident(): Auth_identContext;
    KW_SERVER(): TerminalNode;
    name(): NameContext;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    create_generic_options(): Create_generic_optionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Auth_identContext extends ParserRuleContext {
    rolespec(): RolespecContext | undefined;
    KW_USER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterusermappingstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_USER(): TerminalNode;
    KW_MAPPING(): TerminalNode;
    KW_FOR(): TerminalNode;
    auth_ident(): Auth_identContext;
    KW_SERVER(): TerminalNode;
    name(): NameContext;
    alter_generic_options(): Alter_generic_optionsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatepolicystmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_POLICY(): TerminalNode;
    name(): NameContext;
    KW_ON(): TerminalNode;
    qualified_name(): Qualified_nameContext;
    rowsecuritydefaultpermissive(): RowsecuritydefaultpermissiveContext | undefined;
    rowsecuritydefaultforcmd(): RowsecuritydefaultforcmdContext | undefined;
    rowsecuritydefaulttorole(): RowsecuritydefaulttoroleContext | undefined;
    rowsecurityoptionalexpr(): RowsecurityoptionalexprContext | undefined;
    rowsecurityoptionalwithcheck(): RowsecurityoptionalwithcheckContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterpolicystmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_POLICY(): TerminalNode;
    name(): NameContext;
    KW_ON(): TerminalNode;
    qualified_name(): Qualified_nameContext;
    rowsecurityoptionaltorole(): RowsecurityoptionaltoroleContext | undefined;
    rowsecurityoptionalexpr(): RowsecurityoptionalexprContext | undefined;
    rowsecurityoptionalwithcheck(): RowsecurityoptionalwithcheckContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterprocedurestmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_PROCEDURE(): TerminalNode;
    procedure_name(): Procedure_nameContext;
    procedure_cluase(): Procedure_cluaseContext;
    func_args(): Func_argsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Procedure_cluaseContext extends ParserRuleContext {
    procedure_action(): Procedure_actionContext[];
    procedure_action(i: number): Procedure_actionContext;
    KW_RESTRICT(): TerminalNode | undefined;
    KW_RENAME(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    procedure_name_create(): Procedure_name_createContext | undefined;
    KW_OWNER(): TerminalNode | undefined;
    rolespec(): RolespecContext | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name_create(): Schema_name_createContext | undefined;
    KW_DEPENDS(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_EXTENSION(): TerminalNode | undefined;
    name(): NameContext | undefined;
    KW_NO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Procedure_actionContext extends ParserRuleContext {
    KW_SECURITY(): TerminalNode | undefined;
    KW_INVOKER(): TerminalNode | undefined;
    KW_DEFINER(): TerminalNode | undefined;
    KW_EXTERNAL(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    name(): NameContext[];
    name(i: number): NameContext;
    KW_TO(): TerminalNode | undefined;
    STAR(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_RESET(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RowsecurityoptionalexprContext extends ParserRuleContext {
    KW_USING(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    a_expr(): A_exprContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RowsecurityoptionalwithcheckContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    KW_CHECK(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    a_expr(): A_exprContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RowsecuritydefaulttoroleContext extends ParserRuleContext {
    KW_TO(): TerminalNode;
    role_list(): Role_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RowsecurityoptionaltoroleContext extends ParserRuleContext {
    KW_TO(): TerminalNode;
    role_list(): Role_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RowsecuritydefaultpermissiveContext extends ParserRuleContext {
    KW_AS(): TerminalNode;
    KW_PERMISSIVE(): TerminalNode | undefined;
    KW_RESTRICTIVE(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RowsecuritydefaultforcmdContext extends ParserRuleContext {
    KW_FOR(): TerminalNode;
    row_security_cmd(): Row_security_cmdContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Row_security_cmdContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    KW_SELECT(): TerminalNode | undefined;
    KW_INSERT(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    KW_DELETE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateamstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_ACCESS(): TerminalNode;
    KW_METHOD(): TerminalNode;
    name(): NameContext;
    KW_TYPE(): TerminalNode;
    am_type(): Am_typeContext;
    KW_HANDLER(): TerminalNode;
    handler_name(): Handler_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Am_typeContext extends ParserRuleContext {
    KW_INDEX(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatetrigstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_TRIGGER(): TerminalNode;
    name(): NameContext;
    triggeractiontime(): TriggeractiontimeContext;
    triggerevents(): TriggereventsContext;
    KW_ON(): TerminalNode;
    table_name(): Table_nameContext;
    KW_EXECUTE(): TerminalNode;
    function_or_procedure(): Function_or_procedureContext;
    OPEN_PAREN(): TerminalNode;
    triggerfuncargs(): TriggerfuncargsContext;
    CLOSE_PAREN(): TerminalNode;
    opt_or_replace(): Opt_or_replaceContext | undefined;
    triggerreferencing(): TriggerreferencingContext | undefined;
    triggerforspec(): TriggerforspecContext | undefined;
    triggerwhen(): TriggerwhenContext | undefined;
    constraintattributespec(): ConstraintattributespecContext | undefined;
    KW_CONSTRAINT(): TerminalNode | undefined;
    optconstrfromtable(): OptconstrfromtableContext | undefined;
    foreachrow(): ForeachrowContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TriggeractiontimeContext extends ParserRuleContext {
    KW_BEFORE(): TerminalNode | undefined;
    KW_AFTER(): TerminalNode | undefined;
    KW_INSTEAD(): TerminalNode | undefined;
    KW_OF(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ForeachrowContext extends ParserRuleContext {
    KW_FOR(): TerminalNode;
    roworstatment(): RoworstatmentContext;
    KW_EACH(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RoworstatmentContext extends ParserRuleContext {
    KW_ROW(): TerminalNode | undefined;
    KW_STATEMENT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TriggereventsContext extends ParserRuleContext {
    triggeroneevent(): TriggeroneeventContext[];
    triggeroneevent(i: number): TriggeroneeventContext;
    KW_OR(): TerminalNode[];
    KW_OR(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TriggeroneeventContext extends ParserRuleContext {
    KW_INSERT(): TerminalNode | undefined;
    KW_DELETE(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    KW_OF(): TerminalNode | undefined;
    columnlist(): ColumnlistContext | undefined;
    KW_TRUNCATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TriggerreferencingContext extends ParserRuleContext {
    KW_REFERENCING(): TerminalNode;
    triggertransitions(): TriggertransitionsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TriggertransitionsContext extends ParserRuleContext {
    triggertransition(): TriggertransitionContext[];
    triggertransition(i: number): TriggertransitionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TriggertransitionContext extends ParserRuleContext {
    transitionoldornew(): TransitionoldornewContext;
    transitionrowortable(): TransitionrowortableContext;
    transitionrelname(): TransitionrelnameContext;
    opt_as(): Opt_asContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TransitionoldornewContext extends ParserRuleContext {
    KW_NEW(): TerminalNode | undefined;
    KW_OLD(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TransitionrowortableContext extends ParserRuleContext {
    KW_TABLE(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TransitionrelnameContext extends ParserRuleContext {
    colid(): ColidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TriggerforspecContext extends ParserRuleContext {
    KW_FOR(): TerminalNode;
    triggerfortype(): TriggerfortypeContext;
    triggerforopteach(): TriggerforopteachContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TriggerforopteachContext extends ParserRuleContext {
    KW_EACH(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TriggerfortypeContext extends ParserRuleContext {
    KW_ROW(): TerminalNode | undefined;
    KW_STATEMENT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TriggerwhenContext extends ParserRuleContext {
    KW_WHEN(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    a_expr(): A_exprContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Function_or_procedureContext extends ParserRuleContext {
    KW_FUNCTION(): TerminalNode | undefined;
    function_name(): Function_nameContext | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    procedure_name(): Procedure_nameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TriggerfuncargsContext extends ParserRuleContext {
    triggerfuncarg(): TriggerfuncargContext[];
    triggerfuncarg(i: number): TriggerfuncargContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TriggerfuncargContext extends ParserRuleContext {
    iconst(): IconstContext | undefined;
    fconst(): FconstContext | undefined;
    sconst(): SconstContext | undefined;
    collabel(): CollabelContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OptconstrfromtableContext extends ParserRuleContext {
    KW_FROM(): TerminalNode;
    qualified_name(): Qualified_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ConstraintattributespecContext extends ParserRuleContext {
    constraintattributeElem(): ConstraintattributeElemContext[];
    constraintattributeElem(i: number): ConstraintattributeElemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ConstraintattributeElemContext extends ParserRuleContext {
    KW_NOT(): TerminalNode | undefined;
    KW_DEFERRABLE(): TerminalNode | undefined;
    KW_INITIALLY(): TerminalNode | undefined;
    KW_IMMEDIATE(): TerminalNode | undefined;
    KW_DEFERRED(): TerminalNode | undefined;
    KW_VALID(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_INHERIT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateeventtrigstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_EVENT(): TerminalNode;
    KW_TRIGGER(): TerminalNode;
    name(): NameContext;
    KW_ON(): TerminalNode;
    collabel(): CollabelContext;
    KW_EXECUTE(): TerminalNode;
    function_or_procedure(): Function_or_procedureContext;
    OPEN_PAREN(): TerminalNode;
    CLOSE_PAREN(): TerminalNode;
    KW_WHEN(): TerminalNode | undefined;
    event_trigger_when_list(): Event_trigger_when_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Event_trigger_when_listContext extends ParserRuleContext {
    event_trigger_when_item(): Event_trigger_when_itemContext[];
    event_trigger_when_item(i: number): Event_trigger_when_itemContext;
    KW_AND(): TerminalNode[];
    KW_AND(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Event_trigger_when_itemContext extends ParserRuleContext {
    colid(): ColidContext;
    KW_IN(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    event_trigger_value_list(): Event_trigger_value_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Event_trigger_value_listContext extends ParserRuleContext {
    sconst(): SconstContext[];
    sconst(i: number): SconstContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AltereventtrigstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_EVENT(): TerminalNode;
    KW_TRIGGER(): TerminalNode;
    name(): NameContext;
    enable_trigger(): Enable_triggerContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Enable_triggerContext extends ParserRuleContext {
    KW_ENABLE(): TerminalNode | undefined;
    KW_REPLICA(): TerminalNode | undefined;
    KW_ALWAYS(): TerminalNode | undefined;
    KW_DISABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateassertionstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_ASSERTION(): TerminalNode;
    any_name(): Any_nameContext;
    KW_CHECK(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    a_expr(): A_exprContext;
    CLOSE_PAREN(): TerminalNode;
    constraintattributespec(): ConstraintattributespecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class DefinestmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_AGGREGATE(): TerminalNode | undefined;
    function_name(): Function_nameContext | undefined;
    aggr_args(): Aggr_argsContext | undefined;
    definition(): DefinitionContext | undefined;
    opt_or_replace(): Opt_or_replaceContext | undefined;
    old_aggr_definition(): Old_aggr_definitionContext | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    any_operator(): Any_operatorContext | undefined;
    KW_TYPE(): TerminalNode | undefined;
    any_name(): Any_nameContext[];
    any_name(i: number): Any_nameContext;
    KW_AS(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    opttablefuncelementlist(): OpttablefuncelementlistContext | undefined;
    KW_ENUM(): TerminalNode | undefined;
    opt_enum_val_list(): Opt_enum_val_listContext | undefined;
    KW_RANGE(): TerminalNode | undefined;
    KW_TEXT(): TerminalNode | undefined;
    KW_SEARCH(): TerminalNode | undefined;
    KW_PARSER(): TerminalNode | undefined;
    KW_DICTIONARY(): TerminalNode | undefined;
    KW_TEMPLATE(): TerminalNode | undefined;
    KW_CONFIGURATION(): TerminalNode | undefined;
    KW_COLLATION(): TerminalNode | undefined;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    KW_FROM(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class DefinitionContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    def_list(): Def_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Def_listContext extends ParserRuleContext {
    def_elem(): Def_elemContext[];
    def_elem(i: number): Def_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Def_elemContext extends ParserRuleContext {
    collabel(): CollabelContext;
    EQUAL(): TerminalNode | undefined;
    def_arg(): Def_argContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Def_argContext extends ParserRuleContext {
    func_type(): Func_typeContext | undefined;
    reserved_keyword(): Reserved_keywordContext | undefined;
    qual_all_op(): Qual_all_opContext | undefined;
    numericonly(): NumericonlyContext | undefined;
    sconst(): SconstContext | undefined;
    KW_NONE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Old_aggr_definitionContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    old_aggr_list(): Old_aggr_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Old_aggr_listContext extends ParserRuleContext {
    old_aggr_elem(): Old_aggr_elemContext[];
    old_aggr_elem(i: number): Old_aggr_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Old_aggr_elemContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    EQUAL(): TerminalNode;
    def_arg(): Def_argContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_enum_val_listContext extends ParserRuleContext {
    enum_val_list(): Enum_val_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Enum_val_listContext extends ParserRuleContext {
    sconst(): SconstContext[];
    sconst(i: number): SconstContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterenumstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TYPE(): TerminalNode;
    any_name(): Any_nameContext;
    KW_ADD(): TerminalNode | undefined;
    KW_VALUE(): TerminalNode;
    sconst(): SconstContext[];
    sconst(i: number): SconstContext;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    KW_BEFORE(): TerminalNode | undefined;
    KW_AFTER(): TerminalNode | undefined;
    KW_RENAME(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_if_not_existsContext extends ParserRuleContext {
    KW_IF(): TerminalNode;
    KW_NOT(): TerminalNode;
    KW_EXISTS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateopclassstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_OPERATOR(): TerminalNode;
    KW_CLASS(): TerminalNode;
    any_name(): Any_nameContext;
    KW_FOR(): TerminalNode;
    KW_TYPE(): TerminalNode;
    typename(): TypenameContext;
    KW_USING(): TerminalNode;
    name(): NameContext;
    KW_AS(): TerminalNode;
    opclass_item_list(): Opclass_item_listContext;
    opt_default(): Opt_defaultContext | undefined;
    opt_opfamily(): Opt_opfamilyContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opclass_item_listContext extends ParserRuleContext {
    opclass_item(): Opclass_itemContext[];
    opclass_item(i: number): Opclass_itemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opclass_itemContext extends ParserRuleContext {
    KW_OPERATOR(): TerminalNode | undefined;
    iconst(): IconstContext | undefined;
    any_operator(): Any_operatorContext | undefined;
    opclass_purpose(): Opclass_purposeContext | undefined;
    opt_recheck(): Opt_recheckContext | undefined;
    operator_with_argtypes(): Operator_with_argtypesContext | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    function_with_argtypes(): Function_with_argtypesContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    type_list(): Type_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_STORAGE(): TerminalNode | undefined;
    typename(): TypenameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_defaultContext extends ParserRuleContext {
    KW_DEFAULT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_opfamilyContext extends ParserRuleContext {
    KW_FAMILY(): TerminalNode;
    any_name(): Any_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opclass_purposeContext extends ParserRuleContext {
    KW_FOR(): TerminalNode;
    KW_SEARCH(): TerminalNode | undefined;
    KW_ORDER(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_recheckContext extends ParserRuleContext {
    KW_RECHECK(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateopfamilystmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_OPERATOR(): TerminalNode;
    KW_FAMILY(): TerminalNode;
    any_name(): Any_nameContext;
    KW_USING(): TerminalNode;
    name(): NameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlteropfamilystmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_OPERATOR(): TerminalNode;
    KW_FAMILY(): TerminalNode;
    any_name(): Any_nameContext;
    KW_USING(): TerminalNode;
    name(): NameContext;
    KW_ADD(): TerminalNode | undefined;
    opclass_item_list(): Opclass_item_listContext | undefined;
    KW_DROP(): TerminalNode | undefined;
    opclass_drop_list(): Opclass_drop_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opclass_drop_listContext extends ParserRuleContext {
    opclass_drop(): Opclass_dropContext[];
    opclass_drop(i: number): Opclass_dropContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opclass_dropContext extends ParserRuleContext {
    KW_OPERATOR(): TerminalNode | undefined;
    iconst(): IconstContext;
    OPEN_PAREN(): TerminalNode;
    type_list(): Type_listContext;
    CLOSE_PAREN(): TerminalNode;
    KW_FUNCTION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ReassignownedstmtContext extends ParserRuleContext {
    KW_REASSIGN(): TerminalNode;
    KW_OWNED(): TerminalNode;
    KW_BY(): TerminalNode;
    role_list(): Role_listContext;
    KW_TO(): TerminalNode;
    rolespec(): RolespecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class DropstmtContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_TABLE(): TerminalNode | undefined;
    table_name_list(): Table_name_listContext | undefined;
    opt_if_exists(): Opt_if_existsContext | undefined;
    opt_drop_behavior(): Opt_drop_behaviorContext | undefined;
    KW_SEQUENCE(): TerminalNode | undefined;
    name_list(): Name_listContext | undefined;
    KW_VIEW(): TerminalNode | undefined;
    view_nameList(): View_nameListContext | undefined;
    KW_MATERIALIZED(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    KW_FOREIGN(): TerminalNode | undefined;
    KW_COLLATION(): TerminalNode | undefined;
    KW_CONVERSION(): TerminalNode | undefined;
    KW_STATISTICS(): TerminalNode | undefined;
    KW_TEXT(): TerminalNode | undefined;
    KW_SEARCH(): TerminalNode | undefined;
    KW_PARSER(): TerminalNode | undefined;
    KW_DICTIONARY(): TerminalNode | undefined;
    KW_TEMPLATE(): TerminalNode | undefined;
    KW_CONFIGURATION(): TerminalNode | undefined;
    KW_ACCESS(): TerminalNode | undefined;
    KW_METHOD(): TerminalNode | undefined;
    KW_EVENT(): TerminalNode | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    KW_EXTENSION(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_WRAPPER(): TerminalNode | undefined;
    KW_LANGUAGE(): TerminalNode | undefined;
    opt_procedural(): Opt_proceduralContext | undefined;
    KW_PUBLICATION(): TerminalNode | undefined;
    KW_SERVER(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name_list(): Schema_name_listContext | undefined;
    KW_POLICY(): TerminalNode | undefined;
    name(): NameContext | undefined;
    KW_ON(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    KW_RULE(): TerminalNode | undefined;
    KW_TYPE(): TerminalNode | undefined;
    type_name_list(): Type_name_listContext | undefined;
    KW_DOMAIN(): TerminalNode | undefined;
    KW_CONCURRENTLY(): TerminalNode | undefined;
    any_name_list(): Any_name_listContext | undefined;
    KW_CAST(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    typename(): TypenameContext[];
    typename(i: number): TypenameContext;
    KW_AS(): TerminalNode | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    KW_CLASS(): TerminalNode | undefined;
    KW_USING(): TerminalNode | undefined;
    KW_FAMILY(): TerminalNode | undefined;
    KW_OWNED(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    role_list(): Role_listContext | undefined;
    KW_SUBSCRIPTION(): TerminalNode | undefined;
    KW_TABLESPACE(): TerminalNode | undefined;
    tablespace_name(): Tablespace_nameContext | undefined;
    KW_TRANSFORM(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_MAPPING(): TerminalNode | undefined;
    auth_ident(): Auth_identContext | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    database_name(): Database_nameContext | undefined;
    drop_option_list(): Drop_option_listContext | undefined;
    opt_with(): Opt_withContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class View_nameListContext extends ParserRuleContext {
    view_name(): View_nameContext[];
    view_name(i: number): View_nameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Object_type_any_nameContext extends ParserRuleContext {
    KW_TABLE(): TerminalNode | undefined;
    table_name(): Table_nameContext | undefined;
    KW_SEQUENCE(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    KW_VIEW(): TerminalNode | undefined;
    view_name(): View_nameContext | undefined;
    KW_MATERIALIZED(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    KW_FOREIGN(): TerminalNode | undefined;
    KW_COLLATION(): TerminalNode | undefined;
    KW_CONVERSION(): TerminalNode | undefined;
    KW_STATISTICS(): TerminalNode | undefined;
    KW_TEXT(): TerminalNode | undefined;
    KW_SEARCH(): TerminalNode | undefined;
    KW_PARSER(): TerminalNode | undefined;
    KW_DICTIONARY(): TerminalNode | undefined;
    KW_TEMPLATE(): TerminalNode | undefined;
    KW_CONFIGURATION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Object_type_nameContext extends ParserRuleContext {
    KW_ACCESS(): TerminalNode | undefined;
    KW_METHOD(): TerminalNode | undefined;
    name(): NameContext | undefined;
    KW_EVENT(): TerminalNode | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    KW_EXTENSION(): TerminalNode | undefined;
    KW_FOREIGN(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_WRAPPER(): TerminalNode | undefined;
    KW_LANGUAGE(): TerminalNode | undefined;
    opt_procedural(): Opt_proceduralContext | undefined;
    KW_PUBLICATION(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name(): Schema_nameContext | undefined;
    KW_SERVER(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    database_name(): Database_nameContext | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_SUBSCRIPTION(): TerminalNode | undefined;
    KW_TABLESPACE(): TerminalNode | undefined;
    tablespace_name(): Tablespace_nameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Object_type_name_on_any_nameContext extends ParserRuleContext {
    KW_POLICY(): TerminalNode | undefined;
    KW_RULE(): TerminalNode | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Any_name_listContext extends ParserRuleContext {
    any_name(): Any_nameContext[];
    any_name(i: number): Any_nameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Table_column_nameContext extends ParserRuleContext {
    table_name(): Table_nameContext;
    DOT(): TerminalNode;
    column_name(): Column_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Relation_column_nameContext extends ParserRuleContext {
    relation_name(): Relation_nameContext;
    DOT(): TerminalNode;
    column_name(): Column_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Relation_nameContext extends ParserRuleContext {
    colid(): ColidContext;
    attrs(): AttrsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Any_nameContext extends ParserRuleContext {
    colid(): ColidContext;
    attrs(): AttrsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AttrsContext extends ParserRuleContext {
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    attr_name(): Attr_nameContext[];
    attr_name(i: number): Attr_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Type_name_listContext extends ParserRuleContext {
    typename(): TypenameContext[];
    typename(i: number): TypenameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TruncatestmtContext extends ParserRuleContext {
    KW_TRUNCATE(): TerminalNode;
    relation_expr_list(): Relation_expr_listContext;
    opt_table(): Opt_tableContext | undefined;
    opt_restart_seqs(): Opt_restart_seqsContext | undefined;
    opt_drop_behavior(): Opt_drop_behaviorContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_restart_seqsContext extends ParserRuleContext {
    KW_CONTINUE(): TerminalNode | undefined;
    KW_IDENTITY(): TerminalNode;
    KW_RESTART(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CommentstmtContext extends ParserRuleContext {
    KW_COMMENT(): TerminalNode;
    KW_ON(): TerminalNode[];
    KW_ON(i: number): TerminalNode;
    object_type_any_name(): Object_type_any_nameContext | undefined;
    KW_IS(): TerminalNode;
    comment_text(): Comment_textContext;
    KW_COLUMN(): TerminalNode | undefined;
    relation_column_name(): Relation_column_nameContext | undefined;
    object_type_name(): Object_type_nameContext | undefined;
    KW_TYPE(): TerminalNode | undefined;
    typename(): TypenameContext[];
    typename(i: number): TypenameContext;
    KW_DOMAIN(): TerminalNode | undefined;
    KW_AGGREGATE(): TerminalNode | undefined;
    aggregate_with_argtypes(): Aggregate_with_argtypesContext | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    function_with_argtypes(): Function_with_argtypesContext | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    operator_with_argtypes(): Operator_with_argtypesContext | undefined;
    KW_CONSTRAINT(): TerminalNode | undefined;
    name(): NameContext | undefined;
    table_name(): Table_nameContext | undefined;
    any_name(): Any_nameContext | undefined;
    KW_POLICY(): TerminalNode | undefined;
    KW_RULE(): TerminalNode | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    procedure_with_argtypes(): Procedure_with_argtypesContext | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    routine_with_argtypes(): Routine_with_argtypesContext | undefined;
    KW_TRANSFORM(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_LANGUAGE(): TerminalNode | undefined;
    KW_CLASS(): TerminalNode | undefined;
    KW_USING(): TerminalNode | undefined;
    KW_FAMILY(): TerminalNode | undefined;
    KW_LARGE(): TerminalNode | undefined;
    KW_OBJECT(): TerminalNode | undefined;
    numericonly(): NumericonlyContext | undefined;
    KW_CAST(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Comment_textContext extends ParserRuleContext {
    sconst(): SconstContext | undefined;
    KW_NULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class SeclabelstmtContext extends ParserRuleContext {
    KW_SECURITY(): TerminalNode;
    KW_LABEL(): TerminalNode;
    KW_ON(): TerminalNode;
    object_type_any_name(): Object_type_any_nameContext | undefined;
    KW_IS(): TerminalNode;
    security_label(): Security_labelContext;
    opt_provider(): Opt_providerContext | undefined;
    KW_COLUMN(): TerminalNode | undefined;
    table_column_name(): Table_column_nameContext | undefined;
    object_type_name(): Object_type_nameContext | undefined;
    KW_TYPE(): TerminalNode | undefined;
    typename(): TypenameContext | undefined;
    KW_DOMAIN(): TerminalNode | undefined;
    KW_AGGREGATE(): TerminalNode | undefined;
    aggregate_with_argtypes(): Aggregate_with_argtypesContext | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    function_with_argtypes(): Function_with_argtypesContext | undefined;
    KW_LARGE(): TerminalNode | undefined;
    KW_OBJECT(): TerminalNode | undefined;
    numericonly(): NumericonlyContext | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    procedure_with_argtypes(): Procedure_with_argtypesContext | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    routine_with_argtypes(): Routine_with_argtypesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_providerContext extends ParserRuleContext {
    KW_FOR(): TerminalNode;
    nonreservedword_or_sconst(): Nonreservedword_or_sconstContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Security_labelContext extends ParserRuleContext {
    sconst(): SconstContext | undefined;
    KW_NULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class FetchstmtContext extends ParserRuleContext {
    KW_FETCH(): TerminalNode | undefined;
    fetch_args(): Fetch_argsContext;
    KW_MOVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Fetch_argsContext extends ParserRuleContext {
    cursor_name(): Cursor_nameContext;
    from_in(): From_inContext | undefined;
    KW_NEXT(): TerminalNode | undefined;
    opt_from_in(): Opt_from_inContext | undefined;
    KW_PRIOR(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    KW_ABSOLUTE(): TerminalNode | undefined;
    signediconst(): SignediconstContext | undefined;
    KW_RELATIVE(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_FORWARD(): TerminalNode | undefined;
    KW_BACKWARD(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class From_inContext extends ParserRuleContext {
    KW_FROM(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_from_inContext extends ParserRuleContext {
    from_in(): From_inContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class GrantstmtContext extends ParserRuleContext {
    KW_GRANT(): TerminalNode;
    privileges(): PrivilegesContext;
    KW_ON(): TerminalNode;
    privilege_target(): Privilege_targetContext;
    KW_TO(): TerminalNode;
    grantee_list(): Grantee_listContext;
    opt_grant_grant_option(): Opt_grant_grant_optionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RevokestmtContext extends ParserRuleContext {
    KW_REVOKE(): TerminalNode;
    privileges(): PrivilegesContext;
    KW_ON(): TerminalNode;
    privilege_target(): Privilege_targetContext;
    KW_FROM(): TerminalNode;
    grantee_list(): Grantee_listContext;
    opt_drop_behavior(): Opt_drop_behaviorContext | undefined;
    KW_GRANT(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class PrivilegesContext extends ParserRuleContext {
    privilege_list(): Privilege_listContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_PRIVILEGES(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    columnlist(): ColumnlistContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    beforeprivilegeselectlist(): BeforeprivilegeselectlistContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class BeforeprivilegeselectlistContext extends ParserRuleContext {
    beforeprivilegeselect(): BeforeprivilegeselectContext[];
    beforeprivilegeselect(i: number): BeforeprivilegeselectContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class BeforeprivilegeselectContext extends ParserRuleContext {
    KW_SELECT(): TerminalNode | undefined;
    KW_INSERT(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    KW_DELETE(): TerminalNode | undefined;
    KW_TRUNCATE(): TerminalNode | undefined;
    KW_PEFERENCES(): TerminalNode | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    KW_USAGE(): TerminalNode | undefined;
    KW_CREATE(): TerminalNode | undefined;
    KW_CONNECT(): TerminalNode | undefined;
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_TEMP(): TerminalNode | undefined;
    KW_EXECUTE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Privilege_listContext extends ParserRuleContext {
    privilege(): PrivilegeContext[];
    privilege(i: number): PrivilegeContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class PrivilegeContext extends ParserRuleContext {
    KW_SELECT(): TerminalNode | undefined;
    opt_column_list(): Opt_column_listContext | undefined;
    KW_REFERENCES(): TerminalNode | undefined;
    KW_CREATE(): TerminalNode | undefined;
    colid(): ColidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Privilege_targetContext extends ParserRuleContext {
    qualified_name_list(): Qualified_name_listContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    table_name_list(): Table_name_listContext | undefined;
    KW_SEQUENCE(): TerminalNode | undefined;
    KW_FOREIGN(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_WRAPPER(): TerminalNode | undefined;
    name_list(): Name_listContext | undefined;
    KW_SERVER(): TerminalNode | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    function_with_argtypes_list(): Function_with_argtypes_listContext | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    procedure_with_argtypes_list(): Procedure_with_argtypes_listContext | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    routine_with_argtypes_list(): Routine_with_argtypes_listContext | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    database_nameList(): Database_nameListContext | undefined;
    KW_DOMAIN(): TerminalNode | undefined;
    any_name_list(): Any_name_listContext | undefined;
    KW_LANGUAGE(): TerminalNode | undefined;
    KW_LARGE(): TerminalNode | undefined;
    KW_OBJECT(): TerminalNode | undefined;
    numericonly_list(): Numericonly_listContext | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name_list(): Schema_name_listContext | undefined;
    KW_TABLESPACE(): TerminalNode | undefined;
    tablespace_name_list(): Tablespace_name_listContext | undefined;
    KW_TYPE(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_TABLES(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_SEQUENCES(): TerminalNode | undefined;
    KW_FUNCTIONS(): TerminalNode | undefined;
    KW_PROCEDURES(): TerminalNode | undefined;
    KW_ROUTINES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Grantee_listContext extends ParserRuleContext {
    grantee(): GranteeContext[];
    grantee(i: number): GranteeContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class GranteeContext extends ParserRuleContext {
    rolespec(): RolespecContext;
    KW_GROUP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_grant_grant_optionContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    KW_GRANT(): TerminalNode;
    KW_OPTION(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class GrantrolestmtContext extends ParserRuleContext {
    KW_GRANT(): TerminalNode;
    privilege_list(): Privilege_listContext;
    KW_TO(): TerminalNode;
    role_list(): Role_listContext;
    opt_grant_admin_option(): Opt_grant_admin_optionContext | undefined;
    opt_granted_by(): Opt_granted_byContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RevokerolestmtContext extends ParserRuleContext {
    KW_REVOKE(): TerminalNode;
    privilege_list(): Privilege_listContext;
    KW_FROM(): TerminalNode;
    role_list(): Role_listContext;
    opt_granted_by(): Opt_granted_byContext | undefined;
    opt_drop_behavior(): Opt_drop_behaviorContext | undefined;
    KW_ADMIN(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_grant_admin_optionContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    KW_ADMIN(): TerminalNode;
    KW_OPTION(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_granted_byContext extends ParserRuleContext {
    KW_GRANTED(): TerminalNode;
    KW_BY(): TerminalNode;
    rolespec(): RolespecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterdefaultprivilegesstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_DEFAULT(): TerminalNode;
    KW_PRIVILEGES(): TerminalNode;
    defacloptionlist(): DefacloptionlistContext;
    defaclaction(): DefaclactionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class DefacloptionlistContext extends ParserRuleContext {
    defacloption(): DefacloptionContext[];
    defacloption(i: number): DefacloptionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class DefacloptionContext extends ParserRuleContext {
    KW_IN(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name_list(): Schema_name_listContext | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    role_list(): Role_listContext | undefined;
    KW_USER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class DefaclactionContext extends ParserRuleContext {
    KW_GRANT(): TerminalNode | undefined;
    privileges(): PrivilegesContext;
    KW_ON(): TerminalNode;
    defacl_privilege_target(): Defacl_privilege_targetContext;
    KW_TO(): TerminalNode | undefined;
    grantee_list(): Grantee_listContext;
    opt_grant_grant_option(): Opt_grant_grant_optionContext | undefined;
    KW_REVOKE(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    opt_drop_behavior(): Opt_drop_behaviorContext | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Defacl_privilege_targetContext extends ParserRuleContext {
    KW_TABLES(): TerminalNode | undefined;
    KW_FUNCTIONS(): TerminalNode | undefined;
    KW_ROUTINES(): TerminalNode | undefined;
    KW_SEQUENCES(): TerminalNode | undefined;
    KW_TYPES(): TerminalNode | undefined;
    KW_SCHEMAS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class IndexstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_INDEX(): TerminalNode;
    KW_ON(): TerminalNode;
    relation_expr(): Relation_exprContext;
    OPEN_PAREN(): TerminalNode;
    index_params(): Index_paramsContext;
    CLOSE_PAREN(): TerminalNode;
    opt_unique(): Opt_uniqueContext | undefined;
    opt_concurrently(): Opt_concurrentlyContext | undefined;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    opt_index_name(): Opt_index_nameContext | undefined;
    access_method_clause(): Access_method_clauseContext | undefined;
    opt_include(): Opt_includeContext | undefined;
    nulls_distinct(): Nulls_distinctContext | undefined;
    opt_reloptions(): Opt_reloptionsContext | undefined;
    opttablespace(): OpttablespaceContext | undefined;
    where_clause(): Where_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_uniqueContext extends ParserRuleContext {
    KW_UNIQUE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_concurrentlyContext extends ParserRuleContext {
    KW_CONCURRENTLY(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_index_nameContext extends ParserRuleContext {
    name(): NameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Access_method_clauseContext extends ParserRuleContext {
    KW_USING(): TerminalNode;
    name(): NameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Index_paramsContext extends ParserRuleContext {
    index_elem(): Index_elemContext[];
    index_elem(i: number): Index_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Index_elem_optionsContext extends ParserRuleContext {
    opt_collate(): Opt_collateContext | undefined;
    opt_class(): Opt_classContext | undefined;
    opt_asc_desc(): Opt_asc_descContext | undefined;
    opt_nulls_order(): Opt_nulls_orderContext | undefined;
    any_name(): Any_nameContext | undefined;
    reloptions(): ReloptionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Index_elemContext extends ParserRuleContext {
    column_name(): Column_nameContext | undefined;
    index_elem_options(): Index_elem_optionsContext;
    func_expr_windowless(): Func_expr_windowlessContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_includeContext extends ParserRuleContext {
    KW_INCLUDE(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    index_including_params(): Index_including_paramsContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Index_including_paramsContext extends ParserRuleContext {
    index_elem(): Index_elemContext[];
    index_elem(i: number): Index_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_collateContext extends ParserRuleContext {
    KW_COLLATE(): TerminalNode;
    any_name(): Any_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_classContext extends ParserRuleContext {
    any_name(): Any_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_asc_descContext extends ParserRuleContext {
    KW_ASC(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_nulls_orderContext extends ParserRuleContext {
    KW_NULLS(): TerminalNode;
    KW_FIRST(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatefunctionstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    func_args_with_defaults(): Func_args_with_defaultsContext;
    createfunc_opt_list(): Createfunc_opt_listContext;
    KW_FUNCTION(): TerminalNode | undefined;
    function_name_create(): Function_name_createContext | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    procedure_name_create(): Procedure_name_createContext | undefined;
    opt_or_replace(): Opt_or_replaceContext | undefined;
    KW_RETURNS(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    attrilist(): AttrilistContext | undefined;
    func_return(): Func_returnContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    table_func_column_list(): Table_func_column_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AttrilistContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    colid(): ColidContext[];
    colid(i: number): ColidContext;
    CLOSE_PAREN(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_or_replaceContext extends ParserRuleContext {
    KW_OR(): TerminalNode;
    KW_REPLACE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_argsContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    CLOSE_PAREN(): TerminalNode;
    func_args_list(): Func_args_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_args_listContext extends ParserRuleContext {
    func_arg(): Func_argContext[];
    func_arg(i: number): Func_argContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Routine_with_argtypes_listContext extends ParserRuleContext {
    routine_with_argtypes(): Routine_with_argtypesContext[];
    routine_with_argtypes(i: number): Routine_with_argtypesContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Routine_with_argtypesContext extends ParserRuleContext {
    routine_name(): Routine_nameContext | undefined;
    func_args(): Func_argsContext | undefined;
    type_func_name_keyword(): Type_func_name_keywordContext | undefined;
    colid(): ColidContext | undefined;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Procedure_with_argtypes_listContext extends ParserRuleContext {
    procedure_with_argtypes(): Procedure_with_argtypesContext[];
    procedure_with_argtypes(i: number): Procedure_with_argtypesContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Procedure_with_argtypesContext extends ParserRuleContext {
    procedure_name(): Procedure_nameContext | undefined;
    func_args(): Func_argsContext | undefined;
    type_func_name_keyword(): Type_func_name_keywordContext | undefined;
    colid(): ColidContext | undefined;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Function_with_argtypes_listContext extends ParserRuleContext {
    function_with_argtypes(): Function_with_argtypesContext[];
    function_with_argtypes(i: number): Function_with_argtypesContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Function_with_argtypesContext extends ParserRuleContext {
    function_name(): Function_nameContext | undefined;
    func_args(): Func_argsContext | undefined;
    type_func_name_keyword(): Type_func_name_keywordContext | undefined;
    colid(): ColidContext | undefined;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_args_with_defaultsContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    CLOSE_PAREN(): TerminalNode;
    func_args_with_defaults_list(): Func_args_with_defaults_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_args_with_defaults_listContext extends ParserRuleContext {
    func_arg_with_default(): Func_arg_with_defaultContext[];
    func_arg_with_default(i: number): Func_arg_with_defaultContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_argContext extends ParserRuleContext {
    arg_class(): Arg_classContext | undefined;
    func_type(): Func_typeContext;
    param_name(): Param_nameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Arg_classContext extends ParserRuleContext {
    KW_IN(): TerminalNode | undefined;
    KW_OUT(): TerminalNode | undefined;
    KW_INOUT(): TerminalNode | undefined;
    KW_VARIADIC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Param_nameContext extends ParserRuleContext {
    type_function_name(): Type_function_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_returnContext extends ParserRuleContext {
    func_type(): Func_typeContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_typeContext extends ParserRuleContext {
    typename(): TypenameContext | undefined;
    type_function_name(): Type_function_nameContext | undefined;
    attrs(): AttrsContext | undefined;
    PERCENT(): TerminalNode | undefined;
    KW_TYPE(): TerminalNode | undefined;
    KW_SETOF(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_arg_with_defaultContext extends ParserRuleContext {
    func_arg(): Func_argContext;
    a_expr(): A_exprContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    EQUAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Aggr_argContext extends ParserRuleContext {
    func_arg(): Func_argContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Aggr_argsContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    CLOSE_PAREN(): TerminalNode;
    STAR(): TerminalNode | undefined;
    aggr_args_list(): Aggr_args_listContext[];
    aggr_args_list(i: number): Aggr_args_listContext;
    KW_ORDER(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Aggr_args_listContext extends ParserRuleContext {
    aggr_arg(): Aggr_argContext[];
    aggr_arg(i: number): Aggr_argContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Aggregate_with_argtypesContext extends ParserRuleContext {
    function_name(): Function_nameContext;
    aggr_args(): Aggr_argsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Aggregate_with_argtypes_listContext extends ParserRuleContext {
    aggregate_with_argtypes(): Aggregate_with_argtypesContext[];
    aggregate_with_argtypes(i: number): Aggregate_with_argtypesContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Createfunc_opt_listContext extends ParserRuleContext {
    createfunc_opt_item(): Createfunc_opt_itemContext[];
    createfunc_opt_item(i: number): Createfunc_opt_itemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Common_func_opt_itemContext extends ParserRuleContext {
    KW_CALLED(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_NULL(): TerminalNode[];
    KW_NULL(i: number): TerminalNode;
    KW_INPUT(): TerminalNode | undefined;
    KW_RETURNS(): TerminalNode | undefined;
    KW_STRICT(): TerminalNode | undefined;
    KW_IMMUTABLE(): TerminalNode | undefined;
    KW_STABLE(): TerminalNode | undefined;
    KW_VOLATILE(): TerminalNode | undefined;
    KW_EXTERNAL(): TerminalNode | undefined;
    KW_SECURITY(): TerminalNode | undefined;
    KW_DEFINER(): TerminalNode | undefined;
    KW_INVOKER(): TerminalNode | undefined;
    KW_LEAKPROOF(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_COST(): TerminalNode | undefined;
    numericonly(): NumericonlyContext | undefined;
    KW_ROWS(): TerminalNode | undefined;
    KW_SUPPORT(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    functionsetresetclause(): FunctionsetresetclauseContext | undefined;
    KW_PARALLEL(): TerminalNode | undefined;
    colid(): ColidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Createfunc_opt_itemContext extends ParserRuleContext {
    KW_AS(): TerminalNode | undefined;
    sconst(): SconstContext[];
    sconst(i: number): SconstContext;
    COMMA(): TerminalNode | undefined;
    KW_LANGUAGE(): TerminalNode | undefined;
    nonreservedword_or_sconst(): Nonreservedword_or_sconstContext | undefined;
    KW_TRANSFORM(): TerminalNode | undefined;
    transform_type_list(): Transform_type_listContext | undefined;
    KW_WINDOW(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    colid(): ColidContext[];
    colid(i: number): ColidContext;
    KW_TO(): TerminalNode | undefined;
    EQUAL(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    stmt(): StmtContext | undefined;
    common_func_opt_item(): Common_func_opt_itemContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Transform_type_listContext extends ParserRuleContext {
    KW_FOR(): TerminalNode[];
    KW_FOR(i: number): TerminalNode;
    KW_TYPE(): TerminalNode[];
    KW_TYPE(i: number): TerminalNode;
    typename(): TypenameContext[];
    typename(i: number): TypenameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_definitionContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    definition(): DefinitionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Table_func_columnContext extends ParserRuleContext {
    column_name(): Column_nameContext;
    func_type(): Func_typeContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Table_func_column_listContext extends ParserRuleContext {
    table_func_column(): Table_func_columnContext[];
    table_func_column(i: number): Table_func_columnContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterfunctionstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    alterFunctionTypeClause(): AlterFunctionTypeClauseContext;
    alterfunc_opt_list(): Alterfunc_opt_listContext;
    opt_restrict(): Opt_restrictContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterFunctionTypeClauseContext extends ParserRuleContext {
    KW_FUNCTION(): TerminalNode | undefined;
    function_with_argtypes(): Function_with_argtypesContext | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    procedure_with_argtypes(): Procedure_with_argtypesContext | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    routine_with_argtypes(): Routine_with_argtypesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alterfunc_opt_listContext extends ParserRuleContext {
    common_func_opt_item(): Common_func_opt_itemContext[];
    common_func_opt_item(i: number): Common_func_opt_itemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_restrictContext extends ParserRuleContext {
    KW_RESTRICT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RemovefuncstmtContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_FUNCTION(): TerminalNode | undefined;
    function_with_argtypes_list(): Function_with_argtypes_listContext | undefined;
    opt_if_exists(): Opt_if_existsContext | undefined;
    opt_drop_behavior(): Opt_drop_behaviorContext | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    procedure_with_argtypes_list(): Procedure_with_argtypes_listContext | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    routine_with_argtypes_list(): Routine_with_argtypes_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RemoveaggrstmtContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_AGGREGATE(): TerminalNode;
    aggregate_with_argtypes_list(): Aggregate_with_argtypes_listContext;
    opt_if_exists(): Opt_if_existsContext | undefined;
    opt_drop_behavior(): Opt_drop_behaviorContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RemoveoperstmtContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_OPERATOR(): TerminalNode;
    operator_with_argtypes_list(): Operator_with_argtypes_listContext;
    opt_if_exists(): Opt_if_existsContext | undefined;
    opt_drop_behavior(): Opt_drop_behaviorContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Oper_argtypesContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    typename(): TypenameContext[];
    typename(i: number): TypenameContext;
    CLOSE_PAREN(): TerminalNode;
    COMMA(): TerminalNode | undefined;
    KW_NONE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Any_operatorContext extends ParserRuleContext {
    all_op(): All_opContext;
    colid(): ColidContext[];
    colid(i: number): ColidContext;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Operator_with_argtypes_listContext extends ParserRuleContext {
    operator_with_argtypes(): Operator_with_argtypesContext[];
    operator_with_argtypes(i: number): Operator_with_argtypesContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Operator_with_argtypesContext extends ParserRuleContext {
    any_operator(): Any_operatorContext;
    oper_argtypes(): Oper_argtypesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class DostmtContext extends ParserRuleContext {
    KW_DO(): TerminalNode;
    dostmt_opt_list(): Dostmt_opt_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Dostmt_opt_listContext extends ParserRuleContext {
    dostmt_opt_item(): Dostmt_opt_itemContext[];
    dostmt_opt_item(i: number): Dostmt_opt_itemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Dostmt_opt_itemContext extends ParserRuleContext {
    sconst(): SconstContext | undefined;
    KW_LANGUAGE(): TerminalNode | undefined;
    nonreservedword_or_sconst(): Nonreservedword_or_sconstContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatecaststmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_CAST(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    typename(): TypenameContext[];
    typename(i: number): TypenameContext;
    KW_AS(): TerminalNode;
    CLOSE_PAREN(): TerminalNode;
    KW_WITH(): TerminalNode | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    function_with_argtypes(): Function_with_argtypesContext | undefined;
    cast_context(): Cast_contextContext | undefined;
    KW_WITHOUT(): TerminalNode | undefined;
    KW_INOUT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Cast_contextContext extends ParserRuleContext {
    KW_AS(): TerminalNode;
    KW_IMPLICIT(): TerminalNode | undefined;
    KW_ASSIGNMENT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_if_existsContext extends ParserRuleContext {
    KW_IF(): TerminalNode;
    KW_EXISTS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatetransformstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_TRANSFORM(): TerminalNode;
    KW_FOR(): TerminalNode;
    typename(): TypenameContext;
    KW_LANGUAGE(): TerminalNode;
    name(): NameContext;
    OPEN_PAREN(): TerminalNode;
    transform_element_list(): Transform_element_listContext;
    CLOSE_PAREN(): TerminalNode;
    opt_or_replace(): Opt_or_replaceContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Transform_element_listContext extends ParserRuleContext {
    KW_FROM(): TerminalNode | undefined;
    KW_SQL(): TerminalNode[];
    KW_SQL(i: number): TerminalNode;
    KW_WITH(): TerminalNode[];
    KW_WITH(i: number): TerminalNode;
    KW_FUNCTION(): TerminalNode[];
    KW_FUNCTION(i: number): TerminalNode;
    function_with_argtypes(): Function_with_argtypesContext[];
    function_with_argtypes(i: number): Function_with_argtypesContext;
    COMMA(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ReindexstmtContext extends ParserRuleContext {
    KW_REINDEX(): TerminalNode;
    reindex_target_type(): Reindex_target_typeContext | undefined;
    reindex_target_multitable(): Reindex_target_multitableContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    reindex_option_list(): Reindex_option_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Reindex_target_typeContext extends ParserRuleContext {
    KW_INDEX(): TerminalNode | undefined;
    qualified_name(): Qualified_nameContext | undefined;
    opt_concurrently(): Opt_concurrentlyContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    table_name(): Table_nameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Reindex_target_multitableContext extends ParserRuleContext {
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name(): Schema_nameContext | undefined;
    opt_concurrently(): Opt_concurrentlyContext | undefined;
    KW_SYSTEM(): TerminalNode | undefined;
    name(): NameContext | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    database_name(): Database_nameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Reindex_option_listContext extends ParserRuleContext {
    reindex_option_elem(): Reindex_option_elemContext[];
    reindex_option_elem(i: number): Reindex_option_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Reindex_option_elemContext extends ParserRuleContext {
    KW_VERBOSE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AltertblspcstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLESPACE(): TerminalNode;
    tablespace_name(): Tablespace_nameContext;
    KW_SET(): TerminalNode | undefined;
    reloptions(): ReloptionsContext;
    KW_RESET(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RenamestmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_AGGREGATE(): TerminalNode | undefined;
    aggregate_with_argtypes(): Aggregate_with_argtypesContext | undefined;
    KW_RENAME(): TerminalNode;
    KW_TO(): TerminalNode;
    name(): NameContext[];
    name(i: number): NameContext;
    KW_COLLATION(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    KW_CONVERSION(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    database_name(): Database_nameContext | undefined;
    database_name_create(): Database_name_createContext | undefined;
    KW_DOMAIN(): TerminalNode | undefined;
    KW_CONSTRAINT(): TerminalNode | undefined;
    KW_FOREIGN(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_WRAPPER(): TerminalNode | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    function_with_argtypes(): Function_with_argtypesContext | undefined;
    function_name_create(): Function_name_createContext | undefined;
    KW_GROUP(): TerminalNode | undefined;
    roleid(): RoleidContext[];
    roleid(i: number): RoleidContext;
    KW_LANGUAGE(): TerminalNode | undefined;
    opt_procedural(): Opt_proceduralContext | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    KW_CLASS(): TerminalNode | undefined;
    KW_USING(): TerminalNode | undefined;
    KW_FAMILY(): TerminalNode | undefined;
    KW_POLICY(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    qualified_name(): Qualified_nameContext | undefined;
    opt_if_exists(): Opt_if_existsContext | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    procedure_with_argtypes(): Procedure_with_argtypesContext | undefined;
    procedure_name_create(): Procedure_name_createContext | undefined;
    KW_PUBLICATION(): TerminalNode | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    routine_with_argtypes(): Routine_with_argtypesContext | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name(): Schema_nameContext | undefined;
    schema_name_create(): Schema_name_createContext | undefined;
    KW_SERVER(): TerminalNode | undefined;
    KW_SUBSCRIPTION(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    relation_expr(): Relation_exprContext | undefined;
    table_name_create(): Table_name_createContext | undefined;
    KW_SEQUENCE(): TerminalNode | undefined;
    KW_VIEW(): TerminalNode | undefined;
    view_name(): View_nameContext | undefined;
    view_name_create(): View_name_createContext | undefined;
    KW_MATERIALIZED(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    column_name(): Column_nameContext | undefined;
    column_name_create(): Column_name_createContext | undefined;
    opt_column(): Opt_columnContext | undefined;
    KW_RULE(): TerminalNode | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    KW_EVENT(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_TABLESPACE(): TerminalNode | undefined;
    tablespace_name(): Tablespace_nameContext | undefined;
    tablespace_name_create(): Tablespace_name_createContext | undefined;
    KW_STATISTICS(): TerminalNode | undefined;
    KW_TEXT(): TerminalNode | undefined;
    KW_SEARCH(): TerminalNode | undefined;
    KW_PARSER(): TerminalNode | undefined;
    KW_DICTIONARY(): TerminalNode | undefined;
    KW_TEMPLATE(): TerminalNode | undefined;
    KW_CONFIGURATION(): TerminalNode | undefined;
    KW_TYPE(): TerminalNode | undefined;
    KW_ATTRIBUTE(): TerminalNode | undefined;
    opt_drop_behavior(): Opt_drop_behaviorContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_columnContext extends ParserRuleContext {
    KW_COLUMN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_set_dataContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    KW_DATA(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterobjectdependsstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_FUNCTION(): TerminalNode | undefined;
    function_with_argtypes(): Function_with_argtypesContext | undefined;
    KW_DEPENDS(): TerminalNode;
    KW_ON(): TerminalNode[];
    KW_ON(i: number): TerminalNode;
    KW_EXTENSION(): TerminalNode;
    name(): NameContext[];
    name(i: number): NameContext;
    opt_no(): Opt_noContext | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    procedure_with_argtypes(): Procedure_with_argtypesContext | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    routine_with_argtypes(): Routine_with_argtypesContext | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    qualified_name(): Qualified_nameContext | undefined;
    KW_MATERIALIZED(): TerminalNode | undefined;
    KW_VIEW(): TerminalNode | undefined;
    view_name(): View_nameContext | undefined;
    KW_INDEX(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_noContext extends ParserRuleContext {
    KW_NO(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterobjectschemastmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_AGGREGATE(): TerminalNode | undefined;
    aggregate_with_argtypes(): Aggregate_with_argtypesContext | undefined;
    KW_SET(): TerminalNode;
    KW_SCHEMA(): TerminalNode;
    schema_name_create(): Schema_name_createContext;
    KW_COLLATION(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    KW_CONVERSION(): TerminalNode | undefined;
    KW_DOMAIN(): TerminalNode | undefined;
    KW_EXTENSION(): TerminalNode | undefined;
    name(): NameContext | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    function_with_argtypes(): Function_with_argtypesContext | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    operator_with_argtypes(): Operator_with_argtypesContext | undefined;
    KW_CLASS(): TerminalNode | undefined;
    KW_USING(): TerminalNode | undefined;
    KW_FAMILY(): TerminalNode | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    procedure_with_argtypes(): Procedure_with_argtypesContext | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    routine_with_argtypes(): Routine_with_argtypesContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    relation_expr(): Relation_exprContext | undefined;
    opt_if_exists(): Opt_if_existsContext | undefined;
    KW_STATISTICS(): TerminalNode | undefined;
    KW_TEXT(): TerminalNode | undefined;
    KW_SEARCH(): TerminalNode | undefined;
    KW_PARSER(): TerminalNode | undefined;
    KW_DICTIONARY(): TerminalNode | undefined;
    KW_TEMPLATE(): TerminalNode | undefined;
    KW_CONFIGURATION(): TerminalNode | undefined;
    KW_SEQUENCE(): TerminalNode | undefined;
    qualified_name(): Qualified_nameContext | undefined;
    KW_VIEW(): TerminalNode | undefined;
    view_name(): View_nameContext | undefined;
    KW_MATERIALIZED(): TerminalNode | undefined;
    KW_FOREIGN(): TerminalNode | undefined;
    KW_TYPE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlteroperatorstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_OPERATOR(): TerminalNode;
    operator_with_argtypes(): Operator_with_argtypesContext;
    KW_SET(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    operator_def_list(): Operator_def_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Operator_def_listContext extends ParserRuleContext {
    operator_def_elem(): Operator_def_elemContext[];
    operator_def_elem(i: number): Operator_def_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Operator_def_elemContext extends ParserRuleContext {
    collabel(): CollabelContext;
    EQUAL(): TerminalNode;
    KW_NONE(): TerminalNode | undefined;
    operator_def_arg(): Operator_def_argContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Operator_def_argContext extends ParserRuleContext {
    func_type(): Func_typeContext | undefined;
    reserved_keyword(): Reserved_keywordContext | undefined;
    qual_all_op(): Qual_all_opContext | undefined;
    numericonly(): NumericonlyContext | undefined;
    sconst(): SconstContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AltertypestmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TYPE(): TerminalNode;
    any_name(): Any_nameContext;
    KW_SET(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    operator_def_list(): Operator_def_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterownerstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_AGGREGATE(): TerminalNode | undefined;
    aggregate_with_argtypes(): Aggregate_with_argtypesContext | undefined;
    KW_OWNER(): TerminalNode;
    KW_TO(): TerminalNode;
    rolespec(): RolespecContext;
    KW_COLLATION(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    KW_CONVERSION(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    database_name(): Database_nameContext | undefined;
    KW_DOMAIN(): TerminalNode | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    function_with_argtypes(): Function_with_argtypesContext | undefined;
    KW_LANGUAGE(): TerminalNode | undefined;
    name(): NameContext | undefined;
    opt_procedural(): Opt_proceduralContext | undefined;
    KW_LARGE(): TerminalNode | undefined;
    KW_OBJECT(): TerminalNode | undefined;
    numericonly(): NumericonlyContext | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    operator_with_argtypes(): Operator_with_argtypesContext | undefined;
    KW_CLASS(): TerminalNode | undefined;
    KW_USING(): TerminalNode | undefined;
    KW_FAMILY(): TerminalNode | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    procedure_with_argtypes(): Procedure_with_argtypesContext | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    routine_with_argtypes(): Routine_with_argtypesContext | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name(): Schema_nameContext | undefined;
    KW_TYPE(): TerminalNode | undefined;
    KW_TABLESPACE(): TerminalNode | undefined;
    tablespace_name(): Tablespace_nameContext | undefined;
    KW_STATISTICS(): TerminalNode | undefined;
    KW_TEXT(): TerminalNode | undefined;
    KW_SEARCH(): TerminalNode | undefined;
    KW_DICTIONARY(): TerminalNode | undefined;
    KW_CONFIGURATION(): TerminalNode | undefined;
    KW_FOREIGN(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_WRAPPER(): TerminalNode | undefined;
    KW_SERVER(): TerminalNode | undefined;
    KW_EVENT(): TerminalNode | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    KW_PUBLICATION(): TerminalNode | undefined;
    KW_SUBSCRIPTION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatepublicationstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_PUBLICATION(): TerminalNode;
    name(): NameContext;
    opt_publication_for_tables(): Opt_publication_for_tablesContext | undefined;
    opt_definition(): Opt_definitionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_publication_for_tablesContext extends ParserRuleContext {
    publication_for_tables(): Publication_for_tablesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Publication_for_tablesContext extends ParserRuleContext {
    KW_FOR(): TerminalNode;
    KW_TABLE(): TerminalNode | undefined;
    relation_expr_list(): Relation_expr_listContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_TABLES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterpublicationstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_PUBLICATION(): TerminalNode;
    name(): NameContext[];
    name(i: number): NameContext;
    KW_SET(): TerminalNode | undefined;
    definition(): DefinitionContext | undefined;
    KW_ADD(): TerminalNode | undefined;
    publication_relation_expr_list(): Publication_relation_expr_listContext | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_OWNER(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    rolespec(): RolespecContext | undefined;
    KW_RENAME(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatesubscriptionstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_SUBSCRIPTION(): TerminalNode;
    name(): NameContext;
    KW_CONNECTION(): TerminalNode;
    sconst(): SconstContext;
    KW_PUBLICATION(): TerminalNode;
    publication_name_list(): Publication_name_listContext;
    opt_definition(): Opt_definitionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Publication_name_listContext extends ParserRuleContext {
    publication_name_item(): Publication_name_itemContext[];
    publication_name_item(i: number): Publication_name_itemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Publication_name_itemContext extends ParserRuleContext {
    collabel(): CollabelContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AltersubscriptionstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_SUBSCRIPTION(): TerminalNode;
    name(): NameContext;
    KW_SET(): TerminalNode | undefined;
    definition(): DefinitionContext | undefined;
    KW_CONNECTION(): TerminalNode | undefined;
    sconst(): SconstContext | undefined;
    KW_REFRESH(): TerminalNode | undefined;
    KW_PUBLICATION(): TerminalNode | undefined;
    opt_definition(): Opt_definitionContext | undefined;
    publication_name_list(): Publication_name_listContext | undefined;
    KW_ADD(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_ENABLE(): TerminalNode | undefined;
    KW_DISABLE(): TerminalNode | undefined;
    KW_SKIP(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    old_aggr_elem(): Old_aggr_elemContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_OWNER(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    rolespec(): RolespecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RulestmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_RULE(): TerminalNode;
    name(): NameContext;
    KW_AS(): TerminalNode;
    KW_ON(): TerminalNode;
    event(): EventContext;
    KW_TO(): TerminalNode;
    qualified_name(): Qualified_nameContext;
    KW_DO(): TerminalNode;
    ruleactionlist(): RuleactionlistContext;
    opt_or_replace(): Opt_or_replaceContext | undefined;
    where_clause(): Where_clauseContext | undefined;
    opt_instead(): Opt_insteadContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RuleactionlistContext extends ParserRuleContext {
    KW_NOTHING(): TerminalNode | undefined;
    ruleactionstmt(): RuleactionstmtContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    ruleactionmulti(): RuleactionmultiContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RuleactionmultiContext extends ParserRuleContext {
    ruleactionstmtOrEmpty(): RuleactionstmtOrEmptyContext[];
    ruleactionstmtOrEmpty(i: number): RuleactionstmtOrEmptyContext;
    SEMI(): TerminalNode[];
    SEMI(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RuleactionstmtContext extends ParserRuleContext {
    selectstmt(): SelectstmtContext | undefined;
    insertstmt(): InsertstmtContext | undefined;
    updatestmt(): UpdatestmtContext | undefined;
    deletestmt(): DeletestmtContext | undefined;
    notifystmt(): NotifystmtContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RuleactionstmtOrEmptyContext extends ParserRuleContext {
    ruleactionstmt(): RuleactionstmtContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class EventContext extends ParserRuleContext {
    KW_SELECT(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    KW_DELETE(): TerminalNode | undefined;
    KW_INSERT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_insteadContext extends ParserRuleContext {
    KW_INSTEAD(): TerminalNode | undefined;
    KW_ALSO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class NotifystmtContext extends ParserRuleContext {
    KW_NOTIFY(): TerminalNode;
    colid(): ColidContext;
    notify_payload(): Notify_payloadContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Notify_payloadContext extends ParserRuleContext {
    COMMA(): TerminalNode;
    sconst(): SconstContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ListenstmtContext extends ParserRuleContext {
    KW_LISTEN(): TerminalNode;
    colid(): ColidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class UnlistenstmtContext extends ParserRuleContext {
    KW_UNLISTEN(): TerminalNode;
    colid(): ColidContext | undefined;
    STAR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TransactionstmtContext extends ParserRuleContext {
    KW_ABORT(): TerminalNode | undefined;
    opt_transaction(): Opt_transactionContext | undefined;
    opt_transaction_chain(): Opt_transaction_chainContext | undefined;
    KW_BEGIN(): TerminalNode | undefined;
    transaction_mode_list_or_empty(): Transaction_mode_list_or_emptyContext | undefined;
    KW_START(): TerminalNode | undefined;
    KW_TRANSACTION(): TerminalNode | undefined;
    KW_COMMIT(): TerminalNode | undefined;
    KW_END(): TerminalNode | undefined;
    KW_ROLLBACK(): TerminalNode | undefined;
    KW_SAVEPOINT(): TerminalNode | undefined;
    colid(): ColidContext | undefined;
    KW_RELEASE(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    KW_PREPARE(): TerminalNode | undefined;
    sconst(): SconstContext | undefined;
    KW_PREPARED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_transactionContext extends ParserRuleContext {
    KW_WORK(): TerminalNode | undefined;
    KW_TRANSACTION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Transaction_mode_itemContext extends ParserRuleContext {
    KW_ISOLATION(): TerminalNode | undefined;
    KW_LEVEL(): TerminalNode | undefined;
    iso_level(): Iso_levelContext | undefined;
    KW_READ(): TerminalNode | undefined;
    KW_ONLY(): TerminalNode | undefined;
    KW_WRITE(): TerminalNode | undefined;
    KW_DEFERRABLE(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Transaction_mode_listContext extends ParserRuleContext {
    transaction_mode_item(): Transaction_mode_itemContext[];
    transaction_mode_item(i: number): Transaction_mode_itemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Transaction_mode_list_or_emptyContext extends ParserRuleContext {
    transaction_mode_list(): Transaction_mode_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_transaction_chainContext extends ParserRuleContext {
    KW_AND(): TerminalNode;
    KW_CHAIN(): TerminalNode;
    KW_NO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ViewstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_AS(): TerminalNode;
    selectstmt(): SelectstmtContext;
    KW_VIEW(): TerminalNode | undefined;
    view_name_create(): View_name_createContext | undefined;
    KW_RECURSIVE(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    columnlist(): ColumnlistContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_OR(): TerminalNode | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    opttemp(): OpttempContext | undefined;
    opt_check_option(): Opt_check_optionContext | undefined;
    opt_column_list(): Opt_column_listContext | undefined;
    opt_reloptions(): Opt_reloptionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_check_optionContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    KW_CHECK(): TerminalNode;
    KW_OPTION(): TerminalNode;
    KW_CASCADED(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class LoadstmtContext extends ParserRuleContext {
    KW_LOAD(): TerminalNode;
    file_name(): File_nameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatedbstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_DATABASE(): TerminalNode;
    database_name_create(): Database_name_createContext;
    opt_with(): Opt_withContext | undefined;
    createdb_opt_list(): Createdb_opt_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Createdb_opt_listContext extends ParserRuleContext {
    createdb_opt_items(): Createdb_opt_itemsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Createdb_opt_itemsContext extends ParserRuleContext {
    createdb_opt_item(): Createdb_opt_itemContext[];
    createdb_opt_item(i: number): Createdb_opt_itemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Createdb_opt_itemContext extends ParserRuleContext {
    createdb_opt_name(): Createdb_opt_nameContext;
    signediconst(): SignediconstContext | undefined;
    opt_boolean_or_string(): Opt_boolean_or_stringContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    opt_equal(): Opt_equalContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Createdb_opt_nameContext extends ParserRuleContext {
    identifier(): IdentifierContext | undefined;
    KW_CONNECTION(): TerminalNode | undefined;
    KW_LIMIT(): TerminalNode | undefined;
    KW_ENCODING(): TerminalNode | undefined;
    KW_LOCATION(): TerminalNode | undefined;
    KW_OWNER(): TerminalNode | undefined;
    KW_TABLESPACE(): TerminalNode | undefined;
    KW_TEMPLATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_equalContext extends ParserRuleContext {
    EQUAL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterdatabasestmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_DATABASE(): TerminalNode;
    database_name(): Database_nameContext;
    createdb_opt_list(): Createdb_opt_listContext | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_TABLESPACE(): TerminalNode | undefined;
    tablespace_name_create(): Tablespace_name_createContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterdatabasesetstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_DATABASE(): TerminalNode;
    database_name(): Database_nameContext;
    setresetclause(): SetresetclauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Drop_option_listContext extends ParserRuleContext {
    drop_option(): Drop_optionContext[];
    drop_option(i: number): Drop_optionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Drop_optionContext extends ParserRuleContext {
    KW_FORCE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AltercollationstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_COLLATION(): TerminalNode;
    any_name(): Any_nameContext;
    KW_REFRESH(): TerminalNode;
    KW_VERSION(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AltersystemstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_SYSTEM(): TerminalNode;
    generic_set(): Generic_setContext;
    KW_SET(): TerminalNode | undefined;
    KW_RESET(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreatedomainstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_DOMAIN(): TerminalNode;
    any_name(): Any_nameContext;
    typename(): TypenameContext;
    colquallist(): ColquallistContext;
    opt_as(): Opt_asContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AlterdomainstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_DOMAIN(): TerminalNode;
    any_name(): Any_nameContext;
    alter_column_default(): Alter_column_defaultContext | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_ADD(): TerminalNode | undefined;
    tableconstraint(): TableconstraintContext | undefined;
    KW_CONSTRAINT(): TerminalNode | undefined;
    name(): NameContext | undefined;
    KW_VALIDATE(): TerminalNode | undefined;
    opt_if_exists(): Opt_if_existsContext | undefined;
    opt_drop_behavior(): Opt_drop_behaviorContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_asContext extends ParserRuleContext {
    KW_AS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AltertsdictionarystmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TEXT(): TerminalNode;
    KW_SEARCH(): TerminalNode;
    KW_DICTIONARY(): TerminalNode;
    any_name(): Any_nameContext;
    definition(): DefinitionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AltertsconfigurationstmtContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode[];
    KW_ALTER(i: number): TerminalNode;
    KW_TEXT(): TerminalNode;
    KW_SEARCH(): TerminalNode;
    KW_CONFIGURATION(): TerminalNode;
    any_name(): Any_nameContext[];
    any_name(i: number): Any_nameContext;
    KW_ADD(): TerminalNode | undefined;
    KW_MAPPING(): TerminalNode;
    KW_FOR(): TerminalNode | undefined;
    name_list(): Name_listContext | undefined;
    any_with(): Any_withContext | undefined;
    any_name_list(): Any_name_listContext | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    opt_if_exists(): Opt_if_existsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Any_withContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CreateconversionstmtContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_CONVERSION(): TerminalNode;
    any_name(): Any_nameContext[];
    any_name(i: number): Any_nameContext;
    KW_FOR(): TerminalNode;
    sconst(): SconstContext[];
    sconst(i: number): SconstContext;
    KW_TO(): TerminalNode;
    KW_FROM(): TerminalNode;
    opt_default(): Opt_defaultContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ClusterstmtContext extends ParserRuleContext {
    KW_CLUSTER(): TerminalNode;
    table_name(): Table_nameContext | undefined;
    opt_verbose(): Opt_verboseContext | undefined;
    cluster_index_specification(): Cluster_index_specificationContext | undefined;
    opt_verbose_list(): Opt_verbose_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_verbose_listContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    opt_verbose(): Opt_verboseContext[];
    opt_verbose(i: number): Opt_verboseContext;
    CLOSE_PAREN(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Cluster_index_specificationContext extends ParserRuleContext {
    KW_USING(): TerminalNode;
    name(): NameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class VacuumstmtContext extends ParserRuleContext {
    KW_VACUUM(): TerminalNode;
    opt_full(): Opt_fullContext | undefined;
    opt_freeze(): Opt_freezeContext | undefined;
    opt_verbose(): Opt_verboseContext | undefined;
    opt_analyze(): Opt_analyzeContext | undefined;
    opt_vacuum_relation_list(): Opt_vacuum_relation_listContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    vac_analyze_option_list(): Vac_analyze_option_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AnalyzestmtContext extends ParserRuleContext {
    analyze_keyword(): Analyze_keywordContext;
    opt_verbose(): Opt_verboseContext | undefined;
    opt_vacuum_relation_list(): Opt_vacuum_relation_listContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    analyze_options_list(): Analyze_options_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Vac_analyze_option_listContext extends ParserRuleContext {
    vac_analyze_option_elem(): Vac_analyze_option_elemContext[];
    vac_analyze_option_elem(i: number): Vac_analyze_option_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Analyze_keywordContext extends ParserRuleContext {
    KW_ANALYZE(): TerminalNode | undefined;
    KW_ANALYSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Vac_analyze_option_elemContext extends ParserRuleContext {
    vac_analyze_option_name(): Vac_analyze_option_nameContext;
    vac_analyze_option_arg(): Vac_analyze_option_argContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Vac_analyze_option_nameContext extends ParserRuleContext {
    nonreservedword(): NonreservedwordContext | undefined;
    analyze_keyword(): Analyze_keywordContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Vac_analyze_option_argContext extends ParserRuleContext {
    opt_boolean_or_string(): Opt_boolean_or_stringContext | undefined;
    numericonly(): NumericonlyContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_analyzeContext extends ParserRuleContext {
    analyze_keyword(): Analyze_keywordContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Analyze_options_listContext extends ParserRuleContext {
    analyze_option_elem(): Analyze_option_elemContext[];
    analyze_option_elem(i: number): Analyze_option_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Analyze_option_elemContext extends ParserRuleContext {
    opt_verbose(): Opt_verboseContext | undefined;
    opt_skiplock(): Opt_skiplockContext | undefined;
    opt_buffer_usage_limit(): Opt_buffer_usage_limitContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_verboseContext extends ParserRuleContext {
    KW_VERBOSE(): TerminalNode;
    KW_FALSE(): TerminalNode | undefined;
    KW_TRUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_skiplockContext extends ParserRuleContext {
    KW_SKIP_LOCKED(): TerminalNode;
    KW_FALSE(): TerminalNode | undefined;
    KW_TRUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_buffer_usage_limitContext extends ParserRuleContext {
    KW_BUFFER_USAGE_LIMIT(): TerminalNode;
    numericonly(): NumericonlyContext | undefined;
    sconst(): SconstContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_fullContext extends ParserRuleContext {
    KW_FULL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_freezeContext extends ParserRuleContext {
    KW_FREEZE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_name_listContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    columnlist(): ColumnlistContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Vacuum_relationContext extends ParserRuleContext {
    table_name(): Table_nameContext;
    opt_name_list(): Opt_name_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Vacuum_relation_listContext extends ParserRuleContext {
    vacuum_relation(): Vacuum_relationContext[];
    vacuum_relation(i: number): Vacuum_relationContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_vacuum_relation_listContext extends ParserRuleContext {
    vacuum_relation_list(): Vacuum_relation_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ExplainstmtContext extends ParserRuleContext {
    KW_EXPLAIN(): TerminalNode;
    explainablestmt(): ExplainablestmtContext;
    analyze_keyword(): Analyze_keywordContext | undefined;
    opt_verbose(): Opt_verboseContext | undefined;
    KW_VERBOSE(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    explain_option_list(): Explain_option_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ExplainablestmtContext extends ParserRuleContext {
    selectstmt(): SelectstmtContext | undefined;
    insertstmt(): InsertstmtContext | undefined;
    updatestmt(): UpdatestmtContext | undefined;
    deletestmt(): DeletestmtContext | undefined;
    declarecursorstmt(): DeclarecursorstmtContext | undefined;
    createasstmt(): CreateasstmtContext | undefined;
    creatematviewstmt(): CreatematviewstmtContext | undefined;
    refreshmatviewstmt(): RefreshmatviewstmtContext | undefined;
    executestmt(): ExecutestmtContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Explain_option_listContext extends ParserRuleContext {
    explain_option_elem(): Explain_option_elemContext[];
    explain_option_elem(i: number): Explain_option_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Explain_option_elemContext extends ParserRuleContext {
    explain_option_name(): Explain_option_nameContext;
    explain_option_arg(): Explain_option_argContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Explain_option_nameContext extends ParserRuleContext {
    nonreservedword(): NonreservedwordContext | undefined;
    analyze_keyword(): Analyze_keywordContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Explain_option_argContext extends ParserRuleContext {
    opt_boolean_or_string(): Opt_boolean_or_stringContext | undefined;
    numericonly(): NumericonlyContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class PreparestmtContext extends ParserRuleContext {
    KW_PREPARE(): TerminalNode;
    name(): NameContext;
    KW_AS(): TerminalNode;
    preparablestmt(): PreparablestmtContext;
    prep_type_clause(): Prep_type_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Prep_type_clauseContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    type_list(): Type_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class PreparablestmtContext extends ParserRuleContext {
    selectstmt(): SelectstmtContext | undefined;
    insertstmt(): InsertstmtContext | undefined;
    updatestmt(): UpdatestmtContext | undefined;
    deletestmt(): DeletestmtContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ExecutestmtContext extends ParserRuleContext {
    KW_EXECUTE(): TerminalNode;
    name(): NameContext;
    execute_param_clause(): Execute_param_clauseContext | undefined;
    KW_CREATE(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    create_as_target(): Create_as_targetContext | undefined;
    KW_AS(): TerminalNode | undefined;
    opttemp(): OpttempContext | undefined;
    opt_if_not_exists(): Opt_if_not_existsContext | undefined;
    opt_with_data(): Opt_with_dataContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Execute_param_clauseContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    expr_list(): Expr_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class DeallocatestmtContext extends ParserRuleContext {
    KW_DEALLOCATE(): TerminalNode;
    name(): NameContext | undefined;
    KW_PREPARE(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class InsertstmtContext extends ParserRuleContext {
    KW_INSERT(): TerminalNode;
    KW_INTO(): TerminalNode;
    insert_target(): Insert_targetContext;
    insert_rest(): Insert_restContext;
    opt_with_clause(): Opt_with_clauseContext | undefined;
    opt_on_conflict(): Opt_on_conflictContext | undefined;
    returning_clause(): Returning_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Insert_targetContext extends ParserRuleContext {
    table_name(): Table_nameContext;
    KW_AS(): TerminalNode | undefined;
    colid(): ColidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Insert_restContext extends ParserRuleContext {
    default_values_or_values(): Default_values_or_valuesContext | undefined;
    selectstmt(): SelectstmtContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    insert_column_list(): Insert_column_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_OVERRIDING(): TerminalNode | undefined;
    override_kind(): Override_kindContext | undefined;
    KW_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Override_kindContext extends ParserRuleContext {
    KW_USER(): TerminalNode | undefined;
    KW_SYSTEM(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Insert_column_listContext extends ParserRuleContext {
    insert_column_item(): Insert_column_itemContext[];
    insert_column_item(i: number): Insert_column_itemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Insert_column_itemContext extends ParserRuleContext {
    column_name(): Column_nameContext;
    opt_indirection(): Opt_indirectionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_on_conflictContext extends ParserRuleContext {
    KW_ON(): TerminalNode;
    KW_CONFLICT(): TerminalNode;
    KW_DO(): TerminalNode;
    KW_UPDATE(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    set_clause_list(): Set_clause_listContext | undefined;
    KW_NOTHING(): TerminalNode | undefined;
    opt_conf_expr(): Opt_conf_exprContext | undefined;
    where_clause(): Where_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_conf_exprContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode | undefined;
    index_params(): Index_paramsContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    where_clause(): Where_clauseContext | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_CONSTRAINT(): TerminalNode | undefined;
    name(): NameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Returning_clauseContext extends ParserRuleContext {
    KW_RETURNING(): TerminalNode;
    target_list(): Target_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class DeletestmtContext extends ParserRuleContext {
    KW_DELETE(): TerminalNode;
    KW_FROM(): TerminalNode;
    relation_expr_opt_alias(): Relation_expr_opt_aliasContext;
    opt_with_clause(): Opt_with_clauseContext | undefined;
    using_clause(): Using_clauseContext | undefined;
    where_or_current_clause(): Where_or_current_clauseContext | undefined;
    returning_clause(): Returning_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Using_clauseContext extends ParserRuleContext {
    KW_USING(): TerminalNode;
    from_list(): From_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class LockstmtContext extends ParserRuleContext {
    KW_LOCK(): TerminalNode;
    relation_expr_list(): Relation_expr_listContext;
    opt_table(): Opt_tableContext | undefined;
    opt_lock(): Opt_lockContext | undefined;
    opt_nowait(): Opt_nowaitContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_lockContext extends ParserRuleContext {
    KW_IN(): TerminalNode;
    lock_type(): Lock_typeContext;
    KW_MODE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Lock_typeContext extends ParserRuleContext {
    KW_ACCESS(): TerminalNode | undefined;
    KW_SHARE(): TerminalNode | undefined;
    KW_EXCLUSIVE(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_nowaitContext extends ParserRuleContext {
    KW_NOWAIT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_nowait_or_skipContext extends ParserRuleContext {
    KW_NOWAIT(): TerminalNode | undefined;
    KW_SKIP(): TerminalNode | undefined;
    KW_LOCKED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class UpdatestmtContext extends ParserRuleContext {
    KW_UPDATE(): TerminalNode;
    relation_expr_opt_alias(): Relation_expr_opt_aliasContext;
    KW_SET(): TerminalNode;
    set_clause_list(): Set_clause_listContext;
    opt_with_clause(): Opt_with_clauseContext | undefined;
    from_clause(): From_clauseContext | undefined;
    where_or_current_clause(): Where_or_current_clauseContext | undefined;
    returning_clause(): Returning_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Set_clause_listContext extends ParserRuleContext {
    set_clause(): Set_clauseContext[];
    set_clause(i: number): Set_clauseContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Set_clauseContext extends ParserRuleContext {
    set_target(): Set_targetContext | undefined;
    EQUAL(): TerminalNode;
    a_expr(): A_exprContext | undefined;
    OPEN_PAREN(): TerminalNode[];
    OPEN_PAREN(i: number): TerminalNode;
    set_target_list(): Set_target_listContext | undefined;
    CLOSE_PAREN(): TerminalNode[];
    CLOSE_PAREN(i: number): TerminalNode;
    select_clause(): Select_clauseContext | undefined;
    KW_ROW(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Set_targetContext extends ParserRuleContext {
    column_name(): Column_nameContext;
    opt_indirection(): Opt_indirectionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Set_target_listContext extends ParserRuleContext {
    set_target(): Set_targetContext[];
    set_target(i: number): Set_targetContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class DeclarecursorstmtContext extends ParserRuleContext {
    KW_DECLARE(): TerminalNode;
    cursor_name(): Cursor_nameContext;
    cursor_options(): Cursor_optionsContext;
    KW_CURSOR(): TerminalNode;
    opt_hold(): Opt_holdContext;
    KW_FOR(): TerminalNode;
    selectstmt(): SelectstmtContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Cursor_nameContext extends ParserRuleContext {
    name(): NameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Cursor_optionsContext extends ParserRuleContext {
    KW_NO(): TerminalNode[];
    KW_NO(i: number): TerminalNode;
    KW_SCROLL(): TerminalNode[];
    KW_SCROLL(i: number): TerminalNode;
    KW_BINARY(): TerminalNode[];
    KW_BINARY(i: number): TerminalNode;
    KW_INSENSITIVE(): TerminalNode[];
    KW_INSENSITIVE(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_holdContext extends ParserRuleContext {
    KW_WITH(): TerminalNode | undefined;
    KW_HOLD(): TerminalNode | undefined;
    KW_WITHOUT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class SelectstmtContext extends ParserRuleContext {
    select_no_parens(): Select_no_parensContext | undefined;
    select_with_parens(): Select_with_parensContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Select_with_parensContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    select_no_parens(): Select_no_parensContext | undefined;
    CLOSE_PAREN(): TerminalNode;
    select_with_parens(): Select_with_parensContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Select_no_parensContext extends ParserRuleContext {
    select_clause(): Select_clauseContext;
    opt_sort_clause(): Opt_sort_clauseContext | undefined;
    for_locking_clause(): For_locking_clauseContext | undefined;
    select_limit(): Select_limitContext | undefined;
    opt_select_limit(): Opt_select_limitContext | undefined;
    opt_for_locking_clause(): Opt_for_locking_clauseContext | undefined;
    with_clause(): With_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Select_clauseContext extends ParserRuleContext {
    simple_select(): Simple_selectContext | undefined;
    select_with_parens(): Select_with_parensContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Simple_selectContext extends ParserRuleContext {
    KW_SELECT(): TerminalNode | undefined;
    values_clause(): Values_clauseContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    relation_expr(): Relation_exprContext | undefined;
    select_with_parens(): Select_with_parensContext[];
    select_with_parens(i: number): Select_with_parensContext;
    set_operator_with_all_or_distinct(): Set_operator_with_all_or_distinctContext[];
    set_operator_with_all_or_distinct(i: number): Set_operator_with_all_or_distinctContext;
    simple_select(): Simple_selectContext[];
    simple_select(i: number): Simple_selectContext;
    into_clause(): Into_clauseContext[];
    into_clause(i: number): Into_clauseContext;
    from_clause(): From_clauseContext | undefined;
    where_clause(): Where_clauseContext | undefined;
    group_clause(): Group_clauseContext | undefined;
    having_clause(): Having_clauseContext | undefined;
    window_clause(): Window_clauseContext | undefined;
    opt_all_clause(): Opt_all_clauseContext | undefined;
    opt_target_list(): Opt_target_listContext | undefined;
    distinct_clause(): Distinct_clauseContext | undefined;
    target_list(): Target_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Set_operatorContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: Set_operatorContext): void;
}
export declare class UnionContext extends Set_operatorContext {
    KW_UNION(): TerminalNode;
    constructor(ctx: Set_operatorContext);
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class IntersectContext extends Set_operatorContext {
    KW_INTERSECT(): TerminalNode;
    constructor(ctx: Set_operatorContext);
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ExceptContext extends Set_operatorContext {
    KW_EXCEPT(): TerminalNode;
    constructor(ctx: Set_operatorContext);
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Set_operator_with_all_or_distinctContext extends ParserRuleContext {
    set_operator(): Set_operatorContext;
    all_or_distinct(): All_or_distinctContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class With_clauseContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    cte_list(): Cte_listContext;
    KW_RECURSIVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Cte_listContext extends ParserRuleContext {
    common_table_expr(): Common_table_exprContext[];
    common_table_expr(i: number): Common_table_exprContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Common_table_exprContext extends ParserRuleContext {
    name(): NameContext;
    KW_AS(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    preparablestmt(): PreparablestmtContext;
    CLOSE_PAREN(): TerminalNode;
    opt_name_list(): Opt_name_listContext | undefined;
    opt_materialized(): Opt_materializedContext | undefined;
    search_cluase(): Search_cluaseContext | undefined;
    cycle_cluase(): Cycle_cluaseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Search_cluaseContext extends ParserRuleContext {
    KW_SEARCH(): TerminalNode;
    KW_FIRST(): TerminalNode;
    KW_BY(): TerminalNode;
    columnlist(): ColumnlistContext;
    KW_SET(): TerminalNode;
    column_name(): Column_nameContext;
    KW_BREADTH(): TerminalNode | undefined;
    KW_DEPTH(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Cycle_cluaseContext extends ParserRuleContext {
    KW_CYCLE(): TerminalNode;
    columnlist(): ColumnlistContext;
    KW_SET(): TerminalNode;
    column_name(): Column_nameContext[];
    column_name(i: number): Column_nameContext;
    KW_USING(): TerminalNode;
    KW_TO(): TerminalNode | undefined;
    name(): NameContext[];
    name(i: number): NameContext;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_materializedContext extends ParserRuleContext {
    KW_MATERIALIZED(): TerminalNode;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_with_clauseContext extends ParserRuleContext {
    with_clause(): With_clauseContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Into_clauseContext extends ParserRuleContext {
    KW_INTO(): TerminalNode;
    opt_strict(): Opt_strictContext | undefined;
    opttempTableName(): OpttempTableNameContext | undefined;
    into_target(): Into_targetContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_strictContext extends ParserRuleContext {
    KW_STRICT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OpttempTableNameContext extends ParserRuleContext {
    table_name_create(): Table_name_createContext;
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_TEMP(): TerminalNode | undefined;
    opt_table(): Opt_tableContext | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    KW_GLOBAL(): TerminalNode | undefined;
    KW_UNLOGGED(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_tableContext extends ParserRuleContext {
    KW_TABLE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class All_or_distinctContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Distinct_clauseContext extends ParserRuleContext {
    KW_DISTINCT(): TerminalNode;
    KW_ON(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    expr_list(): Expr_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_all_clauseContext extends ParserRuleContext {
    KW_ALL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_sort_clauseContext extends ParserRuleContext {
    sort_clause(): Sort_clauseContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Sort_clauseContext extends ParserRuleContext {
    KW_ORDER(): TerminalNode;
    KW_BY(): TerminalNode;
    sortby_list(): Sortby_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Sortby_listContext extends ParserRuleContext {
    sortby(): SortbyContext[];
    sortby(i: number): SortbyContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class SortbyContext extends ParserRuleContext {
    column_expr_noparen(): Column_expr_noparenContext;
    KW_USING(): TerminalNode | undefined;
    qual_all_op(): Qual_all_opContext | undefined;
    opt_asc_desc(): Opt_asc_descContext | undefined;
    opt_nulls_order(): Opt_nulls_orderContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Select_limitContext extends ParserRuleContext {
    limit_clause(): Limit_clauseContext | undefined;
    offset_clause(): Offset_clauseContext | undefined;
    fetch_clause(): Fetch_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_select_limitContext extends ParserRuleContext {
    select_limit(): Select_limitContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Limit_clauseContext extends ParserRuleContext {
    KW_LIMIT(): TerminalNode;
    select_limit_value(): Select_limit_valueContext;
    COMMA(): TerminalNode | undefined;
    select_offset_value(): Select_offset_valueContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Fetch_clauseContext extends ParserRuleContext {
    KW_FETCH(): TerminalNode;
    first_or_next(): First_or_nextContext;
    select_fetch_first_value(): Select_fetch_first_valueContext | undefined;
    row_or_rows(): Row_or_rowsContext | undefined;
    KW_ONLY(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_TIES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Offset_clauseContext extends ParserRuleContext {
    KW_OFFSET(): TerminalNode;
    select_offset_value(): Select_offset_valueContext | undefined;
    select_fetch_first_value(): Select_fetch_first_valueContext | undefined;
    row_or_rows(): Row_or_rowsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Select_limit_valueContext extends ParserRuleContext {
    a_expr(): A_exprContext | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Select_offset_valueContext extends ParserRuleContext {
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Select_fetch_first_valueContext extends ParserRuleContext {
    c_expr(): C_exprContext | undefined;
    PLUS(): TerminalNode | undefined;
    i_or_f_const(): I_or_f_constContext | undefined;
    MINUS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class I_or_f_constContext extends ParserRuleContext {
    iconst(): IconstContext | undefined;
    fconst(): FconstContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Row_or_rowsContext extends ParserRuleContext {
    KW_ROW(): TerminalNode | undefined;
    KW_ROWS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class First_or_nextContext extends ParserRuleContext {
    KW_FIRST(): TerminalNode | undefined;
    KW_NEXT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Group_clauseContext extends ParserRuleContext {
    KW_GROUP(): TerminalNode;
    KW_BY(): TerminalNode;
    group_by_list(): Group_by_listContext;
    all_or_distinct(): All_or_distinctContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Group_by_listContext extends ParserRuleContext {
    group_by_item(): Group_by_itemContext[];
    group_by_item(i: number): Group_by_itemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Group_by_itemContext extends ParserRuleContext {
    column_expr_noparen(): Column_expr_noparenContext | undefined;
    empty_grouping_set(): Empty_grouping_setContext | undefined;
    cube_clause(): Cube_clauseContext | undefined;
    rollup_clause(): Rollup_clauseContext | undefined;
    grouping_sets_clause(): Grouping_sets_clauseContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    column_expr_list_noparen(): Column_expr_list_noparenContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Empty_grouping_setContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Rollup_clauseContext extends ParserRuleContext {
    KW_ROLLUP(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    column_expr_list_noparen(): Column_expr_list_noparenContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Cube_clauseContext extends ParserRuleContext {
    KW_CUBE(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    column_expr_list_noparen(): Column_expr_list_noparenContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Grouping_sets_clauseContext extends ParserRuleContext {
    KW_GROUPING(): TerminalNode;
    KW_SETS(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    group_by_list(): Group_by_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Having_clauseContext extends ParserRuleContext {
    KW_HAVING(): TerminalNode;
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class For_locking_clauseContext extends ParserRuleContext {
    for_locking_items(): For_locking_itemsContext | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_READ(): TerminalNode | undefined;
    KW_ONLY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_for_locking_clauseContext extends ParserRuleContext {
    for_locking_clause(): For_locking_clauseContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class For_locking_itemsContext extends ParserRuleContext {
    for_locking_item(): For_locking_itemContext[];
    for_locking_item(i: number): For_locking_itemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class For_locking_itemContext extends ParserRuleContext {
    for_locking_strength(): For_locking_strengthContext;
    locked_rels_list(): Locked_rels_listContext | undefined;
    opt_nowait_or_skip(): Opt_nowait_or_skipContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class For_locking_strengthContext extends ParserRuleContext {
    KW_FOR(): TerminalNode;
    KW_UPDATE(): TerminalNode | undefined;
    KW_SHARE(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Locked_rels_listContext extends ParserRuleContext {
    KW_OF(): TerminalNode;
    qualified_name_list(): Qualified_name_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Values_clauseContext extends ParserRuleContext {
    KW_VALUES(): TerminalNode;
    OPEN_PAREN(): TerminalNode[];
    OPEN_PAREN(i: number): TerminalNode;
    expr_list(): Expr_listContext[];
    expr_list(i: number): Expr_listContext;
    CLOSE_PAREN(): TerminalNode[];
    CLOSE_PAREN(i: number): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class From_clauseContext extends ParserRuleContext {
    KW_FROM(): TerminalNode;
    from_list(): From_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class From_listContext extends ParserRuleContext {
    table_ref(): Table_refContext[];
    table_ref(i: number): Table_refContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Table_refContext extends ParserRuleContext {
    relation_expr(): Relation_exprContext | undefined;
    func_table(): Func_tableContext | undefined;
    xmltable(): XmltableContext | undefined;
    select_with_parens(): Select_with_parensContext | undefined;
    KW_LATERAL(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    table_ref(): Table_refContext[];
    table_ref(i: number): Table_refContext;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_CROSS(): TerminalNode[];
    KW_CROSS(i: number): TerminalNode;
    KW_JOIN(): TerminalNode[];
    KW_JOIN(i: number): TerminalNode;
    KW_NATURAL(): TerminalNode[];
    KW_NATURAL(i: number): TerminalNode;
    join_qual(): Join_qualContext[];
    join_qual(i: number): Join_qualContext;
    opt_alias_clause(): Opt_alias_clauseContext | undefined;
    tablesample_clause(): Tablesample_clauseContext | undefined;
    func_alias_clause(): Func_alias_clauseContext | undefined;
    join_type(): Join_typeContext[];
    join_type(i: number): Join_typeContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Alias_clauseContext extends ParserRuleContext {
    colid(): ColidContext;
    KW_AS(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    name_list(): Name_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_alias_clauseContext extends ParserRuleContext {
    alias_clause(): Alias_clauseContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_alias_clauseContext extends ParserRuleContext {
    alias_clause(): Alias_clauseContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    tablefuncelementlist(): TablefuncelementlistContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    colid(): ColidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Join_typeContext extends ParserRuleContext {
    KW_FULL(): TerminalNode | undefined;
    KW_LEFT(): TerminalNode | undefined;
    KW_RIGHT(): TerminalNode | undefined;
    KW_INNER(): TerminalNode | undefined;
    KW_OUTER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Join_qualContext extends ParserRuleContext {
    KW_USING(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    columnlist(): ColumnlistContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Relation_exprContext extends ParserRuleContext {
    table_name(): Table_nameContext | undefined;
    KW_ONLY(): TerminalNode | undefined;
    STAR(): TerminalNode | undefined;
    columnlist(): ColumnlistContext | undefined;
    where_clause(): Where_clauseContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name(): Schema_nameContext | undefined;
    KW_CURRENT_SCHEMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Publication_relation_exprContext extends ParserRuleContext {
    KW_TABLE(): TerminalNode | undefined;
    table_name(): Table_nameContext | undefined;
    KW_ONLY(): TerminalNode | undefined;
    STAR(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    columnlist(): ColumnlistContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    where_clause(): Where_clauseContext | undefined;
    KW_TABLES(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    schema_name(): Schema_nameContext | undefined;
    KW_CURRENT_SCHEMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Relation_expr_listContext extends ParserRuleContext {
    relation_expr(): Relation_exprContext[];
    relation_expr(i: number): Relation_exprContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Publication_relation_expr_listContext extends ParserRuleContext {
    publication_relation_expr(): Publication_relation_exprContext[];
    publication_relation_expr(i: number): Publication_relation_exprContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Relation_expr_opt_aliasContext extends ParserRuleContext {
    relation_expr(): Relation_exprContext;
    colid(): ColidContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Tablesample_clauseContext extends ParserRuleContext {
    KW_TABLESAMPLE(): TerminalNode;
    function_name(): Function_nameContext;
    OPEN_PAREN(): TerminalNode;
    expr_list(): Expr_listContext;
    CLOSE_PAREN(): TerminalNode;
    opt_repeatable_clause(): Opt_repeatable_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_repeatable_clauseContext extends ParserRuleContext {
    KW_REPEATABLE(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    a_expr(): A_exprContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_tableContext extends ParserRuleContext {
    func_expr_windowless(): Func_expr_windowlessContext | undefined;
    opt_ordinality(): Opt_ordinalityContext | undefined;
    KW_ROWS(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    rowsfrom_list(): Rowsfrom_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Rowsfrom_itemContext extends ParserRuleContext {
    func_expr_windowless(): Func_expr_windowlessContext;
    opt_col_def_list(): Opt_col_def_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Rowsfrom_listContext extends ParserRuleContext {
    rowsfrom_item(): Rowsfrom_itemContext[];
    rowsfrom_item(i: number): Rowsfrom_itemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_col_def_listContext extends ParserRuleContext {
    KW_AS(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    tablefuncelementlist(): TablefuncelementlistContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_ordinalityContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    KW_ORDINALITY(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Where_clauseContext extends ParserRuleContext {
    KW_WHERE(): TerminalNode;
    column_expr_noparen(): Column_expr_noparenContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Where_or_current_clauseContext extends ParserRuleContext {
    KW_WHERE(): TerminalNode;
    KW_CURRENT(): TerminalNode | undefined;
    KW_OF(): TerminalNode | undefined;
    cursor_name(): Cursor_nameContext | undefined;
    a_expr(): A_exprContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class OpttablefuncelementlistContext extends ParserRuleContext {
    tablefuncelementlist(): TablefuncelementlistContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TablefuncelementlistContext extends ParserRuleContext {
    tablefuncelement(): TablefuncelementContext[];
    tablefuncelement(i: number): TablefuncelementContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TablefuncelementContext extends ParserRuleContext {
    colid(): ColidContext;
    typename(): TypenameContext;
    opt_collate_clause(): Opt_collate_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class XmltableContext extends ParserRuleContext {
    KW_XMLTABLE(): TerminalNode;
    OPEN_PAREN(): TerminalNode[];
    OPEN_PAREN(i: number): TerminalNode;
    CLOSE_PAREN(): TerminalNode[];
    CLOSE_PAREN(i: number): TerminalNode;
    c_expr(): C_exprContext | undefined;
    xmlexists_argument(): Xmlexists_argumentContext | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    xmltable_column_list(): Xmltable_column_listContext | undefined;
    KW_XMLNAMESPACES(): TerminalNode | undefined;
    xml_namespace_list(): Xml_namespace_listContext | undefined;
    COMMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xmltable_column_listContext extends ParserRuleContext {
    xmltable_column_el(): Xmltable_column_elContext[];
    xmltable_column_el(i: number): Xmltable_column_elContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xmltable_column_elContext extends ParserRuleContext {
    colid(): ColidContext;
    typename(): TypenameContext | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_ORDINALITY(): TerminalNode | undefined;
    xmltable_column_option_list(): Xmltable_column_option_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xmltable_column_option_listContext extends ParserRuleContext {
    xmltable_column_option_el(): Xmltable_column_option_elContext[];
    xmltable_column_option_el(i: number): Xmltable_column_option_elContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xmltable_column_option_elContext extends ParserRuleContext {
    KW_DEFAULT(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    identifier(): IdentifierContext | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xml_namespace_listContext extends ParserRuleContext {
    xml_namespace_el(): Xml_namespace_elContext[];
    xml_namespace_el(i: number): Xml_namespace_elContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xml_namespace_elContext extends ParserRuleContext {
    b_expr(): B_exprContext;
    KW_AS(): TerminalNode | undefined;
    collabel(): CollabelContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class TypenameContext extends ParserRuleContext {
    simpletypename(): SimpletypenameContext | undefined;
    opt_array_bounds(): Opt_array_boundsContext | undefined;
    KW_ARRAY(): TerminalNode | undefined;
    KW_SETOF(): TerminalNode | undefined;
    OPEN_BRACKET(): TerminalNode | undefined;
    iconst(): IconstContext | undefined;
    CLOSE_BRACKET(): TerminalNode | undefined;
    qualified_name(): Qualified_nameContext | undefined;
    PERCENT(): TerminalNode | undefined;
    KW_ROWTYPE(): TerminalNode | undefined;
    KW_TYPE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_array_boundsContext extends ParserRuleContext {
    OPEN_BRACKET(): TerminalNode[];
    OPEN_BRACKET(i: number): TerminalNode;
    CLOSE_BRACKET(): TerminalNode[];
    CLOSE_BRACKET(i: number): TerminalNode;
    iconst(): IconstContext[];
    iconst(i: number): IconstContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class SimpletypenameContext extends ParserRuleContext {
    generictype(): GenerictypeContext | undefined;
    numeric(): NumericContext | undefined;
    bit(): BitContext | undefined;
    character(): CharacterContext | undefined;
    constdatetime(): ConstdatetimeContext | undefined;
    constinterval(): ConstintervalContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    iconst(): IconstContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    opt_interval(): Opt_intervalContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ConsttypenameContext extends ParserRuleContext {
    numeric(): NumericContext | undefined;
    constbit(): ConstbitContext | undefined;
    constcharacter(): ConstcharacterContext | undefined;
    constdatetime(): ConstdatetimeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class GenerictypeContext extends ParserRuleContext {
    type_function_name(): Type_function_nameContext;
    attrs(): AttrsContext | undefined;
    opt_type_modifiers(): Opt_type_modifiersContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_type_modifiersContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    expr_list(): Expr_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class NumericContext extends ParserRuleContext {
    KW_INT(): TerminalNode | undefined;
    KW_INTEGER(): TerminalNode | undefined;
    KW_SMALLINT(): TerminalNode | undefined;
    KW_BIGINT(): TerminalNode | undefined;
    KW_REAL(): TerminalNode | undefined;
    KW_FLOAT(): TerminalNode | undefined;
    opt_float(): Opt_floatContext | undefined;
    KW_DOUBLE(): TerminalNode | undefined;
    KW_PRECISION(): TerminalNode | undefined;
    KW_DECIMAL(): TerminalNode | undefined;
    opt_type_modifiers(): Opt_type_modifiersContext | undefined;
    KW_DEC(): TerminalNode | undefined;
    KW_NUMERIC(): TerminalNode | undefined;
    KW_BOOLEAN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_floatContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    iconst(): IconstContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class BitContext extends ParserRuleContext {
    bitwithlength(): BitwithlengthContext | undefined;
    bitwithoutlength(): BitwithoutlengthContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ConstbitContext extends ParserRuleContext {
    bitwithlength(): BitwithlengthContext | undefined;
    bitwithoutlength(): BitwithoutlengthContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class BitwithlengthContext extends ParserRuleContext {
    KW_BIT(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    expr_list(): Expr_listContext;
    CLOSE_PAREN(): TerminalNode;
    opt_varying(): Opt_varyingContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class BitwithoutlengthContext extends ParserRuleContext {
    KW_BIT(): TerminalNode;
    opt_varying(): Opt_varyingContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CharacterContext extends ParserRuleContext {
    character_c(): Character_cContext;
    OPEN_PAREN(): TerminalNode | undefined;
    iconst(): IconstContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ConstcharacterContext extends ParserRuleContext {
    character_c(): Character_cContext;
    OPEN_PAREN(): TerminalNode | undefined;
    iconst(): IconstContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Character_cContext extends ParserRuleContext {
    KW_CHARACTER(): TerminalNode | undefined;
    KW_CHAR(): TerminalNode | undefined;
    KW_NCHAR(): TerminalNode | undefined;
    opt_varying(): Opt_varyingContext | undefined;
    KW_VARCHAR(): TerminalNode | undefined;
    KW_NATIONAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_varyingContext extends ParserRuleContext {
    KW_VARYING(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ConstdatetimeContext extends ParserRuleContext {
    KW_TIMESTAMP(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    iconst(): IconstContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    opt_timezone(): Opt_timezoneContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ConstintervalContext extends ParserRuleContext {
    KW_INTERVAL(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_timezoneContext extends ParserRuleContext {
    KW_WITH(): TerminalNode | undefined;
    KW_TIME(): TerminalNode;
    KW_ZONE(): TerminalNode;
    KW_WITHOUT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_intervalContext extends ParserRuleContext {
    KW_YEAR(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    interval_second(): Interval_secondContext | undefined;
    KW_TO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Interval_secondContext extends ParserRuleContext {
    KW_SECOND(): TerminalNode;
    OPEN_PAREN(): TerminalNode | undefined;
    iconst(): IconstContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_escapeContext extends ParserRuleContext {
    KW_ESCAPE(): TerminalNode;
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_exprContext extends ParserRuleContext {
    a_expr_qual(): A_expr_qualContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_qualContext extends ParserRuleContext {
    a_expr_lessless(): A_expr_lesslessContext;
    qual_op(): Qual_opContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_lesslessContext extends ParserRuleContext {
    a_expr_or(): A_expr_orContext[];
    a_expr_or(i: number): A_expr_orContext;
    LESS_LESS(): TerminalNode[];
    LESS_LESS(i: number): TerminalNode;
    GREATER_GREATER(): TerminalNode[];
    GREATER_GREATER(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_orContext extends ParserRuleContext {
    a_expr_and(): A_expr_andContext[];
    a_expr_and(i: number): A_expr_andContext;
    KW_OR(): TerminalNode[];
    KW_OR(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_andContext extends ParserRuleContext {
    a_expr_in(): A_expr_inContext[];
    a_expr_in(i: number): A_expr_inContext;
    KW_AND(): TerminalNode[];
    KW_AND(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_inContext extends ParserRuleContext {
    a_expr_unary_not(): A_expr_unary_notContext;
    KW_IN(): TerminalNode | undefined;
    in_expr(): In_exprContext | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_unary_notContext extends ParserRuleContext {
    a_expr_isnull(): A_expr_isnullContext;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_isnullContext extends ParserRuleContext {
    a_expr_is_not(): A_expr_is_notContext;
    KW_ISNULL(): TerminalNode | undefined;
    KW_NOTNULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_is_notContext extends ParserRuleContext {
    a_expr_compare(): A_expr_compareContext;
    KW_IS(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    KW_UNKNOWN(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    KW_OF(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    type_list(): Type_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_DOCUMENT(): TerminalNode | undefined;
    KW_NORMALIZED(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    unicode_normal_form(): Unicode_normal_formContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_compareContext extends ParserRuleContext {
    a_expr_like(): A_expr_likeContext[];
    a_expr_like(i: number): A_expr_likeContext;
    subquery_Op(): Subquery_OpContext | undefined;
    sub_type(): Sub_typeContext | undefined;
    LT(): TerminalNode | undefined;
    GT(): TerminalNode | undefined;
    EQUAL(): TerminalNode | undefined;
    LESS_EQUALS(): TerminalNode | undefined;
    GREATER_EQUALS(): TerminalNode | undefined;
    NOT_EQUALS(): TerminalNode | undefined;
    select_with_parens(): Select_with_parensContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_likeContext extends ParserRuleContext {
    a_expr_qual_op(): A_expr_qual_opContext[];
    a_expr_qual_op(i: number): A_expr_qual_opContext;
    KW_LIKE(): TerminalNode | undefined;
    KW_ILIKE(): TerminalNode | undefined;
    KW_SIMILAR(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    KW_BETWEEN(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    opt_escape(): Opt_escapeContext | undefined;
    KW_SYMMETRIC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_qual_opContext extends ParserRuleContext {
    a_expr_unary_qualop(): A_expr_unary_qualopContext[];
    a_expr_unary_qualop(i: number): A_expr_unary_qualopContext;
    qual_op(): Qual_opContext[];
    qual_op(i: number): Qual_opContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_unary_qualopContext extends ParserRuleContext {
    a_expr_add(): A_expr_addContext;
    qual_op(): Qual_opContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_addContext extends ParserRuleContext {
    a_expr_mul(): A_expr_mulContext[];
    a_expr_mul(i: number): A_expr_mulContext;
    MINUS(): TerminalNode[];
    MINUS(i: number): TerminalNode;
    PLUS(): TerminalNode[];
    PLUS(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_mulContext extends ParserRuleContext {
    a_expr_caret(): A_expr_caretContext[];
    a_expr_caret(i: number): A_expr_caretContext;
    STAR(): TerminalNode[];
    STAR(i: number): TerminalNode;
    SLASH(): TerminalNode[];
    SLASH(i: number): TerminalNode;
    PERCENT(): TerminalNode[];
    PERCENT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_caretContext extends ParserRuleContext {
    a_expr_unary_sign(): A_expr_unary_signContext;
    CARET(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_unary_signContext extends ParserRuleContext {
    a_expr_at_time_zone(): A_expr_at_time_zoneContext;
    MINUS(): TerminalNode | undefined;
    PLUS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_at_time_zoneContext extends ParserRuleContext {
    a_expr_collate(): A_expr_collateContext;
    KW_AT(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_ZONE(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_collateContext extends ParserRuleContext {
    a_expr_typecast(): A_expr_typecastContext;
    KW_COLLATE(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class A_expr_typecastContext extends ParserRuleContext {
    c_expr(): C_exprContext;
    TYPECAST(): TerminalNode[];
    TYPECAST(i: number): TerminalNode;
    typename(): TypenameContext[];
    typename(i: number): TypenameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class B_exprContext extends ParserRuleContext {
    c_expr(): C_exprContext | undefined;
    b_expr(): B_exprContext[];
    b_expr(i: number): B_exprContext;
    TYPECAST(): TerminalNode | undefined;
    typename(): TypenameContext | undefined;
    PLUS(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    CARET(): TerminalNode | undefined;
    STAR(): TerminalNode | undefined;
    SLASH(): TerminalNode | undefined;
    PERCENT(): TerminalNode | undefined;
    qual_op(): Qual_opContext | undefined;
    LT(): TerminalNode | undefined;
    GT(): TerminalNode | undefined;
    EQUAL(): TerminalNode | undefined;
    LESS_EQUALS(): TerminalNode | undefined;
    GREATER_EQUALS(): TerminalNode | undefined;
    NOT_EQUALS(): TerminalNode | undefined;
    KW_IS(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_OF(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    type_list(): Type_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_DOCUMENT(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class C_exprContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: C_exprContext): void;
}
export declare class C_expr_existsContext extends C_exprContext {
    KW_EXISTS(): TerminalNode;
    select_with_parens(): Select_with_parensContext;
    constructor(ctx: C_exprContext);
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class C_expr_exprContext extends C_exprContext {
    _a_expr_in_parens: A_exprContext;
    KW_ARRAY(): TerminalNode | undefined;
    select_with_parens(): Select_with_parensContext | undefined;
    array_expr(): Array_exprContext | undefined;
    PARAM(): TerminalNode | undefined;
    opt_indirection(): Opt_indirectionContext | undefined;
    KW_GROUPING(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    expr_list(): Expr_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_UNIQUE(): TerminalNode | undefined;
    columnref(): ColumnrefContext | undefined;
    aexprconst(): AexprconstContext | undefined;
    plsqlvariablename(): PlsqlvariablenameContext | undefined;
    a_expr(): A_exprContext | undefined;
    func_expr(): Func_exprContext | undefined;
    indirection(): IndirectionContext | undefined;
    explicit_row(): Explicit_rowContext | undefined;
    implicit_row(): Implicit_rowContext | undefined;
    row(): RowContext[];
    row(i: number): RowContext;
    KW_OVERLAPS(): TerminalNode | undefined;
    constructor(ctx: C_exprContext);
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class C_expr_caseContext extends C_exprContext {
    case_expr(): Case_exprContext;
    constructor(ctx: C_exprContext);
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class PlsqlvariablenameContext extends ParserRuleContext {
    PLSQLVARIABLENAME(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_applicationContext extends ParserRuleContext {
    function_name(): Function_nameContext;
    OPEN_PAREN(): TerminalNode | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    func_arg_list(): Func_arg_listContext | undefined;
    KW_VARIADIC(): TerminalNode | undefined;
    func_arg_expr(): Func_arg_exprContext | undefined;
    STAR(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    COMMA(): TerminalNode | undefined;
    opt_sort_clause(): Opt_sort_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_exprContext extends ParserRuleContext {
    func_application(): Func_applicationContext | undefined;
    within_group_clause(): Within_group_clauseContext | undefined;
    filter_clause(): Filter_clauseContext | undefined;
    over_clause(): Over_clauseContext | undefined;
    func_expr_common_subexpr(): Func_expr_common_subexprContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_expr_windowlessContext extends ParserRuleContext {
    func_application(): Func_applicationContext | undefined;
    func_expr_common_subexpr(): Func_expr_common_subexprContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_expr_common_subexprContext extends ParserRuleContext {
    KW_COLLATION(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    a_expr(): A_exprContext[];
    a_expr(i: number): A_exprContext;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_CURRENT_DATE(): TerminalNode | undefined;
    KW_CURRENT_TIME(): TerminalNode | undefined;
    iconst(): IconstContext | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    KW_LOCALTIME(): TerminalNode | undefined;
    KW_LOCALTIMESTAMP(): TerminalNode | undefined;
    KW_CURRENT_ROLE(): TerminalNode | undefined;
    KW_CURRENT_USER(): TerminalNode | undefined;
    KW_SESSION_USER(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_CURRENT_CATALOG(): TerminalNode | undefined;
    KW_CURRENT_SCHEMA(): TerminalNode | undefined;
    KW_CAST(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    typename(): TypenameContext | undefined;
    KW_EXTRACT(): TerminalNode | undefined;
    extract_list(): Extract_listContext | undefined;
    KW_NORMALIZE(): TerminalNode | undefined;
    COMMA(): TerminalNode | undefined;
    unicode_normal_form(): Unicode_normal_formContext | undefined;
    KW_OVERLAY(): TerminalNode | undefined;
    overlay_list(): Overlay_listContext | undefined;
    KW_POSITION(): TerminalNode | undefined;
    position_list(): Position_listContext | undefined;
    KW_SUBSTRING(): TerminalNode | undefined;
    substr_list(): Substr_listContext | undefined;
    KW_TREAT(): TerminalNode | undefined;
    KW_TRIM(): TerminalNode | undefined;
    trim_list(): Trim_listContext | undefined;
    KW_BOTH(): TerminalNode | undefined;
    KW_LEADING(): TerminalNode | undefined;
    KW_TRAILING(): TerminalNode | undefined;
    KW_NULLIF(): TerminalNode | undefined;
    KW_COALESCE(): TerminalNode | undefined;
    expr_list(): Expr_listContext | undefined;
    KW_GREATEST(): TerminalNode | undefined;
    KW_LEAST(): TerminalNode | undefined;
    KW_XMLCONCAT(): TerminalNode | undefined;
    KW_XMLELEMENT(): TerminalNode | undefined;
    KW_NAME(): TerminalNode | undefined;
    collabel(): CollabelContext | undefined;
    xml_attributes(): Xml_attributesContext | undefined;
    KW_XMLEXISTS(): TerminalNode | undefined;
    c_expr(): C_exprContext | undefined;
    xmlexists_argument(): Xmlexists_argumentContext | undefined;
    KW_XMLFOREST(): TerminalNode | undefined;
    xml_attribute_list(): Xml_attribute_listContext | undefined;
    KW_XMLPARSE(): TerminalNode | undefined;
    document_or_content(): Document_or_contentContext | undefined;
    xml_whitespace_option(): Xml_whitespace_optionContext | undefined;
    KW_XMLPI(): TerminalNode | undefined;
    KW_XMLROOT(): TerminalNode | undefined;
    KW_XML(): TerminalNode | undefined;
    xml_root_version(): Xml_root_versionContext | undefined;
    opt_xml_root_standalone(): Opt_xml_root_standaloneContext | undefined;
    KW_XMLSERIALIZE(): TerminalNode | undefined;
    simpletypename(): SimpletypenameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xml_root_versionContext extends ParserRuleContext {
    KW_VERSION(): TerminalNode;
    a_expr(): A_exprContext | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_xml_root_standaloneContext extends ParserRuleContext {
    COMMA(): TerminalNode;
    KW_STANDALONE(): TerminalNode;
    KW_YES(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xml_attributesContext extends ParserRuleContext {
    KW_XMLATTRIBUTES(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    xml_attribute_list(): Xml_attribute_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xml_attribute_listContext extends ParserRuleContext {
    xml_attribute_el(): Xml_attribute_elContext[];
    xml_attribute_el(i: number): Xml_attribute_elContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xml_attribute_elContext extends ParserRuleContext {
    a_expr(): A_exprContext;
    KW_AS(): TerminalNode | undefined;
    collabel(): CollabelContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Document_or_contentContext extends ParserRuleContext {
    KW_DOCUMENT(): TerminalNode | undefined;
    KW_CONTENT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xml_whitespace_optionContext extends ParserRuleContext {
    KW_PRESERVE(): TerminalNode | undefined;
    KW_WHITESPACE(): TerminalNode;
    KW_STRIP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xmlexists_argumentContext extends ParserRuleContext {
    KW_PASSING(): TerminalNode;
    c_expr(): C_exprContext;
    xml_passing_mech(): Xml_passing_mechContext[];
    xml_passing_mech(i: number): Xml_passing_mechContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Xml_passing_mechContext extends ParserRuleContext {
    KW_BY(): TerminalNode;
    KW_REF(): TerminalNode | undefined;
    KW_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Within_group_clauseContext extends ParserRuleContext {
    KW_WITHIN(): TerminalNode;
    KW_GROUP(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    sort_clause(): Sort_clauseContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Filter_clauseContext extends ParserRuleContext {
    KW_FILTER(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    KW_WHERE(): TerminalNode;
    a_expr(): A_exprContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Window_clauseContext extends ParserRuleContext {
    KW_WINDOW(): TerminalNode;
    window_definition_list(): Window_definition_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Window_definition_listContext extends ParserRuleContext {
    window_definition(): Window_definitionContext[];
    window_definition(i: number): Window_definitionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Window_definitionContext extends ParserRuleContext {
    colid(): ColidContext;
    KW_AS(): TerminalNode;
    window_specification(): Window_specificationContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Over_clauseContext extends ParserRuleContext {
    KW_OVER(): TerminalNode;
    window_specification(): Window_specificationContext | undefined;
    colid(): ColidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Window_specificationContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    CLOSE_PAREN(): TerminalNode;
    opt_existing_window_name(): Opt_existing_window_nameContext | undefined;
    opt_partition_clause(): Opt_partition_clauseContext | undefined;
    opt_sort_clause(): Opt_sort_clauseContext | undefined;
    opt_frame_clause(): Opt_frame_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_existing_window_nameContext extends ParserRuleContext {
    colid(): ColidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_partition_clauseContext extends ParserRuleContext {
    KW_PARTITION(): TerminalNode;
    KW_BY(): TerminalNode;
    expr_list(): Expr_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_frame_clauseContext extends ParserRuleContext {
    KW_RANGE(): TerminalNode | undefined;
    frame_extent(): Frame_extentContext;
    opt_window_exclusion_clause(): Opt_window_exclusion_clauseContext | undefined;
    KW_ROWS(): TerminalNode | undefined;
    KW_GROUPS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Frame_extentContext extends ParserRuleContext {
    frame_bound(): Frame_boundContext[];
    frame_bound(i: number): Frame_boundContext;
    KW_BETWEEN(): TerminalNode | undefined;
    KW_AND(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Frame_boundContext extends ParserRuleContext {
    KW_UNBOUNDED(): TerminalNode | undefined;
    KW_PRECEDING(): TerminalNode | undefined;
    KW_FOLLOWING(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_window_exclusion_clauseContext extends ParserRuleContext {
    KW_EXCLUDE(): TerminalNode;
    KW_CURRENT(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_TIES(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_OTHERS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RowContext extends ParserRuleContext {
    KW_ROW(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode;
    CLOSE_PAREN(): TerminalNode;
    expr_list(): Expr_listContext | undefined;
    COMMA(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Explicit_rowContext extends ParserRuleContext {
    KW_ROW(): TerminalNode;
    OPEN_PAREN(): TerminalNode;
    CLOSE_PAREN(): TerminalNode;
    expr_list(): Expr_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Implicit_rowContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    expr_list(): Expr_listContext;
    COMMA(): TerminalNode;
    a_expr(): A_exprContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Sub_typeContext extends ParserRuleContext {
    KW_ANY(): TerminalNode | undefined;
    KW_SOME(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class All_opContext extends ParserRuleContext {
    Operator(): TerminalNode | undefined;
    mathop(): MathopContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class MathopContext extends ParserRuleContext {
    PLUS(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    STAR(): TerminalNode | undefined;
    SLASH(): TerminalNode | undefined;
    PERCENT(): TerminalNode | undefined;
    CARET(): TerminalNode | undefined;
    LT(): TerminalNode | undefined;
    GT(): TerminalNode | undefined;
    EQUAL(): TerminalNode | undefined;
    LESS_EQUALS(): TerminalNode | undefined;
    GREATER_EQUALS(): TerminalNode | undefined;
    NOT_EQUALS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Qual_opContext extends ParserRuleContext {
    Operator(): TerminalNode | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    any_operator(): Any_operatorContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Qual_all_opContext extends ParserRuleContext {
    all_op(): All_opContext | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    any_operator(): Any_operatorContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Subquery_OpContext extends ParserRuleContext {
    all_op(): All_opContext | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    any_operator(): Any_operatorContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_LIKE(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_ILIKE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Expr_listContext extends ParserRuleContext {
    a_expr(): A_exprContext[];
    a_expr(i: number): A_exprContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Column_expr_list_noparenContext extends ParserRuleContext {
    column_expr_noparen(): Column_expr_noparenContext[];
    column_expr_noparen(i: number): Column_expr_noparenContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Column_expr_listContext extends ParserRuleContext {
    column_expr(): Column_exprContext[];
    column_expr(i: number): Column_exprContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Column_exprContext extends ParserRuleContext {
    column_name(): Column_nameContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Column_expr_noparenContext extends ParserRuleContext {
    column_name(): Column_nameContext | undefined;
    a_expr(): A_exprContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_arg_listContext extends ParserRuleContext {
    func_arg_expr(): Func_arg_exprContext[];
    func_arg_expr(i: number): Func_arg_exprContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Func_arg_exprContext extends ParserRuleContext {
    a_expr(): A_exprContext;
    param_name(): Param_nameContext | undefined;
    COLON_EQUALS(): TerminalNode | undefined;
    EQUALS_GREATER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Type_listContext extends ParserRuleContext {
    typename(): TypenameContext[];
    typename(i: number): TypenameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Array_exprContext extends ParserRuleContext {
    OPEN_BRACKET(): TerminalNode;
    CLOSE_BRACKET(): TerminalNode;
    expr_list(): Expr_listContext | undefined;
    array_expr_list(): Array_expr_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Array_expr_listContext extends ParserRuleContext {
    array_expr(): Array_exprContext[];
    array_expr(i: number): Array_exprContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Extract_listContext extends ParserRuleContext {
    extract_arg(): Extract_argContext;
    KW_FROM(): TerminalNode;
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Extract_argContext extends ParserRuleContext {
    identifier(): IdentifierContext | undefined;
    KW_YEAR(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    sconst(): SconstContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Unicode_normal_formContext extends ParserRuleContext {
    KW_NFC(): TerminalNode | undefined;
    KW_NFD(): TerminalNode | undefined;
    KW_NFKC(): TerminalNode | undefined;
    KW_NFKD(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Overlay_listContext extends ParserRuleContext {
    a_expr(): A_exprContext[];
    a_expr(i: number): A_exprContext;
    KW_PLACING(): TerminalNode;
    KW_FROM(): TerminalNode;
    KW_FOR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Position_listContext extends ParserRuleContext {
    b_expr(): B_exprContext[];
    b_expr(i: number): B_exprContext;
    KW_IN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Substr_listContext extends ParserRuleContext {
    a_expr(): A_exprContext[];
    a_expr(i: number): A_exprContext;
    KW_FROM(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_SIMILAR(): TerminalNode | undefined;
    KW_ESCAPE(): TerminalNode | undefined;
    expr_list(): Expr_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Trim_listContext extends ParserRuleContext {
    a_expr(): A_exprContext | undefined;
    KW_FROM(): TerminalNode | undefined;
    expr_list(): Expr_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class In_exprContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: In_exprContext): void;
}
export declare class In_expr_selectContext extends In_exprContext {
    select_with_parens(): Select_with_parensContext;
    constructor(ctx: In_exprContext);
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class In_expr_listContext extends In_exprContext {
    OPEN_PAREN(): TerminalNode;
    expr_list(): Expr_listContext;
    CLOSE_PAREN(): TerminalNode;
    constructor(ctx: In_exprContext);
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Case_exprContext extends ParserRuleContext {
    KW_CASE(): TerminalNode;
    when_clause_list(): When_clause_listContext;
    KW_END(): TerminalNode;
    case_arg(): Case_argContext | undefined;
    case_default(): Case_defaultContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class When_clause_listContext extends ParserRuleContext {
    when_clause(): When_clauseContext[];
    when_clause(i: number): When_clauseContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class When_clauseContext extends ParserRuleContext {
    KW_WHEN(): TerminalNode;
    a_expr(): A_exprContext[];
    a_expr(i: number): A_exprContext;
    KW_THEN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Case_defaultContext extends ParserRuleContext {
    KW_ELSE(): TerminalNode;
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Case_argContext extends ParserRuleContext {
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ColumnrefContext extends ParserRuleContext {
    colid(): ColidContext;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Indirection_elContext extends ParserRuleContext {
    DOT(): TerminalNode | undefined;
    attr_name(): Attr_nameContext | undefined;
    STAR(): TerminalNode | undefined;
    OPEN_BRACKET(): TerminalNode | undefined;
    CLOSE_BRACKET(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    COLON(): TerminalNode | undefined;
    opt_slice_bound(): Opt_slice_boundContext[];
    opt_slice_bound(i: number): Opt_slice_boundContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_slice_boundContext extends ParserRuleContext {
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class IndirectionContext extends ParserRuleContext {
    indirection_el(): Indirection_elContext[];
    indirection_el(i: number): Indirection_elContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_indirectionContext extends ParserRuleContext {
    indirection_el(): Indirection_elContext[];
    indirection_el(i: number): Indirection_elContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_target_listContext extends ParserRuleContext {
    target_list(): Target_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Target_listContext extends ParserRuleContext {
    target_el(): Target_elContext[];
    target_el(i: number): Target_elContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Target_elContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: Target_elContext): void;
}
export declare class Target_labelContext extends Target_elContext {
    column_expr_noparen(): Column_expr_noparenContext;
    KW_AS(): TerminalNode | undefined;
    collabel(): CollabelContext | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(ctx: Target_elContext);
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Target_starContext extends Target_elContext {
    STAR(): TerminalNode;
    constructor(ctx: Target_elContext);
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Qualified_name_listContext extends ParserRuleContext {
    qualified_name(): Qualified_nameContext[];
    qualified_name(i: number): Qualified_nameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Table_name_listContext extends ParserRuleContext {
    table_name(): Table_nameContext[];
    table_name(i: number): Table_nameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Schema_name_listContext extends ParserRuleContext {
    schema_name(): Schema_nameContext[];
    schema_name(i: number): Schema_nameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Database_nameListContext extends ParserRuleContext {
    database_name(): Database_nameContext[];
    database_name(i: number): Database_nameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Procedure_name_listContext extends ParserRuleContext {
    procedure_name(): Procedure_nameContext[];
    procedure_name(i: number): Procedure_nameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Tablespace_name_createContext extends ParserRuleContext {
    colid(): ColidContext;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Tablespace_nameContext extends ParserRuleContext {
    colid(): ColidContext;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Table_name_createContext extends ParserRuleContext {
    colid(): ColidContext;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Table_nameContext extends ParserRuleContext {
    colid(): ColidContext;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class View_name_createContext extends ParserRuleContext {
    colid(): ColidContext;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class View_nameContext extends ParserRuleContext {
    colid(): ColidContext;
    attrs(): AttrsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Qualified_nameContext extends ParserRuleContext {
    colid(): ColidContext;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Tablespace_name_listContext extends ParserRuleContext {
    tablespace_name(): Tablespace_nameContext[];
    tablespace_name(i: number): Tablespace_nameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Name_listContext extends ParserRuleContext {
    name(): NameContext[];
    name(i: number): NameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Database_name_createContext extends ParserRuleContext {
    colid(): ColidContext;
    attrs(): AttrsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Database_nameContext extends ParserRuleContext {
    colid(): ColidContext;
    attrs(): AttrsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Schema_nameContext extends ParserRuleContext {
    colid(): ColidContext;
    attrs(): AttrsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Routine_name_createContext extends ParserRuleContext {
    colid(): ColidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Routine_nameContext extends ParserRuleContext {
    colid(): ColidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Procedure_nameContext extends ParserRuleContext {
    type_function_name(): Type_function_nameContext | undefined;
    colid(): ColidContext | undefined;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Procedure_name_createContext extends ParserRuleContext {
    type_function_name(): Type_function_nameContext | undefined;
    colid(): ColidContext | undefined;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Column_nameContext extends ParserRuleContext {
    colid(): ColidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Column_name_createContext extends ParserRuleContext {
    colid(): ColidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class NameContext extends ParserRuleContext {
    colid(): ColidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Attr_nameContext extends ParserRuleContext {
    collabel(): CollabelContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class File_nameContext extends ParserRuleContext {
    sconst(): SconstContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Function_name_createContext extends ParserRuleContext {
    type_function_name(): Type_function_nameContext | undefined;
    colid(): ColidContext | undefined;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Function_nameContext extends ParserRuleContext {
    type_function_name(): Type_function_nameContext | undefined;
    colid(): ColidContext | undefined;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Usual_nameContext extends ParserRuleContext {
    type_usual_name(): Type_usual_nameContext | undefined;
    colid(): ColidContext | undefined;
    indirection(): IndirectionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AexprconstContext extends ParserRuleContext {
    iconst(): IconstContext | undefined;
    fconst(): FconstContext | undefined;
    sconst(): SconstContext | undefined;
    bconst(): BconstContext | undefined;
    xconst(): XconstContext | undefined;
    function_name(): Function_nameContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    func_arg_list(): Func_arg_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    opt_sort_clause(): Opt_sort_clauseContext | undefined;
    consttypename(): ConsttypenameContext | undefined;
    constinterval(): ConstintervalContext | undefined;
    opt_interval(): Opt_intervalContext | undefined;
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class XconstContext extends ParserRuleContext {
    HexadecimalStringConstant(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class BconstContext extends ParserRuleContext {
    BinaryStringConstant(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class FconstContext extends ParserRuleContext {
    Numeric(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class IconstContext extends ParserRuleContext {
    Integral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class SconstContext extends ParserRuleContext {
    anysconst(): AnysconstContext;
    opt_uescape(): Opt_uescapeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class AnysconstContext extends ParserRuleContext {
    StringConstant(): TerminalNode | undefined;
    UnicodeEscapeStringConstant(): TerminalNode | undefined;
    BeginDollarStringConstant(): TerminalNode | undefined;
    EndDollarStringConstant(): TerminalNode | undefined;
    DollarText(): TerminalNode[];
    DollarText(i: number): TerminalNode;
    EscapeStringConstant(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_uescapeContext extends ParserRuleContext {
    KW_UESCAPE(): TerminalNode;
    anysconst(): AnysconstContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class SignediconstContext extends ParserRuleContext {
    iconst(): IconstContext;
    PLUS(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class GroupnameContext extends ParserRuleContext {
    rolespec(): RolespecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RoleidContext extends ParserRuleContext {
    rolespec(): RolespecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class RolespecContext extends ParserRuleContext {
    nonreservedword(): NonreservedwordContext | undefined;
    KW_CURRENT_USER(): TerminalNode | undefined;
    KW_CURRENT_ROLE(): TerminalNode | undefined;
    KW_SESSION_USER(): TerminalNode | undefined;
    KW_PUBLIC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Role_listContext extends ParserRuleContext {
    rolespec(): RolespecContext[];
    rolespec(i: number): RolespecContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ColidContext extends ParserRuleContext {
    identifier(): IdentifierContext | undefined;
    unreserved_keyword(): Unreserved_keywordContext | undefined;
    col_name_keyword(): Col_name_keywordContext | undefined;
    plsql_unreserved_keyword(): Plsql_unreserved_keywordContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Index_method_choicesContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Exclude_elementContext extends ParserRuleContext {
    opt_definition(): Opt_definitionContext | undefined;
    identifier(): IdentifierContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    opt_asc_desc(): Opt_asc_descContext | undefined;
    opt_nulls_order(): Opt_nulls_orderContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Index_paramentersContext extends ParserRuleContext {
    KW_WITH(): TerminalNode | undefined;
    reloptions(): ReloptionsContext | undefined;
    optconstablespace(): OptconstablespaceContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Type_function_nameContext extends ParserRuleContext {
    identifier(): IdentifierContext | undefined;
    unreserved_keyword(): Unreserved_keywordContext | undefined;
    plsql_unreserved_keyword(): Plsql_unreserved_keywordContext | undefined;
    type_func_name_keyword(): Type_func_name_keywordContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Type_usual_nameContext extends ParserRuleContext {
    identifier(): IdentifierContext | undefined;
    unreserved_keyword(): Unreserved_keywordContext | undefined;
    plsql_unreserved_keyword(): Plsql_unreserved_keywordContext | undefined;
    type_func_name_keyword(): Type_func_name_keywordContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Nonreservedword_columnContext extends ParserRuleContext {
    column_name(): Column_nameContext | undefined;
    type_func_name_keyword(): Type_func_name_keywordContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class NonreservedwordContext extends ParserRuleContext {
    identifier(): IdentifierContext | undefined;
    unreserved_keyword(): Unreserved_keywordContext | undefined;
    col_name_keyword(): Col_name_keywordContext | undefined;
    type_func_name_keyword(): Type_func_name_keywordContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class CollabelContext extends ParserRuleContext {
    identifier(): IdentifierContext | undefined;
    plsql_unreserved_keyword(): Plsql_unreserved_keywordContext | undefined;
    unreserved_keyword(): Unreserved_keywordContext | undefined;
    col_name_keyword(): Col_name_keywordContext | undefined;
    type_func_name_keyword(): Type_func_name_keywordContext | undefined;
    reserved_keyword(): Reserved_keywordContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class IdentifierContext extends ParserRuleContext {
    Identifier(): TerminalNode | undefined;
    opt_uescape(): Opt_uescapeContext | undefined;
    sconst(): SconstContext | undefined;
    QuotedIdentifier(): TerminalNode | undefined;
    UnicodeQuotedIdentifier(): TerminalNode | undefined;
    plsqlvariablename(): PlsqlvariablenameContext | undefined;
    plsqlidentifier(): PlsqlidentifierContext | undefined;
    plsql_unreserved_keyword(): Plsql_unreserved_keywordContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class PlsqlidentifierContext extends ParserRuleContext {
    PLSQLIDENTIFIER(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Unreserved_keywordContext extends ParserRuleContext {
    KW_ABORT(): TerminalNode | undefined;
    KW_ABSOLUTE(): TerminalNode | undefined;
    KW_ACCESS(): TerminalNode | undefined;
    KW_ACTION(): TerminalNode | undefined;
    KW_ADD(): TerminalNode | undefined;
    KW_ADMIN(): TerminalNode | undefined;
    KW_AFTER(): TerminalNode | undefined;
    KW_AGGREGATE(): TerminalNode | undefined;
    KW_ALSO(): TerminalNode | undefined;
    KW_ALTER(): TerminalNode | undefined;
    KW_ALWAYS(): TerminalNode | undefined;
    KW_ASSERTION(): TerminalNode | undefined;
    KW_ASSIGNMENT(): TerminalNode | undefined;
    KW_AT(): TerminalNode | undefined;
    KW_ATTACH(): TerminalNode | undefined;
    KW_ATTRIBUTE(): TerminalNode | undefined;
    KW_BACKWARD(): TerminalNode | undefined;
    KW_BEFORE(): TerminalNode | undefined;
    KW_BEGIN(): TerminalNode | undefined;
    KW_BUFFER_USAGE_LIMIT(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    KW_CACHE(): TerminalNode | undefined;
    KW_CALL(): TerminalNode | undefined;
    KW_CALLED(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    KW_CASCADED(): TerminalNode | undefined;
    KW_CATALOG(): TerminalNode | undefined;
    KW_CHAIN(): TerminalNode | undefined;
    KW_CHARACTERISTICS(): TerminalNode | undefined;
    KW_CHECKPOINT(): TerminalNode | undefined;
    KW_CLASS(): TerminalNode | undefined;
    KW_CLOSE(): TerminalNode | undefined;
    KW_CLUSTER(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    KW_COMMENTS(): TerminalNode | undefined;
    KW_COMMIT(): TerminalNode | undefined;
    KW_COMMITTED(): TerminalNode | undefined;
    KW_CONFIGURATION(): TerminalNode | undefined;
    KW_CONFLICT(): TerminalNode | undefined;
    KW_CONNECTION(): TerminalNode | undefined;
    KW_CONSTRAINTS(): TerminalNode | undefined;
    KW_CONTENT(): TerminalNode | undefined;
    KW_CONTINUE(): TerminalNode | undefined;
    KW_CONVERSION(): TerminalNode | undefined;
    KW_COPY(): TerminalNode | undefined;
    KW_COST(): TerminalNode | undefined;
    KW_CSV(): TerminalNode | undefined;
    KW_CUBE(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_CURSOR(): TerminalNode | undefined;
    KW_CYCLE(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_DEALLOCATE(): TerminalNode | undefined;
    KW_DECLARE(): TerminalNode | undefined;
    KW_DEFAULTS(): TerminalNode | undefined;
    KW_DEFERRED(): TerminalNode | undefined;
    KW_DEFINER(): TerminalNode | undefined;
    KW_DELETE(): TerminalNode | undefined;
    KW_DELIMITER(): TerminalNode | undefined;
    KW_DELIMITERS(): TerminalNode | undefined;
    KW_DEPENDS(): TerminalNode | undefined;
    KW_DETACH(): TerminalNode | undefined;
    KW_DICTIONARY(): TerminalNode | undefined;
    KW_DISABLE(): TerminalNode | undefined;
    KW_DISCARD(): TerminalNode | undefined;
    KW_DOCUMENT(): TerminalNode | undefined;
    KW_DOMAIN(): TerminalNode | undefined;
    KW_DOUBLE(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_EACH(): TerminalNode | undefined;
    KW_ENABLE(): TerminalNode | undefined;
    KW_ENCODING(): TerminalNode | undefined;
    KW_ENCRYPTED(): TerminalNode | undefined;
    KW_ENUM(): TerminalNode | undefined;
    KW_ESCAPE(): TerminalNode | undefined;
    KW_EVENT(): TerminalNode | undefined;
    KW_EXCLUDE(): TerminalNode | undefined;
    KW_EXCLUDING(): TerminalNode | undefined;
    KW_EXCLUSIVE(): TerminalNode | undefined;
    KW_EXECUTE(): TerminalNode | undefined;
    KW_EXPLAIN(): TerminalNode | undefined;
    KW_EXPRESSION(): TerminalNode | undefined;
    KW_EXTENSION(): TerminalNode | undefined;
    KW_EXTERNAL(): TerminalNode | undefined;
    KW_FAMILY(): TerminalNode | undefined;
    KW_FILTER(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_FOLLOWING(): TerminalNode | undefined;
    KW_FORCE(): TerminalNode | undefined;
    KW_FORWARD(): TerminalNode | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    KW_FUNCTIONS(): TerminalNode | undefined;
    KW_GENERATED(): TerminalNode | undefined;
    KW_GLOBAL(): TerminalNode | undefined;
    KW_GRANTED(): TerminalNode | undefined;
    KW_GROUPS(): TerminalNode | undefined;
    KW_HANDLER(): TerminalNode | undefined;
    KW_HEADER(): TerminalNode | undefined;
    KW_HOLD(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_IDENTITY(): TerminalNode | undefined;
    KW_IF(): TerminalNode | undefined;
    KW_IMMEDIATE(): TerminalNode | undefined;
    KW_IMMUTABLE(): TerminalNode | undefined;
    KW_IMPLICIT(): TerminalNode | undefined;
    KW_IMPORT(): TerminalNode | undefined;
    KW_INCLUDE(): TerminalNode | undefined;
    KW_INCLUDING(): TerminalNode | undefined;
    KW_INCREMENT(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    KW_INDEXES(): TerminalNode | undefined;
    KW_INHERIT(): TerminalNode | undefined;
    KW_INHERITS(): TerminalNode | undefined;
    KW_INLINE(): TerminalNode | undefined;
    KW_INPUT(): TerminalNode | undefined;
    KW_INSENSITIVE(): TerminalNode | undefined;
    KW_INSERT(): TerminalNode | undefined;
    KW_INSTEAD(): TerminalNode | undefined;
    KW_INVOKER(): TerminalNode | undefined;
    KW_ISOLATION(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    KW_LABEL(): TerminalNode | undefined;
    KW_LANGUAGE(): TerminalNode | undefined;
    KW_LARGE(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    KW_LEAKPROOF(): TerminalNode | undefined;
    KW_LEVEL(): TerminalNode | undefined;
    KW_LISTEN(): TerminalNode | undefined;
    KW_LOAD(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    KW_LOCATION(): TerminalNode | undefined;
    KW_LOCK(): TerminalNode | undefined;
    KW_LOCKED(): TerminalNode | undefined;
    KW_LOGGED(): TerminalNode | undefined;
    KW_MAPPING(): TerminalNode | undefined;
    KW_MATCH(): TerminalNode | undefined;
    KW_MATERIALIZED(): TerminalNode | undefined;
    KW_MAXVALUE(): TerminalNode | undefined;
    KW_METHOD(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    KW_MINVALUE(): TerminalNode | undefined;
    KW_MODE(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_MOVE(): TerminalNode | undefined;
    KW_NAME(): TerminalNode | undefined;
    KW_NAMES(): TerminalNode | undefined;
    KW_NEW(): TerminalNode | undefined;
    KW_NEXT(): TerminalNode | undefined;
    KW_NFC(): TerminalNode | undefined;
    KW_NFD(): TerminalNode | undefined;
    KW_NFKC(): TerminalNode | undefined;
    KW_NFKD(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_NORMALIZED(): TerminalNode | undefined;
    KW_NOTHING(): TerminalNode | undefined;
    KW_NOTIFY(): TerminalNode | undefined;
    KW_NOWAIT(): TerminalNode | undefined;
    KW_NULLS(): TerminalNode | undefined;
    KW_OBJECT(): TerminalNode | undefined;
    KW_OF(): TerminalNode | undefined;
    KW_OFF(): TerminalNode | undefined;
    KW_OIDS(): TerminalNode | undefined;
    KW_OLD(): TerminalNode | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_OPTIONS(): TerminalNode | undefined;
    KW_ORDINALITY(): TerminalNode | undefined;
    KW_OTHERS(): TerminalNode | undefined;
    KW_OVER(): TerminalNode | undefined;
    KW_OVERRIDING(): TerminalNode | undefined;
    KW_OWNED(): TerminalNode | undefined;
    KW_OWNER(): TerminalNode | undefined;
    KW_PARALLEL(): TerminalNode | undefined;
    KW_PARSER(): TerminalNode | undefined;
    KW_PARTIAL(): TerminalNode | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    KW_PASSING(): TerminalNode | undefined;
    KW_PASSWORD(): TerminalNode | undefined;
    KW_PLANS(): TerminalNode | undefined;
    KW_POLICY(): TerminalNode | undefined;
    KW_PRECEDING(): TerminalNode | undefined;
    KW_PREPARE(): TerminalNode | undefined;
    KW_PREPARED(): TerminalNode | undefined;
    KW_PRESERVE(): TerminalNode | undefined;
    KW_PRIOR(): TerminalNode | undefined;
    KW_PRIVILEGES(): TerminalNode | undefined;
    KW_PROCEDURAL(): TerminalNode | undefined;
    KW_PROCEDURE(): TerminalNode | undefined;
    KW_PROCEDURES(): TerminalNode | undefined;
    KW_PROGRAM(): TerminalNode | undefined;
    KW_PUBLICATION(): TerminalNode | undefined;
    KW_QUOTE(): TerminalNode | undefined;
    KW_RANGE(): TerminalNode | undefined;
    KW_READ(): TerminalNode | undefined;
    KW_REASSIGN(): TerminalNode | undefined;
    KW_RECHECK(): TerminalNode | undefined;
    KW_RECURSIVE(): TerminalNode | undefined;
    KW_REF(): TerminalNode | undefined;
    KW_REFERENCING(): TerminalNode | undefined;
    KW_REFRESH(): TerminalNode | undefined;
    KW_REINDEX(): TerminalNode | undefined;
    KW_RELATIVE(): TerminalNode | undefined;
    KW_RELEASE(): TerminalNode | undefined;
    KW_RENAME(): TerminalNode | undefined;
    KW_REPEATABLE(): TerminalNode | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    KW_REPLICA(): TerminalNode | undefined;
    KW_RESET(): TerminalNode | undefined;
    KW_RESTART(): TerminalNode | undefined;
    KW_RESTRICT(): TerminalNode | undefined;
    KW_RETURNS(): TerminalNode | undefined;
    KW_REVOKE(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_ROLLBACK(): TerminalNode | undefined;
    KW_ROLLUP(): TerminalNode | undefined;
    KW_ROUTINE(): TerminalNode | undefined;
    KW_ROUTINES(): TerminalNode | undefined;
    KW_ROWS(): TerminalNode | undefined;
    KW_RULE(): TerminalNode | undefined;
    KW_SAVEPOINT(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    KW_SCHEMAS(): TerminalNode | undefined;
    KW_SCROLL(): TerminalNode | undefined;
    KW_SEARCH(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    KW_SECURITY(): TerminalNode | undefined;
    KW_SEQUENCE(): TerminalNode | undefined;
    KW_SEQUENCES(): TerminalNode | undefined;
    KW_SERIALIZABLE(): TerminalNode | undefined;
    KW_SERVER(): TerminalNode | undefined;
    KW_SESSION(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_SETS(): TerminalNode | undefined;
    KW_SHARE(): TerminalNode | undefined;
    KW_SHOW(): TerminalNode | undefined;
    KW_SIMPLE(): TerminalNode | undefined;
    KW_SKIP(): TerminalNode | undefined;
    KW_SKIP_LOCKED(): TerminalNode | undefined;
    KW_SNAPSHOT(): TerminalNode | undefined;
    KW_SQL(): TerminalNode | undefined;
    KW_STABLE(): TerminalNode | undefined;
    KW_STANDALONE(): TerminalNode | undefined;
    KW_START(): TerminalNode | undefined;
    KW_STATEMENT(): TerminalNode | undefined;
    KW_STATISTICS(): TerminalNode | undefined;
    KW_STDIN(): TerminalNode | undefined;
    KW_STDOUT(): TerminalNode | undefined;
    KW_STORAGE(): TerminalNode | undefined;
    KW_STORED(): TerminalNode | undefined;
    KW_STRICT(): TerminalNode | undefined;
    KW_STRIP(): TerminalNode | undefined;
    KW_SUBSCRIPTION(): TerminalNode | undefined;
    KW_SUPPORT(): TerminalNode | undefined;
    KW_SYSID(): TerminalNode | undefined;
    KW_SYSTEM(): TerminalNode | undefined;
    KW_TABLES(): TerminalNode | undefined;
    KW_TABLESPACE(): TerminalNode | undefined;
    KW_TEMP(): TerminalNode | undefined;
    KW_TEMPLATE(): TerminalNode | undefined;
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_TEXT(): TerminalNode | undefined;
    KW_TIES(): TerminalNode | undefined;
    KW_TRANSACTION(): TerminalNode | undefined;
    KW_TRANSFORM(): TerminalNode | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    KW_TRUNCATE(): TerminalNode | undefined;
    KW_TRUSTED(): TerminalNode | undefined;
    KW_TYPE(): TerminalNode | undefined;
    KW_TYPES(): TerminalNode | undefined;
    KW_UESCAPE(): TerminalNode | undefined;
    KW_UNBOUNDED(): TerminalNode | undefined;
    KW_UNCOMMITTED(): TerminalNode | undefined;
    KW_UNENCRYPTED(): TerminalNode | undefined;
    KW_UNKNOWN(): TerminalNode | undefined;
    KW_UNLISTEN(): TerminalNode | undefined;
    KW_UNLOGGED(): TerminalNode | undefined;
    KW_UNTIL(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    KW_VACUUM(): TerminalNode | undefined;
    KW_VALID(): TerminalNode | undefined;
    KW_VALIDATE(): TerminalNode | undefined;
    KW_VALIDATOR(): TerminalNode | undefined;
    KW_VALUE(): TerminalNode | undefined;
    KW_VARYING(): TerminalNode | undefined;
    KW_VERSION(): TerminalNode | undefined;
    KW_VIEW(): TerminalNode | undefined;
    KW_VIEWS(): TerminalNode | undefined;
    KW_VOLATILE(): TerminalNode | undefined;
    KW_WHITESPACE(): TerminalNode | undefined;
    KW_WITHIN(): TerminalNode | undefined;
    KW_WITHOUT(): TerminalNode | undefined;
    KW_WORK(): TerminalNode | undefined;
    KW_WRAPPER(): TerminalNode | undefined;
    KW_WRITE(): TerminalNode | undefined;
    KW_XML(): TerminalNode | undefined;
    KW_YEAR(): TerminalNode | undefined;
    KW_YES(): TerminalNode | undefined;
    KW_ZONE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Col_name_keywordContext extends ParserRuleContext {
    KW_BETWEEN(): TerminalNode | undefined;
    KW_BIGINT(): TerminalNode | undefined;
    bit(): BitContext | undefined;
    KW_BOOLEAN(): TerminalNode | undefined;
    KW_CHAR(): TerminalNode | undefined;
    character(): CharacterContext | undefined;
    KW_COALESCE(): TerminalNode | undefined;
    KW_DEC(): TerminalNode | undefined;
    KW_DECIMAL(): TerminalNode | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    KW_EXTRACT(): TerminalNode | undefined;
    KW_FLOAT(): TerminalNode | undefined;
    KW_GREATEST(): TerminalNode | undefined;
    KW_GROUPING(): TerminalNode | undefined;
    KW_INOUT(): TerminalNode | undefined;
    KW_INT(): TerminalNode | undefined;
    KW_INTEGER(): TerminalNode | undefined;
    KW_INTERVAL(): TerminalNode | undefined;
    KW_LEAST(): TerminalNode | undefined;
    KW_NATIONAL(): TerminalNode | undefined;
    KW_NCHAR(): TerminalNode | undefined;
    KW_NONE(): TerminalNode | undefined;
    KW_NORMALIZE(): TerminalNode | undefined;
    KW_NULLIF(): TerminalNode | undefined;
    numeric(): NumericContext | undefined;
    KW_OUT(): TerminalNode | undefined;
    KW_OVERLAY(): TerminalNode | undefined;
    KW_POSITION(): TerminalNode | undefined;
    KW_PRECISION(): TerminalNode | undefined;
    KW_REAL(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_SETOF(): TerminalNode | undefined;
    KW_SMALLINT(): TerminalNode | undefined;
    KW_SUBSTRING(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_TIMESTAMP(): TerminalNode | undefined;
    KW_TREAT(): TerminalNode | undefined;
    KW_TRIM(): TerminalNode | undefined;
    KW_VALUES(): TerminalNode | undefined;
    KW_VARCHAR(): TerminalNode | undefined;
    KW_XMLATTRIBUTES(): TerminalNode | undefined;
    KW_XMLCONCAT(): TerminalNode | undefined;
    KW_XMLELEMENT(): TerminalNode | undefined;
    KW_XMLEXISTS(): TerminalNode | undefined;
    KW_XMLFOREST(): TerminalNode | undefined;
    KW_XMLNAMESPACES(): TerminalNode | undefined;
    KW_XMLPARSE(): TerminalNode | undefined;
    KW_XMLPI(): TerminalNode | undefined;
    KW_XMLROOT(): TerminalNode | undefined;
    KW_XMLSERIALIZE(): TerminalNode | undefined;
    KW_XMLTABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Type_func_name_keywordContext extends ParserRuleContext {
    KW_AUTHORIZATION(): TerminalNode | undefined;
    KW_BINARY(): TerminalNode | undefined;
    KW_COLLATION(): TerminalNode | undefined;
    KW_CONCURRENTLY(): TerminalNode | undefined;
    KW_CROSS(): TerminalNode | undefined;
    KW_CURRENT_SCHEMA(): TerminalNode | undefined;
    KW_FREEZE(): TerminalNode | undefined;
    KW_FULL(): TerminalNode | undefined;
    KW_ILIKE(): TerminalNode | undefined;
    KW_INNER(): TerminalNode | undefined;
    KW_IS(): TerminalNode | undefined;
    KW_ISNULL(): TerminalNode | undefined;
    KW_JOIN(): TerminalNode | undefined;
    KW_LEFT(): TerminalNode | undefined;
    KW_LIKE(): TerminalNode | undefined;
    KW_NATURAL(): TerminalNode | undefined;
    KW_NOTNULL(): TerminalNode | undefined;
    KW_OUTER(): TerminalNode | undefined;
    KW_OVERLAPS(): TerminalNode | undefined;
    KW_RIGHT(): TerminalNode | undefined;
    KW_SIMILAR(): TerminalNode | undefined;
    KW_TABLESAMPLE(): TerminalNode | undefined;
    KW_VERBOSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Reserved_keywordContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    KW_ANALYSE(): TerminalNode | undefined;
    KW_ANALYZE(): TerminalNode | undefined;
    KW_AND(): TerminalNode | undefined;
    KW_ANY(): TerminalNode | undefined;
    KW_ARRAY(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    KW_ASC(): TerminalNode | undefined;
    KW_ASYMMETRIC(): TerminalNode | undefined;
    KW_BOTH(): TerminalNode | undefined;
    KW_CASE(): TerminalNode | undefined;
    KW_CAST(): TerminalNode | undefined;
    KW_CHECK(): TerminalNode | undefined;
    KW_COLLATE(): TerminalNode | undefined;
    KW_COLUMN(): TerminalNode | undefined;
    KW_CONSTRAINT(): TerminalNode | undefined;
    KW_CREATE(): TerminalNode | undefined;
    KW_CURRENT_CATALOG(): TerminalNode | undefined;
    KW_CURRENT_DATE(): TerminalNode | undefined;
    KW_CURRENT_ROLE(): TerminalNode | undefined;
    KW_CURRENT_TIME(): TerminalNode | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    KW_CURRENT_USER(): TerminalNode | undefined;
    KW_DEFERRABLE(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    KW_DO(): TerminalNode | undefined;
    KW_ELSE(): TerminalNode | undefined;
    KW_END(): TerminalNode | undefined;
    KW_EXCEPT(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    KW_FETCH(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_FOREIGN(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_GRANT(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_HAVING(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_INITIALLY(): TerminalNode | undefined;
    KW_INTERSECT(): TerminalNode | undefined;
    KW_LATERAL(): TerminalNode | undefined;
    KW_LEADING(): TerminalNode | undefined;
    KW_LIMIT(): TerminalNode | undefined;
    KW_LOCALTIME(): TerminalNode | undefined;
    KW_LOCALTIMESTAMP(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_OFFSET(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_ONLY(): TerminalNode | undefined;
    KW_OR(): TerminalNode | undefined;
    KW_ORDER(): TerminalNode | undefined;
    KW_PLACING(): TerminalNode | undefined;
    KW_PRIMARY(): TerminalNode | undefined;
    KW_REFERENCES(): TerminalNode | undefined;
    KW_RETURNING(): TerminalNode | undefined;
    KW_SELECT(): TerminalNode | undefined;
    KW_SESSION_USER(): TerminalNode | undefined;
    KW_SOME(): TerminalNode | undefined;
    KW_SYMMETRIC(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    KW_THEN(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    KW_TRAILING(): TerminalNode | undefined;
    KW_TRUE(): TerminalNode | undefined;
    KW_UNION(): TerminalNode | undefined;
    KW_UNIQUE(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_USING(): TerminalNode | undefined;
    KW_VARIADIC(): TerminalNode | undefined;
    KW_WHEN(): TerminalNode | undefined;
    KW_WHERE(): TerminalNode | undefined;
    KW_WINDOW(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Pl_functionContext extends ParserRuleContext {
    comp_options(): Comp_optionsContext;
    pl_block(): Pl_blockContext;
    opt_semi(): Opt_semiContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Comp_optionsContext extends ParserRuleContext {
    comp_option(): Comp_optionContext[];
    comp_option(i: number): Comp_optionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Comp_optionContext extends ParserRuleContext {
    sharp(): SharpContext;
    KW_OPTION(): TerminalNode | undefined;
    KW_DUMP(): TerminalNode | undefined;
    KW_PRINT_STRICT_PARAMS(): TerminalNode | undefined;
    option_value(): Option_valueContext | undefined;
    KW_VARIABLE_CONFLICT(): TerminalNode | undefined;
    KW_ERROR(): TerminalNode | undefined;
    KW_USE_VARIABLE(): TerminalNode | undefined;
    KW_USE_COLUMN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class SharpContext extends ParserRuleContext {
    Operator(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Option_valueContext extends ParserRuleContext {
    sconst(): SconstContext | undefined;
    reserved_keyword(): Reserved_keywordContext | undefined;
    plsql_unreserved_keyword(): Plsql_unreserved_keywordContext | undefined;
    unreserved_keyword(): Unreserved_keywordContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_semiContext extends ParserRuleContext {
    SEMI(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Pl_blockContext extends ParserRuleContext {
    decl_sect(): Decl_sectContext;
    KW_BEGIN(): TerminalNode;
    proc_sect(): Proc_sectContext;
    exception_sect(): Exception_sectContext;
    KW_END(): TerminalNode;
    opt_label(): Opt_labelContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_sectContext extends ParserRuleContext {
    opt_block_label(): Opt_block_labelContext;
    decl_start(): Decl_startContext | undefined;
    decl_stmts(): Decl_stmtsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_startContext extends ParserRuleContext {
    KW_DECLARE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_stmtsContext extends ParserRuleContext {
    decl_stmt(): Decl_stmtContext[];
    decl_stmt(i: number): Decl_stmtContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Label_declContext extends ParserRuleContext {
    LESS_LESS(): TerminalNode;
    any_identifier(): Any_identifierContext;
    GREATER_GREATER(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_stmtContext extends ParserRuleContext {
    decl_statement(): Decl_statementContext | undefined;
    KW_DECLARE(): TerminalNode | undefined;
    label_decl(): Label_declContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_statementContext extends ParserRuleContext {
    decl_varname(): Decl_varnameContext;
    SEMI(): TerminalNode;
    KW_ALIAS(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    decl_aliasitem(): Decl_aliasitemContext | undefined;
    decl_const(): Decl_constContext | undefined;
    decl_datatype(): Decl_datatypeContext | undefined;
    decl_collate(): Decl_collateContext | undefined;
    decl_notnull(): Decl_notnullContext | undefined;
    decl_defval(): Decl_defvalContext | undefined;
    opt_scrollable(): Opt_scrollableContext | undefined;
    KW_CURSOR(): TerminalNode | undefined;
    decl_cursor_args(): Decl_cursor_argsContext | undefined;
    decl_is_for(): Decl_is_forContext | undefined;
    decl_cursor_query(): Decl_cursor_queryContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_scrollableContext extends ParserRuleContext {
    KW_NO(): TerminalNode | undefined;
    KW_SCROLL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_cursor_queryContext extends ParserRuleContext {
    selectstmt(): SelectstmtContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_cursor_argsContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode | undefined;
    decl_cursor_arglist(): Decl_cursor_arglistContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_cursor_arglistContext extends ParserRuleContext {
    decl_cursor_arg(): Decl_cursor_argContext[];
    decl_cursor_arg(i: number): Decl_cursor_argContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_cursor_argContext extends ParserRuleContext {
    decl_varname(): Decl_varnameContext;
    decl_datatype(): Decl_datatypeContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_is_forContext extends ParserRuleContext {
    KW_IS(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_aliasitemContext extends ParserRuleContext {
    PARAM(): TerminalNode | undefined;
    colid(): ColidContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_varnameContext extends ParserRuleContext {
    any_identifier(): Any_identifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_constContext extends ParserRuleContext {
    KW_CONSTANT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_datatypeContext extends ParserRuleContext {
    typename(): TypenameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_collateContext extends ParserRuleContext {
    KW_COLLATE(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_notnullContext extends ParserRuleContext {
    KW_NOT(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_defvalContext extends ParserRuleContext {
    decl_defkey(): Decl_defkeyContext | undefined;
    sql_expression(): Sql_expressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Decl_defkeyContext extends ParserRuleContext {
    assign_operator(): Assign_operatorContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Assign_operatorContext extends ParserRuleContext {
    EQUAL(): TerminalNode | undefined;
    COLON_EQUALS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Proc_sectContext extends ParserRuleContext {
    proc_stmt(): Proc_stmtContext[];
    proc_stmt(i: number): Proc_stmtContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Proc_stmtContext extends ParserRuleContext {
    pl_block(): Pl_blockContext | undefined;
    SEMI(): TerminalNode | undefined;
    stmt_return(): Stmt_returnContext | undefined;
    stmt_raise(): Stmt_raiseContext | undefined;
    stmt_assign(): Stmt_assignContext | undefined;
    stmt_if(): Stmt_ifContext | undefined;
    stmt_case(): Stmt_caseContext | undefined;
    stmt_loop(): Stmt_loopContext | undefined;
    stmt_while(): Stmt_whileContext | undefined;
    stmt_for(): Stmt_forContext | undefined;
    stmt_foreach_a(): Stmt_foreach_aContext | undefined;
    stmt_exit(): Stmt_exitContext | undefined;
    stmt_assert(): Stmt_assertContext | undefined;
    stmt_execsql(): Stmt_execsqlContext | undefined;
    stmt_dynexecute(): Stmt_dynexecuteContext | undefined;
    stmt_perform(): Stmt_performContext | undefined;
    stmt_call(): Stmt_callContext | undefined;
    stmt_getdiag(): Stmt_getdiagContext | undefined;
    stmt_open(): Stmt_openContext | undefined;
    stmt_fetch(): Stmt_fetchContext | undefined;
    stmt_move(): Stmt_moveContext | undefined;
    stmt_close(): Stmt_closeContext | undefined;
    stmt_null(): Stmt_nullContext | undefined;
    stmt_commit(): Stmt_commitContext | undefined;
    stmt_rollback(): Stmt_rollbackContext | undefined;
    stmt_set(): Stmt_setContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_performContext extends ParserRuleContext {
    KW_PERFORM(): TerminalNode;
    expr_until_semi(): Expr_until_semiContext;
    SEMI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_callContext extends ParserRuleContext {
    KW_CALL(): TerminalNode | undefined;
    any_identifier(): Any_identifierContext;
    OPEN_PAREN(): TerminalNode | undefined;
    opt_expr_list(): Opt_expr_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    SEMI(): TerminalNode | undefined;
    KW_DO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_expr_listContext extends ParserRuleContext {
    expr_list(): Expr_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_assignContext extends ParserRuleContext {
    assign_var(): Assign_varContext;
    assign_operator(): Assign_operatorContext;
    sql_expression(): Sql_expressionContext;
    SEMI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_getdiagContext extends ParserRuleContext {
    KW_GET(): TerminalNode;
    getdiag_area_opt(): Getdiag_area_optContext;
    KW_DIAGNOSTICS(): TerminalNode;
    getdiag_list(): Getdiag_listContext;
    SEMI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Getdiag_area_optContext extends ParserRuleContext {
    KW_CURRENT(): TerminalNode | undefined;
    KW_STACKED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Getdiag_listContext extends ParserRuleContext {
    getdiag_list_item(): Getdiag_list_itemContext[];
    getdiag_list_item(i: number): Getdiag_list_itemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Getdiag_list_itemContext extends ParserRuleContext {
    getdiag_target(): Getdiag_targetContext;
    assign_operator(): Assign_operatorContext;
    getdiag_item(): Getdiag_itemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Getdiag_itemContext extends ParserRuleContext {
    colid(): ColidContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Getdiag_targetContext extends ParserRuleContext {
    assign_var(): Assign_varContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Assign_varContext extends ParserRuleContext {
    any_name(): Any_nameContext | undefined;
    PARAM(): TerminalNode | undefined;
    OPEN_BRACKET(): TerminalNode[];
    OPEN_BRACKET(i: number): TerminalNode;
    expr_until_rightbracket(): Expr_until_rightbracketContext[];
    expr_until_rightbracket(i: number): Expr_until_rightbracketContext;
    CLOSE_BRACKET(): TerminalNode[];
    CLOSE_BRACKET(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_ifContext extends ParserRuleContext {
    KW_IF(): TerminalNode[];
    KW_IF(i: number): TerminalNode;
    expr_until_then(): Expr_until_thenContext;
    KW_THEN(): TerminalNode;
    proc_sect(): Proc_sectContext;
    stmt_elsifs(): Stmt_elsifsContext;
    stmt_else(): Stmt_elseContext;
    KW_END(): TerminalNode;
    SEMI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_elsifsContext extends ParserRuleContext {
    KW_ELSIF(): TerminalNode[];
    KW_ELSIF(i: number): TerminalNode;
    a_expr(): A_exprContext[];
    a_expr(i: number): A_exprContext;
    KW_THEN(): TerminalNode[];
    KW_THEN(i: number): TerminalNode;
    proc_sect(): Proc_sectContext[];
    proc_sect(i: number): Proc_sectContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_elseContext extends ParserRuleContext {
    KW_ELSE(): TerminalNode | undefined;
    proc_sect(): Proc_sectContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_caseContext extends ParserRuleContext {
    KW_CASE(): TerminalNode[];
    KW_CASE(i: number): TerminalNode;
    opt_expr_until_when(): Opt_expr_until_whenContext;
    case_when_list(): Case_when_listContext;
    opt_case_else(): Opt_case_elseContext;
    KW_END(): TerminalNode;
    SEMI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_expr_until_whenContext extends ParserRuleContext {
    sql_expression(): Sql_expressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Case_when_listContext extends ParserRuleContext {
    case_when(): Case_whenContext[];
    case_when(i: number): Case_whenContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Case_whenContext extends ParserRuleContext {
    KW_WHEN(): TerminalNode;
    expr_list(): Expr_listContext;
    KW_THEN(): TerminalNode;
    proc_sect(): Proc_sectContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_case_elseContext extends ParserRuleContext {
    KW_ELSE(): TerminalNode | undefined;
    proc_sect(): Proc_sectContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_loopContext extends ParserRuleContext {
    opt_loop_label(): Opt_loop_labelContext;
    loop_body(): Loop_bodyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_whileContext extends ParserRuleContext {
    opt_loop_label(): Opt_loop_labelContext;
    KW_WHILE(): TerminalNode;
    expr_until_loop(): Expr_until_loopContext;
    loop_body(): Loop_bodyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_forContext extends ParserRuleContext {
    opt_loop_label(): Opt_loop_labelContext;
    KW_FOR(): TerminalNode;
    for_control(): For_controlContext;
    loop_body(): Loop_bodyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class For_controlContext extends ParserRuleContext {
    for_variable(): For_variableContext;
    KW_IN(): TerminalNode;
    cursor_name(): Cursor_nameContext | undefined;
    opt_cursor_parameters(): Opt_cursor_parametersContext | undefined;
    selectstmt(): SelectstmtContext | undefined;
    explainstmt(): ExplainstmtContext | undefined;
    KW_EXECUTE(): TerminalNode | undefined;
    a_expr(): A_exprContext[];
    a_expr(i: number): A_exprContext;
    opt_for_using_expression(): Opt_for_using_expressionContext | undefined;
    opt_reverse(): Opt_reverseContext | undefined;
    DOT_DOT(): TerminalNode | undefined;
    opt_by_expression(): Opt_by_expressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_for_using_expressionContext extends ParserRuleContext {
    KW_USING(): TerminalNode | undefined;
    expr_list(): Expr_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_cursor_parametersContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode | undefined;
    a_expr(): A_exprContext[];
    a_expr(i: number): A_exprContext;
    CLOSE_PAREN(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_reverseContext extends ParserRuleContext {
    KW_REVERSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_by_expressionContext extends ParserRuleContext {
    KW_BY(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class For_variableContext extends ParserRuleContext {
    any_name_list(): Any_name_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_foreach_aContext extends ParserRuleContext {
    opt_loop_label(): Opt_loop_labelContext;
    KW_FOREACH(): TerminalNode;
    for_variable(): For_variableContext;
    foreach_slice(): Foreach_sliceContext;
    KW_IN(): TerminalNode;
    KW_ARRAY(): TerminalNode;
    a_expr(): A_exprContext;
    loop_body(): Loop_bodyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Foreach_sliceContext extends ParserRuleContext {
    KW_SLICE(): TerminalNode | undefined;
    iconst(): IconstContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_exitContext extends ParserRuleContext {
    exit_type(): Exit_typeContext;
    opt_label(): Opt_labelContext;
    SEMI(): TerminalNode;
    opt_exitcond(): Opt_exitcondContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Exit_typeContext extends ParserRuleContext {
    KW_EXIT(): TerminalNode | undefined;
    KW_CONTINUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_returnContext extends ParserRuleContext {
    KW_RETURN(): TerminalNode;
    SEMI(): TerminalNode;
    KW_NEXT(): TerminalNode | undefined;
    sql_expression(): Sql_expressionContext | undefined;
    KW_QUERY(): TerminalNode | undefined;
    opt_return_result(): Opt_return_resultContext | undefined;
    KW_EXECUTE(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    opt_for_using_expression(): Opt_for_using_expressionContext | undefined;
    selectstmt(): SelectstmtContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_return_resultContext extends ParserRuleContext {
    sql_expression(): Sql_expressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_raiseContext extends ParserRuleContext {
    KW_RAISE(): TerminalNode;
    sconst(): SconstContext | undefined;
    opt_raise_list(): Opt_raise_listContext | undefined;
    opt_raise_using(): Opt_raise_usingContext | undefined;
    SEMI(): TerminalNode | undefined;
    opt_stmt_raise_level(): Opt_stmt_raise_levelContext | undefined;
    identifier(): IdentifierContext | undefined;
    KW_SQLSTATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_stmt_raise_levelContext extends ParserRuleContext {
    KW_DEBUG(): TerminalNode | undefined;
    KW_LOG(): TerminalNode | undefined;
    KW_INFO(): TerminalNode | undefined;
    KW_NOTICE(): TerminalNode | undefined;
    KW_WARNING(): TerminalNode | undefined;
    KW_EXCEPTION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_raise_listContext extends ParserRuleContext {
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    a_expr(): A_exprContext[];
    a_expr(i: number): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_raise_usingContext extends ParserRuleContext {
    KW_USING(): TerminalNode | undefined;
    opt_raise_using_elem_list(): Opt_raise_using_elem_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_raise_using_elemContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    EQUAL(): TerminalNode;
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_raise_using_elem_listContext extends ParserRuleContext {
    opt_raise_using_elem(): Opt_raise_using_elemContext[];
    opt_raise_using_elem(i: number): Opt_raise_using_elemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_assertContext extends ParserRuleContext {
    KW_ASSERT(): TerminalNode;
    sql_expression(): Sql_expressionContext;
    opt_stmt_assert_message(): Opt_stmt_assert_messageContext;
    SEMI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_stmt_assert_messageContext extends ParserRuleContext {
    COMMA(): TerminalNode | undefined;
    sql_expression(): Sql_expressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Loop_bodyContext extends ParserRuleContext {
    KW_LOOP(): TerminalNode[];
    KW_LOOP(i: number): TerminalNode;
    proc_sect(): Proc_sectContext;
    KW_END(): TerminalNode;
    opt_label(): Opt_labelContext;
    SEMI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_execsqlContext extends ParserRuleContext {
    make_execsql_stmt(): Make_execsql_stmtContext;
    SEMI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_dynexecuteContext extends ParserRuleContext {
    KW_EXECUTE(): TerminalNode;
    a_expr(): A_exprContext;
    SEMI(): TerminalNode;
    opt_execute_into(): Opt_execute_intoContext | undefined;
    opt_execute_using(): Opt_execute_usingContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_execute_usingContext extends ParserRuleContext {
    KW_USING(): TerminalNode | undefined;
    opt_execute_using_list(): Opt_execute_using_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_execute_using_listContext extends ParserRuleContext {
    a_expr(): A_exprContext[];
    a_expr(i: number): A_exprContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_execute_intoContext extends ParserRuleContext {
    KW_INTO(): TerminalNode | undefined;
    into_target(): Into_targetContext | undefined;
    KW_STRICT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_openContext extends ParserRuleContext {
    KW_OPEN(): TerminalNode;
    SEMI(): TerminalNode;
    cursor_variable(): Cursor_variableContext | undefined;
    opt_scroll_option(): Opt_scroll_optionContext | undefined;
    KW_FOR(): TerminalNode | undefined;
    colid(): ColidContext | undefined;
    selectstmt(): SelectstmtContext | undefined;
    KW_EXECUTE(): TerminalNode | undefined;
    sql_expression(): Sql_expressionContext | undefined;
    opt_open_using(): Opt_open_usingContext | undefined;
    OPEN_PAREN(): TerminalNode | undefined;
    opt_open_bound_list(): Opt_open_bound_listContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_open_bound_list_itemContext extends ParserRuleContext {
    colid(): ColidContext | undefined;
    COLON_EQUALS(): TerminalNode | undefined;
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_open_bound_listContext extends ParserRuleContext {
    opt_open_bound_list_item(): Opt_open_bound_list_itemContext[];
    opt_open_bound_list_item(i: number): Opt_open_bound_list_itemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_open_usingContext extends ParserRuleContext {
    KW_USING(): TerminalNode | undefined;
    expr_list(): Expr_listContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_scroll_optionContext extends ParserRuleContext {
    opt_scroll_option_no(): Opt_scroll_option_noContext | undefined;
    KW_SCROLL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_scroll_option_noContext extends ParserRuleContext {
    KW_NO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_fetchContext extends ParserRuleContext {
    _direction: Opt_fetch_directionContext;
    KW_FETCH(): TerminalNode;
    opt_cursor_from(): Opt_cursor_fromContext;
    cursor_variable(): Cursor_variableContext;
    KW_INTO(): TerminalNode;
    into_target(): Into_targetContext;
    SEMI(): TerminalNode;
    opt_fetch_direction(): Opt_fetch_directionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Into_targetContext extends ParserRuleContext {
    expr_list(): Expr_listContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_cursor_fromContext extends ParserRuleContext {
    KW_FROM(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_fetch_directionContext extends ParserRuleContext {
    KW_NEXT(): TerminalNode | undefined;
    KW_PRIOR(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    KW_ABSOLUTE(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    KW_RELATIVE(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_FORWARD(): TerminalNode | undefined;
    KW_BACKWARD(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_moveContext extends ParserRuleContext {
    KW_MOVE(): TerminalNode;
    cursor_variable(): Cursor_variableContext;
    SEMI(): TerminalNode;
    opt_fetch_direction(): Opt_fetch_directionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class MergestmtContext extends ParserRuleContext {
    KW_MERGE(): TerminalNode;
    KW_INTO(): TerminalNode;
    table_name(): Table_nameContext;
    KW_USING(): TerminalNode;
    data_source(): Data_sourceContext;
    KW_ON(): TerminalNode;
    join_condition(): Join_conditionContext;
    with_clause(): With_clauseContext | undefined;
    KW_ONLY(): TerminalNode | undefined;
    STAR(): TerminalNode | undefined;
    colid(): ColidContext | undefined;
    merge_when_clause(): Merge_when_clauseContext[];
    merge_when_clause(i: number): Merge_when_clauseContext;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Data_sourceContext extends ParserRuleContext {
    table_name(): Table_nameContext | undefined;
    colid(): ColidContext | undefined;
    select_no_parens(): Select_no_parensContext | undefined;
    values_clause(): Values_clauseContext | undefined;
    KW_ONLY(): TerminalNode | undefined;
    STAR(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Join_conditionContext extends ParserRuleContext {
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Merge_when_clauseContext extends ParserRuleContext {
    KW_WHEN(): TerminalNode;
    KW_MATCHED(): TerminalNode;
    KW_THEN(): TerminalNode;
    merge_update(): Merge_updateContext | undefined;
    KW_DELETE(): TerminalNode | undefined;
    KW_DO(): TerminalNode | undefined;
    KW_NOTHING(): TerminalNode | undefined;
    KW_AND(): TerminalNode | undefined;
    a_expr(): A_exprContext | undefined;
    KW_NOT(): TerminalNode | undefined;
    merge_insert(): Merge_insertContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Merge_insertContext extends ParserRuleContext {
    KW_INSERT(): TerminalNode;
    default_values_or_values(): Default_values_or_valuesContext;
    OPEN_PAREN(): TerminalNode | undefined;
    columnlist(): ColumnlistContext | undefined;
    CLOSE_PAREN(): TerminalNode | undefined;
    KW_OVERRIDING(): TerminalNode | undefined;
    KW_VALUE(): TerminalNode | undefined;
    KW_SYSTEM(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Merge_updateContext extends ParserRuleContext {
    KW_UPDATE(): TerminalNode;
    KW_SET(): TerminalNode;
    column_name(): Column_nameContext[];
    column_name(i: number): Column_nameContext;
    EQUAL(): TerminalNode[];
    EQUAL(i: number): TerminalNode;
    exprofdefault(): ExprofdefaultContext[];
    exprofdefault(i: number): ExprofdefaultContext;
    OPEN_PAREN(): TerminalNode[];
    OPEN_PAREN(i: number): TerminalNode;
    columnlist(): ColumnlistContext[];
    columnlist(i: number): ColumnlistContext;
    CLOSE_PAREN(): TerminalNode[];
    CLOSE_PAREN(i: number): TerminalNode;
    exprofdefaultlist(): ExprofdefaultlistContext[];
    exprofdefaultlist(i: number): ExprofdefaultlistContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Default_values_or_valuesContext extends ParserRuleContext {
    KW_VALUES(): TerminalNode;
    exprofdefaultlist(): ExprofdefaultlistContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ExprofdefaultlistContext extends ParserRuleContext {
    OPEN_PAREN(): TerminalNode;
    exprofdefault(): ExprofdefaultContext[];
    exprofdefault(i: number): ExprofdefaultContext;
    CLOSE_PAREN(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class ExprofdefaultContext extends ParserRuleContext {
    sortby(): SortbyContext | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_closeContext extends ParserRuleContext {
    KW_CLOSE(): TerminalNode;
    cursor_variable(): Cursor_variableContext;
    SEMI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_nullContext extends ParserRuleContext {
    KW_NULL(): TerminalNode;
    SEMI(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_commitContext extends ParserRuleContext {
    KW_COMMIT(): TerminalNode;
    SEMI(): TerminalNode;
    plsql_opt_transaction_chain(): Plsql_opt_transaction_chainContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_rollbackContext extends ParserRuleContext {
    KW_ROLLBACK(): TerminalNode;
    SEMI(): TerminalNode;
    plsql_opt_transaction_chain(): Plsql_opt_transaction_chainContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Plsql_opt_transaction_chainContext extends ParserRuleContext {
    KW_AND(): TerminalNode;
    KW_CHAIN(): TerminalNode;
    KW_NO(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Stmt_setContext extends ParserRuleContext {
    KW_SET(): TerminalNode | undefined;
    any_name(): Any_nameContext | undefined;
    KW_TO(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    SEMI(): TerminalNode;
    KW_RESET(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Cursor_variableContext extends ParserRuleContext {
    colid(): ColidContext | undefined;
    PARAM(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Exception_sectContext extends ParserRuleContext {
    KW_EXCEPTION(): TerminalNode | undefined;
    proc_exceptions(): Proc_exceptionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Proc_exceptionsContext extends ParserRuleContext {
    proc_exception(): Proc_exceptionContext[];
    proc_exception(i: number): Proc_exceptionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Proc_exceptionContext extends ParserRuleContext {
    KW_WHEN(): TerminalNode;
    proc_conditions(): Proc_conditionsContext;
    KW_THEN(): TerminalNode;
    proc_sect(): Proc_sectContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Proc_conditionsContext extends ParserRuleContext {
    proc_condition(): Proc_conditionContext[];
    proc_condition(i: number): Proc_conditionContext;
    KW_OR(): TerminalNode[];
    KW_OR(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Proc_conditionContext extends ParserRuleContext {
    any_identifier(): Any_identifierContext | undefined;
    KW_SQLSTATE(): TerminalNode | undefined;
    sconst(): SconstContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_block_labelContext extends ParserRuleContext {
    label_decl(): Label_declContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_loop_labelContext extends ParserRuleContext {
    label_decl(): Label_declContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_labelContext extends ParserRuleContext {
    any_identifier(): Any_identifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_exitcondContext extends ParserRuleContext {
    KW_WHEN(): TerminalNode;
    expr_until_semi(): Expr_until_semiContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Any_identifierContext extends ParserRuleContext {
    colid(): ColidContext | undefined;
    plsql_unreserved_keyword(): Plsql_unreserved_keywordContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Plsql_unreserved_keywordContext extends ParserRuleContext {
    KW_ABSOLUTE(): TerminalNode | undefined;
    KW_ALIAS(): TerminalNode | undefined;
    KW_AND(): TerminalNode | undefined;
    KW_ARRAY(): TerminalNode | undefined;
    KW_ASSERT(): TerminalNode | undefined;
    KW_BACKWARD(): TerminalNode | undefined;
    KW_CALL(): TerminalNode | undefined;
    KW_CHAIN(): TerminalNode | undefined;
    KW_CLOSE(): TerminalNode | undefined;
    KW_COLLATE(): TerminalNode | undefined;
    KW_COLUMN(): TerminalNode | undefined;
    KW_COMMIT(): TerminalNode | undefined;
    KW_CONSTANT(): TerminalNode | undefined;
    KW_CONSTRAINT(): TerminalNode | undefined;
    KW_CONTINUE(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_CURSOR(): TerminalNode | undefined;
    KW_DEBUG(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_DIAGNOSTICS(): TerminalNode | undefined;
    KW_DO(): TerminalNode | undefined;
    KW_DUMP(): TerminalNode | undefined;
    KW_ELSIF(): TerminalNode | undefined;
    KW_ERROR(): TerminalNode | undefined;
    KW_EXCEPTION(): TerminalNode | undefined;
    KW_EXIT(): TerminalNode | undefined;
    KW_FETCH(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_FORWARD(): TerminalNode | undefined;
    KW_GET(): TerminalNode | undefined;
    KW_INFO(): TerminalNode | undefined;
    KW_INSERT(): TerminalNode | undefined;
    KW_IS(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    KW_LOG(): TerminalNode | undefined;
    KW_MOVE(): TerminalNode | undefined;
    KW_NEXT(): TerminalNode | undefined;
    KW_NO(): TerminalNode | undefined;
    KW_NOTICE(): TerminalNode | undefined;
    KW_OPEN(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_PERFORM(): TerminalNode | undefined;
    KW_PRINT_STRICT_PARAMS(): TerminalNode | undefined;
    KW_PRIOR(): TerminalNode | undefined;
    KW_QUERY(): TerminalNode | undefined;
    KW_RAISE(): TerminalNode | undefined;
    KW_RELATIVE(): TerminalNode | undefined;
    KW_RESET(): TerminalNode | undefined;
    KW_RETURN(): TerminalNode | undefined;
    KW_REVERSE(): TerminalNode | undefined;
    KW_ROLLBACK(): TerminalNode | undefined;
    KW_ROWTYPE(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    KW_SCROLL(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    KW_SLICE(): TerminalNode | undefined;
    KW_SQLSTATE(): TerminalNode | undefined;
    KW_STACKED(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    KW_TYPE(): TerminalNode | undefined;
    KW_USE_COLUMN(): TerminalNode | undefined;
    KW_USE_VARIABLE(): TerminalNode | undefined;
    KW_VARIABLE_CONFLICT(): TerminalNode | undefined;
    KW_WARNING(): TerminalNode | undefined;
    KW_OUTER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Sql_expressionContext extends ParserRuleContext {
    opt_target_list(): Opt_target_listContext | undefined;
    into_clause(): Into_clauseContext | undefined;
    from_clause(): From_clauseContext | undefined;
    where_clause(): Where_clauseContext | undefined;
    group_clause(): Group_clauseContext | undefined;
    having_clause(): Having_clauseContext | undefined;
    window_clause(): Window_clauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Expr_until_thenContext extends ParserRuleContext {
    sql_expression(): Sql_expressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Expr_until_semiContext extends ParserRuleContext {
    sql_expression(): Sql_expressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Expr_until_rightbracketContext extends ParserRuleContext {
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Expr_until_loopContext extends ParserRuleContext {
    a_expr(): A_exprContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Make_execsql_stmtContext extends ParserRuleContext {
    stmt(): StmtContext;
    opt_returning_clause_into(): Opt_returning_clause_intoContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
export declare class Opt_returning_clause_intoContext extends ParserRuleContext {
    KW_INTO(): TerminalNode;
    opt_strict(): Opt_strictContext;
    into_target(): Into_targetContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: PostgreSQLParserListener): void;
    exitRule(listener: PostgreSQLParserListener): void;
    accept<Result>(visitor: PostgreSQLParserVisitor<Result>): Result;
}
