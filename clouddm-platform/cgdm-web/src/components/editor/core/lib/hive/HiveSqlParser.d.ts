import { ATN } from "antlr4ts/atn/ATN";
import { FailedPredicateException } from "antlr4ts/FailedPredicateException";
import { Parser } from "antlr4ts/Parser";
import { ParserRuleContext } from "antlr4ts/ParserRuleContext";
import { TerminalNode } from "antlr4ts/tree/TerminalNode";
import { Token } from "antlr4ts/Token";
import { TokenStream } from "antlr4ts/TokenStream";
import { Vocabulary } from "antlr4ts/Vocabulary";
import { HiveSqlParserListener } from "./HiveSqlParserListener";
import { HiveSqlParserVisitor } from "./HiveSqlParserVisitor";
export declare class HiveSqlParser extends Parser {
    static readonly KW_ABORT = 1;
    static readonly KW_ACTIVATE = 2;
    static readonly KW_ACTIVE = 3;
    static readonly KW_ADD = 4;
    static readonly KW_ADMIN = 5;
    static readonly KW_AFTER = 6;
    static readonly KW_ALL = 7;
    static readonly KW_ALLOC_FRACTION = 8;
    static readonly KW_ALTER = 9;
    static readonly KW_ANALYZE = 10;
    static readonly KW_AND = 11;
    static readonly KW_ANTI = 12;
    static readonly KW_ANY = 13;
    static readonly KW_APPLICATION = 14;
    static readonly KW_ARCHIVE = 15;
    static readonly KW_ARRAY = 16;
    static readonly KW_AS = 17;
    static readonly KW_ASC = 18;
    static readonly KW_AST = 19;
    static readonly KW_AT = 20;
    static readonly KW_AUTHORIZATION = 21;
    static readonly KW_AUTOCOMMIT = 22;
    static readonly KW_BATCH = 23;
    static readonly KW_BEFORE = 24;
    static readonly KW_BETWEEN = 25;
    static readonly KW_BIGINT = 26;
    static readonly KW_BINARY = 27;
    static readonly KW_BOOLEAN = 28;
    static readonly KW_BOTH = 29;
    static readonly KW_BUCKET = 30;
    static readonly KW_BUCKETS = 31;
    static readonly KW_BY = 32;
    static readonly KW_CACHE = 33;
    static readonly KW_CASCADE = 34;
    static readonly KW_CASE = 35;
    static readonly KW_CAST = 36;
    static readonly KW_CBO = 37;
    static readonly KW_CHANGE = 38;
    static readonly KW_CHAR = 39;
    static readonly KW_CHECK = 40;
    static readonly KW_CLUSTER = 41;
    static readonly KW_CLUSTERED = 42;
    static readonly KW_CLUSTERSTATUS = 43;
    static readonly KW_COLLECTION = 44;
    static readonly KW_COLUMN = 45;
    static readonly KW_COLUMNS = 46;
    static readonly KW_COMMENT = 47;
    static readonly KW_COMMIT = 48;
    static readonly KW_COMPACT = 49;
    static readonly KW_COMPACTIONS = 50;
    static readonly KW_COMPACT_ID = 51;
    static readonly KW_COMPUTE = 52;
    static readonly KW_CONCATENATE = 53;
    static readonly KW_CONF = 54;
    static readonly KW_CONSTRAINT = 55;
    static readonly KW_CONTINUE = 56;
    static readonly KW_COST = 57;
    static readonly KW_CREATE = 58;
    static readonly KW_CRON = 59;
    static readonly KW_CROSS = 60;
    static readonly KW_CUBE = 61;
    static readonly KW_CURRENT = 62;
    static readonly KW_CURRENT_DATE = 63;
    static readonly KW_CURRENT_TIMESTAMP = 64;
    static readonly KW_CURSOR = 65;
    static readonly KW_DATA = 66;
    static readonly KW_DATABASE = 67;
    static readonly KW_DATABASES = 68;
    static readonly KW_DATACONNECTOR = 69;
    static readonly KW_DATACONNECTORS = 70;
    static readonly KW_DATE = 71;
    static readonly KW_DATETIME = 72;
    static readonly KW_DAY = 73;
    static readonly KW_DAYS = 74;
    static readonly KW_DAYOFWEEK = 75;
    static readonly KW_DBPROPERTIES = 76;
    static readonly KW_DCPROPERTIES = 77;
    static readonly KW_DDL = 78;
    static readonly KW_DEBUG = 79;
    static readonly KW_DEC = 80;
    static readonly KW_DECIMAL = 81;
    static readonly KW_NUMERIC = 82;
    static readonly KW_DEFAULT = 83;
    static readonly KW_DEFERRED = 84;
    static readonly KW_DEFINED = 85;
    static readonly KW_DELETE = 86;
    static readonly KW_DELIMITED = 87;
    static readonly KW_DEPENDENCY = 88;
    static readonly KW_DESC = 89;
    static readonly KW_DESCRIBE = 90;
    static readonly KW_DETAIL = 91;
    static readonly KW_DIRECTORIES = 92;
    static readonly KW_DIRECTORY = 93;
    static readonly KW_DISABLE = 94;
    static readonly KW_DISABLED = 95;
    static readonly KW_DISTINCT = 96;
    static readonly KW_DISTRIBUTE = 97;
    static readonly KW_DISTRIBUTED = 98;
    static readonly KW_DO = 99;
    static readonly KW_DOUBLE = 100;
    static readonly KW_DROP = 101;
    static readonly KW_RECOVER = 102;
    static readonly KW_DUMP = 103;
    static readonly KW_ELEM_TYPE = 104;
    static readonly KW_ELSE = 105;
    static readonly KW_ENABLE = 106;
    static readonly KW_ENABLED = 107;
    static readonly KW_END = 108;
    static readonly KW_ENFORCED = 109;
    static readonly KW_ESCAPED = 110;
    static readonly KW_EVERY = 111;
    static readonly KW_EXCEPT = 112;
    static readonly KW_EXCHANGE = 113;
    static readonly KW_EXCLUSIVE = 114;
    static readonly KW_EXECUTE = 115;
    static readonly KW_EXECUTED = 116;
    static readonly KW_EXISTS = 117;
    static readonly KW_EXPIRE_SNAPSHOTS = 118;
    static readonly KW_EXPLAIN = 119;
    static readonly KW_EXPORT = 120;
    static readonly KW_EXPRESSION = 121;
    static readonly KW_EXTENDED = 122;
    static readonly KW_EXTERNAL = 123;
    static readonly KW_EXTRACT = 124;
    static readonly KW_FALSE = 125;
    static readonly KW_FETCH = 126;
    static readonly KW_FIELDS = 127;
    static readonly KW_FILE = 128;
    static readonly KW_FILEFORMAT = 129;
    static readonly KW_FIRST = 130;
    static readonly KW_FLOAT = 131;
    static readonly KW_FLOOR = 132;
    static readonly KW_FOLLOWING = 133;
    static readonly KW_FOR = 134;
    static readonly KW_FORCE = 135;
    static readonly KW_FOREIGN = 136;
    static readonly KW_FORMAT = 137;
    static readonly KW_FORMATTED = 138;
    static readonly KW_FROM = 139;
    static readonly KW_FULL = 140;
    static readonly KW_FUNCTION = 141;
    static readonly KW_FUNCTIONS = 142;
    static readonly KW_GRANT = 143;
    static readonly KW_GROUP = 144;
    static readonly KW_GROUPING = 145;
    static readonly KW_HAVING = 146;
    static readonly KW_HOLD_DDLTIME = 147;
    static readonly KW_HOUR = 148;
    static readonly KW_HOURS = 149;
    static readonly KW_IDXPROPERTIES = 150;
    static readonly KW_IF = 151;
    static readonly KW_IGNORE = 152;
    static readonly KW_IMPORT = 153;
    static readonly KW_IN = 154;
    static readonly KW_INDEX = 155;
    static readonly KW_INDEXES = 156;
    static readonly KW_INNER = 157;
    static readonly KW_INPATH = 158;
    static readonly KW_INPUTDRIVER = 159;
    static readonly KW_INPUTFORMAT = 160;
    static readonly KW_INSERT = 161;
    static readonly KW_INT = 162;
    static readonly KW_INTEGER = 163;
    static readonly KW_INTERSECT = 164;
    static readonly KW_INTERVAL = 165;
    static readonly KW_INTO = 166;
    static readonly KW_IS = 167;
    static readonly KW_ISOLATION = 168;
    static readonly KW_ITEMS = 169;
    static readonly KW_JAR = 170;
    static readonly KW_JOIN = 171;
    static readonly KW_JOINCOST = 172;
    static readonly KW_KEY = 173;
    static readonly KW_KEYS = 174;
    static readonly KW_KEY_TYPE = 175;
    static readonly KW_KILL = 176;
    static readonly KW_LAST = 177;
    static readonly KW_LATERAL = 178;
    static readonly KW_LEADING = 179;
    static readonly KW_LEFT = 180;
    static readonly KW_LESS = 181;
    static readonly KW_LEVEL = 182;
    static readonly KW_LIKE = 183;
    static readonly KW_LIMIT = 184;
    static readonly KW_LINES = 185;
    static readonly KW_LOAD = 186;
    static readonly KW_LOCAL = 187;
    static readonly KW_LOCATION = 188;
    static readonly KW_LOCK = 189;
    static readonly KW_LOCKS = 190;
    static readonly KW_LOGICAL = 191;
    static readonly KW_LONG = 192;
    static readonly KW_MACRO = 193;
    static readonly KW_MANAGED = 194;
    static readonly KW_MANAGEDLOCATION = 195;
    static readonly KW_MANAGEMENT = 196;
    static readonly KW_MAP = 197;
    static readonly KW_MAPJOIN = 198;
    static readonly KW_MAPPING = 199;
    static readonly KW_MATCHED = 200;
    static readonly KW_MATERIALIZED = 201;
    static readonly KW_MERGE = 202;
    static readonly KW_METADATA = 203;
    static readonly KW_MINUS = 204;
    static readonly KW_MINUTE = 205;
    static readonly KW_MINUTES = 206;
    static readonly KW_MONTH = 207;
    static readonly KW_MONTHS = 208;
    static readonly KW_MORE = 209;
    static readonly KW_MOVE = 210;
    static readonly KW_MSCK = 211;
    static readonly KW_NONE = 212;
    static readonly KW_NORELY = 213;
    static readonly KW_NOSCAN = 214;
    static readonly KW_NOT = 215;
    static readonly KW_NOVALIDATE = 216;
    static readonly KW_NO_DROP = 217;
    static readonly KW_NULL = 218;
    static readonly KW_NULLS = 219;
    static readonly KW_OF = 220;
    static readonly KW_OFFLINE = 221;
    static readonly KW_OFFSET = 222;
    static readonly KW_ON = 223;
    static readonly KW_ONLY = 224;
    static readonly KW_OPERATOR = 225;
    static readonly KW_OPTION = 226;
    static readonly KW_OR = 227;
    static readonly KW_ORDER = 228;
    static readonly KW_OUT = 229;
    static readonly KW_OUTER = 230;
    static readonly KW_OUTPUTDRIVER = 231;
    static readonly KW_OUTPUTFORMAT = 232;
    static readonly KW_OVER = 233;
    static readonly KW_OVERWRITE = 234;
    static readonly KW_OWNER = 235;
    static readonly KW_PARTITION = 236;
    static readonly KW_PARTITIONED = 237;
    static readonly KW_PARTITIONS = 238;
    static readonly KW_PATH = 239;
    static readonly KW_PERCENT = 240;
    static readonly KW_PKFK_JOIN = 241;
    static readonly KW_PLAN = 242;
    static readonly KW_PLANS = 243;
    static readonly KW_PLUS = 244;
    static readonly KW_POOL = 245;
    static readonly KW_PRECEDING = 246;
    static readonly KW_PRECISION = 247;
    static readonly KW_PREPARE = 248;
    static readonly KW_PRESERVE = 249;
    static readonly KW_PRIMARY = 250;
    static readonly KW_PRINCIPALS = 251;
    static readonly KW_PROCEDURE = 252;
    static readonly KW_PROTECTION = 253;
    static readonly KW_PURGE = 254;
    static readonly KW_QUALIFY = 255;
    static readonly KW_QUARTER = 256;
    static readonly KW_QUERY = 257;
    static readonly KW_QUERY_PARALLELISM = 258;
    static readonly KW_RANGE = 259;
    static readonly KW_READ = 260;
    static readonly KW_READONLY = 261;
    static readonly KW_READS = 262;
    static readonly KW_REAL = 263;
    static readonly KW_REBUILD = 264;
    static readonly KW_RECORDREADER = 265;
    static readonly KW_RECORDWRITER = 266;
    static readonly KW_REDUCE = 267;
    static readonly KW_REFERENCES = 268;
    static readonly KW_REGEXP = 269;
    static readonly KW_RELOAD = 270;
    static readonly KW_RELY = 271;
    static readonly KW_REMOTE = 272;
    static readonly KW_RENAME = 273;
    static readonly KW_REOPTIMIZATION = 274;
    static readonly KW_REPAIR = 275;
    static readonly KW_REPL = 276;
    static readonly KW_REPLACE = 277;
    static readonly KW_REPLICATION = 278;
    static readonly KW_RESOURCE = 279;
    static readonly KW_RESPECT = 280;
    static readonly KW_RESTRICT = 281;
    static readonly KW_REVOKE = 282;
    static readonly KW_REWRITE = 283;
    static readonly KW_RIGHT = 284;
    static readonly KW_RLIKE = 285;
    static readonly KW_ROLE = 286;
    static readonly KW_ROLES = 287;
    static readonly KW_ROLLBACK = 288;
    static readonly KW_ROLLUP = 289;
    static readonly KW_ROW = 290;
    static readonly KW_ROWS = 291;
    static readonly KW_SCHEDULED = 292;
    static readonly KW_SCHEDULING_POLICY = 293;
    static readonly KW_SCHEMA = 294;
    static readonly KW_SCHEMAS = 295;
    static readonly KW_SECOND = 296;
    static readonly KW_SECONDS = 297;
    static readonly KW_SELECT = 298;
    static readonly KW_SEMI = 299;
    static readonly KW_SERDE = 300;
    static readonly KW_SERDEPROPERTIES = 301;
    static readonly KW_SERVER = 302;
    static readonly KW_SET = 303;
    static readonly KW_SETS = 304;
    static readonly KW_SET_CURRENT_SNAPSHOT = 305;
    static readonly KW_SHARED = 306;
    static readonly KW_SHOW = 307;
    static readonly KW_SHOW_DATABASE = 308;
    static readonly KW_SKEWED = 309;
    static readonly KW_SMALLINT = 310;
    static readonly KW_SNAPSHOT = 311;
    static readonly KW_SOME = 312;
    static readonly KW_SORT = 313;
    static readonly KW_SORTED = 314;
    static readonly KW_SPEC = 315;
    static readonly KW_SSL = 316;
    static readonly KW_START = 317;
    static readonly KW_STATISTICS = 318;
    static readonly KW_STATUS = 319;
    static readonly KW_STORED = 320;
    static readonly KW_STREAMTABLE = 321;
    static readonly KW_STRING = 322;
    static readonly KW_STRUCT = 323;
    static readonly KW_SUMMARY = 324;
    static readonly KW_SYNC = 325;
    static readonly KW_SYSTEM_TIME = 326;
    static readonly KW_SYSTEM_VERSION = 327;
    static readonly KW_TABLE = 328;
    static readonly KW_TABLES = 329;
    static readonly KW_TABLESAMPLE = 330;
    static readonly KW_TBLPROPERTIES = 331;
    static readonly KW_TEMPORARY = 332;
    static readonly KW_TERMINATED = 333;
    static readonly KW_THEN = 334;
    static readonly KW_TIME = 335;
    static readonly KW_TIMESTAMP = 336;
    static readonly KW_TIMESTAMPLOCALTZ = 337;
    static readonly KW_TIMESTAMPTZ = 338;
    static readonly KW_TINYINT = 339;
    static readonly KW_TO = 340;
    static readonly KW_TOUCH = 341;
    static readonly KW_TRAILING = 342;
    static readonly KW_TRANSACTION = 343;
    static readonly KW_TRANSACTIONAL = 344;
    static readonly KW_TRANSACTIONS = 345;
    static readonly KW_TRANSFORM = 346;
    static readonly KW_TRIGGER = 347;
    static readonly KW_TRIM = 348;
    static readonly KW_TRUE = 349;
    static readonly KW_TRUNCATE = 350;
    static readonly KW_TYPE = 351;
    static readonly KW_UNARCHIVE = 352;
    static readonly KW_UNBOUNDED = 353;
    static readonly KW_UNDO = 354;
    static readonly KW_UNION = 355;
    static readonly KW_UNIONTYPE = 356;
    static readonly KW_UNIQUE = 357;
    static readonly KW_UNIQUEJOIN = 358;
    static readonly KW_UNKNOWN = 359;
    static readonly KW_UNLOCK = 360;
    static readonly KW_UNMANAGED = 361;
    static readonly KW_UNSET = 362;
    static readonly KW_UNSIGNED = 363;
    static readonly KW_UPDATE = 364;
    static readonly KW_URI = 365;
    static readonly KW_URL = 366;
    static readonly KW_USE = 367;
    static readonly KW_USER = 368;
    static readonly KW_USING = 369;
    static readonly KW_UTC = 370;
    static readonly KW_UTCTIMESTAMP = 371;
    static readonly KW_VALIDATE = 372;
    static readonly KW_VALUES = 373;
    static readonly KW_VALUE_TYPE = 374;
    static readonly KW_VARCHAR = 375;
    static readonly KW_VECTORIZATION = 376;
    static readonly KW_VIEW = 377;
    static readonly KW_VIEWS = 378;
    static readonly KW_WAIT = 379;
    static readonly KW_WEEK = 380;
    static readonly KW_WEEKS = 381;
    static readonly KW_WHEN = 382;
    static readonly KW_WHERE = 383;
    static readonly KW_WHILE = 384;
    static readonly KW_WINDOW = 385;
    static readonly KW_WITH = 386;
    static readonly KW_WITHIN = 387;
    static readonly KW_WORK = 388;
    static readonly KW_WORKLOAD = 389;
    static readonly KW_WRITE = 390;
    static readonly KW_YEAR = 391;
    static readonly KW_YEARS = 392;
    static readonly KW_ZONE = 393;
    static readonly DOT = 394;
    static readonly COLON = 395;
    static readonly COMMA = 396;
    static readonly SEMICOLON = 397;
    static readonly LPAREN = 398;
    static readonly RPAREN = 399;
    static readonly LSQUARE = 400;
    static readonly RSQUARE = 401;
    static readonly LCURLY = 402;
    static readonly RCURLY = 403;
    static readonly EQUAL = 404;
    static readonly EQUAL_NS = 405;
    static readonly NOTEQUAL = 406;
    static readonly LESSTHANOREQUALTO = 407;
    static readonly LESSTHAN = 408;
    static readonly GREATERTHANOREQUALTO = 409;
    static readonly GREATERTHAN = 410;
    static readonly DIVIDE = 411;
    static readonly PLUS = 412;
    static readonly MINUS = 413;
    static readonly STAR = 414;
    static readonly MOD = 415;
    static readonly DIV = 416;
    static readonly BITWISENOT = 417;
    static readonly AMPERSAND = 418;
    static readonly TILDE = 419;
    static readonly BITWISEOR = 420;
    static readonly CONCATENATE = 421;
    static readonly BITWISEXOR = 422;
    static readonly QUESTION = 423;
    static readonly DOLLAR = 424;
    static readonly StringLiteral = 425;
    static readonly CharSetLiteral = 426;
    static readonly IntegralLiteral = 427;
    static readonly NumberLiteral = 428;
    static readonly ByteLengthLiteral = 429;
    static readonly Number = 430;
    static readonly Identifier = 431;
    static readonly CharSetName = 432;
    static readonly WHITE_SPACE = 433;
    static readonly LINE_COMMENT = 434;
    static readonly QUERY_HINT = 435;
    static readonly SHOW_HINT = 436;
    static readonly HIDDEN_HINT = 437;
    static readonly RULE_program = 0;
    static readonly RULE_statement = 1;
    static readonly RULE_explainStatement = 2;
    static readonly RULE_explainOption = 3;
    static readonly RULE_vectorizationOnly = 4;
    static readonly RULE_vectorizatonDetail = 5;
    static readonly RULE_execStatement = 6;
    static readonly RULE_loadStatement = 7;
    static readonly RULE_dropPartitionsIgnoreClause = 8;
    static readonly RULE_replicationClause = 9;
    static readonly RULE_exportStatement = 10;
    static readonly RULE_importStatement = 11;
    static readonly RULE_replDumpStatement = 12;
    static readonly RULE_replDbPolicy = 13;
    static readonly RULE_replLoadStatement = 14;
    static readonly RULE_replConfigs = 15;
    static readonly RULE_replConfigsList = 16;
    static readonly RULE_replTableLevelPolicy = 17;
    static readonly RULE_replStatusStatement = 18;
    static readonly RULE_ddlStatement = 19;
    static readonly RULE_ifExists = 20;
    static readonly RULE_restrictOrCascade = 21;
    static readonly RULE_ifNotExists = 22;
    static readonly RULE_force = 23;
    static readonly RULE_rewriteEnabled = 24;
    static readonly RULE_rewriteDisabled = 25;
    static readonly RULE_storedAsDirs = 26;
    static readonly RULE_orReplace = 27;
    static readonly RULE_createDatabaseStatement = 28;
    static readonly RULE_dbLocation = 29;
    static readonly RULE_dbManagedLocation = 30;
    static readonly RULE_dbProperties = 31;
    static readonly RULE_dbPropertiesList = 32;
    static readonly RULE_dbConnectorName = 33;
    static readonly RULE_switchDatabaseStatement = 34;
    static readonly RULE_dropDatabaseStatement = 35;
    static readonly RULE_databaseComment = 36;
    static readonly RULE_truncateTableStatement = 37;
    static readonly RULE_dropTableStatement = 38;
    static readonly RULE_inputFileFormat = 39;
    static readonly RULE_tabTypeExpr = 40;
    static readonly RULE_partTypeExpr = 41;
    static readonly RULE_tabPartColTypeExpr = 42;
    static readonly RULE_descStatement = 43;
    static readonly RULE_analyzeStatement = 44;
    static readonly RULE_from_in = 45;
    static readonly RULE_db_schema = 46;
    static readonly RULE_showStatement = 47;
    static readonly RULE_showTablesFilterExpr = 48;
    static readonly RULE_lockStatement = 49;
    static readonly RULE_lockDatabase = 50;
    static readonly RULE_lockMode = 51;
    static readonly RULE_unlockStatement = 52;
    static readonly RULE_unlockDatabase = 53;
    static readonly RULE_createRoleStatement = 54;
    static readonly RULE_dropRoleStatement = 55;
    static readonly RULE_grantPrivileges = 56;
    static readonly RULE_revokePrivileges = 57;
    static readonly RULE_grantRole = 58;
    static readonly RULE_revokeRole = 59;
    static readonly RULE_showRoleGrants = 60;
    static readonly RULE_showRoles = 61;
    static readonly RULE_showCurrentRole = 62;
    static readonly RULE_setRole = 63;
    static readonly RULE_showGrants = 64;
    static readonly RULE_showRolePrincipals = 65;
    static readonly RULE_privilegeIncludeColObject = 66;
    static readonly RULE_privilegeObject = 67;
    static readonly RULE_privObject = 68;
    static readonly RULE_privObjectCols = 69;
    static readonly RULE_privilegeList = 70;
    static readonly RULE_privlegeDef = 71;
    static readonly RULE_privilegeType = 72;
    static readonly RULE_principalSpecification = 73;
    static readonly RULE_principalName = 74;
    static readonly RULE_principalAlterName = 75;
    static readonly RULE_withGrantOption = 76;
    static readonly RULE_grantOptionFor = 77;
    static readonly RULE_adminOptionFor = 78;
    static readonly RULE_withAdminOption = 79;
    static readonly RULE_metastoreCheck = 80;
    static readonly RULE_resourceList = 81;
    static readonly RULE_resource = 82;
    static readonly RULE_resourceType = 83;
    static readonly RULE_createFunctionStatement = 84;
    static readonly RULE_dropFunctionStatement = 85;
    static readonly RULE_reloadFunctionsStatement = 86;
    static readonly RULE_createMacroStatement = 87;
    static readonly RULE_dropMacroStatement = 88;
    static readonly RULE_createIndexStatement = 89;
    static readonly RULE_dropIndexStatement = 90;
    static readonly RULE_createViewStatement = 91;
    static readonly RULE_viewPartition = 92;
    static readonly RULE_viewOrganization = 93;
    static readonly RULE_viewClusterSpec = 94;
    static readonly RULE_viewComplexSpec = 95;
    static readonly RULE_viewDistSpec = 96;
    static readonly RULE_viewSortSpec = 97;
    static readonly RULE_dropViewStatement = 98;
    static readonly RULE_createMaterializedViewStatement = 99;
    static readonly RULE_dropMaterializedViewStatement = 100;
    static readonly RULE_createScheduledQueryStatement = 101;
    static readonly RULE_dropScheduledQueryStatement = 102;
    static readonly RULE_alterScheduledQueryStatement = 103;
    static readonly RULE_alterScheduledQueryChange = 104;
    static readonly RULE_scheduleSpec = 105;
    static readonly RULE_executedAsSpec = 106;
    static readonly RULE_definedAsSpec = 107;
    static readonly RULE_showStmtIdentifier = 108;
    static readonly RULE_tableComment = 109;
    static readonly RULE_createTablePartitionSpec = 110;
    static readonly RULE_createTablePartitionColumnTypeSpec = 111;
    static readonly RULE_createTablePartitionColumnSpec = 112;
    static readonly RULE_partitionTransformSpec = 113;
    static readonly RULE_columnNameTransformConstraint = 114;
    static readonly RULE_partitionTransformType = 115;
    static readonly RULE_tableBuckets = 116;
    static readonly RULE_tableImplBuckets = 117;
    static readonly RULE_tableSkewed = 118;
    static readonly RULE_rowFormat = 119;
    static readonly RULE_recordReader = 120;
    static readonly RULE_recordWriter = 121;
    static readonly RULE_rowFormatSerde = 122;
    static readonly RULE_rowFormatDelimited = 123;
    static readonly RULE_tableRowFormat = 124;
    static readonly RULE_tablePropertiesPrefixed = 125;
    static readonly RULE_tableProperties = 126;
    static readonly RULE_tablePropertiesList = 127;
    static readonly RULE_keyValueProperty = 128;
    static readonly RULE_keyProperty = 129;
    static readonly RULE_tableRowFormatFieldIdentifier = 130;
    static readonly RULE_tableRowFormatCollItemsIdentifier = 131;
    static readonly RULE_tableRowFormatMapKeysIdentifier = 132;
    static readonly RULE_tableRowFormatLinesIdentifier = 133;
    static readonly RULE_tableRowNullFormat = 134;
    static readonly RULE_tableFileFormat = 135;
    static readonly RULE_tableLocation = 136;
    static readonly RULE_columnNameTypeList = 137;
    static readonly RULE_columnNameTypeOrConstraintList = 138;
    static readonly RULE_columnNameColonTypeList = 139;
    static readonly RULE_columnNameList = 140;
    static readonly RULE_columnName = 141;
    static readonly RULE_columnNameCreate = 142;
    static readonly RULE_extColumnName = 143;
    static readonly RULE_columnNameOrderList = 144;
    static readonly RULE_columnParenthesesList = 145;
    static readonly RULE_enableValidateSpecification = 146;
    static readonly RULE_enableSpecification = 147;
    static readonly RULE_validateSpecification = 148;
    static readonly RULE_enforcedSpecification = 149;
    static readonly RULE_relySpecification = 150;
    static readonly RULE_createConstraint = 151;
    static readonly RULE_alterConstraintWithName = 152;
    static readonly RULE_tableLevelConstraint = 153;
    static readonly RULE_pkUkConstraint = 154;
    static readonly RULE_checkConstraint = 155;
    static readonly RULE_createForeignKey = 156;
    static readonly RULE_alterForeignKeyWithName = 157;
    static readonly RULE_skewedValueElement = 158;
    static readonly RULE_skewedColumnValuePairList = 159;
    static readonly RULE_skewedColumnValuePair = 160;
    static readonly RULE_skewedColumnValues = 161;
    static readonly RULE_skewedColumnValue = 162;
    static readonly RULE_skewedValueLocationElement = 163;
    static readonly RULE_orderSpecification = 164;
    static readonly RULE_nullOrdering = 165;
    static readonly RULE_columnNameOrder = 166;
    static readonly RULE_columnNameCommentList = 167;
    static readonly RULE_columnNameComment = 168;
    static readonly RULE_orderSpecificationRewrite = 169;
    static readonly RULE_columnRefOrder = 170;
    static readonly RULE_columnNameType = 171;
    static readonly RULE_columnNameTypeOrConstraint = 172;
    static readonly RULE_tableConstraint = 173;
    static readonly RULE_columnNameTypeConstraint = 174;
    static readonly RULE_columnConstraint = 175;
    static readonly RULE_foreignKeyConstraint = 176;
    static readonly RULE_colConstraint = 177;
    static readonly RULE_alterColumnConstraint = 178;
    static readonly RULE_alterForeignKeyConstraint = 179;
    static readonly RULE_alterColConstraint = 180;
    static readonly RULE_columnConstraintType = 181;
    static readonly RULE_defaultVal = 182;
    static readonly RULE_tableConstraintType = 183;
    static readonly RULE_constraintOptsCreate = 184;
    static readonly RULE_constraintOptsAlter = 185;
    static readonly RULE_columnNameColonType = 186;
    static readonly RULE_colType = 187;
    static readonly RULE_colTypeList = 188;
    static readonly RULE_type = 189;
    static readonly RULE_primitiveType = 190;
    static readonly RULE_listType = 191;
    static readonly RULE_structType = 192;
    static readonly RULE_mapType = 193;
    static readonly RULE_unionType = 194;
    static readonly RULE_setOperator = 195;
    static readonly RULE_queryStatementExpression = 196;
    static readonly RULE_queryStatementExpressionBody = 197;
    static readonly RULE_withClause = 198;
    static readonly RULE_cteStatement = 199;
    static readonly RULE_fromStatement = 200;
    static readonly RULE_singleFromStatement = 201;
    static readonly RULE_regularBody = 202;
    static readonly RULE_atomSelectStatement = 203;
    static readonly RULE_selectStatement = 204;
    static readonly RULE_setOpSelectStatement = 205;
    static readonly RULE_selectStatementWithCTE = 206;
    static readonly RULE_body = 207;
    static readonly RULE_insertClause = 208;
    static readonly RULE_destination = 209;
    static readonly RULE_limitClause = 210;
    static readonly RULE_deleteStatement = 211;
    static readonly RULE_columnAssignmentClause = 212;
    static readonly RULE_precedencePlusExpressionOrDefault = 213;
    static readonly RULE_setColumnsClause = 214;
    static readonly RULE_updateStatement = 215;
    static readonly RULE_sqlTransactionStatement = 216;
    static readonly RULE_startTransactionStatement = 217;
    static readonly RULE_transactionMode = 218;
    static readonly RULE_transactionAccessMode = 219;
    static readonly RULE_isolationLevel = 220;
    static readonly RULE_levelOfIsolation = 221;
    static readonly RULE_commitStatement = 222;
    static readonly RULE_rollbackStatement = 223;
    static readonly RULE_setAutoCommitStatement = 224;
    static readonly RULE_abortTransactionStatement = 225;
    static readonly RULE_abortCompactionStatement = 226;
    static readonly RULE_mergeStatement = 227;
    static readonly RULE_whenClauses = 228;
    static readonly RULE_whenNotMatchedClause = 229;
    static readonly RULE_whenMatchedAndClause = 230;
    static readonly RULE_whenMatchedThenClause = 231;
    static readonly RULE_updateOrDelete = 232;
    static readonly RULE_killQueryStatement = 233;
    static readonly RULE_compactionId = 234;
    static readonly RULE_compactionPool = 235;
    static readonly RULE_compactionType = 236;
    static readonly RULE_compactionStatus = 237;
    static readonly RULE_alterStatement = 238;
    static readonly RULE_alterTableStatementSuffix = 239;
    static readonly RULE_alterTblPartitionStatementSuffix = 240;
    static readonly RULE_alterStatementPartitionKeyType = 241;
    static readonly RULE_alterViewStatementSuffix = 242;
    static readonly RULE_alterMaterializedViewStatementSuffix = 243;
    static readonly RULE_alterMaterializedViewSuffixRewrite = 244;
    static readonly RULE_alterMaterializedViewSuffixRebuild = 245;
    static readonly RULE_alterDatabaseStatementSuffix = 246;
    static readonly RULE_alterDatabaseSuffixProperties = 247;
    static readonly RULE_alterDatabaseSuffixSetOwner = 248;
    static readonly RULE_alterDatabaseSuffixSetLocation = 249;
    static readonly RULE_alterDatabaseSuffixSetManagedLocation = 250;
    static readonly RULE_alterStatementSuffixRename = 251;
    static readonly RULE_alterStatementSuffixAddCol = 252;
    static readonly RULE_alterStatementSuffixAddConstraint = 253;
    static readonly RULE_alterStatementSuffixUpdateColumns = 254;
    static readonly RULE_alterStatementSuffixProtections = 255;
    static readonly RULE_alterStatementSuffixDropConstraint = 256;
    static readonly RULE_alterStatementSuffixRenameCol = 257;
    static readonly RULE_alterStatementSuffixUpdateStatsCol = 258;
    static readonly RULE_alterStatementSuffixUpdateStats = 259;
    static readonly RULE_alterStatementChangeColPosition = 260;
    static readonly RULE_alterStatementSuffixAddPartitions = 261;
    static readonly RULE_alterStatementSuffixAddPartitionsElement = 262;
    static readonly RULE_alterStatementSuffixTouch = 263;
    static readonly RULE_alterStatementSuffixArchive = 264;
    static readonly RULE_alterStatementSuffixUnArchive = 265;
    static readonly RULE_partitionLocation = 266;
    static readonly RULE_alterStatementSuffixRecoverPartitions = 267;
    static readonly RULE_alterStatementSuffixDropPartitions = 268;
    static readonly RULE_alterStatementSuffixProperties = 269;
    static readonly RULE_alterViewSuffixProperties = 270;
    static readonly RULE_alterStatementSuffixSerdeProperties = 271;
    static readonly RULE_tablePartitionPrefix = 272;
    static readonly RULE_alterStatementSuffixFileFormat = 273;
    static readonly RULE_alterStatementSuffixClusterbySortby = 274;
    static readonly RULE_alterTblPartitionStatementSuffixSkewedLocation = 275;
    static readonly RULE_skewedLocations = 276;
    static readonly RULE_skewedLocationsList = 277;
    static readonly RULE_skewedLocationMap = 278;
    static readonly RULE_alterStatementSuffixLocation = 279;
    static readonly RULE_alterStatementSuffixSkewedby = 280;
    static readonly RULE_alterStatementSuffixExchangePartition = 281;
    static readonly RULE_alterStatementSuffixRenamePart = 282;
    static readonly RULE_alterStatementSuffixStatsPart = 283;
    static readonly RULE_alterStatementSuffixMergeFiles = 284;
    static readonly RULE_alterStatementSuffixBucketNum = 285;
    static readonly RULE_blocking = 286;
    static readonly RULE_compactPool = 287;
    static readonly RULE_alterStatementSuffixCompact = 288;
    static readonly RULE_alterStatementSuffixSetOwner = 289;
    static readonly RULE_alterStatementSuffixSetPartSpec = 290;
    static readonly RULE_alterStatementSuffixExecute = 291;
    static readonly RULE_alterIndexStatementSuffix = 292;
    static readonly RULE_fileFormat = 293;
    static readonly RULE_alterDataConnectorStatementSuffix = 294;
    static readonly RULE_alterDataConnectorSuffixProperties = 295;
    static readonly RULE_alterDataConnectorSuffixSetOwner = 296;
    static readonly RULE_alterDataConnectorSuffixSetUrl = 297;
    static readonly RULE_likeTableOrFile = 298;
    static readonly RULE_createTableStatement = 299;
    static readonly RULE_createDataConnectorStatement = 300;
    static readonly RULE_dataConnectorComment = 301;
    static readonly RULE_dataConnectorUrl = 302;
    static readonly RULE_dataConnectorType = 303;
    static readonly RULE_dcProperties = 304;
    static readonly RULE_dropDataConnectorStatement = 305;
    static readonly RULE_tableAllColumns = 306;
    static readonly RULE_defaultValue = 307;
    static readonly RULE_expressionList = 308;
    static readonly RULE_aliasList = 309;
    static readonly RULE_fromClause = 310;
    static readonly RULE_fromSource = 311;
    static readonly RULE_atomjoinSource = 312;
    static readonly RULE_joinSource = 313;
    static readonly RULE_joinSourcePart = 314;
    static readonly RULE_uniqueJoinSource = 315;
    static readonly RULE_uniqueJoinExpr = 316;
    static readonly RULE_uniqueJoinToken = 317;
    static readonly RULE_joinToken = 318;
    static readonly RULE_lateralView = 319;
    static readonly RULE_tableAlias = 320;
    static readonly RULE_tableBucketSample = 321;
    static readonly RULE_splitSample = 322;
    static readonly RULE_tableSample = 323;
    static readonly RULE_tableSource = 324;
    static readonly RULE_asOfClause = 325;
    static readonly RULE_uniqueJoinTableSource = 326;
    static readonly RULE_dbSchemaName = 327;
    static readonly RULE_dbSchemaNameCreate = 328;
    static readonly RULE_tableOrView = 329;
    static readonly RULE_tableName = 330;
    static readonly RULE_tableNameCreate = 331;
    static readonly RULE_viewName = 332;
    static readonly RULE_viewNameCreate = 333;
    static readonly RULE_subQuerySource = 334;
    static readonly RULE_partitioningSpec = 335;
    static readonly RULE_partitionTableFunctionSource = 336;
    static readonly RULE_partitionedTableFunction = 337;
    static readonly RULE_whereClause = 338;
    static readonly RULE_searchCondition = 339;
    static readonly RULE_valuesSource = 340;
    static readonly RULE_valuesClause = 341;
    static readonly RULE_valuesTableConstructor = 342;
    static readonly RULE_valueRowConstructor = 343;
    static readonly RULE_firstValueRowConstructor = 344;
    static readonly RULE_virtualTableSource = 345;
    static readonly RULE_selectClause = 346;
    static readonly RULE_all_distinct = 347;
    static readonly RULE_selectList = 348;
    static readonly RULE_selectTrfmClause = 349;
    static readonly RULE_selectItem = 350;
    static readonly RULE_trfmClause = 351;
    static readonly RULE_selectExpression = 352;
    static readonly RULE_selectExpressionList = 353;
    static readonly RULE_window_clause = 354;
    static readonly RULE_window_defn = 355;
    static readonly RULE_window_specification = 356;
    static readonly RULE_window_frame = 357;
    static readonly RULE_window_range_expression = 358;
    static readonly RULE_window_value_expression = 359;
    static readonly RULE_window_frame_start_boundary = 360;
    static readonly RULE_window_frame_boundary = 361;
    static readonly RULE_groupByClause = 362;
    static readonly RULE_groupby_expression = 363;
    static readonly RULE_groupByEmpty = 364;
    static readonly RULE_rollupStandard = 365;
    static readonly RULE_rollupOldSyntax = 366;
    static readonly RULE_groupingSetExpression = 367;
    static readonly RULE_groupingSetExpressionMultiple = 368;
    static readonly RULE_groupingExpressionSingle = 369;
    static readonly RULE_havingClause = 370;
    static readonly RULE_qualifyClause = 371;
    static readonly RULE_havingCondition = 372;
    static readonly RULE_expressionsInParenthesis = 373;
    static readonly RULE_expressionsNotInParenthesis = 374;
    static readonly RULE_expressionPart = 375;
    static readonly RULE_expressionOrDefault = 376;
    static readonly RULE_firstExpressionsWithAlias = 377;
    static readonly RULE_expressionWithAlias = 378;
    static readonly RULE_expressions = 379;
    static readonly RULE_columnRefOrderInParenthesis = 380;
    static readonly RULE_columnRefOrderNotInParenthesis = 381;
    static readonly RULE_orderByClause = 382;
    static readonly RULE_clusterByClause = 383;
    static readonly RULE_partitionByClause = 384;
    static readonly RULE_distributeByClause = 385;
    static readonly RULE_sortByClause = 386;
    static readonly RULE_trimFunction = 387;
    static readonly RULE_function_ = 388;
    static readonly RULE_null_treatment = 389;
    static readonly RULE_functionNameCreate = 390;
    static readonly RULE_functionNameForDDL = 391;
    static readonly RULE_functionNameForInvoke = 392;
    static readonly RULE_userDefinedFuncName = 393;
    static readonly RULE_internalFunctionName = 394;
    static readonly RULE_castExpression = 395;
    static readonly RULE_caseExpression = 396;
    static readonly RULE_whenExpression = 397;
    static readonly RULE_floorExpression = 398;
    static readonly RULE_floorDateQualifiers = 399;
    static readonly RULE_extractExpression = 400;
    static readonly RULE_timeQualifiers = 401;
    static readonly RULE_constant = 402;
    static readonly RULE_prepareStmtParam = 403;
    static readonly RULE_parameterIdx = 404;
    static readonly RULE_stringLiteralSequence = 405;
    static readonly RULE_charSetStringLiteral = 406;
    static readonly RULE_dateLiteral = 407;
    static readonly RULE_timestampLiteral = 408;
    static readonly RULE_timestampLocalTZLiteral = 409;
    static readonly RULE_intervalValue = 410;
    static readonly RULE_intervalLiteral = 411;
    static readonly RULE_intervalExpression = 412;
    static readonly RULE_intervalQualifiers = 413;
    static readonly RULE_expression = 414;
    static readonly RULE_atomExpression = 415;
    static readonly RULE_precedenceFieldExpression = 416;
    static readonly RULE_precedenceUnaryOperator = 417;
    static readonly RULE_precedenceUnaryPrefixExpression = 418;
    static readonly RULE_precedenceBitwiseXorOperator = 419;
    static readonly RULE_precedenceBitwiseXorExpression = 420;
    static readonly RULE_precedenceStarOperator = 421;
    static readonly RULE_precedenceStarExpression = 422;
    static readonly RULE_precedencePlusOperator = 423;
    static readonly RULE_precedencePlusExpression = 424;
    static readonly RULE_precedenceConcatenateOperator = 425;
    static readonly RULE_precedenceConcatenateExpression = 426;
    static readonly RULE_precedenceAmpersandOperator = 427;
    static readonly RULE_precedenceAmpersandExpression = 428;
    static readonly RULE_precedenceBitwiseOrOperator = 429;
    static readonly RULE_precedenceBitwiseOrExpression = 430;
    static readonly RULE_precedenceRegexpOperator = 431;
    static readonly RULE_precedenceSimilarOperator = 432;
    static readonly RULE_subQueryExpression = 433;
    static readonly RULE_precedenceSimilarExpression = 434;
    static readonly RULE_precedenceSimilarExpressionMain = 435;
    static readonly RULE_precedenceSimilarExpressionPart = 436;
    static readonly RULE_precedenceSimilarExpressionAtom = 437;
    static readonly RULE_precedenceSimilarExpressionQuantifierPredicate = 438;
    static readonly RULE_quantifierType = 439;
    static readonly RULE_precedenceSimilarExpressionIn = 440;
    static readonly RULE_precedenceSimilarExpressionPartNot = 441;
    static readonly RULE_precedenceDistinctOperator = 442;
    static readonly RULE_precedenceEqualOperator = 443;
    static readonly RULE_precedenceEqualExpression = 444;
    static readonly RULE_isCondition = 445;
    static readonly RULE_precedenceUnarySuffixExpression = 446;
    static readonly RULE_precedenceNotOperator = 447;
    static readonly RULE_precedenceNotExpression = 448;
    static readonly RULE_precedenceAndOperator = 449;
    static readonly RULE_precedenceAndExpression = 450;
    static readonly RULE_precedenceOrOperator = 451;
    static readonly RULE_precedenceOrExpression = 452;
    static readonly RULE_booleanValue = 453;
    static readonly RULE_booleanValueTok = 454;
    static readonly RULE_tableOrPartition = 455;
    static readonly RULE_partitionSpec = 456;
    static readonly RULE_partitionVal = 457;
    static readonly RULE_partitionSelectorSpec = 458;
    static readonly RULE_partitionSelectorVal = 459;
    static readonly RULE_partitionSelectorOperator = 460;
    static readonly RULE_subQuerySelectorOperator = 461;
    static readonly RULE_sysFuncNames = 462;
    static readonly RULE_id_ = 463;
    static readonly RULE_functionIdentifier = 464;
    static readonly RULE_principalIdentifier = 465;
    static readonly RULE_nonReserved = 466;
    static readonly RULE_sql11ReservedKeywordsUsedAsFunctionName = 467;
    static readonly RULE_hint = 468;
    static readonly RULE_hintList = 469;
    static readonly RULE_hintItem = 470;
    static readonly RULE_hintName = 471;
    static readonly RULE_hintArgs = 472;
    static readonly RULE_hintArgName = 473;
    static readonly RULE_prepareStatement = 474;
    static readonly RULE_executeStatement = 475;
    static readonly RULE_executeParamList = 476;
    static readonly RULE_resourcePlanDdlStatements = 477;
    static readonly RULE_rpAssign = 478;
    static readonly RULE_rpAssignList = 479;
    static readonly RULE_rpUnassign = 480;
    static readonly RULE_rpUnassignList = 481;
    static readonly RULE_createResourcePlanStatement = 482;
    static readonly RULE_withReplace = 483;
    static readonly RULE_activate = 484;
    static readonly RULE_enable = 485;
    static readonly RULE_disable = 486;
    static readonly RULE_unmanaged = 487;
    static readonly RULE_year = 488;
    static readonly RULE_month = 489;
    static readonly RULE_week = 490;
    static readonly RULE_day = 491;
    static readonly RULE_hour = 492;
    static readonly RULE_minute = 493;
    static readonly RULE_second = 494;
    static readonly RULE_decimal = 495;
    static readonly RULE_alterResourcePlanStatement = 496;
    static readonly RULE_globalWmStatement = 497;
    static readonly RULE_replaceResourcePlanStatement = 498;
    static readonly RULE_dropResourcePlanStatement = 499;
    static readonly RULE_poolPath = 500;
    static readonly RULE_triggerExpression = 501;
    static readonly RULE_triggerExpressionStandalone = 502;
    static readonly RULE_triggerOrExpression = 503;
    static readonly RULE_triggerAndExpression = 504;
    static readonly RULE_triggerAtomExpression = 505;
    static readonly RULE_triggerLiteral = 506;
    static readonly RULE_comparisionOperator = 507;
    static readonly RULE_triggerActionExpression = 508;
    static readonly RULE_triggerActionExpressionStandalone = 509;
    static readonly RULE_createTriggerStatement = 510;
    static readonly RULE_alterTriggerStatement = 511;
    static readonly RULE_dropTriggerStatement = 512;
    static readonly RULE_poolAssign = 513;
    static readonly RULE_poolAssignList = 514;
    static readonly RULE_createPoolStatement = 515;
    static readonly RULE_alterPoolStatement = 516;
    static readonly RULE_dropPoolStatement = 517;
    static readonly RULE_createMappingStatement = 518;
    static readonly RULE_alterMappingStatement = 519;
    static readonly RULE_dropMappingStatement = 520;
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
    statement(): StatementContext;
    explainStatement(): ExplainStatementContext;
    explainOption(): ExplainOptionContext;
    vectorizationOnly(): VectorizationOnlyContext;
    vectorizatonDetail(): VectorizatonDetailContext;
    execStatement(): ExecStatementContext;
    loadStatement(): LoadStatementContext;
    dropPartitionsIgnoreClause(): DropPartitionsIgnoreClauseContext;
    replicationClause(): ReplicationClauseContext;
    exportStatement(): ExportStatementContext;
    importStatement(): ImportStatementContext;
    replDumpStatement(): ReplDumpStatementContext;
    replDbPolicy(): ReplDbPolicyContext;
    replLoadStatement(): ReplLoadStatementContext;
    replConfigs(): ReplConfigsContext;
    replConfigsList(): ReplConfigsListContext;
    replTableLevelPolicy(): ReplTableLevelPolicyContext;
    replStatusStatement(): ReplStatusStatementContext;
    ddlStatement(): DdlStatementContext;
    ifExists(): IfExistsContext;
    restrictOrCascade(): RestrictOrCascadeContext;
    ifNotExists(): IfNotExistsContext;
    force(): ForceContext;
    rewriteEnabled(): RewriteEnabledContext;
    rewriteDisabled(): RewriteDisabledContext;
    storedAsDirs(): StoredAsDirsContext;
    orReplace(): OrReplaceContext;
    createDatabaseStatement(): CreateDatabaseStatementContext;
    dbLocation(): DbLocationContext;
    dbManagedLocation(): DbManagedLocationContext;
    dbProperties(): DbPropertiesContext;
    dbPropertiesList(): DbPropertiesListContext;
    dbConnectorName(): DbConnectorNameContext;
    switchDatabaseStatement(): SwitchDatabaseStatementContext;
    dropDatabaseStatement(): DropDatabaseStatementContext;
    databaseComment(): DatabaseCommentContext;
    truncateTableStatement(): TruncateTableStatementContext;
    dropTableStatement(): DropTableStatementContext;
    inputFileFormat(): InputFileFormatContext;
    tabTypeExpr(): TabTypeExprContext;
    partTypeExpr(): PartTypeExprContext;
    tabPartColTypeExpr(): TabPartColTypeExprContext;
    descStatement(): DescStatementContext;
    analyzeStatement(): AnalyzeStatementContext;
    from_in(): From_inContext;
    db_schema(): Db_schemaContext;
    showStatement(): ShowStatementContext;
    showTablesFilterExpr(): ShowTablesFilterExprContext;
    lockStatement(): LockStatementContext;
    lockDatabase(): LockDatabaseContext;
    lockMode(): LockModeContext;
    unlockStatement(): UnlockStatementContext;
    unlockDatabase(): UnlockDatabaseContext;
    createRoleStatement(): CreateRoleStatementContext;
    dropRoleStatement(): DropRoleStatementContext;
    grantPrivileges(): GrantPrivilegesContext;
    revokePrivileges(): RevokePrivilegesContext;
    grantRole(): GrantRoleContext;
    revokeRole(): RevokeRoleContext;
    showRoleGrants(): ShowRoleGrantsContext;
    showRoles(): ShowRolesContext;
    showCurrentRole(): ShowCurrentRoleContext;
    setRole(): SetRoleContext;
    showGrants(): ShowGrantsContext;
    showRolePrincipals(): ShowRolePrincipalsContext;
    privilegeIncludeColObject(): PrivilegeIncludeColObjectContext;
    privilegeObject(): PrivilegeObjectContext;
    privObject(): PrivObjectContext;
    privObjectCols(): PrivObjectColsContext;
    privilegeList(): PrivilegeListContext;
    privlegeDef(): PrivlegeDefContext;
    privilegeType(): PrivilegeTypeContext;
    principalSpecification(): PrincipalSpecificationContext;
    principalName(): PrincipalNameContext;
    principalAlterName(): PrincipalAlterNameContext;
    withGrantOption(): WithGrantOptionContext;
    grantOptionFor(): GrantOptionForContext;
    adminOptionFor(): AdminOptionForContext;
    withAdminOption(): WithAdminOptionContext;
    metastoreCheck(): MetastoreCheckContext;
    resourceList(): ResourceListContext;
    resource(): ResourceContext;
    resourceType(): ResourceTypeContext;
    createFunctionStatement(): CreateFunctionStatementContext;
    dropFunctionStatement(): DropFunctionStatementContext;
    reloadFunctionsStatement(): ReloadFunctionsStatementContext;
    createMacroStatement(): CreateMacroStatementContext;
    dropMacroStatement(): DropMacroStatementContext;
    createIndexStatement(): CreateIndexStatementContext;
    dropIndexStatement(): DropIndexStatementContext;
    createViewStatement(): CreateViewStatementContext;
    viewPartition(): ViewPartitionContext;
    viewOrganization(): ViewOrganizationContext;
    viewClusterSpec(): ViewClusterSpecContext;
    viewComplexSpec(): ViewComplexSpecContext;
    viewDistSpec(): ViewDistSpecContext;
    viewSortSpec(): ViewSortSpecContext;
    dropViewStatement(): DropViewStatementContext;
    createMaterializedViewStatement(): CreateMaterializedViewStatementContext;
    dropMaterializedViewStatement(): DropMaterializedViewStatementContext;
    createScheduledQueryStatement(): CreateScheduledQueryStatementContext;
    dropScheduledQueryStatement(): DropScheduledQueryStatementContext;
    alterScheduledQueryStatement(): AlterScheduledQueryStatementContext;
    alterScheduledQueryChange(): AlterScheduledQueryChangeContext;
    scheduleSpec(): ScheduleSpecContext;
    executedAsSpec(): ExecutedAsSpecContext;
    definedAsSpec(): DefinedAsSpecContext;
    showStmtIdentifier(): ShowStmtIdentifierContext;
    tableComment(): TableCommentContext;
    createTablePartitionSpec(): CreateTablePartitionSpecContext;
    createTablePartitionColumnTypeSpec(): CreateTablePartitionColumnTypeSpecContext;
    createTablePartitionColumnSpec(): CreateTablePartitionColumnSpecContext;
    partitionTransformSpec(): PartitionTransformSpecContext;
    columnNameTransformConstraint(): ColumnNameTransformConstraintContext;
    partitionTransformType(): PartitionTransformTypeContext;
    tableBuckets(): TableBucketsContext;
    tableImplBuckets(): TableImplBucketsContext;
    tableSkewed(): TableSkewedContext;
    rowFormat(): RowFormatContext;
    recordReader(): RecordReaderContext;
    recordWriter(): RecordWriterContext;
    rowFormatSerde(): RowFormatSerdeContext;
    rowFormatDelimited(): RowFormatDelimitedContext;
    tableRowFormat(): TableRowFormatContext;
    tablePropertiesPrefixed(): TablePropertiesPrefixedContext;
    tableProperties(): TablePropertiesContext;
    tablePropertiesList(): TablePropertiesListContext;
    keyValueProperty(): KeyValuePropertyContext;
    keyProperty(): KeyPropertyContext;
    tableRowFormatFieldIdentifier(): TableRowFormatFieldIdentifierContext;
    tableRowFormatCollItemsIdentifier(): TableRowFormatCollItemsIdentifierContext;
    tableRowFormatMapKeysIdentifier(): TableRowFormatMapKeysIdentifierContext;
    tableRowFormatLinesIdentifier(): TableRowFormatLinesIdentifierContext;
    tableRowNullFormat(): TableRowNullFormatContext;
    tableFileFormat(): TableFileFormatContext;
    tableLocation(): TableLocationContext;
    columnNameTypeList(): ColumnNameTypeListContext;
    columnNameTypeOrConstraintList(): ColumnNameTypeOrConstraintListContext;
    columnNameColonTypeList(): ColumnNameColonTypeListContext;
    columnNameList(): ColumnNameListContext;
    columnName(): ColumnNameContext;
    columnNameCreate(): ColumnNameCreateContext;
    extColumnName(): ExtColumnNameContext;
    columnNameOrderList(): ColumnNameOrderListContext;
    columnParenthesesList(): ColumnParenthesesListContext;
    enableValidateSpecification(): EnableValidateSpecificationContext;
    enableSpecification(): EnableSpecificationContext;
    validateSpecification(): ValidateSpecificationContext;
    enforcedSpecification(): EnforcedSpecificationContext;
    relySpecification(): RelySpecificationContext;
    createConstraint(): CreateConstraintContext;
    alterConstraintWithName(): AlterConstraintWithNameContext;
    tableLevelConstraint(): TableLevelConstraintContext;
    pkUkConstraint(): PkUkConstraintContext;
    checkConstraint(): CheckConstraintContext;
    createForeignKey(): CreateForeignKeyContext;
    alterForeignKeyWithName(): AlterForeignKeyWithNameContext;
    skewedValueElement(): SkewedValueElementContext;
    skewedColumnValuePairList(): SkewedColumnValuePairListContext;
    skewedColumnValuePair(): SkewedColumnValuePairContext;
    skewedColumnValues(): SkewedColumnValuesContext;
    skewedColumnValue(): SkewedColumnValueContext;
    skewedValueLocationElement(): SkewedValueLocationElementContext;
    orderSpecification(): OrderSpecificationContext;
    nullOrdering(): NullOrderingContext;
    columnNameOrder(): ColumnNameOrderContext;
    columnNameCommentList(): ColumnNameCommentListContext;
    columnNameComment(): ColumnNameCommentContext;
    orderSpecificationRewrite(): OrderSpecificationRewriteContext;
    columnRefOrder(): ColumnRefOrderContext;
    columnNameType(): ColumnNameTypeContext;
    columnNameTypeOrConstraint(): ColumnNameTypeOrConstraintContext;
    tableConstraint(): TableConstraintContext;
    columnNameTypeConstraint(): ColumnNameTypeConstraintContext;
    columnConstraint(): ColumnConstraintContext;
    foreignKeyConstraint(): ForeignKeyConstraintContext;
    colConstraint(): ColConstraintContext;
    alterColumnConstraint(): AlterColumnConstraintContext;
    alterForeignKeyConstraint(): AlterForeignKeyConstraintContext;
    alterColConstraint(): AlterColConstraintContext;
    columnConstraintType(): ColumnConstraintTypeContext;
    defaultVal(): DefaultValContext;
    tableConstraintType(): TableConstraintTypeContext;
    constraintOptsCreate(): ConstraintOptsCreateContext;
    constraintOptsAlter(): ConstraintOptsAlterContext;
    columnNameColonType(): ColumnNameColonTypeContext;
    colType(): ColTypeContext;
    colTypeList(): ColTypeListContext;
    type(): TypeContext;
    primitiveType(): PrimitiveTypeContext;
    listType(): ListTypeContext;
    structType(): StructTypeContext;
    mapType(): MapTypeContext;
    unionType(): UnionTypeContext;
    setOperator(): SetOperatorContext;
    queryStatementExpression(): QueryStatementExpressionContext;
    queryStatementExpressionBody(): QueryStatementExpressionBodyContext;
    withClause(): WithClauseContext;
    cteStatement(): CteStatementContext;
    fromStatement(): FromStatementContext;
    singleFromStatement(): SingleFromStatementContext;
    regularBody(): RegularBodyContext;
    atomSelectStatement(): AtomSelectStatementContext;
    selectStatement(): SelectStatementContext;
    setOpSelectStatement(): SetOpSelectStatementContext;
    selectStatementWithCTE(): SelectStatementWithCTEContext;
    body(): BodyContext;
    insertClause(): InsertClauseContext;
    destination(): DestinationContext;
    limitClause(): LimitClauseContext;
    deleteStatement(): DeleteStatementContext;
    columnAssignmentClause(): ColumnAssignmentClauseContext;
    precedencePlusExpressionOrDefault(): PrecedencePlusExpressionOrDefaultContext;
    setColumnsClause(): SetColumnsClauseContext;
    updateStatement(): UpdateStatementContext;
    sqlTransactionStatement(): SqlTransactionStatementContext;
    startTransactionStatement(): StartTransactionStatementContext;
    transactionMode(): TransactionModeContext;
    transactionAccessMode(): TransactionAccessModeContext;
    isolationLevel(): IsolationLevelContext;
    levelOfIsolation(): LevelOfIsolationContext;
    commitStatement(): CommitStatementContext;
    rollbackStatement(): RollbackStatementContext;
    setAutoCommitStatement(): SetAutoCommitStatementContext;
    abortTransactionStatement(): AbortTransactionStatementContext;
    abortCompactionStatement(): AbortCompactionStatementContext;
    mergeStatement(): MergeStatementContext;
    whenClauses(): WhenClausesContext;
    whenNotMatchedClause(): WhenNotMatchedClauseContext;
    whenMatchedAndClause(): WhenMatchedAndClauseContext;
    whenMatchedThenClause(): WhenMatchedThenClauseContext;
    updateOrDelete(): UpdateOrDeleteContext;
    killQueryStatement(): KillQueryStatementContext;
    compactionId(): CompactionIdContext;
    compactionPool(): CompactionPoolContext;
    compactionType(): CompactionTypeContext;
    compactionStatus(): CompactionStatusContext;
    alterStatement(): AlterStatementContext;
    alterTableStatementSuffix(): AlterTableStatementSuffixContext;
    alterTblPartitionStatementSuffix(): AlterTblPartitionStatementSuffixContext;
    alterStatementPartitionKeyType(): AlterStatementPartitionKeyTypeContext;
    alterViewStatementSuffix(): AlterViewStatementSuffixContext;
    alterMaterializedViewStatementSuffix(): AlterMaterializedViewStatementSuffixContext;
    alterMaterializedViewSuffixRewrite(): AlterMaterializedViewSuffixRewriteContext;
    alterMaterializedViewSuffixRebuild(): AlterMaterializedViewSuffixRebuildContext;
    alterDatabaseStatementSuffix(): AlterDatabaseStatementSuffixContext;
    alterDatabaseSuffixProperties(): AlterDatabaseSuffixPropertiesContext;
    alterDatabaseSuffixSetOwner(): AlterDatabaseSuffixSetOwnerContext;
    alterDatabaseSuffixSetLocation(): AlterDatabaseSuffixSetLocationContext;
    alterDatabaseSuffixSetManagedLocation(): AlterDatabaseSuffixSetManagedLocationContext;
    alterStatementSuffixRename(): AlterStatementSuffixRenameContext;
    alterStatementSuffixAddCol(): AlterStatementSuffixAddColContext;
    alterStatementSuffixAddConstraint(): AlterStatementSuffixAddConstraintContext;
    alterStatementSuffixUpdateColumns(): AlterStatementSuffixUpdateColumnsContext;
    alterStatementSuffixProtections(): AlterStatementSuffixProtectionsContext;
    alterStatementSuffixDropConstraint(): AlterStatementSuffixDropConstraintContext;
    alterStatementSuffixRenameCol(): AlterStatementSuffixRenameColContext;
    alterStatementSuffixUpdateStatsCol(): AlterStatementSuffixUpdateStatsColContext;
    alterStatementSuffixUpdateStats(): AlterStatementSuffixUpdateStatsContext;
    alterStatementChangeColPosition(): AlterStatementChangeColPositionContext;
    alterStatementSuffixAddPartitions(): AlterStatementSuffixAddPartitionsContext;
    alterStatementSuffixAddPartitionsElement(): AlterStatementSuffixAddPartitionsElementContext;
    alterStatementSuffixTouch(): AlterStatementSuffixTouchContext;
    alterStatementSuffixArchive(): AlterStatementSuffixArchiveContext;
    alterStatementSuffixUnArchive(): AlterStatementSuffixUnArchiveContext;
    partitionLocation(): PartitionLocationContext;
    alterStatementSuffixRecoverPartitions(): AlterStatementSuffixRecoverPartitionsContext;
    alterStatementSuffixDropPartitions(): AlterStatementSuffixDropPartitionsContext;
    alterStatementSuffixProperties(): AlterStatementSuffixPropertiesContext;
    alterViewSuffixProperties(): AlterViewSuffixPropertiesContext;
    alterStatementSuffixSerdeProperties(): AlterStatementSuffixSerdePropertiesContext;
    tablePartitionPrefix(): TablePartitionPrefixContext;
    alterStatementSuffixFileFormat(): AlterStatementSuffixFileFormatContext;
    alterStatementSuffixClusterbySortby(): AlterStatementSuffixClusterbySortbyContext;
    alterTblPartitionStatementSuffixSkewedLocation(): AlterTblPartitionStatementSuffixSkewedLocationContext;
    skewedLocations(): SkewedLocationsContext;
    skewedLocationsList(): SkewedLocationsListContext;
    skewedLocationMap(): SkewedLocationMapContext;
    alterStatementSuffixLocation(): AlterStatementSuffixLocationContext;
    alterStatementSuffixSkewedby(): AlterStatementSuffixSkewedbyContext;
    alterStatementSuffixExchangePartition(): AlterStatementSuffixExchangePartitionContext;
    alterStatementSuffixRenamePart(): AlterStatementSuffixRenamePartContext;
    alterStatementSuffixStatsPart(): AlterStatementSuffixStatsPartContext;
    alterStatementSuffixMergeFiles(): AlterStatementSuffixMergeFilesContext;
    alterStatementSuffixBucketNum(): AlterStatementSuffixBucketNumContext;
    blocking(): BlockingContext;
    compactPool(): CompactPoolContext;
    alterStatementSuffixCompact(): AlterStatementSuffixCompactContext;
    alterStatementSuffixSetOwner(): AlterStatementSuffixSetOwnerContext;
    alterStatementSuffixSetPartSpec(): AlterStatementSuffixSetPartSpecContext;
    alterStatementSuffixExecute(): AlterStatementSuffixExecuteContext;
    alterIndexStatementSuffix(): AlterIndexStatementSuffixContext;
    fileFormat(): FileFormatContext;
    alterDataConnectorStatementSuffix(): AlterDataConnectorStatementSuffixContext;
    alterDataConnectorSuffixProperties(): AlterDataConnectorSuffixPropertiesContext;
    alterDataConnectorSuffixSetOwner(): AlterDataConnectorSuffixSetOwnerContext;
    alterDataConnectorSuffixSetUrl(): AlterDataConnectorSuffixSetUrlContext;
    likeTableOrFile(): LikeTableOrFileContext;
    createTableStatement(): CreateTableStatementContext;
    createDataConnectorStatement(): CreateDataConnectorStatementContext;
    dataConnectorComment(): DataConnectorCommentContext;
    dataConnectorUrl(): DataConnectorUrlContext;
    dataConnectorType(): DataConnectorTypeContext;
    dcProperties(): DcPropertiesContext;
    dropDataConnectorStatement(): DropDataConnectorStatementContext;
    tableAllColumns(): TableAllColumnsContext;
    defaultValue(): DefaultValueContext;
    expressionList(): ExpressionListContext;
    aliasList(): AliasListContext;
    fromClause(): FromClauseContext;
    fromSource(): FromSourceContext;
    atomjoinSource(): AtomjoinSourceContext;
    joinSource(): JoinSourceContext;
    joinSourcePart(): JoinSourcePartContext;
    uniqueJoinSource(): UniqueJoinSourceContext;
    uniqueJoinExpr(): UniqueJoinExprContext;
    uniqueJoinToken(): UniqueJoinTokenContext;
    joinToken(): JoinTokenContext;
    lateralView(): LateralViewContext;
    tableAlias(): TableAliasContext;
    tableBucketSample(): TableBucketSampleContext;
    splitSample(): SplitSampleContext;
    tableSample(): TableSampleContext;
    tableSource(): TableSourceContext;
    asOfClause(): AsOfClauseContext;
    uniqueJoinTableSource(): UniqueJoinTableSourceContext;
    dbSchemaName(): DbSchemaNameContext;
    dbSchemaNameCreate(): DbSchemaNameCreateContext;
    tableOrView(): TableOrViewContext;
    tableName(): TableNameContext;
    tableNameCreate(): TableNameCreateContext;
    viewName(): ViewNameContext;
    viewNameCreate(): ViewNameCreateContext;
    subQuerySource(): SubQuerySourceContext;
    partitioningSpec(): PartitioningSpecContext;
    partitionTableFunctionSource(): PartitionTableFunctionSourceContext;
    partitionedTableFunction(): PartitionedTableFunctionContext;
    whereClause(): WhereClauseContext;
    searchCondition(): SearchConditionContext;
    valuesSource(): ValuesSourceContext;
    valuesClause(): ValuesClauseContext;
    valuesTableConstructor(): ValuesTableConstructorContext;
    valueRowConstructor(): ValueRowConstructorContext;
    firstValueRowConstructor(): FirstValueRowConstructorContext;
    virtualTableSource(): VirtualTableSourceContext;
    selectClause(): SelectClauseContext;
    all_distinct(): All_distinctContext;
    selectList(): SelectListContext;
    selectTrfmClause(): SelectTrfmClauseContext;
    selectItem(): SelectItemContext;
    trfmClause(): TrfmClauseContext;
    selectExpression(): SelectExpressionContext;
    selectExpressionList(): SelectExpressionListContext;
    window_clause(): Window_clauseContext;
    window_defn(): Window_defnContext;
    window_specification(): Window_specificationContext;
    window_frame(): Window_frameContext;
    window_range_expression(): Window_range_expressionContext;
    window_value_expression(): Window_value_expressionContext;
    window_frame_start_boundary(): Window_frame_start_boundaryContext;
    window_frame_boundary(): Window_frame_boundaryContext;
    groupByClause(): GroupByClauseContext;
    groupby_expression(): Groupby_expressionContext;
    groupByEmpty(): GroupByEmptyContext;
    rollupStandard(): RollupStandardContext;
    rollupOldSyntax(): RollupOldSyntaxContext;
    groupingSetExpression(): GroupingSetExpressionContext;
    groupingSetExpressionMultiple(): GroupingSetExpressionMultipleContext;
    groupingExpressionSingle(): GroupingExpressionSingleContext;
    havingClause(): HavingClauseContext;
    qualifyClause(): QualifyClauseContext;
    havingCondition(): HavingConditionContext;
    expressionsInParenthesis(): ExpressionsInParenthesisContext;
    expressionsNotInParenthesis(): ExpressionsNotInParenthesisContext;
    expressionPart(): ExpressionPartContext;
    expressionOrDefault(): ExpressionOrDefaultContext;
    firstExpressionsWithAlias(): FirstExpressionsWithAliasContext;
    expressionWithAlias(): ExpressionWithAliasContext;
    expressions(): ExpressionsContext;
    columnRefOrderInParenthesis(): ColumnRefOrderInParenthesisContext;
    columnRefOrderNotInParenthesis(): ColumnRefOrderNotInParenthesisContext;
    orderByClause(): OrderByClauseContext;
    clusterByClause(): ClusterByClauseContext;
    partitionByClause(): PartitionByClauseContext;
    distributeByClause(): DistributeByClauseContext;
    sortByClause(): SortByClauseContext;
    trimFunction(): TrimFunctionContext;
    function_(): Function_Context;
    null_treatment(): Null_treatmentContext;
    functionNameCreate(): FunctionNameCreateContext;
    functionNameForDDL(): FunctionNameForDDLContext;
    functionNameForInvoke(): FunctionNameForInvokeContext;
    userDefinedFuncName(): UserDefinedFuncNameContext;
    internalFunctionName(): InternalFunctionNameContext;
    castExpression(): CastExpressionContext;
    caseExpression(): CaseExpressionContext;
    whenExpression(): WhenExpressionContext;
    floorExpression(): FloorExpressionContext;
    floorDateQualifiers(): FloorDateQualifiersContext;
    extractExpression(): ExtractExpressionContext;
    timeQualifiers(): TimeQualifiersContext;
    constant(): ConstantContext;
    prepareStmtParam(): PrepareStmtParamContext;
    parameterIdx(): ParameterIdxContext;
    stringLiteralSequence(): StringLiteralSequenceContext;
    charSetStringLiteral(): CharSetStringLiteralContext;
    dateLiteral(): DateLiteralContext;
    timestampLiteral(): TimestampLiteralContext;
    timestampLocalTZLiteral(): TimestampLocalTZLiteralContext;
    intervalValue(): IntervalValueContext;
    intervalLiteral(): IntervalLiteralContext;
    intervalExpression(): IntervalExpressionContext;
    intervalQualifiers(): IntervalQualifiersContext;
    expression(): ExpressionContext;
    atomExpression(): AtomExpressionContext;
    precedenceFieldExpression(): PrecedenceFieldExpressionContext;
    precedenceUnaryOperator(): PrecedenceUnaryOperatorContext;
    precedenceUnaryPrefixExpression(): PrecedenceUnaryPrefixExpressionContext;
    precedenceBitwiseXorOperator(): PrecedenceBitwiseXorOperatorContext;
    precedenceBitwiseXorExpression(): PrecedenceBitwiseXorExpressionContext;
    precedenceStarOperator(): PrecedenceStarOperatorContext;
    precedenceStarExpression(): PrecedenceStarExpressionContext;
    precedencePlusOperator(): PrecedencePlusOperatorContext;
    precedencePlusExpression(): PrecedencePlusExpressionContext;
    precedenceConcatenateOperator(): PrecedenceConcatenateOperatorContext;
    precedenceConcatenateExpression(): PrecedenceConcatenateExpressionContext;
    precedenceAmpersandOperator(): PrecedenceAmpersandOperatorContext;
    precedenceAmpersandExpression(): PrecedenceAmpersandExpressionContext;
    precedenceBitwiseOrOperator(): PrecedenceBitwiseOrOperatorContext;
    precedenceBitwiseOrExpression(): PrecedenceBitwiseOrExpressionContext;
    precedenceRegexpOperator(): PrecedenceRegexpOperatorContext;
    precedenceSimilarOperator(): PrecedenceSimilarOperatorContext;
    subQueryExpression(): SubQueryExpressionContext;
    precedenceSimilarExpression(): PrecedenceSimilarExpressionContext;
    precedenceSimilarExpressionMain(): PrecedenceSimilarExpressionMainContext;
    precedenceSimilarExpressionPart(): PrecedenceSimilarExpressionPartContext;
    precedenceSimilarExpressionAtom(): PrecedenceSimilarExpressionAtomContext;
    precedenceSimilarExpressionQuantifierPredicate(): PrecedenceSimilarExpressionQuantifierPredicateContext;
    quantifierType(): QuantifierTypeContext;
    precedenceSimilarExpressionIn(): PrecedenceSimilarExpressionInContext;
    precedenceSimilarExpressionPartNot(): PrecedenceSimilarExpressionPartNotContext;
    precedenceDistinctOperator(): PrecedenceDistinctOperatorContext;
    precedenceEqualOperator(): PrecedenceEqualOperatorContext;
    precedenceEqualExpression(): PrecedenceEqualExpressionContext;
    isCondition(): IsConditionContext;
    precedenceUnarySuffixExpression(): PrecedenceUnarySuffixExpressionContext;
    precedenceNotOperator(): PrecedenceNotOperatorContext;
    precedenceNotExpression(): PrecedenceNotExpressionContext;
    precedenceAndOperator(): PrecedenceAndOperatorContext;
    precedenceAndExpression(): PrecedenceAndExpressionContext;
    precedenceOrOperator(): PrecedenceOrOperatorContext;
    precedenceOrExpression(): PrecedenceOrExpressionContext;
    booleanValue(): BooleanValueContext;
    booleanValueTok(): BooleanValueTokContext;
    tableOrPartition(): TableOrPartitionContext;
    partitionSpec(): PartitionSpecContext;
    partitionVal(): PartitionValContext;
    partitionSelectorSpec(): PartitionSelectorSpecContext;
    partitionSelectorVal(): PartitionSelectorValContext;
    partitionSelectorOperator(): PartitionSelectorOperatorContext;
    subQuerySelectorOperator(): SubQuerySelectorOperatorContext;
    sysFuncNames(): SysFuncNamesContext;
    id_(): Id_Context;
    functionIdentifier(): FunctionIdentifierContext;
    principalIdentifier(): PrincipalIdentifierContext;
    nonReserved(): NonReservedContext;
    sql11ReservedKeywordsUsedAsFunctionName(): Sql11ReservedKeywordsUsedAsFunctionNameContext;
    hint(): HintContext;
    hintList(): HintListContext;
    hintItem(): HintItemContext;
    hintName(): HintNameContext;
    hintArgs(): HintArgsContext;
    hintArgName(): HintArgNameContext;
    prepareStatement(): PrepareStatementContext;
    executeStatement(): ExecuteStatementContext;
    executeParamList(): ExecuteParamListContext;
    resourcePlanDdlStatements(): ResourcePlanDdlStatementsContext;
    rpAssign(): RpAssignContext;
    rpAssignList(): RpAssignListContext;
    rpUnassign(): RpUnassignContext;
    rpUnassignList(): RpUnassignListContext;
    createResourcePlanStatement(): CreateResourcePlanStatementContext;
    withReplace(): WithReplaceContext;
    activate(): ActivateContext;
    enable(): EnableContext;
    disable(): DisableContext;
    unmanaged(): UnmanagedContext;
    year(): YearContext;
    month(): MonthContext;
    week(): WeekContext;
    day(): DayContext;
    hour(): HourContext;
    minute(): MinuteContext;
    second(): SecondContext;
    decimal(): DecimalContext;
    alterResourcePlanStatement(): AlterResourcePlanStatementContext;
    globalWmStatement(): GlobalWmStatementContext;
    replaceResourcePlanStatement(): ReplaceResourcePlanStatementContext;
    dropResourcePlanStatement(): DropResourcePlanStatementContext;
    poolPath(): PoolPathContext;
    triggerExpression(): TriggerExpressionContext;
    triggerExpressionStandalone(): TriggerExpressionStandaloneContext;
    triggerOrExpression(): TriggerOrExpressionContext;
    triggerAndExpression(): TriggerAndExpressionContext;
    triggerAtomExpression(): TriggerAtomExpressionContext;
    triggerLiteral(): TriggerLiteralContext;
    comparisionOperator(): ComparisionOperatorContext;
    triggerActionExpression(): TriggerActionExpressionContext;
    triggerActionExpressionStandalone(): TriggerActionExpressionStandaloneContext;
    createTriggerStatement(): CreateTriggerStatementContext;
    alterTriggerStatement(): AlterTriggerStatementContext;
    dropTriggerStatement(): DropTriggerStatementContext;
    poolAssign(): PoolAssignContext;
    poolAssignList(): PoolAssignListContext;
    createPoolStatement(): CreatePoolStatementContext;
    alterPoolStatement(): AlterPoolStatementContext;
    dropPoolStatement(): DropPoolStatementContext;
    createMappingStatement(): CreateMappingStatementContext;
    alterMappingStatement(): AlterMappingStatementContext;
    dropMappingStatement(): DropMappingStatementContext;
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
    static readonly _serializedATN: string;
    static __ATN: ATN;
    static get _ATN(): ATN;
}
export declare class ProgramContext extends ParserRuleContext {
    EOF(): TerminalNode;
    statement(): StatementContext[];
    statement(i: number): StatementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class StatementContext extends ParserRuleContext {
    explainStatement(): ExplainStatementContext | undefined;
    execStatement(): ExecStatementContext | undefined;
    SEMICOLON(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExplainStatementContext extends ParserRuleContext {
    KW_EXPLAIN(): TerminalNode;
    execStatement(): ExecStatementContext | undefined;
    KW_REWRITE(): TerminalNode | undefined;
    queryStatementExpression(): QueryStatementExpressionContext | undefined;
    explainOption(): ExplainOptionContext[];
    explainOption(i: number): ExplainOptionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExplainOptionContext extends ParserRuleContext {
    KW_EXTENDED(): TerminalNode | undefined;
    KW_FORMATTED(): TerminalNode | undefined;
    KW_DEPENDENCY(): TerminalNode | undefined;
    KW_CBO(): TerminalNode | undefined;
    KW_COST(): TerminalNode | undefined;
    KW_JOINCOST(): TerminalNode | undefined;
    KW_LOGICAL(): TerminalNode | undefined;
    KW_AUTHORIZATION(): TerminalNode | undefined;
    KW_ANALYZE(): TerminalNode | undefined;
    KW_REOPTIMIZATION(): TerminalNode | undefined;
    KW_LOCKS(): TerminalNode | undefined;
    KW_AST(): TerminalNode | undefined;
    KW_VECTORIZATION(): TerminalNode | undefined;
    vectorizationOnly(): VectorizationOnlyContext | undefined;
    vectorizatonDetail(): VectorizatonDetailContext | undefined;
    KW_DEBUG(): TerminalNode | undefined;
    KW_DDL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class VectorizationOnlyContext extends ParserRuleContext {
    KW_ONLY(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class VectorizatonDetailContext extends ParserRuleContext {
    KW_SUMMARY(): TerminalNode | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    KW_EXPRESSION(): TerminalNode | undefined;
    KW_DETAIL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExecStatementContext extends ParserRuleContext {
    queryStatementExpression(): QueryStatementExpressionContext | undefined;
    loadStatement(): LoadStatementContext | undefined;
    exportStatement(): ExportStatementContext | undefined;
    importStatement(): ImportStatementContext | undefined;
    replDumpStatement(): ReplDumpStatementContext | undefined;
    replLoadStatement(): ReplLoadStatementContext | undefined;
    replStatusStatement(): ReplStatusStatementContext | undefined;
    ddlStatement(): DdlStatementContext | undefined;
    deleteStatement(): DeleteStatementContext | undefined;
    updateStatement(): UpdateStatementContext | undefined;
    sqlTransactionStatement(): SqlTransactionStatementContext | undefined;
    mergeStatement(): MergeStatementContext | undefined;
    prepareStatement(): PrepareStatementContext | undefined;
    executeStatement(): ExecuteStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class LoadStatementContext extends ParserRuleContext {
    KW_LOAD(): TerminalNode;
    KW_DATA(): TerminalNode;
    KW_INPATH(): TerminalNode;
    StringLiteral(): TerminalNode;
    KW_INTO(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableOrPartition(): TableOrPartitionContext;
    KW_LOCAL(): TerminalNode | undefined;
    KW_OVERWRITE(): TerminalNode | undefined;
    inputFileFormat(): InputFileFormatContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropPartitionsIgnoreClauseContext extends ParserRuleContext {
    KW_IGNORE(): TerminalNode;
    KW_PROTECTION(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ReplicationClauseContext extends ParserRuleContext {
    KW_FOR(): TerminalNode;
    KW_REPLICATION(): TerminalNode;
    LPAREN(): TerminalNode;
    StringLiteral(): TerminalNode;
    RPAREN(): TerminalNode;
    KW_METADATA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExportStatementContext extends ParserRuleContext {
    KW_EXPORT(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableOrPartition(): TableOrPartitionContext;
    KW_TO(): TerminalNode;
    StringLiteral(): TerminalNode;
    replicationClause(): ReplicationClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ImportStatementContext extends ParserRuleContext {
    _path: Token;
    KW_IMPORT(): TerminalNode;
    KW_FROM(): TerminalNode;
    StringLiteral(): TerminalNode;
    KW_TABLE(): TerminalNode | undefined;
    tableOrPartition(): TableOrPartitionContext | undefined;
    tableLocation(): TableLocationContext | undefined;
    KW_EXTERNAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ReplDumpStatementContext extends ParserRuleContext {
    _dbPolicy: ReplDbPolicyContext;
    _oldDbPolicy: ReplDbPolicyContext;
    _replConf: ReplConfigsContext;
    KW_REPL(): TerminalNode;
    KW_DUMP(): TerminalNode;
    replDbPolicy(): ReplDbPolicyContext[];
    replDbPolicy(i: number): ReplDbPolicyContext;
    KW_REPLACE(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    replConfigs(): ReplConfigsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ReplDbPolicyContext extends ParserRuleContext {
    _dbName: DbSchemaNameContext;
    _tablePolicy: ReplTableLevelPolicyContext;
    dbSchemaName(): DbSchemaNameContext;
    DOT(): TerminalNode | undefined;
    replTableLevelPolicy(): ReplTableLevelPolicyContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ReplLoadStatementContext extends ParserRuleContext {
    _sourceDbPolicy: ReplDbPolicyContext;
    _dbName: DbSchemaNameContext;
    _replConf: ReplConfigsContext;
    KW_REPL(): TerminalNode;
    KW_LOAD(): TerminalNode;
    replDbPolicy(): ReplDbPolicyContext;
    KW_INTO(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    dbSchemaName(): DbSchemaNameContext | undefined;
    replConfigs(): ReplConfigsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ReplConfigsContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    replConfigsList(): ReplConfigsListContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ReplConfigsListContext extends ParserRuleContext {
    keyValueProperty(): KeyValuePropertyContext[];
    keyValueProperty(i: number): KeyValuePropertyContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ReplTableLevelPolicyContext extends ParserRuleContext {
    _replTablesIncludeList: Token;
    _replTablesExcludeList: Token;
    StringLiteral(): TerminalNode[];
    StringLiteral(i: number): TerminalNode;
    DOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ReplStatusStatementContext extends ParserRuleContext {
    _dbName: DbSchemaNameContext;
    _replConf: ReplConfigsContext;
    KW_REPL(): TerminalNode;
    KW_STATUS(): TerminalNode;
    dbSchemaName(): DbSchemaNameContext;
    KW_WITH(): TerminalNode | undefined;
    replConfigs(): ReplConfigsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DdlStatementContext extends ParserRuleContext {
    createDatabaseStatement(): CreateDatabaseStatementContext | undefined;
    switchDatabaseStatement(): SwitchDatabaseStatementContext | undefined;
    dropDatabaseStatement(): DropDatabaseStatementContext | undefined;
    createTableStatement(): CreateTableStatementContext | undefined;
    dropTableStatement(): DropTableStatementContext | undefined;
    truncateTableStatement(): TruncateTableStatementContext | undefined;
    alterStatement(): AlterStatementContext | undefined;
    descStatement(): DescStatementContext | undefined;
    showStatement(): ShowStatementContext | undefined;
    metastoreCheck(): MetastoreCheckContext | undefined;
    createViewStatement(): CreateViewStatementContext | undefined;
    createMaterializedViewStatement(): CreateMaterializedViewStatementContext | undefined;
    createScheduledQueryStatement(): CreateScheduledQueryStatementContext | undefined;
    alterScheduledQueryStatement(): AlterScheduledQueryStatementContext | undefined;
    dropScheduledQueryStatement(): DropScheduledQueryStatementContext | undefined;
    dropViewStatement(): DropViewStatementContext | undefined;
    dropMaterializedViewStatement(): DropMaterializedViewStatementContext | undefined;
    createFunctionStatement(): CreateFunctionStatementContext | undefined;
    createMacroStatement(): CreateMacroStatementContext | undefined;
    dropFunctionStatement(): DropFunctionStatementContext | undefined;
    reloadFunctionsStatement(): ReloadFunctionsStatementContext | undefined;
    dropMacroStatement(): DropMacroStatementContext | undefined;
    createIndexStatement(): CreateIndexStatementContext | undefined;
    dropIndexStatement(): DropIndexStatementContext | undefined;
    analyzeStatement(): AnalyzeStatementContext | undefined;
    lockStatement(): LockStatementContext | undefined;
    unlockStatement(): UnlockStatementContext | undefined;
    lockDatabase(): LockDatabaseContext | undefined;
    unlockDatabase(): UnlockDatabaseContext | undefined;
    createRoleStatement(): CreateRoleStatementContext | undefined;
    dropRoleStatement(): DropRoleStatementContext | undefined;
    grantPrivileges(): GrantPrivilegesContext | undefined;
    revokePrivileges(): RevokePrivilegesContext | undefined;
    showGrants(): ShowGrantsContext | undefined;
    showRoleGrants(): ShowRoleGrantsContext | undefined;
    showRolePrincipals(): ShowRolePrincipalsContext | undefined;
    showRoles(): ShowRolesContext | undefined;
    grantRole(): GrantRoleContext | undefined;
    revokeRole(): RevokeRoleContext | undefined;
    setRole(): SetRoleContext | undefined;
    showCurrentRole(): ShowCurrentRoleContext | undefined;
    abortTransactionStatement(): AbortTransactionStatementContext | undefined;
    abortCompactionStatement(): AbortCompactionStatementContext | undefined;
    killQueryStatement(): KillQueryStatementContext | undefined;
    resourcePlanDdlStatements(): ResourcePlanDdlStatementsContext | undefined;
    createDataConnectorStatement(): CreateDataConnectorStatementContext | undefined;
    dropDataConnectorStatement(): DropDataConnectorStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class IfExistsContext extends ParserRuleContext {
    KW_IF(): TerminalNode;
    KW_EXISTS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RestrictOrCascadeContext extends ParserRuleContext {
    KW_RESTRICT(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class IfNotExistsContext extends ParserRuleContext {
    KW_IF(): TerminalNode;
    KW_NOT(): TerminalNode;
    KW_EXISTS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ForceContext extends ParserRuleContext {
    KW_FORCE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RewriteEnabledContext extends ParserRuleContext {
    enable(): EnableContext;
    KW_REWRITE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RewriteDisabledContext extends ParserRuleContext {
    disable(): DisableContext;
    KW_REWRITE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class StoredAsDirsContext extends ParserRuleContext {
    KW_STORED(): TerminalNode;
    KW_AS(): TerminalNode;
    KW_DIRECTORIES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class OrReplaceContext extends ParserRuleContext {
    KW_OR(): TerminalNode;
    KW_REPLACE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateDatabaseStatementContext extends ParserRuleContext {
    _name: DbSchemaNameCreateContext;
    _dbprops: DbPropertiesContext;
    KW_CREATE(): TerminalNode;
    db_schema(): Db_schemaContext;
    dbSchemaNameCreate(): DbSchemaNameCreateContext;
    KW_REMOTE(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    databaseComment(): DatabaseCommentContext | undefined;
    dbLocation(): DbLocationContext | undefined;
    dbManagedLocation(): DbManagedLocationContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_DBPROPERTIES(): TerminalNode | undefined;
    dbProperties(): DbPropertiesContext | undefined;
    dbConnectorName(): DbConnectorNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DbLocationContext extends ParserRuleContext {
    _locn: Token;
    KW_LOCATION(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DbManagedLocationContext extends ParserRuleContext {
    _locn: Token;
    KW_MANAGEDLOCATION(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DbPropertiesContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    dbPropertiesList(): DbPropertiesListContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DbPropertiesListContext extends ParserRuleContext {
    keyValueProperty(): KeyValuePropertyContext[];
    keyValueProperty(i: number): KeyValuePropertyContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DbConnectorNameContext extends ParserRuleContext {
    _dcName: DbSchemaNameContext;
    KW_USING(): TerminalNode;
    dbSchemaName(): DbSchemaNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SwitchDatabaseStatementContext extends ParserRuleContext {
    KW_USE(): TerminalNode;
    dbSchemaName(): DbSchemaNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropDatabaseStatementContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    db_schema(): Db_schemaContext;
    dbSchemaName(): DbSchemaNameContext;
    ifExists(): IfExistsContext | undefined;
    restrictOrCascade(): RestrictOrCascadeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DatabaseCommentContext extends ParserRuleContext {
    _comment: Token;
    KW_COMMENT(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TruncateTableStatementContext extends ParserRuleContext {
    KW_TRUNCATE(): TerminalNode;
    tablePartitionPrefix(): TablePartitionPrefixContext;
    KW_TABLE(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    columnNameList(): ColumnNameListContext | undefined;
    RPAREN(): TerminalNode | undefined;
    force(): ForceContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropTableStatementContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    ifExists(): IfExistsContext | undefined;
    KW_PURGE(): TerminalNode | undefined;
    replicationClause(): ReplicationClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class InputFileFormatContext extends ParserRuleContext {
    _inFmt: Token;
    _serdeCls: Token;
    KW_INPUTFORMAT(): TerminalNode;
    KW_SERDE(): TerminalNode;
    StringLiteral(): TerminalNode[];
    StringLiteral(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TabTypeExprContext extends ParserRuleContext {
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    KW_ELEM_TYPE(): TerminalNode[];
    KW_ELEM_TYPE(i: number): TerminalNode;
    KW_KEY_TYPE(): TerminalNode[];
    KW_KEY_TYPE(i: number): TerminalNode;
    KW_VALUE_TYPE(): TerminalNode[];
    KW_VALUE_TYPE(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartTypeExprContext extends ParserRuleContext {
    tabTypeExpr(): TabTypeExprContext;
    partitionSpec(): PartitionSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TabPartColTypeExprContext extends ParserRuleContext {
    tableOrView(): TableOrViewContext;
    partitionSpec(): PartitionSpecContext | undefined;
    extColumnName(): ExtColumnNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DescStatementContext extends ParserRuleContext {
    _dbName: DbSchemaNameContext;
    _dcName: DbSchemaNameContext;
    _name: FunctionNameForDDLContext;
    _descOptions: Token;
    _parttype: TabPartColTypeExprContext;
    KW_DESCRIBE(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    db_schema(): Db_schemaContext | undefined;
    KW_DATACONNECTOR(): TerminalNode | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    dbSchemaName(): DbSchemaNameContext | undefined;
    functionNameForDDL(): FunctionNameForDDLContext | undefined;
    tabPartColTypeExpr(): TabPartColTypeExprContext | undefined;
    KW_EXTENDED(): TerminalNode | undefined;
    KW_FORMATTED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AnalyzeStatementContext extends ParserRuleContext {
    _parttype: TableOrPartitionContext;
    _noscan: Token;
    _statsColumnName: ColumnNameListContext;
    KW_ANALYZE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableOrPartition(): TableOrPartitionContext;
    KW_COMPUTE(): TerminalNode | undefined;
    KW_STATISTICS(): TerminalNode | undefined;
    KW_CACHE(): TerminalNode | undefined;
    KW_METADATA(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    KW_NOSCAN(): TerminalNode | undefined;
    columnNameList(): ColumnNameListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class From_inContext extends ParserRuleContext {
    KW_FROM(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Db_schemaContext extends ParserRuleContext {
    KW_DATABASE(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ShowStatementContext extends ParserRuleContext {
    _isExtended: Token;
    _db_name: DbSchemaNameContext;
    _filter: ShowTablesFilterExprContext;
    _tabOrViewName: TableOrViewContext;
    _tabName: TableNameContext;
    _prptyName: Token;
    _dbName: DbSchemaNameContext;
    _parttype: PartTypeExprContext;
    _rp_name: Id_Context;
    KW_SHOW(): TerminalNode;
    KW_DATABASES(): TerminalNode | undefined;
    KW_SCHEMAS(): TerminalNode | undefined;
    KW_LIKE(): TerminalNode | undefined;
    showStmtIdentifier(): ShowStmtIdentifierContext | undefined;
    KW_TABLES(): TerminalNode | undefined;
    from_in(): From_inContext[];
    from_in(i: number): From_inContext;
    KW_EXTENDED(): TerminalNode | undefined;
    dbSchemaName(): DbSchemaNameContext | undefined;
    showTablesFilterExpr(): ShowTablesFilterExprContext | undefined;
    KW_VIEWS(): TerminalNode | undefined;
    KW_MATERIALIZED(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    tableOrView(): TableOrViewContext | undefined;
    KW_SORTED(): TerminalNode | undefined;
    KW_FUNCTIONS(): TerminalNode | undefined;
    functionNameForDDL(): FunctionNameForDDLContext | undefined;
    KW_PARTITIONS(): TerminalNode | undefined;
    partitionSpec(): PartitionSpecContext | undefined;
    whereClause(): WhereClauseContext | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    KW_CREATE(): TerminalNode | undefined;
    db_schema(): Db_schemaContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    tableName(): TableNameContext | undefined;
    KW_TBLPROPERTIES(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    KW_LOCKS(): TerminalNode | undefined;
    partTypeExpr(): PartTypeExprContext | undefined;
    KW_COMPACTIONS(): TerminalNode | undefined;
    compactionId(): CompactionIdContext | undefined;
    compactionPool(): CompactionPoolContext | undefined;
    compactionType(): CompactionTypeContext | undefined;
    compactionStatus(): CompactionStatusContext | undefined;
    KW_TRANSACTIONS(): TerminalNode | undefined;
    KW_CONF(): TerminalNode | undefined;
    KW_RESOURCE(): TerminalNode | undefined;
    KW_PLAN(): TerminalNode | undefined;
    KW_PLANS(): TerminalNode | undefined;
    id_(): Id_Context | undefined;
    KW_DATACONNECTORS(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    KW_INDEXES(): TerminalNode | undefined;
    KW_FORMATTED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ShowTablesFilterExprContext extends ParserRuleContext {
    KW_WHERE(): TerminalNode | undefined;
    id_(): Id_Context | undefined;
    EQUAL(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    KW_LIKE(): TerminalNode | undefined;
    showStmtIdentifier(): ShowStmtIdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class LockStatementContext extends ParserRuleContext {
    KW_LOCK(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    lockMode(): LockModeContext;
    partitionSpec(): PartitionSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class LockDatabaseContext extends ParserRuleContext {
    _dbName: DbSchemaNameContext;
    KW_LOCK(): TerminalNode;
    db_schema(): Db_schemaContext;
    lockMode(): LockModeContext;
    dbSchemaName(): DbSchemaNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class LockModeContext extends ParserRuleContext {
    KW_SHARED(): TerminalNode | undefined;
    KW_EXCLUSIVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class UnlockStatementContext extends ParserRuleContext {
    KW_UNLOCK(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    partitionSpec(): PartitionSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class UnlockDatabaseContext extends ParserRuleContext {
    _dbName: DbSchemaNameContext;
    KW_UNLOCK(): TerminalNode;
    db_schema(): Db_schemaContext;
    dbSchemaName(): DbSchemaNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateRoleStatementContext extends ParserRuleContext {
    _roleName: Id_Context;
    KW_CREATE(): TerminalNode;
    KW_ROLE(): TerminalNode;
    id_(): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropRoleStatementContext extends ParserRuleContext {
    _roleName: Id_Context;
    KW_DROP(): TerminalNode;
    KW_ROLE(): TerminalNode;
    id_(): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class GrantPrivilegesContext extends ParserRuleContext {
    _privList: PrivilegeListContext;
    KW_GRANT(): TerminalNode;
    KW_TO(): TerminalNode;
    principalSpecification(): PrincipalSpecificationContext;
    privilegeList(): PrivilegeListContext;
    privilegeObject(): PrivilegeObjectContext | undefined;
    withGrantOption(): WithGrantOptionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RevokePrivilegesContext extends ParserRuleContext {
    KW_REVOKE(): TerminalNode;
    privilegeList(): PrivilegeListContext;
    KW_FROM(): TerminalNode;
    principalSpecification(): PrincipalSpecificationContext;
    grantOptionFor(): GrantOptionForContext | undefined;
    privilegeObject(): PrivilegeObjectContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class GrantRoleContext extends ParserRuleContext {
    KW_GRANT(): TerminalNode;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    KW_TO(): TerminalNode;
    principalSpecification(): PrincipalSpecificationContext;
    KW_ROLE(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    withAdminOption(): WithAdminOptionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RevokeRoleContext extends ParserRuleContext {
    KW_REVOKE(): TerminalNode;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    KW_FROM(): TerminalNode;
    principalSpecification(): PrincipalSpecificationContext;
    adminOptionFor(): AdminOptionForContext | undefined;
    KW_ROLE(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ShowRoleGrantsContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_ROLE(): TerminalNode;
    KW_GRANT(): TerminalNode;
    principalName(): PrincipalNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ShowRolesContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_ROLES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ShowCurrentRoleContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_CURRENT(): TerminalNode;
    KW_ROLES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SetRoleContext extends ParserRuleContext {
    _all: Token;
    _none: Token;
    KW_SET(): TerminalNode;
    KW_ROLE(): TerminalNode;
    id_(): Id_Context | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_NONE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ShowGrantsContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_GRANT(): TerminalNode;
    principalName(): PrincipalNameContext | undefined;
    KW_ON(): TerminalNode | undefined;
    privilegeIncludeColObject(): PrivilegeIncludeColObjectContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ShowRolePrincipalsContext extends ParserRuleContext {
    _roleName: Id_Context;
    KW_SHOW(): TerminalNode;
    KW_PRINCIPALS(): TerminalNode;
    id_(): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrivilegeIncludeColObjectContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    privObjectCols(): PrivObjectColsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrivilegeObjectContext extends ParserRuleContext {
    KW_ON(): TerminalNode;
    privObject(): PrivObjectContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrivObjectContext extends ParserRuleContext {
    _path: Token;
    db_schema(): Db_schemaContext | undefined;
    dbSchemaName(): DbSchemaNameContext | undefined;
    tableName(): TableNameContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    partitionSpec(): PartitionSpecContext | undefined;
    KW_URI(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    KW_SERVER(): TerminalNode | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrivObjectColsContext extends ParserRuleContext {
    _cols: ColumnNameListContext;
    _path: Token;
    db_schema(): Db_schemaContext | undefined;
    dbSchemaName(): DbSchemaNameContext | undefined;
    tableName(): TableNameContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    partitionSpec(): PartitionSpecContext | undefined;
    columnNameList(): ColumnNameListContext | undefined;
    KW_URI(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    KW_SERVER(): TerminalNode | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrivilegeListContext extends ParserRuleContext {
    privlegeDef(): PrivlegeDefContext[];
    privlegeDef(i: number): PrivlegeDefContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrivlegeDefContext extends ParserRuleContext {
    _cols: ColumnNameListContext;
    privilegeType(): PrivilegeTypeContext;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    columnNameList(): ColumnNameListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrivilegeTypeContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    KW_ALTER(): TerminalNode | undefined;
    KW_UPDATE(): TerminalNode | undefined;
    KW_CREATE(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    KW_LOCK(): TerminalNode | undefined;
    KW_SELECT(): TerminalNode | undefined;
    KW_SHOW_DATABASE(): TerminalNode | undefined;
    KW_INSERT(): TerminalNode | undefined;
    KW_DELETE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrincipalSpecificationContext extends ParserRuleContext {
    principalName(): PrincipalNameContext[];
    principalName(i: number): PrincipalNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrincipalNameContext extends ParserRuleContext {
    KW_USER(): TerminalNode | undefined;
    principalIdentifier(): PrincipalIdentifierContext | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrincipalAlterNameContext extends ParserRuleContext {
    KW_USER(): TerminalNode | undefined;
    principalIdentifier(): PrincipalIdentifierContext | undefined;
    KW_ROLE(): TerminalNode | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class WithGrantOptionContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    KW_GRANT(): TerminalNode;
    KW_OPTION(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class GrantOptionForContext extends ParserRuleContext {
    KW_GRANT(): TerminalNode;
    KW_OPTION(): TerminalNode;
    KW_FOR(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AdminOptionForContext extends ParserRuleContext {
    KW_ADMIN(): TerminalNode;
    KW_OPTION(): TerminalNode;
    KW_FOR(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class WithAdminOptionContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    KW_ADMIN(): TerminalNode;
    KW_OPTION(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class MetastoreCheckContext extends ParserRuleContext {
    _repair: Token;
    _opt: Token;
    _parts: Token;
    KW_MSCK(): TerminalNode;
    KW_TABLE(): TerminalNode | undefined;
    tableName(): TableNameContext | undefined;
    KW_REPAIR(): TerminalNode | undefined;
    KW_PARTITIONS(): TerminalNode | undefined;
    KW_ADD(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_SYNC(): TerminalNode | undefined;
    partitionSelectorSpec(): PartitionSelectorSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ResourceListContext extends ParserRuleContext {
    resource(): ResourceContext[];
    resource(i: number): ResourceContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ResourceContext extends ParserRuleContext {
    _resType: ResourceTypeContext;
    _resPath: Token;
    resourceType(): ResourceTypeContext;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ResourceTypeContext extends ParserRuleContext {
    KW_JAR(): TerminalNode | undefined;
    KW_FILE(): TerminalNode | undefined;
    KW_ARCHIVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateFunctionStatementContext extends ParserRuleContext {
    _temp: Token;
    _rList: ResourceListContext;
    KW_CREATE(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    functionNameCreate(): FunctionNameCreateContext;
    KW_AS(): TerminalNode;
    StringLiteral(): TerminalNode;
    KW_USING(): TerminalNode | undefined;
    KW_TEMPORARY(): TerminalNode | undefined;
    resourceList(): ResourceListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropFunctionStatementContext extends ParserRuleContext {
    _temp: Token;
    KW_DROP(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    functionNameForDDL(): FunctionNameForDDLContext;
    ifExists(): IfExistsContext | undefined;
    KW_TEMPORARY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ReloadFunctionsStatementContext extends ParserRuleContext {
    KW_RELOAD(): TerminalNode;
    KW_FUNCTIONS(): TerminalNode | undefined;
    KW_FUNCTION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateMacroStatementContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_TEMPORARY(): TerminalNode;
    KW_MACRO(): TerminalNode;
    Identifier(): TerminalNode;
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    expression(): ExpressionContext;
    columnNameTypeList(): ColumnNameTypeListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropMacroStatementContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_TEMPORARY(): TerminalNode;
    KW_MACRO(): TerminalNode;
    Identifier(): TerminalNode;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateIndexStatementContext extends ParserRuleContext {
    _indextype: Token;
    _locn: Token;
    KW_CREATE(): TerminalNode;
    KW_INDEX(): TerminalNode;
    id_(): Id_Context;
    KW_ON(): TerminalNode;
    KW_TABLE(): TerminalNode[];
    KW_TABLE(i: number): TerminalNode;
    tableName(): TableNameContext[];
    tableName(i: number): TableNameContext;
    columnParenthesesList(): ColumnParenthesesListContext[];
    columnParenthesesList(i: number): ColumnParenthesesListContext;
    KW_AS(): TerminalNode;
    StringLiteral(): TerminalNode[];
    StringLiteral(i: number): TerminalNode;
    KW_WITH(): TerminalNode | undefined;
    KW_DEFERRED(): TerminalNode | undefined;
    KW_REBUILD(): TerminalNode | undefined;
    KW_IDXPROPERTIES(): TerminalNode | undefined;
    tableProperties(): TablePropertiesContext | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_PARTITIONED(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    tableFileFormat(): TableFileFormatContext | undefined;
    KW_LOCATION(): TerminalNode | undefined;
    tablePropertiesPrefixed(): TablePropertiesPrefixedContext | undefined;
    tableComment(): TableCommentContext | undefined;
    tableRowFormat(): TableRowFormatContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropIndexStatementContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_INDEX(): TerminalNode;
    id_(): Id_Context;
    KW_ON(): TerminalNode;
    tableName(): TableNameContext;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateViewStatementContext extends ParserRuleContext {
    _name: ViewNameCreateContext;
    KW_CREATE(): TerminalNode;
    KW_VIEW(): TerminalNode;
    KW_AS(): TerminalNode;
    selectStatementWithCTE(): SelectStatementWithCTEContext;
    viewNameCreate(): ViewNameCreateContext;
    orReplace(): OrReplaceContext | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    LPAREN(): TerminalNode | undefined;
    columnNameCommentList(): ColumnNameCommentListContext | undefined;
    RPAREN(): TerminalNode | undefined;
    tableComment(): TableCommentContext | undefined;
    viewPartition(): ViewPartitionContext | undefined;
    tablePropertiesPrefixed(): TablePropertiesPrefixedContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ViewPartitionContext extends ParserRuleContext {
    _spec: PartitionTransformSpecContext;
    KW_PARTITIONED(): TerminalNode;
    KW_ON(): TerminalNode;
    RPAREN(): TerminalNode;
    LPAREN(): TerminalNode | undefined;
    columnNameList(): ColumnNameListContext | undefined;
    KW_SPEC(): TerminalNode | undefined;
    partitionTransformSpec(): PartitionTransformSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ViewOrganizationContext extends ParserRuleContext {
    viewClusterSpec(): ViewClusterSpecContext | undefined;
    viewComplexSpec(): ViewComplexSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ViewClusterSpecContext extends ParserRuleContext {
    KW_CLUSTERED(): TerminalNode;
    KW_ON(): TerminalNode;
    LPAREN(): TerminalNode;
    columnNameList(): ColumnNameListContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ViewComplexSpecContext extends ParserRuleContext {
    viewDistSpec(): ViewDistSpecContext;
    viewSortSpec(): ViewSortSpecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ViewDistSpecContext extends ParserRuleContext {
    _colList: ColumnNameListContext;
    KW_DISTRIBUTED(): TerminalNode;
    KW_ON(): TerminalNode;
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    columnNameList(): ColumnNameListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ViewSortSpecContext extends ParserRuleContext {
    _colList: ColumnNameListContext;
    KW_SORTED(): TerminalNode;
    KW_ON(): TerminalNode;
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    columnNameList(): ColumnNameListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropViewStatementContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewName(): ViewNameContext;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateMaterializedViewStatementContext extends ParserRuleContext {
    _name: ViewNameCreateContext;
    KW_CREATE(): TerminalNode;
    KW_MATERIALIZED(): TerminalNode;
    KW_VIEW(): TerminalNode;
    KW_AS(): TerminalNode;
    selectStatementWithCTE(): SelectStatementWithCTEContext;
    viewNameCreate(): ViewNameCreateContext;
    ifNotExists(): IfNotExistsContext | undefined;
    rewriteDisabled(): RewriteDisabledContext | undefined;
    tableComment(): TableCommentContext | undefined;
    viewPartition(): ViewPartitionContext | undefined;
    viewOrganization(): ViewOrganizationContext | undefined;
    tableRowFormat(): TableRowFormatContext | undefined;
    tableFileFormat(): TableFileFormatContext | undefined;
    tableLocation(): TableLocationContext | undefined;
    tablePropertiesPrefixed(): TablePropertiesPrefixedContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropMaterializedViewStatementContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_MATERIALIZED(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewName(): ViewNameContext;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateScheduledQueryStatementContext extends ParserRuleContext {
    _name: Id_Context;
    KW_CREATE(): TerminalNode;
    KW_SCHEDULED(): TerminalNode;
    KW_QUERY(): TerminalNode;
    scheduleSpec(): ScheduleSpecContext;
    definedAsSpec(): DefinedAsSpecContext;
    id_(): Id_Context;
    executedAsSpec(): ExecutedAsSpecContext | undefined;
    enableSpecification(): EnableSpecificationContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropScheduledQueryStatementContext extends ParserRuleContext {
    _name: Id_Context;
    KW_DROP(): TerminalNode;
    KW_SCHEDULED(): TerminalNode;
    KW_QUERY(): TerminalNode;
    id_(): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterScheduledQueryStatementContext extends ParserRuleContext {
    _name: Id_Context;
    _mod: AlterScheduledQueryChangeContext;
    KW_ALTER(): TerminalNode;
    KW_SCHEDULED(): TerminalNode;
    KW_QUERY(): TerminalNode;
    id_(): Id_Context;
    alterScheduledQueryChange(): AlterScheduledQueryChangeContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterScheduledQueryChangeContext extends ParserRuleContext {
    scheduleSpec(): ScheduleSpecContext | undefined;
    executedAsSpec(): ExecutedAsSpecContext | undefined;
    enableSpecification(): EnableSpecificationContext | undefined;
    definedAsSpec(): DefinedAsSpecContext | undefined;
    KW_EXECUTE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ScheduleSpecContext extends ParserRuleContext {
    _cronString: Token;
    _value: Token;
    _qualifier: IntervalQualifiersContext;
    _offsetTs: Token;
    KW_CRON(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    KW_EVERY(): TerminalNode | undefined;
    intervalQualifiers(): IntervalQualifiersContext | undefined;
    Number(): TerminalNode | undefined;
    KW_AT(): TerminalNode | undefined;
    KW_OFFSET(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExecutedAsSpecContext extends ParserRuleContext {
    _executedAs: Token;
    KW_EXECUTED(): TerminalNode;
    KW_AS(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DefinedAsSpecContext extends ParserRuleContext {
    KW_AS(): TerminalNode;
    statement(): StatementContext;
    KW_DEFINED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ShowStmtIdentifierContext extends ParserRuleContext {
    id_(): Id_Context | undefined;
    StringLiteral(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableCommentContext extends ParserRuleContext {
    _comment: Token;
    KW_COMMENT(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateTablePartitionSpecContext extends ParserRuleContext {
    _opt1: CreateTablePartitionColumnTypeSpecContext;
    _opt2: CreateTablePartitionColumnSpecContext;
    _spec: PartitionTransformSpecContext;
    KW_PARTITIONED(): TerminalNode;
    KW_BY(): TerminalNode;
    RPAREN(): TerminalNode;
    LPAREN(): TerminalNode | undefined;
    KW_SPEC(): TerminalNode | undefined;
    partitionTransformSpec(): PartitionTransformSpecContext | undefined;
    createTablePartitionColumnTypeSpec(): CreateTablePartitionColumnTypeSpecContext | undefined;
    createTablePartitionColumnSpec(): CreateTablePartitionColumnSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateTablePartitionColumnTypeSpecContext extends ParserRuleContext {
    columnNameTypeConstraint(): ColumnNameTypeConstraintContext[];
    columnNameTypeConstraint(i: number): ColumnNameTypeConstraintContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateTablePartitionColumnSpecContext extends ParserRuleContext {
    columnName(): ColumnNameContext[];
    columnName(i: number): ColumnNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartitionTransformSpecContext extends ParserRuleContext {
    columnNameTransformConstraint(): ColumnNameTransformConstraintContext[];
    columnNameTransformConstraint(i: number): ColumnNameTransformConstraintContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameTransformConstraintContext extends ParserRuleContext {
    partitionTransformType(): PartitionTransformTypeContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartitionTransformTypeContext extends ParserRuleContext {
    _value: Token;
    columnName(): ColumnNameContext;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    year(): YearContext | undefined;
    month(): MonthContext | undefined;
    day(): DayContext | undefined;
    hour(): HourContext | undefined;
    COMMA(): TerminalNode | undefined;
    KW_TRUNCATE(): TerminalNode | undefined;
    KW_BUCKET(): TerminalNode | undefined;
    Number(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableBucketsContext extends ParserRuleContext {
    _bucketCols: ColumnNameListContext;
    _sortCols: ColumnNameOrderListContext;
    _num: Token;
    KW_CLUSTERED(): TerminalNode;
    KW_BY(): TerminalNode[];
    KW_BY(i: number): TerminalNode;
    LPAREN(): TerminalNode[];
    LPAREN(i: number): TerminalNode;
    RPAREN(): TerminalNode[];
    RPAREN(i: number): TerminalNode;
    KW_INTO(): TerminalNode;
    KW_BUCKETS(): TerminalNode;
    columnNameList(): ColumnNameListContext;
    Number(): TerminalNode;
    KW_SORTED(): TerminalNode | undefined;
    columnNameOrderList(): ColumnNameOrderListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableImplBucketsContext extends ParserRuleContext {
    _num: Token;
    KW_CLUSTERED(): TerminalNode;
    KW_INTO(): TerminalNode;
    KW_BUCKETS(): TerminalNode;
    Number(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableSkewedContext extends ParserRuleContext {
    _skewedCols: ColumnNameListContext;
    _skewedValues: SkewedValueElementContext;
    KW_SKEWED(): TerminalNode;
    KW_BY(): TerminalNode;
    LPAREN(): TerminalNode[];
    LPAREN(i: number): TerminalNode;
    RPAREN(): TerminalNode[];
    RPAREN(i: number): TerminalNode;
    KW_ON(): TerminalNode;
    columnNameList(): ColumnNameListContext;
    skewedValueElement(): SkewedValueElementContext;
    storedAsDirs(): StoredAsDirsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RowFormatContext extends ParserRuleContext {
    rowFormatSerde(): RowFormatSerdeContext | undefined;
    rowFormatDelimited(): RowFormatDelimitedContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RecordReaderContext extends ParserRuleContext {
    KW_RECORDREADER(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RecordWriterContext extends ParserRuleContext {
    KW_RECORDWRITER(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RowFormatSerdeContext extends ParserRuleContext {
    _name: Token;
    _serdeprops: TablePropertiesContext;
    KW_ROW(): TerminalNode;
    KW_FORMAT(): TerminalNode;
    KW_SERDE(): TerminalNode;
    StringLiteral(): TerminalNode;
    KW_WITH(): TerminalNode | undefined;
    KW_SERDEPROPERTIES(): TerminalNode | undefined;
    tableProperties(): TablePropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RowFormatDelimitedContext extends ParserRuleContext {
    KW_ROW(): TerminalNode;
    KW_FORMAT(): TerminalNode;
    KW_DELIMITED(): TerminalNode;
    tableRowFormatFieldIdentifier(): TableRowFormatFieldIdentifierContext | undefined;
    tableRowFormatCollItemsIdentifier(): TableRowFormatCollItemsIdentifierContext | undefined;
    tableRowFormatMapKeysIdentifier(): TableRowFormatMapKeysIdentifierContext | undefined;
    tableRowFormatLinesIdentifier(): TableRowFormatLinesIdentifierContext | undefined;
    tableRowNullFormat(): TableRowNullFormatContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableRowFormatContext extends ParserRuleContext {
    rowFormatDelimited(): RowFormatDelimitedContext | undefined;
    rowFormatSerde(): RowFormatSerdeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TablePropertiesPrefixedContext extends ParserRuleContext {
    KW_TBLPROPERTIES(): TerminalNode;
    tableProperties(): TablePropertiesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TablePropertiesContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    tablePropertiesList(): TablePropertiesListContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TablePropertiesListContext extends ParserRuleContext {
    keyValueProperty(): KeyValuePropertyContext[];
    keyValueProperty(i: number): KeyValuePropertyContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    keyProperty(): KeyPropertyContext[];
    keyProperty(i: number): KeyPropertyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class KeyValuePropertyContext extends ParserRuleContext {
    _key: Token;
    _value: Token;
    EQUAL(): TerminalNode;
    StringLiteral(): TerminalNode[];
    StringLiteral(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class KeyPropertyContext extends ParserRuleContext {
    _key: Token;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableRowFormatFieldIdentifierContext extends ParserRuleContext {
    _fldIdnt: Token;
    _fldEscape: Token;
    KW_FIELDS(): TerminalNode;
    KW_TERMINATED(): TerminalNode;
    KW_BY(): TerminalNode[];
    KW_BY(i: number): TerminalNode;
    StringLiteral(): TerminalNode[];
    StringLiteral(i: number): TerminalNode;
    KW_ESCAPED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableRowFormatCollItemsIdentifierContext extends ParserRuleContext {
    _collIdnt: Token;
    KW_COLLECTION(): TerminalNode;
    KW_ITEMS(): TerminalNode;
    KW_TERMINATED(): TerminalNode;
    KW_BY(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableRowFormatMapKeysIdentifierContext extends ParserRuleContext {
    _mapKeysIdnt: Token;
    KW_MAP(): TerminalNode;
    KW_KEYS(): TerminalNode;
    KW_TERMINATED(): TerminalNode;
    KW_BY(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableRowFormatLinesIdentifierContext extends ParserRuleContext {
    _linesIdnt: Token;
    KW_LINES(): TerminalNode;
    KW_TERMINATED(): TerminalNode;
    KW_BY(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableRowNullFormatContext extends ParserRuleContext {
    _nullIdnt: Token;
    KW_NULL(): TerminalNode;
    KW_DEFINED(): TerminalNode;
    KW_AS(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableFileFormatContext extends ParserRuleContext {
    _inFmt: Token;
    _outFmt: Token;
    _inDriver: Token;
    _outDriver: Token;
    _storageHandler: Token;
    _serdeprops: TablePropertiesContext;
    _fileformat: Id_Context;
    _genericSpec: Id_Context;
    KW_STORED(): TerminalNode[];
    KW_STORED(i: number): TerminalNode;
    KW_AS(): TerminalNode | undefined;
    KW_INPUTFORMAT(): TerminalNode | undefined;
    KW_OUTPUTFORMAT(): TerminalNode | undefined;
    StringLiteral(): TerminalNode[];
    StringLiteral(i: number): TerminalNode;
    KW_INPUTDRIVER(): TerminalNode | undefined;
    KW_OUTPUTDRIVER(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_SERDEPROPERTIES(): TerminalNode | undefined;
    tableProperties(): TablePropertiesContext | undefined;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableLocationContext extends ParserRuleContext {
    _locn: Token;
    KW_LOCATION(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameTypeListContext extends ParserRuleContext {
    columnNameType(): ColumnNameTypeContext[];
    columnNameType(i: number): ColumnNameTypeContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameTypeOrConstraintListContext extends ParserRuleContext {
    columnNameTypeOrConstraint(): ColumnNameTypeOrConstraintContext[];
    columnNameTypeOrConstraint(i: number): ColumnNameTypeOrConstraintContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameColonTypeListContext extends ParserRuleContext {
    columnNameColonType(): ColumnNameColonTypeContext[];
    columnNameColonType(i: number): ColumnNameColonTypeContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameListContext extends ParserRuleContext {
    columnName(): ColumnNameContext[];
    columnName(i: number): ColumnNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameContext extends ParserRuleContext {
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameCreateContext extends ParserRuleContext {
    id_(): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExtColumnNameContext extends ParserRuleContext {
    columnName(): ColumnNameContext;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    KW_ELEM_TYPE(): TerminalNode[];
    KW_ELEM_TYPE(i: number): TerminalNode;
    KW_KEY_TYPE(): TerminalNode[];
    KW_KEY_TYPE(i: number): TerminalNode;
    KW_VALUE_TYPE(): TerminalNode[];
    KW_VALUE_TYPE(i: number): TerminalNode;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameOrderListContext extends ParserRuleContext {
    columnNameOrder(): ColumnNameOrderContext[];
    columnNameOrder(i: number): ColumnNameOrderContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnParenthesesListContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    columnNameList(): ColumnNameListContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class EnableValidateSpecificationContext extends ParserRuleContext {
    enableSpecification(): EnableSpecificationContext | undefined;
    validateSpecification(): ValidateSpecificationContext | undefined;
    enforcedSpecification(): EnforcedSpecificationContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class EnableSpecificationContext extends ParserRuleContext {
    enable(): EnableContext | undefined;
    disable(): DisableContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ValidateSpecificationContext extends ParserRuleContext {
    KW_VALIDATE(): TerminalNode | undefined;
    KW_NOVALIDATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class EnforcedSpecificationContext extends ParserRuleContext {
    KW_ENFORCED(): TerminalNode;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RelySpecificationContext extends ParserRuleContext {
    KW_RELY(): TerminalNode | undefined;
    KW_NORELY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateConstraintContext extends ParserRuleContext {
    _constraintName: Id_Context;
    tableLevelConstraint(): TableLevelConstraintContext;
    KW_CONSTRAINT(): TerminalNode | undefined;
    constraintOptsCreate(): ConstraintOptsCreateContext | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterConstraintWithNameContext extends ParserRuleContext {
    _constraintName: Id_Context;
    KW_CONSTRAINT(): TerminalNode;
    tableLevelConstraint(): TableLevelConstraintContext;
    id_(): Id_Context;
    constraintOptsAlter(): ConstraintOptsAlterContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableLevelConstraintContext extends ParserRuleContext {
    pkUkConstraint(): PkUkConstraintContext | undefined;
    checkConstraint(): CheckConstraintContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PkUkConstraintContext extends ParserRuleContext {
    _pkCols: ColumnParenthesesListContext;
    tableConstraintType(): TableConstraintTypeContext;
    columnParenthesesList(): ColumnParenthesesListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CheckConstraintContext extends ParserRuleContext {
    KW_CHECK(): TerminalNode;
    LPAREN(): TerminalNode;
    expression(): ExpressionContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateForeignKeyContext extends ParserRuleContext {
    _constraintName: Id_Context;
    _fkCols: ColumnParenthesesListContext;
    _tabName: TableNameContext;
    _parCols: ColumnParenthesesListContext;
    KW_FOREIGN(): TerminalNode;
    KW_KEY(): TerminalNode;
    KW_REFERENCES(): TerminalNode;
    columnParenthesesList(): ColumnParenthesesListContext[];
    columnParenthesesList(i: number): ColumnParenthesesListContext;
    tableName(): TableNameContext;
    KW_CONSTRAINT(): TerminalNode | undefined;
    constraintOptsCreate(): ConstraintOptsCreateContext | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterForeignKeyWithNameContext extends ParserRuleContext {
    _constraintName: Id_Context;
    _fkCols: ColumnParenthesesListContext;
    _tabName: TableNameContext;
    _parCols: ColumnParenthesesListContext;
    KW_CONSTRAINT(): TerminalNode;
    KW_FOREIGN(): TerminalNode;
    KW_KEY(): TerminalNode;
    KW_REFERENCES(): TerminalNode;
    id_(): Id_Context;
    columnParenthesesList(): ColumnParenthesesListContext[];
    columnParenthesesList(i: number): ColumnParenthesesListContext;
    tableName(): TableNameContext;
    constraintOptsAlter(): ConstraintOptsAlterContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SkewedValueElementContext extends ParserRuleContext {
    skewedColumnValues(): SkewedColumnValuesContext | undefined;
    skewedColumnValuePairList(): SkewedColumnValuePairListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SkewedColumnValuePairListContext extends ParserRuleContext {
    skewedColumnValuePair(): SkewedColumnValuePairContext[];
    skewedColumnValuePair(i: number): SkewedColumnValuePairContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SkewedColumnValuePairContext extends ParserRuleContext {
    _colValues: SkewedColumnValuesContext;
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    skewedColumnValues(): SkewedColumnValuesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SkewedColumnValuesContext extends ParserRuleContext {
    skewedColumnValue(): SkewedColumnValueContext[];
    skewedColumnValue(i: number): SkewedColumnValueContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SkewedColumnValueContext extends ParserRuleContext {
    constant(): ConstantContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SkewedValueLocationElementContext extends ParserRuleContext {
    skewedColumnValue(): SkewedColumnValueContext | undefined;
    skewedColumnValuePair(): SkewedColumnValuePairContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class OrderSpecificationContext extends ParserRuleContext {
    KW_ASC(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class NullOrderingContext extends ParserRuleContext {
    KW_NULLS(): TerminalNode;
    KW_FIRST(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameOrderContext extends ParserRuleContext {
    _orderSpec: OrderSpecificationContext;
    _nullSpec: NullOrderingContext;
    columnName(): ColumnNameContext;
    orderSpecification(): OrderSpecificationContext | undefined;
    nullOrdering(): NullOrderingContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameCommentListContext extends ParserRuleContext {
    columnNameComment(): ColumnNameCommentContext[];
    columnNameComment(i: number): ColumnNameCommentContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameCommentContext extends ParserRuleContext {
    _colName: ColumnNameCreateContext;
    _comment: Token;
    columnNameCreate(): ColumnNameCreateContext;
    KW_COMMENT(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class OrderSpecificationRewriteContext extends ParserRuleContext {
    KW_ASC(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnRefOrderContext extends ParserRuleContext {
    _orderSpec: OrderSpecificationRewriteContext;
    _nullSpec: NullOrderingContext;
    columnName(): ColumnNameContext | undefined;
    expression(): ExpressionContext | undefined;
    orderSpecificationRewrite(): OrderSpecificationRewriteContext | undefined;
    nullOrdering(): NullOrderingContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameTypeContext extends ParserRuleContext {
    _colName: ColumnNameCreateContext;
    _comment: Token;
    colType(): ColTypeContext;
    columnNameCreate(): ColumnNameCreateContext;
    KW_COMMENT(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameTypeOrConstraintContext extends ParserRuleContext {
    tableConstraint(): TableConstraintContext | undefined;
    columnNameTypeConstraint(): ColumnNameTypeConstraintContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableConstraintContext extends ParserRuleContext {
    createForeignKey(): CreateForeignKeyContext | undefined;
    createConstraint(): CreateConstraintContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameTypeConstraintContext extends ParserRuleContext {
    _colName: ColumnNameCreateContext;
    _comment: Token;
    colType(): ColTypeContext;
    columnNameCreate(): ColumnNameCreateContext;
    columnConstraint(): ColumnConstraintContext | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnConstraintContext extends ParserRuleContext {
    foreignKeyConstraint(): ForeignKeyConstraintContext | undefined;
    colConstraint(): ColConstraintContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ForeignKeyConstraintContext extends ParserRuleContext {
    _constraintName: Id_Context;
    _tabName: TableNameContext;
    _colName: ColumnNameContext;
    KW_REFERENCES(): TerminalNode;
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    tableName(): TableNameContext;
    columnName(): ColumnNameContext;
    KW_CONSTRAINT(): TerminalNode | undefined;
    constraintOptsCreate(): ConstraintOptsCreateContext | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColConstraintContext extends ParserRuleContext {
    _constraintName: Id_Context;
    columnConstraintType(): ColumnConstraintTypeContext;
    KW_CONSTRAINT(): TerminalNode | undefined;
    constraintOptsCreate(): ConstraintOptsCreateContext | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterColumnConstraintContext extends ParserRuleContext {
    alterForeignKeyConstraint(): AlterForeignKeyConstraintContext | undefined;
    alterColConstraint(): AlterColConstraintContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterForeignKeyConstraintContext extends ParserRuleContext {
    _constraintName: Id_Context;
    _tabName: TableNameContext;
    _colName: ColumnNameContext;
    KW_REFERENCES(): TerminalNode;
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    tableName(): TableNameContext;
    columnName(): ColumnNameContext;
    KW_CONSTRAINT(): TerminalNode | undefined;
    constraintOptsAlter(): ConstraintOptsAlterContext | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterColConstraintContext extends ParserRuleContext {
    _constraintName: Id_Context;
    columnConstraintType(): ColumnConstraintTypeContext;
    KW_CONSTRAINT(): TerminalNode | undefined;
    constraintOptsAlter(): ConstraintOptsAlterContext | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnConstraintTypeContext extends ParserRuleContext {
    KW_NOT(): TerminalNode | undefined;
    KW_NULL(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    defaultVal(): DefaultValContext | undefined;
    checkConstraint(): CheckConstraintContext | undefined;
    tableConstraintType(): TableConstraintTypeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DefaultValContext extends ParserRuleContext {
    constant(): ConstantContext | undefined;
    function_(): Function_Context | undefined;
    castExpression(): CastExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableConstraintTypeContext extends ParserRuleContext {
    KW_PRIMARY(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    KW_UNIQUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ConstraintOptsCreateContext extends ParserRuleContext {
    enableValidateSpecification(): EnableValidateSpecificationContext;
    relySpecification(): RelySpecificationContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ConstraintOptsAlterContext extends ParserRuleContext {
    enableValidateSpecification(): EnableValidateSpecificationContext;
    relySpecification(): RelySpecificationContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnNameColonTypeContext extends ParserRuleContext {
    _colName: ColumnNameCreateContext;
    _comment: Token;
    COLON(): TerminalNode;
    colType(): ColTypeContext;
    columnNameCreate(): ColumnNameCreateContext;
    KW_COMMENT(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColTypeContext extends ParserRuleContext {
    type(): TypeContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColTypeListContext extends ParserRuleContext {
    colType(): ColTypeContext[];
    colType(i: number): ColTypeContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TypeContext extends ParserRuleContext {
    primitiveType(): PrimitiveTypeContext | undefined;
    listType(): ListTypeContext | undefined;
    structType(): StructTypeContext | undefined;
    mapType(): MapTypeContext | undefined;
    unionType(): UnionTypeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrimitiveTypeContext extends ParserRuleContext {
    _prec: Token;
    _scale: Token;
    _length: Token;
    KW_TINYINT(): TerminalNode | undefined;
    KW_SMALLINT(): TerminalNode | undefined;
    KW_INT(): TerminalNode | undefined;
    KW_INTEGER(): TerminalNode | undefined;
    KW_BIGINT(): TerminalNode | undefined;
    KW_BOOLEAN(): TerminalNode | undefined;
    KW_FLOAT(): TerminalNode | undefined;
    KW_REAL(): TerminalNode | undefined;
    KW_DOUBLE(): TerminalNode | undefined;
    KW_PRECISION(): TerminalNode | undefined;
    KW_DATE(): TerminalNode | undefined;
    KW_DATETIME(): TerminalNode | undefined;
    KW_TIMESTAMP(): TerminalNode | undefined;
    KW_TIMESTAMPLOCALTZ(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    KW_TIME(): TerminalNode | undefined;
    KW_ZONE(): TerminalNode | undefined;
    KW_STRING(): TerminalNode | undefined;
    KW_BINARY(): TerminalNode | undefined;
    decimal(): DecimalContext | undefined;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    Number(): TerminalNode[];
    Number(i: number): TerminalNode;
    COMMA(): TerminalNode | undefined;
    KW_VARCHAR(): TerminalNode | undefined;
    KW_CHAR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ListTypeContext extends ParserRuleContext {
    KW_ARRAY(): TerminalNode;
    LESSTHAN(): TerminalNode;
    type(): TypeContext;
    GREATERTHAN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class StructTypeContext extends ParserRuleContext {
    KW_STRUCT(): TerminalNode;
    LESSTHAN(): TerminalNode;
    columnNameColonTypeList(): ColumnNameColonTypeListContext;
    GREATERTHAN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class MapTypeContext extends ParserRuleContext {
    _left: PrimitiveTypeContext;
    _right: TypeContext;
    KW_MAP(): TerminalNode;
    LESSTHAN(): TerminalNode;
    COMMA(): TerminalNode;
    GREATERTHAN(): TerminalNode;
    primitiveType(): PrimitiveTypeContext;
    type(): TypeContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class UnionTypeContext extends ParserRuleContext {
    KW_UNIONTYPE(): TerminalNode;
    LESSTHAN(): TerminalNode;
    colTypeList(): ColTypeListContext;
    GREATERTHAN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SetOperatorContext extends ParserRuleContext {
    KW_UNION(): TerminalNode | undefined;
    KW_INTERSECT(): TerminalNode | undefined;
    KW_EXCEPT(): TerminalNode | undefined;
    KW_MINUS(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class QueryStatementExpressionContext extends ParserRuleContext {
    _w: WithClauseContext;
    queryStatementExpressionBody(): QueryStatementExpressionBodyContext;
    withClause(): WithClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class QueryStatementExpressionBodyContext extends ParserRuleContext {
    fromStatement(): FromStatementContext | undefined;
    regularBody(): RegularBodyContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class WithClauseContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    cteStatement(): CteStatementContext[];
    cteStatement(i: number): CteStatementContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CteStatementContext extends ParserRuleContext {
    _colAliases: ColumnNameListContext;
    id_(): Id_Context;
    KW_AS(): TerminalNode;
    LPAREN(): TerminalNode[];
    LPAREN(i: number): TerminalNode;
    queryStatementExpression(): QueryStatementExpressionContext;
    RPAREN(): TerminalNode[];
    RPAREN(i: number): TerminalNode;
    columnNameList(): ColumnNameListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class FromStatementContext extends ParserRuleContext {
    _u: SetOperatorContext;
    _r: SingleFromStatementContext;
    singleFromStatement(): SingleFromStatementContext[];
    singleFromStatement(i: number): SingleFromStatementContext;
    setOperator(): SetOperatorContext[];
    setOperator(i: number): SetOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SingleFromStatementContext extends ParserRuleContext {
    _body: BodyContext;
    _b: BodyContext[];
    fromClause(): FromClauseContext;
    body(): BodyContext[];
    body(i: number): BodyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RegularBodyContext extends ParserRuleContext {
    _i: InsertClauseContext;
    _s: SelectStatementContext;
    insertClause(): InsertClauseContext | undefined;
    selectStatement(): SelectStatementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AtomSelectStatementContext extends ParserRuleContext {
    _s: SelectClauseContext;
    _f: FromClauseContext;
    _w: WhereClauseContext;
    _g: GroupByClauseContext;
    _h: HavingClauseContext;
    _win: Window_clauseContext;
    _q: QualifyClauseContext;
    selectClause(): SelectClauseContext | undefined;
    fromClause(): FromClauseContext | undefined;
    whereClause(): WhereClauseContext | undefined;
    groupByClause(): GroupByClauseContext | undefined;
    havingClause(): HavingClauseContext | undefined;
    window_clause(): Window_clauseContext | undefined;
    qualifyClause(): QualifyClauseContext | undefined;
    LPAREN(): TerminalNode | undefined;
    selectStatement(): SelectStatementContext | undefined;
    RPAREN(): TerminalNode | undefined;
    valuesSource(): ValuesSourceContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SelectStatementContext extends ParserRuleContext {
    _a: AtomSelectStatementContext;
    _set: SetOpSelectStatementContext;
    _o: OrderByClauseContext;
    _c: ClusterByClauseContext;
    _d: DistributeByClauseContext;
    _sort: SortByClauseContext;
    _l: LimitClauseContext;
    atomSelectStatement(): AtomSelectStatementContext;
    setOpSelectStatement(): SetOpSelectStatementContext | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    clusterByClause(): ClusterByClauseContext | undefined;
    distributeByClause(): DistributeByClauseContext | undefined;
    sortByClause(): SortByClauseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SetOpSelectStatementContext extends ParserRuleContext {
    _u: SetOperatorContext;
    _b: AtomSelectStatementContext;
    setOperator(): SetOperatorContext[];
    setOperator(i: number): SetOperatorContext;
    atomSelectStatement(): AtomSelectStatementContext[];
    atomSelectStatement(i: number): AtomSelectStatementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SelectStatementWithCTEContext extends ParserRuleContext {
    _w: WithClauseContext;
    selectStatement(): SelectStatementContext;
    withClause(): WithClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class BodyContext extends ParserRuleContext {
    insertClause(): InsertClauseContext | undefined;
    selectClause(): SelectClauseContext;
    lateralView(): LateralViewContext | undefined;
    whereClause(): WhereClauseContext | undefined;
    groupByClause(): GroupByClauseContext | undefined;
    havingClause(): HavingClauseContext | undefined;
    window_clause(): Window_clauseContext | undefined;
    qualifyClause(): QualifyClauseContext | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    clusterByClause(): ClusterByClauseContext | undefined;
    distributeByClause(): DistributeByClauseContext | undefined;
    sortByClause(): SortByClauseContext | undefined;
    limitClause(): LimitClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class InsertClauseContext extends ParserRuleContext {
    _targetCols: ColumnNameListContext;
    KW_INSERT(): TerminalNode;
    KW_OVERWRITE(): TerminalNode | undefined;
    destination(): DestinationContext | undefined;
    KW_INTO(): TerminalNode | undefined;
    tableOrPartition(): TableOrPartitionContext | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    columnNameList(): ColumnNameListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DestinationContext extends ParserRuleContext {
    _local: Token;
    KW_DIRECTORY(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    tableRowFormat(): TableRowFormatContext | undefined;
    tableFileFormat(): TableFileFormatContext | undefined;
    KW_LOCAL(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    tableOrPartition(): TableOrPartitionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class LimitClauseContext extends ParserRuleContext {
    _offset: Token;
    _num: Token;
    KW_LIMIT(): TerminalNode;
    KW_OFFSET(): TerminalNode | undefined;
    Number(): TerminalNode[];
    Number(i: number): TerminalNode;
    COMMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DeleteStatementContext extends ParserRuleContext {
    KW_DELETE(): TerminalNode;
    KW_FROM(): TerminalNode;
    tableName(): TableNameContext;
    whereClause(): WhereClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnAssignmentClauseContext extends ParserRuleContext {
    columnName(): ColumnNameContext;
    EQUAL(): TerminalNode;
    precedencePlusExpressionOrDefault(): PrecedencePlusExpressionOrDefaultContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedencePlusExpressionOrDefaultContext extends ParserRuleContext {
    defaultValue(): DefaultValueContext | undefined;
    precedencePlusExpression(): PrecedencePlusExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SetColumnsClauseContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    columnAssignmentClause(): ColumnAssignmentClauseContext[];
    columnAssignmentClause(i: number): ColumnAssignmentClauseContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class UpdateStatementContext extends ParserRuleContext {
    KW_UPDATE(): TerminalNode;
    tableName(): TableNameContext;
    setColumnsClause(): SetColumnsClauseContext;
    whereClause(): WhereClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SqlTransactionStatementContext extends ParserRuleContext {
    startTransactionStatement(): StartTransactionStatementContext | undefined;
    commitStatement(): CommitStatementContext | undefined;
    rollbackStatement(): RollbackStatementContext | undefined;
    setAutoCommitStatement(): SetAutoCommitStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class StartTransactionStatementContext extends ParserRuleContext {
    KW_START(): TerminalNode;
    KW_TRANSACTION(): TerminalNode;
    transactionMode(): TransactionModeContext[];
    transactionMode(i: number): TransactionModeContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TransactionModeContext extends ParserRuleContext {
    isolationLevel(): IsolationLevelContext | undefined;
    transactionAccessMode(): TransactionAccessModeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TransactionAccessModeContext extends ParserRuleContext {
    KW_READ(): TerminalNode;
    KW_ONLY(): TerminalNode | undefined;
    KW_WRITE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class IsolationLevelContext extends ParserRuleContext {
    KW_ISOLATION(): TerminalNode;
    KW_LEVEL(): TerminalNode;
    levelOfIsolation(): LevelOfIsolationContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class LevelOfIsolationContext extends ParserRuleContext {
    KW_SNAPSHOT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CommitStatementContext extends ParserRuleContext {
    KW_COMMIT(): TerminalNode;
    KW_WORK(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RollbackStatementContext extends ParserRuleContext {
    KW_ROLLBACK(): TerminalNode;
    KW_WORK(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SetAutoCommitStatementContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    KW_AUTOCOMMIT(): TerminalNode;
    booleanValueTok(): BooleanValueTokContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AbortTransactionStatementContext extends ParserRuleContext {
    KW_ABORT(): TerminalNode;
    KW_TRANSACTIONS(): TerminalNode;
    Number(): TerminalNode[];
    Number(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AbortCompactionStatementContext extends ParserRuleContext {
    KW_ABORT(): TerminalNode;
    KW_COMPACTIONS(): TerminalNode;
    Number(): TerminalNode[];
    Number(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class MergeStatementContext extends ParserRuleContext {
    KW_MERGE(): TerminalNode;
    KW_INTO(): TerminalNode;
    tableName(): TableNameContext;
    KW_USING(): TerminalNode;
    joinSourcePart(): JoinSourcePartContext;
    KW_ON(): TerminalNode;
    expression(): ExpressionContext;
    whenClauses(): WhenClausesContext;
    QUERY_HINT(): TerminalNode | undefined;
    id_(): Id_Context | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class WhenClausesContext extends ParserRuleContext {
    whenMatchedAndClause(): WhenMatchedAndClauseContext[];
    whenMatchedAndClause(i: number): WhenMatchedAndClauseContext;
    whenMatchedThenClause(): WhenMatchedThenClauseContext[];
    whenMatchedThenClause(i: number): WhenMatchedThenClauseContext;
    whenNotMatchedClause(): WhenNotMatchedClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class WhenNotMatchedClauseContext extends ParserRuleContext {
    _targetCols: ColumnParenthesesListContext;
    KW_WHEN(): TerminalNode;
    KW_NOT(): TerminalNode;
    KW_MATCHED(): TerminalNode;
    KW_THEN(): TerminalNode;
    KW_INSERT(): TerminalNode;
    KW_VALUES(): TerminalNode;
    valueRowConstructor(): ValueRowConstructorContext;
    KW_AND(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    columnParenthesesList(): ColumnParenthesesListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class WhenMatchedAndClauseContext extends ParserRuleContext {
    KW_WHEN(): TerminalNode;
    KW_MATCHED(): TerminalNode;
    KW_AND(): TerminalNode;
    expression(): ExpressionContext;
    KW_THEN(): TerminalNode;
    updateOrDelete(): UpdateOrDeleteContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class WhenMatchedThenClauseContext extends ParserRuleContext {
    KW_WHEN(): TerminalNode;
    KW_MATCHED(): TerminalNode;
    KW_THEN(): TerminalNode;
    updateOrDelete(): UpdateOrDeleteContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class UpdateOrDeleteContext extends ParserRuleContext {
    KW_UPDATE(): TerminalNode | undefined;
    setColumnsClause(): SetColumnsClauseContext | undefined;
    KW_DELETE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class KillQueryStatementContext extends ParserRuleContext {
    KW_KILL(): TerminalNode;
    KW_QUERY(): TerminalNode;
    StringLiteral(): TerminalNode[];
    StringLiteral(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CompactionIdContext extends ParserRuleContext {
    _compactId: Token;
    KW_COMPACT_ID(): TerminalNode;
    EQUAL(): TerminalNode;
    Number(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CompactionPoolContext extends ParserRuleContext {
    _poolName: Token;
    KW_POOL(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CompactionTypeContext extends ParserRuleContext {
    _compactType: Token;
    KW_TYPE(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CompactionStatusContext extends ParserRuleContext {
    _status: Token;
    KW_STATUS(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementContext extends ParserRuleContext {
    _tableNameTree: ViewNameContext;
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode | undefined;
    tableName(): TableNameContext | undefined;
    alterTableStatementSuffix(): AlterTableStatementSuffixContext | undefined;
    KW_VIEW(): TerminalNode | undefined;
    viewName(): ViewNameContext | undefined;
    alterViewStatementSuffix(): AlterViewStatementSuffixContext | undefined;
    KW_MATERIALIZED(): TerminalNode | undefined;
    alterMaterializedViewStatementSuffix(): AlterMaterializedViewStatementSuffixContext | undefined;
    db_schema(): Db_schemaContext | undefined;
    alterDatabaseStatementSuffix(): AlterDatabaseStatementSuffixContext | undefined;
    KW_DATACONNECTOR(): TerminalNode | undefined;
    alterDataConnectorStatementSuffix(): AlterDataConnectorStatementSuffixContext | undefined;
    KW_INDEX(): TerminalNode | undefined;
    alterIndexStatementSuffix(): AlterIndexStatementSuffixContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterTableStatementSuffixContext extends ParserRuleContext {
    alterStatementSuffixRename(): AlterStatementSuffixRenameContext | undefined;
    alterStatementSuffixRecoverPartitions(): AlterStatementSuffixRecoverPartitionsContext | undefined;
    alterStatementSuffixDropPartitions(): AlterStatementSuffixDropPartitionsContext | undefined;
    alterStatementSuffixAddPartitions(): AlterStatementSuffixAddPartitionsContext | undefined;
    alterStatementSuffixTouch(): AlterStatementSuffixTouchContext | undefined;
    alterStatementSuffixArchive(): AlterStatementSuffixArchiveContext | undefined;
    alterStatementSuffixUnArchive(): AlterStatementSuffixUnArchiveContext | undefined;
    alterStatementSuffixProperties(): AlterStatementSuffixPropertiesContext | undefined;
    alterStatementSuffixSkewedby(): AlterStatementSuffixSkewedbyContext | undefined;
    alterStatementSuffixExchangePartition(): AlterStatementSuffixExchangePartitionContext | undefined;
    alterStatementPartitionKeyType(): AlterStatementPartitionKeyTypeContext | undefined;
    alterStatementSuffixDropConstraint(): AlterStatementSuffixDropConstraintContext | undefined;
    alterStatementSuffixAddConstraint(): AlterStatementSuffixAddConstraintContext | undefined;
    alterTblPartitionStatementSuffix(): AlterTblPartitionStatementSuffixContext | undefined;
    partitionSpec(): PartitionSpecContext | undefined;
    alterStatementSuffixSetOwner(): AlterStatementSuffixSetOwnerContext | undefined;
    alterStatementSuffixSetPartSpec(): AlterStatementSuffixSetPartSpecContext | undefined;
    alterStatementSuffixExecute(): AlterStatementSuffixExecuteContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterTblPartitionStatementSuffixContext extends ParserRuleContext {
    alterStatementSuffixFileFormat(): AlterStatementSuffixFileFormatContext | undefined;
    alterStatementSuffixLocation(): AlterStatementSuffixLocationContext | undefined;
    alterStatementSuffixMergeFiles(): AlterStatementSuffixMergeFilesContext | undefined;
    alterStatementSuffixSerdeProperties(): AlterStatementSuffixSerdePropertiesContext | undefined;
    alterStatementSuffixRenamePart(): AlterStatementSuffixRenamePartContext | undefined;
    alterStatementSuffixBucketNum(): AlterStatementSuffixBucketNumContext | undefined;
    alterTblPartitionStatementSuffixSkewedLocation(): AlterTblPartitionStatementSuffixSkewedLocationContext | undefined;
    alterStatementSuffixClusterbySortby(): AlterStatementSuffixClusterbySortbyContext | undefined;
    alterStatementSuffixCompact(): AlterStatementSuffixCompactContext | undefined;
    alterStatementSuffixUpdateStatsCol(): AlterStatementSuffixUpdateStatsColContext | undefined;
    alterStatementSuffixUpdateStats(): AlterStatementSuffixUpdateStatsContext | undefined;
    alterStatementSuffixRenameCol(): AlterStatementSuffixRenameColContext | undefined;
    alterStatementSuffixAddCol(): AlterStatementSuffixAddColContext | undefined;
    alterStatementSuffixUpdateColumns(): AlterStatementSuffixUpdateColumnsContext | undefined;
    alterStatementSuffixProtections(): AlterStatementSuffixProtectionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementPartitionKeyTypeContext extends ParserRuleContext {
    KW_PARTITION(): TerminalNode;
    KW_COLUMN(): TerminalNode;
    LPAREN(): TerminalNode;
    columnNameType(): ColumnNameTypeContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterViewStatementSuffixContext extends ParserRuleContext {
    alterViewSuffixProperties(): AlterViewSuffixPropertiesContext | undefined;
    alterStatementSuffixRename(): AlterStatementSuffixRenameContext | undefined;
    alterStatementSuffixAddPartitions(): AlterStatementSuffixAddPartitionsContext | undefined;
    alterStatementSuffixDropPartitions(): AlterStatementSuffixDropPartitionsContext | undefined;
    selectStatementWithCTE(): SelectStatementWithCTEContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterMaterializedViewStatementSuffixContext extends ParserRuleContext {
    alterMaterializedViewSuffixRewrite(): AlterMaterializedViewSuffixRewriteContext | undefined;
    alterMaterializedViewSuffixRebuild(): AlterMaterializedViewSuffixRebuildContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterMaterializedViewSuffixRewriteContext extends ParserRuleContext {
    _mvRewriteFlag: RewriteEnabledContext;
    _mvRewriteFlag2: RewriteDisabledContext;
    rewriteEnabled(): RewriteEnabledContext | undefined;
    rewriteDisabled(): RewriteDisabledContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterMaterializedViewSuffixRebuildContext extends ParserRuleContext {
    KW_REBUILD(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterDatabaseStatementSuffixContext extends ParserRuleContext {
    alterDatabaseSuffixProperties(): AlterDatabaseSuffixPropertiesContext | undefined;
    alterDatabaseSuffixSetOwner(): AlterDatabaseSuffixSetOwnerContext | undefined;
    alterDatabaseSuffixSetLocation(): AlterDatabaseSuffixSetLocationContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterDatabaseSuffixPropertiesContext extends ParserRuleContext {
    _name: DbSchemaNameContext;
    KW_SET(): TerminalNode;
    KW_DBPROPERTIES(): TerminalNode;
    dbProperties(): DbPropertiesContext;
    dbSchemaName(): DbSchemaNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterDatabaseSuffixSetOwnerContext extends ParserRuleContext {
    _dbName: DbSchemaNameContext;
    KW_SET(): TerminalNode;
    KW_OWNER(): TerminalNode;
    principalAlterName(): PrincipalAlterNameContext;
    dbSchemaName(): DbSchemaNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterDatabaseSuffixSetLocationContext extends ParserRuleContext {
    _dbName: DbSchemaNameContext;
    _newLocation: Token;
    KW_SET(): TerminalNode;
    dbSchemaName(): DbSchemaNameContext;
    KW_LOCATION(): TerminalNode | undefined;
    KW_MANAGEDLOCATION(): TerminalNode | undefined;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterDatabaseSuffixSetManagedLocationContext extends ParserRuleContext {
    _dbName: DbSchemaNameContext;
    _newLocation: Token;
    KW_SET(): TerminalNode;
    KW_MANAGEDLOCATION(): TerminalNode;
    dbSchemaName(): DbSchemaNameContext;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixRenameContext extends ParserRuleContext {
    KW_RENAME(): TerminalNode;
    KW_TO(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixAddColContext extends ParserRuleContext {
    _add: Token;
    _replace: Token;
    KW_COLUMNS(): TerminalNode;
    LPAREN(): TerminalNode;
    columnNameTypeList(): ColumnNameTypeListContext;
    RPAREN(): TerminalNode;
    KW_ADD(): TerminalNode | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    restrictOrCascade(): RestrictOrCascadeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixAddConstraintContext extends ParserRuleContext {
    _fk: AlterForeignKeyWithNameContext;
    KW_ADD(): TerminalNode;
    alterConstraintWithName(): AlterConstraintWithNameContext | undefined;
    alterForeignKeyWithName(): AlterForeignKeyWithNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixUpdateColumnsContext extends ParserRuleContext {
    KW_UPDATE(): TerminalNode;
    KW_COLUMNS(): TerminalNode;
    restrictOrCascade(): RestrictOrCascadeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixProtectionsContext extends ParserRuleContext {
    enableSpecification(): EnableSpecificationContext;
    KW_NO_DROP(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    KW_OFFLINE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixDropConstraintContext extends ParserRuleContext {
    _cName: Id_Context;
    KW_DROP(): TerminalNode;
    KW_CONSTRAINT(): TerminalNode;
    id_(): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixRenameColContext extends ParserRuleContext {
    _oldName: ColumnNameContext;
    _newName: ColumnNameCreateContext;
    _comment: Token;
    KW_CHANGE(): TerminalNode;
    colType(): ColTypeContext;
    columnName(): ColumnNameContext;
    columnNameCreate(): ColumnNameCreateContext;
    KW_COLUMN(): TerminalNode | undefined;
    alterColumnConstraint(): AlterColumnConstraintContext | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    alterStatementChangeColPosition(): AlterStatementChangeColPositionContext | undefined;
    restrictOrCascade(): RestrictOrCascadeContext | undefined;
    StringLiteral(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixUpdateStatsColContext extends ParserRuleContext {
    _colName: ColumnNameContext;
    _comment: Token;
    KW_UPDATE(): TerminalNode;
    KW_STATISTICS(): TerminalNode;
    KW_FOR(): TerminalNode;
    KW_SET(): TerminalNode;
    tableProperties(): TablePropertiesContext;
    columnName(): ColumnNameContext;
    KW_COLUMN(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixUpdateStatsContext extends ParserRuleContext {
    KW_UPDATE(): TerminalNode;
    KW_STATISTICS(): TerminalNode;
    KW_SET(): TerminalNode;
    tableProperties(): TablePropertiesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementChangeColPositionContext extends ParserRuleContext {
    _first: Token;
    _afterCol: Id_Context;
    KW_FIRST(): TerminalNode | undefined;
    KW_AFTER(): TerminalNode | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixAddPartitionsContext extends ParserRuleContext {
    KW_ADD(): TerminalNode;
    ifNotExists(): IfNotExistsContext | undefined;
    alterStatementSuffixAddPartitionsElement(): AlterStatementSuffixAddPartitionsElementContext[];
    alterStatementSuffixAddPartitionsElement(i: number): AlterStatementSuffixAddPartitionsElementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixAddPartitionsElementContext extends ParserRuleContext {
    partitionSpec(): PartitionSpecContext;
    partitionLocation(): PartitionLocationContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixTouchContext extends ParserRuleContext {
    KW_TOUCH(): TerminalNode;
    partitionSpec(): PartitionSpecContext[];
    partitionSpec(i: number): PartitionSpecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixArchiveContext extends ParserRuleContext {
    KW_ARCHIVE(): TerminalNode;
    partitionSpec(): PartitionSpecContext[];
    partitionSpec(i: number): PartitionSpecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixUnArchiveContext extends ParserRuleContext {
    KW_UNARCHIVE(): TerminalNode;
    partitionSpec(): PartitionSpecContext[];
    partitionSpec(i: number): PartitionSpecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartitionLocationContext extends ParserRuleContext {
    _locn: Token;
    KW_LOCATION(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixRecoverPartitionsContext extends ParserRuleContext {
    KW_RECOVER(): TerminalNode;
    KW_PARTITIONS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixDropPartitionsContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_PARTITION(): TerminalNode[];
    KW_PARTITION(i: number): TerminalNode;
    partitionSelectorSpec(): PartitionSelectorSpecContext[];
    partitionSelectorSpec(i: number): PartitionSelectorSpecContext;
    ifExists(): IfExistsContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    dropPartitionsIgnoreClause(): DropPartitionsIgnoreClauseContext | undefined;
    KW_PURGE(): TerminalNode | undefined;
    replicationClause(): ReplicationClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixPropertiesContext extends ParserRuleContext {
    KW_SET(): TerminalNode | undefined;
    KW_TBLPROPERTIES(): TerminalNode;
    tableProperties(): TablePropertiesContext;
    KW_UNSET(): TerminalNode | undefined;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterViewSuffixPropertiesContext extends ParserRuleContext {
    KW_SET(): TerminalNode | undefined;
    KW_TBLPROPERTIES(): TerminalNode;
    tableProperties(): TablePropertiesContext;
    KW_UNSET(): TerminalNode | undefined;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixSerdePropertiesContext extends ParserRuleContext {
    _serdeName: Token;
    KW_SET(): TerminalNode | undefined;
    KW_SERDE(): TerminalNode | undefined;
    KW_SERDEPROPERTIES(): TerminalNode | undefined;
    tableProperties(): TablePropertiesContext | undefined;
    StringLiteral(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_UNSET(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TablePartitionPrefixContext extends ParserRuleContext {
    tableName(): TableNameContext;
    partitionSpec(): PartitionSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixFileFormatContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    KW_FILEFORMAT(): TerminalNode;
    fileFormat(): FileFormatContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixClusterbySortbyContext extends ParserRuleContext {
    KW_NOT(): TerminalNode | undefined;
    KW_CLUSTERED(): TerminalNode | undefined;
    KW_SORTED(): TerminalNode | undefined;
    tableBuckets(): TableBucketsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterTblPartitionStatementSuffixSkewedLocationContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    KW_SKEWED(): TerminalNode;
    KW_LOCATION(): TerminalNode;
    skewedLocations(): SkewedLocationsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SkewedLocationsContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    skewedLocationsList(): SkewedLocationsListContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SkewedLocationsListContext extends ParserRuleContext {
    skewedLocationMap(): SkewedLocationMapContext[];
    skewedLocationMap(i: number): SkewedLocationMapContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SkewedLocationMapContext extends ParserRuleContext {
    _key: SkewedValueLocationElementContext;
    _value: Token;
    EQUAL(): TerminalNode;
    skewedValueLocationElement(): SkewedValueLocationElementContext;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixLocationContext extends ParserRuleContext {
    _newLoc: Token;
    KW_SET(): TerminalNode;
    KW_LOCATION(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixSkewedbyContext extends ParserRuleContext {
    tableSkewed(): TableSkewedContext | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_SKEWED(): TerminalNode | undefined;
    storedAsDirs(): StoredAsDirsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixExchangePartitionContext extends ParserRuleContext {
    _exchangename: TableNameContext;
    KW_EXCHANGE(): TerminalNode;
    partitionSpec(): PartitionSpecContext;
    KW_WITH(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableName(): TableNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixRenamePartContext extends ParserRuleContext {
    KW_RENAME(): TerminalNode;
    KW_TO(): TerminalNode;
    partitionSpec(): PartitionSpecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixStatsPartContext extends ParserRuleContext {
    _colName: ColumnNameContext;
    _comment: Token;
    KW_UPDATE(): TerminalNode;
    KW_STATISTICS(): TerminalNode;
    KW_FOR(): TerminalNode;
    KW_SET(): TerminalNode;
    tableProperties(): TablePropertiesContext;
    columnName(): ColumnNameContext;
    KW_COLUMN(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixMergeFilesContext extends ParserRuleContext {
    KW_CONCATENATE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixBucketNumContext extends ParserRuleContext {
    _num: Token;
    KW_INTO(): TerminalNode;
    KW_BUCKETS(): TerminalNode;
    Number(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class BlockingContext extends ParserRuleContext {
    KW_AND(): TerminalNode;
    KW_WAIT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CompactPoolContext extends ParserRuleContext {
    _poolName: Token;
    KW_POOL(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixCompactContext extends ParserRuleContext {
    _compactType: Token;
    KW_COMPACT(): TerminalNode;
    StringLiteral(): TerminalNode;
    blocking(): BlockingContext | undefined;
    tableImplBuckets(): TableImplBucketsContext | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    compactPool(): CompactPoolContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_OVERWRITE(): TerminalNode | undefined;
    KW_TBLPROPERTIES(): TerminalNode | undefined;
    tableProperties(): TablePropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixSetOwnerContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    KW_OWNER(): TerminalNode;
    principalName(): PrincipalNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixSetPartSpecContext extends ParserRuleContext {
    _spec: PartitionTransformSpecContext;
    KW_SET(): TerminalNode;
    KW_PARTITION(): TerminalNode;
    KW_SPEC(): TerminalNode;
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    partitionTransformSpec(): PartitionTransformSpecContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementSuffixExecuteContext extends ParserRuleContext {
    _rollbackParam: Token;
    _expireParam: Token;
    _snapshotParam: Token;
    KW_EXECUTE(): TerminalNode;
    RPAREN(): TerminalNode;
    KW_ROLLBACK(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    KW_EXPIRE_SNAPSHOTS(): TerminalNode | undefined;
    KW_SET_CURRENT_SNAPSHOT(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    Number(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterIndexStatementSuffixContext extends ParserRuleContext {
    id_(): Id_Context;
    KW_ON(): TerminalNode;
    tableName(): TableNameContext;
    KW_REBUILD(): TerminalNode;
    partitionSpec(): PartitionSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class FileFormatContext extends ParserRuleContext {
    _inFmt: Token;
    _outFmt: Token;
    _serdeCls: Token;
    _inDriver: Token;
    _outDriver: Token;
    _genericSpec: Id_Context;
    KW_INPUTFORMAT(): TerminalNode | undefined;
    KW_OUTPUTFORMAT(): TerminalNode | undefined;
    KW_SERDE(): TerminalNode | undefined;
    StringLiteral(): TerminalNode[];
    StringLiteral(i: number): TerminalNode;
    KW_INPUTDRIVER(): TerminalNode | undefined;
    KW_OUTPUTDRIVER(): TerminalNode | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterDataConnectorStatementSuffixContext extends ParserRuleContext {
    alterDataConnectorSuffixProperties(): AlterDataConnectorSuffixPropertiesContext | undefined;
    alterDataConnectorSuffixSetOwner(): AlterDataConnectorSuffixSetOwnerContext | undefined;
    alterDataConnectorSuffixSetUrl(): AlterDataConnectorSuffixSetUrlContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterDataConnectorSuffixPropertiesContext extends ParserRuleContext {
    _name: DbSchemaNameContext;
    KW_SET(): TerminalNode;
    KW_DCPROPERTIES(): TerminalNode;
    dcProperties(): DcPropertiesContext;
    dbSchemaName(): DbSchemaNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterDataConnectorSuffixSetOwnerContext extends ParserRuleContext {
    _dcName: DbSchemaNameContext;
    KW_SET(): TerminalNode;
    KW_OWNER(): TerminalNode;
    principalAlterName(): PrincipalAlterNameContext;
    dbSchemaName(): DbSchemaNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterDataConnectorSuffixSetUrlContext extends ParserRuleContext {
    _dcName: DbSchemaNameContext;
    _newUri: Token;
    KW_SET(): TerminalNode;
    KW_URL(): TerminalNode;
    dbSchemaName(): DbSchemaNameContext;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class LikeTableOrFileContext extends ParserRuleContext {
    _format: Id_Context;
    _uri: Token;
    _likeName: TableNameContext;
    KW_LIKE(): TerminalNode;
    KW_FILE(): TerminalNode | undefined;
    id_(): Id_Context | undefined;
    StringLiteral(): TerminalNode | undefined;
    tableName(): TableNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateTableStatementContext extends ParserRuleContext {
    _temp: Token;
    _trans: Token;
    _ext: Token;
    _name: TableNameCreateContext;
    _mgd: Token;
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    likeTableOrFile(): LikeTableOrFileContext | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_TRANSACTIONAL(): TerminalNode | undefined;
    KW_EXTERNAL(): TerminalNode | undefined;
    createTablePartitionSpec(): CreateTablePartitionSpecContext | undefined;
    tableRowFormat(): TableRowFormatContext | undefined;
    tableFileFormat(): TableFileFormatContext | undefined;
    tableLocation(): TableLocationContext | undefined;
    tablePropertiesPrefixed(): TablePropertiesPrefixedContext | undefined;
    LPAREN(): TerminalNode | undefined;
    columnNameTypeOrConstraintList(): ColumnNameTypeOrConstraintListContext | undefined;
    RPAREN(): TerminalNode | undefined;
    tableComment(): TableCommentContext | undefined;
    tableBuckets(): TableBucketsContext | undefined;
    tableSkewed(): TableSkewedContext | undefined;
    KW_AS(): TerminalNode | undefined;
    selectStatementWithCTE(): SelectStatementWithCTEContext | undefined;
    KW_MANAGED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateDataConnectorStatementContext extends ParserRuleContext {
    _name: Id_Context;
    _dcprops: DcPropertiesContext;
    KW_CREATE(): TerminalNode;
    KW_DATACONNECTOR(): TerminalNode;
    id_(): Id_Context;
    ifNotExists(): IfNotExistsContext | undefined;
    dataConnectorType(): DataConnectorTypeContext | undefined;
    dataConnectorUrl(): DataConnectorUrlContext | undefined;
    dataConnectorComment(): DataConnectorCommentContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_DCPROPERTIES(): TerminalNode | undefined;
    dcProperties(): DcPropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DataConnectorCommentContext extends ParserRuleContext {
    _comment: Token;
    KW_COMMENT(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DataConnectorUrlContext extends ParserRuleContext {
    _url: Token;
    KW_URL(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DataConnectorTypeContext extends ParserRuleContext {
    _dcType: Token;
    KW_TYPE(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DcPropertiesContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    dbPropertiesList(): DbPropertiesListContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropDataConnectorStatementContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_DATACONNECTOR(): TerminalNode;
    id_(): Id_Context;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableAllColumnsContext extends ParserRuleContext {
    STAR(): TerminalNode;
    tableOrView(): TableOrViewContext | undefined;
    DOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DefaultValueContext extends ParserRuleContext {
    KW_DEFAULT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExpressionListContext extends ParserRuleContext {
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AliasListContext extends ParserRuleContext {
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class FromClauseContext extends ParserRuleContext {
    KW_FROM(): TerminalNode;
    fromSource(): FromSourceContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class FromSourceContext extends ParserRuleContext {
    uniqueJoinToken(): UniqueJoinTokenContext | undefined;
    uniqueJoinSource(): UniqueJoinSourceContext[];
    uniqueJoinSource(i: number): UniqueJoinSourceContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    joinSource(): JoinSourceContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AtomjoinSourceContext extends ParserRuleContext {
    tableSource(): TableSourceContext | undefined;
    lateralView(): LateralViewContext[];
    lateralView(i: number): LateralViewContext;
    virtualTableSource(): VirtualTableSourceContext | undefined;
    subQuerySource(): SubQuerySourceContext | undefined;
    partitionedTableFunction(): PartitionedTableFunctionContext | undefined;
    LPAREN(): TerminalNode | undefined;
    joinSource(): JoinSourceContext | undefined;
    RPAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class JoinSourceContext extends ParserRuleContext {
    atomjoinSource(): AtomjoinSourceContext;
    joinToken(): JoinTokenContext[];
    joinToken(i: number): JoinTokenContext;
    joinSourcePart(): JoinSourcePartContext[];
    joinSourcePart(i: number): JoinSourcePartContext;
    KW_ON(): TerminalNode[];
    KW_ON(i: number): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_USING(): TerminalNode[];
    KW_USING(i: number): TerminalNode;
    columnParenthesesList(): ColumnParenthesesListContext[];
    columnParenthesesList(i: number): ColumnParenthesesListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class JoinSourcePartContext extends ParserRuleContext {
    tableSource(): TableSourceContext | undefined;
    virtualTableSource(): VirtualTableSourceContext | undefined;
    subQuerySource(): SubQuerySourceContext | undefined;
    partitionedTableFunction(): PartitionedTableFunctionContext | undefined;
    lateralView(): LateralViewContext[];
    lateralView(i: number): LateralViewContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class UniqueJoinSourceContext extends ParserRuleContext {
    uniqueJoinTableSource(): UniqueJoinTableSourceContext;
    uniqueJoinExpr(): UniqueJoinExprContext;
    KW_PRESERVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class UniqueJoinExprContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    expressionList(): ExpressionListContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class UniqueJoinTokenContext extends ParserRuleContext {
    KW_UNIQUEJOIN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class JoinTokenContext extends ParserRuleContext {
    COMMA(): TerminalNode | undefined;
    KW_JOIN(): TerminalNode | undefined;
    KW_INNER(): TerminalNode | undefined;
    KW_CROSS(): TerminalNode | undefined;
    KW_LEFT(): TerminalNode | undefined;
    KW_RIGHT(): TerminalNode | undefined;
    KW_FULL(): TerminalNode | undefined;
    KW_OUTER(): TerminalNode | undefined;
    KW_SEMI(): TerminalNode | undefined;
    KW_ANTI(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class LateralViewContext extends ParserRuleContext {
    KW_LATERAL(): TerminalNode;
    KW_VIEW(): TerminalNode | undefined;
    KW_OUTER(): TerminalNode | undefined;
    function_(): Function_Context | undefined;
    tableAlias(): TableAliasContext | undefined;
    KW_AS(): TerminalNode | undefined;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_TABLE(): TerminalNode | undefined;
    LPAREN(): TerminalNode[];
    LPAREN(i: number): TerminalNode;
    valuesClause(): ValuesClauseContext | undefined;
    RPAREN(): TerminalNode[];
    RPAREN(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableAliasContext extends ParserRuleContext {
    id_(): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableBucketSampleContext extends ParserRuleContext {
    _numerator: Token;
    _denominator: Token;
    _expression: ExpressionContext;
    _expr: ExpressionContext[];
    KW_TABLESAMPLE(): TerminalNode;
    LPAREN(): TerminalNode;
    KW_BUCKET(): TerminalNode;
    KW_OUT(): TerminalNode;
    KW_OF(): TerminalNode;
    RPAREN(): TerminalNode;
    Number(): TerminalNode[];
    Number(i: number): TerminalNode;
    KW_ON(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SplitSampleContext extends ParserRuleContext {
    KW_TABLESAMPLE(): TerminalNode;
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    Number(): TerminalNode | undefined;
    ByteLengthLiteral(): TerminalNode | undefined;
    KW_PERCENT(): TerminalNode | undefined;
    KW_ROWS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableSampleContext extends ParserRuleContext {
    tableBucketSample(): TableBucketSampleContext | undefined;
    splitSample(): SplitSampleContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableSourceContext extends ParserRuleContext {
    _tabname: TableOrViewContext;
    _props: TablePropertiesContext;
    _ts: TableSampleContext;
    _asOf: AsOfClauseContext;
    _alias: Id_Context;
    tableOrView(): TableOrViewContext;
    tableProperties(): TablePropertiesContext | undefined;
    tableSample(): TableSampleContext | undefined;
    asOfClause(): AsOfClauseContext | undefined;
    id_(): Id_Context | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AsOfClauseContext extends ParserRuleContext {
    _asOfTime: ExpressionContext;
    _asOfVersion: Token;
    KW_FOR(): TerminalNode[];
    KW_FOR(i: number): TerminalNode;
    KW_SYSTEM_TIME(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    KW_OF(): TerminalNode | undefined;
    KW_SYSTEM_VERSION(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    Number(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class UniqueJoinTableSourceContext extends ParserRuleContext {
    _tabname: TableOrViewContext;
    _ts: TableSampleContext;
    _alias: Id_Context;
    tableOrView(): TableOrViewContext;
    tableSample(): TableSampleContext | undefined;
    id_(): Id_Context | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DbSchemaNameContext extends ParserRuleContext {
    id_(): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DbSchemaNameCreateContext extends ParserRuleContext {
    id_(): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableOrViewContext extends ParserRuleContext {
    tableName(): TableNameContext | undefined;
    viewName(): ViewNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableNameContext extends ParserRuleContext {
    _db: Id_Context;
    _tab: Id_Context;
    _meta: Id_Context;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableNameCreateContext extends ParserRuleContext {
    _db: Id_Context;
    _tab: Id_Context;
    _meta: Id_Context;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ViewNameContext extends ParserRuleContext {
    _db: Id_Context;
    _view: Id_Context;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    DOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ViewNameCreateContext extends ParserRuleContext {
    _db: Id_Context;
    _view: Id_Context;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    DOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SubQuerySourceContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    queryStatementExpression(): QueryStatementExpressionContext;
    RPAREN(): TerminalNode;
    id_(): Id_Context;
    KW_AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartitioningSpecContext extends ParserRuleContext {
    partitionByClause(): PartitionByClauseContext | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    distributeByClause(): DistributeByClauseContext | undefined;
    sortByClause(): SortByClauseContext | undefined;
    clusterByClause(): ClusterByClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartitionTableFunctionSourceContext extends ParserRuleContext {
    subQuerySource(): SubQuerySourceContext | undefined;
    tableSource(): TableSourceContext | undefined;
    partitionedTableFunction(): PartitionedTableFunctionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartitionedTableFunctionContext extends ParserRuleContext {
    _n: Id_Context;
    _ptfsrc: PartitionTableFunctionSourceContext;
    _spec: PartitioningSpecContext;
    _alias: Id_Context;
    LPAREN(): TerminalNode[];
    LPAREN(i: number): TerminalNode;
    KW_ON(): TerminalNode;
    RPAREN(): TerminalNode[];
    RPAREN(i: number): TerminalNode;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    partitionTableFunctionSource(): PartitionTableFunctionSourceContext;
    Identifier(): TerminalNode[];
    Identifier(i: number): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    partitioningSpec(): PartitioningSpecContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class WhereClauseContext extends ParserRuleContext {
    KW_WHERE(): TerminalNode;
    searchCondition(): SearchConditionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SearchConditionContext extends ParserRuleContext {
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ValuesSourceContext extends ParserRuleContext {
    valuesClause(): ValuesClauseContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ValuesClauseContext extends ParserRuleContext {
    KW_VALUES(): TerminalNode;
    valuesTableConstructor(): ValuesTableConstructorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ValuesTableConstructorContext extends ParserRuleContext {
    valueRowConstructor(): ValueRowConstructorContext[];
    valueRowConstructor(i: number): ValueRowConstructorContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    firstValueRowConstructor(): FirstValueRowConstructorContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ValueRowConstructorContext extends ParserRuleContext {
    expressionsInParenthesis(): ExpressionsInParenthesisContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class FirstValueRowConstructorContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    firstExpressionsWithAlias(): FirstExpressionsWithAliasContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class VirtualTableSourceContext extends ParserRuleContext {
    KW_TABLE(): TerminalNode;
    LPAREN(): TerminalNode[];
    LPAREN(i: number): TerminalNode;
    valuesClause(): ValuesClauseContext;
    RPAREN(): TerminalNode[];
    RPAREN(i: number): TerminalNode;
    tableAlias(): TableAliasContext;
    KW_AS(): TerminalNode | undefined;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SelectClauseContext extends ParserRuleContext {
    KW_SELECT(): TerminalNode | undefined;
    selectList(): SelectListContext | undefined;
    KW_TRANSFORM(): TerminalNode | undefined;
    selectTrfmClause(): SelectTrfmClauseContext | undefined;
    QUERY_HINT(): TerminalNode | undefined;
    all_distinct(): All_distinctContext | undefined;
    trfmClause(): TrfmClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class All_distinctContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SelectListContext extends ParserRuleContext {
    selectItem(): SelectItemContext[];
    selectItem(i: number): SelectItemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SelectTrfmClauseContext extends ParserRuleContext {
    LPAREN(): TerminalNode[];
    LPAREN(i: number): TerminalNode;
    selectExpressionList(): SelectExpressionListContext;
    RPAREN(): TerminalNode[];
    RPAREN(i: number): TerminalNode;
    rowFormat(): RowFormatContext[];
    rowFormat(i: number): RowFormatContext;
    recordWriter(): RecordWriterContext;
    KW_USING(): TerminalNode;
    StringLiteral(): TerminalNode;
    recordReader(): RecordReaderContext;
    KW_AS(): TerminalNode | undefined;
    aliasList(): AliasListContext | undefined;
    columnNameTypeList(): ColumnNameTypeListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SelectItemContext extends ParserRuleContext {
    tableAllColumns(): TableAllColumnsContext | undefined;
    columnName(): ColumnNameContext | undefined;
    expression(): ExpressionContext | undefined;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    KW_AS(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TrfmClauseContext extends ParserRuleContext {
    selectExpressionList(): SelectExpressionListContext;
    rowFormat(): RowFormatContext[];
    rowFormat(i: number): RowFormatContext;
    recordWriter(): RecordWriterContext;
    KW_USING(): TerminalNode;
    StringLiteral(): TerminalNode;
    recordReader(): RecordReaderContext;
    KW_MAP(): TerminalNode | undefined;
    KW_REDUCE(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    aliasList(): AliasListContext | undefined;
    columnNameTypeList(): ColumnNameTypeListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SelectExpressionContext extends ParserRuleContext {
    tableAllColumns(): TableAllColumnsContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SelectExpressionListContext extends ParserRuleContext {
    selectExpression(): SelectExpressionContext[];
    selectExpression(i: number): SelectExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Window_clauseContext extends ParserRuleContext {
    KW_WINDOW(): TerminalNode;
    window_defn(): Window_defnContext[];
    window_defn(i: number): Window_defnContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Window_defnContext extends ParserRuleContext {
    id_(): Id_Context;
    KW_AS(): TerminalNode;
    window_specification(): Window_specificationContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Window_specificationContext extends ParserRuleContext {
    id_(): Id_Context | undefined;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    partitioningSpec(): PartitioningSpecContext | undefined;
    window_frame(): Window_frameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Window_frameContext extends ParserRuleContext {
    window_range_expression(): Window_range_expressionContext | undefined;
    window_value_expression(): Window_value_expressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Window_range_expressionContext extends ParserRuleContext {
    KW_ROWS(): TerminalNode;
    window_frame_start_boundary(): Window_frame_start_boundaryContext | undefined;
    KW_BETWEEN(): TerminalNode | undefined;
    window_frame_boundary(): Window_frame_boundaryContext[];
    window_frame_boundary(i: number): Window_frame_boundaryContext;
    KW_AND(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Window_value_expressionContext extends ParserRuleContext {
    KW_RANGE(): TerminalNode;
    window_frame_start_boundary(): Window_frame_start_boundaryContext | undefined;
    KW_BETWEEN(): TerminalNode | undefined;
    window_frame_boundary(): Window_frame_boundaryContext[];
    window_frame_boundary(i: number): Window_frame_boundaryContext;
    KW_AND(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Window_frame_start_boundaryContext extends ParserRuleContext {
    KW_UNBOUNDED(): TerminalNode | undefined;
    KW_PRECEDING(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    Number(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Window_frame_boundaryContext extends ParserRuleContext {
    KW_UNBOUNDED(): TerminalNode | undefined;
    Number(): TerminalNode | undefined;
    KW_PRECEDING(): TerminalNode | undefined;
    KW_FOLLOWING(): TerminalNode | undefined;
    KW_CURRENT(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class GroupByClauseContext extends ParserRuleContext {
    KW_GROUP(): TerminalNode;
    KW_BY(): TerminalNode;
    groupby_expression(): Groupby_expressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Groupby_expressionContext extends ParserRuleContext {
    columnName(): ColumnNameContext | undefined;
    rollupStandard(): RollupStandardContext | undefined;
    rollupOldSyntax(): RollupOldSyntaxContext | undefined;
    groupByEmpty(): GroupByEmptyContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class GroupByEmptyContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RollupStandardContext extends ParserRuleContext {
    _rollup: Token;
    _cube: Token;
    LPAREN(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    RPAREN(): TerminalNode;
    KW_ROLLUP(): TerminalNode | undefined;
    KW_CUBE(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RollupOldSyntaxContext extends ParserRuleContext {
    _expr: ExpressionsNotInParenthesisContext;
    _rollup: Token;
    _cube: Token;
    _sets: Token;
    expressionsNotInParenthesis(): ExpressionsNotInParenthesisContext;
    KW_ROLLUP(): TerminalNode | undefined;
    KW_CUBE(): TerminalNode | undefined;
    KW_SETS(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    groupingSetExpression(): GroupingSetExpressionContext[];
    groupingSetExpression(i: number): GroupingSetExpressionContext;
    RPAREN(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_GROUPING(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class GroupingSetExpressionContext extends ParserRuleContext {
    groupingSetExpressionMultiple(): GroupingSetExpressionMultipleContext | undefined;
    groupingExpressionSingle(): GroupingExpressionSingleContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class GroupingSetExpressionMultipleContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class GroupingExpressionSingleContext extends ParserRuleContext {
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class HavingClauseContext extends ParserRuleContext {
    KW_HAVING(): TerminalNode;
    havingCondition(): HavingConditionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class QualifyClauseContext extends ParserRuleContext {
    KW_QUALIFY(): TerminalNode;
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class HavingConditionContext extends ParserRuleContext {
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExpressionsInParenthesisContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    expressionsNotInParenthesis(): ExpressionsNotInParenthesisContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExpressionsNotInParenthesisContext extends ParserRuleContext {
    _first: ExpressionOrDefaultContext;
    _more: ExpressionPartContext;
    expressionOrDefault(): ExpressionOrDefaultContext;
    expressionPart(): ExpressionPartContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExpressionPartContext extends ParserRuleContext {
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    expressionOrDefault(): ExpressionOrDefaultContext[];
    expressionOrDefault(i: number): ExpressionOrDefaultContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExpressionOrDefaultContext extends ParserRuleContext {
    defaultValue(): DefaultValueContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class FirstExpressionsWithAliasContext extends ParserRuleContext {
    _first: ExpressionContext;
    _colAlias: Id_Context;
    expression(): ExpressionContext;
    KW_AS(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    expressionWithAlias(): ExpressionWithAliasContext[];
    expressionWithAlias(i: number): ExpressionWithAliasContext;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExpressionWithAliasContext extends ParserRuleContext {
    _alias: Id_Context;
    expression(): ExpressionContext;
    KW_AS(): TerminalNode | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExpressionsContext extends ParserRuleContext {
    expressionsInParenthesis(): ExpressionsInParenthesisContext | undefined;
    expressionsNotInParenthesis(): ExpressionsNotInParenthesisContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnRefOrderInParenthesisContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    columnRefOrder(): ColumnRefOrderContext[];
    columnRefOrder(i: number): ColumnRefOrderContext;
    RPAREN(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ColumnRefOrderNotInParenthesisContext extends ParserRuleContext {
    columnRefOrder(): ColumnRefOrderContext[];
    columnRefOrder(i: number): ColumnRefOrderContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class OrderByClauseContext extends ParserRuleContext {
    KW_ORDER(): TerminalNode;
    KW_BY(): TerminalNode;
    columnRefOrder(): ColumnRefOrderContext[];
    columnRefOrder(i: number): ColumnRefOrderContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ClusterByClauseContext extends ParserRuleContext {
    KW_CLUSTER(): TerminalNode;
    KW_BY(): TerminalNode;
    expressions(): ExpressionsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartitionByClauseContext extends ParserRuleContext {
    KW_PARTITION(): TerminalNode;
    KW_BY(): TerminalNode;
    expressions(): ExpressionsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DistributeByClauseContext extends ParserRuleContext {
    KW_DISTRIBUTE(): TerminalNode;
    KW_BY(): TerminalNode;
    expressions(): ExpressionsContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SortByClauseContext extends ParserRuleContext {
    KW_SORT(): TerminalNode;
    KW_BY(): TerminalNode;
    columnRefOrderInParenthesis(): ColumnRefOrderInParenthesisContext | undefined;
    columnRefOrderNotInParenthesis(): ColumnRefOrderNotInParenthesisContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TrimFunctionContext extends ParserRuleContext {
    _leading: Token;
    _trailing: Token;
    _trim_characters: SelectExpressionContext;
    _str: SelectExpressionContext;
    KW_TRIM(): TerminalNode;
    LPAREN(): TerminalNode;
    KW_FROM(): TerminalNode;
    RPAREN(): TerminalNode;
    selectExpression(): SelectExpressionContext[];
    selectExpression(i: number): SelectExpressionContext;
    KW_BOTH(): TerminalNode | undefined;
    KW_LEADING(): TerminalNode | undefined;
    KW_TRAILING(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Function_Context extends ParserRuleContext {
    _star: Token;
    _dist: All_distinctContext;
    _within: Token;
    _ordBy: OrderByClauseContext;
    _nt: Null_treatmentContext;
    _ws: Window_specificationContext;
    trimFunction(): TrimFunctionContext | undefined;
    functionNameForInvoke(): FunctionNameForInvokeContext | undefined;
    LPAREN(): TerminalNode[];
    LPAREN(i: number): TerminalNode;
    RPAREN(): TerminalNode[];
    RPAREN(i: number): TerminalNode;
    KW_GROUP(): TerminalNode | undefined;
    KW_OVER(): TerminalNode | undefined;
    STAR(): TerminalNode | undefined;
    KW_WITHIN(): TerminalNode | undefined;
    orderByClause(): OrderByClauseContext | undefined;
    window_specification(): Window_specificationContext | undefined;
    null_treatment(): Null_treatmentContext | undefined;
    selectExpression(): SelectExpressionContext[];
    selectExpression(i: number): SelectExpressionContext;
    all_distinct(): All_distinctContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Null_treatmentContext extends ParserRuleContext {
    KW_RESPECT(): TerminalNode | undefined;
    KW_NULLS(): TerminalNode;
    KW_IGNORE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class FunctionNameCreateContext extends ParserRuleContext {
    functionIdentifier(): FunctionIdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class FunctionNameForDDLContext extends ParserRuleContext {
    userDefinedFuncName(): UserDefinedFuncNameContext | undefined;
    StringLiteral(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class FunctionNameForInvokeContext extends ParserRuleContext {
    userDefinedFuncName(): UserDefinedFuncNameContext | undefined;
    internalFunctionName(): InternalFunctionNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class UserDefinedFuncNameContext extends ParserRuleContext {
    functionIdentifier(): FunctionIdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class InternalFunctionNameContext extends ParserRuleContext {
    sql11ReservedKeywordsUsedAsFunctionName(): Sql11ReservedKeywordsUsedAsFunctionNameContext | undefined;
    sysFuncNames(): SysFuncNamesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CastExpressionContext extends ParserRuleContext {
    _toType: PrimitiveTypeContext;
    _fmt: Token;
    KW_CAST(): TerminalNode;
    LPAREN(): TerminalNode;
    expression(): ExpressionContext;
    KW_AS(): TerminalNode;
    RPAREN(): TerminalNode;
    primitiveType(): PrimitiveTypeContext;
    StringLiteral(): TerminalNode | undefined;
    KW_FORMAT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CaseExpressionContext extends ParserRuleContext {
    KW_CASE(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_END(): TerminalNode;
    KW_WHEN(): TerminalNode[];
    KW_WHEN(i: number): TerminalNode;
    KW_THEN(): TerminalNode[];
    KW_THEN(i: number): TerminalNode;
    KW_ELSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class WhenExpressionContext extends ParserRuleContext {
    KW_CASE(): TerminalNode;
    KW_END(): TerminalNode;
    KW_WHEN(): TerminalNode[];
    KW_WHEN(i: number): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_THEN(): TerminalNode[];
    KW_THEN(i: number): TerminalNode;
    KW_ELSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class FloorExpressionContext extends ParserRuleContext {
    _floorUnit: FloorDateQualifiersContext;
    KW_FLOOR(): TerminalNode;
    LPAREN(): TerminalNode;
    expression(): ExpressionContext;
    RPAREN(): TerminalNode;
    KW_TO(): TerminalNode | undefined;
    floorDateQualifiers(): FloorDateQualifiersContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class FloorDateQualifiersContext extends ParserRuleContext {
    year(): YearContext | undefined;
    KW_QUARTER(): TerminalNode | undefined;
    month(): MonthContext | undefined;
    week(): WeekContext | undefined;
    day(): DayContext | undefined;
    hour(): HourContext | undefined;
    minute(): MinuteContext | undefined;
    second(): SecondContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExtractExpressionContext extends ParserRuleContext {
    _timeUnit: TimeQualifiersContext;
    KW_EXTRACT(): TerminalNode;
    LPAREN(): TerminalNode;
    KW_FROM(): TerminalNode;
    expression(): ExpressionContext;
    RPAREN(): TerminalNode;
    timeQualifiers(): TimeQualifiersContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TimeQualifiersContext extends ParserRuleContext {
    year(): YearContext | undefined;
    KW_QUARTER(): TerminalNode | undefined;
    month(): MonthContext | undefined;
    week(): WeekContext | undefined;
    day(): DayContext | undefined;
    hour(): HourContext | undefined;
    minute(): MinuteContext | undefined;
    second(): SecondContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ConstantContext extends ParserRuleContext {
    intervalLiteral(): IntervalLiteralContext | undefined;
    Number(): TerminalNode | undefined;
    dateLiteral(): DateLiteralContext | undefined;
    timestampLiteral(): TimestampLiteralContext | undefined;
    timestampLocalTZLiteral(): TimestampLocalTZLiteralContext | undefined;
    StringLiteral(): TerminalNode | undefined;
    stringLiteralSequence(): StringLiteralSequenceContext | undefined;
    IntegralLiteral(): TerminalNode | undefined;
    NumberLiteral(): TerminalNode | undefined;
    charSetStringLiteral(): CharSetStringLiteralContext | undefined;
    booleanValue(): BooleanValueContext | undefined;
    KW_NULL(): TerminalNode | undefined;
    prepareStmtParam(): PrepareStmtParamContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrepareStmtParamContext extends ParserRuleContext {
    _p: ParameterIdxContext;
    parameterIdx(): ParameterIdxContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ParameterIdxContext extends ParserRuleContext {
    QUESTION(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class StringLiteralSequenceContext extends ParserRuleContext {
    StringLiteral(): TerminalNode[];
    StringLiteral(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CharSetStringLiteralContext extends ParserRuleContext {
    _csName: Token;
    _csLiteral: Token;
    CharSetName(): TerminalNode;
    CharSetLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DateLiteralContext extends ParserRuleContext {
    KW_DATE(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    KW_CURRENT_DATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TimestampLiteralContext extends ParserRuleContext {
    KW_TIMESTAMP(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TimestampLocalTZLiteralContext extends ParserRuleContext {
    KW_TIMESTAMPLOCALTZ(): TerminalNode;
    StringLiteral(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class IntervalValueContext extends ParserRuleContext {
    StringLiteral(): TerminalNode | undefined;
    Number(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class IntervalLiteralContext extends ParserRuleContext {
    _value: IntervalValueContext;
    _qualifiers: IntervalQualifiersContext;
    intervalValue(): IntervalValueContext;
    intervalQualifiers(): IntervalQualifiersContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class IntervalExpressionContext extends ParserRuleContext {
    _value: IntervalValueContext;
    _qualifiers: IntervalQualifiersContext;
    _expr: ExpressionContext;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    intervalValue(): IntervalValueContext | undefined;
    intervalQualifiers(): IntervalQualifiersContext;
    KW_INTERVAL(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class IntervalQualifiersContext extends ParserRuleContext {
    year(): YearContext | undefined;
    KW_TO(): TerminalNode | undefined;
    month(): MonthContext | undefined;
    day(): DayContext | undefined;
    second(): SecondContext | undefined;
    hour(): HourContext | undefined;
    minute(): MinuteContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExpressionContext extends ParserRuleContext {
    precedenceOrExpression(): PrecedenceOrExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AtomExpressionContext extends ParserRuleContext {
    constant(): ConstantContext | undefined;
    intervalExpression(): IntervalExpressionContext | undefined;
    castExpression(): CastExpressionContext | undefined;
    extractExpression(): ExtractExpressionContext | undefined;
    floorExpression(): FloorExpressionContext | undefined;
    caseExpression(): CaseExpressionContext | undefined;
    whenExpression(): WhenExpressionContext | undefined;
    subQueryExpression(): SubQueryExpressionContext | undefined;
    function_(): Function_Context | undefined;
    expressionsInParenthesis(): ExpressionsInParenthesisContext | undefined;
    id_(): Id_Context | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceFieldExpressionContext extends ParserRuleContext {
    atomExpression(): AtomExpressionContext;
    LSQUARE(): TerminalNode[];
    LSQUARE(i: number): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    RSQUARE(): TerminalNode[];
    RSQUARE(i: number): TerminalNode;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceUnaryOperatorContext extends ParserRuleContext {
    PLUS(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    TILDE(): TerminalNode | undefined;
    BITWISENOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceUnaryPrefixExpressionContext extends ParserRuleContext {
    precedenceFieldExpression(): PrecedenceFieldExpressionContext;
    precedenceUnaryOperator(): PrecedenceUnaryOperatorContext[];
    precedenceUnaryOperator(i: number): PrecedenceUnaryOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceBitwiseXorOperatorContext extends ParserRuleContext {
    BITWISEXOR(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceBitwiseXorExpressionContext extends ParserRuleContext {
    precedenceUnaryPrefixExpression(): PrecedenceUnaryPrefixExpressionContext[];
    precedenceUnaryPrefixExpression(i: number): PrecedenceUnaryPrefixExpressionContext;
    precedenceBitwiseXorOperator(): PrecedenceBitwiseXorOperatorContext[];
    precedenceBitwiseXorOperator(i: number): PrecedenceBitwiseXorOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceStarOperatorContext extends ParserRuleContext {
    STAR(): TerminalNode | undefined;
    DIVIDE(): TerminalNode | undefined;
    MOD(): TerminalNode | undefined;
    DIV(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceStarExpressionContext extends ParserRuleContext {
    precedenceBitwiseXorExpression(): PrecedenceBitwiseXorExpressionContext[];
    precedenceBitwiseXorExpression(i: number): PrecedenceBitwiseXorExpressionContext;
    precedenceStarOperator(): PrecedenceStarOperatorContext[];
    precedenceStarOperator(i: number): PrecedenceStarOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedencePlusOperatorContext extends ParserRuleContext {
    PLUS(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedencePlusExpressionContext extends ParserRuleContext {
    precedenceStarExpression(): PrecedenceStarExpressionContext[];
    precedenceStarExpression(i: number): PrecedenceStarExpressionContext;
    precedencePlusOperator(): PrecedencePlusOperatorContext[];
    precedencePlusOperator(i: number): PrecedencePlusOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceConcatenateOperatorContext extends ParserRuleContext {
    CONCATENATE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceConcatenateExpressionContext extends ParserRuleContext {
    _plus: PrecedencePlusExpressionContext;
    precedencePlusExpression(): PrecedencePlusExpressionContext[];
    precedencePlusExpression(i: number): PrecedencePlusExpressionContext;
    precedenceConcatenateOperator(): PrecedenceConcatenateOperatorContext[];
    precedenceConcatenateOperator(i: number): PrecedenceConcatenateOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceAmpersandOperatorContext extends ParserRuleContext {
    AMPERSAND(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceAmpersandExpressionContext extends ParserRuleContext {
    precedenceConcatenateExpression(): PrecedenceConcatenateExpressionContext[];
    precedenceConcatenateExpression(i: number): PrecedenceConcatenateExpressionContext;
    precedenceAmpersandOperator(): PrecedenceAmpersandOperatorContext[];
    precedenceAmpersandOperator(i: number): PrecedenceAmpersandOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceBitwiseOrOperatorContext extends ParserRuleContext {
    BITWISEOR(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceBitwiseOrExpressionContext extends ParserRuleContext {
    precedenceAmpersandExpression(): PrecedenceAmpersandExpressionContext[];
    precedenceAmpersandExpression(i: number): PrecedenceAmpersandExpressionContext;
    precedenceBitwiseOrOperator(): PrecedenceBitwiseOrOperatorContext[];
    precedenceBitwiseOrOperator(i: number): PrecedenceBitwiseOrOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceRegexpOperatorContext extends ParserRuleContext {
    KW_LIKE(): TerminalNode | undefined;
    KW_RLIKE(): TerminalNode | undefined;
    KW_REGEXP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceSimilarOperatorContext extends ParserRuleContext {
    precedenceRegexpOperator(): PrecedenceRegexpOperatorContext | undefined;
    LESSTHANOREQUALTO(): TerminalNode | undefined;
    LESSTHAN(): TerminalNode | undefined;
    GREATERTHANOREQUALTO(): TerminalNode | undefined;
    GREATERTHAN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SubQueryExpressionContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    selectStatement(): SelectStatementContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceSimilarExpressionContext extends ParserRuleContext {
    precedenceSimilarExpressionMain(): PrecedenceSimilarExpressionMainContext | undefined;
    KW_EXISTS(): TerminalNode | undefined;
    subQueryExpression(): SubQueryExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceSimilarExpressionMainContext extends ParserRuleContext {
    _a: PrecedenceBitwiseOrExpressionContext;
    _part: PrecedenceSimilarExpressionPartContext;
    precedenceBitwiseOrExpression(): PrecedenceBitwiseOrExpressionContext;
    precedenceSimilarExpressionPart(): PrecedenceSimilarExpressionPartContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceSimilarExpressionPartContext extends ParserRuleContext {
    _equalExpr: PrecedenceBitwiseOrExpressionContext;
    precedenceSimilarOperator(): PrecedenceSimilarOperatorContext | undefined;
    precedenceBitwiseOrExpression(): PrecedenceBitwiseOrExpressionContext | undefined;
    precedenceSimilarExpressionAtom(): PrecedenceSimilarExpressionAtomContext | undefined;
    KW_NOT(): TerminalNode | undefined;
    precedenceSimilarExpressionPartNot(): PrecedenceSimilarExpressionPartNotContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceSimilarExpressionAtomContext extends ParserRuleContext {
    _min: PrecedenceBitwiseOrExpressionContext;
    _max: PrecedenceBitwiseOrExpressionContext;
    _expr: ExpressionsInParenthesisContext;
    KW_IN(): TerminalNode | undefined;
    precedenceSimilarExpressionIn(): PrecedenceSimilarExpressionInContext | undefined;
    KW_BETWEEN(): TerminalNode | undefined;
    KW_AND(): TerminalNode | undefined;
    precedenceBitwiseOrExpression(): PrecedenceBitwiseOrExpressionContext[];
    precedenceBitwiseOrExpression(i: number): PrecedenceBitwiseOrExpressionContext;
    KW_LIKE(): TerminalNode | undefined;
    KW_ANY(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    expressionsInParenthesis(): ExpressionsInParenthesisContext | undefined;
    precedenceSimilarExpressionQuantifierPredicate(): PrecedenceSimilarExpressionQuantifierPredicateContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceSimilarExpressionQuantifierPredicateContext extends ParserRuleContext {
    subQuerySelectorOperator(): SubQuerySelectorOperatorContext;
    quantifierType(): QuantifierTypeContext;
    subQueryExpression(): SubQueryExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class QuantifierTypeContext extends ParserRuleContext {
    KW_ANY(): TerminalNode | undefined;
    KW_SOME(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceSimilarExpressionInContext extends ParserRuleContext {
    _expr: ExpressionsInParenthesisContext;
    subQueryExpression(): SubQueryExpressionContext | undefined;
    expressionsInParenthesis(): ExpressionsInParenthesisContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceSimilarExpressionPartNotContext extends ParserRuleContext {
    _notExpr: PrecedenceBitwiseOrExpressionContext;
    precedenceRegexpOperator(): PrecedenceRegexpOperatorContext | undefined;
    precedenceBitwiseOrExpression(): PrecedenceBitwiseOrExpressionContext | undefined;
    precedenceSimilarExpressionAtom(): PrecedenceSimilarExpressionAtomContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceDistinctOperatorContext extends ParserRuleContext {
    KW_IS(): TerminalNode;
    KW_DISTINCT(): TerminalNode;
    KW_FROM(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceEqualOperatorContext extends ParserRuleContext {
    EQUAL(): TerminalNode | undefined;
    EQUAL_NS(): TerminalNode | undefined;
    NOTEQUAL(): TerminalNode | undefined;
    KW_IS(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_DISTINCT(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceEqualExpressionContext extends ParserRuleContext {
    _precedenceEqualOperator: PrecedenceEqualOperatorContext;
    _equal: PrecedenceEqualOperatorContext[];
    _precedenceSimilarExpression: PrecedenceSimilarExpressionContext;
    _p: PrecedenceSimilarExpressionContext[];
    _precedenceDistinctOperator: PrecedenceDistinctOperatorContext;
    _dist: PrecedenceDistinctOperatorContext[];
    precedenceSimilarExpression(): PrecedenceSimilarExpressionContext[];
    precedenceSimilarExpression(i: number): PrecedenceSimilarExpressionContext;
    precedenceEqualOperator(): PrecedenceEqualOperatorContext[];
    precedenceEqualOperator(i: number): PrecedenceEqualOperatorContext;
    precedenceDistinctOperator(): PrecedenceDistinctOperatorContext[];
    precedenceDistinctOperator(i: number): PrecedenceDistinctOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class IsConditionContext extends ParserRuleContext {
    KW_NULL(): TerminalNode | undefined;
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    KW_UNKNOWN(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceUnarySuffixExpressionContext extends ParserRuleContext {
    _a: Token;
    precedenceEqualExpression(): PrecedenceEqualExpressionContext;
    isCondition(): IsConditionContext | undefined;
    KW_IS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceNotOperatorContext extends ParserRuleContext {
    KW_NOT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceNotExpressionContext extends ParserRuleContext {
    precedenceUnarySuffixExpression(): PrecedenceUnarySuffixExpressionContext;
    precedenceNotOperator(): PrecedenceNotOperatorContext[];
    precedenceNotOperator(i: number): PrecedenceNotOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceAndOperatorContext extends ParserRuleContext {
    KW_AND(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceAndExpressionContext extends ParserRuleContext {
    precedenceNotExpression(): PrecedenceNotExpressionContext[];
    precedenceNotExpression(i: number): PrecedenceNotExpressionContext;
    precedenceAndOperator(): PrecedenceAndOperatorContext[];
    precedenceAndOperator(i: number): PrecedenceAndOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceOrOperatorContext extends ParserRuleContext {
    KW_OR(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrecedenceOrExpressionContext extends ParserRuleContext {
    precedenceAndExpression(): PrecedenceAndExpressionContext[];
    precedenceAndExpression(i: number): PrecedenceAndExpressionContext;
    precedenceOrOperator(): PrecedenceOrOperatorContext[];
    precedenceOrOperator(i: number): PrecedenceOrOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class BooleanValueContext extends ParserRuleContext {
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class BooleanValueTokContext extends ParserRuleContext {
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TableOrPartitionContext extends ParserRuleContext {
    tableName(): TableNameContext;
    partitionSpec(): PartitionSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartitionSpecContext extends ParserRuleContext {
    KW_PARTITION(): TerminalNode;
    LPAREN(): TerminalNode;
    partitionVal(): PartitionValContext[];
    partitionVal(i: number): PartitionValContext;
    RPAREN(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartitionValContext extends ParserRuleContext {
    id_(): Id_Context;
    EQUAL(): TerminalNode | undefined;
    constant(): ConstantContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartitionSelectorSpecContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    partitionSelectorVal(): PartitionSelectorValContext[];
    partitionSelectorVal(i: number): PartitionSelectorValContext;
    RPAREN(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartitionSelectorValContext extends ParserRuleContext {
    id_(): Id_Context;
    partitionSelectorOperator(): PartitionSelectorOperatorContext;
    constant(): ConstantContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PartitionSelectorOperatorContext extends ParserRuleContext {
    KW_LIKE(): TerminalNode | undefined;
    subQuerySelectorOperator(): SubQuerySelectorOperatorContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SubQuerySelectorOperatorContext extends ParserRuleContext {
    EQUAL(): TerminalNode | undefined;
    NOTEQUAL(): TerminalNode | undefined;
    LESSTHANOREQUALTO(): TerminalNode | undefined;
    LESSTHAN(): TerminalNode | undefined;
    GREATERTHANOREQUALTO(): TerminalNode | undefined;
    GREATERTHAN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SysFuncNamesContext extends ParserRuleContext {
    KW_AND(): TerminalNode | undefined;
    KW_OR(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    KW_LIKE(): TerminalNode | undefined;
    KW_IF(): TerminalNode | undefined;
    KW_CASE(): TerminalNode | undefined;
    KW_WHEN(): TerminalNode | undefined;
    KW_FLOOR(): TerminalNode | undefined;
    KW_TINYINT(): TerminalNode | undefined;
    KW_SMALLINT(): TerminalNode | undefined;
    KW_INT(): TerminalNode | undefined;
    KW_INTEGER(): TerminalNode | undefined;
    KW_BIGINT(): TerminalNode | undefined;
    KW_FLOAT(): TerminalNode | undefined;
    KW_REAL(): TerminalNode | undefined;
    KW_DOUBLE(): TerminalNode | undefined;
    KW_BOOLEAN(): TerminalNode | undefined;
    KW_STRING(): TerminalNode | undefined;
    KW_BINARY(): TerminalNode | undefined;
    KW_ARRAY(): TerminalNode | undefined;
    KW_MAP(): TerminalNode | undefined;
    KW_STRUCT(): TerminalNode | undefined;
    KW_UNIONTYPE(): TerminalNode | undefined;
    EQUAL(): TerminalNode | undefined;
    EQUAL_NS(): TerminalNode | undefined;
    NOTEQUAL(): TerminalNode | undefined;
    LESSTHANOREQUALTO(): TerminalNode | undefined;
    LESSTHAN(): TerminalNode | undefined;
    GREATERTHANOREQUALTO(): TerminalNode | undefined;
    GREATERTHAN(): TerminalNode | undefined;
    DIVIDE(): TerminalNode | undefined;
    PLUS(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    STAR(): TerminalNode | undefined;
    MOD(): TerminalNode | undefined;
    DIV(): TerminalNode | undefined;
    AMPERSAND(): TerminalNode | undefined;
    TILDE(): TerminalNode | undefined;
    BITWISEOR(): TerminalNode | undefined;
    BITWISEXOR(): TerminalNode | undefined;
    KW_RLIKE(): TerminalNode | undefined;
    KW_REGEXP(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_BETWEEN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Id_Context extends ParserRuleContext {
    Identifier(): TerminalNode | undefined;
    nonReserved(): NonReservedContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class FunctionIdentifierContext extends ParserRuleContext {
    _fn: Id_Context;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    DOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrincipalIdentifierContext extends ParserRuleContext {
    id_(): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class NonReservedContext extends ParserRuleContext {
    KW_ABORT(): TerminalNode | undefined;
    KW_ACTIVATE(): TerminalNode | undefined;
    KW_ACTIVE(): TerminalNode | undefined;
    KW_ADD(): TerminalNode | undefined;
    KW_ADMIN(): TerminalNode | undefined;
    KW_AFTER(): TerminalNode | undefined;
    KW_ALLOC_FRACTION(): TerminalNode | undefined;
    KW_ANALYZE(): TerminalNode | undefined;
    KW_ARCHIVE(): TerminalNode | undefined;
    KW_ASC(): TerminalNode | undefined;
    KW_AST(): TerminalNode | undefined;
    KW_AT(): TerminalNode | undefined;
    KW_AUTOCOMMIT(): TerminalNode | undefined;
    KW_BATCH(): TerminalNode | undefined;
    KW_BEFORE(): TerminalNode | undefined;
    KW_BUCKET(): TerminalNode | undefined;
    KW_BUCKETS(): TerminalNode | undefined;
    KW_CACHE(): TerminalNode | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    KW_CBO(): TerminalNode | undefined;
    KW_CHANGE(): TerminalNode | undefined;
    KW_CHECK(): TerminalNode | undefined;
    KW_CLUSTER(): TerminalNode | undefined;
    KW_CLUSTERED(): TerminalNode | undefined;
    KW_CLUSTERSTATUS(): TerminalNode | undefined;
    KW_COLLECTION(): TerminalNode | undefined;
    KW_COLUMNS(): TerminalNode | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    KW_COMPACT(): TerminalNode | undefined;
    KW_COMPACTIONS(): TerminalNode | undefined;
    KW_COMPUTE(): TerminalNode | undefined;
    KW_CONCATENATE(): TerminalNode | undefined;
    KW_CONTINUE(): TerminalNode | undefined;
    KW_COST(): TerminalNode | undefined;
    KW_CRON(): TerminalNode | undefined;
    KW_DATA(): TerminalNode | undefined;
    KW_DATABASES(): TerminalNode | undefined;
    KW_DATETIME(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_DAYS(): TerminalNode | undefined;
    KW_DAYOFWEEK(): TerminalNode | undefined;
    KW_DBPROPERTIES(): TerminalNode | undefined;
    KW_DCPROPERTIES(): TerminalNode | undefined;
    KW_DEBUG(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_DEFERRED(): TerminalNode | undefined;
    KW_DEFINED(): TerminalNode | undefined;
    KW_DELIMITED(): TerminalNode | undefined;
    KW_DEPENDENCY(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    KW_DETAIL(): TerminalNode | undefined;
    KW_DIRECTORIES(): TerminalNode | undefined;
    KW_DIRECTORY(): TerminalNode | undefined;
    KW_DISABLE(): TerminalNode | undefined;
    KW_DISABLED(): TerminalNode | undefined;
    KW_DISTRIBUTE(): TerminalNode | undefined;
    KW_DISTRIBUTED(): TerminalNode | undefined;
    KW_DO(): TerminalNode | undefined;
    KW_DUMP(): TerminalNode | undefined;
    KW_ELEM_TYPE(): TerminalNode | undefined;
    KW_ENABLE(): TerminalNode | undefined;
    KW_ENABLED(): TerminalNode | undefined;
    KW_ENFORCED(): TerminalNode | undefined;
    KW_ESCAPED(): TerminalNode | undefined;
    KW_EVERY(): TerminalNode | undefined;
    KW_EXCLUSIVE(): TerminalNode | undefined;
    KW_EXECUTE(): TerminalNode | undefined;
    KW_EXECUTED(): TerminalNode | undefined;
    KW_EXPIRE_SNAPSHOTS(): TerminalNode | undefined;
    KW_EXPLAIN(): TerminalNode | undefined;
    KW_EXPORT(): TerminalNode | undefined;
    KW_EXPRESSION(): TerminalNode | undefined;
    KW_FIELDS(): TerminalNode | undefined;
    KW_FILE(): TerminalNode | undefined;
    KW_FILEFORMAT(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_FORMAT(): TerminalNode | undefined;
    KW_FORMATTED(): TerminalNode | undefined;
    KW_FUNCTIONS(): TerminalNode | undefined;
    KW_HOLD_DDLTIME(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_HOURS(): TerminalNode | undefined;
    KW_IDXPROPERTIES(): TerminalNode | undefined;
    KW_IGNORE(): TerminalNode | undefined;
    KW_INDEX(): TerminalNode | undefined;
    KW_INDEXES(): TerminalNode | undefined;
    KW_INPATH(): TerminalNode | undefined;
    KW_INPUTDRIVER(): TerminalNode | undefined;
    KW_INPUTFORMAT(): TerminalNode | undefined;
    KW_ISOLATION(): TerminalNode | undefined;
    KW_ITEMS(): TerminalNode | undefined;
    KW_JAR(): TerminalNode | undefined;
    KW_JOINCOST(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    KW_KEYS(): TerminalNode | undefined;
    KW_KEY_TYPE(): TerminalNode | undefined;
    KW_KILL(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    KW_LEVEL(): TerminalNode | undefined;
    KW_LIMIT(): TerminalNode | undefined;
    KW_LINES(): TerminalNode | undefined;
    KW_LOAD(): TerminalNode | undefined;
    KW_LOCATION(): TerminalNode | undefined;
    KW_LOCK(): TerminalNode | undefined;
    KW_LOCKS(): TerminalNode | undefined;
    KW_LOGICAL(): TerminalNode | undefined;
    KW_LONG(): TerminalNode | undefined;
    KW_MANAGED(): TerminalNode | undefined;
    KW_MANAGEDLOCATION(): TerminalNode | undefined;
    KW_MANAGEMENT(): TerminalNode | undefined;
    KW_MAPJOIN(): TerminalNode | undefined;
    KW_MAPPING(): TerminalNode | undefined;
    KW_MATCHED(): TerminalNode | undefined;
    KW_MATERIALIZED(): TerminalNode | undefined;
    KW_METADATA(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    KW_MINUTES(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_MONTHS(): TerminalNode | undefined;
    KW_MOVE(): TerminalNode | undefined;
    KW_MSCK(): TerminalNode | undefined;
    KW_NORELY(): TerminalNode | undefined;
    KW_NOSCAN(): TerminalNode | undefined;
    KW_NOVALIDATE(): TerminalNode | undefined;
    KW_NO_DROP(): TerminalNode | undefined;
    KW_NULLS(): TerminalNode | undefined;
    KW_OFFLINE(): TerminalNode | undefined;
    KW_OFFSET(): TerminalNode | undefined;
    KW_OPERATOR(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_OUTPUTDRIVER(): TerminalNode | undefined;
    KW_OUTPUTFORMAT(): TerminalNode | undefined;
    KW_OVERWRITE(): TerminalNode | undefined;
    KW_OWNER(): TerminalNode | undefined;
    KW_PARTITIONED(): TerminalNode | undefined;
    KW_PARTITIONS(): TerminalNode | undefined;
    KW_PATH(): TerminalNode | undefined;
    KW_PLAN(): TerminalNode | undefined;
    KW_PLANS(): TerminalNode | undefined;
    KW_PLUS(): TerminalNode | undefined;
    KW_POOL(): TerminalNode | undefined;
    KW_PRINCIPALS(): TerminalNode | undefined;
    KW_PROTECTION(): TerminalNode | undefined;
    KW_PURGE(): TerminalNode | undefined;
    KW_QUARTER(): TerminalNode | undefined;
    KW_QUERY(): TerminalNode | undefined;
    KW_QUERY_PARALLELISM(): TerminalNode | undefined;
    KW_READ(): TerminalNode | undefined;
    KW_READONLY(): TerminalNode | undefined;
    KW_REBUILD(): TerminalNode | undefined;
    KW_RECORDREADER(): TerminalNode | undefined;
    KW_RECORDWRITER(): TerminalNode | undefined;
    KW_RELOAD(): TerminalNode | undefined;
    KW_RELY(): TerminalNode | undefined;
    KW_REMOTE(): TerminalNode | undefined;
    KW_RENAME(): TerminalNode | undefined;
    KW_REOPTIMIZATION(): TerminalNode | undefined;
    KW_REPAIR(): TerminalNode | undefined;
    KW_REPL(): TerminalNode | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    KW_REPLICATION(): TerminalNode | undefined;
    KW_RESOURCE(): TerminalNode | undefined;
    KW_RESPECT(): TerminalNode | undefined;
    KW_RESTRICT(): TerminalNode | undefined;
    KW_REWRITE(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_ROLES(): TerminalNode | undefined;
    KW_SCHEDULED(): TerminalNode | undefined;
    KW_SCHEDULING_POLICY(): TerminalNode | undefined;
    KW_SCHEMA(): TerminalNode | undefined;
    KW_SCHEMAS(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    KW_SECONDS(): TerminalNode | undefined;
    KW_SEMI(): TerminalNode | undefined;
    KW_SERDE(): TerminalNode | undefined;
    KW_SERDEPROPERTIES(): TerminalNode | undefined;
    KW_SERVER(): TerminalNode | undefined;
    KW_SETS(): TerminalNode | undefined;
    KW_SET_CURRENT_SNAPSHOT(): TerminalNode | undefined;
    KW_SHARED(): TerminalNode | undefined;
    KW_SHOW(): TerminalNode | undefined;
    KW_SHOW_DATABASE(): TerminalNode | undefined;
    KW_SKEWED(): TerminalNode | undefined;
    KW_SNAPSHOT(): TerminalNode | undefined;
    KW_SORT(): TerminalNode | undefined;
    KW_SORTED(): TerminalNode | undefined;
    KW_SPEC(): TerminalNode | undefined;
    KW_SSL(): TerminalNode | undefined;
    KW_STATISTICS(): TerminalNode | undefined;
    KW_STATUS(): TerminalNode | undefined;
    KW_STORED(): TerminalNode | undefined;
    KW_STREAMTABLE(): TerminalNode | undefined;
    KW_STRING(): TerminalNode | undefined;
    KW_STRUCT(): TerminalNode | undefined;
    KW_SUMMARY(): TerminalNode | undefined;
    KW_SYSTEM_TIME(): TerminalNode | undefined;
    KW_SYSTEM_VERSION(): TerminalNode | undefined;
    KW_TABLES(): TerminalNode | undefined;
    KW_TBLPROPERTIES(): TerminalNode | undefined;
    KW_TEMPORARY(): TerminalNode | undefined;
    KW_TERMINATED(): TerminalNode | undefined;
    KW_TIMESTAMPTZ(): TerminalNode | undefined;
    KW_TINYINT(): TerminalNode | undefined;
    KW_TOUCH(): TerminalNode | undefined;
    KW_TRANSACTION(): TerminalNode | undefined;
    KW_TRANSACTIONAL(): TerminalNode | undefined;
    KW_TRANSACTIONS(): TerminalNode | undefined;
    KW_TRIM(): TerminalNode | undefined;
    KW_TYPE(): TerminalNode | undefined;
    KW_UNARCHIVE(): TerminalNode | undefined;
    KW_UNDO(): TerminalNode | undefined;
    KW_UNIONTYPE(): TerminalNode | undefined;
    KW_UNKNOWN(): TerminalNode | undefined;
    KW_UNLOCK(): TerminalNode | undefined;
    KW_UNMANAGED(): TerminalNode | undefined;
    KW_UNSET(): TerminalNode | undefined;
    KW_UNSIGNED(): TerminalNode | undefined;
    KW_URI(): TerminalNode | undefined;
    KW_URL(): TerminalNode | undefined;
    KW_USE(): TerminalNode | undefined;
    KW_UTC(): TerminalNode | undefined;
    KW_UTCTIMESTAMP(): TerminalNode | undefined;
    KW_VALIDATE(): TerminalNode | undefined;
    KW_VALUE_TYPE(): TerminalNode | undefined;
    KW_VECTORIZATION(): TerminalNode | undefined;
    KW_VIEW(): TerminalNode | undefined;
    KW_VIEWS(): TerminalNode | undefined;
    KW_WAIT(): TerminalNode | undefined;
    KW_WEEK(): TerminalNode | undefined;
    KW_WEEKS(): TerminalNode | undefined;
    KW_WHILE(): TerminalNode | undefined;
    KW_WITHIN(): TerminalNode | undefined;
    KW_WORK(): TerminalNode | undefined;
    KW_WORKLOAD(): TerminalNode | undefined;
    KW_WRITE(): TerminalNode | undefined;
    KW_YEAR(): TerminalNode | undefined;
    KW_YEARS(): TerminalNode | undefined;
    KW_ZONE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class Sql11ReservedKeywordsUsedAsFunctionNameContext extends ParserRuleContext {
    KW_ARRAY(): TerminalNode | undefined;
    KW_BIGINT(): TerminalNode | undefined;
    KW_BINARY(): TerminalNode | undefined;
    KW_BOOLEAN(): TerminalNode | undefined;
    KW_CURRENT_DATE(): TerminalNode | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    KW_DATE(): TerminalNode | undefined;
    KW_DOUBLE(): TerminalNode | undefined;
    KW_FLOAT(): TerminalNode | undefined;
    KW_GROUPING(): TerminalNode | undefined;
    KW_IF(): TerminalNode | undefined;
    KW_INT(): TerminalNode | undefined;
    KW_INTEGER(): TerminalNode | undefined;
    KW_MAP(): TerminalNode | undefined;
    KW_REAL(): TerminalNode | undefined;
    KW_SMALLINT(): TerminalNode | undefined;
    KW_TIMESTAMP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class HintContext extends ParserRuleContext {
    hintList(): HintListContext;
    EOF(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class HintListContext extends ParserRuleContext {
    hintItem(): HintItemContext[];
    hintItem(i: number): HintItemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class HintItemContext extends ParserRuleContext {
    hintName(): HintNameContext;
    LPAREN(): TerminalNode | undefined;
    hintArgs(): HintArgsContext | undefined;
    RPAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class HintNameContext extends ParserRuleContext {
    KW_MAPJOIN(): TerminalNode | undefined;
    KW_SEMI(): TerminalNode | undefined;
    KW_STREAMTABLE(): TerminalNode | undefined;
    KW_PKFK_JOIN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class HintArgsContext extends ParserRuleContext {
    hintArgName(): HintArgNameContext[];
    hintArgName(i: number): HintArgNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class HintArgNameContext extends ParserRuleContext {
    Identifier(): TerminalNode | undefined;
    Number(): TerminalNode | undefined;
    KW_NONE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PrepareStatementContext extends ParserRuleContext {
    KW_PREPARE(): TerminalNode;
    id_(): Id_Context;
    KW_FROM(): TerminalNode;
    queryStatementExpression(): QueryStatementExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExecuteStatementContext extends ParserRuleContext {
    KW_EXECUTE(): TerminalNode;
    id_(): Id_Context;
    KW_USING(): TerminalNode;
    executeParamList(): ExecuteParamListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ExecuteParamListContext extends ParserRuleContext {
    constant(): ConstantContext[];
    constant(i: number): ConstantContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ResourcePlanDdlStatementsContext extends ParserRuleContext {
    createResourcePlanStatement(): CreateResourcePlanStatementContext | undefined;
    alterResourcePlanStatement(): AlterResourcePlanStatementContext | undefined;
    dropResourcePlanStatement(): DropResourcePlanStatementContext | undefined;
    globalWmStatement(): GlobalWmStatementContext | undefined;
    replaceResourcePlanStatement(): ReplaceResourcePlanStatementContext | undefined;
    createTriggerStatement(): CreateTriggerStatementContext | undefined;
    alterTriggerStatement(): AlterTriggerStatementContext | undefined;
    dropTriggerStatement(): DropTriggerStatementContext | undefined;
    createPoolStatement(): CreatePoolStatementContext | undefined;
    alterPoolStatement(): AlterPoolStatementContext | undefined;
    dropPoolStatement(): DropPoolStatementContext | undefined;
    createMappingStatement(): CreateMappingStatementContext | undefined;
    alterMappingStatement(): AlterMappingStatementContext | undefined;
    dropMappingStatement(): DropMappingStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RpAssignContext extends ParserRuleContext {
    _parallelism: Token;
    KW_QUERY_PARALLELISM(): TerminalNode | undefined;
    EQUAL(): TerminalNode;
    Number(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_POOL(): TerminalNode | undefined;
    poolPath(): PoolPathContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RpAssignListContext extends ParserRuleContext {
    rpAssign(): RpAssignContext[];
    rpAssign(i: number): RpAssignContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RpUnassignContext extends ParserRuleContext {
    KW_QUERY_PARALLELISM(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_POOL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class RpUnassignListContext extends ParserRuleContext {
    rpUnassign(): RpUnassignContext[];
    rpUnassign(i: number): RpUnassignContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateResourcePlanStatementContext extends ParserRuleContext {
    _name: Id_Context;
    _likeName: Id_Context;
    KW_CREATE(): TerminalNode;
    KW_RESOURCE(): TerminalNode;
    KW_PLAN(): TerminalNode;
    KW_LIKE(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    KW_WITH(): TerminalNode | undefined;
    rpAssignList(): RpAssignListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class WithReplaceContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    KW_REPLACE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ActivateContext extends ParserRuleContext {
    KW_ACTIVATE(): TerminalNode;
    withReplace(): WithReplaceContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class EnableContext extends ParserRuleContext {
    KW_ENABLE(): TerminalNode | undefined;
    KW_ENABLED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DisableContext extends ParserRuleContext {
    KW_DISABLE(): TerminalNode | undefined;
    KW_DISABLED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class UnmanagedContext extends ParserRuleContext {
    KW_UNMANAGED(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class YearContext extends ParserRuleContext {
    KW_YEAR(): TerminalNode | undefined;
    KW_YEARS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class MonthContext extends ParserRuleContext {
    KW_MONTH(): TerminalNode | undefined;
    KW_MONTHS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class WeekContext extends ParserRuleContext {
    KW_WEEK(): TerminalNode | undefined;
    KW_WEEKS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DayContext extends ParserRuleContext {
    KW_DAY(): TerminalNode | undefined;
    KW_DAYS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class HourContext extends ParserRuleContext {
    KW_HOUR(): TerminalNode | undefined;
    KW_HOURS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class MinuteContext extends ParserRuleContext {
    KW_MINUTE(): TerminalNode | undefined;
    KW_MINUTES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class SecondContext extends ParserRuleContext {
    KW_SECOND(): TerminalNode | undefined;
    KW_SECONDS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DecimalContext extends ParserRuleContext {
    KW_DEC(): TerminalNode | undefined;
    KW_DECIMAL(): TerminalNode | undefined;
    KW_NUMERIC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterResourcePlanStatementContext extends ParserRuleContext {
    _name: Id_Context;
    _newName: Id_Context;
    KW_ALTER(): TerminalNode;
    KW_RESOURCE(): TerminalNode;
    KW_PLAN(): TerminalNode;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    KW_VALIDATE(): TerminalNode | undefined;
    disable(): DisableContext | undefined;
    KW_SET(): TerminalNode | undefined;
    rpAssignList(): RpAssignListContext | undefined;
    KW_UNSET(): TerminalNode | undefined;
    rpUnassignList(): RpUnassignListContext | undefined;
    KW_RENAME(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    activate(): ActivateContext | undefined;
    enable(): EnableContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class GlobalWmStatementContext extends ParserRuleContext {
    KW_WORKLOAD(): TerminalNode;
    KW_MANAGEMENT(): TerminalNode;
    enable(): EnableContext | undefined;
    disable(): DisableContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ReplaceResourcePlanStatementContext extends ParserRuleContext {
    _src: Id_Context;
    _dest: Id_Context;
    KW_REPLACE(): TerminalNode;
    KW_ACTIVE(): TerminalNode | undefined;
    KW_RESOURCE(): TerminalNode | undefined;
    KW_PLAN(): TerminalNode | undefined;
    KW_WITH(): TerminalNode | undefined;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropResourcePlanStatementContext extends ParserRuleContext {
    _name: Id_Context;
    KW_DROP(): TerminalNode;
    KW_RESOURCE(): TerminalNode;
    KW_PLAN(): TerminalNode;
    id_(): Id_Context;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PoolPathContext extends ParserRuleContext {
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TriggerExpressionContext extends ParserRuleContext {
    triggerAtomExpression(): TriggerAtomExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TriggerExpressionStandaloneContext extends ParserRuleContext {
    triggerExpression(): TriggerExpressionContext;
    EOF(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TriggerOrExpressionContext extends ParserRuleContext {
    triggerAndExpression(): TriggerAndExpressionContext[];
    triggerAndExpression(i: number): TriggerAndExpressionContext;
    KW_OR(): TerminalNode[];
    KW_OR(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TriggerAndExpressionContext extends ParserRuleContext {
    triggerAtomExpression(): TriggerAtomExpressionContext[];
    triggerAtomExpression(i: number): TriggerAtomExpressionContext;
    KW_AND(): TerminalNode[];
    KW_AND(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TriggerAtomExpressionContext extends ParserRuleContext {
    id_(): Id_Context;
    comparisionOperator(): ComparisionOperatorContext;
    triggerLiteral(): TriggerLiteralContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TriggerLiteralContext extends ParserRuleContext {
    Number(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class ComparisionOperatorContext extends ParserRuleContext {
    GREATERTHAN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TriggerActionExpressionContext extends ParserRuleContext {
    KW_KILL(): TerminalNode | undefined;
    KW_MOVE(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    poolPath(): PoolPathContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class TriggerActionExpressionStandaloneContext extends ParserRuleContext {
    triggerActionExpression(): TriggerActionExpressionContext;
    EOF(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateTriggerStatementContext extends ParserRuleContext {
    _rpName: Id_Context;
    _triggerName: Id_Context;
    KW_CREATE(): TerminalNode;
    KW_TRIGGER(): TerminalNode;
    DOT(): TerminalNode;
    KW_WHEN(): TerminalNode;
    triggerExpression(): TriggerExpressionContext;
    KW_DO(): TerminalNode;
    triggerActionExpression(): TriggerActionExpressionContext;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterTriggerStatementContext extends ParserRuleContext {
    _rpName: Id_Context;
    _triggerName: Id_Context;
    _poolName: PoolPathContext;
    KW_ALTER(): TerminalNode;
    KW_TRIGGER(): TerminalNode;
    DOT(): TerminalNode;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    KW_WHEN(): TerminalNode | undefined;
    triggerExpression(): TriggerExpressionContext | undefined;
    KW_DO(): TerminalNode | undefined;
    triggerActionExpression(): TriggerActionExpressionContext | undefined;
    KW_ADD(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_FROM(): TerminalNode | undefined;
    KW_POOL(): TerminalNode | undefined;
    KW_UNMANAGED(): TerminalNode | undefined;
    poolPath(): PoolPathContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropTriggerStatementContext extends ParserRuleContext {
    _rpName: Id_Context;
    _triggerName: Id_Context;
    KW_DROP(): TerminalNode;
    KW_TRIGGER(): TerminalNode;
    DOT(): TerminalNode;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PoolAssignContext extends ParserRuleContext {
    _allocFraction: Token;
    _parallelism: Token;
    _policy: Token;
    _path: PoolPathContext;
    KW_ALLOC_FRACTION(): TerminalNode | undefined;
    EQUAL(): TerminalNode | undefined;
    KW_QUERY_PARALLELISM(): TerminalNode | undefined;
    KW_SCHEDULING_POLICY(): TerminalNode | undefined;
    KW_PATH(): TerminalNode | undefined;
    Number(): TerminalNode | undefined;
    StringLiteral(): TerminalNode | undefined;
    poolPath(): PoolPathContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class PoolAssignListContext extends ParserRuleContext {
    poolAssign(): PoolAssignContext[];
    poolAssign(i: number): PoolAssignContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreatePoolStatementContext extends ParserRuleContext {
    _rpName: Id_Context;
    KW_CREATE(): TerminalNode;
    KW_POOL(): TerminalNode;
    DOT(): TerminalNode;
    poolPath(): PoolPathContext;
    KW_WITH(): TerminalNode;
    poolAssignList(): PoolAssignListContext;
    id_(): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterPoolStatementContext extends ParserRuleContext {
    _rpName: Id_Context;
    _triggerName: Id_Context;
    KW_ALTER(): TerminalNode;
    KW_POOL(): TerminalNode;
    DOT(): TerminalNode;
    poolPath(): PoolPathContext;
    id_(): Id_Context[];
    id_(i: number): Id_Context;
    KW_SET(): TerminalNode | undefined;
    poolAssignList(): PoolAssignListContext | undefined;
    KW_UNSET(): TerminalNode | undefined;
    KW_SCHEDULING_POLICY(): TerminalNode | undefined;
    KW_TRIGGER(): TerminalNode | undefined;
    KW_ADD(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropPoolStatementContext extends ParserRuleContext {
    _rpName: Id_Context;
    KW_DROP(): TerminalNode;
    KW_POOL(): TerminalNode;
    DOT(): TerminalNode;
    poolPath(): PoolPathContext;
    id_(): Id_Context;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class CreateMappingStatementContext extends ParserRuleContext {
    _mappingType: Token;
    _name: Token;
    _rpName: Id_Context;
    _path: PoolPathContext;
    _order: Token;
    KW_CREATE(): TerminalNode;
    KW_MAPPING(): TerminalNode;
    KW_IN(): TerminalNode;
    StringLiteral(): TerminalNode;
    id_(): Id_Context;
    KW_USER(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_APPLICATION(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    unmanaged(): UnmanagedContext | undefined;
    poolPath(): PoolPathContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_ORDER(): TerminalNode | undefined;
    Number(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class AlterMappingStatementContext extends ParserRuleContext {
    _mappingType: Token;
    _name: Token;
    _rpName: Id_Context;
    _path: PoolPathContext;
    _order: Token;
    KW_ALTER(): TerminalNode;
    KW_MAPPING(): TerminalNode;
    KW_IN(): TerminalNode;
    StringLiteral(): TerminalNode;
    id_(): Id_Context;
    KW_USER(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_APPLICATION(): TerminalNode | undefined;
    KW_TO(): TerminalNode | undefined;
    unmanaged(): UnmanagedContext | undefined;
    poolPath(): PoolPathContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_ORDER(): TerminalNode | undefined;
    Number(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
export declare class DropMappingStatementContext extends ParserRuleContext {
    _mappingType: Token;
    _name: Token;
    _rpName: Id_Context;
    KW_DROP(): TerminalNode;
    KW_MAPPING(): TerminalNode;
    KW_IN(): TerminalNode;
    StringLiteral(): TerminalNode;
    id_(): Id_Context;
    KW_USER(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_APPLICATION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: HiveSqlParserListener): void;
    exitRule(listener: HiveSqlParserListener): void;
    accept<Result>(visitor: HiveSqlParserVisitor<Result>): Result;
}
