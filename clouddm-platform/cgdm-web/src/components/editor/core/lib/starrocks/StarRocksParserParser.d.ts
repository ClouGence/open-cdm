import { ATN } from "antlr4ts/atn/ATN";
import { FailedPredicateException } from "antlr4ts/FailedPredicateException";
import { Parser } from "antlr4ts/Parser";
import { ParserRuleContext } from "antlr4ts/ParserRuleContext";
import { RuleContext } from "antlr4ts/RuleContext";
import { TerminalNode } from "antlr4ts/tree/TerminalNode";
import { Token } from "antlr4ts/Token";
import { TokenStream } from "antlr4ts/TokenStream";
import { Vocabulary } from "antlr4ts/Vocabulary";
import { StarRocksParserListener } from "./StarRocksParserListener";
import { StarRocksParserVisitor } from "./StarRocksParserVisitor";
export declare class StarRocksParserParser extends Parser {
    static readonly T__0 = 1;
    static readonly T__1 = 2;
    static readonly T__2 = 3;
    static readonly T__3 = 4;
    static readonly T__4 = 5;
    static readonly T__5 = 6;
    static readonly T__6 = 7;
    static readonly T__7 = 8;
    static readonly T__8 = 9;
    static readonly T__9 = 10;
    static readonly T__10 = 11;
    static readonly ACCESS = 12;
    static readonly ACTIVE = 13;
    static readonly ADD = 14;
    static readonly ADMIN = 15;
    static readonly AFTER = 16;
    static readonly AGGREGATE = 17;
    static readonly ALL = 18;
    static readonly ALTER = 19;
    static readonly ANALYZE = 20;
    static readonly AND = 21;
    static readonly ANTI = 22;
    static readonly APPLY = 23;
    static readonly ARRAY = 24;
    static readonly ARRAY_AGG = 25;
    static readonly AS = 26;
    static readonly ASC = 27;
    static readonly ASYNC = 28;
    static readonly AUTHORS = 29;
    static readonly AUTHENTICATION = 30;
    static readonly AUTO_INCREMENT = 31;
    static readonly AVG = 32;
    static readonly BACKEND = 33;
    static readonly BACKENDS = 34;
    static readonly BACKUP = 35;
    static readonly BASE = 36;
    static readonly BEGIN = 37;
    static readonly BETWEEN = 38;
    static readonly BIGINT = 39;
    static readonly BINARY = 40;
    static readonly BITMAP = 41;
    static readonly BITMAP_UNION = 42;
    static readonly BLACKLIST = 43;
    static readonly BODY = 44;
    static readonly BOOLEAN = 45;
    static readonly BOTH = 46;
    static readonly BROKER = 47;
    static readonly BUCKETS = 48;
    static readonly BUILTIN = 49;
    static readonly BY = 50;
    static readonly CANCEL = 51;
    static readonly CASE = 52;
    static readonly CAST = 53;
    static readonly CATALOG = 54;
    static readonly CATALOGS = 55;
    static readonly CEIL = 56;
    static readonly CHAIN = 57;
    static readonly CHAR = 58;
    static readonly CHARACTER = 59;
    static readonly CHARSET = 60;
    static readonly CHECK = 61;
    static readonly CLEAN = 62;
    static readonly CLEAR = 63;
    static readonly CLUSTER = 64;
    static readonly CLUSTERS = 65;
    static readonly COLLATE = 66;
    static readonly COLLATION = 67;
    static readonly COLUMN = 68;
    static readonly COLUMNS = 69;
    static readonly COMMENT = 70;
    static readonly COMMIT = 71;
    static readonly COMMITTED = 72;
    static readonly COMPACT = 73;
    static readonly COMPACTION = 74;
    static readonly COMPUTE = 75;
    static readonly CONFIG = 76;
    static readonly CONNECTION = 77;
    static readonly CONSISTENT = 78;
    static readonly CONVERT = 79;
    static readonly COSTS = 80;
    static readonly COUNT = 81;
    static readonly CREATE = 82;
    static readonly CROSS = 83;
    static readonly CUBE = 84;
    static readonly CUME_DIST = 85;
    static readonly CUMULATIVE = 86;
    static readonly CURRENT = 87;
    static readonly CURRENT_DATE = 88;
    static readonly CURRENT_ROLE = 89;
    static readonly CURRENT_TIME = 90;
    static readonly CURRENT_TIMESTAMP = 91;
    static readonly CURRENT_USER = 92;
    static readonly DATA = 93;
    static readonly DATACACHE = 94;
    static readonly DATABASE = 95;
    static readonly DATABASES = 96;
    static readonly DATE = 97;
    static readonly DATETIME = 98;
    static readonly DAY = 99;
    static readonly DEALLOCATE = 100;
    static readonly DECIMAL = 101;
    static readonly DECIMALV2 = 102;
    static readonly DECIMAL32 = 103;
    static readonly DECIMAL64 = 104;
    static readonly DECIMAL128 = 105;
    static readonly DECOMMISSION = 106;
    static readonly DEFAULT = 107;
    static readonly DELETE = 108;
    static readonly DENSE_RANK = 109;
    static readonly DEFERRED = 110;
    static readonly NTILE = 111;
    static readonly DESC = 112;
    static readonly DESCRIBE = 113;
    static readonly DISABLE = 114;
    static readonly DISTINCT = 115;
    static readonly DISTRIBUTED = 116;
    static readonly DISTRIBUTION = 117;
    static readonly DOUBLE = 118;
    static readonly DROP = 119;
    static readonly DUAL = 120;
    static readonly DUPLICATE = 121;
    static readonly DYNAMIC = 122;
    static readonly ELSE = 123;
    static readonly ENABLE = 124;
    static readonly ENCLOSE = 125;
    static readonly END = 126;
    static readonly ENGINE = 127;
    static readonly ENGINES = 128;
    static readonly ERRORS = 129;
    static readonly ESCAPE = 130;
    static readonly EVENTS = 131;
    static readonly EXCEPT = 132;
    static readonly EXECUTE = 133;
    static readonly EXISTS = 134;
    static readonly EXPLAIN = 135;
    static readonly EXPORT = 136;
    static readonly EXTERNAL = 137;
    static readonly EXTRACT = 138;
    static readonly EVERY = 139;
    static readonly FAILPOINT = 140;
    static readonly FAILPOINTS = 141;
    static readonly FALSE = 142;
    static readonly FIELDS = 143;
    static readonly FILE = 144;
    static readonly FILES = 145;
    static readonly FILTER = 146;
    static readonly FIRST = 147;
    static readonly FIRST_VALUE = 148;
    static readonly FLOAT = 149;
    static readonly FLOOR = 150;
    static readonly FN = 151;
    static readonly FOLLOWING = 152;
    static readonly FOLLOWER = 153;
    static readonly FOR = 154;
    static readonly FORCE = 155;
    static readonly FORMAT = 156;
    static readonly FREE = 157;
    static readonly FROM = 158;
    static readonly FRONTEND = 159;
    static readonly FRONTENDS = 160;
    static readonly FULL = 161;
    static readonly FUNCTION = 162;
    static readonly FUNCTIONS = 163;
    static readonly GLOBAL = 164;
    static readonly GRANT = 165;
    static readonly GRANTS = 166;
    static readonly GROUP = 167;
    static readonly GROUPS = 168;
    static readonly GROUPING = 169;
    static readonly GROUPING_ID = 170;
    static readonly GROUP_CONCAT = 171;
    static readonly HASH = 172;
    static readonly HAVING = 173;
    static readonly HELP = 174;
    static readonly HISTOGRAM = 175;
    static readonly HLL = 176;
    static readonly HLL_UNION = 177;
    static readonly HOST = 178;
    static readonly HOUR = 179;
    static readonly HUB = 180;
    static readonly IDENTIFIED = 181;
    static readonly IF = 182;
    static readonly IMPERSONATE = 183;
    static readonly IMMEDIATE = 184;
    static readonly IGNORE = 185;
    static readonly IMAGE = 186;
    static readonly IN = 187;
    static readonly INACTIVE = 188;
    static readonly INCREMENTAL = 189;
    static readonly INDEX = 190;
    static readonly INDEXES = 191;
    static readonly INFILE = 192;
    static readonly INNER = 193;
    static readonly INSTALL = 194;
    static readonly INSERT = 195;
    static readonly INT = 196;
    static readonly INTEGER = 197;
    static readonly INTEGRATION = 198;
    static readonly INTEGRATIONS = 199;
    static readonly INTERMEDIATE = 200;
    static readonly INTERSECT = 201;
    static readonly INTERVAL = 202;
    static readonly INTO = 203;
    static readonly GIN = 204;
    static readonly OVERWRITE = 205;
    static readonly IS = 206;
    static readonly ISOLATION = 207;
    static readonly JOB = 208;
    static readonly JOIN = 209;
    static readonly JSON = 210;
    static readonly KEY = 211;
    static readonly KEYS = 212;
    static readonly KILL = 213;
    static readonly LABEL = 214;
    static readonly LAG = 215;
    static readonly LARGEINT = 216;
    static readonly LAST = 217;
    static readonly LAST_VALUE = 218;
    static readonly LATERAL = 219;
    static readonly LEAD = 220;
    static readonly LEFT = 221;
    static readonly LESS = 222;
    static readonly LEVEL = 223;
    static readonly LIKE = 224;
    static readonly LIMIT = 225;
    static readonly LIST = 226;
    static readonly LOAD = 227;
    static readonly LOCAL = 228;
    static readonly LOCALTIME = 229;
    static readonly LOCALTIMESTAMP = 230;
    static readonly LOCATION = 231;
    static readonly LOCATIONS = 232;
    static readonly LOGS = 233;
    static readonly LOGICAL = 234;
    static readonly MANUAL = 235;
    static readonly MAP = 236;
    static readonly MAPPING = 237;
    static readonly MAPPINGS = 238;
    static readonly MASKING = 239;
    static readonly MATERIALIZED = 240;
    static readonly MAX = 241;
    static readonly MAXVALUE = 242;
    static readonly MERGE = 243;
    static readonly MICROSECOND = 244;
    static readonly MILLISECOND = 245;
    static readonly MIN = 246;
    static readonly MINUTE = 247;
    static readonly MINUS = 248;
    static readonly META = 249;
    static readonly MOD = 250;
    static readonly MODE = 251;
    static readonly MODIFY = 252;
    static readonly MONTH = 253;
    static readonly NAME = 254;
    static readonly NAMES = 255;
    static readonly NEGATIVE = 256;
    static readonly NO = 257;
    static readonly NODE = 258;
    static readonly NODES = 259;
    static readonly NONE = 260;
    static readonly NOT = 261;
    static readonly NULL = 262;
    static readonly NULLS = 263;
    static readonly NUMBER = 264;
    static readonly NUMERIC = 265;
    static readonly OBSERVER = 266;
    static readonly OF = 267;
    static readonly OFFSET = 268;
    static readonly ON = 269;
    static readonly ONLY = 270;
    static readonly OPEN = 271;
    static readonly OPERATE = 272;
    static readonly OPTIMIZE = 273;
    static readonly OPTIMIZER = 274;
    static readonly OPTION = 275;
    static readonly OR = 276;
    static readonly ORDER = 277;
    static readonly OUTER = 278;
    static readonly OUTFILE = 279;
    static readonly OVER = 280;
    static readonly PARAMETER = 281;
    static readonly PARTITION = 282;
    static readonly PARTITIONS = 283;
    static readonly PASSWORD = 284;
    static readonly PATH = 285;
    static readonly PAUSE = 286;
    static readonly PENDING = 287;
    static readonly PERCENT_RANK = 288;
    static readonly PERCENTILE = 289;
    static readonly PERCENTILE_UNION = 290;
    static readonly PLUGIN = 291;
    static readonly PLUGINS = 292;
    static readonly PIPE = 293;
    static readonly PIPES = 294;
    static readonly POLICY = 295;
    static readonly POLICIES = 296;
    static readonly PRECEDING = 297;
    static readonly PREPARE = 298;
    static readonly PRIMARY = 299;
    static readonly PRIORITY = 300;
    static readonly PRIVILEGES = 301;
    static readonly PROBABILITY = 302;
    static readonly PROC = 303;
    static readonly PROCEDURE = 304;
    static readonly PROCESSLIST = 305;
    static readonly PROFILE = 306;
    static readonly PROFILELIST = 307;
    static readonly PROPERTIES = 308;
    static readonly PROPERTY = 309;
    static readonly QUALIFY = 310;
    static readonly QUARTER = 311;
    static readonly QUERY = 312;
    static readonly QUERIES = 313;
    static readonly QUEUE = 314;
    static readonly QUOTA = 315;
    static readonly RANDOM = 316;
    static readonly RANGE = 317;
    static readonly RANK = 318;
    static readonly READ = 319;
    static readonly RECOVER = 320;
    static readonly REFRESH = 321;
    static readonly REWRITE = 322;
    static readonly REGEXP = 323;
    static readonly RELEASE = 324;
    static readonly REMOVE = 325;
    static readonly RENAME = 326;
    static readonly REPAIR = 327;
    static readonly REPEATABLE = 328;
    static readonly REPLACE = 329;
    static readonly REPLACE_IF_NOT_NULL = 330;
    static readonly REPLICA = 331;
    static readonly REPOSITORY = 332;
    static readonly REPOSITORIES = 333;
    static readonly RESOURCE = 334;
    static readonly RESOURCES = 335;
    static readonly RESTORE = 336;
    static readonly RESUME = 337;
    static readonly RETURNS = 338;
    static readonly RETRY = 339;
    static readonly REVOKE = 340;
    static readonly REVERT = 341;
    static readonly RIGHT = 342;
    static readonly RLIKE = 343;
    static readonly ROLE = 344;
    static readonly ROLES = 345;
    static readonly ROLLBACK = 346;
    static readonly ROLLUP = 347;
    static readonly ROUTINE = 348;
    static readonly ROW = 349;
    static readonly ROWS = 350;
    static readonly ROW_NUMBER = 351;
    static readonly RULE = 352;
    static readonly RULES = 353;
    static readonly RUNNING = 354;
    static readonly SAMPLE = 355;
    static readonly SCHEDULER = 356;
    static readonly SCHEMA = 357;
    static readonly SCHEMAS = 358;
    static readonly SECOND = 359;
    static readonly SECURITY = 360;
    static readonly SELECT = 361;
    static readonly SEMI = 362;
    static readonly SEPARATOR = 363;
    static readonly SERIALIZABLE = 364;
    static readonly SESSION = 365;
    static readonly SET = 366;
    static readonly SETS = 367;
    static readonly SET_VAR = 368;
    static readonly SIGNED = 369;
    static readonly SKIP_HEADER = 370;
    static readonly SHOW = 371;
    static readonly SMALLINT = 372;
    static readonly SNAPSHOT = 373;
    static readonly SQLBLACKLIST = 374;
    static readonly START = 375;
    static readonly STATS = 376;
    static readonly STATUS = 377;
    static readonly STOP = 378;
    static readonly STORAGE = 379;
    static readonly STREAM = 380;
    static readonly STRING = 381;
    static readonly TEXT = 382;
    static readonly SUBMIT = 383;
    static readonly SUM = 384;
    static readonly SUSPEND = 385;
    static readonly SYNC = 386;
    static readonly SYSTEM = 387;
    static readonly SYSTEM_TIME = 388;
    static readonly SWAP = 389;
    static readonly STRUCT = 390;
    static readonly TABLE = 391;
    static readonly TABLES = 392;
    static readonly TABLET = 393;
    static readonly TASK = 394;
    static readonly TEMPORARY = 395;
    static readonly TERMINATED = 396;
    static readonly THAN = 397;
    static readonly THEN = 398;
    static readonly TIME = 399;
    static readonly TIMES = 400;
    static readonly TIMESTAMP = 401;
    static readonly TIMESTAMPADD = 402;
    static readonly TIMESTAMPDIFF = 403;
    static readonly TINYINT = 404;
    static readonly TRANSACTION = 405;
    static readonly TO = 406;
    static readonly TRACE = 407;
    static readonly TRIGGERS = 408;
    static readonly TRIM_SPACE = 409;
    static readonly TRUE = 410;
    static readonly TRUNCATE = 411;
    static readonly TYPE = 412;
    static readonly TYPES = 413;
    static readonly UNBOUNDED = 414;
    static readonly UNCOMMITTED = 415;
    static readonly UNION = 416;
    static readonly UNIQUE = 417;
    static readonly UNINSTALL = 418;
    static readonly UNSET = 419;
    static readonly UNSIGNED = 420;
    static readonly UPDATE = 421;
    static readonly USAGE = 422;
    static readonly USE = 423;
    static readonly USER = 424;
    static readonly USERS = 425;
    static readonly USING = 426;
    static readonly VALUE = 427;
    static readonly VALUES = 428;
    static readonly VARBINARY = 429;
    static readonly VARCHAR = 430;
    static readonly VARIABLES = 431;
    static readonly VERBOSE = 432;
    static readonly VIEW = 433;
    static readonly VIEWS = 434;
    static readonly VOLUME = 435;
    static readonly VOLUMES = 436;
    static readonly WAREHOUSE = 437;
    static readonly WAREHOUSES = 438;
    static readonly WARNINGS = 439;
    static readonly WEEK = 440;
    static readonly WHEN = 441;
    static readonly WHERE = 442;
    static readonly WHITELIST = 443;
    static readonly WITH = 444;
    static readonly WORK = 445;
    static readonly WRITE = 446;
    static readonly YEAR = 447;
    static readonly LOCK = 448;
    static readonly UNLOCK = 449;
    static readonly LOW_PRIORITY = 450;
    static readonly EQ = 451;
    static readonly NEQ = 452;
    static readonly LT = 453;
    static readonly LTE = 454;
    static readonly GT = 455;
    static readonly GTE = 456;
    static readonly EQ_FOR_NULL = 457;
    static readonly PLUS_SYMBOL = 458;
    static readonly MINUS_SYMBOL = 459;
    static readonly ASTERISK_SYMBOL = 460;
    static readonly SLASH_SYMBOL = 461;
    static readonly PERCENT_SYMBOL = 462;
    static readonly LOGICAL_OR = 463;
    static readonly LOGICAL_AND = 464;
    static readonly LOGICAL_NOT = 465;
    static readonly INT_DIV = 466;
    static readonly BITAND = 467;
    static readonly BITOR = 468;
    static readonly BITXOR = 469;
    static readonly BITNOT = 470;
    static readonly BIT_SHIFT_LEFT = 471;
    static readonly BIT_SHIFT_RIGHT = 472;
    static readonly BIT_SHIFT_RIGHT_LOGICAL = 473;
    static readonly ARROW = 474;
    static readonly AT = 475;
    static readonly INTEGER_VALUE = 476;
    static readonly DECIMAL_VALUE = 477;
    static readonly DOUBLE_VALUE = 478;
    static readonly SINGLE_QUOTED_TEXT = 479;
    static readonly DOUBLE_QUOTED_TEXT = 480;
    static readonly BINARY_SINGLE_QUOTED_TEXT = 481;
    static readonly BINARY_DOUBLE_QUOTED_TEXT = 482;
    static readonly LETTER_IDENTIFIER = 483;
    static readonly DIGIT_IDENTIFIER = 484;
    static readonly BACKQUOTED_IDENTIFIER = 485;
    static readonly DOT_IDENTIFIER = 486;
    static readonly SIMPLE_COMMENT = 487;
    static readonly BRACKETED_COMMENT = 488;
    static readonly SEMICOLON = 489;
    static readonly DOTDOTDOT = 490;
    static readonly WS = 491;
    static readonly CONCAT = 492;
    static readonly RULE_program = 0;
    static readonly RULE_sqlStatements = 1;
    static readonly RULE_emptyStatement = 2;
    static readonly RULE_statement = 3;
    static readonly RULE_useDatabaseStatement = 4;
    static readonly RULE_useCatalogStatement = 5;
    static readonly RULE_setCatalogStatement = 6;
    static readonly RULE_showDatabasesStatement = 7;
    static readonly RULE_alterDbQuotaStatement = 8;
    static readonly RULE_createDbStatement = 9;
    static readonly RULE_dropDbStatement = 10;
    static readonly RULE_showCreateDbStatement = 11;
    static readonly RULE_alterDatabaseRenameStatement = 12;
    static readonly RULE_recoverDbStmt = 13;
    static readonly RULE_showDataStmt = 14;
    static readonly RULE_createTableStatement = 15;
    static readonly RULE_columnDesc = 16;
    static readonly RULE_charsetName = 17;
    static readonly RULE_defaultDesc = 18;
    static readonly RULE_generatedColumnDesc = 19;
    static readonly RULE_indexDesc = 20;
    static readonly RULE_engineDesc = 21;
    static readonly RULE_charsetDesc = 22;
    static readonly RULE_collateDesc = 23;
    static readonly RULE_keyDesc = 24;
    static readonly RULE_orderByDesc = 25;
    static readonly RULE_aggDesc = 26;
    static readonly RULE_rollupDesc = 27;
    static readonly RULE_rollupItem = 28;
    static readonly RULE_dupKeys = 29;
    static readonly RULE_fromRollup = 30;
    static readonly RULE_withMaskingPolicy = 31;
    static readonly RULE_withRowAccessPolicy = 32;
    static readonly RULE_createTemporaryTableStatement = 33;
    static readonly RULE_createTableAsSelectStatement = 34;
    static readonly RULE_dropTableStatement = 35;
    static readonly RULE_alterTableStatement = 36;
    static readonly RULE_createIndexStatement = 37;
    static readonly RULE_dropIndexStatement = 38;
    static readonly RULE_indexType = 39;
    static readonly RULE_showTableStatement = 40;
    static readonly RULE_showCreateTableStatement = 41;
    static readonly RULE_showColumnStatement = 42;
    static readonly RULE_showTableStatusStatement = 43;
    static readonly RULE_refreshTableStatement = 44;
    static readonly RULE_showAlterStatement = 45;
    static readonly RULE_descTableStatement = 46;
    static readonly RULE_createTableLikeStatement = 47;
    static readonly RULE_showIndexStatement = 48;
    static readonly RULE_recoverTableStatement = 49;
    static readonly RULE_truncateTableStatement = 50;
    static readonly RULE_cancelAlterTableStatement = 51;
    static readonly RULE_showPartitionsStatement = 52;
    static readonly RULE_recoverPartitionStatement = 53;
    static readonly RULE_createViewStatement = 54;
    static readonly RULE_alterViewStatement = 55;
    static readonly RULE_dropViewStatement = 56;
    static readonly RULE_columnNameWithComment = 57;
    static readonly RULE_submitTaskStatement = 58;
    static readonly RULE_dropTaskStatement = 59;
    static readonly RULE_createMaterializedViewStatement = 60;
    static readonly RULE_materializedViewDesc = 61;
    static readonly RULE_showMaterializedViewsStatement = 62;
    static readonly RULE_dropMaterializedViewStatement = 63;
    static readonly RULE_alterMaterializedViewStatement = 64;
    static readonly RULE_refreshMaterializedViewStatement = 65;
    static readonly RULE_cancelRefreshMaterializedViewStatement = 66;
    static readonly RULE_adminSetConfigStatement = 67;
    static readonly RULE_adminSetReplicaStatusStatement = 68;
    static readonly RULE_adminShowConfigStatement = 69;
    static readonly RULE_adminShowReplicaDistributionStatement = 70;
    static readonly RULE_adminShowReplicaStatusStatement = 71;
    static readonly RULE_adminRepairTableStatement = 72;
    static readonly RULE_adminCancelRepairTableStatement = 73;
    static readonly RULE_adminCheckTabletsStatement = 74;
    static readonly RULE_killStatement = 75;
    static readonly RULE_syncStatement = 76;
    static readonly RULE_alterSystemStatement = 77;
    static readonly RULE_cancelAlterSystemStatement = 78;
    static readonly RULE_showComputeNodesStatement = 79;
    static readonly RULE_createExternalCatalogStatement = 80;
    static readonly RULE_showCreateExternalCatalogStatement = 81;
    static readonly RULE_dropExternalCatalogStatement = 82;
    static readonly RULE_showCatalogsStatement = 83;
    static readonly RULE_alterCatalogStatement = 84;
    static readonly RULE_createWarehouseStatement = 85;
    static readonly RULE_showWarehousesStatement = 86;
    static readonly RULE_dropWarehouseStatement = 87;
    static readonly RULE_alterWarehouseStatement = 88;
    static readonly RULE_showClustersStatement = 89;
    static readonly RULE_suspendWarehouseStatement = 90;
    static readonly RULE_resumeWarehouseStatement = 91;
    static readonly RULE_createStorageVolumeStatement = 92;
    static readonly RULE_typeDesc = 93;
    static readonly RULE_locationsDesc = 94;
    static readonly RULE_showStorageVolumesStatement = 95;
    static readonly RULE_dropStorageVolumeStatement = 96;
    static readonly RULE_alterStorageVolumeStatement = 97;
    static readonly RULE_alterStorageVolumeClause = 98;
    static readonly RULE_modifyStorageVolumePropertiesClause = 99;
    static readonly RULE_modifyStorageVolumeCommentClause = 100;
    static readonly RULE_descStorageVolumeStatement = 101;
    static readonly RULE_setDefaultStorageVolumeStatement = 102;
    static readonly RULE_updateFailPointStatusStatement = 103;
    static readonly RULE_showFailPointStatement = 104;
    static readonly RULE_alterClause = 105;
    static readonly RULE_addFrontendClause = 106;
    static readonly RULE_dropFrontendClause = 107;
    static readonly RULE_modifyFrontendHostClause = 108;
    static readonly RULE_addBackendClause = 109;
    static readonly RULE_dropBackendClause = 110;
    static readonly RULE_decommissionBackendClause = 111;
    static readonly RULE_modifyBackendHostClause = 112;
    static readonly RULE_addComputeNodeClause = 113;
    static readonly RULE_dropComputeNodeClause = 114;
    static readonly RULE_modifyBrokerClause = 115;
    static readonly RULE_alterLoadErrorUrlClause = 116;
    static readonly RULE_createImageClause = 117;
    static readonly RULE_cleanTabletSchedQClause = 118;
    static readonly RULE_createIndexClause = 119;
    static readonly RULE_dropIndexClause = 120;
    static readonly RULE_tableRenameClause = 121;
    static readonly RULE_swapTableClause = 122;
    static readonly RULE_modifyPropertiesClause = 123;
    static readonly RULE_modifyCommentClause = 124;
    static readonly RULE_optimizeClause = 125;
    static readonly RULE_addColumnClause = 126;
    static readonly RULE_addColumnsClause = 127;
    static readonly RULE_dropColumnClause = 128;
    static readonly RULE_modifyColumnClause = 129;
    static readonly RULE_columnRenameClause = 130;
    static readonly RULE_reorderColumnsClause = 131;
    static readonly RULE_rollupRenameClause = 132;
    static readonly RULE_compactionClause = 133;
    static readonly RULE_applyMaskingPolicyClause = 134;
    static readonly RULE_applyRowAccessPolicyClause = 135;
    static readonly RULE_addPartitionClause = 136;
    static readonly RULE_dropPartitionClause = 137;
    static readonly RULE_truncatePartitionClause = 138;
    static readonly RULE_modifyPartitionClause = 139;
    static readonly RULE_replacePartitionClause = 140;
    static readonly RULE_partitionRenameClause = 141;
    static readonly RULE_insertStatement = 142;
    static readonly RULE_updateStatement = 143;
    static readonly RULE_deleteStatement = 144;
    static readonly RULE_createRoutineLoadStatement = 145;
    static readonly RULE_alterRoutineLoadStatement = 146;
    static readonly RULE_dataSource = 147;
    static readonly RULE_loadProperties = 148;
    static readonly RULE_colSeparatorProperty = 149;
    static readonly RULE_rowDelimiterProperty = 150;
    static readonly RULE_importColumns = 151;
    static readonly RULE_columnProperties = 152;
    static readonly RULE_jobProperties = 153;
    static readonly RULE_dataSourceProperties = 154;
    static readonly RULE_stopRoutineLoadStatement = 155;
    static readonly RULE_resumeRoutineLoadStatement = 156;
    static readonly RULE_pauseRoutineLoadStatement = 157;
    static readonly RULE_showRoutineLoadStatement = 158;
    static readonly RULE_showRoutineLoadTaskStatement = 159;
    static readonly RULE_showCreateRoutineLoadStatement = 160;
    static readonly RULE_showStreamLoadStatement = 161;
    static readonly RULE_analyzeStatement = 162;
    static readonly RULE_dropStatsStatement = 163;
    static readonly RULE_analyzeHistogramStatement = 164;
    static readonly RULE_dropHistogramStatement = 165;
    static readonly RULE_createAnalyzeStatement = 166;
    static readonly RULE_dropAnalyzeJobStatement = 167;
    static readonly RULE_showAnalyzeStatement = 168;
    static readonly RULE_showStatsMetaStatement = 169;
    static readonly RULE_showHistogramMetaStatement = 170;
    static readonly RULE_killAnalyzeStatement = 171;
    static readonly RULE_analyzeProfileStatement = 172;
    static readonly RULE_createResourceGroupStatement = 173;
    static readonly RULE_dropResourceGroupStatement = 174;
    static readonly RULE_alterResourceGroupStatement = 175;
    static readonly RULE_showResourceGroupStatement = 176;
    static readonly RULE_showResourceGroupUsageStatement = 177;
    static readonly RULE_createResourceStatement = 178;
    static readonly RULE_alterResourceStatement = 179;
    static readonly RULE_dropResourceStatement = 180;
    static readonly RULE_showResourceStatement = 181;
    static readonly RULE_classifier = 182;
    static readonly RULE_showFunctionsStatement = 183;
    static readonly RULE_dropFunctionStatement = 184;
    static readonly RULE_createFunctionStatement = 185;
    static readonly RULE_typeList = 186;
    static readonly RULE_loadStatement = 187;
    static readonly RULE_labelName = 188;
    static readonly RULE_dataDescList = 189;
    static readonly RULE_dataDesc = 190;
    static readonly RULE_formatProps = 191;
    static readonly RULE_brokerDesc = 192;
    static readonly RULE_resourceDesc = 193;
    static readonly RULE_showLoadStatement = 194;
    static readonly RULE_showLoadWarningsStatement = 195;
    static readonly RULE_cancelLoadStatement = 196;
    static readonly RULE_alterLoadStatement = 197;
    static readonly RULE_cancelCompactionStatement = 198;
    static readonly RULE_showAuthorStatement = 199;
    static readonly RULE_showBackendsStatement = 200;
    static readonly RULE_showBrokerStatement = 201;
    static readonly RULE_showCharsetStatement = 202;
    static readonly RULE_showCollationStatement = 203;
    static readonly RULE_showDeleteStatement = 204;
    static readonly RULE_showDynamicPartitionStatement = 205;
    static readonly RULE_showEventsStatement = 206;
    static readonly RULE_showEnginesStatement = 207;
    static readonly RULE_showFrontendsStatement = 208;
    static readonly RULE_showPluginsStatement = 209;
    static readonly RULE_showRepositoriesStatement = 210;
    static readonly RULE_showOpenTableStatement = 211;
    static readonly RULE_showPrivilegesStatement = 212;
    static readonly RULE_showProcedureStatement = 213;
    static readonly RULE_showProcStatement = 214;
    static readonly RULE_showProcesslistStatement = 215;
    static readonly RULE_showProfilelistStatement = 216;
    static readonly RULE_showRunningQueriesStatement = 217;
    static readonly RULE_showStatusStatement = 218;
    static readonly RULE_showTabletStatement = 219;
    static readonly RULE_showTransactionStatement = 220;
    static readonly RULE_showTriggersStatement = 221;
    static readonly RULE_showUserPropertyStatement = 222;
    static readonly RULE_showVariablesStatement = 223;
    static readonly RULE_showWarningStatement = 224;
    static readonly RULE_helpStatement = 225;
    static readonly RULE_createUserStatement = 226;
    static readonly RULE_dropUserStatement = 227;
    static readonly RULE_alterUserStatement = 228;
    static readonly RULE_showUserStatement = 229;
    static readonly RULE_showAuthenticationStatement = 230;
    static readonly RULE_executeAsStatement = 231;
    static readonly RULE_createRoleStatement = 232;
    static readonly RULE_alterRoleStatement = 233;
    static readonly RULE_dropRoleStatement = 234;
    static readonly RULE_showRolesStatement = 235;
    static readonly RULE_grantRoleStatement = 236;
    static readonly RULE_revokeRoleStatement = 237;
    static readonly RULE_setRoleStatement = 238;
    static readonly RULE_setDefaultRoleStatement = 239;
    static readonly RULE_grantRevokeClause = 240;
    static readonly RULE_grantPrivilegeStatement = 241;
    static readonly RULE_revokePrivilegeStatement = 242;
    static readonly RULE_showGrantsStatement = 243;
    static readonly RULE_createSecurityIntegrationStatement = 244;
    static readonly RULE_alterSecurityIntegrationStatement = 245;
    static readonly RULE_dropSecurityIntegrationStatement = 246;
    static readonly RULE_showSecurityIntegrationStatement = 247;
    static readonly RULE_showCreateSecurityIntegrationStatement = 248;
    static readonly RULE_createRoleMappingStatement = 249;
    static readonly RULE_alterRoleMappingStatement = 250;
    static readonly RULE_dropRoleMappingStatement = 251;
    static readonly RULE_showRoleMappingStatement = 252;
    static readonly RULE_refreshRoleMappingStatement = 253;
    static readonly RULE_authOption = 254;
    static readonly RULE_privObjectName = 255;
    static readonly RULE_privObjectNameList = 256;
    static readonly RULE_privFunctionObjectNameList = 257;
    static readonly RULE_privilegeTypeList = 258;
    static readonly RULE_privilegeType = 259;
    static readonly RULE_privObjectType = 260;
    static readonly RULE_privObjectTypePlural = 261;
    static readonly RULE_createMaskingPolicyStatement = 262;
    static readonly RULE_dropMaskingPolicyStatement = 263;
    static readonly RULE_alterMaskingPolicyStatement = 264;
    static readonly RULE_showMaskingPolicyStatement = 265;
    static readonly RULE_showCreateMaskingPolicyStatement = 266;
    static readonly RULE_createRowAccessPolicyStatement = 267;
    static readonly RULE_dropRowAccessPolicyStatement = 268;
    static readonly RULE_alterRowAccessPolicyStatement = 269;
    static readonly RULE_showRowAccessPolicyStatement = 270;
    static readonly RULE_showCreateRowAccessPolicyStatement = 271;
    static readonly RULE_policySignature = 272;
    static readonly RULE_backupStatement = 273;
    static readonly RULE_cancelBackupStatement = 274;
    static readonly RULE_showBackupStatement = 275;
    static readonly RULE_restoreStatement = 276;
    static readonly RULE_cancelRestoreStatement = 277;
    static readonly RULE_showRestoreStatement = 278;
    static readonly RULE_showSnapshotStatement = 279;
    static readonly RULE_createRepositoryStatement = 280;
    static readonly RULE_dropRepositoryStatement = 281;
    static readonly RULE_addSqlBlackListStatement = 282;
    static readonly RULE_delSqlBlackListStatement = 283;
    static readonly RULE_showSqlBlackListStatement = 284;
    static readonly RULE_showWhiteListStatement = 285;
    static readonly RULE_dataCacheTarget = 286;
    static readonly RULE_createDataCacheRuleStatement = 287;
    static readonly RULE_showDataCacheRulesStatement = 288;
    static readonly RULE_dropDataCacheRuleStatement = 289;
    static readonly RULE_clearDataCacheRulesStatement = 290;
    static readonly RULE_exportStatement = 291;
    static readonly RULE_cancelExportStatement = 292;
    static readonly RULE_showExportStatement = 293;
    static readonly RULE_installPluginStatement = 294;
    static readonly RULE_uninstallPluginStatement = 295;
    static readonly RULE_createFileStatement = 296;
    static readonly RULE_dropFileStatement = 297;
    static readonly RULE_showSmallFilesStatement = 298;
    static readonly RULE_createPipeStatement = 299;
    static readonly RULE_dropPipeStatement = 300;
    static readonly RULE_alterPipeClause = 301;
    static readonly RULE_alterPipeStatement = 302;
    static readonly RULE_descPipeStatement = 303;
    static readonly RULE_showPipeStatement = 304;
    static readonly RULE_setStatement = 305;
    static readonly RULE_setVar = 306;
    static readonly RULE_transaction_characteristics = 307;
    static readonly RULE_transaction_access_mode = 308;
    static readonly RULE_isolation_level = 309;
    static readonly RULE_isolation_types = 310;
    static readonly RULE_setExprOrDefault = 311;
    static readonly RULE_setUserPropertyStatement = 312;
    static readonly RULE_roleList = 313;
    static readonly RULE_executeScriptStatement = 314;
    static readonly RULE_unsupportedStatement = 315;
    static readonly RULE_lock_item = 316;
    static readonly RULE_lock_type = 317;
    static readonly RULE_queryStatement = 318;
    static readonly RULE_queryRelation = 319;
    static readonly RULE_withClause = 320;
    static readonly RULE_queryNoWith = 321;
    static readonly RULE_temporalClause = 322;
    static readonly RULE_queryPrimary = 323;
    static readonly RULE_subquery = 324;
    static readonly RULE_rowConstructor = 325;
    static readonly RULE_sortItem = 326;
    static readonly RULE_limitElement = 327;
    static readonly RULE_querySpecification = 328;
    static readonly RULE_fromClause = 329;
    static readonly RULE_groupingElement = 330;
    static readonly RULE_groupingSet = 331;
    static readonly RULE_commonTableExpression = 332;
    static readonly RULE_setQuantifier = 333;
    static readonly RULE_selectItem = 334;
    static readonly RULE_relations = 335;
    static readonly RULE_relation = 336;
    static readonly RULE_relationPrimary = 337;
    static readonly RULE_joinRelation = 338;
    static readonly RULE_crossOrInnerJoinType = 339;
    static readonly RULE_outerAndSemiJoinType = 340;
    static readonly RULE_bracketHint = 341;
    static readonly RULE_setVarHint = 342;
    static readonly RULE_hintMap = 343;
    static readonly RULE_joinCriteria = 344;
    static readonly RULE_columnAliases = 345;
    static readonly RULE_partitionNames = 346;
    static readonly RULE_keyPartitions = 347;
    static readonly RULE_tabletList = 348;
    static readonly RULE_prepareStatement = 349;
    static readonly RULE_prepareSql = 350;
    static readonly RULE_executeStatement = 351;
    static readonly RULE_deallocateStatement = 352;
    static readonly RULE_replicaList = 353;
    static readonly RULE_expressionsWithDefault = 354;
    static readonly RULE_expressionOrDefault = 355;
    static readonly RULE_mapExpressionList = 356;
    static readonly RULE_mapExpression = 357;
    static readonly RULE_expressionSingleton = 358;
    static readonly RULE_expression = 359;
    static readonly RULE_expressionList = 360;
    static readonly RULE_booleanExpression = 361;
    static readonly RULE_predicate = 362;
    static readonly RULE_tupleInSubquery = 363;
    static readonly RULE_predicateOperations = 364;
    static readonly RULE_valueExpression = 365;
    static readonly RULE_primaryExpression = 366;
    static readonly RULE_literalExpression = 367;
    static readonly RULE_functionCall = 368;
    static readonly RULE_aggregationFunction = 369;
    static readonly RULE_userVariable = 370;
    static readonly RULE_systemVariable = 371;
    static readonly RULE_columnReference = 372;
    static readonly RULE_informationFunctionExpression = 373;
    static readonly RULE_specialDateTimeExpression = 374;
    static readonly RULE_specialFunctionExpression = 375;
    static readonly RULE_windowFunction = 376;
    static readonly RULE_whenClause = 377;
    static readonly RULE_over = 378;
    static readonly RULE_ignoreNulls = 379;
    static readonly RULE_tableDesc = 380;
    static readonly RULE_restoreTableDesc = 381;
    static readonly RULE_explainDesc = 382;
    static readonly RULE_optimizerTrace = 383;
    static readonly RULE_partitionDesc = 384;
    static readonly RULE_listPartitionDesc = 385;
    static readonly RULE_singleItemListPartitionDesc = 386;
    static readonly RULE_multiItemListPartitionDesc = 387;
    static readonly RULE_stringList = 388;
    static readonly RULE_rangePartitionDesc = 389;
    static readonly RULE_singleRangePartition = 390;
    static readonly RULE_multiRangePartition = 391;
    static readonly RULE_partitionRangeDesc = 392;
    static readonly RULE_partitionKeyDesc = 393;
    static readonly RULE_partitionValueList = 394;
    static readonly RULE_keyPartition = 395;
    static readonly RULE_partitionValue = 396;
    static readonly RULE_distributionClause = 397;
    static readonly RULE_distributionDesc = 398;
    static readonly RULE_refreshSchemeDesc = 399;
    static readonly RULE_statusDesc = 400;
    static readonly RULE_properties = 401;
    static readonly RULE_extProperties = 402;
    static readonly RULE_propertyList = 403;
    static readonly RULE_userPropertyList = 404;
    static readonly RULE_property = 405;
    static readonly RULE_varType = 406;
    static readonly RULE_comment = 407;
    static readonly RULE_outfile = 408;
    static readonly RULE_fileFormat = 409;
    static readonly RULE_string = 410;
    static readonly RULE_binary = 411;
    static readonly RULE_comparisonOperator = 412;
    static readonly RULE_booleanValue = 413;
    static readonly RULE_interval = 414;
    static readonly RULE_unitIdentifier = 415;
    static readonly RULE_unitBoundary = 416;
    static readonly RULE_type = 417;
    static readonly RULE_arrayType = 418;
    static readonly RULE_mapType = 419;
    static readonly RULE_subfieldDesc = 420;
    static readonly RULE_subfieldDescs = 421;
    static readonly RULE_structType = 422;
    static readonly RULE_typeParameter = 423;
    static readonly RULE_baseType = 424;
    static readonly RULE_decimalType = 425;
    static readonly RULE_qualifiedName = 426;
    static readonly RULE_identifier = 427;
    static readonly RULE_identifierList = 428;
    static readonly RULE_identifierOrString = 429;
    static readonly RULE_identifierOrStringList = 430;
    static readonly RULE_identifierOrStringOrStar = 431;
    static readonly RULE_user = 432;
    static readonly RULE_assignment = 433;
    static readonly RULE_assignmentList = 434;
    static readonly RULE_number = 435;
    static readonly RULE_nonReserved = 436;
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
    emptyStatement(): EmptyStatementContext;
    statement(): StatementContext;
    useDatabaseStatement(): UseDatabaseStatementContext;
    useCatalogStatement(): UseCatalogStatementContext;
    setCatalogStatement(): SetCatalogStatementContext;
    showDatabasesStatement(): ShowDatabasesStatementContext;
    alterDbQuotaStatement(): AlterDbQuotaStatementContext;
    createDbStatement(): CreateDbStatementContext;
    dropDbStatement(): DropDbStatementContext;
    showCreateDbStatement(): ShowCreateDbStatementContext;
    alterDatabaseRenameStatement(): AlterDatabaseRenameStatementContext;
    recoverDbStmt(): RecoverDbStmtContext;
    showDataStmt(): ShowDataStmtContext;
    createTableStatement(): CreateTableStatementContext;
    columnDesc(): ColumnDescContext;
    charsetName(): CharsetNameContext;
    defaultDesc(): DefaultDescContext;
    generatedColumnDesc(): GeneratedColumnDescContext;
    indexDesc(): IndexDescContext;
    engineDesc(): EngineDescContext;
    charsetDesc(): CharsetDescContext;
    collateDesc(): CollateDescContext;
    keyDesc(): KeyDescContext;
    orderByDesc(): OrderByDescContext;
    aggDesc(): AggDescContext;
    rollupDesc(): RollupDescContext;
    rollupItem(): RollupItemContext;
    dupKeys(): DupKeysContext;
    fromRollup(): FromRollupContext;
    withMaskingPolicy(): WithMaskingPolicyContext;
    withRowAccessPolicy(): WithRowAccessPolicyContext;
    createTemporaryTableStatement(): CreateTemporaryTableStatementContext;
    createTableAsSelectStatement(): CreateTableAsSelectStatementContext;
    dropTableStatement(): DropTableStatementContext;
    alterTableStatement(): AlterTableStatementContext;
    createIndexStatement(): CreateIndexStatementContext;
    dropIndexStatement(): DropIndexStatementContext;
    indexType(): IndexTypeContext;
    showTableStatement(): ShowTableStatementContext;
    showCreateTableStatement(): ShowCreateTableStatementContext;
    showColumnStatement(): ShowColumnStatementContext;
    showTableStatusStatement(): ShowTableStatusStatementContext;
    refreshTableStatement(): RefreshTableStatementContext;
    showAlterStatement(): ShowAlterStatementContext;
    descTableStatement(): DescTableStatementContext;
    createTableLikeStatement(): CreateTableLikeStatementContext;
    showIndexStatement(): ShowIndexStatementContext;
    recoverTableStatement(): RecoverTableStatementContext;
    truncateTableStatement(): TruncateTableStatementContext;
    cancelAlterTableStatement(): CancelAlterTableStatementContext;
    showPartitionsStatement(): ShowPartitionsStatementContext;
    recoverPartitionStatement(): RecoverPartitionStatementContext;
    createViewStatement(): CreateViewStatementContext;
    alterViewStatement(): AlterViewStatementContext;
    dropViewStatement(): DropViewStatementContext;
    columnNameWithComment(): ColumnNameWithCommentContext;
    submitTaskStatement(): SubmitTaskStatementContext;
    dropTaskStatement(): DropTaskStatementContext;
    createMaterializedViewStatement(): CreateMaterializedViewStatementContext;
    materializedViewDesc(): MaterializedViewDescContext;
    showMaterializedViewsStatement(): ShowMaterializedViewsStatementContext;
    dropMaterializedViewStatement(): DropMaterializedViewStatementContext;
    alterMaterializedViewStatement(): AlterMaterializedViewStatementContext;
    refreshMaterializedViewStatement(): RefreshMaterializedViewStatementContext;
    cancelRefreshMaterializedViewStatement(): CancelRefreshMaterializedViewStatementContext;
    adminSetConfigStatement(): AdminSetConfigStatementContext;
    adminSetReplicaStatusStatement(): AdminSetReplicaStatusStatementContext;
    adminShowConfigStatement(): AdminShowConfigStatementContext;
    adminShowReplicaDistributionStatement(): AdminShowReplicaDistributionStatementContext;
    adminShowReplicaStatusStatement(): AdminShowReplicaStatusStatementContext;
    adminRepairTableStatement(): AdminRepairTableStatementContext;
    adminCancelRepairTableStatement(): AdminCancelRepairTableStatementContext;
    adminCheckTabletsStatement(): AdminCheckTabletsStatementContext;
    killStatement(): KillStatementContext;
    syncStatement(): SyncStatementContext;
    alterSystemStatement(): AlterSystemStatementContext;
    cancelAlterSystemStatement(): CancelAlterSystemStatementContext;
    showComputeNodesStatement(): ShowComputeNodesStatementContext;
    createExternalCatalogStatement(): CreateExternalCatalogStatementContext;
    showCreateExternalCatalogStatement(): ShowCreateExternalCatalogStatementContext;
    dropExternalCatalogStatement(): DropExternalCatalogStatementContext;
    showCatalogsStatement(): ShowCatalogsStatementContext;
    alterCatalogStatement(): AlterCatalogStatementContext;
    createWarehouseStatement(): CreateWarehouseStatementContext;
    showWarehousesStatement(): ShowWarehousesStatementContext;
    dropWarehouseStatement(): DropWarehouseStatementContext;
    alterWarehouseStatement(): AlterWarehouseStatementContext;
    showClustersStatement(): ShowClustersStatementContext;
    suspendWarehouseStatement(): SuspendWarehouseStatementContext;
    resumeWarehouseStatement(): ResumeWarehouseStatementContext;
    createStorageVolumeStatement(): CreateStorageVolumeStatementContext;
    typeDesc(): TypeDescContext;
    locationsDesc(): LocationsDescContext;
    showStorageVolumesStatement(): ShowStorageVolumesStatementContext;
    dropStorageVolumeStatement(): DropStorageVolumeStatementContext;
    alterStorageVolumeStatement(): AlterStorageVolumeStatementContext;
    alterStorageVolumeClause(): AlterStorageVolumeClauseContext;
    modifyStorageVolumePropertiesClause(): ModifyStorageVolumePropertiesClauseContext;
    modifyStorageVolumeCommentClause(): ModifyStorageVolumeCommentClauseContext;
    descStorageVolumeStatement(): DescStorageVolumeStatementContext;
    setDefaultStorageVolumeStatement(): SetDefaultStorageVolumeStatementContext;
    updateFailPointStatusStatement(): UpdateFailPointStatusStatementContext;
    showFailPointStatement(): ShowFailPointStatementContext;
    alterClause(): AlterClauseContext;
    addFrontendClause(): AddFrontendClauseContext;
    dropFrontendClause(): DropFrontendClauseContext;
    modifyFrontendHostClause(): ModifyFrontendHostClauseContext;
    addBackendClause(): AddBackendClauseContext;
    dropBackendClause(): DropBackendClauseContext;
    decommissionBackendClause(): DecommissionBackendClauseContext;
    modifyBackendHostClause(): ModifyBackendHostClauseContext;
    addComputeNodeClause(): AddComputeNodeClauseContext;
    dropComputeNodeClause(): DropComputeNodeClauseContext;
    modifyBrokerClause(): ModifyBrokerClauseContext;
    alterLoadErrorUrlClause(): AlterLoadErrorUrlClauseContext;
    createImageClause(): CreateImageClauseContext;
    cleanTabletSchedQClause(): CleanTabletSchedQClauseContext;
    createIndexClause(): CreateIndexClauseContext;
    dropIndexClause(): DropIndexClauseContext;
    tableRenameClause(): TableRenameClauseContext;
    swapTableClause(): SwapTableClauseContext;
    modifyPropertiesClause(): ModifyPropertiesClauseContext;
    modifyCommentClause(): ModifyCommentClauseContext;
    optimizeClause(): OptimizeClauseContext;
    addColumnClause(): AddColumnClauseContext;
    addColumnsClause(): AddColumnsClauseContext;
    dropColumnClause(): DropColumnClauseContext;
    modifyColumnClause(): ModifyColumnClauseContext;
    columnRenameClause(): ColumnRenameClauseContext;
    reorderColumnsClause(): ReorderColumnsClauseContext;
    rollupRenameClause(): RollupRenameClauseContext;
    compactionClause(): CompactionClauseContext;
    applyMaskingPolicyClause(): ApplyMaskingPolicyClauseContext;
    applyRowAccessPolicyClause(): ApplyRowAccessPolicyClauseContext;
    addPartitionClause(): AddPartitionClauseContext;
    dropPartitionClause(): DropPartitionClauseContext;
    truncatePartitionClause(): TruncatePartitionClauseContext;
    modifyPartitionClause(): ModifyPartitionClauseContext;
    replacePartitionClause(): ReplacePartitionClauseContext;
    partitionRenameClause(): PartitionRenameClauseContext;
    insertStatement(): InsertStatementContext;
    updateStatement(): UpdateStatementContext;
    deleteStatement(): DeleteStatementContext;
    createRoutineLoadStatement(): CreateRoutineLoadStatementContext;
    alterRoutineLoadStatement(): AlterRoutineLoadStatementContext;
    dataSource(): DataSourceContext;
    loadProperties(): LoadPropertiesContext;
    colSeparatorProperty(): ColSeparatorPropertyContext;
    rowDelimiterProperty(): RowDelimiterPropertyContext;
    importColumns(): ImportColumnsContext;
    columnProperties(): ColumnPropertiesContext;
    jobProperties(): JobPropertiesContext;
    dataSourceProperties(): DataSourcePropertiesContext;
    stopRoutineLoadStatement(): StopRoutineLoadStatementContext;
    resumeRoutineLoadStatement(): ResumeRoutineLoadStatementContext;
    pauseRoutineLoadStatement(): PauseRoutineLoadStatementContext;
    showRoutineLoadStatement(): ShowRoutineLoadStatementContext;
    showRoutineLoadTaskStatement(): ShowRoutineLoadTaskStatementContext;
    showCreateRoutineLoadStatement(): ShowCreateRoutineLoadStatementContext;
    showStreamLoadStatement(): ShowStreamLoadStatementContext;
    analyzeStatement(): AnalyzeStatementContext;
    dropStatsStatement(): DropStatsStatementContext;
    analyzeHistogramStatement(): AnalyzeHistogramStatementContext;
    dropHistogramStatement(): DropHistogramStatementContext;
    createAnalyzeStatement(): CreateAnalyzeStatementContext;
    dropAnalyzeJobStatement(): DropAnalyzeJobStatementContext;
    showAnalyzeStatement(): ShowAnalyzeStatementContext;
    showStatsMetaStatement(): ShowStatsMetaStatementContext;
    showHistogramMetaStatement(): ShowHistogramMetaStatementContext;
    killAnalyzeStatement(): KillAnalyzeStatementContext;
    analyzeProfileStatement(): AnalyzeProfileStatementContext;
    createResourceGroupStatement(): CreateResourceGroupStatementContext;
    dropResourceGroupStatement(): DropResourceGroupStatementContext;
    alterResourceGroupStatement(): AlterResourceGroupStatementContext;
    showResourceGroupStatement(): ShowResourceGroupStatementContext;
    showResourceGroupUsageStatement(): ShowResourceGroupUsageStatementContext;
    createResourceStatement(): CreateResourceStatementContext;
    alterResourceStatement(): AlterResourceStatementContext;
    dropResourceStatement(): DropResourceStatementContext;
    showResourceStatement(): ShowResourceStatementContext;
    classifier(): ClassifierContext;
    showFunctionsStatement(): ShowFunctionsStatementContext;
    dropFunctionStatement(): DropFunctionStatementContext;
    createFunctionStatement(): CreateFunctionStatementContext;
    typeList(): TypeListContext;
    loadStatement(): LoadStatementContext;
    labelName(): LabelNameContext;
    dataDescList(): DataDescListContext;
    dataDesc(): DataDescContext;
    formatProps(): FormatPropsContext;
    brokerDesc(): BrokerDescContext;
    resourceDesc(): ResourceDescContext;
    showLoadStatement(): ShowLoadStatementContext;
    showLoadWarningsStatement(): ShowLoadWarningsStatementContext;
    cancelLoadStatement(): CancelLoadStatementContext;
    alterLoadStatement(): AlterLoadStatementContext;
    cancelCompactionStatement(): CancelCompactionStatementContext;
    showAuthorStatement(): ShowAuthorStatementContext;
    showBackendsStatement(): ShowBackendsStatementContext;
    showBrokerStatement(): ShowBrokerStatementContext;
    showCharsetStatement(): ShowCharsetStatementContext;
    showCollationStatement(): ShowCollationStatementContext;
    showDeleteStatement(): ShowDeleteStatementContext;
    showDynamicPartitionStatement(): ShowDynamicPartitionStatementContext;
    showEventsStatement(): ShowEventsStatementContext;
    showEnginesStatement(): ShowEnginesStatementContext;
    showFrontendsStatement(): ShowFrontendsStatementContext;
    showPluginsStatement(): ShowPluginsStatementContext;
    showRepositoriesStatement(): ShowRepositoriesStatementContext;
    showOpenTableStatement(): ShowOpenTableStatementContext;
    showPrivilegesStatement(): ShowPrivilegesStatementContext;
    showProcedureStatement(): ShowProcedureStatementContext;
    showProcStatement(): ShowProcStatementContext;
    showProcesslistStatement(): ShowProcesslistStatementContext;
    showProfilelistStatement(): ShowProfilelistStatementContext;
    showRunningQueriesStatement(): ShowRunningQueriesStatementContext;
    showStatusStatement(): ShowStatusStatementContext;
    showTabletStatement(): ShowTabletStatementContext;
    showTransactionStatement(): ShowTransactionStatementContext;
    showTriggersStatement(): ShowTriggersStatementContext;
    showUserPropertyStatement(): ShowUserPropertyStatementContext;
    showVariablesStatement(): ShowVariablesStatementContext;
    showWarningStatement(): ShowWarningStatementContext;
    helpStatement(): HelpStatementContext;
    createUserStatement(): CreateUserStatementContext;
    dropUserStatement(): DropUserStatementContext;
    alterUserStatement(): AlterUserStatementContext;
    showUserStatement(): ShowUserStatementContext;
    showAuthenticationStatement(): ShowAuthenticationStatementContext;
    executeAsStatement(): ExecuteAsStatementContext;
    createRoleStatement(): CreateRoleStatementContext;
    alterRoleStatement(): AlterRoleStatementContext;
    dropRoleStatement(): DropRoleStatementContext;
    showRolesStatement(): ShowRolesStatementContext;
    grantRoleStatement(): GrantRoleStatementContext;
    revokeRoleStatement(): RevokeRoleStatementContext;
    setRoleStatement(): SetRoleStatementContext;
    setDefaultRoleStatement(): SetDefaultRoleStatementContext;
    grantRevokeClause(): GrantRevokeClauseContext;
    grantPrivilegeStatement(): GrantPrivilegeStatementContext;
    revokePrivilegeStatement(): RevokePrivilegeStatementContext;
    showGrantsStatement(): ShowGrantsStatementContext;
    createSecurityIntegrationStatement(): CreateSecurityIntegrationStatementContext;
    alterSecurityIntegrationStatement(): AlterSecurityIntegrationStatementContext;
    dropSecurityIntegrationStatement(): DropSecurityIntegrationStatementContext;
    showSecurityIntegrationStatement(): ShowSecurityIntegrationStatementContext;
    showCreateSecurityIntegrationStatement(): ShowCreateSecurityIntegrationStatementContext;
    createRoleMappingStatement(): CreateRoleMappingStatementContext;
    alterRoleMappingStatement(): AlterRoleMappingStatementContext;
    dropRoleMappingStatement(): DropRoleMappingStatementContext;
    showRoleMappingStatement(): ShowRoleMappingStatementContext;
    refreshRoleMappingStatement(): RefreshRoleMappingStatementContext;
    authOption(): AuthOptionContext;
    privObjectName(): PrivObjectNameContext;
    privObjectNameList(): PrivObjectNameListContext;
    privFunctionObjectNameList(): PrivFunctionObjectNameListContext;
    privilegeTypeList(): PrivilegeTypeListContext;
    privilegeType(): PrivilegeTypeContext;
    privObjectType(): PrivObjectTypeContext;
    privObjectTypePlural(): PrivObjectTypePluralContext;
    createMaskingPolicyStatement(): CreateMaskingPolicyStatementContext;
    dropMaskingPolicyStatement(): DropMaskingPolicyStatementContext;
    alterMaskingPolicyStatement(): AlterMaskingPolicyStatementContext;
    showMaskingPolicyStatement(): ShowMaskingPolicyStatementContext;
    showCreateMaskingPolicyStatement(): ShowCreateMaskingPolicyStatementContext;
    createRowAccessPolicyStatement(): CreateRowAccessPolicyStatementContext;
    dropRowAccessPolicyStatement(): DropRowAccessPolicyStatementContext;
    alterRowAccessPolicyStatement(): AlterRowAccessPolicyStatementContext;
    showRowAccessPolicyStatement(): ShowRowAccessPolicyStatementContext;
    showCreateRowAccessPolicyStatement(): ShowCreateRowAccessPolicyStatementContext;
    policySignature(): PolicySignatureContext;
    backupStatement(): BackupStatementContext;
    cancelBackupStatement(): CancelBackupStatementContext;
    showBackupStatement(): ShowBackupStatementContext;
    restoreStatement(): RestoreStatementContext;
    cancelRestoreStatement(): CancelRestoreStatementContext;
    showRestoreStatement(): ShowRestoreStatementContext;
    showSnapshotStatement(): ShowSnapshotStatementContext;
    createRepositoryStatement(): CreateRepositoryStatementContext;
    dropRepositoryStatement(): DropRepositoryStatementContext;
    addSqlBlackListStatement(): AddSqlBlackListStatementContext;
    delSqlBlackListStatement(): DelSqlBlackListStatementContext;
    showSqlBlackListStatement(): ShowSqlBlackListStatementContext;
    showWhiteListStatement(): ShowWhiteListStatementContext;
    dataCacheTarget(): DataCacheTargetContext;
    createDataCacheRuleStatement(): CreateDataCacheRuleStatementContext;
    showDataCacheRulesStatement(): ShowDataCacheRulesStatementContext;
    dropDataCacheRuleStatement(): DropDataCacheRuleStatementContext;
    clearDataCacheRulesStatement(): ClearDataCacheRulesStatementContext;
    exportStatement(): ExportStatementContext;
    cancelExportStatement(): CancelExportStatementContext;
    showExportStatement(): ShowExportStatementContext;
    installPluginStatement(): InstallPluginStatementContext;
    uninstallPluginStatement(): UninstallPluginStatementContext;
    createFileStatement(): CreateFileStatementContext;
    dropFileStatement(): DropFileStatementContext;
    showSmallFilesStatement(): ShowSmallFilesStatementContext;
    createPipeStatement(): CreatePipeStatementContext;
    dropPipeStatement(): DropPipeStatementContext;
    alterPipeClause(): AlterPipeClauseContext;
    alterPipeStatement(): AlterPipeStatementContext;
    descPipeStatement(): DescPipeStatementContext;
    showPipeStatement(): ShowPipeStatementContext;
    setStatement(): SetStatementContext;
    setVar(): SetVarContext;
    transaction_characteristics(): Transaction_characteristicsContext;
    transaction_access_mode(): Transaction_access_modeContext;
    isolation_level(): Isolation_levelContext;
    isolation_types(): Isolation_typesContext;
    setExprOrDefault(): SetExprOrDefaultContext;
    setUserPropertyStatement(): SetUserPropertyStatementContext;
    roleList(): RoleListContext;
    executeScriptStatement(): ExecuteScriptStatementContext;
    unsupportedStatement(): UnsupportedStatementContext;
    lock_item(): Lock_itemContext;
    lock_type(): Lock_typeContext;
    queryStatement(): QueryStatementContext;
    queryRelation(): QueryRelationContext;
    withClause(): WithClauseContext;
    queryNoWith(): QueryNoWithContext;
    temporalClause(): TemporalClauseContext;
    queryPrimary(): QueryPrimaryContext;
    queryPrimary(_p: number): QueryPrimaryContext;
    subquery(): SubqueryContext;
    rowConstructor(): RowConstructorContext;
    sortItem(): SortItemContext;
    limitElement(): LimitElementContext;
    querySpecification(): QuerySpecificationContext;
    fromClause(): FromClauseContext;
    groupingElement(): GroupingElementContext;
    groupingSet(): GroupingSetContext;
    commonTableExpression(): CommonTableExpressionContext;
    setQuantifier(): SetQuantifierContext;
    selectItem(): SelectItemContext;
    relations(): RelationsContext;
    relation(): RelationContext;
    relationPrimary(): RelationPrimaryContext;
    joinRelation(): JoinRelationContext;
    crossOrInnerJoinType(): CrossOrInnerJoinTypeContext;
    outerAndSemiJoinType(): OuterAndSemiJoinTypeContext;
    bracketHint(): BracketHintContext;
    setVarHint(): SetVarHintContext;
    hintMap(): HintMapContext;
    joinCriteria(): JoinCriteriaContext;
    columnAliases(): ColumnAliasesContext;
    partitionNames(): PartitionNamesContext;
    keyPartitions(): KeyPartitionsContext;
    tabletList(): TabletListContext;
    prepareStatement(): PrepareStatementContext;
    prepareSql(): PrepareSqlContext;
    executeStatement(): ExecuteStatementContext;
    deallocateStatement(): DeallocateStatementContext;
    replicaList(): ReplicaListContext;
    expressionsWithDefault(): ExpressionsWithDefaultContext;
    expressionOrDefault(): ExpressionOrDefaultContext;
    mapExpressionList(): MapExpressionListContext;
    mapExpression(): MapExpressionContext;
    expressionSingleton(): ExpressionSingletonContext;
    expression(): ExpressionContext;
    expression(_p: number): ExpressionContext;
    expressionList(): ExpressionListContext;
    booleanExpression(): BooleanExpressionContext;
    booleanExpression(_p: number): BooleanExpressionContext;
    predicate(): PredicateContext;
    tupleInSubquery(): TupleInSubqueryContext;
    predicateOperations(value: ParserRuleContext): PredicateOperationsContext;
    valueExpression(): ValueExpressionContext;
    valueExpression(_p: number): ValueExpressionContext;
    primaryExpression(): PrimaryExpressionContext;
    primaryExpression(_p: number): PrimaryExpressionContext;
    literalExpression(): LiteralExpressionContext;
    functionCall(): FunctionCallContext;
    aggregationFunction(): AggregationFunctionContext;
    userVariable(): UserVariableContext;
    systemVariable(): SystemVariableContext;
    columnReference(): ColumnReferenceContext;
    informationFunctionExpression(): InformationFunctionExpressionContext;
    specialDateTimeExpression(): SpecialDateTimeExpressionContext;
    specialFunctionExpression(): SpecialFunctionExpressionContext;
    windowFunction(): WindowFunctionContext;
    whenClause(): WhenClauseContext;
    over(): OverContext;
    ignoreNulls(): IgnoreNullsContext;
    tableDesc(): TableDescContext;
    restoreTableDesc(): RestoreTableDescContext;
    explainDesc(): ExplainDescContext;
    optimizerTrace(): OptimizerTraceContext;
    partitionDesc(): PartitionDescContext;
    listPartitionDesc(): ListPartitionDescContext;
    singleItemListPartitionDesc(): SingleItemListPartitionDescContext;
    multiItemListPartitionDesc(): MultiItemListPartitionDescContext;
    stringList(): StringListContext;
    rangePartitionDesc(): RangePartitionDescContext;
    singleRangePartition(): SingleRangePartitionContext;
    multiRangePartition(): MultiRangePartitionContext;
    partitionRangeDesc(): PartitionRangeDescContext;
    partitionKeyDesc(): PartitionKeyDescContext;
    partitionValueList(): PartitionValueListContext;
    keyPartition(): KeyPartitionContext;
    partitionValue(): PartitionValueContext;
    distributionClause(): DistributionClauseContext;
    distributionDesc(): DistributionDescContext;
    refreshSchemeDesc(): RefreshSchemeDescContext;
    statusDesc(): StatusDescContext;
    properties(): PropertiesContext;
    extProperties(): ExtPropertiesContext;
    propertyList(): PropertyListContext;
    userPropertyList(): UserPropertyListContext;
    property(): PropertyContext;
    varType(): VarTypeContext;
    comment(): CommentContext;
    outfile(): OutfileContext;
    fileFormat(): FileFormatContext;
    string(): StringContext;
    binary(): BinaryContext;
    comparisonOperator(): ComparisonOperatorContext;
    booleanValue(): BooleanValueContext;
    interval(): IntervalContext;
    unitIdentifier(): UnitIdentifierContext;
    unitBoundary(): UnitBoundaryContext;
    type(): TypeContext;
    arrayType(): ArrayTypeContext;
    mapType(): MapTypeContext;
    subfieldDesc(): SubfieldDescContext;
    subfieldDescs(): SubfieldDescsContext;
    structType(): StructTypeContext;
    typeParameter(): TypeParameterContext;
    baseType(): BaseTypeContext;
    decimalType(): DecimalTypeContext;
    qualifiedName(): QualifiedNameContext;
    identifier(): IdentifierContext;
    identifierList(): IdentifierListContext;
    identifierOrString(): IdentifierOrStringContext;
    identifierOrStringList(): IdentifierOrStringListContext;
    identifierOrStringOrStar(): IdentifierOrStringOrStarContext;
    user(): UserContext;
    assignment(): AssignmentContext;
    assignmentList(): AssignmentListContext;
    number(): NumberContext;
    nonReserved(): NonReservedContext;
    sempred(_localctx: RuleContext, ruleIndex: number, predIndex: number): boolean;
    private queryPrimary_sempred;
    private expression_sempred;
    private booleanExpression_sempred;
    private valueExpression_sempred;
    private primaryExpression_sempred;
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
    static readonly _serializedATN: string;
    static __ATN: ATN;
    static get _ATN(): ATN;
}
export declare class ProgramContext extends ParserRuleContext {
    EOF(): TerminalNode;
    sqlStatements(): SqlStatementsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SqlStatementsContext extends ParserRuleContext {
    statement(): StatementContext[];
    statement(i: number): StatementContext;
    emptyStatement(): EmptyStatementContext[];
    emptyStatement(i: number): EmptyStatementContext;
    SEMI(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class EmptyStatementContext extends ParserRuleContext {
    SEMICOLON(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class StatementContext extends ParserRuleContext {
    queryStatement(): QueryStatementContext | undefined;
    showWarehousesStatement(): ShowWarehousesStatementContext | undefined;
    showClustersStatement(): ShowClustersStatementContext | undefined;
    useDatabaseStatement(): UseDatabaseStatementContext | undefined;
    useCatalogStatement(): UseCatalogStatementContext | undefined;
    setCatalogStatement(): SetCatalogStatementContext | undefined;
    showDatabasesStatement(): ShowDatabasesStatementContext | undefined;
    alterDbQuotaStatement(): AlterDbQuotaStatementContext | undefined;
    createDbStatement(): CreateDbStatementContext | undefined;
    dropDbStatement(): DropDbStatementContext | undefined;
    showCreateDbStatement(): ShowCreateDbStatementContext | undefined;
    alterDatabaseRenameStatement(): AlterDatabaseRenameStatementContext | undefined;
    recoverDbStmt(): RecoverDbStmtContext | undefined;
    showDataStmt(): ShowDataStmtContext | undefined;
    createTableStatement(): CreateTableStatementContext | undefined;
    createTableAsSelectStatement(): CreateTableAsSelectStatementContext | undefined;
    createTemporaryTableStatement(): CreateTemporaryTableStatementContext | undefined;
    createTableLikeStatement(): CreateTableLikeStatementContext | undefined;
    showCreateTableStatement(): ShowCreateTableStatementContext | undefined;
    dropTableStatement(): DropTableStatementContext | undefined;
    recoverTableStatement(): RecoverTableStatementContext | undefined;
    truncateTableStatement(): TruncateTableStatementContext | undefined;
    showTableStatement(): ShowTableStatementContext | undefined;
    descTableStatement(): DescTableStatementContext | undefined;
    showTableStatusStatement(): ShowTableStatusStatementContext | undefined;
    showColumnStatement(): ShowColumnStatementContext | undefined;
    refreshTableStatement(): RefreshTableStatementContext | undefined;
    alterTableStatement(): AlterTableStatementContext | undefined;
    cancelAlterTableStatement(): CancelAlterTableStatementContext | undefined;
    showAlterStatement(): ShowAlterStatementContext | undefined;
    createViewStatement(): CreateViewStatementContext | undefined;
    alterViewStatement(): AlterViewStatementContext | undefined;
    dropViewStatement(): DropViewStatementContext | undefined;
    showPartitionsStatement(): ShowPartitionsStatementContext | undefined;
    recoverPartitionStatement(): RecoverPartitionStatementContext | undefined;
    createIndexStatement(): CreateIndexStatementContext | undefined;
    dropIndexStatement(): DropIndexStatementContext | undefined;
    showIndexStatement(): ShowIndexStatementContext | undefined;
    submitTaskStatement(): SubmitTaskStatementContext | undefined;
    dropTaskStatement(): DropTaskStatementContext | undefined;
    createMaterializedViewStatement(): CreateMaterializedViewStatementContext | undefined;
    showMaterializedViewsStatement(): ShowMaterializedViewsStatementContext | undefined;
    dropMaterializedViewStatement(): DropMaterializedViewStatementContext | undefined;
    alterMaterializedViewStatement(): AlterMaterializedViewStatementContext | undefined;
    refreshMaterializedViewStatement(): RefreshMaterializedViewStatementContext | undefined;
    cancelRefreshMaterializedViewStatement(): CancelRefreshMaterializedViewStatementContext | undefined;
    createExternalCatalogStatement(): CreateExternalCatalogStatementContext | undefined;
    dropExternalCatalogStatement(): DropExternalCatalogStatementContext | undefined;
    showCatalogsStatement(): ShowCatalogsStatementContext | undefined;
    showCreateExternalCatalogStatement(): ShowCreateExternalCatalogStatementContext | undefined;
    alterCatalogStatement(): AlterCatalogStatementContext | undefined;
    insertStatement(): InsertStatementContext | undefined;
    updateStatement(): UpdateStatementContext | undefined;
    deleteStatement(): DeleteStatementContext | undefined;
    createRoutineLoadStatement(): CreateRoutineLoadStatementContext | undefined;
    alterRoutineLoadStatement(): AlterRoutineLoadStatementContext | undefined;
    stopRoutineLoadStatement(): StopRoutineLoadStatementContext | undefined;
    resumeRoutineLoadStatement(): ResumeRoutineLoadStatementContext | undefined;
    pauseRoutineLoadStatement(): PauseRoutineLoadStatementContext | undefined;
    showRoutineLoadStatement(): ShowRoutineLoadStatementContext | undefined;
    showRoutineLoadTaskStatement(): ShowRoutineLoadTaskStatementContext | undefined;
    showCreateRoutineLoadStatement(): ShowCreateRoutineLoadStatementContext | undefined;
    showStreamLoadStatement(): ShowStreamLoadStatementContext | undefined;
    adminSetConfigStatement(): AdminSetConfigStatementContext | undefined;
    adminSetReplicaStatusStatement(): AdminSetReplicaStatusStatementContext | undefined;
    adminShowConfigStatement(): AdminShowConfigStatementContext | undefined;
    adminShowReplicaDistributionStatement(): AdminShowReplicaDistributionStatementContext | undefined;
    adminShowReplicaStatusStatement(): AdminShowReplicaStatusStatementContext | undefined;
    adminRepairTableStatement(): AdminRepairTableStatementContext | undefined;
    adminCancelRepairTableStatement(): AdminCancelRepairTableStatementContext | undefined;
    adminCheckTabletsStatement(): AdminCheckTabletsStatementContext | undefined;
    killStatement(): KillStatementContext | undefined;
    syncStatement(): SyncStatementContext | undefined;
    executeScriptStatement(): ExecuteScriptStatementContext | undefined;
    alterSystemStatement(): AlterSystemStatementContext | undefined;
    cancelAlterSystemStatement(): CancelAlterSystemStatementContext | undefined;
    showComputeNodesStatement(): ShowComputeNodesStatementContext | undefined;
    analyzeStatement(): AnalyzeStatementContext | undefined;
    dropStatsStatement(): DropStatsStatementContext | undefined;
    createAnalyzeStatement(): CreateAnalyzeStatementContext | undefined;
    dropAnalyzeJobStatement(): DropAnalyzeJobStatementContext | undefined;
    analyzeHistogramStatement(): AnalyzeHistogramStatementContext | undefined;
    dropHistogramStatement(): DropHistogramStatementContext | undefined;
    showAnalyzeStatement(): ShowAnalyzeStatementContext | undefined;
    showStatsMetaStatement(): ShowStatsMetaStatementContext | undefined;
    showHistogramMetaStatement(): ShowHistogramMetaStatementContext | undefined;
    killAnalyzeStatement(): KillAnalyzeStatementContext | undefined;
    analyzeProfileStatement(): AnalyzeProfileStatementContext | undefined;
    createResourceGroupStatement(): CreateResourceGroupStatementContext | undefined;
    dropResourceGroupStatement(): DropResourceGroupStatementContext | undefined;
    alterResourceGroupStatement(): AlterResourceGroupStatementContext | undefined;
    showResourceGroupStatement(): ShowResourceGroupStatementContext | undefined;
    showResourceGroupUsageStatement(): ShowResourceGroupUsageStatementContext | undefined;
    createResourceStatement(): CreateResourceStatementContext | undefined;
    alterResourceStatement(): AlterResourceStatementContext | undefined;
    dropResourceStatement(): DropResourceStatementContext | undefined;
    showResourceStatement(): ShowResourceStatementContext | undefined;
    showFunctionsStatement(): ShowFunctionsStatementContext | undefined;
    dropFunctionStatement(): DropFunctionStatementContext | undefined;
    createFunctionStatement(): CreateFunctionStatementContext | undefined;
    loadStatement(): LoadStatementContext | undefined;
    showLoadStatement(): ShowLoadStatementContext | undefined;
    showLoadWarningsStatement(): ShowLoadWarningsStatementContext | undefined;
    cancelLoadStatement(): CancelLoadStatementContext | undefined;
    alterLoadStatement(): AlterLoadStatementContext | undefined;
    showAuthorStatement(): ShowAuthorStatementContext | undefined;
    showBackendsStatement(): ShowBackendsStatementContext | undefined;
    showBrokerStatement(): ShowBrokerStatementContext | undefined;
    showCharsetStatement(): ShowCharsetStatementContext | undefined;
    showCollationStatement(): ShowCollationStatementContext | undefined;
    showDeleteStatement(): ShowDeleteStatementContext | undefined;
    showDynamicPartitionStatement(): ShowDynamicPartitionStatementContext | undefined;
    showEventsStatement(): ShowEventsStatementContext | undefined;
    showEnginesStatement(): ShowEnginesStatementContext | undefined;
    showFrontendsStatement(): ShowFrontendsStatementContext | undefined;
    showPluginsStatement(): ShowPluginsStatementContext | undefined;
    showRepositoriesStatement(): ShowRepositoriesStatementContext | undefined;
    showOpenTableStatement(): ShowOpenTableStatementContext | undefined;
    showPrivilegesStatement(): ShowPrivilegesStatementContext | undefined;
    showProcedureStatement(): ShowProcedureStatementContext | undefined;
    showProcStatement(): ShowProcStatementContext | undefined;
    showProcesslistStatement(): ShowProcesslistStatementContext | undefined;
    showProfilelistStatement(): ShowProfilelistStatementContext | undefined;
    showRunningQueriesStatement(): ShowRunningQueriesStatementContext | undefined;
    showStatusStatement(): ShowStatusStatementContext | undefined;
    showTabletStatement(): ShowTabletStatementContext | undefined;
    showTransactionStatement(): ShowTransactionStatementContext | undefined;
    showTriggersStatement(): ShowTriggersStatementContext | undefined;
    showUserPropertyStatement(): ShowUserPropertyStatementContext | undefined;
    showVariablesStatement(): ShowVariablesStatementContext | undefined;
    showWarningStatement(): ShowWarningStatementContext | undefined;
    helpStatement(): HelpStatementContext | undefined;
    createUserStatement(): CreateUserStatementContext | undefined;
    dropUserStatement(): DropUserStatementContext | undefined;
    alterUserStatement(): AlterUserStatementContext | undefined;
    showUserStatement(): ShowUserStatementContext | undefined;
    showAuthenticationStatement(): ShowAuthenticationStatementContext | undefined;
    executeAsStatement(): ExecuteAsStatementContext | undefined;
    createRoleStatement(): CreateRoleStatementContext | undefined;
    alterRoleStatement(): AlterRoleStatementContext | undefined;
    dropRoleStatement(): DropRoleStatementContext | undefined;
    showRolesStatement(): ShowRolesStatementContext | undefined;
    grantRoleStatement(): GrantRoleStatementContext | undefined;
    revokeRoleStatement(): RevokeRoleStatementContext | undefined;
    setRoleStatement(): SetRoleStatementContext | undefined;
    setDefaultRoleStatement(): SetDefaultRoleStatementContext | undefined;
    grantPrivilegeStatement(): GrantPrivilegeStatementContext | undefined;
    revokePrivilegeStatement(): RevokePrivilegeStatementContext | undefined;
    showGrantsStatement(): ShowGrantsStatementContext | undefined;
    createSecurityIntegrationStatement(): CreateSecurityIntegrationStatementContext | undefined;
    alterSecurityIntegrationStatement(): AlterSecurityIntegrationStatementContext | undefined;
    dropSecurityIntegrationStatement(): DropSecurityIntegrationStatementContext | undefined;
    showSecurityIntegrationStatement(): ShowSecurityIntegrationStatementContext | undefined;
    showCreateSecurityIntegrationStatement(): ShowCreateSecurityIntegrationStatementContext | undefined;
    createRoleMappingStatement(): CreateRoleMappingStatementContext | undefined;
    alterRoleMappingStatement(): AlterRoleMappingStatementContext | undefined;
    dropRoleMappingStatement(): DropRoleMappingStatementContext | undefined;
    showRoleMappingStatement(): ShowRoleMappingStatementContext | undefined;
    refreshRoleMappingStatement(): RefreshRoleMappingStatementContext | undefined;
    createMaskingPolicyStatement(): CreateMaskingPolicyStatementContext | undefined;
    dropMaskingPolicyStatement(): DropMaskingPolicyStatementContext | undefined;
    alterMaskingPolicyStatement(): AlterMaskingPolicyStatementContext | undefined;
    showMaskingPolicyStatement(): ShowMaskingPolicyStatementContext | undefined;
    showCreateMaskingPolicyStatement(): ShowCreateMaskingPolicyStatementContext | undefined;
    createRowAccessPolicyStatement(): CreateRowAccessPolicyStatementContext | undefined;
    dropRowAccessPolicyStatement(): DropRowAccessPolicyStatementContext | undefined;
    alterRowAccessPolicyStatement(): AlterRowAccessPolicyStatementContext | undefined;
    showRowAccessPolicyStatement(): ShowRowAccessPolicyStatementContext | undefined;
    showCreateRowAccessPolicyStatement(): ShowCreateRowAccessPolicyStatementContext | undefined;
    backupStatement(): BackupStatementContext | undefined;
    cancelBackupStatement(): CancelBackupStatementContext | undefined;
    showBackupStatement(): ShowBackupStatementContext | undefined;
    restoreStatement(): RestoreStatementContext | undefined;
    cancelRestoreStatement(): CancelRestoreStatementContext | undefined;
    showRestoreStatement(): ShowRestoreStatementContext | undefined;
    showSnapshotStatement(): ShowSnapshotStatementContext | undefined;
    createRepositoryStatement(): CreateRepositoryStatementContext | undefined;
    dropRepositoryStatement(): DropRepositoryStatementContext | undefined;
    addSqlBlackListStatement(): AddSqlBlackListStatementContext | undefined;
    delSqlBlackListStatement(): DelSqlBlackListStatementContext | undefined;
    showSqlBlackListStatement(): ShowSqlBlackListStatementContext | undefined;
    showWhiteListStatement(): ShowWhiteListStatementContext | undefined;
    createDataCacheRuleStatement(): CreateDataCacheRuleStatementContext | undefined;
    showDataCacheRulesStatement(): ShowDataCacheRulesStatementContext | undefined;
    dropDataCacheRuleStatement(): DropDataCacheRuleStatementContext | undefined;
    clearDataCacheRulesStatement(): ClearDataCacheRulesStatementContext | undefined;
    exportStatement(): ExportStatementContext | undefined;
    cancelExportStatement(): CancelExportStatementContext | undefined;
    showExportStatement(): ShowExportStatementContext | undefined;
    installPluginStatement(): InstallPluginStatementContext | undefined;
    uninstallPluginStatement(): UninstallPluginStatementContext | undefined;
    createFileStatement(): CreateFileStatementContext | undefined;
    dropFileStatement(): DropFileStatementContext | undefined;
    showSmallFilesStatement(): ShowSmallFilesStatementContext | undefined;
    setStatement(): SetStatementContext | undefined;
    setUserPropertyStatement(): SetUserPropertyStatementContext | undefined;
    createStorageVolumeStatement(): CreateStorageVolumeStatementContext | undefined;
    alterStorageVolumeStatement(): AlterStorageVolumeStatementContext | undefined;
    dropStorageVolumeStatement(): DropStorageVolumeStatementContext | undefined;
    showStorageVolumesStatement(): ShowStorageVolumesStatementContext | undefined;
    descStorageVolumeStatement(): DescStorageVolumeStatementContext | undefined;
    setDefaultStorageVolumeStatement(): SetDefaultStorageVolumeStatementContext | undefined;
    createPipeStatement(): CreatePipeStatementContext | undefined;
    dropPipeStatement(): DropPipeStatementContext | undefined;
    alterPipeStatement(): AlterPipeStatementContext | undefined;
    showPipeStatement(): ShowPipeStatementContext | undefined;
    descPipeStatement(): DescPipeStatementContext | undefined;
    cancelCompactionStatement(): CancelCompactionStatementContext | undefined;
    updateFailPointStatusStatement(): UpdateFailPointStatusStatementContext | undefined;
    showFailPointStatement(): ShowFailPointStatementContext | undefined;
    prepareStatement(): PrepareStatementContext | undefined;
    executeStatement(): ExecuteStatementContext | undefined;
    deallocateStatement(): DeallocateStatementContext | undefined;
    unsupportedStatement(): UnsupportedStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UseDatabaseStatementContext extends ParserRuleContext {
    USE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UseCatalogStatementContext extends ParserRuleContext {
    USE(): TerminalNode;
    string(): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetCatalogStatementContext extends ParserRuleContext {
    SET(): TerminalNode;
    CATALOG(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowDatabasesStatementContext extends ParserRuleContext {
    _catalog: QualifiedNameContext;
    _pattern: StringContext;
    SHOW(): TerminalNode;
    DATABASES(): TerminalNode | undefined;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    SCHEMAS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterDbQuotaStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    DATABASE(): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    SET(): TerminalNode;
    DATA(): TerminalNode | undefined;
    QUOTA(): TerminalNode;
    REPLICA(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateDbStatementContext extends ParserRuleContext {
    _catalog: IdentifierContext;
    _database: IdentifierContext;
    CREATE(): TerminalNode;
    DATABASE(): TerminalNode | undefined;
    SCHEMA(): TerminalNode | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    charsetDesc(): CharsetDescContext | undefined;
    collateDesc(): CollateDescContext | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropDbStatementContext extends ParserRuleContext {
    _catalog: IdentifierContext;
    _database: IdentifierContext;
    DROP(): TerminalNode;
    DATABASE(): TerminalNode | undefined;
    SCHEMA(): TerminalNode | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    FORCE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowCreateDbStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    CREATE(): TerminalNode;
    identifier(): IdentifierContext;
    DATABASE(): TerminalNode | undefined;
    SCHEMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterDatabaseRenameStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    DATABASE(): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    RENAME(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RecoverDbStmtContext extends ParserRuleContext {
    RECOVER(): TerminalNode;
    identifier(): IdentifierContext;
    DATABASE(): TerminalNode | undefined;
    SCHEMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowDataStmtContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    DATA(): TerminalNode;
    FROM(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateTableStatementContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    columnDesc(): ColumnDescContext[];
    columnDesc(i: number): ColumnDescContext;
    EXTERNAL(): TerminalNode | undefined;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    indexDesc(): IndexDescContext[];
    indexDesc(i: number): IndexDescContext;
    engineDesc(): EngineDescContext | undefined;
    charsetDesc(): CharsetDescContext | undefined;
    keyDesc(): KeyDescContext | undefined;
    withRowAccessPolicy(): WithRowAccessPolicyContext[];
    withRowAccessPolicy(i: number): WithRowAccessPolicyContext;
    comment(): CommentContext | undefined;
    partitionDesc(): PartitionDescContext | undefined;
    distributionDesc(): DistributionDescContext | undefined;
    orderByDesc(): OrderByDescContext | undefined;
    rollupDesc(): RollupDescContext | undefined;
    properties(): PropertiesContext | undefined;
    extProperties(): ExtPropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ColumnDescContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    type(): TypeContext;
    charsetName(): CharsetNameContext | undefined;
    KEY(): TerminalNode | undefined;
    aggDesc(): AggDescContext | undefined;
    NULL(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    defaultDesc(): DefaultDescContext | undefined;
    AUTO_INCREMENT(): TerminalNode | undefined;
    generatedColumnDesc(): GeneratedColumnDescContext | undefined;
    withMaskingPolicy(): WithMaskingPolicyContext | undefined;
    comment(): CommentContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CharsetNameContext extends ParserRuleContext {
    CHAR(): TerminalNode | undefined;
    SET(): TerminalNode | undefined;
    identifier(): IdentifierContext;
    CHARSET(): TerminalNode | undefined;
    CHARACTER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DefaultDescContext extends ParserRuleContext {
    DEFAULT(): TerminalNode;
    string(): StringContext | undefined;
    NULL(): TerminalNode | undefined;
    CURRENT_TIMESTAMP(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GeneratedColumnDescContext extends ParserRuleContext {
    AS(): TerminalNode;
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class IndexDescContext extends ParserRuleContext {
    _indexName: IdentifierContext;
    INDEX(): TerminalNode;
    identifierList(): IdentifierListContext;
    identifier(): IdentifierContext;
    indexType(): IndexTypeContext | undefined;
    comment(): CommentContext | undefined;
    propertyList(): PropertyListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class EngineDescContext extends ParserRuleContext {
    ENGINE(): TerminalNode;
    EQ(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CharsetDescContext extends ParserRuleContext {
    identifierOrString(): IdentifierOrStringContext;
    CHAR(): TerminalNode | undefined;
    SET(): TerminalNode | undefined;
    CHARSET(): TerminalNode | undefined;
    CHARACTER(): TerminalNode | undefined;
    DEFAULT(): TerminalNode | undefined;
    EQ(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CollateDescContext extends ParserRuleContext {
    COLLATE(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    DEFAULT(): TerminalNode | undefined;
    EQ(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class KeyDescContext extends ParserRuleContext {
    KEY(): TerminalNode;
    identifierList(): IdentifierListContext;
    AGGREGATE(): TerminalNode | undefined;
    UNIQUE(): TerminalNode | undefined;
    PRIMARY(): TerminalNode | undefined;
    DUPLICATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class OrderByDescContext extends ParserRuleContext {
    ORDER(): TerminalNode;
    BY(): TerminalNode;
    identifierList(): IdentifierListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AggDescContext extends ParserRuleContext {
    SUM(): TerminalNode | undefined;
    MAX(): TerminalNode | undefined;
    MIN(): TerminalNode | undefined;
    REPLACE(): TerminalNode | undefined;
    HLL_UNION(): TerminalNode | undefined;
    BITMAP_UNION(): TerminalNode | undefined;
    PERCENTILE_UNION(): TerminalNode | undefined;
    REPLACE_IF_NOT_NULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RollupDescContext extends ParserRuleContext {
    ROLLUP(): TerminalNode;
    rollupItem(): RollupItemContext[];
    rollupItem(i: number): RollupItemContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RollupItemContext extends ParserRuleContext {
    _rollupName: IdentifierContext;
    identifierList(): IdentifierListContext;
    identifier(): IdentifierContext;
    dupKeys(): DupKeysContext | undefined;
    fromRollup(): FromRollupContext | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DupKeysContext extends ParserRuleContext {
    DUPLICATE(): TerminalNode;
    KEY(): TerminalNode;
    identifierList(): IdentifierListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class FromRollupContext extends ParserRuleContext {
    FROM(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class WithMaskingPolicyContext extends ParserRuleContext {
    _policyName: QualifiedNameContext;
    WITH(): TerminalNode;
    MASKING(): TerminalNode;
    POLICY(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    USING(): TerminalNode | undefined;
    identifierList(): IdentifierListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class WithRowAccessPolicyContext extends ParserRuleContext {
    _policyName: QualifiedNameContext;
    WITH(): TerminalNode;
    ROW(): TerminalNode;
    ACCESS(): TerminalNode;
    POLICY(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    ON(): TerminalNode | undefined;
    identifierList(): IdentifierListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateTemporaryTableStatementContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    TEMPORARY(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    queryStatement(): QueryStatementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateTableAsSelectStatementContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    AS(): TerminalNode;
    queryStatement(): QueryStatementContext;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    keyDesc(): KeyDescContext | undefined;
    comment(): CommentContext | undefined;
    partitionDesc(): PartitionDescContext | undefined;
    distributionDesc(): DistributionDescContext | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropTableStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    TEMPORARY(): TerminalNode | undefined;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    FORCE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterTableStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    alterClause(): AlterClauseContext[];
    alterClause(i: number): AlterClauseContext;
    ADD(): TerminalNode | undefined;
    ROLLUP(): TerminalNode | undefined;
    rollupItem(): RollupItemContext[];
    rollupItem(i: number): RollupItemContext;
    DROP(): TerminalNode | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateIndexStatementContext extends ParserRuleContext {
    _indexName: IdentifierContext;
    CREATE(): TerminalNode;
    INDEX(): TerminalNode;
    ON(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    identifierList(): IdentifierListContext;
    identifier(): IdentifierContext;
    indexType(): IndexTypeContext | undefined;
    comment(): CommentContext | undefined;
    propertyList(): PropertyListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropIndexStatementContext extends ParserRuleContext {
    _indexName: IdentifierContext;
    DROP(): TerminalNode;
    INDEX(): TerminalNode;
    ON(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class IndexTypeContext extends ParserRuleContext {
    USING(): TerminalNode;
    BITMAP(): TerminalNode | undefined;
    GIN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowTableStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _pattern: StringContext;
    SHOW(): TerminalNode;
    TABLES(): TerminalNode;
    FULL(): TerminalNode | undefined;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowCreateTableStatementContext extends ParserRuleContext {
    _table: QualifiedNameContext;
    SHOW(): TerminalNode;
    CREATE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    TABLE(): TerminalNode | undefined;
    VIEW(): TerminalNode | undefined;
    MATERIALIZED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowColumnStatementContext extends ParserRuleContext {
    _table: QualifiedNameContext;
    _db: QualifiedNameContext;
    _pattern: StringContext;
    SHOW(): TerminalNode;
    COLUMNS(): TerminalNode | undefined;
    FIELDS(): TerminalNode | undefined;
    FULL(): TerminalNode | undefined;
    FROM(): TerminalNode[];
    FROM(i: number): TerminalNode;
    IN(): TerminalNode[];
    IN(i: number): TerminalNode;
    qualifiedName(): QualifiedNameContext[];
    qualifiedName(i: number): QualifiedNameContext;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowTableStatusStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _pattern: StringContext;
    SHOW(): TerminalNode;
    TABLE(): TerminalNode;
    STATUS(): TerminalNode;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RefreshTableStatementContext extends ParserRuleContext {
    REFRESH(): TerminalNode;
    EXTERNAL(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    PARTITION(): TerminalNode | undefined;
    string(): StringContext[];
    string(i: number): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowAlterStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    SHOW(): TerminalNode;
    ALTER(): TerminalNode;
    TABLE(): TerminalNode | undefined;
    COLUMN(): TerminalNode | undefined;
    ROLLUP(): TerminalNode | undefined;
    OPTIMIZE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    ORDER(): TerminalNode | undefined;
    BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    limitElement(): LimitElementContext | undefined;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    MATERIALIZED(): TerminalNode | undefined;
    VIEW(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DescTableStatementContext extends ParserRuleContext {
    _table: QualifiedNameContext;
    DESC(): TerminalNode | undefined;
    DESCRIBE(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext;
    ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateTableLikeStatementContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext[];
    qualifiedName(i: number): QualifiedNameContext;
    LIKE(): TerminalNode;
    EXTERNAL(): TerminalNode | undefined;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    partitionDesc(): PartitionDescContext | undefined;
    distributionDesc(): DistributionDescContext | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowIndexStatementContext extends ParserRuleContext {
    _table: QualifiedNameContext;
    _db: QualifiedNameContext;
    SHOW(): TerminalNode;
    INDEX(): TerminalNode | undefined;
    INDEXES(): TerminalNode | undefined;
    KEY(): TerminalNode | undefined;
    KEYS(): TerminalNode | undefined;
    FROM(): TerminalNode[];
    FROM(i: number): TerminalNode;
    IN(): TerminalNode[];
    IN(i: number): TerminalNode;
    qualifiedName(): QualifiedNameContext[];
    qualifiedName(i: number): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RecoverTableStatementContext extends ParserRuleContext {
    RECOVER(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class TruncateTableStatementContext extends ParserRuleContext {
    TRUNCATE(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    partitionNames(): PartitionNamesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CancelAlterTableStatementContext extends ParserRuleContext {
    CANCEL(): TerminalNode;
    ALTER(): TerminalNode;
    TABLE(): TerminalNode | undefined;
    FROM(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    INTEGER_VALUE(): TerminalNode[];
    INTEGER_VALUE(i: number): TerminalNode;
    COLUMN(): TerminalNode | undefined;
    ROLLUP(): TerminalNode | undefined;
    OPTIMIZE(): TerminalNode | undefined;
    MATERIALIZED(): TerminalNode | undefined;
    VIEW(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowPartitionsStatementContext extends ParserRuleContext {
    _table: QualifiedNameContext;
    SHOW(): TerminalNode;
    PARTITIONS(): TerminalNode;
    FROM(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    TEMPORARY(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    ORDER(): TerminalNode | undefined;
    BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    limitElement(): LimitElementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RecoverPartitionStatementContext extends ParserRuleContext {
    _table: QualifiedNameContext;
    RECOVER(): TerminalNode;
    PARTITION(): TerminalNode;
    identifier(): IdentifierContext;
    FROM(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateViewStatementContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    VIEW(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    AS(): TerminalNode;
    queryStatement(): QueryStatementContext;
    OR(): TerminalNode | undefined;
    REPLACE(): TerminalNode | undefined;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    columnNameWithComment(): ColumnNameWithCommentContext[];
    columnNameWithComment(i: number): ColumnNameWithCommentContext;
    withRowAccessPolicy(): WithRowAccessPolicyContext[];
    withRowAccessPolicy(i: number): WithRowAccessPolicyContext;
    comment(): CommentContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterViewStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    VIEW(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    AS(): TerminalNode | undefined;
    queryStatement(): QueryStatementContext | undefined;
    columnNameWithComment(): ColumnNameWithCommentContext[];
    columnNameWithComment(i: number): ColumnNameWithCommentContext;
    applyMaskingPolicyClause(): ApplyMaskingPolicyClauseContext | undefined;
    applyRowAccessPolicyClause(): ApplyRowAccessPolicyClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropViewStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    VIEW(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ColumnNameWithCommentContext extends ParserRuleContext {
    _columnName: IdentifierContext;
    identifier(): IdentifierContext;
    withMaskingPolicy(): WithMaskingPolicyContext | undefined;
    comment(): CommentContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SubmitTaskStatementContext extends ParserRuleContext {
    SUBMIT(): TerminalNode;
    TASK(): TerminalNode;
    AS(): TerminalNode;
    createTableAsSelectStatement(): CreateTableAsSelectStatementContext | undefined;
    insertStatement(): InsertStatementContext | undefined;
    setVarHint(): SetVarHintContext[];
    setVarHint(i: number): SetVarHintContext;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropTaskStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    TASK(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateMaterializedViewStatementContext extends ParserRuleContext {
    _mvName: QualifiedNameContext;
    CREATE(): TerminalNode;
    MATERIALIZED(): TerminalNode;
    VIEW(): TerminalNode;
    AS(): TerminalNode;
    queryStatement(): QueryStatementContext;
    qualifiedName(): QualifiedNameContext;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    columnNameWithComment(): ColumnNameWithCommentContext[];
    columnNameWithComment(i: number): ColumnNameWithCommentContext;
    withRowAccessPolicy(): WithRowAccessPolicyContext[];
    withRowAccessPolicy(i: number): WithRowAccessPolicyContext;
    comment(): CommentContext | undefined;
    materializedViewDesc(): MaterializedViewDescContext[];
    materializedViewDesc(i: number): MaterializedViewDescContext;
    indexDesc(): IndexDescContext[];
    indexDesc(i: number): IndexDescContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class MaterializedViewDescContext extends ParserRuleContext {
    PARTITION(): TerminalNode | undefined;
    BY(): TerminalNode | undefined;
    primaryExpression(): PrimaryExpressionContext | undefined;
    distributionDesc(): DistributionDescContext | undefined;
    orderByDesc(): OrderByDescContext | undefined;
    refreshSchemeDesc(): RefreshSchemeDescContext | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowMaterializedViewsStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _pattern: StringContext;
    SHOW(): TerminalNode;
    MATERIALIZED(): TerminalNode;
    VIEWS(): TerminalNode;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropMaterializedViewStatementContext extends ParserRuleContext {
    _mvName: QualifiedNameContext;
    DROP(): TerminalNode;
    MATERIALIZED(): TerminalNode;
    VIEW(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterMaterializedViewStatementContext extends ParserRuleContext {
    _mvName: QualifiedNameContext;
    ALTER(): TerminalNode;
    MATERIALIZED(): TerminalNode;
    VIEW(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    refreshSchemeDesc(): RefreshSchemeDescContext | undefined;
    tableRenameClause(): TableRenameClauseContext | undefined;
    modifyPropertiesClause(): ModifyPropertiesClauseContext | undefined;
    swapTableClause(): SwapTableClauseContext | undefined;
    statusDesc(): StatusDescContext | undefined;
    applyMaskingPolicyClause(): ApplyMaskingPolicyClauseContext | undefined;
    applyRowAccessPolicyClause(): ApplyRowAccessPolicyClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RefreshMaterializedViewStatementContext extends ParserRuleContext {
    _mvName: QualifiedNameContext;
    REFRESH(): TerminalNode;
    MATERIALIZED(): TerminalNode;
    VIEW(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    PARTITION(): TerminalNode | undefined;
    partitionRangeDesc(): PartitionRangeDescContext | undefined;
    FORCE(): TerminalNode | undefined;
    WITH(): TerminalNode | undefined;
    MODE(): TerminalNode | undefined;
    SYNC(): TerminalNode | undefined;
    ASYNC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CancelRefreshMaterializedViewStatementContext extends ParserRuleContext {
    _mvName: QualifiedNameContext;
    CANCEL(): TerminalNode;
    REFRESH(): TerminalNode;
    MATERIALIZED(): TerminalNode;
    VIEW(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AdminSetConfigStatementContext extends ParserRuleContext {
    ADMIN(): TerminalNode;
    SET(): TerminalNode;
    FRONTEND(): TerminalNode;
    CONFIG(): TerminalNode;
    property(): PropertyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AdminSetReplicaStatusStatementContext extends ParserRuleContext {
    ADMIN(): TerminalNode;
    SET(): TerminalNode;
    REPLICA(): TerminalNode;
    STATUS(): TerminalNode;
    properties(): PropertiesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AdminShowConfigStatementContext extends ParserRuleContext {
    _pattern: StringContext;
    ADMIN(): TerminalNode;
    SHOW(): TerminalNode;
    FRONTEND(): TerminalNode;
    CONFIG(): TerminalNode;
    LIKE(): TerminalNode | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AdminShowReplicaDistributionStatementContext extends ParserRuleContext {
    ADMIN(): TerminalNode;
    SHOW(): TerminalNode;
    REPLICA(): TerminalNode;
    DISTRIBUTION(): TerminalNode;
    FROM(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    partitionNames(): PartitionNamesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AdminShowReplicaStatusStatementContext extends ParserRuleContext {
    _where: ExpressionContext;
    ADMIN(): TerminalNode;
    SHOW(): TerminalNode;
    REPLICA(): TerminalNode;
    STATUS(): TerminalNode;
    FROM(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    partitionNames(): PartitionNamesContext | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AdminRepairTableStatementContext extends ParserRuleContext {
    ADMIN(): TerminalNode;
    REPAIR(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    partitionNames(): PartitionNamesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AdminCancelRepairTableStatementContext extends ParserRuleContext {
    ADMIN(): TerminalNode;
    CANCEL(): TerminalNode;
    REPAIR(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    partitionNames(): PartitionNamesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AdminCheckTabletsStatementContext extends ParserRuleContext {
    ADMIN(): TerminalNode;
    CHECK(): TerminalNode;
    tabletList(): TabletListContext;
    PROPERTIES(): TerminalNode;
    property(): PropertyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class KillStatementContext extends ParserRuleContext {
    KILL(): TerminalNode;
    INTEGER_VALUE(): TerminalNode;
    QUERY(): TerminalNode | undefined;
    CONNECTION(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SyncStatementContext extends ParserRuleContext {
    SYNC(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterSystemStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    SYSTEM(): TerminalNode;
    alterClause(): AlterClauseContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CancelAlterSystemStatementContext extends ParserRuleContext {
    CANCEL(): TerminalNode;
    DECOMMISSION(): TerminalNode;
    BACKEND(): TerminalNode;
    string(): StringContext[];
    string(i: number): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowComputeNodesStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    COMPUTE(): TerminalNode;
    NODES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateExternalCatalogStatementContext extends ParserRuleContext {
    _catalogName: IdentifierOrStringContext;
    CREATE(): TerminalNode;
    EXTERNAL(): TerminalNode;
    CATALOG(): TerminalNode;
    properties(): PropertiesContext;
    identifierOrString(): IdentifierOrStringContext;
    comment(): CommentContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowCreateExternalCatalogStatementContext extends ParserRuleContext {
    _catalogName: IdentifierOrStringContext;
    SHOW(): TerminalNode;
    CREATE(): TerminalNode;
    CATALOG(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropExternalCatalogStatementContext extends ParserRuleContext {
    _catalogName: IdentifierOrStringContext;
    DROP(): TerminalNode;
    CATALOG(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowCatalogsStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    CATALOGS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterCatalogStatementContext extends ParserRuleContext {
    _catalogName: IdentifierOrStringContext;
    ALTER(): TerminalNode;
    CATALOG(): TerminalNode;
    modifyPropertiesClause(): ModifyPropertiesClauseContext;
    identifierOrString(): IdentifierOrStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateWarehouseStatementContext extends ParserRuleContext {
    _warehouseName: IdentifierOrStringContext;
    CREATE(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    WAREHOUSE(): TerminalNode | undefined;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowWarehousesStatementContext extends ParserRuleContext {
    _pattern: StringContext;
    SHOW(): TerminalNode;
    WAREHOUSES(): TerminalNode;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropWarehouseStatementContext extends ParserRuleContext {
    _warehouseName: IdentifierOrStringContext;
    DROP(): TerminalNode;
    WAREHOUSE(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterWarehouseStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    WAREHOUSE(): TerminalNode;
    identifier(): IdentifierContext;
    ADD(): TerminalNode | undefined;
    CLUSTER(): TerminalNode | undefined;
    REMOVE(): TerminalNode | undefined;
    SET(): TerminalNode | undefined;
    propertyList(): PropertyListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowClustersStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    CLUSTERS(): TerminalNode;
    FROM(): TerminalNode;
    WAREHOUSE(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SuspendWarehouseStatementContext extends ParserRuleContext {
    SUSPEND(): TerminalNode;
    WAREHOUSE(): TerminalNode;
    identifier(): IdentifierContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ResumeWarehouseStatementContext extends ParserRuleContext {
    RESUME(): TerminalNode;
    WAREHOUSE(): TerminalNode;
    identifier(): IdentifierContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateStorageVolumeStatementContext extends ParserRuleContext {
    _storageVolumeName: IdentifierOrStringContext;
    CREATE(): TerminalNode;
    STORAGE(): TerminalNode;
    VOLUME(): TerminalNode;
    typeDesc(): TypeDescContext;
    locationsDesc(): LocationsDescContext;
    identifierOrString(): IdentifierOrStringContext;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    comment(): CommentContext | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class TypeDescContext extends ParserRuleContext {
    TYPE(): TerminalNode;
    EQ(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class LocationsDescContext extends ParserRuleContext {
    LOCATIONS(): TerminalNode;
    EQ(): TerminalNode;
    stringList(): StringListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowStorageVolumesStatementContext extends ParserRuleContext {
    _pattern: StringContext;
    SHOW(): TerminalNode;
    STORAGE(): TerminalNode;
    VOLUMES(): TerminalNode;
    LIKE(): TerminalNode | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropStorageVolumeStatementContext extends ParserRuleContext {
    _storageVolumeName: IdentifierOrStringContext;
    DROP(): TerminalNode;
    STORAGE(): TerminalNode;
    VOLUME(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterStorageVolumeStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    STORAGE(): TerminalNode;
    VOLUME(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    alterStorageVolumeClause(): AlterStorageVolumeClauseContext[];
    alterStorageVolumeClause(i: number): AlterStorageVolumeClauseContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterStorageVolumeClauseContext extends ParserRuleContext {
    modifyStorageVolumeCommentClause(): ModifyStorageVolumeCommentClauseContext | undefined;
    modifyStorageVolumePropertiesClause(): ModifyStorageVolumePropertiesClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ModifyStorageVolumePropertiesClauseContext extends ParserRuleContext {
    SET(): TerminalNode;
    propertyList(): PropertyListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ModifyStorageVolumeCommentClauseContext extends ParserRuleContext {
    COMMENT(): TerminalNode;
    EQ(): TerminalNode;
    string(): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DescStorageVolumeStatementContext extends ParserRuleContext {
    STORAGE(): TerminalNode;
    VOLUME(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    DESC(): TerminalNode | undefined;
    DESCRIBE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetDefaultStorageVolumeStatementContext extends ParserRuleContext {
    SET(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    AS(): TerminalNode;
    DEFAULT(): TerminalNode;
    STORAGE(): TerminalNode;
    VOLUME(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UpdateFailPointStatusStatementContext extends ParserRuleContext {
    ADMIN(): TerminalNode;
    DISABLE(): TerminalNode | undefined;
    FAILPOINT(): TerminalNode;
    string(): StringContext[];
    string(i: number): StringContext;
    ON(): TerminalNode | undefined;
    BACKEND(): TerminalNode | undefined;
    ENABLE(): TerminalNode | undefined;
    WITH(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    TIMES(): TerminalNode | undefined;
    DECIMAL_VALUE(): TerminalNode | undefined;
    PROBABILITY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowFailPointStatementContext extends ParserRuleContext {
    _pattern: StringContext;
    SHOW(): TerminalNode;
    FAILPOINTS(): TerminalNode;
    ON(): TerminalNode | undefined;
    BACKEND(): TerminalNode | undefined;
    string(): StringContext[];
    string(i: number): StringContext;
    LIKE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterClauseContext extends ParserRuleContext {
    addFrontendClause(): AddFrontendClauseContext | undefined;
    dropFrontendClause(): DropFrontendClauseContext | undefined;
    modifyFrontendHostClause(): ModifyFrontendHostClauseContext | undefined;
    addBackendClause(): AddBackendClauseContext | undefined;
    dropBackendClause(): DropBackendClauseContext | undefined;
    decommissionBackendClause(): DecommissionBackendClauseContext | undefined;
    modifyBackendHostClause(): ModifyBackendHostClauseContext | undefined;
    addComputeNodeClause(): AddComputeNodeClauseContext | undefined;
    dropComputeNodeClause(): DropComputeNodeClauseContext | undefined;
    modifyBrokerClause(): ModifyBrokerClauseContext | undefined;
    alterLoadErrorUrlClause(): AlterLoadErrorUrlClauseContext | undefined;
    createImageClause(): CreateImageClauseContext | undefined;
    cleanTabletSchedQClause(): CleanTabletSchedQClauseContext | undefined;
    createIndexClause(): CreateIndexClauseContext | undefined;
    dropIndexClause(): DropIndexClauseContext | undefined;
    tableRenameClause(): TableRenameClauseContext | undefined;
    swapTableClause(): SwapTableClauseContext | undefined;
    modifyPropertiesClause(): ModifyPropertiesClauseContext | undefined;
    addColumnClause(): AddColumnClauseContext | undefined;
    addColumnsClause(): AddColumnsClauseContext | undefined;
    dropColumnClause(): DropColumnClauseContext | undefined;
    modifyColumnClause(): ModifyColumnClauseContext | undefined;
    columnRenameClause(): ColumnRenameClauseContext | undefined;
    reorderColumnsClause(): ReorderColumnsClauseContext | undefined;
    rollupRenameClause(): RollupRenameClauseContext | undefined;
    compactionClause(): CompactionClauseContext | undefined;
    modifyCommentClause(): ModifyCommentClauseContext | undefined;
    optimizeClause(): OptimizeClauseContext | undefined;
    applyMaskingPolicyClause(): ApplyMaskingPolicyClauseContext | undefined;
    applyRowAccessPolicyClause(): ApplyRowAccessPolicyClauseContext | undefined;
    addPartitionClause(): AddPartitionClauseContext | undefined;
    dropPartitionClause(): DropPartitionClauseContext | undefined;
    distributionClause(): DistributionClauseContext | undefined;
    truncatePartitionClause(): TruncatePartitionClauseContext | undefined;
    modifyPartitionClause(): ModifyPartitionClauseContext | undefined;
    replacePartitionClause(): ReplacePartitionClauseContext | undefined;
    partitionRenameClause(): PartitionRenameClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AddFrontendClauseContext extends ParserRuleContext {
    ADD(): TerminalNode;
    string(): StringContext;
    FOLLOWER(): TerminalNode | undefined;
    OBSERVER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropFrontendClauseContext extends ParserRuleContext {
    DROP(): TerminalNode;
    string(): StringContext;
    FOLLOWER(): TerminalNode | undefined;
    OBSERVER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ModifyFrontendHostClauseContext extends ParserRuleContext {
    MODIFY(): TerminalNode;
    FRONTEND(): TerminalNode;
    HOST(): TerminalNode;
    string(): StringContext[];
    string(i: number): StringContext;
    TO(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AddBackendClauseContext extends ParserRuleContext {
    ADD(): TerminalNode;
    BACKEND(): TerminalNode;
    string(): StringContext[];
    string(i: number): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropBackendClauseContext extends ParserRuleContext {
    DROP(): TerminalNode;
    BACKEND(): TerminalNode;
    string(): StringContext[];
    string(i: number): StringContext;
    FORCE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DecommissionBackendClauseContext extends ParserRuleContext {
    DECOMMISSION(): TerminalNode;
    BACKEND(): TerminalNode;
    string(): StringContext[];
    string(i: number): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ModifyBackendHostClauseContext extends ParserRuleContext {
    MODIFY(): TerminalNode;
    BACKEND(): TerminalNode;
    HOST(): TerminalNode;
    string(): StringContext[];
    string(i: number): StringContext;
    TO(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AddComputeNodeClauseContext extends ParserRuleContext {
    ADD(): TerminalNode;
    COMPUTE(): TerminalNode;
    NODE(): TerminalNode;
    string(): StringContext[];
    string(i: number): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropComputeNodeClauseContext extends ParserRuleContext {
    DROP(): TerminalNode;
    COMPUTE(): TerminalNode;
    NODE(): TerminalNode;
    string(): StringContext[];
    string(i: number): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ModifyBrokerClauseContext extends ParserRuleContext {
    ADD(): TerminalNode | undefined;
    BROKER(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    string(): StringContext[];
    string(i: number): StringContext;
    DROP(): TerminalNode | undefined;
    ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterLoadErrorUrlClauseContext extends ParserRuleContext {
    SET(): TerminalNode;
    LOAD(): TerminalNode;
    ERRORS(): TerminalNode;
    HUB(): TerminalNode;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateImageClauseContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    IMAGE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CleanTabletSchedQClauseContext extends ParserRuleContext {
    CLEAN(): TerminalNode;
    TABLET(): TerminalNode;
    SCHEDULER(): TerminalNode;
    QUEUE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateIndexClauseContext extends ParserRuleContext {
    _indexName: IdentifierContext;
    ADD(): TerminalNode;
    INDEX(): TerminalNode;
    identifierList(): IdentifierListContext;
    identifier(): IdentifierContext;
    indexType(): IndexTypeContext | undefined;
    comment(): CommentContext | undefined;
    propertyList(): PropertyListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropIndexClauseContext extends ParserRuleContext {
    _indexName: IdentifierContext;
    DROP(): TerminalNode;
    INDEX(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class TableRenameClauseContext extends ParserRuleContext {
    RENAME(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SwapTableClauseContext extends ParserRuleContext {
    SWAP(): TerminalNode;
    WITH(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ModifyPropertiesClauseContext extends ParserRuleContext {
    SET(): TerminalNode;
    propertyList(): PropertyListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ModifyCommentClauseContext extends ParserRuleContext {
    COMMENT(): TerminalNode;
    EQ(): TerminalNode;
    string(): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class OptimizeClauseContext extends ParserRuleContext {
    partitionNames(): PartitionNamesContext | undefined;
    keyDesc(): KeyDescContext | undefined;
    partitionDesc(): PartitionDescContext | undefined;
    orderByDesc(): OrderByDescContext | undefined;
    distributionDesc(): DistributionDescContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AddColumnClauseContext extends ParserRuleContext {
    _rollupName: IdentifierContext;
    ADD(): TerminalNode;
    COLUMN(): TerminalNode;
    columnDesc(): ColumnDescContext;
    FIRST(): TerminalNode | undefined;
    AFTER(): TerminalNode | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    properties(): PropertiesContext | undefined;
    TO(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AddColumnsClauseContext extends ParserRuleContext {
    _rollupName: IdentifierContext;
    ADD(): TerminalNode;
    COLUMN(): TerminalNode;
    columnDesc(): ColumnDescContext[];
    columnDesc(i: number): ColumnDescContext;
    properties(): PropertiesContext | undefined;
    TO(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropColumnClauseContext extends ParserRuleContext {
    _rollupName: IdentifierContext;
    DROP(): TerminalNode;
    COLUMN(): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    FROM(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ModifyColumnClauseContext extends ParserRuleContext {
    _rollupName: IdentifierContext;
    MODIFY(): TerminalNode;
    COLUMN(): TerminalNode;
    columnDesc(): ColumnDescContext;
    FIRST(): TerminalNode | undefined;
    AFTER(): TerminalNode | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    FROM(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ColumnRenameClauseContext extends ParserRuleContext {
    _oldColumn: IdentifierContext;
    _newColumn: IdentifierContext;
    RENAME(): TerminalNode;
    COLUMN(): TerminalNode;
    TO(): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ReorderColumnsClauseContext extends ParserRuleContext {
    _rollupName: IdentifierContext;
    ORDER(): TerminalNode;
    BY(): TerminalNode;
    identifierList(): IdentifierListContext;
    FROM(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RollupRenameClauseContext extends ParserRuleContext {
    _rollupName: IdentifierContext;
    _newRollupName: IdentifierContext;
    RENAME(): TerminalNode;
    ROLLUP(): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CompactionClauseContext extends ParserRuleContext {
    COMPACT(): TerminalNode;
    identifier(): IdentifierContext | undefined;
    identifierList(): IdentifierListContext | undefined;
    BASE(): TerminalNode | undefined;
    CUMULATIVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ApplyMaskingPolicyClauseContext extends ParserRuleContext {
    _columnName: IdentifierContext;
    _policyName: QualifiedNameContext;
    MODIFY(): TerminalNode;
    COLUMN(): TerminalNode;
    SET(): TerminalNode | undefined;
    MASKING(): TerminalNode;
    POLICY(): TerminalNode;
    identifier(): IdentifierContext;
    qualifiedName(): QualifiedNameContext | undefined;
    USING(): TerminalNode | undefined;
    identifierList(): IdentifierListContext | undefined;
    UNSET(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ApplyRowAccessPolicyClauseContext extends ParserRuleContext {
    _policyName: QualifiedNameContext;
    ADD(): TerminalNode | undefined;
    ROW(): TerminalNode;
    ACCESS(): TerminalNode;
    POLICY(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    ON(): TerminalNode | undefined;
    identifierList(): IdentifierListContext | undefined;
    DROP(): TerminalNode | undefined;
    ALL(): TerminalNode | undefined;
    POLICIES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AddPartitionClauseContext extends ParserRuleContext {
    ADD(): TerminalNode;
    singleRangePartition(): SingleRangePartitionContext | undefined;
    PARTITIONS(): TerminalNode | undefined;
    multiRangePartition(): MultiRangePartitionContext | undefined;
    TEMPORARY(): TerminalNode | undefined;
    distributionDesc(): DistributionDescContext | undefined;
    properties(): PropertiesContext | undefined;
    singleItemListPartitionDesc(): SingleItemListPartitionDescContext | undefined;
    multiItemListPartitionDesc(): MultiItemListPartitionDescContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropPartitionClauseContext extends ParserRuleContext {
    DROP(): TerminalNode;
    PARTITION(): TerminalNode;
    identifier(): IdentifierContext;
    TEMPORARY(): TerminalNode | undefined;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    FORCE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class TruncatePartitionClauseContext extends ParserRuleContext {
    TRUNCATE(): TerminalNode;
    partitionNames(): PartitionNamesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ModifyPartitionClauseContext extends ParserRuleContext {
    MODIFY(): TerminalNode;
    PARTITION(): TerminalNode;
    SET(): TerminalNode | undefined;
    propertyList(): PropertyListContext | undefined;
    identifier(): IdentifierContext | undefined;
    identifierList(): IdentifierListContext | undefined;
    ASTERISK_SYMBOL(): TerminalNode | undefined;
    distributionDesc(): DistributionDescContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ReplacePartitionClauseContext extends ParserRuleContext {
    _parName: PartitionNamesContext;
    _tempParName: PartitionNamesContext;
    REPLACE(): TerminalNode;
    WITH(): TerminalNode;
    partitionNames(): PartitionNamesContext[];
    partitionNames(i: number): PartitionNamesContext;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PartitionRenameClauseContext extends ParserRuleContext {
    _parName: IdentifierContext;
    _newParName: IdentifierContext;
    RENAME(): TerminalNode;
    PARTITION(): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class InsertStatementContext extends ParserRuleContext {
    _label: IdentifierContext;
    INSERT(): TerminalNode;
    INTO(): TerminalNode | undefined;
    OVERWRITE(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    queryStatement(): QueryStatementContext | undefined;
    explainDesc(): ExplainDescContext | undefined;
    setVarHint(): SetVarHintContext[];
    setVarHint(i: number): SetVarHintContext;
    partitionNames(): PartitionNamesContext | undefined;
    WITH(): TerminalNode | undefined;
    LABEL(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    FILES(): TerminalNode | undefined;
    propertyList(): PropertyListContext | undefined;
    identifier(): IdentifierContext | undefined;
    VALUES(): TerminalNode | undefined;
    expressionsWithDefault(): ExpressionsWithDefaultContext[];
    expressionsWithDefault(i: number): ExpressionsWithDefaultContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UpdateStatementContext extends ParserRuleContext {
    _where: ExpressionContext;
    UPDATE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    SET(): TerminalNode;
    assignmentList(): AssignmentListContext;
    fromClause(): FromClauseContext;
    explainDesc(): ExplainDescContext | undefined;
    withClause(): WithClauseContext | undefined;
    setVarHint(): SetVarHintContext[];
    setVarHint(i: number): SetVarHintContext;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DeleteStatementContext extends ParserRuleContext {
    _using: RelationsContext;
    _where: ExpressionContext;
    DELETE(): TerminalNode;
    FROM(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    explainDesc(): ExplainDescContext | undefined;
    withClause(): WithClauseContext | undefined;
    setVarHint(): SetVarHintContext[];
    setVarHint(i: number): SetVarHintContext;
    partitionNames(): PartitionNamesContext | undefined;
    USING(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    relations(): RelationsContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateRoutineLoadStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _name: IdentifierContext;
    _table: QualifiedNameContext;
    _source: IdentifierContext;
    CREATE(): TerminalNode;
    ROUTINE(): TerminalNode;
    LOAD(): TerminalNode;
    ON(): TerminalNode;
    FROM(): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    qualifiedName(): QualifiedNameContext[];
    qualifiedName(i: number): QualifiedNameContext;
    loadProperties(): LoadPropertiesContext[];
    loadProperties(i: number): LoadPropertiesContext;
    jobProperties(): JobPropertiesContext | undefined;
    dataSourceProperties(): DataSourcePropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterRoutineLoadStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _name: IdentifierContext;
    ALTER(): TerminalNode;
    ROUTINE(): TerminalNode;
    LOAD(): TerminalNode;
    FOR(): TerminalNode;
    identifier(): IdentifierContext;
    loadProperties(): LoadPropertiesContext[];
    loadProperties(i: number): LoadPropertiesContext;
    jobProperties(): JobPropertiesContext | undefined;
    dataSource(): DataSourceContext | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DataSourceContext extends ParserRuleContext {
    _source: IdentifierContext;
    FROM(): TerminalNode;
    dataSourceProperties(): DataSourcePropertiesContext;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class LoadPropertiesContext extends ParserRuleContext {
    colSeparatorProperty(): ColSeparatorPropertyContext | undefined;
    rowDelimiterProperty(): RowDelimiterPropertyContext | undefined;
    importColumns(): ImportColumnsContext | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    partitionNames(): PartitionNamesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ColSeparatorPropertyContext extends ParserRuleContext {
    COLUMNS(): TerminalNode;
    TERMINATED(): TerminalNode;
    BY(): TerminalNode;
    string(): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RowDelimiterPropertyContext extends ParserRuleContext {
    ROWS(): TerminalNode;
    TERMINATED(): TerminalNode;
    BY(): TerminalNode;
    string(): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ImportColumnsContext extends ParserRuleContext {
    COLUMNS(): TerminalNode;
    columnProperties(): ColumnPropertiesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ColumnPropertiesContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext[];
    qualifiedName(i: number): QualifiedNameContext;
    assignment(): AssignmentContext[];
    assignment(i: number): AssignmentContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class JobPropertiesContext extends ParserRuleContext {
    properties(): PropertiesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DataSourcePropertiesContext extends ParserRuleContext {
    propertyList(): PropertyListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class StopRoutineLoadStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _name: IdentifierContext;
    STOP(): TerminalNode;
    ROUTINE(): TerminalNode;
    LOAD(): TerminalNode;
    FOR(): TerminalNode;
    identifier(): IdentifierContext;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ResumeRoutineLoadStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _name: IdentifierContext;
    RESUME(): TerminalNode;
    ROUTINE(): TerminalNode;
    LOAD(): TerminalNode;
    FOR(): TerminalNode;
    identifier(): IdentifierContext;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PauseRoutineLoadStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _name: IdentifierContext;
    PAUSE(): TerminalNode;
    ROUTINE(): TerminalNode;
    LOAD(): TerminalNode;
    FOR(): TerminalNode;
    identifier(): IdentifierContext;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowRoutineLoadStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _name: IdentifierContext;
    SHOW(): TerminalNode;
    ROUTINE(): TerminalNode;
    LOAD(): TerminalNode;
    ALL(): TerminalNode | undefined;
    FOR(): TerminalNode | undefined;
    FROM(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    ORDER(): TerminalNode | undefined;
    BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    limitElement(): LimitElementContext | undefined;
    identifier(): IdentifierContext | undefined;
    qualifiedName(): QualifiedNameContext[];
    qualifiedName(i: number): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowRoutineLoadTaskStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    SHOW(): TerminalNode;
    ROUTINE(): TerminalNode;
    LOAD(): TerminalNode;
    TASK(): TerminalNode;
    WHERE(): TerminalNode;
    expression(): ExpressionContext;
    FROM(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowCreateRoutineLoadStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _name: IdentifierContext;
    SHOW(): TerminalNode;
    CREATE(): TerminalNode;
    ROUTINE(): TerminalNode;
    LOAD(): TerminalNode;
    identifier(): IdentifierContext;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowStreamLoadStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _name: IdentifierContext;
    SHOW(): TerminalNode;
    STREAM(): TerminalNode;
    LOAD(): TerminalNode;
    ALL(): TerminalNode | undefined;
    FOR(): TerminalNode | undefined;
    FROM(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    ORDER(): TerminalNode | undefined;
    BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    limitElement(): LimitElementContext | undefined;
    identifier(): IdentifierContext | undefined;
    qualifiedName(): QualifiedNameContext[];
    qualifiedName(i: number): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AnalyzeStatementContext extends ParserRuleContext {
    ANALYZE(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    WITH(): TerminalNode | undefined;
    MODE(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    FULL(): TerminalNode | undefined;
    SAMPLE(): TerminalNode | undefined;
    SYNC(): TerminalNode | undefined;
    ASYNC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropStatsStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    STATS(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AnalyzeHistogramStatementContext extends ParserRuleContext {
    _bucket: Token;
    ANALYZE(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    UPDATE(): TerminalNode;
    HISTOGRAM(): TerminalNode;
    ON(): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    WITH(): TerminalNode[];
    WITH(i: number): TerminalNode;
    MODE(): TerminalNode | undefined;
    BUCKETS(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    SYNC(): TerminalNode | undefined;
    ASYNC(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropHistogramStatementContext extends ParserRuleContext {
    ANALYZE(): TerminalNode;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    DROP(): TerminalNode;
    HISTOGRAM(): TerminalNode;
    ON(): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateAnalyzeStatementContext extends ParserRuleContext {
    _db: IdentifierContext;
    CREATE(): TerminalNode;
    ANALYZE(): TerminalNode;
    ALL(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    FULL(): TerminalNode | undefined;
    SAMPLE(): TerminalNode | undefined;
    DATABASE(): TerminalNode | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    TABLE(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropAnalyzeJobStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    ANALYZE(): TerminalNode;
    INTEGER_VALUE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowAnalyzeStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    ANALYZE(): TerminalNode;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    JOB(): TerminalNode | undefined;
    STATUS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowStatsMetaStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    STATS(): TerminalNode;
    META(): TerminalNode;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowHistogramMetaStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    HISTOGRAM(): TerminalNode;
    META(): TerminalNode;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class KillAnalyzeStatementContext extends ParserRuleContext {
    KILL(): TerminalNode;
    ANALYZE(): TerminalNode;
    INTEGER_VALUE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AnalyzeProfileStatementContext extends ParserRuleContext {
    ANALYZE(): TerminalNode;
    PROFILE(): TerminalNode;
    FROM(): TerminalNode;
    string(): StringContext;
    INTEGER_VALUE(): TerminalNode[];
    INTEGER_VALUE(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateResourceGroupStatementContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    RESOURCE(): TerminalNode;
    GROUP(): TerminalNode;
    identifier(): IdentifierContext;
    WITH(): TerminalNode;
    property(): PropertyContext[];
    property(i: number): PropertyContext;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    OR(): TerminalNode | undefined;
    REPLACE(): TerminalNode | undefined;
    TO(): TerminalNode | undefined;
    classifier(): ClassifierContext[];
    classifier(i: number): ClassifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropResourceGroupStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    RESOURCE(): TerminalNode;
    GROUP(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterResourceGroupStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    RESOURCE(): TerminalNode;
    GROUP(): TerminalNode;
    identifier(): IdentifierContext;
    ADD(): TerminalNode | undefined;
    classifier(): ClassifierContext[];
    classifier(i: number): ClassifierContext;
    DROP(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode[];
    INTEGER_VALUE(i: number): TerminalNode;
    ALL(): TerminalNode | undefined;
    WITH(): TerminalNode | undefined;
    property(): PropertyContext[];
    property(i: number): PropertyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowResourceGroupStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    RESOURCE(): TerminalNode;
    GROUP(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    GROUPS(): TerminalNode | undefined;
    ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowResourceGroupUsageStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    USAGE(): TerminalNode;
    RESOURCE(): TerminalNode;
    GROUP(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    GROUPS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateResourceStatementContext extends ParserRuleContext {
    _resourceName: IdentifierOrStringContext;
    CREATE(): TerminalNode;
    RESOURCE(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    EXTERNAL(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterResourceStatementContext extends ParserRuleContext {
    _resourceName: IdentifierOrStringContext;
    ALTER(): TerminalNode;
    RESOURCE(): TerminalNode;
    SET(): TerminalNode;
    properties(): PropertiesContext;
    identifierOrString(): IdentifierOrStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropResourceStatementContext extends ParserRuleContext {
    _resourceName: IdentifierOrStringContext;
    DROP(): TerminalNode;
    RESOURCE(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowResourceStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    RESOURCES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ClassifierContext extends ParserRuleContext {
    expressionList(): ExpressionListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowFunctionsStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _pattern: StringContext;
    SHOW(): TerminalNode;
    FUNCTIONS(): TerminalNode;
    FULL(): TerminalNode | undefined;
    BUILTIN(): TerminalNode | undefined;
    GLOBAL(): TerminalNode | undefined;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropFunctionStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    FUNCTION(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    typeList(): TypeListContext;
    GLOBAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateFunctionStatementContext extends ParserRuleContext {
    _functionType: Token;
    _returnType: TypeContext;
    _intermediateType: TypeContext;
    CREATE(): TerminalNode;
    FUNCTION(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    typeList(): TypeListContext;
    RETURNS(): TerminalNode;
    type(): TypeContext[];
    type(i: number): TypeContext;
    GLOBAL(): TerminalNode | undefined;
    INTERMEDIATE(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    TABLE(): TerminalNode | undefined;
    AGGREGATE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class TypeListContext extends ParserRuleContext {
    type(): TypeContext[];
    type(i: number): TypeContext;
    DOTDOTDOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class LoadStatementContext extends ParserRuleContext {
    _label: LabelNameContext;
    _data: DataDescListContext;
    _broker: BrokerDescContext;
    _system: IdentifierOrStringContext;
    _props: PropertyListContext;
    _resource: ResourceDescContext;
    LOAD(): TerminalNode;
    LABEL(): TerminalNode;
    labelName(): LabelNameContext;
    setVarHint(): SetVarHintContext[];
    setVarHint(i: number): SetVarHintContext;
    BY(): TerminalNode | undefined;
    PROPERTIES(): TerminalNode | undefined;
    dataDescList(): DataDescListContext | undefined;
    brokerDesc(): BrokerDescContext | undefined;
    identifierOrString(): IdentifierOrStringContext | undefined;
    propertyList(): PropertyListContext | undefined;
    resourceDesc(): ResourceDescContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class LabelNameContext extends ParserRuleContext {
    _db: IdentifierContext;
    _label: IdentifierContext;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DataDescListContext extends ParserRuleContext {
    dataDesc(): DataDescContext[];
    dataDesc(i: number): DataDescContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DataDescContext extends ParserRuleContext {
    _srcFiles: StringListContext;
    _dstTableName: IdentifierContext;
    _partitions: PartitionNamesContext;
    _colSep: StringContext;
    _rowSep: StringContext;
    _format: FileFormatContext;
    _formatPropsField: FormatPropsContext;
    _colList: ColumnAliasesContext;
    _colFromPath: IdentifierListContext;
    _colMappingList: ClassifierContext;
    _where: ExpressionContext;
    _srcTableName: IdentifierContext;
    DATA(): TerminalNode;
    INFILE(): TerminalNode | undefined;
    INTO(): TerminalNode;
    TABLE(): TerminalNode[];
    TABLE(i: number): TerminalNode;
    stringList(): StringListContext | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    NEGATIVE(): TerminalNode | undefined;
    COLUMNS(): TerminalNode[];
    COLUMNS(i: number): TerminalNode;
    TERMINATED(): TerminalNode[];
    TERMINATED(i: number): TerminalNode;
    BY(): TerminalNode[];
    BY(i: number): TerminalNode;
    ROWS(): TerminalNode | undefined;
    FROM(): TerminalNode | undefined;
    PATH(): TerminalNode | undefined;
    AS(): TerminalNode | undefined;
    SET(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    partitionNames(): PartitionNamesContext | undefined;
    string(): StringContext[];
    string(i: number): StringContext;
    fileFormat(): FileFormatContext | undefined;
    formatProps(): FormatPropsContext | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    identifierList(): IdentifierListContext | undefined;
    classifier(): ClassifierContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class FormatPropsContext extends ParserRuleContext {
    _encloseCharacter: StringContext;
    _escapeCharacter: StringContext;
    SKIP_HEADER(): TerminalNode | undefined;
    EQ(): TerminalNode[];
    EQ(i: number): TerminalNode;
    INTEGER_VALUE(): TerminalNode | undefined;
    TRIM_SPACE(): TerminalNode | undefined;
    booleanValue(): BooleanValueContext | undefined;
    ENCLOSE(): TerminalNode | undefined;
    ESCAPE(): TerminalNode | undefined;
    string(): StringContext[];
    string(i: number): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class BrokerDescContext extends ParserRuleContext {
    _props: PropertyListContext;
    _name: IdentifierOrStringContext;
    WITH(): TerminalNode;
    BROKER(): TerminalNode;
    propertyList(): PropertyListContext | undefined;
    identifierOrString(): IdentifierOrStringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ResourceDescContext extends ParserRuleContext {
    _name: IdentifierOrStringContext;
    _props: PropertyListContext;
    WITH(): TerminalNode;
    RESOURCE(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    propertyList(): PropertyListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowLoadStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    LOAD(): TerminalNode;
    ALL(): TerminalNode | undefined;
    FROM(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    ORDER(): TerminalNode | undefined;
    BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    limitElement(): LimitElementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowLoadWarningsStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    LOAD(): TerminalNode;
    WARNINGS(): TerminalNode;
    FROM(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    limitElement(): LimitElementContext | undefined;
    ON(): TerminalNode | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CancelLoadStatementContext extends ParserRuleContext {
    CANCEL(): TerminalNode;
    LOAD(): TerminalNode;
    FROM(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterLoadStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    _name: IdentifierContext;
    ALTER(): TerminalNode;
    LOAD(): TerminalNode;
    FOR(): TerminalNode;
    identifier(): IdentifierContext;
    jobProperties(): JobPropertiesContext | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CancelCompactionStatementContext extends ParserRuleContext {
    CANCEL(): TerminalNode;
    COMPACTION(): TerminalNode;
    WHERE(): TerminalNode;
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowAuthorStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    AUTHORS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowBackendsStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    BACKENDS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowBrokerStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    BROKER(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowCharsetStatementContext extends ParserRuleContext {
    _pattern: StringContext;
    SHOW(): TerminalNode;
    CHAR(): TerminalNode | undefined;
    SET(): TerminalNode | undefined;
    CHARSET(): TerminalNode | undefined;
    CHARACTER(): TerminalNode | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowCollationStatementContext extends ParserRuleContext {
    _pattern: StringContext;
    SHOW(): TerminalNode;
    COLLATION(): TerminalNode;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowDeleteStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    SHOW(): TerminalNode;
    DELETE(): TerminalNode;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowDynamicPartitionStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    SHOW(): TerminalNode;
    DYNAMIC(): TerminalNode;
    PARTITION(): TerminalNode;
    TABLES(): TerminalNode;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowEventsStatementContext extends ParserRuleContext {
    _catalog: QualifiedNameContext;
    _pattern: StringContext;
    SHOW(): TerminalNode;
    EVENTS(): TerminalNode;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowEnginesStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    ENGINES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowFrontendsStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    FRONTENDS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowPluginsStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    PLUGINS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowRepositoriesStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    REPOSITORIES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowOpenTableStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    OPEN(): TerminalNode;
    TABLES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowPrivilegesStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    PRIVILEGES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowProcedureStatementContext extends ParserRuleContext {
    _pattern: StringContext;
    _where: ExpressionContext;
    SHOW(): TerminalNode;
    STATUS(): TerminalNode;
    PROCEDURE(): TerminalNode | undefined;
    FUNCTION(): TerminalNode | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    string(): StringContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowProcStatementContext extends ParserRuleContext {
    _path: StringContext;
    SHOW(): TerminalNode;
    PROC(): TerminalNode;
    string(): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowProcesslistStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    PROCESSLIST(): TerminalNode;
    FULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowProfilelistStatementContext extends ParserRuleContext {
    _limit: Token;
    SHOW(): TerminalNode;
    PROFILELIST(): TerminalNode;
    LIMIT(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowRunningQueriesStatementContext extends ParserRuleContext {
    _limit: Token;
    SHOW(): TerminalNode;
    RUNNING(): TerminalNode;
    QUERIES(): TerminalNode;
    LIMIT(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowStatusStatementContext extends ParserRuleContext {
    _pattern: StringContext;
    SHOW(): TerminalNode;
    STATUS(): TerminalNode;
    varType(): VarTypeContext | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowTabletStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    TABLET(): TerminalNode;
    INTEGER_VALUE(): TerminalNode | undefined;
    FROM(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    partitionNames(): PartitionNamesContext | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    ORDER(): TerminalNode | undefined;
    BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    limitElement(): LimitElementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowTransactionStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    SHOW(): TerminalNode;
    TRANSACTION(): TerminalNode;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowTriggersStatementContext extends ParserRuleContext {
    _catalog: QualifiedNameContext;
    _pattern: StringContext;
    SHOW(): TerminalNode;
    TRIGGERS(): TerminalNode;
    FULL(): TerminalNode | undefined;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowUserPropertyStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    PROPERTY(): TerminalNode;
    FOR(): TerminalNode | undefined;
    string(): StringContext[];
    string(i: number): StringContext;
    LIKE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowVariablesStatementContext extends ParserRuleContext {
    _pattern: StringContext;
    SHOW(): TerminalNode;
    VARIABLES(): TerminalNode;
    varType(): VarTypeContext | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowWarningStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    WARNINGS(): TerminalNode | undefined;
    ERRORS(): TerminalNode | undefined;
    limitElement(): LimitElementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class HelpStatementContext extends ParserRuleContext {
    HELP(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateUserStatementContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    USER(): TerminalNode;
    user(): UserContext;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    authOption(): AuthOptionContext | undefined;
    DEFAULT(): TerminalNode | undefined;
    ROLE(): TerminalNode | undefined;
    roleList(): RoleListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropUserStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    USER(): TerminalNode;
    user(): UserContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterUserStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    USER(): TerminalNode;
    user(): UserContext;
    authOption(): AuthOptionContext | undefined;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    DEFAULT(): TerminalNode | undefined;
    ROLE(): TerminalNode | undefined;
    NONE(): TerminalNode | undefined;
    ALL(): TerminalNode | undefined;
    roleList(): RoleListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowUserStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    USER(): TerminalNode | undefined;
    USERS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowAuthenticationStatementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ShowAuthenticationStatementContext): void;
}
export declare class ShowAllAuthenticationContext extends ShowAuthenticationStatementContext {
    SHOW(): TerminalNode;
    ALL(): TerminalNode;
    AUTHENTICATION(): TerminalNode;
    constructor(ctx: ShowAuthenticationStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowAuthenticationForUserContext extends ShowAuthenticationStatementContext {
    SHOW(): TerminalNode;
    AUTHENTICATION(): TerminalNode;
    FOR(): TerminalNode | undefined;
    user(): UserContext | undefined;
    constructor(ctx: ShowAuthenticationStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ExecuteAsStatementContext extends ParserRuleContext {
    EXECUTE(): TerminalNode;
    AS(): TerminalNode;
    user(): UserContext;
    WITH(): TerminalNode | undefined;
    NO(): TerminalNode | undefined;
    REVERT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateRoleStatementContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    ROLE(): TerminalNode;
    roleList(): RoleListContext;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    comment(): CommentContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterRoleStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    ROLE(): TerminalNode;
    roleList(): RoleListContext;
    SET(): TerminalNode;
    COMMENT(): TerminalNode;
    EQ(): TerminalNode;
    string(): StringContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropRoleStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    ROLE(): TerminalNode;
    roleList(): RoleListContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowRolesStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    ROLES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GrantRoleStatementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: GrantRoleStatementContext): void;
}
export declare class GrantRoleToUserContext extends GrantRoleStatementContext {
    GRANT(): TerminalNode;
    identifierOrStringList(): IdentifierOrStringListContext;
    TO(): TerminalNode;
    user(): UserContext;
    USER(): TerminalNode | undefined;
    constructor(ctx: GrantRoleStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GrantRoleToRoleContext extends GrantRoleStatementContext {
    GRANT(): TerminalNode;
    identifierOrStringList(): IdentifierOrStringListContext;
    TO(): TerminalNode;
    ROLE(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    constructor(ctx: GrantRoleStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RevokeRoleStatementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: RevokeRoleStatementContext): void;
}
export declare class RevokeRoleFromUserContext extends RevokeRoleStatementContext {
    REVOKE(): TerminalNode;
    identifierOrStringList(): IdentifierOrStringListContext;
    FROM(): TerminalNode;
    user(): UserContext;
    USER(): TerminalNode | undefined;
    constructor(ctx: RevokeRoleStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RevokeRoleFromRoleContext extends RevokeRoleStatementContext {
    REVOKE(): TerminalNode;
    identifierOrStringList(): IdentifierOrStringListContext;
    FROM(): TerminalNode;
    ROLE(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    constructor(ctx: RevokeRoleStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetRoleStatementContext extends ParserRuleContext {
    SET(): TerminalNode;
    ROLE(): TerminalNode;
    DEFAULT(): TerminalNode | undefined;
    NONE(): TerminalNode | undefined;
    ALL(): TerminalNode | undefined;
    EXCEPT(): TerminalNode | undefined;
    roleList(): RoleListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetDefaultRoleStatementContext extends ParserRuleContext {
    SET(): TerminalNode;
    DEFAULT(): TerminalNode;
    ROLE(): TerminalNode;
    TO(): TerminalNode;
    user(): UserContext;
    NONE(): TerminalNode | undefined;
    ALL(): TerminalNode | undefined;
    roleList(): RoleListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GrantRevokeClauseContext extends ParserRuleContext {
    user(): UserContext | undefined;
    ROLE(): TerminalNode | undefined;
    identifierOrString(): IdentifierOrStringContext | undefined;
    USER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GrantPrivilegeStatementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: GrantPrivilegeStatementContext): void;
}
export declare class GrantOnUserContext extends GrantPrivilegeStatementContext {
    GRANT(): TerminalNode[];
    GRANT(i: number): TerminalNode;
    IMPERSONATE(): TerminalNode;
    ON(): TerminalNode;
    USER(): TerminalNode;
    user(): UserContext[];
    user(i: number): UserContext;
    TO(): TerminalNode;
    grantRevokeClause(): GrantRevokeClauseContext;
    WITH(): TerminalNode | undefined;
    OPTION(): TerminalNode | undefined;
    constructor(ctx: GrantPrivilegeStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GrantOnTableBriefContext extends GrantPrivilegeStatementContext {
    GRANT(): TerminalNode[];
    GRANT(i: number): TerminalNode;
    privilegeTypeList(): PrivilegeTypeListContext;
    ON(): TerminalNode;
    privObjectNameList(): PrivObjectNameListContext;
    TO(): TerminalNode;
    grantRevokeClause(): GrantRevokeClauseContext;
    WITH(): TerminalNode | undefined;
    OPTION(): TerminalNode | undefined;
    constructor(ctx: GrantPrivilegeStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GrantOnFuncContext extends GrantPrivilegeStatementContext {
    GRANT(): TerminalNode[];
    GRANT(i: number): TerminalNode;
    privilegeTypeList(): PrivilegeTypeListContext;
    ON(): TerminalNode;
    FUNCTION(): TerminalNode;
    privFunctionObjectNameList(): PrivFunctionObjectNameListContext;
    TO(): TerminalNode;
    grantRevokeClause(): GrantRevokeClauseContext;
    GLOBAL(): TerminalNode | undefined;
    WITH(): TerminalNode | undefined;
    OPTION(): TerminalNode | undefined;
    constructor(ctx: GrantPrivilegeStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GrantOnSystemContext extends GrantPrivilegeStatementContext {
    GRANT(): TerminalNode[];
    GRANT(i: number): TerminalNode;
    privilegeTypeList(): PrivilegeTypeListContext;
    ON(): TerminalNode;
    SYSTEM(): TerminalNode;
    TO(): TerminalNode;
    grantRevokeClause(): GrantRevokeClauseContext;
    WITH(): TerminalNode | undefined;
    OPTION(): TerminalNode | undefined;
    constructor(ctx: GrantPrivilegeStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GrantOnPrimaryObjContext extends GrantPrivilegeStatementContext {
    GRANT(): TerminalNode[];
    GRANT(i: number): TerminalNode;
    privilegeTypeList(): PrivilegeTypeListContext;
    ON(): TerminalNode;
    privObjectType(): PrivObjectTypeContext;
    privObjectNameList(): PrivObjectNameListContext;
    TO(): TerminalNode;
    grantRevokeClause(): GrantRevokeClauseContext;
    WITH(): TerminalNode | undefined;
    OPTION(): TerminalNode | undefined;
    constructor(ctx: GrantPrivilegeStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GrantOnAllContext extends GrantPrivilegeStatementContext {
    _isAll: Token;
    GRANT(): TerminalNode[];
    GRANT(i: number): TerminalNode;
    privilegeTypeList(): PrivilegeTypeListContext;
    ON(): TerminalNode;
    ALL(): TerminalNode[];
    ALL(i: number): TerminalNode;
    privObjectTypePlural(): PrivObjectTypePluralContext;
    TO(): TerminalNode;
    grantRevokeClause(): GrantRevokeClauseContext;
    IN(): TerminalNode | undefined;
    DATABASES(): TerminalNode | undefined;
    DATABASE(): TerminalNode | undefined;
    identifierOrString(): IdentifierOrStringContext | undefined;
    WITH(): TerminalNode | undefined;
    OPTION(): TerminalNode | undefined;
    constructor(ctx: GrantPrivilegeStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RevokePrivilegeStatementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: RevokePrivilegeStatementContext): void;
}
export declare class RevokeOnUserContext extends RevokePrivilegeStatementContext {
    REVOKE(): TerminalNode;
    IMPERSONATE(): TerminalNode;
    ON(): TerminalNode;
    USER(): TerminalNode;
    user(): UserContext[];
    user(i: number): UserContext;
    FROM(): TerminalNode;
    grantRevokeClause(): GrantRevokeClauseContext;
    constructor(ctx: RevokePrivilegeStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RevokeOnTableBriefContext extends RevokePrivilegeStatementContext {
    REVOKE(): TerminalNode;
    privilegeTypeList(): PrivilegeTypeListContext;
    ON(): TerminalNode;
    privObjectNameList(): PrivObjectNameListContext;
    FROM(): TerminalNode;
    grantRevokeClause(): GrantRevokeClauseContext;
    constructor(ctx: RevokePrivilegeStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RevokeOnFuncContext extends RevokePrivilegeStatementContext {
    REVOKE(): TerminalNode;
    privilegeTypeList(): PrivilegeTypeListContext;
    ON(): TerminalNode;
    FUNCTION(): TerminalNode;
    privFunctionObjectNameList(): PrivFunctionObjectNameListContext;
    FROM(): TerminalNode;
    grantRevokeClause(): GrantRevokeClauseContext;
    GLOBAL(): TerminalNode | undefined;
    constructor(ctx: RevokePrivilegeStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RevokeOnSystemContext extends RevokePrivilegeStatementContext {
    REVOKE(): TerminalNode;
    privilegeTypeList(): PrivilegeTypeListContext;
    ON(): TerminalNode;
    SYSTEM(): TerminalNode;
    FROM(): TerminalNode;
    grantRevokeClause(): GrantRevokeClauseContext;
    constructor(ctx: RevokePrivilegeStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RevokeOnPrimaryObjContext extends RevokePrivilegeStatementContext {
    REVOKE(): TerminalNode;
    privilegeTypeList(): PrivilegeTypeListContext;
    ON(): TerminalNode;
    privObjectType(): PrivObjectTypeContext;
    privObjectNameList(): PrivObjectNameListContext;
    FROM(): TerminalNode;
    grantRevokeClause(): GrantRevokeClauseContext;
    constructor(ctx: RevokePrivilegeStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RevokeOnAllContext extends RevokePrivilegeStatementContext {
    _isAll: Token;
    REVOKE(): TerminalNode;
    privilegeTypeList(): PrivilegeTypeListContext;
    ON(): TerminalNode;
    ALL(): TerminalNode[];
    ALL(i: number): TerminalNode;
    privObjectTypePlural(): PrivObjectTypePluralContext;
    FROM(): TerminalNode;
    grantRevokeClause(): GrantRevokeClauseContext;
    IN(): TerminalNode | undefined;
    DATABASES(): TerminalNode | undefined;
    DATABASE(): TerminalNode | undefined;
    identifierOrString(): IdentifierOrStringContext | undefined;
    constructor(ctx: RevokePrivilegeStatementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowGrantsStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    GRANTS(): TerminalNode;
    FOR(): TerminalNode | undefined;
    user(): UserContext | undefined;
    USER(): TerminalNode | undefined;
    ROLE(): TerminalNode | undefined;
    identifierOrString(): IdentifierOrStringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateSecurityIntegrationStatementContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    SECURITY(): TerminalNode;
    INTEGRATION(): TerminalNode;
    identifier(): IdentifierContext;
    properties(): PropertiesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterSecurityIntegrationStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    SECURITY(): TerminalNode;
    INTEGRATION(): TerminalNode;
    identifier(): IdentifierContext;
    SET(): TerminalNode;
    propertyList(): PropertyListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropSecurityIntegrationStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    SECURITY(): TerminalNode;
    INTEGRATION(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowSecurityIntegrationStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    SECURITY(): TerminalNode;
    INTEGRATIONS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowCreateSecurityIntegrationStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    CREATE(): TerminalNode;
    SECURITY(): TerminalNode;
    INTEGRATION(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateRoleMappingStatementContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    ROLE(): TerminalNode;
    MAPPING(): TerminalNode;
    identifier(): IdentifierContext;
    properties(): PropertiesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterRoleMappingStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    ROLE(): TerminalNode;
    MAPPING(): TerminalNode;
    identifier(): IdentifierContext;
    SET(): TerminalNode;
    propertyList(): PropertyListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropRoleMappingStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    ROLE(): TerminalNode;
    MAPPING(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowRoleMappingStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    ROLE(): TerminalNode;
    MAPPINGS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RefreshRoleMappingStatementContext extends ParserRuleContext {
    REFRESH(): TerminalNode;
    ALL(): TerminalNode;
    ROLE(): TerminalNode;
    MAPPINGS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AuthOptionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: AuthOptionContext): void;
}
export declare class AuthWithoutPluginContext extends AuthOptionContext {
    IDENTIFIED(): TerminalNode;
    BY(): TerminalNode;
    string(): StringContext;
    PASSWORD(): TerminalNode | undefined;
    constructor(ctx: AuthOptionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AuthWithPluginContext extends AuthOptionContext {
    IDENTIFIED(): TerminalNode;
    WITH(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    string(): StringContext | undefined;
    BY(): TerminalNode | undefined;
    AS(): TerminalNode | undefined;
    constructor(ctx: AuthOptionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PrivObjectNameContext extends ParserRuleContext {
    identifierOrStringOrStar(): IdentifierOrStringOrStarContext[];
    identifierOrStringOrStar(i: number): IdentifierOrStringOrStarContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PrivObjectNameListContext extends ParserRuleContext {
    privObjectName(): PrivObjectNameContext[];
    privObjectName(i: number): PrivObjectNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PrivFunctionObjectNameListContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext[];
    qualifiedName(i: number): QualifiedNameContext;
    typeList(): TypeListContext[];
    typeList(i: number): TypeListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PrivilegeTypeListContext extends ParserRuleContext {
    privilegeType(): PrivilegeTypeContext[];
    privilegeType(i: number): PrivilegeTypeContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PrivilegeTypeContext extends ParserRuleContext {
    ALL(): TerminalNode | undefined;
    PRIVILEGES(): TerminalNode | undefined;
    ALTER(): TerminalNode | undefined;
    APPLY(): TerminalNode | undefined;
    BLACKLIST(): TerminalNode | undefined;
    CREATE(): TerminalNode | undefined;
    DATABASE(): TerminalNode | undefined;
    TABLE(): TerminalNode | undefined;
    VIEW(): TerminalNode | undefined;
    FUNCTION(): TerminalNode | undefined;
    GLOBAL(): TerminalNode | undefined;
    MATERIALIZED(): TerminalNode | undefined;
    RESOURCE(): TerminalNode | undefined;
    GROUP(): TerminalNode | undefined;
    EXTERNAL(): TerminalNode | undefined;
    CATALOG(): TerminalNode | undefined;
    POLICY(): TerminalNode | undefined;
    STORAGE(): TerminalNode | undefined;
    VOLUME(): TerminalNode | undefined;
    PIPE(): TerminalNode | undefined;
    DELETE(): TerminalNode | undefined;
    DROP(): TerminalNode | undefined;
    EXPORT(): TerminalNode | undefined;
    FILE(): TerminalNode | undefined;
    IMPERSONATE(): TerminalNode | undefined;
    INSERT(): TerminalNode | undefined;
    GRANT(): TerminalNode | undefined;
    NODE(): TerminalNode | undefined;
    OPERATE(): TerminalNode | undefined;
    PLUGIN(): TerminalNode | undefined;
    REPOSITORY(): TerminalNode | undefined;
    REFRESH(): TerminalNode | undefined;
    SELECT(): TerminalNode | undefined;
    UPDATE(): TerminalNode | undefined;
    USAGE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PrivObjectTypeContext extends ParserRuleContext {
    CATALOG(): TerminalNode | undefined;
    DATABASE(): TerminalNode | undefined;
    MATERIALIZED(): TerminalNode | undefined;
    VIEW(): TerminalNode | undefined;
    POLICY(): TerminalNode | undefined;
    RESOURCE(): TerminalNode | undefined;
    GROUP(): TerminalNode | undefined;
    STORAGE(): TerminalNode | undefined;
    VOLUME(): TerminalNode | undefined;
    SYSTEM(): TerminalNode | undefined;
    TABLE(): TerminalNode | undefined;
    PIPE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PrivObjectTypePluralContext extends ParserRuleContext {
    CATALOGS(): TerminalNode | undefined;
    DATABASES(): TerminalNode | undefined;
    FUNCTIONS(): TerminalNode | undefined;
    GLOBAL(): TerminalNode | undefined;
    MATERIALIZED(): TerminalNode | undefined;
    VIEWS(): TerminalNode | undefined;
    POLICIES(): TerminalNode | undefined;
    RESOURCES(): TerminalNode | undefined;
    RESOURCE(): TerminalNode | undefined;
    GROUPS(): TerminalNode | undefined;
    STORAGE(): TerminalNode | undefined;
    VOLUMES(): TerminalNode | undefined;
    TABLES(): TerminalNode | undefined;
    USERS(): TerminalNode | undefined;
    PIPES(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateMaskingPolicyStatementContext extends ParserRuleContext {
    _policyName: QualifiedNameContext;
    CREATE(): TerminalNode;
    MASKING(): TerminalNode;
    POLICY(): TerminalNode;
    AS(): TerminalNode;
    policySignature(): PolicySignatureContext[];
    policySignature(i: number): PolicySignatureContext;
    RETURNS(): TerminalNode;
    type(): TypeContext;
    ARROW(): TerminalNode;
    expression(): ExpressionContext;
    qualifiedName(): QualifiedNameContext;
    OR(): TerminalNode | undefined;
    REPLACE(): TerminalNode | undefined;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    comment(): CommentContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropMaskingPolicyStatementContext extends ParserRuleContext {
    _policyName: QualifiedNameContext;
    DROP(): TerminalNode;
    MASKING(): TerminalNode;
    POLICY(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    FORCE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterMaskingPolicyStatementContext extends ParserRuleContext {
    _policyName: QualifiedNameContext;
    _newPolicyName: IdentifierContext;
    ALTER(): TerminalNode;
    MASKING(): TerminalNode;
    POLICY(): TerminalNode;
    SET(): TerminalNode | undefined;
    BODY(): TerminalNode | undefined;
    ARROW(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    qualifiedName(): QualifiedNameContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    COMMENT(): TerminalNode | undefined;
    EQ(): TerminalNode | undefined;
    string(): StringContext | undefined;
    RENAME(): TerminalNode | undefined;
    TO(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowMaskingPolicyStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    SHOW(): TerminalNode;
    MASKING(): TerminalNode;
    POLICIES(): TerminalNode;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowCreateMaskingPolicyStatementContext extends ParserRuleContext {
    _policyName: QualifiedNameContext;
    SHOW(): TerminalNode;
    CREATE(): TerminalNode;
    MASKING(): TerminalNode;
    POLICY(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateRowAccessPolicyStatementContext extends ParserRuleContext {
    _policyName: QualifiedNameContext;
    CREATE(): TerminalNode;
    ROW(): TerminalNode;
    ACCESS(): TerminalNode;
    POLICY(): TerminalNode;
    AS(): TerminalNode;
    policySignature(): PolicySignatureContext[];
    policySignature(i: number): PolicySignatureContext;
    RETURNS(): TerminalNode;
    BOOLEAN(): TerminalNode;
    ARROW(): TerminalNode;
    expression(): ExpressionContext;
    qualifiedName(): QualifiedNameContext;
    OR(): TerminalNode | undefined;
    REPLACE(): TerminalNode | undefined;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    comment(): CommentContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropRowAccessPolicyStatementContext extends ParserRuleContext {
    _policyName: QualifiedNameContext;
    DROP(): TerminalNode;
    ROW(): TerminalNode;
    ACCESS(): TerminalNode;
    POLICY(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    FORCE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterRowAccessPolicyStatementContext extends ParserRuleContext {
    _policyName: QualifiedNameContext;
    _newPolicyName: IdentifierContext;
    ALTER(): TerminalNode;
    ROW(): TerminalNode;
    ACCESS(): TerminalNode;
    POLICY(): TerminalNode;
    SET(): TerminalNode | undefined;
    BODY(): TerminalNode | undefined;
    ARROW(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    qualifiedName(): QualifiedNameContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    COMMENT(): TerminalNode | undefined;
    EQ(): TerminalNode | undefined;
    string(): StringContext | undefined;
    RENAME(): TerminalNode | undefined;
    TO(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowRowAccessPolicyStatementContext extends ParserRuleContext {
    _db: QualifiedNameContext;
    SHOW(): TerminalNode;
    ROW(): TerminalNode;
    ACCESS(): TerminalNode;
    POLICIES(): TerminalNode;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowCreateRowAccessPolicyStatementContext extends ParserRuleContext {
    _policyName: QualifiedNameContext;
    SHOW(): TerminalNode;
    CREATE(): TerminalNode;
    ROW(): TerminalNode;
    ACCESS(): TerminalNode;
    POLICY(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PolicySignatureContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    type(): TypeContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class BackupStatementContext extends ParserRuleContext {
    BACKUP(): TerminalNode;
    SNAPSHOT(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    TO(): TerminalNode;
    identifier(): IdentifierContext;
    ON(): TerminalNode | undefined;
    tableDesc(): TableDescContext[];
    tableDesc(i: number): TableDescContext;
    PROPERTIES(): TerminalNode | undefined;
    propertyList(): PropertyListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CancelBackupStatementContext extends ParserRuleContext {
    CANCEL(): TerminalNode;
    BACKUP(): TerminalNode;
    identifier(): IdentifierContext | undefined;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowBackupStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    BACKUP(): TerminalNode;
    identifier(): IdentifierContext | undefined;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RestoreStatementContext extends ParserRuleContext {
    RESTORE(): TerminalNode;
    SNAPSHOT(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    FROM(): TerminalNode;
    identifier(): IdentifierContext;
    ON(): TerminalNode | undefined;
    restoreTableDesc(): RestoreTableDescContext[];
    restoreTableDesc(i: number): RestoreTableDescContext;
    PROPERTIES(): TerminalNode | undefined;
    propertyList(): PropertyListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CancelRestoreStatementContext extends ParserRuleContext {
    CANCEL(): TerminalNode;
    RESTORE(): TerminalNode;
    identifier(): IdentifierContext | undefined;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowRestoreStatementContext extends ParserRuleContext {
    _where: ExpressionContext;
    SHOW(): TerminalNode;
    RESTORE(): TerminalNode;
    identifier(): IdentifierContext | undefined;
    WHERE(): TerminalNode | undefined;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowSnapshotStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    SNAPSHOT(): TerminalNode;
    ON(): TerminalNode;
    identifier(): IdentifierContext;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateRepositoryStatementContext extends ParserRuleContext {
    _repoName: IdentifierContext;
    _brokerName: IdentifierOrStringContext;
    _location: StringContext;
    CREATE(): TerminalNode;
    REPOSITORY(): TerminalNode;
    WITH(): TerminalNode;
    BROKER(): TerminalNode;
    ON(): TerminalNode;
    LOCATION(): TerminalNode;
    identifier(): IdentifierContext;
    string(): StringContext;
    READ(): TerminalNode | undefined;
    ONLY(): TerminalNode | undefined;
    PROPERTIES(): TerminalNode | undefined;
    propertyList(): PropertyListContext | undefined;
    identifierOrString(): IdentifierOrStringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropRepositoryStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    REPOSITORY(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AddSqlBlackListStatementContext extends ParserRuleContext {
    ADD(): TerminalNode;
    SQLBLACKLIST(): TerminalNode;
    string(): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DelSqlBlackListStatementContext extends ParserRuleContext {
    DELETE(): TerminalNode;
    SQLBLACKLIST(): TerminalNode;
    INTEGER_VALUE(): TerminalNode[];
    INTEGER_VALUE(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowSqlBlackListStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    SQLBLACKLIST(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowWhiteListStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    WHITELIST(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DataCacheTargetContext extends ParserRuleContext {
    identifierOrStringOrStar(): IdentifierOrStringOrStarContext[];
    identifierOrStringOrStar(i: number): IdentifierOrStringOrStarContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateDataCacheRuleStatementContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    DATACACHE(): TerminalNode;
    RULE(): TerminalNode;
    dataCacheTarget(): DataCacheTargetContext;
    PRIORITY(): TerminalNode;
    EQ(): TerminalNode;
    INTEGER_VALUE(): TerminalNode;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    MINUS_SYMBOL(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowDataCacheRulesStatementContext extends ParserRuleContext {
    SHOW(): TerminalNode;
    DATACACHE(): TerminalNode;
    RULES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropDataCacheRuleStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    DATACACHE(): TerminalNode;
    RULE(): TerminalNode;
    INTEGER_VALUE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ClearDataCacheRulesStatementContext extends ParserRuleContext {
    CLEAR(): TerminalNode;
    DATACACHE(): TerminalNode;
    RULES(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ExportStatementContext extends ParserRuleContext {
    EXPORT(): TerminalNode;
    TABLE(): TerminalNode;
    tableDesc(): TableDescContext;
    TO(): TerminalNode;
    string(): StringContext;
    columnAliases(): ColumnAliasesContext | undefined;
    WITH(): TerminalNode | undefined;
    MODE(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    brokerDesc(): BrokerDescContext | undefined;
    SYNC(): TerminalNode | undefined;
    ASYNC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CancelExportStatementContext extends ParserRuleContext {
    _catalog: QualifiedNameContext;
    _pattern: StringContext;
    CANCEL(): TerminalNode;
    EXPORT(): TerminalNode;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowExportStatementContext extends ParserRuleContext {
    _catalog: QualifiedNameContext;
    _pattern: StringContext;
    SHOW(): TerminalNode;
    EXPORT(): TerminalNode;
    ORDER(): TerminalNode | undefined;
    BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    limitElement(): LimitElementContext | undefined;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class InstallPluginStatementContext extends ParserRuleContext {
    INSTALL(): TerminalNode;
    PLUGIN(): TerminalNode;
    FROM(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UninstallPluginStatementContext extends ParserRuleContext {
    UNINSTALL(): TerminalNode;
    PLUGIN(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreateFileStatementContext extends ParserRuleContext {
    _catalog: QualifiedNameContext;
    CREATE(): TerminalNode;
    FILE(): TerminalNode;
    string(): StringContext;
    properties(): PropertiesContext;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropFileStatementContext extends ParserRuleContext {
    _catalog: QualifiedNameContext;
    DROP(): TerminalNode;
    FILE(): TerminalNode;
    string(): StringContext;
    properties(): PropertiesContext;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowSmallFilesStatementContext extends ParserRuleContext {
    _catalog: QualifiedNameContext;
    SHOW(): TerminalNode;
    FILE(): TerminalNode;
    FROM(): TerminalNode | undefined;
    IN(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CreatePipeStatementContext extends ParserRuleContext {
    CREATE(): TerminalNode;
    PIPE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    AS(): TerminalNode;
    insertStatement(): InsertStatementContext;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DropPipeStatementContext extends ParserRuleContext {
    DROP(): TerminalNode;
    PIPE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    IF(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterPipeClauseContext extends ParserRuleContext {
    _fileName: StringContext;
    SUSPEND(): TerminalNode | undefined;
    RESUME(): TerminalNode | undefined;
    RETRY(): TerminalNode | undefined;
    ALL(): TerminalNode | undefined;
    FILE(): TerminalNode | undefined;
    string(): StringContext | undefined;
    SET(): TerminalNode | undefined;
    propertyList(): PropertyListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AlterPipeStatementContext extends ParserRuleContext {
    ALTER(): TerminalNode;
    PIPE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    alterPipeClause(): AlterPipeClauseContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DescPipeStatementContext extends ParserRuleContext {
    PIPE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    DESC(): TerminalNode | undefined;
    DESCRIBE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ShowPipeStatementContext extends ParserRuleContext {
    _pattern: StringContext;
    SHOW(): TerminalNode;
    PIPES(): TerminalNode;
    ORDER(): TerminalNode | undefined;
    BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    limitElement(): LimitElementContext | undefined;
    LIKE(): TerminalNode | undefined;
    WHERE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    FROM(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetStatementContext extends ParserRuleContext {
    SET(): TerminalNode;
    setVar(): SetVarContext[];
    setVar(i: number): SetVarContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetVarContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: SetVarContext): void;
}
export declare class SetNamesContext extends SetVarContext {
    _charset: IdentifierOrStringContext;
    _collate: IdentifierOrStringContext;
    CHAR(): TerminalNode | undefined;
    SET(): TerminalNode | undefined;
    CHARSET(): TerminalNode | undefined;
    CHARACTER(): TerminalNode | undefined;
    identifierOrString(): IdentifierOrStringContext[];
    identifierOrString(i: number): IdentifierOrStringContext;
    DEFAULT(): TerminalNode[];
    DEFAULT(i: number): TerminalNode;
    NAMES(): TerminalNode | undefined;
    COLLATE(): TerminalNode | undefined;
    constructor(ctx: SetVarContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetPasswordContext extends SetVarContext {
    PASSWORD(): TerminalNode[];
    PASSWORD(i: number): TerminalNode;
    EQ(): TerminalNode;
    string(): StringContext | undefined;
    FOR(): TerminalNode | undefined;
    user(): UserContext | undefined;
    constructor(ctx: SetVarContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetUserVarContext extends SetVarContext {
    userVariable(): UserVariableContext;
    EQ(): TerminalNode;
    expression(): ExpressionContext;
    constructor(ctx: SetVarContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetSystemVarContext extends SetVarContext {
    identifier(): IdentifierContext | undefined;
    EQ(): TerminalNode;
    setExprOrDefault(): SetExprOrDefaultContext;
    varType(): VarTypeContext | undefined;
    systemVariable(): SystemVariableContext | undefined;
    constructor(ctx: SetVarContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetTransactionContext extends SetVarContext {
    TRANSACTION(): TerminalNode;
    transaction_characteristics(): Transaction_characteristicsContext;
    varType(): VarTypeContext | undefined;
    constructor(ctx: SetVarContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class Transaction_characteristicsContext extends ParserRuleContext {
    transaction_access_mode(): Transaction_access_modeContext | undefined;
    isolation_level(): Isolation_levelContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class Transaction_access_modeContext extends ParserRuleContext {
    READ(): TerminalNode;
    ONLY(): TerminalNode | undefined;
    WRITE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class Isolation_levelContext extends ParserRuleContext {
    ISOLATION(): TerminalNode;
    LEVEL(): TerminalNode;
    isolation_types(): Isolation_typesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class Isolation_typesContext extends ParserRuleContext {
    READ(): TerminalNode | undefined;
    UNCOMMITTED(): TerminalNode | undefined;
    COMMITTED(): TerminalNode | undefined;
    REPEATABLE(): TerminalNode | undefined;
    SERIALIZABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetExprOrDefaultContext extends ParserRuleContext {
    DEFAULT(): TerminalNode | undefined;
    ON(): TerminalNode | undefined;
    ALL(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetUserPropertyStatementContext extends ParserRuleContext {
    SET(): TerminalNode;
    PROPERTY(): TerminalNode;
    userPropertyList(): UserPropertyListContext;
    FOR(): TerminalNode | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RoleListContext extends ParserRuleContext {
    identifierOrString(): IdentifierOrStringContext[];
    identifierOrString(i: number): IdentifierOrStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ExecuteScriptStatementContext extends ParserRuleContext {
    ADMIN(): TerminalNode;
    EXECUTE(): TerminalNode;
    ON(): TerminalNode;
    string(): StringContext;
    FRONTEND(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UnsupportedStatementContext extends ParserRuleContext {
    START(): TerminalNode | undefined;
    TRANSACTION(): TerminalNode | undefined;
    WITH(): TerminalNode | undefined;
    CONSISTENT(): TerminalNode | undefined;
    SNAPSHOT(): TerminalNode | undefined;
    BEGIN(): TerminalNode | undefined;
    WORK(): TerminalNode | undefined;
    COMMIT(): TerminalNode | undefined;
    AND(): TerminalNode | undefined;
    CHAIN(): TerminalNode | undefined;
    RELEASE(): TerminalNode | undefined;
    NO(): TerminalNode[];
    NO(i: number): TerminalNode;
    ROLLBACK(): TerminalNode | undefined;
    LOCK(): TerminalNode | undefined;
    TABLES(): TerminalNode | undefined;
    lock_item(): Lock_itemContext[];
    lock_item(i: number): Lock_itemContext;
    UNLOCK(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class Lock_itemContext extends ParserRuleContext {
    _alias: IdentifierContext;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    lock_type(): Lock_typeContext;
    AS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class Lock_typeContext extends ParserRuleContext {
    READ(): TerminalNode | undefined;
    LOCAL(): TerminalNode | undefined;
    WRITE(): TerminalNode | undefined;
    LOW_PRIORITY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class QueryStatementContext extends ParserRuleContext {
    queryRelation(): QueryRelationContext;
    explainDesc(): ExplainDescContext | undefined;
    optimizerTrace(): OptimizerTraceContext | undefined;
    outfile(): OutfileContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class QueryRelationContext extends ParserRuleContext {
    queryNoWith(): QueryNoWithContext;
    withClause(): WithClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class WithClauseContext extends ParserRuleContext {
    WITH(): TerminalNode;
    commonTableExpression(): CommonTableExpressionContext[];
    commonTableExpression(i: number): CommonTableExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class QueryNoWithContext extends ParserRuleContext {
    queryPrimary(): QueryPrimaryContext;
    ORDER(): TerminalNode | undefined;
    BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    limitElement(): LimitElementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class TemporalClauseContext extends ParserRuleContext {
    AS(): TerminalNode | undefined;
    OF(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    FOR(): TerminalNode | undefined;
    SYSTEM_TIME(): TerminalNode | undefined;
    TIMESTAMP(): TerminalNode | undefined;
    string(): StringContext | undefined;
    BETWEEN(): TerminalNode | undefined;
    AND(): TerminalNode | undefined;
    FROM(): TerminalNode | undefined;
    TO(): TerminalNode | undefined;
    ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class QueryPrimaryContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: QueryPrimaryContext): void;
}
export declare class QueryPrimaryDefaultContext extends QueryPrimaryContext {
    querySpecification(): QuerySpecificationContext;
    constructor(ctx: QueryPrimaryContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class QueryWithParenthesesContext extends QueryPrimaryContext {
    subquery(): SubqueryContext;
    constructor(ctx: QueryPrimaryContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetOperationContext extends QueryPrimaryContext {
    _left: QueryPrimaryContext;
    _operator: Token;
    _right: QueryPrimaryContext;
    queryPrimary(): QueryPrimaryContext[];
    queryPrimary(i: number): QueryPrimaryContext;
    INTERSECT(): TerminalNode | undefined;
    setQuantifier(): SetQuantifierContext | undefined;
    UNION(): TerminalNode | undefined;
    EXCEPT(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    constructor(ctx: QueryPrimaryContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SubqueryContext extends ParserRuleContext {
    queryRelation(): QueryRelationContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RowConstructorContext extends ParserRuleContext {
    expressionList(): ExpressionListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SortItemContext extends ParserRuleContext {
    _ordering: Token;
    _nullOrdering: Token;
    expression(): ExpressionContext;
    NULLS(): TerminalNode | undefined;
    ASC(): TerminalNode | undefined;
    DESC(): TerminalNode | undefined;
    FIRST(): TerminalNode | undefined;
    LAST(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class LimitElementContext extends ParserRuleContext {
    _limit: Token;
    _offset: Token;
    LIMIT(): TerminalNode;
    INTEGER_VALUE(): TerminalNode[];
    INTEGER_VALUE(i: number): TerminalNode;
    PARAMETER(): TerminalNode[];
    PARAMETER(i: number): TerminalNode;
    OFFSET(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class QuerySpecificationContext extends ParserRuleContext {
    _where: ExpressionContext;
    _having: ExpressionContext;
    _qualifyFunction: SelectItemContext;
    _limit: Token;
    SELECT(): TerminalNode;
    selectItem(): SelectItemContext[];
    selectItem(i: number): SelectItemContext;
    fromClause(): FromClauseContext;
    setVarHint(): SetVarHintContext[];
    setVarHint(i: number): SetVarHintContext;
    setQuantifier(): SetQuantifierContext | undefined;
    WHERE(): TerminalNode | undefined;
    GROUP(): TerminalNode | undefined;
    BY(): TerminalNode | undefined;
    groupingElement(): GroupingElementContext | undefined;
    HAVING(): TerminalNode | undefined;
    QUALIFY(): TerminalNode | undefined;
    comparisonOperator(): ComparisonOperatorContext | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    INTEGER_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class FromClauseContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: FromClauseContext): void;
}
export declare class FromContext extends FromClauseContext {
    FROM(): TerminalNode | undefined;
    relations(): RelationsContext | undefined;
    constructor(ctx: FromClauseContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DualContext extends FromClauseContext {
    FROM(): TerminalNode;
    DUAL(): TerminalNode;
    constructor(ctx: FromClauseContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GroupingElementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: GroupingElementContext): void;
}
export declare class RollupContext extends GroupingElementContext {
    ROLLUP(): TerminalNode;
    expressionList(): ExpressionListContext | undefined;
    constructor(ctx: GroupingElementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CubeContext extends GroupingElementContext {
    CUBE(): TerminalNode;
    expressionList(): ExpressionListContext | undefined;
    constructor(ctx: GroupingElementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class MultipleGroupingSetsContext extends GroupingElementContext {
    GROUPING(): TerminalNode;
    SETS(): TerminalNode;
    groupingSet(): GroupingSetContext[];
    groupingSet(i: number): GroupingSetContext;
    constructor(ctx: GroupingElementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SingleGroupingSetContext extends GroupingElementContext {
    expressionList(): ExpressionListContext;
    constructor(ctx: GroupingElementContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GroupingSetContext extends ParserRuleContext {
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CommonTableExpressionContext extends ParserRuleContext {
    _name: IdentifierContext;
    AS(): TerminalNode;
    queryRelation(): QueryRelationContext;
    identifier(): IdentifierContext;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetQuantifierContext extends ParserRuleContext {
    DISTINCT(): TerminalNode | undefined;
    ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SelectItemContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: SelectItemContext): void;
}
export declare class SelectSingleContext extends SelectItemContext {
    expression(): ExpressionContext;
    identifier(): IdentifierContext | undefined;
    string(): StringContext | undefined;
    AS(): TerminalNode | undefined;
    constructor(ctx: SelectItemContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SelectAllContext extends SelectItemContext {
    qualifiedName(): QualifiedNameContext | undefined;
    ASTERISK_SYMBOL(): TerminalNode;
    constructor(ctx: SelectItemContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RelationsContext extends ParserRuleContext {
    relation(): RelationContext[];
    relation(i: number): RelationContext;
    LATERAL(): TerminalNode[];
    LATERAL(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RelationContext extends ParserRuleContext {
    relationPrimary(): RelationPrimaryContext;
    joinRelation(): JoinRelationContext[];
    joinRelation(i: number): JoinRelationContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RelationPrimaryContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: RelationPrimaryContext): void;
}
export declare class TableAtomContext extends RelationPrimaryContext {
    _alias: IdentifierContext;
    qualifiedName(): QualifiedNameContext;
    temporalClause(): TemporalClauseContext | undefined;
    partitionNames(): PartitionNamesContext | undefined;
    tabletList(): TabletListContext | undefined;
    replicaList(): ReplicaListContext | undefined;
    bracketHint(): BracketHintContext | undefined;
    identifier(): IdentifierContext | undefined;
    AS(): TerminalNode | undefined;
    constructor(ctx: RelationPrimaryContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class InlineTableContext extends RelationPrimaryContext {
    _alias: IdentifierContext;
    VALUES(): TerminalNode;
    rowConstructor(): RowConstructorContext[];
    rowConstructor(i: number): RowConstructorContext;
    identifier(): IdentifierContext | undefined;
    AS(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(ctx: RelationPrimaryContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SubqueryWithAliasContext extends RelationPrimaryContext {
    _alias: IdentifierContext;
    subquery(): SubqueryContext;
    identifier(): IdentifierContext | undefined;
    AS(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(ctx: RelationPrimaryContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class TableFunctionContext extends RelationPrimaryContext {
    _alias: IdentifierContext;
    qualifiedName(): QualifiedNameContext;
    expressionList(): ExpressionListContext;
    identifier(): IdentifierContext | undefined;
    AS(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(ctx: RelationPrimaryContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class NormalizedTableFunctionContext extends RelationPrimaryContext {
    _alias: IdentifierContext;
    TABLE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    expressionList(): ExpressionListContext;
    identifier(): IdentifierContext | undefined;
    AS(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(ctx: RelationPrimaryContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class FileTableFunctionContext extends RelationPrimaryContext {
    _alias: IdentifierContext;
    FILES(): TerminalNode;
    propertyList(): PropertyListContext;
    identifier(): IdentifierContext | undefined;
    AS(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(ctx: RelationPrimaryContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ParenthesizedRelationContext extends RelationPrimaryContext {
    relations(): RelationsContext;
    constructor(ctx: RelationPrimaryContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class JoinRelationContext extends ParserRuleContext {
    _rightRelation: RelationPrimaryContext;
    crossOrInnerJoinType(): CrossOrInnerJoinTypeContext | undefined;
    relationPrimary(): RelationPrimaryContext;
    bracketHint(): BracketHintContext | undefined;
    LATERAL(): TerminalNode | undefined;
    joinCriteria(): JoinCriteriaContext | undefined;
    outerAndSemiJoinType(): OuterAndSemiJoinTypeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CrossOrInnerJoinTypeContext extends ParserRuleContext {
    JOIN(): TerminalNode | undefined;
    INNER(): TerminalNode | undefined;
    CROSS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class OuterAndSemiJoinTypeContext extends ParserRuleContext {
    LEFT(): TerminalNode | undefined;
    JOIN(): TerminalNode;
    RIGHT(): TerminalNode | undefined;
    FULL(): TerminalNode | undefined;
    OUTER(): TerminalNode | undefined;
    SEMI(): TerminalNode | undefined;
    ANTI(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class BracketHintContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SetVarHintContext extends ParserRuleContext {
    SET_VAR(): TerminalNode;
    hintMap(): HintMapContext[];
    hintMap(i: number): HintMapContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class HintMapContext extends ParserRuleContext {
    _k: IdentifierOrStringContext;
    _v: LiteralExpressionContext;
    EQ(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    literalExpression(): LiteralExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class JoinCriteriaContext extends ParserRuleContext {
    ON(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    USING(): TerminalNode | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ColumnAliasesContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PartitionNamesContext extends ParserRuleContext {
    identifierOrString(): IdentifierOrStringContext[];
    identifierOrString(i: number): IdentifierOrStringContext;
    PARTITION(): TerminalNode | undefined;
    PARTITIONS(): TerminalNode | undefined;
    TEMPORARY(): TerminalNode | undefined;
    keyPartitions(): KeyPartitionsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class KeyPartitionsContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: KeyPartitionsContext): void;
}
export declare class KeyPartitionListContext extends KeyPartitionsContext {
    PARTITION(): TerminalNode;
    keyPartition(): KeyPartitionContext[];
    keyPartition(i: number): KeyPartitionContext;
    constructor(ctx: KeyPartitionsContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class TabletListContext extends ParserRuleContext {
    TABLET(): TerminalNode;
    INTEGER_VALUE(): TerminalNode[];
    INTEGER_VALUE(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PrepareStatementContext extends ParserRuleContext {
    PREPARE(): TerminalNode;
    identifier(): IdentifierContext;
    FROM(): TerminalNode;
    prepareSql(): PrepareSqlContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PrepareSqlContext extends ParserRuleContext {
    statement(): StatementContext | undefined;
    SINGLE_QUOTED_TEXT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ExecuteStatementContext extends ParserRuleContext {
    EXECUTE(): TerminalNode;
    identifier(): IdentifierContext;
    USING(): TerminalNode | undefined;
    AT(): TerminalNode[];
    AT(i: number): TerminalNode;
    identifierOrString(): IdentifierOrStringContext[];
    identifierOrString(i: number): IdentifierOrStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DeallocateStatementContext extends ParserRuleContext {
    PREPARE(): TerminalNode;
    identifier(): IdentifierContext;
    DEALLOCATE(): TerminalNode | undefined;
    DROP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ReplicaListContext extends ParserRuleContext {
    REPLICA(): TerminalNode;
    INTEGER_VALUE(): TerminalNode[];
    INTEGER_VALUE(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ExpressionsWithDefaultContext extends ParserRuleContext {
    expressionOrDefault(): ExpressionOrDefaultContext[];
    expressionOrDefault(i: number): ExpressionOrDefaultContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ExpressionOrDefaultContext extends ParserRuleContext {
    expression(): ExpressionContext | undefined;
    DEFAULT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class MapExpressionListContext extends ParserRuleContext {
    mapExpression(): MapExpressionContext[];
    mapExpression(i: number): MapExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class MapExpressionContext extends ParserRuleContext {
    _key: ExpressionContext;
    _value: ExpressionContext;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ExpressionSingletonContext extends ParserRuleContext {
    expression(): ExpressionContext;
    EOF(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ExpressionContext): void;
}
export declare class ExpressionDefaultContext extends ExpressionContext {
    booleanExpression(): BooleanExpressionContext;
    constructor(ctx: ExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class LogicalNotContext extends ExpressionContext {
    NOT(): TerminalNode;
    expression(): ExpressionContext;
    constructor(ctx: ExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class LogicalBinaryContext extends ExpressionContext {
    _left: ExpressionContext;
    _operator: Token;
    _right: ExpressionContext;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    AND(): TerminalNode | undefined;
    LOGICAL_AND(): TerminalNode | undefined;
    OR(): TerminalNode | undefined;
    LOGICAL_OR(): TerminalNode | undefined;
    constructor(ctx: ExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ExpressionListContext extends ParserRuleContext {
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class BooleanExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: BooleanExpressionContext): void;
}
export declare class BooleanExpressionDefaultContext extends BooleanExpressionContext {
    predicate(): PredicateContext;
    constructor(ctx: BooleanExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class IsNullContext extends BooleanExpressionContext {
    booleanExpression(): BooleanExpressionContext;
    IS(): TerminalNode;
    NULL(): TerminalNode;
    NOT(): TerminalNode | undefined;
    constructor(ctx: BooleanExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ComparisonContext extends BooleanExpressionContext {
    _left: BooleanExpressionContext;
    _right: PredicateContext;
    comparisonOperator(): ComparisonOperatorContext;
    booleanExpression(): BooleanExpressionContext;
    predicate(): PredicateContext;
    constructor(ctx: BooleanExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ScalarSubqueryContext extends BooleanExpressionContext {
    booleanExpression(): BooleanExpressionContext;
    comparisonOperator(): ComparisonOperatorContext;
    queryRelation(): QueryRelationContext;
    constructor(ctx: BooleanExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PredicateContext extends ParserRuleContext {
    _valueExpression: ValueExpressionContext;
    valueExpression(): ValueExpressionContext | undefined;
    predicateOperations(): PredicateOperationsContext | undefined;
    tupleInSubquery(): TupleInSubqueryContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class TupleInSubqueryContext extends ParserRuleContext {
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    IN(): TerminalNode;
    queryRelation(): QueryRelationContext;
    NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PredicateOperationsContext extends ParserRuleContext {
    value: ParserRuleContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number, value: ParserRuleContext);
    get ruleIndex(): number;
    copyFrom(ctx: PredicateOperationsContext): void;
}
export declare class InSubqueryContext extends PredicateOperationsContext {
    IN(): TerminalNode;
    queryRelation(): QueryRelationContext;
    NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateOperationsContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class InListContext extends PredicateOperationsContext {
    IN(): TerminalNode;
    expressionList(): ExpressionListContext;
    NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateOperationsContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class BetweenContext extends PredicateOperationsContext {
    _lower: ValueExpressionContext;
    _upper: PredicateContext;
    BETWEEN(): TerminalNode;
    AND(): TerminalNode;
    valueExpression(): ValueExpressionContext;
    predicate(): PredicateContext;
    NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateOperationsContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class LikeContext extends PredicateOperationsContext {
    _pattern: ValueExpressionContext;
    LIKE(): TerminalNode | undefined;
    RLIKE(): TerminalNode | undefined;
    REGEXP(): TerminalNode | undefined;
    valueExpression(): ValueExpressionContext;
    NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateOperationsContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ValueExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ValueExpressionContext): void;
}
export declare class ValueExpressionDefaultContext extends ValueExpressionContext {
    primaryExpression(): PrimaryExpressionContext;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ArithmeticBinaryContext extends ValueExpressionContext {
    _left: ValueExpressionContext;
    _operator: Token;
    _right: ValueExpressionContext;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    BITXOR(): TerminalNode | undefined;
    ASTERISK_SYMBOL(): TerminalNode | undefined;
    SLASH_SYMBOL(): TerminalNode | undefined;
    PERCENT_SYMBOL(): TerminalNode | undefined;
    INT_DIV(): TerminalNode | undefined;
    MOD(): TerminalNode | undefined;
    PLUS_SYMBOL(): TerminalNode | undefined;
    MINUS_SYMBOL(): TerminalNode | undefined;
    BITAND(): TerminalNode | undefined;
    BITOR(): TerminalNode | undefined;
    BIT_SHIFT_LEFT(): TerminalNode | undefined;
    BIT_SHIFT_RIGHT(): TerminalNode | undefined;
    BIT_SHIFT_RIGHT_LOGICAL(): TerminalNode | undefined;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PrimaryExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PrimaryExpressionContext): void;
}
export declare class UserVariableExpressionContext extends PrimaryExpressionContext {
    userVariable(): UserVariableContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SystemVariableExpressionContext extends PrimaryExpressionContext {
    systemVariable(): SystemVariableContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class FunctionCallExpressionContext extends PrimaryExpressionContext {
    functionCall(): FunctionCallContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class OdbcFunctionCallExpressionContext extends PrimaryExpressionContext {
    FN(): TerminalNode;
    functionCall(): FunctionCallContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CollateContext extends PrimaryExpressionContext {
    primaryExpression(): PrimaryExpressionContext;
    COLLATE(): TerminalNode;
    identifier(): IdentifierContext | undefined;
    string(): StringContext | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class LiteralContext extends PrimaryExpressionContext {
    literalExpression(): LiteralExpressionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ColumnRefContext extends PrimaryExpressionContext {
    columnReference(): ColumnReferenceContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DereferenceContext extends PrimaryExpressionContext {
    _base: PrimaryExpressionContext;
    _fieldName: IdentifierContext;
    primaryExpression(): PrimaryExpressionContext;
    DOT_IDENTIFIER(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ConcatContext extends PrimaryExpressionContext {
    _left: PrimaryExpressionContext;
    _right: PrimaryExpressionContext;
    CONCAT(): TerminalNode;
    primaryExpression(): PrimaryExpressionContext[];
    primaryExpression(i: number): PrimaryExpressionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ArithmeticUnaryContext extends PrimaryExpressionContext {
    _operator: Token;
    primaryExpression(): PrimaryExpressionContext;
    MINUS_SYMBOL(): TerminalNode | undefined;
    PLUS_SYMBOL(): TerminalNode | undefined;
    BITNOT(): TerminalNode | undefined;
    LOGICAL_NOT(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ParenthesizedExpressionContext extends PrimaryExpressionContext {
    expression(): ExpressionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ExistsContext extends PrimaryExpressionContext {
    EXISTS(): TerminalNode;
    queryRelation(): QueryRelationContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SubqueryExpressionContext extends PrimaryExpressionContext {
    subquery(): SubqueryContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CastContext extends PrimaryExpressionContext {
    CAST(): TerminalNode;
    expression(): ExpressionContext;
    AS(): TerminalNode;
    type(): TypeContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ConvertContext extends PrimaryExpressionContext {
    CONVERT(): TerminalNode;
    expression(): ExpressionContext;
    type(): TypeContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SimpleCaseContext extends PrimaryExpressionContext {
    _caseExpr: ExpressionContext;
    _elseExpression: ExpressionContext;
    CASE(): TerminalNode;
    END(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    whenClause(): WhenClauseContext[];
    whenClause(i: number): WhenClauseContext;
    ELSE(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SearchedCaseContext extends PrimaryExpressionContext {
    _elseExpression: ExpressionContext;
    CASE(): TerminalNode;
    END(): TerminalNode;
    whenClause(): WhenClauseContext[];
    whenClause(i: number): WhenClauseContext;
    ELSE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ArrayConstructorContext extends PrimaryExpressionContext {
    arrayType(): ArrayTypeContext | undefined;
    expressionList(): ExpressionListContext | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class MapConstructorContext extends PrimaryExpressionContext {
    mapType(): MapTypeContext | undefined;
    mapExpressionList(): MapExpressionListContext | undefined;
    MAP(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CollectionSubscriptContext extends PrimaryExpressionContext {
    _value: PrimaryExpressionContext;
    _index: ValueExpressionContext;
    primaryExpression(): PrimaryExpressionContext;
    valueExpression(): ValueExpressionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ArraySliceContext extends PrimaryExpressionContext {
    _start: Token;
    _end: Token;
    primaryExpression(): PrimaryExpressionContext;
    INTEGER_VALUE(): TerminalNode[];
    INTEGER_VALUE(i: number): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ArrowExpressionContext extends PrimaryExpressionContext {
    primaryExpression(): PrimaryExpressionContext;
    ARROW(): TerminalNode;
    string(): StringContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class LambdaFunctionExprContext extends PrimaryExpressionContext {
    ARROW(): TerminalNode;
    expression(): ExpressionContext | undefined;
    identifier(): IdentifierContext | undefined;
    identifierList(): IdentifierListContext | undefined;
    expressionList(): ExpressionListContext | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class LiteralExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: LiteralExpressionContext): void;
}
export declare class NullLiteralContext extends LiteralExpressionContext {
    NULL(): TerminalNode;
    constructor(ctx: LiteralExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class BooleanLiteralContext extends LiteralExpressionContext {
    booleanValue(): BooleanValueContext;
    constructor(ctx: LiteralExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class NumericLiteralContext extends LiteralExpressionContext {
    number(): NumberContext;
    constructor(ctx: LiteralExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DateLiteralContext extends LiteralExpressionContext {
    string(): StringContext;
    DATE(): TerminalNode | undefined;
    DATETIME(): TerminalNode | undefined;
    constructor(ctx: LiteralExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class StringLiteralContext extends LiteralExpressionContext {
    string(): StringContext;
    constructor(ctx: LiteralExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class IntervalLiteralContext extends LiteralExpressionContext {
    interval(): IntervalContext;
    constructor(ctx: LiteralExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UnitBoundaryLiteralContext extends LiteralExpressionContext {
    unitBoundary(): UnitBoundaryContext;
    constructor(ctx: LiteralExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class BinaryLiteralContext extends LiteralExpressionContext {
    binary(): BinaryContext;
    constructor(ctx: LiteralExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ParameterContext extends LiteralExpressionContext {
    PARAMETER(): TerminalNode;
    constructor(ctx: LiteralExpressionContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class FunctionCallContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: FunctionCallContext): void;
}
export declare class ExtractContext extends FunctionCallContext {
    EXTRACT(): TerminalNode;
    identifier(): IdentifierContext;
    FROM(): TerminalNode;
    valueExpression(): ValueExpressionContext;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class GroupingOperationContext extends FunctionCallContext {
    GROUPING(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    GROUPING_ID(): TerminalNode | undefined;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class InformationFunctionContext extends FunctionCallContext {
    informationFunctionExpression(): InformationFunctionExpressionContext;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SpecialDateTimeContext extends FunctionCallContext {
    specialDateTimeExpression(): SpecialDateTimeExpressionContext;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SpecialFunctionContext extends FunctionCallContext {
    specialFunctionExpression(): SpecialFunctionExpressionContext;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AggregationFunctionCallContext extends FunctionCallContext {
    aggregationFunction(): AggregationFunctionContext;
    over(): OverContext | undefined;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class WindowFunctionCallContext extends FunctionCallContext {
    windowFunction(): WindowFunctionContext;
    over(): OverContext;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SimpleFunctionCallContext extends FunctionCallContext {
    qualifiedName(): QualifiedNameContext;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    over(): OverContext | undefined;
    constructor(ctx: FunctionCallContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AggregationFunctionContext extends ParserRuleContext {
    AVG(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    setQuantifier(): SetQuantifierContext | undefined;
    COUNT(): TerminalNode | undefined;
    ASTERISK_SYMBOL(): TerminalNode | undefined;
    bracketHint(): BracketHintContext | undefined;
    MAX(): TerminalNode | undefined;
    MIN(): TerminalNode | undefined;
    SUM(): TerminalNode | undefined;
    ARRAY_AGG(): TerminalNode | undefined;
    ORDER(): TerminalNode | undefined;
    BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    GROUP_CONCAT(): TerminalNode | undefined;
    SEPARATOR(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UserVariableContext extends ParserRuleContext {
    AT(): TerminalNode;
    identifierOrString(): IdentifierOrStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SystemVariableContext extends ParserRuleContext {
    AT(): TerminalNode[];
    AT(i: number): TerminalNode;
    identifier(): IdentifierContext;
    varType(): VarTypeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ColumnReferenceContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class InformationFunctionExpressionContext extends ParserRuleContext {
    _name: Token;
    CATALOG(): TerminalNode | undefined;
    DATABASE(): TerminalNode | undefined;
    SCHEMA(): TerminalNode | undefined;
    USER(): TerminalNode | undefined;
    CURRENT_USER(): TerminalNode | undefined;
    CURRENT_ROLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SpecialDateTimeExpressionContext extends ParserRuleContext {
    _name: Token;
    CURRENT_DATE(): TerminalNode | undefined;
    CURRENT_TIME(): TerminalNode | undefined;
    CURRENT_TIMESTAMP(): TerminalNode | undefined;
    LOCALTIME(): TerminalNode | undefined;
    LOCALTIMESTAMP(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SpecialFunctionExpressionContext extends ParserRuleContext {
    CHAR(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    DAY(): TerminalNode | undefined;
    HOUR(): TerminalNode | undefined;
    IF(): TerminalNode | undefined;
    LEFT(): TerminalNode | undefined;
    LIKE(): TerminalNode | undefined;
    MINUTE(): TerminalNode | undefined;
    MOD(): TerminalNode | undefined;
    MONTH(): TerminalNode | undefined;
    QUARTER(): TerminalNode | undefined;
    REGEXP(): TerminalNode | undefined;
    REPLACE(): TerminalNode | undefined;
    RIGHT(): TerminalNode | undefined;
    RLIKE(): TerminalNode | undefined;
    SECOND(): TerminalNode | undefined;
    TIMESTAMPADD(): TerminalNode | undefined;
    unitIdentifier(): UnitIdentifierContext | undefined;
    TIMESTAMPDIFF(): TerminalNode | undefined;
    YEAR(): TerminalNode | undefined;
    PASSWORD(): TerminalNode | undefined;
    string(): StringContext | undefined;
    FLOOR(): TerminalNode | undefined;
    CEIL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class WindowFunctionContext extends ParserRuleContext {
    _name: Token;
    ROW_NUMBER(): TerminalNode | undefined;
    RANK(): TerminalNode | undefined;
    DENSE_RANK(): TerminalNode | undefined;
    CUME_DIST(): TerminalNode | undefined;
    PERCENT_RANK(): TerminalNode | undefined;
    NTILE(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    LEAD(): TerminalNode | undefined;
    ignoreNulls(): IgnoreNullsContext[];
    ignoreNulls(i: number): IgnoreNullsContext;
    LAG(): TerminalNode | undefined;
    FIRST_VALUE(): TerminalNode | undefined;
    LAST_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class WhenClauseContext extends ParserRuleContext {
    _condition: ExpressionContext;
    _result: ExpressionContext;
    WHEN(): TerminalNode;
    THEN(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class OverContext extends ParserRuleContext {
    _expression: ExpressionContext;
    _partition: ExpressionContext[];
    OVER(): TerminalNode;
    PARTITION(): TerminalNode | undefined;
    BY(): TerminalNode[];
    BY(i: number): TerminalNode;
    ORDER(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    bracketHint(): BracketHintContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class IgnoreNullsContext extends ParserRuleContext {
    IGNORE(): TerminalNode;
    NULLS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class TableDescContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext;
    partitionNames(): PartitionNamesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RestoreTableDescContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext;
    partitionNames(): PartitionNamesContext | undefined;
    AS(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ExplainDescContext extends ParserRuleContext {
    DESC(): TerminalNode | undefined;
    DESCRIBE(): TerminalNode | undefined;
    EXPLAIN(): TerminalNode | undefined;
    LOGICAL(): TerminalNode | undefined;
    ANALYZE(): TerminalNode | undefined;
    VERBOSE(): TerminalNode | undefined;
    COSTS(): TerminalNode | undefined;
    SCHEDULER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class OptimizerTraceContext extends ParserRuleContext {
    TRACE(): TerminalNode;
    ALL(): TerminalNode | undefined;
    LOGS(): TerminalNode | undefined;
    TIMES(): TerminalNode | undefined;
    VALUES(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PartitionDescContext extends ParserRuleContext {
    PARTITION(): TerminalNode;
    BY(): TerminalNode;
    RANGE(): TerminalNode | undefined;
    identifierList(): IdentifierListContext | undefined;
    rangePartitionDesc(): RangePartitionDescContext[];
    rangePartitionDesc(i: number): RangePartitionDescContext;
    primaryExpression(): PrimaryExpressionContext | undefined;
    LIST(): TerminalNode | undefined;
    listPartitionDesc(): ListPartitionDescContext[];
    listPartitionDesc(i: number): ListPartitionDescContext;
    functionCall(): FunctionCallContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ListPartitionDescContext extends ParserRuleContext {
    singleItemListPartitionDesc(): SingleItemListPartitionDescContext | undefined;
    multiItemListPartitionDesc(): MultiItemListPartitionDescContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SingleItemListPartitionDescContext extends ParserRuleContext {
    PARTITION(): TerminalNode;
    identifier(): IdentifierContext;
    VALUES(): TerminalNode;
    IN(): TerminalNode;
    stringList(): StringListContext;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    propertyList(): PropertyListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class MultiItemListPartitionDescContext extends ParserRuleContext {
    PARTITION(): TerminalNode;
    identifier(): IdentifierContext;
    VALUES(): TerminalNode;
    IN(): TerminalNode;
    stringList(): StringListContext[];
    stringList(i: number): StringListContext;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    propertyList(): PropertyListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class StringListContext extends ParserRuleContext {
    string(): StringContext[];
    string(i: number): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RangePartitionDescContext extends ParserRuleContext {
    singleRangePartition(): SingleRangePartitionContext | undefined;
    multiRangePartition(): MultiRangePartitionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SingleRangePartitionContext extends ParserRuleContext {
    PARTITION(): TerminalNode;
    identifier(): IdentifierContext;
    VALUES(): TerminalNode;
    partitionKeyDesc(): PartitionKeyDescContext;
    IF(): TerminalNode | undefined;
    NOT(): TerminalNode | undefined;
    EXISTS(): TerminalNode | undefined;
    propertyList(): PropertyListContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class MultiRangePartitionContext extends ParserRuleContext {
    START(): TerminalNode;
    string(): StringContext[];
    string(i: number): StringContext;
    END(): TerminalNode;
    EVERY(): TerminalNode;
    interval(): IntervalContext | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PartitionRangeDescContext extends ParserRuleContext {
    START(): TerminalNode;
    string(): StringContext[];
    string(i: number): StringContext;
    END(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PartitionKeyDescContext extends ParserRuleContext {
    LESS(): TerminalNode | undefined;
    THAN(): TerminalNode | undefined;
    MAXVALUE(): TerminalNode | undefined;
    partitionValueList(): PartitionValueListContext[];
    partitionValueList(i: number): PartitionValueListContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PartitionValueListContext extends ParserRuleContext {
    partitionValue(): PartitionValueContext[];
    partitionValue(i: number): PartitionValueContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class KeyPartitionContext extends ParserRuleContext {
    _partitionColName: IdentifierContext;
    _partitionColValue: LiteralExpressionContext;
    EQ(): TerminalNode;
    identifier(): IdentifierContext;
    literalExpression(): LiteralExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PartitionValueContext extends ParserRuleContext {
    MAXVALUE(): TerminalNode | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DistributionClauseContext extends ParserRuleContext {
    DISTRIBUTED(): TerminalNode;
    BY(): TerminalNode;
    HASH(): TerminalNode;
    identifierList(): IdentifierListContext;
    BUCKETS(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DistributionDescContext extends ParserRuleContext {
    DISTRIBUTED(): TerminalNode;
    BY(): TerminalNode;
    HASH(): TerminalNode | undefined;
    identifierList(): IdentifierListContext | undefined;
    BUCKETS(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    RANDOM(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class RefreshSchemeDescContext extends ParserRuleContext {
    REFRESH(): TerminalNode;
    ASYNC(): TerminalNode | undefined;
    EVERY(): TerminalNode | undefined;
    interval(): IntervalContext | undefined;
    INCREMENTAL(): TerminalNode | undefined;
    MANUAL(): TerminalNode | undefined;
    IMMEDIATE(): TerminalNode | undefined;
    DEFERRED(): TerminalNode | undefined;
    START(): TerminalNode | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class StatusDescContext extends ParserRuleContext {
    ACTIVE(): TerminalNode | undefined;
    INACTIVE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PropertiesContext extends ParserRuleContext {
    PROPERTIES(): TerminalNode;
    property(): PropertyContext[];
    property(i: number): PropertyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ExtPropertiesContext extends ParserRuleContext {
    BROKER(): TerminalNode;
    properties(): PropertiesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PropertyListContext extends ParserRuleContext {
    property(): PropertyContext[];
    property(i: number): PropertyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UserPropertyListContext extends ParserRuleContext {
    property(): PropertyContext[];
    property(i: number): PropertyContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class PropertyContext extends ParserRuleContext {
    _key: StringContext;
    _value: StringContext;
    EQ(): TerminalNode;
    string(): StringContext[];
    string(i: number): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class VarTypeContext extends ParserRuleContext {
    GLOBAL(): TerminalNode | undefined;
    LOCAL(): TerminalNode | undefined;
    SESSION(): TerminalNode | undefined;
    VERBOSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class CommentContext extends ParserRuleContext {
    COMMENT(): TerminalNode;
    string(): StringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class OutfileContext extends ParserRuleContext {
    _file: StringContext;
    INTO(): TerminalNode;
    OUTFILE(): TerminalNode;
    string(): StringContext;
    fileFormat(): FileFormatContext | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class FileFormatContext extends ParserRuleContext {
    FORMAT(): TerminalNode;
    AS(): TerminalNode;
    identifier(): IdentifierContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class StringContext extends ParserRuleContext {
    SINGLE_QUOTED_TEXT(): TerminalNode | undefined;
    DOUBLE_QUOTED_TEXT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class BinaryContext extends ParserRuleContext {
    BINARY_SINGLE_QUOTED_TEXT(): TerminalNode | undefined;
    BINARY_DOUBLE_QUOTED_TEXT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ComparisonOperatorContext extends ParserRuleContext {
    EQ(): TerminalNode | undefined;
    NEQ(): TerminalNode | undefined;
    LT(): TerminalNode | undefined;
    LTE(): TerminalNode | undefined;
    GT(): TerminalNode | undefined;
    GTE(): TerminalNode | undefined;
    EQ_FOR_NULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class BooleanValueContext extends ParserRuleContext {
    TRUE(): TerminalNode | undefined;
    FALSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class IntervalContext extends ParserRuleContext {
    _value: ExpressionContext;
    _from: UnitIdentifierContext;
    INTERVAL(): TerminalNode;
    expression(): ExpressionContext;
    unitIdentifier(): UnitIdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UnitIdentifierContext extends ParserRuleContext {
    YEAR(): TerminalNode | undefined;
    MONTH(): TerminalNode | undefined;
    WEEK(): TerminalNode | undefined;
    DAY(): TerminalNode | undefined;
    HOUR(): TerminalNode | undefined;
    MINUTE(): TerminalNode | undefined;
    SECOND(): TerminalNode | undefined;
    QUARTER(): TerminalNode | undefined;
    MILLISECOND(): TerminalNode | undefined;
    MICROSECOND(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UnitBoundaryContext extends ParserRuleContext {
    FLOOR(): TerminalNode | undefined;
    CEIL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class TypeContext extends ParserRuleContext {
    baseType(): BaseTypeContext | undefined;
    decimalType(): DecimalTypeContext | undefined;
    arrayType(): ArrayTypeContext | undefined;
    structType(): StructTypeContext | undefined;
    mapType(): MapTypeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class ArrayTypeContext extends ParserRuleContext {
    ARRAY(): TerminalNode;
    LT(): TerminalNode;
    type(): TypeContext;
    GT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class MapTypeContext extends ParserRuleContext {
    MAP(): TerminalNode;
    LT(): TerminalNode;
    type(): TypeContext[];
    type(i: number): TypeContext;
    GT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SubfieldDescContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    type(): TypeContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class SubfieldDescsContext extends ParserRuleContext {
    subfieldDesc(): SubfieldDescContext[];
    subfieldDesc(i: number): SubfieldDescContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class StructTypeContext extends ParserRuleContext {
    STRUCT(): TerminalNode;
    LT(): TerminalNode;
    subfieldDescs(): SubfieldDescsContext;
    GT(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class TypeParameterContext extends ParserRuleContext {
    INTEGER_VALUE(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class BaseTypeContext extends ParserRuleContext {
    BOOLEAN(): TerminalNode | undefined;
    TINYINT(): TerminalNode | undefined;
    typeParameter(): TypeParameterContext | undefined;
    SMALLINT(): TerminalNode | undefined;
    SIGNED(): TerminalNode | undefined;
    INT(): TerminalNode | undefined;
    INTEGER(): TerminalNode | undefined;
    UNSIGNED(): TerminalNode | undefined;
    BIGINT(): TerminalNode | undefined;
    LARGEINT(): TerminalNode | undefined;
    FLOAT(): TerminalNode | undefined;
    DOUBLE(): TerminalNode | undefined;
    DATE(): TerminalNode | undefined;
    DATETIME(): TerminalNode | undefined;
    TIME(): TerminalNode | undefined;
    CHAR(): TerminalNode | undefined;
    VARCHAR(): TerminalNode | undefined;
    STRING(): TerminalNode | undefined;
    TEXT(): TerminalNode | undefined;
    BITMAP(): TerminalNode | undefined;
    HLL(): TerminalNode | undefined;
    PERCENTILE(): TerminalNode | undefined;
    JSON(): TerminalNode | undefined;
    VARBINARY(): TerminalNode | undefined;
    BINARY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DecimalTypeContext extends ParserRuleContext {
    _precision: Token;
    _scale: Token;
    DECIMAL(): TerminalNode | undefined;
    DECIMALV2(): TerminalNode | undefined;
    DECIMAL32(): TerminalNode | undefined;
    DECIMAL64(): TerminalNode | undefined;
    DECIMAL128(): TerminalNode | undefined;
    NUMERIC(): TerminalNode | undefined;
    NUMBER(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode[];
    INTEGER_VALUE(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class QualifiedNameContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    DOT_IDENTIFIER(): TerminalNode[];
    DOT_IDENTIFIER(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class IdentifierContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: IdentifierContext): void;
}
export declare class UnquotedIdentifierContext extends IdentifierContext {
    LETTER_IDENTIFIER(): TerminalNode | undefined;
    nonReserved(): NonReservedContext | undefined;
    constructor(ctx: IdentifierContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DigitIdentifierContext extends IdentifierContext {
    DIGIT_IDENTIFIER(): TerminalNode;
    constructor(ctx: IdentifierContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class BackQuotedIdentifierContext extends IdentifierContext {
    BACKQUOTED_IDENTIFIER(): TerminalNode;
    constructor(ctx: IdentifierContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class IdentifierListContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class IdentifierOrStringContext extends ParserRuleContext {
    identifier(): IdentifierContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class IdentifierOrStringListContext extends ParserRuleContext {
    identifierOrString(): IdentifierOrStringContext[];
    identifierOrString(i: number): IdentifierOrStringContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class IdentifierOrStringOrStarContext extends ParserRuleContext {
    ASTERISK_SYMBOL(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    string(): StringContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UserContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: UserContext): void;
}
export declare class UserWithoutHostContext extends UserContext {
    identifierOrString(): IdentifierOrStringContext;
    constructor(ctx: UserContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UserWithHostContext extends UserContext {
    identifierOrString(): IdentifierOrStringContext[];
    identifierOrString(i: number): IdentifierOrStringContext;
    AT(): TerminalNode;
    constructor(ctx: UserContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class UserWithHostAndBlanketContext extends UserContext {
    identifierOrString(): IdentifierOrStringContext[];
    identifierOrString(i: number): IdentifierOrStringContext;
    AT(): TerminalNode;
    constructor(ctx: UserContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AssignmentContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    EQ(): TerminalNode;
    expressionOrDefault(): ExpressionOrDefaultContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class AssignmentListContext extends ParserRuleContext {
    assignment(): AssignmentContext[];
    assignment(i: number): AssignmentContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class NumberContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: NumberContext): void;
}
export declare class DecimalValueContext extends NumberContext {
    DECIMAL_VALUE(): TerminalNode;
    constructor(ctx: NumberContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class DoubleValueContext extends NumberContext {
    DOUBLE_VALUE(): TerminalNode;
    constructor(ctx: NumberContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class IntegerValueContext extends NumberContext {
    INTEGER_VALUE(): TerminalNode;
    constructor(ctx: NumberContext);
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
export declare class NonReservedContext extends ParserRuleContext {
    ACCESS(): TerminalNode | undefined;
    ACTIVE(): TerminalNode | undefined;
    AFTER(): TerminalNode | undefined;
    AGGREGATE(): TerminalNode | undefined;
    APPLY(): TerminalNode | undefined;
    ASYNC(): TerminalNode | undefined;
    AUTHORS(): TerminalNode | undefined;
    AVG(): TerminalNode | undefined;
    ADMIN(): TerminalNode | undefined;
    ANTI(): TerminalNode | undefined;
    AUTHENTICATION(): TerminalNode | undefined;
    AUTO_INCREMENT(): TerminalNode | undefined;
    ARRAY_AGG(): TerminalNode | undefined;
    BACKEND(): TerminalNode | undefined;
    BACKENDS(): TerminalNode | undefined;
    BACKUP(): TerminalNode | undefined;
    BEGIN(): TerminalNode | undefined;
    BITMAP_UNION(): TerminalNode | undefined;
    BLACKLIST(): TerminalNode | undefined;
    BINARY(): TerminalNode | undefined;
    BODY(): TerminalNode | undefined;
    BOOLEAN(): TerminalNode | undefined;
    BROKER(): TerminalNode | undefined;
    BUCKETS(): TerminalNode | undefined;
    BUILTIN(): TerminalNode | undefined;
    BASE(): TerminalNode | undefined;
    CAST(): TerminalNode | undefined;
    CANCEL(): TerminalNode | undefined;
    CATALOG(): TerminalNode | undefined;
    CATALOGS(): TerminalNode | undefined;
    CEIL(): TerminalNode | undefined;
    CHAIN(): TerminalNode | undefined;
    CHARSET(): TerminalNode | undefined;
    CLEAN(): TerminalNode | undefined;
    CLEAR(): TerminalNode | undefined;
    CLUSTER(): TerminalNode | undefined;
    CLUSTERS(): TerminalNode | undefined;
    CURRENT(): TerminalNode | undefined;
    COLLATION(): TerminalNode | undefined;
    COLUMNS(): TerminalNode | undefined;
    CUME_DIST(): TerminalNode | undefined;
    CUMULATIVE(): TerminalNode | undefined;
    COMMENT(): TerminalNode | undefined;
    COMMIT(): TerminalNode | undefined;
    COMMITTED(): TerminalNode | undefined;
    COMPUTE(): TerminalNode | undefined;
    CONNECTION(): TerminalNode | undefined;
    CONSISTENT(): TerminalNode | undefined;
    COSTS(): TerminalNode | undefined;
    COUNT(): TerminalNode | undefined;
    CONFIG(): TerminalNode | undefined;
    COMPACT(): TerminalNode | undefined;
    DATA(): TerminalNode | undefined;
    DATE(): TerminalNode | undefined;
    DATACACHE(): TerminalNode | undefined;
    DATETIME(): TerminalNode | undefined;
    DAY(): TerminalNode | undefined;
    DECOMMISSION(): TerminalNode | undefined;
    DISABLE(): TerminalNode | undefined;
    DISTRIBUTION(): TerminalNode | undefined;
    DUPLICATE(): TerminalNode | undefined;
    DYNAMIC(): TerminalNode | undefined;
    DISTRIBUTED(): TerminalNode | undefined;
    DEALLOCATE(): TerminalNode | undefined;
    ENABLE(): TerminalNode | undefined;
    END(): TerminalNode | undefined;
    ENGINE(): TerminalNode | undefined;
    ENGINES(): TerminalNode | undefined;
    ERRORS(): TerminalNode | undefined;
    EVENTS(): TerminalNode | undefined;
    EXECUTE(): TerminalNode | undefined;
    EXTERNAL(): TerminalNode | undefined;
    EXTRACT(): TerminalNode | undefined;
    EVERY(): TerminalNode | undefined;
    ENCLOSE(): TerminalNode | undefined;
    ESCAPE(): TerminalNode | undefined;
    EXPORT(): TerminalNode | undefined;
    FAILPOINT(): TerminalNode | undefined;
    FAILPOINTS(): TerminalNode | undefined;
    FIELDS(): TerminalNode | undefined;
    FILE(): TerminalNode | undefined;
    FILTER(): TerminalNode | undefined;
    FIRST(): TerminalNode | undefined;
    FLOOR(): TerminalNode | undefined;
    FOLLOWING(): TerminalNode | undefined;
    FORMAT(): TerminalNode | undefined;
    FN(): TerminalNode | undefined;
    FRONTEND(): TerminalNode | undefined;
    FRONTENDS(): TerminalNode | undefined;
    FOLLOWER(): TerminalNode | undefined;
    FREE(): TerminalNode | undefined;
    FUNCTIONS(): TerminalNode | undefined;
    GLOBAL(): TerminalNode | undefined;
    GRANTS(): TerminalNode | undefined;
    GROUP_CONCAT(): TerminalNode | undefined;
    HASH(): TerminalNode | undefined;
    HISTOGRAM(): TerminalNode | undefined;
    HELP(): TerminalNode | undefined;
    HLL_UNION(): TerminalNode | undefined;
    HOST(): TerminalNode | undefined;
    HOUR(): TerminalNode | undefined;
    HUB(): TerminalNode | undefined;
    IDENTIFIED(): TerminalNode | undefined;
    IMAGE(): TerminalNode | undefined;
    IMPERSONATE(): TerminalNode | undefined;
    INACTIVE(): TerminalNode | undefined;
    INCREMENTAL(): TerminalNode | undefined;
    INDEXES(): TerminalNode | undefined;
    INSTALL(): TerminalNode | undefined;
    INTEGRATION(): TerminalNode | undefined;
    INTEGRATIONS(): TerminalNode | undefined;
    INTERMEDIATE(): TerminalNode | undefined;
    INTERVAL(): TerminalNode | undefined;
    ISOLATION(): TerminalNode | undefined;
    JOB(): TerminalNode | undefined;
    LABEL(): TerminalNode | undefined;
    LAST(): TerminalNode | undefined;
    LESS(): TerminalNode | undefined;
    LEVEL(): TerminalNode | undefined;
    LIST(): TerminalNode | undefined;
    LOCAL(): TerminalNode | undefined;
    LOCATION(): TerminalNode | undefined;
    LOGS(): TerminalNode | undefined;
    LOGICAL(): TerminalNode | undefined;
    LOW_PRIORITY(): TerminalNode | undefined;
    LOCK(): TerminalNode | undefined;
    LOCATIONS(): TerminalNode | undefined;
    MASKING(): TerminalNode | undefined;
    MANUAL(): TerminalNode | undefined;
    MAP(): TerminalNode | undefined;
    MAPPING(): TerminalNode | undefined;
    MAPPINGS(): TerminalNode | undefined;
    MATERIALIZED(): TerminalNode | undefined;
    MAX(): TerminalNode | undefined;
    META(): TerminalNode | undefined;
    MIN(): TerminalNode | undefined;
    MINUTE(): TerminalNode | undefined;
    MODE(): TerminalNode | undefined;
    MODIFY(): TerminalNode | undefined;
    MONTH(): TerminalNode | undefined;
    MERGE(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    NAME(): TerminalNode | undefined;
    NAMES(): TerminalNode | undefined;
    NEGATIVE(): TerminalNode | undefined;
    NO(): TerminalNode | undefined;
    NODE(): TerminalNode | undefined;
    NODES(): TerminalNode | undefined;
    NONE(): TerminalNode | undefined;
    NULLS(): TerminalNode | undefined;
    NUMBER(): TerminalNode | undefined;
    NUMERIC(): TerminalNode | undefined;
    OBSERVER(): TerminalNode | undefined;
    OF(): TerminalNode | undefined;
    OFFSET(): TerminalNode | undefined;
    ONLY(): TerminalNode | undefined;
    OPTIMIZER(): TerminalNode | undefined;
    OPEN(): TerminalNode | undefined;
    OPERATE(): TerminalNode | undefined;
    OPTION(): TerminalNode | undefined;
    OVERWRITE(): TerminalNode | undefined;
    PARTITIONS(): TerminalNode | undefined;
    PASSWORD(): TerminalNode | undefined;
    PATH(): TerminalNode | undefined;
    PAUSE(): TerminalNode | undefined;
    PENDING(): TerminalNode | undefined;
    PERCENTILE_UNION(): TerminalNode | undefined;
    PLUGIN(): TerminalNode | undefined;
    PLUGINS(): TerminalNode | undefined;
    POLICY(): TerminalNode | undefined;
    POLICIES(): TerminalNode | undefined;
    PERCENT_RANK(): TerminalNode | undefined;
    PRECEDING(): TerminalNode | undefined;
    PRIORITY(): TerminalNode | undefined;
    PROC(): TerminalNode | undefined;
    PROCESSLIST(): TerminalNode | undefined;
    PROFILE(): TerminalNode | undefined;
    PROFILELIST(): TerminalNode | undefined;
    PRIVILEGES(): TerminalNode | undefined;
    PROBABILITY(): TerminalNode | undefined;
    PROPERTIES(): TerminalNode | undefined;
    PROPERTY(): TerminalNode | undefined;
    PIPE(): TerminalNode | undefined;
    PIPES(): TerminalNode | undefined;
    QUARTER(): TerminalNode | undefined;
    QUERY(): TerminalNode | undefined;
    QUERIES(): TerminalNode | undefined;
    QUEUE(): TerminalNode | undefined;
    QUOTA(): TerminalNode | undefined;
    QUALIFY(): TerminalNode | undefined;
    REMOVE(): TerminalNode | undefined;
    REWRITE(): TerminalNode | undefined;
    RANDOM(): TerminalNode | undefined;
    RANK(): TerminalNode | undefined;
    RECOVER(): TerminalNode | undefined;
    REFRESH(): TerminalNode | undefined;
    REPAIR(): TerminalNode | undefined;
    REPEATABLE(): TerminalNode | undefined;
    REPLACE_IF_NOT_NULL(): TerminalNode | undefined;
    REPLICA(): TerminalNode | undefined;
    REPOSITORY(): TerminalNode | undefined;
    REPOSITORIES(): TerminalNode | undefined;
    RESOURCE(): TerminalNode | undefined;
    RESOURCES(): TerminalNode | undefined;
    RESTORE(): TerminalNode | undefined;
    RESUME(): TerminalNode | undefined;
    RETURNS(): TerminalNode | undefined;
    RETRY(): TerminalNode | undefined;
    REVERT(): TerminalNode | undefined;
    ROLE(): TerminalNode | undefined;
    ROLES(): TerminalNode | undefined;
    ROLLUP(): TerminalNode | undefined;
    ROLLBACK(): TerminalNode | undefined;
    ROUTINE(): TerminalNode | undefined;
    ROW(): TerminalNode | undefined;
    RUNNING(): TerminalNode | undefined;
    RULE(): TerminalNode | undefined;
    RULES(): TerminalNode | undefined;
    SAMPLE(): TerminalNode | undefined;
    SCHEDULER(): TerminalNode | undefined;
    SECOND(): TerminalNode | undefined;
    SECURITY(): TerminalNode | undefined;
    SEPARATOR(): TerminalNode | undefined;
    SERIALIZABLE(): TerminalNode | undefined;
    SEMI(): TerminalNode | undefined;
    SESSION(): TerminalNode | undefined;
    SETS(): TerminalNode | undefined;
    SIGNED(): TerminalNode | undefined;
    SNAPSHOT(): TerminalNode | undefined;
    SQLBLACKLIST(): TerminalNode | undefined;
    START(): TerminalNode | undefined;
    STREAM(): TerminalNode | undefined;
    SUM(): TerminalNode | undefined;
    STATUS(): TerminalNode | undefined;
    STOP(): TerminalNode | undefined;
    SKIP_HEADER(): TerminalNode | undefined;
    SWAP(): TerminalNode | undefined;
    STORAGE(): TerminalNode | undefined;
    STRING(): TerminalNode | undefined;
    STRUCT(): TerminalNode | undefined;
    STATS(): TerminalNode | undefined;
    SUBMIT(): TerminalNode | undefined;
    SUSPEND(): TerminalNode | undefined;
    SYNC(): TerminalNode | undefined;
    SYSTEM_TIME(): TerminalNode | undefined;
    TABLES(): TerminalNode | undefined;
    TABLET(): TerminalNode | undefined;
    TASK(): TerminalNode | undefined;
    TEMPORARY(): TerminalNode | undefined;
    TIMESTAMP(): TerminalNode | undefined;
    TIMESTAMPADD(): TerminalNode | undefined;
    TIMESTAMPDIFF(): TerminalNode | undefined;
    THAN(): TerminalNode | undefined;
    TIME(): TerminalNode | undefined;
    TIMES(): TerminalNode | undefined;
    TRANSACTION(): TerminalNode | undefined;
    TRACE(): TerminalNode | undefined;
    TRIM_SPACE(): TerminalNode | undefined;
    TRIGGERS(): TerminalNode | undefined;
    TRUNCATE(): TerminalNode | undefined;
    TYPE(): TerminalNode | undefined;
    TYPES(): TerminalNode | undefined;
    UNBOUNDED(): TerminalNode | undefined;
    UNCOMMITTED(): TerminalNode | undefined;
    UNSET(): TerminalNode | undefined;
    UNINSTALL(): TerminalNode | undefined;
    USAGE(): TerminalNode | undefined;
    USER(): TerminalNode | undefined;
    USERS(): TerminalNode | undefined;
    UNLOCK(): TerminalNode | undefined;
    VALUE(): TerminalNode | undefined;
    VARBINARY(): TerminalNode | undefined;
    VARIABLES(): TerminalNode | undefined;
    VIEW(): TerminalNode | undefined;
    VIEWS(): TerminalNode | undefined;
    VERBOSE(): TerminalNode | undefined;
    VOLUME(): TerminalNode | undefined;
    VOLUMES(): TerminalNode | undefined;
    WARNINGS(): TerminalNode | undefined;
    WEEK(): TerminalNode | undefined;
    WHITELIST(): TerminalNode | undefined;
    WORK(): TerminalNode | undefined;
    WRITE(): TerminalNode | undefined;
    WAREHOUSE(): TerminalNode | undefined;
    WAREHOUSES(): TerminalNode | undefined;
    YEAR(): TerminalNode | undefined;
    DOTDOTDOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: StarRocksParserListener): void;
    exitRule(listener: StarRocksParserListener): void;
    accept<Result>(visitor: StarRocksParserVisitor<Result>): Result;
}
