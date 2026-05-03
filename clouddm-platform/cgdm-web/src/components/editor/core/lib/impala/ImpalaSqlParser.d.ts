import { ATN } from "antlr4ts/atn/ATN";
import { FailedPredicateException } from "antlr4ts/FailedPredicateException";
import { Parser } from "antlr4ts/Parser";
import { ParserRuleContext } from "antlr4ts/ParserRuleContext";
import { RuleContext } from "antlr4ts/RuleContext";
import { TerminalNode } from "antlr4ts/tree/TerminalNode";
import { Token } from "antlr4ts/Token";
import { TokenStream } from "antlr4ts/TokenStream";
import { Vocabulary } from "antlr4ts/Vocabulary";
import { ImpalaSqlParserListener } from "./ImpalaSqlParserListener";
import { ImpalaSqlParserVisitor } from "./ImpalaSqlParserVisitor";
export declare class ImpalaSqlParser extends Parser {
    static readonly KW_ADD = 1;
    static readonly KW_ALL = 2;
    static readonly KW_ANALYTIC = 3;
    static readonly KW_ALTER = 4;
    static readonly KW_AND = 5;
    static readonly KW_ANY = 6;
    static readonly KW_ANTI = 7;
    static readonly KW_ARRAY = 8;
    static readonly KW_AS = 9;
    static readonly KW_ASC = 10;
    static readonly KW_AT = 11;
    static readonly KW_AGGREGATE = 12;
    static readonly KW_AUTHORIZATION = 13;
    static readonly KW_BERNOULLI = 14;
    static readonly KW_BETWEEN = 15;
    static readonly KW_BLOCK_SIZE = 16;
    static readonly KW_PARTITIONED = 17;
    static readonly KW_PREPARE_FN = 18;
    static readonly KW_EXTERNAL = 19;
    static readonly KW_CLOSEFN = 20;
    static readonly KW_SORT = 21;
    static readonly KW_PURGE = 22;
    static readonly KW_STORED = 23;
    static readonly KW_LOCATION = 24;
    static readonly KW_TBLPROPERTIES = 25;
    static readonly KW_BY = 26;
    static readonly KW_CASCADE = 27;
    static readonly KW_CASE = 28;
    static readonly KW_CAST = 29;
    static readonly KW_CACHED = 30;
    static readonly KW_CHANGE = 31;
    static readonly KW_COLUMN = 32;
    static readonly KW_COLUMNS = 33;
    static readonly KW_COMMENT = 34;
    static readonly KW_COMPRESSION = 35;
    static readonly KW_COMPUTE = 36;
    static readonly KW_CREATE = 37;
    static readonly KW_CROSS = 38;
    static readonly KW_CURRENT = 39;
    static readonly KW_CURRENT_DATE = 40;
    static readonly KW_CURRENT_PATH = 41;
    static readonly KW_CURRENT_TIME = 42;
    static readonly KW_CURRENT_TIMESTAMP = 43;
    static readonly KW_CURRENT_USER = 44;
    static readonly KW_DATA = 45;
    static readonly KW_DATABASE = 46;
    static readonly KW_DATABASES = 47;
    static readonly KW_DAY = 48;
    static readonly KW_DAYS = 49;
    static readonly KW_DELETE = 50;
    static readonly KW_DEFAULT = 51;
    static readonly KW_DELIMITED = 52;
    static readonly KW_DISABLE = 53;
    static readonly KW_UPDATE = 54;
    static readonly KW_DESC = 55;
    static readonly KW_DESCRIBE = 56;
    static readonly KW_DISTINCT = 57;
    static readonly KW_DROP = 58;
    static readonly KW_ELSE = 59;
    static readonly KW_ENCODING = 60;
    static readonly KW_END = 61;
    static readonly KW_ESCAPE = 62;
    static readonly KW_ESCAPED = 63;
    static readonly KW_EXCEPT = 64;
    static readonly KW_EXCLUDING = 65;
    static readonly KW_EXISTS = 66;
    static readonly KW_EXPLAIN = 67;
    static readonly KW_EXTRACT = 68;
    static readonly KW_EXTENDED = 69;
    static readonly KW_FALSE = 70;
    static readonly KW_FIELDS = 71;
    static readonly KW_FILEFORMAT = 72;
    static readonly KW_FILES = 73;
    static readonly KW_FILTER = 74;
    static readonly KW_FIRST = 75;
    static readonly KW_FINALIZE_FN = 76;
    static readonly KW_FOLLOWING = 77;
    static readonly KW_FOR = 78;
    static readonly KW_FORMAT = 79;
    static readonly KW_FORMATTED = 80;
    static readonly KW_FOREIGN = 81;
    static readonly KW_FROM = 82;
    static readonly KW_FULL = 83;
    static readonly KW_FUNCTION = 84;
    static readonly KW_FUNCTIONS = 85;
    static readonly KW_GRANT = 86;
    static readonly KW_GROUP = 87;
    static readonly KW_GROUPING = 88;
    static readonly KW_HASH = 89;
    static readonly KW_HAVING = 90;
    static readonly KW_HOUR = 91;
    static readonly KW_HOURS = 92;
    static readonly KW_IF = 93;
    static readonly KW_IN = 94;
    static readonly KW_INCLUDING = 95;
    static readonly KW_INCREMENTAL = 96;
    static readonly KW_INNER = 97;
    static readonly KW_INPATH = 98;
    static readonly KW_INSERT = 99;
    static readonly KW_INTERSECT = 100;
    static readonly KW_INTERVAL = 101;
    static readonly KW_INTERMEDIATE = 102;
    static readonly KW_INTO = 103;
    static readonly KW_INIT_FN = 104;
    static readonly KW_IREGEXP = 105;
    static readonly KW_ILIKE = 106;
    static readonly KW_INVALIDATE = 107;
    static readonly KW_IS = 108;
    static readonly KW_JOIN = 109;
    static readonly KW_KEY = 110;
    static readonly KW_KUDU = 111;
    static readonly KW_LAST = 112;
    static readonly KW_LATERAL = 113;
    static readonly KW_LEFT = 114;
    static readonly KW_LIKE = 115;
    static readonly KW_LIMIT = 116;
    static readonly KW_LINES = 117;
    static readonly KW_LOAD = 118;
    static readonly KW_LOCALTIME = 119;
    static readonly KW_LOCALTIMESTAMP = 120;
    static readonly KW_METADATA = 121;
    static readonly KW_MAP = 122;
    static readonly KW_MINUTE = 123;
    static readonly KW_MINUTES = 124;
    static readonly KW_MONTH = 125;
    static readonly KW_MONTHS = 126;
    static readonly KW_MERGE_FN = 127;
    static readonly KW_NFC = 128;
    static readonly KW_NFD = 129;
    static readonly KW_NFKC = 130;
    static readonly KW_NFKD = 131;
    static readonly KW_NORMALIZE = 132;
    static readonly KW_NOT = 133;
    static readonly KW_NULL = 134;
    static readonly KW_NULLS = 135;
    static readonly KW_OFFSET = 136;
    static readonly KW_ON = 137;
    static readonly KW_OPTION = 138;
    static readonly KW_OR = 139;
    static readonly KW_ORDER = 140;
    static readonly KW_ORDINALITY = 141;
    static readonly KW_OUTER = 142;
    static readonly KW_OWNER = 143;
    static readonly KW_OVER = 144;
    static readonly KW_OVERWRITE = 145;
    static readonly KW_PARTITION = 146;
    static readonly KW_PARTITIONS = 147;
    static readonly KW_PARQUET = 148;
    static readonly KW_POSITION = 149;
    static readonly KW_PRECEDING = 150;
    static readonly KW_PRIMARY = 151;
    static readonly KW_REPLICATION = 152;
    static readonly KW_PRIVILEGES = 153;
    static readonly KW_PROPERTIES = 154;
    static readonly KW_RANGE = 155;
    static readonly KW_RECOVER = 156;
    static readonly KW_RENAME = 157;
    static readonly KW_REPEATABLE = 158;
    static readonly KW_REPLACE = 159;
    static readonly KW_RESTRICT = 160;
    static readonly KW_RETURNS = 161;
    static readonly KW_REVOKE = 162;
    static readonly KW_REFRESH = 163;
    static readonly KW_REGEXP = 164;
    static readonly KW_RLIKE = 165;
    static readonly KW_RIGHT = 166;
    static readonly KW_ROLE = 167;
    static readonly KW_ROLES = 168;
    static readonly KW_ROW = 169;
    static readonly KW_ROWS = 170;
    static readonly KW_SCHEMA = 171;
    static readonly KW_SCHEMAS = 172;
    static readonly KW_SECOND = 173;
    static readonly KW_SECONDS = 174;
    static readonly KW_SELECT = 175;
    static readonly KW_SERDEPROPERTIES = 176;
    static readonly KW_SET = 177;
    static readonly KW_SEMI = 178;
    static readonly KW_SERVER = 179;
    static readonly KW_SHOW = 180;
    static readonly KW_SHUTDOWN = 181;
    static readonly KW_SOME = 182;
    static readonly KW_STATS = 183;
    static readonly KW_STRUCT = 184;
    static readonly KW_STRAIGHT_JOIN = 185;
    static readonly KW_SUBSTRING = 186;
    static readonly KW_SYSTEM = 187;
    static readonly KW_SYMBOL = 188;
    static readonly KW_SERIALIZE_FN = 189;
    static readonly KW_TABLE = 190;
    static readonly KW_TABLES = 191;
    static readonly KW_TABLESAMPLE = 192;
    static readonly KW_TERMINATED = 193;
    static readonly KW_THEN = 194;
    static readonly KW_TO = 195;
    static readonly KW_TRUE = 196;
    static readonly KW_TRY_CAST = 197;
    static readonly KW_TRUNCATE = 198;
    static readonly KW_UNCACHED = 199;
    static readonly KW_UESCAPE = 200;
    static readonly KW_UNBOUNDED = 201;
    static readonly KW_UNION = 202;
    static readonly KW_UNNEST = 203;
    static readonly KW_UNSET = 204;
    static readonly KW_USE = 205;
    static readonly KW_USER = 206;
    static readonly KW_USING = 207;
    static readonly KW_UPDATE_FN = 208;
    static readonly KW_UPSERT = 209;
    static readonly KW_UNKNOWN = 210;
    static readonly KW_URI = 211;
    static readonly KW_VALUE = 212;
    static readonly KW_VALUES = 213;
    static readonly KW_VIEW = 214;
    static readonly KW_VIEWS = 215;
    static readonly KW_WHEN = 216;
    static readonly KW_WHERE = 217;
    static readonly KW_WITH = 218;
    static readonly KW_YEAR = 219;
    static readonly KW_YEARS = 220;
    static readonly KW_TEXTFILE = 221;
    static readonly KW_ORC = 222;
    static readonly KW_AVRO = 223;
    static readonly KW_SEQUENCEFILE = 224;
    static readonly KW_RCFILE = 225;
    static readonly KW_REFERENCES = 226;
    static readonly KW_NOVALIDATE = 227;
    static readonly KW_RELY = 228;
    static readonly STATS_NUMDVS = 229;
    static readonly STATS_NUMNULLS = 230;
    static readonly STATS_AVGSIZE = 231;
    static readonly STATS_MAXSIZE = 232;
    static readonly EQ = 233;
    static readonly NEQ = 234;
    static readonly LT = 235;
    static readonly LTE = 236;
    static readonly GT = 237;
    static readonly GTE = 238;
    static readonly PLUS = 239;
    static readonly MINUS = 240;
    static readonly ASTERISK = 241;
    static readonly SLASH = 242;
    static readonly PERCENT = 243;
    static readonly CONCAT = 244;
    static readonly DOT = 245;
    static readonly SEMICOLON = 246;
    static readonly COMMA = 247;
    static readonly COLON = 248;
    static readonly LPAREN = 249;
    static readonly RPAREN = 250;
    static readonly LSQUARE = 251;
    static readonly RSQUARE = 252;
    static readonly LCURLY = 253;
    static readonly RCURLY = 254;
    static readonly BITWISEOR = 255;
    static readonly QUESTION = 256;
    static readonly RIGHT_ARROW = 257;
    static readonly STRING = 258;
    static readonly UNICODE_STRING = 259;
    static readonly BINARY_LITERAL = 260;
    static readonly INTEGER_VALUE = 261;
    static readonly DECIMAL_VALUE = 262;
    static readonly DOUBLE_VALUE = 263;
    static readonly IDENTIFIER = 264;
    static readonly DIGIT_IDENTIFIER = 265;
    static readonly QUOTED_IDENTIFIER = 266;
    static readonly BACKQUOTED_IDENTIFIER = 267;
    static readonly TIME_WITH_TIME_ZONE = 268;
    static readonly TIMESTAMP_WITH_TIME_ZONE = 269;
    static readonly DOUBLE_PRECISION = 270;
    static readonly SIMPLE_COMMENT = 271;
    static readonly BRACKETED_COMMENT = 272;
    static readonly WS = 273;
    static readonly RULE_program = 0;
    static readonly RULE_singleStatement = 1;
    static readonly RULE_sqlStatement = 2;
    static readonly RULE_useStatement = 3;
    static readonly RULE_createStatement = 4;
    static readonly RULE_createTableSelect = 5;
    static readonly RULE_createTableLike = 6;
    static readonly RULE_createKuduTableAsSelect = 7;
    static readonly RULE_createView = 8;
    static readonly RULE_createSchema = 9;
    static readonly RULE_createRole = 10;
    static readonly RULE_createAggregateFunction = 11;
    static readonly RULE_createFunction = 12;
    static readonly RULE_alterStatement = 13;
    static readonly RULE_alterDatabase = 14;
    static readonly RULE_alterStatsKey = 15;
    static readonly RULE_alterPartitionCache = 16;
    static readonly RULE_changeColumnDefine = 17;
    static readonly RULE_alterDropSingleColumn = 18;
    static readonly RULE_alterTableOwner = 19;
    static readonly RULE_replaceOrAddColumns = 20;
    static readonly RULE_addSingleColumn = 21;
    static readonly RULE_alterTableNonKuduOrKuduOnly = 22;
    static readonly RULE_addPartitionByRangeOrValue = 23;
    static readonly RULE_alterFormat = 24;
    static readonly RULE_recoverPartitions = 25;
    static readonly RULE_dropPartitionByRangeOrValue = 26;
    static readonly RULE_alterView = 27;
    static readonly RULE_renameView = 28;
    static readonly RULE_alterViewOwner = 29;
    static readonly RULE_renameTable = 30;
    static readonly RULE_alterUnSetOrSetViewTblproperties = 31;
    static readonly RULE_truncateTableStatement = 32;
    static readonly RULE_describeStatement = 33;
    static readonly RULE_computeStatement = 34;
    static readonly RULE_computeStats = 35;
    static readonly RULE_computeIncrementalStats = 36;
    static readonly RULE_dropStatement = 37;
    static readonly RULE_dropSchema = 38;
    static readonly RULE_dropView = 39;
    static readonly RULE_dropTable = 40;
    static readonly RULE_dropIncrementalStats = 41;
    static readonly RULE_dropFunction = 42;
    static readonly RULE_dropRole = 43;
    static readonly RULE_grantStatement = 44;
    static readonly RULE_grantRole = 45;
    static readonly RULE_grant = 46;
    static readonly RULE_revokeStatement = 47;
    static readonly RULE_revokeRole = 48;
    static readonly RULE_revoke = 49;
    static readonly RULE_insertStatement = 50;
    static readonly RULE_deleteStatement = 51;
    static readonly RULE_delete = 52;
    static readonly RULE_deleteTableRef = 53;
    static readonly RULE_updateStatement = 54;
    static readonly RULE_upsertStatement = 55;
    static readonly RULE_showStatement = 56;
    static readonly RULE_showSchemas = 57;
    static readonly RULE_showTables = 58;
    static readonly RULE_showFunctions = 59;
    static readonly RULE_showCreateTable = 60;
    static readonly RULE_showCreateView = 61;
    static readonly RULE_showTableStats = 62;
    static readonly RULE_showColumnStats = 63;
    static readonly RULE_showPartitions = 64;
    static readonly RULE_showFiles = 65;
    static readonly RULE_showRoles = 66;
    static readonly RULE_showRoleGrant = 67;
    static readonly RULE_showGrants = 68;
    static readonly RULE_showDatabaseGrant = 69;
    static readonly RULE_showTableGrant = 70;
    static readonly RULE_showColumnGrant = 71;
    static readonly RULE_addCommentStatement = 72;
    static readonly RULE_addDatabaseComments = 73;
    static readonly RULE_addTableComments = 74;
    static readonly RULE_addColumnComments = 75;
    static readonly RULE_explainStatement = 76;
    static readonly RULE_setStatement = 77;
    static readonly RULE_shutdownStatement = 78;
    static readonly RULE_invalidateMetaStatement = 79;
    static readonly RULE_loadDataStatement = 80;
    static readonly RULE_refreshStatement = 81;
    static readonly RULE_refreshMeta = 82;
    static readonly RULE_refreshAuth = 83;
    static readonly RULE_refreshFunction = 84;
    static readonly RULE_ifExists = 85;
    static readonly RULE_ifNotExists = 86;
    static readonly RULE_tableNameCreate = 87;
    static readonly RULE_databaseNameCreate = 88;
    static readonly RULE_viewNameCreate = 89;
    static readonly RULE_functionNameCreate = 90;
    static readonly RULE_columnNamePathCreate = 91;
    static readonly RULE_databaseNamePath = 92;
    static readonly RULE_tableNamePath = 93;
    static readonly RULE_viewNamePath = 94;
    static readonly RULE_functionNamePath = 95;
    static readonly RULE_columnNamePath = 96;
    static readonly RULE_tableOrViewPath = 97;
    static readonly RULE_createCommonItem = 98;
    static readonly RULE_assignmentList = 99;
    static readonly RULE_assignmentItem = 100;
    static readonly RULE_viewColumns = 101;
    static readonly RULE_queryStatement = 102;
    static readonly RULE_with = 103;
    static readonly RULE_constraintSpecification = 104;
    static readonly RULE_foreignKeySpecification = 105;
    static readonly RULE_columnDefinition = 106;
    static readonly RULE_kuduTableElement = 107;
    static readonly RULE_kuduColumnDefinition = 108;
    static readonly RULE_columnSpecWithKudu = 109;
    static readonly RULE_createColumnSpecWithKudu = 110;
    static readonly RULE_kuduAttributes = 111;
    static readonly RULE_kuduStorageAttr = 112;
    static readonly RULE_statsKey = 113;
    static readonly RULE_fileFormat = 114;
    static readonly RULE_kuduPartitionClause = 115;
    static readonly RULE_hashClause = 116;
    static readonly RULE_rangeClause = 117;
    static readonly RULE_kuduPartitionSpec = 118;
    static readonly RULE_cacheSpec = 119;
    static readonly RULE_rangeOperator = 120;
    static readonly RULE_partitionCol = 121;
    static readonly RULE_likeClause = 122;
    static readonly RULE_properties = 123;
    static readonly RULE_partitionedBy = 124;
    static readonly RULE_sortedBy = 125;
    static readonly RULE_rowFormat = 126;
    static readonly RULE_property = 127;
    static readonly RULE_queryNoWith = 128;
    static readonly RULE_queryTerm = 129;
    static readonly RULE_queryPrimary = 130;
    static readonly RULE_sortItem = 131;
    static readonly RULE_querySpecification = 132;
    static readonly RULE_groupBy = 133;
    static readonly RULE_groupingElement = 134;
    static readonly RULE_groupingSet = 135;
    static readonly RULE_namedQuery = 136;
    static readonly RULE_setQuantifier = 137;
    static readonly RULE_selectItem = 138;
    static readonly RULE_relation = 139;
    static readonly RULE_joinType = 140;
    static readonly RULE_joinCriteria = 141;
    static readonly RULE_sampledRelation = 142;
    static readonly RULE_sampleType = 143;
    static readonly RULE_aliasedRelation = 144;
    static readonly RULE_columnAliases = 145;
    static readonly RULE_createColumnAliases = 146;
    static readonly RULE_relationPrimary = 147;
    static readonly RULE_subQueryRelation = 148;
    static readonly RULE_unnest = 149;
    static readonly RULE_parenthesizedRelation = 150;
    static readonly RULE_columnItem = 151;
    static readonly RULE_expression = 152;
    static readonly RULE_booleanExpression = 153;
    static readonly RULE_predicate = 154;
    static readonly RULE_valueExpression = 155;
    static readonly RULE_primaryExpression = 156;
    static readonly RULE_stringLiteral = 157;
    static readonly RULE_comparisonOperator = 158;
    static readonly RULE_comparisonQuantifier = 159;
    static readonly RULE_booleanValue = 160;
    static readonly RULE_interval = 161;
    static readonly RULE_intervalField = 162;
    static readonly RULE_normalForm = 163;
    static readonly RULE_type = 164;
    static readonly RULE_typeParameter = 165;
    static readonly RULE_baseType = 166;
    static readonly RULE_whenClause = 167;
    static readonly RULE_filter = 168;
    static readonly RULE_over = 169;
    static readonly RULE_windowFrame = 170;
    static readonly RULE_frameBound = 171;
    static readonly RULE_pathElement = 172;
    static readonly RULE_pathSpecification = 173;
    static readonly RULE_privilege = 174;
    static readonly RULE_objectType = 175;
    static readonly RULE_qualifiedName = 176;
    static readonly RULE_principal = 177;
    static readonly RULE_identifier = 178;
    static readonly RULE_number = 179;
    static readonly RULE_reservedKeywordsUsedAsFuncName = 180;
    static readonly RULE_nonReserved = 181;
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
    useStatement(): UseStatementContext;
    createStatement(): CreateStatementContext;
    createTableSelect(): CreateTableSelectContext;
    createTableLike(): CreateTableLikeContext;
    createKuduTableAsSelect(): CreateKuduTableAsSelectContext;
    createView(): CreateViewContext;
    createSchema(): CreateSchemaContext;
    createRole(): CreateRoleContext;
    createAggregateFunction(): CreateAggregateFunctionContext;
    createFunction(): CreateFunctionContext;
    alterStatement(): AlterStatementContext;
    alterDatabase(): AlterDatabaseContext;
    alterStatsKey(): AlterStatsKeyContext;
    alterPartitionCache(): AlterPartitionCacheContext;
    changeColumnDefine(): ChangeColumnDefineContext;
    alterDropSingleColumn(): AlterDropSingleColumnContext;
    alterTableOwner(): AlterTableOwnerContext;
    replaceOrAddColumns(): ReplaceOrAddColumnsContext;
    addSingleColumn(): AddSingleColumnContext;
    alterTableNonKuduOrKuduOnly(): AlterTableNonKuduOrKuduOnlyContext;
    addPartitionByRangeOrValue(): AddPartitionByRangeOrValueContext;
    alterFormat(): AlterFormatContext;
    recoverPartitions(): RecoverPartitionsContext;
    dropPartitionByRangeOrValue(): DropPartitionByRangeOrValueContext;
    alterView(): AlterViewContext;
    renameView(): RenameViewContext;
    alterViewOwner(): AlterViewOwnerContext;
    renameTable(): RenameTableContext;
    alterUnSetOrSetViewTblproperties(): AlterUnSetOrSetViewTblpropertiesContext;
    truncateTableStatement(): TruncateTableStatementContext;
    describeStatement(): DescribeStatementContext;
    computeStatement(): ComputeStatementContext;
    computeStats(): ComputeStatsContext;
    computeIncrementalStats(): ComputeIncrementalStatsContext;
    dropStatement(): DropStatementContext;
    dropSchema(): DropSchemaContext;
    dropView(): DropViewContext;
    dropTable(): DropTableContext;
    dropIncrementalStats(): DropIncrementalStatsContext;
    dropFunction(): DropFunctionContext;
    dropRole(): DropRoleContext;
    grantStatement(): GrantStatementContext;
    grantRole(): GrantRoleContext;
    grant(): GrantContext;
    revokeStatement(): RevokeStatementContext;
    revokeRole(): RevokeRoleContext;
    revoke(): RevokeContext;
    insertStatement(): InsertStatementContext;
    deleteStatement(): DeleteStatementContext;
    delete(): DeleteContext;
    deleteTableRef(): DeleteTableRefContext;
    updateStatement(): UpdateStatementContext;
    upsertStatement(): UpsertStatementContext;
    showStatement(): ShowStatementContext;
    showSchemas(): ShowSchemasContext;
    showTables(): ShowTablesContext;
    showFunctions(): ShowFunctionsContext;
    showCreateTable(): ShowCreateTableContext;
    showCreateView(): ShowCreateViewContext;
    showTableStats(): ShowTableStatsContext;
    showColumnStats(): ShowColumnStatsContext;
    showPartitions(): ShowPartitionsContext;
    showFiles(): ShowFilesContext;
    showRoles(): ShowRolesContext;
    showRoleGrant(): ShowRoleGrantContext;
    showGrants(): ShowGrantsContext;
    showDatabaseGrant(): ShowDatabaseGrantContext;
    showTableGrant(): ShowTableGrantContext;
    showColumnGrant(): ShowColumnGrantContext;
    addCommentStatement(): AddCommentStatementContext;
    addDatabaseComments(): AddDatabaseCommentsContext;
    addTableComments(): AddTableCommentsContext;
    addColumnComments(): AddColumnCommentsContext;
    explainStatement(): ExplainStatementContext;
    setStatement(): SetStatementContext;
    shutdownStatement(): ShutdownStatementContext;
    invalidateMetaStatement(): InvalidateMetaStatementContext;
    loadDataStatement(): LoadDataStatementContext;
    refreshStatement(): RefreshStatementContext;
    refreshMeta(): RefreshMetaContext;
    refreshAuth(): RefreshAuthContext;
    refreshFunction(): RefreshFunctionContext;
    ifExists(): IfExistsContext;
    ifNotExists(): IfNotExistsContext;
    tableNameCreate(): TableNameCreateContext;
    databaseNameCreate(): DatabaseNameCreateContext;
    viewNameCreate(): ViewNameCreateContext;
    functionNameCreate(): FunctionNameCreateContext;
    columnNamePathCreate(): ColumnNamePathCreateContext;
    databaseNamePath(): DatabaseNamePathContext;
    tableNamePath(): TableNamePathContext;
    viewNamePath(): ViewNamePathContext;
    functionNamePath(): FunctionNamePathContext;
    columnNamePath(): ColumnNamePathContext;
    tableOrViewPath(): TableOrViewPathContext;
    createCommonItem(): CreateCommonItemContext;
    assignmentList(): AssignmentListContext;
    assignmentItem(): AssignmentItemContext;
    viewColumns(): ViewColumnsContext;
    queryStatement(): QueryStatementContext;
    with(): WithContext;
    constraintSpecification(): ConstraintSpecificationContext;
    foreignKeySpecification(): ForeignKeySpecificationContext;
    columnDefinition(): ColumnDefinitionContext;
    kuduTableElement(): KuduTableElementContext;
    kuduColumnDefinition(): KuduColumnDefinitionContext;
    columnSpecWithKudu(): ColumnSpecWithKuduContext;
    createColumnSpecWithKudu(): CreateColumnSpecWithKuduContext;
    kuduAttributes(): KuduAttributesContext;
    kuduStorageAttr(): KuduStorageAttrContext;
    statsKey(): StatsKeyContext;
    fileFormat(): FileFormatContext;
    kuduPartitionClause(): KuduPartitionClauseContext;
    hashClause(): HashClauseContext;
    rangeClause(): RangeClauseContext;
    kuduPartitionSpec(): KuduPartitionSpecContext;
    cacheSpec(): CacheSpecContext;
    rangeOperator(): RangeOperatorContext;
    partitionCol(): PartitionColContext;
    likeClause(): LikeClauseContext;
    properties(): PropertiesContext;
    partitionedBy(): PartitionedByContext;
    sortedBy(): SortedByContext;
    rowFormat(): RowFormatContext;
    property(): PropertyContext;
    queryNoWith(): QueryNoWithContext;
    queryTerm(): QueryTermContext;
    queryTerm(_p: number): QueryTermContext;
    queryPrimary(): QueryPrimaryContext;
    sortItem(): SortItemContext;
    querySpecification(): QuerySpecificationContext;
    groupBy(): GroupByContext;
    groupingElement(): GroupingElementContext;
    groupingSet(): GroupingSetContext;
    namedQuery(): NamedQueryContext;
    setQuantifier(): SetQuantifierContext;
    selectItem(): SelectItemContext;
    relation(): RelationContext;
    relation(_p: number): RelationContext;
    joinType(): JoinTypeContext;
    joinCriteria(): JoinCriteriaContext;
    sampledRelation(): SampledRelationContext;
    sampleType(): SampleTypeContext;
    aliasedRelation(): AliasedRelationContext;
    columnAliases(): ColumnAliasesContext;
    createColumnAliases(): CreateColumnAliasesContext;
    relationPrimary(): RelationPrimaryContext;
    subQueryRelation(): SubQueryRelationContext;
    unnest(): UnnestContext;
    parenthesizedRelation(): ParenthesizedRelationContext;
    columnItem(): ColumnItemContext;
    expression(): ExpressionContext;
    booleanExpression(): BooleanExpressionContext;
    booleanExpression(_p: number): BooleanExpressionContext;
    predicate(value: ParserRuleContext): PredicateContext;
    valueExpression(): ValueExpressionContext;
    valueExpression(_p: number): ValueExpressionContext;
    primaryExpression(): PrimaryExpressionContext;
    primaryExpression(_p: number): PrimaryExpressionContext;
    stringLiteral(): StringLiteralContext;
    comparisonOperator(): ComparisonOperatorContext;
    comparisonQuantifier(): ComparisonQuantifierContext;
    booleanValue(): BooleanValueContext;
    interval(): IntervalContext;
    intervalField(): IntervalFieldContext;
    normalForm(): NormalFormContext;
    type(): TypeContext;
    type(_p: number): TypeContext;
    typeParameter(): TypeParameterContext;
    baseType(): BaseTypeContext;
    whenClause(): WhenClauseContext;
    filter(): FilterContext;
    over(): OverContext;
    windowFrame(): WindowFrameContext;
    frameBound(): FrameBoundContext;
    pathElement(): PathElementContext;
    pathSpecification(): PathSpecificationContext;
    privilege(): PrivilegeContext;
    objectType(): ObjectTypeContext;
    qualifiedName(): QualifiedNameContext;
    principal(): PrincipalContext;
    identifier(): IdentifierContext;
    number(): NumberContext;
    reservedKeywordsUsedAsFuncName(): ReservedKeywordsUsedAsFuncNameContext;
    nonReserved(): NonReservedContext;
    sempred(_localctx: RuleContext, ruleIndex: number, predIndex: number): boolean;
    private queryTerm_sempred;
    private relation_sempred;
    private booleanExpression_sempred;
    private valueExpression_sempred;
    private primaryExpression_sempred;
    private type_sempred;
    private static readonly _serializedATNSegments;
    private static readonly _serializedATNSegment0;
    private static readonly _serializedATNSegment1;
    private static readonly _serializedATNSegment2;
    private static readonly _serializedATNSegment3;
    private static readonly _serializedATNSegment4;
    private static readonly _serializedATNSegment5;
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
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SingleStatementContext extends ParserRuleContext {
    sqlStatement(): SqlStatementContext;
    SEMICOLON(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SqlStatementContext extends ParserRuleContext {
    queryStatement(): QueryStatementContext | undefined;
    useStatement(): UseStatementContext | undefined;
    createStatement(): CreateStatementContext | undefined;
    alterStatement(): AlterStatementContext | undefined;
    truncateTableStatement(): TruncateTableStatementContext | undefined;
    describeStatement(): DescribeStatementContext | undefined;
    computeStatement(): ComputeStatementContext | undefined;
    dropStatement(): DropStatementContext | undefined;
    grantStatement(): GrantStatementContext | undefined;
    revokeStatement(): RevokeStatementContext | undefined;
    insertStatement(): InsertStatementContext | undefined;
    deleteStatement(): DeleteStatementContext | undefined;
    updateStatement(): UpdateStatementContext | undefined;
    upsertStatement(): UpsertStatementContext | undefined;
    showStatement(): ShowStatementContext | undefined;
    addCommentStatement(): AddCommentStatementContext | undefined;
    explainStatement(): ExplainStatementContext | undefined;
    setStatement(): SetStatementContext | undefined;
    shutdownStatement(): ShutdownStatementContext | undefined;
    invalidateMetaStatement(): InvalidateMetaStatementContext | undefined;
    loadDataStatement(): LoadDataStatementContext | undefined;
    refreshStatement(): RefreshStatementContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class UseStatementContext extends ParserRuleContext {
    KW_USE(): TerminalNode;
    databaseNamePath(): DatabaseNamePathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CreateStatementContext extends ParserRuleContext {
    createSchema(): CreateSchemaContext | undefined;
    createRole(): CreateRoleContext | undefined;
    createAggregateFunction(): CreateAggregateFunctionContext | undefined;
    createFunction(): CreateFunctionContext | undefined;
    createView(): CreateViewContext | undefined;
    createKuduTableAsSelect(): CreateKuduTableAsSelectContext | undefined;
    createTableLike(): CreateTableLikeContext | undefined;
    createTableSelect(): CreateTableSelectContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CreateTableSelectContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    createCommonItem(): CreateCommonItemContext;
    KW_EXTERNAL(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    LPAREN(): TerminalNode | undefined;
    columnDefinition(): ColumnDefinitionContext[];
    columnDefinition(i: number): ColumnDefinitionContext;
    RPAREN(): TerminalNode | undefined;
    KW_PARTITIONED(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    queryStatement(): QueryStatementContext | undefined;
    partitionedBy(): PartitionedByContext | undefined;
    createColumnAliases(): CreateColumnAliasesContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constraintSpecification(): ConstraintSpecificationContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CreateTableLikeContext extends ParserRuleContext {
    _parquet: StringLiteralContext;
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    KW_LIKE(): TerminalNode;
    createCommonItem(): CreateCommonItemContext;
    tableNamePath(): TableNamePathContext | undefined;
    KW_PARQUET(): TerminalNode | undefined;
    KW_EXTERNAL(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    KW_PARTITIONED(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    partitionedBy(): PartitionedByContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CreateKuduTableAsSelectContext extends ParserRuleContext {
    _tblProp: PropertiesContext;
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNameCreate(): TableNameCreateContext;
    KW_STORED(): TerminalNode;
    KW_AS(): TerminalNode[];
    KW_AS(i: number): TerminalNode;
    KW_KUDU(): TerminalNode;
    KW_EXTERNAL(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    LPAREN(): TerminalNode | undefined;
    kuduTableElement(): KuduTableElementContext[];
    kuduTableElement(i: number): KuduTableElementContext;
    RPAREN(): TerminalNode | undefined;
    KW_PRIMARY(): TerminalNode[];
    KW_PRIMARY(i: number): TerminalNode;
    KW_KEY(): TerminalNode[];
    KW_KEY(i: number): TerminalNode;
    KW_PARTITION(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    kuduPartitionClause(): KuduPartitionClauseContext | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    KW_TBLPROPERTIES(): TerminalNode | undefined;
    queryStatement(): QueryStatementContext | undefined;
    properties(): PropertiesContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    columnAliases(): ColumnAliasesContext[];
    columnAliases(i: number): ColumnAliasesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CreateViewContext extends ParserRuleContext {
    _tblProp: PropertiesContext;
    KW_CREATE(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewNameCreate(): ViewNameCreateContext;
    KW_AS(): TerminalNode;
    queryStatement(): QueryStatementContext;
    ifNotExists(): IfNotExistsContext | undefined;
    viewColumns(): ViewColumnsContext | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    KW_TBLPROPERTIES(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CreateSchemaContext extends ParserRuleContext {
    _comment: StringLiteralContext;
    _location: StringLiteralContext;
    KW_CREATE(): TerminalNode;
    databaseNameCreate(): DatabaseNameCreateContext;
    KW_SCHEMA(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    KW_LOCATION(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext[];
    stringLiteral(i: number): StringLiteralContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CreateRoleContext extends ParserRuleContext {
    _name: IdentifierContext;
    KW_CREATE(): TerminalNode;
    KW_ROLE(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CreateAggregateFunctionContext extends ParserRuleContext {
    KW_CREATE(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    functionNameCreate(): FunctionNameCreateContext;
    KW_RETURNS(): TerminalNode;
    type(): TypeContext[];
    type(i: number): TypeContext;
    KW_LOCATION(): TerminalNode;
    STRING(): TerminalNode[];
    STRING(i: number): TerminalNode;
    KW_UPDATE_FN(): TerminalNode;
    EQ(): TerminalNode[];
    EQ(i: number): TerminalNode;
    KW_MERGE_FN(): TerminalNode;
    KW_AGGREGATE(): TerminalNode | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    KW_INTERMEDIATE(): TerminalNode | undefined;
    KW_INIT_FN(): TerminalNode | undefined;
    KW_PREPARE_FN(): TerminalNode | undefined;
    KW_CLOSEFN(): TerminalNode | undefined;
    KW_SERIALIZE_FN(): TerminalNode | undefined;
    KW_FINALIZE_FN(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CreateFunctionContext extends ParserRuleContext {
    _symbol: StringLiteralContext;
    KW_CREATE(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    functionNameCreate(): FunctionNameCreateContext;
    KW_LOCATION(): TerminalNode;
    STRING(): TerminalNode;
    KW_SYMBOL(): TerminalNode;
    EQ(): TerminalNode;
    stringLiteral(): StringLiteralContext;
    ifNotExists(): IfNotExistsContext | undefined;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    KW_RETURNS(): TerminalNode | undefined;
    type(): TypeContext[];
    type(i: number): TypeContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AlterStatementContext extends ParserRuleContext {
    alterDatabase(): AlterDatabaseContext | undefined;
    alterUnSetOrSetViewTblproperties(): AlterUnSetOrSetViewTblpropertiesContext | undefined;
    renameTable(): RenameTableContext | undefined;
    alterViewOwner(): AlterViewOwnerContext | undefined;
    alterView(): AlterViewContext | undefined;
    renameView(): RenameViewContext | undefined;
    dropPartitionByRangeOrValue(): DropPartitionByRangeOrValueContext | undefined;
    alterFormat(): AlterFormatContext | undefined;
    recoverPartitions(): RecoverPartitionsContext | undefined;
    addPartitionByRangeOrValue(): AddPartitionByRangeOrValueContext | undefined;
    alterTableNonKuduOrKuduOnly(): AlterTableNonKuduOrKuduOnlyContext | undefined;
    addSingleColumn(): AddSingleColumnContext | undefined;
    replaceOrAddColumns(): ReplaceOrAddColumnsContext | undefined;
    changeColumnDefine(): ChangeColumnDefineContext | undefined;
    alterStatsKey(): AlterStatsKeyContext | undefined;
    alterPartitionCache(): AlterPartitionCacheContext | undefined;
    alterDropSingleColumn(): AlterDropSingleColumnContext | undefined;
    alterTableOwner(): AlterTableOwnerContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AlterDatabaseContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_DATABASE(): TerminalNode;
    databaseNamePath(): DatabaseNamePathContext;
    KW_SET(): TerminalNode;
    KW_OWNER(): TerminalNode;
    identifier(): IdentifierContext;
    KW_USER(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AlterStatsKeyContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_SET(): TerminalNode;
    KW_COLUMN(): TerminalNode;
    KW_STATS(): TerminalNode;
    columnNamePath(): ColumnNamePathContext;
    LPAREN(): TerminalNode;
    statsKey(): StatsKeyContext[];
    statsKey(i: number): StatsKeyContext;
    EQ(): TerminalNode[];
    EQ(i: number): TerminalNode;
    stringLiteral(): StringLiteralContext[];
    stringLiteral(i: number): StringLiteralContext;
    RPAREN(): TerminalNode;
    COMMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AlterPartitionCacheContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_SET(): TerminalNode;
    KW_UNCACHED(): TerminalNode | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    KW_CACHED(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_REPLICATION(): TerminalNode | undefined;
    EQ(): TerminalNode | undefined;
    number(): NumberContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ChangeColumnDefineContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_CHANGE(): TerminalNode;
    KW_COLUMN(): TerminalNode;
    columnSpecWithKudu(): ColumnSpecWithKuduContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AlterDropSingleColumnContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_DROP(): TerminalNode;
    columnNamePath(): ColumnNamePathContext;
    KW_COLUMN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AlterTableOwnerContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_SET(): TerminalNode;
    KW_OWNER(): TerminalNode;
    identifier(): IdentifierContext;
    KW_USER(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ReplaceOrAddColumnsContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_COLUMNS(): TerminalNode;
    LPAREN(): TerminalNode;
    columnSpecWithKudu(): ColumnSpecWithKuduContext[];
    columnSpecWithKudu(i: number): ColumnSpecWithKuduContext;
    RPAREN(): TerminalNode;
    KW_REPLACE(): TerminalNode | undefined;
    KW_ADD(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    ifNotExists(): IfNotExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AddSingleColumnContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_ADD(): TerminalNode;
    KW_COLUMN(): TerminalNode;
    createColumnSpecWithKudu(): CreateColumnSpecWithKuduContext;
    ifNotExists(): IfNotExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AlterTableNonKuduOrKuduOnlyContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode[];
    KW_ALTER(i: number): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    columnNamePath(): ColumnNamePathContext;
    KW_SET(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_COLUMN(): TerminalNode | undefined;
    kuduStorageAttr(): KuduStorageAttrContext | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AddPartitionByRangeOrValueContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_ADD(): TerminalNode;
    KW_PARTITION(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    KW_RANGE(): TerminalNode | undefined;
    kuduPartitionSpec(): KuduPartitionSpecContext | undefined;
    ifNotExists(): IfNotExistsContext | undefined;
    KW_LOCATION(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    cacheSpec(): CacheSpecContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AlterFormatContext extends ParserRuleContext {
    _tblProp: PropertiesContext;
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_SET(): TerminalNode;
    KW_PARTITION(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    KW_FILEFORMAT(): TerminalNode | undefined;
    fileFormat(): FileFormatContext | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_FORMAT(): TerminalNode | undefined;
    rowFormat(): RowFormatContext | undefined;
    KW_LOCATION(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    KW_TBLPROPERTIES(): TerminalNode | undefined;
    KW_SERDEPROPERTIES(): TerminalNode | undefined;
    properties(): PropertiesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RecoverPartitionsContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_RECOVER(): TerminalNode;
    KW_PARTITIONS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DropPartitionByRangeOrValueContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_DROP(): TerminalNode;
    KW_PARTITION(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    KW_RANGE(): TerminalNode | undefined;
    kuduPartitionSpec(): KuduPartitionSpecContext | undefined;
    ifExists(): IfExistsContext | undefined;
    KW_PURGE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AlterViewContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewNamePath(): ViewNamePathContext;
    KW_AS(): TerminalNode;
    queryStatement(): QueryStatementContext;
    viewColumns(): ViewColumnsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RenameViewContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewNamePath(): ViewNamePathContext[];
    viewNamePath(i: number): ViewNamePathContext;
    KW_RENAME(): TerminalNode;
    KW_TO(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AlterViewOwnerContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewNamePath(): ViewNamePathContext;
    KW_SET(): TerminalNode;
    KW_OWNER(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    KW_USER(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RenameTableContext extends ParserRuleContext {
    KW_ALTER(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext[];
    tableNamePath(i: number): TableNamePathContext;
    KW_RENAME(): TerminalNode;
    KW_TO(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AlterUnSetOrSetViewTblpropertiesContext extends ParserRuleContext {
    _tblProp: PropertiesContext;
    KW_ALTER(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewNamePath(): ViewNamePathContext;
    KW_TBLPROPERTIES(): TerminalNode;
    KW_UNSET(): TerminalNode | undefined;
    KW_SET(): TerminalNode | undefined;
    properties(): PropertiesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class TruncateTableStatementContext extends ParserRuleContext {
    KW_TRUNCATE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_TABLE(): TerminalNode | undefined;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DescribeStatementContext extends ParserRuleContext {
    KW_DESCRIBE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    KW_DATABASE(): TerminalNode | undefined;
    KW_FORMATTED(): TerminalNode | undefined;
    KW_EXTENDED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ComputeStatementContext extends ParserRuleContext {
    computeStats(): ComputeStatsContext | undefined;
    computeIncrementalStats(): ComputeIncrementalStatsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ComputeStatsContext extends ParserRuleContext {
    KW_COMPUTE(): TerminalNode;
    KW_STATS(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    columnAliases(): ColumnAliasesContext | undefined;
    KW_TABLESAMPLE(): TerminalNode | undefined;
    KW_SYSTEM(): TerminalNode | undefined;
    LPAREN(): TerminalNode[];
    LPAREN(i: number): TerminalNode;
    number(): NumberContext[];
    number(i: number): NumberContext;
    RPAREN(): TerminalNode[];
    RPAREN(i: number): TerminalNode;
    KW_REPEATABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ComputeIncrementalStatsContext extends ParserRuleContext {
    KW_COMPUTE(): TerminalNode;
    KW_INCREMENTAL(): TerminalNode;
    KW_STATS(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_PARTITION(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DropStatementContext extends ParserRuleContext {
    dropRole(): DropRoleContext | undefined;
    dropFunction(): DropFunctionContext | undefined;
    dropIncrementalStats(): DropIncrementalStatsContext | undefined;
    dropView(): DropViewContext | undefined;
    dropTable(): DropTableContext | undefined;
    dropSchema(): DropSchemaContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DropSchemaContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    databaseNamePath(): DatabaseNamePathContext;
    KW_SCHEMA(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    ifExists(): IfExistsContext | undefined;
    KW_CASCADE(): TerminalNode | undefined;
    KW_RESTRICT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DropViewContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewNamePath(): ViewNamePathContext;
    ifExists(): IfExistsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DropTableContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    ifExists(): IfExistsContext | undefined;
    KW_PURGE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DropIncrementalStatsContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_STATS(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_INCREMENTAL(): TerminalNode | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DropFunctionContext extends ParserRuleContext {
    KW_DROP(): TerminalNode;
    KW_FUNCTION(): TerminalNode;
    functionNamePath(): FunctionNamePathContext;
    KW_AGGREGATE(): TerminalNode | undefined;
    ifExists(): IfExistsContext | undefined;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    type(): TypeContext[];
    type(i: number): TypeContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DropRoleContext extends ParserRuleContext {
    _name: IdentifierContext;
    KW_DROP(): TerminalNode;
    KW_ROLE(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class GrantStatementContext extends ParserRuleContext {
    grantRole(): GrantRoleContext | undefined;
    grant(): GrantContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class GrantRoleContext extends ParserRuleContext {
    KW_GRANT(): TerminalNode;
    KW_ROLE(): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    KW_TO(): TerminalNode;
    KW_GROUP(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class GrantContext extends ParserRuleContext {
    _grantee: PrincipalContext;
    KW_GRANT(): TerminalNode;
    privilege(): PrivilegeContext;
    KW_ON(): TerminalNode;
    objectType(): ObjectTypeContext;
    KW_TO(): TerminalNode;
    principal(): PrincipalContext;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RevokeStatementContext extends ParserRuleContext {
    revokeRole(): RevokeRoleContext | undefined;
    revoke(): RevokeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RevokeRoleContext extends ParserRuleContext {
    KW_REVOKE(): TerminalNode;
    KW_ROLE(): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    KW_FROM(): TerminalNode;
    KW_GROUP(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RevokeContext extends ParserRuleContext {
    _grantee: PrincipalContext;
    KW_REVOKE(): TerminalNode;
    privilege(): PrivilegeContext;
    KW_ON(): TerminalNode;
    objectType(): ObjectTypeContext;
    KW_FROM(): TerminalNode;
    identifier(): IdentifierContext | undefined;
    KW_GRANT(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_FOR(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    principal(): PrincipalContext | undefined;
    KW_ROLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class InsertStatementContext extends ParserRuleContext {
    KW_INSERT(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    queryStatement(): QueryStatementContext;
    KW_INTO(): TerminalNode | undefined;
    KW_OVERWRITE(): TerminalNode | undefined;
    with(): WithContext | undefined;
    KW_TABLE(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    RPAREN(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DeleteStatementContext extends ParserRuleContext {
    delete(): DeleteContext | undefined;
    deleteTableRef(): DeleteTableRefContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DeleteContext extends ParserRuleContext {
    KW_DELETE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_FROM(): TerminalNode | undefined;
    KW_WHERE(): TerminalNode | undefined;
    booleanExpression(): BooleanExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DeleteTableRefContext extends ParserRuleContext {
    KW_DELETE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_FROM(): TerminalNode;
    identifier(): IdentifierContext | undefined;
    relation(): RelationContext[];
    relation(i: number): RelationContext;
    KW_WHERE(): TerminalNode | undefined;
    booleanExpression(): BooleanExpressionContext | undefined;
    KW_AS(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class UpdateStatementContext extends ParserRuleContext {
    KW_UPDATE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_SET(): TerminalNode;
    assignmentList(): AssignmentListContext;
    KW_FROM(): TerminalNode | undefined;
    relation(): RelationContext[];
    relation(i: number): RelationContext;
    KW_WHERE(): TerminalNode | undefined;
    booleanExpression(): BooleanExpressionContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class UpsertStatementContext extends ParserRuleContext {
    KW_UPSERT(): TerminalNode;
    KW_INTO(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    queryStatement(): QueryStatementContext;
    KW_TABLE(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowStatementContext extends ParserRuleContext {
    showRoles(): ShowRolesContext | undefined;
    showRoleGrant(): ShowRoleGrantContext | undefined;
    showGrants(): ShowGrantsContext | undefined;
    showFiles(): ShowFilesContext | undefined;
    showPartitions(): ShowPartitionsContext | undefined;
    showColumnStats(): ShowColumnStatsContext | undefined;
    showTableStats(): ShowTableStatsContext | undefined;
    showCreateView(): ShowCreateViewContext | undefined;
    showCreateTable(): ShowCreateTableContext | undefined;
    showFunctions(): ShowFunctionsContext | undefined;
    showTables(): ShowTablesContext | undefined;
    showSchemas(): ShowSchemasContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowSchemasContext extends ParserRuleContext {
    _pattern: StringLiteralContext;
    KW_SHOW(): TerminalNode;
    KW_SCHEMAS(): TerminalNode | undefined;
    KW_DATABASES(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext[];
    stringLiteral(i: number): StringLiteralContext;
    KW_LIKE(): TerminalNode | undefined;
    BITWISEOR(): TerminalNode[];
    BITWISEOR(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowTablesContext extends ParserRuleContext {
    _pattern: StringLiteralContext;
    KW_SHOW(): TerminalNode;
    KW_TABLES(): TerminalNode;
    KW_IN(): TerminalNode | undefined;
    tableNamePath(): TableNamePathContext | undefined;
    stringLiteral(): StringLiteralContext[];
    stringLiteral(i: number): StringLiteralContext;
    KW_LIKE(): TerminalNode | undefined;
    BITWISEOR(): TerminalNode[];
    BITWISEOR(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowFunctionsContext extends ParserRuleContext {
    _pattern: StringLiteralContext;
    KW_SHOW(): TerminalNode;
    KW_FUNCTIONS(): TerminalNode;
    KW_IN(): TerminalNode | undefined;
    databaseNamePath(): DatabaseNamePathContext | undefined;
    KW_AGGREGATE(): TerminalNode | undefined;
    KW_ANALYTIC(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext[];
    stringLiteral(i: number): StringLiteralContext;
    KW_LIKE(): TerminalNode | undefined;
    BITWISEOR(): TerminalNode[];
    BITWISEOR(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowCreateTableContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_CREATE(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowCreateViewContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_CREATE(): TerminalNode;
    KW_VIEW(): TerminalNode;
    viewNamePath(): ViewNamePathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowTableStatsContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_TABLE(): TerminalNode;
    KW_STATS(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowColumnStatsContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_COLUMN(): TerminalNode;
    KW_STATS(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowPartitionsContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_PARTITIONS(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_RANGE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowFilesContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_FILES(): TerminalNode;
    KW_IN(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_PARTITION(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    RPAREN(): TerminalNode | undefined;
    COMMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowRolesContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_ROLES(): TerminalNode;
    KW_CURRENT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowRoleGrantContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_ROLE(): TerminalNode;
    KW_GRANT(): TerminalNode;
    KW_GROUP(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowGrantsContext extends ParserRuleContext {
    showDatabaseGrant(): ShowDatabaseGrantContext | undefined;
    showTableGrant(): ShowTableGrantContext | undefined;
    showColumnGrant(): ShowColumnGrantContext | undefined;
    KW_SHOW(): TerminalNode | undefined;
    KW_GRANT(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_SERVER(): TerminalNode | undefined;
    KW_URI(): TerminalNode | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowDatabaseGrantContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_GRANT(): TerminalNode;
    identifier(): IdentifierContext;
    KW_USER(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    databaseNamePath(): DatabaseNamePathContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowTableGrantContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_GRANT(): TerminalNode;
    identifier(): IdentifierContext;
    KW_USER(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    tableNamePath(): TableNamePathContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShowColumnGrantContext extends ParserRuleContext {
    KW_SHOW(): TerminalNode;
    KW_GRANT(): TerminalNode;
    identifier(): IdentifierContext;
    KW_USER(): TerminalNode | undefined;
    KW_ROLE(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_ON(): TerminalNode | undefined;
    KW_COLUMN(): TerminalNode | undefined;
    columnNamePath(): ColumnNamePathContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AddCommentStatementContext extends ParserRuleContext {
    addDatabaseComments(): AddDatabaseCommentsContext | undefined;
    addTableComments(): AddTableCommentsContext | undefined;
    addColumnComments(): AddColumnCommentsContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AddDatabaseCommentsContext extends ParserRuleContext {
    KW_COMMENT(): TerminalNode;
    KW_ON(): TerminalNode;
    KW_DATABASE(): TerminalNode;
    databaseNamePath(): DatabaseNamePathContext;
    KW_IS(): TerminalNode;
    stringLiteral(): StringLiteralContext | undefined;
    KW_NULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AddTableCommentsContext extends ParserRuleContext {
    KW_COMMENT(): TerminalNode;
    KW_ON(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_IS(): TerminalNode;
    stringLiteral(): StringLiteralContext | undefined;
    KW_NULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AddColumnCommentsContext extends ParserRuleContext {
    KW_COMMENT(): TerminalNode;
    KW_ON(): TerminalNode;
    KW_COLUMN(): TerminalNode;
    columnNamePath(): ColumnNamePathContext;
    KW_IS(): TerminalNode;
    stringLiteral(): StringLiteralContext | undefined;
    KW_NULL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ExplainStatementContext extends ParserRuleContext {
    KW_EXPLAIN(): TerminalNode;
    sqlStatement(): SqlStatementContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SetStatementContext extends ParserRuleContext {
    KW_SET(): TerminalNode;
    KW_ALL(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    EQ(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ShutdownStatementContext extends ParserRuleContext {
    COLON(): TerminalNode;
    KW_SHUTDOWN(): TerminalNode;
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    stringLiteral(): StringLiteralContext | undefined;
    expression(): ExpressionContext | undefined;
    COMMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class InvalidateMetaStatementContext extends ParserRuleContext {
    KW_INVALIDATE(): TerminalNode;
    KW_METADATA(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class LoadDataStatementContext extends ParserRuleContext {
    KW_LOAD(): TerminalNode;
    KW_DATA(): TerminalNode;
    KW_INPATH(): TerminalNode;
    STRING(): TerminalNode;
    KW_INTO(): TerminalNode;
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_OVERWRITE(): TerminalNode | undefined;
    KW_PARTITION(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    RPAREN(): TerminalNode | undefined;
    COMMA(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RefreshStatementContext extends ParserRuleContext {
    refreshMeta(): RefreshMetaContext | undefined;
    refreshAuth(): RefreshAuthContext | undefined;
    refreshFunction(): RefreshFunctionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RefreshMetaContext extends ParserRuleContext {
    KW_REFRESH(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_PARTITION(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    RPAREN(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RefreshAuthContext extends ParserRuleContext {
    KW_REFRESH(): TerminalNode;
    KW_AUTHORIZATION(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RefreshFunctionContext extends ParserRuleContext {
    KW_REFRESH(): TerminalNode;
    KW_FUNCTIONS(): TerminalNode;
    functionNamePath(): FunctionNamePathContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class IfExistsContext extends ParserRuleContext {
    KW_IF(): TerminalNode;
    KW_EXISTS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class IfNotExistsContext extends ParserRuleContext {
    KW_IF(): TerminalNode;
    KW_NOT(): TerminalNode;
    KW_EXISTS(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class TableNameCreateContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DatabaseNameCreateContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ViewNameCreateContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class FunctionNameCreateContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ColumnNamePathCreateContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DatabaseNamePathContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class TableNamePathContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ViewNamePathContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class FunctionNamePathContext extends ParserRuleContext {
    reservedKeywordsUsedAsFuncName(): ReservedKeywordsUsedAsFuncNameContext | undefined;
    qualifiedName(): QualifiedNameContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ColumnNamePathContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class TableOrViewPathContext extends ParserRuleContext {
    tableNamePath(): TableNamePathContext | undefined;
    viewNamePath(): ViewNamePathContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CreateCommonItemContext extends ParserRuleContext {
    _comment: StringLiteralContext;
    _serdProp: PropertiesContext;
    _location: StringLiteralContext;
    _cacheName: QualifiedNameContext;
    _tblProp: PropertiesContext;
    KW_SORT(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    KW_COMMENT(): TerminalNode | undefined;
    KW_ROW(): TerminalNode | undefined;
    KW_FORMAT(): TerminalNode | undefined;
    rowFormat(): RowFormatContext | undefined;
    KW_WITH(): TerminalNode[];
    KW_WITH(i: number): TerminalNode;
    KW_SERDEPROPERTIES(): TerminalNode | undefined;
    KW_STORED(): TerminalNode | undefined;
    KW_AS(): TerminalNode | undefined;
    fileFormat(): FileFormatContext | undefined;
    KW_LOCATION(): TerminalNode | undefined;
    KW_CACHED(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    KW_UNCACHED(): TerminalNode | undefined;
    KW_TBLPROPERTIES(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext[];
    stringLiteral(i: number): StringLiteralContext;
    properties(): PropertiesContext[];
    properties(i: number): PropertiesContext;
    qualifiedName(): QualifiedNameContext | undefined;
    KW_REPLICATION(): TerminalNode | undefined;
    EQ(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AssignmentListContext extends ParserRuleContext {
    assignmentItem(): AssignmentItemContext[];
    assignmentItem(i: number): AssignmentItemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AssignmentItemContext extends ParserRuleContext {
    qualifiedName(): QualifiedNameContext;
    EQ(): TerminalNode;
    expression(): ExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ViewColumnsContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    columnNamePath(): ColumnNamePathContext;
    RPAREN(): TerminalNode;
    KW_COMMENT(): TerminalNode[];
    KW_COMMENT(i: number): TerminalNode;
    stringLiteral(): StringLiteralContext[];
    stringLiteral(i: number): StringLiteralContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class QueryStatementContext extends ParserRuleContext {
    queryNoWith(): QueryNoWithContext;
    with(): WithContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class WithContext extends ParserRuleContext {
    KW_WITH(): TerminalNode;
    namedQuery(): NamedQueryContext[];
    namedQuery(i: number): NamedQueryContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ConstraintSpecificationContext extends ParserRuleContext {
    KW_PRIMARY(): TerminalNode;
    KW_KEY(): TerminalNode;
    columnAliases(): ColumnAliasesContext;
    KW_DISABLE(): TerminalNode | undefined;
    KW_NOVALIDATE(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_RELY(): TerminalNode | undefined;
    foreignKeySpecification(): ForeignKeySpecificationContext[];
    foreignKeySpecification(i: number): ForeignKeySpecificationContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ForeignKeySpecificationContext extends ParserRuleContext {
    KW_FOREIGN(): TerminalNode;
    KW_KEY(): TerminalNode;
    columnAliases(): ColumnAliasesContext[];
    columnAliases(i: number): ColumnAliasesContext;
    KW_REFERENCES(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    KW_DISABLE(): TerminalNode | undefined;
    KW_NOVALIDATE(): TerminalNode | undefined;
    KW_RELY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ColumnDefinitionContext extends ParserRuleContext {
    columnNamePathCreate(): ColumnNamePathCreateContext;
    type(): TypeContext;
    KW_COMMENT(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class KuduTableElementContext extends ParserRuleContext {
    kuduColumnDefinition(): KuduColumnDefinitionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class KuduColumnDefinitionContext extends ParserRuleContext {
    columnNamePathCreate(): ColumnNamePathCreateContext;
    type(): TypeContext;
    kuduAttributes(): KuduAttributesContext[];
    kuduAttributes(i: number): KuduAttributesContext;
    KW_COMMENT(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    KW_PRIMARY(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ColumnSpecWithKuduContext extends ParserRuleContext {
    columnNamePath(): ColumnNamePathContext;
    type(): TypeContext;
    KW_COMMENT(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    kuduAttributes(): KuduAttributesContext[];
    kuduAttributes(i: number): KuduAttributesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CreateColumnSpecWithKuduContext extends ParserRuleContext {
    columnNamePathCreate(): ColumnNamePathCreateContext;
    type(): TypeContext;
    KW_COMMENT(): TerminalNode | undefined;
    stringLiteral(): StringLiteralContext | undefined;
    kuduAttributes(): KuduAttributesContext[];
    kuduAttributes(i: number): KuduAttributesContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class KuduAttributesContext extends ParserRuleContext {
    KW_NULL(): TerminalNode | undefined;
    kuduStorageAttr(): KuduStorageAttrContext | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class KuduStorageAttrContext extends ParserRuleContext {
    KW_ENCODING(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    KW_COMPRESSION(): TerminalNode | undefined;
    KW_DEFAULT(): TerminalNode | undefined;
    KW_BLOCK_SIZE(): TerminalNode | undefined;
    number(): NumberContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class StatsKeyContext extends ParserRuleContext {
    STATS_NUMDVS(): TerminalNode | undefined;
    STATS_NUMNULLS(): TerminalNode | undefined;
    STATS_AVGSIZE(): TerminalNode | undefined;
    STATS_MAXSIZE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class FileFormatContext extends ParserRuleContext {
    KW_TEXTFILE(): TerminalNode | undefined;
    KW_PARQUET(): TerminalNode | undefined;
    KW_ORC(): TerminalNode | undefined;
    KW_AVRO(): TerminalNode | undefined;
    KW_SEQUENCEFILE(): TerminalNode | undefined;
    KW_RCFILE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class KuduPartitionClauseContext extends ParserRuleContext {
    hashClause(): HashClauseContext[];
    hashClause(i: number): HashClauseContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    rangeClause(): RangeClauseContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class HashClauseContext extends ParserRuleContext {
    KW_HASH(): TerminalNode;
    KW_PARTITIONS(): TerminalNode;
    number(): NumberContext;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RangeClauseContext extends ParserRuleContext {
    KW_RANGE(): TerminalNode;
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    KW_PARTITION(): TerminalNode[];
    KW_PARTITION(i: number): TerminalNode;
    kuduPartitionSpec(): KuduPartitionSpecContext[];
    kuduPartitionSpec(i: number): KuduPartitionSpecContext;
    columnAliases(): ColumnAliasesContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class KuduPartitionSpecContext extends ParserRuleContext {
    KW_VALUE(): TerminalNode | undefined;
    partitionCol(): PartitionColContext | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_VALUES(): TerminalNode | undefined;
    rangeOperator(): RangeOperatorContext[];
    rangeOperator(i: number): RangeOperatorContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CacheSpecContext extends ParserRuleContext {
    KW_CACHED(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    KW_WITH(): TerminalNode | undefined;
    KW_REPLICATION(): TerminalNode | undefined;
    EQ(): TerminalNode | undefined;
    number(): NumberContext | undefined;
    KW_UNCACHED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RangeOperatorContext extends ParserRuleContext {
    LT(): TerminalNode | undefined;
    LTE(): TerminalNode | undefined;
    GT(): TerminalNode | undefined;
    GTE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class PartitionColContext extends ParserRuleContext {
    EQ(): TerminalNode | undefined;
    NEQ(): TerminalNode | undefined;
    KW_LIKE(): TerminalNode | undefined;
    KW_RLIKE(): TerminalNode | undefined;
    KW_REGEXP(): TerminalNode | undefined;
    KW_BETWEEN(): TerminalNode | undefined;
    KW_IN(): TerminalNode | undefined;
    rangeOperator(): RangeOperatorContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class LikeClauseContext extends ParserRuleContext {
    _optionType: Token;
    KW_LIKE(): TerminalNode;
    qualifiedName(): QualifiedNameContext;
    KW_PROPERTIES(): TerminalNode | undefined;
    KW_INCLUDING(): TerminalNode | undefined;
    KW_EXCLUDING(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class PropertiesContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    property(): PropertyContext[];
    property(i: number): PropertyContext;
    RPAREN(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class PartitionedByContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    columnDefinition(): ColumnDefinitionContext[];
    columnDefinition(i: number): ColumnDefinitionContext;
    RPAREN(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SortedByContext extends ParserRuleContext {
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RowFormatContext extends ParserRuleContext {
    KW_DELIMITED(): TerminalNode;
    KW_FIELDS(): TerminalNode | undefined;
    KW_TERMINATED(): TerminalNode[];
    KW_TERMINATED(i: number): TerminalNode;
    KW_BY(): TerminalNode[];
    KW_BY(i: number): TerminalNode;
    stringLiteral(): StringLiteralContext[];
    stringLiteral(i: number): StringLiteralContext;
    KW_LINES(): TerminalNode | undefined;
    KW_ESCAPED(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class PropertyContext extends ParserRuleContext {
    identifier(): IdentifierContext;
    EQ(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class QueryNoWithContext extends ParserRuleContext {
    _rows: ExpressionContext;
    _offset: Token;
    queryTerm(): QueryTermContext;
    KW_ORDER(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    KW_LIMIT(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_OFFSET(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class QueryTermContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: QueryTermContext): void;
}
export declare class QueryTermDefaultContext extends QueryTermContext {
    queryPrimary(): QueryPrimaryContext;
    constructor(ctx: QueryTermContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SetOperationContext extends QueryTermContext {
    _left: QueryTermContext;
    _operator: Token;
    _right: QueryTermContext;
    queryTerm(): QueryTermContext[];
    queryTerm(i: number): QueryTermContext;
    KW_INTERSECT(): TerminalNode | undefined;
    setQuantifier(): SetQuantifierContext | undefined;
    KW_UNION(): TerminalNode | undefined;
    KW_EXCEPT(): TerminalNode | undefined;
    constructor(ctx: QueryTermContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class QueryPrimaryContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: QueryPrimaryContext): void;
}
export declare class QueryPrimaryDefaultContext extends QueryPrimaryContext {
    querySpecification(): QuerySpecificationContext;
    constructor(ctx: QueryPrimaryContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class TableContext extends QueryPrimaryContext {
    KW_TABLE(): TerminalNode;
    tableNamePath(): TableNamePathContext;
    constructor(ctx: QueryPrimaryContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class InlineTableContext extends QueryPrimaryContext {
    KW_VALUES(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: QueryPrimaryContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SubqueryContext extends QueryPrimaryContext {
    LPAREN(): TerminalNode;
    queryNoWith(): QueryNoWithContext;
    RPAREN(): TerminalNode;
    constructor(ctx: QueryPrimaryContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SortItemContext extends ParserRuleContext {
    _ordering: Token;
    _nullOrdering: Token;
    columnItem(): ColumnItemContext;
    KW_NULLS(): TerminalNode | undefined;
    KW_ASC(): TerminalNode | undefined;
    KW_DESC(): TerminalNode | undefined;
    KW_FIRST(): TerminalNode | undefined;
    KW_LAST(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class QuerySpecificationContext extends ParserRuleContext {
    _where: BooleanExpressionContext;
    _having: BooleanExpressionContext;
    KW_SELECT(): TerminalNode;
    selectItem(): SelectItemContext[];
    selectItem(i: number): SelectItemContext;
    setQuantifier(): SetQuantifierContext | undefined;
    KW_STRAIGHT_JOIN(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_FROM(): TerminalNode | undefined;
    relation(): RelationContext[];
    relation(i: number): RelationContext;
    KW_WHERE(): TerminalNode | undefined;
    KW_GROUP(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    groupBy(): GroupByContext | undefined;
    KW_HAVING(): TerminalNode | undefined;
    booleanExpression(): BooleanExpressionContext[];
    booleanExpression(i: number): BooleanExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class GroupByContext extends ParserRuleContext {
    groupingElement(): GroupingElementContext[];
    groupingElement(i: number): GroupingElementContext;
    setQuantifier(): SetQuantifierContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class GroupingElementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: GroupingElementContext): void;
}
export declare class SingleGroupingSetContext extends GroupingElementContext {
    groupingSet(): GroupingSetContext;
    constructor(ctx: GroupingElementContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class GroupingSetContext extends ParserRuleContext {
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    columnItem(): ColumnItemContext[];
    columnItem(i: number): ColumnItemContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class NamedQueryContext extends ParserRuleContext {
    _name: IdentifierContext;
    KW_AS(): TerminalNode;
    subQueryRelation(): SubQueryRelationContext;
    identifier(): IdentifierContext;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SetQuantifierContext extends ParserRuleContext {
    KW_DISTINCT(): TerminalNode | undefined;
    KW_ALL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SelectItemContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: SelectItemContext): void;
}
export declare class SelectSingleContext extends SelectItemContext {
    columnItem(): ColumnItemContext;
    identifier(): IdentifierContext | undefined;
    KW_AS(): TerminalNode | undefined;
    constructor(ctx: SelectItemContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SelectAllContext extends SelectItemContext {
    qualifiedName(): QualifiedNameContext | undefined;
    DOT(): TerminalNode | undefined;
    ASTERISK(): TerminalNode;
    constructor(ctx: SelectItemContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RelationContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: RelationContext): void;
}
export declare class JoinRelationContext extends RelationContext {
    _left: RelationContext;
    _right: SampledRelationContext;
    _rightRelation: RelationContext;
    relation(): RelationContext[];
    relation(i: number): RelationContext;
    KW_CROSS(): TerminalNode | undefined;
    KW_JOIN(): TerminalNode | undefined;
    joinType(): JoinTypeContext | undefined;
    joinCriteria(): JoinCriteriaContext | undefined;
    sampledRelation(): SampledRelationContext | undefined;
    constructor(ctx: RelationContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RelationDefaultContext extends RelationContext {
    sampledRelation(): SampledRelationContext;
    constructor(ctx: RelationContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class JoinTypeContext extends ParserRuleContext {
    KW_INNER(): TerminalNode | undefined;
    KW_LEFT(): TerminalNode | undefined;
    KW_RIGHT(): TerminalNode | undefined;
    KW_OUTER(): TerminalNode | undefined;
    KW_FULL(): TerminalNode | undefined;
    KW_SEMI(): TerminalNode | undefined;
    KW_ANTI(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class JoinCriteriaContext extends ParserRuleContext {
    KW_ON(): TerminalNode | undefined;
    booleanExpression(): BooleanExpressionContext | undefined;
    KW_USING(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    RPAREN(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SampledRelationContext extends ParserRuleContext {
    _percentage: ExpressionContext;
    _seed: ExpressionContext;
    aliasedRelation(): AliasedRelationContext;
    KW_TABLESAMPLE(): TerminalNode | undefined;
    sampleType(): SampleTypeContext | undefined;
    LPAREN(): TerminalNode[];
    LPAREN(i: number): TerminalNode;
    RPAREN(): TerminalNode[];
    RPAREN(i: number): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_REPEATABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SampleTypeContext extends ParserRuleContext {
    KW_BERNOULLI(): TerminalNode | undefined;
    KW_SYSTEM(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class AliasedRelationContext extends ParserRuleContext {
    relationPrimary(): RelationPrimaryContext;
    identifier(): IdentifierContext | undefined;
    KW_AS(): TerminalNode | undefined;
    columnAliases(): ColumnAliasesContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ColumnAliasesContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    columnNamePath(): ColumnNamePathContext[];
    columnNamePath(i: number): ColumnNamePathContext;
    RPAREN(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CreateColumnAliasesContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    columnNamePathCreate(): ColumnNamePathCreateContext[];
    columnNamePathCreate(i: number): ColumnNamePathCreateContext;
    RPAREN(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RelationPrimaryContext extends ParserRuleContext {
    tableOrViewPath(): TableOrViewPathContext | undefined;
    subQueryRelation(): SubQueryRelationContext | undefined;
    KW_LATERAL(): TerminalNode | undefined;
    unnest(): UnnestContext | undefined;
    parenthesizedRelation(): ParenthesizedRelationContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SubQueryRelationContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    queryStatement(): QueryStatementContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class UnnestContext extends ParserRuleContext {
    KW_UNNEST(): TerminalNode;
    LPAREN(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    RPAREN(): TerminalNode;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_WITH(): TerminalNode | undefined;
    KW_ORDINALITY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ParenthesizedRelationContext extends ParserRuleContext {
    LPAREN(): TerminalNode;
    relation(): RelationContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ColumnItemContext extends ParserRuleContext {
    columnNamePath(): ColumnNamePathContext | undefined;
    expression(): ExpressionContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ExpressionContext extends ParserRuleContext {
    booleanExpression(): BooleanExpressionContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class BooleanExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: BooleanExpressionContext): void;
}
export declare class PredicatedContext extends BooleanExpressionContext {
    _valueExpression: ValueExpressionContext;
    valueExpression(): ValueExpressionContext;
    predicate(): PredicateContext | undefined;
    constructor(ctx: BooleanExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class LogicalNotContext extends BooleanExpressionContext {
    KW_NOT(): TerminalNode;
    booleanExpression(): BooleanExpressionContext;
    constructor(ctx: BooleanExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
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
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class PredicateContext extends ParserRuleContext {
    value: ParserRuleContext;
    constructor(parent: ParserRuleContext | undefined, invokingState: number, value: ParserRuleContext);
    get ruleIndex(): number;
    copyFrom(ctx: PredicateContext): void;
}
export declare class ComparisonContext extends PredicateContext {
    _right: ValueExpressionContext;
    comparisonOperator(): ComparisonOperatorContext;
    valueExpression(): ValueExpressionContext;
    constructor(ctx: PredicateContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class QuantifiedComparisonContext extends PredicateContext {
    comparisonOperator(): ComparisonOperatorContext;
    comparisonQuantifier(): ComparisonQuantifierContext;
    subQueryRelation(): SubQueryRelationContext;
    constructor(ctx: PredicateContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class BetweenContext extends PredicateContext {
    _lower: ValueExpressionContext;
    _upper: ValueExpressionContext;
    KW_BETWEEN(): TerminalNode;
    KW_AND(): TerminalNode;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class InListContext extends PredicateContext {
    KW_IN(): TerminalNode;
    LPAREN(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    RPAREN(): TerminalNode;
    KW_NOT(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: PredicateContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class InSubqueryContext extends PredicateContext {
    KW_IN(): TerminalNode;
    subQueryRelation(): SubQueryRelationContext;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class LikeContext extends PredicateContext {
    _pattern: ValueExpressionContext;
    _escape: ValueExpressionContext;
    KW_LIKE(): TerminalNode | undefined;
    KW_ILIKE(): TerminalNode | undefined;
    KW_RLIKE(): TerminalNode | undefined;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    KW_NOT(): TerminalNode | undefined;
    KW_ESCAPE(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class REGEXPContext extends PredicateContext {
    _pattern: ValueExpressionContext;
    KW_REGEXP(): TerminalNode | undefined;
    KW_IREGEXP(): TerminalNode | undefined;
    valueExpression(): ValueExpressionContext;
    constructor(ctx: PredicateContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class NullOrUnKnownOrBooleanPredicateContext extends PredicateContext {
    KW_IS(): TerminalNode;
    KW_NULL(): TerminalNode | undefined;
    KW_UNKNOWN(): TerminalNode | undefined;
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DistinctFromContext extends PredicateContext {
    _right: ValueExpressionContext;
    KW_IS(): TerminalNode;
    KW_DISTINCT(): TerminalNode;
    KW_FROM(): TerminalNode;
    valueExpression(): ValueExpressionContext;
    KW_NOT(): TerminalNode | undefined;
    constructor(ctx: PredicateContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ValueExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: ValueExpressionContext): void;
}
export declare class ValueExpressionDefaultContext extends ValueExpressionContext {
    primaryExpression(): PrimaryExpressionContext;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ArithmeticUnaryContext extends ValueExpressionContext {
    _operator: Token;
    valueExpression(): ValueExpressionContext;
    MINUS(): TerminalNode | undefined;
    PLUS(): TerminalNode | undefined;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ArithmeticBinaryContext extends ValueExpressionContext {
    _left: ValueExpressionContext;
    _operator: Token;
    _right: ValueExpressionContext;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    ASTERISK(): TerminalNode | undefined;
    SLASH(): TerminalNode | undefined;
    PERCENT(): TerminalNode | undefined;
    PLUS(): TerminalNode | undefined;
    MINUS(): TerminalNode | undefined;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ConcatenationContext extends ValueExpressionContext {
    _left: ValueExpressionContext;
    _right: ValueExpressionContext;
    CONCAT(): TerminalNode;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    constructor(ctx: ValueExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class PrimaryExpressionContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PrimaryExpressionContext): void;
}
export declare class NullLiteralContext extends PrimaryExpressionContext {
    KW_NULL(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class IntervalLiteralContext extends PrimaryExpressionContext {
    interval(): IntervalContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class TypeConstructorContext extends PrimaryExpressionContext {
    identifier(): IdentifierContext | undefined;
    stringLiteral(): StringLiteralContext;
    DOUBLE_PRECISION(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class NumericLiteralContext extends PrimaryExpressionContext {
    number(): NumberContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class BooleanLiteralContext extends PrimaryExpressionContext {
    booleanValue(): BooleanValueContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class StringLiteralValuesContext extends PrimaryExpressionContext {
    stringLiteral(): StringLiteralContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class BinaryLiteralContext extends PrimaryExpressionContext {
    BINARY_LITERAL(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ParameterContext extends PrimaryExpressionContext {
    QUESTION(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class PositionContext extends PrimaryExpressionContext {
    KW_POSITION(): TerminalNode;
    LPAREN(): TerminalNode;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    KW_IN(): TerminalNode;
    RPAREN(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class RowConstructorContext extends PrimaryExpressionContext {
    LPAREN(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    RPAREN(): TerminalNode;
    KW_AS(): TerminalNode[];
    KW_AS(i: number): TerminalNode;
    type(): TypeContext[];
    type(i: number): TypeContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_ROW(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class FunctionCallContext extends PrimaryExpressionContext {
    functionNamePath(): FunctionNamePathContext;
    LPAREN(): TerminalNode;
    ASTERISK(): TerminalNode | undefined;
    RPAREN(): TerminalNode;
    filter(): FilterContext | undefined;
    over(): OverContext | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    KW_ORDER(): TerminalNode | undefined;
    KW_BY(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    setQuantifier(): SetQuantifierContext | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class LambdaContext extends PrimaryExpressionContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    RIGHT_ARROW(): TerminalNode;
    expression(): ExpressionContext;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SubqueryExpressionContext extends PrimaryExpressionContext {
    LPAREN(): TerminalNode;
    queryStatement(): QueryStatementContext;
    RPAREN(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ExistsContext extends PrimaryExpressionContext {
    KW_EXISTS(): TerminalNode;
    LPAREN(): TerminalNode;
    queryStatement(): QueryStatementContext;
    RPAREN(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SimpleCaseContext extends PrimaryExpressionContext {
    _elseExpression: ExpressionContext;
    KW_CASE(): TerminalNode;
    valueExpression(): ValueExpressionContext;
    KW_END(): TerminalNode;
    whenClause(): WhenClauseContext[];
    whenClause(i: number): WhenClauseContext;
    KW_ELSE(): TerminalNode | undefined;
    expression(): ExpressionContext | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
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
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CastContext extends PrimaryExpressionContext {
    KW_CAST(): TerminalNode | undefined;
    LPAREN(): TerminalNode;
    expression(): ExpressionContext;
    KW_AS(): TerminalNode;
    type(): TypeContext;
    RPAREN(): TerminalNode;
    KW_TRY_CAST(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ArrayConstructorContext extends PrimaryExpressionContext {
    KW_ARRAY(): TerminalNode;
    LSQUARE(): TerminalNode;
    RSQUARE(): TerminalNode;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SubscriptContext extends PrimaryExpressionContext {
    _value: PrimaryExpressionContext;
    _index: ValueExpressionContext;
    LSQUARE(): TerminalNode;
    RSQUARE(): TerminalNode;
    primaryExpression(): PrimaryExpressionContext;
    valueExpression(): ValueExpressionContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ColumnReferenceContext extends PrimaryExpressionContext {
    identifier(): IdentifierContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DereferenceContext extends PrimaryExpressionContext {
    _base: PrimaryExpressionContext;
    _fieldName: IdentifierContext;
    DOT(): TerminalNode;
    primaryExpression(): PrimaryExpressionContext;
    identifier(): IdentifierContext;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SpecialDateTimeFunctionContext extends PrimaryExpressionContext {
    _name: Token;
    _precision: Token;
    KW_CURRENT_DATE(): TerminalNode | undefined;
    KW_CURRENT_TIME(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    INTEGER_VALUE(): TerminalNode | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    KW_LOCALTIME(): TerminalNode | undefined;
    KW_LOCALTIMESTAMP(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CurrentUserContext extends PrimaryExpressionContext {
    _name: Token;
    KW_CURRENT_USER(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CurrentPathContext extends PrimaryExpressionContext {
    _name: Token;
    KW_CURRENT_PATH(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class SubstringContext extends PrimaryExpressionContext {
    KW_SUBSTRING(): TerminalNode;
    LPAREN(): TerminalNode;
    valueExpression(): ValueExpressionContext[];
    valueExpression(i: number): ValueExpressionContext;
    KW_FROM(): TerminalNode;
    RPAREN(): TerminalNode;
    KW_FOR(): TerminalNode | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class NormalizeContext extends PrimaryExpressionContext {
    KW_NORMALIZE(): TerminalNode;
    LPAREN(): TerminalNode;
    valueExpression(): ValueExpressionContext;
    RPAREN(): TerminalNode;
    COMMA(): TerminalNode | undefined;
    normalForm(): NormalFormContext | undefined;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ExtractContext extends PrimaryExpressionContext {
    KW_EXTRACT(): TerminalNode;
    LPAREN(): TerminalNode;
    identifier(): IdentifierContext;
    KW_FROM(): TerminalNode;
    valueExpression(): ValueExpressionContext;
    RPAREN(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ParenthesizedExpressionContext extends PrimaryExpressionContext {
    LPAREN(): TerminalNode;
    expression(): ExpressionContext;
    RPAREN(): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class GroupingOperationContext extends PrimaryExpressionContext {
    KW_GROUPING(): TerminalNode;
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    qualifiedName(): QualifiedNameContext[];
    qualifiedName(i: number): QualifiedNameContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(ctx: PrimaryExpressionContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class StringLiteralContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: StringLiteralContext): void;
}
export declare class BasicStringLiteralContext extends StringLiteralContext {
    STRING(): TerminalNode;
    constructor(ctx: StringLiteralContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class UnicodeStringLiteralContext extends StringLiteralContext {
    UNICODE_STRING(): TerminalNode;
    KW_UESCAPE(): TerminalNode | undefined;
    STRING(): TerminalNode | undefined;
    constructor(ctx: StringLiteralContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ComparisonOperatorContext extends ParserRuleContext {
    EQ(): TerminalNode | undefined;
    NEQ(): TerminalNode | undefined;
    LT(): TerminalNode | undefined;
    LTE(): TerminalNode | undefined;
    GT(): TerminalNode | undefined;
    GTE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ComparisonQuantifierContext extends ParserRuleContext {
    KW_ALL(): TerminalNode | undefined;
    KW_SOME(): TerminalNode | undefined;
    KW_ANY(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class BooleanValueContext extends ParserRuleContext {
    KW_TRUE(): TerminalNode | undefined;
    KW_FALSE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class IntervalContext extends ParserRuleContext {
    INTEGER_VALUE(): TerminalNode;
    intervalField(): IntervalFieldContext;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    KW_INTERVAL(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class IntervalFieldContext extends ParserRuleContext {
    KW_YEAR(): TerminalNode | undefined;
    KW_YEARS(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_MONTHS(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_DAYS(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_HOURS(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    KW_MINUTES(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    KW_SECONDS(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class NormalFormContext extends ParserRuleContext {
    KW_NFD(): TerminalNode | undefined;
    KW_NFC(): TerminalNode | undefined;
    KW_NFKD(): TerminalNode | undefined;
    KW_NFKC(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class TypeContext extends ParserRuleContext {
    type(): TypeContext[];
    type(i: number): TypeContext;
    KW_ARRAY(): TerminalNode | undefined;
    LT(): TerminalNode | undefined;
    GT(): TerminalNode | undefined;
    KW_MAP(): TerminalNode | undefined;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    KW_STRUCT(): TerminalNode | undefined;
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    COLON(): TerminalNode[];
    COLON(i: number): TerminalNode;
    baseType(): BaseTypeContext | undefined;
    LPAREN(): TerminalNode | undefined;
    typeParameter(): TypeParameterContext[];
    typeParameter(i: number): TypeParameterContext;
    RPAREN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class TypeParameterContext extends ParserRuleContext {
    INTEGER_VALUE(): TerminalNode | undefined;
    type(): TypeContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class BaseTypeContext extends ParserRuleContext {
    TIME_WITH_TIME_ZONE(): TerminalNode | undefined;
    TIMESTAMP_WITH_TIME_ZONE(): TerminalNode | undefined;
    DOUBLE_PRECISION(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
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
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class FilterContext extends ParserRuleContext {
    KW_FILTER(): TerminalNode;
    LPAREN(): TerminalNode;
    KW_WHERE(): TerminalNode;
    booleanExpression(): BooleanExpressionContext;
    RPAREN(): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class OverContext extends ParserRuleContext {
    _expression: ExpressionContext;
    _partition: ExpressionContext[];
    KW_OVER(): TerminalNode;
    LPAREN(): TerminalNode;
    RPAREN(): TerminalNode;
    KW_PARTITION(): TerminalNode | undefined;
    KW_BY(): TerminalNode[];
    KW_BY(i: number): TerminalNode;
    KW_ORDER(): TerminalNode | undefined;
    sortItem(): SortItemContext[];
    sortItem(i: number): SortItemContext;
    windowFrame(): WindowFrameContext | undefined;
    expression(): ExpressionContext[];
    expression(i: number): ExpressionContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class WindowFrameContext extends ParserRuleContext {
    _frameType: Token;
    _start_: FrameBoundContext;
    _end: FrameBoundContext;
    KW_RANGE(): TerminalNode | undefined;
    frameBound(): FrameBoundContext[];
    frameBound(i: number): FrameBoundContext;
    KW_ROWS(): TerminalNode | undefined;
    KW_BETWEEN(): TerminalNode | undefined;
    KW_AND(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class FrameBoundContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: FrameBoundContext): void;
}
export declare class UnboundedFrameContext extends FrameBoundContext {
    _boundType: Token;
    KW_UNBOUNDED(): TerminalNode;
    KW_PRECEDING(): TerminalNode | undefined;
    KW_FOLLOWING(): TerminalNode | undefined;
    constructor(ctx: FrameBoundContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class CurrentRowBoundContext extends FrameBoundContext {
    KW_CURRENT(): TerminalNode;
    KW_ROW(): TerminalNode;
    constructor(ctx: FrameBoundContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class BoundedFrameContext extends FrameBoundContext {
    _boundType: Token;
    expression(): ExpressionContext;
    KW_PRECEDING(): TerminalNode | undefined;
    KW_FOLLOWING(): TerminalNode | undefined;
    constructor(ctx: FrameBoundContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class PathElementContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PathElementContext): void;
}
export declare class QualifiedArgumentContext extends PathElementContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    DOT(): TerminalNode;
    constructor(ctx: PathElementContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class UnqualifiedArgumentContext extends PathElementContext {
    identifier(): IdentifierContext;
    constructor(ctx: PathElementContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class PathSpecificationContext extends ParserRuleContext {
    pathElement(): PathElementContext[];
    pathElement(i: number): PathElementContext;
    COMMA(): TerminalNode[];
    COMMA(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class PrivilegeContext extends ParserRuleContext {
    _columnName: IdentifierContext;
    KW_ALL(): TerminalNode | undefined;
    KW_ALTER(): TerminalNode | undefined;
    KW_DROP(): TerminalNode | undefined;
    KW_CREATE(): TerminalNode | undefined;
    KW_INSERT(): TerminalNode | undefined;
    KW_REFRESH(): TerminalNode | undefined;
    KW_SELECT(): TerminalNode | undefined;
    LPAREN(): TerminalNode | undefined;
    RPAREN(): TerminalNode | undefined;
    identifier(): IdentifierContext | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ObjectTypeContext extends ParserRuleContext {
    KW_SERVER(): TerminalNode | undefined;
    KW_URI(): TerminalNode | undefined;
    KW_DATABASE(): TerminalNode | undefined;
    KW_TABLE(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class QualifiedNameContext extends ParserRuleContext {
    identifier(): IdentifierContext[];
    identifier(i: number): IdentifierContext;
    DOT(): TerminalNode[];
    DOT(i: number): TerminalNode;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class PrincipalContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: PrincipalContext): void;
}
export declare class RolePrincipalContext extends PrincipalContext {
    KW_ROLE(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(ctx: PrincipalContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class UserPrincipalContext extends PrincipalContext {
    KW_USER(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(ctx: PrincipalContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class GroupPrincipalContext extends PrincipalContext {
    KW_GROUP(): TerminalNode;
    identifier(): IdentifierContext;
    constructor(ctx: PrincipalContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class IdentifierContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: IdentifierContext): void;
}
export declare class UnquotedIdentifierContext extends IdentifierContext {
    IDENTIFIER(): TerminalNode | undefined;
    nonReserved(): NonReservedContext | undefined;
    constructor(ctx: IdentifierContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class QuotedIdentifierContext extends IdentifierContext {
    STRING(): TerminalNode;
    constructor(ctx: IdentifierContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class BackQuotedIdentifierContext extends IdentifierContext {
    BACKQUOTED_IDENTIFIER(): TerminalNode;
    constructor(ctx: IdentifierContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DigitIdentifierContext extends IdentifierContext {
    DIGIT_IDENTIFIER(): TerminalNode;
    constructor(ctx: IdentifierContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class NumberContext extends ParserRuleContext {
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    copyFrom(ctx: NumberContext): void;
}
export declare class DecimalLiteralContext extends NumberContext {
    DECIMAL_VALUE(): TerminalNode;
    MINUS(): TerminalNode | undefined;
    constructor(ctx: NumberContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class DoubleLiteralContext extends NumberContext {
    DOUBLE_VALUE(): TerminalNode;
    MINUS(): TerminalNode | undefined;
    constructor(ctx: NumberContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class IntegerLiteralContext extends NumberContext {
    INTEGER_VALUE(): TerminalNode;
    MINUS(): TerminalNode | undefined;
    constructor(ctx: NumberContext);
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class ReservedKeywordsUsedAsFuncNameContext extends ParserRuleContext {
    KW_TRUNCATE(): TerminalNode | undefined;
    KW_CAST(): TerminalNode | undefined;
    KW_CURRENT_DATE(): TerminalNode | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_EXTRACT(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    KW_YEAR(): TerminalNode | undefined;
    KW_CASE(): TerminalNode | undefined;
    KW_LEFT(): TerminalNode | undefined;
    KW_REPLACE(): TerminalNode | undefined;
    KW_RIGHT(): TerminalNode | undefined;
    KW_SUBSTRING(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
export declare class NonReservedContext extends ParserRuleContext {
    KW_BERNOULLI(): TerminalNode | undefined;
    KW_DAY(): TerminalNode | undefined;
    KW_DAYS(): TerminalNode | undefined;
    KW_EXCLUDING(): TerminalNode | undefined;
    KW_HOUR(): TerminalNode | undefined;
    KW_INCLUDING(): TerminalNode | undefined;
    KW_MINUTE(): TerminalNode | undefined;
    KW_MINUTES(): TerminalNode | undefined;
    KW_MONTH(): TerminalNode | undefined;
    KW_MONTHS(): TerminalNode | undefined;
    KW_NFC(): TerminalNode | undefined;
    KW_NFD(): TerminalNode | undefined;
    KW_NFKC(): TerminalNode | undefined;
    KW_NFKD(): TerminalNode | undefined;
    KW_OPTION(): TerminalNode | undefined;
    KW_ORDINALITY(): TerminalNode | undefined;
    KW_PRIVILEGES(): TerminalNode | undefined;
    KW_PROPERTIES(): TerminalNode | undefined;
    KW_SECOND(): TerminalNode | undefined;
    KW_SECONDS(): TerminalNode | undefined;
    KW_SUBSTRING(): TerminalNode | undefined;
    KW_SYSTEM(): TerminalNode | undefined;
    KW_TRY_CAST(): TerminalNode | undefined;
    KW_USER(): TerminalNode | undefined;
    KW_VIEWS(): TerminalNode | undefined;
    KW_YEAR(): TerminalNode | undefined;
    KW_ORC(): TerminalNode | undefined;
    KW_CURRENT_TIMESTAMP(): TerminalNode | undefined;
    KW_CURRENT_USER(): TerminalNode | undefined;
    KW_EXTRACT(): TerminalNode | undefined;
    KW_KEY(): TerminalNode | undefined;
    KW_LOCALTIME(): TerminalNode | undefined;
    KW_SHUTDOWN(): TerminalNode | undefined;
    constructor(parent: ParserRuleContext | undefined, invokingState: number);
    get ruleIndex(): number;
    enterRule(listener: ImpalaSqlParserListener): void;
    exitRule(listener: ImpalaSqlParserListener): void;
    accept<Result>(visitor: ImpalaSqlParserVisitor<Result>): Result;
}
